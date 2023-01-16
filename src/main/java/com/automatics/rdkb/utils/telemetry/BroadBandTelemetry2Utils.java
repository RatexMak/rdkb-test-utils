/*
 * Copyright 2021 Comcast Cable Communications Management, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.automatics.rdkb.utils.telemetry;

import java.util.Collection;
import java.util.Iterator;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Dut;
import com.automatics.enums.ExecutionStatus;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;
import com.automatics.rdkb.utils.SplunkUtils;
import com.automatics.rdkb.utils.dmcli.DmcliUtils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;

public class BroadBandTelemetry2Utils {
    private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandTelemetry2Utils.class);

    /**
     * Method to validate factory default settings of telemetry 2.0
     * 
     * @param device
     *            {@link Instanceof Dut}
     * @return Validation result
     */

    public static boolean validateFactoryDefaultsForTelemetryParams(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug(" STARTING METHOD: validateFactoryDefaultsForTelemetryParams");
	boolean result = false;
	try {
	    String response = null;
	    for (BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS telesetting : BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS
		    .values()) {
		response = tapEnv.executeWebPaCommand(device, telesetting.getParam());
		result = response != null && response.trim().equals(telesetting.getFactoryDefault());
		if (!result) {
		    break;
		}
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception occurred while trying to validate factory default settings", e);
	}
	LOGGER.debug(" ENDING METHOD: validateFactoryDefaultsForTelemetryParams");
	return result;
    }

    /**
     * Method to validate if telemetry parameters can be get/set via webpa or dmcli
     * 
     * @param device
     *            {@link Instanceof Dut}
     * @param isDmcli
     *            toggle parameter for query method
     * @return validation result
     * 
     * @author Sathurya Ravi
     * @refactor yamini.s
     */
    public static boolean validateIfTelemetryParamsCanBeSetViaWebpaOrDmcli(Dut device, boolean isDmcli,
	    AutomaticsTapApi tapEnv) {
	boolean result = false;
	try {
	    String response = null;
	    for (BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS telesetting : BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS
		    .values()) {
		result = isDmcli
			? DmcliUtils.setWebPaParameterValueUsingDmcliCommand(device, tapEnv, telesetting.getParam(),
				telesetting.getParam()
					.equals(BroadBandWebPaConstants.WEBPA_PARAM_FOR_TELEMETRY_2_0_ENABLE)
						? WebPaDataTypes.BOOLEAN.getValue()
						: WebPaDataTypes.STRING.getValue(),
				telesetting.getDefaultT2Config())
			: BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv, telesetting.getParam(),
				telesetting.getParam()
					.equals(BroadBandWebPaConstants.WEBPA_PARAM_FOR_TELEMETRY_2_0_ENABLE)
						? WebPaDataTypes.BOOLEAN.getValue()
						: WebPaDataTypes.STRING.getValue(),
				telesetting.getDefaultT2Config());
		if (!result) {
		    break;
		}
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception occurred while trying to validate factory default settings", e);
	}
	LOGGER.debug(" ENDING METHOD: retrieveTelemetryLogsFromSplunk");
	return result;
    }

    /**
     * Method to verify Self Heal process in Telemetry 2.0
     * 
     * @param device
     *            {@link Instanceof Dut}
     * 
     * @return Result
     * 
     * @author Sathurya Ravi
     * @refactor yamini.s
     */
    public static boolean verifySelfHealKicksStartsTelemetry2Process(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug(" STARTING METHOD: verifySelfHealKicksStartsTelemetry2Process");
	boolean result = false;
	String response = null;
	if (BroadBandCommonUtils.killAndVerifyProcess(device, tapEnv,
		BroadBandTestConstants.PROCESS_NAME_TELEMETRY_2_0)) {
	    long startTime = System.currentTimeMillis();
	    do {
		response = tapEnv.executeCommandUsingSsh(device,
			BroadBandTestConstants.CMD_CHECK_SELF_HEAL_LOGS_FOR_TELEMETRY2_CRASH);
		result = CommonMethods.isNotNull(response)
			&& response.contains(BroadBandTestConstants.STRING_NEED_RESTART)
			&& BroadBandCommonUtils.verifyProcessRunningStatus(device, tapEnv, false,
				BroadBandTestConstants.PROCESS_NAME_TELEMETRY_2_0);
	    } while (!result
		    && (System.currentTimeMillis() - startTime) <= BroadBandTestConstants.THIRTY_MINUTES_IN_MILLIS
		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
	}
	LOGGER.debug(" ENDING METHOD: verifySelfHealKicksStartsTelemetry2Process");
	return result;
    }

    /**
     * Method to execute common steps for telemetry ver one fall back
     * 
     * @param device
     *            {@link Instanceof Dut}
     * @param tapEnv
     *            {@link Instanceof AutomaticsTapApi}
     * @param stepNumber
     *            stepnumber to start with
     * @param testCaseId
     *            Testcase ID to update
     */
    public static void executeTestStepsForTelemetryVersionOne(Dut device, AutomaticsTapApi tapEnv, int stepNumber,
	    String testCaseId) {
	String stepNum = null;
	boolean status = false;
	String errorMessage = null;
	String response = null;

	try {

	    /**
	     * VALIDATE IF INFO AND ERROR MESSAGES ARE PRESENT IN /RDKLOGS/LOGS/TELEMETRY2_0.TXT.0
	     */
	    stepNum = "S" + stepNumber;
	    errorMessage = "The INFO and ERROR messages are not found under /rdklogs/logs/telemetry2_0.txt.0";
	    long startTime = System.currentTimeMillis();
	    LOGGER.info("**********************************************************************************");
	    LOGGER.info("STEP " + stepNumber
		    + " :DESCRIPTION : Validate if INFO and ERROR messages are present in /rdklogs/logs/telemetry2_0.txt.0 ");
	    LOGGER.info("STEP " + stepNumber + " :ACTION : Execute the command cat /rdklogs/logs/telemetry2_0.txt.0 ");
	    LOGGER.info("STEP " + stepNumber + " : EXPECTED : The INFO and ERROR messages should be present ");
	    LOGGER.info("**********************************************************************************");

	    do {
		response = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.CMD_GET_TELEMETRY_VER_TWO_LOGS);
		status = CommonMethods.isNotNull(response) && CommonMethods.patternMatcher(response,
			BroadBandTestConstants.STRING_FOR_INFO_WARN_ERROR_MESSAGE);
	    } while (!status && (System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS
		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));

	    if (status) {
		LOGGER.info("STEP " + stepNum
			+ ": ACTUAL : The INFO and ERROR messages are found under /rdklogs/logs/telemetry2_0.txt.0 ");
	    } else {
		LOGGER.error("STEP " + stepNum + ": ACTUAL : " + errorMessage);
	    }
	    LOGGER.info("**********************************************************************************");
	    tapEnv.updateExecutionStatus(device, testCaseId, stepNum, status, errorMessage, false);

	    /**
	     * VALIDATE IF DCA_UTILITY.SH IS NOT LISTED UNDER CRON JOBS
	     */
	    ++stepNumber;
	    stepNum = "S" + stepNumber;
	    errorMessage = "The dca_utility.sh is present under cron jobs";
	    startTime = System.currentTimeMillis();
	    LOGGER.info("**********************************************************************************");
	    LOGGER.info(
		    "STEP " + stepNumber + " :DESCRIPTION : Validate if dca_utility.sh is not listed under cron jobs ");
	    LOGGER.info("STEP " + stepNumber
		    + " :ACTION : Execute the command cat /var/spool/cron/crontabs/root|grep -i dca_utility ");
	    LOGGER.info("STEP " + stepNumber + " : EXPECTED : The dca_utility should not be present under cronjobs ");
	    LOGGER.info("**********************************************************************************");

	    do {
		if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
		    response = tapEnv.executeCommandOnAtom(device, BroadBandCommandConstants.CMD_GET_CRON_JOBS_ATOM);
		} else {
		    response = tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_GET_CRON_JOBS);
		}
		status = !CommonMethods.isNotNull(response)
			&& !response.contains(BroadBandTestConstants.FILE_NAME_DCA_UTILITY);
	    } while (!status && (System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS
		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));

	    if (status) {
		LOGGER.info("STEP " + stepNum + ": ACTUAL : The file dca_utility.sh is not found under cron jobs ");
	    } else {
		LOGGER.error("STEP " + stepNum + ": ACTUAL : " + errorMessage);
	    }
	    LOGGER.info("**********************************************************************************");
	    tapEnv.updateExecutionStatus(device, testCaseId, stepNum, status, errorMessage, false);

	    /**
	     * DISABLE THE TELEMETRY 2.0 VIA WEBPA AND REBOOT
	     */
	    ++stepNumber;
	    stepNum = "S" + stepNumber;
	    errorMessage = "The parameters for telemetry version 1 has not been set successfully";
	    LOGGER.info("**********************************************************************************");
	    LOGGER.info("STEP " + stepNumber + " :DESCRIPTION : Disable the telemetry 2.0 via webpa and reboot ");
	    LOGGER.info("STEP " + stepNumber + " :ACTION : Set the telemetry 2.0 parameters to deafault values ");
	    LOGGER.info("STEP " + stepNumber + " : EXPECTED : The parameters should get set successfully ");
	    LOGGER.info("**********************************************************************************");

	    status = setTelemetryVerOneConfigurationViaWebpa(device, tapEnv);

	    if (status) {
		CommonMethods.rebootAndWaitForIpAccusition(device, tapEnv);
		LOGGER.info("STEP " + stepNum + ": ACTUAL : Telemetry 1.0 configs are set successfully on the device");
	    } else {
		LOGGER.error("STEP " + stepNum + ": ACTUAL : " + errorMessage);
	    }
	    LOGGER.info("**********************************************************************************");
	    tapEnv.updateExecutionStatus(device, testCaseId, stepNum, status, errorMessage, true);

	    /**
	     * VERIFY IF THE 'TELEMETRY2_0' PROCESS IS NOT UP AFTER ENABLING TELEMETRY 2.0
	     */
	    ++stepNumber;
	    stepNum = "S" + stepNumber;
	    errorMessage = "The process for telemetry 2 is running on the gateway";
	    LOGGER.info("**********************************************************************************");
	    LOGGER.info("STEP " + stepNumber
		    + " : DESCRIPTION : Verify if the 'telemetry2_0' process is up after enabling telemetry 2.0 ");
	    LOGGER.info("STEP " + stepNumber + " : ACTION : Execute the command 'ps -ww | grep telemetry2_0 ");
	    LOGGER.info("STEP " + stepNumber + " : EXPECTED : The process should be up and running. ");
	    LOGGER.info("**********************************************************************************");

	    status = !BroadBandCommonUtils.verifyProcessRunningStatus(device, tapEnv, false,
		    BroadBandTestConstants.PROCESS_NAME_TELEMETRY_2_0);

	    if (status) {
		LOGGER.info(
			"STEP " + stepNum + ": ACTUAL : Telemetry 2.0 process is not up and running on the gateway");
	    } else {
		LOGGER.error("STEP " + stepNum + ": ACTUAL : " + errorMessage);
	    }
	    LOGGER.info("**********************************************************************************");
	    tapEnv.updateExecutionStatus(device, testCaseId, stepNum, status, errorMessage, true);

	    /**
	     * VALIDATE IF DCA_UTILITY.SH IS LISTED UNDER CRON JOBS
	     */
	    ++stepNumber;
	    stepNum = "S" + stepNumber;
	    errorMessage = "The dca_utility.sh is not present under cron jobs";
	    startTime = System.currentTimeMillis();
	    LOGGER.info("**********************************************************************************");
	    LOGGER.info("STEP " + stepNumber + " :DESCRIPTION : Validate if dca_utility.sh is listed under cron jobs ");
	    LOGGER.info("STEP " + stepNumber
		    + " :ACTION : Execute the command cat /var/spool/cron/crontabs/root|grep -i dca_utility ");
	    LOGGER.info("STEP " + stepNumber + " : EXPECTED : The dca_utility should be present under cronjobs ");
	    LOGGER.info("**********************************************************************************");

	    do {
		if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
		    response = tapEnv.executeCommandOnAtom(device, BroadBandCommandConstants.CMD_GET_CRON_JOBS_ATOM);
		} else {
		    response = tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_GET_CRON_JOBS);
		}
		status = CommonMethods.isNotNull(response)
			&& response.contains(BroadBandTestConstants.FILE_NAME_DCA_UTILITY);
	    } while (!status && (System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS
		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));

	    if (status) {
		LOGGER.info("STEP " + stepNum + ": ACTUAL : The file dca_utility.sh is found under cron jobs ");
	    } else {
		LOGGER.error("STEP " + stepNum + ": ACTUAL : " + errorMessage);
	    }
	    LOGGER.info("**********************************************************************************");
	    tapEnv.updateExecutionStatus(device, testCaseId, stepNum, status, errorMessage, false);

	    /**
	     * VALIDATE THE PRESENCE OF LOGS IN DCMSCRIPT.LOG
	     */
	    ++stepNumber;
	    stepNum = "S" + stepNumber;
	    errorMessage = "The logs are not found in dcmscript.log";
	    String timestamp = null;
	    startTime = System.currentTimeMillis();
	    LOGGER.info("**********************************************************************************");
	    LOGGER.info("STEP " + stepNumber + " :DESCRIPTION : Validate the presence of logs in dcmscript.log ");
	    LOGGER.info("STEP " + stepNumber
		    + " :ACTION : Execute the command cat dcmscript.log and verify the payload data using regex for time ");
	    LOGGER.info("STEP " + stepNumber + " : EXPECTED : The payload data should be present ");
	    LOGGER.info("**********************************************************************************");

	    do {
		response = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.CMD_CAT_RDKLOGS_DCMSCRIPT_LOG);
		if (CommonMethods.isNotNull(response) && CommonMethods.isSTBAccessible(device)) {
		    response = CommonMethods.patternFinder(response,
			    BroadBandTestConstants.REGEX_TIME_REPORTED_IN_TELEMETRY);
		    status = CommonMethods.isNotNull(response);
		    if (status) {
			timestamp = response;
		    }
		}
	    } while (!status && (System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS
		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));

	    if (status) {
		LOGGER.info("STEP " + stepNum + ": ACTUAL : The payload logs are present in dcmscript.log ");
	    } else {
		LOGGER.error("STEP " + stepNum + ": ACTUAL : " + errorMessage);
	    }
	    LOGGER.info("**********************************************************************************");
	    tapEnv.updateExecutionStatus(device, testCaseId, stepNum, status, errorMessage, true);

	    /**
	     * VALIDATE THE PRESENCE OF LOGS IN SPLUNK
	     */
	    boolean isSplunkEnabled = BroadbandPropertyFileHandler.isSplunkEnabled();
	    ++stepNumber;
	    stepNum = "S" + stepNumber;

	    if (isSplunkEnabled) {

		errorMessage = "The logs are not found in splunk";
		startTime = System.currentTimeMillis();
		LOGGER.info("**********************************************************************************");
		LOGGER.info("STEP " + stepNumber
			+ " :DESCRIPTION : Verify log upload in splunk by searching using estb mac ");
		LOGGER.info("STEP " + stepNumber + " :ACTION : Perform splunk search using the device's estb mac ");
		LOGGER.info("STEP " + stepNumber + " : EXPECTED : The payload data should be present in splunk ");
		LOGGER.info("**********************************************************************************");

		do {
		    response = retrieveTelemetryLogsFromSplunk(device, tapEnv, timestamp);
		    status = CommonMethods.isNotNull(response);
		} while (!status
			&& (System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS
			&& BroadBandCommonUtils.hasWaitForDuration(tapEnv,
				BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));

		if (status) {
		    LOGGER.info("STEP " + stepNum + ": ACTUAL : The payload logs are present in splunk ");
		} else {
		    LOGGER.error("STEP " + stepNum + ": ACTUAL : " + errorMessage);
		}
		LOGGER.info("**********************************************************************************");
		tapEnv.updateExecutionStatus(device, testCaseId, stepNum, status, errorMessage, false);
	    } else {
		LOGGER.info("Splunk is not enabled : Skipping corresponding steps");
		errorMessage = "Splunk is not enabled";
		tapEnv.updateExecutionForAllStatus(device, testCaseId, stepNum, ExecutionStatus.NOT_APPLICABLE,
			errorMessage, false);

	    }

	} catch (TestException e) {
	    LOGGER.error("Exception occured while executing common steps telemetry version one settings on gateway", e);
	}
    }

    /**
     * Utility method to set the telemetry 1 settings
     * 
     * @param device
     *            {@link Instanceof Dut}
     * @return setting result
     * 
     * @author Sathurya_R
     * @refactor said.h
     * 
     */
    public static boolean setTelemetryVerOneConfigurationViaWebpa(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug(" STARTING METHOD: setTelemetryVerOneConfigurationViaWebpa");
	boolean result = false;
	try {
	    result = BroadBandWebPaUtils.setParameterValuesUsingWebPaOrDmcli(device, tapEnv,
		    BroadBandWebPaConstants.WEBPA_PARAM_FOR_TELEMETRY_2_0_ENABLE, WebPaDataTypes.BOOLEAN.getValue(),
		    BroadBandTestConstants.FALSE)
		    && BroadBandWebPaUtils.setParameterValuesUsingWebPaOrDmcli(device, tapEnv,
			    BroadBandWebPaConstants.WEBPA_PARAM_FOR_TELEMETRY_2_0_CONFIG_URL,
			    WebPaDataTypes.STRING.getValue(), BroadBandTestConstants.SINGLE_SPACE_CHARACTER);
	    if (result) {
		LOGGER.info(
			"Telemetry 2.0 set to false and Config url to null successfully. Will verify telemetry version as 1.");
		result = BroadBandWebPaUtils.getAndVerifyWebpaValueInPolledDuration(device, tapEnv,
			BroadBandWebPaConstants.WEBPA_PARAM_FOR_TELEMETRY_2_0_VERSION,
			BroadBandTestConstants.STRING_VALUE_ONE, BroadBandTestConstants.TWO_MINUTE_IN_MILLIS,
			BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception occurred while setting T1 configurations", e);
	}
	LOGGER.debug(" ENDING METHOD: setTelemetryVerOneConfigurationViaWebpa");
	return result;
    }

    /**
     * Method to find telemetry 2 logs from splunk
     * 
     * @param device
     *            {@link Instanceof Dut}
     * @param uploadTime
     *            Telemetry upload time
     * @return Telemetry string from splunk
     * 
     * @author Sathurya Ravi
     * 
     */
    public static String retrieveTelemetryLogsFromSplunk(Dut device, AutomaticsTapApi tapEnv, String timeStamp) {
	LOGGER.debug(" STARTING METHOD: retrieveTelemetryLogsFromSplunk");
	String splunkLog = null;
	try {
	    // retrieving the value from splunk
	    String splunkSearchString = "index=rdk-json(" + device.getHostMacAddress() + ")";
	    Collection<String> splunkResponse = SplunkUtils.searchInSplunk(tapEnv, splunkSearchString, null, -1, "5m");
	    if (null != splunkResponse) {
		Iterator<String> iterator = splunkResponse.iterator();
		while (iterator.hasNext()) {
		    String telemetryDataFromSplunk = iterator.next();
		    LOGGER.info("########################### DATA from splunk = " + telemetryDataFromSplunk);
		    LOGGER.info("Matched data from splunk: " + telemetryDataFromSplunk);
		    splunkLog = telemetryDataFromSplunk;
		    if (CommonMethods.isNotNull(timeStamp) && CommonMethods.isNotNull(splunkLog)
			    && CommonMethods.patternMatcher(splunkLog, ".*" + timeStamp + ".*")) {
			break;
		    } else if (!CommonMethods.isNotNull(timeStamp) && CommonMethods.isNotNull(splunkLog)) {
			break;
		    } else {
			continue;
		    }
		}
	    } else {
		LOGGER.error("Received null response from splunk for search-" + splunkSearchString);
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception occured while trying to retrieve the logs from splunk");
	}
	LOGGER.debug(" ENDING METHOD: retrieveTelemetryLogsFromSplunk");
	return splunkLog;
    }

    /**
     * Utility method to validate T2 version parameter for different builds
     * 
     * @param device
     *            {@link Instanceof Dut}
     * @param tapEnv
     *            {@link Instanceof AutomaticsTapApi}
     * @return result
     * @refactor yamini.s
     * 
     */
    public static String validateT2Version(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug(" STARTING METHOD: T2VersionValidation");
	String result = null;
	// String buildType = null;
	boolean t2Enabled = false;
	String t2VersionStb = null;
	try {
	    String buildName = BroadBandCommonUtils.getCurrentlyRunningImageVersionUsingWebPaCommand(tapEnv, device);
	    // if (buildName.contains(BroadBandTestConstants.BUILD_TYPE_SPRINT)) {
	    // buildType = BroadBandTestConstants.BUILD_TYPE_SPRINT;
	    // } else if (buildName.contains(BroadBandTestConstants.STRING_FEATURE_IN_BUILD_NAME)) {
	    // buildType = BroadBandTestConstants.STRING_FEATURE_IN_BUILD_NAME;
	    // } else if (buildName.contains(BroadBandTestConstants.BUILD_TYPE_STABLE)) {
	    // buildType = BroadBandTestConstants.BUILD_TYPE_STABLE;
	    // } else {
	    // buildType = BroadBandTestConstants.BUILD_TYPE_RELEASE;
	    // }
	    t2Enabled = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcliAndVerify(device, tapEnv,
		    BroadBandWebPaConstants.WEBPA_PARAM_FOR_TELEMETRY_2_0_ENABLE, BroadBandTestConstants.TRUE);
	    if (t2Enabled) {
		t2VersionStb = AutomaticsTapApi.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_T2_ENABLED_VERSION);
	    } else {
		t2VersionStb = AutomaticsTapApi.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_T2_VERSION);
	    }
	    LOGGER.info("Telemetry2.0 Version constant in stb properties is :" + t2VersionStb);
	    JSONObject jsonObj = new JSONObject(t2VersionStb);
	    if (jsonObj != null) {
		LOGGER.info("Default t2 version defined in stb.properties is"
			+ jsonObj.getString(BroadBandTestConstants.DEFAULT));
		result = jsonObj.getString(BroadBandTestConstants.DEFAULT);

	    }
	} catch (Exception e) {
	    LOGGER.info("Exception occured T2VersionValidation " + e.getMessage());
	}
	LOGGER.debug(" ENDING METHOD: T2VersionValidation");
	return result;
    }

    /**
     * Utility method to check the telemetry 2 settings
     * 
     * @param device
     *            {@link Instanceof Dut}
     * @return validation result
     * 
     * @author Sathurya_R
     * @refactor yamini.s
     */
    public static boolean verifyTelemetry2ConfigurationViaWebpa(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug(" STARTING METHOD: verifyTelemetry2ConfigurationViaWebpa");
	boolean result = false;
	try {
	    result = false;
	    String response = null;
	    for (BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS telesetting : BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS
		    .values()) {
		response = tapEnv.executeWebPaCommand(device, telesetting.getParam());
		result = CommonMethods.isNotNull(response) && response.equals(telesetting.getDefaultT2Config());
		if (!result) {
		    break;
		}
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception occurred while validating T2 settings", e);
	}
	LOGGER.debug(" ENDING METHOD: verifyTelemetry2ConfigurationViaWebpa");
	return result;
    }
    
    /**
     * Utility method to set the telemetry 2 settings
     * 
     * @param device
     *            {@link Instanceof Dut}
     * @return setting result
     * 
     * @author Sathurya_R
     * 
     */
    public static boolean setTelemetry2ConfigurationViaWebpa(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug(" STARTING METHOD: setTelemetry2ConfigurationViaWebpa");
	boolean result = false;
	try {
	    result = false;
	    for (BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS telesetting : BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS
		    .values()) {
		result = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv, telesetting.getParam(),
			telesetting.getParam().equals(BroadBandWebPaConstants.WEBPA_PARAM_FOR_TELEMETRY_2_0_ENABLE)
				? WebPaDataTypes.BOOLEAN.getValue()
				: WebPaDataTypes.STRING.getValue(),
			telesetting.getDefaultT2Config());
		if (!result) {
		    break;
		}
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception occurred while setting T2 configurations", e);
	}
	LOGGER.debug(" ENDING METHOD: setTelemetry2ConfigurationViaWebpa");
	return result;
    }

}

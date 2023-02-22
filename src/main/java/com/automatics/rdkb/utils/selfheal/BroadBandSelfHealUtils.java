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
package com.automatics.rdkb.utils.selfheal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.constants.SnmpConstants;
import com.automatics.core.DeviceProcess;
import com.automatics.device.Device;
import com.automatics.device.Dut;
import com.automatics.enums.ProcessRestartOption;
import com.automatics.enums.StbProcess;
import com.automatics.exceptions.TestException;
import com.automatics.providers.crashanalysis.SettopCrashUtils;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpMib;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpUtils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.wifi.BroadBandWiFiUtils;
import com.automatics.snmp.SnmpDataType;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;

/**
 * Utils class to hold common APi's for self heal test scenarios
 * 
 * @author Gnanaprakasham.S
 *
 */
public class BroadBandSelfHealUtils {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BroadBandSelfHealUtils.class);

    /**
     * Method to verify logupload frequency on atom side
     * 
     * @param device
     *            instance
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param logUploadFrequency
     *            expected logUploadFrequency value
     * 
     * @return true atom has expected log uploaded frequency
     * @author Gnanaprakasham
     * @Refactor Sruthi Santhosh
     */
    public static boolean verifyLogUploadFrequencyOnAtomConsole(Dut device, AutomaticsTapApi tapEnv,
	    String logUploadFrequency) {
	LOGGER.debug("STARTING METHOD: verifyLogUploadFrequencyOnAtomConsole()");
	String response = null;
	String errorMessage = null;
	boolean status = false;

	response = CommonMethods.executeCommandInAtomConsole(device, tapEnv,
		BroadBandTestConstants.CMD_GET_LOG_UPLOAD_FREQUENCY_ATOM_CONSOLE);

	if (CommonMethods.isNotNull(response)) {

	    response = CommonMethods.patternFinder(response, BroadBandTestConstants.PATTERN_GET_LOG_UPLOAD_FREQUENCY);
	    if (CommonMethods.isNotNull(response)) {
		LOGGER.info("Obtained logupload frequecy on atom console : " + response);
		status = CommonMethods.isNotNull(response) && response.equalsIgnoreCase(logUploadFrequency);
	    } else {
		errorMessage = "Failed to get the log upload frequency from atom console by executing system commands";
		LOGGER.error(errorMessage);
		throw new TestException(errorMessage);
	    }

	} else {
	    errorMessage = "Obtained null response for executing system command \"cat /tmp/cron/root\"to get log upload frequency on atom console";
	    LOGGER.error(errorMessage);
	    throw new TestException(errorMessage);
	}
	LOGGER.debug("ENDING METHOD: verifyLogUploadFrequencyOnAtomConsole()");
	return status;
    }

    /**
     * Method to enable/disable diagnostic mode using webpa params
     * 
     * @param device
     *            instance
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param diagnosticModeFlag
     *            diagnosticModeFlag
     * @return true if diagnosticMode changed as per flag value
     * 
     * @author Gnanaprakasham
     * @Refactor Sruthi Santhosh
     */

    public static boolean enableOrDisableDiagnosticModeUsingWebpaParams(Dut device, AutomaticsTapApi tapEnv,
	    String diagnosticModeFlag) {
	LOGGER.debug("STARTING METHOD : enableOrDisableDiagnosticModeUsingWebpaParams");
	// Variable to store execution status
	boolean status = false;
	String errorMessage = null;

	try {
	    errorMessage = "Not able to enable diagnostic mode using webpa param \"Device.SelfHeal.X_RDKCENTRAL-COM_DiagnosticMode\" ";
	    status = BroadBandWiFiUtils.setWebPaParams(device, BroadBandWebPaConstants.WEBPA_PARAM_DIAGNOSTIC_MODE,
		    diagnosticModeFlag, WebPaDataTypes.BOOLEAN.getValue());
	} catch (Exception exception) {
	    errorMessage = "Exception occurred during execution : " + exception.getMessage();
	    LOGGER.error(errorMessage);
	}

	LOGGER.debug("ENDING METHOD : enableOrDisableDiagnosticModeUsingWebpaParams");
	return status;
    }

    /**
     * Method to initiate process crash and verify process restarted status
     * 
     * @param device
     *            Dut instance
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param stbProcess
     *            - Process name
     * @return true if process crash in successfully initiated
     * 
     * @author Sumathi Gunasekaran
     * @refactor Athira
     */
    public static boolean initiateProcessCrashAndVerifyProcessRestartedStatus(Dut device, AutomaticsTapApi tapEnv,
	    StbProcess stbProcess) {
	LOGGER.debug("STARTING METHOD : InitiateProcessCrashAndVerifyProcessRestartedStatus");
	// Variable to store process ID
	String processId = null;
	// Instance for settop crash utils
	final SettopCrashUtils settopCrashUtils = new SettopCrashUtils();
	// boolean variable to store status
	boolean status = false;
	// Variable to store command response
	String response = null;
	// Variable to store error message
	String errorMessage = null;
	DeviceProcess pardouseProcess = new DeviceProcess();
	pardouseProcess.setProcessName(stbProcess.getProcessName());

	try {

	    processId = CommonMethods.getPidOfProcess(device, tapEnv, stbProcess.getProcessName());

	    if (CommonMethods.isNull(processId) && CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
		// retrieve the PID form ATOM

		processId = BroadBandCommonUtils.getPidOfProcessFromAtomConsole(device, tapEnv,
			pardouseProcess.getProcessName());

		if (CommonMethods.isNotNull(processId)) {
		    LOGGER.info("SUCCESSFULLY RETRIEVED PROCESS ID FOR " + stbProcess.getProcessName() + " PROCESS is :"
			    + processId);

		} else {
		    errorMessage = "Not able to retrieve process ID for " + stbProcess.getProcessName()
			    + " process either from ARM/ATOM console";
		    LOGGER.error(errorMessage);
		    throw new TestException(errorMessage);
		}

	    } else if (CommonMethods.isNull(processId)) {
		errorMessage = "Not able to get the process ID for " + stbProcess.getProcessName()
			+ " process from ARM console";
		LOGGER.error(errorMessage);
		throw new TestException(errorMessage);
	    } else {
		LOGGER.info("SUCCESSFULLY RETRIEVED PROCESS ID FOR " + stbProcess.getProcessName() + " PROCESS");
	    }

	    // Self heal action will not restarted CR process after crash. instead it will
	    // reboot the device
	    // So only kill the current process id
	    if (stbProcess.getProcessName().equalsIgnoreCase(StbProcess.CCSP_CR.getProcessName())) {

		if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
		    response = tapEnv.executeCommandOnAtom(device,
			    ProcessRestartOption.KILL_11.getCommand() + processId);
		} else {
		    response = tapEnv.executeCommandUsingSsh(device,
			    ProcessRestartOption.KILL_11.getCommand() + processId);
		}
		LOGGER.info("Obtained response for process " + stbProcess.getProcessName() + "kill" + response);
		status = CommonMethods.isNull(response);
	    } else {
		// Self heal action will restart the process after crash (other than CR
		// process).
		// So verify process restarted status after crash
		status = restartProcess(device, tapEnv, ProcessRestartOption.KILL_11, stbProcess, processId);
	    }

	} catch (Exception exeception) {
	    throw new TestException(exeception.getMessage());
	}
	LOGGER.debug("ENDING METHOD : InitiateProcessCrashAndVerifyProcessRestartedStatus");
	return status;
    }

    /**
     * Method to restart the process
     * 
     * @param device
     *            Dut instance
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param option
     *            Restart option
     * @param stbProcess
     *            - Process name
     * @param processId
     *            Process id to to killed
     * @return true if process restarted successfully
     * 
     * @author Sumathi Gunasekaran
     * 
     */
    public static boolean restartProcess(Dut device, AutomaticsTapApi tapEnv, ProcessRestartOption option,
	    StbProcess processName, String processId) {
	LOGGER.debug("STARTING METHOD : restartProcess");
	// Receiver restart status
	boolean restartStatus = false;
	// loop count
	final int loopCount = 5;
	// New process id
	String newProcessId = null;
	// command to execute
	String command = null;
	String errorMessage = null;

	try {
	    if (option != null && CommonMethods.isNotNull(processId)) {

		command = option.getCommand() + processId;

		// kill the current running process
		tapEnv.executeCommandInSettopBox(device, command);

		for (int i = 0; i < loopCount; i++) {

		    // get the new process id
		    newProcessId = CommonMethods.getPidOfProcess(device, tapEnv, processName.getProcessName());

		    if (CommonMethods.isNotNull(newProcessId)) {

			if (processId.equals(newProcessId)) {
			    LOGGER.info("Unable to restart '" + processName + "' process. Retrying ...!!");
			} else {
			    LOGGER.info("Successfully restarted '" + processName + "' process.");
			    restartStatus = true;
			    break;
			}
		    } else {
			LOGGER.info(processName.getProcessName()
				+ " is not started after crash...waiting for 3 more minutes!!!!");
			tapEnv.waitTill(AutomaticsConstants.THREE_MINUTES);
		    }

		}
	    } else {
		LOGGER.error("Obtained process id/Process restart option is null for process crash !!!  ");
	    }
	} catch (Exception exception) {
	    errorMessage = "Exception occured during restart process " + processName.getProcessName() + " : "
		    + exception.getMessage();
	    LOGGER.error(errorMessage);
	}

	if (!restartStatus) {
	    errorMessage = "Self Heal action is not restarting the " + processName.getProcessName()
		    + " process even after 15 minutes of " + processName.getProcessName() + " process crash ";
	    LOGGER.error(errorMessage);
	    throw new TestException(errorMessage);
	}
	LOGGER.debug("ENDING METHOD : restartProcess");
	return restartStatus;

    }

    /**
     * Method for setting precondition for self heal crash scenarios
     */
    public static boolean executePreconditionForSelfHealTestScenario(Dut device, AutomaticsTapApi tapEnv) {

	boolean status = false;
	String response = null;
	boolean snmpstatus = false;
	/*
	 * we need to use web pa command else we can use the snmp command .The following command verifies if the device
	 * is DSL device or not.
	 */

	boolean isDSLDevice = DeviceModeHandler.isDSLDevice(device);
	if ((isDSLDevice || CommonMethods.isRunningEthwanMode())) {

	    status = BroadBandWebPaUtils.setAndVerifyParameterValuesUsingWebPaorDmcli(device, tapEnv,
		    BroadBandWebPaConstants.USAGE_COMPUTE_WINDOW, BroadBandTestConstants.CONSTANT_2,
		    BroadBandTestConstants.STRING_CONSTANT_3);

	} else {

	    if (BroadbandPropertyFileHandler.isDeviceCheckForSelfHeal2(device)) {
		BroadBandWebPaUtils.setVerifyWebPAInPolledDuration(device, tapEnv,
			BroadBandWebPaConstants.WEBPA_PARAM_AGGRESSIVE_SELFHEAL_INTERVAL,
			BroadBandTestConstants.CONSTANT_2, BroadBandTestConstants.STRING_CONSTANT_2,
			BroadBandTestConstants.ONE_MINUTE_IN_MILLIS, BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS);
	    }
	    long startTime = System.currentTimeMillis();
	    do {
				response = BroadBandSnmpUtils.retrieveSnmpSetOutputWithGivenIndexOnRdkDevices(device, tapEnv,
						BroadBandSnmpMib.ECM_SELFHEAL_RESOURCE_USAGE_COMPUTER_WINDOW.getOid(), SnmpDataType.INTEGER,
						BroadBandTestConstants.STRING_CONSTANT_3, BroadBandTestConstants.STRING_VALUE_ZERO);
				snmpstatus = BroadBandSnmpUtils.hasNoSNMPErrorOnResponse(tapEnv, device, response);
			} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.TEN_MINUTE_IN_MILLIS && !snmpstatus
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));

	    if (CommonMethods.isNotNull(response)) {

		status = response.equalsIgnoreCase(BroadBandTestConstants.STRING_CONSTANT_3);

	    }

	}

	if (!status) {
	    LOGGER.error("Failed to set the precondition for self heal crash scenario verification");
	}

	return status;
    }

    /**
     * Method to enable/disable diagnostic mode using snmp mib
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param diagnosticModeFlag
     *            diagnosticModeFlag
     * @return true if diagnosticMode changed as per falg value
     * 
     * @author Gnanaprakasham
     * 
     */
    public static boolean enableOrDisableDiagnosticModeUsingSnmpMib(Dut device, AutomaticsTapApi tapEnv, String value) {
	LOGGER.debug("STARTING METHOD : enableOrDisableDiagnosticModeUsingSnmpMib");
	// Variable to store execution status
	boolean status = false;
	String errorMessage = null;
	String mode = value.equals(BroadBandTestConstants.STRING_VALUE_ONE) ? BroadBandTestConstants.STRING_ENABLE
		: BroadBandTestConstants.STRING_DISABLE;

	errorMessage = "Not able to " + mode + " diagnostic mode using snmp mib \".1.3.6.1.4.1.17270.44.1.1.12.0\" ";
	String response = BroadBandSnmpUtils.retrieveSnmpSetOutputWithDefaultIndexOnRdkDevices(device, tapEnv,
		BroadBandSnmpMib.ECM_DIAGNOSTIC_MODE.getOid(), SnmpDataType.INTEGER, value);

	if (CommonMethods.isNotNull(response) && !response.contains(SnmpConstants.NO_SUCH_OID_RESPONSE)) {
	    LOGGER.info("Obtained response for snmp set for diagnostic mode : " + response);
	    status = response.equalsIgnoreCase(value);

	    if (!status) {
		errorMessage = "Not able to " + mode
			+ " diagnostic mode using snmp mib. expected response for snmp set :" + value
			+ " But actual value : " + response;
		throw new TestException(errorMessage);
	    }
	} else {
	    errorMessage = "Obtained null response/No such oid for " + mode
		    + " dignostic mode using snmp mib .1.3.6.1.4.1.17270.44.1.1.12.0 ";
	    LOGGER.error(errorMessage);
	    throw new TestException(errorMessage);
	}

	LOGGER.debug("ENDING METHOD : enableOrDisableDiagnosticModeUsingSnmpMib");
	return status;
    }

    /**
     * Method to verify log upload frequency value using SNMP mib
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param expectedValue
     *            expected vale for snmp get
     * 
     * @return true if snmp query returns expected value
     * @author gnanaprakasham.s
     */
    public static boolean verifyLogUploadFrequencyUsingSnmpMib(Dut device, AutomaticsTapApi tapEnv,
	    String expectedValue) {

	LOGGER.debug("STARTING METHOD: verifyLogUploadFrequencyUsingSnmpMib()");

	String errorMessage = null;
	String response = null;
	boolean status = false;
	response = BroadBandSnmpUtils.executeSnmpGetOnRdkDevices(tapEnv, device,
		BroadBandSnmpMib.ECM_LOG_UPLOAD_FREQUENCY.getOid());
	LOGGER.info("Obtained response for SNMP execution to get log upload frequency value : " + response);

	if (CommonMethods.isNotNull(response) && !response.contains(BroadBandTestConstants.NO_SUCH_INSTANCE)
		&& !response.contains(BroadBandTestConstants.NO_SUCH_OBJECT_AVAILABLE)) {
	    LOGGER.info("OBTAINED LOG UPLOAD FREQUENCY VALUE FOR SNMP QUERY IS  : " + response);
	    status = response.equalsIgnoreCase(expectedValue);

	    if (!status) {
		errorMessage = "Expected response for snmp query should be " + expectedValue + " But obtained value : "
			+ response;
		LOGGER.error(errorMessage);
		throw new TestException(errorMessage);
	    }
	} else {
	    errorMessage = "Obtained null response/No such oid exists during SNMP execution to get log upload frequency value ";
	    LOGGER.error(errorMessage);
	    throw new TestException(errorMessage);
	}
	LOGGER.debug("ENDING METHOD: verifyLogUploadFrequencyUsingSnmpMib()");
	return status;

    }

    /**
     * Method to configure log upload frequency using snmp mib
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param value
     *            value to be set
     * @return true if snmp set successful
     * 
     * @author Gnanaprakasham
     * 
     */

    public static boolean configureLogUploadFrequencyUsingSnmpMib(Dut device, AutomaticsTapApi tapEnv, String value) {
	LOGGER.debug("STARTING METHOD : configureLogUploadFrequencyUsingSnmpMib");
	// Variable to store execution status
	boolean status = false;
	String errorMessage = null;
	String response = null;

	response = BroadBandSnmpUtils.retrieveSnmpSetOutputWithDefaultIndexOnRdkDevices(device, tapEnv,
		BroadBandSnmpMib.ECM_LOG_UPLOAD_FREQUENCY.getOid(), SnmpDataType.UNSIGNED_INTEGER, value);

	if (CommonMethods.isNotNull(response) && !response.contains(SnmpConstants.NO_SUCH_OID_RESPONSE)) {
	    LOGGER.info("Obtained response for snmp set for log upload frequency : " + response);
	    status = response.equalsIgnoreCase(value);
	    LOGGER.info(" SUCCESSFULLY CONFIGURED LOG UPLOAD FREQUENCY USING SNMP MIB : " + status);
	} else {
	    errorMessage = "Obtained null response/No such oid exists during SNMP execution for configure log upload frequency ";
	    LOGGER.error(errorMessage);
	    throw new TestException(errorMessage);
	}

	LOGGER.debug("ENDING METHOD : configureLogUploadFrequencyUsingSnmpMib");
	return status;
    }

    /**
     * Method to verify expected process crash logs in self heal log file
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param searchString
     *            - Expected search string for process crash
     * @param stbProcess
     *            - Process name
     * @return true if expected logs present
     * 
     * @author Gnanaprakasham
     * @refactor Govardhan
     * 
     */
    public static boolean verifyProcessCrashLogsInSelfHealLogFile(Dut device, AutomaticsTapApi tapEnv,
	    String searchString, StbProcess stbProcess) {
	LOGGER.debug("STARTING METHOD : verifyProcessCrashLogsInSelfHealLogFile");
	String response = null;
	String errorMessage = null;
	boolean status = false;

	String command = null;

	if (CommonMethods.isAtomSyncAvailable(device, tapEnv) || DeviceModeHandler.isBusinessClassDevice(device)) {

	    command = BroadBandTestConstants.COMMAND_TO_GET_PROCESS_CRASH_LOGS;
	} else {
	    command = stbProcess.getProcessName().equalsIgnoreCase(StbProcess.CCSP_CR.getProcessName())
		    ? BroadBandTestConstants.COMMAND_TO_GET_PROCESS_CRASH_LOGS
		    : BroadBandTestConstants.COMMAND_TO_GET_SYSTEMD_PROCESS_CRASH_LOGS;
	}

	response = tapEnv.executeCommandUsingSsh(device, command);

	if (CommonMethods.isNotNull(response)) {

	    errorMessage = "Failed to get the process crash logs for " + stbProcess.getProcessName()
		    + "even after 15 minutes of process crash ";
	    if (stbProcess.getProcessName().equalsIgnoreCase(StbProcess.CCSP_CR.getProcessName())) {
		status = CommonMethods.isNotNull(response);
	    } else {
		status = response.contains(searchString);
		if ((CommonMethods.isAtomSyncAvailable(device, tapEnv)
			|| DeviceModeHandler.isBusinessClassDevice(device))
			&& stbProcess.getProcessName().equalsIgnoreCase(StbProcess.CCSP_PSM.getProcessName())) {
		    status = false;
		    LOGGER.info(
			    "Additional validation required for AtomSyncDevices and BusinessClass devices to check the vendor name and Model name in the crash logs");
		    // check the vendor name and Model name in the crash logs
		    String vendorName = CommonMethods.patternFinder(response,
			    BroadBandTestConstants.REGEX_VENDOR_NAME_PROCESS_CRASH_LOGS);
		    String modelName = CommonMethods.patternFinder(response,
			    BroadBandTestConstants.REGEX_MODEL_NAME_PROCESS_CRASH_LOGS.replace("ecmMac",
				    ((Device) device).getEcmMac().toLowerCase()));
		    if (CommonMethods.isNull(modelName)) {
			modelName = CommonMethods.patternFinder(response,
				BroadBandTestConstants.REGEX_MODEL_NAME_PROCESS_CRASH_LOGS.replace("ecmMac",
					((Device) device).getEcmMac().toUpperCase()));
		    }
		    LOGGER.info("*************************************************");
		    LOGGER.info("Vendor name from Crash logs :" + vendorName);
		    LOGGER.info("Model name from Crash logs :" + modelName);
		    LOGGER.info("*************************************************");
		    LOGGER.info("Model name from CATS: " + device.getModel());
		    LOGGER.info("Vendor name from CATS: " + device.getManufacturer());
		    LOGGER.info("*************************************************");
		    if (CommonMethods.isNotNull(modelName) && CommonMethods.isNotNull(vendorName)) {
			if (vendorName.trim().equalsIgnoreCase(device.getManufacturer())
				&& modelName.trim().equalsIgnoreCase(device.getModel())) {
			    status = true;
			} else {
			    LOGGER.error(
				    "Model name or vendor name obtained form Crash logs does not match the value from CATS. Vendor name from Crash logs :"
					    + vendorName + "Model name from Crash logs :" + modelName);
			    status = false;
			}

		    } else {
			LOGGER.error(
				"Either model name or vendor name obtained form Crash logs is null. Vendor name from Crash logs :"
					+ vendorName + "Model name from Crash logs :" + modelName);
			status = false;
		    }

		}
	    }
	    if (!status) {
		LOGGER.error(errorMessage);
		throw new TestException(errorMessage);
	    }

	} else {
	    errorMessage = "No information about Self Heal action is logged for " + stbProcess.getProcessName()
		    + " process crash in SelfHeal.txt.0 log file";
	    LOGGER.error(errorMessage);
	    throw new TestException(errorMessage);
	}
	LOGGER.debug("ENDING METHOD : verifyProcessCrashLogsInSelfHealLogFile");
	return status;
    }

    /**
     * Method to verify process restarted status by self heal action
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param stbProcess
     *            - Process name
     * @return true if process is restarted
     * 
     * @author Gnanaprakasham
     * @refactor Govardhan
     * 
     */
    public static boolean verifyProcessRestartedStatusAfterProcessCrash(Dut device, AutomaticsTapApi tapEnv,
	    StbProcess stbProcess) {
	LOGGER.debug("STARTING METHOD : verifyProcessRestartedStatusAfterProcessCrash");
	String response = null;
	boolean status = false;
	// loop count
	final int loopCount = 5;

	LOGGER.info("Waiting for 1 minutes to start all Ccsp components properly");
	tapEnv.waitTill(RDKBTestConstants.ONE_MINUTE_IN_MILLIS);

	// CR process will take more time for restart. So waiting for maximum 10 minutes only for CR process restart
	// For other process one minute wait time is enough
	if (stbProcess.getProcessName().equalsIgnoreCase(StbProcess.CCSP_CR.getProcessName())) {
	    for (int i = 0; i < loopCount; i++) {
		// CR process is taking more time for starting.. so waiting for maximum 10 minutes
		if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
		    response = BroadBandCommonUtils.getPidOfProcessFromAtomConsole(device, tapEnv,
			    stbProcess.getProcessName());
		} else {
		    response = CommonMethods.getPidOfProcess(device, tapEnv, stbProcess.getProcessName());
		}

		if (CommonMethods.isNotNull(response)) {
		    break;
		} else {
		    LOGGER.info(stbProcess.getProcessName()
			    + " is not started after crash...waiting for 2 more minutes!!!!");
		    tapEnv.waitTill(RDKBTestConstants.TWO_MINUTES);
		}
	    }

	} else {
	    response = CommonMethods.getPidOfProcess(device, tapEnv, stbProcess.getProcessName());
	}
	status = CommonMethods.isNotNull(response);
	LOGGER.debug("ENDING METHOD : verifyProcessRestartedStatusAfterProcessCrash");
	return status;
    }

}
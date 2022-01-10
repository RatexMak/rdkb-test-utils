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
package com.automatics.rdkb.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Dut;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.utils.cdl.BroadBandCodeDownloadUtils;
import com.automatics.rdkb.utils.cdl.BroadBandXconfCdlUtils;
import com.automatics.rdkb.utils.telemetry.BroadBandTelemetry2Utils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.wifi.BroadBandWiFiUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;

/**
 * Utility class which handles the Post condition related functionality and verification.
 * 
 * @author Muthukumar
 */
public class BroadBandPostConditionUtils {

    /** SLF4J logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandPostConditionUtils.class);

    /**
     * Post-Condition method to reactivate the device
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param isReactivated
     *            It is 'True' Device already activated.'False' Device Not reactivated
     * @refactor Govardhan
     */
    public static void executePostConditionToReActivateDevice(Dut device, AutomaticsTapApi tapEnv, boolean isReactivated,
    		int postConStepNumber) throws TestException {
	String errorMessage = null;
	boolean status = false;
	try {
	    /**
	     * POST-CONDITION : BEGIN BROAD BAND DEVICE REACTIVATION
	     */
	    LOGGER.info("#######################################################################################");
	    LOGGER.info(
		    "POST-CONDITION " + postConStepNumber + " : DESCRIPTION : BEGIN BROAD BAND DEVICE REACTIVATION.");
	    LOGGER.info("POST-CONDITION " + postConStepNumber
		    + " : ACTION : BROAD BAND DEVICE REACTIVATION USING WEBPA OR SNMP. ");
	    LOGGER.info("POST-CONDITION " + postConStepNumber + " : EXPECTED : DEVICE SHOULD GET REACTIVATED");
	    LOGGER.info("#######################################################################################");
	    if (!isReactivated) {
		errorMessage = "FAILED TO REACTIVATE THE ROUTER DEVICE";
		status = false;
		try {
		    BroadBandWiFiUtils.reactivateDeviceUsingWebpaOrSnmp(tapEnv, device);
		    status = true;
		} catch (TestException e) {
		    errorMessage = e.getMessage();
		}
		if (status) {
		    LOGGER.info("POST-CONDITION " + postConStepNumber
			    + " : ACTUAL: THE ROUTER DEVICE REACTIVATED SUCCESSFULLY.");
		} else {
		    LOGGER.error("POST-CONDITION " + postConStepNumber + ": ACTUAL: " + errorMessage);
		}
	    } else {
		LOGGER.info("POST-CONDITION " + postConStepNumber
			+ ": ACTUAL: SKIPPING REACTIVATION STEP AS THE DEVICE WAS ALREADY REACTIVATED SUCCESSFULLY IN PRE-CONDITION.");
	    }
	} catch (Exception exception) {
	    LOGGER.error(
		    "Execution error occurred while executing to verify default radio status post conditions due to exception --> "
			    + exception.getMessage());
	}
    }
    
    /**
     * Post-Condition method to trigger Original build on the device. Existing build on the device before starting the
     * test
     * 
     * @param settop
     *            {@link Settop}
     * @param hasLatestBuildChanged
     *            It is 'True' - Latest build flashed.'False' - Latest build didn't flashed
     * 
     * @param hasOriginalBuildChanged
     *            It is 'True'- Orignal build flashed.'False' It is 'True' - Orignal build didn't flashed .
     */
    public static void executePostConditionToTriggerCdl(Dut device, AutomaticsTapApi tapEnv,
	    boolean hasLatestBuildChanged, boolean hasOriginalBuildChanged, int postConStepNumber,
	    String initialFirmwareVersion) throws TestException {
	String errorMessage = null;
	boolean status = false;
	boolean isSTBAccessible = false;
	try {
	    /**
	     * POST-CONDITION : PERFORM FLASHING THE ORIGINAL BUILD ON THE DEVICE
	     */
	    
	    isSTBAccessible = BroadBandCommonUtils.isSTBAccessible(tapEnv, device,
		    BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS, BroadBandTestConstants.CONSTANT_5);
	    if (isSTBAccessible) {
		LOGGER.info(
			"DEVICE IS SSHABLE GOING TO TRIGGER REQUIRED IMAGE  ON THE DEVICE " + initialFirmwareVersion);
	    if (hasLatestBuildChanged && !hasOriginalBuildChanged) {

		postConStepNumber++;
		LOGGER.info("#######################################################################################");
		LOGGER.info("POST-CONDITION " + postConStepNumber
			+ " : DESCRIPTION : PERFORM FLASHING THE ORIGINAL BUILD ON THE DEVICE: "
			+ initialFirmwareVersion);
		LOGGER.info("POST-CONDITION " + postConStepNumber
			+ " : ACTION : FLASH THE ORIGINAL BUILD ON THE DEVICE USING HTTP/ TR-181. ");
		LOGGER.info("POST-CONDITION " + postConStepNumber
			+ " : EXPTECTED : ORIGINAL IMAGE MUST BE FLASHED ON THE DEVICE");
		LOGGER.info("#######################################################################################");
		errorMessage = "UNABLE TO FLASH THE ORIGINAL BUILD ON THE DEVICE. ";
		status = BroadBandCodeDownloadUtils.triggerPreviousCodeDownload(device, tapEnv, initialFirmwareVersion);
		if (status) {
		    LOGGER.info("POST-CONDITION " + postConStepNumber
			    + " : ACTUAL : ORIGINAL IMAGE FLASHED SUCCESSFULLY ON THE DEVICE.");
		} else {
		    LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL :" + errorMessage);
		}
	    } else {
		    errorMessage = "Failed to revert back the original build as the device is not SSHable";
		    LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL :" + errorMessage);
		}
	    }
	} catch (Exception exception) {
	    LOGGER.error(
		    "Execution error occurred while executing to verify default radio status post conditions due to exception --> "
			    + exception.getMessage());
	}
    }
    
    /**
     * Post-Condition method to clear the CDL information URL in /nvram/swupdate.conf
     * 
     * @param settop
     *            instance of{@link Settop}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param postConditionNumber
     *            String to hold post condition number.
     */
    public static void executePostConditionToClearCdlInfoInXconf(Dut device, AutomaticsTapApi tapEnv,
	    int postConStepNumber) {
	boolean status = false;
	String errorMessage = null;
	LOGGER.info("#######################################################################################");
	LOGGER.info("POST-CONDITION " + postConStepNumber
		+ " : DESCRIPTION:Clear the code download URL information in /nvram/swupdate.conf");
	LOGGER.info("POST-CONDITION " + postConStepNumber + " : ACTION : Set the image server url as empty");
	LOGGER.info("POST-CONDITION " + postConStepNumber
		+ " : EXPECTED : Must clear the code download URL information in /nvram/swupdate.conf");
	LOGGER.info("#######################################################################################");
	errorMessage = "Failed to Clear the code download URL information in /nvram/swupdate.conf";
	status = BroadBandXconfCdlUtils.toClearCdlInfoInXconf(device, tapEnv);
	if (status) {
	    LOGGER.info("POST-CONDITION " + postConStepNumber
		    + " :Successfully cleared the code download URL information in /nvram/swupdate.conf ");
	} else {
	    LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL : " + errorMessage);
	}
    }
    
    
    /**
     * Post-Condition method to remove backup file
     * 
     * @param device
     *            {@link Dut}
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * 
     * @param postConditionNumber
     *            int to hold post condition number.
     * 
     * @param String
     *            File to be removed.
     */
    public static void executePostConditionToRemoveBackUpFile(Dut device, AutomaticsTapApi tapEnv, int postConStepNumber,
	    String fileToBeRemoved) throws TestException {
	String errorMessage = null;
	boolean status = false;
	LOGGER.info("#######################################################################################");
	LOGGER.info("POST-CONDITION " + postConStepNumber + " : DESCRIPTION : Remove File and verify File removal");
	LOGGER.info("POST-CONDITION " + postConStepNumber
		+ " : ACTION : Execute command to Remove File and verify File removal.");
	LOGGER.info("POST-CONDITION " + postConStepNumber + " : EXPTECTED : File must be removed successfully");
	LOGGER.info("#######################################################################################");
	errorMessage = "Failed to remove the file from the directory";
	try {
	    if (CommonUtils.isFileExists(device, tapEnv, fileToBeRemoved)) {
		status = CommonUtils.removeFileandVerifyFileRemoval(tapEnv, device, fileToBeRemoved);
	    } else {
		errorMessage = "File not available";
	    }
	} catch (Exception e) {
	    errorMessage += e.getMessage();
	    LOGGER.error(errorMessage);
	}
	if (status) {
	    LOGGER.info("POST-CONDITION " + postConStepNumber + " : ACTUAL : File removed successfully ");

	} else {
	    LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL : " + errorMessage);
	}
    }
    
    /**
     * Method to perform steps to enable Telemetry 2 configs via RFC
     * 
     * @param device
     *            {@link Instanceof Dut}
     * @param stepNumber
     *            step number to start with
     * @param testCaseId
     *            test case ID to update
     */
    public static void PostConditionToUpdateTelemetryVer1SettingsViaRFC(Dut device, int stepNumber,
	    AutomaticsTapApi tapEnv) {
	try {

	    boolean status = false;
	    String errorMessage = null;

	    errorMessage = "Failed to configure RFC payload for telemetry 2 configurations";
	    status = false;

	    LOGGER.info("##################################################################################");
	    LOGGER.info("POST-CONDITION " + stepNumber
		    + ": DESCRIPTION : Configure RFC payload to set telemetry 1 configurations");
	    LOGGER.info("POST-CONDITION " + stepNumber
		    + ": ACTION : 1. Copy and update /nvram/rfc.properties with mock RFC config server URL2. "
		    + "Post payload after replacing ESTB mac and enable/disable value to RFC URL");
	    LOGGER.info("POST-CONDITION " + stepNumber
		    + ": EXPECTED : Successfully rebooted or triggered check-in after configuring RFC payload");
	    LOGGER.info("##################################################################################");

	    if (BroadBandRfcFeatureControlUtils.executePreconditionForRfcTests(device, tapEnv,
		    AutomaticsTapApi.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_TELEMETRY_VER_2_CONFIG)
			    .replaceAll(BroadBandTestConstants.TELEMETRY_ENABLE_VAUE, BroadBandTestConstants.FALSE)
			    .replaceAll(BroadBandTestConstants.TELEMETRY_VERSION_VALUE,
				    BroadBandTestConstants.STRING_VALUE_ONE)
			    .replaceAll(BroadBandTestConstants.TELEMETRY_CONFIG_URL_VALUE, " "))) {
		errorMessage = "Unable to reboot device successfully";
		status = CommonMethods.rebootAndWaitForIpAccusition(device, tapEnv);
	    }

	    if (status) {
		LOGGER.info("POST-CONDITION " + stepNumber
			+ ": ACTUAL : Successfully rebooted or triggered check-in after configuring RFC payload");
	    } else {
		LOGGER.error("POST-CONDITION " + stepNumber + ": ACTUAL : " + errorMessage);
	    }
	    LOGGER.info("##################################################################################");
	    if (status) {
		++stepNumber;
		status = false;
		errorMessage = "Setting the telemetry version 1 configurations via RFC has failed";

		LOGGER.info("##################################################################################");
		LOGGER.info("POST-CONDITION " + stepNumber
			+ ": DESCRIPTION : Verify if telemetry version 1 params are updated by RFC");
		LOGGER.info("POST-CONDITION " + stepNumber
			+ ": ACTION : 1. Verify /tmp/rfc_configdata.txt contains posted parameter value2."
			+ " Verify log message for updation in /rdklogs/logs/dcmrfc.log3. Verify parameter value is changed");
		LOGGER.info(
			"POST-CONDITION " + stepNumber + ": EXPECTED : Successfully set parameter value through RFC");
		LOGGER.info("##################################################################################");

		status = BroadBandRfcFeatureControlUtils
			.verifyParameterUpdatedByRfc(device, tapEnv,
				BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS.FEATURE_ENABLE.getParam(),
				BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS.FEATURE_ENABLE.getFactoryDefault())
			.isStatus()
			&& BroadBandRfcFeatureControlUtils.verifyParameterUpdatedByRfc(device, tapEnv,
				BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS.CONFIG_URL.getParam(),
				BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS.CONFIG_URL.getFactoryDefault())
				.isStatus()
			&& BroadBandRfcFeatureControlUtils
				.verifyParameterUpdatedByRfc(device, tapEnv,
					BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS.VERSION.getParam(),
					BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS.VERSION.getFactoryDefault())
				.isStatus();

		if (status) {
		    LOGGER.info(
			    "POST-CONDITION " + stepNumber + ": ACTUAL : Successfully set parameter value through RFC");
		} else {
		    LOGGER.error("POST-CONDITION " + stepNumber + ": ACTUAL : " + errorMessage);
		}
		LOGGER.info("##################################################################################");

		++stepNumber;
		errorMessage = "The configuration set via RFC for telemetry 1 configurations has not taken effect";
		status = false;

		LOGGER.info("##################################################################################");
		LOGGER.info("POST-CONDITION " + stepNumber
			+ ": DESCRIPTION : Validate from webpa that configuration of telemetry 1 configurations via RFC is reflecting");
		LOGGER.info("POST-CONDITION " + stepNumber
			+ ": ACTION : Execute WebPa GET command on the telemetry 1 webpa paramas");
		LOGGER.info(
			"POST-CONDITION " + stepNumber + ": EXPECTED : The set values should be returned via webpa");
		LOGGER.info("##################################################################################");

		status = BroadBandTelemetry2Utils.validateFactoryDefaultsForTelemetryParams(device, tapEnv);

		if (status) {
		    LOGGER.info("POST-CONDITION " + stepNumber
			    + ": ACTUAL : The configuration set via RFC for telemetry 1 is successful");
		} else {
		    LOGGER.error("POST-CONDITION " + stepNumber + ": ACTUAL : " + errorMessage);
		}
		LOGGER.info("##################################################################################");
	    }
	} catch (TestException e) {
	    LOGGER.error("Exception occured while configuring telemetry 2 settings via RFC", e);
	}

    }
    
    /**
     * Post-Condition method to enable/disable the Moca status
     * 
     * @param device
     *            instance of{@link Dut}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param isEnabled
     *            True - To be Enabled,False- To be Disabled
     * @param postConditionNumber
     *            Parameter to hold post condition number.
     * 
     */
    public static void executePostConditionRevertDefaultMocaStatus(Dut device, AutomaticsTapApi tapEnv, boolean isEnabled,
	    int postConStepNumber) throws TestException {
	String errorMessage = null;
	boolean status = false;
	/**
	 * POST CONDITION : ENABLE/DISABLE THE MoCA STATUS
	 */
	LOGGER.info("#######################################################################################");
	LOGGER.info("POST-CONDITION " + postConStepNumber + " : DESCRIPTION : " + (isEnabled ? "ENABLE" : "DISABLE")
		+ " THE MoCA STATUS");
	LOGGER.info("POST-CONDITION " + postConStepNumber + ": ACTION : " + (isEnabled ? "ENABLE" : "DISABLE")
		+ " THE MoCA STATUS " + BroadBandWebPaConstants.WEBPA_PARAM_FOR_MOCA_INTERFACE_ENABLE + " STATUS AS "
		+ (isEnabled ? "TRUE" : "FALSE") + " USING WEBPA");
	LOGGER.info("POST-CONDITION " + postConStepNumber + " : EXPECTED : MoCA STATUS MUST BE "
		+ (isEnabled ? "ENABLED" : "DISABLED"));
	LOGGER.info("#######################################################################################");
	errorMessage = "UNABLE TO " + (isEnabled ? "ENABLE" : "DISABLE") + " THE MoCA STATUS";
	try {
	    status = BroadBandWebPaUtils.setVerifyWebPAInPolledDuration(device, tapEnv,
		    BroadBandWebPaConstants.WEBPA_PARAM_FOR_MOCA_INTERFACE_ENABLE, BroadBandTestConstants.CONSTANT_3,
		    isEnabled ? BroadBandTestConstants.TRUE : BroadBandTestConstants.FALSE,
		    BroadBandTestConstants.THREE_MINUTES, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
	    if (status) {
		LOGGER.info("POST-CONDITION " + postConStepNumber + ": ACTUAL : SUCCESSFULLY "
			+ (isEnabled ? "ENABLED" : "DISABLED") + " THE  MoCA STATUS");
	    } else {
		LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL : " + errorMessage);
	    }
	} catch (Exception exception) {
	    LOGGER.error("Execution error occured while switching the MoCA status  --> " + exception.getMessage());
	}
    }
    
    /**
     * Post-Condition method to disable the xifnity wifi
     * 
     * @param device
     *            instance of{@link Dut}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param postConditionNumber
     *            String to hold post condition number.
     */
    public static void executePostConditionToDisableXfinityWifi(Dut device, AutomaticsTapApi tapEnv,
	    int postConStepNumber) {
	boolean status = false;
	String errorMessage = null;
	try {
	    LOGGER.info("#######################################################################################");
	    LOGGER.info("POST-CONDITION " + postConStepNumber
		    + " : DESCRIPTION : VERIFY THE XFINITYWIFI STATUS IS DISABLED");
	    LOGGER.info("POST-CONDITION " + postConStepNumber + " : ACTION : EXECUTE WEBPA COMMAND:"
		    + BroadBandWebPaConstants.WEBPA_PARAM_ENABLING_PUBLIC_WIFI);
	    LOGGER.info("POST-CONDITION " + postConStepNumber + " : EXPECTED : XFINITY WIFI MUST BE DISABLED");
	    LOGGER.info("#######################################################################################");
	    errorMessage = "UNABLE TO DISABLE THE XFINITY WIFI ON GATEWAY DEVICE";
	    status = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv,
		    BroadBandWebPaConstants.WEBPA_PARAM_ENABLING_PUBLIC_WIFI, BroadBandTestConstants.CONSTANT_3,
		    BroadBandTestConstants.FALSE);
	    if (status) {
		LOGGER.info("POST-CONDITION " + postConStepNumber
			+ " : ACTUAL : SUCCESSFULLY DISABLED THE XFINITY WIFI ON GATEWAY DEVICE");
	    } else {
		LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL : " + errorMessage);
	    }

	} catch (Exception e) {
	    LOGGER.info(e.getMessage());
	    LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL : " + e.getMessage());
	}
    }
    
}
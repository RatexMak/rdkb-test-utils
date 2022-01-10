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
package com.automatics.rdkb.utils.cdl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Dut;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.constants.BroadBandCdlConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.CommonUtils;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpUtils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.CommonMethods;
import com.automatics.webpa.WebPaParameter;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpMib;
import com.automatics.constants.AutomaticsConstants;

public class BroadBandCodeDownloadUtils {

    /** SLF4J logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandCodeDownloadUtils.class);

    /**
     * Perform HTTP CDL using webpa parameters
     * 
     * @param device
     *            Dut to be verified
     * @param tapApi
     *            AutomaticsTapApi instance
     * @param buildNameToBeTriggerred
     *            buildNameToBeTriggerred
     * 
     * @return true if device is loaded with given build
     * @author Gnanaprakasham
     * @refactor Govardhan
     */
    public static boolean triggerHttpCodeDownloadUsingWebpaParameters(Dut device, AutomaticsTapApi tapEnv,
	    String buildNameToBeTriggerred) {
	return triggerHttpCodeDownloadUsingWebpaParameters(device, tapEnv, buildNameToBeTriggerred, false);
    }

    /**
     * Perform HTTP CDL using webpa parameters
     * 
     * @param device
     *            Dut to be verified
     * @param tapApi
     *            AutomaticsTapApi instance
     * @param buildNameToBeTriggerred
     *            buildNameToBeTriggerred
     * @param isCcsSupportBuild
     *            true if ccs supportbuild model
     * 
     * @return true if device is loaded with given build
     * @author Gnanaprakasham
     * @refactor Govardhan
     */
    public static boolean triggerHttpCodeDownloadUsingWebpaParameters(Dut device, AutomaticsTapApi tapEnv,
	    String buildNameToBeTriggerred, boolean isCcsSupportBuild) {

	String cdlServerUrl = AutomaticsPropertyUtility.getProperty("rdkb.cdl.dac15.url");
	boolean status = false;
	String response = null;
	String buildNameToUse = null;

	String currentFirmwareVersion = FirmwareDownloadUtils.getCurrentFirmwareFileNameForCdl(tapEnv, device);
	LOGGER.info("CURRENT FIRMWARE VERSION: " + currentFirmwareVersion);

	buildNameToUse = buildNameToBeTriggerred + BroadBandTestConstants.BINARY_BUILD_IMAGE_EXTENSION;


	WebPaParameter setCdlServerUrl = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		BroadBandWebPaConstants.WEBPA_PARAM_Device_DeviceInfo_X_RDKCENTRAL_COM_FirmwareDownloadURL,
		cdlServerUrl, WebPaDataTypes.STRING.getValue());
	WebPaParameter configureImageNameForCdl = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		BroadBandWebPaConstants.WEBPA_PARAM_Device_DeviceInfo_X_RDKCENTRAL_COM_FirmwareToDownload,
		buildNameToUse, WebPaDataTypes.STRING.getValue());
	WebPaParameter initiateCodeDownload = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		BroadBandCdlConstants.WEBPA_PARAM_TRIGGER_CDL, RDKBTestConstants.TRUE,
		WebPaDataTypes.BOOLEAN.getValue());
	
	LOGGER.info("initiateCodeDownload MSg "+initiateCodeDownload.getMessage());
	LOGGER.info("initiateCodeDownload DataType "+initiateCodeDownload.getDataType());
	LOGGER.info("initiateCodeDownload Name "+initiateCodeDownload.getName());
	LOGGER.info("initiateCodeDownload Parameter "+initiateCodeDownload.getParameterCount());
	LOGGER.info("initiateCodeDownload String "+initiateCodeDownload.toString());


	List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();

	webPaParameters.add(setCdlServerUrl);
	LOGGER.info("Confgure CDL Server- " + cdlServerUrl);
	webPaParameters.add(configureImageNameForCdl);
	LOGGER.info("Confgure Image CDL - " + buildNameToUse);
	webPaParameters.add(initiateCodeDownload);
	LOGGER.info("Initiate CDL now- " + initiateCodeDownload);

	// Set the required parameter with corresponding values for code download
	Map<String, String> serverResponse = tapEnv.executeMultipleWebPaSetCommands(device, webPaParameters);
	
	LOGGER.info("serverResponse is : "+serverResponse);

	// Verify webpa params set status
	for (Map.Entry<String, String> entry : serverResponse.entrySet()) {
	    response = entry.getKey();
	    response = serverResponse.get(response);
	    if (CommonMethods.isNotNull(response) && response.equalsIgnoreCase(BroadBandTestConstants.SUCCESS_TXT)) {
		LOGGER.info(
			"SUCCESSFULLY CONFIGURED " + entry.getKey() + "  PARAMETER FOR HTTP CODE DOWNLOAD VIA WEBPA");
	    } else {
		throw new TestException("Failed to trigger Http code download through Webpa. Error while setting "
			+ entry.getKey() + " TR-181 parameter value as " + entry.getValue());
	    }
	}

	tapEnv.waitTill(RDKBTestConstants.ONE_MINUTE_IN_MILLIS);

	// Verify code download started, In progress and completed status
	for (int iteration = 0; iteration <= 30; iteration++) {

	    response = tapEnv.executeWebPaCommand(device,
		    BroadBandWebPaConstants.WEBPA_PARAM_Device_DeviceInfo_X_RDKCENTRAL_COM_FirmwareDownloadStatus);
	    LOGGER.info("Obtained firmware upgrade status : " + response);
	    if (CommonMethods.isNotNull(response)) {
		if (response.contains(BroadBandCdlConstants.CDL_STATUS_FAILED)) {
		    LOGGER.error(
			    "Http code download via webpa is failed. Obtained code download status is : " + response);
		    throw new TestException(
			    "Http code download via webpa is failed. Obtained code download status is : " + response);
		} else if (response.contains(BroadBandCdlConstants.CDL_STATUS_COMPLETED)) {
		    CommonUtils.waitForDeviceToReboot(tapEnv, device, BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS);
		    if (CommonMethods.waitForEstbIpAcquisition(tapEnv, device)) {
			// Verify Image upgraded status after code download
			if (BroadBandCommonUtils.verifyCurrentlyRunningImageVersionUsingWebPaCommand(tapEnv, device,
				buildNameToUse, true)) {
			    status = true;
			    LOGGER.info(
				    "DEVICE IS SUCCESSFULLY LOADED WITH GIVE FIRMWARE VERSION USING WEBPA PARAMETERS!!!");
			} else {
			    LOGGER.error("Failed to flash after CDL Download for the image - " + buildNameToUse);
			    throw new TestException(
				    "Failed to flash after CDL Download for the image - " + buildNameToUse);
			}
		    } else {
			LOGGER.error(
				"Failed to flash after CDL Download for the image. Unable to access the device after firmware upgrade !!!");
			throw new TestException(
				"Failed to flash after CDL Download for the image. Unable to access the device after firmware upgrade !!!");
		    }
		    break;
		} else if (response.contains(BroadBandCdlConstants.CDL_STATUS_IN_PROGRESS)
			|| response.contains(BroadBandCdlConstants.CDL_STATUS_NOT_STARTED)) {
		    LOGGER.info(
			    "Firmware upgrade is in progress. Hence wait for few minutes to complete firmware upgrade");

		    LOGGER.info("Log response in PAM file is - " + tapEnv.executeCommandUsingSsh(device,
			    (DeviceModeHandler.isFibreDevice(device))
				    ? "tail -f /rdklogs/logs/EPONAGENTlog.txt.0"
				    : "tail -f /rdklogs/logs/CMlog.txt.0"));
		}

	    } else {
		boolean isRebooted = !CommonUtils.executeTestCommand(tapEnv, device);
		if (isRebooted) {
		    LOGGER.info(
			    "Device is rebooted after cdl trigger through tr-181 parameter. So wait for ip acquisition and verify image name");
		    CommonMethods.waitForEstbIpAcquisition(tapEnv, device);
		    if (BroadBandCommonUtils.verifyCurrentlyRunningImageVersionUsingWebPaCommand(tapEnv, device,
			    buildNameToUse, true)) {
			status = true;
			LOGGER.info(
				"DEVICE IS SUCCESSFULLY LOADED WITH GIVE FIRMWARE VERSION USING WEBPA PARAMETERS!!!");
			break;
		    }
		}
	    }
	    // Sometimes device gets rebooting during this time.
	    tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
	}
	return status;
    }

    /**
     * Perform HTTP,TFTP and SNMP CDL using webpa parameters
     * 
     * @param settop
     *            settop to be verified
     * @param tapApi
     *            AutomaticsTapApi instance
     * @param initialFirmwareVersion
     *            initialFirmwareVersion .
     * 
     * @return true if device is loaded with previous build
     */
    public static boolean triggerPreviousCodeDownload(Dut device, AutomaticsTapApi tapEnv,
	    String initialFirmwareVersion) {
	boolean isTrigggered = false;
	try {
	    isTrigggered = FirmwareDownloadUtils.triggerHttpCdlUsingTr181(tapEnv, device, initialFirmwareVersion);
	} catch (TestException testException) {
	    // Log & Suppress the exception
	    LOGGER.error(testException.getMessage());
	}
	try {
	    // In case the HTTP TR-181 CDL Fails, then trigger CDL using TFTP Docsis SNMP Command.
	    if (!isTrigggered) {
		LOGGER.error("HTTP TR-181 CDL FAILED; HENCE GOING TO TRIGGER CDL WITH TFTP DOCSIS SNMP COMMANDS.");
		isTrigggered = FirmwareDownloadUtils.triggerAndWaitForTftpCodeDownloadUsingDocsisSnmpCommand(tapEnv,
			device, initialFirmwareVersion + BroadBandCdlConstants.BIN_EXTENSION, false);
	    }
	} catch (TestException testException) {
	    // Log & Suppress the exception
	    LOGGER.error(testException.getMessage());
	}
	return isTrigggered;
    }

    /**
     * Method to verify Code download status before Reboot
     * 
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param device
     *            device to be verified
     * @return true if SNMP code download completed successfully
     * @Refactor Athira
     */
    public static boolean verifySnmpCodeDownlaodCompletionStatus(AutomaticsTapApi tapEnv, Dut device) {
	LOGGER.debug("STARING METHOD : verifySnmpCodeDownlaodCompletionStatus");
	// Variable declaration Starts
	boolean isDownloadCompleted = false;
	boolean isDeviceRebooted = false;
	boolean status = false;
	long startTime = 0;
	// Variable declaration Ends

	startTime = System.currentTimeMillis();
	do {
	    String snmpResponse = BroadBandSnmpUtils.snmpGetOnEcm(tapEnv, device,
		    BroadBandSnmpMib.ECM_DOCS_DEV_SW_OPER_STATUS.getOid());
	    LOGGER.info("SNMP RESONSE FOR CODE DOWNLAOD COMPLETION STATUS: " + snmpResponse);

	    if (snmpResponse.equalsIgnoreCase(CodeDownloadStatus.COMPLETE_FROM_MGT.getStatus())
		    || snmpResponse.equalsIgnoreCase(BroadBandTestConstants.STRING_VALUE_FIVE)
		    || snmpResponse.equalsIgnoreCase(BroadBandTestConstants.STRING_VALUE_THREE)) {
		LOGGER.info("CODE DOWNLOAD COMPLETED WITHOUT REBOOT.");
		isDownloadCompleted = true;
		break;
	    } else if (snmpResponse.equalsIgnoreCase(BroadBandTestConstants.STRING_CONSTANT_4)) {
		LOGGER.error("CODE DOWNLOAD FAILED.");
		break;
	    } else if (CommonMethods.patternMatcher(snmpResponse, BroadBandTestConstants.NO_RESPONSE_SNMP)) {
		LOGGER.info("DEVICE REBOOTED SUCCESSFULLY IN BETWEEN DOWMLOAD.");
		isDeviceRebooted = true;
		break;
	    }
	} while (((System.currentTimeMillis() - startTime)
		/ BroadBandTestConstants.INTERGER_CONSTANT_60000) < BroadBandTestConstants.CONSTANT_60
		&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));

	if (isDownloadCompleted) {
	    LOGGER.info("WAITING FOR DEVICE REBOOT AFTER DOWNLOAD COMPLETION.");
	    startTime = System.currentTimeMillis();
	    do {
		String snmpResponse = BroadBandSnmpUtils.snmpGetOnEcm(tapEnv, device,
			BroadBandSnmpMib.ECM_DOCS_DEV_SW_OPER_STATUS.getOid());
		LOGGER.info("SNMP RESONSE FOR FOR REBOOT WAIT: " + snmpResponse);
		if (CommonMethods.patternMatcher(snmpResponse, BroadBandTestConstants.NO_RESPONSE_SNMP)) {
		    LOGGER.info("DEVICE REBOOTED SUCCESSFULLY AFTER DOWNLOAD COMPLETION.");
		    LOGGER.info("WAITING FOR DEVICE TO COME UP.");
		    break;
		}
	    } while (((System.currentTimeMillis() - startTime)
		    / BroadBandTestConstants.INTERGER_CONSTANT_60000) < BroadBandTestConstants.INTEGER_VALUE_5
		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
	    status = verifyCodeDownloadStatusAfterReboot(tapEnv, device);
	} else if (isDeviceRebooted) {
	    LOGGER.info("DEVICE ALREADY REBOOTED IN BETWEEN DOWNLAOD AND WAITING FOR DEVICE TO COME UP.");
	    status = verifyCodeDownloadStatusAfterReboot(tapEnv, device);
	}
	if (!isDeviceRebooted) {
	    LOGGER.error(
		    "Please make sure your CM boot file DHCP criteria is genericCM and it is supports snmp flashing. Device didn't go for Factory Reset.");
	}
	LOGGER.debug("ENDING METHOD : verifySnmpCodeDownlaodCompletionStatus");
	return status;
    }

    /**
     * Method to verify Code download Status after Reboot
     * 
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param device
     *            device to be verified
     * @return true if code downloaded successfully after Reboot
     */
    public static boolean verifyCodeDownloadStatusAfterReboot(AutomaticsTapApi tapEnv, Dut device) {
	LOGGER.debug("STARING METHOD : verifyCodeDownloadStatusAfterReboot");
	// Variable declaration Starts
	boolean isDeviceOnline = false;
	long startTime = System.currentTimeMillis();
	// Variable declaration Ends

	do {
	    String snmpResponse = BroadBandSnmpUtils.snmpGetOnEcm(tapEnv, device,
		    BroadBandSnmpMib.ECM_DOCS_DEV_SW_OPER_STATUS.getOid());
	    LOGGER.info("SNMP RESONSE FOR CODE DOWNLAOD COMPLETION STATUS AFTER REBOOT: " + snmpResponse);
	    if (snmpResponse.equalsIgnoreCase(BroadBandTestConstants.STRING_VALUE_THREE)
		    || snmpResponse.equalsIgnoreCase(BroadBandTestConstants.STRING_VALUE_FIVE)) {
		LOGGER.info("DEVICE CAME UP AFTER REBOOT AND DOWNLOADING IS COMPLETED.");
		isDeviceOnline = true;
		break;
	    } else if (snmpResponse.equalsIgnoreCase(BroadBandTestConstants.STRING_CONSTANT_4)) {
		LOGGER.error("DOWNLAOD GOT FAILED AFTER REBOOT.");
		break;
	    }
	} while (((System.currentTimeMillis() - startTime)
		/ BroadBandTestConstants.INTERGER_CONSTANT_60000) < BroadBandTestConstants.CONSTANT_20
		&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
	LOGGER.debug("ENDING METHOD : verifyCodeDownloadStatusAfterReboot");
	return isDeviceOnline;
    }

    /**
     * Verifies the upgraded image name is reflected in the image by doing cat /version.txt
     * 
     * @param tapEnv
     *            AutomaticsTapApi tap instance.
     * @param device
     *            The set-top box where the command to be executed.
     * 
     * @return true if image name is correct else false
     */
    public static boolean isImageUpgradedInDevice(AutomaticsTapApi tapEnv, Dut device, String imageName) {
	LOGGER.debug("STARING METHOD : isImageUpgradedInDevice");
	// Varaible declaration starts
	boolean isImageUpgraded = false;
	// Varaible declaration Ends

	for (int i = 0; i <= 10; i++) {
	    String imageNameInDevice = BroadBandCodeDownloadUtils.parseOutputToGetImageVersion(
		    tapEnv.executeCommandUsingSsh(device, RDKBTestConstants.CMD_GREP_IMAGE_NAME_FROM_VERSION_FILE));
	    LOGGER.info("Imagename retrieved from PRP file :" + imageName + " :: Image name obtained from Device : "
		    + imageNameInDevice);
	    if ((imageName).contains(imageNameInDevice)) {
		LOGGER.info("Imagename retrieved from PRP file :" + imageName
			+ " and Image name obtained from device : " + imageNameInDevice + "are same - Pass");
		isImageUpgraded = true;
		break;
	    } else {
		tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		continue;
	    }
	}
	LOGGER.debug("ENDING METHOD : isImageUpgradedInDevice");
	return isImageUpgraded;

    }

    /**
     * Verifies the upgrade status MIB is returning "inProgress" during CDL using SNMP command.
     * 
     * @param tapEnv
     *            AutomaticsTapApi tap instance.
     * @param device
     *            The set-top box where the command to be executed.
     * 
     * @return true if the Upgrade status is "inProgress", false otherwise.
     */
    public static boolean isUgradeStatusInProgressUsingSnmpCommand(AutomaticsTapApi tapEnv, Dut device) {
	LOGGER.debug("STARING METHOD : isUgradeStatusInProgressUsingSnmpCommand");
	// Variable declaration Starts
	boolean inprogress = false;
	// Variable declaration Ends

	for (int iteration = 0; iteration < 10; iteration++) {
	    String snmpResponse = BroadBandSnmpUtils.snmpGetOnEcm(tapEnv, device,
		    BroadBandSnmpMib.ECM_DOCS_DEV_SW_OPER_STATUS.getOid());
	    LOGGER.info("SNMP RESPONSE FOR UPGRADE STATUS:" + snmpResponse);

	    if (snmpResponse.equalsIgnoreCase(CodeDownloadStatus.IN_PROGRESS.getStatus())
		    || snmpResponse.equals(BroadBandTestConstants.STRING_VALUE_ONE)
		    || snmpResponse.equals(BroadBandTestConstants.STRING_VALUE_FIVE)) {
		inprogress = true;
		break;
	    } else if (snmpResponse.equals(BroadBandTestConstants.STRING_VALUE_4)) {
		inprogress = false;
		break;
	    } else {
		LOGGER.info("MIB message for box upgrade status : " + snmpResponse);
		tapEnv.waitTill(AutomaticsConstants.THIRTY_SECOND_IN_MILLIS);
		continue;
	    }

	}
	LOGGER.debug("ENDING METHOD : isUgradeStatusInProgressUsingSnmpCommand");
	return inprogress;
    }

    /**
     * Method to parse the output of version details and provide the image version alone.
     * 
     * @param versionOutput
     *            output of Linux command to retrieve image version.
     * 
     * @return the image version.
     */
    public static String parseOutputToGetImageVersion(String commandOutput) {
	LOGGER.debug("STARTING METHOD : parseOutputToGetImageVersion");
	// Variable declaration starts
	String imageVersion = null;
	boolean isImageVersionFound = false;
	// Variable declaration starts

	/** For line by line parsing; Splitting output line by line */
	String[] splittedTopCommandOutput = commandOutput.split("\n");
	/** Parsing Splitted output line by line */
	for (String lineByLineOutput : splittedTopCommandOutput) {
	    String trimedLineOutput = lineByLineOutput.trim();
	    LOGGER.debug("Line by line output : " + trimedLineOutput);
	    /** Skipping empty line and line beginning with hash. */
	    if (!trimedLineOutput.startsWith(AutomaticsConstants.DELIMITER_HASH)
		    && !AutomaticsConstants.EMPTY_STRING.equals(trimedLineOutput)
		    && trimedLineOutput.startsWith(BroadBandTestConstants.IDENTIFIER_FOR_BEGINNING_OF_IMAGE_NAME)
		    && !isImageVersionFound) {
		imageVersion = trimedLineOutput.replaceFirst(
			BroadBandTestConstants.IDENTIFIER_FOR_BEGINNING_OF_IMAGE_NAME,
			AutomaticsConstants.EMPTY_STRING);
		imageVersion = imageVersion.replaceFirst("(:|=)", AutomaticsConstants.EMPTY_STRING);
		isImageVersionFound = true;
	    }
	}
	LOGGER.debug("ENDING METHOD : parseOutputToGetImageVersion");
	return imageVersion;
    }

}

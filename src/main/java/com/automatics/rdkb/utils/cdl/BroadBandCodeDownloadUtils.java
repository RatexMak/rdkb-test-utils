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
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandPropertyKeyConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;
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

	LOGGER.info("initiateCodeDownload MSg " + initiateCodeDownload.getMessage());
	LOGGER.info("initiateCodeDownload DataType " + initiateCodeDownload.getDataType());
	LOGGER.info("initiateCodeDownload Name " + initiateCodeDownload.getName());
	LOGGER.info("initiateCodeDownload Parameter " + initiateCodeDownload.getParameterCount());
	LOGGER.info("initiateCodeDownload String " + initiateCodeDownload.toString());

	List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();

	webPaParameters.add(setCdlServerUrl);
	LOGGER.info("Confgure CDL Server- " + cdlServerUrl);
	webPaParameters.add(configureImageNameForCdl);
	LOGGER.info("Confgure Image CDL - " + buildNameToUse);
	webPaParameters.add(initiateCodeDownload);
	LOGGER.info("Initiate CDL now- " + initiateCodeDownload);

	// Set the required parameter with corresponding values for code download
	Map<String, String> serverResponse = tapEnv.executeMultipleWebPaSetCommands(device, webPaParameters);

	LOGGER.info("serverResponse is : " + serverResponse);

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
			    (DeviceModeHandler.isFibreDevice(device)) ? "tail -f /rdklogs/logs/EPONAGENTlog.txt.0"
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
     * @param device
     *            Dut to be verified
     * @param tapEnv
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
	    // In case the HTTP TR-181 CDL Fails, then trigger CDL using TFTP Docsis SNMP
	    // Command.
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

    /**
     * Perform HTTP,TFTP and SNMP CDL using webpa parameters
     * 
     * @param device
     *            device to be verified
     * @param tapApi
     *            AutomaticsTapApi instance
     * @param initialFirmwareVersion
     *            initialFirmwareVersion .
     * 
     * @return true if device is loaded with latest build
     * @refactor Said Hisham
     * 
     */
    public static boolean triggerLatestCodeDownload(Dut device, AutomaticsTapApi tapEnv,
	    String initialFirmwareVersion) {
	boolean isTriggered = false;
	try {
	    isTriggered = FirmwareDownloadUtils.getLatestAvailableImageAndTriggerCdl(tapEnv, device,
		    initialFirmwareVersion);
	} catch (TestException testException) {
	    // Log & Suppress the Exception
	    LOGGER.error(testException.getMessage());
	}
	try {
	    // In case the HTTP TR-181 CDL Fails, then trigger CDL using TFTP Docsis SNMP
	    // Command.

	    if (!isTriggered) {

		String latestImageNameToUpgrade = tapEnv.getLatestBuildImageVersionForCdlTrigger(device, false);
		LOGGER.info("LATEST FIRMWARE VERSION: " + latestImageNameToUpgrade);
		if (CommonMethods.isNull(latestImageNameToUpgrade)) {
		    LOGGER.info(
			    " GA image obtained from deployed version service is null. Hence getting the image from property file ");
		    latestImageNameToUpgrade = BroadbandPropertyFileHandler.getAutomaticsPropsValueByResolvingPlatform(
			    device, BroadBandPropertyKeyConstants.PARTIAL_PROPERTY_KEY_FOR_GA_IMAGE);
		    LOGGER.info("Latest Firmware version from property file: " + latestImageNameToUpgrade);
		}

		LOGGER.error("HTTP TR-181 CDL FAILED; HENCE GOING TO TRIGGER CDL WITH TFTP DOCSIS SNMP COMMANDS.");
		isTriggered = FirmwareDownloadUtils.triggerAndWaitForTftpCodeDownloadUsingDocsisSnmpCommand(tapEnv,
			device, latestImageNameToUpgrade + BroadBandCdlConstants.BIN_EXTENSION, false);
	    }
	} catch (TestException testException) {
	    // Log & Suppress the Exception
	    LOGGER.error(testException.getMessage());
	}
	return isTriggered;
    }

    /**
     * Verifies the upgrade status MIB is returning "inProgress" during CDL using SNMP command.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi} instance.
     * @param device
     *            The set-top box where the command to be executed.
     * 
     * @return true if the Upgrade status is "inProgress", false otherwise.
     * @refactor Said Hisham
     */
    public static boolean isUpgradeStatusInProgressUsingSnmpCommand(AutomaticsTapApi tapEnv, Dut device) {
	LOGGER.debug("STARING METHOD : isUpgradeStatusInProgressUsingSnmpCommand");
	// Variable declaration Starts
	boolean inprogress = false;
	// Variable declaration Ends
	long startTime = 0;
	String searchResponse = null;
	String mibOid = BroadBandSnmpMib.ECM_DOCS_DEV_SW_OPER_STATUS_WITHOUT_INDEX.getOid() + AutomaticsConstants.DOT
		+ BroadBandTestConstants.STRING_VALUE_ZERO;
	for (int iteration = 0; iteration < 10; iteration++) {
	    String snmpResponse = BroadBandSnmpUtils.snmpGetOnEcm(tapEnv, device, mibOid);
	    LOGGER.info("SNMP RESPONSE FOR UPGRADE STATUS:" + snmpResponse);
	    if (snmpResponse.equalsIgnoreCase(CodeDownloadStatus.IN_PROGRESS.getStatus())
		    || snmpResponse.equals(BroadBandTestConstants.STRING_VALUE_ONE)
		    || snmpResponse.equals(BroadBandTestConstants.STRING_VALUE_FIVE)) {
		startTime = System.currentTimeMillis();
		do {
		    searchResponse = BroadBandCommonUtils.searchLogFiles(tapEnv, device,
			    BroadbandPropertyFileHandler.isDeviceCheckForUpgradeStatusUsingSnmpCommand(device)
				    || CommonMethods.isAtomSyncAvailable(device, tapEnv)
					    ? BroadBandCdlConstants.LOG_MESSAGE_CDL_WITH_CURRENT_FW_DEVICE_CHECK
					    : BroadBandCdlConstants.LOG_MESSAGE_CDL_WITH_CURRENT_FW,
			    BroadbandPropertyFileHandler.isDeviceCheckForUpgradeStatusUsingSnmpCommand(device)
				    || CommonMethods.isAtomSyncAvailable(device, tapEnv)
					    ? BroadBandCommandConstants.LOG_FILE_SEC_CONSOLE
					    : BroadBandCdlConstants.CM_LOG_TXT_0);
		    inprogress = !CommonMethods.isNotNull(searchResponse);
		} while (inprogress
			&& (System.currentTimeMillis() - startTime) < BroadBandTestConstants.TWO_MINUTE_IN_MILLIS
			&& BroadBandCommonUtils.hasWaitForDuration(tapEnv,
				BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
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
	LOGGER.debug("ENDING METHOD : isUpgradeStatusInProgressUsingSnmpCommand");
	return inprogress;
    }
    
    /**
     * Method to load device with give firmware name as input using Xconf device commands
     * 
     * @param device
     *            device to be verified
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param buildImageName
     *            Build name to be loaded in device
     * 
     * @author Sumathi Gunasekaran
     */
    public static boolean upgradeDeviceWithGivenFirmwareVersion(Dut device, AutomaticsTapApi tapEnv,
	    String buildImageName) {

	// Variable to store firmware upgrade status
	boolean status = false;
	// Variable to store error message
	String errorMessage = null;

	try {

	    String currentImageName = FirmwareDownloadUtils.getCurrentFirmwareFileNameForCdl(tapEnv, device);

	    LOGGER.info("REQUESTED IMAGE NAME FOR CODE DOWNLOAD  : " + buildImageName);
	    LOGGER.info("CURRENT BUILD IMAGE VERSION BEFORE TRIGGERING CDL : " + currentImageName);

	    // Configure RDKB device for Xconf cdl
	    if (CommonMethods.isNotNull(currentImageName) && CommonMethods.isNotNull(buildImageName)) {

		if (!currentImageName.equalsIgnoreCase(buildImageName)) {

		    // Verify whether the requested image is available or not in CDL server
		    // if It is not available it will copy from s3 server to CDL server the check the status
		    status = BroadBandCodeDownloadUtils.verifyImageAvailabilityInCDLServer(device, tapEnv,
			    buildImageName);

		    if (status) {

			LOGGER.info("SUCCESSFULLY VERIFIED REQUESTED BUILD IS AVAILABILE IN CDL SERVER");

			/**
			 * Perform CDL using backdoor CDL mechanism For validation purpose we are creating one text file
			 * in /nvram and storing all the cdl logs
			 * 
			 * Finally after completion of cdl we need to make sure user created file is deleted.
			 */
			// BroadBandCodeDownloadUtils.performRdkbHttpCodeDownloadUsingXconfDeviceCommand(device, tapEnv,
			// buildImageName);

			// Wait for 8 minutes and verify whether device is updated with GA build
			status = BroadBandCodeDownloadUtils.waitAndVerifyImageNameAfterCDL(device, tapEnv,
				buildImageName);

		    } else {
			errorMessage = "Requested in not available in CDL server or Not able to copy image from s3 server to CDL server..So not able to load device with GA build";
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);

		    }

		} else {
		    LOGGER.info("CURRENT RUNNING IMAGE AND REQUESTED IMAGE FOR CDL ARE SAME..SO SKIPPING CDL !!!");
		}

	    } else {
		errorMessage = "Obtained null response..Not able to get the current build version details before triggering CDL !!!";
		LOGGER.error(errorMessage);
	    }

	} catch (Exception exception) {
	    errorMessage = "Exception occurred during snmp execution : " + exception.getMessage();
	    LOGGER.error(errorMessage);
	} finally {
	    // Delete user defined text file in nvram after completion of cdl
	    tapEnv.executeCommandUsingSsh(device, RDKBTestConstants.CMD_REMOVE_DIR_FORCEFULLY
		    + RDKBTestConstants.SINGLE_SPACE_CHARACTER + BroadBandTestConstants.FILE_TO_STORE_CDL_LOGS);
	}

	return status;
    }
    
    /**
     * Method to verify Firmware image availability in CDL server
     * 
     * if image is not available it will copy from s3 server to CDL server
     * 
     * @param device
     *            device to be verified
     * @return true, if image is available in CDL server
     * @author Sumathi Gunasekaran
     */

    public static boolean verifyImageAvailabilityInCDLServer(Dut device, AutomaticsTapApi tapEnv,
	    String buildNameToBeTriggerred) {

	LOGGER.debug("STARING METHOD : 'verifyImageAvailabilityInCDLServer'");
	// Variable to store execution status
	boolean status = false;
	String errorMessage = null;
	try {

	    LOGGER.info("Verify whether obtained GA build is available or not in CDL server");
	    // verify image availability in CDL server
	    // if image is not available in CDL server copy image from S3 server to CDL server and verify status
	    LOGGER.info("Requested GA build availability in CDL server : " + status);

	} catch (Exception exception) {
	    errorMessage = "Exception occured while checking image availability: " + exception.getMessage();
	    LOGGER.error(errorMessage);
	    throw new TestException(errorMessage);
	}
	LOGGER.debug("ENDING METHOD : 'verifyImageAvailabilityInCDLServer'");
	return status;
    }
    
    /**
     * Wait for 5 minutes and verify download completed status
     * 
     * then reboot if code download is success
     * 
     * @param device
     *            device to be verified
     * @param tapApi
     *            AutomaticsTapApi instance
     * @param tapApi
     *            AutomaticsTapApi instance
     * @author Sumathi Gunasekaran
     */
    public static boolean waitAndVerifyImageNameAfterCDL(Dut device, AutomaticsTapApi tapEnv,
	    String buildNameToBeTriggerred) {

	LOGGER.debug("STARTING METHOD : 'waitAndVerifyImageNameAfterCDL'");
	boolean status = false;

	String response = tapEnv.executeCommandUsingSsh(device, RDKBTestConstants.CAT_COMMAND
		+ RDKBTestConstants.SINGLE_SPACE_CHARACTER + BroadBandTestConstants.FILE_TO_STORE_CDL_LOGS);

	if (CommonMethods.isNotNull(response)
		&& (response.contains(BroadBandCdlConstants.LOG_MESSAGE_HTTP_DOWNLOAD_COMPLETED)
			|| response.contains(BroadBandCdlConstants.LOG_MESSAGE_HTTP_SUCCESSFULLY_INSTALLED))) {

	    tapEnv.reboot(device);

	    CommonMethods.waitForEstbIpAcquisition(tapEnv, device);
	    status = CodeDownloadUtils.verifyImageVersionFromVersionText(tapEnv, device, buildNameToBeTriggerred);
	    if (!status) {
		LOGGER.info(
			"Device is not loaded with requested build. So verify firmware upgrade with another reboot");
		CommonMethods.rebootAndWaitForIpAccusition(device, tapEnv);
		status = CodeDownloadUtils.verifyImageVersionFromVersionText(tapEnv, device, buildNameToBeTriggerred);
	    }
	    LOGGER.info("Is Device loaded with requested build : " + status);
	} else {
	    LOGGER.error("HTTP CDL using Xconf device commands failed !!!!");
	}
	LOGGER.debug("ENDING METHOD : 'waitAndVerifyImageNameAfterCDL'");

	return status;
    }
}

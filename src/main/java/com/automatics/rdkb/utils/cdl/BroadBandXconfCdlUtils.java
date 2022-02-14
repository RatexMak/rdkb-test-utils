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

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Dut;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.constants.BroadBandCdlConstants;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.utils.xconf.FirmwareConfigurations;
import com.automatics.utils.xconf.XConfUtils;
import com.automatics.webpa.WebPaParameter;

/**
 * Utility class which handles the RDK B xconf code download mechanism.
 * 
 * @author Gnanaprakasham S
 */
public class BroadBandXconfCdlUtils {

    /** SLF4J logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FirmwareDownloadUtils.class);

    /**
     * Keys to store rdk logs
     */
    private static final String KEY_FOR_CDL_DOWNLOAD_SUCCESS_LOG = "CDL_DOWNLOAD_SUCCESS_LOG";
    private static final String KEY_FOR_CDL_DOWNLOAD_STARTED_LOG = "CDL_DOWNLOAD_STARTED_LOG";
    private static final String KEY_FOR_CDL_DOWNLOAD_INITIATED_LOG = "CDL_DOWNLOAD_INITIATED_LOG";

    /**
     * SSH Connection error - Connection refused.
     */
    public static final String SSH_CONNECTION_ERROR_CONNECTION_REFUSED = "Connection refused";

    /**
     * Helper method to initiate xconf code download using tr-181 parameter
     * 
     * Device.X_COMCAST-COM_Xcalibur.Client.xconfCheckNow
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * 
     * @param device
     *            {@link device}
     * 
     * @return Boolean status for setting the webpa parameter to true
     * 
     * @author Gnanaprakasham S
     * 
     */
    public static boolean initiateXconfCdlThroughWebpa(AutomaticsTapApi tapEnv, Dut device) {

	LOGGER.debug("ENTERING METHOD: initiateXconfCdlThroughWebpa");

	tapEnv.executeCommandInSettopBox(device, "rm -rf /rdklogs/logs/xconf.txt");
	tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
	tapEnv.executeCommandInSettopBox(device, "tail -f " + BroadBandTestConstants.RDKLOGS_LOGS_XCONF_TXT_0
		+ " >> /rdklogs/logs/xconf.txt");

	WebPaParameter webPaParameter = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		BroadBandWebPaConstants.WEBPA_PARAM_FOR_TRIGGERING_XCONF_CDL, BroadBandTestConstants.TRUE,
		WebPaDataTypes.BOOLEAN.getValue());

	LOGGER.debug("EXITING METHOD: initiateXconfCdlThroughWebpa");

	return BroadBandCommonUtils.setWebPaParam(tapEnv, device, webPaParameter);
    }
    /**
     * Utility method which creates a swupdate.conf in opt directory and insert the XCONF software update URL.
     *
     * @param tapEnv
     *            TheAutomaticsTapApi instance to access the cats properties
     * @param device
     *            The Dut instance
     */
    public static void updateSoftwareUpdateConfigurationOnClient(AutomaticsTapApi tapEnv, Dut device) {

	String REDIRECT_OPERATOR = " > ";

	String xconfServerUrl = XConfUtils.getXconfServerUrl(device);
	String swUpdateConfFile = "/nvram/swupdate.conf";
	String consoleoutput = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.CMD_ECHO + " \"" + xconfServerUrl
		+ "\"" + REDIRECT_OPERATOR + swUpdateConfFile);
	LOGGER.info("updateSoftwareUpdateConfigurationOnClient: configuration on client : " + consoleoutput);

	tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
	tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.CMD_SYNC);

    }
    /**
     * Configure RDKB device for XCONF Code download
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link device}
     * @param imageName
     *            image name for cdl
     * @param rebootImmediately
     *            true or false
     * @param protocol
     *            protocol for CDL download
     * @param delayDownload
     *            delayDownload to delay time
     */
    public static void configureRdkbDeviceForXconfCdl(AutomaticsTapApi tapEnv, Dut device, String imageName,
	    boolean rebootImmediately, String protocol, int delayDownload) {
	try {
	    LOGGER.debug("STARTING METHOD : configureRdkbDeviceForXconfCdl()");
	    LOGGER.info("Going to initiate the XCONF " + protocol.toUpperCase() + "(rebootImmediately:"
		    + rebootImmediately + ") CDL with version " + imageName);

	    // Create /nvram/swupdate.conf file and add the software update url.
	    updateSoftwareUpdateConfigurationOnClient(tapEnv, device);

	    XConfUtils.configureXconfDownloadFirmwareDetails(device, imageName, rebootImmediately, protocol, null, XConfUtils.getFirmwareLocation(protocol, device, imageName), delayDownload);
	    LOGGER.debug("ENDING METHOD : configureRdkbDeviceForXconfCdl()");
	} catch (Exception e) {
	    throw new TestException("XCONF CONFIGURATION FAILURE " + e.getMessage());
	}
    }

   /* *//**
     * Utility method to trigger the XCONF download using shell script and verify whether code download is started from
     * log message.
     * 
     * @param device
     *            The device to be validated.
     * @param downloadStatusCommand
     *            The command to get the download status.
     * @return boolean if XCONF code download is triggered successfully.
     *//*
    public static boolean triggerXconfDownloadAndVerifyWhetherDownloadStartedUsingLogs(AutomaticsTapApi AutomaticsTapApi,
	    Dut device, String buildImageName) {

	boolean requestAccepted = false;
	boolean requestRejectedForSameVersion = false;

	configureRdkbDeviceForXconfCdl(AutomaticsTapApi, device, buildImageName, false,
		XreTestConstants.FIRMWARE_DOWNLOAD_PROTOCOL_HTTP);

	CommonMethods.rebootAndWaitForIpAccusition(device, AutomaticsTapApi);

	// AutomaticsTapApi.executeCommandIndeviceBox(device, "tail -f " + BroadBandTestConstants.RDKLOGS_LOGS_XCONF_TXT_0 +
	// " >> /rdklogs/logs/xconf.txt");

	for (int cdlCheckCount = 0; cdlCheckCount < 40; cdlCheckCount++) {

	    String response = AutomaticsTapApi.executeCommandUsingSsh(device, BroadBandTestConstants.CAT_XCONF_TXT)
		    .toLowerCase();

	    requestAccepted = verifyWhetherDownloadRequestAcceptedUsingLogMessage(response);

	    if (!requestAccepted) {
		requestRejectedForSameVersion = verifyCdlRequestRejectedWithSameVersionUsingLogMessage(response);
	    }
	    if (!requestAccepted && !requestRejectedForSameVersion) {
		response = AutomaticsTapApi
			.executeCommandUsingSsh(device, BroadBandTestConstants.CMD_GREP_CDL_DOWNLOAD_STATUS)
			.toLowerCase();
		requestAccepted = verifyWhetherDownloadRequestAcceptedUsingLogMessage(response);
		if (!requestAccepted) {
		    requestRejectedForSameVersion = verifyCdlRequestRejectedWithSameVersionUsingLogMessage(response);
		}
	    }

	    if (requestRejectedForSameVersion) {
		requestAccepted = false;
		break;
	    } else if (requestAccepted) {
		break;
	    }

	    AutomaticsTapApi.waitTill(BroadBandTestConstants.FIFTEEN_SECONDS_IN_MILLIS);
	}

	// Verifying CDL status
	return requestAccepted;
    }*/

    /**
     * Configure RDKB device for XCONF Code download
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link device}
     * @param imageName
     *            image name for cdl
     * @param rebootImmediately
     *            true or false
     * @param protocol
     *            protocol for CDL download
     */
    public static void configureRdkbDeviceForXconfCdl(AutomaticsTapApi tapEnv, Dut device, String imageName,
	    boolean rebootImmediately, String protocol) {

	LOGGER.info("Going to initiate the XCONF " + protocol.toUpperCase() + "(rebootImmediately:" + rebootImmediately
		+ ") CDL with version " + imageName);

	// Create /nvram/swupdate.conf file and add the software update url.
	updateSoftwareUpdateConfigurationOnClient(tapEnv, device);

	XConfUtils.configureXconfDownloadFirmwareDetails(device, imageName, rebootImmediately, protocol);
    }

    /**
     * Utility method to validate whether http code download is started using different log messages.
     * 
     * @param commandoutput
     *            The command output.
     * @return True if required logs are available in command response.
     *//*
    public static boolean verifyWhetherDownloadStartedUsingLogMessage(String commandoutput) {
	boolean downloadStarted = CommonUtils.isGivenStringAvailableInCommandOutput(commandoutput,
		BroadBandTraceConstants.HTTP_DOWNLOAD_STARTED_LOG);
	if (!downloadStarted) {
	    downloadStarted = CommonUtils.isGivenStringAvailableInCommandOutput(commandoutput,
		    BroadBandTraceConstants.LOG_MESSAGE_HTTP_DOWNLOAD_STARTED);
	}
	return downloadStarted;
    }

    *//**
     * Utility method to validate whether http code download is completed using different log messages.
     * 
     * @param commandoutput
     *            The command output.
     * @return True if required logs are available in command response.
     *//*
    public static boolean verifyWhetherDownloadCompletedUsingLogMessage(String commandoutput) {

	boolean downloadCompleted = CommonUtils.isGivenStringAvailableInCommandOutput(commandoutput,
		BroadBandTraceConstants.HTTP_DOWNLOAD_COMPLETED_LOG);
	if (!downloadCompleted) {
	    downloadCompleted = CommonUtils.isGivenStringAvailableInCommandOutput(commandoutput,
		    BroadBandTraceConstants.LOG_MESSAGE_HTTP_DOWNLOAD_COMPLETED);
	}
	return downloadCompleted;
    }

    *//**
     * Utility method to validate whether http code download request is accepted using log messages.
     * 
     * @param commandoutput
     *            The command output.
     * @return True if required logs are available in command response.
     *//*
    public static boolean verifyWhetherDownloadRequestAcceptedUsingLogMessage(String response) {

	return CommonUtils.isGivenStringAvailableInCommandOutput(response,
		BroadBandTraceConstants.LOG_MESSAGE_HTTP_DOWNLOAD_REQUEST_ACCEPTED);
    }

    *//**
     * Utility method to verify whether code download is successfully completed using log message.
     * 
     * @param response
     *            The command output.
     * @return True if required logs are available in command response.
     *//*
    public static boolean verifyWhetherCodeDownlaodCompletedSuccessfullyUsingLogMessage(String response) {

	return CommonUtils.isGivenStringAvailableInCommandOutput(response,
		BroadBandTraceConstants.HTTP_DOWNLOAD_SUCCESS_LOG);
    }

    *//**
     * Utility method to verify whether code download completed and waiting for reboot window using log message.
     * 
     * @param response
     * @return True if required logs are available in command response.
     *//*
    public static boolean verifyWhetherCdlCompletedAndWaitingForMaintenanceWindowUsingLogMessage(String response) {

	LOGGER.info("cdl logs : " + BroadBandTraceConstants.MAINTENANCE_WINDOW_LOG);
	return CommonUtils.isGivenStringAvailableInCommandOutput(response,
		BroadBandTraceConstants.MAINTENANCE_WINDOW_LOG);
    }

    *//**
     * Utility method to verify whether code download is failed using log message.
     * 
     * @param response
     *            The command output.
     * @return True if required logs are available in command response.
     *//*
    public static boolean verifyWhetherCdlUpgradeFailedUsingLogMessage(String response) {

	return CommonUtils.isGivenStringAvailableInCommandOutput(response,
		BroadBandTraceConstants.LOG_MESSAGE_HTTP_DOWNLOAD_NOT_SUCCESSFUL);
    }

    *//**
     * Utility method to verify whether code download request rejected with same version using log message.
     * 
     * @param response
     *            The command output.
     * @return True if required logs are available in command response.
     *//*
    public static boolean verifyCdlRequestRejectedWithSameVersionUsingLogMessage(String response) {

	return CommonUtils.isGivenStringAvailableInCommandOutput(response,
		BroadBandTraceConstants.LOG_MESSAGE_HTTP_DOWNLOAD_REQUEST_REJECTED_WITH_SAME_VERSION);
    }

    *//**
     * Method to return cdl initialization log
     * 
     * @param device
     *            instance of {@link device}
     * @return cdl Initialization Log
     *//*
    public static String getCdlInitializationLog(Dut device) {
	// Log for cdl initialization
	String cdlInitializationLog = "XCONF SCRIPT : Download image from HTTP server";
	return cdlInitializationLog;
    }

    *//**
     * Method to return cdl download started logs
     * 
     * @param device
     *            instance of {@link device}
     * @return cdl Download Started Log
     *//*
    public static String getCdlDownloadStartedLog(Dut device) {
	// Log for firmware download started
	String cdlDownloadStartedLog = "### httpdownload started ###";
	return cdlDownloadStartedLog;
    }

    *//**
     * Method to return cdl download completed log
     * 
     * @param device
     *            instance of {@link device}
     * @return cdl Download Completed Log
     *//*
    public static String getCdlDownloadCompletedLog(Dut device) {
	// Log for firmware download completed
	String cdlDownloadCompletedLog = "### httpdownload completed ###";
	return cdlDownloadCompletedLog;
    }

    *//**
     * Method to return cdl download success log
     * 
     * @param device
     *            instance of {@link device}
     * @return cdl Download Success log
     *//*
    public static String getCdlDownloadSuccessLog(Dut device) {
	// Log for firmware download started
	String cdlDownloadSuccess = "XCONF SCRIPT : HTTP download Successful";
	return cdlDownloadSuccess;
    }

    *//**
     * Method to get CDL Logs for validation
     * 
     * @param device
     *            instance of {@link device}
     * @return CDL Logs for validation
     */
    public static String getCdlLogsForValidation(AutomaticsTapApi tapEnv, Dut device) {

	// CDL Log for validation
	String cdlLogsForValidation = AutomaticsConstants.EMPTY_STRING;

	// retrieve all available cdl logs from the device
	HashMap<String, String> cdlLogs = getCdlStatusLogs(tapEnv, device);

	if (cdlLogs.containsKey(KEY_FOR_CDL_DOWNLOAD_SUCCESS_LOG)) {
	    cdlLogsForValidation = cdlLogs.get(KEY_FOR_CDL_DOWNLOAD_SUCCESS_LOG);
	} else if (cdlLogs.containsKey(KEY_FOR_CDL_DOWNLOAD_STARTED_LOG)) {
	    cdlLogsForValidation = cdlLogs.get(KEY_FOR_CDL_DOWNLOAD_STARTED_LOG);
	} else if (cdlLogs.containsKey(KEY_FOR_CDL_DOWNLOAD_INITIATED_LOG)) {
	    cdlLogsForValidation = cdlLogs.get(KEY_FOR_CDL_DOWNLOAD_INITIATED_LOG);
	}
	return cdlLogsForValidation;
    }

   /**
     * Method to wait for given message in RDKB Logs
     * 
     * @param device
     *            instance of {@link device}
     * @return CDL logs
     */
    private static HashMap<String, String> getCdlStatusLogs(AutomaticsTapApi tapEnv, Dut device) {

	LOGGER.debug("STARTING METHOD: getCdlLogsForValidation()");
	// maximum loop count
	int maxLoopCount = 10;
	// CDL response
	String response = null;
	// command to retrieve CDL logs
	String command = "cat /rdklogs/logs/xconf.txt";

	// CDL LOGS FOR VALIDATION

	// Log for cdl initialization
	String cdlInitializationLog = FirmwareDownloadUtils.getCdlInitializationLog(device);
	// Log for firmware download started
	String cdlDownloadStartedLog = FirmwareDownloadUtils.getCdlDownloadStartedLog(device);
	// Log for cdl download success
	String cdlDownloadSuccess = FirmwareDownloadUtils.getCdlDownloadSuccessLog(device);

	// store rdk logs
	HashMap<String, String> cdlLogs = new HashMap<String, String>();
	if (!CommonMethods.isFileExists(device, tapEnv, "/rdklogs/logs/xconf.txt")) {
	    command = BroadBandTestConstants.CAT_COMMAND + BroadBandTestConstants.RDKLOGS_LOGS_XCONF_TXT_0;
	}

	for (int index = 1; index <= maxLoopCount; index++) {

	    LOGGER.info(index + "/" + maxLoopCount + "#waiting for the cdl logs ");

	    response = tapEnv.executeCommandInSettopBox(device, command);

	    if (CommonMethods.isNotNull(response)) {

		// validation for cdl initialization
		if (!cdlLogs.containsKey(KEY_FOR_CDL_DOWNLOAD_INITIATED_LOG)
			&& response.contains(cdlInitializationLog)) {
		    cdlLogs.put(KEY_FOR_CDL_DOWNLOAD_INITIATED_LOG, response);
		}

		// validation for cdl download started
		if (!cdlLogs.containsKey(KEY_FOR_CDL_DOWNLOAD_STARTED_LOG)
			&& response.contains(cdlDownloadStartedLog)) {
		    cdlLogs.put(KEY_FOR_CDL_DOWNLOAD_STARTED_LOG, response);
		}

		// validation for cdl download success
		if (!cdlLogs.containsKey(KEY_FOR_CDL_DOWNLOAD_SUCCESS_LOG) && response.contains(cdlDownloadSuccess)) {
		    cdlLogs.put(KEY_FOR_CDL_DOWNLOAD_SUCCESS_LOG, response);
		}

		
		
		 

		// No upgrade required
		if (response.contains(BroadBandCdlConstants.XCONF_HTTP_DOWNLOAD_NOT_SUCCESSFUL)) {
		    LOGGER.info(
			    "Exiting CDL logs retrieval since retrieved 'XCONF SCRIPT : HTTP download NOT Successful'");
		    break;
		}

		// No upgrade required
		if (response.contains(BroadBandCdlConstants.XCONF_CDL_UPGRADE_OR_DOWNGRADE_NOT_REQUIRED)) {
		    LOGGER.info("Exiting CDL logs retrieval since retrieved 'No upgrade/downgrade required'");
		    break;
		}

		// handle for connection refused scenarios
		if (response.contains(SSH_CONNECTION_ERROR_CONNECTION_REFUSED) && (index > 15)) {
		    LOGGER.info("Exiting CDL logs retrieval since retrieved 'port 22: Connection refused'");
		    break;
		}
	    }

	    // exit cdl logs verification since STB not responding after code download started
	    if (CommonMethods.isNull(response) && cdlLogs.containsKey(KEY_FOR_CDL_DOWNLOAD_STARTED_LOG)
		    && (index > 20)) {
		LOGGER.info(
			"Exiting CDL logs retrieval since matched all the following loop exit scenarios 1) Retrieved server response as empty for the command '"
				+ command + "' 2) retrieved '" + cdlDownloadStartedLog
				+ "' from from RDK logs 3) loop count greater than 20");
		break;
	    }
	    if (cdlLogs.containsKey(KEY_FOR_CDL_DOWNLOAD_SUCCESS_LOG)) {
		break;
	    }

	    tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
	}

	LOGGER.debug("ENDING METHOD: getCdlLogsForValidation()");

	return cdlLogs;
    }

   /**
     * Method to retrieve and validate the XCONF configuration details from RDK Logs
     * 
     * @param rdkLogContents
     *            logs
     * @param imageName
     *            image name
     * @param rebootImmediately
     *            reboot immediately status
     * @param downloadProtocol
     *            CDL Download protocol
     */
    public static void retrieveAndValidateXconfConfiguration(String rdkLogContents, String imageName,
	    String rebootImmediately, String downloadProtocol) {
	// validation status
	boolean status = false;
	// XCONF Configuration from log file
	String xconfConfiguration = null;
	// error message
	String errorMessage = null;

	if (CommonMethods.isNotNull(rdkLogContents)) {

	    // verifying the presents of HTTP RESPONSE CODE is 200 is log file
	    status = rdkLogContents.contains(BroadBandCdlConstants.HTTP_SUCCESS_RESPONSE_FOR_XCONF_CDL);

	    if (status) {
		// retrieve XCONF Configuration from log file
		xconfConfiguration = CommonMethods.patternFinder(rdkLogContents,
			BroadBandCdlConstants.PATTERN_TO_RETRIEVE_XCONF_CONFIGURATION);
		if (CommonMethods.isNotNull(xconfConfiguration)) {
		    status = FirmwareDownloadUtils.validateXconfConfiguration(xconfConfiguration, downloadProtocol,
			    imageName, rebootImmediately);
		    errorMessage = "XCONF configuration comparison failed. ie, actual: " + xconfConfiguration;
		} else {
		    errorMessage = "Unable to retrieve XCONF Configuration from "
			    + BroadBandTestConstants.RDKLOGS_LOGS_XCONF_TXT_0;
		}
	    } else {
		errorMessage = "Unable to find " + BroadBandCdlConstants.HTTP_SUCCESS_RESPONSE_FOR_XCONF_CDL
			+ " message after initiating XCONF CDL in " + BroadBandTestConstants.RDKLOGS_LOGS_XCONF_TXT_0;
	    }

	} else {
	    errorMessage = "Skipping the XCONF configuration validation since the given log snippet is empty/null";
	}

	if (!status) {
	    throw new TestException(errorMessage);
	}
    }

  /**
     * Clear Cdl Info in Xconf
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link device}
     */
    public static boolean toClearCdlInfoInXconf(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug("STARTING METHOD : ToClearCdlInfoInXconf()");
	boolean status = false;
	String errorMessage = null;
	errorMessage = "Failed to Clear the code download URL information in /nvram/swupdate.conf";
	try {
	    FirmwareDownloadUtils.deleteSoftwareUpdateConfigurationFile(tapEnv, device);
	    if (CommonMethods.isFileExists(device, tapEnv, BroadBandCommandConstants.FILE_PATH_TMP_XCONF_URL_OVERRIDE)) {
		FirmwareDownloadUtils.deleteXconfUlrOverrideConfigurationFile(tapEnv, device);
	    }
	    status = true;
	} catch (Exception exception) {
	    errorMessage = "Exception occured while clearing cdlInfo in Xconf " + exception.getMessage();
	    LOGGER.error(errorMessage);
	}
	LOGGER.debug("ENDING METHOD : ToClearCdlInfoInXconf()");
	return status;
    }
    /**
     * Configure RDKB device for XCONF Code download
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param settop
     *            instance of {@link Settop}
     * @param imageName
     *            image name for cdl
     * @param rebootImmediately
     *            true or false
     * @param protocol
     *            protocol for CDL download
     */
    public static void configureRdkbDeviceForXconfCdlWithSwupdate(AutomaticsTapApi tapEnv, Dut device, String imageName,
	    boolean rebootImmediately, String protocol, boolean isSwupdateRequired) {

	LOGGER.info("Going to initiate the XCONF " + protocol.toUpperCase() + "(rebootImmediately:" + rebootImmediately
		+ ") CDL with version " + imageName);

	if (isSwupdateRequired) {
	    // Create /nvram/swupdate.conf file and add the software update url.
	    updateSoftwareUpdateConfigurationOnClient(tapEnv, device);
	}
	XConfUtils.configureXconfDownloadFirmwareDetails(device, imageName, rebootImmediately, protocol);
    }
    
    /**
     * Method used to preform XCONF CDL and retrieving the XCONF code download logs
     * 
     * @param device
     *            instance of{@link Dut}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param firmwareVersionToUpgrade
     *            Firmware Version To Upgrade
     * 
     * @return Code downloaded log file from /rdklogs/logs/xconf.txt.0
     */
    public static String triggerXconfCodeDownloadWithSwupdateOption(Dut device, AutomaticsTapApi tapEnv,
	    String firmwareVersionToUpgrade, boolean isSwupdateRequired) {
	LOGGER.debug("STARTING METHOD : triggerXconfCodeDownloadWithSwupdateOption()");
	String cdlLogsForValidation = null;
	String errorMessage = null;
	boolean status = false;
	try {
	    // Configure RDKB device for XCONF Code download
	    try {
		configureRdkbDeviceForXconfCdlWithSwupdate(tapEnv, device, firmwareVersionToUpgrade, false,
			BroadBandTestConstants.FIRMWARE_DOWNLOAD_PROTOCOL_HTTP, isSwupdateRequired);
		status = true;
		LOGGER.info("SUCCESSFULLY CONFIGURED XCONF CODE DOWNLOAD");
	    } catch (Exception exception) {
		errorMessage = "Exception occured while configuring xconf code download " + exception.getMessage();
		LOGGER.error(errorMessage);
	    }
	    if (status) {
		tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.FILE_FIRMWARE_SCHED_SH);
		// wait for 2 minutes to get the code download logs
		LOGGER.info("WAITING FOR TWO MINUTES TO GET THE CODE DOWNLOAD LOGS");
		tapEnv.waitTill(BroadBandTestConstants.TWO_MINUTES);
		try {
		    cdlLogsForValidation = getCdlLogsForValidation(tapEnv, device);
		} catch (Exception exception) {
		    errorMessage = "Exception occured while validating cdl logs " + exception.getMessage();
		    LOGGER.error(errorMessage);
		}
	    } else {
		errorMessage = "FAILED TO INITIATE XCONF CDL THROUGH firmwareSched.sh";
		LOGGER.error(errorMessage);
	    }
	} catch (Exception exception) {
	    errorMessage = "Exception occured while performing xconf upgrade " + exception.getMessage();
	    LOGGER.error(errorMessage);
	}
	LOGGER.debug("ENDING METHOD : triggerXconfCodeDownloadWithSwupdateOption()");
	return cdlLogsForValidation;
    }
    
    /**
     * Perform Reboot and verify Xconf CDL
     * 
     * @param settop
     *            instance of{@link Settop}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param firmwareVersionToUpgrade
     *            Firmware Version To Upgrade.
     * @param cdlLogsForValidation
     *            Cdl Logs For Validation.
     * 
     * @return true if device is loaded with upgraded build
     */
    public static boolean verifyCdlandReboot(Dut device, AutomaticsTapApi tapEnv, String firmwareVersionToUpgrade,
	    String cdlLogsForValidation) {
	boolean isTrigggered = false;
	String cdlSuccessLogs = null;
	String errorMessage = null;
	boolean status = false;
	LOGGER.debug("STARTING METHOD : verifyCdlandReboot()");
	// Validate XCONF configuration details from RDK Logs
	try {
	    FirmwareDownloadUtils.retrieveAndValidateXconfConfiguration(cdlLogsForValidation, firmwareVersionToUpgrade,
		    BroadBandTestConstants.FALSE, BroadBandTestConstants.FIRMWARE_DOWNLOAD_PROTOCOL_HTTP);
	} catch (Exception exception) {
	    errorMessage = "Exception occured while Validating XCONF configuration details from RDK Logs"
		    + exception.getMessage();
	    LOGGER.error(errorMessage);
	}
	// Verify code download status
	cdlSuccessLogs = FirmwareDownloadUtils.getCdlDownloadSuccessLog(device);
	status = cdlLogsForValidation.toLowerCase().contains(cdlSuccessLogs.toLowerCase());
	LOGGER.info("CODE DOWNLOAD STATUS : " + status);
	if (status) {
	    status = BroadBandCommonUtils.rebootAndWaitForStbAccessible(device, tapEnv);
	    if (status) {
		isTrigggered = firmwareVersionToUpgrade.contains(FirmwareDownloadUtils.getCurrentFirmwareFileNameForCdl(tapEnv, device));
		LOGGER.info("SUCCESSFULLY VERIFIED THE LATEST IMAGE " + isTrigggered);
	    }
	} else {
	    errorMessage = "FAILED TO VERIFY CODE DOWNLOAD SUCCESS MESSAGE IN CDL LOGS ";
	    LOGGER.error(errorMessage);
	}
	LOGGER.debug("ENDING METHOD : verifyCdlandReboot()");
	return isTrigggered;
    }
    /**
     * Helper method to configure the XCONF firmware configuration details with Invalid url.
     * 
     *
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance.
     * @param settop
     *            The settop to be used.
     * @param imageVersion
     *            The name of image version.
     * @param rebootImmediately
     *            The reboot immediately flag.
     * @param protocol
     *            The transport protocol used.
     * 
     * @return FirmwareConfiguration The object which holds Xconf configurations
     * 
     * @author sgunas200
     */
    public static FirmwareConfigurations configureXconfDownloadFirmwareDetailsWithInvalidURL(final AutomaticsTapApi tapEnv ,
	    final Dut device, String imageVersion, final boolean rebootImmediately, final String protocol,
	    int upgradeDelay) {

	return XConfUtils.configureXconfDownloadFirmwareDetails(device, imageVersion, rebootImmediately, protocol, null,
		XConfUtils.getFirmwareLocation( protocol, device, imageVersion), upgradeDelay);
    }
    
    /**
    * Method used to preform XCONF CDL and retrieving the XCONF code download logs
    * 
    * @param device
    *            instance of{@link Dut}
    * @param tapEnv
    *            instance of {@link AutomaticsTapApi}
    * @param firmwareVersionToUpgrade
    *            Firmware Version To Upgrade
    * 
    * @return Code downloaded log file from /rdklogs/logs/xconf.txt.0
    * @Refactor Sruthi Santhosh
    */
   public static String triggerXconfCodeDownload(Dut device, AutomaticsTapApi tapEnv, String firmwareVersionToUpgrade) {
	String cdlLogsForValidation = null;
	String errorMessage = null;
	boolean status = false;
	LOGGER.debug("STARTING METHOD : triggerXconfCodeDownload()");
	try {
	    // Configure RDKB device for XCONF Code download
	    try {
		configureRdkbDeviceForXconfCdl(tapEnv, device, firmwareVersionToUpgrade, false,
			BroadBandTestConstants.FIRMWARE_DOWNLOAD_PROTOCOL_HTTP);
		status = true;
		LOGGER.info("SUCCESSFULLY CONFIGURED XCONF CODE DOWNLOAD");
	    } catch (Exception exception) {
		errorMessage = "Exception occured while configuring xconf code download " + exception.getMessage();
		LOGGER.error(errorMessage);
	    }
	    if (status && BroadBandXconfCdlUtils.initiateXconfCdlThroughWebpa(tapEnv, device)) {
		LOGGER.info("SUCCESSFULLY INITIATED XCONF CDL THROUGH WEBPA");
		// wait for 5 minutes to get the code download logs
		LOGGER.info("WAITING FOR FIVE MINUTES TO GET THE CODE DOWNLOAD LOGS");
		tapEnv.waitTill(BroadBandTestConstants.FIVE_MINUTES);
		try {
		    cdlLogsForValidation = getCdlLogsForValidation(tapEnv, device);
		} catch (Exception exception) {
		    errorMessage = "Exception occured while validating cdl logs " + exception.getMessage();
		    LOGGER.error(errorMessage);
		}
	    } else {
		errorMessage = "FAILED TO INITIATE XCONF CDL THROUGH WEBPA";
		LOGGER.error(errorMessage);
	    }
	} catch (Exception exception) {
	    errorMessage = "Exception occured while performing xconf upgrade " + exception.getMessage();
	    LOGGER.error(errorMessage);
	}
	LOGGER.debug("ENDING METHOD : triggerXconfCodeDownload()");
	return cdlLogsForValidation;
   }
}
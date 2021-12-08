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
}
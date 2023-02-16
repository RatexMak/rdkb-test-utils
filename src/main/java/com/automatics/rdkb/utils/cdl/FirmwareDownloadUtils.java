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
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.CommonMethods;
import com.automatics.utils.xconf.XConfUtils;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;
import com.automatics.rdkb.utils.CommonUtils;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.snmp.SnmpDataType;
import com.automatics.rdkb.constants.BroadBandCdlConstants;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandPropertyKeyConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpUtils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.wifi.BroadBandWiFiUtils;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiTestConstant;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpMib;

public class FirmwareDownloadUtils {
	/** SLF4J logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FirmwareDownloadUtils.class);

	/**
	 * Keys to store rdk logs
	 */
	private static final String KEY_FOR_CDL_DOWNLOAD_SUCCESS_LOG = "CDL_DOWNLOAD_SUCCESS_LOG";
	private static final String KEY_FOR_CDL_DOWNLOAD_STARTED_LOG = "CDL_DOWNLOAD_STARTED_LOG";
	private static final String KEY_FOR_CDL_DOWNLOAD_INITIATED_LOG = "CDL_DOWNLOAD_INITIATED_LOG";

	/**
	 * Maximum wait time for completing the code downlaod using TFTP via SNMP.
	 */
	private static final int MAX_WAIT_TIME_SNMP_TFTP_CODE_DOWNLOAD = 10;
	/**
	 * SNMP TFTP Download - Operation status - Failed.
	 */
	private static final String SNMP_TFTP_DOWNLOAD_STATUS_FAILED = "4";

	/**
	 * SNMP TFTP Download - Operation status - Other.
	 */
	private static final String SNMP_TFTP_DOWNLOAD_STATUS_OTHER = "5";

	/**
	 * SSH Connection error - Connection refused.
	 */
	public static final String SSH_CONNECTION_ERROR_CONNECTION_REFUSED = "Connection refused";

	/**
	 * SNMP TFTP Download - Operation status - Complete from provisioning.
	 */
	private static final String SNMP_TFTP_DOWNLOAD_STATUS_COMPLETE_FROM_PROVISIONING = "2";

	/**
	 * SNMP TFTP Download - Operation status - In-Progress.
	 */
	private static final String SNMP_TFTP_DOWNLOAD_STATUS_IN_PROGRESS = "1";

	/**
	 * SNMP TFTP Download - Operation status - Completed From Management.
	 */
	private static final String SNMP_TFTP_DOWNLOAD_STATUS_COMPLETED_FROM_MGNT = "3";

	/**
	 * Method to retrieve current firmware file name for cdl
	 * 
	 * @param device instance of {@link Dut}
	 * @return current firmware file name for cdl
	 */
	public static String getCurrentFirmwareFileNameForCdl(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("STARTING METHOD: getCurrentFirmwareFileNameForCdl()");

		// file name retrieved for performing the cdl
		String firmwareFileName = null;

		// retrieve current firmware version using webpa command
		firmwareFileName = BroadBandCommonUtils.getCurrentlyRunningImageVersionUsingWebPaCommand(tapEnv, device);
		LOGGER.info("firmwareFileName is " + firmwareFileName
				+ " after the method getCurrentlyRunningImageVersionUsingWebPaCommand");
		// retry from version.txt file
		if (CommonMethods.isNull(firmwareFileName)) {
			firmwareFileName = CodeDownloadUtils.getCurrentRunningImageNameFromVersionTxtFile(device, tapEnv);
			LOGGER.info("firmwareFileName is " + firmwareFileName
					+ " after the method getCurrentRunningImageNameFromVersionTxtFile");
		}
		LOGGER.debug("ENDING METHOD: getCurrentFirmwareFileNameForCdl()");
		return firmwareFileName;
	}

	/**
	 * Method to get CDL Logs for validation
	 * 
	 * @param device instance of {@link Dut}
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
	 * @param device instance of {@link Dut}
	 * @return CDL logs
	 */
	private static HashMap<String, String> getCdlStatusLogs(AutomaticsTapApi tapEnv, Dut device) {

		LOGGER.debug("STARTING METHOD: getCdlLogsForValidation()");
		// maximum loop count
		int maxLoopCount = 35;
		// CDL response
		String response = null;
		// command to retrieve CDL logs
		String command = "cat " + BroadBandTestConstants.RDKLOGS_LOGS_XCONF_TXT_0;

		// CDL LOGS FOR VALIDATION

		// Log for cdl initialization
		String cdlInitializationLog = FirmwareDownloadUtils.getCdlInitializationLog(device);
		// Log for firmware download started
		String cdlDownloadStartedLog = FirmwareDownloadUtils.getCdlDownloadStartedLog(device);
		// Log for cdl download success
		String cdlDownloadSuccess = FirmwareDownloadUtils.getCdlDownloadSuccessLog(device);

		// store rdk logs
		HashMap<String, String> cdlLogs = new HashMap<String, String>();

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

				/*
				 * handle failure scenarios
				 */

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

			// exit cdl logs verification since STB not responding after code download
			// started
			if (CommonMethods.isNull(response) && cdlLogs.containsKey(KEY_FOR_CDL_DOWNLOAD_STARTED_LOG)
					&& (index > 20)) {
				LOGGER.info(
						"Exiting CDL logs retrieval since matched all the following loop exit scenarios 1) Retrieved server response as empty for the command '"
								+ command + "' 2) retrieved '" + cdlDownloadStartedLog
								+ "' from from RDK logs 3) loop count greater than 20");
				break;
			}
			// verify whether CDL success logs are present
			if (cdlLogs.containsKey(KEY_FOR_CDL_DOWNLOAD_SUCCESS_LOG)) {
				break;
			}
		}

		LOGGER.debug("ENDING METHOD: getCdlLogsForValidation()");

		return cdlLogs;
	}

	/**
	 * Method to return cdl initialization log
	 * 
	 * @param device instance of {@link Dut}
	 * @return cdl Initialization Log
	 */
	public static String getCdlInitializationLog(Dut device) {
		// Log for cdl initialization
		String cdlInitializationLog = "XCONF SCRIPT : Download image from HTTP server";
		return cdlInitializationLog;
	}

	/**
	 * Method to return cdl download started logs
	 * 
	 * @param device instance of {@link Dut}
	 * @return cdl Download Started Log
	 */
	public static String getCdlDownloadStartedLog(Dut device) {
		// Log for firmware download started
		String cdlDownloadStartedLog = "### httpdownload started ###";
		return cdlDownloadStartedLog;
	}

	/**
	 * Method to return cdl download success log
	 * 
	 * @param device instance of {@link Dut}
	 * @return cdl Download Success log
	 */
	public static String getCdlDownloadSuccessLog(Dut device) {
		// Log for firmware download started
		String cdlDownloadSuccess = "XCONF SCRIPT : HTTP download Successful";
		return cdlDownloadSuccess;
	}

	/**
	 * Utility method to get the formatted TFTP Server IPv6 Address.
	 */
	public static String getFormattedTftpServerIpv6Address(String tftpServerAddress) {
		String formattedServerAddress = "";

		LOGGER.info("DAC 15 CDL SERVER - TFTP SERVER IPv6 ADDRESS = " + tftpServerAddress);

		if (CommonMethods.isNotNull(tftpServerAddress)) {
			tftpServerAddress = tftpServerAddress.replaceAll(BroadBandTestConstants.DELIMITER_COLON, "");
			formattedServerAddress = tftpServerAddress.trim();
			formattedServerAddress = tftpServerAddress.replaceAll("(..)", "$1 ").trim();
		}
		LOGGER.info("DAC 15 CDL SERVER - FORMATTED TFTP SERVER IPv6 ADDRESS = " + formattedServerAddress);
		return formattedServerAddress;
	}

	/**
	 * Method to set server address for SNMP image download via HTTP and TFTP both
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device The set-top instance
	 * @return true if Server address is set successfully
	 */
	public static boolean setServerAddressForSnmpCodeDownload(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("STARTING METHOD: setServerAddressForSnmpCodeDownload");
		// Variable declaration starts
		boolean isServerSetSuccessful = false;
		String formattedAddress = "";
		String softwareServerAddress = "";
		String snmpSetResponse = "";
		String snmpGetResonse = "";
		String errorMessage = "";
		// Variable declaration ends

		try {

			/*
			 * String tftpServerAddress = tapEnv
			 * .getCatsPropsValue(BroadBandTestConstants.PROP_KEY_HTTP_SERVER_IPV6_ADDRESS);
			 */
			String tftpServerAddress = AutomaticsPropertyUtility
					.getProperty(BroadBandTestConstants.PROP_KEY_HTTP_SERVER_IPV6_ADDRESS);
			LOGGER.info("tftpServerAddress is  : " + tftpServerAddress);
			softwareServerAddress = getFormattedTftpServerIpv6Address(tftpServerAddress);
			LOGGER.info("Formatted TFTP Server IPv6 Address for SNMP MIB Cross validation  : " + softwareServerAddress);
			if (CommonMethods.isNotNull(softwareServerAddress)) {
				formattedAddress = new StringBuffer().append(BroadBandTestConstants.DOUBLE_QUOTE)
						.append(softwareServerAddress.toUpperCase()).append(BroadBandTestConstants.DOUBLE_QUOTE)
						.toString();
				LOGGER.info("FORMATTED TFTP SERVER IPV6 ADDRESS IN HEX FORMAT : " + formattedAddress);
				LOGGER.info("BroadBandSnmpMib.ECM_DOCS_DEV_SW_SERVER_ADDRESS.getOid() : "
						+ BroadBandSnmpMib.ECM_DOCS_DEV_SW_SERVER_ADDRESS.getOid());
				snmpSetResponse = BroadBandSnmpUtils.snmpSetOnEcm(tapEnv, device,
						BroadBandSnmpMib.ECM_DOCS_DEV_SW_SERVER_ADDRESS.getOid(), SnmpDataType.HEXADECIMAL,
						formattedAddress);
				LOGGER.info("SNMPSET RESPONSE FOR docsDevSwServerAddress: " + snmpSetResponse);
				if (CommonMethods.patternMatcher(snmpSetResponse.toLowerCase(), softwareServerAddress.toLowerCase())) {
					LOGGER.info("BroadBandSnmpMib.ECM_DOCS_DEV_SW_SERVER_ADDRESS.getOid() : "
							+ BroadBandSnmpMib.ECM_DOCS_DEV_SW_SERVER_ADDRESS.getOid());
					snmpGetResonse = BroadBandSnmpUtils.snmpGetOnEcm(tapEnv, device,
							BroadBandSnmpMib.ECM_DOCS_DEV_SW_SERVER_ADDRESS.getOid());
					LOGGER.info("SNMPGET RESPONSE FOR docsDevSwServerAddress: " + snmpGetResonse);
					isServerSetSuccessful = CommonMethods.patternMatcher(snmpGetResonse.toLowerCase(),
							softwareServerAddress.toLowerCase());
				}

			}
		} catch (Exception exception) {
			errorMessage = exception.getMessage();
			LOGGER.error(errorMessage);
		}
		LOGGER.debug("ENDING METHOD: setServerAddressForSnmpCodeDownload");
		return isServerSetSuccessful;
	}

	/**
	 * Method to verify that the device is in Maintenance window or not
	 * 
	 * @param tapApi instance of {@link AutomaticsTapApi}
	 * @param device instance of {@link Dut}
	 * @return true if in maintenance window else false
	 */
	public static boolean isDeviceInMaintenanceWindow(AutomaticsTapApi tapApi, Dut device) throws TestException {

		// variable to hold whether the device is in maintenance window or not
		boolean status = false;
		// server response
		String response = null;
		// pattern to match the maintenance window
		// If no property obtained ,The Maintenance window default value is 1 - 4.
		String pattern = AutomaticsPropertyUtility.getProperty(
				BroadBandTestConstants.PROP_KEY_MAINTENANCE_WINDOW_PATTERN, "0[1-3]:[0-5][0-9]:[0-5][0-9]");

		try {
			// retrieve local time from STB
			response = tapApi.executeCommandUsingSsh(device, "LTime");

			if (CommonMethods.isNotNull(response)) {

				// failure conditions
				if (response.contains(SSH_CONNECTION_ERROR_CONNECTION_REFUSED)) {
					throw new TestException("Unable to verify whether device is in maintenance window or not");
				}

				status = CommonMethods.patternMatcher(response, pattern);
			} else {
				throw new TestException("Unable to retrieve local time from given device");
			}
		} catch (TestException testException) {
			throw testException;
		} catch (Exception exception) {
			LOGGER.error("Exception  occured in isDeviceInMaintenanceWindow()");
		}

		LOGGER.info("RDKB Device is " + (status ? " in " : " not in ") + " maintenance window");

		return status;

	}

	/**
	 * Method to validate the XCONF Configuration retrieved from log file with
	 * actual details
	 * 
	 * @param xconfConfigurations      xconf configuration response as json format
	 * @param firmwareDownloadProtocol
	 * @param firmwareVersion
	 * @param rebootImmediately
	 * @return
	 */
	public static boolean validateXconfConfiguration(final String xconfConfigurations, String firmwareDownloadProtocol,
			String firmwareVersion, String rebootImmediately) throws TestException {

		LOGGER.debug("STARTING METHOD: validateXconfConfiguration()");

		// validation status
		boolean status = false;
		// validation error message
		StringBuffer errorMessage = new StringBuffer();
		// Text to compare
		String textToCompare = null;
		// Temporary variable to store XCONF configuration
		String configuration = xconfConfigurations;

		try {

			if (CommonMethods.isNotNull(configuration)) {
				// converting to lower case to avoid the case issues
				configuration = configuration.trim().toLowerCase();

				// validate firmwareDownloadProtocol
				textToCompare = "\"firmwareDownloadProtocol\":\"" + firmwareDownloadProtocol.trim() + "\"";
				if (!configuration.contains(textToCompare.toLowerCase())) {
					errorMessage.append(textToCompare);
				}

				// validate firmwareFilename
				textToCompare = "\"firmwareFilename\":\"" + firmwareVersion.trim()
						+ AutomaticsConstants.BINARY_BUILD_IMAGE_EXTENSION + "\"";

				if (!configuration.contains(textToCompare.toLowerCase())) {
					errorMessage.append(textToCompare);
				}

				// validate firmwareVersion

				// format image name
				firmwareVersion = BroadBandCommonUtils
						.removeDifferentSignedExtensionsInRequestedBuildName(firmwareVersion);

				textToCompare = "\"firmwareVersion\":\"" + firmwareVersion.trim() + "\"";

				if (!configuration.contains(textToCompare.toLowerCase())) {
					errorMessage.append(textToCompare);
				}

				// validate rebootImmediately
				textToCompare = "\"rebootImmediately\":\"" + rebootImmediately.trim() + "\"";

				if (!configuration.contains(textToCompare.toLowerCase())) {
					errorMessage.append(textToCompare);
				}

				if (CommonMethods.isNotNull(errorMessage.toString())) {

					throw new TestException("following XCONF Configuration validation failed for "
							+ errorMessage.toString() + " in " + xconfConfigurations);
				}
				status = true;
			}

		} catch (TestException testException) {
			throw testException;
		} catch (Exception exception) {
			LOGGER.error("Exception occured in validateXconfConfiguration()", exception);
		}

		LOGGER.debug("ENDING METHOD: validateXconfConfiguration()");

		return status;
	}

	/**
	 * Method to retrieve last reboot reason from RDKB device
	 * 
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @param device instance of {@link Dut}
	 * @return last reboot reason
	 */
	public static String getLastRebootReason(AutomaticsTapApi tapEnv, Dut device) {

		LOGGER.debug("STARTING METHOD: getLastRebootReason()");

		// retrieve last reboot reason
		String response = tapEnv.executeWebPaCommand(device,
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_LAST_REBOOT_REASON);

		if (CommonMethods.isNull(response)) {
			// added retry mechanism due to the webpa execution error
			response = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_LAST_REBOOT_REASON);
		}

		LOGGER.debug("ENDING METHOD: getLastRebootReason()");

		return response;
	}

	public static boolean verifyLastRebootReasonViaWebpa(AutomaticsTapApi tapEnv, Dut device, String rebootReason) {
		LOGGER.debug("STARTING METHOD: verifyLastRebootReasonViaWebpa()");
		// Variable declaration starts
		String errorMessage = "";
		boolean isRebootReasonVerified = false;
		String response = "";
		// Variable declaration ends

		try {
			// Retrieving last reboot reason
			response = getLastRebootReason(tapEnv, device);
			if (CommonMethods.isNotNull(response)) {
				isRebootReasonVerified = response.trim().contains(rebootReason);
			} else {
				LOGGER.error("Last reboot reason obtained as null.");
			}
		} catch (Exception exception) {
			errorMessage = "Exception occured while verifying Last Reboot Reason." + exception.getMessage();
			LOGGER.error(errorMessage);
		}
		LOGGER.debug("ENDING METHOD: verifyLastRebootReasonViaWebpa()");
		return isRebootReasonVerified;
	}

	/**
	 * Method to validate the current image both in ARM and ATOM Console
	 * 
	 * @param device    instance of {@link Dut}
	 * @param imageName image name
	 */
	public static void verifyCurrentImageNameBothInArmAndAtomConsole(AutomaticsTapApi tapEnv, Dut device,
			String imageName) {

		LOGGER.debug("STARTING METHOD: verifyCurrentImageNameBothInArmAndAtomConsole()");

		// validation details in arm console
		boolean validationStatusInArm = false;
		// validation details in atom console
		boolean validationStatusInAtom = false;
		// verify whether ATOM sync is available in given device
		boolean isAtomSyncAvailable = false;
		// maximum loop count
		// required ~10 minutes to update the image version in ATOM Console
		final int maxLoopCount = 10;
		// server response
		String response = null;
		// error message
		StringBuffer errorMessage = new StringBuffer();

		// verify whether the atom sync is available in given device
		isAtomSyncAvailable = CommonMethods.isAtomSyncAvailable(device, tapEnv);

		if (!isAtomSyncAvailable) {
			LOGGER.info("SKIPPING IMAGE NAME VERIFICATION IN ATOM CONSOLE SINCE ATOM SYNC IS UNAVAILABLE");
		}

		for (int index = 1; index <= maxLoopCount; index++) {

			LOGGER.info(index + "/" + maxLoopCount + "# Verifying image version from ARM and ATOM Console");

			// validation in ARM Console
			if (!validationStatusInArm) {
				response = tapEnv.executeCommandUsingSsh(device,
						BroadBandTestConstants.CMD_GREP_IMAGE_NAME_FROM_VERSION_FILE);
				if (CommonMethods.isNotNull(response)) {
					validationStatusInArm = response.toLowerCase().contains(imageName.toLowerCase());
				}
			}

			// validation in ATOM Console
			if (isAtomSyncAvailable && !validationStatusInAtom) {
				response = CommonMethods.executeCommandInAtomConsole(device, tapEnv,
						BroadBandTestConstants.CMD_GREP_IMAGE_NAME_FROM_VERSION_FILE);
				if (CommonMethods.isNotNull(response)) {
					validationStatusInAtom = response.toLowerCase().contains(imageName.toLowerCase());
				}
			} else {
				validationStatusInAtom = true;
			}

			// breaking if all the validation is success
			if (validationStatusInArm && validationStatusInAtom) {
				break;
			}
			LOGGER.info("Waiting one minute");
			tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
		}

		if (!validationStatusInArm) {
			errorMessage.append("Unable to find the current image version as " + imageName
					+ "in version.txt file(ARM Console)" + AutomaticsConstants.NEW_LINE);
		}

		if (!validationStatusInAtom) {
			errorMessage.append(
					"Unable to find the current image version as " + imageName + "in version.txt file(ATOM Console)");
		}

		// validation
		if (CommonMethods.isNotNull(errorMessage.toString().trim())) {
			throw new TestException(errorMessage.toString().trim());
		}

		LOGGER.debug("ENDING METHOD: verifyCurrentImageNameBothInArmAndAtomConsole()");
	}

	/**
	 * Utility Method to trigger HTTP CDL using TR-181 Parameters. CDL will be
	 * triggered using TR-181 parameter; once the CDL is completed, the device
	 * reboots; once the device the image flashing is verified and result is
	 * returned.
	 * 
	 * @param tapEnv                      {@link AutomaticsTapApi}
	 * @param device                      {@link Dut}
	 * @param cdlImageWithoutBinExtension String representing the CDL Image Name
	 *                                    without .bin extension
	 * 
	 * @return Boolean representing the result of the CDL Triggered.
	 */
	public static boolean triggerHttpCdlUsingTr181(AutomaticsTapApi tapEnv, Dut device,
			String cdlImageWithoutBinExtension) {
		LOGGER.info("ENTERING METHOD : triggerHttpCdlUsingTr181");
		boolean result = false;
		boolean cdlRequired = true;
		long startTime = 0L;
		startTime = System.currentTimeMillis();
		if (BroadBandXconfCdlUtils.toClearCdlInfoInXconf(device, tapEnv)) {
			LOGGER.info("Cleared cdl info in xconf");
		}
		String currentFirmwareVersion = FirmwareDownloadUtils.getCurrentFirmwareFileNameForCdl(tapEnv, device);
		LOGGER.info("CURRENT FIRMWARE VERSION: " + currentFirmwareVersion);
		if (currentFirmwareVersion.equalsIgnoreCase(cdlImageWithoutBinExtension)) {
			LOGGER.info("AS THE CURRENT BUILD & REQUIRED BUILD ARE SAME, SKIPPING THE PRE-CONDITION.");
			cdlRequired = false;
			result = true;
		}
		if (cdlRequired) {
			String codeDownloadUrl = AutomaticsTapApi.getSTBPropsValue(BroadBandCdlConstants.PROP_KEY_CDL_SERVER_URL);

			LOGGER.info("CODE DOWNLOAD URL: " + codeDownloadUrl);
			result = CommonMethods.isNotNull(codeDownloadUrl);
			String searchResponse = null;

			// Set the WebPA Parameter: Code Download URL & Code Download Image Name
			if (result && cdlRequired) {
				LOGGER.info("GOING TO SET CODE DOWNLOAD IMAGE URL (TR-181)");
				String cdlImageWithBinExtension = null;
				if (!DeviceModeHandler.isRPIDevice(device)) {
					cdlImageWithBinExtension = cdlImageWithoutBinExtension + BroadBandCdlConstants.BIN_EXTENSION;
				} else {
					cdlImageWithBinExtension = cdlImageWithoutBinExtension;
				}
				result = BroadBandWiFiUtils.setWebPaParams(device, BroadBandCdlConstants.WEBPA_PARAM_CODE_DOWNLOAD_URL,
						codeDownloadUrl, BroadBandTestConstants.CONSTANT_0);
				if (result) {
					LOGGER.info("GOING TO SET CODE DOWNLOAD IMAGE NAME (TR-181)");
					tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
					LOGGER.info("CDL image name :" + cdlImageWithBinExtension);
					result = BroadBandWiFiUtils.setWebPaParams(device,
							BroadBandCdlConstants.WEBPA_PARAM_CODE_DOWNLOAD_IMAGE_NAME, cdlImageWithBinExtension,
							BroadBandTestConstants.CONSTANT_0);
				}
				if (result) {
					startTime = System.currentTimeMillis();
					do {
						LOGGER.info("GOING TO WAIT FOR 10 SECONDS.");
						tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
						result = CommonMethods
								.isNotNull(BroadBandCommonUtils.searchLogFiles(tapEnv, device,
										BroadBandCdlConstants.LOG_MESSAGE_CDL_URL + codeDownloadUrl,
										/*
										 * BroadBandCommonUtils.getLogFileNameOnFirmwareDownload(device)
										 */DeviceModeHandler.isRPIDevice(device)
												? BroadBandCdlConstants.FWUPGRADEMANAGER_LOG_FILE_NAME
												: BroadBandCdlConstants.CM_LOG_TXT_0))
								&& CommonMethods.isNotNull(BroadBandCommonUtils.searchLogFiles(tapEnv, device,
										BroadBandCdlConstants.LOG_MESSAGE_CDL_IMAGE_NAME + cdlImageWithBinExtension,
										/*
										 * BroadBandCommonUtils.getLogFileNameOnFirmwareDownload(device)
										 */DeviceModeHandler.isRPIDevice(device)
												? BroadBandCdlConstants.FWUPGRADEMANAGER_LOG_FILE_NAME
												: BroadBandCdlConstants.CM_LOG_TXT_0));
					} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIFTEEN_MINUTES_IN_MILLIS
							&& !result);
				}
				LOGGER.info("CODE DOWNLOAD URL & IMAGE NAME SET SUCCESSFULLY (TR-181): " + result);
			}
			// Trigger the CDL
			if (result && cdlRequired) {
				LOGGER.info("GOING TO TRIGGER CDL (TR-181)");
				result = BroadBandWiFiUtils.setWebPaParams(device, BroadBandCdlConstants.WEBPA_PARAM_TRIGGER_CDL,
						BroadBandTestConstants.TRUE, BroadBandTestConstants.CONSTANT_3);
				if (result) {
					startTime = System.currentTimeMillis();
					do {
						LOGGER.info("GOING TO WAIT FOR 10 SECONDS.");
						tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
						searchResponse = BroadBandCommonUtils.searchLogFiles(tapEnv, device,
								BroadBandCdlConstants.LOG_MESSAGE_CDL_TRIGGERED,
								/*
								 * BroadBandCommonUtils. getLogFileNameOnFirmwareDownload( device)
								 */DeviceModeHandler.isRPIDevice(device)
										? BroadBandCdlConstants.FWUPGRADEMANAGER_LOG_FILE_NAME
										: BroadBandCdlConstants.CM_LOG_TXT_0);
						result = CommonMethods.isNotNull(searchResponse);
						if (CommonUtils.verifyStbRebooted(device, tapEnv)) {
							result = true;
							break;
						}
					} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.EIGHT_MINUTE_IN_MILLIS
							&& !result);
				}
				LOGGER.info("CODE DOWNLOAD TRIGGERED SUCCESSFULLY (TR-181): " + result);
			}
			if (result) {
				result = CommonMethods.isSTBRebooted(tapEnv, device, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS,
						BroadBandTestConstants.CONSTANT_20);
				LOGGER.info("DEVICE REBOOTS AFTER (TR-181) CDL: " + result);
			}
			if (result) {
				result = CommonMethods.waitForEstbIpAcquisition(tapEnv, device);

			} else {
				result = false;
			}
			LOGGER.info("DEVICE COMES UP AFTER (TR-181) CDL & REBOOT: " + result);

			// Verify the flashed image name after CDL.
			if (result && cdlRequired) {
				startTime = System.currentTimeMillis();
				do {
					LOGGER.info("GOING TO WAIT FOR 1 MINUTE.");
					tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
					String tempFirmwareVersion = FirmwareDownloadUtils.getCurrentFirmwareFileNameForCdl(tapEnv, device);
					LOGGER.info("cdl image to be downloaded :" + cdlImageWithoutBinExtension);
					LOGGER.info("cdl image downloaded :" + tempFirmwareVersion);
					result = CommonMethods.isNotNull(tempFirmwareVersion) && DeviceModeHandler.isRPIDevice(device)
							? cdlImageWithoutBinExtension.trim().contains(tempFirmwareVersion.trim())
							: cdlImageWithoutBinExtension.trim().equalsIgnoreCase(tempFirmwareVersion.trim());
				} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.THREE_MINUTES && !result);
				LOGGER.info("CDL IMAGE FLASHED SUCCESSFULLY: " + result);
			}
			LOGGER.info("HTTP CDL TRIGGERED & COMPLETED SUCCESSFULLY: " + result);
		}
		LOGGER.info("ENDING METHOD : triggerHttpCdlUsingTr181");
		return result;
	}

	/**
	 * Utility method to trigger the TFTP code download via DOCSIS SNMP commands and
	 * wait for code download completes and comes up with Requested Image.
	 * 
	 * @param tapEnv                    The {@link AutomaticsTapApi} reference.
	 * @param device                    The Dut to be flashed.
	 * @param buildName                 The requested firmware version.
	 * @param useImageFrmSystemProperty Boolean representing the build name to be
	 *                                  referred from System Property or the
	 *                                  argument passed.
	 * 
	 * @return True if code download succeeds, otherwise false.
	 * @refacor Govardhan
	 */
	public static boolean triggerAndWaitForTftpCodeDownloadUsingDocsisSnmpCommand(AutomaticsTapApi tapEnv, Dut device,
			String buildName, boolean useImageFrmSystemProperty) {

		boolean snmpDownloadCompletedStatus = false;

		/** Added new boolean argument to refer the build name from System Property */
		triggerTftpCodeDownloadUsingSnmp(device, tapEnv, buildName, useImageFrmSystemProperty);

		tapEnv.waitTill(BroadBandTestConstants.THREE_MINUTES);

		for (int index = 0; index < MAX_WAIT_TIME_SNMP_TFTP_CODE_DOWNLOAD; index++) {

			String downloadStatus = BroadBandSnmpUtils.snmpGetOnEcm(tapEnv, device,
					BroadBandSnmpMib.ECM_DOCS_DEV_SW_OPER_STATUS.getOid());

			LOGGER.info("Download Status is : " + downloadStatus);
			if (downloadStatus.equalsIgnoreCase(SNMP_TFTP_DOWNLOAD_STATUS_FAILED)
					|| downloadStatus.equalsIgnoreCase(SNMP_TFTP_DOWNLOAD_STATUS_OTHER)) {
				LOGGER.error("DOCSIS TFTP SNMP CODE DOWNLOAD FAILED, NO OTHER PROCESSING.");
				snmpDownloadCompletedStatus = false;
				break;
			} else if (downloadStatus.equalsIgnoreCase(SNMP_TFTP_DOWNLOAD_STATUS_IN_PROGRESS)) {
				LOGGER.info("DOCSIS TFTP SNMP CODE DOWNLOAD IS IN PROGRESS, WAIT FOR FIVE MINUTES FOR NEXT CHECK");
				tapEnv.waitTill(BroadBandTestConstants.TWO_MINUTE_IN_MILLIS);
			} else if (downloadStatus.equalsIgnoreCase(SNMP_TFTP_DOWNLOAD_STATUS_COMPLETE_FROM_PROVISIONING)
					|| downloadStatus.equalsIgnoreCase(SNMP_TFTP_DOWNLOAD_STATUS_COMPLETED_FROM_MGNT)) {
				LOGGER.info(
						"DOCSIS TFTP SNMP CODE DOWNLOAD IS COMPLETED SUCCESSFULLY, WAIT FOR DEVICE TO REBOOT WITH REQUESTED IMAGE");
				snmpDownloadCompletedStatus = true;
				break;
			} else {
				/*
				 * In case of reboot, snmp output will be having error message. So considering
				 * this as download completed.
				 */
				snmpDownloadCompletedStatus = true;
				LOGGER.info(
						"DOCSIS TFTP SNMP CODE DOWNLOAD IS COMPLETED SUCCESSFULLY, WAIT FOR DEVICE TO REBOOT WITH REQUESTED IMAGE");
				break;
			}
		}
		if (snmpDownloadCompletedStatus) {
			/*
			 * if download completed, then wait for device to reboot and comes up with
			 * requested image.
			 */
			snmpDownloadCompletedStatus = CommonMethods.waitForEstbIpAcquisition(tapEnv, device);
		}

		snmpDownloadCompletedStatus = CodeDownloadUtils.verifyImageVersionFromVersionText(tapEnv, device,
				BroadBandCommonUtils.removeDifferentSignedExtensionsInRequestedBuildName(buildName));

		return snmpDownloadCompletedStatus;
	}

	/**
	 * Utility method to trigger the TFTP code download using DOCS-CABLE-DEVICE-MIB
	 * SNMP Commands.
	 * 
	 * @param device                    The device to be validated.
	 * @param tapEnv                    The {@link AutomaticsTapApi} reference.
	 * @param requestedBuildName        The requested build name.
	 * @param useImageFrmSystemProperty Boolean representing the build name to be
	 *                                  referred from System Property or the
	 *                                  argument passed.
	 * @refacor Govardhan
	 */
	public static void triggerTftpCodeDownloadUsingSnmp(Dut device, AutomaticsTapApi tapEnv, String requestedBuildName,
			boolean useImageFrmSystemProperty) {

		String protocol = BroadBandSnmpUtils.snmpSetOnEcm(tapEnv, device,
				BroadBandSnmpMib.ECM_SERVER_TRANSPORT_PROTOCOL.getOid(), SnmpDataType.INTEGER, "2");
		LOGGER.info("ECM Server Transport Protocol is : " + protocol);
		if (CommonMethods.isNotNull(protocol) && !protocol.equalsIgnoreCase("2")) {
			throw new TestException(
					"Unable to set HTTP as download protocol using SNMP MIB DOCS-CABLE-DEVICE-MIB::docsDevSwServerTransportProtocol. Expected value = 2; Actual value = "
							+ protocol);
		}

		String addressType = BroadBandSnmpUtils.snmpSetOnEcm(tapEnv, device,
				BroadBandSnmpMib.ECM_SERVER_ADDRESS_TYPE_CDL.getOid(), SnmpDataType.INTEGER, "2");
		LOGGER.info("ECM Server Address type is : " + addressType);
		if (CommonMethods.isNotNull(addressType) && !addressType.equalsIgnoreCase("2")) {
			throw new TestException(
					"Unable to set Address type for software upgrade using SNMP MIB DOCS-CABLE-DEVICE-MIB::docsDevSwServerAddressType. Expected value = 2; Actual value = "
							+ addressType);
		}

		String tftpServerAddress = AutomaticsPropertyUtility
				.getProperty(BroadBandTestConstants.PROP_KEY_HTTP_SERVER_IPV6_ADDRESS);
		LOGGER.info("Tftp ServerAddress is : " + tftpServerAddress);
		if (CommonMethods.isNull(tftpServerAddress)) {
			throw new TestException(
					"Unable to retrieve the TFTP IPV6 CDL server Address from 'http.server.ipv6.address' defined in stb.properties");
		}

		String softwareServerAddress = getFormattedTftpServerIpv6Address(tftpServerAddress);
		LOGGER.info("Software ServerAddress is : " + softwareServerAddress);

		String formattedAddress = new StringBuffer().append(BroadBandTestConstants.DOUBLE_QUOTE)
				.append(softwareServerAddress.toUpperCase()).append(BroadBandTestConstants.DOUBLE_QUOTE).toString();

		LOGGER.info("FORMATTED TFTP SERVER IPV6 ADDRESS IN HEX FORMAT : " + formattedAddress);

		String serverAddress = BroadBandSnmpUtils.snmpSetOnEcm(tapEnv, device,
				BroadBandSnmpMib.ECM_DOCS_DEV_SW_SERVER_ADDRESS_PROTOCOL.getOid(), SnmpDataType.HEXADECIMAL,
				formattedAddress);
		LOGGER.info("ServerAddress is : " + serverAddress);
		if (CommonMethods.isNotNull(serverAddress) && !serverAddress.trim().equalsIgnoreCase(softwareServerAddress)) {
			throw new TestException(
					"Unable to set software server Address using SNMP MIB DOCS-CABLE-DEVICE-MIB::docsDevSwServerAddress. Expected value = "
							+ softwareServerAddress + " , Actual value = " + serverAddress.trim());
		}

		/**
		 * Commented to extend the method to accommodate the CDL using the Build Name
		 * passed as argument
		 */
		requestedBuildName = useImageFrmSystemProperty
				? System.getProperty(AutomaticsConstants.BUILD_NAME_SYSTEM_PROPERTY)
				: requestedBuildName;
		LOGGER.info(
				"BUILD IMAGE IS FROM PROPERTY: " + useImageFrmSystemProperty + ", BUILD IMAGE: " + requestedBuildName);

		if (CommonMethods.isNotNull(requestedBuildName)
				&& !requestedBuildName.contains(BroadBandTestConstants.BINARY_BUILD_IMAGE_EXTENSION)) {
			requestedBuildName = requestedBuildName + BroadBandTestConstants.BINARY_BUILD_IMAGE_EXTENSION;
		}

		String softwareFileName = BroadBandSnmpUtils.snmpSetOnEcm(tapEnv, device,
				BroadBandSnmpMib.ECM_DOCS_DEV_SW_FILE_NAME.getOid(), SnmpDataType.STRING, requestedBuildName);
		LOGGER.info("Software File Name is : " + softwareFileName);
		if (!softwareFileName.equalsIgnoreCase(requestedBuildName)) {
			throw new TestException(
					"Unable to set softeare file name using SNMP MIB DOCS-CABLE-DEVICE-MIB::docsDevSwFilename. Expected value = "
							+ requestedBuildName + " , Actual value = " + softwareFileName);
		}
		boolean isFactoryResetCombined = Boolean.parseBoolean(
				System.getProperty(BroadBandTestConstants.SYSTEM_PROPERTY_ENABLE_CDL_FACTORY_RESET, "false"));

		String mibOrOidName = isFactoryResetCombined ? BroadBandSnmpMib.ECM_FWUPGRADE_AND_FACTORYRESET.getOid()
				: BroadBandSnmpMib.ECM_DOCS_DEV_START_CODE_DOWNLOAD.getOid();
		LOGGER.info("Mib Or Oid Name is : " + mibOrOidName);

		String adminStatus = BroadBandSnmpUtils.snmpSetOnEcm(tapEnv, device, mibOrOidName, SnmpDataType.INTEGER, "1");
		LOGGER.info("Admin Status is : " + adminStatus);
		if (!adminStatus.equalsIgnoreCase("1")) {
			throw new TestException(
					"Unable to set admin status using SNMP MIB DOCS-CABLE-DEVICE-MIB::docsDevSwAdminStatus. Expected value = 1, Actual value = "
							+ adminStatus);
		}
	}

	/**
	 * Utility method which delete the swupdate.conf in nvram directory to avoid
	 * further downloads.
	 * 
	 * @param tapEnv The Automaticstap api instance
	 * @param device The Dut instance
	 */
	public static void deleteSoftwareUpdateConfigurationFile(AutomaticsTapApi tapEnv, Dut device) {
		/** Create Empty /nvram/swupdate.conf */
		String CMD_ADD_EMPTY_SOFTWARE_UPDATE_CONF_FILE = " echo \" \" > /nvram/swupdate.conf";
		String consoleoutput = tapEnv.executeCommandUsingSsh(device, CMD_ADD_EMPTY_SOFTWARE_UPDATE_CONF_FILE);
		LOGGER.info("deleteSoftwareUpdateConfigurationFile:  " + consoleoutput);
	}

	/**
	 * Clear Cdl Info in Xconf
	 * 
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @param device instance of {@link device}
	 */
	public static boolean toClearCdlInfoInXconf(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD : ToClearCdlInfoInXconf()");
		boolean status = false;
		String errorMessage = null;
		errorMessage = "Failed to Clear the code download URL information in /nvram/swupdate.conf";
		try {
			FirmwareDownloadUtils.deleteSoftwareUpdateConfigurationFile(tapEnv, device);
			if (CommonMethods.isFileExists(device, tapEnv,
					BroadBandCommandConstants.FILE_PATH_TMP_XCONF_URL_OVERRIDE)) {
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
	 * Method to clear the xconf url configuration in /nvram/XconfUrlOverride
	 * 
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @param device instance of {@link Dut}
	 */
	public static void deleteXconfUlrOverrideConfigurationFile(AutomaticsTapApi tapEnv, Dut device) {
		/** Create Empty /nvram/XconfUrlOverride */
		String consoleoutput = tapEnv.executeCommandUsingSsh(device,
				"echo \" \" > " + BroadBandCommandConstants.FILE_PATH_TMP_XCONF_URL_OVERRIDE);
		LOGGER.info("Create empty " + BroadBandCommandConstants.FILE_PATH_TMP_XCONF_URL_OVERRIDE
				+ " Configuration File:  " + consoleoutput);
	}

	/**
	 * Utility method to validate whether http code download request is accepted
	 * using log messages.
	 * 
	 * @param commandoutput The command output.
	 * @return True if required logs are available in command response.
	 */
	public static boolean verifyWhetherDownloadRequestAcceptedUsingLogMessage(String response) {

		return CommonUtils.isGivenStringAvailableInCommandOutput(response,
				BroadBandTraceConstants.LOG_MESSAGE_HTTP_DOWNLOAD_REQUEST_ACCEPTED);
	}

	/**
	 * Utility method to verify whether code download request rejected with same
	 * version using log message.
	 * 
	 * @param response The command output.
	 * @return True if required logs are available in command response.
	 * @refactor Govardhan
	 */
	public static boolean verifyCdlRequestRejectedWithSameVersionUsingLogMessage(String response) {

		return CommonUtils.isGivenStringAvailableInCommandOutput(response,
				BroadBandTraceConstants.LOG_MESSAGE_HTTP_DOWNLOAD_REQUEST_REJECTED_WITH_SAME_VERSION);
	}

	/**
	 * Utility method to trigger the XCONF download via WebPA or Reboot and verify
	 * whether code download is started from xconf log message.
	 * 
	 * 
	 * @param tapEnv         {@link AutomaticsTapApi}
	 * @param device         {@link Dut}
	 * @param buildImageName The firmware version to be flashed or triggered.
	 * @param isWebPaTrigger True if XCONF trigger is via WebPA, otherwise it will
	 *                       reboot the device for triggering the XCONF.
	 * @return True if XCONF trigger request got accepted. This is based on xconf
	 *         log messages.
	 * @refactor Govardhan
	 */
	public static boolean triggerXconfDownloadAndVerifyDownloadStarted(AutomaticsTapApi tapEnv, Dut device,
			String buildImageName, boolean isWebPaTrigger) {

		boolean requestAccepted = false;
		boolean requestRejectedForSameVersion = false;
		boolean isCdlTriggered = false;

		// Configure RDKB device for XCONF Code download
		BroadBandXconfCdlUtils.configureRdkbDeviceForXconfCdl(tapEnv, device, buildImageName, false,
				BroadBandTestConstants.FIRMWARE_DOWNLOAD_PROTOCOL_HTTP, BroadBandTestConstants.CONSTANT_0);

		if (isWebPaTrigger) {
			// Sometimes we are getting previous cdl logs which causing failure.
			// To avoid that failures, we are making xconf.txt.0 file as empty before
			// triggering XCONF via WebPA.

			isCdlTriggered = BroadBandXconfCdlUtils.initiateXconfCdlThroughWebpa(tapEnv, device);
			// Wait for 30 seconds before checking the status.
			tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		} else {
			isCdlTriggered = CommonUtils.rebootAndWaitForIpAcquisition(tapEnv, device);
		}
		if (isCdlTriggered) {
			/*
			 * Some of devices taking more time to initiate the Device initiated firmware
			 * download.
			 */
			for (int cdlCheckCount = 0; cdlCheckCount < 10; cdlCheckCount++) {

				String response = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.CAT_XCONF_TXT)
						.toLowerCase();

				requestAccepted = FirmwareDownloadUtils.verifyWhetherDownloadRequestAcceptedUsingLogMessage(response);
				if (!requestAccepted) {
					requestRejectedForSameVersion = FirmwareDownloadUtils
							.verifyCdlRequestRejectedWithSameVersionUsingLogMessage(response);
				}
				if (!requestAccepted && !requestRejectedForSameVersion) {

					boolean isWaitingRebootForPreviousDownload = CommonUtils.isGivenStringAvailableInCommandOutput(
							response, BroadBandTestConstants.DOWNLOAD_STATUS_WAITING_FOR_REBOOT_PREVIOUS_DOWNLOAD);
					boolean isPreviousDownloadInProgress = CommonUtils.isGivenStringAvailableInCommandOutput(response,
							BroadBandTestConstants.DOWNLOAD_IN_PROGRESS_FOR_PREVIOUS_TRIGGER);

					if (isWaitingRebootForPreviousDownload || isPreviousDownloadInProgress) {
						// If Device waiting for previous download to complete, trigger the reboot and
						// wait again for completion.
						CommonUtils.rebootAndWaitForIpAcquisition(tapEnv, device);
					}
				}

				if (requestRejectedForSameVersion) {
					requestAccepted = false;
					break;
				} else if (requestAccepted) {
					break;
				}

				tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
			}
		}

		// Verifying CDL status
		return requestAccepted;
	}

	/**
	 * Utility method to validate whether http code download is completed using
	 * different log messages.
	 * 
	 * @param commandoutput The command output.
	 * @return True if required logs are available in command response.
	 * @refactor Govardhan
	 */
	public static boolean verifyWhetherDownloadCompletedUsingLogMessage(String commandoutput) {

		boolean downloadCompleted = CommonUtils.isGivenStringAvailableInCommandOutput(commandoutput,
				BroadBandTraceConstants.HTTP_DOWNLOAD_COMPLETED_LOG);
		if (!downloadCompleted) {
			downloadCompleted = CommonUtils.isGivenStringAvailableInCommandOutput(commandoutput,
					BroadBandTraceConstants.LOG_MESSAGE_HTTP_DOWNLOAD_COMPLETED);
		}
		return downloadCompleted;
	}

	/**
	 * Utility method to verify whether code download is successfully completed
	 * using log message.
	 * 
	 * @param response The command output.
	 * @return True if required logs are available in command response.
	 * @refactor Govardhan
	 */
	public static boolean verifyWhetherCodeDownlaodCompletedSuccessfullyUsingLogMessage(String response) {

		return CommonUtils.isGivenStringAvailableInCommandOutput(response,
				BroadBandTraceConstants.HTTP_DOWNLOAD_SUCCESS_LOG);
	}

	/**
	 * Utility method to verify whether code download completed and waiting for
	 * reboot window using log message.
	 * 
	 * @param response
	 * @return True if required logs are available in command response.
	 * @refactor Govardhan
	 */
	public static boolean verifyWhetherCdlCompletedAndWaitingForMaintenanceWindowUsingLogMessage(String response) {

		return CommonUtils.isGivenStringAvailableInCommandOutput(response,
				BroadBandTraceConstants.MAINTENANCE_WINDOW_LOG);
	}

	/**
	 * Utility method to verify whether code download completed during reboot window
	 * using log message.
	 * 
	 * @param response
	 * @return True if required logs are available in command response.
	 * @refactor Govardhan
	 */
	public static boolean verifyWhetherCdlCompletedDuringMaintenanceWindowUsingLogMessage(String response) {

		return CommonUtils.isGivenStringAvailableInCommandOutput(response,
				BroadBandTraceConstants.LOG_XCONF_DURING_MAINTENANCE_WINDOW);
	}

	/**
	 * Utility method to verify whether code download is failed using log message.
	 * 
	 * @param response The command output.
	 * @return True if required logs are available in command response.
	 * @refactor Govardhan
	 */
	public static boolean verifyWhetherCdlUpgradeFailedUsingLogMessage(String response) {

		return CommonUtils.isGivenStringAvailableInCommandOutput(response,
				BroadBandTraceConstants.LOG_MESSAGE_HTTP_DOWNLOAD_NOT_SUCCESSFUL);
	}

	/**
	 * Helper method to validate the download progress using logs. It takes care of
	 * device reboot during maintenance window and if download takes more than
	 * expected time. This will reduce the false alarm for download failure.
	 * 
	 * @param tapEnv                {@link AutomaticsTapApi}
	 * @param device                {@link Dut}
	 * @param downloadStatusCommand The command to get download status log.
	 * @return True if download succeeded, otherwise it returns false.
	 * @refactor Govardhan
	 */
	public static boolean waitForXconfDownloadCompleteAndVerifyDownloadProgressUsingLogs(AutomaticsTapApi tapEnv,
			Dut device, String buildImageName) {

		boolean status = false;
		boolean rebootOccuredWaitAndCheckVersion = false;
		boolean actualCdlUpgradeFailure = false;

		/*
		 * Different models having different download time, so query the code download
		 * file in TWO minutes interval and check whether required logs are there and
		 * exit the loop if present. This will avoid hard wait and failing the test
		 * after hard wait. This increase the efficiency and reduce the false failure.
		 */
		tapEnv.waitTill(RDKBTestConstants.ONE_MINUTE_IN_MILLIS);
		for (int count = 1; count <= 20; count++) {

			try {
				if (!status) {

					String response = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.CAT_XCONF_TXT)
							.toLowerCase();
					if (CommonMethods.isNotNull(response)) {
						boolean downloadCompleted = verifyWhetherDownloadCompletedUsingLogMessage(response);
						boolean downloadSuccess = verifyWhetherCodeDownlaodCompletedSuccessfullyUsingLogMessage(
								response);
						boolean maintenanceWindow = verifyWhetherCdlCompletedAndWaitingForMaintenanceWindowUsingLogMessage(
								response);
						boolean durinMaintenanceWindow = verifyWhetherCdlCompletedDuringMaintenanceWindowUsingLogMessage(
								response);
						status = downloadCompleted && downloadSuccess && (maintenanceWindow || durinMaintenanceWindow);

						if (!status) {

							if (verifyWhetherCdlUpgradeFailedUsingLogMessage(response)) {
								LOGGER.error("Device Upgrade failed, No need to loop for checking the download status");
								actualCdlUpgradeFailure = true;
								break;
							} else if (verifyCdlRequestRejectedWithSameVersionUsingLogMessage(response)) {
								LOGGER.error(
										"Seems like Code download requested rejected due to same image version, check the version.");
								status = CodeDownloadUtils.verifyImageVersionFromVersionText(tapEnv, device,
										buildImageName);
								break;
							} else if (durinMaintenanceWindow) {
								rebootOccuredWaitAndCheckVersion = CommonUtils.verifyStbRebooted(device, tapEnv);
							} else if (!CommonMethods.isSTBAccessible(device)) {
								LOGGER.info(
										"Seems like device got rebooted during maintainance window, please wait for 4 minutes and check the status");
								// Instead if simply assigned rebooted value as true, check whether device is
								// actually
								// rebooted or not
								// Sometimes, cdl will be in inprogress state. In that case we need to
								// waitForIpAccusitionAfterReboot
								// rebootOccuredWaitAndCheckVersion = true;
								rebootOccuredWaitAndCheckVersion = CommonUtils.verifyStbRebooted(device, tapEnv);

							}
						} else {
							break;
						}
					}
				} else {
					break;
				}

			} catch (Exception ex) {
				LOGGER.error(
						"Exception caught during download progress check. Seems like device is rebooted unexpectedly");
				rebootOccuredWaitAndCheckVersion = CommonUtils.verifyStbRebooted(device, tapEnv);
			}

			/*
			 * Even after 10 minutes device is not showing download status logs, check
			 * whether device rebooted during reboot window and comes up with latest image.
			 * If current running image name and requested image names are same. Please
			 * update the status to true. This logic will be changed once configurable
			 * maintenance window feature is implemented in RDKB devices.
			 */
			if (rebootOccuredWaitAndCheckVersion) {
				LOGGER.info(
						"Device is in reboot state. So wait for IP acquisition and check firmware version after boot up ");
				boolean deviceRebooted = CommonMethods.waitForEstbIpAcquisition(tapEnv, device);
				if (deviceRebooted) {
					status = CodeDownloadUtils.verifyImageVersionFromVersionText(tapEnv, device, buildImageName);
				} else {
					LOGGER.error("Seems like Device not SSHable after device reboot occured during maintenace window.");
					break;
				}
			}

			if (status) {
				break;
			}
			tapEnv.waitTill(RDKBTestConstants.ONE_MINUTE_IN_MILLIS);
		}

		if (!status && !actualCdlUpgradeFailure) {
			/*
			 * If status is false even after 10 iteration and there is no failure, try with
			 * reboot and then check version name. There are situations where some part of
			 * logs missing which causing the failure. Since QT is primary entry point for
			 * triggering other test cases, so rebooting the device and checking whether
			 * device upgraded with requested image.
			 * 
			 * 
			 */

			BroadBandWebPaUtils.setParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_CONTROL_DEVICE_REBOOT, BroadBandTestConstants.CONSTANT_0,
					BroadBandTestConstants.DEVICE);
			tapEnv.waitTill(BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS);
			boolean deviceRebooted = CommonMethods.waitForEstbIpAcquisition(tapEnv, device);
			LOGGER.info("Device Rebooted after : " + deviceRebooted);

			if (deviceRebooted) {
				status = CodeDownloadUtils.verifyImageVersionFromVersionText(tapEnv, device, buildImageName);
			}
		} else {
			tapEnv.waitTill(BroadBandTestConstants.TWO_MINUTES);
		}
		return status;
	}

	/**
	 * Method to retrieve and validate the XCONF configuration details from RDK Logs
	 * 
	 * @param rdkLogContents    logs
	 * @param imageName         image name
	 * @param rebootImmediately reboot immediately status
	 * @param downloadProtocol  CDL Download protocol
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
	 * Utility Method to upgrade image using Mock Xconf.
	 * 
	 * @param tapEnv    {@link AutomaticsTapApi}
	 * @param device    {@link Dut}
	 * @param imageName String representing the CDL Image Name without .bin
	 *                  extension
	 * 
	 * @return Boolean representing the result of the image upgrade
	 * 
	 * @author Ashwin sankara
	 * @refactor yamini.s
	 */
	public static boolean performXconfHttpImageUpgrade(AutomaticsTapApi tapEnv, Dut device, String imageName) {
		LOGGER.info("ENTERING METHOD : performXconfHttpImageUpgrade");
		boolean result = false;

		try {
			String currentFirmwareVersion = FirmwareDownloadUtils.getCurrentFirmwareFileNameForCdl(tapEnv, device);
			LOGGER.info("CURRENT FIRMWARE VERSION: " + currentFirmwareVersion);
			if (currentFirmwareVersion.equalsIgnoreCase(imageName)) {
				LOGGER.info("AS THE CURRENT BUILD & REQUIRED BUILD ARE SAME, CANNOT PERFORM IMAGE UPGRADE");
				return false;
			}

			BroadBandXconfCdlUtils.updateSoftwareUpdateConfigurationOnClient(tapEnv, device);

			XConfUtils.configureXconfDownloadFirmwareDetails(device, imageName, true,
					BroadBandTestConstants.STRING_PROTOCOL_HTTP);

			CommonUtils.clearLogFile(tapEnv, device, BroadBandTestConstants.RDKLOGS_LOGS_XCONF_TXT_0);

			if (BroadBandWebPaUtils.setParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_FOR_TRIGGERING_XCONF_CDL, BroadBandTestConstants.CONSTANT_3,
					BroadBandTestConstants.TRUE)) {

				if (CommonMethods.isNotNull(BroadBandCommonUtils.searchLogFiles(tapEnv, device,
						BroadBandTraceConstants.LOG_MESSAGE_HTTP_DOWNLOAD_REQUEST_ACCEPTED,
						BroadBandTestConstants.RDKLOGS_LOGS_XCONF_TXT_0, BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS,
						BroadBandTestConstants.ONE_MINUTE_IN_MILLIS))) {

					if (CommonMethods.isSTBRebooted(tapEnv, device, BroadBandTestConstants.ONE_MINUTE_IN_MILLIS,
							BroadBandTestConstants.CONSTANT_10)) {

						if (CommonMethods.waitForEstbIpAcquisition(tapEnv, device)) {

							result = CodeDownloadUtils.verifyImageVersionFromVersionText(tapEnv, device, imageName);

						} else {
							LOGGER.info("Device did not come up after going for reboot during CDL");
						}
					} else {
						LOGGER.error("Device did not go for reboot after triggering xconf cdl");
					}
				} else {
					LOGGER.error("Failed to find log message in xconf.txt: "
							+ BroadBandTraceConstants.LOG_MESSAGE_HTTP_DOWNLOAD_REQUEST_ACCEPTED);
				}
			} else {
				LOGGER.error("Failed to trigger Xconf check-in using parameter: "
						+ BroadBandWebPaConstants.WEBPA_PARAM_FOR_TRIGGERING_XCONF_CDL);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured during xconf imamge upgrade: " + e.getMessage());
		}

		LOGGER.info("HTTP CDL TRIGGERED & COMPLETED SUCCESSFULLY: " + result);
		LOGGER.info("ENDING METHOD : performXconfHttpImageUpgrade");
		return result;
	}

	/**
	 * Method to trigger code download using triggerFirmwareDownload.sh script file
	 * 
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @param device instance of {@link Dut}
	 * @throws TestException if validation is failed
	 * @refactor Govardhan
	 */
	public static void triggerCdlUsingShellScriptForDSL(AutomaticsTapApi tapEnv, Dut device) throws TestException {
		LOGGER.debug("STARTING METHOD: triggerCdlUsingShellScriptForDSL()");
		// execution status
		boolean status = false;
		// server response
		String response = null;
		// error message
		String errorMessage = null;
		try {
			// copying the script to STB in case the script is not already present
			if (!CommonUtils.isFileExists(device, tapEnv,
					BroadBandTestConstants.TRIGGER_FIRMWARE_DOWNLOAD_SCRIPT_PATH_DSL)) {
				LOGGER.info("device Path=" + BroadBandTestConstants.TRIGGER_FIRMWARE_DOWNLOAD_SCRIPT_PATH_DSL);
				CommonUtils.downloadFileUsingAutoVault(device, tapEnv, BroadbandPropertyFileHandler.getInvalidFile(),
						BroadBandTestConstants.NVRAM_PATH);
				// verifying whether the file has been copied successfully
				status = CommonUtils.isFileExists(device, tapEnv,
						BroadBandTestConstants.TRIGGER_FIRMWARE_DOWNLOAD_SCRIPT_PATH_DSL);
			} else {
				status = true;
			}
			LOGGER.info("DSL device status of file exists="
					+ BroadBandTestConstants.TRIGGER_FIRMWARE_DOWNLOAD_SCRIPT_PATH_DSL + " status=" + status);
			if (status) {
				// executing the script to start firmware upgrade
				response = tapEnv.executeCommandUsingSsh(device, AutomaticsTapApi
						.getSTBPropsValue(BroadBandWebGuiTestConstant.SH_COMMAND_TO_TRIGGER_FIRMWARE_DOWNLOAD_DSL));

				if (CommonMethods.isNotNull(response)) {
					status = response
							.contains(BroadBandCdlConstants.SUCCES_MESSAGE_FOR_XCONF_CDL_TRIGGERED_SUCCESSFULLY);
					errorMessage = "Unable to trigger XCONF CDL using "
							+ BroadBandTestConstants.TRIGGER_FIRMWARE_DOWNLOAD_SCRIPT_PATH_DSL;
				} else {
					errorMessage = "Unable to trigger XCONF CDL using "
							+ BroadBandTestConstants.TRIGGER_FIRMWARE_DOWNLOAD_SCRIPT_PATH_DSL;
				}
			} else {
				errorMessage = "Unable to copy " + BroadBandTestConstants.TRIGGER_FIRMWARE_DOWNLOAD_SCRIPT_PATH_DSL
						+ " to STB";
			}

		} catch (Exception e) {
			status = false;
			LOGGER.error("Exception occured during trigger shell script for DSL: " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: triggerCdlUsingShellScriptForDSL()");
	}

	/**
	 * Method to return cdl download completed log
	 * 
	 * @param device instance of {@link Dut}
	 * @return cdl Download Completed Log
	 */
	public static String getCdlDownloadCompletedLog(Dut device) {
		// Log for firmware download completed
		String cdlDownloadCompletedLog = "### httpdownload completed ###";
		return cdlDownloadCompletedLog;
	}

	/**
	 * Method to trigger Cdl by http protocol using Tr181 param and Tftp protocol,
	 * in case the http protocol fails.
	 * 
	 * 
	 * @param tapEnv                      {@link AutomaticsTapApi}
	 * @param device                      {@link Dut}
	 * @param cdlImageWithoutBinExtension The CDL Image name Without ".bin"
	 *                                    Extension
	 * @return Boolean result The cdl status
	 * 
	 * @author BALAJI V
	 * 
	 */
	public static boolean triggerCdlUsingTr181OrTftp(AutomaticsTapApi tapEnv, Dut device,
			String cdlImageWithoutBinExtension) {
		LOGGER.debug("ENTERING METHOD: triggerCdlUsingTr181OrTftp");
		// Boolean variable to store the cdl status
		boolean result = false;
		boolean cdlRequired = true;
		// String to store cdl image with ".bin" extension
		String cdlImageWithBinExtension = cdlImageWithoutBinExtension + BroadBandCdlConstants.BIN_EXTENSION;
		String currentFirmwareVersion = FirmwareDownloadUtils.getCurrentFirmwareFileNameForCdl(tapEnv, device);
		LOGGER.info("ORIGINAL FIRMWARE VERSION OF THE DEVICE IS : " + cdlImageWithoutBinExtension);
		LOGGER.info("CURRENT FIRMWARE VERSION OF THE DEVICE IS : " + currentFirmwareVersion);
		if (currentFirmwareVersion.equalsIgnoreCase(cdlImageWithoutBinExtension)) {
			LOGGER.info("AS THE CURRENT BUILD & REQUIRED BUILD ARE SAME, SKIPPING THE CDL REQUEST.");
			cdlRequired = false;
			result = true;
		}
		if (cdlRequired) {
			LOGGER.info("GOING TO TRIGGER HTTP CDL USING TR-181 PARAMETERS.");
			try {
				result = FirmwareDownloadUtils.triggerHttpCdlUsingTr181(tapEnv, device, cdlImageWithoutBinExtension);
			} catch (Exception exception) {
				// Log & Suppress the exception
				LOGGER.error("FOLLOWING EXECUTION ERROR OCCURED WHILE TRIGGERING HTTP CDL USING TR181 PARAM : "
						+ exception.getMessage());
			}
			if (!result) {
				LOGGER.error(
						"Unable to trigger HTTP CDL using TR-181 Parameters. Going to trigger the TFTP code download via DOCSIS SNMP commands.");
				try {
					result = FirmwareDownloadUtils.triggerAndWaitForTftpCodeDownloadUsingDocsisSnmpCommand(tapEnv,
							device, cdlImageWithBinExtension, false);
				} catch (Exception exception) {
					// Log & Suppress the exception
					LOGGER.error("FOLLOWING EXECUTION ERROR OCCURED WHILE TRIGGERING CDL USING TFTP : "
							+ exception.getMessage());
				}
			}
			LOGGER.info("BUILD IMAGE : '" + cdlImageWithoutBinExtension + "', FLASHED SUCCESSFULLY : " + result);
		}
		LOGGER.debug("ENDING METHOD: triggerCdlUsingTr181OrTftp");
		return result;
	}

	/**
	 * Method to retrieve last reboot reason after device reboot
	 * 
	 * @param tapEnv {@link AutomaticsTapApi} Reference
	 * @param device {@link Dut} Reference
	 * @param last   reboot reason source of reboot
	 *
	 * @author Sumathi Gunasekaran
	 * 
	 */
	public static boolean verifyGivenRebootResonFromWebPaCommand(AutomaticsTapApi tapEnv, Dut device,
			String givenRebootReason) {
		LOGGER.debug("Starting Method : verifyGivenRebootResonFromWebPaCommand");
		boolean status = false;
		String response = null;
		long startTime = System.currentTimeMillis();
		try {
			do {
				if (!CommonMethods.isSTBAccessible(device)) {
					LOGGER.info("STB is not accessible!. Reboot has initiated successfully");
					if (CommonMethods.waitForEstbIpAcquisition(tapEnv, device)) {
						LOGGER.info("Waiting for one Minute for the device to come up with all properties");
						tapEnv.waitTill(AutomaticsConstants.ONE_MINUTE_IN_MILLIS);
						response = getLastRebootReason(tapEnv, device);
						status = CommonMethods.isNotNull(response)
								&& response.toLowerCase().contains(givenRebootReason.toLowerCase());
						break;
					}
				}
			} while ((System.currentTimeMillis() - startTime) < AutomaticsConstants.FIVE_MINUTES && !status
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.TEN_SECOND_IN_MILLIS));
		} catch (Exception exception) {
			LOGGER.error("Exception Occurred while verifying Last reboot reason:" + exception.getMessage());
			status = false;
		}
		LOGGER.debug("Ending Method : verifyGivenRebootResonFromWebPaCommand");
		return status;
	}

	/**
	 * Helper method to trigger the CDL on the device with the Latest Available
	 * Image using HTTP TR-181 Method.
	 * 
	 * @param tapEnv                 {@link AutomaticsTapApi}
	 * @param device                 {@link Dut}
	 * @param currentFirmwareVersion String representing the current firmware
	 *                               version.
	 * 
	 * @return Boolean representing the result of CDL Operation.
	 * @refactor Said Hisham
	 */
	public static boolean getLatestAvailableImageAndTriggerCdl(AutomaticsTapApi tapEnv, Dut device,
			String currentFirmwareVersion) {
		LOGGER.debug("ENTERING METHOD: getLatestAvailableImageAndTriggerCdl");
		String errorMessage = null;
		boolean result = false;
		// String cdlImageWithoutBinExtension =
		// getLatestAvailableImageForBroadBandDevices(device, tapEnv,
		// currentFirmwareVersion);
		String cdlImageWithoutBinExtension = tapEnv.getLatestBuildImageVersionForCdlTrigger(device, false);
		LOGGER.info("LATEST FIRMWARE VERSION: " + cdlImageWithoutBinExtension);
		if (CommonMethods.isNull(cdlImageWithoutBinExtension)) {
			LOGGER.info(
					" GA image obtained from deployed version service is null. Hence getting the image from property file ");
			cdlImageWithoutBinExtension = BroadbandPropertyFileHandler.getAutomaticsPropsValueByResolvingPlatform(
					device, BroadBandPropertyKeyConstants.PARTIAL_PROPERTY_KEY_FOR_GA_IMAGE);
			LOGGER.info("Latest Firmware version from property file: " + cdlImageWithoutBinExtension);
		}

		LOGGER.info("IMAGE TO BE FLASHED: " + cdlImageWithoutBinExtension);
		if (CommonMethods.isNull(cdlImageWithoutBinExtension)) {
			errorMessage = "CDL BUILD NAME IS NOT AVAILABLE. HENCE BLOCKING THE EXCEPTION.";
			LOGGER.error(errorMessage);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + errorMessage);
		}
		if (currentFirmwareVersion.equalsIgnoreCase(cdlImageWithoutBinExtension)) {
			errorMessage = "AS THE CURRENT BUILD & REQUIRED BUILD ARE SAME, SKIPPING THE PRE-CONDITION.";
			LOGGER.error(errorMessage);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + errorMessage);
		} else {
			if (BroadBandCommonUtils.isProdBuildOnDevice(device)) {
				LOGGER.info("CURRENT BUILD IS PROD; IMAGE UPGRADE WILL HAPPEN USING TFTP.");
				result = triggerAndWaitForTftpCodeDownloadUsingDocsisSnmpCommand(tapEnv, device,
						cdlImageWithoutBinExtension + BroadBandCdlConstants.BIN_EXTENSION, false);
				LOGGER.info("TFTP CDL USING TR-181 PARAMETERS: " + result);
			} else {
				LOGGER.info("CURRENT BUILD IS NON-PROD; IMAGE UPGRADE WILL HAPPEN USING HTTP & TR-181 PARAMETERS.");
				result = triggerHttpCdlUsingTr181(tapEnv, device, cdlImageWithoutBinExtension);
				LOGGER.info("HTTP CDL USING TR-181 PARAMETERS: " + result);
			}
		}
		if (!result) {
			errorMessage = "CDL COULD NOT BE PERFORMED TO THE REQUIRED RELEASE BUILD. HENCE BLOCKING THE EXCEPTION.";
			LOGGER.error(errorMessage);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + errorMessage);
		}
		LOGGER.debug("ENDING METHOD: getLatestAvailableImageAndTriggerCdl");
		return result;
	}

}

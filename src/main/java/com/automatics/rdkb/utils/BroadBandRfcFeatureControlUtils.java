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

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Dut;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.rdkb.reversessh.BroadBandReverseSshUtils;
import com.automatics.rdkb.utils.CommonUtils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.exceptions.TestException;
import com.automatics.http.ServerCommunicator;
import com.automatics.http.ServerResponse;
import com.automatics.utils.AutomaticsPropertyUtility;

public class BroadBandRfcFeatureControlUtils {

	/** SLF4J logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandRfcFeatureControlUtils.class);

	/**
	 * RFC properties file path in nvram
	 */
	public static final String NVRAM_RFC_PROPERTIES = "/nvram/rfc.properties";

	/** Command to clear RFC hash value */
	private static final String CMD_CLEAR_HASH_VALUE = "rm /tmp/RFC/.hashValue";

	/**
	 * RFC properties file path in etc
	 */
	public static final String ETC_RFC_PROPERTIES = "/etc/rfc.properties";

	/**
	 * Command to copy dcm.properties from etc to opt
	 **/
	public static String CMD_CP_RFC_PROPERTIES = "cp " + ETC_RFC_PROPERTIES + " /nvram/";

	/** Command to copy the dcm.properties file */
	public static final String CMD_CP_DCM_PROPERTIES = "cp /etc/dcm.properties /nvram/";

	/**
	 * Pattern for getting the RFC server URL from rfc.properties
	 */
	public static final String PATTERN_FOR_RFC_CONFIG_SERVER_URL = "RFC_CONFIG_SERVER_URL=(\\S+)";

	/**
	 * first part of the sed command for switching Xconf URLs
	 **/
	public static final String SED_COMMAND_FIRST_PART = "sed -i 's#";
	/**
	 * final part of the sed command with rfc properties as the file where url is to
	 * be replaced
	 **/
	public static final String SED_COMMAND_LAST_PART_WITH_DCM_PROPS = "#g' /nvram/dcm.properties";

	public static final String SED_COMMAND_LAST_PART_WITH_RFC_PROPS = "#g' /nvram/rfc.properties";

	/**
	 * Pattern for getting the RFC server URL from dcm.properties
	 */
	public static final String PATTERN_FOR_DCM_RFC_SERVER_URL = "DCM_RFC_SERVER_URL=(\\S+)";

	/** Command to remove dcmrfc.log **/
	public static String CMD_REMOVE_OPT_LOGS_RFCSCRIPT_LOG = "rm -rf /rdklogs/logs/dcmrfc.log";

	/** Error Message text */
	private static String errorMessage = null;

	/** Property key for rfc delete all settings url */
	public static String PROP_KEY_RFC_DELETE_ALL_URL = "proxy.xconf.rfc.delete.all.settings.url";

	/** Property key for rfc delete update settings url */
	public static String PROP_KEY_RFC_DELETE_UPDATE_URL = "proxy.xconf.rfc.delete.update.settings.url";

	/** Property key for rfc delete feature settings url */
	public static String PROP_KEY_RFC_DELETE_FEATURE_URL = "proxy.xconf.rfc.delete.feature.settings.url";

	/** String feature name */
	public static String STRING_FEATURE_NAME = "FEATURE_NAME";

	/** Log message for completed rfc pass */
	public static String LOG_MESSAGE_COMPLETED_RFC_PASS = "COMPLETED RFC PASS";

	/**
	 * Method to verify parameter is set to corresponding value through RFC
	 * 
	 * @param device         {@link Dut}
	 * @param tapEnv         {@link AutomaticsTapApi}
	 * @param webpaParameter String value of parameter set by RFC
	 * @param value          String containing value of parameter set by RFC
	 * 
	 * @return true if parameter value is updated to value set by RFC
	 * 
	 * @author Ashwin sankara
	 * @refactor Govardhan
	 */
	public static BroadBandResultObject verifyParameterUpdatedByRfc(Dut device, AutomaticsTapApi tapEnv,
			String webpaParameter, String value) {
		LOGGER.debug("Entering method: verifyParameterUpdatedByRfc");
		BroadBandResultObject result = new BroadBandResultObject();
		String response = null;
		result.setStatus(false);
		result.setErrorMessage(null);
		long startTime = System.currentTimeMillis();

		try {
			String parameter = webpaParameter;
			if (parameter.contains(BroadBandWebPaConstants.WEBPA_TABLE_DEVICE_WIFI)) {
				if (parameter.contains(BroadBandWebPaConstants.WEBPA_INDEX_2_4_GHZ_PRIVATE_SSID)) {
					parameter = parameter.replace(BroadBandWebPaConstants.WEBPA_INDEX_2_4_GHZ_PRIVATE_SSID, "1");
				} else if (parameter.contains(BroadBandWebPaConstants.WEBPA_INDEX_5_GHZ_PRIVATE_SSID)) {
					parameter = parameter.replace(BroadBandWebPaConstants.WEBPA_INDEX_5_GHZ_PRIVATE_SSID, "2");
				}
			}
			do {
				if (CommonMethods.isNotNull(BroadBandCommonUtils.searchLogFiles(tapEnv, device,
						BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.TR181_DOT, parameter),
						BroadBandCommandConstants.FILE_RFC_CONFIGDATA_LOG))) {
					break;
				} else {
					if ((System.currentTimeMillis() - startTime) > BroadBandTestConstants.FIFTEEN_MINUTES_IN_MILLIS) {
						throw new TestException("Unable to find entry for parameter: " + parameter + " with value: "
								+ value + " in rfc_configdata");
					}
				}
			} while (BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.ONE_MINUTE_IN_MILLIS));
			result.setErrorMessage("Unable to find successful RFC update or check for parameter: " + parameter
					+ " with value: " + value + " in dcmrfc.log");
			if (CommonMethods.isNotNull(BroadBandCommonUtils.searchLogFiles(tapEnv, device,
					BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.RFC_LOG_PATTERN,
							parameter, BroadBandTestConstants.WITH_VALUE, value),
					BroadBandCommandConstants.FILE_DCMRFC_LOG, BroadBandTestConstants.TEN_MINUTE_IN_MILLIS,
					BroadBandTestConstants.ONE_MINUTE_IN_MILLIS))) {
				result.setErrorMessage("Value of parameter is null");
				response = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv, webpaParameter);
				if (CommonMethods.isNotNull(response)) {
					result.setErrorMessage("Parameter: " + parameter + " is not updated with value: " + value
							+ " after setting by RFC");
					result.setStatus(response.trim().equals(value));
				}
			}
		} catch (Exception e) {
			result.setErrorMessage(e.getMessage());
		}
		LOGGER.debug("Exiting method: verifyParameterUpdatedByRfc");
		return result;
	}

	/**
	 * Helper method to copy the file from etc to dcm and verify whether the file
	 * has been copied
	 * 
	 * @param device The {@link Dut} object.
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @return true if the file has been copied, else false
	 */
	public static boolean copyRfcPropertiesFromEtcToNVRAM(Dut device, AutomaticsTapApi tapApi) {

		LOGGER.debug("STARTING METHOD : BroadBandRfcFeatureControlUtils.copyRfcPropertiesFromEtcToNVRAM");
		// stores the status of whether the file is copied from etc to opt
		boolean status = false;
		String errorMessage = null;

		if (CommonMethods.isFileExists(device, tapApi, ETC_RFC_PROPERTIES)) {
			errorMessage = "Unable to copy rfc.properties from /etc/ to /nvram";
			if (!CommonMethods.isFileExists(device, tapApi, NVRAM_RFC_PROPERTIES)) {
				// copying the file from etc to opt
				tapApi.executeCommandUsingSsh(device, CMD_CP_RFC_PROPERTIES);
				status = CommonMethods.isFileExists(device, tapApi, NVRAM_RFC_PROPERTIES);
			} else {
				status = true;
			}
		} else {
			errorMessage = "Unable to copy dcm.properties from /etc/ to /nvram";
			if (!CommonMethods.isFileExists(device, tapApi, BroadBandTestConstants.DCM_PROPERTIES_FILE_NVRAM_FOLDER)) {
				// copying the file from etc to opt
				tapApi.executeCommandUsingSsh(device, CMD_CP_DCM_PROPERTIES);
				status = CommonMethods.isFileExists(device, tapApi,
						BroadBandTestConstants.DCM_PROPERTIES_FILE_NVRAM_FOLDER);
			} else {
				status = true;
			}
		}
		// status = CommonUtils.isFileExists(device, tapApi,
		// BroadBandTestConstants.DCM_PROPERTIES_FILE_NVRAM_FOLDER);

		if (!status) {
			throw new TestException(errorMessage);
		}
		LOGGER.debug("ENDING METHOD : BroadBandRfcFeatureControlUtils.copyRfcPropertiesFromEtcToNVRAM");
		return status;
	}

	/**
	 * 
	 * Helper method to copy and to update the dcm.properties with the Proxy XCONF
	 * update url
	 * 
	 * @param device instance of Dut
	 * @param tapEnv instance AutomaticsTapApi
	 * @return true if file is copied and updated with proxy xconf url
	 * @author Govardhan
	 */
	public static boolean copyAndUpdateRfcPropertiesNewXconfUrl(Dut device, AutomaticsTapApi tapEnv, String xconfUrl) {

		LOGGER.debug("STARTING METHOD: BroadBandRfcFeatureControlUtils.copyAndUpdateRfcPropertiesNewXconfUrl");
		// for storing command responses
		String response = null;
		// for storing the status
		boolean status = false;

		if (CommonMethods.isFileExists(device, tapEnv, ETC_RFC_PROPERTIES)) {

			response = tapEnv.executeCommandUsingSsh(device, BroadBandCommonUtils
					.concatStringUsingStringBuffer(BroadBandTestConstants.CAT_COMMAND, NVRAM_RFC_PROPERTIES));
			String logUploadUrl = CommonMethods.patternFinder(response, PATTERN_FOR_RFC_CONFIG_SERVER_URL);
			if (null != logUploadUrl) {
				LOGGER.info("Replacing the url " + logUploadUrl + " using 'sed' command..........");
			} else {
				throw new TestException(
						"Failed to update the RFC_CONFIG_SERVER_URL with proxy xconf url in /nvram/rfc.properties "
								+ "as RFC_CONFIG_SERVER_URL entry is not available");
			}
			// replacing using 'sed' command
			String cmdForReplaceUrl = SED_COMMAND_FIRST_PART + logUploadUrl.trim() + RDKBTestConstants.DELIMITER_HASH
					+ xconfUrl + SED_COMMAND_LAST_PART_WITH_RFC_PROPS;

			tapEnv.executeCommandUsingSsh(device, cmdForReplaceUrl);

			// Checking whether LOG_UPLOAD_URL is replaced with sed
			LOGGER.info("Checking whether RFC_CONFIG_SERVER_URL is replaced with proxy xconf url!!");
			response = tapEnv.executeCommandUsingSsh(device, BroadBandCommonUtils
					.concatStringUsingStringBuffer(BroadBandTestConstants.CAT_COMMAND, NVRAM_RFC_PROPERTIES));

			// to alert the execution regarding various outcomes of this test
			if (CommonMethods.isNotNull(response) && response.contains(xconfUrl)) {
				LOGGER.info("********Succesfully updated the url********");
				status = true;
			} else {
				throw new TestException(
						"Failed to update the RFC_CONFIG_SERVER_URL with proxy xconf url in /nvram/rfc.properties");
			}

		} else {

			response = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.CMD_CAT_DCM_PROPERTIES);
			String logUploadUrl = CommonMethods.patternFinder(response, PATTERN_FOR_DCM_RFC_SERVER_URL);
			if (null != logUploadUrl) {
				LOGGER.info("Replacing the url " + logUploadUrl + " using 'sed' command..........");
			} else {
				throw new TestException(
						"Failed to update the DCM_RFC_SERVER_URL with proxy xconf url in /nvram/dcm.properties "
								+ "as DCM_RFC_SERVER_URL entry is not available");
			}
			// replacing using 'sed' command
			String cmdForReplaceUrl = SED_COMMAND_FIRST_PART + logUploadUrl.trim() + RDKBTestConstants.DELIMITER_HASH
					+ xconfUrl + SED_COMMAND_LAST_PART_WITH_DCM_PROPS;

			tapEnv.executeCommandUsingSsh(device, cmdForReplaceUrl);

			// Checking whether LOG_UPLOAD_URL is replaced with sed
			LOGGER.info("Checking whether DCM_RFC_SERVER_URL is replaced with proxy xconf url!!");
			response = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.CMD_CAT_DCM_PROPERTIES);

			// to alert the execution regarding various outcomes of this test
			if (CommonMethods.isNotNull(response) && response.contains(xconfUrl)) {
				LOGGER.info("********Succesfully updated the url********");
				status = true;
			} else {
				throw new TestException(
						"Failed to update the DCM_RFC_SERVER_URL with proxy xconf url in /nvram/dcm.properties");
			}
		}
		LOGGER.debug("ENDING METHOD: BroadBandRfcFeatureControlUtils.copyAndUpdateRfcPropertiesNewXconfUrl");
		return status;

	}

	/**
	 * helper method to post dcm data to proxy server for updating dcm properties
	 * 
	 * @param device The {@link Dut} object.
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @return http post - server response
	 * @Refactor Athira
	 */
	public static int postDataToProxyXconfDcmServer(Dut device, AutomaticsTapApi tapApi, String rfcSettings) {

		LOGGER.debug("STARTING METHOD : BroadBandRfcFeatureControlUtils.postDataToProxyXconfDcmServer");
		// for storing server response
		ServerResponse serverResponse = null;
		// for storing request header
		Map<String, String> headers = new HashMap<String, String>();
		// creating server communicator instance for sending request
		ServerCommunicator serverCommunicator = new ServerCommunicator();
		headers.put("Content-Type", "application/json");
		// getting POST url for proxy Xconf
		String proxyDcmServerUpdateUrl = AutomaticsTapApi.getSTBPropsValue("proxy.xconf.rfc.update.url");
		LOGGER.info("proxyDcmServerUpdateUrl :" + proxyDcmServerUpdateUrl);
		serverResponse = serverCommunicator.postDataToServer(proxyDcmServerUpdateUrl, rfcSettings, "POST", 60000,
				headers);
		return serverResponse.getResponseCode();
	}

	/**
	 * Helper method to execute the precindtion for Rfc Tests *
	 * 
	 * @param device {@link Dut}
	 * @param tapEnv {@link AutomaticsTapApi}
	 */
	public static void clearDCMRFClog(Dut device, AutomaticsTapApi tapEnv) {

		LOGGER.info("STARTING METHOD: BroadBandRfcFeatureControlUtils.executePreconditionForRfcTests");
		// removing dcmrfc.log
		tapEnv.executeCommandUsingSsh(device, CMD_REMOVE_OPT_LOGS_RFCSCRIPT_LOG);
		LOGGER.info("ENDING METHOD: BroadBandRfcFeatureControlUtils.executePreconditionForRfcTests");
	}

	/**
	 * Helper method to execute the preconditions for RFC tests along with posting
	 * RFC settings to proxy server
	 * 
	 * @param device      {@link Dut}
	 * @param tapApi      {@link AutomaticsTapApi}
	 * @param rfcSettings RFC settings which are to be posted to the proxy server
	 * 
	 * @return true if post request is successful else false
	 * @author Govardhan
	 */
	public static boolean executePreconditionForRfcTests(Dut device, AutomaticsTapApi tapEnv, String rfcSettings)
			throws TestException {

		LOGGER.info("STARTING METHOD: BroadBandRfcFeatureControlUtils.executePreconditionForRfcTests");
		boolean status = false;
		int responseCode = 0;
		try {
			tapEnv.executeCommandUsingSsh(device, CMD_CLEAR_HASH_VALUE);
			copyRfcPropertiesFromEtcToNVRAM(device, tapEnv);
			copyAndUpdateRfcPropertiesNewXconfUrl(device, tapEnv,
					AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_PROXY_XCONF_RFC_URL));

			LOGGER.info("PAY LOAD DATA: " + rfcSettings);
			String stbMacAddress = device.getHostMacAddress();
			LOGGER.info("Device Mac address: " + stbMacAddress);
			if (CommonMethods.isNotNull(stbMacAddress)) {
				rfcSettings = rfcSettings.replace(BroadBandTestConstants.CONSTANT_REPLACE_STBMAC_LSAPAYLOADDATA,
						stbMacAddress);
				LOGGER.info("PAY LOAD DATA: " + rfcSettings);
				responseCode = postDataToProxyXconfDcmServer(device, tapEnv, rfcSettings);
				LOGGER.info("RESPONSE CODE: " + responseCode);
				if (responseCode==RDKBTestConstants.CONSTANT_200 || responseCode==RDKBTestConstants.CONSTANT_201) {
					LOGGER.info("POST request for rfc settings is successful");
					clearDCMRFClog(device, tapEnv);
					status = true;
				} else {
					throw new TestException("POST request failed!! Proxy server returning " + responseCode);
				}
			} else {
				throw new TestException("Device Mac address is not obtained");
			}
			LOGGER.debug("ENDING METHOD: BroadBandRfcFeatureControlUtils.executePreconditionForRfcTests");
			return status;
		} catch (Exception e) {
			LOGGER.error("Exception occured during RFC test precondition");
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + e.getMessage());
		}
	}

	/**
	 * Method to enable or disable feature in proxy/mock xconf
	 * 
	 * @param tapEnv              instance of {@link AutomaticsTapApi}
	 * @param device              The {@link Dut} object.
	 * @param featureName         Name of the feature that has to be
	 *                            enabling/disabling
	 * @param enableOrDisableFlag Flag to set feature enable or disable
	 * @return status Enabling/disabling feature in proxy/mock xconfstatus
	 * @throws JSONException
	 * 
	 * @author Susheela C
	 * @Refactor Athira
	 */
	public static boolean enableOrDisableFeatureInProxyXconf(AutomaticsTapApi tapEnv, Dut device, String featureName,
			boolean enableOrDisableFlag) throws JSONException {
		LOGGER.debug("STARTING METHOD: BroadBandRfcFeatureControlUtils.enableOrDisableFeatureInProxyXconf");
		boolean result = false;
		String rfcPayLoadData = null;
		int responseCode = 0;

		switch (featureName) {
		case BroadBandTestConstants.CONFIGURABLE_SSH: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsPropertyUtility
						.getProperty(BroadBandTestConstants.PROP_KEY_PAYLOAD_ENABLE_CONFIGURABLE_SSH)
						+ AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_SSH_WHITELIST_IPS)
						+ AutomaticsConstants.COMMA + BroadBandTestConstants.SYMBOL_QUOTES
						+ AutomaticsPropertyUtility
								.getProperty(BroadBandTestConstants.PROP_KEY_NON_WHITE_LISTED_JUMP_SERVER_IPV6)
						+ BroadBandTestConstants.SYMBOL_QUOTES + "]}]}";
			} else {
				rfcPayLoadData = AutomaticsPropertyUtility
						.getProperty(BroadBandTestConstants.PROP_KEY_PAYLOAD_DISABLE_CONFIGURABLE_SSH)
						+ AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_SSH_WHITELIST_IPS)
						+ "]}]}";
			}
			break;
		}
		case BroadBandTestConstants.FEATURE_NAME_IDS: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_IDS_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_IDS_DISABLE);
			}
			break;
		}
		case BroadBandTestConstants.FIREWALL_PORT: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_ENABLE_FIREWALL_PORT);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_DISABLE_FIREWALL_PORT);
			}
			break;
		}
		case BroadBandTestConstants.RFC_FEATURE_NAME_IDS1: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_IDS1_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_IDS1_DISABLE);
			}
			break;
		}
		case BroadBandTestConstants.CONFIGURABLE_TELEMETRY: {
			rfcPayLoadData = AutomaticsTapApi
					.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_CONFIGURABLE_TELEMETRY_PAYLOAD);
			break;
		}
		case BroadBandTestConstants.FINGER_PRINT: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_ENABLE_FINGER_PRINT);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_DISABLE_FINGER_PRINT);
			}
			break;
		}
		case BroadBandTestConstants.SOFT_FLOWD: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_ENABLE_SOFTFLOWD);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_DISABLE_SOFTFLOWD);
			}
			break;
		}
		case BroadBandTestConstants.CODEBIG_FIRST: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_ENABLE_CODEBIG_FIRST);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_DISABLE_CODEBIG_FIRST);
			}
			break;
		}
		case BroadBandTestConstants.SECURE_UPLOAD: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_TO_ENABLE_DISABLE_CLOUD_UPLOAD_ENCRYPTION)
						.replace(BroadBandTestConstants.STRING_TO_CHANGE_ENCRYPT_CLOUD_UPLOAD_STATUS,
								BroadBandTestConstants.TRUE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_TO_ENABLE_DISABLE_CLOUD_UPLOAD_ENCRYPTION)
						.replace(BroadBandTestConstants.STRING_TO_CHANGE_ENCRYPT_CLOUD_UPLOAD_STATUS,
								BroadBandTestConstants.FALSE);
			}
			break;
		}
		case BroadBandTestConstants.SNMPV3: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_SNMPV3_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_SNMPV3_DISABLE);
			}
			break;
		}
		case BroadBandTestConstants.PWD_FAILURE: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_PWD_FAILURE_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_PWD_FAILURE_DISABLE);
			}
			break;
		}
		case BroadBandTestConstants.CWMP: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_CWMP_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_CWMP_DISABLE);
			}
			break;
		}
		case BroadBandTestConstants.FEATURE_NAME_FAN: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_FAN_MAXOVERRIDE_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_FAN_MAXOVERRIDE_DISABLE);
			}
			break;
		}
		case BroadBandTraceConstants.LOG_MESSAGE_BOOTSTRAP_CONFIG: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi.getSTBPropsValue(BroadBandTestConstants.PROPERTY_KEY_BOOTSTRAP);
			} else {
				rfcPayLoadData = AutomaticsTapApi.getSTBPropsValue(BroadBandTestConstants.PROPERTY_KEY_BOOTSTRAP);
			}
			break;
		}
		case BroadBandTestConstants.FEATURE_NAME_FORWARD_SSH: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_FORWARD_SSH_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_FORWARD_SSH_DISABLE);
			}
			break;
		}
		case BroadBandTraceConstants.LOG_MESSAGE_CPU_MEMORY_FRAGMENTATION: {
			rfcPayLoadData = AutomaticsTapApi
					.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_CPU_MEMORY_FRAGMENTATION);
			break;
		}
		case BroadBandTestConstants.FEATURE_NAME_PRIVACY_PROTECTION_ENABLE: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_PRIVACY_PROTECTION_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_PRIVACY_PROTECTION_DISABLE);
			}
			break;
		}

		case BroadBandTestConstants.SNMPV3_DH_KICKSTART_TABLE_RFC_FEATURE: {

			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_CM_SNMPV3_PAYLOAD_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_CM_SNMPV3_PAYLOAD_DISABLE);
			}
			break;
		}

		case BroadBandTestConstants.FEATURE_NAME_MTLSDCMUPLOAD_CONFIG: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_MTLSDCMUPLOAD_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_MTLSDCMUPLOAD_DISABLE);
			}
			break;
		}
		case BroadBandTestConstants.FEATURE_NAME_NTP_TIME_SERVER: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_NEW_NTP_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_NEW_NTP_DISABLE);
			}
			break;
		}
		case BroadBandTestConstants.FEATURE_NAME_INTERWORKING_CONFIG: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_INTERWORKING_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_INTERWORKING_DISABLE);
			}
			break;
		}
		case BroadBandTestConstants.FEATURE_NAME_OVS: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_OVS_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_OVS_DISABLE);
			}
			break;
		}
		case BroadBandTestConstants.CONFIGURABLE_TELEMETRY_ENDPOINT2: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_TELEMETRYENDPOINT_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_TELEMETRYENDPOINT_DISABLE);
			}
			break;
		}
		case BroadBandTestConstants.CONFIGURABLE_CRED_DWNLD: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_CRED_DWNLD_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_CRED_DWNLD_DISABLE);
			}
			break;
		}

		case BroadBandTestConstants.CONFIGURABLE_CRED_DWNLD_2: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_CRED_DWNLD_USE_DISABLE);
			}
			break;
		}
		case BroadBandTestConstants.CONFIGURABLE_NONROOT_SUPPORT: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_NONROOT_SUPPORT_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_NONROOT_SUPPORT_DISABLE);
			}
			break;
		}
		case BroadBandTestConstants.FEATURE_NAME_AUTO_EXCLUDE: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_AUTO_EXCLUDE_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_AUTO_EXCLUDE_DISABLE);
			}
			break;
		}
		case BroadBandTestConstants.FEATURE_NAME_WIFIBLASTER_CONFIG: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_WIFIBLASTER_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_WIFIBLASTER_DISABLE);
			}
			break;
		}
		case BroadBandTestConstants.RFC_NAME_AGGRESSIVE_SELFHEAL: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_AGG_SELFHEAL_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_AGG_SELFHEAL_DISABLE);
			}
			break;
		}
		case BroadBandTestConstants.FEATURE_NAME_WEBCONFIG: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_WEBCONFIG_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_WEBCONFIG_DISABLE);
			}
			break;
		}
		case BroadBandTestConstants.CONFIGURABLE_PASSPOINT: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_PASSPOINT_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_PASSPOINT_DISABLE);
			}
			break;
		}
		case BroadBandTestConstants.CONFIGURABLE_CABUNDLE: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_DLCASTORE_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_DLCASTORE_DISABLE);
			}
			break;
		}
		case BroadBandTestConstants.CONFIGURABLE_HARDWAREHEALTHTEST: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_ENABLE_CONFIGURABLE_HHT);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_DISABLE_CONFIGURABLE_HHT);
			}
			break;
		}
		case BroadBandTestConstants.OVS_FEATURE_NAME: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_OVS_ENABLE_PAYLOAD);
			} else {
				rfcPayLoadData = AutomaticsTapApi.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_OVS_DISABLE_PAYLOAD);
			}
			break;
		}
		case BroadBandTestConstants.FEATURE_NAME_FAN_MULTIPLE: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_FAN_MULTIPLE_MAXOVERRIDE_ENABLE);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_FAN_MULTIPLE_MAXOVERRIDE_DISABLE);
			}
			break;
		}

		case BroadBandTestConstants.CONFIGURABLE_NONROOT_BLOCKLIST_TELEMETRY: {
			if (enableOrDisableFlag) {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_NONROOT_SUPPORT_BLOCKLIST_TELEMETRY);
			} else {
				rfcPayLoadData = AutomaticsTapApi
						.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_NONROOT_BLOCKLIST);
			}
			break;
		}
		}

		LOGGER.info("PAY LOAD DATA: " + rfcPayLoadData);
		JSONObject jsonObj = new JSONObject(rfcPayLoadData);
		String stbMacAddress = device.getHostMacAddress();
		LOGGER.info("Device Mac address: " + stbMacAddress);

		if (null != jsonObj && CommonMethods.isNotNull(stbMacAddress)) {
			rfcPayLoadData = jsonObj.toString();
			rfcPayLoadData = rfcPayLoadData.replace(BroadBandTestConstants.CONSTANT_REPLACE_STBMAC_LSAPAYLOADDATA,
					stbMacAddress);
			LOGGER.info("PAY LOAD DATA: " + rfcPayLoadData);
			responseCode = BroadBandRfcFeatureControlUtils.postDataToProxyXconfDcmServer(device, tapEnv,
					rfcPayLoadData);
			LOGGER.info("RESPONSE CODE: " + responseCode);
			if (responseCode == AutomaticsConstants.CONSTANT_200) {
				result = true;
			}
		} else {
			LOGGER.error("Device Mac address is not obtained");
		}
		LOGGER.info("Status of " + featureName + " enable " + enableOrDisableFlag + " in xconf: " + result);
		LOGGER.debug("ENDING METHOD: BroadBandRfcFeatureControlUtils.enableOrDisableFeatureInProxyXconf");
		return result;
	}

	/**
	 * Method to Enabling/disabling Feature By RFC
	 * 
	 * @param tapEnv              instance of {@link AutomaticsTapApi}
	 * @param device              The {@link Dut} object.
	 * @param featureName         Name of the feature that has to be
	 *                            enabling/disabling
	 * @param enableOrDisableFlag Flag to set Feature enable or disable
	 * @return status enabling/disabling status
	 * @throws JSONException
	 * 
	 * 
	 * @author Susheela C
	 * @Refactor Athira
	 */
	public static boolean enableOrDisableFeatureByRFC(AutomaticsTapApi tapEnv, Dut device, String featureName,
			boolean enableOrDisableFlag) throws JSONException {

		LOGGER.debug("STARTING METHOD: BroadBandRfcFeatureControlUtils.enableOrDisableFeatureByRFC");
		String proxyXconfUrl = AutomaticsTapApi.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PROXY_XCONF_RFC_URL);
		LOGGER.info("proxyXconfUrl is " + proxyXconfUrl);
		boolean status = false;
		String response = null;
		try {
			if (BroadBandRfcFeatureControlUtils.enableOrDisableFeatureInProxyXconf(tapEnv, device, featureName,
					enableOrDisableFlag)) {
				LOGGER.info("in (BroadBandRfcFeatureControlUtils.enableOrDisableFeatureInProxyXconf ");
				if (BroadBandRfcFeatureControlUtils.copyRfcPropertiesFromEtcToNVRAM(device, tapEnv)) {
					LOGGER.info("in (copyRfcPropertiesFromEtcToNVRAM ");
					status = CommonMethods.copyAndUpdateRfcPropertiesNewXconfUrl(device, tapEnv, proxyXconfUrl);
					LOGGER.info("status in copyRfcPropertiesFromEtcToNVRAM " + status);
					tapEnv.executeCommandUsingSsh(device, CMD_CLEAR_HASH_VALUE);
					status = CommonUtils.rebootAndWaitForIpAcquisition(tapEnv, device);
					LOGGER.info("status CommonUtils.rebootAndWaitForIpAcquisition " + status);
				} else {
					throw new TestException("Error faced while copying dcm properties file from etc to nvram folder");
				}
			} else {
				throw new TestException(featureName + " not enabled/disbaled in XCONF");
			}
			response = CommonUtils.searchLogFilesWithPollingInterval(tapEnv, device,
					BroadBandTraceConstants.LOG_MESSAGE_COMPLETED_RFC_PASS, BroadBandCommandConstants.FILE_DCMRFC_LOG,
					BroadBandTestConstants.TEN_MINUTE_IN_MILLIS, BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
			if (CommonMethods.isNotNull(response)) {
				LOGGER.info("Response from dcmrfc log file is " + response);
				CommonUtils.patternSearchFromTargetString(response,
						BroadBandTraceConstants.LOG_MESSAGE_COMPLETED_RFC_PASS);
			}

			status = CommonUtils.rebootAndWaitForIpAcquisition(tapEnv, device); // Reboot a second time, as per

		} catch (Exception exception) {
			errorMessage = exception.getMessage();
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}

		LOGGER.debug("ENDING METHOD: BroadBandRfcFeatureControlUtils.enableOrDisableFeatureByRFC");
		return status;
	}

	/*
	 * @param tapEnv {@link AutomaticsTapApi}
	 * 
	 * @param device {@link Dut}
	 * 
	 * @refactor Govardhan
	 */
	public static int clearSettingsInProxyXconfDcmServerForRDKB(Dut device, AutomaticsTapApi tapEnv,
			boolean allSettings, String... featureName) {
		return clearMultipleFeaturesInProxyXconfDcmServerForRDKB(device, tapEnv, allSettings, featureName, 2);
	}

	/*
	 * @param tapEnv {@link AutomaticsTapApi}
	 * 
	 * @param device {@link Dut}
	 * 
	 * @refactor Govardhan
	 */
	public static int clearMultipleFeaturesInProxyXconfDcmServerForRDKB(Dut device, AutomaticsTapApi tapEnv,
			boolean allSettings, String[] featureName, int rebootCount) {
		LOGGER.debug("STARTING METHOD : clearMultipleFeaturesInProxyXconfDcmServerForRDKB");
		String payLoadData = null;
		int statusCode = -1;

		try {
			if (allSettings && featureName == null) {
				payLoadData = tapEnv.getSTBPropsValue(PROP_KEY_RFC_DELETE_ALL_URL);
				statusCode = CommonMethods.postPayLoadData(tapEnv, device, payLoadData);
			} else if (!allSettings && featureName == null) {
				payLoadData = tapEnv.getSTBPropsValue(PROP_KEY_RFC_DELETE_UPDATE_URL);
				statusCode = CommonMethods.postPayLoadData(tapEnv, device, payLoadData);
			} else if (featureName != null) {
				String[] var8 = featureName;
				int var9 = featureName.length;

				for (int var10 = BroadBandTestConstants.CONSTANT_0; var10 < var9; ++var10) {
					String feature = var8[var10];
					payLoadData = tapEnv.getSTBPropsValue(PROP_KEY_RFC_DELETE_FEATURE_URL);
					payLoadData = payLoadData.replace(STRING_FEATURE_NAME, feature);
					statusCode = CommonMethods.postPayLoadData(tapEnv, device, payLoadData);
				}
			}
		} catch (Exception var16) {
			LOGGER.error("Exception occured during testing with delete " + var16.getMessage());
		} finally {
			// boolean var12 = false;
			tapEnv.executeCommandUsingSsh(device, CMD_CLEAR_HASH_VALUE);
			tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_CLEAR_DCMRFC_LOG);
			if (BroadBandWebPaUtils.setVerifyWebPAInPolledDuration(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_RFC_CONTROL, BroadBandTestConstants.CONSTANT_2,
					BroadBandTestConstants.STRING_CONSTANT_1, BroadBandTestConstants.ONE_MINUTE_IN_MILLIS,
					BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS)) {
				LOGGER.info("Wait for three minutes to dcmrfc.log get update");
				tapEnv.waitTill(BroadBandTestConstants.THREE_MINUTE_IN_MILLIS);
				String response = CommonUtils.searchLogFilesWithPollingInterval(tapEnv, device,
						LOG_MESSAGE_COMPLETED_RFC_PASS, BroadBandCommandConstants.FILE_DCMRFC_LOG,
						BroadBandTestConstants.TEN_MINUTE_IN_MILLIS, BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
				if (CommonMethods.isNotNull(response)) {
					LOGGER.info("Response from dcmrfc log file is " + response);
					CommonUtils.patternSearchFromTargetString(response, LOG_MESSAGE_COMPLETED_RFC_PASS);
				}

				BroadBandCommonUtils.rebootViaWebpaAndWaitForStbAccessible(device, tapEnv);
			}

		}

		LOGGER.debug("ENDING METHOD : clearMultipleFeaturesInProxyXconfDcmServerForRDKB");
		return statusCode;
	}

	/**
	 * Method to Enabling/disabling Feature By RFC with single reboot
	 * 
	 * @param tapEnv              The AutomaticsTapApi.
	 * @param device              The {@link Dut} object.
	 * @param featureName         Name of the feature that has to be
	 *                            enabling/disabling
	 * @param enableOrDisableFlag Flag to set Feature enable or disable
	 * @return status enabling/disabling status
	 * @throws TestException
	 * @refactor Govardhan
	 * 
	 */
	public static boolean rfcFeatureWithSingleRebootRetrievewNow(AutomaticsTapApi tapEnv, Dut device,
			String featureName, boolean enableOrDisableFlag) throws TestException {
		LOGGER.debug("STARTING METHOD: BroadBandRfcFeatureControlUtils.rfcFeatureWithSingleRebootRetrievewNow");
		// Variable declaration starts
		String proxyXconfUrl = tapEnv.getSTBPropsValue(RDKBTestConstants.PROP_KEY_PROXY_XCONF_RFC_URL);
		boolean status = false;
		String response = null;
		// Variable declaration ends
		try {
			if (BroadBandRfcFeatureControlUtils.enableOrDisableFeatureInProxyXconf(tapEnv, device, featureName,
					enableOrDisableFlag)) {
				if (BroadBandRfcFeatureControlUtils.copyRfcPropertiesFromEtcToNVRAM(device, tapEnv)) {
					status = BroadBandRfcFeatureControlUtils.copyAndUpdateRfcPropertiesNewXconfUrl(device, tapEnv,
							proxyXconfUrl);
					tapEnv.executeCommandUsingSsh(device, CMD_CLEAR_HASH_VALUE);
					tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_CLEAR_DCMRFC_LOG);
					if (status) {
						status = BroadBandWebPaUtils.setVerifyWebPAInPolledDuration(device, tapEnv,
								BroadBandWebPaConstants.WEBPA_PARAM_RFC_CONTROL, BroadBandTestConstants.CONSTANT_2,
								BroadBandTestConstants.STRING_CONSTANT_1, BroadBandTestConstants.ONE_MINUTE_IN_MILLIS,
								BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
						LOGGER.info("Wait for three minutes to dcmrfc.log get update");
						tapEnv.waitTill(BroadBandTestConstants.THREE_MINUTE_IN_MILLIS);
					}
				} else {
					throw new TestException("Error faced while copying dcm properties file from etc to nvram folder");
				}
			} else {
				throw new TestException(featureName + " not enabled/disbaled in XCONF");
			}
			response = CommonUtils.searchLogFilesWithPollingInterval(tapEnv, device,
					BroadBandTraceConstants.LOG_MESSAGE_COMPLETED_RFC_PASS, BroadBandCommandConstants.FILE_DCMRFC_LOG,
					BroadBandTestConstants.TEN_MINUTE_IN_MILLIS, BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
			if (CommonMethods.isNotNull(response)) {
				LOGGER.info("Response from dcmrfc log file is " + response);
				CommonUtils.patternSearchFromTargetString(response,
						BroadBandTraceConstants.LOG_MESSAGE_COMPLETED_RFC_PASS);
			}

			status = BroadBandCommonUtils.rebootViaWebpaAndWaitForStbAccessible(device, tapEnv);

		} catch (Exception exception) {
			errorMessage = exception.getMessage();
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}
		LOGGER.debug("ENDING METHOD: BroadBandRfcFeatureControlUtils.rfcFeatureWithSingleRebootRetrievewNow");
		return status;
	}

	/**
	 * Helper method to remove the opt override for dcm.properties
	 * 
	 * @param device {@link Dut}
	 * @param tapApi {@link AutomaticsTapApi}
	 * @return true if opt override is removed and box has come up with AV after
	 *         reboot, else false
	 */
	public static boolean removeNvramOverrideForRfc(Dut device, AutomaticsTapApi tapApi) {

		LOGGER.debug("STARTING METHOD: BroadBandRfcFeatureControlUtils.removeOptOverrideForRfc");
		// status
		boolean status = false;

		if (CommonUtils.isFileExists(device, tapApi, ETC_RFC_PROPERTIES)) {
			LOGGER.info("Removing nvram override for rfc.properties file and rebooting device");
			// executing the command and retrieving response
			status = CommonUtils.performCreateRemoveUpdateFileOperations(tapApi, device,
					BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_RM_WITH_R_F_OPTION,
							NVRAM_RFC_PROPERTIES));
			status = status ? (!CommonUtils.isFileExists(device, tapApi, NVRAM_RFC_PROPERTIES)) : false;
			if (!status) {
				LOGGER.error("RFC properties file was not removed from /nvram folder");
				return false;
			}
		} else {
			LOGGER.info("Removing nvram override for dcm.properties file and rebooting device");
			// executing the command and retrieving response
			status = CommonUtils.performCreateRemoveUpdateFileOperations(tapApi, device,
					BroadBandTestConstants.CMD_REMOVE_NVRAM_DCM_PROPERTIES);
			status = status
					? (!CommonUtils.isFileExists(device, tapApi,
							BroadBandTestConstants.DCM_PROPERTIES_FILE_NVRAM_FOLDER))
					: false;
			if (!status) {
				LOGGER.error("DCM properties file was not removed from /nvram folder");
				return false;
			}
		}
		// rebooting the STB
		CommonUtils.rebootAndWaitForIpAcquisition(tapApi, device);
		LOGGER.debug("ENDING METHOD: BroadBandRfcFeatureControlUtils.removeOptOverrideForRfc");

		return status;
	}

	/**
	 * Method to validate the RFC config URL in nvram/rfc.properties
	 * 
	 * @param device {@link Dut}
	 * @param tapEnv {@link AutomaticsTapApi}
	 * 
	 * @return true if url retrived is same as that of url in dcmrfc.log
	 */
	public static boolean validateRFCConfigURL(Dut device, AutomaticsTapApi tapEnv) {
		String searchCommand = null;
		String commandOutput = null;
		String dcmServerUrl = null;
		boolean status = false;
		if (CommonUtils.isFileExists(device, tapEnv, BroadBandRfcFeatureControlUtils.NVRAM_RFC_PROPERTIES)) {
			searchCommand = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.CAT_COMMAND,
					BroadBandRfcFeatureControlUtils.NVRAM_RFC_PROPERTIES);
			LOGGER.info("Search Command is: " + searchCommand);
			commandOutput = tapEnv.executeCommandUsingSsh(device, searchCommand);
			if (CommonMethods.isNotNull(commandOutput)) {
				dcmServerUrl = CommonMethods.patternFinder(commandOutput,
						BroadBandRfcFeatureControlUtils.PATTERN_FOR_RFC_CONFIG_SERVER_URL);
				if (CommonMethods.isNotNull(dcmServerUrl)) {
					searchCommand = BroadBandCommonUtils.concatStringUsingStringBuffer(
							BroadBandTestConstants.GREP_COMMAND, dcmServerUrl,
							BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.DCMRFC_LOG_FILE);
					LOGGER.info("Search Command is :" + searchCommand);
					status = CommonUtils.searchLogFiles(tapEnv, device, searchCommand);
				}
			}
		}
		return status;
	}

    /**
     * Method used to enable or disable the forward ssh using RFC
     * 
     * @param tapEnv
     *            The ecats device.
     * @param device
     *            The {@link Dut} object.
     * @param featureName
     *            Name of the feature that has to be enabling/disabling
     * @param enableOrDisableFlag
     *            Flag to set Feature enable or disable
     * @return status True Enable/disable success based on enableOrDisableFlag
     * @refactor Said Hisham
     * 
     */
    public static boolean enableOrDisableForwardSshUsingRfc(AutomaticsTapApi tapEnv, Dut device, String featureName,
	    boolean enableOrDisableFlag) {
	LOGGER.debug("STARTING METHOD: enableOrDisableForwardSshUsingRfc");
	boolean status = false;
	boolean isRfcDataPosted = false;
	// Variable declaration starts
	String proxyXconfUrl = AutomaticsTapApi.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PROXY_XCONF_RFC_URL);
	String response = null;
	BroadBandReverseSshUtils broadBandReverseSshUtilsObject = new BroadBandReverseSshUtils();
	// Variable declaration ends
	try {
	    isRfcDataPosted = BroadBandRfcFeatureControlUtils.enableOrDisableFeatureInProxyXconf(tapEnv, device,
		    featureName, enableOrDisableFlag)
		    && BroadBandRfcFeatureControlUtils.copyRfcPropertiesFromEtcToNVRAM(device, tapEnv)
		    && BroadBandRfcFeatureControlUtils.copyAndUpdateRfcPropertiesNewXconfUrl(device, tapEnv,
			    proxyXconfUrl);
	    tapEnv.executeCommandUsingSsh(device, CMD_CLEAR_HASH_VALUE);
	    if (isRfcDataPosted) {
		tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_CLEAR_DCMRFC_LOG);
		BroadBandWebPaUtils.setVerifyWebPAInPolledDuration(device, tapEnv,
			BroadBandWebPaConstants.WEBPA_PARAM_RFC_CONTROL, BroadBandTestConstants.CONSTANT_2,
			BroadBandTestConstants.STRING_CONSTANT_1, BroadBandTestConstants.ONE_MINUTE_IN_MILLIS,
			BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		LOGGER.info("Wait for three minutes to dcmrfc.log get update");
		tapEnv.waitTill(BroadBandTestConstants.THREE_MINUTE_IN_MILLIS);
		LOGGER.info("Performing the Device Reboot");
		status = (enableOrDisableFlag ? CommonMethods.rebootAndWaitForIpAccusition(device, tapEnv)
			: broadBandReverseSshUtilsObject.rebootWithoutWaitAndGetTheRebootStatusUsingReverseSsh(tapEnv,
				device))
			&& BroadBandWebPaUtils.verifyWebPaProcessIsUp(tapEnv, device, true);
		LOGGER.info("Forward ssh " + (enableOrDisableFlag ? "Disabled " : "Enabled ") + "Successfully");
		if (status) {
		    if (enableOrDisableFlag) {
			LOGGER.info("Verifying 'COMPLETED RFC PASS' using Forward SSH");
			response = CommonUtils.searchLogFilesWithPollingInterval(tapEnv, device,
				BroadBandTraceConstants.LOG_MESSAGE_COMPLETED_RFC_PASS,
				BroadBandCommandConstants.FILE_DCMRFC_LOG,
				BroadBandTestConstants.FIFTEEN_MINUTES_IN_MILLIS,
				BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		    } else {
			LOGGER.info("Verifying 'COMPLETED RFC PASS' using Revrse SSH");
			response = BroadBandReverseSshUtils.searchLogFilesWithPollingIntervalUsingReverseSsh(tapEnv,
				device, BroadBandTraceConstants.LOG_MESSAGE_COMPLETED_RFC_PASS,
				BroadBandCommandConstants.FILE_DCMRFC_LOG,
				BroadBandTestConstants.FIFTEEN_MINUTES_IN_MILLIS,
				BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		    }
		    LOGGER.info("Response from RFC log :" + response);
		    status = CommonMethods.isNotNull(response) && CommonUtils.patternSearchFromTargetString(response,
			    BroadBandTraceConstants.LOG_MESSAGE_COMPLETED_RFC_PASS);
		}
	    } else {
		throw new TestException("Error faced while copying dcm properties file from etc to nvram folder Or "
			+ featureName + " not enabled/disbaled in XCONF");
	    }
	} catch (Exception exception) {
	    errorMessage = exception.getMessage();
	    LOGGER.error(errorMessage);
	    throw new TestException(errorMessage);
	}
	LOGGER.debug("ENDING METHOD: enableOrDisableForwardSshUsingRfc");
	return status;
    }  
    
    /**
     * Helper method to verify whether the feature logs are available in dcmrfc.log
     * 
     * @param device
     *            {@link Dut}
     * @param tapApi
     *            {@link AutomaticsTapApi}
     * 
     * @return string searched for given parameter and value in dcmrfc.log
     */
    public static String searchFeatureLogsInDcmRfcLog(Dut device, AutomaticsTapApi tapApi, String parameter,
	    String value) {

	LOGGER.debug("STARTING METHOD: BroadBandRfcFeatureControlUtils.searchFeatureLogsInDcmRfcLog");
	// stores the command response
	String response = null;
	long startTime = System.currentTimeMillis();

	do {
	    response = tapApi.executeCommandUsingSsh(device,
		    BroadBandCommandConstants.CMD_GREP_RFC_LOGS_FOR_PARAM
			    .replaceAll(BroadBandTestConstants.STRING_REPLACE, parameter)
			    .replaceAll(BroadBandTestConstants.TELEMTRY_MARKER_SET_VALUE, value));
	    if (CommonMethods.isNotNull(response)
		    // Polling until we get 5th line of RFC processing to grep complete logs
		    && CommonMethods.patternMatcher(response,
			    BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.RFC_LOG_PATTERN,
				    parameter, BroadBandTestConstants.WITH_VALUE, value))) {
		break;
	    }

	} while (System.currentTimeMillis() - startTime < BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS
		&& BroadBandCommonUtils.hasWaitForDuration(tapApi, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));

	LOGGER.debug("ENDING METHOD: BroadBandRfcFeatureControlUtils.searchFeatureLogsInDcmRfcLog");
	return response;
    }
    
    /**
     * Method to POST the accountID in Proxy server
     * 
     * @param device
     *            {@link Dut}
     * @param accountID
     *            account ID
     * @param tapEnv
     * 
     * @return true if successfully set
     * @refactor Said hisham
     */
    public static boolean setAccountIDInProxyXconf(Dut device, String accountID, AutomaticsTapApi tapEnv) {
	LOGGER.debug("STARTING METHOD: setAccountIDInProxyXconf");
	boolean result = false;
	String rfcPayLoadData = null;
	String stbMacAddress = device.getHostMacAddress();
	LOGGER.info("Mac address: " + stbMacAddress);
	String payload = BroadBandTestConstants.STRING_RFC_DATA_GENERIC_PAYLOAD;
	JSONObject jsonObj = null;
	try {
	    jsonObj = new JSONObject();
	    payload = payload.replace(BroadBandTestConstants.CONSTANT_REPLACE_STBMAC_LSAPAYLOADDATA, stbMacAddress);
	    payload = payload.replace(BroadBandTestConstants.STRING_PAYLOAD_REPLACE, "AccountId");
	    LOGGER.info("PAY LOAD DATA BEFORE REPLACE: " + payload);
	    jsonObj.put(CommonMethods.concatStringUsingStringBuffer(BroadBandTestConstants.TR181_DOT,
		    BroadBandWebPaConstants.TR181_PARAM_TO_GET_ACCOUNT_ID), accountID);
	    if (null != jsonObj && CommonMethods.isNotNull(stbMacAddress)) {
		rfcPayLoadData = payload.replace(BroadBandTestConstants.STRING_REPLACE, jsonObj.toString());
		LOGGER.info("PAY LOAD DATA AFTER REPLACE: " + rfcPayLoadData);
		int responseCode = BroadBandRfcFeatureControlUtils.postDataToProxyXconfDcmServer(device, tapEnv,
			rfcPayLoadData);
		LOGGER.info("RESPONSE CODE: " + responseCode);
		if (responseCode == BroadBandTestConstants.CONSTANT_200) {
		    result = true;
		}
	    } else {
		LOGGER.error("Mac address is not obtained");
	    }
	} catch (JSONException e) {
	    LOGGER.error("Exception while Posting RFC configuration");
	    result = false;
	}
	LOGGER.info("Status of setting accountID in xconf: " + result);
	LOGGER.debug("ENDING METHOD: setAccountIDInProxyXconf");
	return result;
    }
}

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
	 * @param settop      {@link Settop}
	 * @param tapApi      {@link ECatsTapApi}
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
			LOGGER.info("XB Mac address: " + stbMacAddress);
			if (CommonMethods.isNotNull(stbMacAddress)) {
				rfcSettings = rfcSettings.replace(BroadBandTestConstants.CONSTANT_REPLACE_STBMAC_LSAPAYLOADDATA,
						stbMacAddress);
				LOGGER.info("PAY LOAD DATA: " + rfcSettings);
				responseCode = postDataToProxyXconfDcmServer(device, tapEnv, rfcSettings);
				LOGGER.info("RESPONSE CODE: " + responseCode);
				if (responseCode == RDKBTestConstants.CONSTANT_200) {
					LOGGER.info("POST request for rfc settings is successful");
					clearDCMRFClog(device, tapEnv);
					status = true;
				} else {
					throw new TestException("POST request failed!! Proxy server returning " + responseCode);
				}
			} else {
				throw new TestException("XB Mac address is not obtained");
			}
			LOGGER.debug("ENDING METHOD: BroadBandRfcFeatureControlUtils.executePreconditionForRfcTests");
			return status;
		} catch (Exception e) {
			LOGGER.error("Exception occured during RFC test precondition");
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + e.getMessage());
		}
	}

}

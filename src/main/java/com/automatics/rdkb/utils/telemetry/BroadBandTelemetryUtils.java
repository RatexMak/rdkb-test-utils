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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Device;
import com.automatics.device.Dut;
import com.automatics.error.GeneralError;
import com.automatics.exceptions.FailedTransitionException;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandPropertyKeyConstants;
import com.automatics.rdkb.constants.BroadBandTelemetryConstants;
import com.automatics.rdkb.constants.BroadBandTelemetryConstants.TelemetryData;
import com.automatics.rdkb.constants.BroadBandTelemetryConstants.XConfQueryParams;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;
import com.automatics.rdkb.utils.CommonUtils;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.rdkb.utils.SplunkUtils;
import com.automatics.rdkb.utils.dmcli.DmcliUtils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.restclient.RestClientException;
import com.automatics.restclient.RestEasyClientImpl;
import com.automatics.restclient.RestRequest;
import com.automatics.restclient.RestResponse;
import com.automatics.restclient.RestClientConstants.HttpRequestMethod;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.CommonMethods;

/**
 * Utility class that handles all methods related to Telemetry services
 * 
 * @author susheela
 * @author divya
 * @refactor Govardhan
 */
public class BroadBandTelemetryUtils {
	/** SLF4J logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandTelemetryUtils.class);

	/** http protocol **/
	private static final String HTTP_PROTOCOL = "http";

	/**
	 * keyword to be appended for completing the proxy xconf url for updating dcm
	 * settinsg
	 **/
	private static String PROXY_DCM_XCONF_UPDATE_SETTINGS_KEYWORD = "updateSettings";

	/** Cron interval should be 15 minutes **/
	public static String SCHEDULE_CRON_JOB_TIME_FOR_TELEMETRY = "*/15 * * * *";

	/** Telemetry upload URL **/
	private static String TELEMTRY_UPLOAD_URL = BroadbandPropertyFileHandler.getTelemetryUploadUrl();

	/**
	 * keyword to be appended for completing the proxy xconf url for retireving dcm
	 * settinsg
	 **/
	private static String PROXY_DCM_XCONF_GET_SETTINGS_KEYWORD = "getSettings";

	private static final int CONNECTION_TIMEOUT = 60000;

	/** Constant to hold response from dcmScript log */
	private static String responseDcmScriptLog = null;

	/** Error Message text */
	private static String errorMessage = null;

	/**
	 * Utility Method to remove the Telemetry Configuration Files.
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @return Boolean representing the result of the clearing telemetry
	 *         configuration files.
	 * @refactor Govardhan
	 */
	public static boolean clearTelemetryConfiguration(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("STARTING METHOD clearTelemetryConfiguration");
		boolean removeNvramDcmSettings = CommonUtils.deleteFile(device, tapEnv,
				BroadBandCommandConstants.FILE_NVRAM_DCMSETTINGS_CONF);
		LOGGER.info("NVRAM DCM SETTINGS REMOVED: " + removeNvramDcmSettings);
		boolean removeTmpDcmSettings = CommonUtils.deleteFile(device, tapEnv,
				BroadBandCommandConstants.FILE_TMP_DCMSETTINGS_CONF);
		LOGGER.info("TMP DCM SETTINGS REMOVED: " + removeTmpDcmSettings);
		boolean removeDcmProperties = CommonUtils.deleteFile(device, tapEnv,
				BroadBandTestConstants.DCM_PROPERTIES_FILE_NVRAM_FOLDER);
		LOGGER.info("NVRAM DCM PROPERTIES REMOVED: " + removeDcmProperties);
		boolean result = removeNvramDcmSettings && removeTmpDcmSettings && removeDcmProperties;
		LOGGER.info("TELEMETRY CONFIGURATIONS CLEARED: " + result);
		LOGGER.debug("ENDING METHOD clearTelemetryConfiguration");
		return result;
	}

	/**
	 * 
	 * Helper method to copy and to update the dcm.properties with the XCONF update
	 * url
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @return status 0 box doesn't contain /etc/dcm.properties file 1 XCONF URL
	 *         Updated Successfully 3 Failed to update the LOG UPLOAD XCONF URL
	 * @refactor Govardhan
	 */
	public static int copyAndUpdateDcmProperties(Dut device, AutomaticsTapApi tapEnv) {

		LOGGER.info("STARTING METHOD: TelemetryUtils.copyAndUpdateDcmProperties");

		int status = 0;
		// Creating the proxy xconf getSettings url by appending the two strings
		String proxyDcmGetSettingsUrl = AutomaticsPropertyUtility
				.getProperty(RDKBTestConstants.PROP_KEY_PROXY_XCONF_URL) + PROXY_DCM_XCONF_GET_SETTINGS_KEYWORD;

		// checking the contents of /nvram and verifying whether dcm.properties
		// folder is present
		String command = BroadBandTelemetryConstants.CMD_LIST_DCM_PROPERTIES_ETC_FOLDER;
		String response = null;
		response = tapEnv.executeCommandUsingSsh(device, command);

		if (CommonMethods.isNotNull(response) && response.contains("dcm.properties")) {

			LOGGER.info("The box contains dcm.properties!!!!!");
			// copying the dcm.properties file from /etc/ to /nvram
			command = BroadBandTestConstants.CMD_CP_DCM_PROPERTIES;
			response = tapEnv.executeCommandUsingSsh(device, command);

			// Replacing the 'DCM_LOG_SERVER_URL' in the dcm.properties with
			// XCONF url
			command = BroadBandTestConstants.CMD_CAT_DCM_PROPERTIES;
			response = tapEnv.executeCommandUsingSsh(device, command);
			String logUploadUrl = CommonMethods.patternFinder(response, RDKBTestConstants.PATTERN_FOR_LOG_UPLOAD_URL);

			LOGGER.info("Replacing the url using 'sed' command..........");
			// replacing using 'sed' command
			String cmdForReplaceUrl = "sed -i 's#" + logUploadUrl.trim() + "#" + proxyDcmGetSettingsUrl + "#g' "
					+ BroadBandTestConstants.DCM_PROPERTIES_FILE_NVRAM_FOLDER;

			response = tapEnv.executeCommandUsingSsh(device, cmdForReplaceUrl);

			// Checking whether LOG_UPLOAD_URL is replaced with sed
			LOGGER.info("Checking whether LOG_UPLOAD_URL is replaced with sed...........");
			command = BroadBandTestConstants.CMD_CAT_DCM_PROPERTIES;
			response = tapEnv.executeCommandUsingSsh(device, command);

			// to alert the execution regarding various outcomes of this test
			if (CommonMethods.isNotNull(response) && response.contains(proxyDcmGetSettingsUrl)) {
				LOGGER.info("********Succesfully updated the url********");
				status = 1;
			} else {
				LOGGER.error("Failed to update the LOG UPLOAD url!!!!");
				status = 2;
			}
		} else {
			LOGGER.error("The box doesn't contain /etc/dcm.properties file!!!! Exiting Test........");
		}
		LOGGER.info("ENDING METHOD: TelemetryUtils.copyAndUpdateDcmProperties");
		return status;
	}

	/**
	 * Helper Method to create the basic telemetry settings JSON Object.
	 * 
	 * @return JSONObject representing the Basic Telemetry Settings.
	 * @refactor Govardhan
	 */
	private static JSONObject createBasicTelemetrySettingsJson() {
		LOGGER.debug("STARTING METHOD: TelemetryUtils.createBasicTelemetrySettingsJson");
		JSONObject telemetrySettings = null;
		try {
			// Create Telemetry Setting JSON Object.
			telemetrySettings = new JSONObject();
			telemetrySettings.put("scheduleCron", SCHEDULE_CRON_JOB_TIME_FOR_TELEMETRY);
			telemetrySettings.put("uploadUrl", TELEMTRY_UPLOAD_URL);
			telemetrySettings.put("uploadProtocol", HTTP_PROTOCOL.toUpperCase());
		} catch (JSONException jsonException) {
			// Log & Suppress the Exception.
			LOGGER.error(
					"EXCEPTION OCCURRED WHILE CREATING BASIC TELEMETRY SETTINGS JSON: " + jsonException.toString());
		}
		LOGGER.debug("ENDING METHOD: TelemetryUtils.createBasicTelemetrySettingsJson");
		return telemetrySettings;
	}

	/**
	 * Helper Method to create the JSON Parameter to be posted to the Proxy DCM
	 * Server for Invalid Ping Server Telemetry Marker Configuration.
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @return String representing the JSON Object to be posted to the Proxy DCM
	 *         Server.
	 * @refactor Govardhan
	 */
	public static String getInvalidPingServerConfigJsonToPostToDcmServer(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("STARTING METHOD: TelemetryUtils.getGwConfigJsonToPostToDcmServer");
		String postDataProxyDcmServer = null;
		JSONObject dcmSettings = null;
		try {
			// Create Telemetry Setting JSON Object.
			JSONObject telemetrySettings = createBasicTelemetrySettingsJson();
			JSONArray telemetryProfileJson = new JSONArray();
			JSONObject tmpJsonObject = new JSONObject();
			tmpJsonObject.put(BroadBandTestConstants.JSON_ELEMENT_HEADER,
					BroadBandTraceConstants.TELEMETRY_MARKER_FOR_PING_SERVER_FAILURE);
			tmpJsonObject.put(BroadBandTestConstants.JSON_ELEMENT_CONTENT,
					BroadBandTraceConstants.TELEMETRY_MARKER_FOR_PING_SERVER_FAILURE);
			tmpJsonObject.put(BroadBandTestConstants.JSON_ELEMENT_TYPE,
					BroadBandCommandConstants.FILE_SELFHEAL_LOG_TXT_0);
			tmpJsonObject.put(BroadBandTestConstants.JSON_ELEMENT_POLLING_FREQUENCY, 900);
			telemetryProfileJson.put(tmpJsonObject);

			telemetrySettings.put("telemetryProfile", telemetryProfileJson);
			// Create DCM Setting & add Telemetry Setting to the same.
			dcmSettings = new JSONObject();
			String stbMacAddress = device.getHostMacAddress();
			LOGGER.info("ESTB MAC ADDRESS: " + stbMacAddress);
			dcmSettings.put(BroadBandTestConstants.JSON_ELEMENT_ESTB_MAC_ADDRESS, stbMacAddress);
			String profileName = BroadBandTestConstants.PROFILE_NAME_PING
					+ CommonUtils.getDeviceMacAddressWithColonReplaced(device, BroadBandTestConstants.EMPTY_STRING);
			dcmSettings.put(BroadBandTestConstants.JSON_ELEMENT_TELEMETRY_PROFILE_NAME, profileName);
			dcmSettings.put(BroadBandTestConstants.JSON_ELEMENT_TELEMETRY_SETTINGS, telemetrySettings);
		} catch (JSONException jsonException) {
			// Log & Suppress the Exception.
			LOGGER.error(
					"EXCEPTION OCCURRED WHILE CREATING JSON TO POST TO PROXY DCM SERVER: " + jsonException.toString());
		}
		if (null != dcmSettings) {
			postDataProxyDcmServer = dcmSettings.toString();
		}
		LOGGER.info("DATA TO BE POSTED TO THE PROXY DCM SERVER: " + postDataProxyDcmServer);
		LOGGER.debug("ENDING METHOD: TelemetryUtils.getGwConfigJsonToPostToDcmServer");
		return postDataProxyDcmServer;
	}

	/**
	 * Helper Method to configure the Telemetry Profile for Invalid Ping Servers
	 * Configuration Telemetry Markers. It clears the existing telemetry
	 * configuration files on the device. Then copies the dcm.properties from /etc
	 * to /nvram directory & updates with the appropriate DCM Log Server URL. Then
	 * configure the XCONF server with required telemetry markers for the device.
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @return Boolean representing the result of Invalid Ping Servers Configuration
	 *         Telemetry Profile Configuration.
	 * @refactor Govardhan
	 */
	public static boolean configureTelemetryProfileNwConnectivty(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("STARTING METHOD configureTelemetryProfileNwConnectivty");
		String proxyXconfDcmUrl = null;
		boolean result = clearTelemetryConfiguration(tapEnv, device);
		LOGGER.info("CLEARED EXISTING TELEMETRY/ DCM CONFIGURATION FILES: " + result);
		if (result) {
			proxyXconfDcmUrl = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_PROXY_XCONF_URL);
			LOGGER.info("XCONF DCM URL FROM PROPERTIES: " + proxyXconfDcmUrl);
			result = CommonMethods.isNotNull(proxyXconfDcmUrl);
		}
		LOGGER.info("XCONF DCM URL AVAILABLE: " + result);
		if (result) {
			int iResult = copyAndUpdateDcmProperties(device, tapEnv);
			result = BroadBandTestConstants.CONSTANT_1 == iResult;
		}
		LOGGER.info("DCM PROPERTIES COPIED & UPDATED WITH APPROPRIATE URL: " + result);
		if (result) {
			String postJsonDataProxyDcmServer = getInvalidPingServerConfigJsonToPostToDcmServer(tapEnv, device);
			if (CommonMethods.isNotNull(postJsonDataProxyDcmServer)) {
				proxyXconfDcmUrl = BroadBandCommonUtils.concatStringUsingStringBuffer(proxyXconfDcmUrl,
						PROXY_DCM_XCONF_UPDATE_SETTINGS_KEYWORD);
				result = BroadBandCommonUtils.postDataTlsEnabledClient(tapEnv, device, proxyXconfDcmUrl,
						postJsonDataProxyDcmServer);
			}
			LOGGER.info("NETWORK CONNECTIVITY CONFIG TELEMETRY POST TO DCM SERVER: " + result);
		}
		LOGGER.info("NETWORK CONNECTIVITY CONFIG TELEMETRY PROFILE: " + result);
		LOGGER.debug("ENDING METHOD configureTelemetryProfileNwConnectivty");
		return result;
	}

	/**
	 * Utility Method to get the Payload data from the /rdklogs/logs/dcmscript.log.
	 * The required log message is searched with the passed command. Then the
	 * payload data is extracted using the pattern matcher.
	 * 
	 * @param tapEnv      {@link AutomaticsTapApi}
	 * @param device      {@link Dut}
	 * @param searchText  String representing the Search Text (Typically a Payload
	 *                    Parameter) to be fired on the dcm script log.
	 * @param pollingFlag Boolean representing whether Polling needs to be performed
	 *                    while trying to search the text in dcm script log file.
	 * 
	 * @return JSONObject representing the Payload Data.
	 * @refactor Govardhan
	 */
	public static JSONObject getPayLoadDataAsJson(AutomaticsTapApi tapEnv, Dut device, String searchText,
			boolean pollingFlag) {
		LOGGER.debug("ENTERING METHOD getPayLoadDataAsJson");
		boolean result = false;
		JSONObject telemetryPayLoadData = null;
		String searchResponse = null;
		long pollDuration = BroadBandTestConstants.SIXTEEN_MINUTES_IN_MILLIS;
		long startTime = System.currentTimeMillis();
		do {
			LOGGER.info("GOING TO WAIT FOR 1 MINUTE.");
			tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
			searchResponse = BroadBandCommonUtils.searchLogFiles(tapEnv, device, searchText,
					BroadBandTestConstants.DCMSCRIPT_LOG_FILE);
			result = CommonMethods.isNotNull(searchResponse);
		} while ((System.currentTimeMillis() - startTime) < pollDuration && !result && pollingFlag);
		LOGGER.info("SEARCH RESPONSE: " + searchResponse);
		String strPayLoadData = result
				? CommonMethods.patternFinder(searchResponse, BroadBandTestConstants.PATTERN_MATCHER_PAYLOAD_DATA)
				: null;
		LOGGER.info("PAYLOAD DATA (STRING): " + strPayLoadData);
		if (CommonMethods.isNotNull(strPayLoadData)) {
			try {
				telemetryPayLoadData = new JSONObject(strPayLoadData);
			} catch (JSONException jsonException) {
				LOGGER.error(jsonException.getMessage());
			}
		}
		LOGGER.info("PAYLOAD DATA (JSON): " + telemetryPayLoadData);
		LOGGER.debug("ENDING METHOD getPayLoadDataAsJson");
		return telemetryPayLoadData;
	}

	/**
	 * Utility Method to retrieve the value of the payload parameter given the
	 * payload data in JSON Format & parameter name.
	 * 
	 * @param telemetryPayloadData String representing the Telemetry Payload Data
	 * @param parameterName        String representing the parameter name
	 * 
	 * @return String representing the parameter value.
	 * @refactor Govardhan
	 */
	public static String getPayloadParameterValue(JSONObject telemetryPayloadData, String parameterName) {
		LOGGER.debug("ENTERING METHOD getPayloadParameterValue");
		String parameterValue = null;
		JSONArray jsonArrSearchResult = null;
		try {
			if (null != telemetryPayloadData) {
				jsonArrSearchResult = telemetryPayloadData.getJSONArray("searchResult");
			}
			if (null != jsonArrSearchResult) {
				LOGGER.info("searchResult JSON ARRAY LENGTH: " + jsonArrSearchResult.length());
				for (int iCounter = 0; iCounter < jsonArrSearchResult.length(); iCounter++) {
					JSONObject tmpJsonObject = jsonArrSearchResult.getJSONObject(iCounter);
					boolean isKeyPresent = tmpJsonObject.has(parameterName);
					parameterValue = isKeyPresent ? tmpJsonObject.getString(parameterName) : null;
					if (isKeyPresent) {
						LOGGER.info("PARAMETER PRESENT IN THE TELEMETRY PAYLOAD: " + isKeyPresent);
						break;
					}
				}
			}
		} catch (JSONException jsonException) {
			LOGGER.error(jsonException.getMessage());
		}
		LOGGER.info("TELEMETRY PAYLOAD PARAMETER: " + parameterName + ", VALUE: " + parameterValue);
		LOGGER.debug("ENDING METHOD getPayloadParameterValue");
		return parameterValue;
	}

	/**
	 * Utility Method to verify the ping failed 4 and ping failed 2.
	 * 
	 * @param tapEnv     {@link AutomaticsTapApi}
	 * @param device     {@link Dut}
	 * @param searchText String representing the Search Text (Typically a Payload
	 *                   Parameter) to be fired on the dcm script log.
	 * 
	 * @return status .True-Expected log is present.Else-False
	 * @refactor Govardhan
	 */
	public static boolean verifyPingFailedPayloadParamFromDcmScript(AutomaticsTapApi tapEnv, Dut device,
			String searchText) {
		LOGGER.debug("STARTING METHOD : verifyPingFailedPayloadParamFromDcmScript");
		boolean status = false;
		String searchResponse = null;
		try {
			long startTime = System.currentTimeMillis();
			do {
				searchResponse = BroadBandCommonUtils.searchLogFiles(tapEnv, device, searchText,
						BroadBandTestConstants.DCMSCRIPT_LOG_FILE);
				status = CommonMethods.isNotNull(searchResponse)
						&& CommonMethods.patternMatcher(searchResponse, BroadBandTestConstants.PATTERN_PING_FAILED_FOUR)
						&& CommonMethods.patternMatcher(searchResponse, BroadBandTestConstants.PATTERN_PING_FAILED_TWO);
			} while (!status && (System.currentTimeMillis() - startTime) < BroadBandTestConstants.TWO_MINUTE_IN_MILLIS
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
			LOGGER.info("SEARCH RESPONSE STATUS: " + status);
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage());
		}
		LOGGER.debug("ENDING METHOD : verifyPingFailedPayloadParamFromDcmScript");
		return status;
	}

	/**
	 * Helper method to post dcm data to proxy server for updating dcm properties
	 * 
	 * @param device {@link Dut}
	 * @param tapApi {@link AutomaticsTapApi}
	 * @return http post - server response
	 * @refactor yamini.s
	 */
	public static int postDataToProxyDcmServer(Dut device, AutomaticsTapApi tapApi, boolean isLogUploadSettingsRequired,
			boolean basicCustomScenario) throws TestException {

		LOGGER.debug("STARTING METHOD : TelemetryUtils.postDataToProxyDcmServer");

		LOGGER.info("Posting data to proxy dcm server with updated dcm settings...");
		JSONObject dcmSettings = getJsonParamForUpdatingProxyDcmServer(device, tapApi, isLogUploadSettingsRequired,
				basicCustomScenario);
		// Appending the updateSettings keyword to proxy Xconf url so as to post
		// our telemetry data to the server
		String proxyDcmServerUpdateUrl = BroadbandPropertyFileHandler.getProxyXconfUrl()
				+ PROXY_DCM_XCONF_UPDATE_SETTINGS_KEYWORD;

		// For storing request header
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");

		int httpResponseStatusCode = 0;

		RestEasyClientImpl restClient = new RestEasyClientImpl();

		RestRequest request = new RestRequest(proxyDcmServerUpdateUrl, HttpRequestMethod.POST, headers);
		request.setTimeoutInMilliSeconds(CONNECTION_TIMEOUT);
		request.setMediaType(MediaType.APPLICATION_JSON_TYPE);
		request.setContent(dcmSettings.toString());

		RestResponse response = null;
		try {
			response = restClient.executeAndGetResponse(request);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		httpResponseStatusCode = response.getResponseCode();

		LOGGER.info("HTTP POST response is : " + httpResponseStatusCode);
		LOGGER.debug("ENDING METHOD : TelemetryUtils.postDataToProxyDcmServer");
		return httpResponseStatusCode;
	}

	/**
	 * Helper method to get the JSON param for posting DATA to the poroxy DCM server
	 * for updating dcm stelemetry settings
	 * 
	 * @param device {@link Dut}
	 * @param tapApi {@link AutomaticsTapApi}
	 * @return Json object containing the dcm settings
	 * @refactor yamini.s
	 */
	public static JSONObject getJsonParamForUpdatingProxyDcmServer(Dut device, AutomaticsTapApi tapApi,
			boolean isLogUploadSettingsRequired, boolean basicCustomScenario) {

		LOGGER.debug("STARTING METHOD : TelemetryUtils.getJsonParamForUpdatingProxyDcmServer");
		JSONObject dcmSettings = new JSONObject();
		// VALIDATE IF ECM MAC is returned for RDKB device
		String stbMacAddress = device.getHostMacAddress();
		// adding the stb mac address, log upload settings and telemetry
		// settings
		try {
			/*
			 * Adding the telemetry settings and log upload settings based on requirements
			 */
			dcmSettings.put("estbMacAddress", stbMacAddress);
			if (isLogUploadSettingsRequired) {

				dcmSettings.put("logUploadSettings", getLogUploadSettingsForUpdatingProxyDcmServer(device, tapApi));
			} else {
				dcmSettings.put("telemetrySettings",
						getTelemetrySettingsForUpdatingProxyDcmServer(device, tapApi, basicCustomScenario));
			}

		} catch (JSONException e) {

			e.printStackTrace();
		}
		LOGGER.info("DCM Settings: " + dcmSettings.toString());
		LOGGER.debug("ENDING METHOD : TelemetryUtils.getJsonParamForUpdatingProxyDcmServer");
		return dcmSettings;
	}

	/**
	 * Helper method to get the loag upload settings to update dcm settings in dcm
	 * proxy server
	 * 
	 * @param device {@link Dut}
	 * @param tapApi {@link AutomaticsTapApi}
	 * 
	 * @return Json object containing the log upload settings for updation
	 * @refactor yamini.s
	 */
	public static JSONObject getLogUploadSettingsForUpdatingProxyDcmServer(Dut device, AutomaticsTapApi tapApi) {

		LOGGER.debug("STARTING METHOD : TelemetryUtils.getLogUploadSettingsForUpdatingProxyDcmServer");

		String propValue = BroadbandPropertyFileHandler.getPropKeyForLogUploadSettings();
		JSONObject logUploadSettings = null;
		try {
			logUploadSettings = new JSONObject(propValue);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		LOGGER.debug("ENDING METHOD : TelemetryUtils.getLogUploadSettingsForUpdatingProxyDcmServer");
		return logUploadSettings;
	}

	/**
	 * helper method to get the telemetry settings to update dcm settings in dcm
	 * proxy server
	 * 
	 * @param device {@link Dut}
	 * @param tapApi {@link AutomaticsTapApi}
	 * @return Json object containing the telemetry settings for updation
	 * @refactor yamini.s
	 */
	public static JSONObject getTelemetrySettingsForUpdatingProxyDcmServer(Dut device, AutomaticsTapApi tapApi,
			boolean basicCustomScenario) {

		LOGGER.debug("STARTING METHOD : TelemetryUtils.getTelemetrySettingsForUpdatingProxyDcmServer");
		JSONObject telemetrySettings = new JSONObject();

		/*
		 * Appending the various telemtry setting values including the profiles
		 */
		try {
			telemetrySettings.put("scheduleCron", SCHEDULE_CRON_JOB_TIME_FOR_TELEMETRY);
			telemetrySettings.put("uploadUrl", TELEMTRY_UPLOAD_URL);
			telemetrySettings.put("uploadProtocol", HTTP_PROTOCOL.toUpperCase());
			// adding the telemetry profile to the telemetry settings
			telemetrySettings.put("telemetryProfile",
					getTelemetryProfileForUpdatingProxyDcmServer(device, tapApi, basicCustomScenario));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LOGGER.debug("ENDING METHOD : TelemetryUtils.getTelemetrySettingsForUpdatingProxyDcmServer");
		return telemetrySettings;
	}

	/**
	 * 
	 * Helper method to get the various telemtry profiles to be added in proxy DCM
	 * server
	 * 
	 * @param device {@link Dut}
	 * @param tapApi {@link AutomaticsTapApi}
	 * @return Json array containing the various telemetry profiles
	 * @refactor yamini.s
	 */
	private static JSONArray getTelemetryProfileForUpdatingProxyDcmServer(Dut device, AutomaticsTapApi tapApi,
			boolean basicCustomScenario) {

		LOGGER.debug("STARTING METHOD : TelemetryUtils.getTelemetryProfileForUpdatingProxyDcmServer");

		// getting the profile props value from common.props/xi3.props based on
		// device type
		String propValue = null;

		if (basicCustomScenario) {
			LOGGER.info("Getting profile for Custom Scenario");
			propValue = BroadbandPropertyFileHandler.getPropKeyForBasicTelemetryProfile();
		} else {
			propValue = BroadbandPropertyFileHandler.getPropKeyForTelemetryProfile();
		}

		JSONArray telemetryProfile = new JSONArray();
		try {
			telemetryProfile = new JSONArray(propValue);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LOGGER.debug("ENDING METHOD : TelemetryUtils.getTelemetryProfileForUpdatingProxyDcmServer");
		return telemetryProfile;
	}

	/**
	 * Helper method to reboot the device and cross check with ip after reboot.
	 * Check for reconnecting only till given time period
	 * 
	 * @param tapEnv   AutomaticsTapApi instance
	 * @param device   Dut instance
	 * @param waitTime waitTime for how long to validate
	 * @return true if device is accessbile
	 * @author Praveenkumar Paneerselvam
	 * @refactor yamini.s
	 */
	public static boolean rebootAndWaitForDeviceAccessible(AutomaticsTapApi tapEnv, Dut device, long waitTime) {
		LOGGER.debug("STARTING METHOD : rebootAndWaitForIpAcquisition ");
		boolean status = false;
		// Rebooting the device
		LOGGER.debug("Rebooting the device");
		tapEnv.reboot(device);
		long startTime = System.currentTimeMillis();
		do {
			LOGGER.info("Waiting for device to be accessed");
			// Devices tries to access 5 min in util tapEnv.reboot after reboot. We are just
			// cross checking here.
			status = CommonMethods.isSTBAccessible(device);
		} while (!status && (System.currentTimeMillis() - startTime) < waitTime
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
		LOGGER.debug("ENDING METHOD : rebootAndWaitForIpAcquisition ");
		return status;
	}

	/**
	 * Utility method to get the logupload url from XConf query by grepping
	 * dcmscript.log
	 * 
	 * @param device  The device under test
	 * @param tapEnv  {@link AutomaticsTapApi}
	 * @param propKey The property key for the url for verification
	 * @return The response string after executing the grep command
	 * @throws TestException Throws exception if failed to get the required url
	 * @author divya.rs
	 * @refactor yamini.s
	 */
	public static String verifyXconfDcmConfigurationUrlAndDownloadStatusFromDcmScriptLogs(Dut device,
			AutomaticsTapApi tapEnv, String propKey) throws TestException {
		LOGGER.info("STARTING METHOD : verifyLogUploadUrlInXConfQuery");
		String uploadurl = BroadbandPropertyFileHandler.getProxyXconfUrl();
		String searchUrl = uploadurl;
		boolean dcmUrlStatus = false;
		boolean isTelemetry2Enabled = BroadBandWebPaUtils.getAndVerifyWebpaValueInPolledDuration(device, tapEnv,
				BroadBandWebPaConstants.WEBPA_PARAM_FOR_TELEMETRY_2_0_ENABLE, BroadBandTestConstants.TRUE,
				BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS, BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
		try {
			long startTime = System.currentTimeMillis();
			do {
				searchUrl = isTelemetry2Enabled ? BroadbandPropertyFileHandler.getCIXconfUrlForTelemetry2() : uploadurl;
				LOGGER.info("URL to validate:" + searchUrl);
				responseDcmScriptLog = tapEnv.executeCommandUsingSsh(device,
						isTelemetry2Enabled ? BroadBandTelemetryConstants.CMD_GET_TELEMETRY_REQUEST_DETAILS_FOR_T2
								: BroadBandTelemetryConstants.DCMSCRIPT_LOG_FILE_LOGS_FOLDER);

				LOGGER.info("SearchingForStrings: " + searchUrl + " And "
						+ (isTelemetry2Enabled ? BroadBandTelemetryConstants.CMD_TO_STORE_T2_DOHTTPGET_SUCCESSFULLY
								: BroadBandTelemetryConstants.DCM_CONFIGURATION_DOWNLOAD_STATUS_DCMSCRIPT_LOG));
				if (CommonMethods.isNotNull(responseDcmScriptLog) && !responseDcmScriptLog
						.contains(BroadBandTraceConstants.LOG_MESSAGE_GREP_NO_SUCH_FILE_OR_DIRECTORY)) {
					dcmUrlStatus = responseDcmScriptLog.contains(searchUrl) && responseDcmScriptLog.contains(
							isTelemetry2Enabled ? BroadBandTelemetryConstants.CMD_TO_STORE_T2_DOHTTPGET_SUCCESSFULLY
									: BroadBandTelemetryConstants.DCM_CONFIGURATION_DOWNLOAD_STATUS_DCMSCRIPT_LOG);
					LOGGER.info("dcmUrlStatus:" + dcmUrlStatus);
					/*
					 * Replace default HTTPS URL with HTTP and check whether request URL is present
					 * in dcm script logs. This is is because, most of RDKB devices are not migrated
					 * to HTTPS.
					 */
					if (!dcmUrlStatus) {
						searchUrl = searchUrl.replace("https", "http");
						dcmUrlStatus = responseDcmScriptLog.contains(searchUrl) && responseDcmScriptLog.contains(
								isTelemetry2Enabled ? BroadBandTelemetryConstants.CMD_TO_STORE_T2_DOHTTPGET_SUCCESSFULLY
										: BroadBandTelemetryConstants.DCM_CONFIGURATION_DOWNLOAD_STATUS_DCMSCRIPT_LOG);
						LOGGER.info("dcmUrlStatus after replace:" + dcmUrlStatus);
					}
				}
			} while (!dcmUrlStatus
					&& (System.currentTimeMillis() - startTime) < BroadBandTestConstants.TWENTY_MINUTES_IN_MILLIS
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.ONE_MINUTE_IN_MILLIS));
			if (!dcmUrlStatus) {
				throw new TestException(
						"Failed to get any of the expected strings in response. Expected Strings: 1." + uploadurl
								+ "\n2." + BroadBandTelemetryConstants.DCM_CONFIGURATION_DOWNLOAD_STATUS_DCMSCRIPT_LOG
								+ ";\nActual: " + responseDcmScriptLog);
			}
		} catch (Exception e) {
			LOGGER.error(" Exception occurred : " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : verifyLogUploadUrlInXConfQuery");
		return responseDcmScriptLog;
	}

	/**
	 * Utility method to verify the crin settings in the device.
	 * 
	 * @param device The device under test
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @throws TestException Throws exception if failed to get the cron settings
	 * @author divya.rs
	 * @refactor yamini.s
	 */
	public static boolean verifyDcmCronJobsConfiguredBasedOnDcmSettings(Dut device, AutomaticsTapApi tapEnv,
			String cronSetting) throws TestException {
		LOGGER.debug("STARTING METHOD: verifyCronSettings");
		boolean status = false;
		String response = null;

		String command = BroadBandTelemetryConstants.CMD_CRONTAB_NON_ATOM_SYNC_AVAILABLE;

		if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {

			command = BroadBandTelemetryConstants.CMD_CRONTAB_IS_ATOM_SYNC_AVAILABLE;
			response = BroadBandCommonUtils.executeCommandInAtomConsole(device, tapEnv, command);
		} else {
			response = tapEnv.executeCommandUsingSsh(device, command);
		}

		if (CommonMethods.isNotNull(response) && response.contains(BroadBandTestConstants.DCA_UTILITY_SH)) {
			if (CommonMethods.isNotNull(cronSetting)) {
				status = response.contains(cronSetting);
			} else {
				status = true;
			}
		} else {
			errorMessage = "Device is not configured with required Cron settings mentioned in DCM configuration for uploading telemetry data events at 15 minutes of regular intervals";
		}

		LOGGER.debug("ENDING METHOD: verifyCronSettings");
		if (!status) {
			throw new TestException(errorMessage);
		}
		return status;
	}

	/**
	 * Utility method that configures the telemetry profile for uploading details to
	 * Splunk
	 * 
	 * @param device
	 * 
	 * @author divya.rs
	 * @refcator yamini.s
	 */
	public static void configureTelemetryProfiles(Dut device, AutomaticsTapApi tapEnv, boolean basicScenario)
			throws TestException {
		int status = copyAndUpdateDcmProperties(device, tapEnv);

		boolean configurationStatus = false;

		String errorMessage = null;
		// updating the test case based on the return value from the helper
		// method
		if (0 == status) {
			errorMessage = "The box doesn't contain /etc/dcm.properties file!!!! The test case requires that the box has the file";
		} else if (2 == status) {
			errorMessage = "Successfuly copied /etc/dcm.properties to /nvram folder, But Failed to update the LOG UPLOAD url!!!!";
		} else {

			// sending data to the proxy dcm server before initiating the
			// Advanced Telemety MoCA verification test
			int response = postDataToProxyDcmServer(device, tapEnv, false, basicScenario);
			configurationStatus = (response == 200);
			errorMessage = "Status of posting telemetry profile to proxy DCM Server. Actual: " + response
					+ "Expected: 200";
		}

		if (false == configurationStatus) {
			throw new TestException(errorMessage);
		}

		LOGGER.info("Error Message while configuring Telemetry details : " + errorMessage);
	}

	/**
	 * Utility method to verify Data Pay load in splunk
	 * 
	 * @param tapEnv        {@link AutomaticsTapApi}
	 * @param device        {@link Dut}
	 * @param telemetryData The telemetry data to verify
	 * @return The search result
	 * @refactor Govardhan
	 */
	public static boolean verifyTelemetryDataPayLoadFromSplunk(Dut device, AutomaticsTapApi tapEnv,
			List<String> telemetryData) {

		boolean isDataFound = false;

		// retrieving the value from splunk
		String splunkSearchString = "index=rdk-json(" + device.getHostMacAddress() + ")";
		Collection<String> splunkResponse = SplunkUtils.searchInSplunk(tapEnv, splunkSearchString, null, -1, "20m");
		List<String> markersNotFound = new ArrayList<String>();
		if (null != splunkResponse) {
			Iterator<String> iterator = splunkResponse.iterator();

			while (iterator.hasNext()) {
				String telemetryDataFromSplunk = iterator.next();
				LOGGER.info("########################### DATA from splunk = " + telemetryDataFromSplunk);
				for (String telemetryDataFromPayload : telemetryData) {
					if (CommonMethods.isNotNull(telemetryDataFromSplunk)
							&& telemetryDataFromSplunk.contains(telemetryDataFromPayload)) {
						isDataFound = true;
						LOGGER.info("##################### DATA found");
						break;
					}
				}
				if (!isDataFound) {
					markersNotFound.add(telemetryDataFromSplunk);
				}
			}
			if (markersNotFound != null && markersNotFound.size() > 0) {
				for (String telemetry : markersNotFound) {
					LOGGER.error("Telemetry not found: " + telemetry);
				}
			}
		} else {
			LOGGER.error("Received null response from splunk for search-" + splunkSearchString);
		}
		return isDataFound;
	}

	/**
	 * Method to reset default values of configurable telemetry tag control
	 * parameters
	 * 
	 * @param device {@link Dut}
	 * @param tapApi {@link AutomaticsTapApi}
	 * 
	 * @return true if parameters successfully reset to default values else false
	 * 
	 * @author Ashwin sankara
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean resetDefaultConfigurableTelemetryParameters(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("Entering method: resetDefaultConfigurableTelemetryParameters");
		boolean result = false;
		if (BroadBandWebPaUtils.setAndVerifyParameterValuesUsingWebPaorDmcli(device, tapEnv,
				BroadBandWebPaConstants.WEBPA_PARAM_UNIQUE_TELEMETRY_ENABLE, BroadBandTestConstants.CONSTANT_3,
				BroadBandTestConstants.FALSE)) {
			if (BroadBandWebPaUtils.setAndVerifyParameterValuesUsingWebPaorDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_UNIQUE_TELEMETRY_TIMINGINTERVAL,
					BroadBandTestConstants.CONSTANT_1, BroadBandTestConstants.STRING_ZERO)) {
				if (DmcliUtils.setParameterValueUsingDmcliCommand(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_PARAM_UNIQUE_TELEMETRY_TAGSTRING,
						BroadBandTestConstants.DMCLI_SUFFIX_TO_SET_STRING_PARAMETER,
						BroadBandTestConstants.DMCLI_NULL_VALUE)) {
					result = CommonMethods.isNull(tapEnv.executeWebPaCommand(device,
							BroadBandWebPaConstants.WEBPA_PARAM_UNIQUE_TELEMETRY_TAGSTRING));
				}
			}
		}
		LOGGER.info("Result of configurable telemetry tag parameter values reset: " + result);
		LOGGER.debug("Exiting method: resetDefaultConfigurableTelemetryParameters");
		return result;
	}

	/**
	 * Utility method to get dca counter value from atom console
	 * 
	 * @param device Dut instance
	 * @param tapEnv AutomaticsTapApi instance
	 * @return null if returned value is not integer, else returns Integer value of
	 *         it;
	 * @author Praveenkumar Paneerselvam
	 * @refcator Rakesh C N
	 */
	public static int getDcaCounterValueForTelemetry(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: getDcaCounterValueForTelemetry");
		int dcaCounter = -1;
		String response = null;
		String command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.CAT_COMMAND,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandCommandConstants.FILE_TMP_DCACOUNTER);
		response = BroadBandCommonUtils.executeCommandInAtomConsoleIfAtomIsPresentElseInArm(device, tapEnv, command);
		LOGGER.info("Response in dca counter is : " + response);
		try {
			if (CommonMethods.isNotNull(response)) {
				dcaCounter = Integer
						.parseInt(response.split(BroadBandTestConstants.NEW_LINE_WITH_ESCAPE_CHARACTER)[0].trim());
			}
		} catch (Exception exception) {
			LOGGER.error("DCA COUNTER IS NOT A VALID NUMBER - " + response + " " + exception.getMessage());
		}
		LOGGER.info("DCA counter value is " + dcaCounter);
		LOGGER.debug("ENDING METHOD: getDcaCounterValueForTelemetry");
		return dcaCounter;
	}

	/**
	 * Helper method to execute the commands for adding mock logs for telemetry
	 * throttle testing
	 * 
	 * @param device   {@link Dut}
	 * @param tapApi   {@link AutomaticsTapApi}
	 * @param mocklogs Mocklogs to add in the given file name
	 * @param fileName File path
	 * @return true if commands are executed w/0 any issues, else false
	 * 
	 * @author Praveenkumar Paneerselvam
	 * @refcator Rakesh C N
	 */
	public static boolean executeCommandsForMockingLogsForTelemetryThrottle(Dut device, AutomaticsTapApi tapApi,
			String mockLogs, String fileName) {
		LOGGER.debug("STARTING METHOD: executeCommandsForMockingLogsForTelemetryThrottle");
		boolean status = false;
		String response = null;

		String command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_ECHO_E,
				BroadBandTestConstants.DOUBLE_QUOTE, mockLogs, BroadBandTestConstants.DOUBLE_QUOTE,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.RIGHT_SHIFT_OPERATOR, fileName);
		LOGGER.info("Command to be executed - " + command);
		response = tapApi.executeCommandUsingSsh(device, command);
		status = CommonMethods.isNull(response);
		LOGGER.info("Is the command executed successfully? - " + status);

		LOGGER.debug("ENDING METHOD: executeCommandsForMockingLogsForTelemetryThrottle");
		return status;
	}

	/**
	 * 
	 * Helper method to verify whether telemetry log are getting printed as per the
	 * required throttling
	 * 
	 * @param device          {@link Dut}
	 * @param tapApi          {@link AutomaticsTapApi}
	 * @param expectedPattern Expected telemetry pattern to validate
	 * @param dcaCounter      dca couter value which determines the respective
	 *                        markers to be telemetry uploaded
	 * @return true if logs are getting printed as expected, else false
	 * @author Praveenkumar Paneerselvam
	 * @refcator Rakesh C N
	 */
	public static BroadBandResultObject verifyTelemetryThrottleLogs(Dut device, AutomaticsTapApi tapEnv,
			String expectedPattern) {
		LOGGER.debug("STARTING METHOD: verifyTelemetryThrottleLogs");
		BroadBandResultObject executionStatus = new BroadBandResultObject();
		boolean status = false;
		String response = null;
		String errorMessage = "";
		LOGGER.info("verifyTelemetryThrottleLogs");
		String command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.CMD_GREP_I,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTraceConstants.LOG_MESSAGE_DCA_CURL_CMD,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.DCMSCRIPT_LOG_FILE,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.SYMBOL_PIPE,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.CMD_TAIL_1);
		LOGGER.info("Command to be executed : " + command);
		long startTime = System.currentTimeMillis();
		do {
			try {
				LOGGER.info("Expected Pattern " + expectedPattern);
				response = tapEnv.executeCommandUsingSsh(device, command);
				status = CommonMethods.isNotNull(response) && CommonMethods.patternMatcher(response, expectedPattern);
			} catch (TestException exception) {
				errorMessage = exception.getMessage();
			}
		} while (!status && (System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIFTEEN_MINUTES_IN_MILLIS
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
		if (!status) {
			errorMessage = " Following telemetry profile " + errorMessage + "dint upload as expected ";
		}
		LOGGER.info("Telemetry throttle log upload status is: " + status);
		executionStatus.setStatus(status);
		executionStatus.setErrorMessage(errorMessage);
		LOGGER.debug("ENDING METHOD: verifyTelemetryThrottleLogs");
		return executionStatus;
	}

	/**
	 * Helper method to get cpu usage value.
	 * 
	 * @param device Dut Instance
	 * @param tapEnv tapapi instance
	 * @return cpu usage value in double, returns null if there is an error in
	 *         fetching the value
	 * @author Praveenkumar Paneerselvam
	 * @refcator Rakesh C N
	 */
	public static Double getCpuUsageInPercentage(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: getCpuUsageInPercentage ");
		Double cpuUsage = null;
		String response = tapEnv.executeCommandUsingSsh(device, BroadBandTelemetryConstants.CMD_TOP_TO_GET_CPU_USAGE);
		if (CommonMethods.isNotNull(response)) {
			cpuUsage = parseOutputToGetCpuUsage(response);
		}
		LOGGER.info("CPU usage is - " + cpuUsage);
		LOGGER.debug("ENDING METHOD :getCpuUsageInPercentage ");
		return cpuUsage;
	}

	/**
	 * Utility method to parse the output of top command get the CPU usage
	 * percentage.
	 * 
	 * @param topCommandOutput Top command output.
	 * 
	 * @return CPU usage percentage.
	 * 
	 * @author divya.rs
	 * @refcator Rakesh C N
	 */
	public static double parseOutputToGetCpuUsage(String topCommandOutput) {
		double cpuUsage = 0;
		boolean isCpuUsageFound = false;

		// For line by line parsing.
		String[] splittedTopCommandOutput = topCommandOutput.split("\n");

		for (String lineByLineOutput : splittedTopCommandOutput) {
			String trimedLineOutput = lineByLineOutput.trim();
			LOGGER.debug("Line by line output : " + trimedLineOutput);

			// Skipping empty line and line beginning with hash.
			if (!trimedLineOutput.startsWith(RDKBTestConstants.DELIMITER_HASH)
					&& !RDKBTestConstants.EMPTY_STRING.equals(trimedLineOutput)) {

				try {
					cpuUsage = Double.parseDouble(trimedLineOutput);
					isCpuUsageFound = true;
				} catch (NumberFormatException numExc) {
					LOGGER.error(String.format("Failed to convert %s to double.", trimedLineOutput));
				}
			}
		}

		// If non of the double value found on the output received failure need
		// to be notified.
		if (!isCpuUsageFound) {
			throw new FailedTransitionException(GeneralError.CONSOLE_OUTPUT_COMPARISON_FAILURE,
					"Failed to find the CPU usage detail on the top command output : " + topCommandOutput);
		}
		return cpuUsage;
	}

	/**
	 * Utility method to verify the LOG_SERVER_URL in given property file
	 * 
	 * @param device   The device under test
	 * @param tapEnv   {@link AutomaticsTapApi}
	 * @param filePath The file in the url need to be searched
	 * @param reqdUrl  The expected log server url
	 * @return The status of verification
	 * @throws TestException If any Exception occurs
	 * @author divya.rs
	 * @refactor Athira
	 */
	public static boolean verifyLogServerUrl(Dut device, AutomaticsTapApi tapEnv, String filePath, String reqdUrl)
			throws TestException {
		LOGGER.info("STARTING METHOD: verifyLogServerUrl");
		boolean verificationStatus = false;
		try {
			String cmdToExecute = BroadBandTestConstants.CAT_COMMAND + filePath
					+ BroadBandTelemetryConstants.CMD_GET_DCM_LOG_SERVER_URL;
			String response = tapEnv.executeCommandUsingSsh(device, cmdToExecute);
			LOGGER.info("Response for {} : {}", cmdToExecute, response);
			if (CommonMethods.isNotNull(response) && !response.contains(AutomaticsConstants.NO_SUCH_FILE_OR_DIRECTORY)
					&& response.contains(reqdUrl)) {
				LOGGER.info("Successfully verified the required url as DCM_LOG_SERVER_URL");
				verificationStatus = true;
			} else {
				LOGGER.error("Failed to verify the required URL as the DCM_LOG_SERVER_URL");
			}
		} catch (Exception e) {
			throw new TestException(e.getMessage());
		}

		LOGGER.info("ENDING METHOD: verifyLogServerUrl");
		return verificationStatus;
	}

	/**
	 * Utility method to get the Telemetry Data Pay Load generated from Splunk for
	 * T2
	 * 
	 * @param device The device under test
	 * @param tapEnv {@link AutomaticsTapApi}
	 * 
	 * @return The search string
	 * @refactor Said Hisham
	 */
	public static List<String> getTelemetryDataPayloadGeneratedFromSplunk(Dut device, AutomaticsTapApi tapEnv) {

		List<String> listOfSearchStrings = new ArrayList<String>();
		try {
			long starttime = System.currentTimeMillis();
			boolean status = false;
			String searchResults = null;
			do {
				String splunkSearchString = "index=rdk-json(" + device.getHostMacAddress() + ")";
				Collection<String> splunkResponse = SplunkUtils.searchInSplunk(tapEnv, splunkSearchString, null, -1,
						"20m");
				if (null != splunkResponse) {
					Iterator<String> iterator = splunkResponse.iterator();
					while (iterator.hasNext()) {
						String telemetryDataFromSplunk = iterator.next();
						LOGGER.info("########################### DATA from splunk = " + telemetryDataFromSplunk);
						String telemetryDataFromPayload = BroadBandTelemetryConstants.SEARCH_RESULT;
						LOGGER.info("telemetryDataFromPayload -" + telemetryDataFromPayload);
						if (CommonMethods.isNotNull(telemetryDataFromSplunk)
								&& telemetryDataFromSplunk.contains(telemetryDataFromPayload)) {
							searchResults = CommonMethods.patternFinder(telemetryDataFromSplunk,
									BroadBandTelemetryConstants.SEARCHRESULT_LOG_UPLOAD_TELEMETRY2_MARKER);
							LOGGER.info("searchResults -" + searchResults);
							listOfSearchStrings.add(searchResults);
							status = true;
							LOGGER.info("##################### DATA found");
							break;
						}
					}
				}
			} while (!status
					&& (System.currentTimeMillis() - starttime) < BroadBandTestConstants.TWENTY_MINUTES_IN_MILLIS
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS));
		} catch (Exception e) {
			throw new TestException(
					"Exception occured while retriving the telemetry DATA payload from telemetry2_0.txt.0 ");
		}
		return listOfSearchStrings;
	}

	/**
	 * Utility method to get the Telemetry Data Pay Load generated from
	 * dcmscript.log
	 * 
	 * @param device The device under test
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @return The search string
	 * @refactor Said Hisham
	 */
	public static List<String> getTelemetryDataPayloadGeneratedFromDcmScriptLog(Dut device, AutomaticsTapApi tapEnv) {

		String response = null;
		int totalWaitTime = 0;
		boolean requiredLogFound = false;
		try {
			for (int retry = 0; retry < 25; retry++) {

				response = tapEnv.executeCommandUsingSsh(device,
						BroadBandTelemetryConstants.CMD_GET_TELEMETRY_REQUEST_DETAILS);

				if (DeviceModeHandler.isRPIDevice(device)) {

					if (CommonMethods.isNotNull(response) && response.contains("searchResult")
							&& response.contains("Uploading logs") && response.contains("Direct connection success")) {

						int indexOfSearchResults = response.lastIndexOf("searchResult");
						int indexOfResponseCodeForUploadSuccess = response.lastIndexOf("Direct connection success");

						if (indexOfResponseCodeForUploadSuccess > indexOfSearchResults) {
							requiredLogFound = true;
							break;
						}
					}
				} else {
					if (CommonMethods.isNotNull(response) && response.contains("searchResult")
							&& response.contains("HTTP RESPONSE CODE : 200")) {

						int indexOfSearchResults = response.lastIndexOf("searchResult");
						int indexOfResponseCodeForUploadSuccess = response.lastIndexOf("HTTP RESPONSE CODE : 200");

						if (indexOfResponseCodeForUploadSuccess > indexOfSearchResults) {
							requiredLogFound = true;
							break;
						}
					}
				}

				if (!requiredLogFound) {

					long waitTime = 0;
					if (retry < 5) {
						waitTime = BroadBandTestConstants.THREE_MINUTE_IN_MILLIS;
					} else if (retry >= 5) {
						waitTime = BroadBandTestConstants.ONE_MINUTE_IN_MILLIS;

					}
					totalWaitTime += waitTime;
					LOGGER.info("Waiting for " + (waitTime / (60 * 1000))
							+ " more minutes. Before checking for telemetry log updates. Total wait time is- "
							+ (totalWaitTime / (60 * 1000)) + " minutes ");
					tapEnv.waitTill(waitTime);
				}
			}

			if (!requiredLogFound || CommonMethods.isNull(response)) {
				throw new TestException(
						"Unable to retrive the telemetry DATA payload which is successfully uploaded to Telemetry server from dcmscript.log  even after "
								+ (totalWaitTime / (60 * 1000)) + " minutes of wait time");
			}
		} catch (Exception e) {
			throw new TestException("Exception occured while retriving the telemetry DATA payload from dcmscript.log ");
		}

		List<String> listOfSearchStrings = new ArrayList<String>();
		if (DeviceModeHandler.isRPIDevice(device)) {
			String searchResults1 = null;
			searchResults1 = CommonMethods.patternFinder(response, "[.*}]");
			LOGGER.info("searchResults1 is : " + searchResults1);
			if (CommonMethods.isNotNull(searchResults1)) {
				String[] splitResult = searchResults1.split(",");
				for (String eachValue : splitResult) {
					LOGGER.info("each value :" + eachValue);
					listOfSearchStrings.add(eachValue);
				}
			}
		} else {
			String searchResults = null;
			String[] allResponses = response.split("\\n");
			LOGGER.info("Number of strings: " + allResponses.length);
			for (int lineIndex = 0; lineIndex < allResponses.length; lineIndex++) {
				searchResults = CommonMethods.patternFinder(allResponses[lineIndex],
						"\\'(\\{\"searchResult\".*\\})\\'");
				if (CommonMethods.isNotNull(searchResults)) {
					listOfSearchStrings.add(searchResults);
				}
			}
		}
		if (listOfSearchStrings.isEmpty()) {
			throw new TestException(
					"Unable to retrive the telemetry JSON payload from dcmscript.log  even after 20 minutes of wait time");
		}
		return listOfSearchStrings;
	}

	/**
	 * Utility method to convert the Data Pay load in JSON format
	 * 
	 * @param device The device under test
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @return The {@link JSONObject}
	 * @refactor Said Hisham
	 */
	public static JSONObject getTelemetryDataPayLoadAsJsonFormat(Dut device, AutomaticsTapApi tapEnv,
			List<String> dcmScriptDataPayload) {

		JSONObject telemetryDataPayLoad = new JSONObject();
		try {

			for (String dataPayLoad : dcmScriptDataPayload) {

				if (CommonMethods.isNotNull(dataPayLoad)) {
					JSONObject telemetrySearchResults = new JSONObject(dataPayLoad);
					JSONArray searchResultsArray = telemetrySearchResults.getJSONArray("searchResult");
					LOGGER.debug(" TELEMETRY EVENTS = " + searchResultsArray.toString());
					for (int index = 0; index < searchResultsArray.length(); index++) {

						JSONObject data = searchResultsArray.getJSONObject(index);

						if (data.has("Profile")) {
							telemetryDataPayLoad.put("Profile", data.getString("Profile"));
						} else if (data.has("mac")) {
							telemetryDataPayLoad.put("mac", data.getString("mac"));
						} else if (data.has("erouterIpv4")) {
							telemetryDataPayLoad.put("erouterIpv4", data.getString("erouterIpv4"));
						} else if (data.has("erouterIpv6")) {
							telemetryDataPayLoad.put("erouterIpv6", data.getString("erouterIpv6"));
						} else if (data.has("PartnerId")) {
							telemetryDataPayLoad.put("PartnerId", data.getString("PartnerId"));
						} else if (data.has("Version")) {
							telemetryDataPayLoad.put("Version", data.getString("Version"));
						} else if (data.has("Time")) {
							telemetryDataPayLoad.put("Time", data.getString("Time"));
						} else if (data.has(BroadBandTelemetryConstants.TELEMETRY_MARKER_BOOTUPTIME_ETHERNET)) {
							telemetryDataPayLoad.put(BroadBandTelemetryConstants.TELEMETRY_MARKER_BOOTUPTIME_ETHERNET,
									data.getString(BroadBandTelemetryConstants.TELEMETRY_MARKER_BOOTUPTIME_ETHERNET));
						} else if (data.has(BroadBandTelemetryConstants.TELEMETRY_MARKER_BOOTUPTIME_MOCA)) {
							telemetryDataPayLoad.put(BroadBandTelemetryConstants.TELEMETRY_MARKER_BOOTUPTIME_MOCA,
									data.getString(BroadBandTelemetryConstants.TELEMETRY_MARKER_BOOTUPTIME_MOCA));
						} else if (data.has(BroadBandTelemetryConstants.TELEMETRY_MARKER_BOOTUPTIME_WEBPA)) {
							telemetryDataPayLoad.put(BroadBandTelemetryConstants.TELEMETRY_MARKER_BOOTUPTIME_WEBPA,
									data.getString(BroadBandTelemetryConstants.TELEMETRY_MARKER_BOOTUPTIME_WEBPA));
						}
					}
				}
			}

		} catch (Exception e) {
			throw new TestException("Exception occured while retriving the telemetry JSON payload from dcmscript.log ");
		}

		return telemetryDataPayLoad;
	}

	/**
	 * Helper method to verify the XConf query parameters
	 * 
	 * @param device     The device under test
	 * @param query      The query string
	 * @param queryParam The Query Params objecy
	 * @return The status of verification
	 * @throws TestException Throws exception if the verification failed
	 * @author divya.rs
	 * @refactor Said Hisham
	 */
	public static boolean verifyXConfQueryParamameter(Dut device, AutomaticsTapApi tapEnv, JSONObject queryParameter,
			XConfQueryParams queryParam) {
		LOGGER.debug("STARTING METHOD: verifyXConfQueryParams");
		boolean comparisonStatus = false;
		try {
			switch (queryParam) {
			case ESTB_MACADDRESS:
				String reqdEstbMacAddress = ((Device) device).getHostMacAddress();
				String estMacAddress = queryParameter.getString("estbMacAddress");
				LOGGER.info("EXPECTED : ESTB Mac Address of the device : " + reqdEstbMacAddress
						+ ", ACTUAL :  ESTB Mac Address present in XCONF/DCM Query URL = " + estMacAddress);
				if (estMacAddress.contains(BroadBandTestConstants.URL_ENCODED_CHAR)) {
					estMacAddress = estMacAddress.replaceAll(BroadBandTestConstants.URL_ENCODED_CHAR,
							AutomaticsConstants.EMPTY_STRING);
				}
				if (StringUtils.equalsIgnoreCase(reqdEstbMacAddress, estMacAddress)) {
					LOGGER.info("Successfully verified the presence of eSTB Mac Address in XCONF/DCM query URL");
					comparisonStatus = true;
				}
				break;

			case ECM_MACADDRESS:
				String reqdEcmMacAddress = ((Device) device).getEcmMac();

				String ecmMac = queryParameter.getString("ecmMacAddress");
				LOGGER.info("EXPECTED : ECM Mac Address of the device : " + reqdEcmMacAddress
						+ " , ACTUAL : ECM Mac Address present in XCONF/DCM Query URL : " + ecmMac);
				if (ecmMac.contains(BroadBandTestConstants.URL_ENCODED_CHAR)) {
					estMacAddress = ecmMac.replaceAll(BroadBandTestConstants.URL_ENCODED_CHAR,
							AutomaticsConstants.EMPTY_STRING);
				}
				if (StringUtils.equalsIgnoreCase(reqdEcmMacAddress, ecmMac)) {
					LOGGER.info("Successfully verified the presence of ecm Mac Address in XCONF/DCM query URL");
					comparisonStatus = true;
				}
				break;

			case FIRMWARE_VERSION:
				String reqdfirmwareVersion = device.getFirmwareVersion();

				String firmwareVersion = queryParameter.getString("firmwareVersion");
				LOGGER.info("EXPECTED : Current Firmware version: " + reqdfirmwareVersion
						+ " , ACTUAL : Firmware version present in XCONF/DCM Query URL : " + firmwareVersion);
				if (firmwareVersion.trim().equalsIgnoreCase(reqdfirmwareVersion)) {
					LOGGER.info("Successfully verified the presence of firmware version in XCONF/DCM query URL");
					comparisonStatus = true;
				}
				break;

			case MODEL:
				String reqdModel = ((Device) device).getModel();
				LOGGER.info("Current device model: " + reqdModel);
				String model = queryParameter.getString("model");
				LOGGER.info("EXPECTED : Current device model: " + reqdModel
						+ " , ACTUAL : Device Model present in XCONF/DCM Query URL : " + model);
				if (reqdModel.contains(model.trim())) {
					LOGGER.info("Successfully verified the presence of model in XConf query URL");
					comparisonStatus = true;
				}
				break;
			}
		} catch (Exception ex) {
			throw new TestException(
					"Exception occured while verifying XCONF/DCM Query parameter = " + ex.getLocalizedMessage());
		}

		LOGGER.debug("ENDING METHOD: verifyXConfQueryParams");
		return comparisonStatus;
	}

	public static boolean verifyTelemetryPayLoadParamameter(Dut device, AutomaticsTapApi tapEnv,
			JSONObject telemetryEvents, TelemetryData telemetryData) {
		LOGGER.debug("STARTING METHOD: verifyTelemetryPayLoadParamameter");
		boolean comparisonStatus = false;
		try {
			switch (telemetryData) {

			case EROUTER_MAC_ADDRESS:

				String reqdMacAddress = ((Device) device).getHostMacAddress();
				String macAddress = telemetryEvents.getString("mac");
				LOGGER.info("EXPECTED : Erouter Mac Address of the device : " + reqdMacAddress
						+ ", ACTUAL :  Erouter Mac Address present in TELEMETRY DATA PAY LOAD 'searchResults' = "
						+ macAddress);
				if (StringUtils.equalsIgnoreCase(reqdMacAddress, macAddress)) {
					LOGGER.info(
							"Successfully verified the presence of Erouter Mac Address in TELEMETRY DATA PAY LOAD 'searchResults'");
					comparisonStatus = true;
				}
				break;

			case VERSION:

				String reqdfirmwareVersion = device.getFirmwareVersion();
				String firmwareVersion = telemetryEvents.getString("Version");
				LOGGER.info("EXPECTED : Current Firmware version: " + reqdfirmwareVersion
						+ " , ACTUAL : Firmware version present in TELEMETRY DATA PAY LOAD 'searchResults'  : "
						+ firmwareVersion);
				if (firmwareVersion.trim().equalsIgnoreCase(reqdfirmwareVersion)) {
					LOGGER.info(
							"Successfully verified the presence of firmware version in Telemetry Events sent to Splunk");
					comparisonStatus = true;
				}
				break;

			case PROFILE:

				String reqdProfile = "RDKB";
				String actualProfile = telemetryEvents.has("Profile") ? telemetryEvents.getString("Profile") : "";
				LOGGER.info("EXPECTED : Profile : " + reqdProfile
						+ " , ACTUAL : Profile present in TELEMETRY DATA PAY LOAD 'searchResults' : " + actualProfile);
				if (reqdProfile.equalsIgnoreCase(actualProfile)) {
					LOGGER.info("Successfully verified the presence of Profile in Telemetry Events sent to Splunk");
					comparisonStatus = true;
				}
				break;

			case PARTNER_ID:
				String expectedPartnerId = getSyndicationPartnerIdThroughWebPa(device, tapEnv);
				String actualPartnerId = telemetryEvents.getString("PartnerId");
				LOGGER.info("EXPECTED : Partner ID : " + expectedPartnerId
						+ " , ACTUAL : Partner ID present in TELEMETRY DATA PAY LOAD 'searchResults' : "
						+ actualPartnerId);
				if (expectedPartnerId.equalsIgnoreCase(actualPartnerId)) {
					LOGGER.info("Successfully verified the presence of PartnerId in Telemetry Events sent to Splunk");
					comparisonStatus = true;
				}
				break;
			}
		} catch (Exception ex) {
			throw new TestException(
					"Exception occured while verifying XCONF/DCM Query parameter = " + ex.getLocalizedMessage());
		}

		LOGGER.debug("ENDING METHOD: verifyTelemetryPayLoadParamameter");
		return comparisonStatus;
	}

	/**
	 * Method to get syndication partner id using webpa param
	 * Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.PartnerId
	 * 
	 * We will try to get the partner id using webpa parameter if the parameter is
	 * not available in any device we will take Property file as partner id
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param settop {@link Dut}
	 * @return String partnerId
	 * @refactor Rakesh C N
	 */
	public static String getSyndicationPartnerIdThroughWebPa(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: getSyndicationPartnerIdThroughWebPa");
		String partnerId = null;
		partnerId = tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_FOR_SYNDICATION_PARTNER_ID);
		if (CommonMethods.isNull(partnerId)) {
			partnerId = BroadbandPropertyFileHandler.getDefaultPartnerID();
		}
		LOGGER.debug("ENDING METHOD:getSyndicationPartnerIdThroughWebPa");
		return partnerId;
	}

	/**
	 * helper method to configure /nvram/swupdate.conf with the XCONF update URL.
	 * URL is same for both IPv4 and IPv6
	 * 
	 * @param device instance of DUT
	 * @param tapApi instance of {@link AutomaticsTapApi}
	 * @return true if successfully updated /nvram/swupdate.conf file with given
	 *         xconfUpdateUrl
	 * @refactor Said hisham
	 */
	public static boolean configureAndVerifySwupdateWithXconfUrl(Dut device, AutomaticsTapApi tapApi) {

		LOGGER.info("STARTING METHOD: TelemetryUtils.configureAndVerifySwupdateWithXconfUrl");

		// Validation status
		boolean status = false;
		// Command to execute
		String cmd = null;
		String proxyDcmXconfSwupdateUrl = AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PROXY_XCONF_SWUPDATE_URL);
		if (CommonUtils.isFileExists(device, tapApi, BroadBandTestConstants.SOFTWARE_UPDATE_CONF_FILE)) {
			cmd = "echo " + proxyDcmXconfSwupdateUrl + " > " + BroadBandTestConstants.SOFTWARE_UPDATE_CONF_FILE;
		} else {
			cmd = "echo " + proxyDcmXconfSwupdateUrl + " > " + BroadBandTestConstants.SOFTWARE_UPDATE_CONF_FILE_2;
		}
		LOGGER.info("Command for update /nvram/swupdate.conf is " + cmd);
		tapApi.executeCommandUsingSsh(device, cmd);

		// verify whether swupdate.conf has been updated
		String response = tapApi.executeCommandUsingSsh(device,
				BroadBandTestConstants.CAT_COMMAND + BroadBandTestConstants.SOFTWARE_UPDATE_CONF_FILE);
		LOGGER.info("Content of " + BroadBandTestConstants.SOFTWARE_UPDATE_CONF_FILE + " is :" + response);
		if (CommonMethods.isNull(response) || response.contains(BroadBandTestConstants.NO_SUCH_FILE_OR_DIRECTORY)) {
			response = tapApi.executeCommandUsingSsh(device,
					BroadBandTestConstants.CAT_COMMAND + BroadBandTestConstants.SOFTWARE_UPDATE_CONF_FILE_2);
			LOGGER.info("Content of " + BroadBandTestConstants.SOFTWARE_UPDATE_CONF_FILE_2 + " is :" + response);
		}

		if (CommonMethods.isNotNull(response) && response.contains(proxyDcmXconfSwupdateUrl)) {
			status = true;
		}
		LOGGER.info("ENDING METHOD: TelemetryUtils.configureAndVerifySwupdateWithXconfUrl");
		return status;
	}

}

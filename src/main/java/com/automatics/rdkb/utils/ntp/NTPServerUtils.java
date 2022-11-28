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

package com.automatics.rdkb.utils.ntp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Dut;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.constants.BroadBandPropertyKeyConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.rdkb.constants.WebPaParamConstants;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.BroadBandRfcFeatureControlUtils;
import com.automatics.rdkb.utils.BroadBandSystemUtils;
import com.automatics.rdkb.utils.CommonUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.test.AutomaticsTestBase;
import com.automatics.utils.CommonMethods;
import com.automatics.webpa.WebPaParameter;
import com.automatics.rdkb.utils.ntp.DeviceTimeParams;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;

/**
 * Class having Methods related to ntp automation.
 * 
 * @author susheela
 * 
 */

public class NTPServerUtils extends AutomaticsTestBase {

	/** Error Message text */
	private static String errorMessage = null;

	/**
	 * Method to set value of TR181 param DEVICE.TIME.ENABLE using WEBPA
	 * 
	 * @param tapEnv       The automatics tapApi.
	 * @param device       The {@link Dut} object.
	 * @param enableStatus Device Time param status to be set
	 * @return The validation result
	 * @author Susheela
	 * @refactor Athira
	 */
	public static boolean setDeviceTimeEnableParamValue(AutomaticsTapApi tapEnv, Dut device, boolean enableStatus) {
		LOGGER.debug("STARTING METHOD: NTPServerUtils.setDeviceTimeEnableParamValue");
		String statusToSet = enableStatus == true ? "1" : "0";
		boolean setStatus = false;
		try {
			List<WebPaParameter> list = tapEnv.setWebPaParams(device,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_TIME_ENABLE, statusToSet,
					BroadBandTestConstants.CONSTANT_3);
			for (WebPaParameter webPaParameter : list) {
				LOGGER.info(webPaParameter.getName() + "-------------" + webPaParameter.getMessage());
				if (webPaParameter.getMessage().equalsIgnoreCase(RDKBTestConstants.STRING_CONNECTION_STATUS)) {
					setStatus = true;
				}
			}
			LOGGER.info("WebPA Response --> " + list.toString());
		} catch (Exception exp) {
			LOGGER.error("Exception thrown : " + exp.getMessage());
			setStatus = false;
		}
		if (!setStatus) {
			throw new TestException("Error thrown while setting TR181 param Device.Time.Enable");
		}
		LOGGER.debug("ENDING METHOD: NTPServerUtils.setDeviceTimeEnableParamValue");
		return setStatus;
	}

	/**
	 * Method to set value of TR181 param DEVICE.TIME.NTPServerUrl using WEBPA
	 * 
	 * @param tapEnv       The {@link AutomaticsTapApi} object.
	 * @param device       The {@link Dut} object.
	 * @param ntpServerUrl Device Time url param to set
	 * @return The validation result
	 * @author Susheela
	 * @refactor Athira
	 */
	public static boolean setDeviceTimeNTPServerParamValue(AutomaticsTapApi tapEnv, Dut device, String ntpServerUrl) {
		LOGGER.debug("STARTING METHOD: NTPServerUtils.setDeviceTimeNTPServerParamValue");
		boolean setStatus = false;
		try {
			List<WebPaParameter> list = tapEnv.setWebPaParams(device,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_TIME_NTPServer1, ntpServerUrl,
					RDKBTestConstants.CONSTANT_0);
			for (WebPaParameter webPaParameter : list) {
				LOGGER.info(webPaParameter.getName() + "-------------" + webPaParameter.getMessage());
				if (webPaParameter.getMessage().equalsIgnoreCase(RDKBTestConstants.STRING_CONNECTION_STATUS)) {
					setStatus = true;
				}
			}
			LOGGER.info("WebPA Response --> " + list.toString());
		} catch (Exception exp) {
			LOGGER.error("Exception thrown : " + exp.getMessage());
			setStatus = false;
		}
		if (!setStatus) {
			throw new TestException("Error thrown while setting TR181 param Device.Time.NTPServerUrl");
		}
		LOGGER.debug("ENDING METHOD: NTPServerUtils.setDeviceTimeNTPServerParamValue");
		return setStatus;
	}

	/**
	 * Method to grep the logs to validate if NTP server was enabled and NTP server
	 * url updated
	 * 
	 * @param tapEnv       The {@link AutomaticsTapApi} object.
	 * @param device       The {@link Dut} object.
	 * @param ntpServerUrl Device Time url value to validate
	 * @return The validation result
	 * @author Susheela
	 * @refactor Athira
	 */
	public static boolean validateNTPServerEnableAndUrlInLogs(AutomaticsTapApi tapEnv, Dut device, String ntpServerUrl,
			String deviceDateTime) {
		LOGGER.debug("STARTING METHOD: NTPServerUtils.validateNTPServerEnableAndUrlInLogs");
		boolean status = false;
		boolean logString1Result = false;

		if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
			// Search result for log string "Enabling Network Time Sync"
			logString1Result = BroadBandSystemUtils.verifyArmConsoleLog(tapEnv, device,
					BroadBandTraceConstants.LOG_MESSAGE_DEVICE_TIME_ENABLE, BroadBandTestConstants.COMMAND_NTP_LOG_FILE,
					deviceDateTime);
		}
		// Search result for log string "Setting NTPServer as '<url>'"
		String response = getLatestLogResponse(tapEnv, device, BroadBandTestConstants.LOG_STRING_SETTING_NTPSERVER,
				BroadBandTestConstants.COMMAND_NTP_LOG_FILE, deviceDateTime);

		boolean logString2Result = CommonUtils.isNotEmptyOrNull(response)
				? (response.contains(BroadBandTestConstants.LOG_STRING_SETTING_NTPSERVER + "'" + ntpServerUrl + "'"))
				: false;

		if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
			status = logString1Result && logString2Result;
		} else {
			status = logString2Result;
		}

		if (!status) {
			throw new TestException("Logs validation failed on PAmlog.txt.0 for NTP server enable and ntp server url");
		}
		LOGGER.debug("ENDING METHOD: NTPServerUtils.validateNTPServerEnableAndUrlInLogs");
		return status;
	}

	/**
	 * Method to grep the lLatest log response
	 * 
	 * @param tapEnv         The {@link AutomaticsTapApi} object.
	 * @param device         The {@link Dut} object.
	 * @param searchText     text to search
	 * @param logFileName
	 * @param deviceDateTime
	 * @return The validation result
	 * @author Susheela
	 * @refactor Athira
	 */
	public static String getLatestLogResponse(AutomaticsTapApi tapEnv, Dut device, String searchText,
			String logFileName, String deviceDateTime) {
		LOGGER.debug("ENTERING METHOD verifyExpectedStringInLogFileFromArmConsole");
		boolean result = false;
		String command = BroadBandTestConstants.GREP_COMMAND + "\"" + searchText + "\""
				+ BroadBandTestConstants.SINGLE_SPACE_CHARACTER + logFileName + BroadBandTestConstants.SYMBOL_PIPE
				+ BroadBandTestConstants.CMD_TAIL_1;
		String response = tapEnv.executeCommandUsingSsh(device, command);
		LOGGER.info("FROM ATOM CONSOLE RESPONSE :" + response);
		if (CommonMethods.isNotNull(response) && response.contains(searchText)) {
			result = BroadBandCommonUtils.verifyLogUsingTimeStamp(deviceDateTime, response);
		}
		LOGGER.debug("ENDING METHOD verifyExpectedStringInLogFileFromArmConsole");
		if (!result) {
			response = null;
		}
		return response;
	}

	/**
	 * Method to retrieve current date from STB
	 * 
	 * @param device instance of {@link Dut}
	 * @param tapApi instance of {@link AutomaticsTapApi}
	 * @return Calendar stb time in UTC
	 * @refactor Athira
	 */
	public static Calendar getStbTime(Dut device, AutomaticsTapApi tapApi) {

		LOGGER.debug("STARTING METHOD: getStbTime()");

		// Variable to hold STB Time
		Calendar stbTime = null;
		// STB Time format
		SimpleDateFormat formatter = new SimpleDateFormat(BroadBandTestConstants.SYSTEM_DATE_TIME_FORMAT_RETRIEVED);

		try {

			// Retrieve current date from STB
			String response = tapApi.executeCommandUsingSsh(device, BroadBandTestConstants.CMD_DATE);

			if (response != null && !response.isEmpty()) {

				response = response.trim();

				Date date = formatter.parse(response);

				stbTime = Calendar.getInstance();
				stbTime.setTime(date);

				stbTime.setTimeZone(TimeZone.getTimeZone("UTC"));
				LOGGER.info("STB Time retrieved : " + response);
			} else {
				throw new TestException("No Response returned while setting date and time");
			}
		} catch (ParseException parseException) {
			throw new TestException("Exception occured in getStbTime()" + parseException.getMessage());
		} catch (Exception exception) {
			throw new TestException("Exception occured in getStbTime()" + exception.getMessage());
		}
		LOGGER.debug("ENDING METHOD: getStbTime()");
		return stbTime;
	}

	/**
	 * Method to set date on STB
	 * 
	 * @param device  instance of {@link Dut}
	 * @param tapApi  instance of {@link AutomaticsTapApi}
	 * @param stbTime instance of Calendar time
	 * @return status of set time
	 * @refactor Athira
	 */
	public static boolean setStbTime(Dut device, AutomaticsTapApi tapApi, Calendar stbTime) {

		LOGGER.debug("STARTING METHOD: setStbTime()");

		// STB Time format
		SimpleDateFormat formatter = new SimpleDateFormat(BroadBandTestConstants.SYSTEM_DATE_TIME_FORMAT_RETRIEVED);
		SimpleDateFormat formatter1 = new SimpleDateFormat(BroadBandTestConstants.SYSTEM_DATE_TIME_FORMAT_TO_SET);
		boolean status = false;
		try {
			String stbTimeToSet = formatter1.format(stbTime.getTime());
			// set date on STB
			String response = tapApi.executeCommandUsingSsh(device,
					BroadBandTestConstants.CMD_DATE + " " + stbTimeToSet);

			if (response != null && !response.isEmpty()) {

				response = response.trim();
				Date date = formatter.parse(response);
				status = date.compareTo(stbTime.getTime()) == 0;
				LOGGER.info("STB Time retrieved : " + response);
			} else {
				throw new TestException("No Response returned while setting date and time");
			}
		} catch (ParseException parseException) {
			throw new TestException("Exception occured in setStbTime()" + parseException.getMessage());
		} catch (Exception exception) {
			throw new TestException("Exception occured in setStbTime()" + exception.getMessage());
		}

		LOGGER.debug("ENDING METHOD: setStbTime()");

		return status;
	}

	/**
	 * Validate system time synch with NTP server time
	 * 
	 * @param device  instance of {@link Dut}
	 * @param tapApi  instance of {@link AutomaticsTapApi}
	 * @param stbTime instance of Calendar time
	 * @return status of time synch with NTP server
	 * @refactor Athira
	 */
	public static boolean validateStbTimeAfterNTPServerSynch(Dut device, AutomaticsTapApi tapApi, Calendar stbTime) {
		LOGGER.debug("STARTING METHOD: NTPServerUtils.validateStbTimeAfterNTPServerSynch");
		boolean status = false;
		Calendar currentTime = null;
		long startTime = System.currentTimeMillis();
		long maxPollTime = RDKBTestConstants.FIFTEEN_MINUTES_IN_MILLIS;
		while (System.currentTimeMillis() - startTime < maxPollTime) {
			currentTime = getStbTime(device, tapApi);
			if (currentTime.compareTo(stbTime) < 0) {
				// Ideally current time will be greater than passed time, but
				// after NTP server synch current time will be less than the
				// passed time
				String response = tapApi.executeCommandUsingSsh(device, BroadBandTestConstants.CMD_DATE);
				status = CommonUtils.verifyBoxDate(device, tapApi, response);
				LOGGER.info("STB time synchronized with NTP server time");
				break;
			}
		}
		if (!status) {
			throw new TestException("Failed to verify STB Time synch with NTP server");
		}
		LOGGER.debug("ENDING METHOD: NTPServerUtils.validateStbTimeAfterNTPServerSynch");
		return status;
	}

	/**
	 * Method to Verify NTPD process if running or not
	 * 
	 * @param tapEnv AutomaticsTapApi
	 * @param device The {@link Dut} object.
	 * @return The validation result
	 * @author Susheela
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean verifyNTPDProcess(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("STARTING METHOD: NTPServerUtils.verifyNTPDProcess");
		boolean status = false;
		String response = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.COMMAND_NTPD_PROCESS);
		if (CommonUtils.isNotEmptyOrNull(response)
				&& (response.split(BroadBandTestConstants.DELIMITER_NEW_LINE).length > 1)) {
			status = true;
		} else {
			throw new TestException(
					"NTPD process is not running on the device, please start the process before execution");
		}
		LOGGER.debug("ENDING METHOD: NTPServerUtils.verifyNTPDProcess");
		return status;
	}

	/**
	 * Method to get value of TR181 param DEVICE.TIME.ENABLE using WEBPA
	 * 
	 * @param tapEnv AutomaticsTapApi
	 * @param device The {@link Dut} object.
	 * @return The validation result
	 * @author Susheela
	 * @Refactor Sruthi Santhosh
	 */
	public static String getDeviceTimeEnableParamValue(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("STARTING METHOD: NTPServerUtils.getDeviceTimeEnableParamValue");
		String response = null;
		try {
			response = tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_TIME_ENABLE);
		} catch (Exception exp) {
			LOGGER.error("Exception thrown : " + exp.getMessage());
			response = null;
		}
		if (!CommonUtils.isNotEmptyOrNull(response)) {
			throw new TestException("Error WebPa response returned for the TR181 param Device.Time.Enable");
		}
		LOGGER.debug("ENDING METHOD: NTPServerUtils.getDeviceTimeEnableParamValue");
		return response;
	}

	/**
	 * Method to validate the default param values of TR181 param Device.Time.
	 * 
	 * @param tapEnv AutomaticsTapApi
	 * @param device The {@link Dut} object.
	 * @return The validation result
	 * @author Susheela
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean validateDefaultDeviceTimeParamValues(AutomaticsTapApi tapEnv, Dut device)
			throws TestException {
		LOGGER.debug("STARTING METHOD: NTPServerUtils.validateDefaultDeviceTimeParamValues");
		String upTime = null;
		boolean defaultNTPServerUrlStatus = false;
		try {
			upTime = tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_UPTIME);
			LOGGER.info("uptime-->" + Integer.parseInt(upTime) / BroadBandTestConstants.CONSTANT_60);
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			LOGGER.error(errorMessage);
		}
		DeviceTimeParams response = getDeviceTimeParamValue(tapEnv, device);
		boolean defaultEnableStatus = !response.getDeviceTimeEnableValue() ? true : false;
		boolean defaultStatus = response.getDeviceTimeStatus()
				.equalsIgnoreCase(BroadBandTestConstants.STATUS_VALUE_DISABLED);
		try {
			if (Integer.parseInt(upTime) / BroadBandTestConstants.CONSTANT_60 < BroadBandTestConstants.CONSTANT_15
					&& CommonMethods.isAtomSyncAvailable(device, tapEnv)) {

				LOGGER.error("response " + response);
				defaultNTPServerUrlStatus = CommonMethods.isNull(response.getDeviceTimeNTPServer1())
						|| response.getDeviceTimeNTPServer1().isEmpty() ? true : false;

			} else {
				defaultNTPServerUrlStatus = response.getDeviceTimeNTPServer1()
						.equalsIgnoreCase(BroadBandTestConstants.EXPECTED_OUTPUT_FOR_NTPHOST) ? true : false;
			}
		} catch (Exception e) {
			LOGGER.error("exception " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: NTPServerUtils.validateDefaultDeviceTimeParamValues");
		return (defaultEnableStatus && defaultStatus && defaultNTPServerUrlStatus);
	}

	/**
	 * Method to get values of TR181 param DEVICE.TIME. using WEBPA
	 * 
	 * @param tapEnv AutomaticsTapApi
	 * @param device The {@link Dut} object.
	 * @return The validation result
	 * @author Susheela
	 * @Refactor Sruthi Santhosh
	 */
	public static DeviceTimeParams getDeviceTimeParamValue(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("STARTING METHOD: NTPServerUtils.getDeviceTimeParamValue");
		String response = null;
		try {
			response = tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_TIME);
		} catch (Exception exp) {
			LOGGER.error("Exception thrown : " + exp.getMessage());
			response = null;
		}
		if (!CommonUtils.isNotEmptyOrNull(response)) {
			throw new TestException("Error WebPa response returned for the TR181 param Device.Time.");
		}
		DeviceTimeParams deviceParamValues = null;
		try {
			deviceParamValues = new DeviceTimeParams();
			String pattern = "Device.Time.Enable\",\"value\":\"(\\w*)";
			String defaultEnableBool = CommonMethods.patternFinder(response, pattern);
			LOGGER.info("Default Value for TR181 param Device.Time.Enable : " + defaultEnableBool);
			deviceParamValues.setDeviceTimeEnableValue(defaultEnableBool.equalsIgnoreCase("true") ? true : false);

			pattern = "Device.Time.Status\",\"value\":\"(\\w*)";
			String status = CommonMethods.patternFinder(response, pattern);
			LOGGER.info("Default Value for TR181 param Device.Time.Status : " + status);
			deviceParamValues.setDeviceTimeStatus(status);

			pattern = "Device.Time.NTPServer1\",\"value\":\"(\\S)\",";
			String ntpServer1 = CommonMethods.patternFinder(response, pattern);
			if (CommonMethods.isNull(ntpServer1)) {
				deviceParamValues.setDeviceTimeNTPServer1("");
				LOGGER.error("Default Value for TR181 param Device.Time.NTPServerUrl1 value is null or empty ");
			} else {
				ntpServer1 = CommonMethods.patternFinder(ntpServer1, BroadBandTestConstants.NTPServerURL_Pattern);
				deviceParamValues.setDeviceTimeNTPServer1(ntpServer1);
			}
			LOGGER.info("Default Value for TR181 param Device.Time.NTPServerUrl1 : " + ntpServer1);

			pattern = "Device.Time.NTPServer2\",\"value\":\"(\\S)\",";
			String ntpServer2 = CommonMethods.patternFinder(response, pattern);
			if (CommonMethods.isNull(ntpServer2)) {
				deviceParamValues.setDeviceTimeNTPServer2("");
				LOGGER.error("Default Value for TR181 param Device.Time.NTPServerUrl2 value is null or empty ");
			} else {
				ntpServer2 = CommonMethods.patternFinder(ntpServer2, BroadBandTestConstants.NTPServerURL_Pattern);
				deviceParamValues.setDeviceTimeNTPServer2(ntpServer2);
			}
			LOGGER.info("Default Value for TR181 param Device.Time.NTPServerUrl2 : " + ntpServer2);
		} catch (Exception e) {
			LOGGER.error("Exception occurred while validating getDeviceTimeParamValue()" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: NTPServerUtils.getDeviceTimeParamValue");
		return deviceParamValues;
	}

	/**
	 * Method to execute the precondition for testing enabling/disabling NTP server
	 * feature
	 * 
	 * @param tapEnv                 {@link AutomaticsTapApi}
	 * @param device                 The {@link Dut} object.
	 * @param ntpEnableOrDisableFlag Flag to set ntp enable or disable
	 * @return The validation result
	 * @throws JSONException
	 * @author Susheela
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean enableOrDisableNTPFeatureByRFC(AutomaticsTapApi tapEnv, Dut device,
			boolean ntpEnableOrDisableFlag) throws JSONException {
		LOGGER.debug("STARTING METHOD: NTPServerUtils.executPreconditionForNTPFeatureByRFC");

		String proxyXconfUrl = AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PROXY_XCONF_RFC_URL);
		boolean status = false;
		try {
			if (NTPServerUtils.enableOrDisableNTPInProxyXconf(tapEnv, device, ntpEnableOrDisableFlag)) {
				if (BroadBandRfcFeatureControlUtils.copyRfcPropertiesFromEtcToNVRAM(device, tapEnv)) {
					status = BroadBandRfcFeatureControlUtils.copyAndUpdateRfcPropertiesNewXconfUrl(device, tapEnv,
							proxyXconfUrl);
					tapEnv.reboot(device);
					if (CommonMethods.waitForEstbIpAcquisition(tapEnv, device)) {

						status = BroadBandWebPaUtils.setParameterValuesUsingWebPaOrDmcli(device, tapEnv,
								WebPaParamConstants.WEBPA_PARAM_IMMEDIATE_RFC_CHECK_IN,
								BroadBandTestConstants.CONSTANT_2, BroadBandTestConstants.STRING_VALUE_ONE);
					}
				} else {
					throw new TestException("Error faced while copying dcm properties file from etc to nvram folder");
				}
			} else {
				throw new TestException("NTP server feature not successfully enabled/disbaled in XCONF");
			}

		} catch (Exception exception) {
			errorMessage = exception.getMessage();
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}
		LOGGER.debug("ENDING METHOD: NTPServerUtils.executPreconditionForNTPFeatureByRFC");
		return status;
	}

	/**
	 * Method to enable or disable NTP in proxy/mock xconf
	 * 
	 * @param tapEnv                 {@link AutomaticsTapApi}
	 * @param device                 The {@link Dut} object.
	 * @param ntpEnableOrDisableFlag Flag to set ntp enable or disable
	 * @return The validation result
	 * @throws JSONException
	 * @author Susheela
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean enableOrDisableNTPInProxyXconf(AutomaticsTapApi tapEnv, Dut device,
			boolean ntpEnableOrDisableFlag) throws JSONException {
		LOGGER.debug("STARTING METHOD: NTPServerUtils.enableOrDisableNTPInProxyXconf");
		boolean result = false;
		String ntpPayLoadData = AutomaticsTapApi
				.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_NTP_ENABLE_MOCK_XCONF);
		int responseCode = 0;

		if (!ntpEnableOrDisableFlag) {
			ntpPayLoadData = AutomaticsTapApi
					.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PAYLOAD_NTP_DISABLE_MOCK_XCONF);
		}

		LOGGER.info("PAY LOAD DATA: " + ntpPayLoadData);
		JSONObject jsonObj = new JSONObject(ntpPayLoadData);
		String stbMacAddress = device.getHostMacAddress();
		LOGGER.info("Device Mac address: " + stbMacAddress);

		if (null != jsonObj && CommonMethods.isNotNull(stbMacAddress)) {
			ntpPayLoadData = jsonObj.toString();
			ntpPayLoadData = ntpPayLoadData.replace(BroadBandTestConstants.CONSTANT_REPLACE_STBMAC_LSAPAYLOADDATA,
					stbMacAddress);
			LOGGER.info("PAY LOAD DATA: " + ntpPayLoadData);
			responseCode = BroadBandRfcFeatureControlUtils.postDataToProxyXconfDcmServer(device, tapEnv,
					ntpPayLoadData);
			LOGGER.info("RESPONSE CODE: " + responseCode);
			if (responseCode == AutomaticsConstants.CONSTANT_200) {
				result = true;
			}
		} else {
			LOGGER.error("Device Mac address is not obtained");
		}

		LOGGER.info("Status of ntp enable " + ntpEnableOrDisableFlag + " in xconf: " + result);
		LOGGER.debug("ENDING METHOD: NTPServerUtils.enableOrDisableNTPInProxyXconf");
		return result;
	}

	/**
	 * Method to grep the logs to validate if NTP server was disabled and NTP server
	 * url updated
	 * 
	 * @param tapEnv       {@link AutomaticsTapApi}
	 * @param device       The {@link Dut} object.
	 * @param ntpServerUrl Device Time url value to validate
	 * @return The validation result
	 * @author Susheela
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean validateNTPServerDisableAndUrlInLogs(AutomaticsTapApi tapEnv, Dut device, String ntpServerUrl,
			String deviceDateTime) {
		LOGGER.debug("STARTING METHOD: NTPServerUtils.validateNTPServerDisableAndUrlInLogs");
		boolean logString1Result = false;
		boolean logString2Result = false;
		boolean status = false;
		if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
			// Serach result for log string "Disabling Network Time Sync"
			logString1Result = BroadBandSystemUtils.verifyArmConsoleLog(tapEnv, device,
					BroadBandTraceConstants.LOG_MESSAGE_DEVICE_TIME_DISABLE,
					BroadBandTestConstants.COMMAND_NTP_LOG_FILE, deviceDateTime);
		}
		// Serach result for log string "Setting NTPServer as '<url>'"
		String response = getLatestLogResponse(tapEnv, device, BroadBandTestConstants.LOG_STRING_SETTING_NTPSERVER,
				BroadBandTestConstants.COMMAND_NTP_LOG_FILE, deviceDateTime);

		logString2Result = CommonUtils.isNotEmptyOrNull(response)
				? (response.contains(BroadBandTestConstants.LOG_STRING_SETTING_NTPSERVER + "'" + ntpServerUrl + "'"))
				: false;

		if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
			status = logString1Result && logString2Result;
		} else
			status = logString2Result;

		if (!status) {
			throw new TestException("Logs validation failed on PAmlog.txt.0 for NTP server disable and ntp server url");
		}
		LOGGER.debug("ENDING METHOD: NTPServerUtils.validateNTPServerDisableAndUrlInLogs");
		return status;
	}
}

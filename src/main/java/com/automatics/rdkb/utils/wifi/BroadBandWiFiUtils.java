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
package com.automatics.rdkb.utils.wifi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Device;
import com.automatics.device.Dut;
import com.automatics.enums.ExecutionStatus;
import com.automatics.enums.TR69ParamDataType;
import com.automatics.exceptions.TestException;
import com.automatics.providers.tr69.Parameter;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.WiFiSsidConfigStatus;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandConnectedClientTestConstants;
import com.automatics.rdkb.constants.BroadBandPropertyKeyConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.rdkb.constants.RDKBTestConstants.WiFiFrequencyBand;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;
import com.automatics.rdkb.utils.CommonUtils;
import com.automatics.rdkb.utils.ConnectedNattedClientsUtils;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.rdkb.utils.dmcli.DmcliUtils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.wifi.connectedclients.BroadBandConnectedClientUtils;
import com.automatics.rdkb.webui.page.BroadBandWifiPage.SSIDFrequency;
import com.automatics.snmp.SnmpDataType;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.test.AutomaticsTestBase;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.CommonMethods;
import com.automatics.webpa.WebPaParameter;
import com.automatics.webpa.WebPaServerResponse;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;
import com.automatics.rdkb.enums.BroadBandMFPConfigEnum;
import com.automatics.rdkb.enums.ProtocolOperationTypeEnum;
import com.automatics.rdkb.enums.BroadBandWhixEnumConstants.WEBPA_AP_INDEXES;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpMib;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpUtils;

/**
 * Utility class which handles the RDK B WiFi related functionality and
 * verification.
 * 
 * @author Selvaraj Mariyappan
 * @refactor Govardhan
 */
public class BroadBandWiFiUtils extends AutomaticsTestBase {

	/**
	 * Logger instance for {@link BroadBandWebPaAUtils}
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandWiFiUtils.class);

	private static String NON_ASCII_CHARACTERS_FROM_REMOTE_API_RESPONSE = "\\[1;32m";

	/**
	 * method to set webpa parameters
	 * 
	 * @param device Dut
	 * @param String paramter name of webpa parameter to be set
	 * @param String value value to which paramter is to be set
	 * @param int    dataType boolean-3 integer-2 string-0
	 * @author Govardhan
	 */

	public static boolean setWebPaParams(Dut device, String parameter, String value, int dataType) {
		LOGGER.debug("STARTING METHOD: AutomaticsTapAPI.setWebPaParams");
		WebPaParameter webPaParam = new WebPaParameter();
		webPaParam.setDataType(dataType);
		webPaParam.setName(parameter);
		webPaParam.setValue(value);

		List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
		webPaParameters.add(webPaParam);

		WebPaServerResponse webPaSetResponse = tapEnv.setWebPaParameterValues(device, webPaParameters);

		LOGGER.info("WebPA response : " + webPaSetResponse);
		LOGGER.debug("ENDING METHOD: AutomaticsTapAPI.setWebPaParams");

		if (webPaSetResponse.getMessage().trim().equalsIgnoreCase(BroadBandTestConstants.SUCCESS_TXT)) {
			return true;
		} else {
			LOGGER.error("Failed in setting webpaparameter " + parameter + " with value " + value + " with response -"
					+ webPaSetResponse);
			throw new TestException("Failed in setting webpaparameter " + parameter + " with value " + value
					+ " with response -" + webPaSetResponse);
		}
	}

	/**
	 * Utility method to verify WiFi broadcasted started after Factory Reboot
	 * 
	 * @param device Dut box instance.
	 * @return true, if "Wifi_Name_Broadcasted" message is present in atom console
	 *         file /rdklogs/logs/WiFilog.txt.0
	 * @author Govardhan
	 */
	public static boolean verifyWifiBroadCastedAfterFactoryReboot(Dut device) {
		LOGGER.debug("STARTING METHOD: verifyWifiBroadCastedAfterFactoryReboot()");
		boolean status = false;
		String command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
				BroadBandTraceConstants.LOG_MESSAGE_WIFI_NAME_BROADCASTED,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.LOCATION_WIFI_LOG);
		LOGGER.info("Command to valid Wifi Factory reset completion is -" + command);
		long startTime = System.currentTimeMillis();
		String response = null;
		do {
			response = CommonMethods.executeCommandInAtomConsole(device, tapEnv, command);
			status = CommonMethods.isNotNull(response)
					&& CommonMethods.patternMatcher(response, BroadBandTestConstants.PATTERN_MATCHER_WIFI_BROADCASTED);
			LOGGER.info("Response from atom console for WiFilog.txt.0 - " + response);
		} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.TEN_MINUTE_IN_MILLIS && !status
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.FIFTEEN_SECONDS_IN_MILLIS));
		LOGGER.debug("ENDING METHOD: verifyWifiBroadCastedAfterFactoryReboot()");
		return status;
	}

	/**
	 * 
	 * Helper method to verify the wifi mesh status from logs
	 * 
	 * @param device    {@link Dut}
	 * @param tapApi    {@link AutomaticsTapApi}
	 * @param isEnabled true if wifi mesh is enabled, else false
	 * @return true if logs are verified, else false
	 * @author Govardhan
	 */
	public static boolean verifyWifiMeshStatusFromLogs(Dut device, AutomaticsTapApi tapApi, boolean isEnabled,
			String deviceType) throws TestException {
		// stores the verification status
		boolean status = false;
		// stores the command list
		// stores the command response
		String response = null;
		// stores the expected output value
		String expectedOutput = null;
		try {
			// selecting the expected response based on whether wifi mesh is
			// enabled or not
			expectedOutput = isEnabled ? BroadBandTestConstants.MESHWIFI_ENABLED_LOG
					: BroadBandTestConstants.MESHWIFI_DISABLED_LOG;

			// The command to be executed within the device for checking the
			// logs
			String commandToBeExecuted = isEnabled ? BroadBandTestConstants.COMMAND_TO_READ_WIFIMESH_SERVICE_ENABLE_LOGS
					: BroadBandTestConstants.COMMAND_TO_READ_WIFIMESH_SERVICE_DISABLE_LOGS;

			// commands to be executed within the atom console including the
			// command to get logs and executing them
			// using remote ssh api
			long currentTime = System.currentTimeMillis();
			do {
				response = tapApi.executeCommandOnAtom(device, commandToBeExecuted);

				if (CommonMethods.isNotNull(response) && response.contains(expectedOutput)) {
					status = true;
					LOGGER.info("Wifi mesh service enabled logs are present in /rdklogs/logs/MeshAgentLog.txt.0");
				}
			} while (!status && (System.currentTimeMillis() - currentTime) < RDKBTestConstants.TWO_MINUTES);
		} catch (TestException exception) {
			throw new TestException(exception.getMessage());
		}
		return status;
	}

	/**
	 * Helper method to verify the mesh wifi param values using WebPa
	 * 
	 * @param device         {@link Dut}
	 * @param tapApi         {@link AutomaticsTapApi}
	 * @param meshParam      Mesh param to be validated
	 * @param expectedOutput the expected output
	 * @return true if the dmcli command output matches the expected output
	 * @author Govardhan
	 */
	public static boolean verifyMeshWifiParamValuesUsingWebPa(Dut device, AutomaticsTapApi tapApi, String meshParam,
			String expectedOutput) throws TestException {

		LOGGER.info("STARTING METHOD: verifyMeshWifiParamValuesUsingWebPa");
		boolean status = false;

		// executing the mesh param
		String response = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapApi, meshParam);

		if (CommonMethods.isNotNull(response)) {
			// verifying whether the dmcli command response matched with the
			// expected output
			status = CommonMethods.isNotNull(expectedOutput) && response.contains(expectedOutput);
			if (!status) {
				throw new TestException("The mesh param response " + response + "does not match the expected response "
						+ expectedOutput);
			}
		} else {
			throw new TestException("The param " + meshParam + " returned a null value using dmcli command!!");
		}

		LOGGER.info("ENDING METHOD: verifyMeshWifiParamValuesUsingWebPa");
		return status;
	}

	/**
	 * Helper method to verify the mesh service status using systemctl
	 * 
	 * @param device   {@link Dut}
	 * @param tapApi   {@link AutomaticsTapApi}
	 * @param isActive true if mesh service active status is to verified, else false
	 * @return true if status is verified successfully else false
	 * @throws TestException
	 * @author Govardhan
	 */
	public static boolean verifyMeshSerivceStatusUsingSystemctlFromAtomConsole(Dut device, AutomaticsTapApi tapApi,
			boolean isActive, String deviceType) throws TestException {
		// stores the verification status
		boolean status = false;
		// stores the command list
		String commandList = null;
		// stores the command response
		String response = null;
		// stores the expected output value
		String expectedOutput = null;

		try {
			// selecting the expected response based on whether wifi mesh is
			// enabled or not
			expectedOutput = isActive ? BroadBandTestConstants.SYSTEMCTL_ACTIVE_RESPONSE
					: BroadBandTestConstants.SYSTEMCTL_INACTIVE_RESPONSE;

			// commands to be executed within the atom console including the
			// mesh wifi service systemctl command
			commandList = BroadBandTestConstants.COMMAND_TO_CHECK_THE_RUNNING_STATUS_OF_WIFIMESH_SERVICE;

			response = tapApi.executeCommandOnAtom(device,
					BroadBandTestConstants.COMMAND_TO_CHECK_THE_RUNNING_STATUS_OF_WIFIMESH_SERVICE);

			if (!CommonMethods.isNotNull(response)) {
				response = tapApi.executeCommandUsingSsh(device,
						BroadBandTestConstants.COMMAND_TO_CHECK_THE_RUNNING_STATUS_OF_WIFIMESH_SERVICE);
			}

			LOGGER.debug("Remote Ssh API command response : " + response);

			if (CommonMethods.isNotNull(response)) {

				response = response.replaceAll(NON_ASCII_CHARACTERS_FROM_REMOTE_API_RESPONSE,
						RDKBTestConstants.EMPTY_STRING);
				// retrieving the status from systemctl response
				response = CommonMethods.patternFinder(response,
						BroadBandTestConstants.PATTERN_TO_GET_STATUS_FROM_SYSTEMCTL_RESPONSE);
				LOGGER.debug("systemctl meshwifi service status is: " + response);
				if (CommonMethods.isNotNull(response) && response.trim().equalsIgnoreCase(expectedOutput)) {
					status = true;
					LOGGER.info("systemctl command returned wifi mesh service is " + expectedOutput);
				} else {
					throw new TestException("wifi mesh service is NOT " + expectedOutput
							+ " as observed with systemctl command. The current status is " + response);
				}
			} else {
				throw new TestException("NULL response obtained after executing commands" + commandList
						+ " using remote ssh api in stb");
			}
		} catch (Exception exception) {
			throw new TestException(exception.getMessage());
		}
		return status;
	}

	/**
	 * Helper Method to Reactivate the Broadband Device using WebPA Parameters; this
	 * is not applicable for Captive Portal Scenario.
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 */
	public static void reactivateBroadBandDeviceWebPa(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.info("BEGIN BROADBAND DEVICE REACTIVATION.");
		boolean reactivationStatus = false;
		String hostMacAddress = device.getHostMacAddress();
		hostMacAddress = CommonMethods.isNotNull(hostMacAddress)
				? hostMacAddress.replaceAll(BroadBandTestConstants.DELIMITER_COLON, BroadBandTestConstants.EMPTY_STRING)
				: null;
		LOGGER.info("HOST MAC ADDRESS (FORMATTED): " + hostMacAddress);
		// Setting the 2.4GHz Private SSID Value.
		String paramValue = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_2_4_PRIVATE_SSID);
		if (CommonMethods.isNotNull(hostMacAddress) && CommonMethods.isNotNull(paramValue)) {
			paramValue = paramValue.replace("#HOST_MAC_ADDRESS#",
					hostMacAddress.substring(hostMacAddress.length() - BroadBandTestConstants.CONSTANT_4));
			paramValue = paramValue.replace(BroadBandTestConstants.DOUBLE_QUOTE, BroadBandTestConstants.EMPTY_STRING);
			reactivationStatus = BroadBandWiFiUtils.setWebPaParams(device,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_NAME, paramValue,
					BroadBandTestConstants.CONSTANT_0);
		}
		LOGGER.info("REACTIVATION STEP 1 - SETTING 2.4GHz PRIVATE SSID: " + reactivationStatus);
		// Setting the 5GHz Private SSID Value.
		tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		paramValue = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_5_PRIVATE_SSID);
		if (CommonMethods.isNotNull(hostMacAddress) && CommonMethods.isNotNull(paramValue)) {
			paramValue = paramValue.replace("#HOST_MAC_ADDRESS#",
					hostMacAddress.substring(hostMacAddress.length() - BroadBandTestConstants.CONSTANT_4));
			paramValue = paramValue.replace(BroadBandTestConstants.DOUBLE_QUOTE, BroadBandTestConstants.EMPTY_STRING);
			reactivationStatus = BroadBandWiFiUtils.setWebPaParams(device,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_NAME, paramValue,
					BroadBandTestConstants.CONSTANT_0);
		}
		LOGGER.info("REACTIVATION STEP 2 - SETTING 5GHz PRIVATE SSID: " + reactivationStatus);
		// Setting the 2.4GHz Private SSID Password.
		tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		paramValue = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_2_4_PRIVATE_SSID_PWD);
		if (CommonMethods.isNotNull(paramValue)) {
			paramValue = paramValue.replace(BroadBandTestConstants.DOUBLE_QUOTE, BroadBandTestConstants.EMPTY_STRING);
			reactivationStatus = BroadBandWiFiUtils.setWebPaParams(device,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2GHZ_SECURITY_KEYPASSPHRASE, paramValue,
					BroadBandTestConstants.CONSTANT_0);
		}
		LOGGER.info("REACTIVATION STEP 3 - SETTING 2.4GHz PRIVATE SSID PASSWORD: " + reactivationStatus);
		// Setting the 5GHz Private SSID Password.
		tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		paramValue = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_5_PRIVATE_SSID_PWD);
		if (CommonMethods.isNotNull(paramValue)) {
			paramValue = paramValue.replace(BroadBandTestConstants.DOUBLE_QUOTE, BroadBandTestConstants.EMPTY_STRING);
			reactivationStatus = BroadBandWiFiUtils.setWebPaParams(device,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5GHZ_SECURITY_KEYPASSPHRASE, paramValue,
					BroadBandTestConstants.CONSTANT_0);
		}
		LOGGER.info("REACTIVATION STEP 4 - SETTING 5GHz PRIVATE SSID PASSWORD: " + reactivationStatus);
		// Setting Wifi Config Status to FALSE
		tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		reactivationStatus = BroadBandWiFiUtils.setWebPaParams(device,
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_INFO_RDK_CENTRAL_CONFIGURE_WIFI,
				BroadBandTestConstants.FALSE, BroadBandTestConstants.CONSTANT_3);
		LOGGER.info("REACTIVATION STEP 5 - SETTING WIFI CONFIG STATUS: " + reactivationStatus);
		// Setting Captive Portal Enable to FALSE
		tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		reactivationStatus = BroadBandWiFiUtils.setWebPaParams(device,
				BroadBandWebPaConstants.WEBPA_PARAM_CAPTIVE_PORTAL_ENABLE, BroadBandTestConstants.FALSE,
				BroadBandTestConstants.CONSTANT_3);
		LOGGER.info("REACTIVATION STEP 6 - SETTING CAPTIVE PORTAL ENABLE STATUS: " + reactivationStatus);
		// Setting 2.4GHz 'Apply Setting' to TRUE
		tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		reactivationStatus = BroadBandWiFiUtils.setWebPaParams(device,
				BroadBandWebPaConstants.WEBPA_PARAM_WIFI_2_4_APPLY_SETTING, BroadBandTestConstants.TRUE,
				BroadBandTestConstants.CONSTANT_3);
		LOGGER.info("REACTIVATION STEP 7 - SETTING 2.4GHz 'APPLY SETTING' STATUS: " + reactivationStatus);
		// Setting 5GHz 'Apply Setting' to TRUE
		tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		reactivationStatus = BroadBandWiFiUtils.setWebPaParams(device,
				BroadBandWebPaConstants.WEBPA_PARAM_WIFI_5_APPLY_SETTING, BroadBandTestConstants.TRUE,
				BroadBandTestConstants.CONSTANT_3);
		LOGGER.info("REACTIVATION STEP 8 - SETTING 5GHz 'APPLY SETTING' STATUS: " + reactivationStatus);
		LOGGER.info("COMPLETE BROADBAND DEVICE REACTIVATION.");
	}

	/**
	 * Helper Method to Reactivate the Broadband Device using WebPA or SNMP
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 */
	public static boolean reactivateDeviceUsingWebpaOrSnmp(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("STARTING METHOD : reactivateDeviceUsingWebpaOrSnmp");
		boolean status = false;
		// Reactivation using WebPA
		try {
			// Checking if webpa process is up
			status = BroadBandWebPaUtils.verifyWebPaProcessIsUp(tapEnv, device, true);
			LOGGER.info("WEBPA PROCESS IS UP TO PERFORM DEVICE REACTIVATION : " + status);
			if (status) {
				status = false;
				LOGGER.info("WEBPA PROCESS IS UP, PERFORMING DEVICE REACTIVATION USING WEBPA.");
				reactivateDeviceUsingWebPa(tapEnv, device);
				status = true;
			}
		} catch (Exception exception) {
			LOGGER.error("FOLLOWING EXCEPTION OCCURED WHILE REACTIVATING THE DEVICE USING WEBPA : "
					+ exception.getMessage());
		}
		// Reactivation using SNMP
		try {
			if (!status) {
				LOGGER.error("UNABLE TO PERFORM REACTIVATION USING WEBPA. HENCE ATTEMPTING USING SNMP.");
				// Checking if SNMP process is up
				status = BroadBandSnmpUtils.checkSnmpIsUp(tapEnv, device);
				LOGGER.info("SNMP PROCESS IS UP TO PERFORM DEVICE REACTIVATION : " + status);
				if (status) {
					status = false;
					LOGGER.info("SNMP PROCESS IS UP, PERFORMING DEVICE REACTIVATION USING SNMP.");
					reactivateDeviceUsingSnmp(tapEnv, device);
					status = true;
				}
			}
		} catch (Exception exception) {
			LOGGER.error(
					"FOLLOWING EXCEPTION OCCURED WHILE REACTIVATING THE DEVICE USING SNMP : " + exception.getMessage());
		}
		// Log error in case of reactivation failure.
		if (!status) {
			LOGGER.error("UNABLE TO PERFORM DEVICE REACTIVATION USING WEBPA AS WELL AS SNMP.");
		}
		LOGGER.debug("ENDING METHOD : reactivateDeviceUsingWebpaOrSnmp");
		return status;
	}

	/**
	 * Utility method to reactivate the device using webPa by exeucting all
	 * parameters as a single command
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device The Dut under test
	 * @throws TestException Throws exception if failed to set any of the parameter
	 */
	public static void reactivateDeviceUsingWebPa(AutomaticsTapApi tapEnv, Dut device) throws TestException {
		LOGGER.debug("STARTING METHOD : reactivateDeviceUsingWebPa()");
		// Variable declaration starts
		String failureReason = "Failed to set the parameter(s) : ";
		boolean status = true;
		// Variable declaration ends
		try {
			// Create webPa parameter list for all parameters for device reactivation
			List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
			WebPaParameter webPaParam = null;

			// Setting the 2.4GHz Private SSID Value.
			String hostMacAddress = device.getHostMacAddress();
			hostMacAddress = CommonMethods.isNotNull(hostMacAddress)
					? hostMacAddress.replaceAll(BroadBandTestConstants.DELIMITER_COLON,
							BroadBandTestConstants.EMPTY_STRING)
					: null;
			String paramValue = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_2_4_PRIVATE_SSID);
			if (CommonMethods.isNotNull(hostMacAddress) && CommonMethods.isNotNull(paramValue)) {
				paramValue = paramValue.replace("#HOST_MAC_ADDRESS#",
						hostMacAddress.substring(hostMacAddress.length() - BroadBandTestConstants.CONSTANT_4));
				paramValue = paramValue.replace(BroadBandTestConstants.DOUBLE_QUOTE,
						BroadBandTestConstants.EMPTY_STRING);
				webPaParam = formulateWebPaParameter(
						BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_NAME, paramValue,
						WebPaDataTypes.STRING.getValue());
				webPaParameters.add(webPaParam);
			}

			// Setting the 5GHz Private SSID Value.
			paramValue = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_5_PRIVATE_SSID);
			if (CommonMethods.isNotNull(hostMacAddress) && CommonMethods.isNotNull(paramValue)) {
				paramValue = paramValue.replace("#HOST_MAC_ADDRESS#",
						hostMacAddress.substring(hostMacAddress.length() - BroadBandTestConstants.CONSTANT_4));
				paramValue = paramValue.replace(BroadBandTestConstants.DOUBLE_QUOTE,
						BroadBandTestConstants.EMPTY_STRING);
				webPaParam = formulateWebPaParameter(
						BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_NAME, paramValue,
						WebPaDataTypes.STRING.getValue());
				webPaParameters.add(webPaParam);
			}

			// Setting the 2.4GHz Private SSID Password.
			paramValue = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_2_4_PRIVATE_SSID_PWD);
			if (CommonMethods.isNotNull(paramValue)) {
				paramValue = paramValue.replace(BroadBandTestConstants.DOUBLE_QUOTE,
						BroadBandTestConstants.EMPTY_STRING);
				webPaParam = formulateWebPaParameter(
						BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2GHZ_SECURITY_KEYPASSPHRASE,
						paramValue, WebPaDataTypes.STRING.getValue());
				webPaParameters.add(webPaParam);
			}

			// Setting the 5GHz Private SSID Password.
			paramValue = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_5_PRIVATE_SSID_PWD);
			if (CommonMethods.isNotNull(paramValue)) {
				paramValue = paramValue.replace(BroadBandTestConstants.DOUBLE_QUOTE,
						BroadBandTestConstants.EMPTY_STRING);
				webPaParam = formulateWebPaParameter(
						BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5GHZ_SECURITY_KEYPASSPHRASE,
						paramValue, WebPaDataTypes.STRING.getValue());
				webPaParameters.add(webPaParam);
			}

			// Setting Wifi Config Status to FALSE
			webPaParam = formulateWebPaParameter(
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_INFO_RDK_CENTRAL_CONFIGURE_WIFI,
					BroadBandTestConstants.FALSE, WebPaDataTypes.BOOLEAN.getValue());
			webPaParameters.add(webPaParam);

			// Setting Captive Portal Enable to FALSE
			webPaParam = formulateWebPaParameter(BroadBandWebPaConstants.WEBPA_PARAM_CAPTIVE_PORTAL_ENABLE,
					BroadBandTestConstants.FALSE, WebPaDataTypes.BOOLEAN.getValue());
			webPaParameters.add(webPaParam);

			// Setting 2.4GHz 'Apply Setting' to TRUE
			webPaParam = formulateWebPaParameter(BroadBandWebPaConstants.WEBPA_PARAM_WIFI_2_4_APPLY_SETTING,
					BroadBandTestConstants.TRUE, WebPaDataTypes.BOOLEAN.getValue());
			webPaParameters.add(webPaParam);

			// Setting 5GHz 'Apply Setting' to TRUE
			webPaParam = formulateWebPaParameter(BroadBandWebPaConstants.WEBPA_PARAM_WIFI_5_APPLY_SETTING,
					BroadBandTestConstants.TRUE, WebPaDataTypes.BOOLEAN.getValue());
			webPaParameters.add(webPaParam);
			Map<String, String> deviceActivateWebPaResponse = tapEnv.executeMultipleWebPaSetCommands(device,
					webPaParameters);

			if (null != deviceActivateWebPaResponse && !deviceActivateWebPaResponse.isEmpty()) {
				for (String param : deviceActivateWebPaResponse.keySet()) {
					if (deviceActivateWebPaResponse.get(param)
							.equalsIgnoreCase(RDKBTestConstants.SNMP_RESPONSE_SUCCESS)) {
						LOGGER.info("Successfully set the parameter : " + param);
					} else {
						status = false;
						failureReason += param + "; ";
					}
				}
			} else {
				failureReason = "WEBPA RESPONDSE OBTAINED FROM 'AutomaticsTapApi.executeMultipleWebPaCommands' API IS EMPTY.";
				status = false;
			}
		} catch (Exception e) {
			status = false;
			failureReason = e.getMessage();
		}
		LOGGER.debug("ENDING METHOD : reactivateDeviceUsingWebPa()");
		if (!status) {
			LOGGER.error(failureReason);
			throw new TestException(failureReason);
		}
	}

	/**
	 * Helper method to formulate a webPsa parameter
	 * 
	 * @param parameter The Paramter name
	 * @param value     The value of the paramter
	 * @param dataType  The data type
	 * @return {@link WebPaParameter}
	 */
	public static WebPaParameter formulateWebPaParameter(String parameter, String value, int dataType) {
		WebPaParameter webPaParam = new WebPaParameter();
		webPaParam.setDataType(dataType);
		webPaParam.setName(parameter);
		webPaParam.setValue(value);
		return webPaParam;

	}

	/**
	 * Helper Method to Reactivate the Broadband Device using SNMP OIDs
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 */
	public static void reactivateDeviceUsingSnmp(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.info("BEGIN BROADBAND DEVICE REACTIVATION.");
		boolean reactivationStatus = false;
		String snmpOutput = null;
		String hostMacAddress = device.getHostMacAddress();
		hostMacAddress = CommonMethods.isNotNull(hostMacAddress)
				? hostMacAddress.replaceAll(BroadBandTestConstants.DELIMITER_COLON, BroadBandTestConstants.EMPTY_STRING)
				: null;
		LOGGER.info("HOST MAC ADDRESS (FORMATTED): " + hostMacAddress);

		long waitTime = BroadBandTestConstants.TWO_MINUTE_IN_MILLIS;
		// Setting the 2.4GHz Private SSID Value.
		tapEnv.waitTill(waitTime);
		String paramValue = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_2_4_PRIVATE_SSID);
		if (CommonMethods.isNotNull(hostMacAddress) && CommonMethods.isNotNull(paramValue)) {
			paramValue = paramValue.replace("#HOST_MAC_ADDRESS#",
					hostMacAddress.substring(hostMacAddress.length() - BroadBandTestConstants.CONSTANT_4));
			paramValue = paramValue.replace(BroadBandTestConstants.DOUBLE_QUOTE, BroadBandTestConstants.EMPTY_STRING);
			snmpOutput = BroadBandSnmpUtils.retrieveSnmpSetOutputWithGivenIndexOnRdkDevices(device, tapEnv,
					BroadBandSnmpMib.ECM_PRIVATE_WIFI_SSID_2_4.getOid(), SnmpDataType.STRING, paramValue,
					BroadBandSnmpMib.ECM_PRIVATE_WIFI_SSID_2_4.getTableIndex());
			reactivationStatus = CommonMethods.isNotNull(snmpOutput) && snmpOutput.equals(paramValue);
		}
		LOGGER.info("REACTIVATION STEP 1 - SETTING 2.4GHz PRIVATE SSID: " + reactivationStatus);

		// Setting the 5GHz Private SSID Value.
		tapEnv.waitTill(waitTime);
		paramValue = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_5_PRIVATE_SSID);
		if (CommonMethods.isNotNull(hostMacAddress) && CommonMethods.isNotNull(paramValue)) {
			paramValue = paramValue.replace("#HOST_MAC_ADDRESS#",
					hostMacAddress.substring(hostMacAddress.length() - BroadBandTestConstants.CONSTANT_4));
			paramValue = paramValue.replace(BroadBandTestConstants.DOUBLE_QUOTE, BroadBandTestConstants.EMPTY_STRING);
			snmpOutput = BroadBandSnmpUtils.retrieveSnmpSetOutputWithGivenIndexOnRdkDevices(device, tapEnv,
					BroadBandSnmpMib.ECM_PRIVATE_WIFI_SSID_5.getOid(), SnmpDataType.STRING, paramValue,
					BroadBandSnmpMib.ECM_PRIVATE_WIFI_SSID_5.getTableIndex());
			reactivationStatus = CommonMethods.isNotNull(snmpOutput) && snmpOutput.equals(paramValue);
		}
		LOGGER.info("REACTIVATION STEP 2 - SETTING 5GHz PRIVATE SSID: " + reactivationStatus);

		// Setting the 2.4GHz Private SSID Password.
		tapEnv.waitTill(waitTime);
		paramValue = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_2_4_PRIVATE_SSID_PWD);
		if (CommonMethods.isNotNull(paramValue)) {
			paramValue = paramValue.replace(BroadBandTestConstants.DOUBLE_QUOTE, BroadBandTestConstants.EMPTY_STRING);
			snmpOutput = BroadBandSnmpUtils.retrieveSnmpSetOutputWithGivenIndexOnRdkDevices(device, tapEnv,
					BroadBandSnmpMib.ECM_PRIVATE_WIFI_2_4_PASSPHRASE.getOid(), SnmpDataType.STRING, paramValue,
					BroadBandSnmpMib.ECM_PRIVATE_WIFI_2_4_PASSPHRASE.getTableIndex());
			reactivationStatus = CommonMethods.isNotNull(snmpOutput) && snmpOutput.equals(paramValue);
		}
		LOGGER.info("REACTIVATION STEP 3 - SETTING 2.4GHz PRIVATE SSID PASSWORD: " + reactivationStatus);

		// Setting the 5GHz Private SSID Password.
		tapEnv.waitTill(waitTime);
		paramValue = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_5_PRIVATE_SSID_PWD);
		if (CommonMethods.isNotNull(paramValue)) {
			paramValue = paramValue.replace(BroadBandTestConstants.DOUBLE_QUOTE, BroadBandTestConstants.EMPTY_STRING);
			snmpOutput = BroadBandSnmpUtils.retrieveSnmpSetOutputWithGivenIndexOnRdkDevices(device, tapEnv,
					BroadBandSnmpMib.ECM_PRIVATE_WIFI_5_PASSPHRASE.getOid(), SnmpDataType.STRING, paramValue,
					BroadBandSnmpMib.ECM_PRIVATE_WIFI_5_PASSPHRASE.getTableIndex());
			reactivationStatus = CommonMethods.isNotNull(snmpOutput) && snmpOutput.equals(paramValue);
		}
		LOGGER.info("REACTIVATION STEP 4 - SETTING 5GHz PRIVATE SSID PASSWORD: " + reactivationStatus);

		paramValue = BroadBandTestConstants.STRING_VALUE_TWO;
		// Setting Captive Portal Enable to FALSE
		tapEnv.waitTill(waitTime);
		snmpOutput = BroadBandSnmpUtils.retrieveSnmpSetOutputWithDefaultIndexOnRdkDevices(device, tapEnv,
				BroadBandSnmpMib.ECM_CAPTIVE_PORTAL_ENABLE.getOid(), SnmpDataType.INTEGER, paramValue);
		reactivationStatus = CommonMethods.isNotNull(snmpOutput) && snmpOutput.equals(paramValue);
		if (!reactivationStatus) {
			tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
			snmpOutput = BroadBandSnmpUtils.executeSnmpGetOnRdkDevices(tapEnv, device,
					BroadBandSnmpMib.ECM_CAPTIVE_PORTAL_ENABLE.getOid());
			reactivationStatus = CommonMethods.isNotNull(snmpOutput) && snmpOutput.equals(paramValue);
		}
		LOGGER.info("REACTIVATION STEP 6 - SETTING CAPTIVE PORTAL ENABLE STATUS: " + reactivationStatus);

		LOGGER.info("COMPLETE BROADBAND DEVICE REACTIVATION.");
	}

	/**
	 * Utility method to validate the captive portal mode using WebPA command.
	 * 
	 * @param device The settop to be validated
	 * @return true if captive portal enabled, need wifi personalization enabled and
	 *         configure wifi enabled. Otherwise false.
	 */
	public static boolean verifyCaptivePortalModeUsingWebPaCommand(AutomaticsTapApi tapEnv, Dut device) {

		// variable to store captive portal enabled status
		// boolean captivePortalEnabledStatus = false;
		// variable to store wifi configuration required status
		// boolean configureWiFiStatus = false;
		// variable to store wifi personalization required status
		// boolean wiFiNeedsPersonalizationStatus = false;
		// Map to store webpa response
		Map<String, String> commandResponse = new HashMap<String, String>();
		// variable to store captive portal enabled status
		boolean status = false;

		String[] parameters = { BroadBandWebPaConstants.WEBPA_PARAM_CAPTIVE_PORTAL_ENABLE,
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_INFO_RDK_CENTRAL_CONFIGURE_WIFI,
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_INFO_RDK_CENTRAL_WIFI_NEEDS_PERSONALIZATION };

		commandResponse = BroadBandWebPaUtils.getMultipleParameterValuesUsingWebPaOrDmcli(device, tapEnv, parameters);

		if (CommonMethods.isNotNull(commandResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_CAPTIVE_PORTAL_ENABLE))
				&& CommonMethods.isNotNull(
						commandResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_INFO_RDK_CENTRAL_CONFIGURE_WIFI))
				&& CommonMethods.isNotNull(commandResponse
						.get(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_INFO_RDK_CENTRAL_WIFI_NEEDS_PERSONALIZATION))) {

			boolean captivePortalEnabledStatus = Boolean
					.parseBoolean(commandResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_CAPTIVE_PORTAL_ENABLE));
			boolean configureWiFiStatus = Boolean.parseBoolean(
					commandResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_INFO_RDK_CENTRAL_CONFIGURE_WIFI));
			boolean wiFiNeedsPersonalizationStatus = Boolean.parseBoolean(commandResponse
					.get(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_INFO_RDK_CENTRAL_WIFI_NEEDS_PERSONALIZATION));
			status = captivePortalEnabledStatus && configureWiFiStatus && wiFiNeedsPersonalizationStatus;

		} else {
			throw new TestException("Failed to get the values for TR-181 parameter to check captive portal mode");
		}

		return status;
	}

	/**
	 * Method to get the difference between two time stamps in minutes
	 * 
	 * @param firstTime
	 * @param secondTime
	 * @return
	 */
	public static int getDifferenceInMinutes(String firstTime, String secondTime) {
		int diff = 0;
		if (CommonMethods.isNotNull(firstTime) && CommonMethods.isNotNull(secondTime)) {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			try {
				Date d1 = format.parse(firstTime);
				Date d2 = format.parse(secondTime);
				diff = (int) ((d2.getTime() - d1.getTime()) / (60 * 1000));
				LOGGER.info("Time difference: " + diff);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return diff;
	}

	/**
	 * Utility method to get the Wi-Fi SSID configuration status via WebPA or Dmcli.
	 * 
	 * @param tapEnv     {@link AutomaticsTapApi}
	 * @param device     {@link Dut}
	 * @param parameters The list of parameters to be queried.
	 * @return {@link WiFiSsidConfigStatus}
	 * @refactor Govardhan
	 */
	public static WiFiSsidConfigStatus getWiFiSsidConfigurationStatusViaWebPaOrDmcli(AutomaticsTapApi tapEnv,
			Dut device, List<String> parameters) {

		String[] parametersArray = new String[parameters.size()];
		parametersArray = parameters.toArray(parametersArray);

		Map<String, String> wifiSsidConfigStatus = BroadBandWebPaUtils
				.getMultipleParameterValuesUsingWebPaOrDmcli(device, tapEnv, parametersArray);
		WiFiSsidConfigStatus ssidConfigStatus = new WiFiSsidConfigStatus();

		for (String webPaParam : wifiSsidConfigStatus.keySet()) {
			String paramValue = wifiSsidConfigStatus.get(webPaParam);

			LOGGER.debug("Parameter name :  " + webPaParam + " , value : " + paramValue);
			boolean isEnabled = false;
			switch (webPaParam) {
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_NAME:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_NAME:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PUBLIC_SSID:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PUBLIC_SSID:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4GHZ_LNF_SSID:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_LNF_SSID:
			case BroadBandWebPaConstants.WEBPA_PARAM_5GHZ_SECURED_PUBLIC_WIFI_SSID:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_10004_SSID:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_10104_SSID:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5GHZ_EXTENDER_SSID:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4GHZ_EXTENDER_SSID:
				ssidConfigStatus.setName(paramValue);
				break;

			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_ENABLED_STATUS:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_ENABLED_STATUS:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PUBLIC_SSID_ENABLE_STATUS:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PUBLIC_SSID_ENABLE_STATUS:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_LNF_SSID_ENABLED:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_LNF_SSID_ENABLED:
			case BroadBandWebPaConstants.WEBPA_PARAM_5GHZ_SECURED_PUBLIC_WIFI_SSID_ENABLED:
			case BroadBandWebPaConstants.WEBPA_WAREHOUSE_WIRELESS_SSID1_ENABLE_LNF_2_4:
			case BroadBandWebPaConstants.WEBPA_WAREHOUSE_WIRELESS_SSID_ENABLE_LNF_5G:
			case "Device.WiFi.SSID.10007.Enable":
			case "Device.WiFi.SSID.10107.Enable":
				if (CommonMethods.isNotNull(paramValue)) {
					isEnabled = Boolean.parseBoolean(paramValue.trim());
				}
				ssidConfigStatus.setEnable(isEnabled);
				break;

			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_2_4GHZ_PRIVATE_SSID:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_5GHZ_PRIVATE_SSID:
			case "Device.WiFi.SSID.10003.Status":
			case "Device.WiFi.SSID.10103.Status":
			case "Device.WiFi.SSID.10105.Status":
			case "Device.WiFi.SSID.10006.Status":
			case "Device.WiFi.SSID.10106.Status":
			case "Device.WiFi.SSID.10004.Status":
			case "Device.WiFi.SSID.10104.Status":
			case "Device.WiFi.SSID.10107.Status":
			case "Device.WiFi.SSID.10007.Status":
				ssidConfigStatus.setStatus(paramValue);
				break;

			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_PRIVATE_SECURITY_MODEENABLED:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_PRIVATE_SECURITY_MODEENABLED:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_PUBLIC_SECURITY_MODEENABLED:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_PUBLIC_SECURITY_MODEENABLED:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_GHZ_LNF_SECURITY_MODEENABLED:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_LNF_SECURITY_MODEENABLED:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5GHZ_SECURED_PUBLICWIFI:
			case "Device.WiFi.AccessPoint.10004.Security.ModeEnabled":
			case "Device.WiFi.AccessPoint.10104.Security.ModeEnabled":
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4GHZ_EXTENDER_SECURITY_MODE:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5GHZ_EXTENDER_SECURITY_MODE:
				ssidConfigStatus.setSecurityMode(paramValue);
				break;

			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2GHZ_SECURITY_KEYPASSPHRASE:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5GHZ_SECURITY_KEYPASSPHRASE:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_10003_SECURITY_KEYPASSPHRASE:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_10103_SECURITY_KEYPASSPHRASE:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_10006_SECURITY_KEYPASSPHRASE:
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_10106_SECURITY_KEYPASSPHRASE:
			case "Device.WiFi.AccessPoint.10105.Security.KeyPassphrase":
			case "Device.WiFi.AccessPoint.10004.Security.KeyPassphrase":
			case "Device.WiFi.AccessPoint.10104.Security.KeyPassphrase":
			case "Device.WiFi.AccessPoint.10007.Security.KeyPassphrase":
			case "Device.WiFi.AccessPoint.10107.Security.KeyPassphrase":
				ssidConfigStatus.setPswd(paramValue);
				break;

			default:
				break;

			}
		}
		LOGGER.info(" WiFiSsidConfigStatus :  " + ssidConfigStatus.toString());

		return ssidConfigStatus;
	}

	/**
	 * method to test whether all the wifi ssid's like private wifi, public wifi are
	 * enabled or not Device.WiFi.SSID.10001.Enable , Device.WiFi.SSID.10101.Enable
	 * , Device.WiFi.SSID.10003.Enable, Device.WiFi.SSID.10103.Enable
	 * 
	 * returns 'true' if all the parameters are set to the 'status' that is passed
	 * as argument
	 * 
	 * Throws TestException even if one parameter is not set to the value that is
	 * passed as argument
	 * 
	 * @ AutomaticsTapApi tapEnv @ Dut device @ boolean status
	 * 
	 * @Refactor Sruthi Santhosh
	 */

	public static void verifyWiFiParameterStatus(AutomaticsTapApi tapEnv, Dut device, boolean status)
			throws TestException {
		LOGGER.debug("STARTING METHOD: verifyWiFiParameterStatus");
		String errorMessage = "";
		boolean privateSsid2GhzStatus = validateStatusUsingWebPa(tapEnv, device,
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_ENABLE, status);
		LOGGER.info("Private SSID enable status for 2.4GHz: " + privateSsid2GhzStatus);
		if (!privateSsid2GhzStatus) {
			errorMessage += "Failed to verify the status of private SSID for 2.4Ghz. Expected : " + status
					+ "; Actual : " + privateSsid2GhzStatus;
		}
		boolean privateSsid5GhzStatus = validateStatusUsingWebPa(tapEnv, device,
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_ENABLED_STATUS, status);
		LOGGER.info("Private SSID enable status for 5GHz: " + privateSsid5GhzStatus);
		if (!privateSsid5GhzStatus) {
			errorMessage += "Failed to verify the status of private SSID for 5Ghz. Expected : " + status + "; Actual : "
					+ privateSsid5GhzStatus + errorMessage;
		}

		boolean publicWifi5GhzStatus = validateStatusUsingWebPa(tapEnv, device,
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PUBLIC_SSID_ENABLE_STATUS, status);
		LOGGER.info("Public WiFi status for 5GHz: " + publicWifi5GhzStatus);
		if (!publicWifi5GhzStatus) {
			errorMessage += "Failed to verify the status of public WiFi for 5Ghz. Expected : " + status + "; Actual : "
					+ publicWifi5GhzStatus + errorMessage;
		}
		if (!DeviceModeHandler.isBusinessClassDevice(device)) {
			if (!(privateSsid2GhzStatus && privateSsid5GhzStatus && publicWifi5GhzStatus)) {
				LOGGER.error(errorMessage);
				throw new TestException(errorMessage);
			}
		} else {
			if (!(privateSsid2GhzStatus && privateSsid5GhzStatus && publicWifi5GhzStatus)) {
				LOGGER.error(errorMessage);
				throw new TestException(errorMessage);
			}
		}
		LOGGER.debug("ENDING METHOD: verifyWiFiParameterStatus");
	}

	/**
	 * Utility method to validate the WiFi Status
	 * 
	 * @param tapEnv         {@link AutomaticsTapApi}
	 * @param device         The device under test
	 * @param webPaConstant  The webPA parameter to be validated
	 * @param expectedStatus The expected value of the webPa Paramter
	 * @throws TestException Throws exception if the validation failed.
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean validateStatusUsingWebPa(AutomaticsTapApi tapEnv, Dut device, String webPaConstant,
			Boolean expectedStatus) {
		boolean validationStatus = false;
		String response = tapEnv.executeWebPaCommand(device, webPaConstant);
		if (CommonMethods.isNotNull(response)) {
			boolean parameterStatus = Boolean.parseBoolean(response.trim());
			validationStatus = (parameterStatus == expectedStatus);
		} else {
			throw new TestException("Null response obtained for parameter:" + webPaConstant);
		}

		return validationStatus;
	}

	/**
	 * Method to enable 2.4 Ghz and 5Ghz private wifi ssids
	 * 
	 * 
	 * Expected Wifi Types: 1.BroadBandTestConstants.PRIVATE_WIFI_TYPE
	 * 
	 * @param Dut      device
	 * 
	 * @param tapEnv   {@link AutomaticsTapApi} Reference
	 * 
	 * @param WifiType type of wifi which has to be enabled whether private
	 *                 wifi[BroadBandTestConstants.PRIVATE_WIFI_TYPE]
	 * @return status of enabling the parameters succesfully
	 * 
	 *         wait for one minute in script after enabling wifi parameters
	 * 
	 * @author Revath Kumar Vella
	 * @Refactor Sruthi Santhosh
	 * 
	 */
	public static boolean enableBoth2And5GhzWiFiSsidsUsingWebPaCommand(Dut device, AutomaticsTapApi tapEnv,
			String WifiType) {
		boolean statusFor2GhzEnable = false;
		boolean statusFor5GhzEnable = false;
		boolean status = false;
		String errorMessage = "Failed in enabling Private SSID in 2.4Ghz and 5Ghz bands\n";
		LOGGER.debug("STARTING METHOD: enableBoth2And5GhzWiFiSsidsUsingWebPaCommand");

		Map<String, String> mapOfPrivateWifiParametersIn2And5Ghz = new HashMap<String, String>();
		mapOfPrivateWifiParametersIn2And5Ghz.put(BroadBandTestConstants.BAND_2_4GHZ,
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_ENABLED_STATUS);
		mapOfPrivateWifiParametersIn2And5Ghz.put(BroadBandTestConstants.BAND_5GHZ,
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_ENABLED_STATUS);

		Map<String, Map<String, String>> mapFromWifiTypeToCorrespondingParameters = new HashMap<String, Map<String, String>>();
		mapFromWifiTypeToCorrespondingParameters.put(BroadBandTestConstants.PRIVATE_WIFI_TYPE,
				mapOfPrivateWifiParametersIn2And5Ghz);

		WebPaParameter webPaParameterIn2Ghz = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
				mapFromWifiTypeToCorrespondingParameters.get(WifiType).get(BroadBandTestConstants.BAND_2_4GHZ),
				AutomaticsConstants.TRUE, WebPaDataTypes.BOOLEAN.getValue());
		WebPaParameter webPaParameterIn5Ghz = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
				mapFromWifiTypeToCorrespondingParameters.get(WifiType).get(BroadBandTestConstants.BAND_5GHZ),
				AutomaticsConstants.TRUE, WebPaDataTypes.BOOLEAN.getValue());
		List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
		webPaParameters.add(webPaParameterIn2Ghz);
		webPaParameters.add(webPaParameterIn5Ghz);
		Map<String, String> serverResponse = tapEnv.executeMultipleWebPaSetCommands(device, webPaParameters);
		try {
			statusFor2GhzEnable = serverResponse
					.get(mapFromWifiTypeToCorrespondingParameters.get(WifiType).get(BroadBandTestConstants.BAND_2_4GHZ))
					.equalsIgnoreCase(BroadBandTestConstants.SUCCESS_TXT);
		} catch (NullPointerException exception) {
			errorMessage += "Successmessage is obtained as null from webpa server for parameter  :"
					+ mapFromWifiTypeToCorrespondingParameters.get(WifiType).get(BroadBandTestConstants.BAND_2_4GHZ)
					+ "\n";

		}
		try {
			statusFor5GhzEnable = serverResponse
					.get(mapFromWifiTypeToCorrespondingParameters.get(WifiType).get(BroadBandTestConstants.BAND_5GHZ))
					.equalsIgnoreCase(BroadBandTestConstants.SUCCESS_TXT);
		} catch (NullPointerException exception) {
			errorMessage += "Successmessage is obtained as null from webpa server for parameter  :"
					+ mapFromWifiTypeToCorrespondingParameters.get(WifiType).get(BroadBandTestConstants.BAND_5GHZ)
					+ "\n";

		}
		status = statusFor2GhzEnable && statusFor5GhzEnable;
		if (!status) {
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}
		LOGGER.debug("ENDING METHOD: enableBoth2And5GhzWiFiSsidsUsingWebPaCommand");
		return status;

	}

	/**
	 * Method to verify the IPv4 and IPv6 connection interface & Internet
	 * connectivity for 2.4/5 GHz .
	 * 
	 * @param device          {@link Dut}
	 * @param testId          Test case ID
	 * @param deviceConnected Device Connected
	 * @param stepNumber      Step Number
	 * @Refactor Sruthi Santhosh
	 */
	public static void verifyIpv4AndIpV6ConnectionInterface(Dut device, String testId, Dut deviceConnected,
			int stepNumber) throws TestException {
		LOGGER.debug("STARTING METHOD : verifyIpv4AndIpV6ConnectionInterface()");
		String step = null;
		boolean status = false;
		String errorMessage = null;
		BroadBandResultObject result = null;
		//
		boolean isSystemdPlatforms = false;
		isSystemdPlatforms = DeviceModeHandler.isFibreDevice(device);
		LOGGER.info("Gateway device model is:" + isSystemdPlatforms);
		//
		try {
			String osType = ((Device) deviceConnected).getOsType();
			String model = ((Device) deviceConnected).getModel();
			/**
			 * STEP : VERIFY THE CORRECT IPV4 ADDRESS FOR CONNECTED CLIENT
			 */
			step = "S" + stepNumber;
			status = false;
			LOGGER.info("***************************************************************************************");
			LOGGER.info("STEP " + stepNumber
					+ ": DESCRIPTION :VERIFY THE CORRECT IPV4 ADDRESS FOR CONNECTED CLIENT DEVICE MODEL " + model);
			LOGGER.info("STEP " + stepNumber
					+ ": ACTION : EXECUTE COMMAND, WINDOWS : ipconfig |grep -A 10 'Wireless LAN adapter Wi-Fi' |grep -i 'IPv4 Address' or LINUX : ifconfig | grep 'inet' or ON THE CONNECTED CLIENT");
			LOGGER.info("STEP " + stepNumber
					+ ": EXPECTED : IT SHOULD RETURN THE CORRECT IPV4 ADDRESS FOR DEVICE MODEL " + model);
			LOGGER.info("***************************************************************************************");
			errorMessage = "UNABLE TO GET THE CORRECT IPV4 ADDRESS FROM CLIENT MODEL " + model;
			long startTime = System.currentTimeMillis();
			do {
				status = BroadBandConnectedClientUtils
						.verifyIpv4AddressForWiFiOrLanInterfaceConnectedWithRdkbDevice(osType, deviceConnected, tapEnv);
			} while (!status && (System.currentTimeMillis() - startTime) < BroadBandTestConstants.THREE_MINUTE_IN_MILLIS
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
			if (status) {
				LOGGER.info("STEP " + stepNumber
						+ " : ACTUAL : SUCCESSFYLLY VERIFIED CORRECT IPV4 ADDRESS FROM CLIENT DEVICE MODEL : " + model);
			} else {
				LOGGER.error("STEP " + stepNumber + " : ACTUAL : " + errorMessage);
			}
			LOGGER.info("***************************************************************************************");
			tapEnv.updateExecutionStatus(device, testId, step, status, errorMessage, false);

			/**
			 * STEP : VERIFY THE CORRECT IPV6 ADDRESS FOR CONNECTED CLIENT
			 */
			stepNumber++;
			step = "S" + stepNumber;
			status = false;
			if (BroadbandPropertyFileHandler.isIpv6Enabled()) {
				LOGGER.info("***************************************************************************************");
				LOGGER.info("STEP " + stepNumber
						+ ": DESCRIPTION :VERIFY THE CORRECT IPV6 ADDRESS FOR CONNECTED CLIENT DEVICE MODEL " + model);
				LOGGER.info("STEP " + stepNumber
						+ ": ACTION : EXECUTE COMMAND, WINDOWS : ipconfig |grep -A 10 'Wireless LAN adapter Wi-Fi' |grep -i 'IPv6 Address' or LINUX : ifconfig | grep 'inet6 ' ON THE CONNECTED CLIENT");
				LOGGER.info("STEP " + stepNumber
						+ ": EXPECTED : IT SHOULD RETURN THE CORRECT IPV4 ADDRESS FOR DEVICE MODEL " + model);
				LOGGER.info("***************************************************************************************");
				errorMessage = "UNABLE TO GET THE CORRECT IPV6 ADDRESS FROM CLIENT MODEL " + model;

				if (!isSystemdPlatforms) {
					startTime = System.currentTimeMillis();
					do {
						status = BroadBandConnectedClientUtils
								.verifyIpv6AddressForWiFiOrLanInterfaceConnectedWithRdkbDevice(osType, deviceConnected,
										tapEnv);
					} while (!status
							&& (System.currentTimeMillis() - startTime) < BroadBandTestConstants.THREE_MINUTE_IN_MILLIS
							&& BroadBandCommonUtils.hasWaitForDuration(tapEnv,
									BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
					if (status) {
						LOGGER.info("STEP " + stepNumber
								+ " : ACTUAL : SUCCESSFYLLY VERIFIED CORRECT IPV6 ADDRESS FROM CLIENT DEVICE MODEL : "
								+ model);
					} else {
						LOGGER.error("STEP " + stepNumber + " : ACTUAL : " + errorMessage);
					}
					tapEnv.updateExecutionStatus(device, testId, step, status, errorMessage, false);
				} else {
					tapEnv.updateExecutionForAllStatus(device, testId, step, ExecutionStatus.NOT_APPLICABLE,
							BroadBandTestConstants.FIBRE_NOT_APPLICABLE_IPV6, false);
				}
				LOGGER.info("***************************************************************************************");
			} else {
				LOGGER.info("IPv6 is not available/disabled : Skipping Step 13 ...");
				tapEnv.updateExecutionForAllStatus(device, testId, step, ExecutionStatus.NOT_APPLICABLE, errorMessage,
						false);
			}

			/**
			 * STEP : VERIFY THE INTERNET CONNECTIVITY IN THE CLIENT WITH GIVEN GHZ SSID
			 * INTERFACE USING IPV4 .
			 */
			stepNumber++;
			step = "S" + stepNumber;
			status = false;
			LOGGER.info("***************************************************************************************");
			LOGGER.info("STEP " + stepNumber
					+ ": DESCRIPTION : VERIFY THE INTERNET CONNECTIVITY IN THE CLIENT CONNECTED USING IPV4 FOR DEVICE MODEL : "
					+ model);
			LOGGER.info("STEP " + stepNumber
					+ ": ACTION : EXECUTE COMMAND, WINDOWS : curl -4 -v 'www.google.com'  | grep '200 OK' OR ping -4 -n 5 google.com, LINUX : curl -4 -f --interface <interfaceName> www.google.com | grep '200 OK' OR ping -4 -n 5 google.com ON THE CONNECTED CLIENT");
			LOGGER.info("STEP " + stepNumber
					+ ": EXPECTED : THE INTERNET CONNECTIVITY MUST BE AVAILABLE INTERFACE USING IPV4 ");
			LOGGER.info("***************************************************************************************");
			errorMessage = "NOT ABLE TO ACCESS THE SITE 'www.google.com' FROM CONNECTED CLIENT WITH USING IPV4 FOR DEVICE MODEL : "
					+ model;
			result = BroadBandConnectedClientUtils.verifyInternetIsAccessibleInConnectedClientUsingCurl(tapEnv,
					deviceConnected,
					BroadBandTestConstants.URL_HTTPS + BroadBandTestConstants.STRING_GOOGLE_HOST_ADDRESS,
					BroadBandTestConstants.IP_VERSION4);
			status = result.isStatus();
			errorMessage = result.getErrorMessage();
			if (!status) {
				errorMessage = "PING OPERATION FAILED TO ACCESS THE SITE 'www.google.com' USING IPV4 ";
				status = ConnectedNattedClientsUtils.verifyPingConnectionForIpv4AndIpv6(deviceConnected, tapEnv,
						BroadBandTestConstants.PING_TO_GOOGLE, BroadBandTestConstants.IP_VERSION4);
			}
			if (status) {
				LOGGER.info("STEP " + stepNumber
						+ " : ACTUAL : CONNECTED CLIENT HAS INTERNET CONNECTIVITY USING IPV4 FOR DEVICE MODEL : "
						+ model);
			} else {
				LOGGER.error("STEP " + stepNumber + " : ACTUAL : " + errorMessage);
			}
			LOGGER.info("***************************************************************************************");
			tapEnv.updateExecutionStatus(device, testId, step, status, errorMessage, false);

			/**
			 * STEP : VERIFY THE INTERNET CONNECTIVITY IN THE CLIENT WITH GIVEN GHZ SSID
			 * INTERFACE USING IPV6 .
			 */
			stepNumber++;
			step = "S" + stepNumber;
			status = false;
			if (BroadbandPropertyFileHandler.isIpv6Enabled()) {
				LOGGER.info("***************************************************************************************");
				LOGGER.info("STEP " + stepNumber
						+ ": DESCRIPTION : VERIFY THE INTERNET CONNECTIVITY IN THE CLIENT CONNECTED USING IPV6 FOR DEVICE MODEL : "
						+ model);
				LOGGER.info("STEP " + stepNumber
						+ ": ACTION : EXECUTE COMMAND, WINDOWS : curl -6 -v 'www.google.com' | grep '200 OK' OR ping -6 -n 5 google.com , LINUX : curl -6 -f --interface <interfaceName> www.google.com | grep '200 OK' OR ping -6 -n 5 google.com ON THE CONNECTED CLIENT");
				LOGGER.info("STEP " + stepNumber
						+ ": EXPECTED : THE INTERNET CONNECTIVITY MUST BE AVAILABLE INTERFACE USING IPV4 ");
				LOGGER.info("***************************************************************************************");

				if (!isSystemdPlatforms) {
					errorMessage = "NOT ABLE TO ACCESS THE SITE 'www.google.com' FROM CONNECTED CLIENT WITH USING IPV6 FOR DEVICE MODEL : "
							+ model;
					result = BroadBandConnectedClientUtils.verifyInternetIsAccessibleInConnectedClientUsingCurl(tapEnv,
							deviceConnected,
							BroadBandTestConstants.URL_HTTPS + BroadBandTestConstants.STRING_GOOGLE_HOST_ADDRESS,
							BroadBandTestConstants.IP_VERSION6);
					status = result.isStatus();
					errorMessage = result.getErrorMessage();
					if (!status) {
						errorMessage = "PING OPERATION FAILED TO ACCESS THE SITE 'www.google.com' USING IPV6 ";
						status = ConnectedNattedClientsUtils.verifyPingConnectionForIpv4AndIpv6(deviceConnected, tapEnv,
								BroadBandTestConstants.PING_TO_GOOGLE, BroadBandTestConstants.IP_VERSION6);
					}
					if (status) {
						LOGGER.info("STEP " + stepNumber
								+ " : ACTUAL : CONNECTED CLIENT HAS INTERNET CONNECTIVITY USING IPV6 FOR DEVICE MODEL : "
								+ model);
					} else {
						LOGGER.error("STEP " + stepNumber + " : ACTUAL : " + errorMessage);
					}
					tapEnv.updateExecutionStatus(device, testId, step, status, errorMessage, false);
				} else {
					tapEnv.updateExecutionForAllStatus(device, testId, step, ExecutionStatus.NOT_APPLICABLE,
							BroadBandTestConstants.FIBRE_NOT_APPLICABLE_IPV6, false);
				}
				LOGGER.info("***************************************************************************************");
			} else {
				LOGGER.info("IPv6 is not available/disabled : Skipping Step 6 ...");
				tapEnv.updateExecutionForAllStatus(device, testId, step, ExecutionStatus.NOT_APPLICABLE, errorMessage,
						false);
			}

		} catch (TestException e) {
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}
		LOGGER.debug("ENDING METHOD : verifyIpv4AndIpV6ConnectionInterface()");
	}

	/**
	 * Method to assign gateway IP based on device type
	 * 
	 * @param device
	 * @return gateway ip
	 */

	public static String getGatewayIpOfDevice(Dut device) {
		String gatewayIp = DeviceModeHandler.isBusinessClassDevice(device)
				? AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.PROP_KEY_BUSINESS_CLASS_GATEWAYIP)
				: AutomaticsPropertyUtility
						.getProperty(BroadBandPropertyKeyConstants.PROP_KEY_STRING_RESIDENTIAL_CLASS_GATEWAYIP);
		if (DeviceModeHandler.isDSLDevice(device)) {
			gatewayIp = BroadBandTestConstants.LAN_LOCAL_IP;
		}
		return gatewayIp;
	}

	public static boolean checkAndSetPublicWifi(Dut device, AutomaticsTapApi tapEnv) {

		String publicWifiValue = null;
		boolean reactivationStatus = false;

		try {
			// get the property value from stb.properties
			LOGGER.info(" Going to get property value for " + BroadBandTestConstants.KEY_FOR_PUBLIC_WIFI_WHITELISTING);
			publicWifiValue = AutomaticsTapApi
					.getSTBPropsValue(BroadBandTestConstants.KEY_FOR_PUBLIC_WIFI_WHITELISTING);
			LOGGER.info("Public wifi is set as " + publicWifiValue + " in stb.properties");
			reactivationStatus = BroadBandWebPaUtils.setAndVerifyParameterValuesUsingWebPaorDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_ENABLING_PUBLIC_WIFI, WebPaDataTypes.BOOLEAN.getValue(),
					publicWifiValue);
			if (reactivationStatus) {
				LOGGER.info("Sucessfully set Public wifi as " + publicWifiValue);

			} else {
				LOGGER.info("Failed to set Public wifi as " + publicWifiValue + " as part of reactivation ");
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while checking publicWifiValue from stb.properties ." + e.getMessage());

		}
		return reactivationStatus;
	}

	/**
	 * Method to frame command to ping based on OS.
	 * 
	 * @param connectedSetop
	 * @param gatewayIp
	 * @return command
	 */
	public static String commandToPingBasedOnTypeOfOs(Dut connectedDevice, String gatewayIp) {
		String command = null;
		if (connectedDevice != null) {
			Device connectedClientDevice = (Device) connectedDevice;
			if (connectedClientDevice.isLinux()) {
				command = BroadBandTestConstants.STRING_PING_TO_LINUX.replace("<IPADDRESS>", gatewayIp);
			} else if (connectedClientDevice.isWindows()) {
				command = BroadBandTestConstants.STRING_PING_TO_WINDOWS.replace("<IPADDRESS>", gatewayIp);
			}
		}
		return command;
	}

	/**
	 * Utility method to validate the allowed number of clients based on residential
	 * or commercial device.
	 * 
	 * @param device {@link Dut}
	 * 
	 * @param output value allowed No of clients retrieved using snmp/webpa.
	 * 
	 * @return true if the output matches with the expected value
	 * @refactor Athira
	 */
	public static boolean validateAllowedNoOfClientsValueRetrievedFromWebPA(Dut device, String output) {
		boolean status = false;
		String errorMessage = "Null value retrieved for the Device model";
		String deviceModel = device.getModel();

		String allowedNoOfClients = null;
		try {
			if (CommonMethods.isNotNull(deviceModel)) {
				try {
					// The default number of allowed clients is 15 for business gateway devices.
					allowedNoOfClients = BroadbandPropertyFileHandler.getAutomaticsPropsValueByResolvingPlatform(device,
							BroadBandPropertyKeyConstants.PROP_KEY_ALLOWED_NOOFCLIENTS);

				} catch (Exception e) {
					allowedNoOfClients = BroadBandTestConstants.STRING_VALUE_FIVE;
					LOGGER.info("No device specific value found allowed number of clients ");
				}
			}
			status = output.equals(allowedNoOfClients);

		} catch (Exception exception) {
			errorMessage = "Exception occured during execution : " + exception.getMessage();
			LOGGER.error(errorMessage);
		}
		return status;
	}

	/**
	 * Utils method to frame grep command to check snmp, webPa & Tr-069 Set & Get
	 * operations in logs
	 * 
	 * @param Parameter
	 * @param value
	 * @param logFileName
	 * @param commandType
	 * @return the framed grep command
	 * @refactor Athira
	 */
	public static String generateGrepForProtocolOperationType(String Parameter, String value, String logFileName,
			ProtocolOperationTypeEnum protocalOperationType, boolean isAtom) {
		String grepCommand = null;
		try {
			switch (protocalOperationType) {
			case SNMP_SET:
				grepCommand = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND,
						BroadBandTestConstants.DOUBLE_QUOTE,
						BroadBandTraceConstants.SNMP_LOG_MESSAGE_SET_CALLED_FOR_PARAM,
						BroadBandTestConstants.DOUBLE_QUOTE, BroadBandTestConstants.SINGLE_SPACE_CHARACTER, logFileName,
						BroadBandTestConstants.SINGLE_SPACE_CHARACTER, "|",
						BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.GREP_COMMAND, Parameter);
				break;
			case SNMP_GET:
				grepCommand = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND,
						BroadBandTestConstants.DOUBLE_QUOTE,
						BroadBandTraceConstants.SNMP_LOG_MESSAGE_GET_CALLED_FOR_PARAM,
						BroadBandTestConstants.DOUBLE_QUOTE, BroadBandTestConstants.SINGLE_SPACE_CHARACTER, logFileName,
						BroadBandTestConstants.SINGLE_SPACE_CHARACTER, "|",
						BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.GREP_COMMAND, Parameter);
				break;
			case TR69_GET:
			case TR69_SET:
				grepCommand = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND,
						BroadBandTestConstants.DOUBLE_QUOTE, Parameter, BroadBandTestConstants.DOUBLE_QUOTE,
						BroadBandTestConstants.SINGLE_SPACE_CHARACTER, logFileName);
				break;

			case WEBPA_SET:
				grepCommand = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND,
						BroadBandTestConstants.DOUBLE_QUOTE, BroadBandTestConstants.SET_REQUEST,
						BroadBandTestConstants.DOUBLE_QUOTE, BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
						logFileName);
				break;

			case WEBPA_GET:
				grepCommand = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND,
						BroadBandTestConstants.DOUBLE_QUOTE, BroadBandTestConstants.GET,
						BroadBandTestConstants.DOUBLE_QUOTE, BroadBandTestConstants.SINGLE_SPACE_CHARACTER, logFileName,
						BroadBandTestConstants.SINGLE_SPACE_CHARACTER, "|",
						BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.GREP_COMMAND,
						BroadBandTestConstants.DOUBLE_QUOTE, Parameter, BroadBandTestConstants.DOUBLE_QUOTE);
				break;

			default:
				LOGGER.error("INVALID COMMAND TYPE PASSED AS PARAMETER: " + protocalOperationType);
				break;
			}

		} catch (Exception exception) {
			LOGGER.error("Exception occurred framing grep Command " + exception.getMessage());
		}
		return grepCommand;
	}

	/**
	 * Utils method to execute grep command in device console and verify the logs
	 * with execution timestamp
	 * 
	 * @param tapEnv
	 * @param device
	 * @param dateResponse
	 * @param commandList
	 * @return true if the log generated after the execution
	 * @refactor Athira
	 */
	public static boolean grepLogAndVerifyUsingTimeStamp(AutomaticsTapApi tapEnv, Dut device, String dateResponse,
			String command, boolean isAtom) {
		boolean result = false;

		try {
			String response = null;

			// BroadBandCommonUtils.executeCommandInAtomConsole

			long startTime = System.currentTimeMillis();
			do {
				response = isAtom ? CommonMethods.executeCommandInAtomConsole(device, tapEnv, command)
						: tapEnv.executeCommandUsingSsh(device, command);
			} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.THREE_MINUTE_IN_MILLIS
					&& !(CommonMethods.isNotNull(dateResponse)));

			SimpleDateFormat logMessageDateTimeFormat = new SimpleDateFormat(
					BroadBandTestConstants.TIMESTAMP_FORMAT_LOG_MESSAGE);

			Date dtCapturedDateTime = null;
			try {
				dtCapturedDateTime = CommonMethods.isNotNull(dateResponse)
						? logMessageDateTimeFormat.parse(dateResponse.trim())
						: null;
				LOGGER.info("CAPTURED TIMESTAMP (DATE INSTANCE): " + dtCapturedDateTime);
			} catch (ParseException parseException) {
				LOGGER.error(dateResponse + " COULD NOT BE PARSED: " + parseException.getMessage());
			}
			List<String> logMessageTimeStamps = new ArrayList<>();
			// Retrieve the TimeStamps from the log message
			logMessageTimeStamps = CommonMethods.isNotNull(response)
					? CommonMethods.patternFinderToReturnAllMatchedString(response, "(\\d{6}-\\d{2}:\\d{2}:\\d{2})")
					: null;
			if (null != logMessageTimeStamps && !logMessageTimeStamps.isEmpty()) {
				for (String strLogMessageDateTime : logMessageTimeStamps) {
					Date logMessageDateTime = null;
					try {
						logMessageDateTime = CommonMethods.isNotNull(strLogMessageDateTime)
								? logMessageDateTimeFormat.parse(strLogMessageDateTime)
								: null;
						LOGGER.info("LOG MESSAGE TIMESTAMP (DATE INSTANCE):: " + logMessageDateTime);
					} catch (ParseException parseException) {
						LOGGER.error(strLogMessageDateTime + " COULD NOT BE PARSED: " + parseException.getMessage());
					}
					// Convert the captured DateTime to MilliSeconds & Compare them
					result = null != dtCapturedDateTime && null != logMessageDateTime
							&& dtCapturedDateTime.getTime() < logMessageDateTime.getTime();
					if (result) {
						break;
					}
				}
			}
		} catch (Exception exception) {
			LOGGER.error(
					"Exception occurred while getting the logs and time validating them: " + exception.getMessage());
		}
		return result;
	}

	/**
	 * Method to modify private wifi SSID and password for 2.4 and 5 Ghz
	 * 
	 * @param device
	 * @refactor Athira
	 */

	public static boolean changePrivateWiFiSsidAndPassphraseFor24And5Ghz(Dut device) {
		boolean setResult = false;

		List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
		Map<String, String> webpaResponse = null;
		String ssid_24 = BroadBandTestConstants.APPEND_STRING_FOR_24_GHZ_SSID_NAME_CHANGE
				.concat(Long.toString(System.currentTimeMillis()));
		String ssid_5 = BroadBandTestConstants.APPEND_STRING_FOR_5_GHZ_SSID_NAME_CHANGE
				.concat(Long.toString(System.currentTimeMillis()));
		LOGGER.info("The SSIDs for 2.4 and 5 GHz private wifi: 2.4 GHz - " + ssid_24 + " 5 GHz - " + ssid_5);
		webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_NAME, ssid_24,
				WebPaDataTypes.STRING.getValue()));
		webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_NAME, ssid_5,
				WebPaDataTypes.STRING.getValue()));
		webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
				BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_PRIVATE_SSID_2GHZ_PASSPHRASE,
				(AutomaticsPropertyUtility
						.getProperty(BroadBandPropertyKeyConstants.PRIVATE_WIFI_PASSPHRASE_BAND_STEERING)),
				WebPaDataTypes.STRING.getValue()));
		webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
				BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_PRIVATE_SSID_5GHZ_PASSPHRASE,
				(AutomaticsPropertyUtility
						.getProperty(BroadBandPropertyKeyConstants.PRIVATE_WIFI_PASSPHRASE_BAND_STEERING)),
				WebPaDataTypes.STRING.getValue()));
		webpaResponse = tapEnv.executeMultipleWebPaSetCommands(device, webPaParameters);
		setResult = null != webpaResponse && !webpaResponse.isEmpty()
				&& BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
						BroadBandTestConstants.SUCCESS_TXT,
						webpaResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_NAME))
				&& BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
						BroadBandTestConstants.SUCCESS_TXT,
						webpaResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_NAME))
				&& BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
						BroadBandTestConstants.SUCCESS_TXT,
						webpaResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_PRIVATE_SSID_2GHZ_PASSPHRASE))
				&& BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
						BroadBandTestConstants.SUCCESS_TXT,
						webpaResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_PRIVATE_SSID_5GHZ_PASSPHRASE));

		return setResult;
	}

	/**
	 * Method to enable disable mac filter mode
	 * 
	 * @param device
	 * @param tapEnv
	 * @param index
	 * @refactor Athira
	 */
	public static boolean changeMacFilterStatus(Dut device, AutomaticsTapApi tapEnv, WEBPA_AP_INDEXES index,
			String setValue) {
		boolean setResult = false;
		List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
		Map<String, String> webpaResponse = null;
		webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_MACFILTER_ENABLE
						.replace(BroadBandTestConstants.TR181_NODE_REF, index.get24Ghz()),
				setValue, WebPaDataTypes.BOOLEAN.getValue()));
		webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_MACFILTER_ENABLE
						.replace(BroadBandTestConstants.TR181_NODE_REF, index.get5Ghz()),
				setValue, WebPaDataTypes.BOOLEAN.getValue()));

		webpaResponse = tapEnv.executeMultipleWebPaSetCommands(device, webPaParameters);
		setResult = null != webpaResponse && !webpaResponse.isEmpty()
				&& BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
						BroadBandTestConstants.SUCCESS_TXT,
						webpaResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_MACFILTER_ENABLE
								.replace(BroadBandTestConstants.TR181_NODE_REF, index.get24Ghz())))
				&& BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
						BroadBandTestConstants.SUCCESS_TXT,
						webpaResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_MACFILTER_ENABLE
								.replace(BroadBandTestConstants.TR181_NODE_REF, index.get5Ghz())));
		if (!setResult) {
			setResult = BroadBandWebPaUtils.executeSetAndGetOnMultipleWebPaGetParams(device, tapEnv, webPaParameters)
					.isStatus();
		}
		return setResult;
	}

	/**
	 * Method to verify the IPv4 and IPv6 connection interface & Internet
	 * connectivity for Ethernet with break execution-true since the client should
	 * get valid Ipv4, Ipv6 and connectivity
	 * 
	 * @param device          {@link Dut}
	 * @param testId          Test case ID
	 * @param deviceConnected Device Connected
	 * @param stepNumber      Step Number
	 * 
	 * @refactor Athira
	 */
	public static void verifyIpv4AndIpV6ConnectionForEthernetClient(Dut device, String testId, Dut deviceConnected,
			int stepNumber) throws TestException {
		LOGGER.debug("STARTING METHOD : verifyIpv4AndIpV6ConnectionForEthernetClient()");
		String step = null;
		boolean status = false;
		String errorMessage = null;
		BroadBandResultObject result = null;
		//
		boolean isSystemdPlatforms = false;
		isSystemdPlatforms = DeviceModeHandler.isFibreDevice(device);
		LOGGER.info("Gateway device model is:" + isSystemdPlatforms);
		try {
			String model = ((Device) deviceConnected).getModel();

			/**
			 * STEP : VERIFY THE CORRECT IPV4 ADDRESS FOR CONNECTED CLIENT
			 */
			step = "S" + stepNumber;
			status = false;
			LOGGER.info("**********************************************************************************");
			LOGGER.info("STEP " + stepNumber
					+ ": DESCRIPTION :Verify the client  connected to ethernet has valid IPv4 Address " + model);
			LOGGER.info("STEP " + stepNumber
					+ ": ACTION : Get the device IPv4 address using below commandLinux : ifconfig eth0 |grep -i \"inet addr:\"Windows: ipconfig "
					+ "|grep -A 10 \"Ethernet LAN adapter Wi-Fi\" |grep -i \"IPv4 Address\"");
			LOGGER.info("STEP " + stepNumber
					+ ": EXPECTED : Client connected to the Ethernet should be assigned with a valid IP Address");
			LOGGER.info("**********************************************************************************");
			errorMessage = "Unable to get the correct IPV4 address from LAN client";
			long startTime = System.currentTimeMillis();
			do {
				status = BroadBandConnectedClientUtils.verifyIpv4AddressForWiFiOrLanInterfaceConnectedWithRdkbDevice(
						((Device) deviceConnected).getOsType(), deviceConnected, tapEnv);
			} while (!status && (System.currentTimeMillis() - startTime) < BroadBandTestConstants.THREE_MINUTE_IN_MILLIS
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
			if (status) {
				LOGGER.info("STEP " + stepNumber
						+ " : ACTUAL : Successfully verified the correct IPV4 address from LAN client");
			} else {
				LOGGER.error("STEP " + stepNumber + " : ACTUAL : " + errorMessage);
			}
			LOGGER.info("**********************************************************************************");
			tapEnv.updateExecutionStatus(device, testId, step, status, errorMessage, false);

			/**
			 * STEP : Verify the IPv6 Address is retrieved from the client connected to
			 * Ethernet
			 */
			stepNumber++;
			step = "S" + stepNumber;
			status = false;
			if (BroadbandPropertyFileHandler.isIpv6Enabled()) {
				LOGGER.info("#####################################################################################");
				LOGGER.info("STEP " + stepNumber
						+ ": DESCRIPTION :Verify the IPv6 Address is retrieved from the client connected to Ethernet");
				LOGGER.info("STEP " + stepNumber
						+ ": ACTION :  Get the device IPv4 address using below commandLinux : ifconfig eth0 |"
						+ "grep -i \"inet addr6:\"Windows: ipconfig |grep -A 10 \"Ethernet LAN adapter Wi-Fi\" |grep -i \"IPv6 Address\"");

				LOGGER.info("STEP " + stepNumber
						+ ": EXPECTED :  Local IPv6 Address assigned to the client should be retrieved successfully");
				LOGGER.info("#####################################################################################");
				errorMessage = "UNABLE TO GET THE CORRECT IPV6 ADDRESS FROM CLIENT MODEL " + model;
				if (!isSystemdPlatforms) {
					startTime = System.currentTimeMillis();
					do {
						status = BroadBandConnectedClientUtils
								.verifyIpv6AddressForWiFiOrLanInterfaceConnectedWithRdkbDevice(
										((Device) deviceConnected).getOsType(), deviceConnected, tapEnv);
					} while (!status
							&& (System.currentTimeMillis() - startTime) < BroadBandTestConstants.THREE_MINUTE_IN_MILLIS
							&& BroadBandCommonUtils.hasWaitForDuration(tapEnv,
									BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
					if (status) {
						LOGGER.info("STEP " + stepNumber
								+ " : ACTUAL : SUCCESSFYLLY VERIFIED CORRECT IPV6 ADDRESS FROM CLIENT DEVICE MODEL : "
								+ model);
					} else {
						LOGGER.error("STEP " + stepNumber + " : ACTUAL : " + errorMessage);
					}
					LOGGER.info("**********************************************************************************");
					tapEnv.updateExecutionStatus(device, testId, step, status, errorMessage, false);
				} else {
					LOGGER.info("**********************************************************************************");
					tapEnv.updateExecutionForAllStatus(device, testId, step, ExecutionStatus.NOT_APPLICABLE,
							BroadBandTestConstants.FIBRE_NOT_APPLICABLE_IPV6, false);
				}
			} else {
				LOGGER.info("IPv6 is not available/disabled : skipping teststep ...");
				tapEnv.updateExecutionForAllStatus(device, testId, step, ExecutionStatus.NOT_APPLICABLE, errorMessage,
						false);
			}

			/**
			 * Step : Verify the internet is accessible in the client connected to Ethernet
			 * using IPV4 interface
			 */
			stepNumber++;
			step = "S" + stepNumber;
			errorMessage = "Not able to access the site'www.google.com' from LAN client using IPV4 address";
			status = false;
			LOGGER.info("**********************************************************************************");
			LOGGER.info("STEP " + stepNumber
					+ ": DESCRIPTION : Verify the internet is accessible in the client connected to Ethernet using IPV4 interface");
			LOGGER.info("STEP " + stepNumber
					+ ": ACTION : Execute the command in connected client:curl -4 -v 'www.google.com' "
					+ " | grep '200 OK' OR ping -4 -n 5 google.com, LINUX : curl -4 -f --interface <interfaceName>"
					+ " www.google.com | grep '200 OK' OR ping -4 -n 5 google.com");
			LOGGER.info("STEP " + stepNumber + ": EXPECTED :Internet should be accessible in the connected client.");
			LOGGER.info("**********************************************************************************");
			result = BroadBandConnectedClientUtils.verifyInternetIsAccessibleInConnectedClientUsingCurl(tapEnv,
					deviceConnected,
					BroadBandTestConstants.URL_HTTPS + BroadBandTestConstants.STRING_GOOGLE_HOST_ADDRESS,
					BroadBandTestConstants.IP_VERSION4);
			status = result.isStatus();
			if (!status) {
				errorMessage = "Ping operation failed to access the site 'www.google.com' using IPV4 address";
				status = ConnectedNattedClientsUtils.verifyPingConnectionForIpv4AndIpv6(deviceConnected, tapEnv,
						BroadBandTestConstants.PING_TO_GOOGLE, BroadBandTestConstants.IP_VERSION4);
			}
			if (status) {
				LOGGER.info("STEP " + stepNumber
						+ " : ACTUAL : Connected LAN client has internet connectivity using IPV4 interface");
			} else {
				LOGGER.error("STEP " + stepNumber + " : ACTUAL : " + errorMessage);
			}
			LOGGER.info("**********************************************************************************");
			tapEnv.updateExecutionStatus(device, testId, step, status, errorMessage, false);

			/**
			 * Step : Verify the internet is accessible in the client connected to Ethernet
			 * using IPV6 interface
			 */
			stepNumber++;
			step = "S" + stepNumber;
			errorMessage = "Not able to access the site'www.google.com' from LAN client using IPV6 address";
			status = false;
			if (BroadbandPropertyFileHandler.isIpv6Enabled()) {
				LOGGER.info("**********************************************************************************");
				LOGGER.info("STEP " + stepNumber
						+ ": DESCRIPTION :Verify the internet is accessible in the client connected to Ethernet using IPV6 interface");
				LOGGER.info("STEP " + stepNumber
						+ ": ACTION : Execute the command in connected client:curl -4 -v 'www.google.com' "
						+ " | grep '200 OK' OR ping -4 -n 5 google.com, LINUX : curl -4 -f --interface <interfaceName>"
						+ " www.google.com | grep '200 OK' OR ping -4 -n 5 google.com ");
				LOGGER.info(
						"STEP " + stepNumber + ": EXPECTED : Internet should be accessible in the connected client.");
				LOGGER.info("**********************************************************************************");
				errorMessage = "NOT ABLE TO ACCESS THE SITE 'www.google.com' FROM LAN CLIENT WITH USING IPV6";
				if (isSystemdPlatforms) {
					LOGGER.info(
							"STEP : ACTUAL : IPV6 INTERNET CONNECTIVITY VERIFICATION NOT APPLICABLE FOR fibre DEVICES");
					LOGGER.info("**********************************************************************************");
					tapEnv.updateExecutionForAllStatus(device, testId, step, ExecutionStatus.NOT_APPLICABLE,
							BroadBandTestConstants.NOTAPPLICABLE_VALUE, false);
				} else {
					result = BroadBandConnectedClientUtils.verifyInternetIsAccessibleInConnectedClientUsingCurl(tapEnv,
							deviceConnected,
							BroadBandTestConstants.URL_HTTPS + BroadBandTestConstants.STRING_GOOGLE_HOST_ADDRESS,
							BroadBandTestConstants.IP_VERSION6);
					status = result.isStatus();
					errorMessage = result.getErrorMessage();
					if (!status) {
						errorMessage = "PING OPERATION FAILED TO ACCESS THE SITE 'www.google.com' USING IPV6 ";
						status = ConnectedNattedClientsUtils.verifyPingConnectionForIpv4AndIpv6(deviceConnected, tapEnv,
								BroadBandTestConstants.PING_TO_GOOGLE, BroadBandTestConstants.IP_VERSION6);
					}
					if (status) {
						LOGGER.info("STEP " + stepNumber
								+ " : ACTUAL : Connected LAN client has internet connectivity using IPV6 interface");
					} else {
						LOGGER.error("STEP " + stepNumber + " : ACTUAL : " + errorMessage);
					}
					LOGGER.info("**********************************************************************************");
					tapEnv.updateExecutionStatus(device, testId, step, status, errorMessage, false);
				}
			} else {
				LOGGER.info("IPv6 is not available/disabled : skipping teststep ...");
				tapEnv.updateExecutionForAllStatus(device, testId, step, ExecutionStatus.NOT_APPLICABLE, errorMessage,
						false);
			}
		} catch (TestException e) {
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}
		LOGGER.debug("ENDING METHOD : verifyIpv4AndIpV6ConnectionForEthernetClient()");
	}

	/**
	 * Enum variable to store the operating modes in different bands. Connected
	 * clients only displaying only latest operating standard. Only way to get the
	 * correct standard is to use client with same lower standard clients.
	 * 
	 * @refactor Alan_Bivera
	 */
	public enum WifiOperatingStandard {
		OPERATING_STANDARD_G_N("g,n", "802.11g;802.11n"), OPERATING_STANDARD_B_G_N("b,g,n", "802.11n;802.11b;802.11g"),
		OPERATING_STANDARD_N("n", "802.11n"), OPERATING_STANDARD_A_N_AC("a,n,ac", "802.11a;802.11n;802.11ac"),
		OPERATING_STANDARD_AC("ac", "802.11ac"), OPERATING_STANDARD_N_AC("n,ac", "802.11n;802.11ac"),
		OPERATING_STANDARD_A_N("a,n", "802.11a;802.11n"),
		OPERATING_STANDARD_A_N_AC_AX("a,n,ac,ax", "802.11a;802.11n;802.11ac;802.11ax"),
		OPERATING_STANDARD_G_N_AX("g,n,ax", "802.11g;802.11n;802.11ax");

		private String gatewayOperatingMode;
		private String clientOperatingMode;

		public List<String> getClientOperatingMode() {
			return Arrays.asList(clientOperatingMode.split(";"));
		}

		public void setClientOperatingMode(String clientOperatingMode) {
			this.clientOperatingMode = clientOperatingMode;
		}

		WifiOperatingStandard(String gatewayMode, String clientMode) {
			this.gatewayOperatingMode = gatewayMode;
			this.clientOperatingMode = clientMode;
		}

		public String getOperatingmode() {
			return gatewayOperatingMode;
		}

		public void operatingMode(String mode) {
			this.gatewayOperatingMode = mode;
		}

	}

	/**
	 * Helper method to verify enabling private SSID from snmp.
	 * 
	 * @param device        Dut instance
	 * @param tapEnv        AutomaticsTapApi instance
	 * @param ssidFrequency Wifi SSID frequency
	 * @return true if private ssid is enabled
	 */
	public static boolean verifyEnablingPrivateSsidUsingSnmp(Dut device, AutomaticsTapApi tapEnv,
			SSIDFrequency ssidFrequency) {
		LOGGER.debug("STARTING METHOD : verifyEnablingPrivateSsidUsingSnmp");
		boolean status = false;
		String oid = BroadBandSnmpMib.ECM_WIFI_2_4_SSID_STATUS.getOid();
		if (SSIDFrequency.FREQUENCY_5_GHZ.equals(ssidFrequency)) {
			oid = BroadBandSnmpMib.ECM_WIFI_5_SSID_STATUS.getOid();
		}

		String snmpSetResponse = BroadBandSnmpUtils.snmpSetOnEcm(tapEnv, device, oid, SnmpDataType.INTEGER,
				BroadBandTestConstants.STRING_VALUE_ONE);
		LOGGER.info("Response from SNMP Set command is -" + snmpSetResponse);
		if (CommonMethods.isNotNull(snmpSetResponse)
				&& BroadBandTestConstants.STRING_VALUE_ONE.equalsIgnoreCase(snmpSetResponse)) {
			// Waiting one min
			tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
			String snmpGetResponse = BroadBandSnmpUtils.snmpGetOnEcm(tapEnv, device, oid);
			status = CommonMethods.isNotNull(snmpGetResponse)
					&& !snmpGetResponse.contains(BroadBandTestConstants.TIME_OUT_RESPONSE)
					&& snmpSetResponse.equals(snmpGetResponse);
		}
		LOGGER.info("Is Private SSID enabled through SNMP- " + status);
		LOGGER.debug("ENDING METHOD : verifyEnablingPrivateSsidUsingSnmp");
		return status;
	}

	/**
	 * Helper method to verify enabling private SSID from Tr69.
	 * 
	 * @param device        Dut instance
	 * @param tapEnv        AutomaticsTapApi instance
	 * @param ssidFrequency Wifi SSID frequency
	 * @return true if private ssid is enabled
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean verifyEnablingPrivateSsidUsingTr69(Dut device, AutomaticsTapApi tapEnv,
			SSIDFrequency ssidFrequency) {
		LOGGER.debug("STARTING METHOD : verifyEnablingPrivateSsidUsingTr69");
		boolean status = false;
		// This parameter gives the value of the Private SSID in MSO page, ready only
		// parameter
		String checkSsidStatus = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_2_4GHZ_PRIVATE_SSID;
		// This parameter is for modifying Private SSID state.
		String enableSsid = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_ENABLE;
		if (SSIDFrequency.FREQUENCY_5_GHZ.equals(ssidFrequency)) {
			checkSsidStatus = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_5GHZ_PRIVATE_SSID;
			enableSsid = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_ENABLED_STATUS;
		}
		long startTime = System.currentTimeMillis();
		boolean exceptionStatus = false;

		// parameter 1
		Parameter setParam = new Parameter();

		setParam.setDataType(TR69ParamDataType.BOOLEAN.get());
		setParam.setParamName(enableSsid);
		setParam.setParamValue(BroadBandTestConstants.TRUE);

		List<Parameter> parameters = new ArrayList<Parameter>();
		parameters.add(setParam);

		// parameter 2

		List<String> parameters2 = new ArrayList<String>();
		parameters2.add(checkSsidStatus);

		do {
			try {
				String response = tapEnv.setTR69ParameterValues(device, parameters);
				LOGGER.info("Response of setTR69ParameterValues method is:" + response);

				if (!response.equalsIgnoreCase("Failed to set TR69 param value")) {
					status = !(BroadBandConnectedClientTestConstants.RADIO_STATUS_DOWN
							.equalsIgnoreCase(tapEnv.getTR69ParameterValues(device, parameters2)));
				}
			} catch (Exception exception) {
				exceptionStatus = true;
				LOGGER.error("Exception occurred while verifying radio status of Wifi using TR69.");
			}
		} while (!status && exceptionStatus
				&& ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.THREE_MINUTE_IN_MILLIS)
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.ONE_MINUTE_IN_MILLIS));

		LOGGER.info("Is Private SSID enabled through TR69- " + status);
		LOGGER.debug("ENDING METHOD : verifyEnablingPrivateSsidUsingTr69");
		return status;
	}

	/**
	 * Helper method to verify enabling private SSID from WebPA.
	 * 
	 * @param device        Dut instance
	 * @param tapEnv        AutomaticsTapApi instance
	 * @param ssidFrequency Wifi SSID frequency
	 * @return true if private ssid is enabled
	 */
	public static boolean verifyEnablingPrivateSsidUsingWebpa(Dut device, AutomaticsTapApi tapEnv,
			SSIDFrequency ssidFrequency) {
		LOGGER.debug("STARTING METHOD : verifyEnablingPrivateSsidUsingWebpa");
		boolean status = false;

		// This parameter gives the value of the Private SSID in MSO page, ready only
		// parameter
		String checkSsidStatus = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_2_4GHZ_PRIVATE_SSID;
		// This parameter is for modifying Private SSID state.
		String enableSsid = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_ENABLE;
		if (SSIDFrequency.FREQUENCY_5_GHZ.equals(ssidFrequency)) {
			checkSsidStatus = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_5GHZ_PRIVATE_SSID;
			enableSsid = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_ENABLED_STATUS;
		}

		status = BroadBandWiFiUtils.setWebPaParams(device, enableSsid, BroadBandTestConstants.TRUE,
				WebPaDataTypes.BOOLEAN.getValue());

		LOGGER.info("Response from WEBPA command is -" + status);
		if (status) {
			status = false;
			String getResponse = tapEnv.executeWebPaCommand(device, checkSsidStatus);
			LOGGER.info("Response from WEBPA GET command is -" + getResponse + ".");
			status = CommonMethods.isNotNull(getResponse)
					&& !(BroadBandConnectedClientTestConstants.RADIO_STATUS_DOWN.equalsIgnoreCase(getResponse.trim()));
		}
		LOGGER.info("Is Private SSID enabled through WebPA- " + status);
		LOGGER.debug("ENDING METHOD : verifyEnablingPrivateSsidUsingWebpa");
		return status;
	}

	/**
	 * Method to validate channels are from supported list
	 * 
	 * @param channelList Channel list from webpa
	 * @param band        Wifi frequency band
	 * @return validation result
	 * @Refactor Alan_Bivera
	 */

	public static boolean validateIfChannelsAreFromSupportedList(String channelList, WiFiFrequencyBand band,
			Dut device) {
		LOGGER.debug("STARTING METHOD: validateIfChannelsAreFromSupportedList ");
		boolean result = false;
		try {

			String[] channels = channelList.split(AutomaticsConstants.COMMA);
			for (String channel : channels) {
				result = CommonMethods.patternMatcher(channel,
						band == WiFiFrequencyBand.WIFI_BAND_2_GHZ
								? (CommonMethods.isAtomSyncAvailable(device, tapEnv))
										? BroadBandTestConstants.PATTERN_MATCHER_24_GHZ_CHANNEL_ATOM
										: BroadBandTestConstants.PATTER_MATCHER_FOR_CHANNEL_SELECT_OF_2GHZ_PRIVATE_WIFI
								: BroadBandTestConstants.PATTER_MATCHER_FOR_CHANNEL_SELECT_OF_5GHZ_PRIVATE_WIFI);
				if (!result) {
					break;
				}
			}

		} catch (Exception e) {
			LOGGER.error("Exception occured while validating the channels", e);
		}
		LOGGER.debug("ENDING METHOD: validateIfChannelsAreFromSupportedList ");
		return result;
	}

	/**
	 * 
	 * Verify the telemetry markers for WIFI
	 * 
	 * @param device     {@link Dut}
	 * @param band       {@link WiFiFrequencyBand}
	 * @param logMessage expected log message
	 * @return true if markers matches pattern
	 */
	public static boolean verifyTelemetryMarkers(Dut device, String accessPointNumber, String logMessage,
			String pattern) {
		LOGGER.debug("STARTING METHOD: verifyTelemetryMarkers()");
		boolean status = false;
		String response = null;
		String searchLogMessage = BroadBandCommonUtils.concatStringUsingStringBuffer(logMessage, accessPointNumber,
				AutomaticsConstants.COLON);
		try {
			String pattenMatcher = BroadBandCommonUtils.concatStringUsingStringBuffer(searchLogMessage, pattern);
			String command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND,
					BroadBandTestConstants.TEXT_DOUBLE_QUOTE, searchLogMessage,
					BroadBandTestConstants.TEXT_DOUBLE_QUOTE, BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
					BroadBandCommandConstants.LOCATION_WIFI_HEALTH_LOG, BroadBandTestConstants.SYMBOL_PIPE,
					BroadBandTestConstants.CMD_TAIL_1);
			long startTime = System.currentTimeMillis();
			do {
				response = BroadBandCommonUtils.executeCommandInAtomConsoleIfAtomIsPresentElseInArm(device, tapEnv,
						command);
				status = CommonMethods.isNotNull(response) && CommonMethods.patternMatcher(response, pattenMatcher);
			} while (!status && (System.currentTimeMillis() - startTime) < BroadBandTestConstants.SIX_MINUTE_IN_MILLIS
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
		} catch (Exception e) {
			LOGGER.error("Exception occured while verifying telemetry marker :" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: verifyTelemetryMarkers()");
		return status;
	}

	/**
	 * Method to enable disable wifi stats
	 * 
	 * @param device
	 * @param tapEnv
	 * @param setValue
	 * @return
	 * @refactor Athira
	 */

	public static boolean enableDisableWifiStats(Dut device, AutomaticsTapApi tapEnv, String setValue) {
		boolean setResult = false;
		List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
		Map<String, String> webpaResponse = null;
		webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_STATS_ENABLE, setValue,
				BroadBandTestConstants.CONSTANT_3));
		webpaResponse = tapEnv.executeMultipleWebPaSetCommands(device, webPaParameters);
		setResult = null != webpaResponse && !webpaResponse.isEmpty()
				&& BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
						BroadBandTestConstants.SUCCESS_TXT,
						webpaResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_STATS_ENABLE));
		return setResult;
	}

	/**
	 * 
	 * Method to change the wifi channel numbers in 2.4 Ghz and 5 Ghz to a value
	 * other that their current value
	 * 
	 * All the possi
	 * 
	 * @param Dut               device
	 * @param WiFiFrequencyBand wifiBand - frequency band whose channel number has
	 *                          to be changed
	 * 
	 * @return selected channel number
	 * 
	 * 
	 *         Make sure that you disable auto channel by setting
	 *         Device.WiFi.Radio.10000.AutoChannelEnable ,
	 *         Device.WiFi.Radio.10100.AutoChannelEnable to false
	 * 
	 *         Get the current values for channel numbers in test script and update
	 *         wifi channel parameters those values ain finally script
	 * 
	 *         in the finally block of your script
	 * @refactor Govardhan
	 * 
	 */
	public static String changePrivateWifiChannelToNonDefaultRandomValue(Dut device, AutomaticsTapApi tapEnv,
			WiFiFrequencyBand wifiBand) throws TestException {

		LOGGER.debug("STARTING METHOD: changePrivateWifiChannelToRandomValueOtherThanCurrentValue");
		boolean status = false;
		String[] possibleChannels = null;
		String currentChannelNumber = null;
		String selectedChannelNumber = null;

		Map<WiFiFrequencyBand, String> autoChannelEnableWebpaParams = new HashMap<WiFiFrequencyBand, String>();
		autoChannelEnableWebpaParams.put(WiFiFrequencyBand.WIFI_BAND_2_GHZ,
				BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_2GHZ);
		autoChannelEnableWebpaParams.put(WiFiFrequencyBand.WIFI_BAND_5_GHZ,
				BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_5GHZ);

		Map<WiFiFrequencyBand, String> webPaPrameterToGetEnabledWifiChannel = new HashMap<WiFiFrequencyBand, String>();
		webPaPrameterToGetEnabledWifiChannel.put(WiFiFrequencyBand.WIFI_BAND_2_GHZ,
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_RADIO_CHANNEL_IN_2GHZ);
		webPaPrameterToGetEnabledWifiChannel.put(WiFiFrequencyBand.WIFI_BAND_5_GHZ,
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_RADIO_CHANNEL_IN_5GHZ);

		// Disabling AutoChannel enable
		status = BroadBandWebPaUtils.verifyWebPaValueAfterDuration(device, tapEnv,
				autoChannelEnableWebpaParams.get(wifiBand), WebPaDataTypes.BOOLEAN.getValue(), RDKBTestConstants.FALSE,
				RDKBTestConstants.ONE_MINUTE_IN_MILLIS);
		if (status) {

			status = false;
			// getting the list of possible channels
			possibleChannels = getPossibleChannelList(device, tapEnv, wifiBand);
			LOGGER.info(
					"Possible Channels for Device model -" + device.getModel() + " is " + possibleChannels.toString());
			currentChannelNumber = tapEnv.executeWebPaCommand(device,
					webPaPrameterToGetEnabledWifiChannel.get(wifiBand));
			LOGGER.info("CurrentChannelNumber in " + wifiBand + " " + currentChannelNumber);

			if (CommonMethods.isNotNull(currentChannelNumber)) {
				// Selecting a channel number other than the current channel number
				for (String temp : possibleChannels) {
					if (!(temp.equalsIgnoreCase(currentChannelNumber))) {
						selectedChannelNumber = temp;
						break;
					}
				}
			} else {
				LOGGER.error("current channel number is obtained as null for parameter "
						+ webPaPrameterToGetEnabledWifiChannel.get(wifiBand));
				throw new TestException("current channel number is obtained as null for parameter "
						+ webPaPrameterToGetEnabledWifiChannel.get(wifiBand));
			}

		} else {
			LOGGER.error("unable to set" + autoChannelEnableWebpaParams.get(wifiBand) + "to false");
			throw new TestException("failed in disabling auto channel enable feature by setting"
					+ autoChannelEnableWebpaParams.get(wifiBand) + " to false");
		}
		LOGGER.info("Selected Channel number in " + wifiBand + " " + selectedChannelNumber);
		status = BroadBandWebPaUtils.verifyWebPaValueAfterDuration(device, tapEnv,
				webPaPrameterToGetEnabledWifiChannel.get(wifiBand), WebPaDataTypes.INTEGER.getValue(),
				selectedChannelNumber, RDKBTestConstants.ONE_MINUTE_IN_MILLIS);
		/*
		 * tapEnv.setWebPaParams(device, autoChannelEnableWebpaParams.get(wifiBand),
		 * XreTestConstants.TRUE, WebPaDataTypes.BOOLEAN.getValue());
		 */
		if (status) {
			LOGGER.debug("ENDING METHOD: changePrivateWifiChannelToRandomValueOtherThanCurrentValue");
			return selectedChannelNumber;
		} else {
			LOGGER.error("unable to set " + webPaPrameterToGetEnabledWifiChannel.get(wifiBand) + " with value "
					+ selectedChannelNumber);
			throw new TestException("unable to set " + webPaPrameterToGetEnabledWifiChannel.get(wifiBand)
					+ " with value " + selectedChannelNumber);
		}
	}

	/**
	 * Method to get list of possible channels in a wifi frequency band using webpa
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut} @ param wifiFrequencyBand wifiBand
	 * 
	 * @ return String[] PossibleChannels in a particular frequency band
	 * @refactor Govardhan
	 */
	public static String[] getPossibleChannelList(Dut device, AutomaticsTapApi tapEnv, WiFiFrequencyBand wifiBand) {

		LOGGER.debug("STARTING METHOD : getPossibleChannelList () ");
		String[] possibleChannelList = null;
		String possibleChannels = null;
		if (wifiBand.getValue().equals((WiFiFrequencyBand.WIFI_BAND_2_GHZ.getValue()))) {

			possibleChannels = AutomaticsTapApi
					.getSTBPropsValue(BroadBandTestConstants.KEY_FOR_2GHZ_WIFI_POSSIBLE_CHANNELS);
			LOGGER.info("Obtained channel list for 2.4 GHz : " + possibleChannels);
		} else {
			possibleChannels = BroadbandPropertyFileHandler.getPossibleChannelList5GHZ(device);
			LOGGER.info("Obtained channel list for 5 GHz  : " + possibleChannels);
		}

		if (CommonMethods.isNotNull(possibleChannels)) {
			possibleChannelList = possibleChannels.split(BroadBandTestConstants.COMMA);
		} else {
			throw new TestException("Obtained NULL response for possible channel list for model " + device.getModel()
					+ " from stb.properties file");
		}

		LOGGER.debug("ENDING METHOD : getPossibleChannelList () ");
		return possibleChannelList;
	}

	/**
	 * Test step method used to connect the private wifi for given GHz frequency
	 * band
	 * 
	 * @param device          instance of{@link Dut}
	 * @param testCaseId      Test case ID
	 * @param deviceToConnect Device to connect the WiFi
	 * @param wifiBand        Frequency band to connect the wifi
	 * @param stepNumber      Step Number
	 * 
	 * @refactor yamini.s
	 */
	public static void executeTestStepToConnectPrivateWiFi(Dut device, String testCaseId, Dut deviceToConnect,
			String wifiBand, int stepNumber) {
		/**
		 * STEP : CONNECT THE CLIENT INTO GIVEN GHZ PRIVATE SSID AND VERIFY CONNECTION
		 * STATUS
		 */
		String stepNum = "S" + stepNumber;
		boolean status = false;
		String errorMessage = null;
		LOGGER.info("***************************************************************************************");
		LOGGER.info(
				"STEP " + stepNumber + " : DESCRIPTION : CONNECT A CLIENT TO THE PRIVATE WIFI " + wifiBand + " SSID.");
		LOGGER.info("STEP " + stepNumber + " : ACTION : CONNECT TO " + wifiBand
				+ " PRIVATE WIFI USING BELOW COMMANDS : FOR LINUX :nmcli dev wifi connect <ssid> FOR WINDOWS: netsh wlan connect ssid=<ssid>");
		LOGGER.info("STEP " + stepNumber + " : EXPECTED : CONNECTION SHOULD BE SUCCESSFUL FOR CLIENT WITH " + wifiBand
				+ " PRIVATE WIFI SSID ");
		LOGGER.info("***************************************************************************************");
		errorMessage = "PRIVATE WIFI CONNECTION FAILED FOR " + wifiBand + " SSID IN CLIENT";
		status = ConnectedNattedClientsUtils.connectToSSID(deviceToConnect, tapEnv,
				BroadBandConnectedClientUtils.getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv,
						wifiBand.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ)
								? WiFiFrequencyBand.WIFI_BAND_2_GHZ
								: WiFiFrequencyBand.WIFI_BAND_5_GHZ),
				BroadBandConnectedClientUtils.getSsidPassphraseFromGatewayUsingWebPaOrDmcli(device, tapEnv,
						wifiBand.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ)
								? WiFiFrequencyBand.WIFI_BAND_2_GHZ
								: WiFiFrequencyBand.WIFI_BAND_5_GHZ));
		if (status) {
			LOGGER.info("STEP " + stepNumber + " : ACTUAL : SUCCESSFYLLY CONNECTED THE CLIENT WITH " + wifiBand
					+ " GHZ PRIVATE SSID.");
		} else {
			LOGGER.error("STEP " + stepNumber + " : ACTUAL : " + errorMessage);
		}
		LOGGER.info("***************************************************************************************");
		tapEnv.updateExecutionStatus(device, testCaseId, stepNum, status, errorMessage, true);
	}

	/**
	 * Scan the client device for the networks available and return whether the
	 * device is visible with poll duration when the 2.4/5 Ghz radio is
	 * enabled/disabled
	 * 
	 * @param Dut            device
	 * @param tapEnv         {@link AutomaticsTapApi} Reference
	 * @param clientSettop   Connected client device device instance
	 * @param wifiBand       The value of 2.4/5 GHz band
	 * @param isRadioEnabled Status of radio 2.4/5 GHz band is enable/disabled
	 * @param pollDuration   poll duration
	 * @return true if SSID is visible/invisible in client device based on
	 *         isRadioEnabled
	 * 
	 * @refactor yamini.s
	 */
	public static boolean scanAndVerifyVisibleSsidFromWiFiConnectedClientDeviceWithPollDuration(Dut device,
			AutomaticsTapApi tapEnv, Dut clientSettop, WiFiFrequencyBand wifiBand, boolean isRadioEnabled,
			long pollDuration) {
		LOGGER.debug("STARTING METHOD: scanAndVerifyVisibleSsidFromWiFiConnectedClientDeviceWithPollDuration");
		boolean status = false;
		String ssidName = BroadBandConnectedClientUtils.getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv,
				wifiBand);
		long startTime = System.currentTimeMillis();
		do {
			if (isRadioEnabled) {
				status = BroadBandConnectedClientUtils
						.scanAndVerifyVisibleSsidFromWiFiConnectedClientDevice(clientSettop, ssidName, tapEnv);
			} else {
				status = !BroadBandConnectedClientUtils
						.scanAndVerifyVisibleSsidFromWiFiConnectedClientDevice(clientSettop, ssidName, tapEnv);
			}
			LOGGER.info("Visible Ssid " + wifiBand + " Status:" + status);
		} while ((System.currentTimeMillis() - startTime) < pollDuration && !status
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
		LOGGER.debug("ENDING METHOD: scanAndVerifyVisibleSsidFromWiFiConnectedClientDeviceWithPollDuration");
		return status;
	}

	/**
	 * Utility Method to retrieve the list of WiFi Access Points based
	 * 
	 * @return List of BroadBandMFPConfigEnum.
	 * @refactor Athira
	 */
	public static List<BroadBandMFPConfigEnum> getListofWifiAccessPoints() {
		LOGGER.debug("ENTERING METHOD: getListofWifiAccessPoints");
		List<BroadBandMFPConfigEnum> wifiAccessPointsList = new ArrayList<BroadBandMFPConfigEnum>();
		for (BroadBandMFPConfigEnum wifiAccessPoint : BroadBandMFPConfigEnum.values()) {
			wifiAccessPointsList.add(wifiAccessPoint);
		}
		LOGGER.info("BROADBAND WIFI ACCESS POINTS TO BE VALIDATED: " + wifiAccessPointsList.size());
		LOGGER.debug("ENDING METHOD: getListofWifiAccessPoints");
		return wifiAccessPointsList;
	}

	/**
	 * Utility Method to set the MFGConfig values for VAPs using WebPA.
	 * 
	 * @param tapEnv           {@link AutomaticsTapApi}
	 * @param device           {@link Dut}
	 * @param wifiAccessPoints List of BroadBandMFPConfigEnum
	 * @param valueToSet       String representing value to be set.
	 * @param setDefaultValue  Boolean Flag representing either default value to be
	 *                         set or not; TRUE in case default value to be set.
	 * 
	 * @return {@link BroadBandResultObject}
	 * 
	 * @refactor Athira
	 */
	public static BroadBandResultObject setMFPConfigValuesByWebPa(AutomaticsTapApi tapEnv, Dut device,
			List<BroadBandMFPConfigEnum> wifiAccessPoints, String valueToSet, boolean setDefaultValue) {
		LOGGER.debug("ENTERING METHOD: setMFPConfigValuesByWebPa");
		BroadBandResultObject resultObj = new BroadBandResultObject();
		boolean result = false;
		String errorMessage = BroadBandTestConstants.EMPTY_STRING;
		// Create WebPA Parameter list for all parameters for which power level needs to
		// be set.
		List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
		WebPaParameter webPaParam = null;
		try {
			for (BroadBandMFPConfigEnum wifiAccessPoint : wifiAccessPoints) {
				webPaParam = new WebPaParameter();
				webPaParam.setName(wifiAccessPoint.getWebPaParamMFPConfigr());
				webPaParam.setDataType(wifiAccessPoint.getValueType());

				if (setDefaultValue) {
					webPaParam.setValue(wifiAccessPoint.getDefaultValue());
				} else {
					webPaParam.setValue(valueToSet);
				}
				webPaParameters.add(webPaParam);
			}
			// Execute Multiple WebPA Commands.
			Map<String, String> MFPConfigValuesWebPaParameters = tapEnv.executeMultipleWebPaSetCommands(device,
					webPaParameters);
			// Parse the WebPA Command Execution Result.
			if (null != MFPConfigValuesWebPaParameters && !MFPConfigValuesWebPaParameters.isEmpty()) {
				for (String parameterName : MFPConfigValuesWebPaParameters.keySet()) {
					if (MFPConfigValuesWebPaParameters.get(parameterName)
							.equalsIgnoreCase(BroadBandTestConstants.SNMP_RESPONSE_SUCCESS)) {
						LOGGER.info("SUCCESSFULLY SET THE VALUE FOR PARAMETER: " + parameterName);
					} else {
						errorMessage = BroadBandCommonUtils.concatStringUsingStringBuffer(errorMessage,
								BroadBandTestConstants.SINGLE_SPACE_CHARACTER, parameterName);
					}
				}
				if (CommonMethods.isNull(errorMessage)) {
					result = true;
				} else {
					errorMessage = "UNABLE TO SET THE VALUE FOR PARAMETERS: " + errorMessage;
				}
			}

			resultObj.setStatus(result);
			resultObj.setErrorMessage(errorMessage);
		} catch (Exception e) {
			LOGGER.error("Exception occured while setting MFPConfig Values By Webpa -" + e.getMessage());
		}
		LOGGER.info("SET MFPConfigValues: " + result);
		LOGGER.debug("ENDING METHOD: setMFPConfigValuesByWebPa");
		return resultObj;
	}

	/**
	 * Utility Method to verify the MFPConfig values for the given WiFi Access
	 * Points.
	 * 
	 * @param tapEnv                 {@link AutomaticsTapApi}
	 * @param device                 {@link Dut}
	 * @param wifiAccessPoints       List of BroadBandMFPConfigEnum
	 * @param isBussinessClassDevice Boolean representing the device is Bussiness
	 *                               Class or not.
	 * @param isDeafaultValueCheck   Boolean representing the true for default value
	 *                               check and False for others
	 * 
	 * @return {@link BroadBandResultObject}
	 * 
	 * @refactor Athira
	 */
	public static BroadBandResultObject verifyMFPConfigValues(AutomaticsTapApi tapEnv, Dut device,
			List<BroadBandMFPConfigEnum> wifiAccessPoints, String expectedValue, boolean isBussinessClassDevice,
			boolean isDeafaultValueCheck) {
		LOGGER.debug("ENTERING METHOD: verifyMFPConfigValues");
		BroadBandResultObject resultObj = new BroadBandResultObject();
		boolean result = false;
		String actualValue = null;
		String errorMessage = BroadBandTestConstants.EMPTY_STRING;
		// Create WebPA Parameter list for all parameters for which power level needs to
		// be set.
		List<String> webPaParameters = new ArrayList<String>();
		try {
			for (BroadBandMFPConfigEnum wifiAccessPoint : wifiAccessPoints) {
				webPaParameters.add(wifiAccessPoint.getWebPaParamMFPConfigr());
			}
			// Execute Multiple WebPA Commands.
			Map<String, String> MFGConfigWebPaParameters = tapEnv.executeMultipleWebPaGetCommands(device,
					webPaParameters.toArray(new String[webPaParameters.size()]));

			// Parse the WebPA Command Execution Result.
			if (null != MFGConfigWebPaParameters && !MFGConfigWebPaParameters.isEmpty()) {
				// Loop through the WebPA response map.
				for (String parameterName : MFGConfigWebPaParameters.keySet()) {
					actualValue = MFGConfigWebPaParameters.get(parameterName);
					if (isDeafaultValueCheck) {
						expectedValue = isBussinessClassDevice ? getDefaultValueForBussinessClassDevices(parameterName)
								: expectedValue;
					}
					if (actualValue.equalsIgnoreCase(expectedValue)) {
						LOGGER.info("SUCCESSFULLY VERIFIED THE VALUE OF PARAMETER: " + parameterName);
					} else {
						errorMessage = BroadBandCommonUtils.concatStringUsingStringBuffer(errorMessage,
								BroadBandTestConstants.SINGLE_SPACE_CHARACTER, parameterName, ": EXPECTED VALUE = ",
								expectedValue, ", ACTUAL VALUE = ", actualValue);
					}
				}
				result = CommonMethods.isNull(errorMessage);
			}
			resultObj.setStatus(result);
			resultObj.setErrorMessage(errorMessage);
		} catch (Exception e) {
			LOGGER.error("Exception occured while verifying MFPConfigValues By Webpa -" + e.getMessage());
		}
		LOGGER.info("VERIFIED THE MANAGEMENT FRAMES POWER LEVEL: " + result);
		LOGGER.debug("ENDING METHOD: verifyMFPConfigValues");
		return resultObj;
	}

	/**
	 * Retrieve the value to be set from the Broadband MFPConfig Enum based on the
	 * WebPA Parameter.
	 * 
	 * @param webPaParameter String representing the WebPA Parameter.
	 * @return String representing the Value To Be Set.
	 */
	public static String getDefaultValueForBussinessClassDevices(String webPaParameter) {
		LOGGER.debug("ENTERING METHOD: getParameterBasedOnWebPa");
		String valueToBeSet = null;
		BroadBandMFPConfigEnum[] MFPConfigVAPs = BroadBandMFPConfigEnum.values();
		for (int iCounter = 0; iCounter < MFPConfigVAPs.length; iCounter++) {
			if (MFPConfigVAPs[iCounter].getWebPaParamMFPConfigr().equalsIgnoreCase(webPaParameter)) {
				valueToBeSet = String.valueOf(MFPConfigVAPs[iCounter].getDefaultValue());
				break;
			}
		}
		LOGGER.info("VALUE TO BE SET FOR PARAMETER: " + webPaParameter + " IS = " + valueToBeSet);
		LOGGER.debug("ENDING METHOD: getParameterBasedOnWebPa");
		return valueToBeSet;
	}

	/**
	 * Method to exeute step to verify wifi configuration using snmp is blocked when
	 * radio is force disabled
	 * 
	 * @param device     device instance
	 * @param wifiMib    Mib or Oid to be set
	 * @param dataType   Snmp data type of wifiMib
	 * @param value      value to be set
	 * @param tableIndex Table index of oid to be set
	 * 
	 * @return true if write not allowed and log message is present
	 * 
	 * @author Ashwin Sankarasubramanian
	 * @refactor Athira
	 */
	public static BroadBandResultObject verifyWiFiSnmpConfigDisabledStep(Dut device, String wifiMib,
			SnmpDataType dataType, String value, String tableIndex) {
		LOGGER.debug("STARTING METHOD: verifyWiFiSnmpConfigDisabledStep");
		BroadBandResultObject result = new BroadBandResultObject();
		result.setStatus(false);
		String response = null;
		try {
			result.setErrorMessage("Failed to clear WiFilog file");
			if ((CommonMethods.isAtomSyncAvailable(device, tapEnv))
					? BroadBandCommonUtils.clearAtomLogFile(tapEnv, device, BroadBandTestConstants.LOCATION_WIFI_LOG)
					: CommonUtils.clearLogFile(tapEnv, device, BroadBandTestConstants.LOCATION_WIFI_LOG)) {
				result.setErrorMessage("Able to set wifi mib: " + wifiMib + " with value: " + value
						+ " using snmp when radio force disabled");
				response = BroadBandSnmpUtils.executeSnmpSetWithTableIndexOnRdkDevices(tapEnv, device, wifiMib,
						dataType, value, tableIndex);
				if (CommonMethods.isNotNull(response) && !response.equalsIgnoreCase(value)) {
					result.setErrorMessage(
							"Unable to find log message for wifi config change attempt when disabled using snmp");
					result.setStatus(CommonMethods
							.isNotNull(BroadBandCommonUtils.searchLogFilesInAtomOrArmConsoleByPolling(device, tapEnv,
									BroadBandTraceConstants.LOG_MESSAGE_WIFI_CHANGE_ATTEMPT_WHEN_FORCE_DISABLED,
									BroadBandTestConstants.LOCATION_WIFI_LOG,
									BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS,
									BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS)));
				}
			}
		} catch (Exception e) {
			LOGGER.error(
					"Exception occured while executing method verifyWiFiSnmpConfigDisabledStep: " + e.getMessage());
		}

		LOGGER.debug("ENDING METHOD: verifyWiFiSnmpConfigDisabledStep");
		return result;
	}

	/**
	 * Method to exeute step to verify wifi configuration using webpa is blocked
	 * when radio is force disabled
	 * 
	 * @param device    Dut instance
	 * @param parameter wifi parameter to verify
	 * @param dataType  data type of parameter
	 * @param value     value to be set
	 * 
	 * @return true if write not allowed and log message is present
	 * 
	 * @author Ashwin Sankarasubramanian
	 * @refactor Athira
	 */
	public static BroadBandResultObject verifyWiFiWebPaConfigDisabledStep(Dut device, String parameter, int dataType,
			String value) {
		LOGGER.debug("STARTING METHOD: verifyWiFiWebPaConfigDisabledStep");
		BroadBandResultObject result = new BroadBandResultObject();
		result.setStatus(false);
		try {
			result.setErrorMessage("Failed to clear WiFilog file");
			if ((CommonMethods.isAtomSyncAvailable(device, tapEnv))
					? BroadBandCommonUtils.clearAtomLogFile(tapEnv, device, BroadBandTestConstants.LOCATION_WIFI_LOG)
					: CommonUtils.clearLogFile(tapEnv, device, BroadBandTestConstants.LOCATION_WIFI_LOG)) {
				result.setErrorMessage("Able to set wifi parameter: " + parameter + " with value: " + value
						+ " using webpa when radio force disabled");
				if (!BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv, parameter, dataType,
						value)) {
					result.setErrorMessage(
							"Unable to find log message for wifi config change attempt when disabled using webpa");
					result.setStatus(CommonMethods
							.isNotNull(BroadBandCommonUtils.searchLogFilesInAtomOrArmConsoleByPolling(device, tapEnv,
									BroadBandTraceConstants.LOG_MESSAGE_WIFI_CHANGE_ATTEMPT_WHEN_FORCE_DISABLED,
									BroadBandTestConstants.LOCATION_WIFI_LOG,
									BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS,
									BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS)));
				}
			}
		} catch (Exception e) {
			LOGGER.error(
					"Exception occured while executing method verifyWiFiWebPaConfigDisabledStep: " + e.getMessage());
		}

		LOGGER.debug("ENDING METHOD: verifyWiFiWebPaConfigDisabledStep");
		return result;
	}

	/**
	 * Method to exeute step to verify wifi configuration using dmcli is blocked
	 * when radio is force disabled
	 * 
	 * @param device    Dut instance
	 * @param parameter wifi parameter to verify
	 * @param dataType  data type of parameter
	 * @param value     value to be set
	 * 
	 * @return true if write not allowed and log message is present
	 * 
	 * @author Ashwin Sankarasubramanian
	 * @refactor Athira
	 */
	public static BroadBandResultObject verifyWiFiDmcliConfigDisabledStep(Dut device, String parameter, String dataType,
			String value) {
		LOGGER.debug("STARTING METHOD: verifyWiFiDmcliConfigDisabledStep");
		BroadBandResultObject result = new BroadBandResultObject();
		result.setStatus(false);
		try {
			result.setErrorMessage("Failed to clear WiFilog file");
			if ((CommonMethods.isAtomSyncAvailable(device, tapEnv))
					? BroadBandCommonUtils.clearAtomLogFile(tapEnv, device, BroadBandTestConstants.LOCATION_WIFI_LOG)
					: CommonUtils.clearLogFile(tapEnv, device, BroadBandTestConstants.LOCATION_WIFI_LOG)) {
				result.setErrorMessage("Able to set wifi parameter: " + parameter + " with value: " + value
						+ " using dmcli when radio force disabled");
				if (!DmcliUtils.setParameterValueUsingDmcliCommand(device, tapEnv, parameter, dataType, value)) {
					result.setErrorMessage(
							"Unable to find log message for wifi config change attempt when disabled using dmcli");
					result.setStatus(CommonMethods
							.isNotNull(BroadBandCommonUtils.searchLogFilesInAtomOrArmConsoleByPolling(device, tapEnv,
									BroadBandTraceConstants.LOG_MESSAGE_WIFI_CHANGE_ATTEMPT_WHEN_FORCE_DISABLED,
									BroadBandTestConstants.LOCATION_WIFI_LOG,
									BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS,
									BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS)));
				}
			}
		} catch (Exception e) {
			LOGGER.error(
					"Exception occured while executing method verifyWiFiDmcliConfigDisabledStep: " + e.getMessage());
		}

		LOGGER.debug("ENDING METHOD: verifyWiFiDmcliConfigDisabledStep");
		return result;
	}

	/**
	 * Method to retrieve mac address from device
	 * 
	 * @param wifiClientDevice
	 * @param device
	 * @return macAddressRetrievedFromClient
	 * @refactor Athira
	 */
	public static String retrieveMacAddressOfWifiConnectedClient(Dut wifiClientDevice, Dut device) {
		String macAddressRetrievedFromClient = null;
		macAddressRetrievedFromClient = ((Device) wifiClientDevice).getConnectedDeviceInfo().getWifiMacAddress();
		LOGGER.info("Wifi Mac Address of the Connected client having 2.4GHZ Capability obtained is:"
				+ macAddressRetrievedFromClient);
		return macAddressRetrievedFromClient;
	}

	/**
	 * Verify the both ssid's are visible in connected client
	 * 
	 * @param device         {@link Dut} Reference
	 * @param tapEnv         {@link AutomaticsTapApi} Reference
	 * @param deviceToVerify Connected client device device instance
	 * @param wifiType       Type will be Public WiFi or Private WiFi
	 * @param isSsidVisible  Status of radio 2.4/5 GHz band is enable/disabled
	 * @return true if SSID is visible/invisible in client device based on
	 *         isSsidVisible
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean validateBothSsidVisibleStateInConnectedClient(Dut device, AutomaticsTapApi tapEnv,
			Dut deviceToVerify, String wifiType, boolean isSsidVisible) {
		boolean visibleStatus2Ghz = false;
		boolean visibleStatus5Ghz = false;
		String ssid2Ghz = null;
		String ssid5Ghz = null;
		boolean status = false;
		if (wifiType.equalsIgnoreCase(BroadBandTestConstants.WIFI_PRIVATE)) {
			ssid2Ghz = BroadBandConnectedClientUtils.getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv,
					WiFiFrequencyBand.WIFI_BAND_2_GHZ);
			ssid5Ghz = BroadBandConnectedClientUtils.getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv,
					WiFiFrequencyBand.WIFI_BAND_5_GHZ);
		} else if (wifiType.equalsIgnoreCase(BroadBandTestConstants.PUBLIC_WIFI_TYPE)) {
			ssid5Ghz = BroadBandConnectedClientUtils.getPublicSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv,
					WiFiFrequencyBand.WIFI_BAND_5_GHZ);
		}

		else if (wifiType.equalsIgnoreCase(BroadBandTestConstants.SECURED_WIFI_PUBLIC)) {
			ssid5Ghz = BroadBandConnectedClientUtils.getSecuredPublicSsidNameFromGatewayUsingWebPaOrDmcli(device,
					tapEnv, WiFiFrequencyBand.WIFI_BAND_5_GHZ);
		}

		if (wifiType.equalsIgnoreCase(BroadBandTestConstants.WIFI_PRIVATE)) {
			LOGGER.info("SSID 2GHZ :" + ssid2Ghz);
			LOGGER.info("SSID 5GHZ :" + ssid5Ghz);
			visibleStatus2Ghz = BroadBandWiFiUtils.scanAndVerifyVisibleSsidInCnctdClintDeviceWithPollDuration(
					deviceToVerify, tapEnv, ssid2Ghz, isSsidVisible, BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS);
			if (visibleStatus2Ghz) {
				visibleStatus5Ghz = BroadBandWiFiUtils.scanAndVerifyVisibleSsidInCnctdClintDeviceWithPollDuration(
						deviceToVerify, tapEnv, ssid5Ghz, isSsidVisible, BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS);
			}
			LOGGER.info(" VALIDATED " + BroadBandTestConstants.WIFI_PRIVATE.toUpperCase()
					+ " 2.4 GHZ SSID BROADCASTED IN CONNECTED CLIENT : EXPECTED :"
					+ (isSsidVisible ? " SHOULD BE " : " SHOULD NOT BE ") + " VISIBLE " + " ACTUAL : "
					+ visibleStatus2Ghz);
			LOGGER.info(" VALIDATED " + BroadBandTestConstants.WIFI_PRIVATE.toUpperCase()
					+ " 5 GHZ SSID BROADCASTED IN CONNECTED CLIENT : EXPECTED :"
					+ (isSsidVisible ? " SHOULD BE " : " SHOULD NOT BE ") + " VISIBLE " + " ACTUAL : "
					+ visibleStatus5Ghz);
			status = visibleStatus2Ghz && visibleStatus5Ghz;
		} else {
			if (wifiType.equalsIgnoreCase(BroadBandTestConstants.SECURED_WIFI_PUBLIC)) {
				LOGGER.info("SSID 5GHZ :" + BroadBandTestConstants.SECURED_WIFI_PUBLIC.toUpperCase() + ":" + ssid5Ghz);
				visibleStatus5Ghz = BroadBandWiFiUtils.scanAndVerifyVisibleSsidInCnctdClintDeviceWithPollDuration(
						deviceToVerify, tapEnv, ssid5Ghz, isSsidVisible, BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS);
				LOGGER.info(" VALIDATED " + BroadBandTestConstants.SECURED_WIFI_PUBLIC.toUpperCase()
						+ " 5 GHZ SSID BROADCASTED IN CONNECTED CLIENT : EXPECTED :"
						+ (isSsidVisible ? " SHOULD BE " : " SHOULD NOT BE ") + " VISIBLE " + " ACTUAL : "
						+ visibleStatus5Ghz);
				status = visibleStatus5Ghz;
			} else {
				LOGGER.info("SSID 5GHZ :" + BroadBandTestConstants.PUBLIC_WIFI_TYPE.toUpperCase() + ":" + ssid5Ghz);
				visibleStatus5Ghz = BroadBandWiFiUtils.scanAndVerifyVisibleSsidInCnctdClintDeviceWithPollDuration(
						deviceToVerify, tapEnv, ssid5Ghz, isSsidVisible, BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS);
				LOGGER.info(" VALIDATED " + BroadBandTestConstants.PUBLIC_WIFI_TYPE.toUpperCase()
						+ " 5 GHZ SSID BROADCASTED IN CONNECTED CLIENT : EXPECTED :"
						+ (isSsidVisible ? " SHOULD BE " : " SHOULD NOT BE ") + " VISIBLE " + " ACTUAL : "
						+ visibleStatus5Ghz);
				status = visibleStatus5Ghz;
			}
		}
		return status;
	}

	/**
	 * Method to verify the Public Wifi connection status for 2.4/5 GHz .
	 * 
	 * @param device          {@link Dut}
	 * @param testId          Test case ID
	 * @param deviceConnected Device Connected
	 * @param stepNumber      Step Number
	 * @refactor Govardhan
	 */
	public static void validatePublicWifiConnectionStatus(Dut device, String testCaseId, Dut deviceConnected,
			int stepNumber) {
		LOGGER.debug("STARTING METHOD : validatePublicWifiConnectionStatus()");
		String stepNum = null;
		boolean status = false;
		String errorMessage = null;
		try {
			/**
			 * SETP : VERIFY IPV4 INVERFACE & INTERNET CONNECTION
			 */
			stepNumber++;
			verifyPublicWifiIpv4ConnectionInterface(device, testCaseId, deviceConnected, stepNumber);
		} catch (TestException e) {
			LOGGER.error(errorMessage);
			CommonUtils.updateTestStatusDuringException(tapEnv, device, testCaseId, stepNum, status, errorMessage,
					false);
		}

		LOGGER.debug("ENDING METHOD : validatePublicWifiConnectionStatus()");
	}

	/**
	 * Scan the client device for the networks available and return whether the
	 * device is visible with poll duration when the 2.4/5 Ghz ssid is
	 * enabled/disabled
	 * 
	 * @param clientDevice  Connected client device instance
	 * @param tapEnv        {@link AutomaticsTapApi} Reference
	 * @param wifiBand      The value of 2.4/5 GHz band
	 * @param isSsidVisible Status of 2.4/5 GHz ssid is enable/disabled
	 * @param pollDuration  poll duration
	 * @return true if SSID is visible/invisible in client device based on
	 *         isSsidVisible
	 * 
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean scanAndVerifyVisibleSsidInCnctdClintDeviceWithPollDuration(Dut clientDevice,
			AutomaticsTapApi tapEnv, String ssidName, boolean isSsidVisible, long pollDuration) {
		LOGGER.debug("STARTING METHOD: scanAndVerifyVisibleSsidInCnctdClintDeviceWithPollDuration");
		boolean status = false;
		long startTime = System.currentTimeMillis();
		do {
			if (isSsidVisible) {
				status = BroadBandConnectedClientUtils
						.scanAndVerifyVisibleSsidFromWiFiConnectedClientDevice(clientDevice, ssidName, tapEnv);
			} else {
				status = !BroadBandConnectedClientUtils
						.scanAndVerifyVisibleSsidFromWiFiConnectedClientDevice(clientDevice, ssidName, tapEnv);
			}
		} while ((System.currentTimeMillis() - startTime) < pollDuration && !status
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
		LOGGER.debug("ENDING METHOD: scanAndVerifyVisibleSsidInCnctdClintDeviceWithPollDuration");
		return status;
	}

	/**
	 * Method to verify the Public Wifi IPv4 Connection interface & Internet
	 * connectivity for 2.4/5 GHz .
	 * 
	 * @param device          {@link Dut}
	 * @param testId          Test case ID
	 * @param deviceConnected Device Connected
	 * @param stepNumber      Step Number
	 */
	public static void verifyPublicWifiIpv4ConnectionInterface(Dut device, String testId, Dut deviceConnected,
			int stepNumber) throws TestException {
		LOGGER.debug("STARTING METHOD : verifyPublicWifiIpv4ConnectionInterface()");
		String step = null;
		boolean status = false;
		String errorMessage = null;
		BroadBandResultObject result = null;

		String osType = ((Device) deviceConnected).getOsType();
		String model = ((Device) deviceConnected).getModel();

		try {
			/**
			 * STEP : VERIFY THE CORRECT IPV4 ADDRESS FOR CONNECTED CLIENT
			 */
			step = "S" + stepNumber;
			status = false;
			LOGGER.info("***************************************************************************************");
			LOGGER.info("STEP " + stepNumber
					+ ": DESCRIPTION :VERIFY THE CORRECT IPV4 ADDRESS FOR CONNECTED CLIENT DEVICE MODEL " + model);
			LOGGER.info("STEP " + stepNumber
					+ ": ACTION : EXECUTE COMMAND, WINDOWS : ipconfig |grep -A 10 'Wireless LAN adapter Wi-Fi' |grep -i 'IPv4 Address' or LINUX : ifconfig | grep 'inet' or ON THE CONNECTED CLIENT");
			LOGGER.info("STEP " + stepNumber
					+ ": EXPECTED : IT SHOULD RETURN THE CORRECT IPV4 ADDRESS FOR DEVICE MODEL " + model);
			LOGGER.info("***************************************************************************************");
			errorMessage = "UNABLE TO GET THE CORRECT IPV4 ADDRESS FROM CLIENT MODEL " + model;
			status = BroadBandConnectedClientUtils.verifyIpv4AddressForWiFiOrLanInterfaceConnectedWithRdkbDevice(osType,
					deviceConnected, tapEnv);
			if (status) {
				LOGGER.info("STEP " + stepNumber
						+ " : ACTUAL : SUCCESSFYLLY VERIFIED CORRECT IPV4 ADDRESS FROM CLIENT DEVICE MODEL : " + model);
			} else {
				LOGGER.error("STEP " + stepNumber + " : ACTUAL : " + errorMessage);
			}
			LOGGER.info("***************************************************************************************");
			tapEnv.updateExecutionStatus(device, testId, step, status, errorMessage, false);

		} catch (TestException e) {
			LOGGER.error(errorMessage);
			CommonUtils.updateTestStatusDuringException(tapEnv, device, testId, step, status, errorMessage, false);
		}

		try {
			/**
			 * STEP : VERIFY THE INTERNET CONNECTIVITY IN THE CLIENT WITH GIVEN GHZ SSID
			 * INTERFACE USING IPV4 .
			 */
			stepNumber++;
			step = "S" + stepNumber;
			status = false;
			LOGGER.info("***************************************************************************************");
			LOGGER.info("STEP " + stepNumber
					+ ": DESCRIPTION : VERIFY THE INTERNET CONNECTIVITY IN THE CLIENT CONNECTED USING IPV4 FOR DEVICE MODEL : "
					+ model);
			LOGGER.info("STEP " + stepNumber
					+ ": ACTION : EXECUTE COMMAND, WINDOWS : curl -4 -v 'www.google.com'  | grep '200 OK' OR ping -4 -n 5 google.com, LINUX : curl -4 -f --interface <interfaceName> www.google.com | grep '200 OK' OR ping -4 -n 5 google.com ON THE CONNECTED CLIENT");
			LOGGER.info("STEP " + stepNumber
					+ ": EXPECTED : THE INTERNET CONNECTIVITY MUST BE AVAILABLE INTERFACE USING IPV4 ");
			LOGGER.info("***************************************************************************************");
			errorMessage = "NOT ABLE TO ACCESS THE SITE 'www.google.com' FROM CONNECTED CLIENT WITH USING IPV4 FOR DEVICE MODEL : "
					+ model;
			long startTime = System.currentTimeMillis();
			do {
				result = BroadBandConnectedClientUtils.verifyInternetIsAccessibleInConnectedClientUsingCurl(tapEnv,
						deviceConnected,
						BroadBandTestConstants.URL_HTTPS + BroadBandTestConstants.STRING_GOOGLE_HOST_ADDRESS,
						BroadBandTestConstants.IP_VERSION4);
				status = result.isStatus();
				errorMessage = result.getErrorMessage();
			} while (!status && (System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
			if (!status) {
				errorMessage = "PIGN OPERATION FAILED TO ACCESS THE SITE 'www.google.com' USING IPV4 ";
				status = ConnectedNattedClientsUtils.verifyPingConnectionForIpv4AndIpv6(deviceConnected, tapEnv,
						BroadBandTestConstants.PING_TO_GOOGLE, BroadBandTestConstants.IP_VERSION4);
			}
			if (status) {
				LOGGER.info("STEP " + stepNumber
						+ " : ACTUAL : CONNECTED CLIENT HAS INTERNET CONNECTIVITY USING IPV4 FOR DEVICE MODEL : "
						+ model);
			} else {
				LOGGER.error("STEP " + stepNumber + " : ACTUAL : " + errorMessage);
			}
			LOGGER.info("***************************************************************************************");
			tapEnv.updateExecutionStatus(device, testId, step, status, errorMessage, false);

		} catch (TestException e) {
			LOGGER.error(errorMessage);
			CommonUtils.updateTestStatusDuringException(tapEnv, device, testId, step, status, errorMessage, false);
		}

		LOGGER.debug("ENDING METHOD : verifyPublicWifiIpv4ConnectionInterface()");
	}

	/**
	 * Test step method used to disconnect the private wifi for given GHz frequency
	 * band
	 * 
	 * @param device             instance of{@link Dut}
	 * @param testCaseId         Test case ID
	 * @param deviceToDisconnect Device to disconnect the WiFi
	 * @param wifiBand           Frequency band to connect the wifi
	 * @param stepNumber         Step Number
	 * 
	 * @refactor yamini.s
	 */
	public static void executeTestStepToDisconnectPrivateWiFi(Dut device, String testCaseId, Dut deviceToDisconnect,
			String wifiBand, int stepNumber) {
		/**
		 * STEP : VERIFY DISCONNECTING THE CLIENT FROM GIVEN GHZ PRIVATE WIFI SSID
		 */
		String stepNum = "S" + stepNumber;
		boolean status = false;
		String errorMessage = null;
		LOGGER.info("***************************************************************************************");
		LOGGER.info("STEP :  " + stepNumber + " : DESCRIPTION : VERIFY DISCONNECTING THE  CLIENT CONNECTED WITH "
				+ wifiBand + " SSID");
		LOGGER.info("STEP :  " + stepNumber
				+ " : ACTION : EXECUTE COMMAND : FOR WINDOWS :netsh wlan disconnect ,For Linux :nmcli con down id <ssid>");
		LOGGER.info("STEP :  " + stepNumber + " : EXPECTED: THE CLIENT MUST BE DISCONNECTED FROM " + wifiBand
				+ " SSID SUCCESSFULLY");
		LOGGER.info("***************************************************************************************");
		errorMessage = "UNABLE TO DISCONNECT THE CLIENT CONNECTED WITH " + wifiBand + " SSID";
		status = ConnectedNattedClientsUtils.disconnectSSID(deviceToDisconnect, tapEnv,
				BroadBandConnectedClientUtils.getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv,
						wifiBand.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ)
								? WiFiFrequencyBand.WIFI_BAND_2_GHZ
								: WiFiFrequencyBand.WIFI_BAND_5_GHZ));
		if (status) {
			LOGGER.info("STEP :  " + stepNumber + " : ACTUAL : SUCCESSFULLY DISCONNECTED THE CLIENT CONNECTED WITH "
					+ wifiBand + " SSID");
		} else {
			LOGGER.error("STEP :  " + stepNumber + " : ACTUAL : " + errorMessage);
		}
		LOGGER.info("***************************************************************************************");
		tapEnv.updateExecutionStatus(device, testCaseId, stepNum, status, errorMessage, false);
	}

	/**
	 * Test step method used to connect the private wifi for given GHz frequency
	 * band
	 * 
	 * @param device          instance of{@link Dut}
	 * @param testCaseId      Test case ID
	 * @param deviceToConnect Device to connect the WiFi
	 * @param wifiBand        Frequency band to connect the wifi
	 * @param stepNumber      Step Number
	 * 
	 * @Refactor Sruthi Santhosh
	 */
	public static void executeTestStepToConnectPrivateWiFiOpenSecurityMode(Dut device, String testCaseId,
			Dut deviceToConnect, String wifiBand, int stepNumber) {
		/**
		 * STEP : CONNECT THE CLIENT INTO GIVEN GHZ PRIVATE SSID WITH OPEN SECURITY MODE
		 * AND VERIFY CONNECTION STATUS
		 */
		String stepNum = "S" + stepNumber;
		boolean status = false;
		String errorMessage = null;
		LOGGER.info("#####################################################################################");
		LOGGER.info("STEP " + stepNumber + " : DESCRIPTION : CONNECT A CLIENT TO THE PRIVATE WIFI " + wifiBand
				+ " SSID WITH OPEN SECURITY MODE.");
		LOGGER.info("STEP " + stepNumber + " : ACTION : CONNECT TO " + wifiBand
				+ " PRIVATE WIFI USING BELOW COMMANDS : FOR LINUX :nmcli dev wifi connect <ssid> FOR WINDOWS: netsh wlan connect ssid=<ssid>");
		LOGGER.info("STEP " + stepNumber + " : EXPECTED : CONNECTION SHOULD BE SUCCESSFUL FOR CLIENT WITH " + wifiBand
				+ " PRIVATE WIFI SSID WITH OPEN SECURITY MODE");
		LOGGER.info("#######################################################################################");
		errorMessage = "PRIVATE WIFI CONNECTION FAILED FOR " + wifiBand + " SSID IN CLIENT WITH OPEN SECURITY MODE";

		status = ConnectedNattedClientsUtils.connectToSSID(deviceToConnect, tapEnv,
				BroadBandConnectedClientUtils.getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv,
						wifiBand.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ)
								? WiFiFrequencyBand.WIFI_BAND_2_GHZ
								: WiFiFrequencyBand.WIFI_BAND_5_GHZ),
				BroadBandConnectedClientUtils.getSsidPassphraseFromGatewayUsingWebPaOrDmcli(device, tapEnv,
						wifiBand.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ)
								? WiFiFrequencyBand.WIFI_BAND_2_GHZ
								: WiFiFrequencyBand.WIFI_BAND_5_GHZ),
				BroadBandConnectedClientTestConstants.SECURITY_MODE_OPEN, false);

		if (status) {
			LOGGER.info("STEP " + stepNumber + " : ACTUAL : SUCCESSFYLLY CONNECTED THE CLIENT WITH " + wifiBand
					+ " GHZ PRIVATE SSID WITH OPEN SECURITY MODE.");
		} else {
			LOGGER.error("STEP " + stepNumber + " : ACTUAL : " + errorMessage);
		}
		LOGGER.info("#######################################################################################");
		tapEnv.updateExecutionStatus(device, testCaseId, stepNum, status, errorMessage, true);
	}

	/**
	 * Method used to verify the default operating standard bandwidth.
	 * 
	 * @param device   {@link Dut} Reference
	 * @param tapEnv   {@link AutomaticsTapApi} Reference
	 * @param response webpa response
	 * @param wifiBand The value of 2.4/5 GHz band
	 * @return status of default operating standards
	 * 
	 */
	public static boolean validateDefaultOperatingBandwidth(Dut device, AutomaticsTapApi tapEnv, String response,
			String wifiBand) {
		LOGGER.debug("STARTING METHOD: validateDefaultOperatingBandwidth");
		boolean status = false;
		boolean isFibreOrBussinessClassDevice = false;

		isFibreOrBussinessClassDevice = DeviceModeHandler.isFibreDevice(device)
				|| DeviceModeHandler.isBusinessClassDevice(device);

		if (wifiBand.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ)) {
			if (isFibreOrBussinessClassDevice) {
				status = response.equalsIgnoreCase(BroadBandTestConstants.OPERATING_BANDWIDTH_40_MMZ);
			} else {
				status = response.equalsIgnoreCase(BroadBandTestConstants.OPERATING_BANDWIDTH_20_MMZ);
			}
		} else {
			if (!DeviceModeHandler.isRPIDevice(device)) {
				status = response.equalsIgnoreCase(BroadBandTestConstants.OPERATING_BANDWIDTH_80_MMZ);
			} else {
				status = response.equalsIgnoreCase(BroadBandTestConstants.OPERATING_BANDWIDTH_40_MMZ);
			}
		}
		LOGGER.debug("ENDING METHOD: validateDefaultOperatingBandwidth");
		return status;
	}

	/**
	 * Method to grep bssid from gateway
	 * 
	 * @param device {@link Instanceof Dut}
	 * @param tapEnv {@link Instanceof AutomaticsTapApi}
	 * @param band   {@link Instanceof WiFiFrequencyBand}
	 * @return Bssid of the private wifi
	 * @refactor Rakesh C N
	 */

	public static String getBssidFromGateway(Dut device, AutomaticsTapApi tapEnv, WiFiFrequencyBand band) {
		LOGGER.debug("STARTING METHOD: getBssidFromGateway ");
		String bssid = null;
		String response = null;
		String command = null;
		try {
			if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
				command = (band == WiFiFrequencyBand.WIFI_BAND_2_GHZ)
						? BroadBandTestConstants.CMD_IW_CONFIG + " " + BroadBandTestConstants.INTERFACE_ATH0
						: BroadBandTestConstants.CMD_IW_CONFIG + " " + BroadBandTestConstants.INTERFACE_ATH1;
				response = tapEnv.executeCommandOnAtom(device, command);
			} else if (DeviceModeHandler.isFibreDevice(device)) {
				command = (band == WiFiFrequencyBand.WIFI_BAND_2_GHZ) ? BroadBandTestConstants.INTERFACE_FIBER_PRIVATE_2
						: BroadBandTestConstants.INTERFACE_FIBER_PRIVATE_5;
				response = tapEnv.executeCommandUsingSsh(device, command);
			} else {
				command = (band == WiFiFrequencyBand.WIFI_BAND_2_GHZ)
						? BroadBandWebPaUtils.getWiFiInterface(device, tapEnv, BroadBandTestConstants.BAND_2_4GHZ,
								BroadBandTestConstants.PRIVATE_WIFI_TYPE, BroadBandTestConstants.BSSID_PARAM)
						: BroadBandWebPaUtils.getWiFiInterface(device, tapEnv, BroadBandTestConstants.BAND_5GHZ,
								BroadBandTestConstants.PRIVATE_WIFI_TYPE, BroadBandTestConstants.BSSID_PARAM);
				LOGGER.info("command :" + command);
				response = tapEnv.executeCommandUsingSsh(device, command);
				LOGGER.info("Response :" + response);
			}
			if (CommonMethods.isNotNull(response.trim()))
				bssid = CommonMethods.patternFinder(response.trim(), BroadBandTestConstants.REGEX_GREP_BSSID);
		} catch (Exception e) {
			LOGGER.error("Exception occured while trying to grep bssid from gateway");
		}
		LOGGER.debug("ENDING METHOD: getBssidFromGateway ");
		return bssid;
	}

	/**
	 * Test to validate if wifi driver is Intel
	 * 
	 * @param client Instance of Dut
	 * @param tapEnv Instance of AutomaticsTapApi
	 * @return validation result
	 * 
	 * @refactor yamini.s
	 */

	public static boolean validateIfWifiDriverIsIntelForClient(Dut client, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: validateIfWifiDriverIsIntelForClient ");
		boolean result = false;
		String response = null;
		response = tapEnv.executeCommandOnOneIPClients(client, BroadBandTestConstants.CMD_GET_SYSTEM_INFO_WIRELESS);
		result = CommonMethods.isNotNull(response) && response.contains(BroadBandTestConstants.WIFI_DRIVER_INTEL);
		LOGGER.debug("ENDING METHOD: validateIfWifiDriverIsIntelForClient ");
		return result;
	}

	/**
	 * Verify the both ssid's are visible in connected client
	 * 
	 * @param device         {@link Dut} Reference
	 * @param tapEnv         {@link AutomaticsTapApi} Reference
	 * @param deviceToVerify Connected client device device instance
	 * @param wifiType       Type will be Public WiFi or Private WiFi
	 * @param isSsidVisible  Status of radio 2.4/5 GHz band is enable/disabled
	 * @return true if SSID is visible/invisible in client device based on
	 *         isSsidVisible
	 * 
	 * @refactor Athira
	 */
	public static boolean validateVisibleStateOfBothSsidInConnectedClient(Dut device, AutomaticsTapApi tapEnv,
			Dut deviceToVerify, String wifiType, boolean isSsidVisible) {
		boolean visibleStatus2Ghz = false;
		boolean visibleStatus5Ghz = false;
		String ssid2Ghz = null;
		String ssid5Ghz = null;
		LOGGER.info("validateVisibleStateOfBothSsidInConnectedClient started ");
		LOGGER.info("SSID 5GHZ :" + ssid5Ghz);
		LOGGER.info("wifiType :" + wifiType);
		LOGGER.info("SSID 5GHZ :" + ssid5Ghz);

		if (wifiType.equalsIgnoreCase(BroadBandTestConstants.WIFI_PRIVATE)) {
			ssid2Ghz = BroadBandConnectedClientUtils.getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv,
					WiFiFrequencyBand.WIFI_BAND_2_GHZ);
			ssid5Ghz = BroadBandConnectedClientUtils.getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv,
					WiFiFrequencyBand.WIFI_BAND_5_GHZ);
		} else if (wifiType.equalsIgnoreCase(BroadBandTestConstants.PUBLIC_WIFI_TYPE)) {
			ssid2Ghz = BroadBandConnectedClientUtils.getPublicSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv,
					WiFiFrequencyBand.WIFI_BAND_2_GHZ);
			ssid5Ghz = BroadBandConnectedClientUtils.getPublicSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv,
					WiFiFrequencyBand.WIFI_BAND_5_GHZ);
		} else if (wifiType.equalsIgnoreCase(BroadBandTestConstants.SECURED_WIFI_PUBLIC)) {
			ssid2Ghz = BroadBandConnectedClientUtils.getSecuredPublicSsidNameFromGatewayUsingWebPaOrDmcli(device,
					tapEnv, WiFiFrequencyBand.WIFI_BAND_2_GHZ);
			ssid5Ghz = BroadBandConnectedClientUtils.getSecuredPublicSsidNameFromGatewayUsingWebPaOrDmcli(device,
					tapEnv, WiFiFrequencyBand.WIFI_BAND_5_GHZ);
		}
		LOGGER.info("SSID 2GHZ :" + ssid2Ghz);
		LOGGER.info("SSID 5GHZ :" + ssid5Ghz);
		visibleStatus2Ghz = BroadBandWiFiUtils.scanAndVerifyVisibleSsidInCnctdClintDeviceWithPollDuration(
				deviceToVerify, tapEnv, ssid2Ghz, isSsidVisible, BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS);
		if (visibleStatus2Ghz) {
			visibleStatus5Ghz = BroadBandWiFiUtils.scanAndVerifyVisibleSsidInCnctdClintDeviceWithPollDuration(
					deviceToVerify, tapEnv, ssid5Ghz, isSsidVisible, BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS);
		}
		LOGGER.info(" VALIDATED "
				+ (wifiType.equalsIgnoreCase(BroadBandTestConstants.WIFI_PRIVATE)
						? BroadBandTestConstants.WIFI_PRIVATE.toUpperCase()
						: BroadBandTestConstants.WIFI_PUBLIC.toUpperCase())
				+ " 2.4 GHZ SSID BROADCASTED IN CONNECTED CLIENT : EXPECTED :"
				+ (isSsidVisible ? " SHOULD BE " : " SHOULD NOT BE ") + " VISIBLE " + " ACTUAL : " + visibleStatus2Ghz);
		LOGGER.info(" VALIDATED "
				+ (wifiType.equalsIgnoreCase(BroadBandTestConstants.WIFI_PRIVATE)
						? BroadBandTestConstants.WIFI_PRIVATE.toUpperCase()
						: BroadBandTestConstants.WIFI_PUBLIC.toUpperCase())
				+ " 5 GHZ SSID BROADCASTED IN CONNECTED CLIENT : EXPECTED :"
				+ (isSsidVisible ? " SHOULD BE " : " SHOULD NOT BE ") + " VISIBLE " + " ACTUAL : " + visibleStatus5Ghz);
		LOGGER.info("validateVisibleStateOfBothSsidInConnectedClient End ");
		return visibleStatus2Ghz && visibleStatus5Ghz;
	}

	/**
	 * Method to modify private wifi SSID and password for 2.4 and 5 Ghz
	 * 
	 * @param device
	 * @return setResult
	 * @refactor Athira
	 */

	public static boolean changeWiFiSsidAndPassphraseFor24And5Ghz(Dut device, WEBPA_AP_INDEXES indexes) {
		boolean setResult = false;

		List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
		Map<String, String> webpaResponse = null;
		String ssid_24 = BroadBandTestConstants.STRING_24_WIFI_SSID_PREFIX + Long.toString(System.currentTimeMillis())
				+ BroadBandTestConstants.CHARACTER_UNDER_SCORE + indexes.name();
		String ssid_5 = BroadBandTestConstants.STRING_5_WIFI_SSID_PREFIX + Long.toString(System.currentTimeMillis())
				+ BroadBandTestConstants.CHARACTER_UNDER_SCORE + indexes.name();
		LOGGER.info("The SSIDs for 2.4 and 5 GHz " + indexes + ": 2.4 GHz - " + ssid_24 + " 5 GHz - " + ssid_5);
		webPaParameters
				.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
						BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_NAME
								.replace(BroadBandTestConstants.TR181_NODE_REF, indexes.get24Ghz()),
						ssid_24, WebPaDataTypes.STRING.getValue()));
		webPaParameters
				.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
						BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_NAME
								.replace(BroadBandTestConstants.TR181_NODE_REF, indexes.get5Ghz()),
						ssid_5, WebPaDataTypes.STRING.getValue()));
		webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_ENABLE
						.replace(BroadBandTestConstants.TR181_NODE_REF, indexes.get24Ghz()),
				BroadBandTestConstants.TRUE, BroadBandTestConstants.CONSTANT_3));
		webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_ENABLE
						.replace(BroadBandTestConstants.TR181_NODE_REF, indexes.get5Ghz()),
				BroadBandTestConstants.TRUE, BroadBandTestConstants.CONSTANT_3));
		webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_SSIDADVERTISEMENT
						.replace(BroadBandTestConstants.TR181_NODE_REF, indexes.get24Ghz()),
				BroadBandTestConstants.TRUE, BroadBandTestConstants.CONSTANT_3));
		webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_SSIDADVERTISEMENT
						.replace(BroadBandTestConstants.TR181_NODE_REF, indexes.get5Ghz()),
				BroadBandTestConstants.TRUE, BroadBandTestConstants.CONSTANT_3));
		webpaResponse = tapEnv.executeMultipleWebPaSetCommands(device, webPaParameters);
		setResult = null != webpaResponse && !webpaResponse.isEmpty()
				&& BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
						BroadBandTestConstants.SUCCESS_TXT,
						webpaResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_NAME
								.replace(BroadBandTestConstants.TR181_NODE_REF, indexes.get24Ghz())))
				&& BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
						BroadBandTestConstants.SUCCESS_TXT,
						webpaResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_NAME
								.replace(BroadBandTestConstants.TR181_NODE_REF, indexes.get5Ghz())))
				&& BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
						BroadBandTestConstants.SUCCESS_TXT,
						webpaResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_ENABLE
								.replace(BroadBandTestConstants.TR181_NODE_REF, indexes.get24Ghz())))
				&& BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
						BroadBandTestConstants.SUCCESS_TXT,
						webpaResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_ENABLE
								.replace(BroadBandTestConstants.TR181_NODE_REF, indexes.get5Ghz())))
				&& BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
						BroadBandTestConstants.SUCCESS_TXT,
						webpaResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_SSIDADVERTISEMENT
								.replace(BroadBandTestConstants.TR181_NODE_REF, indexes.get24Ghz())))
				&& BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
						BroadBandTestConstants.SUCCESS_TXT,
						webpaResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_SSIDADVERTISEMENT
								.replace(BroadBandTestConstants.TR181_NODE_REF, indexes.get5Ghz())));

		return setResult;
	}

	/**
	 * Method to generate the traffic using ping or curl with multiple urls
	 * 
	 * @param device          {@link Dut}
	 * @param deviceConnected Device Connected
	 * @return status . Its return True,if ping or curl is success, Else False
	 * @refactor Said Hisham
	 */
	public static boolean generateTrafficUsingCurlOrPing(Dut device, Dut deviceConnected) {
		LOGGER.debug("STARTING METHOD : generateTrafficUsingCurlOrPing()");
		boolean status = false;
		int pingCount = 0;
		BroadBandResultObject resultObject = new BroadBandResultObject();
		List<String> listOfWebSitesToCurl = BroadBandTestConstants.getListOfWebSitesToCurl();
		long startTime = System.currentTimeMillis();
		LOGGER.info("Ping or Curl Operation startTime :" + startTime);
		do {
			for (String strUrl : listOfWebSitesToCurl) {
				resultObject = BroadBandConnectedClientUtils.verifyInternetIsAccessibleInConnectedClientUsingCurl(
						tapEnv, deviceConnected, strUrl, BroadBandTestConstants.IP_VERSION4);
				status = resultObject.isStatus();
				if (!status) {
					status = ConnectedNattedClientsUtils.verifyPingConnectionForIpv4AndIpv6(deviceConnected, tapEnv,
							strUrl.replace(BroadBandTestConstants.URL_HTTPS, ""), BroadBandTestConstants.IP_VERSION4);
				}
				if (status) {
					pingCount = pingCount + 1;
				}
			}
			LOGGER.info("PING OR CURL OPERATION COMPLETED");
		} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.TWO_MINUTES
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.TEN_SECOND_IN_MILLIS));
		LOGGER.info("Ping or Curl Operation EndTime :" + System.currentTimeMillis());
		LOGGER.info("Ping or Curl Operation Count :" + pingCount);
		if (pingCount > 0) {
			status = true;
		}
		LOGGER.debug("ENDING METHOD : generateTrafficUsingCurlOrPing()");
		return status;
	}

	/**
	 * Utility method used to validate the invalid subent mask values for
	 * residential and commercial devices
	 * 
	 * @param settop {@link Instanceof Dut}
	 * @return objResult {@link Instanceof BroadBandResultObject}
	 * @refactor Said Hisham
	 */
	public static BroadBandResultObject validateInValidSubnetMaskValues(Dut settop, String previousSetValue) {
		LOGGER.debug("STARTING METHOD : validateInValidSubnetMaskValues()");
		BroadBandResultObject objResult = new BroadBandResultObject();
		String errorMessage = "";
		boolean status = false;
		int resultCount = BroadBandTestConstants.CONSTANT_0;
		boolean result = false;
		String currentTimeStamp = null;
		List<String> listOfSubnetMask = null;
		String subnetMaskFromProperty = null;
		try {
			subnetMaskFromProperty = AutomaticsTapApi.getAutomaticsPropsValueByResolvingPlatform(settop,
					BroadBandTestConstants.PROP_KEY_INVALID_SUBNETMAK_VALUES);
			listOfSubnetMask = Arrays.asList(subnetMaskFromProperty.split(BroadBandTestConstants.COMMA));
			LOGGER.info("Current subnetmask value:" + previousSetValue);
			LOGGER.info("List of Invalid Subnet masks:" + listOfSubnetMask);
			LOGGER.info("List of Invalid Subnet masks size:" + listOfSubnetMask.size());
			if (listOfSubnetMask.size() > BroadBandTestConstants.CONSTANT_0) {
				for (String subnetMask : listOfSubnetMask) {
					currentTimeStamp = BroadBandCommonUtils.getCurrentTimeStampOnDevice(tapEnv, settop);
					tapEnv.waitTill(BroadBandTestConstants.THREE_SECONDS_IN_MILLIS);
					if (!BroadBandWebPaUtils.setVerifyWebPAInPolledDuration(settop, tapEnv,
							BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_LAN_SUBNET, WebPaDataTypes.STRING.getValue(),
							subnetMask, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS,
							BroadBandTestConstants.TEN_SECOND_IN_MILLIS)) {
						LOGGER.info("Unable to set the invalid subnet mask values");
//						if (BroadBandCommonUtils.validateLogMessageInPamLog(settop, tapEnv,
//								BroadBandTestConstants.BOOLEAN_VALUE_TRUE, currentTimeStamp)
//								&& BroadBandCommonUtils.validateLogMessageInPamLog(settop, tapEnv,
//										BroadBandTestConstants.BOOLEAN_VALUE_FALSE, currentTimeStamp)) {
						if (BroadBandCommonUtils.validateLogMessageInPamLog(settop, tapEnv,
								BroadBandTestConstants.BOOLEAN_VALUE_FALSE, currentTimeStamp)) {
							LOGGER.info("Log message validation successful in "
									+ BroadBandTestConstants.COMMAND_NTP_LOG_FILE);
							result = BroadBandWebPaUtils.getAndVerifyWebpaValueInPolledDuration(settop, tapEnv,
									BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_LAN_SUBNET, previousSetValue,
									BroadBandTestConstants.ONE_MINUTE_IN_MILLIS,
									BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
							if (result) {
								LOGGER.info("Successfully verified the previouse set subnet mask value is persisted "
										+ subnetMask);
								resultCount = resultCount + 1;
							} else {
								errorMessage += "Failed to verify the previouse set subnet mask value is persisted "
										+ subnetMask + " \n";
							}
						} else {
							errorMessage += "Failed to validate the log message in "
									+ BroadBandTestConstants.COMMAND_NTP_LOG_FILE + " for " + subnetMask + " \n";
						}
					} else {
						errorMessage += "Failed to set and verify the invalid subnet mask " + subnetMask
								+ " using webpa \n";
					}
				}
				LOGGER.info("Subnet masks success Count:" + resultCount);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurres while validating invalid subnet mask values" + e.getMessage());
			errorMessage = e.getMessage();
		}
		status = (resultCount == listOfSubnetMask.size());
		objResult.setStatus(status);
		objResult.setErrorMessage(errorMessage);
		LOGGER.debug("ENDING METHOD: validateInValidSubnetMaskValues()");
		return objResult;
	}

	/**
	 * Utility method used to validate the valid subnet mask values
	 * 
	 * @param device          instance of {@link Dut}
	 * @param deviceConnected * @param device instance of {@link Dut}
	 * @return objResult- instance of {@link BroadBandResultObject}
	 * @refactor Said Hisham
	 */
	public static BroadBandResultObject validateValidSubnetMaskValues(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD : validateValidSubnetMaskValues()");
		BroadBandResultObject objResult = new BroadBandResultObject();
		String errorMessage = "";
		boolean status = false;
		int resultCount = BroadBandTestConstants.CONSTANT_0;
		boolean result = false;
		String currentTimeStamp = null;
		List<String> listOfSubnetMask = null;
		String subnetMaskFromProperty = null;
		try {
			subnetMaskFromProperty = AutomaticsTapApi.getAutomaticsPropsValueByResolvingPlatform(device,
					BroadBandTestConstants.PROP_KEY_VALID_SUBNETMAK_VALUES);
			listOfSubnetMask = Arrays.asList(subnetMaskFromProperty.split(BroadBandTestConstants.COMMA));
			LOGGER.info("List of Valid Subnet masks:" + listOfSubnetMask);
			LOGGER.info("List of Valid Subnet masks size:" + listOfSubnetMask.size());
			if (listOfSubnetMask.size() > BroadBandTestConstants.CONSTANT_0) {
				for (String subnetMaskAddress : listOfSubnetMask) {
					LOGGER.info("SUBNET MASK ADDRESS:" + subnetMaskAddress);
					currentTimeStamp = BroadBandCommonUtils.getCurrentTimeStampOnDevice(tapEnv, device);
					tapEnv.waitTill(BroadBandTestConstants.FIVE_SECONDS_IN_MILLIS);
					if (BroadBandWebPaUtils.setVerifyWebPAInPolledDuration(device, tapEnv,
							BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_LAN_SUBNET, BroadBandTestConstants.CONSTANT_0,
							subnetMaskAddress, BroadBandTestConstants.TWO_MINUTE_IN_MILLIS,
							BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS)) {
						LOGGER.info("Subnet Mask set successfully ");
						result = BroadBandCommonUtils.validateLogMessageInPamLog(device, tapEnv,
								BroadBandTestConstants.BOOLEAN_VALUE_TRUE, currentTimeStamp);
//								&& !BroadBandCommonUtils.validateLogMessageInPamLog(device, tapEnv,
//										BroadBandTestConstants.BOOLEAN_VALUE_FALSE, currentTimeStamp);
						LOGGER.info("Log message validation completed");
						if (result) {
							LOGGER.info("Successfully set the subnet mask value " + subnetMaskAddress);
							resultCount = resultCount + BroadBandTestConstants.CONSTANT_1;
						} else {
							errorMessage += "Failed to set the subnet mask value " + subnetMaskAddress;
							LOGGER.error(errorMessage);
						}
					} else {
						errorMessage += "Failed to set and verify the subnet mask " + subnetMaskAddress
								+ " using webpa \n";
						LOGGER.error(errorMessage);
					}
				}
				LOGGER.info("List of Subnet masks:" + listOfSubnetMask.size());
				LOGGER.info("Subnet masks success Count:" + resultCount);
			}
//		} catch (Exception e) {
//			LOGGER.error("Excepton Occured in validateValidSubnetMaskValues() " + e.getMessage());
//		}
		} catch (Throwable e) {
			LOGGER.error("Error: ", e);
		}
		status = (resultCount == listOfSubnetMask.size());
		objResult.setStatus(status);
		objResult.setErrorMessage(errorMessage);
		LOGGER.debug("ENDING METHOD: validateValidSubnetMaskValues()");
		return objResult;
	}

	/**
	 * Method to VERIFY DISCONNECTING THE WIFI CLIENT FROM THE GATEWAY DEVICE
	 * 
	 * @param device          {@link Dut}
	 * @param testId          Test case ID
	 * @param deviceConnected Device Connected
	 * @param wifiBand
	 * @param stepNumber      Step Number
	 * 
	 * @refactor Athira
	 */
	public static void verifyClientMacInGateway(Dut device, String testId, Dut deviceConnected,
			WiFiFrequencyBand wifiBand, int stepNumber) throws TestException {
		String testStepNumber = "S" + stepNumber;
		boolean status = false;
		LOGGER.info("#######################################################################################");
		LOGGER.info("STEP :  " + stepNumber + " : DESCRIPTION : VERIFY " + wifiBand
				+ " CONNECTED CLIENT'S MAC ADDRESS IN GATEWAY");
		LOGGER.info("STEP :  " + stepNumber + " : ACTION : EXECUTE COMMAND arp -n IN THE GATEWAY DEVICE");
		LOGGER.info("STEP :  " + stepNumber + " : EXPECTED: MUST RETURN HAVE CONNECTED CLIENT MAC ADDRESS");
		LOGGER.info("#######################################################################################");
		String errorMessage = "THE GATEWAY DOESN'T HAVE THE CLIENT'S MAC ADDRESS WHILE EXECUTING COMMAND : '/sbin/arp -n | grep -i <MAC ADDRESS>'";
		try {
			// getConnectedClientIpOrMacFromTheDevice API GETS THE MAC ADDRESS FROM THE
			// CLIENT AND VERIFIES
			// IF THE GATEWAY HAS THE SAME MAC ADDRESS USING COMMAND : "/sbin/arp -n | grep
			// -i <MAC ADDRESS>"
			String macAddressRetrievedFromClient = BroadBandConnectedClientUtils
					.getConnectedClientIpOrMacFromTheDevice(device, deviceConnected, tapEnv, false);
			status = CommonMethods.isNotNull(macAddressRetrievedFromClient);
		} catch (TestException exp) {
			errorMessage = exp.getMessage();
		}
		if (status) {
			LOGGER.info("STEP " + stepNumber
					+ " : ACTUAL : SUCCESSFULLY VERIFIED THE MAC ADDRESS OF THE CLIENT IN THE GATEWAY");
		} else {
			LOGGER.error("STEP " + stepNumber + " : ACTUAL : " + errorMessage);
		}
		LOGGER.info("#######################################################################################");
		tapEnv.updateExecutionStatus(device, testId, testStepNumber, status, errorMessage, false);
	}

	/**
	 * Test step method used change the Partner ID and Reactivate Wifi Settings.
	 * 
	 * @param device    instance of{@link Dut}
	 * @param partnerId String partner Id to be set.
	 * @refactor yamini.s
	 */
	public static boolean changePartnerIdAndReactivateTheDevice(Dut device, String partnerId) {
		LOGGER.debug("ENTERING METHOD changePartnerIdAndReactivateTheDevice");
		boolean status = false;
		try {
			if (BroadBandCommonUtils.changePartnerIdAndVerify(device, tapEnv, partnerId)) {
				reactivateDeviceUsingWebpaOrSnmp(tapEnv, device);
				status = true;
			}
		} catch (Exception e) {
			status = false;
			LOGGER.error("Exception Occured during Changing the Partner ID to " + partnerId + " :" + e.getMessage());
		}
		LOGGER.debug("EXITING METHOD changePartnerIdAndReactivateTheDevice");
		return status;
	}

	/**
	 * Method to verify the enable ax mode for the devices
	 * 
	 * @param device Dut instance
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @return status True-if ax mode is enabled for the device or else false
	 * 
	 */
	public static boolean enableAxMode(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD :EnableAxMode");
		boolean status = false;
		if (BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcliAndVerify(device, tapEnv,
				BroadBandWebPaConstants.WEBPA_PARAM_FOR_2_4_GHZ_AX_ENABLE, BroadBandTestConstants.FALSE)) {
			status = BroadBandWebPaUtils.setAndVerifyParameterValuesUsingWebPaorDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_FOR_2_4_GHZ_AX_ENABLE, WebPaDataTypes.BOOLEAN.getValue(),
					BroadBandTestConstants.TRUE)
					&& BroadBandWebPaUtils.setParameterValuesUsingWebPaOrDmcli(device, tapEnv,
							BroadBandWebPaConstants.WEBPA_PARAM_WIFI_2_4_APPLY_SETTING,
							BroadBandTestConstants.CONSTANT_3, BroadBandTestConstants.TRUE);
		}
		LOGGER.debug("ENDING METHOD :EnableAxMode");
		return status;
	}

	/**
	 * Utility method to validate the allowed number of clients based on residential
	 * or commercial device.
	 * 
	 * @param device {@link Dut}
	 * 
	 * @param output value allowed No of clients retrieved using snmp/webpa.
	 * 
	 * @return true if the output matches with the expected value
	 * 
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean validateAllowedNoOfClientsValueRetrievedFromSnmp(Dut device, String output) {
		boolean status = false;
		String errorMessage = "Null value retrieved for the Device model";
		String deviceModel = device.getModel();
		boolean isBusinessDevice = DeviceModeHandler.isBusinessClassDevice(device);

		try {
			status = CommonMethods.isNotNull(deviceModel) ? ((DeviceModeHandler.isBusinessClassDevice(device))
					? isBusinessDevice ? output.equals(BroadBandTestConstants.STRING_VALUE_FIVE)
							: output.equals(BroadBandTestConstants.STRING_VALUE_FIFTEEN)
					: output.equals(BroadBandTestConstants.STRING_VALUE_FIVE)) : false;
		} catch (Exception exception) {
			errorMessage = "Exception occured during execution : " + exception.getMessage();
			LOGGER.error(errorMessage);
		}
		return status;
	}
}

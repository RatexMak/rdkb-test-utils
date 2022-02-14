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
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.WiFiSsidConfigStatus;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.ConnectedNattedClientsUtils;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.wifi.connectedclients.BroadBandConnectedClientUtils;
import com.automatics.snmp.SnmpDataType;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.test.AutomaticsTestBase;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.CommonMethods;
import com.automatics.webpa.WebPaParameter;
import com.automatics.webpa.WebPaServerResponse;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;
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
			case BroadBandWebPaConstants.WEBPA_PARAM_5GHZ_SECURED_XFINITY_WIFI_SSID:
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
			case BroadBandWebPaConstants.WEBPA_PARAM_5GHZ_SECURED_XFINITY_WIFI_SSID_ENABLED:
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
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5GHZ_SECURED_XFINITY:
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
			errorMessage += "Failed to verify the status of XFinity WiFi for 5Ghz. Expected : " + status + "; Actual : "
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
						BroadBandTestConstants.PACE_NOT_APPLICABLE_IPV6, false);
			}
			LOGGER.info("***************************************************************************************");

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
						BroadBandTestConstants.PACE_NOT_APPLICABLE_IPV6, false);
			}
			LOGGER.info("***************************************************************************************");

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
		? BroadBandTestConstants.STRING_BUSINESS_CLASS_GATEWAYIP
		: BroadBandTestConstants.STRING_RESIDENTIAL_CLASS_GATEWAYIP;
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
}

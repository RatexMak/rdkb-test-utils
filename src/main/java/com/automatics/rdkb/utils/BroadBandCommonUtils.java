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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.constants.LinuxCommandConstants;
import com.automatics.device.Device;
import com.automatics.device.Dut;
import com.automatics.error.GeneralError;
import com.automatics.exceptions.FailedTransitionException;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.BroadBandDeviceStatus;
import com.automatics.rdkb.BroadBandParentalControlParameter;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.BroadbandRfcDataBaseValueObject;
import com.automatics.rdkb.constants.BroadBandCdlConstants;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandConnectedClientTestConstants;
import com.automatics.rdkb.constants.BroadBandPropertyKeyConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants.RdkBSsidParameters;
import com.automatics.rdkb.constants.BroadBandWebPaConstants.RdkBWifiParameters;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.rdkb.constants.RDKBTestConstants.WiFiFrequencyBand;
import com.automatics.rdkb.constants.WebPaParamConstants;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;
import com.automatics.rdkb.enums.ReportingServiceParameterEnum;
import com.automatics.rdkb.server.WhiteListServer;
import com.automatics.rdkb.utils.cdl.FirmwareDownloadUtils;
import com.automatics.rdkb.utils.dmcli.DmcliUtils;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpMib;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpUtils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.wifi.BroadBandWiFiUtils;
import com.automatics.rdkb.utils.wifi.connectedclients.BroadBandConnectedClientInfo;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiElements;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiTestConstant;
import com.automatics.rdkb.webui.page.BroadBandCommonPage;
import com.automatics.rdkb.webui.page.BroadBandWifiPage.SSIDFrequency;
import com.automatics.rdkb.webui.page.LanSideBasePage;
import com.automatics.resource.IServer;
import com.automatics.snmp.SnmpDataType;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.CommonMethods;
import com.automatics.webpa.WebPaParameter;
import com.automatics.webpa.WebPaServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class which handles the RDK B common functionality and verification.
 * 
 * @author Selvaraj Mariyappan
 * @refactor Govardhan
 * 
 */
public class BroadBandCommonUtils {

	private static String ifConfigErouter0Response = null;

	/**
	 * Logger instance for {@link BroadBandCommonUtils}
	 */
	protected static final Logger LOGGER = LoggerFactory.getLogger(BroadBandCommonUtils.class);

	/**
	 * Method to build command using string buffer.
	 * 
	 * @param values , N number of arguments
	 * 
	 * @author ArunKumar Jayachandran
	 * @refactor Govardhan
	 */
	public static String concatStringUsingStringBuffer(String... values) {
		LOGGER.debug("Entering into concatStringUsingStringBuffer");
		StringBuffer command = new StringBuffer();
		for (String cmd : values) {
			command.append(cmd);
		}
		LOGGER.debug("Exiting into concatStringUsingStringBuffer");
		return command.toString();
	}

	/**
	 * Method to wait for certain amount of time and return true
	 * 
	 * @param tapEnv       instance of {@link AutomaticsTapApi}
	 * 
	 * @param waitDuration long integer specifying the time in milliseconds to wait
	 *                     for
	 * 
	 * @author Ashwin sankara
	 * @refactor Govardhan
	 * 
	 */
	public static boolean hasWaitForDuration(AutomaticsTapApi tapEnv, long waitDuration) {
		LOGGER.debug("ENTERING METHOD waitAndReturnTrue");
		LOGGER.info("Waiting for " + waitDuration / 1000 + " seconds");
		tapEnv.waitTill(waitDuration);
		LOGGER.debug("EXITING METHOD waitAndReturnTrue");
		return true;
	}

	/**
	 * Utility Method to set the value of WebPA Parameter for the device.
	 * 
	 * @param tapEnv         {@link AutomaticsTapApi}
	 * @param device         {@link Dut}
	 * @param webPaParameter Object representing WebPaParameter
	 * 
	 * @return Boolean representing the result of setting the Parameter value.
	 * 
	 * @author Govardhan
	 */
	public static boolean setWebPaParam(AutomaticsTapApi tapEnv, Dut device, WebPaParameter webPaParameter) {
		LOGGER.debug("ENTERING METHOD setWebPaParam");
		List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
		webPaParameters.add(webPaParameter);
		WebPaServerResponse response = tapEnv.setWebPaParameterValues(device, webPaParameters);
		LOGGER.info("RESPONSE CODE: " + response.getStatusCode());
		LOGGER.info("RESPONSE MESSAGE: " + response.getMessage());
		boolean result = response.getStatusCode() == RDKBTestConstants.CONSTANT_200
				|| response.getMessage().contains(BroadBandTestConstants.SUCCESS_TXT);
		LOGGER.info("WEBPA PARAM - " + webPaParameter.getName() + " SET WITH VALUE - " + webPaParameter.getValue()
				+ " IS SUCCESSFUL: " + result);
		LOGGER.debug("ENDING METHOD setWebPaParam");
		return result;
	}

	/**
	 * Utility to method to search for the given text in the given log file based on
	 * Poll Interval & Poll Duration; returns String representing the search
	 * response.
	 * 
	 * @param tapEnv       {@link AutomaticsTapApi}
	 * @param device       {@link Dut}
	 * @param searchText   String representing the Search Text. It needs to be
	 *                     passed with the required escape character.
	 * @param logFile      String representing the log file.
	 * @param pollDuration Long representing the duration for which polling needs to
	 *                     be performed.
	 * @param pollInterval Long representing the polling interval.
	 * 
	 * @return String representing the search response.
	 * @author Govardhan
	 */
	public static String searchLogFiles(AutomaticsTapApi tapEnv, Dut device, String searchText, String logFile,
			long pollDuration, long pollInterval) {
		LOGGER.debug("STARTING METHOD searchLogFiles");
		StringBuffer sbCommand = new StringBuffer(BroadBandTestConstants.GREP_COMMAND);
		// In case the search text contains space and not wrapped with double quotes.
		if (searchText.contains(BroadBandTestConstants.SINGLE_SPACE_CHARACTER)
				&& !searchText.contains(BroadBandTestConstants.DOUBLE_QUOTE)) {
			sbCommand.append(BroadBandTestConstants.DOUBLE_QUOTE);
			sbCommand.append(searchText);
			sbCommand.append(BroadBandTestConstants.DOUBLE_QUOTE);
		} else {
			sbCommand.append(searchText);
		}
		sbCommand.append(BroadBandTestConstants.SINGLE_SPACE_CHARACTER);
		sbCommand.append(logFile);
		LOGGER.info("COMMAND TO BE EXECUTED: " + sbCommand.toString());
		long startTime = System.currentTimeMillis();
		String searchResponse = null;
		do {
			tapEnv.waitTill(pollInterval);
			searchResponse = tapEnv.executeCommandUsingSsh(device, sbCommand.toString());
			searchResponse = CommonMethods.isNotNull(searchResponse)
					&& !searchResponse.contains(BroadBandTestConstants.NO_SUCH_FILE_OR_DIRECTORY)
							? searchResponse.trim()
							: null;
		} while ((System.currentTimeMillis() - startTime) < pollDuration && CommonMethods.isNull(searchResponse));

		LOGGER.info(
				"SEARCH RESPONSE FOR - " + searchText + " IN THE LOG FILE - " + logFile + " IS : " + searchResponse);
		LOGGER.debug("ENDING METHOD searchLogFiles");
		return searchResponse;
	}

	/**
	 * Helper method to search the log message with polling time
	 * 
	 * @param device          Dut Box
	 * @param tapEnv          AutomaticsTapApi instance
	 * @param logMessage      log message to search
	 * @param logFile         log file to search
	 * @param maxDuration     max duration to poll
	 * @param pollingInterval interval time for each time within max duration
	 * @return response response from the log file for search message
	 * @author ArunKumar Jayachandran
	 * @refactor Govardhan
	 */
	public static String searchLogFilesInAtomOrArmConsoleByPolling(Dut device, AutomaticsTapApi tapEnv,
			String logMessage, String logFile, long maxDuration, long pollingInterval) {
		LOGGER.debug("STARTING METHOD: searchLogFilesInAtomOrArmConsoleByPolling");
		String response = null;
		response = BroadBandCommonUtils.searchLogFiles(tapEnv, device, logMessage, logFile, maxDuration,
				pollingInterval);
		LOGGER.info("Search response: " + response);
		LOGGER.debug("ENDING METHOD: searchLogFilesInAtomOrArmConsoleByPolling");
		return response;
	}

	/**
	 * Utility method to get WebPa value for a request and verify with given value
	 * 
	 * @param device            , Dut box instance
	 * @param tapEnv            , AutomaticsTapApi instance
	 * @param parameter         , WebPA parameter
	 * @param valueToBeVerified , value to be verified from webpa get request
	 * @return true, if WebPA value from get request is same as valueToBeVerified
	 * @author Govardhan
	 */
	public static boolean getWebPaValueAndVerify(Dut device, AutomaticsTapApi tapEnv, String parameter,
			String valueToBeVerified) {
		boolean status = false;
		LOGGER.debug("Entering method getWebPaValueAndVerify");
		if (CommonMethods.isNotNull(valueToBeVerified)) {
			String response = null;
			response = tapEnv.executeWebPaCommand(device, parameter);
			if (CommonMethods.isNotNull(response)) {
				LOGGER.info("Value expected from WebPA get request is " + valueToBeVerified);
				LOGGER.info("Response from WebPA get request is " + response);
				status = valueToBeVerified.equalsIgnoreCase(response);
				if (!status) {
					LOGGER.error("Expected value for " + parameter + "is:" + valueToBeVerified + " Actual:" + response);
					throw new TestException(
							"Expected value for " + parameter + "is:" + valueToBeVerified + " Actual:" + response);

				}
			} else {
				LOGGER.error("No response from WebPA request for the parameter " + parameter);
				throw new TestException("No response from WebPA request for the parameter " + parameter);
			}
		} else {
			LOGGER.error(" Value to be verified cant be empty or null.");
			throw new TestException("No response from WebPA request for the parameter " + parameter);
		}
		LOGGER.info("Is the value from WebPA get request is the same as expected - " + status);
		LOGGER.debug("Exit from method getWebPaValueAndVerify");
		return status;
	}

	/**
	 * Utility to method to search for the given text in the given log file; returns
	 * String representing the search response.
	 * 
	 * @param tapEnv     {@link AutomaticsTapApi}
	 * @param device     {@link Dut}
	 * @param searchText String representing the Search Text. It needs to be passed
	 *                   with the required escape character.
	 * @param logFile    String representing the log file.
	 * 
	 * @return String representing the search response.
	 * @author Athira
	 */
	public static String searchLogFiles(AutomaticsTapApi tapEnv, Dut device, String searchText, String logFile) {
		LOGGER.debug("STARTING METHOD searchLogFiles");

		String response = null;
		StringBuffer sbCommand = new StringBuffer(BroadBandTestConstants.GREP_COMMAND);
		// In case the search text contains space and not wrapped with double
		// quotes.
		if (searchText.contains(BroadBandTestConstants.SINGLE_SPACE_CHARACTER)
				&& !searchText.contains(BroadBandTestConstants.DOUBLE_QUOTE)) {
			sbCommand.append(BroadBandTestConstants.DOUBLE_QUOTE);
			sbCommand.append(searchText);
			sbCommand.append(BroadBandTestConstants.DOUBLE_QUOTE);
		} else {
			sbCommand.append(searchText);
		}
		sbCommand.append(BroadBandTestConstants.SINGLE_SPACE_CHARACTER);
		sbCommand.append(logFile);
		LOGGER.info("COMMAND TO BE EXECUTED: " + sbCommand.toString());

		response = tapEnv.executeCommandUsingSsh(device, sbCommand.toString());
		response = CommonMethods.isNotNull(response)
				&& !response.contains(BroadBandTestConstants.NO_SUCH_FILE_OR_DIRECTORY) ? response.trim() : null;
		LOGGER.info("SEARCH RESPONSE FOR - " + searchText + " IN THE LOG FILE - " + logFile + " IS : " + response);
		LOGGER.debug("ENDING METHOD searchLogFiles");
		return response;
	}

	/**
	 * Utility method to obtain the string for the specific pattern based on the
	 * match position; this method to be invoked when the expected number of matches
	 * is more than one.
	 * 
	 * @param response       String representing the text on which pattern matcher
	 *                       to be performed.
	 * @param patternToMatch String representing the pattern to be searched.
	 * @return String representing the result of the pattern matcher based on match
	 *         position.
	 * @author Govardhan
	 */
	public static String patternFinderMatchPositionBased(String response, String patternToMatch, int matchPosition) {

		LOGGER.debug("STARTING METHOD: patternFinderMatchPositionBased");
		String matchedString = null;
		if (CommonMethods.isNotNull(response) && CommonMethods.isNotNull(patternToMatch)) {
			Pattern pattern = Pattern.compile(patternToMatch);
			Matcher matcher = pattern.matcher(response);
			if (matcher.find()) {
				matchedString = matcher.group(matchPosition);
			}
		}
		LOGGER.info("PATTERN MATCHED STRING:" + matchedString);

		LOGGER.debug("ENDING METHOD: patternFinderMatchPositionBased");

		return matchedString;
	}

	/**
	 * Utility Method to retrieve the current Date and time from the device from
	 * ATOM or Arm Console
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * 
	 * @return Returns the current current time stamp from device.
	 * @author Govardhan
	 */
	public static String getCurrentTimeStampOnDeviceFromAtomorArmConsole(AutomaticsTapApi tapEnv, Dut device) {
		String currentTimeStamp = null;
		LOGGER.debug("STARTING METHOD : getCurrentTimeStampOnDeviceFromAtomorArmConsole()");
		boolean isAtomSyncAvailable = false;
		try {
			isAtomSyncAvailable = CommonMethods.isAtomSyncAvailable(device, tapEnv);
			if (isAtomSyncAvailable) {
				currentTimeStamp = tapEnv.executeCommandOnAtom(device, BroadBandCommandConstants.CMD_DATE_LOG_FORMAT);
				tapEnv.waitTill(BroadBandTestConstants.TWO_SECOND_IN_MILLIS);
			} else {
				currentTimeStamp = tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_DATE_LOG_FORMAT);
				tapEnv.waitTill(BroadBandTestConstants.TWO_SECOND_IN_MILLIS);
			}
		} catch (Exception exception) {
			LOGGER.error("Exception occured while searching log ", exception);

		}
		LOGGER.debug("ENDING METHOD : getCurrentTimeStampOnDeviceFromAtomorArmConsole()");

		return currentTimeStamp;
	}

	/**
	 * Utility method to perform Factory Reset on the device using WebPA & then wait
	 * for the device to comeup.
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * 
	 * @return Boolean representing the result of the Factory Reset Operation.
	 * @author Govardhan
	 */
	public static boolean performFactoryResetWebPa(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.info("ENTERING METHOD performFactoryResetWebPa");
		boolean result = BroadBandWiFiUtils.setWebPaParams(device, WebPaParamConstants.WEBPA_PARAM_FACTORY_RESET,
				BroadBandTestConstants.STRING_FOR_FACTORY_RESET_OF_THE_DEVICE, BroadBandTestConstants.CONSTANT_0);
		LOGGER.info("Result is : " + result);
		long startTime = 0L;
		if (result) {
			// Check if the device goes down.
			startTime = System.currentTimeMillis();
			do {
				LOGGER.info("GOING TO WAIT FOR 10 SEC BEFORE EXECUTING TEST COMMAND.");
				tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
				result = !CommonMethods.isSTBAccessible(device);
				// result = !CommonUtils.executeTestCommand(tapEnv, device);
				LOGGER.info("Test Command Result is : " + result);
			} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.THREE_MINUTES && !result);
		}
		LOGGER.info("DEVICE REBOOTS AFTER TRIGGERING FACTORY RESET: " + result);
		if (result) {
			startTime = System.currentTimeMillis();
			do {
				LOGGER.info("GOING TO WAIT FOR 1 MINUTE.");
				tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
				result = CommonMethods.isSTBAccessible(device);
				LOGGER.info("STB Accessible Result is : " + result);
			} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIFTEEN_MINUTES_IN_MILLIS
					&& !result);
			LOGGER.info("DEVICE COMES UP AFTER FACTORY RESET: " + result);
		}
		if (result) {
			result = BroadBandWebPaUtils.verifyWebPaProcessIsUp(tapEnv, device, true);
		}
		LOGGER.info("BROAD BAND DEVICE FACTORY RESET (WEBPA) PERFORMED SUCCESSFULLY: " + result);
		LOGGER.info("ENDING METHOD performFactoryResetWebPa");
		return result;
	}

	/**
	 * Method to execute customized command
	 * 
	 * @param device
	 * @param command
	 * @param maxDuration
	 * @param pollingIntravel
	 * @return response
	 * 
	 * @author ArunKumar Jayachandran
	 * @Refactor Athira
	 */
	public static String executeCommandByPolling(Dut device, AutomaticsTapApi tapEnv, String command, long maxDuration,
			long pollingIntravel) {
		LOGGER.debug("STARTING executeCommandByPolling()");
		String response = null;
		long startTime = System.currentTimeMillis();
		do {
			tapEnv.waitTill(pollingIntravel);
			response = tapEnv.executeCommandUsingSsh(device, command);
			response = CommonMethods.isNotNull(response)
					&& !response.contains(BroadBandTestConstants.NO_SUCH_FILE_OR_DIRECTORY) ? response.trim() : null;
		} while ((System.currentTimeMillis() - startTime) < maxDuration && CommonMethods.isNull(response));
		LOGGER.debug("ENDING executeCommandByPolling()");
		return response;
	}

	/**
	 * Helper method to get status of rabid and advsec process
	 * 
	 * @param Dut      {@link device}
	 * @param tapEnv   {@link AutomaticsTapApi}
	 * @param pidRapid Rapid process pid
	 * @return true if response is obtained for both rabid and advsec process
	 * @Refactor Athira
	 */
	public static boolean getRabidAndAdvSecStatus(Dut device, AutomaticsTapApi tapEnv, String pidRabid) {
		LOGGER.debug("Starting of Method: getRabidAndAdvSecStatus");
		String response = null;
		boolean status = false;
		try {
			response = tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_PS_GREP_RABID_PROCESS);
			status = CommonMethods.isNotNull(response)
					&& CommonUtils.isGivenStringAvailableInCommandOutput(response, pidRabid);
			if (status) {
				response = tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_PS_GREP_ADVSEC);
				status = CommonMethods.isNotNull(response);
			}

		} catch (Exception e) {
			LOGGER.error("Exception occured while retrieving status of Advsec/Rabid " + e.getMessage());
		}
		LOGGER.debug("Ending of Method: getRabidAndAdvSecStatus");
		return status;
	}

	/**
	 * Utility method to perform Factory Reset on the device using WebPA & then wait
	 * for the device to comeup.
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * 
	 * @return Boolean representing the result of the Factory Reset Operation.
	 * @author Govardhan
	 */
	public static boolean performFactoryResetAndWaitForWebPaProcessToUp(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("ENTERING METHOD performFactoryResetAndWaitForWebPaProcessToUp");
		boolean result = BroadBandWebPaUtils.setParameterValuesUsingWebPaOrDmcli(device, tapEnv,
				WebPaParamConstants.WEBPA_PARAM_FACTORY_RESET, BroadBandTestConstants.CONSTANT_0,
				BroadBandTestConstants.STRING_FOR_FACTORY_RESET_OF_THE_DEVICE);
		long startTime = 0L;
		if (result) {
			// Check if the device goes down.
			startTime = System.currentTimeMillis();
			do {
				result = !CommonMethods.isSTBAccessible(device);
			} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.TEN_MINUTE_IN_MILLIS && !result
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
		}
		LOGGER.info("DEVICE REBOOTS AFTER TRIGGERING FACTORY RESET: " + result);
		if (result) {
			startTime = System.currentTimeMillis();
			do {
				result = CommonMethods.isSTBAccessible(device);
			} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.TWENTY_MINUTES_IN_MILLIS
					&& !result
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
			LOGGER.info("DEVICE COMES UP AFTER FACTORY RESET: " + result);
		}
		if (result) {
			result = BroadBandWebPaUtils.verifyWebPaProcessIsUp(tapEnv, device, true);
		}
		LOGGER.info("BROAD BAND DEVICE FACTORY RESET (WEBPA) PERFORMED SUCCESSFULLY: " + result);
		LOGGER.debug("ENDING METHOD performFactoryResetAndWaitForWebPaProcessToUp");
		return result;
	}

	/**
	 * Method to verify if the commandOutput contains the given list of strings
	 * 
	 * @param commandOutput       Output of the command
	 * @param stringVerifications List of Strings to be verified
	 * @return status true if the log is present else false
	 * @author Govardhan
	 */
	public static BroadBandResultObject isGivenStringListAvailableInCommandOutput(String commandOutput,
			List<String> stringVerifications) {

		LOGGER.debug("STARTING METHOD: CommonUtils.isGivenStringListAvailableInCommandOutput()");
		String errorMessage = BroadBandTestConstants.EMPTY_STRING;
		BroadBandResultObject broadBandResultObject = new BroadBandResultObject();
		boolean isPresent = true;
		// Validation status
		boolean status = false;

		if (CommonMethods.isNotNull(commandOutput)) {
			// Loop through the list and verify if all the entries are present in the
			// command response
			for (String entry : stringVerifications) {
				status = false;
				if (commandOutput.contains(entry)) {
					status = true;
					LOGGER.info("Presence of parameter " + entry + " in the command output : " + status);
				} else {
					isPresent = false;
					errorMessage += entry + ", ";
				}
			}

		} else {

			LOGGER.info("The response of command is null");
		}
		LOGGER.debug("ENDING METHOD: CommonUtils.isGivenStringAvailableInCommandOutput()");

		LOGGER.info(errorMessage);
		broadBandResultObject.setErrorMessage(errorMessage);
		broadBandResultObject.setStatus(isPresent);
		return broadBandResultObject;
	}

	/**
	 * Method to retrieve current running image name from device
	 * 
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @param device instance of {@link Dut}
	 * @return current running image name
	 */
	public static String getCurrentlyRunningImageVersionUsingWebPaCommand(AutomaticsTapApi tapEnv, Dut device) {

		LOGGER.debug("STARTING METHOD: getCurrentlyRunningImageVersionUsingWebPaCommand()");
		String imageName = tapEnv.executeWebPaCommand(device, BroadBandTestConstants.TR69_RDKB_CURRENT_IMAGE_NAME);
		LOGGER.debug("ENDING METHOD: getCurrentlyRunningImageVersionUsingWebPaCommand()");
		return imageName;
	}

	/**
	 * Method to search log file in Arm or Atom and provide response
	 * 
	 * @param tapEnv     {@link AutomaticsTapApi}
	 * @param device     {@link Dut}
	 * 
	 * @param searchText string containing text to be searched in log file
	 * @param logFile    string containing path of the file to be searched
	 * 
	 * @return response string containing result of log search
	 * 
	 * @author Ashwin sankara
	 * @Refactor Athira
	 * 
	 */
	public static String searchArmOrAtomLogFile(AutomaticsTapApi tapEnv, Dut device, String searchText,
			String logFile) {
		LOGGER.debug("Entering method: searchArmOrAtomLogFile");
		String command = concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.SYMBOL_QUOTES, searchText,
				BroadBandTestConstants.SYMBOL_QUOTES, BroadBandTestConstants.SINGLE_SPACE_CHARACTER, logFile);
		LOGGER.info("Command to be executed: " + command);
		String response = CommonMethods.executeCommandInAtomConsoleIfAtomIsPresentElseInArm(device, tapEnv, command,
				CommonMethods.isAtomSyncAvailable(device, tapEnv));
		response = CommonMethods.isNotNull(response)
				&& !CommonMethods.patternMatcher(response, BroadBandTestConstants.NO_SUCH_FILE_OR_DIRECTORY)
						? response.trim()
						: null;
		LOGGER.info("Response after searching " + response);
		LOGGER.debug("Exiting method: searchArmOrAtomLogFile");
		return response;
	}

	/**
	 * Method to obtain the list of matched strings for the specific pattern
	 * 
	 * @param response            the target string on which the pattern has to be
	 *                            searched
	 * @param patternToMatch      pattern that has to be matched
	 * @param patternMatcherGroup integer representing the pattern Matcher Group
	 * @return list of strings found
	 * @author susheela C
	 * @Refactor Athira
	 */
	public static List<String> patternFinderForMultipleMatches(String response, String patternToMatch,
			int patternMatcherGroup) {

		LOGGER.debug("STARTING METHOD: BroadBandCommonUtils.patternFinderForMultipleMatches()");
		// matched string
		String matchedString = "";
		// instance of pattern for match
		Pattern pattern = null;
		// Instance of matcher
		Matcher matcher = null;
		List<String> matchedStringList = new ArrayList<String>();
		try {

			if (CommonMethods.isNotNull(response) && CommonMethods.isNotNull(patternToMatch)) {
				pattern = Pattern.compile(patternToMatch);
				matcher = pattern.matcher(response);
				while (matcher.find()) {
					matchedString = matcher.group(patternMatcherGroup);
					LOGGER.info("Matching string : " + matchedString);
					matchedStringList.add(matchedString.trim());
				}
			}
		} catch (Exception exception) {
			LOGGER.error("Exception occured in patternFinder()", exception);
		}
		LOGGER.info("No of matched strings: " + matchedStringList.size());
		LOGGER.debug("ENDING METHOD: BroadBandCommonUtils.patternFinderForMultipleMatches()");
		return matchedStringList;
	}

	/**
	 * helper method to obtain the list of matched strings for the specific pattern
	 * 
	 * @param response
	 * @param patternToMatch
	 * @return iist of strings found
	 */
	public static List<String> patternFinderForMultipleMatches(String response, String patternToMatch) {

		LOGGER.debug("STARTING METHOD: patternFinder()");

		// matched string
		String matchedString = "";
		// instance of pattern for match
		Pattern pattern = null;
		// Instance of matcher
		Matcher matcher = null;
		List<String> matchedStringList = new ArrayList<String>();
		try {

			if (CommonMethods.isNotNull(response) && CommonMethods.isNotNull(patternToMatch)) {
				pattern = Pattern.compile(patternToMatch);
				matcher = pattern.matcher(response);
				while (matcher.find()) {
					matchedString = matcher.group(1);
					LOGGER.info("Matching string : " + matchedString);
					matchedStringList.add(matchedString.trim());
				}
			}
		} catch (Exception exception) {
			LOGGER.error("Exception occured in patternFinder()", exception);
		}

		LOGGER.info("No of matched strings -" + matchedStringList.size());

		LOGGER.debug("ENDING METHOD: patternFinder()");

		return matchedStringList;
	}

	/**
	 * 
	 * This method is to verify ping command execution on the Jump Server.
	 * 
	 * @param tapEnv Instance of {@link AutomaticsTapApi}
	 * @param device Instance of {@link device}
	 * @param url    URL for which the ping connection needs to be checked
	 * 
	 * @return returns status of operation
	 */
	public static BroadBandResultObject verifyPingConnectionFromJumpServer(Dut device, AutomaticsTapApi tapEnv,
			String url) {
		LOGGER.info("STARTING METHOD : verifyPingConnectionFromJumpServer()");

		String errorMessage = null;
		String pingResponse = null;
		boolean isReachable = false;
		BroadBandResultObject result = new BroadBandResultObject();

		String pingCommand = CommonMethods.isIpv6Address(url) ? BroadBandCommandConstants.CMD_PING_LINUX_IPV6
				: BroadBandCommandConstants.CMD_PING_LINUX;
		pingCommand = BroadBandCommonUtils.concatStringUsingStringBuffer(pingCommand, url);

		pingResponse = tapEnv.executeCommandUsingSshConnection(WhiteListServer.getInstance(tapEnv, "localhost"),
				pingCommand);
		LOGGER.info("PING RESPONSE :"+pingResponse);

		isReachable = CommonMethods.isNotNull(pingResponse)
				&& CommonUtils.patternSearchFromTargetString(pingResponse, BroadBandTestConstants.PING_SUCCESS_MESSAGE)
				&& !CommonUtils.patternSearchFromTargetString(pingResponse,
						BroadBandTestConstants.PING_FAILURE_MESSAGE);
		if (!isReachable) {
			errorMessage = CommonMethods.isNotNull(pingResponse) & CommonUtils
					.patternSearchFromTargetString(pingResponse, BroadBandTestConstants.PING_FAILURE_MESSAGE)
							? "Ping from WAN got failed to the IP Address/URL."
							: "Unable to ping the IP Address/URL from WAN side.";
			errorMessage = errorMessage + " ACTUAL RESPONSE: " + pingResponse;
		}
		result.setErrorMessage(errorMessage);
		result.setStatus(isReachable);
		LOGGER.info("ENDING METHOD : verifyPingConnectionFromJumpServer()");
		return result;
	}

	/**
	 * Utility method to verify a file is present on the device. It uses the
	 * command: find / -iname <fileName>
	 * 
	 * @param tapEnv   {@link AutomaticsTapApi}
	 * @param device   {@link Dut}
	 * @param fileName String representing the Library Name
	 * 
	 * @return Boolean representing the result of the validation.
	 */
	public static boolean isFilePresentOnDevice(AutomaticsTapApi tapEnv, Dut device, String fileName) {
		LOGGER.debug("STARTING METHOD: isFilePresentOnDevice");
		boolean result = false;
		String command = concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_FIND_INAME, fileName);
		LOGGER.info("Command is : " + command);
		String response = tapEnv.executeCommandUsingSsh(device, command);
		LOGGER.info("Response is : " + response);
		if (CommonMethods.isNotNull(response)) {
			result = CommonMethods.patternMatcher(response, BroadBandTestConstants.PATTERN_MATCHER_FIND_NO_SUCH_FILE)
					&& !CommonMethods.patternMatcher(response, fileName + "[\r|\n]");
		} else {
			// Even when the response is blank which we get inconsistently, we are updating
			// the result as Success.
			LOGGER.info("NULL/ BLANK RESPONSE. MARKING AS FILE NOT PRESENT.");
			result = true;
		}
		LOGGER.info("FILE " + fileName + " IS PRESENT ON THE DEVICE: " + !result);
		LOGGER.debug("ENDING METHOD: isFilePresentOnDevice");
		return !result;
	}

	/**
	 * Method to check whether the STB is accessible or inaccessible as per the
	 * boolean variable "expectedResult" passed for a max polling time and polling
	 * interval as passed. This is cross verified by sending an echo test_connection
	 * command and see if we get the response or not.
	 * 
	 * @param tapEnv          {@link AutomaticsTapApi}
	 * @param device          {@link Dut}
	 * @param pollingInterval The Interval In which the validation has to be done
	 * @param maxPollingTime  The Max Duration In which the validation has to be
	 *                        done
	 * @param expectedResult  true if the device has to be in "Accessible" state or
	 *                        false if the device has to be in "Inaccessible"
	 * @return result
	 * 
	 * @author BALAJI V
	 * @Refactor Athira
	 */
	public static boolean isRdkbDeviceAccessible(AutomaticsTapApi tapEnv, Dut device, long pollingInterval,
			long maxPollingTime, boolean expectedResult) {
		LOGGER.info("ENTERING METHOD: isRdkbDeviceAccessible");
		// Boolean Variable to store the result
		boolean result = false;
		// Start time
		long startTime = System.currentTimeMillis();
		do {
			try {
				String response = tapEnv.executeCommandUsingSsh(device,
						BroadBandTestConstants.ECHO_WITH_SPACE + BroadBandTestConstants.CONNECTION_TEST_MESSAGE);
				result = expectedResult
						? (CommonMethods.isNotNull(response) && CommonUtils.patternSearchFromTargetString(response,
								BroadBandTestConstants.CONNECTION_TEST_MESSAGE))
						: (CommonMethods.isNull(response)
								|| response.indexOf(BroadBandTestConstants.CONNECTION_TEST_MESSAGE) == -1);
			} catch (FailedTransitionException e) {
				LOGGER.error("Exception occured :" + e.getMessage());
				if (expectedResult == false
						&& e.getError().getCode() == (GeneralError.SSH_CONNECTION_FAILURE.getCode())) {
					result = true;
					return result;
				} else
					return result;
			}
		} while (((System.currentTimeMillis() - startTime) < maxPollingTime) && !result
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, pollingInterval));
		LOGGER.info("ENDING METHOD: isRdkbDeviceAccessible");
		return result;
	}

	/**
	 * This method will wait the boot up time of the device is as given in the
	 * parameter
	 * 
	 * @param device              {@link Dut}
	 * @param tapEnv              {@link AutomaticsTapApi}
	 * @param waitTimeAfterReboot
	 */
	public static void waitTillGivenBootupTime(Dut device, AutomaticsTapApi tapEnv, long waitTimeAfterReboot) {
		long boxUpTimeInSeconds;
		long startTime = System.currentTimeMillis();
		// Variable to hold current time
		long currentTime = -1L;
		boolean isSTBAccessible = false;
		try {
			do {
				isSTBAccessible = CommonMethods.isSTBAccessible(device);
				if (!isSTBAccessible) {
					LOGGER.error("Device is not accessible while waiting till the given boot time");
				}
				boxUpTimeInSeconds = CommonUtils.getBoxUptimeInSeconds(device, tapEnv);
				LOGGER.info("Bootup time in minutes: " + boxUpTimeInSeconds / 60);
				currentTime = System.currentTimeMillis();
				if (currentTime > (startTime + waitTimeAfterReboot)) {
					break;
				}
			} while (boxUpTimeInSeconds * 1000 < waitTimeAfterReboot && isSTBAccessible && BroadBandCommonUtils
					.hasWaitForDuration(tapEnv, BroadBandTestConstants.FIFTEEN_SECONDS_IN_MILLIS));
		} catch (Exception e) {
			LOGGER.error("Exception occured while checking the boot time");
		}
	}

	/**
	 * Utility Method to validate the log message searched & found is the recent one
	 * i.e. after performing the appropriate action. This method utilizes the date
	 * captured on the device before performing the action and the log message
	 * searched after performing the action. Both the timestamps are compared &
	 * validated.
	 * 
	 * @param dateResponse   String representing the DateTime captured before
	 *                       performing the action.
	 * @param searchResponse String representing the search response after
	 *                       performing the action.
	 * @return Boolean representing the result of validation of log message using
	 *         TimeStamp.
	 */
	public static boolean verifyLogUsingTimeStamp(String dateResponse, String searchResponse) {
		LOGGER.debug("ENTERING METHOD verifyLogUsingTimeStamp");
		boolean result = false;
		SimpleDateFormat logMessageDateTimeFormat = new SimpleDateFormat(
				BroadBandTestConstants.TIMESTAMP_FORMAT_LOG_MESSAGE);
		LOGGER.info("dateResponse: " + dateResponse);
		LOGGER.info("searchResponse: " + searchResponse);
		// Parse the TimeStamp captured on the device with the required format.
		Date dtCapturedDateTime = null;
		try {
			dtCapturedDateTime = CommonMethods.isNotNull(dateResponse)
					? logMessageDateTimeFormat.parse(dateResponse.trim())
					: null;
			LOGGER.info("CAPTURED TIMESTAMP (DATE INSTANCE): " + dtCapturedDateTime);
		} catch (ParseException parseException) {
			LOGGER.error(dateResponse + " COULD NOT BE PARSED: " + parseException.getMessage());
		}

		// Parse the TimeStamp from the log message after extracting it.
		String strLogMessageDateTime = CommonMethods.isNotNull(searchResponse)
				? CommonMethods.patternFinder(searchResponse,
						BroadBandTestConstants.PATTERN_MATCHER_TIMESTAMP_LOG_MESSAGE)
				: null;
		LOGGER.info("LOG FILE TIMESTAMP AFTER EXTRACTING(strLogMessageDateTime): " + strLogMessageDateTime);
		Date logMessageDateTime = null;
		try {
			logMessageDateTime = CommonMethods.isNotNull(strLogMessageDateTime)
					? logMessageDateTimeFormat.parse(strLogMessageDateTime)
					: null;
			LOGGER.info("LOG MESSAGE TIMESTAMP (DATE INSTANCE):: " + logMessageDateTime);
		} catch (ParseException parseException) {
			LOGGER.error(strLogMessageDateTime + " COULD NOT BE PARSED: " + parseException.getMessage());
		}

		// Convert the captured DateTime to MilliSeconds & Compare them.
		// If the TimeStamp Captured before performing action has a value lower
		// than the TimeStamp in log message, then
		// it returns TRUE.
		result = null != dtCapturedDateTime && null != logMessageDateTime
				&& dtCapturedDateTime.getTime() < logMessageDateTime.getTime();

		LOGGER.info("LOG VALIDATION (USING TIMESTAMP) RESULT: " + result);
		LOGGER.debug("ENDING METHOD verifyLogUsingTimeStamp");
		return result;
	}

	/**
	 * Helper method to search text in log file with polling method
	 * 
	 * @param tapEnv       Instance of {@link AutomaticsTapApi}
	 * @param device       {@link Dut}
	 * @param logFile      log file location
	 * @param pollDuration maximum poll duration
	 * @param pollInterval polling interval
	 * @return searchResponse response for search text
	 * 
	 */
	public static String searchLogByPolling(AutomaticsTapApi tapEnv, Dut device, String command, long pollDuration,
			long pollInterval) {
		String response = null;
		boolean status = false;
		long startTime = System.currentTimeMillis();
		do {
			response = tapEnv.executeCommandUsingSsh(device, command);
			status = CommonMethods.isNotNull(response);
		} while ((System.currentTimeMillis() - startTime) < pollDuration && !status
				&& hasWaitForDuration(tapEnv, pollInterval));
		return response;
	}

	/**
	 * Method to verify Ping test for set interval of time .
	 * 
	 * @param device        {@link Dut}
	 * @param tapEnv        {@link AutomaticsTapApi}
	 * @param timeToCompare time stamp from the first cycle of log
	 * @return boolean return true if second cycle log has occurred in 15 mins of
	 *         time
	 * @Refactor Athira
	 */
	public static boolean pingTestForSetIntervalTime(Dut device, AutomaticsTapApi tapEnv, String timeToCompare) {
		LOGGER.debug("STARTING METHOD : pingTestForSetIntervalTime()");
		boolean status = false;
		String response = null;
		String timeFromSecondCycleLog = null;
		boolean isTimeloggedDifferent = false;
		int timeDifference;
		try {
			long startTime = System.currentTimeMillis();
			do {
				response = tapEnv.executeCommandUsingSsh(device, BroadBandCommonUtils.concatStringUsingStringBuffer(
						BroadBandTestConstants.GREP_COMMAND, BroadBandTraceConstants.PING_SERVER_SUCCESS_LOG_MESSAGE,
						BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandCommandConstants.FILE_SELFHEAL_LOG,
						BroadBandTestConstants.SYMBOL_PIPE, BroadBandTestConstants.CMD_TAIL_1));
				LOGGER.info("Response is" + response);
				if (CommonMethods.isNotNull(response)
						&& CommonMethods.patternMatcher(response,
								BroadBandTraceConstants.PING_SERVER_SUCCESS_LOG_MESSAGE.replace(
										BroadBandTestConstants.TEXT_DOUBLE_QUOTE,
										BroadBandTestConstants.EMPTY_STRING))) {
					timeFromSecondCycleLog = CommonMethods.patternFinder(response,
							BroadBandTestConstants.PATTERN_FINDER_TO_GREP_TIMESTAMP);
					LOGGER.info("The time stamp grepped is: " + timeFromSecondCycleLog);
					isTimeloggedDifferent = CommonMethods.isNotNull(timeFromSecondCycleLog)
							&& !timeFromSecondCycleLog.equalsIgnoreCase(timeToCompare);
					if (isTimeloggedDifferent) {
						LOGGER.info("The ping server connectivity message for the next cycle is-" + response);
						timeDifference = BroadBandWiFiUtils.getDifferenceInMinutes(timeToCompare,
								timeFromSecondCycleLog);
						LOGGER.info("The time difference between the default cycle and next cycle is" + timeDifference);
						if (timeDifference == BroadBandTestConstants.CONSTANT_15) {
							status = true;
						} else {
							break;
						}
					}
				}
			} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.SIXTEEN_MINUTES_IN_MILLIS
					&& !status
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
		} catch (Exception exception) {
			LOGGER.error("Exception occurred while checking ping test for the set ping interval time : "
					+ exception.getMessage());
		}
		LOGGER.debug("ENDING METHOD : pingTestForSetIntervalTime()");
		return status;
	}

	/**
	 * Utility method to validate the all processes defined in properties with given
	 * response and property key.
	 * 
	 * @param device         The Dut to be validated.
	 * @param tapEnv         The {@link AutomaticsTapApi} instance.
	 * @param response       The given command response.
	 * @param processListKey The process list property key.
	 * @return {@link BroadBandResultObject } with verification details.
	 */
	public static BroadBandResultObject validateAllProcessesDefinedInProperties(Dut device, AutomaticsTapApi tapEnv,
			String response, String processList) {

		StringBuffer processNotInitialized = new StringBuffer();
		StringBuffer processInitializedMoreThanOne = new StringBuffer();
		BroadBandResultObject broadBandResultObject = new BroadBandResultObject();

		List<String> runningProcessList = new ArrayList<>();
		List<String> notRunningProcessList = new ArrayList<>();
		Map<String, String> pidMap = new HashMap<String, String>();
		boolean isProcessNotInitializedProperly = false;

		List<String> processNames = Arrays.asList(processList.split(","));

		for (String processRegex : processNames) {
			String pidValue = null;
			Pattern pattern = Pattern.compile("(\\w+)(.*)" + processRegex.trim() + "(\\s|$)");
			// Pattern pattern = Pattern.compile(processRegex.trim() + "(\\s|$)");
			Matcher matcher = pattern.matcher(response);
			int count = 0;
			String matchedString = "";
			while (matcher.find()) {
				matchedString = matcher.group();
				// LOGGER.info("PID: " + matcher.group(1));
				pidValue = CommonMethods.isNull(pidValue) ? matcher.group(1)
						: pidValue.concat(",").concat(matcher.group(1));
				count++;
			}

			if (0 == count) {
				processNotInitialized.append(processRegex).append(",");
				notRunningProcessList.add(processRegex);
				isProcessNotInitializedProperly = true;
			} else if (count > 1) {
				// Seems like rdkbLogMonitor.sh creating child process which is
				// there for 25 to
				// 30 minutes. We can ignore the duplicate process.
				if (!processRegex.equalsIgnoreCase("rdkbLogMonitor.sh")
						&& !processRegex.equalsIgnoreCase("snmp_subagent")
						&& !processRegex.equalsIgnoreCase("dnsmasq")) {
					processInitializedMoreThanOne.append(" '").append(processRegex).append("' ")
							.append(" IS INITIALIZED ").append(count).append(" TIMES  | ");
					isProcessNotInitializedProperly = true;
				}
			} else {
				LOGGER.info("PROCESS ' " + processRegex + "' INITIALIZED");
				runningProcessList.add(matchedString);
				pidMap.put(processRegex, pidValue);
			}

		}

		String notInitialized = processNotInitialized.toString();
		String moreThanOneProcessInitialized = processInitializedMoreThanOne.toString();
		String errorMessage = "";

		if (CommonMethods.isNotNull(notInitialized)) {
			errorMessage = "PROCESS(S)  NOT INITIALIZED : " + notInitialized;
			LOGGER.info(errorMessage);
		}
		if (CommonMethods.isNotNull(moreThanOneProcessInitialized)) {
			errorMessage = " | PROCESS(S) INITIALIZED MORE THAN ONCE : " + moreThanOneProcessInitialized;
			LOGGER.info(errorMessage);
		}

		broadBandResultObject.setStatus(!isProcessNotInitializedProperly);
		broadBandResultObject.setErrorMessage(errorMessage);
		broadBandResultObject.setRunningProcessList(runningProcessList);
		broadBandResultObject.setNotRunningProcessList(notRunningProcessList);
		broadBandResultObject.setProcessPidMap(pidMap);

		return broadBandResultObject;
	}

	/**
	 * Utility method to verify all required Broadband processes. The required
	 * processes details are retrieved from 'common.properties' property file
	 * 
	 * @param device The Dut to be validated
	 * @param tapEnv The {@link AutomaticsTapApi } instance.
	 * @return {@link BroadBandResultObject } with verification details.
	 * @author Govardhan
	 */
	public static BroadBandResultObject verifyAllRequiredBroadBandProcessesForQt(Dut device, AutomaticsTapApi tapEnv) {

		String processListKey = "arm.process.list.qt.";
		String processList = null;
		try {
			processList = getAutomaticsPropsValueByResolvingPlatform(device, processListKey);
		} catch (Exception e) {
			LOGGER.info("Process List not configured for the Device. Proceeding with default Process List");
			processList = AutomaticsPropertyUtility.getProperty(processListKey);
		}
		String commandResponse = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.CMD_GET_PROCESS_DETAILS);
		return validateAllProcessesDefinedInProperties(device, tapEnv, commandResponse, processList);
	}

	/**
	 * Utility method to remove all different signed extensions in requested build
	 * name.
	 * 
	 * @param buildImageName The build name to be formatted
	 * @return THe requested build name without signed extension.
	 */
	public static String removeDifferentSignedExtensionsInRequestedBuildName(String buildImageName) {
		String buildImageWithoutExtension = StringUtils
				.replaceEach(buildImageName, BroadBandTestConstants.IMAGE_VERSION, BroadBandTestConstants.REPLACE)
				.trim();

		return buildImageWithoutExtension;
	}

	/**
	 * Utility method to identify whether device is loaded with PROD build or not.
	 * 
	 * @param device The Dut to be used.
	 * @return True if device is loaded with prod build.
	 * @author Govardhan
	 */
	public static boolean isProdBuildOnDevice(Dut device) {
		return device.getFirmwareVersion().toLowerCase().contains("prod");
	}

	/**
	 * Utility method to Convert string to Double Value.
	 * 
	 * @param stringToConvert String which needs to be converted to Double value.
	 *
	 * @return Double value post conversion.
	 * @author Govardhan
	 */
	public static double convertStringToDouble(String stringToConvert) {
		LOGGER.debug("STARTING METHOD : convertStringToDouble()");
		double returnValue = 0.0;
		try {
			returnValue = Double.parseDouble(stringToConvert);
		} catch (NumberFormatException nfe) {
			LOGGER.error(
					"Number format Exception occured while trying to parse the string to Double -  " + stringToConvert,
					nfe);
		}
		LOGGER.debug("ENDING METHOD: convertStringToDouble()");
		return returnValue;
	}

	/**
	 * Method to Check for required pattern string in Target String.
	 * 
	 * @param targetString
	 * 
	 * @param patterToSearch
	 * 
	 * @return isPatterFoundInText return True if targetString Matches with the
	 *         patter to search string else returns False.
	 * @author Govardhan
	 */
	public static boolean patternSearchFromTargetString(String targetString, String patterToSearch) {
		LOGGER.debug("START METHOD: patternSearchFromTxt");
		boolean isPatternFoundInText = false;
		if (CommonMethods.isNotNull(targetString) && CommonMethods.isNotNull(patterToSearch)) {
			isPatternFoundInText = CommonUtils.patternSearchFromTargetString(targetString, patterToSearch);
		} else {
			LOGGER.error("Null value passed for TargetString/patterntosearch. TARGET STRING: " + targetString
					+ " PATTERN TO SEARCH: " + patterToSearch);
		}
		return isPatternFoundInText;
	}

	/**
	 * @param response response to validate
	 * @author Govardhan
	 */

	public static int convertStringToInteger(String stringToConvert) {
		int return_value = 0;
		try {
			return_value = Integer.parseInt(stringToConvert);
		} catch (NumberFormatException nfe) {
			LOGGER.error("Number format exception occured while trying to convert: " + stringToConvert, nfe);
		}
		return return_value;
	}

	/**
	 * Helper method is to format MAC address to remove leading zeros.
	 * 
	 * @param macAddress The given MAC address.
	 * @return Formatted string.
	 */
	public static String formatIpOrMacWithoutLeadingZeros(String targetString) {

		StringBuffer formattedString = new StringBuffer();

		if (CommonMethods.isNotNull(targetString)) {

			String[] splitStrings = targetString.split(BroadBandTestConstants.DELIMITER_COLON);
			for (int index = 0; index < splitStrings.length; index++) {
				int value = Integer.parseInt(splitStrings[index], 16);
				formattedString.append(Integer.toHexString(value));

				if (index + 1 != splitStrings.length) {
					formattedString.append(BroadBandTestConstants.DELIMITER_COLON);
				}
			}
		}
		LOGGER.info("Formatted Mac Address  = " + formattedString);

		return formattedString.toString();
	}

	/**
	 * Utility method to set device in router mode using WebPA command.
	 * 
	 * @param tapApi  {@link AutomaticsTapApi}
	 * @param device  {@link Dut}
	 * @param lanMode mode to set
	 * @return true if setting lan mode is success else false
	 */
	public static boolean setAndVerifyLanModeUsingWebPaCommands(AutomaticsTapApi tapApi, Dut device, String lanMode) {

		LOGGER.debug("STARTING METHOD: setLanModeUsingWebPaCommands()");

		// status
		boolean status = false;
		// maximum loop count to perform if the operation is failed
		int maxLoopCount = 2;
		// server response
		String response = null;
		try {
			for (int index = 1; index <= maxLoopCount; index++) {

				LOGGER.info(index + "/" + maxLoopCount + "# performing operation to set device lan mode to " + lanMode);

				// set lan mode
				tapApi.setWebPaParams(device, BroadBandWebPaConstants.WEBPA_PARAM_BRIDGE_MODE_STATUS, lanMode,
						BroadBandTestConstants.CONSTANT_0);

				tapApi.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);

				response = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapApi,
						BroadBandWebPaConstants.WEBPA_PARAM_BRIDGE_MODE_STATUS);

				if (CommonMethods.isNotNull(response)) {
					status = response.contains(lanMode);
					if (status) {
						break;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.info("Exception in setAndVerifyLanModeUsingWebPaCommands. ", e);
		}
		LOGGER.info("Status of setting device lan mode to " + lanMode + " is " + (status ? "SUCCESS" : "FAILED"));

		LOGGER.debug("ENDING METHOD: setLanModeUsingWebPaCommands()");
		if (!status) {
			LOGGER.error(
					"unable to change lanmode Device.X_CISCO_COM_DeviceControl.LanManagementEntry.1.LanMode to value "
							+ lanMode);
			throw new TestException(
					"unable to change lanmode Device.X_CISCO_COM_DeviceControl.LanManagementEntry.1.LanMode to value "
							+ lanMode);
		}
		return status;
	}

	/**
	 * Method to get snmp mib index to get wan mac from a device
	 * 
	 * @param tapEnv AutomaticsTapApi instance
	 * @param device device instance
	 * @return index to get wanmac
	 * 
	 * 
	 */
	public static String getIndexForWanMac(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("STARTING METHOD:getIndexForWanMac");
		String index = null;
		index = BroadBandTestConstants.STRING_VALUE_ONE;
		LOGGER.info("Index to be used to get device mac is-" + index);
		LOGGER.debug("ENDING METHOD:getIndexForWanMac");
		return index;
	}

	/**
	 * Utility method to set device in router mode using WebPA command.
	 * 
	 * @param tapApi {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @return true if setting lan mode is success else false
	 */
	public static boolean setDeviceInBridgeStaticModeStatusUsingWebPaCommand(AutomaticsTapApi tapApi, Dut device) {
		return setAndVerifyLanModeUsingWebPaCommands(tapApi, device, BroadBandTestConstants.CONSTANT_BRIDGE_STATIC);
	}

	/**
	 * Utility method to set device in router mode using WebPA command.
	 * 
	 * @param tapApi {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @return true if setting lan mode is success else false
	 */
	public static boolean setDeviceInRouterModeStatusUsingWebPaCommand(AutomaticsTapApi tapApi, Dut device) {
		return setAndVerifyLanModeUsingWebPaCommands(tapApi, device, BroadBandTestConstants.LAN_MANAGEMENT_MODE_ROUTER);
	}

	/**
	 * Uses the partial key by appending it with the device platform to get the
	 * property value from cats.props.
	 *
	 * @param device          The Dut instance.
	 * @param partialPropsKey Partial property key which will be appended with the
	 *                        device platform to form the complete key.(Example:-
	 *                        "cdl.unsigned.image.name." it get resolved to
	 *                        "cdl.unsigned.image.name.pace_x1")
	 *
	 * @return CATS property value
	 */
	public static String getAutomaticsPropsValueByResolvingPlatform(Dut device, String partialPropsKey) {
		String propertyToCheck = partialPropsKey + device.getManufacturer().toLowerCase() + "_"
				+ device.getModel().toLowerCase();
		LOGGER.info("Platform specific resolved property key is : " + propertyToCheck);
		return AutomaticsTapApi.getSTBPropsValue(propertyToCheck);
	}

	/**
	 * Method to validate uptime from snmp response and uptime command
	 * 
	 * @param snmpCommandOutput Response of snmp command
	 * @param uptimeCmdResponse Response of uptime command
	 * 
	 * @return status Validation status
	 */
	public static boolean verifyUptimeFromSnmpOutput(String snmpCommandOutput, String uptimeCmdResponse) {

		boolean status = false;
		try {
			String snmpUptimeResponse = CommonMethods.patternFinder(snmpCommandOutput,
					BroadBandTestConstants.SYS_UP_TIME_INSTANCE);
			LOGGER.info("Uptime from Snmp = " + snmpUptimeResponse);

			if (CommonUtils.isNotEmptyOrNull(snmpUptimeResponse)) {

				int day = Integer.parseInt(snmpUptimeResponse.split(":")[0]);
				int hour = Integer.parseInt(snmpUptimeResponse.split(":")[1]);
				int min = Integer.parseInt(snmpUptimeResponse.split(":")[2]);
				int sec = Integer.parseInt(snmpUptimeResponse.split(":")[3]);

				int cmd_uptimeInSeconds = Integer.parseInt(uptimeCmdResponse);
				int snmp_uptimeInSeconds = (day * 24 * 60 * 60) + (hour * 60 * 60) + (min * 60) + sec;
				LOGGER.info(
						"Uptime command output = " + cmd_uptimeInSeconds + "\nsnmp uptime = " + snmp_uptimeInSeconds);

				if (snmp_uptimeInSeconds - cmd_uptimeInSeconds <= RDKBTestConstants.TWO_MINUTES_IN_SECONDS) {
					status = true;
					LOGGER.info("Snmp output and command output are matching");
				} else {
					LOGGER.error("Uptime is not matching" + " Uptime from snmp = " + snmpUptimeResponse
							+ " Uptime from command = " + uptimeCmdResponse);
				}

			} else {
				LOGGER.error("Failed to retrieve uptime from snmp response " + snmpCommandOutput);
			}
			LOGGER.debug("Uptime validation status = " + status);
		} catch (Exception e) {
			LOGGER.error("Exception occurred while testing " + e.getMessage(), e);
		}
		return status;
	}

	/**
	 * Compares list string with the limit value
	 * 
	 * 
	 * @param device The device to be validated
	 * @return true is it satisfies the limit value
	 */
	public static boolean compareListBetweenLimitRange(List<String> match, int firstLimitValue, int secondLimitValue) {

		boolean status = false;
		for (int i = 0; i < match.size(); i++) {
			int matchList = Integer.parseInt(match.get(i));
			if (matchList >= firstLimitValue && matchList <= secondLimitValue) {
				status = true;
			} else {
				status = false;
				LOGGER.error("List is not between the given limit, Expected: Should be between " + firstLimitValue
						+ " and " + secondLimitValue + " Actual " + matchList);
				break;
			}
		}
		LOGGER.info("Status of list between the given limit " + status);
		return status;
	}

	/**
	 * Helper Method to Post JSON Data to the passed URL using TLS Enabled HTTP
	 * Client.
	 * 
	 * @param tapEnv       {@link AutomaticsTapApi}
	 * @param device       {@link Dut}
	 * @param requestUrl   String representing the Request URL
	 * @param postDataJson String the data to be posted in JSON Format.
	 * @return Boolean representing the result of the POST operation.
	 * @refactor Govardhan
	 */
	public static boolean postDataTlsEnabledClient(AutomaticsTapApi tapEnv, Dut device, String requestUrl,
			String postDataJson) {
		LOGGER.debug("STARTING METHOD: postDataTlsEnabledClient");
		boolean result = false;
		HttpResponse response = null;
		try {
			HttpClient httpClient = CommonMethods.getTlsEnabledHttpClient();
			LOGGER.info("CREATE REQUEST WITH URL: " + requestUrl);
			HttpPost request = new HttpPost(requestUrl);
			LOGGER.info("CREATE REQUEST ENTITY WITH POST DATA: " + postDataJson);
			HttpEntity params = new StringEntity(postDataJson);
			request.addHeader("content-type", "application/json");
			request.setEntity(params);
			LOGGER.info("GOING TO EXECUTE THE HTTP POST REQUEST.");
			response = httpClient.execute(request);
		} catch (UnsupportedEncodingException unsupportedEncodingException) {
			// Log & Suppress the exception
			LOGGER.error("EXCEPTION OCCURRED WHILE POSTING JSON DATA USING TLS ENABLED CLIENT: "
					+ unsupportedEncodingException.getMessage());
		} catch (ClientProtocolException clientProtocolException) {
			// Log & Suppress the exception
			LOGGER.error("EXCEPTION OCCURRED WHILE POSTING JSON DATA USING TLS ENABLED CLIENT: "
					+ clientProtocolException.getMessage());
		} catch (IOException ioException) {
			// Log & Suppress the exception
			LOGGER.error(
					"EXCEPTION OCCURRED WHILE POSTING JSON DATA USING TLS ENABLED CLIENT: " + ioException.getMessage());
		}
		// Process the response obtained.
		if (null != response) {
			LOGGER.info("HTTP RESPONSE (POST):" + response.toString());
			StatusLine status = response.getStatusLine();
			LOGGER.info("POST RESPONSE STATUS = " + status);
			LOGGER.info("POST RESPONSE CODE:" + status.getStatusCode());
			result = HttpStatus.SC_OK == status.getStatusCode();
		}
		LOGGER.info("JSON DATA POSTED SUCCESSFULLY TO THE URL: " + result);
		LOGGER.debug("ENDING METHOD: postDataTlsEnabledClient");
		return result;
	}

	/**
	 * Utility Method to perform the device reboot operation as a part of
	 * pre-condition.
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @refactor Govardhan
	 */
	public static void rebootDeviceAsPreCondition(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("STARTING METHOD rebootDeviceAsPreCondition");
		LOGGER.info("### BEGIN PRE-CONDITION ### REBOOT THE DEVICE.");
		if (!CommonUtils.rebootAndWaitForIpAcquisition(tapEnv, device)) {
			String message = "UNABLE TO REBOOT THE DEVICE.";
			LOGGER.error(message);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + message);
		}
		LOGGER.info("### END PRE-CONDITION ### REBOOTED THE DEVICE.");
		LOGGER.debug("ENDING METHOD rebootDeviceAsPreCondition");
	}

	/**
	 * Utility Method to perform the check on whether file exists. The file check
	 * operation is being performed by polling for the existence of file for 10
	 * minutes.
	 * 
	 * @param tapEnv           {@link AutomaticsTapApi}
	 * @param device           {@link Dut}
	 * @param completeFilePath String representing the complete file path.
	 * @refactor Govardhan
	 */
	public static void doesFileExistPreCondition(AutomaticsTapApi tapEnv, Dut device, String completeFilePath) {
		LOGGER.debug("STARTING METHOD doesFileExistPreCondition");
		long pollDuration = BroadBandTestConstants.TEN_MINUTE_IN_MILLIS;
		long startTime = System.currentTimeMillis();
		boolean result = false;
		LOGGER.info("### BEGIN PRE-CONDITION ### VERIFY THE PRESENCE OF THE FILE: " + completeFilePath);
		do {
			result = CommonUtils.isFileExists(device, tapEnv, completeFilePath);
		} while (!result && ((System.currentTimeMillis() - startTime) < pollDuration)
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));

		if (!result) {
			String message = "UNABLE TO VERIFY THE PRESENCE OF " + completeFilePath + " LOG FILE.";
			LOGGER.error(message);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + message);
		}
		LOGGER.info("### END PRE-CONDITION ### VERIFIED THE PRESENCE OF THE FILE: " + completeFilePath);
		LOGGER.debug("ENDING METHOD doesFileExistPreCondition");
	}

	/**
	 * Utility Method to retrieve the current Date and time from the device
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * 
	 * @return Returns the current cutrrent timestamp from device.
	 * @refactor Govardhan
	 */
	public static String getCurrentTimeStampOnDevice(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.info("STARTING METHOD : getCurrentTimeStampOnDevice()");
		String currentTimeStamp = tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_DATE_LOG_FORMAT);
		tapEnv.waitTill(BroadBandTestConstants.TWO_SECOND_IN_MILLIS);
		LOGGER.info("ENDING METHOD : getCurrentTimeStampOnDevice()");
		return currentTimeStamp;
	}

	/**
	 * Utility method to perform validation as operations like NULL, NOT NULL,
	 * COMPARISON, ZERO, NON ZERO, RANGE etc.
	 * 
	 * @param operation     String representing the operation. The possible values
	 *                      to be passed can be taken from the Switch Case block.
	 *                      Possible values are: TXT_NULL, TXT_NOT_NULL,
	 *                      TXT_COMPARISON, INT_ZERO, INT_NON_ZERO, INT_COMPARISON,
	 *                      INT_RANGE.
	 * @param expectedValue String representing the Expected Value. In case of
	 *                      integer, it will be parsed from String.If not
	 *                      applicable, can be passed as NULL.
	 * @param actualValue   String representing the Actual Value. In case of
	 *                      integer, it will be parsed from String.
	 * 
	 * @return Boolean representing the result of validation; TRUE in case the
	 *         comparison is successful; FALSE in case the comparison fails.
	 * @author Govardhan
	 */
	public static boolean compareValues(String operation, String expectedValue, String actualValue) {
		LOGGER.debug("STARTING METHOD performCompareOperation");
		boolean result = false;
		switch (operation) {
		// Validation for the actual value is a BOOLEAN
		case "BOOL_CHECK":
			result = CommonMethods.isNotNull(actualValue)
					&& (actualValue.trim().equalsIgnoreCase(BroadBandTestConstants.TRUE)
							|| actualValue.trim().equalsIgnoreCase(BroadBandTestConstants.FALSE));
			break;

		// Validation for the actual value (TEXT) is NULL
		case "TXT_NULL":
			result = CommonMethods.isNull(actualValue);
			break;

		// Validation for the actual value (TEXT) is NOT NULL
		case "TXT_NOT_NULL":
			result = CommonMethods.isNotNull(actualValue);
			break;

		// Validation for the expected value is same as actual value (TEXT COMPARISON)
		case "TXT_COMPARISON":
			result = CommonMethods.isNotNull(actualValue) && CommonMethods.isNotNull(expectedValue)
					&& expectedValue.trim().equalsIgnoreCase(actualValue.trim());
			break;

		// Validation for the value (INTEGER) is 0.
		case "INT_ZERO":
			try {
				int iActualValue = Integer.parseInt(actualValue);
				LOGGER.info("ACTUAL VALUE (INTEGER): " + iActualValue);
				result = iActualValue == 0;
			} catch (NumberFormatException numberFormatException) {
				LOGGER.error("EXCEPTION OCCURRED WHILE TRYING TO PARSE THE STRING: " + actualValue + " TO INTEGER - "
						+ numberFormatException);
			}
			break;

		// Validation for the value (INTEGER) is NOT 0.
		case "INT_NON_ZERO":
			try {
				int iActualValue = Integer.parseInt(actualValue);
				LOGGER.info("ACTUAL VALUE (INTEGER): " + iActualValue);
				result = iActualValue != 0;
			} catch (NumberFormatException numberFormatException) {
				LOGGER.error("EXCEPTION OCCURRED WHILE TRYING TO PARSE THE STRING: " + actualValue + " TO INTEGER - "
						+ numberFormatException);
			}
			break;

		// Validation for the value (INTEGER) is Unsigned and NOT 0.
		case "INT_UNSIGNED":
			try {
				int iActualValue = Integer.parseInt(actualValue);
				LOGGER.info("ACTUAL VALUE (INTEGER): " + iActualValue);
				result = iActualValue > 0;
			} catch (NumberFormatException numberFormatException) {
				LOGGER.error("EXCEPTION OCCURRED WHILE TRYING TO PARSE THE STRING: " + actualValue + " TO INTEGER - "
						+ numberFormatException);
			}
			break;

		// Validation for the value (INTEGER) is Unsigned Including 0.
		case "INT_UNSIGNED_INC_ZERO":
			try {
				int iActualValue = Integer.parseInt(actualValue);
				LOGGER.info("ACTUAL VALUE (INTEGER): " + iActualValue);
				result = iActualValue >= 0;
			} catch (NumberFormatException numberFormatException) {
				LOGGER.error("EXCEPTION OCCURRED WHILE TRYING TO PARSE THE STRING: " + actualValue + " TO INTEGER - "
						+ numberFormatException);
			}
			break;

		// Validation for the expected value is same as actual value (INTEGER
		// COMPARISON)
		case "INT_COMPARISON":
			try {
				int iActualValue = Integer.parseInt(actualValue.trim());
				LOGGER.info("ACTUAL VALUE (INTEGER): " + iActualValue);
				int iExpectedValue = Integer.parseInt(expectedValue.trim());
				LOGGER.info("EXPECTED VALUE (INTEGER): " + iExpectedValue);
				result = iActualValue != 0 && iExpectedValue != 0 && iActualValue == iExpectedValue;
			} catch (NumberFormatException numberFormatException) {
				LOGGER.error("EXCEPTION OCCURRED WHILE TRYING TO PARSE THE STRING: " + actualValue + " OR "
						+ expectedValue + " TO INTEGER - " + numberFormatException);
			}
			break;

		// Validation for the actual value is with in the range (INTEGER).
		// Expected value must be passed as LOWER_VALUE-HIGHER_VALUE as 5-10.
		case "INT_RANGE":
			try {
				String[] arrRange = expectedValue.split(BroadBandTestConstants.SYMBOL_HYPHEN);
				if (arrRange.length == BroadBandTestConstants.CONSTANT_2) {
					int iExpectedLowerValue = Integer.parseInt(arrRange[0].trim());
					LOGGER.info("ACTUAL LOWER RANGE VALUE (INTEGER): " + iExpectedLowerValue);
					int iExpectedHigherValue = Integer.parseInt(arrRange[1].trim());
					LOGGER.info("ACTUAL HIGHER RANGE VALUE (INTEGER): " + iExpectedHigherValue);
					int iActualValue = Integer.parseInt(actualValue.trim());
					LOGGER.info("ACTUAL VALUE (INTEGER): " + iActualValue);
					result = iActualValue != 0 && iActualValue <= iExpectedHigherValue
							&& iActualValue >= iExpectedLowerValue;
				} else {
					LOGGER.info(
							"EXPECTED VALUE PARAMETER DOES NOT HAVE THE REQUIRED VALUES, LOWER RANGE & HIGHER RANGE: "
									+ expectedValue);
				}
			} catch (NumberFormatException numberFormatException) {
				LOGGER.error("EXCEPTION OCCURRED WHILE TRYING TO PARSE THE STRING: " + actualValue + " OR "
						+ expectedValue + " (RANGE VALUE) TO INTEGER - " + numberFormatException);
			}
			break;

		// Validation for the actual value is within the range(includes zero in range).
		// Expected value must be passed as LOWER_VALUE-HIGHER_VALUE eg:- 0-8547895412.
		case "BIG_INT_RANGE_INC_ZERO":
			try {
				if (CommonMethods.isNotNull(expectedValue)) {
					String[] arrRange = expectedValue.split(BroadBandTestConstants.SYMBOL_HYPHEN);
					if (arrRange.length == BroadBandTestConstants.CONSTANT_2) {
						BigInteger expectedLowerValue = new BigInteger(arrRange[0].trim());
						LOGGER.info("ACTUAL LOWER RANGE VALUE : " + expectedLowerValue);
						BigInteger expectedHigherValue = new BigInteger(arrRange[1].trim());
						LOGGER.info("ACTUAL HIGHER RANGE VALUE : " + expectedHigherValue);
						BigInteger actualValueRetrieved = new BigInteger(actualValue.trim());
						LOGGER.info("ACTUAL VALUE : " + actualValueRetrieved);
						result = (actualValueRetrieved
								.compareTo(expectedLowerValue) == BroadBandTestConstants.CONSTANT_0
								|| actualValueRetrieved
										.compareTo(expectedLowerValue) == BroadBandTestConstants.CONSTANT_1)
								&& (actualValueRetrieved
										.compareTo(expectedHigherValue) == BroadBandTestConstants.CONSTANT_0
										|| actualValueRetrieved.compareTo(
												expectedHigherValue) == BroadBandTestConstants.INT_VALUE_MINUS_ONE);
					} else {
						LOGGER.info(
								"EXPECTED VALUE PARAMETER DOES NOT HAVE THE REQUIRED VALUES, LOWER RANGE & HIGHER RANGE: "
										+ expectedValue);
					}
				} else {
					LOGGER.info("EXPECTED VALUE PARAMETER IS NULL");
				}
			} catch (NumberFormatException numberFormatException) {
				LOGGER.error("EXCEPTION OCCURRED WHILE TRYING TO PARSE THE STRING: " + actualValue + " OR "
						+ expectedValue + " (RANGE VALUE) TO BIGINTEGER - " + numberFormatException);
			}
			break;

		// If the Operation is invalid, error to be logged.
		default:
			LOGGER.error("INVALID OPERATION PASSED AS PARAMETER: " + operation);
		}
		LOGGER.info("VALIDATION RESULT OF OPERATION = " + operation + ", EXPECTED VALUE = " + expectedValue
				+ ", ACTUAL VALUE = " + actualValue + " IS: " + result);
		LOGGER.debug("ENDING METHOD performCompareOperation");
		return result;
	}

	/**
	 * Utility method to get the CMTS MAC Address from Arp table corresponding to
	 * Erouter interface.
	 * 
	 * @param device Dut instance
	 * @param tapEnv AutomaticsTapApi instance
	 * 
	 * @return CMTS MAC Address from Arp Table
	 * @author Govardhan
	 */
	public static String getCmtsMacAddressFromArpTableErouterInterface(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: getCmtsMacAddressFromArpTableErouterInterface()");
		// Variable declaration starts
		String response = null;
		String cmtsMacAddressFromArpCommand = null;
		// Variable declaration ends
		try {
			LOGGER.info("Getting Arp Table.");
			response = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.COMMAND_ARP);
			LOGGER.info("Arp Table: " + response);
			if (CommonMethods.isNotNull(response) && BroadBandCommonUtils.patternSearchFromTargetString(response,
					BroadBandTestConstants.INTERFACE_NAME_EROUTER0)) {
				response = tapEnv.executeCommandUsingSsh(device,
						BroadBandTestConstants.CMD_ARP_TABLE_FOR_EROUTER0_INTERFACE);
				cmtsMacAddressFromArpCommand = CommonMethods.caseInsensitivePatternFinder(response,
						BroadBandTestConstants.REG_EXPRESSION_TO_GET_MAC_ADDRESS_SEMICOLON);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while getting CMTS Mac address from Arp Table.");
		}
		LOGGER.debug("ENDING METHOD: getCmtsMacAddressFromArpTableErouterInterface()");
		return cmtsMacAddressFromArpCommand;
	}

	/**
	 * Utility method to calculate Broadband device maintenance window(start time
	 * and end time) based on current device local time with wait time.
	 * 
	 * @param tapEnv The {@link AutomaticsTapApi} instance.
	 * @param device The device to be validated.
	 * 
	 * @return Maintenance window details.
	 */
	public static Map<String, String> calculateFirmwareUpgradeMaintenanceWindowWithTimeInterval(AutomaticsTapApi tapEnv,
			Dut device, long waitTime) {
		LOGGER.debug("STARTING METHOD: calculateFirmwareUpgradeMaintenanceWindowWithTimeInterval");
		Map<String, String> maintenanceWindow = new HashMap<String, String>();

		String firmwareUpgradeStartTime = AutomaticsPropertyUtility
				.getProperty(BroadBandTestConstants.PROP_KEY_DEFAULT_MAINTENANCE_WINDOW_START_TIME, "3600");
		String firmwareUpgradeEndTime = AutomaticsPropertyUtility
				.getProperty(BroadBandTestConstants.PROP_KEY_DEFAULT_MAINTENANCE_WINDOW_END_TIME, "14400");
		String currentDeviceLocalTime = null;
		long currentTimeInSeconds = 0L;
		long startTime = 0L;
		long endTime = 0L;
		try {
			currentDeviceLocalTime = tapEnv.executeCommandUsingSsh(device, "LTime");
			if (CommonMethods.isNotNull(currentDeviceLocalTime)) {
				String[] splitHourMinSec = currentDeviceLocalTime.split(":");
				if (splitHourMinSec.length == 3) {
					currentTimeInSeconds = Integer.parseInt(splitHourMinSec[0].trim()) * 60 * 60
							+ Integer.parseInt(splitHourMinSec[1].trim()) * 60
							+ Integer.parseInt(splitHourMinSec[2].trim());
					/*
					 * Maintenance window is calculated and configured as 0 - 86400( 24 hour format)
					 * and reset again to 0 at midnight. If current time is between 12 AM(00:00:00)
					 * and 1AM (01:00:00). Maintenance window start time is current time and end
					 * time will be start time + 1200 seconds(20 minutes)
					 */
					if (currentTimeInSeconds > 0 && currentTimeInSeconds <= 3600) {
						startTime = currentTimeInSeconds + (waitTime * 60);
						endTime = startTime + (20 * 60);
						/*
						 * If maintenance window time is between 84600(23:30:00) and 86399(23:59:59).
						 * Calculate the upgrade end time from 0 instead of adding 3600 to current time
						 * which exceed the 86399.
						 */
					} else if (currentTimeInSeconds >= 84600 && currentTimeInSeconds <= 86399) {
						startTime = (currentTimeInSeconds + (waitTime * 60)) < 86399
								? currentTimeInSeconds + (waitTime * 60)
								: (currentTimeInSeconds + (waitTime * 60)) - 86399;
						endTime = (startTime + (20 * 60)) < 86399 ? (startTime + (20 * 60))
								: (startTime + (20 * 60)) - 86399;
						/*
						 * Other cases, calculate maintenance window time like -> start time = current
						 * time + (waitTime * 60) seconds end time = start time + 1200 seconds(20
						 * Minutes)
						 */
					} else {
						startTime = currentTimeInSeconds + (waitTime * 60);
						endTime = startTime + (20 * 60);
					}
					firmwareUpgradeStartTime = Long.toString(startTime);
					firmwareUpgradeEndTime = Long.toString(endTime);
				}
			}
			maintenanceWindow.put("main_window_start_time", firmwareUpgradeStartTime);
			maintenanceWindow.put("main_window_end_time", firmwareUpgradeEndTime);
			maintenanceWindow.put("main_window_curr_time", Long.toString(currentTimeInSeconds));
			maintenanceWindow.put("device_local_time", currentDeviceLocalTime);
			LOGGER.info("Calculated Maintenance Window =>  Start Time : " + firmwareUpgradeStartTime + ", End Time : "
					+ firmwareUpgradeEndTime + " , Current Time : " + currentTimeInSeconds);
		} catch (Exception e) {
			LOGGER.error("Exception occurred while fetching the current time or calculating the maintenance window"
					+ e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: calculateFirmwareUpgradeMaintenanceWindowWithTimeInterval");
		return maintenanceWindow;
	}

	/**
	 * Method used to calculate and set the maintenance window
	 * 
	 * @param device     {@link Dut}
	 * @param startAFter Maintenance window start after given minutes
	 * @param interval   Maintenance window reboot interval
	 * @return status -True -Maintenance window set successful, Else False
	 */
	public static boolean calculateAndSetMaintanenceWindow(Dut device, AutomaticsTapApi tapEnv, long waitTime,
			Map<String, String> maintenanceWindow) {
		LOGGER.debug("STARTING METHOD: calculateAndSetMaintanenceWindow");
		boolean status = false;
		String errorMessage = null;
		String firmwareUpgradeStartTime = null;
		String firmwareUpgradeEndTime = null;
		String main_window_curr_time = null;
		try {
			firmwareUpgradeStartTime = maintenanceWindow.get("main_window_start_time");
			firmwareUpgradeEndTime = maintenanceWindow.get("main_window_end_time");
			main_window_curr_time = maintenanceWindow.get("main_window_curr_time");

			LOGGER.info("firmwareUpgradeStartTime : " + firmwareUpgradeStartTime + "==>"
					+ BroadBandCommonUtils.convertSecondsToHourMinuteSeconds(firmwareUpgradeStartTime));
			LOGGER.info("firmwareUpgradeEndTime : " + firmwareUpgradeEndTime + "==>"
					+ BroadBandCommonUtils.convertSecondsToHourMinuteSeconds(firmwareUpgradeEndTime));
			LOGGER.info("main_window_curr_time : " + main_window_curr_time + "==>"
					+ BroadBandCommonUtils.convertSecondsToHourMinuteSeconds(main_window_curr_time));
			if (CommonMethods.isNotNull(main_window_curr_time) && CommonMethods.isNotNull(firmwareUpgradeStartTime)
					&& CommonMethods.isNotNull(firmwareUpgradeEndTime)) {
				status = setMaiantenanceWindowInMockXconfRFC(device, firmwareUpgradeStartTime.toString(),
						firmwareUpgradeEndTime.toString(), tapEnv);
			}
		} catch (Exception exception) {
			errorMessage = "Exception occurred while setting maintenace window . Error is -" + exception.getMessage();
			LOGGER.error(errorMessage);
			status = false;
		}
		LOGGER.info("Is Maintenance window set successfully " + status);
		LOGGER.debug("ENDING METHOD: calculateAndSetMaintanenceWindow");
		return status;
	}

	/**
	 * Method used to convert the seconds to Hour Minutes and seconds.
	 * 
	 * @param seconds seconds to convert
	 * @return hhMmSs
	 */
	public static String convertSecondsToHourMinuteSeconds(String seconds) {
		LOGGER.debug("STARTING METHOD: convertSecondsToHourMinuteSeconds");
		String hhMmSs = null;
		try {
			Long secondsInLong = Long.parseLong(seconds);
			Long sec = secondsInLong % 60;
			Long hr = secondsInLong / 60;
			Long min = hr % 60;
			hr = hr / 60;
			hhMmSs = hr + ":" + min + ":" + sec;
		} catch (NumberFormatException e) {
			LOGGER.info("Exception while getting Hour,Minute and Seconds : " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: convertSecondsToHourMinuteSeconds");
		return hhMmSs;
	}

	/**
	 * Method to set the maintenance window start and end time in Proxy server
	 * 
	 * @param device    {@link Dut}
	 * @param startTime Maintenance window start time
	 * @param endTime   Maintenance window end time
	 * @param tapEnv    {@link AutomaticsTapApi}
	 * 
	 * @return true if successfully set
	 */
	private static boolean setMaintenanceWindowDataInProxyXconf(Dut device, String startTime, String endTime,
			AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: setMaintenanceWindowDataInProxyXconf");
		boolean result = false;
		String rfcPayLoadData = null;
		String stbMacAddress = device.getHostMacAddress();
		String payload = BroadBandTestConstants.STRING_RFC_DATA_GENERIC_PAYLOAD;
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject();
			payload = payload.replace(BroadBandTestConstants.CONSTANT_REPLACE_STBMAC_LSAPAYLOADDATA, stbMacAddress);
			payload = payload.replace(BroadBandTestConstants.STRING_PAYLOAD_REPLACE, "PFC");
			payload = payload.replace("\"effectiveImmediate\":true", "\"effectiveImmediate\":false");
			LOGGER.info("PAY LOAD DATA BEFORE REPLACE: " + payload);
			jsonObj.put(CommonMethods.concatStringUsingStringBuffer(BroadBandTestConstants.TR181_DOT,
					BroadBandWebPaConstants.WEBPA_COMMAND_MAINTENANCE_WINDOW_START_TIME), startTime);
			jsonObj.put(CommonMethods.concatStringUsingStringBuffer(BroadBandTestConstants.TR181_DOT,
					BroadBandWebPaConstants.WEBPA_COMMAND_MAINTENANCE_WINDOW_END_TIME), endTime);
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
				LOGGER.error("Device Mac address is not obtained");
			}
		} catch (JSONException e) {
			LOGGER.error("Exception while Posting RFC configuration");
			result = false;
		}
		LOGGER.info("Status of setting Maintenance window in xconf: " + result);
		LOGGER.debug("ENDING METHOD: setMaintenanceWindowDataInProxyXconf");
		return result;
	}

	/**
	 * Set the Maintenance window start and end time in Proxy xconf and configure
	 * the device to point to proxy server
	 * 
	 * @param device    {@link Dut}
	 * @param startTime Maintenance window start time
	 * @param endTime   Maintenance window end time
	 * @param tapEnv    {@link AutomaticsTapApi}
	 * @return true if device configuration to proxy server is success
	 * @throws JSONException
	 */
	public static boolean setMaiantenanceWindowInMockXconfRFC(Dut device, String startTime, String endTime,
			AutomaticsTapApi tapEnv) throws JSONException {
		LOGGER.debug("STARTING METHOD: setMaiantenanceWindowInMockXconfRFC");
		String proxyXconfUrl = AutomaticsTapApi.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PROXY_XCONF_RFC_URL);
		boolean status = false;
		String response = null;
		String errorMessage = null;
		String CMD_CLEAR_HASH_VALUE = "rm /tmp/RFC/.hashValue";
		try {
			if (setMaintenanceWindowDataInProxyXconf(device, startTime, endTime, tapEnv)) {
				if (BroadBandRfcFeatureControlUtils.copyRfcPropertiesFromEtcToNVRAM(device, tapEnv)) {
					status = BroadBandRfcFeatureControlUtils.copyAndUpdateRfcPropertiesNewXconfUrl(device, tapEnv,
							proxyXconfUrl);
					tapEnv.executeCommandUsingSsh(device, CMD_CLEAR_HASH_VALUE);
					BroadBandWebPaUtils.setVerifyWebPAInPolledDuration(device, tapEnv,
							BroadBandWebPaConstants.WEBPA_PARAM_RFC_CONTROL, BroadBandTestConstants.CONSTANT_2,
							BroadBandTestConstants.STRING_CONSTANT_1, BroadBandTestConstants.ONE_MINUTE_IN_MILLIS,
							BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
				} else {
					throw new TestException("Error faced while copying dcm properties file from etc to nvram folder");
				}
			} else {
				throw new TestException("Maintenance window not updated in XCONF");
			}
			if (status) {
				status = false;
				response = CommonUtils.searchLogFilesWithPollingInterval(tapEnv, device,
						BroadBandTraceConstants.LOG_MESSAGE_COMPLETED_RFC_PASS,
						BroadBandCommandConstants.FILE_DCMRFC_LOG, BroadBandTestConstants.TEN_MINUTE_IN_MILLIS,
						BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
				if (CommonMethods.isNotNull(response)) {
					LOGGER.info("Response from dcmrfc log file is " + response);
					status = CommonUtils.patternSearchFromTargetString(response,
							BroadBandTraceConstants.LOG_MESSAGE_COMPLETED_RFC_PASS);
				}
			}
		} catch (Exception exception) {
			errorMessage = exception.getMessage();
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}
		LOGGER.debug("ENDING METHOD: setMaiantenanceWindowInMockXconfRFC");
		return status;
	}

	/**
	 * Method used to get the current time from the device
	 * 
	 * @param device {@link Dut}
	 * @return current time from the device
	 */
	private static long getCurrentLocalTime(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: getCurrentLocalTime");
		long currentTimeInSeconds = 0;
		try {
			String currentDeviceLocalTime = tapEnv.executeCommandUsingSsh(device, "LTime");
			if (CommonMethods.isNotNull(currentDeviceLocalTime)) {
				String[] splitHourMinSec = currentDeviceLocalTime.split(":");
				if (splitHourMinSec.length == 3) {
					currentTimeInSeconds = Integer.parseInt(splitHourMinSec[0].trim()) * 60 * 60
							+ Integer.parseInt(splitHourMinSec[1].trim()) * 60
							+ Integer.parseInt(splitHourMinSec[2].trim());
				}
			}
		} catch (Exception e) {
			LOGGER.info("Exception occurred while getting the current time " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: getCurrentLocalTime");
		return currentTimeInSeconds;
	}

	/**
	 * Method used to get the Maintenance window details
	 * 
	 * @param device instance of {@link Dut}
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 */
	public static void getMaintenanceWindowDetails(Dut device, AutomaticsTapApi tapEnv) {
		try {
			LOGGER.info(" WINDOW START TIME :" + tapEnv.executeWebPaCommand(device,
					BroadBandWebPaConstants.WEBPA_COMMAND_MAINTENANCE_WINDOW_START_TIME));
			LOGGER.info(" WINDOW END TIME :" + tapEnv.executeWebPaCommand(device,
					BroadBandWebPaConstants.WEBPA_COMMAND_MAINTENANCE_WINDOW_END_TIME));
			LOGGER.info(" CODEBIG FIRST ENABLE :"
					+ tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_CODEBIG_FIRST_ENABLE));
			LOGGER.info(" DEVICE CURRENT TIME :" + tapEnv.executeCommandUsingSsh(device, "LTime"));
		} catch (Exception e) {
			LOGGER.info("Exception while getting Maintenance Window Details: " + e.getMessage());
		}
	}

	/**
	 * Method used to verify device reboot in the maintenance window
	 * 
	 * @param device                    instance of {@link Dut}
	 * @param tapEnv                    instance of {@link AutomaticsTapApi}
	 * @param waitTime                  Maintenance window start after given minutes
	 * @param maintenanceWindowInterval Maintenance window reboot interval
	 * @return status-True- Device rebooted successfully during maintenance
	 *         window,Else-False
	 */
	public static boolean verifyDeviceRebootDuringMaintenanceWindow(Dut device, AutomaticsTapApi tapEnv,
			Map<String, String> maintenanceWindow) {
		LOGGER.debug("STARTING METHOD: VerifyDeviceRebootDuringMaintenanceWindow");
		boolean status = false;
		long presentTime = getCurrentLocalTime(device, tapEnv);
		long waitTime = 0L;
		long startTime = 0L;
		long endTime = 0L;
		try {
			startTime = Long.parseLong(maintenanceWindow.get("main_window_start_time"));
			endTime = Long.parseLong(maintenanceWindow.get("main_window_end_time"));
		} catch (NumberFormatException e) {
			LOGGER.info("Exception occurred while parsing the manitenance window start time and end time: "
					+ e.getMessage());
		}
		try {
			if (presentTime < startTime) {
				waitTime = startTime - presentTime;
				LOGGER.info("Waiting " + (waitTime) + " seconds or "
						+ BroadBandCommonUtils.convertSecondsToHourMinuteSeconds(String.valueOf(waitTime))
						+ " for the Maintenace window to start");
				tapEnv.waitTill(waitTime * BroadBandTestConstants.ONE_SECOND_IN_MILLIS);
			}
			LOGGER.info("Device will reboot any time inbetween Miantenance Window start and end time");
			LOGGER.info(tapEnv.executeCommandUsingSsh(device,
					BroadBandTestConstants.CAT_COMMAND + BroadBandTestConstants.RDKLOGS_LOGS_XCONF_TXT_0));
			long currentTime = getCurrentLocalTime(device, tapEnv);
			long diffBetweenEndAndCurrTime = endTime - getCurrentLocalTime(device, tapEnv);
			do {
				status = !CommonMethods.isSTBAccessible(device);
			} while ((getCurrentLocalTime(device, tapEnv) - currentTime) < diffBetweenEndAndCurrTime && !status
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
		} catch (Exception e) {
			LOGGER.info("Exception occurred while reboot verification during maintenance window : " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: VerifyDeviceRebootDuringMaintenanceWindow");
		return status;
	}

	/**
	 * Method to remove file/folder
	 * 
	 * @param tapEnv           The AutomaticsTapApi.
	 * @param device           The {@link Dut} object.
	 * @param completeFilePath The Complete file path
	 * @return The validation result
	 * @author Govardhan
	 */
	public static boolean removeFileandVerifyFileRemoval(AutomaticsTapApi tapEnv, Dut device, String completeFilePath) {
		boolean result = false;
		String commandToRemoveFile = RDKBTestConstants.CMD_REMOVE_DIR_FORCEFULLY
				+ RDKBTestConstants.SINGLE_SPACE_CHARACTER + completeFilePath;

		String response = tapEnv.executeCommandUsingSsh(device, commandToRemoveFile);
		LOGGER.info("Repsonse on executing the command to remove file/folder: " + response);

		result = !(CommonUtils.isFileExists(device, tapEnv, completeFilePath));
		LOGGER.info("Status of removing " + completeFilePath + "is : " + result);

		return result;
	}

	/**
	 * Method to check Test Command
	 * 
	 * @return boolean
	 */
	public static boolean executeTestCommand(AutomaticsTapApi tapEnv, Dut device) {
		boolean isSTBAccessible = true;
		String commandResponse = null;
		LOGGER.info("Going to verify whether the stb is accessible using the IP address");

		commandResponse = tapEnv.executeCommandUsingSsh(device, "echo test_connection");
		LOGGER.info("Test Coammnd Response is : " + commandResponse);
		if (CommonMethods.isNull(commandResponse) || (commandResponse.indexOf("test_connection") == -1)) {
			LOGGER.error(
					new StringBuilder().append("\n*************************************\n UNABLE TO ACCESS THE STB (")
							.append(device.getHostMacAddress()).append(") USING THE IP ")
							.append(((Device) device).getHostIpAddress())
							.append("\n*************************************").toString());
			isSTBAccessible = false;
		}
		return isSTBAccessible;
	}

	/**
	 * Method to check whether the STB is accessible. This is cross verified by
	 * sending an echo test_connection command and see if we get the response.
	 * 
	 * @param tapApi       instance of {@link AutomaticsTapApi}
	 * @param device       instance of {@link Dut}
	 * @param delay        delay in between the retry
	 * @param maxLoopCount maximum loop count
	 * @return true if STB is accessible else false
	 */
	public static boolean isSTBAccessible(AutomaticsTapApi tapApi, Dut device, long delay, int maxLoopCount) {
		// STB accessible status
		boolean status = false;
		for (int index = 1; index <= maxLoopCount; index++) {
			LOGGER.info(index + "/" + maxLoopCount + "# verify whether the device is accessible or not");
			// verifying whether device is accessible or not
			if (executeTestCommand(tapApi, device)) {
				status = true;
				break;
			}

			if (delay > 0) {
				LOGGER.info("Waiting " + (delay / 1000) + " seconds to device up");
			}

			tapApi.waitTill(delay);
		}
		LOGGER.info("Exiting method isSTBAccessible. Status - " + status);
		return status;
	}

	/**
	 * Utility method to validate the currently running firmware version using WebPA
	 * command.
	 * 
	 * @param device     The device to be validated.
	 * @param buildImage The firmware version to be validated.
	 * @return True if given image name matches with WebPA response.
	 * @author Govardhan
	 */
	public static boolean verifyCurrentlyRunningImageVersionUsingWebPaCommand(AutomaticsTapApi tapEnv, Dut device,
			String buildImage) {
		return verifyCurrentlyRunningImageVersionUsingWebPaCommand(tapEnv, device, buildImage, false);
	}

	/**
	 * Utility method to validate the currently running firmware version using WebPA
	 * command.
	 * 
	 * @param device             The Dut to be validated.
	 * @param buildImage         The firmware version to be validated.
	 * @param convertToUpperCase true if both needs to converted to upper case and
	 *                           verified
	 * @return True if given image name matches with WebPA response.
	 * @author Praveenkumar Paneerselvam
	 * @refactor Govardhan
	 */
	public static boolean verifyCurrentlyRunningImageVersionUsingWebPaCommand(AutomaticsTapApi tapEnv, Dut device,
			String buildImage, boolean convertToUpperCase) {
		LOGGER.debug("STARTING METHOD: verifyCurrentlyRunningImageVersionUsingWebPaCommand()");
		boolean currentFirmwareVersionStatus = false;
		String imageName = null;
		long startTime = System.currentTimeMillis();
		do {
			imageName = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandTestConstants.TR69_RDKB_CURRENT_IMAGE_NAME);
		} while (CommonMethods.isNull(imageName)
				&& (System.currentTimeMillis() - startTime) < BroadBandTestConstants.TEN_MINUTE_IN_MILLIS
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.TEN_SECOND_IN_MILLIS));

		LOGGER.info("Current image name - " + imageName);
		LOGGER.info("Image name to be verified - " + buildImage);
		if (CommonMethods.isNotNull(imageName)) {
			buildImage = StringUtils
					.replaceEach(buildImage, RDKBTestConstants.IMAGE_VERSION, BroadBandTestConstants.REPLACE).trim();
			LOGGER.info("Image name to be verified after replacement - " + buildImage);
			currentFirmwareVersionStatus = buildImage.trim().equalsIgnoreCase(imageName.trim());
		}
		LOGGER.debug("ENDING METHOD: verifyCurrentlyRunningImageVersionUsingWebPaCommand()");
		return currentFirmwareVersionStatus;
	}

	/**
	 * Method to perform reboot and verify the device is accessible without
	 * verifying the processes are up
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @return true if device is up after reboot
	 * @refactor Govardhan
	 */
	public static boolean rebootAndWaitForStbAccessible(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.info("STARTING METHOD: rebootAndWaitForStbAccessible()");
		boolean isStbAccessible = false;
		String errorMessage = "Failed to perform reboot";
		tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_TO_REBOOT_DEVICE);
		long startTime = System.currentTimeMillis();
		try {
			errorMessage = "Failed to verify StbAccessible";
			startTime = System.currentTimeMillis();
			do {
				isStbAccessible = CommonMethods.isSTBAccessible(device);
			} while (!isStbAccessible
					&& ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.TEN_MINUTE_IN_MILLIS)
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
			LOGGER.info("Device is accessible after reboot :" + isStbAccessible);
		} catch (Exception exception) {
			errorMessage = "Exception occured while performing reboot " + exception.getMessage();
			LOGGER.error(errorMessage);
		}
		LOGGER.info("ENDING METHOD: rebootAndWaitForStbAccessible()");
		return isStbAccessible;
	}

	/**
	 * Utility Method to get uptime for each device
	 * 
	 * @param device {@link Dut}
	 * @author Govardhan
	 */
	public static String getWebpaReadyResponse(Dut device) {
		String readyResponse = null;
		String propertyKey = "webpa.ready.";
		readyResponse = getAutomaticsPropsValueByResolvingPlatform(device, propertyKey);
		LOGGER.info("Webpa Ready Response time is : " + readyResponse);
		return readyResponse;
	}

	/**
	 * Utility Method to get connect time for each device
	 * 
	 * @param device {@link Dut}
	 * @author Govardhan
	 */
	public static String getParodusConnectTime(Dut device) {
		String connectTime = null;
		String propertyKey = "parodus.connect.";
		connectTime = getAutomaticsPropsValueByResolvingPlatform(device, propertyKey);
		LOGGER.info("Parodus Connect Time is : " + connectTime);
		return connectTime;
	}

	/**
	 * Utility method to obtain epoch time value from log message
	 * 
	 * @param logMessage string containing log message to obtain UTC time from
	 * 
	 * @return long value of GMT epoch time from log message
	 * 
	 * @author Ashwin sankara
	 * @refactor Govardhan
	 */
	public static long getGMTEpochTimeFromUTCLogMessage(String logMessage) throws Exception {
		LOGGER.debug("Entering method: getGMTEpochTimeFromUTCLogMessage");
		long epochTime = BroadBandTestConstants.CONSTANT_0;
		String timeStamp = null;
		if (CommonMethods.isNotNull(logMessage)) {
			timeStamp = CommonMethods.patternFinder(logMessage,
					BroadBandTestConstants.PATTERN_MATCHER_TIMESTAMP_SPEEDTEST_LOG_MESSAGE);
			if (CommonMethods.isNotNull(timeStamp)) {
				timeStamp = concatStringUsingStringBuffer(timeStamp, BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
						BroadBandTestConstants.TIMEZONE_UTC);
				epochTime = new SimpleDateFormat(BroadBandTestConstants.TIMESTAMP_FORMAT_SPEEDTEST_LOG_MESSAGE)
						.parse(timeStamp).getTime() / BroadBandTestConstants.CONSTANT_1000;
				LOGGER.info("Epoch time from timestamp in log message: " + epochTime);
			}
		} else {
			throw new TestException("Log message is null");
		}
		LOGGER.debug("Exiting method: getGMTEpochTimeFromUTCLogMessage");
		return epochTime;
	}

	/**
	 * Utility Method to get connect time for each device
	 * 
	 * @param device {@link Dut}
	 * @author Govardhan
	 */
	public static String getDeviceManufacturerName(Dut device) {
		String manufacturerName = null;
		String propertyKey = "device.manufacturer.name.";
		manufacturerName = getAutomaticsPropsValueByResolvingPlatform(device, propertyKey);
		LOGGER.info("Manufacturer Name is : " + manufacturerName);
		return manufacturerName;
	}

	/**
	 * Utility method to retrieve the device status {@link BroadBandDeviceStatus }
	 * via WebPA or dmcli.
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @return The {@link BroadBandDeviceStatus}
	 * @refactor Govardhan
	 */
	public static BroadBandDeviceStatus getDeviceStatusViaWebPaOrDmcli(Dut device, AutomaticsTapApi tapEnv,
			boolean isCiquickTest) {

		BroadBandDeviceStatus deviceStatus = new BroadBandDeviceStatus();

		String[] parameters = null;

		if (DeviceModeHandler.isDSLDevice(device)) {
			parameters = BroadBandWebPaUtils.retrieveDeviceStatusUsingWebPaParameterForDSLDevices(device,
					isCiquickTest);
		} else {
			parameters = BroadBandWebPaUtils.retrieveDeviceStatusUsingWebPaParameterBasedOnSupportedModels(device,
					isCiquickTest);
		}

		Map<String, String> deviceStatusResponse = BroadBandWebPaUtils
				.getMultipleParameterValuesUsingWebPaOrDmcli(device, tapEnv, parameters);

		for (String param : deviceStatusResponse.keySet()) {

			String paramValue = deviceStatusResponse.get(param);

			boolean status = false;

			LOGGER.debug("Parameter name : " + param + " , value : " + paramValue);

			switch (param) {

			case BroadBandWebPaConstants.WEBPA_COMMAND_MTA_IP_OF_DEVICE:
				if (CommonMethods.isNotNull(paramValue)) {
					String mtaIpAddress = CommonMethods.patternFinder(paramValue,
							BroadBandTestConstants.PATTERN_TO_GET_IPV4_ADDRESS);
					status = CommonMethods.isNotNull(mtaIpAddress)
							&& !mtaIpAddress.equals(BroadBandTestConstants.STRING_NULL_IP);
				}
				deviceStatus.setValidMtaIpAddress(status);
				break;
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_AKER_ENABLE:
				if (CommonMethods.isNotNull(paramValue)) {
					status = Boolean.parseBoolean(paramValue.trim());
				}
				deviceStatus.setAkerEnabled(status);
				break;
			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_FINGER_PRINT_ENABLE:
				if (CommonMethods.isNotNull(paramValue)) {
					status = Boolean.parseBoolean(paramValue.trim());
				}
				deviceStatus.setAdvanceSecurityStatus(status);
				break;

			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_2_4GHZ_PRIVATE_SSID:

				if (CommonMethods.isNotNull(paramValue)) {
					status = paramValue.equalsIgnoreCase("Up");
				}
				deviceStatus.setPrivateWiFi2GhzStatus(status);
				break;

			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_5GHZ_PRIVATE_SSID:
				if (CommonMethods.isNotNull(paramValue)) {
					status = paramValue.equalsIgnoreCase("Up");
				}
				deviceStatus.setPrivateWiFi5GhzStatus(status);
				break;

			case BroadBandWebPaConstants.WEBPA_PARAM_BRIDGE_MODE_STATUS:

				if (CommonMethods.isNotNull(paramValue)) {
					status = paramValue.equalsIgnoreCase(BroadBandTestConstants.LAN_MANAGEMENT_MODE_ROUTER);
				}
				deviceStatus.setRouterModeStatus(status);
				break;

			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_MESH_STATUS:
				if (CommonMethods.isNotNull(paramValue)) {
					status = paramValue.equalsIgnoreCase(BroadBandTestConstants.MESHWIFI_STATE_FULL);
				}

				deviceStatus.setMeshStatus(status);
				break;
			case "Device.WiFi.SSID.10003.Status":
				if (CommonMethods.isNotNull(paramValue)) {
					status = paramValue.equalsIgnoreCase("Up");
				}

				deviceStatus.setPublicWiFi2GhzOpenStatus(status);
				break;

			case "Device.WiFi.SSID.10103.Status":
				if (CommonMethods.isNotNull(paramValue)) {
					status = paramValue.equalsIgnoreCase("Up");
				}

				deviceStatus.setPublicWiFi5GhzOpenStatus(status);

				break;

			case "Device.WiFi.SSID.10105.Status":
				if (CommonMethods.isNotNull(paramValue)) {
					status = paramValue.equalsIgnoreCase("Up");
				}

				deviceStatus.setPublicWiFi5GhzSecureStatus(status);

				break;

			case BroadBandWebPaConstants.WEBPA_PARAM_FOR_MOCA_INTERFACE_ENABLE:
				if (CommonMethods.isNotNull(paramValue)) {
					status = Boolean.parseBoolean(paramValue.trim());
				}
				deviceStatus.setMocaEnable(status);

				break;

			case BroadBandWebPaConstants.WEBPA_PARAM_GET_MOCA_STATUS:
				if (CommonMethods.isNotNull(paramValue)) {
					status = paramValue.equalsIgnoreCase("Up");
				}
				deviceStatus.setMocaStatus(status);

				break;
			// 2.4 GHz Radio channel number.
			case BroadBandWebPaConstants.WEBPA_WAREHOUSE_WIFI_2G_CHANNEL:
				deviceStatus.setRadio2gChanNum(paramValue);
				break;
			// 5 GHz Radio channel number.
			case BroadBandWebPaConstants.WEBPA_WAREHOUSE_WIFI_5G_CHANNEL:
				deviceStatus.setRadio5gChanNum(paramValue);
				break;
			// 2.4 GHz Auto channel status.
			case BroadBandWebPaConstants.WEBPA_WAREHOUSE_WIFI_2G_WIRELESS_CHANNEL:
				if (CommonMethods.isNotNull(paramValue)) {
					status = Boolean.parseBoolean(paramValue.trim());
				}
				deviceStatus.setRadio2gAutoChanStatus(status);

				break;
			// 5 GHz Auto channel status.
			case BroadBandWebPaConstants.WEBPA_WAREHOUSE_WIFI_5G_WIRELESS_CHANNEL:
				if (CommonMethods.isNotNull(paramValue)) {
					status = Boolean.parseBoolean(paramValue.trim());
				}
				deviceStatus.setRadio5gAutoChanStatus(status);

				break;

			case BroadBandWebPaConstants.WEBPA_PARAM_TO_GET_XDNS_FEATURE_STATUS:
				if (CommonMethods.isNotNull(paramValue)) {
					status = Boolean.parseBoolean(paramValue.trim());
				}
				deviceStatus.setXdnsEnabled(status);

				break;

			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_CLOUD_UI:
				if (CommonMethods.isNotNull(paramValue)) {
					status = Boolean.parseBoolean(paramValue.trim());
				}
				deviceStatus.setCloudUiEnabled(status);
				break;

			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_WPS_STATUS_2_4:
				if (CommonMethods.isNotNull(paramValue)) {
					status = Boolean.parseBoolean(paramValue.trim());
				}
				deviceStatus.setWps2gEnabled(status);
				break;

			case BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_WPS_STATUS_5:
				if (CommonMethods.isNotNull(paramValue)) {
					status = Boolean.parseBoolean(paramValue.trim());
				}
				deviceStatus.setWps5gEnabled(status);
				break;

			case BroadBandWebPaConstants.WEBPA_PARAM_CODEBIG_FIRST_ENABLE:
				if (CommonMethods.isNotNull(paramValue)) {
					status = Boolean.parseBoolean(paramValue.trim());
				}
				deviceStatus.setCodeBigEnabled(status);
				break;
			default:
				break;
			}
		}
		LOGGER.info(" BroadBandDeviceStatus :  " + deviceStatus.toString());
		return deviceStatus;
	}

	/**
	 * Utility method to validate all required processes in ATOM side.
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @return The {@link BroadBandResultObject }
	 * @refactor Govardhan
	 */
	public static BroadBandResultObject validateAllRequiredProcessOnAtomSideForQt(AutomaticsTapApi tapEnv, Dut device) {
		String commandAtomResponse = CommonMethods.executeCommandInAtomConsole(device, tapEnv,
				BroadBandTestConstants.CMD_GET_PROCESS_DETAILS);
		String processListKey = "atom.process.list.qt.";
		String processList = null;
		try {
			processList = getAutomaticsPropsValueByResolvingPlatform(device, processListKey);
		} catch (Exception e) {
			LOGGER.info("Process List not configured for the Device. Proceeding with default Process List");
			processList = AutomaticsPropertyUtility.getProperty(processListKey);
		}
		BroadBandResultObject broadBandResultObject = validateAllProcessesDefinedInProperties(device, tapEnv,
				commandAtomResponse, processList);
		return broadBandResultObject;
	}

	/**
	 * This method will print the diagnostic information.
	 * 
	 * @param tapEnv     {@link AutomaticsTapApi}
	 * @param device     {@link Dut}
	 * @param testCaseId test case ID
	 * @param stepNumber step number
	 * @refactor Govardhan
	 */
	public static void printDiagnosticInformationOnFailureSteps(Dut device, AutomaticsTapApi tapEnv, String testCaseId,
			String stepNumber) {
		try {
			// get the CPU usage
			String cpuUsage = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.CMD_GET_CPU_USAGE);
			LOGGER.info("\n************CPU USAGE information during execution of test Id " + testCaseId + " step "
					+ stepNumber + "************** \n" + cpuUsage);
			// get the memory usage
			String memoryUsage = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.CMD_GET_MEM_USAGE);
			LOGGER.info("\n***********Memory USAGE information  during execution of test Id " + testCaseId + " step "
					+ stepNumber + " **************************\n " + memoryUsage);
			// get the upTime
			String upTimeResponse = tapEnv.executeCommandUsingSsh(device, RDKBTestConstants.CMD_UPTIME);
			LOGGER.info("Device uptime information  at execution of test Id " + testCaseId + " step " + stepNumber
					+ " is \n" + upTimeResponse);
		} catch (Exception e) {
			LOGGER.error("Exception during getting diagnostic information");
		}
	}

	/**
	 * Utility method to set the Device mode to 'router' using WebPA or dmcli and
	 * wait for 5 minutes to come up with router mode.
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @return true if device mode changed to 'router'
	 * @refactor Govardhan
	 */
	public static boolean setDeviceInRouterModeStatusUsingWebPaOrDmcli(Dut device, AutomaticsTapApi tapEnv) {
		boolean lanModeChange = BroadBandWebPaUtils.setParameterValuesUsingWebPaOrDmcli(device, tapEnv,
				BroadBandWebPaConstants.WEBPA_PARAM_BRIDGE_MODE_STATUS, WebPaDataTypes.STRING.getValue(),
				BroadBandTestConstants.LAN_MANAGEMENT_MODE_ROUTER);
		if (lanModeChange) {
			tapEnv.waitTill(BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS);
		}
		return lanModeChange;
	}

	/**
	 * Utility method to calculate Broadband device firmware upgrade maintenance
	 * window(start time and end time) based on current device local time.
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @return Maintenance window details.
	 * @refactor Govardhan
	 */
	public static Map<String, String> calculateFirmwareUpgradeMaintenanceWindow(AutomaticsTapApi tapEnv, Dut device) {
		Map<String, String> maintenanceWindow = new HashMap<String, String>();

		String firmwareUpgradeStartTime = BroadBandTestConstants.DEFAULT_FIRMWARE_UPGRADE_MAINTENANCE_WINDOW_START_TIME;
		String firmwareUpgradeEndTime = BroadBandTestConstants.DEFAULT_FIRMWARE_UPGRADE_MAINTENANCE_WINDOW_END_TIME;

		try {
			String currentDeviceLocalTime = tapEnv.executeCommandUsingSsh(device, "LTime");
			if (CommonMethods.isNotNull(currentDeviceLocalTime)) {
				String[] splitHourMinSec = currentDeviceLocalTime.split(":");
				if (splitHourMinSec.length == 3) {
					long currentTimeInSeconds = Integer.parseInt(splitHourMinSec[0].trim()) * 60 * 60
							+ Integer.parseInt(splitHourMinSec[1].trim()) * 60
							+ Integer.parseInt(splitHourMinSec[2].trim());

					long startTime = 0L;
					long endTime = 0L;

					/*
					 * Maintenance window is calculated and configured as 0 - 86400( 24 hour format)
					 * and reset again to 0 at midnight. If current time is between 12 AM(00:00:00)
					 * and 1AM (01:00:00). Maintenance window start time is current time and end
					 * time will be current time + 3600 seconds
					 */
					if (currentTimeInSeconds > 0 && currentTimeInSeconds <= 3600) {
						startTime = currentTimeInSeconds;
						endTime = currentTimeInSeconds + 60 * 60;
						/*
						 * If maintenance window time is between 84600(23:30:00) and 86399(23:59:59).
						 * Calculate the upgrade end time from 0 instead of adding 3600 to current time
						 * which exceed the 86399.
						 */
					} else if (currentTimeInSeconds >= 84600 && currentTimeInSeconds <= 86399) {
						startTime = currentTimeInSeconds - 120;
						endTime = 60 * 60;
						/*
						 * Other cases, calculate maintenance window time like -> start time = current
						 * time - 120 seconds end time = current time + 3600 seconds.
						 */
					} else {
						startTime = currentTimeInSeconds - 120;
						endTime = currentTimeInSeconds + 60 * 60;
					}

					firmwareUpgradeStartTime = Long.toString(startTime);
					firmwareUpgradeEndTime = Long.toString(endTime);
					LOGGER.info("Calculated Maintenance Window =>  Start Time : " + firmwareUpgradeStartTime
							+ ", End Time : " + firmwareUpgradeEndTime + " , Current Time : " + currentTimeInSeconds);
				}
			}
			maintenanceWindow.put("device_local_time", currentDeviceLocalTime);
		} catch (Exception e) {
			LOGGER.error(
					"Exception occurred while fetching the current time or calculating the maintenance window, resetting to default value");
		}
		maintenanceWindow.put("main_window_start_time", firmwareUpgradeStartTime);
		maintenanceWindow.put("main_window_end_time", firmwareUpgradeEndTime);

		return maintenanceWindow;
	}

	/**
	 * Compares list string with the limit value
	 * 
	 * 
	 * @param match      list to match
	 * @param limitValue limit value
	 * @return true is it satisfies the limit value
	 * @Refactor Alan_Bivera
	 */
	public static boolean compareListWithLimitValue(List<String> match, int limitValue) {

		boolean status = false;

		for (int i = 0; i < match.size(); i++) {
			int matchList = Integer.parseInt(match.get(i));
			if (matchList >= limitValue) {
				status = true;
			} else {
				status = false;
				LOGGER.error("List is not within the given limit, Expected >= " + limitValue + " Actual " + matchList);
				break;
			}
		}
		LOGGER.info("Status of list within the given limit " + status);
		return status;
	}

	/**
	 * Utils method to execute and compare values retrieved from WEBPA and SNMP
	 * 
	 * @param tapEnv     {@link AutomaticsTapApi}
	 * @param device     {@link Dut}
	 * @param tr181Param WebPA Parameter
	 * @param snmpMib    SNMP OID
	 * @return result object with error message and status
	 */
	public static BroadBandResultObject executeAndCompareValuesFromWebpaAndSnmp(AutomaticsTapApi tapEnv, Dut device,
			String tr181Param, BroadBandSnmpMib snmpMib) {

		BroadBandResultObject result = new BroadBandResultObject();
		boolean status = false;
		String valueFromWebpa = null;
		String valueFromSnmp = null;
		String errorMessage = null;

		try {
			errorMessage = "Unable to retrieve value for param '" + tr181Param + "' from WebPA";
			valueFromWebpa = tapEnv.executeWebPaCommand(device, tr181Param);
			if (CommonMethods.isNotNull(valueFromWebpa)) {
				errorMessage = "Unable to retrieve value for OID '" + snmpMib.getOid() + "' from SNMP";
				if (CommonMethods.isNotNull(snmpMib.getTableIndex())) {
					valueFromSnmp = BroadBandSnmpUtils.executeSnmpGetWithTableIndexOnRdkDevices(tapEnv, device,
							snmpMib.getOid(), snmpMib.getTableIndex());
				} else {
					valueFromSnmp = BroadBandSnmpUtils.executeSnmpGetOnRdkDevices(tapEnv, device, snmpMib.getOid());
				}
				if (CommonMethods.isNotNull(valueFromSnmp)) {
					status = BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
							valueFromWebpa, valueFromSnmp);
					errorMessage = "Value retrieved from WEBPA and SNMP do not match. Value from WEBPA : "
							+ valueFromWebpa + "; Value from SNMP : " + valueFromSnmp;
				}
			}
		} catch (Exception e) {
			LOGGER.error("Execption occured while retrieving values from WEBPA/SNMP :" + e.getMessage());
			errorMessage = e.getMessage();
		}

		result.setErrorMessage(errorMessage);
		result.setStatus(status);

		return result;

	}

	/**
	 * Utility method to verify a file is encrypted. It uses the command: cat
	 * <fileName>
	 * 
	 * @param tapEnv   {@link AutomaticsTapApi}
	 * @param device   {@link Dut}
	 * @param fileName String representing the Library Name
	 * 
	 * @return Boolean representing the result of the validation.
	 * @author ArunKumar Jayachandran
	 * @refactor Govardhan
	 */
	public static boolean isFileEncrypted(AutomaticsTapApi tapEnv, Dut device, String fileName, boolean isAtomConsole) {
		LOGGER.debug("STARTING METHOD: isFileEncrypted");
		boolean result = false;
		String command = concatStringUsingStringBuffer(BroadBandTestConstants.CAT_COMMAND, fileName);
		String response = (isAtomConsole ? CommonMethods.executeCommandInAtomConsole(device, tapEnv, command)
				: tapEnv.executeCommandUsingSsh(device, command));
		result = CommonMethods.isNotNull(response)
				&& (CommonMethods.patternMatcher(BroadBandTestConstants.PATTERN_TO_MATCH_LETTERS_AND_DIGITS, response));
		LOGGER.info("FILE " + fileName + " is Encrypted: " + !result);
		LOGGER.debug("ENDING METHOD: isFileEncrypted");
		return !result;
	}

	/**
	 * Method to execute command in arm console or console log
	 * 
	 * @param tapEnv     {@link AutomaticsTapApi}
	 * @param device     {@link Dut}
	 * @param searchText searchText to execute in device
	 * @author ArunKumar Jayachandran
	 * @refactor Govardhan
	 */
	public static String searchLogsInArmConsoleOrConsole(Dut device, AutomaticsTapApi tapEnv, String searchText) {
		LOGGER.debug("STARTING METHOD: searchLogsInArmConsoleOrConsole()");
		String response = null;
		String command = null;
		command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND, searchText,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER);
		command = (CommonMethods.isAtomSyncAvailable(device, tapEnv)
				? BroadBandCommonUtils.concatStringUsingStringBuffer(command,
						BroadBandTestConstants.RDKLOGS_LOGS_ARM_CONSOLE_0)
				: BroadBandCommonUtils.concatStringUsingStringBuffer(command,
						BroadBandTestConstants.RDKLOGS_LOGS_CONSOLE_TXT_0));
		LOGGER.info("Command to be executed on device is: " + command);
		response = tapEnv.executeCommandUsingSsh(device, command);
		LOGGER.info("Response from the device is: " + response);
		LOGGER.debug("ENDING METHOD: searchLogsInArmConsoleOrConsole()");
		return response;
	}

	/**
	 * Method to compare serial number with value of serial number parameter
	 * 
	 * @param tapEnv       {@link AutomaticsTapApi}
	 * @param device       {@link Dut}
	 * @param serialNumber serial number to be compared with parameter
	 * 
	 * @return true if value is same as tr181 parameter else false
	 */
	public static boolean compareSerialNumberWithParameterValue(AutomaticsTapApi tapEnv, Dut device,
			String serialNumber) {
		LOGGER.debug("Entering method: compareSerialNumberWithParameterValue");
		boolean result = false;
		String response = null;
		response = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
				BroadBandWebPaConstants.TR69_PARAM_SERIAL_NUMBER);
		LOGGER.info("Serial number obtained from parameter :" + response);
		if (CommonMethods.isNotNull(response)) {
			result = response.equalsIgnoreCase(serialNumber);
		} else {
			LOGGER.error("Serial number from WebPA parameter is NULL");
		}
		LOGGER.debug("Exiting method: compareSerialNumberWithParameterValue");
		return result;
	}

	/**
	 * Method to compare serial number with value of serial number parameter
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @param ecmMac mac to be compared with parameter
	 * 
	 * @return true if value is same as tr181 parameter else false
	 * @refactor Govardhan
	 */
	public static boolean compareCMMacWithParameterValue(AutomaticsTapApi tapEnv, Dut device, String cmMac) {
		LOGGER.debug("Entering method: compareCMMacWithParameterValue");
		boolean result = false;
		String response = null;
		response = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
				BroadBandWebPaConstants.WEBPA_PARAM_CM_MAC);
		LOGGER.info("CM mac obtained from parameter :" + response);
		if (CommonMethods.isNotNull(response) && CommonMethods.isNotNull(cmMac)) {
			response = response.replaceAll(BroadBandTestConstants.DELIMITER_COLON, BroadBandTestConstants.EMPTY_STRING);
			cmMac = cmMac.replaceAll(BroadBandTestConstants.DELIMITER_COLON, BroadBandTestConstants.EMPTY_STRING);
			result = response.equalsIgnoreCase(cmMac);
		} else {
			LOGGER.error("CM mac value is NULL");
		}
		LOGGER.debug("Exiting method: compareCMMacWithParameterValue");
		return result;
	}

	/**
	 * Method to execute command in Atom console
	 * 
	 * @param tapEnv  {@link AutomaticsTapApi}
	 * @param device  {@link Dut}
	 * @param command command to execute
	 * @return server response
	 * @refactor Govardhan
	 */
	public static String executeCommandInAtomConsole(Dut device, AutomaticsTapApi tapEnv, String command)
			throws TestException {

		LOGGER.info("STARTING METHOD: executeCommandInAtomConsole()");
		// Variable to hold the server response
		String response = tapEnv.executeCommandOnAtom(device, command);
		LOGGER.info("Response from atom console: " + response);
		LOGGER.info("ENDING METHOD: executeCommandInAtomConsole()");
		return response;
	}

	/**
	 * Method to get PID of process from Atom console
	 * 
	 * @param tapEnv      {@link AutomaticsTapApi}
	 * @param device      {@link Dut}
	 * @param processName String representing the process name
	 * 
	 * @return PID of given process
	 * @refactor Govardhan
	 */
	public static String getPidOfProcessFromAtomConsole(Dut device, AutomaticsTapApi tapEnv, String processName) {
		LOGGER.debug("ENTERING METHOD getPidOfProcessFromAtomConsole");
		String processId = null;
		String response = null;
		try {
			response = executeCommandInAtomConsole(device, tapEnv, "pidof " + processName);
			processId = CommonMethods.patternFinder(response, "(\\d+)");
			if (CommonMethods.isNotNull(processId)) {
				processId = processId.trim();
				// validating whether PID is integer or not
				Integer.parseInt(processId);
			}
		} catch (Exception exception) {
			LOGGER.error("EXCEPTION OCCURRED WHILE TRYING TO GET THE PROCESS ID: " + exception.getMessage());
		}
		LOGGER.info("PROCESS ID: " + processId);
		LOGGER.debug("ENDING METHOD getPidOfProcessFromAtomConsole");
		return processId;
	}

	/**
	 * Helper method to search text in log file with polling method
	 * 
	 * @param tapEnv       {@link AutomaticsTapApi}
	 * @param device       {@link Dut}
	 * @param searchText   search text message
	 * @param logFile      log file location
	 * @param pollDuration maximum poll duration
	 * @param pollInterval polling interval
	 * @return searchResponse response for search text
	 * @author ArunKumar Jayachandran
	 */
	public static String searchLogFilesInAtomConsoleByPolling(AutomaticsTapApi tapEnv, Dut device, String searchText,
			String logFile, long pollDuration, long pollInterval) {
		LOGGER.debug("STARTING METHOD searchLogFilesInAtomConsoleByPolling");
		String command = BroadBandTestConstants.GREP_COMMAND;
		// In case the search text contains space and not wrapped with double quotes.
		if (CommonUtils.isGivenStringAvailableInCommandOutput(searchText, BroadBandTestConstants.SINGLE_SPACE_CHARACTER)
				&& !CommonUtils.isGivenStringAvailableInCommandOutput(searchText,
						BroadBandTestConstants.DOUBLE_QUOTE)) {
			command = BroadBandCommonUtils.concatStringUsingStringBuffer(command, BroadBandTestConstants.DOUBLE_QUOTE,
					searchText, BroadBandTestConstants.DOUBLE_QUOTE);
		} else {
			command = BroadBandCommonUtils.concatStringUsingStringBuffer(command, searchText);
		}
		command = BroadBandCommonUtils.concatStringUsingStringBuffer(command,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, logFile);
		LOGGER.info("COMMAND TO BE EXECUTED: " + command);
		long startTime = System.currentTimeMillis();
		String searchResponse = null;
		do {
			tapEnv.waitTill(pollInterval);
			searchResponse = BroadBandCommonUtils.executeCommandInAtomConsole(device, tapEnv, command);
			searchResponse = CommonMethods.isNotNull(searchResponse)
					&& !searchResponse.contains(BroadBandTestConstants.NO_SUCH_FILE_OR_DIRECTORY)
							? searchResponse.trim()
							: null;
		} while ((System.currentTimeMillis() - startTime) < pollDuration && CommonMethods.isNull(searchResponse));

		LOGGER.info(
				"SEARCH RESPONSE FOR - " + searchText + " IN THE LOG FILE - " + logFile + " IS : " + searchResponse);
		LOGGER.debug("ENDING METHOD searchLogFilesInAtomConsoleByPolling");
		return searchResponse;
	}

	/**
	 * Utility Method to kill a process given the process name in Atom Console.
	 * 
	 * @param tapEnv      {@link AutomaticsTapApi}
	 * @param device      {@link Dut}
	 * @param processName String representing the Process Name
	 * 
	 * @return Boolean representing the result of operation.
	 * @refactor Govardhan
	 */
	public static boolean killProcessAndVerifyInAtomConsole(AutomaticsTapApi tapEnv, Dut device, String processName) {
		LOGGER.debug("STARTING METHOD killProcessAndVerifyInAtomConsole");
		boolean result = false;
		try {
			String pidInitial = getPidOfProcessFromAtomConsole(device, tapEnv, processName);
			LOGGER.info("PID (INITIAL) IN ATOM CONSOLE: " + pidInitial);
			if (CommonMethods.isNotNull(pidInitial)) {
				tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
				CommonMethods.executeCommandInAtomConsole(device, tapEnv, BroadBandCommandConstants.KILL_11_PROCESS
						+ BroadBandTestConstants.SINGLE_SPACE_CHARACTER + pidInitial);
				tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
				String pidFinal = getPidOfProcessFromAtomConsole(device, tapEnv, processName);
				LOGGER.info("PID (FINAL) IN ATOM CONSOLE: " + pidFinal);
				result = CommonMethods.isNull(pidFinal) || !pidFinal.equals(pidInitial);
				LOGGER.info("PROCESS " + processName + " KILLED & VERIFIED: " + result);
			} else {
				LOGGER.error("PROCESS " + processName + " IS NOT RUNNING. HENCE NO ATTEMPT TO KILL MADE.");
				result = true;
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while killing Process in Atom Console.");
		}
		LOGGER.info("ENDING METHOD killProcessAndVerifyInAtomConsole");
		return result;
	}

	/**
	 * Utility method to verify the logs in Atom or Arm Console with pattern matcher
	 * 
	 * @param device         Instance of{@link Dut}
	 * @param tapEnv         instance of {@link AutomaticsTapApi}
	 * @param searchText     Text to search
	 * @param logFileName    Log file name
	 * @param patternToMatch Pattern to match
	 * @param isAtom         True - To verify the logs in Atom console.False-To
	 *                       verify the logs in Arm console
	 * @return True- Expected log is available with pattern matcher.Else False.
	 */
	public static boolean verifyLogsInAtomOrArmWithPatternMatcher(AutomaticsTapApi tapEnv, Dut device,
			String searchText, String logFileName, String patternToMatch, boolean isAtom) {
		boolean status = false;
		String response = null;
		long startTime = System.currentTimeMillis();
		do {
			response = isAtom ? BroadBandCommonUtils.searchAtomConsoleLogs(tapEnv, device, searchText, logFileName)
					: BroadBandCommonUtils.searchLogFiles(tapEnv, device, searchText, logFileName);
			status = CommonMethods.isNotNull(response) && CommonMethods.patternMatcher(response, patternToMatch);
		} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.EIGHT_MINUTE_IN_MILLIS && !status
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.ONE_MINUTE_IN_MILLIS));
		return status;
	}

	/**
	 * Utility method that performs search in ATOM Console & extract only the search
	 * response from the ATOM Console Execution Response.
	 * 
	 * @param tapEnv     {@link AutomaticsTapApi}
	 * @param device     {@link Dut} to be validated
	 * @param searchText String representing the text to be searched.
	 * @param logFile    String representing the log file in which Search to be
	 *                   performed.
	 * 
	 * @return String representing the trimmed Search Response.
	 * @refactor Govardhan
	 */
	public static String searchAtomConsoleLogs(AutomaticsTapApi tapEnv, Dut device, String searchText, String logFile) {
		LOGGER.info("STARTING METHOD searchAtomConsoleLogs");
		String searchCommand = BroadBandTestConstants.GREP_COMMAND + searchText
				+ BroadBandTestConstants.SINGLE_SPACE_CHARACTER + logFile;
		LOGGER.info("SEARCH COMMAND: " + searchCommand);
		String response = CommonMethods.executeCommandInAtomConsole(device, tapEnv, searchCommand);
		LOGGER.info("SEARCH RESPONSE (INITIAL): " + response);
		if (CommonMethods.isNotNull(response)) {
			response = CommonMethods.patternFinder(response,
					BroadBandTestConstants.PATTERN_MATCHER_ATOM_CONSOLE_LOG_SEARCH_RESPONSE);
			response = CommonMethods.isNotNull(response) ? response.trim() : null;
		}
		LOGGER.info("SEARCH RESPONSE FOR TEXT " + searchText + " IN THE FILE " + logFile + " (FINAL): " + response);
		LOGGER.info("ENDING METHOD searchAtomConsoleLogs");
		return response;
	}

	/**
	 * Utility method to verify the expected partner ID is available or not
	 * 
	 * @param partnerIdToVerify Actual partnerID retrieved from script
	 * 
	 * @return Boolean representing the result of the validation.
	 * @author Govardhan
	 */
	public static String getDefaultSSIDPrefixForDeviceAndPartnerSpecific(Dut device, String partnerID) {
		LOGGER.info("STARTING METHOD: getDefaultSSIDPrefixForDeviceAndPartnerSpecific");
		String defaultSSIDPrefix = "";
		defaultSSIDPrefix = BroadbandPropertyFileHandler.getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.PROP_KEY_DEFAULT_SSID_PREFIX + partnerID + ".");
		return defaultSSIDPrefix;
	}

	/**
	 * Utility method to get expectedSSID.
	 * 
	 * @return ExpectedSSID
	 * @author Govardhan
	 */
	public static String getExpectedSSID(Dut device, String partnerID, String prefix, String serialNumber,
			String ecmMac, String wifiBand) {
		String expectedSsid = "";
		try {
			if (BroadbandPropertyFileHandler.getSerialNumber_ECMAC_PatternAfterRemoval(partnerID) != null) {
				LOGGER.info("Serial Number : " + serialNumber);
				expectedSsid = BroadBandCommonUtils.concatStringUsingStringBuffer(prefix,
						serialNumber.substring(serialNumber.length() - BroadBandTestConstants.CONSTANT_5));
			} else {
				expectedSsid = BroadBandCommonUtils.concatStringUsingStringBuffer(prefix,
						BroadBandTestConstants.SYMBOL_HYPHEN,
						ecmMac.substring(ecmMac.length() - RDKBTestConstants.CONSTANT_4));
			}
		} catch (Exception e) {
			expectedSsid = BroadBandCommonUtils.concatStringUsingStringBuffer(prefix,
					BroadBandTestConstants.SYMBOL_HYPHEN,
					ecmMac.substring(ecmMac.length() - RDKBTestConstants.CONSTANT_4));
		}

		try {
			String wifiBandValue = BroadbandPropertyFileHandler.getWifiBandForPartner(device, wifiBand);
			if (wifiBandValue.contains(device.getModel())) {
				expectedSsid = BroadBandCommonUtils.concatStringUsingStringBuffer(expectedSsid,
						BroadBandTestConstants.SYMBOL_HYPHEN, wifiBand);
			}
		} catch (Exception e) {

		}
		return expectedSsid;
	}

	/**
	 * method to compare the values of wifi paramters obtained by webpa command with
	 * the defaults
	 * 
	 * @param Map                of default webpa values obtained
	 * @param RdkBWiFiParameters parameter
	 * 
	 * @return true if parameter value is equal to that of expected default value
	 * @refactor Govardhan
	 */
	public static boolean verifyRdkbWifiParameters(Map<String, String> webPaResponse,
			RdkBWifiParameters parameterToVerify) throws TestException {
		boolean status = false;
		String errorMessage = null;
		String actualValue = null;
		String expectedValue = null;
		if (null != webPaResponse) {

			if (CommonMethods.isNotNull(webPaResponse.get(parameterToVerify.getParameterName()))) {
				actualValue = webPaResponse.get(parameterToVerify.getParameterName());
				expectedValue = parameterToVerify.getDefaultValue();
				LOGGER.info("obtained value for parameter " + parameterToVerify.getParameterName() + "-"
						+ webPaResponse.get(parameterToVerify.getParameterName()));
				LOGGER.info("Expected value for parameter " + parameterToVerify.getParameterName() + "-"
						+ parameterToVerify.getDefaultValue());
				status = actualValue.equalsIgnoreCase(expectedValue);
			} else {
				errorMessage = "Failed to verify the default value of " + parameterToVerify.getParameterName()
						+ "Expected: " + expectedValue + "Actual: null";
				LOGGER.error(errorMessage);
				throw new TestException(errorMessage);
			}
		} else {
			errorMessage = "Failed to verify the default value of " + parameterToVerify.getParameterName()
					+ "Expected: " + expectedValue + "Actual: The WebPA response list is null";
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}

		if (!status) {
			errorMessage = "Failed to verify the default value of " + parameterToVerify.getParameterName()
					+ "Expected: " + expectedValue + "Actual: " + actualValue;
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}
		return status;
	}

	/**
	 * method to compare the values of wifi paramters obtained by webpa command with
	 * the defaults
	 * 
	 * @param Map                of default webpa values obtained
	 * @param RdkBWiFiParameters parameter
	 * 
	 * @return true if parameter value is equal to that of expected default value
	 * @refactor Govardhan
	 */
	public static boolean verifyRdkbWifiParameters(Map<String, String> webPaResponse, JSONObject parameterToVerify)
			throws TestException {
		boolean status = false;
		String errorMessage = null;
		String actualValue = null;
		String expectedValue = null;
		if (null != webPaResponse) {

			try {
				if (CommonMethods.isNotNull(webPaResponse.get(parameterToVerify.getString("name")))) {
					actualValue = webPaResponse.get(parameterToVerify.getString("name"));
					expectedValue = parameterToVerify.getString("value");
					LOGGER.info("obtained value for parameter " + parameterToVerify.getString("name") + "-"
							+ webPaResponse.get(parameterToVerify.getString("name")));
					LOGGER.info("Expected value for parameter " + parameterToVerify.getString("name") + "-"
							+ parameterToVerify.getString("value"));
					status = actualValue.equalsIgnoreCase(expectedValue);
				} else {
					errorMessage = "Failed to verify the default value of " + parameterToVerify.getString("name")
							+ "Expected: " + expectedValue + "Actual: null";
					LOGGER.error(errorMessage);
					throw new TestException(errorMessage);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				errorMessage = "Failed to verify the default value of " + parameterToVerify.getString("name")
						+ "Expected: " + expectedValue + "Actual: The WebPA response list is null";
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}

		if (!status) {
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}
		return status;
	}

	/**
	 * Utility method to verify process running status
	 * 
	 * @param device        {@link Dut}
	 * @param tapEnv        {@link AutomaticsTapApi}
	 * @param isAtomConsole isAtomConsole
	 * @param process       Process to be verified
	 * 
	 * @return true if process is running
	 * @author Seerangarayar Gnanaprakasham
	 */
	public static boolean verifyProcessRunningStatus(Dut device, AutomaticsTapApi tapEnv, boolean isAtomConsole,
			String process) {

		String processId = null;
		if (isAtomConsole) {
			processId = BroadBandCommonUtils.getPidOfProcessFromAtomConsole(device, tapEnv, process);
		} else {
			processId = CommonMethods.getPidOfProcess(device, tapEnv, process);
		}

		if (CommonMethods.isNotNull(processId)
				&& CommonMethods.patternMatcher(processId, BroadBandTestConstants.PATTERN_FOR_PROCESSID)) {
			LOGGER.info("Successfully verified " + process + " process is running after CDL");
			return true;
		} else {
			LOGGER.info(
					"Obtained null response for process check.." + process + " is not running/Initialized properly ");
			return false;
		}

	}

	/**
	 * Utility Method to perform the check on whether file exists. The file check
	 * operation is being performed by polling for the existence of file for 10
	 * minutes.
	 * 
	 * @param tapEnv           {@link AutomaticsTapApi}
	 * @param device           {@link Dut}
	 * @param completeFilePath String representing the complete file path.
	 */
	public static boolean doesFileExistWithinGivenTimeFrameInArm(AutomaticsTapApi tapEnv, Dut device,
			String completeFilePath, long pollDuration, long pollInterval) {
		LOGGER.debug("STARTING METHOD doesFileExistWithinGivenTimeFrameInArm");
		long startTime = System.currentTimeMillis();
		boolean result = false;
		do {
			result = CommonUtils.isFileExists(device, tapEnv, completeFilePath);
			tapEnv.waitTill(pollInterval);
			if (result) {
				break;
			}
		} while ((System.currentTimeMillis() - startTime) < pollDuration && !result);

		LOGGER.info("### VERIFIED THE PRESENCE OF THE FILE: " + completeFilePath);
		LOGGER.debug("ENDING METHOD doesFileExistWithinGivenTimeFrameInArm");
		return result;
	}

	/**
	 * Helper method to kill and verify the given process
	 * 
	 * @param Dut         {@link device}
	 * @param tapEnv      {@link AutomaticsTapApi}
	 * @param processName Name of the Process
	 * @return status . If Its True,If process killed, Else False
	 * @Refactor Athira
	 */
	public static boolean killAndVerifyProcess(Dut device, AutomaticsTapApi tapEnv, String processName) {
		LOGGER.debug("Starting Method : killAndVerifyTcpDumpProcess()");
		boolean status = false;
		String cmdToKillTcpdum = BroadBandCommonUtils.concatStringUsingStringBuffer(
				BroadBandTestConstants.CMD_KILLALL_11, BroadBandTestConstants.SINGLE_SPACE_CHARACTER, processName);
		String processId = CommonMethods.getPidOfProcess(device, tapEnv, processName);
		LOGGER.info("Tcp Dump Process Id : " + processId);
		if (CommonMethods.isNotNull(processId)) {
			tapEnv.executeCommandUsingSsh(device, cmdToKillTcpdum);
			processId = CommonMethods.getPidOfProcess(device, tapEnv, cmdToKillTcpdum);
			status = CommonMethods.isNull(processId);
		}
		LOGGER.debug("Ending Method : killAndVerifyTcpDumpProcess()");
		return status;
	}

	/**
	 * Helper method to verify mount staus for partitions
	 * 
	 * @param response
	 * @param partition
	 * @param pattern
	 * @param permission
	 * @return
	 */
	public static BroadBandResultObject verifyMountStatusAndReadWritePermission(Dut device, AutomaticsTapApi tapEnv,
			String response, String partition, String pattern, String permission) {

		boolean status = false;

		String errorMessage = BroadBandTestConstants.EMPTY_STRING;
		BroadBandResultObject broadBandResultObject = new BroadBandResultObject();

		if (CommonMethods.isNotNull(response)) {

			response = CommonMethods.patternFinder(response, pattern);
			LOGGER.info("Obtained response for " + partition + " partition is : " + response);

			if (CommonMethods.isNotNull(response)) {
				LOGGER.info(partition + " is properly mounted and obtained read-write permission is  : " + response);
				status = response.equalsIgnoreCase(permission);
			}

			if (!status) {
				errorMessage = partition + " is not properly mounted. Obtained response for mount check is : "
						+ response;
				LOGGER.error(errorMessage);
			}

		} else {
			errorMessage = "Failed to get the mount status for " + partition + " using mount | grep " + partition
					+ " command";

		}

		broadBandResultObject.setStatus(status);
		broadBandResultObject.setErrorMessage(errorMessage);
		return broadBandResultObject;
	}

	/**
	 * Utility method to create and verify owner read write permission
	 * 
	 * @param tapEnv    AutomaticsTapApi instance
	 * @param device    instance of Dut
	 * @param dummyTest Test file path
	 * 
	 * @return true if the file is created with the read write permission
	 * 
	 * @author Karthick Pandiyan
	 */

	public static boolean createAndVerifyOwnerReadWritePermission(AutomaticsTapApi tapEnv, Dut device,
			String dummyTest) {
		LOGGER.debug("STARTING METHOD: createAndVerifyOwnerReadWritePermission()");
		boolean status = false;
		String response = null;
		try {
			tapEnv.executeCommandUsingSsh(device,
					BroadBandCommandConstants.CMD_TOUCH + BroadBandTestConstants.SINGLE_SPACE_CHARACTER + dummyTest);

			if (CommonUtils.isFileExists(device, tapEnv, dummyTest)) {

				response = tapEnv.executeCommandUsingSsh(device,
						BroadBandCommonUtils.concatStringUsingStringBuffer(
								BroadBandTestConstants.CMD_TO_LONGLIST_FOLDER_FILES,
								BroadBandTestConstants.SINGLE_SPACE_CHARACTER, dummyTest));

				status = CommonMethods.isNotNull(response)
						? response.trim().substring(1, 3).equals(BroadBandTestConstants.STRING_READ_WRITE_PARTITION)
						: false;
			}
		} catch (Exception e) {
			LOGGER.error("Caught exception while creating and verifying owner read write permission " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: createAndVerifyOwnerReadWritePermission()");
		return status;
	}

	/**
	 * Utility method to remove file and verify status
	 * 
	 * @param tapEnv     AutomaticsTapApi instance
	 * @param device     instance of Dut
	 * @param createFile Test file path
	 * 
	 * @return true if the file is removed successfully
	 * 
	 * @author Karthick Pandiyan
	 */

	public static boolean removeFileAndVerifyStatus(AutomaticsTapApi tapEnv, Dut device, String createFile) {
		LOGGER.debug("STARTING METHOD: removeFileAndVerifyStatus()");
		boolean status = false;
		try {
			String commandToRemoveFile = BroadBandCommonUtils.concatStringUsingStringBuffer(
					BroadBandTestConstants.CMD_REMOVE_DIR_FORCEFULLY, BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
					createFile);
			tapEnv.executeCommandUsingSsh(device, commandToRemoveFile);
			status = !CommonUtils.isFileExists(device, tapEnv, createFile);
		} catch (Exception e) {
			LOGGER.error("Caught exception while removing file and verifying the status " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: removeFileAndVerifyStatus()");
		return status;
	}

	/**
	 * Method to to verify the device is rebooted and it is accessible.
	 * 
	 * @param device Dut Instance
	 * @param tapEnv AutomaticsTapApi Instance
	 * @return true if device is up after webpa reboot
	 * @Refactor Athira
	 */
	public static boolean verifySTBRebootAndStbAccessible(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.info("STARTING METHOD: verifySTBRebootAndStbAccessible()");
		boolean isRebooted = false;
		boolean isStbAccessible = false;
		long startTime = System.currentTimeMillis();
		String errorMessage = "Failed to perform reboot via WEBPA.";
		startTime = System.currentTimeMillis();
		try {
			do {
				isRebooted = CommonUtils.verifyStbRebooted(device, tapEnv);
			} while (!isRebooted
					&& ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS)
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.TEN_SECOND_IN_MILLIS));
			startTime = System.currentTimeMillis();
			LOGGER.info("is rebooted :"+isRebooted);
			do {
				isStbAccessible = CommonMethods.isSTBAccessible(device);
			} while (!isStbAccessible
					&& ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.EIGHT_MINUTE_IN_MILLIS)
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.TEN_SECOND_IN_MILLIS));
			errorMessage = "Failed to verify StbAccessible";
			LOGGER.info("is device Accessible :"+isStbAccessible);
		} catch (Exception exception) {
			errorMessage = "Exception occured while performing reboot " + exception.getMessage();
			LOGGER.error(errorMessage);
		}
		LOGGER.info("ENDING METHOD: verifySTBRebootAndStbAccessible()");
		return isRebooted && isStbAccessible;
	}

	/**
	 * Method to check whether Log file is available in rdklogs/logs and tail them
	 * to Backup<Filename> in nvram in given console type
	 * 
	 * @param Dut                 device instance
	 * @param tapEnv              AutomaticsTapApi instance
	 * @param mustHaveLogFileList List<String> logfiles
	 * @param tailValue           value to grep line
	 * @param polledTime          long polled time
	 * @param maxDuration         long maxtime
	 * @param consoleType         String consoletype
	 * @param backUpPath          String path for backup
	 * @return Map<String, String> log file and path
	 * @Refactor Athira
	 */
	public static Map<String, String> verifyRdkLogAlbltyAndTailLogToGivenPathAndConsole(Dut device,
			AutomaticsTapApi tapEnv, List<String> mustHaveLogFileList, String tailValue, long polledTime,
			long maxDuration, String consoleType, String backUpPath) {
		LOGGER.debug("STARTING METHOD : verifyRdkLogAlbltyAndTailLogToGivenPathAndConsole()");
		long startTime = System.currentTimeMillis();
		boolean result = false;
		String errorMessage = "";

		Map<String, String> mapForLogFileWithPath = new HashMap<>();
		String pathForBackupFile;
		try {
			String command = BroadBandCommandConstants.TAIL_GIVEN_LOG_FILE_WITH_PATH
					.replace(BroadBandTestConstants.STRING_VALUE_TO_REPLACE, tailValue);
			String commandToExecute;
			Map<String, Boolean> logFilesUpdate = new HashMap<>();
			for (String logFile : mustHaveLogFileList) {
				logFilesUpdate.put(logFile, false);
			}
			List<String> missingLogFiles = mustHaveLogFileList;
			boolean isAtomSyncAvailable = CommonMethods.isAtomSyncAvailable(device, tapEnv);
			do {
				// Identifying whether log files available or not
				if (consoleType.equals(BroadBandTestConstants.ARM)) {
					missingLogFiles = BroadBandCommonUtils.verifyMustHaveLogFilesAvailabilityinAtomOrArm(device, tapEnv,
							mustHaveLogFileList, false);
				} else {
					missingLogFiles = BroadBandCommonUtils.verifyMustHaveLogFilesAvailabilityinAtomOrArm(device, tapEnv,
							mustHaveLogFileList, isAtomSyncAvailable);
				}
				result = missingLogFiles.isEmpty();
				mustHaveLogFileList.removeAll(missingLogFiles);
				if (!mustHaveLogFileList.isEmpty()) {
					for (String logFile : mustHaveLogFileList) {
						pathForBackupFile = BroadBandCommonUtils.concatStringUsingStringBuffer(backUpPath,
								BroadBandTestConstants.TAG_BACK_UP_FILE, logFile);
						commandToExecute = command.replace(BroadBandTestConstants.STRING_REPLACE, logFile)
								.replace(BroadBandTestConstants.REPLACE_BACKUP_FILE, pathForBackupFile);
						if (!logFilesUpdate.get(logFile)) {
							if (consoleType.equals(BroadBandTestConstants.STRING_ATOM_CONSOLE)) {
								tapEnv.executeCommandOnAtom(device, commandToExecute);
							} else if (consoleType.equals(BroadBandTestConstants.ARM)) {
								tapEnv.executeCommandUsingSsh(device, commandToExecute);
							} else {
								if (isAtomSyncAvailable) {
									tapEnv.executeCommandOnAtom(device, commandToExecute);
								} else {
									tapEnv.executeCommandUsingSsh(device, commandToExecute);
								}
							}
							logFilesUpdate.put(logFile, true);
							mapForLogFileWithPath.put(logFile, pathForBackupFile);
						}
					}
				}
				mustHaveLogFileList = new ArrayList<String>(missingLogFiles);
				LOGGER.info("Successfully verified all the must have log files are present : " + result);
			} while (!result && ((System.currentTimeMillis() - startTime) < maxDuration)
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, polledTime));

			errorMessage = result ? "Successfully verified all the must have log files are present"
					: "Some files are missing -> " + missingLogFiles;

		} catch (Exception exception) {
			errorMessage = "Exception occured while verifying logavailability  " + exception.getMessage();
			LOGGER.error(errorMessage);
		}
		LOGGER.info("ENDING METHOD:  verifyRdkLogAlbltyAndTailLogToGivenPathAndConsole()");
		return mapForLogFileWithPath;
	}

	/**
	 * Method to verify must have device logs availability status
	 * 
	 * @param device              Dut
	 * @param tapEnv              instance of {@link AutomaticsTapApi}
	 * @param mustHaveLogFileList Must have log file list
	 * @param isAtomSyncAvailable
	 * 
	 * @return List with files and its size details
	 * @Refactor Athira
	 */
	public static ArrayList<String> verifyMustHaveLogFilesAvailabilityinAtomOrArm(Dut device, AutomaticsTapApi tapEnv,
			List<String> mustHaveLogFileList, boolean isAtomSyncAvailable) {
		String errorMessage = null;
		List<String> availableLogFiles = new ArrayList<String>();
		ArrayList<String> missingLogFiles = null;
		String response = null;
		String command = CommonMethods.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_LS,
				BroadBandCommandConstants.DIRECTORY_LOGS);
		if (isAtomSyncAvailable) {
			response = CommonMethods.executeCommandInAtomConsole(device, tapEnv, command);
		} else {
			response = tapEnv.executeCommandUsingSsh(device, command);
		}
		LOGGER.info("COMMAND EXECUTION RESPONSE: " + response);
		if (CommonMethods.isNotNull(response)
				&& !response.equalsIgnoreCase(AutomaticsConstants.NO_SUCH_FILE_OR_DIRECTORY)) {
			availableLogFiles = Arrays
					.asList(response.split(BroadBandTestConstants.PATTERN_MATCHER_FOR_MULTIPLE_SPACES));
			missingLogFiles = BroadBandCommonUtils.verifyLogFileIsAvailable(availableLogFiles, mustHaveLogFileList);
		} else {
			errorMessage = "Unable to retrieve the details of device logs under /rdklogs/logs/ folder using command";
			throw new TestException(errorMessage);
		}

		return missingLogFiles;
	}

	/**
	 * Method to validate log file availability status
	 * 
	 * @param hmap        Hash map to hold files and its size
	 * @param logFileList List to hold all the required log files
	 * 
	 * @return files availability status
	 */

	public static ArrayList<String> verifyLogFileIsAvailable(List<String> availableLogFiles, List<String> logFileList) {

		LOGGER.debug("STARTING METHOD : verifyLogFileIsAvailable");
		boolean status = false;
		ArrayList<String> missingFileList = new ArrayList<>();
		for (String file : logFileList) {
			status = availableLogFiles.contains(file);
			LOGGER.info(">>>>>>> " + file + " availability status : " + status);
			if (!status) {
				missingFileList.add(file);
				LOGGER.error(">>>>>>> " + file + " is missing ");
			}
		}
		LOGGER.debug("ENDING METHOD : verifyLogFileIsAvailable");
		return missingFileList;
	}

	/**
	 * Method to execute command on jump server and return IP address
	 * 
	 * @param tapEnv
	 * @param command
	 * @param isIpv6Addr
	 * @param patternSearch
	 * @param patternMatcherGroup
	 * @return IP Address
	 */
	public static String executeCommandOnJumpServerAndRetrievIPAddressWithPatternSearch(AutomaticsTapApi tapEnv,
			String command, boolean isIpv6Addr, String patternSearch, int patternMatcherGroup) {
		LOGGER.debug("STARTING METHOD :executeCommandOnJumpServerAndRetrievIPAddressWithPatternSearch");
		String response = BroadBandCommonUtils.executeCommandInNonWhiteListedJumpServer(tapEnv, command);
		List<String> responseList = new ArrayList<>();
		String ipAddressRetrieved = null;
		if (CommonMethods.isNotNull(response)) {
			responseList = BroadBandCommonUtils.patternFinderForMultipleMatches(response, patternSearch,
					patternMatcherGroup);
			if (!responseList.isEmpty()) {
				ipAddressRetrieved = responseList.get(0);
				// if isIpv6Addr is true then validation for IPv6
				if (isIpv6Addr && CommonMethods.isIpv6Address(ipAddressRetrieved)) {
					return ipAddressRetrieved;
				} else if (!isIpv6Addr && CommonMethods.isIpv4Address(ipAddressRetrieved)) {
					return ipAddressRetrieved;
				}
			}
		}
		LOGGER.debug("ENDING METHOD :executeCommandOnJumpServerAndRetrievIPAddressWithPatternSearch");
		return ipAddressRetrieved;
	}

	/**
	 * Helper method to execute command in Non White listed jump server
	 * 
	 * @param tapEnv  AutomaticsTapApi instance
	 * @param command command to be executed
	 * @return string , response of the command executed.
	 * 
	 */
	public static String executeCommandInNonWhiteListedJumpServer(AutomaticsTapApi tapEnv, String command) {
		LOGGER.debug("STARTING METHOD: executeCommandInNonWhiteListedJumpServer");
		String jumpServer = null;
		String response = null;
		try {
			// Executing the command directly in jump server.
			jumpServer = BroadbandPropertyFileHandler.getNonWhiteListedJumpserverIP();

			LOGGER.info("Jump Server detail is - " + jumpServer);
			// Obtaining the command response
			response = tapEnv.executeCommandUsingSshConnection(WhiteListServer.getInstance(tapEnv, jumpServer),
					command);
		} catch (Exception exception) {
			LOGGER.error(
					"Failed to execute command in jump server " + jumpServer + ". Error - " + exception.getMessage());
		}
		LOGGER.debug("ENDING METHOD: executeCommandInNonWhiteListedJumpServer");
		return response;
	}

	/**
	 * Method to verify the snmp reboot reason
	 * 
	 * @param device Dut instance
	 * @param tapEnv AutomaticsTapApi instance
	 * @return True-if expected reboot reason is obtained
	 * @throws JSONException
	 * 
	 */
	public static boolean verifySnmpRebootReason(Dut device, AutomaticsTapApi tapEnv) throws JSONException {
		LOGGER.debug("STARTING METHOD: verifySnmpRebootReason()");
		boolean isSnmpRebootReasonValid = false;
		String rebootReason = null;
		try {
			rebootReason = BroadbandPropertyFileHandler.getSnmpRebootReasonList();
			JSONObject jsonObj = new JSONObject(rebootReason);
			if (jsonObj != null) {
				isSnmpRebootReasonValid = BroadBandWebPaUtils.getAndVerifyWebpaValueInPolledDuration(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_COMMAND_LAST_REBOOT_REASON,
						jsonObj.has(device.getModel()) ? jsonObj.getString(device.getModel())
								: jsonObj.getString(BroadBandTestConstants.DEFAULT),
						BroadBandTestConstants.ONE_MINUTE_IN_MILLIS, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while verifying SNMP reboot reason:" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: verifySnmpRebootReason()");
		return isSnmpRebootReasonValid;
	}

	/**
	 * Method to execute grep command to grep telemetry marker for SNMP reboot based
	 * on the device vendor
	 * 
	 * @param tapEnv AutomaticsTapApi instance
	 * @param device The device to be validated
	 * @return - true if grep command retrieves the expected value else false
	 */
	public static boolean verifyTelemetryMarkerForDeviceRebootInitiatedBySnmpDocDevMib(Dut device,
			AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: verifyTelemetryMarkerForDeviceRebootInitiatedBySnmpDocDevMib()");
		// Variable declaration starts
		boolean status = false;
		String grepCommandOutput = null;
		String response = null;
		String errorMessage = "";
		String expectedOutput = null;
		// Variable declaration ends
		try {
			grepCommandOutput = BroadBandCommandConstants.COMMAND_TO_GREP_REBOOT_TELEMETRY_MARKER;
			expectedOutput = BroadBandTraceConstants.TELEMETRY_MARKER_DOCSIS_SNMP_REBOOT;
			response = tapEnv.executeCommandUsingSsh(device, grepCommandOutput);
			status = CommonMethods.isNotNull(response)
					&& (CommonUtils.patternSearchFromTargetString(response, expectedOutput));
		} catch (Exception exception) {
			errorMessage = "Exception occure while verifying yelemetry marker for device reboot initiated by SnmpDocDevMib."
					+ exception.getMessage();
			LOGGER.error(errorMessage);
		}
		LOGGER.debug("ENDING METHOD: verifyTelemetryMarkerForDeviceRebootInitiatedBySnmpDocDevMib()");
		return status;
	}

	/**
	 * Method to get PID of process based on architecture
	 * 
	 * @param tapEnv      {@link AutomaticsTapApi}
	 * @param device      {@link Dut}
	 * @param processName String representing the process name
	 * 
	 * @return PID of given process
	 * @author Alan_Bivera
	 */
	public static String getPidOfProcessResolvingArch(Dut device, AutomaticsTapApi tapEnv, String processName) {
		LOGGER.debug("ENTERING METHOD getPidOfProcessResolvingArch");
		String processId = null;
		try {
			if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
				processId = getPidOfProcessFromAtomConsole(device, tapEnv, processName);
			} else {
				processId = CommonMethods.getPidOfProcess(device, tapEnv, processName);
			}
		} catch (Exception exception) {
			LOGGER.error("EXCEPTION OCCURRED WHILE TRYING TO GET THE PROCESS ID: " + exception.getMessage());
		}
		LOGGER.info("PROCESS ID: " + processId);
		LOGGER.debug("ENDING METHOD getPidOfProcessResolvingArch");
		return processId;
	}

	/**
	 * Method to get device file space
	 * 
	 * @param device {@link Dut}
	 * @refactor Govardhan
	 */
	public static boolean isModelWithFileSpaceGreaterThan90(Dut device) {
		boolean result = false;
		if (BroadbandPropertyFileHandler
				.getFileSystemSpaceBasedOnDevice(device) == BroadBandTestConstants.STRING_VALUE_90) {
			result = true;
		}
		return result;
	}

	/**
	 * @param tapEnv        {@link AutomaticsTapApi}
	 * @param device        {@link Dut}
	 * @param versionNumber
	 * @return true if lighttpd version upgraded or matches to the stbprops
	 * @refactor Leela Krishnama Naidu Andela
	 * @refactor Govardhan
	 */
	public static boolean verifyLighttpdVersion(Dut device, AutomaticsTapApi tapEnv, String versionNumber) {
		LOGGER.debug("Starting Method : verifyLighttpdVersion()");
		String response = null;
		String majorVersion = null;
		String majorIndex = null;
		boolean status = false;
		// execute command and get lighttpd version
		response = tapEnv.executeCommandUsingSsh(device, RDKBTestConstants.CMD_GET_LIGHTTPD_VERSION);
		if (CommonMethods.isNotNull(response)) {

			LOGGER.info("Obtained lighttpd version from server response : " + response);
			// grep only version from server response using pattern finder
			// method
			response = CommonMethods.patternFinder(response, RDKBTestConstants.PATTERN_GET_LIGHTTPD_VERSION);
			LOGGER.info("Obtained lighttpd version fom server response: " + response);
			LOGGER.info("Obtained lighttpd version from stbprops : " + versionNumber);
			versionNumber = versionNumber.trim();
			majorVersion = CommonMethods.patternFinder(response,
					BroadBandTestConstants.PATTERN_TO_RETRIEVE_LIGHTTPD_MAJOR_VERSION);

			majorIndex = CommonMethods.patternFinder(versionNumber,
					BroadBandTestConstants.PATTERN_TO_RETRIEVE_LIGHTTPD_MAJOR_VERSION);

			int minorVersion = Integer.parseInt(CommonMethods.patternFinder(response,
					BroadBandTestConstants.PATTERN_TO_RETRIEVE_LIGHTTPD_MINOR_VERSION));

			int minorIndex = Integer.parseInt(CommonMethods.patternFinder(versionNumber,
					BroadBandTestConstants.PATTERN_TO_RETRIEVE_LIGHTTPD_MINOR_VERSION));

			if (majorVersion.equals(majorIndex)) {
				LOGGER.info("Lighttpd major version obtained from device and stbprops matched");

				if (minorVersion >= minorIndex) {
					status = true;
					LOGGER.info("Lighttpd version updated :" + response);

				} else {
					LOGGER.error("Unable to get the updated lighttpd version :" + response);
				}

			} else {
				LOGGER.error("Lighttp major version obtained from device and stbprops are different");
			}
		} else {
			LOGGER.error(
					"Obtained NULL response!!..Not able to get lighttpd version from box using \"lighttpd -version\" command");
		}
		LOGGER.debug("Ending Method : verifyLighttpdVersion()");
		return status;
	}

	/**
	 * Method to perform webpa reboot and verify the device is accessible.
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut} AutomaticsTapApi Instance
	 * @return true if device is up after webpa reboot
	 * @refactor Govardhan
	 */
	public static boolean rebootViaWebpaAndWaitForStbAccessible(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.info("STARTING METHOD: rebootViaWebpaAndWaitForStbAccessible()");
		boolean isRebootedAndStbAccessible = false;
		boolean webpaSetSuccessful = false;
		long startTime = System.currentTimeMillis();
		do {
			webpaSetSuccessful = BroadBandWiFiUtils.setWebPaParams(device,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_CONTROL_DEVICE_REBOOT, BroadBandTestConstants.DEVICE,
					BroadBandTestConstants.CONSTANT_0);
		} while (!webpaSetSuccessful
				&& ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.THREE_MINUTE_IN_MILLIS)
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
		if (webpaSetSuccessful) {
			isRebootedAndStbAccessible = verifySTBRebootAndStbAccessible(device, tapEnv);
			LOGGER.info("isRebootedAndStbAccessible :"+isRebootedAndStbAccessible);
		}
		LOGGER.info("ENDING METHOD: rebootViaWebpaAndWaitForStbAccessible()");
		return webpaSetSuccessful && isRebootedAndStbAccessible;
	}

	/**
	 * Helper method to kill and verify the given process
	 * 
	 * @param tapEnv      {@link AutomaticsTapApi}
	 * @param device      {@link Dut}
	 * @param processName Name of the Process
	 * @return status . If Its True,If process killed, Else False
	 * @refactor Govardhan
	 */
	public static boolean killAndCheckProcess(Dut device, AutomaticsTapApi tapEnv, String processName) {
		LOGGER.debug("Starting Method : killAndVerifyProcess()");
		boolean status = false;
		String cmdToKill = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.CMD_KILLALL_11,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, processName);
		String processId = CommonMethods.getPidOfProcess(device, tapEnv, processName);
		LOGGER.info(" Process Id : " + processId);
		if (CommonMethods.isNotNull(processId)) {
			tapEnv.executeCommandUsingSsh(device, cmdToKill);
			processId = CommonMethods.getPidOfProcess(device, tapEnv, cmdToKill);
			status = CommonMethods.isNull(processId);
		}
		LOGGER.debug("Ending Method : killAndVerifyProcess()");
		return status;
	}

	/**
	 * Method to set Maintenance Window without offset time
	 * 
	 * @param tapEnv                   {@link AutomaticsTapApi}
	 * @param device                   {@link Dut}
	 * @param maintenanceWindowIterval Maintenance Window to be set
	 * @return true if given Maintenance Window is set successfully
	 * @refactor Govardhan
	 */
	public static BroadBandResultObject setCustomMaintenanceWindow(Dut device, AutomaticsTapApi tapEnv,
			String maintenanceWindowIterval) {
		LOGGER.debug("STARTING METHOD : setMaintenanceWindow");
		// Variable declaration starts
		BroadBandResultObject result = new BroadBandResultObject();
		boolean status = false;
		String errorMessage = "Obtained null response for time of the day in seconds using date command";
		String windowStartTime = null;
		String windowEndTime = null;
		String response = "";
		// Variable declaration ends
		try {
			response = BroadBandCommonUtils.getTimeOfDayInSeconds(device, tapEnv);
			LOGGER.info("Time of the day in seconds: " + response);
			if (CommonMethods.isNotNull(response)) {
				windowStartTime = Long.toString(Long.parseLong(response) + BroadBandTestConstants.CONSTANT_60);
				LOGGER.info("windowStartTime to be set: " + windowStartTime);
				windowEndTime = Long
						.toString(Long.parseLong(windowStartTime) + Long.parseLong(maintenanceWindowIterval));
				LOGGER.info("windowEndTime to be set: " + windowEndTime);
				errorMessage = "Failed to set maintenance window start time as " + windowStartTime;
				if (BroadBandWebPaUtils.setAndVerifyParameterValuesUsingWebPaorDmcli(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_COMMAND_MAINTENANCE_WINDOW_START_TIME,
						BroadBandTestConstants.CONSTANT_0, windowStartTime)) {
					errorMessage = "Failed to set maintenance window end time as " + windowEndTime;
					status = BroadBandWebPaUtils.setAndVerifyParameterValuesUsingWebPaorDmcli(device, tapEnv,
							BroadBandWebPaConstants.WEBPA_COMMAND_MAINTENANCE_WINDOW_END_TIME,
							BroadBandTestConstants.CONSTANT_0, windowEndTime);
				}
			}
		} catch (Exception exception) {
			errorMessage = errorMessage
					+ "Exception occurred while setting maintenace window through DMCLI/Webpa. Error is -"
					+ exception.getMessage();
			LOGGER.error(errorMessage);
		}
		LOGGER.info("Is Maintenance window set successfully " + status);
		result.setStatus(status);
		result.setErrorMessage(errorMessage);
		LOGGER.debug("ENDING METHOD : setMaintenanceWindow");
		return result;
	}

	/**
	 * Helper method to get current time of the day in seconds
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @return String containing seconds of time of the day
	 * 
	 * @author Ashwin sankara
	 * @refactor Govardhan
	 */
	public static String getTimeOfDayInSeconds(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("Entering method: getTimeOfDayInSeconds");
		String timeDifference = null;
		String currentTime = null;
		String midnightTime = null;
		midnightTime = tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_MIDNIGHT_EPOCH_TIME);
		if (CommonMethods.isNotNull(midnightTime)) {
			currentTime = tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_CURRENT_EPOCH_TIME);
			if (CommonMethods.isNotNull(currentTime)) {
				// Subtracting midnight time from current time to get time of day in seconds
				timeDifference = Long
						.toString(Long.parseLong(currentTime.trim()) - Long.parseLong(midnightTime.trim()));
				LOGGER.info("Time of day in seconds: " + timeDifference);
			}
		}
		LOGGER.debug("Exiting method: getTimeOfDayInSeconds");
		return timeDifference;
	}

	/**
	 * Helper method to get Epoch Time in Second
	 * 
	 * @refactor Govardhan
	 */
	public static Long getEpochTimeInSecond(AutomaticsTapApi tapEnv, Dut device) {
		String consoleOutput = tapEnv.executeCommandUsingSsh(device, "date +%s");
		LOGGER.debug("Epoch time console output: " + consoleOutput);

		String epochInSec = parseCommandOutputForOneLinerResult(consoleOutput, "date +%s");

		return Long.valueOf(Long.parseLong(epochInSec));
	}

	/**
	 * Helper method to get parse Command Output
	 * 
	 * @refactor Govardhan
	 */

	public static String parseCommandOutputForOneLinerResult(String commandOutput, String executedCommand) {
		String[] splitCommandOutputs = commandOutput.trim().split("\n");
		String trimmedCommandOutputLine = null;

		String commandResult = null;

		for (String commandOutputLine : splitCommandOutputs) {
			trimmedCommandOutputLine = commandOutputLine.trim();

			if ((trimmedCommandOutputLine.isEmpty()) || (trimmedCommandOutputLine.contains("#"))
					|| (trimmedCommandOutputLine.contains(executedCommand)))
				continue;
			LOGGER.debug("Found a line satisfying the conditions........ : " + trimmedCommandOutputLine);
			commandResult = trimmedCommandOutputLine.replaceAll("#", "");
		}

		LOGGER.info(String.format("Parsed command result : %s.", new Object[] { commandResult }));

		return commandResult;
	}

	/**
	 * Helper method to set Maintenance Window for given time offset, from current
	 * time, and time interval
	 * 
	 * @param tapEnv                   {@link AutomaticsTapApi}
	 * @param device                   {@link Dut}
	 * @param offset                   Time offset from current time in min
	 * @param maintenanceWindowIterval time interval between start and end of
	 *                                 maintenance window time. time interval should
	 *                                 be greater than 15
	 * 
	 * @return {@link BroadBandResultObject} with status true if value is set, false
	 *         if value is not set
	 * @author Praveenkumar Paneerselvam
	 * @refactor Govardhan
	 */
	public static BroadBandResultObject setMaintenanceWindowWithGivenDetail(Dut device, AutomaticsTapApi tapEnv,
			long offsetInterval, long maintenanceWindowIterval) {
		BroadBandResultObject result = new BroadBandResultObject();
		LOGGER.debug("STARTING METHOD : setMaintenanceWindowWithGivenDetail");
		boolean status = false;
		String errorMessage = null;
		try {

			String offset = DmcliUtils.getParameterValueUsingDmcliCommand(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_TIME_OFFSET);
			long currentTime = getEpochTimeInSecond(tapEnv, device);
			errorMessage = "Failed to get offset time from parameter "
					+ BroadBandWebPaConstants.WEBPA_PARAM_TIME_OFFSET;
			Long dayStartTime = getEpochDateStartTime(device, tapEnv);
			if (CommonMethods.isNotNull(offset)) {
				Long offsetTime = Long.parseLong(offset.trim());
				Long startTime = currentTime - dayStartTime + offsetInterval * 60 + offsetTime;
				errorMessage = "Failed to set Maintenace window start time using tr-181 paramter "
						+ BroadBandWebPaConstants.WEBPA_COMMAND_MAINTENANCE_WINDOW_START_TIME;

				if (DmcliUtils.setParameterValueUsingDmcliCommand(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_COMMAND_MAINTENANCE_WINDOW_START_TIME,
						BroadBandTestConstants.DMCLI_SUFFIX_TO_SET_STRING_PARAMETER, startTime.toString())) {
					errorMessage = "Failed to set Maintenace window end time using tr-181 paramter "
							+ BroadBandWebPaConstants.WEBPA_COMMAND_MAINTENANCE_WINDOW_END_TIME;
					Long endTime = startTime + maintenanceWindowIterval * 60;
					status = DmcliUtils.setParameterValueUsingDmcliCommand(device, tapEnv,
							BroadBandWebPaConstants.WEBPA_COMMAND_MAINTENANCE_WINDOW_END_TIME,
							BroadBandTestConstants.DMCLI_SUFFIX_TO_SET_STRING_PARAMETER, endTime.toString());
					LOGGER.info("Value to be set for start time and end time are " + startTime + " and " + endTime);
				}
			}
		} catch (Exception exception) {
			errorMessage = "Exception occurred while setting maintenace window through DMCLI/Webpa. Error is -"
					+ exception.getMessage();
			LOGGER.error(errorMessage);
		}
		LOGGER.info("Is Maintenance window set successfully " + status);
		result.setStatus(status);
		result.setErrorMessage(errorMessage);
		LOGGER.debug("ENDING METHOD : setMaintenanceWindowWithGivenDetail");
		return result;
	}

	/**
	 * Helper method to get epoch date start time
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @return epoch date start time
	 * @author Praveenkumar Paneerselvam
	 * @refactor Govardhan
	 */
	public static Long getEpochDateStartTime(Dut device, AutomaticsTapApi tapEnv) {
		String consoleOutput = tapEnv.executeCommandUsingSsh(device, "date -d '00:00:00' '+%s'");
		LOGGER.debug("Epoch time console output: " + consoleOutput);

		String epochInSec = parseCommandOutputForOneLinerResult(consoleOutput, RDKBTestConstants.CMD_EPOCH_TIME);

		return Long.parseLong(epochInSec);
	}

	/**
	 * Method to parse Json input array of Webpa name and value
	 * 
	 * @param jsonInput String Json Input to parse
	 * @return Map with name and value
	 */
	public static Map<String, String> getJsonDataFromJsonArrayInMap(String jsonInput) {
		LOGGER.debug("STARTING  getJsonDataFromJsonArrayInMap()");
		Map<String, String> jsonMap = null;

		JSONArray jsonArray;
		JSONObject object;
		try {
			jsonArray = new JSONArray(jsonInput);
			jsonMap = new HashMap<String, String>();
			for (int count = 0; count < jsonArray.length(); count++) {
				object = (JSONObject) jsonArray.get(count);
				jsonMap.put(object.get("name").toString(), object.get("value").toString());
			}
		} catch (Exception e) {
			LOGGER.error("Unable to parse given Json input array");
		}
		LOGGER.debug("ENDING getJsonDataFromJsonArrayInMap()");
		return jsonMap;
	}

	/**
	 * Utils method compare SNMP and WebPA output
	 * 
	 * @param tapEnv
	 * @param device
	 * @param oid
	 * @param tableIndex
	 * @param webPaParam
	 * @return
	 */
	public BroadBandResultObject compareValueFromSnmpAndWebPa(AutomaticsTapApi tapEnv, Dut device, String oid,
			String tableIndex, String webPaParam) {

		BroadBandResultObject result = new BroadBandResultObject();
		String errorMessage = "";
		String snmpOutput = "";
		String webPaOutput = "";
		boolean status = false;

		try {
			snmpOutput = BroadBandSnmpUtils.snmpGetOnEcm(tapEnv, device, oid, tableIndex);
			LOGGER.info("Value retrieved from SNMP : " + snmpOutput);
			errorMessage = "Unable to retrieve value from SNMP using OID : " + oid;
			if (CommonMethods.isNotNull(snmpOutput)) {
				webPaOutput = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv, webPaParam
						.replace(BroadBandTestConstants.TR181_NODE_REF, BroadBandTestConstants.STRING_CONSTANT_1));
				snmpOutput = convertOutputCorrespondingToWebPAParam(webPaParam, snmpOutput, webPaOutput);
				LOGGER.info("Value retrieved from WEBPA/DMCLI : " + webPaOutput);
				errorMessage = "Unable to retrieve value from WEBPA/DMCLI using param : " + webPaParam;
				if (CommonMethods.isNotNull(webPaOutput)) {
					errorMessage = "Value retrieved from SNMP and WEBPA/DMCLI do not match. Value from SNMP : "
							+ snmpOutput + "; value from WEBPA/DMCLI : " + webPaOutput;
					status = CommonUtils.patternSearchFromTargetString(webPaOutput, snmpOutput);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while retrieving and comapring values from SNMP and WEBPA/DMCLI : "
					+ e.getMessage());
		}

		result.setStatus(status);
		result.setOutput(snmpOutput);
		result.setErrorMessage(errorMessage);

		return result;
	}

	/**
	 * Utils method to convert output corresponding to WEBPA Param
	 * 
	 * @param webPaParam
	 * @param snmpOutput
	 * @return
	 */
	public String convertOutputCorrespondingToWebPAParam(String webPaParam, String snmpOutput, String webPaOutput) {
		String convertedOutput = "";
		try {
			switch (webPaParam) {
			case BroadBandWebPaConstants.WEBPA_PARAM_DOWNSTREAM_CHANNEL_FREQUENCY:
				convertedOutput = CommonUtils.patternSearchFromTargetString(webPaOutput, "MHz")
						? String.valueOf(new BigInteger(snmpOutput).divide(new BigInteger("1000000")))
						: snmpOutput;
				break;
			case BroadBandWebPaConstants.WEBPA_PARAM_UPSTREAM_CHANNEL_POWERLEVEL:
			case BroadBandWebPaConstants.WEBPA_PARAM_DOWNSTREAM_CHANNEL_SNRLEVEL:
				convertedOutput = String.valueOf(Integer.parseInt(snmpOutput) / BroadBandTestConstants.CONSTANT_10);
				break;
			default:
				convertedOutput = snmpOutput;
				break;
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while converting the output corresponding to WEBPA : " + e.getMessage());
		}
		LOGGER.info("Converted Output : " + convertedOutput);
		return convertedOutput;
	}

	/**
	 * Helper method to find the feature availability in build
	 * 
	 * @param device
	 * @param BuildFromStbProp
	 * @return status
	 * 
	 * @author ArunKumar Jayachandran
	 */
	public static boolean verifyFeatureAvailabilityInBuild(AutomaticsTapApi tapEnv, Dut device,
			String BuildFromStbProp) {
		LOGGER.debug("Starting of Method: verifyFeatureAvailabilityInBuild");
		String imageName = null;
		boolean status = false;
		imageName = FirmwareDownloadUtils.getCurrentFirmwareFileNameForCdl(tapEnv, device);
		if (CommonMethods.isNotNull(imageName)) {
			if (BuildFromStbProp.equalsIgnoreCase(BroadBandTestConstants.BUILD_TYPE_SPRINT)) {
				status = CommonUtils.isGivenStringAvailableInCommandOutput(imageName,
						BroadBandTestConstants.BUILD_TYPE_SPRINT);
			} else if (BuildFromStbProp.equalsIgnoreCase(BroadBandTestConstants.BUILD_TYPE_STABLE)) {
				status = CommonUtils.isGivenStringAvailableInCommandOutput(imageName,
						BroadBandTestConstants.BUILD_TYPE_SPRINT)
						|| CommonUtils.isGivenStringAvailableInCommandOutput(imageName,
								BroadBandTestConstants.BUILD_TYPE_STABLE);
			} else if (BuildFromStbProp.equalsIgnoreCase(BroadBandTestConstants.BUILD_TYPE_RELEASE)) {
				status = true;
			}
		}
		LOGGER.debug("Ending of Method: verifyFeatureAvailabilityInBuild");
		return status;
	}

	/**
	 * Utility method to verify given directory exists in the ARM Console.
	 * 
	 * @param tapEnv             {@link AutomaticsTapApi}
	 * @param device             {@link Dut}
	 * @param completeFolderPath String representing the complete path of the file.
	 * @param pollDuration       Maximum poll duration
	 * @param pollInterval       Polling interval time
	 * @param isAvailable        True or False value to verify
	 * 
	 * @return Object representing the BroadBandResultObject with status & message.
	 * 
	 * @author Anuvarshini Manickavasagam Arulnambi
	 * @refactor Said Hisham
	 */
	public static BroadBandResultObject doesDirectoryExistInArmConsole(Dut device, AutomaticsTapApi tapEnv,
			String completeFolderPath, long pollDuration, long pollInterval, String isAvailable) {
		LOGGER.debug("STARTING METHOD doesDirectoryExistInArmConsole");
		// Variable declaration starts
		boolean result = false;
		long startTime = System.currentTimeMillis();
		String response = null;
		// Variable declaration ends
		do {
			tapEnv.waitTill(pollInterval);

			response = tapEnv.executeCommandUsingSsh(device,
					"if [ -d " + completeFolderPath + " ] ; then echo \"true\" ; else echo \"false\" ; fi");
			LOGGER.info("COMMAND EXECUTION RESPONSE: " + response);
			if (CommonMethods.isNotNull(response)
					&& !response.contains(BroadBandTestConstants.NO_SUCH_FILE_OR_DIRECTORY)) {
				result = response.trim().equalsIgnoreCase(isAvailable);
			}
			if (result) {
				break;
			}
		} while ((System.currentTimeMillis() - startTime) < pollDuration && !result);
		String errorMessage = result ? BroadBandTestConstants.EMPTY_STRING
				: "FOLDER " + completeFolderPath + " IS NOT PRESENT IN DEVICE.";
		LOGGER.info("IS FOLDER " + completeFolderPath + " PRESENT IN DEVICE: " + result);
		BroadBandResultObject objResult = new BroadBandResultObject();
		objResult.setStatus(result);
		objResult.setErrorMessage(errorMessage);
		LOGGER.debug("ENDING METHOD doesDirectoryExistInArmConsole");
		return objResult;
	}

	/**
	 * Utility method to verify given directory exists in the ARM Console.
	 * 
	 * @param device  {@link Dut}
	 * @param tapEnv  {@link AutomaticsTapApi}
	 * @param command Command to be executed
	 * @param refSize Reference size to check greater or less than actual size
	 * 
	 * @return status
	 * 
	 * @author Anuvarshini Manickavasagam Arulnambi
	 * @refactor Said Hisham
	 */
	public static boolean verifyDirectorySizeInArmConsole(Dut device, AutomaticsTapApi tapEnv, String command,
			double refSize) {
		LOGGER.debug("STARTING METHOD: verifyDirectorySizeInArmConsole()");
		// Variable declaration starts
		String response = null;
		boolean status = false;
		double sizeInMB = 0;
		// Variable declaration ends
		try {
			response = tapEnv.executeCommandUsingSsh(device, command);
			LOGGER.info("The value is " + response);
			if (CommonMethods.isNotNull(response) && CommonUtils.isGivenStringAvailableInCommandOutput(response,
					BroadBandTestConstants.VALUE_FOR_KB)) {
				LOGGER.info("cpuprocanalyzer size is less than " + refSize + "MB");
				status = true;
			} else if (CommonMethods.isNotNull(response) && CommonUtils.isGivenStringAvailableInCommandOutput(response,
					BroadBandTestConstants.VALUE_FOR_MB)) {
				response = response.substring(BroadBandTestConstants.CONSTANT_0,
						response.length() - BroadBandTestConstants.CONSTANT_1);
				LOGGER.info("sizeM is-" + response);
				sizeInMB = Double.parseDouble(response);
				LOGGER.info("sizeInMB is-" + sizeInMB);
				if (sizeInMB > refSize) {
					status = false;
				} else {
					status = true;
				}
			}
			LOGGER.info("sizeInLimit- " + status);
		} catch (Exception e) {
			LOGGER.error("Exception occured while getting detail in verifyDirectorySizeInArmConsole()");
		}
		LOGGER.debug("ENDING METHOD: verifyDirectorySizeInArmConsole()");
		return status;
	}

	/**
	 * Method to check whether list of patterns or messages are present in file
	 * 
	 * @param messageList contains pattern or message list to be checked in file
	 * @param fileName    contains name of file to be checked
	 * @param device      instance of {@link Dut}
	 * @param tapEnv      instance of {@link AutomaticsTapApi}
	 * @param checkType   boolean value containing whether to check for presence or
	 *                    absence of messages
	 * 
	 * @author Ashwin Sankarasubramanian
	 * @refactor Said Hisham
	 * 
	 */
	public static ResultValues checkFileForPatterns(Dut device, AutomaticsTapApi tapApi, List<String> messageList,
			String fileName, boolean checkType) {
		LOGGER.debug("ENTERING METHOD checkFileForPattern");
		ResultValues result = new ResultValues();
		result.setResult(false);
		if (CommonUtils.isFileExists(device, tapApi, fileName)) {
			for (String pattern : messageList) {
				String response = searchLogFiles(tapApi, device, pattern, fileName);
				result.setResult(CommonMethods.isNotNull(response));
				LOGGER.info("Search result: " + result.isResult());
				// Pass case: When pattern is expected and it's present, or when
				// pattern is not expected and it's absent
				if (checkType == result.isResult()) {
					// Inverting result for pass case when result is false
					result.setResult(true);
				} else {
					// Inverting result for fail case when result is true
					result.setResult(false);
					result.setMessage("Expected pattern match not found: " + pattern);
					break;
				}
			}
		} else {
			result.setMessage(AutomaticsConstants.NO_SUCH_FILE_OR_DIRECTORY);
		}
		LOGGER.info("Final result: " + result.isResult());
		LOGGER.debug("EXITING METHOD checkFileForPattern");
		return result;
	}

	/**
	 * Method to kill the process and verify using ps | grep command
	 * 
	 * @param device        {@link device}
	 * @param tapEnv        {@link tapEnv}
	 * @param processName   process name to kill
	 * @param patternForPid pattern to get the pid from ps command
	 * @author ArunKumar Jayachandran
	 * @refactor Rakesh C N
	 */
	public static boolean killProcessAndVerify(Dut device, AutomaticsTapApi tapEnv, String processName,
			String patternForPid) {
		LOGGER.debug("STARTING METHOD: killProcessAndVerify()");
		String response = null;
		String pid = null;
		boolean status = false;
		// execute the command ps | grep <process name>
		response = tapEnv.executeCommandUsingSsh(device,
				BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_PS_GREP, processName,
						BroadBandCommandConstants.CMD_TO_GREP_ONLY_PROCESS));
		if (CommonMethods.isNotNull(response)) {
			// Get the process id using regex pattern (\\d+)
			pid = CommonMethods.patternFinder(response, patternForPid);
			// Kill the process using kill -9 <pid>
			if (CommonMethods.isNotNull(pid)) {
				LOGGER.info("Process Id for " + processName + " is: " + pid);
				tapEnv.executeCommandUsingSsh(device,
						BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.KILL_9, pid));
			} else {
				LOGGER.error("Getting empty process id from ps | grep " + processName + " command");
			}
		} else {
			LOGGER.error("There is no process running for " + processName + " & response for \"ps | grep " + processName
					+ "\" command output:" + response);
		}
		// verify the process is killed properly
		if (CommonMethods.isNotNull(pid)) {
			response = tapEnv.executeCommandUsingSsh(device, BroadBandCommonUtils
					.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_PS_GREP, processName));
			status = CommonMethods.isNotNull(response)
					&& !CommonUtils.isGivenStringAvailableInCommandOutput(response, pid);
			LOGGER.info((status ? "Successfully killed the process" : "Failed to kill the process"));
		}
		LOGGER.debug("ENDING METHOD: killProcessAndVerify()");
		return status;
	}

	/**
	 * Helper method to get partner value for web UI validation
	 * 
	 * @param device {@link Dut}
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @return the true if AtomSyncUp uptime is greater than 20 mis
	 * @author Govardhan
	 */
	public static boolean getAtomSyncUptimeStatus(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("Starting of Method: getAtomSyncUptimeStatus");
		String response = null;
		int uptimeInMin = 0;
		boolean status = false;
		long startTime = System.currentTimeMillis();
		String errorMessage = null;
		try {

			do {
				errorMessage = "Webpa process is not up";
				response = tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_UPTIME);
				LOGGER.info("Device uptime " + response);
				status = CommonMethods.isNotNull(response);
				if (status) {
					errorMessage = "Device uptime is not greater than 20 mins";
					status = false;
					uptimeInMin = Integer.parseInt(response) / BroadBandTestConstants.CONSTANT_60;
					if (!(uptimeInMin >= BroadBandTestConstants.CONSTANT_20)) {
						BroadBandCommonUtils.hasWaitForDuration(tapEnv,
								(BroadBandTestConstants.CONSTANT_20 - uptimeInMin) * 60000);

						uptimeInMin = Integer.parseInt(
								tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_UPTIME))
								/ BroadBandTestConstants.CONSTANT_60;
						if (uptimeInMin >= BroadBandTestConstants.CONSTANT_20) {
							status = true;
							LOGGER.info("Device uptime is greater than 20 mins " + status);
						}
					} else {
						status = true;
						LOGGER.info("Device uptime is greater than 20 mins " + status);
					}
				}
			} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.EIGHT_MINUTE_IN_MILLIS
					&& !status);
			if (!status) {
				throw new TestException(
						"Exception occured : Webpa process is not up/ Device uptime is not greater than 20 mins "
								+ errorMessage);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while retrieving partner value " + e.getMessage());
		}
		LOGGER.debug("Ending of Method: getAtomSyncUptimeStatus");
		return status;
	}

	/**
	 * Utility method to validate whether device is in bridge-static mode or not
	 * using WebPA command.
	 * 
	 * @param device The device to be validated.
	 * @return true if device is in bridge-static mode else false.
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean verifyDeviceInBridgeStaticModeStatusUsingWebPaCommand(AutomaticsTapApi automaticsTapApi,
			Dut device) {
		LOGGER.debug("STARTING METHOD: verifyDeviceInBridgeStaticModeStatusUsingWebPaCommand()");

		// status
		boolean isBridgeMode = false;

		String response = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, automaticsTapApi,
				BroadBandWebPaConstants.WEBPA_PARAM_BRIDGE_MODE_STATUS);

		if (CommonMethods.isNotNull(response)) {
			isBridgeMode = response.contains(BroadBandTestConstants.LAN_MANAGEMENT_MODE_BRIDGE_STATIC);
		} else {
			LOGGER.error(
					"Null response obtained for web pa get on parameter Device.X_CISCO_COM_DeviceControl.LanManagementEntry.1.LanMode");
			throw new TestException(
					"Null response obtained for web pa get on parameter Device.X_CISCO_COM_DeviceControl.LanManagementEntry.1.LanMode");
		}
		LOGGER.debug("ENDING METHOD: verifyDeviceInBridgeStaticModeStatusUsingWebPaCommand()");
		return isBridgeMode;
	}

	/**
	 * Utility method to validate whether device is bridge mode or not using WebPA
	 * command.
	 * 
	 * @param device The device to be validated.
	 * @return true if device is in router mode, false if device is in bridge mode.
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean verifyDeviceInRouterModeStatusUsingWebPaCommand(AutomaticsTapApi tapEnv, Dut device) {

		LOGGER.debug("STARTING METHOD: verifyDeviceInRouterModeStatusUsingWebPaCommand()");

		boolean routerMode = false;

		String response = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
				BroadBandWebPaConstants.WEBPA_PARAM_BRIDGE_MODE_STATUS);

		if (CommonMethods.isNotNull(response)) {
			routerMode = response.contains(BroadBandTestConstants.LAN_MANAGEMENT_MODE_ROUTER);
		} else {
			LOGGER.error(
					"Null response obtained for web pa get on parameter Device.X_CISCO_COM_DeviceControl.LanManagementEntry.1.LanMode");
			throw new TestException(
					"Null response obtained for web pa get on parameter Device.X_CISCO_COM_DeviceControl.LanManagementEntry.1.LanMode");
		}

		LOGGER.debug("ENDING METHOD: verifyDeviceInRouterModeStatusUsingWebPaCommand()");
		return routerMode;
	}

	/**
	 * Utility Method to compare the Comma separated values; the values are
	 * converted to Array & then to ArrayList and then compared against each other.
	 * 
	 * @param expectedValue String representing the Expected Value
	 * @param actualValue   String representing the Actual Value
	 * 
	 * @return Boolean representing the result of the validation.
	 * @refactor said.h
	 */
	public static boolean compareCommaSeparateValues(String expectedValue, String actualValue) {
		LOGGER.debug("ENTERING METHOD compareCommaSeparateValues");
		boolean result = false;
		List<String> expectedValues = null;
		List<String> actualValues = null;
		// Create the List from the Comma separated String
		if (CommonMethods.isNotNull(expectedValue) && CommonMethods.isNotNull(actualValue)) {
			expectedValues = new ArrayList<String>(Arrays.asList(expectedValue.split(AutomaticsConstants.COMMA)));
			actualValues = new ArrayList<String>(Arrays.asList(actualValue.split(AutomaticsConstants.COMMA)));
		}
		// Compare the Lists
		if (null != expectedValues && null != actualValues && expectedValues.size() > 0 && actualValues.size() > 0) {
			result = expectedValues.containsAll(actualValues) && actualValues.containsAll(expectedValues);
		}
		LOGGER.info("COMPARED THE COMMA SEPARATED VALUES: EXPECTED = " + expectedValue + " | ACTUAL = " + actualValue
				+ " | RESULT = " + result);
		LOGGER.debug("ENDING METHOD compareCommaSeparateValues");
		return result;

	}

	/**
	 * method to check the device type and return the value
	 * 
	 * @param device
	 * @param value1 expected value for Atom and DSL device
	 * @param value2 expected value for a specific arm based device
	 * @return expectedValue return expected value based on the device type
	 * @author ArunKumar Jayachandran
	 * @refactor said hisham
	 */
	public static String getTxRateExpctdValForDeviceType(Dut device, String value1, String value2) {
		LOGGER.debug("STARTING METHOD : getTxRateExpctdValForDeviceType()");
		String expectedValue = null;
		if (CommonMethods.isAtomSyncAvailable(device, AutomaticsTapApi.getInstance())
				|| DeviceModeHandler.isDSLDevice(device)) {
			expectedValue = value1;
		} else
			expectedValue = value2;
		LOGGER.debug("ENDING METHOD :getTxRateExpctdValForDeviceType()");
		return expectedValue;
	}

	/**
	 * Utility method to perform Factory Reset on the device using WebPA by
	 * providing max polling duration taken to trigger factory reset & then wait for
	 * the device to come up.
	 * 
	 * @param tapEnv                 {@link AutomaticsTapApi}
	 * @param device                 {@link Dut}
	 * @param factoryResetTiggerTime max polling duration taken to trigger factory
	 *                               reset
	 * @return Boolean representing the result of the Factory Reset Operation.
	 */
	public static boolean performFactoryResetWebPaByPassingTriggerTime(AutomaticsTapApi tapEnv, Dut device,
			long factoryResetTiggerTime) {
		LOGGER.debug("ENTERING METHOD : performFactoryResetWebPaByPassingTriggerTime");
		// boolean variable to store the status
		boolean result = false;
		// boolean variable to store the whether the WebPa Service is Up or not
		boolean isWebPaServiceUp = false;
		if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
			BroadBandCommonUtils.getAtomsyncUptimeStatus(device, tapEnv);
		}
		result = BroadBandWiFiUtils.setWebPaParams(device, WebPaParamConstants.WEBPA_PARAM_FACTORY_RESET,
				DeviceModeHandler.isDSLDevice(device) ? BroadBandTestConstants.STRING_FACTORY_RESET_PARAMETER_FOR_DSL
						: BroadBandTestConstants.STRING_FOR_FACTORY_RESET_OF_THE_DEVICE,
				BroadBandTestConstants.CONSTANT_0);
		long startTime = 0L;
		// Check if the device goes down.
		if (result) {

			result = isRdkbDeviceAccessible(tapEnv, device, BroadBandTestConstants.TEN_SECOND_IN_MILLIS,
					factoryResetTiggerTime, false);

			LOGGER.info("IS DEVICE REBOOTING AFTER TRIGGERING FACTORY RESET : " + result);

			if (result) {
				CommonMethods.waitForEstbIpAcquisition(tapEnv, device);
				startTime = System.currentTimeMillis();
				do {
					result = CommonMethods.isSTBAccessible(device);
					if (result) {
						break;
					}
					LOGGER.info("DEVICE IS NOT UP AFTER FACTORY RESET. GOING TO WAIT FOR 1 MINUTE AND CHECK AGAIN.");
					tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
				} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIFTEEN_MINUTES_IN_MILLIS
						&& !result);
				LOGGER.info("IS DEVICE COMES UP AFTER FACTORY RESET : " + result);
			}
		} else {
			LOGGER.info("Webpa execution failed for performing factory reset ");
		}

		// If the device is up, check whether the webpa service is up and validate the
		// factory reset using
		// "verifyFactoryReset"
		if (result) {
			isWebPaServiceUp = BroadBandWebPaUtils.verifyWebPaProcessIsUp(tapEnv, device, true);
			if (isWebPaServiceUp) {
				if (BroadBandWebPaUtils.getAndVerifyWebpaValueInPolledDuration(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_PARAM_FOR_SYNDICATION_PARTNER_ID,
						BroadbandPropertyFileHandler.getDefaultPartnerID(), BroadBandTestConstants.ONE_MINUTE_IN_MILLIS,
						BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS)) {
					result = verifyFactoryReset(tapEnv, device);
				} else {
					// For Syndication Partners
					BroadBandResultObject resultObject = BroadBandRestoreWifiUtils.verifyDefaultSSIDForPartner(device,
							WiFiFrequencyBand.WIFI_BAND_2_GHZ, tapEnv, false);
					result = resultObject.isStatus();
				}
			}
		} else {
			LOGGER.error("DEVICE DIDN'T COME UP AFTER FACTORY RESETTING, EVEN AFTER WAITING FOR 15 MINUTES");
		}
		if (!isWebPaServiceUp) {
			LOGGER.error(
					"WEBPA SERVICE IS NOT UP IN THE DEVICE AFTER FACTORY RESETTING THE DEVICE, EVEN AFTER WAITING FOR 8 MINUTES. "
							+ "UNABLE TO VERIFY THE FACTORY RESET STATUS. HENCE RETURNING THE FACTORY RESET STATUS AS 'FALSE'");
		}
		LOGGER.debug("ENDING METHOD : performFactoryResetWebPaByPassingTriggerTime");
		return result;
	}

	/**
	 * Helper method to get the process id for dibbler client
	 * 
	 * @param device
	 * @param tapEnv
	 * @return pid
	 * 
	 * @author ArunKumar Jayachandran
	 * @refactor Said Hisham
	 */
	public static String getProcessIdForDibblerClient(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: getProcessIdForDibblerClient");
		String response = null;
		String pid = null;
		tapEnv.executeCommandUsingSsh(device, BroadBandCommonUtils.concatStringUsingStringBuffer(
				BroadBandCommandConstants.CMD_DIBBLER_CLIENT, BroadBandTestConstants.STRING_START));
		tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		response = tapEnv.executeCommandUsingSsh(device, BroadBandCommonUtils.concatStringUsingStringBuffer(
				BroadBandCommandConstants.CMD_DIBBLER_CLIENT, BroadBandTestConstants.STATUS));
		if (CommonMethods.isNotNull(response)) {
			if (!CommonUtils.isGivenStringAvailableInCommandOutput(response,
					BroadBandTraceConstants.LOG_MESSAGE_DIBBLER_CLIENT_PID)) {
				tapEnv.executeCommandUsingSsh(device, BroadBandCommonUtils.concatStringUsingStringBuffer(
						BroadBandCommandConstants.CMD_DIBBLER_CLIENT, BroadBandTestConstants.STRING_START));
				tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
				response = tapEnv.executeCommandUsingSsh(device, BroadBandCommonUtils.concatStringUsingStringBuffer(
						BroadBandCommandConstants.CMD_DIBBLER_CLIENT, BroadBandTestConstants.STATUS));
			}
			pid = CommonMethods.patternFinder(response, BroadBandTestConstants.PATTERN_TO_GET_PID_DIBBLER_CLIENT);
		}
		LOGGER.info("Dibbler client process id is: " + pid);
		LOGGER.debug("ENDING METHOD: getProcessIdForDibblerClient");
		return pid;
	}

	/**
	 * Helper method to get the peer interface name for Multi core devices
	 * 
	 * @param device
	 * @param tapEnv
	 * 
	 * @return peerInterface
	 * 
	 * @author ArunKumar Jayachandran
	 * @refactor Said Hisham
	 */
	public static String getPeerInterfaceNameForMultiCoreDevice(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: getPeerInterfaceNameForMultiCoreDevice");
		String response = null;
		String peerIp = null;
		String peerInterface = null;
		response = BroadBandCommonUtils.searchLogFiles(tapEnv, device, BroadBandTraceConstants.LOG_MESSAGE_PEER_INT_IP,
				BroadBandCommandConstants.FILE_DEVICE_PROPERTIES);
		if (CommonMethods.isNotNull(response)) {
			peerIp = CommonMethods.patternFinder(response, BroadBandTestConstants.PATTERN_TO_GET_THREE_OCTAT_IPV4);
			if (CommonMethods.isNotNull(peerIp)) {
				response = tapEnv.executeCommandUsingSsh(device, BroadBandCommonUtils
						.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_IFNAME_USING_IFCONFIG, peerIp));
				if (CommonMethods.isNotNull(response)) {
					peerInterface = CommonMethods.patternFinder(response,
							BroadBandTestConstants.PATTERN_TO_GET_INTERFACE_NAME);
				}
			}
		}
		LOGGER.info("Peer interface name: " + peerInterface);
		LOGGER.debug("ENDING METHOD: getPeerInterfaceNameForMultiCoreDevice");
		return peerInterface;
	}

	/**
	 * Method to generate ip rules and save them to a particular location
	 * 
	 * @param device               instance of {@link Dut}
	 * @param tapEnv               instance of {@link AutomaticsTapApi}
	 * @param locationForIpv4Table -- location to store the ipv4 rules
	 * @param locationForIpv6Table -- location to store the ipv6 rules
	 * 
	 * @author revanth.k
	 * @refactor Athira
	 */

	public static boolean dumpIpTablesToGivenLocation(Dut device, AutomaticsTapApi tapEnv, String locationForIpv4Table,
			String locationForIpv6Table) {
		LOGGER.debug("STARTING METHOD: dumpIpTablesToGivenLocation");
		String[] commandList = new String[2];
		String commandToListIpv4TableList = null;
		String commandToListIpv6TableList = null;
		String response = null;
		String errorMessage = null;
		boolean statusForIpv4Table = false;
		boolean statusForIpv6Table = false;

		commandList[0] = BroadBandCommonUtils
				.concatStringUsingStringBuffer(BroadBandTestConstants.COMMAND_TO_SAVE_IPV4_TABLE_RULES
						+ RDKBTestConstants.SINGLE_SPACE_CHARACTER + BroadBandTestConstants.CHR_CLOSING_ANGLE_BRACKET
						+ RDKBTestConstants.SINGLE_SPACE_CHARACTER + locationForIpv4Table);

		commandList[1] = BroadBandCommonUtils
				.concatStringUsingStringBuffer(BroadBandTestConstants.COMMAND_TO_SAVE_IPV6_TABLE_RULES
						+ RDKBTestConstants.SINGLE_SPACE_CHARACTER + BroadBandTestConstants.CHR_CLOSING_ANGLE_BRACKET
						+ RDKBTestConstants.SINGLE_SPACE_CHARACTER + locationForIpv6Table);

		tapEnv.executeCommandUsingSsh(device, commandList);

		commandToListIpv4TableList = BroadBandCommonUtils.concatStringUsingStringBuffer(
				LinuxCommandConstants.CMD_LIST_FOLDER_FILES, RDKBTestConstants.SINGLE_SPACE_CHARACTER,
				locationForIpv4Table);
		commandToListIpv6TableList = BroadBandCommonUtils.concatStringUsingStringBuffer(
				LinuxCommandConstants.CMD_LIST_FOLDER_FILES, RDKBTestConstants.SINGLE_SPACE_CHARACTER,
				locationForIpv6Table);
		response = tapEnv.executeCommandUsingSsh(device, commandToListIpv4TableList);

		LOGGER.info("Response for listing ipv4 table rules text file is -" + response);

		response = tapEnv.executeCommandUsingSsh(device, commandToListIpv4TableList);
		if (CommonMethods.isNotNull(response) && !response.contains(RDKBTestConstants.NO_SUCH_FILE_OR_DIRECTORY)
				&& CommonMethods.patternMatcher(response.trim(), locationForIpv4Table)) {
			statusForIpv4Table = true;
		} else {
			errorMessage += "Failed to save ipv4 table rules in location -" + locationForIpv4Table;
		}
		response = tapEnv.executeCommandUsingSsh(device, commandToListIpv6TableList);
		if (CommonMethods.isNotNull(response) && !response.contains(RDKBTestConstants.NO_SUCH_FILE_OR_DIRECTORY)
				&& CommonMethods.patternMatcher(response.trim(), locationForIpv6Table)) {
			statusForIpv6Table = true;
		} else {
			errorMessage += "Failed to save ipv6 table rules in location -" + locationForIpv6Table;
		}
		if (!(statusForIpv4Table && statusForIpv6Table)) {
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}
		LOGGER.debug("ENDING METHOD: dumpIpTablesToGivenLocation");
		return statusForIpv4Table && statusForIpv6Table;
	}

	/**
	 * Method to validate braln0 configuration..It executes "/sbin/ifconfig brlan0"
	 * and checks for the ip address fields in the reponse
	 * 
	 * @param device instance of {@link Dut}
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @return boolean true if the brlan0 configuration has both ipv4 and ipv6
	 *         addresses
	 */
	public static boolean validateBrlan0Configuration(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD : validateBrlan0Configuration");
		String response = null;
		boolean statusForInet4 = false;
		boolean statusForInet6 = false;
		String errorMessage = "Failed to validate brlan0 configuration";
		response = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.IFCONFIG_BRLAN);

		if (CommonMethods.isNotNull(response)) {
			if ((CommonMethods.patternMatcher(response, BroadBandTestConstants.INET_V4_ADDRESS_PATTERN))) {
				statusForInet4 = true;
			}

			if ((CommonMethods.patternMatcher(response, BroadBandTestConstants.INET_V6_ADDRESS_PATTERN))) {
				statusForInet6 = true;
			}
		} else {
			errorMessage += "Null response obtained for brlan0 configuration using command /sbin/ifconfig brlan0";
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}
		LOGGER.debug("ENDING METHOD :validateBrlan0Configuration");
		return statusForInet4 && statusForInet6;
	}

	/**
	 * 
	 * Method to read iptable rules stored in particular location in Device and
	 * validate them with the current rules
	 * 
	 * @param device                       instance of {@link Dut}
	 * @param automaticsTapApi             instance of {@link AutomaticsTapApi}
	 * @param locationForInitialIpV4Tables -- location where the ipv4 table rules
	 *                                     are stored initially
	 * @param locationForInitialIpV6Tables -- location where the ipv6 table rules
	 *                                     are stored initially
	 * @return boolean status of validation
	 * 
	 * @author revanth.k
	 * @refactor Athira
	 */
	public static boolean getIptableRulesAndVerify(Dut device, AutomaticsTapApi tapEnv,
			String locationForInitialIpV4Tables, String locationForInitialIpV6Tables) {

		LOGGER.debug("STARTING METHOD : getIptableRulesAndVerify");
		String ipv4TablesInitial = null;
		String ipv4TablesFinal = null;
		String ipv6TablesInitial = null;
		String ipv6TablesFinal = null;
		boolean statusForIpv4TablesValidation = false;
		boolean statusForIpv6TablesValidation = false;
		boolean status = false;
		String errorMessage = null;

		// generating current ip rules and storing in a different location
		status = dumpIpTablesToGivenLocation(device, tapEnv,
				BroadBandTestConstants.FILE_NAME_TO_STORE_FINAL_IPV4_TABLE_RULES,
				BroadBandTestConstants.FILE_NAME_TO_STORE_FINAL_IPV6_TABLE_RULES);
		if (status) {
			status = false;
			// uniq command is used to avoid repeated rules in the text file
			ipv4TablesInitial = tapEnv.executeCommandUsingSsh(device,
					BroadBandCommandConstants.COMMAND_UNIQ + locationForInitialIpV4Tables);
			ipv4TablesFinal = tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.COMMAND_UNIQ
					+ BroadBandTestConstants.FILE_NAME_TO_STORE_FINAL_IPV4_TABLE_RULES);
			ipv6TablesInitial = tapEnv.executeCommandUsingSsh(device,
					BroadBandCommandConstants.COMMAND_UNIQ + locationForInitialIpV6Tables);
			ipv6TablesFinal = tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.COMMAND_UNIQ
					+ BroadBandTestConstants.FILE_NAME_TO_STORE_FINAL_IPV6_TABLE_RULES);
			statusForIpv4TablesValidation = validateIpRules(ipv4TablesInitial, ipv4TablesFinal);
			if (!statusForIpv4TablesValidation) {
				errorMessage += "Failed to validate ipv4 rules";
			}
			LOGGER.info("Status For validating ipv4 rules -" + statusForIpv4TablesValidation);
			statusForIpv6TablesValidation = validateIpRules(ipv6TablesInitial, ipv6TablesFinal);
			if (!statusForIpv6TablesValidation) {
				errorMessage += "Failed to validate ipv6 rules";
			}
			LOGGER.info("Status For validating ipv6 rules -" + statusForIpv6TablesValidation);
			status = statusForIpv4TablesValidation && statusForIpv6TablesValidation;
			if (!status) {
				throw new TestException(errorMessage);
			}
		}
		LOGGER.debug("ENDING METHOD : getIptableRulesAndVerify");
		return status;
	}

	/**
	 * Method to iterate through two different iptable rules passed as arguments and
	 * compare them
	 * 
	 * @param IptableInitial
	 * @param IptableFinal   --- two ip table rules which are to be compared
	 * @return boolean true if the two ip tables are identical except at logs which
	 *         involve time and Rules which involve no.of packets transferred
	 * 
	 * @author revanth.k
	 * @refactor Athira
	 */

	public static boolean validateIpRules(String IptableInitial, String IptableFinal) {
		LOGGER.debug("STARTING METHOD: validateIpRules");
		boolean status = true;
		// all rules which do not involve dates start with
		Pattern pattern = Pattern.compile("(-A.*)");
		Matcher matcherForInitialTable = pattern.matcher(IptableInitial);
		Matcher matcherForFinalTable = pattern.matcher(IptableFinal);
		int matchedStringCount = 1;
		while (matcherForInitialTable.find() && matcherForFinalTable.find()) {

			LOGGER.info("in while loop " + matchedStringCount);
			LOGGER.info(matcherForFinalTable.group(AutomaticsConstants.CONSTANT_1));
			LOGGER.info(matcherForInitialTable.group(AutomaticsConstants.CONSTANT_1));
			if (!(matcherForFinalTable.group(AutomaticsConstants.CONSTANT_1)
					.equalsIgnoreCase(matcherForInitialTable.group(AutomaticsConstants.CONSTANT_1)))) {
				status = false;
				LOGGER.error("Mismatch found while verifying the IP rules");
				LOGGER.error("Line number " + matchedStringCount);
				LOGGER.error("Initial value :   " + matcherForInitialTable.group(AutomaticsConstants.CONSTANT_1));
				LOGGER.error("Final value :   " + matcherForFinalTable.group(AutomaticsConstants.CONSTANT_1));
				break;
			}

			matchedStringCount++;
		}

		LOGGER.debug("ENDING METHOD: validateIpRules");
		return status;

	}

	/**
	 * Utility method to validate whether device is in Docsis operational mode or
	 * not using WebPA command.
	 * 
	 * @param tapEnv The {@link AutomaticsTapApi} instance.
	 * @param device The device to be validated.
	 * @return true if device is in DOCSIS mode else false.
	 */
	public static boolean verifyDeviceInDocsisModeStatusUsingWebPaCommand(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("STARTING METHOD: verifyDeviceInDocsisModeStatusUsingWebPaCommand()");

		// status
		boolean isDOCSISMode = false;

		String response = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_OPERATIONAL_MODE);

		if (CommonMethods.isNotNull(response)) {
			isDOCSISMode = response.contains(BroadBandTestConstants.STRING_DEVICE_OPERATIONAL_MODE_DOCSIS);
		} else {
			LOGGER.error(
					"Null response obtained for webpa get on parameter Device.DeviceInfo.X_RDKCENTRAL-COM_EthernetWAN.CurrentOperationalMode");
			throw new TestException(
					"Null response obtained for webpa get on parameter Device.DeviceInfo.X_RDKCENTRAL-COM_EthernetWAN.CurrentOperationalMode");
		}
		LOGGER.debug("ENDING METHOD: verifyDeviceInDocsisModeStatusUsingWebPaCommand()");
		return isDOCSISMode;
	}

	/**
	 * Utility method to verify the specific partner ID is available or not
	 * 
	 * @param partnerIdToVerify Actual partnerID retrieved from script
	 * 
	 * @return Boolean representing the result of the validation.
	 * @author Athira
	 */
	public static boolean verifySpecificPartnerAvailability(String partnerIdToVerify) {
		LOGGER.info("STARTING METHOD: verifySpecificPartnerAvailability");
		boolean partnerIDExists = false;
		String partnerID = BroadbandPropertyFileHandler.getSecificPartnerIDValues();
		LOGGER.info("Partner id from propery file " + partnerID);
		LOGGER.info("ACTUAL Partner id  " + partnerIdToVerify);

		if (partnerID.equalsIgnoreCase(partnerIdToVerify)) {
			partnerIDExists = true;
		}
		LOGGER.info("Value of partnerIDExists is " + partnerIDExists);
		LOGGER.info("ENDING METHOD: verifySpecificPartnerAvailability");
		return partnerIDExists;

	}

	/**
	 * Method to fetch self heal configuration values and verify with expected
	 * values
	 * 
	 * @param selfHealConfiguration - Hash map with self heal parameters and its
	 *                              values
	 * @param selfHealParameter     - self heal params
	 * @param expectedValue         - expected value for self heal params
	 * 
	 * @return true if self heal params contains expected values
	 * 
	 * @author gnanaprakasham.s
	 * 
	 **/
	public static boolean validateSelfHealParameterValues(HashMap<String, String> selfHealConfiguration,
			String selfHealParameter, String expectedValue) {

		LOGGER.debug("STARTING METHOD: validateSelfHealParameterValues()");

		// Variable to store command response
		String response = null;
		// Variable to store status
		boolean status = false;
		// String variable to store error message
		String errorMessage = null;

		response = selfHealConfiguration.get(selfHealParameter);

		if (CommonMethods.isNotNull(response)) {

			status = response.equalsIgnoreCase(expectedValue);
			if (!status) {

				errorMessage = "Failed to get the value for " + selfHealParameter + " as " + expectedValue
						+ " Expected Value should be " + expectedValue + " But Actual obtained value is :" + response;
				LOGGER.error(errorMessage);
			}
			LOGGER.info("SUCCESSFULLY OBTAINED VALUE AS " + expectedValue + " FOR " + selfHealParameter
					+ "EXPECTED VALUE :" + expectedValue + "ACTUAL OBTAINED VALUE :" + response);
		} else {

			errorMessage = "Obtained null response. Not able to get the value for " + selfHealParameter;
			throw new TestException(errorMessage);

		}
		LOGGER.debug("ENDING METHOD: validateSelfHealParameterValues()");
		return status;
	}

	/**
	 * Method to retrieve self heal configuration values using SNMP mib
	 * 
	 * @param device the Dut instance to be tested
	 * @param tapEnv The AutomaticsTapApi instance
	 * 
	 * @return hash map with self heal parameter and its values
	 * @author gnanaprakasham.s
	 * @Refactor Athira
	 */
	public static HashMap<String, String> retrieveSelfHealparameterValues(Dut device, AutomaticsTapApi tapEnv) {

		LOGGER.debug("STARTING METHOD: retrieveSelfHealparameterValues()");

		// Hash map to store self heal configuration
		HashMap<String, String> selfHealConfiguration = new HashMap<>();
		String errorMessage = null;
		String response = null;
		response = BroadBandSnmpUtils.executeSnmpWalkOnRdkDevices(tapEnv, device,
				BroadBandSnmpMib.ECM_SELF_HEAL_CONFIGURATION.getOid());

		LOGGER.info("Obtained response for SNMP execution to retrieve self heal parameters : " + response);

		if (CommonMethods.isNotNull(response) && !response.contains(BroadBandTestConstants.NO_SUCH_INSTANCE)
				&& !response.contains(BroadBandTestConstants.NO_SUCH_OBJECT_AVAILABLE)) {

			LOGGER.info(" SUCCESSFULLY OBTAINED SELF HEAL CONFIGURATION DETAILS : " + response);
			// created hash map with self heal parameters and its values
			selfHealConfiguration = createMapwithSelfHealParametersAndValues(response);

		} else {

			errorMessage = "Obtained null response/No such oid exists during SNMP execution for retrieving default self heal configuration ";
			LOGGER.error(errorMessage);
			// throw new TestException(errorMessage);
		}
		LOGGER.debug("ENDING METHOD: retrieveSelfHealparameterValues()");
		return selfHealConfiguration;

	}

	/**
	 * Method to create map with self heal parameters and its values
	 * 
	 * @param defaultSelfHealConfiguration - Default Self heal configuration values
	 * 
	 * @return HashMap with self heal parameters with values
	 * 
	 * @author gnanaprakasham.s
	 **/

	public static HashMap<String, String> createMapwithSelfHealParametersAndValues(
			String defaultSelfHealConfiguration) {

		LOGGER.debug("STARTING METHOD: createMapwithSelfHealParametersAndValues()");

		HashMap<String, String> selfHealConfiguration = new HashMap<>();
		String pattern = ".0 = (\\d+)";
		/** String array to hold self heal parameters */
		String[] selfHealParameters = { AutomaticsConstants.EMPTY_STRING,
				BroadBandTestConstants.STRING_SELF_HEAL_ENABLED_STATUS,
				BroadBandTestConstants.STRING_NUMBER_PINGS_PER_SERVER_FOR_SELF_HEAL,
				BroadBandTestConstants.SELF_HEAL_MINIMUM_NUMBER_OF_PING_SERVER,
				BroadBandTestConstants.STRING_PING_INTERVAL_FOR_SELF_HEAL,
				BroadBandTestConstants.STRING_RESET_COUNT_FOR_SELF_HEAL, AutomaticsConstants.EMPTY_STRING,
				BroadBandTestConstants.STRING_RESOURCE_USAGE_FOR_SELF_HEAL,
				BroadBandTestConstants.STRING_AVG_CPU_THRESHOLD_FOR_SELF_HEAL,
				BroadBandTestConstants.STRING_AVG_MEMORY_THRESHOLD_FOR_SELF_HEAL,
				BroadBandTestConstants.STRING_MAXIMUM_REBOOT_COUNT_FOR_SELF_HEAL,
				BroadBandTestConstants.STRING_MAXIMUM_SUB_SYSTEM_RESET_COUNT_FOR_SELF_HEAL };

		if (CommonMethods.isNotNull(defaultSelfHealConfiguration)) {

			for (int iteration = 1; iteration < selfHealParameters.length; iteration++) {

				String response = CommonMethods.patternFinder(defaultSelfHealConfiguration,
						BroadBandTestConstants.PATTERN_GET_SELF_HEAL_PArAMETER_VALUES + iteration + pattern);

				if (CommonMethods.isNotNull(response)) {

					selfHealConfiguration.put(selfHealParameters[iteration], response);

				}

			}

		}
		LOGGER.debug("ENDING METHOD: createMapwithSelfHealParametersAndValues()");
		return selfHealConfiguration;
	}

	/**
	 * helper method that executes the command in atom console if available else
	 * runs the command in arm console and returns the response
	 * 
	 * @param device  device in which command is to be executed
	 * @param tapApi  AutomaticsTapApi instance
	 * @param command command to be executed in device
	 * @return response of the command
	 * 
	 */
	public static String executeCommandInAtomConsoleIfAtomIsPresentElseInArm(Dut device, AutomaticsTapApi tapApi,
			String command) {

		LOGGER.debug("STARTING METHOD: executeCommandInAtomConsoleIfAtomIsPresentElseInArm");
		String response = null;
		boolean isAtomSyncAvailable = CommonMethods.isAtomSyncAvailable(device, tapApi);
		if (isAtomSyncAvailable) {
			response = tapApi.executeCommandOnAtom(device, command);
		} else {
//			response = tapApi.executeCommandUsingSsh(device, command);
			response = tapApi.executeCommandUsingSsh(device, command, BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS);
		}
		LOGGER.debug("ENDING METHOD: executeCommandInAtomConsoleIfAtomIsPresentElseInArm");
		return response;
	}

	/**
	 * Method to check whether Log file is available in rdklogs/logs and tail them
	 * to Backup<Filename> in nvram in given consoletype
	 * 
	 * @param device              Dut Instance
	 * @param tapEnv              AutomaticsTapApi Instance
	 * @param mustHaveLogFileList List of Log files to Log
	 * @param polledTime          polled duration
	 * @param maxDuration         Max Duration to poll check
	 * @param consoleType         type of console
	 * @param tailvalue           number of lines
	 * @return BroadBandResultObject
	 */
	public static Map<String, String> verifyLogAlbltyAndTailLogToNvramInGivenConsole(Dut device,
			AutomaticsTapApi tapEnv, List<String> mustHaveLogFileList, String tailValue, long polledTime,
			long maxDuration, String consoleType) {
		LOGGER.debug("STARTING verifyLogAlbltyAndTailLogToNvramInGivenConsole()");
		long startTime = System.currentTimeMillis();
		boolean result = false;
		String errorMessage = "";

		Map<String, String> mapForLogFileWithPath = new HashMap<>();
		String pathForBackupFile;
		try {
			String command = BroadBandCommandConstants.TAIL_GIVEN_LOG_FILE_WITH_PATH
					.replace(BroadBandTestConstants.STRING_VALUE_TO_REPLACE, tailValue);
			String commandToExecute;
			Map<String, Boolean> logFilesUpdate = new HashMap<>();
			for (String logFile : mustHaveLogFileList) {
				logFilesUpdate.put(logFile, false);
			}
			List<String> missingLogFiles = mustHaveLogFileList;
			boolean isAtomSyncAvailable = CommonMethods.isAtomSyncAvailable(device, tapEnv);
			do {
				// Identifying whether log files availble or not
				if (consoleType.equals(BroadBandTestConstants.ARM)) {
					missingLogFiles = BroadBandCommonUtils.verifyMustHaveLogFilesAvailabilityinAtomOrArm(device, tapEnv,
							mustHaveLogFileList, false);
				} else {
					missingLogFiles = BroadBandCommonUtils.verifyMustHaveLogFilesAvailabilityinAtomOrArm(device, tapEnv,
							mustHaveLogFileList, isAtomSyncAvailable);
				}
				result = missingLogFiles.isEmpty();
				mustHaveLogFileList.removeAll(missingLogFiles);
				if (!mustHaveLogFileList.isEmpty()) {
					for (String logFile : mustHaveLogFileList) {
						pathForBackupFile = BroadBandCommandConstants.PATH_FOR_BACK_UP_FILE
								.replace(BroadBandTestConstants.STRING_REPLACE, logFile);
						commandToExecute = command.replace(BroadBandTestConstants.STRING_REPLACE, logFile)
								.replace(BroadBandTestConstants.REPLACE_BACKUP_FILE, pathForBackupFile);
						if (!logFilesUpdate.get(logFile)) {
							if (consoleType.equals(BroadBandTestConstants.STRING_ATOM_CONSOLE)) {
								tapEnv.executeCommandOnAtom(device, commandToExecute);
							} else if (consoleType.equals(BroadBandTestConstants.ARM)) {
								tapEnv.executeCommandUsingSsh(device, commandToExecute);
							} else {
								if (isAtomSyncAvailable) {
									tapEnv.executeCommandOnAtom(device, commandToExecute);
								} else {
									tapEnv.executeCommandUsingSsh(device, commandToExecute);
								}
							}
							logFilesUpdate.put(logFile, true);
							mapForLogFileWithPath.put(logFile, pathForBackupFile);
						}
					}
				}
				mustHaveLogFileList = missingLogFiles;
				LOGGER.info("Successfully verified all the must have log files are present : " + result);
			} while (!result && ((System.currentTimeMillis() - startTime) < maxDuration)
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, polledTime));

			errorMessage = result ? "Successfully verified all the must have log files are present"
					: "Some files are missing -> " + missingLogFiles;

		} catch (Exception exception) {
			errorMessage = "Exception occured while verifying logavailability  " + exception.getMessage();
			LOGGER.error(errorMessage);
		}
		LOGGER.info("ENDING METHOD:  verifyLogAlbltyAndTailLogToNvramInGivenConsole()");
		return mapForLogFileWithPath;
	}

	/**
	 * Method to delete temporary backup files
	 * 
	 * @param device
	 * @param tapEnv
	 * @param logFilesToRemove
	 * @return
	 */
	public static boolean deleteTemporaryFilesInNvram(Dut device, AutomaticsTapApi tapEnv,
			List<String> logFilesToRemove) {
		LOGGER.debug("STARTING METHOD : deleteTemporaryFilesInNvram");
		List<String> logFileList = new ArrayList<>();
		boolean status = false;
		List<Boolean> statusOfDelete = new ArrayList<>();
		for (String tempLogFile : logFilesToRemove) {
			logFileList.add(
					CommonMethods.concatStringUsingStringBuffer(BroadBandTestConstants.STRING_BACKUP, tempLogFile));
		}
		for (String logFile : logFileList) {
			boolean logFileExists = CommonUtils.isFileExists(device, tapEnv,
					CommonMethods.concatStringUsingStringBuffer(BroadBandCommandConstants.NAVIGATE_GIVEN_FILE_IN_NVRAM
							.replace(BroadBandTestConstants.STRING_REPLACE, logFile)));
			if (logFileExists) {
				status = CommonUtils.deleteFile(device, tapEnv,
						CommonMethods
								.concatStringUsingStringBuffer(BroadBandCommandConstants.NAVIGATE_GIVEN_FILE_IN_NVRAM
										.replace(BroadBandTestConstants.STRING_REPLACE, logFile)));
				statusOfDelete.add(status);
			}
		}
		status = !statusOfDelete.contains(BroadBandTestConstants.BOOLEAN_VALUE_FALSE);
		LOGGER.debug("ENDING METHOD : deleteTemporaryFilesInNvram");
		return status;
	}

	/**
	 * Utility method to get eRouter ipv6 address from the device
	 * 
	 * @param device The device under test
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @return The erouteripv6 address
	 */
	public static String getErouteripv6Address(Dut device, AutomaticsTapApi tapEnv) {

		String eRouteripv6 = null;
		String pattern = null;
		ifConfigErouter0Response = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.IFCONFIG_EROUTER);
		if (!DeviceModeHandler.isDSLDevice(device)) {
			pattern = BroadBandTestConstants.INET_V6_ADDRESS_PATTERN;
		} else {
			pattern = BroadBandTestConstants.INET_V6_ADDRESS_LINK_PATTERN;
		}

		LOGGER.info("PATTERN: " + pattern);
		if (CommonMethods.isNotNull(ifConfigErouter0Response)) {
			eRouteripv6 = CommonMethods.patternFinder(ifConfigErouter0Response, pattern);
		}

		return eRouteripv6;
	}

	/**
	 * Utility method to verify last reboot reason from '\rdklogs\logs\BootTime.log'
	 * file after factory resetting the device
	 * 
	 * @param device instance of {@link Dut}
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @return status true if string 'Received reboot_reason as:factory-reset' is
	 *         found in BootTime.log
	 * @author Praveenkumar Paneerselvam
	 * @Refactor Athira
	 */
	// Renaming the API from verifyFactoryResetReasonFromWebpaLog to
	// verifyFactoryResetReasonFromBootTimeLog

	public static boolean verifyFactoryResetReasonFromBootTimeLog(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD : verifyFactoryResetReasonFromBootTimeLog");
		boolean status = false;
		String response = null;
		long startTime = System.currentTimeMillis();
		LOGGER.info(
				"GOING TO POLL FOR MAX 10 min WITH 30 sec TIME INTERVAL FOR '/rdklogs/logs/BootTime.log' FILE GENERATION.");
		do {
			if (CommonUtils.isFileExists(device, tapEnv, BroadBandCommandConstants.FILE_BOOT_TIME_LOG)) {
				response = BroadBandCommonUtils.searchLogFiles(tapEnv, device,
						BroadBandTraceConstants.LOG_MESSAGE_REBOOT_REASON_FACTORY_RESET,
						BroadBandCommandConstants.FILE_BOOT_TIME_LOG);
				LOGGER.info("RESPONSE FROM '/rdklogs/logs/BootTime.log' IS : " + response);
				status = CommonMethods.isNotNull(response);
			}
		} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.TEN_MINUTE_IN_MILLIS && !status
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
		LOGGER.info(
				"IS FILE '/rdklogs/logs/BootTime.log' EXISTS TO VALIDATE THE LAST REBOOT REASON AFTER FACTORY RESET : "
						+ status);

		LOGGER.info("IS 'Received reboot_reason as:factory-reset' STRING FOUND IN '/rdklogs/logs/BootTime.log' FILE : "
				+ status);
		LOGGER.debug("ENDING METHOD : verifyFactoryResetReasonFromBootTimeLog");
		return status;
	}

	/**
	 * Utility method to get LAN end IP for partners
	 * 
	 * @param partnerId Actual partnerID retrieved from script
	 * 
	 * @return String LAN end IP
	 * @author Athira
	 */
	public static String getSynEndIPforPartner(String partnerId) {
		LOGGER.info("STARTING METHOD: getSynEndIPforPartner");
		String propKeySynEndIP = BroadBandPropertyKeyConstants.PROP_KEY_SYN_END_IP_ADDRESS + partnerId;
		String SynEndIP = AutomaticsTapApi.getSTBPropsValue(propKeySynEndIP);
		LOGGER.info("propKeySynEndIP is " + propKeySynEndIP);
		LOGGER.info("SynEndIP is " + SynEndIP);
		LOGGER.info("ENDING METHOD: getSynEndIPforPartner");
		return SynEndIP;

	}

	/**
	 * Utility method to get LAN start IP for partners
	 * 
	 * @param partnerId Actual partnerID retrieved from script
	 * 
	 * @return String LAN Start IP
	 * @refactor Athira
	 */
	public static String getSynStartIPforPartner(String partnerId) {
		LOGGER.info("STARTING METHOD: getSynStartIPforPartner");
		String propKeySycStartIP = BroadBandPropertyKeyConstants.PROP_KEY_SYN_START_IP_ADDRESS + partnerId;
		String SynStartIP = AutomaticsTapApi.getSTBPropsValue(propKeySycStartIP);
		LOGGER.info("propKeySycStartIP is " + propKeySycStartIP);
		LOGGER.info("SynStartIP is " + SynStartIP);
		LOGGER.info("ENDING METHOD: getSynStartIPforPartner");
		return SynStartIP;

	}

	/**
	 * Utility method to get LAN local IP for partners
	 * 
	 * @param partnerId Actual partnerID retrieved from script
	 * 
	 * @return String LAN Local IP
	 * @refactor Athira
	 */
	public static String getSynLocalIPforPartner(String partnerId) {
		LOGGER.info("STARTING METHOD: getSynLocalIPforPartner");
		String propKeySynLocalIP = BroadBandPropertyKeyConstants.PROP_KEY_SYN_LAN_LOCAL_IP + partnerId;
		String SynLocalIP = AutomaticsTapApi.getSTBPropsValue(propKeySynLocalIP);
		LOGGER.info("propKeySynLocalIP is " + propKeySynLocalIP);
		LOGGER.info("SynLocalIP is " + SynLocalIP);
		LOGGER.info("ENDING METHOD: getSynLocalIPforPartner");
		return SynLocalIP;

	}

	/**
	 * Utility method to perform factory reset through SNMP and verify it in
	 * '/rdklogs/logs/BootTime.log' file
	 * 
	 * @param device instance of {@link Dut}
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @return status true, if factory reset is successfully performed using SNMP
	 *         and also reboot reason appears as 'factory reset' in
	 *         '/rdklogs/logs/BootTime.log' file
	 */
	// Renaming the API from performFactoryUsingSNMPAndVerifyInWebPaLog to
	// performFactoryUsingSNMPAndVerifyInBootTimeLog

	public static boolean performFactoryUsingSNMPAndVerifyInBootTimeLog(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD : performFactoryUsingSNMPAndVerifyInBootTimeLog");
		boolean status = BroadBandCommonUtils.performFactoryResetSnmp(tapEnv, device);
		LOGGER.info("Is Factory Reset success through SNMP : " + status);
		if (status) {
			status = BroadBandCommonUtils.verifyFactoryResetReasonFromBootTimeLog(device, tapEnv);
		}
		LOGGER.debug("ENDING METHOD : performFactoryUsingSNMPAndVerifyInBootTimeLog");
		return status;
	}

	/**
	 * Utility method to perform Factory Reset on the device using SNMP & then wait
	 * for the device to comeup.
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * 
	 * @return Boolean representing the result of the Factory Reset Operation.
	 */
	public static boolean performFactoryResetSnmp(AutomaticsTapApi tapEnv, Dut device) {

		LOGGER.debug("ENTERING METHOD performFactoryResetSnmp");

		String snmpOutput = null;
		if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
			BroadBandCommonUtils.getAtomSyncUptimeStatus(device, tapEnv);
		}
		if (DeviceModeHandler.isFibreDevice(device)) {

			snmpOutput = BroadBandSnmpUtils
					.snmpSetOnEstb(tapEnv, device, BroadBandSnmpMib.ESTB_FACTORY_RESET_DEVICE.getOid(),
							SnmpDataType.INTEGER, BroadBandTestConstants.STRING_VALUE_ONE)
					.trim();

		} else {
			snmpOutput = BroadBandSnmpUtils
					.snmpSetOnEcm(tapEnv, device, BroadBandSnmpMib.ESTB_FACTORY_RESET_DEVICE.getOid(),
							SnmpDataType.INTEGER, BroadBandTestConstants.STRING_VALUE_ONE)
					.trim();
		}

		boolean result = CommonMethods.isNotNull(snmpOutput)
				&& snmpOutput.equals(BroadBandTestConstants.STRING_VALUE_ONE);
		if (result) {
			// Check if the device goes down.
			long pollDuration = BroadBandTestConstants.EIGHT_MINUTE_IN_MILLIS;
			long startTime = System.currentTimeMillis();
			do {
				LOGGER.info("GOING TO WAIT FOR 1 MINUTE.");
				tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
				result = !CommonMethods.isSTBAccessible(device);
			} while ((System.currentTimeMillis() - startTime) < pollDuration && !result);
		}
		LOGGER.info("DEVICE REBOOTS AFTER TRIGGERING FACTORY RESET: " + result);

		long startTime = 0L;
		if (result) {
			startTime = System.currentTimeMillis();
			do {
				LOGGER.info("GOING TO WAIT FOR 1 MINUTE.");
				tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
				result = CommonMethods.isSTBAccessible(device);
			} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIFTEEN_MINUTES_IN_MILLIS
					&& !result);
			LOGGER.info("DEVICE COMES UP AFTER FACTORY RESET: " + result);
		}
		if (result) {
			result = verifyFactoryReset(tapEnv, device);
		}
		LOGGER.info("BROAD BAND DEVICE FACTORY RESET (SNMP) PERFORMED SUCCESSFULLY: " + result);
		LOGGER.debug("ENDING METHOD performFactoryResetSnmp");
		return result;
	}

	/**
	 * Utility Method to verify the Factory Reset is successful; verified using the
	 * Private SSID which must contain the last 4 digits of ECM MAC Address of the
	 * device.
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * 
	 * @return Boolean representing the result of the validation; TRUE if the
	 *         factory reset is successful; else FALSE.
	 * @author Govardhan
	 */
	public static boolean verifyFactoryReset(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.info("STARTING METHOD verifyFactoryReset");
		boolean result = false;
		tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
		String privateSsid = null;
		long startTime = System.currentTimeMillis();
		do {
			LOGGER.info("GOING TO WAIT FOR 30 SECONDS.");
			tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
			privateSsid = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID);
			LOGGER.info("Private Ssid is : " + privateSsid);
			BroadBandResultObject resultObject = BroadBandRestoreWifiUtils.verifyDefaultSSIDForPartner(device,
					WiFiFrequencyBand.WIFI_BAND_2_GHZ, tapEnv, false);
			result = resultObject.isStatus();
		} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.TEN_MINUTE_IN_MILLIS && !result);
		LOGGER.info("FACTORY RESET SUCCESSFUL: " + result);
		LOGGER.info("ENDING METHOD verifyFactoryReset");
		return result;
	}

	/**
	 * Utility method to Validate defualt SSId for various Partners
	 * 
	 * @param devive      Dut under test
	 * @param tapEnv      {@link AutomaticsTapApi}
	 * @param ssidValue   String ssid obtained by webpa command
	 * @param parameter   parameter for Band of the Wi-Fi
	 * @param deviceModel String instance for Device Model
	 * @param PartnerId   String instance of Partener ID for which Expected SSID
	 *                    varies
	 * @param radio       {@link WiFiFrequencyBand}
	 * @return boolean true if the ssid is as expected
	 * 
	 * 
	 */
	public static boolean validateDefaultSsidforDifferentPartners(Dut device, AutomaticsTapApi tapEnv, String ssidValue,
			RdkBSsidParameters parameter, String deviceModel, String partnerId, WiFiFrequencyBand radio) {
		LOGGER.debug("STARTING METHOD validateDefaultSsidforDifferentPartners");
		boolean status = false;
		String expectedSsid = null;
		String prefix = null;
		String serialNumber = null;
		try {
			if (DeviceModeHandler.isDSLDevice(device)) {
				if (ssidValue.length() == BroadBandTestConstants.EIGHT_NUMBER
						|| ssidValue.length() == BroadBandTestConstants.INTEGER_VALUE_13) {
					expectedSsid = ssidValue;
				}
			}
			if (DeviceModeHandler.isRPIDevice(device)) {
				if (radio.equals((WiFiFrequencyBand.WIFI_BAND_2_GHZ))) {
					expectedSsid = BroadbandPropertyFileHandler.getDefaultSsid24AfterFR();
					status = ssidValue.equalsIgnoreCase(expectedSsid);
				} else
					expectedSsid = BroadbandPropertyFileHandler.getDefaultSsid5AfterFR();
				status = ssidValue.equalsIgnoreCase(expectedSsid);
			}

			else {
				prefix = getDefaultSSIDPrefixForDeviceAndPartnerSpecific(device, partnerId);
				LOGGER.info("Prefix for The SSID Specific To Device is: " + prefix);
				String ecmMac = ((Device) device).getEcmMac();
				try {
					serialNumber = tapEnv
							.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_SERIAL_NUMBER)
							.trim();
				} catch (Exception exception) {
					LOGGER.error("Caught exception while getting the serial Number by WebPa" + exception.getMessage());
				}
				ecmMac = ecmMac.replace(BroadBandTestConstants.DELIMITER_COLON, BroadBandTestConstants.EMPTY_STRING);
				LOGGER.info("ecmMAC of the device is: " + ecmMac);
				expectedSsid = getExpectedSSID(device, partnerId, prefix, serialNumber, ecmMac,
						parameter.getWifiBand());
			}
			LOGGER.info("Expected SSID is: " + expectedSsid);
			status = ssidValue.equalsIgnoreCase(expectedSsid);
			LOGGER.info("Is Expected and Actual SSID are equal :" + status);
			// Applicable only for Atom based device

			if (!status) {
				String verifyPartnerID = BroadbandPropertyFileHandler.getPartnerIDandModelValues();
				JSONObject json = new JSONObject(verifyPartnerID);
				String partnerIDFromProperty = json.getString(partnerId);
				String deviceModelFromProperty = json.getString(deviceModel);

				if (partnerIDFromProperty == partnerId && deviceModelFromProperty == device.getModel()) {
					String webpaParameter = null;
					if (radio.equals((WiFiFrequencyBand.WIFI_BAND_2_GHZ))) {
						webpaParameter = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_MAC_ADDRESS;
					} else if (radio.equals((WiFiFrequencyBand.WIFI_BAND_5_GHZ))) {
						webpaParameter = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_MAC_ADDRESS;
					}
					String wifiMAC = tapEnv.executeWebPaCommand(device, webpaParameter);
					LOGGER.info("wifiMAC  -" + wifiMAC);
					wifiMAC = wifiMAC.replace(BroadBandTestConstants.DELIMITER_COLON,
							BroadBandTestConstants.EMPTY_STRING);
					expectedSsid = wifiMAC.substring(wifiMAC.length() - BroadBandTestConstants.CONSTANT_6);
					LOGGER.info("wifiMAC last 6 digits -" + expectedSsid);
					if (CommonMethods.isNotNull(ssidValue) && CommonMethods.isNotNull(expectedSsid)) {
						status = ssidValue.equalsIgnoreCase(expectedSsid);
						LOGGER.info("Is Expected and Actual SSID are equal :" + status);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(
					"Caught exception while Validating the default SSid for Different Partners : " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD validateDefaultSsidforDifferentPartners");
		return status;
	}

	/**
	 * Utility method to verify the expected partner ID is available or not
	 * 
	 * @param partnerIdToVerify Actual partnerID retrieved from script
	 * 
	 * @return Boolean representing the result of the validation.
	 * @author Govardhan
	 */
	public static boolean verifyPartnerAvailability(String partnerIdToVerify) {
		LOGGER.info("STARTING METHOD: verifyPartnerAvailability");
		boolean partnerIDExists = false;
		String partnerIDList = BroadbandPropertyFileHandler.getPartnerIDValues();

		String[] partnerIDValues = partnerIDList.split(",");

		for (String partnerID : partnerIDValues) {
			if (partnerID.equalsIgnoreCase(partnerIdToVerify)) {
				partnerIDExists = true;
				break;
			}
		}
		return partnerIDExists;
	}

	/**
	 * Utility method to compare all the matched pattern
	 * 
	 * @param device               instance of Dut
	 * @param listPattern          Pattern that is to match from the array list
	 * @param commandOutputPattern Pattern that is to match the command output
	 * 
	 * @return true if all the pattern matches
	 * 
	 * @author Karthick Pandiyan
	 */
	public static boolean compareAllTheMatchedPattern(String response, String listPattern,
			String commandOutputPattern) {
		LOGGER.debug("STARTING METHOD: compareAllTheMatchedPattern()");
		boolean status = false;
		try {
			if (CommonMethods.isNotNull(response)) {

				ArrayList<String> matchedList = CommonMethods.patternFinderToReturnAllMatchedString(response,
						commandOutputPattern);
				if (!matchedList.isEmpty()) {
					for (String entry : matchedList) {
						status = false;
						if (CommonMethods.patternMatcher(entry, listPattern)) {
							status = true;
						} else {
							LOGGER.info("Presence of parameter " + entry + " in the command output : " + status);
							break;
						}

						LOGGER.info("Presence of parameter " + entry + " in the command output : " + status);
					}
				}
				LOGGER.info("pattern matched string are - " + matchedList);
			}
		} catch (Exception e) {
			LOGGER.error("Caught exception while comparing all the matched pattern " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: compareAllTheMatchedPattern()");
		return status;
	}

	/**
	 * Utility method to validate the IP of namespace
	 * 
	 * @param response             Command output
	 * @param commandOutputPattern Pattern that is to match the command output
	 * 
	 * @return true if all the pattern matches
	 * 
	 * @author Alan_Bivera
	 */
	public static boolean validateNameServerIP(String response, String commandOutputPattern) {
		LOGGER.info("STARTING METHOD: validateNameServerIP()");
		boolean status = false;
		try {
			if (CommonMethods.isNotNull(response)) {

				ArrayList<String> matchedList = CommonMethods.patternFinderToReturnAllMatchedString(response,
						commandOutputPattern);
				if (!matchedList.isEmpty()) {
					for (String entry : matchedList) {
						status = false;
						LOGGER.info("entry = " + entry);

						if (CommonMethods.isIpv4Address(entry) || CommonMethods.isIpv6Address(entry)) {
							status = true;
						} else {
							LOGGER.info("Presence of parameter " + entry + " in the command output : " + status);
							break;
						}

						LOGGER.info("Presence of parameter " + entry + " in the command output : " + status);

					}
				}
				LOGGER.info("pattern matched string are - " + matchedList);
			}
		} catch (Exception e) {
			LOGGER.error("Caught exception while comparing all the matched pattern " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: validateNameServerIP()");
		return status;
	}

	/**
	 * Helper method to get Ip Address from given Interface
	 * 
	 * @param device        Dut instance
	 * @param tapEnv        AutomaticsTapApi instance
	 * @param interfaceName Name of the interface for which Ip should be fetched
	 * @param isIpv4        true, if ipv4, false for Ipv6
	 * @return Ip address of the given interface
	 * 
	 * @author Praveenkumar Paneerselvam
	 */
	public static String getIpAddressFromGivenInterface(Dut device, AutomaticsTapApi tapEnv, String interfaceName,
			boolean isIpv4) {
		LOGGER.debug("STARTING METHOD : getIpAddressFromGivenInterface");
		long startTime = System.currentTimeMillis();
		String response = null;
		Boolean isSupportedDevices = null;

		isSupportedDevices = BroadbandPropertyFileHandler.isDeviceSupported(device);
		do {
			if (BroadBandTestConstants.INTERFACE_NAME_MTA0.equalsIgnoreCase(interfaceName)
					&& (isSupportedDevices || DeviceModeHandler.isBusinessClassDevice(device))) {
				response = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_COMMAND_MTA_IP_OF_DEVICE);
				LOGGER.info("Response from webpa parameter is " + response);
				if (CommonMethods.isNotNull(response)) {
					response = (isIpv4 ? CommonMethods.isIpv4Address(response.trim())
							: CommonMethods.isIpv6Address(response.trim())) ? response : null;
				}
			} else {
				response = tapEnv.executeCommandUsingSsh(device,
						BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_IFCONFIG,
								BroadBandTestConstants.SINGLE_SPACE_CHARACTER, interfaceName));
				LOGGER.info("Response from ifconfig is " + response);
				if (CommonMethods.isNotNull(response)) {
					response = CommonMethods.patternFinder(response.trim(),
							isIpv4 ? BroadBandTestConstants.INET_V4_ADDRESS_PATTERN
									: BroadBandTestConstants.INET_V6_ADDRESS_PATTERN_ALTERNATE);
				}
			}
		} while (CommonMethods.isNull(response)
				&& (System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS);
		LOGGER.info("IP address for the interface " + interfaceName + " is " + response);
		LOGGER.debug("ENDING METHOD : getIpAddressFromGivenInterface");
		return response;
	}

	/**
	 * Common step to verify the ccspwifi process is up and running.
	 * 
	 * @param device          {@link Dut} Reference
	 * @param tapEnv          {@link AutomaticsTapApi} Reference
	 * @param isAtomConsole
	 * @param cmdToGetProcess Command to get the process response
	 * @param regexToGetPid   pattern matcher to get the process ID from the
	 *                        response
	 * @param status          It return true/false
	 */
	public static boolean verifyProcessId(Dut device, AutomaticsTapApi tapEnv, boolean isAtomConsole,
			String cmdToGetProcess, String regexToGetPid) {
		boolean status = false;
		LOGGER.debug("STARTING METHOD: verifyProcessId");
		try {
			String response = isAtomConsole ? CommonMethods.executeCommandInAtomConsole(device, tapEnv, cmdToGetProcess)
					: tapEnv.executeCommandUsingSsh(device, cmdToGetProcess);
			String processId = CommonMethods.isNotNull(response) ? CommonMethods.patternFinder(response, regexToGetPid)
					: null;
			status = CommonMethods.isNotNull(processId);
			LOGGER.info("PROCESS ID OBTAINED FOR " + cmdToGetProcess + " IS : " + (status ? processId : "NULL"));
		} catch (Exception exp) {
			LOGGER.error(
					"FOLLOWING EXCEPTION OCCURED IN BroadBandCommonUtils.verifyProcessId() API " + exp.getMessage());
		}
		LOGGER.debug("ENDING METHOD: verifyProcessId");
		return status;
	}

	/**
	 * Method to search list of patterns in a file on atom console
	 * 
	 * @param device      Dut to be used for execution
	 * 
	 * @param tapEnv      instance of {@link AutomaticsTapApi}
	 * 
	 * @param fileName    complete path of file to be searched
	 * 
	 * @param patternList list of patterns to be searched in file
	 * 
	 * @return result boolean value containing search operation result
	 * 
	 * @author Ashwin sankara
	 * @refactor Govardhan
	 */
	public static boolean searchAtomFileForPatterns(Dut device, AutomaticsTapApi tapEnv, String fileName,
			String... patternList) {
		LOGGER.debug("Entering method: searchAtomFileForPatterns");
		boolean result = false;
		String command = null;
		String response = null;
		for (String pattern : patternList) {
			command = concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND,
					BroadBandTestConstants.SYMBOL_QUOTES, pattern, BroadBandTestConstants.SYMBOL_QUOTES,
					BroadBandTestConstants.SINGLE_SPACE_CHARACTER, fileName);
			LOGGER.info("Command to be executed: " + command);
			response = CommonMethods.executeCommandInAtomConsole(device, tapEnv, command);
			LOGGER.info("Command response: " + response);
			result = CommonMethods.isNotNull(response) && CommonMethods.patternMatcher(response, pattern);
			LOGGER.info("Search result for " + pattern + ": " + result);
			if (!result) {
				break;
			}
		}
		LOGGER.debug("Exiting method: searchAtomFileForPatterns");
		return result;
	}

	/**
	 * Utility method to verify given file exists in the ATOM Console.
	 * 
	 * @param tapEnv           {@link AutomaticsTapApi}
	 * @param device           {@link Dut}
	 * @param completeFilePath String representing the complete path of the file.
	 * @author anandam.s
	 * 
	 * @return Object representing the BroadBandResultObject with status & message.
	 */
	public static BroadBandResultObject doesFileExistInAtomConsole(Dut device, AutomaticsTapApi tapEnv,
			String completeFilePath) {
		LOGGER.debug("STARTING METHOD doesFileExistInAtomConsole");
		String command = BroadBandCommandConstants.CMD_LS + completeFilePath;
		String response = CommonMethods.executeCommandInAtomConsole(device, tapEnv, command);
		LOGGER.info("COMMAND EXECUTION RESPONSE: " + response);
		boolean result = false;
		if (CommonMethods.isNotNull(response) && !response.contains(BroadBandTestConstants.NO_SUCH_FILE_OR_DIRECTORY)) {
			result = response.trim().equalsIgnoreCase(completeFilePath);
		}
		String errorMessage = result ? BroadBandTestConstants.EMPTY_STRING
				: "FILE " + completeFilePath + " IS NOT PRESENT IN THE ATOM CONSOLE.";
		LOGGER.info("IS FILE " + completeFilePath + " PRESENT IN ATOM CONSOLE: " + result);
		BroadBandResultObject objResult = new BroadBandResultObject();
		objResult.setStatus(result);
		objResult.setErrorMessage(errorMessage);
		LOGGER.debug("ENDING METHOD doesFileExistInAtomConsole");
		return objResult;
	}

	/**
	 * Utility method to obtain the uptime value from uptime commmand after reboot.
	 * 
	 * @param device instance of {@link Dut}
	 * @param tapApi instance of {@link AutomaticsTapApi}
	 * @return integer value of uptime in mins
	 * 
	 * @author Ashwin sankara
	 * @refactor Athira
	 */
	public static int getUptimeAfterReboot(Dut device, AutomaticsTapApi tapApi) throws Exception {
		LOGGER.debug("Entering method: getUptimeAfterReboot");
		String response = null;
		int uptime = BroadBandTestConstants.CONSTANT_0;
		response = tapApi.executeCommandUsingSsh(device, BroadBandTestConstants.CMD_UPTIME);
		if (CommonMethods.isNotNull(response)) {
			response = CommonMethods.patternFinder(response,
					BroadBandTestConstants.PATTERN_GET_MINUTES_FROM_UPTIME_RESPONSE);
			uptime = Integer.parseInt(response.trim());
			LOGGER.info("Uptime: " + uptime);
		} else {
			throw new TestException("Null response obtained for uptime command");
		}
		LOGGER.debug("Exiting method: getUptimeAfterReboot");
		return uptime;
	}

	/**
	 * Helper method to get partner value for web UI validation
	 * 
	 * @param device {@link Dut}
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @return the true if Atom Based Device uptime is greater than 20 mis
	 * @refactor Alan_Bivera
	 */
	public static boolean getAtomDeviceUptimeStatus(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("Starting of Method: getAtomDeviceUptimeStatus");
		String response = null;
		int uptimeInMin = 0;
		boolean status = false;
		long startTime = System.currentTimeMillis();
		String errorMessage = null;
		try {

			do {
				errorMessage = "Webpa process is not up";
				response = tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_UPTIME);
				LOGGER.info("Device uptime " + response);
				status = CommonMethods.isNotNull(response);
				if (status) {
					errorMessage = "Device uptime is not greater than 20 mins";
					status = false;
					uptimeInMin = Integer.parseInt(response) / BroadBandTestConstants.CONSTANT_60;
					if (!(uptimeInMin >= BroadBandTestConstants.CONSTANT_20)) {
						BroadBandCommonUtils.hasWaitForDuration(tapEnv,
								(BroadBandTestConstants.CONSTANT_20 - uptimeInMin) * 60000);

						uptimeInMin = Integer.parseInt(
								tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_UPTIME))
								/ BroadBandTestConstants.CONSTANT_60;
						if (uptimeInMin >= BroadBandTestConstants.CONSTANT_20) {
							status = true;
							LOGGER.info("Device uptime is greater than 20 mins " + status);
						}
					} else {
						status = true;
						LOGGER.info("Device uptime is greater than 20 mins " + status);
					}

				}
			} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.EIGHT_MINUTE_IN_MILLIS
					&& !status);
			if (!status) {
				throw new TestException(
						"Exception occured : Webpa process is not up/ Device uptime is not greater than 20 mins "
								+ errorMessage);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while retrieving partner value " + e.getMessage());
		}
		LOGGER.debug("Ending of Method: getAtomDeviceUptimeStatus");
		return status;
	}

	/**
	 * Method used to verify the syndicate partner id on device
	 * 
	 * @param device {@link Dut}
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @return True- If device with partners, Else- False
	 */
	public static boolean verifySyndicatePartnerIdOnDevice(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD : verifySyndicatePartnerIdOnDevice");
		boolean status = false;
		boolean isPartnerSpecificDevice = false;
		Boolean isSupportedDevices = null;

		isSupportedDevices = BroadbandPropertyFileHandler.isDeviceSupported(device);

		try {
			isPartnerSpecificDevice = (CommonMethods.isAtomSyncAvailable(device, tapEnv) || isSupportedDevices);
			if (isPartnerSpecificDevice) {

				String currentPartnerIdName = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_PARAM_FOR_SYNDICATION_PARTNER_ID);
				LOGGER.info("Current Partner ID from Device : " + currentPartnerIdName);
				status = BroadBandCommonUtils.verifySyndicationPartnerAvailability(currentPartnerIdName);
			}
		} catch (Exception exception) {
			LOGGER.error("Exception occurrd while ping wan ip address from jump server : " + exception.getMessage());
		}
		LOGGER.debug("ENDING METHOD : verifySyndicatePartnerIdOnDevice");
		return status;
	}

	/**
	 * Utility method to verify the expected syndication partner ID is available or
	 * not
	 * 
	 * @param SyndicationpartnerIdToVerify Actual partnerID retrieved from script
	 * 
	 * @return Boolean representing the result of the validation.
	 * @author Athira
	 */
	public static boolean verifySyndicationPartnerAvailability(String synPartnerIdToVerify) {
		LOGGER.info("STARTING METHOD: verifyPartnerAvailability");
		boolean partnerIDExists = false;
		String partnerIDList = BroadbandPropertyFileHandler.getSyndicationPartnerIDValues();

		String[] partnerIDValues = partnerIDList.split(",");

		for (String partnerID : partnerIDValues) {
			if (partnerID.equalsIgnoreCase(synPartnerIdToVerify)) {
				partnerIDExists = true;
				break;
			}
		}
		return partnerIDExists;
	}

	/**
	 * Utility method to verify the expected syndication partner ID is available or
	 * not
	 * 
	 * @param verifySpecificSyndicationPartnerAvailability Actual partnerID
	 *                                                     retrieved from script
	 * 
	 * @return Boolean representing the result of the validation.
	 * @author Athira
	 */
	public static boolean verifySpecificSyndicationPartnerAvailability(String synPartnerIdToVerify) {
		LOGGER.info("STARTING METHOD: verifyPartnerAvailability");
		boolean partnerIDExists = false;
		String partnerIDList = BroadbandPropertyFileHandler.getSpecificSyndicationPartnerIDValues();

		String[] partnerIDValues = partnerIDList.split(",");

		for (String partnerID : partnerIDValues) {
			if (partnerID.equalsIgnoreCase(synPartnerIdToVerify)) {
				partnerIDExists = true;
				break;
			}
		}
		return partnerIDExists;
	}

	/**
	 * Method used to validate the syndication partner id's based on device models.
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @return true if partner is syndication else false
	 * @refactor Athira
	 */

	public static boolean isPartnerNameIsSyndication(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("STARTING METHOD : isPartnerNameIsSyndication()");
		boolean status = false;
		try {
			String currentPartnerIdName = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_FOR_SYNDICATION_PARTNER_ID);
			status = BroadBandCommonUtils.verifySyndicationPartnerAvailability(currentPartnerIdName);

			LOGGER.info("Partner id :" + currentPartnerIdName + " Is avaiable:" + status);
		} catch (Exception exception) {

			LOGGER.error("Exception occurred while validating the partners : " + exception.getMessage());
		}
		LOGGER.debug("ENDING METHOD : isPartnerNameIsSyndication()");
		return status;
	}

	/**
	 * Utility method to get eRouter ipv4 address from the device
	 * 
	 * @param device The device under test
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @return The erouteripv4 address
	 * 
	 * @refactor yamini.s
	 */
	public static String getErouteripv4Address(Dut device, AutomaticsTapApi tapEnv) {
		String eRouteripv4 = null;
		ifConfigErouter0Response = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.IFCONFIG_EROUTER);

		LOGGER.info("PATTERN: " + BroadBandCommandConstants.INET_V4_ADDRESS_PATTERN);
		if (CommonMethods.isNotNull(ifConfigErouter0Response)) {

			eRouteripv4 = CommonMethods.patternFinder(ifConfigErouter0Response,
					BroadBandCommandConstants.INET_V4_ADDRESS_PATTERN);
		}

		return eRouteripv4;
	}

	/**
	 * Utility method to get currently running image version and validate with
	 * requested one. This method dedicated for retrieving the parameter
	 * 
	 * @param tapEnv                     The {@link AutomaticsTapApi} reference.
	 * @param device                     The {@link Dut} to be validated.
	 * @param buildImageWithoutExtension The build image name.
	 * @return The {@link BroadBandResultObject} with status and error message.
	 * @refactor Said Hisham
	 */
	public static BroadBandResultObject verifyImageVersionUsingWebPaCommand(AutomaticsTapApi tapEnv, Dut device,
			String buildImageWithoutExtension) {

		BroadBandResultObject broadBandResultObject = new BroadBandResultObject();
		boolean webPaConnectivity = false;
		String errorMessage = BroadBandTestConstants.EMPTY_STRING;
		String currentVersion = BroadBandTestConstants.EMPTY_STRING;
		try {
			List<WebPaParameter> params = tapEnv.executeWebPaGetCommand(device,
					new String[] { BroadBandTestConstants.TR69_RDKB_CURRENT_IMAGE_NAME });
			if (null != params && !params.isEmpty() && params.size() >= 1) {
				currentVersion = params.get(0).getValue();
				if (CommonMethods.isNotNull(currentVersion)) {
					webPaConnectivity = buildImageWithoutExtension.toLowerCase().contains(currentVersion.toLowerCase())
							|| currentVersion.toLowerCase().contains(buildImageWithoutExtension.toLowerCase());
				} else {
					errorMessage = "Empty response from WebPA server for 'Device.DeviceInfo.X_CISCO_COM_FirmwareName'";
				}
				if (!webPaConnectivity) {
					errorMessage = "'Device.DeviceInfo.X_CISCO_COM_FirmwareName'  is returning wrong image version. Expected version : "
							+ buildImageWithoutExtension + ", Actual version : " + currentVersion;
				}
			} else {
				errorMessage = "Unable to retrieve the 'Device.DeviceInfo.X_CISCO_COM_FirmwareName' using WebPA";
			}
		} catch (Exception ex) {
			errorMessage = ex.getMessage();
		}
		broadBandResultObject.setStatus(webPaConnectivity);
		broadBandResultObject.setErrorMessage(errorMessage);
		return broadBandResultObject;
	}

	/**
	 * Helper method to check dibbler version
	 * 
	 * @param device          {@link Dut}
	 * @param tapEnv          {@link AutomaticsTapApi}
	 * @param versionNumber   dibbler current version number fetched from stb props
	 * @param dibbler_version version for client/server
	 * @return result boolean value containing search operation result
	 * @author Deepa Bada
	 */
	public static boolean verifyDibblerVersion(Dut device, AutomaticsTapApi tapEnv, String versionNumber,
			String dibbler_version_command) {
		LOGGER.debug("Starting Method : verifyDibblerVersion()");
		String response = null;
		boolean status = false;
		// execute command and get dibblerq version
		response = tapEnv.executeCommandUsingSsh(device, dibbler_version_command);
		if (CommonMethods.isNotNull(response)) {

			LOGGER.info("Obtained dibbler version from server response : " + response);
			// grep only version from server response using pattern finder
			// method
			response = CommonMethods.patternFinder(response, BroadBandTestConstants.PATTER_DIBBLER_VERSION);
			LOGGER.info("Obtained dibbler version : " + response);

			if (CommonMethods.isNotNull(response)) {

				status = response.contains(versionNumber);

				LOGGER.info(status ? "dibbler version updated to " + versionNumber
						: "dibbler version is not updated with latest version");
			} else {
				LOGGER.error("Unable to get the dibbler version from the response by pattern match"
						+ BroadBandTestConstants.PATTER_DIBBLER_VERSION);
			}
		} else {
			LOGGER.error("Obtained NULL response!!..Not able to get dibbler version from box using "
					+ dibbler_version_command + " command");
		}
		LOGGER.debug("Ending Method : verifyDibblerVersion()");
		return status;
	}

	/**
	 * Method to verify Build change requirement in post-condition
	 * 
	 * @param device                  Dut instance
	 * @param tapEnv                  AutomaticsTapApi instance
	 * @param buildNameToBeTriggerred Build name to which device upgrade is required
	 * @return true if build change is required
	 * 
	 * @author Prashant Mishra
	 */
	public static boolean verifyBuildChange(Dut device, AutomaticsTapApi tapEnv, String buildNameToBeTriggerred) {
		LOGGER.debug("STARTING METHOD postConditionToVerifyBuildChange()");
		// Variable declaration starts
		boolean isBuildChangeRequired = false;
		// Variable declaration ends
		try {
			LOGGER.info("Verifying whether device is accessible or not.");
			if (CommonMethods.isSTBAccessible(device)) {
				LOGGER.info("Device is accessible.");
				String currentImageName = FirmwareDownloadUtils.getCurrentFirmwareFileNameForCdl(tapEnv, device);
				LOGGER.info("REQUESTED IMAGE NAME FOR CODE DOWNLOAD  : " + buildNameToBeTriggerred);
				LOGGER.info("CURRENT BUILD IMAGE VERSION BEFORE TRIGGERING CDL : " + currentImageName);
				isBuildChangeRequired = !BroadBandCommonUtils.compareValues(
						BroadBandTestConstants.CONSTANT_TXT_COMPARISON, buildNameToBeTriggerred, currentImageName);
			} else {
				LOGGER.error("DEVICE IS NOT COMING UP. NOT ABLE TO VERIFY BUILD.");
			}

		} catch (Exception e) {
			LOGGER.error("Exception occured while verifying build change requirement. " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD postConditionToVerifyBuildChange()");
		return isBuildChangeRequired;
	}

	/**
	 * Utility method to get radio status of given Wifi SSID Frequency
	 * 
	 * @param ssidFrequency
	 * @param tapEnv
	 * @param device
	 * @return Radio status of given Wifi frequency
	 * @author Praveenkumar Paneerselvam
	 */
	public static String getRadioStatus(SSIDFrequency ssidFrequency, AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("INSIDE METHOD - getRadioStatus");
		String response = null;
		switch (ssidFrequency) {
		case FREQUENCY_2_4_GHZ:
			response = tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_WIFI_2_4_RADIO_ENABLE);
			break;
		case FREQUENCY_5_GHZ:
			response = tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_WIFI_5_RADIO_ENABLE);
			break;
		default:
			LOGGER.info("Unsupported Radio type");
			break;
		}
		LOGGER.info("Radio status of " + ssidFrequency.toString() + " Ghz: " + response);
		LOGGER.debug("EXIT FROM METHOD - getRadioStatus");
		return response;
	}

	/**
	 * Method to execute command to verify DNS nslookup functionality
	 * 
	 * @param device  instance of {@link Dut}
	 * @param tapEnv  instance of {@link AutomaticsTapApi}
	 * @param command command to execute
	 * @return response
	 * @refactor Alan_Bivera
	 */
	public static boolean executeAndVerifyNsLookUpCommandInConnectedClient(Dut device, AutomaticsTapApi tapEnv,
			String dnsIpAddress, String expectedvalue) throws TestException {
		LOGGER.debug("STARTING METHOD: executeAndVerifyNsLookUpCommandInConnectedClient()");
		boolean status = false;
		String command = BroadBandCommonUtils.concatStringUsingStringBuffer(
				BroadBandCommandConstants.CMD_NS_LOOKUP_OPEN_DNS_COM, BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
				dnsIpAddress);
		try {
			String response = tapEnv.executeCommandOnOneIPClients(device, command).replaceAll(
					BroadBandTestConstants.PATTERN_MATCHER_FOR_MULTIPLE_SPACES,
					BroadBandTestConstants.SINGLE_SPACE_CHARACTER);
			LOGGER.info("NS LOOKUP RESPONSE: " + response);
			status = CommonMethods.isNotNull(response)
					&& !CommonMethods.patternMatcher(response,
							BroadBandTestConstants.STRING_DNS_REQUEST_TIMED_OUT_ERROR_MESSAGE)
					&& CommonUtils.patternSearchFromTargetString(response, expectedvalue);
			if (expectedvalue.equalsIgnoreCase(BroadBandTestConstants.NS_LOOKUP_GLOBAL_DNS_RESPONSE)) {
				status = CommonMethods.isNotNull(response)
						&& !CommonMethods.patternMatcher(response,
								BroadBandTestConstants.STRING_DNS_REQUEST_TIMED_OUT_ERROR_MESSAGE)
						&& CommonUtils.patternSearchFromTargetString(response, expectedvalue);
			} else {
				status = CommonMethods.isNotNull(response)
						&& !CommonMethods.patternMatcher(response,
								BroadBandTestConstants.STRING_DNS_REQUEST_TIMED_OUT_ERROR_MESSAGE)
						&& (CommonUtils.patternSearchFromTargetString(response,
								BroadBandTestConstants.PATTERN_STRING_DOT_ASH)
								|| CommonUtils.patternSearchFromTargetString(response,
										BroadBandTestConstants.PATTERN_STRING_DOT_NYC));
			}
		} catch (Exception exception) {
			LOGGER.error("Exception occurred while verifying executeAndVerifyNsLookUpCommandInConnectedClient ():"
					+ exception.getMessage());
		}
		LOGGER.debug("ENDING METHOD: executeAndVerifyNsLookUpCommandInConnectedClient()");
		return status;
	}

	/**
	 * Utility Method to perform the check on whether file exists. The file check
	 * operation is being performed by polling for the existence of file for 10
	 * minutes.
	 * 
	 * @param tapEnv           {@link AutomaticsTapApi}
	 * @param device           {@link Dut}
	 * @param completeFilePath String representing the complete file path.
	 */
	public static boolean doesFileExistWithinGivenTimeFrame(AutomaticsTapApi tapEnv, Dut device,
			String completeFilePath, long pollDuration, long pollInterval) {
		LOGGER.debug("STARTING METHOD doesFileExistPreCondition");
		long startTime = System.currentTimeMillis();
		boolean result = false;
		LOGGER.info("### BEGIN PRE-CONDITION ### VERIFY THE PRESENCE OF THE FILE: " + completeFilePath);
		do {

			result = CommonMethods.isAtomSyncAvailable(device, tapEnv)
					? doesFileExistInAtomConsole(device, tapEnv, completeFilePath).isStatus()
					: CommonUtils.isFileExists(device, tapEnv, completeFilePath);
			tapEnv.waitTill(pollInterval);
			if (result) {
				break;
			}
		} while ((System.currentTimeMillis() - startTime) < pollDuration && !result);

		LOGGER.info("### VERIFIED THE PRESENCE OF THE FILE: " + completeFilePath);
		LOGGER.debug("ENDING METHOD doesFileExistWithinGivenTimeFrame");
		return result;
	}

	/**
	 * Increments step by 1
	 * 
	 * @param currentStep
	 * @return step number after incrementing by 1
	 * @refactor Athira
	 */
	public static String stepAdder(String currentStep) {
		StringBuilder sb = new StringBuilder("s");
		String stepNumber = currentStep.substring(currentStep.indexOf("s") + 1, currentStep.length());
		sb.append(Integer.parseInt(stepNumber) + 1);
		return sb.toString();
	}

	public static boolean isFileExistsonAtom(Dut device, AutomaticsTapApi tapEnv, String completeFilePath) {
		LOGGER.info("STARTING METHOD: isFileExistsonAtom()");
		boolean status = false;
		String response = tapEnv.executeCommandOnAtom(device,
				"if [ -f " + completeFilePath + " ] ; then echo \"true\" ; else echo \"false\" ; fi");
		if (CommonMethods.isNotNull(response)) {
			if (response.trim().equals(BroadBandTestConstants.TRUE)) {
				status = true;
			} else {
				if (!response.trim().equals(BroadBandTestConstants.FALSE)) {
					throw new TestException("Obtained response other than 'true' or 'false'");
				}

				status = false;
			}

			LOGGER.info("ENDING METHOD: isFileExists()");
			return status;
		} else {
			throw new TestException("Obtained null response");
		}
	}

	/**
	 * Utility method to verify a file is present on the device. It uses the
	 * command: find / -iname <fileName>
	 * 
	 * @param tapEnv   {@link AutomaticsTapApi}
	 * @param device   {@link Dut}
	 * @param fileName String representing the Library Name
	 * 
	 * @return Boolean representing the result of the validation.
	 */
	public static boolean isFilePresentOnDeviceAtom(AutomaticsTapApi tapEnv, Dut device, String fileName) {
		LOGGER.debug("STARTING METHOD: isFilePresentOnDevice");
		boolean result = false;
		String command = concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_FIND_INAME, fileName);
		String response = tapEnv.executeCommandOnAtom(device, command);
		if (CommonMethods.isNotNull(response)) {
			result = CommonMethods.patternMatcher(response, BroadBandTestConstants.PATTERN_MATCHER_FIND_NO_SUCH_FILE)
					&& !CommonMethods.patternMatcher(response, fileName + "[\r|\n]");
		} else {
			// Even when the response is blank which we get inconsistently, we
			// are updating the result as Success.
			LOGGER.info("NULL/ BLANK RESPONSE. MARKING AS FILE NOT PRESENT.");
			result = true;
		}
		LOGGER.info("FILE " + fileName + " IS PRESENT ON THE DEVICE: " + !result);
		LOGGER.debug("ENDING METHOD: isFilePresentOnDevice");
		return !result;
	}

	/**
	 * Method to execute a single WebPA response and return the result.
	 * 
	 * @param tapEnv                   {@link AutomaticsTapApi}
	 * @param device                   {@link Dut}
	 * @param parentalControlParameter object of type
	 *                                 BroadBandParentalControlParameter with
	 *                                 details of the url to be blocked WebPA
	 *                                 command to be executed.
	 * 
	 * @return New row parameter created on setting parental control rule
	 * @refactor Govardhan
	 */
	public static String setParentalControlParam(Dut device, AutomaticsTapApi tapEnv,
			BroadBandParentalControlParameter parentalControlParam) throws TestException {

		LOGGER.debug("STARTING METHOD: setParentalControlParam");
		boolean status = false;
		String newWebPaParameterCreatedForParentalControlRule = null;
		StringBuffer url = new StringBuffer();
		url = url.append(BroadbandPropertyFileHandler.getWebpaServerURL()).append(((Device) device).getEcmMac())
				.append("/config/").append(BroadBandWebPaConstants.WEBPA_PARAM_TO_SET_A_PARENTAL_CONTROL_RULE);
		// enabling parental controls
		status = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv,
				BroadBandWebPaConstants.WEBPA_PARAM_TO_ENABLE_PARENTAL_CONTROLS, WebPaDataTypes.BOOLEAN.getValue(),
				RDKBTestConstants.TRUE);
		if (status) {
			Map<String, List<String>> data = new HashMap<String, List<String>>();
			List<String> BlockMethod = new ArrayList<String>();
			if (CommonMethods.isNotNull(parentalControlParam.getBlockMethod())) {
				BlockMethod.add(parentalControlParam.getBlockMethod());
			}
			List<String> Site = new ArrayList<String>();
			if (CommonMethods.isNotNull(parentalControlParam.getSite())) {
				Site.add(parentalControlParam.getSite());
			}
			List<String> AlwaysBlock = new ArrayList<String>();
			if (CommonMethods.isNotNull(parentalControlParam.getAlwaysBlock())) {
				AlwaysBlock.add(parentalControlParam.getAlwaysBlock());
			}
			List<String> StartTime = new ArrayList<String>();
			if (CommonMethods.isNotNull(parentalControlParam.getStartTime())) {
				StartTime.add(parentalControlParam.getStartTime());
			}
			List<String> EndTime = new ArrayList<String>();
			if (CommonMethods.isNotNull(parentalControlParam.getEndTime())) {
				EndTime.add(parentalControlParam.getEndTime());
			}
			List<String> BlockDays = new ArrayList<String>();
			if (CommonMethods.isNotNull(parentalControlParam.getBlockDays())) {
				BlockDays.add(parentalControlParam.getBlockDays());
			}
			if (!BlockMethod.isEmpty()) {
				data.put("BlockMethod", BlockMethod);
			}
			if (!Site.isEmpty()) {
				data.put("Site", Site);
			}
			if (!AlwaysBlock.isEmpty()) {
				data.put("AlwaysBlock", AlwaysBlock);
			}
			if (!StartTime.isEmpty()) {
				data.put("StartTime", StartTime);
			}
			if (!EndTime.isEmpty()) {
				data.put("EndTime", EndTime);
			}
			if (!BlockDays.isEmpty()) {
				data.put("BlockDays", BlockDays);
			}
			WebPaServerResponse serverResponse = tapEnv.postWebpaTableParamUsingRestApi(device,
					BroadBandWebPaConstants.WEBPA_PARAM_TO_SET_A_PARENTAL_CONTROL_RULE, data);
			LOGGER.info("WebPaServerResponse is : " + serverResponse);
			if (serverResponse != null) {
				newWebPaParameterCreatedForParentalControlRule = serverResponse.getRow();
			} else {
				LOGGER.error("Null response obtained for setting parental control rule");
				throw new TestException("Null response obtained for setting parental control rule");
			}
		} else {
			LOGGER.error(
					"Failed in Enabling Parental control rule usin parameter Device.X_Comcast_com_ParentalControl.ManagedSites.Enable");
			throw new TestException(
					"Failed in Enabling Parental control rule using parameter Device.X_Comcast_com_ParentalControl.ManagedSites.Enable");
		}
		LOGGER.info("New WebPa Parameter Created For Parental Control Rule is : "
				+ newWebPaParameterCreatedForParentalControlRule);
		return newWebPaParameterCreatedForParentalControlRule;
	}

	/**
	 * Method to validate ssh banners presence
	 * 
	 * @param response command execution response
	 * 
	 * @return true, if it is present
	 * @refactor Govardhan
	 */
	public static void verifySshBannerPresenceWithLegalWarnings(String response) throws TestException {

		boolean status = false;
		String errorMessage = null;

		if (CommonMethods.isNotNull(response)) {

			LOGGER.info("Successfully obtained Security banner response from server");

			response = response.replaceAll(RDKBTestConstants.PATTERN_GET_NEWLINE_SPACE, RDKBTestConstants.EMPTY_STRING);

			LOGGER.info("response after space removed : " + response);

			if (response.contains(BroadbandPropertyFileHandler.getSSHBanner()
					.replaceAll(BroadBandTestConstants.PATTERN_GET_NEWLINE_SPACE, RDKBTestConstants.EMPTY_STRING))) {

				status = response.contains(RDKBTestConstants.STRING_CONNECTION_STATUS);

				if (status) {
					LOGGER.info(
							"Successfully obtained improved Security response banner from jump server during SSH connection and also able to access STB");
				} else {
					LOGGER.info("Unable to access STB using jump server");
					errorMessage = "Successfully obtained improved Security banners from server but not able to access STB";

				}
			} else {
				errorMessage = "Obtained security Banner is not matching with improved security banners";
				LOGGER.info(errorMessage + ".Response obtained : " + response);
			}
		}

		if (!status) {
			throw new TestException(errorMessage);
		}
	}

	/**
	 * Utility method to configure Port Forwarding Rule
	 * 
	 * @param tapEnv                   Instance of {@link AutomaticsTapApi}
	 * @param device                   Instance of {@link Dut}
	 * @param enable                   To enable port forwarding (true/false)
	 * @param startPort                Starting port range
	 * @param endPort                  Ending port range
	 * @param protocolToUsed           Protocol to be used (TCP/UDP/BOTH)
	 * @param connectedClientIpAddress IP Address of the connected client for which
	 *                                 the port traffic should be forwarded
	 * @param ruleDescription          Name of the port forwarding rule
	 * 
	 * @return the row number of the new entry in port forwarding table
	 */
	public static String configurePortForwardingRule(AutomaticsTapApi tapEnv, Dut device, String enable,
			String startPort, String endPort, String protocolToUsed, String connectedClientIpAddress,
			String ruleDescription) {
		LOGGER.debug("ENTERING METHOD configurePortForwardingRule");

		String portForwardingTableAddRowResponse = null;

		Map<String, List<String>> data = new HashMap<String, List<String>>();
		List<String> toEnable = new ArrayList<String>();
		toEnable.add(enable);
		List<String> externalPort = new ArrayList<String>();
		externalPort.add(startPort);
		List<String> externalPortEndRange = new ArrayList<String>();
		externalPortEndRange.add(endPort);
		List<String> protocol = new ArrayList<String>();
		protocol.add(protocolToUsed);
		List<String> internalClient = new ArrayList<String>();
		internalClient.add(connectedClientIpAddress);
		List<String> description = new ArrayList<String>();
		description.add(ruleDescription);

		data.put("Enable", toEnable);
		data.put("ExternalPort", externalPort);
		data.put("ExternalPortEndRange", externalPortEndRange);
		data.put("Protocol", protocol);
		data.put("InternalClient", internalClient);
		data.put("Description", description);

		WebPaServerResponse serverResponse = tapEnv.postWebpaTableParamUsingRestApi(device,
				BroadBandWebPaConstants.WEBPA_PARAM_TO_CONFIGURE_PORT_FORWARDING_RULE, data);

		LOGGER.info("WEBPA SERVER RESPONSE FOR CONFIGURING PORT FORWARDING RULE : " + serverResponse);

		if (serverResponse != null) {
			portForwardingTableAddRowResponse = serverResponse.getRow();
		} else {
			LOGGER.error("NULL RESPOSNE OBTAINED FOR CONFIGURING PORT FORWARDING RULE");
		}
		LOGGER.debug("ENDING METHOD configurePortForwardingRule");
		return portForwardingTableAddRowResponse;
	}

	/**
	 * Utility method to verify Portfowarding rules are configured in Iptables
	 * 
	 * @param tapEnv                       {@link AutomaticsTapApi}
	 * @param device                       {@link Dut}
	 * @param ipAddressRetrievedFromClient Ipv4 address retrived from the client
	 * 
	 * @return true if the .ipt file has the portforwarding rule configurations
	 * @refactor Govardhan
	 */
	public static boolean verifyPortForwardingRuleConfigurationInIPTables(AutomaticsTapApi tapEnv, Dut device,
			String ipAddressRetrievedFromClient) {
		LOGGER.debug("ENTERING METHOD verifyPortForwardingRuleinIPTables");
		boolean result = false;
		try {
			String response = tapEnv.executeCommandUsingSsh(device,
					BroadBandCommonUtils.concatStringUsingStringBuffer(
							BroadBandCommandConstants.CMD_TO_GET_IPTABLES_VALUES,
							BroadBandTestConstants.SINGLE_SPACE_CHARACTER, ipAddressRetrievedFromClient));
			if (CommonMethods.isNotNull(response)) {
				String logs[] = { BroadBandTestConstants.STRING_WAN2LAN_CONFIG,
						BroadBandTestConstants.STRING_LAN2WAN_CONFIG, BroadBandTestConstants.STRING_WAN2LAN_UDP_CONFIG,
						BroadBandTestConstants.STRING_LAN2WAN_UDP_CONFIG };
				for (String config : logs) {
					String logsToCompare = BroadBandCommonUtils.concatStringUsingStringBuffer(config,
							BroadBandTestConstants.SINGLE_SPACE_CHARACTER, ipAddressRetrievedFromClient);
					if (!CommonUtils.patternSearchFromTargetString(response, logsToCompare)) {
						result = false;
						break;
					} else {
						result = true;
					}
				}
			}
		} catch (Exception exception) {
			LOGGER.error("Exception occured while searching for portforwarding config ", exception);
		}
		LOGGER.debug("ENDING METHOD verifyPortForwardingRuleinIPTables");
		return result;
	}

	/**
	 * Utils method to verify the connectivity to the given URL/IP Address using
	 * Curl Command
	 * 
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @param url    Internet access of URL which needs to be checked
	 * 
	 * @return true if the given URL/IP Address is accessible from Jumper server
	 *         using curl
	 * @Refactor Alan_Bivera
	 */
	public static BroadBandResultObject verifyConnectivityFromJumpServerUsingCurl(AutomaticsTapApi tapEnv, Dut device,
			String url) {

		boolean isAccessible = false;
		String errorMessage = null;
		BroadBandResultObject result = new BroadBandResultObject();
		String command = "";
		if (CommonMethods.isIpv6Address(url)) {
			command = BroadBandCommonUtils.concatStringUsingStringBuffer(
					BroadBandCommandConstants.CMD_CURL_WITH_20_SECS_TIMEOUT_IPV6,
					BroadBandTestConstants.OPENING_BRACKETS, url, BroadBandTestConstants.CLOSING_BRACKETS);
		} else {
			command = BroadBandCommonUtils
					.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_CURL_WITH_20_SECS_TIMEOUT, url);
		}
		// String response = BroadBandCommonUtils.executeCommandInJumpServer(tapEnv,
		// command);
		String response = tapEnv.executeCommandUsingSsh(device, command);
		errorMessage = "Null response obtained on checking connectivity from the Jump Server";
		if (CommonMethods.isNotNull(response)) {
			isAccessible = CommonUtils.patternSearchFromTargetString(response,
					BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_HEADER_SUCCESS_MESSAGE)
					|| CommonUtils.patternSearchFromTargetString(response,
							BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_SUCCESS_MESSAGE)
					|| CommonUtils.patternSearchFromTargetString(response,
							BroadBandConnectedClientTestConstants.RESPONSE_STATUS_OK);
			errorMessage = (!isAccessible && CommonUtils
					.patternSearchFromTargetString(response, BroadBandTestConstants.CURL_NOT_INSTALLED_ERROR_MESSAGE))
							? "Curl is not installed in the " // + SshConnectionHandler.get().getJumpServerIPv6()
									+ " Jump Server"
							: (!isAccessible && (CommonUtils.patternSearchFromTargetString(response,
									BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_CONNECTION_FAILURE_MESSAGE)
									|| CommonUtils.patternSearchFromTargetString(response,
											BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_CONNECTION_SSL_HANDSHAKE_FAILURE_MESSAGE)
									|| CommonUtils.patternSearchFromTargetString(response,
											BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_FAILURE_MESSAGE)
									|| CommonUtils.patternSearchFromTargetString(response,
											BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_CONNECTION_TIMEOUT_MESSAGE)
									|| CommonUtils.patternSearchFromTargetString(response,
											BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_OPERATION_TIMEOUT_MESSAGE)
									|| CommonUtils.patternSearchFromTargetString(response,
											BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_RESOLVING_TIMEOUT_MESSAGE)))
													? "Website/IP Address is not accessible from the Jump Server"
													: "Unable to check the connectivity to Website/IP Address from the Jump Server";
		}
		result.setErrorMessage(errorMessage);
		result.setStatus(isAccessible);

		return result;
	}

	/**
	 * @param response response to validate
	 * @author Sathurya Ravi
	 * @refactor Said Hisham
	 */

	public static boolean isBoolean(String response) {
		return ((CommonMethods.isNotNull(response) && (response.equalsIgnoreCase(AutomaticsConstants.TRUE)
				|| response.equalsIgnoreCase(AutomaticsConstants.FALSE))));
	}

	/**
	 * Method to add invalid certificates to RPI.
	 * 
	 * @param values , tapEnv, rasberrypi
	 * 
	 * @author Prince ArunRaj
	 * @refactor Said Hisham
	 */
	public static boolean addInvalidCertificatedsToRPI(AutomaticsTapApi tapEnv, Dut raspberryPi) {
		LOGGER.debug("Entering into addInvalidCertificatedsToRPI()");
		String command = null;
		String response = null;
		boolean status = false;

		command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_SUDO,
				BroadBandCommandConstants.CMD_CHMOD, BroadBandCommandConstants.CHMOD_777_VALUE,
				BroadBandCommandConstants.PATH_CERTIFICATE);
		tapEnv.executeCommandOnOneIPClients(raspberryPi, command);
		// adding invalid certificates to RPI
		command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_SUDO,
				BroadBandCommandConstants.CMD_ECHO,
				AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.PROP_KEY_CERT_CA_CHAIN),
				BroadBandCommandConstants.CMD_REDIRECTION, BroadBandCommandConstants.PATH_CERTIFICATE,
				BroadBandCommandConstants.FILE_CA_CHAIN_CERT);
		tapEnv.executeCommandOnOneIPClients(raspberryPi, command);
		command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_SUDO,
				BroadBandCommandConstants.CMD_ECHO,
				AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.PROP_KEY_CERT_VIDEO_CLIENT),
				BroadBandCommandConstants.CMD_REDIRECTION, BroadBandCommandConstants.PATH_CERTIFICATE,
				BroadBandCommandConstants.FILE_VIDEO_CLIENT_CERT);
		tapEnv.executeCommandOnOneIPClients(raspberryPi, command);
		command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_SUDO,
				BroadBandCommandConstants.CMD_ECHO,
				AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.PROP_KEY_VIDEO_CLIENT),
				BroadBandCommandConstants.CMD_REDIRECTION, BroadBandCommandConstants.PATH_CERTIFICATE,
				BroadBandCommandConstants.FILE_VIDEO_CLIENT_KEY);
		tapEnv.executeCommandOnOneIPClients(raspberryPi, command);

		command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_SUDO,
				BroadBandCommandConstants.CMD_CHMOD, BroadBandCommandConstants.CHMOD_755_VALUE,
				BroadBandCommandConstants.PATH_CERTIFICATE);
		tapEnv.executeCommandOnOneIPClients(raspberryPi, command);

		// verifying the files added to RPI
		command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_SUDO,
				BroadBandCommandConstants.CMD_LS, BroadBandCommandConstants.PATH_CERTIFICATE);
		response = tapEnv.executeCommandOnOneIPClients(raspberryPi, command);
		LOGGER.info(
				"certificates added in certs folder are ca-chain.cert.pem, actual certificates available in certs folder: \n"
						+ response);
		status = CommonMethods.isNotNull(response)
				&& CommonUtils.isGivenStringAvailableInCommandOutput(response,
						BroadBandCommandConstants.FILE_CA_CHAIN_CERT)
				&& CommonUtils.isGivenStringAvailableInCommandOutput(response,
						BroadBandCommandConstants.FILE_VIDEO_CLIENT_CERT)
				&& CommonUtils.isGivenStringAvailableInCommandOutput(response,
						BroadBandCommandConstants.FILE_VIDEO_CLIENT_KEY);

		LOGGER.debug("Exiting from addInvalidCertificatedsToRPI()");
		return status;
	}

	/**
	 * Method to remove invalid certificates to RPI.
	 * 
	 * @param values , tapEnv, rasberrypi
	 * 
	 * @author Prince ArunRaj
	 * @refactor Said Hisham
	 */
	public static boolean rmInvalidCertificatedsToRPI(AutomaticsTapApi tapEnv, Dut raspberryPi) {
		LOGGER.debug("Entering into rmInvalidCertificatedsToRPI()");
		String command = null;
		boolean status = false;
		command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_REMOVE,
				BroadBandCommandConstants.PATH_CERTIFICATE, BroadBandCommandConstants.FILE_CA_CHAIN_CERT);
		tapEnv.executeCommandOnOneIPClients(raspberryPi, command);
		command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_REMOVE,
				BroadBandCommandConstants.PATH_CERTIFICATE, BroadBandCommandConstants.FILE_VIDEO_CLIENT_CERT);
		tapEnv.executeCommandOnOneIPClients(raspberryPi, command);
		command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_REMOVE,
				BroadBandCommandConstants.PATH_CERTIFICATE, BroadBandCommandConstants.FILE_VIDEO_CLIENT_KEY);
		tapEnv.executeCommandOnOneIPClients(raspberryPi, command);
		status = true;
		LOGGER.debug("Exiting from rmInvalidCertificatedsToRPI()");
		return status;
	}

	/**
	 * method to check the device type and return the value
	 * 
	 * @param device
	 * @param value1 expected value
	 * @param value2 expected value
	 * @param value3 expected value
	 * @return expectedValue return expected value based on the device type
	 * @author ArunKumar Jayachandran
	 * @refactor yamini.s
	 */
	public static String getTxRateExpctdValForDeviceType(Dut device, String value1, String value2, String value3) {
		LOGGER.debug("STARTING METHOD : getTxRateExpctdValForDeviceType()");
		String expectedValue = null;
		if ((CommonMethods.isAtomSyncAvailable(device, AutomaticsTapApi.getInstance()))
				|| (DeviceModeHandler.isDSLDevice(device))
				|| (BroadbandPropertyFileHandler.isDeviceCheckToReturnExpectedValue1(device))) {
			expectedValue = value1;
		} else if ((BroadbandPropertyFileHandler.isDeviceCheckToReturnExpectedValue2(device))) {
			expectedValue = value2;
		} else if ((BroadbandPropertyFileHandler.isDeviceCheckToReturnExpectedValue3(device))) {
			expectedValue = value3;
		}
		LOGGER.debug("ENDING METHOD :getTxRateExpctdValForDeviceType()");
		return expectedValue;
	}

	/**
	 * Utility method to verify the logs in Atom or Arm Console with pattern matcher
	 * 
	 * @param device         Instance of{@link Dut}
	 * @param tapEnv         instance of {@link AutomaticsTapApi}
	 * @param searchText     Text to search
	 * @param logFileName    Log file name
	 * @param patternToFind  Pattern to match
	 * @param isAtom         True - To verify the logs in Atom console.False-To
	 *                       verify the logs in Arm console
	 * @param deviceDateTime Device date and time
	 * @return True- Expected log is available with pattern matcher.Else False.
	 * 
	 * @refactor Athira
	 */
	public static BroadBandResultObject verifyLogsInAtomOrArmWithPatternMatcherWithLogTime(AutomaticsTapApi tapEnv,
			Dut device, String searchText, String logFileName, String patternToFind, boolean isAtom,
			String deviceDateTime) {
		LOGGER.debug("STARTING METHOD : verifyLogsInAtomOrArmWithPatternMatcherWithLogTime");
		String expectedValue = null;
		String response = null;
		long startTime = System.currentTimeMillis();
		boolean status = false;
		BroadBandResultObject broadBandResultObject = new BroadBandResultObject();
		String errorMessage = "Unable to get the value from log file";
		String command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND,
				BroadBandTestConstants.TEXT_DOUBLE_QUOTE, searchText, BroadBandTestConstants.TEXT_DOUBLE_QUOTE,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, logFileName, BroadBandTestConstants.SYMBOL_PIPE,
				BroadBandTestConstants.CMD_TAIL_1);
		LOGGER.info("EXPECTED PATTERN = " + patternToFind);
		LOGGER.info("EXPECTED SEARCH TEXT = " + searchText);
		try {
			do {
				response = isAtom ? tapEnv.executeCommandOnAtom(device, command)
						: tapEnv.executeCommandUsingSsh(device, command);
				status = CommonMethods.isNotNull(response) && CommonMethods.patternMatcher(response, patternToFind)
						&& BroadBandCommonUtils.verifyLogUsingTimeStamp(deviceDateTime, response);
			} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.SIX_MINUTE_IN_MILLIS && !status
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
			if (status) {
				try {
					expectedValue = CommonMethods.patternFinder(response, patternToFind);
				} catch (Exception e) {
					LOGGER.error("Exception occurred in patternFinder:" + e.getMessage());
				}
			}
		} catch (Exception e) {
			LOGGER.error("Unable to get the expected value from search logs " + e.getMessage());
			errorMessage += e.getMessage();
		}
		broadBandResultObject.setStatus(status);
		broadBandResultObject.setOutput(expectedValue);
		broadBandResultObject.setErrorMessage(errorMessage);
		LOGGER.debug("ENDING METHOD : verifyLogsInAtomOrArmWithPatternMatcherWithLogTime");
		return broadBandResultObject;
	}

	/**
	 * Utility method to verify the log messages on given log file for multiple date
	 * format.
	 * 
	 * @param tapEnv                         {@link AutomaticsTapApi} Reference
	 * @param device                         {@link Dut} to be validated
	 * @param searchText                     String representing the text to be
	 *                                       searched in the log file.
	 * @param logFileName                    String representing the log file name.
	 * @param deviceDateTime                 Timestamp from Device
	 * @param patternMatcherToGetDateFromLog pattern matcher string to get the date
	 *                                       and time from the log
	 * @param logMessageTimeStampFormat      TimeStamp format in the log file
	 * @return result Boolean representing the result of verification of Log file.
	 * 
	 * @author susheela C
	 * @refactor Athira
	 */

	public static boolean verifyConsoleLogByPassingDateFormat(AutomaticsTapApi tapEnv, Dut device, String searchText,
			String logFileName, String deviceDateTime, String patternMatcherToGetDateFromLog,
			String logMessageTimeStampFormat) {
		LOGGER.debug("ENTERING METHOD: BroadBandCommonUtils.verifyConsoleLogForMultipleDateFormat");
		boolean result = false;
		String command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND,
				BroadBandTestConstants.TEXT_DOUBLE_QUOTE, searchText, BroadBandTestConstants.TEXT_DOUBLE_QUOTE,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, logFileName);
		String command2 = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND, "-c",
				BroadBandTestConstants.TEXT_DOUBLE_QUOTE, searchText, BroadBandTestConstants.TEXT_DOUBLE_QUOTE,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, logFileName);
		String response = tapEnv.executeCommandUsingSsh(device, command);
		String response2 = tapEnv.executeCommandUsingSsh(device, command2);
		LOGGER.info("Count of the string :" + response2);
		searchText = searchText.replaceAll(BroadBandTestConstants.TEXT_DOUBLE_QUOTE, AutomaticsConstants.EMPTY_STRING);
		SimpleDateFormat logMessageDateTimeFormat = new SimpleDateFormat(logMessageTimeStampFormat);
		if (CommonMethods.isNotNull(response) && CommonUtils.patternSearchFromTargetString(response, searchText)) {
			result = isLogTimeStampRecentThanCapDeviceTime(deviceDateTime, response, patternMatcherToGetDateFromLog,
					logMessageDateTimeFormat);
		}
		LOGGER.debug("ENDING METHOD: BroadBandCommonUtils.verifyConsoleLogForMultipleDateFormat");
		return result;
	}

	/**
	 * Utility Method to validate the log message searched & found is the recent one
	 * i.e. after performing the validation step, the timestamp on the device is
	 * captured and compared with the timestamp of logs retrieved for the validation
	 * step. The timestamp of the log entry should be greater than the timestamp
	 * captured after validation step.
	 * 
	 * This method utilizes the date captured on the device before performing the
	 * action and the log message searched after performing the action. Both the
	 * timestamps are compared & validated for multiple date format.
	 * 
	 * @param capturedDeviceTime             Timestamp from Device
	 * @param searchResponse                 String representing the search response
	 *                                       after performing the action.
	 * @param patternMatcherToGetDateFromLog pattern matcher string to get the date
	 *                                       and time from the log
	 * @param logMessageDateTimeFormat       TimeStamp format in the log file
	 * 
	 * @return result Boolean representing the result of validation of log message
	 *         using TimeStamp.
	 * 
	 * @author susheela C
	 * @refactor Athira
	 */
	public static boolean isLogTimeStampRecentThanCapDeviceTime(String capturedDeviceTime, String searchResponse,
			String patternMatcherToGetDateFromLog, SimpleDateFormat logMessageDateTimeFormat) {
		LOGGER.debug("ENTERING METHOD: BroadBandCommonUtils.isLogTimeStampRecentThanCapDeviceTime");
		boolean result = false;
		SimpleDateFormat deviceDateTimeFormat = new SimpleDateFormat(
				BroadBandTestConstants.TIMESTAMP_FORMAT_LOG_MESSAGE);
		LOGGER.info("dateResponse: " + capturedDeviceTime);
		LOGGER.info("searchResponse: " + searchResponse);
		// Parse the TimeStamp captured on the device with the required format.
		Date timeCapturedInDevice = null;
		try {
			timeCapturedInDevice = CommonMethods.isNotNull(capturedDeviceTime)
					? deviceDateTimeFormat.parse(capturedDeviceTime.trim())
					: null;
			LOGGER.info("CAPTURED TIMESTAMP IN DEVICE(DATE INSTANCE): " + timeCapturedInDevice);
		} catch (ParseException parseException) {
			LOGGER.error(capturedDeviceTime + " COULD NOT BE PARSED: " + parseException.getMessage());
		}

		// Parse the TimeStamp from the log message after extracting it.
		String strLogMessageDateTime = CommonMethods.isNotNull(searchResponse)
				? CommonMethods.patternFinder(searchResponse, patternMatcherToGetDateFromLog)
				: null;
		LOGGER.info("LOG FILE TIMESTAMP AFTER EXTRACTING(strLogMessageDateTime): " + strLogMessageDateTime);
		Date logMessageDateTime = null;
		try {
			if (CommonMethods.isNotNull(strLogMessageDateTime)) {
				strLogMessageDateTime = strLogMessageDateTime.replaceAll("\\[|\\]", AutomaticsConstants.EMPTY_STRING);
				LOGGER.info("AFTER REPLACEING SQUARE BRACKETS, IF ANY: " + strLogMessageDateTime);
				logMessageDateTime = logMessageDateTimeFormat.parse(strLogMessageDateTime);
			}
			LOGGER.info("CAPTURED TIMESTAMP IN LOG(DATE INSTANCE):" + logMessageDateTime);
		} catch (ParseException parseException) {
			LOGGER.error(strLogMessageDateTime + " COULD NOT BE PARSED: " + parseException.getMessage());
		}

		// Converting the captured DateTime to MilliSeconds & Comparing them.
		// If the TimeStamp Captured before performing action has a value lower than the
		// TimeStamp in log message, then
		// it returns TRUE.
		result = null != timeCapturedInDevice && null != logMessageDateTime
				&& timeCapturedInDevice.getTime() < logMessageDateTime.getTime();
		LOGGER.info("LOG VALIDATION (USING TIMESTAMP) RESULT: " + result);
		LOGGER.debug("ENDING METHOD: BroadBandCommonUtils.isLogTimeStampRecentThanCapDeviceTime");
		return result;
	}

	/**
	 * Method to verify feature enable via RFC
	 * 
	 * @param device        Dut instance
	 * @param tapEnv        AutomaticsTapApi instance
	 * @param pattern       pattern which is to be executed
	 * @param operation     value comparison operation
	 * @param expectedValue enabled is true
	 * @return true if expected value matches the response
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @refactor Govardhan
	 */
	public static boolean verifyFeatureEnableViaRFC(Dut device, AutomaticsTapApi tapEnv, String pattern,
			String operation, String expectedValue) {
		LOGGER.debug("STARTING METHOD: verifyFeatureEnableViaRFC()");
		String response = null;
		boolean status = false;
		try {
			response = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.COMMAND_TO_RFC_VALUE_CONFIG_FILE);
			if (CommonMethods.isNotNull(response)) {
				response = CommonMethods.patternFinder(response, pattern);
				if (CommonMethods.isNotNull(response)) {
					LOGGER.info("Feature value  in RFC configuration : " + response);
					status = BroadBandCommonUtils.compareValues(operation, response, expectedValue);
					LOGGER.info("Feature is enabled by RFC configuration : " + status);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while verifying SNMP reboot reason:" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: verifyFeatureEnableViaRFC()");
		return status;
	}

	/**
	 * Method to clear log file on atom console
	 * 
	 * @param tapEnv   {@link AutomaticsTapApi}
	 * @param device   {@link Dut}
	 * @param fileName path of file on atom to be cleared
	 * @return true if clear is success
	 * 
	 * @author Ashwin Sankarasubramanian
	 * @refactor Athira
	 */
	public static boolean clearAtomLogFile(AutomaticsTapApi tapEnv, Dut device, String fileName) {
		LOGGER.debug("STARTING METHOD clearLogFile");
		String response = tapEnv.executeCommandOnAtom(device,
				concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_ECHO_CLEAR, fileName));
		boolean result = CommonMethods.isNull(response);
		LOGGER.info("Is " + fileName + " CLEARED: " + result);
		LOGGER.debug("ENDING METHOD clearLogFile");
		return result;
	}

	public static String getSecurityModeForDeviceModels(Dut device) {
		LOGGER.debug("STARTING METHOD: getSecurityModeForDeviceModels()");
		String deviceModel = device.getModel();
		String securityMode = null;
		boolean isNonDefaultSecurityModeModel = false;

		securityMode = BroadBandTestConstants.SECURITY_MODE_WPA2_PERSONAL;
		LOGGER.debug("ENDING METHOD: getSecurityModeForDeviceModels()");
		return securityMode;
	}

	/**
	 * Method to remove file/ folder from ATOM/ ARM Console depending upon the
	 * availability of ATOM Console.
	 * 
	 * @param tapEnv              {@link AutomaticsTapApi}
	 * @param device              {@link Dut}
	 * @param completeFilePath    The Complete file path
	 * @param isAtomSyncAvailable Flag representing the availability of ATOM
	 *                            Console; TRUE if present; else FALSE
	 * 
	 * @return Result of the file delete operation; TRUE if successful; else FALSE.
	 * @refactor Govardhan
	 */
	public static boolean deleteFileAndVerifyIfAtomPresentElseArm(AutomaticsTapApi tapEnv, Dut device,
			String completeFilePath, boolean isAtomSyncAvailable) {
		LOGGER.info("ENTERING METHOD deleteFileAndVerifyIfAtomPresentElseArm");
		boolean result = false;
		if (isAtomSyncAvailable) {
			String commandToRemoveFile = BroadBandTestConstants.CMD_REMOVE_DIR_FORCEFULLY
					+ BroadBandTestConstants.SINGLE_SPACE_CHARACTER + completeFilePath;
			String response = tapEnv.executeCommandOnAtom(device, commandToRemoveFile);
			LOGGER.info("COMMAND RESPONSE TO DELETE FILE/ FOLDER: " + response);
			BroadBandResultObject resultObject = doesFileExistInAtomConsole(device, tapEnv, completeFilePath);
			result = !resultObject.isStatus();
		} else {
			result = CommonUtils.deleteFile(device, tapEnv, completeFilePath);
		}
		LOGGER.info("REMOVED THE FILE " + completeFilePath + " FROM " + (isAtomSyncAvailable ? "ATOM " : "ARM ")
				+ "CONSOLE SUCCESSFULLY - " + result);
		LOGGER.info("ENDING METHOD deleteFileAndVerifyIfAtomPresentElseArm");
		return result;
	}

	/**
	 * Helper method to format the mac address from wifi_api response
	 * 
	 * @param macAddress
	 * @return macResponse
	 * 
	 * @author ArunKumar Jayachandran
	 */
	public static String wifiApiMacAddressFormat(String macAddress) {
		LOGGER.debug("STARTING METHOD: wifiApiMacAddressFormat()");
		String mac[] = macAddress.split(BroadBandTestConstants.CHARACTER_COLON.toString());
		StringBuilder macResponse = new StringBuilder();
		for (int count = BroadBandTestConstants.CONSTANT_0; count < mac.length; count++) {
			if (mac[count].length() == BroadBandTestConstants.CONSTANT_1) {
				mac[count] = BroadBandTestConstants.STRING_ZERO + mac[count];
			}
			if (count != mac.length - BroadBandTestConstants.CONSTANT_1) {
				mac[count] = mac[count] + BroadBandTestConstants.CHARACTER_COLON;
			}

			macResponse.append(mac[count]);
		}
		LOGGER.debug("ENDING METHOD: wifiApiMacAddressFormat()");
		return macResponse.toString();
	}

	/**
	 * Helper method to execute command in ATOM console with polling method
	 * 
	 * @param tapEnv       {@link AutomaticsTapApi}
	 * @param device       {@link Dut}
	 * @param command      command to execute
	 * @param pollDuration maximum poll duration
	 * @param pollInterval polling interval
	 * @return searchResponse response for search text
	 * @author ArunKumar Jayachandran
	 * @refactor Govardhan
	 */
	public static String executeCommandInAtomConsoleByPolling(AutomaticsTapApi tapEnv, Dut device, String command,
			long pollDuration, long pollInterval) {
		LOGGER.debug("STARTING METHOD executeCommandInAtomConsoleByPolling");
		String response = null;
		long startTime = System.currentTimeMillis();
		do {
			response = BroadBandCommonUtils.executeCommandInAtomConsoleIfAtomIsPresentElseInArm(device, tapEnv,
					command);
			response = CommonMethods.isNotNull(response) && (!CommonUtils
					.isGivenStringAvailableInCommandOutput(response, BroadBandTestConstants.COMMAND_NOT_FOUND_ERROR)
					|| !CommonUtils.isGivenStringAvailableInCommandOutput(response,
							BroadBandTestConstants.NO_SUCH_FILE_OR_DIRECTORY)) ? response.trim() : null;
		} while ((System.currentTimeMillis() - startTime) < pollDuration && CommonMethods.isNull(response)
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, pollInterval));
		LOGGER.info("COMMAND OUTPUT FOR - " + command + " IS : " + response);
		LOGGER.debug("ENDING METHOD executeCommandInAtomConsoleByPolling");
		return response;
	}

	/**
	 * Method to copy file, replace text in file, mount copy bind file and remove
	 * file
	 * 
	 * Note: To remove the mount copy bind in original file caused by this method,
	 * we should either unmount the file using umount command or reboot the device
	 * 
	 * @param tapEnv            {@link AutomaticsTapApi}
	 * @param device            {@link Dut}
	 * @param originalFilePath  string containing path of file to be copied
	 * @param copyToDirectory   string containing destination to be copied
	 * @param textBeforeReplace string containing text in file to be replaced
	 * @param textAfterReplace  string containing new text to be put in file
	 * 
	 * @return true if file copied, text replaced and mount copy bind success
	 * @refactor Govardhan
	 */
	public static boolean copyReplaceTextMountCopyBindRemoveFile(Dut device, AutomaticsTapApi tapEnv,
			String originalFilePath, String copyToDirectory, String textBeforeReplace, String textAfterReplace) {

		LOGGER.debug("Entering method: copyReplaceTextMountCopyBindRemoveFile");
		boolean status = false;
		String tempFilePath = concatStringUsingStringBuffer(copyToDirectory,
				originalFilePath.substring(originalFilePath.lastIndexOf(BroadBandTestConstants.FORWARD_SLASH_CHARACTER)
						+ BroadBandTestConstants.CONSTANT_1));

		tapEnv.executeCommandUsingSsh(device,
				BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.CMD_COPY_FILES,
						BroadBandTestConstants.SINGLE_SPACE_CHARACTER, originalFilePath,
						BroadBandTestConstants.SINGLE_SPACE_CHARACTER, copyToDirectory));
		if (CommonUtils.replaceTextInFileUsingSed(tapEnv, device, textBeforeReplace, textAfterReplace, tempFilePath)) {
			tapEnv.executeCommandUsingSsh(device,
					BroadBandCommonUtils.concatStringUsingStringBuffer(
							BroadBandCommandConstants.CMD_SBIN_MOUNT_COPYBIND, tempFilePath,
							BroadBandTestConstants.SINGLE_SPACE_CHARACTER, originalFilePath));
			status = CommonMethods
					.isNotNull(BroadBandCommonUtils.searchLogFiles(tapEnv, device, textAfterReplace, originalFilePath));
			CommonUtils.removeFileandVerifyFileRemoval(tapEnv, device, tempFilePath);
		} else {
			LOGGER.error("Unable to replace interface name in /nvram/advsec.sh using sed command");
		}

		LOGGER.info("Result of copy, replace text and mount copybind file: " + status);
		LOGGER.debug("Exiting method: copyReplaceTextMountCopyBindRemoveFile");
		return status;
	}

	/**
	 * Helper method to convert list of strings to lower case characters
	 * 
	 * 
	 * @param response List of String which needs to be converted to lower case
	 *                 characters
	 * 
	 * @return list containing lower case characters
	 * @refactor Said Hisham
	 * 
	 */
	public static List<String> convertListOfStringsToLowerCase(List<String> response) {
		LOGGER.debug("Starting Method : convertListOfStringsToLowerCase()");
		List<String> listOfStringsWithLowerCaseCharacters = new ArrayList<String>();
		try {
			for (int iterationOverListOfStrings = 0; iterationOverListOfStrings < response
					.size(); iterationOverListOfStrings++) {
				listOfStringsWithLowerCaseCharacters.add(response.get(iterationOverListOfStrings).toLowerCase());
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred in convertListOfStringsToLowerCase.Error -" + e.getMessage());
		}
		LOGGER.debug("Starting Method : ()convertListOfStringsToLowerCase");
		return listOfStringsWithLowerCaseCharacters;
	}

	/**
	 * Utility method to verify the logs in Atom or Arm Console
	 * 
	 * @param tapEnv      instance of {@link AutomaticsTapApi}
	 * @param device      instance of {@link Dut}
	 * @param searchText  text that needs to be searched in the log file
	 * @param logFileName log file name to be searched
	 * @param isAtom      is atom available or not
	 * 
	 * @return true if the searchText is not logged in the log file
	 * @refactor Said Hisham
	 */
	public static boolean verifyLogsInAtomOrArmConsoleWithErrorLogValidations(AutomaticsTapApi tapEnv, Dut device,
			String searchText, String logFileName, boolean isAtom) {
		LOGGER.debug("STARTING METHOD : verifyLogsInAtomOrArmConsoleWithErrorLogValidations");
		boolean result = false;
		String response = null;
		try {
			response = isAtom ? BroadBandCommonUtils.searchAtomConsoleLogs(tapEnv, device, searchText, logFileName)
					: BroadBandCommonUtils.searchLogFiles(tapEnv, device, searchText, logFileName);
			result = CommonMethods.isNull(response) || (CommonMethods.isNotNull(response)
					&& !CommonUtils.patternSearchFromTargetString(response, searchText)
					&& !CommonUtils.patternSearchFromTargetString(response, BroadBandTestConstants.NO_ROUTE_TO_HOST)
					&& !CommonUtils.patternSearchFromTargetString(response,
							BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_CONNECTION_TIMEOUT_MESSAGE)
					&& !CommonUtils.patternSearchFromTargetString(response,
							BroadBandTestConstants.STRING_CONNECTION_REFUSED)
					&& !CommonUtils.patternSearchFromTargetString(response,
							BroadBandTestConstants.AUTHENTICATION_FAILED));
		} catch (Exception exception) {
			LOGGER.error("Exception occurrd while verifying logs in arm or atom console with Error log validations: "
					+ exception.getMessage());
		}
		LOGGER.debug("ENDING METHOD : verifyLogsInAtomOrArmConsoleWithErrorLogValidations");
		return result;
	}

	/**
	 * @param response response to convert
	 * @author Sathurya Ravi
	 * @refactor Athira
	 */

	public static String convertIntegerToString(int numberToConvert) {
		try {
			return Integer.toString(numberToConvert);
		} catch (NumberFormatException nfe) {
			LOGGER.error("Number format exception occured while trying to convert: " + numberToConvert, nfe);
		}
		return null;
	}

	/**
	 * Method to verify reboot status after CR process crash
	 * 
	 * @param tapEnv   {@link AutomaticsTapApi}
	 * @param device   {@link Dut}
	 * @param waitTime waitTime
	 * @return true if the device is in reboot state
	 * 
	 * @author Gnanaprakasham
	 * @refactor Govardhan
	 */

	public static boolean verifyRebootStatusAfterSpecificWaitTime(Dut device, AutomaticsTapApi tapEnv,
			String rebootReason, long waitTime) {

		LOGGER.debug("STARTING METHOD : verifyRebootStatusAfterSpecificWaitTime");
		// Variable to store execution status
		boolean status = false;

		String errorMessage = null;

		// CR process crash will take ~20 mins to initiate reboot
		// So check device accessibility for every 1 min
		long delayDuration = BroadBandTestConstants.ONE_MINUTE_IN_MILLIS;

		LOGGER.info("Delay duration time for polling : " + delayDuration);

		status = isRdkbDeviceAccessible(tapEnv, device, delayDuration, waitTime, false);
		LOGGER.info("is device is in reboot state : " + status);
		if (status) {
			LOGGER.info("Going to wait for IP acquitiosition ");
			status = CommonMethods.waitForEstbIpAcquisition(tapEnv, device);
			if (!status) {
				errorMessage = "Device is not accessible after reboot initiated by " + rebootReason;
				throw new TestException(errorMessage);
			}
		} else {
			errorMessage = rebootReason + " is not initiating reboot even 20 minutes after process crash";

			throw new TestException(errorMessage);
		}
		LOGGER.debug("ENDING METHOD : verifyRebootStatusAfterSpecificWaitTime");
		return status;
	}

	/**
	 * Method to get the search log file on firmware download for different models
	 * 
	 * @param device instance of {@link Dut}
	 * 
	 * @return search log file name based on device model
	 * @refactor Said Hisham
	 */
	public static String getLogFileNameOnFirmwareDownload(Dut device) {
		LOGGER.debug("STARTING METHOD: getLogFileNameOnFirmwareDownload()");
		String logFile = null;
		if (DeviceModeHandler.isDSLDevice(device)) {
			logFile = BroadBandCdlConstants.WANAGENT_LOG;
		} else {
			logFile = ((DeviceModeHandler.isFibreDevice(device)) ? BroadBandCdlConstants.EPONAGENT_LOG
					: BroadBandCdlConstants.CM_LOG_TXT_0);
		}
		LOGGER.debug("ENDING METHOD: getLogFileNameOnFirmwareDownload()");
		return logFile;
	}

	/**
	 * Utility method to Cross verify the the log upload file name with uptime.
	 * 
	 * @param device device
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @return true if Log Upload File name is latest with time greater than the
	 *         uptime and upload is successful.
	 * @refactor Said Hisham
	 */
	public static boolean verifyLatestLogUploadSuccessful(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("STARTING METHOD : verifyLatestLogUploadSuccessful()");
		boolean status = false;
		String uptimeObtainedViaWebpa = null;
		String uploadedFileNameResponse = null;
		String uploadSuccessfulResponse = null;
		long startTime = 0L;
		String upTimeObtainedViaCommand = null;
		String uploadedFileName = null;
		try {
			startTime = System.currentTimeMillis();
			do {
				if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
					uploadSuccessfulResponse = tapEnv.executeCommandUsingSsh(device,
							BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND,
									BroadBandCommandConstants.LOG_UPLOAD_SUCCESS_TXT_GREP_FROM_LOGS,
									BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
									BroadBandCommandConstants.FILE_ARMCONSOLELOG));
					uploadedFileNameResponse = tapEnv.executeCommandUsingSsh(device,
							BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND,
									BroadBandCommandConstants.LOG_UPLOAD_FILE_NAME_GREP_FROM_LOGS,
									BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
									BroadBandCommandConstants.FILE_ARMCONSOLELOG));
				} else {
					uploadSuccessfulResponse = tapEnv.executeCommandUsingSsh(device,
							BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND,
									BroadBandCommandConstants.LOG_UPLOAD_SUCCESS_TXT_GREP_FROM_LOGS,
									BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
									BroadBandCommandConstants.FILE_CONSOLELOG));
					uploadedFileNameResponse = tapEnv.executeCommandUsingSsh(device,
							BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND,
									BroadBandCommandConstants.LOG_UPLOAD_FILE_NAME_GREP_FROM_LOGS,
									BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
									BroadBandCommandConstants.FILE_CONSOLELOG));
				}
				status = CommonMethods.isNotNull(uploadSuccessfulResponse)
						&& CommonMethods.isNotNull(uploadedFileNameResponse);
			} while (!status
					&& ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.TWENTY_MINUTES_IN_MILLIS)
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
			upTimeObtainedViaCommand = tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_UPTIME);
			uptimeObtainedViaWebpa = tapEnv.executeWebPaCommand(device,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_UPTIME);
			if (CommonMethods.isNotNull(uploadSuccessfulResponse) && CommonMethods.isNotNull(uploadedFileNameResponse)
					&& CommonMethods.isNotNull(upTimeObtainedViaCommand)
					&& CommonMethods.isNotNull(uptimeObtainedViaWebpa)) {
				uploadedFileName = CommonMethods.patternFinder(uploadedFileNameResponse,
						BroadBandTestConstants.LOG_UPLOAD_PATTERN_MATCHER);
				LOGGER.info("UPLOADED FILE NAME IS : " + uploadedFileName);
				LOGGER.info("RESPONSE FOR UPTIME VIA WEBPA : " + uptimeObtainedViaWebpa);
				LOGGER.info("RESPONSE FOR UPTIME COMMAND : " + upTimeObtainedViaCommand);
				status = verifyLatestLogUploadFileName(uploadedFileName, uptimeObtainedViaWebpa,
						upTimeObtainedViaCommand);
			} else {
				LOGGER.error("Invalid/Null response obtained for either of the following values \n"
						+ " 1. uptime via WEBPA \n" + "	2. uptime via Command \n"
						+ "	3. Latest Log upload Filename \n" + "	4. Log Upload success text");
				status = false;
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while cross verifying the Log Upload Successful status :" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: verifyLatestLogUploadSuccessful()");
		return status;
	}

	/**
	 * Utility method to Cross verify the the log upload file name with uptime.
	 * 
	 * @param logUploadfileName        instance to hold string value of log upload
	 *                                 file name
	 * @param uptimeObtainedViaWebpa   instance to hold string value of uptime
	 *                                 obtained via WEBPA
	 * @param upTimeObtainedViaCommand instance to hold string value of uptime
	 *                                 obtained via Command
	 * @return true if Log Upload File name is latest with time greater than the
	 *         uptime of the device after reboot.
	 * @refactor Said Hisham
	 */
	public static boolean verifyLatestLogUploadFileName(String logUploadfileName, String uptimeObtainedViaWebpa,
			String upTimeObtainedViaCommand) {
		LOGGER.debug("STARTING METHOD : verifyLatestLogUploadFileName()");
		boolean status = false;
		long hoursFromFileName = 0L;
		long minutesFromFileName = 0L;
		long totalTimeInSecondsFromFileName = 0L;
		long hoursFromUpTimeCommandResponse = 0L;
		long minutesFromUpTimeCommandResponse = 0L;
		long secondsFromUpTimeCommandResponse = 0L;
		long totalTimeInSecondsFromUpTimeCommandResponse = 0L;
		long timeDifference = 0L;
		long uptimeViaWebpa = 0L;
		try {
			// Since file name is in 12hrs format, Converting into 24hrs format to cross
			// check with uptime of the device
			// which is in 24hrs format and Converting hours into seconds
			hoursFromFileName = CommonMethods.patternMatcher(logUploadfileName,
					BroadBandTestConstants.LOG_UPLOAD_FILE_NAME_WITH_PM_PATTERN_MATCHER)
							? (convertStringToLong(CommonMethods.patternFinder(logUploadfileName,
									BroadBandTestConstants.GET_HOURS_FROM_LOG_UPLOAD_FILE_NAME_PATTERN_MATCHER))
									+ BroadBandTestConstants.CONSTANT_12)
									* BroadBandTestConstants.INTERGER_CONSTANT_3600
							: (convertStringToLong(CommonMethods.patternFinder(logUploadfileName,
									BroadBandTestConstants.GET_HOURS_FROM_LOG_UPLOAD_FILE_NAME_PATTERN_MATCHER)))
									* BroadBandTestConstants.INTERGER_CONSTANT_3600;
			// Converting minutes into seconds
			minutesFromFileName = convertStringToLong(CommonMethods.patternFinder(logUploadfileName,
					BroadBandTestConstants.GET_MINUTES_FROM_LOG_UPLOAD_FILE_NAME_PATTERN_MATCHER))
					* BroadBandTestConstants.CONSTANT_60;
			totalTimeInSecondsFromFileName = hoursFromFileName + minutesFromFileName;
			LOGGER.info("Hours converted to seconds from File name is : " + hoursFromFileName);
			LOGGER.info("Minutes converted to seconds obtained from File name is : " + minutesFromFileName);
			LOGGER.info("Total Time in seconds obtained from file name : " + totalTimeInSecondsFromFileName);

			// Converting hours into seconds for response obtained from uptime command
			hoursFromUpTimeCommandResponse = convertStringToLong(CommonMethods.patternFinder(upTimeObtainedViaCommand,
					BroadBandTestConstants.GET_HOURS_FROM_UPTIME_VALUE_PATTERN_MATCHER))
					* BroadBandTestConstants.INTERGER_CONSTANT_3600;
			// Converting minutes into seconds for respose obtained from uptime command
			minutesFromUpTimeCommandResponse = convertStringToLong(CommonMethods.patternFinder(upTimeObtainedViaCommand,
					BroadBandTestConstants.GET_MINUTES_FROM_UPTIME_VALUE_PATTERN_MATCHER))
					* BroadBandTestConstants.CONSTANT_60;
			secondsFromUpTimeCommandResponse = convertStringToLong(CommonMethods.patternFinder(upTimeObtainedViaCommand,
					BroadBandTestConstants.GET_SECONDS_FROM_UPTIME_VALUE_PATTERN_MATCHER));
			totalTimeInSecondsFromUpTimeCommandResponse = hoursFromUpTimeCommandResponse
					+ minutesFromUpTimeCommandResponse + secondsFromUpTimeCommandResponse;
			LOGGER.info(
					"Hours converted to seconds from UpTime Command response is : " + hoursFromUpTimeCommandResponse);
			LOGGER.info("Minutes converted to seconds obtained from UpTime Command response is : "
					+ minutesFromUpTimeCommandResponse);
			LOGGER.info("Seconds obtained from UpTime Command response is : " + secondsFromUpTimeCommandResponse);
			LOGGER.info("Total Time in seconds obtained from UpTime Command response is : "
					+ totalTimeInSecondsFromUpTimeCommandResponse);

			// convert the string value of uptime obtained via WEBPA to long
			uptimeViaWebpa = convertStringToLong(uptimeObtainedViaWebpa);
			// Time Difference = Current Device time - uptime (exact time when device came
			// up online after reboot)
			timeDifference = totalTimeInSecondsFromUpTimeCommandResponse - uptimeViaWebpa;
			LOGGER.info("Actual Time (in seconds) when device came online after reboot is : " + timeDifference);
			LOGGER.info(
					"Actual Time (in seconds) of log file when it got uploaded : " + totalTimeInSecondsFromFileName);
			// Time Obtained from file name should be Greater than the device uptime after
			// reboot and difference between
			// log upload file name time and device up time should be less than or equal to
			// 30 minutes
			status = (totalTimeInSecondsFromFileName > timeDifference
					&& ((totalTimeInSecondsFromFileName - timeDifference) <= BroadBandTestConstants.CONSTANT_1800));
		} catch (Exception e) {
			LOGGER.error("Exception occured while cross verifying the latest file name with uptime of the device :"
					+ e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: verifyLatestLogUploadFileName()");
		return status;
	}

	/**
	 * Utility method to Convert string to Long Value.
	 * 
	 * @param stringToConvert String which needs to be converted to Long value.
	 *
	 * @return Long value post conversion.
	 * @refactor Said Hisham
	 */
	public static long convertStringToLong(String stringToConvert) {
		LOGGER.debug("STARTING METHOD : convertStringToLong()");
		long longValueAfterConversion = 0L;
		try {
			longValueAfterConversion = Long.parseLong(stringToConvert);
		} catch (NumberFormatException nfe) {
			LOGGER.error(
					"Number format Exception occured while trying to parse the string to Double -  " + stringToConvert,
					nfe);
		}
		LOGGER.debug("ENDING METHOD: convertStringToLong()");
		return longValueAfterConversion;
	}

	/**
	 * Method to ssh to the WanIpv6 address on jump server and return the status.
	 * 
	 * @param tapEnv
	 * @param wanIpv6address
	 * @refactor Alan_Bivera
	 * 
	 */
	public static boolean executeSshCommandOnJumpServer(AutomaticsTapApi tapEnv, Dut device, String wanIpv6) {
		Boolean status = false;

		LOGGER.debug("STARTING METHOD :executeSshCommandOnJumpServer");
		String command = CommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.LINUX_CMD_SUDO_STB_SSH_IPV6,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, wanIpv6, BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
				BroadBandCommandConstants.CMD_ECHO_TEST_CONNECTION);
		LOGGER.info("Command is " + command);

		String response = tapEnv.executeCommandUsingSsh(device, command);
		LOGGER.info("the response is" + response);

		if (CommonMethods.isNotNull(response)
				&& CommonUtils.patternSearchFromTargetString(response, BroadBandTestConstants.STRING_TEST_CONNECTION)) {
			status = true;
		}
		LOGGER.debug("ENDING METHOD :executeSshCommandOnJumpServer");
		return status;
	}

	/**
	 * Utility method to verify given file exists in the ARM or ATOM Console.
	 * 
	 * @param tapEnv             {@link AutomaticsTapApi}
	 * @param device             {@link Dut}
	 * @param completeFolderPath String representing the complete path of the file.
	 * @param pollDuration       Maximum poll duration
	 * @param pollInterval       Polling interval time
	 * @param isAvailable        True or False value to verify
	 * 
	 * @return Object representing the BroadBandResultObject with status & message.
	 * 
	 * @author Betel Costrow
	 * @refactor Athira
	 */
	public static BroadBandResultObject doesDirectoryExistInArmElseAtomConsole(Dut device, AutomaticsTapApi tapEnv,
			String completeFolderPath, long pollDuration, long pollInterval, String isAvailable) {
		LOGGER.debug("STARTING METHOD doesFileExistInAtomConsole");
		boolean result = false;
		long startTime = System.currentTimeMillis();
		String response = null;
		do {
			tapEnv.waitTill(pollInterval);
			if (CommonMethods.isBoxHavingLxcSupport(device, tapEnv)) {
				response = CommonMethods.executeCommandInAtomConsole(device, tapEnv,
						"if [ -d " + completeFolderPath + " ] ; then echo \"true\" ; else echo \"false\" ; fi");
			} else {
				response = tapEnv.executeCommandUsingSsh(device,
						"if [ -d " + completeFolderPath + " ] ; then echo \"true\" ; else echo \"false\" ; fi");
			}
			LOGGER.info("COMMAND EXECUTION RESPONSE: " + response);
			if (CommonMethods.isNotNull(response)
					&& !response.contains(BroadBandTestConstants.NO_SUCH_FILE_OR_DIRECTORY)) {
				result = response.trim().equalsIgnoreCase(isAvailable);
			}
			if (result) {
				break;
			}
		} while ((System.currentTimeMillis() - startTime) < pollDuration && !result);
		String errorMessage = result ? BroadBandTestConstants.EMPTY_STRING
				: "FOLDER " + completeFolderPath + " IS NOT PRESENT IN DEVICE.";
		LOGGER.info("IS FOLDER " + completeFolderPath + " PRESENT IN DEVICE: " + result);
		BroadBandResultObject objResult = new BroadBandResultObject();
		objResult.setStatus(result);
		objResult.setErrorMessage(errorMessage);
		LOGGER.debug("ENDING METHOD doesDirectoryExistInAtomConsole");
		return objResult;
	}

	/**
	 * Helper method to check jst file present
	 * 
	 * @param tapEnv AutomaticsTapApi instance
	 * @param device {@link Dut}
	 * @return isJstPresent true or false
	 * 
	 * @author ArunKumar Jayachandran
	 * @refactor Said Hisham
	 */
	public static boolean isWebUiUsesJst(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("STARTING METHOD isWebUiUsesJst");
		boolean isJstPresent = false;
		if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
			isJstPresent = BroadBandCommonUtils.isFileExistsonAtom(device, tapEnv,
					BroadBandCommandConstants.FILE_PATH_JST);
		} else {
			isJstPresent = CommonMethods.isFileExists(device, tapEnv, BroadBandCommandConstants.FILE_PATH_JST);
		}
		LOGGER.info("JST file path present status: " + isJstPresent);
		LOGGER.debug("ENDING METHOD isWebUiUsesJst");
		return isJstPresent;
	}

	/**
	 * Method to retrive the status of Secure WebUI Enable
	 * 
	 * @param device {@link Dut}
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @return true if SecureWebUI is Enabled
	 * @refactor Said Hisham
	 */
	public static boolean isSecureWebUIEnabled(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("Starting of Method: isSecureWebUIEnabled");
		boolean status = false;
		try {
			status = BroadBandWebPaUtils.getAndVerifyWebpaValueInPolledDuration(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_TO_GET_AND_SET_SECURE_WEBUI, BroadBandTestConstants.TRUE,
					BroadBandTestConstants.ONE_MINUTE_IN_MILLIS, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);

		} catch (Exception e) {
			LOGGER.error("Exception occured while retrieving status of SecureWebUIEnabled  " + e.getMessage());
		}
		LOGGER.debug("Ending of Method: isSecureWebUIEnabled");
		return status;
	}

	/**
	 * This method captures the values of parameter
	 * Device.DeviceInfo.X_RDKCENTRAL-COM_FirmwareDownloadStatus during cdl process
	 * and adds them to Array
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param Dut    {@link device}
	 * 
	 * @return List of statuses that are captured
	 * 
	 * @refactor Alan_Bivera
	 */
	public static ArrayList<String> captureFirmWareDownloadStatusesDuringCdl(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("ENTERING METHOD : captureFirmWareDownloadStatusesDuringCdl");
		long pollDuration = BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS;
		long startTime = System.currentTimeMillis();
		String webPaResponse = null;
		ArrayList<String> cldStatus = new ArrayList<String>();
		do {
			webPaResponse = tapEnv.executeWebPaCommand(device, BroadBandCdlConstants.WEBPA_PARAM_CDL_STATUS);
			if (CommonMethods.isNotNull(webPaResponse) && !cldStatus.contains(webPaResponse)) {
				cldStatus.add(webPaResponse);
			}
			if (CommonMethods.isNull(webPaResponse)) {
				if (!CommonMethods.isSTBAccessible(device))
					break;
			}

		} while (!webPaResponse.equalsIgnoreCase(BroadBandCdlConstants.CDL_STATUS_COMPLETED)
				&& !webPaResponse.equalsIgnoreCase(BroadBandCdlConstants.CDL_STATUS_FAILED)
				&& (System.currentTimeMillis() - startTime) < pollDuration);
		LOGGER.info("Captured statuses of parameter " + BroadBandWebPaConstants.WEBPA_PARAM_CDL_STATUS + " are:"
				+ cldStatus.toString());
		LOGGER.debug("ENDING METHOD : captureFirmWareDownloadStatusesDuringCdl");
		if (cldStatus.size() > 0)
			return cldStatus;
		else
			return null;
	}

	/**
	 * Helper method to verify admin page password using syscfg command
	 * 
	 * @param tapEnv   AutomaticsTapApi instance
	 * @param device   Dut instance
	 * @param password password to be verified.
	 * @return true if password from syscfg command and password to be verified are
	 *         same.
	 * @author Praveenkumar Paneerselvam
	 * @refactor Rakesh C N
	 */
	public static boolean verifyAdminPagePasswordFromSyscfgCommand(AutomaticsTapApi tapEnv, Dut device,
			String password) {
		LOGGER.debug("STARTING METHOD: verifyAdminPagePasswordFromSyscfgCommand");
		boolean isPwdSame = false;
		String defaultPassword = null;
		String command = DeviceModeHandler.isBusinessClassDevice(device)
				? BroadBandWebGuiTestConstant.SSH_GET_DEFAULT_PASSWORD_BUSINESS_CLASS
				: BroadBandWebGuiTestConstant.SSH_GET_DEFAULT_PASSWORD;
		defaultPassword = tapEnv.executeCommandUsingSsh(device, command);
		LOGGER.info("Expected password is - " + password);
		LOGGER.info("Password obtained from syscfg db is - " + defaultPassword);
		/*
		 * Changes: 1. No change for Business class devices. 2. For residential devices,
		 * default value 'password' will be stored in syscfg db so we can validate the
		 * same 3. Once password is changed from default value, it will be removed from
		 * syscfg db so we validate null response
		 */

		// Password other than default value will be removed from syscfg db as per
		// so need to verify
		// null response
		LOGGER.info("Since password has been removed from syscfg db, we need to verify response is null");
		isPwdSame = CommonMethods.isNull(defaultPassword);

		LOGGER.info("Is the default password of the admin page is as expected - " + isPwdSame);
		LOGGER.debug("ENDING METHOD: verifyAdminPagePasswordFromSyscfgCommand");
		return isPwdSame;
	}

	/**
	 * Helper method to verify admin page password is hashed using syscfg command
	 * 
	 * @param tapEnv   AutomaticsTapApi instance
	 * @param device   Dut instance
	 * @param password password to be verified.
	 * @return true if password from syscfg command and password to be verified are
	 *         not same.
	 * @author Praveenkumar Paneerselvam
	 * @refactor Rakesh C N
	 */
	public static boolean verifyAdminPageHashPasswordFromSyscfgCommand(AutomaticsTapApi tapEnv, Dut device,
			String password) {
		LOGGER.debug("STARTING METHOD: verifyAdminPageHashPasswordFromSyscfgCommand");
		boolean isPwdSame = false;
		String defaultPassword = null;
		String command = DeviceModeHandler.isBusinessClassDevice(device)
				? BroadBandWebGuiTestConstant.SSH_GET_DEFAULT_HASH_PASSWORD_BUSINESS_CLASS
				: BroadBandWebGuiTestConstant.SSH_GET_DEFAULT_HASH_PASSWORD;
		defaultPassword = tapEnv.executeCommandUsingSsh(device, command);
		if (CommonMethods.isNotNull(defaultPassword)) {
			isPwdSame = !defaultPassword.equals(password);
		} else {
			LOGGER.error("Failed to get Password using the command - " + command);
		}
		LOGGER.info("Is the default password of the admin page is as expected - " + isPwdSame);
		LOGGER.debug("ENDING METHOD: verifyAdminPageHashPasswordFromSyscfgCommand");
		return isPwdSame;
	}

	/**
	 * Helper method to get the connected client detail using Host Ipv4 address
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @param String containing Host Ipv4 address of the connected client
	 * @param String containing the WEB PA parameter
	 * 
	 * @return String containing value of the webPa param
	 * 
	 * @author Parvathy Premachandran Nair
	 * 
	 * @Refactor Sruthi Santhosh
	 */
	public static String getConnectedClientDetailUsingHostIpAddress(Dut device, AutomaticsTapApi tapEnv,
			String ipAddress, String webPaParam) {
		LOGGER.debug("STARTING METHOD: getConnectedClientDetailUsingIpAddress()");
		// Variable declaration starts
		String detail = null;
		int hostNoOfEntries = 0;
		// Variable declaration ends
		try {
			hostNoOfEntries = Integer.parseInt(tapEnv.executeWebPaCommand(device,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_HOST_NUMBER_OF_ENTRIES));
			if (hostNoOfEntries != 0) {
				for (int index = 1; index <= hostNoOfEntries; index++) {
					String response = tapEnv.executeWebPaCommand(device,
							BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_HOST_IPV4_ADDRESS
									.replace(BroadBandTestConstants.TR181_NODE_REF, Integer.toString(index)));
					if (BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON, ipAddress,
							response.trim())) {
						detail = tapEnv.executeWebPaCommand(device,
								webPaParam.replace(BroadBandTestConstants.TR181_NODE_REF, Integer.toString(index)));
					}
				}
			} else {
				LOGGER.error("Unable to get Host number of entries.");
			}
		} catch (Exception e) {
			LOGGER.error(
					"Exception occured while getting detail of client via Ipv4 address in getConnectedClientDetailUsingIpAddress()");
		}
		LOGGER.debug("ENDING METHOD: getConnectedClientDetailUsingIpAddress()");
		return detail;
	}

	/**
	 * Method used to calculate the device Up time After Firmware update reboot
	 * 
	 * @param device {@link Dut}
	 * @param tapEnv {@link AutomaticsTapApi}
	 * 
	 * @return true if Actual and Expected bootTime is equal
	 * 
	 * @refactor yamini.s
	 */

	public static BroadBandResultObject calculateDeviceUpTime(Dut device, AutomaticsTapApi tapEnv) {
		BroadBandResultObject result = new BroadBandResultObject();
		boolean status = false;
		String errorMessage = null;
		String response = null;
		String upTime = tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_UPTIME);
		errorMessage = "Unable to get uptime from device using WebPA request with parameter: "
				+ BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_UPTIME;
		LOGGER.info("Uptime value from device is: " + upTime);
		if (CommonMethods.isNotNull(upTime)) {
			long expectedBootTime = (new Date().getTime() / BroadBandTestConstants.ONE_SECOND_IN_MILLIS)
					- Long.valueOf(upTime.trim());
			LOGGER.info("Expected Boot Time is: " + expectedBootTime);
			response = tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_BOOTTIME);
			LOGGER.info("Boot time value from device through WebPA request is: " + response);
			errorMessage = "Failed to get response through WebPA for BootTime parameter "
					+ BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_BOOTTIME + ". WebPA response -" + response;
			if (CommonMethods.isNotNull(response)) {
				status = ((expectedBootTime - Long.valueOf(response.trim()) >= BroadBandTestConstants.CONSTANT_0)
						&& (expectedBootTime - Long.valueOf(response.trim()) < BroadBandTestConstants.CONSTANT_5));
				errorMessage = "Actual and Expected bootTime is not equal";
			}
		}
		result.setErrorMessage(errorMessage);
		result.setStatus(status);
		return result;
	}

	/**
	 * Validating the invalid/valid response in log message in
	 * /rdklogs/logs/PAMlog.txt.0
	 * 
	 * 
	 * @param device               Instance of{@link Dut}
	 * @param tapEnv               Instance of{@link AutomaticsTapApi}
	 * @param isValidRespAvailable -True -validating the valid response, False -
	 *                             Validating the invalid response
	 * @param currentTimeStamp     - Current time stamp from device
	 * @return True - Expected log message is present, Else-False
	 * @refactor Said Hisham
	 */
	public static boolean validateLogMessageInPamLog(Dut device, AutomaticsTapApi tapEnv, boolean isValidRespAvailable,
			String currentTimeStamp) {
		LOGGER.info("STARTING METHOD : validateLogMessageInPamLog()");
		String response = null;
		boolean status = false;
		String searchText = isValidRespAvailable ? BroadBandTraceConstants.LOG_MESSAGE_SETTING_VAILD_SUBNET_MASK_VALUE
				: BroadBandTraceConstants.LOG_MESSAGE_SETTING_INVAILD_SUBNET_MASK_VALUE;
		try {
			long startTime = System.currentTimeMillis();
			String command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND,
					BroadBandTestConstants.TEXT_DOUBLE_QUOTE, searchText, BroadBandTestConstants.TEXT_DOUBLE_QUOTE,
					BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.COMMAND_NTP_LOG_FILE,
					BroadBandTestConstants.SYMBOL_PIPE, BroadBandTestConstants.CMD_TAIL_1);
			do {
				response = tapEnv.executeCommandUsingSsh(device, command);
				status = CommonMethods.isNotNull(response)
						&& BroadBandCommonUtils.verifyLogUsingTimeStamp(currentTimeStamp, response);
			} while (!status
					&& (System.currentTimeMillis() - startTime) < BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.TEN_SECOND_IN_MILLIS));
			LOGGER.info(searchText + " is present :" + status);
		} catch (Exception e) {
			LOGGER.error("Exception occurres while validating the log message" + e.getMessage());

		}
		LOGGER.info("ENDING METHOD: validateLogMessageInPamLog()");
		return status;
	}

	/**
	 * Helper method to convert time from different units to seconds
	 * 
	 * @param timeValue Value that has to be converted to seconds
	 * @param timeUnit  The Time Unit that the value is currently in
	 * @return convertedTime Converted Time in seconds
	 * 
	 * @refactor Athira
	 * 
	 */
	public static int convertTimeToSeconds(int timeValue, String timeUnit) {
		LOGGER.debug("STARTING METHOD: convertTimeToSeconds");
		LOGGER.info("Obtained TIME VALUE is : '" + timeValue + "', Which is in UNIT : '" + timeUnit + "'");
		int convertedTime = 0;
		switch (timeUnit) {
		case "seconds":
			convertedTime = timeValue;
			break;
		case "minutes":
			convertedTime = timeValue * 60;
			break;
		case "hours":
			convertedTime = timeValue * 3600;
			break;
		case "days":
			convertedTime = timeValue * 3600 * 24;
			break;
		case "weeks":
			convertedTime = timeValue * 3600 * 24 * 7;
			break;
		case "forever":
			convertedTime = -1;
			break;
		}
		LOGGER.info(timeValue + " " + timeUnit + " IS '" + convertedTime + "' SECONDS");
		LOGGER.debug("ENDING METHOD: convertTimeToSeconds");
		return convertedTime;
	}

	/**
	 * Method to wait for device to go for reset and to turn up
	 * 
	 * @param device
	 * @param tapEnv
	 * @return true if box turns up, else false
	 * 
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean waitAndCheckDeviceResetInDuration(Dut device, AutomaticsTapApi tapEnv, long duration) {
		boolean status = false;
		long startTime = System.currentTimeMillis();
		do {
			if (!CommonMethods.isSTBAccessible(device)) {
				LOGGER.info("STB is not accessible!. Reboot has initiated successfully");
				if (CommonMethods.waitForEstbIpAcquisition(tapEnv, device)) {
					LOGGER.info("Waiting for one Minute for the device to come up with all properties");
					tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
					status = true;
				}
			}
		} while ((System.currentTimeMillis() - startTime) < duration && !status
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.TEN_SECOND_IN_MILLIS));
		return status;
	}

	/**
	 * Method to get DHCP Range Based on Existing Range
	 * 
	 * @param ip4AddressRetrievedFromClient
	 * @return List which contains DHCP Start Address and End Address
	 * 
	 * @Refactor Sruthi Santhosh
	 */
	public static List<String> getDhcpRangeBasedOnEsistingRange(String ip4AddressRetrievedFromClient) {
		String dhcpStartAddress = "";
		String dhcpEndAddress = "";
		String[] ipSplitedArray = new String[4];
		List<String> dhcpAddresses = new ArrayList<>();

		/** Splitting IP address */
		ipSplitedArray = ip4AddressRetrievedFromClient.split("\\.");

		/** Assigning DHCP Start Address and End Address */
		if (Integer.parseInt(ipSplitedArray[ipSplitedArray.length - 1]) + 20 < 253) {
			dhcpStartAddress = Integer.toString(Integer.parseInt(ipSplitedArray[ipSplitedArray.length - 1]) + 1);
			dhcpEndAddress = Integer.toString(Integer.parseInt(ipSplitedArray[ipSplitedArray.length - 1]) + 20);
		} else if (Integer.parseInt(ipSplitedArray[ipSplitedArray.length - 1]) + 20 >= 253) {
			dhcpStartAddress = Integer.toString(Integer.parseInt(ipSplitedArray[ipSplitedArray.length - 1]) - 20);
			dhcpEndAddress = Integer.toString(Integer.parseInt(ipSplitedArray[ipSplitedArray.length - 1]) - 1);
		}
		/** Adding DHCP Starting Address in List */
		dhcpAddresses.add(dhcpStartAddress);
		/** Adding DHCP End Address in List */
		dhcpAddresses.add(dhcpEndAddress);

		return dhcpAddresses;
	}

	/**
	 * Method to Verify Configured DHCP Address
	 * 
	 * @param DHCPAddAfterDHCPConfiguration
	 * @param valueToBeVerified
	 * 
	 * @return true if configured value verified
	 * 
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean verifyConfiguredDhcpAddress(String DHCPAddAfterDHCPConfiguration, String valueToBeVerified) {
		boolean isConfiguredIpVerified = false;
		String[] ipSplitedArray = new String[4];

		/** Splitting IP address */
		ipSplitedArray = DHCPAddAfterDHCPConfiguration.split("\\.");
		/** Verifying Configured DHCP Address */
		isConfiguredIpVerified = BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
				valueToBeVerified, ipSplitedArray[ipSplitedArray.length - 1]);

		return isConfiguredIpVerified;
	}

	/**
	 * Method to add default DHCP values in Map
	 * 
	 * @return Map which contains all DHCP default values
	 * 
	 * @Refactor Sruthi Santhosh
	 */
	public static HashMap<String, String> addDefaultDhcpValuesinMap(Dut device) {
		HashMap<String, String> defaultDchpIpv4ValuesMap = new HashMap<String, String>();
		LOGGER.info("Insdie addDefaultDhcpValuesinMap");
		try {
			defaultDchpIpv4ValuesMap.put(BroadBandTestConstants.DEFAULT_IPV4_SUBNET_MASK,
					BroadBandTestConstants.STRING_DNS_IP_WITH_SUBNET_MASK);
			defaultDchpIpv4ValuesMap.put(BroadBandTestConstants.DEFAULT_DHCPV4_BEGIN_ADDRESS,
					DeviceModeHandler.isBusinessClassDevice(device)
							? BroadBandTestConstants.STRING_DHCP_MIN_ADDRESS_BUSSI
							: BroadBandTestConstants.LAN_START_IP_ADDRESS_DEFAULT);
			defaultDchpIpv4ValuesMap.put(BroadBandTestConstants.DEFAULT_DHCPV4_ENDING_ADDRESS,
					DeviceModeHandler.isBusinessClassDevice(device) ? BroadBandTestConstants.STRING_DHCP_MAX_ADDRESS_BCI
							: BroadBandTestConstants.LAN_END_IP_ADDRESS_DEFAULT);
			defaultDchpIpv4ValuesMap.put(BroadBandTestConstants.DEFAULT_DHCPLEASE_TIME,
					BroadBandTestConstants.STRING_LEASE_TIME_DEFAULT_VALUE);
		} catch (Exception exec) {
			LOGGER.error("Failed to get the default DHCP IPv4 Values" + exec.getMessage());
			throw new TestException(
					BroadBandTestConstants.PRE_CONDITION_ERROR + "FAILED TO GET THE DEFAULT DHCP IPV4 VALUES");
		}
		return defaultDchpIpv4ValuesMap;
	}

	/**
	 * Method to retrieve ACS status using qcsapi commands such as for Specific
	 * devices trigger commands for 2.4 and 5 GHz "qcsapi_sockraw host0
	 * 00:11:22:AB:CD:EE get_auto_channel wifi0_0" and "qcsapi_sockraw host0
	 * 00:11:22:AB:CD:EE get_auto_channel wifi2_0" For specific device trigger
	 * commands for 2.4 and 5 GHz "qcsapi_pcie get_auto_channel wifi0_0" and
	 * "qcsapi_pcie get_auto_channel wifi2_0" For atom trigger commands for 2.4 and
	 * 5 GHz in atom console "cfg -s | grep AP_PRIMARY_CH"
	 * 
	 * @param device - device instance
	 * @param band   - command for 2.4GHz or 5 GHz
	 * @return status - True or false based on the response for ACS commands
	 * @author Deepika Sekar
	 * @refactor Said hisham
	 * 
	 **/
	public static boolean retrieveACSStatusUsingSystemCommands(Dut device, AutomaticsTapApi tapEnv, String band) {
		LOGGER.debug("Starting of Method: retrieveACSStatusUsingSystemCommands");
		String response = null;
		boolean status = false;
		String deviceModel = device.getModel().toLowerCase();
		String[] commandList = null;
		Map<String, String> systemCommands_Map = new HashMap<String, String>();
		Map<String, String> expectedResponse_Map = new HashMap<String, String>();
		try {
			if ((band.equalsIgnoreCase(BroadBandTestConstants.BAND_5GHZ))) {
				commandList = BroadbandPropertyFileHandler
						.getListOfCommandsForDevicesForAcsStatusUsingSystemCommands5ghz(device)
						.split(BroadBandTestConstants.SEMI_COLON);
			} else {
				commandList = BroadbandPropertyFileHandler
						.getListOfCommandsForDevicesForAcsStatusUsingSystemCommands(device)
						.split(BroadBandTestConstants.SEMI_COLON);
			}
			String[] responseList = BroadbandPropertyFileHandler
					.getListOfExpectedResponseForDevicesForAcsStatusUsingSystemCommands(device)
					.split(BroadBandTestConstants.SEMI_COLON);

			for (String eachCommand : commandList) {
				String[] systemCommands;
				systemCommands = eachCommand.split("=");
				systemCommands_Map.put(systemCommands[0].toString().trim(), systemCommands[1].toString().trim());
			}
			for (String eachResponse : responseList) {
				String[] expectedResponse;
				expectedResponse = eachResponse.split("=");
				expectedResponse_Map.put(expectedResponse[0].toString().trim(), expectedResponse[1].toString().trim());
			}

			for (String eachModel : systemCommands_Map.keySet()) {
				if (deviceModel.contains(eachModel)) {
					response = BroadBandCommonUtils.executeCommandInAtomConsoleIfAtomIsPresentElseInArm(device, tapEnv,
							systemCommands_Map.get(eachModel));
					LOGGER.info("the response is :" + response);
				}
				if (CommonMethods.isNotNull(response)) {
					for (String eachModels : expectedResponse_Map.keySet()) {
						if (deviceModel.contains(eachModels)) {
							status = response.toLowerCase().trim().contains(expectedResponse_Map.get(eachModels));
							if (!status) {
								status = response.toLowerCase().trim()
										.equalsIgnoreCase(expectedResponse_Map.get(eachModels));
							}
							LOGGER.info("status is :" + status);
						}
					}
				}
			}
			if (CommonMethods.isNull(response)) {
				LOGGER.error("This scenario is applicable only for atom, fiber and some specific devices");
			}

		} catch (Exception e) {
			LOGGER.error("Exception occured while retrieving ACS status" + e.getMessage());
		}
		LOGGER.debug("Ending of Method: retrieveACSStatusUsingSystemCommands");
		return status;
	}

	/**
	 * Utils method to get the Ipv4 begining and ending Address
	 * 
	 * @return the List of INTEGER containig IPV4 address between DHCP min max
	 *         values.
	 * 
	 * @refactor Athira
	 */
	public static List<Integer> getRandomDhcpV4BeginningAndEndingAddress() {
		LOGGER.debug("STARTING METHOD: getRandomDhcpV4BeginningAndEndingAddress");
		int dhcpIpv4BeginningValue = 0;
		int dhcpIpv4EndingValue = 0;
		List<Integer> dhcpBeginningAndEndingValue = new ArrayList<Integer>();
		dhcpIpv4BeginningValue = BroadBandSystemUtils.getRandomNumberBetweenRange(BroadBandTestConstants.CONSTANT_2,
				BroadBandTestConstants.CONSTANT_253);
		dhcpIpv4EndingValue = getIpv4EndingAddress(dhcpIpv4BeginningValue);
		LOGGER.info("DHCP IPV4 Random Brginning Value to be set is: " + dhcpIpv4BeginningValue);
		LOGGER.info("DHCP IPV4 Ending Value to be set is: " + dhcpIpv4EndingValue);
		if (isValidBeginningAndEndingIpv4Value(dhcpIpv4BeginningValue, dhcpIpv4EndingValue)) {
			dhcpBeginningAndEndingValue.add(dhcpIpv4BeginningValue);
			dhcpBeginningAndEndingValue.add(dhcpIpv4EndingValue);
		}
		LOGGER.debug("ENDING METHOD: getRandomDhcpV4BeginningAndEndingAddress");
		return dhcpBeginningAndEndingValue;
	}

	/**
	 * Utils method to get the Ipv4 begining Address
	 * 
	 * @param beginingAddress of DHCPV4 value
	 * 
	 * @return the IPV4 ending address between DHCP range
	 * 
	 * @refactor Athira
	 */

	public static int getIpv4EndingAddress(int beginingAddress) {
		LOGGER.debug("ENTERING METHOD getIpv4EndingAddress");
		int endingAddress = 0;
		if (beginingAddress >= BroadBandTestConstants.CONSTANT_248)
			endingAddress = BroadBandTestConstants.CONSTANT_253;
		else
			endingAddress = beginingAddress + BroadBandTestConstants.CONSTANT_5;

		LOGGER.debug("ENDING METHOD getIpv4EndingAddress");
		return endingAddress;
	}

	/**
	 * Utils method to get validate IPV4 beginning and ending value
	 * 
	 * @param dhcpIpv4BeginningValue minimum value for DHCPV4 that is to be
	 *                               validated
	 * 
	 * @param dhcpIpv4EndingValue    maximum value for DHCPV4 that is to be
	 *                               validated
	 * 
	 * @return boolean true if valid.
	 * 
	 * @reafctor Athira
	 */
	public static boolean isValidBeginningAndEndingIpv4Value(int dhcpIpv4BeginningValue, int dhcpIpv4EndingValue) {
		boolean status = false;
		status = (dhcpIpv4BeginningValue > BroadBandTestConstants.CONSTANT_1
				&& dhcpIpv4EndingValue <= BroadBandTestConstants.CONSTANT_253)
				&& (dhcpIpv4EndingValue >= dhcpIpv4BeginningValue);
		return status;
	}

	/**
	 * Verify whether the Min and Max Value set in GUI matches With WEBPA value.
	 * 
	 * @param tapEnv                 {@link AutomaticsTapApi}
	 * @param device                 {@link Dut}
	 * @param dhcpIpv4BeginningValue Dhcp Ipv4 Beginning Value set via UI.
	 * @param dhcpIpv4EndingValue    Dhcp Ipv4 Ending Value set via UI.
	 * 
	 * @refactor Athira
	 * 
	 */
	public static boolean verifyMinMaxRangeSetUsingWebpa(AutomaticsTapApi tapEnv, Dut device,
			int dhcpIpv4BeginningValue, int dhcpIpv4EndingValue) {
		boolean status = false;
		String dhcpMinRange = null;
		String dhcpMaxRange = null;
		int minRange = 0;
		int maxRange = 0;
		try {
			dhcpMinRange = tapEnv.executeWebPaCommand(device,
					BroadBandWebPaConstants.WEBPA_PARAM_TO_RETRIEVE_DHCP_STARTING_IP_ADDRESS);
			dhcpMaxRange = tapEnv.executeWebPaCommand(device,
					BroadBandWebPaConstants.WEBPA_PARAM_TO_RETRIEVE_DHCP_ENDING_IP_ADDRESS);
			LOGGER.info("DHCP MINIMUM IP RANGE CONFIGURED FOR GATEWAY : " + dhcpMinRange);
			LOGGER.info("DHCP MAXIMUM IP RANGE CONFIGURED FOR GATEWAY : " + dhcpMaxRange);
			LOGGER.info("DHCP IPV4 Beginning Value set in UI is: " + dhcpIpv4BeginningValue);
			LOGGER.info("DHCP IPV4 Ending Value set in UI is: " + dhcpIpv4EndingValue);
			if (CommonMethods.isIpv4Address(dhcpMinRange) && CommonMethods.isIpv4Address(dhcpMaxRange)) {
				minRange = Integer.parseInt(CommonMethods.patternFinder(dhcpMinRange,
						BroadBandTestConstants.PATTERN_TO_RETRIEVE_LAST_DIGIT_OF_IPv4_ADDRESS));
				maxRange = Integer.parseInt(CommonMethods.patternFinder(dhcpMaxRange,
						BroadBandTestConstants.PATTERN_TO_RETRIEVE_LAST_DIGIT_OF_IPv4_ADDRESS));
			}
			status = (minRange == dhcpIpv4BeginningValue) && (maxRange == dhcpIpv4EndingValue);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}
		return status;
	}

	/**
	 * Helper method to change partner id and to verify if it is changed
	 * 
	 * @param device
	 * @param tapEnv
	 * @param partnerId
	 * @return true if partner id changed, else false
	 */
	public static boolean changePartnerIdAndVerify(Dut device, AutomaticsTapApi tapEnv, String partnerId) {
		boolean status = false;
		String errorMessage = null;
		String partnerIdName = null;
		try {
			errorMessage = "Failed to set syndication partner id " + partnerId;
			status = BroadBandWebPaUtils.setParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_FOR_SYNDICATION_PARTNER_ID, BroadBandTestConstants.CONSTANT_0,
					partnerId);
			if (status) {
				errorMessage = "Box didn't go for the syndication factory reset after one minute of wait time";
				status = BroadBandWebPaUtils.setParameterValuesUsingWebPaOrDmcli(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_PARAM_ACTIVATE_PARTNER_ID, BroadBandTestConstants.CONSTANT_3,
						BroadBandTestConstants.TRUE);
				if (status) {
					long startTime = System.currentTimeMillis();
					status = false;
					do {
						if (!CommonUtils.executeTestCommand(tapEnv, device)) {
							LOGGER.info("STB is not accessible!. Reboot has initiated successfully");
							status = CommonMethods.waitForEstbIpAcquisition(tapEnv, device);

						}
					} while ((System.currentTimeMillis() - startTime) < AutomaticsConstants.FIVE_MINUTES && !status
							&& BroadBandCommonUtils.hasWaitForDuration(tapEnv,
									BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));

					partnerIdName = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
							BroadBandWebPaConstants.WEBPA_PARAM_FOR_SYNDICATION_PARTNER_ID).trim();
					status = status && CommonMethods.isNotNull(partnerIdName)
							&& partnerIdName.equalsIgnoreCase(partnerId);

				}
			}
		} catch (Exception e) {
			errorMessage = errorMessage + e.getMessage();
			LOGGER.error(errorMessage);
		}
		return status;
	}

	/**
	 * Utility method used to get the list of interface from bbhm_cur_cfg.xml
	 * 
	 * @param device {@link Dut}
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @return listOfInterfaces . It will return the list of interfaces
	 * 
	 * @refactor yamini.s
	 */
	public static List<String> getListOfInterfaces(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD : getListOfInterfaces()");
		List<String> listOfInterfaces = new ArrayList<String>();
		List<String> tempArray = new ArrayList<String>();
		String response = null;
		try {
			response = BroadBandCommonUtils.searchLogFiles(tapEnv, device,
					BroadBandCommandConstants.CMD_DMSB_L2NET_1_MEMBERS, BroadBandCommandConstants.FILE_TMP_PSM_DB);
			if (CommonMethods.isNull(response)) {
				response = BroadBandCommonUtils.searchLogFiles(tapEnv, device,
						BroadBandCommandConstants.CMD_DMSB_L2NET_1_MEMBERS, BroadBandCommandConstants.FILE_PSM_DB);
			}
			if (CommonMethods.isNotNull(response)) {
				try {
					tempArray = CommonMethods.patternFinderToReturnAllMatchedString(response,
							BroadBandTestConstants.PATTERN_TO_GET_INTERFACE);
					for (String singleInterface : tempArray) {
						System.out.println("interFace:" + singleInterface);
						if (singleInterface.contains(BroadBandTestConstants.SINGLE_SPACE_CHARACTER)) {
							String strArray[] = singleInterface.split(BroadBandTestConstants.SINGLE_SPACE_CHARACTER);
							for (int iteration = BroadBandTestConstants.CONSTANT_0; iteration < strArray.length; iteration++) {
								listOfInterfaces.add(strArray[iteration]);
							}
						} else {
							listOfInterfaces.add(singleInterface);
						}
					}
				} catch (Exception e) {
					LOGGER.error("Failed to get the multiple interface details:" + e.getMessage());
				}
				LOGGER.info("listOfInterfaces:" + listOfInterfaces);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred in getListOfInterfaces():" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : getListOfInterfaces()");
		return listOfInterfaces;
	}

	/**
	 * Utility method used to validate the list of log message
	 * 
	 * @param device        {@link Dut}
	 * @param tapEnv        {@link AutomaticsTapApi}
	 * @param listOfLogInfo List of log imformation
	 * @param logFilename   Log File Name
	 * @return status True- List of log information validation success,Else- False
	 * 
	 * @refactor yamini.s
	 */
	public static boolean validateListOfLogInformation(Dut device, AutomaticsTapApi tapEnv, List<String> listOfLogInfo,
			String logFilename) {
		LOGGER.debug("STARTING METHOD : validateListOfLogInformation()");
		boolean status = false;
		int resultCount = BroadBandTestConstants.CONSTANT_0;
		String response = null;
		try {
			for (String logInfo : listOfLogInfo) {
				response = searchArmOrAtomLogFile(tapEnv, device, logInfo, logFilename);
				if (CommonMethods.isNotNull(response)) {
					resultCount = resultCount + BroadBandTestConstants.CONSTANT_1;
				} else {
					LOGGER.error("Failed to verify " + logInfo + " in " + logFilename);
				}
			}
			status = listOfLogInfo.size() == resultCount;
		} catch (Exception e) {
			LOGGER.error("Exception occured in validateListOfLogInformation(): " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : validateListOfLogInformation()");
		return status;
	}

	/**
	 * Helper method to verify device rebooted status and wait for IP acquisition
	 * after reboot
	 * 
	 * @param device Dut instance
	 * @param tapEnv EcatsTapApi instance
	 * @return true, if device is accessile after reboot
	 * 
	 * @author Gnanaprakasham
	 * @refactor yamini.s
	 */
	public static boolean verifyDeviceRebootedStatusAndWaitForIpAcquisition(Dut device, AutomaticsTapApi tapEnv,
			long maximumTime) {

		boolean status = false;

		LOGGER.debug("STARTING METHOD : verifyDeviceRebootedStatusAndWaitForIpAcquisition");
		long startTime = System.currentTimeMillis();
		do {
			// Verify device is in reboot state or not
			if (CommonUtils.verifyStbRebooted(device, tapEnv)) {
				LOGGER.info("STB is not accessible!. Reboot has initiated successfully");
				LOGGER.info("Wait for 2 minutes in seconds");
				tapEnv.waitTill(BroadBandTestConstants.TWO_MINUTES);
				if (CommonMethods.waitForEstbIpAcquisition(tapEnv, device)) {
					LOGGER.info("Waiting for one Minute for the device to come up with all properties");
					tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
					status = true;
				}
			}
		} while ((System.currentTimeMillis() - startTime) < maximumTime && !status
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.TEN_SECOND_IN_MILLIS));

		LOGGER.debug("ENDING METHOD : verifyDeviceRebootedStatusAndWaitForIpAcquisition");
		return status;

	}

	/**
	 * 
	 * Method to get the partner id from build appender
	 * 
	 * @param device {@code Dut}
	 * @param tapEnv {@code AutomaticsTapApi}
	 * @return partnerId
	 * 
	 * @author ArunKumar Jayachandran
	 * @refactor yamini.s
	 */
	public static String getPartnerIdFromBuildAppender(Dut device, AutomaticsTapApi tapEnv) {

		LOGGER.debug("Starting method getPartnerIdFromBuildAppender");
		String[] appenderArray = null;
		String[] partnerArray = null;
		String partnerId = null;
		String buildAppender = null;
		buildAppender = System.getProperty(BroadBandTestConstants.BUILD_APPENDER, BroadBandTestConstants.EMPTY_STRING);
		LOGGER.info("build appender :" + buildAppender);
		if (CommonMethods.isNotNull(buildAppender)) {
			appenderArray = buildAppender.split(BroadBandTestConstants.COMMA);
			partnerArray = BroadbandPropertyFileHandler.getPartnerIDValues().split(BroadBandTestConstants.COMMA);
			for (String details : appenderArray) {
				try {
					for (String partner : partnerArray) {
						if (details.equalsIgnoreCase(partner)) {
							partnerId = partner;
							break;
						} else
							continue;
					}
					break;
				} catch (Exception exception) {
					continue;
				}
			}
		}
		if (CommonMethods.isNull(partnerId) || partnerId.equalsIgnoreCase(BroadBandTestConstants.EMPTY_STRING)) {
			partnerId = BroadbandPropertyFileHandler.getDefaultPartnerID();
		}
		LOGGER.info("Partner id from build appender: " + partnerId);
		LOGGER.debug("Ending method getPartnerIdFromBuildAppender");
		return partnerId;
	}

	/**
	 * Method to verify Ping test from Jump server .
	 * 
	 * @param device       {@link Dut}
	 * @param wanIpAddress wanIpV4Address to verify Ping
	 * @return boolean return true if ping success
	 * 
	 * @refactor Athira
	 */
	public static boolean pingTestFromJumpServer(Dut device, AutomaticsTapApi tapEnv, String wanIpAddress) {
		LOGGER.debug("STARTING METHOD : pingTestFromJumpServer()");
		String pingCommand = null;
		boolean status = false;
		try {
			pingCommand = BroadBandCommandConstants.CMD_PING_LINUX + " " + wanIpAddress;
			LOGGER.info("Ping Command: " + pingCommand);
			String response = BroadBandCommonUtils.executeCommandInJumpServer(tapEnv, pingCommand);
			if (CommonMethods.isNotNull(response)) {
				response = BroadBandCommonUtils.patternFinderMatchPositionBased(response,
						BroadBandConnectedClientTestConstants.PATTERN_MATCHER_PING_RESPONSE_LINUX,
						BroadBandTestConstants.CONSTANT_3);

				if (CommonMethods.isNotNull(response)) {
					try {
						int iActualValue = Integer.parseInt(response);
						LOGGER.info("ActualVlaue :" + iActualValue);
						status = iActualValue != BroadBandTestConstants.PERCENTAGE_VALUE_HUNDRED;
					} catch (NumberFormatException numberFormatException) {
						// Log & Suppress the Exception
						LOGGER.error(numberFormatException.getMessage());
					}

				}
			}
		} catch (Exception exception) {

			LOGGER.error("Exception occurrd whild ping wan ip address from jump server : " + exception.getMessage());
		}

		LOGGER.debug("ENDING METHOD : pingTestFromJumpServer()");
		return status;
	}

	/**
	 * Helper method to execute command in jump server
	 * 
	 * @param tapEnv  AutomaticsTapApi instance
	 * @param command command to be executed
	 * @return string response of the command executed.
	 * @author Praveenkumar Paneerselvam
	 * @refactor Rakesh C N
	 */
	public static String executeCommandInJumpServer(AutomaticsTapApi tapEnv, String command) {
		LOGGER.debug("STARTING METHOD: executeCommandInIpv6JumpServer");
		IServer iserverDetails = null;
		String response = null;
		try {

			iserverDetails = WhiteListServer.getInstance(tapEnv, "localhost");
			response = tapEnv.executeCommandUsingSshConnection(iserverDetails, command);

			LOGGER.info("Jump Server detail is - " + iserverDetails);

		} catch (Exception exception) {
			LOGGER.error("Failed to execute command in jump server " + iserverDetails + ". Error - "
					+ exception.getMessage());
		}
		LOGGER.debug("ENDING METHOD: executeCommandInIpv6JumpServer");
		return response;
	}

	/**
	 * Method to retrieve BSSID Mac address using system commands
	 * 
	 * @param device        - device instance
	 * @param systemCommand - System command to be executed
	 * @return BSSID mac address
	 * 
	 * @author gnanaprakasham.s
	 * @refactor Alan_Bivera
	 * 
	 **/
	public static String retrieveBssidMacAddressUsingSystemCommands(Dut device, AutomaticsTapApi tapEnv,
			String systemCommand) {

		String pattern = null;
		String response = null;
		String errorMessage = null;

		if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {

			pattern = BroadBandTestConstants.REGEX_TO_GET_BSSID_MAC_ADDRESS_ATOM;
			response = CommonMethods.executeCommandInAtomConsole(device, tapEnv, systemCommand);

		} else if (DeviceModeHandler.isFibreDevice(device) || DeviceModeHandler.isBusinessClassDevice(device)
				|| DeviceModeHandler.isDSLDevice(device)) {

			pattern = BroadBandTestConstants.REGEX_TO_GET_BSSID_MAC_ADDRESS_FIBRE;
			response = tapEnv.executeCommandUsingSsh(device, systemCommand);

		} else if (!CommonMethods.isAtomSyncAvailable(device, tapEnv)) {

			response = tapEnv.executeCommandUsingSsh(device, systemCommand);
			pattern = BroadBandTestConstants.REG_EXPRESSION_TO_GET_MAC_ADDRESS_SEMICOLON;

		}
		if (CommonMethods.isNotNull(response) && !response.contains(BroadBandTestConstants.COMMAND_NOT_FOUND_ERROR)) {
			LOGGER.info("Obtained BSSID Mac Address using system command " + systemCommand + " is : " + response);
			response = CommonMethods.patternFinder(response, pattern);
			if (CommonMethods.isNotNull(response)) {
				LOGGER.info("Retrieved BSSID MAC address using system command " + systemCommand + " is :" + response);
			} else {
				errorMessage = "Failed to get the proper BSSID MAC address using system command : " + systemCommand;
				LOGGER.error(errorMessage);
				throw new TestException(errorMessage);
			}
		} else {
			errorMessage = "Obtained NULL response/Command not found error message for system command execution => "
					+ systemCommand;
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}
		return response;
	}

	/**
	 * Method to verify the device models which has specified resourceParam
	 * 
	 * @param device        Dut instance
	 * @param resourceParam String value for specified parameter
	 * @return True-Given model is applicable for specified resourceParam.Else-
	 *         False
	 * 
	 * @refactor Athira
	 */
	public static boolean applicableDeviceVerification(final Dut device, String resourceParam) {
		LOGGER.debug("STARTING METHOD :applicableDeviceVerification()");
		boolean isapplicableModel = false;
		String deviceModel = device.getModel();
		LOGGER.info("Device Model:" + deviceModel);
		if (CommonMethods.isNotNull(deviceModel)) {
			String deviceModels = AutomaticsPropertyUtility.getProperty(resourceParam);
			String[] deviceModelList = deviceModels.split(",");
			for (String model : deviceModelList) {
				LOGGER.info("Model from property:" + model);
				if (model.equalsIgnoreCase(deviceModel)) {
					isapplicableModel = true;
					break;
				}
			}
		}
		LOGGER.debug("ENDING METHOD :applicableDeviceVerification()");
		return isapplicableModel;
	}

	/**
	 * This method polls for STB accessibility for defined time at an interval of 30
	 * seconds
	 * 
	 * @param tapEnv      {@link {@link AutomaticsTapApi}
	 * @param device      {@link Dut}
	 * @param pollingTime total time to poll
	 * @return true if device is accessible
	 * @refactor Athira
	 */
	public static boolean pollForSTBAccessibility(AutomaticsTapApi tapEnv, Dut device, long pollingTime) {
		boolean status = false;
		long pollingInterval = BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS;
		int noOfTimesToPoll = (int) (pollingTime / BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		for (int counter = 0; counter < noOfTimesToPoll; counter++) {
			// Waiting for 30 seconds for the for the changes to get affected
			tapEnv.waitTill(pollingInterval);

			LOGGER.info("Going to verify that the device is accesssible.");
			status = CommonMethods.isSTBAccessible(device);
			if (status) {
				break;
			}
		}

		return status;
	}

	/**
	 * Utility method to verify last reboot reason from BootTime.log or Parodus log
	 * after factory resetting the device
	 * 
	 * Sample reboot response string -- "171107-10:38:21.592209 [mod=WEBPA,
	 * lvl=INFO] [tid=3078] Received reboot_reason as:factory-reset"
	 * 
	 * @param device instance of {@link Dut}
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @return status of finding string 'Received reboot_reason as:factory-reset' in
	 *         BootTime.log or Parodus log file
	 * 
	 */

	public static boolean verifyLastRebootReasonFromBootTimeOrParodusLogFile(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD verifyLastRebootReasonFromBootTimeOrParodusLogFile");
		boolean status = false;
		status = BroadBandCommonUtils.verifyFactoryResetReasonFromBootTimeLog(device, tapEnv);
		if (status) {
			LOGGER.info(
					"SUCCESSFULLY FOUND THE LAST REBOOT REASON STRING 'Received reboot_reason as:factory-reset' IN BootTime.log");
		} else {
			LOGGER.error(
					"LAST REBOOT REASON STRING 'Received reboot_reason as:factory-reset' IN BootTime.log FILE. GOING TO CHECK THE SAME IN PARODUSlog.txt.* FILE.");
			status = BroadBandCommonUtils.verifyLastRebootReasonFromParodusLogFile(device, tapEnv);
		}
		LOGGER.debug("ENDING METHOD verifyLastRebootReasonFromBootTimeOrParodusLogFile");
		return status;
	}

	/**
	 * method to get reboot reason from PARODUSlog.txt.0 file
	 * 
	 * sample reboot response string -- "171107-10:38:21.592209 [mod=WEBPA,
	 * lvl=INFO] [tid=3078] Received reboot_reason as:factory-reset"
	 * 
	 * @param tapEnv {@link {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @return String rebootReason
	 * 
	 * @refactor Athira
	 * 
	 */
	public static boolean verifyLastRebootReasonFromParodusLogFile(Dut device, AutomaticsTapApi tapEnv) {

		LOGGER.debug("STARTING METHOD getRebootReasonFromParodusLogFile");
		String rebootReason = null;
		String response = null;
		boolean status = false;
		String errorMessage = null;

		response = searchLogFiles(tapEnv, device, BroadBandTestConstants.REBOOT_REASON_LOG_FROM_WEBPALOG,
				BroadBandTestConstants.RDKLOGS_LOGS_PARODUS_TXT_0);
		LOGGER.info("reboot response string in webpa log file is -" + response);

		if (CommonMethods.isNotNull(response)) {
			rebootReason = CommonMethods.patternFinder(response,
					BroadBandTestConstants.REGULAR_EXPRESSION_TO_GET_REBOOT_REASON_FROM_WEBPALOG_FILE).trim();
			LOGGER.info("reboot reason obtained from parodus log file is - " + rebootReason);
			if (CommonMethods.isNotNull(rebootReason)) {
				status = rebootReason.equalsIgnoreCase(BroadBandTestConstants.REBOOT_REASON_FACTORY_RESET);
				if (!status) {
					throw new TestException(rebootReason);
				}
			} else {
				errorMessage = "Obtained null response for last reboot reason from parodus log file";
				LOGGER.error(errorMessage);
				throw new TestException(errorMessage);
			}

		} else {
			errorMessage = "Unable to get the last reboot reason for factory reset from wepa.txt or parodus log file";
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}
		LOGGER.debug("ENDING METHOD getRebootReasonFromParodusLogFile");
		return status;

	}

	/**
	 * Method to get the RDKB Wifi Parameter values for device models
	 * 
	 * @param device            Dut instance
	 * @param parameterToVerify RdkBWifiParameters instance of Wifi Parameters
	 * @return ParameterValue
	 * 
	 * @refactor Athira
	 */
	public static RdkBWifiParameters getWiFiParamForDeviceModels(Dut device, RdkBWifiParameters parameterToVerify) {
		LOGGER.debug("STARTING METHOD : getWiFiParamForDeviceModels()");
		if (parameterToVerify == RdkBWifiParameters.SECURITY_MODE_2GHZ_PRIVATE_WIFI) {
			parameterToVerify = RdkBWifiParameters.SECURITY_MODE_2GHZ_PRIVATE_WIFI;

		} else if (parameterToVerify == RdkBWifiParameters.SECURITY_MODE_5GHZ_PRIVATE_WIFI) {
			parameterToVerify = RdkBWifiParameters.SECURITY_MODE_5GHZ_PRIVATE_WIFI;
		} else if (parameterToVerify == RdkBWifiParameters.OPERATING_STANDARDS_2GHZ) {

			if (BroadBandCommonUtils.applicableDeviceVerification(device,
					BroadBandTestConstants.OPERATING_STD_AX_MODELS)) {
				parameterToVerify = RdkBWifiParameters.OPERATING_STANDARDS_2GHZ_AX;
			} else if (DeviceModeHandler.isDSLDevice(device)) {
				parameterToVerify = RdkBWifiParameters.OPERATING_STANDARDS_2GHZ_DSL;
			} else {
				parameterToVerify = RdkBWifiParameters.OPERATING_STANDARDS_2GHZ;
			}
		} else if (parameterToVerify == RdkBWifiParameters.OPERATING_STANDARDS_5GHZ) {

			if (BroadBandCommonUtils.applicableDeviceVerification(device,
					BroadBandTestConstants.OPERATING_STD_AX_MODELS)) {
				parameterToVerify = RdkBWifiParameters.OPERATING_STANDARDS_5GHZ_AX;
			} else if (DeviceModeHandler.isDSLDevice(device)) {
				parameterToVerify = RdkBWifiParameters.OPERATING_STANDARDS_5GHZ_DSL;
			} else if (BroadbandPropertyFileHandler.isParticularDevice(device)) {
				parameterToVerify = RdkBWifiParameters.OPERATING_STANDARDS_5GHZ_DEVICES_A_N;
			} else {
				parameterToVerify = RdkBWifiParameters.OPERATING_STANDARDS_5GHZ;
			}
		}
		LOGGER.debug("ENDING METHOD :getWiFiParamForDeviceModels()");
		return parameterToVerify;
	}

	/**
	 * Helper method to get the connected client details
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @param String containing mac address of the connected client
	 * 
	 * @param String containing the WEB PA parameter
	 * 
	 * @return String containing value of the webPa param
	 * 
	 * @author Parvathy Premachandran Nair
	 * @refactor Rakesh C N
	 * 
	 */
	public static String getConnectedClientDetailsUsingWebpa(Dut device, AutomaticsTapApi tapEnv,
			String macAddressOfConnectedClient, String webPaParam) {

		String detail = null;
		int hostNoOfEntries = Integer.parseInt(
				tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_HOST_NUMBER_OF_ENTRIES));
		for (int index = 1; index <= hostNoOfEntries; index++) {
			String response = tapEnv.executeWebPaCommand(device,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_HOST_PHYSICAL_ADDRESS
							.replace(BroadBandTestConstants.TR181_NODE_REF, Integer.toString(index)));
			if (BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
					macAddressOfConnectedClient, response.trim())) {
				detail = tapEnv.executeWebPaCommand(device,
						webPaParam.replace(BroadBandTestConstants.TR181_NODE_REF, Integer.toString(index)));
			}
		}
		return detail;
	}

	/**
	 * Utils method to get the Connected client Info of all Offline Devices
	 * 
	 * @param driver Web driver
	 * @return list of all offline Devices
	 * 
	 * @Refactor Sruthi Santhosh
	 */
	public static List<BroadBandConnectedClientInfo> getDetailsOfInActiveDevicesFromGui(WebDriver driver) {
		String hostNameXpath = null;
		String clientInfoXpath = null;
		int iterationCount = 1;
		BroadBandConnectedClientInfo connectedClientInfo = null;
		List<BroadBandConnectedClientInfo> listOfInActiveDevices = new ArrayList<>();
		BroadBandCommonPage connectionPage = new BroadBandCommonPage(driver);

		do {
			iterationCount++;
			// Starting with 2nd Row, As 1st Row is for Heading
			hostNameXpath = BroadBandWebGuiElements.ELEMENT_XPATH_OFFLINE_DEVICES_HOST_NAME
					.replaceAll(BroadBandTestConstants.PLACE_HOLDER_FOR_ROW_NUMBER, Integer.toString(iterationCount));
			clientInfoXpath = BroadBandWebGuiElements.ELEMENT_XPATH_OFFLINE_DEVICES_INFO
					.replaceAll(BroadBandTestConstants.PLACE_HOLDER_FOR_ROW_NUMBER, Integer.toString(iterationCount));
			connectedClientInfo = new BroadBandConnectedClientInfo();
			try {
				// Exception occurs in case there is no value for the given xpath ie., Execution
				// breaks in case no further rows are available.
				connectionPage.click(By.xpath(hostNameXpath));
			} catch (Exception e) {
				LOGGER.error(
						"BREAKING EXECUTION, AS NO FURTHER ROWS ARE AVAILABLE IN THE 'OFFLINE DEVICES - PRIVATE NETWORK TABLE'.");
				break;
			}
			try {
				String hostName = connectionPage.getText(By.xpath(hostNameXpath));
				if (CommonMethods.isNotNull(hostName)) {
					connectedClientInfo.setHostName(hostName.trim());
					connectedClientInfo.setActiveStatus(false);
				}
				String clientInfo = LanSideBasePage.getText(By.xpath(clientInfoXpath)).toLowerCase();
				if (CommonMethods.isNotNull(clientInfo)) {
					String macAddress = CommonMethods.patternFinder(clientInfo,
							BroadBandTestConstants.PATTERN_TO_GET_MAC_ADDRESS);
					if (CommonMethods.isNotNull(macAddress)) {
						macAddress = macAddress
								.replace(BroadBandTestConstants.COLON, BroadBandTestConstants.EMPTY_STRING)
								.toUpperCase();
						connectedClientInfo.setMacAddress(macAddress);
					}
				}
				LOGGER.info("DETAILS OF CONNECTED CLIENT IN ROW " + (iterationCount - 1) + ":"
						+ connectedClientInfo.toStringOfflineClients());
				listOfInActiveDevices.add(connectedClientInfo);

			} catch (Exception ex) {
				LOGGER.error(
						"Exception occured while retrieving Connected clients details from GUI : " + ex.getMessage());
			}
		} while (iterationCount <= BroadBandTestConstants.CONSTANT_10);

		return listOfInActiveDevices;
	}

	/**
	 * Method to parse json data to map
	 * 
	 * @param jsonInput String jsoninput
	 * @return Map of values
	 * 
	 */
	public static Map<String, String> parseJsonDataToMap(String jsonInput) {
		LOGGER.debug("STARTING METHOD parseJsonDataToMap");
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = mapper.readValue(jsonInput, Map.class);
		} catch (IOException e) {
			LOGGER.error("Unable to parse json input");
		}
		LOGGER.debug("ENDING METHOD parseJsonDataToMap");
		return map;
	}

	/**
	 * Method to parse rfc json data to map
	 * 
	 * @param jsonInput String jsoninput
	 * @return Map of values
	 */
	public static Map<String, BroadbandRfcDataBaseValueObject> parseRfcDataBaseResponse(String jsonInput) {
		LOGGER.debug("STARTING METHOD parseRfcDataBaseResponse");
		Map<String, BroadbandRfcDataBaseValueObject> responseMap = new HashMap<String, BroadbandRfcDataBaseValueObject>();
		BroadbandRfcDataBaseValueObject broadbandRfcDataBaseValueObject;
		ObjectMapper mapper = new ObjectMapper();

		String response = null;
		try {
			JSONObject inputObject = new JSONObject(jsonInput);
			Iterator keys = inputObject.keys();
			while (keys.hasNext()) {
				response = (String) keys.next();
				LOGGER.info("Value retrieve for Key:" + response + "; Values:"
						+ inputObject.getJSONObject(response).toString());
				broadbandRfcDataBaseValueObject = mapper.readValue(inputObject.getJSONObject(response).toString(),
						BroadbandRfcDataBaseValueObject.class);
				responseMap.put(response, broadbandRfcDataBaseValueObject);
			}

		} catch (IOException | JSONException e) {
			LOGGER.error("Unable to parse json input values are missing " + e.getMessage());
			responseMap = null;
		}
		LOGGER.debug("ENDING METHOD parseRfcDataBaseResponse");
		return responseMap;
	}

	/**
	 * Method to get values from partner_default json file and check default values
	 * for forword and output mark of syndication
	 * 
	 * @param device Dut instance
	 * @param tapEnv AutomaticsTapApi instance
	 * @return boolean
	 * 
	 * @author prasanthreddy.a
	 * @refactor Athira
	 */
	public static boolean verifySyndicateFlowControlValuesInDevice(Dut device, AutomaticsTapApi tapEnv) {
		boolean status = false;
		String response = null;
		LOGGER.debug("STARTING METHOD: verifySyndicateFlowControlValuesInDevice()");
		try {
			response = tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_CAT_PARTNERS_DEFAULT_JSON);
			// List of Partner specific
			List<String> partnerList = Arrays
					.asList(AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PARTNER_ID));

			if (CommonMethods.isNotNull(response)) {
				JSONObject partnerSpecific = new JSONObject(response);
				// Keys here are partner values as above list
				Iterator<String> partnerIterator = partnerSpecific.keys();
				JSONObject paramsOnPartner;
				Map<String, Boolean> partner = new HashMap<>();
				// Iterating to check partner available or not
				while (partnerIterator.hasNext()) {
					String partnerFromJson = partnerIterator.next().toString();
					if (partnerList.contains(partnerFromJson)) {
						LOGGER.info("Valid Partner Retrieved: " + partnerFromJson);
						partner.put(partnerFromJson.trim(), true);
					}
				}
				if (!partner.isEmpty()) {
					for (Map.Entry<String, Boolean> parterEntry : partner.entrySet()) {
						LOGGER.info("Partner ID validating: " + parterEntry);
						paramsOnPartner = (JSONObject) partnerSpecific.get(parterEntry.getKey().toString());
						// Getting default values
						LOGGER.info("paramsOnPartner : " + paramsOnPartner);
						Map<String, String> paramValuesToValidate = BroadBandWebPaConstants.DSCP_VALUES;
						LOGGER.info("paramValuesToValidate: " + paramValuesToValidate);
						for (Map.Entry<String, String> entry : paramValuesToValidate.entrySet()) {
							String param1 = entry.getKey();
							LOGGER.info("param1: " + param1);
						}
						// Validating default values with response
						for (Map.Entry<String, String> entry : paramValuesToValidate.entrySet()) {
							String param = entry.getKey();
							String expected = entry.getValue();
							String retrived = paramsOnPartner.get(entry.getKey()).toString();
							LOGGER.info("Param :" + param + ": Expected Value :" + expected + " Retrieved Value :"
									+ retrived);
							if (!retrived.equals(expected))
								partner.put(parterEntry.getKey(), false);
						}
					}
					status = !partner.containsValue(BroadBandTestConstants.BOOLEAN_VALUE_FALSE);
				} else {
					LOGGER.error("Unable to retrieve partner specific details from device : Response :" + response);
				}
			} else {
				LOGGER.error("Unable to retrieve data from device : Response :" + response);
			}
		} catch (Exception e) {
			LOGGER.error("Exception caught while validating syndication flow control values " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD:verifySyndicateFlowControlValuesInDevice()");
		return status;
	}

	/**
	 * Util method to enable/disable Interface device wifi reporting service
	 * (Harvester reporting service) along with polling and reporting parameter.
	 * 
	 * @param device                    Dut object
	 * @param reportingServiceParameter Reporting service which to be enabled and
	 *                                  polling period and reporting period to be
	 *                                  set.
	 * @param enable                    true/false,
	 * @param pollingPeriod             polling period can be null or any positive
	 *                                  number value
	 * @param reportingPeriod           reporting period should be null if polling
	 *                                  period is null and reporting period will be
	 *                                  set only if polling period value is given
	 *                                  and polling period value is set successfully
	 * @return true if all the given parameters are set successfully
	 * @author Praveen Kumar P
	 * @refactor Govardhan
	 */
	public static boolean enableDisableReportingService(Dut device,
			ReportingServiceParameterEnum reportingServiceParameter, String enable, String pollingPeriod,
			String reportingPeriod) {
		LOGGER.debug("Inside method enableDisableReportingService");
		boolean status = false;
		if (CommonMethods.isNotNull(pollingPeriod) && CommonMethods.isNotNull(reportingPeriod)) {
			// Set Polling period to defined uint for report using webPA
			status = BroadBandWiFiUtils.setWebPaParams(device, reportingServiceParameter.getWebpaPollingPeriod(),
					pollingPeriod, BroadBandTestConstants.CONSTANT_2);
			if (status) {
				// Set Reporting period to defined uint for report using webPA
				status = BroadBandWiFiUtils.setWebPaParams(device, reportingServiceParameter.getWebpaReportingPeriod(),
						reportingPeriod, BroadBandTestConstants.CONSTANT_2);
			}
		} else if (CommonMethods.isNotNull(pollingPeriod)) {
			// Set Polling period to defined uint for report using webPA
			status = BroadBandWiFiUtils.setWebPaParams(device, reportingServiceParameter.getWebpaPollingPeriod(),
					pollingPeriod, BroadBandTestConstants.CONSTANT_2);
		} else if (!CommonMethods.isNotNull(reportingPeriod)) {
			// If polling period is null. Reporting period cannot be set.
			status = true;
		}

		if (status) {
			// Enabling a report service using webPA
			status = BroadBandWiFiUtils.setWebPaParams(device, reportingServiceParameter.getWebpaEnable(), enable,
					BroadBandTestConstants.CONSTANT_3);
		}
		LOGGER.info("Status of enable harvester report using WEBPA command is - " + status
				+ ". WebPA parameter used is " + reportingServiceParameter);
		LOGGER.debug("Exit from method enableDisableReportingService");
		return status;
	}

	/**
	 * Method to retrieve property form /etc/device.properties
	 * 
	 * @param device instance of {@link Dut}
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @return property value from /etc/device.properties
	 * @refactor Govardhan
	 */
	public static String getPropertyFromDeviceProperties(Dut device, AutomaticsTapApi tapEnv, String propertyKey,
			String patternToMatchTheValue) {

		LOGGER.debug("STARTING METHOD: getPropertyFromDeviceProperties()");
		// hold the property value
		String value = null;
		// loop count
		int maxLoopCount = 3;
		for (int index = 0; index < maxLoopCount; index++) {
			String response = tapEnv.executeCommandUsingSsh(device, "cat /etc/device.properties | grep " + propertyKey);
			if (CommonMethods.isNotNull(response)) {
				value = CommonMethods.patternFinder(response, patternToMatchTheValue);
			}
			if (CommonMethods.isNotNull(value)) {
				break;
			}
		}
		LOGGER.debug("ENDING METHOD: getPropertyFromDeviceProperties()");
		return value;
	}

	/**
	 * Helper method to get partner value for web UI validation
	 * 
	 * @param device {@link Dut}
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @return the true if Atom Sync uptime is greater than 20 mis
	 */
	public static boolean getAtomsyncUptimeStatus(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("Starting of Method: getAtomsyncUptimeStatus");
		String response = null;
		int uptimeInMin = 0;
		boolean status = false;
		long startTime = System.currentTimeMillis();
		String errorMessage = null;
		try {

			do {
				errorMessage = "Webpa process is not up";
				response = tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_UPTIME);
				LOGGER.info("Device uptime " + response);
				status = CommonMethods.isNotNull(response);
				if (status) {
					errorMessage = "Device uptime is not greater than 20 mins";
					status = false;
					uptimeInMin = Integer.parseInt(response) / BroadBandTestConstants.CONSTANT_60;
					if (!(uptimeInMin >= BroadBandTestConstants.CONSTANT_20)) {
						BroadBandCommonUtils.hasWaitForDuration(tapEnv,
								(BroadBandTestConstants.CONSTANT_20 - uptimeInMin) * 60000);

						uptimeInMin = Integer.parseInt(
								tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_UPTIME))
								/ BroadBandTestConstants.CONSTANT_60;
						if (uptimeInMin >= BroadBandTestConstants.CONSTANT_20) {
							status = true;
							LOGGER.info("Device uptime is greater than 20 mins " + status);
						}
					} else {
						status = true;
						LOGGER.info("Device uptime is greater than 20 mins " + status);
					}

				}
			} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.EIGHT_MINUTE_IN_MILLIS
					&& !status);
			if (!status) {
				throw new TestException(
						"Exception occured : Webpa process is not up/ Device uptime is not greater than 20 mins "
								+ errorMessage);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while retrieving partner value " + e.getMessage());
		}
		LOGGER.debug("Ending of Method: getAtomsyncUptimeStatus");
		return status;
	}
}

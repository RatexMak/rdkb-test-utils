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
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Device;
import com.automatics.device.Dut;
import com.automatics.exceptions.TestException;
import com.automatics.providers.connection.SshConnection;
import com.automatics.rdkb.BroadBandDeviceStatus;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
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
import com.automatics.rdkb.server.WhiteListServer;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpMib;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpUtils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.wifi.BroadBandWiFiUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.CommonMethods;
import com.automatics.webpa.WebPaParameter;
import com.automatics.webpa.WebPaServerResponse;

/**
 * Utility class which handles the RDK B common functionality and verification.
 * 
 * @author Selvaraj Mariyappan
 * @refactor Govardhan
 * 
 */
public class BroadBandCommonUtils {

    /**
     * Logger instance for {@link BroadBandCommonUtils}
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(BroadBandCommonUtils.class);

    /**
     * Method to build command using string buffer.
     * 
     * @param values
     *            , N number of arguments
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
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * 
     * @param waitDuration
     *            long integer specifying the time in milliseconds to wait for
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
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param webPaParameter
     *            Object representing WebPaParameter
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
	boolean result = response.getStatusCode() == RDKBTestConstants.CONSTANT_200;
	LOGGER.info("WEBPA PARAM - " + webPaParameter.getName() + " SET WITH VALUE - " + webPaParameter.getValue()
		+ " IS SUCCESSFUL: " + result);
	LOGGER.debug("ENDING METHOD setWebPaParam");
	return result;
    }

    /**
     * Utility to method to search for the given text in the given log file based on Poll Interval & Poll Duration;
     * returns String representing the search response.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param searchText
     *            String representing the Search Text. It needs to be passed with the required escape character.
     * @param logFile
     *            String representing the log file.
     * @param pollDuration
     *            Long representing the duration for which polling needs to be performed.
     * @param pollInterval
     *            Long representing the polling interval.
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
     * @param device
     *            Dut Box
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param logMessage
     *            log message to search
     * @param logFile
     *            log file to search
     * @param maxDuration
     *            max duration to poll
     * @param pollingInterval
     *            interval time for each time within max duration
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
     * @param device
     *            , Dut box instance
     * @param tapEnv
     *            , AutomaticsTapApi instance
     * @param parameter
     *            , WebPA parameter
     * @param valueToBeVerified
     *            , value to be verified from webpa get request
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
     * Utility to method to search for the given text in the given log file; returns String representing the search
     * response.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param searchText
     *            String representing the Search Text. It needs to be passed with the required escape character.
     * @param logFile
     *            String representing the log file.
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
     * Utility method to obtain the string for the specific pattern based on the match position; this method to be
     * invoked when the expected number of matches is more than one.
     * 
     * @param response
     *            String representing the text on which pattern matcher to be performed.
     * @param patternToMatch
     *            String representing the pattern to be searched.
     * @return String representing the result of the pattern matcher based on match position.
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
     * Utility Method to retrieve the current Date and time from the device from ATOM or Arm Console
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
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
     * Utility method to perform Factory Reset on the device using WebPA & then wait for the device to comeup.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
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
		LOGGER.info("GOING TO WAIT FOR 1 MINUTE BEFORE EXECUTING TEST COMMAND.");
		tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
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
     * @param Dut
     *            {@link device}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param pidRapid
     *            Rapid process pid
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
     * Utility method to perform Factory Reset on the device using WebPA & then wait for the device to comeup.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
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
     * @param commandOutput
     *            Output of the command
     * @param stringVerifications
     *            List of Strings to be verified
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
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link Dut}
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
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * 
     * @param searchText
     *            string containing text to be searched in log file
     * @param logFile
     *            string containing path of the file to be searched
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
     * @param response
     *            the target string on which the pattern has to be searched
     * @param patternToMatch
     *            pattern that has to be matched
     * @param patternMatcherGroup
     *            integer representing the pattern Matcher Group
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
     * @param tapEnv
     *            Instance of {@link AutomaticsTapApi}
     * @param device
     *            Instance of {@link device}
     * @param url
     *            URL for which the ping connection needs to be checked
     * 
     * @return returns status of operation
     */
    public static BroadBandResultObject verifyPingConnectionFromJumpServer(Dut device, AutomaticsTapApi tapEnv,
	    String url) {
	LOGGER.debug("STARTING METHOD : verifyPingConnectionFromJumpServer()");

	String errorMessage = null;
	String pingResponse = null;
	boolean isReachable = false;
	BroadBandResultObject result = new BroadBandResultObject();

	String pingCommand = CommonMethods.isIpv6Address(url) ? BroadBandCommandConstants.CMD_PING_LINUX_IPV6
		: BroadBandCommandConstants.CMD_PING_LINUX;
	pingCommand = BroadBandCommonUtils.concatStringUsingStringBuffer(pingCommand, url);

	pingResponse = tapEnv.executeCommandUsingSshConnection(
		WhiteListServer.getInstance(tapEnv,
			AutomaticsPropertyUtility.getProperty(RDKBTestConstants.PROPERTY_REVERSE_SSH_JUMP_SERVER)),
		pingCommand);

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
	LOGGER.debug("ENDING METHOD : verifyPingConnectionFromJumpServer()");
	return result;
    }

    /**
     * Utility method to verify a file is present on the device. It uses the command: find / -iname <fileName>
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param fileName
     *            String representing the Library Name
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
     * Method to check whether the STB is accessible or inaccessible as per the boolean variable "expectedResult" passed
     * for a max polling time and polling interval as passed. This is cross verified by sending an echo test_connection
     * command and see if we get the response or not.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param pollingInterval
     *            The Interval In which the validation has to be done
     * @param maxPollingTime
     *            The Max Duration In which the validation has to be done
     * @param expectedResult
     *            true if the device has to be in "Accessible" state or false if the device has to be in "Inaccessible"
     * @return result
     * 
     * @author BALAJI V
     * @Refactor Athira
     */
    public static boolean isRdkbDeviceAccessible(AutomaticsTapApi tapEnv, Dut device, long pollingInterval,
	    long maxPollingTime, boolean expectedResult) {
	LOGGER.debug("ENTERING METHOD: isRdkbDeviceAccessible");
	// Boolean Variable to store the result
	boolean result = false;
	// Start time
	long startTime = System.currentTimeMillis();
	do {
	    String response = tapEnv.executeCommandUsingSsh(device,
		    BroadBandTestConstants.ECHO_WITH_SPACE + BroadBandTestConstants.CONNECTION_TEST_MESSAGE);
	    result = expectedResult
		    ? (CommonMethods.isNotNull(response) && CommonUtils.patternSearchFromTargetString(response,
			    BroadBandTestConstants.CONNECTION_TEST_MESSAGE))
		    : (CommonMethods.isNull(response)
			    || response.indexOf(BroadBandTestConstants.CONNECTION_TEST_MESSAGE) == -1);
	} while (((System.currentTimeMillis() - startTime) < maxPollingTime) && !result
		&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, pollingInterval));
	LOGGER.debug("ENDING METHOD: isRdkbDeviceAccessible");
	return result;
    }
    
    /**
     * This method will wait the boot up time of the device is as given in the parameter
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
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
     * Utility Method to validate the log message searched & found is the recent one i.e. after performing the
     * appropriate action. This method utilizes the date captured on the device before performing the action and the log
     * message searched after performing the action. Both the timestamps are compared & validated.
     * 
     * @param dateResponse
     *            String representing the DateTime captured before performing the action.
     * @param searchResponse
     *            String representing the search response after performing the action.
     * @return Boolean representing the result of validation of log message using TimeStamp.
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
     * @param tapEnv
     *            Instance of {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param logFile
     *            log file location
     * @param pollDuration
     *            maximum poll duration
     * @param pollInterval
     *            polling interval
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
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param timeToCompare
     *            time stamp from the first cycle of log
     * @return boolean return true if second cycle log has occurred in 15 mins of time
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
     * Utility method to validate the all processes defined in properties with given response and property key.
     * 
     * @param device
     *            The Dut to be validated.
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance.
     * @param response
     *            The given command response.
     * @param processListKey
     *            The process list property key.
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
     * Utility method to verify all required Broadband processes. The required processes details are retrieved from
     * 'common.properties' property file
     * 
     * @param device
     *            The Dut to be validated
     * @param tapEnv
     *            The {@link AutomaticsTapApi } instance.
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
     * Utility method to remove all different signed extensions in requested build name.
     * 
     * @param buildImageName
     *            The build name to be formatted
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
     * @param settop
     *            The settop to be used.
     * @return True if device is loaded with prod build.
     * @author Govardhan
     */
    public static boolean isProdBuildOnDevice(Dut device) {
	return device.getFirmwareVersion().toLowerCase().contains("prod");
    }

    /**
     * Utility method to Convert string to Double Value.
     * 
     * @param stringToConvert
     *            String which needs to be converted to Double value.
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
     * @return isPatterFoundInText return True if targetString Matches with the patter to search string else returns
     *         False.
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
     * @param response
     *            response to validate
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
     * Utility method to Cross verify the Configparemgen Version based on Current Build in the device
     * 
     * @param buildName
     *            Current Buildname of the device.
     * @param configparamgenVersion
     *            Configparamgen Version obtained from the gateway.
     * @return true if configparamgen version matches the version
     * @author Govardhan
     */
    public static boolean verifyConfigparamgenForGivenBuild(String buildName, String configparamgenVersion) {
	LOGGER.debug("STARTING METHOD : verifyConfigparamgenForGivenBuild()");
	boolean status = false;
	try {
	    double coreVersion = BroadBandTestConstants.CHANNEL_ATTENUATION_5_GHZ;
	    int pVersion = BroadBandTestConstants.CONSTANT_0;
	    int sVersion = BroadBandTestConstants.CONSTANT_0;
	    double configparamgen = convertStringToDouble(configparamgenVersion);
	    // condition covers sprint, stable, feature builds.
	    if (BroadBandCommonUtils.patternSearchFromTargetString(buildName,
		    BroadBandTestConstants.STRING_SPRINT_IN_BUILD_NAME)
		    || BroadBandCommonUtils.patternSearchFromTargetString(buildName,
			    BroadBandTestConstants.FIRMWARE_VERSION_BRANCH_STABLE)
		    || BroadBandCommonUtils.patternSearchFromTargetString(buildName,
			    BroadBandTestConstants.STRING_FEATURE_IN_BUILD_NAME)) {
		LOGGER.info("Current build in device is either Sprint/Stable/Feature :" + buildName);
		LOGGER.info("Expected Configparamgen Version : >=3.7");
		LOGGER.info("Actual Configparamgen Version : " + configparamgen);
		status = configparamgen >= BroadBandTestConstants.CONFIGPARAMGEN_VERSION_3_7;
	    } else {
		coreVersion = convertStringToDouble(CommonMethods.patternFinder(buildName,
			BroadBandTestConstants.CORE_VERSION_FROM_REGULAR_RELEASE_BUILD_PATTERN_MATCHER));
		// for regular/Proper Release builds
		if (CommonMethods.patternMatcher(buildName,
			BroadBandTestConstants.REGULAR_RELEASE_BUILD_PATTERN_MATCHER)
			|| CommonMethods.patternMatcher(buildName,
				BroadBandTestConstants.PROPER_RELEASE_BUILD_PATTERN_MATCHER)) {
		    LOGGER.info("Current build in device is a proper/Regular Release Build :" + buildName);
		    pVersion = convertStringToInteger(CommonMethods.patternFinder(buildName,
			    BroadBandTestConstants.P_VERSION_FROM_REGULAR_RELEASE_BUILD_PATTERN_MATCHER));
		    sVersion = convertStringToInteger(CommonMethods.patternFinder(buildName,
			    BroadBandTestConstants.S_VERSION_FROM_REGULAR_RELEASE_BUILD_PATTERN_MATCHER));
		}
		// for Spin Builds where patter differs from regular release builds
		else if (CommonMethods.patternMatcher(buildName, BroadBandTestConstants.SPIN_BUILD_PATTERN_MATCHER)
			|| CommonMethods.patternMatcher(buildName,
				BroadBandTestConstants.PROPER_SPIN_BUILD_PATTERN_MATCHER)) {
		    LOGGER.info("Current build in device is a spin Build :" + buildName);
		    sVersion = convertStringToInteger(CommonMethods.patternFinder(buildName,
			    BroadBandTestConstants.S_VERSION_FROM_REGULAR_RELEASE_BUILD_PATTERN_MATCHER));
		}

		// Build name is split into core, p, s version in order to match the expected
		// logic
		LOGGER.info("Core Version in build is : " + coreVersion);
		LOGGER.info("P Version in build is : " + pVersion);
		LOGGER.info("s Version in build is : " + sVersion);
		// condition covers 'Release <4.4' or 'Release between 4.4p1s10 to 4.4p2s2'.
		if (coreVersion < BroadBandTestConstants.FIRMWARE_VERSION_4_4
			|| (coreVersion == BroadBandTestConstants.FIRMWARE_VERSION_4_4
				&& ((pVersion == BroadBandTestConstants.CONSTANT_1
					&& sVersion == BroadBandTestConstants.CONSTANT_10)
					|| (pVersion == BroadBandTestConstants.CONSTANT_1
						&& sVersion == BroadBandTestConstants.CONSTANT_11)
					|| (pVersion == BroadBandTestConstants.CONSTANT_2
						&& sVersion == BroadBandTestConstants.CONSTANT_1)
					|| (pVersion == BroadBandTestConstants.CONSTANT_2
						&& sVersion == BroadBandTestConstants.CONSTANT_2)))) {
		    LOGGER.info(
			    "Given build Version is either : 'Release <4.4' or 'Release between 4.4p1s10 to 4.4p2s2'");
		    LOGGER.info("Expected Configparamgen Version : 2.17");
		    LOGGER.info("Actual Configparamgen Version : " + configparamgen);
		    status = configparamgen == BroadBandTestConstants.CONFIGPARAMGEN_VERSION_2_17;
		}
		// condition covers '4.4 initial releases until 4.4p1s9' or 'Release 4.4p2s3 to
		// latest release
		// build'.
		else if (coreVersion >= BroadBandTestConstants.FIRMWARE_VERSION_4_4) {
		    LOGGER.info(
			    "Given build Version is either : '4.4 initial releases until 4.4p1s9' or 'Release 4.4p2s3 to latest release build'");
		    LOGGER.info("Expected Configparamgen Version : >=3.7");
		    LOGGER.info("Actual Configparamgen Version : " + configparamgen);
		    status = configparamgen >= BroadBandTestConstants.CONFIGPARAMGEN_VERSION_3_7;
		}
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception occured while cross verifying the configparamgen values :" + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD: verifyConfigparamgenForGivenBuild()");
	return status;
    }

    /**
     * Helper method is to format MAC address to remove leading zeros.
     * 
     * @param macAddress
     *            The given MAC address.
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
     * @param tapApi
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param lanMode
     *            mode to set
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
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param settop
     *            Settop instance
     * @return index to get wanmac
     * 
     * 
     */
    public static String getIndexForWanMac(AutomaticsTapApi tapEnv, Dut device) {
	LOGGER.debug("STARTING METHOD:getIndexForWanMac");
	String index = null;
	String response = null;
	String errorMessage = null;
	index = BroadBandTestConstants.STRING_VALUE_ONE;
	LOGGER.info("Index to be used to get device mac is-" + index);
	LOGGER.debug("ENDING METHOD:getIndexForWanMac");
	return index;
    }

    /**
     * Utility method to set device in router mode using WebPA command.
     * 
     * @param tapApi
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @return true if setting lan mode is success else false
     */
    public static boolean setDeviceInBridgeStaticModeStatusUsingWebPaCommand(AutomaticsTapApi tapApi, Dut device) {
	return setAndVerifyLanModeUsingWebPaCommands(tapApi, device, BroadBandTestConstants.CONSTANT_BRIDGE_STATIC);
    }

    /**
     * Utility method to set device in router mode using WebPA command.
     * 
     * @param tapApi
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @return true if setting lan mode is success else false
     */
    public static boolean setDeviceInRouterModeStatusUsingWebPaCommand(AutomaticsTapApi tapApi, Dut device) {
	return setAndVerifyLanModeUsingWebPaCommands(tapApi, device, BroadBandTestConstants.LAN_MANAGEMENT_MODE_ROUTER);
    }

    /**
     * Uses the partial key by appending it with the settop platform to get the property value from cats.props.
     *
     * @param device
     *            The Dut instance.
     * @param partialPropsKey
     *            Partial property key which will be appended with the settop platform to form the complete
     *            key.(Example:- "cdl.unsigned.image.name." it get resolved to "cdl.unsigned.image.name.pace_x1")
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
     * @param snmpCommandOutput
     *            Response of snmp command
     * @param uptimeCmdResponse
     *            Response of uptime command
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
     * @param settop
     *            The device to be validated
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
     * Helper Method to Post JSON Data to the passed URL using TLS Enabled HTTP Client.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param requestUrl
     *            String representing the Request URL
     * @param postDataJson
     *            String the data to be posted in JSON Format.
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
     * Utility Method to perform the device reboot operation as a part of pre-condition.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
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
     * Utility Method to perform the check on whether file exists. The file check operation is being performed by
     * polling for the existence of file for 10 minutes.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param completeFilePath
     *            String representing the complete file path.
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
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
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
     * Utility method to perform validation as operations like NULL, NOT NULL, COMPARISON, ZERO, NON ZERO, RANGE etc.
     * 
     * @param operation
     *            String representing the operation. The possible values to be passed can be taken from the Switch Case
     *            block. Possible values are: TXT_NULL, TXT_NOT_NULL, TXT_COMPARISON, INT_ZERO, INT_NON_ZERO,
     *            INT_COMPARISON, INT_RANGE.
     * @param expectedValue
     *            String representing the Expected Value. In case of integer, it will be parsed from String.If not
     *            applicable, can be passed as NULL.
     * @param actualValue
     *            String representing the Actual Value. In case of integer, it will be parsed from String.
     * 
     * @return Boolean representing the result of validation; TRUE in case the comparison is successful; FALSE in case
     *         the comparison fails.
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
     * Utility method to get the CMTS MAC Address from Arp table corresponding to Erouter interface.
     * 
     * @param device
     *            Dut instance
     * @param tapEnv
     *            AutomaticsTapApi instance
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
     * Utility method to calculate Broadband device maintenance window(start time and end time) based on current device
     * local time with wait time.
     * 
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance.
     * @param device
     *            The device to be validated.
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
		     * Maintenance window is calculated and configured as 0 - 86400( 24 hour format) and reset again to
		     * 0 at midnight. If current time is between 12 AM(00:00:00) and 1AM (01:00:00). Maintenance window
		     * start time is current time and end time will be start time + 1200 seconds(20 minutes)
		     */
		    if (currentTimeInSeconds > 0 && currentTimeInSeconds <= 3600) {
			startTime = currentTimeInSeconds + (waitTime * 60);
			endTime = startTime + (20 * 60);
			/*
			 * If maintenance window time is between 84600(23:30:00) and 86399(23:59:59). Calculate the
			 * upgrade end time from 0 instead of adding 3600 to current time which exceed the 86399.
			 */
		    } else if (currentTimeInSeconds >= 84600 && currentTimeInSeconds <= 86399) {
			startTime = (currentTimeInSeconds + (waitTime * 60)) < 86399
				? currentTimeInSeconds + (waitTime * 60)
				: (currentTimeInSeconds + (waitTime * 60)) - 86399;
			endTime = (startTime + (20 * 60)) < 86399 ? (startTime + (20 * 60))
				: (startTime + (20 * 60)) - 86399;
			/*
			 * Other cases, calculate maintenance window time like -> start time = current time + (waitTime
			 * * 60) seconds end time = start time + 1200 seconds(20 Minutes)
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
     * @param device
     *            {@link Dut}
     * @param startAFter
     *            Maintenance window start after given minutes
     * @param interval
     *            Maintenance window reboot interval
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
     * @param seconds
     *            seconds to convert
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
     * @param device
     *            {@link Dut}
     * @param startTime
     *            Maintenance window start time
     * @param endTime
     *            Maintenance window end time
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * 
     * @return true if successfully set
     */
    private static boolean setMaintenanceWindowDataInProxyXconf(Dut device, String startTime, String endTime,
	    AutomaticsTapApi tapEnv) {
	LOGGER.debug("STARTING METHOD: setMaintenanceWindowDataInProxyXconf");
	boolean result = false;
	String rfcPayLoadData = null;
	String stbMacAddress = device.getHostMacAddress();
	LOGGER.info("XB Mac address: " + stbMacAddress);
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
		LOGGER.error("XB Mac address is not obtained");
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
     * Set the Maintenance window start and end time in Proxy xconf and configure the device to point to proxy server
     * 
     * @param device
     *            {@link Dut}
     * @param startTime
     *            Maintenance window start time
     * @param endTime
     *            Maintenance window end time
     * @param tapEnv
     *            {@link AutomaticsTapApi}
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
     * @param settop
     *            {@link Settop}
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
     * @param device
     *            instance of {@link Dut}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
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
     * @param settop
     *            instance of {@link Settop}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param waitTime
     *            Maintenance window start after given minutes
     * @param maintenanceWindowInterval
     *            Maintenance window reboot interval
     * @return status-True- Device rebooted successfully during maintenance window,Else-False
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
     * @param tapEnv
     *            The AutomaticsTapApi.
     * @param device
     *            The {@link Dut} object.
     * @param completeFilePath
     *            The Complete file path
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
     * Method to check whether the STB is accessible. This is cross verified by sending an echo test_connection command
     * and see if we get the response.
     * 
     * @param tapApi
     *            instance of {@link AutomaticsTapApi}
     * @param settop
     *            instance of {@link Settop}
     * @param delay
     *            delay in between the retry
     * @param maxLoopCount
     *            maximum loop count
     * @return true if STB is accessible else false
     */
    public static boolean isSTBAccessible(AutomaticsTapApi tapApi, Dut device, long delay, int maxLoopCount) {
	// STB accessible status
	boolean status = false;
	// Added this continues check as per the discussion with selva
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
     * Utility method to validate the currently running firmware version using WebPA command.
     * 
     * @param settop
     *            The settop to be validated.
     * @param buildImage
     *            The firmware version to be validated.
     * @return True if given image name matches with WebPA response.
     * @author Govardhan
     */
    public static boolean verifyCurrentlyRunningImageVersionUsingWebPaCommand(AutomaticsTapApi tapEnv, Dut device,
	    String buildImage) {
	return verifyCurrentlyRunningImageVersionUsingWebPaCommand(tapEnv, device, buildImage, false);
    }

    /**
     * Utility method to validate the currently running firmware version using WebPA command.
     * 
     * @param device
     *            The Dut to be validated.
     * @param buildImage
     *            The firmware version to be validated.
     * @param convertToUpperCase
     *            true if both needs to converted to upper case and verified
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
     * Method to perform reboot and verify the device is accessible without verifying the processes are up
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
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
     * @param device
     *            {@link Dut}
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
     * @param device
     *            {@link Dut}
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
     * @param logMessage
     *            string containing log message to obtain UTC time from
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
     * @param device
     *            {@link Dut}
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
     * Utility method to retrieve the device status {@link BroadBandDeviceStatus } via WebPA or dmcli.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
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

		deviceStatus.setXfinityWiFi2GhzOpenStatus(status);
		break;

	    case "Device.WiFi.SSID.10103.Status":
		if (CommonMethods.isNotNull(paramValue)) {
		    status = paramValue.equalsIgnoreCase("Up");
		}

		deviceStatus.setXfinityWiFi5GhzOpenStatus(status);

		break;

	    case "Device.WiFi.SSID.10105.Status":
		if (CommonMethods.isNotNull(paramValue)) {
		    status = paramValue.equalsIgnoreCase("Up");
		}

		deviceStatus.setXfinityWiFi5GhzSecureStatus(status);

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
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
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
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param testCaseId
     *            test case ID
     * @param stepNumber
     *            step number
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
     * Utility method to set the Device mode to 'router' using WebPA or dmcli and wait for 5 minutes to come up with
     * router mode.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
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
     * Utility method to calculate Broadband device firmware upgrade maintenance window(start time and end time) based
     * on current device local time.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
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
		     * Maintenance window is calculated and configured as 0 - 86400( 24 hour format) and reset again to
		     * 0 at midnight. If current time is between 12 AM(00:00:00) and 1AM (01:00:00). Maintenance window
		     * start time is current time and end time will be current time + 3600 seconds
		     */
		    if (currentTimeInSeconds > 0 && currentTimeInSeconds <= 3600) {
			startTime = currentTimeInSeconds;
			endTime = currentTimeInSeconds + 60 * 60;
			/*
			 * If maintenance window time is between 84600(23:30:00) and 86399(23:59:59). Calculate the
			 * upgrade end time from 0 instead of adding 3600 to current time which exceed the 86399.
			 */
		    } else if (currentTimeInSeconds >= 84600 && currentTimeInSeconds <= 86399) {
			startTime = currentTimeInSeconds - 120;
			endTime = 60 * 60;
			/*
			 * Other cases, calculate maintenance window time like -> start time = current time - 120
			 * seconds end time = current time + 3600 seconds.
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
     * @param match
     *            list to match
     * @param limitValue
     *            limit value
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
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param tr181Param
     *            WebPA Parameter
     * @param snmpMib
     *            SNMP OID
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
     * Utility method to verify a file is encrypted. It uses the command: cat <fileName>
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param fileName
     *            String representing the Library Name
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
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param searchText
     *            searchText to execute in device
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
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param serialNumber
     *            serial number to be compared with parameter
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
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param ecmMac
     *            mac to be compared with parameter
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
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param command
     *            command to execute
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
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param processName
     *            String representing the process name
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
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param searchText
     *            search text message
     * @param logFile
     *            log file location
     * @param pollDuration
     *            maximum poll duration
     * @param pollInterval
     *            polling interval
     * @return searchResponse response for search text
     * @author ArunKumar Jayachandran
     */
    public static String searchLogFilesInAtomConsoleByPolling(AutomaticsTapApi tapEnv, Dut settop, String searchText,
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
	    searchResponse = BroadBandCommonUtils.executeCommandInAtomConsole(settop, tapEnv, command);
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
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param processName
     *            String representing the Process Name
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
     * @param device
     *            Instance of{@link Dut}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param searchText
     *            Text to search
     * @param logFileName
     *            Log file name
     * @param patternToMatch
     *            Pattern to match
     * @param isAtom
     *            True - To verify the logs in Atom console.False-To verify the logs in Arm console
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
     * Utility method that performs search in ATOM Console & extract only the search response from the ATOM Console
     * Execution Response.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut} to be validated
     * @param searchText
     *            String representing the text to be searched.
     * @param logFile
     *            String representing the log file in which Search to be performed.
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
     * @param partnerIdToVerify
     *            Actual partnerID retrieved from script
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
     * method to compare the values of wifi paramters obtained by webpa command with the defaults
     * 
     * @param Map
     *            of default webpa values obtained
     * @param RdkBWiFiParameters
     *            parameter
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
     * method to compare the values of wifi paramters obtained by webpa command with the defaults
     * 
     * @param Map
     *            of default webpa values obtained
     * @param RdkBWiFiParameters
     *            parameter
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
     * @param settop
     *            {@link Settop}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param isAtomConsole
     *            isAtomConsole
     * @param process
     *            Process to be verified
     * 
     * @return true if process is running
     * @author sgnana010c
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
     * Utility Method to perform the check on whether file exists. The file check operation is being performed by
     * polling for the existence of file for 10 minutes.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param completeFilePath
     *            String representing the complete file path.
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
     * @param Dut
     *            {@link device}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param processName
     *            Name of the Process
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
      * @param tapEnv
      *            AutomaticsTapApi instance
      * @param settop
      *            instance of settop
      * @param dummyTest
      *            Test file path
      * 
      * @return true if the file is created with the read write permission
      * 
      * @author Karthick Pandiyan
      */

     public static boolean createAndVerifyOwnerReadWritePermission(AutomaticsTapApi tapEnv, Dut device, String dummyTest) {
 	LOGGER.debug("STARTING METHOD: createAndVerifyOwnerReadWritePermission()");
 	boolean status = false;
 	String response = null;
 	try {
 	    tapEnv.executeCommandUsingSsh(device,
 		    BroadBandCommandConstants.CMD_TOUCH + BroadBandTestConstants.SINGLE_SPACE_CHARACTER + dummyTest);

 	    if (CommonUtils.isFileExists(device, tapEnv, dummyTest)) {

 		response = tapEnv.executeCommandUsingSsh(device,
 			BroadBandCommonUtils.concatStringUsingStringBuffer(
 				BroadBandTestConstants.CMD_TO_LONGLIST_FOLDER_FILES, BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
 				dummyTest));

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
      * @param tapEnv
      *            AutomaticsTapApi instance
      * @param settop
      *            instance of settop
      * @param createFile
      *            Test file path
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
      * @param device
      *            Dut Instance
      * @param tapEnv
      *            AutomaticsTapApi Instance
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
 		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
 	    startTime = System.currentTimeMillis();
 	    do {
 		isStbAccessible = CommonMethods.isSTBAccessible(device);
 	    } while (!isStbAccessible
 		    && ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.EIGHT_MINUTE_IN_MILLIS)
 		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
 	    errorMessage = "Failed to verify StbAccessible";
 	} catch (Exception exception) {
 	    errorMessage = "Exception occured while performing reboot " + exception.getMessage();
 	    LOGGER.error(errorMessage);
 	}
 	LOGGER.info("ENDING METHOD: verifySTBRebootAndStbAccessible()");
 	return isRebooted && isStbAccessible;
     }

     /**
      * Method to check whether Log file is available in rdklogs/logs and tail them to Backup<Filename> in nvram in given
      * console type
      * 
      * @param Dut
      *            device instance
      * @param tapEnv
      *            AutomaticsTapApi instance
      * @param mustHaveLogFileList
      *            List<String> logfiles
      * @param tailValue
      *            value to grep line
      * @param polledTime
      *            long polled time
      * @param maxDuration
      *            long maxtime
      * @param consoleType
      *            String consoletype
      * @param backUpPath
      *            String path for backup
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
      * @param device
      *            Dut
      * @param tapEnv
      *            instance of {@link AutomaticsTapApi}
      * @param mustHaveLogFileList
      *            Must have log file list
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
      * @param hmap
      *            Hash map to hold files and its size
      * @param logFileList
      *            List to hold all the required log files
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
      * @param tapEnv
      *            AutomaticsTapApi instance
      * @param command
      *            command to be executed
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
 	    // SSH connection util
 	    SshConnection sshConnection = ServerUtils.getSshConnection(jumpServer);
 	    // Obtaining the command response
 	    response = tapEnv.executeCommandUsingSshConnection(WhiteListServer.getInstance(tapEnv, jumpServer), command);
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
      * @param device
      *            Dut instance
      * @param tapEnv
      *            AutomaticsTapApi instance
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
 		isSnmpRebootReasonValid = BroadBandWebPaUtils.getAndVerifyWebpaValueInPolledDuration(
 			device,
 			tapEnv,
 			BroadBandWebPaConstants.WEBPA_COMMAND_LAST_REBOOT_REASON,
 			jsonObj.has(device.getModel()) ? jsonObj.getString(device.getModel()) : jsonObj
 				.getString(BroadBandTestConstants.DEFAULT),
 			BroadBandTestConstants.ONE_MINUTE_IN_MILLIS, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
 	    }
 	} catch (Exception e) {
 	    LOGGER.error("Exception occured while verifying SNMP reboot reason:" + e.getMessage());
 	}
 	LOGGER.debug("ENDING METHOD: verifySnmpRebootReason()");
 	return isSnmpRebootReasonValid;
     }
     
     /**
      * Method to execute grep command to grep telemetry marker for SNMP reboot based on the device vendor
      * 
      * @param tapEnv
      *            AutomaticsTapApi instance
      * @param settop
      *            The settop to be validated
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
      * @param tapEnv
      *            {@link AutomaticsTapApi}
      * @param device
      *            {@link Dut}
      * @param processName
      *            String representing the process name
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
     

}

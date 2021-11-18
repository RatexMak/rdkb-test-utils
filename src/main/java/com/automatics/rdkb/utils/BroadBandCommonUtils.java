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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.text.ParseException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Device;
import com.automatics.device.Dut;
import com.automatics.exceptions.TestException;
import com.automatics.providers.connection.SshConnection;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants.syndicationPartnerID;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants.RdkBSsidParameters;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.rdkb.constants.RDKBTestConstants.WiFiFrequencyBand;
import com.automatics.rdkb.constants.WebPaParamConstants;
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
     * Helper method to get partner value for web UI validation
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @return the true if xb3 uptime is greater than 20 mis
     * @author Govardhan
     */
    public static boolean getXB3UptimeStatus(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug("Starting of Method: getXB3UptimeStatus");
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
	LOGGER.debug("Ending of Method: getXB3UptimeStatus");
	return status;
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

	// String response = tapEnv.executeCommandUsingSsh(settop,
	// sbCommand.toString());

	/*
	 * if (SupportedModelHandler.isXB3(settop) &&
	 * (searchText.contains(BroadBandTraceConstants.ACTIVATION_WIFI_BROADCAST_COMPLETE) ||
	 * searchText.contains(BroadBandTraceConstants.ACTIVATION_WIFI_NAME_BROADCAST))) { response =
	 * tapEnv.executeCommandOnAtom(settop, sbCommand.toString()); } else { response =
	 * tapEnv.executeCommandUsingSsh(settop, sbCommand.toString()); }
	 */
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
     * Utility Method to verify the Factory Reset is successful; verified using the Private SSID which must contain the
     * last 4 digits of ECM MAC Address of the device.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * 
     * @return Boolean representing the result of the validation; TRUE if the factory reset is successful; else FALSE.
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
		//result = !CommonUtils.executeTestCommand(tapEnv, device);
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
	    result = verifyFactoryReset(tapEnv, device);
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
     * Utility method to Validate defualt SSId for various Partners
     * 
     * @param devive
     *            Dut under test
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param ssidValue
     *            String ssid obtained by webpa command
     * @param parameter
     *            parameter for Band of the Wi-Fi
     * @param deviceModel
     *            String instance for Device Model
     * @param PartnerId
     *            String instance of Partener ID for which Expected SSID varies
     * @param radio
     *            {@link WiFiFrequencyBand}
     * @return boolean true if the ssid is as expected
     * 
     *         Expected SSID for cisco - 'HOME-[last four digits of ecmmac]-[wifiband]' for tech cbr- 'CBCI-[last four
     *         digits of ecmmac]' for others - 'XFSETUP-[last four digits of ecmmac]'
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
	    prefix = BroadBandTestConstants.DEFAULT_SSID_PREFIX_FOR_DEVICE_AND_PARTNER_SPECIFIC.get(device.getModel())
		    .get(partnerId);
	    LOGGER.info("Prefix for The SSID Specific To Device is: " + prefix);
	    String ecmMac = ((Device) device).getEcmMac();
	    LOGGER.info("ECMMac is : " + ecmMac);
	    try {
		serialNumber = tapEnv
			.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_SERIAL_NUMBER).trim();
	    } catch (Exception exception) {
		LOGGER.error("Caught exception while getting the serial Number by WebPa" + exception.getMessage());
	    }
	    ecmMac = ecmMac.replace(BroadBandTestConstants.DELIMITER_COLON, BroadBandTestConstants.EMPTY_STRING);
	    LOGGER.info("ecmMAC of the device is: " + ecmMac);

		expectedSsid = BroadBandCommonUtils.concatStringUsingStringBuffer(prefix,
			BroadBandTestConstants.SYMBOL_HYPHEN,
			ecmMac.substring(ecmMac.length() - RDKBTestConstants.CONSTANT_4));

	    LOGGER.info("Expected SSID is: " + expectedSsid);
	    status = ssidValue.equalsIgnoreCase(expectedSsid);
	    LOGGER.info("Is Expected and Actual SSID are equal :" + status);
	} catch (Exception e) {
	    LOGGER.error(
		    "Caught exception while Validating the default SSid for Different Partners : " + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD validateDefaultSsidforDifferentPartners");
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
     * Method to search log file in Arm or Atom for XB3 and provide response
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

	LOGGER.info("Ping Command: " + pingCommand);
	pingResponse = BroadBandCommonUtils.executeCommandInJumpServer(tapEnv, pingCommand);
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
     * Helper method to execute command in jump server
     * 
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param command
     *            command to be executed
     * @return string , response of the command executed.
     * @author Praveenkumar Paneerselvam
     * @Refactor Athira
     */
    public static String executeCommandInJumpServer(AutomaticsTapApi tapEnv, String command) {
	LOGGER.debug("STARTING METHOD: executeCommandInIpv6JumpServer");
	String jumpServer = null;
	String response = null;
	try {
	    // Executing the command directly in jump server.
	    LOGGER.info("Jump Server detail is - " + jumpServer);
	    // SSH connection util of EcatsFramework
	    SshConnection sshConnection = ServerUtils.getSshConnection(jumpServer);
	    // Obtaining the command response
	    // response = tapEnv.executeCommandUsingSshConnection(sshConnection, command);
	} catch (Exception exception) {
	    LOGGER.error(
		    "Failed to execute command in jump server " + jumpServer + ". Error - " + exception.getMessage());
	}
	LOGGER.debug("ENDING METHOD: executeCommandInIpv6JumpServer");
	return response;
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
	    String response, String processListKey) {

	StringBuffer processNotInitialized = new StringBuffer();
	StringBuffer processInitializedMoreThanOne = new StringBuffer();
	BroadBandResultObject broadBandResultObject = new BroadBandResultObject();

	List<String> runningProcessList = new ArrayList<>();
	List<String> notRunningProcessList = new ArrayList<>();
	Map<String, String> pidMap = new HashMap<String, String>();
	boolean isProcessNotInitializedProperly = false;

	// String processList = tapEnv.getProperty(settop, processListKey);
	String processList = AutomaticsPropertyUtility.getProperty(processListKey);

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
     */
    public static BroadBandResultObject verifyAllRequiredBroadBandProcessesForQt(Dut device, AutomaticsTapApi tapEnv) {

	String processListKey = "xb6.process.list.qt";
	String commandResponse = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.CMD_GET_PROCESS_DETAILS);
	return validateAllProcessesDefinedInProperties(device, tapEnv, commandResponse, processListKey);
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

		// Build name is split into core, p, s version in order to match the expected logic
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
		// condition covers '4.4 initial releases until 4.4p1s9' or 'Release 4.4p2s3 to latest release
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
     * Method to get snmp mib index to get wan mac from a device
     * 
     * @param tapEnv
     *            EcatsTapApi instance
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
}

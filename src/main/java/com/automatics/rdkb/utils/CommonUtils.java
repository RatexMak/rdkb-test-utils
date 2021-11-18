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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.core.SupportedModelHandler;
import com.automatics.device.Device;
import com.automatics.device.Dut;
import com.automatics.enums.ExecutionStatus;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;

public class CommonUtils {

    public static final String CMD_TEST_CONNECTION = "echo test_connection";

    /** SLF4J logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

    /**
     * Method to check pattern string in target String
     * 
     * @param targetString
     *            
     * @param patterToSearch
     *          
     * @return boolean true, if pattern found in target string else false
     * 
     * @author Govardhan
     */
    public static boolean patternSearchFromTargetString(String targetString, String patterToSearch) {
	LOGGER.debug("START METHOD: patternSearchFromTxt");
	boolean isPatternFoundInText = false;
	Pattern compiledPattern = Pattern.compile(patterToSearch);
	Matcher matcher = compiledPattern.matcher(targetString);
	if (matcher.find()) {
	    isPatternFoundInText = true;
	    LOGGER.info("Pattern Found = " + patterToSearch);
	    LOGGER.info(matcher.group(0));
	} else {
	    LOGGER.error("Pattern Not Found = " + patterToSearch);
	}
	LOGGER.info("is patter found for " + patterToSearch + " in target string = " + isPatternFoundInText);
	LOGGER.debug("END METHOD: patternSearchFromTxt");
	return isPatternFoundInText;
    }

    /**
     * Update the execution status during exception scenario based on the error message.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param testId
     *            Manual test ID
     * @param testStepNumber
     *            Manual test step number.
     * @param status
     *            Flag representing the Status.
     * @param errorMessage
     *            String representing Error message.
     * @param blockExecution
     *            Flag to throw an exception to block further execution of test cases.
     * @author Govardhan
     */
    public static void updateTestStatusDuringException(AutomaticsTapApi tapEnv, Dut device, String testId,
	    String testStepNumber, boolean status, String errorMessage, boolean blockExecution) {
	if (CommonMethods.isNull(errorMessage)) {
	    errorMessage = RDKBTestConstants.EMPTY_STRING;
	}
	if (errorMessage.contains(RDKBTestConstants.PRE_CONDITION_ERROR)) {
	    tapEnv.updateExecutionForAllStatus(device, testId, testStepNumber, ExecutionStatus.NOT_TESTED, errorMessage,
		    true);
	} else {
	    tapEnv.updateExecutionStatus(device, testId, testStepNumber, false, errorMessage, true);
	}
    }

    /**
     * Method to verify if the commandOutput contains the given string
     * 
     * @param commandOutput
     *            Output of the command
     * @param stringVerication
     *            String to be verified
     * @return status true if the log is present else false
     * @author Govardhan
     */
    public static boolean isGivenStringAvailableInCommandOutput(String commandOutput, String stringVerication) {

	LOGGER.debug("STARTING METHOD: MiscellaneousTests.isGivenStringAvailableInCommandOutput()");
	// Validation status
	boolean status = false;
	if (CommonMethods.isNotNull(commandOutput) && commandOutput.contains(stringVerication)) {
	    status = true;
	}
	LOGGER.info("Presence of parameter " + stringVerication + " in the command output : " + status);
	LOGGER.debug("ENDING METHOD: MiscellaneousTests.isGivenStringAvailableInCommandOutput()");
	return status;
    }

    /**
     * Provision the device IP address. It can be either ip4 or ip6 address.
     * 
     * @return IP4 or IP6 address.
     * @see device#getHostIp4Address()
     * @see device#getHostIp6Address()
     */
    public static String getIPAddress(Dut device) {
	String hostIpAddress = null;
	if (SupportedModelHandler.isRDKB(device)) {
	    hostIpAddress = ((Device) device).getErouterIpAddress();
	} else {
	    hostIpAddress = device.getHostIp4Address();
	    if (StringUtils.isBlank(hostIpAddress)) {
		hostIpAddress = device.getHostIp6Address();
	    }
	}
	return hostIpAddress;
    }

    /**
     * Method to do Reboot and wait for IpAcquisition
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @return status true if device is accessible
     * @author Govardhan
     */
    public static boolean rebootAndWaitForIpAcquisition(AutomaticsTapApi tapEnv, Dut device) {
	boolean status = false;
	LOGGER.info("STARTING METHOD: rebootAndWaitForIpAcquisition()");
	try {
	    LOGGER.info("About to reboot the device..");
	    tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_TO_REBOOT_DEVICE);
	    tapEnv.waitTill(AutomaticsConstants.THREE_MINUTES);
	    status = CommonMethods.waitForEstbIpAcquisition(tapEnv, device);
	} catch (Exception e) {
	    LOGGER.info("Exception caught rebooting the device" + e.getMessage());
	}
	LOGGER.info("Ending METHOD: rebootAndWaitForIpAcquisition()");
	return status;
    }

    /**
     * This utility method validates the trace message.
     * 
     * @param taEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param message
     *            Message that needs to be searched in the trace log.
     * @param traceWaitTime
     *            Duration for which search needs to be performed before throwing timeout.
     * @param presence
     *            boolean representing the presence or absence of the trace message to be validated.
     * 
     * @return Boolean representing the result of the operation
     */
    public static boolean validateTraceLog(AutomaticsTapApi tapEnv, Dut device, String message, long traceWaitTime,
	    boolean presence) {
	boolean result = false;
	LOGGER.debug("ENTERING METHOD validateTraceLog");
	LOGGER.info("TRACE MESSAGE TO BE VALIDATED: " + message);
	String[] arrMessage = message.split(AutomaticsConstants.DELIMITER_HASH);
	LOGGER.info("arrMessage = " + arrMessage);	
	for (int iCounter = 0; iCounter < arrMessage.length; iCounter++) {
	    LOGGER.info("TRACE MESSAGE TO BE VALIDATED: " + arrMessage[iCounter]);
	    String response = tapEnv.searchAndGetTraceLineWithMatchingString(device, arrMessage[iCounter].trim(),
		    traceWaitTime);
	    result = CommonMethods.isNotNull(response) ? presence : !presence;
	    if (result) {
		break;
	    }
	}
	LOGGER.info("TRACE LOG VALIDATION = " + result);
	LOGGER.debug("ENDING METHOD validateTraceLog");
	return result;
    }

    /**
     * Method to check whether GRAM MODE is enabled or not
     * 
     * @return boolean
     */
    public static boolean isGRAMModeEnabled() {

	String buildAppender = System.getProperty(AutomaticsConstants.SYSTEM_PROPERTY_BUILD_APPENDER,
		AutomaticsConstants.EMPTY_STRING);
	String executionMode = System.getProperty(AutomaticsConstants.SYSTEM_PROPERTY_EXECUTION_MODE,
		RDKBTestConstants.EXECUTION_MODE_SP);
	// Make isGramModeEnabled to true for Account execution mode or
	// if in
	// buildAppender, user provided
	// option to execute as GRAM(enable revstbssh)
	LOGGER.debug("Inside isGramModeEnabled : buildAppender : {}, executionMode : {} ", buildAppender,
		executionMode);
	boolean isGramExecutionMode = executionMode.equalsIgnoreCase(RDKBTestConstants.EXECUTION_MODE_GRAM);
	boolean isAccountTestExecutionMode = executionMode.equalsIgnoreCase(AutomaticsConstants.EXECUTION_MODE_ACCOUNT);
	boolean isGramBuildAppender = buildAppender.toLowerCase()
		.contains(RDKBTestConstants.GRAM_APPENDER.toLowerCase());

	if (isGramExecutionMode || (isAccountTestExecutionMode && isGramBuildAppender)) {
	    LOGGER.info("isGRAMModeEnabled: true");
	    return true;
	}

	return false;
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

	commandResponse = tapEnv.executeCommandUsingSsh(device, CMD_TEST_CONNECTION);
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
     * method to patternFinderGroupTwo
     */
    public static String patternFinderGroupTwo(String response, String patternToMatch) {
	Pattern pattern = Pattern.compile(patternToMatch);
	Matcher matcher = pattern.matcher(response);
	LOGGER.info(new StringBuilder().append("The response is ").append(response).toString());

	if (matcher.find()) {
	    LOGGER.info("------------MATCH FOUND FOR THE PATTERN-----------");
	    return matcher.group(2);
	}
	return "";
    }

    /**
     * method to get box uptime in seconds.
     * 
     * @param device
     * @param tapApi
     * 
     * @return box uptime in seconds
     */
    public static long getBoxUptimeInSeconds(Dut device, AutomaticsTapApi tapEnv) {

	// stores the uptime in minutes
	long minutes = 0;
	// stores the uptime in hours
	long hours = 0;
	// stores Uptime in seconds
	long boxUptime = 0L;
	try {
	    // get the uptime response
	    String upTimeResponse = tapEnv.executeCommandUsingSsh(device, RDKBTestConstants.CMD_UPTIME);

	    if (CommonMethods.isNotNull(upTimeResponse)
		    && upTimeResponse.contains(RDKBTestConstants.COMMAND_NOT_FOUND_ERROR)) {
		LOGGER.error("uptime command response obtained is invalid - > uptime command response : "
			+ upTimeResponse + "!!! Exiting....");
	    }

	    if (upTimeResponse.contains("min")) {
		/*
		 * If the uptime is less than an hour then it will indicate the uptime in minutes only, indicated by
		 * 'min' So here we are parsing the number of minutes the box is up and subtracting the calculated
		 * seconds (min * 60) from the current epoch time to get the estimated epochtime for boot time
		 */
		LOGGER.info("Box is up for less than an hour, now parsing the minutes");
		try {
		    minutes = Long.parseLong(CommonMethods.patternFinder(upTimeResponse,
			    RDKBTestConstants.PATTERN_GET_MINUTES_FROM_UPTIME_RESPONSE));
		} catch (NumberFormatException exception) {
		    LOGGER.error("Number format exception occured!! -> " + exception.getMessage());
		}
		boxUptime = minutes * 60;
	    } else {
		// Handle the case where up time is in days
		if (upTimeResponse.contains("day")) {
		    try {
			long days = Long.parseLong(CommonMethods.patternFinder(upTimeResponse,
				RDKBTestConstants.PATTERN_GET_DAYS_FROM_UPTIME_RESPONSE));
			boxUptime = days * 24 * 60 * 60;

			upTimeResponse = upTimeResponse.replaceAll(Long.toString(days) + "\\s*+day+\\s*+,", "");
		    } catch (NumberFormatException exception) {
			LOGGER.error("Number format exception occurred!! -> " + exception.getMessage());
		    }
		}
		/*
		 * If the uptime is more than an hour then it will indicate uptime in the format e.g '1:15', implying
		 * the box has been up for 1 hour and 15 minutes. So here we subtract the total seconds calculated
		 * (hours * 3600 + min * 60) from the current epoch time to get the estimated epochtime for boot time
		 */

		try {
		    hours = Long.parseLong(CommonMethods.patternFinder(upTimeResponse,
			    RDKBTestConstants.PATTERN_GET_HOURS_MINUTES_FROM_UPTIME_RESPONSE));
		    boxUptime = boxUptime + (hours * 3600);

		    minutes = Long.parseLong(CommonUtils.patternFinderGroupTwo(upTimeResponse,
			    RDKBTestConstants.PATTERN_GET_HOURS_MINUTES_FROM_UPTIME_RESPONSE));
		    boxUptime = boxUptime + (minutes * 60);

		    LOGGER.info("Box is up for more than an hour, parsed the hours and minutes");
		} catch (NumberFormatException exception) {
		    LOGGER.error("Number format exception occurred!! -> " + exception.getMessage());
		}
	    }
	} catch (Exception e) {
	    LOGGER.error(e.getMessage());
	}
	LOGGER.info("Box Uptime in Seconds : " + boxUptime);

	return boxUptime;
    }
    
    /**
     * Utility to validate the string is neither null nor blank.
     * 
     * @param text
     *            String to be validated
     * 
     * @return Boolean result of the validation.
     * @author Govardhan
     */
    public static boolean isNotEmptyOrNull(String text) {
	boolean result = false;
	if (null != text && !text.trim().isEmpty()) {
	    result = true;
	}
	return result;
    }
}

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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.core.SupportedModelHandler;
import com.automatics.device.Device;
import com.automatics.device.Dut;
import com.automatics.enums.ExecutionMode;
import com.automatics.enums.ExecutionStatus;
import com.automatics.exceptions.TestException;
import com.automatics.http.ServerCommunicator;
import com.automatics.http.ServerResponse;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.utils.AutomaticsPropertyUtility;

public class CommonUtils {

    /** SLF4J logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);

    public static final String CMD_TEST_CONNECTION = "echo test_connection";

    /** DeviceConfig IP Address Type - ECM. */
    public static final String DEVICE_IP_ADDRESS_TYPE_ECM = "ECM";

    /** DeviceConfig IP Address Type - ESTB. */
    public static final String DEVICE_IP_ADDRESS_TYPE_ESTB = "ESTB";

    /** Variable to hold the property name of Box Health Checker Server IP and port */
    public static final String BOX_HEALTH_CHECKER_URL_PROPERTY = "box.health.check.url";

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

    /**
     * Utility to method to search for the given text in the given log file based on Poll Interval & Poll Duration;
     * returns String representing the search response.
     * 
     * @param tapEnv
     *            {@link ECatsTapApi}
     * @param settop
     *            {@link Settop}
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
     * @Refactor Athira
     */
    public static String searchLogFilesWithPollingInterval(AutomaticsTapApi tapEnv, Dut device, String searchText,
	    String logFile, long pollDuration, long pollInterval) {
	LOGGER.debug("STARTING METHOD searchLogFilesWithPollingInterval");
	StringBuffer sbCommand = new StringBuffer(RDKBTestConstants.GREP_COMMAND);

	if (CommonMethods.isNull(searchText)) {
	    return null;
	}

	// In case the search text contains space and not wrapped with double quotes.
	if (searchText.contains(RDKBTestConstants.SINGLE_SPACE_CHARACTER)
		&& !searchText.contains(AutomaticsConstants.DOUBLE_QUOTE)) {
	    sbCommand.append(AutomaticsConstants.DOUBLE_QUOTE);
	    sbCommand.append(searchText);
	    sbCommand.append(AutomaticsConstants.DOUBLE_QUOTE);
	} else {
	    sbCommand.append(searchText);
	}
	sbCommand.append(RDKBTestConstants.SINGLE_SPACE_CHARACTER);
	sbCommand.append(logFile);
	LOGGER.info("COMMAND TO BE EXECUTED: " + sbCommand.toString());
	long startTime = System.currentTimeMillis();
	String searchResponse = null;
	do {
	    tapEnv.waitTill(pollInterval);
	    searchResponse = tapEnv.executeCommandUsingSsh(device, sbCommand.toString());
	    searchResponse = CommonMethods.isNotNull(searchResponse)
		    && !searchResponse.contains(AutomaticsConstants.NO_SUCH_FILE_OR_DIRECTORY) ? searchResponse.trim()
			    : null;
	} while ((System.currentTimeMillis() - startTime) < pollDuration && CommonMethods.isNull(searchResponse));
	LOGGER.info(
		"SEARCH RESPONSE FOR - " + searchText + " IN THE LOG FILE - " + logFile + " IS : " + searchResponse);
	LOGGER.debug("ENDING METHOD searchLogFilesWithPollingInterval");
	return searchResponse;
    }

    /**
     * Method to retrieve device IP address from BHC
     * 
     * @param device
     *            The device to be used.
     * @param type
     *            The type of IP address, ECM or ESTB.
     * @return The requested IP address.
     */
    public static String getDeviceIpAddressFromBhc(Dut device, String type) {

	LOGGER.debug("Entering into getDeviceIpAddressFromBhc() : Type = " + type);

	String deviceIpAddress = null;
	String macAddress = null;
	StringBuffer targetURL = null;
	ServerCommunicator serverCommunicator = new ServerCommunicator(LOGGER);
	ServerResponse serverResponse = null;
	String healthCheckerURL = null;
	try {
	    healthCheckerURL = AutomaticsPropertyUtility.getProperty(BOX_HEALTH_CHECKER_URL_PROPERTY);
	    targetURL = new StringBuffer();
	    targetURL.append(healthCheckerURL);
	    targetURL.append("/bhc/getSTBIpAddress.htm?mac=");
	    if (DEVICE_IP_ADDRESS_TYPE_ECM.equalsIgnoreCase(type)) {
		macAddress = ((Device) device).getEcmMac();
	    } else if (DEVICE_IP_ADDRESS_TYPE_ESTB.equalsIgnoreCase(type)) {
		macAddress = device.getHostMacAddress();
	    } else {
		LOGGER.error("Invalid DeviceConfig IP Address Type :" + type);
	    }
	    targetURL.append(macAddress);
	    targetURL.append("&macType=" + type);

	    LOGGER.info("Fetch " + type + "IP Address from BHC using the url:" + targetURL);

	    serverResponse = serverCommunicator.postDataToServer(targetURL.toString(), null, "GET", 30000, null);

	    if (serverResponse != null) {
		if (serverResponse.getResponseCode() == HttpStatus.SC_OK) {
		    // Set the obtained Ip address.
		    deviceIpAddress = serverResponse.getResponseStatus();
		} else {
		    LOGGER.info("Invalid response from BHC for STB IP Address request -" + serverResponse.toString());
		}
	    }
	} catch (Exception exec) {
	    LOGGER.info("getDeviceIpAddressFromBhc() : Exception " + exec + "Occurred while trying to get IP Address");
	}

	LOGGER.info(type + " DeviceConfig IP Address from BHC - " + deviceIpAddress);

	LOGGER.debug("Exiting from getDeviceIpAddressFromBhc");
	return deviceIpAddress;
    }

    /**
     * Helper method to check whether box is rebooted or not via connection to the old ip after reboot
     * 
     * @param device
     *            the Dut instance to be tested
     * @param tapEnv
     *            The AutomaticsTapApi instance
     * 
     * @return true in the connection is not accessible
     */

    public static boolean verifyStbRebooted(Dut device, AutomaticsTapApi tapEnv) {
	boolean status = true;
	// String for status verification
	String successString = "CONNECTION-ESTABLISHED";

	LOGGER.info("About to execute command echo CONNECTION-ESTABLISHED");
	String response = tapEnv.executeCommandUsingSsh(device, "echo " + successString);
	LOGGER.info(" : Server response during device reboot - " + response);
	if (response != null) {
	    status = response.contains(successString);
	}

	return (!status);
    }

    /**
     * Helper method to check wait for device to reboot
     * 
     * @param device
     *            the Dut instance to be tested
     * @param tapEnv
     *            The AutomaticsTapApi instance
     * 
     * @return true in the connection is not accessible
     * @refactor Govardhan
     */
    public static boolean waitForDeviceToReboot(AutomaticsTapApi tapEnv, Dut device, long timeInMilli) {
	LOGGER.info("STARTING METHOD: waitForDeviceToReboot");

	boolean deviceRebootNotification = false;
	long startTime = System.currentTimeMillis();

	while ((!(deviceRebootNotification)) && (System.currentTimeMillis() - startTime < timeInMilli)) {
	    deviceRebootNotification = !(CommonMethods.isSTBAccessible(device));
	    if (deviceRebootNotification) {
		LOGGER.info("Device rebooted automatically");
		break;
	    }
	    tapEnv.waitTill(30000L);
	    LOGGER.info("Tring to validate reboot one more time");
	}
	LOGGER.info(new StringBuilder().append("Device reboot validated within ").append(timeInMilli).append(" millis")
		.toString());
	LOGGER.info("ENDING METHOD: waitForDeviceToReboot");
	return deviceRebootNotification;
    }

    /**
     * Utility method to search the String in the log files.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param searchCommand
     *            Command to be executed for search.
     * 
     * @return Boolean representing the result of search operation.
     * @refactor Govardhan
     */
    public static boolean searchLogFiles(AutomaticsTapApi tapEnv, Dut device, String searchCommand) {
	LOGGER.info("ENTERING METHOD searchLogFiles");
	String response = tapEnv.executeCommandUsingSsh(device, searchCommand);
	LOGGER.info("LOG SEARCH RESPONSE = " + response);
	boolean result = CommonMethods.isNotNull(response)
		&& !response.contains(RDKBTestConstants.NO_SUCH_FILE_OR_DIRECTORY);
	LOGGER.info("LOG SEARCH RESULT = " + result);
	LOGGER.info("ENDING METHOD searchLogFiles");
	return result;
    }

    /**
     * Helper method to get the PID of a process and kill the same
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param processName
     *            Process anme which is to be killed
     * @return status killed status
     * @refactor Govardhan
     */
    public static boolean getProcessIdAndKill(Dut device, AutomaticsTapApi tapEnv, String processName) {

	LOGGER.info("STARTING METHOD: getProcessIdAndKill()");
	boolean status = true;
	String KILL_CMD = "kill -9 ";

	String processId = CommonUtils.getPidOfProcess(device, tapEnv, processName);
	if (CommonMethods.isNull(processId)) {
	    // retry to fetch pid
	    processId = CommonUtils.getPidOfProcess(device, tapEnv, processName);
	    LOGGER.info("Obtained process ID : " + processId);
	}

	if (CommonMethods.isNotNull(processId)) {
	    LOGGER.info("Killing process ID : " + processId + " using command : " + KILL_CMD + processId);
	    // kill the process
	    tapEnv.executeCommandUsingSsh(device, KILL_CMD + processId);

	    // waiting for five seconds to see if process has restarted
	    tapEnv.waitTill(RDKBTestConstants.FIFTEEN_SECONDS_IN_MILLIS);

	    LOGGER.info(" Reverifying whether process got killed or not : ");
	    // reverify the process is not running
	    String newProcessId = CommonUtils.getPidOfProcess(device, tapEnv, processName);

	    LOGGER.info(" Obtained process ID after executing KILL Command : " + newProcessId);
	    if (CommonMethods.isNull(newProcessId)) {
		status = true;
		LOGGER.info(" Successfully killed process : " + processId);
	    } else {
		status = false;
		if (newProcessId.equals(processId)) {
		    LOGGER.info(" Failed to kill process : " + processId);
		} else {
		    status = true;
		    LOGGER.info("Process gor restarted with new Pid " + newProcessId);
		}
	    }
	}
	LOGGER.info("ENDING METHOD: getProcessIdAndKill()");
	return status;
    }

    /**
     * Retrieves the process id of the process given as parameter.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param process
     *            The process name of which pid is to be found
     * 
     * @return Process Id of the process,null if not found
     * @refactor Govardhan
     */
    public static String getPidOfProcess(Dut device, AutomaticsTapApi tapEnv, String process) {
	return CommonMethods.getPidOfProcess(device, tapEnv, process);
    }

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
	LOGGER.info("Entering into concatStringUsingStringBuffer");
	StringBuffer command = new StringBuffer();
	for (String cmd : values) {
	    command.append(cmd);
	}
	LOGGER.info("Exiting into concatStringUsingStringBuffer");
	return command.toString();
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
     * Method to verify whether file exists
     * 
     * @author Govardhan
     */
    public static boolean isFileExists(Dut device, AutomaticsTapApi tapEnv, String completeFilePath) {
	LOGGER.info("STARTING METHOD: isFileExists()");
	boolean status = false;

	String response = tapEnv.executeCommandUsingSsh(device, new StringBuilder().append("if [ -f ")
		.append(completeFilePath).append(" ] ; then echo \"true\" ; else echo \"false\" ; fi").toString());

	if (CommonMethods.isNotNull(response)) {
	    if (response.trim().equals("true")) {
		status = true;
	    } else if (response.trim().equals("false")) {
		status = false;
	    } else {
		throw new TestException("Obtained response other than 'true' or 'false'");
	    }
	} else {
	    throw new TestException("Obtained null response");
	}
	LOGGER.info("ENDING METHOD: isFileExists()");
	return status;
    }

    /**
     * Method to verify whether file deleted
     * 
     * @author Govardhan
     */
    public static boolean deleteFile(Dut device, AutomaticsTapApi tapEnv, String completeFilePath) {
	LOGGER.info("STARTING METHOD: deleteFile()");
	boolean status = false;

	tapEnv.executeCommandUsingSsh(device,
		new StringBuilder().append("rm -rf ").append(completeFilePath).toString());
	status = isFileExists(device, tapEnv, completeFilePath);
	if (status) {
	    tapEnv.executeCommandUsingSsh(device,
		    new StringBuilder().append("rm -rf ").append(completeFilePath).toString());
	}
	LOGGER.info("ENDING METHOD: deleteFile()");
	return (!(isFileExists(device, tapEnv, completeFilePath)));
    }

    /**
     * Method to get Device MacAddress With Colon Replaced
     * 
     * @author Govardhan
     */
    public static String getDeviceMacAddressWithColonReplaced(Dut device, String stringToReplaceColon) {
	return ((device != null) ? device.getHostMacAddress().replace(":", stringToReplaceColon) : null);
    }

    /**
     * Method to get Device MacAddress With Colon Replaced
     * 
     * @author Govardhan
     */
    public static String getDeviceId(Dut device) {
	String macWithColunReplaced = getDeviceMacAddressWithColonReplaced(device, "");

	return ((null != macWithColunReplaced) ? macWithColunReplaced.toUpperCase() : null);
    }
    /**
     * This utility method will perform the COPY, REMOVE & UPDATE values in a file operations.
     * 
     * @param tapEnv
     *            {@link ECatsTapApi}
     * @param settop
     *            {@link Settop}
     * @param command
     *            Command to be executed
     * 
     * @return Result of operation execution.
     */
    public static boolean performCreateRemoveUpdateFileOperations(AutomaticsTapApi tapEnv, Dut settop, String command) {
	boolean result = false;
	String executionResult = tapEnv.executeCommandUsingSsh(settop, command);
	executionResult = executionResult.trim();
	if (executionResult.isEmpty()) {
	    result = true;
	} else {
	    LOGGER.error(executionResult);
	}
	return result;
    }
    
    /**
     * Method to get the uptime of an Stb using "cat /proc/uptime" command
     * 
     * @param tapApi
     *            Instance of ECatsTapApi
     * @param settop
     *            Instance of Settop
     * @return uptime uptime in seconds
     */
    public static String getUptimeFromProc(AutomaticsTapApi tapEnv, Dut device) {

	String uptime = tapEnv.executeCommandUsingSsh(device, RDKBTestConstants.PROC_CMD_UPTIME);
	
	if (CommonUtils.isNotEmptyOrNull(uptime)) {

	    if (uptime.indexOf(".") != -1) {
		uptime = uptime.split("\\.")[0];
	    } else {
		uptime = uptime.split(" ")[0];
	    }

	    if (CommonUtils.isNotEmptyOrNull(uptime)) {

		LOGGER.info("Uptime from /proc/uptime = " + uptime);

	    } else {
		LOGGER.debug("Failed to retrieve uptime from uptime command output");
	    }
	} else {
	    LOGGER.debug("Failed to retrieve uptime from Stb using uptime command");
	    LOGGER.debug("Response = " + uptime);
	}
	LOGGER.debug("uptime = " + uptime);

	return uptime;
    }
    
    public static List<String> patternFinderForMultipleMatches(String response, String patternToMatch)
    {
      LOGGER.debug("STARTING METHOD: patternFinder()");

      String matchedString = "";

      Pattern pattern = null;

      Matcher matcher = null;
      List matchedStringList = new ArrayList();
      try
      {
        if ((CommonMethods.isNotNull(response)) && (CommonMethods.isNotNull(patternToMatch))) {
          pattern = Pattern.compile(patternToMatch);
          matcher = pattern.matcher(response);
          while (matcher.find()) {
            matchedString = matcher.group(1);
            LOGGER.info(new StringBuilder().append("Matching string : ").append(matchedString).toString());
            matchedStringList.add(matchedString.trim());
          }
        }
      } catch (Exception exception) {
        LOGGER.error("Exception occured in patternFinder()", exception);
      }

      LOGGER.info(new StringBuilder().append("No of matched strings -").append(matchedStringList.size()).toString());

      LOGGER.debug("ENDING METHOD: patternFinder()");

      return matchedStringList;
    }
}

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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Dut;
import com.automatics.enums.ProcessRestartOption;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.CommonMethods;

public class BroadBandSystemUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandSystemUtils.class);

    /**
     * Method to check whether telnet file is present in atom console
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param fileName
     *            file name to search
     * @return true if file is not present
     */

    public static boolean verifytelnetFileAvailabilityInAtomConsole(AutomaticsTapApi tapEnv, Dut device,
	    String fileName) {
	LOGGER.debug("Entering into verifytelnetFileAvailabilityInAtomConsole");
	boolean status = false;
	String response = null;
	try {
	    String searchCommand = BroadBandCommonUtils.concatStringUsingStringBuffer(
		    BroadBandCommandConstants.CMD_FIND_INAME, BroadBandTestConstants.SINGLE_SPACE_CHARACTER, fileName,
		    BroadBandCommandConstants.CMD_TELNET_EXCLUDE_UNWANTED);
	    response = BroadBandCommonUtils.executeCommandInAtomConsole(device, tapEnv, searchCommand);
	    LOGGER.info("AtomConsole Response for find command: " + response);
	    status = CommonMethods.isNull(response);
	} catch (Exception exception) {
	    LOGGER.error(
		    "Exception Occurred while verifying File availability in atom console:" + exception.getMessage());

	}
	LOGGER.debug("Exiting into verifytelnetFileAvailabilityInAtomConsole");
	return status;
    }

    /**
     * Method to Validate telnet connection for both telnet and ssh to ATOM console
     *
     * @param tapEnv
     *            {@link AutomaticsTapApi} Reference
     * @param device
     *            device
     * @param connectionType
     *            connectionType either telnet or ssh
     * @param ipAddress
     *            Interface address
     *
     * @author Arunkumar
     * @refactor yamini.s
     */
    public static boolean checkConnectionToIpAddress(AutomaticsTapApi tapEnv, Dut device, String connectionType,
	    String ipAddress) {
	LOGGER.info("Entering into checkConnectionToIpAddress");
	boolean status = false;
	String response = null;
	StringBuffer commandBuffer = new StringBuffer();
	if (connectionType.contains(BroadBandCommandConstants.CMD_SSH)) {
	    commandBuffer.append(BroadBandCommandConstants.CMD_SSH);
	    commandBuffer.append(BroadBandTestConstants.TELNET_ATOM_LOGIN);
	    commandBuffer.append(BroadBandTestConstants.SYMBOL_AT);
	} else if (connectionType.contains(BroadBandCommandConstants.CMD_TELNET)) {
	    commandBuffer.append(BroadBandCommandConstants.CMD_TELNET);
	    commandBuffer.append(BroadBandTestConstants.SINGLE_SPACE_CHARACTER);
	} else {
	    LOGGER.info("No command is available");
	}
	commandBuffer.append(ipAddress);
	response = tapEnv.executeCommandUsingSsh(device, commandBuffer.toString());
	status = !(response.contains(BroadBandTestConstants.TEXT_REFUSED)
		|| response.contains(BroadBandTestConstants.TEXT_NO_AUTH_METHODS)
		|| CommonUtils.isGivenStringAvailableInCommandOutput(response,
			BroadBandTestConstants.TELNET_CMD_NOT_FOUND)
		|| CommonUtils.isGivenStringAvailableInCommandOutput(response,
			BroadBandTestConstants.TELNET_NOT_FOUND));
	LOGGER.info("Exiting into checkConnectionToIpAddress");
	return status;
    }

    /**
     * Method to Validate telnet connection to ipv4 or ipv6. Collect the ip address from the ifconfig output, and telnet
     * to ip.
     *
     * @param tapEnv
     *            {@link AutomaticsTapApi} Reference
     * @param device
     *            device
     * @param interfaceName
     *            interfacename for ifconfig.
     *
     * @author Arunkumar
     * @refactor yamini.s
     */
    public static boolean verifyTelnetConnectionToInterface(AutomaticsTapApi tapEnv, Dut device, String interfaceName) {
	LOGGER.info("Entering into verifyTelnetConnectionToInterface");
	boolean status = false;
	boolean ipv4Status = true;
	boolean ipv6Status = true;
	StringBuffer commandBuffer = new StringBuffer();
	commandBuffer.append(BroadBandTestConstants.COMMAND_TO_GET_IP_CONFIGURATION_DETAILS);
	commandBuffer.append(BroadBandTestConstants.SINGLE_SPACE_CHARACTER);
	commandBuffer.append(interfaceName);
	String ifConfigOutput = tapEnv.executeCommandUsingSsh(device, commandBuffer.toString());
	if (CommonMethods.isNotNull(ifConfigOutput)) {
	    LOGGER.info("Collect the IP address from ifconfig output");
	    String ipv4Address = CommonMethods.patternFinder(ifConfigOutput,
		    BroadBandTestConstants.INET_V4_ADDRESS_PATTERN);
	    String ipv6Address = CommonMethods.patternFinder(ifConfigOutput,
		    BroadBandTestConstants.INET_V6_ADDRESS_PATTERN);
	    if (CommonMethods.isNull(ipv6Address) && CommonMethods.isNull(ipv4Address)) {
		LOGGER.info("Ipv4 & Ipv6 addresses are not available");
	    } else {
		if (CommonMethods.isNotNull(ipv4Address)) {
		    ipv4Status = checkConnectionToIpAddress(tapEnv, device, BroadBandCommandConstants.CMD_TELNET,
			    ipv4Address);
		} else {
		    LOGGER.info("Ipv4 address is not available for the interface: " + interfaceName);
		}
		if (CommonMethods.isNotNull(ipv6Address)) {
		    ipv6Status = checkConnectionToIpAddress(tapEnv, device, BroadBandCommandConstants.CMD_TELNET,
			    ipv6Address);
		} else {
		    LOGGER.info("IPv6 address is not available for the interface " + interfaceName);
		}
		if (ipv4Status && ipv6Status) {
		    status = true;
		}
	    }
	    LOGGER.info("Exiting from verifyTelnetConnectionToInterface");
	}
	return status;
    }

    /**
     * Utility method to verify the log messages on given log file from Arm Console for a given polling time .
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi} Reference
     * @param device
     *            {@link device} to be validated
     * @param searchText
     *            String representing the text to be searched in the log file.
     * @param logFileName
     *            String representing the URL which needs to be present in the searched text.
     * @param deviceDateTime
     *            Timestamp from Device * *
     * @param pollDuration
     *            polling frequency time
     * @return Boolean representing the result of verification of Log file.
     */

    public static boolean verifyArmConsoleLogForPollingTime(AutomaticsTapApi tapEnv, Dut device, String searchText,
	    String logFileName, String deviceDateTime, long pollDuration) {
	LOGGER.debug("ENTERING METHOD verifyArmConsoleLogForPollingTime");
	// stores the test status
	boolean result = false;
	long startTime = System.currentTimeMillis();
	do {
	    LOGGER.info("verifying the arm console logs for a polling Duration");
	    StringBuffer command = new StringBuffer();
	    command.append(BroadBandTestConstants.GREP_COMMAND);
	    command.append(searchText);
	    command.append(BroadBandTestConstants.SINGLE_SPACE_CHARACTER);
	    command.append(logFileName);
	    command.append(BroadBandTestConstants.SYMBOL_PIPE);
	    command.append(BroadBandTestConstants.CMD_TAIL_1);
	    String response = tapEnv.executeCommandUsingSsh(device, command.toString());
	    LOGGER.info("FROM ARM CONSOLE RESPONSE :" + response);
	    if (CommonMethods.isNotNull(response) && response.contains(searchText.replace("\"", ""))) {
		result = BroadBandCommonUtils.verifyLogUsingTimeStamp(deviceDateTime, response);
	    }

	    if (result) {
		break;
	    }
	    LOGGER.info("GOING TO WAIT FOR 1 MINUTES.");
	    tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);

	} while ((System.currentTimeMillis() - startTime) < pollDuration);
	LOGGER.info("VERIFYING ARM CONSOLE LOGS" + result);
	LOGGER.debug("ENDING METHOD verifyArmConsoleLogForPollingTime");
	return result;

    }

    /**
     * Method to simulate process crash and verify process restarted with new pid within 15 mins of crash
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param processName
     *            String containing name of process to be restarted
     * 
     * @return true if process restarted successfully after crash
     * 
     * @author Ashwin sankara
     * @refactor Govardhan
     * 
     */
    public static boolean killAndVerifyProcessRestarted(Dut device, AutomaticsTapApi tapEnv, String processName) {

	LOGGER.debug("Entering method : killAndVerifyProcessRestarted");

	boolean isAtom = false;
	String processId = null;
	String newPid = null;
	long startTime = BroadBandTestConstants.CONSTANT_0;
	boolean status = false;

	processId = CommonMethods.getPidOfProcess(device, tapEnv, processName);
	if (CommonMethods.isNull(processId) && CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
	    // Checking if pid can be obtained from Atom for atom devices
	    processId = BroadBandCommonUtils.getPidOfProcessFromAtomConsole(device, tapEnv, processName);
	    isAtom = CommonMethods.isNotNull(processId);
	}
	if (CommonMethods.isNull(processId)) {
	    LOGGER.error("No process id obtained for the process: " + processName);
	    return false;
	}
	LOGGER.info(
		isAtom ? "Obtained process id from atom console: " + processId : "Obtained process id: " + processId);

	// Restarting process on Atom if process id found only in atom
	if (isAtom) {
	    tapEnv.executeCommandOnAtom(device, BroadBandCommonUtils
		    .concatStringUsingStringBuffer(ProcessRestartOption.KILL_11.getCommand(), processId));
	    startTime = System.currentTimeMillis();
	    do {
		LOGGER.info("Waiting for one minute");
		tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
		newPid = BroadBandCommonUtils.getPidOfProcessFromAtomConsole(device, tapEnv, processName);
		if (CommonMethods.isNotNull(newPid)) {
		    break;
		}
	    } while (System.currentTimeMillis() - startTime < BroadBandTestConstants.TWENTY_MINUTES_IN_MILLIS);
	} else {
	    tapEnv.executeCommandUsingSsh(device, BroadBandCommonUtils
		    .concatStringUsingStringBuffer(ProcessRestartOption.KILL_11.getCommand(), processId));
	    startTime = System.currentTimeMillis();
	    do {
		LOGGER.info("Waiting for one minute");
		tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
		newPid = CommonMethods.getPidOfProcess(device, tapEnv, processName);
		if (CommonMethods.isNotNull(newPid)) {
		    break;
		}
	    } while (System.currentTimeMillis() - startTime < BroadBandTestConstants.TWENTY_MINUTES_IN_MILLIS);
	}

	// Verifying new pid obtained is not null and different from original pid
	if (CommonMethods.isNull(newPid)) {
	    LOGGER.error("Pid not obtained after 20 minutes of crash for process: " + processName);
	} else if (newPid.equals(processName)) {
	    LOGGER.error("Process restart with kill -11 failed for process: " + processName);
	} else {
	    status = true;
	}

	LOGGER.debug("Exiting method : killAndVerifyProcessRestarted");
	return status;
    }

    /**
     * Helper method to search system ready signal in webpa log file either in ARM or ATOM console
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param isAtomSyncDevice
     *            is AtomSync device
     * @return status
     * 
     */
    public static boolean searchSystemReadySignalLogInArmOrAtom(Dut device, AutomaticsTapApi tapEnv,
	    boolean isAtomSyncDevice) {
	LOGGER.debug("STARTING METHOD searchSystemReadySignalLogInArmOrAtom");
	String response = null;
	boolean status = false;
	boolean isCRSystemReady = false;
	try {
	    response = BroadBandCommonUtils.searchLogFilesInAtomOrArmConsoleByPolling(device, tapEnv,
		    BroadBandTraceConstants.LOG_MESSAGE_CR_SYSTEM_READY, BroadBandCommandConstants.LOG_FILE_CR,
		    BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS, BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS);
	    isCRSystemReady = CommonMethods.isNotNull(response) && CommonUtils.patternSearchFromTargetString(response,
		    BroadBandTraceConstants.LOG_MESSAGE_CR_SYSTEM_READY);
	    if (isCRSystemReady) {
		response = BroadBandCommonUtils.searchLogFilesInAtomOrArmConsoleByPolling(device, tapEnv,
			BroadBandTraceConstants.LOG_MESSAGE_RECIEVED, BroadBandCommandConstants.LOG_FILE_WEBPA,
			BroadBandTestConstants.THREE_MINUTE_IN_MILLIS, BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS);
		status = CommonMethods.isNotNull(response) && CommonUtils.patternSearchFromTargetString(response,
			BroadBandTraceConstants.LOG_MESSAGE_RECIEVED);
		if (!status) {
		    response = BroadBandCommonUtils.searchLogFilesInAtomOrArmConsoleByPolling(device, tapEnv,
			    BroadBandTraceConstants.LOG_MESSAGE_CHECKED, BroadBandCommandConstants.LOG_FILE_WEBPA,
			    BroadBandTestConstants.TEN_MINUTE_IN_MILLIS,
			    BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		    status = CommonMethods.isNotNull(response) && CommonUtils.patternSearchFromTargetString(response,
			    BroadBandTraceConstants.LOG_MESSAGE_CHECKED);
		} else {
		    LOGGER.info("Received system ready signal is present in the WEBPAlog.txt.0");
		}

	    } else {
		LOGGER.error("From CR: System is ready signal message is not present in the CRlog.txt.0");
	    }
	} catch (Exception e) {
	    LOGGER.error("Exeception had occured while searching for system ready signal log in Arm or Atom");
	    LOGGER.error(e.getMessage());
	}
	LOGGER.debug("ENDING METHOD searchSystemReadySignalLogInArmOrAtom");
	return status;
    }

    /**
     * Method to Validate file availability in ATOM console
     *
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param fileName
     *            fileName used in find command
     *
     * @author ArunKumar Jayachandran
     * @refactor Govardhan
     */
    public static boolean verifyFileAvailabilityInAtomConsole(AutomaticsTapApi tapEnv, Dut device, String fileName) {
	LOGGER.info("Entering into verifyFileAvailabilityInAtomConsole");
	boolean status = false;
	String response = null;
	try {
	    String searchCommand = BroadBandCommonUtils.concatStringUsingStringBuffer(
		    BroadBandCommandConstants.CMD_FIND_INAME, BroadBandTestConstants.SINGLE_SPACE_CHARACTER, fileName);
	    response = BroadBandCommonUtils.executeCommandInAtomConsole(device, tapEnv, searchCommand);
	    LOGGER.info("AtomConsole Response for find command: " + response);
	    if (CommonMethods.isNotNull(response)) {
		status = !CommonUtils.patternSearchFromTargetString(response, fileName)
			&& !CommonUtils.patternSearchFromTargetString(response, BroadBandTestConstants.NO_ROUTE_TO_HOST)
			&& !CommonUtils.patternSearchFromTargetString(response,
				BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_CONNECTION_TIMEOUT_MESSAGE)
			&& !CommonUtils.patternSearchFromTargetString(response,
				BroadBandTestConstants.STRING_CONNECTION_REFUSED)
			&& !CommonUtils.patternSearchFromTargetString(response,
				BroadBandTestConstants.AUTHENTICATION_FAILED)
			&& !CommonUtils.patternSearchFromTargetString(response,
				BroadBandTestConstants.PATTERN_MATCHER_FIND_NO_SUCH_FILE);
	    } else {
		LOGGER.error("Response is empty & file is not available in Atom Console.");
	    }
	} catch (Exception exception) {
	    LOGGER.error(
		    "Exception Occurred while verifying File availability in atom console:" + exception.getMessage());

	}
	LOGGER.info("File Availability In Atom Console is : " + response);
	LOGGER.info("Exiting into verifyFileAvailabilityInAtomConsole");
	return status;
    }
    
    /**
     * Utility method to verify the webpa log messages on WEBPALog.txt.0 log file from Arm Console/Atom console for a
     * polling time .
     * 
     * @param device
     *            {@link Dut}
     * @param tapApi
     *            {@link AutomaticsTapApi}
     * @param webpaNotification
     *            String representing the text to be searched in the webpa log file.
     * @param deviceDateTime
     *            Timestamp from Device * *
     * @return Boolean representing the result of verification of Log file.
     * @Refactor Sruthi Santhosh
     */

    public static boolean verifyWebpaNotificationForPollingTime(AutomaticsTapApi tapEnv, Dut device,
	    String webpaNotification, String deviceDateTime) {
	LOGGER.debug("ENTERING METHOD verifyWebpaNotificationForPollingTime");
	// stores the test status
	boolean status = false;
	// stores the response
	String response = null;
	// stores the response
	String paradusResponse = null;
	long pollDuration = BroadBandTestConstants.THREE_MINUTE_IN_MILLIS;
	long startTime = System.currentTimeMillis();
	do {
	    if (CommonMethods.isAtomSyncAvailable(device, tapEnv)){
		status = BroadBandSystemUtils.verifyAtomConsoleLog(tapEnv, device, webpaNotification,
			BroadBandTestConstants.RDKLOGS_LOGS_WEBPA_TXT_0, deviceDateTime)
			&& BroadBandSystemUtils.verifyAtomConsoleLog(tapEnv, device,
				BroadBandTraceConstants.LOG_MESSAGE_SUCCCESS_TO_PARADOUS,
				BroadBandTestConstants.RDKLOGS_LOGS_WEBPA_TXT_0, deviceDateTime);
	    } else {
		LOGGER.info("Inside other devices");
		response = LoggerUtils.getLatestLogMessageBasedOnTimeStamp(tapEnv, device, webpaNotification,
			BroadBandTestConstants.RDKLOGS_LOGS_WEBPA_TXT_0, deviceDateTime, false);
		paradusResponse = LoggerUtils.getLatestLogMessageBasedOnTimeStamp(tapEnv, device,
			BroadBandTraceConstants.LOG_MESSAGE_SUCCCESS_TO_PARADOUS,
			BroadBandTestConstants.RDKLOGS_LOGS_WEBPA_TXT_0, deviceDateTime, false);
		status = CommonMethods.isNotNull(response) && CommonMethods.isNotNull(paradusResponse);
	    }
	    if (status) {
		break;
	    }
	} while ((System.currentTimeMillis() - startTime) < pollDuration);
	LOGGER.debug("ENDING METHOD verifyWebpaNotificationForPollingTime");
	return status;

    }
    
    /**
     * Utility method to verify the log messages on given log file from atom Console .
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param searchText
     *            String representing the text to be searched in the log file.
     * @param logFileName
     *            String representing the URL which needs to be present in the searched text.
     * @param deviceDateTime
     *            Timestamp from Device *
     * @return Boolean representing the result of verification of Log file.
     * @refactor Govardhan
     */

    public static boolean verifyAtomConsoleLog(AutomaticsTapApi tapEnv, Dut device, String searchText, String logFileName,
	    String deviceDateTime) {
	LOGGER.info("into verifyAtomConsoleLog");
	LOGGER.info("deviceDateTime:" + deviceDateTime);
	LOGGER.info("searchText is:" + searchText);
	LOGGER.info("ENTERING METHOD verifyAtomConsoleLog");
	boolean result = false;
	String command = BroadBandTestConstants.GREP_COMMAND + searchText
		+ BroadBandTestConstants.SINGLE_SPACE_CHARACTER + logFileName + BroadBandTestConstants.SYMBOL_PIPE
		+ BroadBandTestConstants.CMD_TAIL_1;
	LOGGER.info("command is:" + command);
	String response = CommonMethods.executeCommandInAtomConsole(device, tapEnv, command);
	LOGGER.info("FROM ATOM CONSOLE RESPONSE :" + response);
	if (CommonMethods.isNotNull(response) && response.contains(searchText.replace("\"", ""))) {
	    response = CommonMethods.patternFinder(response,
		    BroadBandTestConstants.PATTERN_MATCHER_ATOM_CONSOLE_LOG_SEARCH_RESPONSE);
	    result = BroadBandCommonUtils.verifyLogUsingTimeStamp(deviceDateTime, response);
	}
	LOGGER.info("ENDING METHOD verifyAtomConsoleLog");
	return result;
    }

    /**
     * Utils method to get the random number between range
     * 
     * @param min
     *            integer representing the minimum number.
     * @param max
     *            integer representing the maximum number.
     * 
     * @return the random number between the range
     */

    public static int getRandomNumberBetweenRange(int min, int max) {
	if (min >= max) {
	    throw new IllegalArgumentException("max must be greater than min");
	}
	Random random = new Random();
	return random.nextInt((max - min) + 1) + min;
    }
    
    /**
     * Helper method to verify the dnsmasq service status using systemctl
     * 
     * @param device
     *            {@link Dut}
     * @param tapApi
     *            {@link AutomaticsTapApi}
     * @param serviceStatus
     *            {@link DNSMASQ_SERVICE_STATUS}
     * 
     * @return true if status is verified successfully else false
     * @throws TestException
     */
    public static boolean verifyDnsmasqSerivceStatusUsingSystemctl(Dut device, AutomaticsTapApi tapApi,
	    String serviceStatus) throws TestException {
	// stores the verification status
	boolean status = false;
	// stores the command response
	String response = null;
	String NON_ASCII_CHARACTERS_FROM_REMOTE_API_RESPONSE = "\\[1;32m";

	try {

	    response = tapApi.executeCommandUsingSsh(device,
			    BroadBandCommandConstants.COMMAND_TO_CHECK_THE_RUNNING_STATUS_OF_DNSMASQ_SERVICE);

	    LOGGER.info("DNSMASQ service status response : " + response);

	    if (CommonMethods.isNotNull(response)) {

		response = response.replaceAll(NON_ASCII_CHARACTERS_FROM_REMOTE_API_RESPONSE,
			AutomaticsConstants.EMPTY_STRING);
		// retrieving the status from systemctl response
		response = CommonMethods.patternFinder(response,
			BroadBandTestConstants.PATTERN_TO_GET_STATUS_FROM_SYSTEMCTL_RESPONSE);
		LOGGER.info("systemctl dnsmasq service status is: " + response);
		if (CommonMethods.isNotNull(response) && response.trim().equalsIgnoreCase(serviceStatus)) {
		    status = true;
		    LOGGER.info("systemctl command returned dnsmasq service is " + serviceStatus);
		} else {
		    throw new TestException("dnsmasq service is NOT " + serviceStatus
			    + " as observed with systemctl command. The current status is " + response);
		}
	    } else {
		throw new TestException("NULL response obtained after executing commands"
			+ BroadBandCommandConstants.COMMAND_TO_CHECK_THE_RUNNING_STATUS_OF_DNSMASQ_SERVICE
			+ " using remote ssh api in stb");
	    }
	} catch (Exception exception) {
	    throw new TestException(exception.getMessage());
	}
	return status;
    }
    
    /**
     * Helper method to verify the dnsmasq service status using systemctl
     * 
     * @param device
     *            {@link Dut}
     * @param tapApi
     *            {@link AutomaticsTapApi}
     * @param serviceStatus
     *            {@link DNSMASQ_SERVICE_STATUS}
     * 
     * @return true if status is verified successfully else false
     * @throws TestException
     */
    public static boolean verifyDnsmasqSerivceStatus(Dut device, AutomaticsTapApi tapApi,
	    String serviceStatus) throws TestException {
	// stores the verification status
	boolean status = false;
	// stores the command response
	String response = null;
	String NON_ASCII_CHARACTERS_FROM_REMOTE_API_RESPONSE = "\\[1;32m";

	try {


	    String cmmandForRunningStsDnsMasq = AutomaticsPropertyUtility
			    .getProperty(BroadBandTestConstants.PROP_KEY_CMDTOCHECK_RUNNING_STATUS_DNSMASQ_SERVICE);
	    
	    response = tapApi.executeCommandUsingSsh(device,
	    		cmmandForRunningStsDnsMasq);	    
	    LOGGER.info("DNSMASQ service status response : " + response);

	    if (CommonMethods.isNotNull(response)) {

		response = response.replaceAll(NON_ASCII_CHARACTERS_FROM_REMOTE_API_RESPONSE,
			AutomaticsConstants.EMPTY_STRING);
		
	    String patternTogetStatusFromSystmctlResponse = AutomaticsPropertyUtility
			    .getProperty(BroadBandTestConstants.PROP_KEY_PATTERN_TOCHECK_DNSMASQ_SERVICE);
		
		// retrieving the status from systemctl response
		response = CommonMethods.patternFinder(response,
				patternTogetStatusFromSystmctlResponse);
		LOGGER.info("systemctl dnsmasq service status is: " + response);
		if (CommonMethods.isNotNull(response) && response.trim().equalsIgnoreCase(serviceStatus)) {
		    status = true;
		    LOGGER.info("systemctl command returned dnsmasq service is " + serviceStatus);
		} else {
		    throw new TestException("dnsmasq service is NOT " + serviceStatus
			    + " as observed with systemctl command. The current status is " + response);
		}
	    } else {
		throw new TestException("NULL response obtained after executing commands"
			+ cmmandForRunningStsDnsMasq
			+ " using remote ssh api in stb");
	    }
	} catch (Exception exception) {
	    throw new TestException(exception.getMessage());
	}
	return status;
    }
    
    
    /**
     * helper method to verify that the three backup files, which are used for
     * encrypting /opt/secure folder, exist under /opt
     * 
     * 
     * @param device
     *            instance of {@link Dut}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi
     * 
     * @return true if all the backup files are found under /opt, else false
     * 
     * @author  Sumathi Gunasekaran
     * @refactor yamini.s
     */
    public static BroadBandResultObject verifyBackupFilesArePresentForEncryption(Dut device, AutomaticsTapApi tapEnv) {
	BroadBandResultObject result = new BroadBandResultObject();
	result.setStatus(false);
	result.setErrorMessage(null);
	String files = BroadbandPropertyFileHandler.getSecureMountBackUpFiles();
	String[] backup_files = files.split(",");
	
	try {
	    LOGGER.debug("STARTING METHOD : verifyBackupFilesArePresentForEncryption() ");
	    for (int loopCounter = BroadBandTestConstants.CONSTANT_0; loopCounter < backup_files.length; loopCounter++) {
		try {
		    LOGGER.info("Checking for the presence of file : " + backup_files[loopCounter]);
		    result.setStatus(CommonUtils.isFileExists(device, tapEnv,
			    CommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.MOUNT_NVRAM,
			    		backup_files[loopCounter])));
		    LOGGER.info("The file " + backup_files[loopCounter] + " is present under /opt");

		} catch (Exception e) {
		    result.setStatus(false);
		    result.setErrorMessage("The file /opt/" + backup_files[loopCounter]
			    + " is not present under /opt");
		}
	    }
	} catch (Exception e) {
	    result.setStatus(false);
	    result.setErrorMessage(
		    "Exception occured inside method:: verifyBackupFilesArePresentForEncryption. " + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : verifyBackupFilesArePresentForEncryption() ");
	return result;
    }
    
    /**
     * helper method to remove all the three backup files, which are used for
     * encrypting /opt/secure folder, from /opt
     * 
     * @param device
     *            instance of {@link Dut}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi
     * 
     * @return true if all the backup files are removed successfully, else false
     * 
     * @author Sumathi Gunasekaran
     * @refactor yamini.s
     */
    public static BroadBandResultObject removeBackupFilesUsedForEncryption(Dut device, AutomaticsTapApi tapEnv) {
	BroadBandResultObject result = new BroadBandResultObject();
	result.setStatus(false);
	result.setErrorMessage(null);
	String files = BroadbandPropertyFileHandler.getSecureMountBackUpFiles();
	String[] backup_files = files.split(",");
	try {
	    LOGGER.debug("STARTING METHOD : removeBackupFilesUsedForEncryption() ");
	    for (int loopCounter = BroadBandTestConstants.CONSTANT_0; loopCounter < backup_files.length; loopCounter++) {
		try {
		    LOGGER.info("Removing the backup file :: " + backup_files[loopCounter]);
		    result.setStatus(CommonUtils.removeFileandVerifyFileRemoval(tapEnv, device,
			    CommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.MOUNT_NVRAM,
			    		backup_files[loopCounter])));

		} catch (Exception e) {
		    result.setStatus(false);
		    result.setErrorMessage("Failed to remove the file /opt/" + backup_files[loopCounter]);
		}
	    }
	} catch (Exception e) {
	    result.setErrorMessage("Exception occured inside method:: removeBackupFilesUsedForEncryption. " + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : removeBackupFilesUsedForEncryption() ");
	return result;
    }

    /**
     * Utility method to verify the log messages on given log file from Arm Console .
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi} Reference
     * @param device
     *            {@link Dut} to be validated
     * @param deviceDateTime
     *            Timestamp from Device
     * @return Boolean representing the result of verification of Log file.
     * @Refactor Sruthi Santhosh
     */
    public static boolean verifyArmConsoleLog(AutomaticsTapApi tapEnv, Dut device, String searchText,
	    String logFileName, String deviceDateTime) {
	LOGGER.debug("ENTERING METHOD verifyArmConsoleLogForPollingTime");
	// stores the test status
	boolean result = false;
	LOGGER.info("verifying the arm console logs for a polling Duration");
	StringBuffer command = new StringBuffer();
	command.append(BroadBandTestConstants.GREP_COMMAND);
	command.append(searchText);
	command.append(BroadBandTestConstants.SINGLE_SPACE_CHARACTER);
	command.append(logFileName);
	command.append(BroadBandTestConstants.SYMBOL_PIPE);
	command.append(BroadBandTestConstants.CMD_TAIL_1);
	String response = tapEnv.executeCommandUsingSsh(device, command.toString());
	LOGGER.info("FROM ARM CONSOLE RESPONSE :" + response);
	if (CommonMethods.isNotNull(response) && response.contains(searchText.replace("\"", ""))) {
	    result = BroadBandCommonUtils.verifyLogUsingTimeStamp(deviceDateTime, response);
	   	}

	LOGGER.info("VERIFYING ARM CONSOLE LOGS" + result);
	LOGGER.debug("ENDING METHOD verifyArmConsoleLog");
	return result;
    }
    
    /**
     * Utility method to rerieve ip address and domain name for the url from phishtank site
     * 
     * @param CclientDevice
     *            {@link Dut} to be validated
     * 
     * @param tapEnv
     * 			{@value AutomaticsTapApi}
     * 
     * @return String array
     * 
     * @author Sumathi Gunasekaran
     * @refactor Athira
     */
    public static String[] retrieveIPAddressAndDomainName(Dut clientDevice, AutomaticsTapApi tapEnv,
	    String phishTankUrl) {
	LOGGER.debug("STARTING METHOD: retrieveIPAddressAndDomainName");
	String commandToExecute = null;
	String response = null;
	String pingResponseArray[] = null;
	String nsLookupDomainName = null;
	String nsLookupIpAddress = null;
	String[] domainNameAndIpAddress = new String[BroadBandTestConstants.CONSTANT_2];
	LOGGER.info("URL Retrieved from Phishtank website is:" + phishTankUrl);
	URL domainName = null;
	try {
	    domainName = new java.net.URL(phishTankUrl);
	    if (null != domainName) {
		LOGGER.info("URL Host Name is:" + domainName.getHost());
		commandToExecute = BroadBandCommonUtils.concatStringUsingStringBuffer(
			BroadBandTestConstants.STRING_NS_LOOKUP, AutomaticsConstants.SINGLE_SPACE_CHARACTER,
			domainName.getHost());
		response = tapEnv.executeCommandOnOneIPClients(clientDevice, commandToExecute);
		if (CommonMethods.isNotNull(response)
			&& (CommonUtils.isGivenStringAvailableInCommandOutput(response,
				BroadBandTestConstants.STRING_NAME_COLON))
			&& (CommonUtils.isGivenStringAvailableInCommandOutput(response,
				BroadBandTestConstants.STRING_ADDRESS_COLON))) {
		    pingResponseArray = response.split(BroadBandTestConstants.STRING_ANSWER_COLON);
		    if (CommonMethods.isNotNull(pingResponseArray[BroadBandTestConstants.CONSTANT_1])) {
			nsLookupIpAddress = CommonMethods.patternFinder(
				pingResponseArray[BroadBandTestConstants.CONSTANT_1],
				BroadBandTestConstants.REGEX_STRING_ADDRESS_AND_IPV4_ADDRESS);
			nsLookupDomainName = CommonMethods.patternFinder(
				pingResponseArray[BroadBandTestConstants.CONSTANT_1],
				BroadBandTestConstants.REGEX_STRING_NAME_AND_HOST_NAME);
			LOGGER.info("IpAddress: " + nsLookupIpAddress + "\n DomainName: " + nsLookupDomainName);
			if (CommonMethods.isNotNull(nsLookupIpAddress) && CommonMethods.isNotNull(nsLookupDomainName)) {
			    domainNameAndIpAddress[BroadBandTestConstants.CONSTANT_0] = nsLookupIpAddress;
			    domainNameAndIpAddress[BroadBandTestConstants.CONSTANT_1] = nsLookupDomainName;
			}
		    }
		}
	    }
	} catch (MalformedURLException e) {
	    LOGGER.error("Exception found::::>>>>" + e.getMessage(), e);
	}
	LOGGER.debug("ENDING METHOD: retrieveIPAddressAndDomainName");
	return domainNameAndIpAddress;
    }
    
}

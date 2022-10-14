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
package com.automatics.rdkb.reversessh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Dut;
import com.automatics.error.GeneralError;
import com.automatics.exceptions.FailedTransitionException;
import com.automatics.providers.connection.SshConnection;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandPropertyKeyConstants;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.rdkb.utils.ServerUtils;
import com.automatics.rdkb.utils.cdl.FirmwareDownloadUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.CommonMethods;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.utils.CommonUtils;
import com.automatics.device.Device;

public class BroadBandReverseSshUtils {
	
    /**
     * Logger instance for this particular class.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(BroadBandReverseSshUtils.class);
    
    /**
     * Establishes the reverse ssh connection
     *
     * @param device,
     *            instance of Dut object
     * @return boolean, true if device successfully establishes reverse ssh connection.
     * 
     * @refactor Athira
     */
    public static boolean establishReverseSshConnectionInBroadBandDevice(Dut device) {
	boolean status = false;
	LOGGER.debug("Entering method establishReverseSshConnectionInBroadBandDevice");
	String response = null;
	SshConnection sshConnection = null;
	try {

		String reverseSshJumpServer = BroadbandPropertyFileHandler.getReverseSshJumpServer();
	    String reverseSshCommand = BroadBandCommandConstants.CMD_SUDO_REVERSE_SSH_RDKB;
	    if (DeviceModeHandler.isDSLDevice(device)) {
		reverseSshCommand = BroadBandCommandConstants.CMD_REVERSE_SSH;
	    } else {

		reverseSshCommand = DeviceModeHandler.isFibreDevice(device)
			? AutomaticsPropertyUtility
					.getProperty(BroadBandTestConstants.PROP_KEY_CMD_REVERSE_SSH_FIBRE)
			: AutomaticsPropertyUtility
			.getProperty(BroadBandTestConstants.PROP_KEY_CMD_SUDO_REVERSE_SSH_RDKB);
	    }
	    LOGGER.info("REVERSE SSH COMMAND: " + reverseSshCommand);
	    if (CommonMethods.isNotNull(reverseSshJumpServer)) {
		// Reverse ssh requires ecm mac address instead of IP
		String command = BroadBandCommonUtils.concatStringUsingStringBuffer(reverseSshCommand,
			BroadBandTestConstants.SINGLE_SPACE_CHARACTER, ((Device) device).getEcmMac(),
			BroadBandTestConstants.CHAR_NEW_LINE);
		// api should use only reverseSshJumpServer it should not use any
		// random jump server on failing to establish a connection

		sshConnection = ServerUtils.getSshConnection(reverseSshJumpServer);
		LOGGER.info("establish reverse ssh connection by executing command -" + command);
		sshConnection.send(command, (int) (int) AutomaticsConstants.ONE_MINUTE);
		LOGGER.debug("Trying to get response");
		response = sshConnection.getSettopResponse(AutomaticsConstants.ONE_MINUTE);
		LOGGER.debug("Trying to get response over");
		LOGGER.info("Response from device using reverse ssh connection is " + response);
		status = CommonMethods.isNotNull(response)
			&& !response.contains(BroadBandTestConstants.MESSAGE_UNABLE_TO_START_SSH)
			&& !CommonUtils.patternSearchFromTargetString(response,
				BroadBandTestConstants.STRING_SUDO_PASSWORD)
			&& !response.contains("HTTP error message while setting arguments")
			&& !response.contains("status code 520") && !response.contains("All ports are in use");
		
		if (status) {
		    int indexEndOfPrivacyMessage = response
			    .lastIndexOf(BroadBandTestConstants.END_OF_SSH_CONNECTION_PRIVACY_MESSAGE);
		    LOGGER.debug("Index of  occuarance of the warning message" + indexEndOfPrivacyMessage);
		    if (indexEndOfPrivacyMessage > 0) {
			response = response.substring(indexEndOfPrivacyMessage
				+ BroadBandTestConstants.END_OF_SSH_CONNECTION_PRIVACY_MESSAGE.length());
		    }
		    response = response.trim();
		    status = !response.contains(FirmwareDownloadUtils.SSH_CONNECTION_ERROR_CONNECTION_REFUSED)
			    && !response.contains(BroadBandTestConstants.STRING_FAILED_TO_GET_IP);
		}
	    } else {
		LOGGER.error("Failed to verify reverse ssh connection");
	    }
	} catch (Exception exc) {
	    throw new FailedTransitionException(GeneralError.SSH_CONNECTION_FAILURE, exc);
	} finally {
	    if (null != sshConnection) {
		ServerUtils.closeSshConnection(sshConnection);
	    }
	}
	LOGGER.info("Is reverse ssh connection established - " + status);
	LOGGER.debug("Exit method establishReverseSshConnectionInBroadBandDevice");
	return status;
    }
    
    /**
     * Utility to method to search for the given text in the given log file based on Poll Interval & Poll Duration using
     * Reverse SSH; returns String representing the search response.
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
     * 
     * @author Dipankar Nalui
     * @refactor Said hisham
     */
    public static String searchLogFilesWithPollingIntervalUsingReverseSsh(AutomaticsTapApi tapEnv, Dut device,
	    String searchText, String logFile, long pollDuration, long pollInterval) {
	LOGGER.debug("STARTING METHOD searchLogFilesWithPollingIntervalUsingReverseSsh");
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
	String [] command =  new String [] {sbCommand.toString()};
	LOGGER.info("COMMAND TO BE EXECUTED: " + command);
	long startTime = System.currentTimeMillis();
	String searchResponse = null;
	do {
	    tapEnv.waitTill(pollInterval);
	    searchResponse = tapEnv.executeCommandUsingReverseSsh(device, command);
	    LOGGER.info("searchResponse " + searchResponse);
	    searchResponse = CommonMethods.isNotNull(searchResponse)
		    && !searchResponse.contains(BroadBandTestConstants.NO_SUCH_FILE_OR_DIRECTORY) ? searchResponse.trim()
			    : null;
	} while ((System.currentTimeMillis() - startTime) < pollDuration && CommonMethods.isNull(searchResponse));
	LOGGER.info(
		"SEARCH RESPONSE FOR - " + searchText + " IN THE LOG FILE - " + logFile + " IS : " + searchResponse);
	LOGGER.debug("ENDING METHOD searchLogFilesWithPollingIntervalUsingReverseSsh");
	return searchResponse;
    }
    
    /**
     * Method to reboot the device with out wait and returns the verification status using reverse SSH
     * 
     * @param device
     *            The device instance to be checked
     * 
     *            Returns true if its able to reboot the device
     * @author Dipankar Nalui
     * @refactor Said Hisham           
     *            
     */

    public boolean rebootWithoutWaitAndGetTheRebootStatusUsingReverseSsh(AutomaticsTapApi tapEnv, Dut device) {

	boolean rebootStatus = false;
	String [] command = new String[] {BroadBandTestConstants.CMD_REBOOT};
	LOGGER.info("Going to reboot.");
	try {
	    tapEnv.executeCommandUsingReverseSsh(device, command);
	    if (CommonMethods.isSTBAccessible(device)) {
		LOGGER.info("Going to reboot via command second time.");
		tapEnv.executeCommandUsingReverseSsh(device, command);
	    } else {
		rebootStatus = true;
	    }

	    if (CommonMethods.isSTBAccessible(device)) {
		try {
		    LOGGER.info("Going to reboot via power code as command failed twice.");
		    device.getPower().reboot();
		    tapEnv.waitTill(BroadBandTestConstants.TWO_SECONDS);		    
		} catch (Exception e) {
		    LOGGER.info("Failed to reboot via all methods exiting");
		} finally {
		    rebootStatus = !CommonMethods.isSTBAccessible(device);
		}
	    } else {
		AutomaticsTapApi.incrementKnownRebootCounter(device.getHostMacAddress());
		rebootStatus = true;
	    }
	} catch (Exception e) {
	    LOGGER.error("Failed to reboot the box using the command. Rebooting using 'reboot' power switch");	   
	} 
	LOGGER.info("Final Reboot Status." + rebootStatus);
	return rebootStatus;
    }

}

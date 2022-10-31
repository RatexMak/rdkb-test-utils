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

import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Dut;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.utils.dmcli.DmcliUtils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;

/**
 * Utility class which handles the Parodus related validations.
 * 
 * @author BALAJI V
 * @refactor Govardhan
 */
public class ParodusUtils {

    /** SLF4J logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ParodusUtils.class);

    /**
     * Utility method to compare the Device Manufacturer name
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param deviceManufacturerNameWebPA
     *            Device manufacturer name retrieved using WebPa
     * 
     * @return true if the Device manufacturer names retrieved using WebPA and in Parodus log file are same
     * @refactor Govardhan
     */
    public static boolean compareDeviceManufacturerName(AutomaticsTapApi tapEnv, Dut device,
	    String deviceManufacturerName) {
	LOGGER.info("ENTERING METHOD compareDeviceManufacturerName");
	boolean result = true;
	String verifyParodusLogMessage = tapEnv.executeCommandUsingSsh(device,
		BroadBandTestConstants.COMMAND_TO_RETRIEVE_MANUFACTURER_NAME);
	LOGGER.info("The Retrieved Device Manufacturer name: " + verifyParodusLogMessage);
	String deviceManufacturerNameInParodusLog = CommonMethods.isNotNull(verifyParodusLogMessage)
		? CommonMethods.patternFinder(verifyParodusLogMessage,
			BroadBandTestConstants.PATTERN_MATCHER_MANUFACTURER_NAME)
		: null;
	result = CommonMethods.isNotNull(deviceManufacturerNameInParodusLog)
		&& (deviceManufacturerName.trim().equals(deviceManufacturerNameInParodusLog.trim()));
	if (result) {
	    LOGGER.info("The Retrieved Device Manufacturer name: " + verifyParodusLogMessage
		    + ", is same as expected Manufacturer name : " + deviceManufacturerName);
	} else {
	    LOGGER.error("The Retrieved Device Manufacturer name: " + verifyParodusLogMessage
		    + ", is different from expected Manufacturer name : " + deviceManufacturerName);
	}
	LOGGER.info("ENDING METHOD compareDeviceManufacturerName");
	return result;
    }

    /**
     * Method to verify parodus reconnect messages having jitter algorithm
     * 
     * @param response
     *            String containing jitter retry messages
     * 
     * @return true if jitter algorithm verified
     * @refactor Govardhan
     */
    public static BroadBandResultObject verifyParodusReconnectJitter(String response) {

	LOGGER.debug("STARTING METHOD: verifyParodusReconnectJitter");
	BroadBandResultObject result = new BroadBandResultObject();
	result.setStatus(true);
	boolean status = false;
	int maxDelay = BroadBandTestConstants.CONSTANT_0;
	int retryTime = BroadBandTestConstants.CONSTANT_0;
	int expDelay = BroadBandTestConstants.CONSTANT_0;
	String value = null;
	ListIterator<Integer> iterator = BroadBandTestConstants.PARODUS_JITTER_INTERVALS
		.listIterator(BroadBandTestConstants.CONSTANT_0);
	// Separate different lines of retry timer log messages
	String[] messageList = response.split(BroadBandTestConstants.DELIMITER_NEW_LINE);
	for (String message : messageList) {
	    status = false;
	    if (iterator.hasNext()) {
		expDelay = iterator.next();
	    }
	    LOGGER.info("Expected max delay value: " + expDelay);
	    // Get max delay value from log message
	    value = CommonMethods.patternFinder(message, BroadBandTestConstants.PATTERN_TO_GET_MAX_DELAY);
	    if (CommonMethods.isNotNull(value)) {
		maxDelay = Integer.parseInt(value);
		LOGGER.info("Max delay value from log message: " + maxDelay);
		// Compare max delay values logged are as per expected values
		if (maxDelay == expDelay) {
		    // Get backoffRetrytime value from log message
		    value = CommonMethods.patternFinder(message,
			    BroadBandTestConstants.PATTERN_TO_GET_BACKOFFRETRYTIME);
		    if (CommonMethods.isNotNull(value)) {
			retryTime = Integer.parseInt(value);
			LOGGER.info("RetryTime value from log message: " + retryTime);
			// Verify backofretryTime value is within interval [3, maxValue]
			status = (retryTime >= BroadBandTestConstants.CONSTANT_3) && (retryTime <= maxDelay);
			if (status) {
			    LOGGER.info("RetryTime: " + retryTime + " is within range [3 , " + maxDelay + "]");
			} else {
			    LOGGER.error("RetryTime: " + retryTime + " is not within range [3 , " + maxDelay + "]");
			}
		    } else {
			LOGGER.error("BackoffRetryTime is not found in log message");
		    }
		} else {
		    LOGGER.error("Max delay limit not expected value: " + expDelay);
		}
	    } else {
		LOGGER.error("Max delay is not found in log message");
	    }
	    result.setStatus(result.isStatus() && status);
	}
	LOGGER.debug("ENDING METHOD: verifyParodusReconnectJitter");
	return result;
    }

    /**
     * Method to verify parodus command output and dmcli response
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi} Reference
     * @param device
     *            Dut
     * @param dmcliCommand
     *            webpa parameter
     *
     * @author Sumathi Gunasekaran
     * @Refactor Athira
     */

    public static boolean verifyParodusAndDmcliCommandResponse(AutomaticsTapApi tapEnv, Dut device,
	    String dmcliCommandInput) {
	LOGGER.debug("Starting Method : verifyParodusAndDmcliCommandResponse");
	String commandOutput = null;
	Boolean status = false;
	String response = null;
	commandOutput = tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CAT_FILE_PARODUSCMD);
	LOGGER.info("Command output: " + commandOutput);
	if (CommonMethods.isNotNull(commandOutput)) {
	    response = DmcliUtils.getParameterValueUsingDmcliCommand(device, tapEnv, dmcliCommandInput);
	    LOGGER.info("Value retrieved through Dmcli command for:" + dmcliCommandInput + "is:" + response);
	    status = CommonUtils.isGivenStringAvailableInCommandOutput(commandOutput, response);
	}
	LOGGER.debug("Ending Method : verifyParodusAndDmcliCommandResponse");
	return status;
    }

    /**
     * Helper method to get Parodus boot time from cat /tmp/parodusCmd.cmd
     * 
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param device
     *            Dut instance
     * @return boot time value from cat /tmp/parodusCmd.cmd
     * 
     * @author Praveenkumar Paneerselvam
     */
    public static String getParodusBootTime(AutomaticsTapApi tapEnv, Dut device) {
	LOGGER.debug("STARTING Method : getParodusBootTime");
	String bootTime = tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CAT_FILE_PARODUSCMD);
	LOGGER.info("ParodudCMD response is - " + bootTime);
	bootTime = CommonMethods.isNotNull(bootTime)
		? CommonMethods.patternFinder(bootTime, BroadBandTestConstants.STRING_REGEX_PARODUS_BOOT_TIME)
		: null;
	LOGGER.info("Boot Time value is - " + bootTime);
	LOGGER.debug("Ending Method : getParodusBootTime");
	return bootTime;
    }

    /**
     * Helper method to verify Parodus Boot time with expected Value
     * 
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param device
     *            Dut instance
     * @param expectedBootTime
     *            Expected boot time in string
     * @return true if both boot time are same.
     * 
     * @author Praveenkumar Paneerselvam
     */
    public static BroadBandResultObject verifyParodusBootTimeIsValid(AutomaticsTapApi tapEnv, Dut device,
	    String bootTime) {
	LOGGER.debug("Starting Method : verifyParodusBootTime");
	boolean status = false;
	BroadBandResultObject result = new BroadBandResultObject();
	String errorMessage = null;
	try {
	    Long bootValue = Long.parseLong(bootTime);
	    LOGGER.info("Boot Time from log is - " + bootValue);
	    String upTimeInSec = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_UPTIME);
	    long stbTime = DateUtils.getEpochTimeInMilliSecond(tapEnv, device);
	    LOGGER.info("STB TIME is " + stbTime);
	    errorMessage = "boot time is not with in the range 0-4294967295l ";
	    if (bootValue >= 0 && bootValue <= 4294967295l && CommonMethods.isNotNull(upTimeInSec)) {
		errorMessage = "parodus boot time is before the device start up time";
		long startTime = stbTime / 1000 - Long.parseLong(upTimeInSec.trim()) - 30;
		status = startTime <= bootValue;
	    }
	} catch (Exception exception) {
	    errorMessage = "Exception occured while verifying Parodus Boot time. Error Message - "
		    + exception.getMessage();
	    LOGGER.info(errorMessage);
	}
	result.setStatus(status);
	result.setErrorMessage(errorMessage);
	LOGGER.debug("Ending Method : verifyParodusBootTime");
	return result;
    }

    /**
     * Helper method to verify boot time retry wait in cat /tmp/parodusCmd.cmd
     * 
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param device
     *            Dut instance
     * @return true, if -boot-time-retry-wait= is present
     * 
     * @author Praveenkumar Paneerselvam
     */
    public static boolean verifyParodusBootRetryCountIsPresent(AutomaticsTapApi tapEnv, Dut device) {
	LOGGER.debug("Starting Method : verifyParodusBootRetryCountIsPresent");
	String commandOutput = null;
	Boolean status = false;
	commandOutput = tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CAT_FILE_PARODUSCMD);
	LOGGER.info("Command output: " + commandOutput);
	status = CommonMethods.patternMatcher(commandOutput,
		BroadBandTestConstants.STRING_REGEX_PARODUS_BOOT_TIME_RETRY);
	LOGGER.debug("Ending Method : verifyParodusBootRetryCountIsPresent");
	return status;
    }

    /**
     * Utility method to retrieve the Parodus URL from /etc/device.properties.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi} Reference
     * @param device
     *            {@link Dut} to be validated
     * 
     * @return String representing the Parodus URL.
     * @refactor Govardhan
     */
    public static String getParodusUrl(AutomaticsTapApi tapEnv, Dut device) {
	LOGGER.debug("ENTERING METHOD getParodusUrl");
	String parodusUrl = null;
	String command = BroadBandTestConstants.GREP_COMMAND + BroadBandTestConstants.PARAM_PARODUS_URL
		+ BroadBandTestConstants.SINGLE_SPACE_CHARACTER + BroadBandCommandConstants.FILE_DEVICE_PROPERTIES;
	LOGGER.info("COMMAND TO BE EXECUTED: " + command);
	String response = tapEnv.executeCommandUsingSsh(device, command);
	if (CommonMethods.isNotNull(response)) {
	    String[] tempArray = response.split(AutomaticsConstants.DELIMITER_EQUALS);
	    if (tempArray.length == 2) {
		parodusUrl = tempArray[1].trim();
	    }
	}
	LOGGER.info("PARODUS URL: " + parodusUrl);
	LOGGER.debug("ENDING METHOD getParodusUrl");
	return parodusUrl;
    }
}

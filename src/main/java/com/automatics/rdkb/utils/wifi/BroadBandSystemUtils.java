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

/**
 * Utility class which handles the RDK-B System related functionality and verification.
 * 
 * @author BALAJI V
 * @refactor Govardhan
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.automatics.device.Dut;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.rdkb.utils.BroadBandCommonUtils;

public class BroadBandSystemUtils {
	/**
	 * Logger instance for {@link BroadBandSystemUtils}
	 */
	protected static final Logger LOGGER = LoggerFactory.getLogger(BroadBandSystemUtils.class);
	/**
     * Utility method to verify the log messages on given log file from Arm Console for a given polling time .
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi} Reference
     * @param settop
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

}

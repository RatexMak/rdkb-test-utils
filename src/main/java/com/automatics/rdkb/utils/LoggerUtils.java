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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Dut;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.utils.CommonMethods;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class which handles the Logger Related Operations.
 * 
 * @author BALAJI V
 */
public class LoggerUtils {

	/** SLF4J logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerUtils.class);

	/**
	 * Private Constructor
	 */
	private LoggerUtils() {

	}

	/**
	 * Utility method to search for the last available log messages & then confirm
	 * that this log messages was written after the timestamp passed as an argument.
	 * This method in turn invokes the
	 * BroadBandCommonUtils.verifyLogUsingTimeStamp(). This method takes the Search
	 * Text, Log File & ATOM Sync parameters in addition to the existing method. To
	 * invoke this method we need to pass the timestamp captured before performing
	 * the action.
	 * 
	 * @param device                        {@link Dut}
	 * @param tapApi                        {@link AutomaticsTapApi}
	 * @param searchText                    String representing the Search Text
	 * @param logFile                       String representing the Log File.
	 * @param timeStampCapturedBeforeAction String representing date time captured
	 *                                      before performing any action.
	 * @param isAtomSyncAvailable           Boolean representing the search to be
	 *                                      performed in ATOM/ARM Console; TRUE in
	 *                                      case of ATOM Console; FALSE in case of
	 *                                      ARM Console.
	 * @return String representing the search response.
	 * @Refactor Sruthi Santhosh
	 */
	public static String getLatestLogMessageBasedOnTimeStamp(AutomaticsTapApi tapEnv, Dut device, String searchText,
			String logFile, String timeStampCapturedBeforeAction, boolean isAtomSyncAvailable) {
		LOGGER.debug("STARTING METHOD getLatestLogMessageBasedOnTimeStamp");
		// In case the search text contains space and not wrapped with double quotes.
		if (searchText.contains(BroadBandTestConstants.SINGLE_SPACE_CHARACTER)
				&& !searchText.contains(BroadBandTestConstants.DOUBLE_QUOTE)) {
			searchText = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.DOUBLE_QUOTE,
					searchText, BroadBandTestConstants.DOUBLE_QUOTE);
		}
		String searchCommand = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND,
				searchText, BroadBandTestConstants.SINGLE_SPACE_CHARACTER, logFile, BroadBandTestConstants.SYMBOL_PIPE,
				BroadBandTestConstants.CMD_TAIL_1);
		LOGGER.info("COMMAND TO BE EXECUTED: " + searchCommand);
		String response = isAtomSyncAvailable ? CommonMethods.executeCommandInAtomConsole(device, tapEnv, searchCommand)
				: tapEnv.executeCommandUsingSsh(device, searchCommand);
		if (CommonMethods.isNotNull(response)) {
			response = CommonMethods.patternFinder(response,
					BroadBandTestConstants.PATTERN_MATCHER_ATOM_CONSOLE_LOG_SEARCH_RESPONSE);
			response = CommonMethods.isNotNull(response) ? response.trim() : null;
		}
		LOGGER.info("SEARCH RESPONSE FOR - " + searchText + " IN THE LOG FILE - " + logFile + " IS : " + response);
		boolean result = BroadBandCommonUtils.verifyLogUsingTimeStamp(timeStampCapturedBeforeAction, response);
		LOGGER.info("IS LOG MESSAGE RECENT: " + result);
		response = result ? response : null;
		LOGGER.info("RECENT LOG SEARCH RESPONSE: " + response);
		LOGGER.debug("ENDING METHOD getLatestLogMessageBasedOnTimeStamp");
		return response;
	}

	/**
	 * Utility method to check the ARM Console Log is in sync with ATOM Console Log.
	 * 
	 * @param atomConsoleLog String representing the Log from ATOM Conole Log.
	 * @param armConsoleLog  String representing the Log from ARM Conole Log.
	 * 
	 * @return Boolean representing result of the comparison.
	 * 
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean compareAtomArmConsoleLogs(String atomConsoleLog, String armConsoleLog) {
		LOGGER.debug("STARTING METHOD compareAtomArmConsoleLogs");
		boolean result = false;
		if (CommonMethods.isNotNull(atomConsoleLog) && CommonMethods.isNotNull(armConsoleLog)) {
			// Check both the strings are equal ignoring case;
			atomConsoleLog = atomConsoleLog.trim();
			armConsoleLog = armConsoleLog.trim();
			result = atomConsoleLog.equalsIgnoreCase(armConsoleLog);
			LOGGER.info("RESULT 1 (EQUALS CHECK): " + result);
			// In case the logs are not matching, check the ATOM Console Log is a substring
			// of the ARM Console Log.
			result = result ? result : armConsoleLog.contains(atomConsoleLog) || atomConsoleLog.contains(armConsoleLog);
			LOGGER.info("RESULT 2 (CONTAINS CHECK): " + result);
		}
		LOGGER.info("ATOM CONSOLE HARVESTER LOG: " + atomConsoleLog);
		LOGGER.info("ARM CONSOLE HARVESTER LOG: " + armConsoleLog);
		LOGGER.info("ATOM CONSOLE & ARM CONSOLE LOGS COMPARISON RESULT: " + result);
		LOGGER.debug("ENDING METHOD compareAtomArmConsoleLogs");
		return result;
	}

    /**
     * Utility method to validate the messages have been logged at the stipulated interval.
     * 
     * @param initialSearchResponse
     *            String representing the log message based on the initial search.
     * @param finalSearchResponse
     *            String representing the log message based on the search after stipulated duration.
     * @param expectedTimeInterval
     *            Long representing the logging interval in seconds.
     * 
     * @return Boolean representing the result of the logging interval.
     * 
     * @refactor Athira
     */
    public static boolean verifyLogInterval(String initialSearchResponse, String finalSearchResponse,
	    long expectedTimeInterval) {
	LOGGER.debug("ENTERING METHOD verifyLogInterval");
	boolean result = false;
	try {
	    // Extract the Date Time from the Log Message.
	    String strInitialTimeStamp = CommonMethods.patternFinder(initialSearchResponse,
		    BroadBandTestConstants.PATTERN_MATCHER_LOG_MESSAGE_TIMESTAMP);
	    LOGGER.info("INITIAL TIMESTAMP (EXTRACTED FROM LOG MESSAGE): " + strInitialTimeStamp);
	    String strFinalTimeStamp = CommonMethods.patternFinder(finalSearchResponse,
		    BroadBandTestConstants.PATTERN_MATCHER_LOG_MESSAGE_TIMESTAMP);
	    LOGGER.info("FINAL TIMESTAMP (EXTRACTED FROM LOG MESSAGE): " + strFinalTimeStamp);
	    Date initialTimeStamp = null;
	    Date finalTimeStamp = null;
	    // Parse the extracted Date Time to the required Date format.
	    if (CommonMethods.isNotNull(strInitialTimeStamp) && CommonMethods.isNotNull(strFinalTimeStamp)) {
		SimpleDateFormat logMessageDateTimeFormat = new SimpleDateFormat(
			BroadBandTestConstants.TIMESTAMP_FORMAT_LOG_MESSAGE);
		initialTimeStamp = logMessageDateTimeFormat.parse(strInitialTimeStamp);
		LOGGER.info("INITIAL TIMESTAMP (DATE): " + initialTimeStamp);
		finalTimeStamp = logMessageDateTimeFormat.parse(strFinalTimeStamp);
		LOGGER.info("FINAL TIMESTAMP (DATE): " + finalTimeStamp);
	    }
	    // Compare the date objects created to find the logging interval
	    if (null != initialTimeStamp && null != finalTimeStamp) {
		long logInterval = finalTimeStamp.getTime() - initialTimeStamp.getTime();
		logInterval /= 1000L;
		LOGGER.info("LOG MESSAGE INTERVAL: " + logInterval);
		long timeDiff = logInterval - expectedTimeInterval;
		result = timeDiff >= 0L && timeDiff < 10L;
	    }
	} catch (ParseException parseException) {
	    LOGGER.error("EXCEPTION OCCURRED WHILE PARSING THE DATE: " + parseException.getMessage());
	}
	LOGGER.info("LOG INTERVAL VALIDATION RESULT: " + result);
	LOGGER.debug("ENDING METHOD verifyLogUsingTimeStamp");
	return result;
    }
}
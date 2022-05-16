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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.automatics.device.Dut;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;

public class BroadBandWifiWhixUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandWifiWhixUtils.class);

	/**
	 * Method to restart wifi driver on clients
	 * 
	 * @param clientDevice
	 * @return
	 */
	public static boolean restartWifiDriver(Dut clientDevice, AutomaticsTapApi tapEnv) {
		boolean restartStatus = false;
		String response = null;
		String driverName = null;
		response = tapEnv.executeCommandOnOneIPClients(clientDevice, BroadBandTestConstants.CMD_GET_WIFI_NAME);
		if (CommonMethods.isNotNull(response)) {
			driverName = CommonMethods.patternFinder(response, BroadBandTestConstants.REGEX_GREP_WIFI_NAME);
			LOGGER.info("Driver name from client: " + driverName);
			if (CommonMethods.isNotNull(driverName)) {
				response = tapEnv.executeCommandOnOneIPClients(clientDevice,
						BroadBandTestConstants.CMD_DISABLE_WIFI_DRIVER.replace(BroadBandTestConstants.STRING_REPLACE,
								driverName));
				if (!CommonMethods.isNotNull(response)) {
					LOGGER.info("Waiting for 30 seconds");
					tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
					response = tapEnv.executeCommandOnOneIPClients(clientDevice,
							BroadBandTestConstants.CMD_ENABLE_WIFI_DRIVER.replace(BroadBandTestConstants.STRING_REPLACE,
									driverName));
					restartStatus = !CommonMethods.isNotNull(response);
					if (restartStatus) {
						LOGGER.info("Waiting for 60 seconds after enabling the WiFi driver");
						tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
					}
				}
			}
		}
		return restartStatus;
	}
	
    /**
     * Method to validate the CPU and memory utilisation for negative impact
     * 
     * @param beforeAverage
     * @param AfterAverage
     */
    public static BroadBandResultObject validateCpuAndMemoryUtilisationForNegativeEffect(String beforeAverage,
	    String afterAverage) {
	BroadBandResultObject bandResultObject = new BroadBandResultObject();
	int before = 0;
	int after = 0;

	if (CommonMethods.isNotNull(beforeAverage) && CommonMethods.isNotNull(afterAverage)) {
	    // Validating for Memory utilisation
	    before = BroadBandCommonUtils
		    .convertStringToInteger(beforeAverage.substring(BroadBandTestConstants.CONSTANT_0,
			    beforeAverage.indexOf(BroadBandTestConstants.SINGLE_HASH_TERMINATING_CHAR)));
	    after = BroadBandCommonUtils
		    .convertStringToInteger(afterAverage.substring(BroadBandTestConstants.CONSTANT_0,
			    afterAverage.indexOf(BroadBandTestConstants.SINGLE_HASH_TERMINATING_CHAR)));
	    LOGGER.info("Memory: before : " + before + " after: " + after);
	    if ((((after - before) / after) * 100) <= BroadBandTestConstants.CONSTANT_10) {
		// Validating for CPU utilisation.
		before = BroadBandCommonUtils.convertStringToInteger(beforeAverage.substring(
			beforeAverage.indexOf(BroadBandTestConstants.SINGLE_HASH_TERMINATING_CHAR) + 1,
			beforeAverage.length()));
		after = BroadBandCommonUtils.convertStringToInteger(afterAverage.substring(
			afterAverage.indexOf(BroadBandTestConstants.SINGLE_HASH_TERMINATING_CHAR) + 1,
			afterAverage.length()));
		LOGGER.info("CPU: before : " + before + " after: " + after);
		// since CPU utilisation is given as percent already, this is
		// not converted to percent
		bandResultObject.setStatus((after - before) <= BroadBandTestConstants.CONSTANT_10);
		bandResultObject.setErrorMessage("There is negative impact on the device when this feature is enabled");
	    }
	}

	return bandResultObject;
    }
    
    /**
     * Method to calculate Average CPU and Mem utilisation
     * 
     * @param device
     * @param tapEnv
     * @param pollDuration
     * @param pollInterval
     * @return
     */
    public static String calculateAverageCpuAndMemoryUtilisation(Dut device, AutomaticsTapApi tapEnv, long pollDuration,
	    long pollInterval) {
	String averageValues = null;
	String response1, response2 = null;
	List<String> utilisationValues = new ArrayList<String>();
	long startTime = System.currentTimeMillis();
	do {
	    response1 = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.CMD_GET_MEM_USAGE);
	    response2 = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.CMD_GET_CPU_USAGE);
	    if (CommonMethods.isNotNull(response1) && CommonMethods.isNotNull(response2)) {
		utilisationValues.add(CommonMethods.patternFinder(response1, BroadBandTestConstants.CMD_GREP_MEM_UTIL)
			+ BroadBandTestConstants.SINGLE_HASH_TERMINATING_CHAR
			+ CommonMethods.patternFinder(response2, BroadBandTestConstants.CMD_GREP_CPU_UTIL));
	    }
	    tapEnv.waitTill(pollInterval);
	} while ((System.currentTimeMillis() - startTime) < pollDuration);
	LOGGER.info("List utilisation values: " + utilisationValues.size());
	if (!utilisationValues.isEmpty()) {
	    int summationCpu = 0;
	    int summationMem = 0;
	    for (String value : utilisationValues) {
		LOGGER.info("Values from CPU: " + value);
		String[] splitValue = value.split(BroadBandTestConstants.SINGLE_HASH_TERMINATING_CHAR);
		summationMem = BroadBandCommonUtils
			.convertStringToInteger(splitValue[BroadBandTestConstants.CONSTANT_0]) + summationMem;
		summationCpu = BroadBandCommonUtils
			.convertStringToInteger(splitValue[BroadBandTestConstants.CONSTANT_1]) + summationCpu;
	    }
	    if (summationCpu > BroadBandTestConstants.CONSTANT_0 && summationMem > BroadBandTestConstants.CONSTANT_0) {
		averageValues = (summationMem / utilisationValues.size())
			+ BroadBandTestConstants.SINGLE_HASH_TERMINATING_CHAR
			+ (summationCpu / utilisationValues.size());
	    }

	}
	LOGGER.info("Return values: " + averageValues);
	return averageValues;
    }

}
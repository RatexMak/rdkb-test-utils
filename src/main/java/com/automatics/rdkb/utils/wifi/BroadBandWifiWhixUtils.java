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

}
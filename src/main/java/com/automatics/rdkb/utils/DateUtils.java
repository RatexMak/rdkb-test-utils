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
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.tap.AutomaticsTapApi;


public class DateUtils {
	

    /** Instance of logger. */
    public static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);
    
    /**
     * Method to get epoch time in milli seconds.
     * 
     * @param tapEnv
     *            TAP api reference.
     * @param device
     *            Set-top where test executes.
     * 
     * @return the epoch time in milli seconds.
     */
    public static Long getEpochTimeInMilliSecond(AutomaticsTapApi tapEnv, Dut device) {

	LOGGER.info("STARTING METHOD: DateUtils.getEpochTimeInMilliSecond()");
	String consoleOutput = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.CMD_EPOCH_TIME_IN_MILLI);
	LOGGER.info("Epoch time console output in milli : " + consoleOutput);

	String epochInMilli = BroadBandCommonUtils.parseCommandOutputForOneLinerResult(consoleOutput,
		BroadBandTestConstants.CMD_EPOCH_TIME_IN_MILLI);

	LOGGER.info("ENDING METHOD: DateUtils.getEpochTimeInMilliSecond()");
	return Long.parseLong(epochInMilli);
    }

}

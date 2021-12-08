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
	String deviceManufacturerNameInParodusLog = CommonMethods.isNotNull(verifyParodusLogMessage) ? CommonMethods
		.patternFinder(verifyParodusLogMessage, BroadBandTestConstants.PATTERN_MATCHER_MANUFACTURER_NAME)
		: null;
	result = CommonMethods.isNotNull(deviceManufacturerNameInParodusLog)
		&& (deviceManufacturerName.trim().equals(deviceManufacturerNameInParodusLog.trim()));
	if(result) {
	    LOGGER.info("The Retrieved Device Manufacturer name: " + verifyParodusLogMessage + ", is same as expected Manufacturer name : "+deviceManufacturerName);
	}else {
	    LOGGER.error("The Retrieved Device Manufacturer name: " + verifyParodusLogMessage + ", is different from expected Manufacturer name : "+deviceManufacturerName);
	}
	LOGGER.info("ENDING METHOD compareDeviceManufacturerName");
	return result;
    }
}

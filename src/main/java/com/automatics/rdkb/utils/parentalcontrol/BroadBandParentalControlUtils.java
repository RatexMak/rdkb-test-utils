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
package com.automatics.rdkb.utils.parentalcontrol;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Device;
import com.automatics.device.Dut;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;


public class BroadBandParentalControlUtils {
	

    /** SLF4j logger instance. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandParentalControlUtils.class);
	
    /**
     * Method to Get connected client MAC address
     * 
     * @param tapEnv
     *            Instance of {@link AutomaticsTapApi}
     * @param deviceConnected
     *            Connected client setup
     * @param clientType
     *            Connected client connection type(Ethernet or WiFi)
     * @return Connected Client MAC address
     * 
     * @author prashant.mishra12
     */
    public static String getConnectedClientMACAddress(AutomaticsTapApi tapEnv, Dut deviceConnected, String clientType) {
	LOGGER.debug("STARTING METHOD: getConnectedClientMACAddress()");
	// Variable declaration starts
	String connectedClientMacAddress = null;
	String errorMessage = "";
	// Variable declaration starts
	try {
	    if (clientType.equals(BroadBandTestConstants.CLIENT_TYPE_WIFI)) {
		connectedClientMacAddress = ((Device) deviceConnected).getConnectedDeviceInfo()
			.getWifiMacAddress();
	    } else {
		connectedClientMacAddress = ((Device) deviceConnected).getConnectedDeviceInfo()
			.getEthernetMacAddress();
	    }
	} catch (Exception exception) {
	    LOGGER.error("Exception occured while getting connected client MAC address.");
	    errorMessage = exception.getMessage();
	    LOGGER.error(errorMessage);
	}
	LOGGER.debug("ENDING METHOD: getConnectedClientMACAddress()");
	return connectedClientMacAddress;
    }

    /**
     * Utility method to get the Days which needs to be added in Parental Control Rule when 'Always block' is set to
     * false
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @return the day/s which needs to be added in Parental Control Rule
     * @refactor Govardhan
     */
    public static String getCurrentDayToAddParentalControlRuleWhenAlwaysBlockIsDisabled(AutomaticsTapApi tapEnv,
	    Dut device) {
	LOGGER.debug("ENTERING METHOD getCurrentDayToAddParentalControlRuleWhenAlwaysBlockIsDisabled");
	String blockDays = null;
	try {
	    String date = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.CMD_FOR_DATE);
	    blockDays = CommonMethods.patternFinder(date,
		    BroadBandTestConstants.PATTERN_TO_RETRIEVE_DAY_OF_THE_WEEK_FROM_TIMESTAMP);
	    String currentTimeOnlyHours = CommonMethods.patternFinder(date,
		    BroadBandTestConstants.PATTERN_TO_RETRIEVE_HOUR_FROM_TIMESTAMP);
	    if (CommonMethods.isNotNull(currentTimeOnlyHours)
		    && currentTimeOnlyHours.equals(BroadBandTestConstants.ONLY_HOURS_11_PM)) {
		SimpleDateFormat formatter = new SimpleDateFormat(BroadBandTestConstants.FORMAT_FOR_DAY_OF_THE_WEEK);
		Date day = formatter.parse(blockDays);
		blockDays = BroadBandCommonUtils.concatStringUsingStringBuffer(blockDays, BroadBandTestConstants.COMMA,
			formatter.format(DateUtils.addDays(day, 1)));
	    }
	} catch (Exception e) {
	    LOGGER.error(
		    "EXPECTION OCCCURED WHILE TRYING TO GET THE CURRENT DAY WHICH NEEDS TO BE ADDED IN PARENTAL CONTROL RULE: "
			    + e.getMessage());
	}
	LOGGER.info("Block Days : "+blockDays);
	LOGGER.debug("ENDING METHOD getCurrentDayToAddParentalControlRuleWhenAlwaysBlockIsDisabled");
	return blockDays;
    }
}

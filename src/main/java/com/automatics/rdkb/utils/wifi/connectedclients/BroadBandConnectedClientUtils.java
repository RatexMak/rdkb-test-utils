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
package com.automatics.rdkb.utils.wifi.connectedclients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Dut;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.utils.BroadBandSystemUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;

/**
 * Utils class to hold connected client common Api's
 * 
 * @author Gnanaprakasham.s,Anandam.S
 * 
 */
public class BroadBandConnectedClientUtils {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BroadBandConnectedClientUtils.class);

    /**
     * Utils method to get the reserved ip between the dhcp range
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param ipv4Address
     *            String representing the ipv4 address
     * @return the reserved IP address between DHCP range
     * @refactor Govardhan
     */
    public static String getReservedIpBetweenDhcpRangeFromRouter(AutomaticsTapApi tapEnv, Dut device,
	    String ipv4Address) {
	LOGGER.info("ENTERING METHOD reservedIpBetweenDhcpRange");
	String reservedIp = null;
	String dhcpIpv4Address = null;
	int maxRange = 0;
	int minRange = 0;
	try {
	    String dhcpMinRange = tapEnv.executeWebPaCommand(device,
		    BroadBandWebPaConstants.WEBPA_PARAM_TO_RETRIEVE_DHCP_STARTING_IP_ADDRESS);
	    String dhcpMaxRange = tapEnv.executeWebPaCommand(device,
		    BroadBandWebPaConstants.WEBPA_PARAM_TO_RETRIEVE_DHCP_ENDING_IP_ADDRESS);
	    LOGGER.info("DHCP MINIMUM IP RANGE CONFIGURED FOR GATEWAY : " + dhcpMinRange);
	    LOGGER.info("DHCP MAXIMUM IP RANGE CONFIGURED FOR GATEWAY : " + dhcpMaxRange);
	    if (CommonMethods.isIpv4Address(dhcpMinRange) && CommonMethods.isIpv4Address(dhcpMaxRange)) {

		maxRange = Integer.parseInt(CommonMethods.patternFinder(dhcpMaxRange,
			BroadBandTestConstants.PATTERN_TO_RETRIEVE_LAST_DIGIT_OF_IPv4_ADDRESS));
		minRange = Integer.parseInt(CommonMethods.patternFinder(dhcpMinRange,
			BroadBandTestConstants.PATTERN_TO_RETRIEVE_LAST_DIGIT_OF_IPv4_ADDRESS));
		int reservedRange = BroadBandSystemUtils.getRandomNumberBetweenRange(minRange, maxRange);
		dhcpIpv4Address = CommonMethods.patternFinder(dhcpMinRange,
			BroadBandTestConstants.PATTERN_TO_RETRIEVE_FIRST_3_DIGITS_OF_IPv4_ADDRESS);
		if (CommonMethods.isNotNull(dhcpIpv4Address)) {
		    reservedIp = dhcpIpv4Address.concat(Integer.toString(reservedRange));
		}
	    }
	    while (reservedIp.equalsIgnoreCase(ipv4Address)) {
		int reservedRange = BroadBandSystemUtils.getRandomNumberBetweenRange(minRange, maxRange);
		reservedIp = dhcpIpv4Address.concat(Integer.toString(reservedRange));
		if (!reservedIp.equalsIgnoreCase(ipv4Address)) {
		    break;
		}
	    }

	} catch (Exception e) {
	    LOGGER.error("EXCEPTION OCCURRED WHILE GETTING THE RESERVED IP WITHIN THE DHCP RANGE : " + e.getMessage());
	}

	LOGGER.info("Reserved Ip is : " + reservedIp);
	LOGGER.info("ENDING METHOD getReservedIpBetweenDhcpRangeFromRouter");
	return reservedIp;

    }

}

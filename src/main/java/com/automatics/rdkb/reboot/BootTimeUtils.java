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

package com.automatics.rdkb.reboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Dut;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandConnectedClientTestConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;

public class BootTimeUtils {

    /** SLF4j logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BootTimeUtils.class);

    /**
     * Method to verify status of Interface brlan0
     * 
     * @param device
     *            Set top to be used
     * @return Interface status (UP or DOWN) return status of interface brlan0
     * 
     * @author prashant.mishra12
     * @refactor Said Hisham
     */
    public static String verifyInterfaceBrlan0UpStatus(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug("STARTING METHOD : verifyInterfaceBrlan0UpStatus");
	// Variable declaration starts
	String interfaceStatus = "";
	String response = "";
	String errorMessage = "";
	// Variable declaration ends
	try {
	    response = tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_BRLAN0_STATUS);
	    if (response.contains(BroadBandTestConstants.SBIN_FOLDER_NOT_FOUND_ERROR)) {
		response = tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_BRLAN0_STATUS
			.replace(BroadBandTestConstants.SBIN_FOLDER_CONSTANT, AutomaticsConstants.EMPTY_STRING));
	    }
	    if (CommonMethods.patternMatcher(response, BroadBandTestConstants.STATE_UP_KEYWORD_INTERFACE_BRLAN_0)) {
		interfaceStatus = BroadBandConnectedClientTestConstants.RADIO_STATUS_UP;
	    } else if (CommonMethods.patternMatcher(response,
		    BroadBandTestConstants.STATE_DOWN_KEYWORD_INTERFACE_BRLAN_0)) {
		interfaceStatus = BroadBandConnectedClientTestConstants.RADIO_STATUS_DOWN;
	    }
	} catch (Exception exception) {
	    errorMessage = exception.getMessage();
	    LOGGER.error("Exception occured while getting status (UP or DOWN) for Interface brlan0.");
	    LOGGER.error(errorMessage);
	}
	LOGGER.debug("ENDING METHOD : verifyInterfaceBrlan0UpStatus");
	return interfaceStatus;
    }

    /**
     * Method to verify assigned Ip address(ipv4 & ipv6) to interface brlan0 after selfheal process
     * 
     * @param device
     *            Set top to be used
     * @param ipAddressType
     *            Depends on which Ip address need to be verified (Ipv4 OR Ipv6)
     * @return true if Ip address(Ipv4 or Ipv6 depends on input ipAddressType) assigned successfully to interface brlan0
     * @refactor Said Hisham
     */
    public static boolean verifyIpAddressOfInterfaceBrlan0AfterSelfHeal(Dut device, String ipAddressType,
	    AutomaticsTapApi tapEnv) {
	LOGGER.debug("STARTING METHOD : verifyIpAddressOfInterfaceBrlan0AfterSelfHeal");
	// Variable declaration starts
	boolean isIpAddressAssigned = false;
	String response = null;
	String errorMessage = null;
	int count = 0;
	// Variable declaration ends
	try {
	    do {
		response = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.IFCONFIG_BRLAN);
		if (ipAddressType.equalsIgnoreCase(BroadBandTestConstants.String_CONSTANT_IPV4)) {
		    isIpAddressAssigned = CommonMethods.patternMatcher(response,
			    BroadBandTestConstants.INET_V4_ADDRESS_PATTERN);
		} else if (ipAddressType.equalsIgnoreCase(BroadBandTestConstants.String_CONSTANT_IPV6)) {
		    isIpAddressAssigned = CommonMethods.patternMatcher(response,
			    BroadBandTestConstants.INET_V6_ADDRESS_PATTERN);
		}
		if (isIpAddressAssigned) {
		    LOGGER.info(ipAddressType
			    + " address for interface brlan0 retrieved successfully after interface self heal.");
		    break;
		} else {
		    LOGGER.error("UNABLE TO RETRIEVE " + ipAddressType.toUpperCase()
			    + " ADDRESS AFTER SELF HEAL. WILL TRY AGAIN AFTER WAITING FOR 30 SECONDS.");
		    count++;
		    tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		}
	    } while (count < BroadBandTestConstants.INT_VALUE_THIRTY);
	} catch (Exception exception) {
	    errorMessage = exception.getMessage();
	    LOGGER.error("Exception occured while getting ip address for Interface brlan0 after selfheal.");
	    LOGGER.error(errorMessage);
	}
	LOGGER.debug("ENDING METHOD : verifyIpAddressOfInterfaceBrlan0AfterSelfHeal");
	return isIpAddressAssigned;
    }
}

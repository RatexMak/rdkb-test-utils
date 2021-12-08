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
package com.automatics.rdkb.utils.networkconnectivity;

/**
 * 
 * Utility class for WebPA related functionality.
 * 
 * @author Joseph_Maduram
 * @refactor Govardhan
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Dut;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.constants.BroadBandCdlConstants;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.rdkb.server.WhiteListServer;

public class BroadBandNetworkConnectivityUtils {
    /**
     * Logger instance for {@link BroadBandTr69Utils}
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandNetworkConnectivityUtils.class);

    /**
     * Utility method to get valid Ipv4 ping servers.
     * 
     * @param device
     *            Device to be used
     * @param tapEnv
     *            {@link AutomaticsTapApi} Reference
     * 
     * @return Returns valid ping facebook Ipv4 address
     */
    public static String retrievePingServerUsingNslookUpForIpv4(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug("ENTERING METHOD : retrievePingServerUsingNslookUp");
	boolean status = false;
	String nslookupIPv4Addr = null;// String to store IPv4
	String response = null;
	try {
	    response = tapEnv
		    .executeCommandUsingSshConnection(
			    WhiteListServer.getInstance(tapEnv,
				    AutomaticsPropertyUtility
					    .getProperty(RDKBTestConstants.PROPERTY_REVERSE_SSH_JUMP_SERVER)),
			    BroadBandCommonUtils.concatStringUsingStringBuffer(
				    BroadBandCommandConstants.CMD_NSLOOKUP_WITH_PATH_FOR_IPV4_ADDRESS,
				    BroadBandTestConstants.NSLOOKUP_FOR_FACEBOOK));

	    if (CommonMethods.isNotNull(response)) {
		nslookupIPv4Addr = BroadBandCommonUtils.patternFinderForMultipleMatches(response,
			BroadBandTestConstants.PATTERN_TO_RETRIEVE_IPV4_ADDRESS_FROM_NSLOOKUP_FACEBOOK,
			BroadBandTestConstants.CONSTANT_1).get(BroadBandTestConstants.CONSTANT_0);
		status = CommonMethods.isNotNull(nslookupIPv4Addr) && CommonMethods.isIpv4Address(nslookupIPv4Addr);
		response = status ? nslookupIPv4Addr : null;
	    }
	} catch (Exception e) {
	    LOGGER.error(
		    "EXCEPTION OCCURED WHILE RETRIEVING VALID PINGSERVER IPV4 ADDRESS FROM NS LOOKUP FACEBOOK.COM :\n"
			    + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD retrievePingServerUsingNslookUp");
	return response;
    }

    /**
     * Utility method to get valid Ipv6 ping servers by nslookup google.com.
     * 
     * @param device
     *            Device to be used
     * @param tapEnv
     *            {@link AutomaticsTapApi} Reference
     * 
     * @return Returns valid ping google Ipv6 address
     */
    public static String retrievePingServerUsingNslookUpForIpv6(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug("ENTERING METHOD : retrievePingServerUsingNslookUpForIpv6");
	String nslookupIPv6Addr = null;// String to store IPv4
	// String nslookupForGoogle = null;
	try {
	    nslookupIPv6Addr = tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_WAN_IPV6);
	    LOGGER.info(
		    "WanIpv6 address retrieved from device and used as Ipv6PingServer address is- " + nslookupIPv6Addr);
	} catch (Exception e) {
	    LOGGER.error("EXCEPTION OCCURED WHILE RETRIEVING VALID PINGSERVER IPV6 ADDRESS FROM NSLOOKUP GOOGLE.COM :\n"
		    + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD retrievePingServerUsingNslookUpForIpv6");
	return nslookupIPv6Addr;
    }

    /**
     * Utility method to get valid ping servers.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param type
     *            Ipv4 or Ipv6 ping servers
     * @return Returns true if computational window is set successfully
     * @refactor Govardhan
     */
    public static String resolvePingServer(Dut device, AutomaticsTapApi tapEnv, String type) {
	LOGGER.debug("ENTERING METHOD : validPingServers");
	String response = null;
	String pattern = null;
	try {
	    LOGGER.info("ENTERING METHOD : validPingServers");
	    String validPingServerUrl = AutomaticsPropertyUtility.getProperty(BroadBandCdlConstants.VALID_PING_SERVER_URL);
	    LOGGER.info("validPingServerUrl is-" + validPingServerUrl);
	    String command = 
		    BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_NSLOOKUP,
			    BroadBandTestConstants.SINGLE_SPACE_CHARACTER, validPingServerUrl) ;
	    String output = tapEnv.executeCommandUsingSsh(device, command);
	    if (CommonMethods.isNotNull(output)) {
		if (BroadBandTestConstants.IP_VERSION4.equalsIgnoreCase(type)) {
		    pattern = BroadBandTestConstants.PATTERN_MATCHER_IPV4_VALID_PING_SERVER;
		} else if (BroadBandTestConstants.IP_VERSION6.equalsIgnoreCase(type)) {
		    pattern = BroadBandTestConstants.PATTERN_MATCHER_IPV6_VALID_PING_SERVER;
		} else {
		    throw new TestException("Wrong type passed in to the resolve ping servers Api");
		}
		response = CommonMethods.patternFinder(output, pattern);
	    }
	    LOGGER.info("response is-" + response);
	} catch (Exception exception) {
	    LOGGER.error("EXCEPTION OCCURRED WHILE TRYING TO GET THE PING SERVERS: " + exception.getMessage());
	}
	LOGGER.debug("ENDING METHOD validPingServers");
	return response;
    }
	
}

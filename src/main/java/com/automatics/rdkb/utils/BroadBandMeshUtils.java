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

/**
 * Utils for mesh tests
 * 
 * @author Sathurya
 * @refactor Govardhan
 */

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import com.automatics.device.Dut;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants.WINDOWS_WIRELESS_MODE_OPTIONS;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;

public class BroadBandMeshUtils {

    /**
     * Logger instance for {@link BroadBandMeshUtils}
     */

    protected static final Logger LOGGER = LoggerFactory.getLogger(BroadBandMeshUtils.class);

    /**
     * 
     * This method will check and enable /disable mesh
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param toEnable
     *            true if mesh is to be enabled, else false
     * @return true if success
     */
    public static boolean enableOrDisableMesh(Dut device, AutomaticsTapApi tapEnv, boolean toEnable) {
	boolean status = false;
	String meshStateToSet = toEnable ? BroadBandTestConstants.TRUE : BroadBandTestConstants.FALSE;
	LOGGER.info("Mesh state to set : " + meshStateToSet);
	status = BroadBandWebPaUtils.setParameterValuesUsingWebPaOrDmcli(device, tapEnv,
		BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_MESH_ENABLE, WebPaDataTypes.BOOLEAN.getValue(),
		meshStateToSet);
	return status;
    }

    /**
     * 
     * This method will verify the radio channel has changed after enabling/disabling mesh
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param expectedChannel
     *            channel no to compare
     * @return {@link BroadBandResultObject}
     */

    public static BroadBandResultObject verifyRadioChannelAfterMeshChange(Dut device, AutomaticsTapApi tapEnv,
	    String expectedChannel) {
	BroadBandResultObject result = new BroadBandResultObject();
	List<String> possibleChannels = new ArrayList<String>();
	String possible5ghzChannels = tapEnv.executeWebPaCommand(device,
		BroadBandWebPaConstants.WEBPA_PARAM_FOR_POSSIBLECHANNELS_IN_5GHZ);
	if (CommonMethods.isNotNull(possible5ghzChannels)) {
	    possibleChannels = Arrays.asList(possible5ghzChannels.split(","));
	    LOGGER.info("List of channels : " + possibleChannels.toString());
	    String currentChannelAfterMeshEnable = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device,
		    tapEnv, BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_RADIO_CHANNEL_IN_5GHZ);
	    LOGGER.info("Current Radio channel :" + currentChannelAfterMeshEnable);
	    if (CommonMethods.isNotNull(currentChannelAfterMeshEnable)
		    && currentChannelAfterMeshEnable.equals(expectedChannel)) {
		result.setStatus(true);
		LOGGER.info("5ghz operating channel has not changed after enabling Mesh ");
	    } else if ((possibleChannels.size() > 0 && CommonMethods.isNotNull(currentChannelAfterMeshEnable))) {
		result.setStatus(possibleChannels.contains(currentChannelAfterMeshEnable));
		if (!result.isStatus()) {
		    result.setErrorMessage(
			    "5ghz operating channel is in overlapping range or DFS range .Current radio channel is "
				    + currentChannelAfterMeshEnable + " Initial Radio channel  is " + expectedChannel);
		} else {
		    LOGGER.info(
			    "5ghz operating channel has changed but is still in non overlapping range after mesh enable");
		}
	    } else {
		result.setStatus(false);
		result.setErrorMessage("Current radio channel obtained is null");
	    }
	} else {
	    result.setStatus(false);
	    result.setErrorMessage("Possible 5ghz radio channel list obtained using webpa "
		    + BroadBandWebPaConstants.WEBPA_PARAM_FOR_POSSIBLECHANNELS_IN_5GHZ + " is null");
	}
	return result;
    }
    
    /**
     * Method to change wireless mode on windows client
     * 
     * @param tapEnv
     *            {@link Instanceof AutomaticsTapApi}
     * @param clientSettop
     *            {@link Instanceof Dut}
     * @param wirelessMode
     *            wireless mode to set
     * @return status of set operation
     * 
     * @author Sathurya Ravi
     * @refactor yamini.s
     */

    public static boolean changeWirelessModeOnWindowsClients(AutomaticsTapApi tapEnv, Dut clientSettop,
	    WINDOWS_WIRELESS_MODE_OPTIONS wirelessMode) {
	boolean status = false;
	String response = null;
	LOGGER.debug("STARTING METHOD: changeWirelessModeOnWindowsClients");
	try {

	    LOGGER.info("Option to set " + wirelessMode.getOption());
	    response = tapEnv.executeCommandOnOneIPClients(clientSettop,
		    BroadBandTestConstants.COMMAND_SET_WIRELESS_MODE.replace(BroadBandTestConstants.STRING_REPLACE,
			    wirelessMode.getOption()));
	    
	    if (!CommonMethods.isNotNull(response)) {
		response = tapEnv.executeCommandOnOneIPClients(clientSettop,
			BroadBandTestConstants.COMMAND_GET_WIRELESS_MODE);
		status = CommonMethods.isNotNull(response) && response.contains(wirelessMode.getName());
	    }

	} catch (Exception e) {
	    LOGGER.error("Exception occurred while trying to change wireless mode", e);
	}
	LOGGER.debug("ENDING METHOD:changeWirelessModeOnWindowsClients");
	return status;
    }

}

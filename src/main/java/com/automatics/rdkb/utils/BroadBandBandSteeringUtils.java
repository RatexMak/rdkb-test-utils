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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Dut;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;
import com.automatics.rdkb.utils.dmcli.DmcliUtils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.webpa.WebPaParameter;

/**
 * Utils for band steering tests
 * 
 * @author anandam.s
 * @refactor Govardhan, Alan_Bivera
 */
public class BroadBandBandSteeringUtils {

    /**
     * Logger instance for {@link BroadBandBandSteeringUtils}
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(BroadBandBandSteeringUtils.class);

    /**
     * This method will configure SSID for both Radios
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {{@link AutomaticsTapApi}
     * @return {@link BroadBandResultObject}
     * @refactor Govardhan
     */
    public static BroadBandResultObject configureSSIDForBothRadios(Dut device, AutomaticsTapApi tapEnv,
	    Map<String, String> paramMap) {
	BroadBandResultObject result = new BroadBandResultObject();
	List<String> paramList = new ArrayList<String>();
	for (Map.Entry<String, String> entry : paramMap.entrySet()) {
	    paramList.add(entry.getKey());
	}
	// set new values
	List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
	for (String param : paramList) {
	    WebPaParameter webPaParameter = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(param,
		    paramMap.get(param), WebPaDataTypes.STRING.getValue());
	    webPaParameters.add(webPaParameter);

	}
	Map<String, String> serverResponse = tapEnv.executeMultipleWebPaSetCommands(device, webPaParameters);
	try {
	    for (String param : paramList) {

		boolean status = serverResponse.get(param).equalsIgnoreCase(BroadBandTestConstants.SUCCESS_TXT);
		result.setStatus(status);
		if (!status) {
		    result.setErrorMessage("Failed to Set webpa for " + param);
		    LOGGER.error(result.getErrorMessage());
		    break;
		}
	    }
	} catch (NullPointerException exception) {
	    result.setErrorMessage("Message is obtained as null from webpa server");
	    LOGGER.error(result.getErrorMessage());
	}
	return result;
    }

    /**
     * Get and compare the webpa or dmcli values with the default values
     * 
     * @param settop
     *            {@link Settop}
     * @param webpaParameter
     *            webpa or dmcli parameter to be checked
     * @param defaultValue
     *            expected value
     * @param isWebpa
     *            true if webpa needs to be checked else if dmcli
     * @return BroadBandResultObject {@link BroadBandResultObject}
     * @Refactor Alan_Bivera
     */

    public static BroadBandResultObject getAndCompareWebpaOrDmcliValues(Dut device, AutomaticsTapApi tapEnv,
	    String webpaParameter, String defaultValue, boolean isWebpa,
	    Map<String, String> bandSteeringWebpaResponse) {
	String response = null;
	BroadBandResultObject result = new BroadBandResultObject();
	if (isWebpa) {
	    response = bandSteeringWebpaResponse.get(webpaParameter);
	} else {
	    response = DmcliUtils.getParameterValueUsingDmcliCommand(device, tapEnv, webpaParameter);
	}
	if (CommonMethods.isNotNull(response)) {
	    LOGGER.info(" Response of " + (isWebpa ? "Webpa " : "dmcli ") + webpaParameter + " : " + response);
	    result.setStatus(response.equalsIgnoreCase(defaultValue));
	    if (!result.isStatus()) {
		result.setErrorMessage("Default value of " + (isWebpa ? "Webpa " : "dmcli ") + webpaParameter
			+ "  is not as exepected . EXPECTED :  " + defaultValue + "ACTUAL : " + response);
	    }
	} else {
	    result.setErrorMessage(
		    "Error in getting " + (isWebpa ? "Webpa " : "dmcli ") + " response for " + webpaParameter);
	}

	return result;
    }
}

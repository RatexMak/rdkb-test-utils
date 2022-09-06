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

package com.automatics.rdkb.utils.webpa;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Dut;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.webpa.WebPaParameter;

public class WebPACommonUtils {
	
	 /** SLF4j logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebPACommonUtils.class);
	
    /**
     * Utility method to set a single webPa parameter and get the status.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            The device under test
     * @param parameter
     *            The parameter to set
     * @param value
     *            The value to set
     * @param dataType
     *            The datatype of the parameter
     * @return The status of the webPa command execution
     * @author divya.rs
     * @refactor Rakesh C N
     */
    public static boolean setWebPaParameter(AutomaticsTapApi tapEnv, Dut device, String parameter, String value,
	    int dataType) throws TestException {
	LOGGER.debug("STARTING METHOD : setWebPaParameter");
	boolean status = false;
	try {
	    List<WebPaParameter> list = tapEnv.setWebPaParams(device, parameter, value, dataType);
	    for (WebPaParameter webPaParameter : list) {
		LOGGER.info(
			"Response for " + webPaParameter.getName() + " ------------- " + webPaParameter.getMessage());
		if (webPaParameter.getMessage().equalsIgnoreCase(RDKBTestConstants.STRING_CONNECTION_STATUS)) {
		    status = true;
		} else {
		    status = false;
		    break;
		}
	    }
	} catch (Exception e) {
	    LOGGER.error(e.getMessage());
	    throw new TestException(e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : setWebPaParameter");
	return status;
    }

}


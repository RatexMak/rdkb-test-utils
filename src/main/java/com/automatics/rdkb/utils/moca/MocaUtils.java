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
package com.automatics.rdkb.utils.moca;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Dut;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;

/**
 * Class that stores all utilities required for Enabling and Disabling MOCA and
 * Bridge mode
 * 
 * @author susheela
 */
public class MocaUtils {
	/** SLF4j logger. */

	private static final Logger LOGGER = LoggerFactory.getLogger(MocaUtils.class);

	/**
	 * Method to enable Moca in device using webpa parameter
	 * Device.MoCA.Interface.1.Enable
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @throws TestException
	 * @Refactor Sruthi Santhosh
	 */
	public static void enableMocaAndVerifyTheEnableStatus(AutomaticsTapApi tapEnv, Dut device) throws TestException {
		boolean status = false;
		LOGGER.debug("STARTING METHOD: enableMoca");
		tapEnv.setWebPaParams(device, BroadBandWebPaConstants.WEBPA_PARAM_ENABLE_MOCA, BroadBandTestConstants.TRUE,
				WebPaDataTypes.BOOLEAN.getValue());
		tapEnv.waitTill(AutomaticsConstants.THIRTY_SECOND_IN_MILLIS);
		String response = tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_ENABLE_MOCA);
		LOGGER.info("webpa moca enabled status : " + response);
		if (CommonMethods.isNotNull(response)) {
			status = response.equals(BroadBandTestConstants.TRUE);
		}

		if (!status) {
			throw new TestException("Failed to enable moca in the device with webpa response -" + response);
		}
		LOGGER.debug("ENDING METHOD: enableMoca");

	}

	/**
	 * Method to validate the status of Moca with that of expected
	 * 
	 * @param tapEnv
	 * @param device
	 * @param status
	 * @throws TestException
	 * @Refactor Sruthi Santhosh
	 */
	public static void validateMocaStatus(AutomaticsTapApi tapEnv, Dut device, boolean status) throws TestException {
		boolean mocaStatusValidation = false;
		String response = tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_ENABLE_MOCA);
		LOGGER.info("Moca enabled status : " + response);
		if (CommonMethods.isNotNull(response)) {
			boolean mocaStatus = response.equals(BroadBandTestConstants.TRUE);
			mocaStatusValidation = (status == mocaStatus);
		}

		if (!mocaStatusValidation) {
			throw new TestException("Failed to validate moca status. Expected: " + status + "Actual: ");
		}
	}
}
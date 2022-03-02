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

import com.automatics.device.Dut;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.CommonMethods;

/**
 * Class to verify Device Types
 * 
 * @author Govardhan
 */
public class DeviceModeHandler {
	
    /**
     * Method to get model name whether its a isDSLDevice or not
     * 
     * @author Govardhan
     */
    public static boolean isDSLDevice(Dut device) {
	boolean isDSLDevice = false;
	String deviceModel = device.getModel();
	if (CommonMethods.isNotNull(deviceModel)) {
	    String dslModels = AutomaticsPropertyUtility.getProperty("dsl.device.models");
	    String[] dslModelsList = dslModels.split(",");
	    for (String model : dslModelsList) {
		if (model.equalsIgnoreCase(deviceModel)) {
		    isDSLDevice = true;
		    break;
		}
	    }
	}
	return isDSLDevice;
    }
    
    /**
     * Method to get model name whether its a isFibreDevice or not
     * 
     * @author Govardhan
     */
    public static boolean isFibreDevice(Dut device) {
	boolean isFibreDevice = false;
	String deviceModel = device.getModel();
	if ((CommonMethods.isNotNull(deviceModel))
		&& (AutomaticsPropertyUtility.getProperty("fibre.device.models").contains(deviceModel))) {
	    isFibreDevice = true;
	}
	return isFibreDevice;
    }
    
    /**
     * Method to get model name whether its a isBusinessClassDevice or not
     * 
     * @author Govardhan
     */
    public static boolean isBusinessClassDevice(Dut device) {
	boolean isBusinessClassDevice = false;
	String deviceModel = device.getModel();
	if (CommonMethods.isNotNull(deviceModel)) {
	    String isBusinessClassDeviceValues = AutomaticsPropertyUtility.getProperty("businessclass.device.models");

	    String[] businessClassModels = isBusinessClassDeviceValues.split(",");

	    for (String model : businessClassModels) {
		if (model.equalsIgnoreCase(deviceModel)) {
		    isBusinessClassDevice = true;
		    break;
		}
	    }
	}
	return isBusinessClassDevice;
    }

	
}
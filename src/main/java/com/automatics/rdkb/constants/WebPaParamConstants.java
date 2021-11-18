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
package com.automatics.rdkb.constants;

public class WebPaParamConstants {
    
    public enum WebPaDataTypes {
	STRING(0), UNSIGNED_INT(1), INTEGER(2), BOOLEAN(3);

	private int type;

	WebPaDataTypes(int dataType) {
	    this.type = dataType;
	}

	public int getValue() {
	    return type;
	}
    }
    /** WebPa parameter for Factory Reset */
    public static final String WEBPA_PARAM_FACTORY_RESET = "Device.X_CISCO_COM_DeviceControl.FactoryReset";
}

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

package com.automatics.rdkb.enums;

public enum ProtocolOperationTypeEnum {
	

    /** types of command */

    SNMP_SET("SNMP_SET"),
    SNMP_GET("SNMP_GET"),
    TR69_SET("TR69_SET"),
    TR69_GET("TR69_GET"),
    WEBPA_SET("WEBPA_SET"),
    WEBPA_GET("WEBPA_GET"),
    WEBPA_SET_ATOM("WEBPA_SET_ATOM"),
    WEBPA_GET_ATOM("WEBPA_GET_ATOM");


    private String commandType;

    /**
     * Enumeration constructor.
     * 
     * @param commandType
     */
    private ProtocolOperationTypeEnum(String commandType) {
	this.commandType = commandType;
    }

    /**
     * Method to get the Command Type.
     * 
     * @return The command type
     */
    public String getCommandType() {
	return this.commandType;
    }

}

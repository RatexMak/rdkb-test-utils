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
package com.automatics.rdkb;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * BroadbandRfcDataBaseValueObject Object is used store values from tr181store.json file parse each value of parameter
 * 
 * @author prasanthreddy.a
 * @refactor yamini.s
 *
 */
public class BroadbandRfcDataBaseValueObject {

    private String value;
    private String updateTime;
    private String updateSource;

    public String getValue() {
	return value;
    }

    @JsonProperty("Value")
    public void setValue(String value) {
	this.value = value;
    }

    public String getUpdateTime() {
	return updateTime;
    }

    @JsonProperty("UpdateTime")
    public void setUpdateTime(String updateTime) {
	this.updateTime = updateTime;
    }

    public String getUpdateSource() {
	return updateSource;
    }

    @JsonProperty("UpdateSource")
    public void setUpdateSource(String updateSource) {
	this.updateSource = updateSource;
    }

}


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

package com.automatics.rdkb.utils.tr69;

public enum TR69WriteAccessParameters {

    WEBPA_PARAM_DEVICE_WIFI_RADIO("Device.WiFi.Radio.{i}."),
    WEBPA_PARAM_DEVICE_WIFI_RADIO_STATS("Device.WiFi.Radio.{i}.Stats."),
    WEBPA_PARAM_DEVICE_WIFI_RADIO_ENABLE("Device.WiFi.Radio.{i}.Enable"),
    WEBPA_PARAM_DEVICE_WIFI_RADIO_STATUS("Device.WiFi.Radio.{i}.Status"),
    WEBPA_PARAM_DEVICE_WIFI_RADIO_ALIAS("Device.WiFi.Radio.{i}.Alias"),
    WEBPA_PARAM_DEVICE_WIFI_RADIO_NAME("Device.WiFi.Radio.{i}.Name"),
    WEBPA_PARAM_DEVICE_WIFI_SSID("Device.WiFi.SSID.{i}."),
    WEBPA_PARAM_DEVICE_WIFI_SSID_STATS("Device.WiFi.SSID.{i}.Stats."),
    WEBPA_PARAM_DEVICE_WIFI_SSID_ENABLE("Device.WiFi.SSID.{i}.Enable"),
    WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS("Device.WiFi.SSID.{i}.Status"),
    WEBPA_PARAM_DEVICE_WIFI_SSID_ALIAS("Device.WiFi.SSID.{i}.Alias"),
    WEBPA_PARAM_DEVICE_WIFI_SSID_LASTCHANGE("Device.WiFi.SSID.{i}.LastChange"),
    WEBPA_PARAM_DEVICE_WIFI_SSID_NAME("Device.WiFi.SSID.{i}.Name"),
    WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT("Device.WiFi.AccessPoint.{i}."),
    WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_ASSOCIATEDDEVICE("Device.WiFi.AccessPoint.{i}.AssociatedDevice."),
    WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_WPS("Device.WiFi.AccessPoint.{i}.WPS."),
    WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_SECURITY("Device.WiFi.AccessPoint.{i}.Security."),
    WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_STATUS("Device.WiFi.AccessPoint.{i}.Status"),
    WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_ENABLE("Device.WiFi.AccessPoint.{i}.Enable"),
    WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_ALIAS("Device.WiFi.AccessPoint.{i}.Alias"),
    WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_SSIDREFERENCE("Device.WiFi.AccessPoint.{i}.SSIDReference");

    private String parameter;
    

    private TR69WriteAccessParameters(String parameter) {
	this.parameter = parameter;
    }

    /**
     * Method to get the parameter name.
     * 
     * @return The parameter name
     */
    public String getParameter() {
	return this.parameter;
    }

    /**
     * Method to assign index to objects.
     * 
     * @return The object name with index
     */
    public String assignIndexAndReturn(int i) {
	return this.parameter.replace("{i}", Integer.toString(i));
    }
    
    
}

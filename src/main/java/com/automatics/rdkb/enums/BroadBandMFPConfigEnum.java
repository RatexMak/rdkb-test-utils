/**
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

import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;

/**
 * Enumeration for Broadband Management Frame Protection Config Parameters.
 * 
 * 
 * @author Kiruthiga
 * @refactor Athira
 */

public enum BroadBandMFPConfigEnum {

    MFP_CONFIG_WIFI_AP_1(
	    "Device.WiFi.AccessPoint.10001.Security.MFPConfig",
	    "Optional",
	    WebPaDataTypes.STRING.getValue(),
	    0),
    MFP_CONFIG_WIFI_AP_2(
	    "Device.WiFi.AccessPoint.10002.Security.MFPConfig",
	    "Optional",
	    WebPaDataTypes.STRING.getValue(),
	    2),
    MFP_CONFIG_WIFI_AP_3(
	    "Device.WiFi.AccessPoint.10003.Security.MFPConfig",
	    "Optional",
	    WebPaDataTypes.STRING.getValue(),
	    4),
    MFP_CONFIG_WIFI_AP_4(
	    "Device.WiFi.AccessPoint.10004.Security.MFPConfig",
	    "Optional",
	    WebPaDataTypes.STRING.getValue(),
	    6),
    MFP_CONFIG_WIFI_AP_5(
	    "Device.WiFi.AccessPoint.10005.Security.MFPConfig",
	    "Optional",
	    WebPaDataTypes.STRING.getValue(),
	    8),
    MFP_CONFIG_WIFI_AP_6(
	    "Device.WiFi.AccessPoint.10006.Security.MFPConfig",
	    "Optional",
	    WebPaDataTypes.STRING.getValue(),
	    10),
    MFP_CONFIG_WIFI_AP_7(
	    "Device.WiFi.AccessPoint.10007.Security.MFPConfig",
	    "Optional",
	    WebPaDataTypes.STRING.getValue(),
	    12),
    MFP_CONFIG_WIFI_AP_8(
	    "Device.WiFi.AccessPoint.10101.Security.MFPConfig",
	    "Disabled",
	    WebPaDataTypes.STRING.getValue(),
	    1),
    MFP_CONFIG_WIFI_AP_9(
	    "Device.WiFi.AccessPoint.10102.Security.MFPConfig",
	    "Disabled",
	    WebPaDataTypes.STRING.getValue(),
	    3),
    MFP_CONFIG_WIFI_AP_10(
	    "Device.WiFi.AccessPoint.10103.Security.MFPConfig",
	    "Disabled",
	    WebPaDataTypes.STRING.getValue(),
	    5),
    MFP_CONFIG_WIFI_AP_11(
	    "Device.WiFi.AccessPoint.10104.Security.MFPConfig",
	    "Disabled",
	    WebPaDataTypes.STRING.getValue(),
	    7),
    MFP_CONFIG_WIFI_AP_12(
	    "Device.WiFi.AccessPoint.10105.Security.MFPConfig",
	    "Disabled",
	    WebPaDataTypes.STRING.getValue(),
	    9),
    MFP_CONFIG_WIFI_AP_13(
	    "Device.WiFi.AccessPoint.10106.Security.MFPConfig",
	    "Disabled",
	    WebPaDataTypes.STRING.getValue(),
	    11),
    MFP_CONFIG_WIFI_AP_14(
	    "Device.WiFi.AccessPoint.10107.Security.MFPConfig",
	    "Disabled",
	    WebPaDataTypes.STRING.getValue(),
	    13);

    private String webPaParamMFPConfig;
    private String defaultValue;
    private int valueType;
    private int wifiAPIIndex;

    /**
     * Enumeration Constructor
     * 
     * @param webPaParamMFPConfig
     *            String representing the WebPA Parameter for MFPConfig of WiFi Access Points.
     * @param defaultValue
     *            String representing the default value of the parameter.
     * @param valueType
     *            Integer representing the Webpa parameter datatype.
     * @param wifiAPIIndex
     *            Integer representing the wifi-HAL API index.
     */
    private BroadBandMFPConfigEnum(String webPaParamMFPConfig, String defaultValue, int valueType, int wifiAPIIndex) {
	this.setWebPaParamMFPConfig(webPaParamMFPConfig);
	this.setDefaultValue(defaultValue);
	this.setValueType(valueType);
	this.setWifiAPIIndex(wifiAPIIndex);
    }

    /**
     * @return the webPaParamMFPConfig
     */
    public String getWebPaParamMFPConfigr() {
	return webPaParamMFPConfig;
    }

    /**
     * @param webPaParamMFPConfig
     *            the webPaParamMFPConfig to set
     */
    public void setWebPaParamMFPConfig(String webPaParamMFPConfig) {
	this.webPaParamMFPConfig = webPaParamMFPConfig;
    }

    /**
     * @return the defaultValue
     */
    public String getDefaultValue() {
	return defaultValue;
    }

    /**
     * @param defaultValue
     *            the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
	this.defaultValue = defaultValue;
    }

    /**
     * @return the valueType
     */
    public int getValueType() {
	return valueType;
    }

    /**
     * @param valueType
     *            the valueType to set
     */
    public void setValueType(int valueType) {
	this.valueType = valueType;
    }

    /**
     * @return the wifiAPIIndex
     */
    public int getWifiAPIIndex() {
	return wifiAPIIndex;
    }

    /**
     * @param wifiAPIIndex
     *            the wifiAPIIndex to set
     */
    public void setWifiAPIIndex(int wifiAPIIndex) {
	this.wifiAPIIndex = wifiAPIIndex;
    }
}

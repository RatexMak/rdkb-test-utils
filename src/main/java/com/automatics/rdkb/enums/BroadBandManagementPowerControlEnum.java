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

/**
 * Enumeration for Broadband Activation Journey Events.
 * 
 * <p>
 * Broadband Management Power Control Parameters
 * </p>
 * <ol>
 * <li>Private 2.4GHz WiFi Radio Management Power</li>
 * <li>XHS 2.4GHz WiFi Radio Management Power</li>
 * <li>Public Security Enabled 2.4GHz WiFi Radio Management Power</li>
 * <li>Public 2.4GHz WiFi Radio Management Power</li>
 * <li>AP 5 2.4GHz WiFi Radio Management Power</li>
 * <li>LNF 2.4GHz WiFi Radio Management Power</li>
 * <li>AP 7 2.4GHz WiFi Radio Management Power</li>
 * <li>AP 8 2.4GHz WiFi Radio Management Power</li>
 * <li>Private 5GHz WiFi Radio Management Power</li>
 * <li>XHS 5GHz WiFi Radio Management Power</li>
 * <li>Public Security Enabled 5GHz WiFi Radio Management Power</li>
 * <li>Public 5GHz WiFi Radio Management Power</li>
 * <li>AP 5 5GHz WiFi Radio Management Power</li>
 * <li>LNF 5GHz WiFi Radio Management Power</li>
 * <li>AP 7 5GHz WiFi Radio Management Power</li>
 * <li>AP 8 5GHz WiFi Radio Management Power</li>
 * </ol>
 * 
 * @author BALAJI V
 * @refactor Alan_Bivera
 */

public enum BroadBandManagementPowerControlEnum {

    MGMT_PWR_CTRL_WIFI_AP_1(
	    "Device.WiFi.AccessPoint.10001.X_RDKCENTRAL-COM_ManagementFramePowerControl",
	    -16,
	    0,
	    false),
    MGMT_PWR_CTRL_WIFI_AP_2(
	    "Device.WiFi.AccessPoint.10002.X_RDKCENTRAL-COM_ManagementFramePowerControl",
	    -15,
	    0,
	    false),
    MGMT_PWR_CTRL_WIFI_AP_3(
	    "Device.WiFi.AccessPoint.10003.X_RDKCENTRAL-COM_ManagementFramePowerControl",
	    -14,
	    0,
	    false),
    MGMT_PWR_CTRL_WIFI_AP_4(
	    "Device.WiFi.AccessPoint.10004.X_RDKCENTRAL-COM_ManagementFramePowerControl",
	    -13,
	    0,
	    false),
    MGMT_PWR_CTRL_WIFI_AP_5(
	    "Device.WiFi.AccessPoint.10005.X_RDKCENTRAL-COM_ManagementFramePowerControl",
	    -12,
	    0,
	    false),
    MGMT_PWR_CTRL_WIFI_AP_6(
	    "Device.WiFi.AccessPoint.10006.X_RDKCENTRAL-COM_ManagementFramePowerControl",
	    -11,
	    0,
	    false),
    MGMT_PWR_CTRL_WIFI_AP_7(
	    "Device.WiFi.AccessPoint.10007.X_RDKCENTRAL-COM_ManagementFramePowerControl",
	    -10,
	    0,
	    false),
    MGMT_PWR_CTRL_WIFI_AP_8("Device.WiFi.AccessPoint.10008.X_RDKCENTRAL-COM_ManagementFramePowerControl", -9, 0, false),
    MGMT_PWR_CTRL_WIFI_AP_9("Device.WiFi.AccessPoint.10101.X_RDKCENTRAL-COM_ManagementFramePowerControl", -8, 0, true),
    MGMT_PWR_CTRL_WIFI_AP_10("Device.WiFi.AccessPoint.10102.X_RDKCENTRAL-COM_ManagementFramePowerControl", -7, 0, true),
    MGMT_PWR_CTRL_WIFI_AP_11("Device.WiFi.AccessPoint.10103.X_RDKCENTRAL-COM_ManagementFramePowerControl", -6, 0, true),
    MGMT_PWR_CTRL_WIFI_AP_12("Device.WiFi.AccessPoint.10104.X_RDKCENTRAL-COM_ManagementFramePowerControl", -5, 0, true),
    MGMT_PWR_CTRL_WIFI_AP_13("Device.WiFi.AccessPoint.10105.X_RDKCENTRAL-COM_ManagementFramePowerControl", -4, 0, true),
    MGMT_PWR_CTRL_WIFI_AP_14("Device.WiFi.AccessPoint.10106.X_RDKCENTRAL-COM_ManagementFramePowerControl", -3, 0, true),
    MGMT_PWR_CTRL_WIFI_AP_15("Device.WiFi.AccessPoint.10107.X_RDKCENTRAL-COM_ManagementFramePowerControl", -2, 0, true),
    MGMT_PWR_CTRL_WIFI_AP_16("Device.WiFi.AccessPoint.10108.X_RDKCENTRAL-COM_ManagementFramePowerControl", -1, 0, true);

    private String webPaParamMgmtPower;
    private int valueToBeSet;
    private int defaultValue;
    private boolean wifiRadio5Ghz;

    /**
     * Enumeration Constructor
     * 
     * @param webPaParamMgmtPower
     *            String representing the WebPA Parameter for Management Power Control of WiFi Access Points.
     * @param valueToBeSet
     *            Integer representing the incremental values to be set during the course of execution.
     * @param defaultValue
     *            Integer representing the default value of the parameter.
     * @param is5GhzWiFiRadio
     *            Boolean representing the parameter belongs to 2.GHz Radio or 5GHz Radio.
     */
    private BroadBandManagementPowerControlEnum(String webPaParamMgmtPower, int valueToBeSet, int defaultValue,
	    boolean is5GhzWiFiRadio) {
	this.setWebPaParamMgmtPower(webPaParamMgmtPower);
	this.setValueToBeSet(valueToBeSet);
	this.setDefaultValue(defaultValue);
	this.setWifiRadio5Ghz(is5GhzWiFiRadio);
    }

    /**
     * @return the webPaParamMgmtPower
     */
    public String getWebPaParamMgmtPower() {
	return webPaParamMgmtPower;
    }

    /**
     * @param webPaParamMgmtPower
     *            the webPaParamMgmtPower to set
     */
    public void setWebPaParamMgmtPower(String webPaParamMgmtPower) {
	this.webPaParamMgmtPower = webPaParamMgmtPower;
    }

    /**
     * @return the valueToBeSet
     */
    public int getValueToBeSet() {
	return valueToBeSet;
    }

    /**
     * @param valueToBeSet
     *            the valueToBeSet to set
     */
    public void setValueToBeSet(int valueToBeSet) {
	this.valueToBeSet = valueToBeSet;
    }

    /**
     * @return the defaultValue
     */
    public int getDefaultValue() {
	return defaultValue;
    }

    /**
     * @param defaultValue
     *            the defaultValue to set
     */
    public void setDefaultValue(int defaultValue) {
	this.defaultValue = defaultValue;
    }

    /**
     * @return the wifiRadio5Ghz
     */
    public boolean isWifiRadio5Ghz() {
	return wifiRadio5Ghz;
    }

    /**
     * @param wifiRadio5Ghz
     *            the wifiRadio5Ghz to set
     */
    public void setWifiRadio5Ghz(boolean wifiRadio5Ghz) {
	this.wifiRadio5Ghz = wifiRadio5Ghz;
    }
}
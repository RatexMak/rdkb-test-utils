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
package com.automatics.rdkb.utils.ntp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that stores all required details corresponding to Moca PHy Rates for Associated Devices
 * 
 * @author Susheela
 * 
 */
public class DeviceTimeParams {

    /** TR181 param value for Device.Time.Enable */
    private boolean deviceTimeEnableValue = false;

    /** TR181 param value for Device.Time.Status */
    private String deviceTimeStatus = null;

    /** TR181 param value for Device.Time.NTPServer1 */
    private String deviceTimeNTPServer1 = null;

    /** TR181 param value for Device.Time.NTPServer2 */
    private String deviceTimeNTPServer2 = null;

    /** TR181 param value for Device.Time.NTPServer3 */
    private String deviceTimeNTPServer3 = null;

    /** TR181 param value for Device.Time.NTPServer4 */
    private String deviceTimeNTPServer4 = null;

    /** TR181 param value for Device.Time.NTPServer5 */
    private String deviceTimeNTPServer5 = null;

    /** TR181 param value for Device.Time.CurrentLocalTime */
    private String deviceCurrentLocalTime = null;

    /** TR181 param value for Device.Time.LocalTimeZone */
    private String deviceLocalTimeZone = null;

    /** TR181 param value for Device.Time.TimeOffset */
    private String deviceTimeOffset = null;

    /** TR181 param value for Device.Time.UTC_Enable */
    private String deviceUTCEnable = null;

    /** SLF4J logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceTimeParams.class);

    /**
     * @return the deviceTimeEnableValue
     */
    public boolean getDeviceTimeEnableValue() {
	return deviceTimeEnableValue;
    }

    /**
     * @param deviceTimeEnableValue
     *            the deviceTimeEnableValue to set
     */
    public void setDeviceTimeEnableValue(boolean deviceTimeEnableValue) {
	this.deviceTimeEnableValue = deviceTimeEnableValue;
    }

    /**
     * @return the deviceTimeStatus
     */
    public String getDeviceTimeStatus() {
	return deviceTimeStatus;
    }

    /**
     * @param deviceTimeStatus
     *            the deviceTimeStatus to set
     */
    public void setDeviceTimeStatus(String deviceTimeStatus) {
	this.deviceTimeStatus = deviceTimeStatus;
    }

    /**
     * @return the deviceTimeNTPServer1
     */
    public String getDeviceTimeNTPServer1() {
	return deviceTimeNTPServer1;
    }

    /**
     * @param deviceTimeNTPServer1
     *            the deviceTimeNTPServer1 to set
     */
    public void setDeviceTimeNTPServer1(String deviceTimeNTPServer1) {
	this.deviceTimeNTPServer1 = deviceTimeNTPServer1;
    }

    /**
     * @return the deviceTimeNTPServer2
     */
    public String getDeviceTimeNTPServer2() {
	return deviceTimeNTPServer2;
    }

    /**
     * @param deviceTimeNTPServer2
     *            the deviceTimeNTPServer2 to set
     */
    public void setDeviceTimeNTPServer2(String deviceTimeNTPServer2) {
	this.deviceTimeNTPServer2 = deviceTimeNTPServer2;
    }

    /**
     * @return the deviceTimeNTPServer3
     */
    public String getDeviceTimeNTPServer3() {
	return deviceTimeNTPServer3;
    }

    /**
     * @param deviceTimeNTPServer3
     *            the deviceTimeNTPServer3 to set
     */
    public void setDeviceTimeNTPServer3(String deviceTimeNTPServer3) {
	this.deviceTimeNTPServer3 = deviceTimeNTPServer3;
    }

    /**
     * @return the deviceTimeNTPServer4
     */
    public String getDeviceTimeNTPServer4() {
	return deviceTimeNTPServer4;
    }

    /**
     * @param deviceTimeNTPServer4
     *            the deviceTimeNTPServer4 to set
     */
    public void setDeviceTimeNTPServer4(String deviceTimeNTPServer4) {
	this.deviceTimeNTPServer4 = deviceTimeNTPServer4;
    }

    /**
     * @return the deviceTimeNTPServer5
     */
    public String getDeviceTimeNTPServer5() {
	return deviceTimeNTPServer5;
    }

    /**
     * @param deviceTimeNTPServer5
     *            the deviceTimeNTPServer5 to set
     */
    public void setDeviceTimeNTPServer5(String deviceTimeNTPServer5) {
	this.deviceTimeNTPServer5 = deviceTimeNTPServer5;
    }

    /**
     * @return the deviceCurrentLocalTime
     */
    public String getDeviceCurrentLocalTime() {
	return deviceCurrentLocalTime;
    }

    /**
     * @param deviceCurrentLocalTime
     *            the deviceCurrentLocalTime to set
     */
    public void setDeviceCurrentLocalTime(String deviceCurrentLocalTime) {
	this.deviceCurrentLocalTime = deviceCurrentLocalTime;
    }

    /**
     * @return the deviceLocalTimeZone
     */
    public String getDeviceLocalTimeZone() {
	return deviceLocalTimeZone;
    }

    /**
     * @param deviceLocalTimeZone
     *            the deviceLocalTimeZone to set
     */
    public void setDeviceLocalTimeZone(String deviceLocalTimeZone) {
	this.deviceLocalTimeZone = deviceLocalTimeZone;
    }

    /**
     * @return the deviceTimeOffset
     */
    public String getDeviceTimeOffset() {
	return deviceTimeOffset;
    }

    /**
     * @param deviceTimeOffset
     *            the deviceTimeOffset to set
     */
    public void setDeviceTimeOffset(String deviceTimeOffset) {
	this.deviceTimeOffset = deviceTimeOffset;
    }

    /**
     * @return the deviceUTCEnable
     */
    public String getDeviceUTCEnable() {
	return deviceUTCEnable;
    }

    /**
     * @param deviceUTCEnable
     *            the deviceUTCEnable to set
     */
    public void setDeviceUTCEnable(String deviceUTCEnable) {
	this.deviceUTCEnable = deviceUTCEnable;
    }

}

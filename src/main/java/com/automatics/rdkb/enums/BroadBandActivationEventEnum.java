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

import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;

/**
 * Enumeration for Broadband Activation Journey Events.
 * 
 * <p>
 * Broadband Activation Journey Events
 * </p>
 * <ol>
 * <li>Docsis Registration Complete</li>
 * <li>Lan Initialization Start</li>
 * <li>Lan Initialization Complete</li>
 * <li>Wan Initialization Start</li>
 * <li>Wan Initialization Complete</li>
 * <li>Client Connection Complete</li>
 * <li>Wifi Name Broadcast</li>
 * <li>Wifi Broadcast Complete</li>
 * <li>Enter Wifi Personalization Captive</li>
 * </ol>
 * 
 * @author BALAJI V
 * 
 * @author Reworked by Gnanaprakasham.s
 * @refactor Yamini
 */
public enum BroadBandActivationEventEnum {

    DOCSIS_REG_COMPLETE_ATOM_SYNC_DEVICES(
	    BroadBandTraceConstants.ACTIVATION_DOCSIS_REGISTRATION,
	    BroadBandTestConstants.RDKLOGS_LOGS_ARM_CONSOLE_0,
	    BroadbandPropertyFileHandler.getAtomSyncDevicesForBroadbandActivationEvent(),
	    BroadBandTestConstants.EMPTY_STRING),

    EPON_REG_COMPLETE_FIBER_DEVICES(
	    BroadBandTraceConstants.ACTIVATION_EPON_REGISTRATION,
	    BroadBandTestConstants.RDKLOGS_LOGS_CONSOLE_TXT_0,
	    BroadbandPropertyFileHandler.getFiberDevicesForBroadbandActivationEvent(),
	    BroadBandTestConstants.EMPTY_STRING),

    DOCSIS_REG_COMPLETE_SEC_CONSOLE(
	    BroadBandTraceConstants.ACTIVATION_DOCSIS_REGISTRATION,
	    BroadBandCommandConstants.LOG_FILE_SEC_CONSOLE,
	    BroadbandPropertyFileHandler.getApplicableDevicesForBroadbandActivationEventWithSecConsoleFile(),
	    BroadBandTestConstants.EMPTY_STRING),

    DOCSIS_REG_COMPLETE_TECH_CONSOLE_LOG_TXT(
	    BroadBandTraceConstants.ACTIVATION_DOCSIS_REGISTRATION,
	    BroadBandTestConstants.RDKLOGS_LOGS_CONSOLE_TXT_0,
	    BroadbandPropertyFileHandler.getApplicableDevicesForBroadbandActivationEventWithConsoleLogTxtFile(),
	    BroadBandTestConstants.EMPTY_STRING),

    LAN_INIT_START(
	    BroadBandTraceConstants.ACTIVATION_LAN_INITIALIZATION_START,
	    BroadBandTestConstants.RDKLOGS_LOGS_BOOTTIME_LOG,
	    BroadbandPropertyFileHandler.getApplicableDevicesForLan_Init_StartEvent(),
	    BroadBandTestConstants.EMPTY_STRING),

    LANINIT_COMPLETE(
	    BroadBandTraceConstants.ACTIVATION_LAN_INITIALIZATION_COMPLETE_NEW,
	    BroadBandTestConstants.RDKLOGS_LOGS_BOOTTIME_LOG,
	    BroadbandPropertyFileHandler.getApplicableDevicesForLaninit_completeEvent(),
	    BroadBandTestConstants.EMPTY_STRING),

    WAN_INIT_START(
	    BroadBandTraceConstants.ACTIVATION_WAN_INITIALIZATION_START,
	    BroadBandTestConstants.RDKLOGS_LOGS_BOOTTIME_LOG,
	    BroadbandPropertyFileHandler.getApplicableDevicesForWan_init_startEvent(),
	    BroadBandTestConstants.EMPTY_STRING),

    WANINIT_COMPLETE(
	    BroadBandTraceConstants.ACTIVATION_WAN_INITIALIZATION_COMPLETE_NEW,
	    BroadBandTestConstants.RDKLOGS_LOGS_BOOTTIME_LOG,
	    BroadbandPropertyFileHandler.getApplicableDevicesForWaninit_completeEvent(),
	    BroadBandTestConstants.EMPTY_STRING),

    CLIENT_CONNECT(
	    BroadBandTraceConstants.ACTIVATION_CLIENT_CONNECTION,
	    BroadBandCommandConstants.LOG_FILE_LM,
	    BroadbandPropertyFileHandler.getApplicableDevicesForClient_connectEvent(),
	    BroadBandTestConstants.EMPTY_STRING),

    WIFI_NAME_BROADCAST_2_4(
	    BroadBandTraceConstants.ACTIVATION_WIFI_NAME_BROADCAST,
	    BroadBandTestConstants.LOCATION_WIFI_LOG,
	    BroadbandPropertyFileHandler.getApplicableDevicesForWifi_name_broadcast_2_4Event(),
	    BroadBandTestConstants.EMPTY_STRING),

    WIFI_NAME_BROADCAST_5(
	    BroadBandTraceConstants.ACTIVATION_WIFI_NAME_BROADCAST,
	    BroadBandTestConstants.LOCATION_WIFI_LOG,
	    BroadbandPropertyFileHandler.getApplicableDevicesForWifi_name_broadcast_5Event(),
	    BroadBandTestConstants.EMPTY_STRING),

    WIFI_BROADCAST_COMPLETE(
	    BroadBandTraceConstants.ACTIVATION_WIFI_BROADCAST_COMPLETE,
	    BroadBandTestConstants.LOCATION_WIFI_LOG,
	    BroadbandPropertyFileHandler.getApplicableDevicesForWifi_broadcast_completeEvent(),
	    BroadBandTestConstants.EMPTY_STRING),

    ENTER_WIFI_PERSONALIZATION_CAPTIVE_ATOM_SYNC_DEVICES(
	    BroadBandTraceConstants.ACTIVATION_ENTER_WIFI_PERSONALIZATION_CAPTIVE,
	    BroadBandTestConstants.RDKLOGS_LOGS_ARM_CONSOLE_0,
	    BroadbandPropertyFileHandler.getAtomSyncDevicesForBroadbandActivationEvent(),
	    BroadBandTestConstants.EMPTY_STRING),

    ENTER_WIFI_PERSONALIZATION_CAPTIVE(
	    BroadBandTraceConstants.ACTIVATION_ENTER_WIFI_PERSONALIZATION_CAPTIVE,
	    BroadBandTestConstants.RDKLOGS_LOGS_CONSOLE_TXT_0,
	    BroadbandPropertyFileHandler.getApplicableDevicesForWifiPersonilizationCaptiveEvent(),
	    BroadBandTestConstants.EMPTY_STRING);

    private String logMessageToBeSearched;
    private String logFileName;
    private String applicableDeviceModels;
    private String logSearchResponse;

    /**
     * Enumeration Constructor
     * 
     * @param logMessageToBeSearched
     *            String representing the log message to be searched for the activation event.
     * @param logFileName
     *            String representing the file name in which search needs to be performed
     * @param applicableDeviceModels
     *            String Array of applicable device models
     * @param logSearchResponse
     *            String representing the search response
     */
    private BroadBandActivationEventEnum(String logMessageToBeSearched, String logFileName,
	    String applicableDeviceModels, String logSearchResponse) {
	this.setLogMessageToBeSearched(logMessageToBeSearched);
	this.setLogFileName(logFileName);
	this.setApplicableDeviceModels(applicableDeviceModels);
	this.setLogSearchResponse(logSearchResponse);
    }

    /**
     * @return the logMessageToBeSearched
     */
    public String getLogMessageToBeSearched() {
	return logMessageToBeSearched;
    }

    /**
     * @param logMessageToBeSearched
     *            the logMessageToBeSearched to set
     */
    public void setLogMessageToBeSearched(String logMessageToBeSearched) {
	this.logMessageToBeSearched = logMessageToBeSearched;
    }

    /**
     * @return the logFileName
     */
    public String getLogFileName() {
	return logFileName;
    }

    /**
     * @param logFileName
     *            the logFileName to set
     */
    public void setLogFileName(String logFileName) {
	this.logFileName = logFileName;
    }

    /**
     * @return the applicableDeviceModels
     */
    public String getApplicableDeviceModels() {
	return applicableDeviceModels;
    }

    /**
     * @param applicableDeviceModels
     *            the applicableDeviceModels to set
     */
    public void setApplicableDeviceModels(String applicableDeviceModels) {
	this.applicableDeviceModels = applicableDeviceModels;
    }

    /**
     * @return the logSearchResponse
     */
    public String getLogSearchResponse() {
	return logSearchResponse;
    }

    /**
     * @param logSearchResponse
     *            the logSearchResponse to set
     */
    public void setLogSearchResponse(String logSearchResponse) {
	this.logSearchResponse = logSearchResponse;
    }
}
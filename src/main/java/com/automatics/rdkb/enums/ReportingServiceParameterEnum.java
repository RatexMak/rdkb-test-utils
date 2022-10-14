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
 * Enum Class to get all related parameters to reporting service 
 * 
 * @author Praveenkumar Paneerselvam
 *
 */
public enum ReportingServiceParameterEnum {

    
    INTERFACE_DEVICE_WIFI("Device.X_RDKCENTRAL-COM_Report.InterfaceDevicesWifi.Enabled","Device.X_RDKCENTRAL-COM_Report.InterfaceDevicesWifi.PollingPeriod","Device.X_RDKCENTRAL-COM_Report.InterfaceDevicesWifi.ReportingPeriod"),
    NETWORK_DEVICE_STATUS ("Device.X_RDKCENTRAL-COM_Report.NetworkDevicesStatus.Enabled","Device.X_RDKCENTRAL-COM_Report.NetworkDevicesStatus.PollingPeriod","Device.X_RDKCENTRAL-COM_Report.NetworkDevicesStatus.ReportingPeriod"),
    RADIO_INTERFACE_STATISTICS("Device.X_RDKCENTRAL-COM_Report.RadioInterfaceStatistics.Enabled", "Device.X_RDKCENTRAL-COM_Report.RadioInterfaceStatistics.Default.PollingPeriod", "Device.X_RDKCENTRAL-COM_Report.RadioInterfaceStatistics.Default.ReportingPeriod");
    
    private String webpaEnable = null;
    
    private String webpaPollingPeriod = null;
    
    private String webpaReportingPeriod = null;
    
    /**
     * @return the webpaEnable
     */
    public String getWebpaEnable() {
        return webpaEnable;
    }

    /**
     * @param webpaEnable the webpaEnable to set
     */
    public void setWebpaEnable(String webpaEnable) {
        this.webpaEnable = webpaEnable;
    }

    /**
     * @return the webpaPollingPeriod
     */
    public String getWebpaPollingPeriod() {
        return webpaPollingPeriod;
    }

    /**
     * @param webpaPollingPeriod the webpaPollingPeriod to set
     */
    public void setWebpaPollingPeriod(String webpaPollingPeriod) {
        this.webpaPollingPeriod = webpaPollingPeriod;
    }

    /**
     * @return the webpaReportingPeriod
     */
    public String getWebpaReportingPeriod() {
        return webpaReportingPeriod;
    }

    /**
     * @param webpaReportingPeriod the webpaReportingPeriod to set
     */
    public void setWebpaReportingPeriod(String webpaReportingPeriod) {
        this.webpaReportingPeriod = webpaReportingPeriod;
    }

    
    /**
     * @param webpaEnable webPA parameter to enable the service.
     * @param webpaPollingPeriod webpa parameter to set the polling period of the service
     * @param webpaReportingPeriod webpa parameter to set the reporting period of the service
     */
    private ReportingServiceParameterEnum(String webpaEnable,String webpaPollingPeriod,String webpaReportingPeriod ){
	this.webpaEnable = webpaEnable;
	this.webpaPollingPeriod = webpaPollingPeriod;
	this.webpaReportingPeriod = webpaReportingPeriod;
    }
}

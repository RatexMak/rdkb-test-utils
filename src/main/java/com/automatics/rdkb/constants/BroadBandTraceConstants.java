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

public class BroadBandTraceConstants {
    
    /** Log message for 2.4GHz WiFi channel bandwidth in wifihealth log file */
    public static final String LOG_MESSAGE_WIFI_2G_CHAN_WIDTH = "WiFi_config_2G_chan_width_split";
    
    /** Log message for 2.4GHz WiFi channel bandwidth in wifihealth log file */
    public static final String LOG_MESSAGE_WIFI_5G_CHAN_WIDTH = "WiFi_config_5G_chan_width_split";
    
    /** Log Message in WiFilog.txt.0 for Wifi factory reset verification in atom console */
    public static final String LOG_MESSAGE_WIFI_NAME_BROADCASTED = "\"Wifi_Name_Broadcasted\"";
    
    /** Log Message for wifi interface name from Mac OS */
    public static final String LOG_MESSAGE_MAC_OS_WIFI_INTERFACE_NAME = "Device:\\s+(.*)";
    
    /** Log message for beacon rate change when operating mode set to b,g,n */
    public static final String LOG_MESSAGE_BEACON_CHANGE_MODE_BGN = "BEACON RATE CHANGED vAP0 6Mbps to 1Mbps by TR-181 Object Device.WiFi.Radio.1.OperatingStandards";
    
    /** Log message for beacon rate change when operating mode set to g,n */
    public static final String LOG_MESSAGE_BEACON_CHANGE_MODE_GN = "BEACON RATE CHANGED vAP0 1Mbps to 6Mbps by TR-181 Object Device.WiFi.Radio.1.OperatingStandards";
    
    /** Log message for beacon rate changed using webpa */
    public static final String LOG_MESSAGE_BEACON_PARAM_CHANGE = "BEACON RATE CHANGED vAP0 6Mbps to 2Mbps by TR-181 Object Device.WiFi.AccessPoint.1.X_RDKCENTRAL-COM_BeaconRate";

    /** Log Message for wifi ssid removed from Mac OS wifi profile */
    public static final String LOG_MESSAGE_MAC_OS_WIFI_SSID_REMOVED_IN_PROFILE = "Removed\\s+(.*)\\s+from the preferred networks list";
    
    /** Log Message for wifi ssid Added into Mac OS wifi profile */
    public static final String LOG_MESSAGE_MAC_OS_WIFI_SSID_ADDED_IN_PROFILE = "(?s)Added\\s+(.*)\\s+to preferred networks list";
    
    /** Log Message for ssid disconnected from Mac OS */
    public static final String LOG_MESSAGE_MAC_OS_SSID_DISCONNECTED = "You are not associated with an AirPort network";
    
    /** Log Message for ssid connected from Mac OS */
    public static final String LOG_MESSAGE_MAC_OS_SSID_CONNECTED = "Current Wi-Fi Network:\\s+(.*)";
   
    /** Log message for snmp initialized in SNMP.txt.0 */
    public static final String LOG_MESSAGE_SNMP_INITIALIZED = "snmp initialzed";
    
    /** Log Message for Execution Succeed Message */
    public static final String DMCLI_OUTPUT_EXECUTION_SUCCEED = "Execution succeed";
    
    /** Log message of fragmentation rule in iptables */
    public static final String LOG_MESSAGE_FRAG_DROP = "FRAG_DROP";
    
    /** Log message of flood detect rule in iptables */
    public static final String LOG_MESSAGE_DOS = "DOS";
    
    /** Log message of port scan rule in iptables */
    public static final String LOG_MESSAGE_PORT_SCAN = "PORT_SCAN";
	
	/** Log Message for Direct Communication */
    public static final String LOG_MESSAGE_DIRECT_COMMUNICATION = "\"Trying Direct Communication\"";
    
    /** Log Message for Direct communication server url */
    public static final String LOG_MESSAGE_DIRECT_COMMUNICATION_SERVER_URL = "\"S3 URL is : <REPLACE>\"";
    
    /** pattern for getting http upload link **/
    public static final String LOG_PATTERN_FOR_HTTP_UPLOAD_LINK = "HTTP_UPLOAD_LINK=(.*)";
    
    /** Log Message for Bridge Mode Enabled */
    public static final String LOG_MESSAGE_BRIDGE_MODE_ENABLED = "RDKB_LAN_CONFIG_CHANGED: Setting new LanMode value (bridge-dhcp(1),bridge-static(2),router(3),full-bridge-static(4)) as (2)...";
    //public static final String LOG_MESSAGE_BRIDGE_MODE_ENABLED = "CosaUtilGetIpv6AddrInfo";
    /** Log message for setting mesh wifi to disabled as bridge mode enabled in PAMlog */
    public static final String LOG_MESSAGE_SETTING_MESH_DISABLED = "Setting MESH to disabled as LanMode is changed to Bridge mode";
    
    /** ping Test connectivity Success log */
    public static final String PING_SERVER_SUCCESS_LOG_MESSAGE = "\"Connectivity Test is Successfull\"";
    
    /** gateway Ip connectivity Success log */
    public static final String PING_SERVER_GATEWAY_IP_SUCCESS_LOG_MESSAGE = "\"GW IP Connectivity Test Successfull\"";


}

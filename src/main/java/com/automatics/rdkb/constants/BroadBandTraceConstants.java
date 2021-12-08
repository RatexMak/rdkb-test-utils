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

public class BroadBandTraceConstants extends RDKBTraceConstants{
    
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
    
    /** Log message for webPA request received in PARODUS log file */
    public static final String PARODUS_WEBPA_REQUEST_RECEIVED_TRACE = "Received msg from server";
    
    /** Log message for webPA response sent in PARODUS log file */
    public static final String PARODUS_WEBPA_RESPONSE_SENT_TRACE = "Sending response to server";

    /** Telemetry Marker for Invalid ping server failure */
    public static final String TELEMETRY_MARKER_FOR_PING_SERVER_FAILURE = "PING_FAILED";
    
    /** Ping failure Log Message for IPv4 ping server URI */
    public static final String PING_SERVER_IPV4_FIRST_URI_FAIL = "\"PING_FAILED:1.2.3.4\"";

    /** Ping failure Log Message for IPv4 ping server URI */
    public static final String PING_SERVER_IPV4_SECOND_URI_FAIL = "\"PING_FAILED:2.3.4.5\"";

    /** Ping failure Log Message for IPv4 ping server URI */
    public static final String PING_SERVER_IPV4_THIRD_URI_FAIL = "\"PING_FAILED:3.4.5.6\"";

    /** Ping failure Log Message for IPv4 ping server URI */
    public static final String PING_SERVER_IPV6_FIRST_URI_FAIL = "\"PING_FAILED:2001::2002\"";

    /** Ping failure Log Message for IPv4 ping server URI */
    public static final String PING_SERVER_IPV6_SECOND_URI_FAIL = "\"PING_FAILED:2002::2003\"";

    /** Ping failure Log Message for IPv4 ping server URI */
    public static final String PING_SERVER_IPV6_THIRD_URI_FAIL = "\"PING_FAILED:2003::2004\"";
    
    /** Ping failure Log Message */
    public static final String PING_SERVER_FAIL_LOG_MESSAGE = "\"Ping to both IPv4 and IPv6 servers are failed\"";
    
    /** Log File for DCM Script */
    public static final String LOG_FILE_SYSCFG = " /nvram/syscfg.db";
    
    /** log for getting datablock size */
    public static final String DATA_BLOCK_SIZE_LOG_MESSAGE = "\"selfheal_ping_DataBlockSize=64\"";
    
    
    /** Log message for completed RFC pass in dcmrfc.log */
    public static final String LOG_MESSAGE_COMPLETED_RFC_PASS = "COMPLETED RFC PASS";
    
    /** Log message for bootstrap RFC configuration */
    public static final String LOG_MESSAGE_BOOTSTRAP_CONFIG = "BOOTSTRAP_CONFIG";
    
    /** Activation Journey - WAN Initialization */
    public static final String ACTIVATION_WAN_INITIALIZATION_COMPLETE_NEW = "Waninit_complete";
    
    /** Log Message in Webpalog for webpa ready uptime */
    public static final String WEBPA_READY_UPTIME = "\"boot_to_WEBPA_READY_uptime\"";
    
    /** Log message for starting parodus component in PARODUSlog */
    public static final String LOG_MESSAGE_STARTING_PAROUS = "Starting component: Parodus";
    
    /** Log Message in PARODUS log for Parodus error */
    public static final String LOG_MESSAGE_FOR_PARODUS_LOG = "Certificate is not yet valid";
    
    /** Log message for connect time minus boot time logged in PARODUSlog */
    public static final String LOG_MESSAGE_CONNECT_TIME_DIFF_BOOT_TIME = "connect_time-diff-boot_time";
    
    /** Log message for starting webpa component in WEBPAlog */
    public static final String LOG_MESSAGE_STARTING_WEBPA = "Starting component: com.cisco.spvtg.ccsp.webpaagent";
    
    /** Log Message in PARODUS log for Connected to server over SSL */
    public static final String LOG_MESSAGE_SERVER_CONNECTION = "Connected to server over SSL";
    
    /** The constant holding a log for http download request rejected status. */
    public static final String LOG_MESSAGE_HTTP_DOWNLOAD_REQUEST_REJECTED_WITH_SAME_VERSION = "no upgrade/downgrade required";
    
    /** The constant holding a log for http download request accepted status. */
    public static final String LOG_MESSAGE_HTTP_DOWNLOAD_REQUEST_ACCEPTED = "processing upgrade/downgrade";
    
    /** The constant holding a log for http download completed status. */
    public static final String LOG_MESSAGE_HTTP_DOWNLOAD_COMPLETED = "http download completed with status : 200";
    
    /** The constant holding a log for http download not successful status. */
    public static final String LOG_MESSAGE_HTTP_DOWNLOAD_NOT_SUCCESSFUL = "http download not successful";
    
    /** The constant holding a log during maintenance window. */
    public static final String LOG_XCONF_DURING_MAINTENANCE_WINDOW = "still within current maintenance window for reboot";
}

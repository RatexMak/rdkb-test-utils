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
    public static final String LOG_MESSAGE_BEACON_PARAM_CHANGE = "BEACON RATE CHANGED vAP0 6Mbps to 12Mbps by TR-181 Object Device.WiFi.AccessPoint.1.X_RDKCENTRAL-COM_BeaconRate";

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
    
    /** Log message for Alert! RDK Event will be processed once Device is online; Received WHITE Solid in SecConsole */
    public static final String LOG_MESSAGE_WHITE_SOLID_AFTER_BOOT_SECCONSOLE = "Alert! RDK Event will be processed once Device is online; Received WHITE Solid";
    
    /** Log message for Front LED Transition: WHITE LED will SOLID in PAMLOG */
    public static final String LOG_MESSAGE_WHITE_SOLID_PAMLOG = "Front LED Transition: WHITE LED will be SOLID";  
    
    /** Log message for Front LED Transition: GREEN LED will blink, Reason: Factory Reset in PAMlog */
    public static final String LOG_MESSAGE_LED_CHANGE_GREEN_BEFORE_FR_PAMLOG = "Front LED Transition: GREEN LED will blink, Reason: Factory Reset";

    public static final String LOG_MESSAGE_LED_CHANGE_WHITE_BEFORE_FR_SECCONSOLE = "Front LED Transition: Changing Led to White";
    /** Log message for Front LED Transition : Mode -----> SOLID in SecConsole */

    /** Log message for Front LED Transition: Changing Led to Green in SecConsole */
    public static final String LOG_MESSAGE_LED_CHANGE_GREEN_BEFORE_FR_SECCONSOLE = "Front LED Transition: Changing Led to Green";

    /** Log message for Front LED Transition : Mode -----> SOLID in SecConsole */
    public static final String LOG_MESSAGE_LED_MODE_SOLID_BEFORE_FR_SECCONSOLE = "Front LED Transition :  Mode----> Solid";        

    /** Log message for Front LED Transition : Mode -----> Blink in SecConsole */
    public static final String LOG_MESSAGE_LED_MODE_BLINK_BEFORE_FR_SECCONSOLE = "Front LED Transition :  Mode -----> Blink";
 
    /** Log message for Front LED Transition: WHITE LED will SOLID, Reason: CaptivePortal Disabled in PAMlog */
    public static final String LOG_MESSAGE_WHITE_SOLID_AFTER_BOOT_PAMLOG = "Front LED Transition: WHITE LED will be SOLID, Reason: ConfigureWiFi is TRUE, but CaptivePortal is disabled";
    
    /** Log message for Changing Led to White in SecConsole */
    public static final String LOG_MESSAGE_LED_CHANGE_WHITE_AFTER_BOOT_SECCONSOLE = "Changing Led to White";
    
    /** Log message for Alert! RDK Event will be processed once Device is online; Received WHITE Blink in SecConsole */
    public static final String LOG_MESSAGE_WHITE_BLINK_AFTER_BOOT_SECCONSOLE = "Alert! RDK Event will be processed once Device is online; Received WHITE Blink";
    
    /** Log message for Front LED Transition: WHITE LED will blink, Reason: CaptivePortal_MODE in PAMlog */
    public static final String LOG_MESSAGE_WHITE_BLINK_AFTER_BOOT_PAMLOG = "Front LED Transition: WHITE LED will blink, Reason: CaptivePortal_MODE";
    
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
    
    /** Log message for Parodus serial number log message in Arm/Consolelog */
    public static final String LOG_MESSAGE_PARODUS_SER_NUM = "serialNumber returned from hal";
    
    /** Log message for Parodus mac log message in Arm/Consolelog */
    public static final String LOG_MESSAGE_PARODUS_MAC = "deviceMac";
    
    /** Log message for Parodus http_code: 200 log message in Arm/Consolelog */
    public static final String LOG_MESSAGE_PARODUS_HTTP_200 = "PARODUS: themis curl response 0 http_code 200";
    
    /** Log message for parodus curl failure in PARODUSlog */
    public static final String LOG_MESSAGE_TO_CHECK_PARODUS_CURL_RETRY_FAILURE = "PARODUS: Curl retry is reached to max 3 attempts, proceeding without token";
    
    /** Log message for parodus curl success in PARODUSlog */
    public static final String LOG_MESSAGE_PARODUS_CURL_SUCCESS = "Parodus: cURL success";
    
    /** Log message for parodus client_cert_path in PARODUSlog */
    public static final String LOG_MESSAGE_CLIENT_CERT_PATH = "client_cert_path";
    
    /** Log message for parodus token_server_url in PARODUSlog */
    public static final String LOG_MESSAGE_TOKEN_SERVER_URL = "token_server_url";
    
    /** Log message for Parodus command token-acquisition-script arguement */
    public static final String LOG_MESSAGE_PARODUS_CMD_CREATE_FILE_ARGUEMENT = "\"\\\\--token-acquisition-script=/usr/ccsp/parodus/parodus_create_file.sh\"";
    
    /** Log message for Parodus command token-read-script arguement */
    public static final String LOG_MESSAGE_PARODUS_CMD_READ_FILE_ARGUEMENT = "\"\\\\--token-read-script=/usr/ccsp/parodus/parodus_read_file.sh\"";
    
    /** Log message for wbepa ready */
    public static final String LOG_MESSAGE_WEBPA_READY = "\"WEBPA_READY\"";
    
    /** String to store the Log message for CR system ready signal */
    public static final String LOG_MESSAGE_CR_SYSTEM_READY = "From CR: System is ready";
    
    /** String to store the Log message for Received system ready signal */
    public static final String LOG_MESSAGE_RECIEVED = "Received system ready signal";
    
    /** String to store the Log message for checked CR-System is ready */
    public static final String LOG_MESSAGE_CHECKED = "Checked CR - System is ready, proceed with component caching";
    
    /** Log message for component caching is completed */
    public static final String LOG_MESSAGE_COMP_CACHING = "\"Component caching is completed\"";
    
    /** Log message for received reboot reason */
    public static final String LOG_MESSAGE_REBOOT_REASON = "\"Received reboot_reason as:\"";
    
    /** Log message for failed to */
    public static final String LOG_MESSAGE_FAILED_TO = "\"Failed to\"";

    /** Log message for webpa agent */
    public static final String LOG_MESSAGE_WEBPA_AGENT = "\"webpaagent\"";
    
    /** Log message for minidump upload is successful */
    public static final String LOG_MESSAGE_UPLOAD_SUCCESS = "minidump Upload is successful";
    
    /** Log message for full url in parodus log */
    public static final String LOG_MESSAGE_FULL_URL = "full url";
    
    /**
     * Log message for Received temporary redirection response message in parodus log
     */
    public static final String LOG_MESSAGE_RECEIVED_TEMP_REDIRECT = "Received temporary redirection response message";
    
    /** Log message for startParodus is enabled in parodus log */
    public static final String LOG_MESSAGE_START_PARODUS = "startParodus is enabled";
    
    /** Log message for CPU Memory Fragmentation */
    public static final String LOG_MESSAGE_CPU_MEMORY_FRAGMENTATION = "CPU_MEMORY_FRAGMENTATION";
    
    /** Log Message for LMLite Integration */
    public static final String LOG_MESSAGE_LMLITE_INTEGRATION = "\"LMLite: Init for parodus Success\"";
    
    /** Log message for harvester initialization */
    public static final String LOG_MESSAGE_HARVESTER_INITIALIZATION = "\"HARV : /tmp/harvester_initialized created\"";
    
    /** Log message for Device finger printing enabled in Console log */
    public static final String WEBPA_GET_NOTIFICATION = "\"WDMP-C: Request\"";

    /** Log message for Device finger printing enabled in Console log */
    public static final String WEBPA_SET_NOTIFICATION = "\"WDMP-C: SET Request\"";

    /** Log message for Device finger printing enabled in Console log */
    public static final String WEBPA_POST_NOTIFICATION = "\"WDMP-C: ADD_ROW Request\"";

    /** Log message for Device finger printing enabled in Console log */
    public static final String WEBPA_PUT_NOTIFICATION = "\"WDMP-C: REPLACE_ROWS Request\"";

    /** Log message for Device finger printing enabled in Console log */
    public static final String WEBPA_DELETE_NOTIFICATION = "\"WDMP-C: DELETE_ROW Request\"";
    
    /** Log message for successful message sent to parodus */
    public static final String LOG_MESSAGE_SUCCCESS_TO_PARADOUS = "\" Sent message successfully to parodus\"";

  /** Log message for firmware udate url */
    public static final String LOG_MESSAGE_FW_UPDATE_FROM_URL = "Device retrieves firmware update from url";

    /** Log message for HTTP response code 404 */
    public static final String LOG_MESSAGE_HTTP_RESPONSE_CODE_404 = "HTTP RESPONSE CODE is 404";

    /** Log message for FW upgrade Excluded */
    public static final String LOG_MESSAGE_EXCLUDED_FROM_FW_UPGRADE = "\"Device excluded from FW Upgrade!! Exiting\"";

    /** Log message for SNMPv3 support enable in dcmscript.log file */
    public static final String LOG_MESSAGE_SNMPV3_SUPPORT = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.SNMP.V3Support value=true";
    
    /** Log message for Init for parodus success */
    public static final String LOG_MESSAGE_INIT_PARODUS = "\"Init for parodus Success\"";
    
    /** Log message for get attributes */
    public static final String LOG_MESSAGE_GET_ATTRIBUTES = "\"GET_ATTRIBUTES\"";
    
    /** Log message for get */
    public static final String LOG_MESSAGE_GET = "\"GET\"";
    
    /** Log message for set attributes */
    public static final String LOG_MESSAGE_SET_ATTRIBUTES = "\"SET ATTRIBUTES\"";
    
    /** Log message for test and set request */
    public static final String LOG_MESSAGE_TEST_SET = "\"Test and Set Request\"";
    
    /** Log message for add_row request */
    public static final String LOG_MESSAGE_ADD_ROW_REQ = "\"ADD_ROW Request\"";
    
    /** Log message for replace_rows */
    public static final String LOG_MESSAGE_REPLACE_ROWS = "\"REPLACE_ROWS\"";

    /** Log message for delete_row */
    public static final String LOG_MESSAGE_DELETE_ROWS = "\"DELETE_ROW\"";
    
    /** Log message for Notification event from stack */
    public static final String LOG_MESSAGE_NOTIFY_STACK = "\"Notification Event from stack\"";
    
    /** Log message for cloud status set offline in PARODUSlog */
    public static final String LOG_MESSAGE_CLOUD_STATUS_SET_OFFLINE = "PARODUS: cloud_status set as offline after connection close";
    
    /** Log message for custom parodus close reason in /tmp */
    public static final String LOG_MESSAGE_CLOSING_FROM_FILE = "Closing from file";
    
    /** Log message for parodus close reason file path */
    public static final String LOG_MESSAGE_PARODUS_CLOSE_REASON_FILE = "PARODUS: sigterm_close_reason_file is /tmp/parodus_close_reason.txt";
    
    /** Log Message for Parodus Integration with Seshat Completion */
    public static final String LOG_MESSAGE_SESHAT_INTEGRATION_COMPLETE = "\"PARODUS: Ping received with payload\"";
    
    /** Log message for complete parodus custom close reason in PARODUSlog.txt.0 */
    public static final String LOG_MESSAGE_PARODUS_CUSTOM_CLOSE_REASON = "PARODUS: Closed by SIGTERM, reason: "
	    + LOG_MESSAGE_CLOSING_FROM_FILE;
    
    /** Log message for shutdown reason in PARODUSlog */
    public static final String LOG_MESSAGE_PARODUS_CUSTOM_SHUTDOWN_REASON = "PARODUS: shutdown reason at close "
	    + LOG_MESSAGE_CLOSING_FROM_FILE;
    
    /** Log message for shutdown parodus signal received in PARODUSlog */
    public static final String LOG_MESSAGE_PARODUS_SIGTERM_RECEIVED = "PARODUS: SIGTERM received";
    
    /** Log Message for Mesh Enable Error when bridge enabled */
    public static final String LOG_MESSAGE_MESH_ERROR_BRIDGE_ENABLED = "MESH_ERROR:Fail to enable Mesh when Bridge mode is on";
    
    /** Telemetry marker for DOSCIS SNMP device reboot.*/
    public static final String TELEMETRY_MARKER_DOCSIS_SNMP_REBOOT = "RDKB_REBOOT: Reboot triggered by SNMP";
    
    /** Log message for component caching is completed in settop trace */
    public static final String WEBPA_LOG_MESSAGE_COMP_CACHING_TRACE = "WEBPA: Component caching is completed";
    
    /** Log message for harvester log INIT parodus Check */
    public static final String LOG_MESSAGE_PARODUS_INIT = "Init for parodus Success";
    
    /** Log message for successful message sent to parodus For LmLite */
    public static final String LOG_MESSAGE_SUCCCESS_TO_PARADOUS_LMLITE = "Sent message successfully to parodus";
    
    /** Log Message for Parodus process */
    public static final String LOG_MESSAGE_PARODUS_PROCESS = "\"Started parodusStart in background\"";
    
    /** Log message parameter server.http-parseopts available in /var/lighttpd.conf */
    public static final String LOG_MESSAGE_SERVER_HTTP_PARSEOPTS = "server.http-parseopts";
    
    /** Log message available in /var/lighttpd.conf */
    public static final String LOG_MESSAGE_URL_PATH_2F_DECODE = "url-path-2f-decode";
    
    /** Log message available in Consolelog.txt.0 or ArmConsolelog.txt.0 when UpdateNvram is true */
    public static final String LOG_MESSAGE_SYSCFG_MESSAGE_STORED_IN_NVRAM = "SEC: Syscfg stored in /nvram/syscfg.db";
    
    /** Log message available in Consolelog.txt.0 or ArmConsolelog.txt.0 */
    public static final String LOG_MESSAGE_SYSCFG_MESSAGE_STORED_IN_SECURE_DATA = "SEC: Syscfg stored in ";
 	
 	/** Log message to check if syscfg.db moved to /opt/secure/data folder */
    public static final String LOG_MESSAGE_SYSCFG_FIREWALL = "firewall_level=High";
 	
    /** Log message available in Consolelog.txt.0 or ArmConsolelog.txt.0 when UpdateNvram is true */
    public static final String LOG_MESSAGE_SYSCFG_STORED_IN_NVRAM = "Syscfg stored in /nvram/syscfg.db";

    /** Log message available in Consolelog.txt.0 or ArmConsolelog.txt.0 when UpdateNvram is false */
    public static final String LOG_MESSAGE_SYSCFG_STORED_IN_OPT_SECURE_DATA = "Syscfg stored in /opt/secure/data/syscfg.db";
    
    /** Log Message for IOT Client Registered */
    public static final String LOG_MESSAGE_CONFIG_CLIENT_REGISTERED = "\"PARODUS: Client config Registered successfully\"";
    
    /** Log message for uprivilege mode running as non-root */
    public static final String LOG_MESSAGE_UNPRIVILEGE_MODE_NON_ROOT = "unprivilege user name: non-root";
    
    /** Log message for uprivilege mode */
    public static final String LOG_MESSAGE_UNPRIVILEGE = "unprivilege";

    /** Log message for uprivilege mode */
    public static final String LOG_MESSAGE_UNPRIVILEGE_MODE = "Dropping root privilege of parodus: runs as unprivilege mode";
    
    /** Log message for response code 200 */
    public static final String LOG_MESSAGE_RESPONSE_200 = "Response code: 200";
    
    /** Log message for back off retry time */
    public static final String LOG_MESSAGE_BACK_OFF_RETRY_TIME = "backoffRetryTime";
    
    /** Log message for Connected to server over SSL in parodus log */
    public static final String LOG_MESSAGE_CONNECTED_TO_SERVER_OVER_SSL = "Connected to server over SSL";

    /** Trace Constants for WebPA Telemetry */
    /** Log Message for WebPA_process is not running */
    public static final String LOG_MESSAGE_WEBPA_PROCESS_RESTART = "\"WebPA_process is not running, restarting it\"";
    
    /** Log Message for parodus process is not running */
    public static final String LOG_MESSAGE_PARODUS_PROCESS_RESTART = "\"parodus process is not running, need restart\"";
  
    /**
     * Log Message for Parameter value field is not available in WebPA curl patch request
     */
    public static final String LOG_MESSAGE_WEBPA_WITHOUT_FIELD = "\"Parameter value field is not available\"";
    
    /** Log Message for Received reboot reason */
    public static final String LOG_MESSAGE_RECEIVED_REBOOT_REASON = "\"Received reboot_reason as:webpa-reboot\"";
    
    /** Log Message in PAMlog.txt for reboot pending notification */
    public static final String LOG_MESSAGE_REBOOT_PENDING_NOTIFICATION = "reboot-pending";
    
    /** Log message for shutdown parodus in Arm/Consolelog */
    public static final String LOG_MESSAGE_SHUTDOWN_PARODUS = "Shutdown parodus";
    
    /** Log message for shutdown reason in PARODUSlog */
    public static final String LOG_MESSAGE_PARODUS_SHUTDOWN_REASON = "PARODUS: shutdown reason at close system_restarting";
    
    /** Log message for firmware download started notification in xconf.txt */
    public static final String LOG_MESSAGE_FIRMWARE_DOWNLOAD_STARTED_NOTIFICATION = "FirmwareDownloadStartedNotification";
    
    /** Log message for firmware download started notification in PAM and PARODUS logs */
    public static final String LOG_MESSAGE_FIRMWARE_DOWNLOAD_STARTED = "firmware-download-started";
    
    /** Log message for firmware download completed notification in xconf.txt */
    public static final String LOG_MESSAGE_FIRMWARE_DOWNLOAD_COMPLETED_NOTIFICATION = "FirmwareDownloadCompletedNotification";
    
    /** Log message for firmware download completed notification in PAM and PARODUS logs */
    public static final String LOG_MESSAGE_FIRMWARE_DOWNLOAD_COMPLETED = "firmware-download-completed";
    
    /** Log message for "***Exiting the application***" */
    public static final String LOG_MESSAGE_EXITING_THE_APPLICATION = "Exiting the application";

    /** Log message for "Triggering RunCPUProcAnalyzer.sh" */
    public static final String LOG_MESSAGE_TRIGGERING_RUNCPUPROCANALYZER = "Triggering RunCPUProcAnalyzer.sh";

    /** Log message for CPAstats */
    public static final String LOG_MESSAGE_CPASTATS = "CPAstats";
    
    /** Log Message for Logs Uploaded Successfully */
    public static final String LOG_MESSAGE_LOGS_UPLOADED_SUCCESSFULLY = "\"LOGS UPLOADED SUCCESSFULLY, RETURN CODE: 200\"";
    
    /** Log message for minidump upload failed to S3 amazon location */
    public static final String LOG_MESSAGE_MINIDUMP_AMAZON_UPLOAD_FAILED = "S3 Amazon Upload of minidump Failed";

    /** Log message for retry minidump upload to S3 amazon location */
    public static final String LOG_MESSAGE_MINIDUMP_AMAZON_UPLOAD_RETRY = "Retry";

    /** Log message for minidump Fail over mechanism upload to crash portal */
    public static final String LOG_MESSAGE_MINIDUMP_FAIL_OVER_UPLOAD = "Fail Over Mechanism: CURL minidump to crashportal";

    /** Log message for minidump successfully uploaded to crash portal */
    public static final String LOG_MESSAGE_MINIDUMP_CRASH_PORTAL_SUCCESS_UPLOAD = "Success uploading minidump file:";

    /** Log message for minidump Fail over mechanism upload to crash portal failed */
    public static final String LOG_MESSAGE_MINIDUMP_FAIL_OVER_FAILED = "Fail Over Mechanism for minidump : Failed..!";

    /** Log message for cleanup minidump working directory */
    public static final String LOG_MESSAGE_MINIDUMP_CLEANUP_DIRECTORY = "Cleanup minidump directory /minidumps";

    /** Log message for minidump working directory empty */
    public static final String LOG_MESSAGE_MINIDUMP_WORKING_DIR_EMPTY = "WORKING_DIR is empty";

    /** Log message for minidump upload success to S3 amazon location */
    public static final String LOG_MESSAGE_MINIDUMP_AMAZON_UPLOAD_SUCCESS = "S3 minidump Upload is successful with TLS1.2";
    
    /** Log message for upload string in core_log for crash files */
    public static final String LOG_MESSAGE_UPLOAD_STRING = "Upload string";
    
    /** Log message to indicate that DNS strict order is disabled */
    public static final String LOG_MSG_FOR_DNS_STRICT_ORDER_DISABLED = "\"RFC DNSTRICT ORDER is not defined or Enabled\" ";
    
    /** Log message to indicate that DNS strict order is enabled via RFC */
    public static final String LOG_MSG_FOR_DNS_STRICT_ORDER_ENABLED = " \"Starting dnsmasq with additional dns strict order option:\" ";

    /** Log Message in PAM log for wifi reset */
    public static final String LOG_MESSAGE_WIFI_REBOOT_MESSAGE = "WiFi is going to reboot";
    
    /** Log Message in PAM log for Router reboot */
    public static final String LOG_MESSAGE_ROUTER_REBOOT_MESSAGE = "Router is going to reboot";
    
    /** Log message for firmware download started notification not sent */
    public static final String LOG_MESSAGE_FW_DWN_START_NOTIFY_NOT_SENT = "\"firmware download start notification is not sent\""; 
    
    /** Log message for firmware download completed notification not sent */
    public static final String LOG_MESSAGE_FW_DWN_COMPLETE_NOTIFY_NOT_SENT = "\"firmware download completed notfication is not sent\"";
    
    /** Log message for fully manageable notification in PAMlog */
    public static final String LOG_MESSAGE_FULLY_MANAGEABLE_NOTIFICATION = "fully-manageable";
    
    /** Log Message in PAMlog.txt.0 for reboot abort */
    public static final String LOG_MESSAGE_ABORT_REBOOT = "\"invalid request for parameter.*AbortReboot\"";
    
    /** Log message for abort count under xconf.txt.0 log */
    public static final String LOG_MESSAGE_ABORT_COUNT = "\"Abort Count is\" ";
    
    /** Log message for maximum abort count */
    public static final String LOG_MESSAGE_MAXIMUM_ABORT_COUNT = "\"Abort Count reached maximum limit 5\""; 
    
    /** Telemetry marker to validate device initiation */
    public static final String TELEMETRY_MARKER_DEVICE_INITIATION = "DEVICE_INIT";
    
    /** Log message to validate Firmware upgrade start time */
    public static final String LOG_MESSAGE_FIRMWARE_START_TIME = "Firmware upgrade start time";

    /** Log message to validate Firmware upgrade end time */
    public static final String LOG_MESSAGE_FIRMWARE_END_TIME = "Firmware upgrade end time";
    
    /** Log message for Wan Link Heal for bootup-check invoked in SelfHeal.txt.0 */
    public static final String LOG_MESSAGE_WAN_LINK_HEAL_INVOKED = "Wan Link Heal";
    
    /** Log message for cm_status operational in SelfHeal.txt.0 */
    public static final String LOG_MESSAGE_CM_STATUS_OPERATIONAL = "cm_status=OPERATIONAL";
    
    /** Log message for cm_status 1 in SelfHeal.txt.0 */
    public static final String LOG_MESSAGE_CM_STATUS_ONE = "cm_status=1";
    
    /** Log message for cm prov IPv6 in SelfHeal.txt.0 */
    public static final String LOG_MESSAGE_CM_PROV_IPV6 = "cm_prov=IPv6";
    
    /** Log message for cm ipv6 in SelfHeal.txt.0 */
    public static final String LOG_MESSAGE_CM_IPV6 = "cm_ipv6=";
    
    /** Log message for cm_ip_status in SelfHeal.txt.0 */
    public static final String LOG_MESSAGE_CM_IP_STATUS = "cm_ip_status=1";
    
    /** Log message for wan_ipv4 in SelfHeal.txt.0 */
    public static final String LOG_MESSAGE_WAN_IPV4 = "wan_ipv4=";

    /** Log message for wan_ipv6 in SelfHeal.txt.0 */
    public static final String LOG_MESSAGE_WAN_IPV6 = "wan_ipv6=";
    
    /** Log message for wan_ip_status in SelfHeal.txt.0 */
    public static final String LOG_MESSAGE_WAN_IP_STATUS = "wan_ip_status=1";
    
    /** Log message for ping4_status in SelfHeal.txt.0 */
    public static final String LOG_MESSAGE_PING_IPV4_STATUS = "ping4_status=1";
    
    /** Log message for gw_health stored in SelfHeal.txt.0 */
    public static final String LOG_MESSAGE_GW_HEALTH_STORED = "gw_health stored";

    /** Log message for gw_health current in SelfHeal.txt.0 */
    public static final String LOG_MESSAGE_GW_HEALTH_CURRENT = "gw_health current";
    
    /** Log message for IsNeedtoRebootDevice in SelfHeal.txt.0 */
    public static final String LOG_MESSAGE_NEED_TO_REBOOT = "IsNeedtoRebootDevice = 0";
    
    /** Log message which indicates that /opt/secure is mounted successfully */
    public static final String LOG_TO_VERIFY_OPT_SECURE_IS_MOUNTED = "Mounted cpc directory successfully";
    
    /** Log message which indicates that /opt/secure is not mounted successfully */
    public static final String LOG_TO_VERIFY_OPT_SECURE_IS_NOT_MOUNTED = "Failed to mount cpc directory";
    
    /** Log message for unmount target is busy */
    public static final String LOG_TARGET_IS_BUSY = "umount: /opt/secure: target is busy";
    
    /** Log message for interval is 2 */
    public static final String LOG_MESSAGE_AGGR_SELFHEAL_INTERVAL = "\"INTERVAL is: 2\"";

    /** Log message for brlan0 is not completely up */
    public static final String LOG_MESSAGE_BRLAN0_DOWN = "\"brlan0 is not completely up\"";

    /** Log message for brlan0 already restarted */
    public static final String LOG_MESSAGE_BRLAN0_RESTARTED = "\"brlan0 already restarted\"";

    /** Log message for brlan1 is not completely up */
    public static final String LOG_MESSAGE_BRLAN1_DOWN = "\"brlan1 is not completely up\"";

    /** Log message for brlan1 already restarted */
    public static final String LOG_MESSAGE_BRLAN1_RESTARTED = "\"brlan1 already restarted\"";

    /** Log message for dnsmasq is not running */
    public static final String LOG_MESSAGE_DNSMASQ_NOT_RUN = "\"dnsmasq is not running\"";

    /** Log message for dibbler is not running */
    public static final String LOG_MESSAGE_DIBBLER_NOT_RUN = "\"dibbler is not running\"";

    /** Log message for erouter0 is down */
    public static final String LOG_MESSAGE_EROUTER0_DOWN = "\"erouter0 is DOWN\"";

    /** Log message for zombie instance of dnsmasq */
    public static final String LOG_MESSAGE_ZOMBIE_INSTANCE_OF_DNSMASQ = "\"Zombie instance of dnsmasq is present\"";
    
    /** Log message for DHCPv6 client not running */
    public static final String LOG_MESSAGE_DHCP_V6_CLIENT_NOT_RUNNING = "\"DHCP Client for v6 is not running, need restart\"";
    
    /** Log message for DHCPv4 client is not running */
    public static final String LOG_MESSAGE_DHCP_CLIENT_V4_NOT_RUNNING = "\"DHCP Client for v4 is not running, need restart\"";
    
    /** Log message for dibbler client running */
    public static final String LOG_MESSAGE_DIBBLER_CLIENT_PID = "Dibbler client: RUNNING, pid=";
    
    /** Log message for ping to peer ip failed */
    public static final String LOG_MESSAGE_PING_TO_PEER_IP_FAILED = "\"Ping to Peer IP failed\"";
    
    /** Log message for peer interface ip */
    public static final String LOG_MESSAGE_PEER_INT_IP = "PEER_INTERFACE_IP";
    
    /** Constant for Netcat invoked log */
    public static final String LOG_MESSAGE_NC_NETCAT_INVOKED = "Use `dbg here' to see log messages; other dbg cmds for log level";
    
    /** The constant holding a log for no such file or directory while using grep command */
    public static final String LOG_MESSAGE_GREP_NO_SUCH_FILE_OR_DIRECTORY = "No such file or directory";
    
    /** Log message for fetching ethernet related details in windows NUC */
    public static final String LOG_MESSAGE_IPCONFIG_ETHERNET = "\"Ethernet adapter Ethernet:\"";
    
    /** Log message for fetching ethernet related details in windows NUC */
    public static final String LOG_MESSAGE_IPCONFIG_WIFI = "\"Wireless LAN adapter Wi-Fi\"";
    
    /** Log message for harvester integration with seshat library */
    public static final String LOG_MESSAGE_HARVESTER_INTEGRATION = "\"Init for parodus Success\""; 
    
    /** Log Message in WEBPA.txt.0 for factory reset verification */
    public static final String LOG_MESSAGE_REBOOT_REASON_FACTORY_RESET = "Received reboot_reason as:factory-reset";

    /** Log message for openssl failed certificate verification */
    public static final String OPENSSL_FAILED_CERTIFICATE_VERIFICATION = "openssl_connect - SSL handshake failed -- ssl state = SSLERR";

    /** Log message for openssl success certificate verification */
    public static final String OPENSSL_SUCCESS_CERTIFICATE_VERIFICATION = "client_openssl_validate_certificate - X509 certificate successfully verified on host";
    
    /** Log message from SelfHeal.txt.0 */
    public static final String LOG_MESSAGE_100_CPU_LOAD_FROM_SELFHEAL = "RDKB_SELFHEAL : CPU load at 100, top process:";
    
    /** Trace Constant for Management Frame Power Control Change */
    public static final String LOG_MESSAGE_MGMT_FRAME_POWER_CONTROL = "X_RDKCENTRAL-COM_ManagementFramePowerControl:";
    

}

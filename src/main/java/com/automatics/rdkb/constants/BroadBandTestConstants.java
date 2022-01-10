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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.automatics.snmp.SnmpDataType;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpMib;

public class BroadBandTestConstants extends RDKBTestConstants {

    /** String variable to store common ssid for band steering */
    public static final String TEXT_RDKB = "RDKB";

    /** Constant to hold value string Auto */
    public static final String STRING_AUTO = "Auto";

    /** String value for Wi-Fi */
    public static final String WIFI = "Wi-Fi";

    /** Constant for 2.4GHZ band */
    public static final String BAND_2_4GHZ = "2.4GHZ";

    /** Command to get the disk usage value. */
    public static final String CMD_GREP_I = "grep -i";

    /** Constant for number 0 */
    public static final int CONSTANT_0 = 0;

    /** Constant for number 1 */
    public static final int CONSTANT_1 = 1;

    /** Constant for number 2 */
    public static final int CONSTANT_2 = 2;

    /** Constant for number 4 */
    public static final int CONSTANT_4 = 4;
    
    /** Constant for number 7 */
    public static final int CONSTANT_7 = 7;
   
    /** Constant for number 8 */
    public static final int CONSTANT_8 = 8;
    
    /** Constant for number 10 */
    public static final int CONSTANT_10 = 10;

    /** Constant to integer value 25 */
    public static final int CONSTANT_25 = 25;

    /** constant to value 30 */
    public static final int CONSTANT_30 = 30;
    
    /** Constant for number 11 */
    public static final int CONSTANT_11 = 11;
    
    /** Constant for number 13 */
    public static final int CONSTANT_13 = 13;
    
    /** Constant for number 16 */
    public static final int CONSTANT_16 = 16;
    
    /** Constant for number 1000 */
    public static final int CONSTANT_1000 = 1000;
    
    /** Integer value 5 */
    public static final Integer INTEGER_VALUE_5 = 5;
    
    /** String constant value 1 */
    public static final String STRING_CONSTANT_1 = "1";
    
    /** String constant 2 */
    public static final String STRING_CONSTANT_2 = "2";
    
    /** String value 2 */
    public static final String STRING_VALUE_TWO = "2";
    
    /** String to store value 4 */
    public static final String STRING_VALUE_4 = "4";
    
    /** String value 5 */
    public static final String STRING_VALUE_FIVE = "5";
    
    /** String value for 10 */
    public static final String STRING_VALUE_TEN = "10";
    
    /** String to store value 11 */
    public static final String STRING_VALUE_11 = "11";
    /** String to store value 12 */
    public static final String STRING_VALUE_12 = "12";
    /** String to store value 13 */
    public static final String STRING_VALUE_13 = "13";
    /** String to store value 14 */
    public static final String STRING_VALUE_14 = "14";
    /** String to store value 15 */
    public static final String STRING_VALUE_15 = "15";
    /** String to store value 16 */
    public static final String STRING_VALUE_16 = "16";
    /** String to store value 17 */
    public static final String STRING_VALUE_17 = "17";
    /** String to store value 18 */
    public static final String STRING_VALUE_18 = "18";
    /** String to store value 19 */
    public static final String STRING_VALUE_19 = "19";
    
    /** Constant to hold 153 */
    public static final String CONSTANT_153 = "153";

    /** String value 40 */
    public static final String STRING_VALUE_FORTY = "40";
    

    /** constant for Ping Interval as 15 mins */
    public static final String CONSTANT_PING_INTERVAL = "15";

    /** Constant for Character Hyphen */
    public static final String CHARACTER_HYPHEN = "-";
    
    /** Symbol quotes ("). */
    public static final String SYMBOL_QUOTES = "\"";
    
    public static final String CHAR_NEW_LINE = "\n";

    /** String value Test1 */
    public static final String STRING_TEST_1 = "Test1";

    /** String value Test2 */
    public static final String STRING_TEST_2 = "Test2";

    /** Field descriptor wait for 2 seconds */
    public static final long TWO_SECOND_IN_MILLIS = 2000L;

    /** Field descriptor wait for 20 seconds */
    public static final long TWENTY_SECOND_IN_MILLIS = 20000L;

    /** wait for 30 seconds */
    public static final long THIRTY_SECOND_IN_MILLIS = 30000L;

    /** Constant to hold int value for ninety seconds in milliseconds */
    public static final long NINETY_SECOND_IN_MILLIS = 90000L;

    /** wait for 90 seconds */
    public static final long NINTY_SECOND_IN_MILLIS = 90000L;

    /** wait for ONE minute */
    public static final long ONE_MINUTE_IN_MILLIS = 60000L;

    /** wait for Two minutes */
    public static final long TWO_MINUTE_IN_MILLIS = 120000L;

    /** wait for 3 minutes */
    public static final long THREE_MINUTE_IN_MILLIS = 180000L;

    /** wait for 5 minutes */
    public static final long FIVE_MINUTE_IN_MILLIS = 300000L;

    /** wait for 10 minutes */
    public static final long TEN_MINUTE_IN_MILLIS = 600000L;
    
    /** Ten minute in millisecond representation. */
    public static final long SIX_MINUTE_IN_MILLIS = 6 * ONE_MINUTE_IN_MILLIS;
    
    /** Ten seconds in millisecond representation. */
    public static final long TEN_SECOND_IN_MILLIS = 5 * TWO_SECOND_IN_MILLIS;
    
    /** 60 minutes in milliseconds. */
    public static final long SIXTY_MINUTES_IN_MILLIS = 60 * ONE_MINUTE_IN_MILLIS;
    
    /** String FACTORY RESET VALUE for WiFi */
    public static final String STRING_FACTORY_RESET_WIFI = "Wifi";

    /** The constant holds the WebPA success response */
    public static final String SUCCESS_TXT = "success";
    
    /** Constant to hold Parameter is not writable */
    public static final String PARAMETER_NOT_WRITABLE = "Parameter is not writable";

    /** Wifi Broadcasted Wifi Pattern Matcher */
    public static final String PATTERN_MATCHER_WIFI_BROADCASTED = "Wifi_Name_Broadcasted(.*)";

    /** Log file location for WiFiLog.txt.0 */
    public static final String LOCATION_WIFI_LOG = "/rdklogs/logs/WiFilog.txt.0";

    /** log entry disabled */
    public static final String MESHWIFI_STATE_FULL = "Full";

    /** Mesh Wifi OFF state */
    public static final String MESHWIFI_STATE_OFF = "Off";

    /** Status init */
    public static final String WIFIMESH_STATUS_INIT = "Init";

    /** log entry enabled */
    public static final String MESHWIFI_ENABLED_LOG = "MESH_STATUS:enabled";

    /** log entry disabled */
    public static final String MESHWIFI_DISABLED_LOG = "MESH_STATUS:disabled";
    
    /** Constant to hold ping failure message */
    public static final String PING_FAILURE_MESSAGE = "100% packet loss";
    
    /** Constant to hold ping success message */
    public static final String PING_SUCCESS_MESSAGE = "0% packet loss";

    /** Constant to hold facebook.com */
    public static final String NSLOOKUP_FOR_FACEBOOK = "facebook.com";
    
    /** Constant to hold the value to set firewall IPv4 & IPv6 to 'Custom Security' */
    public static final String FIREWALL_CUSTOM_SECURITY = "Custom";
    
    /** Constant to hold the value to set firewall IPv6 to 'Typical Security' */
    public static final String FIREWALL_IPV6_TYPICAL_SECURITY = "Default";

    /** Pattern to retrieve ipv6 address from Nslookup response via SSH */
    public static final String PATTERN_TO_RETRIVE_IPV6_ADDRESS_FROM_NSLOOKUP_FACEBOOK_VIA_SSH = "Address 1:\\s*(.*)\\s*edge-star-mini6-shv-01.*";

    /**
     * Command to read WifiMesh disabled status logs from /rdklogs/logs/MeshAgentLog.txt.0
     */
    public static final String COMMAND_TO_READ_WIFIMESH_SERVICE_DISABLE_LOGS = "cat /rdklogs/logs/MeshAgentLog.txt.0 | grep -i \"disabled\"";

    /**
     * Command to read wifiMesh enable status logs from /rdklogs/logs/MeshAgentLog.txt.0
     */
    public static final String COMMAND_TO_READ_WIFIMESH_SERVICE_ENABLE_LOGS = "cat /rdklogs/logs/MeshAgentLog.txt.0 | grep -i \"enabled\"";

    /**
     * System property to identify whether WebPA connectivity broken or not.
     */
    public static final String SYSTEM_PROPERTY_WEBPA_CONNECTIVITY_BROKEN = "WEBPA_CONNECTIVITY_BROKEN";
    
    /** Constant to hold the value to set firewall IPv4 to 'Minimum Security' */
    public static final String FIREWALL_IPV4_MINIMUM_SECURITY = "Low";
    
    /** Constant to hold the value to set firewall IPv4 to 'Maximum Security' */
    public static final String FIREWALL_IPV4_MAXIMUM_SECURITY = "High";

    /** Constant to hold 2.4GHZ index */
    public static final String RADIO_24_GHZ_INDEX = "10000";

    /** Constant to hold 5GHZ index */
    public static final String RADIO_5_GHZ_INDEX = "10100";

    /** inactive response of systemsctl */
    public static final String SYSTEMCTL_INACTIVE_RESPONSE = "inactive";

    /** active response of systemctl */
    public static final String SYSTEMCTL_ACTIVE_RESPONSE = "active";

    /** Command to know the status of wifi mesh service */
    public static final String COMMAND_TO_CHECK_THE_RUNNING_STATUS_OF_WIFIMESH_SERVICE = "systemctl status meshwifi.service";

    /**
     * Pattern to get the Active status of wifimesh service from systemctl response
     */
    public static final String PATTERN_TO_GET_STATUS_FROM_SYSTEMCTL_RESPONSE = "Active:\\s+(\\S+)";
    
    /** Constants to hold the bridge mode value bridge-static */
    public static final String CONSTANT_BRIDGE_STATIC = "bridge-static";

    /** The constant for checking router mode. */
    public static final String LAN_MANAGEMENT_MODE_ROUTER = "router";

    /** String value for Operating mode b/g/n */
    public static final String OPERATING_MODE_BGN = "b,g,n";

    /** String value for 1Mbps WiFi beacon rate */
    public static final String TEXT_ONE_MBPS = "1Mbps";

    /** String value for Operating mode g/n */
    public static final String OPERATING_MODE_GN = "g,n";

    /** String value for 6Mbps WiFi beacon rate */
    public static final String TEXT_SIX_MBPS = "6Mbps";

    /** String value for 2Mbps WiFi beacon rate */
    public static final String TEXT_TWO_MBPS = "2Mbps";

    /** CAT command. */
    public static final String CAT_COMMAND = "cat ";

    /** String constant 5 */
    public static final String STRING_5 = "5";
    
    /** String value for 100 */
    public static final String STRING_VALUE_HUNDRED = "100";

    /** pattern to get the 2GHz channel bandwidth */
    public static final String PATTERN_WIFI_2G_CHAN_WIDTH = "WiFi_config_2G_chan_width_split:(\\d+)";

    /** pattern to get the 5GHz channel bandwidth */
    public static final String PATTERN_WIFI_5G_CHAN_WIDTH = "WiFi_config_5G_chan_width_split:(\\d+)";

    /** channal bandwidth 20MHz */
    public static final String CHANNEL_WIDTH_20MHZ = "20MHz";

    /** channal bandwidth 40MHz */
    public static final String CHANNEL_WIDTH_40MHZ = "40MHz";

    /** channal bandwidth 80MHz */
    public static final String CHANNEL_WIDTH_80MHZ = "80MHz";

    /** Pattern matcher to obtain number value */
    public static final String PATTERN_MATCHER_GET_NUMBER = "(\\d+)";

    /** Character colon */
    public static Character CHARACTER_COLON = ':';
    
    /** fifty seconds in millisecond representation. */
    public static final long FIFTY_SECONDS_IN_MILLIS = 50 * ONE_SECOND_IN_MILLIS;

    /** os type windows for connected client */
    public static final String OS_TYPE_WINDOWS = "WINDOWS";

    /** os type windows for connected client */
    public static final String OS_TYPE_LINUX = "LINUX";

    /** Constant for Security mode None */
    public static final String SECURITY_MODE_NONE = "None";

    /** Constant for Security mode WPA2_PERSONAL */
    public static final String SECURITY_MODE_WPA2_PERSONAL = "WPA2-Personal";

    /** Constant for Security mode WPA2-Enterprise */
    public static final String SECURITY_MODE_WPA2_ENTERPRISE = "WPA2-Enterprise";

    /** Constant for Telemetry marker while changing the security mode */
    public static final String TELEMTRY_MARKER_FOR_WIFI_CONFIG_CHANGED = "\"RDKB_WIFI_CONFIG_CHANGED : Wifi security mode SETVALUE is Enabled\"";

    /** Constant to hold command to get wifi driver name */
    public static final String CMD_GET_WIFI_NAME = "netsh wlan show profiles |grep -i interface";

    /** Constant to hold pattern finder to get driver name */
    public static final String REGEX_GREP_WIFI_NAME = "interface\\s+(.*):";

    /** Constant to hold command to enable wifi driver */
    public static final String CMD_ENABLE_WIFI_DRIVER = "netsh interface set interface \"<REPLACE>\" enable";

    /** Constant to hold command to disable wifi driver */
    public static final String CMD_DISABLE_WIFI_DRIVER = "netsh interface set interface \"<REPLACE>\" disable";

    /** String used for replacing the value in other string */
    public static final String STRING_REPLACE = "<REPLACE>";

    /** String value for | symbol. */
    public static final String SYMBOL_PIPE = "|";

    /** Constant to hold Home Directory */
    public static final String HOME_DIR = "/home/";

    /** No Such Object available. */
    public static final String NO_SUCH_OBJECT_AVAILABLE = "No Such Object available on this agent at this OID";

    /** No Such Instance currently exists. */
    public static final String NO_SUCH_INSTANCE = "No Such Instance currently exists at this OID";

    /** String No Response From - error message */
    public static final String NO_RESPONSE_FROM = "No Response from(.*)";

    /** Command to get the disk usage value. */
    public static final String CMD_TAIL_1 = "tail -1";

    /** Constant to hold channel value as 36 */
    public static final String RADIO_CHANNEL_36 = "36";

    /** Constant to hold channel value as 48 */
    public static final String RADIO_CHANNEL_48 = "48";
  
    /** Process name rabid */
    public static final String PROCESS_RABID = "rabid";
    
    /** Command echo */
    public static final String CMD_ECHO = "echo ";
    
    /** Constant for HTTP_UPLOAD_LINK URL property */
    public static final String PROP_HTTP_UPLOAD_LINK_URL = "HTTP_UPLOAD_LINK";
    
    /** The constant for firmware download protocol 'http'. */
    public static final String FIRMWARE_DOWNLOAD_PROTOCOL_HTTP = "http";
    
    /** The partial property key for xconf ipv4 firmware location. */
    public static final String PROP_KEY_XCONF_FIRMWARE_LOCATION_DAC15 = "xconf.firmware.location.";
    
    /** Constant to hold the invalid firmware location */
    public static final String INVALID_FIRMWARE_LOCATION = "https://tbddummy.com/invalid/Images/";
    
    /** File path for /rdklogs/logs/xconf.txt.0 */
    public static final String RDKLOGS_LOGS_XCONF_TXT_0 = "/rdklogs/logs/xconf.txt.0";
    
    /** Property key for HTTP server IPv6 Address. */
    public static final String PROP_KEY_HTTP_SERVER_IPV6_ADDRESS = "http.server.ipv6.address";
        
    /** Identifies the beginning of image name in the telnet out put. */
    public static final String IDENTIFIER_FOR_BEGINNING_OF_IMAGE_NAME = "imagename";
    
    /** Command constant to grep for log strings in PAMlog.txt.0 */
    public static final String COMMAND_NTP_LOG_FILE = "/rdklogs/logs/PAMlog.txt.0";
    
    /** Constant to hold string containing echo with space */
    public static final String ECHO_WITH_SPACE = "echo ";
    
    /** Constant to hold text for connection test message from device */
    public static final String CONNECTION_TEST_MESSAGE = "test_connection";
    
    /** reboot reason for factory reset */
    public static final String REBOOT_REASON_FACTORY_RESET = "factory-reset";
    
    /** reboot reason for CM_variant_change */
    public static final String REBOOT_REASON_CM_VARIANT_CHANGE = "CM_variant_change";
    
    /** partner id for cox */
    public static final String PARTNER_ID_COX = "cox";
    
    /** WiFi region code USI */
    public static final String WIFI_POWER_LEVEL_USI = "USI";
    
    /** WiFi region code CAI */
    public static final String WIFI_POWER_LEVEL_CAI = "CAI";
    
    /** Simple Data Format - Data/Time in Log Messages */
    public static final String TIMESTAMP_FORMAT_LOG_MESSAGE = "yyMMdd-HH:mm:ss";
    
    /**
     * Map with Warehouse Wireless sequence OIDs and corresponding SNMP Data type
     */
    public static HashMap<BroadBandSnmpMib, SnmpDataType> WAREHOUSE_WIRELESS_SEQUENCE_OIDS_DATATYPE = new HashMap<BroadBandSnmpMib, SnmpDataType>() {
	{
	    put(BroadBandSnmpMib.ECM_STATUS_PRIVATE_WIFI_2_4_GHZ, SnmpDataType.INTEGER);
	    put(BroadBandSnmpMib.HOME_SECURITY_2_4_SSID_STATUS, SnmpDataType.INTEGER);
	    put(BroadBandSnmpMib.HOT_SPOT_2_4_SSID_STATUS, SnmpDataType.INTEGER);
	    put(BroadBandSnmpMib.LNF_2_4_SSID_STATUS, SnmpDataType.INTEGER);
	    put(BroadBandSnmpMib.ECM_STATUS_PRIVATE_WIFI_5_GHZ, SnmpDataType.INTEGER);
	    put(BroadBandSnmpMib.HOT_SPOT_5_SSID_STATUS, SnmpDataType.INTEGER);
	    put(BroadBandSnmpMib.LNF_5_SSID_STATUS, SnmpDataType.INTEGER);
	    put(BroadBandSnmpMib.ECM_PRIVATE_WIFI_SSID_2_4, SnmpDataType.STRING);
	    put(BroadBandSnmpMib.ECM_PRIVATE_WIFI_SSID_5, SnmpDataType.STRING);
	    put(BroadBandSnmpMib.ECM_PRIVATE_WIFI_2_4_PASSPHRASE, SnmpDataType.STRING);
	    put(BroadBandSnmpMib.ECM_PRIVATE_WIFI_5_PASSPHRASE, SnmpDataType.STRING);
	    put(BroadBandSnmpMib.ECM_PRIVATE_WIFI_2_4_WIRELESSPASS, SnmpDataType.STRING);
	    put(BroadBandSnmpMib.ECM_PRIVATE_WIFI_5_WIRELESSPASS, SnmpDataType.STRING);
	    put(BroadBandSnmpMib.ENABLE_DISABLE_WIFI_RADIO_2_4_GHZ, SnmpDataType.INTEGER);
	    put(BroadBandSnmpMib.ENABLE_DISABLE_WIFI_RADIO_5_GHZ, SnmpDataType.INTEGER);
	    put(BroadBandSnmpMib.ECM_WIFI_2_4_CHANNEL_INFO, SnmpDataType.UNSIGNED_INTEGER);
	    put(BroadBandSnmpMib.ECM_WIFI_5_CHANNEL_INFO, SnmpDataType.UNSIGNED_INTEGER);
	}
    };

    /** Pattern for ipv4 address from ifconfig output */
    public static final String INET_V4_ADDRESS_PATTERN = "inet +addr\\:((\\d+\\.){1,3}\\d+) +Bcast\\:(\\d+\\.){1,3}\\d+ +Mask\\:";

    /** String value of dollar symbol $. */
    public static final String STRING_DOLLAR_SIGN = "$";

    /** String value 1 */
    public static final String STRING_VALUE_ONE = "1";

    /**
     * Property name to get list of expected ethernet interface from linux client
     */
    public static final String PROP_KEY_TO_GET_EXPECTED_ETHERNET_INTERFACE_IN_LINUX_CLIENT = "ethernet.expected.interface.linux";

    /**
     * Property name to get list of expected wifi interface from linux client
     */
    public static final String PROP_KEY_TO_GET_EXPECTED_WIFI_INTERFACE_IN_LINUX_CLIENT = "wifi.expected.interface.linux";

    /** Constant for HTTPS URL */
    public static final String URL_HTTPS = "https://";

    /** Constant to hold google.com */
    public static final String PING_TO_GOOGLE = "google.com";

    /** Test Constant for 'www.google.com' */
    public static final String STRING_GOOGLE_HOST_ADDRESS = "www.google.com";

    /**
     * Holds header success message returned by curl command when accessing web
     */
    public static final String ACCESS_TO_URL_USING_CURL_HEADER_SUCCESS_MESSAGE = "HTTP/2 200";

    /**
     * Holds header success message returned by curl command when accessing web
     */
    public static final String ACCESS_TO_URL_USING_CURL_HEADER_SUCCESS_HTTP_1_1 = "HTTP/1.1 200";

    /** Holds success message returned by curl command when accessing web */
    public static final String ACCESS_TO_URL_USING_CURL_SUCCESS_MESSAGE = "Connected to";

    /** Holds error message when curl is not installed */
    public static final String CURL_NOT_INSTALLED_ERROR_MESSAGE = "curl: command not found";

    /** Holds message returned by curl command when ssl handshake failed */
    public static final String ACCESS_TO_URL_USING_CURL_CONNECTION_SSL_HANDSHAKE_FAILURE_MESSAGE = "SSL_ERROR_SYSCALL";

    /** Holds failure message returned by curl command when accessing web */
    public static final String ACCESS_TO_URL_USING_CURL_FAILURE_MESSAGE = "Failed to connect";

    /**
     * Holds message returned by curl command when web is not accessible even after the given time
     */
    public static final String ACCESS_TO_URL_USING_CURL_CONNECTION_TIMEOUT_MESSAGE = "Connection timed out";
    /**
     * Holds message returned by curl command when web is not accessible even after the given time
     */
    public static final String ACCESS_TO_URL_USING_CURL_OPERATION_TIMEOUT_MESSAGE = "Operation timed out";
    /**
     * Holds message returned by curl command when its not able to resolve the IP Address of the domain
     */
    public static final String ACCESS_TO_URL_USING_CURL_RESOLVING_TIMEOUT_MESSAGE = "Resolving timed out";

    /** Percentage value 100. */
    public static final int PERCENTAGE_VALUE_HUNDRED = 100;

    /** Constant for ping error message TTL Expired in Transit */
    public static final String PING_ERR_MSG_TTL_EXPIRED_IN_TRANSIT = "TTL Expired in Transit";

    /** Constant for ping error message Destination Host Unreachable */
    public static final String PING_ERR_MSG_DEST_HOST_UNREACHABLE = "Destination host unreachable";

    /** Constant for ping error message Request Timed Out */
    public static final String PING_ERR_MSG_REQUEST_TIMED_OUT = "Request timed out";

    /** Constant for ping error message Unknown Host */
    public static final String PING_ERR_MSG_UNKNOWN_HOST = "Unknown host";

    /** Constant for ping error message Ping request could not find host */
    public static final String PING_ERR_MSG_COULD_NOT_FIND_HOST = "Ping request could not find host";

    /** Constant for ping error message Request timeout for icmp_seq */
    public static final String PING_ERR_MSG_REQ_TIME_OUT_FOR_ICMP_SEQ = "Request timeout for icmp_seq";

    /** Constant for Replacing this calue by set value */
    public static final String TELEMTRY_MARKER_SET_VALUE = "SETVALUE";

    /** Prefix length :/64 for IPv6 address */
    public static final String PREFIX_LENGTH_64 = ":/64";

    /** Pattern Matcher for multiple spaces */
    public static final String PATTERN_MATCHER_FOR_MULTIPLE_SPACES = "\\s+";

    /** regular expression to get interface name from connected client */
    public static final String PATTERN_MATCHER_WIFI_INTERFACE_CONN_CLIENT_LINUX = "([\\S]*)wifi";

    /**
     * Test Constant for average response time threshold while pinging a host address
     */
    public static final int PING_RESPONSE_AVG_TIME_THRESHOLD = 30;

    /** Constant to store integer 0 as string */
    public static final String STRING_ZERO = "0";

    /** Test Constants for PSM DB Sync */
    /** Boolean suffix to set a parameter using Dmcli */
    public static final String DMCLI_SUFFIX_TO_SET_STRING_PARAMETER = "string";

    /** Boolean suffix to set a parameter using Dmcli */
    public static final String DMCLI_SUFFIX_TO_SET_INT_PARAMETER = "int";

    /** Boolean suffix to set a parameter using Dmcli */
    public static final String DMCLI_SUFFIX_TO_SET_UINT_PARAMETER = "uint";

    /** Boolean suffix to set a parameter using Dmcli */
    public static final String DMCLI_SUFFIX_TO_SET_BOOLEAN_PARAMETER = "bool";

    /** Boolean suffix to set a parameter using Dmcli */
    public static final String DMCLI_SUFFIX_TO_SET_DATETIME_PARAMETER = "dateTime";

    /** Boolean suffix to set a parameter using Dmcli */
    public static final String DMCLI_SUFFIX_TO_SET_INVALID_PARAMETER = "invalid";

    /** Dmcli prefix to set a parameter */
    public static final String DMCLI_PREFIX_TO_SET_PARAMETER = "dmcli eRT setv";

    /**
     * Test Constant for Pattern Matcher to retrieve the Average Time from Ping response.
     */
    public static final String PATTERN_MATCHER_PING_RESPONSE_AVG_TIME = ".*Average\\s+=\\s+(\\d+)ms";

    /** String value 3 */
    public static final String STRING_VALUE_THREE = "3";
    
    /** String value 3 */
    public static final String STRING_VALUE_FOUR = "4";
    
    /** constant for PingInterval */
    public static final String CONSTANT_DEFAULT_PING_INTERVAL = "60";
    
    /** constant for PingRespWaitTime */
    public static final String CONSTANT_PING_RESP_WAIT_TIME = "1000";
    
    /** constant for ResourceUsageComputeWindow */
    public static final String CONSTANT_RESOURCE_USAGE_COMPUTE_WINDOW = "15";
    
    /** constant for PingInterval for 1441 mins */
    public static final String CONSTANT_PING_INTERVAL_1441 = "1441";
    
    /** constant for PingInterval for 14 mins */
    public static final String CONSTANT_PING_INTERVAL_14 = "14";
    
    /** Default max reset count key names */
    public static final String DEFAULT_MAX_RESET_COUNT_FOR_RELEASE_BUILD = "default.max.reset.count.release.build";

    public static final String DEFAULT_MAX_RESET_COUNT = "default.max.reset.count";
    
    /** constant for AvgCPUThreshold */
    public static final String CONSTANT_AVG_CPU_THRESHOLD = "100";
    
    /** constant for avgMemoryThreshold */
    public static final String CONSTANT_AVG_MEMORY_THRESHOLD = "100";
    
    /** String value for factory resetting the box */
    public static final String STRING_FOR_FACTORY_RESET_OF_THE_DEVICE = "Router,Wifi,VoIP,Dect,MoCA";
    
    /** Equals delimiter. */
    public static final String DELIMITER_COLON = ":";
    
    /** Test Constants for Factory Reset */
    /** Property Key for 2.4GHz Private SSID */
    public static final String PROP_KEY_2_4_PRIVATE_SSID = "private.ssid.value.2.4";

    /** Property Key for 2.4GHz Private SSID */
    public static final String PROP_KEY_5_PRIVATE_SSID = "private.ssid.value.5";

    /** Property Key for 2.4GHz Private SSID Password */
    public static final String PROP_KEY_2_4_PRIVATE_SSID_PWD = "private.ssid.password.value.2.4";

    /** Property Key for 2.4GHz Private SSID Password */
    public static final String PROP_KEY_5_PRIVATE_SSID_PWD = "private.ssid.password.value.5";
	
	  
   /** String double quote */
   public static final String TEXT_DOUBLE_QUOTE = "\"";
   
   /** String value for greater symbol */
   public static final String STRING_GREATER_SYMBOL = ">";
   
   /** Constant holding the location of swupdate.conf file. */
   public static final String SOFTWARE_UPDATE_CONF_FILE = "/opt/swupdate.conf";
   
   /** Command pidof */
   public static final String CMD_PID_OF = "pidof";
   
   /** String to store CCSP invalid parameter name */
   public static final String ERROR_MESSAGE_DMCLI_PARAMETER_NAME = "CCSP_ERR_INVALID_PARAMETER_NAME";
   
   
   /** String to store CCSP_ERR_NOT_CONNECT */
   public static final String PATTERN_DMCLIPARAMETER_CCSP_ERR_NOT_CONNECT = "CCSP_ERR_NOT_CONNECT";
   
   /** Variable to store dummy last reboot reason */
   public static final String SAMPLE_LAST_REBOOT_REASON = "firewall-test";   
  
   /** Linux command to sync. */
   public static final String CMD_SYNC = "sync";
   
   /** String to hold No Response output for SNMP */
   public static final String NO_RESPONSE_SNMP = "No Response";
   
   /** Integer that holds the value 60000 */
   public static final int INTERGER_CONSTANT_60000 = 60000;
   
   public enum syndicationPartnerID {
	PARTNER_DUMMY("dummy"),
	PARTNER_COX("cox"),
	PARTNER_ROGERS("rogers"),
	PARTNER_SHAW("shaw"),
	PARTNER_VIDEOTRON("videotron");

	String paramName;

	public String getParamName() {
	    return paramName;
	}

	public void setParamName(String paramName) {
	    this.paramName = paramName;
	}

	syndicationPartnerID(String name) {
	    this.paramName = name;
	}
   }
   

   
   
   /** Number Eight */
   public static final int EIGHT_NUMBER = 8;   

   /** Integer value 13 */
   public static final Integer INTEGER_VALUE_13 = 13;
   
   /** Constants for XBB Telemetry Marker */
   /** Symbol - HYPHEN */
   public static final String SYMBOL_HYPHEN = "-";
   
   /** Constant for TR-181 Node Reference */
   public static final String TR181_NODE_REF = "{i}";
   
   /** String constant 4 */
   public static final String STRING_CONSTANT_4 = "4";
   
   /** String value 6 */
   public static final String STRING_6 = "6";
   
   /** Constant for SSID prefix */
   public static final String CONSTANT_SSID_NAME_PREFIX = "XFSETUP";
     
   /** WiFi region code CAO */
   public static final String WIFI_POWER_LEVEL_CAO = "CAO";
   
   /** WiFi region code USO */
   public static final String WIFI_POWER_LEVEL_USO = "USO";
      
   /** The array list of ipv4 fragmentation ip packets rules */
   public static final List<String> LIST_IPV_4_FRAG_IP_PKTS_RULES = new ArrayList<String>() {
	{
	    add("INPUT");
	    add("FORWARD");
	    add("FRAG_DROP");
	    add("erouter0");
	    add("lan");
	}
   };
   
   /** The array list of ipv4 and ipv6 flood detect rules */
   public static final List<String> LIST_IPV_4_6_FLOOD_DETECT_RULES = new ArrayList<String>() {
	{
	    add("DOS");
	    add("INPUT");
	    add("FORWARD");
	    add("DOS_FWD");
	    add("DOS_TCP");
	    add("DOS_UDP");
	    add("DOS_ICMP");
	    add("DOS_ICMP_REQUEST");
	    add("DOS_ICMP_REPLY");
	    add("DOS_ICMP_OTHER");
	    add("DOS_DROP");
	}
   };

   /** The array list of ipv4 port scan rules */
   public static final List<String> LIST_IPV_4_PORT_SCAN_RULES = new ArrayList<String>() {
	{
	    add("PORT_SCAN_CHK");
	    add("PORT_SCAN_DROP");
	    add("INPUT");
	    add("FORWARD");
	    add("erouter0");
	    add("lo");
	    add("udp");
	    add("tcp");
	    add("DROP");
	}
   };
   
   /** The array list of ipv6 fragmentation ip packets rules */
   public static final List<String> LIST_IPV_6_FRAG_IP_PKTS_RULES = new ArrayList<String>() {
	{
	    add("FRAG_DROP");
	    add("INPUT");
	    add("FORWARD");
	    add("DROP");
	}
   };
   
   /** The array list of ipv6 port scan rules */
   public static final List<String> LIST_IPV_6_PORT_SCAN_RULES = new ArrayList<String>() {
	{
	    add("PORT_SCAN_CHK");
	    add("INPUT");
	    add("FORWARD");
	    add("erouter0");
	    add("lo");
	}
   };
   
   /** Test Constants for Library Security Test */
   /** Telnet Library */
   public static final String LIB_TELNET = "telnet";
   /** Rlogin Library */
   public static final String LIB_RLOGIN = "rlogin";
   /** NFS Library */
   public static final String LIB_NFS = "nfs";

   /** Pattern matcher - No Such File or Directory */
   public static final String PATTERN_MATCHER_FIND_NO_SUCH_FILE = "find:\\s+.*:\\s+No such file or directory.*";
   
   /**
    * Map of Wi-Fi SSID configuration details ( Private SSID ).
    * SSID status includes the Enable status, status, name, passphrase and security mode).
    */
   @SuppressWarnings("serial")
   public static final Map<String, List<String>> DEVICE_WIFI_SSID_CONFIG_DETAILS = new LinkedHashMap<String, List<String>>() {
	{
	    put("private_2ghz", new ArrayList<String>() {
		{
		    add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_NAME);
		    add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_ENABLED_STATUS);
		    add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_2_4GHZ_PRIVATE_SSID);
		    add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2GHZ_SECURITY_KEYPASSPHRASE);
		    add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_PRIVATE_SECURITY_MODEENABLED);
		}
	    });
	    put("private_5ghz", new ArrayList<String>() {
		{
		    add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_ENABLED_STATUS);
		    add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_NAME);
		    add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5GHZ_SECURITY_KEYPASSPHRASE);
		    add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_PRIVATE_SECURITY_MODEENABLED);
		    add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_5GHZ_PRIVATE_SSID);
		}
	    });
	    put("lnf_secure_2ghz", new ArrayList<String>() {
		{
		    add("Device.WiFi.SSID.10006.SSID");
		    add("Device.WiFi.SSID.10006.Enable");
		    add("Device.WiFi.SSID.10006.Status");
		    add("Device.WiFi.AccessPoint.10006.Security.KeyPassphrase");
		    add("Device.WiFi.AccessPoint.10006.Security.ModeEnabled");
		}
	    });
	    put("lnf_secure_5ghz", new ArrayList<String>() {
		{
		    add("Device.WiFi.SSID.10106.SSID");
		    add("Device.WiFi.SSID.10106.Enable");
		    add("Device.WiFi.SSID.10106.Status");
		    add("Device.WiFi.AccessPoint.10106.Security.KeyPassphrase");
		    add("Device.WiFi.AccessPoint.10106.Security.ModeEnabled");
		}
	    });
	    put("lnf_psk_2ghz", new ArrayList<String>() {
		{
		    add("Device.WiFi.SSID.10004.SSID");
		    add("Device.WiFi.SSID.10004.Enable");
		    add("Device.WiFi.SSID.10004.Status");
		    add("Device.WiFi.AccessPoint.10004.Security.KeyPassphrase");
		    add("Device.WiFi.AccessPoint.10004.Security.ModeEnabled");
		}
	    });
	    put("lnf_psk_5ghz", new ArrayList<String>() {
		{
		    add("Device.WiFi.SSID.10104.SSID");
		    add("Device.WiFi.SSID.10104.Enable");
		    add("Device.WiFi.SSID.10104.Status");
		    add("Device.WiFi.AccessPoint.10104.Security.KeyPassphrase");
		    add("Device.WiFi.AccessPoint.10104.Security.ModeEnabled");
		}
	    });
	    put("meshwifi_2ghz", new ArrayList<String>() {
		{
		    add("Device.WiFi.SSID.10007.SSID");
		    add("Device.WiFi.SSID.10007.Enable");
		    add("Device.WiFi.SSID.10007.Status");
		    add("Device.WiFi.AccessPoint.10007.Security.KeyPassphrase");
		    add("Device.WiFi.AccessPoint.10007.Security.ModeEnabled");
		}
	    });
	    put("meshwifi_5ghz", new ArrayList<String>() {
		{
		    add("Device.WiFi.SSID.10107.SSID");
		    add("Device.WiFi.SSID.10107.Enable");
		    add("Device.WiFi.SSID.10107.Status");
		    add("Device.WiFi.AccessPoint.10107.Security.KeyPassphrase");
		    add("Device.WiFi.AccessPoint.10107.Security.ModeEnabled");
		}
	    });
	}
   };
   
   /** Constant to hold LAN local IP */
   public static final String LAN_LOCAL_IP = "192.168.0.1";
   
   /** Constant to hold LAN subnet mask */
   public static final String LAN_SUBNET_MASK_DEFAULT = "255.255.255.0";
   
   /** Constant to hold LAN start IP */
   public static final String LAN_START_IP_ADDRESS = "192.168.0.2";
   
   /** Constant to hold DHCP end IP address */
   public static final String DHCP_END_IP_ADDRESS_DEFAULT = "192.168.0.253";
   
   /** Constant to get RFC value from rfc_configdata.txt */
   public static final String COMMAND_TO_RFC_VALUE_CONFIG_FILE = "cat /tmp/rfc_configdata.txt";
   
   /** The constant holds the quick test status - FAILURE. */
   public static final String QUICK_TEST_FAILURE = "FAILURE";
   
   /** The constant for test status - BUILD VERIFICATION FAILED. */
   public static final String TEST_STATUS_BUILD_VERIFICATION_FAILED = "BUILD VERIFICATION FAILED";
   
   /** The constant for test status - BUILD SUCCESS. */
   public static final String TEST_STATUS_BUILD_VERIFICATION_SUCCEEDED = "BUILD VERIFICATION SUCCEEDED";
   
   /**
    * The constant holds the quick test status - SUCCESS.Do not use for checking the test status
    */
   public static final String QUICK_TEST_SUCCESS = "COMPLETED";
   
   /** The constant holds the quick test status - BOX_NOT_AVAILABLE. */
   public static final String QUICK_TEST_FAILURE_BOX_NOT_AVAILABLE = "BOX_NOT_AVAILABLE";
   
   /** Pattern Matcher to retrieve the timestamp from log message */
   public static final String PATTERN_MATCHER_TIMESTAMP_LOG_MESSAGE = "([^\\s]+)";
   
   /** Pattern to retrieve ipv4 address */
   public static final String PATTERN_TO_RETRIEVE_IPV4_ADDRESS_FROM_NSLOOKUP_FACEBOOK = ".*facebook.com\\s*Address\\s*\\d*:\\s+(\\d+\\.\\d+\\.\\d+\\.\\d+)\\s*(.*)";
  
   /** Constant to hold pattern finder for time stamp */
   public static final String PATTERN_FINDER_TO_GREP_TIMESTAMP = "\\d+-(\\d+:\\d+:\\d+)";
   
   /** wait for 16 minutes */
   public static final long SIXTEEN_MINUTES_IN_MILLIS = FIFTEEN_MINUTES_IN_MILLIS + ONE_MINUTE_IN_MILLIS;

   /**
    * Linux command to list all running process on Broadband devices.
    */
   public static final String CMD_GET_PROCESS_DETAILS = "ps -ww | sed '/pidof/d'";
  
   public static final String[] REPLACE = { "", "", "", "", "", "", "", "",
		"", "", "" };
   
   /** The constant holds the error message for 'CDL' failure. */
   public static final String BUILD_NOT_AVAILABLE = "Build '<buildImageName>' is not available in CDL server, "
	    + "check whether the jenkins status in portal is success and build is uploaded to cdl servers";
   
   /** Test Constants for WebPA Telemetry */
   /** Process Name for WebPA */
   public static final String PROCESS_NAME_WEBPA = "webpa";
   
   /** Constant to hold Webpa version Pattern Matcher */
   public static final String WEBPA_VERSION_PATTERN_MATCHER = "WEBPA\\-(\\d+)\\.(\\d+)-.*";

   /** Constant to hold Configparamgen version Pattern Matcher */
   public static final String CONFIGPARAMGEN_VERSION_PATTERN_MATCHER = "Usage\\(v(.*)\\)\\s*:";
   
   /** Constant to hold attenuation for 5 GHZ */
   public static final Double CHANNEL_ATTENUATION_5_GHZ = 0.0;
   
   /** sprint keyword to search in build name */
   public static final String STRING_SPRINT_IN_BUILD_NAME = "sprint";
   
   /** String to store fimrware version branch stable */
   public static final String FIRMWARE_VERSION_BRANCH_STABLE = "stable";
   
   /** Feature keyword to search in build name */
   public static final String STRING_FEATURE_IN_BUILD_NAME = "feature";

   /** Constant to hold double configparamgen Version as 3.7 */
   public static final double CONFIGPARAMGEN_VERSION_3_7 = 3.7;

   /** Constant to hold double Firmware Version as 4.4 */
   public static final double FIRMWARE_VERSION_4_4 = 4.4;
   
   /** Constant to hold double configparamgen Version as 2.17 */
   public static final double CONFIGPARAMGEN_VERSION_2_17 = 2.17;
   
   /** Constant to hold core version from Regular release build Pattern Matcher */
   public static final String CORE_VERSION_FROM_REGULAR_RELEASE_BUILD_PATTERN_MATCHER = "_(\\d+.\\d+)";
   
   /** Constant to hold Regular release build with 2 fields in core version Pattern Matcher */
   public static final String PROPER_RELEASE_BUILD_PATTERN_MATCHER = "_(\\d+.\\d+)+p(\\d+)+s(\\d+)_";
   
   /** Constant to hold Regular release build Pattern Matcher */
   public static final String REGULAR_RELEASE_BUILD_PATTERN_MATCHER = "_(\\d+.\\d)+p(\\d+)+s(\\d+)_";
   
   /** Constant to hold p version from Regular release build Pattern Matcher */
   public static final String P_VERSION_FROM_REGULAR_RELEASE_BUILD_PATTERN_MATCHER = "p(\\d+)s";
   
   /** Constant to hold s version from Regular release build Pattern Matcher */
   public static final String S_VERSION_FROM_REGULAR_RELEASE_BUILD_PATTERN_MATCHER = "s(\\d+)_";
   
   /** Constant to hold spin release build Pattern Matcher */
   public static final String SPIN_BUILD_PATTERN_MATCHER = "_(\\d+.\\d)+s(\\d+)_";
   
   /** Constant to hold spin release build with 2 fields in core version Pattern Matcher */
   public static final String PROPER_SPIN_BUILD_PATTERN_MATCHER = "_(\\d+.\\d+)+s(\\d+)_";
   
   /** The constant for failure in 5 GHz Private SSID after CDL. */
   public static final String TEST_STATUS_PRIVATE_SSID_5_GHZ_NOT_ENABLED_AFTER_CDL = "Device is not properly enabled with private 5GHz SSID after CDL.";
   
   /** The constant for failure in 2.4 GHz Private SSID after CDL. */
   public static final String TEST_STATUS_PRIVATE_SSID_2_4_GHZ_NOT_ENABLED_AFTER_CDL = "Device is not properly enabled with private 2.4GHz SSID after CDL.";
   
   /** The constant for failure in bridge mode status after CDL. */
   public static final String TEST_STATUS_BRIDGE_MODE_AFTER_CDL = "Device went to Bridge mode after CDL";
   
   /** The constant for failure in captive portal mode. */
   public static final String TEST_STATUS_CAPTIVE_PORTAL_AFTER_CDL = "Device went to Captive Portal mode after CDL";
   
   /** The constant for the Webpa Communication error. */
   public static final String TEST_STATUS_WEBPA_COMMUNICATION_ERROR = "Device is not responding to WebPA command after cdl";
   
   /**
    * The constant for failure in getting ccsp components list. initialization.
    */
   public static final String TEST_STATUS_CCSP_COMPONENTS_NOT_INITIALIZED_ERROR = "All CCSP processes are not initialized properly <console> after cdl. ";
   
   /** The constant for failure in SNMP. */
   public static final String TEST_STATUS_SNMP_FAILURE = "Device is not responding to snmp request after cdl or sysdescr mib is returning wrong image version. "
	    + "Expected version : <buildImageName>";
   
   /** Default inet6 address pattern from log */
   public static final String INET_V6_ADDRESS_PATTERN = "inet6 addr:\\s*(\\w+:\\w+:\\w+:\\w+:\\w+:\\w+:\\w+:\\w+)\\/\\d+\\s*Scope:Global";
   
   /**
    * The constant for failure in getting DHCPV6 address during brlan0 initialization.
    */
   public static final String TEST_STATUS_BRLAN0_DHCPV6_ERROR = "brlan0 interface is not initialized properly with IPV6 DHCP address after cdl";
   
   /** The constant holding a command to ifconfig brlan. */
   public static final String IFCONFIG_BRLAN = "/sbin/ifconfig brlan0";
   
   /**
    * The constant for failure in getting DHCPV4 address during brlan0 initialization.
    */
   public static final String TEST_STATUS_BRLAN0_DHCPV4_ERROR = "brlan0 interface is not initialized properly with IPV4 DHCP address after cdl";
   
   /**
    * Regular expression for retrieving device MAC Address from ifconfig command
    */
   public static final String PATTERN_DEVICE_MAC_ADDRESS_FROM_IFCONFIG_CMD = "HWaddr\\s+(\\w+:\\w+:\\w+:\\w+:\\w+:\\w+)";
   
   /**
    * The constant for failure in getting WAN MAC address during erouter0 initialization .
    */
   public static final String TEST_STATUS_EROUTER0_MAC_ADDRESS_ERROR = "erouter0 MAC address not matched with WAN MAC address after cdl. "
	    + "Expected WAN MAC address : <wanMacAddress>, but Actual : <erouter0MacAddress>";
   
   /**
    * The constant for failure in getting IPV6 address during erouter0 initialization .
    */
   public static final String TEST_STATUS_EROUTER0_IPV6_ERROR = "erouter0 interface is not initialized properly with IPv6 DHCP address after cdl";
   
   /**
    * The constant for failure in getting IPV4 address during erouter0 initialization.
    */
   public static final String TEST_STATUS_EROUTER0_IPV4_ERROR = "erouter0 interface is not initialized properly with IPV4 DHCP address after cdl";
   
   /** The constant for test status - BOX_NOT_FLASHED_WITH_REQUESTED_IMAGE. */
   public static final String TEST_STATUS_BOX_NOT_FLASHED_WITH_REQUESTED_IMAGE = "Device is not booting up with the requested image or "
	    + "version.txt is providing wrong image name. Expected image after cdl : <buildImageName>";
   

   /** The constant for test status - XCONF_CDL_TRIGGER_FAILED. */
   public static final String TEST_STATUS_XCONF_CDL_TRIGGER_FAILED = "TR-181/WebPA HTTP, XCONF HTTP & DOCSIS SNMP code download trigger for build '<buildImageName>' failed, "
	    + "Please check the device whether it accept any code download/expected log messages are fine";
   
   /** Allowed number of system descriptor field as per CableLab specification. */
   public static final int ALLOWED_NUMBER_OF_SYS_DESCRIPTOR_FILED = 5;
   
   /** LEFT_SHIFT_OPERATOR **/
   public static final String LEFT_SHIFT_OPERATOR = "<<";
   
   /** RIGHT SHIFT OPERATOR **/
   public static final String RIGHT_SHIFT_OPERATOR = ">>";
   
   /** Key to get software version from system Descriptor. */
   public static final String KEY_SYS_DESCR_SOFTWARE_VERSION = "SW_REV";
   
   /** Constant to hold boolean value FALSE */
   public static final Boolean BOOLEAN_VALUE_FALSE = false;
   
   /** Key to get vendor details from system Descriptor. */
   public static final String KEY_SYS_DESCR_VENDOR = "VENDOR";
   
   /** Key to get device model from system Descriptor. */
   public static final String KEY_SYS_DESCR_MODEL = "MODEL";
   
   /** Key to get device boot loader version from system Descriptor. */
   public static final String KEY_SYS_DESCR_BOOT_LOADER_VERSION = "BOOTR";
   
   /** Key to get Hardware revision from system Descriptor. */
   public static final String KEY_SYS_DESCR_HARDWARE_VERSION = "HW_REV";
   
   /** Constant to hold boolean value true */
   public static final Boolean BOOLEAN_VALUE_TRUE = true;
   /** symbol for + */
   public static final String SYMBOL_PLUS = "+";
   /** RFC feature name SNMPv3 */
   public static final String SNMPV3 = "SNMPV3";
   

   /** Test constant for string configurableSSH */
   public static final String CONFIGURABLE_SSH = "configurableSSH";
   

   /** stb properties key for payload data to enable configurable ssh rfc in xconf to enable */
   public static final String PROP_KEY_PAYLOAD_ENABLE_CONFIGURABLE_SSH = "rfc.configurablessh.enable.payload";
   

   /** Log string list of ips for configurable SSH in stb.properties file */
   public static final String PROP_KEY_SSH_WHITELIST_IPS = "rfc.configurablessh.iplist";
   
   public static final String PROP_KEY_NON_WHITE_LISTED_JUMP_SERVER_IPV6 = "wl.server.ip.ipv6";
   
   /** stb properties key for payload data to disable configurable ssh rfc in xconf to disable */
   public static final String PROP_KEY_PAYLOAD_DISABLE_CONFIGURABLE_SSH = "rfc.configurablessh.disable.payload";
   
   /** RFC feature name IDS */
   public static final String FEATURE_NAME_IDS = "IDS";
   
   /** Pay load data for IDS enable */
   public static final String PROP_KEY_PAYLOAD_IDS_ENABLE = "rfc.ids.enable";
   

   /** Pay load data for IDS disable */
   public static final String PROP_KEY_PAYLOAD_IDS_DISABLE = "rfc.ids.disable";
   
   /** Test constant for string FirewallPort */
   public static final String FIREWALL_PORT = "FirewallPort";
   
   /** stb properties key for payload data to enable configurable ssh rfc in xconf to enable */
   public static final String PROP_KEY_PAYLOAD_ENABLE_FIREWALL_PORT = "rfc.firewall.enable";

   /** stb properties key for payload data to disable configurable ssh rfc in xconf to disable */
   public static final String PROP_KEY_PAYLOAD_DISABLE_FIREWALL_PORT = "rfc.firewall.disable";
   
   /** RFC feature name IDS */
   public static final String RFC_FEATURE_NAME_IDS1 = "IDS1";
   
   /** Pay load data for IDS enable */
   public static final String PROP_KEY_PAYLOAD_IDS1_ENABLE = "rfc.ids1.enable";

   /** Pay load data for IDS disable */
   public static final String PROP_KEY_PAYLOAD_IDS1_DISABLE = "rfc.ids1.disable";
   
   /** String constant to store configurable telemetry feature name value */
   public static final String CONFIGURABLE_TELEMETRY = "configurableTelemetry";
   
   /** Property key for rfc payload data in stb.props to configure unique telemetry tag */
   public static final String PROP_KEY_CONFIGURABLE_TELEMETRY_PAYLOAD = "rfc.configurabletelemetrytag.payload";
   
   /** Test constant for string configurableSSH */
   public static final String FINGER_PRINT = "FingerPrint";

   /**
    * stb properties key for payload data to enable configurable rfc fingerprint to enable
    **/
   public static final String PROP_KEY_PAYLOAD_ENABLE_FINGER_PRINT = "rfc.fingerprint.enable";

   /**
    * stb properties key for payload data to disable configurable rfc fingerprint to disable
    **/
   public static final String PROP_KEY_PAYLOAD_DISABLE_FINGER_PRINT = "rfc.fingerprint.disable";
   
   /** Test constant for string configurableSSH */
   public static final String SOFT_FLOWD = "SoftFlowd";

   /**
    * stb properties key for payload data to enable configurable rfc softflowd too enable
    **/
   public static final String PROP_KEY_PAYLOAD_ENABLE_SOFTFLOWD = "rfc.softFlowd.enable";

   /**
    * stb properties key for payload data to disable configurable rfc softflowd to disable
    **/
   public static final String PROP_KEY_PAYLOAD_DISABLE_SOFTFLOWD = "rfc.softFlowd.disable";
   
   /** Test constant for string configurableSSH */
   public static final String CODEBIG_FIRST = "CodebigFirst";

   /**
    * stb properties key for payload data to enable configurable rfc softflowd too enable
    **/
   public static final String PROP_KEY_PAYLOAD_ENABLE_CODEBIG_FIRST = "rfc.CodebigFirst.enable";

   /**
    * stb properties key for payload data to disable configurable rfc softflowd to disable
    **/
   public static final String PROP_KEY_PAYLOAD_DISABLE_CODEBIG_FIRST = "rfc.CodebigFirst.disable";
   
   /** String constant to store Secure Upload feature name value */
   public static final String SECURE_UPLOAD = "SecureUpload";
   
   /** Property key to read payload to enable/disable encrypt cloud upload feature */
   public static final String PROP_KEY_TO_ENABLE_DISABLE_CLOUD_UPLOAD_ENCRYPTION = "rfc.enable.disable.encrypt.cloud.upload";

   /** String to change the status of encrypt cloud upload feature */
   public static final String STRING_TO_CHANGE_ENCRYPT_CLOUD_UPLOAD_STATUS = "<ENCRYPT_CLOUD_UPLOAD_STATUS>";
   
   /** stb property key for SNMPv3 enable */
   public static final String PROP_KEY_PAYLOAD_SNMPV3_ENABLE = "rfc.payload.snmpv3.enable";

   /** stb property key for SNMPv3 disable */
   public static final String PROP_KEY_PAYLOAD_SNMPV3_DISABLE = "rfc.payload.snmpv3.disable";
   
   /** RFC feature name password failure indication */
   public static final String PWD_FAILURE = "PWD_FAILURE";

   /** STB property file for pwd_failure enable */
   public static final String PROP_KEY_PAYLOAD_PWD_FAILURE_ENABLE = "rfc.payload.pwd_failure.enable";

   /** STB property file for pwd_failure disable */
   public static final String PROP_KEY_PAYLOAD_PWD_FAILURE_DISABLE = "rfc.payload.pwd_failure.disable";
   
   /** constant to hold stb prop name for CWMP */
   public static final String CWMP = "RFC_Versioning_Check";
   
   /** String constant to hold prop key value to enable CWMP */
   public static final String PROP_KEY_PAYLOAD_CWMP_ENABLE = "rfc.cwmp.enable.payload";

   /** String constant to hold prop key value for CWMP disable */
   public static final String PROP_KEY_PAYLOAD_CWMP_DISABLE = "rfc.cwmp.disable.payload";
   
   /** RFC feature name FAN */
   public static final String FEATURE_NAME_FAN = "FAN";

   /** Pay load data for Fan.Maxoverride enable */
   public static final String PROP_KEY_PAYLOAD_FAN_MAXOVERRIDE_ENABLE = "rfc.fan.enable";

   /** Pay load data for Fan.Maxoverride disable */
   public static final String PROP_KEY_PAYLOAD_FAN_MAXOVERRIDE_DISABLE = "rfc.fan.disable";
   
   /** Bootstrap config payload for rfc */
   public static final String PROPERTY_KEY_BOOTSTRAP = "rfc.bootstrap.payload";
   
   /** RFC feature name FORWARD SSH */
   public static final String FEATURE_NAME_FORWARD_SSH = "FORWARD_SSH_TEST";
   
   /** Pay load data for ForwardSSH enable */
   public static final String PROP_KEY_PAYLOAD_FORWARD_SSH_ENABLE = "rfc.forwardssh.enable";

   /** Pay load data for ForwardSSH disable */
   public static final String PROP_KEY_PAYLOAD_FORWARD_SSH_DISABLE = "rfc.forwardssh.disable";
  
   /** Pay load data for CPU memory Fragmentation */
   public static final String PROP_KEY_PAYLOAD_CPU_MEMORY_FRAGMENTATION = "rfc.payload.CPUMemFrag";
   
   /** RFC feature name for Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.PrivacyProtection.Enable */
   public static final String FEATURE_NAME_PRIVACY_PROTECTION_ENABLE = "PRIVACY_PROTECTION_ENABLE";
   
   /** Pay load data for Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.PrivacyProtection.Enable true */
   public static final String PROP_KEY_PAYLOAD_PRIVACY_PROTECTION_ENABLE = "rfc.privacy.protection.enable";

   /** Pay load data for Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.PrivacyProtection.Enable false */
   public static final String PROP_KEY_PAYLOAD_PRIVACY_PROTECTION_DISABLE = "rfc.privacy.protection.disable";
   
   /** String variable to store snmpv3 dk kick start table rfc feature */
   public static final String SNMPV3_DH_KICKSTART_TABLE_RFC_FEATURE = "Snmpv3DHKickstartTable";
   
   /** Pay load data for security snmpv3 payload enable */
   public static final String PROP_KEY_PAYLOAD_CM_SNMPV3_PAYLOAD_ENABLE = "rfc.cm.snmpv3.payload.enable";

   /** Pay load data for security snmpv3 payload disable */
   public static final String PROP_KEY_PAYLOAD_CM_SNMPV3_PAYLOAD_DISABLE = "rfc.cm.snmpv3.payload.disable";
   
   /** Feature name for Telemetry mLTS DCMUpload rfc configuration */
   public static final String FEATURE_NAME_MTLSDCMUPLOAD_CONFIG = "MTLSDCMUPLOAD_CONFIG";
   
   /** Pay load data for Telemetry mLTS DCMUpload rfc enable */
   public static final String PROP_KEY_PAYLOAD_MTLSDCMUPLOAD_ENABLE = "rfc.mtlsdcmupload.enable";

   /** Pay load data for Telemetry mLTS DCMUpload rfc disable */
   public static final String PROP_KEY_PAYLOAD_MTLSDCMUPLOAD_DISABLE = "rfc.mtlsdcmupload.disable";
   
   /** RFC feature name for Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.newNTP.Enable */
   public static final String FEATURE_NAME_NTP_TIME_SERVER = "NEW_NTP_UPDATE";

   /** Pay load data for Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.newNTP.Enable true */
   public static final String PROP_KEY_PAYLOAD_NEW_NTP_ENABLE = "rfc.newNTP.enable.payload";

   /** Pay load data for Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.newNTP.Enable false */
   public static final String PROP_KEY_PAYLOAD_NEW_NTP_DISABLE = "rfc.newNTP.disable.payload";
   
   /** Feature Name for WiFi Interworking Enable rfc configuration */
   public static final String FEATURE_NAME_INTERWORKING_CONFIG = "INTERWORKING_CONFIG";

   /** Pay load data for Telemetry mLTS DCMUpload rfc enable */
   public static final String PROP_KEY_PAYLOAD_INTERWORKING_ENABLE = "rfc.interworking.enable";

   /** Pay load data for Telemetry mLTS DCMUpload rfc disable */
   public static final String PROP_KEY_PAYLOAD_INTERWORKING_DISABLE = "rfc.interworking.disable";
   
   /** RFC feature name OVS */
   public static final String FEATURE_NAME_OVS = "OVS";

   /** RFC payload for OVS enable */
   public static final String PROP_KEY_PAYLOAD_OVS_ENABLE = "rfc.ovs.enable.payload";

   /** RFC payload for OVS disable */
   public static final String PROP_KEY_PAYLOAD_OVS_DISABLE = "rfc.ovs.disable.payload";

   /** String constant to store configurable telemetry feature name value */
   public static final String CONFIGURABLE_TELEMETRY_ENDPOINT2 = "TelemetryNewEndpoint2";

   /** RFC payload for Telemetry End Point enable */
   public static final String PROP_KEY_PAYLOAD_TELEMETRYENDPOINT_ENABLE = "rfc.feature.instance.override.enable";

   /** RFC payload for Telemetry End Point disable */
   public static final String PROP_KEY_PAYLOAD_TELEMETRYENDPOINT_DISABLE = "rfc.feature.instance.override.disable";

   /** String constant to store configurable non-root support */
   public static final String CONFIGURABLE_NONROOT_SUPPORT = "nonroot_support";

   /** RFC payload for non-root support enable */
   public static final String PROP_KEY_PAYLOAD_NONROOT_SUPPORT_ENABLE = "rfc.rdkb.nonroot.support.enable";

   /** RFC payload for non-root support disable */
   public static final String PROP_KEY_PAYLOAD_NONROOT_SUPPORT_DISABLE = "rfc.rdkb.nonroot.support.disable";
   
   /** RFC feature name for auto exclude */
   public static final String FEATURE_NAME_AUTO_EXCLUDE = "AUTO_EXCLUDE";

   /** RFC payload to enable auto exclude */
   public static final String PROP_KEY_PAYLOAD_AUTO_EXCLUDE_ENABLE = "rfc.autoexclude.enable.payload";

   /** RFC payload to disable auto exclude */
   public static final String PROP_KEY_PAYLOAD_AUTO_EXCLUDE_DISABLE = "rfc.autoexclude.disable.payload";
   /** String constant to store configurable credential download */
   public static final String CONFIGURABLE_CRED_DWNLD = "cred_download";

   /** String constant to store configurable credential download */
   public static final String CONFIGURABLE_CRED_DWNLD_2 = "cred_download_2";

   /** RFC payload for enable cred dwnld */
   public static final String PROP_KEY_PAYLOAD_CRED_DWNLD_ENABLE = "rfc.rdkb.cred.download.enable.true.use.enable";

   /** RFC payload for disable cred dwnld */
   public static final String PROP_KEY_PAYLOAD_CRED_DWNLD_DISABLE = "rfc.rdkb.cred.download.enable.false.use.disable";

   /** RFC payload for disable cred dwnld */
   public static final String PROP_KEY_PAYLOAD_CRED_DWNLD_USE_DISABLE = "rfc.rdkb.cred.download.enable.true.use.disable";
  
   /** Feature Name for WiFi Wifi blaster Enable rfc configuration */
   public static final String FEATURE_NAME_WIFIBLASTER_CONFIG = "WIFIBLASTER_CONFIG";
   
   /** Pay load data for wifi blaster rfc enable */
   public static final String PROP_KEY_PAYLOAD_WIFIBLASTER_ENABLE = "rfc.wifiblaster.enable";

   /** Pay load data for wifi balster rfc disable */
   public static final String PROP_KEY_PAYLOAD_WIFIBLASTER_DISABLE = "rfc.wifiblaster.disable";
  
   /** RFC feature name for Aggressive selfheal */
   public static final String RFC_NAME_AGGRESSIVE_SELFHEAL = "AGGRESSIVE_SELFHEAL";
   
   /** Payload data for aggressive selfheal interval enable */
   public static final String PROP_KEY_PAYLOAD_AGG_SELFHEAL_ENABLE = "rfc.aggressive_selfheal.enable";

   /** Payload data for aggressive selfheal interval disable */
   public static final String PROP_KEY_PAYLOAD_AGG_SELFHEAL_DISABLE = "rfc.aggressive_selfheal.disable";
   
   /** Feature Name for WiFi Wifi blaster Enable rfc configuration */
   public static final String FEATURE_NAME_WEBCONFIG = "WEBCONFIG";
   
   /** Pay load data for wifi blaster rfc enable */
   public static final String PROP_KEY_PAYLOAD_WEBCONFIG_ENABLE = "rfc.webconfig.enable";

   /** Pay load data for wifi balster rfc disable */
   public static final String PROP_KEY_PAYLOAD_WEBCONFIG_DISABLE = "rfc.webconfig.disable";
   
   /** String constant to store configurable Passpoint feature */
   public static final String CONFIGURABLE_PASSPOINT = "WiFi-Passpoint";
   
   /** RFC payload for Passpoint support enable */
   public static final String PROP_KEY_PAYLOAD_PASSPOINT_ENABLE = "rfc.rdkb.nonroot.support.enable";

   /** RFC payload for Passpoint disable */
   public static final String PROP_KEY_PAYLOAD_PASSPOINT_DISABLE = "rfc.rdkb.nonroot.support.disable";
   
   /** String constant to store CABUNDLE feature */
   public static final String CONFIGURABLE_CABUNDLE = "CABUNDLE";
   
   /** RFC payload for CABUNDLE enable */
   public static final String PROP_KEY_PAYLOAD_DLCASTORE_ENABLE = "rfc.cabundle.enable";

   /** RFC payload for CABUNDLE disable */
   public static final String PROP_KEY_PAYLOAD_DLCASTORE_DISABLE = "rfc.cabundle.disable";
   
	/** String constant to store Hardware health test feature */
   public static final String CONFIGURABLE_HARDWAREHEALTHTEST = "hwHealthTest";
   
   /** stb properties key for payload data to enable configurable hwHealthTest rfc in xconf to enable */
   public static final String PROP_KEY_PAYLOAD_ENABLE_CONFIGURABLE_HHT = "rfc.hht.enable";

   /** stb properties key for payload data to disable configurable hwHealthTest rfc in xconf to disable */
   public static final String PROP_KEY_PAYLOAD_DISABLE_CONFIGURABLE_HHT = "rfc.hht.disable";
   
   /** RFC feature name OVS */
   public static final String OVS_FEATURE_NAME = "OVS_FEATURE";
   
   /** RFC payload for OVS enable */
   public static final String PROP_KEY_OVS_ENABLE_PAYLOAD = "rfc.ovs.enable";

   /** RFC payload for OVS disable */
   public static final String PROP_KEY_OVS_DISABLE_PAYLOAD = "rfc.ovs.disable";
   
   /** RFC feature name FAN */
   public static final String FEATURE_NAME_FAN_MULTIPLE = "FAN_MULTIPLE";

   /** Pay load data for Fan.Maxoverride enable */
   public static final String PROP_KEY_PAYLOAD_FAN_MULTIPLE_MAXOVERRIDE_ENABLE = "rfc.fan.multiple.enable";

   /** Pay load data for Fan.Maxoverride disable */
   public static final String PROP_KEY_PAYLOAD_FAN_MULTIPLE_MAXOVERRIDE_DISABLE = "rfc.fan.multiple.disable";
   
   /** String to replace in LSA pay load data **/
   public static final String CONSTANT_REPLACE_STBMAC_LSAPAYLOADDATA = "ESTB_MAC_ADDRESS";
   
   /** Constant holding the location of dcm.properties file in /nvram folder */
   public static final String DCM_PROPERTIES_FILE_NVRAM_FOLDER = "/nvram/dcm.properties";
   
   /** String to store value of 'arm' */
   public static final String ARM = "arm";

   /** String value to replace */
   public static final String STRING_VALUE_TO_REPLACE = "<<VALUE>>";
   
   /** String to store Backup */
   public static final String TAG_BACK_UP_FILE = "automation_Backup";
   
   /** String constant to replace BACKUP **/
   public static final String REPLACE_BACKUP_FILE = "<BACKUP>";
   
   /** String variable to atom console */
   public static final String STRING_ATOM_CONSOLE = "atom";
   /** Command to copy the dcm.properties file */
   public static final String CMD_CAT_DCM_PROPERTIES = "cat " + DCM_PROPERTIES_FILE_NVRAM_FOLDER;
   
   /** String constant to store tr181 value */
   public static final String TR181_DOT = "tr181.";
   
   /** String constant to store with value text */
   public static final String WITH_VALUE = ".*value.*";
   
   /** String constant to match RFC log message patterns */
   public static final String RFC_LOG_PATTERN = "RFC:.*";
   
   /** The constant holding command for seeing the process status of dnsmasq process. */
   public static final String STRING_DNSMASQ_PROCESS = "dnsmasq -u";
   
   /** String variable to store dnsmasq is not running */
   public static final String STRING_DNSMASQ_NOT_RUNNING = "dnsmasq is not running";
   
   /** String value for dnsmasq */
   public static final String STRING_DNSMASQ = "dnsmasq";
   
   /** Command killall -11 */
   public static final String CMD_KILLALL_11 = "killall -11";
   
   /** Command to read the dnsmasq process status in RDKB devices alone */
   public static final String COMMAND_TO_GET_DNSMASQ_PROCESS_STATUS = "ps | grep -i \"[d]nsmasq \" | wc -l";
   
   /** Command to read the dnsmasq process status in RDKB devices alone */
   public static final String COMMAND_TO_GET_DNSMASQ_PROCESS = "ps | grep -i \"[d]nsmasq\"";

   /** stb properties key for getting proxy xconf for rfc update settings url **/
   public static final String PROP_KEY_PROXY_XCONF_RFC_URL = "proxy.xconf.rfc.url";
   
   /** Command ARP */
   public static final String COMMAND_ARP = "/sbin/arp -n";
   
   /** interface name to erouter0 */
   public static final String INTERFACE_NAME_EROUTER0 = "erouter0";
   
   /**
    * The constant holds the command to get CMTS Mac Address from Arp table.
    */
   public static final String CMD_ARP_TABLE_FOR_EROUTER0_INTERFACE = "/sbin/arp -n | grep -i \"erouter0\"";
   
   /**
    * The constant holds the regular expression for getting MAC Address with semicolon.
    */
   public static final String REG_EXPRESSION_TO_GET_MAC_ADDRESS_SEMICOLON = "(\\w+:\\w+:\\w+:\\w+:\\w+:\\w+)";
   
   /** stores the constant value for TXT_COMPARISON */
   public static final String CONSTANT_TXT_COMPARISON = "TXT_COMPARISON";
   
   /** int value -1 */
   public static final int INT_VALUE_MINUS_ONE = -1;
   
   /** Process Name for Parodus */
   public static final String PROCESS_NAME_PARODUS = "parodus";
   
   /** String value to store Parodus grep command */
   public static final String COMMAND_TO_GET_PARODUS_PROCESS = "ps | grep -i parodus";
   
   /** Pattern Matcher Extract Search Response from ATOM Console Execution */
   public static final String PATTERN_MATCHER_ATOM_CONSOLE_LOG_SEARCH_RESPONSE = "(?s)(\\d{6}.*)"; 
   /** Property Key for S3 Amazon Signing URL */
   public static final String PROP_KEY_AMAZON_URL = "S3_AMAZON_SIGNING_URL";
   
   /** Property Key for S3 Amazon Signing URL */
   public static final String PROP_KEY_S3_AMAZON_URL = "amazon.s3.server.url";
   
   /** Constant to hold timeout response */
   public static final String SNMP_TIME_OUT_RESPONSE = "Timeout: No Response from";
   
   /** STRING NULL IP(0.0.0.0) */
   public static final String STRING_NULL_IP = "0.0.0.0";
   
   
   /** Constant to store the telephone line status as down(2) */
   public static final String SNMP_RESPONSE_FOR_TEL_LINE_STATUS_DOWN = "down";
   
   /** String for Rfc properties to post - Replace with data payload */
   public static final String STRING_RFC_DATA_GENERIC_PAYLOAD = "{\"estbMacAddress\":\"ESTB_MAC_ADDRESS\",\"features\":[{\"name\":\"<PAYLOAD>\",\"effectiveImmediate\":true,\"enable\":true,\"configData\":<REPLACE>}]}\r\n";
   
   /**
    * String for keyword replace for payload
    */
   public static final String STRING_PAYLOAD_REPLACE = "<PAYLOAD>";
   
   /** Constant to store partial property key for GA image */
   public static final String PARTIAL_PROPERTY_KEY_FOR_GA_IMAGE = "cdl.ga.image.";
  
   /** String value to store unknown value */
   public static final String UNKNOWN_REBOOT_REASON = "unknown";
   
   /** String to store activationInProgress= */
   public static final String STRING_ACTIVATION_INPROGRESS = "activationInProgress=";
   

   /** String to store delaydownload reset */
   public static final String STRING_FOR_DELAY_DOWNLOAD_RESET = "Resetting the download delay to 0 minutes";
   /** String to store delaydownload wait time */
   public static final String STRING_FOR_DELAY_DOWNLOAD_SPECIFIED_TIME = "Device configured with download delay of <REPLACE> minutes";
   /** String to store download trigger from delaydownload */
   public static final String STRING_XCONF_TRIGGER_IMMEDIATE = "Trigger from delayDownload Timer";
   
   /** Constant to store reboot reason rfc_reboot */
   public static final String REBOOT_REASON_RFC_REBOOT = "rfc_reboot";
   
    /** Constant to hold property key for maintenance window pattern */
    public static final String PROP_KEY_MAINTENANCE_WINDOW_PATTERN = "maintenance.window.pattern";
    
    /** Constant to hold property key for maintenance window pattern */
    public static final String PROP_KEY_DEFAULT_MAINTENANCE_WINDOW_START_TIME = "default.maintenance.window.startTime";

    /** Constant to hold property key for maintenance window pattern */
    public static final String PROP_KEY_DEFAULT_MAINTENANCE_WINDOW_END_TIME = "default.maintenance.window.endTime";

    /** String value to store device and reboot reason */
    public static final String DEVICE = "Device";
    
    /** Cat Xconf text */
    public static final String CAT_XCONF_TXT = "cat /rdklogs/logs/xconf.txt.0";
    
    /** command to get the memory uasge */
    public static final String CMD_GET_MEM_USAGE = "top -bn1 | grep -i \"Mem:\" | sed -n \'1p\'";

    /** command to get the CPU uasge */
    public static final String CMD_GET_CPU_USAGE = "top -bn1 | grep -i \"CPU:\" | sed -n \'1p\'";
	  
   /*******************
    * Test constants to verify self heal scenarios
    ***************************/
   /**
    * String variable to store self heal enabled status string
    */
   public static final String STRING_SELF_HEAL_ENABLED_STATUS = "SelfHealenabledStatus";
   /**
    * String variable to store number of pings per server for self heal
    */
   public static final String STRING_NUMBER_PINGS_PER_SERVER_FOR_SELF_HEAL = "numPingsPerServer";
   /**
    * String variable to store minimum number of ping server for self heal
    */
   public static final String SELF_HEAL_MINIMUM_NUMBER_OF_PING_SERVER = "minNumPingServer";
   /**
    * String variable to store ping interval for self heal
    */
   public static final String STRING_PING_INTERVAL_FOR_SELF_HEAL = "pingInterval";
   /**
    * String variable to store reset count for self heal
    */
   public static final String STRING_RESET_COUNT_FOR_SELF_HEAL = "resetCount";
   /**
    * String variable to store resource usage compute window for self heal
    */
   public static final String STRING_RESOURCE_USAGE_FOR_SELF_HEAL = "resourceUsageComputeWindow";
   /**
    * String variable to store average cpu threshold for self heal
    */
   public static final String STRING_AVG_CPU_THRESHOLD_FOR_SELF_HEAL = "avgCPUThreshold";
   /**
    * String variable to store average memory threshold for self heal
    */
   public static final String STRING_AVG_MEMORY_THRESHOLD_FOR_SELF_HEAL = "avgMemoryThreshold";
   /**
    * String variable to store maximum reboot count for self heal
    */
   public static final String STRING_MAXIMUM_REBOOT_COUNT_FOR_SELF_HEAL = "maxRebootCount";
   /**
    * String variable to store maximum sub system reset count for self heal
    */
   public static final String STRING_MAXIMUM_SUB_SYSTEM_RESET_COUNT_FOR_SELF_HEAL = "maxSubsystemResetCount";
   
   /** pattern to get self heal param values */
   public static final String PATTERN_GET_SELF_HEAL_PArAMETER_VALUES = "SNMPv2-SMI::enterprises.17270.44.1.1.";
   
   /** String value for 60 */
   public static final String SELF_HEAL_PING_INTERVAL = "60";
   

   
   /** Constant to hold SET */
   public static final String SET = "SET";

   /** Constant to hold Get */
   public static final String GET = "GET";
   
   /** String to store value 5 */
   public static final String STRING_VALUE_5 = "5";
   /** String to store value 6 */
   public static final String STRING_VALUE_6 = "6";
   /** String to store value 7 */
   public static final String STRING_VALUE_7 = "7";
   /** String to store value 8 */
   public static final String STRING_VALUE_8 = "8";
   /** String to store value 9 */
   public static final String STRING_VALUE_9 = "9";
   /** String to store value 10 */
   public static final String STRING_VALUE_10 = "10";
   /** String to store value 20 */
   public static final String STRING_VALUE_20 = "20";
   /** String to store value 30 */
   public static final String STRING_VALUE_30 = "30";
   /** String to store value 40 */
   public static final String STRING_VALUE_40 = "40";
   /** String to store value 50 */
   public static final String STRING_VALUE_50 = "50";
   /** String to store value 60 */
   public static final String STRING_VALUE_60 = "60";   
   /** String to store value 70 */
   public static final String STRING_VALUE_70 = "70";
   /** String to store value 80 */
   public static final String STRING_VALUE_80 = "80";
   /** String to store value 90 */
   public static final String STRING_VALUE_90 = "90";
   /** String to store value 100 */
   public static final String STRING_VALUE_100 = "100";
   /** String to store value 110 */
   public static final String STRING_VALUE_110 = "110";

   /** Default time to get the warehouse works. */
   public static final long FIVE_SECONDS_IN_MILLIS = 5 * ONE_SECOND_IN_MILLIS;
   
   /** Process Name -paradus */
   public static final String PARODUS_PROCESS_NAME = "parodus";
 
   /** Pattern to get transaction ID from Parodus log */
   public static final String PATTERN_TO_GET_TRANSACTION_ID_FROM_PARODUS_LOG = "transaction_uuid:(.*)";
  
   /** Constant to hold SET Request */
   public static final String SET_REQUEST = "SET Request";
   
   /** Open parenthesis */
   public static final String OPEN_PARENTHESIS = "(";
   
   /** REGEX FOR RDKB LOG TIMESTAMP */
   public static final String REGEX_FOR_RDKB_LOG_TIMESTAMP = "\\d{6}-\\d{2}:\\d{2}:\\d{2}\\.\\d{6}";
   
   /** Closed paranthesis ) */
   public static final String CLOSED_PARANTHESIS = ")";

   /** String wildcard symbol. */
   public static final String STRING_WILDCARD = ".*";
 
   /** REGEX FOR TIMESTAMP WITH ONLY TIME */
   public static final String REGEX_FOR_TIMESTAMP_WITH_ONLY_TIME = "\\d{6}-(\\d{2}:\\d{2}:\\d{2}\\.\\d{6})";
   
   /** Symbol Forward Slash */
   public static final String SYMBOL_FORWARD_SLASH = "\\";
   
   /**
    * params for telemetry error verifications
    */
   /** Constant for single quote - ' */
   public static final String SINGLE_QUOTE = "'";
   
   /** String to hold double quote and single quote */
   public static final String DOUBLE_QUOTE_SINGLE_QUOTE = "\"'";
   
   /** File path for Parodus log file */
   public static final String RDKLOGS_LOGS_PARODUS_TXT_0 = "/rdklogs/logs/PARODUSlog.txt.*";
   
   /** Pattern for verifying the signed integer. */
   public static final String PATTERN_FOR_UN_SIGNED_INT = "(\\d+)";
   
   /** Pattern to get parodus connect time from PARODUSlog */
   public static final String PATTERN_PARODUS_CONNECT_TIME = "connect_time-diff-boot_time=(\\d+)";
   
   /** Pattern Matcher to retrieve the timestamp from speedtest log message */
   public static final String PATTERN_MATCHER_TIMESTAMP_SPEEDTEST_LOG_MESSAGE = "(\\d{6}-\\d{2}:\\d{2}:\\d{2})+";
   
   /** The constant for UTC TimeZone */
   public static final String TIMEZONE_UTC = "UTC";
   
   /** Simple Data Format - Date/Time in Log Messages */
   public static final String TIMESTAMP_FORMAT_SPEEDTEST_LOG_MESSAGE = "yyMMdd-HH:mm:ss";
   
   /** Pattern Matcher to retrieve the device manufacturer name */
   public static final String COMMAND_TO_RETRIEVE_MANUFACTURER_NAME = "grep -i \"hw_manufacturer\" /rdklogs/logs/PARODUSlog.txt.0";
   
   /**
    * Pattern Matcher to retrieve the manufacturer name from parodus log message
    */
   public static final String PATTERN_MATCHER_MANUFACTURER_NAME = "is (.*)";
   
   /** Software Version Pattern */
   public static final String SOFTWARE_VERSION_PATTERN = "SW_REV:\\s*(.*)";
   
   /** Command to removed RFC override */
   public static final String CMD_REMOVED_RFC_OVERRIDE = "rm -rf /nvram/RFC/ /nvram/rfc.properties /nvram/dcm.properties";
   
   /** Operating Standards for 5GHz a/n/ac mode. */
   public static final String OPERATING_STANDARDS_A_N_AC = "a,n,ac";
   
   /** Operating Standards for 2.4GHz b/g/n mode. */
   public static final String OPERATING_STANDARDS_B_G_N = "b,g,n";
   
   /** Value to reset the WIFI settings */
   public static final String STRING_WIFI_RESET_VALUE = "1,2;1,2";
   
   /** constant for first ipv4 pingserver uri */
   public static final String FIRST_IPV4_PINGSERVER_URI = "1.2.3.4";

   /** constant for first ipv4 pingserver uri */
   public static final String SECOND_IPV4_PINGSERVER_URI = "2.3.4.5";

   /** constant for first ipv4 pingserver uri */
   public static final String THIRD_IPV4_PINGSERVER_URI = "3.4.5.6";

   /** constant for first ipv4 pingserver uri */
   public static final String FIRST_IPV6_PINGSERVER_URI = "2001::2002";

   /** constant for first ipv4 pingserver uri */
   public static final String SECOND_IPV6_PINGSERVER_URI = "2002::2003";

   /** constant for first ipv4 pingserver uri */
   public static final String THIRD_IPV6_PINGSERVER_URI = "2003::2004";
   
   /** Constant represent log file /rdklogs/logs/dcmscript.log **/
   public static final String DCMSCRIPT_LOG_FILE = "/rdklogs/logs/dcmscript.log";
   
   /** Constant to hold the value 6 */
   public static final String STRING_VALUE_SIX = "6";

   /** Pattern Matcher to extract the Payload Data from dcmscript log file */
   public static final String PATTERN_MATCHER_PAYLOAD_DATA = "-X POST -d\\s+'(.*)'";
   
   /** JSON Request Data - Header Element */
   public static final String JSON_ELEMENT_HEADER = "header";
   /** JSON Request Data - Content Element */
   public static final String JSON_ELEMENT_CONTENT = "content";
   /** JSON Request Data - Type Element */
   public static final String JSON_ELEMENT_TYPE = "type";
   
   /** JSON Request Data - Telemetry Profile Name Element */
   public static final String JSON_ELEMENT_TELEMETRY_PROFILE_NAME = "telemetryProfile:name";
   /** JSON Request Data - Telemetry Settings Element */
   public static final String JSON_ELEMENT_TELEMETRY_SETTINGS = "telemetrySettings";
   
   /** JSON Request Data - Polling Frequency Element */
   public static final String JSON_ELEMENT_POLLING_FREQUENCY = "pollingFrequency";
   /** JSON Request Data - MAC Address Element */
   public static final String JSON_ELEMENT_ESTB_MAC_ADDRESS = "estbMacAddress";
   
   /** constant for profile name for ping servers */
   public static final String PROFILE_NAME_PING = "PING_SERV_TO_DTCT_NW_CNCTVTY_";
   
   /** Command to copy the dcm.properties file */
   public static final String CMD_CP_DCM_PROPERTIES = "cp /etc/dcm.properties /nvram/";
   
   /** Pattern for Ping test failed 4 */
   public static final String PATTERN_PING_FAILED_FOUR = "\"PING_FAILED\":\"4\"";

   /** Pattern for Ping test failed 2 */
   public static final String PATTERN_PING_FAILED_TWO = "\"PING_FAILED\":\"2\"";
   
   /** String regex to get next line */
   public static final String STRING_REGEX_MATCH_LINE = "\\r?\\n";
   
   /** pattern to find IPV4 valid ping address */
   public static final String PATTERN_MATCHER_IPV4_VALID_PING_SERVER = "Server:\\s+(.*)";

   /** Default inet6 address log */
   public static final String INET_V6_ADDRESS = "inet6 addr";

   /** pattern to get the table rows */
   public static final String PATTERN_MATCHER_TO_GET_TABLE_ROWS = "ServerTable\\.(\\d+)\\.";

   /** pattern to find IPV6 valid ping address */
   public static final String PATTERN_MATCHER_IPV6_VALID_PING_SERVER = "Address 1:\\s+(\\w+:\\w+:\\w+:\\w+:\\w+:\\w+:\\w+:\\w+)[\n\r]Address 2";
   
   /** constant for Data Block Size */
   public static final String DATA_BLOCK_SIZE = "64";
   

   /**
    * Common SSID password for broadband device.
    */
   public static final String BROAD_BAND_DEVICE_SSID_PASSWORD = "broadband.device.sssid.password.value";
   
   /** Pattern to get IPv4 Address */
   public static final String PATTERN_TO_GET_IPV4_ADDRESS = "(\\d+\\.\\d+\\.\\d+\\.\\d+)";
   
   /** Constant for Primary remote endpoint */
   public static final String PRIMARY_REMOTE_ENDPOINT = "";

   /** Constant for Secondary remote endpoint */
   public static final String SECONDARY_REMOTE_ENDPOINT = "";
   
   /** Pattern to get xfinity wifi status from rfc config data */
   public static final String PATTERN_GET_XFINITY_WIFI_STATUS_FROM_RFC_CONFIG = "tr181.Device.DeviceInfo.X_COMCAST_COM_xfinitywifiEnable\\W+(\\w+)";
   
   /**
    * Default firmware maintenance upgrade window start time.
    */
   public static final String DEFAULT_FIRMWARE_UPGRADE_MAINTENANCE_WINDOW_START_TIME = "3600";

   /**
    * Default firmware maintenance upgrade window end time.
    */
   public static final String DEFAULT_FIRMWARE_UPGRADE_MAINTENANCE_WINDOW_END_TIME = "14400";
   
   /** Test constant for Global Dns IPv4 default value 75.75.75.75 */
   public static final String STRING_DEFAULT_GLOBAL_DNS_IPV4_VALUE = "75.75.75.75";
   
   /** Test constant for Global Dns IPv6 default value 2001:558:feed::1 */
   public static final String STRING_DEFAULT_GLOBAL_DNS_IPV6_VALUE = "2001:558:feed::1";
   
   /** Constant for DSCPMarkPolicy */
   public static final String DSCP_MARK_POLICY = "44";
   
   /** Constant for 5GHZ band */
   public static final String BAND_5GHZ = "5GHZ";
   
   /** Constant for private WifiType */
   public static final String PRIVATE_WIFI_TYPE = "PRIVATE_WIFI";
   
   /** Constant for SSID */
   public static final String SSID_PARAM = "SSID";
   
   /** Constant for PUBLIC WifiType */
   public static final String PUBLIC_WIFI_TYPE = "PUBLIC_WIFI";
   
   /** Interface ath0 */
   public static final String INTERFACE_ATH0 = "ath0";
   
   /** Interface ath1 */
   public static final String INTERFACE_ATH1 = "ath1";

   /** Interface ath2 */
   public static final String INTERFACE_ATH2 = "ath2";

   /** Interface ath3 */
   public static final String INTERFACE_ATH3 = "ath3";

   /** Interface ath4 */
   public static final String INTERFACE_ATH4 = "ath4";

   /** Interface ath5 */
   public static final String INTERFACE_ATH5 = "ath5";
   
   /** Constant for MAC */
   public static final String MAC_PARAM = "MAC";

   /** Constant for BSSID */
   public static final String BSSID_PARAM = "BSSID";
   
   /** String value for Dual Band */
   public static final String DUAL_BAND = "Dual band";
   
   /** Constatnt for iwconfig */
   public static final String CMD_IW_CONFIG = "/sbin/iwconfig";
   
   /** Constant for NO such devices */
   public static final String NOSUCHDEVICES = "No such device";
   
   /** Path for /usr/sbin */
   public static final String STRING_PATH_USR_SBIN = "/usr/sbin/";
   
   /**
    * System property which enables Code download along with Factory Reset.
    */
   public static final String SYSTEM_PROPERTY_ENABLE_CDL_FACTORY_RESET = "ENABLE_CDL_FACTORY_RESET";
   
   /**
    * Download status - waiting for reboot to previos download.ss
    */
   public static final String DOWNLOAD_STATUS_WAITING_FOR_REBOOT_PREVIOUS_DOWNLOAD = "waiting reboot after download, so exit";
   
   /**
    * Download status - download is in progress, exit
    */
   public static final String DOWNLOAD_IN_PROGRESS_FOR_PREVIOUS_TRIGGER = "download is in progress, exit";
   
   /** String to store 2.4Ghz SSID name */
   public static final String STRING_VALUE_24GHZ_SSID1 = "RDKB-2.4G1";
   /** String to store 5Ghz SSID name */
   public static final String STRING_VALUE_5GHZ_SSID1 = "RDKB-5G1";
   /** String to store 2.4Ghz/5Ghz passkey */
   public static final String STRING_VALUE_24_5GHZ_KEY1 = "password1";
   /** String to store 2.4Ghz SSID name */
   public static final String STRING_VALUE_24GHZ_SSID2 = "RDKB-2.4G2";
   /** String to store 5Ghz SSID name */
   public static final String STRING_VALUE_5GHZ_SSID2 = "RDKB-5G2";
   /** String to store 2.4Ghz/5Ghz passkey */
   public static final String STRING_VALUE_24_5GHZ_KEY2 = "password2";
   /** String to store 2.4Ghz SSID name */
   public static final String STRING_VALUE_24GHZ_SSID3 = "RDKB-2.4G3";
   /** String to store 5Ghz SSID name */
   public static final String STRING_VALUE_5GHZ_SSID3 = "RDKB-5G3";
   /** String to store 2.4Ghz/5Ghz passkey */
   public static final String STRING_VALUE_24_5GHZ_KEY3 = "password3";
   /** String to store 2.4Ghz SSID name */
   public static final String STRING_VALUE_24GHZ_SSID4 = "RDKB-2.4G4";
   /** String to store 5Ghz SSID name */
   public static final String STRING_VALUE_5GHZ_SSID4 = "RDKB-5G4";
   /** String to store 2.4Ghz/5Ghz passkey */
   public static final String STRING_VALUE_24_5GHZ_KEY4 = "password4";
   /** String to store 2.4Ghz SSID name */
   public static final String STRING_VALUE_24GHZ_SSID5 = "RDKB-2.4G5";
   /** String to store 5Ghz SSID name */
   public static final String STRING_VALUE_5GHZ_SSID5 = "RDKB-5G5";
   /** String to store 2.4Ghz/5Ghz passkey */
   public static final String STRING_VALUE_24_5GHZ_KEY5 = "password5";
   /** String to store 2.4Ghz SSID name */
   public static final String STRING_VALUE_24GHZ_SSID6 = "RDKB-2.4G6";
   /** String to store 5Ghz SSID name */
   public static final String STRING_VALUE_5GHZ_SSID6 = "RDKB-5G6";
   /** String to store 2.4Ghz/5Ghz passkey */
   public static final String STRING_VALUE_24_5GHZ_KEY6 = "password6";
   /** String to store 2.4Ghz SSID name */
   public static final String STRING_VALUE_24GHZ_SSID7 = "RDKB-2.4G7";
   /** String to store 5Ghz SSID name */
   public static final String STRING_VALUE_5GHZ_SSID7 = "RDKB-5G7";
   /** String to store 2.4Ghz/5Ghz passkey */
   public static final String STRING_VALUE_24_5GHZ_KEY7 = "password7";
   /** String to store 2.4Ghz SSID name */
   public static final String STRING_VALUE_24GHZ_SSID8 = "RDKB-2.4G8";
   /** String to store 5Ghz SSID name */
   public static final String STRING_VALUE_5GHZ_SSID8 = "RDKB-5G8";
   /** String to store 2.4Ghz/5Ghz passkey */
   public static final String STRING_VALUE_24_5GHZ_KEY8 = "password8";
   /** String to store 2.4Ghz SSID name */
   public static final String STRING_VALUE_24GHZ_SSID9 = "RDKB-2.4G9";
   /** String to store 5Ghz SSID name */
   public static final String STRING_VALUE_5GHZ_SSID9 = "RDKB-5G9";
   /** String to store 2.4Ghz/5Ghz passkey */
   public static final String STRING_VALUE_24_5GHZ_KEY9 = "password9";
   /** String to store 2.4Ghz SSID name */
   public static final String STRING_VALUE_24GHZ_SSID10 = "RDKB-2.4G10";
   /** String to store 5Ghz SSID name */
   public static final String STRING_VALUE_5GHZ_SSID10 = "RDKB-5G10";
   /** String to store 2.4Ghz/5Ghz passkey */
   public static final String STRING_VALUE_24_5GHZ_KEY10 = "password10";
   
   /** String to store 2.4Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS1 = "AccessPoint-2GHz1";

   /** String to store 2.4Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS2 = "AccessPoint-2GHz2";

   /** String to store 2.4Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS3 = "AccessPoint-2GHz3";

   /** String to store 2.4Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS4 = "AccessPoint-2GHz4";

   /** String to store 2.4Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS5 = "AccessPoint-2GHz5";

   /** String to store 2.4Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS6 = "AccessPoint-2GHz6";

   /** String to store 2.4Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS7 = "AccessPoint-2GHz7";

   /** String to store 2.4Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS8 = "AccessPoint-2GHz8";

   /** String to store 2.4Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS9 = "AccessPoint-2GHz9";

   /** String to store 2.4Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS10 = "AccessPoint-2GHz10";

   /** String to store 5Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS1 = "AccessPoint-5GHz1";

   /** String to store 5Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS2 = "AccessPoint-5GHz2";

   /** String to store 5Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS3 = "AccessPoint-5GHz3";

   /** String to store 5Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS4 = "AccessPoint-5GHz4";

   /** String to store 5Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS5 = "AccessPoint-5GHz5";

   /** String to store 5Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS6 = "AccessPoint-5GHz6";

   /** String to store 5Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS7 = "AccessPoint-5GHz7";

   /** String to store 5Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS8 = "AccessPoint-5GHz8";

   /** String to store 5Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS9 = "AccessPoint-5GHz9";

   /** String to store 5Ghz Wifi Access point Alias */
   public static final String STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS10 = "AccessPoint-5GHz10";

/** Pattern for getting docsis event count. */
   public static final String DOCSIS_EVENT_COUNT_PATTERN = "SNMPv2-SMI::mib-2.\\d+.\\d+.\\d+.\\d+.\\d+.\\d+.\\d+ = (\\d+)";
   
/** Pattern for getting docsis event text. */
   public static final String DOCSIS_EVENT_TEXT_PATTERN = "SNMPv2-SMI::mib-2.\\d+.\\d+.\\d+.\\d+.\\d+.\\d+.\\d+ = (.*)";

   /** Pattern for getting docsif mib value. */
   public static final String DOCSIF_MIB_VALUE_PATTERN = "SNMPv2-SMI::\\w+.*= -?(\\d+)";
   
   /** TRUE flag for SNMP operations */
   public static final String TRUE_FLAG = " = 1";
   
   /** int value to store two hundred and ninety */
   public static final String STRING_CONSTANT_290 = "290";
   
   /** int value 200 */
   public static final int INT_VALUE_TWO_HUNDRED = 200;
   
   /** int value 500 */
   public static final int INT_VALUE_FIVE_HUNDRED = 500;
   
   /** int value 580 */
   public static final int INT_VALUE_FIVE_HUNDRED_AND_EIGHTY = 580;
   
   /** int value 80 */
   public static final int INT_VALUE_EIGHTY = 80;
   
   /** int value -150 */
   public static final int INT_VALUE_MINUS_HUNDRED_AND_FIFTY = -150;

   /** int value 150 */
   public static final int INT_VALUE_HUNDRED_AND_FIFTY = 150;
   
   /** int value 120 */
   public static final int INT_VALUE_HUNDRED_AND_TWENTY = 120;
   
   /** Property key which contains payload data to enable/disable Snmpv2 */
   public static final String PROP_KEY_TO_DISABLE_SNMPV2 = "rfc.payload.disable.snmpv2";
   
   /** String which will be replaced by actual enable/disable value in payload data */
   public static final String ENABLE_DISABLE_VALUE = "ENABLEDISABLEVALUE";

   /** Command tot remove /opt/dcm.properties **/
   public static final String CMD_REMOVE_NVRAM_DCM_PROPERTIES = "rm -rf " + DCM_PROPERTIES_FILE_NVRAM_FOLDER;
   
   /** The constant for the build name for signed image. */
   public static final String SIGNED_BUILD_IMAGE_EXTENSION = "-signed";
   
   /** The property key  for signed extensions. */
   public static final String PROP_KEY_SIGNED_EXTENSION="build.signed.extension.";
   
   /** The property key  for signed extensions. */
   public static final String PROP_KEY_AVG_RESPONSE_TIME="average.responsetime.";
   
   /** The property key  for signed extensions. */
   public static final String MANUFACTURERNAME_VIAWEBPA="manufacturerName.RetrievedViaWebpa.";
   
   /** The property key  for 2ghz operating standars */
   public static final String OPERATING_STANDARDS_2GHZ_ARM="operatingstandards.2ghz.arm.";
   
   /** The property key  for 2ghz operating standars */
   public static final String OPERATING_STANDARDS_2GHZ_ATOM="operatingstandards.2ghz.atom.";
   
   /** The property key  for 2ghz operating standars */
   public static final String LOGFILENAME_ATOM="operatingstandards.2ghz.atom.";
   
   /** Pattern to get Uptime from snmp response string */
   public static final String SYS_UP_TIME_INSTANCE = "DISMAN-EVENT-MIB::sysUpTimeInstance = (\\d+:\\d+:\\d+:\\d+)";
   
   /** Pattern to extract failure reason trace from SNMP output */
   public static final String PATTERN_FINDER_FAILURE_REASON = ".*\n(.*)\\s+\\(";
   
   /** Constant to hold the SNMP response as Not Writable */
   public static final String NOT_WRITABLE = "Reason: notWritable";
   
   /** The property key  for wifi interface name. */
   public static final String PROP_KEY_WIFI_PRIVATE_5GHZ_INTERFACE_NAME_BY_SSID= "wifi.interface.name.private.5ghz.ssid.";
   public static final String PROP_KEY_WIFI_PRIVATE_2GHZ_INTERFACE_NAME_BY_SSID= "wifi.interface.name.private.2ghz.ssid.";  
   public static final String PROP_KEY_WIFI_PUBLIC_5GHZ_INTERFACE_NAME_BY_SSID= "wifi.interface.name.public.5ghz.ssid.";
   public static final String PROP_KEY_WIFI_PUBLIC_2GHZ_INTERFACE_NAME_BY_SSID= "wifi.interface.name.public.2ghz.ssid.";
   
   public static final String PROP_KEY_WIFI_PRIVATE_5GHZ_INTERFACE_NAME_BY_BSSID= "wifi.interface.name.private.5ghz.bssid.";
   public static final String PROP_KEY_WIFI_PRIVATE_2GHZ_INTERFACE_NAME_BY_BSSID= "wifi.interface.name.private.2ghz.bssid.";  
   public static final String PROP_KEY_WIFI_PUBLIC_5GHZ_INTERFACE_NAME_BY_BSSID= "wifi.interface.name.public.5ghz.bssid.";
   public static final String PROP_KEY_WIFI_PUBLIC_2GHZ_INTERFACE_NAME_BY_BSSID= "wifi.interface.name.public.2ghz.bssid.";
   
   public static final String PROP_KEY_WIFI_PRIVATE_5GHZ_INTERFACE_NAME_BY_MAC= "wifi.interface.name.private.5ghz.mac.";
   public static final String PROP_KEY_WIFI_PRIVATE_2GHZ_INTERFACE_NAME_BY_MAC= "wifi.interface.name.private.2ghz.mac.";  
   public static final String PROP_KEY_WIFI_PUBLIC_5GHZ_INTERFACE_NAME_BY_MAC= "wifi.interface.name.public.5ghz.mac.";
   public static final String PROP_KEY_WIFI_PUBLIC_2GHZ_INTERFACE_NAME_BY_MAC= "wifi.interface.name.public.2ghz.mac.";
   public static final String PROP_KEY_USER_SBIN_PATH= "path.user.sbin.";
   
   /** Constant for no such device */
   public static final String NOT_ASSOCIATED = "Not-Associated";

   /** Constant for Invalid Mac Address */
   public static final String INVALID_MAC_ADDRESS = "00:00:00:00:00:00";

   /** Constant for Out Of service */
   public static final String OUTOFSERVICE = "OutOfService";
   
   /** Constant for Public wifi ssid for 5GHZ */
   public static final String PUBLIC_WIFI_SSID_5 = "xfinitywifi_5";
   
   /** Constant for Public wifi ssid for 2GHZ */
   public static final String PUBLIC_WIFI_SSID_2 = "xfinitywifi_2";
   
   /** Command to grep MAC ADDRESS from Atom device */
   public static final String CMD_TO_GREP_MAC_ADDRESS_ATOM = " Access Point:\\s+(.*)\\s+Bit";
   
   /** Command to grep SSID from the Atom device */
   public static final String CMD_TO_GREP_SSID_ATOM = "[(ESSID|SSID)]:(.*)\\s+Mode";
   
   /** Command to grep SSID from the Fibre device */
   public static final String CMD_TO_GREP_SSID_FIBER = "SSID:\\s+(.*)\\s+Mode";
   
   /** Command to grep MAC ADDRESS from the Fibre device */
   public static final String CMD_TO_GREP_MAC_ADDRESS_FIBER = "BSSID:\\s+(.*)\\s+Capability";
   
   /** Regex pattern to validate the Hexa String */
   public static final String HEXA_STRING_REGEX =  "[[A-Z0-9a-z]{2}\\s].*";
   
   /** Constant to hold snmpv3 timeout error */
   public static final String SNMPV3_TIMEOUT_ERROR = "tsm: needed to free transport data";
   
   /** constant for number 1000000 */
   public static final int CONSTANT_1000000 = 1000000;
   
   /** Constant for number 100 */
   public static final int CONSTANT_100 = 100;
   
   /** Pattern to extract the values from SNMP walk Output */
   public static final String PATTERN_TO_EXTRACT_VALUES_FROM_SNMP_WALK_OUTPUT = "SNMPv2-SMI::\\w+.*=\\s+(.*)";
   
   /** String value 380 */
   public static final String STRING_VALUE_THREE_HUNDRED_AND_EIGHTY = "380";
   
   /** Pattern to extract the table index */
   public static final String PATTERN_TO_EXTRACT_TABLE_INDEX = "SNMPv2-SMI::\\w+.*\\d+\\.(\\d+)\\s+=";
   
   /** Pattern to match only digits */
   public static final String PATTERN_ONLY_DIGITS = "^\\d+$";
   
   /** String value 52400 */
   public static final String STRING_VALUE_FIVE_LAKH_TWENTY_FOUR_THOUSAND = "524000";
   
   /** Binary build image extension. */
   public static final String BINARY_BUILD_IMAGE_EXTENSION = ".bin";
   
   /** Thirteen minute in millisecond representation. */
   public static final long THIRTEEN_MINUTE_IN_MILLIS = 13 * ONE_MINUTE_IN_MILLIS;
   
   public static final int INCERMENTAL_ONE = 1;
   
   public static final int INCERMENTAL_THREE = 3;
   
   /** WebPA Parameter to Enabling Public wifi */
   public static final String WEBPA_PARAM_ENABLING_PUBLIC_WIFI = "Device.DeviceInfo.X_COMCAST_COM_xfinitywifiEnable";
   
   /** Pattern to obtain serial number displayed by Parodus in log message */
   public static final String PATTERN_MATCHER_PARODUS_SERIAL_NUMBER = "serialNumber returned from hal:(.*)\\s+";
   
   /** String constant to hold parodus token server url */
   public static final String PROP_PARODUS_TOKEN_SERVER_URL = "parodus.token.server.url";
   
   /** Pattern to match letters and digits */
   public static final String PATTERN_TO_MATCH_LETTERS_AND_DIGITS = "^[A-Za-z0-9]+";
     
   /** File path for ArmConsolelog.txt */
   public static final String RDKLOGS_LOGS_ARM_CONSOLE_0 = "/rdklogs/logs/ArmConsolelog.txt.0";
   
   /** File path for /rdklogs/logs/Consolelog.txt.0 */
   public static final String RDKLOGS_LOGS_CONSOLE_TXT_0 = "/rdklogs/logs/Consolelog.txt.0";
   
   /** Pattern to obtain mac displayed by Parodus in log message */
   public static final String PATTERN_MATCHER_PARODUS_MAC = "deviceMac is (.*)\\s+";
   
   /** String value Authentication failed */
   public static final String AUTHENTICATION_FAILED = "Authentication failed";
   
   /** String for Connection refused */
   public static final String STRING_CONNECTION_REFUSED = "Connection refused";
   
   /** Log File for RDKB Crashes */
   public static final String LOG_FILE_FOR_CRASHES_RDKB = "/rdklogs/logs/core_log.txt";
   
   /** Constant to hold Not applicable message since the system is ready and caching is completed */
   public static final String NA_MSG_FOR_SYSTEM_READY_SIGNAL = "Test Step Not Applicable since System is ready and caching is completed.";
   
   /** Pattern for time stamp in console log */
   public static final String PATTERN_TIME_FORMAT_IN_LOG_FILE = "(\\d+\\-\\d+:\\d+:\\d+)";
   
   /** pattern for IP address from netstat */
   public static final String PATTERN_IP_NESTAT = "tcp\\s+\\d+\\s+\\d+\\s+?\\S+?\\s+(\\S+)\\:8080\\s+ESTABLISHED";
   
   /** String value No Route to host */
   public static final String NO_ROUTE_TO_HOST = "No route to host";
   
   /** Command to remove rfc override */
   public static final String CMD_REMOVE_RFC_OVERRIDE = "rm -rf /nvram/rfc.properties";

   /** snmp version */
   public static final String SYSTEM_PARAM_SNMP_VERSION = "snmpVersion";
   
   /** snmpv3 dk kickstart security number 1 */
   public static final String SNMPV3_DH_KICK_START_SECURITY_NUMBER_1 = "";
   /** snmpv3 dk kickstart security number 2 */
   public static final String SNMPV3_DH_KICK_START_SECURITY_NUMBER_2 = "";
   /** snmpv3 dk kickstart security number 3 */
   public static final String SNMPV3_DH_KICK_START_SECURITY_NUMBER_3 = "";
   /** snmpv3 dk kickstart security number 4 */
   public static final String SNMPV3_DH_KICK_START_SECURITY_NUMBER_4 = "";
   
   /** snmpv3 dk kickstart security Name 1 */
   public static final String SNMPV3_DH_KICK_START_SECURITY_NAME_1 = "docsismanager";

   /** snmpv3 dk kickstart security Name 2 */
   public static final String SNMPV3_DH_KICK_START_SECURITY_NAME_2 = "docsismonitor";

   /** snmpv3 dk kickstart security Name 3 */
   public static final String SNMPV3_DH_KICK_START_SECURITY_NAME_3 = "docsisoperator";

   /** snmpv3 dk kickstart security Name 4 */
   public static final String SNMPV3_DH_KICK_START_SECURITY_NAME_4 = "docsisuser";
   
   /** constant for ADDING TABLE */
   public static final String CONSTANT_PING_SERVER_ADDTABLE = "5";
   
   /** The constant holding login name for ATOM Console */
   public static final String TELNET_ATOM_LOGIN = "root";
   
   /** Symbol @ */
   public static final String SYMBOL_AT = "@";
   
   /** text refused */
   public static final String TEXT_REFUSED = "refused";

   /** Text No auth method */
   public static final String TEXT_NO_AUTH_METHODS = "No auth methods";
   
   /** Response for telnet command not found */
   public static final String TELNET_CMD_NOT_FOUND = "telnet: command not found";

   /** String telnet not found */
   public static final String TELNET_NOT_FOUND = "telnet: not found";
   
   /** interface name to wan0 */
   public static final String INTERFACE_NAME_WAN0 = "wan0";
   
   /** interface name to mta0 */
   public static final String INTERFACE_NAME_MTA0 = "mta0";
   
   /** Command to get the ip configuration details. */
   public static final String COMMAND_TO_GET_IP_CONFIGURATION_DETAILS = "/sbin/ifconfig";
   
   /** Constant to hold Parodus Process Output */
   public static final String PARODUS_PROCESS_OUTPUT = "/usr/bin/parodus";

   /** Constant to hold process name LMLite */
   public static final String CONSTANT_LMLITE = "CcspLMLite";

   /** String value for harvester */
   public static final String STRING_HARVESTER = "harvester";
   
   /** constant to store Webpa POST */
   public static final String STRING_POST = "POST";

   /** constant to store Webpa PUT */
   public static final String STRING_PUT = "PUT";
   
   /** File path for WEBPAlog.txt */
   public static final String RDKLOGS_LOGS_WEBPA_TXT_0 = "/rdklogs/logs/WEBPAlog.txt.0";
   
   /** string for holding dummy first mac address for XDNS */
   public static final String VALUE_MACADDRESS_FIRST_XDNS = "";

   /** string for holding dummy second mac address for XDNS */
   public static final String VALUE_MACADDRESS_SECOND_XDNS = "";
   
   /**
    * Test constant for applying Dns ovverride at client MAC level IPv4 level three primaryvalue
    */
   public static final String STRING_DNS_IPV4_VALUE_FOR_DNS_LEVEL_THREE_PRIMARY = "";
   
   /**
    * Test constant for applying Dns ovverride at client MAC level IPv6 level three primary value
    */
   public static final String STRING_DNS_IPV6_VALUE_FOR_DNS_LEVEL_THREE_PRIMARY = "";
   
   /** Test constant for string value MacAddress */
   public static final String STRING_DNS_MAC_ADDRESS = "MacAddress";
   
   /** Test constant for string value DnsIPv4 */
   public static final String STRING_DNS_IPV4 = "DnsIPv4";

   /** Test constant for string value DnsIPv6 */
   public static final String STRING_DNS_IPV6 = "DnsIPv6";

   /** Test constant for string value Tag */
   public static final String STRING_DNS_TAG = "Tag";
   
   /** Constant to hold primary */
   public static final String PRIMARY = "primary";

   /** Constant to hold nonprimary */
   public static final String NON_PRIMARY = "nonprimary";
   
   /**
    * enum variable to store the band setting options
    */
   public enum TELEMETRY_2_WEBPA_SETTINGS {
	FEATURE_ENABLE(BroadBandWebPaConstants.WEBPA_PARAM_FOR_TELEMETRY_2_0_ENABLE, "false", "true"),
	VERSION(BroadBandWebPaConstants.WEBPA_PARAM_FOR_TELEMETRY_2_0_VERSION, "1", "2"),
	CONFIG_URL(
		BroadBandWebPaConstants.WEBPA_PARAM_FOR_TELEMETRY_2_0_CONFIG_URL,
		"",
		AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_PROD_XCONF_TELEMETRY_VER2));

	String webpaParam;
	String factoryDefault;
	String defaultT2Config;

	public String getParam() {
	    return webpaParam;
	}

	public void setParam(String webpaParam) {
	    this.webpaParam = webpaParam;
	}

	private TELEMETRY_2_WEBPA_SETTINGS(String webpaParam, String factoryDefault, String defaultT2Config) {
	    this.setParam(webpaParam);
	    this.setFactoryDefault(factoryDefault);
	    this.setDefaultT2Config(defaultT2Config);
	}

	public String getDefaultT2Config() {
	    return defaultT2Config;
	}

	public void setDefaultT2Config(String defaultT2Config) {
	    this.defaultT2Config = defaultT2Config;
	}

	public String getFactoryDefault() {
	    return factoryDefault;
	}

	public void setFactoryDefault(String factoryDefault) {
	    this.factoryDefault = factoryDefault;
	}

   }

   /** Property key to retrieve prod xconf URL from stb.properties */
   public static final String PROP_KEY_PROD_XCONF_TELEMETRY_VER2 = "default.xconf.telemetry2.url";
   
   
   /** Constant represent log file /rdklogs/logs/dcmscript.log **/
   public static final String DCMRFC_LOG_FILE = "/rdklogs/logs/dcmrfc.log";
   
   /** Command cat /rdklogs/logs/dcmrfc.log **/
   public static final String CMD_CAT_DCMRFC_LOG = "cat " + DCMRFC_LOG_FILE;
   
   /** Process name telemetry 2 */
   public static final String PROCESS_NAME_TELEMETRY_2_0 = "telemetry2_0";
   
   /** Weba parameter to hold configuration URL for telemetry 2 */
   public static final String WEBPA_PARAM_FOR_TELEMETRY_2_0_CONFIG_URL = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.Telemetry.ConfigURL";

   /** Pay load data for telemetry 2 configurations */
   public static final String PROP_KEY_TELEMETRY_VER_2_CONFIG = "rfc.payload.enable.telemetryVer2Config";
   
   /** String which will be replaced by actual enable/disable value in payload data */
   public static final String TELEMETRY_ENABLE_VAUE = "<TELEMETRY_ENABLE>";

   /** String which will be replaced by actual enable/disable value in payload data */
   public static final String TELEMETRY_VERSION_VALUE = "<TELEMETRY_VERSION>";

   /** String which will be replaced by actual enable/disable value in payload data */
   public static final String TELEMETRY_CONFIG_URL_VALUE = "<CONFIG_URL>";
   
   /** Path to the telemetry 2.0 file */
   public static final String FILE_PATH_TELEMETRY_2_0 = "/rdklogs/logs/telemetry2_0.txt.0";
   
   /** Constant to store logs for invalid telemetry version 2 URL */
   public static final String LOGS_INDICATE_INVALID_TELEMETRY2_URL = "URL doesn't start with https or is invalid";
   
   /** String to store Backup */
   public static final String STRING_AUTOMATION_BACK_UP = "automation_BackUp";
   
   /** Constant to hold command to get /rdklogs/logs/telemetry2_0.txt.0 logs */
   public static final String CMD_GET_TELEMETRY_VER_TWO_LOGS = "cat " + FILE_PATH_TELEMETRY_2_0;
   
   /** Constant to store logs for XCONF re-try telemetry version 2 URL */
   public static final String LOGS_INDICATE_XCONF_RE_TRY_MSG = "Waiting for 180 sec before trying fetchRemoteConfiguration, No.of tries : 3";  
   
   /** Constant to store logs for XCONF re-try telemetry version 2 URL */
   public static final String LOGS_INDICATE_XCONF_RE_TRY_MSG_2 = "Waiting for 180 sec before trying fetchRemoteConfiguration, No.of tries : 2";
   
   /** Constant to store logs for XCONF fail telemetry version 2 URL */
   public static final String LOGS_INDICATE_XCONF_GET_INVALID = "T2:Curl GET of XCONF data failed";
   
   /** Time formart pattern finder for dd:mm:ss */
   public static final String TIME_FORMAT = "dd:mm:ss";
   
   /** Time formart pattern finder for dd:mm:ss */
   public static final String PATTERN_TIME_FORMAT = "(\\d\\d:\\d\\d:\\d\\d)\\."; 
   
   /** Command cat /rdklogs/logs/dcmscript.log **/
   public static final String CMD_CAT_RDKLOGS_DCMSCRIPT_LOG = "cat " + DCMSCRIPT_LOG_FILE;

   /** Regex to hold pattern matcher for T2 implementation */
   public static final String PATTERN_MATCHER_T2_IMPLEMENTATION = ".*parse will be handled by T2 implementation.*";
   
   /** Command to check self heal logs for telemetry process crash */
   public static final String CMD_CHECK_SELF_HEAL_LOGS_FOR_TELEMETRY2_CRASH = "grep -i \"RDKB_PROCESS_CRASHED : telemetry2_0 is not running, need restart\" /rdklogs/logs/SelfHeal.txt.0";
   
   /** Constant to hold logs for telemetry process restart */
   public static final String STRING_NEED_RESTART = "telemetry2_0 is not running, need restart";
   
   /** Command to get report sent success message from /rdklogs/logs/telemetry2_0.txt.0 */
   public static final String CMD_GET_REPORT_SENT_SUCCESS_MESSAGE_FROM_LOGS = "cat " + FILE_PATH_TELEMETRY_2_0
	    + "|grep -i \"Report Sent Successfully\"|tail -1";
   
   /** Constant to hold the string for checking report sent string */
   public static final String STRING_REPORT_SENT = "Report Sent Successfully";
   
   /** List of Pattern to be verified in telemtry log upload */
   public static final ArrayList<String> PATTERN_TELEMETRY_LOG_UPLOAD = new ArrayList<String>(
	    Arrays.asList("\"Profile\"", "\"Time\"", "\"mac\"", "\"erouterIpv4\"", "\"erouterIpv6\"", "\"PartnerId\"",
		    "\"Version\""));
   
   /** Pattern matcher to match the telemetry 2 */
   public static final String PATTERN_MATCHER_TELEMETRY_VERSION_2 = ".*\"T2\".*";
   
   /** String to store pattern */
   public static final String PATTERN_FOR_PROCESSID = "\\d+";
   
   /** Constant to hold command to check INFO/ERROR/WARN messages under /rdklogs/logs/telemetry2_0.txt.0 */
   public static final String STRING_FOR_INFO_WARN_ERROR_MESSAGE = "lvl=WARN|lvl=INFO|lvl=ERROR";
   
   /** Constant to store the file name dca_utility.sh */
   public static final String FILE_NAME_DCA_UTILITY = "dca_utility.sh";
   
   /** Constant to store regex to find the time reported in telemetry */
   public static final String REGEX_TIME_REPORTED_IN_TELEMETRY = "\"Time\":\"(.*)\"";
   
   /** Constant to hold build type sprint */
   public static final String BUILD_TYPE_SPRINT = "sprint";
	
   /** Constant to hold build type stable */
   public static final String BUILD_TYPE_STABLE = "stable";
	
   /** Constant to hold build type release */
   public static final String BUILD_TYPE_RELEASE = "release";
   
   /** STB Property key to store TELEMETRY VERSION When Enabled  */
   public static final String PROP_KEY_T2_ENABLED_VERSION = "current.t2enabled.version";
   
   /** STB Property key to store TELEMETRY VERSION */
   public static final String PROP_KEY_T2_VERSION = "t2.version";
   
   /** String variable to hold the DEFAULT constant value */
   public static final String DEFAULT = "DEFAULT";
   
   /** Command to give the version of OPenSSl used */
   public static final String COMMAND_FOR_OPENSSL_VERSION = "openssl version";
   
   /** Command to get the libssl.so file present in the build */
   public static final String COMMAND_TO_FETCH_LIBSSL_FILE = "ls /usr/lib/ | grep -i \"libssl.so\" |awk ' {printf \"/usr/lib/\"\\$NF}'";

   /** prefix Command to read OpenSSL version in ssl library */
   public static final String PREFIX_COMMAND_TO_READ_OPENSSL_OF_SSL_LIBRARY = "strings";

   /** postfix command to read OpenSSL version in ssl library */
   public static final String POSTFIX_COMMAND_TO_READ_OPENSSL_OF_SSL_LIBRARY = "| grep \"^OpenSSL.*[0-9][0-9][0-9]\"";
   
   /** Command to get the libcrypto.so file present in the build */
   public static final String COMMAND_TO_FETCH_LIBCRYPTO_FILE = "ls /usr/lib/ | grep -i \"libcrypto.so\" |awk ' {printf \"/usr/lib/\"\\$NF}'";

   /** Command to get libssl used by all running processes */
   public static final String COMMAND_TO_GET_LIBSSL_USED_IN_ALL_PROCESSES = "cat /proc/\\*/maps | grep -i \"libssl.so\" |awk ' {print \\$NF}' | uniq ";

   /** pattern to get libssl location from COMMAND_TO_GET__LIBSSL_USED_IN_ALL_PROCESSES response */
   public static final String PATTERN_TO_GET_LIBSSL = "(\\S+libssl\\S+)";
   
   /** Command to get libcrypto used by all running processes */
   public static final String COMMAND_TO_GET_LIBCRYPTO_USED_IN_ALL_PROCESSES = "cat /proc/\\*/maps | grep -i \"libcrypto.so\" |awk ' {print \\$NF}' | uniq ";

   /** pattern to get libssl location from COMMAND_TO_GET__LIBCRYPTO_USED_IN_ALL_PROCESSES response */
   public static final String PATTERN_TO_GET_LIBCRYPTO = "(\\S+libcrypto\\S+)";
   
   /** String for AdTrackerBlockingRFCEnable */
   public static final String ADTRACKER_BLOCKING_RFC_ENABLE = "AdTrackerBlockingRFCEnable";
   
   /*** File path for ADVSEClog.txt.0 ***/
   public static final String FILE_ADVSEC_0 = "/rdklogs/logs/ADVSEClog.txt.0";

 /** String variable to store emta detection * */
   public static final String STRING_EMTA_DETECTION = "emta detection";
   /** constant to pass as key value to validate emta detection */
   public static final String WAREHOUSE_GENERAL_EMTA_DETECTION = "WAREHOUSE_GENERAL_EMTA_DETECTION";

   /** constant to pass as key value to validate wireless ssid enable status for Xfinity Home 2.4GHz */
   public static final String WAREHOUSE_WIRELESS_SSID_ENABLE_XHS_2_4 = "WAREHOUSE_WIRELESS_SSID_ENABLE1";

   /** constant to pass as key value to validate wireless ssid1 enable status for Lost and Found 2.4GHz */
   public static final String WAREHOUSE_WIRELESS_SSID1_ENABLE_LNF_2_4 = "WAREHOUSE_WIRELESS_SSID_ENABLE2";

   /** constant to pass as key value to validate wireless ssid enable status for Lost and Found 2.4GHz */
   public static final String WAREHOUSE_WIRELESS_SSID2_ENABLE_LNF_2_4 = "WAREHOUSE_WIRELESS_SSID_ENABLE3";

   /** constant to pass as key value to validate wireless ssid enable status for Lost and Found 5GHz */
   public static final String WAREHOUSE_WIRELESS_SSID_ENABLE_LNF_5G = "WAREHOUSE_WIRELESS_SSID_ENABLE1_5G";
   
   /** webpa Parameter for 2.4GHz guest SSID name */
   public static final String WAREHOUSE_PARAM_DEVICE_WIFI_SSID_10002_SSID = "WAREHOUSE_WIRELESS_SSID_ENABLE1";

   /** Webpa param for 5GHz guest SSID name */
   public static final String WAREHOUSE_PARAM_DEVICE_WIFI_SSID_10102_SSID = "WAREHOUSE_WIRELESS_SSID_ENABLE_5G";

   
   /** constant to pass as key value to validate wireless ssid password for private 2.4GHz */
   public static final String WAREHOUSE_WIRELESS_SSID_PASSWORD_PRIVATE_2G = "WAREHOUSE_WIRELESS_SSID_PASSWORD_2G";

   /** constant to pass as key value to validate wireless ssid password for private 5GHz */
   public static final String WAREHOUSE_WIRELESS_SSID_PASSWORD_PRIVATE_5G = "WAREHOUSE_WIRELESS_SSID_PASSWORD_5G";

   /** constant to pass as key value to validate 2g channel */
   public static final String WAREHOUSE_WIFI_2G_CHANNEL = "WAREHOUSE_WIFI_2_4_OID";

   /** constant to pass as key value to validate 5g channel */
   public static final String WAREHOUSE_WIFI_5G_CHANNEL = "WAREHOUSE_WIFI_5_0_OID";

   /** constant to pass as key value to validate 2g wireless channel */
   public static final String WAREHOUSE_WIFI_2G_WIRELESS_CHANNEL = "WAREHOUSE_WIFI_2_4_OID_1";

   /** constant to pass as key value to validate 5g wireless channel */
   public static final String WAREHOUSE_WIFI_5G_WIRELESS_CHANNEL = "WAREHOUSE_WIFI_5_0_OID_1";

   /** constant to pass as key value to validate upgrade server */
   public static final String WAREHOUSE_UPGRADE_WITH_RESET = "WAREHOUSE_UPGRADE_WITH_RESET";
   
   /** String value to store CI Xconf url */
   public static final String PROP_KEY_XCONF_CI_SERVER_SOFTWARE_UPDATE_URL_IPV6 = "xconf.ci.server.sw.update.url.ipv6";

   /** Constant for setting null value using Dmcli */
   public static final String DMCLI_NULL_VALUE = "\"\"";
   
   /**
    * Map with security mode value and corresponding key to retrieve the security mode value
    */
   public static HashMap<String, Integer> MAP_SECURITY_MODE_AND_KEY_TO_GET_POSSIBLE_SECURITY_MODE_VALUES = new HashMap<String, Integer>() {
	{
	    put(BroadBandTestConstants.SECURITY_MODE_NONE, BroadBandTestConstants.CONSTANT_0);
	    put(BroadBandTestConstants.SECURITY_MODE_WEP_64, BroadBandTestConstants.CONSTANT_1);
	    put(BroadBandTestConstants.SECURITY_MODE_WEP_128, BroadBandTestConstants.CONSTANT_2);
	    put(BroadBandTestConstants.SECURITY_MODE_WPA2_PERSONAL, BroadBandTestConstants.CONSTANT_3);
	    put(BroadBandTestConstants.SECURITY_MODE_WPA_WPA2_PERSONAL, BroadBandTestConstants.CONSTANT_7);
	    put(BroadBandTestConstants.SECURITY_MODE_WPA2_ENTERPRISE, BroadBandTestConstants.CONSTANT_5);
	    put(BroadBandTestConstants.SECURITY_MODE_WPA_WPA2_ENTERPRISE, BroadBandTestConstants.CONSTANT_8);
	}
   };
   
   /** Constant for Security mode WEP-64 */
   public static final String SECURITY_MODE_WEP_64 = "WEP-64";

   /** Constant for Security mode WEP-128 */
   public static final String SECURITY_MODE_WEP_128 = "WEP-128";
   
   /** Constant for Security mode WPA-WPA2-Enterprise */
   public static final String SECURITY_MODE_WPA_WPA2_ENTERPRISE = "WPA-WPA2-Enterprise";
   
   /** Constant to Hold the XHS Security mode */
   public static final String SECURITY_MODE_WPA_WPA2_PERSONAL = "WPA-WPA2-Personal";
   
   /** The constant for checking bridge-static mode. */
   public static final String LAN_MANAGEMENT_MODE_BRIDGE_STATIC = "bridge-static";
   
   /** Test Constants for Parodus Integration with Seshat */
   /** Seshat - Process Name */
   public static final String PROCESS_NAME_SESHAT = "seshat";
   
   /** Constant to hold the SNMP response as Wrong Value */
   public static final String WRONG_VALUE = "Reason: wrongValue";
   
   /** Variable to hold string notify */
   public static final String NOTIFY = "notify";
   
   /** Variable to hold string data type */
   public static final String ATTRIBUTES = "attributes";
   
   /** Variable to hold String block */
   public static final String BLOCK = "Block";

   /** Variable to hold String mac address */
   public static final String MACADDRESS = "MACAddress";

   /** Variable to hold String type */
   public static final String TYPE = "Type";

   /** Variable to hold string Description */
   public static final String DESCRIPTION = "Description";

   /** Variable to hold Dummy Mac */
   public static final String DUMMY_MAC = "01:01:01:01:01:01";
   
   /** Log message for add_row request */
   public static final String LOG_MESSAGE_ADD_ROW_REQ = "\"ADD_ROW Request\"";
   
   /** Variable to hold string Yiaddr */
   public static final String IPADDRESS = "Yiaddr";
   
   /** Test SSID Name 5 GHz */
   public static final String TEST_SSID_5 = "test-ssid-5";
   
   /** Pattern to retrieve first 3 digits of IPv4 address */
   public static final String PATTERN_TO_RETRIEVE_FIRST_3_DIGITS_OF_IPv4_ADDRESS = "((\\d+\\.){3})\\d+";

   /** Pattern to retrieve first 3 digits of IPv4 address */
   public static final String PATTERN_TO_RETRIEVE_LAST_DIGIT_OF_IPv4_ADDRESS = "\\d+\\.\\d+\\.\\d+\\.(\\d+)";
   
   /** String Constant for Slash Symbol */
   public static final String SLASH_SYMBOL = "/";
   
   /** Text Hello World */
   public static final String TEXT_HELLO_WORLD = "Hello World";

   /** Error Message Permission Denied */
   public static final String ERROR_PERMISSION_DENIED = "Permission denied";
   
   /** Test Shell Script File Name */
   public static final String FILE_TEST_SHELL_SCRIPT_CONTENT = "\"#!/bin/bash\n echo \"Hello World\"\"";
   
   /** String Constant redirect operator */
   public static String REDIRECT_OPERATOR = " > ";
   
   /**
    * system-local.conf file location. This file is removed now and had authentication detail for DBus messaging
    */
   public static final String FILE_SYSTEM_LOCAL_CONF = "/etc/dbus-1/system-local.conf";
   
   /** NVRAM partition file name **/
   public static final String NVRAM_PARTITION = "\"nvram\"";
   
   /** String variable to store nvram partition * */
   public static final String STRING_PARTITION_NVRAM = "/nvram";
   
   /** pattern to get read write permission for nvram* */
   public static final String PATTERN_GET_READ_WRITE_PERMISSION_NVRAM_PARTITION = "\\/nvram.*[(](rw)[)]?";
   
   /** String variable to read write permission for partition* */
   public static final String STRING_READ_WRITE_PARTITION = "rw";
   
   /** NVRAM Dummytest file **/
   public static final String NVRAM_DUMMY_TEST = "/nvram/dummytest.sh";
   
   /** NVRAM2 partition file name **/
   public static final String NVRAM2_PARTITION = "\"nvram2\"";
   
   /** String variable to store nvram2 partition* */
   public static final String STRING_PARTITION_NVRAM2 = "/nvram2";
   
   /** pattern to get read write permission for nvram2* */
   public static final String PATTERN_GET_READ_WRITE_PERMISSION_NVRAM2_PARTITION = "\\/nvram2.*[(](rw)[)]?";
   
   /** NVRAM2 Dummytest file **/
   public static final String NVRAM2_DUMMY_TEST = "/nvram2/dummytest.sh";
   
   /** tmp partition file name **/
   public static final String TMP_PARTITION = "\"tmp\"";
   
   /** String variable to store tmp partition * */
   public static final String STRING_PARTITION_TMP = "/tmp";
   
   /** pattern to get read write permission for tmp* */
   public static final String PATTERN_GET_READ_WRITE_PERMISSION_TMP_PARTITION = "\\/tmp.*[(](rw)[)]?";
   
   /** regex to find temporary mount point */
   public static final String REGEX_TO_GET_TEMPORARY_MOUNT_POINT = "type\\s+tmpfs\\W";
   
   /** tmp dummytest file **/
   public static final String TMP_DUMMY_TEST = "/tmp/dummytest.sh";
   
   /** Minidumps partition file name **/
   public static final String MINIDUMPS_PARTITION = "\"minidumps\"";
   
   /** String variable to store MINIDUMPS partition * */
   public static final String STRING_PARTITION_MINIDUMPS = "/minidumps";
   
   /** pattern to get read write permission for minidumps* */
   public static final String PATTERN_GET_READ_WRITE_PERMISSION_MINIDUMPS_PARTITION = "\\/minidumps.*[(](rw)[)]?";
   
   /** minidumps dummytest file **/
   public static final String MINIDUMPS_DUMMY_TEST = "/minidumps/dummytest.txt";
   
   /** rdklogs partition file name **/
   public static final String RDKLOGS_PARTITION = "\"rdklogs\"";
   
   /** p String variable to store rdklogs partition* */
   public static final String STRING_PARTITION_RDKLOGS = "/rdklogs";
   
   /** pattern to get read write permission for rdklogs* */
   public static final String PATTERN_GET_READ_WRITE_PERMISSION_RDKLOGS_PARTITION = "\\/rdklogs.*[(](rw)[)]?";
   
   /** rdklogs dummytest file **/
   public static final String RDKLOGS_DUMMY_TEST = "/rdklogs/dummytest.sh";
   
   /** cron partition file name **/
   public static final String CRON_PARTITION = "\"cron\"";
   
   /** pattern to get read write permission for cron* */
   public static final String PATTERN_GET_READ_WRITE_PERMISSION_CRON_PARTITION = "\\/cron.*[(](rw)[)]?";
   
   /** String variable to store cron partition * */
   public static final String STRING_PARTITION_CRON = "/cron";
   
   /** etc dummytest file **/
   public static final String ETC_DUMMY_TEST = "/etc/dummytest.sh";
   
   /** dhcp_static_hosts partition file name **/
   public static final String DHCP_STATIC_HOSTS_PARTITION = "\"dhcp_static_hosts\"";
   
   /** String variable to store dhcp_static_hosts partition * */
   public static final String STRING_PARTITION_DHCP_STATIC_HOSTS = "/dhcp_static_hosts";
   
   /** pattern to get read write permission for dhcp_static_hosts* */
   public static final String PATTERN_GET_READ_WRITE_PERMISSION_DHCP_STATIC_HOSTS_PARTITION = "\\/dhcp_static_hosts.*[(](rw)[)]?";
   
   /** xupnp partition file name **/
   public static final String XUPNP_PARTITION = "\"xupnp\"";
   
   /** String variable to store xupnp partition * */
   public static final String STRING_PARTITION_XUPNP = "/xupnp";
   
   /** pattern to get read write permission for xupnp* */
   public static final String PATTERN_GET_READ_WRITE_PERMISSION_XUPNP_PARTITION = "\\/xupnp.*[(](rw)[)]?";
   
   /** dibbler partition file name **/
   public static final String DIBBLER_PARTITION = "\"dibbler\"";
   
   /** String variable to store dibbler partition * */
   public static final String STRING_PARTITION_DIBBLER = "/dibbler";
   
   /** pattern to get read write permission for xupnp* */
   public static final String PATTERN_GET_READ_WRITE_PERMISSION_DIBBLER_PARTITION = "\\/dibbler.*[(](rw)[)]?";
   
   /** command to list the contents of given folder in long listing format */
   public static final String CMD_TO_LONGLIST_FOLDER_FILES = "ls -la";
   
   /** Constant for Radius Server IPAddr */
   public static final String RADIUS_SERVER_IPADDR = "";
   
   /** String for 'Requested' */
   public static final String STRING_REQUESTED = "Requested";
   
   /** String for 'Completed' */
   public static final String STRING_COMPLETE = "Complete";
   
   /** String for 'null' */
   public static final String STRING_NULL = "null";
   
   /** SSID name starting with XH */
   public static final String SSID_NAME_WITH_WHITSPACE_ONLY = "  ";
   
   /** Pattern to retrieve ipv6 address */
   public static final String PATTERN_TO_RETRIVE_IPV6_ADDRESS_FROM_NSLOOKUP_FACEBOOK = ".*facebook.com\\s*has AAAA address(.*)\\s*Authoritative.*";
  
   /** String constant 3600 */
   public static final String STRING_CONSTANT_3600 = "3600";
   
   /** Harvester Process Name */
   public static final String PROCESS_NAME_HARVESTER = "harvester";
   
   /** Constant to hold Lmlite Process Output */
   public static final String LMLITE_PROCESS_OUTPUT = "usr/bin/lmlite";
   
   
   
}

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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.automatics.snmp.SnmpDataType;
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
    
    /** Constant for number 10 */
    public static final int CONSTANT_10 = 10;

    /** Constant to integer value 25 */
    public static final int CONSTANT_25 = 25;

    /** constant to value 30 */
    public static final int CONSTANT_30 = 30;
    
    /** Constant for number 11 */
    public static final int CONSTANT_11 = 11;
    
    /** Integer value 5 */
    public static final Integer INTEGER_VALUE_5 = 5;
    
    /** String constant value 1 */
    public static final String STRING_CONSTANT_1 = "1";
    
    /** String value 2 */
    public static final String STRING_VALUE_TWO = "2";
    
    /** String to store value 4 */
    public static final String STRING_VALUE_4 = "4";
    
    /** String value 5 */
    public static final String STRING_VALUE_FIVE = "5";

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
    public static final String OPERATING_MODE_BGN = "g,n";

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
    
    /** String parameter for factory resetting the box for Sky Hub Device */
    public static final String STRING_FACTORY_RESET_PARAMETER_FOR_SKYHUB = "Router,Wifi,VoIP,Firewall";
    
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
	PARTNER_DUMMY("dummy");

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
   
   /** The array list of Partner ID for Devices */
   public static final List<String> PARTNER_ID_LIST = new ArrayList<String>() {
	{

	}
   };
   
   
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
   public static final String CONSTANT_SSID_NAME_PREFIX = " TBD ";
     
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
   
   /** The Map with various Partner ID as key and SSID Prefix as values */
   public static final Map<String, String> PARTNER_SPECIFIC_DEFAULT_SSID_FOR_NON_CISCO_RESIDENTIAL_TYPE = new HashMap<String, String>() {
	{

	}
   };
   
   /** The Map with various Device Model as key and Partner Specific Map as value */
   public static final Map<String, Map<String, String>> DEFAULT_SSID_PREFIX_FOR_DEVICE_AND_PARTNER_SPECIFIC = new HashMap<String, Map<String, String>>() {
	{

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
}

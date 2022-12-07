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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpMib;
import com.automatics.snmp.SnmpDataType;
import com.automatics.utils.AutomaticsPropertyUtility;

public class BroadBandTestConstants extends RDKBTestConstants {

    /**
     * Method to create a map to store the Access point mapping with corresponding digits.
     * 
     * @return map with access points.
     */
    private static Map<String, String> accessPointMapping() {
	Map<String, String> mapping = new HashMap<String, String>() {
	    {
		put("1", "10001");
		put("2", "10101");
		put("3", "10002");
		put("4", "10102");
		put("5", "10003");
		put("6", "10103");
		put("7", "10004");
		put("8", "10104");
		put("9", "10005");
		put("10", "10105");
		put("11", "10006");
		put("12", "10106");
		put("13", "10007");
		put("14", "10107");
		put("15", "10008");
		put("16", "10108");

	    }
	};

	return Collections.unmodifiableMap(mapping);
    }

    /** Constant to hold command to get band preference */

    /**
     * enum variable to store registry value for wireless mode on windows clients
     */
    public enum WINDOWS_WIRELESS_MODE_OPTIONS {
	A_802_11("802.11a", "17"),
	B_802_11("802.11b", "0"),
	G_802_11("802.11g", "16"),
	B_G_802_11("802.11b/g", "32"),
	A_G_802_11("802.11a/g", "18"),
	A_B_G_802_11("802.11a/b/g", "34");

	String option = "";
	String name = "";

	public String getOption() {
	    return option;
	}

	public void setOption(String option) {
	    this.option = option;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	private WINDOWS_WIRELESS_MODE_OPTIONS(String name, String option) {
	    this.option = option;
	    this.name = name;
	}

    }

    /**
     * The hash map to store the Access point mapping with corresponding digits
     */
    public static final Map<String, String> ACCESS_POINT_MAPPING = accessPointMapping();

    /** Pattern to verify ca cert path */
    public static final String PATH_FOR_CA_CERTS = "/usr/share/ca-certificates";

    /** security certificates of speedtest process */
    public static final String SPEED_TEST_SECURITY_CERTIFICATE = "/etc/ssl/certs/ca-certificates.crt";

    /** constant represents the ssl certificates folder */
    public static final String SSL_CERTS_FOLDER = "/etc/ssl/certs/";

    /** String variable to store common ssid for band steering */
    public static final String TEXT_RDKB = "RDKB";

    /** Constant to hold value string Auto */
    public static final String STRING_AUTO = "Auto";

    /** String value for response Timout **/
    public static final String TIME_OUT_RESPONSE = "Timeout";

    /** String value for Wi-Fi */
    public static final String WIFI = "Wi-Fi";

    /** String constant to hold wifi public */
    public static final String WIFI_PUBLIC = "Public Wifi";

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

    /** Constant for number 12 */
    public static final int CONSTANT_12 = 12;

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

    /** Constant to integer value 26 */
    public static final int CONSTANT_26 = 26;

    /** Constant to integer value 32 */
    public static final int CONSTANT_32 = 32;

    /** Constant to integer value 40 */
    public static final int CONSTANT_40 = 40;

    /** Constant for number 1000 */
    public static final int CONSTANT_1000 = 1000;

    /** Constant for number -3 */
    public static final int CONSTANT_NEGATIVE_3 = -3;

    /** Integer value 5 */
    public static final Integer INTEGER_VALUE_5 = 5;

    /** String constant value 1 */
    public static final String STRING_CONSTANT_1 = "1";

    /** String constant 2 */
    public static final String STRING_CONSTANT_2 = "2";

    /** String constant 3 */
    public static final String STRING_CONSTANT_3 = "3";

    /** Constant to integer value 34 */
    public static final int CONSTANT_34 = 34;

    /** String constant value 2000 */
    public static final String STRING_CONSTANT_2000 = "2000";

    /** String value 2 */
    public static final String STRING_VALUE_TWO = "2";

    /** String to store value 4 */
    public static final String STRING_VALUE_4 = "4";

    /** String value 5 */
    public static final String STRING_VALUE_FIVE = "5";

    /** String value for 7 */
    public static final String STRING_VALUE_SEVEN = "7";

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
    /** String value 30 */
    public static final String STRING_30 = "30";
    /** constant to hold string value 400 */
    public static final String STRING_400 = "400";

    /** Constant to hold 153 */
    public static final String CONSTANT_153 = "153";

    /** String value for 255 */
    public static final String STRING_VALUE_TWO_HUNDRED_AND_FIFTY_FIVE = "255";

    /** String value 40 */
    public static final String STRING_VALUE_FORTY = "40";

    /** constant for Ping Interval as 15 mins */
    public static final String CONSTANT_PING_INTERVAL = "15";

    /** Constant for number 21 */
    public static final int CONSTANT_21 = 21;

    /** Constant for Character Hyphen */
    public static final String CHARACTER_HYPHEN = "-";

    /** Symbol quotes ("). */
    public static final String SYMBOL_QUOTES = "\"";

    public static final String CHAR_NEW_LINE = "\n";

    /** String value Test1 */
    public static final String STRING_TEST_1 = "Test1";

    /** String value Test2 */
    public static final String STRING_TEST_2 = "Test2";

    /** constant to hold string value 1470 */
    public static final String STRING_1470 = "1470";

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

    /** String to store Not Applicable */
    public static final String NOTAPPLICABLE_VALUE = "Not Applicable";

    /** Time value for DHCP lease time */
    public static final String STRING_LEASE_TIME_VALUE = "120";

    /** REGEX for get BSSID in atom device */
    public static final String REGEX_TO_GET_BSSID_MAC_ADDRESS_ATOM = "Access Point: (\\w+:\\w+:\\w+:\\w+:\\w+:\\w+)";

    /** REGEX for get BSSID in Xf3 device */
    public static final String REGEX_TO_GET_BSSID_MAC_ADDRESS_FIBRE = "BSSID: (\\w+:\\w+:\\w+:\\w+:\\w+:\\w+)";

    /** String Constant for Process Name */
    public static final String PROCESS_NAME = "PROCESS_NAME";

    /**
     * Constant to hold the value to set firewall IPv4 & IPv6 to 'Custom Security'
     */
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
     * Command to get address of the interface. Replace the <REPLACE> with interface name
     */
    public static final String GET_MAC_FROM_INTERFACE_IN_LINUX = "cat /sys/class/net/<REPLACE>/address";

    /**
     * System property to identify whether WebPA connectivity broken or not.
     */
    public static final String SYSTEM_PROPERTY_WEBPA_CONNECTIVITY_BROKEN = "WEBPA_CONNECTIVITY_BROKEN";

    /** Constant to hold the value to set firewall IPv4 to 'Minimum Security' */
    public static final String FIREWALL_IPV4_MINIMUM_SECURITY = "Low";

    /** Constant to hold the value to set firewall IPv4 to 'Maximum Security' */
    public static final String FIREWALL_IPV4_MAXIMUM_SECURITY = "High";

    /** Constant to hold Typical Firewall Level */
    public static final String TYPICAL_FIREWALL_SECURITY = "Typical Security (Medium)";

    /** Constant to hold Maximum Firewall Level */
    public static final String MAXIMUM_FIREWALL_SECURITY = "Maximum Security (High)";

    /** Constant to hold custom IPv4 Firewall Level */
    public static final String CUSTOM_IPV4_FIREWALL_SECURITY = "Custom Security";

    /** Constant to hold 2.4GHZ index */
    public static final String RADIO_24_GHZ_INDEX = "10000";

    /** Constant to hold 5GHZ index */
    public static final String RADIO_5_GHZ_INDEX = "10100";

    /** inactive response of systemsctl */
    public static final String SYSTEMCTL_INACTIVE_RESPONSE = "inactive";

    /** active response of systemctl */
    public static final String SYSTEMCTL_ACTIVE_RESPONSE = "active";

    /** String for wifi Process */
    public static final String WIFI_PROCESS_NAME = "/usr/bin/CcspWifiSsp";

    /** Command to know the status of wifi mesh service */
    public static final String COMMAND_TO_CHECK_THE_RUNNING_STATUS_OF_WIFIMESH_SERVICE = "systemctl status meshwifi.service";

    /**
     * Pattern to get the Active status of wifimesh service from systemctl response
     */
    public static final String PATTERN_TO_GET_STATUS_FROM_SYSTEMCTL_RESPONSE = "Active:\\s+(\\S+)";

    /** Pattern Matcher to retrieve the Pid of Ccsp wifi process */
    public static final String PATTERN_MATCHER_TO_GET_CCSP_WIFI_PROCESS_PID = "(\\d+)\\sroot\\s+[a-zA-Z0-9.]+\\sS\\s+/usr/bin/CcspWifiSsp\\s+";

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
    public static final String CMD_GET_WIFI_NAME = "netsh wlan show profiles"; //changed

    /** Constant for iwconfig */
    public static final String CMD_IW_CONFIG_PATH = "/sbin/iwconfig";

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

    /** Property key for Command to check running status of dnsmasq service */
    public static final String PROP_KEY_CMDTOCHECK_RUNNING_STATUS_DNSMASQ_SERVICE = "cmd.tochk.runningsts.dnsmasqservice";

    /** Property key for pattern to check status of dnsmasq service */
    public static final String PROP_KEY_PATTERN_TOCHECK_DNSMASQ_SERVICE = "pattern.tochk.dnsmasqservice";

    /** Property key for expected response */
    public static final String PROP_KEY_EXPECTED_RESPONSE = "expected.response";

    /** The property key for pamSearchLog message */
    public static final String PROP_KEY_PAMSEARCHLOG = "pamSearchLog.device.";

    /** The property key for pamSearchLog message */
    public static final String PROP_KEY_PAMSEARCHALERT = "pamLog.alert.";

    /** The property key for pamSearchLog FR message */
    public static final String PROP_KEY_PAMLOG_FR = "pamLog.device.fr.";

    /** The property key for pamSearchLog message */
    public static final String PROP_KEY_PAMLOG = "pamLog.device.";

    /** The property key for pamSearchLog message before FR */
    public static final String PROP_KEY_PAMLOG_BFR = "pamLog.device.bfr.";

    /** The property key for pamSearchLog message post reboot */
    public static final String PROP_KEY_PAMLOG_POSTREBOOT = "pamLog.device.postreboot.";

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

    /** WiFi region code USI */
    public static final String WIFI_POWER_LEVEL_USI = "USI";

    /** WiFi region code CAI */
    public static final String WIFI_POWER_LEVEL_CAI = "CAI";

    /** Simple Data Format - Data/Time in Log Messages */
    public static final String TIMESTAMP_FORMAT_LOG_MESSAGE = "yyMMdd-HH:mm:ss";

    /** String regex to get ip address from ifconfig value */
    public static final String STRING_REGEX_ADDR = "addr:";

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

    public static final String PARTNER_ID = "partner.id";

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

    /** Log for reboot reason on webpa */
    public static final String RDKB_REBOOT_REASON_TR69_REBOOT = "tr069-reboot";

    /** Log for TR69 reboot */
    public static final String RDKB_REBOOT_REASON_TR69_LOG = "RDKB_REBOOT : RebootDevice triggered from TR69 with value 'Device'";

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

    /** Number Eight */
    public static final int EIGHT_NUMBER = 8;

    /** Integer value 13 */
    public static final Integer INTEGER_VALUE_13 = 13;

    /** Constants for Telemetry Marker */
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
     * Map of Wi-Fi SSID configuration details ( Private SSID ). SSID status includes the Enable status, status, name,
     * passphrase and security mode).
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

    public static final String[] REPLACE = { "", "", "", "", "", "", "", "", "", "", "" };

    /** The constant holds the error message for 'CDL' failure. */
    public static final String BUILD_NOT_AVAILABLE = "Build '<buildImageName>' is not available in CDL server, "
	    + "check whether the jenkins status in portal is success and build is uploaded to cdl servers";

    /** Test Constants for WebPA Telemetry */
    /** Process Name for WebPA */
    public static final String PROCESS_NAME_WEBPA = "webpa";

    /** Constant to hold Webpa version Pattern Matcher */
    public static final String WEBPA_VERSION_PATTERN_MATCHER = "WEBPA\\-(\\d+)\\.(\\d+)-.*";

    /** Constant to hold attenuation for 5 GHZ */
    public static final Double CHANNEL_ATTENUATION_5_GHZ = 0.0;

    /** sprint keyword to search in build name */
    public static final String STRING_SPRINT_IN_BUILD_NAME = "sprint";

    /** String to store fimrware version branch stable */
    public static final String FIRMWARE_VERSION_BRANCH_STABLE = "stable";

    /** Feature keyword to search in build name */
    public static final String STRING_FEATURE_IN_BUILD_NAME = "feature";

    /** Constant to hold core version from Regular release build Pattern Matcher */
    public static final String CORE_VERSION_FROM_REGULAR_RELEASE_BUILD_PATTERN_MATCHER = "_(\\d+.\\d+)";

    /**
     * Constant to hold Regular release build with 2 fields in core version Pattern Matcher
     */
    public static final String PROPER_RELEASE_BUILD_PATTERN_MATCHER = "_(\\d+.\\d+)+p(\\d+)+s(\\d+)_";

    /** Constant to hold Regular release build Pattern Matcher */
    public static final String REGULAR_RELEASE_BUILD_PATTERN_MATCHER = "_(\\d+.\\d)+p(\\d+)+s(\\d+)_";

    /** Constant to hold p version from Regular release build Pattern Matcher */
    public static final String P_VERSION_FROM_REGULAR_RELEASE_BUILD_PATTERN_MATCHER = "p(\\d+)s";

    /** Constant to hold s version from Regular release build Pattern Matcher */
    public static final String S_VERSION_FROM_REGULAR_RELEASE_BUILD_PATTERN_MATCHER = "s(\\d+)_";

    /** Constant to hold spin release build Pattern Matcher */
    public static final String SPIN_BUILD_PATTERN_MATCHER = "_(\\d+.\\d)+s(\\d+)_";

    /**
     * Constant to hold spin release build with 2 fields in core version Pattern Matcher
     */
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

    /** Constant to hold Custom Security pop up message */
    public static final String CUSTOM_SECURITY_POP_UP_MESSAGE = "You are trying to disable the firewall. It is a security risk";

    /**
     * stb properties key for payload data to enable configurable ssh rfc in xconf to enable
     */
    public static final String PROP_KEY_PAYLOAD_ENABLE_CONFIGURABLE_SSH = "rfc.configurablessh.enable.payload";

    /** Log string list of ips for configurable SSH in stb.properties file */
    public static final String PROP_KEY_SSH_WHITELIST_IPS = "rfc.configurablessh.iplist";

    public static final String PROP_KEY_NON_WHITE_LISTED_JUMP_SERVER_IPV6 = "wl.server.ip.ipv6";

    /**
     * stb properties key for payload data to disable configurable ssh rfc in xconf to disable
     */
    public static final String PROP_KEY_PAYLOAD_DISABLE_CONFIGURABLE_SSH = "rfc.configurablessh.disable.payload";

    /** RFC feature name IDS */
    public static final String FEATURE_NAME_IDS = "IDS";

    /** Pay load data for IDS enable */
    public static final String PROP_KEY_PAYLOAD_IDS_ENABLE = "rfc.ids.enable";

    /** Pay load data for IDS disable */
    public static final String PROP_KEY_PAYLOAD_IDS_DISABLE = "rfc.ids.disable";

    /** Test constant for string FirewallPort */
    public static final String FIREWALL_PORT = "FirewallPort";

    /**
     * stb properties key for payload data to enable configurable ssh rfc in xconf to enable
     */
    public static final String PROP_KEY_PAYLOAD_ENABLE_FIREWALL_PORT = "rfc.firewall.enable";

    /**
     * stb properties key for payload data to disable configurable ssh rfc in xconf to disable
     */
    public static final String PROP_KEY_PAYLOAD_DISABLE_FIREWALL_PORT = "rfc.firewall.disable";

    /** RFC feature name IDS */
    public static final String RFC_FEATURE_NAME_IDS1 = "IDS1";

    /** Pay load data for IDS enable */
    public static final String PROP_KEY_PAYLOAD_IDS1_ENABLE = "rfc.ids1.enable";

    /** Pay load data for IDS disable */
    public static final String PROP_KEY_PAYLOAD_IDS1_DISABLE = "rfc.ids1.disable";

    /** String constant to store configurable telemetry feature name value */
    public static final String CONFIGURABLE_TELEMETRY = "configurableTelemetry";

    /**
     * Property key for rfc payload data in stb.props to configure unique telemetry tag
     */
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

    /**
     * Property key to read payload to enable/disable encrypt cloud upload feature
     */
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

    /**
     * RFC feature name for Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.PrivacyProtection.Enable
     */
    public static final String FEATURE_NAME_PRIVACY_PROTECTION_ENABLE = "PRIVACY_PROTECTION_ENABLE";

    /**
     * Pay load data for Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.PrivacyProtection.Enable true
     */
    public static final String PROP_KEY_PAYLOAD_PRIVACY_PROTECTION_ENABLE = "rfc.privacy.protection.enable";

    /**
     * Pay load data for Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.PrivacyProtection.Enable false
     */
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

    /**
     * RFC feature name for Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.newNTP.Enable
     */
    public static final String FEATURE_NAME_NTP_TIME_SERVER = "NEW_NTP_UPDATE";

    /**
     * Pay load data for Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.newNTP.Enable true
     */
    public static final String PROP_KEY_PAYLOAD_NEW_NTP_ENABLE = "rfc.newNTP.enable.payload";

    /**
     * Pay load data for Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.newNTP.Enable false
     */
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

    /**
     * stb properties key for payload data to enable configurable hwHealthTest rfc in xconf to enable
     */
    public static final String PROP_KEY_PAYLOAD_ENABLE_CONFIGURABLE_HHT = "rfc.hht.enable";

    /**
     * stb properties key for payload data to disable configurable hwHealthTest rfc in xconf to disable
     */
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

    /**
     * The constant holding command for seeing the process status of dnsmasq process.
     */
    public static final String STRING_DNSMASQ_PROCESS = "dnsmasq";

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
    public static final String PRIMARY_REMOTE_ENDPOINT = BroadbandPropertyFileHandler.getPrimaryRemoteEndPoint();

    /** Constant for Secondary remote endpoint */
    public static final String SECONDARY_REMOTE_ENDPOINT = BroadbandPropertyFileHandler.getSecondaryRemoteEndPoint();

    /** Pattern to get public wifi status from rfc config data */
    public static final String PATTERN_GET_PUBLIC_WIFI_STATUS_FROM_RFC_CONFIG = "tr181.Device.DeviceInfo.X_COMCAST_COM_xfinitywifiEnable\\W+(\\w+)";

    /**
     * Default firmware maintenance upgrade window start time.
     */
    public static final String DEFAULT_FIRMWARE_UPGRADE_MAINTENANCE_WINDOW_START_TIME = "3600";

    /**
     * Default firmware maintenance upgrade window end time.
     */
    public static final String DEFAULT_FIRMWARE_UPGRADE_MAINTENANCE_WINDOW_END_TIME = "14400";

    /** Test constant for Global Dns IPv6 default value */
    public static final String STRING_DEFAULT_GLOBAL_DNS_IPV6_VALUE = "";

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

    /**
     * String which will be replaced by actual enable/disable value in payload data
     */
    public static final String ENABLE_DISABLE_VALUE = "ENABLEDISABLEVALUE";

    /** Command tot remove /opt/dcm.properties **/
    public static final String CMD_REMOVE_NVRAM_DCM_PROPERTIES = "rm -rf " + DCM_PROPERTIES_FILE_NVRAM_FOLDER;

    /** The constant for the build name for signed image. */
    public static final String SIGNED_BUILD_IMAGE_EXTENSION = "-signed";

    /** The property key for signed extensions. */
    public static final String PROP_KEY_SIGNED_EXTENSION = "build.signed.extension.";

    /** The property key for signed extensions. */
    public static final String PROP_KEY_AVG_RESPONSE_TIME = "average.responsetime.";

    /** The property key for signed extensions. */
    public static final String MANUFACTURERNAME_VIAWEBPA = "manufacturerName.RetrievedViaWebpa.";

    /** The property key for 2ghz operating standars */
    public static final String OPERATING_STANDARDS_2GHZ_ARM = "operatingstandards.2ghz.arm.";

    /** The property key for 2ghz operating standars */
    public static final String OPERATING_STANDARDS_2GHZ_ATOM = "operatingstandards.2ghz.atom.";

    /** The property key for 2ghz operating standars */
    public static final String LOGFILENAME_ATOM = "operatingstandards.2ghz.atom.";

    /** Pattern to get Uptime from snmp response string */
    public static final String SYS_UP_TIME_INSTANCE = "DISMAN-EVENT-MIB::sysUpTimeInstance = (\\d+:\\d+:\\d+:\\d+)";

    /** Pattern to extract failure reason trace from SNMP output */
    public static final String PATTERN_FINDER_FAILURE_REASON = ".*\n(.*)\\s+\\(";

    /** Constant to hold the SNMP response as Not Writable */
    public static final String NOT_WRITABLE = "Reason: notWritable";

    /** The property key for wifi interface name. */
    public static final String PROP_KEY_WIFI_PRIVATE_5GHZ_INTERFACE_NAME_BY_SSID = "wifi.interface.name.private.5ghz.ssid.";
    public static final String PROP_KEY_WIFI_PRIVATE_2GHZ_INTERFACE_NAME_BY_SSID = "wifi.interface.name.private.2ghz.ssid.";
    public static final String PROP_KEY_WIFI_PUBLIC_5GHZ_INTERFACE_NAME_BY_SSID = "wifi.interface.name.public.5ghz.ssid.";
    public static final String PROP_KEY_WIFI_PUBLIC_2GHZ_INTERFACE_NAME_BY_SSID = "wifi.interface.name.public.2ghz.ssid.";

    public static final String PROP_KEY_WIFI_PRIVATE_5GHZ_INTERFACE_NAME_BY_BSSID = "wifi.interface.name.private.5ghz.bssid.";
    public static final String PROP_KEY_WIFI_PRIVATE_2GHZ_INTERFACE_NAME_BY_BSSID = "wifi.interface.name.private.2ghz.bssid.";
    public static final String PROP_KEY_WIFI_PUBLIC_5GHZ_INTERFACE_NAME_BY_BSSID = "wifi.interface.name.public.5ghz.bssid.";
    public static final String PROP_KEY_WIFI_PUBLIC_2GHZ_INTERFACE_NAME_BY_BSSID = "wifi.interface.name.public.2ghz.bssid.";

    public static final String PROP_KEY_WIFI_PRIVATE_5GHZ_INTERFACE_NAME_BY_MAC = "wifi.interface.name.private.5ghz.mac.";
    public static final String PROP_KEY_WIFI_PRIVATE_2GHZ_INTERFACE_NAME_BY_MAC = "wifi.interface.name.private.2ghz.mac.";
    public static final String PROP_KEY_WIFI_PUBLIC_5GHZ_INTERFACE_NAME_BY_MAC = "wifi.interface.name.public.5ghz.mac.";
    public static final String PROP_KEY_WIFI_PUBLIC_2GHZ_INTERFACE_NAME_BY_MAC = "wifi.interface.name.public.2ghz.mac.";
    public static final String PROP_KEY_USER_SBIN_PATH = "path.user.sbin.";

    /** Constant for no such device */
    public static final String NOT_ASSOCIATED = "Not-Associated";

    /** Constant for Invalid Mac Address */
    public static final String INVALID_MAC_ADDRESS = "00:00:00:00:00:00";

    /** Constant for Out Of service */
    public static final String OUTOFSERVICE = "OutOfService";

    /** Command to grep MAC ADDRESS from Atom device */
    public static final String CMD_TO_GREP_MAC_ADDRESS_ATOM = " Access Point:\\s+(.*)\\s+Bit";

    /** Command to grep SSID from the Atom device */
    public static final String CMD_TO_GREP_SSID_ATOM = "[(ESSID|SSID)]:(.*)\\s+Mode";

    /** Command to grep SSID from the Fibre device */
    public static final String CMD_TO_GREP_SSID_FIBER = "SSID:\\s+(.*)\\s+Mode";

    /** Command to grep MAC ADDRESS from the Fibre device */
    public static final String CMD_TO_GREP_MAC_ADDRESS_FIBER = "BSSID:\\s+(.*)\\s+Capability";

    /** Regex pattern to validate the Hexa String */
    public static final String HEXA_STRING_REGEX = "[[A-Z0-9a-z]{2}\\s].*";

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

    /**
     * Constant to hold Not applicable message since the system is ready and caching is completed
     */
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

    /** Possible channel list of Radio 1 */
    public static final String PROP_KEY_POSSIBLE_CHANNELLIST_RADIO_ONE = "possible.channellist.radio1";

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

    /**
     * String which will be replaced by actual enable/disable value in payload data
     */
    public static final String TELEMETRY_ENABLE_VAUE = "<TELEMETRY_ENABLE>";

    /**
     * String which will be replaced by actual enable/disable value in payload data
     */
    public static final String TELEMETRY_VERSION_VALUE = "<TELEMETRY_VERSION>";

    /**
     * String which will be replaced by actual enable/disable value in payload data
     */
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

    /**
     * Command to get report sent success message from /rdklogs/logs/telemetry2_0.txt.0
     */
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

    /**
     * Constant to hold command to check INFO/ERROR/WARN messages under /rdklogs/logs/telemetry2_0.txt.0
     */
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

    /** STB Property key to store TELEMETRY VERSION When Enabled */
    public static final String PROP_KEY_T2_ENABLED_VERSION = "current.t2enabled.version";

    /** STB Property key to store TELEMETRY VERSION */
    public static final String PROP_KEY_T2_VERSION = "t2.version";

    /** String variable to hold the DEFAULT constant value */
    public static final String DEFAULT = "DEFAULT";

    /** Command to give the version of OPenSSl used */
    public static final String COMMAND_FOR_OPENSSL_VERSION = "openssl version";

    /** Command to get the libssl.so file present in the build */
    public static final String COMMAND_TO_FETCH_LIBSSL_FILE = "ls /usr/lib/ | grep -i libssl.so";

    /** prefix Command to read OpenSSL version in ssl library */
    public static final String PREFIX_COMMAND_TO_READ_OPENSSL_OF_SSL_LIBRARY = "strings";

    /** postfix command to read OpenSSL version in ssl library */
    public static final String POSTFIX_COMMAND_TO_READ_OPENSSL_OF_SSL_LIBRARY = "| grep \"^OpenSSL.*[0-9][0-9][0-9]\"";

    /** Command to get the libcrypto.so file present in the build */
    public static final String COMMAND_TO_FETCH_LIBCRYPTO_FILE = "ls /usr/lib/ | grep -i libcrypto.so";

    /** Command to get libssl used by all running processes */
    public static final String COMMAND_TO_GET_LIBSSL_USED_IN_ALL_PROCESSES = "cat /proc/*/maps | grep -i libssl.so |awk ' {print $NF}' | uniq ";

    /**
     * pattern to get libssl location from COMMAND_TO_GET__LIBSSL_USED_IN_ALL_PROCESSES response
     */
    public static final String PATTERN_TO_GET_LIBSSL = "(\\S+libssl\\S+)";

    /** Command to get libcrypto used by all running processes */
    public static final String COMMAND_TO_GET_LIBCRYPTO_USED_IN_ALL_PROCESSES = "cat /proc/*/maps | grep -i libcrypto.so |awk ' {print $NF}' | uniq ";

    /**
     * pattern to get libssl location from COMMAND_TO_GET__LIBCRYPTO_USED_IN_ALL_PROCESSES response
     */
    public static final String PATTERN_TO_GET_LIBCRYPTO = "(\\S+libcrypto\\S+)";

    /** String for AdTrackerBlockingRFCEnable */
    public static final String ADTRACKER_BLOCKING_RFC_ENABLE = "AdTrackerBlockingRFCEnable";

    /*** File path for ADVSEClog.txt.0 ***/
    public static final String FILE_ADVSEC_0 = "/rdklogs/logs/ADVSEClog.txt.0";

    /** String variable to store emta detection * */
    public static final String STRING_EMTA_DETECTION = "emta detection";
    /** constant to pass as key value to validate emta detection */
    public static final String WAREHOUSE_GENERAL_EMTA_DETECTION = "WAREHOUSE_GENERAL_EMTA_DETECTION";

    /**
     * constant to pass as key value to validate wireless ssid enable status for public Home 2.4GHz
     */
    public static final String WAREHOUSE_WIRELESS_SSID_ENABLE_XHS_2_4 = "WAREHOUSE_WIRELESS_SSID_ENABLE1";

    /**
     * constant to pass as key value to validate wireless ssid1 enable status for Lost and Found 2.4GHz
     */
    public static final String WAREHOUSE_WIRELESS_SSID1_ENABLE_LNF_2_4 = "WAREHOUSE_WIRELESS_SSID_ENABLE2";

    /**
     * constant to pass as key value to validate wireless ssid enable status for Lost and Found 2.4GHz
     */
    public static final String WAREHOUSE_WIRELESS_SSID2_ENABLE_LNF_2_4 = "WAREHOUSE_WIRELESS_SSID_ENABLE3";

    /**
     * constant to pass as key value to validate wireless ssid enable status for Lost and Found 5GHz
     */
    public static final String WAREHOUSE_WIRELESS_SSID_ENABLE_LNF_5G = "WAREHOUSE_WIRELESS_SSID_ENABLE1_5G";

    /** webpa Parameter for 2.4GHz guest SSID name */
    public static final String WAREHOUSE_PARAM_DEVICE_WIFI_SSID_10002_SSID = "WAREHOUSE_WIRELESS_SSID_ENABLE1";

    /** Webpa param for 5GHz guest SSID name */
    public static final String WAREHOUSE_PARAM_DEVICE_WIFI_SSID_10102_SSID = "WAREHOUSE_WIRELESS_SSID_ENABLE_5G";

    /**
     * constant to pass as key value to validate wireless ssid password for private 2.4GHz
     */
    public static final String WAREHOUSE_WIRELESS_SSID_PASSWORD_PRIVATE_2G = "WAREHOUSE_WIRELESS_SSID_PASSWORD_2G";

    /**
     * constant to pass as key value to validate wireless ssid password for private 5GHz
     */
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

    /** command to store the ipv6 Type */
    public static final String STRING_IPV6_TYPE = "ipv6 type";

    /** String timeout */
    public static final String STRING_TIMEOUT = "timeout";

    /** command to store the upgrade server */
    public static final String STRING_UPGRADE_SERVER = "upgrade server";

    /** Constant to store String server.tag */
    public static final String CONSTANT_SERVER_DOT_TAG = "server.tag";

    /** pattern to search server tag */
    public static final String PATTERN_SERVER_TAG = "server.tag\\s+=\\W+([\\w+\\s+]+)\\W+";

    /** Constant to store file path lighttpd.conf */
    public static final String CONSTANT_LIGHTTPD_CONF = "/etc/lighttpd.conf";

    /** Constant to store string expose_php */
    public static final String CONSTANT_EXPOSE_PHP = "expose_php";

    /** Constant to store file path php.ini */
    public static final String CONSTANT_PHP_INI = "/etc/php.ini";

    /** Constant to check expose php value */
    public static final String CONSTANT_EXPOSE_PHP_VALUE = "expose_php = Off";

    /** String variable for PHP Version */
    public static final String PHP_VERSION = "php.version";

    /** Dot Delimiter **/
    public static final String DELIMITER_DOT = "\\.";

    /** Constant for number -1 */
    public static final int CONSTANT_NEGATIVE_1 = -1;

    /** Constant to hold default channel selection 2.4 GHz */
    public static final String DEFAULT_CHANNEL_SELECTION_2GHZ = "DEFAULT_CHANNEL_SELECTION_2GHZ";

    /** Constant to hold default channel selection 5 GHz */
    public static final String DEFAULT_CHANNEL_SELECTION_5GHZ = "DEFAULT_CHANNEL_SELECTION_5GHZ";

    /** Constant to hold default channel no 2.4 GHz */
    public static final String DEFAULT_CHANNEL_NO_2GHZ = "DEFAULT_CHANNEL_NO_2GHZ";

    /** Constant to hold default channel no 5 GHz */
    public static final String DEFAULT_CHANNEL_NO_5GHZ = "DEFAULT_CHANNEL_NO_5GHZ";

    /** String constant to hold FACTORY_RESET */
    public static final String FACTORY_RESET = "factory reset";

    /** constant to hold 2g wireless channel */
    public static final String WAREHOUSE_WIFI_2GHZ_CHANNEL = "wireless channel 2g";

    /** constant to hold 5g wireless channel */
    public static final String WAREHOUSE_WIFI_5GHZ_CHANNEL = "wireless channel 5g";

    /** Constant for Character comma */
    public static final String CHARACTER_COMMA = ",";

    /** String to store space character */
    public static final String ESCAPE_SEQUENCE_SPACE = "\\s";

    /** Constant to hold the date command */
    public static final String STRING_CMD_DATE = "date";

    /** String regex to get the date value */
    public static final String STRING_REGEX_DATE = "((Mon|Tue|Wed|Thu|Fri|Sat|Sun)\\s(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\s+\\d+\\s\\d{2}:\\d{2}:\\d{2}\\sUTC\\s\\d{4})";

    /** Command to get the used space for filename in the device */
    public static final String COMMAND_TO_FETCH_USED_SPACE_FOR_FILENAME = "df | grep FILENAME | awk 'NR==1{print $5}'";

    /** Constant for the filename nvram */
    public static final String NVRAM_FILE_NAME = "nvram";

    /** Constant for Replacing the filename */
    public static final String FILE_NAME = "FILENAME";

    /** Constant for the filename rdklogs */
    public static final String RDKLOGS_FILE_NAME = "rdklogs";

    /** Constant to hold Integer value 90 */
    public static final int CONSTANT_90 = 90;

    /** String regex to get the value before % */
    public static final String STRING_REGEX_TO_GET_PERCENTAGE_VALUE = "(\\w+)%";

    /** Command to get the Highest % of CPU consumption in the device */
    public static final String COMMAND_TO_FETCH_CPU_PERCENTAGE = "top -n1 -- o %cpu | awk 'NR==6{print $7}'";

    /** String value for 50 */
    public static final String STRING_VALUE_FIFTY = "50";

    /** Pattern to retrieve current lighttpd version */
    public static final String PATTERN_TO_RETRIEVE_LIGHTTPD_MINOR_VERSION = "\\d+\\.\\d+\\.(\\d+)";

    /** Pattern to retrieve current lighttpd version */
    public static final String PATTERN_TO_RETRIEVE_LIGHTTPD_MAJOR_VERSION = "((\\d+\\.){2}).\\d+";

    /** String variable to store enable */
    public static final String STRING_ENABLE = "Enable";

    /** Test SSID Name 2.4 GHz */
    public static final String TEST_SSID_2_4 = "test-ssid-2.4";

    /** String value Zero */
    public static final String STRING_VALUE_ZERO = "0";

    /** String Variable to hold the Low constant value */
    public static final String STRING_CONSTANT_LOW = "Low";

    /**
     * The constant holding command for seeing the process status of CCSPXDNSSSP process.
     */
    public static final String STRING_CCSPXDNSSSP_PROCESS = "CcspXdnsSsp -subsys eRT";

    /**
     * The constant holding command for seeing the process status of CCSPHOMESECURITY process.
     */
    public static final String STRING_CCSPHOMESECURITY_PROCESS = "CcspHomeSecurity";//

    /**
     * The constant holding command for seeing the process status of SNMP process.
     */
    public static final String STRING_SNMP_D_PROCESS = "snmpd";
    
    /**
     * The constant holding command for seeing the process status of SNMP Subagent.
     */
    public static final String STRING_SNMP_SUBAGENT_PROCESS = "snmp_subagent";
    
    /** String for PRIVACY_PROTECTION_DEACTIVATED */
    public static final String STRING_PRIVACY_PROTECTION_DEACTIVATED = "PRIVACY_PROTECTION_DEACTIVATED";

    /** String for PRIVACY_PROTECTION_ACTIVATED */
    public static final String STRING_PRIVACY_PROTECTION_ACTIVATED = "PRIVACY_PROTECTION_ACTIVATED";

    /** String constant to hold yocto version dunfell */
    public static final String YOCTO_VERSION_DUNFELL = "dunfell";

    /** Pattern to get the tocto version */
    public static final String PATTERN_TO_GET_YOCTO_VERSION = "YOCTO_VERSION=(\\S+)";

    /** Command to get latest dnsmasq binary file name used by all process */
    public static final String CMD_TO_GET_DNSMASQ_BINARY_USED_BY_ALL_PROCESS = "cat /proc/*/maps | grep -i dnsmasq |  awk ' {print $NF}' | uniq";

    /** Command to get latest dnsmasq version from binary */
    public static final String CMD_TO_PARSE_DNSMASQ_VERSION_FROM_BINARY = "strings  <DNSMASQ-BINARY>  | grep -i dnsmasq-";

    /**
     * pattern to get dnsmasq location from CMD_TO_GET_DNSMASQ_BINARY_USED_BY_ALL_PROCESS response
     */
    public static final String PATTERN_TO_GET_DNSMASQ_FILE_NAME = "(\\S*dnsmasq\\S*)";

    /** command to query IPV4 enable external server */
    public static final String COMMAND_TO_QUERY_IPV4_ENABLED_EXTERNAL_SERVER = "curl -v -4 --interface erouter0 https://www.google.com/";

    /** HTTP success response 302 */
    public static final String HTTP_RESPONSE_302 = "HTTP/1.1 302";

    /** HTTP response connected to www.google.com */
    public static final String TEXT_CONNECTED_TO_GOOGLE = "Connected to www.google.com";

    /** command to query IPV6 enable external server */
    public static final String COMMAND_TO_QUERY_IPV6_ENABLED_EXTERNAL_SERVER = "curl -v -6 --interface erouter0 https://www.google.com/";

    /**
     * Pattern Matcher to retrieve the timestamp in yymmdd-HH:mm:ss format from log message
     */
    public static final String PATTERN_MATCHER_TIMESTAMP_FORMAT_YYMMDD = "(\\d+-\\d+:\\d+:\\d+)\\.\\d+";

    /** String for protocol http */
    public static final String STRING_PROTOCOL_HTTP = "http";

    /** String variable to store success status for config params tests */
    public static final String PATTERN_GET_FAILURE_STEPS_FOR_CONFIGPARAM_TESTS = "Tests failed:\\s(\\d+)";

    public static final String STRING_TESTFILE = "testfile";

    /** Path for secure folder */
    public static final String FILE_PATH_SECURE_FOLDER = "/opt/secure/";

    /** String constant 15 */
    public static final String STRING_CONSTANT_15 = "15";

    /** String constant 5 */
    public static final String STRING_CONSTANT_5 = "5";

    /** String constant for Negative Value -1 */
    public static final String STRING_CONSTANT_NEGATIVE_1 = "-1";

    /** String to check SNMPV3 support from /opt/secure/data/syscfg.db */
    public static final String STRING_SNMP_V3SUPPORT = "V3Support";

    /** String to check firewall_level from /opt/secure/data/syscfg.db */
    public static final String STRING_FIREWALL_LEVEL_V6 = "firewall_levelv6";

    /** String to check UPdateNvram from /opt/secure/data/syscfg.db */
    public static final String STRING_UPDATE_NVRAM = "UPdateNvram";

    /** String value for | symbol with spaces. */
    public static final String SYMBOL_PIPE_WITH_SPACES = " | ";

    /** List of sensitive keyword removed from iptable */
    public static final List<String> SENSITIVE_KEYWORDS_REMOVED_FROM_IPTABLE = Arrays.asList("configparam", "whitelist",
	    "firewall", "encrypt", "decrypt", "secret", "private", "shared", "PSK", "password", "credential", "key",
	    "dropbear", "passphrase", "obfuscate", "PROPRIETARY");

    /** String to hold root */
    public static final String STRING_ROOT = "root";

    /** String to hold non-root */
    public static final String STRING_NON_ROOT = "non-root";

    /** String 2.4Ghz private wifi default ssid for BridgeMode test cases */
    public static final String SSID_FOR_2GHZ_PRIVATE_WIFI = "PrivateWifi_2.4";

    /** String 2.4Ghz Private ssid name for bridge mode test cases */
    public static final String PRIVATEWIFI_NAME_FOR_2GHZ_BAND = "Hello_2.4";

    /** Pattern to get backoffretrytime from log message */
    public static final String PATTERN_TO_GET_BACKOFFRETRYTIME = "backoffRetryTime (\\d+)";

    /** Pattern to get max delay value from log message */
    public static final String PATTERN_TO_GET_MAX_DELAY = "max delay (\\d+)";

    /** Integer list of max intervals for parodus delay as per jitter */
    public static final List<Integer> PARODUS_JITTER_INTERVALS = new ArrayList<Integer>() {
	{
	    add(3);
	    add(7);
	    add(15);
	    add(31);
	    add(63);
	    add(127);
	    add(255);
	}
    };

    /** Ethernet Connection */
    public static final String CONNECTION_TYPE_ETHERNET = "Ethernet";

    /** Constant to hold google.com */
    public static final String NSLOOKUP_FOR_GOOGLE = "www.google.com";

    public static final String FACEBOOK_IPV6_ADDRESS = "2a03:2880:f003:0c07:face:b00c:0000:0002";

    public static final String NSLOOKUP_NO_RESPONSE = "No response from server";
    public static final String NSLOOKUP_TIMEOUT_RESPONSE = "DNS request timed out";

    public static final String NSLOOKUP_CACHE_REDIRECT_IP = "10.0.0.1";

    /** constant to hold int 900 */
    public static final String CONSTANT_NINE_HUNDRED = "900";

    /** Constant to hold int value for number 70 */
    public static final int CONSTANT_70 = 70;

    /** String to store reboot reason syseventd crash */
    public static final String REBOOT_REASON_SYSEVENTD_CRASH = "Syseventd_crash";

    /** String to store value of ten thousand eight hundred - 3 hours in seconds */
    public static final String STRING_VALUE_10800 = "10800";

    /** Process name syseventd */
    public static final String PROCESS_NAME_SYSEVENTD = "syseventd";

    /** Constant to hold downstream channel frequency value */
    public static final String DOWNSTREAM_CHANNEL_FREQUENCY_VALUE = "Device\\.X_CISCO_COM_CableModem\\.DownstreamChannel\\.(\\d+)\\.Frequency";

    /** Pattern to hold downstream channel frequency range using webpa */
    public static final String PATTERN_FOR_DWNSTRM_CHNL_FREQ_WEBPA_RESPONSE_MHZ = "MHz";

    /** Pattern to hold downstream channel frequency response using webpa */
    public static final String PATTERN_FOR_DWNSTRM_CHNL_FREQ_WEBPA_RESPONSE = "(\\d+)(.*) MHz";

    /** Constant to hold the string value INT_RANGE */
    public static final String INT_RANGE = "INT_RANGE";

    /** Pattern to hold downstream channel frequency response using snmp */
    public static final String PATTERN_FOR_DWNSTRM_CHNL_FREQ_SNMP_RESPONSE = "\\s(\\d+)";

    /** Constant to hold string 1000000 */
    public static final String STRING_CONSTANT_1000000 = "1000000";

    /** Constant to hold the string value BIG_INT_RANGE_INC_ZERO */
    public static final String BIG_INT_RANGE_INC_ZERO = "BIG_INT_RANGE_INC_ZERO";

    /** constant integer value 23 */
    public static final int CONSTANT_23 = 23;

    /** int value 30 */
    public static final int INT_VALUE_THIRTY = 30;

    /** Constant to integer value 35 */
    public static final int CONSTANT_35 = 35;

    /** Constant to integer value 65 */
    public static final int CONSTANT_65 = 65;

    /** command to get logupload frequency on atom side */
    public static final String CMD_GET_LOG_UPLOAD_FREQUENCY_ATOM_CONSOLE = "cat /tmp/cron/root";

    /** pattern to get logupload frequency on atom side */
    public static final String PATTERN_GET_LOG_UPLOAD_FREQUENCY = "\\/(\\d+).*dca_utility.sh";

    /** String value 1440 */
    public static final String STRING_VALUE_1440 = "1440";

    /** String to store the Device Name Constant */
    public static final String STRING_DEVICE_NAME = "TEST";

    /** path for script to trigger firmware download for DSL devices **/
    public static final String TRIGGER_FIRMWARE_DOWNLOAD_SCRIPT_PATH_DSL = "/<path>/dsl_triggerFirmwareDownload.sh";

    /** NVRAM PATH **/
    public static final String NVRAM_PATH = "/nvram/";

    /** Property key for feature availability */
    public static final String PROP_KEY_JAIL_UI_FEATURE = "jail.ui.feature.build";

    /** Pattern to get the notification for firmware download in PAM log file */
    public static final String PATTERN_GET_XCONF_PAYLOAD = "Notification payload (.*)";

    /** String status */
    public static final String STATUS = "status";

    /** Constant to hold firmware download reboot priority */
    public static final String PRIORITY = "priority";

    /** Constant key start-time of firmware download */
    public static final String START_TIME = "start-time";

    /** Constant key current_fw_ver */
    public static final String CURRENT_FW_VER = "current_fw_ver";

    /** Constant for download_fw_ver */
    public static final String DOWNLOAD_FW_VER = "download_fw_ver";

    /** Constant firmware-download-started */
    public static final String FW_DWN_STARTED = "firmware-download-started";

    /** Constant for reboot priority deferred */
    public static final String FW_DWN_PRIORITY_DEFERRED = "deferred";

    /** String to store failure status value */
    public static final String STATUS_FAILURE = "failure";

    /** Constant to hold FEATURE_CPUPROCANALYZER_TIMETORUN_SECS value */
    public static final String FEATURE_CPUPROCANALYZER_TIMETORUN_SECS = "FEATURE.CPUPROCANALYZER.TIMETORUN.SECS = 600";

    /** Constant to hold FEATURE_CPUPROCANALYZER_SLEEP_SECS value */
    public static final String FEATURE_CPUPROCANALYZER_SLEEP_SECS = "FEATURE.CPUPROCANALYZER.SLEEP.SECS = 60";

    /** Constant to hold FEATURE_CPUPROCANALYZER_DYNAMIC value */
    public static final String FEATURE_CPUPROCANALYZER_DYNAMIC = "FEATURE.CPUPROCANALYZER.DYNAMIC = 1";

    /** Constant to hold UPLOAD_LOGS_VAL_DCM value */
    public static final String VALUE_FOR_UPLOAD_LOGS_VAL_DCM = "UPLOAD_LOGS_VAL_DCM";

    /** Pattern to check KB */
    public static final String VALUE_FOR_KB = "K";

    /** Pattern to check MB */
    public static final String VALUE_FOR_MB = "M";

    /** String containing url of rdkb crash failover upload server */
    public static final String RDKB_CRASH_FAILOVER_UPLOAD_URL = "";

    /** Log which indicates selfheal process is up and running */
    public static final String SELFHEAL_PROCESS_UP_LOG = "{self_heal_conne}";

    /** Expected log for Interface status selfheal from SelfHeal.txt.0 */
    public static final String INTERFACE_BRLAN_0_SELFHEAL_LOG_01 = "RM AdvSecurityDhcp process not running , restarting it";

    /** Expected log for Interface status selfheal from SelfHeal.txt.0 */
    public static final String INTERFACE_BRLAN_0_SELFHEAL_LOG_02 = "setting event to recreate vlan and brlan0 interface";

    /** Expected log for Interface status selfheal from SelfHeal.txt.0 */
    public static final String INTERFACE_BRLAN_0_SELFHEAL_LOG_03 = "setting event to recreate brlan0 interface";

    /** Command to get self heal logs for process crash */
    public static final String COMMAND_TO_GET_SELF_HEAL_LOGS_FOR_PROCESS_CRASH = "rm -rf /nvram/automation_sample.txt;tail -F /rdklogs/logs/SelfHeal.txt.0 > /nvram/automation_sample.txt";

    /** Command to get self heal logs for process crash */
    public static final String COMMAND_TO_GET_SELF_HEAL_AGGRESSIVE_LOGS_FOR_PROCESS_CRASH = "rm -rf /nvram/automation_sample.txt;tail -F /rdklogs/logs/SelfHealAggressive.txt > /nvram/automation_sample.txt";

    /** Command to remove sample text file */
    public static final String REMOVE_SAMPLE_TEXT_FILE = "rm -rf /nvram/automation_sample.txt";

    /** Command to get process crash logs */
    public static final String COMMAND_TO_GET_PROCESS_CRASH_LOGS = "cat /nvram/automation_sample.txt";

    /** String variable to store constant ipv6 */
    public static final String String_CONSTANT_IPV6 = "ipv6";

    /** String variable to store constant ipv4 */
    public static final String String_CONSTANT_IPV4 = "ipv4";

    /** Log constant for sbin folder not found */
    public static final String SBIN_FOLDER_NOT_FOUND_ERROR = "/sbin/ip: No such file or directory";

    /** Command Constants for /sbin/ */
    public static final String SBIN_FOLDER_CONSTANT = "/sbin/";

    /** Constant to hold Up state keyword for Interface brlan0 */
    public static final String STATE_UP_KEYWORD_INTERFACE_BRLAN_0 = "state UP";

    /** Constant to hold Down state keyword for Interface brlan0 */
    public static final String STATE_DOWN_KEYWORD_INTERFACE_BRLAN_0 = "state DOWN";

    /** pattern to get the process id for dnsmasq parent */
    public static final String PATTERN_PROCESS_DNSMASQ = "(\\d+) +\\w+ +.* dnsmasq";

    /** invalid dns server */
    public static final String INVALID_DNS_SERVER = "2001:558:feed::10";
    /** String value 10 */
    public static final String STRING_10 = "10";

    /** dns server 2 */
    public static final String DNS_SERVER_2 = "2001:558:feed::2";

    /** String variable to store webpa error message */
    public static final String WEBPA_ERROR_UNSUPPORTED_NAME_SPACE = "Error unsupported namespace";

    /** Command to get the host0 configurations details.* */
    public static final String CMD_IFCONFIG_HOST0 = "/sbin/ifconfig host0";

    /** String value to hold default delay */
    public static final String DEFAULT_DELAY = "delay=0";

    /** String value Router */
    public static final String STRING_ROUTER = "Router";

    /** String value to source */
    public static final String SOURCE = "source=";

    /** String value to store CSR-reboot */
    public static final String CSR_REBOOT_REASON = "CSR-reboot";

    /** String value to hold 10 second delay */
    public static final String DELAY = "delay=10";

    /** String value to store wifi */
    public static final String WIFI_WEBPA_REBOOT = "wifi";

    /** Constant to hold udhcpc error message */
    public static final String UDHCPC_ERROR_MESSAGE = "\"/etc/udhcpc.script: line\"";

    /** regex to retrieve ipconfig ipv6 address */
    public static final String PATTERN_TO_RETRIVE_IPV6_ADDRESS_FROM_IPCONFIG = ".*IPv6 Address. . . . . . . . . . . :\\s+(([0-9a-f]{1,4}:){7}([0-9a-f]{1,4})).*";

    /** Constant to hold pattern matcher for downstream lock message */
    public static final String PATTERN_MATCH_DOWNSTREAM_LOCK = "\\d+-\\d+:\\d+:\\d+.\\d+.*Downstream\\sis\\sLocked";

    /** Constant to hold pattern matcher for upstream lock message */
    public static final String PATTERN_MATCH_UPSTREAM_LOCK = "\\d+-\\d+:\\d+:\\d+.\\d+.*Upstream\\sis\\sLocked";

    /** Constant to hold the file name */
    public static final String CM_LOG_FILE_NAME = "/rdklogs/logs/CMlog.txt.0";

    /** Invalid special character constant for ResourceUsageComputeWindow */
    public static final String INVALID_SPEC_CHAR_CONSTANT_RESOURCE_USAGE_COMPUTE_WINDOW = "$";

    /** Invalid constant for ResourceUsageComputeWindow */
    public static final String INVALID_CONSTANT_RESOURCE_USAGE_COMPUTE_WINDOW = "16";

    /** Pattern to check the string in return value of dmcli */
    public static final String PATTERN_DMCLIPARAMETER_NOT_FOUND_DBUS_MODE = "Execution fail(error code:CCSP_ERR_NOT_CONNECT(190)).";

    /** Pattern to check the string in return value of dmcli */
    public static final String PATTERN_DMCLIPARAMETER_NOT_FOUND = "Can't find destination component";

    /** Constant to hold Reboot Reason as 'reboot_cmd' */
    public static final String REBOOT_REASON_REBOOT_CMD = "reboot_cmd";

    /** Log for reboot reason on kernel-panic */
    public static final String RDKB_REBOOT_REASON_KERNEL_PANIC = "kernel-panic";

    /** String value for 12Mbps WiFi beacon rate */
    public static final String TEXT_TWELEVE_MBPS = "12Mbps";

    /** Constant to integer value 22 */
    public static final int CONSTANT_22 = 22;

    /** pattern to get the process id for sleep 290 */
    public static final String PATTERN_TO_GET_PID_SLEEP_290 = "(\\d+) +root +\\d+ +S +N? +sleep +290";

    public static final String TR181_PARAM_CM_IPV6 = "Device.DeviceInfo.X_COMCAST-COM_CM_IP";

    /** Property key for public WiFi Primary End point IP */
    public static final String PROP_KEY_PRIMARY_ENDPOINT_IP = "public.gre.tunnel.primary.remote.endpoint.ip";

    /** Property key for public WiFi Secondary End point IP */
    public static final String PROP_KEY_SECONDARY_ENDPOINT_IP = "public.gre.tunnel.secondary.remote.endpoint.ip";

    /** int value to store five hundred and twenty */
    public static final String STRING_CONSTANT_520 = "520";

    /** command to list all mounted file systems */
    public static final String CMD_TO_LIST_ALL_MOUNTED_FILES = "mount";

    /** Folder name */
    public static final String SECURE_FOLDER = "secure";

    /** String value for opt */
    public static final String TEXT_OPT = "opt";

    /**
     * String to store expected response when /opt/secure is mounted with encryption
     */
    public static final String OPT_SECURE_SAFE_MOUNT_RESPONSE = "/opt/secure type ecryptfs";

    /** String to be written to temporary file */
    public static final String TEXT_TO_TEMP_FILE = "This is for encryption check";

    /** Constants to hold close angle bracket character */
    public static final String CHR_CLOSING_ANGLE_BRACKET = ">";

    /** temporary file name */
    public static final String FILE_CREATED_TO_TEST_SECURITY = "sample.txt";

    /** Service name securemount */
    public static final String SECUREMOUNT_SERVICE = "securemount";

    /** Status 'Active' for any service */
    public static final String STATUS_ACTIVE = "active";

    /** command to unmount specified file system */
    public static final String CMD_TO_UNMOUNT_FILE_SYSTEM = "umount -i";

    /** String to store the beginning name of encrypted files */
    public static final String ENCRYPTED_FILE_STRING = "ECRYPTFS_FNEK_ENCRYPTED";

    /** Linux Command 'cat' to display the content. */
    public static final String CMD_LINUX_CAT = "cat";

    /** Constant for number 18 */
    public static final int CONSTANT_18 = 18;

    /** String to invalid argument */
    public static final String TEXT_INVALID_ARGUMENT = "Invalid argument";

    /** Constant to hold default Subnet mask value */
    public static final String DEFAULT_IPV4_SUBNET_MASK = "DEFAULT_IPV4_SUBNET_MASK";

    /** Asterisk. */
    public static final String ASTERISK = "*";

    /** Service Status - Active **/
    public static final String SERVICE_STATUS_ACTIVE = "active";

    /** Service Status - Inactive **/
    public static final String SERVICE_STATUS_INACTIVE = "inactive";

    /** Command get status of a service using systemctl */
    public static final String CMD_SYSTEMCTL_STATUS = "systemctl status ";

    /** Command get status of a service using systemctl */
    public static final String PROCESS_STATUS = "Active: (\\S+)";

    /** Command to restart any service */
    public static final String CMD_RESTART_SERVICE = "systemctl restart ";

    /** Failure response obtained when some service restart fails */
    public static final String SERVICE_FAILED_MESSAGE = "Failed to restart";

    /** Temporary operational Tx rate for mode g,n */
    public static final String TEMP_OPRATIONAL_TX_RATE_G_N = "6,9,12,18";

    /** Unsupported operational Tx rate for 2.4GHz */
    public static final String UNSUPPORTED_OPERATION_TX_RATE_2GHZ = "3,4,5,15";

    /** Non basic operational Tx rate for 2.4GHz */
    public static final String NON_BASIC_OPERATIONAL_TX_RATE_2GHZ = "9,18,36";

    /** Temporary operational Tx rate for 2.4GHz */
    public static final String TEMP_OPEATIONAL_TX_RATE_2GHZ = "1,2,6,9";

    /** Basic Tx Rate for 5GHz */
    public static final String BASIC_TX_RATE_5GHZ = "6,12,24";

    /** String value for Operating mode n/ac */
    public static final String OPERATING_MODE_NAC = "n,ac";

    /** Operating Standards for 5GHz a/n/ac mode. */
    public static final String OPERATING_STANDARDS_N_AC = "n,ac";

    /** supported data Tx Rate for 5GHz */
    public static final String SUPPORTED_DATA_TX_RATE_5GHZ = "6,9,12,18,24,36,48,54";

    /** Operational transmission Tx Rate for 5GHz */
    public static final String OPERATIONAL_TX_RATE_5GHZ = "6,9,12,18,24,36,48,54";

    /** Operating Standards for 5GHz a/n/ac mode. */
    public static final String OPERATING_STANDARDS_AC = "ac";

    /** Default Operational Tx Rate for 2.4GHz b/g/n mode. */
    public static final String OPERATION_TX_RATE_B_G_N = "1,2,5.5,11,6,9,12,18,24,36,48,54";

    /** Constant for b/g/n Operating Standard */
    public static final String WIFI_OPERATING_STANDARD_G_N = "g,n";

    /** Default Basic Tx Rate for 2.4GHz g/n mode. */
    public static final String BASIC_TX_RATE_G_N = "1,2,5.5,11,6,12,24";

    /** Constant to hold private wifi 5GHZ index */
    public static final String WIFI_5_GHZ_INDEX = "10101";

    /** Operating Standards for 5GHz a/n/ac mode. */
    public static final String OPERATING_STANDARDS_N = "n";

    /** Default Operational Tx Rate for 2.4GHz g/n mode. */
    public static final String OPERATION_TX_RATE_B_G_N_DSL_DEVICE = "1,2,5.5,6,9,11,12,18,24,36,48,54";

    /** dnsmasq service name */
    public static final String DNSMASQ_PROCESS_NAME = "dnsmasq";

    /** String value start */
    public static final String STRING_START = "start";

    /** String value stop */
    public static final String STRING_STOP = "stop";

    /** Pattern to get the process id from ps command */
    public static final String PATTERN_GET_PID_FROM_PS = "(\\d+) ";

    /** Constant to hold the down status */
    public static final String STRING_DOWN = "down";

    /** Pattern to get the dibbler client process id */
    public static final String PATTERN_TO_GET_PID_DIBBLER_CLIENT = "Dibbler client\\: RUNNING\\, pid=(\\d+)";

    /** Pattern to get the first 3 octat from IPv4 address */
    public static final String PATTERN_TO_GET_THREE_OCTAT_IPV4 = "(\\d+\\.\\d+\\.\\d+)";

    /** Pattern to get the interface name */
    public static final String PATTERN_TO_GET_INTERFACE_NAME = "([\\w\\.]+) ";

    /** Constant for netcat version */
    public static final String NETCAT_VERSION = "BusyBox v";

    /** Constant for netcat version */
    public static final String NETCAT_GNU_VERSION = "netcat (The GNU Netcat) 0.7.1";

    /** Constant for invalid option --l */
    public static final String NC_INVALID_OPTION_L = "nc: invalid option -- 'l'";

    /** Constant for invalid option --e */
    public static final String NC_INVALID_OPTION_E = "nc: invalid option -- 'e'";

    /** Constant for Bad Host */
    public static final String NC_ERROR_BAD_HOST = "Bad Host ";

    /** Constant for nc Connection error */
    public static final String NC_CONNECTION_ERROR_LAN_IP = NC_ERROR_BAD_HOST
	    + AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.PROP_KEY_XB_LAN_IP);

    /** Constant for nc Connection error */
    public static final String NC_CONNECTION_ERROR_IP_192 = NC_ERROR_BAD_HOST + "";

    /** Constant for nc Connection error */
    public static final String NC_CONNECTION_ERROR_IP_169 = NC_ERROR_BAD_HOST + "";

    /** Constant for nc Connection error */
    public static final String NC_CONNECTION_ERROR_IP_147 = NC_ERROR_BAD_HOST + "";

    /** Constant for nc Connection error */
    public static final String NC_CONNECTION_ERROR_IP_10 = NC_ERROR_BAD_HOST + "";

    /** Constant for Netcat invoked */
    public static final String NC_NETCAT_INVOKED = "Netcat invoked with [tcp]mode Host";

    /**
     * The constant for fibre Devices not applicable for IPV6 related steps
     */
    public static final String FIBRE_NOT_APPLICABLE_IPV6 = "Not Applicable For fibre Box as Gateway Device";

    /** Three minutes in milli seconds. */
    public static final long FOUR_MINUTES = 4 * 60 * 1000;

    /** Constant to hold wifi log error message */
    public static final String CONSTANT_WIFI_ERROR_MESSAGE = "Failed to get parameter value of";

    /** String for NAME */
    public static String STRING_NAME = "name";

    /** Constant for holding String 'Not Applicable' */
    public static final String STRING_NOT_APPLICABLE = "NotApplicable";

    /** Temporary file name to store initial ipv4 table rules */
    public static final String FILE_NAME_TO_STORE_INITIAL_IPV4_TABLE_RULES = "/tmp/ipv4tableinitial";

    /** Temporary file name to store initial ipv6 table rules */
    public static final String FILE_NAME_TO_STORE_INITIAL_IPV6_TABLE_RULES = "/tmp/ipv6tableinitial";

    /** command to save ipv4 table rules */
    public static final String COMMAND_TO_SAVE_IPV4_TABLE_RULES = "/usr/sbin/iptables-save";

    /** command to save ipv6 table rules */
    public static final String COMMAND_TO_SAVE_IPV6_TABLE_RULES = "/usr/sbin/ip6tables-save";

    /** Temporary file name to store final ipv4 table rules */
    public static final String FILE_NAME_TO_STORE_FINAL_IPV4_TABLE_RULES = "/tmp/ipv4tablefinal";

    /** Temporary file name to store final ipv6 table rules */
    public static final String FILE_NAME_TO_STORE_FINAL_IPV6_TABLE_RULES = "/tmp/ipv6tablefinal";

    /** String to store ping response in linux */
    public static final String STRING_LINUX_PING_RESPONSE = "0% packet loss";

    /** String command for ping to linux */
    public static final String STRING_PING_TO_LINUX = "ping -c 4 <IPADDRESS>";

    /** String command for ping to windows */
    public static final String STRING_PING_TO_WINDOWS = "ping <IPADDRESS>";

    /** String containing values of Device Operational Mode - Ethernet */
    public static final String STRING_DEVICE_OPERATIONAL_MODE_ETHERNET = "Ethernet";

    /** String containing values of Device Operational Mode - DOCSIS */
    public static final String STRING_DEVICE_OPERATIONAL_MODE_DOCSIS = "DOCSIS";

    /** The partial property key for Firmware downpoad script */
    public static final String PROP_KEY_FIRMWARE_DOWNLOAD_SCRIPT = "firmware.downlod.script.";

    /** Command to get peridic firmware check details from rfc_config data **/
    public static final String CMD_GET_PERIODIC_FIRMWARE_CHECK_VALUE_RFC_CONFIG_FILE = "cat /tmp/rfc_configdata.txt | grep -i \"periodic\"";

    /** pattern to get peridic firmware check details from rfc_config data **/
    public static final String PATTERN_GET_PERIODIC_FIRMWARE_CHECK_VALUE_RFC_CONFIG_FILE = "tr181.Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.PeriodicFWCheck.Enable\\W+(\\w+)";

    /** Key to get the public wifi value to be set */
    public static String KEY_FOR_PUBLIC_WIFI_WHITELISTING = "rdkb.whitelist.publicwifivalue";

    /** The property key for device status. */
    public static final String DEVICE_STATUS = "device.status.";

    /** Regex for getting the MaxResetCount */
    public static final String PATTERN_TO_GET_MAX_RESET_COUNT = "tr181.Device.SelfHeal.X_RDKCENTRAL-COM_MaxResetCount#~(\\w+)";

    /** String to check telemetry marker for memory usage from the log */
    public static final String STRING_MEM = "mem";

    /** String to check telemetry marker for cpu usage from the log */
    public static final String STRING_CPU = "cpu";

    /** String File path /nvram/DCMresponse.txt */
    public static final String FILE_NVRAM_DCMRESPONSE = "/nvram/DCMresponse.txt";

    /**
     * The constant holding command for seeing the process status of CCSP process
     */
    public static final String PS_COMMAND_FOR_CCSP_PROCESS = "ps | grep -i \"[Cc|S]sp\"";

    /**
     * The array list of Ccsp Components to test telemetry data for CPU and Memory usage
     */
    public static final List<String> LIST_OF_PROCESS_FOR_CPU_MEM = new ArrayList<String>() {
	{
	    add("CcspCMAgentSsp");
	    add("CcspPandMSsp");
	    add("CcspHomeSecurity");
	    add("CcspMoCA");
	    add("CcspTandDSsp");
	    add("CcspXdnsSsp");
	    add("CcspEthAgent");
	    add("CcspLMLite");
	    add("PsmSsp");
	}
    };

    /** utility script in cron job list for telemetry **/
    public static final String DCA_UTILITY_SH = "sh /lib/rdk/dca_utility.sh";

    /** Constant to hold command to get the static WAN IP from the device */
    public static final String COMMAND_TO_GET_STATIC_WAN_IP = "cat /etc/ripd.conf|grep network|tail -1|sed 's/.* //g'|sed 's/ *\\/.*//'";

    /** String regex to get ethernet connected client ipv4 from ifconfig */
    public static final String STRING_REGEX_TO_GET_ETHERNET_IPV4 = "inet[\\sa-zA-Z:|\\s]*(\\d+\\.\\d+\\.\\d+\\.\\d+).*cast.*[\\s+:](\\d+\\.\\d+\\.\\d+\\.\\d+)";

    /** String value IPAddress to get ip address from ipconfig */
    public static final String STRING_IP_4_ADDRESS = "IPv4 Address";

    /** String value PhysAddress to get mac from ipconfig */
    public static final String STRING_PHYSICAL_ADDRESS = "Physical Address";

    /**
     * String regex to get IP and MAC address from ipconfig for given interface name
     */
    public static final String STRING_REGEX_IP_MAC = "[.\\s]*:\\s*([\\d.-a-zA-Z]+)";

    /** String regex for new line */
    public static final String STRING_REGEX_PATTERN_NEW_LINE = "\\r\\n";

    /**
     * String regex to get raspian linux ethernet connected client ipv4 from ifconfig
     */
    public static final String STRING_REGEX_TO_GET_RASPIAN_ETHERNET_IPV4 = "inet[(\\s+.*:)|(\\s+)](\\d+.\\d+.\\d+.\\d+).*netmask.*";

    /** String for Change SSID NAME of 2.4GHZ Wifi */
    public static final String TWO_GHZ_SSID_NAME_CHANGE = "2.4GHZ_WiFi";

    /** String for Change SSID NAME of 5GHZ Wifi */
    public static final String FIVE_GHZ_SSID_NAME_CHANGE = "5GHZ_WiFi";

    /** String for Folder name */
    public static final String TEMPORARY_FOLDER = "test";

    /** Log for CDL started condition */
    public static final String LOG_CDL_STARTED = "### httpdownload started ###";

    /** Pattern for verifying the dev proto kernel scope link src . */
    public static final String PATTERN_DEV_PROTO_KERNEL_SCOPE_LINK = "dev\\s+\\S+\\s+proto\\s+kernel\\s+scope\\s+link\\s+ src\\s+(\\d+\\.\\d+\\.\\d+\\.\\d)";

    /** Pattern for verifying the dev scope link */
    public static final String PATTERN_DEV_SCOPE_LINK = "dev\\s+\\S+\\s+scope\\s+link";

    /** Pattern for verifying the dev default interface . */
    public static final String PATTERN_DEFAULT_DEV_IP = "default\\s+via\\s+(\\d+\\.\\d+\\.\\d+\\.\\d)\\s+dev";

    /** String constant for All */
    public static final String STRING_ALL = "All";

    /** String value for Operating mode n */
    public static final String OPERATING_MODE_N = "n";

    /** String value for 25 */
    public static final String STRING_VALUE_TWENTY_FIVE = "25";

    /** Constant for Extension channel value */
    public static final String EXTENSION_CHANNEL_VALUE = "AboveControlChannel";

    /** Constant to hold channel value as 11 */
    public static final String RADIO_CHANNEL_11 = "11";

    /** Constant to hold filename Harvesterlog.txt.0 */
    public static final String FILE_NAME_HARVESTER = "Harvesterlog.txt.0";

    /** The property key for device status. */
    public static final String DEVICE_NA = "device.na.";

    /** Constant to hold the following file names */
    public static final String FILE_NAME_LM = "LM.txt.0";

    /** String for PARODUSlog.txt.0 */
    public static final String STRING_PARODUS_LOG = "PARODUSlog.txt.0";

    /** Twenty five in milliseconds representation. */
    public static final long TWENTYFIVE_MINUTES_IN_MILLS = 25 * 60 * ONE_SECOND_IN_MILLIS;

    /**
     * String regex to get parodus boot time from command cat /tmp/parodusCmd.cmd
     */
    public static final String STRING_REGEX_PARODUS_BOOT_TIME = "--boot-time=(.*)\\s+--hw";

    /** Command to get epoch time in milli second. */
    public static final String CMD_EPOCH_TIME_IN_MILLI = "date +%s000";

    /** String backup */
    public static final String STRING_BACKUP = "BackUp";

    /**
     * String regex to check parodus --boot-time-retry-wait=(.*) from command cat /tmp/parodusCmd.cmd
     */
    public static final String STRING_REGEX_PARODUS_BOOT_TIME_RETRY = "--boot-time-retry-wait=(.*)";

    /** Constant to integer value 38 */
    public static final int CONSTANT_38 = 38;

    /** Constant to hold string 1000 */
    public static final String STRING_CONSTANT_1000 = "1000";

    /** String constant to hold value 10000 */
    public static final String STRING_10000 = "10000";

    /** String constant to hold value 15000 */
    public static final String STRING_15000 = "15000";

    /** Constant to hold Not applicable message for business class devices */
    public static final String NA_MSG_FOR_BUSINESS_CLASS_DEVICES = "Test Step Not Applicable for Business class devices";

    /** Constant for hold string 5000 */
    public static final String STRING_CONSTANT_5000 = "5000";

    /** Constant to hold Not applicable message for Commercial devices */
    public static final String NA_MSG_FOR_COMMERCIAL_DEVICES = "Test Step Not Applicable for Commercial class devices";

    /** Consatnt to hold Step ID values for wifi balster */
    public static final List<String> WIFI_BLASTER_STEPID_VALUES = new ArrayList<String>() {
	{
	    add("1");
	    add("9");
	    add("16");
	    add("31");
	}
    };

    /** constant integer value 45 */
    public static final int CONSTANT_45 = 45;

    /** string variable to store invalid url for dns ping test */
    public static final String STRING_DNS_PING_INVALID_URL = "www.oopsinvalid.com";

    /** command for enable connectivity test corrective action */
    public static final String CMD_ENABLED_CONNECTIVITY_TEST_CORRECTIVE_ACTION = "#syscfg set ConnTest_CorrectiveAction true;#syscfg commit";

    /** command to get DNS ping failure logs */
    public static final String CMD_GET_DNS_PING_FAILURE_LOGS = "cat /nvram/automation_sample.txt | grep -i \"RDKB_REBOOT\"";

    /** String variable to store dns ping failure self heal logs */
    public static final String STRING_DNS_PING_FAILURE_SELF_HEAL_LOG = "Reset router due to PING connectivity test failure";

    /** Default inet6 address pattern from log */
    public static final String INET_V6_ADDRESS_LINK_PATTERN = "inet6 addr:\\s*([\\w+:]*\\w+)\\/\\d+\\s*Scope:Link";

    /** String value 15 */
    public static final String STRING_VALUE_FIFTEEN = "15";

    /** String constant value for Operating bandwidth 20 MHz */
    public static final String OPERATING_BANDWIDTH_20_MMZ = "20MHz";

    /** String constant value for Operating bandwidth 40 MHz */
    public static final String OPERATING_BANDWIDTH_40_MMZ = "40MHz";

    /** String constant value for Operating bandwidth 80 MHz */
    public static final String OPERATING_BANDWIDTH_80_MMZ = "80MHz";

    /** WebPa Parameter for admin page url */
    public static final String WEBPA_PARAM_LAN_IP_ADDRESS = "Device.X_CISCO_COM_DeviceControl.LanManagementEntry.1.LanIPAddress";

    /** Constant to string for checking LAN IP address in system defaults file */
    public static final String STRING_LAN_IP_ADDR = "lan_ipaddr=";

    /**
     * Constant to string for checking DHCP start address in system defaults file
     */
    public static final String STRING_DHCP_START_ADDR = "dhcp_start=";

    /** Constant to string for checking DHCP end address in system defaults file */
    public static final String STRING_DHCP_END_ADDR = "dhcp_end=";

    /** File path for system defaults file */
    public static final String FILE_SYSTEM_DEFAULTS = " /etc/utopia/system_defaults";

    /** String value for Regular expression (.*) */
    public static final String REG_EXPRESSION_DOT_STAR = "(.*)";

    /** Valid Gateway local DHCP Start address in setting Gateway Ip */
    public static final String TEST_GATEWAY_LOCAL_DHCP_START_ADDR = "";

    /** Valid Gateway local DHCP Start address in setting Gateway Ip */
    public static final String TEST_GATEWAY_LOCAL_DHCP_END_ADDR = "";

    /** Telescope Rest API URL. */
    public static final String TELESCOPIC_REST_API_URL = "telescopic.rest.api.url";

    /** WebPA parameter for Device ManagementServer */
    public static final String WEBPA_PARAM_DEVICE_MANAGEMENTSERVER = "Device.ManagementServer.";

    /** Holds error message when command not found */
    public static final String CMD_NOT_FOUND = "command not found";

    /** Holds the string commercial */
    public static final String COMMERCIAL = "commercial";

    /** Constant for holding string FAILED */
    public static final String STRING_FAILED = "FAILED";

    /**
     * String constant to hold pattern to find telemetry endpoint enable status form response
     */
    public static final String PATTERN_TO_FIND_TELEMETRY_ENDPOINT_ENABLE = "RFC:  updated for Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.TelemetryEndpoint.Enable from value old=(\\w*)";

    /**
     * String constant to hold pattern to grep telemetry endpoint enable status from response
     */
    public static final String PATTERN_TO_GREP_TELEMETRY_ENDPOINT_ENABLE = "RFC:  updated for Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.TelemetryEndpoint.Enable from value old";

    /** String constant to store TR069 support */
    public static final String TR069_SUPPORT = "TR069support";

    /** File path for TR69log.txt */
    public static final String RDKLOGS_LOGS_TR69LOG_TXT_0 = "/rdklogs/logs/TR69log.txt.0";

    /** Log message for ACS request complete */
    public static final String ACS_REQUEST_COMPLETE = "ACS Request has completed with status code 0";

    /** String to store process name CcspTr069PaSsp */
    public static final String PROCESS_NAME_CCSPTR069PASSP = "CcspTr069PaSsp";

    /** String for expected TR69 cert location */
    public static final String STRING_EXPECTED_TR69_CERTLOCATION = "/etc/cacert.pem";

    /** Default certiticate location from property */
    public static final String PROP_KEY_TR69_CORRECT_ACS_URL = "correct.acs.url";

    /** Default ACS url from property */
    public static final String PROP_KEY_TR69_DEFAULT_ACS_URL = "default.acs.url";

    /** Constant for number 9 */
    public static final int CONSTANT_9 = 9;

    /** command to make cpu usage 100% */
    public static final String COMMAND_TO_MAKE_CPU_USAGE_HUNDRED_PERCENT = "yes > /dev/null &";

    /** String variable to store cpu usage 100% */
    public static final String STRING_CPU_USAGE_100_PERCENT = "CPU usage is 100";

    /**
     * String variable to store Avg CPU usage after 5 minutes of CPU Avg monitor window as 100%
     */
    public static final String STRING_AVG_CPU_AVG_MONITOR_WINDOW = "RDKB_SELFHEAL : Avg CPU usage after 5 minutes of CPU Avg monitor window is 100";

    /** String variable to store top five task in device */
    public static final String STRING_TOP_FIVE_TASK = "RDKB_SELFHEAL : Top 5 tasks running on device";

    /** pattern to validate message format from selfheal.txt.0 **/
    public static final String PATTERN_TO_VALIDATE_MSG_FROMAT_FROM_SELFHEAL = "((\\s+\\w+,\\s+\\d{0,3}%,)+(\\s+\\w+,\\s+\\d{0,3}%))";

    /** Constant to hold regex to get the top 5 process running in device */
    public static final String REGEX_TOP_FIVE_PROCESS = "RDKB_SELFHEAL : Top 5 tasks running on device\r?\n(.*)\r?\n(.*)\r?\n(.*)\r?\n(.*)\r?\n(.*)";

    /**
     * Default value of webpa Device.WiFi.X_RDKCENTRAL-COM_BandSteering.BandSetting.2.PhyRateThreshold
     */
    public static final String WEBPA_DEFAULT_VALUE_BAND_STEERING_PHY_THRESHOLD_5GHZ = "6000";

    /** value to be set for PhyThreshold in band steering */
    public static final String BAND_STEERING_PHY_THRESHOLD_VALUE = "40";

    /**
     * Default value of webpa Device.WiFi.X_RDKCENTRAL-COM_BandSteering.BandSetting.1.PhyRateThreshold
     */
    public static final String WEBPA_DEFAULT_VALUE_BAND_STEERING_PHY_THRESHOLD_2GHZ = "50000";

    /**
     * Test value of webpa Device.WiFi.X_RDKCENTRAL-COM_BandSteering.BandSetting.1.IdleInactiveTime
     */
    public static final String TEST_VALUE_IDLE_INACTIVE_TIME_2GHZ = "11";

    /**
     * Test value of webpa Device.WiFi.X_RDKCENTRAL-COM_BandSteering.BandSetting.2.IdleInactiveTime
     */
    public static final String TEST_VALUE_IDLE_INACTIVE_TIME_5GHZ = "13";

    /**
     * Test value of webpa Device.WiFi.X_RDKCENTRAL-COM_BandSteering.BandSetting.1. OverloadInactiveTime
     */
    public static final String TEST_VALUE_OVERLOAD_INACTIVE_TIME_2GHZ = "12";

    /**
     * Test value of webpa Device.WiFi.X_RDKCENTRAL-COM_BandSteering.BandSetting.2. OverloadInactiveTime
     */
    public static final String TEST_VALUE_OVERLOAD_INACTIVE_TIME_5GHZ = "14";

    /** Constant to hold Null MAC address without delimeter */
    public static final String NULL_MAC_ADDRESS_WITHOUT_DELIMETER = "00000000000d";

    /** Constant to hold WiFiClient Schema type */
    public static final String WIFICLIENT_SCHEMA_TYPE = "WifiSingleClient.avsc";

    /**
     * enum variable to store the band steering parameter to check in lbd.conf
     */
    public enum BAND_STEERING_PARAM {
	BAND_STEERING_PARAM_IDLE_INACTIVE_TIME,
	BAND_STEERING_PARAM_OVERLOAD_INACTIVE_TIME,
	BAND_STEERING_PARAM_APGROUP;
    }

    /** Command to check the IdleInactiveTimein lbd.conf */
    public static final String CMD_LBD_IDLE_INACTIVE = "InactIdleThreshold";

    /** Command to check the OverloadInactiveTimein lbd.conf */
    public static final String CMD_LBD_OVERLOAD_INACTIVE = "InactOverloadThreshold";

    /** Command to check the IdleInactiveTimein cfg */
    public static final String CMD_CFG_IDLE_INACTIVE = "/sbin/cfg -s | grep BS_IS_NORM_INACT_TIMEOUT";

    /** Command to check the OverloadInactiveTimein cfg */
    public static final String CMD_CFG_OVERLOAD_INACTIVE = "/sbin/cfg -s | grep BS_IS_OVERLOAD_INACT_TIMEOUT";

    /** Command to get the contents of lbd.conf */
    public static final String CMD_CAT_LBD = "cat /tmp/lbd.conf";

    /** Command to check the 2g params in lbd.conf */
    public static final String STRING_LBD_HEADING_2G_PARAMS = "[WLANIF2G]";

    /** Command to check the 5g params in lbd.conf */
    public static final String STRING_LBD_HEADING_5G_PARAMS = "[WLANIF5G]";

    /** Command to check the STDAB lbd.conf */
    public static final String STRING_LBD_STADB_PARAMS = "[STADB]";

    /**
     * Command to get ip from ifconfig for the given interface. Replace the <REPLACE> with interface name
     */
    public static final String GET_IP_OF_INTERFACE_FROM_IFCONFIG = "/sbin/ifconfig <REPLACE> |grep 'inet '|awk '{print $2}'";

    /**
     * Simple Data Format - System Date and Time retrieved by executing date command
     */
    public static final String SYSTEM_DATE_TIME_FORMAT_RETRIEVED = "E MMM dd HH:mm:ss zzz yyyy";

    /** Pattern Matcher to retrieve the resource usage */
    public static final String COMMAND_TO_RETRIEVE_RESOURCE_USAGE = "grep -i \"RDKB_SELFHEAL : CPU usage is \" /rdklogs/logs/SelfHeal.txt.0";

    /** Constant to hold regex for matching CPU RESOURCE USAGE TIME FORMAT */
    public static final String PATTERN_MATCH_FOR_TIME_FORMAT = "at\\s+timestamp\\s+\\d{4}:\\d{2}:\\d{2}:(\\d{2}:\\d{2}:\\d{2})";

    /** String value for -150 */
    public static final String STRING_VALUE_MINUS_ONE_FIFFY = "-150";

    /** pattern to verify the TAD process */
    public static final String PATTERN_TO_GET_TAD_RUNNING_STATUS = "(/usr/ccsp/tad)";

    /** Integer value 4 */
    public static final Integer INTEGER_VALUE_4 = 4;

    /** Constant to hold spot single colon followed by space for LSA */
    public static final String COLON_AND_SPACE = ": ";

    /** resolv.conf partition file name **/
    public static final String RESOLV_CONF_PARTITION = "\"resolv.conf\"";

    /** String variable to store resolv.conf partition * */
    public static final String STRING_PARTITION_RESOLV_CONF = "/resolv.conf";

    /** pattern to get read write permission for resolv.conf * */
    public static final String PATTERN_GET_READ_WRITE_PERMISSION_RESOLV_CONF_PARTITION = "\\/resolv.conf.*[(](rw)[)]?";

    /** Constant holding the location of resolv.conf file. */
    public static final String RESOLVE_DOT_CONF_FILE = "/etc/resolv.conf";

    /** pattern to get ipv4 or ipv6 server address * */
    public static final String PATTERN_GET_IPV4_OR_IPV6_SERVER_ADDRESS = " ";

    /** pattern to get namespace server address * */
    public static final String PATTERN_GET_NAMESPACE_SERVER_ADDRESS = "nameserver (\\w+[.|:]\\w+[.|:]\\w+[.|:][\\w+|:]+)";

    /** interface name to brlan0 */
    public static final String INTERFACE_NAME_BRLAN0 = "brlan0";

    /** Verifying IPv6 address in the interface */
    public static final String INET_V6_ADDRESS_PATTERN_ALTERNATE = "inet6 addr:\\s*(\\w+::\\w+:\\w+:\\w+:\\w+)\\/\\d+\\s*Scope:[A-Za-z]*";

    /** String to store ping response in windows */
    public static final String STRING_WINDOWS_PING_RESPONSE = "Lost = 0";

    /** Pattern Matcher to retrieve the Pid of wifi process */
    public static final String PATTERN_MATCHER_TO_GET_WIFI_PROCESS_PID = "(\\d+)\\sroot";

    /** Log string to validate the param value Device.Time.Enable as true */
    public static final String LOG_STRING_ENABLING_NETWORK_TIME = "Enabling Network Time Sync";

    /** Log string to validate the param value Device.Time.NtpServerUrl as true */
    public static final String LOG_STRING_SETTING_NTPSERVER = "Setting NTPServer as ";

    /** Constant to hold the command for uptime */
    public static final String CMD_DATE = "date";

    /** Simple Data Format - System Date and Time to set */
    public static final String SYSTEM_DATE_TIME_FORMAT_TO_SET = "HH:mm:ss";

    /** String to hold dropbear process */
    public static final String STRING_DROPBEAR_PROCESSOR = "dropbear";

    /** Constant for number 14 */
    public static final int CONSTANT_14 = 14;

    /** Constant to store the telephone line status as up(2) */
    public static final String SNMP_RESPONSE_FOR_TEL_LINE_STATUS_UP = "up";

    /** MeshAgent Log File Name */
    public static final String LOG_FILE_MESHAGENT = "/rdklogs/logs/MeshAgentLog.txt.0";

    /** Constant to hold Mesh syscfg db output */
    public static final String MESH_SYSCFG_DB_OP = "mesh_enable=";

    /** The property key for partner ID Names */
    public static final String PROP_KEY_PARTNER_ID_LIST = "partner.id.list";

    /**
     * System property isWebPA which will determine whether the tr69 parameter needs to be run through WebPA or tr-069
     */
    public static final String SYSTEM_PROPERTY_ISWEBPA = "isWebPA";

    /** file to store the logs for cdl using xconf device commands */
    public static final String FILE_TO_STORE_CDL_LOGS = "/nvram/xconf.txt.0";

    /** Constant for update status loop count. */
    public static final int ESTB_SW_UPDATE_STATUS_LOOP_COUNT = 20;

    /** Constant to hold configured Textures Cache Limit */
    public static final int CONFIGURED_TEXTURES_CACHE_LIMIT_IN_MB = 40;

    /** String variable blacklist */
    public static final String STRING_BLACKLIST = "blacklist";

    /** Test constant for phish tank sites in stb.properties file */
    public static final String PROP_KEY_PHISH_TANK_SITES = "phish.tank.urls";

    /** Command to check running status of dnsmasq service */
    public static final String PROP_KEY_LOG_FILE_SECURE_SYSCFG = "logfile.secure.syscfg";

    /** Regex for getting the acs url */
    public static final String PATTERN_TO_GET_ACS_URL = "ManagementServerURLID_PSM:\\s+(.*)\\s";

    /** Constant to hold the Management Server URL */
    public static final String MANAGEMENT_SERVER_URL = AutomaticsPropertyUtility
	    .getProperty(BroadBandTestConstants.PROP_KEY_TR69_CORRECT_ACS_URL);

    /** Constant to hold URL of wikipedia */
    public static final String URL_WIKIPEDIA = "https://www.wikipedia.org";

    /**
     * The array list containing phished sites from https://www.phishtank.com/phish_archive.php?page=0 page with valid
     * phish and online
     * 
     */
    public static final List<String> LIST_OF_PHISHED_WEBSITES = new ArrayList<String>() {
	{
	    String url = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_PHISH_TANK_SITES);
	    List<String> listOfSites = Arrays.asList(url.split(","));
	    for (String site : listOfSites) {
		add(site);
	    }
	}
    };

    /** String regex to get ethernet connected client ipv4 from ifconfig */
    public static final String STRING_REGEX_TO_GET_ETHERNET_IPV6 = ".*inet6[a-z:\\s](([0-9a-f]{1,4}:){7}([0-9a-f]{1,4}))[\0-9]*\\s.*";

    /** REGEX for String Address and IPv4 Address */
    public static final String REGEX_STRING_ADDRESS_AND_IPV4_ADDRESS = "Address: +(\\d+\\.\\d+\\.\\d+\\.\\d+)";

    /** REGEX for String Name and host name */
    public static final String REGEX_STRING_NAME_AND_HOST_NAME = "Name:\\s+(\\S+)";

    /** Test constant for string 'nslookup' */
    public static final String STRING_NS_LOOKUP = "nslookup";

    /** String Variable to store Name and colon */
    public static final String STRING_NAME_COLON = "Name:";

    /** String Variable to store Address and colon */
    public static final String STRING_ADDRESS_COLON = "Address:";

    /** String Variable to hold answer */
    public static final String STRING_ANSWER_COLON = "answer:";

    /** Constant to hold string that needs to append for setting 2.4 GHZ SSID */
    public static final String APPEND_STRING_FOR_24_GHZ_SSID_NAME_CHANGE = "24GHZ_private_";

    /** Constant to hold string that needs to append for setting 5 GHZ SSID */
    public static final String APPEND_STRING_FOR_5_GHZ_SSID_NAME_CHANGE = "5GHZ_private_";

    /** Command to get ip from ifconfig for the given interface */
    public static final String GET_GATEWAY_IP_FROM_IFCONFIG = "/sbin/ifconfig brlan0|grep \"inet addr\"|grep -oE \"\\b([0-9]{1,3}\\.){3}[0-9]{1,3}\\b\"|head -n 1";

    /** Substring to be checked for PROD Builds */
    public static final String PROD_BUILD_SUBSTRING = "_prod_";

    /** Sample Password for testing */
    public static final String TEST_SSID_PASSWORD = "password123";

    /** Constant for 2.4GHZ OR 5GHZ band */
    public static final String BAND_2_4GHZ_OR_5GHZ = "2.4GHZ OR 5GHZ";

    /** Time value for DHCP lease time */
    public static final String STRING_LEASE_TIME_DEFAULT_VALUE = "604800";

    /** String to store the device comment */
    public static final String STRING_DEVICE_COMMENT = "reserved_Ip_test";

    /** Constant to hold URL of HTTPS site: https://www.facebook.com */
    public static final String URL_HTTPS_FACEBOOK = "https://www.facebook.com/";

    /** String to store value of dhcp switch script */
    public static final String DHCP_SWITCH_SCRIPT = "dhcpswitch.sh";

    /** String to store process name udhcpc */
    public static final String PROCESS_NAME_UDHCPC = "udhcpc";

    /** The constant holding a command to ifconfig erouter. */
    public static final String IFCONFIG_EROUTER = "/sbin/ifconfig erouter0";

    /** String value for 100 */
    public static final String STRING_VALUE_ONE_HUNDRED = "100";

    /** Constant to hold Not applicable message for residential class devices */
    public static final String NA_MSG_FOR_DSL_DEVICES = "Test Step Not Applicable for DSL devices";

    /** String value for MAC_ADDRESS */
    public static final String MAC_ADDRESS = "MACAddress";

    /** String value for DeviceName */
    public static final String DEVICE_NAME = "DeviceName";

    /** Constant for mac Filter as Deny */
    public static final String MAC_FILTER_DENY = "Deny";

    /** String value for Connected_Device */
    public static final String CONNECTED_DEVICE = "Connected_Device";

    /** Constant for mac Filter as Allow */
    public static final String MAC_FILTER_ALLOW = "Allow";

    /** command to get log upload details */
    public static final String CMD_GET_LOG_UPLOAD_DETAILS = "cat /nvram/automation_test.txt | grep -i \"CURL_CMD\"";

    /** System command to get ping data block size */
    public static final String CMD_GET_PING_DATA_BLOCK_SIZE = "cat /opt/secure/data/syscfg.db  | grep selfheal_ping_DataBlockSize";

    /** pattern to get ping data block size */
    public static final String PATTERN_GET_PING_DATA_BLOCK_SIZE = "selfheal_ping_DataBlockSize=(\\d+)";

    /** pattern to get log upload details */
    public static final String PATTERN_GET_LOG_UPLOAD_TIMING = "\\d+-(\\d+:\\d+:\\d+).\\d+ [d]?[c]?[a]?[:]?[ ]?CURL_CMD";

    /** String to store File path to snmp certs */
    public static final String FILE_LOCATION_SNMP_CERTS = "/etc/ssl/certs/snmp/tls/certs/";

    /** String - regex to retrieve not before date in cert */
    public static final String REGEX_TO_RETRIEVE_CERT_NOT_BEFORE_DATE = "Not Before:\\s*(.*)";

    /** String - regex to retrieve not after date in cert */
    public static final String REGEX_TO_RETRIEVE_CERT_NOT_AFTER_DATE = "Not After :\\s*(.*)";

    /** String to store snmpv3 cert encryption */
    public static final String SNMPV3_CERT_SIGNATURE = "sha256WithRSAEncryption";

    /** String to store prop snmpv3.auth.mechanis */
    public static final String SNMPV3_AUTH_MECHANISM = "snmpv3.auth.mechanism";

    /** String snmpv3 auth type SHA1 */
    public static final String SNMPV3_AUTH_SHA1 = "SHA1";

    /** String to store format of cert */
    public static final String DATE_FORMAT_FOR_CERT = "MMM dd HH:mm:ss yyyy z";

    /** String - regex to retrieve signature in cert */
    public static final String REGEX_TO_RETRIEVE_CERT_SIGNATURE = "Signature Algorithm:\\s*(.*)";

    /** String snmpv3 auth type SHA256 */
    public static final String SNMPV3_AUTH_SHA256 = "SHA256";

    /** String value 180 */
    public static final String STRING_180 = "180";

    /** String for executable file */
    public static final String TEMPORARY_EXECUTABLE_FILE = "test.sh";

    /** String for File with full permissions */
    public static final String FULL_FILE_PERMISSIONS_FOR_FILE = "rwxrwxrwx";

    /** Process name hostapd */
    public static final String PROCESS_NAME_HOSTAPD = "hostapd";

    /** String value 300 */
    public static final String STRING_300 = "300";

    /**
     * Pattern to get the mac address of client device from /tmp/wifiMon log file
     */
    public static final String PATTERN_CONNECTED_DEVICE_MAC = "Device:(\\w+:\\w+:\\w+:\\w+:\\w+:\\w+) connected on ap:1";

    /** Linux command to remove any files. */
    public static final String CMD_REMOVE_FORCEFULLY = "rm -f ";

    /** Constant to hold default begining address value */
    public static final String DEFAULT_DHCPV4_BEGIN_ADDRESS = "DEFAULT_DHCPV4_BEGIN_ADDRESS";

    /** Constant to hold default begining address value */
    public static final String DEFAULT_DHCPV4_ENDING_ADDRESS = "DEFAULT_DHCPV4_ENDING_ADDRESS";

    /** Constant to hold default dhcp lease time */
    public static final String DEFAULT_DHCPLEASE_TIME = "DEFAULT_DHCPLEASE_TIME";

    /** String for <<INTERFACE>> */
    public static final String STRING_INTERFACE_TO_REPLACE = "<<INTERFACE>>";

    /** Text to look up for in the Interface Information */
    public static final String WIFI_PACKET_MONITOR_TEXT = "Mode:Monitor";

    /** Auto vault rdkb file path */
    public static final String AUTOVAULT_FILE_PATH = "cpefiles/rdkb/";

    /** Constant to hold commands for ssh and telnet */
    public enum sshTelnetCommandExpectedOutput {

	LINUX_COMMANDS(
		"ssh -o ConnectTimeout=10 admin@<REPLACE>",
		"timeout 10 telnet <REPLACE>",
		"port 22: Connection timed out",
		""),
	WINDOWS_COMMANDS("ssh admin@<REPLACE>", "telnet <REPLACE>", "port 22: Connection timed out", "");

	String sshCommand;
	String telnetCommand;
	String expectedSsh;
	String expectedTelnet;

	public String getSshCommandWithIp(String ip) {
	    return sshCommand.replace(BroadBandTestConstants.STRING_REPLACE, ip);
	}

	public String getTelnetCommandWithIp(String ip) {
	    return telnetCommand.replace(BroadBandTestConstants.STRING_REPLACE, ip);
	}

	public String getExpectedSsh() {
	    return expectedSsh;
	}

	public String getExpectedTelnet() {
	    return expectedTelnet;
	}

	sshTelnetCommandExpectedOutput(String sshCommand, String telnetCommand, String expectedSsh,
		String expectedTelnet) {
	    this.sshCommand = sshCommand;
	    this.telnetCommand = telnetCommand;
	    this.expectedSsh = expectedSsh;
	    this.expectedTelnet = expectedTelnet;
	}

    }

    /** Constant to Not_Applicable value to verify Fan Rotor status */
    public static final String STRING_FAN_NOT_APPLICABLE_ROTOR = "Not_Applicable";

    /** Log for CDL started condition */
    public static final String LOG_CDL_COMPLETED = "### httpdownload completed ###";

    /** Log for CDL started condition */
    public static final String LOG_CDL_SUCCESSFUL = "HTTP download Successful";

    /** The constant holds the WebPA success code */
    public static final int SUCCESS_CODE = 200;

    /** String to Verirfy the firmware downlaod Failed status */
    public static final String STRING_FIRMWARE_DOWNLOAD_FAILED = "Failed";

    /** String to Verirfy the firmware downlaod completed status */
    public static final String STRING_FIRMWARE_DOWNLOAD_COMPLETED = "Completed";

    /** Command tot remove waiting for reboot flag **/
    public static final String CMD_REMOVE_WAITING_REBOOT_FLAG = "rm -rf /tmp/.waitingreboot";

    /** String to store Not Applicable */
    public static final String NOT_APPLICABLE_VALUE = "Not Applicable";

    /** STRING to RESET OID */
    public static final String STRING_MTA_RESET_OID = "1.3.6.1.2.1.140.1.1.1";

    /** Constant for mac Filter as Allow-All */
    public static final String MAC_FILTER_ALLOW_ALL = "Allow-ALL";

    public static final String PROP_KEY_DIBBLER_VERSION = "current.dibbler.version";

    /** String value to store dibbler pattern */
    public static final String PATTER_DIBBLER_VERSION = "DHCPv6, version (\\S+)\\s+";

    /** String variable to store disable */
    public static final String STRING_DISABLE = "Disable";

    public static final String STRING_UP = "up";

    /** Constant for ssid frequency both band */
    public static final String SSID_FREQ_BOTH_BAND = "BOTH";

    /** Default response for NS lookup IPV4 address */
    public static final String NS_LOOKUP_GLOBAL_DNS_RESPONSE = "I am not an OpenDNS resolver";

    /** Test constant for string 'DNS request timed out' error message */
    public static final String STRING_DNS_REQUEST_TIMED_OUT_ERROR_MESSAGE = "DNS request timed out";

    /** Default nslookup response pattern .ash */
    public static final String PATTERN_STRING_DOT_ASH = ".ash";

    /** Default nslookup response pattern .nyc */
    public static final String PATTERN_STRING_DOT_NYC = ".nyc";

    /** Constant to hold the DNS resolver1 IP 208.67.222.222 */
    public static final String NS_LOOKUP_IP_208_67_222_222 = "208.67.222.222";

    /** Constant to hold the DNS resolver2 IP 208.67.220.220 */
    public static final String NS_LOOKUP_IP_208_67_220_220 = "208.67.220.220";

    /** Constant to hold the DNS resolver3 IP 208.67.222.220 */
    public static final String NS_LOOKUP_IP_208_67_222_220 = "208.67.222.220";

    /** Constant to hold the DNS resolver4 IP 208.67.220.222 */
    public static final String NS_LOOKUP_IP_208_67_220_222 = "208.67.220.222";

    /** Constant to hold the DNS resolver1 domain name */
    public static final String SERVER_NAME_208_67_222_222 = "resolver1.opendns.com";

    /** Constant to hold the DNS resolver2 domain name */
    public static final String SERVER_NAME_208_67_220_220 = "resolver2.opendns.com";

    /** Constant to hold the DNS resolver3 domain name */
    public static final String SERVER_NAME_208_67_222_220 = "resolver3.opendns.com";

    /** Constant to hold the DNS resolver5 domain name */
    public static final String SERVER_NAME_208_67_220_222 = "resolver4.opendns.com";

    /** Constant for reboot priority forced */
    public static final String FW_DWN_PRIORITY_FORCED = "forced";

    /** Expected string in log file after kernel crash */
    public static final String STRING_KERNEL_DUMP_END = "PREVIOUS_KERNEL_OOPS_DUMP_END";

    /** Constant to hold regex to check channel list from webpa for Atom */
    public static final String PATTERN_MATCHER_24_GHZ_CHANNEL_ATOM = "\\d+-\\d+";

    /** Constant to hold regex to check the channel list from webpa */
    public static final String PATTERN_MATCHER_CHANNEL_LIST_WITH_COMMA = "[0-9]+(,[0-9]+)+";

    /**
     * String to store pattern matcher for channel select of 2.4 GHZ retrieved from web GUI
     */
    public static final String PATTER_MATCHER_FOR_CHANNEL_SELECT_OF_2GHZ_PRIVATE_WIFI = "^(-1|[1-9]|10|11)$";

    /**
     * String to store pattern matcher for channel select of 5 GHZ retrieved from web GUI
     */
    public static final String PATTER_MATCHER_FOR_CHANNEL_SELECT_OF_5GHZ_PRIVATE_WIFI = "^(-1|36|40|44|48|52|56|60|64|100|104|108|112|116|120|124|128|132|136|140|144|149|153|157|161)$";

    /** folder path of tmp file */
    public static final String FOLDER_TMP = "tmp";

    /** String to identify tar path */
    public static final String PATH_TO_TAR = "/bin/tar";

    /** String for tar file */
    public static final String TAR_TEMPORARY_FILE = "sample.tar.gz";

    /** String read only file system */
    public static final String TEXT_RO_FILE_SYSTEM = "Read-only file system";

    /** property to get ca-chain.cert.pem */
    public static final String PROP_KEY_GET_CA_CHAIN_CERT_PEM = "rdkb.ca.chain.cert.pem";

    /** property to get radiussrv.cert.pem */
    public static final String PROP_KEY_GET_RADIUSSRV_CERT_PEM = "rdkb.radiussrv.cert.pem";

    /** String gmail.com */
    public static final String NSLOOKUP_FOR_GMAIL = "gmail.com";

    /** String amazon.com */
    public static final String NSLOOKUP_FOR_AMAZON = "amazon.com";

    /** String opendns.com */
    public static final String NSLOOKUP_FOR_OPENDNS = "opendns.com";

    /** String Ip adress of Open dns 8.8.8.8 */
    public static final String STRING_OPEN_DNS_IP_8 = "8.8.8.8";

    /** String Ip adress of Open dns 2a02:c7a:601:a::5000 for DSL */
    public static final String STRING_OPEN_DNS_IP_DSL = "2a02:c7a:601:a::5000";

    /** String Ip adress of Open dns 75.75.76.76 */
    public static final String STRING_OPEN_DNS_IP_75_76 = "75.75.76.76";

    /** String Ip adress of opendns.com 208.67.222.222 */
    public static final String STRING_OPEN_DNS_IP_222 = "208.67.222.222";

    /** String yahoo.com */
    public static final String NSLOOKUP_FOR_YAHOO = "yahoo.com";

    /** String Ip adress of opendns.com 208.67.220.220 */
    public static final String STRING_OPEN_DNS_IP_220 = "208.67.220.220";

    /** String comcast.com */
    public static final String NSLOOKUP_FOR_COMCAST = "comcast.com";

    /** String cisco.com */
    public static final String NSLOOKUP_FOR_CISCO = "cisco.com";

    /** String Ip adress of opendns.com 208.67.222.220 */
    public static final String STRING_OPEN_DNS_IP_222_220 = "208.67.222.220";

    /** String Ip adress of opendns.com 208.67.220.222 */
    public static final String STRING_OPEN_DNS_IP_220_222 = "208.67.220.222";

    /** String retry */
    public static final String STRING_RETRY = "retry";

    /** String valueretrieved to replace */
    public static final String STRING_VALUERETRIEVED_TO_REPLACE = "<<VALUERETRIEVED>>";

    /** String valueparamter to replace */
    public static final String STRING_VALUEPARAMETER_TO_REPLACE = "<<VALUEPARAMETER>>";

    /** String timeout with value 20 */
    public static final String STRING_TIMEOUT_WITH_VALUE_20 = "timeout 20";

    /** String retry with value 20 */
    public static final String STRING_RETRY_WITH_VALUE_20 = "retry 20";

    /** Failed to get IP address for STRING */
    public static final String STRING_FAILED_TO_GET_IP = "Failed to get IP address for";

    /** The end string of privacy message. */
    public static final String END_OF_SSH_CONNECTION_PRIVACY_MESSAGE = "law enforcement.";

    /** Message Unable to start the SSH Connection */
    public static final String MESSAGE_UNABLE_TO_START_SSH = "Unable to start ssh connection to RDKB device";

    /** String password prompt '[sudo] password for' */
    public static final String STRING_SUDO_PASSWORD = "[sudo] password for ";

    /** Property key for Reverse SSH Command for Fibre devices */
    public static final String PROP_KEY_CMD_REVERSE_SSH_FIBRE = "reversessh.jump.server.fibredevice";

    /** Property key for Reverse SSH Command for RDKB devices. */
    public static final String PROP_KEY_CMD_SUDO_REVERSE_SSH_RDKB = "reversessh.jump.server.rdkb";

    /** The constant holding # character for expect method */
    public static final String SINGLE_HASH_TERMINATING_CHAR = "#";

    /** Constant to hold the grep command to extract memory utilisation */
    public static final String CMD_GREP_MEM_UTIL = "Mem:\\s+(\\d+).*";

    /** Constant to hold the grep command to extract memory utilisation */
    public static final String CMD_GREP_CPU_UTIL = "CPU:\\s+(\\d+).*";

    /** Pattern for verifying the telemetry marker. */
    public static final String PATTERN_FOR_TELEMETRY_MARKER = "(([+|-]?\\d+(,)?)+)";

    /** wait for 12 minutes */
    public static final long TWELVE_MINUTE_IN_MILLIS = TEN_MINUTE_IN_MILLIS + TWO_MINUTE_IN_MILLIS;

    /** Constant to hold wait duration of 300 Seconds */
    public static final String FIVE_MINUTES_IN_SECONDS = "300";

    /** string for holding the file name */
    public static final String FILE_WIFIHEALTH = "wifihealth.txt";

    /** Wi-Fi type Public SSID */
    public static final String WIFI_TYPE_PUBLIC = "PUBLIC_WIFI";

    /** String value ACTIVE */
    public static final String STRING_ACTIVE = "ACTIVE";

    /** String value INACTIVE */
    public static final String STRING_INACTIVE = "INACTIVE";

    /** String value host */
    public static final String STRING_HOST = "host";

    /**
     * String argument for WebPA command for parameter Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.ReverseSSH.
     * xOpsReverseSshArgs without host
     */
    public static final String BROADBAND_REVERSE_SSH_ARGUMENTS_EXCEPT_HOST = "idletimeout=300;revsshport='portToUse';sshport=22;user=webpa_user01;";

    /** Constant to denote the Pre-Condition Error */
    public static final String PROPERTY_NOT_FOUND_ERROR = "Property not found in stb.Properties : ";

    /** Command to get ip from ifconfig for the given interface */
    public static final String CMD_CHECK_INTERFACE_LAN = "/sbin/ifconfig brlan0|grep \"inet addr\"";

    /** Constant to hold Constant of Wifi Client Type */
    public static final String CLIENT_TYPE_WIFI = "WIFI";

    /** Command to obtain the current box date and time **/
    public static final String CMD_FOR_DATE = "date";

    /** Pattern to retrieve day of the week */
    public static final String PATTERN_TO_RETRIEVE_DAY_OF_THE_WEEK_FROM_TIMESTAMP = "([a-z|A-Z]{3}).*\\d+:\\d+:\\d+\\s+UTC";

    /** Constant to hold Time as 11 PM in HH format */
    public static final String ONLY_HOURS_11_PM = "23";

    /** Consatnt to hold format for day of the week */
    public static final String FORMAT_FOR_DAY_OF_THE_WEEK = "E";

    /** Pattern to retrieve Hour from timestamp */
    public static final String PATTERN_TO_RETRIEVE_HOUR_FROM_TIMESTAMP = "(\\d+):\\d+:\\d+\\s+UTC";

    /** Constant for comma. */
    public static final String COMMA = ",";

    /** Constant to hold instagram */
    public static final String INSTAGRAM = "instagram";

    /** Constant to hold SiteBlocked */
    public static final String SITE_BLOCKED = "SiteBlocked";

    /** Constant to hold Time as 12 AM in HH:MM format */
    public static final String HOURS_12_AM = "00:00";

    /** Constant to hold Time as 11:59 PM in HH:MM format */
    public static final String HOURS_23_59_PM = "23:59";

    /** Constant to hold wikipedia */
    public static final String WIKIPEDIA = "wikipedia";

    /** Constant to hold w3schools */
    public static final String W3SCHOOLS = "w3schools";

    /** Constant to hold URL of instagram */
    public static final String URL_INSTAGRAM = "https://www.instagram.com";

    /** Constant to hold URL of w3school */
    public static final String URL_W3SCHOOLS = "https://www.w3schools.com";

    /** Holds message returned by curl command Could not resolve host */
    public static final String ACCESS_TO_URL_USING_CURL_CONNECTION_COULD_NOT_RESOLVE_HOST_MESSAGE = "Could not resolve host";

    /** VAR PATH **/
    public static final String VAR_PATH = "/var/";

    /** name of script to set the static IP */
    public static final String SCRIPT_CLICONFIG = "cliconfig.txt";

    /** pattern for sucessful execution */
    public static final String RESPONSE_EXECUTION_SUCCEED = "Execution succeed";

    /** path for CCSP_BUS_CLIENT_TOOL in Business Class devices */
    public static final String PATH_BUSSINESSCLASS_CCSP_BUS_CLIENT_TOOL = "/usr/bin/";

    /** path for CCSP_BUS_CLIENT_TOOL in BWG devices */
    public static final String PATH_BUSINESSGW_CCSP_BUS_CLIENT_TOOL = "/fss/gw/usr/ccsp/";

    /** pattern to get the wan ip address set in script */
    public static final String PATTERN_TO_GET_WAN_IP = "wan_ip_address (.*)";

    /** default location to copy static IP file to Device */
    public static final String DEFAULT_LOCATION_TO_COPY_FILE = "/cpefiles/rdkb/cliconfig.txt";

    public static final String PING_UNREACHABLE = "Network is unreachable";
    public static final String PING_FAILURE = "General failure";
    public static final String PING_LOSS = "100% loss";
    public static final String PING_LOSS_LINUX = "100% packet loss";

    /** String to hold sshbanner.txt file path */
    public static final String STRING_PATH_SSH_BANNER_TXT_FILE = "/etc/sshbanner.txt";

    /** String for sshbanner.txt file */
    public static final String SSH_BANNER_TXT_FILE_NAME = "sshbanner.txt";

    /** constant for string Enabled */
    public static final String INTERFACE_ENABLED_STATUS = "Enabled";

    /** Constant to hold the down status */
    public static final String STRING_LOWERLAYER_DOWN = "LowerLayerDown";

    /** String 5Ghz Private ssid name for bridge mode test cases */
    public static final String PRIVATEWIFI_NAME_FOR_5GHZ_BAND = "Hello_5";

    /** Key to get the possible channels in 2Ghz from Stb properties file */
    public static String KEY_FOR_2GHZ_WIFI_POSSIBLE_CHANNELS = "possibleChannelsIn2GhzWifi";

    /** constant to hold HTTP port number */
    public static final String HTTP_PORT_NUMBER = "80";

    /** Constant to hold Protocol as TCP/UDP */
    public static final String PROTOCOL_TCP_AND_UDP = "BOTH";

    /** Constant to hold Port Forwarding Description as HTTP */
    public static final String PORT_FORWARDING_RULE_DESCRIPTION_AS_HTTP = "HTTP";

    /** String value to store wan2lan config */
    public static final String STRING_WAN2LAN_CONFIG = "-A wan2lan_forwarding_accept -p tcp -m tcp -d";

    /** String value to store lan2wan config */
    public static final String STRING_LAN2WAN_CONFIG = "-A lan2wan_forwarding_accept -p tcp -m tcp -s";

    /** String value to store wan2lan udp config */
    public static final String STRING_WAN2LAN_UDP_CONFIG = "-A wan2lan_forwarding_accept -p udp -m udp -d";

    /** String value to store lan2wan udp config */
    public static final String STRING_LAN2WAN_UDP_CONFIG = "-A lan2wan_forwarding_accept -p udp -m udp -s";

    /** Command for getting firewall logs */
    public static final String STRING_FIREWALL = "firewall";

    /** String value for ip tables restore fail */
    public static final String STRING_IPTABLES_RESTORE_FAILURE = "iptables-restore: line 43 failed";

    /** Holds failure message returned by curl command when accessing web */
    public static final String ACCESS_TO_URL_USING_CURL_CONNECTION_FAILURE_MESSAGE = "Failed connect to";

    public static final String PARENTAL_CONTROL_PARAM_ARGUMENT_SITE = "Site";

    /** Opening brackets */
    public static final String OPENING_BRACKETS = "[";

    /** Closing brackets */
    public static final String CLOSING_BRACKETS = "]";

    /** String to check unit_activated used in syscfg get command */
    public static final String STRING_UNIT_ACTIVATED = "unit_activated";

    /** String to check Device.DeviceInfo.X_RDKCENTRAL-COM_OnBoarding_State */
    public static final String STRING_ONBOARDED = "OnBoarded";

    /** String to get sysevent status of snmp-onboard-reboot */
    public static final String STRING_SNMP_ONBOARD_REBOOT = "snmp-onboard-reboot";

    /** Log for reboot reason from arm console log */
    public static final String RDKB_REBOOT_REASON_ARM_CONSOLE = "RDKB_REBOOT: Device is up after reboot";

    /** String to check RDKB REBOOT */
    public static final String STRING_RDKB_REBOOT = "RDKB_REBOOT";

    /** constant for string Device.WiFi.SSID.10101. */
    public static final String VALUE_FOR_LOWERLAYER_DEVICE_WIFI_SSID_10101 = "Device.WiFi.SSID.10101.";

    /** constant for string Disabled */
    public static final String INTERFACE_DISABLED_STATUS = "Disabled";

    /** constant for string Device.WiFi.SSID.10001. */
    public static final String VALUE_FOR_LOWERLAYER_DEVICE_WIFI_SSID_10001 = "Device.WiFi.SSID.10001.";

    /** pattern to match supported modes */
    public static final String REG_EXPRESSION_SUPPORTED_MODES = "^[(\\w\\-),]+$";

    /** pattern to find the bssid value in wpa_supplicant file */
    public static final String PATTERN_BSSID_IN_WPA_SUPPLICANT = "(bssid=.*)";

    /** String constants for certs directory name */
    public static final String STRING_CERTS = "certs";

    /** text bssid for wpa_supplicant command */
    public static final String STRING_BSSID = "bssid ";

    /** text bssid with equal */
    public static final String STRING_BSSID_EQUAL = "/bssid=";

    /** text g for sed command */
    public static final String STRING_G = "/g' ";

    /**
     * pattern for time stamp value from radiusauthd.log file eg: 171228-10:57:43.196054
     */
    public static final String PATTERN_TIME_STAMP_RADIUSAUTH = "\\d+-(\\d+\\:\\d+\\:\\d+)\\.\\d+\\s+";

    /** process name wpa_supplicant */
    public static final String STRING_WPA_SUPPLICANT = "wpa_supplicant";

    /** pattern for session id from radiusauthd.log file */
    public static final String PATTERN_EAP_AUTHENTICATION_FAILED = "\\[(0x\\d+) \\d+.\\d+.\\d+.\\d+\\] EAP authentication failed";

    /** String name raspberry */
    public static final String TEXT_RASPBERRY = "Raspberry";

    /** stores the constant value for INT_UNSIGNED_INC_ZERO */
    public static final String CONSTANT_INT_UNSIGNED_INC_ZERO = "INT_UNSIGNED_INC_ZERO";

    /** constant for string Device.WiFi.Radio.10100. */
    public static final String VALUE_FOR_LOWERLAYER_DEVICE_WIFI_RADIO_10100 = "Device.WiFi.Radio.10100";

    /** constant for string Device.WiFi.Radio.10000. */
    public static final String VALUE_FOR_LOWERLAYER_DEVICE_WIFI_RADIO_10000 = "Device.WiFi.Radio.10000";

    /** Constant to integer value 24 */
    public static final int CONSTANT_24 = 24;

    /** The array list of wifi operating modes */
    public static final List<String> WIFI_OPERATING_MODES = new ArrayList<String>() {
	{
	    add("g,n");
	    add("b,g,n");
	    add("n");
	    add("a,n,ac");
	    add("ac");
	    add("n,ac");
	    add("ax");
	    add("g,n,ax");
	    add("ac,ax");
	    add("a,n,ac,ax");
	}
    };

    /** Constant to hold Alias name for 2.4GHZ radio as Radio0 */
    public static final String STRING_RADIO_0 = "Radio0";

    /** Constant to hold Alias name for 5GHZ radio as Radio1 */
    public static final String STRING_RADIO_1 = "Radio1";

    /** The array list of possible status values */
    public static final List<String> STATUS_VALUES = new ArrayList<String>() {
	{
	    add("Up");
	    add("Down");
	    add("Unknown");
	    add("Dormant");
	    add("NotPresent");
	    add("LowerLayerDown");
	    add("Error");
	}
    };

    /** The array list of wifi operating modes */
    public static final List<String> WIFI_REGULATORY_DOMAN = new ArrayList<String>() {
	{
	    add("USI");
	    add("Q2I");
	}
    };

    /** Regex to verify true or false */
    public static final String PATTERN_TRUE_OR_FALSE = "true|false";
    /** Regex to verify ETHERNET interface status */
    public static final String PATTERN_FOR_ETHERNET_INTERFACE_STATUS = "Up|Down|Unknown|Dormant|NotPresent|LowerLayerDown|Error";
    /** Regex to verify One letter with rest characters */
    public static final String PATTERN_ONE_LETTER_REST_CHARACTERS = "[a-zA-Z].*";
    /** Regex to verify Mac address */
    public static final String PATTERN_MAC_VALIDATION = "([a-fA-F0-9]{2}[:-]){5}[a-fA-F0-9]{2}";
    /** Regex to verify 20 numbers */
    public static final String PATTERN_20_NUMBERS = "[0-9]{0,20}";
    /** Regex to verify 10 numbers */
    public static final String PATTERN_10_NUMBERS = "[0-9]{0,10}";
    /** Regex to verify numbers */
    public static final String PATTERN_IS_DIGIT = "[0-9]*";

    /** Constant to integer value 31 */
    public static final int CONSTANT_31 = 31;

    /** Pattern Matcher to validate the possible channels */
    public static final String PATTERN_MATCHER_POSSIBLE_CHANNELS_HYPHEN = "^(\\d+)-(\\d+)$";

    /** Pattern Matcher to validate the possible channels */
    public static final String PATTERN_MATCHER_POSSIBLE_CHANNELS_COMMA = "^[(\\d+),]+$";

    /** Pattern Matcher to validate the Operating frequency */
    public static final String PATTERN_MATCHER_OPERATING_FREQUENCY = "^(\\d+)MHz$";

    /** The array list of possible extension channel values */
    public static final List<String> EXTENSION_CHANNEL_VALUES = new ArrayList<String>() {
	{
	    add("AboveControlChannel");
	    add("BelowControlChannel");
	    add("Auto");
	}
    };

    /** Default Operational Tx Rate for 2.4GHz g/n mode. */
    public static final String OPERATION_TX_RATE_G_N = "1,2,5.5,11,6,9,12,18,24,36,48,54";

    /** Default Operational Tx Rate for 2.4GHz g/n mode. */
    public static final String OPERATION_TX_RATE_G_N_2GHZ = "6,9,12,18,24,36,48,54,6.5,7,13,14,19.5,21,26,28,39,43,52,57,58.5,65,72,78,86,104,115,117,130,144,97.5,108";

    /** Supported transmission rates for 2.4GHz particular arm based device */
    public static final String SUPPORTED_TX_RATE_2GHZ = "6,9,12,18,24,36,48,54,6.5,7,13,14,19.5,21,26,28,39,43,52,57,58.5,65,72,78,86,104,115,117,130,"
	    + "144,97.5,108,13.5,15,27,30,40.5,45,60,81,90,120,121.5,135,150,162,180,216,240,243,270,300,324,360";

    /** Basic transmission rate for particular arm based device */
    public static final String BASIC_TX_RATE = "6";

    /** Test Constants for Wifi Tx Rate Configuration */
    /** Constant for b/g/n Operating Standard */
    public static final String WIFI_OPERATING_STANDARD_B_G_N = "b,g,n";

    /** String for verify operation mode on wireless basic settings */
    public static final String G_N_AX_MODE_ON_WIRELESS_SETTINGS = "g,n,ax";

    /** Default Basic Tx Rate for 2.4GHz b/g/n mode. */
    public static final String BASIC_TX_RATE_B_G_N = "1,2,5.5,11,6,12,24";

    /** Temporary basic Tx rate */
    public static final String TEMP_BASIC_TX_RATE = "1,2,6";

    /** String value for Operating mode a/n/ac */
    public static final String OPERATING_MODE_ANAC = "a,n,ac";

    /** Supported transmission rates for 5GHz particular arm based device */
    public static final String SUPPORTED_DATATX_RATE_5GHZ = "6,9,12,18,24,36,48,54,6.5,7,13.5,15,13,14,27,30,19.5,21,40.5,45,26,28,60,39,"
	    + "43,81,90,52,57,108,120,58.5,65,121.5,135,72,150,78,86,162,180,104,115,216,240,117,130,243,270,144,300,156,173,324,360,175.5,195,364.5,"
	    + "405,450,208,231,432,480,234,260,486,540,288,600,97.5,202.5,225,91,101,189,210,136.5,151,283.5,315,143,158,297,330,214.5,238,445.5,495,"
	    + "29.5,32.5,88,263.5,292.5,325,351,390,0,433.5,59,42,176,56,114,468,520,527,585,650,172,702,780,867,88.5,63,264,84,129,526.5,171,790.5,"
	    + "877.5,975,258,1053,1170,1300.5,118,352,112,228,936,1040,1054,1300,648,720,312,344,1404,1560,1734";

    /** String value for Operating mode ac */
    public static final String OPERATING_MODE_AC = "ac";

    /** Kernel module name sound */
    public static final String MODULE_SOUND_KERNEL = "sound";

    /** Kernel module name libsven */
    public static final String MODULE_LIBSVEN = "libsven*";

    /** Kernal mdould name libmtp */
    public static final String MODULE_MTP_TOOL = "libmtp*";

    /** Constant to hold command for checking COSAXcalibur.XML file in the device */
    public static final String FILE_COSAXCALIBUR_XML = "COSAXcalibur.XML";

    /** String to store telemetry report success messgae string */
    public static final String STRING_TELEMETRY_REPORT_SUCCESS = "Report Sent Successfully over HTTP : 200";

    /** String to store BandSteeringEnabledTest telemetry profile header */
    public static final String BAND_STEERING_ENABLED_TEST = "BandSteeringEnabledTest";

    /** String to store TestInvalidParam_split telemetry profile header */
    public static final String TEST_INVALID_PARAM = "TestInvalidParam_split";

    /**
     * String to store pattern to get value of BandSteeringEnabledTest parameter in dcmscript.log
     */
    public static final String PATTERN_MATCHER_BANDSTEERING_TESTHEADER_VALUE = "BandSteeringEnabledTest\":\"(\\w+)\"";

    /** String to store TEST_MULTI_INSTANCE telemetry profile header */
    public static final String TEST_MULTI_INSTANCE = "TEST_MULTI_INSTANCE";

    /** String to store TestInvalidParam_Multi telemetry profile header */
    public static final String TEST_INVALID_PARAM_MULTI = "TestInvalidParam_Multi";

    /**
     * Test constant for applying Dns ovverride at client MAC level IPv4 null value 00.00.00.00
     */
    public static final String STRING_DNS_IPV4_NULL_VALUE = "00.00.00.00";

    /** Test constant for invalid value for mac address A:B:C:X:Y:Z */
    public static final String STRING_INVALID_VALUE_FOR_MAC_ADDRESS = "A:B:C:X:Y:Z";

    /** Test constant for subnet mask value 255.255.255.0 */
    public static final String STRING_DNS_IP_WITH_SUBNET_MASK = "255.255.255.0";

    /** Test constant for dns IP with network address 10.0.0.0 */
    public static final String STRING_DNS_IP_WITH_NETWORK_ADDRESS = "10.0.0.0";

    /** Test constant for dns IP with broadcast address 10.0.0.255 */
    public static final String STRING_DNS_IP_WITH_BROADCAST_ADDRESS = "10.0.0.255";

    /** Test constant for dns IP with null broadcast address 0.0.0.0 */
    public static final String STRING_DNS_IP_WITH_NULL_BROADCAST_ADDRESS = "0.0.0.0";

    /**
     * Test constant for applying Dns ovverride at client MAC level IPv4 level two primary value 75.75.75.20
     */
    public static final String STRING_DNS_IPV4_VALUE_FOR_DNS_LEVEL_TWO_PRIMARY = "75.75.75.20";

    /** String used for replacing the ipv4 value in other string */
    public static final String STRING_REPLACE_IPV4 = "<IPV4>";

    /** String used for replacing the ipv6 value in other string */
    public static final String STRING_REPLACE_IPV6 = "<IPV6>";

    /** String to store invalid DNS primary IPV4 value */
    public static final String STRING_DNS_INVALID_PRIMARY_IPV4_VALUE = "13.13.12.12";

    /** String to store invalid DNS primary IPV6 value */
    public static final String STRING_DNS_INVALID_PRIMARY_IPV6_VALUE = "2621:104:a00b::4";

    /** String to store invalid DNS Secondary IPV4 value */
    public static final String STRING_DNS_ANOTHER_INVALID_SECONDARY_IPV4_VALUE = "13.13.12.11";

    /** String to store invalid DNS Secondary IPV6 value */
    public static final String STRING_DNS_ANOTHER_INVALID_SECONDARY_IPV6_VALUE = "2621:104:a00b::6";

    /** String to store valid DNS IPV4 value */
    public static final String STRING_DNS_VALID_IPV4_VALUE = "74.121.125.54";

    /** String to store valid DNS IPV6 value */
    public static final String STRING_DNS_VALID_IPV6_VALUE = "2620:104:a00b::53";

    /** String to store invalid DNS Secondary IPV4 value */
    public static final String STRING_DNS_INVALID_SECONDARY_IPV4_VALUE = "12.12.12.12";

    /** String to store invalid DNS Secondary IPV6 value */
    public static final String STRING_DNS_INVALID_SECONDARY_IPV6_VALUE = "2620:104:a00b::4";

    /** String to store valid DNS Secondary IPV4 value */
    public static final String STRING_DNS_VALID_SECONDARY_IPV4_VALUE = "74.121.125.55";

    /** String to store valid DNS Secondary IPV6 value */
    public static final String STRING_DNS_VALID_SECONDARY_IPV6_VALUE = "2620:104:a00b::54";

    /** Test constant for dnsmasq_servers.conf file */
    public static final String DNSMASQ_CONFIGURATION_FILE = "/nvram/dnsmasq_servers.conf";

    /** The property key for the bash vulnerability file */
    public static final String PROP_KEY_SHELL_SHOCK_VULNERABILITY = "shell.shock.vulnerability";

    /** Command to list the files in nvram folder */
    public static final String CMD_LS_NVRAM = "ls /nvram/";

    /** String to store shell script name for Bash Test */
    public static final String SHELL_SHOCK_SCRIPT = "shellShockCheck.sh";

    /** Shellshockcheck.sh path **/
    public static final String FILE_PATH_1 = "cpefiles/generic/";

    /** Constant for sh command */
    public static final String CMD_SH = "/bin/sh";

    /** String to hold vulnerability text */
    public static final String STRING_BASH_IS_VULNERABLE = "Bash is vulnerable";

    /** String to hold bash test text */
    public static final String STRING_BASH_TEST = "Bash Test";

    /** Constant to hold private wifi 2.4GHZ index */

    public static final String WIFI_24_GHZ_INDEX = "10001";

    /** String for systemctl */
    public static final String COMMAND_SYSTEMCTL = "systemctl --all";

    /**
     * Test constant for applying Dns ovverride at gateway level IPv4 value 75.75.75.76
     */
    public static final String STRING_DNS_GATEWAY_LEVEL_OVERRIDE_IPV4_VALUE = "75.75.75.76";

    /** Test constant for level one site host address in stb.properties file */
    public static final String PROP_KEY_HOST_ADDRESS_FOR_LEVEL_ONE_SITE = "dnsoverride.siteaddress.level.one";

    /**
     * Test constant for dnsoverride level one block site address in stb.properties file
     */
    public static final String PROP_KEY_DNS_BLOCK_ADDRESS_FOR_LEVEL_ONE_SITE = "dnsoverride.blocksiteaddress.level.one";

    /**
     * Test constant for applying Dns ovverride at client MAC level IPv6 level two primary value 2001:558:feed::7520
     */
    public static final String STRING_DNS_IPV6_VALUE_FOR_DNS_LEVEL_TWO_PRIMARY = "2001:558:feed::7520";

    /** Test constant for level two site host address in stb.properties file */
    public static final String PROP_KEY_HOST_ADDRESS_FOR_LEVEL_TWO_SITE = "dnsoverride.siteaddress.level.two";

    /**
     * Test constant for dnsoverride level two block site address in stb.properties file
     */
    public static final String PROP_KEY_DNS_BLOCK_ADDRESS_FOR_LEVEL_TWO_SITE = "dnsoverride.blocksiteaddress.level.two";

    /** Test constant for level three site host address in stb.properties file */
    public static final String PROP_KEY_HOST_ADDRESS_FOR_LEVEL_THREE_SITE = "dnsoverride.siteaddress.level.three";

    /**
     * Test constant for dnsoverride level three block site address in stb.properties file
     */
    public static final String PROP_KEY_DNS_BLOCK_ADDRESS_FOR_LEVEL_THREE_SITE = "dnsoverride.blocksiteaddress.level.three";

    /** Constant to hold the Channel Number Place holder */
    public static final String CHANNEL_NUMBER_PLACE_HOLDER = "#CHANNEL#";

    /** Integer that holds the value 3600 */
    public static final int INTERGER_CONSTANT_3600 = 3600;

    /** Pattern for Ethernet client count */
    public static final String PATTERN_ETH_MAC_TOTAL_COUNT = "ETH_MAC_<REPLACE>_TOTAL_COUNT:(\\d+)";

    /** Pattern for Ethernet Mac address */
    public static final String PATTERN_ETH_MAC = "ETH_MAC_<REPLACE>:"
	    + BroadBandTestConstants.REG_EXPRESSION_TO_GET_MAC_ADDRESS_SEMICOLON;

    /** Pattern for Ethernet phy.rate */
    public static final String PATTERN_ETH_PHYRATE = "ETH_PHYRATE_<REPLACE>:(\\d+)";

    /** Pattern for 2.4GHz WiFi client count */
    public static final String PATTERN_WIFI_MAC_1_TOTAL_COUNT = "WIFI_MAC_1_TOTAL_COUNT:(\\d+)";

    /** File path for Consolelog.txt */
    public static final String LOGS_CONSOLE_TXT_0 = "/rdklogs/logs/Consolelog.txt.0";

    /** The constant for Failed Status */
    public static final String STATUS_FAILED = "Failed";

    /** Boot log file location */
    public static final String CAT_RDKLOGS_LOGS_BOOT_TIME_LOG = "cat /rdklogs/logs/BootTime.log";

    /** File path for /rdklogs/logs/BootTime.log */
    public static final String RDKLOGS_LOGS_BOOTTIME_LOG = "/rdklogs/logs/BootTime.log";

    public static final int BOOT_TO_MOCA_UP_MAX_VALUE = 400;

    /** Max time for wifi up time */
    public static final int BOOT_TO_WIFI_UP_MAX_VALUE = 400;

    /** Max time for lnfssid */
    public static final int BOOT_TO_LNF_SSID_UP_TIME_MAX_VALUE = 800;

    /** Max time for moca up time */
    public static final int BOOT_TO_ETH_UP_MAX_VALUE = 400;

    /** Max time for DHCP Operational time */
    public static final int CM_DHCP_OPERATIONAL_MAX_VALUE = 400;

    /** Max time for Downstream lock time */
    public static final int DOWNSTREAM_LOCK_MAX_VALUE = 150;

    /** Max time for snmp sub agent up time */
    public static final int BOOT_TO_SNMP_SUB_AGENT_UP_TIME_MAX_VALUE = 800;

    /** Max time for tr69 time */
    public static final int BOOT_TO_TR69_UP_TIME_MAX_VALUE = 900;

    /** Max time for wan up time */
    public static final int BOOT_TO_WAN_UP_TIME_MAX_VALUE = 800;

    /** Max time for webpa up time */
    public static final int BOOT_TO_WEBPA_UP_TIME_MAX_VALUE = 1002;

    /** Max time for DHCP end time */
    public static final int CM_DHCP_END_MAX_VALUE = 400;

    /** Max time for DHCP start time */
    public static final int CM_DHCP_START_MAX_VALUE = 200;

    /** File path for PARODUSlog.txt.0 */
    public static final String LOGS_PARODUS_TXT_0 = "/rdklogs/logs/PARODUSlog.txt.0";

    /** Constant to hold wait duration of 300 Seconds */
    public static final String THIRTY_SECONDS = "30";

    /** Constant to hold fifty seconds */
    public static final int FIFTY_SECONDS = 50;

    /** Multiplier to convert seconds to milliseconds */
    public static final int SECONDS_TO_MILLISECONDS = 1000;

    /** String constant value Channel No 11 */
    public static final String CHANNEL_NO_11 = "11";

    /** String constant value Channel No 161 */
    public static final String CHANNEL_NO_161 = "161";

    /** String to hold the default value of MFPConfig Values */
    public static final String DEFAULT_VALUE_MFPCONFIG_ACCESSPOINT = "Disabled";

    /** String to hold the Optional value of MFPConfig Values */
    public static final String OPTIONAL_VALUE_MFPCONFIG_ACCESSPOINT = "Optional";

    /** String to hold the Required value of MFPConfig Values */
    public static final String REQUIRED_VALUE_MFPCONFIG_ACCESSPOINT = "Required";

    /** File name for dcmrfc log file */
    public static final String FILE_BLE_LOG = "/rdklogs/logs/Blelog.txt.0";

    public static final String PATTERN_GET_BLE_RADIO_PARAMETER_FROM_RFC_CONFIG = "tr181.Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.WiFi-Interworking.Enable\\W+(\\w+)";

    public static final String PATTERN_GET_WEBPA_PARAM_BLE_DISCOVERY_FROM_RFC_CONFIG = "tr181.Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.BLE.Discovery\\W+(\\w+)";

    /** String regex to retrieve process ID */
    public static final String PATTERN_TO_CHECK_IPERF_PROCESS = ".*iperf3";

    /** Constant to hold " -t" for time parameter */
    public static final String TIME_PARAM = " -t";

    /** String regex to retrieve process ID */
    public static final String PATTERN_TO_RETRIEVE_PROCESS_ID = ".*\\s+(\\d{5}|\\d{4}|\\d{3})\\s+\\d+.*iperf3";

    /** String that stores url of google */
    public static final String URL_GOOGLE = "www.google.com";

    /** String value for 8 */
    public static final String STRING_VALUE_EIGHT = "8";

    /** constant to hold string value 63 */
    public static final String STRING_63 = "63";

    /** String value for 64 */
    public static final String STRING_VALUE_SIXTY_FOUR = "64";

    /** constant to hold string value 1471 */
    public static final String STRING_1471 = "1471";

    /** constant to hold string value 101 */
    public static final String STRING_101 = "101";

    /** String containing json value of WiFi GAS configuration to be set */
    public static final String STRING_GAS_CONFIG_JSON_VALUE = "{\"gasConfig\":[{\"AdvertisementId\": 0,\"pauseForServerResp\": true,\"respTimeout\": 5000,\"comebackDelay\": 1000,\"respBufferTime\": 1000,\"queryRespLengthLimit\": 90 }]}";

    /** String containing json value of WiFi GAS configuration to be set */
    public static final String STRING_GAS_CONFIG_DEFAULT_VALUE = "{\"gasConfig\": [{ \"advertId\": 0, \"pauseForServerResp\": true,  \"respTimeout\": 5000, \"comebackDelay\": 1000, \"respBufferTime\": 1000, \"queryRespLengthLimit\": 127 }]}";

    /** WiFi vAP list for private and XH */
    public static final String PRIVATE_XH_AP_LIST = "1,2";

    /** Pattern to get the wifi client mac address from wifi_api */
    public static final String PATTERN_CLI_MAC_ADDRSS = "cli_MACAddress\\s+=\\s";

    /** Pattern to find the maximum downlink rate */
    public static final String PATTERN_MAX_DOWNLINK_RATE = "cli_MaxDownlinkRate = (\\d+)";

    /** Pattern to find the maximum uplink rate */
    public static final String PATTERN_MAX_UPLINK_RATE = "cli_MaxUplinkRate = (\\d+)";

    /** pattern to get the max tx rate */
    public static final String PATTERN_MAX_TX_RATE = "Max Tx Rate:(\\d+)";

    /** pattern to get the max rx rate */
    public static final String PATTERN_MAX_RX_RATE = "Max Rx Rate:(\\d+)";

    /** Pattern to find the maximum TxRate for 5GHz Clients */
    public static final String PATTERN_MAX_TXCLIENTS_2 = "WIFI_MAX_TXCLIENTS_2:(\\d+),";

    /** Pattern to find the maximum RxRate for 5GHz Clients */
    public static final String PATTERN_MAX_RXCLIENTS_2 = "WIFI_MAX_RXCLIENTS_2:(\\d+),";

    /** Pattern to get 2.4GHz wifi client mac address */
    public static final String PATTERN_WIFI_MAC_1 = "WIFI_MAC_1:";

    /** Pattern to get 5GHz wifi client mac address */
    public static final String PATTERN_WIFI_MAC_2 = "WIFI_MAC_2:";

    /** Pattern to find the maximum TxRate for 2.4GHz Clients */
    public static final String PATTERN_MAX_TXCLIENTS_1 = "WIFI_MAX_TXCLIENTS_1:(\\d+),";

    /** Pattern to find the maximum RxRate for 2.4GHz Clients */
    public static final String PATTERN_MAX_RXCLIENTS_1 = "WIFI_MAX_RXCLIENTS_1:(\\d+),";

    /** String constant for Export */
    public static final String STRING_EXPORT = "export";

    /** String constant INVALID to set the invalid value for WebPA parameter */
    public static final String STRING_INVALID = "INVALID";

    /** String value for 7895 */
    public static final String STRING_VALUE_SEVEN_THOUSAND_EIGHT_HUNDRED_AND_NINETY_FIVE = "7895";

    /** String constant to store two thousand eight hundred eighty value */
    public static final String STRING_VALUE_TWO_THOUSAND_EIGHT_HUNDRED_EIGHTY = "2880";

    /** Linux command to copy files */
    public static final String CMD_COPY_FILES = "cp";

    /** Constant to hold low level site blocker */
    public static final String LOW_LEVEL_SITE_BLOCKER = "LOW_LEVEL_SITE_BLOCKER";

    /** Constant to hold medium level site blocker */
    public static final String MEDIUM_LEVEL_SITE_BLOCKER = "MEDIUM_LEVEL_SITE_BLOCKER";

    /** Constant to hold high level site blocker */
    public static final String HIGH_LEVEL_SITE_BLOCKER = "HIGH_LEVEL_SITE_BLOCKER";

    /** Constant to hold low level blocked site */
    public static final String LOW_LEVEL_SITE = "LOW_LEVEL_SITE";

    /** Constant to hold medium level blocked site */
    public static final String MEDIUM_LEVEL_SITE = "MEDIUM_LEVEL_SITE";

    /** Constant to hold high level blocked site */
    public static final String HIGH_LEVEL_SITE = "HIGH_LEVEL_SITE";

    /**
     * The hash map to retrieve the the level with its bloecked sites
     */
    public static final Map<String, String> RESTRICTED_SITE_MAPPING = getRestrictedSites();

    /**
     * Method to create a map to store the blocked sites
     * 
     * @return map with level as key and its blocked site as value
     */
    private static Map<String, String> getRestrictedSites() {
	Map<String, String> mapping = new HashMap<String, String>() {
	    {
		put(LOW_LEVEL_SITE, AutomaticsPropertyUtility
			.getProperty(BroadBandTestConstants.PROP_KEY_HOST_ADDRESS_FOR_LEVEL_ONE_SITE));
		put(MEDIUM_LEVEL_SITE, AutomaticsPropertyUtility
			.getProperty(BroadBandTestConstants.PROP_KEY_HOST_ADDRESS_FOR_LEVEL_TWO_SITE));
		put(HIGH_LEVEL_SITE, AutomaticsPropertyUtility
			.getProperty(BroadBandTestConstants.PROP_KEY_HOST_ADDRESS_FOR_LEVEL_THREE_SITE));
	    }
	};
	return Collections.unmodifiableMap(mapping);
    }

    /**
     * The hash map to retrieve the the level with its site blocker
     */
    public static final Map<String, String> DNS_SITE_BLOCKER_MAPPING = getDnsSiteBlocker();

    /**
     * Method to create a map to store the site blocker
     * 
     * @return map with level as key and its site blocker as value
     */
    private static Map<String, String> getDnsSiteBlocker() {
	Map<String, String> mapping = new HashMap<String, String>() {
	    {
		put(LOW_LEVEL_SITE_BLOCKER, AutomaticsPropertyUtility
			.getProperty(BroadBandTestConstants.PROP_KEY_DNS_BLOCK_ADDRESS_FOR_LEVEL_ONE_SITE));
		put(MEDIUM_LEVEL_SITE_BLOCKER, AutomaticsPropertyUtility
			.getProperty(BroadBandTestConstants.PROP_KEY_DNS_BLOCK_ADDRESS_FOR_LEVEL_TWO_SITE));
		put(HIGH_LEVEL_SITE_BLOCKER, AutomaticsPropertyUtility
			.getProperty(BroadBandTestConstants.PROP_KEY_DNS_BLOCK_ADDRESS_FOR_LEVEL_THREE_SITE));
	    }
	};
	return Collections.unmodifiableMap(mapping);
    }

    /** Constant to hold site www */
    public static final String SITE_WWWW = "www";

    /** Pattern for getting the blocked site name. */
    public static final String PATTERN_SITE_NAME = "Name:\\s*(\\S+)";

    /** Constant to hold 2.4GHZ index */
    public static final int TWO_FOUR_GHZ_INDEX = 10000;

    /** Constant to hold 5GHZ index */
    public static final int FIVE_GHZ_INDEX = 10100;

    /** Constant to hold Object Not found error */
    public static final String OBJECT_NOT_FOUND_ERROR = "Object Not Found!";

    /** String to hold value - tcpdump */
    public static final String TCPDUMP = "tcpdump";

    /** string to hold ampersand */
    public static final String CONSTANT_STRING_AMPERSAND = "&";

    /** Command to get upnp version */
    public static final String CMD_GET_UPNP_VERSION = "/usr/sbin/miniupnpd --version";

    /** Pattern to get upnp version */
    public static final String PATTERN_GET_UPNP_VERSION = "miniupnpd (\\d+.\\d+)";

    /** String Restarting process CcspXdnsSsp in systemd_processRestart.log */
    public static final String TEXT_RESTARTING_CCSPXDNSSSP = "Stopping/Restarting CcspXdnsSsp";

    /** String Resetting process CcspXdnsSsp in SelfHeal.txt.0 */
    public static final String TEXT_RESETTING_CCSPXDNSSSP = "Resetting process CcspXdnsSsp";

    /** Sample string with 2000 character */
    public static final String SAMPLE_STRING_WITH_2000_CHARACTER = "String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request String used to authenticate the Speed Test request Stri";

    /** Constant to hold the string value Block */
    public static final String PARENTAL_CONTROL_MANAGE_DEVICE_RULE_TYPE = "Block";

    /** Connection Type Wifi 5 Ghz */
    public static final String CONNECTION_TYPE_WIFI_5_GHZ = "Wi-Fi_5G";

    /** Connection Type Wifi 2.4 Ghz */
    public static final String CONNECTION_TYPE_WIFI_24_GHZ = "Wi-Fi_2.4G";

    /** String to pass in searchlogfiles to retrieve crash portal log file */
    public static final String STRING_FOR_CRASH_PORTAL_FILE_NAME_SEARCH = "Removing file";

    /** String regex wild card (.*) */
    public static final String STRING_REGEX_WILDCARD = "(.*)";

    /** String constant to hold public wifi secured */
    public static final String SECURED_WIFI_PUBLIC = "Secured Public Wifi";

    /** To verify dump upload to vbn crash portal succeeded */
    public static final String FAIL_OVER_MECHANISM_VBN_SUCCESS = "Success uploading.+file:\\s*((\\w+_)?mac+\\w+_dat+\\d+-\\d+-\\d+-\\d+-\\d+-\\d+_box+\\w+_mod+\\w+.*(.dmp.tgz|.core.tgz)\\sto\\svbn.crashportal.ccp.xcal.tv)";

    /** String CR_crash */
    public static final String STRING_CR_CRASH = "CR_crash";

    /** Constant to integer value 17 */
    public static final int CONSTANT_17 = 17;

    /** String for Backhaul wifi 24ghz ssid */
    public static final String STRING_LNF_SSID_24GHZ = "LNF_SSID_24Ghz";

    /** String for Backhaul wifi 5ghz ssid */
    public static final String STRING_LNF_SSID_5GHZ = "LNF_SSID_5Ghz";

    /** Reg ex to get the mac address */
    public static final String REG_EX_MAC_ADDRESS_FORMAT = "\\w+:\\w+:\\w+:\\w+:\\w+:\\w+";

    /** String constant to hold the status Not Processed */
    public static final String NOT_PROCESSED = "Not Processed";

    /** String constant to hold wifi private */
    public static final String WIFI_PRIVATE = "Private Wifi";

    /** Constant to integer value 33 */
    public static final int CONSTANT_33 = 33;

    /** Constant to integer value 41 */
    public static final int CONSTANT_41 = 41;

    /** String for Rfc properties to post - Replace with data payload */
    public static final String STRING_RFC_DATA_PAYLOAD = "{\"estbMacAddress\":\"ESTB_MAC_ADDRESS\",\"features\":[{\"name\":\"EncryptCloudData\",\"effectiveImmediate\":true,\"enable\":true,\"configData\":<REPLACE>}]}\r\n";

    /** Operating Standards for 5GHz a mode. */
    public static final String OPERATING_STANDARDS_A = "a";

    /** Operating Standards for 2GHz b mode. */
    public static final String OPERATING_STANDARDS_B = "b";

    /** Operating Standards for 2GHz g mode. */
    public static final String OPERATING_STANDARDS_G = "g";

    /** Constant to hold WiFi Band 5.0 Constant */
    public static final String WIFI_BAND_5_0 = "5.0";

    /** Constant for 2.4GHZ band */
    public static final String STRING_WIFI_BAND_2_4 = "2.4";

    /** Pattern to match Crontab time interval */
    public static final String PATTERN_TO_FETCH_CRONTAB_TIME_INTERVAL_FOR_1HR = "0\\s+(1,(\\d+,)+\\d+)(\\s*){3}\\s+";

    /** Constant value for crontab 4hours time interval */
    public static final String FOUR_HRS_TIME_INTERVAL = "0,4,8,12,16,20";

    /** Constant value for crontab 2hours time interval */
    public static final String ONE_HRS_TIME_INTERVAL = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23";

    /** Pattern to fetch host zones from cpu memory fragmentation details */
    public static final String PATTERN_TO_FETCH_HOST_ZONES_CPU_MEMFRAG = "PROC_BUDDYINFO_HOST:CPU_MEM_FRAG-(\\w+)";

    /** Pattern to fetch host values from cpu memory fragmentation details */
    public static final String PATTERN_TO_FETCH_HOST_VALUES_CPU_MEMFRAG = "PROC_BUDDYINFO_HOST:CPU_MEM_FRAG-\\w+,([\\d+,]{0,}\\d+)";

    /** Pattern to fetch peer zones from cpu memory fragmentation details */
    public static final String PATTERN_TO_FETCH_PEER_ZONES_CPU_MEMFRAG = "PROC_BUDDYINFO_PEER:CPU_MEM_FRAG-(\\w+)";

    /** Pattern to fetch peer values from cpu memory fragmentation details */
    public static final String PATTERN_TO_FETCH_PEER_VALUES_CPU_MEMFRAG = "PROC_BUDDYINFO_PEER:CPU_MEM_FRAG-\\w+,([\\d+,]{0,}\\d+)";

    /** Pattern to check cpu memory fragmentation format */
    public static final String PATTERN_TO_CHECK_CPU_FRAGMENTATION_FORMAT = "(\\d{6}-\\d{2}:\\d{2}:\\d{2}.\\d+\\sPROC_BUDDYINFO_(\\w+):CPU_MEM_FRAG-(\\w+)(,\\d+)+)";

    /** Pattern to check buddy info details */
    public static final String PATTERN_TO_FETCH_BUDDYINFO = "Node \\d+, zone\\s+(\\w+)";

    /** qtn_hal Log File Name */
    public static final String LOG_FILE_QTN_HAL = "/rdklogs/logs/qtn_hal.log";

    /** qtn_system_snapshot Log File Name */
    public static final String LOG_FILE_QTN_SYSTEM_SNAPSHOT = "/rdklogs/logs/qtn_system_snapshot.log";

    /** Integer value 2 */
    public static final Integer INTEGER_VALUE_2 = 2;

    /** MoCA Log File Name */
    public static final String LOG_FILE_MOCA_TEXT = "/rdklogs/logs/MOCAlog.txt.0";

    /** Pattern to extract passphrase from MoCA log file */
    public static final String PATTERN_TO_EXTRACT_PASSPHRASE_FROM_MOCA_LOG = "KeyPassphrase pString:\\s*(.*)";

    /** Constant to hold signature */
    public static final String SIGNATURE = "signature";

    /** Constant to hold curl */
    public static final String CURL = "curl";

    /** Constant to hold KeyPassphrase */
    public static final String KEY_PASSPHRASE = "KeyPassphrase";

    /** Constant for number 10001 */
    public static final int CONSTANT_10001 = 10001;

    /** Constant for number 10001 */
    public static final int CONSTANT_10101 = 10101;

    /** Constant for number 10001 */
    public static final int CONSTANT_10002 = 10002;

    /** Constant for number 10001 */
    public static final int CONSTANT_10102 = 10102;

    /** Constant for number 10001 */
    public static final int CONSTANT_10003 = 10003;

    /** Constant for number 10001 */
    public static final int CONSTANT_10103 = 10103;

    /** Constant to integer value 29 */
    public static final int CONSTANT_29 = 29;

    /** Constant to integer value 36 */
    public static final int CONSTANT_36 = 36;

    /** String value for 128 */
    public static final String STRING_VALUE_ONE_HUNDRED_AND_TWENTY_EIGHT = "128";

    /** String value for 148 */
    public static final String STRING_VALUE_ONE_HUNDRED_AND_FORTY_EIGHT = "148";

    public static final String FILE_NAME_WIFILOG = "WiFilog.txt.0";

    /** Constant to hold second node reference */
    public static final String SECONDARY_NODE_REF = "{j}";

    /** Constant to hold snmpv3 execution error */
    public static final String SNMPV3_EXECUTION_ERROR = "snmpv3 execution error has occurred";

    /** Pattern to validate the Mac address */
    public static final String PATTERN_TO_VALIDATE_MAC_ADDRESS = "(\\w+:|-\\w+:|-\\w+:|-\\w+:|-\\w+:|-\\w+)";

    /** Constant to hold the SNMP response as General failure Value */
    public static final String GENERAL_FAILURE_VALUE = "genError";

    /** Constant to hold the SNMP response as Bad failure Value */
    public static final String BAD_FAILURE_VALUE = "badValue";

    /**
     * The array list of Applicable Values for InterfaceDevicesWifi InterfaceDevicesWifiExtender NetworkDevicesStatus
     * Harvester Parameters
     */
    public static final List<String> APPLICABLE_VALUES_FOR_HARVESTER_DEVICE_WIFI_PARAMETERS = new ArrayList<String>() {
	{
	    add("5");
	    add("10");
	    add("15");
	    add("30");
	    add("60");
	    add("300");
	    add("900");
	    add("1800");
	    add("3600");
	    add("10800");
	    add("21600");
	    add("43200");
	    add("86400");
	}
    };

    /**
     * The hash map to store the bridge mode status mapping with corresponding Integer value retrieved using SNMP.
     */
    public static final HashMap<String, String> BRIDGE_MODE_VALUE_FROM_SNMP_MAPPING = new HashMap<String, String>() {
	{
	    put("router", "2");
	    put("bridge-static", "1");

	}
    };

    /**
     * The hash map to store the WIFI radio operating standards mapping with corresponding Integer value retrieved using
     * SNMP.
     */
    public static final HashMap<String, String> WIFI_MODE_VALUE_FROM_SNMP_MAPPING = new HashMap<String, String>() {
	{

	    put("g,n", "160");
	    put("a,n,ac", "448");
	    put("a,n", "192");
	    put("g,n,ax", "672");
	    put("a,n,ac,ax", "960");
	}
    };

    /** Constant for number 900 */
    public static final int CONSTANT_900 = 900;

    /** Server url for single client report */
    public static final String SINGLE_CLIENT_REPORT_SERVER_URL = "raw.kestrel.reports.WifiSingleClient";

    /** String to store vap with colon */
    public static final String PRIVATE_VAP_COLON = "vap:";

    /** Incorrect schema name for single client report */
    public static final String INCORRECT_SCHEMA_NAME = "singleClient.avsc";

    /** Telemetry marker for Test telemetry tag - configurable tag */
    public static final String TELEMETRY_MARKER_TEST_TELEMETRY_TAG = "Test_Telemetry_Tag";

    /** String constant to store text with 260 characters */
    public static final String STRING_WITH_260_CHAR_LENGTH = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    /** String constant 901 */
    public static final String STRING_CONSTANT_901 = "901";

    /** Common pattern for Integer */
    public static final String COMMON_PATTERN_FOR_INTEGER = "\\d+";

    /** Constant represent Pipe Symbol with Escape Sequence **/
    public static final String PIPE_SYMBOL_WITH_ESCAPE_SEQUENCE = "\\|";

    /** String to remove wifi band */
    public static final String REGEX_MODEL_FOR_REMOVING_WIFIBAND = "(\\S+)-2.4Ghz";

    /** String to hold place holder for MAC Address in property file */
    public static final String PLACE_HOLDER_FOR_MAC_ADDRESS = "#HOST_MAC_ADDRESS#";

    /** Constant to Hold header message in captive portal page */
    public static final String CAPTIVE_PORTAL_CONFIGURATION_PAGE_HEADER_MESSAGE = "Create Your Wi-Fi Name & Password";

    /** Pattern to validate the query param for CURL Post */
    public static final String PATTERN_CURL_POST_QUERY_PARAM = "-d\\s+(.*?)(partnerId=.*)(https:\\/\\/)&{0,1}";

    /** Pattern to validate the query param for CURL Get */
    public static final String PATTERN_CURL_GET_QUERY_PARAM = "https:\\/\\/(.*)(partnerId=.*)&{0,1}";

    /** Pattern to validate the query param for CURL Get after RFC Enable */
    public static final String PATTERN_CURL_GET_QUERY_PARAM_AFTER_RFC = "&partnerId=(\\S+)";

    /** Command to get process crash logs */
    public static final String COMMAND_TO_GET_SYSTEMD_PROCESS_CRASH_LOGS = "cat /rdklogs/logs/systemd_processRestart.log";

    /** String to store telemetry logs for PSM process crash */
    public static final String REGEX_VENDOR_NAME_PROCESS_CRASH_LOGS = "CABLEMODEM\\[(.*)\\]";

    /** String to store telemetry logs for PSM process crash */
    public static final String REGEX_MODEL_NAME_PROCESS_CRASH_LOGS = "<ecmMac><(.*)>";

    /** String to store telemetry logs for CR process crash */
    public static final String CR_PROCESS_CRASH_TELEMETRY_LOGS = "RDKB_PROCESS_CRASHED : CR_process is not running, need to reboot the unit";

    /** Command to get systemd logs for process crash */
    public static final String COMMAND_TO_GET_SYSTEMD_LOGS_FOR_PROCESS_CRASH = "rm -rf /nvram/automation_sample.txt;tail -F /rdklogs/logs/systemd_processRestart.log > /nvram/automation_sample.txt";

    /** TELEMETRY STATUS STRING 'sendHttpRequestToServer returned 0' */
    public static final String TELEMETRY_STATUS_STRING = "sendHttpRequestToServer returned 0";

    public static final String REBOOT_REASON_CRASH = "\"rdkb_rebootreason_split\":\"CR_crash\"";

    /** String to store telemetry logs for PSM process crash */
    public static final String PSM_PROCESS_CRASH_TELEMETRY_LOGS = "RDKB_PROCESS_CRASHED : PSM_process is not running";

    /** String to store telemetry logs for PAM process crash */
    public static final String PAM_PROCESS_CRASH_TELEMETRY_LOGS = "RDKB_PROCESS_CRASHED : PAM_process is not running";

    /** String to store telemetry logs for TR069 process crash */
    public static final String TR069_PROCESS_CRASH_TELEMETRY_LOGS = "RDKB_PROCESS_CRASHED : TR69_process is not running";

    /** Constant to get Uploaded Log file name Pattern Matcher */
    public static final String LOG_UPLOAD_PATTERN_MATCHER = "Logs_(.*)";

    /** Constant to hold Uploaded Log file name with PM Pattern Matcher */
    public static final String LOG_UPLOAD_FILE_NAME_WITH_PM_PATTERN_MATCHER = "(\\d+-\\d+-\\d+-\\d+-\\d+PM)";

    /** Constant to get hours value from Uploaded Log file Pattern Matcher */
    public static final String GET_HOURS_FROM_LOG_UPLOAD_FILE_NAME_PATTERN_MATCHER = "\\d+-\\d+-\\d+-(\\d+)-\\d+";

    /** Constant to get Minutes value from Uploaded Log file Pattern Matcher */
    public static final String GET_MINUTES_FROM_LOG_UPLOAD_FILE_NAME_PATTERN_MATCHER = "\\d+-\\d+-\\d+-\\d+-(\\d+)";

    /** Constant to get hours value from uptime value Pattern Matcher */
    public static final String GET_HOURS_FROM_UPTIME_VALUE_PATTERN_MATCHER = "(\\d+):\\d+:\\d+";

    /** Constant to get minutes value from uptime value Pattern Matcher */
    public static final String GET_MINUTES_FROM_UPTIME_VALUE_PATTERN_MATCHER = "\\d+:(\\d+):\\d+";

    /** Constant to get seconds value from uptime value Pattern Matcher */
    public static final String GET_SECONDS_FROM_UPTIME_VALUE_PATTERN_MATCHER = "\\d+:\\d+:(\\d+)";

    /** Constant for number 1800 */
    public static final int CONSTANT_1800 = 1800;

    /**
     * Log Message for Received Upstream Event Radion Interface Statistics Report
     */
    public static final String LOG_MESSAGE_RECEIVED_UPSTREAM_EVENT_NETWORK_TRAFFIC_REPORT = ".*raw.kestrel.reports.NetworkDevicesTraffic";

    /** Pattern for Log NETWORK_TRAFFIC_REPORT Message */
    public static final String PATTERN_FOR_NETWORK_TRAFFIC_REPORT = "Received upstream event data: dest '.*raw.kestrel.reports.NetworkDevicesTraffic'";

    /** default value of polling period for network device status in seconds */
    public static final int DEFAULT_VALUE_HARV_NETWORK_DEVICE_STATUS_POLLING_PERIOD = 900;

    /** default value of reporting period for network device status in seconds */
    public static final int DEFAULT_VALUE_HARV_NETWORK_DEVICE_STATUS_REPORTING_PERIOD = 900;

    /** default value of TTL period for network device status in seconds */
    public static final int DEFAULT_VALUE_HARV_NETWORK_DEVICE_STATUS_TTL_PERIOD = 300;

    /**
     * value of reporting period/polling period to be set for network device status in seconds
     */
    public static final String VALUE_HARV_NETWORK_DEVICE_STATUS_REPORTING_POLLING_PERIOD = "30";

    /** Schema name for single client report */
    public static final String SINGLE_CLIENT_REPORT_SCHEMA = "WifiSingleClient.avsc";

    /** Default mac address for single client harvester report */
    public static final String DEFAULT_MAC_ADDRESS = "000000000000";

    /** The constant for Not Triggered Status */
    public static final String STATUS_NOT_TRIGGERED = "Not triggered";

    /** The constant for Triggered Status */
    public static final String STATUS_TRIGGERED = "Triggered";

    /** The constant for In Progress Status */
    public static final String STATUS_INPROGRESS = "In Progress";

    /** The constant for Complete Status */
    public static final String STATUS_COMPLETE = "Complete";

    /** Pattern Matcher to retrieve the upload status */
    public static final String PATTERN_MATCHER_UPLOAD_STATUS = "(.*)\\s(Mon|Tue|Wed|Thu|Fri|Sat|Sun)";

    /** Pattern Matcher to retrieve the dns name for ipv4 loopback */
    public static final String PATTERN_MATCHER_TO_GET_UPLOAD_LOG_TIME = ".*((Mon|Tue|Wed|Thu|Fri|Sat|Sun).*)";

    /** The constant for UTC TimeZone Format */
    public static final String FORMAT_TIMEZONE_UTC = "E MMM dd HH:mm:ss zzz yyyy";

    /**
     * Pattern matcher to retrieve Log File name to be Uploaded in ArmConsole
     */
    public static final String COMMAND_TO_RETRIEVE_LOG_FILE_NAME_ARM_CONSOLE = "grep -i \"File to be uploaded:\" /rdklogs/logs/ArmConsolelog.txt.0 | tail -1";

    /**
     * Pattern matcher to retrieve Log File name to be Uploaded in Consolelog.txt.0
     */
    public static final String COMMAND_TO_RETRIEVE_LOG_FILE_NAME_CONSOLE_LOG = "grep -i \"File to be uploaded:\" /rdklogs/logs/Consolelog.txt.0 | tail -1";

    /** Pattern Matcher to retrieve the timestamp from log message */
    public static final String PATTERN_MATCHER_LOG_FILENAME = "uploaded: (.*)";

    /** The constant to add the dns names for blocking the Log upload */
    public static final String COMMAND_TO_ADD_DNS_NAMES = "echo \"::1   ccp-stbloglanding2.s3.dualstack.us-east-1.amazonaws.com\" >> /etc/hosts;echo \"127.0.0.1 ccp-stbloglanding2.s3.dualstack.us-east-1.amazonaws.com\" >> /etc/hosts";

    /** The constant to open the the Hosts file */
    public static final String COMMAND_TO_GREP_DNS_NAMES = "grep -i \"ccp-stbloglanding2.s3.dualstack.us-east-1.amazonaws.com\" /etc/hosts";

    /** The constant to remove the dns names which blocks the Log upload */
    public static final String COMMAND_TO_REMOVE_DNS_NAMES = "sed '/ccp-stbloglanding2.s3.dualstack.us-east-1.amazonaws.com/d' /etc/hosts > /tmp/hosts.tmp";

    /** The constant holds the DNS name for IPv6 Loopback */
    public static final String DNS_NAME_FOR_IPV6_LOOPBACK = "::1   ccp-stbloglanding2.s3.dualstack.us-east-1.amazonaws.com";

    /** The constant holds the DNS name for IPv4 Loopback */
    public static final String DNS_NAME_FOR_IPV4_LOOPBACK = "127.0.0.1 ccp-stbloglanding2.s3.dualstack.us-east-1.amazonaws.com";

    /** The constant to copy the host file content from temporary file */
    public static final String COPY_TMP_TO_HOSTS_FILE = "cat /tmp/hosts.tmp > /etc/hosts";

    /** Pattern Matcher to retrieve the dns name for ipv6 loopback */
    public static final String PATTERN_MATCHER_DNS_NAME_FOR_IPV6_LOOPBACK = "(::1.*)";

    /** Pattern Matcher to retrieve the dns name for ipv4 loopback */
    public static final String PATTERN_MATCHER_DNS_NAME_FOR_IPV4_LOOPBACK = "(127.*)";

    /** Integer value 11 */
    public static final Integer INTEGER_VALUE_11 = 11;

    /** Command to get wifi reset logs from pamlog file */
    public static final String CMD_GET_WIFI_RESET_LOG_FROM_PAMLOG_FILE = "tail -F /rdklogs/logs/PAMlog.txt.0 > /nvram/automation_sample.txt";

    /** Command to cat sample text file */
    public static final String CAT_SAMPLE_TEXT_FILE = "cat /nvram/automation_sample.txt";

    /** String to store wifi module reset logs */
    public static final String STRING_WIFI_MODULE_RESET_LOGS = "RebootDevice:WiFi is going to reboot now";

    /** Pattern to retrieve first 2 digits of IPv4 address */
    public static final String PATTERN_TO_RETRIEVE_FIRST_2_DIGITS_OF_IPv4_ADDRESS = "((\\d+\\.){2})\\d+\\.\\d+";

    /** The constant holds the dcm server url property name */
    public static final String DCM_RFC_SERVER_URL_PROPERTY_NAME = "\"DCM_RFC_SERVER_URL\"";

    /** Regular expression to grep server url */
    public static final String REG_EXPRESSION_DCM_SERVER_URL = "DCM_RFC_SERVER_URL=(\\w+.*)";

    /** Constant to hold dmcli command of RABIDFRAMEWORK_ENABLE */
    public static final String RABID_FRAMEWORK_ENABLE = "tr181.Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.RabidFramework.Enable";

    /** String for featureControl */
    public static final String STRING_FEATURECONTROL = "featureControl";

    /** String for feature */
    public static final String STRING_FEATURES = "features";

    /** String for configData */
    public static final String STRING_CONFIG_DATA = "configData";

    /** RFC param String */
    public static final String RFC_PARAM = "\"RFC: For param";

    /** RFC same value message */
    public static final String RFC_SAME_VALUE_MESSAGE = "new and old values are same value";

    /** RFC updated param String */
    public static final String RFC_UPDATED_PARAM = "\"RFC:  updated for";

    /** RFC updated param String */
    public static final String RFC_SET_PARAM = "\"RFC: dmcli SET called for RFC namespace param: ";

    /**
     * String to store pattern matcher for access point for wifi
     */
    public static final String PATTER_MATCHER_FOR_WIFI_ACCESS_POINT = "Device.WiFi.AccessPoint.(\\d+).";

    /**
     * String to store pattern matcher for wifi radio
     */
    public static final String PATTER_MATCHER_FOR_WIFI_RADIO = "Device.WiFi.Radio.(\\d+).";

    /** Holds error message when curl is not installed */
    public static final String PATTERN_TO_RETRIVE_IP_ADDRESS_FROM_NSLOOKUP_RESPONSE = ".*Address\\s+\\d+:\\s+(\\d+\\.\\d+\\.\\d+\\.\\d+)\\s+(.*)";

    /**
     * The hash map to store the wifi radio Access point mapping with corresponding digits
     */
    public static final Map<String, String> WIFI_RADIO_MAPPING = wifiRadiomapping();

    /**
     * Method to create a map to store the Wifi Radio mapping with corresponding digits.
     * 
     * @return map with wifi radio access points.
     */
    private static Map<String, String> wifiRadiomapping() {
	Map<String, String> mapping = new HashMap<String, String>() {
	    {
		put("1", "10000");
		put("2", "10100");

	    }
	};

	return Collections.unmodifiableMap(mapping);
    }

    /** Constant to hold string for Maximum Associated Devices */
    public static final String STRING_MAX_ASSOCIATED_DEVICES = "MaxAssociatedDevices";

    /** Linux command to do stbssh to estb ipv6. */
    public static final String LINUX_CMD_SUDO_STB_SSH_IPV6 = "sudo -S stbsshv6";

    /** constant for wifi Invalid password */
    public static final String INVALID_WIFI_PASSWORD = "Invalid123!";

    /** Constant to hold URL of HTTP site: http://www.facebook.com */
    public static final String URL_HTTP_FACEBOOK = "http://www.facebook.com/";

    /** Interface for ATOM SYNC PRIVATE__2 GHZ */
    public static final String INTERFACE_ATOM_PRIVATE_2 = "wl -i wl0 status";

    /** Interface for ATOM SYNC PRIVATE_5 GHZ */
    public static final String INTERFACE_ATOM_PRIVATE_5 = "wl -i wl1 status";

    /** Constant to hold regex to grep bssid */
    public static final String REGEX_GREP_BSSID = ":\\s(\\w+:\\w+:\\w+:\\w+:\\w+:\\w+)";

    /**
     * String variable to store test_connection which is used to test ssh connectivity
     */
    public static final String STRING_TEST_CONNECTION = "test_connection";

    /** Constant to hold command to get systeminfo_wireless */
    public static final String CMD_GET_SYSTEM_INFO_WIRELESS = "systeminfo |grep -i \"wireless\"";

    /** Constant to hold Intel */
    public static final String WIFI_DRIVER_INTEL = "Intel";

    /** The property key for device status. */
    public static final String SPECIFIC_PLATFORM = "specific.platform.";

    /** Constant to hold string 24 */
    public static final String STRING_24_WIFI_SSID_PREFIX = "24_";

    /** Constant to hold string 5 */
    public static final String STRING_5_WIFI_SSID_PREFIX = "5_";

    /** Constant to hold single under score character */
    public static final String CHARACTER_UNDER_SCORE = "_";

    /** Constant to hold character Colon. */
    public static final String COLON = ":";

    /** Constant to hold command to get band preference */
    public static final String COMMAND_GET_WIRELESS_MODE = "PowerShell.exe -command Get-NetAdapterAdvancedProperty -RegistryKeyword \"WirelessMode\"";

    /** Constant to hold command to set band preference */
    public static final String COMMAND_SET_WIRELESS_MODE = "PowerShell.exe -command Set-NetAdapterAdvancedProperty -RegistryKeyword \"WirelessMode\" -RegistryValue \"<REPLACE>\"";

    /** String to store nodev mount option */
    public static final String MOUNT_OPTION_NODEV = "nodev";

    /** String to store nosuid mount option */
    public static final String MOUNT_OPTION_NOSUID = "nosuid";

    /** String to store noexec mount option */
    public static final String MOUNT_OPTION_NOEXEC = "noexec";

    /** String variable to check atom accessibility */
    public static final String STRING_VERIFY_ATOM_ACCESSIBILITY = "Atom is accessible from Arm";

    /** The property key to obtain Atom console IP for specific devices. */
    public static final String ATOM_CONSOLE_IP_SPECIFIC_DEVICES = "ip.atom.console.";

    /** String constant to hold Non Default Login Password string */
    public static final String NON_DEFAULT_LOGIN_PASSWORD = "password1";

    /** Constant for HTTP URL */
    public static final String URL_HTTP = "http://";

    /** Constant to hold Gateway admin page SECURE UI DOMAIN */
    public static final String GATEWAY_ADMIN_PAGE_SECURE_UI_DOMAIN = "myrouter.io";

    /** Constant holds a password with proper standards */
    public static final String PASSWORD_WITH_LETTERS_AND_NUMBERS = "testpassword123";

    /** Constant holds the error message while password mismatch */
    public static final String PASSWORD_MISMATCH_ERROR_MESSAGE = "Please enter the same value again.";

    /** wait for 3 seconds */
    public static final long THREE_SECOND_IN_MILLIS = 3000L;

    /** Constant holds the error message while setting a password as blank */
    public static final String PASSWORD_BLANK_FIELD_ERROR_MESSAGE = "This is a required field.";

    /**
     * Constant holds the error message for special characters/space in a password
     */
    public static final String PASSWORD_SPECIAL_CHARACTER_AND_SPACE_ERROR_MESSAGE = "Only letters and numbers are valid. No spaces or special characters.";

    /** Static image name locator for DSL in stb.properties file */
    public static final String DSL_IMAGE = "cdl.dsl.image";

    /** Constant to hold Connected Devices */
    public static final String CONNECTED_DEVICES = "Connected_Devices";

    /** Pattern to get Mac Address */
    public static final String PATTERN_TO_GET_MAC_ADDRESS = "(\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2}:\\w{2})";

    /** Constant to hold the Row Number Place holder */
    public static final String PLACE_HOLDER_FOR_ROW_NUMBER = "##ROW##";

    /** Constant representing ERROR log level */
    public static final String ERROR_LOG = "ERROR";

    /** String for business class gateway IP */
    public static final String STRING_BUSINESS_CLASS_GATEWAYIP = "10.1.10.1";

    /** String for residential class gateway ip */
    public static final String STRING_RESIDENTIAL_CLASS_GATEWAYIP = "10.0.0.1";

    /** The array list of Ccsp Components for Atom based device in ATOM side */
    public static final List<String> CCSP_COMPONENT_LIST_ATOM = new ArrayList<String>() {
	{
	    add("CcspCrSsp");
	    add("CcspWifiSsp");
	}
    };

    /** Constant for number 420 */
    public static final int CONSTANT_420 = 420;

    /** Constant for number 540 */
    public static final int CONSTANT_540 = 540;

    /** String to store colon */
    public static final String ESCAPE_SEQUENCE_CHARACTER_COLON = "\\:";

    /** String value containing IPv4 primary XDNS value to set */
    public static final String VALUE_PRIMARY_IPV4_XDNS = "";

    /** String value containing IPv6 primary XDNS value to set */
    public static final String VALUE_PRIMARY_IPV6_XDNS = "";

    /** String value containing IPv4 secondary XDNS value to set */
    public static final String VALUE_SECONDARY_IPV4_XDNS = "";

    /** String value containing IPv6 secondary XDNS value to set */
    public static final String VALUE_SECONDARY_IPV6_XDNS = "";

    /** Constant to hold string for Security Edge */
    public static final String STRING_XDNS_SECURITY_EDGE = "SecurityEdge";

    /** Constant to hold string for eenadu.net */
    public static final String STRING_XDNS_BROWSE = "eenadu.net";

    /** Test constant for Global Dns IPv4 default value 75.75.75.75 */
    public static final String STRING_DEFAULT_GLOBAL_DNS_IPV4_VALUE = "75.75.75.75";

    /** Constant to hold string for Security Edge Exclusion */
    public static final String STRING_XDNS_SECURITY_EDGE_EXCLUSION = "SecurityEdge_Exclusion";

    /** String to store empty keyword */
    public static final String EMPTY_KEYWORD = "EMPTY";

    /** Default value of Business Class devices Admin page */
    public static final String STRING_BUSINESS_CLASS_DEFAULT_ADMIN_PWD = "highspeed";

    /** String constant to hold Password string */
    public static final String STRING_PASSWORD = "password";

    /** Constant to hold Managed Services - Add Sites */
    public static final String MANAGED_SITES_ADD_SITES = "Managed_Services_Add_Site";

    /** Constant to hold Managed Services - Add Keyword */
    public static final String MANAGED_SITES_ADD_KEYWORD = "Managed_Services_Add_Keyword";

    /** Constant to hold Managed Services */
    public static final String MANAGED_SERVICES = "Managed_Services";

    /** Constant to hold Start Port in Managed Service */
    public static final String START_PORT = "21";

    /** Constant to hold End Port in Managed Service */
    public static final String END_PORT = "21";

    /** Constant to hold Managed_Devices */
    public static final String MANAGED_DEVICES = "Managed_Devices";

    /** Variable to hold Double Zero in Double quotes */
    public static final String DOUBLE_ZERO_IN_DOUBLE_QUOTES = "00";

    /** Variable to hold AM */
    public static final String TIME_AM = "AM";

    /** String for value */
    public static String STRING_VALUE = "value";

    /**
     * Constant to hold Parental Control - Managed Services Description as HTTP
     */
    public static final String MNG_SERVICES_DESCRIPTION_AS_HTTP = "HTTP";

    /** Constant to hold HTTP value in dropdown */
    public static final String HTTP_VALUE_IN_DROPDOWN = "80|80";

    /** Constant to hold 'Alert' popup title */
    public static final String POPUP_TTILE_ALERT = "Alert";

    /**
     * Variable to hold Pop Up message for Start Block time greater than End Block time in Parental Control
     */
    public static final String POP_UP_MESSAGE_START_TIME_GREATER_THAN_END_TIME = "Start time should be smaller than End time !";

    /** Constant to hold Managed Services - Add Service */
    public static final String MANAGED_SERVICES_ADD_SERVICE = "Managed_Services_Add_Service";

    /** Constant to hold string new line character with escape character */
    public static final String NEW_LINE_WITH_ESCAPE_CHARACTER = "\\n";

    /** Constant for number 28 */
    public static final int CONSTANT_28 = 28;

    /** Constant to hold int value for number 19 */
    public static final int CONSTANT_19 = 19;

    /** constant float value 0.0 */
    public static final float CONSTANT_ZERO = 0.0f;

    /** Constant to hold Constant of Wifi Client Type */
    public static final String CLIENT_TYPE_ETH = "ETHERNET";

    /** Constant to hold Add_Device_With_Reserved_Ip */
    public static final String ADD_DEVICE_WITH_RESERVED_IP = "Add_Device_With_Reserved_Ip";

    /** String Constant to replace the value :/64 */
    public static final String REPLACE_COLON_SLASH_64 = ":/64";

    /** Constant for Tshark capture protocol-dhcpv6 */
    public static final String PROTOCOL_DHCPV6 = "dhcpv6";

    /** String to hold preferred lifetime packet capture constant */
    public static final String STRING_PREFERRED_LIFETIME = "pltime:";

    /** String to hold valid lifetime packet capture constant */
    public static final String STRING_VALID_LIFETIME = "vltime:";

    /** Constant to hold URL of ebay */
    public static final String URL_EBAY = "https://www.ebay.com";

    /**
     * List of website to generate the traffic by using curl
     */
    public static final List<String> getListOfWebSitesToCurl() {
	final List<String> listOfWebsitesToPing = new ArrayList<String>() {
	    {
		add(URL_INSTAGRAM);
		add(URL_EBAY);
		add(URL_W3SCHOOLS);
		add(URL_WIKIPEDIA);
	    }
	};
	return listOfWebsitesToPing;
    }

    /** Constant for DHCPv6 beginning address */
    public static final String DHCPIPV6_SET_BEGINNING_ADDRESS = "ffff:ffff:ffff:0001";

    /** Constant for DHCPv6 ending address */
    public static final String DHCPIPV6_SET_ENDING_ADDRESS = "ffff:ffff:ffff:ffff";

    /** Constant to hold Not applicable message for residential class devices */
    public static final String NA_MSG_FOR_RESIDENTIAL_CLASS_DEVICES = "Test Step Not Applicable for Residential class devices";

    /**
     * The hash map to store the firewall modes with its corresponding Xpath
     */
    public static final Map<String, String> FIREWALL_SETTINGS = createFirewallSettingsMap();

    /**
     * Method to create a map for firewall settings corresponding to its xpath
     * 
     * @return map with xpath as key and its Security Level as value
     */
    private static Map<String, String> createFirewallSettingsMap() {
	Map<String, String> mapping = new HashMap<String, String>() {
	    {
		put("//*[@id=\"firewall_level_maximum\"]", "Maximum Security (High)");
		put("//*[@id=\"firewall_level_typical\"]", "Typical Security (Medium)");
		put("//*[@id=\"firewall_level_minimum\"]", "Minimum Security (Low)");
		put("//*[@id=\"firewall_level_custom\"]", "Custom Security");
		put("//*[@id=\"firewall_level_default\"]", "Typical Security (Default)");
	    }
	};
	return Collections.unmodifiableMap(mapping);
    }

    /** Constant to hold default IPv4 Firewall Level */
    public static final String DEFAULT_IPV4_FIREWALL_SECURITY = "Minimum Security (Low)";

    /** Constant to hold default IPv6 Firewall Level */
    public static final String DEFAULT_IPV6_FIREWALL_SECURITY = "Typical Security (Default)";

    /** Constant to hold Firewall IPv6 */
    public static final String FIREWALL_IPV6 = "Firewall_IPv6";

    /** Constant to hold Firewall IPv4 */
    public static final String FIREWALL_IPV4 = "Firewall_IPv4";

    /** Property key for invalid subnetmak values */
    public static final String PROP_KEY_INVALID_SUBNETMAK_VALUES = "invalid.subnetmaks.values.";

    /** Three seconds in millisecond representation. */
    public static final long THREE_SECONDS_IN_MILLIS = 3 * ONE_SECOND_IN_MILLIS;

    /** Property key for valid subnetmak values */
    public static final String PROP_KEY_VALID_SUBNETMAK_VALUES = "valid.subnetmaks.values.";

    /** Constant count to replace */
    public static final String STRING_COUNT = "COUNT";

    /** String disabled */
    public static final String STRING_DISABLED = "DISABLED";

    /** String enabled */
    public static final String STRING_ENABLED = "ENABLED";

    /** Constant holds a password with proper standards */
    public static final String PASSWORD_MISMATCH = "testing12";

    /** Wireless mode 802.11 n */
    public static final String WIRELESS_MODE_N = "802.11 n";

    /** Constant to hold Alert Title for deleting Port Forward Rule */
    public static final String STRING_ARE_YOU_SURE_ALERT_MESSAGE = "Are You Sure?";

    /** Wireless mode 802.11 a/n/ac */
    public static final String WIRELESS_MODE_A_N_AC = "802.11 a/n/ac";

    /** Constant to hold Weeks value in DHCP Lease time measure Minutes */
    public static final String DHCP_DROP_DOWN_MINUTES = "Minutes";

    /** String value 360 */
    public static final String STRING_360 = "360";

    /** String DHCP min address for business class devices */
    public static final String STRING_DHCP_MIN_ADDRESS_BUSSI = "10.1.10.2";

    /** Constant to hold LAN start IP default */
    public static final String LAN_START_IP_ADDRESS_DEFAULT = "10.0.0.2";

    /** Constant to hold LAN end IP */
    public static final String LAN_END_IP_ADDRESS_DEFAULT = "10.0.0.253";

    /** String DHCP min address for business class devices */
    public static final String STRING_DHCP_MAX_ADDRESS_BCI = "10.1.10.253";

    /** Ping Connectivity Test Message */
    public static final String PING_CONNECTIVITY_TEST_MESSAGE = "OK";

    /** TraceRoute Success Message */
    public static final String TRACE_ROUTE_SUCCESS_MESSAGE = "Status: Complete !";

    /** Delimiter Ampersand. */
    public static final String DELIMITER_AMPERSAND = "&";

    /** Process name lighttpd */
    public static final String PROCESS_NAME_LIGHTTPD = "lighttpd";

    /** string to store value of server.pem */
    public static final String STRING_SERVER = "server.pem";

    /** Pattern to get the parodus execution path */
    public static final String PATTERN_PARODUS_EXECUTION_PATH = "(/usr/bin/parodus .*)";

    /** Pattern to get the parner id from parodus log file */
    public static final String PATTERN_MATCHER_PARODUS_PARTNER_ID = "partnerid_header formed X-Midt-Partner-Id: +\\*,(\\w+)";

    /** Pattern to get the partner id from webconfig file */
    public static final String PATTERN_MATCHER_WEBCONFIG_PARTNER_ID = "X-Midt-Partner-Id header is +\\*,(\\w+)";

    /** string invalid partner id */
    public static final String TXT_INVALID_PARTNER_ID = "invalid partner id";

    /** String constant to hold value 200 */
    public static final String STRING_200 = "200";

    /** Pattern to get the process id for parodus using ps command */
    public static final String PATTERN_PROCESS_PARODUS = "(\\d+) +[\\w\\-?]+ +.*parodus";

    /** Constant to hold the deny status */
    public static final String STRING_DENY = "Deny";

    /** Constant to hold the Allow All status */
    public static final String STRING_ALLOW_ALL = "Allow-All";

    /** String to store unknown **/
    public static final String RFC_RESTART_SUCCEEDED = "Initial URL: https";

    /** Pattern to get ZRAM partition file names */
    public static final String PATTERN_TO_GET_ZRAM_PARTITION_FILE_NAMES = ".*(/dev/zram\\d).*";

    /** Constant to integer value 253 */
    public static final int CONSTANT_253 = 253;

    /** Constant to integer value 248 */
    public static final int CONSTANT_248 = 248;

    /** Constant to hold Error Message for DHCP invalid address */
    public static final String ERROR_MESSAGE_DHCP_INVALID_ADDRESS = "Beginning Address can't be larger than ending address!";

    /** Constant to hold Error Message after adding Invalid Host IP */
    public static final String ERROR_MESSAGE_DMZ_PAGE = "Please enter a value less than or equal to 253.";

    /** Constant to hold bootstrap config file active value */
    public static final String KEY_ACTIVE_VALUE = "ActiveValue";

    /** Pattern to get bootstrap version */
    public static final String PATTERN_BOOTSTRAP_VERSION = "version\":\\s+\"([\\d+\\.]+)\"";

    /** Constant for holding value in Xapi */
    public static final String BUILD_APPENDER = "buildAppender";

    /** String for TR69 ACS URL for partner ACS connection url parameter */
    public static final String TR69_ACS_CONNECTION_URL_PARAMETER = "Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.TR69ACSConnectURL";

    /** String File path /nvram/partner_default json */
    public static final String FILE_NVRAM_PARTNERJSON = "/nvram/partners_defaults.json";

    /** RFC payload for non-root support blocklist telemetry */
    public static final String PROP_KEY_NONROOT_SUPPORT_BLOCKLIST_TELEMETRY = "rfc.rdkb.nonroot.support.blocklist.telemetry";

    /** RFC payload for non-root support disable */
    public static final String PROP_KEY_PAYLOAD_NONROOT_BLOCKLIST = "rfc.rdkb.nonroot.support.blocklist";

    /** host address for pingtest in stb.properties file */
    public static final String PROP_KEY_HOST_ADDRESS_FOR_PINGTEST = "pingtest.host.address";

    /** invalid host address for pingtest in stb.properties file */
    public static final String PROP_KEY_INVALID_HOST_ADDRESS_FOR_PINGTEST = "pingtest.invalid.host.address";

    /** Test constant for pingtest log file */
    public static final String LOG_FILE_PINGTEST = "/rdklogs/logs/TDMlog.txt.0";

    /** Command to last n lines from a log. */
    public static final String CMD_TAIL = "tail -";

    /** Device lock success status. */
    public static final int DEVICE_LOCK_SUCCESS = 1;

    /** Ten seconds in millisecond representation. */
    public static final long TEN_SECONDS = 10000;

    /** WebPA Parameter to get PartnerID */
    public static final String WEBPA_PARAM_TO_GET_PARTNERID = "Device.IP.Diagnostics.X_RDKCENTRAL-COM_PingTest.PartnerID";

    /** Two seconds in millisecond representation. */
    public static final long TWO_SECONDS = 2000;

    /** Linux command to reboot the system. */
    public static final String CMD_REBOOT = "/sbin/reboot";

    /** String regex to check webpa response value */
    public static final String STRING_REGEX_WEBPA_RESPONSE = "value\":\"";

    /** Strings for wifihealth.txt */
    public static final String STRING_WIFIHEALTH_LOG_FILE = "wifihealth.txt";

    /** Constant to store tx overflow marker */
    public static final String STRING_FOR_TX_OVERFLOW = "WiFi_TX_Overflow_SSID_";

    /** Constant for temporary folder */
    public static final String TMP_FOLDER_LOCATION = "/tmp/";

    /** String pattern to check iper service */
    public static final String PATTERN_TO_CHECK_IPERF = ".*iperf";

    /** Constant for number 600 */
    public static final int CONSTANT_600 = 600;

    /** String telemetry market tx overflow */
    public static final String TELEMETRY_MARKER_TX_OVERFLOW = "WIFI_TX_OF_{i}_split";

    /** Pattern for verifying the String. */
    public static final String PATTERN_FOR_STRING = "[\\w-.]+";

    /** String for parameter */
    public static String STRING_PARAMETERS = "parameters";

    /** Command to get valid certificate issuer **/
    public static final String CERTIFICATE_ISSUER = "openssl crl2pkcs7 -nocrl -certfile /usr/share/ca-certificates/ca-certificates.crt | openssl pkcs7 -print_certs -text  | grep \"Issuer:\" > /tmp/issuer_details.txt";

    /** Command to get the certificates issuers */
    public static final String FILE_ISSUER_DETAILS = "/tmp/issuer_details.txt";

    /** Comamnd to get Certfiicate issuer_name */
    public static final String VALID_CERT_ISSUER_NAME = "certificate.issuers.name";

    /** Regex string to remove json invalid string from recording started line. */
    public static final String REGEX_CHECK_NEXT_LINE = "\r\n";

    /** Extension the certificate need to has */
    public static final String CERT_EXTENSION = ".crt";

    /** Regex pattern for A-Z */
    public static final String REGEX_PATTERN_CAPITAL_LETTERS = "([A-Z]+)=";

    /** Constant to hold No blocklisted process value */
    public static final String CMD_NO_BLOCKLIST_PROCESS = "No blocklisted process";

    /** String constant to store configurable non-root blocklist */
    public static final String CONFIGURABLE_NONROOT_BLOCKLIST_TELEMETRY = "nonroot_blocklist_telemetry";

    /** String to store accountid **/
    public static final String STRING_ACCOUNTID = "accountId=";

    /** String to store unknown **/
    public static final String STRING_UNKNOWN = "Unknown";

    /** String constant to hold response code 304 */
    public static final String HTTP_RESPONSE_CODE_304 = "http_code: 304";

    /** String to hold Invaid Account ID */
    public static final String INVAILD_ACCOUNT_ID = "1234567890";

    /** String to hold AccountId mismatch */
    public static final String ACCOUNT_ID_MISMATCH = "Account Id mismatch: old=1234567890";

    /** Pattern to get the list of single interface */
    public static final String PATTERN_TO_GET_INTERFACE = "astr\">(.*?)</Record>";

    /** Constant to store brlan0 under ovs bridge control */
    public static final String STRING_PORT_BRLAN0 = "Port brlan0";

    /** Constant to store brlan0 under ovs bridge control */
    public static final String STRING_BRIDGE_BRLAN0 = "Bridge brlan0";

    /** Constant for port with name */
    public static final String PORT_NAME = "Port <REPLACE>";

    /** Constant for Interface with name */
    public static final String INTERFACE_NAME = "Interface <REPLACE>";

    /** interface name to brlan1 */
    public static final String INTERFACE_NAME_BRLAN1 = "brlan1";

    /** interface name to mta0 */
    public static final String INTERFACE_NAME_HOST0 = "host0";

    /** String constant online */
    public static final String STRING_ONLINE = "online";

    /** 45 minutes in milliseconds. */
    public static final long FORTYFIVE_MINUTES_IN_MILLIS = 45 * ONE_MINUTE_IN_MILLIS;

    /** Pattern for verifying total memory. */
    public static final String PATTERN_FOR_SYS_TOTAL_MEMEORY = "RDKB_SYS_MEM_INFO_SYS : Total memory in system is";

    /** Pattern for verifying used memory. */
    public static final String PATTERN_FOR_SYS_USED_MEMEORY = "RDKB_SYS_MEM_INFO_SYS : Used memory in system is";

    /** Pattern for verifying free memory. */
    public static final String PATTERN_FOR_SYS_FREE_MEMEORY = "RDKB_SYS_MEM_INFO_SYS : Free memory in system is";

    /** Constant to hold the Div Number Place holder */
    public static final String PLACE_HOLDER_FOR_DIV_1_NUMBER = "##DIV1##";

    /** Constant to hold the Div Number Place holder */
    public static final String PLACE_HOLDER_FOR_DIV_2_NUMBER = "##DIV2##";

    /** Test Constant to store the string value Reseved IP */
    public static final String RESERVED_IP = "Reserved IP";

    /** Test Constant to store the string value DHCP */
    public static final String STRING_DHCP = "DHCP";

    /**
     * Constant to store models that has security mode as WPA3-Personal-Transition
     */
    public static final String WPA3_PERSONAL_TRANSITION_MODELS = "wpa3.personal.transition.models";

    /** reboot reason log present in webpalog.txt.0 after reboot */
    public static final String REBOOT_REASON_LOG_FROM_WEBPALOG = "Received reboot_reason as:factory-reset";

    /** regular expression to get reboot reason from webpalog file */
    public static final String REGULAR_EXPRESSION_TO_GET_REBOOT_REASON_FROM_WEBPALOG_FILE = "Received reboot_reason as:([\\S]*)";

    /** Property key for Devices with ax mode enabled */
    public static final String ENABLE_AX_MODE_DEVICES = "ax.mode.enable.models";

    /** Pattern to get webpa parameter RFC Transition enable */
    public static final String PATTERN_GET_WEBPA_PARAM_RFC_TRANSITION_ENABLE = "tr181.Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.WPA3_Personal_Transition.Enable\\\\\\\\W+(\\\\\\\\w+)";

    /** stores the constant value for BOOL_CHECK */
    public static final String CONSTANT_BOOL_CHECK = "BOOL_CHECK";

    /** Constant to store models that support Operating standards a,n,ac and ax */
    public static final String OPERATING_STD_AX_MODELS = "ax.mode.enable.models";

    /** Test constant for FirewallDebug.txt file */
    public static final String FIREWALLDEBUG_FILE = "/rdklogs/logs/FirewallDebug.txt";

    /**
     * Property key for rfc payload data in stb.props to set ForwardSSH with an invalid string
     */
    public static final String PROP_KEY_PAYLOAD_FORWARD_SSH_INVALID_STRING = "rfc.forwardssh.invalidstring";

    /**
     * Property key for rfc payload data in stb.props to set ForwardSSH with an invalid string
     */
    public static final String INVALID_STRING = "INVALIDSTRING";

    /** Constant to hold WAN Default Gateway Address (IPv6) string */
    public static final String STRING_WAN_IPV6_DEFAULT = "WAN Default Gateway Address (IPv6)";

    /** Constant to hold Primary DNS Server (IPv6) string */
    public static final String STRING_PRIMARY_IPV6_DNS = "Primary DNS Server (IPv6)";

    /** Constant to hold Secondary DNS Server (IPv6) string */
    public static final String STRING_SECONDARY_IPV6_DNS = "Secondary DNS Server (IPv6)";

    /** Constant to hold WAN IP Address (IPv6) string */
    public static final String STRING_WAN_IPV6_ADDRESS = "WAN IP Address (IPv6)";

    /** Constant to hold WAN Link Local Address (IPv6) string */
    public static final String STRING_WAN_LOCAL_IPV6 = "WAN Link Local Address (IPv6)";

    /** String to store pattern for docsis ofdm SNR level */
    public static final String PATTERN_FOR_PARAM_OFDM_SNR = "Device\\.X_RDKCENTRAL-COM_CableModem\\.DsOfdmChan\\.\\d+\\.SNRLevel";

    /** String to store metric dB */
    public static final String METRIC_DB = "dB";

    /** String to store metric dBmV */
    public static final String METRIC_DBMV = "dBmV";

    /** String to store pattern for docsis ofdm PowerLevel level */
    public static final String PATTERN_FOR_PARAM_OFDM_POWER = "Device\\.X_RDKCENTRAL-COM_CableModem\\.DsOfdmChan\\.\\d+\\.PowerLevel";

    /** String to store pattern for docsis ofdm ChannelID level */
    public static final String PATTERN_FOR_PARAM_OFDM_CHANNELID = "Device\\.X_RDKCENTRAL-COM_CableModem\\.DsOfdmChan\\.\\d+\\.ChannelID";

    /** Regex pattern to get the Power Level from SNMP Response */
    public static final String SNMP_POWER_LEVEL_RESPONSE_REGEX = "\\d.0\\s=\\s(-\\d+)";

    /** Alternalte Regex pattern to get the Power Level from SNMP Response */
    public static final String ALTERNATE_SNMP_POWER_LEVEL_RESPONSE_REGEX = "\\d.0\\s=\\s(\\d+)";

    /** Regex pattern to get the Power Level from WEBPA Response */
    public static final String POWER_LEVEL_RESPONSE_REGEX_FROM_WEBPA = "(-\\d+.\\d+)\\sdB";

    /** Alternate Regex pattern to get the Power Level from WEBPA Response */
    public static final String ALTERNATE_POWER_LEVEL_RESPONSE_REGEX_FROM_WEBPA = "(\\d+.\\d+)\\sdB";

    /** Regex pattern to get the SNR Level from SNMP Response */
    public static final String SNMP_SNR_LEVEL_RESPONSE_REGEX = ".\\d\\s=\\s(\\d+)";

    /** Regex pattern to get the SNR Level from WEBPA Response */
    public static final String SNR_LEVEL_RESPONSE_REGEX_FROM_WEBPA = "(\\d+)\\sdB";

    /** Alternate Regex pattern to get the SNR Level from WEBPA Response */
    public static final String ALTERNATE_SNR_LEVEL_RESPONSE_REGEX_FROM_WEBPA = "(\\d+.\\d+)\\sdB";

    /**
     * ENUM WHICH STORES ALL THE REQUIRED Public WiFi hotspot WEBPA PARAMS for predefined values
     */
    public static enum HotspotPublicInvalidParamsWEBPA {
	DEVICE_HS_2_4GHZ_OPEN_SEC_MODE("Device.WiFi.AccessPoint.10003.Security.ModeEnabled", "2"),
	DEVICE_HS_5GHZ_OPEN_SEC_MODE("Device.WiFi.AccessPoint.10103.Security.ModeEnabled", "2"),
	DEVICE_HS_2_4GHZ_SEC_SEC_MODE("Device.WiFi.AccessPoint.10005.Security.ModeEnabled", "2"),
	DEVICE_HS_5GHZ_SEC_SEC_MODE("Device.WiFi.AccessPoint.10105.Security.ModeEnabled", "2"),
	DEVICE_RADIO_CHANNEL_ONE("Device.WiFi.Radio.10000.Channel", "4"),
	DEVICE_RADIO_CHANNEL_TWO("Device.WiFi.Radio.10100.Channel", "4"),
	DEVICE_RADIO_CHANNEL_ONE_FB("Device.WiFi.Radio.10000.OperatingFrequencyBand", "2"),
	DEVICE_RADIO_CHANNEL_TWO_FB("Device.WiFi.Radio.10100.OperatingFrequencyBand", "2"),
	DEVICE_RADIO_CHANNEL_ONE_BW("Device.WiFi.Radio.10000.OperatingChannelBandwidth", "2"),
	DEVICE_RADIO_CHANNEL_TWO_BW("Device.WiFi.Radio.10100.OperatingChannelBandwidth", "2");

	private String webpa;
	private String value;

	private HotspotPublicInvalidParamsWEBPA(String webpa, String value) {
	    this.webpa = webpa;
	    this.value = value;
	}

	/**
	 * @return the valueToCheck
	 */
	public String getValue() {
	    return value;
	}

	/**
	 * @return the webpa
	 */
	public String getWebpa() {
	    return webpa;
	}
    }

    /** Constant to hold string for sec mode enabled */
    public static final String STRING_FOR_SEC_MODE = "ModeEnabled";

    /** Constant to hold invalid value for sec mode */
    public static final String STRING_VALUE_INVALID_SEC_MODE = "WPE2-Persoanl";

    /** Constant to hold string for operating frequency band */
    public static final String STRING_FOR_OPERATINGBAND = "OperatingFrequencyBand";

    /** Constant to hold invalid value for operating frequency band */
    public static final String STRING_VALUE_INVALID_OPER_BAND = "2GHz";

    /** Constant to hold string for channel */
    public static final String STRING_FOR_CHANNEL = "Channel";

    /** Constant to hold string for operating channel bandwidth */
    public static final String STRING_FOR_BANDWIDTH = "OperatingChannelBandwidth";

    /** Constant to hold invalid value for operating band width */
    public static final String STRING_VALUE_INVALID_OPER_BW = "90MHz";

    /** Constant to hold invalid value for channel */
    public static final String STRING_VALUE_INVALID_CHANNEL = "35";

    /** String to get CSRF Test Page */
    public static final String CSRF_TEST_PAGE = "wireless_network_configuration.jst";

    /** String Regex to validate CSRF Protection */
    public static final String STRING_REGEX_CSRF_PROTECTION = "X-CSRF-Protection:(.*)\\s+";

    /** String CSRF Version */
    public static final String CSRFP_VERSION = "OWASP CSRFP 1.0.0";

    /** String Regex to validate CSRF Token */
    public static final String STRING_REGEX_CSRF_TOKEN = "csrfp_token=(.*);";

    /** Constant representing GET local storage null response */
    public static final String GET_LOCAL_STORAGE_NULL_TEXT = "null";

    /**
     * Constant to hold Not applicable message for Device in ETHWAN Execution Mode
     */
    public static final String Not_Applicable_For_Ethwan = "Test Step Not Applicable for device in ethwan mode";

    /** String value of csrf library in lighttpd.conf file */
    public static final String CSRF_LIBRARY = "mod_csrf";

    /** String value Module Csrf */
    public static final String MODULE_CSRF = "CSRF";

    /** String to store rfc param pattern */
    public static final String STRING_RFC_PARAM_PATTERN = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.";

    /** String rfc */
    public static final String STRING_RFC = "rfc";

    /** stb properties key for getting proxy xconf rfc invalid url **/
    public static final String PROP_KEY_PROXY_XCONF_INVALID_URL = "proxy.xconf.rfc.url.invalid";

    /** Pattern to get list the of RFC feature enabled */
    public static final String PATTERN_RFC_FEATURE_LIST = "(\\w+=\\w+),[\\s+]?";

    /** Log message to verify URL connection is success in dcm script log file */
    public static final String STRING_VALUE_HTTP_CODE_200 = "http_code: 200";

    /** String constamt to hold pattern to search hash value form response */
    public static final String PATTERN_TO_FIND_HASH_VALUE = "RFC: configsethash=";

    /** String constant to hold pattern to fecth hash key value */
    public static final String PATTERN_TO_FETCH_HASH_VALUE = "RFC: configsethash=(\\w*)";

    /** Constant to store RFC_Reboot.sh script name */
    public static final String RFC_REBOOT_SH = "RFC_Reboot.sh";

    /** String to store pattern */
    public static final String PATTERN_FOR_ACCOUNTID = "\\d+";

    /** Constant to hold invalid AccountID value with special chars **/
    public static final String INVALID_ACCOUNTID_VALUE_WITH_SPECIAL_CHARS = "1245666@3#1245666@3#^";

    /** Constant to hold invalid AccountID value with more than 32 chars **/
    public static final String INVALID_ACCOUNTID_MORE_THAN_THIRTY_TWO = "123456789369852147159753123345691";

    /** Command to look for ntpd process */
    public static final String COMMAND_NTPD_PROCESS = "ps | grep ntpd";

    public static final String EXPECTED_OUTPUT_FOR_NTPHOST = "ntp.ccp.xcal.tv";

    /** Variable for status value - 'Disabled' */
    public static final String STATUS_VALUE_DISABLED = "Disabled";

    /** String to store NTPServerURL_Pattern value */
    public static final String NTPServerURL_Pattern = "(\\w*.\\w*.\\w*.\\w*)";

    /** Pattern to match Crontab time interval */
    public static final String PATTERN_TO_FETCH_CRONTAB_TIME_INTERVAL = "0\\s(0,(\\d+,)+\\d+)(\\s\\*){3}\\s/usr/ccsp/tad/log_buddyinfo.sh";

    /** Pattern Matcher to Extract the Timestamp from the log messages */
    public static final String PATTERN_MATCHER_LOG_MESSAGE_TIMESTAMP = "(^.*)\\.\\d+";

    /** String to store the path of /tmp/moca_initialized file */
    public static final String PATH_FOR_MOCA_INITIALIZED_FILE = "/tmp/moca_initialized";

    /**
     * String to store the value for average cpu threshold value
     */
    public static final String AVG_CPU_THRESHOLD_VALUE = "100";

    /** String constant value 56982 */
    public static final String STRING_CONSTANT_56982 = "56982";

    /** Constant to hold ebay in uppercase */
    public static final String EBAY_UPPERCASE = "EBAY";

    /**
     * Pattern to check syndication flow control forward mark
     */
    public static final String DSCP_INITIAL_FORWARDED_MARK = "DSCP_InitialForwardedMark=<REPLACE>";

    /**
     * Pattern to check syndication flow control output mark
     */
    public static final String DSCP_INITIAL_OUTPUT_MARK = "DSCP_InitialOutputMark=<REPLACE>";

    /**
     * Pattern to check syndication flow control status
     */
    public static final String STRING_SYNDICATION_FLOW_CONTROL = "SyndicationFlowControlEnable=<REPLACE>";

    /** stb properties key for payload data to disable ntp in xconf **/
    public static final String PROP_KEY_PAYLOAD_NTP_DISABLE_MOCK_XCONF = "rfc.ntp.disable.payload";

    /** stb properties key for payload data to enable ntp in xconf **/
    public static final String PROP_KEY_PAYLOAD_NTP_ENABLE_MOCK_XCONF = "rfc.ntp.enable.payload";

    /** Device.Time.NTPServer1 Log string to search dcmrfc.log */
    public static final String NTP_SERVER_URL_LOG_STRING = "Device.Time.NTPServer1 from value";

    /**
     * Constant to hold Parental Control - Managed Services Description as FTP
     */
    public static final String MNG_SERVICES_DESCRIPTION_AS_FTP = "FTP";

    /** Constant to hold FTP Service Port Number */
    public static final String FTP_PORT_NUMBER = "21";

    /** Constant to hold URL of FTP site: ftp.hp.com */
    public static final String URL_FTP_HP = "ftp://ftp.hp.com/";

    /** command to grep the snmp reboot telemetry marker from rdklogs folder */
    public static final String URL_ENCODED_CHAR = "%3A";

    /** String constant to store configurable telemetry feature name value */
    public static final String CONFIGURABLE_TELEMETRY_ENDPOINT = "TelemetryNewEndpoint";

    /** Constant to Hold the Security mode */
    public static final String WPA_WPA2_SECURITY_MODE = "WPA-WPA2-Personal";

    /** Constant holding the location of swupdate.conf file. */
    public static final String SOFTWARE_UPDATE_CONF_FILE_2 = "/nvram/swupdate.conf";

    /** Constant for dlcertbundle value */
    public static final String CONSTANT_DLCERTBUNDLE = "dlCertBundle";

    /*** File nvram path for myrouter.io.cert.pem ***/
    public static final String FILE_NVRAM_MYROUTER_IO_CERT_WEBUI = "/nvram/certs/myrouter.io.cert.pem";

    /** String to hold myreouter certificate before validity time */
    public static final String STRING_MYROUTER_CERTIFICATE_BEFORE_VALIDITY_DATE = "notBefore=Jun 23 00:00:00 2021";

    /** String to hold myreouter certificate after validity time */
    public static final String STRING_MYROUTER_CERTIFICATE_AFTER_VALIDITY_DATE = "notAfter=Jun 23 23:59:59 2022";

    /** Constant for nc Connection error */
    public static final String NC_CONNECTION_ERROR_IP_1 = NC_ERROR_BAD_HOST
	    + BroadbandPropertyFileHandler.getNCPrivateIPOutsideRange1();

    /** Constant for nc Connection error */
    public static final String NC_CONNECTION_ERROR_IP_2 = NC_ERROR_BAD_HOST
	    + BroadbandPropertyFileHandler.getNCPrivateIPOutsideRange2();

    /** Constant for nc Connection error */
    public static final String NC_CONNECTION_ERROR_IP_3 = NC_ERROR_BAD_HOST
	    + BroadbandPropertyFileHandler.getNCPrivateIPOutsideRange3();

    /** Constant for nc Connection error */
    public static final String NC_CONNECTION_ERROR_IP_4 = NC_ERROR_BAD_HOST
	    + BroadbandPropertyFileHandler.getNCPrivateIPOutsideRange4();

    /** Constant for Netcat console */
    public static final String NC_NETCAT_CONSOLE = "Login:";

    /** SESHAT IP from stb.properties */
    public final static String SESHAT_IP_PROPERTY_FILE_KEY = "seshat.4090.interface.ip";

    /** SESHAT IP from stb.properties */
    public final static String SESHAT_PORT_PROPERTY_FILE_KEY = "seshat.4090.interface.port";

    /** SESHAT IP from stb.properties */
    public final static String PARODUS_IP_PROPERTY_FILE_KEY = "parodus.4090.interface.ip";

    /** SESHAT IP from stb.properties */
    public final static String PARODUS_PORT_PROPERTY_FILE_KEY = "parodus.4090.interface.port";

    /** Parodus URL Parameter in device.properties */
    public static final String PARAM_PARODUS_URL = "PARODUS_URL";

    /** Constant to hold TCP */
    public static final String TCP_PROTOCOL = "tcp";

    /** String value SESHAT_URL */
    public static final String STRING_SESHAT_URL = "SESHAT_URL";

    /** String value PARODUS_URL */
    public static final String STRING_PARODUS_URL = "PARODUS_URL";

    /** Regex value for SESHAT URL */
    public static final String REGEX_SESHAT_URL = "SESHAT_URL=(.*)";

    /** String value for INPUT */
    public static final String STRING_INPUT = "INPUT";

    /** String value for FORWARD */
    public static final String STRING_FORWARD = "FORWARD";

    /** String value for ACCEPT */
    public static final String STRING_ACCEPT = "ACCEPT";

    /** Pattern Finder to confirm the 4090 Interface communication in iptable input rule */
    public static final String PATTER_MATCHER_IPTABLE_INPUT_RULE = "(INPUT.*)(.*"
	    + BroadbandPropertyFileHandler.get4090InterfaceToValidateInIptables() + ".*)(.*ACCEPT)";

    /** Pattern Finder to confirm the 4090 Interface communication in iptable forward rule */
    public static final String PATTERN_MATCHER_IPTABLE_FORWARD_RULE = "(FORWARD.*)(.*"
	    + BroadbandPropertyFileHandler.get4090InterfaceToValidateInIptables() + ".*)(.*ACCEPT)";

    /** String value 6666 */
    public static final String STRING_6666 = "6666";

    /** IOT Client Port */
    public static final String IOT_CLIENT_PORT_6668 = "6668";

    /** Log Message for Received Upstream Event Radion Interface Statistics Report */
    public static final String LOG_MESSAGE_RECEIVED_UPSTREAM_EVENT_INERFACE_DEVICES_WIFI = ".*raw.kestrel.reports.InterfaceDevicesWifi";

    /** Pattern Finder to confirm the communication between parodus and webpa */
    public static final String PATTERN_MATCHER_CONNECTION_ESTABLISHED = "(tcp.*)((?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))(?![\\d])(.*ESTABL.*)";

    /** Log Message for Received Upstream Event Radion Interface Statistics Report */
    public static final String LOG_MESSAGE_RECEIVED_UPSTREAM_EVENT_NETWORK_STATUS_REPORT = ".*raw.kestrel.reports.NetworkDevicesStatus";
    
    /** Pattern Matcher to extract the value from Activation Log */
    public static final String PATTERN_MATCHER_ACTIVATION_BOOTLOG_VALUE = "#TELEMETRY_LOG#=(\\d+)";

    /** Pattern Matcher to extract the value from Activation Log */
    public static final String PATTERN_MATCHER_ACTIVATION_LOG_VALUE = "#TELEMETRY_LOG#:(\\d+)";

    /** Telemetry Log Text */
    public static final String TEXT_TELEMETRY_LOG = "#TELEMETRY_LOG#";
    
    /** String parameter for factory resetting the box for DSL Device */
    public static final String STRING_FACTORY_RESET_PARAMETER_FOR_DSL = "Router,Wifi,VoIP,Firewall";
}

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

import com.automatics.utils.AutomaticsPropertyUtility;

public class BroadBandConnectedClientTestConstants {
    /** windows command to get the WLAN networks  */
    public static final String WINDOWS_COMMAND_TO_GET_WLAN_NETWORK = "netsh wlan show networks";
    
    /** linux command to get the WLAN networks  */
    public static final String LINUX_COMMAND_TO_GET_WLAN_NETWORK = "sudo iw dev <INTERFACE> scan ";
    
    /** String variable to store connection type */
    public static final String STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI = "Wi-Fi";
    
    /** String variable to store wifi capability for only 2 GHz */
    public static final String STRING_WIFI_CAPABILITY_2_4GHZ_ONLY = "2.4GHz";

    /** String variable to store wifi capability for only 5 GHz */
    public static final String STRING_WIFI_CAPABILITY_5GHZ_ONLY = "5GHz";
    
    /** String variable to store wifi capability for dual band */
    public static final String STRING_WIFI_CAPABILITY_DUAL_BAND = "Dual band";
    
    /** Command Constant to get the Network connections list */
    public static final String CMD_MAC_OS_WIFI_NETWORK_INTERFACE_NAME = "networksetup -listallhardwareports |grep -A 1 -i <interface> -i |grep -i Device:";
    
    /** The constant holds the wifi  interface name */
    public static final String MAC_OS_WIFI_PARAM_INTERFACE_NAME = "<interface>";
    
    /** Command Constant to verify the particular ssid is available in preferred network list(Profile) */
    public static final String CMD_MAC_OS_VERIFY_SSID_IN_PREFERRED_NWLIST = "networksetup -listpreferredwirelessnetworks <interface> | grep -i <ssid>";
    
    /** The constant holds the wifi  ssid name */
    public static final String MAC_OS_WIFI_PARAM_SSID = "<ssid>";
    
    /** Command Constant to Add the particular ssid to preferred network list(Profile) */
    public static final String CMD_MAC_OS_ADD_SSID_PREFERRED_NWLIST = "networksetup -addpreferredwirelessnetworkatindex <interface> <ssid> 1 <securitymode> <password>";
    
    /** The constant holds the wifi  ssid security mode */
    public static final String MAC_OS_WIFI_PARAM_SECURITY_MODE = "<securitymode>";
    
    /** The constant holds the wifi password */
    public static final String MAC_OS_WIFI_PARAM_PASSWORD = "<password>";
    
    /** Command Constant to Remove the particular ssid From preferred network list(Profile) */
    public static final String CMD_MAC_OS_REMOVE_SSID_PREFERRED_NWLIST = "networksetup -removepreferredwirelessnetwork <interface> <ssid>";
    
    /** Command Constant to connect the ssid */
    public static final String CMD_MAC_OS_CONNECT_SSID = "networksetup -setairportnetwork <interface> <ssid> <password>";
    
    /** Command Constant to disconnect the currently connected ssid */
    public static final String CMD_MAC_OS_DISCONNECT_SSID = "sudo /System/Library/PrivateFrameworks/Apple80211.framework/Versions/Current/Resources/airport -z ";
    
    /** Command Constant to get the currently connected ssid */
    public static final String CMD_MAC_OS_CONNECTED_SSID = "networksetup -getairportnetwork <interface>";
    
    /** Constant for Security mode Open */
    public static final String SECURITY_MODE_OPEN = "Open";
    
    /** Constant for holding the windows os */
    public static final String OS_WINDOWS = "WINDOWS";
    
    /** Constant for holding the MAC OS */
    public static final String OS_MAC = "MAC";
    
    /** Constant for holding linux os */
    public static final String OS_LINUX = "LINUX";
    
    /** MAC OS command for checking IPV6 address */
    public static final String MAC_OS_COMMAND_CHECK_IPV4_ADDRESS = "ifconfig <interface> |grep -i \"inet\"";
    
    /** MAC OS command for checking IPV6 address */
    public static final String MAC_OS_COMMAND_CHECK_IPV6_ADDRESS = "ifconfig <interface> |grep -i \"inet6\"";
    
    /** Pattern for checking IPV4 address MAC OS */
    public static final String PATTERN_MAC_OS_VALID_IPV4_ADDRESS = "(\\d+.){1,3}";
    
    /** Pattern for checking IPV6 address MAC OS */
    public static final String PATTERN_MAC_OS_VALID_IPV6_ADDRESS = "(\\w+:\\w+:\\w+:\\w+:\\w+:\\w+:\\w+:\\w+)";
    
    /** linux command for checking IPV4 address */
    public static final String LINUX_COMMAND_CHECK_IPV4_ADDRESS = "/sbin/ifconfig | grep 'inet '";
    
    /** linux command for checking IPV6 address */
    public static final String LINUX_COMMAND_CHECK_IPV6_ADDRESS = "/sbin/ifconfig | grep 'inet6'";
    
    /** windows command for checking IPV4 address */
    public static final String WINDOWS_COMMAND_CHECK_IPV4_ADDRESS = "ipconfig |grep -A 10 \"Wireless LAN adapter Wi\" |grep -i \"IPv4 Address\"";
    
    /** windows command for checking IPV6 address */
    public static final String WINDOWS_COMMAND_CHECK_IPV6_ADDRESS = "ipconfig |grep -A 10 \"Wireless LAN adapter Wi\" |grep -i \"IPv6 Address\"";
    
    /**Pattern for checking IPV4 address*/
    public static final String PATTERN_LINUX_VALID_IPV4_ADDRESS="inet (\\d+.){1,3}\\d+ +netmask (\\d+.){1,3}\\d+ +broadcast (\\d+.){1,3}\\d+";
    
    /**Pattern for checking IPV6 address*/
    public static final String PATTERN_LINUX_VALID_IPV6_ADDRESS="inet6\\s*(\\w+:\\w+:\\w+:\\w+:\\w+:\\w+:\\w+:\\w+)";
      
    /**Pattern for checking IPV4 address*/
    public static final String PATTERN_WINDOWS_VALID_IPV4_ADDRESS="(\\d+.){1,3}";
    
    /**Pattern for checking IPV6 address*/
    public static final String PATTERN_WINDOWS_VALID_IPV6_ADDRESS="(\\w+:\\w+:\\w+:\\w+:\\w+:\\w+:\\w+:\\w+)";
    
    /** Default inet6 address pattern from log without scope */
    public static final String INET_V6_ADDRESS_PATTERN_WITUOUT_SCOPE = "inet6 addr:\\s*(\\w+:\\w+:\\w+:\\w+:\\w+:\\w+:\\w+:\\w+)";
    
    /**Pattern for checking IPV6 address*/
    public static final String PATTERN_WINDOWS_VALID_SEG_FIVE_IPV6_ADDRESS="(\\w+:\\w+:\\w+:\\w+::\\w+)";
    
    /** Constant for response status as OK */
    public static final String RESPONSE_STATUS_OK = "200 OK";
    
    /** Pattern to match ping response from Windows machine */
    public static final String PATTERN_MATCHER_PING_RESPONSE_WINDOWS = "Packets:\\s+Sent\\s+=\\s(\\d+),\\s+Received\\s+=\\s(\\d+),\\s+Lost\\s+=\\s(\\d+)\\s\\((\\d+)%\\s+loss";
  
    /** Pattern to match ping response from Linux machine */
    public static final String PATTERN_MATCHER_PING_RESPONSE_LINUX = "(\\d+)\\s+.*transmitted.*\\s+(\\d+)\\s+.*received.*\\s+(\\d+)%\\s+.*loss";
       
    /** Pattern to match ping response from MAC machine */
    public static final String PATTERN_MATCHER_PING_RESPONSE_MAC = "(\\d+)\\s+.*packets transmitted,*\\s+(\\d+)\\s+.*packets received,*\\s+(\\d+)*\\.?(\\d+)%\\s+.*packet loss";
 
    /**Pattern for checking IPV6 address*/
    public static final String PATTERN_WINDOWS_VALID_SEG_FOUR_IPV6_ADDRESS="(\\w+:\\w+:\\w+::\\w+)";
    
    /** Verify Wifi network connectivty for windows client*/
    public static final String CMD_WINDOWS_NW_CONNECTION = "ping www.google.com -n 1 | grep 'Reply from'"; 
    
    /** Verify Wifi network connectivty using IPV4 address for linux client*/
    public static final String CMD_LINUX_NW_CONNECTION_USING_IPV4_IP = "curl -4 -f --interface <interfaceName> www.google.com | grep '200 OK'";
    
    /** Verify Wifi network connectivty using IPV6 address for linux client*/
    public static final String CMD_LINUX_NW_CONNECTION_USING_IPV6_IP = "curl -6 -f --interface <interfaceName> www.google.com | grep '200 OK'";  
  
    /** Windows command to get Connection-specific DNS suffix */
    public static final String WINDOWS_COMMAND_TO_GET_DNS_SUFFIX = "ipconfig |grep -i \"Connection-specific DNS suffix\" ";

    /** Linux command to retrieve Connection-specific DNS suffix */
    public static final String CMD_LINUX_TO_RETRIEVE_DNS_SUFFIX = "nmcli dev show | grep DOMAIN";
    
    /** Command constant to get wifi interface name */
    public static final String CMD_LINUX_GET_INTERFACE_NAME = "nmcli dev status | grep wifi";
    
    /** Ping Command Packet Size Option */
    public static final String CMD_PING_PACKET_SIZE_OPTION = "-l 100 ";
    
    /** External Router OS Type */
    public static final String OS_TYPE_EXTERNAL_ROUTER = "External Router";
    
    /** Constant for holding the radio status up */
    public static final String RADIO_STATUS_UP = "Up";
    
    /** Constant for holding the radio status down */
    public static final String RADIO_STATUS_DOWN = "Down";
    
    /** curl command in linux  to check connectivity using IPV4 address */
    public static final String COMMAND_CURL_LINUX_IPV4_ADDRESS = "curl -4 -v --interface <INTERFACE> 'www.google.com' | grep '200 OK'";

    /** curl command  in linux  to check connectivity using IPV4 address */
    public static final String COMMAND_CURL_LINUX_IPV6_ADDRESS = "curl -6 -v --interface wlan0 'www.google.com' | grep '200 OK'";
    
    /** curl command in windows  to check connectivity using IPV4 address */
    public static final String COMMAND_CURL_WINDOWS_IPV4_ADDRESS = "curl -4 -v 'www.google.com' | grep '200 OK'";

    /** curl command in windows  to check connectivity using IPV4 address */
    public static final String COMMAND_CURL_WINDOWS_IPV6_ADDRESS = "curl -6 -v 'www.google.com' | grep '200 OK'";
    
    /** Windows command to get IP address */
    public static final String WINDOWS_COMMAND_TO_GET_IP_ADDRESS = "ipconfig /all";
    
    /** Linux command to retrieve dns server ip in client */
    public static final String CMD_LINUX_TO_RETRIEVE_DNS_SERVER_IPS = "nmcli dev show | grep DNS";
    
    /** Raspbian Linux OS */
    public static final String OS_RASPBIAN_LINUX = "RASPBIAN LINUX";
    
    /** windows command to get the operating mode  */
    public static final String WINDOWS_COMMAND_TO_GET_OPERATING_MODE = "netsh wlan show interface | grep \"Radio type\" ";
    
    /** windows command to get the wifi security mode */
    public static final String WINDOWS_COMMAND_TO_GET_WIFI_SECURITY_MODE = "netsh wlan show networks | awk '/<ssid>/,/Encryption/' | grep Authentication";
 
    /** Pattern to get security mode from 2 GHz client device */
    public static final String PATTERN_GET_SECURITY_MODE_2GHZ_CLIENT_DEVICES = "Authentication\\s+:\\s+([\\w\\d-]*)";
    
    /** Constant for Security mode None */
    public static final String SECURITY_ENCRYPTION_METHOD_AES = "AES";
    
    /** Pattern to get security mode from 5 GHz client device */
    public static final String PATTERN_GET_SECURITY_MODE_5GHZ_CLIENT_DEVICES = "Authentication\\s+:\\s+([\\w\\d-]*)";
    
    /** Linux command to get the default address */
    public static final String CMD_LINUX_DETAULT_GATEWAY_IP = "ip route | grep default";
    
    /** windows command to get the default address */
    public static final String CMD_WIN_DETAULT_GATEWAY_IP = "ipconfig |grep -A 20 \"Ethernet adapter Ethernet\" |grep -i \"Default Gateway\"";

    /** Flag for IIS service start */
    public static final Boolean IIS_START_FLAG = true;
    
    /** Command for validate IIS service status */
    public static final String CMD_TO_CHECK_ISS_STATUS = "iisreset /status";
    
    /** Pattern to match ping response from active status for iis */
    public static final String PATTERN_TO_CHECK_IIS_RUNNING_STATUS = "Running";

    /** Pattern to match ping response from stop iss service */
    public static final String PATTERN_TO_CHECK_IIS_STOP_STATUS = "stopped";
    
    /** Command for start IIS service */
    public static final String CMD_ISS_START = "iisreset /start";
    
    /** Command for stop IIS service */
    public static final String CMD_ISS_STOP = "iisreset /stop";
    
    /** Command to get home dir */
    public static final String CMD_HOME_FILE_PATH = "echo $HOME";
    
    /** The constant holds the mac address */
    public static final String LOG_PARAM_MAC_ADDRESS = "MacAddress is (\\w+:\\w+:\\w+:\\w+:\\w+:\\w+)";
    
    /** The constant holds the connected client type wifi */
    public static final String LOG_PARAM_CLIENT_TYPE_WIFI = "RDKB_CONNECTED_CLIENTS: Client type is WiFi,";
    
    /** The constant holds the connected client type ethernet */
    public static final String LOG_PARAM_CLIENT_TYPE_ETHERNET = "RDKB_CONNECTED_CLIENTS: Client type is Ethernet,";
    
    /** String variable to store connection type Ethernet */
    public static final String CLIENT_DEVICE_CONNECTION_TYPE_ETHERNET = "Ethernet";
    
    /** Pattern to match iperf response for sender */
    public static final String PATTERN_TO_GET_IPERF_TRANSFER_RATE_SENT_FROM_CLIENT = ".*\\s+([\\d\\.]+\\s+[KMG]?Bytes).*sender";
  
    /** Pattern to match iperf response for receiver */
    public static final String PATTERN_TO_GET_IPERF_TRANSFER_RATE_RECEIVED_BY_SERVER = ".*\\s+([\\d\\.]+\\s+[KMG]?Bytes).*receiver";
    
    /** Constant hold driver name geckodriver */
    public static final String DRIVER_NAME_GECKODRIVER = "geckodriver";
    
    /** Command to get the gecko driver version */
    public static final String CMD_TO_GET_THE_GECKO_DRIVER_VERSION = DRIVER_NAME_GECKODRIVER + " --version";
    
    /** Pattern to get gecko driver version from client */
    public static final String PATTERN_TO_GET_GECKO_DRIVER_VERSION_FROM_CLIENT = "geckodriver\\s+\\d+.(\\d+).\\d+";
    
    /** Command to check unzip support */
    public static final String CMD_TO_CHECK_UNZIP_SUPPORT = "unzip -v";
    
    /** Chrome driver storage api url from property file */
    public static final String CHROME_DRIVER_STORAGE_GOOGLE_APIS_URL = "chromedriver.storage.googleapis.url";
    
    /** Constant to hold selenium configuration path to be read from property file for windows os */
    public static final String SELENIUM_CONFIG_PATH_FOR_WINDOWS_OS = "selenium.config.path.windows.os";
    
    /** Constant hold java kill command for chrome driver in windows */
    public static final String CHROME_DRIVER_KILL_CMD_FOR_WINDOWS = "taskkill /F /IM chromedriver.exe /T";
    
    /** Constant hold grep command for chrome driver in windows */
    public static final String CHROME_DRIVER_PROCESS_STATUS_FOR_WINDOWS = "tasklist | grep 'chromedriver.exe'";
    
    /** Pattern to for chrome browser version */
    public static final String PATTERN_FOR_CHROME_DRIVER_PROCESS_RUNNING_STATUS ="chromedriver.exe\\s+\\d+\\s+Services";
    
    /** download file path */
    public static final String DOWNLOAD_FILE_PATH = "/home/<REPLACE>/";
    
    /** Chrome driver download zip file name from property file */
    public static final String CHROME_DRIVER_DOWNLOAD_ZIP_FILE_NAME = "chromedriver.download.filename.zip";
    
    /** Constant to hold selenium file path for cygwin */
    public static final String CYGWIN_SELENUIM_FILE_PATH = "/cygdrive/c/Selenium/";
    
    /** Command to unzip and move the file to configured location */
    public static final String CMD_TO_DOWNLOAD_DRIVER = "curl --create -O --output " + DOWNLOAD_FILE_PATH + " "
	    + AutomaticsPropertyUtility.getProperty(CHROME_DRIVER_STORAGE_GOOGLE_APIS_URL) + "<<VALUE>>/"
	    + AutomaticsPropertyUtility.getProperty(CHROME_DRIVER_DOWNLOAD_ZIP_FILE_NAME);
    
    /** Command to unzip and move the file to configured location */
    public static final String CMD_TO_UNZIP_AND_MOVE_THE_DRIVER = "unzip " + DOWNLOAD_FILE_PATH
	    + AutomaticsPropertyUtility.getProperty(CHROME_DRIVER_DOWNLOAD_ZIP_FILE_NAME) + " -d "
	    + CYGWIN_SELENUIM_FILE_PATH;

    /** Constant to hold version check windows vbs file */
    public static final String CHROMEDRIVER_VERSION_CHECK_VBS_FILE_NAME = "chromedriver.version.check.vbs.file.name";
    
    /** Constant to hold version check windows bat file */
    public static final String CHROMEDRIVER_VERSION_CHECK_BAT_FILE_NAME="chromedriver.version.check.bat.file.name";
    
    /** Selenium register command for windows os */
    public static final String SELENIUM_REGISTER_CMD_FOR_WONDOWS_OS = "selenium.reg.cmd.for.windows.os";
    
    /** Constant hold replace string for vbs file location */
    public static final String REPLACE_STRING_VBS_FILE_LOCATION = "<VBS_FILE_LOCATION>";
    
    /** Constant to hold version check output file name */
    public static final String VERSION_CHECK_OUTPUT_FILE_NAME = "chromedriver.version.check.output.file.name";
    
    /** Selenium configuration chrome driver file name to be read from property file */
    public static final String SELENIUM_CONFIG_CHROME_DRIVER_FILE_NAME = "selenium.config.chrome.driver.file.name";
    
    /** Command to get the chrome driver version in version_output.txt */
    public static final String CMD_TO_GET_THE_CHROME_DRIVER_VERSION = "grep -i \"ChromeDriver\" <REPLACE>";

    /** Command to get the chrome browser version in version_output.txt */
    public static final String CMD_TO_GET_THE_CHROME_BROWSER_VERSION = "grep -i \"Version    REG_SZ\" <REPLACE>";

    /** Pattern to for chrome driver version */
    public static final String PATTERN_TO_GET_DRIVER_VERSION = "ChromeDriver\\s+(\\d+)";

    /** Pattern to for chrome browser version */
    public static final String PATTERN_TO_GET_BROWSER_VERSION = "Version\\s+REG_SZ\\s+(\\d+)";
    
    /** Constant for latest release */
    public static final String LATEST_RELEASE = "LATEST_RELEASE_";
    
    /** Constant to get the installed path using which cmd*/
    public static final String GET_INSTALLED_PATH = "which ";
    
    /** Constant hold browser name firefox */
    public static final String BROWSER_NAME_FIREFOX = "firefox";
    
    /** Command to get the firefox browser version */
    public static final String CMD_TO_GET_THE_FIREFOX_VERSION = BROWSER_NAME_FIREFOX + " -v";
    
    /** Constant for ethernet client driver browser mapping from property file*/
    public static final String ETHERNET_CLIENT_DRIVER_BROWSER_MAPPING = "ethernet.client.driver.browser.mapping";
    
    /** Command to get present working directory */
    public static final String CMD_TO_GET_PWD = "pwd";
    
    /** Pattern to get gecko driver version from property */
    public static final String PATTERN_TO_GET_GECKO_DRIVER_VERSION_FROM_PROPERTY = "\\d+.(\\d+).\\d+";

    /** Pattern to get firefox browser version */
    public static final String PATTERN_TO_GET_FIREFOX_VERSION = "Mozilla Firefox\\s+(\\d+).\\d+.\\d+";
    
    /** Gecko driver downlaod url from property file */
    public static final String GECKO_DRIVER_DOWNLOAD_URL = "gecko.driver.download.url";
    
    /** Pattern to get gecko driver download status */
    public static final String PATTERN_FOR_GECKO_DRIVER_DOWNLAOD_STATUS = "saved\\s+\\[\\d+/\\d+\\]";

    /** Pattern to get gecko driver filename */
    public static final String PATTERN_FOR_GECKO_DRIVER_FILENAME = "(geckodriver-v\\d+.\\d+.\\d+-arm7hf.tar.gz)";
    
    /** Command to untar the file*/
    public static final String CMD_TAR="tar -xvzf ";
}

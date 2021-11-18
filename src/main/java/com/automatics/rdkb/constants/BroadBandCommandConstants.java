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

public class BroadBandCommandConstants extends RDKBTestConstants{
    
    /** Command to set the log interval time for channel bandwidth */
    public static final String CMD_UPLOAD_TIME = "echo 5 > /tmp/upload";
    
    /** File path /tmp/upload */
    public static final String FILE_UPLOAD = "/tmp/upload";
    
    /** Log file location for WiFiLog.txt.0 */
    public static final String LOCATION_WIFI_HEALTH_LOG = "/rdklogs/logs/wifihealth.txt";
    
    /** Command to remove the contents */
    public static final String CMD_RM_WITH_R_F_OPTION = "rm -rf ";
    
    /** Ls command */
    public static String CMD_LS = "ls ";
    
    /** Command to show the hidden files in the directory */
    public static final String CMD_TO_SHOW_HIDDEN_FILES = "ls -la | grep";
    
    /** SNMP Log File Name */
    public static final String LOG_FILE_SNMP = "/rdklogs/logs/SNMP.txt.*";
    
    /** Nslookup with path */
    public static final String CMD_NSLOOKUP_WITH_PATH = "/usr/bin/nslookup";
    
    /** Command to get interface column number from route table */
    public static final String CMD_GET_INTERFACE_COLUMN_NUMBER_IN_ROUTE_TABLE = "/sbin/route | grep Iface | awk -v col='Iface' '{for (i=1; i<=NF; i++) if ($i == col){c=i; break}}{print c}'";

    /** Command to get default column value from route table */
    public static final String CMD_ROUTE_DEFAULT_ROW_FIRST_COLUMN = "/sbin/route | grep default | awk '{print $1}'";

    /** curl command with 20 secs timeout */
    public static final String CMD_CURL_WITH_20_SECS_TIMEOUT = "curl -k --connect-timeout 20 -v ";
    
    /** curl command to retrieve header details of a connection */
    public static final String CMD_CURL_WITH_TIMEOUT_AND_HEADER = "curl -k --connect-timeout 20 --head ";
    
    /** curl command to resolve domain to IPv6 Address */
    public static final String CMD_CURL_RESOLVE_DOMAIN_TO_IPV6_ADDRESS = CMD_CURL_WITH_TIMEOUT_AND_HEADER + "-6 ";
    /** curl command to resolve domain to IPv4 Address */
    public static final String CMD_CURL_RESOLVE_DOMAIN_TO_IPV4_ADDRESS = CMD_CURL_WITH_TIMEOUT_AND_HEADER + "-4 ";
    
    /** Command Constants for linux ping for ipv6 address */
    public static final String CMD_PING_LINUX_IPV6 = "ping6 -c 5 ";
    
    /** Ping - Command for windows using ipv6 */
    public static final String CMD_PING_WINDOWS_IPV6 = "ping -6 -n 5 ";
    
    /** Ping - Command */
    public static final String CMD_PING_WINDOWS = "ping -n 5 ";
    /** Ping - Linux Command */
    public static final String CMD_PING_LINUX = "ping -c 5 ";
    
    /** Command to get the data time from device in specific format */
    public static final String CMD_DATE_LOG_FORMAT = "date +%y%m%d-%H:%M:%S";
    
    /** Command to check mesh status */
    public static final String CMD_SYSTEM_MESH_STATUS=  "systemctl status -l meshwifi.service";
    
    /** Command to stop mesh agent */
    public static final String CMD_SYSTEM_STOP_MESH_AGENT=  "systemctl stop meshAgent.service";
    
    /** Command to stop mesh service */
    public static final String CMD_SYSTEM_STOP_MESH_SERVICE=  "systemctl stop meshwifi.service";
    
    /** command to get txpower */
    public static final String CMD_TXPOWER = "/sbin/iwlist ath1 txpower";
    
    /** pattern to find Tx power in channel */
    public static final String PATTERN_TO_FIND_POWERLIMIT = "Current Tx-Power=(\\d+)";
    
    /** SSR Xconf url */
    public static final String SSR_XCONF_URL = " TBD ";
      
    /** CI Xconf url */
    public static final String CI_XCONF_URL = " TBD ";
    
    
    /** Command to get the details of rabid process */
    public static final String CMD_PS_GREP_RABID_PROCESS = "ps | grep -i rabid | grep -v grep";
    
    /** Command to get the details of rabid process */
    public static final String CMD_PS_GREP_ADVSEC = "ps | grep -i advsec | grep -v grep";
    
    /** Command constant to refresh firewall */
    public static final String CMD_TO_REFRESH_FIREWALL = "/usr/bin/sysevent set firewall-restart";
    
    /** Command to Reboot */
    public static final String CMD_TO_REBOOT_DEVICE = "/sbin/reboot";
    
    /** Command to grep values from iptable */
    public static final String CMD_TO_GREP_VALUES_FROM_IPTABLE = "/usr/sbin/iptables -S -w | grep ";
    
    /** Command to grep values from ip6tables */
    public static final String CMD_TO_GREP_VALUES_FROM_IP6TABLES = "/usr/sbin/ip6tables -S -w | grep ";
	
	    /** File name for Consolelog.txt */
    public static final String FILE_CONSOLELOG = "/rdklogs/logs/Consolelog.txt.0";
    
    /** Dmcli prefix to add a table */
    public static final String DMCLI_PREFIX_TO_ADD_TABLE = "dmcli eRT addtable";
    
    /** MeshAgent Log File Name */
    public static final String LOG_FILE_MESHAGENT = "/rdklogs/logs/MeshAgentLog.txt.0";
    
    /** command to find file */
    public static final String CMD_FIND_INAME = "find / -iname ";
    
    /** Cat command to display the default json message */
    public static final String CMD_CAT_PARTNERS_DEFAULT_JSON = "cat /etc/partners_defaults.json";
    
    /** Command to get nslookup ipv4 address */
    public static final String CMD_NSLOOKUP_WITH_PATH_FOR_IPV4_ADDRESS = "/usr/bin/nslookup -query=A ";
    
    /** FolderPath for SelfHeal.txt.0 */
    public static final String FILE_SELFHEAL_LOG = "/rdklogs/logs/SelfHeal.txt.0";
    
    /** Command to get the configparamgen version */
    public static final String CONFIGPARAMGEN = "configparamgen";
}

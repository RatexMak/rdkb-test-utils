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

public class BroadBandCommandConstants extends RDKBTestConstants {

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
    public static final String CMD_SYSTEM_MESH_STATUS = "systemctl status -l meshwifi.service";

    /** Command to stop mesh agent */
    public static final String CMD_SYSTEM_STOP_MESH_AGENT = "systemctl stop meshAgent.service";

    /** Command to stop mesh service */
    public static final String CMD_SYSTEM_STOP_MESH_SERVICE = "systemctl stop meshwifi.service";

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
    /** File name for dcmrfc log file */
    public static final String FILE_DCMRFC_LOG = "/rdklogs/logs/dcmrfc.log";

    /** Sec Console Log File location */
    public static final String LOG_FILE_SEC_CONSOLE = "/rdklogs/logs/SecConsole.txt.0";

    /** Command Constants for GW Configuration Telemetry Marker */
    /** Logs Directory */
    public static final String DIRECTORY_LOGS = "/rdklogs/logs/";

    /** Command to get the configuration download details from /rdklogs/logs/PAMlog.txt.0 */
    public static final String CMD_TO_GET_CONFIG_DOWNLOAD_DETAILS = "/rdklogs/logs/PAMlog.txt.0";

    /** Command to tail given file to Backup file in nvram */
    public static final String TAIL_GIVEN_LOG_FILE_WITH_PATH = "tail -f -n <<VALUE>> /rdklogs/logs/<REPLACE> > <BACKUP>&";

    /** File name for rfc_configfata.txt */
    public static final String FILE_RFC_CONFIGDATA_LOG = "/tmp/rfc_configdata.txt";

    /** Command to know the status of dnsmasq service */
    public static final String COMMAND_TO_CHECK_THE_RUNNING_STATUS_OF_DNSMASQ_SERVICE = "systemctl status dnsmasq.service";

    /** Command to Verify the running status of dnsmasq process */
    public static final String COMMAND_TO_LIST_DNSMASQ_FROM_RUNNING_PROCESSES = "ps | grep dnsmasq |grep -v grep";

    /** Command to Verify the running status of dnsmasq zombie process */
    public static final String COMMAND_TO_LIST_DNSMASQ_ZOMBIE_FROM_RUNNING_PROCESSES = "  ps | grep dnsmasq |grep -v grep | grep \"Z\"";

    /** SelfHeal Log File Name */
    public static final String LOG_FILE_SELFHEAL = "/rdklogs/logs/SelfHeal.txt.0";

    /** AtomConsolelog Log File Name */
    public static final String LOG_FILE_ATOM_CONSOLE = "/rdklogs/logs/AtomConsolelog.txt.0";

    /** WebPA Log File Name */
    public static final String LOG_FILE_WEBPA = "/rdklogs/logs/WEBPAlog.txt.0";

    /** Log Message in PARODUS log for Connected to server over SSL */
    public static final String LOG_MESSAGE_SERVER_CONNECTION = "Connected to server over SSL";

    /** Log File for Parodus. */
    public static final String LOG_FILE_PARODUS = "/rdklogs/logs/PARODUSlog.txt.0";

    /** Command Constant for kill Process 11 */
    public static final String KILL_11_PROCESS = "kill -11";

    /** device.properties File */
    public static final String FILE_DEVICE_PROPERTIES = "/etc/device.properties";

    /** Overridden coredump properties file name */
    public static final String FILE_NVRAM_COREDUMP_PROPERTIES = "/nvram/coredump.properties";

    /** Ccsp Process Name **/
    public static final String CCSP_COMPONENT_PROCESS_NAME = "CcspCrSsp";

    /** Constant to hold file path for rdklogs/xconf.txt.0 file */
    public static final String FILE_TMP_XCONFCDL = "/tmp/xconfcdl";

    /** Log file to get tail of /nvram/log/event/eventlog */
    public static final String CMD_GET_XCONF_TXT = "tail -f -n +1 /rdklogs/logs/xconf.txt.0 > " + FILE_TMP_XCONFCDL
	    + "&";

    /** String to store file location for xconf backup */
    public static final String STRING_XCONF_BACKUP_FILEPATH = "/rdklogs/logs/xconf.txt";

    /** CAT for reading Log File */
    public static final String CMD_CAT = "cat ";

    /** File path for XconfUrlOverride file in tmp */
    public static final String FILE_PATH_TMP_XCONF_URL_OVERRIDE = "/nvram/XconfUrlOverride";

    /** Command to get the data time from device in specific format */
    public static final String CMD_DATE_RDKB_LOG_FORMAT = "date +%y%m%d-%T.%6N";

    /** WebPA Log File Name */
    public static final String LOG_FILE_WEBPA_TEXT = "/rdklogs/logs/WEBPAlog.txt.*";

    /** Command to grep between range */
    public static final String CMD_GREP_AWK_START_RANGE = " | awk '\\$1 >= \"";

    /** Command to grep between range */
    public static final String CMD_GREP_AWK_END_RANGE = "\" && \\$1 <= \"";

    /** Command Constants for Soft Reboot Test */
    /** BootTime Log File */
    public static final String FILE_BOOT_TIME_LOG = "/rdklogs/logs/BootTime.log";

    /** Selfheal Log File */
    public static final String FILE_SELFHEAL_LOG_TXT_0 = "SelfHeal.txt.0";

    /** DCM Settings Conf File - /nvram */
    public static final String FILE_NVRAM_DCMSETTINGS_CONF = "/nvram/.DCMSettings.conf";

    /** DCM Settings Conf File - /tmp */
    public static final String FILE_TMP_DCMSETTINGS_CONF = "/tmp/DCMSettings.conf";

    /** Log File for secure syscfg.d */
    public static final String LOG_FILE_SECURE_SYSCFG = "/opt/secure/data/syscfg.db";

    /** Log File for DCM Script */
    public static final String LOG_FILE_SYSCFG = " /nvram/syscfg.db";

    /** CMD NS lookup */
    public static final String CMD_NSLOOKUP = "nslookup ";

    /** File name for ArmConsoleLog.txt */
    public static final String FILE_ARMCONSOLELOG = "/rdklogs/logs/ArmConsolelog.txt.0";

    /** Command to check ipv6 Prefix from zebra.conf */
    public static final String COMMAND_TO_GET_IPV6_PREFIX_VALUE_ZEBRA = "grep -i \"ipv6 nd prefix\" /var/zebra.conf";

    /** File path for parodus_create_file.sh */
    public static final String FILE_PATH_PARODUS_CREATE_FILE_SH = "/usr/ccsp/parodus/parodus_create_file.sh";

    /** File path for parodus_read_file.sh */
    public static final String FILE_PATH_PARODUS_READ_FILE_SH = "/usr/ccsp/parodus/parodus_read_file.sh";

    /** File path for encrypted parodus client certificate */
    public static final String FILE_PATH_ENCRYPTED_PARODUS_CERT = "/etc/ssl/certs/staticXpkiCrt.pk12";

    /** File path for decrypted parodus client certificate */
    public static final String FILE_PATH_DECRYPTED_PARODUS_CERT = "/tmp/adzvfchig-res.mch";

    /** File path for parodus command file in tmp */
    public static final String FILE_PATH_PARODUS_CMD = "/tmp/parodusCmd.cmd";

    /** CR Log File Name */
    public static final String LOG_FILE_CR = "/rdklogs/logs/CRlog.txt.0";

    /** File name PARODUS_ENABLE */
    public static final String FILE_NAME_PARODUS_ENABLE = "PARODUS_ENABLE";

    /** command echo with empty string */
    public static final String CMD_ECHO_WITH_SPACE = "echo \" \" > ";

    /** Command netstat for 8080 */
    public static final String CMD_NETSTAT_8080 = "/bin/netstat -an | grep 8080";

    /** Command to get snmpv3 logs */
    public static final String CMD_TO_GET_SNMPV3_DH_KICKSTART_LOGS = "cat /rdklogs/logs/dcmrfc.log | grep -i \"snmpv3\"";

    /** command telnetd */
    public static final String CMD_TELNETD = "telnetd";

    /** command telnet */
    public static final String CMD_TELNET = "telnet";

    /** command telnetexcludeunwanted */
    public static final String CMD_TELNET_EXCLUDE_UNWANTED = " 2>/dev/null";

    /** SSH Command */
    public static final String CMD_SSH = "ssh ";

    /** LmLite Log File Name */
    public static final String LOG_FILE_LMLITE = "/rdklogs/logs/LM.txt.0";

    /** Harvester Log File */
    public static final String FILE_HARVESTER_LOG = "/rdklogs/logs/Harvesterlog.txt.0";

    /** Command to tail given file to Backup file in NVRAM */
    public static final String TAIL_LOG_TO_BACKUP_FILE = "rm -rf /nvram/automation_BackUp<REPLACE>;tail -f -n +1 /rdklogs/logs/<REPLACE> > /nvram/automation_BackUp<REPLACE>&";

    /** Naviagte to nvram log with given log */
    public static final String NAVIGATE_GIVEN_FILE_IN_NVRAM = "/nvram/<REPLACE>";

    /** File path for storing root crontab schedule */
    public static final String ROOT_CRON_TAB_TEMP = "/tmp/cron/root";

    /** Command to get cron jobs */
    public static final String CMD_GET_CRON_JOBS_ATOM = "cat " + ROOT_CRON_TAB_TEMP + " | grep -i dca_utility";

    /** File path for storing root crontab schedule */
    public static final String ROOT_CRON_TAB = "/var/spool/cron/crontabs/root";

    /** Command to get cron jobs */
    public static final String CMD_GET_CRON_JOBS = "cat " + ROOT_CRON_TAB + " |grep -i dca_utility";

    /** Temporary file in nvram to store PARODUS log tail */
    public static final String FILE_PATH_NVRAM_PARODUS_TAIL = "/nvram/automation_PARODUStail.txt";
    
    /** Command to tail PARODUSlog contents to nvram */
    public static final String CMD_GET_PARODUSLOGS_NVRAM = "tail -f /rdklogs/logs/PARODUSlog.txt.0 > /nvram/automation_PARODUStail.txt &";
    
    /** Command to stop parodus service using systemctl */
    public static final String CMD_SYSTEMCTL_STOP_PARODUS = "systemctl stop parodus.service";
    
    /** File path for storing custom parodus close reason */
    public static final String CMD_TEMP_PARODUS_CLOSE_REASON = "/tmp/parodus_close_reason.txt";

    /** Command to replace ampersand with close reason option in /tmp/parodusCmd.cmd */
    public static final String CMD_SED_ADD_PARODUS_CLOSE_REASON_FILE = "sed -i 's#\\&#--close-reason-file="
	    + CMD_TEMP_PARODUS_CLOSE_REASON + "#g' /tmp/parodusCmd.cmd";

    /** Command to echo custom parodus close reason into temp file */
    public static final String CMD_ECHO_CUSTOM_PARODUS_REASON_INTO_FILE = "echo \"Closing from file\" > "
	    + CMD_TEMP_PARODUS_CLOSE_REASON;
    
    /** Command to start parodus using custom /tmp/parodusCmd.cmd file */
    public static final String CMD_START_CUSTOM_PARODUS_FROM_TMPCMD = "export LOG4C_RCPATH=/fss/gw/rdklogger; \\`cat /tmp/parodusCmd.cmd\\` \\&";
    
    /** Data Only Mount /nvram */
    public static final String MOUNT_NVRAM = "/nvram/";
    
    /** Data Only Mount /nvram2 */
    public static final String MOUNT_NVRAM2 = "/nvram2/";
    
    /** Test Shell Script File Name */
    public static final String FILE_TEST_SHELL_SCRIPT = "rdkb10618.sh";
    
    /** ECHO Command with -e Option */
    public static final String CMD_ECHO_E = "echo -e ";
    
    /** chmod Command */
    public static final String CMD_CHMOD = "chmod ";

    /** rwx Permission to ALL Users */
    public static final String CHMOD_777_VALUE = "777 ";

 /** Command to initiate firmware schedule.sh file */
    public static final String FILE_FIRMWARE_SCHED_SH = "export PATH=$PATH:/bin:/sbin;export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/bin:/sbin;sh /etc/firmwareSched.sh";

    /** Command to retrieve Linux Kernel Version */
    public static final String CMD_LINUX_KERNEL_VERSION = "uname -r";

    /** Command to retrieve the host name and Linux version */
    public static final String CMD_LINUX_VERSION = " cat /proc/version";
    
    /** Grep Xconf Current Model */
    public static final String XCONF_CURRENT_MODEL_ARM_CONSOLE_LOGS = "grep -i \"XCONF SCRIPT : MODEL\" /rdklogs/logs/ArmConsolelog.txt.0";
    
    /** Grep Xconf Current Model in xconf.txt.0 */
    public static final String XCONF_CURRENT_MODEL = "grep -i \"XCONF SCRIPT\" /rdklogs/logs/Consolelog.txt.0";
    
    /** Command constant to get Dropbear version of the device */
    public static final String CMD_GET_DROPBEAR_VERSION = "/usr/sbin/dropbear -V";
    
    /** Command to get mounted status of the specific partition **/
    public static final String MOUNT_COMMAND = "mount | grep -w ";
    
    /** Touch Command */
    public static final String CMD_TOUCH = "touch ";
    
    /** Nslookup with path */
    public static final String CMD_NSLOOKUP_WITH_PATH_FOR_IPV6_ADDRESS = "nslookup -query=AAAA";
    
    /** command to copy to nvram consolelog.txt */
    public static final String COMMAND_TO_COPY_TO_NVRAM_CONSOLELOG = "tail -f /rdklogs/logs/Consolelog.txt.0 > /tmp/Consolelog.txt &";

    /** Temporary file in tmp to store Consolelog */
    public static final String FILE_PATH_TMP_CONSOLE_LOG = "/tmp/Consolelog.txt";
    
    /** command to grep the snmp reboot telemetry marker from nvram folder */
   public static final String COMMAND_TO_GREP_REBOOT_TELEMETRY_MARKER = "grep -i \"RDKB_REBOOT: Reboot triggered by SNMP\" /rdklogs/logs/Consolelog.txt.0";
  
   /** Command to get process details of ccsp */
   public static final String CMD_PS_GREP_CCSP = "ps | grep -i ccsp";
   
 

}

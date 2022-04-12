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
import java.util.List;

import com.automatics.tap.AutomaticsTapApi;

public class BroadBandCommandConstants extends RDKBTestConstants {
	
    public static final String CMD_TO_LIST_TMP_CERTS = "ls /tmp/shesc/";
	
    /** Command to move CA certificate */
    public static final String CMD_GET_CABUNDLE_MOVECERTIFICATE = "mv /tmp/shesc/ca-certificates.crt-orig /tmp/shesc/ca-certificates.crt";
	
    /** Command to move CA certificate BACK */
    public static final String CMD_GET_CABUNDLE_MOVECERTIFICATE_BACK = "mv /tmp/shesc/ca-certificates.crt /tmp/shesc/ca-certificates.crt-orig ";
	
    /** Command for CA BUNDLE PING GOOGLE */
    public static final String CMD_TO_PING_GOOGLE = "curl -v --cert-status https://google.com/";
	
    /** Command for CA BUNDLE Certs List */
    public static final String CMD_TO_LIST_CERTS = "ls -la /etc/ssl/certs/COMODO_RSA_Certification_Authority.pem";
    
    /**
     * Command to verify if all certs are present in the shadow folder /tmp/shesc
     */
    public static final String CMD_GET_CABUNDLE_SHADOWFOLDER = "/tmp/shesc/";
	
    /** Command to verify caupdatebundle packages are downloaded successfully */
    public static final String CMD_GET_CABUNDLE_DOWNLOAD = "/tmp/caupdatebundle/etc/ca-store-update-bundle.bz2";
    
    /** Command for getting RDM Status logs for CABundle */
    public static final String CMD_GET_CABUNDLE_RDMSTATUS_LOG = "/rdklogs/logs/rdm_status.log";
	
    /** Command for check if CABUNDLE is enabled */
    public static final String CMD_GET_CABUNDLE_ENABLE_LOG = "/rdklogs/logs/caupdate.log";
	
    /** Command for CA BUNDLE */
    public static final String CMD_SET_CABUNDLE_URL = "echo \"https://<cdl server>\" > /tmp/.xconfssrdownloadurl";

    /** Command to set the log interval time for channel bandwidth */
    public static final String CMD_UPLOAD_TIME = "echo 5 > /tmp/upload";

    /** File path /tmp/upload */
    public static final String FILE_UPLOAD = "/tmp/upload";

    /** Log file location for WiFiLog.txt.0 */
    public static final String LOCATION_WIFI_HEALTH_LOG = "/rdklogs/logs/wifihealth.txt";

    /** Command to remove the contents */
    public static final String CMD_RM_WITH_R_F_OPTION = "rm -rf ";

    /** command for ipconfig */
    public static final String COMMAND_IPCONFIG = "ipconfig";

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

    /*** Command to get latest php version ***/
    public static final String CMD_TO_GET_LATEST_PHP_VERSION = "php-cgi --version | head -n 1 | cut -d\" \" -f2";

    /**
     * Command to check the difference between /nvram/syscfg.db and /opt/secure/data/syscfg.db files
     */
    public static final String CMD_CONFIG_SYSCFG_DIFFERENCE = "diff -q /nvram/syscfg.db /opt/secure/data/syscfg.db";

    /** Command to check firewall level */
    public static final String CMD_CONFIG_FIREWALL_LEVEL = "syscfg get firewall_level";

    /** Command to set High firewall level */
    public static final String CMD_CONFIG_SET_FIREWALL_LEVEL_HIGH = "syscfg set firewall_level High; syscfg commit";

    /** Command to set Low firewall level */
    public static final String CMD_CONFIG_SET_FIREWALL_LEVEL_LOW = "syscfg set firewall_level Low; syscfg commit";

    /**
     * The constant holding command for seeing the process status of CCSPXDNSSSP process.
     */
    public static final String PS_COMMAND_FOR_CCSPXDNSSSP_PROCESS = "ps | grep CcspXdnsSsp";

    /**
     * The constant holding command for seeing the process status of CcspHomeSecurity process.
     */
    public static final String PS_COMMAND_FOR_CCSPHOMESECURITY_PROCESS = "ps | grep CcspHomeSecurity";

    /** File path for CUJO agent log file */
    public static final String LOG_FILE_AGENT = "/rdklogs/logs/agent.txt";

    /** Command to get latest dnsmasq version on any device */
    public static final String CMD_TO_GET_LATEST_DNSMASQ_VERSION = "\'dnsmasq -version | head -n 1 |cut -d' ' -f3\'";

    /** Command to ge the yocto version */
    public static final String CMD_GREP_YOCTO_VER_FROM_VERSION_FILE = " grep \"YOCTO_VERSION[:=]\" /version.txt";

    /** Command to retrieve Size of the wifihealth.txt file */
    public static final String CMD_WIFIHEALTH_FILE_SIZE = "ls -lrt /rdklogs/logs/ | grep wifihealth.txt | cut -c39-43";

    /** Command to retrieve TimeStamp from first column of wifihealth.txt file */
    public static final String CMD_WIFIHEALTH_TIMESTAMP_FROM_FIRST_COLUMN = "cat /rdklogs/logs/wifihealth.txt | sed -n 1p | cut -c1-21";

    /** Command to retrieve TimeStamp from first column of aphealth.txt file */
    public static final String CMD_APHEALTH_TIMESTAMP_FROM_FIRST_COLUMN = "/usr/ccsp/wifi/aphealth.sh | sed -n 1p | cut -c1-21";

    /** Command to folder path tmp **/
    public static final String FOLDER_PATH_TMP = "/tmp";

    /** Command to make a file executable */
    public static final String CMD_MAKE_FILE_EXECUTABLE = "chmod a+x ";

    /** Command diff /nvram/testfile /opt/secure/testfile */
    public static final String CMD_DIFF_NVRAM_OPT_SECURE_TESTFILE = "diff /nvram/testfile /opt/secure/testfile";

    /** Command to execute shell script */
    public static final String EXECUTE_STRESS_TEST_SHELL_SCRIPT = "sh /nvram/Stress_Test.sh";

    /** File name result.txt */
    public static final String FILE_RESULT_TXT = "result.txt";

    /** cat result */
    public static final String CAT_TMP_RESULT = "cat /tmp/" + FILE_RESULT_TXT;

    /** Constant for Directory Name tmp */
    public static final String DIRECTORY_TMP = "/tmp";

    /** Log File for /tmp/syscfg.db */
    public static final String LOG_FILE_TMP_SYSCFG = "/tmp/syscfg.db";

    /** Command to show syscfg */
    public static final String CMD_SYSCFG_SHOW = "syscfg show";

    /** Command to set firewall_levelv6 to Custom */
    public static final String CMD_TO_SET_FIREWALL_LEVEL_V6_TO_CUSTOM = "syscfg set firewall_levelv6 Custom; syscfg commit";

    /** Command to set firewall_levelv6 to High */
    public static final String CMD_TO_SET_FIREWALL_LEVEL_V6_TO_HIGH = "syscfg set firewall_levelv6 High; syscfg commit";

    /** Command to enable SNMPV3 support */
    public static final String CMD_CONFIG_ENABLEV3SUPPORT = "syscfg set V3Support true; syscfg commit";

    /** Command to disable SNMPV3 support */
    public static final String CMD_CONFIG_DISABLEV3SUPPORT = "syscfg set V3Support false; syscfg commit";

    /** Command to get iptable */
    public static final String CMD_TO_GET_IPTABLE = "/usr/sbin/iptables-save";

    /** Command to clear dcmrfc log file */
    public static final String CMD_CLEAR_DCMRFC_LOG = "echo '' > /rdklogs/logs/dcmrfc.log";

    /** Command to get CapDebug.log file */
    public static final String CMD_TO_GET_CAPDEBUG = "/rdklogs/logs/CapDebug.txt";

    /** command to Grep process details */
    public static final String CMD_GET_PROCESS_DETAILS = "ps | grep -nri <REPLACE> | grep -v grep";

    /** File path for autovault wbpa_cfg.json */
    public static final String FILE_PATH_AUTOVAULT_WBPA_CFG_JSON = "cpefiles/rdkb/wbpa_cfg.json";

    /** File path for /tmp/wbpa_cfg.json */
    public static final String FILE_PATH_TMP_WBPA_CFG_JSON = "/tmp/wbpa_cfg.json";

    /** Webpa configuration backup file path */
    public static final String WEBPA_CFG_JSON_BKP_FILE = "/nvram/webpa_cfg.json_bkp";

    /** File WebPA CFG Json */
    public static final String FILE_WEBPA_CFG = "/nvram/webpa_cfg.json";

    /** Command cp */
    public static final String CMD_COPY = "cp ";

    /** Command to tail PAMlog contents to nvram */
    public static final String CMD_GET_PAMLOGS_NVRAM = "tail -f /rdklogs/logs/PAMlog.txt.0 > /nvram/automation_PAMtail.txt &";

    /** Command to tail ArmConsolelog contents to nvram */
    public static final String CMD_GET_ARMCONSOLELOGS_NVRAM = "tail -f /rdklogs/logs/ArmConsolelog.txt.0 > /nvram/automation_Consoletail.txt &";

    /** Command to tail Consolelog contents to nvram */
    public static final String CMD_GET_CONSOLELOGS_NVRAM = "tail -f /rdklogs/logs/Consolelog.txt.0 > /nvram/automation_Consoletail.txt &";

    /** Temporary file in nvram to store PAMlog log tail */
    public static final String FILE_PATH_NVRAM_PAM_TAIL = "/nvram/automation_PAMtail.txt";

    /** Temporary file in nvram to store Console log tail */
    public static final String FILE_PATH_NVRAM_CONSOLE_TAIL = "/nvram/automation_Consoletail.txt";

    /** Command to get the midnight epoch time */
    public static final String CMD_MIDNIGHT_EPOCH_TIME = "date -d '00:00:00' '+%s'";

    /** Command to get current epoch time */
    public static final String CMD_CURRENT_EPOCH_TIME = "date +%s";

    /** Command to trigger firmware download for devices **/
    public static final String SH_COMMAND_TO_TRIGGER_FIRMWARE_DOWNLOAD_DSL = "/bin/sh /nvram/hub4_triggerFirmwareDownload.sh";

    /** Command to get sysevent status */
    public static final String CMD_SYSEVENT_GET = "sysevent get ";

    /** Command to set UPLOAD_LOGS_VAL_DCM value */
    public static final String SET_VALUE_FOR_UPLOAD_LOGS_VAL_DCM = "/usr/bin/sysevent set UPLOAD_LOGS_VAL_DCM true";

    /** Command to get the process for cpu procanalyzer */
    public static final String PROCESS_NAME_CPUPROCANALYZER = "cpuprocanalyzer";

    /** File path for cpuprocanalyzer */
    public static final String FILE_PATH_CPU_PROC_ANALYZER = "/etc/procanalyzerconfig.ini";

    /** File path for auto vault for procanalyzerconfig.ini */
    public static final String FILE_PATH_CPU_PROC_NVRAM = "/nvram/procanalyzerconfig.ini";

    /** File path for auto vault for processes.list */
    public static final String FILE_PATH_NVRAM_PROCESSES = "/nvram/processes.list";

    /** Command to get the size of the cpu procanalyzer */
    public static final String SIZE_OF_CPUPROCANALYZER = "du -shc /tmp/* | grep -i cpuprocanalyzer|cut -f1";

    /** Folder path for cpuprocanalyzer */
    public static final String FOLDER_PATH_CPU_PROC_ANALYZER = "/tmp/cpuprocanalyzer";

    /** Command constant to grep for log strings in CPUPROCANALYZERlog.txt.0 */
    public static final String COMMAND_CPUPROCANALYZER_LOG_FILE = "/rdklogs/logs/CPUPROCANALYZERlog.txt.0";

    /** Command to override S3 Amazon signing url */
    public static final String CMD_AMAZON_URL_OVERRIDE = "echo 'S3_AMAZON_SIGNING_URL=http://test' > "
	    + FILE_NVRAM_COREDUMP_PROPERTIES;

    /** Command to Start PAM process */
    public static final String CMD_START_PANDM_PROCESS = "/usr/bin/CcspPandMSsp -subsys eRT";

    /** Command Constants for brlan0 interface */
    public static final String CMD_BRLAN0_DOWN = "/sbin/ifconfig brlan0 down";

    /** Command Constants for brlan0 interface status */
    public static final String CMD_BRLAN0_STATUS = "/sbin/ip a show brlan0";

    /** Command to grep only process */
    public static final String CMD_TO_GREP_ONLY_PROCESS = " | grep -v grep";

    /** Command to grep process using ps */
    public static final String CMD_PS_GREP = "ps | grep ";

    /** File name for Atom journal Log.txt */
    public static final String FILE_ATOM_JOURNALLOG = "/rdklogs/logs/atom_journal_logs.txt.0";

    /**
     * This API will get the TCPDUMP file_autovault from properties
     * 
     * @author Rakesh C N
     */
    public static String getTCPDUMPFile() {
	return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.FILE_PATH_TCPDUMP);
    }

    /** Command to provide write permission to tmp/tcpdump **/
    public static final String CMD_PERMISSION_TO_TCPDUMP = "chmod 755 /tmp/tcpdump";

    /** Command to start tcpdump */
    public static final String CMD_TO_START_TCPDUMP_FOR_PORT_53_FOR_CISCO = "/tmp/tcpdump -i any -n udp port 53 &> /tmp/dummy.txt &";

    /** Command to start tcpdump */
    public static final String CMD_TO_START_TCPDUMP_FOR_PORT_53 = "/usr/sbin/tcpdump -i any -n udp port 53 &> /tmp/dummy.txt &";

    /** Command to ping google 10 times */
    public static final String CMD_TO_PING_GOOGLE_COM = "ping6 -c 10 www.google.com";

    /** Command to kill any process */
    public static final String CMD_TO_KILL_ANY_PROCESS = "killall ";

    /** String to hold value - tcpdump */
    public static final String TCPDUMP = "tcpdump";

    /** To use dummy file path inside device */
    public static final String DUMMY_FILE_PATH_IN_TMP = "/tmp/dummy.txt";

    /** Source path to override resolv.dnsmasq file */
    public static final String FILE_RESOLV_DNSMASQ_SRC_PATH = "/etc/resolv.conf ";

    /** String that stores url of google */
    public static final String URL_GOOGLE = "www.google.com";

    /** Command to fetch dnsmasq service details using ps command */
    public static final String CMD_TO_FETCH_DNSMASQ_SERVICE_DETAILS = "ps | grep -i dnsmasq";

    /** Constant variable to store -o option which is used by dnsmasq service */
    public static final String STRING_CONST_HYPHEN_O = "-o";

    /** Linux command to copy files */
    public static final String CMD_COPY_FILES = "cp";

    /** Destination path to override resolv.dnsmasq file */
    public static final String FILE_RESOLV_DNSMASQ_DSTN_PATH = "/tmp/resolv.conf";

    /** Command to delete all lines from file other than 1st line */
    public static final String CMD_TO_DEL_ALL_LINES_EXCEPT_FIRST = "sed -i '1\\!d' /tmp/resolv.conf";

    /** command to add invalid servver to file */
    public static final String CMD_TO_ADD_INVALID_PRI_DNS_SERVER = "sed -i \\'$ a nameserver <IPV6>\\' /tmp/resolv.conf";

    /** command to add secondary dns servver to file */
    public static final String CMD_TO_ADD_VALID_SEC_DNS_SERVER = "sed -i \\'$ a nameserver <IPV6>\\' /tmp/resolv.conf";

    /** String to store the command for /sbin/mount-copybind **/
    public static String SBIN_MOUNT_COPYBIND = "/sbin/mount-copybind";

    /** command to unmount specified file system */
    public static final String CMD_TO_UNMOUNT_FILE_SYSTEM = "umount -i";

    /** Command to get downstream upstream from CMLog.txt.0 */
    public static final String CMD_TO_GET_UPSTREAM_DOWNSTREAM_LOG = "cat /rdklogs/logs/CMlog.txt.0 |grep -E \"Downstream|Upstream\"";

    /** Process name to get the process for CcspServiceManager */
    public static final String PROCESS_NAME_CCSPSERVICEMANAGER = "CcspServiceManager";

    /** Log filename for Serivce Manager Log */
    public static final String LOG_SERVICEMANAGERLOG = "ServiceManagerlog";

    /** Library name for svcagent library */
    public static final String LIB_SVCAGENT = "libsvc_agt.so";

    /** Command Constants for CCSP Service Manager Test */

    /** Library name for CcspServiceManagerBroadBand Library */
    public static final String LIB_CCSPSERVICEMANAGERBROADBAND = "ccsp-servicemanager-broadband";

    /** Command to grep sleep process */
    public static final String CMD_PS_GREP_SLEEP = "ps | grep sleep";

    /** Command to get ip6table */
    public static final String CMD_TO_GET_IPV6TABLE = "/usr/sbin/ip6tables-save";

    /** Complete file path for ecfs.txt file */
    public static final String FILE_ECFS_TXT = " /opt/logs/ecfs.txt";

    /** File path /dev/mmcblk0p7 */
    public static final String FILE_PATH_DEV_MMCBLK0P7 = "/dev/mmcblk0p7";

    /** File path for ecfs.txt */
    public static final String FILE_PATH_ECFS_TXT = "/rdklogs/logs/ecfs.txt";

    /** File path for Aggressive self heal */
    public static final String FILE_PATH_SELFHEAL_AGGRESSIVE_TXT = "/rdklogs/logs/SelfHealAggressive.txt";

    /** command to grep the value of inet6 from brlan0 */
    public static final String CMD_GET_BRLAN0_IP = "/sbin/ifconfig brlan0|grep -i global|cut -d \" \" -f13";

    /** Process name dibbler server */
    public static final String PROCESS_DIBBLER_SERVER = "dibbler-server";

    /** Command to make the interface brlan1 down */
    public static final String CMD_BRLAN1_DOWN = "/sbin/ifconfig brlan1 down";

    /** Command to create a new zombie instance for dnsmasq */
    public static final String CMD_ZOMBIE_INSTANCE_FOR_DNSMASQ = "\\$(dnsmasq -u nobody -q --clear-on-reload --bind-dynamic --add-mac --add-cpe-id=abcdefgh --dhcp-authoritative -P 4096 -C /var/dnsmasq.conf & exec /bin/sleep 5m) &";

    /** Command Constant for ifconfig */
    public static final String CMD_IFCONFIG = "/sbin/ifconfig";

    /** Command Constant for erouter0 down */
    public static final String CMD_EROUTER0_INTERFACE_DOWN = "/sbin/ifconfig erouter0 down";

    /** Command Constant for erouter0 Up */
    public static final String CMD_EROUTER0_INTERFACE_UP = "/sbin/ifconfig erouter0 up";

    /** Command to get the process details for dhcp v4 */
    public static final String CMD_PS_TI_UDHCPC = "ps -ww | grep -i ti_udhcpc | grep -v grep";

    /** Command to get the process details for dhcp v6 */
    public static final String CMD_PS_TI_DHCP6C = "ps -ww | grep -i ti_dhcp6c | grep -v grep";

    /** Command to get the process details for DHCPv4 from ps */
    public static final String CMD_PS_UDHCPC_EROUTER = "ps -ww | grep -i udhcpc | grep -i erouter | grep -v grep";

    /** Command to get the dibbler client */
    public static final String CMD_DIBBLER_CLIENT = "/usr/sbin/dibbler-client ";

    /** Command to get the ifconfig output and get the before line */
    public static final String CMD_IFNAME_USING_IFCONFIG = "/sbin/ifconfig | grep -B 1 -i ";

    /** Command NetCat version */
    public static final String CMD_NETCAT_VERSION = "nc -vV";

    /** Command NetCat listening cmd */
    public static final String CMD_NC_LISTENING = "nc -l 2222 ; nc -nvlp 443";

    /** Command NetCat execute cmd */
    public static final String CMD_NC_EXECUTE = "nc -e /bin/sh";

    /** Command NetCat bad host cmd */
    public static final String CMD_NC_BAD_HOST = "nc 10.0.0.1 7767 ; nc 192.168.106.254 23 ; nc 169.254.1.1 767 ; nc 192.168.147.10";

    /** Command NetCat connect host outside */
    public static final String CMD_NC_OUTSIDE_HOST = "nc 10.0.0.23 7767";

    /** Command NetCat connect local host */
    public static final String CMD_NC_LOCAL_HOST = "nc 127.0.0.1 7787";

    /** WiFi Log File */
    public static final String LOCATION_FILE_WIFI_LOG_TXT_0 = "/rdklogs/logs/WiFilog.txt.0";

    /** command to read a text file avoiding repeated lines */
    public static final String COMMAND_UNIQ = "uniq ";

    /** command for crontab -1 */
    public static final String CRONTAB_EXECUTE_COMMAND = "crontab -l";

    /** Log File for DCM Script */
    public static final String LOG_FILE_DCM_SCRIPT = "/rdklogs/logs/dcmscript.log";

    /** ipconfig command to get 40 lines after the grep match */
    public static final String CMD_IPCONFIG_ALL_GREP_A40 = "ipconfig /all |grep -A 40 ";

    /**
     * Command for to grep Wifi Passphrase value from nvram not in /nvram/secure/
     */
    public static final String CMD_WIFI_PASSWORD_NVRAM = "grep -rinI <REPLACE> /nvram/ | grep -v /[secure_path]";

    /** Command to start mesh agent */
    public static final String CMD_SYSTEM_START_MESH_AGENT = "systemctl start meshAgent.service";

    /** Command to start mesh service */
    public static final String CMD_SYSTEM_START_MESH_SERVICE = "systemctl start meshwifi.service";

    /** Command Constants for ping -c 5 8.8.8.8 */
    public static final String CMD_PING_8_8_8_8 = "ping -c 5 8.8.8.8";

    /** Command Constants for ip route with dir */
    public static final String CMD_SBIN_IP_ROUTE = "/sbin/ip route";

    /** Command Constants for ip route */
    public static final String CMD_IP_ROUTE = "ip route";

    /** Command to folder nvram/BackUp tmp path **/
    public static final String PATH_FOR_BACK_UP_FILE = "/nvram/automation_BackUp<REPLACE>";

    /** cat File name parodusCmd.cmd */
    public static final String CAT_FILE_PARODUSCMD = "cat /tmp/parodusCmd.cmd";

    /** Command to check firmware schedule.sh file */
    public static final String FILE_ETC_FIRMWARE_SCHED_SH = "/etc/firmwareSched.sh";

    /** Command to get the details from /rdklogs/logs/CPUInfo.txt.0 * */
    public static final String FILE_PATH_TO_GET_CPU_INFO = "/rdklogs/logs/CPUInfo.txt.0";

    /** Command to get wan status */
    public static final String GET_WAN_STATUS = "sysevent get wan-status";

    /** Command to stop/start CcspTr069PaSsp process */
    public static final String CMD_SYSTEM_STOP_START_CCSPTR069PASSP = "systemctl <REPLACE> CcspTr069PaSsp";

    /** Command to Verify the running status of TestAndDiag process */
    public static final String COMMAND_TO_LIST_TAD_FROM_RUNNING_PROCESSES = "ps | grep tad";

    /** Command to Verify the resource moniter started file presence */
    public static final String COMMAND_TO_FIND_RESOURCE_MONITOR_STARTED = "ls /tmp/.resource_monitor_started";

    /** command to Grep CcspWifiSsp process */
    public static final String CMD_GET_WIFI_PROCESS = "ps | grep -i \"CcspWifiSsp\"";

    /** command to Grep hostapd process */
    public static final String CMD_GET_HOSTAPD_PROCESS = "ps -ww | grep -i \"hostapd\"";

    /** String to hold dropbear_dss_host_key file name */
    public static final String FILE_NAME_OF_DROPBEAR_DSS_KEY = "dropbear_dss_host_key";

    /** String to hold dropbear_rsa_host_key file name */
    public static final String FILE_NAME_OF_DROPBEAR_RSA_KEY = "dropbear_rsa_host_key";

    /** String to hold id_dropbear file name */
    public static final String FILE_NAME_OF_ID_DROPBEAR = "id_dropbear";

    /** String to store the /etc/dropbear/ directory path */
    public static final String FULL_PATH_OF_ETC_DROPBEAR_DIR = "/etc/dropbear/";

    /** String to store the yav file used by dropbear process */
    public static final String FILE_NAME_OF_YAV_FILE_USED_BY_DROPBEAR = "filjzumaq.yav";

    /** String to store kep file used by dropbear process */
    public static final String FILE_NAME_OF_KEP_FILE_USED_BY_DROPBEAR = "gelewumol.kep";

    /** Command to execute ps -ww | grep */
    public static final String CMD_PS_WW_GREP = "ps -ww | grep ";

    /** File name for dropbear config1 */
    public static final String FILE_DROP_CFG_1 = "dropcfg1";

    /** File name for dropbear config2 */
    public static final String FILE_DROP_CFG_2 = "dropcfg2";

    /** Command to grep the dropbear running process */
    public static final String PROCESS_NAME_DROPBEAR = "\"[d]ropbear\"";

    /** Command for to grep mesh syscfg db value */
    public static final String CMD_MESH_VALUE_SYSCFG_DB = "syscfg show| grep -i mesh";

    /** TR-069 Log File Name */
    public static final String LOG_FILE_TR69_TEXT = "/rdklogs/logs/TR69log.txt.*";

    /** String variable dig */
    public static final String CMD_DIG = "dig";

    /** String variable wget */
    public static final String CMD_WGET_HTTP = "wget ";

    /** Command to ping */
    public static final String CMD_PING_COUNT_TWO = "ping -c 2";

    /** Command to list the ipset */
    public static final String CMD_LIST_IPSET = "/usr/sbin/ipset list";

    /** Command to flush ipset list */
    public static final String CMD_FLUSH_IPSET_LIST = "/usr/sbin/ipset flush ip_blacklist";

    /** Command to list ipset list */
    public static final String CMD_LIST_IPSET_TABLE = "/usr/sbin/iptables -L | grep \"blacklist\"";

    /** Command to drop ipset list */
    public static final String CMD_DROP_IPSET_LIST = "/usr/sbin/iptables -I FORWARD -m set --match-set ip_blacklist dst -j DROP";

    /** Command to add ipaddress to blacklist */
    public static final String CMD_ADD_IP_TO_BLACKLIST = "/usr/sbin/ipset add ip_blacklist";

    /** Command to create the blacklist in ipset */
    public static final String CMD_CREATE_BLACKLIST_IPSET = "/usr/sbin/ipset create ip_blacklist hash:ip";

    /** String variable to hold ipset version command */
    public static final String CMD_IPSET_VERSION = "/usr/sbin/ipset -v";

    /** Command to Retrieve ACS url from tr69 logs */
    public static final String CMD_TO_GET_ACS_URL_TR69_LOGS = "cat /rdklogs/logs/TR69log.txt.0 | grep CcspManagementServer";

    /** Command to get first column value from route table */
    public static final String CMD_ROUTE_ROW_FIRST_COLUMN = "/sbin/route | awk '{print $1}'";

    /** Default inet address pattern from log */
    public static final String INET_V4_ADDRESS_PATTERN = "inet addr:\\s*(\\d+.\\d+.\\d+.\\d+)\\s*Bcast:(\\d+.\\d+.\\d+.\\d+)";

    /** Constant to store AutoReboot.sh script */
    public static final String AUTO_REBOOT_SH = "AutoReboot.sh";

    /** Command to get SNMP enabled status IP table 10161 */
    public static final String CMD_IPTABLES_SAVE_GREP_10161 = CMD_TO_GET_IPTABLE + " | grep 10161";

    /** Command to renew IP in Windows client */
    public static final String CMD_TO_RENEW_IP_IN_WINDOWS = "ipconfig /renew";

    /** String to store port 10161 */
    public static final String PORT_10161 = "10161";

    /** String to store port 10163 */
    public static final String PORT_10163 = "10163";

    /** Command to get SNMP enabled status IP table 10161 */
    public static final String CMD_IP6TABLES_SAVE_GREP_10161 = CMD_TO_GET_IPV6TABLE + " | grep " + PORT_10163;

    /** String value Head */
    public static final String CMD_HEAD = "head";

    /** Argument for ls command */
    public static final String CMD_TAG_FOR_LS = "-la";

    /** Command to search 'GREP' */
    public static final String CMD_GREP = "grep ";

    /** File location for wifi monitor debug under /nvram directory */
    public static final String FILE_WIFI_MON_DBG = "/nvram/wifiMonDbg";

    /** Command to renew IP in Linux client */
    public static final String CMD_TO_RENEW_IP_CLIENT_INTERFACE_IN_LINUX = "sudo dhclient -v <<INTERFACE>>";

    /** File location for wifi monitor log under /tmp directory */
    public static final String FILE_WIFI_MON = "/tmp/wifiMon";

    /** Command to release IP in Linux client */
    public static final String CMD_TO_RELEASE_IP_CLIENT_INTERFACE_IN_LINUX = "sudo dhclient -r <<INTERFACE>>";

    /** Command to get bridgemode status */
    public static final String GET_BRIDGE_STATUS = "sysevent get bridge_mode";

    /** Command to get lan status */
    public static final String GET_LAN_STATUS = "sysevent get lan-status";

    /** Command to get ipv4_wan subnet */
    public static final String GET_WAN_SUBNET = "sysevent get ipv4_wan_subnet";

    /** Command to get ipv4 link */
    public static final String GET_IPV4_LINK_STATUS = "sysevent get current_ipv4_link_state";

    /** Command to ping erouter */
    public static final String PING_EROUTER = "ping -c 2 -I erouter0 www.yahoo.com";

    /** Command to show brctl */
    public static final String BRCTL_SHOW = "/usr/sbin/brctl show";

    /** Command Constants for ip route */
    public static final String CMD_ROOT_IP_ROUTE = "/sbin/ip route";
    
    /** ifconfig command to check Voice Interface */
    public static final String CMD_IFCONFIG_MTA0 = "/sbin/ifconfig mta0";

    /** command to check dibbler client version */
    public static final String CMD_DIBBLER_CLIENT_VERSION = "/usr/sbin/dibbler-client -version";
    
    /** command to check dibbler server version */
    public static final String CMD_DIBBLER_SERVER_VERSION = "/usr/sbin/dibbler-server -version";
    
    /** Command to hold the filter */
    public static final String CMD_NS_LOOKUP_OPEN_DNS_COM = "nslookup -type=txt which.opendns.com";
    
    /** Command to trigger reboot by hard lockup  */
    public static final String CMD_FOR_HARD_LOCKUP = "/sbin/modprobe hard_lockup";
    
    /** Command to trigger reboot by soft lockup  */
    public static final String CMD_FOR_SOFT_LOCKUP = "/sbin/modprobe soft_lockup";
    
    /** File path for /nvram/6/reset_reason.log */
    public static final String FILE_PATH_RESET_REASON_LOG = "/nvram/6/reset_reason.log";
    
    /** File path for /rdklogs/logs/resetinfo.txt.0  */
    
    public static final String FILE_PATH_RESET_INFO_TXT = "/rdklogs/logs/resetinfo.txt.0";
    
    public static final String CMD_PING_FOR_WINDOWS = "ping -n";

    public static final String CMD_PING_FOR_LINUX = "ping -c";
    
	/** List of executable binaries */
	public static final List<String> EXECUTABLE_BUSYBOX_UTILS = new ArrayList<String>() {
		{
			add("/usr/bin/du");
			add("/bin/df");
			add("/usr/bin/top -n -1");
			add("/usr/bin/uptime");
			add("/bin/touch");
			//add("/bin/dmesg | head  -20");

		}
	};
	
    /** Command constant ls -l */
    public static final String CMD_LS_L = "ls -l ";
    
    /** Process name busybox */
    public static final String PROCESS_BUSY_BOX = "busybox.nosuid";
    
    /** Command to check whether tar is installed */
    public static final String CMD_TAR_CHECK = "which tar";

    /** Command group logs to tar in home path */
    public static final String CMD_TO_TAR_RDKLOG_FILE = "tar -zcvf /nvram/<REPLACE> /rdklogs/logs/";

    /** Command untar in given path */
    public static final String CMD_TO_UNTAR_GIVEN_FILE = "tar -C <REPLACE> -xvf <<VALUE>>";

    /** Command constant for create/make directory */
    public static final String CMD_MKDIR = "mkdir ";
    
    /** Command to get ca-chain.cert.pem */
    public static final String CMD_TO_GET_CA_CHAIN_CERT_PEM = "/usr/bin/openssl x509 -enddate -noout -in /tmp/lnf/certs/ca-chain.cert.pem";

    /** Command to get radiussrv.cert.pem */
    public static final String CMD_TO_GET_RADIUSSRV_CERT_PEM = "/usr/bin/openssl x509 -enddate -noout -in /tmp/lnf/certs/radiussrv.cert.pem";

    /** Command to remove files/Directory */
    public static final String CMD_TO_REMOVE = "rm -rf";
    
	/** Command to get value from dhclient conf file using grep */
	public static final String CMD_DHCLIENT_GREP_VALUE = "cat /etc/dhcp/dhclient.conf | grep <<VALUE>>";
	
	/** Command to replace value in dhclient file */
	public static final String CMD_TO_REPLACE_VALUE_DHCLIENT_CONF = "sed -i 's/<<VALUERETRIEVED>>/<<VALUEPARAMETER>>/g' /etc/dhcp/dhclient.conf";

    /** Linux command to do revstbssh Xi devices. */
    public static final String CMD_SUDO_REVERSE_SSH_RDKB = "sudo -S revstbssh_rdkb";

    /** Command Constants for Reverse SSH */
    public static final String CMD_REVERSE_SSH = "sh ~/.bashrc;sudo -S revstbssh";
    
    /** Command to get 2.4GHz wifi client max tx rx rate */
    public static final String CMD_MAX_TX_RX_CLIENT_1 = "cat /rdklogs/logs/wifihealth.txt | grep -i max | grep -i clients_1 | tail -2";

    /** Command to run aphealth script */
    public static final String CMD_RUN_APHEALTH_SCRIPT = "sh /usr/ccsp/wifi/aphealth_log.sh ";
    
    /** Command to remove wifi health log */
    public static final String CLEAR_WIFI_HEALTH_LOG = "echo \"\" > /rdklogs/logs/wifihealth.txt";
    
    /** startTunnel.sh file location */
    public static final String FILE_START_TUNNEL_SSH = "/lib/rdk/startTunnel.sh";
    
    /** command toapply the static IP configuration */
    public static final String COMMAND_APPLY_STATIC_IP_CONF = "ccsp_bus_client_tool eRT setv Device.X_CISCO_COM_TrueStaticIP.ConfigApply bool true";

    /** command to get the wan ip address set in script */
    public static final String COMMAND_GET_STATIC_IP_ADDRESS = "cat /var/cliconfig.txt | grep \"wan_ip_address\"";
    
    /** File path for Encrypted ManagementServer STUN Password stored in nvram */
    public static final String FILE_NVRAM_KEYS_MGMTSTUNCRPWDID = "/nvram/.keysMgmtCRSTUNPwdID";

    /** File path for Encrypted ManagementServer Password stored in nvram */
    public static final String FILE_NVRAM_KEYS_MGMTPWDID = "/nvram/.keys/MgmtPwdID";

    /** File path for Encrypted ManagementServer ConnectionRequestPassword stored in nvram */
    public static final String FILE_NVRAM_KEYS_MGMTCRPWDID = "/nvram/.keys/MgmtCRPwdID";
    
    /** String command for ftp connection */
    public static final String COMMAND_FTP_CONNECTION = "ftp <<IPV6ADDRESS>>";
    
    /** Command to retrieve the ip tables configurations */
    public static final String CMD_TO_GET_IPTABLES_VALUES = "cat /tmp/.ipt | grep -i";
    
    /** curl command with 20 secs timeout for Ipv6 */
    public static final String CMD_CURL_WITH_20_SECS_TIMEOUT_IPV6 = "curl --head -g -6 ";

    /** Command to get from syscng.db file **/
    public static final String CMD_FOR_SYSCFG_GET = "syscfg get ";
    
    /** File name for /etc/ONBOARD_LOGGING_ENABLE **/
    public static final String FILE_ETC_ONBOARD_LOGGING_ENABLE = "/etc/ONBOARD_LOGGING_ENABLE";
    
    /** File name for /nvram/.device_onboarded **/
    public static final String FILE_NVRAM_DEVICE_ONBOARDED = "/nvram/.device_onboarded";
        
    /** Command Constants for get onboard log creation time */
    public static final String CMD_GET_ONBOARD_LOGS_TIME = "ls -ltr /nvram2/onboardlogs";

    /** File name /nvram2/onboardlogs/OnBoardingLog*.txt.0 */
    public static final String FILE_NVRAM_ONBOARDLOGS_ONBOARDINGLOG = "/nvram2/onboardlogs/OnBoardingLog*.txt.0";
    
    /** Process name util linux utility */
    public static final String UTIL_LINUX = "util-linux";
    
    /** Command to delete the WiFi Packet Capture File */
    public static final String CMD_REMOVE_WIFI_PACKET_CAPURE = "sudo rm -f /tmp/rdkb_test_pc.cap /root/packet_capture_script.sh";

    
    /** List of executable binaries */
    public static final List<String> EXECUTABLE_BINARY_LIST = new ArrayList<String>() {
	{
	    add("/bin/dmesg");
	    add("/bin/kill");
	    add("/bin/more");
	    add("/bin/mount");
	    add("/bin/umount");
	    add("/sbin/swapon");
	    add("/sbin/swapoff");
	    add("/sbin/fsck");
	    add("/sbin/hwclock");
	    add("/usr/bin/chrt");
	    add("/usr/bin/eject");
	    add("/usr/bin/flock");
	    add("/usr/bin/hexdump");
	    add("/usr/bin/logger");
	    add("/usr/bin/mesg");
	    add("/usr/bin/renice");
	    add("/usr/bin/setsid");
	    add("/sbin/losetup");
	    add("/sbin/fsck.minix");
	}
    };
    
    /** Log file location /rdklogs/logs/radiusauthd.log */
    public static final String FILE_RADIUSAUTHD_LOG = "/rdklogs/logs/radiusauthd.log";

    /** File name to get the Raspberry pi device model */
    public static final String FILE_RPI_DEVICE_MODEL = "/proc/device-tree/model";

    /** RPI configuration files in VM Location */
    public static final String RPI_FILES_IN_VM_LOCATION = "/home/svccpeqa01/RPI/";

    /** File name wpa supplicant conf in RPI device */
    public static final String FILE_WPA_SUPPLICANT_CONF = "wpa_supplicant.conf";

    /** file path in RPI device /etc/wpa_supplicant/ */
    public static final String PATH_WPA_SUPPLICANT = "/etc/wpa_supplicant/";

    /** file path in RPI device for certificates /etc/wpa_supplicant/certs/ */
    public static final String PATH_CERTIFICATE = "/etc/wpa_supplicant/certs/";

    /** file name xi5client key pem in RPI device */
    public static final String FILE_XI5_CLIENT_KEY = "xi5client.key.pem";

    /** file name xi5client certificate pem in RPI device */
    public static final String FILE_XI5_CLIENT_CERT = "xi5client.cert.pem";

    /** file name ca chain certificate in RPI device */
    public static final String FILE_CA_CHAIN_CERT = "ca-chain.cert.pem";

    /** command constant for overwriting file content */
    public static final String CMD_SED = "sed -i 's/";

    /** sudo command */
    public static final String CMD_SUDO = "sudo ";
    
    /** Command echo */
    public static final String CMD_ECHO = "echo ";
    
    /** Command operator */
    public static final String CMD_REDIRECTION = " >> ";
    
    /** rwx Permission to Users */
    public static final String CHMOD_755_VALUE = "755 ";
    
    /** Command remove */
    public static final String CMD_REMOVE = "rm ";
    
    /** Command constant for moving file/directory */
    public static final String CMD_MV = "mv ";
    
    /** Command to get ntpd process details */
    public static final String CMD_TO_GET_NTPD_PROCESS = "ps | grep -i ntpd | grep -v grep";

    /** Process details for ntpd in Arm side */
    public static final String PROCESS_DETAILS_NTPD = "ntpd -c /tmp/ntp.conf";
    
    /** Command to bring the WiFi Packet Monitoring interface down */
    public static final String CMD_MONITOR_INTERFACE_DOWN = "sudo ifconfig mon0 down";

    /** Command to bring the WiFi Packet Monitoring interface up */
    public static final String CMD_MONITOR_INTERFACE_UP = "sudo ifconfig mon0 up";
    
    /** iwconfig Command */
    public static final String CMD_IWCONFIG_MON0 = "sudo iwconfig mon0";
    
    /** sh Command */
    public static final String CMD_SH = "sh ";

    /** iw dev Command */
    public static final String CMD_IW_DEV = "sudo iw dev";

    /** iw phy Command to retrieve the information */
    public static final String CMD_IW_PHY_INFO = "sudo iw phy phy0 info";

    /** iw phy Command to add WiFi Packet Monitoring Interface */
    public static final String CMD_IW_PHY_ADD_MON0 = "sudo iw phy phy0 interface add mon0 type monitor";

    /** iw dev Command to delete interface */
    public static final String CMD_IW_DEL_INTERFACE = "sudo iw dev wlp4s0 del";

    /** Command to retrieve the WiFi Packet Monitoring interface */
    public static final String CMD_MONITOR_INTERFACE_NAME = "sudo iwconfig 2>&1 | grep -i 'Mode:Monitor'";
    
    /** Shell Script File that captures the WiFi Packets */
    public static final String FILE_PACKET_CAPTURE_SCRIPT = "packet_capture_script.sh";

    /** Command to check if file exists - head part */
    public static final String CMD_FILE_EXISTS_HEAD = "if [ -f  ";

    /** Command to check if file exists - tail part */
    public static final String CMD_FILE_EXISTS_TAIL = " ] ; then echo \"true\" ; else echo \"false\" ; fi";
    
    /** Command/ Process TCPDUMP */
    public static final String CMD_TCPDUMP = "tcpdump";
    
    /** Command to read the captured WiFi Packets */
    public static final String CMD_READ_CAPTURE_WIFI_PACKETS = "sudo tcpdump -r /tmp/rdkb_test_pc.cap | grep -i ";
    
    /** Log file /rdklogs/logs/ETHAGENTLog.txt.0 */
    public static final String FILE_ETH_AGENT_LOG = "/rdklogs/logs/ETHAGENTLog.txt.0";
    
    /** Command to get ethernet telemetry log for client */
    public static final String FILE_ETH_TELEMETRY_TXT = "/rdklogs/logs/eth_telemetry.txt";
    
    /** Command Constants for linux ping for ipv6 address */
    public static final String CMD_PING_FOR_LINUX_IPV6 = "ping6 -c";
    
    /** LM.txt.0 file location */
    public static final String LOG_FILE_LM = "/rdklogs/logs/LM.txt.0 ";

    public enum QUALCOM_FILES {
	ATF_BIN("/lib/firmware/AR9888/hw.2/atf.bin"),
	UTF_BIN("/lib/firmware/AR9888/hw.2/utf.bin"),
	OTP_BIN("/lib/firmware/AR9888/hw.2/otp.bin"),
	BD3GLD_BIN("/lib/firmware/AR9888/hw.2/boardData_3_QC98XX_xb141_gld.bin"),
	BD2GLD_BIN("/lib/firmware/AR9888/hw.2/boardData_2_QC98XX_xb143_gld.bin"),
	BD2CUSGLD_BIN("/lib/firmware/AR9888/hw.2/boardData_2_QC98XX_cus223_523_gld.bin"),
	FAKE_BIN("/lib/firmware/AR9888/hw.2/fakeBoardData_AR6004_bak.bin"),
	FAKEBOARD("/lib/firmware/AR9888/hw.2/fakeBoardData_AR6004.bin");

	private String file;

	public String getFile() {
	    return file;
	}

	public void setFile(String file) {
	    this.file = file;
	}

	QUALCOM_FILES(String file) {
	    this.file = file;
	}

    }

    public enum ATOMSYNC_QUALCOM_FILES {
	BISTIFY_BIN("/lib/firmware/AR9888/hw.2/bistify.dbg"),
	SPORT_BIN("/lib/firmware/AR9888/hw.2/serialport.bin"),
	OTP_BIN("/lib/firmware/AR9888/hw.2/otp_test.bin"),
	ROM_BIN("/lib/firmware/AR9888/hw.2/fw.rom.bin"),
	RAM_BIN("/lib/firmware/AR9888/hw.2/fw.ram.bin"),
	WALTEST_BIN("/lib/firmware/AR9888/hw.2/waltest.bin"),
	ATF_BIN("/lib/firmware/AR9888/hw.2/atf.bin"),
	UTF_BIN("/lib/firmware/AR9888/hw.2/utf.bin"),
	EPPING_BIN("/lib/firmware/AR9888/hw.2/endpointping.bin");

	private String file;

	public String getFile() {
	    return file;
	}

	public void setFile(String file) {
	    this.file = file;
	}

	ATOMSYNC_QUALCOM_FILES(String file) {
	    this.file = file;
	}

    }

    /** Constant to hold Atom removal files */
    public enum ATOM_REMOVAL_FILES {
	EZIPUPDATE("/usr/bin/ez-ipupdate"),
	DCA_UTILITY("/rdklogger/dca_utility.sh"),
	USBHIDDUMP("/usr/bin/usbhid-dump"),
	DCASPLUNKUPLOAD("/rdklogger/dcaSplunkUpload.sh"),
	DCMSCRIPT("/rdklogger/DCMscript.sh"),
	ESTSECUREWRAPPER("/usr/bin/testsecurewrapper"),
	LTIME("/usr/bin/LTime"),
	STARTTUNNEL("/rdklogger/startTunnel.sh"),
	RFC_REFRESH("/rdklogger/rfc_refresh.sh"),
	GETPARTNERID("/rdklogger/getpartnerid.sh"),
	GETACCOUNTID("/rdklogger/getaccountid.sh"),
	DCMCRONRESCHEDULE("/rdklogger/DCMCronreschedule.sh"),
	TELEMETRYEVENTLISTENER("/rdklogger/telemetryEventListener.sh"),
	IPTABLES_CONTAINER("/rdklogger/iptables_container.sh"),
	APPLY_PARTNER_CUSTOMIZATION("/rdklogger/apply_partner_customization.sh"),
	XHSSCRIPT("/rdklogger/xhsScript.sh"),
	GETIPV6_CONTAINER("/rdklogger/getipv6_container.sh"),
	RFC("/rdklogger/rfc.sh"),
	GETIP_FILE("/rdklogger/getip_file.sh"),
	LOG_TIMESTAMP("/rdklogger/log_timestamp.sh"),
	FILEUPLOADRANDOM("rdklogger/fileUploadRandom.sh"),
	WAN_SSH("/rdklogger/wan_ssh.sh"),
	EXPECT_CREATE_USERSPACE("/usr/bin/expect_create_userspace"),
	EXPECT_CREATE_NAT("/usr/bin/expect_create_nat"),
	EXPECT_CREATE("/usr/bin/expect_create"),
	CONNTRACK_FILTER("/usr/bin/conntrack_filter"),
	CONNTRACK_MASTER("/usr/bin/conntrack_master"),
	EXPECT_GET("/usr/bin/expect_get"),
	CONNTRACK_GET("/usr/bin/conntrack_get"),
	EXPECT_DELETE("/usr/bin/expect_delete"),
	CONNTRACK_GRP_CREATE("/usr/bin/conntrack_grp_create"),
	CONNTRACK_CREATE_NAT("/usr/bin/conntrack_create_nat"),
	CONNTRACK_UPDATE("/usr/bin/conntrack_update"),
	CONNTRACK_CREATE("/usr/bin/conntrack_create"),
	CONNTRACK_DUMP_FILTER("/usr/bin/conntrack_dump_filter"),
	CTEXP_EVENTS("/usr/bin/ctexp_events"),
	CONNTRACK_DELETE("/usr/bin/conntrack_delete"),
	EXPECT_EVENTS("/usr/bin/expect_events"),
	CONNTRACK_EVENTS("/usr/bin/conntrack_events"),
	CONNTRACK_DUMP("/usr/bin/conntrack_dump"),
	EXPECT_DUMP("/usr/bin/expect_dump"),
	EXPECT_FLUSH("/usr/bin/expect_flush"),
	CONNTRACK_FLUSH("/usr/bin/conntrack_flush"),
	SERVICE_DHCP("/usr/bin/service_dhcp"),
	SERVICE_MULTINET_EXEC("/usr/bin/service_multinet_exec"),
	APPLY_SYSTEM_DEFAULTS("/usr/bin/apply_system_defaults"),
	SERVICE_IPV6("/usr/bin/service_ipv6"),
	SERVICE_WAN("/usr/bin/service_wan"),
	SERVICE_UDHCPC("/usr/bin/service_udhcpc"),
	DHCP_PROXY("/usr/bin/dhcp_proxy"),
	SERVICE_ROUTED("/usr/bin/service_routed"),
	NFQ_HANDLER("/usr/bin/nfq_handler"),
	SYSEVENTD_PROXY("/usr/bin/syseventd_proxy"),
	NEWHOST("/usr/bin/newhost"),
	PMON("/usr/bin/pmon"),
	EXECUTE_DIR("/usr/bin/execute_dir"),
	UTCMD("/usr/bin/utcmd"),
	INIT_01("/usr/bin/01_init"),
	WAN_IPV6_03("/usr/bin/03_wan_ipv6"),
	BYOI_04("/usr/bin/04_byoi"),
	BYOI_GAP_04("/usr/bin/04_byoi_gap"),
	IPV6_02("/usr/bin/02_ipv6"),
	QOS_15("/usr/bin/15_qos"),
	BOOTSTRAP_DNS_10("/usr/bin/10_bootstrap_dns"),
	XHS_15("/usr/bin/15_xhs"),
	DNSFORWARDER_15("/usr/bin/15_dnsforwarder"),
	MCASTSNOOPER_10("/usr/bin/10_mcastsnooper"),
	NTPCLIENT_10("/usr/bin/10_ntpclient"),
	FACTORYDEFAULT_20("/usr/bin/20_factorydefault"),
	CISCOCONNECT_20("/usr/bin/20_ciscoconnect"),
	GWRESET_20("/usr/bin/20_gwreset"),
	SWITCHPMON_15("/usr/bin/15_switchpmon"),
	MANAGED_02("/usr/bin/02_managed"),
	SYSTEM_01("/usr/bin/01_system"),
	FPM_10("/usr/bin/10_fpm"),
	WLAN_02("/usr/bin/02_wlan"),
	LAN_02("/usr/bin/02_lan"),
	FTP_SERVER_15("/usr/bin/15_ftp_server"),
	IGD("/usr/bin/IGD"),
	IGD_15("/usr/bin/15_igd"),
	FIREWALL("/usr/bin/firewall"),
	GENFWLOG("/usr/bin/GenFWLog"),
	FIREWALL_10("/usr/bin/10_firewall"),
	IPV4_02("/usr/bin/02_ipv4"),
	HOTSPOT_15("/usr/bin/15_hotspot"),
	DHCPV6_SERVER_15("/usr/bin/15_dhcpv6_server"),
	WAN_02("/usr/bin/02_wan"),
	LANHANDLER_02("/usr/bin/02_lanHandler"),
	DHCPV6_CLIENT_15("/usr/bin/15_dhcpv6_client"),
	MCASTPROXY_10("/usr/bin/10_mcastproxy"),
	CCSPHS_15("/usr/bin/15_ccsphs"),
	MLDPROXY_10("/usr/bin/10_mldproxy"),
	DDNSCLIENT_15("/usr/bin/15_ddnsclient"),
	POTD_26("/usr/bin/26_potd"),
	MISC_15("/usr/bin/15_misc"),
	XDNS_09("/usr/bin/09_xdns"),
	NTPD_10("/usr/bin/10_ntpd"),
	PARODUS_02("/usr/bin/02_parodus"),
	FORWARDING_02("/usr/bin/02_forwarding"),
	BRIDGE_02("/usr/bin/02_bridge"),
	COSA_33("/usr/bin/33_cosa");

	private String file;

	public String getFile() {
	    return file;
	}

	public void setFile(String file) {
	    this.file = file;
	}

	ATOM_REMOVAL_FILES(String file) {
	    this.file = file;
	}

    }

    /** Constant to hold Arm removal files across platforms */
    public enum ARM_REMOVAL_FILES {
	APPLY_PARTNER_CUSTOMIZATION("/rdklogger/apply_partner_customization.sh"),
	DCA_UTILITY("/rdklogger/dca_utility.sh"),
	DCASPLUNKUPLOAD("/rdklogger/dcaSplunkUpload.sh"),
	DCMCRONRESCHEDULE("/rdklogger/DCMCronreschedule.sh"),
	DCMSCRIPT("/rdklogger/DCMscript.sh"),
	GETACCOUNTID("/rdklogger/getaccountid.sh"),
	GETPARTNERID("/rdklogger/getpartnerid.sh"),
	INITZRAM("/rdklogger/init-zram.sh"),
	RFC_REFRESH("/rdklogger/rfc_refresh.sh"),
	STARTTUNNEL("/rdklogger/startTunnel.sh"),
	TELEMETRYEVENTLISTENER("/rdklogger/telemetryEventListener.sh"),
	UTILS("/rdklogger/utils.sh"),
	WAN_SSH("/rdklogger/wan_ssh.sh");

	private String file;

	public String getFile() {
	    return file;
	}

	public void setFile(String file) {
	    this.file = file;
	}

	ARM_REMOVAL_FILES(String file) {
	    this.file = file;
	}
    }
    
    /** File path for dcm processing log file */
    public static final String LOG_FILE_DCMPROCESSING = "/rdklogs/logs/dcmProcessing.log";

    
}

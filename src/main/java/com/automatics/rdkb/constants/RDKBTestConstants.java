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

public class RDKBTestConstants {

    /** Enum to get the WiFi access band */
    public enum WiFiFrequencyBand {

	WIFI_BAND_2_GHZ("2GHz"),

	WIFI_BAND_5_GHZ("5GHz");

	private String frequencyBand;

	private WiFiFrequencyBand(String frequencyBand) {
	    this.frequencyBand = frequencyBand;
	}

	public String getValue() {
	    return frequencyBand;
	}
    }
    
    /** Constant for number 200 */
    public static final int CONSTANT_200 = 200;
    
    /**
     * XRE Test Constants for Handling Closed Caption Service Descriptor
     */
    /** Constant to denote the Pre-Condition Error */
    public static final String PRE_CONDITION_ERROR = "PRE-CONDITION ERROR: ";
    
    /** Single space character. */
    public static final String SINGLE_SPACE_CHARACTER = " ";
    

    /** One second in millisecond representation. */
    public static final long ONE_SECOND_IN_MILLIS = 1000;
    
    /** One Minute in millisecond representation. */
    public static final long ONE_MINUTE_IN_MILLIS = 60 * ONE_SECOND_IN_MILLIS;
    
    /** Two minutes in milli seconds. */
    public static final long TWO_MINUTES = 2 * 60 * 1000;
    
    /** Three minutes in milli seconds. */
    public static final long THREE_MINUTES = 3 * 60 * 1000;
    
    /** Ten minutes in milli seconds. */
    public static final long FIVE_MINUTES = 5 * 60 * ONE_SECOND_IN_MILLIS;
    
    /** Eight minute in millisecond representation. */
    public static final long EIGHT_MINUTE_IN_MILLIS = 8 * ONE_MINUTE_IN_MILLIS;
    
    /** 15 minutes in milliseconds. */
    public static final long FIFTEEN_MINUTES_IN_MILLIS = 15 * ONE_MINUTE_IN_MILLIS;
    
    /** Fifteen seconds in millisecond representation. */
    public static final long FIFTEEN_SECONDS_IN_MILLIS = 15 * ONE_SECOND_IN_MILLIS;
    
    /**
     * Property key for list of jump server IP address for prod build testing.
     */
    public static final String PROP_KEY_JUMP_SERVER_IP_ADDRESS_LIST_PROD_TESTS = "jump.server.ip.address.list.prod.test";
    
    /** grep command. */
    public static final String GREP_COMMAND = "grep -i ";
    
    /** Empty string. */
    public static final String EMPTY_STRING = "";
    
    /** Text for "false". */
    public static final String FALSE = "false";
    
    /** Text for "true". */
    public static final String TRUE = "true";
    
    /** DMCLI Command to get value for particular parameter. */
    public static final String CMD_DMCLI_GET_VALUE = "dmcli eRT getv";
    
    /** New line delimiter. */
    public static final String DELIMITER_NEW_LINE = "\n";
    
    /** Regular expression to get DMCLI command value. */
    public static final String REGULAR_EXPRESSION_DMCLI_COMMAND_VALUE = "value:\\s*(.*)";
    
    /** Constant for number 60 */
    public static final int CONSTANT_60 = 60;
    
    /** String to hold the value - 20 */
    public static final int CONSTANT_20 = 20;
    
    /** String to hold the double quote */
    public static final String DOUBLE_QUOTE = "\"";
 
    /** String to hold the NO_SUCH_FILE_OR_DIRECTORY */
    public static final String NO_SUCH_FILE_OR_DIRECTORY = "No such file or directory";
    
    /** Constants for VOD Session Setup Test **/
    /** String constant for IPv4 **/
    public static final String IP_VERSION4 = "IPv4";
    
    /** String constant for IPv6 **/
    public static final String IP_VERSION6 = "IPv6";
    
    /** Constants representation for Semicolon */
    public static final String SEMI_COLON = ";";

    /** Constant for number 3 */
    public static final int CONSTANT_3 = 3;
    
    /** Constant for number 4 */
    public static final int CONSTANT_4 = 4;
    
    /** Constant for number 5 */
    public static final int CONSTANT_5 = 5;
    
    /** Constant for number 6 */
    public static final int CONSTANT_6 = 6;
    
    /** Constant for number 15 */
    public static final int CONSTANT_15 = 15;
    
    /** Constant for Blank */
    public static final String BLANK = "";
    
    /** WebPA Response message when parameter value is invalid */
    public static final String INVALID_PARAMETER_VALUE = "Invalid parameter value";
    
    /** 20 minutes in milliseconds. */
    public static final long TWENTY_MINUTES_IN_MILLIS = 20 * ONE_MINUTE_IN_MILLIS;
    /** TR69 command to get the current image name. */
    public static final String TR69_RDKB_CURRENT_IMAGE_NAME = "Device.DeviceInfo.X_CISCO_COM_FirmwareName";
    
    /** Constant holding the location of swupdate.conf file. */
    public static final String SOFTWARE_UPDATE_CONF_FILE = "/opt/swupdate.conf";
    
    /** The linux grep command to get image name from version.txt file. */
    public static final String CMD_GREP_IMAGE_NAME_FROM_VERSION_FILE = "grep \"imagename[:=]\" /version.txt";
    
    /** The constant holding the image version */
    public static final String[] IMAGE_VERSION = { "-signed", "-unsigned", "_svn_d30", "_svn_d31", "_signed", "-sgnd",
	    "_d30", "_d31", ".bin.ccs", ".bin", "_sgnd" };
    
    /** Automation execution mode SP (Standard production) */
    public static final String EXECUTION_MODE_SP = "SP";
    
    /** Automation execution mode GRAM */
    public static final String EXECUTION_MODE_GRAM = "GRAM";
    
    /** Automation build appender GRAM */
    public static final String GRAM_APPENDER = "_gram";
    
    /** snmp command - Response Success. */
    public static final String SNMP_RESPONSE_SUCCESS = "Success";
    
    /** Commandd to get the uptime **/
    public static final String CMD_UPTIME = "uptime";
    
    /** Constant to hold the command not found error **/
    public static final String COMMAND_NOT_FOUND_ERROR = "command not found";
    
    /**
     * pattern to get the minutes from the box's uptime response if the box is up only for less than an hour
     */
    public static final String PATTERN_GET_MINUTES_FROM_UPTIME_RESPONSE = "\\s+(\\d+)\\s+min";

    /**
     * pattern o get the hurs and minutes when the box's uptime is more than an hour
     **/
    public static final String PATTERN_GET_HOURS_MINUTES_FROM_UPTIME_RESPONSE = "up\\s+(\\d+):(\\d+)\\S+\\s+";
    
    /**
     * pattern to get the no.of.days when the box's uptime is more than a day
     **/
    public static final String PATTERN_GET_DAYS_FROM_UPTIME_RESPONSE = "up\\s*(\\d+)\\s*+day";
    
    /** Constant for number 0 */
    public static final int CONSTANT_0 = 0;
    
    /** Property key reverse ssh jump server. */
    public static final String PROPERTY_REVERSE_SSH_JUMP_SERVER = "reversessh.jump.server";
    
    /** Props key for CI Server user ID. */
    public static final String PROP_KEY_CI_SERVER_USER_ID = "ci.server.user.id";

    /** Props key for CI Server password. */
    public static final String PROP_KEY_CI_SERVER_PASSWORD = "ci.server.password";
    
    /** The constant holding a command to ifconfig erouter. */
    public static final String IFCONFIG_EROUTER = "/sbin/ifconfig erouter0";

    /** Commandd to get the uptime from /proc/uptime **/
    public static final String PROC_CMD_UPTIME = "cat /proc/uptime";    

    /** Three minutes in seconds. */
    public static final long TWO_MINUTES_IN_SECONDS = 2 * 60;

    /** Prop key xconf url. */
    public static final String PROP_KEY_PROXY_XCONF_URL = "proxy.xconf.logupload.url";
    
    /** Pattern log url. */
    public static final String PATTERN_FOR_LOG_UPLOAD_URL = "DCM_LOG_SERVER_URL=(\\S+)";
    
    /** 30 minutes in milliseconds. */
    public static final long THIRTY_MINUTES_IN_MILLIS = 30 * ONE_MINUTE_IN_MILLIS;
    
    /** Delimeter Hash. */
    public static final String DELIMITER_HASH = "#";
    
    /** Linux command to remove any folder. */
    public static final String CMD_REMOVE_DIR_FORCEFULLY = "rm -rf";
    
    /** To Replace */
    public static final String[] REPLACE = { "", "", "", "", "", "", "", "", "", "", "" };
    
    /** Command Constant for clearing file using echo **/
    public static final String CMD_ECHO_CLEAR = "echo > ";
    
    /** Dot operator **/
    public static final String DOT_OPERATOR = ".";
    
    /** Constant for storing STRING Zero */
    public static final String STRING_ZERO = "0";
    
    /** String to command 'kill -9' */
    public static final String KILL_9 = "kill -9 ";
    
    /** DeviceConfig IP Address Type - ESTB. */
    public static final String DEVICE_IP_ADDRESS_TYPE_ESTB = "ESTB";
    
}

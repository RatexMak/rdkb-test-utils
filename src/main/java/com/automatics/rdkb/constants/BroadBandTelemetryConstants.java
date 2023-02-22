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

public class BroadBandTelemetryConstants {

	/** Constant for the location of dcm.properties file in /etc folder */
	public static final String DCM_PROPERTIES_FILE_ETC_FOLDER = "/etc/dcm.properties";

	/** Command to list the dcm.properties in etc folder. */
	public static final String CMD_LIST_DCM_PROPERTIES_ETC_FOLDER = "ls -ltr /etc/ | grep -i  dcm.properties";

	/** Constant for storing string for Last reboot reasonin case of webpa reboot */
	public static final String LAST_REBOOT_REASON_FOR_WEBPA_REBOOT = "webpa-reboot";

	/** String to ci xconf url for telemetry2.0 */
	public static final String CI_XCONF_URL_TELEMTRY_2 = "telemetry.config.url";

	/** Command to get telemetry request details from telemetry2_0.txt.0 */
	public static final String CMD_GET_TELEMETRY_REQUEST_DETAILS_FOR_T2 = "cat "
			+ BroadBandTestConstants.FILE_PATH_TELEMETRY_2_0;

	/** Constant for location of dcmscript.log */
	public static final String DCMSCRIPT_LOG_FILE_LOGS_FOLDER = "cat /rdklogs/logs/dcmscript.log";

	/**
	 * String to store Successfully communaication successful in telemetry2_0.txt.0
	 */
	public static final String CMD_TO_STORE_T2_DOHTTPGET_SUCCESSFULLY = "T2:Telemetry XCONF communication success";

	/** DCM configuration download status from dcm script log */
	public static final String DCM_CONFIGURATION_DOWNLOAD_STATUS_DCMSCRIPT_LOG = "ret = 0 http_code: 200";

	/**
	 * Command for verifying cron settings using crontab - For non-atom sync
	 * available devices
	 */
	public static final String CMD_CRONTAB_NON_ATOM_SYNC_AVAILABLE = "crontab -l | grep -i \"dca\"";

	/**
	 * Command for verifying cron settings using crontab - For atom sync available
	 * devices
	 */
	public static final String CMD_CRONTAB_IS_ATOM_SYNC_AVAILABLE = "crontab -c /tmp/cron/ -l | grep dca";

	/** Endpoint for telemetry should include a single 30 second timeout */
	public static final String ENDPOINT_TIMEOUT = "connect-timeout 30 -m 30";

	/** Telemetry marker for WIFI_MAC_COUNT_TELEMETRY_MARKER */
	public static final String WIFI_MAC_COUNT_TELEMETRY_MARKER = "Total_2G_clients_split";

	/** Telemetry marker for WebPA reboot */
	public static final String TELEMETRY_STRING_WEBPA_REBOOT = "rdkb_rebootreason_split";

	/** Command to grep reboot reason from dcmscript log */
	public static final String CMD_GREP_REBOOT_REASON_WEBPA_LOG = "cat /rdklogs/logs/dcmscript.log | grep "
			+ TELEMETRY_STRING_WEBPA_REBOOT;

	   /**
     * Telemetry Profile enum with header, content, type and Polling frequency detail.
     * 
     * @author Praveenkumar Paneerselvam
     *
     */
    public enum BroadBandTelemetryProfile {

	TELEMETRY_TEST_PROFILE_1(
		"Telemetry1 TEST HEADER",
		"Telemetry1 TEST CONTENT",
		BroadBandCommandConstants.LOG_FILE_LM,
		0),
	TELEMETRY_TEST_PROFILE_2(
		"Telemetry2 TEST HEADER",
		"Telemetry2 TEST CONTENT",
		BroadBandCommandConstants.LOG_FILE_LM,
		0),
	TELEMETRY_TEST_PROFILE_3(
		"Telemetry3 TEST HEADER",
		"Telemetry3 TEST CONTENT",
		BroadBandCommandConstants.LOG_FILE_LM,
		1),
	TELEMETRY_TEST_PROFILE_4(
		"Telemetry4 TEST HEADER",
		"Telemetry4 TEST CONTENT",
		BroadBandCommandConstants.LOG_FILE_LM,
		2),
	TELEMETRY_TEST_PROFILE_5(
		"Telemetry5 TEST HEADER",
		"Telemetry5 TEST CONTENT",
		BroadBandCommandConstants.LOG_FILE_LM,
		3),
	TELEMETRY_TEST_PROFILE_6(
		"Telemetry6 TEST HEADER",
		"Telemetry6 TEST CONTENT",
		BroadBandCommandConstants.LOG_FILE_LM,
		4),
	TELEMETRY_TEST_PROFILE_7(
		"Telemetry7 TEST HEADER",
		"Telemetry7 TEST CONTENT",
		BroadBandCommandConstants.LOG_FILE_LM,
		5),
	TELEMETRY_TEST_PROFILE_8(
		"Telemetry8 TEST HEADER",
		"Telemetry8 TEST CONTENT",
		BroadBandCommandConstants.LOG_FILE_LM,
		6);

	private String telemetryProfileHeader = null;
	private String telemetryProfileContent = null;
	private String telemetryProfileType = null;
	private int telemetryProfilePollingFrequency = 0;

	/**
	 * Header name expected in dcmscript.log
	 * 
	 * @return the telemetryProfileHeader
	 */
	public String getTelemetryProfileHeader() {
	    return telemetryProfileHeader;
	}

	/**
	 * Profile content looked for in respective profileType
	 * 
	 * @return the telemetryProfileContent
	 */
	public String getTelemetryProfileContent() {
	    return telemetryProfileContent;
	}

	/**
	 * File name
	 * 
	 * @return the telemetryProfileType
	 */
	public String getTelemetryProfileType() {
	    return telemetryProfileType;
	}

	/**
	 * frequency of the marker for which it should be uploaded.
	 * 
	 * @return the telemetryProfilePollingFrequency
	 */
	public int getTelemetryProfilePollingFrequency() {
	    return telemetryProfilePollingFrequency;
	}

	/**
	 * Private constructor class.
	 * 
	 * @param telemetryProfileHeader
	 *            Header name expected in dcmscript.log
	 * @param telemetryProfileContent
	 *            Profile content looked for in respective profileType
	 * @param telemetryProfileType
	 *            File name
	 * @param telemetryProfilePollingFrequency
	 *            frequency of the marker for which it should be uploaded.
	 */
	private BroadBandTelemetryProfile(String telemetryProfileHeader, String telemetryProfileContent,
		String telemetryProfileType, int telemetryProfilePollingFrequency) {
	    this.telemetryProfileHeader = telemetryProfileHeader;
	    this.telemetryProfileContent = telemetryProfileContent;
	    this.telemetryProfileType = telemetryProfileType;
	    this.telemetryProfilePollingFrequency = telemetryProfilePollingFrequency;
	}
}
    
/** Telemetry marker for Dca count 1 */
public static final String TELEMETRY_PATTERN_FOR_DCA_COUNT_1 = "\\{\"Telemetry1 TEST HEADER\":\"\\d+\"\\},\\{\"Telemetry2 TEST HEADER\":\"\\d+\"\\}";

/** Telemetry marker for Dca count 2 */
public static final String TELEMETRY_PATTERN_FOR_DCA_COUNT_2 = "\\{\"Telemetry1 TEST HEADER\":\"\\d+\\\"},\\{\"Telemetry2 TEST HEADER\":\"\\d+\"\\},\\{\"Telemetry3 TEST HEADER\":\"\\d+\"\\}";

/** Telemetry marker for Dca count 3 */
public static final String TELEMETRY_PATTERN_FOR_DCA_COUNT_3 = "\\{\"Telemetry1 TEST HEADER\":\"\\d+\"\\},\\{\"Telemetry2 TEST HEADER\":\"\\d+\"\\},\\{\"Telemetry4 TEST HEADER\":\"\\d+\"\\}";

/** Telemetry marker for Dca count 4 */
public static final String TELEMETRY_PATTERN_FOR_DCA_COUNT_4 = "\\{\"Telemetry1 TEST HEADER\":\"\\d+\"\\},\\{\"Telemetry2 TEST HEADER\":\"\\d+\"\\},\\{\"Telemetry3 TEST HEADER\":\"\\d+\"\\},\\{\"Telemetry5 TEST HEADER\":\"\\d+\"\\}";

/** Telemetry marker for Dca count 5 */
public static final String TELEMETRY_PATTERN_FOR_DCA_COUNT_5 = "\\{\"Telemetry1 TEST HEADER\":\"\\d+\"\\},\\{\"Telemetry2 TEST HEADER\":\"\\d+\"\\},\\{\"Telemetry6 TEST HEADER\":\"\\d+\"\\}";

/** Telemetry marker for Dca count 6 */
public static final String TELEMETRY_PATTERN_FOR_DCA_COUNT_6 = "\\{\"Telemetry1 TEST HEADER\":\"\\d+\"\\},\\{\"Telemetry2 TEST HEADER\":\"\\d+\"\\},\\{\"Telemetry3 TEST HEADER\":\"\\d+\"\\},\\{\"Telemetry4 TEST HEADER\":\"\\d+\"\\},\\{\"Telemetry7 TEST HEADER\":\"\\d+\"\\}";

/** Telemetry marker for Dca count 7 */
public static final String TELEMETRY_PATTERN_FOR_DCA_COUNT_7 = "\\{\"Telemetry1 TEST HEADER\":\"\\d+\"\\},\\{\"Telemetry2 TEST HEADER\":\"\\d+\"\\},\\{\"Telemetry8 TEST HEADER\":\"\\d+\"\\}";

/** Command to get CPU usage using top command */
public static final String CMD_TOP_TO_GET_CPU_USAGE = "top -bn1 | grep -i \"Cpu(s):\\|CPU:\" | cut -d'%' -f1 | cut  -d':' -f2";

/** Property key for default xconf logupload url */
public static final String PROP_KEY_DEFAULT_XCONF_LOGUPLOAD_URL = "default.xconf.logupload.url";

/** Constant for DCM_LOG_SERVER_URL property */
public static final String PROP_DCM_LOG_SERVER_URL = "DCM_LOG_SERVER_URL";

/** Get Log Server URL from dcm.properties */
public static final String CMD_GET_DCM_LOG_SERVER_URL = " | grep -i " + PROP_DCM_LOG_SERVER_URL;  

/** String to store SearchResult in telemetry2_0.txt.0 */
public static final String SEARCH_RESULT = "searchResult";

/** Telemetry marker forLog upload in Telemetry log */
public static final String SEARCHRESULT_LOG_UPLOAD_TELEMETRY2_MARKER = "(\\{\"searchResult\".*\\})";

/** Command to get telemetry request details */
public static final String CMD_GET_TELEMETRY_REQUEST_DETAILS = "cat /rdklogs/logs/dcmscript.log";

/** Telemetry marker for bootuptime_Ethernet_split */
public static final String TELEMETRY_MARKER_BOOTUPTIME_ETHERNET = "btime_eth_split";

/** Telemetry marker for bootuptime_moca_split */
public static final String TELEMETRY_MARKER_BOOTUPTIME_MOCA = "btime_moca_split";

/** Telemetry marker for bootuptime_webpa_split */
public static final String TELEMETRY_MARKER_BOOTUPTIME_WEBPA = "btime_webpa_split";

/** Json Parameter for erouter ipv6 in "dcmscript.log" file */
public static final String JSON_MARKER_IPV6_ADDRESS = "erouterIpv6"; 

/** Json Parameter for erouter ipv4 in "dcmscript.log" file */
public static final String JSON_MARKER_IPV4_ADDRESS = "erouterIpv4";

public enum TelemetryData {
	PROFILE,
	EROUTER_MAC_ADDRESS,
	EROUTER_IPV4,
	EROUTER_IPV6,
	VERSION,
	PARTNER_ID,
	TIME,
	BOOTUP_TIME_WEBPA,
	BOOTUP_TIME_ETHERNET,
	BOOTUP_TIME_MOCA;
    }

public enum XConfQueryParams {
ESTB_MACADDRESS,
ECM_MACADDRESS,
MODEL,
FIRMWARE_VERSION;
}

public static final String CMD_GET_TELEMETRY_SEARCHRESULTS_DETAILS = "cat /rdklogs/logs/dcmscript.log | grep searchResult";

}

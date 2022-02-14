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
    public static final String CMD_GET_TELEMETRY_REQUEST_DETAILS_FOR_T2 = "cat "+BroadBandTestConstants.FILE_PATH_TELEMETRY_2_0;
    
    /** Constant for location of dcmscript.log */
    public static final String DCMSCRIPT_LOG_FILE_LOGS_FOLDER = "cat /rdklogs/logs/dcmscript.log";
    
    /** String to store Successfully communaication successful in telemetry2_0.txt.0 */
    public static final String CMD_TO_STORE_T2_DOHTTPGET_SUCCESSFULLY = "T2:Telemetry XCONF communication success";
    
    /** DCM configuration download status from dcm script log */
    public static final String DCM_CONFIGURATION_DOWNLOAD_STATUS_DCMSCRIPT_LOG = "ret = 0 http_code: 200";
    
    /** Command for verifying cron settings using crontab - For non-atom sync available devices */
    public static final String CMD_CRONTAB_NON_ATOM_SYNC_AVAILABLE = "crontab -l | grep -i \"dca\"";
    
    /** Command for verifying cron settings using crontab - For atom sync available devices */
    public static final String CMD_CRONTAB_IS_ATOM_SYNC_AVAILABLE = "crontab -c /tmp/cron/ -l | grep dca";
    
}

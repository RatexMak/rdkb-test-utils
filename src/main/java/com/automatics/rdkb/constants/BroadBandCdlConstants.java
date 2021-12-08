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

public class BroadBandCdlConstants {

	/**
	 * configuration value for the current image name
	 */
	public static final String CONFIGURATION_CURRENT_IMAGE_NAME = "<current_image_name>";

	/**
	 * configuration value for the requested image name
	 */
	public static final String CONFIGURATION_REQUESTED_IMAGE_NAME = "<requested_image_name>";

	/**
	 * XCONF CDL accepted message format
	 */
	public static final String ACCEPTED_MESSAGE_FOR_XCONF_CODE_DOWNLOAD = "XCONF SCRIPT : Current image ("
			+ CONFIGURATION_CURRENT_IMAGE_NAME + ") and Requested image (" + CONFIGURATION_REQUESTED_IMAGE_NAME
			+ ") are different. Processing Upgrade/Downgrade";

	/**
	 * Constant for HTTP Download NOT successful
	 */
	public static final String XCONF_HTTP_DOWNLOAD_NOT_SUCCESSFUL = "XCONF SCRIPT : HTTP download NOT Successful";

	/**
	 * constant for No upgrade/downgrade required
	 */
	public static final String XCONF_CDL_UPGRADE_OR_DOWNGRADE_NOT_REQUIRED = "No upgrade/downgrade required";

	/**
	 * XCONF CDL accepted message format
	 */
	public static final String XCONF_CODE_DOWNLOAD_MESSAGE = "(?i)XCONF SCRIPT : Current image \\("
			+ CONFIGURATION_CURRENT_IMAGE_NAME + "\\) and Requested im[ga][ag]e \\("
			+ CONFIGURATION_REQUESTED_IMAGE_NAME + "\\) are different. Processing Upgrade/Downgrade";

	/** String Constant for .bin Extension */
	public static final String BIN_EXTENSION = ".bin";

	/**
	 * Constant for getting valid ping servers
	 */
	public static final String VALID_PING_SERVER_URL = "urltoresolvevalidpingservers";
	
    /**
     * HTTP success message to verify that the XCONF CDL is success
     */
    public static final String HTTP_SUCCESS_RESPONSE_FOR_XCONF_CDL = "XCONF SCRIPT : HTTP RESPONSE CODE is";
    
    /** Pattern to retrieve the XCONF Configuration */
    public static final String PATTERN_TO_RETRIEVE_XCONF_CONFIGURATION = "XCONF SCRIPT : HTTP RESPONSE CODE is[\\d\\s]*\\s*(\\{.*\\})\\s*";
    /**
     * last reboot reason after cdl
     */
    public static final String EXPECTED_LAST_REBOOT_REASON_STATUS_DIFD_CDL_VIA_WEBPA_REBOOT = "webpa-reboot";
    
    /**
     * last reboot reason after cdl
     */
    public static final String EXPECTED_LAST_REBOOT_REASON_STATUS_AFTER_CDL = "Software_upgrade";
    
    /** WebPA Parameter for Code Download URL */
    public static final String WEBPA_PARAM_CODE_DOWNLOAD_URL = "Device.DeviceInfo.X_RDKCENTRAL-COM_FirmwareDownloadURL";

    /** WebPA Parameter for Code Download Image Name */
    public static final String WEBPA_PARAM_CODE_DOWNLOAD_IMAGE_NAME = "Device.DeviceInfo.X_RDKCENTRAL-COM_FirmwareToDownload";

    /** Property Key for CDL server URL */
    public static final String PROP_KEY_CDL_SERVER_URL = "xconf.firmware.location.http";
    
    /** WebPA Parameter for Code Download Protocol */
    public static final String WEBPA_PARAM_CODE_DOWNLOAD_PROTOCOL = "Device.DeviceInfo.X_RDKCENTRAL-COM_FirmwareDownloadProtocol";

    /** Log Message Code Download URL */
    public static final String LOG_MESSAGE_CDL_URL = "URL is ";

    /** Log Message Code Download Image */
    public static final String LOG_MESSAGE_CDL_IMAGE_NAME = "FW DL is ";
    
    /** CM Log File */
    public static final String CM_LOG_TXT_0 = "/rdklogs/logs/CMlog.txt.0";
    
    /** WebPA Parameter for triggering CDL */
    public static final String WEBPA_PARAM_TRIGGER_CDL = "Device.DeviceInfo.X_RDKCENTRAL-COM_FirmwareDownloadNow";    

    /** Log Message Code Download Image */
    public static final String LOG_MESSAGE_CDL_TRIGGERED = "\"Image downloading triggered successfully\"";

    /** Log Message Code Download Not Allowed */
    public static final String LOG_MESSAGE_CDL_NOT_ALLOWED = "\"FW DL is not allowed for this image type\"";

    /** Log Message CDL with the Current FW */
    public static final String LOG_MESSAGE_CDL_WITH_CURRENT_FW = "\"Current FW is same, Ignoring request\"";
    
    /** CDL Status - Not Started **/
    public static final String CDL_STATUS_NOT_STARTED = "Not Started";

    /** CDL Status - In Progress **/
    public static final String CDL_STATUS_IN_PROGRESS = "In Progress";

    /** CDL Status - Completed **/
    public static final String CDL_STATUS_COMPLETED = "Completed";

    /** CDL Status - Failed **/
    public static final String CDL_STATUS_FAILED = "Failed";
    
}

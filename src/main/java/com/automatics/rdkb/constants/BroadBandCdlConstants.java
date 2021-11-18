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
}

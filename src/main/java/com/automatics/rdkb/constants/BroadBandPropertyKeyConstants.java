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

/**
 * This class will have the consants with key to get the value from Automatics
 * properties
 * 
 * @author anandam.s
 *
 */
public class BroadBandPropertyKeyConstants {
	/** The property key for partner ID Names */
	public static final String PROP_KEY_PARTNER_ID_LIST = "partner.id.list";

	/** The property key to get partner ID Prefix */
	public static final String PROP_KEY_DEFAULT_SSID_PREFIX = "default.ssid.prefix.";

	/** The property key for removing chars based on partner */
	public static final String PROP_KEY_PARTNER_CHARS_REMOVE = "remove.chars.partner.";

	/** The property key for wifi band for partner ID */
	public static final String PROP_KEY_WIFIBAND_PARTNERID = "wifiband.partner.id.";

	/** The property key for wifi band for partner ID */
	public static final String PROP_KEY_PARTNERID_DEVICE = "verify.partner.id.device";

	/** The property key for wifi operating standards of 2GHZ */
	public static final String PROP_KEY_DEVICE_OPERATING_STANDARDS_2GHZ = "operating.standars.2ghz.";

	/** The property key for wifi operating standards of 5GHZ */
	public static final String PROP_KEY_DEVICE_OPERATING_STANDARDS_5GHZ = "operating.standars.5ghz.";

	/** The property key for wifi operating channel bandwidth of 5GHZ */
	public static final String PROP_KEY_DEVICE_OPERATING_CHANNEL_BANDWIDTH_5GHZ = "operating.channel.bandwidth.5ghz.";

	/** The property key to hold parodus token server url */
	public static final String PROP_KEY_PARODUS_TOKEN_SERVER_URL = "parodus.token.server.url";

	public static final String SPLUNK_ENABLED = "is.splunk.enabled";

	public static final String PROP_KEY_SPLUNK_HOST = "splunk.host";

	public static final String PROP_KEY_SPLUNK_PORT = "splunk.port";

	public static final String PROP_KEY_SPLUNK_USER = "splunk.user";

	public static final String PROP_KEY_SPLUNK_PASSWORD = "splunk.password";

	/** property to get the latest openSSL version */
	public static final String CURRENT_OPENSSL_VERSION = "current.openssl.version";

	public static final String DEFAULT_MAX_RESET_COUNT = "default.max.reset.count";

	/** Verify prod xconf code download url */
	public static final String STB_PROP_PROD_CODE_DWLD_URL = "prod.xconf.codedownload.url";

	/** The property key for signed extensions. */
	public static final String PROP_KEY_SIGNED_EXTENSION = "build.signed.extension.";

	/** Constant to store partial property key for GA image */
	public static final String PARTIAL_PROPERTY_KEY_FOR_GA_IMAGE = "cdl.ga.image.";

	/** stb properties key for getting proxy xconf for rfc update settings url **/
	public static final String PROP_KEY_PROXY_XCONF_RFC_URL = "proxy.xconf.rfc.url";

	/** property to hold the prod build substring */
	public static final String PROP_PROD_BUILD_SUBSTRING = "prod.build.substring";
	
	/** key string to get latest dropbear version from properties file */
        public static final String LATEST_DROPBEAR_VERSION = "latest.dropbear.version";
    
        /** Property key reverse ssh jump server. */
        public static final String PROPERTY_REVERSE_SSH_JUMP_SERVER = "reversessh.jump.server";
        
        /** property to configure the non whitelisted jump server */
        public static final String PROPERTY_NON_WHITELISTED_JUMP_SERVER_IP = "nonwhitelisted.jumpserver.ip";
        
        /** String constant to get snmp reboot reason */
        public static final String PROP_KEY_REBOOT_REASON = "snmp.reboot.reason";
        
        /** The property key for 2ghz operating standards */
        public static final String LOGFILENAME_ATOM = "operatingstandards.2ghz.atom.";
        

}

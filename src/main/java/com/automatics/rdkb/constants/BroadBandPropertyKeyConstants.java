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
 * This class will have the consants with key to get the value from Automatics properties
 * 
 * @author anandam.s
 *
 */
public class BroadBandPropertyKeyConstants {
    /** The property key for partner ID Names */
    public static final String PROP_KEY_PARTNER_ID_LIST = "partner.id.list";

    /** The property key for syndication partner ID Names */
    public static final String PROP_KEY_SYNDICATION_PARTNER_ID_LIST = "syndicationpartner.id.list";

    /** The property key to get partner ID Prefix */
    public static final String PROP_KEY_DEFAULT_SSID_PREFIX = "default.ssid.prefix.";

    /** The property key for removing chars based on partner */
    public static final String PROP_KEY_PARTNER_CHARS_REMOVE = "remove.chars.partner.";

    /** The property key for wifi band for partner ID */
    public static final String PROP_KEY_WIFIBAND_PARTNERID = "wifiband.partner.id.";

    /** The property key for wifi band for partner ID */
    public static final String PROP_KEY_PARTNERID_DEVICE = "verify.partner.id.device";
    
    /** property to configure the invalid NTP server URL  */
    public static final String PROPKEY_FOR_INVALID_NTPHOST = "serverurl.invalid.ntp";
    
    /** property to configure the NTP server URL  */
    public static final String PROPKEY_FOR_NTPHOST = "serverurl.ntp";

    /** The property key for wifi operating standards of 2GHZ */
    public static final String PROP_KEY_DEVICE_OPERATING_STANDARDS_2GHZ = "operating.standars.2ghz.";

    /** The property key for wifi operating standards of 5GHZ */
    public static final String PROP_KEY_DEVICE_OPERATING_STANDARDS_5GHZ = "operating.standars.5ghz.";

    /** The property key for wifi operating channel bandwidth of 5GHZ */
    public static final String PROP_KEY_DEVICE_OPERATING_CHANNEL_BANDWIDTH_5GHZ = "operating.channel.bandwidth.5ghz.";

    /** The property key to hold parodus token server url */
    public static final String PROP_KEY_PARODUS_TOKEN_SERVER_URL = "parodus.token.server.url";
    
    /** Property key for store number of allowed clients */
    public static final String PROP_KEY_ALLOWED_NOOFCLIENTS = "numberofclients.allowed.";

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

    /** Property key name for TFTP server IP address. */
    public static final String PROP_KEY_TFTP_SERVER_IP_ADDRESS = "cdl.tftp.server.ip";

    public static final String PARTIAL_DEVICE_CHECK = "is.device.check.";

    /** Property to get file system space based on the device */
    public static final String PROP_KEY_FILE_SYSTEM_SPACE = "file.system.space.";

    /** Lighttpd version to be checked */
    public static final String PROP_KEY_LIGHTTPD_VERSION = "web.server.lighttpd.version";

    public static final String TEST_SSID_PASSWORD = "test.ssid.password";

    public static final String PROP_KEY_DNSMASQ_VERSION = "dnsmasq.version";

    public static final String PROP_KEY_DUNFELL_DNSMASQ_VERSION = "dunfell.dnsmasq.version";

    /** configparamgen path from stb.props **/
    public static final String STB_PROPS_CONFIGPARAMGEN_VM_PATH = "config.param.gen.utility.rdkb.vm.path";

    /** configparamgen utility file name **/
    public static final String STB_PROPS_CONFIGPARAMGEN_FILENAME = "config.param.gen.utility.rdkb";

    /** Constant for Property Key to fetch download url to access AutoVault service */
    public static final String PROP_KEY_AUTOVAULT_DOWNLOAD_URL = "autovault.downloadURL";

    /** Constant for Property Key to fetch credentails to access AutoVault service */
    public static final String PROP_KEY_AUTOVAULT_BASE64_CREDENTIALS = "autovault.base64.credentials";

    public static final String STRESS_TEST_FILE_PATH = "autovault.stress.test.file.path";

    /** Property key for Single Reboot Device */
    public static final String PROP_KEY_SINGLE_REBOOT_DEVICE = "prop.single.reboot.device";

    public static final String PROP_KEY_WEBPA_SERVER_URL = "WEBPA_SERVER_URL";

    /** String containing incorrect webpa server url */
    public static final String PROP_KEY_INCORRECT_WEBPA_URL = "incorrect.webpa.url";

    /** Plume back haul url for wifi mesh service */
    public static final String PROP_KEY_PLUME_BACK_HAUL_URL = "plume.back.url";

    /** Constant to hold expected range for downstream channel frequency value */
    public static final String RANGE_FOR_DWNSTRM_CHNL_FREQ_VALUE = "frequency.range.value";

    /** String to the location of firmwaredownload file for DSL devices */
    public static final String AUTOVAULT_FIRMWAREDOWNLOAD_PATH_DSL = "autovalut.invalid.firmwaredownload.path";

    public static final String CPUPROCANALYZER_FILE_PATH = "autovault.cpuprocanalyzer.file.path";

    public static final String PROCESS_LIST_FILE_PATH = "autovault.process.list.file.path";

    /** Command to store path of tcpdump **/
    public static final String FILE_PATH_TCPDUMP = "autovalut.tcpdump.file.path";

    /** Property Key for DAC15 CDL URL */
    public static final String PROP_KEY_DAC15_CDL_URL = "rdkb.cdl.dac15.url";

    public static final String OPERATIONAL_TRANSMISSION_RATE_2GHZ_FOR_DEVICES = "operational.transmission.rate.2ghz.";

    public static final String SUPPORTED_TRANSMISSION_RATE_2GHZ_FOR_DEVICES = "supported.transmission.rate.2ghz.";

    public static final String TEMP_OPERATIONAL_TRANSMISSION_RATE_2GHZ_FOR_DEVICES = "temp.operational.transmission.rate.2ghz.";

    public static final String OPERATIONAL_TRANSMISSION_RATE_5GHZ_FOR_DEVICES = "operational.transmission.rate.5ghz.";

    public static final String SUPPORTED_TRANSMISSION_RATE_5GHZ_FOR_DEVICES = "supported.transmission.rate.5ghz.";

    public static final String WIFI_OPERATING_STANDARDS_FOR_DEVICE_AND_WIFI_FREQUENCY_SPECIFIC_2GHZ = "wifi.operational.standard.device.wifi.frequency.2ghz.";

    public static final String WIFI_OPERATING_STANDARDS_FOR_DEVICE_AND_WIFI_FREQUENCY_SPECIFIC_5GHZ = "wifi.operational.standard.device.wifi.frequency.5ghz.";

    public static final String BASIC_TX_RATE_2GHZ_FOR_DEVICES = "basic.txrate.2ghz.";

    public static final String TEMP_SUPPORTED_TRANSMISSION_RATE_2GHZ_FOR_DEVICES = "temp.supported.transmission.rate.2ghz.";

    public static final String BASIC_TX_RATE_2GHZ_FOR_DEVICES_POST_UPGRADE = "basic.txrate.postupgrade.2ghz.";

    public static final String BASIC_TX_RATE_5GHZ_FOR_DEVICES_POST_UPGRADE = "basic.txrate.postupgrade.5ghz.";

    public static final String TEMP_BASIC_TX_RATE_2GHZ_FOR_DEVICES = "temp.basic.txrate.2ghz.";

    public static final String SUPPORTED_TX_RATE_5GHZ_FOR_N_DEVICE = "supported.txrate.n.5ghz";

    public static final String OPERATIONAL_TX_RATE_5GHZ_FOR_N_DEVICE = "operational.txrate.n.5ghz";

    public static final String SUPPORTED_DATA_TX_RATE_5GHZ_FOR_DEVICE = "supported.data.txrate.5ghz";

    public static final String SUPPORTED_TX_RATE_2GHZ_FOR_DEVICE = "supported.txrate.2ghz";

    public static final String PARTIAL_DEVICE_CHECK_SELF_HEAL = "is.device.check.self.heal.";

    /** Constant to store private wifi passphrase for band steering */
    public static final String PRIVATE_WIFI_PASSPHRASE_BAND_STEERING = "private.wifi.passphrase.bandsteering";

    public static final String PRIVATE_WIFI_SSID_BAND_STEERING = "private.wifi.ssid.bandsteering";

    public static final String SECURE_MOUNT_BACKUP_FILES = "secure.mount.backup.files";

    /** Prop key xconf url. */
    public static final String PROP_KEY_PROXY_XCONF_URL = "proxy.xconf.logupload.url";

    /** The property key for partner ID Names */
    public static final String PROP_KEY_SPECIFIC_PARTNER_ID = "specific.partner.id";

    /** Property key for Device Ledlogs */
    public static final String PROP_KEY_LEDLOGS_DEVICE = "Device.ledlogs.Available.";

    /** Property key for Device Ledlogs */
    public static final String PROP_KEY_LEDLOGS_SPECIFICDEVICE = "Device.ledlogs.";

    /** Prop key for DCM log upload settings */
    public static String PROP_KEY_FOR_LOG_UPLOAD_SETTINGS = "dcm.logupload.settings";

    /** String to ci xconf url for telemetry2.0 */
    public static final String CI_XCONF_URL_TELEMTRY_2 = "telemetry.config.url";

    /** **/
    public static String PROP_KEY_FOR_TELEMETRY_PROFILE = "dcm.telemetry.profiles";

    /** Telemetry profile for basic custom scenario */
    public static String PROP_KEY_FOR_BASIC_TELEMETRY_PROFILE = "dcm.telemetry.profiles.custom";

    /** Property key for Device with firmwareupgrader */
    public static final String PROP_KEY_SUPPORT_DEVICE = "Device.support.Available.";

    public static final String INVALID_WIFI_PASSWORD = "invalid.wifi.password";

    public static final String PARTIAL_PROPERTY_KEY_FOR_FILE_LOCATION = "encrypted.file.location.";

    public static final String PARTIAL_DEVICE_CHECK_SELF_HEAL_02 = "is.device.check.self.heal2.";

    /** Property key for Device Ledlogs */
    public static final String PROP_KEY_LIMITBEACON_DEVICE = "Device.limit.beacon.";

    /** The partial property key for LAN end ip of partners */
    public static final String PROP_KEY_SYN_END_IP_ADDRESS = "syn.lan.endip.";

    /** The partial property key for LAN start ip of partners */
    public static final String PROP_KEY_SYN_START_IP_ADDRESS = "syn.lan.startip.";

    /** The partial property key for LAN local ip of partners */
    public static final String PROP_KEY_SYN_LAN_LOCAL_IP = "syn.lan.localip.";

    public static final String PARTIAL_DEVICE_CHECK_UPGRADE_STATUS_SNMP_COMMAND = "is.device.check.upgrade.snmp.status.";

    public static final String MTA_COMMUNITY_STRING = "mta.community.string";

    /** String to store property of cert of rdk_manager snmpv3 */
    public static final String FILE_SNMPV3_CERT_RDK_MANAGER = "snmpv3.cert.rdk.manager";

    /** String to store property of cert of rdkb_snmpd snmpv3 */
    public static final String FILE_SNMPV3_CERT_RDKB_SNMPD = "snmpv3.cert.rdkb.snmpd";

    /** Property key models with corrupt image of same device class */
    public static final String MAP_MODEL_CORRUPT_IMAGE_SAME_DEVICE_CLASS = "map.model.corrupt.image.same.device.class.";

    /** Property key models with corrupt image info of same device class */
    public static final String MAP_MODEL_CORRUPT_IMAGE_SAME_DEVICE_CLASS_INFO = "map.model.corrupt.image.same.device.class.info.";

    /** Property key models with corrupt image of same device class */
    public static final String MAP_MODEL_CORRUPT_IMAGE_DIFFERENT_DEVICE_CLASS = "map.model.corrupt.image.different.device.class.";

    /** Property key models with corrupt image info of same device class */
    public static final String MAP_MODEL_CORRUPT_IMAGE_DIFFERENT_DEVICE_CLASS_INFO = "map.model.corrupt.image.different.device.class.info.";

    /** Property key models with corrupt image of same device class */
    public static final String MAP_MODEL_CORRUPT_IMAGE_PARTIAL_DOWNLOAD = "map.model.corrupt.image.partial.download.";

    /** Property key models with corrupt image info of same device class */
    public static final String MAP_MODEL_CORRUPT_IMAGE_PARTIAL_DOWNLOAD_INFO = "map.model.corrupt.image.partial.download.info.";
    
    /** property key for corrupt image location */
    public static final String PROP_KEY_XCONF_FIRMWARE_LOCATION_CORRUPT_IMAGES = "xconf.firmware.location.http.corrupt.images";

    public static final String PARTIAL_DEVICE_CHECK_NEGETIVE_CDL = "partial.device.check.negetive.cdl.";
    
    
}

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

    /** The property key for specific syndication partner ID Names */
    public static final String PROP_KEY_SPECIFIC_SYNDICATION_PARTNER_ID_LIST = "specific.syndicationpartner.id.list";

    /** The property key to get partner ID Prefix */
    public static final String PROP_KEY_DEFAULT_SSID_PREFIX = "default.ssid.prefix.";

    /** The property key for removing chars based on partner */
    public static final String PROP_KEY_PARTNER_CHARS_REMOVE = "remove.chars.partner.";

    /** The property key for wifi band for partner ID */
    public static final String PROP_KEY_WIFIBAND_PARTNERID = "wifiband.partner.id.";

    /** The property key for wifi band for partner ID */
    public static final String PROP_KEY_PARTNERID_DEVICE = "verify.partner.id.device";

    /** property to configure the invalid NTP server URL */
    public static final String PROPKEY_FOR_INVALID_NTPHOST = "serverurl.invalid.ntp";

    /** property to configure the NTP server URL */
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

    /** Property key for Device with firmwareupgrader */
    public static final String PROP_KEY_SPECIFIC_DEVICE = "Device.specific.";

    public static final String PARTIAL_DEVICE_CHECK = "is.device.check.";

    /** Property to get file system space based on the device */
    public static final String PROP_KEY_FILE_SYSTEM_SPACE = "file.system.space.";

    /** Lighttpd version to be checked */
    public static final String PROP_KEY_LIGHTTPD_VERSION = "web.server.lighttpd.version";

    public static final String TEST_SSID_PASSWORD = "test.ssid.password";

    public static final String PROP_KEY_DNSMASQ_VERSION = "dnsmasq.version";

    public static final String PROP_KEY_DUNFELL_DNSMASQ_VERSION = "dunfell.dnsmasq.version";

    /**
     * Constant for Property Key to fetch download url to access AutoVault service
     */
    public static final String PROP_KEY_AUTOVAULT_DOWNLOAD_URL = "autovault.downloadURL";

    /**
     * Constant for Property Key to fetch credentails to access AutoVault service
     */
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

    /** Property Key for CDL URL */
    public static final String PROP_KEY_CDL_URL = "rdkb.cdl.url";

    /** property to configure public Broadband Router Server */
    public static final String PROP_KEY_CONSTANT_SERVER_TAG = "broadband.router.server";

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

    /** property to configure Public wifi ssid for 2GHZ */
    public static final String PROP_KEY_PUBLIC_WIFI_SSID_2 = "public.wifi.ssid.2.4";

    /** property to configure Public wifi ssid for 5GHZ */
    public static final String PROP_KEY_PUBLIC_WIFI_SSID_5 = "public.wifi.ssid.5";

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

    public static final String PROP_KEY_STB_SECURITY_BANNERS = "security.banner";

    /** Key to get the possible channels in 5Ghz from Stb properties file */
    public static String KEY_FOR_5GHZ_WIFI_POSSIBLE_CHANNELS = "possibleChannelsIn5GhzWifi.";

    public static final String NEW_STBRTL_URL = "new.stbrtl.url";

    public static final String OLD_STBRTL_URL = "old.stbrtl.url";

    /** Property key for interface values */
    public static final String PROP_KEY_INTERFACE_VALUES = "interface.alias.";

    /** Property key for interface values */
    public static final String PROP_KEY_INTERFACE_NAMES = "interface.name.";

    public static final String RADIO_NAME_24GHZ_CHECK = "radio.name.24ghz.device.check.";

    public static final String RADIO_NAME_5GHZ_CHECK = "radio.name.5ghz.device.check.";

    public static final String DEVICE_CHECK_VALUE1 = "is.device.check.value1.";

    public static final String DEVICE_CHECK_VALUE2 = "is.device.check.value2.";

    public static final String DEVICE_CHECK_VALUE3 = "is.device.check.value3.";

    public static final String PROCESS_NTPD_CLIENT1 = "process.ntpd.client1";

    public static final String PROCESS_NTPD_CLIENT2 = "process.ntpd.client2";

    public static final String PARTIAL_DEVICE_CHECK_ACCEPTANCE_CRITERIA = "is.device.check.acceptance1.";

    public static final String PARTIAL_DEVICE_CHECK_ACCEPTANCE = "is.device.check.acceptance.";

    /**
     * Test constant for dnsoverride level one block site address in stb.properties file
     */
    public static final String PROP_KEY_DNS_BLOCK_ADDRESS_FOR_LEVEL_ONE_SITE = "dnsoverride.blocksiteaddress.level.one";

    /** Test constant for level one site host address in stb.properties file */
    public static final String PROP_KEY_HOST_ADDRESS_FOR_LEVEL_ONE_SITE = "dnsoverride.siteaddress.level.one";

    public static final String PROP_KEY_TXRX_RATELIST_DEVICE = "txrx.ratelist.devices";

    public static final String STRING_DEFAULT_GLOBAL_DNS_IPV4_VALUE = "default.global.dns.ipv4.value";

    public static final String STRING_DEFAULT_GLOBAL_DNS_IPV6_VALUE = "default.global.dns.ipv6.value";

    public static final String STRING_DNS_IPV4_VALUE_FOR_DNS_LEVEL_ONE_PRIMARY = "dns.ipv4.value.level.one.primary";

    public static final String STRING_DNS_IPV4_VALUE_FOR_DNS_LEVEL_TWO_PRIMARY = "dns.ipv4.value.level.two.primary";

    public static final String STRING_DNS_IPV4_VALUE_FOR_DNS_LEVEL_THREE_PRIMARY = "dns.ipv4.value.level.three.primary";

    public static final String STRING_DNS_IPV6_VALUE_FOR_DNS_LEVEL_ONE_PRIMARY = "dns.ipv6.value.level.one.primary";

    public static final String STRING_DNS_IPV6_VALUE_FOR_DNS_LEVEL_TWO_PRIMARY = "dns.ipv6.value.level.two.primary";

    public static final String STRING_DNS_IPV6_VALUE_FOR_DNS_LEVEL_THREE_PRIMARY = "dns.ipv6.value.level.three.primary";

    public static final String STRING_DNS_VALID_IPV4_VALUE = "dns.valid.ipv4.value";

    public static final String STRING_DNS_VALID_IPV6_VALUE = "dns.valid.ipv6.value";

    public static final String STRING_DNS_INVALID_SECONDARY_IPV4_VALUE = "dns.invalid.secondary.ipv4.value";

    public static final String STRING_DNS_INVALID_SECONDARY_IPV6_VALUE = "dns.invalid.secondary.ipv6.value";

    public static final String STRING_DNS_INVALID_PRIMARY_IPV4_VALUE = "dns.invalid.primary.ipv4.value";

    public static final String STRING_DNS_INVALID_PRIMARY_IPV6_VALUE = "dns.invalid.primary.ipv6.value";

    public static final String STRING_DNS_VALID_SECONDARY_IPV4_VALUE = "dns.valid.secondary.ipv4.value";

    public static final String STRING_DNS_VALID_SECONDARY_IPV6_VALUE = "dns.valid.secondary.ipv6.value";

    public static final String STRING_DNS_ANOTHER_INVALID_SECONDARY_IPV4_VALUE = "dns.another.invalid.secondary.ipv4.value";

    public static final String STRING_DNS_ANOTHER_INVALID_SECONDARY_IPV6_VALUE = "dns.another.invalid.secondary.ipv6.value";

    public static final String DEVICE_CHECK_VALUE_XDNS = "is.device.check.xdns.";

    public static final String CURRENT_UPNP_VERSION = "current.upnp.version";

    public static final String PARTIAL_DEVICE_CHECK_GBPAD = "is.device.check.gbpad.";

    public static final String PARTIAL_DEVICE_CHECK_GBPAD1 = "is.device.check.gbpad1.";

    public static final String PARTIAL_DEVICE_CHECK_GBPAD2 = "is.device.check.gbpad2.";

    public static final String IS_SERVER_CONFIGURED_TO_UPLOAD_CRASH_DETAILS = "is.server.configured";

    public static final String CAPTIVE_PORTAL_PAGE_TITLE_FOR_PARTNER_DEVICE = "captive.portal.page.titles.partner.device.";

    public static final String CAPTIVE_PORTAL_CONFIGURATION_CONFIRM_PAGE_HEADER_MESSAGE = "captive.portal.configuraion.confirm.message.partner.";

    public static final String CAPTIVE_PORTAL_CONFIGURATION_SUCCESS_PAGE_HEADER_MESSAGE = "captive.portal.configuraion.success.message.partner.";

    public static final String TR69_NAMING_CONVENTION_PARAMETERS = "tr69.namingconvention.parameters";

    /**
     * Property Key for CDL URL for Business Class devices which are Atom Console
     */
    public static final String PROP_KEY_CDL_URL_BUSINESS_ATOM = "rdkb.cdl.url.business.atom";

    public static final String IS_SERVER_CONFIGURED_TO_UPLOAD_TO_SERVER = "is.logupload.server.configured";

    /** property to configure the default partner */
    public static final String PROPERTY_DEFAULT_PARTNER = "default.partnerId";

    /** platform based property for available syndication partners */
    public static final String PROPERTY_PLATFORM_BASED_PARTNER_LIST = "partners.list.";

    /** property to configure AES encryption key **/
    public static final String PROP_KEY_AES_ENCRYPTION_KEY = "aes.encryption.key";

    /** property to configure invalid certificates to verify L & F **/
    public static final String PROP_KEY_CERT_CA_CHAIN = "cert.ca.chain";
    public static final String PROP_KEY_CERT_VIDEO_CLIENT = "cert.video.client";
    public static final String PROP_KEY_VIDEO_CLIENT = "key.video.client";

    /** property to configure snmpv3 dk kickstart security number 1 */
    public static final String PROP_KEY_SNMPV3_DH_KICK_START_SECURITY_NUMBER_1 = "snmpv3.dh.kickstart.seccurity.no1";
    /** property to configure snmpv3 dk kickstart security number 2 */
    public static final String PROP_KEY_SNMPV3_DH_KICK_START_SECURITY_NUMBER_2 = "snmpv3.dh.kickstart.seccurity.no2";
    /** property to configure snmpv3 dk kickstart security number 3 */
    public static final String PROP_KEY_SNMPV3_DH_KICK_START_SECURITY_NUMBER_3 = "snmpv3.dh.kickstart.seccurity.no3";
    /** property to configure snmpv3 dk kickstart security number 4 */
    public static final String PROP_KEY_SNMPV3_DH_KICK_START_SECURITY_NUMBER_4 = "snmpv3.dh.kickstart.seccurity.no4";

    /** property to configure dummy first mac address for XDNS */
    public static final String PROP_KEY_VALUE_MACADDRESS_FIRST_XDNS = "value.macaddress.first.xdns";

    /** property to configure dummy second mac address for XDNS */
    public static final String PROP_KEY_VALUE_MACADDRESS_SECOND_XDNS = "value.macaddress.second.xdns";

    /** property to configure Radius Server IPAddr */
    public static final String PROP_KEY_RADIUS_SERVER_IPADDR = "radius.server.ipaddr";

    /** property to configure url of rdkb crash failover upload server */
    public static final String PROP_KEY_RDKB_CRASH_FAILOVER_UPLOAD_URL = "rdkd.crash.fallover.upload.url";

    /** property to configure Broadband Device LAN IP */
    public static final String PROP_KEY_XB_LAN_IP = "xb.lan.ip";

    /** property to configure business calss gateway IP */
    public static final String PROP_KEY_BUSINESS_CLASS_GATEWAYIP = "bussiness.class.gatewayip";

    /** property to configure residential class gateway ip */
    public static final String PROP_KEY_STRING_RESIDENTIAL_CLASS_GATEWAYIP = "residential.class.gatewatip";

    /** property to configure DHCP min address for business class devices */
    public static final String PROP_KEY_STRING_DHCP_MIN_ADDRESS_BUSSI = "dhcp.min.address.buss";

    /** property to configure DHCP min address for residential class devices */
    public static final String PROP_KEY_STRING_DHCP_MIN_ADDRESS = "dhcp.min.address";

    /** property to configure DHCP max address for business class devices */
    public static final String PROP_KEY_STRING_DHCP_MAX_ADDRESS_BUSSI = "dhcp.max.address.buss";

    /** property to configure DHCP max address for residential class devices */
    public static final String PROP_KEY_STRING_DHCP_MAX_ADDRESS = "dhcp.max.address";

    /** property to configure plan ID of wifi blaster */
    public static final String PROP_KEY_BLASTER_PLANID = "wifiblaster.plan.id";

    /** property to configure destination mac for wifi blaster */
    public static final String PROP_KEY_BLASTER_DESTMAC = "wifiblaster.destmac";

    /** property to configure valid Gateway local Ip in setting Gateway Ip */
    public static final String PROP_KEY_TEST_GATEWAY_LOCAL_IP_1 = "test.gateway.localip1";

    /**
     * property to configure valid Gateway local DHCP Start address in setting Gateway Ip
     */
    public static final String PROP_KEY_TEST_GATEWAY_LOCAL_DHCP_START_ADDR = "test.gateway.localdhcp.startaddr";

    /**
     * property to configure valid Gateway local DHCP end address in setting Gateway Ip
     */
    public static final String PROP_KEY_TEST_GATEWAY_LOCAL_DHCP_END_ADDR = "test.gateway.localdhcp.endaddr";

    /** property to configure WIFI Client MacAddress parameter */
    public static final String PROP_KEY_MAC_ADDRESS_WIFICLIENT = "wificlient.macaddress";

    /** property to configure command to access atom from arm console */
    public static final String CMD_TOACCESS_ATOMCONSOLEFROMARM = "cmd.accesss.atomfromarm";

    /** property Constant to holds a password with special character */
    public static final String PROP_KEY_PASSWORD_WITH_SPECIAL_CHARACTER = "home.network.password.with.specialcharacter";

    /** property Constant to holds a password with space */
    public static final String PROP_KEY_PASSWORD_WITH_SPACE = "home.network.password.with.space";

    /** property to configure wehgui heading of homepage */
    public static final String PROP_KEY_WEB_GUI_HOME_PAGE_HEADING = "webgui.heading.homepage";

    public static final String ADMIN_LOGIN_PAGE_TITLE = "lan.gui.admin.login.page.title";

    public static final String PAGE_TITLE_AT_A_GLANCE = "lan.gui.page.title.at.a.glance";

    /** property to configure link text for Reset / Restore Gateway page */
    public static final String PROP_KEY_LINK_TEXT_HARDWARE_WIZARD = "lan.gui.link.text.hardware.wizard";

    /** property to configure page url for hardware wizard page */
    public static final String PROP_KEY_PAGE_URL_HARDWARE_WIZARD = "lan.gui.page.url.hardware.wizard";

    /** property to configure page title for hardware wizard page */
    public static final String PROP_KEY_PAGE_TITLE_HARDWARE_WIZARD = "lan.gui.page.title.hardware.wizard";

    /** property Constant to holds a password with proper standards */
    public static final String PROP_KEY_PASSWORD_MISMATCH = "home.network.password.with.proper.standards";

    /**
     * property to retrive a part of buildname to check its contains in the buildname
     */
    public static final String STRING_BUILDNAME_PART = "buildname.part.check";

    /** property to configure Page Title of Connected Device page */
    public static final String PAGE_TITLE_CONNECTED_DEVICE = "lan.gui.page.title.connected.devices";

    /** property to configure captiveportal logo */
    public static final String STRING_LOGO_CAPTIVEPORTAL = "logo.captivePortal";

    /** property to configure Page Title of Connection wifi page */
    public static final String PAGE_TITLE_CONNECTION_WIFI = "lan.gui.page.title.connection.wifi";

    /** property Constant to store wifi edit 5 GHz page title */
    public static String PROP_KEY_PAGE_TITLE_WIFI_CONFIG_PAGE = "lan.gui.page.title.wifi.configuration.page";

    public static final String CHANGE_PASSWORDPAGE_TITLE = "lan.gui.page.title.changepassword";

    public static final String PAGE_TITLE_PARENTAL_CONTROL_MANAGED_SITES = "lan.gui.page.title.parental.control.managed.sites";

    public static final String PAGE_TITLE_PARENTAL_CONTROL_ADD_BLOCKED_DOMAIN = "lan.gui.page.title.parental.control.managed.sites.add.blocked.domain";

    public static final String PAGE_TITLE_PARENTAL_CONTROL_ADD_BLOCKED_KEYWORD = "lan.gui.page.title.parental.control.managed.sites.add.blocked.keyword";

    public static final String PAGE_TITLE_PARENTAL_CONTROL_MANAGED_SERVICE = "lan.gui.page.title.parental.control.managed.service";

    public static final String PAGE_TITLE_PARENTAL_CONTROL_MANAGED_SERVICES_ADD_BLOCKED_SERVICE = "lan.gui.page.title.parental.control.managed.services.add.blocked.service";

    /** property to configure page title for connected devices page */
    public static final String PROP_KEY_PAGE_TITLE_CONNECTED_DEVICE_PAGE = "lan.gui.page.title.connected.devices";

    /** property to configure link text for connected devices page */
    public static final String PROP_KEY_LINK_TEXT_CONNECTED_DEVICE_PAGE = "lan.gui.link.text.connected.devices";

    /** property to configure page url for connected devices page */
    public static final String PROP_KEY_PAGE_URL_CONNECTED_DEVICE_PAGE = "lan.gui.page.url.connected.devices";

    /** property to configure page url for connected devices page */
    public static final String PROP_KEY_PAGE_TITLE_CONNECTED_DEVICE_EDIT_DEVICE_PAGE = "lan.gui.page.title.connected.devices.edit.devices";

    public static final String LINK_TEXT_LOCAL_IP_NETWORK = "lan.gui.link.text.connection.local.ip.network";

    public static final String PAGE_URL_LOCAL_IP_NETWORK = "lan.gui.page.url.connection.local.ip.network";

    public static final String PAGE_TITLE_LOCAL_IP_NETWORK = "lan.gui.page.title.connection.local.ip.network";

    public static final String LINK_TEXT_PARTNER_NETWORK = "lan.gui.link.text.connection.partner.network";

    public static final String PAGE_URL_PARTNER_NETWORK = "lan.gui.page.url.connection.partner.network";

    public static final String PAGE_TITLE_PARTNER_NETWORK = "lan.gui.page.title.connection.partner.network";

    public static final String PAGE_TITLE_FIREWALL_IPV4 = "lan.gui.page.title.firewall.ipv4";

    public static final String PAGE_TITLE_PARENTAL_CONTROL = "lan.gui.page.title.parental.control";

    public static final String PAGE_TITLE_MANAGED_DEVICES = "lan.gui.page.title.managed.devices";

    public static final String PAGE_TITLE_FIREWALL_IPV6 = "lan.gui.page.title.firewall.ipv6";

    public static final String LINK_TEXT_CONNECTION_STATUS = "lan.gui.link.text.connection.status";

    public static final String PAGE_URL_CONNECTION_STATUS = "lan.gui.page.url.connection.status";

    public static final String PAGE_TITLE_CONNECTION_STATUS = "lan.gui.page.title.connection.status";

    /** property to configure link text for Reset / Restore Gateway page */
    public static final String LINK_TEXT_RESET_RESTORE_GATEWAY = "lan.gui.link.text.reset.restore.gateway";

    /** property to configure page url for hardware wizard page */
    public static final String PAGE_URL_RESET_RESTORE_GATEWAY = "lan.gui.page.url.reset.restore.gateway";

    /** property to configure page title for Reset / Restore Gateway page */
    public static final String PAGE_TITLE_RESET_RESTORE_GATEWAY = "lan.gui.page.title.reset.restore.gateway";

    /** property to configure page title for network diagnostics page */
    public static final String PROP_KEY_PAGE_TITLE_NW_DIAG_PAGE = "lan.gui.page.title.network.diagnostics";

    /** property to configure page title for network diagnostics page */
    public static final String PROP_KEY_LINK_TEXT_NW_DIAG_PAGE = "lan.gui.link.text.network.diagnostics";

    /** property to configure page title for network diagnostics page */
    public static final String PROP_KEY_PAGE_URL_NW_DIAG_PAGE = "lan.gui.page.url.network.diagnostics";

    public static final String DEVICE_CHECK_WIFI_DRIVER_COMMANDS = "wifi.driver.commands.";

    public static final String COMMANDS_TO_GET_INTERFACE_VALUES_FOR_DEVICES = "commands.get.interface.values.for.devices";

    public static final String COMMANDS_TO_GET_INTERFACE_VALUES_FOR_DEVICES_5GHZ = "commands.get.interface.values.for.devices.5ghz";

    public static final String RESPONSE_OF_COMMANDS_TO_GET_INTERFACE_VALUES_FOR_DEVICES = "response.of.commands.get.interface.values.for.devices";

    public static final String PAGE_URL_MoCA = "lan.gui.page.url.connection.moca";

    public static final String PAGE_TITLE_MoCA = "lan.gui.page.title.connection.moca";

    public static final String LINK_TEXT_MoCA = "lan.gui.link.text.connection.moca";

    public static final String PAGE_URL_SOFTWARE = "lan.gui.page.url.software";

    public static final String PAGE_TITLE_SOFTWARE = "lan.gui.page.title.software";

    public static final String LINK_TEXT_SOFTWARE = "lan.gui.link.text.software";

    public static final String PAGE_URL_HARDWARE = "lan.gui.page.url.hardware";

    public static final String LINK_TEXT_HARDWARE = "lan.gui.link.text.hardware";

    public static final String PAGE_URL_SYSTEM_HARDWARE = "lan.gui.page.url.system.hardware";

    public static final String PAGE_TITLE_SYSTEM_HARDWARE = "lan.gui.page.title.system.hardware";

    public static final String LINK_TEXT_SYSTEM_HARDWARE = "lan.gui.link.text.system.hardware";

    public static final String PAGE_URL_HARDWARE_LAN = "lan.gui.page.url.hardware.lan";

    public static final String PAGE_TITLE_HARDWARE_LAN = "lan.gui.page.title.hardware.lan";

    public static final String LINK_TEXT_HARDWARE_LAN = "lan.gui.link.text.hardware.lan";

    public static final String PAGE_URL_HARDWARE_WIRELESS = "lan.gui.page.url.hardware.wireless";

    public static final String PAGE_TITLE_HARDWARE_WIRELESS = "lan.gui.page.title.hardware.wireless";

    public static final String LINK_TEXT_HARDWARE_WIRELESS = "lan.gui.link.text.hardware.wireless";

    public static final String PAGE_URL_MANAGED_SERVICES = "lan.gui.page.url.managed.services";

    public static final String PAGE_TITLE_MANAGED_SERVICES = "lan.gui.page.title.managed.services";

    public static final String PAGE_URL_REPORTS = "lan.gui.page.url.reports";

    public static final String PAGE_TITLE_REPORTS = "lan.gui.page.title.reports";

    public static final String LINK_TEXT_REPORTS = "lan.gui.link.text.reports";

    public static final String PAGE_URL_ADVANCED = "lan.gui.page.url.advanced";

    public static final String PAGE_TITLE_ADVANCED = "lan.gui.page.title.advanced";

    public static final String LINK_TEXT_ADVANCED = "lan.gui.link.text.advanced";

    public static final String PAGE_URL_ADVANCED_REMOTE_MGMT = "lan.gui.page.url.advanced.remote.mgmt";

    public static final String PAGE_TITLE_ADVANCED_REMOTE_MGMT = "lan.gui.page.title.advanced.remote.mgmt";

    public static final String LINK_TEXT_ADVANCED_REMOTE_MGMT = "lan.gui.link.text.advanced.remote.mgmt";

    public static final String PAGE_URL_ADVANCED_DMZ = "lan.gui.page.url.advanced.dmz";

    public static final String PAGE_TITLE_ADVANCED_DMZ = "lan.gui.page.title.advanced.dmz";

    public static final String LINK_TEXT_ADVANCED_DMZ = "lan.gui.link.text.advanced.dmz";

    public static final String PAGE_URL_ADVANCED_DEVICE_DISCOVER = "lan.gui.page.url.advanced.device.discover";

    public static final String PAGE_TITLE_ADVANCED_DEVICE_DISCOVER = "lan.gui.page.title.advanced.device.discover";

    public static final String LINK_TEXT_ADVANCED_DEVICE_DISCOVER = "lan.gui.link.text.advanced.device.discover";

    public static final String PAGE_URL_TROUBLESHOOTING_LOGS = "lan.gui.page.url.troubleshooting.logs";

    public static final String PAGE_TITLE_TROUBLESHOOTING_LOGS = "lan.gui.page.title.troubleshooting.logs";

    public static final String LINK_TEXT_TROUBLESHOOTING_LOGS = "lan.gui.link.text.troubleshooting.logs";

    public static final String PAGE_URL_TROUBLESHOOTING_WIFI_SPECTRUM_ANALYZER = "lan.gui.page.url.troubleshooting.wifi.spectrum.analyzer";

    public static final String PAGE_TITLE_TROUBLESHOOTING_WIFI_SPECTRUM_ANALYZER = "lan.gui.page.title.troubleshooting.wifi.spectrum.analyzer";

    public static final String LINK_TEXT_TROUBLESHOOTING_WIFI_SPECTRUM_ANALYZER = "lan.gui.link.text.troubleshooting.wifi.spectrum.analyzer";

    public static final String PAGE_URL_TROUBLESHOOTING_MOCA_DIAGNOSTICS = "lan.gui.page.url.troubleshooting.moca.diagnostics";

    public static final String PAGE_TITLE_TROUBLESHOOTING_MOCA_DIAGNOSTICS = "lan.gui.page.title.troubleshooting.moca.diagnostics";

    public static final String LINK_TEXT_TROUBLESHOOTING_MOCA_DIAGNOSTICS = "lan.gui.link.text.troubleshooting.moca.diagnostics";

    public static final String PAGE_URL_TROUBLESHOOTING_CHANGE_PWD = "lan.gui.page.url.troubleshooting.change.pwd";

    public static final String PAGE_TITLE_TROUBLESHOOTING_CHANGE_PWD = "lan.gui.page.title.troubleshooting.change.pwd";

    public static final String LINK_TEXT_TROUBLESHOOTING_CHANGE_PWD = "lan.gui.link.text.troubleshooting.change.pwd";

    public static final String PAGE_URL_MANAGED_SITES = "lan.gui.page.url.managed.sites";

    public static final String PAGE_TITLE_MANAGED_SITES = "lan.gui.page.title.managed.sites";

    public static final String LINK_TEXT_MANAGED_SITES = "lan.gui.link.text.managed.sites";

    public static final String LINK_TEXT_MANAGED_SERVICES = "lan.gui.link.managed.services";

    public static final String PAGE_TITLE_PARENTAL_CONTROL_MANAGED_DEVICES = "lan.gui.page.title.parental.control.managed.devices";

    /** property to configure page title for port triggering page */
    public static final String PROP_KEY_LINK_TEXT_PORT_TRIGGERING = "lan.gui.link.text.port.triggering";

    /** property to configure page title for port triggering page */
    public static final String PROP_KEY_PAGE_URL_PORT_TRIGGERING = "lan.gui.page.url.port.triggering";

    /** property to configure page title for port triggering page */
    public static final String PROP_KEY_PAGE_TITLE_PORT_TRIGGERING = "lan.gui.page.title.port.triggering";

    /** The property key to hold parodus token server url */
    public static final String PROP_KEY_INVALID_PARODUS_TOKEN_SERVER_URL = "parodus.invalid.token.server.url";

    public static final String BUILD_EXTENSION_1 = "build.extension1";

    public static final String BUILD_EXTENSION_2 = "build.extension2";

    public static final String BUILD_EXTENSION_3 = "build.extension3";

    public static final String BUILD_EXTENSION_4 = "build.extension4";

    /** Command to get certificate issuer_name */
    public static final String VALID_CERT_ISSUER_NAME = "certificate.issuers.name";

    public static final String TELEMETRY_UPLOAD_URL = "telemetry.upload.url";

    public static final String DNS_SECONDARY_IP_TCPDUMP = "cmd.dns.secondaryip.tcpdump";

    public static final String DNS_PRIMARY_IP_TCPDUMP = "cmd.dns.primaryip.tcpdump";

    public static final String INVALID_DNS_PRIMARY_IP_TCPDUMP = "cmd.dns.invalid.primaryip.tcpdump";

    /** Property key to identify particular device model */
    public static final String PROP_KEY_PARTICULAR_DEVICE = "Device.particular.";

    /** Property key to identify the applicable devicemodel */
    public static final String PROP_KEY_DEVICE_APPLICABLE_MODEL = "Device.applicable.model.";

    /** Property key which contains payload data to enable/disable Snmpv2 */
    public static final String PROP_KEY_TO_DISABLE_SNMPV2 = "rfc.payload.disable.snmpv2";

    public static final String PROP_KEY_FILE_CONSOLELOG = "console.log.file.name.docsis.reg.";

    public static final String DEVICE_CHECK_FOR_EXPECTED_DEFAULT_OVS_LINUXBRIDGE_CONTROL = "expected.device.for.default.ovs.linuxbridge.control.check.";

    public static final String DEFAULT_BANDSTEERING_ENABLE_VALUE = "default.bandsteering.value.device";

    public static final String PROP_KEY_PUBLIC_PRIMARY_ENDPOINT_IP = "public.gre.tunnel.primary.remote.endpoint.ip";

    public static final String PROP_KEY_PUBLIC_SECONDARY_ENDPOINT_IP = "public.gre.tunnel.secondary.remote.endpoint.ip";

    public static final String PROP_KEY_PROXY_XCONF_SWUPDATE_URL = "proxy.xconf.swupdate.url";

    public static final String WEBUI_CERT_BUNDLE_VERSION = "webui.cert.bundle.version";

    public static final String NC_BAD_HOST_IP = "netcat.bad.host.ip.address";

    public static final String NC_OUTSIDE_HOST_IP = "netcat.outside.host.ip.address";

    public static final String NC_PRIVATE_HOST_IP = "netcat.private.host.ip.address";

    public static final String NC_LOCAL_HOST_PORT = "netcat.local.host.ip.port";

    public static final String NC_PRIVATE_IP_OUTSIDE_RANGE_1 = "netcat.private.ip.outside.range.1";

    public static final String NC_PRIVATE_IP_OUTSIDE_RANGE_2 = "netcat.private.ip.outside.range.2";

    public static final String NC_PRIVATE_IP_OUTSIDE_RANGE_3 = "netcat.private.ip.outside.range.3";

    public static final String NC_PRIVATE_IP_OUTSIDE_RANGE_4 = "netcat.private.ip.outside.range.4";
    
    public static final String INTERFACE_4090_IPTABLE = "interface.4090.iptables";
    
    public static final String ATOM_SYNC_DEVICES_BROADBAND_ACTIVATION_EVENT = "broadband.activation.event.devices.atom.sync";

    public static final String FIBER_DEVICES_BROADBAND_ACTIVATION_EVENT = "broadband.activation.event.devices.fiber";

    public static final String DEVICES_BROADBAND_ACTIVATION_EVENT_SEC_CONSOLE = "broadband.activation.event.sec.console.file.devices";

    public static final String DEVICES_BROADBAND_ACTIVATION_EVENT_CONSOLE_LOG_TXT = "broadband.activation.event.console.log.txt.file.devices";

    public static final String DEVICES_BROADBAND_ACTIVATION_EVENT_LAN_INIT_START = "broadband.activation.event.lan.init.start.devices";

    public static final String DEVICES_BROADBAND_ACTIVATION_EVENT_LANINIT_COMPLETE = "broadband.activation.event.lan.init.complete.devices";

    public static final String DEVICES_BROADBAND_ACTIVATION_EVENT_WAN_INIT_START = "broadband.activation.event.wan.init.start.devices";

    public static final String DEVICES_BROADBAND_ACTIVATION_EVENT_WANINIT_COMPLETE = "broadband.activation.event.wan.init.complete.devices";

    public static final String DEVICES_BROADBAND_ACTIVATION_EVENT_CLIENT_CONNECT = "broadband.activation.event.client.connect.devices";

    public static final String DEVICES_BROADBAND_ACTIVATION_EVENT_WIFI_NAME_BROADCAST_2_4 = "broadband.activation.event.wifi.name.broadcast.24.devices";

    public static final String DEVICES_BROADBAND_ACTIVATION_EVENT_WIFI_NAME_BROADCAST_5 = "broadband.activation.event.wifi.name.broadcast.5.devices";

    public static final String DEVICES_BROADBAND_ACTIVATION_EVENT_WIFI_BROADCAST_COMPLETE = "broadband.activation.event.wifi.broadcast.complete.devices";

    public static final String DEVICES_BROADBAND_ACTIVATION_EVENT_WIFI_PERSONALIZATION_CAPTIVE = "broadband.activation.event.wifi.personalization.captive.mode.devices";

    public static final String CAPTIVE_PORTAL_DEFAULT_DISABLED_PARTNER = "captive.portal.default.disabled.partner";
    
    public static final String DEVICE_CHECK_BOOTSTRAP = "is.device.check.bootstrap.";
}

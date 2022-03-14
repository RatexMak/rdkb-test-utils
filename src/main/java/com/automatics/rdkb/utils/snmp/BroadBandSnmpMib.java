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
package com.automatics.rdkb.utils.snmp;


/**
 * Broad Band specific SNMP MIBs. Common SNMP MIBs are available in {@link SnmpMib} in rdkv-utils, please refer that
 * before defining any new MIBs to avoid duplication.
 * 
 * @Refactor Athira, Alan_Bivera
 * 
 */

public enum BroadBandSnmpMib {

    ECM_DOCS_IF_CM_CMTS_MAC_ADDRESS("docsIfCmCmtsAddress", "1.3.6.1.2.1.10.127.1.2.1.1.1"),
    ECM_DOCS_DEV_CURRENT_SOFTWARE_VERSION("docsDevSwCurrentVers", "1.3.6.1.2.1.69.1.3.5.0"),
    ECM_SERVER_TRANSPORT_PROTOCOL("docsDevSwServerTransportProtocol", "1.3.6.1.2.1.69.1.3.8.0"),
    ECM_SERVER_TRANSPORT_PROTOCOL_WITHOUT_INDEX("docsDevSwServerTransportProtocol", "1.3.6.1.2.1.69.1.3.8"),
    ECM_DOCS_DEV_SW_FILE_NAME_WITHOUT_INDEX("docsdevSwFilename", ".1.3.6.1.2.1.69.1.3.2"),
    ECM_SERVER_ADDRESS_TYPE_CDL_WITHOUT_INDEX("docsDevSwServerAddressType", "1.3.6.1.2.1.69.1.3.6"),
    ECM_DOCS_DEVSW_ADMIN_STATAUS_WITHOUT_INDEX("docsDevSwAdminStatus", ".1.3.6.1.2.1.69.1.3.3"),
    ECM_DOCS_DEV_SW_OPER_STATUS_WITHOUT_INDEX("docsDevOperStatus", "1.3.6.1.2.1.69.1.3.4"),
    ECM_DOCS_DEV_SW_SERVER_ADDRESS_WITHOUT_INDEX("docsDevSwServerAddres", "1.3.6.1.2.1.69.1.3.7"),
    ECM_FWUPGRADE_AND_FACTORYRESET_WITHOUT_INDEX("rdkbRgDeviceFWupdateAndFactoryReset",".1.3.6.1.4.1.17270.50.2.1.1.1008"),
    ECM_DOCS_DEV_CURRENT_SOFTWARE_VERSION_WITHOUT_INDEX("docsDevSwCurrentVers", "1.3.6.1.2.1.69.1.3.5"),
    ECM_DOCS_DEV_SW_FILE_NAME("docsdevSwFilename", ".1.3.6.1.2.1.69.1.3.2.0"),
    ECM_SERVER_ADDRESS_TYPE_CDL("docsDevSwServerAddressType", "1.3.6.1.2.1.69.1.3.6.0"),
    ECM_DOCS_DEVSW_ADMIN_STATAUS("docsDevSwAdminStatus", ".1.3.6.1.2.1.69.1.3.3.0"),
    ECM_DOCS_DEV_SW_OPER_STATUS("docsDevOperStatus", "1.3.6.1.2.1.69.1.3.4.0"),
    ESTB_REBOOT_DEVICE("rebootDevice", "1.3.6.1.4.1.17270.50.2.1.1.1.0"),
    ESTB_WIFI_RESTORE_DEVICE("wifiRestoreDevice", "1.3.6.1.4.1.17270.50.2.1.1.1003"),
    ECM_CURRENT_SSID("currentSSID", "1.3.6.1.4.1.17270.50.2.2.2.1.1.3"),
    ENABLE_DISABLE_BRIDGE_MODE("enableDisableBridgeMode","1.3.6.1.4.1.17270.50.2.3.2.1.1","32"),
    ESTB_FACTORY_RESET_DEVICE("factoryResetDevice", "1.3.6.1.4.1.17270.50.2.1.1.1002.0"),
    ECM_STATUS_PRIVATE_WIFI_2_4_GHZ("privateWifiStatus2.4",".1.3.6.1.4.1.17270.50.2.2.2.1.1.2","10001"),
    HOME_SECURITY_2_4_SSID_STATUS("HomeSecurityssidStatus2.4", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.2", "10002"),
    HOT_SPOT_2_4_SSID_STATUS("HotSpotStatus2.4", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.2", "10004"),
    LNF_2_4_SSID_STATUS("LnfStatus2.4", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.2", "10006"),
    ECM_STATUS_PRIVATE_WIFI_5_GHZ("privateWifiStatus5",".1.3.6.1.4.1.17270.50.2.2.2.1.1.2","10101"),
    WIFI_2_4_SSID_STATUS("ssidStatus2.4", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.2", "10001"),
    HOT_SPOT_5_SSID_STATUS("HotSpotStatus5", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.2", "10104"),
    LNF_5_SSID_STATUS("LnfStatus5", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.2", "10106"),
    PUBLIC_5_SSID_STATUS("HomeSecurityssidStatus5", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.2", "10103"),
    ECM_PRIVATE_WIFI_SSID_2_4("privateSsid2.4", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.3", "10001"),
    ECM_PRIVATE_WIFI_SSID_5("privateSsid5", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.3", "10101"),
    ECM_PRIVATE_WIFI_2_4_WIRELESSPASS("privateWifiWirelessPass2.4", "1.3.6.1.4.1.17270.50.2.2.3.3.1.3", "10001"),
    ECM_PRIVATE_WIFI_5_WIRELESSPASS("privateWifiWirelessPass5", "1.3.6.1.4.1.17270.50.2.2.3.3.1.3", "10101"),
    ENABLE_DISABLE_WIFI_RADIO_2_4_GHZ("radioStatus2.4","1.3.6.1.4.1.17270.50.2.2.6.1.1.1","10000"),
    ENABLE_DISABLE_WIFI_RADIO_5_GHZ("radioStatus5","1.3.6.1.4.1.17270.50.2.2.6.1.1.1","10100"),
    ECM_WIFI_2_4_SSID_STATUS("ssidStatus2.4", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.2.10001"),
    ECM_WIFI_2_4_CHANNEL_INFO("wifi2GhzChannelInfo","1.3.6.1.4.1.17270.50.2.2.6.1.1.3", "10000"),
    ECM_WIFI_5_CHANNEL_INFO("wifi2GhzChannelInfo","1.3.6.1.4.1.17270.50.2.2.6.1.1.3", "10100"),
    ECM_DOCS_DEV_SW_SERVER_ADDRESS("docsDevSwServerAddres", "1.3.6.1.2.1.69.1.3.7.0"),
    ECM_PRIVATE_WIFI_2_4_PASSPHRASE("privateWifiPassphrase2.4", ".1.3.6.1.4.1.17270.50.2.2.3.1.1.2", "10001"),
    ECM_PRIVATE_WIFI_5_PASSPHRASE("privateWifiPassphrase5", ".1.3.6.1.4.1.17270.50.2.2.3.1.1.2", "10101"),
    ECM_WIFI_5_SSID_STATUS("ssidStatus5", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.2.10101"),
    ECM_SELF_HEAL_CONFIGURATION("selfHealConfiguration", ".1.3.6.1.4.1.17270.44.1.1"),
    ECM_MAXIMUM_SUB_SYSTEM_RESET_COUNT_FOR_SELF_HEAL("maximumSubsystemResetCount", ".1.3.6.1.4.1.17270.44.1.1.11.0"),
    ECM_CAPTIVE_PORTAL_ENABLE("captivePortalEnable", ".1.3.6.1.4.1.17270.50.2.1.1.1007"),
    ECM_WIFI_SSID_2_4("ssid2.4", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.3.10001"),
    ECM_WIFI_SSID_5("ssid5", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.3.10101"),
    ECM_SYS_DESCR("sysDescr", "1.3.6.1.2.1.1.1", "0"),
    ECM_DEFAULT_SSID("defaultSSID", "1.3.6.1.4.1.17270.50.2.2.2.1.1.16"),
    ECM_SERIAL_NUMBER("getSerialNumber", "1.3.6.1.2.1.69.1.1.4", "0"),
    ECM_CABLE_INTERFACE_MAC_ADDRESS("cableInterfaceMacAddress", "1.3.6.1.2.1.2.2.1.6", "2"),
    ECM_MODEM_CONFIGURATION_FILENAME("modemConfigurationFilename", "1.3.6.1.2.1.69.1.4.5", "0"),
    EMC_EMTA_ADDRESS("emtaAddress", "1.3.6.1.2.1.2.2.1.6", "16"),
    ESTB_SYS_DESCRIPTION("sysDescr", ".1.3.6.1.2.1.1","1"),
    /** OIDs for getting device physical mac addresses. */
    ECM_IF_MIB_IF_PHYSICAL_ADDRESS("ifPhysAddress", ".1.3.6.1.2.1.2.2.1.6"),
    ECM_DOCS_DEV_SW_OPERATION_STATUS("docsDevSwOperStatus", ".1.3.6.1.2.1.69.1.3.4"),
    ECM_DOCS_DEV_SW_SERVER_TRANSPORT_PROTOCOL("docsDevSwServerTransportProtocol", ".1.3.6.1.2.1.69.1.3.8"),
    ECM_DOCS_DEV_SW_SERVER_ADDRESS_TYPE("docsDevSwServerAddressType", ".1.3.6.1.2.1.69.1.3.6"), 
    ECM_DOCS_DEV_SW_SERVER_ADDRESS_PROTOCOL("docsdevswserveraddressprotocol", "1.3.6.1.2.1.69.1.3.7.0"),
    ECM_FWUPGRADE_AND_FACTORYRESET("rdkbRgDeviceFWupdateAndFactoryReset","1.3.6.1.4.1.17270.50.2.1.1.1008."),
    ECM_DOCS_DEV_START_CODE_DOWNLOAD("docsDevStartCodeDownload", "1.3.6.1.2.1.69.1.3.3.0"),
    ESTB_SYS_UP_TIME("sysUpTime", ".1.3.6.1.2.1.1.3"),
    ESTB_RDKB_RUNNING("factoryResetDevice", "1.3.6.1.4.1.17270.50.2.1.1.1006"),
    WIFI_APPLY_SETTINGS("applySettings",".1.3.6.1.4.1.17270.50.2.2.1001"),
	
    /** OIDs for geting device serial number. */
    EMTA_TELEPHONE_LINE_RESET_NEGATIVE_SCENARIO("mtaReset", "1.3.6.1.2.1.2.2.1.7.9", "0"),
    ECM_DOCS_CABLE_DEVICE_MIB_DOCS_DEV_SERIAL_NUMBER("docsDevSerialNumber", "1.3.6.1.2.1.69.1.1.4.0"),
    ECM_RESET_MIB("docsdevresetnow", ".1.3.6.1.2.1.69.1.1.3"),
    EMTA_TELEPHONE_LINE_RESET_POSITIVE_SCENARIO("mtaReset", "1.3.6.1.2.1.2.2.1.7", "9"),
    ESTB_DOCS_IS_EVENT_COUNT("docsisEventCount", "1.3.6.1.2.1.69.1.5.8.1.4"),
    ESTB_DOCS_IS_EVENT_TEXT("docsisEventText", "1.3.6.1.2.1.69.1.5.8.1.7"),
    ESTB_DOCS_DEV_SERVER_BOOT_STATE("docsDevServerBootState", ".1.3.6.1.2.1.69.1.4.1"),
    ECM_DOCS_IF_CM_STATUS_TX_POWER("docsIfCmStatusTxPower", "1.3.6.1.2.1.10.127.1.2.2.1.3"),
    ECM_DOCS_IF_DOWN_CHANNEL_POWER_MIB("docsIfDownChannelPower", "1.3.6.1.2.1.10.127.1.1.1.1.6"),
    ECM_DOCS_IF_SIG_Q_SIGNAL_NOISE("docsIfSigQSignalNoise", "1.3.6.1.2.1.10.127.1.1.4.1.5"),
    ECM_DOCSIS_SIGNAL_QUALITY_EXTENDED_RX_MER_TABLE("docsIf3SignalQualityExtTable", ".1.3.6.1.4.1.4491.2.1.20.1.24.1.1"),
    DEFAULT_PRIVATE_WIFI_SSID_2_4("defaultSSID2", "1.3.6.1.4.1.17270.50.2.2.2.1.1.16", "10001"),
    DEFAULT_PRIVATE_WIFI_SSID_5("defaultSSID5", "1.3.6.1.4.1.17270.50.2.2.2.1.1.16", "10101"),
    DEFAULT_PRIVATE_WIFI_PASSWORD_2_4("defaultPassword2", ".1.3.6.1.4.1.17270.50.2.2.3.1.1.4", "10001"),
    DEFAULT_PRIVATE_WIFI_PASSWORD_5("defaultPassword5", "1.3.6.1.4.1.17270.50.2.2.3.1.1.4", "10101"),
    ECM_SELFHEAL_NO_OF_PINGS_PER_SERVER("numPingsPerServer", ".1.3.6.1.4.1.17270.44.1.1.2"),
    ECM_SELFHEAL_MIN_NO_OF_PING_SERVER("minNumPingServer", ".1.3.6.1.4.1.17270.44.1.1.3"),
    ECM_SELFHEAL_PING_INTERVAL("pingInterval", ".1.3.6.1.4.1.17270.44.1.1.4"),
    ECM_SELFHEAL_PING_RESPONSE_WAIT_TIME("pingRespWaitTime", ".1.3.6.1.4.1.17270.44.1.1.5"),
    ECM_SELFHEAL_RESOURCE_USAGE_COMPUTER_WINDOW("resourceUsageComputeWindow", ".1.3.6.1.4.1.17270.44.1.1.7"),
    ECM_SELFHEAL_AVG_CPU_THRESHOLD("avgCPUThreshold", ".1.3.6.1.4.1.17270.44.1.1.8"),
    ECM_SELFHEAL_MAX_REBOOT_COUNT("maxRebootCount", ".1.3.6.1.4.1.17270.44.1.1.10"),
    ECM_SELFHEAL_MAX_SUB_SYSTEM_RESET_COUNT("maximumSubsystemResetCount", ".1.3.6.1.4.1.17270.44.1.1.11"),
    ECM_SELFHEAL_AVG_MEMORY_THRESHOLD("avgMemoryThreshold", ".1.3.6.1.4.1.17270.44.1.1.9"),
    ECM_PRIVATE_2_4GHZ_SSID_ADVERTISEMENT_ENABLED("ssidadvertisementenabledforprivate2_4ghz", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.5","10001"),
    ECM_PRIVATE_5GHZ_SSID_ADVERTISEMENT_ENABLED("ssidadvertisementenabledforprivate5ghz", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.5","10101"),
    ECM_CM_ETHERNET_OPER_SETTING("cmEthernetOperSetting","1.3.6.1.2.1.2.2.1.5","1"),
    ECM_DEV_MAX_CPE("DevMaxCPE" ,".1.3.6.1.2.1.69.1.1.7","0"),
    ECM_DEV_SW_OPER_STATUS("DevSwOperStatus",".1.3.6.1.2.1.69.1.3.4","0"),
    ECM_DEV_SERVER_DHCP_ADDRESS("DevServerDHCPAddress" ,".1.3.6.1.2.1.69.1.4.7","0"),
    ECM_DEV_SERVER_DHCP_TYPE("DevServerDHCPType",".1.3.6.1.2.1.69.1.4.6","0"),
    ECM_DATE("Date",".1.3.6.1.2.1.69.1.1.2","0"),
    ECM_STATIC_WAN_IPV6("rdkbRgDeviceConfigStaticIpv6",".1.3.6.1.4.1.17270.50.2.1.4.7","0"),
    ECM_DOCSIS_SIGNAL_QUALITY_EXTENDED_RX_MER_SAMPLES_TABLE(
	    "docsIf3SignalQualityExtRxMerSamples",
	    ".1.3.6.1.4.1.4491.2.1.20.1.24.1.2"),
    ECM_IPV4_PING_SERVER_TABLE_ONE("ipv4pingservertableone", ".1.3.6.1.4.1.17270.44.1.1.6.1.1.2", "1"),
    ECM_IPV4_PING_SERVER_TABLE_ONE_ADD("ipv4pingservertableoneadd", ".1.3.6.1.4.1.17270.44.1.1.6.1.1.3", "1"),
    ECM_IPV4_PING_SERVER_TABLE_TWO_ADD("ipv4pingservertabletwoadd", ".1.3.6.1.4.1.17270.44.1.1.6.1.1.3", "2"),
    ECM_IPV4_PING_SERVER_TABLE_TWO("ipv4pingservertabletwo", ".1.3.6.1.4.1.17270.44.1.1.6.1.1.2", "2"),
    ECM_IPV4_PING_SERVER_TABLE_THREE_ADD("ipv4pingservertablethreeadd", ".1.3.6.1.4.1.17270.44.1.1.6.1.1.3", "3"),
    ECM_IPV4_PING_SERVER_TABLE_THREE("ipv4pingservertablethree", ".1.3.6.1.4.1.17270.44.1.1.6.1.1.2", "3"),
    ECM_IPV6_PING_SERVER_TABLE_ONE_ADD("ipv6pingservertableoneadd", ".1.3.6.1.4.1.17270.44.1.1.6.2.1.3", "1"),
    ECM_IPV6_PING_SERVER_TABLE_ONE("ipv6pingservertableone", ".1.3.6.1.4.1.17270.44.1.1.6.2.1.2", "1"),
    ECM_IPV6_PING_SERVER_TABLE_TWO("ipv6pingservertabletwo", ".1.3.6.1.4.1.17270.44.1.1.6.2.1.2", "2"),
    ECM_IPV6_PING_SERVER_TABLE_TWO_ADD("ipv6pingservertabletwoadd", ".1.3.6.1.4.1.17270.44.1.1.6.2.1.3", "2"),
    ECM_IPV6_PING_SERVER_TABLE_THREE_ADD("ipv6pingservertablethreeadd", ".1.3.6.1.4.1.17270.44.1.1.6.2.1.3", "3"),
    ECM_IPV6_PING_SERVER_TABLE_THREE("ipv6pingservertablethree", ".1.3.6.1.4.1.17270.44.1.1.6.2.1.2", "3"),
    ECM_WIFI_RADIO_MODE_2_4("radiowifisecuritymode2.4", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.4", "10001"),
    ECM_WIFI_RADIO_MODE_5("radiowifisecuritymode5", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.4", "10101"),
    ESTB_VERIFY_MOCA_INTERFACE_STATUS("verifyMocaInterfaceStatus", " .1.3.6.1.4.1.31621.1.1.1.1.1.1"),
    FACTORY_RESET_DEVICE("FactoryReset","1.3.6.1.4.1.17270.50.2.1.1.1002"),
    ECM_STATIC_WAN_IPV4("rdkbRgDeviceConfigStaticIp", ".1.3.6.1.4.1.17270.50.2.1.4.6", "0"),																							
    ECM_DOCS_DEV_SW_IPV4_SERVER_ADDRESS("docsDevSwipv4ServerAddres", "1.3.6.1.2.1.69.1.3.1"),
    ECM_DIAGNOSTIC_MODE("diagnosticMode", ".1.3.6.1.4.1.17270.44.1.1.12.0"),
    ECM_LOG_UPLOAD_FREQUENCY("logUploadFrequency", ".1.3.6.1.4.1.17270.44.1.1.13.0"),
    ECM_BAND_STEERING("rdkbRgDot11BandSteeringEnable",".1.3.6.1.4.1.17270.50.2.2.8.2","0");
    
    private String mibName;

    private String oidValue;

    private String tableIndex;
    
    /**
     * Enumeration constructor.
     * 
     * @param key
     *            The SNMP MIB name.
     * @param value
     *            The OID Value
     */
    private BroadBandSnmpMib(String key, String value) {
	this.mibName = key;
	this.oidValue = value;
    }
    /**
     * Enumeration constructor.
     * 
     * @param key
     *            The SNMP MIB name.
     * @param value
     *            The OID Value
     * @param value
     *            The Expected Output
     */
    private BroadBandSnmpMib(String key, String value, String tableIndex) {
	this.mibName = key;
	this.oidValue = value;
	this.tableIndex = tableIndex;
    }

    /**
     * Method to get the OID value.
     * 
     * @return The OID value
     */
    public String getOid() {
	return this.oidValue;
    }

    /**
     * Method to get the MIB name.
     * 
     * @return The mib name.
     */
    public String getName() {
	return this.mibName;
    }

    /**
     * Method to get the Table Index.
     * 
     * @return The Table Index.
     */
    public String getTableIndex() {
	return this.tableIndex;
    }
}

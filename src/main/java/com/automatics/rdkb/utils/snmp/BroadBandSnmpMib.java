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

    ECM_DOCS_DEV_CURRENT_SOFTWARE_VERSION("docsDevSwCurrentVers", "1.3.6.1.2.1.69.1.3.5.0"),
    ECM_CURRENT_SSID("currentSSID", "1.3.6.1.4.1.17270.50.2.2.2.1.1.3"),
    ENABLE_DISABLE_BRIDGE_MODE("enableDisableBridgeMode","1.3.6.1.4.1.17270.50.2.3.2.1.1","32"),
    ECM_STATUS_PRIVATE_WIFI_2_4_GHZ("privateWifiStatus2.4",".1.3.6.1.4.1.17270.50.2.2.2.1.1.2","10001"),
    HOME_SECURITY_2_4_SSID_STATUS("HomeSecurityssidStatus2.4", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.2", "10002"),
    HOT_SPOT_2_4_SSID_STATUS("HotSpotStatus2.4", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.2", "10004"),
    LNF_2_4_SSID_STATUS("LnfStatus2.4", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.2", "10006"),
    ECM_STATUS_PRIVATE_WIFI_5_GHZ("privateWifiStatus5",".1.3.6.1.4.1.17270.50.2.2.2.1.1.2","10101"),
    HOT_SPOT_5_SSID_STATUS("HotSpotStatus5", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.2", "10104"),
    LNF_5_SSID_STATUS("LnfStatus5", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.2", "10106"),
    ECM_PRIVATE_WIFI_SSID_2_4("privateSsid2.4", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.3", "10001"),
    ECM_PRIVATE_WIFI_SSID_5("privateSsid5", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.3", "10101"),
    ECM_PRIVATE_WIFI_2_4_WIRELESSPASS("privateWifiWirelessPass2.4", "1.3.6.1.4.1.17270.50.2.2.3.3.1.3", "10001"),
    ECM_PRIVATE_WIFI_5_WIRELESSPASS("privateWifiWirelessPass5", "1.3.6.1.4.1.17270.50.2.2.3.3.1.3", "10101"),
    ENABLE_DISABLE_WIFI_RADIO_2_4_GHZ("radioStatus2.4","1.3.6.1.4.1.17270.50.2.2.6.1.1.1","10000"),
    ENABLE_DISABLE_WIFI_RADIO_5_GHZ("radioStatus5","1.3.6.1.4.1.17270.50.2.2.6.1.1.1","10100"),
    ECM_WIFI_2_4_CHANNEL_INFO("wifi2GhzChannelInfo","1.3.6.1.4.1.17270.50.2.2.6.1.1.3", "10000"),
    ECM_WIFI_5_CHANNEL_INFO("wifi2GhzChannelInfo","1.3.6.1.4.1.17270.50.2.2.6.1.1.3", "10100"),
    ECM_DOCS_DEV_SW_SERVER_ADDRESS("docsDevSwServerAddres", "1.3.6.1.2.1.69.1.3.7"),
    ECM_PRIVATE_WIFI_2_4_PASSPHRASE("privateWifiPassphrase2.4", ".1.3.6.1.4.1.17270.50.2.2.3.1.1.2", "10001"),
    ECM_PRIVATE_WIFI_5_PASSPHRASE("privateWifiPassphrase5", ".1.3.6.1.4.1.17270.50.2.2.3.1.1.2", "10101"),
    ECM_CAPTIVE_PORTAL_ENABLE("captivePortalEnable", ".1.3.6.1.4.1.17270.50.2.1.1.1007"),
    ECM_WIFI_SSID_2_4("ssid2.4", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.3.10001"),
    ECM_WIFI_SSID_5("ssid5", ".1.3.6.1.4.1.17270.50.2.2.2.1.1.3.10101"),
    ECM_SYS_DESCR("sysDescr", "1.3.6.1.2.1.1.1", "0"),
    ECM_DEFAULT_SSID("defaultSSID", "1.3.6.1.4.1.17270.50.2.2.2.1.1.16"),
    
    /** OIDs for getting device physical mac addresses. */
    ECM_IF_MIB_IF_PHYSICAL_ADDRESS("ifPhysAddress", ".1.3.6.1.2.1.2.2.1.6"),
	
    /** OIDs for geting device serial number. */
    ECM_DOCS_CABLE_DEVICE_MIB_DOCS_DEV_SERIAL_NUMBER("docsDevSerialNumber", "1.3.6.1.2.1.69.1.1.4.0");
    
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

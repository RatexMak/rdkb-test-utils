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

import java.util.ArrayList;
import java.util.List;

import com.automatics.rdkb.utils.snmp.BroadBandSnmpMib;
import com.automatics.snmp.SnmpDataType;

public class BroadBandSnmpConstants {

    public static enum SNMP_MODE {
   	SET,
   	GET,
   	SET_GET;
       }
    
    /** SNMP MIB details */
    public static enum BROADBAND_WAREHOUSE_SNMP_LIST {
	WAREHOUSE_GENERAL_EMTA_DETECTION(
		"1.3.6.1.2.1.2.2.1.16.",
		"16",
		"emta detection",
		SnmpDataType.STRING,
		"",
		SNMP_MODE.GET),
	WAREHOUSE_WIRELESS_SSID_ENABLE1(
		"1.3.6.1.4.1.17270.50.2.2.2.1.1.2",
		"10002",
		"wireless ssid enable",
		SnmpDataType.INTEGER,
		"2",
		SNMP_MODE.SET_GET),
	WAREHOUSE_WIRELESS_SSID_ENABLE2(
		"1.3.6.1.4.1.17270.50.2.2.2.1.1.2",
		"10004",
		"wireless ssid enable",
		SnmpDataType.INTEGER,
		"2",
		SNMP_MODE.SET_GET),
	WAREHOUSE_WIRELESS_SSID_ENABLE3(
		"1.3.6.1.4.1.17270.50.2.2.2.1.1.2",
		"10006",
		"wireless ssid enable",
		SnmpDataType.INTEGER,
		"2",
		SNMP_MODE.SET_GET),
	WAREHOUSE_WIRELESS_SSID_ENABLE1_5G(
		"1.3.6.1.4.1.17270.50.2.2.2.1.1.2",
		"10104",
		"wireless ssid enable 5g",
		SnmpDataType.INTEGER,
		"2",
		SNMP_MODE.SET_GET),
	WAREHOUSE_WIRELESS_SSID_PASSWORD_2G(
		"1.3.6.1.4.1.17270.50.2.2.3.3.1.3",
		"10001",
		"wireless password",
		SnmpDataType.STRING,
		"TEST_SSID",
		SNMP_MODE.SET_GET),
	WAREHOUSE_WIRELESS_SSID_PASSWORD_5G(
		"1.3.6.1.4.1.17270.50.2.2.3.3.1.3",
		"10101",
		"wireless password 5g",
		SnmpDataType.STRING,
		"TEST_PASSWORD12#",
		SNMP_MODE.SET_GET),
	WAREHOUSE_WIFI_2_4_OID_1(
		"1.3.6.1.4.1.17270.50.2.2.6.1.1.3",
		"10000",
		"wireless channel 2g",
		SnmpDataType.UNSIGNED_INTEGER,
		"5",
		SNMP_MODE.SET_GET),
	WAREHOUSE_WIFI_5_0_OID_1(
		"1.3.6.1.4.1.17270.50.2.2.6.1.1.3",
		"10100",
		"wireless channel 5g",
		SnmpDataType.UNSIGNED_INTEGER,
		"36",
		SNMP_MODE.SET_GET),
	WAREHOUSE_WIFI_2_4_OID(
		"1.3.6.1.4.1.17270.50.2.2.6.1.1.18",
		"10000",
		"2g channel get",
		SnmpDataType.INTEGER,
		"",
		SNMP_MODE.GET),
	WAREHOUSE_WIFI_5_0_OID(
		"1.3.6.1.4.1.17270.50.2.2.6.1.1.18",
		"10100",
		"5g channel get",
		SnmpDataType.INTEGER,
		"",
		SNMP_MODE.GET),
	WAREHOUSE_FACTORY_RESET_1(
		"1.3.6.1.4.1.4413.2.2.2.1.2.1.6",
		"0",
		"factory reset",
		SnmpDataType.INTEGER,
		"1",
		SNMP_MODE.SET),
	WAREHOUSE_UPGRADE_WITH_RESET(
		"1.3.6.1.4.1.17270.50.2.1.1.1008",
		"0",
		"upgrade with reset",
		SnmpDataType.INTEGER,
		"0",
		SNMP_MODE.SET_GET);

	String oid;
	String info;
	String tableIndex;
	SnmpDataType dataType;
	String value;
	SNMP_MODE mode;

	private BROADBAND_WAREHOUSE_SNMP_LIST(String oid, String tableIndex, String info, SnmpDataType dataType,
		String value, SNMP_MODE mode) {
	    this.oid = oid;
	    this.info = info;
	    this.dataType = dataType;
	    this.value = value;
	    this.mode = mode;
	    this.tableIndex = tableIndex;

	}

	/**
	 * @return the tableIndex
	 */
	public String getTableIndex() {
	    return tableIndex;
	}

	/**
	 * @param tableIndex
	 *            the tableIndex to set
	 */
	public void setTableIndex(String tableIndex) {
	    this.tableIndex = tableIndex;
	}

	/**
	 * @return the oid
	 */
	public String getOid() {
	    return oid;
	}

	/**
	 * @return the oid
	 */
	public String getMibOid() {
	    return oid+"."+tableIndex;
	}
	
	/**
	 * @return the info
	 */
	public String getInfo() {
	    return info;
	}

	/**
	 * @return the dataType
	 */
	public SnmpDataType getDataType() {
	    return dataType;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
	    return value;
	}

	/**
	 * @return the mode
	 */
	public SNMP_MODE getMode() {
	    return mode;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(SNMP_MODE mode) {
	    this.mode = mode;
	}

    }
    
    /** SNMP MIB details for RPi */
    public static enum BROADBAND_WAREHOUSE_SNMP_LIST_RPI {
	WAREHOUSE_GENERAL_EMTA_DETECTION(
		"1.3.6.1.2.1.2.2.1.16.",
		"16",
		"emta detection",
		SnmpDataType.STRING,
		"",
		SNMP_MODE.GET),
	WAREHOUSE_WIRELESS_SSID_PASSWORD_2G(
		"1.3.6.1.4.1.17270.50.2.2.3.3.1.3",
		"10001",
		"wireless password",
		SnmpDataType.STRING,
		"TEST_SSID",
		SNMP_MODE.SET_GET),
	WAREHOUSE_WIRELESS_SSID_PASSWORD_5G(
		"1.3.6.1.4.1.17270.50.2.2.3.3.1.3",
		"10101",
		"wireless password 5g",
		SnmpDataType.STRING,
		"TEST_PASSWORD12#",
		SNMP_MODE.SET_GET),
	WAREHOUSE_WIFI_2_4_OID_1(
		"1.3.6.1.4.1.17270.50.2.2.6.1.1.3",
		"10000",
		"wireless channel 2g",
		SnmpDataType.UNSIGNED_INTEGER,
		"5",
		SNMP_MODE.SET_GET),
	WAREHOUSE_WIFI_5_0_OID_1(
		"1.3.6.1.4.1.17270.50.2.2.6.1.1.3",
		"10100",
		"wireless channel 5g",
		SnmpDataType.UNSIGNED_INTEGER,
		"36",
		SNMP_MODE.SET_GET),
	WAREHOUSE_WIFI_2_4_OID(
		"1.3.6.1.4.1.17270.50.2.2.6.1.1.18",
		"10000",
		"2g channel get",
		SnmpDataType.INTEGER,
		"",
		SNMP_MODE.GET),
	WAREHOUSE_WIFI_5_0_OID(
		"1.3.6.1.4.1.17270.50.2.2.6.1.1.18",
		"10100",
		"5g channel get",
		SnmpDataType.INTEGER,
		"",
		SNMP_MODE.GET),
	WAREHOUSE_FACTORY_RESET_1(
		"1.3.6.1.4.1.4413.2.2.2.1.2.1.6",
		"0",
		"factory reset",
		SnmpDataType.INTEGER,
		"1",
		SNMP_MODE.SET);

	String oid;
	String info;
	String tableIndex;
	SnmpDataType dataType;
	String value;
	SNMP_MODE mode;

	private BROADBAND_WAREHOUSE_SNMP_LIST_RPI(String oid, String tableIndex, String info, SnmpDataType dataType,
		String value, SNMP_MODE mode) {
	    this.oid = oid;
	    this.info = info;
	    this.dataType = dataType;
	    this.value = value;
	    this.mode = mode;
	    this.tableIndex = tableIndex;

	}

	/**
	 * @return the tableIndex
	 */
	public String getTableIndex() {
	    return tableIndex;
	}

	/**
	 * @param tableIndex
	 *            the tableIndex to set
	 */
	public void setTableIndex(String tableIndex) {
	    this.tableIndex = tableIndex;
	}

	/**
	 * @return the oid
	 */
	public String getOid() {
	    return oid;
	}

	/**
	 * @return the oid
	 */
	public String getMibOid() {
	    return oid+"."+tableIndex;
	}
	
	/**
	 * @return the info
	 */
	public String getInfo() {
	    return info;
	}

	/**
	 * @return the dataType
	 */
	public SnmpDataType getDataType() {
	    return dataType;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
	    return value;
	}

	/**
	 * @return the mode
	 */
	public SNMP_MODE getMode() {
	    return mode;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(SNMP_MODE mode) {
	    this.mode = mode;
	}

    }
    
    
    
    /** snmp response for Non fiber devices for Emta Detection */
    public static final String SNMP_RESPONSE_FOR_EMTA_DETECTION_NON_FIBER_DEVICES = "PacketCable Embedded Interface";

    /** snmp response for fiber devices for Emta Detection */
    public static final String SNMP_RESPONSE_FOR_EMTA_DETECTION_FIBER_DEVICES = "pon0";
    
    /** SNMP Error response for No such Object */
    public static final String SNMP_ERROR_RESPONSE_NO_OBJECT = "No Such Object available on this agent at this OID";

    /** SNMP Error response for No such OID */
    public static final String SNMP_ERROR_RESPONSE_NO_OID = "No Such Instance currently exists at this OID";
    
    /** SNMP Error response for unknown Object */
    public static final String SNMP_ERROR_UNKNOWN_OBJECT = "Unknown Object Identifier";
    
    /**
     * Snmp command to retrieve Security mode of 2.4Ghz access point
     */
    public static final String SNMP_COMMAND_2_4_GHZ_SECURITY_MODE = "1000<REPLACE>";

    /**
     * Snmp command to retrieve Security mode of 5Ghz access point
     */
    public static final String SNMP_COMMAND_5_GHZ_SECURITY_MODE = "1010<REPLACE>";
    
    /** SNMP Error response for No writable */
    public static final String SNMP_ERROR_NON_WRITABLE = "notWritable";
    

    /** DOCSIS MIBs */
    public static enum BROADBAND_WAREHOUSE_DOCSIS_SNMP_LIST {
	WAREHOUSE_RF_SECTION_DOWNSTREAM_FREQUENCY(
		"1.3.6.1.2.1.10.127.1.1.1.1.2",
		"3",
		"downstream frequency",
		SnmpDataType.INTEGER,
		"",
		SNMP_MODE.GET),
	WAREHOUSE_RF_SECTION_DOWNSTREAM_SNR(
		"1.3.6.1.2.1.10.127.1.1.4.1.5",
		"3",
		"downstream snr",
		SnmpDataType.INTEGER,
		"",
		SNMP_MODE.GET),
	WAREHOUSE_RF_SECTION_UPSTREAM_POWER(
		"1.3.6.1.2.1.10.127.1.2.2.1.3",
		"2",
		"upstream power",
		SnmpDataType.INTEGER,
		"",
		SNMP_MODE.GET),
	WAREHOUSE_RF_SECTION_RF_STATUS(
		"1.3.6.1.2.1.69.1.4.1",
		"0",
		"rf status",
		SnmpDataType.INTEGER,
		"",
		SNMP_MODE.GET),
	WAREHOUSE_GENERAL_BOOT_FILE(
		"1.3.6.1.2.1.69.1.4.5",
		"0",
		"boot file info",
		SnmpDataType.STRING,
		"",
		SNMP_MODE.GET),
	WAREHOUSE_RF_SECTION_LEVEL(
		"1.3.6.1.4.1.4491.2.1.20.1.24.1.1",
		"3",
		"RF level",
		SnmpDataType.INTEGER,
		"",
		SNMP_MODE.GET),
	WAREHOUSE_UPGRADE_PROTOCOL(
		"1.3.6.1.2.1.69.1.3.8",
		"0",
		"upgrade protocol",
		SnmpDataType.INTEGER,
		"2",
		SNMP_MODE.SET_GET),
	WAREHOUSE_UPGRADE_STATUS(
		"1.3.6.1.2.1.69.1.3.3",
		"0",
		"upgrade status",
		SnmpDataType.INTEGER,
		"3",
		SNMP_MODE.SET_GET),
	WAREHOUSE_UPGRADE_FILE(
		"1.3.6.1.2.1.69.1.3.2",
		"0",
		"upgrade file",
		SnmpDataType.STRING,
		"test.bin.ccs",
		SNMP_MODE.SET_GET),
	WAREHOUSE_IPV6_TYPE("1.3.6.1.2.1.69.1.3.6", "0", "ipv6 type", SnmpDataType.INTEGER, "2", SNMP_MODE.SET_GET),
	WAREHOUSE_UPGRADE_SERVER(
		"1.3.6.1.2.1.69.1.3.7",
		"0",
		"upgrade server",
		SnmpDataType.HEXADECIMAL,
		"0x26001f18227b0c01b1613d177a86fe36",
		SNMP_MODE.SET_GET);

	String oid;
	String info;
	String tableIndex;
	SnmpDataType dataType;
	String value;
	SNMP_MODE mode;

	private BROADBAND_WAREHOUSE_DOCSIS_SNMP_LIST(String oid, String tableIndex, String info, SnmpDataType dataType,
		String value, SNMP_MODE mode) {
	    this.oid = oid;
	    this.info = info;
	    this.dataType = dataType;
	    this.value = value;
	    this.mode = mode;
	    this.tableIndex = tableIndex;

	}

	/**
	 * @return the tableIndex
	 */
	public String getTableIndex() {
	    return tableIndex;
	}

	/**
	 * @param tableIndex
	 *            the tableIndex to set
	 */
	public void setTableIndex(String tableIndex) {
	    this.tableIndex = tableIndex;
	}

	/**
	 * @return the oid
	 */
	public String getOid() {
	    return oid;
	}
	
	/**
	 * @return the final oid
	 */
	public String getMibOid() {
	    return oid +"."+tableIndex;
	}

	/**
	 * @return the info
	 */
	public String getInfo() {
	    return info;
	}

	/**
	 * @return the dataType
	 */
	public SnmpDataType getDataType() {
	    return dataType;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
	    return value;
	}

	/**
	 * @return the mode
	 */
	public SNMP_MODE getMode() {
	    return mode;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	public void setMode(SNMP_MODE mode) {
	    this.mode = mode;
	}

    }
    
    /**
     * List for all rdk ssid index values
     */
    public static final List<String> RDKB_WIFI_STATUS_SNMP_MIBS = new ArrayList<String>() {
	{
	    add(BroadBandSnmpMib.ECM_STATUS_PRIVATE_WIFI_2_4_GHZ.getTableIndex());
	    add(BroadBandSnmpMib.ECM_STATUS_PRIVATE_WIFI_5_GHZ.getTableIndex());
	    add(BroadBandSnmpMib.HOME_SECURITY_2_4_SSID_STATUS.getTableIndex());
	    add(BroadBandSnmpMib.HOME_SECURITY_5_SSID_STATUS.getTableIndex());
	    add(BroadBandSnmpMib.HOT_SPOT_2_4_SSID_STATUS.getTableIndex());
	    add(BroadBandSnmpMib.HOT_SPOT_5_SSID_STATUS.getTableIndex());
	    add(BroadBandSnmpMib.LNF_2_4_SSID_STATUS.getTableIndex());
	    add(BroadBandSnmpMib.LNF_5_SSID_STATUS.getTableIndex());
	}
    };
    /**
     * List for all rdk ssid index values for Business Class
     */
    public static final List<String> RDKB_WIFI_STATUS_SNMP_MIBS_BUSINESSCLASS = new ArrayList<String>() {
	{
	    add(BroadBandSnmpMib.ECM_STATUS_PRIVATE_WIFI_2_4_GHZ.getTableIndex());
	    add(BroadBandSnmpMib.ECM_STATUS_PRIVATE_WIFI_5_GHZ.getTableIndex());
	}
    };

}

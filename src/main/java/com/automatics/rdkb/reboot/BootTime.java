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
package com.automatics.rdkb.reboot;

/**
 * Class for all boot time values
 * 
 * @author Arun V S
 * @refactor Govardhan
 */
public class BootTime {
	
    /** Variable for boot parameter Downstream Lock Success */
    private long downstreamLockSuccessTime;

    /** Variable for boot parameter CM DHCP START */
    private long cmDhcpStartTime;

    /** Variable for boot parameter boot_to_MOCA_uptime */
    private long bootToMocaUptime;

    /** Variable for boot parameter boot_to_ETH_uptime */
    private long bootToEthUptime;

    /** Variable for boot parameter boot_to_WIFI_uptime */
    private long bootToWifiUptime;

    /** Variable for boot parameter boot_to_WIFI_uptime */
    private long bootToXhomeUptime;

    /** Variable for boot parameter boot_to_XHOME_uptime */
    private long cmDchpEndTime;

    /** Variable for boot parameter CM DHCP END */
    private long cmOperationalTime;

    /** Variable for boot parameter WEBPA READY */
    private long bootToWebpaTime;

    /** Variable for boot parameter boot_to_LnF_SSID_uptime */
    public long lnfSSIDUptime;

    /** Variable for boot parameter boot_to_snmp_subagent_uptime */
    public long snmpSubAgentUptime;

    /** Variable for boot parameter boot_to_tr069_uptime */
    public long tr69Uptime;

    /** Variable for boot parameter boot_to_wan_uptime */
    public long wanUptime;

    /** Variable for boot parameter boot_to_WEBPA_READY_uptime */
    public long webpaUptime;
	
    public enum BootTimePatterns {

 	DOWNSTREAM_LOCK_SUCCESS_TIME("Downstream Lock Success=(\\d+)"),
 	CM_DHCP_START_TIME("CM DHCP START=(\\d+)"),
 	BOOT_TO_MOCA_UPTIME("boot_to_MOCA_uptime=(\\d+)"),
 	BOOT_TO_ETH_UPTIME("boot_to_ETH_uptime=(\\d+)"),
 	BOOT_TO_WIFI_UPTIME("boot_to_WIFI_uptime=(\\d+)"),
 	BOOT_TO_XHOME_UPTIME("boot_to_XHOME_uptime=(\\d+)"),
 	CM_DHCP_END_TIME("CM DHCP END=(\\d+)"),
 	CM_IS_OPERATIONAL_TIME("CM is Operational=(\\d+)"),
 	BOOT_TO_LNF_SSID_UPTIME("boot_to_LnF_SSID_uptime=(\\d+)"),
 	BOOT_TO_TR69_UPTIME("boot_to_tr069_uptime=(\\d+)"),
 	BOOT_TO_SNMP_V2_SUBAGENT_UPTIME("boot_to_snmp_subagent_v2_uptime=(\\d+)"),
 	BOOT_TO_SNMP_V3_SUBAGENT_UPTIME("boot_to_snmp_subagent_v3_uptime=(\\d+)"),
 	BOOT_TO_WAN_TIME("boot_to_wan_uptime=(\\d+)"),
 	BOOT_TO_WEBPA_TIME("boot_to_WEBPA_READY_uptime=(\\d+)");

 	String pattern;

 	public String getPattern() {
 	    return pattern;
 	}

 	private BootTimePatterns(String pattern) {
 	    this.pattern = pattern;
 	}
     }
    
    public long getBootToMocaUptime() {
	return bootToMocaUptime;
    }
   

    public void setBootToMocaUptime(long bootToMocaUptime) {
	this.bootToMocaUptime = bootToMocaUptime;
    }
    
    /**
     * 
     * Getters and setters for above variables
     * 
     * @return
     */
    public void setLnfSSIDUptime(long lnfSSIDUptime) {
	this.lnfSSIDUptime = lnfSSIDUptime;
    }

    public long getLnfSSIDUptime() {
	return lnfSSIDUptime;
    }
    
    public long getBootToWifiUptime() {
	return bootToWifiUptime;
    }

    public void setBootToWifiUptime(long bootToWifiUptime) {
	this.bootToWifiUptime = bootToWifiUptime;
    }

    public long getBootToEthUptime() {
	return bootToEthUptime;
    }

    public void setBootToEthUptime(long bootToEthUptime) {
	this.bootToEthUptime = bootToEthUptime;
    }
    
    public long getCmOperationalTime() {
	return cmOperationalTime;
    }

    public void setCmOperationalTime(long cmOperationalTime) {
	this.cmOperationalTime = cmOperationalTime;
    }
    
    public long getDownstreamLockSuccessTime() {
	return downstreamLockSuccessTime;
    }

    public void setDownstreamLockSuccessTime(long downstreamLockSuccessTime) {
	this.downstreamLockSuccessTime = downstreamLockSuccessTime;
    }
    

    public long getSnmpSubAgentUptime() {
	return snmpSubAgentUptime;
    }

    public void setSnmpSubAgentUptime(long snmpSubAgentUptime) {
	this.snmpSubAgentUptime = snmpSubAgentUptime;
    }
    
    public long getTr69Uptime() {
	return tr69Uptime;
    }

    public void setTr69Uptime(long tr69Uptime) {
	this.tr69Uptime = tr69Uptime;
    }

    public long getWanUptime() {
	return wanUptime;
    }

    public void setWanUptime(long wanUptime) {
	this.wanUptime = wanUptime;
    }

    public long getWebpaUptime() {
	return webpaUptime;
    }

    public void setWebpaUptime(long webpaUptime) {
	this.webpaUptime = webpaUptime;
    }
    
    public long getCmDchpEndTime() {
	return cmDchpEndTime;
    }

    public void setCmDchpEndTime(long cmDchpEndTime) {
	this.cmDchpEndTime = cmDchpEndTime;
    }
    
    public long getCmDhcpStartTime() {
	return cmDhcpStartTime;
    }

    public void setCmDhcpStartTime(long cmDhcpStartTime) {
	this.cmDhcpStartTime = cmDhcpStartTime;
    }

}

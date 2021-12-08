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
}

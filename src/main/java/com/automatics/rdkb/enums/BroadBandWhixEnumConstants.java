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
package com.automatics.rdkb.enums;


public class BroadBandWhixEnumConstants {
	public static enum WEBPA_AP_INDEXES {
		PRIVATE_WIFI("10001", "10101", "1", "2"),
		PUBLIC_WIFI("10003", "10103", "5", "6"),
		PHASE1_LNF("10004", "10104", "7", "8"),
		POD_SNR("10007", "10107", "13", "14");
		
		private String index_24;
		private String index_5;
		private String dmcli_24;
		private String dmcli_5;

		public String get24Ghz() {
		    return this.index_24;
		}

		public String get5Ghz() {
		    return this.index_5;
		}

		public String getDmcliIndex24Ghz() {
		    return this.dmcli_24;
		}

		public String getDmcliIndex5Ghz() {
		    return this.dmcli_5;
		}

		public void setValue(String index_24, String index_5, String dmcli_24, String dmcli_5) {
		    this.index_24 = index_24;
		    this.index_5 = index_5;
		    this.dmcli_24 = dmcli_24;
		    this.dmcli_5 = dmcli_5;
		}

		WEBPA_AP_INDEXES(String index_24, String index_5, String dmcli_24, String dmcli_5) {
		    this.index_24 = index_24;
		    this.index_5 = index_5;
		    this.dmcli_24 = dmcli_24;
		    this.dmcli_5 = dmcli_5;
		}
	}

}

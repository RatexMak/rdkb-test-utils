/* Copyright 2021 Comcast Cable Communications Management, LLC
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
package com.automatics.rdkb.webui.page;

public class BroadBandWifiPage {

	/**
	 * Enum class containing different Wi-Fi bandwidth available
	 * 
	 * @author Praveen Kumar
	 *
	 */
	public enum SSIDFrequency {
		FREQUENCY_2_4_GHZ("2.4"), FREQUENCY_5_GHZ("5"), BOTH_BAND("BOTH");

		private String stringValue = null;

		private SSIDFrequency(String frequency) {
			this.stringValue = frequency;
		}

		/**
		 * Method to get string value.
		 * 
		 * @return string value
		 */
		public String getValue() {
			return stringValue;
		}
	}
}

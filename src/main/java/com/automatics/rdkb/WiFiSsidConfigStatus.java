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
package com.automatics.rdkb;

/**
 * 
 * Representation of Wi-Fi SSID configuration status.
 * 
 * @author Selvaraj Mariyappan
 * @refactor Govardhan
 */
public class WiFiSsidConfigStatus {

	/**
	 * SSID Name.
	 */
	private String name;

	/**
	 * SSID passphrase.
	 */
	private String pswd;

	/**
	 * SSID status.
	 */
	private String status;

	/**
	 * SSID Security Mode.
	 */
	private String SecurityMode;

	/**
	 * SSID enabled status.
	 */
	private boolean enable;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the pswd
	 */
	public String getPswd() {
		return pswd;
	}

	/**
	 * @param pswd the pswd to set
	 */
	public void setPswd(String pswd) {
		this.pswd = pswd;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the securityMode
	 */
	public String getSecurityMode() {
		return SecurityMode;
	}

	/**
	 * @param securityMode the securityMode to set
	 */
	public void setSecurityMode(String securityMode) {
		SecurityMode = securityMode;
	}

	/**
	 * @return the enable
	 */
	public boolean isEnable() {
		return enable;
	}

	/**
	 * @param enable the enable to set
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	@Override
	public String toString() {

		return new StringBuffer().append(" \r Ssid Name : ").append(name).append("  \r Ssid password : ").append(pswd)
				.append("  \r Security Mode :").append(SecurityMode).append("  \r isEnabled : ").append(enable)
				.append("  \r CurrentStatus : ").append(status).append("\n").toString();
	}
}

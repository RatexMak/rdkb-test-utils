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
 * Representation of Broadband Device info.
 * 
 * @author Selvaraj Mariyappan
 * @author Gnanaprakasham.S
 * @refactor Govardhan
 */
public class BroadBandDeviceSsidInfo {

	private String private2ghzSsidName;
	private String private2ghzSsidPwd;
	private String private2ghzSsidSecurityMode;
	private String private5ghzSsidName;
	private String private5ghzSsidPwd;
	private String private5ghzSsidSecurityMode;

	private String publicOpen5GhzSsidName;
	private String publicOpen2GhzSsidName;
	private String publicSecure5GhzSsidName;

	private String lnf2GhzSsidName;
	private String lnf2GhzSsidPwd;
	private String lnf2ghzSsidSecurityMode;
	private String lnf5GhzSsidName;
	private String lnf5GhzSsidPwd;
	private String lnf5ghzSsidSecurityMode;
	
	private String gwIpAddr;
	private String lanSubnetMask;
	private String dhcpMinAddr;
	private String dhcpMaxAddr;

	/**
	 * @return the private2ghzSsidName
	 */
	public String getPrivate2ghzSsidName() {
		return private2ghzSsidName;
	}

	/**
	 * @param private2ghzSsidName
	 *            the private2ghzSsidName to set
	 */
	public void setPrivate2ghzSsidName(String private2ghzSsidName) {
		this.private2ghzSsidName = private2ghzSsidName;
	}

	/**
	 * @return the private2ghzSsidPwd
	 */
	public String getPrivate2ghzSsidPwd() {
		return private2ghzSsidPwd;
	}

	/**
	 * @param private2ghzSsidPwd
	 *            the private2ghzSsidPwd to set
	 */
	public void setPrivate2ghzSsidPwd(String private2ghzSsidPwd) {
		this.private2ghzSsidPwd = private2ghzSsidPwd;
	}

	/**
	 * @return the private5ghzSsidName
	 */
	public String getPrivate5ghzSsidName() {
		return private5ghzSsidName;
	}

	/**
	 * @param private5ghzSsidName
	 *            the private5ghzSsidName to set
	 */
	public void setPrivate5ghzSsidName(String private5ghzSsidName) {
		this.private5ghzSsidName = private5ghzSsidName;
	}

	/**
	 * @return the private5ghzSsidPwd
	 */
	public String getPrivate5ghzSsidPwd() {
		return private5ghzSsidPwd;
	}

	/**
	 * @param private5ghzSsidPwd
	 *            the private5ghzSsidPwd to set
	 */
	public void setPrivate5ghzSsidPwd(String private5ghzSsidPwd) {
		this.private5ghzSsidPwd = private5ghzSsidPwd;
	}

	/**
	 * @return the publicOpen5GhzSsidName
	 */
	public String getPublicSeOpen5GhzSsidName() {
		return publicOpen5GhzSsidName;
	}

	/**
	 * @param publicOpen5GhzSsidName
	 *            the publicOpen5GhzSsidName to set
	 */
	public void setPublicOpen5GhzSsidName(String publicOpen5GhzSsidName) {
		this.publicOpen5GhzSsidName = publicOpen5GhzSsidName;
	}

	/**
	 * @return the publicOpen2GhzSsidName
	 */
	public String getPublicSeOpen2GhzSsidName() {
		return publicOpen2GhzSsidName;
	}

	/**
	 * @param publicOpen2GhzSsidName
	 *            the publicOpen2GhzSsidName to set
	 */
	public void setPublicOpen2GhzSsidName(String publicOpen2GhzSsidName) {
		this.publicOpen2GhzSsidName = publicOpen2GhzSsidName;
	}

	/**
	 * @return the publicSecure5GhzSsidName
	 */
	public String getPublicSecure5GhzSsidName() {
		return publicSecure5GhzSsidName;
	}

	/**
	 * @param publicSecure5GhzSsidName
	 *            the publicSecure5GhzSsidName to set
	 */
	public void setPublicSecure5GhzSsidName(String publicSecure5GhzSsidName) {
		this.publicSecure5GhzSsidName = publicSecure5GhzSsidName;
	}

	/**
	 * @return the Lnf2GhzSsidName
	 */
	public String getLnf2GhzSsidName() {
		return lnf2GhzSsidName;
	}

	/**
	 * @param Lnf2GhzSsidName
	 *            the Lnf2GhzSsidName to set
	 */
	public void setLnf2GhzSsidName(String lnf2GhzSsidName) {
		this.lnf2GhzSsidName = lnf2GhzSsidName;
	}

	/**
	 * @return the Lnf2GhzSsidPwd
	 */
	public String getLnf2GhzSsidPwd() {
		return lnf2GhzSsidPwd;
	}

	/**
	 * @param Lnf2GhzSsidPwd
	 *            the Lnf2GhzSsidPwd to set
	 */
	public void setLnf2GhzSsidPwd(String lnf2GhzSsidPwd) {
		this.lnf2GhzSsidPwd = lnf2GhzSsidPwd;
	}

	/**
	 * @return the Lnf5GhzSsidName
	 */
	public String getLnf5GhzSsidName() {
		return lnf5GhzSsidName;
	}

	/**
	 * @param Lnf5GhzSsidName
	 *            the Lnf5GhzSsidName to set
	 */
	public void setLnf5GhzSsidName(String lnf5GhzSsidName) {
		this.lnf5GhzSsidName = lnf5GhzSsidName;
	}

	/**
	 * @return the Lnf5GhzSsidPwd
	 */
	public String getLnf5GhzSsidPwd() {
		return lnf5GhzSsidPwd;
	}

	/**
	 * @param Lnf5GhzSsidPwd
	 *            the Lnf5GhzSsidPwd to set
	 */
	public void setLnf5GhzSsidPwd(String lnf5GhzSsidPwd) {
		this.lnf5GhzSsidPwd = lnf5GhzSsidPwd;
	}

	/**
	 * @return the Private2ghzSsidSecurityMode
	 */
	public String getPrivate2ghzSsidSecurityMode() {
		return private2ghzSsidSecurityMode;
	}

	/**
	 * @param Private2ghzSsidSecurityMode
	 *            the Private2ghzSsidSecurityMode to set
	 */
	public void setPrivate2ghzSsidSecurityMode(String private2ghzSsidSecurityMode) {
		this.private2ghzSsidSecurityMode = private2ghzSsidSecurityMode;
	}

	/**
	 * @return the Private5ghzSsidSecurityMode
	 */
	public String getPrivate5ghzSsidSecurityMode() {
		return private5ghzSsidSecurityMode;
	}

	/**
	 * @param Private5ghzSsidSecurityMode
	 *            the Private5ghzSsidSecurityMode to set
	 */
	public void setPrivate5ghzSsidSecurityMode(String private5ghzSsidSecurityMode) {
		this.private5ghzSsidSecurityMode = private5ghzSsidSecurityMode;
	}

	/**
	 * @return the Lnf2ghzSsidSecurityMode
	 */
	public String getLnf2ghzSsidSecurityMode() {
		return lnf2ghzSsidSecurityMode;
	}

	/**
	 * @param Lnf2ghzSsidSecurityMode
	 *            the Lnf2ghzSsidSecurityMode to set
	 */
	public void setLnf2ghzSsidSecurityMode(String lnf2ghzSsidSecurityMode) {
		this.lnf2ghzSsidSecurityMode = lnf2ghzSsidSecurityMode;
	}

	/**
	 * @return the Lnf5ghzSsidSecurityMode
	 */
	public String getLnf5ghzSsidSecurityMode() {
		return lnf5ghzSsidSecurityMode;
	}

	/**
	 * @param Lnf5ghzSsidSecurityMode
	 *            the Lnf5ghzSsidSecurityMode to set
	 */
	public void setLnf5ghzSsidSecurityMode(String lnf5ghzSsidSecurityMode) {
		this.lnf5ghzSsidSecurityMode = lnf5ghzSsidSecurityMode;
	}
	
	/**
	 * @return the gwIpAddr
	 */
	public String getGwIpAddr() {
		return gwIpAddr;
	}

	/**
	 * @param gwIpAddr the gwIpAddr to set
	 */
	public void setGwIpAddr(String gwIpAddr) {
		this.gwIpAddr = gwIpAddr;
	}

	/**
	 * @return the lanSubnetMask
	 */
	public String getLanSubnetMask() {
		return lanSubnetMask;
	}

	/**
	 * @param lanSubnetMask the lanSubnetMask to set
	 */
	public void setLanSubnetMask(String lanSubnetMask) {
		this.lanSubnetMask = lanSubnetMask;
	}

	/**
	 * @return the dhcpMinAddr
	 */
	public String getDhcpMinAddr() {
		return dhcpMinAddr;
	}

	/**
	 * @param dhcpMinAddr the dhcpMinAddr to set
	 */
	public void setDhcpMinAddr(String dhcpMinAddr) {
		this.dhcpMinAddr = dhcpMinAddr;
	}

	/**
	 * @return the dhcpMaxAddr
	 */
	public String getDhcpMaxAddr() {
		return dhcpMaxAddr;
	}

	/**
	 * @param dhcpMaxAddr the dhcpMaxAddr to set
	 */
	public void setDhcpMaxAddr(String dhcpMaxAddr) {
		this.dhcpMaxAddr = dhcpMaxAddr;
	}

}

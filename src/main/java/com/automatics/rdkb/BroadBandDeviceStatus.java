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
 * Represents the Broadband device status.
 * 
 * @author Selvaraj Mariyappan
 * @refactor Govardhan
 */
public class BroadBandDeviceStatus {


	/**
	 * Device router mode status.
	 */
	private boolean routerModeStatus;
	/**
	 * Device MoCA status.
	 */
	private boolean mocaStatus;

	/**
	 * Device Mesh enabled status
	 */
	private boolean meshStatus;

	/**
	 * Advance security status.
	 */
	private boolean advanceSecurityStatus;
	/**
	 * Wi-Fi Private SSID 2.4 GHz status.
	 */
	private boolean privateWiFi2GhzStatus;
	/**
	 * Wi-Fi Private SSID 5 GHz status.
	 */
	private boolean privateWiFi5GhzStatus;

	/**
	 * xfinity Wi-Fi 2.4 GHz(open) SSID status.
	 */
	private boolean xfinityWiFi2GhzOpenStatus;
	/**
	 * xfinity Wi-Fi 5 GHz(open) SSID status.
	 */
	private boolean xfinityWiFi5GhzOpenStatus;

	/**
	 * xfinity Wi-Fi 5 GHz(secure) SSID status.
	 */
	private boolean xfinityWiFi5GhzSecureStatus;

	/**
	 * Aker enabled status.
	 */
	private boolean akerEnabled;

	/**
	 * Device MoCA enabled status.
	 */
	private boolean mocaEnable;

	/**
	 * Current operating channel for Wi-Fi radio 2.4 GHz.
	 */
	private String radio2gChanNum;

	/**
	 * Current operating channel for Wi-Fi radio 5 GHz.
	 */
	private String radio5gChanNum;

	/**
	 * Wi-Fi radio 2.4 GHz auto channel status.
	 */
	private boolean radio2gAutoChanStatus;
	/**
	 * Wi-Fi radio 5 GHz auto channel status.
	 */
	private boolean radio5gAutoChanStatus;

	/**
	 * xDNS enabled status.
	 */
	private boolean xdnsEnabled;

	/**
	 * xFi Cloud UI Enable Status
	 */
	private boolean cloudUiEnabled;

	/**
	 * MTA IP address
	 */
	private boolean validMtaIpAddress;
	
	/**
	 * Partner ID
	 */

	private String partnerId;
	
	/**
	 * Syndication partner
	 */

	private boolean syndicationDevice;

	/**
	 * Code big cdl enabled status.
	 */
	private boolean codeBigEnabled;
	
	/**
	 * wps enabled status
	 */
	private boolean wps2gEnabled;
	
	/**
	 * wps enabled status
	 */
	private boolean wps5gEnabled;

	/**
	 * @return true if device enabled with code big
	 */
	public boolean isCodeBigEnabled() {
		return codeBigEnabled;
	}

	/**
	 * @param codeBigEnabled
	 *            the codeBigEnabled status to set
	 */
	public void setCodeBigEnabled(boolean codeBigEnabled) {
		this.codeBigEnabled = codeBigEnabled;
	}

	/**
	 * @return partner Id
	 */
	public String getPartnerId() {
		return partnerId;
	}

	/**
	 * @param partnerId
	 *            the partnerId to set
	 */
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	/**
	 * @return true if device has valid Mta IpAddress
	 */
	public boolean isValidMtaIpAddress() {
		return validMtaIpAddress;
	}
	/**
	 * @param validMtaIpAddress
	 *            the validMtaIpAddress status to set
	 */
	public void setValidMtaIpAddress(boolean validMtaIpAddress) {
		this.validMtaIpAddress = validMtaIpAddress;
	}

	/**
	 * @return the routerModeStatus
	 */
	public boolean isRouterModeStatus() {
		return routerModeStatus;
	}

	/**
	 * @param routerModeStatus the routerModeStatus to set
	 */
	public void setRouterModeStatus(boolean routerModeStatus) {
		this.routerModeStatus = routerModeStatus;
	}

	/**
	 * @return the mocaStatus
	 */
	public boolean isMocaStatus() {
		return mocaStatus;
	}

	/**
	 * @param mocaStatus the mocaStatus to set
	 */
	public void setMocaStatus(boolean mocaStatus) {
		this.mocaStatus = mocaStatus;
	}

	/**
	 * @return the meshStatus
	 */
	public boolean isMeshStatus() {
		return meshStatus;
	}

	/**
	 * @param meshStatus the meshStatus to set
	 */
	public void setMeshStatus(boolean meshStatus) {
		this.meshStatus = meshStatus;
	}

	/**
	 * @return the advanceSecurityStatus
	 */
	public boolean isAdvanceSecurityStatus() {
		return advanceSecurityStatus;
	}

	/**
	 * @param advanceSecurityStatus the advanceSecurityStatus to set
	 */
	public void setAdvanceSecurityStatus(boolean advanceSecurityStatus) {
		this.advanceSecurityStatus = advanceSecurityStatus;
	}

	/**
	 * @return the privateWiFi2GhzStatus
	 */
	public boolean isPrivateWiFi2GhzStatus() {
		return privateWiFi2GhzStatus;
	}

	/**
	 * @param privateWiFi2GhzStatus the privateWiFi2GhzStatus to set
	 */
	public void setPrivateWiFi2GhzStatus(boolean privateWiFi2GhzStatus) {
		this.privateWiFi2GhzStatus = privateWiFi2GhzStatus;
	}

	/**
	 * @return the privateWiFi5GhzStatus
	 */
	public boolean isPrivateWiFi5GhzStatus() {
		return privateWiFi5GhzStatus;
	}

	/**
	 * @param privateWiFi5GhzStatus the privateWiFi5GhzStatus to set
	 */
	public void setPrivateWiFi5GhzStatus(boolean privateWiFi5GhzStatus) {
		this.privateWiFi5GhzStatus = privateWiFi5GhzStatus;
	}

	/**
	 * @return the xfinityWiFi2GhzOpenStatus
	 */
	public boolean isXfinityWiFi2GhzOpenStatus() {
		return xfinityWiFi2GhzOpenStatus;
	}

	/**
	 * @param xfinityWiFi2GhzOpenStatus the xfinityWiFi2GhzOpenStatus to set
	 */
	public void setXfinityWiFi2GhzOpenStatus(boolean xfinityWiFi2GhzOpenStatus) {
		this.xfinityWiFi2GhzOpenStatus = xfinityWiFi2GhzOpenStatus;
	}

	/**
	 * @return the xfinityWiFi5GhzOpenStatus
	 */
	public boolean isXfinityWiFi5GhzOpenStatus() {
		return xfinityWiFi5GhzOpenStatus;
	}

	/**
	 * @param xfinityWiFi5GhzOpenStatus the xfinityWiFi5GhzOpenStatus to set
	 */
	public void setXfinityWiFi5GhzOpenStatus(boolean xfinityWiFi5GhzOpenStatus) {
		this.xfinityWiFi5GhzOpenStatus = xfinityWiFi5GhzOpenStatus;
	}

	/**
	 * @return the xfinityWiFi5GhzSecureStatus
	 */
	public boolean isXfinityWiFi5GhzSecureStatus() {
		return xfinityWiFi5GhzSecureStatus;
	}

	/**
	 * @param xfinityWiFi5GhzSecureStatus the xfinityWiFi5GhzSecureStatus to set
	 */
	public void setXfinityWiFi5GhzSecureStatus(boolean xfinityWiFi5GhzSecureStatus) {
		this.xfinityWiFi5GhzSecureStatus = xfinityWiFi5GhzSecureStatus;
	}

	/**
	 * @return the wps2gEnabled
	 */
	public boolean isWps2gEnabled() {
	    return wps2gEnabled;
	}

	/**
	 * @param wps2gEnabled the wps2gEnabled to set
	 */
	public void setWps2gEnabled(boolean wps2gEnabled) {
	    this.wps2gEnabled = wps2gEnabled;
	}

	/**
	 * @return the wps5gEnabled
	 */
	public boolean isWps5gEnabled() {
	    return wps5gEnabled;
	}

	/**
	 * @param wps5gEnabled the wps5gEnabled to set
	 */
	public void setWps5gEnabled(boolean wps5gEnabled) {
	    this.wps5gEnabled = wps5gEnabled;
	}

	/**
	 * @return the akerEnabled
	 */
	public boolean isAkerEnabled() {
		return akerEnabled;
	}

	/**
	 * @param akerEnabled the akerEnabled to set
	 */
	public void setAkerEnabled(boolean akerEnabled) {
		this.akerEnabled = akerEnabled;
	}


	/**
	 * @return the mocaEnable
	 */
	public boolean isMocaEnable() {
		return mocaEnable;
	}

	/**
	 * @param mocaEnable the mocaEnable to set
	 */
	public void setMocaEnable(boolean mocaEnable) {
		this.mocaEnable = mocaEnable;
	}

	/**
	 * @return the radio2gChanNum
	 */
	public String getRadio2gChanNum() {
		return radio2gChanNum;
	}

	/**
	 * @param radio2gChanNum the radio2gChanNum to set
	 */
	public void setRadio2gChanNum(String radio2gChanNum) {
		this.radio2gChanNum = radio2gChanNum;
	}

	/**
	 * @return the radio5gChanNum
	 */
	public String getRadio5gChanNum() {
		return radio5gChanNum;
	}

	/**
	 * @param radio5gChanNum the radio5gChanNum to set
	 */
	public void setRadio5gChanNum(String radio5gChanNum) {
		this.radio5gChanNum = radio5gChanNum;
	}

	/**
	 * @return the radio2gAutoChanStatus
	 */
	public boolean isRadio2gAutoChanStatus() {
		return radio2gAutoChanStatus;
	}

	/**
	 * @param radio2gAutoChanStatus the radio2gAutoChanStatus to set
	 */
	public void setRadio2gAutoChanStatus(boolean radio2gAutoChanStatus) {
		this.radio2gAutoChanStatus = radio2gAutoChanStatus;
	}

	/**
	 * @return the radio5gAutoChanStatus
	 */
	public boolean isRadio5gAutoChanStatus() {
		return radio5gAutoChanStatus;
	}

	/**
	 * @param radio5gAutoChanStatus the radio5gAutoChanStatus to set
	 */
	public void setRadio5gAutoChanStatus(boolean radio5gAutoChanStatus) {
		this.radio5gAutoChanStatus = radio5gAutoChanStatus;
	}

	/**
	 * @return the xdnsEnabled
	 */
	public boolean isXdnsEnabled() {
		return xdnsEnabled;
	}

	/**
	 * @param xdnsEnabled the xdnsEnabled to set
	 */
	public void setXdnsEnabled(boolean xdnsEnabled) {
		this.xdnsEnabled = xdnsEnabled;
	}

	/**
	 * @return the cloudUiEnabled
	 */
	public boolean isCloudUiEnabled() {
		return cloudUiEnabled;
	}

	/**
	 * @param cloudUiEnabled the cloudUiEnabled to set
	 */
	public void setCloudUiEnabled(boolean cloudUiEnabled) {
		this.cloudUiEnabled = cloudUiEnabled;
	}

	/**
	 * @return the syndicationDevice
	 */
	public boolean isSyndicationDevice() {
	    return syndicationDevice;
	}

	/**
	 * @param syndicationDevice the syndicationDevice to set
	 */
	public void setSyndicationDevice(boolean syndicationDevice) {
	    this.syndicationDevice = syndicationDevice;
	}

	@Override
	public String toString() {
		return new StringBuffer().append(" routerModeStatus : ").append(routerModeStatus).append(" | mocaStatus : ")
				.append(mocaStatus).append(" | meshStatus : ").append(meshStatus).append(" | advanceSecurityStatus : ")
				.append(advanceSecurityStatus).append(" | akerEnabled : ").append(akerEnabled)
				.append(" | xfinityWiFi5GhzSecureStatus : ").append(xfinityWiFi5GhzSecureStatus)
				.append(" | xfinityWiFi5GhzOpenStatus : ").append(xfinityWiFi5GhzOpenStatus)
				.append(" | xfinityWiFi2GhzOpenStatus :  ").append(xfinityWiFi2GhzOpenStatus)
				.append(" | privateWiFi5GhzStatus :  ").append(privateWiFi5GhzStatus)
				.append(" | privateWiFi2GhzStatus  : ").append(privateWiFi2GhzStatus).append(" | mocaEnable : ")
				.append(mocaEnable).append(" |  radio2gChanNum : ").append(radio2gChanNum)
				.append(" | radio5gChanNum : ").append(radio5gChanNum).append(" | radio2gAutoChanStatus : ")
				.append(radio2gAutoChanStatus).append(" | radio5gAutoChanStatus : ").append(radio5gAutoChanStatus)
				.append(" | xdnsEnabled : ").append(xdnsEnabled).append(" | cloudUiEnabled : ").append(cloudUiEnabled)
				.append(" | wps2gEnabled : ").append(wps2gEnabled).append(" | wps5gEnabled : ").append(wps5gEnabled)
				.append(" | syndicationDevice : ").append(syndicationDevice).toString();
	}
}

/**
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
package com.automatics.rdkb.wt.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO class to store Station details The mandatory parameters needed to create a station in LANForge are alias, ssid,
 * psk, radio and the optional parameters are mode and ethernet_interface
 * 
 * @author Arun V S
 * @author sreejasuma
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Station {

    @JsonProperty("wtSimulatorBaseUrl")
    String wtSimulatorBaseUrl;

    @JsonProperty("disabled")
    String status;

    @JsonProperty("true")
    boolean down;

    @JsonProperty("parentDev")
    String parentDev;

    @JsonProperty("ethernetInterface")
    String ethernetInterface;

    boolean enabled;

    @JsonProperty("band")
    String band;

    @JsonProperty("connected")
    String connectionStatus;

    @JsonProperty("id")
    String id;

    @JsonProperty("ssid")
    String ssid;

    @JsonProperty("port")
    String port;

    @JsonProperty("psk")
    String psk;

    @JsonProperty("status")
    String statusOld;

    @JsonProperty("mac")
    String mac;

    @JsonProperty("connection_status")
    String connectionStatusOld;

    @JsonProperty("remoteInterface")
    String ethernet_interface;

    @JsonProperty("mode")
    String mode;

    @JsonProperty("alias")
    String alias;

    @JsonProperty("radio")
    String radio;

    /**
     * Get station mode
     * 
     * @return
     */
    public String getMode() {
	return mode;
    }

    /**
     * Set station mode (6 for 2.4Gz, 9 for 5 Gz) Optional parameter used to create a station
     * 
     * @param mode
     */
    public void setMode(String mode) {
	this.mode = mode;
    }

    /**
     * Get station alias
     * 
     * @return String
     */
    public String getwtSimulatorBaseUrl() {
	return wtSimulatorBaseUrl;
    }

    /**
     * Set alias for station (starts with sta{number}, number max upto 6500) Mandatory parameter used to create a
     * station
     * 
     * @param alias
     */
    public void setwtSimulatorBaseUrl(String wtSimulatorBaseUrl) {
	this.wtSimulatorBaseUrl = wtSimulatorBaseUrl;
    }

    /**
     * Get station alias
     * 
     * @return String
     */
    public String getAlias() {
	return alias;
    }

    /**
     * Set alias for station (starts with sta{number}, number max upto 6500) Mandatory parameter used to create a
     * station
     * 
     * @param alias
     */
    public void setAlias(String alias) {
	this.alias = alias;
    }

    /**
     * Get radio
     * 
     * @return String
     */
    public String getRadio() {
	return radio;
    }

    /**
     * Set radio (wiphy0/wiphy1) Mandatory parameter used to create a station
     * 
     * @param radio
     */
    public void setRadio(String radio) {
	this.radio = radio;
    }

    /**
     * Get mac address
     * 
     * @return
     */

    public String getMac() {
	return mac;
    }

    /***
     * Set mac address. (Not needed for LANForge)
     * 
     * @param mac
     */

    public void setMac(String mac) {
	this.mac = mac;
    }

    /**
     * Deprecated
     * 
     * @return
     */

    public String getStatusOld() {
	return statusOld;
    }

    /**
     * Deprecated
     * 
     * @param statusOld
     */

    public void setStatusOld(String statusOld) {
	this.statusOld = statusOld;
    }

    /**
     * Deprecated
     * 
     * @return
     */

    public String getConnectionStatusOld() {
	return connectionStatusOld;
    }

    /**
     * Deprecated
     * 
     * @param connectionStatusOld
     */
    /*
     * public void setConnectionStatusOld(String connectionStatusOld) { this.connectionStatusOld = connectionStatusOld;
     * }
     */

    /**
     * Get psk
     * 
     * @return
     */
    public String getPsk() {
	return psk;
    }

    /**
     * Set psk Mandatory parameter used to create a station
     * 
     * @param psk
     */
    public void setPsk(String psk) {
	this.psk = psk;
    }

    /**
     * Get status
     * 
     * @return
     */

    public String getStatus() {
	return status;
    }

    /**
     * Sets status User doesn't need to set this parameter
     * 
     * @param status
     */

    public void setStatus(String status) {
	this.status = status;
    }

    /**
     * Get Downstatus
     * 
     * @return
     */

    public boolean getDownStatus() {
	return down;
    }

    /**
     * Sets downstatus User doesn't need to set this parameter
     * 
     * @param status
     */

    public void setDownStatus(boolean down) {
	this.down = down;
    }

    /**
     * Get parentDev
     * 
     * @return
     */

    public String getParentDev() {
	return parentDev;
    }

    /**
     * Sets parentDev
     * 
     * @param parentDev
     */

    public void setParentDev(String parentDev) {
	this.parentDev = parentDev;
    }

    /**
     * Get parentDev
     * 
     * @return
     */

    public String getEthernetInterface() {
	return ethernetInterface;
    }

    /**
     * Sets parentDev
     * 
     * @param parentDev
     */

    public void setEthernetInterface(String ethernetInterface) {
	this.ethernetInterface = ethernetInterface;
    }

    /**
     * Get band
     * 
     * @return
     */
    public String getBand() {
	return band;
    }

    /**
     * Set band User doesn't need to set this parameter
     * 
     * @param band
     */

    public void setBand(String band) {
	this.band = band;
    }

    /**
     * Get connection status
     * 
     * @return
     */

    public String getConnectionStatus() {
	return connectionStatus;
    }

    /**
     * Sets connection staus User doesn't need to set this parameter
     * 
     * @param connectionStatus
     */

    public void setConnectionStatus(String connectionStatus) {
	this.connectionStatus = connectionStatus;
    }

    /**
     * Get Id
     * 
     * @return
     */

    public String getId() {
	return id;
    }

    /**
     * Set Id User doesn't need to set this parameter
     * 
     * @param id
     */

    public void setId(String id) {
	this.id = id;
    }

    /**
     * Get ssid
     * 
     * @return
     */
    public String getSsid() {
	return ssid;
    }

    /**
     * Set ssid Mandatory parameter used to create a station
     * 
     * @param ssid
     */
    public void setSsid(String ssid) {
	this.ssid = ssid;
    }

    /**
     * Get port
     * 
     * @return
     */
    public String getPort() {
	return port;
    }

    /**
     * Set port for station
     * 
     * @param alias
     */
    public void setPort(String port) {
	this.port = port;
    }

    /**
     * Returns enabled
     * 
     * @return
     */

    public boolean isEnabled() {
	return enabled;
    }

    /**
     * Sets enable flag User doesn't need to set this parameter
     * 
     * @param enabled
     */

    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }

    /**
     * Get ethernet interface
     * 
     * @return
     */
    public String getEthernet_interface() {
	return ethernet_interface;
    }

    /**
     * Set ethernet interface (For LANForge) Optional parameter used to create a station. If not set default parameter
     * will be set (ie eth1)
     * 
     * @param ethernet_interface
     */
    public void setEthernet_interface(String ethernet_interface) {
	this.ethernet_interface = ethernet_interface;
    }

    public String toString() {
	String s = " status : " + status + " id : " + id + " connectionStatus : " + connectionStatus + " band : " + band
		+ " ssid : " + ssid + " mac : " + mac + "Ethernet Interface : " + ethernet_interface;
	return s;
    }

}
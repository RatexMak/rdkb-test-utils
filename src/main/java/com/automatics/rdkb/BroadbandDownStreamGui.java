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
 * BroadbandDownStreamGui is used to store values from Downstream values from GUI
 * 
 * @author prasanthreddy.a
 *
 */

public class BroadbandDownStreamGui {
    /**
     * String to store index
     */
    private String index = null;
    /**
     * String to store lock status
     */
    private String lockStatus = null;
    /**
     * String to store frequency
     */
    private String frequency = null;
    /**
     * String to store snr level
     */
    private String snr = null;
    /**
     * String to store power level
     */
    private String powerLevel = null;
    /**
     * String to store modulation
     */
    private String modulation = null;

    /**
     * 
     * @return index
     * 
     */
    public String getIndex() {
	return index;
    }

    /**
     * 
     * @param index
     *            set index value
     */
    public void setIndex(String index) {
	this.index = index;
    }

    /**
     * 
     * @return lockStatus
     */
    public String getLockStatus() {
	return lockStatus;
    }

    /**
     * 
     * @param lockStatus
     *            set lockstatus value
     */
    public void setLockStatus(String lockStatus) {
	this.lockStatus = lockStatus;
    }

    /**
     * 
     * @return frequency
     */
    public String getFrequency() {
	return frequency;
    }

    /**
     * 
     * @param frequency
     *            set frequency value
     */
    public void setFrequency(String frequency) {
	this.frequency = frequency;
    }

    /**
     * 
     * @return snr
     */
    public String getSnr() {
	return snr;
    }

    /**
     * 
     * @param snr
     *            set snr level value
     */
    public void setSnr(String snr) {
	this.snr = snr;
    }

    /**
     * 
     * @return powerLevel
     */
    public String getPowerLevel() {
	return powerLevel;
    }

    /**
     * 
     * @param powerLevel
     *            set power level value
     */
    public void setPowerLevel(String powerLevel) {
	this.powerLevel = powerLevel;
    }

    /**
     * 
     * @return modulation
     */
    public String getModulation() {
	return modulation;
    }

    /**
     * 
     * @param modulation
     *            set modulation value
     */
    public void setModulation(String modulation) {
	this.modulation = modulation;
    }

}

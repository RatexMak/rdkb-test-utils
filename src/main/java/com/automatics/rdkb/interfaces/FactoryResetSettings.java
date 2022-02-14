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


package com.automatics.rdkb.interfaces;

import com.automatics.device.Dut;
import com.automatics.tap.AutomaticsTapApi;

/**
 * Factory Reset Settings option
 * 
 * @author Praveenkumar Paneerselvam
 * @Refactor Athira
 *
 */

public interface FactoryResetSettings {
    /**
     * Method to get default value for the Settings
     * 
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param device
     *            Dut instance,
     * @return default value of the settings. Returns null if default value cannot be found.
     * @author Praveenkumar Paneerselvam
     * @Refactor Athira
     */
    public String getDefaultValue(AutomaticsTapApi tapEnv, Dut device);

    /**
     * Method to set settings value on Device
     * 
     * @param tapEnv
     *            AutomaticsTapApi Instance
     * @param device
     *            Dut Object instance
     * @param value
     *            value to be set
     * @return true, if value is set successfully
     * @author Praveenkumar Paneerselvam
     * @Refactor Athira
     */
    public boolean updateSettingsValueOnDevice(AutomaticsTapApi tapEnv, Dut device, String value);

    /**
     * @return WebPA Parameter for the setting option
     * @author Praveenkumar Paneerselvam
     * @Refactor Athira
     */
    public String getWebpaParameter();

    /**
     * @return getSystem default varialbe
     * @author Praveenkumar Paneerselvam
     * @Refactor Athira
     */
    public String getSystemDefaultVariable();
}

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
package com.automatics.rdkb.webui.constants;

public class BroadBandWebGuiElements {
    /**
     * Variable to store the web element of start up button in captive portal
     */
    public static final String ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_START_UP_BUTTON = "//*[@id=\"get_set_up\"]";

    /** Variable to store the web element of Header in captive portal Page */
    public static final String ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_PAGE_HEADER = "//*[@id=\"personalize\"]/h1";
    
    /** Element id to find Text box to configure SSID */
    public static final String ELEMENT_ID_CAPTIVE_PORTAL_CONFIGURE_WIFI_SSID_TEXTBOX = "WiFi_Name";

    /** Element id to find Text box to configure Wi-Fi password */
    public static final String ELEMENT_ID_CAPTIVE_PORTAL_CONFIGURE_WIFI_PASSWORD_TEXTBOX = "WiFi_Password";

    /**
     * Variable to store the web element of next button in captive portal Page
     */
    public static final String ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_PAGE_NEXT_BUTTON = "//*[@id=\"button_next\"]";

    /** Variable to store the web element of Header in captive portal Page */
    public static final String ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_CONFIRMATION_PAGE_HEADER = "//*[@id=\"confirm\"]/h1";
    
    /**
     * Variable to store the web element of next button in captive portal Page
     */
    public static final String ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_CONFIRMATION_PAGE_NEXT_BUTTON = "//*[@id=\"button_next_01\"]";

    /** Variable to store the web element of Header in captive portal Page */
    public static final String ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_COMPLETION_PAGE_HEADER = "//*[@id=\"setup\"]/h1";

    /** Variable to store the web element of Header in captive portal Page */
    public static final String ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_SUCCESS_PAGE_HEADER = "//*[@id=\"ready\"]/h1";

}

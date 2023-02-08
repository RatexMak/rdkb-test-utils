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

import org.openqa.selenium.By;

public class BroadBandWebGuiTestConstant {

    /** Variable to store Admin page URL */
    public static final String ADMIN_PAGE_URL = "Admin.page.url";

    /** Variable to store Admin page URL for Business Class Device */
    public static final String ADMIN_PAGE_URL_BUSINESS_CLASS = "Admin.page.url.business";

    /** Variable to store SSH command to get the admin page default password */
    public static final String SSH_GET_DEFAULT_PASSWORD_BUSINESS_CLASS = "syscfg get user_password_2";

    /** Variable to store SSH command to get the admin page default password */
    public static final String SSH_GET_DEFAULT_PASSWORD = "syscfg get user_password_3";

    /** Variable to store Admin page PASSWORD */
    public static final String ADMIN_PAGE_PASSWORD = "Admin.password";

    /** Variable to store SSH command to get the admin page default username */
    public static final String SSH_GET_DEFAULT_USERNAME_BUSINESS_CLASS = "syscfg get user_name_2";

    /** Variable to store SSH command to get the admin page default username */
    public static final String SSH_GET_DEFAULT_USERNAME = "syscfg get user_name_3";

    /** Variable to store webui home page page heading element id */
    public static String ELEMENT_ID_HOME_PAGE_HEADING = "//*[@id=\"content\"]/h1";

    /** Variable to store webui login page heading for business class devices */
    public static String STRING_WEB_GUI_LOGIN_PAGE_HEADING_BCI = "Admin Tool Login";

    /**
     * Variable to store webui login page heading element id for business class devices
     */
    public static String ELEMENT_ID_LOGIN_PAGE_HEADING_BCI = "//*[@id=\"main-content\"]/h1";

    /** Variable to store webui login page heading */
    public static String STRING_WEB_GUI_LOGIN_PAGE_HEADING = "Gateway > Login";

    /** Variable to store webui login page heading element id */
    public static String ELEMENT_ID_LOGIN_PAGE_HEADING = "//*[@id=\"content\"]/h1";

    /** Constant to hold Alert Title for deleting Port Forward Rule */
    public static final String STRING_ARE_YOU_SURE_ALERT_MESSAGE = "Are You Sure?";

    /** xpath to get pop up message title */
    public static final String XPATH_TO_GET_POP_TITLE = "//*[@id=\"popup_title\"]";

    /** xpath for popup message obtained on clicking enable/disable buttons */
    public static final By XPATH_FOR_POPUP_MESSAGE = By.xpath("//*[@id=\"popup_message\"]");

    /** xpath for ok button in popup message */
    public static final By XPATH_FOR_OK_IN_POPUP_MESSAGE = By.xpath("//*[@id=\"popup_ok\"]");

    /** Enter old password Xpath */
    public static final String XPATH_ENTER_OLD_PASSWORD = "//*[@id=\"oldPassword\"]";

    /** Enter New User password Xpath */
    public static final String XPATH_ENTER_NEW_USER_PASSWORD = "//*[@id=\"userPassword\"]";

    /** Re-Enter/Verify New User password Xpath */
    public static final String XPATH_VERIFY_RE_ENTER_NEW_USER_PASSWORD = "//*[@id=\"verifyPassword\"]";

    /** Save New User password Xpath */
    public static final String XPATH_SAVE_NEW_USER_PASSWORD = "//*[@id=\"submit_pwd\"]";

    /** Click Ok button after saving password Xpath */
    public static final String XPATH_CLICK_OK_AFTER_SAVING_NEW_PASSWORD = "//*[@id=\"popup_ok\"]";

    /** Variable to store the xpath for clicking typical security */
    public static String XPATH_FOR_TYPICAL_SECURITY = "//*[@id='default']/label";

    /** Variable to store the save settings of Custom firewall */
    public static String XPATH_FOR_SAVE_SETTINGS = "//*[@id='submit_firewall']";

    /** Xpath for ipv4 subnet mask common id name */
    public static final String XPATH_IPV4_SUBNET_MASK = "ipv4_subnet_mask_";

    /** xpath for custom security */
    public static final String XPATH_FOR_CUSTOM_SECURITY = "//*[text() = 'Custom Security']";

    /** Variable to store the xpath for block icmp */
    public static String XPATH_FOR_BLOCK_ICMP = "//*[@id='block_icmp'] | //*[@id=\"firmess13\"]";

    /** AdminUI Connection > Wifi Xpath */
    public static final String XPATH_LAN_UI_WIFI_PAGE = "//a[@href='wireless_network_configuration.php']";

    /** Wifi Edit page Xpath */
    public static final String XPATH_WIFI_2_4GHz_EDIT_PAGE = "//a[@href='wireless_network_configuration_edit.php?id=1']";

    /** Pattern for Login attempt alert */
    public static final String PATTERN_LOGIN_ATTEMPT_ALERT = "There are\\s+\\d+\\s+remaining login attempts for remind me later option; after\\s+\\d+\\s+attempts, you will be required to change your password.";

    /** Pattern for Login default password alert */
    public static final String PATTERN_DEFAULT_PASSWORD_ALERT = "You are using default password. Please change the password.";

    /** Pattern for Login password saved changes alert */
    public static final String PATTERN_PASSWORD_CHANGED_ALERT = "Changes saved successfully.Please login with the new password.";

    /** UI Link text for Wizard */
    public static final String LINK_TEXT_HARDWARE_WIZARD = "Wizard";

    /** AdminUI Advanced > Remote Management Link Text */
    public static final String LINK_TEXT_CONNECTED_DEVICES = "Connected Devices";

    /** AdminUI Connection Link Text */
    public static final String LINK_TEXT_CONNECTION = "Connection";

    /** Advanced > Wi-Fi Page Title */
    public static final String PAGE_TITLE_WIFI_SNDTE = "Gateway > Connection > WiFi - <REPLACE>";

    /** String to hold .php value of url */
    public static final String DOT_PHP = ".php";

    /** String to hold .jst value of url */
    public static final String DOT_JST = ".jst";

    /** Variable to store http */
    public static String STRING_HTTP = "http://";

    /** String 20/40 to channel bandwidth value in Wifi page */
    public static final String STRING_CHANNEL_BANDWIDTH_20_40 = "20/40";

    /** String 20/40/80 to channel bandwidth value in Wifi page */
    public static final String STRING_CHANNEL_BANDWIDTH_20_40_80 = "20/40/80";

    /** Gateway wifi 2.4ghz Page Title */
    public static final String PAGE_TITLE_WIFI_24EDIT_PAGE = "Gateway > Connection > Wireless > Edit 2.4 GHz - <REPLACE>";

    /** Gateway wifi 5ghz wifi edit Page Title */
    public static final String PAGE_TITLE_WIFI_5EDIT_PAGE = "Gateway > Connection > Wireless > Edit 5 GHz - <REPLACE>";

    /** UI Connection Wireless network Link Text */
    public static final String LINK_TEXT_CONNECTION_WIFI = "Wi-Fi";

    /** variable to store wifi page url */
    public static String STRING_WIFI_PAGE_URL = "/wireless_network_configuration.php";

    /** variable to store wifi edit 2.4 GHz page title */
    public static String UI_WIFI_2_4_GHZ_EDIT_PAGE_TITLE = "lan.gui.page.title.wifi.2.4.GHZ_EDIT";

    /** variable to store wifi edit 5 GHz page title */
    public static String UI_WIFI_5_GHZ_EDIT_PAGE_TITLE = "lan.gui.page.title.wifi.5.GHZ_EDIT";

    /** Variable to store the input type password attribute */
    public static String INPUT_TYPE_PASSWORD = "password";

    /**
     * Variable to store SSH command to get the admin page default hash password for business class device
     */
    public static final String SSH_GET_DEFAULT_HASH_PASSWORD_BUSINESS_CLASS = "syscfg get hash_password_2";

    /**
     * Variable to store SSH command to get the admin page default hash password
     */
    public static final String SSH_GET_DEFAULT_HASH_PASSWORD = "syscfg get hash_password_3";

    /** Link text for Parental Control */
    public static final String LINK_TEXT_PARENTAL_CONTROL = "Parental Control";

    /** Attribute aria checked */
    public static final String ARIA_CHECKED_ATTRIBUTE = "aria-checked";

    /** variable to store string google */
    public static final String STRING_GOOGLE = "google";

    /** String to store user defined service */
    public static final String STRING_USER_DEFINED_SERVICE = "user_defined_service";

    /** UI Link text for Managed Services */
    public static final String LINK_TEXT_MANAGED_SERVICES = "Managed Services";

    /** Connection Type Wifi in web ui */
    public static final String CONNECTION_TYPE_WIFI = "Wi-Fi";

    /** Connection Type Ethernet in web ui */
    public static final String CONNECTION_TYPE_ETHERNET = "Ethernet";

    /** Variable to store DSL DEVICE page URL */
    public static final String DSL_PAGE_URL = "dsl.page.url";

    /** Variable to store webui home page page heading */
    public static String STRING_WEB_GUI_HOME_PAGE_HEADING = "Gateway > At a Glance";

    public static final String LINK_TEXT_FIREWALL = "Firewall";

    /** Link text for IPv4 page in FIREWALL Menu */
    public static final String LINK_TEXT_IPV4 = "IPv4";

    /** Link text for IPv4 page in FIREWALL Menu */
    public static final String LINK_TEXT_IPV6 = "IPv6";

    /** Link text for Content Filtering */
    public static final String LINK_TEXT_CONTENT_FILTERING = "Content Filtering";

    /** Link text for Managed Devices */
    public static final String LINK_TEXT_MANAGED_DEVICES = "Managed Devices";

    /** String text for firewall high */
    public static final String FIREWALL_MAXIMUM_TEXT = "High Security";

    /** String text for firewall low */
    public static final String FIREWALL_MINIMUM_TEXT = "Low Security";

    /** LOCAL IP NETWORK name in Connection tab */
    public static String STRING_NAME_LOCAL_IP_NETWORK = "Local IP Network";

    /** 2.4 GHZ Network Password Save Xpath */
    public static final String XPATH_SAVE_2_4_GHZ_NETWORK_PASSWORD = "//*[@id=\"save_settings\"]";

    /** Enum to get the WiFi access band */
    public enum RestoreGateWay {

	RESTART_GATEWAY("RESET"),

	RESTART_ONLY_WIFI_MODULE("RESET Wi-Fi MODULE"),

	RESTART_ONLY_WIFI_ROUTER("RESET Wi-Fi ROUTER"),

	RESTORE_WIFI_SETTINGS("RESTORE Wi-Fi SETTINGS"),

	RESET_PASSWORD("RESET PASSWORD"),

	RESTORE_FACTORY_SETTINGS("RESTORE FACTORY SETTINGS"),

	RESTORE_FACTORY_SETTINGS_ITALIAN("REIMPOSTA IMPOSTAZIONI DI FABBRICA"),

	RESET_MTA_MODULE("RESET MTA MODULE");

	private String resetType;

	private RestoreGateWay(String resetType) {
	    this.resetType = resetType;
	}

	public String getValue() {
	    return resetType;
	}
    }

    /** variable to store the xpath for pop ups */
    public static String XPATH_FOR_POP_UPS = "/html/body/div[@id='__alertLiveCont']/div[@id='popup_container']/h2[@id='popup_title']";

    /** variable to store the validation text for "Operation In Progress" */
    public static String VALIDATION_TEXT_FOR_OPERATION_IN_PROGRESS = "Operation In Progress";

    /** variable to store the xpath for pop ups for Italian alert */
    public static String XPATH_FOR_POP_UPS_ITALIAN = "//*[@id=\"popUp_container\"]/div/div[1]";

    /** variable to store the validation text for "Operation in Progress" */
    public static String VALIDATION_TEXT_FOR_OPERATION_IN_PROGRESS_MODAL = "Operation in Progress";

    /** Web element ID to store factory reset pop up */
    public static String WEBELEMENT_ID_FACTORY_RESET_ALERT_MESSAGE = "/html/body/div[@id='__alertLiveCont']/div[@id='popup_container']/h2[@id='popup_title']";

    /** variable to store the xpath for pop ups */
    public static String XPATH_FOR_POP_UP_MODAL = "//*[@id='popUp_container']/div/div[1]/h2";

    /** Web element id for ipv4 connectivity check */
    public static final String WEB_ELEMENT_ID_IPV4_CONNECTIVITY_CHECK = "check_ipv4";

    /** Web element id to get ipv4 connectivity status */
    public static final String WEB_ELEMENT_ID_GET_IPV4_CONNECTIVITY_STATUS = "connectivity_ipv4";

    /** Pattern for Public Wi-Fi Network block */
    public static final String PATTERN_PUBLIC_WIFI_NETWORK = "Public Wi-Fi Network";

    /** Constant to hold up and down stream header lock status */
    public static final String UP_AND_DOWN_STREAM_PARAM_LOCK_STATUS = "Lock Status";

    /** Constant to hold up and down stream header Frequency */
    public static final String UP_AND_DOWN_STREAM_PARAM_FREQUENCY = "Frequency";

    /** Constant to hold up and down stream header SNR */
    public static final String UP_AND_DOWN_STREAM_PARAM_SNR = "SNR";

    /** Constant to hold up and down stream header Power Level */
    public static final String UP_AND_DOWN_STREAM_PARAM_POWER_LEVEL = "Power Level";

    /** Constant to hold up and down stream header Modulation */
    public static final String UP_AND_DOWN_STREAM_PARAM_MODULATION = "Modulation";

    /** Constant to hold up and down stream header Symbol Rate */
    public static final String UP_AND_DOWN_STREAM_PARAM_SYMBOL_RATE = "Symbol Rate";

    /** Constant to hold up and down stream header Channel Type */
    public static final String UP_AND_DOWN_STREAM_PARAM_CHANNEL_TYPE = "Channel Type";

    /** Variable to store the xpath for Prefer private Wifi */
    public static String XPATH_FOR_PREFER_PRIVATE = "//*[@id='prefer_private']";

    /** Property to get command to trigger firmware download for devices **/
    public static final String SH_COMMAND_TO_TRIGGER_FIRMWARE_DOWNLOAD_DSL = "sh.command.totrigger. fwdownload_dsl";

//    /** port forwarding mode */
//    /**
//     * Xpath element to get the aria checked status of port forwarding mode enable button
//     */
//    public static String XPATH_FOR_PORT_FORWARDING_MODE_ENABLE_LINK = "//*[@id='forwarding-switch']/a[1]";
//
//    /**
//     * Xpath element to get the aria checked status of port forwarding mode disable button
//     */
//    public static String XPATH_FOR_PORT_FORWARDING_MODE_DISABLE_LINK = "//*[@id='forwarding-switch']/a[2]";
    
    /** port forwarding mode */
    /**
     * Xpath element to get the aria checked status of port forwarding mode enable button
     */
    public static String XPATH_FOR_PORT_FORWARDING_MODE_ENABLE_LINK = "//*[@id='forwarding-switch']/a[1]/li/label";

    /**
     * Xpath element to get the aria checked status of port forwarding mode disable button
     */
    public static String XPATH_FOR_PORT_FORWARDING_MODE_DISABLE_LINK = "//*[@id='forwarding-switch']/a[2]/li/label";
    
    /** Xpath element to get the area checked status of Https mode enable button */
    public static String XPATH_FOR_HTTPS_MODE_ENABLE_LINK = "//*[@id='https-switch']/a[1]";

    /** Xpath element to get the area checked status of Https mode disable button */
    public static String XPATH_FOR_HTTPS_MODE_DISABLE_LINK = "//*[@id='https-switch']/a[2]";
    
    /** Variable to store the xpath for DHCP IPV4 Beginning Address */
    public static String XPATH_FOR_DHCP_IPV4_BEGINNING_ADDRESS = "//*[@id='ipv4_dhcp_beginning_address_4']";
    
    /** Variable to store the xpath for DHCP IPV4 Ending Address */
    public static String XPATH_FOR_DHCP_IPV4_ENDING_ADDRESS = "//*[@id='ipv4_dhcp_ending_address_4']";
    
    /** Variable to store the xpath for DHCP IPV4 Save Button */
    public static String XPATH_FOR_DHCP_IPV4_SAVE_BUTTON = "//*[@id='submit_ipv4']";
    
    /** Variable to store the Connected devices edit page Header title */
    public static final String HEADER_TITLE_CONNECTED_DEVICES_EDIT_PAGE = "Connected Devices > Devices > Edit Device";
    
    /** Variable to store trouble shooting page link text */
    public static String ELEMENT_ID_TROUBLE_SHOOTING_PAGE = "Troubleshooting";
    
    /**
     * Variable to store the web element ID for trouble shooting page heading
     */
    public static String ELEMENT_ID_TROUBLE_SHOOTING_PAGE_HEADING = "//*[@id=\"content\"]/h1";
    
    /** Variable to store gateway reset page link text */
    public static String ELEMENT_ID_RESET_GATEWAY_IN_TROUBLESHOOTING_PAGE = "Reset/Restore Gateway";    

    /** Variable to store the web element ID for gateway reset page heading */
    public static String ELEMENT_ID_GATEWAY_RESET_PAGE_HEADING = "//*[@id=\"content\"]/h1";
    
    /** Variable to store trouble shooting page heading */
    public static String STRING_TROUBLE_SHOOTING_PAGE_HEADING = "Troubleshooting > Logs";
    
    /** Variable to store gateway reset page heading */
    public static String STRING_GATEWAY_RESET_PAGE_HEADING = "Troubleshooting > Reset / Restore Gateway";    
 
    /** Variable to store reset/restore page title */
    public static String RESET_RESTORE_GATEWAY_PAGE_TITLE_1 = "Troubleshooting > Reset / Restore Gateway -";
    
    /** String to hold the confirmation message on factory restore */
    public static final String ARE_YOU_SURE_FACTORY_RESTORE = "Are you sure you want to Restore Factory settings?";
    
    /** Variable to store the web element id for factory reset */
    public static final String ELEMENT_ID_FACTORY_RESET_BUTTON = "btn5";
    
    /** xpath for popup message obtained on clicking enable/disable buttons */
    public static String STRING_XPATH_FOR_POPUP_MESSAGE = "//*[@id=\"popup_message\"]";
    
    /** Variable to store the web element id for ok button for pop up */
    public static final String ELEMENT_ID_OK_BUTTON_POP_UP_MESSAGE = "popup_ok";
    
    /** Variable to store open square bracket symbol */
    public static String OPEN_SQUARE_BRACKET = "[";
    
    /** Variable to store the closed square bracket symbol */
    public static String CLOSE_SQAURE_BRACKET = "]";
    
    
    /** Dynamic Xpath element to get edit button */
    public static final String DYNAMIC_XPATH_FOR_EDIT_BTN = "//*[@id='btn-{i}']";

    /** Dynamic Xpath element to get Radio button of Reserver IP */
    public static final String DYNAMIC_XPATH_RESERVEDIP_RADIO = "//*[@id='ip_static-{i}']";

    /** Dynamic Xpath element to Reserver IP Address */
    public static final String DYNAMIC_XPATH_RESERVEDIP_ADDRESS = "//*[@id='staticIPAddress-{i}']";
}

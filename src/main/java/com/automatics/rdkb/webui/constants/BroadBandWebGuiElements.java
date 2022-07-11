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

import java.util.ArrayList;
import java.util.List;

public class BroadBandWebGuiElements {
    /**
     * Variable to store the web element of start up button in captive portal
     */
    public static final String ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_START_UP_BUTTON = "//*[@id=\"get_set_up\"]";

    /** Variable to store the web element of Header in captive portal Page */
    public static final String ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_PAGE_HEADER = "//*[@id=\"personalize\"]/h1";

    /** Variable to store xpath of 5GHz Wifi Edit button in WiFi page */
    public static final String ELEMENT_XPATH_WIFI_5GHZ_EDIT = "//a[@href='wireless_network_configuration_edit.php?id=2']";

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

    /** xpath for troubleshooting old password text box */
    public static final String XPATH_TROUBLESHOOTING_CHANGE_CREDENTIALS_CURRENT_PASSWORD_TEXT_BOX = "//*[@id=\"oldPassword\"]";

    /** xpath for troubleshooting new password text box */
    public static final String XPATH_TROUBLESHOOTING_CHANGE_CREDENTIALS_NEW_PASSWORD_TEXT_BOX = "//*[@id=\"userPassword\"]";

    /** xpath for troubleshooting re-enter new password text box */
    public static final String XPATH_TROUBLESHOOTING_CHANGE_CREDENTIALS_RE_ENTER_NEW_PASSWORD_TEXT_BOX = "//*[@id=\"verifyPassword\"]";

    /** xpath for troubleshooting change password save button */
    public static final String XPATH_TROUBLESHOOTING_CHANGE_CREDENTIALS_SAVE_BUTTON = "//*[@id=\"submit_pwd\"]";

    /**
     * Xpath for retype password error message in Gateway > Wizard Screen from connected client
     */
    public static final String XPATH_FOR_RETYPE_PASSWORD_ERROR_UI_PORTAL = "//*[@id=\"pageForm\"]/div[3]/p";

    /**
     * Xpath for current password error message in Gateway > Wizard Screen from connected client
     */
    public static final String XPATH_FOR_CURRENT_PASSWORD_ERROR_UI_PORTAL = "//*[@id=\"pageForm\"]/div[1]/p";

    /**
     * Xpath for new password error message in Gateway > Wizard Screen from connected client
     */
    public static final String XPATH_FOR_NEW_PASSWORD_ERROR_UI_PORTAL = "//*[@id=\"pageForm\"]/div[2]/p";

    /** Variable to store xpath of save current configurations button */
    public static final String ELEMENT_XPATH_SAVE_CURRENT_CONFIGURATIONS = "//*[@value='Save Current Configuration']";

    /** Variable to store xpath of restore saved configurations button */
    public static final String ELEMENT_XPATH_RESTORE_SAVED_CONFIGURATIONS = "//*[@value='Restore Saved Configuration']";

    /**
     * Variable to store the web element Xpath Online Devices host name in Connected Devices pages
     */
    public static final String ELEMENT_XPATH_ONLINE_DEVICES_HOST_NAME = "//*[@id=\"online-private\"]/table/tbody/tr[##ROW##]/td[1]/a/u";

    /**
     * Variable to store the web element Xpath Online Devices connection type in Connected Devices pages
     */
    public static final String ELEMENT_XPATH_ONLINE_DEVICES_CONNECTION_TYPE = "//*[@id=\"online-private\"]/table/tbody/tr[##ROW##]/td[4]";

    /**
     * Variable to store the web element Xpath Online Devices info in Connected Devices pages
     */
    public static final String ELEMENT_XPATH_ONLINE_DEVICES_INFO = "//*[@id=\"online-private\"]/table/tbody/tr[##ROW##]/td[1]/div/dl";

    /** Channel bandwidth 20Mhz text in wifi edit page of lan side gui */
    public static final String ELEMENT_XPATH_CHANNEL_BANDWIDTH_20MHZ = "//*[@id='bandwidth_switch']/b[1]";
    /** Channel bandwidth 40Mhz text in wifi edit page of lan side gui */
    public static final String ELEMENT_XPATH_CHANNEL_BANDWIDTH_40MHZ = "//*[@id='bandwidth_switch']/b[2]";
    /** Channel bandwidth 80Mhz text in wifi edit page of lan side gui */
    public static final String ELEMENT_XPATH_CHANNEL_BANDWIDTH_80MHZ = "//*[@id='bandwidth_switch']/b[3]";
    /**
     * Variable to store the web element Xpath RSSI level value
     */
    public static final String ELEMENT_XPATH_RSSI_LEVEL = "//*[@id=\"online-private\"]/table/tbody/tr[##ROW##]/td[3]";

    /** String variable to store user name */
    public static String ADMINUI_LOGIN_PAGE_USER_NAME = "username";
    /** String variable to store password */
    public static String ADMINUI_LOGIN_PAGE_PASSWORD = "password";

    /** Channel bandwidth 20Mhz id in wifi page of Lan side gui */
    public static final String ELEMENT_ID_CHANNEL_20MHZ = "channel_bandwidth20";

    /** Channel bandwidth 40Mhz id in wifi page of Lan side gui */
    public static final String ELEMENT_ID_CHANNEL_40MHZ = "channel_bandwidth";

    /** Channel bandwidth 40Mhz id in wifi page 5Ghz of Lan side gui */
    public static final String ELEMENT_ID_CHANNEL_40MHZ_5Ghz = "channel_bandwidth1";

    /** Element id to get SSID Name text box driver in Wifi edit page */
    public static String ELEMENT_ID_WIFI_EDIT_PAGE_SSID_NAME = "network_name";

    /**
     * Variable to store the web element id for show password element in WiFi edit page
     */
    public static String ELEMENT_ID_SHOW_PWD_CHECKBOX = "password_show";

    /**
     * Element id to get SSID Password entry text box driver in Wifi edit page
     */
    public static String ELEMENT_ID_WIFI_EDIT_PAGE_SSID_PASSWORD = "network_password";
    /** Element id to get SSID save settings button in Wifi edit page */
    public static String ELEMENT_ID_WIFI_EDIT_PAGE_SAVE_SETTING = "save_settings";

    /** Element id to select security mode options in Wifi Edit Page */
    public static final String ELEMENT_ID_SECURITY_MODE = "security";

    /** Link Text for 'more' in Gui pages */
    public static final String LINK_TEXT_FOR_SUMMARY_MORE = "more";

    /** xPATH to get OPEN security option in Security selection pop up */
    public static final String ELEMENT_ID_OPEN_SECURITY_OPTION = "path5";

    /** Variable to store the web element id for ok button for pop ok */
    public static final String ELEMENT_ID_OK_BUTTON_POP_OK_MESSAGE = "pop_ok";

    /** id for cancel button in popup message */
    public static final String ELEMENT_ID_FOR_CANCEL_IN_POPUP_MESSAGE = "popup_cancel";
	
    /**
     * Variable to store the web element of Enable in Managed sites in Parental Control
     */
    public static final String ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SITES_ENABLE = "//*[@id=\"managed-sites-switch\"]/a[1]/li/label";
 
    /** Constant to hold Label tag in Enable Disable Xpath for Port Forwarding Page */
    public static final String LABLE_TAG_IN_XPATH = "/li/label";
    
    /**
     * Variable to store the web element of Add Sites in Managed Site in Parental Control
     */
    public static final String ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SITES_ADD_SITES = "//*[@id=\"add_blocked_site\"]";
    
    /** Variable to store the web element of Add Sites in Managed Site in Parental Control */
    public static final String XPATH_PARENTAL_CONTROL_MANAGED_SITES_ADD_SITES_TEXT_BOX = "//*[@id=\"url\"]";
    
    /**
     * Variable to store the web element of Save button in Add Blocked sites page in Parental Control
     */
    public static final String ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SITES_SAVE_BLOCKED_SITES = "//*[@id=\"pageForm\"]/div/div/div[4]/input[1]";

    /** Variable to store the web element of Add Keyword in Managed Site in Parental Control */
    public static final String XPATH_PARENTAL_CONTROL_MANAGED_SITES_ADD_KEYWORD_TEXT_BOX = "//*[@id=\"keyword\"]";
    
    /**
     * Variable to store the web element of User Defined Service text box in Managed Service add page in Parental
     * Control
     */
    public static final String ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SERVICE_USER_DEFINED_SERVICE_TEXT_BOX = "//*[@id=\"user_defined_service\"]";

    /**
     * Variable to store the web element of Start Port text box in Managed Service add page in Parental Control
     */
    public static final String ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SERVICE_START_PORT_TEXT_BOX = "//*[@id=\"start_port\"]";
    
    /**
     * Variable to store the web element of End Port text box in Managed Service add page in Parental Control
     */
    public static final String ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SERVICE_END_PORT_TEXT_BOX = "//*[@id=\"end_port\"]";
    
    /**
     * Variable to store the web element of Save button in Add Blocked Service page in Parental Control
     */
    public static final String ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SERVICES_SAVE_BLOCKED_SERVICES = "//*[@id=\"btn-save\"]";

    /**
     * Variable to store the web element of Computer Name text box in Managed Device add page in Parental Control
     */
    public static final String ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SERVICE_COMPUTER_NAME_TEXT_BOX = "//*[@id=\"custom_name\"]";

    /**
     * Variable to store the web element of MAC Address text box in Managed Device add page in Parental Control
     */
    public static final String ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SERVICE_MAC_ADDRESS_TEXT_BOX = "//*[@id=\"custom_mac\"]";

    /**
     * Variable to store the web element of Save button in Add Blocked Device page in Parental Control
     */
    public static final String ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SITES_SAVE_BLOCKED_DEVICE = "//*[@id=\"btn-save\"]";
    
    /** Variable to store Xpath for Always Block No button in Parental Control */
    public static final String XPATH_PARENTAL_CONTROL_ALWAYS_BLOCK_NO_BUTTON = "//*[@id=\"always-switch\"]/a[1]/li/label";
    
    /** Variable to store Xpath for Start Time Hour Dropdown in Parental Control */
    public static final String XPATH_PARENTAL_CONTROL_START_TIME_HOUR = "//*[@id=\"time_start_hour\"]";

    /** Variable to store Xpath for Start Time Minute Dropdown in Parental Control */
    public static final String XPATH_PARENTAL_CONTROL_START_TIME_MINUTE = "//*[@id=\"time_start_minute\"]";

    /** Variable to store Xpath for Start Time AM/PM Dropdown in Parental Control */
    public static final String XPATH_PARENTAL_CONTROL_START_TIME_AM_PM = "//*[@id=\"time_start_ampm\"]";
    
    /** Variable to store Xpath for End Time Hour Dropdown in Parental Control */
    public static final String XPATH_PARENTAL_CONTROL_END_TIME_HOUR = "//*[@id=\"time_end_hour\"]";

    /** Variable to store Xpath for End Time Minute Dropdown in Parental Control */
    public static final String XPATH_PARENTAL_CONTROL_END_TIME_MINUTE = "//*[@id=\"time_end_minute\"]";

    /** Variable to store Xpath for End Time AM/PM Dropdown in Parental Control */
    public static final String XPATH_PARENTAL_CONTROL_END_TIME_AM_PM = "//*[@id=\"time_end_ampm\"]";
    
	/**
	 * Variable to store the web element of Pop up title in report generation in
	 * Parental Control
	 */
	public static final String ELEMENT_XPATH_PARENTAL_CONTROL_REPORT_GENERATION_POP_UP_TITLE = "//*[@id=\"popup_title\"]";
	
	/**
	 * Variable to store the web element of Pop up OK button in report generation in
	 * Parental Control
	 **/
	public static final String ELEMENT_XPATH_PARENTAL_CONTROL_REPORT_GENERATION_POP_UP_OK = "//*[@id=\"popup_ok\"]";
	
    /** Web element id to cancel port forward configuration */
    public static final String ELEMENT_ID_CANCEL_PORT_FORWARDING_CONFIGURATION = "btn-cancel";
    
    /**
     * Variable to store the web element of Add Key Word in Managed sites in Parental Control
     */
    public static final String ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SITES_ADD_KEY_WORD = "//*[@id=\"add-blocked-keywords\"]";

    /**
     * Variable to store the web element of Disable in Managed sites in Parental Control
     */
    public static final String ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SITES_DISABLE = "//*[@id=\"managed-sites-switch\"]/a[2]/li/label";

    /**
     * Variable to store the web element of Enable in Managed services in Parental Control
     */
    public static final String ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SERVICES_ENABLE = "//*[@id=\"managed-services-switch\"]/a[1]/li/label";

    /**
     * Variable to store the web element of Add Services in Managed Service page in Parental Control
     */
    public static final String ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SERVICE_ADD_SERVICES = "//*[@id=\"add-blocked-services\"]";

    /**
     * Variable to store the web element of Disable in Managed Service in Parental Control
     */
    public static final String ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SERVICE_DISABLE = "//*[@id=\"managed-services-switch\"]/a[2]/li/label";

    /** xpath for link Add Device with Reserved IP in Admin GUI */
	public static final String XPATH_ADD_DEVICE_WITH_RESERVED_IP = "//*[@id=\"online-private\"]/div/a";
	
    /** xpath for text box of host name in Reserved IP GUI */
    public static final String XPATH_RESERVED_IP_HOST_NAME = "//*[@id=\"host_name\"]";

    /** xpath for text box of host name in Reserved IP GUI */
    public static final String XPATH_RESERVED_IP_MAC_ADDRESS = "//*[@id=\"mac_address\"]";
    
    /** xpath for text box for Reserved IP in Admin GUI */
    public static final String XPATH_RESERVED_IP_TEXT_BOX = "//*[@id=\"staticIPAddress\"]";
    
    /** xpath for Save Button in Add Device with Reserved IP page of Admin GUI */
    public static final String XPATH_ADD_DEVICE_WITH_RESERVED_IP_SAVE_BUTTON = "//*[@id=\"saveBtn\"]";
    
    /** Element ID for Stateful check box */
	public static final String ID_CHECKBOX_STATEFUL = "Stateful";

	/** Element Xpath for Stateless check box */
	public static final String XPATH_CHECKBOX_STATELESS = "//*[@id=\"Stateless\"]";
	
	/**
	 * Variable to store the xpath for Delegated prefix (IPv6)-Business class device
	 */
	public static String XPATH_FOR_DELEGATED_PREFIX_IPV6_BUSINESS = "//*[@id=\"content\"]/div[2]/div[9]/span[2]";

	/** Variable to store the xpath for Delegated prefix (IPv6) */
	public static String XPATH_FOR_DELEGATED_PREFIX_IPV6 = "//*[@id=\"content\"]/div[2]/div[8]/span[2]";
	
	/** Xpath for DHCPv6 Lease Time Element */
	public static final String XPATH_DHCP_LEASE_TIME = "//*[text()='DHCPv6 Lease Time:']";
	
	/**
	 * Variable to store the web element Id for DHCPv6 Ending Address from the Local
	 * IP Network Page
	 */
	public static final String ELEMENT_ID_DEA = "DEA_";
	
	/**
	 * Variable to store the web element Id for DHCPv6 Beginning Address from the
	 * Local IP Network Page
	 */
	public static final String ELEMENT_ID_DBA = "DBA_";

	/** Id of the Submit button in local ip page IPv6 */
	public static final String ELEMENT_ID_SUBMIT_BTN_IPV6 = "submit_ipv6";
	
	/** xpath for cancel button in Ethwan popup message */
	public static final String XPATH_FOR_CANCEL_IN_ETHWAN_POPUP_MESSAGE = "//*[@id=\"popup_cancel\"]";
	
	/**
	 * List of Possible Xpaths for firewall modes of IPv4
	 */
	public static final List<String> XPATH_FOR_FIREWALL_SETTINGS_IPV4 = new ArrayList<String>() {
		{
			add("//*[@id=\"firewall_level_minimum\"]");
			add("//*[@id=\"firewall_level_maximum\"]");
			add("//*[@id=\"firewall_level_typical\"]");
			add("//*[@id=\"firewall_level_custom\"]");
		}
	};

	/**
	 * List of Possible Xpaths for firewall modes of IPv6
	 */
	public static final List<String> XPATH_FOR_FIREWALL_SETTINGS_IPV6 = new ArrayList<String>() {
		{
			add("//*[@id=\"firewall_level_default\"]");
			add("//*[@id=\"firewall_level_custom\"]");
		}
	};
	
	/** xpath to get the status of Managed Devices Disabled radio button */
	public static final String XPATH_MANAGED_DEVICES_STATUS_DISABLED_RADIO_BUTTON = "//*[@id=\"managed_devices_disabled\"]";

	/** xpath to get the status of Managed Devices Enabled radio button */
	public static final String XPATH_MANAGED_DEVICES_STATUS_ENABLED_RADIO_BUTTON = "//*[@id=\"managed_devices_enabled\"]";




}

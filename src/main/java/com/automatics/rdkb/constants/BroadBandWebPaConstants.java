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
package com.automatics.rdkb.constants;

public class BroadBandWebPaConstants {

    /** WebPA Parameter to get Default SSID for 2.4 Ghz */
    public static final String WEBPA_DEFAULT_SSID_NAME_2_4_GHZ = "Device.WiFi.SSID.10001.X_COMCAST-COM_DefaultSSID";

    /** WebPA Parameter to get Default SSID for 5 Ghz */
    public static final String WEBPA_DEFAULT_SSID_NAME_5_GHZ = "Device.WiFi.SSID.10101.X_COMCAST-COM_DefaultSSID";

    /**
     * WebPA Parameter to get SSID of 5G Private Network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID = "Device.WiFi.SSID.10101.SSID";

    /** webpa parameter for private wifi passphrase in 2.4 Ghz */
    public static final String WEBPA_PARAM_FOR_WIFI_PRIVATE_SSID_2GHZ_PASSPHRASE = "Device.WiFi.AccessPoint.10001.Security.X_COMCAST-COM_KeyPassphrase";

    /** webpa parameter for private wifi passphrase in 5 Ghz */
    public static final String WEBPA_PARAM_FOR_WIFI_PRIVATE_SSID_5GHZ_PASSPHRASE = "Device.WiFi.AccessPoint.10101.Security.X_COMCAST-COM_KeyPassphrase";

    /** web pa parameter to get private wifi operating Bandwidth in 2.4Ghz Band */
    public static final String WEBPA_PARAM_FOR_OPERATING_BANDWIDTH_IN_2GHZ_BAND = "Device.WiFi.Radio.10000.OperatingChannelBandwidth";

    /** web pa parameter to get private wifi operating Bandwidth in 5Ghz Band */
    public static final String WEBPA_PARAM_FOR_OPERATING_BANDWIDTH_IN_5GHZ_BAND = "Device.WiFi.Radio.10100.OperatingChannelBandwidth";

    /** WebPA Parameter to get WiFi Radio.10000 Operating standard */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_10000_OPERATINGSTANDARDS = "Device.WiFi.Radio.10000.OperatingStandards";

    /** webpa parameter to change the Auto Change enable status in 5Ghz band */
    public static final String WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_5GHZ = "Device.WiFi.Radio.10100.AutoChannelEnable";

    /** WebPA Parameter to get WiFi Access.10001 point Beacon rate */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_10001_X_RDKCENTRAL_COM_BEACONRATE = "Device.WiFi.AccessPoint.10001.X_RDKCENTRAL-COM_BeaconRate";

    /**
     * WebPA parameter to enable/disbale Bridge Mode
     */
    public static final String WEBPA_PARAM_BRIDGE_MODE_ENABLE = "Device.X_CISCO_COM_DeviceControl.LanManagementEntry.1.LanMode";

    /** Webpa parameter to set cdl module url */
    public static final String WEBPA_PARAM_CDL_MODULE_URL = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.CDLDM.CDLModuleUrl";

    /** WebPA Parameter to configure Firewall Level for IPv6 Traffic */
    public static final String WEBPA_PARAM_FIREWALL_LEVEL_IPV6 = "Device.X_CISCO_COM_Security.Firewall.FirewallLevelV6";

    /** WebPA Parameter to block ICMP IPV4 Traffic */
    public static final String WEBPA_PARAM_TO_BLOCK_ICMP_FOR_IPV6_TRAFFIC_UNDER_CUSTOM_FIREWALL = "Device.X_CISCO_COM_Security.Firewall.FilterAnonymousInternetRequestsV6";

    /** Webpa parameter to get Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_Control.ActivatePartnerId */
    public static final String WEBPA_PARAM_ACTIVATE_PARTNER_ID = "Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_Control.ActivatePartnerId";

    /** webpa parameter for WiFi Region code */
    public static final String WEBPA_PARAM_WIFI_REGION_CODE = "Device.WiFi.X_RDKCENTRAL-COM_Syndication.WiFiRegion.Code";

    /** WebPA Parameter for band steering capability */
    public static final String WEBPA_PARAM_BAND_STEERING_CAPABILITY = "Device.WiFi.X_RDKCENTRAL-COM_BandSteering.Capability";

    /**
     * WebPa index for 2.4GHz Private SSID.
     */
    public static final String WEBPA_INDEX_2_4_GHZ_PRIVATE_SSID = "10001";

    /**
     * WebPa index for 5GHz Private SSID.
     */
    public static final String WEBPA_INDEX_5_GHZ_PRIVATE_SSID = "10101";

    /** webpa parameter for public wifi webpa index for 2.4 GHz */
    public static final String WEBPA_INDEX_2_4_GHZ_PUBLIC_WIFI = "10003";

    /** webpa parameter for public wifi webpa index for 5 GHz */
    public static final String WEBPA_INDEX_5_GHZ_PUBLIC_WIFI = "10103";

    /** WebPa index for 2.4 GHz Public SSID Access Point 1. */
    public static final String WEBPA_INDEX_2_4_GHZ_PUBLIC_SSID_AP1 = "10003";

    /** WebPa index for 2.4 GHz Public SSID Access Point 2. */
    public static final String WEBPA_INDEX_2_4_GHZ_PUBLIC_SSID_AP2 = "10005";

    /** WebPa index for 2.4 GHz Public SSID Access Point 1. */
    public static final String WEBPA_INDEX_5_GHZ_PUBLIC_SSID_AP1 = "10103";

    /** WebPa index for 2.4 GHz Public SSID Access Point 2. */
    public static final String WEBPA_INDEX_5_GHZ_PUBLIC_SSID_AP2 = "10105";

    /** WebPa index for 2.4 GHz Open LNF SSID Access Point 1. */
    public static final String WEBPA_INDEX_2_4_GHZ_OPEN_LNF_AP1 = "10004";

    /** WebPa index for 5 GHz Open LNF SSID Access Point 2. */
    public static final String WEBPA_INDEX_5_GHZ_OPEN_LNF_AP2 = "10104";

    /**
     * WebPA parameter to get the SSID name for 2.4 GHz Wi-Fi network.
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_NAME = "Device.WiFi.SSID.10001.SSID";

    /**
     * WebPA Parameter to get SSID of 2.4G Private Network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID = "Device.WiFi.SSID.10001.SSID";

    /**
     * WebPA parameter to get the SSID name for 5 GHz Wi-Fi network.
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_NAME = "Device.WiFi.SSID.10101.SSID";

    /**
     * WebPA parameter to get the SSID enabled status for 2.4 GHz Wi-Fi network.
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_ENABLED_STATUS = "Device.WiFi.SSID.10001.Enable";

    /**
     * WebPA parameter to get mesh back end url
     */
    public static final String WEBPA_PARAM_DEVICE_MESH_BACKHAULURL = "Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.Mesh.URL";

    /**
     * WebPA parameter to get mesh state
     */
    public static final String WEBPA_PARAM_DEVICE_MESH_STATE = "Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.Mesh.State";

    /**
     * WebPA parameter to get status of Meshwifi service
     */
    public static final String WEBPA_PARAM_DEVICE_MESH_STATUS = "Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.Mesh.Status";

    /**
     * WebPA parameter to get the enable status of mesh wifi service
     */
    public static final String WEBPA_PARAM_DEVICE_RDKCENTRAL_MESH_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.Mesh.Enable";

    /** WebPA Parameter for device serial number */
    public static final String WEBPA_PARAMETER_FOR_SERIAL_NUMBER = "Device.DeviceInfo.SerialNumber";

    /**
     * WebPa table for Device.WiFi.
     */
    public static final String WEBPA_TABLE_DEVICE_WIFI = "Device.WiFi";

    /** WebPa Parameter for code big support */
    public static final String WEBPA_PARAM_CODEBIG_SUPPORT = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.CodebigSupport";

    /** WebPa Parameter for uploading logs */
    public static final String WEBPA_PARAM_UPLOAD_LOGS_NOW = "Device.DeviceInfo.X_RDKCENTRAL-COM.Ops.UploadLogsNow";

    /**
     * WebPA parameter to get the bridge mode status.
     */
    public static final String WEBPA_PARAM_BRIDGE_MODE_STATUS = "Device.X_CISCO_COM_DeviceControl.LanManagementEntry.1.LanMode";

    /** WebPA Parameter for 2.4 Ghz Dynamic channel selection enable parameter */
    public static final String WEBPA_PARAM_ENABLE_DYNAMIC_CHANNEL_SELECTION_FOR_2GHZ = "Device.WiFi.Radio.10000.X_COMCAST-COM_DCSEnable";

    /** WebPA Parameter for 5 Ghz Dynamic channel selection enable parameter */
    public static final String WEBPA_PARAM_ENABLE_DYNAMIC_CHANNEL_SELECTION_FOR_5GHZ = "Device.WiFi.Radio.10100.X_COMCAST-COM_DCSEnable";

    /** WebPA Parameter for band steering enable */
    public static final String WEBPA_PARAM_BAND_STEERING_ENABLE = "Device.WiFi.X_RDKCENTRAL-COM_BandSteering.Enable";

    /** WebPa Parameter for getting device upTime */
    public static final String WEBPA_PARAM_DEVICE_UPTIME = "Device.DeviceInfo.UpTime";

    /**
     * WebPA parameter to get the SSID Advertisement enabled status for 2.4 GHz Wi-Fi network.
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_ADV_ENABLED = "Device.WiFi.AccessPoint.10001.SSIDAdvertisementEnabled";

    /**
     * WebPA parameter to get the SSID enabled status for 5 GHz Wi-Fi network.
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_ENABLED_STATUS = "Device.WiFi.SSID.10101.Enable";

    /**
     * WebPA parameter to get the SSID Advertisement enabled status for 5 GHz Wi-Fi network.
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_ADV_ENABLED = "Device.WiFi.AccessPoint.10101.SSIDAdvertisementEnabled";

    /**
     * WebPA Parameter to get/set Security mode Enabled by 2.4G Private network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_PRIVATE_SECURITY_MODEENABLED = "Device.WiFi.AccessPoint.10001.Security.ModeEnabled";

    /**
     * WebPA parameter to get the Captive portal enable status.
     */
    public static final String WEBPA_PARAM_CAPTIVE_PORTAL_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_CaptivePortalEnable";

    /**
     * WebPA Parameter to change/get Security mode in 5G Private network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_PRIVATE_SECURITY_MODEENABLED = "Device.WiFi.AccessPoint.10101.Security.ModeEnabled";

    /**
     * WebPA Parameter to change/get Security mode in 5G Public network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_PUBLIC_SECURITY_MODEENABLED = "Device.WiFi.AccessPoint.10103.Security.ModeEnabled";

    /** webpa parameter to get the Delegated prefix (IPv6) value */
    public static final String WEB_PARAM_DELEGATED_PREFIX_IPV6 = "Device.IP.Interface.1.IPv6Prefix.1.Prefix";

    /**
     * WebPA Parameter get channel radio for 10100
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_CHANNEL_IN_5GHZ = "Device.WiFi.Radio.10100.Channel";

    /** WebPA Parameter for Applying Settings to 5GHz Interface */
    public static final String WEBPA_PARAM_WIFI_5_APPLY_SETTING = "Device.WiFi.Radio.10100.X_CISCO_COM_ApplySetting";

    /**
     * WebPA Parameter for Numpingsperserver
     */
    public static final String NUM_PINGS_PER_SERVER = "Device.SelfHeal.ConnectivityTest.X_RDKCENTRAL-COM_NumPingsPerServer";

    /**
     * WebPA Parameter for MinNumPingServer
     */
    public static final String MIN_NUM_PINGS_SERVER = "Device.SelfHeal.ConnectivityTest.X_RDKCENTRAL-COM_MinNumPingServer";

    /**
     * WebPA Parameter for PingInterval
     * 
     */
    public static final String PING_INTERVAL = "Device.SelfHeal.ConnectivityTest.X_RDKCENTRAL-COM_PingInterval";

    /**
     * WebPA Parameter for PingRespWaitTime
     */
    public static final String PING_RESP_WAIT_TIME = "Device.SelfHeal.ConnectivityTest.X_RDKCENTRAL-COM_PingRespWaitTime";

    /**
     * WebPA Parameter for UsageComputeWindow
     */
    public static final String USAGE_COMPUTE_WINDOW = "Device.SelfHeal.ResourceMonitor.X_RDKCENTRAL-COM_UsageComputeWindow";

    /**
     * WebPA Parameter for MaxResetCount
     */
    public static final String MAX_RESET_COUNT = "Device.SelfHeal.X_RDKCENTRAL-COM_MaxResetCount";

    /**
     * WebPA Parameter for AvgCPUThreshold
     */
    public static final String AVG_CPU_THRESHOLD = "Device.SelfHeal.ResourceMonitor.X_RDKCENTRAL-COM_AvgCPUThreshold";

    /**
     * WebPA Parameter for avgMemoryThreshold
     */
    public static final String AVG_MEMORY_THRESHOLD = "Device.SelfHeal.ResourceMonitor.X_RDKCENTRAL-COM_AvgMemoryThreshold";

    /**
     * WebPA Parameter for MaxRebootCount
     */
    public static final String MAX_REBOOT_COUNT = "Device.SelfHeal.X_RDKCENTRAL-COM_MaxRebootCount";

    /** WebPA Parameter for Applying Settings to 2.4GHz Interface */
    public static final String WEBPA_PARAM_WIFI_2_4_APPLY_SETTING = "Device.WiFi.Radio.10000.X_CISCO_COM_ApplySetting";

    /**
     * WebPA parameter to get the Configure Wi-Fi status.
     */
    public static final String WEBPA_PARAM_DEVICE_INFO_RDK_CENTRAL_CONFIGURE_WIFI = "Device.DeviceInfo.X_RDKCENTRAL-COM_ConfigureWiFi";

    /** WebPA parameter representation for Device.WiFi.AccessPoint.10001.Security.KeyPassphrase */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2GHZ_SECURITY_KEYPASSPHRASE = "Device.WiFi.AccessPoint.10001.Security.KeyPassphrase";

    /** WebPA parameter representation for Device.WiFi.AccessPoint.10101.Security.KeyPassphrase */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5GHZ_SECURITY_KEYPASSPHRASE = "Device.WiFi.AccessPoint.10101.Security.KeyPassphrase";

    /** WebPa parameter for Device Finger Print Enable */
    public static final String WEBPA_PARAM_DEVICE_FINGER_PRINT_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_DeviceFingerPrint.Enable";

    /** WebPa parameter for Advanced Security safe browsing */
    public static final String WEBPA_PARAM_ADVANCED_SECURITY_SAFE_BROWSING = "Device.DeviceInfo.X_RDKCENTRAL-COM_AdvancedSecurity.SafeBrowsing.Enable";

    /** WebPa parameter for Advanced Security soft flowd */
    public static final String WEBPA_PARAM_ADVANCED_SECURITY_SOFTFLOWD = "Device.DeviceInfo.X_RDKCENTRAL-COM_AdvancedSecurity.Softflowd.Enable";

    /** WebPa parameter for rabid framework */
    public static final String WEBPA_PARAM_RABID_FRAME_WORK_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.RabidFramework.Enable";

    /** TR69 parameter to get the last reboot reason */
    public static final String WEBPA_PARAM_DEVICE_LAST_REBOOT_REASON = "Device.DeviceInfo.X_RDKCENTRAL-COM_LastRebootReason";

    /** WebPA parameter of ATM group */
    public static final String WEBPA_ATM_GROUP = "Device.WiFi.X_RDKCENTRAL-COM_ATM.APGroup.";

    /** WebPA Parameter for adding stations to ATM group */
    public static final String WEBPA_ATM_GROUP_ADD_STATIONS = ".Sta.";

    /** WebPA Parameter for assigning percentage to ATM group */
    public static final String WEBPA_ATM_GROUP_PERCENTAGE = ".AirTimePercent";

    /** webpa parameter to get all the possible channels for wifi in 5Ghz band */
    public static final String WEBPA_PARAM_FOR_POSSIBLECHANNELS_IN_5GHZ = "Device.WiFi.Radio.10100.PossibleChannels";

    /** WebPA Parameter for Firewall Level */
    public static final String WEBPA_PARAM_FIREWALL_LEVEL = "Device.X_CISCO_COM_Security.Firewall.FirewallLevel";
    
    /** WebPA Parameter for band steering PhyRateThreshold for 2.4GHZ radio */
    public static final String WEBPA_PARAM_BAND_STEERING_PHY_THRESHOLD_2_4GHZ = "Device.WiFi.X_RDKCENTRAL-COM_BandSteering.BandSetting.1.PhyRateThreshold";

    /** WebPA Parameter for band steering PhyRateThreshold for 5GHZ radio */
    public static final String WEBPA_PARAM_BAND_STEERING_PHY_THRESHOLD_5GHZ = "Device.WiFi.X_RDKCENTRAL-COM_BandSteering.BandSetting.2.PhyRateThreshold";

    /** WebPA Parameter for band steering idle inactive time for 2.4GHZ radio */
    public static final String WEBPA_PARAM_BAND_STEERING_IDLE_INACTIVE_TIME_2_4GHZ = "Device.WiFi.X_RDKCENTRAL-COM_BandSteering.BandSetting.1.IdleInactiveTime";

    /** WebPA Parameter for band steering idle inactive time for 5GHZ radio */
    public static final String WEBPA_PARAM_BAND_STEERING_IDLE_INACTIVE_TIME_5GHZ = "Device.WiFi.X_RDKCENTRAL-COM_BandSteering.BandSetting.2.IdleInactiveTime";

    /** WebPA Parameter for band steering overload inactive time for 2.4GHZ radio */
    public static final String WEBPA_PARAM_BAND_STEERING_OVERLOAD_INACTIVE_TIME_2_4GHZ = "Device.WiFi.X_RDKCENTRAL-COM_BandSteering.BandSetting.1.OverloadInactiveTime";

    /** WebPA Parameter for band steering overload inactive time for 5GHZ radio */
    public static final String WEBPA_PARAM_BAND_STEERING_OVERLOAD_INACTIVE_TIME_5GHZ = "Device.WiFi.X_RDKCENTRAL-COM_BandSteering.BandSetting.2.OverloadInactiveTime";

    /** WebPA Parameter for band steering enable */
    public static final String WEBPA_PARAM_BAND_STEERING_APGROUP = "Device.WiFi.X_RDKCENTRAL-COM_BandSteering.APGroup";

    public enum RdkBBandSteeringParameters {
	DEFAULT_STEERING_ENABLE(WEBPA_PARAM_BAND_STEERING_ENABLE, "false"),
	DEFAULT_STEERING_IDLE_INACTIVE_TIME_2GHZ(WEBPA_PARAM_BAND_STEERING_IDLE_INACTIVE_TIME_2_4GHZ, "10"),
	DEFAULT_STEERING_IDLE_INACTIVE_TIME_5GHZ(WEBPA_PARAM_BAND_STEERING_IDLE_INACTIVE_TIME_5GHZ, "10"),
	DEFAULT_STEERING_OVERLOAD_INACTIVE_TIME_2GHZ(WEBPA_PARAM_BAND_STEERING_OVERLOAD_INACTIVE_TIME_2_4GHZ, "10"),
	DEFAULT_STEERING_OVERLOAD_INACTIVE_TIME_5GHZ(WEBPA_PARAM_BAND_STEERING_OVERLOAD_INACTIVE_TIME_5GHZ, "10"),
	DEFAULT_STEERING_IDLE_INACTIVE_TIME_2GHZ_XB6(WEBPA_PARAM_BAND_STEERING_IDLE_INACTIVE_TIME_2_4GHZ, "0"),
	DEFAULT_STEERING_IDLE_INACTIVE_TIME_5GHZ_XB6(WEBPA_PARAM_BAND_STEERING_IDLE_INACTIVE_TIME_5GHZ, "0"),
	DEFAULT_STEERING_OVERLOAD_INACTIVE_TIME_2GHZ_XB6(WEBPA_PARAM_BAND_STEERING_OVERLOAD_INACTIVE_TIME_2_4GHZ, "0"),
	DEFAULT_STEERING_OVERLOAD_INACTIVE_TIME_5GHZ_XB6(WEBPA_PARAM_BAND_STEERING_OVERLOAD_INACTIVE_TIME_5GHZ, "0"),
	DEFAULT_STEERING_APGROUP(WEBPA_PARAM_BAND_STEERING_APGROUP, "1,2"),
	DEFAULT_STEERING_IDLE_INACTIVE_TIME_2GHZ_XB7(WEBPA_PARAM_BAND_STEERING_IDLE_INACTIVE_TIME_2_4GHZ, "0"),
	DEFAULT_STEERING_IDLE_INACTIVE_TIME_5GHZ_XB7(WEBPA_PARAM_BAND_STEERING_IDLE_INACTIVE_TIME_5GHZ, "0"),
	DEFAULT_STEERING_OVERLOAD_INACTIVE_TIME_2GHZ_XB7(WEBPA_PARAM_BAND_STEERING_OVERLOAD_INACTIVE_TIME_2_4GHZ, "0"),
	DEFAULT_STEERING_OVERLOAD_INACTIVE_TIME_5GHZ_XB7(WEBPA_PARAM_BAND_STEERING_OVERLOAD_INACTIVE_TIME_5GHZ, "0");

	String param;
	String defaultValue;

	RdkBBandSteeringParameters(String param, String defaultValue) {
	    this.param = param;
	    this.defaultValue = defaultValue;
	}

	/**
	 * @return the param
	 */
	public String getParam() {
	    return param;
	}

	/**
	 * @param param
	 *            the param to set
	 */
	public void setParam(String param) {
	    this.param = param;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
	    return defaultValue;
	}

	/**
	 * @param defaultValue
	 *            the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
	    this.defaultValue = defaultValue;
	}

	/**
	 * returns all band steering webpa names
	 * 
	 * @return
	 */
	public static String[] getAllBandSteeringWebPaConstantNames() {
	    RdkBBandSteeringParameters[] steeringParams = values();
	    String[] names = new String[steeringParams.length];

	    for (int i = 0; i < steeringParams.length; i++) {
		names[i] = steeringParams[i].getParam();
	    }

	    return names;
	}
    }

    public enum RdkBSsidParameters {
	SSID_FOR_2GHZ_PRIVATE_WIFI("Device.WiFi.SSID.10001.SSID", "2.4"),
	SSID_FOR_5GHZ_PRIVATE_WIFI("Device.WiFi.SSID.10101.SSID", "5");

	private String ssidParameterName;
	private String wifiBand;

	RdkBSsidParameters(String paramter, String band) {
	    this.ssidParameterName = paramter;
	    this.wifiBand = band;
	}

	public String getWifiBand() {
	    return wifiBand;
	}

	public void setWifiBand(String wifiBand) {
	    this.wifiBand = wifiBand;
	}

	public String getSsidParameterName() {
	    return ssidParameterName;
	}

	public void setSsidParameterName(String ssidParameterName) {
	    this.ssidParameterName = ssidParameterName;
	}

    }

    /** WebPA Parameter for Syndication Partner ID. */
    public static final String WEBPA_PARAM_FOR_SYNDICATION_PARTNER_ID = "Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.PartnerId";

    /**
     * WebPA Parameter to check MAC Address of 5G Private Network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_MAC_ADDRESS = "Device.WiFi.SSID.10101.MACAddress";

    /** WebPA parameter to get firewall fragmentation ip packets value */
    public static final String WEBPA_PARAM_FIREWALL_FRAGMENTATION_IPPKTS = "Device.Firewall.X_RDKCENTRAL-COM_Security.V{i}.BlockFragIPPkts";

    /** WebPA parameter to get firewall ip flood detect value */
    public static final String WEBPA_PARAM_FIREWALL_IPFLOOD_DETECT = "Device.Firewall.X_RDKCENTRAL-COM_Security.V{i}.IPFloodDetect";

    /** WebPA parameter to get firewall portscan protect value */
    public static final String WEBPA_PARAM_FIREWALL_PORTSCAN_PROTECT = "Device.Firewall.X_RDKCENTRAL-COM_Security.V{i}.PortScanProtect";

    /**
     * WebPA parameter to get the Wi-Fi needs personalization status.
     */
    public static final String WEBPA_PARAM_DEVICE_INFO_RDK_CENTRAL_WIFI_NEEDS_PERSONALIZATION = "Device.DeviceInfo.X_RDKCENTRAL-COM_WiFiNeedsPersonalization";

    /** web pa parameter to get the wifi enabled status in 2.4Ghz private wifi */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_2_4GHZ_PRIVATE_SSID = "Device.WiFi.SSID.10001.Status";

    /** web pa parameter to get the wifi enabled status in 5Ghz private wifi */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_5GHZ_PRIVATE_SSID = "Device.WiFi.SSID.10101.Status";

    /**
     * WebPA Parameter to change/get Security mode in 2.4G public network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_PUBLIC_SECURITY_MODEENABLED = "Device.WiFi.AccessPoint.10003.Security.ModeEnabled";

    /**
     * WebPA Parameter to change/get Security mode in 2G LNF network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_GHZ_LNF_SECURITY_MODEENABLED = "Device.WiFi.AccessPoint.10006.Security.ModeEnabled";

    /**
     * WebPA Parameter to change/get Security mode in 5G LNF network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_LNF_SECURITY_MODEENABLED = "Device.WiFi.AccessPoint.10106.Security.ModeEnabled";

    /** WebPA parameter to get 2.4GHz extender security mode */
    public static final String WEBPA_PARAM_DEVICE_WIFI_2_4GHZ_EXTENDER_SECURITY_MODE = "Device.WiFi.AccessPoint.10007.Security.ModeEnabled";

    /** WebPA parameter to get 5GHz extender SSID name */
    public static final String WEBPA_PARAM_DEVICE_WIFI_5GHZ_EXTENDER_SECURITY_MODE = "Device.WiFi.AccessPoint.10107.Security.ModeEnabled";

    /**
     * WebPA Parameter to get Passphrase of 2.4 GHz public Network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_10003_SECURITY_KEYPASSPHRASE = "Device.WiFi.AccessPoint.10003.Security.KeyPassphrase";

    /**
     * WebPA Parameter to get Passphrase of 5 GHz public Network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_10103_SECURITY_KEYPASSPHRASE = "Device.WiFi.AccessPoint.10103.Security.KeyPassphrase";

    /**
     * WebPA Parameter to get Passphrase of 2 GHz LNF Network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_10006_SECURITY_KEYPASSPHRASE = "Device.WiFi.AccessPoint.10006.Security.KeyPassphrase";

    /**
     * WebPA Parameter to get Passphrase of 5 GHz LNF Network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_10106_SECURITY_KEYPASSPHRASE = "Device.WiFi.AccessPoint.10106.Security.KeyPassphrase";

    /**
     * WebPA Parameter to get SSID of 5G Public Network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PUBLIC_SSID = "Device.WiFi.SSID.10103.SSID";

    /**
     * WebPA Parameter to get SSID of 2.4G public Network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PUBLIC_SSID = "Device.WiFi.SSID.10003.SSID";

    /** WebPA parameter to get 2.4GHz LNF SSID name */
    public static final String WEBPA_PARAM_DEVICE_WIFI_2_4GHZ_LNF_SSID = "Device.WiFi.SSID.10006.SSID";

    /** WebPA parameter to get BSSID of 2.4GHz LNF SSID */
    public static final String WEBPA_PARAM_DEVICE_WIFI_2_4GHZ_LNF_BSSID = "Device.WiFi.SSID.10006.BSSID";

    /** WebPA parameter to get 5GHz LNF SSID name */
    public static final String WEBPA_PARAM_DEVICE_WIFI_5_GHZ_LNF_SSID = "Device.WiFi.SSID.10106.SSID";

    /** webpa Parameter for 2.4GHz LNF PSK SSID name */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_10004_SSID = "Device.WiFi.SSID.10004.SSID";

    /** Webpa param for 5GHz LNF SSID name */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_10104_SSID = "Device.WiFi.SSID.10104.SSID";

    /** WebPA parameter to get 5GHz extender SSID name */
    public static final String WEBPA_PARAM_DEVICE_WIFI_5GHZ_EXTENDER_SSID = "Device.WiFi.SSID.10107.SSID";

    /** WebPA parameter to get 2.4GHz extender SSID name */
    public static final String WEBPA_PARAM_DEVICE_WIFI_2_4GHZ_EXTENDER_SSID = "Device.WiFi.SSID.10007.SSID";

    /**
     * WebPA Parameter to enable/disable SSID of 2.4G Public Network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PUBLIC_SSID_ENABLE_STATUS = "Device.WiFi.SSID.10003.Enable";

    /**
     * WebPA Parameter to enable SSID of 5G Public Network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PUBLIC_SSID_ENABLE_STATUS = "Device.WiFi.SSID.10103.Enable";

    /**
     * WebPA parameter to get the LNF SSID enabled status for 2.4 GHz Wi-Fi network.
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_LNF_SSID_ENABLED = "Device.WiFi.SSID.10006.Enable";

    /**
     * WebPA parameter to get the LNF SSID enabled status for 5 GHz Wi-Fi network.
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_5_GHZ_LNF_SSID_ENABLED = "Device.WiFi.SSID.10106.Enable";

    /** webpa parameter to validate wireless ssid enable status for Lost and Found 2.4GHz */
    public static final String WEBPA_WAREHOUSE_WIRELESS_SSID1_ENABLE_LNF_2_4 = "Device.WiFi.SSID.10004.Enable";

    /** webpa parameter to validate wireless ssid enable status for Lost and Found 2.4GHz */
    public static final String WEBPA_WAREHOUSE_WIRELESS_SSID2_ENABLE_LNF_2_4 = "Device.WiFi.SSID.10006.Enable";

    /** webpa parameter to validate wireless ssid enable status for Lost and Found 5GHz */
    public static final String WEBPA_WAREHOUSE_WIRELESS_SSID_ENABLE_LNF_5G = "Device.WiFi.SSID.10104.Enable";

    /** WebPA Parameter for LAN IP address */
    public static final String WEBPA_PARAMETER_FOR_LANIP_ADDRESS = "Device.X_CISCO_COM_DeviceControl.LanManagementEntry.1.LanIPAddress";

    /** WebPA Parameter for LAN subnet */
    public static final String WEBPA_PARAMETER_FOR_LAN_SUBNET = "Device.X_CISCO_COM_DeviceControl.LanManagementEntry.1.LanSubnetMask";

    /** WebPA Parameter for DHCPv4 server enable */
    public static final String WEBPA_PARAMETER_FOR_DHCP_SERVER = "Device.DHCPv4.Server.Enable";

    /** WebPA Parameter for DHCPv4 min address */
    public static final String WEBPA_PARAMETER_FOR_DHCP_MINADDRESS = "Device.DHCPv4.Server.Pool.1.MinAddress";

    /** WebPA Parameter for DHCPv4 max address */
    public static final String WEBPA_PARAMETER_FOR_DHCP_MAXADDRESS = "Device.DHCPv4.Server.Pool.1.MaxAddress";

    /** WebPA Parameter for IPv4 Ping Table URI */
    public static final String WEBPA_IPV4_PING_SERVER_URI = "X_RDKCENTRAL-COM_Ipv4PingServerURI";

    /** WebPA Parameter for Adding IPV4 Ping Table */
    public static final String WEBPA_IPV4_PING_ADD_TABLE = "Device.SelfHeal.ConnectivityTest.PingServerList.IPv4PingServerTable.";

    /** WebPA Parameter for IPv6 Ping Table URI */
    public static final String WEBPA_IPV6_PING_SERVER_URI = "X_RDKCENTRAL-COM_Ipv6PingServerURI";

    /** WebPA Parameter for Adding IPV6 Ping Table */
    public static final String WEBPA_IPV6_PING_ADD_TABLE = "Device.SelfHeal.ConnectivityTest.PingServerList.IPv6PingServerTable.";

    /** WebPA Parameter for WAN IPv6 */
    public static final String WEBPA_PARAM_WAN_IPV6 = "Device.DeviceInfo.X_COMCAST-COM_WAN_IPv6";

    /** WebPA Parameter to get WebPA Version */
    public static final String WEBPA_PARAM_WEBPA_VERSION = "Device.X_RDKCENTRAL-COM_Webpa.Version";

    /**
     * WebPA parameter to get the SSID enabled status for 2.4 GHz Wi-Fi network.
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_ENABLE = "Device.WiFi.SSID.10001.Enable";

    /**
     * WebPA parameter to get the SSID enabled status for 5 GHz Wi-Fi network.
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_ENABLED = "Device.WiFi.SSID.10101.Enable";
    
    /**
     * WebPA Parameter to change/get Security mode in 5G network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_SECURITY_MODEENABLED = "Device.WiFi.AccessPoint.10102.Security.ModeEnabled";

    /**
     * WebPA Parameter to change/get Security mode in 2.4G network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_SECURITY_MODEENABLED = "Device.WiFi.AccessPoint.10002.Security.ModeEnabled";

}

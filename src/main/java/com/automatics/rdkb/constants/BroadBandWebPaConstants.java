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

import java.util.HashMap;

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

    /** webpa Parameter for 2.4GHz guest SSID name */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_10002_SSID = "Device.WiFi.SSID.10002.SSID";

    /** Webpa param for 5GHz guest SSID name */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_10102_SSID = "Device.WiFi.SSID.10102.SSID";
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
     * WebPA parameter to get the SSID name for 5 GHz Wi-Fi network.
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PUBLIC_SSID_NAME = "Device.WiFi.SSID.10105.SSID";

    /**
     * WebPA parameter to get the SSID name for 5 GHz Wi-Fi network.
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PUBLIC_SSID_NAME = "Device.WiFi.SSID.10005.SSID";

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
     * WebPA parameter to get the SSID Advertisement enabled status for 5 GHz Wi-Fi network.
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PUBLIC_SSID_ADV_ENABLED = "Device.WiFi.AccessPoint.10105.SSIDAdvertisementEnabled";

    /**
     * WebPA parameter to get the SSID Advertisement enabled status for 5 GHz Wi-Fi network.
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PUBLIC_SSID_ADV_ENABLED = "Device.WiFi.AccessPoint.10005.SSIDAdvertisementEnabled";

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

    /** TR-069 Parameter for device software version. */
    public static final String TR69_PARAM_SOFTWARE_VERSION = "Device.DeviceInfo.SoftwareVersion";

    /** TR-069 Parameter for manufacturer. */
    public static final String TR69_PARAM_MANUFACTURER = "Device.DeviceInfo.Manufacturer";

    /** WebPA Parameter for device model name */
    public static final String WEBPA_PARAMETER_FOR_MODELNAME = "Device.DeviceInfo.ModelName";

    /** TR-069 Parameter for Device.DeviceInfo.X_CISCO_COM_BootloaderVersion. */
    public static final String TR69_PARAM_DEVICE_INFO_BOOT_LOADER_VERSION = "Device.DeviceInfo.X_CISCO_COM_BootloaderVersion";

    /** WebPA Parameter for device hardware version */
    public static final String WEBPA_PARAMETER_FOR_HARDWARE_VERSION = "Device.DeviceInfo.HardwareVersion";

    /** WebPA parameter for storing CM MAC value */
    public static final String WEBPA_PARAM_CM_MAC = "Device.X_CISCO_COM_CableModem.MACAddress";

    /** WebPA parameter for DEVICE Boot FILENAME */
    public static final String WEBPA_PARAM_DEVICE_BOOT_FILENAME = "Device.X_CISCO_COM_CableModem.IPv6BootFileName";

    /** WebPA parameter for DEVICE MTA MAC */
    public static final String WEBPA_PARAM_DEVICE_MTA_MAC = "Device.X_CISCO_COM_MTA.MACAddress";

    /** MTA to retrieve MTA IP from Device */
    public static final String WEBPA_COMMAND_MTA_IP_OF_DEVICE = "Device.X_CISCO_COM_MTA.IPAddress";

    /** WebPA parameter for SNMPv3 support */
    public static final String WEBPA_PARAM_SNMPV3_SUPPORT = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.SNMP.V3Support";

    /** WebPA parameter for Codebig first */
    public static final String WEBPA_PARAM_CODEBIG_FIRST_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.CodeBigFirst.Enable";

    /**
     * WebPA Parameter to get Firmware Download URL
     */
    public static final String WEBPA_PARAM_Device_DeviceInfo_X_RDKCENTRAL_COM_FirmwareDownloadURL = "Device.DeviceInfo.X_RDKCENTRAL-COM_FirmwareDownloadURL";

    /**
     * WebPA Parameter Firmware To Download
     */
    public static final String WEBPA_PARAM_Device_DeviceInfo_X_RDKCENTRAL_COM_FirmwareToDownload = "Device.DeviceInfo.X_RDKCENTRAL-COM_FirmwareToDownload";

    /**
     * WebPA Parameter to Check firmware download status
     */
    public static final String WEBPA_PARAM_Device_DeviceInfo_X_RDKCENTRAL_COM_FirmwareDownloadStatus = "Device.DeviceInfo.X_RDKCENTRAL-COM_FirmwareDownloadStatus";

    /**
     * WebPA parameter to get the CMTS MAC Address.
     */
    public static final String WEBPA_PARAM_DEVICE_INFO_RDK_CENTRAL_CMTS_MAC = "Device.DeviceInfo.X_RDKCENTRAL-COM_CMTS_MAC";

    /**
     * WebPA Parameter to enable SSID of 5G Private Network
     */
    public static final String WEBPA_PARAM_Device_WiFi_SSID_10101_Enable = "Device.WiFi.SSID.10101.Enable";

    /**
     * WebPA Parameter to get Firmware download protocol
     */
    public static final String WEBPA_PARAM_Device_DeviceInfo_X_RDKCENTRAL_COM_FirmwareDownloadProtocol = "Device.DeviceInfo.X_RDKCENTRAL-COM_FirmwareDownloadProtocol";

    /** WebPa Parameter for Maintenance window start time */
    public static final String WEBPA_COMMAND_MAINTENANCE_WINDOW_START_TIME = "Device.DeviceInfo.X_RDKCENTRAL-COM_MaintenanceWindow.FirmwareUpgradeStartTime";

    /** WebPa Parameter for Maintenance window end time */
    public static final String WEBPA_COMMAND_MAINTENANCE_WINDOW_END_TIME = "Device.DeviceInfo.X_RDKCENTRAL-COM_MaintenanceWindow.FirmwareUpgradeEndTime";

    /** WebPA Parameter to retrieve the RFC control for telemetry 2.0 */
    public static final String WEBPA_PARAM_RFC_CONTROL = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Control.RetrieveNow";

    /** Webpa parameter to trigger xconf cdl */
    public static final String WEBPA_PARAM_FOR_TRIGGERING_XCONF_CDL = "Device.X_COMCAST-COM_Xcalibur.Client.xconfCheckNow";

    /**
     * WebPa parameter for device reboot. The value should be of string type - "Device"
     */
    public static final String WEBPA_PARAM_DEVICE_CONTROL_DEVICE_REBOOT = "Device.X_CISCO_COM_DeviceControl.RebootDevice";

    /**
     * WebPA Parameter to get maxSubsytemResetCount for self heal
     */
    public static final String WEBPA_PARAM_MAX_SUB_SYSTEM_RESET_COUNT_FOR_SELF_HEAL = "Device.SelfHeal.X_RDKCENTRAL-COM_MaxResetCount";

    /** WebPA Parameter for selfheal ping interval */
    public static final String WEBPA_PARAMETER_FOR_SELFHEAL_PINGINTERVAL = "Device.SelfHeal.ConnectivityTest.X_RDKCENTRAL-COM_PingInterval";

    /** WebPa Parameter for getting device boot time */
    public static final String WEBPA_PARAM_DEVICE_BOOTTIME = "Device.DeviceInfo.X_RDKCENTRAL-COM_BootTime";

    /*** WebPA Parameter to Enabling Public wifi */
    public static final String WEBPA_PARAM_ENABLING_PUBLIC_WIFI = "Device.DeviceInfo.X_COMCAST_COM_xfinitywifiEnable";

    public static final String[] WEBPA_PARAM_LIST_QT_POSTCONDITION = {
	    BroadBandWebPaConstants.WEBPA_PARAM_CAPTIVE_PORTAL_ENABLE,
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_INFO_RDK_CENTRAL_CONFIGURE_WIFI,
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_INFO_RDK_CENTRAL_WIFI_NEEDS_PERSONALIZATION,
	    BroadBandWebPaConstants.WEBPA_PARAM_BRIDGE_MODE_STATUS,
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_NAME,
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_NAME,
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_ADV_ENABLED,
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_ADV_ENABLED,
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_ENABLED_STATUS,
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_ENABLED_STATUS,
	    BroadBandWebPaConstants.WEBPA_PARAM_ENABLING_PUBLIC_WIFI };

    /** WebPA Parameter for product class */
    public static final String WEBPA_PARAMETER_FOR_PRODUCTCLASS = "Device.DeviceInfo.ProductClass";

    /** WebPA Parameter for Device manufacturer */
    public static final String WEBPA_PARAMETER_FOR_MANUFACTURER_INFO = "Device.DeviceInfo.Manufacturer";

    /** WebPA Parameter for firmware name */
    public static final String WEBPA_PARAMETER_FOR_FIRMWARE_NAME = "Device.DeviceInfo.X_CISCO_COM_FirmwareName";

    /** WebPA Parameter for device CMC */
    public static final String WEBPA_PARAMETER_FOR_CMC = "Device.DeviceInfo.Webpa.X_COMCAST-COM_CMC";

    /** WebPA Parameter for device CID */
    public static final String WEBPA_PARAMETER_FOR_CID = "Device.DeviceInfo.Webpa.X_COMCAST-COM_CID";

    /** WebPA Parameter for DHCPv4 lease time */
    public static final String WEBPA_PARAMETER_FOR_DHCP_LEASETIME = "Device.DHCPv4.Server.Pool.1.LeaseTime";

    /** WebPA Parameter for DNS client server enable */
    public static final String WEBPA_PARAMETER_FOR_DNS_CLIENT_SERVER = "Device.DNS.Client.Server.1.Enable";

    /** WebPA Parameter for DNS client server type */
    public static final String WEBPA_PARAMETER_FOR_DNS_CLIENT_SERVER_TYPE = "Device.DNS.Client.Server.1.Type";

    /** WebPA Parameter for selfheal max reset count */
    public static final String WEBPA_PARAMETER_FOR_SELFHEAL_MAXRESET_COUNT = "Device.SelfHeal.X_RDKCENTRAL-COM_MaxResetCount";

    /** WebPA Parameter for Xfinity 2.4Ghz ssid */
    public static final String WEBPA_PARAMETER_FOR_PRIVATE_24GHZ_SSID = "Device.WiFi.SSID.10001.SSID";
    /** WebPA Parameter for Xfinity 5Ghz ssid */
    public static final String WEBPA_PARAMETER_FOR_PRIVATE_5GHZ_SSID = "Device.WiFi.SSID.10101.SSID";
    /** WebPA Parameter for xfinity 2.4ghz passkey */
    public static final String WEBPA_PARAMETER_FOR_PRIVATE_24GHZ_PASS = "Device.WiFi.AccessPoint.10001.Security.KeyPassphrase";
    /** WebPA Parameter for xfinity 5ghz passkey */
    public static final String WEBPA_PARAMETER_FOR_PRIVATE_5GHZ_PASS = "Device.WiFi.AccessPoint.10101.Security.KeyPassphrase";

    /** webpa parameter to change the Auto Change enable status in 2.4Ghz band */
    public static final String WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_2GHZ = "Device.WiFi.Radio.10000.AutoChannelEnable";

    /** web pa parameter to get guard interval in 2.4Ghz band private wifi */
    public static final String WEBPA_PARAM_FOR_GUARD_INTERVAL_IN_2GHZ = "Device.WiFi.Radio.10000.GuardInterval";

    /** WebPA Parameter for ENABLING radio setting for Wifi 2.4 Ghz */
    public static final String WEBPA_PARAM_WIFI_2_4_RADIO_ENABLE = "Device.WiFi.Radio.10000.Enable";

    /**
     * WebPA Parameter get channel radio for 10000
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_CHANNEL_IN_2GHZ = "Device.WiFi.Radio.10000.Channel";

    /** webpa parameter to get guard interval in 2.4Ghz band private wifi */
    public static final String WEBPA_PARAM_FOR_GUARD_INTERVAL_IN_5GHZ = "Device.WiFi.Radio.10100.GuardInterval";

    /** WebPA Parameter to get WiFi Radio.10100 Operating standard */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_OPERATINGSTANDARDS_IN_5GHZ = "Device.WiFi.Radio.10100.OperatingStandards";

    /** WebPA Parameter to get WiFi Radio.10100 Operating standard */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_OPERATINGSTANDARDS_IN_2GHZ = "Device.WiFi.Radio.10000.OperatingStandards";

    /** WebPA Parameter for ENABLING radio setting for Wifi 5 Ghz */
    public static final String WEBPA_PARAM_WIFI_5_RADIO_ENABLE = "Device.WiFi.Radio.10100.Enable";

    /** web pa parameter to get the encryption method in 2.4ghz for private wifi */
    public static final String WEBPA_PARAM_FOR_ENCRYPTIONMETHOD_IN_2GHZ_PRIVATE_WIFI = "Device.WiFi.AccessPoint.10001.Security.X_CISCO_COM_EncryptionMethod";

    /** web pa parameter to get the encryption method in 5ghz for private wifi */
    public static final String WEBPA_PARAM_FOR_ENCRYPTIONMETHOD_IN_5GHZ_PRIVATE_WIFI = "Device.WiFi.AccessPoint.10101.Security.X_CISCO_COM_EncryptionMethod";

    /** This enum stores diffrent methods available for restting wifi settings */
    public enum WIFI_RESTORE_METHOD {
	SNMP,
	WEBPA,
	MSO_GUI,
	ADMIN_GUI;
    }

    /** This enum stores the non default values for all 2.4 and 5GHZ wifi parameters */
    public enum NonDefaultWiFiParametersEnum {

	NONDEFAULT_VALUE_SSID_2_4(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID, "TEST-2.4", 0),
	NONDEFAULT_VALUE_SECURITY_MODE_2_4(
		BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_PRIVATE_SECURITY_MODEENABLED,
		"WPA2-Enterprise",
		0),
	NONDEFAULT_VALUE_CHANNEL_MODE_2_4(
		BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_2GHZ,
		"false",
		3),
	NONDEFAULT_VALUE_BANDWIDTH_2_4(
		BroadBandWebPaConstants.WEBPA_PARAM_FOR_OPERATING_BANDWIDTH_IN_2GHZ_BAND,
		"20MHz",
		0),
	NONDEFAULT_VALUE_GUARD_INTERVAL_2_4(
		BroadBandWebPaConstants.WEBPA_PARAM_FOR_GUARD_INTERVAL_IN_2GHZ,
		"800nsec",
		0),
	NONDEFAULT_VALUE_NETWORK_NAME_ENABLED_2_4(
		BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_ADV_ENABLED,
		"false",
		3),
	NONDEFAULT_VALUE_NETWORK_ACTIVE_STATUS_2_4(
		BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_ENABLED_STATUS,
		"false",
		3),
	NONDEFAULT_VALUE_OPERATING_STANDARD_2_4(
		BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_RADIO_10000_OPERATINGSTANDARDS,
		"n",
		0),
	NONDEFAULT_VALUE_RADIO_STATUS_2_4(BroadBandWebPaConstants.WEBPA_PARAM_WIFI_2_4_RADIO_ENABLE, "false", 3),
	NONDEFAULT_VALUE_RADIO_CHANNEL_2_4(
		BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_RADIO_CHANNEL_IN_2GHZ,
		"3",
		2),
	NONDEFAULT_VALUE_SSID_5(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID, "TEST-5", 0),
	NONDEFAULT_VALUE_SECURITY_MODE_5(
		BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_PRIVATE_SECURITY_MODEENABLED,
		"WPA2-Enterprise",
		0),
	NONDEFAULT_VALUE_CHANNEL_MODE_5(
		BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_5GHZ,
		"false",
		3),
	NONDEFAULT_VALUE_BANDWIDTH_5(
		BroadBandWebPaConstants.WEBPA_PARAM_FOR_OPERATING_BANDWIDTH_IN_5GHZ_BAND,
		"40MHz",
		0),
	NONDEFAULT_VALUE_GUARD_INTERVAL_5(BroadBandWebPaConstants.WEBPA_PARAM_FOR_GUARD_INTERVAL_IN_5GHZ, "800nsec", 0),
	NONDEFAULT_VALUE_NETWORK_NAME_ENABLED_5(
		BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_ADV_ENABLED,
		"false",
		3),
	NONDEFAULT_VALUE_NETWORK_ACTIVE_STATUS_5(
		BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_ENABLED_STATUS,
		"false",
		3),
	NONDEFAULT_VALUE_OPERATING_STANDARD_5(
		BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_RADIO_OPERATINGSTANDARDS_IN_5GHZ,
		"n",
		0),
	NONDEFAULT_VALUE_RADIO_STATUS_5(BroadBandWebPaConstants.WEBPA_PARAM_WIFI_5_RADIO_ENABLE, "false", 3),
	NONDEFAULT_VALUE_RADIO_CHANNEL_5(
		BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_RADIO_CHANNEL_IN_5GHZ,
		"52",
		2);

	String webPaParam;
	String webPaValue;
	int valueType;

	/**
	 * Constructor
	 * 
	 * @param webPaParam
	 * @param webPaValue
	 * @param valueType
	 */
	NonDefaultWiFiParametersEnum(String webPaParam, String webPaValue, int valueType) {
	    this.webPaParam = webPaParam;
	    this.webPaValue = webPaValue;
	    this.valueType = valueType;
	}

	/**
	 * @return the webPaParam
	 */
	public String getWebPaParam() {
	    return webPaParam;
	}

	/**
	 * @param webPaParam
	 *            the webPaParam to set
	 */
	public void setWebPaParam(String webPaParam) {
	    this.webPaParam = webPaParam;
	}

	/**
	 * @return the webPaValue
	 */
	public String getWebPaValue() {
	    return webPaValue;
	}

	/**
	 * @param webPaValue
	 *            the webPaValue to set
	 */
	public void setWebPaValue(String webPaValue) {
	    this.webPaValue = webPaValue;
	}

	/**
	 * @return the valueType
	 */
	public int getValueType() {
	    return valueType;
	}

	/**
	 * @param valueType
	 *            the valueType to set
	 */
	public void setValueType(int valueType) {
	    this.valueType = valueType;
	}
    }

    /**
     * WebPA parameters array for 2.4GHz band
     */
    public static final String[] PARAMETERS_FOR_2_4_GHZ_BAND = new String[] {
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_PRIVATE_SECURITY_MODEENABLED,
	    BroadBandWebPaConstants.WEBPA_PARAM_FOR_ENCRYPTIONMETHOD_IN_2GHZ_PRIVATE_WIFI,
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_2_4GHZ_PRIVATE_SSID,
	    BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_2GHZ,
	    BroadBandWebPaConstants.WEBPA_PARAM_FOR_OPERATING_BANDWIDTH_IN_2GHZ_BAND,
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_ADV_ENABLED,
	    BroadBandWebPaConstants.WEBPA_PARAM_FOR_GUARD_INTERVAL_IN_2GHZ,
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_ENABLED_STATUS,
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_RADIO_10000_OPERATINGSTANDARDS,
	    BroadBandWebPaConstants.WEBPA_PARAM_WIFI_2_4_RADIO_ENABLE,
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_RADIO_CHANNEL_IN_2GHZ };

    /**
     * WebPA parameters array for 5GHz band
     */
    public static final String[] PARAMETERS_FOR_5GHZ_BAND = new String[] {
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_PRIVATE_SECURITY_MODEENABLED,
	    BroadBandWebPaConstants.WEBPA_PARAM_FOR_ENCRYPTIONMETHOD_IN_5GHZ_PRIVATE_WIFI,
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_5GHZ_PRIVATE_SSID,
	    BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_5GHZ,
	    BroadBandWebPaConstants.WEBPA_PARAM_FOR_OPERATING_BANDWIDTH_IN_5GHZ_BAND,
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_ADV_ENABLED,
	    BroadBandWebPaConstants.WEBPA_PARAM_FOR_GUARD_INTERVAL_IN_5GHZ,
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_ENABLED_STATUS,
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_RADIO_OPERATINGSTANDARDS_IN_5GHZ,
	    BroadBandWebPaConstants.WEBPA_PARAM_WIFI_5_RADIO_ENABLE,
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_RADIO_CHANNEL_IN_5GHZ };

    /** webpa parameter to factory reset the box */
    public static final String WEBPA_PARAM_FOR_WIFI_RESET = "Device.WiFi.X_CISCO_COM_FactoryResetRadioAndAp";

    /** enum variable to store default values of wifi parameters in 2.4Ghz after factory reset */

    public enum RdkBWifiParameters {
	SECURITY_MODE_2GHZ_PRIVATE_WIFI("Device.WiFi.AccessPoint.10001.Security.ModeEnabled", "WPA2-Personal"),
	SECURITY_MODE_5GHZ_PRIVATE_WIFI("Device.WiFi.AccessPoint.10101.Security.ModeEnabled", "WPA2-Personal"),
	ENCRYPTION_MODE_2GHZ_PRIVATE_WIFI("Device.WiFi.AccessPoint.10001.Security.X_CISCO_COM_EncryptionMethod", "AES"),
	ENCRYPTION_MODE_5GHZ_PRIVATE_WIFI("Device.WiFi.AccessPoint.10101.Security.X_CISCO_COM_EncryptionMethod", "AES"),
	SSID_STATUS_2GHZ_PRIVATE_WIFI("Device.WiFi.SSID.10001.Status", "Up"),
	SSID_STATUS_5GHZ_PRIVATE_WIFI("Device.WiFi.SSID.10101.Status", "Up"),
	AUTOCHANNEL_ENABLE_STATUS_2GHZ("Device.WiFi.Radio.10000.AutoChannelEnable", "true"),
	AUTOCHANNEL_ENABLE_STATUS_5GHZ("Device.WiFi.Radio.10100.AutoChannelEnable", "true"),
	OPERATING_CHANNEL_BANDWIDTH_2GHZ("Device.WiFi.Radio.10000.OperatingChannelBandwidth", "20MHz"),
	OPERATING_CHANNEL_BANDWIDTH_2GHZ_FOR_FIBRE_DEVICES("Device.WiFi.Radio.10000.OperatingChannelBandwidth", "40MHz"),
	OPERATING_CHANNEL_BANDWIDTH_5GHZ("Device.WiFi.Radio.10100.OperatingChannelBandwidth", "80MHz"),
	SSID_ADVERTISEMENT_ENABLE_2GHZ_PRIVATE_WIFI("Device.WiFi.AccessPoint.10001.SSIDAdvertisementEnabled", "true"),
	SSID_ADVERTISEMENT_ENABLE_5GHZ_PRIVATE_WIFI("Device.WiFi.AccessPoint.10101.SSIDAdvertisementEnabled", "true"),
	GUARD_INTERVAL_2GHZ("Device.WiFi.Radio.10000.GuardInterval", "Auto"),
	GUARD_INTERVAL_5GHZ("Device.WiFi.Radio.10100.GuardInterval", "Auto"),
	SSID_ENABLE_STATUS_2GHZ_PRIVATE_WIFI("Device.WiFi.SSID.10001.Enable", "true"),
	SSID_ENABLE_STATUS_5GHZ_PRIVATE_WIFI("Device.WiFi.SSID.10101.Enable", "true"),
	OPERATING_STANDARDS_2GHZ("Device.WiFi.Radio.10000.OperatingStandards", "g,n"),
	OPERATING_STANDARDS_5GHZ("Device.WiFi.Radio.10100.OperatingStandards", "a,n,ac"),
	RADIO_STATUS_2GHZ("Device.WiFi.Radio.10000.Enable", "true"),
	RADIO_STATUS_5GHZ("Device.WiFi.Radio.10100.Enable", "true"),
	RADIO_CHANNEL_2GHZ("Device.WiFi.Radio.10000.Channel", "1"),
	RADIO_CHANNEL_5GHZ("Device.WiFi.Radio.10100.Channel", "157");

	private String defaultValue;
	private String parameterName;

	RdkBWifiParameters(String parameterName, String value) {
	    this.defaultValue = value;
	    this.parameterName = parameterName;
	}

	public String getDefaultValue() {
	    return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
	    this.defaultValue = defaultValue;
	}

	public String getParameterName() {
	    return parameterName;
	}

	public void setParameterName(String parameterName) {
	    this.parameterName = parameterName;
	}
    }

    /** Weba parameter to enable/disable telemetry 2 */
    public static final String WEBPA_PARAM_FOR_TELEMETRY_2_0_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.Telemetry.Enable";

    /**
     * WebPA Parameter for data Block Size
     */
    public static final String WEBPA_DATABLOCKSIZE = "Device.IP.Diagnostics.IPPing.DataBlockSize";

    /** WebPA Parameter for aker Parental Control Scheduler */
    public static final String WEBPA_PARAM_DEVICE_RDKCENTRAL_AKER_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_AkerEnable";

    /** Webpa parameter for 5GHz XH SSID status */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_10102_STATUS = "Device.WiFi.SSID.10102.Status";

    /** Webpa parameter for 2.4GHz XH SSID status */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_10002_STATUS = "Device.WiFi.SSID.10002.Status";

    /** webpa Parameter for MoCA interface enable . */
    public static final String WEBPA_PARAM_FOR_MOCA_INTERFACE_ENABLE = "Device.MoCA.Interface.1.Enable";

    /** WebPa Parameter for getting Moca Status */
    public static final String WEBPA_PARAM_GET_MOCA_STATUS = "Device.MoCA.Interface.1.Status";

    /** WebPA Parameter to enable/disable XDNS Feature */
    public static final String WEBPA_PARAM_TO_GET_XDNS_FEATURE_STATUS = "Device.DeviceInfo.X_RDKCENTRAL-COM_EnableXDNS";

    /** Webpa Param for Device Cloud UI enable */
    public static final String WEBPA_PARAM_DEVICE_CLOUD_UI = "Device.DeviceInfo.X_RDKCENTRAL-COM_CloudUIEnable";

    /** webpa parameter to validate 2g channel */
    public static final String WEBPA_WAREHOUSE_WIFI_2G_CHANNEL = "Device.WiFi.Radio.10000.Channel";

    /** webpa parameter to validate 5g channel */
    public static final String WEBPA_WAREHOUSE_WIFI_5G_CHANNEL = "Device.WiFi.Radio.10100.Channel";

    /** webpa parameter to get the channel selection mode for 2.4 GHz Radio */
    public static final String WEBPA_PARAM_2_4_GHZ_CHANNEL_SELECTION_MODE = "Device.WiFi.Radio.10000.AutoChannelEnable";

    /** webpa parameter to get the channel selection mode for 5 GHz Radio */
    public static final String WEBPA_PARAM_5_GHZ_CHANNEL_SELECTION_MODE = "Device.WiFi.Radio.10100.AutoChannelEnable";

    /** webpa parameter to validate 2g wireless channel */
    public static final String WEBPA_WAREHOUSE_WIFI_2G_WIRELESS_CHANNEL = "Device.WiFi.Radio.10000.AutoChannelEnable";

    /** webpa parameter to validate 5g wireless channel */
    public static final String WEBPA_WAREHOUSE_WIFI_5G_WIRELESS_CHANNEL = "Device.WiFi.Radio.10100.AutoChannelEnable";

    /**
     * WebPA Parameter to get status of WPS for 2.4GHZ Private Network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_WPS_STATUS_2_4 = "Device.WiFi.AccessPoint.10001.WPS.Enable";

    /**
     * WebPA Parameter to get status of WPS for 5GHZ Private Network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_WPS_STATUS_5 = "Device.WiFi.AccessPoint.10101.WPS.Enable";

    /** WebPa Parameter to defer firmware download reboot */
    public static final String WEBPA_PARAM_DEFER_FIRMWARE_DOWNLOAD_REBOOT = "Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.RPC.DeferFWDownloadReboot";

    /**
     * WebPA Parameter to represent Number of allowed clients for 5 GHz Secure SSID
     */
    public static final String WEBPA_PARAM_ALLOWED_CLIENT_LIMIT_SECURE_SSID_5 = "Device.WiFi.AccessPoint.10105.X_CISCO_COM_BssMaxNumSta";

    /**
     * WebPA Parameter to change/get Advertisement in 5G Public network
     */
    public static final String WEBPA_PARAM_Device_WiFi_AccessPoint_10103_SSIDAdvertisementEnabled = "Device.WiFi.AccessPoint.10103.SSIDAdvertisementEnabled";

    /**
     * WebPA Parameter to change/get Advertisement in 2.4G public network
     */
    public static final String WEBPA_PARAM_Device_WiFi_AccessPoint_10003_SSIDAdvertisementEnabled = "Device.WiFi.AccessPoint.10003.SSIDAdvertisementEnabled";

    /**
     * WebPa Parameter to retrieve Device Manufacturer name
     */
    public static final String WEBPA_DEVICE_MANUFACTURER_NAME = "Device.DeviceInfo.Manufacturer";

    /** WebPA parameter for secured xfinity wifi 5GHz SSID name */
    public static final String WEBPA_PARAM_5GHZ_SECURED_XFINITY_WIFI_SSID = "Device.WiFi.SSID.10105.SSID";

    /** WebPA parameter to get the secured xfinity 2.4GHz wifi SSID enabled status */
    public static final String WEBPA_PARAM_2GHZ_SECURED_XFINITY_WIFI_SSID_ENABLED = "Device.WiFi.SSID.10005.Enable";

    /** WebPA parameter to get the secured xfinity 5GHz wifi SSID enabled status */
    public static final String WEBPA_PARAM_5GHZ_SECURED_XFINITY_WIFI_SSID_ENABLED = "Device.WiFi.SSID.10105.Enable";

    /** webpa parameter to get the value of Device.WiFi.AccessPoint.10105.Security.ModeEnabled */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5GHZ_SECURED_XFINITY = "Device.WiFi.AccessPoint.10105.Security.ModeEnabled";

    /** WebPA Parameter to update the Global DNS IPv4 value */
    public static final String WEBPA_PARAM_TO_UPDATE_GLOBAL_XDNS_IPV4 = "Device.X_RDKCENTRAL-COM_XDNS.DefaultDeviceDnsIPv4";

    /** WebPA Parameter to update the Global DNS IPv6 value */
    public static final String WEBPA_PARAM_TO_UPDATE_GLOBAL_XDNS_IPV6 = "Device.X_RDKCENTRAL-COM_XDNS.DefaultDeviceDnsIPv6";

    /**
     * WebPA Parameter to get MAC Address of 2.4G XH Network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_MAC_ADDRESS = "Device.WiFi.SSID.10001.MACAddress";

    /**
     * WebPA Parameter to change the DSCPMarkPolicy
     */
    public static final String WEBPA_PARAM_DSCP_MARK_POLICY = "Device.X_COMCAST-COM_GRE.Tunnel.1.DSCPMarkPolicy";

    /**
     * WebPA Parameter to change the PrimaryRemoteEndpoint
     */
    public static final String WEBPA_PARAM_PRIMARY_REMOTE_ENDPOINT = "Device.X_COMCAST-COM_GRE.Tunnel.1.PrimaryRemoteEndpoint";

    /**
     * WebPA Parameter to change the SecondaryRemoteEndpoint
     */
    public static final String WEBPA_PARAM_SECONDARY_REMOTE_ENDPOINT = "Device.X_COMCAST-COM_GRE.Tunnel.1.SecondaryRemoteEndpoint";
    // TODO check if below params usable
    public static final String WEBPA_PARAMETER_FOR_BEACON_INTERVAL = "Device.WiFi.Radio.10100.X_COMCAST-COM_BeaconInterval";
    /** WebPA Parameter for selfheal max reboot */
    public static final String WEBPA_PARAMETER_FOR_SELFHEAL_MAXREBOOT = "Device.SelfHeal.X_RDKCENTRAL-COM_MaxRebootCount";
    /** WebPA Parameter for selfheal ping wait time */
    public static final String WEBPA_PARAMETER_FOR_SELFHEAL_PING_WAITTIME = "Device.SelfHeal.ConnectivityTest.X_RDKCENTRAL-COM_PingRespWaitTime";
    /** WebPA Parameter for 2.4Ghz rekey interval */
    public static final String WEBPA_PARAMETER_FOR_2GHZ_REKEY_INTERVAL = "Device.WiFi.AccessPoint.10001.Security.RekeyingInterval";
    /** WebPA Parameter for 5Ghz rekey interval */
    public static final String WEBPA_PARAMETER_FOR_5GHZ_REKEY_INTERVAL = "Device.WiFi.AccessPoint.10101.Security.RekeyingInterval";
    /** WebPA Parameter for 5Ghz radius reauthentication interval */
    public static final String WEBPA_PARAMETER_FOR_5GHZ_RADIUS_REAUTH_INTERVAL = "Device.WiFi.AccessPoint.10101.Security.X_CISCO_COM_RadiusReAuthInterval";
    /** WebPA Parameter for XHS 2.4Ghz ssid */
    public static final String WEBPA_PARAMETER_FOR_XHS_24GHZ_SSID = "Device.WiFi.SSID.10002.SSID";
    /** WebPA Parameter for XHS 5Ghz ssid */
    public static final String WEBPA_PARAMETER_FOR_XHS_5GHZ_SSID = "Device.WiFi.SSID.10102.SSID";
    /** WebPA Parameter for XHS 2.4Ghz pass key */
    public static final String WEBPA_PARAMETER_FOR_XHS_24GHZ_KEY = "Device.WiFi.AccessPoint.10002.Security.KeyPassphrase";
    /** WebPA Parameter for XHS 5Ghz pass key */
    public static final String WEBPA_PARAMETER_FOR_XHS_5GHZ_KEY = "Device.WiFi.AccessPoint.10102.Security.KeyPassphrase";

    /** WebPA parameter for 2.4 Ghz Wifi access point alias **/
    public static final String WEBPA_PARAM_2_4GHZ_WIFI_ACCESSPOINT_ALIAS = "Device.WiFi.AccessPoint.10001.Alias";

    /** WebPA parameter for 5 Ghz Wifi access point alias **/
    public static final String WEBPA_PARAM_5GHZ_WIFI_ACCESSPOINT_ALIAS = "Device.WiFi.AccessPoint.10101.Alias";

    /** WebPA parameter for 2.4 Ghz Wifi access point retry limit **/
    public static final String WEBPA_PARAM_2_4GHZ_WIFI_ACCESSPOINT_RETRY_LIMIT = "Device.WiFi.AccessPoint.10001.RetryLimit";

    /** WebPA parameter for 5 Ghz Wifi access point retry limit **/
    public static final String WEBPA_PARAM_5GHZ_WIFI_ACCESSPOINT_RETRY_LIMIT = "Device.WiFi.AccessPoint.10101.RetryLimit";

    /** WebPA parameter to enable selfheal in the device */
    public static final String WEBPA_PARAM_DEVICE_SELFHEAL_PROCESS_ENABLE_STATUS = "Device.SelfHeal.X_RDKCENTRAL-COM_Enable";

    /** webpa parameter for get default PASSWORD for 2.4 GHz after factory reset */
    public static final String WEBPA_PARAM_DEVICE_PASSWORD_DEFAULT_SSID_2_4 = "Device.WiFi.AccessPoint.10001.Security.X_COMCAST-COM_DefaultKeyPassphrase";

    /** webpa parameter for get default PASSWORD for 5 GHz after factory reset */
    public static final String WEBPA_PARAM_DEVICE_PASSWORD_DEFAULT_SSID_5 = "Device.WiFi.AccessPoint.10101.Security.X_COMCAST-COM_DefaultKeyPassphrase";

    /** webpa parameter to enable/disable snmpv2 support */
    public static final String WEBPA_PARAM_TO_DISABLE_SNMPV2 = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.SNMP.V2Support";

    /** WebPA Parameter to check MAC Address of 5G Public Network */
    public static final String WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PUBLIC_SSID_MAC_ADDRESS = "Device.WiFi.SSID.10103.MACAddress";

    /** WebPA Parameter to get MAC Address of 2.4G Public Network */
    public static final String WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PUBLIC_SSID_MAC_ADDRESS = "Device.WiFi.SSID.10003.MACAddress";

    /** WebPA parameter to get the SSID enabled status for 2.4 GHz Wi-Fi network. */
    public static final String WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_ENABLED = "Device.WiFi.SSID.10001.Enable";

    /** WebPA parameter to get max CPE value */
    public static final String WEBPA_PARAM_TO_CHECK_CPE_VALUE = "Device.X_CISCO_COM_CableModem.MaxCpeAllowed";

    /** WebPA parameter to get the delegated IPv6 prefix */
    public static final String WEBPA_PARAM_DELEGATED_IPV6_PREFIX = "Device.IP.Interface.1.IPv6Prefix.1.Prefix";

    /** TR69 Parameter to retrieve Serial Number */
    public static final String TR69_PARAM_SERIAL_NUMBER = "Device.DeviceInfo.SerialNumber";

    /**
     * WebPA Parameter for SNMP version 3 support
     */
    public static final String WEBPA_PARAM_SNMP_VERSION_3_SUPPORT = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.SNMP.V3Support";

    /**
     * WebPA Parameter for SNMP version 3 dh kick start security number
     */
    public static final String WEBPA_PARAM_SNMP_VERSION_3_DH_KICKSTART_SECURITY_NUMBER = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.Snmpv3DHKickstart.Table.{i}.SecurityNumber";

    /**
     * WebPA Parameter for SNMP version 3 dh kick start security number
     */
    public static final String WEBPA_PARAM_SNMP_VERSION_3_DH_KICKSTART_SECURITY_NAME = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.Snmpv3DHKickstart.Table.{i}.SecurityName";

    /**
     * WebPA Parameter for SNMP version 3 dh kick start
     */
    public static final String WEBPA_PARAM_SNMP_VERSION_3_DH_KICKSTART = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.Snmpv3DHKickstart.Enabled";

    /**
     * WebPA Parameter for SNMP version 3 dh kick start table number
     */
    public static final String WEBPA_PARAM_SNMP_VERSION_3_DH_KICKSTART_TABLE_NUMBER = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.Snmpv3DHKickstart.TableNumberOfEntries";

    /**
     * WebPA Parameter for SNMP version 2 support
     */
    public static final String WEBPA_PARAM_SNMP_VERSION_2_SUPPORT = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.SNMP.V2Support";

    /** WebPA Parameter Interface Device Wifi Report */
    public static final String WEBPA_INTERFACE_DEVICES_WIFI_REPORT = "Device.X_RDKCENTRAL-COM_Report.InterfaceDevicesWifi.Enabled";

    /** WebPA Parameter network Device traffic Report */
    public static final String WEBPA_NETWORK_DEVICES_TRAFFIC_WIFI_REPORT = "Device.X_RDKCENTRAL-COM_Report.NetworkDevicesTraffic.Enabled";
    
    /** WebPA Parameter to add/get DNS Mapping Table */

    public static final String WEBPA_PARAM_DNS_MAPPING_TABLE = "Device.X_RDKCENTRAL-COM_XDNS.DNSMappingTable.";    

    /** Weba parameter to hold configuration URL for telemetry 2 */
    public static final String WEBPA_PARAM_FOR_TELEMETRY_2_0_CONFIG_URL = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.Telemetry.ConfigURL";

    /** Weba parameter to hold version for telemetry 2 */
    public static final String WEBPA_PARAM_FOR_TELEMETRY_2_0_VERSION = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.Telemetry.Version";
    
    /** WebPA Parameter for Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.PrivacyProtection.Enable */
    public static final String WEBPA_PARAM_PRIVACY_PROTECTION_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.PrivacyProtection.Enable";
    
    /** WebPA Parameter for Device.DeviceInfo.X_RDKCENTRAL-COM_PrivacyProtection.Activate */
    public static final String WEBPA_PARAM_PRIVACY_PROTECTION_ACTIVATE = "Device.DeviceInfo.X_RDKCENTRAL-COM_PrivacyProtection.Activate";
    
    /** WebPA Constant for Get attribute */
    public static final String WEBPA_PARAM_DEVICE_CM_IP_PARENTAL_CONTROL = "Device.DeviceInfo.X_COMCAST-COM_CM_IP,Device.X_Comcast_com_ParentalControl.RollbackUTC_Local";
    
    /** Webpa Param for Parental Control Managed Devices */
    public static final String WEBPA_PARAM_PARENTAL_CONTROL_MANAGED_DEVICES = "Device.X_Comcast_com_ParentalControl.ManagedDevices.Device.";

    /** Webpa Param for server pool static address */
    public static final String WEBPA_PARAM_SERVER_POOL_STATIC_ADDRESS = "Device.DHCPv4.Server.Pool.1.StaticAddress.";
    
    /** WebPA Parameter to retrieve DHCP starting range IP Address */
    public static final String WEBPA_PARAM_TO_RETRIEVE_DHCP_STARTING_IP_ADDRESS = "Device.DHCPv4.Server.Pool.1.MinAddress";

    /** WebPA Parameter to retrieve DHCP ending range IP Address */
    public static final String WEBPA_PARAM_TO_RETRIEVE_DHCP_ENDING_IP_ADDRESS = "Device.DHCPv4.Server.Pool.1.MaxAddress";

    /** webpa parameter to validate wireless ssid password for private 2.4GHz */
    public static final String WEBPA_WAREHOUSE_WIRELESS_SSID_PASSWORD_PRIVATE_2G = "Device.WiFi.AccessPoint.10001.Security.KeyPassphrase";

    /** webpa parameter to validate wireless ssid password for private 5GHz */
    public static final String WEBPA_WAREHOUSE_WIRELESS_SSID_PASSWORD_PRIVATE_5G = "Device.WiFi.AccessPoint.10101.Security.KeyPassphrase";
    
    /** webpa parameter to validate for upgrade server */
    public static final String WEBPA_WAREHOUSE_UPGRADE_WITH_RESET = "Device.DeviceInfo.X_RDKCENTRAL-COM_FirmwareDownloadAndFactoryReset";
    /**
     * Map with snmp Name as key and corresponding webpa parameter as value to pass the webpa parameter as argument and
     * fetch the webpa response.
     */
    public static HashMap<String, String> webParamMap = new HashMap<String, String>() {
	{
	    put(BroadBandTestConstants.WAREHOUSE_WIRELESS_SSID1_ENABLE_LNF_2_4,
		    WEBPA_WAREHOUSE_WIRELESS_SSID1_ENABLE_LNF_2_4);
	    put(BroadBandTestConstants.WAREHOUSE_WIRELESS_SSID2_ENABLE_LNF_2_4,
		    WEBPA_WAREHOUSE_WIRELESS_SSID2_ENABLE_LNF_2_4);
	    put(BroadBandTestConstants.WAREHOUSE_WIRELESS_SSID_ENABLE_LNF_5G,
		    WEBPA_WAREHOUSE_WIRELESS_SSID_ENABLE_LNF_5G);
	    put(BroadBandTestConstants.WAREHOUSE_WIRELESS_SSID_PASSWORD_PRIVATE_2G,
		    WEBPA_WAREHOUSE_WIRELESS_SSID_PASSWORD_PRIVATE_2G);
	    put(BroadBandTestConstants.WAREHOUSE_WIRELESS_SSID_PASSWORD_PRIVATE_5G,
		    WEBPA_WAREHOUSE_WIRELESS_SSID_PASSWORD_PRIVATE_5G);
	    put(BroadBandTestConstants.WAREHOUSE_PARAM_DEVICE_WIFI_SSID_10002_SSID,
	    WEBPA_PARAM_DEVICE_WIFI_SSID_10002_SSID);
	    put(BroadBandTestConstants.WAREHOUSE_PARAM_DEVICE_WIFI_SSID_10102_SSID,
	    WEBPA_PARAM_DEVICE_WIFI_SSID_10102_SSID);
	    put(BroadBandTestConstants.WAREHOUSE_WIFI_2G_CHANNEL, WEBPA_WAREHOUSE_WIFI_2G_CHANNEL);
	    put(BroadBandTestConstants.WAREHOUSE_WIFI_5G_CHANNEL, WEBPA_WAREHOUSE_WIFI_5G_CHANNEL);
	    put(BroadBandTestConstants.WAREHOUSE_WIFI_2G_WIRELESS_CHANNEL, WEBPA_WAREHOUSE_WIFI_2G_WIRELESS_CHANNEL);
	    put(BroadBandTestConstants.WAREHOUSE_WIFI_5G_WIRELESS_CHANNEL, WEBPA_WAREHOUSE_WIFI_5G_WIRELESS_CHANNEL);
	    put(BroadBandTestConstants.WAREHOUSE_UPGRADE_WITH_RESET, WEBPA_WAREHOUSE_UPGRADE_WITH_RESET);
	}
    };
    
    /** Webpa parameter for FWUpdate auto exclude enable */
    public static final String WEBPA_PARAM_FWUPDATE_AUTO_EXCLUDED_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.FWUpdate.AutoExcluded.Enable";

    /** Webpa parameter for FWUpdate auto exclude url */
    public static final String WEBPA_PARAM_FWUPDATE_AUTO_EXCLCUDED_XCONF_URL = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.FWUpdate.AutoExcluded.XconfUrl";
    /**
     * Webpa Param for wifi ssid Security mode
     */
    public static final String WEBPA_PARAM_SECURITY_MODE_OF_WIFI = "Device.WiFi.AccessPoint.{i}.Security.ModeEnabled";
    
    /** webpa parameter to get the value of Device.WiFi.AccessPoint.10005.Security.ModeEnabled */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4GHZ_SECURED_XFINITY = "Device.WiFi.AccessPoint.10005.Security.ModeEnabled";
    
    /** WebPA Parameter for secured wifi 2.4GHz RadiusServerIPAddr */
    public static final String WEBPA_PARAM_2_4GHZ_WIFI_10005_RADIUSSERVERIPADDR = "Device.WiFi.AccessPoint.10005.Security.RadiusServerIPAddr";
    
    /** WebPA Parameter for secured wifi 2.4GHz secondary RadiusServerIPAddr */
    public static final String WEBPA_PARAM_2_4GHZ_WIFI_10005_SECONDARY_RADIUSSERVERIPADDR = "Device.WiFi.AccessPoint.10005.Security.SecondaryRadiusServerIPAddr";
    
    /** WebPA Parameter for secured wifi 5GHz RadiusServerIPAddr */
    public static final String WEBPA_PARAM_5GHZ_WIFI_10105_RADIUSSERVERIPADDR = "Device.WiFi.AccessPoint.10105.Security.RadiusServerIPAddr";
    
    /** WebPA Parameter for secured wifi 5GHz secondary RadiusServerIPAddr */
    public static final String WEBPA_PARAM_5GHZ_WIFI_10105_SECONDARY_RADIUSSERVERIPADDR = "Device.WiFi.AccessPoint.10105.Security.SecondaryRadiusServerIPAddr";
   
    /** WebPA Parameter for trace route host */
   public static final String WEBPA_PARAM_TRACEROUTE_HOST = "Device.IP.Diagnostics.TraceRoute.Host";
  
   /** WebPA Parameter for trace route diagnostic state */
   public static final String WEBPA_PARAM_TRACEROUTE_DIAGNOSTIC_STATE = "Device.IP.Diagnostics.TraceRoute.DiagnosticsState";
   
   /** WebPa Parameter for Trace route hops */
   public static final String WEBPA_PARAM_TRACEROUTE_HOPS = "Device.IP.Diagnostics.TraceRoute.RouteHops.";
   
   /** WebPa Parameter for trace route first hop host */
   public static final String WEBPA_PARAM_TRACEROUTE_HOPS_FIRST_HOST = "Device.IP.Diagnostics.TraceRoute.RouteHops.1.Host";
   
   /** TR69 parameter to get the last reboot reason */
   public static final String WEBPA_COMMAND_LAST_REBOOT_REASON = "Device.DeviceInfo.X_RDKCENTRAL-COM_LastRebootReason";
   
   /**
    * WebPA Parameter to get SSID of 2.4G Private Network
    */
   public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_10001_SSID = "Device.WiFi.SSID.10001.SSID";
   
   /** WebPA Parameter Interface Device Wifi Report */
   public static final String WEBPA_NETWORK_DEVICES_WIFI_REPORT = "Device.X_RDKCENTRAL-COM_Report.NetworkDevicesStatus.Enabled";
   
   /** WebPA Parameter neighboringAP Enabled Report */
   public static final String WEBPA_PARAM_NEIGHBORINGAP_ENABLED_REPORT = "Device.X_RDKCENTRAL-COM_Report.NeighboringAP.Enabled";
   
   /** WebPA Parameter polling period of neighboringAP */
   public static final String WEBPA_PARAM_NEIGHBORINGAP_POLLING_PERIOD = "Device.X_RDKCENTRAL-COM_Report.NeighboringAP.PollingPeriod";
 
   /** WebPA Parameter reporting period of neighboringAP */
   public static final String WEBPA_PARAM_NEIGHBORINGAP_REPORTING_PERIOD = "Device.X_RDKCENTRAL-COM_Report.NeighboringAP.ReportingPeriod";
   


}

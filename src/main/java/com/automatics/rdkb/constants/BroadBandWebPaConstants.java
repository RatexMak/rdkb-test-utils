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
import java.util.Map;

import com.automatics.rdkb.constants.BroadBandWebPaConstants.RdkBBandSteeringParameters;

public class BroadBandWebPaConstants {

    /** WebPA Parameter to update XDNS Device Tag Name */
    public static final String WEBPA_PARAM_TO_UPDATE_XDNS_DEVICE_TAG = "Device.X_RDKCENTRAL-COM_XDNS.DefaultDeviceTag";

    /** Webpa param for 2.4GHz LNF SSIDAdvertisementEnabled */
    public static final String WEBPA_PARAM_Device_WiFi_AccessPoint_10004_SSIDAdvertisementEnabled = "Device.WiFi.AccessPoint.10004.SSIDAdvertisementEnabled";

    /** Webpa param for 5GHz LNF SSIDAdvertisementEnabled */
    public static final String WEBPA_PARAM_Device_WiFi_AccessPoint_10104_SSIDAdvertisementEnabled = "Device.WiFi.AccessPoint.10104.SSIDAdvertisementEnabled";

    /** WebPA Parameter to get Default SSID for 2.4 Ghz */
    public static final String WEBPA_DEFAULT_SSID_NAME_2_4_GHZ = "Device.WiFi.SSID.10001.X_COMCAST-COM_DefaultSSID";

    /** WebPA Parameter to get Default SSID for 5 Ghz */
    public static final String WEBPA_DEFAULT_SSID_NAME_5_GHZ = "Device.WiFi.SSID.10101.X_COMCAST-COM_DefaultSSID";

    /** WebPA Parameter for Private Prefer Connection */
    public static final String WEBPA_PARAMETER_PREFER_PRIVATE_CONNECTION = "Device.WiFi.X_RDKCENTRAL-COM_PreferPrivate";

    /** WebPA Parameter to get SSID of 5G Private Network */
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

    /**
     * Webpa parameter to get Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_Control.ActivatePartnerId
     */
    public static final String WEBPA_PARAM_ACTIVATE_PARTNER_ID = "Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_Control.ActivatePartnerId";

    /** webpa parameter for WiFi Region code */
    public static final String WEBPA_PARAM_WIFI_REGION_CODE = "Device.WiFi.X_RDKCENTRAL-COM_Syndication.WiFiRegion.Code";

    /** WebPA Parameter for band steering capability */
    public static final String WEBPA_PARAM_BAND_STEERING_CAPABILITY = "Device.WiFi.X_RDKCENTRAL-COM_BandSteering.Capability";

    /** webpa Parameter for 2.4GHz guest SSID name */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_10002_SSID = "Device.WiFi.SSID.10002.SSID";

    /** Webpa param for 5GHz guest SSID name */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_10102_SSID = "Device.WiFi.SSID.10102.SSID";

    /** WebPA parameter for Device ManagementServer Connection Request URL */
    public static final String WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_CONNECTIONREQUESTURL = "Device.ManagementServer.ConnectionRequestURL";

    /** WebPa index for 2.4GHz Private SSID. */
    public static final String WEBPA_INDEX_2_4_GHZ_PRIVATE_SSID = "10001";

    /** WebPa index for 5GHz Private SSID. */
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

    /**
     * WebPA parameter representation for Device.WiFi.AccessPoint.10001.Security.KeyPassphrase
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2GHZ_SECURITY_KEYPASSPHRASE = "Device.WiFi.AccessPoint.10001.Security.KeyPassphrase";

    /**
     * WebPA parameter representation for Device.WiFi.AccessPoint.10101.Security.KeyPassphrase
     */
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

    /** WebPa Parameter for Adding static address table */
    public static final String WEBPA_PARAM_ADD_STATIC_ADDRESS = "Device.DHCPv4.Server.Pool.1.StaticAddress.";

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

    /**
     * WebPA Parameter to represent Number of allowed clients for 2.4 GHz Secure SSID
     */
    public static final String WEBPA_PARAM_ALLOWED_CLIENT_LIMIT_SECURE_SSID_2_4 = "Device.WiFi.AccessPoint.10005.X_CISCO_COM_BssMaxNumSta";

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

    /** webpa parameter to get the associated device count for 2.4 GHz Radio */
    public static final String WEBPA_PARAM_2_4_GHZ_ASSOCIATED_DEVICES = "Device.WiFi.AccessPoint.10001.AssociatedDeviceNumberOfEntries";

    /** webpa parameter to get the associated device count for 5 GHz Radio */
    public static final String WEBPA_PARAM_5_GHZ_ASSOCIATED_DEVICES = "Device.WiFi.AccessPoint.10101.AssociatedDeviceNumberOfEntries";

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

    /**
     * webpa parameter to validate wireless ssid enable status for Lost and Found 2.4GHz
     */
    public static final String WEBPA_WAREHOUSE_WIRELESS_SSID1_ENABLE_LNF_2_4 = "Device.WiFi.SSID.10004.Enable";

    /**
     * webpa parameter to validate wireless ssid enable status for Lost and Found 2.4GHz
     */
    public static final String WEBPA_WAREHOUSE_WIRELESS_SSID2_ENABLE_LNF_2_4 = "Device.WiFi.SSID.10006.Enable";

    /**
     * webpa parameter to validate wireless ssid enable status for Lost and Found 5GHz
     */
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

    /** WebPA Parameter for DHCPv4 lease time */
    public static final String WEBPA_PARAMETER_FOR_DHCP_LEASETIME = "Device.DHCPv4.Server.Pool.1.LeaseTime";

    /** WebPA Parameter for DNS client server enable */
    public static final String WEBPA_PARAMETER_FOR_DNS_CLIENT_SERVER = "Device.DNS.Client.Server.1.Enable";

    /** WebPA Parameter for DNS client server type */
    public static final String WEBPA_PARAMETER_FOR_DNS_CLIENT_SERVER_TYPE = "Device.DNS.Client.Server.1.Type";

    /** WebPA Parameter for selfheal max reset count */
    public static final String WEBPA_PARAMETER_FOR_SELFHEAL_MAXRESET_COUNT = "Device.SelfHeal.X_RDKCENTRAL-COM_MaxResetCount";

    /** WebPA Parameter for public 2.4Ghz ssid */
    public static final String WEBPA_PARAMETER_FOR_PRIVATE_24GHZ_SSID = "Device.WiFi.SSID.10001.SSID";
    /** WebPA Parameter for public 5Ghz ssid */
    public static final String WEBPA_PARAMETER_FOR_PRIVATE_5GHZ_SSID = "Device.WiFi.SSID.10101.SSID";
    /** WebPA Parameter for public 2.4ghz passkey */
    public static final String WEBPA_PARAMETER_FOR_PRIVATE_24GHZ_PASS = "Device.WiFi.AccessPoint.10001.Security.KeyPassphrase";
    /** WebPA Parameter for public 5ghz passkey */
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

    /**
     * This enum stores the non default values for all 2.4 and 5GHZ wifi parameters
     */
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

    /**
     * enum variable to store default values of wifi parameters in 2.4Ghz after factory reset
     */

    public enum RdkBWifiParameters {
	SECURITY_MODE_2GHZ_PRIVATE_WIFI("Device.WiFi.AccessPoint.10001.Security.ModeEnabled", "WPA2-Personal"),
	SECURITY_MODE_5GHZ_PRIVATE_WIFI("Device.WiFi.AccessPoint.10101.Security.ModeEnabled", "WPA2-Personal"),
	SECURITY_MODE_2GHZ_PRIVATE_WIFI_WPA3(
		"Device.WiFi.AccessPoint.10001.Security.ModeEnabled",
		"WPA3-Personal-Transition"),
	SECURITY_MODE_5GHZ_PRIVATE_WIFI_WPA3(
		"Device.WiFi.AccessPoint.10101.Security.ModeEnabled",
		"WPA3-Personal-Transition"),
	ENCRYPTION_MODE_2GHZ_PRIVATE_WIFI("Device.WiFi.AccessPoint.10001.Security.X_CISCO_COM_EncryptionMethod", "AES"),
	ENCRYPTION_MODE_5GHZ_PRIVATE_WIFI("Device.WiFi.AccessPoint.10101.Security.X_CISCO_COM_EncryptionMethod", "AES"),
	SSID_STATUS_2GHZ_PRIVATE_WIFI("Device.WiFi.SSID.10001.Status", "Up"),
	SSID_STATUS_5GHZ_PRIVATE_WIFI("Device.WiFi.SSID.10101.Status", "Up"),
	AUTOCHANNEL_ENABLE_STATUS_2GHZ("Device.WiFi.Radio.10000.AutoChannelEnable", "true"),
	AUTOCHANNEL_ENABLE_STATUS_5GHZ("Device.WiFi.Radio.10100.AutoChannelEnable", "true"),
	AUTOCHANNEL_ENABLE_STATUS_5GHZ_DSL("Device.WiFi.Radio.10100.AutoChannelEnable", "false"),
	OPERATING_CHANNEL_BANDWIDTH_2GHZ("Device.WiFi.Radio.10000.OperatingChannelBandwidth", "20MHz"),
	OPERATING_CHANNEL_BANDWIDTH_2GHZ_FOR_FIBRE_DEVICES(
		"Device.WiFi.Radio.10000.OperatingChannelBandwidth",
		"40MHz"),
	OPERATING_CHANNEL_BANDWIDTH_5GHZ("Device.WiFi.Radio.10100.OperatingChannelBandwidth", "80MHz"),
	OPERATING_CHANNEL_BANDWIDTH_5GHZ_FOR_SPECIFIC_DEVICES(
		"Device.WiFi.Radio.10100.OperatingChannelBandwidth",
		"40MHz"),
	SSID_ADVERTISEMENT_ENABLE_2GHZ_PRIVATE_WIFI("Device.WiFi.AccessPoint.10001.SSIDAdvertisementEnabled", "true"),
	SSID_ADVERTISEMENT_ENABLE_5GHZ_PRIVATE_WIFI("Device.WiFi.AccessPoint.10101.SSIDAdvertisementEnabled", "true"),
	GUARD_INTERVAL_2GHZ("Device.WiFi.Radio.10000.GuardInterval", "Auto"),
	GUARD_INTERVAL_5GHZ("Device.WiFi.Radio.10100.GuardInterval", "Auto"),
	SSID_ENABLE_STATUS_2GHZ_PRIVATE_WIFI("Device.WiFi.SSID.10001.Enable", "true"),
	SSID_ENABLE_STATUS_5GHZ_PRIVATE_WIFI("Device.WiFi.SSID.10101.Enable", "true"),
	OPERATING_STANDARDS_2GHZ("Device.WiFi.Radio.10000.OperatingStandards", "g,n"),
	OPERATING_STANDARDS_5GHZ("Device.WiFi.Radio.10100.OperatingStandards", "a,n,ac"),
	OPERATING_STANDARDS_2GHZ_AX("Device.WiFi.Radio.10000.OperatingStandards", "g,n,ax"),
	OPERATING_STANDARDS_5GHZ_AX("Device.WiFi.Radio.10100.OperatingStandards", "a,n,ac,ax"),
	OPERATING_STANDARDS_5GHZ_DEVICES_A_N("Device.WiFi.Radio.10100.OperatingStandards", "a,n"),
	OPERATING_STANDARDS_2GHZ_DSL("Device.WiFi.Radio.10000.OperatingStandards", "b,g,n"),
	OPERATING_STANDARDS_5GHZ_DSL("Device.WiFi.Radio.10100.OperatingStandards", "a,n,ac"),
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

    /**
     * WebPA parameter to get the secured public 2.4GHz wifi SSID enabled status
     */
    public static final String WEBPA_PARAM_2GHZ_SECURED_PUBLIC_WIFI_SSID_ENABLED = "Device.WiFi.SSID.10005.Enable";

    /** WebPA parameter to get the secured public 5GHz wifi SSID enabled status */
    public static final String WEBPA_PARAM_5GHZ_SECURED_PUBLIC_WIFI_SSID_ENABLED = "Device.WiFi.SSID.10105.Enable";

    /**
     * webpa parameter to get the value of Device.WiFi.AccessPoint.10105.Security.ModeEnabled
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5GHZ_SECURED_PUBLICWIFI = "Device.WiFi.AccessPoint.10105.Security.ModeEnabled";

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

    /**
     * WebPA Parameter for Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.PrivacyProtection.Enable
     */
    public static final String WEBPA_PARAM_PRIVACY_PROTECTION_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.PrivacyProtection.Enable";

    /**
     * WebPA Parameter for Device.DeviceInfo.X_RDKCENTRAL-COM_PrivacyProtection.Activate
     */
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

    /**
     * webpa parameter to get the value of Device.WiFi.AccessPoint.10005.Security.ModeEnabled
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4GHZ_SECURED_PUBLICWIFI = "Device.WiFi.AccessPoint.10005.Security.ModeEnabled";

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

    /** WebPA parameter to get the Possible Channels */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_POSSIBLECHANNELS = "Device.WiFi.Radio.{i}.PossibleChannels";

    /** WebPA Parameter for UpdateNvram */
    public static final String WEBPA_PARAM_SYSCFG_UPDATE_NVRAM = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.SysCfg.UpdateNvram";

    /** WebPA parameter to get the Aggressive selfheal interval */
    public static final String WEBPA_PARAM_AGGRESSIVE_SELFHEAL_INTERVAL = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.SoftwareProcessManager.SelfHeal.AggressiveInterval";

    /** tr181 parameter to check non-root support status */
    public static final String TR181_PARAM_NONROOT_SUPPORT = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.NonRootSupport.Enable";

    /**
     * WebPA Parameter for Adding IPV4 Ping Server 1
     */
    public static final String WEBPA_IPV4_PING_SERVER1 = "Device.SelfHeal.ConnectivityTest.PingServerList.IPv4PingServerTable.1.X_RDKCENTRAL-COM_Ipv4PingServerURI";

    /** WebPA Parameter for Adding IPV4 Ping Server 21 */
    public static final String WEBPA_IPV4_PING_SERVER21 = "Device.SelfHeal.ConnectivityTest.PingServerList.IPv4PingServerTable.21.X_RDKCENTRAL-COM_Ipv4PingServerURI";

    /** WebPA parameter for enabling Manageable notification feature */
    public static final String WEBPA_PARAMETER_MANAGEABLE_NOTIFICATION_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.ManageableNotification.Enable";

    /** WebPA parameter to get time offset value */
    public static final String WEBPA_PARAM_TIME_OFFSET = "Device.Time.TimeOffset";

    /** WebPA parameter for Downstream channel value */
    public static final String WEBPA_PARAM_DOWNSTREAM_CHANNEL = "Device.X_CISCO_COM_CableModem.DownstreamChannel.";

    /** WebPA parameter Downstream channel frequency value */
    public static final String WEBPA_PARAM_DOWNSTREAM_CHANNEL_FREQUENCY = "Device.X_CISCO_COM_CableModem.DownstreamChannel.{i}.Frequency";

    /** WebPA parameter Downstream channel SNRLevel value */
    public static final String WEBPA_PARAM_DOWNSTREAM_CHANNEL_SNRLEVEL = "Device.X_CISCO_COM_CableModem.DownstreamChannel.{i}.SNRLevel";

    /** WebPA parameter Upstream channel PowerLevel value */
    public static final String WEBPA_PARAM_UPSTREAM_CHANNEL_POWERLEVEL = "Device.X_CISCO_COM_CableModem.UpstreamChannel.{i}.PowerLevel";

    /** Webpa param for boot file */
    public static final String WEBPA_PARAM_BOOT_FILE = "Device.X_CISCO_COM_CableModem.DOCSISConfigFileName";

    /** WebPA parameter Downstream channel Modulation value */
    public static final String WEBPA_PARAM_DOWNSTREAM_CHANNEL_MODULATION = "Device.X_CISCO_COM_CableModem.DownstreamChannel.{i}.Modulation";

    /** WebPA Parameter for diagnostic mode */
    public static final String WEBPA_PARAM_DIAGNOSTIC_MODE = "Device.SelfHeal.X_RDKCENTRAL-COM_DiagnosticMode";

    /** WebPA Parameter for diagnostic mode logupload frequency */
    public static final String WEBPA_PARAM_DIAG_MODE_LOG_UPLOAD_FREQUENCY = "Device.SelfHeal.X_RDKCENTRAL-COM_DiagMode_LogUploadFrequency";

    /** WebPa Parameter for periodic firmware upgrade check enable */
    public static final String WEBPA_PARAM_PERIODIC_FW_CHECK_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.PeriodicFWCheck.Enable";

    /** WebPA parameter to get value of firmware download started notification */
    public static final String WEBPA_PARAMETER_FIRMWARE_DOWNLOAD_STARTED_NOTIFICATION = "Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.RPC.FirmwareDownloadStartedNotification";

    /** WebPA parameter to get value of firmware download completed notification */
    public static final String WEBPA_PARAMETER_FIRMWARE_DOWNLOAD_COMPLETED_NOTIFICATION = "Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.RPC.FirmwareDownloadCompletedNotification";

    /** webpa parameter to enable/disable CPUPROCANALYZER */
    public static final String WEBPA_PARAM_FOR_CPU_PROC_ANALYZER = "Device.SelfHeal.X_RDK_CPUProcAnalyzer_Enable";

    /** Webpa param for DNS strict order enable */
    public static final String WEBPA_PARAM_ENABLE_DISABLE_STRICT_DNS_ORDER = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.DNSStrictOrder.Enable";
    /** WebPa Parameter to abort reboot */
    public static final String WEBPA_PARAM_ABORT_REBOOT = "Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.RPC.AbortReboot";

    /** WebPa parameter to reboot the device */
    public static final String WEBPA_PARAM_REBOOT_DEVICE = "Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.RPC.RebootDevice";

    /** Weba parameter to enable rbus */
    public static final String WEBPA_PARAM_FOR_RBUS_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.RBUS.Enable";

    /** WebPA parameter for checking service agent */
    public static final String WEBPA_PARAM_SERVICEAGENT = "Device.X_RDKCENTRAL_COM_ServiceAgent.Services.";

    /** WebPA parameter to get value of Device manageable notification time */
    public static final String WEBPA_PARAMETER_DEVICE_MANAGEABLE_NOTIFICATION = "Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.RPC.DeviceManageableNotification";

    /** WebPA parameter to enable/disable Ethwan Mode */
    public static final String WEBPA_PARAM_DEVICE_ETHWAN_MODE_ENABLE = "Device.Ethernet.X_RDKCENTRAL-COM_WAN.Enabled";

    /** WebPA parameter for Wan Link Heal Enable */
    public static final String WEBPA_PARAM_WAN_LINK_HEAL_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.WANLinkHeal.Enable";

    /** WebPA Parameter for WAN IPv4 */
    public static final String WEBPA_PARAM_WAN_IPV4 = "Device.DeviceInfo.X_COMCAST-COM_WAN_IP";

    /**
     * WebPA parameter to enable/disable SSIDAdvertisementEnabled of Public Wifi(open) 2.4 GHz Network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PUBLIC_SSID_ADVERTISE_ENABLED = "Device.WiFi.AccessPoint.10103.SSIDAdvertisementEnabled";

    /** WebPa Parameter for Enabling Moca */
    public static final String WEBPA_PARAM_ENABLE_MOCA = "Device.MoCA.Interface.1.Enable";

    /** WebPA parameter to enable Device Management Server */
    public static final String WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_ENABLECWMP = "Device.ManagementServer.EnableCWMP";

    /** WebPA Parameter for Wifi 2.4GHz Operating Standard */
    public static final String WEBPA_PARAM_2_4_OPERATING_STD = "Device.WiFi.Radio.10000.OperatingStandards";

    /** WebPA Parameter for Wifi 2.4GHz Operational Tx Rates */
    public static final String WEBPA_PARAM_2_4_OPERATIONAL_TX_RATES = "Device.WiFi.Radio.10000.OperationalDataTransmitRates";

    /** WebPA Parameter for Wifi 2.4GHz Supported Tx Rates */
    public static final String WEBPA_PARAM_2_4_SUPPORTED_DATA_TX_RATES = "Device.WiFi.Radio.10000.SupportedDataTransmitRates";

    /** WebPA Parameter for Wifi 2.4GHz Basic Tx Rates */
    public static final String WEBPA_PARAM_2_4_BASIC_TX_RATES = "Device.WiFi.Radio.10000.BasicDataTransmitRates";

    /** WebPA Parameter for Wifi 5GHz Operating Standard */
    public static final String WEBPA_PARAM_5_OPERATING_STD = "Device.WiFi.Radio.10100.OperatingStandards";

    /** WebPA Parameter for Wifi 5GHz Operational Tx Rates */
    public static final String WEBPA_PARAM_5_OPERATIONAL_TX_RATES = "Device.WiFi.Radio.10100.OperationalDataTransmitRates";

    /** WebPA Parameter for Wifi 5GHz Basic Tx Rates */
    public static final String WEBPA_PARAM_5_BASIC_TX_RATES = "Device.WiFi.Radio.10100.BasicDataTransmitRates";

    /** WebPA Parameter for Wifi 2.4GHz Beacon Rate */
    public static final String WEBPA_PARAM_2_4_BEACON_RATE = "Device.WiFi.AccessPoint.10001.X_RDKCENTRAL-COM_BeaconRate";

    /** WebPA Parameter for Wifi 5GHz supported data Tx Rates */
    public static final String WEBPA_PARAM_5_SUPPORTED_DATA_TRANSMIT_TX_RATES = "Device.WiFi.Radio.10100.SupportedDataTransmitRates";

    /**
     * WebPA Parameter for Wifi 2.4GHZ or 5GHz for BasicDataTransmitRates DSL device
     */
    public static final String WEBPA_PARAM_2_OR_5_DSL_DEVICE_BASIC_DATA_TRANSMIT_RATES = "Device.WiFi.Radio.{i}.BasicDataTransmitRates";

    /** DMCLI PARAMTER to fectch wireless mode */
    public static final String DMCLI_COMMAND_WIRELESS_MODE_5GHZ = "Device.WiFi.Radio.2.OperatingStandards";

    /** WebPA Parameter for Wifi 2.4GHZ BEACON Rate for DSL device */
    public static final String WEBPA_PARAM_BEACON_DSL_DEVICE_OPERATING_STD = "Device.WiFi.AccessPoint.{i}.X_RDKCENTRAL-COM_BeaconRate";

    /** WebPA parameter to get the wifi params */
    public static final String WEBPA_PARAM_DEVICE_WIFI_PARAMS = "Device.WiFi.";

    /** WebPA parameter to set Device Current Operational Mode */
    public static final String WEBPA_PARAM_DEVICE_SELECTED_OPERATIONAL_MODE = "Device.X_RDKCENTRAL-COM_EthernetWAN.SelectedOperationalMode";

    /** WebPA parameter to get Device Operational Mode */
    public static final String WEBPA_PARAM_DEVICE_OPERATIONAL_MODE = "Device.X_RDKCENTRAL-COM_EthernetWAN.CurrentOperationalMode";

    /** WebPA parameter to get the static WAN IP of the device */
    public static final String WEBPA_PARAM_STATIC_WAN_IP = "Device.X_CISCO_COM_TrueStaticIP.IPAddress";

    /** WebPA Parameter to configure LAN IP Address to DMZ */
    public static final String WEBPA_PARAM_TO_CONFIGURE_LAN_IP_ADDRESS_TO_DMZ = "Device.NAT.X_CISCO_COM_DMZ.InternalIP";

    /** WebPA Parameter for Device Control Erouter Enable */
    public static final String WEBPA_PARAMETER_FOR_DEVICE_CONTROL_EROUTER_ENABLE = "Device.X_CISCO_COM_DeviceControl.ErouterEnable";

    /** WebPA Parameter to enable/disable port forwarding */
    public static final String WEBPA_PARAM_TO_ENABLE_AND_DISABLE_PORT_FORWARDING = "Device.NAT.X_Comcast_com_EnablePortMapping";

    /** WebPA parameter for Device ManagementServer Connection Port */
    public static final String WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_CONNECTION_PORT = "Device.ManagementServer.X_CISCO_COM_ConnectionRequestURLPort";

    /**
     * WebPA Parameters constant to get generic parameters residential device
     * <ol>
     * <li>1.Get the value for DMZ LAN IP Address</li>
     * <li>2.Get the value for Device cloud UI status</li>
     * <li>3.Get the value for device control router enable status</li>
     * <li>4.Get the value for port forwarding status</li>
     * </ol>
     */
    public static final String[] WEBPA_PARAMS_FOR_COMMON_RESI_DEVICE = { WEBPA_PARAM_TO_CONFIGURE_LAN_IP_ADDRESS_TO_DMZ,
	    WEBPA_PARAM_DEVICE_CLOUD_UI, WEBPA_PARAMETER_FOR_DEVICE_CONTROL_EROUTER_ENABLE,
	    WEBPA_PARAM_TO_ENABLE_AND_DISABLE_PORT_FORWARDING };

    /** WebPA Parameter for DFS Enable */
    public static final String WEBPA_PARAM_DFS_ENABLE = "Device.WiFi.Radio.10100.X_COMCAST_COM_DFSEnable";

    /** WebPA Parameter for 2.4 GHz DFS Enable */
    public static final String WEBPA_PARAMETER_FOR_2GHZ_RADIO_DFS_ENABLE = "Device.WiFi.Radio.10000.X_COMCAST_COM_DFSEnable";

    /** Webpa Param for channel extension 2.4Ghz wifi radio */
    public static final String WEBPA_PARAM_FOR_24GHZ_EXTENSION_CHANNEL = "Device.WiFi.Radio.10000.ExtensionChannel";
    /** Webpa Param for channel extension 5 GHz wifi radio */
    public static final String WEBPA_PARAM_FOR_5GHZ_EXTENSION_CHANNEL = "Device.WiFi.Radio.10100.ExtensionChannel";

    /** WebPA Parameter for 2.4 GHz beancon interval */
    public static final String WEBPA_PARAMETER_FOR_2GHZ_BEACON_INTERVAL = "Device.WiFi.Radio.10000.X_COMCAST-COM_BeaconInterval";

    /** WebPA Parameter for 2.4 GHz radio basic rate */
    public static final String WEBPA_PARAMETER_FOR_2GHZ_RADIO_BASIC_RATE = "Device.WiFi.Radio.10000.X_CISCO_COM_BasicRate";

    /** WebPA Parameter for 5 GHz radio basic rate */
    public static final String WEBPA_PARAMETER_FOR_5GHZ_RADIO_BASIC_RATE = "Device.WiFi.Radio.10100.X_CISCO_COM_BasicRate";

    /** WebPA Parameter for 2.4 GHz radio Transmit Power */
    public static final String WEBPA_PARAMETER_FOR_2GHZ_RADIO_TRANSMIT_POWER = "Device.WiFi.Radio.10000.TransmitPower";

    /** WebPA Parameter for 5 GHz radio TransmitPower */
    public static final String WEBPA_PARAMETER_FOR_5GHZ_RADIO_TRANSMIT_POWER = "Device.WiFi.Radio.10100.TransmitPower";

    /** WebPA Parameter to get WiFi Radio.10100 Operating standard */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_5GHZ_OPERATING_STANDARD = "Device.WiFi.Radio.10100.OperatingStandards";

    /** WebPA Parameter to get WiFi Radio.10000 Operating standard */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_2_4_GHZ_OPERATING_STANDARD = "Device.WiFi.Radio.10000.OperatingStandards";

    /** WebPA parameter to set force wifi radio disable */
    public static final String WEBPA_PARAM_FORCE_WIFI_DISABLE = "Device.WiFi.X_RDK-CENTRAL_COM_ForceDisable";

    /** WebPA Parameter for dhcp IPv4 client 1 IP routers */
    public static final String WEBPA_PARAMETER_FOR_DHCPV4_CLIENT_IP_ROUTERS = "Device.DHCPv4.Client.1.IPRouters";

    /** WebPA Parameter for rabid dns cache size */
    public static final String WEBPA_PARAM_RABID_DNS_CACHE_SIZE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.RabidFramework.DNSCacheSize";

    /** tr181 parameter to enable cred download */
    public static final String TR181_PARAM_CRED_DWNLD_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.CredDwnld.Enable";

    /** tr181 parameter to enable cred use */
    public static final String TR181_PARAM_CRED_USE_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.CredDwnld.Use";

    /** webpa parameter to Enable Wifi blaster from RFC */
    public static final String WEBPA_PARAM_FOR_RFC_WIFI_BLASTER_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.WifiClient.ActiveMeasurements.Enable";

    /** webpa parameter to Wifi blaster trigger */
    public static final String WEBPA_PARAM_FOR_WIFI_BLASTER_TRIGGER = "Device.WiFi.X_RDKCENTRAL-COM_Report.WifiClient.ActiveMeasurements.Enable";

    /** webpa parameter to Enable Wifi blaster packet Size */
    public static final String WEBPA_PARAM_FOR_BLASTER_PACKETSIZE = "Device.WiFi.X_RDKCENTRAL-COM_Report.WifiClient.ActiveMeasurements.PacketSize";

    /** webpa parameter to Enable Wifi blaster sample duration */
    public static final String WEBPA_PARAM_FOR_BLASTER_SAMPLEDURATION = "Device.WiFi.X_RDKCENTRAL-COM_Report.WifiClient.ActiveMeasurements.SampleDuration";

    /** webpa parameter to Enable Wifi blaster Number of samples */
    public static final String WEBPA_PARAM_FOR_BLASTER_NUMOFSAMPLES = "Device.WiFi.X_RDKCENTRAL-COM_Report.WifiClient.ActiveMeasurements.NumberOfSamples";

    /** webpa parameter to Enable Wifi blaster Plan.PlanID */
    public static final String WEBPA_PARAM_FOR_BLASTER_PLANID = "Device.WiFi.X_RDKCENTRAL-COM_Report.WifiClient.ActiveMeasurements.Plan.PlanID";

    /** webpa parameter to Enable Wifi blaster stepID */
    public static final String WEBPA_PARAM_FOR_BLASTER_STEPID = "Device.WiFi.X_RDKCENTRAL-COM_Report.WifiClient.ActiveMeasurements.Plan.Step.{i}.StepID";

    /** webpa parameter to Enable Wifi blaster SourceMac */
    public static final String WEBPA_PARAM_FOR_BLASTER_SOURCEMAC = "Device.WiFi.X_RDKCENTRAL-COM_Report.WifiClient.ActiveMeasurements.Plan.Step.{i}.SourceMac";

    /** webpa parameter to Enable Wifi blaster DestMac */
    public static final String WEBPA_PARAM_FOR_BLASTER_DESTMAC = "Device.WiFi.X_RDKCENTRAL-COM_Report.WifiClient.ActiveMeasurements.Plan.Step.{i}.DestMac";

    /** webpa parameter to Enable Wifi blaster Number Of Entries */
    public static final String WEBPA_PARAM_FOR_BLASTER_NUMOFENTRIES = "Device.WiFi.X_RDKCENTRAL-COM_Report.WifiClient.ActiveMeasurements.Plan.StepNumberOfEntries";

    /** webpa parameter to Enable TR181 RDKFirwareupgrader */
    public static final String WEBPA_PARAM_RDK_FIRMWARE_UPGRADER = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.RDKFirmwareUpgrader.Enable";

    /**
     * WebPA Parameters constant to get the current value for 2.4 & 5 GHz Radios
     * 
     * <ol>
     * <li>1.Get the value for 2.4 GHz radio extension channel</li>
     * <li>2.Get the value for 2.4 GHz radio beacon interval</li>
     * <li>3.Get the value for 2.4 GHz radio basic rate</li>
     * <li>4.Get the value for 2.4 GHz radio operating standard</li>
     * <li>5.Get the value for 2.4 GHz radio transmit power</li>
     * <li>6.Get the value for 2.4 GHz radio status</li>
     * <li>7.Get the value for 2.4 GHz radio channel</li>
     * <li>8.Get the value for 2.4 GHz radio wireless channel</li>
     * <li>9.Get the value for 2.4 GHz radio operating channel bandwidth</li>
     * <li>10.Get the value for 2.4 GHz radio DFS enable</li>
     * <li>11.Get the value for 5 GHz radio extension channel</li>
     * <li>12.Get the value for 5 GHz radio beacon interval</li>
     * <li>13.Get the value for 5 GHz radio basic rate</li>
     * <li>14.Get the value for 5 GHz radio operating standard</li>
     * <li>15.Get the value for 5 GHz radio transmit power</li>
     * <li>16.Get the value for 5 GHz radio status</li>
     * <li>17.Get the value for 5 GHz radio channel</li>
     * <li>18.Get the value for 5 GHz radio wireless channel</li>
     * <li>19.Get the value for 5 GHz radio operating channel bandwidth</li>
     * <li>20.Get the value for 5 GHz radio DFS enable</li>
     * </ol>
     */
    public static final String[] WEBPA_PARAMS_FOR_BOTH_RADIOS = { WEBPA_PARAM_FOR_24GHZ_EXTENSION_CHANNEL,
	    WEBPA_PARAMETER_FOR_2GHZ_BEACON_INTERVAL, WEBPA_PARAMETER_FOR_2GHZ_RADIO_BASIC_RATE,
	    WEBPA_PARAM_DEVICE_WIFI_RADIO_2_4_GHZ_OPERATING_STANDARD, WEBPA_PARAMETER_FOR_2GHZ_RADIO_TRANSMIT_POWER,
	    WEBPA_PARAM_WIFI_2_4_RADIO_ENABLE, WEBPA_WAREHOUSE_WIFI_2G_CHANNEL,
	    WEBPA_WAREHOUSE_WIFI_2G_WIRELESS_CHANNEL, WEBPA_PARAM_FOR_OPERATING_BANDWIDTH_IN_2GHZ_BAND,
	    WEBPA_PARAMETER_FOR_2GHZ_RADIO_DFS_ENABLE, WEBPA_PARAM_FOR_5GHZ_EXTENSION_CHANNEL,
	    WEBPA_PARAMETER_FOR_BEACON_INTERVAL, WEBPA_PARAMETER_FOR_5GHZ_RADIO_BASIC_RATE,
	    WEBPA_PARAM_DEVICE_WIFI_RADIO_5GHZ_OPERATING_STANDARD, WEBPA_PARAMETER_FOR_5GHZ_RADIO_TRANSMIT_POWER,
	    WEBPA_PARAM_WIFI_5_RADIO_ENABLE, WEBPA_WAREHOUSE_WIFI_5G_CHANNEL, WEBPA_WAREHOUSE_WIFI_5G_WIRELESS_CHANNEL,
	    WEBPA_PARAM_FOR_OPERATING_BANDWIDTH_IN_5GHZ_BAND, WEBPA_PARAM_DFS_ENABLE };

    /** WebPA Parameter to get/set the interface enable status */
    public static final String WEBPA_PARAM_INTERFACE_ENABLE = "Device.IP.Interface.1.Enable";

    /**
     * WebPA Parameters constant to get generic parameters business class devices
     * <ol>
     * <li>1.Get the value for DMZ LAN IP Address</li>
     * <li>2.Get the value for device control router enable status</li>
     * <li>3.Get the value for port forwarding status</li>
     * </ol>
     */
    public static final String[] WEBPA_PARAMS_FOR_COMMON_BUSI_DEVICE = { WEBPA_PARAM_TO_CONFIGURE_LAN_IP_ADDRESS_TO_DMZ,
	    WEBPA_PARAMETER_FOR_DEVICE_CONTROL_EROUTER_ENABLE, WEBPA_PARAM_TO_ENABLE_AND_DISABLE_PORT_FORWARDING };

    /**
     * WebPA Parameters constant to get WAN IP Address and Client IP Routers
     * <ol>
     * <li>1.Get the value for WAN IPv4 Address</li>
     * <li>2.Get the value for DHCP v4 client IP routers</li>
     * </ol>
     */
    public static final String[] WEB_PARAM_FOR_WAN_CLIENT_IP = { WEBPA_PARAM_WAN_IPV4,
	    WEBPA_PARAMETER_FOR_DHCPV4_CLIENT_IP_ROUTERS };

    /** WebPA Parameter for erouter reset */
    public static final String WEBPA_PARAM_DEVICE_EROUTER_RESET = "Device.X_CISCO_COM_DeviceControl.RebootDevice";

    /** WebPA Parameter for DNS ping test enable */
    public static final String WEBPA_PARAM_DNS_PING_TEST_ENABLE = "Device.SelfHeal.X_RDKCENTRAL-COM_DNS_PINGTEST_Enable";

    /** WebPA Parameter for DNS ping test enable */
    public static final String WEBPA_PARAM_DNS_PING_TEST_URL = "Device.SelfHeal.X_RDKCENTRAL-COM_DNS_URL";

    /** WebPA Parameter to block ICMP IPV4 Traffic */
    public static final String WEBPA_PARAM_TO_BLOCK_ICMP_FOR_IPV4_TRAFFIC_UNDER_CUSTOM_FIREWALL = "Device.X_CISCO_COM_Security.Firewall.FilterAnonymousInternetRequests";

    /** Webpa parameter for telemetryEndpoint Enable */
    public static final String WEBPA_PARAM_WIFI_TELEMETRY_ENDPOINT_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.TelemetryEndpoint.Enable";

    /**
     * WebPa parameter for Device.DeviceInfo.X_RDKCENTRAL-COM_xBlueTooth.LimitBeaconDetection
     */
    public static final String WEBPA_PARAM_LIMIT_BEACON_DETECTION = "Device.DeviceInfo.X_RDKCENTRAL-COM_xBlueTooth.LimitBeaconDetection";

    /** WebPA Parameter for Rabid framework memory limit */
    public static final String WEBPA_PARAM_RABID_FW_MEMORY_LIMIT = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.RabidFramework.MemoryLimit";

    /** WebPA parameter to enable selfheal in the device */
    public static final String WEBPA_PARAM_DEFAULT_LANIP_FOR_SYNDICATION = "Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.DefaultAdminIP";

    /** WebPa Parameter for admin page url */
    public static final String WEBPA_PARAM_LAN_IP_ADDRESS = "Device.X_CISCO_COM_DeviceControl.LanManagementEntry.1.LanIPAddress";

    /** WebPA parameter for controlling TR69 process running */
    public static final String WEBPA_PARAM_TR69_SUPPORT_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.TR069support.Enable";

    /** WebPA Parameter for TR-181 parameter for Firewall hole access */
    public static final String WEBPA_PARAM_ALLOW_FIREWALL_HOLE_ALLOW_PORTS = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.AllowOpenPorts.Enable";

    /** WebPA parameter for Device ManagementServer Password */
    public static final String WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_PASSWORD = "Device.ManagementServer.Password";

    /** Webpa Param for Device cert location */
    public static final String WEBPA_PARAM_DEVICE_PARTNER_CERT_LOCATION = "Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.TR69CertLocation";

    /** WebPA parameter for Device ManagementServer URL */
    public static final String WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_URL = "Device.ManagementServer.URL";

    /** WebPA Parameter for Enabling WiFiClient */
    public static final String WEBPA_PARAM_WIFICLIENT_ENABLE = "Device.WiFi.X_RDKCENTRAL-COM_Report.WifiClient.Enabled";

    /** WebPA Parameter for WiFiClient Reporting Period */
    public static final String WEBPA_PARAM_WIFICLIENT_REPORTING_PERIOD = "Device.WiFi.X_RDKCENTRAL-COM_Report.WifiClient.ReportingPeriod";

    /** WebPA Parameter for WiFiClient MAC Address */
    public static final String WEBPA_PARAM_WIFICLIENT_MAC_ADDRESS = "Device.WiFi.X_RDKCENTRAL-COM_Report.WifiClient.MacAddress";

    /** WebPA Parameter for WiFiClient Default Reporting Period */
    public static final String WEBPA_PARAM_WIFICLIENT_DEFAULT_REPORTING_PERIOD = "Device.WiFi.X_RDKCENTRAL-COM_Report.WifiClient.Default.ReportingPeriod";

    /** WebPA Parameter for WiFiClient Schema */
    public static final String WEBPA_PARAM_WIFICLIENT_SCHEMA = "Device.WiFi.X_RDKCENTRAL-COM_Report.WifiClient.Schema";

    /** WebPA parameter representation for Device.Time.Enable */
    public static final String WEBPA_PARAM_DEVICE_TIME_ENABLE = "Device.Time.Enable";

    /** WebPA parameter representation for Device.Time.NTPServer1 */
    public static final String WEBPA_PARAM_DEVICE_TIME_NTPServer1 = "Device.Time.NTPServer1";

    /** WebPa Parameter for Adding Device Name */
    public static final String WEBPA_PARAM_ADD_DEVICE_NAME = "X_CISCO_COM_DeviceName";

    /** WebPa Parameter for Adding Wifi Mac address In Add Device Page */
    public static final String WEBPA_PARAM_ADDING_WIFI_MAC_ADDRESS = "Chaddr";

    /** WebPa Parameter for Adding Reserved IP address In Add Device Page */
    public static final String WEBPA_PARAM_ADDING_RESERVED_IP_ADDRESS = "Yiaddr";

    /** WebPa Parameter for Adding Comments */
    public static final String WEBPA_PARAM_COMMENT = "X_CISCO_COM_Comment";

    /**
     * WebPA Parameter to represent Number of allowed clients for 5 GHz Open SSID
     */
    public static final String WEBPA_PARAM_ALLOWED_CLIENT_LIMIT_OPEN_SSID_5 = "Device.WiFi.AccessPoint.10103.X_CISCO_COM_BssMaxNumSta";

    /**
     * WebPA Parameter to represent Number of allowed clients for 2.4 GHz Open SSID
     */
    public static final String WEBPA_PARAM_ALLOWED_CLIENT_LIMIT_OPEN_SSID_2_4 = "Device.WiFi.AccessPoint.10003.X_CISCO_COM_BssMaxNumSta";

    /** WebPA Parameter to get Passphrase of 2.4 GHZ LNF Network */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_10004_SECURITY_KEYPASSPHRASE = "Device.WiFi.AccessPoint.10004.Security.KeyPassphrase";

    /** WebPA Parameter to get Passphrase of 5 GHz LNF Network */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_10104_SECURITY_KEYPASSPHRASE = "Device.WiFi.AccessPoint.10104.Security.KeyPassphrase";

    /** webpa parameter for public wifi ssid key pharsepass for 2.4/5 GHz */
    public static final String WEBPA_PARAM_FOR_WIFI_PUBLIC_SSID_PASSPHRASE = "Device.WiFi.AccessPoint.<REPLACE>.Security.X_COMCAST-COM_KeyPassphrase";

    /**
     * webpa parameter to get enable/disable mac filter for different accessPoints
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_MACFILTER_ENABLE = "Device.WiFi.AccessPoint.{i}.X_CISCO_COM_MACFilter.Enable";

    /** WebPA Parameter to get SSID of 5G Private Network */
    public static final String WEBPA_PARAM_Device_WiFi_SSID_10101_SSID = "Device.WiFi.SSID.10101.SSID";

    /** WebPA parameter for Deviceinfo Manufacturer OUI */
    public static final String WEBPA_PARAM_DEVICE_INFO_MANUFACTURER_OUI = "Device.DeviceInfo.ManufacturerOUI";

    /** WebPA parameter for Deviceinfo Product Class */
    public static final String WEBPA_PARAM_DEVICE_DEVICE_INFO_PRODUCT_CLASS = "Device.DeviceInfo.ProductClass";

    /** WebPA parameter for Device ManagementServer Username */
    public static final String WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_USERNAME = "Device.ManagementServer.Username";

    /** WebPA parameter for Device ManagementServer Connection Request Username */
    public static final String WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_CONNECTIONREQUESTUSERNAME = "Device.ManagementServer.ConnectionRequestUsername";

    /** WebPA parameter to get the primary dns address */
    public static final String WEBPA_PARAM_PRIMARY_DNS = "Device.DNS.Client.Server.1.DNSServer";

    /** WebPA parameter to get the primary and secondary dns ip address */
    public static final String WEBPA_PARAM_PRIMARY_AND_SECONDARY_DNS_IP = "Device.DNS.Client.Server.<REPLACE>.DNSServer";

    /** WebPA parameter to switch to UDHCPC */
    public static final String WEBPA_PARAM_SWITCH_TO_UDHCPC = "Device.DeviceInfo.X_RDKCENTRAL-COM_SwitchToUDHCPC.Enable";

    /** WebPA Parameter for auto reboot enable */
    public static final String WEBPA_PARAM_RFC_FEATURE_AUTOREBOOT_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.AutoReboot.Enable";

    /** WebPA Parameter for auto reboot uptime value */
    public static final String WEBPA_PARAM_RFC_FEATURE_AUTOREBOOT_UPTIME = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.AutoReboot.UpTime";

    /** WebPA Parameter to block HTTP for IPV4 Traffic */
    public static final String WEBPA_PARAM_TO_BLOCK_HTTP_FOR_IPV4_TRAFFIC_UNDER_CUSTOM_FIREWALL = "Device.X_CISCO_COM_Security.Firewall.FilterHTTP";

    /** WebPA Parameter to block HTTPS for IPV4 Traffic */
    public static final String WEBPA_PARAM_TO_BLOCK_HTTPS_FOR_IPV4_TRAFFIC_UNDER_CUSTOM_FIREWALL = "Device.X_CISCO_COM_Security.Firewall.FilterHTTPs";

    /** WebPA Parameter to block HTTP for IPV6 Traffic */
    public static final String WEBPA_PARAM_TO_BLOCK_HTTP_FOR_IPV6_TRAFFIC_UNDER_CUSTOM_FIREWALL = "Device.X_CISCO_COM_Security.Firewall.FilterHTTPV6";

    /** WebPA Parameter to block HTTPS for IPV6 Traffic */
    public static final String WEBPA_PARAM_TO_BLOCK_HTTPS_FOR_IPV6_TRAFFIC_UNDER_CUSTOM_FIREWALL = "Device.X_CISCO_COM_Security.Firewall.FilterHTTPsV6";

    /**
     * WebPA Parameter for getting Device Wifi Accesspoint MacFilter Table in 5GHZ network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_MAC_FILTER_TABLE = "Device.WiFi.AccessPoint.10101.X_CISCO_COM_MacFilterTable.";

    /**
     * WebPA Parameter for Device Wifi Accesspoint MacFilter Enable in 5GHZ network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_MAC_FILTER_ENABLE = "Device.WiFi.AccessPoint.10101.X_CISCO_COM_MACFilter.Enable";

    /**
     * WebPA Parameter for getting Device Wifi Accesspoint MacFilter Black List in 5GHZ network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_MAC_FILTER_BLACK_LIST = "Device.WiFi.AccessPoint.10101.X_CISCO_COM_MACFilter.FilterAsBlackList";

    /**
     * WebPA Parameter for getting Device Wifi Accesspoint MacFilter mode in 5GHZ network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_MAC_FILTER_MODE = "Device.WiFi.AccessPoint.10101.X_COMCAST-COM_MAC_FilteringMode";

    /** Webpa parameter for telemetry log interval */
    public static final String WEBPA_PARAM_WIFI_TELEMETRY_LOG_INTERVAL = "Device.DeviceInfo.X_RDKCENTRAL-COM_WIFI_TELEMETRY.LogInterval";

    /** webpa parameter for Rapid Reconnect Indication Enable */
    public static final String WEBPA_PARAM_RAPID_RECONNECT_INDICATION = "Device.WiFi.X_RDKCENTRAL-COM_RapidReconnectIndicationEnable";

    /** webpa parameter for 2.4GHz rapid Reconnect Count Enable */
    public static final String WEBPA_PARAM_2_4_RAPID_COUNT_ENABLE = "Device.WiFi.AccessPoint.10001.X_RDKCENTRAL-COM_rapidReconnectCountEnable";

    /** webpa parameter for 5GHz rapid Reconnect Count Enable */
    public static final String WEBPA_PARAM_5_RAPID_COUNT_ENABLE = "Device.WiFi.AccessPoint.10101.X_RDKCENTRAL-COM_rapidReconnectCountEnable";

    /** webpa parameter for 2.4GHz rapid Reconnect Max Time */
    public static final String WEBPA_PARAM_2_4_RECONNECT_MAXTIME = "Device.WiFi.AccessPoint.10001.X_RDKCENTRAL-COM_rapidReconnectMaxTime";

    /** webpa parameter for 5GHz rapid Reconnect Max Time */
    public static final String WEBPA_PARAM_5_RECONNECT_MAXTIME = "Device.WiFi.AccessPoint.10101.X_RDKCENTRAL-COM_rapidReconnectMaxTime";
    /** String value 300 */
    public static final String STRING_300 = "300";

    /** WebPa Parameter for LAN IPv4 page subnet mask */
    public static final String WEBPA_PARAM_LAN_IPv4_SUBNET_MASK = "Device.X_CISCO_COM_DeviceControl.LanManagementEntry.1.LanSubnetMask";

    /**
     * Webpa parameters array to get primary and secondary dns IPv4 & IPv6 address
     */
    public static final String[] WEBPA_PARAMETER_ARRAY_TO_GET_DNS_IP_ADDRESS = { WEBPA_PARAM_PRIMARY_DNS,
	    WEBPA_PARAM_PRIMARY_AND_SECONDARY_DNS_IP.replace(BroadBandTestConstants.STRING_REPLACE,
		    BroadBandTestConstants.STRING_VALUE_TWO),
	    WEBPA_PARAM_PRIMARY_AND_SECONDARY_DNS_IP.replace(BroadBandTestConstants.STRING_REPLACE,
		    BroadBandTestConstants.STRING_VALUE_THREE),
	    WEBPA_PARAM_PRIMARY_AND_SECONDARY_DNS_IP.replace(BroadBandTestConstants.STRING_REPLACE,
		    BroadBandTestConstants.STRING_VALUE_FOUR) };

    public enum RdkBBandSteeringParameters {
	DEFAULT_STEERING_ENABLE(WEBPA_PARAM_BAND_STEERING_ENABLE, "false"),
	DEFAULT_STEERING_IDLE_INACTIVE_TIME_2GHZ(WEBPA_PARAM_BAND_STEERING_IDLE_INACTIVE_TIME_2_4GHZ, "10"),
	DEFAULT_STEERING_IDLE_INACTIVE_TIME_5GHZ(WEBPA_PARAM_BAND_STEERING_IDLE_INACTIVE_TIME_5GHZ, "10"),
	DEFAULT_STEERING_OVERLOAD_INACTIVE_TIME_2GHZ(WEBPA_PARAM_BAND_STEERING_OVERLOAD_INACTIVE_TIME_2_4GHZ, "10"),
	DEFAULT_STEERING_OVERLOAD_INACTIVE_TIME_5GHZ(WEBPA_PARAM_BAND_STEERING_OVERLOAD_INACTIVE_TIME_5GHZ, "10"),
	DEFAULT_STEERING_APGROUP(WEBPA_PARAM_BAND_STEERING_APGROUP, "1,2");

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

    /** WebPa parameter for Device.Thermal.Fan.Status */
    public static final String WEBPA_PARAM_DEVICE_THERMAL_FAN_STATUS = "Device.Thermal.Fan.{i}.Status";

    /** WebPa parameter for Device.Thermal.Fan.Speed */
    public static final String WEBPA_PARAM_DEVICE_THERMAL_FAN_SPEED = "Device.Thermal.Fan.{i}.Speed";

    /** WebPa parameter for Device.Thermal.Fan.RotorLock */
    public static final String WEBPA_PARAM_DEVICE_THERMAL_FAN_ROTORLOCK = "Device.Thermal.Fan.{i}.RotorLock";

    /** WebPa parameter for Device.Thermal.Fan.MaxOverride */
    public static final String WEBPA_PARAM_DEVICE_THERMAL_FAN_MAXOVERRIDE = "Device.Thermal.Fan.{i}.MaxOverride";

    /** WebPa parameter to get no of fans configured */
    public static final String WEBPA_PARAM_DEVICE_THERMAL_FAN_NO_ENTRIES = "Device.Thermal.FanNumberOfEntries";

    /**
     * WebPA Parameter for getting Device Wifi Accesspoint MacFilter Table in 2.4GHZ network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_MAC_FILTER_TABLE = "Device.WiFi.AccessPoint.10001.X_CISCO_COM_MacFilterTable.";

    /**
     * WebPA Parameter for Device Wifi Accesspoint MacFilter Enable in 2.4GHZ network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_MAC_FILTER_ENABLE = "Device.WiFi.AccessPoint.10001.X_CISCO_COM_MACFilter.Enable";

    /**
     * WebPA Parameter for getting Device Wifi Accesspoint MacFilter Black List in 2.4GHZ network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_MAC_FILTER_BLACK_LIST = "Device.WiFi.AccessPoint.10001.X_CISCO_COM_MACFilter.FilterAsBlackList";

    /**
     * WebPA Parameter for getting Device Wifi Accesspoint MacFilter mode in 2.4GHZ network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_MAC_FILTER_MODE = "Device.WiFi.AccessPoint.10001.X_COMCAST-COM_MAC_FilteringMode";

    /** WebPA parameter to get the radio enabled status for 2.4 ghz */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_STATUS_FOR_2_4GHZ = "Device.WiFi.Radio.10000.Status";

    /** WebPA parameter to get the radio last change time for 2.4 ghz */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_LAST_CHANGE_FOR_2_4GHZ = "Device.WiFi.Radio.10000.LastChange";

    /** WebPA parameter to get the radio enabled status for 5 ghz */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_STATUS_FOR_5GHZ = "Device.WiFi.Radio.10100.Status";

    /** WebPA parameter to get the radio enabled status for 5 ghz */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_LAST_CHANGE_STATUS_FOR_5GHZ = "Device.WiFi.Radio.10100.LastChange";

    /** webpa parameter to get all the possible channels for wifi in 2.4Ghz band */
    public static final String WEBPA_PARAM_FOR_POSSIBLECHANNELS_IN_2GHZ = "Device.WiFi.Radio.10000.PossibleChannels";

    /** WebPA Parameter to get status of prefer private Feature */
    public static final String WEBPA_PARAM_TO_GET_PREFER_PRIVATE_FEATURE_STATUS = "Device.WiFi.X_RDKCENTRAL-COM_PreferPrivate";

    /** webpa parameter to enable/disable wifi stats */
    public static final String WEBPA_PARAM_DEVICE_WIFI_STATS_ENABLE = "Device.WiFi.X_RDKCENTRAL-COM_vAPStatsEnable";

    /** Webpa parameter for vAP stats Enable */
    public static final String WEBPA_PARAM_WIFI_VAP_STATS_ENABLE = "Device.WiFi.X_RDKCENTRAL-COM_vAPStatsEnable";

    /** WebPA Parameter Reverse SSH trigger */
    public static final String WEBPA_REVERSE_SSH_STATUS = "Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.ReverseSSH.xOpsReverseSshStatus";

    /** WebPA Parameter Reverse SSH trigger */
    public static final String WEBPA_REVERSE_SSH_TRIGGER = "Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.ReverseSSH.xOpsReverseSshTrigger";

    /** WebPA Parameter Reverse SSH args */
    public static final String WEBPA_REVERSE_SSH_ARGS = "Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.ReverseSSH.xOpsReverseSshArgs";

    /** webpa parameter to enable parental control rule */
    public static final String WEBPA_PARAM_TO_ENABLE_PARENTAL_CONTROLS = "Device.X_Comcast_com_ParentalControl.ManagedSites.Enable";

    /** webpa parameter to set a parental control rule */
    public static final String WEBPA_PARAM_TO_SET_A_PARENTAL_CONTROL_RULE = "Device.X_Comcast_com_ParentalControl.ManagedSites.BlockedSite.";

    public enum ParentalControlBlockMethod {
	URL("URL"),
	Keyword("Keyword");

	private String blockMethod;

	private ParentalControlBlockMethod(String blockMethod) {
	    this.blockMethod = blockMethod;
	}

	public String getValue() {
	    return blockMethod;
	}
    }

    /** web pa parameter to get the wifi last change value in 5Ghz private wifi */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_LAST_CHANGE_FOR_5GHZ_PRIVATE_SSID = "Device.WiFi.SSID.10101.LastChange";

    /** web pa parameter to get the wifi last change value in 2.4Ghz private wifi */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_LAST_CHANGE_FOR_2_4GHZ_PRIVATE_SSID = "Device.WiFi.SSID.10001.LastChange";

    /** WebPA parameter for Device ManagementServer ConnectionRequestPassword */
    public static final String WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_CONNECTIONREQUESTPASSWORD = "Device.ManagementServer.ConnectionRequestPassword";

    /** WebPA parameter for Device ManagementServer STUNPassword */
    public static final String WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_STUNPASSWORD = "Device.ManagementServer.STUNPassword";

    /** WebPA parameter to get the wifi status */
    public static final String WEBPA_PARAM_DEVICE_WIFI_STATUS = "Device.WiFi.Status";

    /** WebPA Parameter to set DLCaStore.Enable */
    public static final String WEBPA_PARAM_DLCASTORE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.DLCaStore.Enable";

    /** WebPA Parameter for Interworking element Enable */
    public static final String WEBPA_PARAM_FOR_IE_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.WiFi-Interworking.Enable";

    /** WebPA Parameter for Interworking Service capability */
    public static final String WEBPA_PARAM_FOR_IE_SERVICE_CAPABILITY = "Device.WiFi.AccessPoint.{i}.X_RDKCENTRAL-COM_InterworkingServiceCapability";

    /** WebPA Parameter for Interworking Service Enable */
    public static final String WEBPA_PARAM_FOR_IE_SERVICE_ENABLE = "Device.WiFi.AccessPoint.{i}.X_RDKCENTRAL-COM_InterworkingServiceEnable";

    /** WebPA Parameter for Interworking Service Internet */
    public static final String WEBPA_PARAM_FOR_IE_INTERNET = "Device.WiFi.AccessPoint.{i}.X_RDKCENTRAL-COM_InterworkingElement.Internet";

    /** WebPA Parameter for Interworking Service Apply Settings */
    public static final String WEBPA_PARAM_FOR_IE_SETTINGS = "Device.WiFi.AccessPoint.{i}.X_RDKCENTRAL-COM_InterworkingApplySettings";

    /**
     * WebPA parameter to get the IPv4 capable status of the gateway.
     */
    public static final String WEBPA_PARAM_DEVICE_IP_IPV4CAPABLE = "Device.IP.IPv4Capable";

    /**
     * WebPA parameter to get the IPv4 Enabled status of the gateway.
     */
    public static final String WEBPA_PARAM_DEVICE_IP_IPV4ENABLE = "Device.IP.IPv4Enable";

    /**
     * WebPA parameter to get the IPv4 status of the gateway.
     */
    public static final String WEBPA_PARAM_DEVICE_IP_IPV4STATUS = "Device.IP.IPv4Status";

    /**
     * WebPA parameter to get the IPv6 capable status of the gateway.
     */
    public static final String WEBPA_PARAM_DEVICE_IP_IPV6CAPABLE = "Device.IP.IPv6Capable";

    /**
     * WebPA parameter to get the IPv6 Enabled status of the gateway.
     */
    public static final String WEBPA_PARAM_DEVICE_IP_IPV6ENABLE = "Device.IP.IPv6Enable";

    /**
     * WebPA parameter to get the IPv6 status of the gateway.
     */
    public static final String WEBPA_PARAM_DEVICE_IP_IPV6STATUS = "Device.IP.IPv6Status";

    /** webpa parameter to change the http remote access status in */
    public static final String WEBPA_PARAM_FOR_HTTP_REMOTE_ACCESSSTATUS = "Device.UserInterface.X_CISCO_COM_RemoteAccess.HttpEnable";

    /** webpa parameter to change the https remote access status in */
    public static final String WEBPA_PARAM_FOR_HTTPS_REMOTE_ACCESSSTATUS = "Device.UserInterface.X_CISCO_COM_RemoteAccess.HttpsEnable";

    /** WebPA Parameter to configure port forwarding rule */
    public static final String WEBPA_PARAM_TO_CONFIGURE_PORT_FORWARDING_RULE = "Device.NAT.PortMapping.";

    /**
     * WebPa parameter for Device.DeviceInfo.X_RDKCENTRAL-COM_OnBoarding_State
     */
    public static final String WEBPA_PARAM_FEATURE_ONBOARDING_STATE = "Device.DeviceInfo.X_RDKCENTRAL-COM_OnBoarding_State";

    /**
     * webpa parameter to get the value of Device.WiFi.AccessPoint.10001.SSIDAdvertisementEnabled
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5GHZ_SSIDADVERTISEMENTENABLED = "Device.WiFi.AccessPoint.10101.SSIDAdvertisementEnabled";

    /**
     * webpa parameter to get the value of Device.WiFi.AccessPoint.10001.SSIDReference
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5GHZ_SSIDREFERENCE = "Device.WiFi.AccessPoint.10101.SSIDReference";

    /** webpa parameter to get the value of Device.WiFi.AccessPoint.10001.Status */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5GHZ_STATUS = "Device.WiFi.AccessPoint.10101.Status";

    /** webpa parameter to get the value of Device.WiFi.AccessPoint.10001.Enable */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4GHZ_ENABLE = "Device.WiFi.AccessPoint.10001.Enable";

    /** webpa parameter to get the value of Device.WiFi.AccessPoint.10001.Enable */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5GHZ_ENABLE = "Device.WiFi.AccessPoint.10101.Enable";

    /**
     * webpa parameter to get the value of Device.WiFi.AccessPoint.10001.Security.ModeEnabled
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4GHZ_MODEENABLED = "Device.WiFi.AccessPoint.10001.Security.ModeEnabled";

    /**
     * webpa parameter to get the value of Device.WiFi.AccessPoint.10001.Security.ModesSupported
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4GHZ_MODESSUPPORTED = "Device.WiFi.AccessPoint.10001.Security.ModesSupported";

    /**
     * webpa parameter to get the value of Device.WiFi.AccessPoint.10001.MaxAssociatedDevices
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4GHZ_MAXASSOCIATEDDEVICES = "Device.WiFi.AccessPoint.10001.MaxAssociatedDevices";

    /**
     * webpa parameter to get the value of Device.WiFi.AccessPoint.10001.MaxAssociatedDevices
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5GHZ_MAXASSOCIATEDDEVICES = "Device.WiFi.AccessPoint.10101.MaxAssociatedDevices";

    /**
     * webpa parameter to get the value of Device.WiFi.AccessPoint.10001.AssociatedDeviceNumberOfEntries
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4GHZ_ASSOCIATEDDEVICENUMBEROFENTRIES = "Device.WiFi.AccessPoint.10001.AssociatedDeviceNumberOfEntries";

    /**
     * webpa parameter to get the value of Device.WiFi.AccessPoint.10001.SSIDAdvertisementEnabled
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4GHZ_SSIDADVERTISEMENTENABLED = "Device.WiFi.AccessPoint.10001.SSIDAdvertisementEnabled";

    /**
     * webpa parameter to get the value of Device.WiFi.AccessPoint.10001.SSIDReference
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4GHZ_SSIDREFERENCE = "Device.WiFi.AccessPoint.10001.SSIDReference";

    /** webpa parameter to get the value of Device.WiFi.AccessPoint.10001.Status */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4GHZ_STATUS = "Device.WiFi.AccessPoint.10001.Status";

    /**
     * webpa parameter to get the value of Device.WiFi.AccessPoint.10001.AssociatedDeviceNumberOfEntries
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5GHZ_ASSOCIATEDDEVICENUMBEROFENTRIES = "Device.WiFi.AccessPoint.10101.AssociatedDeviceNumberOfEntries";

    /**
     * webpa parameter to get the value of Device.WiFi.AccessPoint.10001.Security.ModesSupported
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5GHZ_MODESSUPPORTED = "Device.WiFi.AccessPoint.10101.Security.ModesSupported";

    /**
     * webpa parameter to get the value of Device.WiFi.AccessPoint.10001.Security.ModeEnabled
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5GHZ_MODEENABLED = "Device.WiFi.AccessPoint.10101.Security.ModeEnabled";

    /** Webpa parameter for telemetryEndpoint URL */
    public static final String WEBPA_PARAM_WIFI_TELEMETRY_ENDPOINTURL = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.TelemetryEndpoint.URL";

    /** WebPA parameter to get Black list table timeout for 2.4GHz LNF SSID */
    public static final String WEBPA_PARAM_DEVICE_WIFI_2_4GHZ_LNF_BLACK_LIST_TABLE_TIMEOUT = "Device.WiFi.AccessPoint.10006.Security.X_COMCAST-COM_RadiusSettings.BlacklistTableTimeout";

    /** WebPA parameter to get Max authentication attempts */
    public static final String WEBPA_PARAM_DEVICE_WIFI_2_4GHZ_LNF_MAX_AUTHENTICATION_ATTEMPTS = "Device.WiFi.AccessPoint.10006.Security.X_COMCAST-COM_RadiusSettings.MaxAuthenticationAttempts";

    /** WebPA parameter to get the radio stats BytesSent */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_STATS_BYTESSENT = "Device.WiFi.Radio.{i}.Stats.BytesSent";

    /** WebPA parameter to get the radio stats Bytes Received */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_STATS_BYTESRECEIVED = "Device.WiFi.Radio.{i}.Stats.BytesReceived";

    /** WebPA parameter to get the radio stats Packets Sent */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_STATS_PACKETSSENT = "Device.WiFi.Radio.{i}.Stats.PacketsSent";

    /** WebPA parameter to get the radio stats Packets Received */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_STATS_PACKETSRECEIVED = "Device.WiFi.Radio.{i}.Stats.PacketsReceived";

    /** WebPA parameter to get the radio stats Errors Sent */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_STATS_ERRORSSENT = "Device.WiFi.Radio.{i}.Stats.ErrorsSent";

    /** WebPA parameter to get the radio stats Errors Received */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_STATS_ERRORSRECEIVED = "Device.WiFi.Radio.{i}.Stats.ErrorsReceived";

    /** WebPA parameter to get the radio stats discard packets sent */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_STATS_DISCARDPACKETSSENT = "Device.WiFi.Radio.{i}.Stats.DiscardPacketsSent";

    /** WebPA parameter to get the radio stats discard packets received */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_STATS_DISCARDPACKETSRECEIVED = "Device.WiFi.Radio.{i}.Stats.DiscardPacketsReceived";

    /** web pa parameter to get the wifi SSID Alias in 2.4Ghz private wifi */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_ALIAS_FOR_2_4GHZ_PRIVATE_SSID = "Device.WiFi.SSID.10001.Alias";

    /** web pa parameter to get the wifi SSID Alias in 5Ghz private wifi */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_ALIAS_FOR_5GHZ_PRIVATE_SSID = "Device.WiFi.SSID.10101.Alias";

    /** web pa parameter to get the wifi SSID Name in 2.4Ghz private wifi */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_NAME_FOR_2_4GHZ_PRIVATE_SSID = "Device.WiFi.SSID.10001.Name";

    /** web pa parameter to get the wifi SSID Name in 5Ghz private wifi */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_NAME_FOR_5GHZ_PRIVATE_SSID = "Device.WiFi.SSID.10101.Name";

    /** web pa parameter to get the wifi SSID LowerLayers in 2.4Ghz private wifi */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_LOWERLAYERS_FOR_2_4GHZ_PRIVATE_SSID = "Device.WiFi.SSID.10001.LowerLayers";

    /** web pa parameter to get the wifi SSID LowerLayers in 5Ghz private wifi */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_LOWERLAYERS_FOR_5GHZ_PRIVATE_SSID = "Device.WiFi.SSID.10101.LowerLayers";

    /** WebPA parameter to get the 2.4GHZ private WiFi BSSID */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_BSSID_FOR_2_4GHZ = "Device.WiFi.SSID.10001.BSSID";

    /** WebPA parameter to get the 5GHZ rprivate WiFi BSSID */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_BSSID_FOR_5GHZ = "Device.WiFi.SSID.10101.BSSID";

    /** WebPA parameter to get the 2.4GHZ private WiFi MACAddress */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_MACADDRESS_FOR_2_4GHZ = "Device.WiFi.SSID.10001.MACAddress";

    /** WebPA parameter to get the 5GHZ rprivate WiFi MACAddress */
    public static final String WEBPA_PARAM_DEVICE_WIFI_SSID_MACADDRESS_FOR_5GHZ = "Device.WiFi.SSID.10101.MACAddress";

    /** WebPA Parameter for ENABLING radio setting */
    public static final String WEBPA_PARAM_WIFI_RADIO_ENABLE = "Device.WiFi.Radio.{i}.Enable";

    /** WebPA parameter to get the Supported Standards */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_OPERATINGSTANDARDS = "Device.WiFi.Radio.{i}.OperatingStandards";

    /** WebPA parameter to get the Supported Standards */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_SUPPORTEDSTANDARDS = "Device.WiFi.Radio.{i}.SupportedStandards";

    /** WebPA parameter to get the Supported Frequency Bands */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_SUPPORTEDFREQUENCYBANDS = "Device.WiFi.Radio.{i}.SupportedFrequencyBands";

    /** WebPA parameter to get the Operating Frequency Bands */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_OPERATINGFREQUENCYBAND = "Device.WiFi.Radio.{i}.OperatingFrequencyBand";

    /** WebPA parameter to get the radio Upstream */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_UPSTREAM = "Device.WiFi.Radio.{i}.Upstream";

    /** WebPA parameter to get the radio Max Bit rate */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_MAXBITRATE = "Device.WiFi.Radio.{i}.MaxBitRate";

    /** WebPA parameter to get the radio Alias name */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_ALIAS = "Device.WiFi.Radio.{i}.Alias";

    /** WebPA parameter to get the radio name */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_NAME = "Device.WiFi.Radio.{i}.Name";

    /** WebPA parameter to get the radio last change time */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_LAST_CHANGE = "Device.WiFi.Radio.{i}.LastChange";

    /** WebPA parameter to get the radio lower layers */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_LOWERLAYERS = "Device.WiFi.Radio.{i}.LowerLayers";

    /** WebPA parameter to get the radio enabled status */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_STATUS = "Device.WiFi.Radio.{i}.Status";

    /** WebPa param for notify component connected client */
    public static final String WEBPA_PARAM_DEVICE_NOTIFY_COMPONENT_CONNECTED_CLIENT = "Device.NotifyComponent.X_RDKCENTRAL-COM_Connected-Client";

    public static final String WEBPA_PARAM_DEVICE_ETHERNET_INTERFACE_ENTRIES = "Device.Ethernet.InterfaceNumberOfEntries";

    /** Webpa Param for Device ethernet interface entries enable status */
    public static final String WEBPA_PARAM_DEVICE_ETHERNET_INTERFACE_ENABLE = "Device.Ethernet.Interface.{i}.Enable";
    /** Webpa Param for Device ethernet interface status */
    public static final String WEBPA_PARAM_DEVICE_ETHERNET_INTERFACE_STATUS = "Device.Ethernet.Interface.{i}.Status";
    /** Webpa Param for Device ethernet interface Alias */
    public static final String WEBPA_PARAM_DEVICE_ETHERNET_INTERFACE_ALIAS = "Device.Ethernet.Interface.{i}.Alias";
    /** Webpa Param for Device ethernet interface Name */
    public static final String WEBPA_PARAM_DEVICE_ETHERNET_INTERFACE_NAME = "Device.Ethernet.Interface.{i}.Name";
    /** Webpa Param for Device ethernet interface Mac Address */
    public static final String WEBPA_PARAM_DEVICE_ETHERNET_INTERFACE_MACADDRESS = "Device.Ethernet.Interface.{i}.MACAddress";
    /** Webpa Param for Device ethernet interface Max bit rate */
    public static final String WEBPA_PARAM_DEVICE_ETHERNET_INTERFACE_MAXBITRATE = "Device.Ethernet.Interface.{i}.MaxBitRate";
    /** Webpa Param for Device ethernet interface Byte sent */
    public static final String WEBPA_PARAM_DEVICE_ETHERNET_INTERFACE_BYTE_SENT = "Device.Ethernet.Interface.{i}.Stats.BytesSent";
    /** Webpa Param for Device ethernet interface Byte received */
    public static final String WEBPA_PARAM_DEVICE_ETHERNET_INTERFACE_BYTE_RECEIVED = "Device.Ethernet.Interface.{i}.Stats.BytesReceived";
    /** Webpa Param for Device ethernet interface Packet received */
    public static final String WEBPA_PARAM_DEVICE_ETHERNET_INTERFACE_PACKETS_RECEIVED = "Device.Ethernet.Interface.{i}.Stats.PacketsReceived";
    /** Webpa Param for Device ethernet interface Error received */
    public static final String WEBPA_PARAM_DEVICE_ETHERNET_INTERFACE_ERRORS_RECEIVED = "Device.Ethernet.Interface.{i}.Stats.ErrorsReceived";
    /** Webpa Param for Device ethernet interface Discard Packets Sent */
    public static final String WEBPA_PARAM_DEVICE_ETHERNET_INTERFACE_DISCARD_PACKETS_SENT = "Device.Ethernet.Interface.{i}.Stats.DiscardPacketsSent";
    /** Webpa Param for Device ethernet interface Broadcast packets sent */
    public static final String WEBPA_PARAM_DEVICE_ETHERNET_INTERFACE_BROADCAST_PACKETS_SENT = "Device.Ethernet.Interface.{i}.Stats.BroadcastPacketsSent";
    /** Webpa Param for Device ethernet interface Broadcast packets received */
    public static final String WEBPA_PARAM_DEVICE_ETHERNET_INTERFACE_BROADCAST_PACKETS_RECEIVED = "Device.Ethernet.Interface.{i}.Stats.BroadcastPacketsReceived";

    /** WebPA Parameter for retrieving the Host Address Source */
    public static final String WEBPA_PARAM_DEVICE_HOST_ADDRESS_SOURCE = "Device.Hosts.Host.{i}.AddressSource";
    /** WebPA Parameter for retrieving the Host Lease time remaining */
    public static final String WEBPA_PARAM_DEVICE_HOST_LEASE_TIME_REMAINING = "Device.Hosts.Host.{i}.LeaseTimeRemaining";
    /** WebPA Parameter for retrieving the Host Associated device */
    public static final String WEBPA_PARAM_DEVICE_HOST_ASSOCIATED_DEVICE = "Device.Hosts.Host.{i}.AssociatedDevice";
    /** WebPA Parameter for retrieving the Host Connection Type */
    public static final String WEBPA_PARAM_DEVICE_HOST_LAYER_3_INTERFACE = "Device.Hosts.Host.{i}.Layer3Interface";
    /** WebPA Parameter for retrieving the Host LAST CHANGE */
    public static final String WEBPA_PARAM_DEVICE_HOST_LAST_CHANGE = "Device.Hosts.Host.{i}.X_COMCAST-COM_LastChange";
    /**
     * WebPA parameter representation for Device.WiFi.AccessPoint.{i}.Security.PreSharedKey
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_SECURITY_PRESHAREDKEY = "Device.WiFi.AccessPoint.{i}.Security.PreSharedKey";

    /** WebPA parameter to get the Channels in Use */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_CHANNELSINUSE = "Device.WiFi.Radio.{i}.ChannelsInUse";

    /** WebPA parameter to get the Channel */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_CHANNEL = "Device.WiFi.Radio.{i}.Channel";

    /** WebPA parameter to get the Auto Channel Supported */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_AUTOCHANNELSUPPORTED = "Device.WiFi.Radio.{i}.AutoChannelSupported";

    /** WebPA parameter to get the Auto Channel Enable */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_AUTOCHANNELENABLE = "Device.WiFi.Radio.{i}.AutoChannelEnable";

    /** WebPA parameter to get the Auto Refresh Period */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_AUTOREFRESHPERIOD = "Device.WiFi.Radio.{i}.AutoChannelRefreshPeriod";

    /** WebPA parameter to get the Operating Channel Bandwidth */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_OPERATINGCHANNELBANDWIDTH = "Device.WiFi.Radio.{i}.OperatingChannelBandwidth";

    /** WebPA parameter to get the Extension channel */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_EXTENSIONCHANNEL = "Device.WiFi.Radio.{i}.ExtensionChannel";

    /** WebPA parameter to get the Guard Interval */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_GUARDINTERVAL = "Device.WiFi.Radio.{i}.GuardInterval";

    /** WebPA parameter to get the MCS */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_MCS = "Device.WiFi.Radio.{i}.MCS";

    /** WebPA parameter to get the transmit power supported */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_TRANSMITPOWERSUPPORTED = "Device.WiFi.Radio.{i}.TransmitPowerSupported";

    /** WebPA parameter to get the transmit power supported */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_TRANSMITPOWER = "Device.WiFi.Radio.{i}.TransmitPower";

    /** WebPA parameter to get the IEEE80211h Supported */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_IEEE80211HSUPPORTED = "Device.WiFi.Radio.{i}.IEEE80211hSupported";

    /** WebPA parameter to get the IEEE80211h Supported */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_IEEE80211HENABLED = "Device.WiFi.Radio.{i}.IEEE80211hEnabled";

    /** WebPA parameter to get the regulatory domain */
    public static final String WEBPA_PARAM_DEVICE_WIFI_RADIO_REGULATORYDOMAIN = "Device.WiFi.Radio.{i}.RegulatoryDomain";

    /** WebPA parameter for enabling feature Telemetry message bus source */
    public static final String WEBPA_PARAM_TELEMETRY_MESSAGE_BUS_SOURCE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.Telemetry.MessageBusSource.Enable";

    /** Webpa param for block lost and found internet enable */
    public static final String WEBPA_PARAM_FOR_LOST_N_FOUND_INTERNET_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.BlockLostandFoundInternet.Enable";

    /** Webpa param for ipv6 prefix enable */
    public static final String WEBPA_PARAM_FOR_LOST_N_FOUND_INTERNET_ENABLE_IPV6_PREFIX = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.IPv6subPrefix.Enable";

    /** Webpa param for block lost and found internet enable ipv6 */
    public static final String WEBPA_PARAM_FOR_LOST_N_FOUND_INTERNET_ENABLE_IPV6_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.IPv6onLnF.Enable";

    /** WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.BytesSent */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_BYTESENT = "Device.WiFi.SSID.{i}.Stats.BytesSent";

    /** WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.BytesReceived */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_BYTESRECEIVED = "Device.WiFi.SSID.{i}.Stats.BytesReceived";

    /** WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.PacketsSent */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_PACKETSSENT = "Device.WiFi.SSID.{i}.Stats.PacketsSent";

    /** WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.PacketsReceived */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_PACKETSRECEIVED = "Device.WiFi.SSID.{i}.Stats.PacketsReceived";

    /** WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.ErrorsSent */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_ERRORSSENT = "Device.WiFi.SSID.{i}.Stats.ErrorsSent";

    /** WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.ErrorsReceived */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_ERRORSRECEIVED = "Device.WiFi.SSID.{i}.Stats.ErrorsReceived";

    /**
     * WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.UnicastPacketsSent
     */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_UNICASTPACKETSSENT = "Device.WiFi.SSID.{i}.Stats.UnicastPacketsSent";

    /**
     * WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.UnicastPacketsReceived
     */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_UNICASTPACKETSRECEIVED = "Device.WiFi.SSID.{i}.Stats.UnicastPacketsReceived";

    /**
     * WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.DiscardPacketsSent
     */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_DISCARDPACKETSSENT = "Device.WiFi.SSID.{i}.Stats.DiscardPacketsSent";

    /**
     * WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.DiscardPacketsReceived
     */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_DISCARDPACKETSRECEIVED = "Device.WiFi.SSID.{i}.Stats.DiscardPacketsReceived";

    /**
     * WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.MulticastPacketsSent
     */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_MULTICASTPACKETSSENT = "Device.WiFi.SSID.{i}.Stats.MulticastPacketsSent";

    /**
     * WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.MulticastPacketsReceived
     */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_MULTICASTPACKETSRECEIVED = "Device.WiFi.SSID.{i}.Stats.MulticastPacketsReceived";

    /**
     * WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.BroadcastPacketsSent
     */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_BROADCASTPACKETSSENT = "Device.WiFi.SSID.{i}.Stats.BroadcastPacketsSent";

    /**
     * WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.BroadcastPacketsReceived
     */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_BROADCASTPACKETSRECEIVED = "Device.WiFi.SSID.{i}.Stats.BroadcastPacketsReceived";

    /**
     * WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.UnknownProtoPacketsReceived
     */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_UNKNOWNPROTOCOLPACKETSSENT = "Device.WiFi.SSID.{i}.Stats.UnknownProtoPacketsReceived";

    /** WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.RetransCount */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_RETRANSCOUNT = "Device.WiFi.SSID.{i}.Stats.RetransCount";

    /**
     * WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.FailedRetransCount
     */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_FAILEDRETRANSCOUNT = "Device.WiFi.SSID.{i}.Stats.FailedRetransCount";

    /** WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.RetryCount */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_RETRYCOUNT = "Device.WiFi.SSID.{i}.Stats.RetryCount";

    /**
     * WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.MultipleRetryCount
     */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_MULTIPLERETRYCOUNT = "Device.WiFi.SSID.{i}.Stats.MultipleRetryCount";

    /** WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.ACKFailureCount */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_ACKFAILURECOUNT = "Device.WiFi.SSID.{i}.Stats.ACKFailureCount";

    /**
     * WebPa Parameter for the object Device.WiFi.SSID.{i}.Stats.AggregatedPacketCount
     */
    public static final String WEBPA_DEVICE_WIFI_SSID_STATS_AGGREGATEDPACKETCOUNT = "Device.WiFi.SSID.{i}.Stats.AggregatedPacketCount";

    /** WebPA Parameter for Interworking Service Access Network Type */
    public static final String WEBPA_PARAM_FOR_IE_ACCESS_NETWORK_TYPE = "Device.WiFi.AccessPoint.{i}.X_RDKCENTRAL-COM_InterworkingElement.AccessNetworkType";

    /** WebPA Parameter for Interworking Service ASRA */
    public static final String WEBPA_PARAM_FOR_IE_ASRA = "Device.WiFi.AccessPoint.{i}.X_RDKCENTRAL-COM_InterworkingElement.ASRA";

    /** WebPA Parameter for Interworking Service ESR */
    public static final String WEBPA_PARAM_FOR_IE_ESR = "Device.WiFi.AccessPoint.{i}.X_RDKCENTRAL-COM_InterworkingElement.ESR";

    /** WebPA Parameter for Interworking Service UESA */
    public static final String WEBPA_PARAM_FOR_IE_UESA = "Device.WiFi.AccessPoint.{i}.X_RDKCENTRAL-COM_InterworkingElement.UESA";

    /** WebPA Parameter for Interworking Service Venue Group */
    public static final String WEBPA_PARAM_FOR_IE_VENUE_GROUP = "Device.WiFi.AccessPoint.{i}.X_RDKCENTRAL-COM_InterworkingElement.VenueInfo.Group";

    /** WebPA Parameter for Interworking Service Venue Type */
    public static final String WEBPA_PARAM_FOR_IE_VENUE_TYPE = "Device.WiFi.AccessPoint.{i}.X_RDKCENTRAL-COM_InterworkingElement.VenueInfo.Type";

    /** WebPA Parameter for Interworking Service HESSID */
    public static final String WEBPA_PARAM_FOR_IE_HESSID = "Device.WiFi.AccessPoint.{i}.X_RDKCENTRAL-COM_InterworkingElement.HESSID";

    /** WebPA Parameter to enable/disable DMZ */
    public static final String WEBPA_PARAM_TO_ENABLE_AND_DISABLE_DMZ = "Device.NAT.X_CISCO_COM_DMZ.Enable";

    /** Webpa parameter to get the Ethernet log period */
    public static final String WEBPA_PARAM_ETH_LOG_PERIOD = "Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.Logging.xOpsDMEthLogPeriod";

    /** Webpa parameter to get the Ethernet log enabled */
    public static final String WEBPA_PARAM_ETH_LOG_ENABLED = "Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.Logging.xOpsDMEthLogEnabled";

    /** WebPA Parameter to retrieve the Ethernet clients no.of entries */
    public static final String WEBPA_PARAM_ETH_ASSOCIATED_CLIENT_NO_OF_ENTRIES = "Device.Ethernet.Interface.{i}.X_RDKCENTRAL-COM_AssociatedDeviceNumberOfEntries";

    /** WebPA Parameter to retrieve the Ethernet client mac address */
    public static final String WEBPA_PARAM_ETH_CLIENT_MAC_ADDRESS = "Device.Ethernet.Interface.{i}.X_RDKCENTRAL-COM_AssociatedDevice.1.MACAddress";

    /** Webpa parameter to get 2.4GHz WiFi number of entries */
    public static final String WEBPA_PARAM_WIFI_NO_OF_ENTRIES = "Device.WiFi.AccessPoint.10001.AssociatedDeviceNumberOfEntries";

    /** Webpa parameter to get 2.4GHz client Mac address */
    public static final String WEBPA_PARAM_WIFI_MAC_ADDRESS = "Device.WiFi.AccessPoint.10001.AssociatedDevice.1.MACAddress";

    /** TR69 param for reboot device */
    public static final String TR69_PARAM_REBOOT = "Device.X_CISCO_COM_DeviceControl.RebootDevice";

    /** Webpa parameter for wifi feature MFPCONFIG */
    public static final String WEBPA_PARAM_WIFI_FEATURE_MFPCONFIG = "Device.WiFi.FeatureMFPConfig";

    /**
     * WebPa parameter for Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.BLERadio
     */
    public static final String WEBPA_PARAM_FEATURE_BLE_RADIO = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.BLERadio";

    /**
     * WebPa parameter for Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.BLE.Discovery
     */
    public static final String WEBPA_PARAM_FEATURE_BLE_DISCOVERY = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.BLE.Discovery";

    /**
     * WebPA Parameter to get Passphrase of 2.4 GHZ private WiFi Network
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_1_SECURITY_COMCAST_COM_KEYPASSPHRASE = "Device.WiFi.AccessPoint.1.Security.X_COMCAST-COM_KeyPassphrase";

    /** WebPA parameter to set WiFi GAS configuration */
    public static final String WEBPA_PARAM_WIFI_GAS_CONFIGURATION = "Device.WiFi.X_RDKCENTRAL-COM_GASConfiguration";

    /** WebPA parameter to set WiFi GAS Queries Stats */
    public static final String WEBPA_PARAM_WIFI_GAS_STATS_QUERIES = "Device.WiFi.X_RDKCENTRAL-COM_GASStats.1.Queries";

    /** WebPA parameter to set WiFi GAS QueryRate Stats */
    public static final String WEBPA_PARAM_WIFI_GAS_STATS_QUERY_RATE = "Device.WiFi.X_RDKCENTRAL-COM_GASStats.1.QueryRate";

    /** WebPA parameter to set WiFi GAS Responses Stats */
    public static final String WEBPA_PARAM_WIFI_GAS_STATS_RESPONSES = "Device.WiFi.X_RDKCENTRAL-COM_GASStats.1.Responses";

    /** WebPA parameter to set WiFi GAS ResponseRate Stats */
    public static final String WEBPA_PARAM_WIFI_GAS_STATS_RESPONSE_RATE = "Device.WiFi.X_RDKCENTRAL-COM_GASStats.1.ResponseRate";

    /** WebPA parameter to set WiFi GAS NoRequestOutstanding Stats */
    public static final String WEBPA_PARAM_WIFI_GAS_STATS_REQUEST_OUTSTANDING = "Device.WiFi.X_RDKCENTRAL-COM_GASStats.1.NoRequestOutstanding";

    /** WebPA parameter to set WiFi GAS ResponsesDiscarded Stats */
    public static final String WEBPA_PARAM_WIFI_GAS_STATS_RESPONSES_DISCARDED = "Device.WiFi.X_RDKCENTRAL-COM_GASStats.1.ResponsesDiscarded";

    /** WebPA parameter to set WiFi GAS FailedResponses Stats */
    public static final String WEBPA_PARAM_WIFI_GAS_STATS_FAILED_RESPONSES = "Device.WiFi.X_RDKCENTRAL-COM_GASStats.1.FailedResponses";

    /** Webpa parameter for telemetry TxRxRateList */
    public static final String WEBPA_PARAM_WIFI_TELE_TXRX_RATE_LIST = "Device.DeviceInfo.X_RDKCENTRAL-COM_WIFI_TELEMETRY.TxRxRateList";

    /** WebPA parameter to get the public WiFi BSSID */
    public static final String WEBPA_PARAM_DEVICE_PUBLIC_WIFI_SSID_BSSID = "Device.WiFi.SSID.{i}.BSSID";

    /** WebPA parameter for Device Finger Print Logging Period */
    public static final String WEBPA_PARAM_DEVICE_FINGER_PRINT_LOGGING_PERIOD = "Device.DeviceInfo.X_RDKCENTRAL-COM_DeviceFingerPrint.LoggingPeriod";

    /** WebPA Parameter to update the Global DNS IPv4 value */
    public static final String WEBPA_PARAM_TO_UPDATE_GLOBAL_SECONDARY_XDNS_IPV4 = "Device.X_RDKCENTRAL-COM_XDNS.DefaultSecondaryDeviceDnsIPv4";

    /** WebPA Parameter to update the Global DNS IPv6 value */
    public static final String WEBPA_PARAM_TO_UPDATE_GLOBAL_SECONDARY_XDNS_IPV6 = "Device.X_RDKCENTRAL-COM_XDNS.DefaultSecondaryDeviceDnsIPv6";

    /** WebPA parameter for SpeedTest.Authentication */
    public static final String WEBPA_PARAM_SPEED_TEST_AUTHENTICATION = "Device.IP.Diagnostics.X_RDKCENTRAL-COM_SpeedTest.Authentication";

    /**
     * WebPA Parameter to enable/disable Parental control managed devices feature
     */
    public static final String WEBPA_PARAM_TO_ENABLE_PARENTAL_CONTROL_MANAGED_DEVICES_FEATURE = "Device.X_Comcast_com_ParentalControl.ManagedDevices.Enable";

    /**
     * WebPA Parameter to enable/disable Parental control managed devices allow all
     */
    public static final String WEBPA_PARAM_TO_ENABLE_PARENTAL_CONTROL_ALLOW_ALL = "Device.X_Comcast_com_ParentalControl.ManagedDevices.AllowAll";

    /** WebPA Parameter to add/get parental control managed devices table */
    public static final String WEBPA_PARAM_TO_GET_PARENTAL_CONTROL_MANAGED_DEVICES_TABLE = "Device.X_Comcast_com_ParentalControl.ManagedDevices.Device.";

    /** Webpa parameter to enable OVS */
    public static final String WEBPA_PARAM_OVS_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.OVS.Enable";

    /** WebPA parameter for secured public wifi 2GHz SSID name */
    public static final String WEBPA_PARAM_2GHZ_SECURED_PUBLIC_WIFI_SSID = "Device.WiFi.SSID.10005.SSID";

    /** WebPA parameter for secured public wifi 5GHz SSID name */
    public static final String WEBPA_PARAM_5GHZ_SECURED_PUBLIC_WIFI_SSID = "Device.WiFi.SSID.10105.SSID";

    /** WebPA Parameter for WiFi associated device details */
    public static final String WEBPA_PARAM_FOR_CONNECTED_DEVICES_DETAILS = "Device.WiFi.AccessPoint.{i}.AssociatedDevice.";

    /** WebPA Parameter to check secondary cpu dma memory fragmentation details */
    public static final String WEBPA_PARAM_SECONDARY_CPU_MEMFRAG_DMA = "Device.SelfHeal.CpuMemFrag.2.DMA";

    /** WebPA Parameter to check secondary cpu dma32 memory fragmentation details */
    public static final String WEBPA_PARAM_SECONDARY_CPU_MEMFRAG_DMA32 = "Device.SelfHeal.CpuMemFrag.2.DMA32";

    /**
     * WebPA Parameter to check secondary cpu normal memory fragmentation details
     */
    public static final String WEBPA_PARAM_SECONDARY_CPU_MEMFRAG_NORMAL = "Device.SelfHeal.CpuMemFrag.2.Normal";

    /**
     * WebPA Parameter to check secondary cpu highmem memory fragmentation details
     */
    public static final String WEBPA_PARAM_SECONDARY_CPU_MEMFRAG_HIGHMEM = "Device.SelfHeal.CpuMemFrag.2.Highmem";

    /** WebPA Parameter to check primary cpu dma memory fragmentation details */
    public static final String WEBPA_PARAM_PRIMARY_CPU_MEMFRAG_DMA = "Device.SelfHeal.CpuMemFrag.1.DMA";

    /** WebPA Parameter to check primary cpu dma32 memory fragmentation details */
    public static final String WEBPA_PARAM_PRIMARY_CPU_MEMFRAG_DMA32 = "Device.SelfHeal.CpuMemFrag.1.DMA32";

    /** WebPA Parameter to check primary cpu normal memory fragmentation details */
    public static final String WEBPA_PARAM_PRIMARY_CPU_MEMFRAG_NORMAL = "Device.SelfHeal.CpuMemFrag.1.Normal";

    /** WebPA Parameter to check primary cpu highmem memory fragmentation details */
    public static final String WEBPA_PARAM_PRIMARY_CPU_MEMFRAG_HIGHMEM = "Device.SelfHeal.CpuMemFrag.1.Highmem";

    /** WebPA Parameter to check cpu fragmentation interval details */
    public static final String WEBPA_PARAM_CPU_FRAGMENTATION_INTERVAL = "Device.SelfHeal.X_RDKCENTRAL-COM_CpuMemFragInterval";

    /** WebPA Parameter reporting period of INTERFACE device status */
    public static final String WEBPA_INTERFACE_DEVICES_REPORTING_PERIOD = "Device.X_RDKCENTRAL-COM_Report.InterfaceDevicesWifi.ReportingPeriod";

    /** WebPA Parameter polling period of INTERFACE device status */
    public static final String WEBPA_INTERFACE_DEVICES_POLLING_PERIOD = "Device.X_RDKCENTRAL-COM_Report.InterfaceDevicesWifi.PollingPeriod";

    /** WebPA Parameter reporting period of INTERFACE device status */
    public static final String WEBPA_RADIO_INTERFACE_STATISTICS_REPORTING_PERIOD = "Device.X_RDKCENTRAL-COM_Report.RadioInterfaceStatistics.ReportingPeriod";

    /** WebPA Parameter Radio Interface Statistics */
    public static final String WEBPA_RADIO_INTERFACE_STATISTICS = "Device.X_RDKCENTRAL-COM_Report.RadioInterfaceStatistics.Enabled";

    /** WebPA Parameter for retrieving the Host Physical Address */
    public static final String WEBPA_PARAM_DEVICE_HOST_PHYSICAL_ADDRESS = "Device.Hosts.Host.{i}.PhysAddress";

    /** WebPA Parameter for retrieving the Host Entries */
    public static final String WEBPA_PARAM_DEVICE_HOST_NUMBER_OF_ENTRIES = "Device.Hosts.HostNumberOfEntries";

    /** Webpa Parameter for Public Wifi enable */
    public static final String WEBPA_PARAM_PUBLIC_WIFI_ENABLE = "Device.DeviceInfo.X_COMCAST_COM_xfinitywifiEnable";

    /** WebPA Parameter to retrieve bridge mode status . */
    public static final String WEBPA_PARAM_TO_GET_BRIDGE_MODE_STATUS = "Device.X_CISCO_COM_DeviceControl.LanManagementEntry.1.LanMode";

    /** WebPa Parameter to retrieve the WAN MAC address. */
    public static final String WEBPA_PARAM_WAN_MAC_ADDRESS = "Device.DeviceInfo.X_COMCAST-COM_WAN_MAC";

    /** WebPA Parameter to retrieve Wi-Fi mode on 5GHz Feature */
    public static final String WEBPA_PARAM_TO_GET_WIFI_MODE_ON_5 = "Device.WiFi.Radio.10100.OperatingStandards";

    /** WebPA Parameter to retrieve Wi-Fi mode on 2.4GHz Feature */
    public static final String WEBPA_PARAM_TO_GET_WIFI_MODE_ON_2_4 = "Device.WiFi.Radio.10000.OperatingStandards";

    /** Webpa parameter to get the LastDataDownlinkRate */
    public static final String WEBPA_PARAM_AP_LASTDATADOWNLINKRATE = "Device.WiFi.AccessPoint.{i}.AssociatedDevice.{j}.LastDataDownlinkRate";

    /** Webpa parameter to get the LastDataUplinkRate */
    public static final String WEBPA_PARAM_AP_LASTDATAUPLINKRATE = "Device.WiFi.AccessPoint.{i}.AssociatedDevice.{j}.LastDataUplinkRate";

    /** webpa parameter to get the value of wifi client default override TTL */
    public static final String WEBPA_PARAM_WIFI_CLIENT_DEFAULT_OVERRIDE_TTL = "Device.WiFi.X_RDKCENTRAL-COM_Report.WifiClient.Default.OverrideTTL";

    /** WebPA Parameter polling period of INTERFACE device status */
    public static final String WEBPA_RADIO_INTERFACE_STATISTICS_POLLING_PERIOD = "Device.X_RDKCENTRAL-COM_Report.RadioInterfaceStatistics.PollingPeriod";

    /** WebPA parameter for Unique telemetry id enable */
    public static final String WEBPA_PARAM_UNIQUE_TELEMETRY_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.UniqueTelemetryId.Enable";

    /** WebPA parameter for Unique telemetry id tag string */
    public static final String WEBPA_PARAM_UNIQUE_TELEMETRY_TAGSTRING = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.UniqueTelemetryId.TagString";

    /** WebPA parameter for Unique telemetry id timing interval */
    public static final String WEBPA_PARAM_UNIQUE_TELEMETRY_TIMINGINTERVAL = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.UniqueTelemetryId.TimingInterval";

    /** Webpa parameter for vAP stats Enable */
    public static final String WEBPA_PARAMETER_WIFI_TELEMETRY_NORMALIZED_RSSI_LIST = "Device.DeviceInfo.X_RDKCENTRAL-COM_WIFI_TELEMETRY.NormalizedRssiList";

    /** WebPA Parameter for WiFiClient Schema Id */
    public static final String WEBPA_PARAM_WIFICLIENT_SCHEMA_ID = "Device.WiFi.X_RDKCENTRAL-COM_Report.WifiClient.SchemaID";

    /** WebPA Parameter reporting period of INTERFACE device status */
    public static final String WEBPA_INTERFACE_DEVICES_DEFAULT_REPORTING_PERIOD = "Device.X_RDKCENTRAL-COM_Report.InterfaceDevicesWifi.Default.ReportingPeriod";

    /** WebPA Parameter reporting period of INTERFACE device status */
    public static final String WEBPA_RADIO_INTERFACE_STATISTICS_DEFAULT_REPORTING_PERIOD = "Device.X_RDKCENTRAL-COM_Report.RadioInterfaceStatistics.Default.ReportingPeriod";

    /** WebPA Parameter polling period of INTERFACE device status */
    public static final String WEBPA_INTERFACE_DEVICES_DEFAULT_POLLING_PERIOD = "Device.X_RDKCENTRAL-COM_Report.InterfaceDevicesWifi.Default.PollingPeriod";

    /** WebPA Parameter polling period of INTERFACE device status */
    public static final String WEBPA_RADIO_INTERFACE_STATISTICS_DEFAULT_POLLING_PERIOD = "Device.X_RDKCENTRAL-COM_Report.RadioInterfaceStatistics.Default.PollingPeriod";

    /** WebPA parameter to get the SSID name for 2.4 GHz Wi-Fi network. */
    public static final String WEBPA_PARAM_DEVICE_WIFI_NAME = "Device.WiFi.SSID.{i}.SSID";

    /** webpa parameter to get the value of Device.WiFi.AccessPoint.{i}.Enable */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_ENABLE = "Device.WiFi.SSID.{i}.Enable";

    /** webpa parameter to get the value of Device.WiFi.AccessPoint.{i}.Enable */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_SSIDADVERTISEMENT = "Device.WiFi.AccessPoint.{i}.SSIDAdvertisementEnabled";

    /** WebPA Parameter get Device.DeviceInfo. parameters */
    public static final String WEBPA_PARAM_DEVICE_DEVICEINFO = "Device.DeviceInfo.";

    /**
     * WebPA Parameter get Device.WiFi. parameters
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI = "Device.WiFi.";

    /**
     * WebPA Parameter get Device.MoCA. parameters
     */
    public static final String WEBPA_PARAM_DEVICE_MOCA = "Device.MoCA.";

    /**
     * WebPA Parameter get Device.IP. parameters
     */
    public static final String WEBPA_PARAM_DEVICE_IP = "Device.IP.";

    /**
     * WebPA Parameter get Device.Hosts. parameters
     */
    public static final String WEBPA_PARAM_DEVICE_HOSTS = "Device.Hosts.";

    /** WebPA parameter for WEB-GUI admin page login password */
    public static final String WEBPA_PARAM_GUI_ADMIN_PASSWORD = "Device.Users.User.3.Password";

    /** WebPA parameter for WEB-GUI cusadmin page login password */
    public static final String WEBPA_PARAM_GUI_CUSADMIN_PASSWORD = "Device.Users.User.2.Password";

    /** webpa Parameter to get Gateway IPV4 Address. */
    public static final String WEBPA_PARAM_GATEWAY_IPV4_ADDRESS = "Device.IP.Interface.4.IPv4Address.1.IPAddress";

    /**
     * WebPa Parameter to check the UploadLog status
     */
    public static final String DEVICE_DEVICE_INFO_RDK_LOG_UPLOAD_STATUS = "Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.Logging.xOpsDMLogsUploadStatus";
    /**
     * WebPa Parameter to trigger the Log Upload
     */
    public static final String DEVICE_DEVICE_INFO_RDK_LOG_UPLOAD_NOW_STATUS = "Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.Logging.xOpsDMUploadLogsNow";

    /** WebPA Parameter to enable/disable Secure WebUI Feature */
    public static final String WEBPA_PARAM_TO_GET_AND_SET_SECURE_WEBUI = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.SecureWebUI.Enable";

    /** WebPA Parameter for CDL Status */
    public static final String WEBPA_PARAM_CDL_STATUS = "Device.DeviceInfo.X_RDKCENTRAL-COM_FirmwareDownloadStatus";

    /** WebPa parameter for RFC Feature IDS Enable */
    public static final String DEVICE_DEVICEINFO_RFC_FEATURE_IDS_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.IDS.Enable";

    /** Webpa parameter for IDS scan task */
    public static final String WEBPA_PARAM_IDS_SCAN_TASK = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.IDS.ScanTask";

    /**
     * ENUM WHICH STORES ALL THE REQUIRED WEBPA PARAMS FOR DEVICE MANAGEMENT SERVER
     */
    public static enum DeviceManagementServerParams {
	DEVICE_MANAGEMENT_ENABLE_CWMP("Device.ManagementServer.EnableCWMP", "1"),
	DEVICE_MANAGEMENT_ENABLE_PERIODIC_INFORM("Device.ManagementServer.PeriodicInformEnable", "1"),
	DEVICE_MANAGEMENT_INTERVAL_PERIODIC_INFORM("Device.ManagementServer.PeriodicInformInterval", " "),
	DEVICE_MANAGEMENT_TIME_PERIODIC_INFORM("Device.ManagementServer.PeriodicInformTime", "00:00:00.000Z"),
	DEVICE_MANAGEMENT_ACTIVE_NOTIFICATION_THROTTLE(
		"Device.ManagementServer.DefaultActiveNotificationThrottle",
		" "),
	DEVICE_MANAGEMENT_CWMP_RETRY_MIN_WAIT_INTERVAL("Device.ManagementServer.CWMPRetryMinimumWaitInterval", " "),
	DEVICE_MANAGEMENT_CWMP_RETRY_INTERVAL_MULTIPLIER("Device.ManagementServer.CWMPRetryIntervalMultiplier", " "),
	DEVICE_MANAGEMENT_ALIAS_ADDRESSING("Device.ManagementServer.AliasBasedAddressing", " "),
	DEVICE_MANAGEMENT_PARAMETER_KEY("Device.ManagementServer.ParameterKey", " "),
	DEVICE_MANAGEMENT_AUTO_CREATE_INSTANCES("Device.ManagementServer.AutoCreateInstances", " "),
	DEVICE_MANAGEMENT_INSTANCE_MODE("Device.ManagementServer.InstanceMode", " ");

	private String webpa;
	private String valueToCheck;

	private DeviceManagementServerParams(String webpa, String valueToCheck) {
	    this.webpa = webpa;
	    this.setValueToCheck(valueToCheck);
	}

	/**
	 * @return the valueToCheck
	 */
	public String getValueToCheck() {
	    return valueToCheck;
	}

	/**
	 * @param valueToCheck
	 *            the valueToCheck to set
	 */
	public void setValueToCheck(String valueToCheck) {
	    this.valueToCheck = valueToCheck;
	}

	/**
	 * @return the webpa
	 */
	public String getWebpa() {
	    return webpa;
	}
    }

    /** WebPA Parameter to update the xDNS client MAC Address in Mapping Table */
    public static final String WEBPA_PARAM_XDNS_MAC_ADDR_MAPPING_TABLE = "Device.X_RDKCENTRAL-COM_XDNS.DNSMappingTable.{i}.MacAddress";

    /** WebPA Parameter to update the xDNS client IPv4 address in Mapping Table */
    public static final String WEBPA_PARAM_XDNS_IPV4_MAPPING_TABLE = "Device.X_RDKCENTRAL-COM_XDNS.DNSMappingTable.{i}.DnsIPv4";

    /** WebPA Parameter to update the xDNS client IPv6 address in Mapping Table */
    public static final String WEBPA_PARAM_XDNS_IPV6_MAPPING_TABLE = "Device.X_RDKCENTRAL-COM_XDNS.DNSMappingTable.{i}.DnsIPv6";

    /** WebPA Parameter to update the xDNS client tag name in Mapping Table */
    public static final String WEBPA_PARAM_XDNS_CLIENT_TAG_NAME_MAPPING_TABLE = "Device.X_RDKCENTRAL-COM_XDNS.DNSMappingTable.{i}.Tag";

    /** WebPA parameter for PasswordReset for cusadmin login of lan UI */
    public static final String WEBPA_PARAM_CUSADMIN_PASSWORD_RESET = "Device.Users.User.2.X_RDKCENTRAL-COM_PasswordReset";

    /** Webpa parameter for PasswordReset for admin page */
    public static final String WEBPA_PARAM_PASSWORD_RESET = "Device.Users.User.3.X_RDKCENTRAL-COM_PasswordReset";

    /** WebPA Parameter for retrieving the Host Host name */
    public static final String WEBPA_PARAM_DEVICE_HOST_HOSTNAME = "Device.Hosts.Host.{i}.HostName";

    /** WebPA Parameter for retrieving the Host IPv4 Address */
    public static final String WEBPA_PARAM_DEVICE_HOST_IPV4_ADDRESS = "Device.Hosts.Host.{i}.IPAddress";

    /** webpa parameter to get the DHCPv6 Beginning Address value */
    public static final String WEB_PARAM_DHCPV6_BEGINNING_ADDRESS = "Device.DHCPv6.Server.Pool.1.PrefixRangeBegin";

    /** webpa parameter to get the DHCPv6 Ending Address value */
    public static final String WEB_PARAM_DHCPV6_ENDING_ADDRESS = "Device.DHCPv6.Server.Pool.1.PrefixRangeEnd";

    /** Webpa parameter for Preferred life time value */
    public static final String WEBPA_PARAM_PREFERRED_LIFETIME = "Device.IP.Interface.1.IPv6Prefix.1.X_CISCO_COM_PreferredLifetime";

    /** Webpa parameter for Valid life time value */
    public static final String WEBPA_PARAM_VALID_LIFETIME = "Device.IP.Interface.1.IPv6Prefix.1.X_CISCO_COM_ValidLifetime";

    /** WebPA Parameter for Device Connected */
    public static final String WEBPA_PARAM_DEVICE_CONNECTED = "Device.WiFi.AccessPoint.{i}.X_RDK_deviceConnected";

    /** webpa parameter for Web config RFC enable */
    public static final String WEBPA_PARAM_WEBCONFIG_RFC_ENABLE = "Device.X_RDK_WebConfig.RfcEnable";

    /** WebPA parameter Downstream channel LockStatus value */
    public static final String WEBPA_PARAM_DOWNSTREAM_CHANNEL_LOCKSTATUS = "Device.X_CISCO_COM_CableModem.DownstreamChannel.{i}.LockStatus";

    /** WebPA parameter Downstream channel PowerLevel value */
    public static final String WEBPA_PARAM_DOWNSTREAM_CHANNEL_POWERLEVEL = "Device.X_CISCO_COM_CableModem.DownstreamChannel.{i}.PowerLevel";

    /** WebPA parameter Upstream channel LockStatus value */
    public static final String WEBPA_PARAM_UPSTREAM_CHANNEL_LOCKSTATUS = "Device.X_CISCO_COM_CableModem.UpstreamChannel.{i}.LockStatus";

    /** WebPA parameter Upstream channel frequency value */
    public static final String WEBPA_PARAM_UPSTREAM_CHANNEL_FREQUENCY = "Device.X_CISCO_COM_CableModem.UpstreamChannel.{i}.Frequency";

    /** WebPA parameter Upstream channel SymbolRate value */
    public static final String WEBPA_PARAM_UPSTREAM_CHANNEL_SYMBOLRATE = "Device.X_CISCO_COM_CableModem.UpstreamChannel.{i}.SymbolRate";

    /** WebPA parameter Upstream channel Modulation value */
    public static final String WEBPA_PARAM_UPSTREAM_CHANNEL_MODULATION = "Device.X_CISCO_COM_CableModem.UpstreamChannel.{i}.Modulation";

    /** WebPA parameter Upstream channel type value */
    public static final String WEBPA_PARAM_UPSTREAM_CHANNEL_CHANNELTYPE = "Device.X_CISCO_COM_CableModem.UpstreamChannel.{i}.ChannelType";

    /**
     * WebPA Parameter to CHECK FILTERINGMODE FOR PRIVATE WIFI 2.4GHZ.
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_COMCAST_COM_MAC_FILTERINGMODE = "Device.WiFi.AccessPoint.10003.X_COMCAST-COM_MAC_FilteringMode";

    /**
     * WebPA Parameter to CHECK FILTERINGMODE FOR PRIVATE WIFI 5GHZ.
     */
    public static final String WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_COMCAST_COM_MAC_FILTERINGMODE = "Device.WiFi.AccessPoint.10103.X_COMCAST-COM_MAC_FilteringMode";

    /** WebPA parameter to enable memory swap */
    public static final String WEBPA_PARAM_TO_ENABLE_MEMORY_SWAP = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.MEMSWAP.Enable";

    /** Bootstrap parameters remove list in enum */
    public enum BootStrapParametersRfcList {
	FOOTER_PARTNER_LINK("Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.Footer.PartnerLink"),
	FOOTER_USER_GUIDE_LINK("Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.Footer.UserGuideLink"),
	FOOTER_CUSTERCENTRAL_LINK(
		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.Footer.CustomerCentralLink"),
	FOOTER_USERGUIDE_TEXT("Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.Footer.UserGuideText"),
	FOOTER_CUSTOMERCENTRAL_TEXT(
		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.Footer.CustomerCentralText"),
	CONNECTION_MSO_MENU("Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.Connection.MSOmenu"),
	CONNECTION_MSO_INFO("Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.Connection.MSOinfo"),
	CONNECTION_STATUS_TITLE(
		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.Connection.StatusTitle"),
	CONNECTION_STATUS_INFO("Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.Connection.StatusInfo"),
	CONNECTIVITY_TEST_URL(
		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.NetworkDiagnosticTools.ConnectivityTestURL"),
	PARTNER_HELP_LINK(
		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.WiFiPersonalization.PartnerHelpLink"),
	DEFAULT_LANGUAGE("Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.DefaultLanguage"),
	PAUSE_SCREEN_FILE_LOCATION("Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.PauseScreenFileLocation");

	private String parameterName;

	BootStrapParametersRfcList(String paramter) {
	    this.parameterName = paramter;

	}

	public String getParameterName() {
	    return parameterName;
	}

	public void setParameterName(String parameterName) {
	    this.parameterName = parameterName;
	}

    }

    /** Bootstrap parameters remove list in enum */
    public enum BootStrapParametersRemoveList {
	MTA_STARTUP_IP_MODE("Device.X_RDKCENTRAL-COM_EthernetWAN_MTA.StartupIPMode"),
	MTA_IPV4_PRIMARY_DHCP("Device.X_RDKCENTRAL-COM_EthernetWAN_MTA.IPv4PrimaryDhcpServerOptions"),
	MTA_IPV4_SEC_DHCP("Device.X_RDKCENTRAL-COM_EthernetWAN_MTA.IPv4SecondaryDhcpServerOptions"),
	MTA_IPV6_PRIMARY_DHCP("Device.X_RDKCENTRAL-COM_EthernetWAN_MTA.IPv6PrimaryDhcpServerOptions"),
	NTP_SERVER_1("Device.Time.NTPServer1"),
	NTP_SERVER_2("Device.Time.NTPServer2"),
	NTP_SERVER_3("Device.Time.NTPServer3"),
	NTP_SERVER_4("Device.Time.NTPServer4"),
	NTP_SERVER_5("Device.Time.NTPServer5");

	private String parameterName;

	BootStrapParametersRemoveList(String paramter) {
	    this.parameterName = paramter;

	}

	public String getParameterName() {
	    return parameterName;
	}

	public void setParameterName(String parameterName) {
	    this.parameterName = parameterName;
	}

    }

    /** WebPA Parameter to get ecmMAC */
    public static final String WEBPA_PARAM_TO_GET_ECM_MAC = "Device.IP.Diagnostics.X_RDKCENTRAL-COM_PingTest.ecmMAC";

    /** WebPA Parameter to get DeviceID */
    public static final String WEBPA_PARAM_TO_GET_DEVICE_ID = "Device.IP.Diagnostics.X_RDKCENTRAL-COM_PingTest.DeviceID";

    /** WebPA Parameter to get device model */
    public static final String WEBPA_PARAM_TO_GET_DEVICE_MODEL = "Device.IP.Diagnostics.X_RDKCENTRAL-COM_PingTest.DeviceModel";

    /** WebPA Parameter to get number of repetitions */
    public static final String WEBPA_PARAM_TO_GET_NUMBER_OF_REPETITIONS = "Device.IP.Diagnostics.IPPing.NumberOfRepetitions";

    /** WebPA Parameter to get timeout */
    public static final String WEBPA_PARAM_TO_GET_TIMEOUT = "Device.IP.Diagnostics.IPPing.Timeout";

    /** WebPA Parameter to get average ping response time */
    public static final String WEBPA_PARAM_TO_GET_AVERAGE_RESPONSE_TIME = "Device.IP.Diagnostics.IPPing.AverageResponseTime";

    /** WebPA Parameter to set interface */
    public static final String WEBPA_PARAM_TO_SET_INTERFACE = "Device.IP.Diagnostics.IPPing.Interface";

    /** WebPA Parameter to set host address */
    public static final String WEBPA_PARAM_TO_SET_HOST_ADDRESS = "Device.IP.Diagnostics.IPPing.Host";

    /** WebPA Parameter to trigger pingtest */
    public static final String WEBPA_PARAM_TO_TRIGGER_PINGTEST = "Device.IP.Diagnostics.X_RDKCENTRAL-COM_PingTest.Run";

    /** WebPA Parameter to get Pingtest Diagnostics State */
    public static final String WEBPA_PARAM_TO_GET_DIAGNOSTICS_STATE = "Device.IP.Diagnostics.IPPing.DiagnosticsState";

    /** WebPA Parameter to get PartnerID */
    public static final String WEBPA_PARAM_TO_GET_PARTNERID = "Device.IP.Diagnostics.X_RDKCENTRAL-COM_PingTest.PartnerID";

    public static final String TR181_PARAM_NONROOT_SUPPORT_BLOCKLIST = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.NonRootSupport.Blocklist";

    /** Webpa Param for Device ethernet interface Broadcast packets received */
    public static final String TR181_PARAM_TO_GET_ACCOUNT_ID = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.AccountInfo.AccountID";

    /**
     * WebPa parameter for Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.ForwardSSH.Enable
     */
    public static final String WEBPA_PARAM_FEATURE_FORWARD_SSH = "Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.ForwardSSH.Enable";

    /**
     * WebPA parameter array for SSID names
     */
    public static final String[] PARAMETERS_FOR_SSID_NAMES = new String[] {
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_NAME,
	    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_NAME };

    /** WebPA Parameter to enable ax for 2.4 ghz operating standards */
    public static final String WEBPA_PARAM_FOR_2_4_GHZ_AX_ENABLE = "Device.WiFi.2G80211axEnable";

    /** WebPA Parameter for DOSCIS OFDM */
    public static final String WEBPA_PARAM_DOSCIS_OFDM = "Device.X_RDKCENTRAL-COM_CableModem.DsOfdmChan.";

    /** WebPA Parameter for DOSCIS OFDM ChannelID */
    public static final String WEBPA_PARAM_DOSCIS_OFDM_CHANNELID = "Device.X_RDKCENTRAL-COM_CableModem.DsOfdmChan.{i}.ChannelID";

    /** WebPA Parameter for DOSCIS OFDM SNR level */
    public static final String WEBPA_PARAM_DOSCIS_OFDM_SNR_LEVEL = "Device.X_RDKCENTRAL-COM_CableModem.DsOfdmChan.{i}.SNRLevel";

    /** WebPA Parameter for DOSCIS OFDM Power level */
    public static final String WEBPA_PARAM_DOSCIS_OFDM_POWER_LEVEL = "Device.X_RDKCENTRAL-COM_CableModem.DsOfdmChan.{i}.PowerLevel";

    /** WebPA Parameter to enable/disable presence detect */
    public static final String WEBPA_PARAM_TO_GET_AND_SET_PRESENCE_DETECT_STATUS = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.PresenceDetect.Enable";

    public static final String TR69_PARAM_DEVICE_RFC_FEATURE_ENCRYPT_CLOUD_UPLOAD_ENABLE = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.EncryptCloudUpload.Enable";

    /** Webpa parameter for Docsis Poll Time */
    public static final String WEBPA_PARAM_DOCSIS_POLL_TIME = "Device.DeviceInfo.X_RDKCENTRAL-COM_ConfigureDocsicPollTime";

    /** webpa parameter to get the value of MoCA logging telemetry status */
    public static final String WEBPA_PARAM_FOR_MOCA_LOGGING_TELEMETRY = "Device.DeviceInfo.X_RDKCENTRAL-COM_xOpsDeviceMgmt.Logging.xOpsDMMoCALogEnabled";

    /**
     * WebPA parameter representation for Device.Time.
     */
    public static final String WEBPA_PARAM_DEVICE_TIME = "Device.Time.";

    /** webpa Parameter for MoCA interface force enable . */
    public static final String WEBPA_PARAM_FOR_MOCA_INTERFACE_FORCE_ENABLE = "Device.MoCA.X_RDKCENTRAL-COM_ForceEnable";

    /** WebPA Parameter for IPV4 No of Table Entries */
    public static final String WEBPA_PARAM_IPV4_OF_TABLE_ENTRIES = "Device.SelfHeal.ConnectivityTest.PingServerList.IPv4PingServerTableNumberOfEntries";

    /**
     * WebPA Parameter to represent Number of allowed clients for 2.4 GHz Private SSID
     */
    public static final String WEBPA_PARAM_ALLOWED_CLIENT_LIMIT_PRIVATE_SSID_2_4 = "Device.WiFi.AccessPoint.10001.X_CISCO_COM_BssMaxNumSta";

    /**
     * WebPA Parameter to represent Number of allowed clients for 5 GHz Private SSID
     */
    public static final String WEBPA_PARAM_ALLOWED_CLIENT_LIMIT_PRIVATE_SSID_5 = "Device.WiFi.AccessPoint.10101.X_CISCO_COM_BssMaxNumSta";

    /**
     * Webpa parameter for Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.IPv6onMoCA.Enable Status
     */
    public static final String WEBPA_PARAM_DEVICE_RFC_FEATURE_IPV6ONMOCA_ENABLE_STATUS = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.IPv6onMoCA.Enable";

    /** Webpa param for syndication flow control enable */
    public static final String WEBPA_PARAM_SYNDICATION_FLOW_CONTROL = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.SyndicationFlowControl.Enable";

    /**
     * WebpaP Param for syndication forward mark
     */
    public static final String WEBPA_PARAM_SYNDICATION_INITIAL_FORWARD_MARK = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.SyndicationFlowControl.InitialForwardedMark";

    /**
     * Dscp forward mark
     */
    public static final String DSCP_FORWARD_MARK = "cs0";
    /**
     * Dscp output mark
     */
    public static final String DSCP_OUTPUT_MARK = "af22";

    /**
     * WebpaP Param for syndication output mark
     */
    public static final String WEBPA_PARAM_SYNDICATION_INITIAL_OUTPUT_MARK = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.SyndicationFlowControl.InitialOutputMark";

    /**
     * Map for comcast dscp markings
     */
    public static final Map<String, String> DSCP_VALUES = new HashMap<String, String>() {
	{
	    put(BroadBandWebPaConstants.WEBPA_PARAM_SYNDICATION_INITIAL_FORWARD_MARK, DSCP_FORWARD_MARK);
	    put(WEBPA_PARAM_SYNDICATION_INITIAL_OUTPUT_MARK, DSCP_OUTPUT_MARK);

	}
    };

    /** webpa parameter to enable parental control-managed services */
    public static final String WEBPA_PARAM_TO_ENABLE_PARENTAL_CONTROLS_MANAGED_SERVICES = "Device.X_Comcast_com_ParentalControl.ManagedServices.Enable";

    /** webpa parameter to add a parental control-managed service rule */
    public static final String WEBPA_PARAM_TO_ADD_A_PARENTAL_CONTROL_MANAGED_SERVICES_RULE = "Device.X_Comcast_com_ParentalControl.ManagedServices.Service.";

    /** WebPA parameter name for enabling IOT service */
    public static final String WEBPA_ENABLE_IOT = "Device.IoT.X_RDKCENTRAL-COM_EnableIoT";
    
    public enum webPAParametersForSyndication {
    	DEVICE_RDKB_UI_BRANDING_FOOTER_PARTNERLINK(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.Footer.PartnerLink"),
    	DEVICE_RDKB_UI_BRANDING_FOOTER_USERGUIDELINK(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.Footer.UserGuideLink"),
    	DEVICE_RDKB_UI_BRANDING_FOOTER_CUSTOMERCENTRAL_LINK(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.Footer.CustomerCentralLink"),
    	DEVICE_RDKB_UI_BRANDING_CONNECTION_MSOMENU(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.Connection.MSOmenu"),
    	DEVICE_RDKB_UI_BRANDING_CONNECTION_MSOINFO(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.Connection.MSOinfo"),
    	DEVICE_RDKB_UI_BRANDING_CONNECTION_STATUSTITLE(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.Connection.StatusTitle"),
    	DEVICE_RDKB_UI_BRANDING_CONNECTION_STATUSINFO(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.Connection.StatusInfo"),
    	DEVICE_RDKB_UI_BRANDING_NETWORKDIAGNOSTICTOOLS_CONNECTIVITYTESTURL(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.NetworkDiagnosticTools.ConnectivityTestURL"),
    	DEVICE_RDKB_UI_BRANDING_WIFIPERSONALIZATION_SUPPORT(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.WiFiPersonalization.Support"),
    	DEVICE_RDKB_UI_BRANDING_WIFIPERSONALIZATION_PARTNERHELPLINK(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.WiFiPersonalization.PartnerHelpLink"),
    	DEVICE_RDKB_UI_BRANDING_WIFIPERSONALIZATION_SMSSUPPORT(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.WiFiPersonalization.SMSsupport"),
    	DEVICE_RDKB_UI_BRANDING_WIFIPERSONALIZATION_MYACCOUNTAPPSUPPORT(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.WiFiPersonalization.MyAccountAppSupport"),
    	DEVICE_RDKB_UI_BRANDING_DEFAULTADMINIP(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.DefaultAdminIP"),
    	DEVICE_RDKB_UI_BRANDING_DEFAULTLOCALIPV4SUBNETRANGE(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.DefaultLocalIPv4SubnetRange"),
    	DEVICE_RDKB_UI_BRANDING_LOCALUI_DEFAULTLOGINUSERNAME(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.LocalUI.DefaultLoginUsername"),
    	DEVICE_RDKB_UI_BRANDING_FOOTER_PARTNERTEXT(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.Footer.PartnerText"),
    	DEVICE_RDKB_UI_BRANDING_FOOTER_USERGUIDETEXT(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.Footer.UserGuideText"),
    	DEVICE_RDKB_UI_BRANDING_FOOTER_CUSTOMERCENTRALTEXT(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.Footer.CustomerCentralText"),
    	DEVICE_RDKB_UI_BRANDING_LOCALUI_MSOLOGO(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.LocalUI.MSOLogo"),
    	DEVICE_RDKB_UI_BRANDING_WIFIPERSONALIZATION_MSOLOGO(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.WiFiPersonalization.MSOLogo"),
    	DEVICE_RDKB_UI_BRANDING_LOCALUI_DEFAULT_LOGIN_PASSWORD(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.LocalUI.DefaultLoginPassword"),
    	DEVICE_RDKB_UI_BRANDING_CLOUDUI_BRANDNAME(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.CloudUI.brandname"),
    	DEVICE_RDKB_UI_BRANDING_CLOUDUI_PRODUCTNAME(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.CloudUI.productname"),
    	DEVICE_RDKB_UI_BRANDING_CLOUDUI_LINK(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.CloudUI.link"),
    	DEVICE_RDKB_UI_BRANDING_DEFAULTLANGUAGE(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.DefaultLanguage"),
    	DEVICE_RDKB_UI_BRANDING_HELPTIP_NETWORKNAME(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.HelpTip.NetworkName"),
    	DEVICE_RDKB_UI_BRANDING_WIFIPERSONALIZATION_TITLE(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.WiFiPersonalization.Title"),
    	DEVICE_RDKB_UI_BRANDING_WIFIPERSONALIZATION_WELCOME_MESSAGE(
    		"Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.RDKB_UIBranding.WiFiPersonalization.WelcomeMessage");

    	String paramName;

    	public String getParamName() {
    	    return paramName;
    	}

    	public void setParamName(String paramName) {
    	    this.paramName = paramName;
    	}

    	webPAParametersForSyndication(String name) {
    	    this.paramName = name;
    	}

        }
        
        /** String to RFC Feature SyndicationFlowControl parameter */
        public static final String PARAMETER_RFC_FEATURE_SYNDICATIONFLOWCONTROL = "Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.SyndicationFlowControl";

        /** String to Syndication parameter */
        public static final String PARAMETER_SYNDICATION = "Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication";

        /** String to ManagementServer parameter */
        public static final String PARAMETER_MANAGEMENTSERVERL = "Device.ManagementServer";
        
        /** webpa parameter for Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.CMVoiceImageSelect */
        public static final String WEBPA_PARAM_DEVICE_CMVOICE_IMAGE_SELECT = "Device.DeviceInfo.X_RDKCENTRAL-COM_Syndication.CMVoiceImageSelect";
        
        /** webpa parameter to Check Web config server URL */
        public static final String WEBPA_PARAM_WEBCONFIG_URL = "Device.X_RDK_WebConfig.URL";
        
        /** webpa parameter to Check Web config server Supplementary Service Urls for Telemetry */
        public static final String WEBPA_PARAM_WEBCONFIG_SUPPLEMENTARYSERVICE_TELEMETRY = "Device.X_RDK_WebConfig.SupplementaryServiceUrls.Telemetry";
        
        /** WebPA Parameter to retrieve the CM Mac Address */
        public static final String WEBPA_PARAM_CABLE_MODEM_MAC_ADDRESS = "Device.X_CISCO_COM_CableModem.MACAddress";
        
        

}

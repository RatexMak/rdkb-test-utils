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
package com.automatics.rdkb.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Dut;
import com.automatics.rdkb.BroadBandDeviceSsidInfo;
import com.automatics.rdkb.BroadBandDeviceStatus;
import com.automatics.rdkb.WiFiSsidConfigStatus;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.wifi.BroadBandWiFiUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.webpa.WebPaParameter;

public class BroadBandRegressionQuickTestUtils {
    
    /** SLF4J logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandRegressionQuickTestUtils.class);
    
    /**
     * Utility method to retrieve Device info to prepare for the device to test
     * 
     * @param device
     *            The Dut to be validated.
     * @return The {@link BroadBandDeviceInfo}
     * @refactor Govardhan
     */
    public static BroadBandDeviceSsidInfo getBroadBandDeviceSsidInfo(Dut device) {

	String macAddressWithoutColon = CommonUtils.getDeviceId(device);

	BroadBandDeviceSsidInfo deviceInfo = new BroadBandDeviceSsidInfo();
	
	String devicePassword = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.BROAD_BAND_DEVICE_SSID_PASSWORD);

	deviceInfo.setPrivate2ghzSsidName(("rdkb-" + macAddressWithoutColon + "-2.4-GHz").toLowerCase());
	deviceInfo.setPrivate5ghzSsidName(("rdkb-" + macAddressWithoutColon + "-5-GHz").toLowerCase());
	deviceInfo.setXfinityOpen2GhzSsidName(("xfinity-" + macAddressWithoutColon + "-2.4-GHz").toLowerCase());
	deviceInfo.setXfinityOpen5GhzSsidName(("xfinity-" + macAddressWithoutColon + "-5-GHz").toLowerCase());
	deviceInfo.setLnf2GhzSsidName("lnf-" + macAddressWithoutColon + "-2.4-GHz");
	deviceInfo.setLnf5GhzSsidName("lnf-" + macAddressWithoutColon + "-5-GHz");
	deviceInfo.setXfinitySecure5GhzSsidName("XFINITY-" + macAddressWithoutColon + "-5-GHz");

	deviceInfo.setPrivate2ghzSsidPwd(devicePassword);
	deviceInfo.setPrivate5ghzSsidPwd(devicePassword);
	deviceInfo.setLnf5GhzSsidPwd(devicePassword);
	deviceInfo.setLnf2GhzSsidPwd(devicePassword);

	deviceInfo.setGwIpAddr(BroadBandTestConstants.LAN_LOCAL_IP);
	deviceInfo.setLanSubnetMask(BroadBandTestConstants.LAN_SUBNET_MASK_DEFAULT);
	deviceInfo.setDhcpMinAddr(BroadBandTestConstants.LAN_START_IP_ADDRESS);
	deviceInfo.setDhcpMaxAddr(BroadBandTestConstants.DHCP_END_IP_ADDRESS_DEFAULT);

	return deviceInfo;
    }
    
    /**
     * Utility Method to set all the necessary precondition before quick test
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param deviceInfo
     *            The {@link BroadBandDeviceInfo} instance.
     * @return Map of different SSID configuration details.
     * @refactor Govardhan
     */
    public static Map<String, WiFiSsidConfigStatus> setRequiredPreconditionForQuickTest(Dut device, AutomaticsTapApi tapEnv,
	    BroadBandDeviceSsidInfo deviceInfo, boolean isCiquickTest) {

	boolean isBusinessGateway = DeviceModeHandler.isBusinessClassDevice(device);

	BroadBandDeviceStatus deviceStatus = BroadBandCommonUtils.getDeviceStatusViaWebPaOrDmcli(device, tapEnv,
		isCiquickTest);
	
	List<WebPaParameter> genericComponents = new ArrayList<WebPaParameter>();
	List<WebPaParameter> wifiComponents = new ArrayList<WebPaParameter>();
	List<WebPaParameter> rdmComponents = new ArrayList<WebPaParameter>();

	if (!deviceStatus.isRouterModeStatus()) {
	    BroadBandCommonUtils.setDeviceInRouterModeStatusUsingWebPaOrDmcli(device, tapEnv);
	    deviceStatus = BroadBandCommonUtils.getDeviceStatusViaWebPaOrDmcli(device, tapEnv, isCiquickTest);
	}

	Map<String, String> maintenanceWindow = BroadBandCommonUtils.calculateFirmwareUpgradeMaintenanceWindow(tapEnv,
		device);
	String firmwareUpgradeStartTime = maintenanceWindow.get("main_window_start_time");
	String firmwareUpgradeEndTime = maintenanceWindow.get("main_window_end_time");

	// Some cases - Firmware upgrade start time and end time not getting updated.
	tapEnv.executeCommandUsingSsh(device,
		"touch /nvram/.FirmwareUpgradeStartTime;touch /nvram/.FirmwareUpgradeEndTime;echo \""
			+ firmwareUpgradeStartTime + "\" > /nvram/.FirmwareUpgradeStartTime;echo \""
			+ firmwareUpgradeEndTime + "\" > /nvram/.FirmwareUpgradeEndTime");

	if (!isBusinessGateway) {
	    WebPaParameter differFirmDwReboot = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_DEFER_FIRMWARE_DOWNLOAD_REBOOT,
		    BroadBandTestConstants.STRING_VALUE_60, WebPaDataTypes.INTEGER.getValue());
	    rdmComponents.add(differFirmDwReboot);


	    WebPaParameter lnfSsidName2ghz = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4GHZ_LNF_SSID, deviceInfo.getLnf2GhzSsidName(),
		    WebPaDataTypes.STRING.getValue());
	    wifiComponents.add(lnfSsidName2ghz);

	    WebPaParameter lnfSsidName5ghz = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_LNF_SSID, deviceInfo.getLnf5GhzSsidName(),
		    WebPaDataTypes.STRING.getValue());
	    wifiComponents.add(lnfSsidName5ghz);

	    WebPaParameter lnfSsid2GhzPassword = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_10006_SECURITY_KEYPASSPHRASE,
		    deviceInfo.getLnf2GhzSsidPwd(), WebPaDataTypes.STRING.getValue());
	    wifiComponents.add(lnfSsid2GhzPassword);

	    WebPaParameter lnfSsid5GhzPassword = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_10106_SECURITY_KEYPASSPHRASE,
		    deviceInfo.getLnf5GhzSsidPwd(), WebPaDataTypes.STRING.getValue());
	    wifiComponents.add(lnfSsid5GhzPassword);

	    if (!isCiquickTest) {
		    WebPaParameter safeBrowsingEnable = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			    BroadBandWebPaConstants.WEBPA_PARAM_ADVANCED_SECURITY_SAFE_BROWSING,
			    BroadBandTestConstants.TRUE, WebPaDataTypes.BOOLEAN.getValue());
		    rdmComponents.add(safeBrowsingEnable);

		    WebPaParameter softFlowedEnable = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			    BroadBandWebPaConstants.WEBPA_PARAM_ADVANCED_SECURITY_SOFTFLOWD,
			    BroadBandTestConstants.TRUE, WebPaDataTypes.BOOLEAN.getValue());
		    rdmComponents.add(softFlowedEnable);
	    }

	    if (!deviceStatus.isAkerEnabled()) {

		WebPaParameter akerEnabled = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_AKER_ENABLE, BroadBandTestConstants.TRUE,
			WebPaDataTypes.BOOLEAN.getValue());
		genericComponents.add(akerEnabled);
	    }

	    if (!isCiquickTest) {
		if (!deviceStatus.isMeshStatus()) {
		    WebPaParameter meshStatus = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_MESH_ENABLE,
			    BroadBandTestConstants.TRUE, WebPaDataTypes.BOOLEAN.getValue());
		    rdmComponents.add(meshStatus);
		}
	    }

	    if (!deviceStatus.isMocaStatus()) {

		WebPaParameter mocaStatus = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			BroadBandWebPaConstants.WEBPA_PARAM_FOR_MOCA_INTERFACE_ENABLE, BroadBandTestConstants.TRUE,
			WebPaDataTypes.BOOLEAN.getValue());
		genericComponents.add(mocaStatus);
	    }

	    if (!deviceStatus.isCloudUiEnabled()) {
		WebPaParameter cloudUiStatus = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_CLOUD_UI, BroadBandTestConstants.TRUE,
			WebPaDataTypes.BOOLEAN.getValue());
		genericComponents.add(cloudUiStatus);
	    }

	    if (!deviceStatus.isXdnsEnabled()) {
		WebPaParameter defaultDeviceDnsIpv4 = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			BroadBandWebPaConstants.WEBPA_PARAM_TO_UPDATE_GLOBAL_XDNS_IPV4,
			BroadBandTestConstants.STRING_DEFAULT_GLOBAL_DNS_IPV4_VALUE, WebPaDataTypes.STRING.getValue());
		genericComponents.add(defaultDeviceDnsIpv4);
		WebPaParameter defaultDeviceDnsIpv6 = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			BroadBandWebPaConstants.WEBPA_PARAM_TO_UPDATE_GLOBAL_XDNS_IPV6,
			BroadBandTestConstants.STRING_DEFAULT_GLOBAL_DNS_IPV6_VALUE, WebPaDataTypes.STRING.getValue());
		genericComponents.add(defaultDeviceDnsIpv6);

		WebPaParameter defaultDeviceDnsTag = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			"Device.X_RDKCENTRAL-COM_XDNS.DefaultDeviceTag", "default_dns_tag",
			WebPaDataTypes.STRING.getValue());
		genericComponents.add(defaultDeviceDnsTag);

		WebPaParameter enableXdns = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			BroadBandWebPaConstants.WEBPA_PARAM_TO_GET_XDNS_FEATURE_STATUS, BroadBandTestConstants.TRUE,
			WebPaDataTypes.BOOLEAN.getValue());
		genericComponents.add(enableXdns);
	    }
	}

	if (!deviceStatus.isRadio2gAutoChanStatus()) {
	    WebPaParameter wifiRadio2ghz = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_2GHZ,
		    BroadBandTestConstants.TRUE, WebPaDataTypes.BOOLEAN.getValue());
	    wifiComponents.add(wifiRadio2ghz);
	}

	if (!deviceStatus.isRadio5gAutoChanStatus()) {
	    WebPaParameter wifiRadio5ghz = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_5GHZ,
		    BroadBandTestConstants.TRUE, WebPaDataTypes.BOOLEAN.getValue());
	    wifiComponents.add(wifiRadio5ghz);
	}

	WebPaParameter captivePortalEnabled = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		BroadBandWebPaConstants.WEBPA_PARAM_CAPTIVE_PORTAL_ENABLE, BroadBandTestConstants.FALSE,
		WebPaDataTypes.BOOLEAN.getValue());
	genericComponents.add(captivePortalEnabled);

	WebPaParameter configureWifi = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_INFO_RDK_CENTRAL_CONFIGURE_WIFI,
		BroadBandTestConstants.FALSE, WebPaDataTypes.BOOLEAN.getValue());
	genericComponents.add(configureWifi);

	if (deviceStatus.isCodeBigEnabled()) {
	    WebPaParameter disableCodeBig = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_CODEBIG_FIRST_ENABLE, BroadBandTestConstants.FALSE,
		    WebPaDataTypes.BOOLEAN.getValue());
	    rdmComponents.add(disableCodeBig);
	}
	WebPaParameter fwUpgradeStartTime = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		BroadBandWebPaConstants.WEBPA_COMMAND_MAINTENANCE_WINDOW_START_TIME, firmwareUpgradeStartTime,
		WebPaDataTypes.STRING.getValue());
	rdmComponents.add(fwUpgradeStartTime);

	WebPaParameter fwUpgradeEndTime = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		BroadBandWebPaConstants.WEBPA_COMMAND_MAINTENANCE_WINDOW_END_TIME, firmwareUpgradeEndTime,
		WebPaDataTypes.STRING.getValue());
	rdmComponents.add(fwUpgradeEndTime);

	WebPaParameter privateSsidName2ghz = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_NAME,
		deviceInfo.getPrivate2ghzSsidName(), WebPaDataTypes.STRING.getValue());
	wifiComponents.add(privateSsidName2ghz);

	WebPaParameter privateSsidName5ghz = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_NAME,
		deviceInfo.getPrivate5ghzSsidName(), WebPaDataTypes.STRING.getValue());
	wifiComponents.add(privateSsidName5ghz);

	WebPaParameter privateSsid2GhzPassword = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_PRIVATE_SSID_2GHZ_PASSPHRASE,
		deviceInfo.getPrivate2ghzSsidPwd(), WebPaDataTypes.STRING.getValue());
	wifiComponents.add(privateSsid2GhzPassword);

	WebPaParameter privateSsid5GhzPassword = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_PRIVATE_SSID_5GHZ_PASSPHRASE,
		deviceInfo.getPrivate5ghzSsidPwd(), WebPaDataTypes.STRING.getValue());
	wifiComponents.add(privateSsid5GhzPassword);

	WebPaParameter lanIpAddress = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_LANIP_ADDRESS, deviceInfo.getGwIpAddr(),
		WebPaDataTypes.STRING.getValue());
	genericComponents.add(lanIpAddress);

	WebPaParameter lanSubnetAddress = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_LAN_SUBNET, deviceInfo.getLanSubnetMask(),
		WebPaDataTypes.STRING.getValue());
	genericComponents.add(lanSubnetAddress);

	WebPaParameter dhcpMaxAddress = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_DHCP_MAXADDRESS, deviceInfo.getDhcpMaxAddr(),
		WebPaDataTypes.STRING.getValue());
	genericComponents.add(dhcpMaxAddress);

	WebPaParameter dhcpMinAddress = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_DHCP_MINADDRESS, deviceInfo.getDhcpMinAddr(),
		WebPaDataTypes.STRING.getValue());
	genericComponents.add(dhcpMinAddress);

	if ((!deviceStatus.isXfinityWiFi2GhzOpenStatus() || !deviceStatus.isXfinityWiFi5GhzOpenStatus()
		|| !deviceStatus.isXfinityWiFi5GhzSecureStatus())) {

	    WebPaParameter ssid_name_2ghz = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PUBLIC_SSID,
		    deviceInfo.getXfinityOpen2GhzSsidName(), WebPaDataTypes.STRING.getValue());
	    wifiComponents.add(ssid_name_2ghz);

	    WebPaParameter ssid_name_5ghz = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PUBLIC_SSID,
		    deviceInfo.getXfinityOpen5GhzSsidName(), WebPaDataTypes.STRING.getValue());
	    wifiComponents.add(ssid_name_5ghz);

	    WebPaParameter dhcpMarkPolicy = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_DSCP_MARK_POLICY, BroadBandTestConstants.DSCP_MARK_POLICY,
		    WebPaDataTypes.UNSIGNED_INT.getValue());
	    wifiComponents.add(dhcpMarkPolicy);

	    WebPaParameter primaryRemoteEndpoint = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_PRIMARY_REMOTE_ENDPOINT,
		    BroadBandTestConstants.PRIMARY_REMOTE_ENDPOINT, WebPaDataTypes.STRING.getValue());
	    wifiComponents.add(primaryRemoteEndpoint);

	    WebPaParameter secondaryRemoteEndpoint = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_SECONDARY_REMOTE_ENDPOINT,
		    BroadBandTestConstants.SECONDARY_REMOTE_ENDPOINT, WebPaDataTypes.STRING.getValue());
	    wifiComponents.add(secondaryRemoteEndpoint);

	    WebPaParameter ssidAdvEnable_2ghz = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_Device_WiFi_AccessPoint_10003_SSIDAdvertisementEnabled,
		    BroadBandTestConstants.TRUE, WebPaDataTypes.BOOLEAN.getValue());
	    wifiComponents.add(ssidAdvEnable_2ghz);

	    WebPaParameter ssidAdvEnable_5ghz = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_Device_WiFi_AccessPoint_10103_SSIDAdvertisementEnabled,
		    BroadBandTestConstants.TRUE, WebPaDataTypes.BOOLEAN.getValue());
	    wifiComponents.add(ssidAdvEnable_5ghz);
	    if (!isBusinessGateway) {

		WebPaParameter xfinitySecureSsid = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			"Device.WiFi.SSID.10105.SSID", deviceInfo.getXfinitySecure5GhzSsidName(),
			WebPaDataTypes.STRING.getValue());
		wifiComponents.add(xfinitySecureSsid);

		WebPaParameter xfinitySecureAdvStatus = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			"Device.WiFi.AccessPoint.10105.SSIDAdvertisementEnabled", BroadBandTestConstants.TRUE,
			WebPaDataTypes.BOOLEAN.getValue());
		wifiComponents.add(xfinitySecureAdvStatus);

		WebPaParameter xfinitySecureSecurityMode = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			"Device.WiFi.AccessPoint.10105.Security.ModeEnabled",
			BroadBandTestConstants.SECURITY_MODE_WPA2_ENTERPRISE, WebPaDataTypes.STRING.getValue());
		wifiComponents.add(xfinitySecureSecurityMode);

		WebPaParameter xfinitySecureEncryptionMethod = BroadBandWebPaUtils
			.generateWebpaParameterWithValueAndType(
				"Device.WiFi.AccessPoint.10105.Security.X_CISCO_COM_EncryptionMethod", "AES",
				WebPaDataTypes.STRING.getValue());
		wifiComponents.add(xfinitySecureEncryptionMethod);

		WebPaParameter bssMaxNumSta = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			BroadBandWebPaConstants.WEBPA_PARAM_ALLOWED_CLIENT_LIMIT_SECURE_SSID_5, "5",
			WebPaDataTypes.UNSIGNED_INT.getValue());
		wifiComponents.add(bssMaxNumSta);

		WebPaParameter radiusServerIpAddr = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			"Device.WiFi.AccessPoint.10105.Security.RadiusServerIPAddr", "96.115.128.204",
			WebPaDataTypes.STRING.getValue());
		wifiComponents.add(radiusServerIpAddr);

		WebPaParameter secondaryRadiusServerIpAddr = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			"Device.WiFi.AccessPoint.10105.Security.SecondaryRadiusServerIPAddr", "96.115.128.204",
			WebPaDataTypes.STRING.getValue());
		wifiComponents.add(secondaryRadiusServerIpAddr);

		WebPaParameter radiusServerPort = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			"Device.WiFi.AccessPoint.10105.Security.RadiusServerPort", "1812",
			WebPaDataTypes.INTEGER.getValue());
		wifiComponents.add(radiusServerPort);

		WebPaParameter secondayRadiusServerPort = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			"Device.WiFi.AccessPoint.10105.Security.SecondaryRadiusServerPort", "1812",
			WebPaDataTypes.INTEGER.getValue());
		wifiComponents.add(secondayRadiusServerPort);

		WebPaParameter radiusServerSecret = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			"Device.WiFi.AccessPoint.10105.Security.RadiusSecret", "tCnx3DrzP!kZfhM8vbiJ",
			WebPaDataTypes.STRING.getValue());
		wifiComponents.add(radiusServerSecret);

		WebPaParameter secondaryRadiusServerSecret = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			"Device.WiFi.AccessPoint.10105.Security.SecondaryRadiusSecret", "tCnx3DrzP!kZfhM8vbiJ",
			WebPaDataTypes.STRING.getValue());
		wifiComponents.add(secondaryRadiusServerSecret);

		WebPaParameter secureSsidEnable = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			"Device.WiFi.SSID.10105.Enable ", BroadBandTestConstants.TRUE,
			WebPaDataTypes.BOOLEAN.getValue());
		wifiComponents.add(secureSsidEnable);
	    }

	    WebPaParameter ssidEnable_2ghz = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PUBLIC_SSID_ENABLE_STATUS,
		    BroadBandTestConstants.TRUE, WebPaDataTypes.BOOLEAN.getValue());
	    wifiComponents.add(ssidEnable_2ghz);

	    WebPaParameter ssidEnable_5ghz = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PUBLIC_SSID_ENABLE_STATUS,
		    BroadBandTestConstants.TRUE, WebPaDataTypes.BOOLEAN.getValue());
	    wifiComponents.add(ssidEnable_5ghz);
	}

	/*
	 * Setting the RDK-B Wi-Fi parameters like Private SSID, Public/xfinity SSID(open, secure), etc.
	 */
	LOGGER.info(
		"Configuring RDK-B Wi-Fi  parameters like Private SSID, Public/xfinity SSID(open, secure), etc.");
	boolean isConfigured = BroadBandWebPaUtils.setMultipleParametersUsingWebPaOrDmcli(device, tapEnv,
		wifiComponents);
	if (!isConfigured) {
	    LOGGER.error(
		    "Unable to configure RDK-B Wi-Fi  parameters like Private SSID, Public/xfinity SSID(open, secure), etc.");
	}

	/*
	 * Some of the models it takes more than THREE minutes to initialize the Wi-Fi and come with the proper status.
	 */
	tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);

	// xfinity WiFi enable should be executed after configuring all parameters.
	    isConfigured = BroadBandWebPaUtils.setParameterValuesUsingWebPaOrDmcli(device, tapEnv,
		    BroadBandWebPaConstants.WEBPA_PARAM_ENABLING_PUBLIC_WIFI, WebPaDataTypes.BOOLEAN.getValue(),
		    BroadBandTestConstants.TRUE);
	    if (!isConfigured) {
		LOGGER.error("Unable to enable xfinitywifi using WebPA or dmcli.");
	    }
	/*
	 * Some of the models it takes more than THREE minutes to initialize the Wi-Fi and come with the proper status.
	 */
	tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);

	/*
	 * Setting the RDK-B Generic components parameters like PAM, MoCA, xDNS, etc.
	 */
	LOGGER.info("Configuring RDK-B generic components parameters like PAM, MoCA, xDNS, etc.");
	isConfigured = BroadBandWebPaUtils.setMultipleParametersUsingWebPaOrDmcli(device, tapEnv, genericComponents);
	if (!isConfigured) {
	    LOGGER.error("Unable to configure RDK-B generic components parameters like PAM, MoCA, xDNS, etc.");
	}

	/*
	 * Setting the RDM(remotely download-able modules) components parameters like Mesh, SpeedTest, Advanced
	 * Security, etc.
	 */
	LOGGER.info(
		"Configuring RDM(remotely downloadable modules) components parameters like Mesh (NA for CI qt), SpeedTest, Advanced Security, etc.");
	isConfigured = BroadBandWebPaUtils.setMultipleParametersUsingWebPaOrDmcli(device, tapEnv, rdmComponents);

	if (!isConfigured) {
	    LOGGER.error(
		    "Unable to configure RDM(remotely downloadable modules) components parameters like Mesh, SpeedTest, Advanced Security, etc.");
	}

	LOGGER.info("WAITING FOR ONE MINUTE TO APPLY ALL CONFIGURED SETTINGS BEFORE RETRIEVING THE DEVICE STATUS");

	/*
	 * Some of the models it takes more than THREE minutes to initialize the Wi-Fi and come with the proper status.
	 */
	tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);

	Map<String, WiFiSsidConfigStatus> deviceSsidStatus = new LinkedHashMap<String, WiFiSsidConfigStatus>();

	// Update the current SSID informations like name, security mode, pswd, enable
	// status, ssid status,etc. for different SSID groups.
	for (String ssidGroup : BroadBandTestConstants.DEVICE_WIFI_SSID_CONFIG_DETAILS.keySet()) {

	    if (!((ssidGroup.contains("xfinity_secure_5ghz")) && isBusinessGateway || ssidGroup.contains("xfinity_secure_5ghz"))) {
		WiFiSsidConfigStatus ssidConfigStatus = BroadBandWiFiUtils
			.getWiFiSsidConfigurationStatusViaWebPaOrDmcli(tapEnv, device,
				BroadBandTestConstants.DEVICE_WIFI_SSID_CONFIG_DETAILS.get(ssidGroup));
		deviceSsidStatus.put(ssidGroup, ssidConfigStatus);
	    }
	}

	return deviceSsidStatus;

    }
}

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
package com.automatics.rdkb.utils.wifi.connectedclients;

import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.connection.handler.SeleniumNodeConnectionHandler;
import com.automatics.constants.AutomaticsConstants;
import com.automatics.core.SupportedModelHandler;
import com.automatics.device.Device;
import com.automatics.device.Dut;
import com.automatics.enums.ExecutionStatus;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandConnectedClientTestConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiTestConstant;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.rdkb.constants.RDKBTestConstants.WiFiFrequencyBand;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;
import com.automatics.rdkb.enums.BroadBandWhixEnumConstants.WEBPA_AP_INDEXES;
import com.automatics.rdkb.utils.BroadBandBandSteeringUtils;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.BroadBandSystemUtils;
import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;
import com.automatics.rdkb.utils.CommonUtils;
import com.automatics.rdkb.utils.ConnectedNattedClientsUtils;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.wifi.BroadBandWiFiUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.CommonMethods;
import com.automatics.webpa.WebPaParameter;
import com.automatics.webpa.WebPaServerResponse;
import com.google.common.net.InetAddresses;

/**
 * Utils class to hold connected client common Api's
 * 
 * @author Gnanaprakasham.s,Anandam.S
 * 
 */
public class BroadBandConnectedClientUtils {

	protected static final Logger LOGGER = LoggerFactory.getLogger(BroadBandConnectedClientUtils.class);

	/** Connection Type Ethernet in web ui */
	public static final String CONNECTION_TYPE_ETHERNET = "Ethernet";

	/**
	 * Utils method to get the reserved ip between the dhcp range
	 * 
	 * @param tapEnv      {@link AutomaticsTapApi}
	 * @param device      {@link Dut}
	 * @param ipv4Address String representing the ipv4 address
	 * @return the reserved IP address between DHCP range
	 * @refactor Govardhan
	 */
	public static String getReservedIpBetweenDhcpRangeFromRouter(AutomaticsTapApi tapEnv, Dut device,
			String ipv4Address) {
		LOGGER.info("ENTERING METHOD reservedIpBetweenDhcpRange");
		String reservedIp = null;
		String dhcpIpv4Address = null;
		int maxRange = 0;
		int minRange = 0;
		try {
			String dhcpMinRange = tapEnv.executeWebPaCommand(device,
					BroadBandWebPaConstants.WEBPA_PARAM_TO_RETRIEVE_DHCP_STARTING_IP_ADDRESS);
			String dhcpMaxRange = tapEnv.executeWebPaCommand(device,
					BroadBandWebPaConstants.WEBPA_PARAM_TO_RETRIEVE_DHCP_ENDING_IP_ADDRESS);
			LOGGER.info("DHCP MINIMUM IP RANGE CONFIGURED FOR GATEWAY : " + dhcpMinRange);
			LOGGER.info("DHCP MAXIMUM IP RANGE CONFIGURED FOR GATEWAY : " + dhcpMaxRange);
			if (CommonMethods.isIpv4Address(dhcpMinRange) && CommonMethods.isIpv4Address(dhcpMaxRange)) {

				maxRange = Integer.parseInt(CommonMethods.patternFinder(dhcpMaxRange,
						BroadBandTestConstants.PATTERN_TO_RETRIEVE_LAST_DIGIT_OF_IPv4_ADDRESS));
				minRange = Integer.parseInt(CommonMethods.patternFinder(dhcpMinRange,
						BroadBandTestConstants.PATTERN_TO_RETRIEVE_LAST_DIGIT_OF_IPv4_ADDRESS));
				int reservedRange = BroadBandSystemUtils.getRandomNumberBetweenRange(minRange, maxRange);
				dhcpIpv4Address = CommonMethods.patternFinder(dhcpMinRange,
						BroadBandTestConstants.PATTERN_TO_RETRIEVE_FIRST_3_DIGITS_OF_IPv4_ADDRESS);
				if (CommonMethods.isNotNull(dhcpIpv4Address)) {
					reservedIp = dhcpIpv4Address.concat(Integer.toString(reservedRange));
				}
			}
			while (reservedIp.equalsIgnoreCase(ipv4Address)) {
				int reservedRange = BroadBandSystemUtils.getRandomNumberBetweenRange(minRange, maxRange);
				reservedIp = dhcpIpv4Address.concat(Integer.toString(reservedRange));
				if (!reservedIp.equalsIgnoreCase(ipv4Address)) {
					break;
				}
			}

		} catch (Exception e) {
			LOGGER.error("EXCEPTION OCCURRED WHILE GETTING THE RESERVED IP WITHIN THE DHCP RANGE : " + e.getMessage());
		}

		LOGGER.info("Reserved Ip is : " + reservedIp);
		LOGGER.info("ENDING METHOD getReservedIpBetweenDhcpRangeFromRouter");
		return reservedIp;
	}

	/**
	 * Method to get the ethernet connected client from the Connected Client List.
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * 
	 * @return Dut object representing the Ethernet Connected Client.
	 * @refactor Govardhan
	 */
	public static Dut getEthernetConnectedClient(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("ENTERING METHOD getEthernetConnectedClient");
		Dut ethernetClient = null;
		List<Dut> connectedClients = ((Device) device).getConnectedDeviceList();
		LOGGER.info("DUTINFO DEVICE LIST = " + connectedClients);
		int connectedDevicesCount = null == connectedClients ? 0 : connectedClients.size();
		LOGGER.info("# OF CONNECTED CLIENTS AVAILABLE = " + connectedDevicesCount);
		if (null != connectedClients && connectedClients.size() > 0) {
			for (Dut clientDevice : connectedClients) {
				if (SupportedModelHandler.isNonRDKDevice(clientDevice)) {
					String connectionType = ((Device) clientDevice).getConnectedDeviceInfo().getConnectionType();
					BroadBandConnectedClientUtils.LOGGER.info("CLIENT DEVICE CONNECTION TYPE: " + connectionType);
					String connectedClientOs = ((Device) clientDevice).getConnectedDeviceInfo().getOsType();
					BroadBandConnectedClientUtils.LOGGER.info("CLIENT DEVICE OS TYPE: " + connectedClientOs);
					if (CommonMethods.isNotNull(connectionType)
							&& connectionType.equalsIgnoreCase(BroadBandTestConstants.CONNECTION_TYPE_ETHERNET)
							&& CommonMethods.isNotNull(connectedClientOs) && !connectedClientOs
									.equalsIgnoreCase(BroadBandConnectedClientTestConstants.OS_TYPE_EXTERNAL_ROUTER)) {
						ethernetClient = clientDevice;
						break;
					}
				}
			}
		}
		LOGGER.info("IS ETHERNET CLIENT AVAILABLE: " + (null != ethernetClient));
		LOGGER.debug("ENDING METHOD getEthernetConnectedClient");
		return ethernetClient;
	}

	/**
	 * 
	 * This method is to verify the nslookup command on Ethernet Connected Client
	 * 
	 * 
	 * @param tapEnv          {@link AutomaticsTapApi}
	 * @param deviceConnected {@link Dut}
	 * @param site            site Name to be checked for nslookup
	 * 
	 * @return status It return true,if nslookup command execution is successful.
	 *         Else False.
	 * @refactor Govardhan
	 */
	public static boolean verifyNsLookUpOnEthernetCnctdClient(AutomaticsTapApi tapEnv, Dut deviceConnected,
			String site) {
		LOGGER.debug("STARTING METHOD : verifyNsLookUpOnEthernetCnctdClient()");
		String errorMessage = null;
		boolean status = false;
		String nslookupCommand = null;
		String nslookupResponse = null;
		try {
			nslookupCommand = BroadBandCommonUtils.concatStringUsingStringBuffer(
					BroadBandCommandConstants.CMD_NSLOOKUP_WITH_PATH, BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
					site);
			LOGGER.info("Going to execute the command " + nslookupCommand);
			nslookupResponse = tapEnv.executeCommandOnOneIPClients(deviceConnected, nslookupCommand);
			status = CommonUtils.isNotEmptyOrNull(nslookupResponse)
					&& CommonUtils.patternSearchFromTargetString(nslookupResponse,
							BroadBandTestConstants.PATTERN_TO_GET_IPV4_ADDRESS)
					&& !nslookupResponse.contains(BroadBandTestConstants.NSLOOKUP_NO_RESPONSE)
					&& !nslookupResponse.contains(BroadBandTestConstants.NSLOOKUP_TIMEOUT_RESPONSE)
					&& !nslookupResponse.contains(BroadBandTestConstants.NSLOOKUP_CACHE_REDIRECT_IP);
		} catch (TestException exception) {
			errorMessage = "Exception occured while verifying the nslookup command : " + exception.getMessage();
			LOGGER.error(errorMessage);
		}
		LOGGER.debug("ENDING METHOD : verifyNsLookUpOnEthernetCnctdClient()");
		return status;
	}

	/**
	 * Method to get the all radio status
	 * 
	 * @param tapEnv - instance of {@link AutomaticsTapApi}
	 * @param device - instance of {@link Dut}
	 * @return Map with radio status for 2.4 and 5 GHz
	 * @author Anandam S
	 * @Refactor Sruthi Santhosh
	 */
	public static Map<String, String> getAllRadioStatus(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.info("Going to get radio status of all radios");
		String[] webpaParameters = {
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_2_4GHZ_PRIVATE_SSID,
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_5GHZ_PRIVATE_SSID };
		Map<String, String> webpaResponse = tapEnv.executeMultipleWebPaGetCommands(device, webpaParameters);
		return webpaResponse;

	}

	/**
	 * Method to get the NUC device from connected client and connect with 5 GHz
	 * wifi network
	 * 
	 * @param device           instance of {@link Dut}
	 * @param AutomaticsTapApi instance of {@link AutomaticsTapApi}
	 * @return 5 Ghz client device instance
	 * 
	 * @author Gnanaprakasham.s
	 */
	public static Dut get5GhzWiFiCapableClientDeviceAndConnectToAssociated5GhzSsid(Dut device,
			AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD : get5GhzWiFiCapableClientDeviceAndConnectToAssociated5GhzSsid () ");
		// device instance to store 5GHz client device
		Dut deviceConnectedWith5Ghz = null;
		// variable to store wifi capability
		String wifiCapability = null;
		// variable to store connection type of the connected client
		String connectionType = null;
		// variable to store the SSID of 5GHz Wi-Fi Network
		String ssidName = null;
		// variable to store the Pass Phrase of 5GHz Wi-Fi Network
		String passPhraseName = null;
		// Get list of connected client devices
		List<Dut> connectedClientDevices = ((Device) device).getConnectedDeviceList();

		try {
			if (connectedClientDevices != null && connectedClientDevices.size() > 0) {
				// SSID of 5GHz Wi-Fi Network
				ssidName = getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv, WiFiFrequencyBand.WIFI_BAND_5_GHZ);
				// Pass Phrase of 5GHz Wi-Fi Network
				passPhraseName = getSsidPassphraseFromGatewayUsingWebPaOrDmcli(device, tapEnv,
						WiFiFrequencyBand.WIFI_BAND_5_GHZ);
				// Security Mode of 5GHz Wi-Fi Network
				String securityModeOf5GHzNetwork = tapEnv.executeWebPaCommand(device,
						BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_PRIVATE_SECURITY_MODEENABLED);
				LOGGER.info("Security mode obtained for 5GHz Wi-Fi network from the Router Device is : "
						+ securityModeOf5GHzNetwork);
				boolean openSecurityMode = CommonMethods.isNotNull(securityModeOf5GHzNetwork)
						&& securityModeOf5GHzNetwork.equalsIgnoreCase(BroadBandTestConstants.SECURITY_MODE_NONE);
				LOGGER.info("Obtained number of client devices is : " + connectedClientDevices.size());
				for (Dut clientDevice : connectedClientDevices) {
					connectionType = ((Device) clientDevice).getConnectedDeviceInfo().getConnectionType();
					LOGGER.info("Client device connection type : " + connectionType);
					wifiCapability = ((Device) clientDevice).getConnectedDeviceInfo().getWifiCapability();
					LOGGER.info("Client device Wi-Fi capability : " + wifiCapability);
					if (CommonMethods.isNotNull(connectionType) && CommonMethods.isNotNull(wifiCapability)) {
						if (connectionType.equalsIgnoreCase(
								BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI)
								&& (wifiCapability.equalsIgnoreCase(
										BroadBandConnectedClientTestConstants.STRING_WIFI_CAPABILITY_5GHZ_ONLY)
										|| wifiCapability.equalsIgnoreCase(
												BroadBandConnectedClientTestConstants.STRING_WIFI_CAPABILITY_DUAL_BAND))) {
							if (openSecurityMode) {
								LOGGER.info(
										"Going to connect the client with 5GHz Wi-Fi network : Open security mode.");
								deviceConnectedWith5Ghz = ConnectedNattedClientsUtils.connectToSSID(clientDevice,
										tapEnv, ssidName, passPhraseName,
										BroadBandConnectedClientTestConstants.SECURITY_MODE_OPEN.toLowerCase())
												? clientDevice
												: null;
							} else {
								LOGGER.info(
										"Going to connect the client with 5GHz Wi-Fi network : Recommanded security mode.");
								deviceConnectedWith5Ghz = ConnectedNattedClientsUtils.connectToSSID(clientDevice,
										tapEnv, ssidName, passPhraseName) ? clientDevice : null;
							}
							if (deviceConnectedWith5Ghz != null
									&& validateConnectedClientPrerequisites(device, deviceConnectedWith5Ghz, tapEnv)) {
								LOGGER.info("Successfully established Wi-Fi connection with 5 GHz Wi-Fi network!");
								break;
							} else {
								LOGGER.info(
										"Unable to establish Wi-Fi connection with 5 GHz Wi-Fi network from the obtained connected or failed to pass the prerequisite steps");
								deviceConnectedWith5Ghz = null;
							}
						}
					} else {
						LOGGER.error(
								"Obtained null response for client device 'connection type' or 'wifi capability'. Check MDS configuration for the connected client with Wi-Fi Mac Address : "
										+ ((Device) clientDevice).getConnectedDeviceInfo().getWifiCapability());
					}
				}
				if (deviceConnectedWith5Ghz == null) {
					throw new TestException(
							"Unable to establish Wi-Fi connection with 5 GHz Wi-Fi network from the obtained connected clients!");
				}
			} else {
				throw new TestException("No connected client is currently associated with the gateway device!");
			}

		} catch (Exception exception) {
			throw new TestException("Unable to connect the client with 5GHz Wi-Fi Network due to following Reason : "
					+ exception.getMessage());
		}
		LOGGER.debug("ENDING METHOD : get5GhzWiFiCapableClientDeviceAndConnectToAssociated5GhzSsid () ");
		return deviceConnectedWith5Ghz;
	}

	/**
	 * Method to get the SSID name using Device.WiFi.SSID.{i}.SSID
	 * 
	 * @param tapEnv            {@link AutomaticsTapApi}
	 * @param device            {@link Dut}
	 * @param WiFiFrequencyBand frequency band
	 * @return SSID name
	 * 
	 * @author Gnanaprakasham.s
	 * @refactor Govardhan
	 */
	public static String getSsidNameFromGatewayUsingWebPaOrDmcli(Dut device, AutomaticsTapApi tapEnv,
			WiFiFrequencyBand wifiBand) {
		LOGGER.debug("START METHOD : getSsidNameFromGatewayUsingWebPaOrDmcli () ");
		String ssidGatewayDevice = null;
		String errorMessage = null;

		if (wifiBand.compareTo(WiFiFrequencyBand.WIFI_BAND_2_GHZ) == 0) {
			ssidGatewayDevice = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID);

		} else if (wifiBand.compareTo(WiFiFrequencyBand.WIFI_BAND_5_GHZ) == 0) {
			ssidGatewayDevice = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_NAME);
		}

		if (CommonMethods.isNull(ssidGatewayDevice)) {

			errorMessage = "Not able to get the SSID name for client device connected with " + wifiBand + " GHz ";
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}
		LOGGER.debug("Ending METHOD : getSsidNameFromGatewayUsingWebPaOrDmcli () ");
		return ssidGatewayDevice;

	}

	/**
	 * Method to get the SSID pass phrase using
	 * Device.WiFi.AccessPoint.{i}.Security.X_COMCAST-COM_KeyPassphrase
	 * 
	 * @param tapEnv            {@link AutomaticsTapApi}
	 * @param device            {@link Dut}
	 * @param WiFiFrequencyBand frequency band
	 * @return SSID pass phrase
	 * 
	 * @author Gnanaprakasham.s
	 * @refactor Govardhan
	 */
	public static String getSsidPassphraseFromGatewayUsingWebPaOrDmcli(Dut device, AutomaticsTapApi tapEnv,
			WiFiFrequencyBand wifiBand) {
		LOGGER.debug("START METHOD : getSsidPassphraseFromGatewayUsingWebPaOrDmcli () ");
		String password = null;

		if (wifiBand.compareTo(WiFiFrequencyBand.WIFI_BAND_2_GHZ) == 0) {
			password = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_PRIVATE_SSID_2GHZ_PASSPHRASE);

		} else if (wifiBand.compareTo(WiFiFrequencyBand.WIFI_BAND_5_GHZ) == 0) {
			password = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_PRIVATE_SSID_5GHZ_PASSPHRASE);
		}

		if (!CommonMethods.isNotNull(password)) {
			throw new TestException(
					"Not able to get the passphrase for client device connected with " + wifiBand + " GHz ");
		}
		LOGGER.debug("ENDING METHOD : getSsidPassphraseFromGatewayUsingWebPaOrDmcli () ");
		return password;

	}

	/**
	 * Method to validate connected client Prerequisites (IPv4 Address,IPv4 Internet
	 * Connectivity,IPv6 Address,IPv6 Internet Connectivity)
	 * 
	 * @param device          Dut instance
	 * @param connectedClient Connected client instance
	 * @param tapEnv          AutomaticsTapApi instance
	 * @return status true if all prerequisite steps are passed
	 */
	public static boolean validateConnectedClientPrerequisites(Dut device, Dut connectedClient,
			AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD : validateConnectedClientPrerequisites () ");
		boolean validateIpv4Status = false;
		boolean validateIpv4Connection = false;
		boolean validateIpv6Status = false;
		boolean validateIpv6Connection = false;
		boolean captivePortalStatus = false;
		boolean status = false;
		try {

			if (DeviceModeHandler.isFibreDevice(device)) {
				captivePortalStatus = false;
			} else {
				captivePortalStatus = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcliAndVerify(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_PARAM_CAPTIVE_PORTAL_ENABLE, BroadBandTestConstants.TRUE);
			}
			LOGGER.info("captivePortalStatus" + captivePortalStatus);
			if (!captivePortalStatus) {
				// VERIFY THE CORRECT IPV4 ADDRESS FOR CONNECTED CLIENT WITH GIVEN GHZ SSID
				validateIpv4Status = BroadBandConnectedClientUtils
						.verifyIpv4OrIpv6AddressForWiFiOrLanInterfaceConnectedWithRdkbDeviceWithPolledDuration(tapEnv,
								connectedClient, BroadBandTestConstants.IP_VERSION4,
								BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS,
								BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
				LOGGER.info("VERIFYING THE CORRECT IPV4 ADDRESS FOR CONNECTED CLIENT WITH GIVEN GHZ SSID; STATUS="
						+ validateIpv4Status);
				validateIpv4Connection = BroadBandConnectedClientUtils
						.checkInternetConnectivityforIpv4OrIpv6WithPolledDuation(tapEnv, connectedClient,
								BroadBandTestConstants.IP_VERSION4, BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS,
								BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
				LOGGER.info(
						"VERIFYING THE INTERNET CONNECTIVITY IN THE CLIENT WITH GIVEN GHZ SSID INTERFACEUSING IPV4; STATUS="
								+ validateIpv4Connection);
				status = validateIpv4Status && validateIpv4Connection;
				if (status && (!DeviceModeHandler.isFibreDevice(device)
						&& (BroadbandPropertyFileHandler.isIpv6Enabled()))) {
					// VERIFY THE CORRECT IPV6 ADDRESS FOR CONNECTED CLIENT WITH GIVEN GHZ SSID .
					validateIpv6Status = BroadBandConnectedClientUtils
							.verifyIpv4OrIpv6AddressForWiFiOrLanInterfaceConnectedWithRdkbDeviceWithPolledDuration(
									tapEnv, connectedClient, BroadBandTestConstants.IP_VERSION6,
									BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS,
									BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
					if (!validateIpv6Status && DeviceModeHandler.isDSLDevice(device)) {
						validateIpv6Status = validateIPv6AddressCompleteCheck(((Device) connectedClient).getOsType(),
								device, connectedClient, tapEnv);
					}
					LOGGER.info("VERIFYING THE CORRECT IPV6 ADDRESS FOR CONNECTED CLIENT WITH GIVEN GHZ SSID; STATUS="
							+ validateIpv6Status);

					validateIpv6Connection = BroadBandConnectedClientUtils
							.checkInternetConnectivityforIpv4OrIpv6WithPolledDuation(tapEnv, connectedClient,
									BroadBandTestConstants.IP_VERSION6, BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS,
									BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
					LOGGER.info(
							"VERIFYING THE INTERNET CONNECTIVITY IN THE CLIENT WITH GIVEN GHZ SSID INTERFACEUSING IPV6; STATUS="
									+ validateIpv6Connection);
					status = validateIpv4Status && validateIpv4Connection && validateIpv6Status
							&& validateIpv6Connection;
				}

			} else {
				LOGGER.info("DEVICE IS IN CAPTIVE PORTAL MODE SO SKIPPING PREREQUISITES TEST");
				status = true;
			}

		} catch (Exception e) {
			LOGGER.error(
					"Exception caught while validating wifi Prerequisites (IPv4 Address,IPv4 Internet Connectivity,IPv6 Address,IPv6 Internet Connectivity)"
							+ e.getMessage());

		}
		LOGGER.debug("ENDING METHOD : validateConnectedClientPrerequisites () ");
		return status;
	}

	/**
	 * Method to check ipv4 or ipv6 address in connected client
	 * 
	 * @param tapEnv                {@link AutomaticsTapApi}
	 * @param connectedClientDevice {@link Dut}
	 * @param ipVersion             String Ipversion
	 * @param polledDuration        long polledduration
	 * @param polledInterval        long polled interval
	 * @return status as true if the ipv4 or ipv6 address is retrieved.
	 */
	public static boolean verifyIpv4OrIpv6AddressForWiFiOrLanInterfaceConnectedWithRdkbDeviceWithPolledDuration(
			AutomaticsTapApi tapEnv, Dut connectedClientDevice, String ipVersion, long polledDuration,
			long polledInterval) {
		LOGGER.debug(
				"STARTING METHOD : verifyIpv4OrIpv6AddressForWiFiOrLanInterfaceConnectedWithRdkbDeviceWithPolledDuratiobn () ");
		long startTime = System.currentTimeMillis();
		boolean status = false;
		try {
			do {
				if (ipVersion.equals(BroadBandTestConstants.IP_VERSION4)) {
					status = BroadBandConnectedClientUtils
							.verifyIpv4AddressForWiFiOrLanInterfaceConnectedWithRdkbDevice(
									((Device) connectedClientDevice).getOsType(), connectedClientDevice, tapEnv);
				} else if (ipVersion.equals(BroadBandTestConstants.IP_VERSION6)) {
					status = BroadBandConnectedClientUtils
							.verifyIpv6AddressForWiFiOrLanInterfaceConnectedWithRdkbDevice(
									((Device) connectedClientDevice).getOsType(), connectedClientDevice, tapEnv);
				}
			} while (!status && (System.currentTimeMillis() - startTime) < polledDuration
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, polledInterval));
			LOGGER.info(status ? "CONNECTED CLIENT " + ipVersion + " ADDRESS OBTAINED AND VALIDATED SUCCESSFULLY"
					: "CONNECTED CLIENT " + ipVersion + " ADDRESS NOT OBTAINED ");

		} catch (Exception e) {
			LOGGER.error("Exception occured while checking for internet accessibility for " + ipVersion
					+ " using curl or ping method " + e.getMessage());
		}
		LOGGER.debug(
				"ENDING METHOD : verifyIpv4OrIpv6AddressForWiFiOrLanInterfaceConnectedWithRdkbDeviceWithPolledDuratiobn () ");
		return status;
	}

	/**
	 * Method to check internet access for either ipv4 or ipv6 with curl operation
	 * if fails checks with ping operation.
	 * 
	 * @param tapEnv                instance of {@link AutomaticsTapApi}
	 * @param connectedClientDevice Connected client Dut instance
	 * @param ipVersion             String instance of either IPV4 or IPV6.
	 * @return status as true if the Internet is accessible using curl or ping.
	 * 
	 */
	public static boolean checkInternetConnectivityforIpv4OrIpv6WithPolledDuation(AutomaticsTapApi tapEnv,
			Dut connectedClientDevice, String ipVersion, long polledDuration, long polledInterval) {
		LOGGER.debug("STARTING METHOD : checkInternetConnectivityforIpv4OrIpv6WithPolledDuation () ");
		BroadBandResultObject result = null;
		long startTime = System.currentTimeMillis();
		boolean status = false;
		try {
			do {
				result = BroadBandConnectedClientUtils.verifyInternetIsAccessibleInConnectedClientUsingCurl(tapEnv,
						connectedClientDevice,
						BroadBandTestConstants.URL_HTTPS + BroadBandTestConstants.STRING_GOOGLE_HOST_ADDRESS,
						ipVersion);
				status = result.isStatus();

				LOGGER.info(status ? "INTERNET CONNECTION WITH " + ipVersion + " IS SUCCESSFUL USING CURL"
						: "INTERNET CONNECTION VALIDATION FAILED WITH " + ipVersion
								+ "; WILL TRY PING REQUEST AS FALLBACK");
			} while (!status && (System.currentTimeMillis() - startTime) < polledDuration
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, polledInterval));
			if (!status) {
				do {
					status = ConnectedNattedClientsUtils.verifyPingConnectionForIpv4AndIpv6(connectedClientDevice,
							tapEnv, BroadBandTestConstants.PING_TO_GOOGLE, ipVersion);
				} while (!status && (System.currentTimeMillis() - startTime) < polledDuration
						&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, polledInterval));
				LOGGER.info(status ? "INTERNET CONNECTION WITH " + ipVersion + " IS SUCCESSFUL USING PING"
						: "PING OPERATION FAILED TO ACCESS THE SITE 'www.google.com' USING IPV4 ");
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while checking for internet accessibility for " + ipVersion
					+ " using curl or ping method " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : checkInternetConnectivityforIpv4OrIpv6WithPolledDuation () ");
		return status;
	}

	/**
	 * This method is to Validate IPv6 Address
	 * 
	 * @param osType - Os type of the client device
	 * @param device - instance of {@link Dut}
	 * @paran device - instance of {@link Dut}
	 * @param tapEnv - instance of {@link AutomaticsTapApi}
	 * @return
	 * 
	 * @refactor Govardhan
	 */
	public static boolean validateIPv6AddressCompleteCheck(String osType, Dut device, Dut clientDevice,
			AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD : validateIPv6AddressCompleteCheck");
		// Variable declaration begins
		String command = null;
		String commandResponse = null;
		String pattern = null;
		boolean result = false;
		String defaultInterface = null;
		String prefix = null;
		// Variable declaration ends
		if (BroadBandConnectedClientTestConstants.OS_MAC.equalsIgnoreCase(osType)) {
			String interfaceNameMacOS = ConnectedNattedClientsUtils.getWifiInterfaceNameOnMacOS(clientDevice, tapEnv);
			command = BroadBandConnectedClientTestConstants.MAC_OS_COMMAND_CHECK_IPV6_ADDRESS.replace(
					BroadBandConnectedClientTestConstants.MAC_OS_WIFI_PARAM_INTERFACE_NAME, interfaceNameMacOS);
			commandResponse = tapEnv.executeCommandOnOneIPClients(clientDevice, command);
			pattern = BroadBandConnectedClientTestConstants.PATTERN_MAC_OS_VALID_IPV6_ADDRESS;
			LOGGER.info("PATTERN: " + pattern);
			result = CommonMethods.isNotNull(commandResponse) && CommonMethods.patternMatcher(commandResponse, pattern);
		} else {// If OS type is LINUX or WINDOWS
			if (BroadBandConnectedClientTestConstants.OS_LINUX.equalsIgnoreCase(osType)) {
				defaultInterface = getSpecificDefaultInterfaceNameOfTheLinux(clientDevice, tapEnv);
			}
			command = BroadBandConnectedClientTestConstants.OS_LINUX.equalsIgnoreCase(osType)
					? BroadBandConnectedClientTestConstants.LINUX_COMMAND_CHECK_IPV6_ADDRESS
							.replaceAll(BroadBandTestConstants.STRING_REPLACE, defaultInterface.trim())
					: BroadBandConnectedClientTestConstants.WINDOWS_COMMAND_CHECK_IPV6_ADDRESS;
			commandResponse = tapEnv.executeCommandOnOneIPClients(clientDevice, command);
			prefix = tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEB_PARAM_DELEGATED_PREFIX_IPV6);
			if (CommonMethods.isNotNull(prefix)) {
				prefix = prefix.replaceAll(BroadBandTestConstants.PREFIX_LENGTH_64,
						BroadBandTestConstants.EMPTY_STRING);
				if (CommonMethods.isNotNull(commandResponse)) {
					String ipv6Address = CommonMethods.patternFinder(commandResponse,
							BroadBandConnectedClientTestConstants.PATTERN_WINDOWS_VALID_SEG_FOUR_IPV6_ADDRESS);
					LOGGER.info("IPv6 address retrieved using four segment pattern : " + ipv6Address);
					result = InetAddresses.isInetAddress(ipv6Address)
							&& CommonUtils.patternSearchFromTargetString(commandResponse, prefix);
				}
			}
		}
		LOGGER.debug("ENDING METHOD : validateIPv6AddressCompleteCheck");
		return result;
	}

	/**
	 * Check whether IPV4 address pattern in one IP Client
	 * 
	 * @param osType - Os type of the client device
	 * @param device - instance of {@link Dut}
	 * @param tapEnv - instance of {@link AutomaticsTapApi}
	 * @return
	 * @refactor Govardhan
	 */
	public static boolean verifyIpv4AddressForWiFiOrLanInterfaceConnectedWithRdkbDevice(String osType, Dut device,
			AutomaticsTapApi tapEnv) throws TestException {
		String command = null;
		String commandResponse = null;
		String pattern = null;
		boolean result = false;
		String defaultInterface = null;
		try {
			// If OS Type is MAC
			if (BroadBandConnectedClientTestConstants.OS_MAC.equalsIgnoreCase(osType)) {
				String interfaceNameMacOS = ConnectedNattedClientsUtils.getWifiInterfaceNameOnMacOS(device, tapEnv);
				command = BroadBandConnectedClientTestConstants.MAC_OS_COMMAND_CHECK_IPV4_ADDRESS.replace(
						BroadBandConnectedClientTestConstants.MAC_OS_WIFI_PARAM_INTERFACE_NAME, interfaceNameMacOS);
				commandResponse = tapEnv.executeCommandOnOneIPClients(device, command);
				pattern = BroadBandConnectedClientTestConstants.PATTERN_MAC_OS_VALID_IPV4_ADDRESS;
				LOGGER.info("PATTERN: " + pattern);
				result = CommonMethods.isNotNull(commandResponse)
						&& CommonMethods.patternMatcher(commandResponse, pattern);
			} else {// If OS type is LINUX or WINDOWS
				if (BroadBandConnectedClientTestConstants.OS_LINUX.equalsIgnoreCase(osType)) {
					defaultInterface = getSpecificDefaultInterfaceNameOfTheLinux(device, tapEnv);
				}
				command = BroadBandConnectedClientTestConstants.OS_LINUX.equalsIgnoreCase(osType)
						? BroadBandConnectedClientTestConstants.LINUX_COMMAND_CHECK_IPV4_ADDRESS
								.replaceAll(BroadBandTestConstants.STRING_REPLACE, defaultInterface.trim())
						: BroadBandConnectedClientTestConstants.WINDOWS_COMMAND_CHECK_IPV4_ADDRESS;
				commandResponse = tapEnv.executeCommandOnOneIPClients(device, command);
				pattern = BroadBandConnectedClientTestConstants.OS_LINUX.equalsIgnoreCase(osType)
						? BroadBandConnectedClientTestConstants.PATTERN_LINUX_VALID_IPV4_ADDRESS
						: BroadBandConnectedClientTestConstants.PATTERN_WINDOWS_VALID_IPV4_ADDRESS;
				LOGGER.info("PATTERN: " + pattern);
				result = CommonMethods.isNotNull(commandResponse)
						&& CommonMethods.patternMatcher(commandResponse, pattern);
				// Its applicable only for ` OS
				if (!result && BroadBandConnectedClientTestConstants.OS_LINUX.equalsIgnoreCase(osType)) {
					result = CommonMethods.isNotNull(commandResponse) && CommonMethods.patternMatcher(commandResponse,
							BroadBandTestConstants.INET_V4_ADDRESS_PATTERN);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception Occurred while getting ipv4 address:" + e.getMessage());
		}
		return result;
	}

	/**
	 * Check whether IPV6 address pattern in one IP Client
	 * 
	 * @param osType - Os type of the client device
	 * @param device - instance of {@link Dut}
	 * @param tapEnv - instance of {@link AutomaticsTapApi}
	 * @return
	 */
	public static boolean verifyIpv6AddressForWiFiOrLanInterfaceConnectedWithRdkbDevice(String osType, Dut device,
			AutomaticsTapApi tapEnv) {
		String command = null;
		String commandResponse = null;
		String pattern = null;
		boolean result = false;
		String defaultInterface = null;
		try {
			if (BroadBandConnectedClientTestConstants.OS_MAC.equalsIgnoreCase(osType)) {
				String interfaceNameMacOS = ConnectedNattedClientsUtils.getWifiInterfaceNameOnMacOS(device, tapEnv);
				command = BroadBandConnectedClientTestConstants.MAC_OS_COMMAND_CHECK_IPV6_ADDRESS.replace(
						BroadBandConnectedClientTestConstants.MAC_OS_WIFI_PARAM_INTERFACE_NAME, interfaceNameMacOS);
				commandResponse = tapEnv.executeCommandOnOneIPClients(device, command);
				pattern = BroadBandConnectedClientTestConstants.PATTERN_MAC_OS_VALID_IPV6_ADDRESS;
				LOGGER.info("PATTERN: " + pattern);
				result = CommonMethods.isNotNull(commandResponse)
						&& CommonMethods.patternMatcher(commandResponse, pattern);
			} else {// If OS type is LINUX or WINDOWS
				if (BroadBandConnectedClientTestConstants.OS_LINUX.equalsIgnoreCase(osType)) {
					defaultInterface = getSpecificDefaultInterfaceNameOfTheLinux(device, tapEnv);
				}
				command = BroadBandConnectedClientTestConstants.OS_LINUX.equalsIgnoreCase(osType)
						? BroadBandConnectedClientTestConstants.LINUX_COMMAND_CHECK_IPV6_ADDRESS
								.replaceAll(BroadBandTestConstants.STRING_REPLACE, defaultInterface.trim())
						: BroadBandConnectedClientTestConstants.WINDOWS_COMMAND_CHECK_IPV6_ADDRESS;
				commandResponse = tapEnv.executeCommandOnOneIPClients(device, command);
				pattern = BroadBandConnectedClientTestConstants.OS_LINUX.equalsIgnoreCase(osType)
						? BroadBandConnectedClientTestConstants.PATTERN_LINUX_VALID_IPV6_ADDRESS
						: BroadBandConnectedClientTestConstants.PATTERN_WINDOWS_VALID_IPV6_ADDRESS;
				LOGGER.info("PATTERN: " + pattern);
				result = CommonMethods.isNotNull(commandResponse)
						&& CommonMethods.patternMatcher(commandResponse, pattern);
				// Its applicable only for Linux OS
				if (!result && BroadBandConnectedClientTestConstants.OS_LINUX.equalsIgnoreCase(osType)) {
					result = CommonMethods.isNotNull(commandResponse) && CommonMethods.patternMatcher(commandResponse,
							BroadBandConnectedClientTestConstants.INET_V6_ADDRESS_PATTERN_WITUOUT_SCOPE);
				}
				// Its applicable only for Windows OS
				if (!result && BroadBandConnectedClientTestConstants.OS_WINDOWS.equalsIgnoreCase(osType)) {
					result = CommonMethods.isNotNull(commandResponse) && CommonMethods.patternMatcher(commandResponse,
							BroadBandConnectedClientTestConstants.PATTERN_WINDOWS_VALID_SEG_FIVE_IPV6_ADDRESS);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception Occurred while getting ipv6 address:" + e.getMessage());
		}
		return result;

	}

	/**
	 * 
	 * @param tapEnv                instance of {@link AutomaticsTapApi}
	 * @param connectedClientDevice Connected client Dut instance
	 * @param url                   Internet access of URL which needs to be checked
	 * @return true if the given URL is acccessible using curl
	 */
	public static BroadBandResultObject verifyInternetIsAccessibleInConnectedClientUsingCurl(AutomaticsTapApi tapEnv,
			Dut connectedClientDevice, String url, String resolveTo) {

		boolean isAccessible = false;
		String errorMessage = null;
		BroadBandResultObject result = new BroadBandResultObject();
		String curlCommand = null;

		switch (resolveTo) {
		case BroadBandTestConstants.IP_VERSION6:
			curlCommand = (CommonUtils.patternSearchFromTargetString(url, "ftp"))
					? BroadBandCommonUtils.concatStringUsingStringBuffer(
							BroadBandCommandConstants.CMD_CURL_WITH_20_SECS_TIMEOUT, "-6 ", url)
					: BroadBandCommonUtils.concatStringUsingStringBuffer(
							BroadBandCommandConstants.CMD_CURL_RESOLVE_DOMAIN_TO_IPV6_ADDRESS, url);
			break;

		case BroadBandTestConstants.IP_VERSION4:
			curlCommand = (CommonUtils.patternSearchFromTargetString(url, "ftp"))
					? BroadBandCommonUtils.concatStringUsingStringBuffer(
							BroadBandCommandConstants.CMD_CURL_WITH_20_SECS_TIMEOUT, "-4 ", url)
					: BroadBandCommonUtils.concatStringUsingStringBuffer(
							BroadBandCommandConstants.CMD_CURL_RESOLVE_DOMAIN_TO_IPV4_ADDRESS, url);
			break;

		default:
			curlCommand = (CommonUtils.patternSearchFromTargetString(url, "ftp"))
					? BroadBandCommonUtils
							.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_CURL_WITH_20_SECS_TIMEOUT, url)
					: BroadBandCommonUtils.concatStringUsingStringBuffer(
							BroadBandCommandConstants.CMD_CURL_WITH_TIMEOUT_AND_HEADER, url);
			break;
		}

		String response = tapEnv.executeCommandOnOneIPClients(connectedClientDevice, curlCommand);
		errorMessage = "Null response obtained on checking Internet access in the connected client.";
		if (CommonMethods.isNotNull(response)) {
			isAccessible = CommonUtils.patternSearchFromTargetString(response,
					BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_HEADER_SUCCESS_MESSAGE)
					|| CommonUtils.patternSearchFromTargetString(response,
							BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_HEADER_SUCCESS_HTTP_1_1)
					|| CommonUtils.patternSearchFromTargetString(response,
							BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_SUCCESS_MESSAGE)
					|| CommonUtils.patternSearchFromTargetString(response,
							BroadBandConnectedClientTestConstants.RESPONSE_STATUS_OK);
			errorMessage = (!isAccessible && CommonUtils.patternSearchFromTargetString(response,
					BroadBandTestConstants.CURL_NOT_INSTALLED_ERROR_MESSAGE))
							? "Curl is not installed in the "
									+ ((Device) connectedClientDevice).getConnectedDeviceInfo().getConnectionType()
									+ " Connected Client"
							: (!isAccessible && (CommonUtils.patternSearchFromTargetString(response,
									BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_CONNECTION_SSL_HANDSHAKE_FAILURE_MESSAGE)
									|| CommonUtils.patternSearchFromTargetString(response,
											BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_FAILURE_MESSAGE)
									|| CommonUtils.patternSearchFromTargetString(response,
											BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_CONNECTION_TIMEOUT_MESSAGE)
									|| CommonUtils.patternSearchFromTargetString(response,
											BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_OPERATION_TIMEOUT_MESSAGE)
									|| CommonUtils.patternSearchFromTargetString(response,
											BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_RESOLVING_TIMEOUT_MESSAGE)))
													? "Website/IP Address is not accessible in the Connected Client"
													: "Unable to check the internet access in the connected client using curl";
		}
		result.setErrorMessage(errorMessage);
		result.setStatus(isAccessible);

		return result;
	}

	/**
	 * Method used to execute the command to get the specific interface name Linux
	 * connected client
	 * 
	 * @param deviceConnected Connected client Dut instance
	 * @param tapEnv          AutomaticsTapApi instance
	 * @return defaultInterFaceName dfault specific interface
	 * @refactor Govardhan
	 */
	public static String getSpecificDefaultInterfaceNameOfTheLinux(Dut deviceConnected, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD : getSpecificDefaultInterfaceNameOfTheLinux()");
		String defaultInterFaceName = null;
		try {
			Device ecastSettop = (Device) deviceConnected;
			String defaultInterface = BroadBandConnectedClientUtils
					.getDefaultInterfaceNameOfTheLinuxConnClientDevice(deviceConnected, tapEnv).trim();
			LOGGER.info("DEFAULT INTERFACE OBTAINED IN THE LINUX CLIENT IS  : " + defaultInterface);
			String connectionType = ecastSettop.getConnectedDeviceInfo().getConnectionType();
			LOGGER.info("CONNECTION TYPE OF THE CONNECTED CLIENT IS : " + connectionType);
			String[] expectedInterfaces = null;
			if (CommonMethods.isNotNull(defaultInterface) && CommonMethods.isNotNull(connectionType)
					&& CONNECTION_TYPE_ETHERNET.equalsIgnoreCase(connectionType)) {
				LOGGER.info("GOING TO GET IPV4 ADDRESS FROM ETHERNET INTERFACE");
				expectedInterfaces = AutomaticsTapApi
						.getSTBPropsValue(
								BroadBandTestConstants.PROP_KEY_TO_GET_EXPECTED_ETHERNET_INTERFACE_IN_LINUX_CLIENT)
						.split(BroadBandTestConstants.SEMI_COLON);
			} else if (CommonMethods.isNotNull(defaultInterface)
					&& BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI
							.equalsIgnoreCase(connectionType)) {
				LOGGER.info("GOING TO GET IPV4 ADDRESS FROM WI-FI INTERFACE");
				expectedInterfaces = AutomaticsTapApi
						.getSTBPropsValue(
								BroadBandTestConstants.PROP_KEY_TO_GET_EXPECTED_WIFI_INTERFACE_IN_LINUX_CLIENT)
						.split(BroadBandTestConstants.SEMI_COLON);
			}
			if (expectedInterfaces != null && expectedInterfaces.length != 0) {
				for (String interfaceName : expectedInterfaces) {
					if (CommonUtils.patternSearchFromTargetString(defaultInterface, interfaceName)) {
						defaultInterFaceName = interfaceName;
						LOGGER.info("DEFAULT INTERFACE NAME : " + interfaceName);
						break;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception Occured in getSpecificDefaultInterfaceNameOfTheLinux():" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : getSpecificDefaultInterfaceNameOfTheLinux()");
		return defaultInterFaceName;
	}

	/**
	 * Helper method to get interface name of the linux connected client
	 * 
	 * @param connectedClientDevice Connected client Dut instance
	 * @param tapEnv                AutomaticsTapApi instance
	 * @return default interface name
	 * @refactor Govardhan
	 */
	public static String getDefaultInterfaceNameOfTheLinuxConnClientDevice(Dut connectedClientDevice,
			AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: getDefaultInterfaceNameOfTheLinuxConnClientDevice");
		String interfaceName = null;
		String connectionType = ((Device) connectedClientDevice).getConnectedDeviceInfo().getConnectionType();
		LOGGER.info("Linux device is connected to Gateway device to " + connectionType);
		if (CommonMethods.isNotNull(connectionType)) {
			// To get column number of the interface column in route table
			String columnNumber = tapEnv.executeCommandOnOneIPClients(connectedClientDevice,
					BroadBandCommandConstants.CMD_GET_INTERFACE_COLUMN_NUMBER_IN_ROUTE_TABLE);
			// To get default interface name to which device is connected to
			// route | grep default | awk '{print $8}'
			String command = BroadBandCommandConstants.CMD_ROUTE_DEFAULT_ROW_FIRST_COLUMN.replace(
					BroadBandTestConstants.STRING_DOLLAR_SIGN + BroadBandTestConstants.STRING_VALUE_ONE,
					BroadBandTestConstants.STRING_DOLLAR_SIGN + columnNumber);
			LOGGER.info("Command to be executed is - " + command);
			interfaceName = tapEnv.executeCommandOnOneIPClients(connectedClientDevice, command);
			LOGGER.info("Interface name - " + interfaceName);
		} else {
			LOGGER.error("Device connection type is not marked in MDS. Device connection type cant be empty");
		}
		LOGGER.info("Default interface is - " + interfaceName);
		LOGGER.debug("ENDING METHOD: getDefaultInterfaceNameOfTheLinuxConnClientDevice ");
		return interfaceName;
	}

	/**
	 * Based on flag value either we are enabling or disabling the particular radio
	 * type using webpa params.
	 * 
	 * @param radiotype - Radio type (frequency band)
	 * @param device    - instance of {@link Dut}
	 * @param tapEnv    - instance of {@link AutomaticsTapApi}
	 * @param toEnable  - Flag value to enable/disable Radio
	 * @return
	 * @author anandam.s
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean enableOrDisableRadiosForGivenSsidUsingWebPaCommand(WiFiFrequencyBand radiotype,
			AutomaticsTapApi tapEnv, Dut device, boolean toEnable) {
		boolean isEnabled = false;
		String enableValue = toEnable ? BroadBandTestConstants.TRUE : BroadBandTestConstants.FALSE;
		LOGGER.info("Going to " + (toEnable ? "Enable" : "Disable") + radiotype.toString());
		switch (radiotype) {

		case WIFI_BAND_2_GHZ:
			isEnabled = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_ENABLED_STATUS,
					WebPaDataTypes.BOOLEAN.getValue(), enableValue);
			break;
		case WIFI_BAND_5_GHZ:
			isEnabled = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_ENABLED_STATUS,
					WebPaDataTypes.BOOLEAN.getValue(), enableValue);
			break;
		default:
			LOGGER.info("Unsupported Radio type");
			break;
		}
		LOGGER.info((toEnable ? "Enable " : "Disable ") + radiotype.toString() + " success");
		return isEnabled;
	}

	/**
	 * Get the radio status of given band
	 * 
	 * @param radiotype - wifi frequency band
	 * @param tapEnv    - instance of {@link AutomaticsTapApi}
	 * @param device    - instance of {@link Dut}
	 * @return radio status
	 * @author anandam.s
	 * @Refactor Sruthi Santhosh
	 */

	public static String getRadioStatus(WiFiFrequencyBand radiotype, AutomaticsTapApi tapEnv, Dut device) {
		String response = null;
		LOGGER.info("Going to get radio status of " + radiotype.toString());
		switch (radiotype) {

		case WIFI_BAND_2_GHZ:
			response = tapEnv.executeWebPaCommand(device,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_2_4GHZ_PRIVATE_SSID);
			break;
		case WIFI_BAND_5_GHZ:
			response = tapEnv.executeWebPaCommand(device,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_5GHZ_PRIVATE_SSID);
			break;
		default:
			LOGGER.info("Unsupported Radio type");
			break;
		}
		LOGGER.info("Radio status of " + radiotype.toString() + ": " + response);
		return response;

	}

	/**
	 * Scan the client device for the networks available and return whether the
	 * device is visible
	 * 
	 * @param connectedDevices - Connected client device instance
	 * @param ssid             - WiFi SSID name
	 * @param tapEnv           - instance of {@link AutomaticsTapApi}
	 * @return true if SSID is visible in client device
	 * @author anandam.s
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean scanAndVerifyVisibleSsidFromWiFiConnectedClientDevice(Dut connectedDevice, String ssid,
			AutomaticsTapApi tapEnv) {

		boolean isVisible = false;
		String[] command = null;
		try {
			if (null != connectedDevice) {
				String osType = ((Device) connectedDevice).getOsType();
				switch (osType) {
				case BroadBandConnectedClientTestConstants.OS_LINUX:
					command = new String[] { BroadBandConnectedClientTestConstants.LINUX_COMMAND_TO_GET_WLAN_NETWORK
							.replace("<INTERFACE>", AutomaticsTapApi.getSTBPropsValue(
									BroadBandTestConstants.PROP_KEY_TO_GET_EXPECTED_WIFI_INTERFACE_IN_LINUX_CLIENT)) };
					break;
				case BroadBandConnectedClientTestConstants.OS_WINDOWS:
					command = new String[] { BroadBandConnectedClientTestConstants.WINDOWS_COMMAND_TO_GET_WLAN_NETWORK
							+ BroadBandTestConstants.SINGLE_SPACE_CHARACTER + BroadBandTestConstants.SYMBOL_PIPE
							+ BroadBandTestConstants.SINGLE_SPACE_CHARACTER + BroadBandTestConstants.CMD_GREP_I
							+ BroadBandTestConstants.SINGLE_SPACE_CHARACTER + ssid };
					break;
				}
				long startTime = System.currentTimeMillis();
				do {
					String commandResponse = tapEnv.executeCommandOnOneIPClients(connectedDevice, command);
					isVisible = CommonMethods.isNotNull(commandResponse)
							&& CommonUtils.patternSearchFromTargetString(commandResponse, ssid);
				} while (!isVisible
						&& (System.currentTimeMillis() - startTime) < BroadBandTestConstants.TWO_MINUTE_IN_MILLIS
						&& BroadBandCommonUtils.hasWaitForDuration(tapEnv,
								BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
			} else {
				LOGGER.error("Connected device is null");
			}
		} catch (Exception exception) {
			LOGGER.error("EXCEPTION OCCURRED SINCE SSID IS NOT LISTED IN THE CLIENT : " + exception.getMessage());
		}
		LOGGER.debug("ENDING METHOD : scanAndVerifyVisibleSsidFromWiFiConnectedClientDevice () ");
		return isVisible;

	}

	public static void resetAllRadios(Dut device, AutomaticsTapApi tapEnv) {
		// Enable 2.4ghz radio
		BroadBandConnectedClientUtils.enableOrDisableRadiosForGivenSsidUsingWebPaCommand(
				WiFiFrequencyBand.WIFI_BAND_2_GHZ, tapEnv, device, true);
		// wait for 90sec .This is given to compensate the time taken for
		// applying
		// settings for each webpa command. This is handled in generic method.
		// So
		// skipping 90 seconds delay.
		// tapEnv.waitTill(BroadBandTestConstants.NINTY_SECOND_IN_MILLIS);
		// Enable 5ghz radio
		BroadBandConnectedClientUtils.enableOrDisableRadiosForGivenSsidUsingWebPaCommand(
				WiFiFrequencyBand.WIFI_BAND_5_GHZ, tapEnv, device, true);
	}

	/**
	 * Method to get the client device from connected client and connect with 2.4
	 * GHz wifi network
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @return 2.4 Ghz client device instance
	 * 
	 * @author Gnanaprakasham.s
	 * @refactor Govardhan
	 */
	public static Dut get2GhzWiFiCapableClientDeviceAndConnectToAssociated2GhzSsid(Dut device,
			AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD : get2GhzWiFiCapableClientDeviceAndConnectToAssociated2GhzSsid () ");
		// device instance to store 2.4GHz client device
		Dut deviceConnectedWith2Ghz = null;
		// variable to store wifi capability
		String wifiCapability = null;
		// variable to store connection type of the connected client
		String connectionType = null;
		// variable to store the SSID of 2.4GHz Wi-Fi Network
		String ssidName = null;
		// variable to store the Pass Phrase of 2.4GHz Wi-Fi Network
		String passPhraseName = null;
		String macAddress = null;
		// Get list of connected client devices
		List<Dut> connectedClientDevices = ((Device) device).getConnectedDeviceList();
		try {
			if (connectedClientDevices != null && connectedClientDevices.size() > 0) {
				// SSID of 2.4GHz Wi-Fi Network
				ssidName = getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv, WiFiFrequencyBand.WIFI_BAND_2_GHZ);
				// Pass Phrase of 2.4GHz Wi-Fi Network
				passPhraseName = getSsidPassphraseFromGatewayUsingWebPaOrDmcli(device, tapEnv,
						WiFiFrequencyBand.WIFI_BAND_2_GHZ);
				// Security Mode of 2.4GHz Wi-Fi Network
				String securityModeOf2GHzNetwork = tapEnv.executeWebPaCommand(device,
						BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_PRIVATE_SECURITY_MODEENABLED);
				LOGGER.info("Security mode obtained for 2.4GHz Wi-Fi network from the Router Device is : "
						+ securityModeOf2GHzNetwork);
				boolean openSecurityMode = CommonMethods.isNotNull(securityModeOf2GHzNetwork)
						&& securityModeOf2GHzNetwork.equalsIgnoreCase(BroadBandTestConstants.SECURITY_MODE_NONE);
				LOGGER.info("Obtained number of client devices is : " + connectedClientDevices.size());
				for (Dut clientSettop : connectedClientDevices) {

					macAddress = clientSettop.getHostMacAddress();
					LOGGER.info("Client device Mac address : " + macAddress);
					connectionType = ((Device) clientSettop).getConnectedDeviceInfo().getConnectionType();
					/*
					 * if (connectionType == ""); { connectionType = "Wi-Fi"; }
					 */
					LOGGER.info("Client device connection type : " + connectionType);
					wifiCapability = ((Device) clientSettop).getConnectedDeviceInfo().getWifiCapability();
					/*
					 * if (wifiCapability == ""); { wifiCapability = "Dual band"; }
					 */
					LOGGER.info("Client device Wi-Fi capability : " + wifiCapability);
					if (CommonMethods.isNotNull(connectionType) && CommonMethods.isNotNull(wifiCapability)) {
						if (connectionType.equalsIgnoreCase(
								BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI)
								&& (wifiCapability.equalsIgnoreCase(
										BroadBandConnectedClientTestConstants.STRING_WIFI_CAPABILITY_2_4GHZ_ONLY)
										|| wifiCapability.equalsIgnoreCase(
												BroadBandConnectedClientTestConstants.STRING_WIFI_CAPABILITY_DUAL_BAND))) {
							if (openSecurityMode) {
								LOGGER.info(
										"Going to connect the client with 2.4GHz Wi-Fi network : Open security mode.");
								deviceConnectedWith2Ghz = ConnectedNattedClientsUtils.connectToSSID(clientSettop,
										tapEnv, ssidName, passPhraseName,
										BroadBandConnectedClientTestConstants.SECURITY_MODE_OPEN.toLowerCase())
												? clientSettop
												: null;
							} else {
								LOGGER.info(
										"Going to connect the client with 2.4GHz Wi-Fi network : Recommanded security mode.");
								deviceConnectedWith2Ghz = ConnectedNattedClientsUtils.connectToSSID(clientSettop,
										tapEnv, ssidName, passPhraseName) ? clientSettop : null;
							}
							if (deviceConnectedWith2Ghz != null
									&& validateConnectedClientPrerequisites(device, deviceConnectedWith2Ghz, tapEnv)) {
								LOGGER.info("Successfully established Wi-Fi connection with 2.4 GHz Wi-Fi network!");

								break;
							} else {
								LOGGER.info(
										"Unable to establish Wi-Fi connection with 2.4 GHz Wi-Fi network from the obtained connected or failed to pass the prerequisite steps");
								deviceConnectedWith2Ghz = null;
							}
						}
					} else {
						LOGGER.error(
								"Obtained null response for client device 'connection type' or 'wifi capability'. Check MDS configuration for the connected client with Wi-Fi Mac Address : "
										+ ((Device) clientSettop).getConnectedDeviceInfo().getWifiCapability());
					}

				}
				if (deviceConnectedWith2Ghz == null) {
					throw new TestException(
							"Unable to establish Wi-Fi connection with 2.4 GHz Wi-Fi network from the obtained connected clients!");
				}
			} else {
				throw new TestException("No connected client is currently associated with the gateway device!");
			}

		} catch (Exception exception) {
			throw new TestException("Unable to connect the client with 2.4GHz Wi-Fi Network due to following Reason : "
					+ exception.getMessage());
		}
		LOGGER.debug("ENDING METHOD : get2GhzWiFiCapableClientDeviceAndConnectToAssociated2GhzSsid () ");
		return deviceConnectedWith2Ghz;
	}

	/**
	 * Method to set private ssid names to standard values following pattern
	 * "RKDB-<MAC address>-<Band>"
	 * 
	 * 
	 * @param device instance of {@link Dut}
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @return status of setting the parameter values true if both are set to value
	 *         false even if one is not set
	 * 
	 * @author Revanth Kumar Vella
	 * @refactor Athira
	 */
	public static boolean setPrivateWifiSsidNamesIn2GhzAnd5GhzToStandardValues(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("SARTING METHOD: setPrivateWifiSsidNamesIn2GhzAnd5GhzToStandardValues");
		BroadBandResultObject result = new BroadBandResultObject();
		String ssidNameFor2Ghz = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.TEXT_RDKB,
				BroadBandTestConstants.CHARACTER_HYPHEN, device.getHostMacAddress(),
				BroadBandTestConstants.CHARACTER_HYPHEN, BroadBandTestConstants.BAND_2_4GHZ);
		String ssidNameFor5Ghz = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.TEXT_RDKB,
				BroadBandTestConstants.CHARACTER_HYPHEN, device.getHostMacAddress(),
				BroadBandTestConstants.CHARACTER_HYPHEN, WiFiFrequencyBand.WIFI_BAND_5_GHZ.getValue());
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID, ssidNameFor2Ghz);
		paramMap.put(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID, ssidNameFor5Ghz);
		result = BroadBandBandSteeringUtils.configureSSIDForBothRadios(device, tapEnv, paramMap);
		LOGGER.debug("ENDING METHOD: setPrivateWifiSsidNamesIn2GhzAnd5GhzToStandardValues");
		return result.isStatus();
	}

	/**
	 * Method to verify SSID visibility from client device
	 * 
	 * @param device   {@link Dut}
	 * @param tapEnv   {@link AutomaticsTapApi}
	 * @param ssidName SSID name
	 * 
	 * @return true is ssid is visible in client device
	 * @refactor Athira
	 */

	public static boolean verifySsidVisibilityInClientDevice(Dut clientDevice, AutomaticsTapApi tapEnv, String ssidName,
			boolean isVisibility) {

		LOGGER.debug("SARTING METHOD: verifySsidVisibilityInClientDevice");

		// Variable to store status
		boolean status = false;
		// Variable to store command response
		String response = null;
		// Variable to store errorMessage
		String errorMessage = null;
		// Variable to store command
		String[] command = null;
		int iteration = 0;

		// Get the OS type of the client device
		String clientOsType = ((Device) clientDevice).getOsType();

		if (CommonMethods.isNotNull(ssidName) && CommonMethods.isNotNull(clientOsType)) {

			// get the command based on OS type
			if (clientOsType.equalsIgnoreCase(BroadBandTestConstants.OS_TYPE_WINDOWS)) {
				command = new String[] { BroadBandConnectedClientTestConstants.WINDOWS_COMMAND_TO_GET_WLAN_NETWORK };
			} else if (clientOsType.equalsIgnoreCase(BroadBandTestConstants.OS_TYPE_LINUX)) {
				command = new String[] { BroadBandConnectedClientTestConstants.LINUX_COMMAND_TO_GET_WLAN_NETWORK };
			}
			for (iteration = 0; iteration < 4; iteration++) {
				response = tapEnv.executeCommandOnOneIPClients(clientDevice, command);
				if (CommonMethods.isNotNull(response)) {
					if (isVisibility) {
						status = response.contains(ssidName);
						if (status) {
							LOGGER.info(ssidName + "is  available in the client device network.");
							break;
						}
					} else {
						status = !response.contains(ssidName);
						if (status) {
							LOGGER.info(ssidName + "is  not available in the client device network.");
							break;
						}
					}
				} else {

					errorMessage = "Obtained null response for " + command + " command execution";
					LOGGER.error(errorMessage);
					if (iteration == 3) {
						throw new TestException(errorMessage);
					}
				}
				LOGGER.info("waiting for two minute before checking the client device again for ssid visibility");
				tapEnv.waitTill(AutomaticsConstants.TWO_MINUTES);
			}
		} else {
			errorMessage = "Either Obtained null response for SSID name or Client device OS type ";
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}
		LOGGER.debug("ENDING METHOD : verifySsidVisibilityInClientDevice () ");
		return status;
	}

	/**
	 * Method to verify whether the client device is connected and to get its ip
	 * address
	 * 
	 * @param device
	 * @param connectedDeviceActivated
	 * @param testId
	 * @author anandam.s
	 * @refactor Athira
	 */
	public static void checkIpAddressAndConnectivity(Dut device, AutomaticsTapApi tapEnv, Dut connectedDeviceActivated,
			String testId, String[] stepNumbers) {
		// String to store the test case status
		boolean status = false;
		// Test step number
		String testStepNumber = stepNumbers[0];
		// String to store the error message
		String errorMessage = null;
		// response obtained
		String commandResponse = null;
		boolean isSystemdPlatforms = false;
		isSystemdPlatforms = DeviceModeHandler.isFibreDevice(device);
		LOGGER.info("Gateway device model is:" + isSystemdPlatforms);

		if (stepNumbers.length == 4) {
			checkIpAddressObtained(device, tapEnv, connectedDeviceActivated, testId,
					new String[] { stepNumbers[0], stepNumbers[1] });

			LOGGER.info("******************************************************");
			LOGGER.info("STEP " + stepNumbers[2]
					+ " : DESCRIPTION : Verify whether you have connectivity using that particular interface using IPV4 ");
			LOGGER.info("STEP " + stepNumbers[2]
					+ " : ACTION : Execute command :Linux :  curl -4 -f --interface <interface name> www.google.com Windows:ping www.google.com -4");
			LOGGER.info("STEP " + stepNumbers[2] + " : EXPECTED: Connectivity check should return status as 200");
			LOGGER.info("******************************************************");
			testStepNumber = stepNumbers[2];
			status = false;
			String command = ((Device) connectedDeviceActivated).getOsType()
					.equalsIgnoreCase(BroadBandConnectedClientTestConstants.OS_LINUX)
							? BroadBandConnectedClientTestConstants.COMMAND_CURL_LINUX_IPV4_ADDRESS
									.replace("<INTERFACE>", BroadbandPropertyFileHandler.getLinuxClientWifiInterface())
							: BroadBandConnectedClientTestConstants.COMMAND_CURL_WINDOWS_IPV4_ADDRESS;
			errorMessage = "Connectivty check using IPV4 address failed";
			commandResponse = tapEnv.executeCommandOnOneIPClients(connectedDeviceActivated, command);
			if (CommonMethods.isNotNull(commandResponse)) {
				status = commandResponse.contains(BroadBandConnectedClientTestConstants.RESPONSE_STATUS_OK);

				if (status) {
					LOGGER.info("STEP " + stepNumbers[2] + " : ACTUAL :  Connectivity check return status as 200");
				} else {
					errorMessage = "Expected 200 OK as response .But obtained " + commandResponse;
					LOGGER.error("STEP " + stepNumbers[2] + " : ACTUAL : " + errorMessage);
				}
			} else {
				errorMessage = "Unable to execute curl command for IPV4 on connected client device. Please check the connectivity between connected client and Jump server.";
			}
			tapEnv.updateExecutionStatus(device, testId, testStepNumber, status, errorMessage, false);

			LOGGER.info("******************************************************");
			LOGGER.info("STEP " + stepNumbers[3]
					+ " : DESCRIPTION : Verify whether you have connectivity using that particular interface using IPV6 ");
			LOGGER.info("STEP " + stepNumbers[3]
					+ " : ACTION : Linux Result:It should return the http status code as 200 Windows Result: Pinging google.com Response should return true");
			LOGGER.info("STEP " + stepNumbers[3] + " : EXPECTED: Connectivity check should return status as 200");
			LOGGER.info("******************************************************");
			testStepNumber = stepNumbers[3];
			status = false;
			errorMessage = "Connectivty check using IPV6 address failed";

			if (BroadbandPropertyFileHandler.isIpv6Enabled() && !isSystemdPlatforms) {

				command = ((Device) connectedDeviceActivated).getOsType()
						.equalsIgnoreCase(BroadBandConnectedClientTestConstants.OS_LINUX)
								? BroadBandConnectedClientTestConstants.COMMAND_CURL_LINUX_IPV6_ADDRESS
								: BroadBandConnectedClientTestConstants.COMMAND_CURL_WINDOWS_IPV6_ADDRESS;
				commandResponse = tapEnv.executeCommandOnOneIPClients(connectedDeviceActivated, command);
				if (CommonMethods.isNotNull(commandResponse)) {
					status = commandResponse.contains(BroadBandConnectedClientTestConstants.RESPONSE_STATUS_OK);
					if (status) {
						LOGGER.info("STEP " + stepNumbers[3] + " : ACTUAL :  Connectivity check return status as 200");
					} else {

						errorMessage = "Expected 200 OK as response .But obtained " + commandResponse;
						LOGGER.error("STEP " + stepNumbers[3] + " : " + errorMessage);
					}
				} else {
					errorMessage = "Unable to execute curl command for IPv6 on connected client device. Please check the connectivity between connected client and Jump server.";
				}
				tapEnv.updateExecutionStatus(device, testId, testStepNumber, status, errorMessage, false);
			} else {
				tapEnv.updateExecutionForAllStatus(device, testId, testStepNumber, ExecutionStatus.NOT_APPLICABLE,
						BroadBandTestConstants.STRING_NOT_APPLICABLE, false);
			}

		} else {
			LOGGER.info("This function is meant for executing 4 steps.Current steps passed are " + stepNumbers.length);
		}
	}

	/**
	 * Common steps for checking IP address obtained for the connected client device
	 * 
	 * @param device
	 * @param connectedDeviceActivated
	 * @param testId
	 * @param stepNumbers
	 * @refactor Athira
	 */
	public static void checkIpAddressObtained(Dut device, AutomaticsTapApi tapEnv, Dut connectedDeviceActivated,
			String testId, String[] stepNumbers) {
		// String to store the test case status
		boolean status = false;
		// Test step number
		String testStepNumber = stepNumbers[0];
		long polling_window_ms = 90000L;
		// String to store the error message
		String errorMessage = null;
		if (stepNumbers.length == 2) {

			LOGGER.info("******************************************************");
			LOGGER.info("STEP " + stepNumbers[0]
					+ " : DESCRIPTION : Verify whether interface got the correct IPv4  address.");
			LOGGER.info("STEP " + stepNumbers[0]
					+ " : ACTION : Connected a 5GHz wifi client with the gateway device's 5GHz wifi network using SSID and PASSWORD");
			LOGGER.info("STEP " + stepNumbers[0] + " : EXPECTED : Interface IP address should be shown");
			LOGGER.info("******************************************************");

			errorMessage = "interface did not get the correct IPV4 address";
			String osType = ((Device) connectedDeviceActivated).getOsType();
			long startTime = System.currentTimeMillis();
			// Loop for this function is a waiting time of max 90sec for the
			// webpa changes to get applied
			do {
				status = BroadBandConnectedClientUtils.verifyIpv4AddressForWiFiOrLanInterfaceConnectedWithRdkbDevice(
						osType, connectedDeviceActivated, tapEnv);
				if (status) {
					LOGGER.info("STEP " + stepNumbers[0]
							+ " : ACTUAL : Successfully verified interface got the correct IPv4  address.");
					break;
				} else {
					LOGGER.info("STEP " + stepNumbers[0] + " : ACTUAL : " + errorMessage);
				}
			} while (System.currentTimeMillis() < (startTime + polling_window_ms));
			tapEnv.updateExecutionStatus(device, testId, testStepNumber, status, errorMessage, false);

			LOGGER.info("******************************************************");
			LOGGER.info("STEP " + stepNumbers[1] + " : Verify whether interface got the correct IPv6  address.");
			LOGGER.info("STEP " + stepNumbers[1]
					+ " : ACTION : Get the device IPv4 address using below command Linux : ifconfig wlan0 |grep -i \"inet6 addr:\"Windows:ipconfig |grep -A 10 \"Wireless LAN adapter Wi-Fi\" |grep -i \"Pv6 Address");
			LOGGER.info("STEP " + stepNumbers[1] + " : EXPECTED:Interface IP address should be shown");
			LOGGER.info("******************************************************");

			testStepNumber = stepNumbers[1];
			status = false;
			if (BroadbandPropertyFileHandler.isIpv6Enabled()) {
				status = BroadBandConnectedClientUtils.verifyIpv6AddressForWiFiOrLanInterfaceConnectedWithRdkbDevice(
						osType, connectedDeviceActivated, tapEnv);
				if (status) {
					LOGGER.info("STEP " + stepNumbers[1]
							+ " : ACTUAL : Successfully verified interface got the correct IPv6  address.");
				} else {
					LOGGER.error("STEP " + stepNumbers[1] + " : ACTUAL : " + errorMessage);
				}
				tapEnv.updateExecutionStatus(device, testId, testStepNumber, status, errorMessage, false);

			} else {
				LOGGER.info("IPv6 is not available/disabled : Skipping Step 6 ...");
				tapEnv.updateExecutionForAllStatus(device, testId, testStepNumber, ExecutionStatus.NOT_APPLICABLE,
						errorMessage, false);
			}

		} else {
			LOGGER.info("This function is meant for executing 2 steps.Current steps passed are " + stepNumbers.length);
		}
	}

	/**
	 * Method to get a 2.4 GHz or 5 GHz Wi-Fi connected client and connect with
	 * respective Wi-Fi network
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @return 2.4 Ghz or 5Ghz client device instance
	 * 
	 * @author Susheela C
	 * @refactor Govardhan
	 */
	public static Dut get2GhzOr5GhzWiFiCapableClientDeviceAndConnectToCorrespondingSsid(Dut device,
			AutomaticsTapApi tapEnv) throws TestException {
		LOGGER.debug("STARTING METHOD : get2GhzOr5GhzWiFiCapableClientDeviceAndConnectToCorrespondingSsid () ");
		// Connected client device instance
		Dut connectedClientDevice = null;
		try {
			connectedClientDevice = BroadBandConnectedClientUtils
					.get2GhzWiFiCapableClientDeviceAndConnectToAssociated2GhzSsid(device, tapEnv);
			LOGGER.info("Successfully obtained a 2.4GHz Capable Wi-Fi client and Connected to 2.4GHz Wi-Fi network.");
		} catch (TestException e) {
			LOGGER.error("Following error occured while trying to get a 2.4GHz connected client. ERROR : "
					+ e.getMessage() + ". Going to try getting a 5GHz connected client!");
			try {
				connectedClientDevice = BroadBandConnectedClientUtils
						.get5GhzWiFiCapableClientDeviceAndConnectToAssociated5GhzSsid(device, tapEnv);
				LOGGER.info("Successfully obtained a 5GHz Capable Wi-Fi client and Connected to 5GHz Wi-Fi network.");
			} catch (TestException exeception) {
				LOGGER.error("Following error occured while trying to get a 5GHz connected client. ERROR : "
						+ exeception.getMessage());
			}
		}
		if (connectedClientDevice == null) {
			throw new TestException("Unable to obtain a 2.4 GHz or 5GHz  Working Wi-Fi connected client!");
		}
		LOGGER.debug("ENDING METHOD : get2GhzOr5GhzWiFiCapableClientDeviceAndConnectToCorrespondingSsid () ");
		return connectedClientDevice;
	}

	/**
	 * Method to connect given connectec client to band 2.4ghz/5ghz
	 * 
	 * @param device       Dut Instance
	 * @param tapEnv       AutomaticsTapApi instance
	 * @param clientSettop Connected client instance
	 * @param wifiBand     Wifi Frequency band
	 * @return Time
	 * @refactor Athira
	 */
	public static long connectToGivenWiFiCapableClientForPerfTest(Dut device, AutomaticsTapApi tapEnv, Dut clientSettop,
			String wifiBand) {
		LOGGER.info("STARTING METHOD : connectToGivenWiFiCapableClientForPerfTest () ");
		// variable to store the SSID Wi-Fi Network
		String ssidName = null;
		// variable to store the Pass Phrase Wi-Fi Network
		String passPhraseName = null;
		String securityModeofNetwork = null;
		// Long to store connection time
		long connectTime = 0;
		WiFiFrequencyBand wifiFrequecyBand = wifiBand.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ)
				? WiFiFrequencyBand.WIFI_BAND_2_GHZ
				: WiFiFrequencyBand.WIFI_BAND_5_GHZ;
		try {
			ssidName = getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv, wifiFrequecyBand);
			passPhraseName = getSsidPassphraseFromGatewayUsingWebPaOrDmcli(device, tapEnv, wifiFrequecyBand);
			securityModeofNetwork = getSsidSecurityModeFromGatewayUsingWebPaOrDmcli(device, tapEnv, wifiFrequecyBand);
			LOGGER.info("SECURITY MODE OBTAINED FOR " + wifiFrequecyBand + " WI-FI NETWORK FROM THE ROUTER DEVICE IS : "
					+ securityModeofNetwork);
			boolean openSecurityMode = CommonMethods.isNotNull(securityModeofNetwork)
					&& securityModeofNetwork.equalsIgnoreCase(BroadBandTestConstants.SECURITY_MODE_NONE);
			if (verfyCnctdClntIsWifiCpble(clientSettop)) {
				if (openSecurityMode) {
					LOGGER.info("GOING TO CONNECT THE CLIENT WITH " + wifiFrequecyBand
							+ " WI-FI NETWORK : OPEN SECURITY MODE.");
					connectTime = ConnectedNattedClientsUtils.connectToSSIDForPerfTest(device, clientSettop, tapEnv,
							ssidName, passPhraseName,
							BroadBandConnectedClientTestConstants.SECURITY_MODE_OPEN.toLowerCase(), false);
				} else {
					LOGGER.info("GOING TO CONNECT THE CLIENT WITH " + wifiFrequecyBand
							+ " WI-FI NETWORK : RECOMMANDED SECURITY MODE.");
					connectTime = ConnectedNattedClientsUtils.connectToSSIDForPerfTest(device, clientSettop, tapEnv,
							ssidName, passPhraseName, false);
				}
				if (connectTime != 0) {
					LOGGER.info("SUCCESSFULLY ESTABLISHED WI-FI CONNECTION WITH " + wifiFrequecyBand
							+ " WI-FI NETWORK AND CAPTURED TIME DIFFERENCE");
				}
			}
			if (connectTime == 0) {
				LOGGER.error("UNABLE TO ESTABLISH WI-FI CONNECTION/PING STATISTICS MISMATCH WITH " + wifiFrequecyBand
						+ " WI-FI NETWORK FROM THE OBTAINED CONNECTED CLIENTS!");
			}
		} catch (Exception exception) {
			throw new TestException("UNABLE TO CONNECT THE CLIENT WITH " + wifiFrequecyBand
					+ " WI-FI NETWORK DUE TO FOLLOWING REASON : " + exception.getMessage());
		}
		LOGGER.info("ENDING METHOD : connectToGivenWiFiCapableClientForPerfTest () ");
		return connectTime;

	}

	/**
	 * Method to get the SSID Security mode using
	 * Device.WiFi.AccessPoint.{i}.Security.X_COMCAST-COM_KeyPassphrase
	 * 
	 * @param device            instance of {@link Dut}
	 * @param tapEnv            instance of {@link AutomaticsTapApi}
	 * @param WiFiFrequencyBand frequency band
	 * @return SSID Security mode
	 * @refactor Athira
	 * 
	 */
	public static String getSsidSecurityModeFromGatewayUsingWebPaOrDmcli(Dut device, AutomaticsTapApi tapEnv,
			WiFiFrequencyBand wifiBand) {
		LOGGER.debug("START METHOD : getSsidSecurityModeFromGatewayUsingWebPaOrDmcli");
		String securityMode = null;

		if (wifiBand.compareTo(WiFiFrequencyBand.WIFI_BAND_2_GHZ) == 0) {
			securityMode = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_PRIVATE_SECURITY_MODEENABLED);

		} else if (wifiBand.compareTo(WiFiFrequencyBand.WIFI_BAND_5_GHZ) == 0) {
			securityMode = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_PRIVATE_SECURITY_MODEENABLED);
		}

		if (!CommonMethods.isNotNull(securityMode)) {
			throw new TestException(
					"Not able to get the security mode for client device connected with " + wifiBand + " GHz ");
		}
		LOGGER.debug("ENDING METHOD : getSsidSecurityModeFromGatewayUsingWebPaOrDmcli");
		return securityMode;

	}

	/**
	 * Method to check whether Given connected client is capable of wifi or not
	 * 
	 * @param connectedClient Connected client Settop instance
	 * @return Wifi Capability status
	 */
	public static boolean verfyCnctdClntIsWifiCpble(Dut connectedClient) {

		// get the Quad atten URL from extra Properties
		// Map<String, String> extraProps = deviceAccount.getExtraProperties();

		String connectionType = ((Device) connectedClient).getConnectedDeviceInfo().getConnectionType();
		LOGGER.info("CLIENT DEVICE CONNECTION TYPE : " + connectionType);
		String wifiCapability = ((Device) connectedClient).getConnectedDeviceInfo().getWifiCapability();
		LOGGER.info("CLIENT DEVICE WI-FI CAPABILITY : " + wifiCapability);
		if (CommonMethods.isNotNull(connectionType) && CommonMethods.isNotNull(wifiCapability)) {
			if (connectionType
					.equalsIgnoreCase(BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI)
					&& (wifiCapability
							.equalsIgnoreCase(BroadBandConnectedClientTestConstants.STRING_WIFI_CAPABILITY_2_4GHZ_ONLY)
							|| wifiCapability.equalsIgnoreCase(
									BroadBandConnectedClientTestConstants.STRING_WIFI_CAPABILITY_DUAL_BAND))) {
				LOGGER.info("DEVICE IS CAPABLE OF WIFI CONNECTIVITY ");
				return true;
			}
		} else {
			LOGGER.error(
					"OBTAINED NULL RESPONSE FOR CLIENT DEVICE 'CONNECTION TYPE' OR 'WIFI CAPABILITY'. CHECK MDS CONFIGURATION FOR THE CONNECTED CLIENT WITH WI-FI MAC ADDRESS : "
							+ ((Device) connectedClient).getConnectedDeviceInfo().getWifiCapability());
			return false;
		}
		return false;
	}

	/**
	 * Utils method to verify if the IP address assigned to the connected client is
	 * between DHCP configured range
	 * 
	 * @param tapEnv                instance of {@link AutomaticsTapApi}
	 * @param device                instance of {@link Dut}
	 * @param connectedClientDevice Connected client device instance
	 * 
	 * @return true if the IP address to the connected client is between configured
	 *         DHCP range
	 * @refactor Said Hisham
	 */
	public static boolean verifyIpv4AddressOFConnectedClientIsBetweenDhcpRange(AutomaticsTapApi tapEnv, Dut device,
			Dut connectedClientDevice) {
		LOGGER.debug("ENTERING METHOD verifyIpv4AddressOFConnectedClientIsBetweenDhcpRange");
		boolean result = false;
		try {

			String dhcpMinRange = tapEnv.executeWebPaCommand(device,
					BroadBandWebPaConstants.WEBPA_PARAM_TO_RETRIEVE_DHCP_STARTING_IP_ADDRESS);
			LOGGER.info("DHCP MINIMUM IP RANGE CONFIGURED FOR GATEWAY : " + dhcpMinRange);
			String dhcpMaxRange = tapEnv.executeWebPaCommand(device,
					BroadBandWebPaConstants.WEBPA_PARAM_TO_RETRIEVE_DHCP_ENDING_IP_ADDRESS);
			LOGGER.info("DHCP MAXIMUM IP RANGE CONFIGURED FOR GATEWAY : " + dhcpMaxRange);
			String ipAddressRetrievedFromClient = getIpv4AddressFromConnClient(tapEnv, device, connectedClientDevice);
			LOGGER.info("IP ADDRESS ASSIGNED TO THE CONNECTED CLIENT FROM DHCP : " + ipAddressRetrievedFromClient);

			if (CommonMethods.isIpv4Address(dhcpMinRange) && CommonMethods.isIpv4Address(dhcpMaxRange)
					&& CommonMethods.isIpv4Address(ipAddressRetrievedFromClient)) {
				if (CommonMethods
						.patternFinder(ipAddressRetrievedFromClient,
								BroadBandTestConstants.PATTERN_TO_RETRIEVE_FIRST_3_DIGITS_OF_IPv4_ADDRESS)
						.equalsIgnoreCase(CommonMethods.patternFinder(dhcpMinRange,
								BroadBandTestConstants.PATTERN_TO_RETRIEVE_FIRST_3_DIGITS_OF_IPv4_ADDRESS))) {

					int minRange = Integer.parseInt(CommonMethods.patternFinder(dhcpMinRange,
							BroadBandTestConstants.PATTERN_TO_RETRIEVE_LAST_DIGIT_OF_IPv4_ADDRESS));
					int maxRange = Integer.parseInt(CommonMethods.patternFinder(dhcpMaxRange,
							BroadBandTestConstants.PATTERN_TO_RETRIEVE_LAST_DIGIT_OF_IPv4_ADDRESS));
					int actualvalue = Integer.parseInt(CommonMethods.patternFinder(ipAddressRetrievedFromClient,
							BroadBandTestConstants.PATTERN_TO_RETRIEVE_LAST_DIGIT_OF_IPv4_ADDRESS));
					result = actualvalue >= minRange && actualvalue <= maxRange;
				}
			}

		} catch (Exception e) {
			LOGGER.error("EXCEPTION OCCURRED WHILE VERIFYING IPv4 ASSIGNNED TO CONNECTED CLIENT IS WITHIN DHCP RANGE : "
					+ e.getMessage());
		}

		LOGGER.info("IS IP ADDRESS ASSIGNED TO THE CONNECTED CLIENT BETWEEN DHCP RANGE : " + result);
		LOGGER.debug("ENDING METHOD verifyIpv4AddressOFConnectedClientIsBetweenDhcpRange");
		return result;

	}

	/**
	 * Helper method to get the ipv4 address of the connected client
	 * 
	 * @param tapEnv                instance of {@link AutomaticsTapApi}
	 * @param device                instance of {@link Dut}
	 * @param connectedClientDevice Connected client device instance
	 * @return Ipv4 of the connected client device
	 * 
	 * @author BALAJI V
	 * @refactor Said Hisham
	 */
	public static String getIpv4AddressFromConnClient(AutomaticsTapApi tapEnv, Dut device, Dut connectedClientDevice) {
		LOGGER.debug("STARTING METHOD: getIpv4AddressFromConnClient");
		String ipv4Value = null;
		Device ecastSettop = (Device) connectedClientDevice;
		if (ecastSettop.isLinux()) {
			ipv4Value = getIpv4AddressFromLinuxConnClient(tapEnv, device, connectedClientDevice);
		} else if (ecastSettop.isWindows()) {
			ipv4Value = getIpOrMacFromWindowsConnectedClient(device, connectedClientDevice, tapEnv, true);
		} else if (ecastSettop.isRaspbianLinux()) {
			ipv4Value = getIpv4AddressFromRaspbianConnClient(tapEnv, device, connectedClientDevice);
		} else {
			LOGGER.error("'getIpv4AddressFromConnClient' IS ONLY APPLICABLE FOR WINDOWS/LINUX OS CONNECTED CLIENTS.");
		}
		LOGGER.debug("ENDING METHOD: getIpv4AddressFromConnClient");
		return ipv4Value;
	}

	/**
	 * Helper method to get the ipv4 address from a linux connected client
	 * 
	 * @param tapEnv                instance of {@link AutomaticsTapApi}
	 * @param device                instance of {@link Dut}
	 * @param connectedClientDevice Connected client device instance
	 * @return Ipv4 address of the linux connected client device
	 * 
	 * @author BALAJI V
	 * @refactor Said Hisham
	 */
	public static String getIpv4AddressFromLinuxConnClient(AutomaticsTapApi tapEnv, Dut device,
			Dut connectedClientDevice) {
		LOGGER.debug("STARTING METHOD: getIpv4AddressFromLinuxConnClient");
		// String to store ipv4 address
		String linuxIpv4Value = null;
		// Instance to store client device
		Device ecastSettop = (Device) connectedClientDevice;
		try {
			if (ecastSettop.isLinux()) {
				String defaultInterface = getDefaultInterfaceNameOfTheLinuxConnClientDevice(connectedClientDevice,
						tapEnv).trim();
				LOGGER.info("DEFAULT INTERFACE OBTAINED IN THE LINUX CLIENT IS  : " + defaultInterface);
				String connectionType = ecastSettop.getConnectedDeviceInfo().getConnectionType();
				LOGGER.info("CONNECTION TYPE OF THE CONNECTED CLIENT IS : " + connectionType);
				String[] expectedInterfaces = null;
				if (CommonMethods.isNotNull(defaultInterface) && CommonMethods.isNotNull(connectionType)
						&& CONNECTION_TYPE_ETHERNET.equalsIgnoreCase(connectionType)) {
					LOGGER.info("GOING TO GET IPV4 ADDRESS FROM ETHERNET INTERFACE");
					expectedInterfaces = AutomaticsTapApi
							.getSTBPropsValue(
									BroadBandTestConstants.PROP_KEY_TO_GET_EXPECTED_ETHERNET_INTERFACE_IN_LINUX_CLIENT)
							.split(BroadBandTestConstants.SEMI_COLON);
				} else if (CommonMethods.isNotNull(defaultInterface)
						&& BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI
								.equalsIgnoreCase(connectionType)) {
					LOGGER.info("GOING TO GET IPV4 ADDRESS FROM WI-FI INTERFACE");
					expectedInterfaces = AutomaticsTapApi
							.getSTBPropsValue(
									BroadBandTestConstants.PROP_KEY_TO_GET_EXPECTED_WIFI_INTERFACE_IN_LINUX_CLIENT)
							.split(BroadBandTestConstants.SEMI_COLON);
				} else {
					String errorMessage = CommonMethods.isNotNull(defaultInterface)
							? "'getIpv4AddressFromLinuxConnClient' METHOD IS ONLY APPLICABLE FOR ETHERNET/WI-FI CONNECTION TYPE"
							: "UNABLE TO OBTAIN THE DEFAULT INTERFACE NAME FROM LINUX CLIENT";
					LOGGER.error(errorMessage);
				}
				if (expectedInterfaces != null && expectedInterfaces.length != 0) {
					for (String interfaceName : expectedInterfaces) {
						if (CommonUtils.patternSearchFromTargetString(defaultInterface, interfaceName)) {
							LOGGER.info(
									"GOING TO GET IPV4 ASSIGNED TO THE CLIENT FROM THE INTERFACE : " + interfaceName);
							String response = tapEnv
									.executeCommandOnOneIPClients(connectedClientDevice,
											BroadBandCommonUtils.concatStringUsingStringBuffer(
													BroadBandTestConstants.COMMAND_TO_GET_IP_CONFIGURATION_DETAILS,
													BroadBandTestConstants.SINGLE_SPACE_CHARACTER, interfaceName))
									.trim();
							linuxIpv4Value = CommonMethods.isNotNull(response) ? CommonMethods
									.patternFinder(response, BroadBandTestConstants.STRING_REGEX_TO_GET_ETHERNET_IPV4)
									.trim() : null;
							if (CommonMethods.isNotNull(linuxIpv4Value)) {
								LOGGER.info("IPv4 OBTAINED FOR ETHERNET CONNECTED LINUX CLIENT IS : " + linuxIpv4Value);
							}

							break;
						}
					}
				}
			} else {
				String errorMessage = ecastSettop.isLinux()
						? "UNABLE TO RETRIEVE DHCP MINIMUM IP RANGE CONFIGURED IN GATEWAY USING WEBPA PARAM 'Device.DHCPv4.Server.Pool.1.MinAddress'"
						: "'getEthernetIpAddressFromWindowsConnectedClient' IS ONLY APPLICABLE FOR LINUX OS CONNECTED CLIENTS.";
				LOGGER.error(errorMessage);
			}
		} catch (Exception e) {
			LOGGER.error("EXCEPTION OCCURRED WHILE RETRIEVING THE IPv4 ASSIGNNED TO LINUX CONNECTED CLIENT. ERROR : "
					+ e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: getIpv4AddressFromLinuxConnClient");
		return linuxIpv4Value;
	}

	/**
	 * Helper method to get Connected client device ip or mac from the same windows
	 * device
	 * 
	 * @param connectedClientDevice Connected client device instance
	 * @param tapEnv                AutomaticsTapApi instance
	 * @param isIpNeeded            true, if ip of the device is needed, false for
	 *                              mac
	 * @return ip of mac of the connected client device
	 * @author Praveenkumar Paneerselvam
	 * @refactor Said Hisham
	 */
	public static String getIpOrMacFromWindowsConnectedClient(Dut device, Dut connectedClientDevice,
			AutomaticsTapApi tapEnv, boolean isIpNeeded) {
		LOGGER.debug("STARTING METHOD: getIpOrMacFromWindowsConnectedClient()");
		String value = null;
		String connectionType = ((Device) connectedClientDevice).getConnectedDeviceInfo().getConnectionType();
		LOGGER.info("Windows device is connected to Gateway device to " + connectionType);
		String command = null;
		String parameter = BroadBandTestConstants.STRING_IP_4_ADDRESS;
		if (!isIpNeeded) {
			parameter = BroadBandTestConstants.STRING_PHYSICAL_ADDRESS;
		}
		String searchTrace = null;
		if (CommonMethods.isNotNull(connectionType) && CONNECTION_TYPE_ETHERNET.equalsIgnoreCase(connectionType)) {
			searchTrace = BroadBandTraceConstants.LOG_MESSAGE_IPCONFIG_ETHERNET;
		} else if (CommonMethods.isNotNull(connectionType)
				&& BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI
						.equalsIgnoreCase(connectionType)) {
			searchTrace = BroadBandTraceConstants.LOG_MESSAGE_IPCONFIG_WIFI;
		}
		if (CommonMethods.isNotNull(searchTrace)) {
			command = BroadBandCommonUtils.concatStringUsingStringBuffer(
					BroadBandCommandConstants.CMD_IPCONFIG_ALL_GREP_A20, searchTrace,
					BroadBandTestConstants.SYMBOL_PIPE, BroadBandTestConstants.GREP_COMMAND,
					BroadBandTestConstants.DOUBLE_QUOTE, parameter, BroadBandTestConstants.DOUBLE_QUOTE);

			LOGGER.info("Command to be executed is " + command);
			String response = tapEnv.executeCommandOnOneIPClients(connectedClientDevice, command);
			if (CommonMethods.isNotNull(response)) {
				String[] responses = response.split(BroadBandTestConstants.STRING_REGEX_PATTERN_NEW_LINE);
				LOGGER.info("response .lenght - " + responses.length);
				for (String ipOrMac : responses) {
					LOGGER.info("response of ip or mac is -" + ipOrMac);
					value = CommonMethods.isNotNull(ipOrMac)
							? CommonMethods.patternFinder(ipOrMac,
									parameter + BroadBandTestConstants.STRING_REGEX_IP_MAC)
							: null;
					LOGGER.info("Value of ip or mac is -" + value + ".");
					if (!isIpNeeded && CommonMethods.isNotNull(value)) {
						value = value.replaceAll(BroadBandTestConstants.SYMBOL_HYPHEN,
								BroadBandTestConstants.DELIMITER_COLON);
					}
					if (CommonMethods.isNotNull(value) && CommonMethods.isNotNull(connectionType)) {
						// Command sample "/sbin/arp -n | grep -i " + value;
						command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.COMMAND_ARP_FOR_WINDOWS,
								BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.SYMBOL_PIPE,
								BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.GREP_COMMAND,
								value);
						response = tapEnv.executeCommandUsingSsh(device, command);
						LOGGER.info("Validate response is -" + response + ".");
						if (CommonMethods.isNull(response)) {
							value = null;
						} else {
							break;
						}
					}
				}
			}
		}
		LOGGER.info("Value of Windows connected client is " + value);
		LOGGER.debug("ENDING METHOD: getIpOrMacFromWindowsConnectedClient()");
		return value;
	}

	/**
	 * Helper method to get the ipv4 address from a Raspbian linux connected client
	 * 
	 * @param tapEnv                instance of {@link AutomaticsTapApi}
	 * @param device                instance of {@link Dut}
	 * @param connectedClientDevice Connected client device instance
	 * @return Ipv4 address of the Raspbian linux connected client device
	 * @refactor Said Hisham
	 * 
	 */

	public static String getIpv4AddressFromRaspbianConnClient(AutomaticsTapApi tapEnv, Dut device,
			Dut connectedClientDevice) {
		LOGGER.debug("STARTING METHOD: getIpv4AddressFromRaspbianLinuxConnClient");
		// String to store ipv4 address
		String raspbianLinuxIpv4Value = null;
		// Instance to store client device
		Device ecastSettop = (Device) connectedClientDevice;
		try {
			if (ecastSettop.isRaspbianLinux()) {
				String defaultInterface = getDefaultInterfaceNameOfTheLinuxConnClientDevice(connectedClientDevice,
						tapEnv).trim();
				LOGGER.info("DEFAULT INTERFACE OBTAINED IN THE LINUX CLIENT IS  : " + defaultInterface);
				String connectionType = ecastSettop.getConnectedDeviceInfo().getConnectionType();
				LOGGER.info("CONNECTION TYPE OF THE CONNECTED CLIENT IS : " + connectionType);
				String[] expectedInterfaces = null;
				if (CommonMethods.isNotNull(defaultInterface) && CommonMethods.isNotNull(connectionType)
						&& CONNECTION_TYPE_ETHERNET.equalsIgnoreCase(connectionType)) {
					LOGGER.info("GOING TO GET IPV4 ADDRESS FROM ETHERNET INTERFACE");
					expectedInterfaces = AutomaticsTapApi
							.getSTBPropsValue(
									BroadBandTestConstants.PROP_KEY_TO_GET_EXPECTED_ETHERNET_INTERFACE_IN_LINUX_CLIENT)
							.split(BroadBandTestConstants.SEMI_COLON);
				} else if (CommonMethods.isNotNull(defaultInterface)
						&& BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI
								.equalsIgnoreCase(connectionType)) {
					LOGGER.info("GOING TO GET IPV4 ADDRESS FROM WI-FI INTERFACE");
					expectedInterfaces = AutomaticsTapApi
							.getSTBPropsValue(
									BroadBandTestConstants.PROP_KEY_TO_GET_EXPECTED_WIFI_INTERFACE_IN_LINUX_CLIENT)
							.split(BroadBandTestConstants.SEMI_COLON);
				} else {
					String errorMessage = CommonMethods.isNotNull(defaultInterface)
							? "'getIpv4AddressFromRaspbianConnClient' METHOD IS ONLY APPLICABLE FOR ETHERNET/WI-FI CONNECTION TYPE"
							: "UNABLE TO OBTAIN THE DEFAULT INTERFACE NAME FROM LINUX CLIENT";
					LOGGER.error(errorMessage);
				}

				if (expectedInterfaces != null && expectedInterfaces.length != 0) {
					for (String interfaceName : expectedInterfaces) {
						if (CommonUtils.patternSearchFromTargetString(defaultInterface, interfaceName)) {
							LOGGER.info(
									"GOING TO GET IPV4 ASSIGNED TO THE CLIENT FROM THE INTERFACE : " + interfaceName);
							String response = tapEnv
									.executeCommandOnOneIPClients(connectedClientDevice,
											BroadBandCommonUtils.concatStringUsingStringBuffer(
													BroadBandTestConstants.COMMAND_TO_GET_IP_CONFIGURATION_DETAILS,
													BroadBandTestConstants.SINGLE_SPACE_CHARACTER, interfaceName))
									.trim();
							raspbianLinuxIpv4Value = CommonMethods.isNotNull(response)
									? CommonMethods
											.patternFinder(response,
													BroadBandTestConstants.STRING_REGEX_TO_GET_RASPIAN_ETHERNET_IPV4)
											.trim()
									: null;
							if (CommonMethods.isNotNull(raspbianLinuxIpv4Value)) {
								LOGGER.info("IPv4 OBTAINED FOR ETHERNET CONNECTED LINUX CLIENT IS : "
										+ raspbianLinuxIpv4Value);
							}
							break;
						}
					}
				}
			} else {
				String errorMessage = ecastSettop.isRaspbianLinux()
						? "UNABLE TO RETRIEVE DHCP MINIMUM IP RANGE CONFIGURED IN GATEWAY USING WEBPA PARAM 'Device.DHCPv4.Server.Pool.1.MinAddress'"
						: "'getIpv4AddressFromRaspbianConnClient' IS ONLY APPLICABLE FOR LINUX OS CONNECTED CLIENTS.";
				LOGGER.error(errorMessage);
			}
		} catch (Exception e) {
			LOGGER.error(
					"EXCEPTION OCCURRED WHILE RETRIEVING THE IPv4 ASSIGNNED TO RASPBIAN LINUX CONNECTED CLIENT. ERROR : "
							+ e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: getIpv4AddressFromRaspbianConnClient");
		return raspbianLinuxIpv4Value;
	}

	/**
	 * Helper method to get device instance of connected client device based on
	 * given connection type
	 * 
	 * @param device                 Dut instance
	 * @param tapEnv                 AutomaticsTapApi Instance
	 * @param connectedClientSettops List of connected client settops
	 * @param connectionType         connection type
	 * @return Dut instance
	 * 
	 * @author Praveenkumar Paneerselvam
	 * @refactor Athira
	 */
	public static Dut getConnectedClientFromConnectionType(Dut device, AutomaticsTapApi tapEnv,
			List<Dut> connectedClientDevices, String connectionType) {
		LOGGER.debug("STARTING METHOD: getConnectedClientBasedOnConnectionType()");
		Dut requiredDevices = null;
		for (Dut clientDevice : connectedClientDevices) {
			if (CommonMethods.isNotNull(connectionType) && connectionType
					.equalsIgnoreCase(((Device) clientDevice).getConnectedDeviceInfo().getConnectionType())) {
				requiredDevices = clientDevice;

				break;
			}
		}
		LOGGER.info(
				"Number of Connected client with connection type " + connectionType + " found is " + requiredDevices);
		LOGGER.debug("ENDING METHOD: getConnectedClientBasedOnConnectionType()");
		return requiredDevices;
	}

	/**
	 * Method to connect clients to either private/PUBLICWiFi wifi
	 * 
	 * @param device
	 * @param tapApi
	 * @param clientDevices
	 * @param wifiType
	 * @refactor Athira
	 */
	public static BroadBandResultObject connectClientsToGivenTypeOfWifi(Dut device, AutomaticsTapApi tapApi,
			Dut clientDevice, WEBPA_AP_INDEXES wifiType, WiFiFrequencyBand band) {
		BroadBandResultObject bandResultObject = new BroadBandResultObject();
		String ssid = null;
		String password = null;
		if (wifiType == WEBPA_AP_INDEXES.PRIVATE_WIFI) {
			ssid = band == WiFiFrequencyBand.WIFI_BAND_2_GHZ
					? tapApi.executeWebPaCommand(device,
							BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_10001_SSID)
					: tapApi.executeWebPaCommand(device,
							BroadBandWebPaConstants.WEBPA_PARAM_Device_WiFi_SSID_10101_SSID);
			password = band == WiFiFrequencyBand.WIFI_BAND_2_GHZ
					? tapApi.executeWebPaCommand(device,
							BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_PRIVATE_SSID_2GHZ_PASSPHRASE)
					: tapApi.executeWebPaCommand(device,
							BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_PRIVATE_SSID_5GHZ_PASSPHRASE);
			bandResultObject
					.setErrorMessage("SSID name and phrase is not obtained for the gateway device. Failed to connect to"
							+ " Broadband Gateway device. SSID NAME - " + ssid + " SSID Passphrase -" + password);
			if (CommonMethods.isNotNull(ssid) && CommonMethods.isNotNull(password)) {
				bandResultObject
						.setStatus(ConnectedNattedClientsUtils.connectToSSID(clientDevice, tapApi, ssid, password));
				bandResultObject
						.setErrorMessage("Attempt to connect client to " + band + " has failed for " + wifiType);
			}
		} else if (wifiType == WEBPA_AP_INDEXES.PUBLIC_WIFI) {
			ssid = getPublicSsidNameFromGatewayUsingWebPaOrDmcli(device, tapApi, band);
			password = (band == WiFiFrequencyBand.WIFI_BAND_2_GHZ)
					? BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapApi,
							BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_PUBLIC_SSID_PASSPHRASE.replace(
									BroadBandTestConstants.STRING_REPLACE,
									BroadBandWebPaConstants.WEBPA_INDEX_2_4_GHZ_PUBLIC_WIFI))
					: BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapApi,
							BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_PUBLIC_SSID_PASSPHRASE.replace(
									BroadBandTestConstants.STRING_REPLACE,
									BroadBandWebPaConstants.WEBPA_INDEX_5_GHZ_PUBLIC_WIFI));
			if (BroadBandWiFiUtils.changeMacFilterStatus(device, tapApi, wifiType, BroadBandTestConstants.FALSE)
					&& CommonMethods.isNotNull(ssid) && CommonMethods.isNotNull(password)) {
				bandResultObject
						.setErrorMessage("Attempt to connect client to " + band + " has failed for " + wifiType);
				LOGGER.info("waiting for a two minutes for the changes to take effect");
				tapApi.waitTill(BroadBandTestConstants.TWO_MINUTE_IN_MILLIS);

			}
		} else if (wifiType == WEBPA_AP_INDEXES.PHASE1_LNF) {
			ssid = band == WiFiFrequencyBand.WIFI_BAND_2_GHZ
					? tapApi.executeWebPaCommand(device,
							BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_10004_SSID)
					: tapApi.executeWebPaCommand(device,
							BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_10104_SSID);
			password = (band == WiFiFrequencyBand.WIFI_BAND_2_GHZ)
					? BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapApi,
							BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_10004_SECURITY_KEYPASSPHRASE)
					: BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapApi,
							BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_10104_SECURITY_KEYPASSPHRASE);
			bandResultObject
					.setErrorMessage("SSID name and phrase is not obtained for the gateway device. Failed to connect to"
							+ " Broadband Gateway device. SSID NAME - " + ssid + " SSID Passphrase -" + password);
			if (CommonMethods.isNotNull(ssid) && CommonMethods.isNotNull(password)) {
				bandResultObject
						.setStatus(ConnectedNattedClientsUtils.connectToSSID(clientDevice, tapApi, ssid, password));
				bandResultObject
						.setErrorMessage("Attempt to connect client to " + band + " has failed for " + wifiType);
			}
		}

		return bandResultObject;
	}

	/**
	 * Method to get the SSID name using Device.WiFi.SSID.{i}.SSID
	 * 
	 * @param device            instance of {@link Dut}
	 * @param tapEnv            instance of {@link AutomaticsTapApi}
	 * @param WiFiFrequencyBand frequency band
	 * @return SSID name
	 * 
	 * @author Praveenkumar Paneerselvam
	 * @refactor Athira
	 */
	public static String getPublicSsidNameFromGatewayUsingWebPaOrDmcli(Dut device, AutomaticsTapApi tapEnv,
			WiFiFrequencyBand wifiBand) {
		LOGGER.debug("START METHOD : getPublicSsidNameFromGatewayUsingWebPaOrDmcli () ");
		String ssidGatewayDevice = null;
		String errorMessage = null;
		if (wifiBand.compareTo(WiFiFrequencyBand.WIFI_BAND_2_GHZ) == 0) {
			ssidGatewayDevice = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PUBLIC_SSID);
		} else if (wifiBand.compareTo(WiFiFrequencyBand.WIFI_BAND_5_GHZ) == 0) {
			ssidGatewayDevice = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PUBLIC_SSID);
		}
		if (CommonMethods.isNull(ssidGatewayDevice)) {
			errorMessage = "Not able to get the Public SSID name for client device connected with " + wifiBand
					+ " GHz ";
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}
		LOGGER.debug("Ending METHOD : getPublicSsidNameFromGatewayUsingWebPaOrDmcli () ");
		return ssidGatewayDevice;
	}

	/**
	 * Helper method to connect given connected client ip to give SSID name.
	 * 
	 * @param device          Dut instance
	 * @param tapEnv          AutomaticsTapapi instance
	 * @param connectedClient connected client device instance, which needs to be
	 *                        connected to Gateway
	 * @param wifi            frequency band.
	 * @return Brandband result object with status true if the given connected
	 *         client is connected to the given wifi band frequency
	 * @author Praveenkumar Paneerselvam
	 * @refactor Alan_Bivera
	 */
	public static BroadBandResultObject connectGivenConnectedClientToWifi(Dut device, AutomaticsTapApi tapEnv,
			Dut connectedClient, WiFiFrequencyBand wifiBand) {
		LOGGER.debug("STARTING METHOD: connectGivenConnectedClientToWifi () ");
		BroadBandResultObject result = new BroadBandResultObject();
		boolean status = false;
		// variable to store wifi capability
		String errorMessage = "connected client device object is not NUC";
		LOGGER.info("connectedClient.getModel()====" + connectedClient.getModel());

		String ssidName = BroadBandConnectedClientUtils.getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv,
				wifiBand);
		String ssidPassPhrase = BroadBandConnectedClientUtils.getSsidPassphraseFromGatewayUsingWebPaOrDmcli(device,
				tapEnv, wifiBand);
		errorMessage = "SSID name and phrase is not obtained for the gateway device. Failed to connect to Broadband Gateway device. SSID NAME - "
				+ ssidName + " SSID Passphrase -" + ssidPassPhrase;
		if (CommonMethods.isNotNull(ssidName) && CommonMethods.isNotNull(ssidPassPhrase)) {
			String connectionType = ((Device) connectedClient).getConnectedDeviceInfo().getConnectionType();
			LOGGER.info("Client device connection type : " + connectionType);
			String wifiCapability = ((Device) connectedClient).getConnectedDeviceInfo().getWifiCapability();
			LOGGER.info("Client device wifi capability : " + wifiCapability);
			errorMessage = "Obtained null value for connection type and wifi capabitlity from MDS ";
			if (CommonMethods.isNotNull(connectionType) && CommonMethods.isNotNull(wifiCapability)) {
				if (connectionType.equalsIgnoreCase(
						BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI)
						&& wifiCapability.equalsIgnoreCase(
								BroadBandConnectedClientTestConstants.STRING_WIFI_CAPABILITY_DUAL_BAND)) {
					status = ConnectedNattedClientsUtils.connectToSSID(connectedClient, tapEnv, ssidName,
							ssidPassPhrase);
					errorMessage = "Failed to connect connected client devices with Broadband Gateway ";
				}
			}
		}

		result.setErrorMessage(errorMessage);
		result.setStatus(status);
		LOGGER.info(" Is the device connected to Gateway Wi-Fi " + status);
		LOGGER.debug("ENDING METHOD: connectGivenConnectedClientToWifi () ");
		return result;
	}

	/**
	 * Utils method to set the device table
	 * 'Device.DHCPv4.Server.Pool.1.StaticAddress.' and return the result.
	 * 
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @param device instance of {@link Dut}
	 * 
	 * @return the WebPA response
	 * 
	 *         refactor Athira
	 */
	public static WebPaServerResponse setDeviceDetailsToDhcpServerPool(AutomaticsTapApi tapEnv, Dut device,
			String wifiMacAddress, String reservedIp) {

		// stores the WebpaServer Response
		WebPaServerResponse webPaServerResponse = null;
		Map<String, List<String>> deviceTable = new HashMap<String, List<String>>();

		// Host Name
		List<String> hostNameList = new ArrayList<String>();
		hostNameList.add(BroadBandTestConstants.STRING_DEVICE_NAME);

		// Wifi MacAddress
		List<String> macAddressList = new ArrayList<String>();
		macAddressList.add(wifiMacAddress);

		// Reserved Ip Address
		List<String> reservedIpAddressList = new ArrayList<String>();
		reservedIpAddressList.add(reservedIp);
		// Comments//
		List<String> commentsList = new ArrayList<String>();
		commentsList.add(BroadBandTestConstants.STRING_DEVICE_COMMENT);

		// adding to the Map.
		deviceTable.put(BroadBandWebPaConstants.WEBPA_PARAM_ADD_DEVICE_NAME, hostNameList);
		deviceTable.put(BroadBandWebPaConstants.WEBPA_PARAM_ADDING_WIFI_MAC_ADDRESS, macAddressList);
		deviceTable.put(BroadBandWebPaConstants.WEBPA_PARAM_ADDING_RESERVED_IP_ADDRESS, reservedIpAddressList);
		deviceTable.put(BroadBandWebPaConstants.WEBPA_PARAM_COMMENT, commentsList);

		webPaServerResponse = tapEnv.postWebpaTableParamUsingRestApi(device,
				BroadBandWebPaConstants.WEBPA_PARAM_ADD_STATIC_ADDRESS, deviceTable);
		return webPaServerResponse;
	}

	/**
	 * Helper method to get Connected client device ip or mac from the same device
	 * 
	 * @param connectedClientDevice Connected client Dut instance
	 * @param tapEnv                AutomaticsTapApi instance
	 * @param isIpNeeded            true if ip is needed, false if mac is needed.
	 * @return client ip of the connected client device
	 * @author Praveenkumar Paneerselvam
	 * @refactor Govardhan
	 */
	public static String getConnectedClientIpOrMacFromTheDevice(Dut device, Dut connectedClientDevice,
			AutomaticsTapApi tapEnv, boolean isIpNeeded) {
		LOGGER.debug("STARTING METHOD: getConnectedClientIpOrMacFromTheDevice");
		String clientIp = null;
		if ((((Device) connectedClientDevice).isLinux()) || (((Device) connectedClientDevice).isRaspbianLinux())) {
			clientIp = getIpOrMacFromLinuxConnectedClient(device, connectedClientDevice, tapEnv, isIpNeeded);
		} else if ((((Device) connectedClientDevice).isWindows())) {
			clientIp = getIpOrMacFromWindowsConnectedClient(device, connectedClientDevice, tapEnv, isIpNeeded);
		}
		LOGGER.info("Ip of the connected client device is - " + clientIp);
		LOGGER.debug("ENDING METHOD: getConnectedClientIpOrMacFromTheDevice");
		return clientIp;
	}

	/**
	 * Helper method to get Connected client device ip or mac from the same linux
	 * device
	 * 
	 * @param connectedClientDevice Connected client device instance
	 * @param tapEnv                AutomaticsTapApi instance
	 * @param isIpNeeded            true, if ip of the device is needed, false for
	 *                              mac
	 * @return ip of mac of the connected client device
	 * @author Praveenkumar Paneerselvam
	 * @refactor Govardhan
	 */
	public static String getIpOrMacFromLinuxConnectedClient(Dut device, Dut connectedClientDevice,
			AutomaticsTapApi tapEnv, boolean isIpNeeded) {
		LOGGER.debug("STARTING METHOD: getIpOrMacFromLinuxConnectedClient()");
		String deviceInfo = null;
		String defaultInterface = getInterfaceNameOfTheEthernetConnLinuxClientDevice(device, connectedClientDevice,
				tapEnv);
		deviceInfo = getIpOrMacFromLinuxInterface(connectedClientDevice, tapEnv, defaultInterface, isIpNeeded);
		LOGGER.info("Device Info of the linux connected client is " + deviceInfo);
		LOGGER.debug("ENDING METHOD: getIpOrMacFromLinuxConnectedClient()");
		return deviceInfo;
	}

	/**
	 * Helper method to get interface name of the ethernet Linux client device
	 * 
	 * @param device                Dut instance
	 * @param connectedClientSettop Connected client device instance
	 * @param tapEnv                AutomaticsTapApi instance
	 * @return interface name of the ethernet
	 * @author Praveenkumar Paneerselvam
	 * @refactor Govardhan
	 */
	public static String getInterfaceNameOfTheEthernetConnLinuxClientDevice(Dut device, Dut connectedClientDevice,
			AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: getInterfaceNameOfTheEthernetConnLinuxClientDevice");
		String interfaceName = null;
		String connectionType = ((Device) connectedClientDevice).getConnectedDeviceInfo().getConnectionType();
		LOGGER.info("Linux device is connected to Gateway device to " + connectionType);
		if (CommonMethods.isNotNull(connectionType)
				&& connectionType.equalsIgnoreCase(BroadBandTestConstants.CONNECTION_TYPE_ETHERNET)) {
			// To get column number of the interface column in route table
			String columnNumber = tapEnv.executeCommandOnOneIPClients(connectedClientDevice,
					BroadBandCommandConstants.CMD_GET_INTERFACE_COLUMN_NUMBER_IN_ROUTE_TABLE);
			// To get interfaces name to which device is connected to
			// route | grep default | awk '{print $8}'
			String command = BroadBandCommandConstants.CMD_ROUTE_ROW_FIRST_COLUMN.replace(
					BroadBandTestConstants.STRING_DOLLAR_SIGN + BroadBandTestConstants.STRING_VALUE_ONE,
					BroadBandTestConstants.STRING_DOLLAR_SIGN + columnNumber);
			String response = tapEnv.executeCommandOnOneIPClients(connectedClientDevice, command);
			String ip = null;
			LOGGER.info("All Interfaces connected in the device - " + response);
			if (CommonMethods.isNotNull(response)) {
				String[] interfaces = response.trim().split(BroadBandTestConstants.DELIMITER_NEW_LINE);
				for (String interFace : interfaces) {
					LOGGER.info("interface name - " + interFace);
					ip = getIpOrMacFromLinuxInterface(connectedClientDevice, tapEnv, interFace, true);
					LOGGER.info("Ip Address is - " + ip);
					if (validateIpInArpCommand(device, tapEnv, ip)) {
						interfaceName = interFace.trim();
						break;
					}
				}
			}
		} else {
			LOGGER.error(
					"Device connection type is not marked in MDS. Device connection type cant be empty or Device connection is not ETHERNET in MDS");
		}
		LOGGER.info("Interface of the Ethernet connected client linux device is - " + interfaceName);
		LOGGER.debug("ENDING METHOD: getInterfaceNameOfTheEthernetConnLinuxClientDevice ");
		return interfaceName;
	}

	/**
	 * Helper method to validate the given ip is present in arp -n command
	 * 
	 * @param device Dut instance
	 * @param tapEnv AutomaticsTapApi instance
	 * @param ip     DHCP ip of the client device
	 * @return true, if DHCP ip provided by the gateway is present in arp -n command
	 * @author Praveenkumar Paneerselvam
	 * @refactor Govardhan
	 */
	public static boolean validateIpInArpCommand(Dut device, AutomaticsTapApi tapEnv, String ip) {
		boolean status = false;
		LOGGER.debug("STARTING METHOD: validateIpInArpCommand ");
		if (CommonMethods.isNotNull(ip) && CommonMethods.isIpv4Address(ip)) {
			// Validating whether ip is correct from arp -n command
			String command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.COMMAND_ARP,
					BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.SYMBOL_PIPE,
					BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.GREP_COMMAND, ip.trim());
			LOGGER.info("Command to be executed - " + command);
			String response = tapEnv.executeCommandUsingSsh(device, command);
			LOGGER.info("Valid IP response is -" + response);
			status = CommonMethods.isNotNull(response);
		}
		LOGGER.info("Is given IP present in arp -n command - " + status);
		LOGGER.debug("ENDING METHOD: validateIpInArpCommand ");
		return status;
	}

	/**
	 * Helper method to get ip or mac from linux interface name provided
	 * 
	 * @param connectedClientDevice Connected client Dut instance
	 * @param tapEnv                AutomaticsTapApi Instance
	 * @param interfaceName         interface Name
	 * @param isIpNeeded            true, if IP required, false for MAC
	 * @return IP or MAC of the given interface
	 * @refactor Govardhan
	 */
	public static String getIpOrMacFromLinuxInterface(Dut connectedClientDevice, AutomaticsTapApi tapEnv,
			String interfaceName, boolean isIpNeeded) {
		LOGGER.debug("STARTING METHOD: getIpOrMacFromLinuxInterface");
		String deviceInfo = null;
		LOGGER.info("Command to be executed is ");
		if (CommonMethods.isNotNull(interfaceName)) {
			if (isIpNeeded) {
				deviceInfo = tapEnv.executeCommandOnOneIPClients(connectedClientDevice,
						BroadBandTestConstants.GET_IP_OF_INTERFACE_FROM_IFCONFIG
								.replaceAll(BroadBandTestConstants.STRING_REPLACE, interfaceName.trim()));
				deviceInfo = CommonMethods.isNotNull(deviceInfo) ? deviceInfo.trim().replaceAll(
						BroadBandTestConstants.STRING_REGEX_ADDR, BroadBandTestConstants.EMPTY_STRING) : null;

			} else {
				deviceInfo = tapEnv.executeCommandOnOneIPClients(connectedClientDevice,
						BroadBandTestConstants.GET_MAC_FROM_INTERFACE_IN_LINUX
								.replaceAll(BroadBandTestConstants.STRING_REPLACE, interfaceName.trim()));
			}
			if (CommonMethods.isNotNull(deviceInfo) && !(isIpNeeded ? CommonMethods.isIpv4Address(deviceInfo)
					: CommonMethods.patternMatcher(deviceInfo,
							BroadBandTestConstants.REG_EXPRESSION_TO_GET_MAC_ADDRESS_SEMICOLON))) {
				deviceInfo = null;
			} else {
				deviceInfo = deviceInfo.trim();
			}
		}
		LOGGER.info("Device info is - " + deviceInfo);
		LOGGER.debug("ENDING METHOD: getIpOrMacFromLinuxInterface");
		return deviceInfo;
	}

	/**
	 * Method to retrieve IPv6 address from connected client
	 * 
	 * @param device
	 * @param tapEnv
	 * @return Ipv6 address
	 * 
	 * @refactor yamini.s
	 */
	public static String retrieveIPv6AddressFromConnectedClientWithDeviceCOnnected(Dut device,
			AutomaticsTapApi tapEnv) {
		String command = null;
		String response = null;
		String connectedClientIpv6Address = null;
		// List to store the ipv6 address
		List<String> ipAddress = new ArrayList<>();
		String patternForIpv6 = null;
		LOGGER.debug("STARTING METHOD: retrieveIPv6AddressFromConnectedClientWithDeviceCOnnected");
		if (((Device) device).getOsType().equalsIgnoreCase(BroadBandConnectedClientTestConstants.OS_RASPBIAN_LINUX)
				|| ((Device) device).getOsType().equalsIgnoreCase(BroadBandConnectedClientTestConstants.OS_LINUX)) {
			connectedClientIpv6Address = getIpv6AddressFromLinuxOrRaspbianConnClient(tapEnv, device);
		} else if (((Device) device).getOsType().equalsIgnoreCase(BroadBandConnectedClientTestConstants.OS_WINDOWS)) {
			command = BroadBandCommandConstants.COMMAND_IPCONFIG;
			patternForIpv6 = BroadBandTestConstants.PATTERN_TO_RETRIVE_IPV6_ADDRESS_FROM_IPCONFIG;
			response = tapEnv.executeCommandOnOneIPClients(device, command);
			if (CommonMethods.isNotNull(response)) {
				ipAddress = BroadBandCommonUtils.patternFinderForMultipleMatches(response, patternForIpv6,
						BroadBandTestConstants.CONSTANT_1);
				for (String ipv6Addr : ipAddress) {
					if (CommonMethods.isIpv6Address(ipv6Addr)) {
						connectedClientIpv6Address = ipv6Addr;
						break;
					}
				}
			}
		}
		LOGGER.debug("ENDING METHOD: retrieveIPv6AddressFromConnectedClientWithDeviceCOnnected");
		return connectedClientIpv6Address;
	}

	/**
	 * Method to get IPv6 address from Linux/Raspbian connected client
	 * 
	 * @param tapEnv
	 * @param connectedClientSettop
	 * @return ipv6 address
	 * 
	 * @refactor yamini.s
	 */
	public static String getIpv6AddressFromLinuxOrRaspbianConnClient(AutomaticsTapApi tapEnv,
			Dut connectedClientSettop) {
		LOGGER.debug("STARTING METHOD: getIpv6AddressFromLinuxOrRaspbianConnClient");
		// String to store ipv4 address
		String linuxIpv6Value = null;
		// Instance to store client device
		Device ecastSettop = (Device) connectedClientSettop;
		try {
			if (ecastSettop.isLinux() || ecastSettop.isRaspbianLinux()) {
				String defaultInterface = getDefaultInterfaceNameOfTheLinuxConnClientDevice(connectedClientSettop,
						tapEnv).trim();
				LOGGER.info("DEFAULT INTERFACE OBTAINED IN THE LINUX/RASPBIAN CLIENT IS  : " + defaultInterface);
				String connectionType = ecastSettop.getConnectedDeviceInfo().getConnectionType();
				LOGGER.info("CONNECTION TYPE OF THE CONNECTED CLIENT IS : " + connectionType);
				String[] expectedInterfaces = null;
				if (CommonMethods.isNotNull(defaultInterface) && CommonMethods.isNotNull(connectionType)
						&& BroadBandTestConstants.CONNECTION_TYPE_ETHERNET.equalsIgnoreCase(connectionType)) {
					LOGGER.info("GOING TO GET IPV6 ADDRESS FROM ETHERNET INTERFACE");
					expectedInterfaces = AutomaticsTapApi
							.getSTBPropsValue(
									BroadBandTestConstants.PROP_KEY_TO_GET_EXPECTED_ETHERNET_INTERFACE_IN_LINUX_CLIENT)
							.split(BroadBandTestConstants.SEMI_COLON);
				} else if (CommonMethods.isNotNull(defaultInterface)
						&& BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI
								.equalsIgnoreCase(connectionType)) {
					LOGGER.info("GOING TO GET IPV6 ADDRESS FROM WI-FI INTERFACE");
					expectedInterfaces = AutomaticsTapApi
							.getSTBPropsValue(
									BroadBandTestConstants.PROP_KEY_TO_GET_EXPECTED_WIFI_INTERFACE_IN_LINUX_CLIENT)
							.split(BroadBandTestConstants.SEMI_COLON);
				} else {
					String errorMessage = CommonMethods.isNotNull(defaultInterface)
							? "UNABLE TO HANDLE IT IS ONLY APPLICABLE FOR ETHERNET/WI-FI CONNECTION TYPE"
							: "UNABLE TO OBTAIN THE DEFAULT INTERFACE NAME FROM LINUX/RASPBIAN CLIENT";
					LOGGER.error(errorMessage);
				}
				if (expectedInterfaces != null && expectedInterfaces.length != 0) {
					for (String interfaceName : expectedInterfaces) {
						if (CommonUtils.patternSearchFromTargetString(defaultInterface, interfaceName)) {
							LOGGER.info(
									"GOING TO GET IPV6 ASSIGNED TO THE CLIENT FROM THE INTERFACE : " + interfaceName);
							String response = tapEnv
									.executeCommandOnOneIPClients(connectedClientSettop,
											BroadBandCommonUtils.concatStringUsingStringBuffer(
													BroadBandTestConstants.COMMAND_TO_GET_IP_CONFIGURATION_DETAILS,
													BroadBandTestConstants.SINGLE_SPACE_CHARACTER, interfaceName))
									.trim();
							List<String> ipAddress = CommonMethods.isNotNull(response)
									? BroadBandCommonUtils.patternFinderForMultipleMatches(response,
											BroadBandTestConstants.STRING_REGEX_TO_GET_ETHERNET_IPV6,
											BroadBandTestConstants.CONSTANT_1)
									: null;
							if (!ipAddress.isEmpty() && null != ipAddress) {
								for (String ipv6Addr : ipAddress) {
									if (CommonMethods.isIpv6Address(ipv6Addr)) {
										linuxIpv6Value = ipv6Addr;
										break;
									}
								}
								LOGGER.info("IPv6 OBTAINED FOR ETHERNET CONNECTED LINUX/RASPBIAN CLIENT IS : "
										+ linuxIpv6Value);
							}
							break;
						}

					}
				}
			} else {
				String errorMessage = "UNABLE TO HANDLE OTHER THAN LINUX/RASPBIAN OS CONNECTED CLIENTS.";
				LOGGER.error(errorMessage);
			}
		} catch (Exception e) {
			LOGGER.error(
					"EXCEPTION OCCURRED WHILE RETRIEVING THE IPv6 ASSIGNNED TO LINUX/RASPBIAN CONNECTED CLIENT. ERROR : "
							+ e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: getIpv6AddressFromLinuxOrRaspbianConnClient");
		return linuxIpv6Value;
	}

	/**
	 * method to verify the operating standard of the connected client device
	 * 
	 * method currently works only for windows connected client. Implementation for
	 * Linux connected client is deferred due to unavailabilty of command
	 * 
	 * @refactor Alan_Bivera
	 */

	public static boolean verifyOperatingStandardInConnectedClient(Dut connectedClientDevice, AutomaticsTapApi tapEnv,
			List<String> expectedOperatingMode) throws TestException {
		LOGGER.debug("Entering verifyOperatingStandardInConnectedClient");
		boolean status = false;
		String clientOsType = null;
		String command = null;
		String response = null;
		String pattermToGetRadioType = "Radio type[\" \"]*:(.*)";
		clientOsType = ((Device) connectedClientDevice).getOsType();
		LOGGER.info("OS type of client device : " + clientOsType);

		if (clientOsType.equalsIgnoreCase(BroadBandConnectedClientTestConstants.OS_WINDOWS)) {
			command = BroadBandConnectedClientTestConstants.WINDOWS_COMMAND_TO_GET_OPERATING_MODE;
			response = tapEnv.executeCommandOnOneIPClients(connectedClientDevice, command);
			if (CommonMethods.isNotNull(response)) {
				response = CommonMethods.patternFinder(response, pattermToGetRadioType);
				LOGGER.info("Response for radio type in Windows client : " + response);

				if (CommonMethods.isNotNull(response) && expectedOperatingMode.contains(response.trim())) {
					status = true;
				} else {

					throw new TestException(
							"failed in verifying the expected operating mode with the mode obatined from box "
									+ response);

				}
			} else {
				throw new TestException(
						"Obtained Null response while validating the operating standard of client device");
			}
		} else if (clientOsType.equalsIgnoreCase(BroadBandConnectedClientTestConstants.OS_LINUX)) {

			throw new TestException("command to get the operating mode for linux client is not known as of now");

		}

		LOGGER.debug("Ending verifyOperatingStandardInConnectedClient");
		return status;

	}

	/**
	 * Method to get the NUC device from connected client and connect with 5 GHz
	 * wifi network
	 * 
	 * @param device                 instance of {@link Dut}
	 * @param AutomaticsTapApi       instance of {@link AutomaticsTapApi}
	 * @param connectedClientDevices Connected client device list
	 * @param wifiBand               Wifi frequency band
	 * @return client device instance
	 * 
	 * @author Gnanaprakasham.s
	 * @Refactor Sruthi Santhosh
	 */
	public static Dut getWindowsClientsAndConnectToGivenSSID(Dut device, AutomaticsTapApi tapEnv,
			WiFiFrequencyBand wifiBand) {

		LOGGER.debug("START METHOD : getWindowsClientsAndConnectToGivenSSID () ");
		// Dut instance to store 2gh client device
		Dut clientDevice = null;
		// variable to store wifi capability
		String wifiCapability = null;
		String errorMessage = null;
		// Get list of connected client devices
		List<Dut> connectedClientDevices = ((Device) device).getConnectedDeviceList();

		String ssidName = getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv, wifiBand);
		String ssidPassPhrase = getSsidPassphraseFromGatewayUsingWebPaOrDmcli(device, tapEnv, wifiBand);

		if (connectedClientDevices != null && connectedClientDevices.size() > 0) {
			for (Dut client_Device : connectedClientDevices) {
				String osType = ((Device) client_Device).getOsType();
				LOGGER.info("Client device OS Type : " + osType);

				if (CommonMethods.isNotNull(osType) && ((Device) client_Device).getOsType()
						.equalsIgnoreCase(BroadBandConnectedClientTestConstants.OS_WINDOWS)) {

					String connectionType = ((Device) client_Device).getConnectedDeviceInfo().getConnectionType();
					LOGGER.info("Client device connection type : " + connectionType);

					wifiCapability = ((Device) client_Device).getConnectedDeviceInfo().getWifiCapability();
					LOGGER.info("Client device wifi capability : " + wifiCapability);

					if (CommonMethods.isNotNull(connectionType) && CommonMethods.isNotNull(wifiCapability)) {
						if (connectionType.equalsIgnoreCase(
								BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI)
								&& wifiCapability.equalsIgnoreCase(
										BroadBandConnectedClientTestConstants.STRING_WIFI_CAPABILITY_DUAL_BAND)) {

							if (ConnectedNattedClientsUtils.connectToSSID(client_Device, tapEnv, ssidName,
									ssidPassPhrase)) {
								clientDevice = client_Device;
								break;
							}
						}
					} else {
						errorMessage = "Obtained null value for connection type and wifi capabitlity from MDS ";
					}

				}

			}

		} else {
			errorMessage = "There is not client device connected with current running gateway device";
		}
		if (clientDevice == null) {
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}

		LOGGER.debug("ENDING METHOD : getWindowsClientsAndConnectToGivenSSID () ");
		return clientDevice;

	}

	/**
	 * Method to get one of the connected client based on the Band and Type.
	 * 
	 * @param device                 instance of {@link Dut}
	 * @param tapEnv                 instance of {@link AutomaticsTapApi}
	 * @param connectedClientSettops Connected client device list
	 * @param Type                   Type of connectivity
	 * @param band                   band of the Wifi(2 or 5 GHZ)
	 * @return client device instance
	 * 
	 * @author Joseph Maduram
	 * @refactor yamini.s
	 */
	public static Dut getConnectedClientBasedOnTypeAndBand(Dut device, AutomaticsTapApi tapEnv,
			List<Dut> connectedClientSettops, String type, String band) {

		// variable to store wifi capability
		String wifiCapability = null;
		// device instance to store the client device
		Dut clientDevice = null;

		try {
			if (null != connectedClientSettops && connectedClientSettops.size() > 0) {
				for (Dut clientSettop : connectedClientSettops) {

					String connectionType = ((Device) clientSettop).getConnectedDeviceInfo().getConnectionType();
					LOGGER.info("Client device connection type : " + connectionType);

					wifiCapability = ((Device) clientSettop).getConnectedDeviceInfo().getWifiCapability();
					LOGGER.info("Client device wifi capability : " + wifiCapability);
					if (CommonMethods.isNotNull(wifiCapability) && CommonMethods.isNotNull(connectionType)
							&& CommonMethods.isNotNull(wifiCapability)) {
						if (connectionType.equalsIgnoreCase(type) && (wifiCapability.equalsIgnoreCase(band)
								|| wifiCapability.equalsIgnoreCase(BroadBandTestConstants.DUAL_BAND))) {

							clientDevice = clientSettop;
							break;

						}
					} else {
						LOGGER.info(
								"Either of the Connection Type  or wifi capability is null in MDS for the client device object-"
										+ ((Device) clientSettop).getConnectedDeviceInfo().toString());

					}
				}
			}
			if (clientDevice == null) {

				throw new TestException(
						"Either client devices are not available or device is not enabled with Wifi port");
			}
		} catch (Exception exception) {
			throw new TestException("Not able to get the connected clients due to => " + exception.getMessage());

		}

		return clientDevice;
	}

	/**
	 * 
	 * method can return a connected client of the required capability except the
	 * passed device object.
	 * 
	 * @param device                 instance of {@link Dut}
	 * @param AutomaticsTapApi       instance of {@link AutomaticsTapApi}
	 * @param connectedClientSettops Connected client device list
	 * @param type                   Type of connectivity
	 * @param band                   band of the Wifi(2 or 5 GHZ)
	 * @return client device instance
	 * 
	 * @author Joseph Maduram
	 * @refactor yamini.s
	 */

	public static Dut getOtherConnectedClient(Dut cnnClientSettop, AutomaticsTapApi tapEnv,
			List<Dut> connectedClientSettops, String type, String band) {
		String wifiCapability = null;
		Dut deviceConnected = null;
		String wifiMacAddress = null;
		String wifiMacAddrConnClient = null;
		String connectionType = null;
		try {
			wifiMacAddrConnClient = ((Device) cnnClientSettop).getConnectedDeviceInfo().getWifiMacAddress();
			LOGGER.info("ALREADY CONNECTED CLIENT'S WIFI MAC ADDRESS : " + wifiMacAddrConnClient);
			if (null != connectedClientSettops && connectedClientSettops.size() > 0) {
				LOGGER.info("NUMBER OF CONNECTED CLIENTS ASSOCIATED WITH THE GATEWAY DEVICE : "
						+ connectedClientSettops.size());
				for (Dut clientSettop : connectedClientSettops) {
					try {
						wifiMacAddress = ((Device) clientSettop).getConnectedDeviceInfo().getWifiMacAddress();
						LOGGER.info("OBTAINED CONNECTED CLIENT WIFI MAC ADDRESS : " + wifiMacAddress);
						connectionType = ((Device) clientSettop).getConnectedDeviceInfo().getConnectionType();
						LOGGER.info("OBTAINED CONNECTED CLIENT CONNECTION TYPE : " + connectionType);
						wifiCapability = ((Device) clientSettop).getConnectedDeviceInfo().getWifiCapability();
						LOGGER.info("OBTAINED CONNECTED CLIENT WIFI CAPABILITY : " + wifiCapability);
						if (CommonMethods.isNull(wifiMacAddress) || CommonMethods.isNull(connectionType)
								|| CommonMethods.isNull(wifiCapability)) {
							LOGGER.error(
									"OBTAINED NULL VALUE FOR WIFI MAC ADDRESS/CONNECTION TYPE/WIFI CAPABILITY, HENCE SKIPPING THIS CLIENT SETTOP.");
							continue;
						}
						if (wifiMacAddrConnClient.equalsIgnoreCase(wifiMacAddress)) {
							LOGGER.error(
									"OBTAINED CONNECTED CLIENT IS ALREADY CONNECTED TO THE GATEWAY, HENCE SKIPPING THIS CLIENT SETTOP.");
							continue;
						}
						LOGGER.info("REQUIRED CONNECTED CLIENT CONNECTION TYPE : " + type);
						LOGGER.info("REQUIRED CONNECTED CLIENT WIFI CAPABILITY : " + band + " OR : Dual band");
						if (connectionType.equalsIgnoreCase(type) && (wifiCapability.equalsIgnoreCase(band)
								|| wifiCapability.equalsIgnoreCase(BroadBandTestConstants.DUAL_BAND))) {
							LOGGER.info("SUCCESSFULLY OBTAINED A CONNECTED CLIENT OTHER THAN : " + wifiMacAddrConnClient
									+ ". WIFI MAC ADDRESS IS : " + wifiMacAddress);
							deviceConnected = clientSettop;
							break;
						} else {
							LOGGER.error(
									"OBTAINED CONNECTED CLIENT'S BAND/WIFI CAPABILITY DOESN'T MATCH THE REQUIREMENT, HENCE SKIPPING THIS CLIENT SETTOP.");
						}
					} catch (Exception exception) {
						LOGGER.error("FOLLOWING EXCEPTION OCCERRED WHILE ITERATING THE CONNECTED CLIENTS : "
								+ exception.getMessage());
					}
				}
			} else {
				throw new TestException("OBTAINED CONNECTED CLIENT LIST IS EMPTY");
			}
			if (deviceConnected == null) {
				throw new TestException(
						"NOT ABLE TO GET A DIFFERENT CONNECTED CLIENT WITH REQUIRED BAND AND CAPABILITY.");
			}
		} catch (Exception exception) {
			throw new TestException("NOT ABLE TO GET OTHER CONNECTED CLIENT DUE TO : " + exception.getMessage());
		}
		return deviceConnected;
	}

	/**
	 * Helper method to enable/disable all radios
	 *
	 * @param device      instance of {@link Dut}
	 * @param tapEnv      instance of {@link AutomaticsTapApi}
	 * @param radioStatus radio status
	 * @return true if radios are enabled/disabled
	 * 
	 * @author Gnanaprakasham.S
	 * @refactor yamini.s
	 */

	public static boolean enableOrDisableAllRadios(Dut device, AutomaticsTapApi tapEnv, boolean radioStatus) {

		String errorMessage = null;
		boolean radioStatus2Ghz = false;
		boolean radioStatus5Ghz = false;
		boolean status = false;

		String radioState = radioStatus ? "enable" : "disable";
		// disable 2.4ghz radio
		radioStatus2Ghz = BroadBandConnectedClientUtils.enableOrDisableRadiosForGivenSsidUsingWebPaCommand(
				WiFiFrequencyBand.WIFI_BAND_2_GHZ, tapEnv, device, radioStatus);
		errorMessage = radioStatus2Ghz ? null
				: "Not able to " + radioState + " 2.4 GHz radio using webpa param \"Device.WiFi.SSID.10001.Enable\"";

		tapEnv.waitTill(BroadBandTestConstants.NINTY_SECOND_IN_MILLIS);
		// Enable 5ghz radio
		radioStatus5Ghz = BroadBandConnectedClientUtils.enableOrDisableRadiosForGivenSsidUsingWebPaCommand(
				WiFiFrequencyBand.WIFI_BAND_5_GHZ, tapEnv, device, radioStatus);
		errorMessage = radioStatus5Ghz ? null
				: errorMessage + " Not able to " + radioState
						+ " 5 GHz radio using webpa param \"Device.WiFi.SSID.10101.Enable\"";

		status = radioStatus2Ghz && radioStatus5Ghz;
		if (!status) {
			LOGGER.error(errorMessage);
		}

		return status;

	}

	/**
	 * Method to verify whether client device has connectivity and ip address when
	 * wifi disabled
	 * 
	 * @param device
	 * @param connectedDeviceActivated
	 * @param testId
	 * @author Gnanaprakasham.s
	 */
	public static void checkIpAddressAndConnectivityAfterWifiDisabled(Dut device, AutomaticsTapApi tapEnv,
			Dut connectedDeviceActivated, String testId, String[] stepNumbers) {
		// String to store the test case status
		boolean status = false;
		// Test step number
		String testStepNumber = stepNumbers[0];
		// String to store the error message
		String errorMessage = null;
		// response obtained
		String commandResponse = null;
		if (stepNumbers.length == 4) {
			checkIpAddressObtainedAfterWifiDisabled(device, tapEnv, connectedDeviceActivated, testId,
					new String[] { stepNumbers[0], stepNumbers[1] });

			LOGGER.info("******************************************************");
			LOGGER.info("STEP " + stepNumbers[2]
					+ ": Verify whether you have connectivity using that particular interface using IPV4 ");
			LOGGER.info("EXPECTED: Connectivity check should return connection failure error message");
			LOGGER.info("******************************************************");
			testStepNumber = stepNumbers[2];
			status = false;
			String command = ((Device) connectedDeviceActivated).getOsType()
					.equalsIgnoreCase(BroadBandConnectedClientTestConstants.OS_LINUX)
							? BroadBandConnectedClientTestConstants.COMMAND_CURL_LINUX_IPV4_ADDRESS
									.replace("<INTERFACE>", BroadbandPropertyFileHandler.getLinuxClientWifiInterface())
							: BroadBandConnectedClientTestConstants.COMMAND_CURL_WINDOWS_IPV4_ADDRESS;
			errorMessage = "Connectivty check using IPV4 address failed";
			commandResponse = tapEnv.executeCommandOnOneIPClients(connectedDeviceActivated, command);
			if (CommonMethods.isNotNull(commandResponse)) {
				status = commandResponse.contains("Failed to connect to www.google.com")
						|| commandResponse.contains("Could not resolve host");
				if (!status) {
					errorMessage = "Expected Failed to connect to www.google.com as response .But obtained "
							+ commandResponse;
				}
			} else {
				errorMessage = "Unable to execute curl command for IPV4 on connected client device. Please check the connectivity between connected client and Jump server.";
			}
			tapEnv.updateExecutionStatus(device, testId, testStepNumber, status, errorMessage, false);

			LOGGER.info("******************************************************");
			LOGGER.info("STEP " + stepNumbers[3]
					+ ": Verify whether you have connectivity using that particular interface using IPV6 ");
			LOGGER.info("EXPECTED: Connectivity check should return network not reachable error message ");
			LOGGER.info("******************************************************");
			testStepNumber = stepNumbers[3];
			status = false;
			errorMessage = "Connectivty check using IPV6 address failed";
			command = ((Device) connectedDeviceActivated).getOsType()
					.equalsIgnoreCase(BroadBandConnectedClientTestConstants.OS_LINUX)
							? BroadBandConnectedClientTestConstants.COMMAND_CURL_LINUX_IPV6_ADDRESS
							: BroadBandConnectedClientTestConstants.COMMAND_CURL_WINDOWS_IPV6_ADDRESS;
			commandResponse = tapEnv.executeCommandOnOneIPClients(connectedDeviceActivated, command);
			if (CommonMethods.isNotNull(commandResponse)) {
				status = commandResponse.contains("Failed to connect to www.google.com")
						|| commandResponse.contains("Could not resolve host");
				if (!status) {
					errorMessage = "Expected Failed to connect to www.google.com as response .But obtained "
							+ commandResponse;
				}
			} else {
				errorMessage = "Unable to execute curl command for IPv6 on connected client device. Please check the connectivity between connected client and Jump server.";
			}
			tapEnv.updateExecutionStatus(device, testId, testStepNumber, status, errorMessage, false);
		} else {
			LOGGER.info("This function is meant for executing 4 steps.Current steps passed are " + stepNumbers.length);
		}
	}

	/**
	 * Common steps for checking IP address obtained for the connected client device
	 * when wifi disabled
	 * 
	 * @param device
	 * @param connectedDeviceActivated
	 * @param testId
	 * @param stepNumbers
	 */
	public static void checkIpAddressObtainedAfterWifiDisabled(Dut device, AutomaticsTapApi tapEnv,
			Dut connectedDeviceActivated, String testId, String[] stepNumberss) {
		// String to store the test case status
		boolean status = false;
		// Test step number
		String testStepNumber = stepNumberss[0];
		long polling_window_ms = 90000L;
		// String to store the error message
		String errorMessage = null;
		if (stepNumberss.length == 2) {

			LOGGER.info("******************************************************");
			LOGGER.info("STEP " + stepNumberss[0] + ":Verify whether interface got the correct IPv4  address.");
			LOGGER.info("EXPECTED:Interface IP address should not be shown");
			LOGGER.info("******************************************************");

			errorMessage = "wifi interface is having ipv4 address even after wifi disabled";
			String osType = ((Device) connectedDeviceActivated).getOsType();
			long startTime = System.currentTimeMillis();

			do {
				status = !BroadBandConnectedClientUtils.verifyIpv4AddressForWiFiOrLanInterfaceConnectedWithRdkbDevice(
						osType, connectedDeviceActivated, tapEnv);
				if (status) {
					break;
				}
			} while (System.currentTimeMillis() < (startTime + polling_window_ms));
			tapEnv.updateExecutionStatus(device, testId, testStepNumber, status, errorMessage, false);

			LOGGER.info("******************************************************");
			LOGGER.info("STEP " + stepNumberss[1] + ":Verify whether interface got the correct IPv6  address.");
			LOGGER.info("EXPECTED:Interface IP address should not be shown");
			LOGGER.info("******************************************************");

			testStepNumber = stepNumberss[1];
			status = false;
			errorMessage = "wifi interface is having ipv6 address even after wifi disabled";
			status = !BroadBandConnectedClientUtils.verifyIpv6AddressForWiFiOrLanInterfaceConnectedWithRdkbDevice(
					osType, connectedDeviceActivated, tapEnv);
			tapEnv.updateExecutionStatus(device, testId, testStepNumber, status, errorMessage, false);
		} else {
			LOGGER.info("This function is meant for executing 2 steps.Current steps passed are " + stepNumberss.length);
		}
	}

	/**
	 * Method to verify Renewing IPv4 connected client is in DHCP range
	 * 
	 * @param tapEnv                {@link AutomaticsTapApi}
	 * @param device                {@link Dut}
	 * @param connectedClientSettop Connected client device instance
	 * 
	 * @return status true if Renew of IP Address is Successful in the Connected
	 *         Client
	 * @refactor Govardhan
	 */
	public static boolean renewIpAddressInDhcp(Dut device, AutomaticsTapApi tapEnv, Dut connectedClientSettop) {
		LOGGER.debug("STARTING METHOD: renewIpAddressInDhcp");
		boolean result = false;
		String osType = ((Device) connectedClientSettop).getOsType();
		Device ecastSettop = (Device) connectedClientSettop;
		try {
			if (CommonMethods.isNotNull(osType) && osType.equals(BroadBandConnectedClientTestConstants.OS_WINDOWS)) {
				tapEnv.executeCommandOnOneIPClients(connectedClientSettop,
						BroadBandCommandConstants.CMD_TO_RENEW_IP_IN_WINDOWS);
				LOGGER.info("Waiting for two minutes to reflect IP changes");
				tapEnv.waitTill(BroadBandTestConstants.TWO_MINUTE_IN_MILLIS);
				result = true;
			} else {
				String defaultInterface = getDefaultInterfaceNameOfTheLinuxConnClientDevice(connectedClientSettop,
						tapEnv).trim();
				LOGGER.info("DEFAULT INTERFACE OBTAINED IN THE LINUX CLIENT IS  : " + defaultInterface);
				String connectionType = ecastSettop.getConnectedDeviceInfo().getConnectionType();
				LOGGER.info("CONNECTION TYPE OF THE CONNECTED CLIENT IS : " + connectionType);
				String[] expectedInterfaces = null;
				if (CommonMethods.isNotNull(defaultInterface) && CommonMethods.isNotNull(connectionType)
						&& BroadBandTestConstants.CONNECTION_TYPE_ETHERNET.equalsIgnoreCase(connectionType)) {
					expectedInterfaces = AutomaticsTapApi
							.getSTBPropsValue(
									BroadBandTestConstants.PROP_KEY_TO_GET_EXPECTED_ETHERNET_INTERFACE_IN_LINUX_CLIENT)
							.split(BroadBandTestConstants.SEMI_COLON);
				} else if (CommonMethods.isNotNull(defaultInterface) && CommonMethods.isNotNull(connectionType)
						&& BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI
								.equalsIgnoreCase(connectionType)) {
					expectedInterfaces = AutomaticsTapApi
							.getSTBPropsValue(
									BroadBandTestConstants.PROP_KEY_TO_GET_EXPECTED_WIFI_INTERFACE_IN_LINUX_CLIENT)
							.split(BroadBandTestConstants.SEMI_COLON);
				} else {
					LOGGER.error("UNABLE TO OBTAIN THE DEFAULT INTERFACE NAME FROM LINUX CLIENT");
				}

				if (expectedInterfaces != null && expectedInterfaces.length != 0) {
					for (String interfaceName : expectedInterfaces) {
						if (CommonUtils.patternSearchFromTargetString(defaultInterface, interfaceName)) {
							LOGGER.info("Renew IP in connected client: " + interfaceName);
							tapEnv.executeCommandOnOneIPClients(connectedClientSettop,
									BroadBandCommandConstants.CMD_TO_RELEASE_IP_CLIENT_INTERFACE_IN_LINUX.replace(
											BroadBandTestConstants.STRING_INTERFACE_TO_REPLACE, interfaceName));
							tapEnv.executeCommandOnOneIPClients(connectedClientSettop,
									BroadBandCommandConstants.CMD_TO_RENEW_IP_CLIENT_INTERFACE_IN_LINUX.replace(
											BroadBandTestConstants.STRING_INTERFACE_TO_REPLACE, interfaceName));
							LOGGER.info("Waiting for one minute to reflect IP changes");
							tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
							result = true;
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception in Renewing DHCP Address in DHCP of connected client." + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: renewIpAddressInDhcp");
		return result;
	}

	/**
	 * Helper method to get the ipv4 address of the connected client without
	 * performing ARP Check
	 * 
	 * @param tapEnv                instance of {@link AutomaticsTapApi}
	 * @param device                instance of {@link Dut}
	 * @param connectedClientDevice Connected client device instance
	 * @return Ipv4 of the connected client device
	 * 
	 * @author Vignesh
	 * @Refactor Sruthi Santhosh
	 */
	public static String getIpv4AddressFromConnClientWithoutArpCheck(AutomaticsTapApi tapEnv, Dut device,
			Dut connectedClientDevice) {
		LOGGER.debug("STARTING METHOD: getIpv4AddressFromConnClientWithoutArpCheck");
		String ipv4Value = null;
		try {
			Device connClientDevice = (Device) connectedClientDevice;
			if (connClientDevice.isLinux()) {
				ipv4Value = getIpv4AddressFromLinuxConnClient(tapEnv, device, connectedClientDevice);
			} else if (connClientDevice.isWindows()) {
				ipv4Value = getIpFromWindowsConnectedClientWithoutArpCheck(device, connectedClientDevice, tapEnv);
			} else if (connClientDevice.isRaspbianLinux()) {
				ipv4Value = getIpv4AddressFromRaspbianConnClient(tapEnv, device, connectedClientDevice);
			} else {
				LOGGER.error(
						"'getIpv4AddressFromConnClientWithoutArpCheck' IS ONLY APPLICABLE FOR WINDOWS/LINUX OS CONNECTED CLIENTS.");
			}
		} catch (Exception e) {
			LOGGER.error("Exception While Getting the IPV4 Address From The Client." + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: getIpv4AddressFromConnClientWithoutArpCheck");
		return ipv4Value;
	}

	/**
	 * Helper method to get Connected client device ip from the same windows device
	 * without performing Sbin\ARP Check
	 * 
	 * @param device                Dut Instance
	 * @param connectedClientDevice Connected client device instance
	 * @param tapEnv                AutomaticstapApi instance
	 * @return ip of the connected client device
	 * @Refactor Sruthi Santhosh
	 */
	public static String getIpFromWindowsConnectedClientWithoutArpCheck(Dut device, Dut connectedClientDevice,
			AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: getIpFromWindowsConnectedClientWithoutArpCheck()");
		String value = null;
		String command = null;
		String searchTrace = null;
		String response = null;
		String connectionType = ((Device) connectedClientDevice).getConnectedDeviceInfo().getConnectionType();
		try {
			LOGGER.info("Windows device is connected to Gateway device to " + connectionType);
			String parameter = BroadBandTestConstants.STRING_IP_4_ADDRESS;
			if (CommonMethods.isNotNull(connectionType)
					&& BroadBandConnectedClientUtils.CONNECTION_TYPE_ETHERNET.equalsIgnoreCase(connectionType)) {
				searchTrace = BroadBandTraceConstants.LOG_MESSAGE_IPCONFIG_ETHERNET;
			} else if (CommonMethods.isNotNull(connectionType)
					&& BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI
							.equalsIgnoreCase(connectionType)) {
				searchTrace = BroadBandTraceConstants.LOG_MESSAGE_IPCONFIG_WIFI;
			}
			if (CommonMethods.isNotNull(searchTrace)) {
				command = BroadBandCommonUtils.concatStringUsingStringBuffer(
						BroadBandCommandConstants.CMD_IPCONFIG_ALL_GREP_A40, searchTrace,
						BroadBandTestConstants.SYMBOL_PIPE, BroadBandTestConstants.GREP_COMMAND,
						BroadBandTestConstants.DOUBLE_QUOTE, parameter, BroadBandTestConstants.DOUBLE_QUOTE);
				LOGGER.info("Command to be executed is " + command);
				response = tapEnv.executeCommandOnOneIPClients(connectedClientDevice, command);
				if (CommonMethods.isNotNull(response)) {
					value = CommonMethods.patternFinder(response, BroadBandTestConstants.PATTERN_TO_GET_IPV4_ADDRESS);
					LOGGER.info("Value of ip is - " + value);
				}
			}
			LOGGER.info("Value of Windows connected client is " + value);
		} catch (Exception e) {
			LOGGER.error("Exception While Getting the IPV4 Address From The Client." + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: getIpFromWindowsConnectedClientWithoutArpCheck()");
		return value;
	}

	/**
	 * Helper method to disconnect connected client from Public 2.4Ghz / 5Ghz SSIDs,
	 * if connected. If not connected to 2.4GHz / 5GHz Wi-fi networks the method
	 * returns true.
	 * 
	 * @param tapEnv                {@link AutomaticsTapApi}
	 * @param device                {@link Dut}
	 * @param connectedClientSettop Connected client device instance
	 * @return status
	 * @refactor Govardhan
	 */
	public static boolean validateDhcpIpv4AddressBetweenRangeInConnectedClient(String beginIpAddress,
			String endingIpAddress, String ipAddressToVerify) {
		LOGGER.debug("STARTING METHOD: validateDhcpIpv4AddressBetweenRangeInConnectedClient()");
		boolean status = false;
		long dhcpIpv4BeginAddress = 0;
		long dhcpIpvEndingAddress = 0;
		long dhcpIpv4ddressToVerify = 0;
		try {
			dhcpIpv4BeginAddress = convertIpAddressToLong(InetAddress.getByName(beginIpAddress));
			dhcpIpvEndingAddress = convertIpAddressToLong(InetAddress.getByName(endingIpAddress));
			dhcpIpv4ddressToVerify = convertIpAddressToLong(InetAddress.getByName(ipAddressToVerify));
			status = dhcpIpv4ddressToVerify >= dhcpIpv4BeginAddress && dhcpIpv4ddressToVerify <= dhcpIpvEndingAddress;
		} catch (Exception e) {
			LOGGER.error("Exception occurred validateDhcpIpv4AddressBetweenRangeInConnectedClient() " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: validateDhcpIpv4AddressBetweenRangeInConnectedClient()");
		return status;
	}

	/**
	 * Helper method to disconnect connected client from Public 2.4Ghz / 5Ghz SSIDs,
	 * if connected. If not connected to 2.4GHz / 5GHz Wi-fi networks the method
	 * returns true.
	 * 
	 * @param tapEnv                {@link AutomaticsTapApi}
	 * @param device                {@link Dut}
	 * @param connectedClientSettop Connected client settop instance
	 * @return status
	 * @refactor Govardhan
	 */
	public static long convertIpAddressToLong(InetAddress ip) {
		LOGGER.debug("STARTING METHOD: convertIpAddressToLong()");
		long result = 0;
		try {
			byte[] octets = ip.getAddress();
			for (byte octet : octets) {
				result <<= 8;
				result |= octet & 0xff;
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred converting Ip Address to Long " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: convertIpAddressToLong()");
		return result;
	}

	/**
	 * Method to get current security mode for wifi network using webpa command
	 * 
	 * @param device               instance of {@link Dut}
	 * @param tapEnv               instance of {@link AutomaticsTapApi}
	 * @param command              webpa params needs to be executed
	 * @param expectedSecurityMode expectedSecurityMode
	 * @return true if security mode returned as expected
	 * 
	 * @author Gnanaprakasham.s
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean verifyWiFiSecutityModeUsingWebPaOrDmcliCommand(Dut device, AutomaticsTapApi tapEnv,
			String command, String expectedSecurityMode) {
		LOGGER.debug("STARTING METHOD : verifyWiFiSecutityModeUsingWebPaOrDmcliCommand () ");
		String response = null;
		boolean status = false;
		String errorMessage = null;

		response = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv, command);

		if (CommonMethods.isNotNull(response)) {
			LOGGER.info("Successfully obtained " + expectedSecurityMode + " security mode using webpa command : "
					+ response);
			status = response.equalsIgnoreCase(expectedSecurityMode);

			if (!status) {
				errorMessage = "Failed to get " + expectedSecurityMode + " security mode!!..Obtained security mode : "
						+ response;
				throw new TestException(errorMessage);
			}

		} else {
			errorMessage = "Failed to get " + expectedSecurityMode
					+ " security mode using webpa command..Obtained ressponse " + response;
			throw new TestException(errorMessage);

		}
		LOGGER.debug("ENDING METHOD : verifyWiFiSecutityModeUsingWebPaOrDmcliCommand () ");
		return status;
	}

	/**
	 * Method to verify security mode from client devices
	 * 
	 * @param clientDevice         Client device instance
	 * @param response             Obtained response from device
	 * @param pattern              pattern to be searched
	 * @param expectedSecurityMode expected security mode
	 * @return true if security mode returned as expected
	 * 
	 * @author Gnanaprakasham.s
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean verifyWiFiSecurityModeFromClientDeviceUsingSystemCommand(Dut clientDevice,
			AutomaticsTapApi tapEnv, String[] command, String pattern, String expectedSecurityMode) {
		LOGGER.debug("STARTING METHOD : verifyWiFiSecurityModeFromClientDeviceUsingSystemCommand () ");

		boolean status = false;
		String errorMessage = null;
		String response = null;

		response = tapEnv.executeCommandOnOneIPClients(clientDevice, command);

		if (CommonMethods.isNotNull(response)) {

			String securityMode = CommonMethods.patternFinder(response, pattern);
			LOGGER.info("Obtained security mode from client device : " + securityMode);

			if (CommonMethods.isNotNull(securityMode)) {

				status = securityMode.equalsIgnoreCase(expectedSecurityMode);
				errorMessage = "Obtained security mode from client device is" + securityMode
						+ " but expected mode should be " + expectedSecurityMode;
			} else {
				errorMessage = "Obtained null response.. Not able to get the security mode from client device";
			}
		} else {
			errorMessage = "Obtained null response for command => " + command;
		}
		if (!status) {
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}
		LOGGER.debug("ENDING METHOD : verifyWiFiSecurityModeFromClientDeviceUsingSystemCommand () ");
		return status;
	}

	/**
	 * Method to set security mode for wifi network using webpa command
	 * 
	 * @param device             instance of {@link Dut}
	 * @param securityMode       webpa command for security mode
	 * @param securityModeValues securityMode Values
	 * @param encryption         method webpa param for encryption method
	 * @param encryption         method encryption method value
	 * @return true if security mode successfully set
	 * 
	 * @author Gnanaprakasham.s
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean setSecurityModeForWiFiNetwork(AutomaticsTapApi tapEnv, Dut device, String securityModeValues,
			String encryptionMethodValue, WiFiFrequencyBand wifiBand) {
		LOGGER.debug("STARTING METHOD : setSecurityModeForWiFiNetwork () ");
		boolean status = false;
		String errorMessage = null;
		String securityMode = null;
		String encryptionMethod = null;

		if (wifiBand.compareTo(WiFiFrequencyBand.WIFI_BAND_2_GHZ) == 0) {

			securityMode = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_PRIVATE_SECURITY_MODEENABLED;
			encryptionMethod = BroadBandWebPaConstants.WEBPA_PARAM_FOR_ENCRYPTIONMETHOD_IN_2GHZ_PRIVATE_WIFI;

		} else if (wifiBand.compareTo(WiFiFrequencyBand.WIFI_BAND_5_GHZ) == 0) {
			securityMode = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_PRIVATE_SECURITY_MODEENABLED;
			encryptionMethod = BroadBandWebPaConstants.WEBPA_PARAM_FOR_ENCRYPTIONMETHOD_IN_5GHZ_PRIVATE_WIFI;
		}

		WebPaParameter webPaParamSecuityMode = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(securityMode,
				securityModeValues, WebPaDataTypes.STRING.getValue());
		WebPaParameter webPaParamEncryption = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
				encryptionMethod, encryptionMethodValue, WebPaDataTypes.STRING.getValue());

		List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
		webPaParameters.add(webPaParamSecuityMode);
		webPaParameters.add(webPaParamEncryption);

		WebPaServerResponse webPaSetResponse = tapEnv.setWebPaParameterValues(device, webPaParameters);

		if (webPaSetResponse.getMessage().trim().equalsIgnoreCase(BroadBandTestConstants.SUCCESS_TXT)) {
			status = true;
		} else {

			errorMessage = "Failed to set " + securityMode + " parameter to  " + securityModeValues + " and "
					+ encryptionMethod + " parameter to " + encryptionMethodValue;
			LOGGER.info(errorMessage);
			throw new TestException(errorMessage);

		}
		LOGGER.debug("ENDING METHOD : setSecurityModeForWiFiNetwork () ");
		return status;

	}

	/**
	 * 
	 * This method is to verify if the hostaddress passed is blocked on the dns
	 * server by checking the DNS Configuration
	 * 
	 * @param tapEnv                Tap environment
	 * @param connectedClientSettop Settop object
	 * @param hostAddress           site to look up
	 * @param dnsServer             dns server on which the validation has to be
	 *                              done
	 * @param expecteDomainName     hostAddress passed domain name expected for the
	 * @return status of the operation
	 * @refactor Alan_Bivera
	 */
	public static boolean verifyDNSConfigInCnctdClientPassingHostAddrAndDnsSrvr(AutomaticsTapApi tapEnv,
			Dut connectedClientSettop, String hostAddress, String dnsServer, String expecteDomainName) {
		LOGGER.debug("ENDING METHOD : verifyDNSConfigInCnctdClientPassingHostAddrAndDnsSrvr()");
		// String to hold the response
		String response = null;
		// Status of dns overRiding at client mac Level
		boolean status = false;
		// Instance to store the client settop
		Device ecastSettop = (Device) connectedClientSettop;
		// String to store ssh command
		String sshCommand = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.STRING_NS_LOOKUP,
				AutomaticsConstants.SPACE, hostAddress, AutomaticsConstants.SPACE, dnsServer);
		response = tapEnv.executeCommandOnOneIPClients(ecastSettop, sshCommand);
		response = response.replaceAll(BroadBandTestConstants.PATTERN_MATCHER_FOR_MULTIPLE_SPACES,
				BroadBandCommandConstants.BLANK);
		status = (CommonMethods.patternMatcher(response, "Name:" + expecteDomainName) || CommonMethods.patternMatcher(
				response, "Name:" + expecteDomainName.replace("www.", BroadBandCommandConstants.BLANK)));
		LOGGER.debug("ENDING METHOD : verifyDNSConfigInCnctdClientPassingHostAddrAndDnsSrvr()");
		return status;
	}

	/**
	 * Helper method to disconnect connected client from 2.4Ghz / 5Ghz SSIDs, if
	 * connected. If not connected to 2.4GHz / 5GHz Wi-fi networks the method
	 * returns true.
	 * 
	 * @param tapEnv                instance of {@link AutomaticsTapApi}
	 * @param device                instance of {@link Dut}
	 * @param connectedClientDevice Connected client device instance
	 * @return status
	 * 
	 * @author BALAJI V
	 * @refactor Govardhan
	 */
	public static BroadBandResultObject disconnectCnnClientFromSsid(AutomaticsTapApi tapEnv, Dut device,
			Dut connectedClientDevice) {
		LOGGER.debug("STARTING METHOD: disconnectCnnClientFromSSID");
		boolean isWifiDiscntd = false;
		String logMsg = null;
		BroadBandResultObject result = new BroadBandResultObject();
		try {
			// String to store the 2.4Ghz wifi network ssid
			String ssid2Ghz = BroadBandConnectedClientUtils.getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv,
					WiFiFrequencyBand.WIFI_BAND_2_GHZ);
			// String to store the 5Ghz wifi network ssid
			String ssid5Ghz = BroadBandConnectedClientUtils.getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv,
					WiFiFrequencyBand.WIFI_BAND_5_GHZ);
			boolean isConnToWifi = ConnectedNattedClientsUtils.verifyConnectToSSID(connectedClientDevice, tapEnv,
					ssid2Ghz, true)
					|| ConnectedNattedClientsUtils.verifyConnectToSSID(connectedClientDevice, tapEnv, ssid5Ghz, true);
			logMsg = isConnToWifi
					? "CONNECTED CLIENT IS CONNECTED TO WIFI NETWORK. GOING TO TRY DISCONNECT IT FROM 2.4GHZ SSID."
					: "Wi-Fi CLIENT IS NOT CONNECTED TO 2.4GHz SSID NETWORK : '" + ssid2Ghz
							+ "' OR 5GHz SSID NETWORK : '" + ssid5Ghz
							+ "', HENCE RETURNING THE DISCONNECTION STATUS AS 'TRUE'";
			LOGGER.info(logMsg);
			if (isConnToWifi) {
				isWifiDiscntd = (ConnectedNattedClientsUtils.disconnectSSID(connectedClientDevice, tapEnv, ssid2Ghz)
						&& ConnectedNattedClientsUtils.verifyConnectToSSID(connectedClientDevice, tapEnv, ssid2Ghz,
								false));
				if (!isWifiDiscntd) {
					LOGGER.info(
							"CONNECTED CLIENT IS STILL CONNECTED TO WIFI NETWORK. GOING TO TRY DISCONNECT IT FROM 5GHZ SSID.");
					isWifiDiscntd = ConnectedNattedClientsUtils.disconnectSSID(connectedClientDevice, tapEnv, ssid5Ghz)
							&& ConnectedNattedClientsUtils.verifyConnectToSSID(connectedClientDevice, tapEnv, ssid5Ghz,
									false);
				}
				logMsg = "CONNECTED CLIENT WITH WIFI MAC ADDRESS : '"
						+ ((Device) connectedClientDevice).getConnectedDeviceInfo().getWifiMacAddress();
				logMsg = isWifiDiscntd ? logMsg + "' DISCONNECTED SUCCESSFULLY FROM WI-FI NETWORK."
						: "UNABLE TO DISCONNECT THE " + logMsg + "' FROM THE WI-FI NETWORK";
				LOGGER.info(logMsg);
			} else {
				isWifiDiscntd = true;
			}
			result.setErrorMessage(logMsg);
			result.setStatus(isWifiDiscntd);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: disconnectCnnClientFromSSID");
		return result;
	}

	/**
	 * Method verify DHCP IP of connected client In case of failure renew Ip and
	 * recheck Ip is in DHCP range
	 * 
	 * @param device
	 * @param tapEnv
	 * @param connectedClientSettop
	 * @return status
	 * 
	 * @refactor yamini.s
	 */
	public static BroadBandResultObject verifyConnectedClientIpv4AddressInDhcpAfterRenew(Dut device,
			AutomaticsTapApi tapEnv, Dut connectedClientSettop) {
		LOGGER.debug("STARTING METHOD: verifyConnectedClientIpv4AddressInDhcpAfterRenew");
		BroadBandResultObject bandResultObject = new BroadBandResultObject();
		String errorMessage = "Connected Client IPv4 address is not in DHCP range";
		String timeoutDefault = null;
		String retryDefault = null;
		boolean result = false;
		String osType = ((Device) connectedClientSettop).getOsType();
		Device ecastSettop = (Device) connectedClientSettop;
		try {
			result = isConnClientIpv4AddrBtwnDhcpRange(tapEnv, device, connectedClientSettop);
			if (result) {
				// Returning true as value is already equal to value to be set
				bandResultObject.setStatus(result);
				return bandResultObject;
			} else {
				if (CommonMethods.isNotNull(osType)
						&& osType.equals(BroadBandConnectedClientTestConstants.OS_WINDOWS)) {
					tapEnv.executeCommandOnOneIPClients(connectedClientSettop,
							BroadBandCommandConstants.CMD_TO_RENEW_IP_IN_WINDOWS);
					LOGGER.info("Waiting for two minutes to reflect IP changes");
					tapEnv.waitTill(BroadBandTestConstants.TWO_MINUTE_IN_MILLIS);
				} else {
					String defaultInterface = getDefaultInterfaceNameOfTheLinuxConnClientDevice(connectedClientSettop,
							tapEnv).trim();
					LOGGER.info("DEFAULT INTERFACE OBTAINED IN THE LINUX CLIENT IS  : " + defaultInterface);
					String connectionType = ecastSettop.getConnectedDeviceInfo().getConnectionType();
					LOGGER.info("CONNECTION TYPE OF THE CONNECTED CLIENT IS : " + connectionType);
					String[] expectedInterfaces = null;
					if (CommonMethods.isNotNull(defaultInterface) && CommonMethods.isNotNull(connectionType)
							&& BroadBandTestConstants.CONNECTION_TYPE_ETHERNET.equalsIgnoreCase(connectionType)) {
						LOGGER.info("GOING TO GET IPV4 ADDRESS FROM ETHERNET INTERFACE");
						expectedInterfaces = AutomaticsTapApi.getSTBPropsValue(
								BroadBandTestConstants.PROP_KEY_TO_GET_EXPECTED_ETHERNET_INTERFACE_IN_LINUX_CLIENT)
								.split(BroadBandTestConstants.SEMI_COLON);
					} else if (CommonMethods.isNotNull(defaultInterface)
							&& BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI
									.equalsIgnoreCase(connectionType)) {
						LOGGER.info("GOING TO GET IPV4 ADDRESS FROM WI-FI INTERFACE");
						expectedInterfaces = AutomaticsTapApi
								.getSTBPropsValue(
										BroadBandTestConstants.PROP_KEY_TO_GET_EXPECTED_WIFI_INTERFACE_IN_LINUX_CLIENT)
								.split(BroadBandTestConstants.SEMI_COLON);
					} else {
						errorMessage = "UNABLE TO OBTAIN THE DEFAULT INTERFACE NAME FROM LINUX CLIENT \n";
						LOGGER.error(errorMessage);
					}
					timeoutDefault = executeCommandInLinuxToEditDhclientDetails(connectedClientSettop, tapEnv,
							BroadBandTestConstants.STRING_TIMEOUT, BroadBandTestConstants.STRING_TIMEOUT_WITH_VALUE_20);
					retryDefault = executeCommandInLinuxToEditDhclientDetails(connectedClientSettop, tapEnv,
							BroadBandTestConstants.STRING_RETRY, BroadBandTestConstants.STRING_RETRY_WITH_VALUE_20);

					if (expectedInterfaces != null && expectedInterfaces.length != 0) {
						for (String interfaceName : expectedInterfaces) {
							if (CommonUtils.patternSearchFromTargetString(defaultInterface, interfaceName)) {
								LOGGER.info("Renew IP in connected client: " + interfaceName);
								tapEnv.executeCommandOnOneIPClients(connectedClientSettop,
										BroadBandCommandConstants.CMD_TO_RENEW_IP_CLIENT_INTERFACE_IN_LINUX.replace(
												BroadBandTestConstants.STRING_INTERFACE_TO_REPLACE, interfaceName));
								LOGGER.info("Waiting for two minutes to reflect IP changes");
								tapEnv.waitTill(BroadBandTestConstants.TWO_MINUTE_IN_MILLIS);
								break;
							}
						}
					}
				}
				errorMessage += "Connected Client IPv4 address is not in DHCP range";
				result = isConnClientIpv4AddrBtwnDhcpRange(tapEnv, device, connectedClientSettop);
			}

		} catch (Exception e) {
			LOGGER.error("Exception in Validating IPv4 address in DHCP of connected client ");
		} finally {
			if ((ecastSettop.isLinux()) || ecastSettop.isRaspbianLinux()) {
				if (CommonMethods.isNotNull(retryDefault)) {
					executeCommandInLinuxToEditDhclientDetails(connectedClientSettop, tapEnv,
							BroadBandTestConstants.STRING_RETRY, retryDefault);
				}
				if (CommonMethods.isNotNull(timeoutDefault)) {
					executeCommandInLinuxToEditDhclientDetails(connectedClientSettop, tapEnv,
							BroadBandTestConstants.STRING_TIMEOUT, timeoutDefault);
				}
			}
		}
		bandResultObject.setStatus(result);
		bandResultObject.setErrorMessage(errorMessage);
		LOGGER.debug("EXITING : verifyConnectedClientIpv4AddressInDhcpAfterRenew");
		return bandResultObject;
	}

	/**
	 * Retrieve Value in dhclient conf file and replace with value required
	 * 
	 * @param connectedClient
	 * @param tapEnv
	 * @param parameter
	 * @param ValueWithParameter
	 * @return replaced Value
	 * 
	 * @refactor yamini.s
	 */
	public static String executeCommandInLinuxToEditDhclientDetails(Dut connectedClient, AutomaticsTapApi tapEnv,
			String parameter, String ValueWithParameter) {
		String response = null;
		String valueRetrieved = null;
		LOGGER.debug("STARTING METHOD : executeCommandInLinuxEditDhclientDetails");
		Device ecastSettop = (Device) connectedClient;
		if (ecastSettop.isLinux() || ecastSettop.isRaspbianLinux()) {
			response = tapEnv.executeCommandOnOneIPClients(connectedClient,
					BroadBandCommandConstants.CMD_DHCLIENT_GREP_VALUE
							.replace(BroadBandTestConstants.STRING_VALUE_TO_REPLACE, parameter));
			if (CommonMethods.patternMatcher(response, parameter)) {
				valueRetrieved = response.split(";")[0].trim();
				tapEnv.executeCommandOnOneIPClients(connectedClient,
						BroadBandCommandConstants.CMD_TO_REPLACE_VALUE_DHCLIENT_CONF
								.replace(BroadBandTestConstants.STRING_VALUERETRIEVED_TO_REPLACE, valueRetrieved)
								.replace(BroadBandTestConstants.STRING_VALUEPARAMETER_TO_REPLACE, ValueWithParameter));
			}
		}
		LOGGER.debug("ENDING METHOD : executeCommandInLinuxEditDhclientDetails");
		return valueRetrieved;

	}

	/**
	 * Helper method to verify if the IPv4 address assigned to connected client is
	 * between DHCP configured range. Code duplication of method
	 * "verifyIpv4AddressOFConnectedClientIsBetweenDhcpRange", Reason being ipv4
	 * address was not obtained for linux OS client.
	 * 
	 * @param tapEnv                instance of {@link AutomaticsTapApi}
	 * @param device                instance of {@link Dut}
	 * @param connectedClientSettop Connected client device instance
	 * 
	 * @return true if the IPv4 address of the connected client is between
	 *         configured DHCP range
	 * 
	 * @author BALAJI V, INFOSYS
	 * @refactor yamini.s
	 */

	public static boolean isConnClientIpv4AddrBtwnDhcpRange(AutomaticsTapApi tapEnv, Dut device,
			Dut connectedClientSettop) {
		LOGGER.debug("ENTERING METHOD isConnClientIpv4AddrBtwnDhcpRange");
		boolean result = false;
		try {
			String dhcpMinRange = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_TO_RETRIEVE_DHCP_STARTING_IP_ADDRESS);
			LOGGER.info("DHCP MINIMUM IP RANGE CONFIGURED FOR GATEWAY : " + dhcpMinRange);
			String dhcpMaxRange = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_TO_RETRIEVE_DHCP_ENDING_IP_ADDRESS);
			LOGGER.info("DHCP MAXIMUM IP RANGE CONFIGURED FOR GATEWAY : " + dhcpMaxRange);
			String ipv4RetrievedFromClient = BroadBandConnectedClientUtils.getIpv4AddressFromConnClient(tapEnv, device,
					connectedClientSettop);
			LOGGER.info("IP ADDRESS ASSIGNED TO THE CONNECTED CLIENT : " + ipv4RetrievedFromClient);
			boolean isDhcpRangeAndIpv4Retrieved = CommonMethods.isNotNull(dhcpMinRange)
					&& CommonMethods.isNotNull(dhcpMaxRange) && CommonMethods.isNotNull(ipv4RetrievedFromClient);
			if (isDhcpRangeAndIpv4Retrieved && CommonMethods.isIpv4Address(dhcpMinRange)
					&& CommonMethods.isIpv4Address(dhcpMaxRange)
					&& CommonMethods.isIpv4Address(ipv4RetrievedFromClient)) {
				if (CommonMethods
						.patternFinder(ipv4RetrievedFromClient,
								BroadBandTestConstants.PATTERN_TO_RETRIEVE_FIRST_3_DIGITS_OF_IPv4_ADDRESS)
						.equalsIgnoreCase(CommonMethods.patternFinder(dhcpMinRange,
								BroadBandTestConstants.PATTERN_TO_RETRIEVE_FIRST_3_DIGITS_OF_IPv4_ADDRESS))) {
					LOGGER.info("GOING TO VERIFY IF THE OBTAINED IP ADDRESS IS WITHIN DHCP RANGE.");
					int minRange = Integer.parseInt(CommonMethods.patternFinder(dhcpMinRange,
							BroadBandTestConstants.PATTERN_TO_RETRIEVE_LAST_DIGIT_OF_IPv4_ADDRESS));
					int maxRange = Integer.parseInt(CommonMethods.patternFinder(dhcpMaxRange,
							BroadBandTestConstants.PATTERN_TO_RETRIEVE_LAST_DIGIT_OF_IPv4_ADDRESS));
					int actualvalue = Integer.parseInt(CommonMethods.patternFinder(ipv4RetrievedFromClient,
							BroadBandTestConstants.PATTERN_TO_RETRIEVE_LAST_DIGIT_OF_IPv4_ADDRESS));
					result = actualvalue >= minRange && actualvalue <= maxRange;
				}
			}
		} catch (Exception e) {
			LOGGER.error(
					"EXCEPTION OCCURRED WHILE VERIFYING IPv4 ASSIGNNED TO THE CLIENT CONNECTED VIA ETHERNET IS WITHIN DHCP RANGE. ERROR : "
							+ e.getMessage());
		}

		LOGGER.info("IS IP ADDRESS ASSIGNED TO THE CONNECTED CLIENT BETWEEN DHCP RANGE : " + result);
		LOGGER.debug("ENDING METHOD isConnClientIpv4AddrBtwnDhcpRange");
		return result;

	}

	/**
	 * Method to verify the default gateway ip address in connected client
	 * 
	 * @param deviceConnected Connected client Dut instance
	 * @param tapEnv          AutomaticsTapApi instance
	 * @return status True-verified default Gateway IP of the connected client
	 *         device.Else False
	 */
	public static boolean verifyDefaultGatewayAddressInConnectedClient(Dut device, Dut deviceConnected,
			AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: verifyDefaultGatewayAddressInConnectedClient");
		boolean status = false;
		String gatewayIPExpected = (DeviceModeHandler.isDSLDevice(device)) ? BroadBandTestConstants.LAN_LOCAL_IP
				: tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_LAN_IP_ADDRESS);
		if (CommonMethods.isNotNull(gatewayIPExpected) && CommonMethods.isIpv4Address(gatewayIPExpected)) {
			status = CommonUtils.patternSearchFromTargetString(
					getGatewayIpv4AddressFromConctdClient(device, deviceConnected, tapEnv), gatewayIPExpected);
		}
		LOGGER.debug("ENDING METHOD: verifyDefaultGatewayAddressInConnectedClient");
		return status;
	}

	/**
	 * Method to retrieve the default gateway ip address in connected client
	 * 
	 * @param deviceConnected Connected client device instance
	 * @param tapEnv          AutomaticsTapApi instance
	 * @return status True-verified default Gateway IP of the connected client
	 *         device.Else False
	 */
	public static String getGatewayIpv4AddressFromConctdClient(Dut device, Dut deviceConnected,
			AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: getGatewayIpv4AddressFronConctdClient");
		String gatewayIp = null;
		String command = null;
		if ((((Device) deviceConnected).isLinux()) || (((Device) deviceConnected).isRaspbianLinux())) {
			command = BroadBandConnectedClientTestConstants.CMD_LINUX_DETAULT_GATEWAY_IP;
		} else if ((((Device) deviceConnected).isWindows())) {
			command = BroadBandConnectedClientTestConstants.CMD_WIN_DETAULT_GATEWAY_IP;
		}
		gatewayIp = tapEnv.executeCommandOnOneIPClients(deviceConnected, command);
		LOGGER.debug("ENDING METHOD: getGatewayIpv4AddressFronConctdClient");
		return gatewayIp;
	}

	/**
	 * Method to get the client device from connected client and connect with given
	 * wifi network
	 * 
	 * @param device                 instance of {@link Dut}
	 * @param tapEnv                 instance of {@link AutomaticsTapApi}
	 * @param deviceAlreadyConnected device already connected
	 * @param wifiBand               Frequency band device needs to be connected
	 * @return newConnectedDevice
	 * 
	 * @refactor yamini.s
	 * 
	 */
	public static Dut getOtherWiFiCapableClientDeviceAndConnect(Dut device, AutomaticsTapApi tapEnv,
			Dut deviceAlreadyConnected, String wifiBand) {
		LOGGER.debug("STARTING METHOD : getOtherWiFiCapableClientDeviceAndConnect () ");
		Dut newConnectedDevice = null;
		String wifiCapability = null;
		String connectionType = null;
		String ssidName = null;
		String passPhraseName = null;
		boolean openSecurityMode = false;
		String securityModeofNetwork = null;
		String wifiMacAddress = null;
		String osType = null;
		List<Dut> connectedClientSettops = ((Device) device).getConnectedDeviceList();
		try {
			if (connectedClientSettops != null && connectedClientSettops.size() > 0) {
				LOGGER.info("Already Connected Clients Wifi Mac Address :"
						+ ((Device) deviceAlreadyConnected).getConnectedDeviceInfo().getWifiMacAddress());
				LOGGER.info("Obtained number of client devices is : " + connectedClientSettops.size());
				ssidName = wifiBand.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ)
						? getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv, WiFiFrequencyBand.WIFI_BAND_2_GHZ)
						: getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv, WiFiFrequencyBand.WIFI_BAND_5_GHZ);
				passPhraseName = wifiBand.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ)
						? getSsidPassphraseFromGatewayUsingWebPaOrDmcli(device, tapEnv,
								WiFiFrequencyBand.WIFI_BAND_2_GHZ)
						: getSsidPassphraseFromGatewayUsingWebPaOrDmcli(device, tapEnv,
								WiFiFrequencyBand.WIFI_BAND_5_GHZ);
				securityModeofNetwork = wifiBand.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ)
						? tapEnv.executeWebPaCommand(device,
								BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_PRIVATE_SECURITY_MODEENABLED)
						: tapEnv.executeWebPaCommand(device,
								BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_PRIVATE_SECURITY_MODEENABLED);
				LOGGER.info("Security mode obtained for Given " + wifiBand
						+ " Wi-Fi network from the Router Device is : " + securityModeofNetwork);
				openSecurityMode = CommonMethods.isNotNull(securityModeofNetwork)
						&& securityModeofNetwork.equalsIgnoreCase(BroadBandTestConstants.SECURITY_MODE_NONE);
				LOGGER.info("OPEN SECURITY MODE :" + openSecurityMode);
				for (Dut clientSettop : connectedClientSettops) {
					wifiMacAddress = ((Device) clientSettop).getConnectedDeviceInfo().getWifiMacAddress();
					osType = ((Device) clientSettop).getConnectedDeviceInfo().getOsType();
					connectionType = ((Device) clientSettop).getConnectedDeviceInfo().getConnectionType();
					wifiCapability = ((Device) clientSettop).getConnectedDeviceInfo().getWifiCapability();
					if (CommonMethods.isNotNull(wifiMacAddress) && !wifiMacAddress.trim().equalsIgnoreCase(
							((Device) deviceAlreadyConnected).getConnectedDeviceInfo().getWifiMacAddress())) {
						LOGGER.info("Connected Clients Wifi Mac Address :" + wifiMacAddress);
						LOGGER.info("Connected Clients Wifi OS Type :" + osType);
						LOGGER.info("Client device connection type :" + connectionType);
						LOGGER.info("Client device Wi-Fi capability :" + wifiCapability);
						if (CommonMethods.isNotNull(connectionType) && CommonMethods.isNotNull(wifiCapability)) {
							if (connectionType.trim().equalsIgnoreCase(
									BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI)
									&& (wifiCapability.trim().equalsIgnoreCase(
											BroadBandConnectedClientTestConstants.STRING_WIFI_CAPABILITY_2_4GHZ_ONLY)
											|| wifiCapability.trim().equalsIgnoreCase(
													BroadBandConnectedClientTestConstants.STRING_WIFI_CAPABILITY_5GHZ_ONLY)
											|| wifiCapability.trim().equalsIgnoreCase(
													BroadBandConnectedClientTestConstants.STRING_WIFI_CAPABILITY_DUAL_BAND))) {
								if (openSecurityMode) {
									LOGGER.info("Going to connect the client with " + wifiBand
											+ " Wi-Fi network : Open security mode.");
									newConnectedDevice = ConnectedNattedClientsUtils.connectToSSID(clientSettop, tapEnv,
											ssidName, passPhraseName,
											BroadBandConnectedClientTestConstants.SECURITY_MODE_OPEN.toLowerCase())
													? clientSettop
													: null;
								} else {
									LOGGER.info("Going to connect the client with " + wifiBand
											+ " Wi-Fi network : Recommanded security mode.");
									newConnectedDevice = ConnectedNattedClientsUtils.connectToSSID(clientSettop, tapEnv,
											ssidName, passPhraseName) ? clientSettop : null;
								}
								if (newConnectedDevice != null) {
									LOGGER.info("Successfully established Wi-Fi connection with " + wifiBand
											+ " Wi-Fi network!");
									break;
								}
							}
						} else {
							LOGGER.error(
									"Obtained null response for client device 'connection type' or 'wifi capability'. Check MDS configuration for the connected client with Wi-Fi Mac Address : "
											+ ((Device) clientSettop).getConnectedDeviceInfo().getWifiCapability());
						}
					}
				}
				if (newConnectedDevice == null) {
					throw new TestException("Unable to establish Wi-Fi connection with " + wifiBand
							+ " GHz Wi-Fi network from the obtained connected clients!");
				}
			} else {
				throw new TestException("No connected client is currently associated with the gateway device!");
			}
		} catch (Exception exception) {
			throw new TestException("Unable to connect the client with " + wifiBand
					+ " Wi-Fi Network due to following Reason : " + exception.getMessage());
		}
		LOGGER.debug("ENDING METHOD : getOtherWiFiCapableClientDeviceAndConnect () ");
		return newConnectedDevice;
	}

	/**
	 * Helper method to connect Connected client to public SSID
	 * 
	 * @param device        Dut instance
	 * @param tapEnv        AutomaticsTapApi instance
	 * @param securityMode  security mode of the public wifi
	 * @param frequencyBand frequency band of the public wifi to be connected
	 * @return Dut instance of the connected client connected to public wifi
	 * @author Praveenkumar Paneerselvam
	 * @refactor Athira
	 */
	public static Dut getWiFiCapableClientDeviceAndConnectToPublicSsid(Dut device, AutomaticsTapApi tapEnv,
			String securityMode, WiFiFrequencyBand frequencyBand) {
		LOGGER.debug("STARTING METHOD : getWiFiCapableClientDeviceAndConnectToPublicSsid () ");
		Dut isDeviceConnected = null;
		isDeviceConnected = getWiFiCapableClientDeviceAndConnectToGivenSsidType(device, tapEnv, securityMode,
				frequencyBand, BroadBandTestConstants.WIFI_TYPE_PUBLIC);
		LOGGER.debug("ENDING METHOD : getWiFiCapableClientDeviceAndConnectToPublicSsid () ");
		return isDeviceConnected;
	}

	/**
	 * Helper method to connect Connected client to public SSID
	 * 
	 * @param device        Dut instance
	 * @param tapEnv        AutomaticsTapApi instance
	 * @param securityMode  security mode of the public wifi
	 * @param frequencyBand frequency band of the public wifi to be connected
	 * @param ssidType      PUBLIC_WIFI to connect to public wifi network.
	 * @return Dut instance of the connected client connected to public wifi
	 * @author Praveenkumar Paneerselvam
	 * @refactor Athira
	 */
	public static Dut getWiFiCapableClientDeviceAndConnectToGivenSsidType(Dut device, AutomaticsTapApi tapEnv,
			String securityMode, WiFiFrequencyBand frequencyBand, String ssidType) {
		LOGGER.debug("STARTING METHOD : getWiFiCapableClientDeviceAndConnectToGivenSsidType () ");
		// device instance to store 2.4GHz client device
		Dut isDeviceConnected = null;
		// variable to store wifi capability
		String wifiCapability = null;
		// variable to store connection type of the connected client
		String connectionType = null;
		// variable to store the SSID of 2.4GHz Wi-Fi Network
		String ssidName = null;
		// Get list of connected client devices
		List<Dut> connectedClientSettops = ((Device) device).getConnectedDeviceList();

		String password = BroadBandTestConstants.EMPTY_STRING;
		String webpaParameter = null;
		boolean isHiddenSsid = false;
		if (CommonMethods.isNotNull(ssidType)) {
			if (BroadBandTestConstants.WIFI_TYPE_PUBLIC.equalsIgnoreCase(ssidType)) {
				webpaParameter = WiFiFrequencyBand.WIFI_BAND_2_GHZ.equals(frequencyBand)
						? BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_PUBLIC_SECURITY_MODEENABLED
						: BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_PUBLIC_SECURITY_MODEENABLED;
				// SSID of 2.4GHz Wi-Fi Network
				ssidName = getPublicSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv, frequencyBand);
			}
		}
		try {
			if (connectedClientSettops != null && connectedClientSettops.size() > 0
					&& CommonMethods.isNotNull(webpaParameter) && CommonMethods.isNotNull(ssidName)) {
				// Security Mode of 2.4GHz Wi-Fi Network
				String securityModeOf2GHzNetwork = tapEnv.executeWebPaCommand(device, webpaParameter);
				LOGGER.info("Security mode obtained for " + frequencyBand
						+ " Wi-Fi network from the Router Device is : " + securityModeOf2GHzNetwork);
				boolean openSecurityMode = CommonMethods.isNotNull(securityModeOf2GHzNetwork)
						&& securityModeOf2GHzNetwork.equalsIgnoreCase(BroadBandTestConstants.SECURITY_MODE_NONE);
				LOGGER.info("Obtained number of client devices is : " + connectedClientSettops.size());
				for (Dut clientClient : connectedClientSettops) {
					connectionType = ((Device) clientClient).getConnectedDeviceInfo().getConnectionType();
					LOGGER.info("Client device connection type : " + connectionType);
					wifiCapability = ((Device) clientClient).getConnectedDeviceInfo().getWifiCapability();
					String wifiCapable = WiFiFrequencyBand.WIFI_BAND_2_GHZ.equals(frequencyBand)
							? BroadBandConnectedClientTestConstants.STRING_WIFI_CAPABILITY_2_4GHZ_ONLY
							: BroadBandConnectedClientTestConstants.STRING_WIFI_CAPABILITY_5GHZ_ONLY;
					LOGGER.info("Client device Wi-Fi capability : " + wifiCapability);
					if (CommonMethods.isNotNull(connectionType) && CommonMethods.isNotNull(wifiCapability)) {
						if (connectionType.equalsIgnoreCase(
								BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI)
								&& (wifiCapability.equalsIgnoreCase(wifiCapable) || wifiCapability.equalsIgnoreCase(
										BroadBandConnectedClientTestConstants.STRING_WIFI_CAPABILITY_DUAL_BAND))) {
							if (openSecurityMode) {
								LOGGER.info("Going to connect the client with " + frequencyBand
										+ " Wi-Fi network : Open security mode.");
								isDeviceConnected = ConnectedNattedClientsUtils.connectToSSID(clientClient, tapEnv,
										ssidName, password, securityMode, isHiddenSsid) ? clientClient : null;
							} else {
								LOGGER.info("Going to connect the client with " + frequencyBand
										+ " Wi-Fi network : Recommended security mode.");
								isDeviceConnected = ConnectedNattedClientsUtils.connectToSSID(clientClient, tapEnv,
										ssidName, password, isHiddenSsid) ? clientClient : null;
							}
							if (isDeviceConnected != null) {
								LOGGER.info("Successfully established Wi-Fi connection with " + frequencyBand
										+ " Wi-Fi network!");
								break;
							}
						}
					} else {
						LOGGER.error(
								"Obtained null response for client device 'connection type' or 'wifi capability'. Check MDS configuration for the connected client with Wi-Fi Mac Address : "
										+ ((Device) clientClient).getConnectedDeviceInfo().getWifiCapability());
					}

				}
				if (isDeviceConnected == null) {
					throw new TestException("Unable to establish Wi-Fi connection with " + frequencyBand
							+ " Wi-Fi network from the obtained connected clients!");
				}
			} else {
				throw new TestException(
						"No connected client is currently associated with the gateway device! or given SSID type does not support.");
			}

		} catch (Exception exception) {
			throw new TestException("Unable to connect the client with " + frequencyBand
					+ " Wi-Fi Network due to following Reason : " + exception.getMessage());
		}
		LOGGER.debug("ENDING METHOD : getWiFiCapableClientDeviceAndConnectToGivenSsidType () ");
		return isDeviceConnected;
	}

	/**
	 * 
	 * @param tapEnv                instance of {@link AutomaticsTapApi}
	 * @param connectedClientSettop Connected client device instance
	 * @param url                   Internet access of URL which needs to be checked
	 * @return BroadBandResultObject
	 * @refactor Govardhan
	 */
	public static BroadBandResultObject verifyInternetAccessUsingCurl(AutomaticsTapApi tapEnv,
			Dut connectedClientDevice, String url) {

		boolean isNotAccessible = false;
		String errorMessage = null;
		BroadBandResultObject result = new BroadBandResultObject();
		String curlCommand = (CommonUtils.patternSearchFromTargetString(url, "ftp"))
				? BroadBandCommonUtils
						.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_CURL_WITH_20_SECS_TIMEOUT, url)
				: BroadBandCommonUtils
						.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_CURL_WITH_TIMEOUT_AND_HEADER, url);

		String response = tapEnv.executeCommandOnOneIPClients(connectedClientDevice, curlCommand);
		errorMessage = "Null response obtained on checking Internet access in the connected client.";
		if (CommonMethods.isNotNull(response)) {
			isNotAccessible = CommonUtils.patternSearchFromTargetString(response,
					BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_CONNECTION_SSL_HANDSHAKE_FAILURE_MESSAGE)
					|| CommonUtils.patternSearchFromTargetString(response,
							BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_FAILURE_MESSAGE)
					|| CommonUtils.patternSearchFromTargetString(response,
							BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_CONNECTION_TIMEOUT_MESSAGE)
					|| CommonUtils.patternSearchFromTargetString(response,
							BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_OPERATION_TIMEOUT_MESSAGE)
					|| CommonUtils.patternSearchFromTargetString(response,
							BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_RESOLVING_TIMEOUT_MESSAGE)
					|| CommonUtils.patternSearchFromTargetString(response,
							BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_CONNECTION_COULD_NOT_RESOLVE_HOST_MESSAGE);
			errorMessage = (!isNotAccessible && CommonUtils.patternSearchFromTargetString(response,
					BroadBandTestConstants.CURL_NOT_INSTALLED_ERROR_MESSAGE))
							? "Curl is not installed in the "
									+ ((Device) connectedClientDevice).getConnectedDeviceInfo().getConnectionType()
									+ " Connected Client"
							: (!isNotAccessible && (CommonUtils.patternSearchFromTargetString(response,
									BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_HEADER_SUCCESS_MESSAGE)
									|| CommonUtils.patternSearchFromTargetString(response,
											BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_HEADER_SUCCESS_HTTP_1_1)
									|| CommonUtils.patternSearchFromTargetString(response,
											BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_SUCCESS_MESSAGE)
									|| CommonUtils.patternSearchFromTargetString(response,
											BroadBandConnectedClientTestConstants.RESPONSE_STATUS_OK)))
													? "Website/IP Address is accessible in the Connected Client"
													: "Unable to check the internet access in the connected client using curl";
		}

		result.setErrorMessage(errorMessage);
		result.setStatus(isNotAccessible);

		return result;
	}

	/**
	 * 
	 * This method is to verify IIS status on the WiFi Client Device.
	 * 
	 * @param wifiClientDevice {@link Dut}
	 * @param tapEnv           {@link AutomaticsTapApi}
	 * @param isEnabled        Boolean representing the IIS start/stop state.
	 * @return returns status of operation
	 * @Refactor Alan_Bivera
	 */
	public static boolean verifyIISStatus(Dut wifiClientDevice, AutomaticsTapApi tapEnv, boolean isEnabled) {
		LOGGER.debug("STARTING METHOD : verifyIISStatus()");
		boolean status = false;
		String response = null;
		try {
			response = tapEnv.executeCommandOnOneIPClients(wifiClientDevice,
					BroadBandConnectedClientTestConstants.CMD_TO_CHECK_ISS_STATUS).trim();
			status = CommonMethods.isNotNull(response) && CommonUtils.patternSearchFromTargetString(response,
					isEnabled ? BroadBandConnectedClientTestConstants.PATTERN_TO_CHECK_IIS_RUNNING_STATUS
							: BroadBandConnectedClientTestConstants.PATTERN_TO_CHECK_IIS_STOP_STATUS);
			if (isEnabled && !status) {
				tapEnv.executeCommandOnOneIPClients(wifiClientDevice,
						BroadBandConnectedClientTestConstants.CMD_ISS_START);
				response = tapEnv.executeCommandOnOneIPClients(wifiClientDevice,
						BroadBandConnectedClientTestConstants.CMD_TO_CHECK_ISS_STATUS).trim();
				status = CommonMethods.isNotNull(response) && CommonUtils.patternSearchFromTargetString(response,
						BroadBandConnectedClientTestConstants.PATTERN_TO_CHECK_IIS_RUNNING_STATUS);
			} else if (!isEnabled && !status) {
				tapEnv.executeCommandOnOneIPClients(wifiClientDevice,
						BroadBandConnectedClientTestConstants.CMD_ISS_STOP);
				response = tapEnv.executeCommandOnOneIPClients(wifiClientDevice,
						BroadBandConnectedClientTestConstants.CMD_TO_CHECK_ISS_STATUS).trim();
				status = CommonMethods.isNotNull(response) && CommonUtils.patternSearchFromTargetString(response,
						BroadBandConnectedClientTestConstants.PATTERN_TO_CHECK_IIS_STOP_STATUS);
			}
		} catch (Exception exception) {
			throw new TestException("unable validate the iis status : " + exception.getMessage());
		}
		LOGGER.debug("ENDING METHOD : verifyIISStatus()");
		return status;
	}

	/**
	 * Method to get RaspberryPi device from connected clients
	 * 
	 * @param device instance of {@link Dut}
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @return raspberryPi instance of {@link Dut}
	 * 
	 * @author ArunKumar Jayachandran
	 * @refactor Said Hisham
	 */
	public static Dut getRaspberryPiFromConnectedClients(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: getRaspberryPiFromConnectedClients()");
		List<Dut> connectedClientSettops = ((Device) device).getConnectedDeviceList();
		Dut raspberryPi = null;
		String command = null;
		String response = null;
		try {
			if (null != connectedClientSettops && connectedClientSettops.size() > 0) {
				for (Dut clientSettop : connectedClientSettops) {
					// Verify the connected client is Raspberry pi device
					command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.CAT_COMMAND,
							BroadBandCommandConstants.FILE_RPI_DEVICE_MODEL);
					response = tapEnv.executeCommandOnOneIPClients(clientSettop, command);
					if (CommonUtils.isGivenStringAvailableInCommandOutput(response,
							BroadBandTestConstants.TEXT_RASPBERRY)) {
						raspberryPi = clientSettop;
						LOGGER.info("Raspberry pi available in the connected client setup");
						break;
					}
				}
				if (raspberryPi == null) {
					throw new TestException("Raspberry pi device not available in connected client list");
				}
			}
		} catch (Exception exception) {
			throw new TestException("Not able to get the connected clients due to => " + exception.getMessage());
		}
		LOGGER.debug("ENDING METHOD: getRaspberryPiFromConnectedClients()");
		return raspberryPi;
	}

	/**
	 * Method to kill the process in connected client device
	 * 
	 * @param client      instance of {@link Dut}
	 * @param tapEnv      instance of {@link AutomaticsTapApi}
	 * @param processName processName
	 * 
	 * @author ArunKumar Jayachandran
	 * @refactor Said Hisham
	 */
	public static boolean killProcessInConnectedClientSettop(Dut client, AutomaticsTapApi tapEnv, String processName) {
		LOGGER.debug("STARTING METHOD: killProcessInConnectedClientSettop()");
		String command = null;
		String response = null;
		String pid = null;
		boolean status = false;
		command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.CMD_PID_OF,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, processName);
		// execute the command pidof <process name>
		response = tapEnv.executeCommandOnOneIPClients(client, command);
		if (CommonMethods.isNotNull(response)) {
			// Get the process id using regex pattern (\\d+)
			pid = CommonMethods.patternFinder(response, BroadBandTestConstants.PATTERN_MATCHER_GET_NUMBER);
			// Kill the process using kill -9 <pid>
			if (CommonMethods.isNotNull(pid)) {
				LOGGER.info("Process Id for " + processName + " is: " + pid);
				tapEnv.executeCommandOnOneIPClients(client, BroadBandTestConstants.KILL_9 + pid);
			} else {
				LOGGER.error("Getting empty process id from pidof " + processName + " command");
			}
		} else {
			LOGGER.error("There is no process running for " + processName + " in RPI device & response for \"pidof "
					+ processName + "\" command output:" + response);
		}
		// verify the process is killed properly
		if (CommonMethods.isNotNull(pid)) {
			response = tapEnv.executeCommandOnOneIPClients(client, command);
			status = !CommonUtils.isGivenStringAvailableInCommandOutput(response, pid);
			LOGGER.info((status ? "Successfully killed the process" : "Failed to kill the process"));
		}
		LOGGER.debug("ENDING METHOD: killProcessInConnectedClientSettop()");
		return status;
	}

	/**
	 * Method to apply dns overRide at client mac Level through dnsmasq_servers.conf
	 * file
	 * 
	 * @param tapEnv                instance of {@link AutomaticsTapApi}
	 * @param device                instance of {@link Dut}
	 * 
	 * @param clientMacAddress
	 * 
	 * @param XdnsOverRideLevelList
	 * 
	 * @return status The status of applying overRide at client mac Level through
	 *         dnsmasq_servers.conf file
	 * 
	 * @author Susheela C
	 * @refactor yamini.s
	 */
	public static boolean applyXdnsOverRideAddingDNSMapTbl(AutomaticsTapApi tapEnv, Dut device, String clientMacAddress,
			String xdnsOverRideLevelIpv4, String xdnsOverRideLevelIpv6) {
		// Status of dns overRiding at client mac Level
		boolean status = false;
		LOGGER.debug("STARTING METHOD : applyXdnsOverRideAddingDNSMapTbl()");
		if (CommonUtils.isFileExists(device, tapEnv, BroadBandTestConstants.DNSMASQ_CONFIGURATION_FILE)) {
			LOGGER.info(
					"dnsmasq_servers.conf file exists. Going add DNS mapping table using webpa param 'Device.X_RDKCENTRAL-COM_XDNS.DNSMappingTable.'.");
			if (CommonMethods.isNotNull(clientMacAddress)) {
				List<String> macAddressList = new ArrayList<String>();
				HashMap<String, List<String>> rowParamsMap = new HashMap<String, List<String>>();
				Map<String, HashMap<String, List<String>>> parentalControlTableMap = new HashMap<String, HashMap<String, List<String>>>();
				macAddressList.add(clientMacAddress);
				rowParamsMap.put(BroadBandTestConstants.STRING_DNS_MAC_ADDRESS, macAddressList);
				List<String> dnsIPv6List = new ArrayList<String>();
				dnsIPv6List.add(xdnsOverRideLevelIpv6);
				rowParamsMap.put(BroadBandTestConstants.STRING_DNS_IPV6, dnsIPv6List);
				List<String> dnsIPv4List = new ArrayList<String>();
				dnsIPv4List.add(xdnsOverRideLevelIpv4);
				rowParamsMap.put(BroadBandTestConstants.STRING_DNS_IPV4, dnsIPv4List);
				List<String> tagList = new ArrayList<String>();
				tagList.add(BroadBandTestConstants.CONNECTED_DEVICE);
				rowParamsMap.put(BroadBandTestConstants.STRING_DNS_TAG, tagList);
				parentalControlTableMap.put("0", rowParamsMap);
				// Instance to store webPaServerResponse
				WebPaServerResponse webPaServerResponse = tapEnv.putWebpaTableParamUsingRestApi(device,
						BroadBandWebPaConstants.WEBPA_PARAM_DNS_MAPPING_TABLE, parentalControlTableMap);
				if (webPaServerResponse != null) {
					status = webPaServerResponse.getMessage().equalsIgnoreCase(BroadBandTestConstants.SUCCESS_TXT);
				}
			} else {
				LOGGER.error("'null' value is obtained for the Wi-Fi MacAddress.");
			}
		} else {
			LOGGER.error("dnsmasq_servers.conf file doesn't exist.");
		}
		LOGGER.debug("ENDING METHOD : applyXdnsOverRideAddingDNSMapTbl()");
		return status;
	}

	/**
	 * Method to verify file presence in connected client
	 * 
	 * @param device           instance of {@link Dut}
	 * @param tapEnv           instance of {@link AutomaticsTapApi}
	 * @param completeFilePath complete file path
	 * @param isExist          True- File should be present,False- File should not
	 *                         be present
	 * @return true if file present else false
	 * @refactor Said Hisham
	 */
	public static boolean isFileExistsInConnectedClient(Dut device, AutomaticsTapApi tapEnv, String completeFilePath,
			boolean isExist) {
		LOGGER.debug("STARTING METHOD: isFileExistsInConnectedClient()");
		boolean status = false;
		String response = null;
		try {
			for (int iteration = BroadBandTestConstants.CONSTANT_0; iteration < BroadBandTestConstants.CONSTANT_3; iteration++) {
				response = tapEnv
						.executeCommandOnOneIPClients(device,
								BroadBandCommonUtils.concatStringUsingStringBuffer(
										BroadBandCommandConstants.CMD_FILE_EXISTS_HEAD, completeFilePath,
										BroadBandCommandConstants.CMD_FILE_EXISTS_TAIL))
						.replace(BroadBandTestConstants.CHAR_NEW_LINE, BroadBandTestConstants.EMPTY_STRING).trim();
				status = CommonMethods.isNotNull(response)
						&& response.equals(isExist ? BroadBandTestConstants.TRUE : BroadBandTestConstants.FALSE);
				LOGGER.info("response :" + response + " status: " + status);
				LOGGER.info("File " + completeFilePath + (status ? " available" : " not available"));
				if (status) {
					break;
				} else {
					BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured in isFileExistsInConnectedClient()." + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: isFileExistsInConnectedClient()");
		return status;
	}

	/**
	 * Method get download the file using AutoVault service and move the file to
	 * client
	 * 
	 * @param device           instance of {@link Dut}
	 * @param tapEnv           instance of {@link AutomaticsTapApi}
	 * @param downloadFilePath Auto vault download file path
	 * @param targetLocation   Target location
	 * @param targetFileName   Target fileName
	 * @return True- File downloaded successfully from Auto vault,Else-False
	 * @refactor Said Hisham
	 */
	public static boolean downloadFileUsingAutoVaultToConnectedClient(Dut device, AutomaticsTapApi tapEnv,
			String downloadFilePath, String targetLocation, String targetFileName) {
		LOGGER.debug("STARTING method : downloadFileUsingAutoVaultToConnectedClient");
		boolean result = false;
		String base64Credentials = null;
		String completeCommand = null;
		String downloadURL = null;
		String cmdResponse = null;
		String exportPaths = "export PATH=$PATH:/usr/bin:/bin:/usr/local/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/sbin:/usr/sbin:.;export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/bin:/usr/local/lib:/usr/local/bin:/usr/local/lib:/usr/local/lib/gstreamer-0.10;";
		long startTime = System.currentTimeMillis();
		try {
			if (CommonMethods.isNotNull(downloadFilePath) && CommonMethods.isNotNull(targetLocation)) {
				base64Credentials = BroadbandPropertyFileHandler.getAutoVaultBase64Credentials();
				downloadURL = BroadbandPropertyFileHandler.getAutoVaultDownloadURL();
				targetFileName = downloadFilePath.split(BroadBandTestConstants.FORWARD_SLASH_CHARACTER)[downloadFilePath
						.split(BroadBandTestConstants.FORWARD_SLASH_CHARACTER).length
						- BroadBandTestConstants.CONSTANT_1];
				completeCommand = exportPaths + "curl -L " + downloadURL + "?fileName=" + downloadFilePath + " -o '"
						+ targetFileName + "' -w \"status_code:%{http_code}\n\" --header \"authorization: Basic "
						+ base64Credentials + "\"";
				startTime = System.currentTimeMillis();
				do {
					cmdResponse = tapEnv.executeCommandOnOneIPClients(device, completeCommand);
					result = CommonMethods.patternMatcher(cmdResponse, BroadBandTestConstants.STATUS_CODE_SUCCESS);
				} while (!result
						&& ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.THREE_MINUTE_IN_MILLIS)
						&& BroadBandCommonUtils.hasWaitForDuration(tapEnv,
								BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
				if (result) {
					tapEnv.executeCommandOnOneIPClients(device,
							BroadBandCommandConstants.CMD_MAKE_FILE_EXECUTABLE + targetLocation + targetFileName);
					LOGGER.info("chmod a+x mode changed for " + targetLocation + targetFileName);
				}
			} else {
				LOGGER.error("The complete file path or the target location is invalid");
			}
		} catch (Exception e) {
			LOGGER.error("Failed to copy the file using AutoVault service : " + e.getMessage());
		}
		LOGGER.debug("ENDING method : downloadFileUsingAutoVaultToConnectedClient");
		return result;
	}

	/**
	 * Method to verify the connected client details in LMLog.txt.0 file
	 * 
	 * @param device         instance of {@link Dut}
	 * @param tapEnv         instance of {@link AutomaticsTapApi}
	 * @param connectionType Client Connection Type
	 * @param hostMacAddress Client Mac Address
	 * @param pollDuration   Duration to verify the logs
	 * @param deviceDateTime Device Date and Time
	 * @return true if expected log message available
	 * 
	 * @refactor Athira
	 * 
	 */

	public static boolean verifyConnectedClientDetailsInLMlog(Dut device, AutomaticsTapApi tapEnv,
			String connectionType, String hostMacAddress, long pollDuration, String deviceDateTime) {
		LOGGER.debug("STARTING METHOD : verifyConnectedClientDetailsInLMlog () ");
		String connectionTypeLog = null;
		boolean logStatus = false;
		String searchString = null;
		String alternateString = null;
		boolean isCnnTypeValid = true;
		if (connectionType
				.equalsIgnoreCase(BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI)) {
			LOGGER.info("OBTAINED CONNECTION TYPE IS : "
					+ BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI);
			connectionTypeLog = BroadBandConnectedClientTestConstants.LOG_PARAM_CLIENT_TYPE_WIFI;
			searchString = BroadBandTraceConstants.LOG_MESSAGE_TO_VERIFY_CONNECTED_CLIENT_ONLINE
					.replace(BroadBandTestConstants.STRING_REPLACE, hostMacAddress);
			alternateString = BroadBandTraceConstants.LOG_MESSAGE_TO_VERIFY_CONNECTED_CLIENT_CONNECTED
					.replace(BroadBandTestConstants.STRING_REPLACE, hostMacAddress);
		} else if (connectionType
				.equalsIgnoreCase(BroadBandConnectedClientTestConstants.CLIENT_DEVICE_CONNECTION_TYPE_ETHERNET)) {
			LOGGER.info("OBTAINED CONNECTION TYPE IS : "
					+ BroadBandConnectedClientTestConstants.CLIENT_DEVICE_CONNECTION_TYPE_ETHERNET);
			connectionTypeLog = BroadBandConnectedClientTestConstants.LOG_PARAM_CLIENT_TYPE_ETHERNET;
			searchString = BroadBandTraceConstants.LOG_MESSAGE_TO_VERIFY_ETHERNET_CONNECTED_CLIENT_ONLINE
					.replaceAll("##MACADDRESS##", hostMacAddress);
			alternateString = BroadBandTraceConstants.LOG_MESSAGE_TO_VERIFY_CONNECTED_ETHERNET_CLIENT_CONNECTED
					.replaceAll("##MACADDRESS##", hostMacAddress);
		} else {
			LOGGER.error("verifyConnectedClientDetailsInLMlog API IS ONLY APPLICABLE FOR "
					+ BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI + " AND "
					+ BroadBandConnectedClientTestConstants.CLIENT_DEVICE_CONNECTION_TYPE_ETHERNET + ""
					+ "CONNECTION TYPES BUT OBTAINED CONNECTION TYPE IS " + connectionType);
			isCnnTypeValid = false;
		}
		if (isCnnTypeValid) {
			String command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND,
					searchString, BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandCommandConstants.LOG_FILE_LM,
					BroadBandTestConstants.SYMBOL_PIPE, BroadBandTestConstants.CMD_TAIL_1);
			logStatus = verifyTheLogInformationWithPoolduration(device, tapEnv, connectionTypeLog, hostMacAddress,
					pollDuration, deviceDateTime, command);
			if (!logStatus) {
				command = command.replaceAll(searchString, alternateString);
				logStatus = verifyTheLogInformationWithPoolduration(device, tapEnv, connectionTypeLog, hostMacAddress,
						pollDuration, deviceDateTime, command);
			}
			LOGGER.info("SEARCH STATUS OF LOG '" + connectionTypeLog + "' IS : " + logStatus);
		} else {
			LOGGER.error(
					"SKIPPING LOG VALIDATION AS THE CONNECTION TYPE OBATAINED : " + connectionType + " IS INVALID");
		}
		LOGGER.debug("ENDING METHOD : verifyConnectedClientDetailsInLMlog () ");
		return logStatus;
	}

	/**
	 * Method to verify the connected client details in LMLog.txt.0 file
	 * 
	 * @param device            instance of {@link Dut}
	 * @param tapEnv            instance of {@link AutomaticsTapApi}
	 * @param connectionTypeLog Client Connection Type log message
	 * @param hostMacAddress    Client Mac Address
	 * @param pollDuration      Duration to verify the logs
	 * @param deviceDateTime    Device Date and Time
	 * @param command           command to execute
	 * @return true if expected log message available
	 * 
	 * @refactor Athira
	 * 
	 */
	public static boolean verifyTheLogInformationWithPoolduration(Dut device, AutomaticsTapApi tapEnv,
			String connectionTypeLog, String hostMacAddress, long pollDuration, String deviceDateTime, String command) {
		LOGGER.debug("ENDING METHOD : verifyTheLogInformationWithPoolduration () ");
		boolean logStatus = false;
		String response = null;
		long startTime = System.currentTimeMillis();
		do {
			LOGGER.info("command in verifyTheLogInformationWithPoolduration is" + command);
			response = tapEnv.executeCommandUsingSsh(device, command);
			LOGGER.info("response in verifyTheLogInformationWithPoolduration is" + response);
			if (BroadBandCommonUtils.verifyLogUsingTimeStamp(deviceDateTime, response)) {
				logStatus = CommonUtils.patternSearchFromTargetString(response, connectionTypeLog)
						&& (hostMacAddress.equalsIgnoreCase(CommonMethods
								.patternFinder(response, BroadBandConnectedClientTestConstants.LOG_PARAM_MAC_ADDRESS)
								.trim()));
			}
		} while ((System.currentTimeMillis() - startTime) < pollDuration && !logStatus
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
		LOGGER.debug("ENDING METHOD : verifyTheLogInformationWithPoolduration () ");
		return logStatus;
	}

	/**
	 * Method to verify the associated device count in device
	 * 
	 * @param device       instance of {@link Dut}
	 * @param tapEnv       instance of {@link AutomaticsTapApi}
	 * @param webParameter Webpa parameter
	 * @param pollDuration Duration to verify the logs
	 * @param deviceCount  Expected device count
	 * @return true if expected log message available
	 * 
	 * @refactor Athira
	 */
	public static boolean verifyAssociatedDeviceCount(Dut device, AutomaticsTapApi tapEnv, String webParameter,
			long pollDuration, int deviceCount) {
		LOGGER.debug("STARTING METHOD : verifyAssociatedDeviceCount () ");
		boolean logStatus = false;
		long startTime = System.currentTimeMillis();
		do {
			String response = tapEnv.executeWebPaCommand(device, webParameter);
			LOGGER.info(" response in verifyAssociatedDeviceCount " + response);
			try {
				logStatus = CommonMethods.isNotNull(response) && Integer.parseInt(response) == deviceCount;
				LOGGER.info(" logStatus in verifyAssociatedDeviceCount " + logStatus);
				LOGGER.info(" deviceCount in verifyAssociatedDeviceCount " + deviceCount);
			} catch (NumberFormatException exception) {
				LOGGER.error(exception.getMessage());
			}
		} while ((System.currentTimeMillis() - startTime) < pollDuration && !logStatus
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
		LOGGER.debug("ENDING METHOD : verifyAssociatedDeviceCount () ");
		return logStatus;
	}

	/**
	 * Method to connect the given WiFi capable Client and connect with given WiFi
	 * band
	 *
	 * @param tapEnv          {@link AutomaticsTapApi}
	 * @param device          {@link Dut}
	 * 
	 * @param deviceToConnect Device to be connected
	 * @param wifiBand        Frequency band device needs to be connected
	 * @return connectedStatus Connection Status is success - True,otherwise -
	 *         False'
	 * @refactor Govardhan
	 * 
	 */
	public static boolean connectGivenWiFiCapableClientAndConnectWithGivenWiFiBand(Dut device, AutomaticsTapApi tapEnv,
			Dut deviceToConnect, String wifiBand) {
		LOGGER.debug("STARTING METHOD : connectGivenWiFiCapableClientAndConnectWithGivenWiFiBand () ");
		boolean connectedStatus = false;
		String ssidName = null;
		String passPhraseName = null;
		try {
			ssidName = wifiBand.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ)
					? getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv, WiFiFrequencyBand.WIFI_BAND_2_GHZ)
					: getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv, WiFiFrequencyBand.WIFI_BAND_5_GHZ);
			passPhraseName = wifiBand.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ)
					? getSsidPassphraseFromGatewayUsingWebPaOrDmcli(device, tapEnv, WiFiFrequencyBand.WIFI_BAND_2_GHZ)
					: getSsidPassphraseFromGatewayUsingWebPaOrDmcli(device, tapEnv, WiFiFrequencyBand.WIFI_BAND_5_GHZ);
			if (CommonMethods.isNotNull(ssidName) && CommonMethods.isNotNull(passPhraseName)) {
				connectedStatus = ConnectedNattedClientsUtils.connectToSSID(deviceToConnect, tapEnv, ssidName,
						passPhraseName);
			}
		} catch (Exception exception) {
			throw new TestException("Unable to connect the client with " + wifiBand
					+ " Wi-Fi Network due to following Reason : " + exception.getMessage());
		}
		LOGGER.debug("ENDING METHOD : connectGivenWiFiCapableClientAndConnectWithGivenWiFiBand () ");
		return connectedStatus;
	}

	/**
	 * 
	 * This method is to verify if the blocked site is accessible on the dns server
	 * by checking the DNS Configuration
	 * 
	 * @param tapEnv          instance of {@link AutomaticsTapApi}
	 * @param deviceConnected instance of {@link ConnectedClient}
	 * @param blockedSite     Blocked Site Name
	 * @param dnsServer       Name of the DNS Server
	 * @param dnsSiteBlocker  Dns Site Blocker
	 * 
	 * @return status It return true,if blocked site is accessible. Else False.
	 * @refactor Govardhan
	 */
	public static boolean verifySiteNsLookUpOnCnctdClient(AutomaticsTapApi tapEnv, Dut deviceConnected,
			String blockedSite, String dnsServer, String dnsSiteBlocker) {
		LOGGER.debug("STARTING METHOD : verifySiteNsLookUpOnCnctdClient()");
		String response = null;
		boolean status = false;
		try {
			String nsLookUpCommand = BroadBandCommonUtils.concatStringUsingStringBuffer(
					BroadBandTestConstants.STRING_NS_LOOKUP, AutomaticsConstants.SPACE, blockedSite,
					AutomaticsConstants.SPACE, dnsServer);
			response = tapEnv.executeCommandOnOneIPClients(deviceConnected, nsLookUpCommand)
					.replaceAll(BroadBandTestConstants.PATTERN_MATCHER_FOR_MULTIPLE_SPACES,
							BroadBandTestConstants.SINGLE_SPACE_CHARACTER)
					.replace(BroadBandTestConstants.SITE_WWWW + ".", BroadBandCommandConstants.BLANK);
			LOGGER.info("FINAL RESPONSE :" + response);
			LOGGER.info("DNS SITE BLOCKER :" + dnsSiteBlocker);
			status = CommonMethods.isNotNull(response)
					&& CommonMethods.patternMatcher(response,
							CommonMethods.patternFinder(response, BroadBandTestConstants.PATTERN_SITE_NAME))
					&& !CommonMethods.patternMatcher(response, dnsSiteBlocker);
		} catch (Exception e) {
			LOGGER.error("Exception Occured in verifySiteNsLookUpOnCnctdClient():" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : verifySiteNsLookUpOnCnctdClient()");
		return status;
	}

	/**
	 * Method to add the DNS mapping table and verify the entry in
	 * dnsmasq_servers.conf
	 * 
	 * @param tapEnv          {@link AutomaticsTapApi}
	 * @param device          {@link Dut}
	 * @param deviceConnected instance of {@link ConnectedClient}
	 * @param dnsServerIp     DNS Server Ip Address
	 * @param dnsServerIpType DNS Server Ip Address Type(IPv4/IPv6)
	 * @return status It return true,DNS mapping entry added in dnsmasq_servers.conf
	 *         file. Else False.
	 * @refactor Govardhan
	 */
	public static BroadBandResultObject addDnsMappingTable(Dut device, AutomaticsTapApi tapEnv, Dut deviceConnected,
			String dnsServerIp, String dnsServerIpType) {
		BroadBandResultObject resultObject = new BroadBandResultObject();
		boolean status = false;
		String errorMessage = null;
		LOGGER.debug("STARTING METHOD : addDnsMappingTable()");
		String clientMacAddress = ((Device) deviceConnected).getConnectedDeviceInfo().getWifiMacAddress();
		try {
			if (CommonUtils.isFileExists(device, tapEnv, BroadBandTestConstants.DNSMASQ_CONFIGURATION_FILE)) {
				LOGGER.info("DNSMASQ_SERVERS.CONF FILE EXISTS. GOING ADD DNS MAPPING TABLE USING WEBPA PARAM "
						+ BroadBandWebPaConstants.WEBPA_PARAM_DNS_MAPPING_TABLE);
				if (CommonMethods.isNotNull(clientMacAddress)) {
					List<String> macAddressList = new ArrayList<String>();
					// Create the row in dns server mapping table
					HashMap<String, List<String>> rowParamsMap = new HashMap<String, List<String>>();
					// Add the list of parameters in mapping table
					Map<String, HashMap<String, List<String>>> dnsServerTableMap = new HashMap<String, HashMap<String, List<String>>>();
					macAddressList.add(clientMacAddress);
					rowParamsMap.put(BroadBandTestConstants.STRING_DNS_MAC_ADDRESS, macAddressList);
					if (BroadBandTestConstants.STRING_DNS_IPV6.equalsIgnoreCase(dnsServerIpType)) {
						List<String> dnsIPv6List = new ArrayList<String>();
						dnsIPv6List.add(dnsServerIp);
						rowParamsMap.put(BroadBandTestConstants.STRING_DNS_IPV6, dnsIPv6List);
					} else {
						List<String> dnsIPv4List = new ArrayList<String>();
						dnsIPv4List.add(dnsServerIp);
						rowParamsMap.put(BroadBandTestConstants.STRING_DNS_IPV4, dnsIPv4List);
					}
					List<String> tagList = new ArrayList<String>();
					tagList.add(BroadBandTestConstants.CONNECTED_DEVICE);
					rowParamsMap.put(BroadBandTestConstants.STRING_DNS_TAG, tagList);
					dnsServerTableMap.put(BroadBandTestConstants.STRING_ZERO, rowParamsMap);
					WebPaServerResponse webPaServerResponse = tapEnv.putWebpaTableParamUsingRestApi(device,
							BroadBandWebPaConstants.WEBPA_PARAM_DNS_MAPPING_TABLE, dnsServerTableMap);
					if (webPaServerResponse != null && CommonUtils.patternSearchFromTargetString(
							BroadBandTestConstants.SUCCESS_TXT, webPaServerResponse.getMessage().toLowerCase())) {
						LOGGER.info(
								"DETAILS ADDED IN DNS_MAPPING_TABLE,WAITING FOR TEN SECONDS TO VERIFY THE ENTRY IN DNSMASQ_SERVERS.CONF FILE");
						tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
						status = CommonMethods
								.isNotNull(BroadBandCommonUtils.searchLogFiles(tapEnv, device, dnsServerIp,
										BroadBandTestConstants.DNSMASQ_CONFIGURATION_FILE))
								&& CommonMethods.isNotNull(BroadBandCommonUtils.searchLogFiles(tapEnv, device,
										clientMacAddress, BroadBandTestConstants.DNSMASQ_CONFIGURATION_FILE));
						if (!status) {
							errorMessage = "CLIENT MAC ADDRESS " + clientMacAddress + " AND DNS SERVER IP "
									+ dnsServerIp + " ENTRY NOT AVAILABLE IN DNSMASQ_SERVERS.CONF FILE";
						}
					} else {
						errorMessage = webPaServerResponse.getMessage().toUpperCase();
					}
				} else {
					errorMessage = "CONNECTED DEVICE MAC ADDRESS IS NULL OR INVALID.";
				}
			} else {
				errorMessage = "DNSMASQ_SERVERS.CONF FILE DOESN'T EXIST.";
			}
		} catch (Exception e) {
			errorMessage = e.getMessage();
			LOGGER.error("Exception Occured in addDnsMappingTable():" + e.getMessage());
		}
		resultObject.setErrorMessage(errorMessage);
		resultObject.setStatus(status);
		LOGGER.debug("ENDING METHOD : addDnsMappingTable()");
		return resultObject;
	}

	/**
	 * Method to check internet access for either ipv4 or ipv6 with curl operation
	 * if fails checks with ping operation.
	 * 
	 * @param tapEnv                instance of {@link AutomaticsTapApi}
	 * @param connectedClientSettop Connected client Dut instance
	 * @param ipVersion             String instance of either IPV4 or IPV6.
	 * @return status as true if the Internet is accessible using curl or ping.
	 * 
	 */
	public static BroadBandResultObject checkInternetConnectivityUsingCurlOrPingforIpv4OrIpv6(AutomaticsTapApi tapEnv,
			Dut connectedClientSettop, String ipVersion) {
		LOGGER.debug("STARTING METHOD : checkInternetConnectivityUsingCurlOrPingforIpv4OrIpv6 () ");
		BroadBandResultObject result = null;
		String errorMessage = null;
		try {
			result = BroadBandConnectedClientUtils.verifyInternetIsAccessibleInConnectedClientUsingCurl(tapEnv,
					connectedClientSettop,
					BroadBandTestConstants.URL_HTTPS + BroadBandTestConstants.STRING_GOOGLE_HOST_ADDRESS, ipVersion);
			errorMessage = result.getErrorMessage();
			if (!result.isStatus()) {
				errorMessage = "PIGN OPERATION FAILED TO ACCESS THE SITE 'www.google.com' USING IPV4 ";
				result.setErrorMessage(errorMessage);
				result.setStatus(ConnectedNattedClientsUtils.verifyPingConnectionForIpv4AndIpv6(connectedClientSettop,
						tapEnv, BroadBandTestConstants.PING_TO_GOOGLE, ipVersion));
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while checking for internet accessibility for " + ipVersion
					+ " using curl or ping method " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : checkInternetConnectivityUsingCurlOrPingforIpv4OrIpv6 () ");
		return result;
	}

	/**
	 * Method to get the SSID name using Device.WiFi.SSID.{i}.SSID
	 * 
	 * @param device            instance of {@link Dut}
	 * @param AutomaticsTapApi  instance of {@link AutonaticsTapApi}
	 * @param WiFiFrequencyBand frequency band
	 * @return SSID name
	 * 
	 * @Refactor Sruthi Santhosh
	 */
	public static String getSecuredPublicSsidNameFromGatewayUsingWebPaOrDmcli(Dut device, AutomaticsTapApi tapEnv,
			WiFiFrequencyBand wifiBand) {
		LOGGER.debug("START METHOD : getSecuredPublicSsidNameFromGatewayUsingWebPaOrDmcli () ");
		String ssidGatewayDevice = null;
		String errorMessage = null;
		if (wifiBand.compareTo(WiFiFrequencyBand.WIFI_BAND_2_GHZ) == 0) {
			ssidGatewayDevice = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_2GHZ_SECURED_PUBLIC_WIFI_SSID);
		} else if (wifiBand.compareTo(WiFiFrequencyBand.WIFI_BAND_5_GHZ) == 0) {
			ssidGatewayDevice = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_5GHZ_SECURED_PUBLIC_WIFI_SSID);
		}
		if (CommonMethods.isNull(ssidGatewayDevice)) {
			errorMessage = "Not able to get the Secured Public SSID name for client device connected with " + wifiBand
					+ " GHz ";
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}
		LOGGER.debug("Ending METHOD : getSecuredPublicSsidNameFromGatewayUsingWebPaOrDmcli () ");
		return ssidGatewayDevice;
	}

	/**
	 * Method to Enable, Disable and Broadacast Lnf SSIDs
	 * 
	 * @param device     Dut instance
	 * @param tapEnv     AutomaticsTapApi instance
	 * @param enableFlag Variable which indicates enable or disable action
	 * @return true if successfully enabled or disabled as per enableFlag
	 * 
	 * @refactor Athira
	 */
	public static boolean enableDisableAndBroadcastLnfSsid(Dut device, AutomaticsTapApi tapEnv, String enableFlag) {
		LOGGER.debug("STARTING METHOD: enableDisableAndBroadcastLnfSsid()");
		// Variable declaration starts
		boolean status = false;
		String errorMessage = "";
		BroadBandResultObject bandResultObject;
		List<WebPaParameter> listOfSetParameter = new ArrayList<WebPaParameter>();
		List<WebPaParameter> listOfSetParameterToApply = new ArrayList<WebPaParameter>();
		// Variable declaration ends
		try {
			listOfSetParameter.add(BroadBandWebPaUtils.setAndReturnWebPaParameterObject(
					BroadBandWebPaConstants.WEBPA_WAREHOUSE_WIRELESS_SSID1_ENABLE_LNF_2_4, enableFlag,
					BroadBandTestConstants.CONSTANT_3));
			listOfSetParameter.add(BroadBandWebPaUtils.setAndReturnWebPaParameterObject(
					BroadBandWebPaConstants.WEBPA_WAREHOUSE_WIRELESS_SSID_ENABLE_LNF_5G, enableFlag,
					BroadBandTestConstants.CONSTANT_3));
			listOfSetParameter.add(BroadBandWebPaUtils.setAndReturnWebPaParameterObject(
					BroadBandWebPaConstants.WEBPA_PARAM_Device_WiFi_AccessPoint_10004_SSIDAdvertisementEnabled,
					enableFlag, BroadBandTestConstants.CONSTANT_3));
			listOfSetParameter.add(BroadBandWebPaUtils.setAndReturnWebPaParameterObject(
					BroadBandWebPaConstants.WEBPA_PARAM_Device_WiFi_AccessPoint_10104_SSIDAdvertisementEnabled,
					enableFlag, BroadBandTestConstants.CONSTANT_3));
			listOfSetParameter.add(BroadBandWebPaUtils.setAndReturnWebPaParameterObject(
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_10004_SSID,
					BroadBandTestConstants.STRING_LNF_SSID_24GHZ, BroadBandTestConstants.CONSTANT_0));
			listOfSetParameter.add(BroadBandWebPaUtils.setAndReturnWebPaParameterObject(
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_10104_SSID,
					BroadBandTestConstants.STRING_LNF_SSID_5GHZ, BroadBandTestConstants.CONSTANT_0));
			bandResultObject = BroadBandWebPaUtils.executeSetAndGetOnMultipleWebPaGetParams(device, tapEnv,
					listOfSetParameter);
			status = bandResultObject.isStatus();
			LOGGER.info("bandResultObject.isStatus is : " + status);
			if (status) {
				listOfSetParameterToApply.add(BroadBandWebPaUtils.setAndReturnWebPaParameterObject(
						BroadBandWebPaConstants.WEBPA_PARAM_WIFI_2_4_APPLY_SETTING, enableFlag,
						BroadBandTestConstants.CONSTANT_3));
				listOfSetParameterToApply.add(BroadBandWebPaUtils.setAndReturnWebPaParameterObject(
						BroadBandWebPaConstants.WEBPA_PARAM_WIFI_5_APPLY_SETTING, enableFlag,
						BroadBandTestConstants.CONSTANT_3));
				Map<String, String> responseMap = tapEnv.executeMultipleWebPaSetCommands(device,
						listOfSetParameterToApply);

				status = responseMap.containsValue("true");
				LOGGER.info("bandResultObject.isStatus for true is : " + status);
				status = responseMap.containsValue(RDKBTestConstants.SNMP_RESPONSE_SUCCESS);
				LOGGER.info("bandResultObject.isStatus for Success is : " + status);
			}
		} catch (Exception exception) {
			errorMessage = errorMessage + exception.getMessage();
			LOGGER.error(errorMessage);
		}
		LOGGER.debug("ENDING METHOD: enableDisableAndBroadcastLnfSsid()");
		return status;
	}

	/**
	 * Method to execute pre-condition for whix user stories
	 * 
	 * @param device
	 * @param tapEnv
	 * @return
	 * @refactor Athira
	 */
	public static List<Dut> executePreconditionForDeviceList(Dut device, AutomaticsTapApi tapEnv) {
		List<Dut> dualBandDevices = null;
		dualBandDevices = BroadBandBandSteeringUtils.getConnectedClientBasedOnCapability(device, tapEnv,
				((Device) device).getConnectedDeviceList(),
				BroadBandConnectedClientTestConstants.STRING_WIFI_CAPABILITY_DUAL_BAND);
		if (dualBandDevices != null && dualBandDevices.size() >= 2) {
			List<Dut> tempList = new ArrayList<Dut>();
			for (Dut client : dualBandDevices) {
				if (((Device) client).isWindows()) {
					tempList.add(client);
					if (tempList.size() == BroadBandTestConstants.CONSTANT_2) {
						break;
					}
				}
			}
			dualBandDevices = tempList;
			if (!(dualBandDevices.size() == BroadBandTestConstants.CONSTANT_2)
					&& !BroadBandWiFiUtils.enableDisableWifiStats(device, tapEnv, BroadBandTestConstants.TRUE)) {
				throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR
						+ "PRE_CONDITION_FAILED :The device doesn't have two windows client "
						+ "connected or enabling wifi stats was not successful" + device.getHostMacAddress());
			} else {

				LOGGER.info("### Pre-Condition checks are executed successfully!");
				LOGGER.info("######################################################");
			}
		} else {
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR
					+ "PRE_CONDITION_FAILED :The device doesn't have two windows client connected "
					+ device.getHostMacAddress());
		}
		return dualBandDevices;
	}

	/**
	 * Helper method to get Connected client device ip or mac from the same windows
	 * device
	 * 
	 * @param connectedClientDettop Connected client Dut instance
	 * @param tapEnv                AutomaticsTapApi instance
	 * @param isIpNeeded            true, if ip of the device is needed, false for
	 *                              mac
	 * @return ip of mac of the connected client device
	 * @author Praveenkumar Paneerselvam
	 * @refactor Athira
	 */
	public static String getIpOrMacFromWindowsConnectedClient(Dut connectedClientDettop, AutomaticsTapApi tapEnv,
			boolean isIpNeeded) {
		LOGGER.debug("STARTING METHOD: getIpOrMacFromWindowsConnectedClient()");
		String value = null;
		String connectionType = ((Device) connectedClientDettop).getConnectedDeviceInfo().getConnectionType();
		LOGGER.info("Windows device is connected to Gateway device to " + connectionType);
		String command = null;
		String parameter = BroadBandTestConstants.STRING_IP_4_ADDRESS;
		if (!isIpNeeded) {
			parameter = BroadBandTestConstants.STRING_PHYSICAL_ADDRESS;
		}
		String searchTrace = null;
		if (CommonMethods.isNotNull(connectionType)
				&& BroadBandTestConstants.CONNECTION_TYPE_ETHERNET.equalsIgnoreCase(connectionType)) {
			searchTrace = BroadBandTraceConstants.LOG_MESSAGE_IPCONFIG_ETHERNET;
		} else if (CommonMethods.isNotNull(connectionType)
				&& BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI
						.equalsIgnoreCase(connectionType)) {
			searchTrace = BroadBandTraceConstants.LOG_MESSAGE_IPCONFIG_WIFI;
		}
		if (CommonMethods.isNotNull(searchTrace)) {
			command = BroadBandCommonUtils.concatStringUsingStringBuffer(
					BroadBandCommandConstants.CMD_IPCONFIG_ALL_GREP_A20, searchTrace,
					BroadBandTestConstants.SYMBOL_PIPE, BroadBandTestConstants.GREP_COMMAND,
					BroadBandTestConstants.DOUBLE_QUOTE, parameter, BroadBandTestConstants.DOUBLE_QUOTE);
			LOGGER.info("Command to be executed is " + command);
			String response = tapEnv.executeCommandOnOneIPClients(connectedClientDettop, command);
			value = CommonMethods.isNotNull(response)
					? CommonMethods.patternFinder(response, parameter + BroadBandTestConstants.STRING_REGEX_IP_MAC)
					: null;
		}
		if (!isIpNeeded && CommonMethods.isNotNull(value)) {
			value = value.replaceAll(BroadBandTestConstants.SYMBOL_HYPHEN, BroadBandTestConstants.DELIMITER_COLON);
		}
		LOGGER.info("Value of Windows connected client is " + value);
		LOGGER.debug("ENDING METHOD: getIpOrMacFromWindowsConnectedClient()");
		return value;
	}

	/**
	 * Method to validate the presence of notification
	 * 
	 * @param device
	 * @param tapEnv
	 * @param clientMac
	 * @param filename
	 * @param isPresent
	 * @return validationResult
	 * @refactor Athira
	 */
	public static boolean validatePresenceOfConnectNotification(Dut device, AutomaticsTapApi tapEnv, String clientMac,
			String filename, boolean isPresent) {
		boolean validationResult = false;
		String response = null;
		if (CommonMethods.isNotNull(clientMac)) {
			if (filename.equals(BroadBandTestConstants.FILE_NAME_LM)) {
				response = tapEnv.executeCommandUsingSsh(device,
						BroadBandCommandConstants.CMD_SEARCH_FILE_TEXT
								.replace(BroadBandTestConstants.TR181_NODE_REF, filename)
								.replace(BroadBandTestConstants.SECONDARY_NODE_REF, clientMac));
				validationResult = isPresent ? CommonMethods.isNotNull(response) : !CommonMethods.isNotNull(response);
			} else {
				response = CommonMethods.isAtomSyncAvailable(device, tapEnv)
						? tapEnv.executeCommandOnAtom(device,
								BroadBandCommandConstants.CMD_SEARCH_FILE_TEXT
										.replace(BroadBandTestConstants.TR181_NODE_REF, filename)
										.replace(BroadBandTestConstants.SECONDARY_NODE_REF, clientMac))
						: tapEnv.executeCommandUsingSsh(device,
								BroadBandCommandConstants.CMD_SEARCH_FILE_TEXT
										.replace(BroadBandTestConstants.TR181_NODE_REF, filename)
										.replace(BroadBandTestConstants.SECONDARY_NODE_REF, clientMac));
				validationResult = isPresent ? CommonMethods.isNotNull(response) : !CommonMethods.isNotNull(response);
			}
		}
		return validationResult;
	}

	/**
	 * Method to validate host table entry for a client
	 * 
	 * @param device
	 * @param tapEnv
	 * @param clientMac
	 * @return
	 */
	public static boolean validateIfClientsAreAddedToHostTable(Dut device, AutomaticsTapApi tapEnv, List<Dut> client) {
		boolean result = false;
		String response = null;
		int noOfHosts = 0;

		response = tapEnv.executeWebPaCommand(device,
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_HOST_NUMBER_OF_ENTRIES);

		if (CommonMethods.isNotNull(response) && null != client && client.size() > BroadBandTestConstants.CONSTANT_0) {
			noOfHosts = BroadBandCommonUtils.convertStringToInteger(response);
			LOGGER.info("The no of Hosts: " + noOfHosts);
			for (Dut ClientDevice : client) {
				String mac = getIpOrMacFromWindowsConnectedClient(ClientDevice, tapEnv, false);
				result = false;
				if (noOfHosts > BroadBandTestConstants.CONSTANT_0) {
					for (int index = 1; index <= noOfHosts; index++) {
						response = tapEnv.executeWebPaCommand(device,
								BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_HOST_PHYSICAL_ADDRESS.replace(
										BroadBandTestConstants.TR181_NODE_REF,
										BroadBandCommonUtils.convertIntegerToString(index)));
						if (CommonMethods.isNotNull(response) && response.equalsIgnoreCase(mac)) {
							result = true;
							break;
						}
					}
				}
				if (!result) {
					LOGGER.info("The host table entries are not present for the client: " + mac);
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Method to get the associated device count in device
	 * 
	 * @param device       instance of {@link Dut}
	 * @param tapEnv       instance of {@link AutomaticsTapApi}
	 * @param webParameter Webpa parameter
	 * @return the associated deivice count
	 * 
	 * @refactor Athira
	 * 
	 */
	public static int getAssociatedDeviceCount(Dut device, AutomaticsTapApi tapEnv, String webParameter) {
		LOGGER.debug("STARTING METHOD : getAssociatedDeviceCount() ");
		int deviceCount = 0;
		String response = null;
		try {
			response = tapEnv.executeWebPaCommand(device, webParameter);
			deviceCount = Integer.parseInt(response);
		} catch (NumberFormatException exception) {
			LOGGER.error("getAssociatedDeviceCount NumberFormatException :" + exception.getMessage());
		} catch (Exception exception) {
			LOGGER.error("getAssociatedDeviceCount Exception :" + exception.getMessage());
		}
		LOGGER.debug("ENDING METHOD : getAssociatedDeviceCount() ");
		return deviceCount;
	}

	/**
	 * Utility method used to get the firefox driver version
	 * 
	 * @param device   Instance of {@link Dut}
	 * @param tapEnv   Instance of {@link AutomaticsTapApi}
	 * @param filePath File path
	 * @return driverVersion- It will return the geckodriver version
	 * @refactor Govardhan
	 */
	public static int getGeckoDriverVersion(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: getGeckoDriverVersion()");
		int driverVersion = BroadBandTestConstants.CONSTANT_0;
		String response = null;
		for (int iteration = BroadBandTestConstants.CONSTANT_0; iteration < BroadBandTestConstants.CONSTANT_3; iteration++) {
			try {
				response = tapEnv.executeCommandOnOneIPClients(device,
						BroadBandCommonUtils.concatStringUsingStringBuffer(
								BroadBandConnectedClientTestConstants.CMD_TO_GET_THE_GECKO_DRIVER_VERSION));
				if (CommonMethods.isNotNull(response)) {
					driverVersion = BroadBandCommonUtils.convertStringToInteger(CommonMethods.patternFinder(response,
							BroadBandConnectedClientTestConstants.PATTERN_TO_GET_GECKO_DRIVER_VERSION_FROM_CLIENT));
				}
			} catch (Exception e) {
				LOGGER.error("Exception occured in getGeckoDriverVersion()" + e.getMessage());
			}
			if (driverVersion > BroadBandTestConstants.CONSTANT_0) {
				break;
			} else {
				BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
			}
		}
		LOGGER.info("Driver Version: " + driverVersion);
		LOGGER.debug("ENDING METHOD: getGeckoDriverVersion()");
		return driverVersion;
	}

	/**
	 * Utility method used to verify the unzip option support on connected client
	 * 
	 * @param device instance of{@link Device}
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @return status True-Unzip support available,Else-Flse
	 * @refactor Govardhan
	 */
	public static boolean verifyUnzipSupportOnConnectedClient(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: verifyUnzipSupportOnConnectedClient()");
		boolean status = false;
		String response = null;
		try {
			response = tapEnv.executeCommandOnOneIPClients(device,
					BroadBandConnectedClientTestConstants.CMD_TO_CHECK_UNZIP_SUPPORT);
			status = CommonMethods.isNotNull(response) && !CommonUtils.isGivenStringAvailableInCommandOutput(response,
					BroadBandTestConstants.CMD_NOT_FOUND);
			LOGGER.info("Unzip support" + (status ? " " : " not ") + "available in Cygwin");
		} catch (Exception e) {
			LOGGER.error("Exception Occurred in verifyUnzipSupportOnConnectedClient() " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: verifyUnzipSupportOnConnectedClient()");
		return status;
	}

	/**
	 * Utility method used to get the chrome driver exe file with path
	 * 
	 * @return - Return the file name with path
	 * @refactor Govardhan
	 */
	public static String getChromeExeFileNameWithLocation() {
		LOGGER.debug("STARTING METHOD: getChromeExeFileNameWithLocation()");
		String filePathForChromeExe = null;
		try {
			filePathForChromeExe = BroadBandCommonUtils.concatStringUsingStringBuffer(
					BroadBandConnectedClientTestConstants.CYGWIN_SELENUIM_FILE_PATH,
					AutomaticsPropertyUtility.getProperty(
							BroadBandConnectedClientTestConstants.SELENIUM_CONFIG_CHROME_DRIVER_FILE_NAME));
		} catch (Exception e) {
			LOGGER.error("Exception occured in getChromeExeFileNameWithLocation()" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: getChromeExeFileNameWithLocation()");
		return filePathForChromeExe;
	}

	/**
	 * Utility method used to verify the version check for browser and driver
	 * 
	 * @param browserVersion Browser version
	 * @param driverVersion  Driver version
	 * @return True- Both versions are same, Else-False
	 * @refactor Govardhan
	 */
	public static boolean verifyBrowserAndDriverVersionsAreSame(int browserVersion, int driverVersion) {
		LOGGER.debug("STARTING METHOD: verifyBrowserAndDriverVersionsAreSame()");
		boolean status = false;
		try {
			LOGGER.info("Driver Version: " + driverVersion + " Browser Version: " + browserVersion);
			if (browserVersion != BroadBandTestConstants.CONSTANT_0
					&& driverVersion != BroadBandTestConstants.CONSTANT_0) {
				status = (driverVersion == browserVersion);
			} else {
				LOGGER.info("Browser and driver versions is Zero");
			}
			LOGGER.info(status ? "Browser and driver versions are same" : "Browser and driver versions are not same");
		} catch (Exception e) {
			LOGGER.error("Exception occured in verifyBrowserAndDriverVersionsAreSame()" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: verifyBrowserAndDriverVersionsAreSame()");
		return status;
	}

	/**
	 * Utility method used to start the chrome driver
	 * 
	 * @param device instance of{@link Dut}
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @return True - Chrome driver started successfully,Else-False
	 * @refactor Govardhan
	 */
	public static boolean startChromeDriver(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: startChromeDriver()");
		boolean status = false;
		String command = null;
		String filePath = null;
		String response = null;
		try {
			filePath = BroadBandCommonUtils.concatStringUsingStringBuffer(
					BroadBandConnectedClientTestConstants.CYGWIN_SELENUIM_FILE_PATH,
					AutomaticsPropertyUtility.getProperty(
							BroadBandConnectedClientTestConstants.SELENIUM_CONFIG_CHROME_DRIVER_FILE_NAME));
			command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_CHMOD,
					BroadBandCommandConstants.CHMOD_777_VALUE, BroadBandTestConstants.SINGLE_SPACE_CHARACTER, filePath);
			tapEnv.executeCommandOnOneIPClients(device, command);
			response = tapEnv.executeCommandOnOneIPClients(device, filePath);
			LOGGER.info(response);
			status = verifyChromeDriverProcessRunningStatusOnWindowsClient(device, tapEnv);
		} catch (Exception e) {
			LOGGER.error("Exception Occurred in startChromeDriver() " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: startChromeDriver()");
		return status;
	}

	/**
	 * Utility method used to verify the chrome driver process running status on
	 * Windows client
	 * 
	 * @param device instance of{@link Device}
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @return status True - Process running. False - Process not running
	 * @refactor Govardhan
	 */
	public static boolean verifyChromeDriverProcessRunningStatusOnWindowsClient(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: verifyChromeDriverProcessRunningStatusOnWindowsClient()");
		String response = null;
		boolean status = false;
		try {
			response = tapEnv.executeCommandOnOneIPClients(device,
					BroadBandConnectedClientTestConstants.CHROME_DRIVER_PROCESS_STATUS_FOR_WINDOWS);
			LOGGER.info("Chrome Driver running status:" + response);
			status = CommonMethods.isNotNull(response) && CommonMethods.patternMatcher(response,
					BroadBandConnectedClientTestConstants.PATTERN_FOR_CHROME_DRIVER_PROCESS_RUNNING_STATUS);
			LOGGER.info(status ? "Chrome driver is up and running" : "Chrome driver is not up and running");
		} catch (Exception e) {
			LOGGER.error("Exception Occurred in verifyChromeDriverProcessRunningStatusOnWindows() " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: verifyChromeDriverProcessRunningStatusOnWindowsClient()");
		return status;
	}

	/**
	 * Utility method used to get the chrome driver zip file with path from download
	 * location
	 * 
	 * @return - Return the file name with path
	 * @refactor Govardhan
	 */
	public static String getChromeZipFileNameWithLocation(Dut device) {
		LOGGER.debug("STARTING METHOD: getChromeZipFileNameWithLocation()");
		String downloadedFilePath = null;
		Device ecastSettop = (Device) device;
		try {
			downloadedFilePath = BroadBandCommonUtils.concatStringUsingStringBuffer(
					BroadBandConnectedClientTestConstants.DOWNLOAD_FILE_PATH
							.replaceAll(BroadBandTestConstants.STRING_REPLACE, ecastSettop.getUsername()),
					AutomaticsPropertyUtility
							.getProperty(BroadBandConnectedClientTestConstants.CHROME_DRIVER_DOWNLOAD_ZIP_FILE_NAME));
		} catch (Exception e) {
			LOGGER.error("Exception occured in getChromeZipFileNameWithLocation()" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: getChromeZipFileNameWithLocation()");
		return downloadedFilePath;
	}

	/**
	 * Utility method used to download and move chrome driver to configured location
	 * 
	 * @param device            instance of{@link Dut}
	 * @param tapEnv            instance of {@link AutomaticsTapApi}
	 * @param versionToDownload Version to download
	 * @param userName          User name
	 * @return status True - Process running. False - Process not running
	 * @refactor Govardhan
	 */
	public static boolean downloadChromeDriverAndMoveTheFileToConfiguredLocation(Dut device, AutomaticsTapApi tapEnv,
			String versionToDownload, String userName) {
		LOGGER.debug("STARTING METHOD: downloadChromeDriverAndMoveTheFileToConfiguredLocation()");
		boolean status = false;
		String command = null;
		String response = null;
		boolean isDownloaded = false;
		try {
			command = getChromeZipFileNameWithLocation(device);
			if (isFileExistsInConnectedClient(device, tapEnv, command, BroadBandTestConstants.BOOLEAN_VALUE_TRUE)) {
				tapEnv.executeCommandOnOneIPClients(device,
						BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_TO_REMOVE,
								BroadBandTestConstants.SINGLE_SPACE_CHARACTER, command));
			}
			response = tapEnv.executeCommandOnOneIPClients(device,
					BroadBandConnectedClientTestConstants.CMD_TO_DOWNLOAD_DRIVER
							.replaceAll(BroadBandTestConstants.STRING_VALUE_TO_REPLACE, versionToDownload)
							.replaceAll(BroadBandTestConstants.STRING_REPLACE, userName));
			if (CommonMethods.isNotNull(response)) {
				isDownloaded = !(CommonUtils.patternSearchFromTargetString(response,
						BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_CONNECTION_SSL_HANDSHAKE_FAILURE_MESSAGE)
						|| CommonUtils.patternSearchFromTargetString(response,
								BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_FAILURE_MESSAGE)
						|| CommonUtils.patternSearchFromTargetString(response,
								BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_CONNECTION_TIMEOUT_MESSAGE)
						|| CommonUtils.patternSearchFromTargetString(response,
								BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_OPERATION_TIMEOUT_MESSAGE)
						|| CommonUtils.patternSearchFromTargetString(response,
								BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_RESOLVING_TIMEOUT_MESSAGE)
						|| CommonUtils.patternSearchFromTargetString(response,
								BroadBandTestConstants.ACCESS_TO_URL_USING_CURL_CONNECTION_COULD_NOT_RESOLVE_HOST_MESSAGE));
			}
			LOGGER.info("File downloaded:" + isDownloaded);
			if (isDownloaded && isFileExistsInConnectedClient(device, tapEnv, command,
					BroadBandTestConstants.BOOLEAN_VALUE_TRUE)) {
				command = getChromeExeFileNameWithLocation();
				if (isFileExistsInConnectedClient(device, tapEnv, command, BroadBandTestConstants.BOOLEAN_VALUE_TRUE)) {
					tapEnv.executeCommandOnOneIPClients(device,
							BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_TO_REMOVE,
									BroadBandTestConstants.SINGLE_SPACE_CHARACTER, command));
				}
				tapEnv.executeCommandOnOneIPClients(device,
						BroadBandConnectedClientTestConstants.CMD_TO_UNZIP_AND_MOVE_THE_DRIVER
								.replaceAll(BroadBandTestConstants.STRING_REPLACE, userName));
				status = isFileExistsInConnectedClient(device, tapEnv, command,
						BroadBandTestConstants.BOOLEAN_VALUE_TRUE);
			}
			LOGGER.info(status ? "File download and unzip is success" : "File download/unzip is failed");
		} catch (Exception e) {
			LOGGER.error(
					"Exception Occurred in downloadChromeDriverAndMoveTheFileToConfiguredLocation() " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: downloadChromeDriverAndMoveTheFileToConfiguredLocation()");
		return status;
	}

	/**
	 * Utility method used to download and move the file to actual configured
	 * selenium location and verify the file presence in configured location
	 * 
	 * @param device   Instance of {@link Dut}
	 * @param tapEnv   Instance of {@link AutomaticsTapApi}
	 * @param FileName File Name
	 * @return True- File present in configured location, Else-False
	 * @refactor Govardhan
	 */
	public static boolean moveDownloadedFileToConfiguredLocation(Dut device, AutomaticsTapApi tapEnv, String FileName) {
		LOGGER.debug("STARTING METHOD: moveDownloadedFileToConfiguredLocation()");
		boolean status = false;
		String fileNameWithPath = null;
		String downloadedLocation = null;
		Device ecastSettop = (Device) device;
		try {
			fileNameWithPath = BroadBandCommonUtils.concatStringUsingStringBuffer(
					BroadBandConnectedClientTestConstants.CYGWIN_SELENUIM_FILE_PATH, FileName);
			LOGGER.info("File Name With Path:" + fileNameWithPath);
			if (isFileExistsInConnectedClient(device, tapEnv, fileNameWithPath,
					BroadBandTestConstants.BOOLEAN_VALUE_FALSE)) {
				downloadedLocation = BroadBandConnectedClientTestConstants.DOWNLOAD_FILE_PATH
						.replaceAll(BroadBandTestConstants.STRING_REPLACE, ecastSettop.getUsername());
				LOGGER.info("Downloaded Location:" + downloadedLocation);
				if (downloadFileUsingAutoVaultToConnectedClient(device, tapEnv, BroadBandCommonUtils
						.concatStringUsingStringBuffer(BroadBandTestConstants.AUTOVAULT_FILE_PATH, FileName),
						downloadedLocation, FileName)) {
					LOGGER.info(FileName + " File Downloaded");
					tapEnv.executeCommandOnOneIPClients(device,
							BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_COPY,
									downloadedLocation, FileName, BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
									BroadBandConnectedClientTestConstants.CYGWIN_SELENUIM_FILE_PATH));
					LOGGER.info("Executed copy command");
				}
			}
			status = isFileExistsInConnectedClient(device, tapEnv, fileNameWithPath,
					BroadBandTestConstants.BOOLEAN_VALUE_TRUE);
		} catch (Exception e) {
			LOGGER.error("Exception occured in moveDownloadedFileToConfiguredLocation()" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: moveDownloadedFileToConfiguredLocation()");
		return status;
	}

	/**
	 * Utility method used to run the version check vbs file
	 * 
	 * @param device               Instance of {@link Dut}
	 * @param tapEnv               Instance of {@link AutomaticsTapApi}
	 * @param seleniumHomeLocation Selenium Home Location
	 * @refactor Govardhan
	 */
	public static void runVersionCheckVbsFile(Dut device, AutomaticsTapApi tapEnv, String seleniumHomeLocation) {
		LOGGER.debug("STARTING METHOD: runVersionCheckVbsFile()");
		String command = null;
		StringBuffer filePath = new StringBuffer();
		String versionCheckBatFileName = null;
		String versionCheckVbsFileName = null;
		try {
			versionCheckBatFileName = AutomaticsPropertyUtility
					.getProperty(BroadBandConnectedClientTestConstants.CHROMEDRIVER_VERSION_CHECK_BAT_FILE_NAME);
			versionCheckVbsFileName = AutomaticsPropertyUtility
					.getProperty(BroadBandConnectedClientTestConstants.CHROMEDRIVER_VERSION_CHECK_VBS_FILE_NAME);
			LOGGER.info("Bat File Name:" + versionCheckBatFileName);
			LOGGER.info("VBS File Name:" + versionCheckVbsFileName);
			if (moveDownloadedFileToConfiguredLocation(device, tapEnv, versionCheckBatFileName)
					&& moveDownloadedFileToConfiguredLocation(device, tapEnv, versionCheckVbsFileName)) {
				command = AutomaticsPropertyUtility
						.getProperty(BroadBandConnectedClientTestConstants.SELENIUM_REGISTER_CMD_FOR_WONDOWS_OS);
				filePath.append(AutomaticsConstants.SYMBOL_QUOTES).append(seleniumHomeLocation)
						.append(versionCheckVbsFileName).append(AutomaticsConstants.SYMBOL_QUOTES);
				command = SeleniumNodeConnectionHandler.getFilePathCommand(command,
						BroadBandConnectedClientTestConstants.REPLACE_STRING_VBS_FILE_LOCATION, filePath.toString());
				tapEnv.executeCommandOnOneIPClients(device, command);
				BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured in runVersionCheckVbsFile()" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: runVersionCheckVbsFile()");
	}

	/**
	 * Utility method used to get the version output file name with path
	 * 
	 * @return - Return the file name with path
	 * @refactor Govardhan
	 */
	public static String getVersionOutputFileNameWithLocation() {
		LOGGER.debug("STARTING METHOD: getVersionOutputFileNameWithLocation()");
		String filePathForVersionCheck = null;
		try {
			filePathForVersionCheck = BroadBandCommonUtils.concatStringUsingStringBuffer(
					BroadBandConnectedClientTestConstants.CYGWIN_SELENUIM_FILE_PATH, AutomaticsPropertyUtility
							.getProperty(BroadBandConnectedClientTestConstants.VERSION_CHECK_OUTPUT_FILE_NAME));
		} catch (Exception e) {
			LOGGER.error("Exception occured in getVersionOutputFileNameWithLocation()" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: getVersionOutputFileNameWithLocation()");
		return filePathForVersionCheck;
	}

	/**
	 * Utility method used to get the chrome driver information from goolge api
	 * 
	 * @param urlToGetDriverInfo Url to get the driver version information
	 * @return response - It will return the expected driver information
	 * @refactor Govardhan
	 */
	public static String getDriverVersionInformation(String urlToGetDriverInfo) {
		LOGGER.debug("STARTING METHOD: getDriverVersionInformation()");
		String response = null;
		URL url = null;
		Scanner scanner = null;
		StringBuffer stringBuffer = new StringBuffer();
		try {
			url = new URL(urlToGetDriverInfo);
			scanner = new Scanner(url.openStream());
			while (scanner.hasNext()) {
				stringBuffer.append(scanner.next());
			}
			response = stringBuffer.toString();
		} catch (Exception e) {
			LOGGER.error("Exception Occurred in getDriverVersionInformation() " + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: getDriverVersionInformation()");
		return response;
	}

	/**
	 * Utility method used to get the chrome driver version
	 * 
	 * @param device   Instance of {@link Dut}
	 * @param tapEnv   Instance of {@link AutomaticsTapApi}
	 * @param filePath File path
	 * @return driverVersion- It will return the chrome driver version
	 * @refactor Govardhan
	 */
	public static int getChromeDriverVersion(Dut device, AutomaticsTapApi tapEnv, String filePath) {
		LOGGER.debug("STARTING METHOD: getChromeDriverVersion()");
		int driverVersion = BroadBandTestConstants.CONSTANT_0;
		String response = null;
		for (int iteration = BroadBandTestConstants.CONSTANT_0; iteration < BroadBandTestConstants.CONSTANT_3; iteration++) {
			try {
				response = tapEnv.executeCommandOnOneIPClients(device,
						BroadBandConnectedClientTestConstants.CMD_TO_GET_THE_CHROME_DRIVER_VERSION
								.replaceAll(BroadBandTestConstants.STRING_REPLACE, filePath));
				if (CommonMethods.isNotNull(response)) {
					driverVersion = BroadBandCommonUtils.convertStringToInteger(CommonMethods.patternFinder(response,
							BroadBandConnectedClientTestConstants.PATTERN_TO_GET_DRIVER_VERSION));
				}
			} catch (Exception e) {
				LOGGER.error("Exception occured in getChromeDriverVersion()" + e.getMessage());
			}
			if (driverVersion > BroadBandTestConstants.CONSTANT_0) {
				break;
			} else {
				BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
			}
		}
		LOGGER.info("Driver Version: " + driverVersion);
		LOGGER.debug("ENDING METHOD: getChromeDriverVersion()");
		return driverVersion;
	}

	/**
	 * Utility method used to get the chrome browser version
	 * 
	 * @param device   Instance of {@link Dut}
	 * @param tapEnv   Instance of {@link AutomaticsTapApi}
	 * @param filePath File path
	 * @return browserVersion- It will return the chrome browser version
	 * @refactor Govardhan
	 */
	public static int getChromeBrowserVersion(Dut device, AutomaticsTapApi tapEnv, String filePath) {
		LOGGER.debug("STARTING METHOD: getChromeBrowserVersion()");
		int browserVersion = BroadBandTestConstants.CONSTANT_0;
		String response = null;
		for (int iteration = BroadBandTestConstants.CONSTANT_0; iteration < BroadBandTestConstants.CONSTANT_3; iteration++) {
			try {
				response = tapEnv.executeCommandOnOneIPClients(device,
						BroadBandConnectedClientTestConstants.CMD_TO_GET_THE_CHROME_BROWSER_VERSION
								.replaceAll(BroadBandTestConstants.STRING_REPLACE, filePath));
				if (CommonMethods.isNotNull(response)) {
					browserVersion = BroadBandCommonUtils.convertStringToInteger(CommonMethods.patternFinder(
							response.toLowerCase(),
							BroadBandConnectedClientTestConstants.PATTERN_TO_GET_BROWSER_VERSION.toLowerCase()));
				}
			} catch (Exception e) {
				LOGGER.error("Exception occured in getChromeBrowserVersion()" + e.getMessage());
			}
			if (browserVersion > BroadBandTestConstants.CONSTANT_0) {
				break;
			} else {
				BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
			}
		}
		LOGGER.info(" Browser Version: " + browserVersion);
		LOGGER.debug("ENDING METHOD: getChromeBrowserVersion()");
		return browserVersion;
	}

	/**
	 * Utility method to get pattern for chrome version from google api page
	 * 
	 * @param browserVersion Browser Version
	 * @return pattern Pattern for chrome driver version
	 * @refactor Govardhan
	 */
	public static String getPatternForChromeDriverVersion(int browserVersion) {
		LOGGER.debug("STARTING METHOD: getPatternForChromeDriverVersion()");
		String pattern = null;
		try {
			pattern = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.OPEN_PARENTHESIS,
					BroadBandConnectedClientTestConstants.LATEST_RELEASE, String.valueOf(browserVersion),
					BroadBandTestConstants.DOT_OPERATOR, BroadBandTestConstants.COMMON_PATTERN_FOR_INTEGER,
					BroadBandTestConstants.DOT_OPERATOR, BroadBandTestConstants.COMMON_PATTERN_FOR_INTEGER,
					BroadBandTestConstants.CLOSED_PARANTHESIS);
		} catch (Exception e) {
			LOGGER.error("Exception occured in getPatternForChromeDriverVersion()" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: getPatternForChromeDriverVersion()");
		return pattern;
	}

	/**
	 * Utility method used to get the installed path for Firefox or Gecko driver
	 * 
	 * @param device           Instance of {@link Dut}
	 * @param tapEnv           Instance of {@link AutomaticsTapApi}
	 * @param isPathForBrowser -True - Path for browser False- Path for Driver
	 * @return filePath File Path
	 * @refactor Govardhan
	 */
	public static String getFireFoxBrowserOrGeckoDriverInstalledFilePath(Dut device, AutomaticsTapApi tapEnv,
			boolean isPathForBrowser) {
		LOGGER.debug("STARTING METHOD: getFireFoxBrowserOrGeckoDriverInstalledFilePath()");
		String filePath = null;
		String command = null;
		try {
			command = BroadBandCommonUtils.concatStringUsingStringBuffer(
					BroadBandConnectedClientTestConstants.GET_INSTALLED_PATH,
					isPathForBrowser ? BroadBandConnectedClientTestConstants.BROWSER_NAME_FIREFOX
							: BroadBandConnectedClientTestConstants.DRIVER_NAME_GECKODRIVER);
			filePath = tapEnv.executeCommandOnOneIPClients(device, command).trim();
		} catch (Exception e) {
			LOGGER.error("Exception occured in getFireFoxBrowserOrGeckoDriverInstalledFilePath()" + e.getMessage());
		}
		if (CommonMethods.isNotNull(filePath)) {
			filePath = filePath.replace(
					isPathForBrowser ? BroadBandConnectedClientTestConstants.BROWSER_NAME_FIREFOX
							: BroadBandConnectedClientTestConstants.DRIVER_NAME_GECKODRIVER,
					BroadBandTestConstants.EMPTY_STRING);
		}
		LOGGER.info((isPathForBrowser ? "Browser path:" : "Driver path:") + filePath);
		LOGGER.debug("ENDING METHOD: getFireFoxBrowserOrGeckoDriverInstalledFilePath()");
		return filePath;
	}

	/**
	 * Utility method used to get the Firefox browser version
	 * 
	 * @param device   Instance of {@link Dut}
	 * @param tapEnv   Instance of {@link AutomaticsTapApi}
	 * @param filePath File path
	 * @return browserVersion- It will return the firefox browser version
	 * @refactor Govardhan
	 */
	public static int getFirefoxBrowserVersion(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: getFirefoxBrowserVersion()");
		int browserVersion = BroadBandTestConstants.CONSTANT_0;
		String response = null;
		for (int iteration = BroadBandTestConstants.CONSTANT_0; iteration < BroadBandTestConstants.CONSTANT_3; iteration++) {
			try {
				response = tapEnv.executeCommandOnOneIPClients(device,
						BroadBandConnectedClientTestConstants.CMD_TO_GET_THE_FIREFOX_VERSION);
				if (CommonMethods.isNotNull(response)) {
					browserVersion = BroadBandCommonUtils.convertStringToInteger(CommonMethods.patternFinder(response,
							BroadBandConnectedClientTestConstants.PATTERN_TO_GET_FIREFOX_VERSION));
				}
			} catch (Exception e) {
				LOGGER.error("Exception occured in getFirefoxBrowserVersion()" + e.getMessage());
			}
			if (browserVersion > BroadBandTestConstants.CONSTANT_0) {
				break;
			} else {
				BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
			}
		}
		LOGGER.info(" Browser Version: " + browserVersion);
		LOGGER.debug("ENDING METHOD: getFirefoxBrowserVersion()");
		return browserVersion;
	}

	/**
	 * Utility method used to verify the version check for firefox browser and gecko
	 * driver
	 * 
	 * @param browserVersion Browser version
	 * @param driverVersion  Driver version
	 * @return True- Expected versions are same, Else-False
	 * @refactor Govardhan
	 */
	public static boolean verifyExpectedBrowserAndDriverVersionsInEthernetClient(AutomaticsTapApi tapEnv,
			int browserVersion, int driverVersion) {
		LOGGER.debug("STARTING METHOD: verifyExpectedBrowserAndDriverVersionsInEthernetClient()");
		boolean status = false;
		int expectedDriverVersion = BroadBandTestConstants.CONSTANT_0;
		HashMap<String, String> hashMap = null;
		try {
			LOGGER.info("Actaul Driver Version: " + driverVersion + " Actual Browser Version: " + browserVersion);
			hashMap = getBrowserAndDriverVersionFromProperty(tapEnv);
			expectedDriverVersion = BroadBandCommonUtils
					.convertStringToInteger(CommonMethods.patternFinder(hashMap.get(String.valueOf(browserVersion)),
							BroadBandConnectedClientTestConstants.PATTERN_TO_GET_GECKO_DRIVER_VERSION_FROM_PROPERTY));
			LOGGER.info("From Property ==> Driver Version  : " + expectedDriverVersion);
			status = (driverVersion == expectedDriverVersion);
			LOGGER.info(status ? "Browser and driver versions are same"
					: "Browser and driver versions are not same/Add the expected browser and driver version in stb.properties");
		} catch (Exception e) {
			LOGGER.error(
					"Exception occured in verifyExpectedBrowserAndDriverVersionsInEthernetClient()" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: verifyExpectedBrowserAndDriverVersionsInEthernetClient()");
		return status;
	}

	/**
	 * Method to get the browser and driver version from property
	 * 
	 * @param device instance of {@link Dut}
	 * @param tapEnv AutomaticsTapApi instance
	 * @return Map value based on device model
	 * @refactor Govardhan
	 */
	public static HashMap<String, String> getBrowserAndDriverVersionFromProperty(AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: getBrowserAndDriverVersionFromProperty()");
		List<String> listOfBrowserAndDrivers = null;
		List<String> browserAndDriversSubList = null;
		String key = null;
		String value = null;
		String response = null;
		HashMap<String, String> hashMap = new HashMap<String, String>();
		try {
			response = AutomaticsPropertyUtility
					.getProperty(BroadBandConnectedClientTestConstants.ETHERNET_CLIENT_DRIVER_BROWSER_MAPPING);
			LOGGER.info("Mapping from property:" + response);
			listOfBrowserAndDrivers = Arrays.asList(response.split(BroadBandTestConstants.DELIMITER_HASH));
			for (String browserList : listOfBrowserAndDrivers) {
				browserAndDriversSubList = Arrays
						.asList(browserList.split(BroadBandTestConstants.PIPE_SYMBOL_WITH_ESCAPE_SEQUENCE));
				key = browserAndDriversSubList.get(BroadBandTestConstants.CONSTANT_0);
				value = browserAndDriversSubList.get(BroadBandTestConstants.CONSTANT_1);
				hashMap.put(key, value);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured while retrieving map values" + e.getMessage());
		}
		if (hashMap.size() == 0) {
			throw new TestException("Null response obtained while retrieving map values");
		}
		LOGGER.info("The map values are: " + hashMap);
		LOGGER.debug("ENDING METHOD: getBrowserAndDriverVersionFromProperty()");
		return hashMap;
	}

	/**
	 * Utility method used to download and move the gecko driver to configured
	 * location
	 * 
	 * @param device         instance of {@link Dut}
	 * @param tapEnv         instance of {@link AutomaticsTapApi}
	 * @param driverVersion  Driver version
	 * @param driverFilePath Selenium home location
	 * @return True-File Download successful,Else-False
	 * @refactor Govardhan
	 */
	public static boolean downloadAndMoveGeckoDriverToConfigLocation(Dut device, AutomaticsTapApi tapEnv,
			String driverVersion, String driverFilePath, String exeFileName, String presentWorkingDir) {
		LOGGER.debug("STARTING METHOD: downloadAndMoveGeckoDriverToConfigLocation()");
		boolean status = false;
		String url = null;
		String response = null;
		String zipFileName = null;
		String command = null;
		try {
			url = AutomaticsPropertyUtility
					.getProperty(BroadBandConnectedClientTestConstants.GECKO_DRIVER_DOWNLOAD_URL);
			url = url.replaceAll(BroadBandTestConstants.STRING_REPLACE, driverVersion);
			response = tapEnv.executeCommandOnOneIPClients(device, url);
			if (CommonMethods.isNotNull(response)
					&& CommonUtils.patternSearchFromTargetString(response,
							BroadBandConnectedClientTestConstants.RESPONSE_STATUS_OK)
					&& CommonMethods.patternMatcher(response,
							BroadBandConnectedClientTestConstants.PATTERN_FOR_GECKO_DRIVER_DOWNLAOD_STATUS)) {
				zipFileName = CommonMethods.patternFinder(response,
						BroadBandConnectedClientTestConstants.PATTERN_FOR_GECKO_DRIVER_FILENAME);
				LOGGER.info("Gecko driver downloaded successfully" + zipFileName);
				/** Unzip the downloaded files */
				command = BroadBandCommonUtils.concatStringUsingStringBuffer(
						BroadBandConnectedClientTestConstants.CMD_TAR, presentWorkingDir,
						BroadBandTestConstants.SLASH_SYMBOL, zipFileName);
				LOGGER.info("command for unzip:" + command);
				tapEnv.executeCommandOnOneIPClients(device, command);
				/** File presence after unzip */
				command = BroadBandCommonUtils.concatStringUsingStringBuffer(presentWorkingDir,
						BroadBandTestConstants.SLASH_SYMBOL, exeFileName);
				LOGGER.info("command for file presence:" + command);
				if (isFileExistsInConnectedClient(device, tapEnv, command, true)) {
					command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_CHMOD,
							BroadBandCommandConstants.CHMOD_777_VALUE, BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
							presentWorkingDir, BroadBandTestConstants.SLASH_SYMBOL, exeFileName);
					LOGGER.info("command for access rights:" + command);
					tapEnv.executeCommandOnOneIPClients(device, command);
					/** Remove the gecko driver in configured location */
					command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_SUDO,
							BroadBandCommandConstants.CMD_TO_REMOVE, BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
							driverFilePath, BroadBandConnectedClientTestConstants.DRIVER_NAME_GECKODRIVER);
					LOGGER.info("command for remove the exe:" + command);
					tapEnv.executeCommandOnOneIPClients(device, command);

					/** Remove zip file in downloded location */
					command = BroadBandCommonUtils.concatStringUsingStringBuffer(
							BroadBandCommandConstants.CMD_TO_REMOVE, BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
							presentWorkingDir, zipFileName);
					LOGGER.info("command for remove zip:" + command);
					tapEnv.executeCommandOnOneIPClients(device, command);

					/** move the file to configured location */
					command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_SUDO,
							BroadBandCommandConstants.CMD_COPY, presentWorkingDir, BroadBandTestConstants.SLASH_SYMBOL,
							exeFileName, BroadBandTestConstants.SINGLE_SPACE_CHARACTER, driverFilePath);
					LOGGER.info("command for move the file:" + command);
					tapEnv.executeCommandOnOneIPClients(device, command);
					command = BroadBandCommonUtils.concatStringUsingStringBuffer(driverFilePath, exeFileName);
					LOGGER.info("command for file presence after move:" + command);
					status = isFileExistsInConnectedClient(device, tapEnv, command, true);
				}
			} else {
				LOGGER.info("Failed to downlaod the gecko driver:" + driverVersion);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured in downloadAndMoveGeckoDriverToConfigLocation()" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: downloadAndMoveGeckoDriverToConfigLocation()");
		return status;
	}

	/**
	 * Utility method used to verify the browser and driver capability on connected
	 * client
	 * 
	 * @param device Instance of {@link Dut}
	 * @return True- Browser and Driver capability verification success, Else-False
	 * @refactor Govardhan
	 */
	public static boolean verifyBrowserAndDriverCapabilityOnConnectedClient(Dut device) {
		LOGGER.debug("STARTING METHOD: verifyBrowserAndDriverCapabilityOnWindowsClient()");
		boolean status = false;
		Device ecastSettop = (Device) device;
		AutomaticsTapApi tapEnv = null;
		String url = null;
		String response = null;
		String expectedDriverVersion = null;
		int browserVersion = BroadBandTestConstants.CONSTANT_0;
		int driverVersion = BroadBandTestConstants.CONSTANT_0;
		String userName = null;
		String seleniumHomeLocation = null;
		String outputFileName = null;
		boolean getVersionStatus = false;
		HashMap<String, String> hashMap = null;
		String exeFileName = null;
		String presentWorkingDir = null;
		String browserFilePath = null;
		String driverFilePath = null;
		try {
			tapEnv = AutomaticsTapApi.getInstance();
			userName = ecastSettop.getConnectedDeviceInfo().getUserName();
			LOGGER.info("USER NAME: " + userName);
			LOGGER.info("CLIENT TYPE: " + ecastSettop.getConnectedDeviceInfo().getConnectionType());
			if (ecastSettop.isWindows() && verifyUnzipSupportOnConnectedClient(device, tapEnv)) {
				url = AutomaticsPropertyUtility
						.getProperty(BroadBandConnectedClientTestConstants.CHROME_DRIVER_STORAGE_GOOGLE_APIS_URL);
				seleniumHomeLocation = AutomaticsPropertyUtility
						.getProperty(BroadBandConnectedClientTestConstants.SELENIUM_CONFIG_PATH_FOR_WINDOWS_OS);
				if (isFileExistsInConnectedClient(device, tapEnv, getChromeExeFileNameWithLocation(),
						BroadBandTestConstants.BOOLEAN_VALUE_TRUE)) {
					runVersionCheckVbsFile(device, tapEnv, seleniumHomeLocation);
					outputFileName = getVersionOutputFileNameWithLocation();
					if (CommonMethods.isNotNull(outputFileName) && isFileExistsInConnectedClient(device, tapEnv,
							outputFileName, BroadBandTestConstants.BOOLEAN_VALUE_TRUE)) {
						driverVersion = getChromeDriverVersion(device, tapEnv, outputFileName);
						browserVersion = getChromeBrowserVersion(device, tapEnv, outputFileName);
						getVersionStatus = true;
					} else {
						LOGGER.error("File not found in location: " + outputFileName);
					}
					if (getVersionStatus && !verifyBrowserAndDriverVersionsAreSame(browserVersion, driverVersion)) {
						if (ConnectedNattedClientsUtils.verifyConnectionStatusOnWiFiClient(device, tapEnv)) {
							tapEnv.executeCommandOnOneIPClients(device,
									BroadBandConnectedClientTestConstants.CHROME_DRIVER_KILL_CMD_FOR_WINDOWS);
							if (!verifyChromeDriverProcessRunningStatusOnWindowsClient(device, tapEnv)) {
								response = getDriverVersionInformation(url);
								if (CommonMethods.isNotNull(response)) {
									response = CommonMethods.patternFinder(response,
											getPatternForChromeDriverVersion(browserVersion));
									LOGGER.info("Latest release:" + response);
									expectedDriverVersion = getDriverVersionInformation(
											BroadBandCommonUtils.concatStringUsingStringBuffer(url, response));
									LOGGER.info("Expected Driver Version:" + expectedDriverVersion);
									if (CommonMethods.isNotNull(expectedDriverVersion)) {
										if (downloadChromeDriverAndMoveTheFileToConfiguredLocation(device, tapEnv,
												expectedDriverVersion, userName)) {
											startChromeDriver(device, tapEnv);
											runVersionCheckVbsFile(device, tapEnv, seleniumHomeLocation);
											outputFileName = getVersionOutputFileNameWithLocation();
											driverVersion = getChromeDriverVersion(device, tapEnv, outputFileName);
											browserVersion = getChromeBrowserVersion(device, tapEnv, outputFileName);
											status = verifyBrowserAndDriverVersionsAreSame(browserVersion,
													driverVersion);
										} else {
											LOGGER.error("Unable to downlad the expected driver version:"
													+ expectedDriverVersion);
										}
									} else {
										LOGGER.error("Unable to get the expected driver version from google api:"
												+ expectedDriverVersion);
									}
								} else {
									LOGGER.error("Expected Driver Version " + browserVersion + " is not available");
								}
							}
						}
					}
				} else {
					LOGGER.error("Default chrome driver is not available");
				}
			} else if (ecastSettop.isLinux()) {
				browserFilePath = getFireFoxBrowserOrGeckoDriverInstalledFilePath(ecastSettop, tapEnv,
						BroadBandTestConstants.BOOLEAN_VALUE_TRUE);
				driverFilePath = getFireFoxBrowserOrGeckoDriverInstalledFilePath(ecastSettop, tapEnv,
						BroadBandTestConstants.BOOLEAN_VALUE_FALSE);
				if (CommonMethods.isNotNull(browserFilePath) && CommonMethods.isNotNull(driverFilePath)) {
					presentWorkingDir = tapEnv
							.executeCommandOnOneIPClients(device, BroadBandConnectedClientTestConstants.CMD_TO_GET_PWD)
							.trim();
					LOGGER.info("presentWorkingDir:" + presentWorkingDir);
					exeFileName = BroadBandConnectedClientTestConstants.DRIVER_NAME_GECKODRIVER;
					LOGGER.info("Driver File Name:" + exeFileName);
					driverVersion = getGeckoDriverVersion(ecastSettop, tapEnv);
					browserVersion = getFirefoxBrowserVersion(ecastSettop, tapEnv);
					if (!verifyExpectedBrowserAndDriverVersionsInEthernetClient(tapEnv, browserVersion,
							driverVersion)) {
						hashMap = getBrowserAndDriverVersionFromProperty(tapEnv);
						if (downloadAndMoveGeckoDriverToConfigLocation(ecastSettop, tapEnv,
								hashMap.get(String.valueOf(browserVersion)), driverFilePath, exeFileName,
								presentWorkingDir)) {
							driverVersion = getGeckoDriverVersion(ecastSettop, tapEnv);
							browserVersion = getFirefoxBrowserVersion(ecastSettop, tapEnv);
							status = verifyExpectedBrowserAndDriverVersionsInEthernetClient(tapEnv, browserVersion,
									driverVersion);
						}
					}
				} else {
					LOGGER.error("Default gecko driver/firefox is not available");
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception occured in verifyBrowserAndDriverCapabilityOnWindowsClient()" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD: verifyBrowserAndDriverCapabilityOnWindowsClient()");
		return status;
	}

	public static void verifyIpstatusAndConnectivityInConnectedClient(Dut device, AutomaticsTapApi tapEnv,
			Dut connectedClient, String testCaseId, Integer testNumber, String connectionType) {
		String testStepNumber = null;
		boolean status = false;
		String errorMessage = null;

		boolean isSystemdPlatforms = false;

		LOGGER.info("Gateway device model is:" + isSystemdPlatforms);
		//
		testStepNumber = "s" + testNumber;
		status = false;
		errorMessage = "Interface  didnt get the correct IPV4 address";
		LOGGER.info("******************************************************************************");
		LOGGER.info("STEP " + testNumber + ":DESCRIPTION:VERIFY WHETHER THE " + connectionType
				+ " GOT THE IPV4 ADDRESS FROM DHCP RANGE.");
		LOGGER.info("STEP " + testNumber
				+ ":ACTION : EXECUTE ipconfig/ifconfig AND VALIDATE IPV4 ADDRESS WITH DEVICE DHCP RANGE");
		LOGGER.info("STEP " + testNumber + ":EXPECTED: IPV4 ADDRESS SHOULD  BE VALIDATED IN DHCP RANGE");
		LOGGER.info("******************************************************************************");
		status = BroadBandConnectedClientUtils.isConnClientIpv4AddrBtwnDhcpRange(tapEnv, device, connectedClient);
		if (status) {
			LOGGER.info("STEP " + testNumber + ":ACTUAL :Connected client has IPv4 address in DHCP range");
		} else {
			LOGGER.error("STEP " + testNumber + ":ACTUAL :" + errorMessage);
		}
		LOGGER.info("******************************************************************************");
		tapEnv.updateExecutionStatus(device, testCaseId, testStepNumber, status, errorMessage, false);

		testNumber++;
		testStepNumber = "s" + testNumber;
		status = false;
		errorMessage = "Interface didnt get the correct IPV6 address";
		LOGGER.info("******************************************************************************");
		LOGGER.info("STEP " + testNumber + " :DESCRIPTION: VERIFY WHETHER " + connectionType
				+ " OBTAINED THE IPV6 ADDRESS.");
		LOGGER.info("STEP " + testNumber
				+ " :ACTION : EXECUTE ipconfig/ifconfig AND VALIDATE IPV6 ADDRESS WITH DEVICE DHCP RANGE");
		LOGGER.info("STEP " + testNumber + " :EXPECTED:IPV6 ADDRESS SHOULD  BE VALIDATED");
		LOGGER.info("******************************************************************************");

		if (!isSystemdPlatforms) {
			String osType = ((Device) connectedClient).getOsType();
			status = BroadBandConnectedClientUtils.verifyIpv6AddressForWiFiOrLanInterfaceConnectedWithRdkbDevice(osType,
					connectedClient, tapEnv);
			if (status) {
				LOGGER.info("STEP " + testNumber + " ACTUAL :Connected client has IPv6 address validated successfully");
			} else {
				LOGGER.error("STEP " + testNumber + " ACTUAL :" + errorMessage);
			}
			LOGGER.info("******************************************************************************");
			tapEnv.updateExecutionStatus(device, testCaseId, testStepNumber, status, errorMessage, false);
		} else {
			LOGGER.info("******************************************************************************");
			tapEnv.updateExecutionForAllStatus(device, testCaseId, testStepNumber, ExecutionStatus.NOT_APPLICABLE,
					BroadBandTestConstants.FIBRE_NOT_APPLICABLE_IPV6, false);
		}

		testNumber++;
		testStepNumber = "s" + testNumber;
		status = false;
		LOGGER.info("******************************************************************************");
		LOGGER.info("STEP " + testNumber + ": DESCRIPTION :VERIFY CONNECTIVITY OF " + connectionType
				+ " USING IPV4 WITH CURL REQUEST ");
		LOGGER.info("STEP " + testNumber
				+ ": ACTION : EXECUTE curl --connect-timeout 20 --head -4 google.com SHOULD BE SUCCESSFUL");
		LOGGER.info("STEP " + testNumber + ": EXPECTED: CONNECTIVITY CHECK SHOULD RETURN STATUS AS 200");
		LOGGER.info("******************************************************************************");
		BroadBandResultObject broadBandResultObject = BroadBandConnectedClientUtils
				.verifyInternetIsAccessibleInConnectedClientUsingCurl(tapEnv, connectedClient,
						BroadBandTestConstants.URL_GOOGLE, BroadBandTestConstants.IP_VERSION4);
		status = broadBandResultObject.isStatus();
		errorMessage = broadBandResultObject.getErrorMessage();
		if (status) {
			LOGGER.info(
					"STEP " + testNumber + ": ACTUAL: Internet connectivity successful using ipv4 with Curl request");
		} else {
			LOGGER.error("STEP " + testNumber + ": ACTUAL: " + errorMessage);
		}
		LOGGER.info("******************************************************************************");
		tapEnv.updateExecutionStatus(device, testCaseId, testStepNumber, status, errorMessage, false);

		testNumber++;
		status = false;
		testStepNumber = "s" + testNumber;
		LOGGER.info("******************************************************************************");
		LOGGER.info("STEP " + testNumber + ": DESCRIPTION :VERIFY CONNECTIVITY OF " + connectionType
				+ " USING IPV6 WITH CURL REQUEST");
		LOGGER.info("STEP " + testNumber
				+ ": ACTION : EXECUTE curl --connect-timeout 20 --head -6 google.com SHOULD BE SUCCESSFUL");
		LOGGER.info("STEP " + testNumber + ": EXPECTED: CONNECTIVITY CHECK SHOULD RETURN STATUS AS 200");
		LOGGER.info("******************************************************************************");

		if (!isSystemdPlatforms) {
			broadBandResultObject = BroadBandConnectedClientUtils.verifyInternetIsAccessibleInConnectedClientUsingCurl(
					tapEnv, connectedClient, BroadBandTestConstants.URL_GOOGLE, BroadBandTestConstants.IP_VERSION6);
			status = broadBandResultObject.isStatus();
			errorMessage = broadBandResultObject.getErrorMessage();
			if (status) {
				LOGGER.info("STEP " + testNumber
						+ ": ACTUAL: Internet Connectivity successful using ipv6 with Curl request ");
			} else {
				LOGGER.error("STEP " + testNumber + ": ACTUAL: " + errorMessage);
			}
			LOGGER.info("******************************************************************************");
			tapEnv.updateExecutionStatus(device, testCaseId, testStepNumber, status, errorMessage, false);
		} else {
			tapEnv.updateExecutionForAllStatus(device, testCaseId, testStepNumber, ExecutionStatus.NOT_APPLICABLE,
					BroadBandTestConstants.FIBRE_NOT_APPLICABLE_IPV6, false);
		}
	}

	/**
	 * Method to retrieve Wan ip of the device and validate Lan client is in same
	 * broadcast
	 * 
	 * @param tapEnv
	 * @param device
	 * @param connectedClientSettop
	 * @return BroadBandResultObject
	 * 
	 * @refactor yamini.s
	 */
	public static BroadBandResultObject verifyLanClientWanIpAddressDeviceInBridgeMode(AutomaticsTapApi tapEnv,
			Dut device, Dut connectedClientSettop) {
		boolean status = false;
		String ipv4RetrievedFromClient = null;
		BroadBandResultObject result = new BroadBandResultObject();
		String errorMsg = null;
		LOGGER.debug("STARTING METHOD : verifyLanClientWanIpAddressDeviceInBridgeMode");
		try {
			Device ecastSettop = (Device) connectedClientSettop;
			if (ecastSettop.isRaspbianLinux()) {
				ipv4RetrievedFromClient = getIpv4AddressFromRaspbianConnClient(tapEnv, ecastSettop,
						connectedClientSettop);
			} else if (ecastSettop.isLinux()) {
				ipv4RetrievedFromClient = getIpv4AddressFromLinuxConnClient(tapEnv, ecastSettop, connectedClientSettop);
			} else if (ecastSettop.isWindows()) {
				ipv4RetrievedFromClient = getIpOrMacFromWindowsConnectedClient(device, connectedClientSettop, tapEnv,
						true);
			}
			if (CommonMethods.isNotNull(ipv4RetrievedFromClient)
					&& CommonMethods.isIpv4Address(ipv4RetrievedFromClient)) {
				String wanIpv4Address = tapEnv.executeWebPaCommand(device,
						BroadBandWebPaConstants.WEBPA_PARAM_WAN_IPV4);
				LOGGER.info("IP ADDRESS ASSIGNED TO THE CONNECTED CLIENT : " + ipv4RetrievedFromClient);
				if (CommonMethods.isNotNull(wanIpv4Address) && CommonMethods.isIpv4Address(wanIpv4Address)
						&& CommonMethods.isNotNull(ipv4RetrievedFromClient)
						&& CommonMethods.isIpv4Address(ipv4RetrievedFromClient)) {
					if (CommonMethods
							.patternFinder(ipv4RetrievedFromClient,
									BroadBandTestConstants.PATTERN_TO_RETRIEVE_FIRST_2_DIGITS_OF_IPv4_ADDRESS)
							.equalsIgnoreCase(CommonMethods.patternFinder(wanIpv4Address,
									BroadBandTestConstants.PATTERN_TO_RETRIEVE_FIRST_2_DIGITS_OF_IPv4_ADDRESS))) {
						LOGGER.info("GOING TO VERIFY IF THE OBTAINED WAN IP ADDRESS ");
						status = true;
					} else {
						errorMsg = "WanIp Address obtained from webpa and LAN Client are not same";
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(
					"EXCEPTION OCCURRED WHILE VERIFYING WAN IPv4 ASSIGNNED TO THE CLIENT CONNECTED VIA ETHERNET . ERROR : "
							+ e.getMessage());
		}

		LOGGER.info("IS IP ADDRESS ASSIGNED TO THE CONNECTED CLIENT IS IN BROADCAST ADDRESS OF DEVICE : " + result);
		LOGGER.debug("ENDING METHOD verifyLanClientWanIpAddressDeviceInBridgeMode");
		result.setErrorMessage(errorMsg);
		result.setStatus(status);
		return result;
	}

	/**
	 * Utils method to validated clients connectivity by checking for valid IP
	 * Addresses and internet connectivity with 5 mins polling Time.
	 * 
	 * @param device                instance of {@link Dut}
	 * @param connectedClientDevice client device object
	 * @param tapEnv                instance of {@link AutomaticsTapApi}
	 * @param stepNumber            Step number
	 * @param testCaseId            Test case ID
	 * @return
	 */
	public static List<String> validateIpAddressesAndInternetConnectivityOfConnectedClient(Dut device,
			Dut connectedClient, AutomaticsTapApi tapEnv, int stepNumber, String testCaseId) {

		return validateIpAddressesAndInternetConnectivityOfConnectedClient(device, connectedClient, tapEnv, stepNumber,
				testCaseId, false);
	}

	/**
	 * Utils method to validated clients connectivity by checking for valid IP
	 * Addresses and internet connectivity with 5 mins polling Time.
	 * 
	 * @param device                instance of {@link Dut}
	 * @param connectedClientDevice client device object
	 * @param tapEnv                instance of {@link AutomaticsTapApi}
	 * @param stepNumber            Step number
	 * @param testCaseId            Test case ID
	 * @param isBridgeMode          Flag value for bridge mode
	 * @return
	 * @refactor Said Hisham
	 */
	public static List<String> validateIpAddressesAndInternetConnectivityOfConnectedClient(Dut device,
			Dut connectedClientDevice, AutomaticsTapApi tapEnv, int stepNumber, String testCaseId,
			boolean isBridgeMode) {
		boolean status = false;
		String stepNum = "S" + stepNumber;
		String errorMessage = null;
		String ipv4AddressRetrievedFromClient = null;
		String ipv6AddressRetrievedFromClient = null;
		List<String> ipAddresses = new ArrayList<>();
		long startTime = 0;
		boolean ipv4Status = false;
		boolean isSystemdPlatforms = false;
		isSystemdPlatforms = DeviceModeHandler.isFibreDevice(device);
		LOGGER.info("Gateway device model is:" + isSystemdPlatforms);
		try {

			errorMessage = "Connected Client doesn't have  a valid  IPv4 Address between DHCP Range";
			LOGGER.info("**********************************************************************************");
			LOGGER.info("STEP " + stepNumber + ": DESCRIPTION : Verify the Connected client has got the IPv4 Address ");
			LOGGER.info("STEP " + stepNumber
					+ ": ACTION : Get the device IPv4 address using below commandLinux : ifconfig wlan0\\eth0 |grep -i \"inet addr:\"Windows: ipconfig |grep -A 10 \"Wireless\\Ethernet LAN adapter Wi-Fi\" |grep -i \"IPv4 Address\"");
			LOGGER.info("STEP " + stepNumber
					+ ": EXPECTED : Connected Client should be assigned with the IPv4 Address between DHCP Range");
			LOGGER.info("**********************************************************************************");

			startTime = System.currentTimeMillis();
			do {
				LOGGER.info("Waiting for device to verify IPV4 Address status");

				ipv4AddressRetrievedFromClient = getIpv4AddressFromConnClient(tapEnv, device, connectedClientDevice);
				status = CommonMethods.isIpv4Address(ipv4AddressRetrievedFromClient)
						&& verifyIpv4AddressOFConnectedClientIsBetweenDhcpRangeForAnySubnetValue(tapEnv, device,
								connectedClientDevice).isStatus();
				;
			} while (!status && (System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
			if (status) {
				ipv4Status = true;
				ipAddresses.add(ipv4AddressRetrievedFromClient);
				LOGGER.info("STEP " + stepNumber
						+ " ACTUAL: Connected Client is assigned with a valid IPv4 Address DHCP Range");
			} else {
				LOGGER.error("STEP " + stepNumber + " ACTUAL : " + errorMessage);
			}
			tapEnv.updateExecutionStatus(device, testCaseId, stepNum, status, errorMessage, false);
			LOGGER.info("**********************************************************************************");

			stepNumber++;
			stepNum = "S" + stepNumber;
			errorMessage = "Connected Client haven't got a valid IPV6 Address from the gateway";
			status = false;
			LOGGER.info("**********************************************************************************");
			LOGGER.info("STEP " + stepNumber
					+ ": DESCRIPTION : Verify the Connected client has got the valid IPv6 Address");
			LOGGER.info("STEP " + stepNumber
					+ ": ACTION : Get the device IPv6 address using below command Linux : ifconfig wlan0\\eth0 |grep -i \"inet addr6:\"Windows: ipconfig |grep -A 10 \"Wireless\\Ethernet LAN adapter Wi-Fi\" |grep -i \"IPv6 Address\"");
			LOGGER.info("STEP " + stepNumber
					+ ": EXPECTED : Local IPv6 Address assigned to the client should be retrieved successfully");
			LOGGER.info("**********************************************************************************");
			if (!isSystemdPlatforms) {
				startTime = System.currentTimeMillis();
				do {
					LOGGER.info("Waiting for device to verify IPV6 Address status");
					ipv6AddressRetrievedFromClient = BroadBandConnectedClientUtils
							.retrieveIPv6AddressFromConnectedClientWithDeviceCOnnected(connectedClientDevice, tapEnv);
					status = CommonMethods.isIpv6Address(ipv6AddressRetrievedFromClient);
				} while (!status
						&& (System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS
						&& BroadBandCommonUtils.hasWaitForDuration(tapEnv,
								BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
				if (!status && DeviceModeHandler.isDSLDevice(device)) {
					status = BroadBandConnectedClientUtils.validateIPv6AddressCompleteCheck(
							((Device) connectedClientDevice).getOsType(), device, connectedClientDevice, tapEnv);
				}
				if (status) {
					ipAddresses.add(ipv6AddressRetrievedFromClient);
					LOGGER.info("STEP " + stepNumber
							+ " ACTUAL: Local IPv6 Address assigned to the client is valid Address");
				} else {
					LOGGER.error("STEP " + stepNumber + " ACTUAL: " + errorMessage);
				}
				tapEnv.updateExecutionStatus(device, testCaseId, stepNum, status, errorMessage, false);
			} else {
				tapEnv.updateExecutionForAllStatus(device, testCaseId, stepNum, ExecutionStatus.NOT_APPLICABLE,
						BroadBandTestConstants.FIBRE_NOT_APPLICABLE_IPV6, false);
			}
			LOGGER.info("**********************************************************************************");

			stepNumber++;
			stepNum = "S" + stepNumber;
			if (ipv4Status) {
				errorMessage = "Unable to check Internet access in the Connected client";
				status = false;
				LOGGER.info("**********************************************************************************");
				LOGGER.info("STEP " + stepNumber
						+ ": DESCRIPTION : Verify the internet is accessible in the Connected client");
				LOGGER.info("STEP " + stepNumber
						+ ": ACTION : Execute the command in connected client: curl --connect-timeout 20 --head https://www.instagram.com");
				LOGGER.info(
						"STEP " + stepNumber + ": EXPECTED : Internet should be accessible in the connected client.");
				LOGGER.info("**********************************************************************************");
				startTime = System.currentTimeMillis();
				do {
					BroadBandResultObject result = BroadBandConnectedClientUtils
							.verifyInternetIsAccessibleInConnectedClientUsingCurl(tapEnv, connectedClientDevice,
									BroadBandTestConstants.PING_TO_GOOGLE, BroadBandTestConstants.EMPTY_STRING);
					status = result.isStatus();
					errorMessage = result.getErrorMessage();
					if (!status) {
						errorMessage = "PING OPERATION FAILED TO ACCESS THE SITE 'www.google.com' USING IPV4 ";
						status = ConnectedNattedClientsUtils.verifyPingConnectionForIpv4AndIpv6(connectedClientDevice,
								tapEnv, BroadBandTestConstants.PING_TO_GOOGLE, BroadBandTestConstants.IP_VERSION4);
					}
				} while (!status
						&& (System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS
						&& BroadBandCommonUtils.hasWaitForDuration(tapEnv,
								BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));

				if (status) {
					LOGGER.info("STEP " + stepNumber + " ACTUAL: Internet is accessible in the Connected client");
				} else {
					LOGGER.error("STEP " + stepNumber + " ACTUAL: " + errorMessage);
				}
				tapEnv.updateExecutionStatus(device, testCaseId, stepNum, status, errorMessage, false);
				LOGGER.info("**********************************************************************************");
			} else {
				LOGGER.info("STEP " + stepNumber + " NT AS IP ADDRESS VALIDATION FAILED");
				tapEnv.updateExecutionForAllStatus(device, testCaseId, stepNum, ExecutionStatus.NOT_TESTED,
						errorMessage, false);
			}
		} catch (Exception e) {
			errorMessage = errorMessage + e.getMessage();
			throw new TestException(errorMessage);
		}

		return ipAddresses;
	}

	/**
	 * Utils method to verify if the IP address assigned to the connected client is
	 * between DHCP configured range
	 * 
	 * @param tapEnv                instance of {@link AutomaticsTapApi}
	 * @param device                instance of {@link Dut}
	 * @param connectedClientSettop Connected client device instance
	 * 
	 * @return true if the IP address to the connected client is between configured
	 *         DHCP range
	 * @refactor Said Hisham
	 */
	public static BroadBandResultObject verifyIpv4AddressOFConnectedClientIsBetweenDhcpRangeForAnySubnetValue(
			AutomaticsTapApi tapEnv, Dut device, Dut connectedClientSettop) {
		LOGGER.debug("ENTERING METHOD verifyIpv4AddressOFConnectedClientIsBetweenDhcpRangeForAnySubnetValue");
		boolean result = false;
		BroadBandResultObject resultObject = new BroadBandResultObject();
		String errorMessage = "IPV4 address retrieved from the client is not between the DHCP range.";
		try {

			String dhcpMinRange = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_TO_RETRIEVE_DHCP_STARTING_IP_ADDRESS);
			LOGGER.info("DHCP MINIMUM IP RANGE CONFIGURED FOR GATEWAY : " + dhcpMinRange);
			String dhcpMaxRange = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_TO_RETRIEVE_DHCP_ENDING_IP_ADDRESS);
			LOGGER.info("DHCP MAXIMUM IP RANGE CONFIGURED FOR GATEWAY : " + dhcpMaxRange);
			String ipAddressRetrievedFromClient = getIpv4AddressFromConnClient(tapEnv, device, connectedClientSettop);
			LOGGER.info("IP ADDRESS ASSIGNED TO THE CONNECTED CLIENT FROM DHCP : " + ipAddressRetrievedFromClient);

			if (CommonMethods.isIpv4Address(dhcpMinRange) && CommonMethods.isIpv4Address(dhcpMaxRange)
					&& CommonMethods.isIpv4Address(ipAddressRetrievedFromClient)) {
				result = validateDhcpIpv4AddressBetweenRangeInConnectedClient(dhcpMinRange, dhcpMaxRange,
						ipAddressRetrievedFromClient);
			}

		} catch (Exception e) {
			LOGGER.error("EXCEPTION OCCURRED WHILE VERIFYING IPv4 ASSIGNNED TO CONNECTED CLIENT IS WITHIN DHCP RANGE : "
					+ e.getMessage());
		}

		LOGGER.info("IS IP ADDRESS ASSIGNED TO THE CONNECTED CLIENT BETWEEN DHCP RANGE : " + result);
		LOGGER.debug("ENDING METHOD verifyIpv4AddressOFConnectedClientIsBetweenDhcpRangeForAnySubnetValue");
		resultObject.setErrorMessage(errorMessage);
		resultObject.setStatus(result);
		return resultObject;

	}

	/**
	 * Method to disconnect and reconnect same WiFi client
	 * 
	 * @param WiFiConnectionType WiFi Connection Band(2.4GHz or 5GHz)
	 * @param tapEnv             AutomaticsTapApi instance
	 * @param device             Dut instance
	 * @param connectedClient    Connected client instance
	 * @return result which contains result of disconnecting and reconnecting WiFi
	 *         Client
	 * @refactor Said Hisham
	 */
	public static BroadBandResultObject disconnectAndReconnectWiFiClient(String WiFiConnectionType,
			AutomaticsTapApi tapEnv, Dut device, Dut connectedClient) {
		LOGGER.debug("STARTING METHOD: disconnectAndReconnectWiFiClient()");
		// Variable declaration starts
		BroadBandResultObject result = null;
		// Variable declaration ends

		try {
			WiFiFrequencyBand wiFiFrequencyBand = WiFiConnectionType.equals(BroadBandTestConstants.BAND_2_4GHZ)
					? WiFiFrequencyBand.WIFI_BAND_2_GHZ
					: WiFiFrequencyBand.WIFI_BAND_5_GHZ;
			result = BroadBandConnectedClientUtils.disconnectCnnClientFromSsid(tapEnv, device, connectedClient);
			if (result.isStatus()) {
				LOGGER.info("Client disconnected successfully from WiFi.");
				LOGGER.info("Waiting for 1 min");
				tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
				LOGGER.info("Reconecting same client to WiFi.");
				result = BroadBandConnectedClientUtils.connectGivenConnectedClientToWifi(device, tapEnv,
						connectedClient, wiFiFrequencyBand);
				if (result.isStatus()) {
					LOGGER.info("Device reconnected successfully");
					LOGGER.info("Waiting for 1 min");
					tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
				} else {
					result.setErrorMessage("Unable to reconnect same WiFi Client.");
				}
			} else {
				result.setErrorMessage("Unable to disconnect WiFi Client.");
			}
		} catch (Exception e) {
			LOGGER.info("Exception occured while disconnecting and reconnecting client from WiFi.");
		}
		LOGGER.debug("ENDING METHOD: disconnectAndReconnectWiFiClient()");
		return result;
	}

	/**
	 * Method verify DHCP IP6 Address of connected client after IP renew
	 * 
	 * @param device                instance of {@link Dut}
	 * @param tapEnv                instance of {@link AutomaticsTapApi}
	 * @param connectedClientSettop Connected client device instance
	 * @return status true if expected IP range is available
	 * @refactor Said Hisham
	 */
	public static boolean verifyConnectedClientIpv6AddressInDhcpAfterRenew(Dut device, AutomaticsTapApi tapEnv,
			Dut connectedClientSettop) {
		LOGGER.debug("STARTING METHOD: verifyConnectedClientIpv6AddressInDhcpAfterRenew");
		boolean result = false;
		Device ecastSettop = (Device) connectedClientSettop;
		result = verifyIpv6AddressOFConnectedClientIsBetweenDhcpRange(tapEnv, device, connectedClientSettop);
		if (result) {
			// Returning true as value is already equal to value to be set
			return true;
		} else {
			String osType = ((Device) connectedClientSettop).getOsType();
			if (osType.equals(BroadBandConnectedClientTestConstants.OS_WINDOWS)) {
				tapEnv.executeCommandOnOneIPClients(connectedClientSettop,
						BroadBandCommandConstants.CMD_TO_RENEW_IP_IN_WINDOWS);
			} else {
				String defaultInterface = getDefaultInterfaceNameOfTheLinuxConnClientDevice(connectedClientSettop,
						tapEnv).trim();
				LOGGER.info("DEFAULT INTERFACE OBTAINED IN THE LINUX CLIENT IS  : " + defaultInterface);
				String connectionType = ecastSettop.getConnectedDeviceInfo().getConnectionType();
				LOGGER.info("CONNECTION TYPE OF THE CONNECTED CLIENT IS : " + connectionType);
				String[] expectedInterfaces = null;
				if (CommonMethods.isNotNull(defaultInterface) && CommonMethods.isNotNull(connectionType)
						&& BroadBandWebGuiTestConstant.CONNECTION_TYPE_ETHERNET.equalsIgnoreCase(connectionType)) {
					LOGGER.info("GOING TO GET IPV4 ADDRESS FROM ETHERNET INTERFACE");
					expectedInterfaces = AutomaticsTapApi
							.getSTBPropsValue(
									BroadBandTestConstants.PROP_KEY_TO_GET_EXPECTED_ETHERNET_INTERFACE_IN_LINUX_CLIENT)
							.split(BroadBandTestConstants.SEMI_COLON);
				} else if (CommonMethods.isNotNull(defaultInterface)
						&& BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI
								.equalsIgnoreCase(connectionType)) {
					LOGGER.info("GOING TO GET IPV4 ADDRESS FROM WI-FI INTERFACE");
					expectedInterfaces = AutomaticsTapApi
							.getSTBPropsValue(
									BroadBandTestConstants.PROP_KEY_TO_GET_EXPECTED_WIFI_INTERFACE_IN_LINUX_CLIENT)
							.split(BroadBandTestConstants.SEMI_COLON);
				} else {
					String errorMessage = CommonMethods.isNotNull(defaultInterface)
							? "'getIpv4AddressFromRaspbianConnClient' METHOD IS ONLY APPLICABLE FOR ETHERNET/WI-FI CONNECTION TYPE"
							: "UNABLE TO OBTAIN THE DEFAULT INTERFACE NAME FROM LINUX CLIENT";
					LOGGER.error(errorMessage);
				}
				if (expectedInterfaces != null && expectedInterfaces.length != 0) {
					for (String interfaceName : expectedInterfaces) {
						if (CommonUtils.patternSearchFromTargetString(defaultInterface, interfaceName)) {
							LOGGER.info("Renew IP in connected client: " + interfaceName);
							tapEnv.executeCommandOnOneIPClients(connectedClientSettop,
									BroadBandCommandConstants.CMD_TO_RENEW_IP_CLIENT_INTERFACE_IN_LINUX
											.replace("<<INTERFACE>>", interfaceName));
							tapEnv.waitTill(BroadBandTestConstants.TWO_MINUTE_IN_MILLIS);
							break;
						}
					}
				}
			}
			result = verifyIpv6AddressOFConnectedClientIsBetweenDhcpRange(tapEnv, device, connectedClientSettop);
		}
		LOGGER.debug("ENDING METHOD : verifyConnectedClientIpv6AddressInDhcpAfterRenew");
		return result;
	}

	/**
	 * Utils method to verify if the IP address assigned to the connected client is
	 * between DHCP V6 configured range
	 * 
	 * @param tapEnv                instance of {@link AutomaticsTapApi}
	 * @param device                instance of {@link Dut}
	 * @param connectedClientSettop Connected client device instance
	 * 
	 * @return true if the IP address to the connected client is between configured
	 *         DHCP IPv6 range
	 * @refactor Said Hisham
	 */
	public static boolean verifyIpv6AddressOFConnectedClientIsBetweenDhcpRange(AutomaticsTapApi tapEnv, Dut device,
			Dut connectedClientSettop) {
		LOGGER.debug("STARTING METHOD verifyIpv6AddressOFConnectedClientIsBetweenDhcpRange");
		boolean result = false;
		try {
			String osType = ((Device) connectedClientSettop).getOsType();
			String delegatedPrefix = tapEnv.executeWebPaCommand(device,
					BroadBandWebPaConstants.WEB_PARAM_DELEGATED_PREFIX_IPV6);
			delegatedPrefix = delegatedPrefix.replaceAll(":/64", " ").trim();
			LOGGER.info("DHCP DELIGATED PREFIX FOR GATEWAY : " + delegatedPrefix);
			String dhcpBeginRange = tapEnv.executeWebPaCommand(device,
					BroadBandWebPaConstants.WEB_PARAM_DHCPV6_BEGINNING_ADDRESS);
			LOGGER.info("DHCP BEGIN IP RANGE CONFIGURED FOR GATEWAY : " + dhcpBeginRange);
			String dhcpEndingRange = tapEnv.executeWebPaCommand(device,
					BroadBandWebPaConstants.WEB_PARAM_DHCPV6_ENDING_ADDRESS);
			LOGGER.info("DHCP ENDING IP RANGE CONFIGURED FOR GATEWAY : " + dhcpEndingRange);
			dhcpBeginRange = delegatedPrefix + dhcpBeginRange;
			dhcpEndingRange = delegatedPrefix + dhcpEndingRange;
			List<String> ipAddressRetrievedFromClient = getListIpv6AddressFromConnClient(tapEnv, osType,
					connectedClientSettop);
			String[] arrayofBeginAddress = dhcpBeginRange.split(BroadBandTestConstants.DELIMITER_COLON);
			String[] arrayofEndAddress = dhcpEndingRange.split(BroadBandTestConstants.DELIMITER_COLON);

			if (!ipAddressRetrievedFromClient.isEmpty()) {
				for (int listCount = 0; listCount < ipAddressRetrievedFromClient.size(); listCount++) {
					LOGGER.info("IP ADDRESS ASSIGNED TO THE CONNECTED CLIENT FROM DHCP : "
							+ ipAddressRetrievedFromClient.get(listCount));
					String[] arrayofGivenAddress = ipAddressRetrievedFromClient.get(listCount)
							.split(BroadBandTestConstants.DELIMITER_COLON);
					if (CommonMethods.isIpv6Address(dhcpBeginRange) && CommonMethods.isIpv6Address(dhcpEndingRange)
							&& CommonMethods.isIpv6Address(ipAddressRetrievedFromClient.get(listCount))) {
						for (int count = 0; count < arrayofBeginAddress.length; count++) {
							int minRange = Integer.parseInt(arrayofBeginAddress[count],
									BroadBandTestConstants.CONSTANT_16);
							int maxRange = Integer.parseInt(arrayofEndAddress[count],
									BroadBandTestConstants.CONSTANT_16);
							int actualvalue = Integer.parseInt(arrayofGivenAddress[count],
									BroadBandTestConstants.CONSTANT_16);
							result = (actualvalue >= minRange && actualvalue <= maxRange);
							if (!result)
								break;
						}
					}
					if (result) {
						LOGGER.info("IPv6 ADDRESS RETRIEVED IS IN DHCP RANGE ");
						break;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("EXCEPTION OCCURRED WHILE VERIFYING IPv6 ASSIGNNED TO CONNECTED CLIENT IS WITHIN DHCP RANGE : "
					+ e.getMessage());
		}
		LOGGER.info("IS IP ADDRESS ASSIGNED TO THE CONNECTED CLIENT BETWEEN DHCP RANGE : " + result);
		LOGGER.debug("ENDING METHOD : verifyIpv6AddressOFConnectedClientIsBetweenDhcpRange");
		return result;
	}

	/**
	 * Helper method to get the list of ipv6 address of the connected client
	 * 
	 * @param tapEnv                instance of {@link AutomaticsTapApi}
	 * @param osType                Os type of connected client
	 * @param connectedClientSettop Connected client device instance
	 * @return List of Ipv6 address from connected client device
	 * @refactor Said Hisham
	 */
	public static List<String> getListIpv6AddressFromConnClient(AutomaticsTapApi tapEnv, String osType,
			Dut connectedClientSettop) {
		LOGGER.debug("STARTING METHOD: getListIpv6AddressFromConnClient");
		List<String> ipv6Value = null;
		String commandResponse = null;
		String defaultInterface = null;
		try {
			if (BroadBandConnectedClientTestConstants.OS_LINUX.equalsIgnoreCase(osType)) {
				defaultInterface = getSpecificDefaultInterfaceNameOfTheLinux(connectedClientSettop, tapEnv);
			}
			String command = BroadBandConnectedClientTestConstants.OS_LINUX.equalsIgnoreCase(osType)
					? BroadBandConnectedClientTestConstants.LINUX_COMMAND_CHECK_IPV6_ADDRESS
							.replaceAll(BroadBandTestConstants.STRING_REPLACE, defaultInterface.trim())
					: BroadBandConnectedClientTestConstants.WINDOWS_COMMAND_CHECK_IPV6_ADDRESS;
			commandResponse = tapEnv.executeCommandOnOneIPClients(connectedClientSettop, command);

			if (CommonMethods.isNotNull(commandResponse)) {
				ipv6Value = BroadBandConnectedClientTestConstants.OS_LINUX.equalsIgnoreCase(osType)
						? CommonMethods.patternFinderToReturnAllMatchedString(commandResponse,
								BroadBandConnectedClientTestConstants.PATTERN_LINUX_VALID_IPV6_ADDRESS)
						: CommonMethods.patternFinderToReturnAllMatchedString(commandResponse,
								BroadBandConnectedClientTestConstants.PATTERN_WINDOWS_VALID_IPV6_ADDRESS);
			}
		} catch (Exception e) {
			LOGGER.error("Exception occurred while getting IPv6 address : " + ipv6Value);
		}
		LOGGER.info("IP DHCP V6 : " + ipv6Value);
		LOGGER.debug("ENDING METHOD: getListIpv6AddressFromConnClient");
		return ipv6Value;
	}

	/**
	 * Check IPV6 address in the ethernet Interface of the device
	 * 
	 * @param device - instance of {@link Dut}
	 * @param tapEnv - instance of {@link AutomaticsTapApi}
	 * @return result object
	 * @refactor Said Hisham
	 */
	public static BroadBandResultObject verifyIpv6AddressOfEthInterfaceConnectedWithBroadbandDevice(Dut device,
			AutomaticsTapApi tapEnv) {
		String command = null;
		String response = null;
		String lanIpv6Address = null;
		// List to store the ipv6 address
		List<String> ipAddress = new ArrayList<>();
		String errorMessage = null;
		String patternForIpv6 = null;
		BroadBandResultObject result = new BroadBandResultObject();
		errorMessage = "unable to get the IPv6 address from the Ethernet interface";
		if (((Device) device).getOsType().equalsIgnoreCase(BroadBandConnectedClientTestConstants.OS_RASPBIAN_LINUX)
				|| ((Device) device).getOsType().equalsIgnoreCase(BroadBandConnectedClientTestConstants.OS_LINUX)) {
			lanIpv6Address = getIpv6AddressFromLinuxOrRaspbianConnClient(tapEnv, device);
		} else if (((Device) device).getOsType().equalsIgnoreCase(BroadBandConnectedClientTestConstants.OS_WINDOWS)) {
			// command for windows client
			command = BroadBandCommandConstants.COMMAND_IPCONFIG;
			// pattern for windows client
			patternForIpv6 = BroadBandTestConstants.PATTERN_TO_RETRIVE_IPV6_ADDRESS_FROM_IPCONFIG;
			response = tapEnv.executeCommandOnOneIPClients(device, command);
			ipAddress = BroadBandCommonUtils.patternFinderForMultipleMatches(response, patternForIpv6,
					BroadBandTestConstants.CONSTANT_1);
			for (String ipv6Addr : ipAddress) {
				if (CommonMethods.isIpv6Address(ipv6Addr)) {
					lanIpv6Address = ipv6Addr;
					break;
				}
			}
		}
		result.setStatus(CommonMethods.isNotNull(lanIpv6Address));
		result.setErrorMessage(errorMessage);
		return result;
	}

	/**
	 * Method used to execute the command to renew the ip in connected client
	 * 
	 * @param deviceConnected Connected client Dut instance
	 * @param tapEnv          AutomaticsTapApi instance
	 * 
	 * @refactor Athira
	 */
	public static void dhcpRenewInConnectedClient(Dut deviceConnected, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD : dhcpRenewInConnectedClient()");
		try {
			Device connDevice = (Device) deviceConnected;
			String osType = connDevice.getOsType();
			if (osType.equals(BroadBandConnectedClientTestConstants.OS_WINDOWS)) {
				tapEnv.executeCommandOnOneIPClients(deviceConnected,
						BroadBandCommandConstants.CMD_TO_RENEW_IP_IN_WINDOWS);
			} else {
				String interfaceName = getSpecificDefaultInterfaceNameOfTheLinux(deviceConnected, tapEnv);
				tapEnv.executeCommandOnOneIPClients(deviceConnected,
						BroadBandCommonUtils.concatStringUsingStringBuffer(
								BroadBandTestConstants.COMMAND_TO_GET_IP_CONFIGURATION_DETAILS,
								BroadBandTestConstants.SINGLE_SPACE_CHARACTER, interfaceName));
			}
		} catch (Exception e) {
			LOGGER.error("Exception Occured in dhcpRenewInConnectedClient():" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : dhcpRenewInConnectedClient()");
	}

}

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.RDKBTestConstants.WiFiFrequencyBand;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;
import com.automatics.rdkb.utils.BroadBandBandSteeringUtils;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.BroadBandSystemUtils;
import com.automatics.rdkb.utils.CommonUtils;
import com.automatics.rdkb.utils.ConnectedNattedClientsUtils;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
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
	 * @param device      instance of {@link Dut}
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
				if (status && (!DeviceModeHandler.isFibreDevice(device))) {
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
					command = new String[] { BroadBandConnectedClientTestConstants.LINUX_COMMAND_TO_GET_WLAN_NETWORK };
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
     * Method to set private ssid names to standard values following pattern "RKDB-<MAC address>-<Band>"
     * 
     * 
     * @param device
     *            instance of {@link Dut}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @return status of setting the parameter values true if both are set to value false even if one is not set
     * 
     * @author rvella613
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
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param ssidName
     *            SSID name
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
     * Method to verify whether the client device is connected and to get its ip address
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

	    if (!isSystemdPlatforms) {

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
	    status = BroadBandConnectedClientUtils.verifyIpv6AddressForWiFiOrLanInterfaceConnectedWithRdkbDevice(osType,
		    connectedDeviceActivated, tapEnv);
	    if (status) {
		LOGGER.info("STEP " + stepNumbers[1]
			+ " : ACTUAL : Successfully verified interface got the correct IPv6  address.");
	    } else {
		LOGGER.error("STEP " + stepNumbers[1] + " : ACTUAL : " + errorMessage);
	    }
	    tapEnv.updateExecutionStatus(device, testId, testStepNumber, status, errorMessage, false);
	} else {
	    LOGGER.info("This function is meant for executing 2 steps.Current steps passed are " + stepNumbers.length);
	}
    }

    
    /**
     * Method to get a 2.4 GHz or 5 GHz Wi-Fi connected client and connect with respective Wi-Fi network
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
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
     * @param settop
     *            Settop Instance
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param clientSettop
     *            Connected client instance
     * @param wifiBand
     *            Wifi Frequency band
     * @return Time
     * @refactor Athira
     */
    public static long connectToGivenWiFiCapableClientForPerfTest(Dut device, AutomaticsTapApi tapEnv,
	    Dut clientSettop, String wifiBand) {
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
     * Method to get the SSID Security mode using Device.WiFi.AccessPoint.{i}.Security.X_COMCAST-COM_KeyPassphrase
     * 
     * @param device
     *            instance of {@link Dut}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param WiFiFrequencyBand
     *            frequency band
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
     * @param connectedClient
     *            Connected client Settop instance
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
     * Utils method to verify if the IP address assigned to the connected client is between DHCP configured range
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link Dut}
     * @param connectedClientDevice
     *            Connected client device instance
     * 
     * @return true if the IP address to the connected client is between configured DHCP range
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
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link Dut}
     * @param connectedClientDevice
     *            Connected client device instance
     * @return Ipv4 of the connected client device
     * 
     * @author BALAJI V, INFOSYS
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
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link Dut}
     * @param connectedClientDevice
     *            Connected client device instance
     * @return Ipv4 address of the linux connected client device
     * 
     * @author BALAJI V, INFOSYS
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
     * Helper method to get Connected client device ip or mac from the same windows device
     * 
     * @param connectedClientDevice
     *            Connected client device instance
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param isIpNeeded
     *            true, if ip of the device is needed, false for mac
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
			command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.COMMAND_ARP,
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
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link Dut}
     * @param connectedClientDevice
     *            Connected client device instance
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


}


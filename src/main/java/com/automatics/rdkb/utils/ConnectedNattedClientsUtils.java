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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.automatics.device.Device;
import com.automatics.device.Dut;
import com.automatics.providers.connection.SshConnection;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandConnectedClientTestConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.rdkb.utils.wifi.BroadBandWiFiUtils;
import com.automatics.rdkb.utils.wifi.BroadBandWifiWhixUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ConnectedNattedClientsUtils {

    /** SLF4j logger instance. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectedNattedClientsUtils.class);

    /** Connect WIFI connection in Linux. */
    private static final String CONNECT_LINUX = "nmcli d wifi connect <ssid> password <password> iface wlan0";

    /** Connect WIFI connection in Linux success message. */
    private static final String CONNECT_LINUX_SUCCESS_MESSAGE = "Device 'wlan0' successfully activated";

    /** Connect to SSID in Windows. */
    private static final String CONNECT_PROFILE_WINDOWS = "netsh wlan connect name=\"<ssid>\"";

    /** Connect WIFI message in Windows. */
    private static final String CONNECT_WINDOWS_SUCCESS_MESSAGE = "Connection request was completed successfully";

    /** Verify WIFI connection in Linux. */
    private static final String VERIFY_CONNECT_LINUX = "nmcli | grep connected |grep -i wlan0 ";

    /** Verify WIFI connection in Linux success message. */
    private static final String VERIFY_CONNECT_LINUX_MESSAGE = "connected to";

    /** Verify WIFI connection in Windows. */
    private static final String VERIFY_CONNECT_WINDOWS = "ipconfig |grep -A 10 \"Wireless LAN adapter Wi-Fi\" |grep -i \"IPv4 Address\"";

    /** check WIFI connection status in Linux . */
    private static final String CONNECT_STATUS_COMMAND_LINUX = "nmcli device status |grep -i <ssid> |awk '{print $5}' | tr -d \"\n\"";

    /** Disconnect WIFI connection in Linux success message. */
    private static final String DISCONNECT_LINUX_SUCCESS_MESSAGE = "successfully deactivated";

    /** Diconnect WIFI in Windows. */
    private static final String DISCONNECT_WINDOWS = "netsh wlan disconnect";

    /** Diconnect WIFI connection in Linux. */
    private static final String DISCONNECT_LINUX = "nmcli con down id \"<ssid>\"";

    /** Diconnect WIFI message in Windows. */
    private static final String DISCONNECT_WINDOWS_SUCCESS_MESSAGE = "Disconnection request was completed successfully for interface \"Wi-Fi\"";

    /** Show WIFI profile in Windows. */
    private static final String SHOW_AUTO_DISCOVERED_NETWROKS_WINDOWS = "netsh wlan show networks |grep -i \"<ssid>\"";

    /** Remove all WIFI profile in Windows. */
    private static final String DELETE_ALL_EXISTING_PROFILES_WINDOWS = "netsh wlan delete profile name=*";

    private static final String MSG_PROFILE_DELETION_SUCCESS_WINDOWS = "is deleted from interface";

    private static final String MSG_NO_PROFILE_FOUND_WINDOWS = "Profile \"*\" is not found on any interface";

    /** SECURITY MODE */
    public static final String SECURITY_MODE_OPEN = "open";
    public static final String SECURITY_MODE_WPA2PSK = "WPA2";

    /** windows command to get the wifi security mode */
    public static final String WINDOWS_COMMAND_TO_GET_WIFI_SECURITY_MODE = "netsh wlan show networks | awk '/<ssid>/,/Encryption/' | grep Authentication";

    /** Add WIFI profile in Windows. */
    private static final String ADD_PROFILE_WINDOWS = "netsh wlan add profile filename=<profile>";

    private static final String ADD_PROFILE_WINDOWS_SUCCESS_MESSAGE = "is added on interface Wi-Fi";

    /** Boolean for Connect Automatically */
    public static boolean WIFI_CONNECT_IN_AUTO = false;

    /** WIFI PROFILE TEMPLATE */
    private static final String XML_FILENAME_WIFI_PROFILE_TEMPLATE = "wifi_profile_template.xml";

    /** XML NODES to be identifed for profile creation **/
    public static final String NODE_NAME = "name";
    public static final String NODE_SSIDCONFIG = "SSIDConfig";
    public static final String NODE_HEX = "hex";
    public static final String NODE_NONBROADCAST = "nonBroadcast";
    public static final String NODE_MSM = "MSM";
    public static final String NODE_KEYMATERIAL = "keyMaterial";
    public static final String NODE_AUTHENTICATION = "authentication";
    public static final String NODE_ENCRYPTION = "encryption";
    public static final String NODE_TYPE = "text";
    public static final String NODE_CONNECTION_MODE = "connectionMode";

    /** NO ENCRYPTION SPEC for Open security mode */
    public static final String ENCRYPTION_NONE = "none";

    /**
     * Wi-Fi connection status - CONNECTED.
     */
    private static final String WIFI_INTERFACE_CONNECTION_STATUS = "connected";

    /**
     * Maximum number of retry to check WIFI state - Connected
     */
    private static final int MAX_NUMBER_OF_RETRY_WIFI_CONNECTED_STATE = 10;

    /**
     * Windows command to get the current Wi-Fi connection state.
     */
    private static final String CMD_WINDOWS_WIFI_INTERFACE_CONNECTION_STATUS = "netsh wlan show interface | grep -i \"state\"";

    /**
     * The pattern used for retrieving connection status.
     */
    private static final String PATTERN_WIFI_INTERFACE_CONNECTION_STATUS = "State\\s*:\\s*(\\w+)";

    /**
     * 
     * This method is to verify ping command execution on the WiFi Client Device.
     * 
     * @param wifiClientDevice
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param ipAddress
     *            String representing the IP Address.
     * @return returns status of operation
     * @refactor Govardhan
     */
    public static boolean verifyPingConnectionForIpv4AndIpv6(Dut ClientDevice, AutomaticsTapApi tapEnv,
	    String ipAddress, String resolveTo) {
	LOGGER.debug("STARTING METHOD : verifyPingConnection()");
	boolean result = false;
	String pingCommand = null;
	Device ecastSettop = (Device) ClientDevice;
	LOGGER.info("OS TYPE OF THE WIFI CLIENT: " + ecastSettop.getOsType());
	if (resolveTo.equalsIgnoreCase(BroadBandTestConstants.IP_VERSION6)) {
	    pingCommand = ecastSettop.isLinux() || ecastSettop.isMacOS() ? BroadBandCommandConstants.CMD_PING_LINUX_IPV6
		    : BroadBandCommandConstants.CMD_PING_WINDOWS_IPV6;
	} else if (resolveTo.equalsIgnoreCase(BroadBandTestConstants.IP_VERSION4)) {
	    pingCommand = ecastSettop.isLinux() || ecastSettop.isMacOS() ? BroadBandCommandConstants.CMD_PING_LINUX
		    : BroadBandCommandConstants.CMD_PING_WINDOWS;
	}
	LOGGER.info("Ping Command: " + pingCommand);
	String response = tapEnv.executeCommandOnOneIPClients(ClientDevice,
		BroadBandCommonUtils.concatStringUsingStringBuffer(pingCommand, ipAddress));
	if (CommonMethods.isNotNull(response) && !validatePingResponse(response)) {
	    response = ecastSettop.isLinux() || ecastSettop.isMacOS()
		    ? BroadBandCommonUtils.patternFinderMatchPositionBased(response,
			    ecastSettop.isLinux()
				    ? BroadBandConnectedClientTestConstants.PATTERN_MATCHER_PING_RESPONSE_LINUX
				    : BroadBandConnectedClientTestConstants.PATTERN_MATCHER_PING_RESPONSE_MAC,
			    BroadBandTestConstants.CONSTANT_3)
		    : BroadBandCommonUtils.patternFinderMatchPositionBased(response,
			    BroadBandConnectedClientTestConstants.PATTERN_MATCHER_PING_RESPONSE_WINDOWS,
			    BroadBandTestConstants.CONSTANT_4);
	    if (CommonMethods.isNotNull(response)) {
		try {
		    int iActualValue = Integer.parseInt(response);
		    result = iActualValue != BroadBandTestConstants.PERCENTAGE_VALUE_HUNDRED;
		} catch (NumberFormatException numberFormatException) {
		    // Log & Suppress the Exception
		    LOGGER.error(numberFormatException.getMessage());
		}
	    }
	}
	LOGGER.debug("ENDING METHOD : verifyPingConnection()");
	return result;
    }

    /**
     * 
     * This method used validate the ping response
     * 
     * @param response
     *            ping response
     *
     * @return isValid It returns true or false
     * @refactor Govardhan
     */
    public static boolean validatePingResponse(String response) {
	LOGGER.debug("STARTING METHOD : validatePingResponse()");
	boolean isNotReachable = false;
	isNotReachable = CommonUtils.patternSearchFromTargetString(response,
		BroadBandTestConstants.PING_ERR_MSG_TTL_EXPIRED_IN_TRANSIT)
		|| CommonUtils.patternSearchFromTargetString(response,
			BroadBandTestConstants.PING_ERR_MSG_DEST_HOST_UNREACHABLE)
		|| CommonUtils.patternSearchFromTargetString(response,
			BroadBandTestConstants.PING_ERR_MSG_REQUEST_TIMED_OUT)
		|| CommonUtils.patternSearchFromTargetString(response, BroadBandTestConstants.PING_ERR_MSG_UNKNOWN_HOST)
		|| CommonUtils.patternSearchFromTargetString(response,
			BroadBandTestConstants.PING_ERR_MSG_COULD_NOT_FIND_HOST)
		|| CommonUtils.patternSearchFromTargetString(response,
			BroadBandTestConstants.PING_ERR_MSG_REQ_TIME_OUT_FOR_ICMP_SEQ);
	LOGGER.info("validate Ping Response is :" + isNotReachable);
	LOGGER.debug("ENDING METHOD : validatePingResponse()");
	return isNotReachable;
    }

    /**
     * 
     * This method executes commands on provided device , based on its OS type and model , to connect to given SSID
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param ssid
     *            SSID to connect to
     * @param password
     *            Password for connecting to SSID
     * @param isSsidHidden
     *            Is SSID to be connected is Hidden.
     * @return returns status of operation
     */
    public static boolean connectToSSID(Dut device, AutomaticsTapApi tapEnv, String ssid, String password,
	    boolean isSsidHidden) {
	boolean retrunStatus = false;
	Device ecastSettop = (Device) device;
	if (ecastSettop.isLinux()) {
	    LOGGER.info("[TEST LOG] : isLinux ");
	    retrunStatus = connectToLinux(device, tapEnv, ssid, password);
	} else if (ecastSettop.isWindows()) {
	    LOGGER.info("[TEST LOG] : isWindows ");
	    retrunStatus = connectToWindows(ecastSettop, tapEnv, ssid, password, null, isSsidHidden);
	} else if (ecastSettop.isRaspbianLinux()) {
	    LOGGER.info("[TEST LOG] : isRaspbianLinux ");
	} else if (ecastSettop.isMacOS()) {
	    LOGGER.info("[TEST LOG] : isMacOS ");
	    retrunStatus = connectToMacOS(ecastSettop, tapEnv, ssid, password, null);
	}
	return retrunStatus;
    }

    /**
     * 
     * This method connects the LINUX device to wifi by specifying the ssid and password
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param ssid
     *            SSID to connect to
     * @param password
     *            Password for connecting to SSID
     * @return status of connection
     */
    private static boolean connectToLinux(Dut device, AutomaticsTapApi tapEnv, String ssid, String password) {
	boolean retrunStatus = false;
	String command = CONNECT_LINUX.replaceAll("<ssid>", ssid).replaceAll("<password>", password);
	String response = tapEnv.executeCommandOnOneIPClients(device, command);
	if (CommonMethods.isNotNull(response) && response.contains(CONNECT_LINUX_SUCCESS_MESSAGE)) {
	    retrunStatus = true;
	}
	return retrunStatus;
    }

    /**
     * 
     * This method connects the WINDOWS device to wifi by specifying the ssid and password
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param ssid
     *            SSID to connect to
     * @param password
     *            Password for connecting to SSID
     * @param securityMode
     *            Security mode with which connection has to be established.
     * @param isSsidHidden
     *            true, if ssid to connect to hidden
     * @return status of connection
     * @refactor Govardhan
     */
    private static boolean connectToWindows(Dut device, AutomaticsTapApi tapEnv, String ssid, String password,
	    String securityMode, boolean isSsidHidden) {
	boolean retrunStatus = false;
	boolean proceedToConnect = false;
	if (BroadBandWifiWhixUtils.restartWifiDriver(device, tapEnv) && isProfileAvailable(device, tapEnv, ssid)) {
	    LOGGER.info("[TEST LOG] : Creating profile with ssid & password....");
	    proceedToConnect = addWifiProfileAndConnect(device, tapEnv, ssid, password, securityMode, isSsidHidden);
	}
	if (proceedToConnect) {
	    String command = CONNECT_PROFILE_WINDOWS.replaceAll("<ssid>", ssid);
	    String response = tapEnv.executeCommandOnOneIPClients(device, command);
	    if (CommonMethods.isNotNull(response) && response.contains(CONNECT_WINDOWS_SUCCESS_MESSAGE)) {
		// Adding loop to make sure that device is connected to SSID before exiting from
		// this method.
		for (int index = 0; index < MAX_NUMBER_OF_RETRY_WIFI_CONNECTED_STATE; index++) {
		    response = tapEnv.executeCommandOnOneIPClients(device,
			    CMD_WINDOWS_WIFI_INTERFACE_CONNECTION_STATUS);
		    String connectionString = CommonMethods.patternFinder(response,
			    PATTERN_WIFI_INTERFACE_CONNECTION_STATUS);
		    retrunStatus = CommonMethods.isNotNull(connectionString)
			    && WIFI_INTERFACE_CONNECTION_STATUS.equalsIgnoreCase(connectionString.trim());
		    if (retrunStatus) {
			break;
		    } else {
			LOGGER.info("Waiting for TWO seconds for connection status - Connected.");
			tapEnv.waitTill(BroadBandTestConstants.TWO_SECOND_IN_MILLIS);
		    }
		}
	    }
	}
	return retrunStatus;
    }

    /**
     * This method connects the MAC device to wifi by specifying the ssid
     * 
     * @param device
     *            Dut object
     * @param tapEnv
     *            Tap environment
     * @param ssid
     *            SSID to connect
     * @param password
     *            Password to connect
     * @param securityMode
     *            SecurityMode to connect
     * 
     * @return status of connection
     * @refactor Govardhan
     */
    public static boolean connectToMacOS(Dut device, AutomaticsTapApi tapEnv, String ssid, String password,
	    String securityMode) {
	LOGGER.debug("STARTING METHOD : connectToMacOS()");
	boolean connectedStatus = false;
	boolean isProfileAdded = false;
	String interfaceNameMacOS = getWifiInterfaceNameOnMacOS(device, tapEnv);
	String mode = CommonMethods.isNull(securityMode) ? SECURITY_MODE_WPA2PSK : securityMode;
	if (CommonMethods.isNotNull(interfaceNameMacOS)
		&& !isWifiProfileExistOnMacOS(device, tapEnv, interfaceNameMacOS, ssid)) {
	    isProfileAdded = addWifiProfileOnMacOS(device, tapEnv, interfaceNameMacOS, ssid, password, mode);
	} else {
	    if (removeExistingWifiProfileOnMacOS(device, tapEnv, interfaceNameMacOS, ssid)) {
		isProfileAdded = addWifiProfileOnMacOS(device, tapEnv, interfaceNameMacOS, ssid, password, mode);
	    }
	}
	if (isProfileAdded) {
	    connectedStatus = connectSsidWifiOnMacOS(device, tapEnv, interfaceNameMacOS, ssid, password, mode);
	}
	LOGGER.debug("ENDING METHOD : connectToMacOS()");
	return connectedStatus;
    }

    /**
     * This Method checks if a profile is availabe on the given SSID.By default Profile name is SSID value.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param ssid
     *            SSID to connect to
     * @return Returns status after checking if profile is available
     * @refactor Govardhan
     */
    private static boolean isProfileAvailable(Dut device, AutomaticsTapApi tapEnv, String ssid) {
	boolean status = false;
	String response = null;
	// String command = SHOW_PROFILE_WINDOWS.replaceAll("<ssid>", ssid);
	String command = SHOW_AUTO_DISCOVERED_NETWROKS_WINDOWS.replaceAll("<ssid>", ssid);

	response = tapEnv.executeCommandOnOneIPClients(device, command);
	if (CommonMethods.isNotNull(response)) {
	    if (CommonUtils.patternSearchFromTargetString(response, ssid)) {
		removeAllExistingProfiles(device, tapEnv);
		status = true;
	    } else {
		LOGGER.error("The given SSID is not discovered on NUC {} .Hence unable to connect to the given SSID",
			device.getHostMacAddress());
	    }
	} else {
	    LOGGER.error(
		    "No response obtained.The given SSID is not discovered on NUC {} .Hence unable to connect to the given SSID",
		    device.getHostMacAddress());
	}
	return status;
    }

    /**
     * 
     * For windows machines, wifi profile need to be added ,inorder to connect to wifi network. This method adds wifi
     * profile
     * 
     * @param device
     *            Dut object
     * @param tapEnv
     *            Tap environment
     * @param ssid
     *            SSID to connect to
     * @param password
     *            Password for connecting to SSID
     * @param isSsidHidden
     *            true, if ssid to connect to hidden
     * @return returns status of operation
     */
    private static boolean addWifiProfileAndConnect(Dut device, AutomaticsTapApi tapEnv, String ssid, String password,
	    String securityMode, boolean isSsidHidden) {
	LOGGER.debug("STARTING METHOD : addWifiProfileAndConnect()");
	String hostMacAddress = null;
	String profileNameToAdd = null;
	String profileFileNameToCreate = null;
	boolean returnStatus = false;
	String profileAvailable = null;
	String path = null;
	String completeProfilePath = null;
	String securityModeToVerify = null;
	boolean isFileCopied = false;
	try {

	    hostMacAddress = device.getHostMacAddress();
	    hostMacAddress = hostMacAddress.replaceAll(":|\\.", "");
	    LOGGER.info("[TEST LOG] : hostMacAddress : " + hostMacAddress);
	    if (CommonMethods.isNotNull(hostMacAddress)) {
		hostMacAddress = hostMacAddress.substring(hostMacAddress.length() - BroadBandTestConstants.CONSTANT_4);
	    } else {
		// incase Client Mac address not configured in CATS inventory.Adding client host
		// Mac as WiFi in profile
		// xml
		hostMacAddress = BroadBandTestConstants.WIFI;
	    }
	    LOGGER.info("[TEST LOG] 2: hostMacAddress : " + hostMacAddress);
	    profileNameToAdd = "." + hostMacAddress + "_" + ssid.replaceAll(":|\\.", "") + ".xml";
	    LOGGER.info("[TEST LOG] : profileNameToAdd : " + profileNameToAdd);
	    profileFileNameToCreate = profileNameToAdd;
	    path = Paths.get(".").toAbsolutePath().normalize().toString() + File.separator + profileFileNameToCreate;
	    LOGGER.info("[TEST LOG] : path : " + path);
	    securityModeToVerify = CommonMethods.isNotNull(securityMode) ? securityMode : SECURITY_MODE_WPA2PSK;
	    LOGGER.info("[TEST LOG] : securityModeToVerify : " + securityModeToVerify);
	    LOGGER.info("[TEST LOG] : isSsidHidden : " + isSsidHidden);
	    if (isSsidHidden) {
		LOGGER.info("[TEST LOG] : isSsidHidden is true ");
		// If the Connected client has already been connected with hidden ssid, the ssid
		// will be visible for the
		// device
		isSsidHidden = !verifyExpectedSecurityModeInConnectedClient(device, tapEnv, securityModeToVerify, ssid);
	    }
	    completeProfilePath = URLDecoder.decode(path, "UTF-8");
	    LOGGER.info("[TEST LOG] : Creating new profile in server : " + completeProfilePath);
	    boolean resp = createNewProfileFromTemplate(ssid, password, completeProfilePath, securityMode,
		    isSsidHidden);
	    LOGGER.info("[TEST LOG] : resp: " + resp);
	    if (createNewProfileFromTemplate(ssid, password, completeProfilePath, securityMode, isSsidHidden)) {
		LOGGER.info("[TEST LOG] : Copying new profile to device : " + completeProfilePath);
		long startTime = System.currentTimeMillis();
		do {
		    try {
			if (copyFileFromJumpServerToClient(device, completeProfilePath)
				|| copyfileToClient(completeProfilePath, device)) {
			    profileAvailable = tapEnv.executeCommandOnOneIPClients(device,
				    BroadBandCommonUtils.concatStringUsingStringBuffer(
					    BroadBandCommandConstants.CMD_TO_SHOW_HIDDEN_FILES,
					    BroadBandTestConstants.SINGLE_SPACE_CHARACTER, profileNameToAdd));
			    isFileCopied = CommonMethods.isNotNull(profileAvailable)
				    && CommonUtils.patternSearchFromTargetString(profileAvailable, profileNameToAdd);
			}
		    } catch (Exception e) {
			LOGGER.info("Exception while copying new profile to cleint device : " + e.getMessage());
		    }
		    LOGGER.info("Is wifi profile xml moved to client : " + isFileCopied);
		} while (!isFileCopied
			&& (System.currentTimeMillis() - startTime) < BroadBandTestConstants.THREE_MINUTE_IN_MILLIS
			&& BroadBandCommonUtils.hasWaitForDuration(tapEnv,
				BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
		if (isFileCopied) {
		    // To validate whether the copied new profile is available in the client
		    LOGGER.info("[TEST LOG] : Adding wifi profile for ssid " + ssid);
		    String command = ADD_PROFILE_WINDOWS.replaceAll("<profile>", profileNameToAdd);
		    String response = tapEnv.executeCommandOnOneIPClients(device, command);
		    returnStatus = CommonMethods.isNotNull(response)
			    && response.contains(ADD_PROFILE_WINDOWS_SUCCESS_MESSAGE);
		    LOGGER.info("[TEST LOG] : Wifi profile  added successfully for ssid " + ssid);
		} else {
		    LOGGER.error("Profile is not available after copying to the client");
		}
	    }
	    File file = new File(completeProfilePath);
	    if (file.delete()) {
		LOGGER.info("Removed profile created locally in server");
	    }
	    returnStatus = returnStatus && (isSsidHidden ? true
		    : verifyExpectedSecurityModeInConnectedClient(device, tapEnv, securityModeToVerify, ssid));
	} catch (UnsupportedEncodingException e) {
	    LOGGER.error("Unable to create " + profileNameToAdd + " : " + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : addWifiProfileAndConnect()");
	return returnStatus;
    }

    /**
     * 
     * This method used to get the WiFi interface name from MAC client
     * 
     * @param device
     *            Dut object
     * @param tapEnv
     *            Tap environment
     * 
     * @return wifiInterface wifi interface name
     * @refactor Govardhan
     */
    public static String getWifiInterfaceNameOnMacOS(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug("STARTING METHOD : getWifiInterfaceNameOnMacOS()");
	String wifiInterface = null;
	String command = BroadBandConnectedClientTestConstants.CMD_MAC_OS_WIFI_NETWORK_INTERFACE_NAME.replace(
		BroadBandConnectedClientTestConstants.MAC_OS_WIFI_PARAM_INTERFACE_NAME, BroadBandTestConstants.WIFI);
	String response = tapEnv.executeCommandOnOneIPClients(device, command);
	if (CommonMethods.isNotNull(response)) {
	    wifiInterface = CommonMethods
		    .patternFinder(response, BroadBandTraceConstants.LOG_MESSAGE_MAC_OS_WIFI_INTERFACE_NAME).trim();
	}
	LOGGER.info("WiFi Interface Name: " + wifiInterface);
	LOGGER.debug("ENDING METHOD : getWifiInterfaceNameOnMacOS()");
	return wifiInterface;
    }

    /**
     * This method used to verify profile is exist in MAC Client
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param interfaceName
     *            Wifi interface name
     * @param ssid
     *            SSID to connect
     * 
     * @return isAvailable It returns true or false
     * @refactor Govardhan
     */
    public static boolean isWifiProfileExistOnMacOS(Dut device, AutomaticsTapApi tapEnv, String interfaceName,
	    String ssid) {
	LOGGER.debug("STARTING METHOD : isWifiProfileExistOnMacOS()");
	boolean isAvailable = false;
	String command = BroadBandConnectedClientTestConstants.CMD_MAC_OS_VERIFY_SSID_IN_PREFERRED_NWLIST
		.replace(BroadBandConnectedClientTestConstants.MAC_OS_WIFI_PARAM_INTERFACE_NAME, interfaceName)
		.replace(BroadBandConnectedClientTestConstants.MAC_OS_WIFI_PARAM_SSID, ssid);
	String response = tapEnv.executeCommandOnOneIPClients(device, command);
	isAvailable = CommonMethods.isNotNull(response);
	LOGGER.info(isAvailable ? ssid + " Profile Available" : ssid + " Profile Not Available");
	LOGGER.debug("ENDING METHOD : isWifiProfileExistOnMacOS()");
	return isAvailable;
    }

    /**
     * This method used to add the profile in MAC Client
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param interfaceName
     *            Wifi interface name
     * @param ssid
     *            SSID to connect
     * @param securityMode
     *            Security Mode
     * 
     * @return isAdded It returns true or false
     * @refactor Govardhan
     */
    public static boolean addWifiProfileOnMacOS(Dut device, AutomaticsTapApi tapEnv, String interfaceName, String ssid,
	    String passWord, String securityMode) {
	LOGGER.debug("STARTING METHOD : addWifiProfileOnMacOS()");
	boolean isAdded = false;
	String command = BroadBandConnectedClientTestConstants.CMD_MAC_OS_ADD_SSID_PREFERRED_NWLIST
		.replace(BroadBandConnectedClientTestConstants.MAC_OS_WIFI_PARAM_INTERFACE_NAME, interfaceName)
		.replace(BroadBandConnectedClientTestConstants.MAC_OS_WIFI_PARAM_SSID, ssid)
		.replace(BroadBandConnectedClientTestConstants.MAC_OS_WIFI_PARAM_SECURITY_MODE, securityMode);
	if (securityMode.equalsIgnoreCase(SECURITY_MODE_OPEN)) {
	    command = command.replace(BroadBandConnectedClientTestConstants.MAC_OS_WIFI_PARAM_PASSWORD, "").trim();
	} else {
	    command = command.replace(BroadBandConnectedClientTestConstants.MAC_OS_WIFI_PARAM_PASSWORD, passWord)
		    .trim();
	}
	String response = tapEnv.executeCommandOnOneIPClients(device, command);
	isAdded = CommonMethods.isNotNull(response) && CommonMethods.isNotNull(CommonMethods.patternFinder(response,
		BroadBandTraceConstants.LOG_MESSAGE_MAC_OS_WIFI_SSID_ADDED_IN_PROFILE));
	LOGGER.info("Profile Added :" + isAdded);
	LOGGER.debug("ENDING METHOD : addWifiProfileOnMacOS()");
	return isAdded;
    }

    /**
     * This method used to remove the profile in MAC Client
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param interfaceName
     *            Wifi interface name
     * @param ssid
     *            SSID to connect
     * 
     * @return isRemoved It returns true or false
     * @refactor Govardhan
     */
    public static boolean removeExistingWifiProfileOnMacOS(Dut device, AutomaticsTapApi tapEnv, String interfaceName,
	    String ssid) {
	LOGGER.debug("STARTING METHOD : removeExistingWifiProfileOnMacOS()");
	boolean isRemoved = false;
	String command = BroadBandConnectedClientTestConstants.CMD_MAC_OS_REMOVE_SSID_PREFERRED_NWLIST
		.replace(BroadBandConnectedClientTestConstants.MAC_OS_WIFI_PARAM_INTERFACE_NAME, interfaceName)
		.replace(BroadBandConnectedClientTestConstants.MAC_OS_WIFI_PARAM_SSID, ssid);
	String response = tapEnv.executeCommandOnOneIPClients(device, command);
	isRemoved = CommonMethods.isNotNull(response) && CommonMethods.isNotNull(CommonMethods.patternFinder(response,
		BroadBandTraceConstants.LOG_MESSAGE_MAC_OS_WIFI_SSID_REMOVED_IN_PROFILE));
	LOGGER.info("Profile Removed :" + isRemoved);
	LOGGER.debug("ENDING METHOD : removeExistingWifiProfileOnMacOS()");
	return isRemoved;
    }

    /**
     * This method used to connect the ssid in MAC Client
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param interfaceName
     *            Wifi interface name
     * @param ssid
     *            SSID to connect
     * 
     * @return isConnected It returns true or false
     * @refactor Govardhan
     */
    public static boolean connectSsidWifiOnMacOS(Dut device, AutomaticsTapApi tapEnv, String interfaceName, String ssid,
	    String passWord, String securityMode) {
	LOGGER.debug("STARTING METHOD : connectXfinityWifiOnMacOS()");
	boolean isConnected = false;
	if (disConnectSsidOnMacOS(device, tapEnv, interfaceName)) {
	    String command = BroadBandConnectedClientTestConstants.CMD_MAC_OS_CONNECT_SSID
		    .replace(BroadBandConnectedClientTestConstants.MAC_OS_WIFI_PARAM_INTERFACE_NAME, interfaceName)
		    .replace(BroadBandConnectedClientTestConstants.MAC_OS_WIFI_PARAM_SSID, ssid);
	    if (securityMode.equalsIgnoreCase(SECURITY_MODE_OPEN)) {
		command = command.replace(BroadBandConnectedClientTestConstants.MAC_OS_WIFI_PARAM_PASSWORD, "").trim();
	    } else {
		command = command.replace(BroadBandConnectedClientTestConstants.MAC_OS_WIFI_PARAM_PASSWORD, passWord)
			.trim();
	    }
	    tapEnv.executeCommandOnOneIPClients(device, command);
	    String connectedSsid = getConnectedWifiSsidOnMacOS(device, tapEnv, interfaceName);
	    isConnected = CommonMethods.isNotNull(connectedSsid)
		    && CommonUtils.patternSearchFromTargetString(ssid, connectedSsid);
	}
	LOGGER.debug("ENDING METHOD : connectXfinityWifiOnMacOS()");
	return isConnected;
    }

    /**
     * This method used to disconnect the wifi in MAC Client
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param interfaceName
     *            Wifi interface name
     * 
     * @return isDisConnected It returns true or false
     * @refactor Govardhan
     */
    public static boolean disConnectSsidOnMacOS(Dut device, AutomaticsTapApi tapEnv, String interfaceName) {
	LOGGER.debug("STARTING METHOD : disConnectSsidOnMacOS()");
	boolean isDisConnected = false;
	if (CommonMethods.isNull(tapEnv.executeCommandOnOneIPClients(device,
		BroadBandConnectedClientTestConstants.CMD_MAC_OS_DISCONNECT_SSID))) {
	    String response = tapEnv.executeCommandOnOneIPClients(device,
		    BroadBandConnectedClientTestConstants.CMD_MAC_OS_CONNECTED_SSID.replace(
			    BroadBandConnectedClientTestConstants.MAC_OS_WIFI_PARAM_INTERFACE_NAME, interfaceName));
	    isDisConnected = CommonMethods.isNotNull(response) && CommonUtils.patternSearchFromTargetString(response,
		    BroadBandTraceConstants.LOG_MESSAGE_MAC_OS_SSID_DISCONNECTED);
	}
	LOGGER.info("SSID Disonnected to MAC  :" + isDisConnected);
	LOGGER.debug("ENDING METHOD : disConnectSsidOnMacOS()");
	return isDisConnected;
    }

    /**
     * This method used to connect the ssid in MAC Client
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param interfaceName
     *            Wifi interface name
     * 
     * @return String It returns true or false
     * @refactor Govardhan
     */
    public static String getConnectedWifiSsidOnMacOS(Dut device, AutomaticsTapApi tapEnv, String interfaceName) {
	LOGGER.debug("STARTING METHOD : getConnectedWifiSsidOnMacOS()");
	String connectedSsid = null;
	String response = tapEnv.executeCommandOnOneIPClients(device,
		BroadBandConnectedClientTestConstants.CMD_MAC_OS_CONNECTED_SSID.replace(
			BroadBandConnectedClientTestConstants.MAC_OS_WIFI_PARAM_INTERFACE_NAME, interfaceName));
	LOGGER.info("GetConnectedWifiSsidOnMacOS Response :" + response);
	if (CommonMethods.isNotNull(response) && !CommonUtils.patternSearchFromTargetString(response,
		BroadBandTraceConstants.LOG_MESSAGE_MAC_OS_SSID_DISCONNECTED)) {
	    connectedSsid = CommonMethods.patternFinder(response,
		    BroadBandTraceConstants.LOG_MESSAGE_MAC_OS_SSID_CONNECTED);
	}
	LOGGER.info("SSID Connected to MAC  :" + connectedSsid);
	LOGGER.debug("ENDING METHOD : getConnectedWifiSsidOnMacOS()");
	return connectedSsid;
    }

    /**
     * Method to remove existing profiles in NUCs. This is done to prevent autoconnect to any existing profiles
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     */
    private static void removeAllExistingProfiles(Dut device, AutomaticsTapApi tapEnv) {
	String response = tapEnv.executeCommandOnOneIPClients(device, DELETE_ALL_EXISTING_PROFILES_WINDOWS);
	if (CommonMethods.isNotNull(response) && (response.contains(MSG_NO_PROFILE_FOUND_WINDOWS)
		|| response.contains(MSG_PROFILE_DELETION_SUCCESS_WINDOWS))) {
	    LOGGER.info("Successfully removed all existing profiles on NUC " + device.getHostMacAddress());
	} else {
	    LOGGER.info("Existing profiles in NUC may not be resolved properly for NUC {} , Response : {}",
		    device.getHostMacAddress(), response);
	}

    }

    /**
     * Method to verify the Wi-Fi Security Mode in connected client for Windows client due to client refresh issues
     * 
     * @param connectedClientDevice
     *            Connected client Dut list
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param expectedSecurityMode
     *            the security mode expected
     * @param ssid
     *            ssid
     * @return status
     * 
     * @author Susheela C
     * @refactor Govardhan
     */

    public static boolean verifyExpectedSecurityModeInConnectedClient(Dut connectedClientDevice,
	    AutomaticsTapApi tapEnv, String expectedSecurityMode, String ssid) {
	LOGGER.debug("Entering verifyExpectedSecurityModeInConnectedClient");
	boolean status = false;
	String response = null;
	String commandResponse = null;
	String command = null;
	try {
	    command = BroadBandConnectedClientTestConstants.WINDOWS_COMMAND_TO_GET_WLAN_NETWORK
		    + BroadBandTestConstants.SINGLE_SPACE_CHARACTER + BroadBandTestConstants.SYMBOL_PIPE
		    + BroadBandTestConstants.SINGLE_SPACE_CHARACTER + BroadBandTestConstants.CMD_GREP_I
		    + BroadBandTestConstants.SINGLE_SPACE_CHARACTER + ssid;
	    LOGGER.info("[TEST LOG] : command : " + command);

	    long startTime = System.currentTimeMillis();
	    LOGGER.info("[TEST LOG] : startTime : " + startTime);
	    do {
		commandResponse = tapEnv.executeCommandOnOneIPClients(connectedClientDevice, command);
		LOGGER.info("[TEST LOG] : commandResponse : " + commandResponse);
		if (CommonMethods.isNotNull(commandResponse)
			&& CommonUtils.patternSearchFromTargetString(commandResponse, ssid)) {
		    LOGGER.info(
			    "Going to validate Wifi Security Mode for the SSID : " + ssid + " on the windows client");
		    response = tapEnv.executeCommandOnOneIPClients(connectedClientDevice,
			    WINDOWS_COMMAND_TO_GET_WIFI_SECURITY_MODE.replaceAll("<ssid>", ssid));
		    LOGGER.info("Response for wifi security mode in Windows client : " + response);
		    status = CommonMethods.isNotNull(response)
			    && response.toUpperCase().contains(expectedSecurityMode.toUpperCase());
		    if (!status) {
			LOGGER.info("Wifi Security Mode for the SSID : " + ssid
				+ " on the windows client is still not updated to " + expectedSecurityMode
				+ "...Going to Wait for 30 seconds and re-check.");
		    }
		} else {
		    LOGGER.info("SSID : " + ssid
			    + " is not visible on the client to make a wifi conenction. Going to Wait for 30 seconds and re-check.");
		}
	    } while (!status && (System.currentTimeMillis() - startTime) < BroadBandTestConstants.THREE_MINUTE_IN_MILLIS
		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
	} catch (Exception exception) {
	    LOGGER.error("Exception occured while Verifying Expected Security Mode In ConnectedClient : "
		    + exception.getMessage());
	}
	LOGGER.debug("Ending verifyExpectedSecurityModeInConnectedClient");
	return status;
    }

    /**
     * This method will create a profile from a predefined template xml file .The new profile can be used to establish
     * connection to given ssid and password
     * 
     * @param ssid
     *            SSID to connect
     * @param password
     *            PASSWORD corresponding to SSID
     * @param profileName
     *            Profilename to be provided
     * @param isSsidHidden
     *            true if SSID to be connected is Hidden.
     * @return Returns status of profile creation
     * @refactor Govardhan
     */

    public static boolean createNewProfileFromTemplate(String ssid, String password, String profileName,
	    String securityMode, boolean isSsidHidden) {
	boolean status = false;
	ClassLoader classloader = Thread.currentThread().getContextClassLoader();
	URL xmlProfileTemplate = classloader.getResource(XML_FILENAME_WIFI_PROFILE_TEMPLATE);

	try {
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder;
	    dBuilder = dbFactory.newDocumentBuilder();
	    Document doc = dBuilder.parse(xmlProfileTemplate.toString());
	    LOGGER.info("[TEST LOG] : Document : " + doc);
	    doc.getDocumentElement().normalize();
	    getNodeAndAssignContent(doc, NODE_NAME, ssid);
	    BigInteger bIntegerSSID = new BigInteger(ssid.getBytes("UTF-8"));
	    LOGGER.info("[TEST LOG] : bIntegerSSID : " + bIntegerSSID);
	    String hex = bIntegerSSID.toString(16);
	    LOGGER.info("[TEST LOG] : hex : " + hex);
	    getNodeAndAssignContent(doc, NODE_HEX, hex);
	    getNodeAndAssignContent(doc, NODE_NONBROADCAST,
		    isSsidHidden ? BroadBandTestConstants.TRUE : BroadBandTestConstants.FALSE);
	    getNodeAndAssignContent(doc, NODE_KEYMATERIAL, password);
	    if (ConnectedNattedClientsUtils.WIFI_CONNECT_IN_AUTO) {
		LOGGER.info("[TEST LOG] : WIFI_CONNECT_IN_AUTO ");
		getNodeAndAssignContent(doc, NODE_CONNECTION_MODE, BroadBandTestConstants.STRING_AUTO.toLowerCase());
	    }
	    if (CommonMethods.isNotNull(securityMode) && securityMode.equalsIgnoreCase(SECURITY_MODE_OPEN)) {
		LOGGER.info("[TEST LOG] : WIFI_CONNECT_IN_AUTO ");
		getNodeAndAssignContent(doc, NODE_AUTHENTICATION, SECURITY_MODE_OPEN);
		getNodeAndAssignContent(doc, NODE_ENCRYPTION, ENCRYPTION_NONE);
	    }
	    DOMSource source = new DOMSource(doc);
	    // for output to file, console
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = transformerFactory.newTransformer();
	    // write to console or file
	    // StreamResult console = new StreamResult(System.out);
	    // StreamResult file = new StreamResult(new File(profileName));
	    LOGGER.debug("Creating stream result =" + profileName);
	    LOGGER.debug("Creating stream result =" + URLDecoder.decode(profileName, "UTF-8"));
	    StreamResult file = new StreamResult(
		    new FileOutputStream(new File(URLDecoder.decode(profileName, "UTF-8"))));
	    // write data
	    // transformer.transform(source, console);
	    transformer.transform(source, file);

	    File f = new File(profileName);
	    if (f.exists() && !f.isDirectory()) {
		status = true;
		LOGGER.info("[TEST LOG] : Successfully created profile");
	    }
	} catch (IOException e) {
	    LOGGER.error("Exception occurred while creating profile for " + ssid + " E:" + e.getMessage());
	} catch (ParserConfigurationException e) {
	    LOGGER.error("Exception occurred while creating profile for " + ssid + " E:" + e.getMessage());
	} catch (SAXException e) {
	    LOGGER.error("Exception occurred while creating profile for " + ssid + " E:" + e.getMessage());
	} catch (TransformerConfigurationException e) {
	    LOGGER.error("Exception occurred while creating profile for " + ssid + " E:" + e.getMessage());
	} catch (TransformerException e) {
	    LOGGER.error("Exception occurred while creating profile for " + ssid + " E:" + e.getMessage());
	} catch (Exception e) {
	    LOGGER.error("Exception occurred while creating profile for " + ssid + " E:" + e.getMessage());
	} finally {
	    ConnectedNattedClientsUtils.WIFI_CONNECT_IN_AUTO = false;
	}

	return status;
    }

    /**
     * 
     * This method selects the required node and sets its value of given xml file
     * 
     * @param doc
     *            Document interface representing the XML profile template
     * @param nodeName
     *            Name of the node ,whose value has to be set
     * @param content
     *            Content to set.
     * @refactor Govardhan
     */
    private static void getNodeAndAssignContent(Document doc, String nodeName, String content) {
	NodeList subnList = (NodeList) doc.getElementsByTagName(nodeName);
	if (subnList != null) {
	    for (int i = 0; i < subnList.getLength(); i++) {
		subnList.item(i).setTextContent(content);
		LOGGER.info("[TEST LOG] : subnList.item(i).setTextContent(content) " + nodeName);
	    }
	} else {
	    LOGGER.error("Missing node '" + nodeName + "' in template.Profile will not be generated properly");
	}
    }

    /**
     * This method copied the created profile to given device using sftp
     * 
     * Note : CommonMethods.copyDirectoryFromVMToConnectedDevice also we can use to copy the file from VM to Connected
     * Client.But password contains '@' char,This method wont support to move the file.
     * 
     * @param device
     *            {@link Dut}
     * @param fileCopyFrom
     *            Fullpath of file to be copied
     * @refactor Govardhan
     */
    public static boolean copyFileFromJumpServerToClient(Dut device, String fileCopyFrom) {
	LOGGER.debug("STARTING METHOD : copyFileFromJumpServerToClient()");
	boolean status = false;
	Device settop = (Device) device;
	String remoteFileName = fileCopyFrom.substring(fileCopyFrom.lastIndexOf(File.separator) + 1);
	String username = settop.getUsername();
	String hostname = settop.getNatAddress();
	String port = settop.getNatPort();
	String password = settop.getPassword();
	String fileCopyTo = BroadBandTestConstants.HOME_DIR + settop.getUsername() + "/" + remoteFileName;
	SshConnection sshConnection = null;
	try {
	    LOGGER.info("[TEST LOG] : Copying File From JumpServer To Client");
	    LOGGER.info("[TEST LOG] : Path for Copying File From :" + fileCopyFrom);
	    LOGGER.info("[TEST LOG] : Path for Copying File To  :" + fileCopyTo);
	    if (CommonMethods.isNotNull(username) && CommonMethods.isNotNull(hostname) && CommonMethods.isNotNull(port)
		    && CommonMethods.isNotNull(password)) {
		int portValue = Integer.parseInt(port);
		sshConnection = new SshConnection(hostname, portValue, username, password);
		sshConnection.putFile(fileCopyFrom, fileCopyTo);
		status = true;
	    } else {
		LOGGER.error("Device " + device.getHostMacAddress() + " is not properly configured ");
	    }
	} catch (Exception e) {
	    LOGGER.error("Error while copying profile " + e.getMessage());
	} finally {
	    try {
		if (sshConnection != null) {
		    sshConnection.disconnect();
		}
	    } catch (Exception e) {
		LOGGER.error("Error while disconnecting the channel  " + e.getMessage());
	    }
	}
	LOGGER.debug("ENDING METHOD : copyFileFromJumpServerToClient()");
	return status;
    }

    /**
     * 
     * This method copied the created profile to given device
     * 
     * @param fileToCopy
     *            Fullpath of file to be copied
     * @param device
     *            Dut Object
     * @refactor Govardhan
     */
    public static boolean copyfileToClient(String fileToCopy, Dut device) {
	boolean status = false;
	Device settop = (Device) device;
	String remoteFileName = fileToCopy.substring(fileToCopy.lastIndexOf(File.separator) + 1);
	String userName = settop.getUsername();
	String host = settop.getNatAddress();
	String port = settop.getNatPort();
	String password = settop.getPassword();
	JSch jsch = new JSch();

	OutputStream out = null;
	InputStream in = null;
	Session session = null;
	FileInputStream fis = null;
	Channel channel = null;
	try {
	    if (CommonMethods.isNotNull(userName) && CommonMethods.isNotNull(host) && CommonMethods.isNotNull(port)
		    && CommonMethods.isNotNull(password)) {
		int portValue = Integer.parseInt(port);

		java.util.Properties config = new java.util.Properties();
		config.put("StrictHostKeyChecking", "no");
		session = jsch.getSession(userName, host, portValue);
		session.setConfig(config);
		LOGGER.debug("Creating session");
		session.setPassword(password);
		session.connect();

		// exec 'scp -t remoteFileName' remotely
		String command = "scp " + " -t " + remoteFileName;
		channel = session.openChannel("exec");
		((ChannelExec) channel).setCommand(command);

		// get I/O streams for remote scp
		out = channel.getOutputStream();
		in = channel.getInputStream();

		channel.connect();

		File fp = new File(fileToCopy);
		long fileSize = fp.length();
		command = "C0644 " + fileSize + " " + remoteFileName + "\n";
		out.write(command.getBytes());
		out.flush();

		ischeckCopyAcknowledgementSuccess(in);

		fis = new FileInputStream(fileToCopy);
		byte[] buf = new byte[1024];
		while (true) {
		    int len = fis.read(buf, 0, buf.length);
		    if (len <= 0)
			break;
		    out.write(buf, 0, len); // out.flush();
		}
		// send '\0'
		buf[0] = 0;
		out.write(buf, 0, 1);
		out.flush();
		ischeckCopyAcknowledgementSuccess(in);
		LOGGER.info("[TEST LOG] : SCP Complete : " + remoteFileName);
		status = true;
	    } else {
		LOGGER.error("Device " + device.getHostMacAddress() + " is not properly configured ");
	    }
	} catch (JSchException e) {
	    LOGGER.error("Error while copying profile " + e.getMessage());
	} catch (Exception e) {
	    LOGGER.error("Error while copying profile " + e.getMessage());
	} finally {
	    try {
		if (fis != null) {
		    fis.close();
		}
		if (in != null) {
		    in.close();
		}
		if (out != null) {
		    out.close();
		}
		if (channel != null) {
		    channel.disconnect();
		}
		if (session != null) {
		    session.disconnect();
		}
	    } catch (IOException e) {
	    }
	}
	return status;
    }

    private static void ischeckCopyAcknowledgementSuccess(InputStream is) throws IOException, JSchException {
	boolean status = false;
	int b = is.read();
	if (b == 0) {
	    status = true;
	}
	if (!status) {
	    throw new JSchException("Error while initiating or during scp for profile");
	}
    }

    /**
     * This method execute commands on provided settop , based on its OS type and model , to connect to given SSID with
     * the provided security mode for connection
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param ssid
     *            SSID to connect to
     * @param password
     *            Password for connecting to SSID
     * @param securityMode
     *            Security mode for conenction.By default, conenction mode is WPA2PSK and encryption is AES.If user want
     *            open conenction,the string to be provided for this field is "open"
     * @return returns status of operation
     * @refactor Govardhan
     */
    public static boolean connectToSSID(Dut device, AutomaticsTapApi tapEnv, String ssid, String password,
	    String securityMode) {
	return connectToSSID(device, tapEnv, ssid, password, securityMode, false);
    }

    /**
     * This method execute commands on provided settop , based on its OS type and model , to connect to given SSID with
     * the provconnectToSSIDided security mode for connection
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param ssid
     *            SSID to connect to
     * @param password
     *            Password for connecting to SSID
     * @param securityMode
     *            Security mode for conenction.By default, conenction mode is WPA2PSK and encryption is AES.If user want
     *            open conenction,the string to be provided for this field is "open"
     * @param isSsidHidden
     *            Is SSID to be connected is Hidden.
     * @return returns status of operation
     * @refactor Govardhan
     */
    public static boolean connectToSSID(Dut device, AutomaticsTapApi tapEnv, String ssid, String password,
	    String securityMode, boolean isSsidHidden) {
	boolean retrunStatus = false;
	Device ecastSettop = (Device) device;
	if (ecastSettop.isLinux()) {
	    retrunStatus = connectToLinux(device, tapEnv, ssid, password);
	} else if (ecastSettop.isWindows()) {
	    retrunStatus = connectToWindows(ecastSettop, tapEnv, ssid, password, securityMode, isSsidHidden);
	} else if (ecastSettop.isRaspbianLinux()) {
	} else if (ecastSettop.isMacOS()) {
	    retrunStatus = connectToMacOS(ecastSettop, tapEnv, ssid, password, securityMode);
	}
	return retrunStatus;
    }

    /**
     * 
     * This method executes commands on provided settop , based on its OS type and model , to connect to given SSID
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param ssid
     *            SSID to connect to
     * @param password
     *            Password for connecting to SSID
     * @return returns status of operation
     * @refactor Govardhan
     */
    public static boolean connectToSSID(Dut device, AutomaticsTapApi tapEnv, String ssid, String password) {
	return connectToSSID(device, tapEnv, ssid, password, false);
    }

    /**
     * This method execute commands on provided settop , based on its OS type and model , to connect to given SSID and
     * get execution time the provided security mode for connection
     * 
     * @param clientSettop
     *            Settop object
     * @param tapEnv
     *            Tap environment
     * @param ssid
     *            SSID to connect to
     * @param password
     *            Password for connecting to SSID
     * @param securityMode
     *            Security mode for conenction.By default, conenction mode is WPA2PSK and encryption is AES.If user want
     *            open conenction,the string to be provided for this field is "open"
     * @param isSsidHidden
     *            true, if SSID to connect is hidden.
     * @return time difference before and after wifi connection
     * @refactor Athira
     */
    public static long connectToSSIDForPerfTest(Dut device, Dut clientDevice, AutomaticsTapApi tapEnv, String ssid,
	    String password, String securityMode, boolean isSsidHidden) {
	long timeOfConnection = 0;
	Device tapDevice = (Device) clientDevice;

	if (tapDevice.isLinux()) {
	    timeOfConnection = connectToLinuxForPerfTest(device, clientDevice, tapEnv, ssid, password);
	} else if (tapDevice.isWindows()) {
	    timeOfConnection = connectToWindowsForPerfTest(device, clientDevice, tapEnv, ssid, password, securityMode,
		    isSsidHidden);
	}
	return timeOfConnection;
    }

    /**
     * 
     * This method executes commands on provided settop , based on its OS type and model , to connect to given SSID and
     * get execution time
     * 
     * @param clientDevice
     *            Dut object
     * @param tapEnv
     *            Tap environment
     * @param ssid
     *            SSID to connect to
     * @param password
     *            Password for connecting to SSID
     * @param isSsidHidden
     *            true, if SSID to connect is hidden.
     * @return time difference before and after wifi connection
     * @refactor Athira
     */
    public static long connectToSSIDForPerfTest(Dut device, Dut clientDevice, AutomaticsTapApi tapEnv, String ssid,
	    String password, boolean isSsidHidden) {
	long timeOfConnection = 0;
	Device tapDevice = (Device) clientDevice;
	if (tapDevice.isLinux()) {
	    timeOfConnection = connectToLinuxForPerfTest(device, clientDevice, tapEnv, ssid, password);
	} else if (tapDevice.isWindows()) {
	    timeOfConnection = connectToWindowsForPerfTest(device, clientDevice, tapEnv, ssid, password, null,
		    isSsidHidden);
	}
	return timeOfConnection;
    }

    /**
     * 
     * This method connects the LINUX device to wifi by specifying the ssid and password
     * 
     * @param clientDevice
     *            Dut object
     * @param tapEnv
     *            Tap environment
     * @param ssid
     *            SSID to connect to
     * @param password
     *            Password for connecting to SSID
     * @return time difference before and after wifi connection
     */
    private static long connectToLinuxForPerfTest(Dut device, Dut clientDevice, AutomaticsTapApi tapEnv, String ssid,
	    String passwordwifi) {
	String command = "";
	long startTime;
	long endTime = 0;
	long averageTime = 0;
	String response = "";
	startTime = System.currentTimeMillis();
	String gatewayIp = null;
	Device tapDevice = (Device) clientDevice;
	String host = tapDevice.getNatAddress();
	String username = tapDevice.getUsername();
	String password = tapDevice.getPassword();
	String port = tapDevice.getNatPort();
	SshConnection sshConnection = null;
	gatewayIp = BroadBandWiFiUtils.getGatewayIpOfDevice(device);

	String[] commandToConnect = { CONNECT_LINUX.replaceAll("<ssid>", ssid).replaceAll("<password>", passwordwifi) };
	String[] commandToPing = { BroadBandTestConstants.STRING_PING_TO_LINUX.replace("<IPADDRESS>", gatewayIp) };

	try {
	    StringBuffer commandString = new StringBuffer();
	    commandString.append(commandToConnect[0]);
	    String formattedCommands = commandString.toString();
	    sshConnection = new SshConnection(host, Integer.parseInt(port), username, password);
	    startTime = System.currentTimeMillis();
	    sshConnection.send(formattedCommands, 1000);
	    response = sshConnection.getSettopResponse();
	    LOGGER.info("[SSH EXECUTION] : Executed command " + formattedCommands
		    + " on connected client setup : Mac Address [" + clientDevice.getHostMacAddress() + "] IP Address ["
		    + host + "] and Port Number [" + port + "]" + "\n[SSH EXECUTION] : Result - \n" + response);
	    if (CommonMethods.isNotNull(response) && response.contains(CONNECT_LINUX_SUCCESS_MESSAGE)) {
		commandString = new StringBuffer();
		commandString.append(commandToPing[0]);
		formattedCommands = commandString.toString();
		sshConnection.send(formattedCommands, 1000);
		response = sshConnection.getSettopResponse();
		endTime = System.currentTimeMillis();
		LOGGER.info("[SSH EXECUTION] : Executed command " + formattedCommands
			+ " on connected client setup : Mac Address [" + clientDevice.getHostMacAddress()
			+ "] IP Address [" + host + "] and Port Number [" + port + "]"
			+ "\n[SSH EXECUTION] : Result - \n" + response);
		if ((response.contains(BroadBandTestConstants.STRING_LINUX_PING_RESPONSE))
			&& CommonMethods.isNotNull(response)) {
		    LOGGER.info("END CAPTURING TIME");
		    averageTime = endTime - startTime;
		} else {
		    LOGGER.error("Ping statistics response from device is greater than 0% loss");
		}
	    }

	} catch (Exception e) {
	    LOGGER.error("[SSH FAILED] : " + host + ":" + port + e.getMessage());
	    LOGGER.error("[SSH FAILED] : " + host + ":" + port + " Looks like this device is not properly configured");
	} finally {
	    if (null != sshConnection) {
		sshConnection.disconnect();
	    }
	}
	String connectStatus = CONNECT_STATUS_COMMAND_LINUX.replaceAll("<ssid>", ssid);
	String output = tapEnv.executeCommandOnOneIPClients(clientDevice, connectStatus);
	command = DISCONNECT_LINUX.replaceAll("<ssid>", ssid + " " + output);
	response = tapEnv.executeCommandOnOneIPClients(clientDevice, command);
	if (CommonMethods.isNotNull(response) && response.contains(DISCONNECT_LINUX_SUCCESS_MESSAGE)) {
	    LOGGER.info("Dissconnected from WIFI SSID successfully");
	}

	return averageTime;
    }

    /**
     * 
     * This method connects the WINDOWS device to wifi by specifying the ssid and password
     * 
     * @param clientDevice
     *            Dut object
     * @param tapEnv
     *            Tap environment
     * @param ssid
     *            SSID to connect to
     * @param password
     *            Password for connecting to SSID
     * @param securityMode
     *            Security mode with which connection has to be established.
     * @param isSsidHidden
     *            true, if SSID to connect is Hidden.
     * @return status of connection
     * @refactor Athira
     */
    private static long connectToWindowsForPerfTest(Dut device, Dut clientDevice, AutomaticsTapApi tapEnv, String ssid,
	    String passwordone, String securityMode, boolean isSsidHidden) {
	String gatewayIp;
	boolean proceedToConnect = true;
	long endTime = 0;
	long connectionTime = 0;
	long startTime = 0;
	String response = "";
	String pingLoss = null;
	Device tapDevice = (Device) clientDevice;
	// Retriving Connected client values
	String host = tapDevice.getNatAddress();
	String username = tapDevice.getUsername();
	String password = tapDevice.getPassword();
	String port = tapDevice.getNatPort();
	SshConnection sshConnection = null;
	// Adding Profile to Connected client
	if (isProfileAvailable(clientDevice, tapEnv, ssid)) {
	    LOGGER.info("[TEST LOG] : Creating profile with ssid & password....");
	    proceedToConnect = addWifiProfileAndConnect(clientDevice, tapEnv, ssid, passwordone, securityMode,
		    isSsidHidden);
	}
	// Retrieving Gateway IP
	gatewayIp = BroadBandWiFiUtils.getGatewayIpOfDevice(device);
	if (proceedToConnect) {
	    String commandToConnect = CONNECT_PROFILE_WINDOWS.replaceAll("<ssid>", ssid);
	    String commandToPing = BroadBandTestConstants.STRING_PING_TO_WINDOWS.replace("<IPADDRESS>", gatewayIp);
	    try {
		// Executing command'netsh wlan connect profile=<ssid>' in Connected client
		sshConnection = new SshConnection(host, Integer.parseInt(port), username, password);
		startTime = System.currentTimeMillis();
		sshConnection.send(commandToConnect, 1000);
		response = sshConnection.getDefaultResponse();
		LOGGER.info("[SSH EXECUTION] : Executed command " + commandToConnect
			+ " on connected client setup : Mac Address [" + clientDevice.getHostMacAddress()
			+ "] IP Address [" + host + "] and Port Number [" + port + "]"
			+ "\n[SSH EXECUTION] : Result - \n" + response);
		if (CommonMethods.isNotNull(response) && BroadBandCommonUtils.patternSearchFromTargetString(response,
			CONNECT_WINDOWS_SUCCESS_MESSAGE)) {
		    // Executing command'ping ' in connecting client
		    sshConnection.send(commandToPing, 1000);
		    response = sshConnection.getDefaultResponse();
		    endTime = System.currentTimeMillis();
		    LOGGER.info("[SSH EXECUTION] : Executed command " + commandToPing
			    + " on connected client setup : Mac Address [" + clientDevice.getHostMacAddress()
			    + "] IP Address [" + host + "] and Port Number [" + port + "]"
			    + "\n[SSH EXECUTION] : Result - \n" + response);
		    try {
			// Retrieving ping percentage from response
			pingLoss = CommonMethods
				.patternFinderToReturnAllMatchedString(response,
					BroadBandConnectedClientTestConstants.PATTERN_MATCHER_PING_RESPONSE_WINDOWS)
				.get(BroadBandTestConstants.CONSTANT_3);
			if (CommonMethods.isNotNull(pingLoss) && pingLoss.equals(BroadBandTestConstants.STRING_ZERO)) {
			    LOGGER.info("END CAPTURING TIME");
			    connectionTime = endTime - startTime;
			} else {
			    LOGGER.error("Ping statistics response from device is greater than 0% loss ; LOSS = "
				    + pingLoss);
			}
		    } catch (IndexOutOfBoundsException exception) {
			// If ping failure Pattern cant retrieve pattern group throws
			// IndexOutOfBoundsException
			LOGGER.error("Ping failure in connected client RESPONSE :\n" + response);
		    } catch (Exception exception) {
			LOGGER.error(
				"Exception caught while executing ping in Connected client " + exception.getMessage());
		    }
		}

	    } catch (Exception e) {
		LOGGER.error("[SSH FAILED] : " + host + ":" + port + e.getMessage());
		LOGGER.error(
			"[SSH FAILED] : " + host + ":" + port + " Looks like this device is not properly configured");
	    } finally {
		if (null != sshConnection) {
		    sshConnection.disconnect();
		}
	    }
	    LOGGER.info("[TEST LOG] : Disconnecting conenctivitty with ssid" + ssid);

	    response = tapEnv.executeCommandOnOneIPClients(clientDevice, DISCONNECT_WINDOWS);
	    if (CommonMethods.isNotNull(response) && response.contains(DISCONNECT_WINDOWS_SUCCESS_MESSAGE)) {
		LOGGER.info("Dissconnected from WIFI SSID successfully");
	    }
	}
	return connectionTime;

    }

    /**
     * 
     * This method disconnects wifi from an SSID.
     * 
     * @param device
     *            Dut object
     * @param tapEnv
     *            AutomaticsTapApi environment
     * @param ssid
     *            SSID to connect to
     * @return returns status of operation
     * @refactor Govardhan
     */
    public static boolean disconnectSSID(Dut device, AutomaticsTapApi tapEnv, String ssid) {
	boolean retrunStatus = false;
	Device ecastDevice = (Device) device;
	if (ecastDevice.isLinux()) {
	    String connectStatus = CONNECT_STATUS_COMMAND_LINUX.replaceAll("<ssid>", ssid);
	    String output = tapEnv.executeCommandOnOneIPClients(device, connectStatus);
	    String command = DISCONNECT_LINUX.replaceAll("<ssid>", ssid + " " + output);
	    String response = tapEnv.executeCommandOnOneIPClients(device, command);
	    if (CommonMethods.isNotNull(response) && response.contains(DISCONNECT_LINUX_SUCCESS_MESSAGE)) {
		retrunStatus = true;
	    }
	} else if (ecastDevice.isWindows()) {
	    LOGGER.info("[TEST LOG] : Disconnecting conenctivitty with ssid" + ssid);
	    String response = tapEnv.executeCommandOnOneIPClients(device, DISCONNECT_WINDOWS);
	    if (CommonMethods.isNotNull(response) && response.contains(DISCONNECT_WINDOWS_SUCCESS_MESSAGE)) {
		retrunStatus = true;
	    }
	}
	return retrunStatus;
    }

    /**
     * 
     * This method is to verify ping command execution on the WiFi Client Device.
     * 
     * @param wifiClientDevice
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param ipAddress
     *            String representing the IP Address.
     * @return returns status of operation
     * @refactor Alan_Bivera
     */
    public static boolean verifyPingConnection(Dut wifiClientDevice, AutomaticsTapApi tapEnv, String ipAddress) {
	LOGGER.debug("STARTING METHOD : verifyPingConnection()");
	boolean result = false;
	Device ecastSettop = (Device) wifiClientDevice;
	LOGGER.info("OS TYPE OF THE WIFI CLIENT: " + ecastSettop.getOsType());
	String pingCommand = ecastSettop.isLinux()
		? (CommonMethods.isIpv6Address(ipAddress) ? BroadBandCommandConstants.CMD_PING_LINUX_IPV6
			: BroadBandCommandConstants.CMD_PING_LINUX)
		: BroadBandCommandConstants.CMD_PING_WINDOWS;
	LOGGER.info("Ping Command: " + pingCommand);
	LOGGER.info("BroadBandCommonUtils.concatStringUsingStringBuffer(pingCommand, ipAddress) is: "
		+ BroadBandCommonUtils.concatStringUsingStringBuffer(pingCommand, ipAddress));
	String response = tapEnv.executeCommandOnOneIPClients(wifiClientDevice,
		BroadBandCommonUtils.concatStringUsingStringBuffer(pingCommand, ipAddress));
	LOGGER.info("response is: " + response);

	if (CommonMethods.isNotNull(response) && !validatePingResponse(response)) {
	    response = ecastSettop.isLinux() || ecastSettop.isMacOS()
		    ? BroadBandCommonUtils.patternFinderMatchPositionBased(response,
			    ecastSettop.isLinux()
				    ? BroadBandConnectedClientTestConstants.PATTERN_MATCHER_PING_RESPONSE_LINUX
				    : BroadBandConnectedClientTestConstants.PATTERN_MATCHER_PING_RESPONSE_MAC,
			    BroadBandTestConstants.CONSTANT_3)
		    : BroadBandCommonUtils.patternFinderMatchPositionBased(response,
			    BroadBandConnectedClientTestConstants.PATTERN_MATCHER_PING_RESPONSE_WINDOWS,
			    BroadBandTestConstants.CONSTANT_4);
	    if (CommonMethods.isNotNull(response)) {
		try {
		    int iActualValue = Integer.parseInt(response);
		    result = iActualValue != BroadBandTestConstants.PERCENTAGE_VALUE_HUNDRED;
		} catch (NumberFormatException numberFormatException) {
		    // Log & Suppress the Exception
		    LOGGER.error(numberFormatException.getMessage());
		}
	    }
	}
	LOGGER.debug("ENDING METHOD : verifyPingConnection()");
	return result;
    }

    /**
     * 
     * This method verifies the wifi connect/disconnect status to/from an SSID.
     * 
     * @param device
     *            Dut object
     * @param tapEnv
     *            Tap environment
     * @param ssid
     *            SSID to connect to
     * @param shouldVerifyConnectStat
     *            boolean parameter indication whether to cehck for connect status or disconnect status.True corresponds
     *            to connectStatus
     * @return returns status of operation
     * @refactor Athira
     */
    public static boolean verifyWifiConnectionStatus(Dut device, AutomaticsTapApi tapEnv, String ssid,
	    boolean shouldVerifyConnectStat) {
	boolean retrunStatus = false;
	Device connDevice = (Device) device;
	LOGGER.info("[TEST LOG] : Verifying conenctivitty with ssid " + ssid);
	if (connDevice.isLinux()) {
	    String response = tapEnv.executeCommandOnOneIPClients(connDevice, VERIFY_CONNECT_LINUX);
	    if (CommonMethods.isNotNull(response) && response.contains(VERIFY_CONNECT_LINUX_MESSAGE)) {
		if (shouldVerifyConnectStat) {
		    retrunStatus = true;
		}
	    } else {
		if (!shouldVerifyConnectStat) {
		    retrunStatus = true;
		}
	    }
	} else if (connDevice.isWindows()) {
	    String response = tapEnv.executeCommandOnOneIPClients(connDevice,
		    CMD_WINDOWS_WIFI_INTERFACE_CONNECTION_STATUS);
	    String connectionString = CommonMethods.patternFinder(response, PATTERN_WIFI_INTERFACE_CONNECTION_STATUS);
	    if (shouldVerifyConnectStat) {
		retrunStatus = CommonMethods.isNotNull(connectionString)
			&& WIFI_INTERFACE_CONNECTION_STATUS.equalsIgnoreCase(connectionString.trim());
	    } else {
		retrunStatus = CommonMethods.isNull(connectionString) || (CommonMethods.isNotNull(connectionString)
			&& !WIFI_INTERFACE_CONNECTION_STATUS.equalsIgnoreCase(connectionString.trim()));
	    }
	}
	return retrunStatus;
    }

    /**
     * 
     * This method is to verify network connectivity using IPV4 or IPV6 address
     * 
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param IPV4Address
     *            boolean parameter indication to conenct with IPV4 address if passed as true and IPV6 address if passed
     *            as false
     * @return returns status of operation
     */
    public static boolean verifyNetworkConnection(Dut device, AutomaticsTapApi tapEnv, boolean IPV4Address) {
	boolean status = false;
	Device ecastDevice = (Device) device;
	String response = null;
	LOGGER.info("OS Type of the client obtained is: " + ecastDevice.getOsType());
	LOGGER.debug("STARTING METHOD : verifyNetworkConnection()");
	if (ecastDevice.isLinux()) {
	    LOGGER.info("Getting wifi connection interface Name for linux client");
	    String interfaceName = getWiFiInterfaceNameForLinuxClients(tapEnv, device);
	    if (CommonMethods.isNotNull(interfaceName)) {
		LOGGER.info("Successful retrieved the wifi connection interface name for linux client, "
			+ "checking n/w connectivity");
		response = IPV4Address
			? tapEnv.executeCommandOnOneIPClients(ecastDevice,
				BroadBandConnectedClientTestConstants.CMD_LINUX_NW_CONNECTION_USING_IPV4_IP
					.replaceAll("<interfaceName>", interfaceName))
			: tapEnv.executeCommandOnOneIPClients(ecastDevice,
				BroadBandConnectedClientTestConstants.CMD_LINUX_NW_CONNECTION_USING_IPV6_IP
					.replaceAll("<interfaceName>", interfaceName));
		status = CommonMethods.isNotNull(response);
	    } else {
		LOGGER.error("Failed to retrieve the Wifi Connection Interface Name using command "
			+ "'nmcli dev status | grep wifi'");
	    }
	} else if (ecastDevice.isWindows()) {
	    LOGGER.info("Checking network connectivity for the windows client");
	    response = tapEnv.executeCommandOnOneIPClients(ecastDevice,
		    BroadBandConnectedClientTestConstants.CMD_WINDOWS_NW_CONNECTION);
	    status = (CommonMethods.isNotNull(response)
		    && (CommonUtils.patternSearchFromTargetString(response, "bytes=")
			    || CommonUtils.patternSearchFromTargetString(response, "time=")));
	}
	LOGGER.debug("ENDING METHOD : verifyNetworkConnection()");
	return status;
    }

    /**
     * 
     * This method is to get WiFi Interface Name for linux clients
     * 
     * @param clientDevice
     *            Dut object
     * @param tapEnv
     *            AutomaticsTapApi environment
     * 
     * @return returns interfaceName
     * @refactor Govardhan
     */
    public static String getWiFiInterfaceNameForLinuxClients(AutomaticsTapApi tapEnv, Dut clientDevice) {
	String interfaceName = null;
	String response = null;
	Device ecastDevice = (Device) clientDevice;
	LOGGER.debug("STARTING METHOD : getWiFiInterfaceNameForLinuxClients()");
	if (ecastDevice.isLinux()) {
	    response = tapEnv.executeCommandOnOneIPClients(ecastDevice,
		    BroadBandConnectedClientTestConstants.CMD_LINUX_GET_INTERFACE_NAME);
	    if (CommonMethods.isNotNull(response)) {
		response = response.replaceAll(BroadBandTestConstants.PATTERN_MATCHER_FOR_MULTIPLE_SPACES,
			RDKBTestConstants.BLANK);
		interfaceName = CommonMethods.patternFinder(response,
			BroadBandTestConstants.PATTERN_MATCHER_WIFI_INTERFACE_CONN_CLIENT_LINUX).trim();
		LOGGER.info("Interface name obtained is : " + interfaceName);
	    }
	}
	LOGGER.debug("ENDING METHOD : getWiFiInterfaceNameForLinuxClients()");
	return interfaceName;
    }

    /**
     * 
     * This method is to retrieve the average time taken to ping the given host.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param wifiClientDevice
     *            {@link Dut}
     * @param host
     *            String representing the host to which ping needs to be performed.
     * 
     * @return Integer representing the Average Time from the Ping Response.
     * @refactor Govardhan
     * 
     */
    public static int getPingResponseAvgTime(Dut wifiClientDevice, AutomaticsTapApi tapEnv, String host) {
	LOGGER.debug("STARTING METHOD : getPingResponseAvgTime()");
	int iPingResponseAvgTime = 0;
	Device ecastDevice = (Device) wifiClientDevice;
	LOGGER.info("OS TYPE OF THE WIFI CLIENT: " + ecastDevice.getOsType());
	String pingCommand = ecastDevice.isLinux() ? BroadBandCommandConstants.CMD_PING_LINUX
		: BroadBandCommandConstants.CMD_PING_WINDOWS;
	String response = tapEnv.executeCommandOnOneIPClients(wifiClientDevice,
		BroadBandCommonUtils.concatStringUsingStringBuffer(pingCommand,
			BroadBandConnectedClientTestConstants.CMD_PING_PACKET_SIZE_OPTION, host));
	if (CommonMethods.isNotNull(response)) {
	    String avgTime = CommonMethods.patternFinder(response,
		    BroadBandTestConstants.PATTERN_MATCHER_PING_RESPONSE_AVG_TIME);
	    try {
		iPingResponseAvgTime = Integer.parseInt(avgTime);
	    } catch (NumberFormatException numberFormatException) {
		// Log & Suppress the Exception
		LOGGER.error(numberFormatException.getMessage());
	    }
	}
	LOGGER.info("PING RESPONSE AVG TIME: " + iPingResponseAvgTime);
	LOGGER.debug("ENDING METHOD : getPingResponseAvgTime()");
	return iPingResponseAvgTime;
    }

    /**
     * 
     * This method verifies the wifi connect/disconnect status to/from an SSID.
     * 
     * @param device
     *            Dut object
     * @param tapEnv
     *            AutomaticsTapApi environment
     * @param ssid
     *            SSID to connect to
     * @param shouldVerifyConnectStat
     *            boolean parameter indication whether to cehck for connect status or disconnect status.True corresponds
     *            to connectStatus
     * @return returns status of operation
     * @refactor Govardhan
     */
    public static boolean verifyConnectToSSID(Dut device, AutomaticsTapApi tapEnv, String ssid,
	    boolean shouldVerifyConnectStat) {
	boolean retrunStatus = false;
	Device ecastDevice = (Device) device;
	LOGGER.info("[TEST LOG] : Verifying conenctivitty with ssid " + ssid);
	if (ecastDevice.isLinux()) {
	    String response = tapEnv.executeCommandOnOneIPClients(ecastDevice, VERIFY_CONNECT_LINUX);
	    if (CommonMethods.isNotNull(response) && response.contains(VERIFY_CONNECT_LINUX_MESSAGE)) {
		if (shouldVerifyConnectStat) {
		    retrunStatus = true;
		}
	    } else {
		if (!shouldVerifyConnectStat) {
		    retrunStatus = true;
		}
	    }
	} else if (ecastDevice.isWindows()) {
	    String response = tapEnv.executeCommandOnOneIPClients(ecastDevice, VERIFY_CONNECT_WINDOWS);
	    if (CommonMethods.isNotNull(response)) {
		if (shouldVerifyConnectStat) {
		    retrunStatus = true;
		}
	    } else {
		if (!shouldVerifyConnectStat) {
		    retrunStatus = true;
		}
	    }
	} else if (ecastDevice.isRaspbianLinux()) {

	}
	return retrunStatus;
    }

    /**
     * 
     * This method is to verify ping command execution for a particular duration on the Connected Client Device.
     * 
     * @param connectedClientDevice
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param ipAddress
     *            String representing the IP Address
     * @param durationInSeconds
     *            String which gives the Ping Count
     * @return returns status of operation
     * @refactor Govardhan
     */
    public static boolean verifyPingConnection(Dut connectedClientDevice, AutomaticsTapApi tapEnv, String ipAddress,
	    String durationInSeconds) {
	LOGGER.debug("STARTING METHOD : verifyPingConnection()");
	boolean result = false;
	Device ecastSettop = (Device) connectedClientDevice;
	LOGGER.info("OS TYPE OF THE WIFI CLIENT: " + ecastSettop.getOsType());
	String pingCommand = ecastSettop.isLinux() || ecastSettop.isRaspbianLinux()
		? (CommonMethods.isIpv6Address(ipAddress) ? BroadBandCommandConstants.CMD_PING_FOR_LINUX_IPV6
			: BroadBandCommandConstants.CMD_PING_FOR_LINUX)
		: BroadBandCommandConstants.CMD_PING_FOR_WINDOWS;
	LOGGER.info("PING Command: " + pingCommand + " " + durationInSeconds + " " + ipAddress);
	String[] commands = new String[] { BroadBandCommonUtils.concatStringUsingStringBuffer(pingCommand,
		BroadBandTestConstants.SINGLE_SPACE_CHARACTER, durationInSeconds,
		BroadBandTestConstants.SINGLE_SPACE_CHARACTER, ipAddress) };

	String response = tapEnv.executeCommandOnOneIPClients(connectedClientDevice, commands,
		(Integer.parseInt(durationInSeconds) + BroadBandTestConstants.FIFTY_SECONDS)
			* BroadBandTestConstants.SECONDS_TO_MILLISECONDS);
	if (CommonMethods.isNotNull(response)) {
	    response = ecastSettop.isWindows()
		    ? BroadBandCommonUtils.patternFinderMatchPositionBased(response,
			    BroadBandConnectedClientTestConstants.PATTERN_MATCHER_PING_RESPONSE_WINDOWS,
			    BroadBandTestConstants.CONSTANT_4)
		    : BroadBandCommonUtils.patternFinderMatchPositionBased(response,
			    BroadBandConnectedClientTestConstants.PATTERN_MATCHER_PING_RESPONSE_LINUX,
			    BroadBandTestConstants.CONSTANT_3);
	}
	if (CommonMethods.isNotNull(response)) {
	    try {
		int iActualValue = Integer.parseInt(response);
		result = iActualValue != BroadBandTestConstants.PERCENTAGE_VALUE_HUNDRED;
	    } catch (NumberFormatException numberFormatException) {
		// Log & Suppress the Exception
		LOGGER.error(numberFormatException.getMessage());
	    }
	}
	LOGGER.debug("ENDING METHOD : verifyPingConnection()");
	return result;

    }

    /**
     * 
     * This method is to verify iperf command execution for a particular duration on the Connected Client Device.
     * 
     * @param connectedClientDevice
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param ipAddress
     *            String representing the IP Address
     * @param durationInSeconds
     *            String which gives iperf time
     * @return returns status of operation
     * @refactor Said Hisham
     */
    public static boolean verifyIperfConnection(Dut connectedClientDevice, AutomaticsTapApi tapEnv, String ipAddress,
	    String durationInSeconds) {
	LOGGER.debug("STARTING METHOD : verifyIperfConnection()");
	boolean result = false;
	String senderResponse = null;
	String receiverResponse = null;
	Device ecastSettop = (Device) connectedClientDevice;
	LOGGER.info("OS TYPE OF THE WIFI CLIENT: " + ecastSettop.getOsType());
	String iperfCommand = BroadBandCommandConstants.CMD_TO_INITIATE_IPERF_TRAFFIC_FROM_CLIENT;
	String[] commands = new String[] { BroadBandCommonUtils.concatStringUsingStringBuffer(iperfCommand,
		BroadBandTestConstants.SINGLE_SPACE_CHARACTER, ipAddress, BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
		BroadBandTestConstants.TIME_PARAM, durationInSeconds) };
	LOGGER.info("IPERF Command: " + commands[0]);

	String response = tapEnv.executeCommandOnOneIPClients(connectedClientDevice, commands,
		(Integer.parseInt(durationInSeconds) + 100) * BroadBandTestConstants.SECONDS_TO_MILLISECONDS);
	LOGGER.info("response of iperf : " + response);
	if (CommonMethods.isNotNull(response)) {
	    LOGGER.info("response of iperf is not null");
	    senderResponse = BroadBandCommonUtils.patternFinderMatchPositionBased(response,
		    BroadBandConnectedClientTestConstants.PATTERN_TO_GET_IPERF_TRANSFER_RATE_SENT_FROM_CLIENT,
		    BroadBandTestConstants.CONSTANT_1);
	    LOGGER.info("DATA SENT : " + senderResponse);
	    receiverResponse = BroadBandCommonUtils.patternFinderMatchPositionBased(response,
		    BroadBandConnectedClientTestConstants.PATTERN_TO_GET_IPERF_TRANSFER_RATE_RECEIVED_BY_SERVER,
		    BroadBandTestConstants.CONSTANT_1);
	    LOGGER.info("DATA RECEIVED : " + receiverResponse);
	}
	if (CommonMethods.isNotNull(senderResponse) && CommonMethods.isNotNull(receiverResponse)) {
	    result = senderResponse.equalsIgnoreCase(receiverResponse);
	}
	LOGGER.debug("ENDING METHOD : verifyIperfConnection()");
	return result;
    }

    /**
     * Utility method used to verify the wifi client connection status
     * 
     * @param device
     *            Instance of {@link Dut}
     * @param tapEnv
     *            Instance of {@link AutomaticsTapApi}
     * @return True- Device already connected, Else-False
     * @refactor Govardhan
     */
    public static boolean verifyConnectionStatusOnWiFiClient(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug("STARTING METHOD verifyConnectionStatusOnWiFiClient()");
	boolean status = false;
	try {
	    String response = tapEnv.executeCommandOnOneIPClients(device, CMD_WINDOWS_WIFI_INTERFACE_CONNECTION_STATUS);
	    String connectionString = CommonMethods.patternFinder(response, PATTERN_WIFI_INTERFACE_CONNECTION_STATUS);
	    status = CommonMethods.isNotNull(connectionString)
		    && WIFI_INTERFACE_CONNECTION_STATUS.equalsIgnoreCase(connectionString.trim());
	    LOGGER.info(status ? "WiFi Client is connected with SSID" : "WiFi Client is not connected with SSID");
	} catch (Exception e) {
	    LOGGER.error(e.getMessage() + " Exception occured verifyConnectionStatusOnWiFiClient()" + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : verifyConnectionStatusOnWiFiClient()");
	return status;
    }

}

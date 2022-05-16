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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Device;
import com.automatics.device.Dut;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandConnectedClientTestConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;

public class BroadBandWiFiPacketCaptureUtils {
	protected static final Logger LOGGER = LoggerFactory.getLogger(BroadBandWiFiPacketCaptureUtils.class);
	
    /**
     * Method to get the ethernet connected client from the Connected Client List.
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link Dut}
     * 
     * @return Dut object representing the Ethernet Connected Client.
     * @refactor Athira
     */
    public static Dut getEthernetConnectedLinuxClient(AutomaticsTapApi tapEnv, Dut device) {
	BroadBandConnectedClientUtils.LOGGER.debug("ENTERING METHOD getEthernetConnectedClient");
	Dut ethernetClient = null;
	List<Dut> connectedClients = ((Device) device).getConnectedDeviceList();
	BroadBandConnectedClientUtils.LOGGER.info("DUTINFO DEVICE LIST = " + connectedClients);
	int connectedDevicesCount = null == connectedClients ? 0 : connectedClients.size();
	BroadBandConnectedClientUtils.LOGGER.info("# OF CONNECTED CLIENTS AVAILABLE = " + connectedDevicesCount);

	if (null != connectedClients && connectedClients.size() > 0) {
	    for (Dut clientSettop : connectedClients) {
		String connectionType = ((Device) clientSettop).getConnectedDeviceInfo().getConnectionType();
		BroadBandConnectedClientUtils.LOGGER.info("CLIENT DEVICE CONNECTION TYPE: " + connectionType);
		String connectedClientOs = ((Device) clientSettop).getConnectedDeviceInfo().getOsType();
		BroadBandConnectedClientUtils.LOGGER.info("CLIENT DEVICE OS TYPE: " + connectedClientOs);
		if (connectionType.equalsIgnoreCase(BroadBandTestConstants.CONNECTION_TYPE_ETHERNET)
			&& (connectedClientOs.equalsIgnoreCase(BroadBandConnectedClientTestConstants.OS_RASPBIAN_LINUX)
				|| connectedClientOs.equalsIgnoreCase(BroadBandTestConstants.OS_TYPE_LINUX))) {
		    ethernetClient = clientSettop;
		    break;
		}
	    }
	}
	BroadBandConnectedClientUtils.LOGGER.info("IS ETHERNET LINUX CLIENT AVAILABLE: " + (null != ethernetClient));
	BroadBandConnectedClientUtils.LOGGER.debug("ENDING METHOD getEthernetConnectedClient");
	return ethernetClient;
    }

    /**
     * Method to start capturing the WiFi packets. It initializes WiFi Packet Monitoring interface; then it starts
     * capturing the WiFi Packets.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param ethernetClient
     *            {@link Dut}
     * 
     * @return BroadBandResultObject representing the result of the operation with error message.
     * 
     * @refactor Athira
     */
    public static BroadBandResultObject startCapturingPackets(AutomaticsTapApi tapEnv, Dut ethernetClient) {
	LOGGER.debug("ENTERING METHOD startCapturingPackets");
	BroadBandResultObject resultObject = new BroadBandResultObject();
	boolean result = false;
	String errorMessage = null;
	String targetLocation = null;
	try {
	    errorMessage = "UNABLE TO RETRIEVE THE ETHERNET CLIENT FROM THE SETUP.";
	    result = null != ethernetClient;
	    if (result) {
		resultObject = initializeWiFiMonitorInterface(tapEnv, ethernetClient);
		result = resultObject.isStatus();
		errorMessage = resultObject.getErrorMessage();
	    }
	    if (resultObject.isStatus()) {
		errorMessage = "UNABLE TO TRANSFER THE SHELL SCRIPT FILE THAT CAPTURES THE WIFI PACKETS.";
		targetLocation = tapEnv.executeCommandOnOneIPClients(ethernetClient,
			BroadBandConnectedClientTestConstants.CMD_HOME_FILE_PATH).trim() + "/";
		String fileNameWithPath = BroadBandCommonUtils.concatStringUsingStringBuffer(targetLocation,
			BroadBandCommandConstants.FILE_PACKET_CAPTURE_SCRIPT);
		LOGGER.info("File Name With Path:" + fileNameWithPath);
		if (BroadBandConnectedClientUtils.isFileExistsInConnectedClient(ethernetClient, tapEnv,
			fileNameWithPath, BroadBandTestConstants.BOOLEAN_VALUE_FALSE)) {
		    if (BroadBandConnectedClientUtils.downloadFileUsingAutoVaultToConnectedClient(ethernetClient,
			    tapEnv,
			    BroadBandCommonUtils.concatStringUsingStringBuffer(
				    BroadBandTestConstants.AUTOVAULT_FILE_PATH,
				    BroadBandCommandConstants.FILE_PACKET_CAPTURE_SCRIPT),
			    targetLocation, BroadBandCommandConstants.FILE_PACKET_CAPTURE_SCRIPT)) {
			LOGGER.info(BroadBandCommandConstants.FILE_PACKET_CAPTURE_SCRIPT + " File Downloaded");
		    }
		}
		result = BroadBandConnectedClientUtils.isFileExistsInConnectedClient(ethernetClient, tapEnv,
			fileNameWithPath, BroadBandTestConstants.BOOLEAN_VALUE_TRUE);
	    }
	    if (result) {
		errorMessage = "UNABLE TO START CAPTURING THE WIFI PACKETS";
		tapEnv.executeCommandOnOneIPClients(ethernetClient,
			BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_SH,
				targetLocation, BroadBandCommandConstants.FILE_PACKET_CAPTURE_SCRIPT));
		tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		// Verify the capture started successfully with the PID of the process.
		String command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.CMD_PID_OF,
			BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandCommandConstants.CMD_TCPDUMP);
		String response = tapEnv.executeCommandOnOneIPClients(ethernetClient, command);
		result = CommonMethods.isNotNull(response);
	    }
	    LOGGER.info("### STARTED CAPTURING WIFI PACKETS ### : " + result);
	} catch (TestException exception) {
	    errorMessage = "EXCEPTION OCCURRED WHILE TRYING TO VERIFY THE CAPTURED WIFI PACKETS: "
		    + exception.getMessage();
	    LOGGER.error(errorMessage);
	}
	LOGGER.debug("ENDING METHOD startCapturingPackets");
	resultObject.setStatus(result);
	resultObject.setErrorMessage(errorMessage);
	return resultObject;
    }
    
    /**
     * Method to initialize the interface to capture the WiFi packets. It uses the following set of commands: iwdev, iw
     * phy phy0 info,iw phy phy0 interface add mon0 type monitor, iw dev, iw dev wlp4s0 del, ifconfig mon0 down,
     * ifconfig mon0 up, iwconfig mon0
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param ethernetClient
     *            {@link Dut}
     * 
     * @return BroadBandResultObject representing the result of the operation with error message.
     * 
     * @refactor Athira
     */
    public static BroadBandResultObject initializeWiFiMonitorInterface(AutomaticsTapApi tapEnv, Dut ethernetClient) {
	LOGGER.debug("ENTERING METHOD initializeWiFiMonitorInterface");
	BroadBandResultObject resultObject = new BroadBandResultObject();
	boolean result = false;
	String errorMessage = null;
	try {
	    errorMessage = "UNABLE TO RETRIEVE THE ETHERNET CLIENT FROM THE SETUP.";
	    result = null != ethernetClient;
	    if (result) {
		errorMessage = "UNABLE TO INITIALIZE THE WIFI MONITORING INTERFACE TO CAPTURE WIFI PACKETS.";
		tapEnv.executeCommandOnOneIPClients(ethernetClient, BroadBandCommandConstants.CMD_IW_DEV);
		tapEnv.executeCommandOnOneIPClients(ethernetClient, BroadBandCommandConstants.CMD_IW_PHY_INFO);
		tapEnv.executeCommandOnOneIPClients(ethernetClient, BroadBandCommandConstants.CMD_IW_PHY_ADD_MON0);
		tapEnv.executeCommandOnOneIPClients(ethernetClient, BroadBandCommandConstants.CMD_IW_DEV);
		tapEnv.executeCommandOnOneIPClients(ethernetClient, BroadBandCommandConstants.CMD_IW_DEL_INTERFACE);
		tapEnv.executeCommandOnOneIPClients(ethernetClient,
			BroadBandCommandConstants.CMD_MONITOR_INTERFACE_DOWN);
		tapEnv.executeCommandOnOneIPClients(ethernetClient, BroadBandCommandConstants.CMD_MONITOR_INTERFACE_UP);
		String response = tapEnv.executeCommandOnOneIPClients(ethernetClient,
			BroadBandCommandConstants.CMD_IWCONFIG_MON0);
		result = CommonMethods.isNotNull(response)
			&& response.contains(BroadBandTestConstants.WIFI_PACKET_MONITOR_TEXT);
	    }
	} catch (TestException exception) {
	    errorMessage = "EXCEPTION OCCURRED WHILE TRYING TO VERIFY THE CAPTURED WIFI PACKETS: "
		    + exception.getMessage();
	    LOGGER.error(errorMessage);
	}
	LOGGER.info("### INITIALIZE WIFI MONITORING INTERFACE ### : " + result);
	LOGGER.debug("ENDING METHOD initializeWiFiMonitorInterface");
	resultObject.setStatus(result);
	resultObject.setErrorMessage(errorMessage);
	return resultObject;
    }
    
    /**
     * Method to verify the text in the captured wifi packets which is in pcap file format. It is performed using the
     * tcpdump command.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param ethernetClient
     *            {@link Dut}
     * @param expectedTxRate
     *            String representing the expected Tx Rate.
     * @param wifiBand
     *            {@link WiFiFrequencyBand}
     * 
     * @return BroadBandResultObject representing the result of the operation with error message.
     * 
     * @refactor Athira
     */
    public static BroadBandResultObject searchInPcapFile(AutomaticsTapApi tapEnv, Dut ethernetClient, String searchText) {
	LOGGER.debug("ENTERING METHOD searchInPcapFile");
	BroadBandResultObject resultObject = new BroadBandResultObject();
	boolean result = false;
	String errorMessage = null;
	try {
	    errorMessage = "UNABLE TO VERIFY THE SEARCH TEXT IN THE CAPTURED WIFI PACKETS.";
	    String command = BroadBandCommonUtils
		    .concatStringUsingStringBuffer(BroadBandCommandConstants.CMD_READ_CAPTURE_WIFI_PACKETS, searchText);
	    String response = tapEnv.executeCommandOnOneIPClients(ethernetClient, command);
	    result = CommonMethods.isNotNull(response) && response.contains(
		    searchText.replaceAll(BroadBandTestConstants.SINGLE_QUOTE, BroadBandTestConstants.EMPTY_STRING));
	    LOGGER.info("### VERIFIED SEARCH TEXT IN CAPTURED WIFI PACKETS ### : " + result);
	} catch (TestException exception) {
	    errorMessage = "EXCEPTION OCCURRED WHILE TRYING TO VERIFY THE CAPTURED WIFI PACKETS: "
		    + exception.getMessage();
	    LOGGER.error(errorMessage);
	} finally {
	    // Remove the WiFi Packets File.
	    tapEnv.executeCommandOnOneIPClients(ethernetClient,
		    BroadBandCommandConstants.CMD_REMOVE_WIFI_PACKET_CAPURE);
	}
	LOGGER.debug("ENDING METHOD searchInPcapFile");
	resultObject.setStatus(result);
	resultObject.setErrorMessage(errorMessage);
	return resultObject;
    }
    
    
    /**
     * Method to stop capturing the WiFi packets. It retrieves the process id of tcpdump & kills the process.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param ethernetClient
     *            {@link Dut}
     * 
     * @return BroadBandResultObject representing the result of the operation with error message.
     * 
     * @refactor Athira
     */
    public static BroadBandResultObject stopCapturingPackets(AutomaticsTapApi tapEnv, Dut ethernetClient) {
	LOGGER.debug("ENTERING METHOD stopCapturingPackets");
	BroadBandResultObject resultObject = new BroadBandResultObject();
	boolean result = false;
	String errorMessage = null;
	String command = null;
	String response = null;
	try {
	    errorMessage = "UNABLE TO RETRIEVE THE ETHERNET CLIENT FROM THE SETUP.";
	    result = null != ethernetClient;
	    if (result) {
		errorMessage = "UNABLE TO RETRIEVE THE PROCESS ID OF TCPDUMP THAT CAPTURES THE WIFI PACKETS";

		command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.CMD_PID_OF,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandCommandConstants.CMD_TCPDUMP);
		response = tapEnv.executeCommandOnOneIPClients(ethernetClient, command);
		result = CommonMethods.isNotNull(response);
	    }
	    if (result) {
		errorMessage = "UNABLE TO STOP THE PROCESS THAT CAPTURES WIFI PACKETS.";
		command = BroadBandCommonUtils.concatStringUsingStringBuffer(
				((Device)ethernetClient).isMacOS() ? RDKBTestConstants.SDV_AGENT_KILL_PROCESS : BroadBandTestConstants.KILL_9, response.trim());
		response = tapEnv.executeCommandOnOneIPClients(ethernetClient, command);

		command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.CMD_PID_OF,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandCommandConstants.CMD_TCPDUMP);
		response = tapEnv.executeCommandOnOneIPClients(ethernetClient, command);
		result = CommonMethods.isNull(response);
	    }
	    LOGGER.info("### STOPPED CAPTURING WIFI PACKETS ### : " + result);
	} catch (TestException exception) {
	    errorMessage = "EXCEPTION OCCURRED WHILE TRYING TO VERIFY THE CAPTURED WIFI PACKETS: "
		    + exception.getMessage();
	    LOGGER.error(errorMessage);
	}
	LOGGER.debug("ENDING METHOD stopCapturingPackets");
	resultObject.setStatus(result);
	resultObject.setErrorMessage(errorMessage);
	return resultObject;
    }
    
}

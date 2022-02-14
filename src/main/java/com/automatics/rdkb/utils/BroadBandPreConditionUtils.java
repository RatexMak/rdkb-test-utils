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

/**
 * Utility class which handles the Pre condition related functionality and verification.
 * 
 * @author Muthukumar
 * @refactor Govardhan
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Dut;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.webpa.WebPaParameter;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpUtils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.wifi.BroadBandWiFiUtils;
import com.automatics.rdkb.utils.wifi.connectedclients.BroadBandConnectedClientUtils;

public class BroadBandPreConditionUtils {

    /** SLF4J logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandPreConditionUtils.class);

    /**
     * Pre condition to reboot the device and wait for ip accusition
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param preCondNumber
     *            Pre condition number
     * @author Govardhan
     */
    public static void preConditionToRebootAndWaitForIpAccusition(Dut device, AutomaticsTapApi tapEnv,
	    int preConStepNumber) {
	String errorMessage = null;
	boolean status = false;
	/**
	 * PRE-CONDITION : REBOOT THE DEVICE AND WAIT FOR IP ACCUSITION
	 */
	LOGGER.info("#######################################################################################");
	LOGGER.info(
		"PRE-CONDITION " + preConStepNumber + " : DESCRIPTION : REBOOT THE DEVICE AND WAIT FOR IP ACCUSITION");
	LOGGER.info("PRE-CONDITION " + preConStepNumber + " : ACTION : EXECUTE COMMAND : /sbin/reboot");
	LOGGER.info("PRE-CONDITION " + preConStepNumber + " : EXPECTED : DEVICE REBOOT SHOULD BE SUCCESSFUL");
	LOGGER.info("#######################################################################################");
	errorMessage = "FAILED TO REBOOT THE DEVICE AND WAIT FOR IP ACCUSITION";
	try {
	    status = CommonUtils.rebootAndWaitForIpAcquisition(tapEnv, device);
	} catch (Exception e) {
	    LOGGER.info("Exception caught rebooting the device" + e.getMessage());
	}
	if (status) {
	    LOGGER.info(
		    "PRE-CONDITION " + preConStepNumber + " : ACTUAL : DEVICE REBOOT AND IP ACCUSITION IS SUCCESSFUL");
	} else {
	    LOGGER.error("PRE-CONDITION " + preConStepNumber + " : ACTUAL : " + errorMessage);
	    throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION " + preConStepNumber
		    + " : FAILED : " + errorMessage);
	}
    }

    /**
     * Pre-Condition method to stop the MESH status using system commands
     * 
     * @param device
     *            instance of{@link Dut}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param meshInitialStatus
     *            mesh initial status before stopping mesh
     * @param preCondNumber
     *            Pre condition number
     * 
     * @Refactor Athira
     */
    public static void executePreConditionToDisableMeshUsingSystemCommands(Dut device, AutomaticsTapApi tapEnv,
	    String meshInitialStatus, int preConStepNumber) throws TestException {
	String errorMessage = null;
	boolean isMeshDisabled = false;
	/**
	 * PRECONDITION : STOP THE MESH STATUS
	 */
	LOGGER.info("#######################################################################################");
	LOGGER.info("PRE-CONDITION " + preConStepNumber + " : DESCRIPTION : STOP THE MESH SERVICE");
	LOGGER.info("PRE-CONDITION " + preConStepNumber
		+ " : ACTION :  + Execute system command to disable mesh and verify "
		+ BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_MESH_ENABLE);
	LOGGER.info("PRE-CONDITION " + preConStepNumber + " : EXPECTED : MESH SERVICE MUST BE STOPPED");
	LOGGER.info("#######################################################################################");
	errorMessage = "UNABLE TO STOP THE MESH STATUS";
	try {
	    if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
		tapEnv.executeCommandOnAtom(device, BroadBandCommandConstants.CMD_SYSTEM_MESH_STATUS);
		tapEnv.executeCommandOnAtom(device, BroadBandCommandConstants.CMD_SYSTEM_STOP_MESH_AGENT);
		tapEnv.executeCommandOnAtom(device, BroadBandCommandConstants.CMD_SYSTEM_STOP_MESH_SERVICE);
	    } else {
		tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_SYSTEM_MESH_STATUS);
		tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_SYSTEM_STOP_MESH_AGENT);
		tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_SYSTEM_STOP_MESH_SERVICE);
	    }
	    isMeshDisabled = BroadBandWebPaUtils.getAndVerifyWebpaValueInPolledDuration(device, tapEnv,
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_MESH_ENABLE, meshInitialStatus,
		    BroadBandTestConstants.ONE_MINUTE_IN_MILLIS, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
	} catch (Exception e) {
	    LOGGER.error("Unable to Stop mesh service due to " + e.getMessage());
	}
	if (!isMeshDisabled) {
	    LOGGER.info("PRE-CONDITION " + preConStepNumber + " : ACTUAL :  WEBPA RESPONSE FOR "
		    + BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_MESH_ENABLE
		    + "IS NULL. SUCCESSFULLY STOPPED THE MESH STATUS");
	} else {
	    LOGGER.error("PRE-CONDITION " + preConStepNumber + " : ACTUAL : " + errorMessage);
	    throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION " + preConStepNumber
		    + " FAILED : " + errorMessage);

	}

    }

    /**
     * Pre-Condition method to disable code big first enable.
     * 
     * @param device
     *            {@link Dut}
     */
    public static void executePreConditionToDisableCodeBigFirst(Dut device, AutomaticsTapApi tapEnv,
	    int preConStepNumber) throws TestException {
	String errorMessage = null;
	boolean status = false;
	LOGGER.info("#######################################################################################");
	LOGGER.info("PRE-CONDITION " + preConStepNumber + " : DESCRIPTION : Disable Codebig first by using webpa");
	LOGGER.info("PRE-CONDITION " + preConStepNumber
		+ " : ACTION : Set Device.DeviceInfo.X_RDKCENTRAL-COM_RFC.Feature.CodeBigFirst.Enable as false.");
	LOGGER.info("PRE-CONDITION " + preConStepNumber
		+ " : EXPTECTED : Codebig first Enable should be disabled using webpa.");
	LOGGER.info("#######################################################################################");
	errorMessage = "Failed to enable Codebig first.";
	try {
	    status = BroadBandCommonUtils.getWebPaValueAndVerify(device, tapEnv,
		    BroadBandWebPaConstants.WEBPA_PARAM_CODEBIG_FIRST_ENABLE, BroadBandTestConstants.FALSE);
	} catch (Exception exception) {
	    LOGGER.error(errorMessage + " : " + exception.getMessage());
	}
	try {
	    if (!status) {
		status = BroadBandWebPaUtils.setVerifyWebPAInPolledDuration(device, tapEnv,
			BroadBandWebPaConstants.WEBPA_PARAM_CODEBIG_FIRST_ENABLE, WebPaDataTypes.BOOLEAN.getValue(),
			BroadBandTestConstants.FALSE, BroadBandTestConstants.TWO_MINUTE_IN_MILLIS,
			BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
	    }
	} catch (Exception exception) {
	    LOGGER.error(errorMessage + " : " + exception.getMessage());
	}
	if (status) {
	    LOGGER.info("PRE-CONDITION " + preConStepNumber + " : ACTUAL : Codebig first disabled using webpa.");
	} else {
	    LOGGER.error("PRE-CONDITION " + preConStepNumber + " : ACTUAL : " + errorMessage);
	    throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION : " + preConStepNumber
		    + " FAILED : " + errorMessage);
	}

    }

    /**
     * Pre-Condition method to perform Factory reset the device .
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @Refactor Athira
     */
    public static boolean executePreConditionToFactoryResetDevice(Dut device, AutomaticsTapApi tapEnv,
	    int preConStepNumber) throws TestException {
	String errorMessage = null;
	boolean status = false;
	boolean isFactoryReset = false;
	/**
	 * PRE-CONDITION :Factory Reset
	 */
	LOGGER.info("#######################################################################################");
	LOGGER.info("PRE-CONDITION " + preConStepNumber + " : DESCRIPTION : PERFORM FACTORY RESET ON THE DEVICE.");
	LOGGER.info("PRE-CONDITION " + preConStepNumber + " : ACTION : PERFORM FACTORY RESET USING WEBPA.");
	LOGGER.info("PRE-CONDITION " + preConStepNumber + " : EXPTECTED : DEVICE MUST UNDERGO FACTORY RESET.");
	LOGGER.info("#######################################################################################");
	errorMessage = "UNABLE TO PERFORM WIFI FACTORY RESET OPERATION ON THE DEVICE. HENCE BLOCKING THE EXECUTION.";

	status = BroadBandCommonUtils.performFactoryResetWebPa(tapEnv, device);
	if (status) {
	    isFactoryReset = status;
	    LOGGER.info("PRE-CONDITION " + preConStepNumber + " : ACTUAL : FACTORY RESET SUCCESSFULLY PERFORMED.");
	} else {
	    LOGGER.error("PRE-CONDITION " + preConStepNumber + " : ACTUAL : " + errorMessage);
	    throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION : " + preConStepNumber
		    + " FAILED : " + errorMessage);
	}

	return isFactoryReset;
    }

    /**
     * Pre-Condition method to perform reactivate the device .
     * 
     * @param device
     *            {@link Dut}
     */
    public static boolean executePreConditionToReacitivateDevice(Dut device, AutomaticsTapApi tapEnv,
	    int preConStepNumber) throws TestException {
	String errorMessage = null;
	boolean status = false;
	boolean isReactivated = false;
	errorMessage = null;
	status = false;
	LOGGER.info("#######################################################################################");
	LOGGER.info("PRE-CONDITION " + preConStepNumber + " : DESCRIPTION : REACTIVATE THE ROUTER DEVICE");
	LOGGER.info("PRE-CONDITION " + preConStepNumber
		+ " : ACTION : SET VALUES TO 2.4GHz AND 5GHz - PRIVATE SSID AND PASSWORD");
	LOGGER.info("PRE-CONDITION " + preConStepNumber
		+ " : EXPECTED : THE ROUTER DEVICE SHOULD BE REACTIVATED SUCCESSFULLY");
	LOGGER.info("#######################################################################################");
	errorMessage = "FAILED TO REACTIVATE THE ROUTER DEVICE";
	status = false;
	try {
	    BroadBandWiFiUtils.reactivateDeviceUsingWebpaOrSnmp(tapEnv, device);
	    status = true;
	    isReactivated = status;
	} catch (TestException e) {
	    errorMessage = e.getMessage();
	}
	if (status) {
	    LOGGER.info("PRE-CONDITION " + preConStepNumber + " : ACTUAL: THE ROUTER DEVICE REACTIVATED SUCCESSFULLY.");
	} else {
	    LOGGER.error("PRE-CONDITION " + preConStepNumber + " : ACTUAL: " + errorMessage);
	    throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION : " + preConStepNumber
		    + " FAILED : " + errorMessage);
	}
	return isReactivated;
    }

    /**
     * Pre-Condition method to get the default channel values using webpa .
     * 
     * <li></li>
     * <li>Get the default Channel selection mode for 2.4 GHz</li>
     * <li>Get the default Channel No for for 2.4 GHz</li>
     * <li>Get the default Channel selection mode for 5 GHz</li>
     * <li>Get the default Channel No for for 5 GHz</li>
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @return defaultChannelValuesMap Its return the key and pair value for default channel values
     * @Refactor Sruthi Santhosh
     * 
     */
    public static HashMap<String, String> executePreconditionToGetTheDefaultChannelValues(Dut device,
	    AutomaticsTapApi tapEnv) {
	HashMap<String, String> defaultChannelValuesMap = new HashMap<String, String>();
	try {
	    defaultChannelValuesMap.put(BroadBandTestConstants.DEFAULT_CHANNEL_SELECTION_2GHZ,
		    tapEnv.executeWebPaCommand(device,
			    BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_2GHZ));
	    defaultChannelValuesMap.put(BroadBandTestConstants.DEFAULT_CHANNEL_NO_2GHZ, tapEnv.executeWebPaCommand(
		    device, BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_RADIO_CHANNEL_IN_2GHZ));
	    defaultChannelValuesMap.put(BroadBandTestConstants.DEFAULT_CHANNEL_SELECTION_5GHZ,
		    tapEnv.executeWebPaCommand(device,
			    BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_5GHZ));
	    defaultChannelValuesMap.put(BroadBandTestConstants.DEFAULT_CHANNEL_NO_5GHZ, tapEnv.executeWebPaCommand(
		    device, BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_RADIO_CHANNEL_IN_5GHZ));
	} catch (Exception exec) {
	    LOGGER.error("Failed to get the default channel Values" + exec.getMessage());
	    throw new TestException(
		    BroadBandTestConstants.PRE_CONDITION_ERROR + "FAILED TO GET THE DEFAULT CHANNEL VALUES");
	}
	return defaultChannelValuesMap;
    }

    /**
     * Pre-Condition method to verify Snmp Process Up or Not.
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * 
     */

    public static void executePreConditionForVerifySnmpProcessUp(Dut device, AutomaticsTapApi tapEnv,
	    int preConStepNumber) throws TestException {
	boolean status = false;
	String errorMessage = null;

	/**
	 * PRECONDITION :VERIFY SNMP PROCESS UP
	 */
	LOGGER.info("#######################################################################################");
	LOGGER.info("PRE-CONDITION " + preConStepNumber + " : DESCRIPTION : VERIFY SNMP PROCESS UP OR NOT ");
	LOGGER.info("PRE-CONDITION " + preConStepNumber
		+ " : ACTION : GET EXECUTE SNMP GET COMMAND TO GET SNMP VERSION FOR CHECKING SNMP PROCESS UP OR NOT");
	LOGGER.info("PRE-CONDITION " + preConStepNumber + " : EXPTECTED : SNMP PROCESS SHOULD BE UP");
	LOGGER.info("#######################################################################################");
	errorMessage = "SNMP PROCESS IS NOT UP";
	status = BroadBandSnmpUtils.checkSnmpIsUp(tapEnv, device);

	if (status) {
	    LOGGER.info("PRE-CONDITION :" + preConStepNumber + " : ACTUAL : VERIFIED SNMP PROCESS IS UP");
	} else {
	    LOGGER.error("PRE-CONDITION " + preConStepNumber + " : ACTUAL : " + errorMessage);
	    throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION : " + preConStepNumber
		    + " FAILED : " + errorMessage);
	}
	LOGGER.info("**********************************************************************************");

    }

    /**
     * Method to execute Pre-condition steps for Band steering Setting Same SSID , Password and Security Mode for Both
     * the radios Disabling Mesh
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of{@link Dut}
     * 
     */
    public static void executePreconditionForBandSteering(AutomaticsTapApi tapEnv, Dut device) {
	// Variable to store status of command execution
	boolean status = false;
	// Variable to store webpa parameters
	List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
	Map<String, String> webpaResponse = null;
	String ssid = BroadBandCommonUtils.concatStringUsingStringBuffer(
		BroadbandPropertyFileHandler.getPrivateWifiSsid(), CommonMethods.CHARACTER_UNDER_SCORE,
		Long.toString(System.currentTimeMillis()));
	String errorMessage = null;

	LOGGER.info("#######################################################################################");
	LOGGER.info(
		"PRE-CONDITION 1: DESCRIPTION : 1. The gateway should have same SSID, Password and Security mode for both the radios.\n2. Disable mesh if enabled on the gateway via WebPa\n 3. Disable AutoChannel on both the radios");
	LOGGER.info("PRE-CONDITION 1: ACTION : Execute commands: 1. "
		+ BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_NAME + " \n2. "
		+ BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_NAME + " \n3. "
		+ BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_PRIVATE_SSID_2GHZ_PASSPHRASE + "\n 4. "
		+ BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_PRIVATE_SSID_5GHZ_PASSPHRASE + "\n 5."
		+ BroadBandWebPaConstants.RdkBWifiParameters.SECURITY_MODE_2GHZ_PRIVATE_WIFI + " \n6."
		+ BroadBandWebPaConstants.RdkBWifiParameters.SECURITY_MODE_5GHZ_PRIVATE_WIFI + "\n 7. "
		+ BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_MESH_ENABLE + "\n 8. "
		+ BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_2GHZ + "\n 9."
		+ BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_5GHZ);
	LOGGER.info(
		"PRE-CONDITION 1: EXPECTED : The SSID, passphrase and security mode should get set successfully for both the radios.");
	LOGGER.info("#######################################################################################");
	errorMessage = "UNABLE TO SET SAME SSID, PASSWORD, SECURITY MODE AND UNABLE TO DISABLE MESH";

	try {

	    webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_NAME, ssid,
		    WebPaDataTypes.STRING.getValue()));
	    webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_NAME, ssid,
		    WebPaDataTypes.STRING.getValue()));
	    webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_PRIVATE_SSID_2GHZ_PASSPHRASE,
		    BroadbandPropertyFileHandler.getPrivateWifiPassPhrase(), WebPaDataTypes.STRING.getValue()));
	    webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_PRIVATE_SSID_5GHZ_PASSPHRASE,
		    BroadbandPropertyFileHandler.getPrivateWifiPassPhrase(), WebPaDataTypes.STRING.getValue()));
	    webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.RdkBWifiParameters.SECURITY_MODE_2GHZ_PRIVATE_WIFI.getParameterName(),
		    BroadBandWebPaConstants.RdkBWifiParameters.SECURITY_MODE_2GHZ_PRIVATE_WIFI.getDefaultValue(),
		    WebPaDataTypes.STRING.getValue()));
	    webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.RdkBWifiParameters.SECURITY_MODE_5GHZ_PRIVATE_WIFI.getParameterName(),
		    BroadBandWebPaConstants.RdkBWifiParameters.SECURITY_MODE_5GHZ_PRIVATE_WIFI.getDefaultValue(),
		    WebPaDataTypes.STRING.getValue()));
	    webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_2GHZ,
		    AutomaticsConstants.FALSE, WebPaDataTypes.BOOLEAN.getValue()));
	    webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_5GHZ,
		    AutomaticsConstants.FALSE, WebPaDataTypes.BOOLEAN.getValue()));
	    webpaResponse = tapEnv.executeMultipleWebPaSetCommands(device, webPaParameters);
	    status = null != webpaResponse && !webpaResponse.isEmpty()
		    && BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
			    BroadBandTestConstants.SUCCESS_TXT,
			    webpaResponse
				    .get(BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_2GHZ))
		    && BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
			    BroadBandTestConstants.SUCCESS_TXT,
			    webpaResponse
				    .get(BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_5GHZ))
		    && BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
			    BroadBandTestConstants.SUCCESS_TXT,
			    webpaResponse
				    .get(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_NAME))
		    && BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
			    BroadBandTestConstants.SUCCESS_TXT,
			    webpaResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_NAME))
		    && BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
			    BroadBandTestConstants.SUCCESS_TXT,
			    webpaResponse
				    .get(BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_PRIVATE_SSID_2GHZ_PASSPHRASE))
		    && BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
			    BroadBandTestConstants.SUCCESS_TXT,
			    webpaResponse
				    .get(BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_PRIVATE_SSID_5GHZ_PASSPHRASE))
		    && BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
			    BroadBandTestConstants.SUCCESS_TXT,
			    webpaResponse.get(BroadBandWebPaConstants.RdkBWifiParameters.SECURITY_MODE_2GHZ_PRIVATE_WIFI
				    .getParameterName()))
		    && BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
			    BroadBandTestConstants.SUCCESS_TXT,
			    webpaResponse.get(BroadBandWebPaConstants.RdkBWifiParameters.SECURITY_MODE_5GHZ_PRIVATE_WIFI
				    .getParameterName()))
		    && BroadBandMeshUtils.enableOrDisableMesh(device, tapEnv, false);
	} catch (Exception e) {
	    errorMessage += e.getMessage();
	}
	if (status) {
	    LOGGER.info("Waiting for 90 seconds after disabling mesh and auto channel...");
	    tapEnv.waitTill(
		    BroadBandTestConstants.ONE_MINUTE_IN_MILLIS + BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
	    LOGGER.info("PRE-CONDITION 1 : ACTUAL : SUCCESSFULLY ENABLED THE SECURED XFINITY WIFI ON GATEWAY DEVICE ");
	} else {
	    LOGGER.error("PRE-CONDITION 1 : ACTUAL : " + errorMessage);
	    throw new TestException(
		    BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION 1 : FAILED : " + errorMessage);
	}
    }

    /**
     * Precondition to connect to 2.4/5GHz Wifi client validate IPv4, Ipv6 ,connectivity status for both IP's
     * <ol>
     * <li>1 : OBTAIN A WIFI CLIENT ASSOSIATED WITH THE GATEWAY</li>
     * <li>2 : VERIFY THE CORRECT IPV4 ADDRESS FOR LAN CLIENT</li>
     * <li>3 : VERIFY THE CORRECT IPV6 ADDRESS FOR LAN CLIENT</li>
     * <li>4 : VERIFY THE INTERNET CONNECTIVITY IN THE CONNECTED LAN CLIENT USING IPV4 INTERFACE</li>
     * <li>5 : VERIFY THE INTERNET CONNECTIVITY IN THE CONNECTED LAN CLIENT USING IPV6 INTERFACE</li>
     * </ol>
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param wifiBand
     *            If band is '2.4GHZ',It will connect the client with 2.4GHz SSID If band is '5GHZ',It will connect the
     *            client with 5GHz SSID If band is '2.4GHZ OR 5GHZ',It will connect the client with 2.4GHz SSID OR 5GHz
     *            SSID
     * @param preConditionNumber
     * 
     * @return deviceConnectedWithWifi instance of connected device
     * @refactor Athira
     */
    public static Dut executePreConditionToVerifyWiFiClientStatus(Dut device, AutomaticsTapApi tapEnv, String wifiBand,
	    int preConditionNumber) throws TestException {
	LOGGER.debug("STARTING METHOD: executePreConditionToVerifyWiFiClientStatus");
	String errorMessage = null;
	boolean status = false;
	Dut deviceConnected = null;

	/**
	 * PRECONDITION 1 : OBTAIN A WIFI CLIENT ASSOSIATED WITH THE GATEWAY
	 */
	LOGGER.info("#######################################################################################");
	LOGGER.info("PRE-CONDITION " + preConditionNumber
		+ " : DESCRIPTION : OBTAIN A WIFI CLIENT ASSOSIATED WITH THE GATEWAY");
	LOGGER.info(
		"PRE-CONDITION " + preConditionNumber + " : ACTION : OBTAIN A WIFI CLIENT ASSOSIATED WITH THE GATEWAY");
	LOGGER.info("PRE-CONDITION " + preConditionNumber + " : EXPECTED : THE CONNECTION MUST BE SUCCESSFUL");
	LOGGER.info("#######################################################################################");
	errorMessage = "FAILED TO OBTAIN A WIFI CLIENT ASSOSIATED WITH THE GATEWAY";
	try {
	    if (wifiBand.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ)) {
		deviceConnected = BroadBandConnectedClientUtils
			.get2GhzWiFiCapableClientDeviceAndConnectToAssociated2GhzSsid(device, tapEnv);
	    } else if (wifiBand.equalsIgnoreCase(BroadBandTestConstants.BAND_5GHZ)) {
		deviceConnected = BroadBandConnectedClientUtils
			.get5GhzWiFiCapableClientDeviceAndConnectToAssociated5GhzSsid(device, tapEnv);
	    } else {
		deviceConnected = BroadBandConnectedClientUtils
			.get2GhzOr5GhzWiFiCapableClientDeviceAndConnectToCorrespondingSsid(device, tapEnv);
	    }
	} catch (TestException exception) {
	    errorMessage = exception.getMessage();
	    LOGGER.error(errorMessage);
	}
	status = (null != deviceConnected);
	if (status) {
	    LOGGER.info("PRE-CONDITION " + preConditionNumber
		    + " : ACTUAL : OBTAINED A WIFI CLIENT ASSOSIATED WITH THE GATEWAY SUCCESSFULLY.");
	} else {
	    LOGGER.error("PRE-CONDITION " + preConditionNumber + " : ACTUAL : " + errorMessage);
	    throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION " + preConditionNumber
		    + " : FAILED : " + errorMessage);
	}
	LOGGER.debug("STARTING METHOD: executePreConditionToVerifyWiFiClientStatus");
	return deviceConnected;
    }

    /**
     * Pre-Condition method to get the initial values for 2.4 & 5 GHZ radio's
     * 
     * @param device
     *            instance of{@link Dut}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param preCondNumber
     *            Pre condition number
     * @return WifiValues instance of {@link Map}
     * @refactor Govardhan
     */
    public static Map<String, String> executePreConditionToGetTheDefaultValuesForBothRadios(Dut device,
	    AutomaticsTapApi tapEnv, int preCondNumber) throws TestException {
	String errorMessage = null;
	boolean status = false;
	Map<String, String> wifiRadioValues = null;
	/**
	 * PRECONDITION : RETRIEVE THE 2.4 & 5 GHZ RADIO VALUES FROM DEVICE
	 */
	LOGGER.info("#######################################################################################");
	LOGGER.info("PRE-CONDITION " + preCondNumber
		+ " : DESCRIPTION : RETRIEVE VALUE FOR 2.4 & 5 GHZ RADIOS FROM DEVICE");
	LOGGER.info("PRE-CONDITION " + preCondNumber + " : ACTION : EXECUTE WEBPA COMMAND :\n "
		+ "1. Get the value for 2.4 GHz radio extension channel \n"
		+ "2. Get the value for 2.4 GHz radio beacon interval \n"
		+ "3. Get the value for 2.4 GHz radio basic rate \n"
		+ "4. Get the value for 2.4 GHz radio operating standard \n"
		+ "5. Get the value for 2.4 GHz radio transmit power \n"
		+ "6. Get the value for 2.4 GHz radio status \n" + "7. Get the value for 2.4 GHz radio channel \n"
		+ "8. Get the value for 2.4 GHz radio wireless channel \n"
		+ "9. Get the value for 2.4 GHz radio operating channel bandwidth \n"
		+ "10. Get the value for 2.4 GHz radio dfs enable \n"
		+ "11. Get the value for 5 GHz radio extension channel \n"
		+ "12. Get the value for 5 GHz radio beacon interval \n"
		+ "13. Get the value for 5 GHz radio basic rate \n"
		+ "14. Get the value for 5 GHz radio operating standard \n"
		+ "15. Get the value for 5 GHz radio transmit power \n" + "16. Get the value for 5 GHz radio status \n"
		+ "17. Get the value for 5 GHz radio channel \n"
		+ "18. Get the value for 5 GHz radio wireless channel \n"
		+ "19. Get the value for 5 GHz radio operating channel bandwidth \n"
		+ "20. Get the value for 5 GHz radio dfs enable ");
	LOGGER.info("PRE-CONDITION " + preCondNumber
		+ " : EXPECTED : MUST RETURN THE 2.4 & 5 GHZ RADIOS VALUES FROM DEVICE.");
	LOGGER.info("#######################################################################################");
	errorMessage = "UNABLE TO RETRIEVE THE 2.4 & 5 GHZ RADIOS VALUES FROM DEVICE.";
	try {
	    wifiRadioValues = tapEnv.executeMultipleWebPaGetCommands(device,
		    BroadBandWebPaConstants.WEBPA_PARAMS_FOR_BOTH_RADIOS);
	    status = !wifiRadioValues.isEmpty()
		    && BroadBandWebPaUtils.getAndVerifyMapValueIsNotNullOrEmpty(wifiRadioValues);
	} catch (Exception e) {
	    LOGGER.error("Exception occured while getting 2.4 and 5 GHz radio values.");
	    errorMessage = errorMessage + e.getMessage();
	}
	if (status) {
	    LOGGER.info("PRE-CONDITION " + preCondNumber
		    + " : ACTUAL : SUCESSFULLY RETRIEVED THE 2.4 & 5 GHZ RADIOS VALUES FROM DEVICE");
	} else {
	    LOGGER.error("PRE-CONDITION " + preCondNumber + " : ACTUAL : " + errorMessage);
	    throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION " + preCondNumber
		    + " : FAILED : " + errorMessage);
	}
	return wifiRadioValues;
    }

    /**
     * Pre-Condition method to get the initial values for common webpa parameters
     * 
     * @param device
     *            instance of{@link Dut}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param preCondNumber
     *            Pre condition number
     * @return Common Parameter values instance of {@link Map}
     */
    public static Map<String, String> executePreConditionToGetTheDefaultValuesForCommonParams(Dut device,
	    AutomaticsTapApi tapEnv, int preCondNumber) throws TestException {
	String errorMessage = null;
	boolean status = false;
	Map<String, String> commonParamValues = null;
	/**
	 * PRECONDITION : RETRIEVE COMMON PARAMETER VALUES FROM DEVICE
	 */
	LOGGER.info("#######################################################################################");
	LOGGER.info("PRE-CONDITION " + preCondNumber + " : DESCRIPTION : RETRIEVE COMMON PARAMETER VALUES FROM DEVICE");
	LOGGER.info("PRE-CONDITION " + preCondNumber + " : ACTION : EXECUTE WEBPA COMMAND :\n "
		+ "	1. Get the value for DMZ LAN IP Address \n" + "	2. Get the value for Device cloud UI status \n"
		+ "	3. Get the value for device control router enable status \n"
		+ "	4. Get the value for port forwarding status");
	LOGGER.info("PRE-CONDITION " + preCondNumber
		+ " : EXPECTED : MUST RETURN THE COMMON PARAMETER VALUES FROM DEVICE.");
	LOGGER.info("#######################################################################################");
	errorMessage = "UNABLE TO RETRIEVE THE COMMON PARAMETER VALUES FROM DEVICE.";
	try {
	    commonParamValues = tapEnv.executeMultipleWebPaGetCommands(device,
		    DeviceModeHandler.isBusinessClassDevice(device)
			    ? BroadBandWebPaConstants.WEBPA_PARAMS_FOR_COMMON_BUSI_DEVICE
			    : BroadBandWebPaConstants.WEBPA_PARAMS_FOR_COMMON_RESI_DEVICE);
	    status = !commonParamValues.isEmpty()
		    && BroadBandWebPaUtils.getAndVerifyMapValueIsNotNullOrEmpty(commonParamValues);
	} catch (Exception e) {
	    LOGGER.error("Exception occured while getting common parameter values.");
	    errorMessage = errorMessage + e.getMessage();
	}
	if (status) {
	    LOGGER.info("PRE-CONDITION " + preCondNumber
		    + " : ACTUAL : SUCESSFULLY RETRIEVED THE COMMON PARAMETER VALUES FROM DEVICE");
	} else {
	    LOGGER.error("PRE-CONDITION " + preCondNumber + " : ACTUAL : " + errorMessage);
	    throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION " + preCondNumber
		    + " : FAILED : " + errorMessage);
	}
	return commonParamValues;
    }
    
    /**
     * Pre-Condition method to ENABLE/DISABLE Mesh via XPC  
     * 
     * @param device
     *            instance of{@link Dut}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param toEnable
     * 		  True to Enable ,False to Disable
     * @param preCondNumber
     *            Pre condition number
     * @refactor Alan_Bivera
     * 
     */
    public static void  executePreConditionToEnableOrDisable(Dut device, AutomaticsTapApi tapEnv, boolean toEnable, int preConStepNumber)
	    throws TestException {
	String errorMessage = null;
	boolean status = false;
	LOGGER.info("#######################################################################################");
	LOGGER.info("PRE-CONDITION " + preConStepNumber + " : DESCRIPTION : " + (toEnable ? "ENABLE" : "DISABLE")
		+ " THE MESH STATUS");
	LOGGER.info("PRE-CONDITION " + preConStepNumber + " : ACTION : " + (toEnable ? "ENABLE" : "DISABLE")
		+ " THE MESH STATUS " + BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_MESH_ENABLE
		+ " STATUS AS " + (toEnable ? "TRUE" : "FALSE") + " USING WEBPA");
	LOGGER.info("PRE-CONDITION " + preConStepNumber + " : EXPECTED : MESH STATUS MUST BE "
		+ (toEnable ? "ENABLED" : "DISABLED"));
	LOGGER.info("#######################################################################################");
	errorMessage = "UNABLE TO " + (toEnable ? "ENABLE" : "DISABLE") + " THE MESH STATUS";

	 status = BroadBandMeshUtils.enableOrDisableMesh(device, tapEnv, false);
	if (status) {
	    LOGGER.info("PRE-CONDITION " + preConStepNumber + " : ACTUAL : SUCCESSFULLY "
		    + (toEnable ? "ENABLED" : "DISABLED") + " THE MESH STATUS");
	} else {
	    LOGGER.error("PRE-CONDITION " + preConStepNumber + " : ACTUAL : " + errorMessage);
	    throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION " + preConStepNumber
		    + " : FAILED : " + errorMessage);
	}
    }

}

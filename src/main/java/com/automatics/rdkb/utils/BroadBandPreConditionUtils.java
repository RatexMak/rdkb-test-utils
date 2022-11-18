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

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Device;
import com.automatics.device.Dut;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandConnectedClientTestConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.webpa.WebPaParameter;
import com.automatics.rdkb.utils.cdl.FirmwareDownloadUtils;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpUtils;
import com.automatics.rdkb.utils.telemetry.BroadBandTelemetry2Utils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.wifi.BroadBandWiFiUtils;
import com.automatics.rdkb.utils.wifi.BroadBandWifiWhixUtils;
import com.automatics.rdkb.utils.wifi.connectedclients.BroadBandConnectedClientUtils;

public class BroadBandPreConditionUtils {

	/** SLF4J logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandPreConditionUtils.class);

	/**
	 * Pre condition to reboot the device and wait for ip accusition
	 * 
	 * @param device        {@link Dut}
	 * @param tapEnv        {@link AutomaticsTapApi}
	 * @param preCondNumber Pre condition number
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
	 * @param device            instance of{@link Dut}
	 * @param tapEnv            instance of {@link AutomaticsTapApi}
	 * @param meshInitialStatus mesh initial status before stopping mesh
	 * @param preCondNumber     Pre condition number
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
	 * @param device {@link Dut}
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
	 * @param device {@link Dut}
	 * @param tapEnv {@link AutomaticsTapApi}
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
		if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
		    BroadBandCommonUtils.getAtomsyncUptimeStatus(device, tapEnv);
		}
		status = BroadBandCommonUtils.performFactoryResetWebPaByPassingTriggerTime(tapEnv, device,
			BroadBandTestConstants.EIGHT_MINUTE_IN_MILLIS);
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
	 * @param device {@link Dut}
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
	 * @param device {@link Dut}
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @return defaultChannelValuesMap Its return the key and pair value for default
	 *         channel values
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
	 * @param device {@link Dut}
	 * @param tapEnv {@link AutomaticsTapApi}
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
	 * Method to execute Pre-condition steps for Band steering Setting Same SSID ,
	 * Password and Security Mode for Both the radios Disabling Mesh
	 * 
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @param device instance of{@link Dut}
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
			LOGGER.info("PRE-CONDITION 1 : ACTUAL : SUCCESSFULLY ENABLED THE SECURED PUBLIC WIFI ON GATEWAY DEVICE ");
		} else {
			LOGGER.error("PRE-CONDITION 1 : ACTUAL : " + errorMessage);
			throw new TestException(
					BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION 1 : FAILED : " + errorMessage);
		}
	}

	/**
	 * Precondition to connect to 2.4/5GHz Wifi client validate IPv4, Ipv6
	 * ,connectivity status for both IP's
	 * <ol>
	 * <li>1 : OBTAIN A WIFI CLIENT ASSOSIATED WITH THE GATEWAY</li>
	 * <li>2 : VERIFY THE CORRECT IPV4 ADDRESS FOR LAN CLIENT</li>
	 * <li>3 : VERIFY THE CORRECT IPV6 ADDRESS FOR LAN CLIENT</li>
	 * <li>4 : VERIFY THE INTERNET CONNECTIVITY IN THE CONNECTED LAN CLIENT USING
	 * IPV4 INTERFACE</li>
	 * <li>5 : VERIFY THE INTERNET CONNECTIVITY IN THE CONNECTED LAN CLIENT USING
	 * IPV6 INTERFACE</li>
	 * </ol>
	 * 
	 * @param device             {@link Dut}
	 * @param tapEnv             instance of {@link AutomaticsTapApi}
	 * @param wifiBand           If band is '2.4GHZ',It will connect the client with
	 *                           2.4GHz SSID If band is '5GHZ',It will connect the
	 *                           client with 5GHz SSID If band is '2.4GHZ OR
	 *                           5GHZ',It will connect the client with 2.4GHz SSID
	 *                           OR 5GHz SSID
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
	 * @param device        instance of{@link Dut}
	 * @param tapEnv        instance of {@link AutomaticsTapApi}
	 * @param preCondNumber Pre condition number
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
	 * @param device        instance of{@link Dut}
	 * @param tapEnv        instance of {@link AutomaticsTapApi}
	 * @param preCondNumber Pre condition number
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
	 * @param device        instance of{@link Dut}
	 * @param tapEnv        instance of {@link AutomaticsTapApi}
	 * @param toEnable      True to Enable ,False to Disable
	 * @param preCondNumber Pre condition number
	 * @refactor Alan_Bivera
	 * 
	 */
	public static void executePreConditionToEnableOrDisable(Dut device, AutomaticsTapApi tapEnv, boolean toEnable,
			int preConStepNumber) throws TestException {
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

	/**
	 * Pre-Condition method to check that device has syndication partner Id
	 * 
	 * @param device        instance of{@link Dut}
	 * @param tapEnv        instance of {@link AutomaticsTapApi}
	 * @param preCondNumber Pre condition number
	 * @refactor Athira
	 */
	public static void executePreConditionToCheckSyndicationPartnerOnDevice(Dut device, AutomaticsTapApi tapEnv,
			int preConStepNumber) throws TestException {
		String errorMessage = null;
		boolean status = false;
		LOGGER.info("#######################################################################################");
		LOGGER.info("PRE-CONDITION " + preConStepNumber + ": DESCRIPTION : Verify the gateway has syndication partner");
		LOGGER.info(
				"PRE-CONDITION " + preConStepNumber + ": ACTION : Execute webpa command to get Syndication Partner");
		LOGGER.info(
				"PRE-CONDITION " + preConStepNumber + ": EXPECTED : PartnerId retrived should be syndication partner.");
		LOGGER.info("#######################################################################################");
		errorMessage = "Failed to verify the syndication partner for the device";
		status = BroadBandCommonUtils.isPartnerNameIsSyndication(tapEnv, device);
		if (status) {
			LOGGER.info("PRE-CONDITION " + preConStepNumber
					+ ": ACTUAL :Sucessfully verified that the Device has syndication Partner");
		} else {
			LOGGER.error("PRE-CONDITION " + preConStepNumber + ": ACTUAL : Device doest not have syndication partner");
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + errorMessage);
		}
	}

	/**
	 * Pre-Condition method to perform Factory reset and reactivate the device .
	 * 
	 * @param device {@link Dut}
	 * @refactor Alan_Bivera
	 */
	public static void executePreConditionToFactoryResetAndReacitivateDevice(Dut device, AutomaticsTapApi tapEnv,
			int preConStepNumber, boolean isReactivated) throws TestException {
		String errorMessage = null;
		boolean status = false;
		/**
		 * PRE-CONDITION :Factory Reset
		 */
		LOGGER.info("#######################################################################################");
		LOGGER.info("PRE-CONDITION " + preConStepNumber + " : DESCRIPTION : PERFORM FACTORY RESET ON THE DEVICE.");
		LOGGER.info("PRE-CONDITION " + preConStepNumber + " : ACTION : PERFORM FACTORY RESET USING WEBPA.");
		LOGGER.info("PRE-CONDITION " + preConStepNumber + " : EXPTECTED : DEVICE MUST UNDERGO FACTORY RESET.");
		LOGGER.info("#######################################################################################");
		errorMessage = "UNABLE TO PERFORM WIFI FACTORY RESET OPERATION ON THE DEVICE. HENCE BLOCKING THE EXECUTION.";
		if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
			BroadBandCommonUtils.getAtomDeviceUptimeStatus(device, tapEnv);
		}
		status = BroadBandCommonUtils.performFactoryResetWebPaByPassingTriggerTime(tapEnv, device,
				BroadBandTestConstants.EIGHT_MINUTE_IN_MILLIS);
		if (status) {
			LOGGER.info("PRE-CONDITION " + preConStepNumber + " : ACTUAL : FACTORY RESET SUCCESSFULLY PERFORMED.");
		} else {
			LOGGER.error("PRE-CONDITION " + preConStepNumber + " : ACTUAL : " + errorMessage);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION : " + preConStepNumber
					+ " FAILED : " + errorMessage);
		}
		/**
		 * PRE-CONDITION 2 :WebPA Process Status
		 */
		preConStepNumber++;
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
	}

	/**
	 * Pre-Condition method to get the dhcp ipv4 values using webpa .
	 * 
	 * <li></li>
	 * <li>Get the default Ipv4 SubnetMask Address</li>
	 * <li>Get the default Ipv4 BeginAddress Address</li>
	 * <li>Get the default Ipv4 EndingAddress Address</li>
	 * <li>Get the default Ipv4 DhcpLeaseTime Address</li>
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @return defaultDchpIpv4ValuesMap Its return the key and pair value for
	 *         default dhcp v4 values
	 * @refactor Govardhan
	 */
	public static HashMap<String, String> executePreconditionToGetTheDefaultDchpIpv4Values(Dut device,
			AutomaticsTapApi tapEnv) {
		HashMap<String, String> defaultDchpIpv4ValuesMap = new HashMap<String, String>();
		try {
			defaultDchpIpv4ValuesMap.put(BroadBandTestConstants.DEFAULT_IPV4_SUBNET_MASK,
					tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_LAN_IPv4_SUBNET_MASK));
			defaultDchpIpv4ValuesMap.put(BroadBandTestConstants.DEFAULT_DHCPV4_BEGIN_ADDRESS,
					tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_DHCP_MINADDRESS));
			defaultDchpIpv4ValuesMap.put(BroadBandTestConstants.DEFAULT_DHCPV4_ENDING_ADDRESS,
					tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_DHCP_MAXADDRESS));
			defaultDchpIpv4ValuesMap.put(BroadBandTestConstants.DEFAULT_DHCPLEASE_TIME,
					tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_DHCP_LEASETIME));
		} catch (Exception exec) {
			LOGGER.error("Failed to get the default DHCP IPv4 Values" + exec.getMessage());
			throw new TestException(
					BroadBandTestConstants.PRE_CONDITION_ERROR + "FAILED TO GET THE DEFAULT DHCP IPV4 VALUES");
		}
		return defaultDchpIpv4ValuesMap;
	}

	/**
	 * Precondition to validate Wifi client IPv4,Ipv6 ,connectivity status for
	 * 2.4GHZ OR 5GHZ
	 * <ol>
	 * <li>1 : OBTAIN A WIFI CLIENT ASSOSIATED WITH THE GATEWAY</li>
	 * <li>2 : VERIFY THE CORRECT IPV4 ADDRESS FOR LAN CLIENT</li>
	 * <li>3 : VERIFY THE CORRECT IPV4 ADDRESS FOR LAN CLIENT</li>
	 * <li>4 : VERIFY THE INTERNET CONNECTIVITY IN THE CONNECTED LAN CLIENT USING
	 * IPV4 INTERFACE</li>
	 * <li>5 : VERIFY THE INTERNET CONNECTIVITY IN THE CONNECTED LAN CLIENT USING
	 * IPV6 INTERFACE</li>
	 * </ol>
	 * 
	 * @param device   {@link Dut}
	 * @param tapEnv   instance of {@link AutomaticsTapApi}
	 * @param wifiBand If band is '2.4GHZ',It will connect the client with 2.4GHz
	 *                 SSID If band is '5GHZ',It will connect the client with 5GHz
	 *                 SSID If band is '2.4GHZ OR 5GHZ',It will connect the client
	 *                 with 2.4GHz SSID OR 5GHz SSID
	 * 
	 * @return deviceConnectedWithEthernet instance of connected device
	 * @refactor Alan_Bivera
	 * 
	 */
	public static Dut executePreConditionToVerifyWiFiClientStatus(Dut device, AutomaticsTapApi tapEnv, String wifiBand)
			throws TestException {
		String errorMessage = "";
		boolean status = false;
		Dut deviceConnected = null;

		BroadBandResultObject result = null; // stores test result and error
		/**
		 * PRECONDITION 1 : OBTAIN A WIFI CLIENT ASSOSIATED WITH THE GATEWAY
		 */
		LOGGER.info("#######################################################################################");
		LOGGER.info("PRE-CONDITION 1 : DESCRIPTION : OBTAIN A WIFI CLIENT ASSOSIATED WITH THE GATEWAY");
		LOGGER.info("PRE-CONDITION 1 : ACTION : OBTAIN A WIFI CLIENT ASSOSIATED WITH THE GATEWAY");
		LOGGER.info("PRE-CONDITION 1 : EXPECTED : THE CONNECTION MUST BE SUCCESSFUL");
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
			LOGGER.info("PRE-CONDITION 1 : ACTUAL : OBTAINED A WIFI CLIENT ASSOSIATED WITH THE GATEWAY SUCCESSFULLY.");
		} else {
			LOGGER.error("PRE-CONDITION 1 : ACTUAL : " + errorMessage);
			throw new TestException(
					BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION 1 : FAILED : " + errorMessage);
		}

		/**
		 * PRECONDITION 2 : VERIFY THE CORRECT IPV4 ADDRESS FOR LAN CLIENT
		 */
		status = false;
		LOGGER.info("#####################################################################################");
		LOGGER.info("PRE-CONDITION 2 : DESCRIPTION :VERIFY THE CORRECT IPV4 ADDRESS FOR WIFI CLIENT");
		LOGGER.info(
				"PRE-CONDITION 2 : ACTION : EXECUTE COMMAND, WINDOWS : ipconfig |grep -A 10 'Wireless adapter Wi-Fi' |grep -i 'IPv4 Address' or LINUX : ifconfig | grep 'inet' or ON THE CONNECTED CLIENT");
		LOGGER.info("PRE-CONDITION 2 : EXPECTED : IT SHOULD RETURN THE CORRECT IPV4 ADDRESS FOR WIFI CLIENT ");
		LOGGER.info("#####################################################################################");
		errorMessage = "UNABLE TO GET THE CORRECT IPV4 ADDRESS FROM CLIENT  ";
		status = BroadBandConnectedClientUtils.verifyIpv4AddressForWiFiOrLanInterfaceConnectedWithRdkbDevice(
				((Device) deviceConnected).getOsType(), deviceConnected, tapEnv);
		if (status) {
			LOGGER.info("PRE-CONDITION 2 : ACTUAL : SUCCESSFYLLY VERIFIED CORRECT IPV4 ADDRESS FROM WIFI CLIENT ");
		} else {
			LOGGER.error("PRE-CONDITION 2 : ACTUAL : " + errorMessage);
			throw new TestException(
					BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION 2 : FAILED : " + errorMessage);
		}
		/**
		 * PRECONDITION 3 : VERIFY THE CORRECT IPV6 ADDRESS FOR WIFI CLIENT
		 */
		status = false;
		LOGGER.info("#####################################################################################");
		LOGGER.info("PRE-CONDITION 3 : DESCRIPTION :VERIFY THE CORRECT IPV4 ADDRESS FOR WIFI CLIENT");
		LOGGER.info(
				"PRE-CONDITION 3 : ACTION : EXECUTE COMMAND, WINDOWS : ipconfig |grep -A 10 'Wireless adapter Wi-Fi' |grep -i 'IPv6 Address' or LINUX : ifconfig | grep 'inet6' or ON THE CONNECTED CLIENT");
		LOGGER.info("PRE-CONDITION 3 : EXPECTED : IT SHOULD RETURN THE CORRECT IPV6 ADDRESS FOR WIFI CLIENT ");
		LOGGER.info("#####################################################################################");
		errorMessage = "UNABLE TO GET THE CORRECT IPV6 ADDRESS FROM CLIENT  ";
		status = BroadBandConnectedClientUtils.verifyIpv6AddressForWiFiOrLanInterfaceConnectedWithRdkbDevice(
				((Device) deviceConnected).getOsType(), deviceConnected, tapEnv);
		if (status) {
			LOGGER.info("PRE-CONDITION 3 : ACTUAL : SUCCESSFYLLY VERIFIED CORRECT IPV6 ADDRESS FROM LAN CLIENT ");
		} else {
			LOGGER.error("PRE-CONDITION 3 : ACTUAL : " + errorMessage);
			throw new TestException(
					BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION 3 : FAILED : " + errorMessage);
		}

		/**
		 * PRECONDITION 4 : VERIFY THE INTERNET CONNECTIVITY IN THE WIFI CLIENT
		 * INTERFACE USING IPV4 .
		 */
		status = false;
		LOGGER.info("#######################################################################################");
		LOGGER.info(
				"PRE-CONDITION 4 : DESCRIPTION : VERIFY THE INTERNET CONNECTIVITY IN THE CONNECTED WIFI CLIENT USING IPV4 INTERFACE");
		LOGGER.info(
				"PRE-CONDITION 4 : ACTION : EXECUTE COMMAND, WINDOWS : curl -4 -v 'www.google.com'  | grep '200 OK' OR ping -4 -n 5 google.com, LINUX : curl -4 -f --interface <interfaceName> www.google.com | grep '200 OK' OR ping -4 -n 5 google.com ON THE CONNECTED LAN CLIENT");
		LOGGER.info("PRE-CONDITION 4 : EXPECTED : THE INTERNET CONNECTIVITY MUST BE AVAILABLE INTERFACE USING IPV4 ");
		LOGGER.info("#######################################################################################");
		errorMessage = "NOT ABLE TO ACCESS THE SITE 'www.google.com' FROM WIFI CLIENT WITH USING IPV4";
		long startTime = System.currentTimeMillis();
		do {
			result = BroadBandConnectedClientUtils.verifyInternetIsAccessibleInConnectedClientUsingCurl(tapEnv,
					deviceConnected,
					BroadBandTestConstants.URL_HTTPS + BroadBandTestConstants.STRING_GOOGLE_HOST_ADDRESS,
					BroadBandTestConstants.IP_VERSION4);
			status = result.isStatus();
			errorMessage = result.getErrorMessage();
		} while (!status && (System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
		if (!status) {
			errorMessage = "PIGN OPERATION FAILED TO ACCESS THE SITE 'www.google.com' USING IPV4 ";
			status = ConnectedNattedClientsUtils.verifyPingConnectionForIpv4AndIpv6(deviceConnected, tapEnv,
					BroadBandTestConstants.PING_TO_GOOGLE, BroadBandTestConstants.IP_VERSION4);
		}
		if (status) {
			LOGGER.info(
					"PRE-CONDITION 4 : ACTUAL : CONNECTED WIFI CLIENT HAS INTERNET CONNECTIVITY USING IPV4 INTERFACE");
		} else {
			LOGGER.error("PRE-CONDITION 4 : ACTUAL : " + errorMessage);
			throw new TestException(
					BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION 4 : FAILED : " + errorMessage);
		}

		/**
		 * PRECONDITION 5 : VERIFY THE INTERNET CONNECTIVITY IN THE WIFI CLIENT
		 * INTERFACE USING IPV6 .
		 */
		status = false;
		LOGGER.info("#######################################################################################");
		LOGGER.info(
				"PRE-CONDITION 5 : DESCRIPTION : VERIFY THE INTERNET CONNECTIVITY IN THE CONNECTED WIFI CLIENT USING IPV6 INTERFACE");
		LOGGER.info(
				"PRE-CONDITION 5 : ACTION : EXECUTE COMMAND, WINDOWS : curl -4 -v 'www.google.com'  | grep '200 OK' OR ping -4 -n 5 google.com, LINUX : curl -4 -f --interface <interfaceName> www.google.com | grep '200 OK' OR ping -4 -n 5 google.com ON THE CONNECTED LAN CLIENT");
		LOGGER.info("PRE-CONDITION 5 : EXPECTED : THE INTERNET CONNECTIVITY MUST BE AVAILABLE INTERFACE USING IPV6 ");
		LOGGER.info("#######################################################################################");
		errorMessage = "NOT ABLE TO ACCESS THE SITE 'www.google.com' FROM WIFI CLIENT WITH USING IPV6";
		if (DeviceModeHandler.isFibreDevice(device)) {
			LOGGER.info(
					"PRE-CONDITION 5 : ACTUAL : IPV6 INTERNET CONNECTIVITY VERIFICATION NOT APPLICABLE FOR fibre DEVICES");
		} else {
			startTime = System.currentTimeMillis();
			do {
				result = BroadBandConnectedClientUtils.verifyInternetIsAccessibleInConnectedClientUsingCurl(tapEnv,
						deviceConnected,
						BroadBandTestConstants.URL_HTTPS + BroadBandTestConstants.STRING_GOOGLE_HOST_ADDRESS,
						BroadBandTestConstants.IP_VERSION6);
				status = result.isStatus();
				errorMessage = result.getErrorMessage();
			} while (!status && (System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS
					&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
			if (!status) {
				errorMessage = "PIGN OPERATION FAILED TO ACCESS THE SITE 'www.google.com' USING IPV6 ";
				status = ConnectedNattedClientsUtils.verifyPingConnectionForIpv4AndIpv6(deviceConnected, tapEnv,
						BroadBandTestConstants.PING_TO_GOOGLE, BroadBandTestConstants.IP_VERSION6);
			}
			if (status) {
				LOGGER.info(
						"PRE-CONDITION 5 : ACTUAL : CONNECTED WIFI CLIENT HAS INTERNET CONNECTIVITY USING IPV6 INTERFACE");
			} else {
				LOGGER.error("PRE-CONDITION 5 : ACTUAL : " + errorMessage);
				throw new TestException(
						BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION 5 : FAILED : " + errorMessage);
			}
		}
		return deviceConnected;
	}

	/**
	 * Pre-Condition method to verify the below condition in Ethernet connected
	 * Client .
	 * 
	 * <li>PRE-CONDITION 1 : Verify Ethernet client is connected with Gateway</li>
	 * <li>PRE-CONDITION 2 : Verify IPv4 is assigned on the Ethernet client</li>
	 * <li>PRE-CONDITION 3 : Verify IPv6 is assigned on the Ethernet client</li>
	 * <li>PRE-CONDITION 4 : Verify Internet is accessible by using Interface IPv4
	 * on the Ethernet client</li>
	 * <li>PRE-CONDITION 5 : Verify Internet is accessible by using Interface IPv6
	 * on the Ethernet client</li>
	 * <li>PRE-CONDITION 6 : Verify the gateway Ip address</li>
	 * 
	 * @param device {@link Dut}
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @return deviceConnectedWithEthernet instance of connected device
	 * 
	 */
	public static Dut executePreConditionToVerifyLanClientStatus(Dut device, AutomaticsTapApi tapEnv)
			throws TestException {
		Dut deviceConnectedWithEthernet = executePreConditionToVerifyLanClientStatus(device, tapEnv,
				BroadBandTestConstants.CONSTANT_1);

		return deviceConnectedWithEthernet;
	}

	/**
	 * Precondition to connect to Ethernet client, Validate IPv4, Ipv6, connectivity
	 * status for both IPs's.
	 * <ol>
	 * <li>1 : OBTAIN A ETHERNET CLIENT ASSOSIATED WITH THE GATEWAY</li>
	 * <li>2 : VERIFY THE CORRECT IPV4 ADDRESS FOR LAN CLIENT</li>
	 * <li>3 : VERIFY THE CORRECT IPV6 ADDRESS FOR LAN CLIENT</li>
	 * <li>4 : VERIFY THE INTERNET CONNECTIVITY IN THE CONNECTED LAN CLIENT USING
	 * IPV4 INTERFACE</li>
	 * <li>5 : VERIFY THE INTERNET CONNECTIVITY IN THE CONNECTED LAN CLIENT USING
	 * IPV6 INTERFACE</li>
	 * </ol>
	 * 
	 * @param device             {@link Dut}
	 * @param tapEnv             instance of {@link AutomaticsTapApi}
	 * @param preConditionNumber Pre condition number
	 * @return deviceConnectedWithEthernet instance of connected device
	 * 
	 */
	public static Dut executePreConditionToVerifyLanClientStatus(Dut device, AutomaticsTapApi tapEnv,
			int preConditionNumber) throws TestException {
		String errorMessage = null;
		boolean status = false;
		Dut deviceConnectedWithEthernet = null;
		BroadBandResultObject result = null; // stores test result and error

		/**
		 * PRECONDITION 1 : OBTAIN A ETHERNET CLIENT ASSOSIATED WITH THE GATEWAY
		 */
		LOGGER.info("#######################################################################################");
		LOGGER.info("PRE-CONDITION " + preConditionNumber
				+ " : DESCRIPTION : OBTAIN A ETHERNET CLIENT ASSOSIATED WITH THE GATEWAY");
		LOGGER.info("PRE-CONDITION " + preConditionNumber
				+ " : ACTION : OBTAIN A ETHERNET CLIENT ASSOSIATED WITH THE GATEWAY");
		LOGGER.info("PRE-CONDITION " + preConditionNumber + " : EXPTECTED : THE CONNECTION MUST BE SUCCESSFUL");
		LOGGER.info("#######################################################################################");
		errorMessage = "FAILED TO OBTAIN A ETHERNET CLIENT ASSOSIATED WITH THE GATEWAY";
		try {
			deviceConnectedWithEthernet = BroadBandConnectedClientUtils.getEthernetConnectedClient(tapEnv, device);
		} catch (TestException exception) {
			errorMessage = exception.getMessage();
			LOGGER.error(errorMessage);
		}
		status = (null != deviceConnectedWithEthernet);
		if (status) {
			LOGGER.info("PRE-CONDITION " + preConditionNumber
					+ " : ACTUAL : OBTAINED A ETHERNET CLIENT ASSOSIATED WITH THE GATEWAY SUCCESSFULLY.");
		} else {
			LOGGER.error("PRE-CONDITION " + preConditionNumber + " : ACTUAL : " + errorMessage);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION " + preConditionNumber
					+ " : FAILED : " + errorMessage);
		}
		return deviceConnectedWithEthernet;
	}

	/**
	 * Pre-Condition method to used to enable the TR69 Configuration
	 * 
	 * @param device        {@link Dut}
	 * @param tapEnv        {@link AutomaticsTapApi}
	 * @param preCondNumber Pre condition number
	 * 
	 * @refactor yamini.s
	 * 
	 */
	public static void executePreConditionToEnableTR69Configuration(Dut device, AutomaticsTapApi tapEnv,
			int preConStepNumber) throws TestException {
		String errorMessage = null;
		boolean status = false;
		LOGGER.info("#######################################################################################");
		LOGGER.info("PRE-CONDITION " + preConStepNumber + " : DESCRIPTION : Enable the TR69 configuration");
		LOGGER.info("PRE-CONDITION " + preConStepNumber + " : ACTION : Set "
				+ BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_ENABLECWMP + " as TRUE.");
		LOGGER.info("PRE-CONDITION " + preConStepNumber
				+ " : EXPTECTED : Enable the TR69 configuration configuration should be success");
		LOGGER.info("#######################################################################################");
		errorMessage = device.getModel() + " is not a syndication partner device ";
		status = BroadBandCommonUtils.verifySyndicatePartnerIdOnDevice(device, tapEnv);
		if (status) {
			status = false;
			errorMessage = "Failed to Enable the TR69 configuration";
			try {
				status = BroadBandCommonUtils.getWebPaValueAndVerify(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_ENABLECWMP,
						BroadBandTestConstants.TRUE);
				LOGGER.info("GET DEVICE_MANAGEMENTSERVER_ENABLECWMP :" + status);
			} catch (Exception exception) {
				LOGGER.error(errorMessage + " : " + exception.getMessage());
			}
			try {
				if (!status) {
					LOGGER.info("TRYING TO SET DEVICE_MANAGEMENTSERVER_ENABLECWMP :" + status);
					status = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv,
							BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_ENABLECWMP,
							WebPaDataTypes.BOOLEAN.getValue(), BroadBandTestConstants.TRUE)
							&& CommonMethods.isNotNull(BroadBandCommonUtils.searchLogFiles(tapEnv, device,
									BroadBandTestConstants.ACS_REQUEST_COMPLETE,
									BroadBandTestConstants.RDKLOGS_LOGS_TR69LOG_TXT_0,
									BroadBandTestConstants.THREE_MINUTE_IN_MILLIS,
									BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
				}
			} catch (Exception exception) {
				LOGGER.error(errorMessage + " : " + exception.getMessage());
			}
		}
		if (status) {
			LOGGER.info("PRE-CONDITION " + preConStepNumber
					+ " : ACTUAL : TR69 configuration configuration enabled successfully.");
		} else {
			LOGGER.error("PRE-CONDITION " + preConStepNumber + " : ACTUAL : " + errorMessage);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION " + preConStepNumber
					+ " : FAILED : " + errorMessage);
		}
	}

	/**
	 * pre-Condition method to verify the default radio status
	 * 
	 * @param device {@link Dut}
	 * 
	 * @refactor yamini.s
	 */
	public static void executePreConditionToVerifyRadioStatus(Dut device, AutomaticsTapApi tapEnv, int preConStepNumber)
			throws TestException {
		String errorMessage = null;
		boolean status = false;
		/**
		 * PRECONDITION 1 :Enable Private 2.4 GHz SSID via WebPA
		 */
		errorMessage = null;
		status = false;
		LOGGER.info("#######################################################################################");
		LOGGER.info("PRE-CONDITION " + preConStepNumber
				+ " : DESCRIPTION : SET AND VERIFY WHETHER PRIVATE 2.4 GHZ SSID 'DEVICE.WIFI.SSID.10001.ENABLE' IS ENABLED ");
		LOGGER.info("PRE-CONDITION " + preConStepNumber
				+ " : ACTION : SET AND VERIFY WHETHER PRIVATE 2.4 GHZ SSID 'DEVICE.WIFI.SSID.10001.ENABLE' IS ENABLED USING WEBPA ");
		LOGGER.info("PRE-CONDITION " + preConStepNumber
				+ " : EXPTECTED : DEVICE SHOULD BE ENABLED WITH PRIVATE 2.4 GHZ SSID AND RESPONSE SHOULD BE TRUE");
		LOGGER.info("#######################################################################################");
		errorMessage = "NOT ABLE TO ENABLE THE 2.4GHZ PRIVATE SSID ON THIS DEVICE - HENCE BLOCKING THE EXECUTION.";
		try {
			status = BroadBandCommonUtils.getWebPaValueAndVerify(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_WIFI_2_4_RADIO_ENABLE, BroadBandTestConstants.TRUE);
		} catch (TestException exception) {
			status = false;
			LOGGER.error(errorMessage + " : " + exception.getMessage());
		}
		if (!status) {
			errorMessage = "UNABLE TO SET THE 2.4 GHZ RADIO STATUS AS 'TRUE'.";
			status = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_WIFI_2_4_RADIO_ENABLE, BroadBandTestConstants.CONSTANT_3,
					BroadBandTestConstants.TRUE);
		}
		if (status) {
			LOGGER.info("PRE-CONDITION " + preConStepNumber
					+ " : ACTUAL : PRIVATE 2.4 GHZ SSID ENABLED IN GATEWAY DEVICE.");
		} else {
			LOGGER.error("PRE-CONDITION " + preConStepNumber + " : ACTUAL : " + errorMessage);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION : " + preConStepNumber
					+ " FAILED : " + errorMessage);
		}
		LOGGER.info("#######################################################################################");
		/**
		 * PRECONDITION :Enable Private 5 GHz SSID via WebPA
		 */
		preConStepNumber++;
		errorMessage = null;
		status = false;
		LOGGER.info("#######################################################################################");
		LOGGER.info("PRE-CONDITION " + preConStepNumber
				+ " : DESCRIPTION : SET AND VERIFY WHETHER PRIVATE 5 GHZ SSID 'DEVICE.WIFI.SSID.10101.ENABLE' IS ENABLED ");
		LOGGER.info("PRE-CONDITION " + preConStepNumber
				+ " : ACTION : SET AND VERIFY WHETHER PRIVATE 5 GHZ SSID 'DEVICE.WIFI.SSID.10101.ENABLE' IS ENABLED USING WEBPA ");
		LOGGER.info("PRE-CONDITION " + preConStepNumber
				+ " : EXPTECTED : DEVICE SHOULD BE ENABLED WITH PRIVATE 5 GHZ SSID AND RESPONSE SHOULD BE TRUE");
		LOGGER.info("#######################################################################################");
		errorMessage = "NOT ABLE TO ENABLE THE 5GHZ PRIVATE SSID ON THIS DEVICE - HENCE BLOCKING THE EXECUTION.";
		try {
			status = BroadBandCommonUtils.getWebPaValueAndVerify(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_WIFI_5_RADIO_ENABLE, BroadBandTestConstants.TRUE);
		} catch (TestException exception) {
			status = false;
			LOGGER.error(errorMessage + " : " + exception.getMessage());
		}
		if (!status) {
			errorMessage = "UNABLE TO SET THE 5 GHZ RADIO STATUS AS 'TRUE'.";
			status = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_WIFI_5_RADIO_ENABLE, BroadBandTestConstants.CONSTANT_3,
					BroadBandTestConstants.TRUE);
		}
		if (status) {
			LOGGER.info(
					"PRE-CONDITION " + preConStepNumber + " : ACTUAL : PRIVATE 5 GHZ SSID ENABLED IN GATEWAY DEVICE.");
		} else {
			LOGGER.error("PRE-CONDITION " + preConStepNumber + " : ACTUAL : " + errorMessage);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION : " + preConStepNumber
					+ " FAILED : " + errorMessage);
		}
	}

	/**
	 * Method to execute pre-condition for telemetry 2 configurations
	 * 
	 * @param device              {@link Instanceof Dut}
	 * @param preConditionStepNum precondition step number
	 * @refactor Rakesh C N
	 */
	public static boolean executePreConfigurationsForTelemetry(Dut device, AutomaticsTapApi tapEnv,
			int preConditionStepNum) {
		boolean status = false;
		boolean isTelemetryVerOneUpdated = false;
		LOGGER.info("##################################################################################");
		LOGGER.info("PRE-CONDITION " + preConditionStepNum
				+ " :DESCRIPTION : Validate if the T2 settings are already present else set the settings and reboot the device");
		LOGGER.info("PRE-CONDITION " + preConditionStepNum
				+ " :ACTION : Execute webpa get on the T2 webpa parameters and validate the settings ");
		LOGGER.info("PRE-CONDITION " + preConditionStepNum
				+ " : EXPECTED : Webpa get/set should be successful and device should come up with T2 settings if rebooted ");
		LOGGER.info("##################################################################################");

		status = BroadBandCommonUtils.verifyProcessRunningStatus(device, tapEnv, false,
				BroadBandTestConstants.PROCESS_NAME_TELEMETRY_2_0);
		if (!status) {
			LOGGER.info("Telemetry 2 process is not running on the device. "
					+ "Therefore configuring device with telemetry version 2 settings");
			if (BroadBandTelemetry2Utils.setTelemetry2ConfigurationViaWebpa(device, tapEnv)) {
				isTelemetryVerOneUpdated = true;
				status = CommonMethods.rebootAndWaitForIpAccusition(device, tapEnv)
						&& BroadBandTelemetry2Utils.verifyTelemetry2ConfigurationViaWebpa(device, tapEnv);
			}
		} else {
			LOGGER.info("The device is already configured for Telemetry 2");
		}
		if (status) {
			LOGGER.info("Telemetry 2 settings has been configured successfully on the gateway");
		} else {
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR
					+ "PRE_CONDITION_FAILED :Telemetry 2 settings hasn't been configured successfully on the gateway "
					+ device.getHostMacAddress());
		}
		return isTelemetryVerOneUpdated;
	}

	/**
	 * Pre-Condition method to get the both private ssid visible multiple device
	 * 
	 * @param device          instance of{@link Dut}
	 * @param tapEnv          instance of {@link AutomaticsTapApi}
	 * @param preCondNumber   Pre condition number
	 * @param requiredDevices No of device to be verified
	 * @return deviceConnected instance of connected device(both private ssids are
	 *         visible in the connected device)
	 * @refactor Said Hisham
	 * 
	 */
	public static List<Dut> executePreConditionToGetBothPrivateWiFiSsidsVisibleDevices(Dut device,
			AutomaticsTapApi tapEnv, int preCondNumber, int requiredDevices) throws TestException {
		List<Dut> listOfCntdDevices = null;
		String errorMessage = null;
		boolean status = false;
		List<Dut> ssidVisibleDevices = new ArrayList<Dut>();
		try {
			/**
			 * PRE-CONDITION : VERIFY THE PRIVATE WIFI 2.4 GHZ AND 5 GHZ SSID'S ARE
			 * BROADCASTING IN CONNECTED CLIENT
			 */
			status = false;
			errorMessage = null;
			LOGGER.info("#####################################################################################");
			LOGGER.info("PRE-CONDITION " + preCondNumber
					+ " : DESCRIPTION : VERIFY THE PRIVATE WIFI 2.4 AND 5 GHZ SSID's ARE BROADCASTING IN CONNECTED CLIENT ");
			LOGGER.info("PRE-CONDITION " + preCondNumber
					+ " : ACTION : EXECUTE COMMAND FOR WINDOWS: 1.'NETSH WLAN SHOW NETWORKS | GREP -I '<PRIVATE_SSID_2GHZ>' 2.'NETSH WLAN SHOW NETWORKS | GREP -I '<PRIVATE_SSID_5GHZ>' OR LINUX : 'SUDO IWLIST WLAN0 SCAN' ");
			LOGGER.info("PRE-CONDITION " + preCondNumber
					+ " : EXPECTED : PRIVATE WIFI 2.4 AND 5 GHZ SSID's SHOULD BE BROADCASTED IN CONNECTED CLIENT");
			LOGGER.info("#####################################################################################");
			errorMessage = "UNABLE TO VERIFY PRIVATE WIFI 2.4 AND 5 GHZ SSID's BROADCASTING STATUS IN CONNECTED CLIENT";
			listOfCntdDevices = ((Device) device).getConnectedDeviceList();
			LOGGER.info("PRE-CONDITION : LIST OF CONNECTED DEVICE :" + listOfCntdDevices.size());
			String connectionType = null;
			if (null != listOfCntdDevices) {
				for (Dut ssidVisibleDevice : listOfCntdDevices) {
					try {
						if (BroadBandWifiWhixUtils.restartWifiDriver(ssidVisibleDevice, tapEnv)) {
							connectionType = ((Device) ssidVisibleDevice).getConnectedDeviceInfo().getConnectionType();
							LOGGER.info("PRE-CONDITION : CLIENT MAC :"
									+ ((Device) ssidVisibleDevice).getConnectedDeviceInfo().getWifiMacAddress());
							LOGGER.info("PRE-CONDITION : CONNECTION TYPE :" + connectionType);
							LOGGER.info("PRE-CONDITION : CLIENT IP :"
									+ ((Device) ssidVisibleDevice).getConnectedDeviceInfo().getDeviceIpAddress());
							LOGGER.info("PRE-CONDITION : CLIENT PORT :"
									+ ((Device) ssidVisibleDevice).getConnectedDeviceInfo().getDevicePortAddress());
							if (CommonMethods.isNotNull(connectionType) && connectionType.trim().equalsIgnoreCase(
									BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI)
									&& BroadBandWiFiUtils.validateBothSsidVisibleStateInConnectedClient(device, tapEnv,
											ssidVisibleDevice, BroadBandTestConstants.WIFI_PRIVATE, true)) {
								ssidVisibleDevices.add(ssidVisibleDevice);
								if (ssidVisibleDevices.size() >= requiredDevices) {
									break;
								}
							}
						}
					} catch (Exception e) {
						errorMessage = e.getMessage();
						LOGGER.error("EXCEPTION IN VERIFYING SSID VISBILITY :" + errorMessage);
					}
				}
			}
			status = (ssidVisibleDevices.size() >= requiredDevices);
			if (status) {
				LOGGER.info("PRE-CONDITION " + preCondNumber
						+ " : ACTUAL : PRIVATE WIFI 2.4 GHZ AND 5 GHZ SSID's ARE BROADCASTED IN "
						+ ssidVisibleDevices.size() + " CONNECTED SETUP");
			} else {
				errorMessage = "PRIVATE WIFI 2.4 GHZ AND 5 GHZ SSID's ARE BROADCASTED IN " + ssidVisibleDevices.size()
						+ " CONNECTED SETUP, MIMIMUM " + requiredDevices + " CONNECTED SETUP IS REQUIRED ";
				LOGGER.error("PRE-CONDITION " + preCondNumber + " : ACTUAL : " + errorMessage);
				throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + " PRE-CONDITION " + preCondNumber
						+ " : FAILED : " + errorMessage);
			}
		} catch (Exception e) {
			errorMessage = e.getMessage();
			LOGGER.error("PRE-CONDITION: EXCEPTION OCCURRED WHILE VERIFYING THE SSID VISIBLE STATUS" + errorMessage);
		}
		return ssidVisibleDevices;
	}

	/**
	 * Pre-Condition method to enable/disable the prefer private wifi
	 * 
	 * @param device        instance of{@link Dut}
	 * @param tapEnv        instance of {@link AutomaticsTapApi}
	 * @param isEnabled     True - To be Enabled,False- To be Disabled
	 * @param preCondNumber Pre condition number
	 * @refactor Said Hisham
	 */
	public static void executePreCondToTogglePreferPrivateWiFi(Dut device, AutomaticsTapApi tapEnv, boolean isEnabled,
			int preCondNumber) throws TestException {
		String errorMessage = null;
		boolean status = false;
		/**
		 * PRECONDITION : ENABLE/DISABLE THE PREFER PTIVATE WIFI
		 */
		LOGGER.info("#######################################################################################");
		LOGGER.info("PRE-CONDITION " + preCondNumber + " : DESCRIPTION : " + (isEnabled ? "ENABLE" : "DISABLE")
				+ " THE PREFER PTIVATE WIFI");
		LOGGER.info("PRE-CONDITION " + preCondNumber + " : ACTION : " + (isEnabled ? "ENABLE" : "DISABLE")
				+ " THE PREFER PTIVATE WIFI " + BroadBandWebPaConstants.WEBPA_PARAMETER_PREFER_PRIVATE_CONNECTION
				+ " STATUS AS " + (isEnabled ? "TRUE" : "FALSE") + " USING WEBPA");
		LOGGER.info("PRE-CONDITION " + preCondNumber + " : EXPECTED : PREFER PTIVATE WIFI MUST BE "
				+ (isEnabled ? "ENABLED" : "DISABLED"));
		LOGGER.info("#######################################################################################");
		errorMessage = "UNABLE TO " + (isEnabled ? "ENABLE" : "DISABLE") + " THE PREFER PRVATE WIFI";
		status = BroadBandWebPaUtils.setVerifyWebPAInPolledDuration(device, tapEnv,
				BroadBandWebPaConstants.WEBPA_PARAMETER_PREFER_PRIVATE_CONNECTION, WebPaDataTypes.BOOLEAN.getValue(),
				BroadBandTestConstants.FALSE, AutomaticsConstants.THREE_MINUTES,
				BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		if (status) {
			LOGGER.info("PRE-CONDITION " + preCondNumber + " : ACTUAL : SUCCESSFULLY "
					+ (isEnabled ? "ENABLED" : "DISABLED") + " THE PREFER PTIVATE WIFI");
		} else {
			LOGGER.error("PRE-CONDITION " + preCondNumber + " : ACTUAL : " + errorMessage);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION " + preCondNumber
					+ " : FAILED : " + errorMessage);
		}
	}

	/**
	 * Pre-Condition method to verify and change the given security mode
	 * 
	 * 
	 * @param tapEnv            instance of {@link AutomaticsTapApi}
	 * @param device            instance of{@link Dut}
	 * @param preCondNumber     Precondition number
	 * @param securityModeToset Security mode for 2.4 /5 Ghz
	 * 
	 * @throws TestException
	 */
	public static void executePreConditionToSetSecurityMode(Dut device, AutomaticsTapApi tapEnv, int preConStepNumber,
			String securityModeToset) throws TestException {
		String errorMessage = null;
		boolean status = false;
		/**
		 * PRE-CONDITION :SET THE SECURITY MODE FOR 2.4 GHZ SSID USING WEBPA
		 */
		status = false;
		errorMessage = null;
		LOGGER.info("#######################################################################################");
		LOGGER.info("PRE-CONDITION " + preConStepNumber + " : DESCRIPTION : SET THE SECURITY MODE " + securityModeToset
				+ " FOR 2.4 GHZ SSID ");
		LOGGER.info("PRE-CONDITION " + preConStepNumber + " : ACTION : SET THE SECURITY MODE " + securityModeToset
				+ " FOR 2.4 GHZ SSID USING WEBPA ");
		LOGGER.info("PRE-CONDITION " + preConStepNumber + " : EXPTECTED : SECURITY MODE " + securityModeToset
				+ " CHANGED SUCCESSFULLY FOR 2.4 GHZ SSID");
		LOGGER.info("#######################################################################################");
		errorMessage = "UNABLE TO CHANGE THE SECURITY MODE " + securityModeToset + " FOR 2.4GHz SSID USING WEBPA. ";
		try {
			status = BroadBandWebPaUtils.getAndVerifyWebpaValueInPolledDuration(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_PRIVATE_SECURITY_MODEENABLED,
					securityModeToset, BroadBandTestConstants.ONE_MINUTE_IN_MILLIS,
					BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		} catch (Exception exception) {
			errorMessage = "UNABLE TO GET THE SECURITY MODE " + securityModeToset + " FOR 2.4GHz SSID USING WEBPA. ";
			status = false;
			LOGGER.error(errorMessage + " : " + exception.getMessage());
		}
		try {
			LOGGER.info("TRYING TO SET THE SECURITY MODE " + securityModeToset + " FOR 2.4GHz SSID USING WEBPA. ");
			if (!status) {
				status = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_2_4_GHZ_PRIVATE_SECURITY_MODEENABLED,
						BroadBandTestConstants.CONSTANT_0, securityModeToset);
			}
		} catch (Exception exception) {
			errorMessage = "UNABLE TO SET THE SECURITY MODE " + securityModeToset + " FOR 2.4GHz SSID USING WEBPA. ";
			status = false;
			LOGGER.error(errorMessage + " : " + exception.getMessage());
		}
		if (status) {
			LOGGER.info("PRE-CONDITION " + preConStepNumber + " : ACTUAL : SECURITY MODE " + securityModeToset
					+ " VERIFIED/CHANGED SUCCESSFULLY FOR 2.4 GHZ SSID.");
		} else {
			LOGGER.error("PRE-CONDITION " + preConStepNumber + " : ACTUAL :" + errorMessage);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION : " + preConStepNumber
					+ " FAILED : " + errorMessage);
		}
		/**
		 * PRE-CONDITION :SET THE SECURITY MODE FOR 5 GHZ SSID USING WEBPA
		 */
		preConStepNumber++;
		status = false;
		errorMessage = null;
		LOGGER.info("#######################################################################################");
		LOGGER.info("PRE-CONDITION " + preConStepNumber + " : DESCRIPTION : SET THE SECURITY MODE " + securityModeToset
				+ " FOR 5 GHZ SSID ");
		LOGGER.info("PRE-CONDITION " + preConStepNumber + " : ACTION : SET THE SECURITY MODE " + securityModeToset
				+ " FOR 5 GHZ SSID USING WEBPA ");
		LOGGER.info("PRE-CONDITION " + preConStepNumber + " : EXPTECTED : SECURITY MODE " + securityModeToset
				+ " CHANGED SUCCESSFULLY FOR 5 GHZ SSID");
		LOGGER.info("#######################################################################################");
		errorMessage = "UNABLE TO CHANGE THE SECURITY MODE " + securityModeToset + " FOR 5GHz SSID USING WEBPA. ";
		try {
			status = BroadBandWebPaUtils.getAndVerifyWebpaValueInPolledDuration(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_PRIVATE_SECURITY_MODEENABLED,
					securityModeToset, BroadBandTestConstants.ONE_MINUTE_IN_MILLIS,
					BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		} catch (Exception exception) {
			errorMessage = "UNABLE TO GET THE SECURITY MODE " + securityModeToset + " FOR 5GHz SSID USING WEBPA. ";
			status = false;
			LOGGER.error(errorMessage + " : " + exception.getMessage());
		}
		try {
			LOGGER.info("TRYING TO SET THE SECURITY MODE " + securityModeToset + " FOR 5GHz SSID USING WEBPA. ");
			if (!status) {
				status = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_ACCESSPOINT_5_GHZ_PRIVATE_SECURITY_MODEENABLED,
						BroadBandTestConstants.CONSTANT_0, securityModeToset);
			}
		} catch (Exception exception) {
			errorMessage = "UNABLE TO SET THE SECURITY MODE " + securityModeToset + " FOR 5GHz SSID USING WEBPA. ";
			status = false;
			LOGGER.error(errorMessage + " : " + exception.getMessage());
		}
		if (status) {
			LOGGER.info("PRE-CONDITION " + preConStepNumber + " : ACTUAL : SECURITY MODE " + securityModeToset
					+ " VERIFIED/CHANGED SUCCESSFULLY FOR 5 GHZ SSID.");
		} else {
			LOGGER.error("PRE-CONDITION " + preConStepNumber + " : ACTUAL :" + errorMessage);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION : " + preConStepNumber
					+ " FAILED : " + errorMessage);
		}
	}

	/**
	 * Pre-Condition method to perform the apply setting for 2.4 /5 Ghz Radio's
	 * 
	 * @param device        instance of{@link Dut}
	 * @param tapEnv        instance of {@link AutomaticsTapApi}
	 * @param preCondNumber Pre condition number
	 * 
	 * @Refactor Sruthi Santhosh
	 * 
	 */
	public static void executePreConditionToRadioApplySetting(Dut device, AutomaticsTapApi tapEnv, int preCondNumber)
			throws TestException {
		String errorMessage = null;
		boolean status = false;
		/**
		 * PRE-CONDITION : PERFORM APPLY SETTINGS FOR 2.4 AND 5 Ghz RADIO's
		 */
		LOGGER.info("#######################################################################################");
		LOGGER.info("PRE-CONDITION " + preCondNumber + " : DESCRIPTION : SET THE APPLY SETTINGS FOR 2.4 AND 5 GHZ ");
		LOGGER.info("PRE-CONDITION " + preCondNumber + " : ACTION : EXECUTE WEBPA COMMAND: FOR 2.4 GHz:"
				+ BroadBandWebPaConstants.WEBPA_PARAM_WIFI_2_4_APPLY_SETTING + " FOR 5 GHz:"
				+ BroadBandWebPaConstants.WEBPA_PARAM_WIFI_5_APPLY_SETTING);
		LOGGER.info("PRE-CONDITION " + preCondNumber
				+ " : EXPECTED : MUST SET THE APPLY SETTINGS FOR 2.4 5 AND Ghz RADIO's ");
		LOGGER.info("#######################################################################################");
		errorMessage = "UNABLE TO SET THE APPLY SETTINGS FOR 2.4 AND 5 Ghz RADIO's ";
		try {
			List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
			WebPaParameter applySettings2Ghz = BroadBandWebPaUtils.setAndReturnWebPaParameterObject(
					BroadBandWebPaConstants.WEBPA_PARAM_WIFI_2_4_APPLY_SETTING, BroadBandTestConstants.TRUE,
					BroadBandTestConstants.CONSTANT_3);
			WebPaParameter applySettings5Ghz = BroadBandWebPaUtils.setAndReturnWebPaParameterObject(
					BroadBandWebPaConstants.WEBPA_PARAM_WIFI_5_APPLY_SETTING, BroadBandTestConstants.TRUE,
					BroadBandTestConstants.CONSTANT_3);
			webPaParameters.add(applySettings2Ghz);
			webPaParameters.add(applySettings5Ghz);
			tapEnv.executeMultipleWebPaSetCommands(device, webPaParameters);
			status = true;
			status = true;
		} catch (Exception e) {
			LOGGER.error("Exception occured while performing apply settings " + e.getMessage());
		}
		if (status) {
			LOGGER.info("PRE-CONDITION " + preCondNumber
					+ " : ACTUAL : SUCCESSFULLY SET THE APPLY SETTINGS FOR 2.4 AND 5 Ghz RADIO's ");
		} else {
			LOGGER.error("PRE-CONDITION " + preCondNumber + " : ACTUAL : " + errorMessage);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION " + preCondNumber
					+ " : FAILED : " + errorMessage);
		}
	}

	/**
	 * Pre-Condition method to check the partner Id
	 * 
	 * @param device        instance of{@link Dut}
	 * @param tapEnv        instance of {@link AutomaticsTapApi}
	 * 
	 * @param preCondNumber Pre condition number
	 * 
	 */
	public static String executePreConditionToCheckPartnerId(Dut device, AutomaticsTapApi tapEnv, int preCondNumber)
			throws TestException {
		String errorMessage = null;
		String initialPartnerIdName = null;
		boolean status = false;
		LOGGER.info("#######################################################################################");
		LOGGER.info("PRE-CONDITION " + preCondNumber + " : DESCRIPTION : Check the partner Id");
		LOGGER.info("PRE-CONDITION " + preCondNumber + " Execute webpa command to get the partner Id");
		LOGGER.info("PRE-CONDITION " + preCondNumber + " PartnerId should be received ");
		LOGGER.info("#######################################################################################");
		errorMessage = "Failed to get Initial Partner Id";
		long startTime = System.currentTimeMillis();
		do {
			try {
				initialPartnerIdName = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_PARAM_FOR_SYNDICATION_PARTNER_ID);
			} catch (Exception testException) {
				// Log & Suppress the exception.
				LOGGER.error("EXCEPTION OCCURRED WHILE VERIFYING WEBPA PROCESS IS UP: " + testException.getMessage());
			}
			status = CommonMethods.isNotNull(initialPartnerIdName);
		} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.EIGHT_MINUTE_IN_MILLIS && !status
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
		if (status) {
			LOGGER.info("PRE-CONDITION " + preCondNumber + " ACTUAL : Successfully Received partner Id");
			return initialPartnerIdName;
		} else {
			LOGGER.error("PRE-CONDITION " + preCondNumber + " : ACTUAL : " + errorMessage);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION " + preCondNumber
					+ " : FAILED : " + errorMessage);
		}
	}

	/**
	 * Pre-Condition method to get the current firmware version on device.
	 * 
	 * @param device {@link Dut}
	 * @refactor Said Hisham
	 */
	public static String executePreConditionToGetCurrentFirmwareVersion(Dut device, AutomaticsTapApi tapEnv,
			int preConStepNumber) throws TestException {
		String initialFirmwareVersion = null;
		String errorMessage = null;
		boolean status = false;
		/**
		 * PRE-CONDITION :GET CURRENT FIRMWARE VERSION ON DEVICE
		 */
		LOGGER.info("#######################################################################################");
		LOGGER.info("PRE-CONDITION " + preConStepNumber + " : DESCRIPTION : GET RUNNING FIRMWARE VERSION ON DEVICE ");
		LOGGER.info("PRE-CONDITION " + preConStepNumber
				+ " : ACTION : GET RUNNING FIRMWARE VERSION ON DEVICE USING WEBPA OR EXECUTE COMMAND : HEAD/VERSION.TXT ");
		LOGGER.info("PRE-CONDITION " + preConStepNumber
				+ " : EXPTECTED : SUCCESSFULLY RETRIEVED THE CURRENT FIRMWAE VERSION ON DEVICE");
		LOGGER.info("#######################################################################################");
		errorMessage = "UNABLE TO RETRIEVE THE CURRENT FIRMWAE VERSION ON DEVICE. HENCE BLOCKING THE EXECUTION.";
		try {
			initialFirmwareVersion = FirmwareDownloadUtils.getCurrentFirmwareFileNameForCdl(tapEnv, device);
		} catch (Exception e) {
			errorMessage = e.getMessage();
			status = false;
		}
		status = CommonMethods.isNotNull(initialFirmwareVersion);
		if (status) {
			LOGGER.info("PRE-CONDITION " + preConStepNumber
					+ " : ACTUAL : SUCCESSFULLY RETRIEVED THE CURRENT FIRMWAE VERSION ON DEVICE.");
		} else {
			LOGGER.error("PRE-CONDITION " + preConStepNumber + " : ACTUAL : " + errorMessage);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION : " + preConStepNumber
					+ " FAILED : " + errorMessage);
		}
		LOGGER.info("#######################################################################################");
		return initialFirmwareVersion;
	}

	/**
	 * Pre-Condition method to verify the private SSID enabled status for 2.4 & 5
	 * GHz .
	 * 
	 * @param device        {@link Dut}
	 * @param tapEnv        instance of {@link AutomaticsTapApi}
	 * @param preCondNumber Pre condition number
	 * 
	 * @refactor yamini.s
	 */
	public static void executePreConditionToVerifyPrivateSsidIsEnabled(Dut device, AutomaticsTapApi tapEnv,
			int preConStepNumber) throws TestException {
		String errorMessage = null;
		boolean status = false;
		try {
			/**
			 * PRE-CONDITION :Enable Private 2.4 GHz SSID via WebPA
			 */

			errorMessage = null;
			status = false;
			LOGGER.info("#######################################################################################");
			LOGGER.info("PRE-CONDITION " + preConStepNumber
					+ " : DESCRIPTION : VERIFY WHETHER PRIVATE 2.4 GHZ SSID 'DEVICE.WIFI.SSID.10001.ENABLE' IS ENABLED, IF NOT ENABLE THE PRIVATE 2.4 GHZ SSID ");
			LOGGER.info("PRE-CONDITION " + preConStepNumber
					+ " : ACTION : VERIFY WHETHER PRIVATE 2.4 GHZ SSID 'DEVICE.WIFI.SSID.10001.ENABLE' IS ENABLED,IF NOT ENABLE THE PRIVATE 2.4 GHZ SSID USING WEBPA ");
			LOGGER.info("PRE-CONDITION " + preConStepNumber
					+ " : EXPTECTED : DEVICE SHOULD BE ENABLED WITH PRIVATE 2.4 GHZ SSID AND RESPONSE SHOULD BE TRUE");
			LOGGER.info("#######################################################################################");
			errorMessage = "NOT ABLE TO ENABLE THE 2.4GHZ PRIVATE SSID ON THIS DEVICE";
			try {
				status = BroadBandCommonUtils.getWebPaValueAndVerify(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_ENABLED,
						BroadBandTestConstants.TRUE);
			} catch (TestException exception) {
				status = false;
				LOGGER.error(errorMessage + " : " + exception.getMessage());
			}
			if (!status) {
				errorMessage = "UNABLE TO SET THE PRIVATE 2.4 GHZ SSID STATUS AS 'TRUE'.";
				status = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_ENABLED,
						BroadBandTestConstants.CONSTANT_3, BroadBandTestConstants.TRUE);
			}
			if (status) {
				LOGGER.info("PRE-CONDITION " + preConStepNumber
						+ " : ACTUAL : PRIVATE 2.4 GHZ SSID VERIFIED/ENABLED IN GATEWAY DEVICE.");
			} else {
				LOGGER.error("PRE-CONDITION " + preConStepNumber + " : ACTUAL : " + errorMessage);
				throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION " + preConStepNumber
						+ " : FAILED : " + errorMessage);
			}
			LOGGER.info("#######################################################################################");
			/**
			 * PRE-CONDITION :Enable Private 5 GHz SSID via WebPA
			 */
			preConStepNumber++;
			errorMessage = null;
			status = false;
			LOGGER.info("#######################################################################################");
			LOGGER.info("PRE-CONDITION " + preConStepNumber
					+ " : DESCRIPTION : SET AND VERIFY WHETHER PRIVATE 5 GHZ SSID 'DEVICE.WIFI.SSID.10101.ENABLE' IS ENABLED,IF NOT ENABLE THE PRIVATE 2.4 GHZ SSID ");
			LOGGER.info("PRE-CONDITION " + preConStepNumber
					+ " : ACTION : SET AND VERIFY WHETHER PRIVATE 5 GHZ SSID 'DEVICE.WIFI.SSID.10101.ENABLE' IS ENABLED,IF NOT ENABLE THE PRIVATE 2.4 GHZ SSID USING WEBPA ");
			LOGGER.info("PRE-CONDITION " + preConStepNumber
					+ " : EXPTECTED : DEVICE SHOULD BE ENABLED WITH PRIVATE 5 GHZ SSID AND RESPONSE SHOULD BE TRUE");
			LOGGER.info("#######################################################################################");
			errorMessage = "NOT ABLE TO ENABLE THE 5GHZ PRIVATE SSID ON THIS DEVICE";
			try {
				status = BroadBandCommonUtils.getWebPaValueAndVerify(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_ENABLED,
						BroadBandTestConstants.TRUE);
			} catch (TestException exception) {
				status = false;
				LOGGER.error(errorMessage + " : " + exception.getMessage());
			}
			if (!status) {
				errorMessage = "UNABLE TO SET THE PRIVATE 5 GHZ SSID STATUS AS 'TRUE'.";

				status = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_ENABLED,
						BroadBandTestConstants.CONSTANT_3, BroadBandTestConstants.TRUE);
			}
			if (status) {
				LOGGER.info("PRE-CONDITION " + preConStepNumber
						+ " : ACTUAL : PRIVATE 5 GHZ SSID VERIFIED/ENABLED IN GATEWAY DEVICE.");
			} else {
				LOGGER.error("PRE-CONDITION " + preConStepNumber + " : ACTUAL : " + errorMessage);
				throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION " + preConStepNumber
						+ " : FAILED : " + errorMessage);
			}
		} catch (Exception exception) {
			LOGGER.error(
					"Execution error occurred while executing to verify default SSID status pre conditions due to exception --> "
							+ exception.getMessage());
		}
		LOGGER.info("#######################################################################################");
	}

	/**
	 * Pre-Condition method to verify telemetry 2 is enabled.
	 * 
	 * @param device        {@link Dut}
	 * @param tapEnv        {@link AutomaticsTapApi}
	 * @param preCondNumber Pre condition number
	 * 
	 */
	public static boolean executePreConditionToVerifyTelemetryStatus(Dut device, AutomaticsTapApi tapEnv,
			int preConStepNumber) throws TestException {
		String errorMessage = null;
		LOGGER.info("#######################################################################################");
		LOGGER.info("PRE-CONDITION " + preConStepNumber + "  : DESCRIPTION : Verify telemetry 2 status");
		LOGGER.info("PRE-CONDITION " + preConStepNumber
				+ "  : ACTION : Execute command BroadBandWebPaConstants.WEBPA_PARAM_FOR_TELEMETRY_2_0_ENABLE");
		LOGGER.info("PRE-CONDITION " + preConStepNumber + "   : EXPECTED : Telemetr 2 should be false");
		LOGGER.info("#######################################################################################");
		boolean isTelemetryEnabled = false;
		try {
			isTelemetryEnabled = BroadBandWebPaUtils.getAndVerifyWebpaValueInPolledDuration(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_FOR_TELEMETRY_2_0_ENABLE, BroadBandTestConstants.TRUE,
					BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS, BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
		} catch (Exception e) {
			errorMessage = BroadBandTestConstants.PRE_CONDITION_ERROR + e.getMessage();
		}

		if (isTelemetryEnabled) {
			LOGGER.error("PRE-CONDITION " + preConStepNumber + "  : ACTUAL : Telemetry 2.0 is enabled.");
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION " + preConStepNumber
					+ ": FAILED : " + errorMessage);

		} else {
			LOGGER.info("PRE-CONDITION " + preConStepNumber + "  : ACTUAL : Telemetry 2.0 is disabled");

		}
		return isTelemetryEnabled;
	}

	/**
	 * Pre-Condition method to get the default operating standard for 2.4 & 5 Ghz .
	 * 
	 * @param Dut               {@link device}
	 * @param tapEnv            {@link AutomaticsTapApi}
	 * @param preConStepNumber
	 * @param wifiFrequencyBand
	 * 
	 * @refactor Athira
	 */
	public static String executePreConditionToGetDefaultOperStandard(Dut device, AutomaticsTapApi tapEnv,
			int preConStepNumber, String wifiFrequencyBand) throws TestException {
		String defaultOperStandard = null;
		String errorMessage = null;
		boolean status = false;
		/**
		 * PRE-CONDITION :GET THE DEFAULT VALUE FOR OPERATING STANDARD 2.4 GHZ
		 */
		if (wifiFrequencyBand.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ)) {
			errorMessage = null;
			status = false;
			LOGGER.info("#######################################################################################");
			LOGGER.info("PRE-CONDITION " + preConStepNumber
					+ " : DESCRIPTION : GET THE DEFAULT VALUE FOR OPERATING STANDARD 2.4 GHZ ");
			LOGGER.info("PRE-CONDITION " + preConStepNumber
					+ " : ACTION : GET THE DEFAULT VALUE FOR OPERATING STANDARD 'Device.WiFi.Radio.10000.OperatingStandards' 2.4 GHZ USING WEBPA ");
			LOGGER.info("PRE-CONDITION " + preConStepNumber
					+ " : EXPTECTED : MUST RETURN THE DEFAULT OPERATING STANDARD VALUE FOR 2.4 GHZ");
			LOGGER.info("#######################################################################################");
			errorMessage = "UNABLE TO GET THE DEFAULT VALUE FOR OPERATING STANDARD 2.4 GHZ - HENCE BLOCKING THE EXECUTION.";
			defaultOperStandard = tapEnv.executeWebPaCommand(device,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_RADIO_2_4_GHZ_OPERATING_STANDARD);
			status = CommonMethods.isNotNull(defaultOperStandard);
			if (status) {
				LOGGER.info("PRE-CONDITION " + preConStepNumber
						+ " : ACTUAL : RETRIEVED THE DEFAULT VALUE FOR OPERATING STANDARD 2.4 GHZ SUCCESSFULLY.");
			} else {
				LOGGER.error("PRE-CONDITION " + preConStepNumber + " : ACTUAL : " + errorMessage);
				throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION : "
						+ preConStepNumber + " FAILED : " + errorMessage);
			}
			LOGGER.info("#######################################################################################");
		}
		/**
		 * PRE-CONDITION :GET THE DEFAULT VALUE FOR OPERATING STANDARD 5 GHZ
		 */
		if (wifiFrequencyBand.equalsIgnoreCase(BroadBandTestConstants.BAND_5GHZ)) {
			errorMessage = null;
			status = false;
			preConStepNumber++;
			LOGGER.info("#######################################################################################");
			LOGGER.info("PRE-CONDITION " + preConStepNumber
					+ " : DESCRIPTION : GET THE DEFAULT VALUE FOR OPERATING STANDARD 5 GHZ ");
			LOGGER.info("PRE-CONDITION " + preConStepNumber
					+ " : ACTION : GET THE DEFAULT VALUE FOR OPERATING STANDARD 'Device.WiFi.Radio.10100.OperatingStandards' 5 GHZ USING WEBPA ");
			LOGGER.info("PRE-CONDITION " + preConStepNumber
					+ " : EXPTECTED : MUST RETURN THE DEFAULT OPERATING STANDARD VALUE FOR 5 GHZ");
			LOGGER.info("#######################################################################################");
			errorMessage = "UNABLE TO GET THE DEFAULT VALUE FOR OPERATING STANDARD 5 GHZ - HENCE BLOCKING THE EXECUTION.";
			defaultOperStandard = tapEnv.executeWebPaCommand(device,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_RADIO_5GHZ_OPERATING_STANDARD);
			status = CommonMethods.isNotNull(defaultOperStandard);
			if (status) {
				LOGGER.info("PRE-CONDITION " + preConStepNumber
						+ " : ACTUAL : RETRIEVED THE DEFAULT VALUE FOR OPERATING STANDARD 5 GHZ SUCCESSFULLY.");
			} else {
				LOGGER.error("PRE-CONDITION " + preConStepNumber + " : ACTUAL : " + errorMessage);
				throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION : "
						+ preConStepNumber + " FAILED : " + errorMessage);
			}
			LOGGER.info("#######################################################################################");
		}
		return defaultOperStandard;
	}

	/**
	 * Pre-Condition method to get the both private ssid visible multiple Intel wifi
	 * adapter device
	 * 
	 * @param device          instance of{@link Dut}
	 * @param tapEnv          instance of {@link AutomaticsTapApi}
	 * @param preCondNumber   Pre condition number
	 * @param requiredDevices No of device to be verified
	 * @return deviceConnected instance of connected device(both private ssids are
	 *         visible in the connected device)
	 * 
	 * @refactor yamini.s
	 * 
	 */
	public static List<Dut> executePreConditionToGetBothPrivateWiFiSsidsVisibleIntelDevices(Dut device,
			AutomaticsTapApi tapEnv, int preCondNumber, int requiredDevices) throws TestException {
		List<Dut> listOfCntdDevices = null;
		String errorMessage = null;
		boolean status = false;
		List<Dut> ssidVisibleDevices = new ArrayList<Dut>();
		boolean isIntelClient = false;
		try {
			/**
			 * PRE-CONDITION : VERIFY THE PRIVATE WIFI 2.4 GHZ AND 5 GHZ SSID'S ARE
			 * BROADCASTING IN INTEL WIFIF ADAPTER CONNECTED CLIENT
			 */
			status = false;
			errorMessage = null;
			LOGGER.info("#####################################################################################");
			LOGGER.info("PRE-CONDITION " + preCondNumber
					+ " : DESCRIPTION : VERIFY THE PRIVATE WIFI 2.4 AND 5 GHZ SSID's ARE BROADCASTING IN INTEL WIFIF ADAPTER CONNECTED CLIENT ");
			LOGGER.info("PRE-CONDITION " + preCondNumber
					+ " : ACTION : EXECUTE COMMAND FOR WINDOWS: 1.'NETSH WLAN SHOW NETWORKS | GREP -I '<PRIVATE_SSID_2GHZ>' 2.'NETSH WLAN SHOW NETWORKS | GREP -I '<PRIVATE_SSID_5GHZ>' OR LINUX : 'SUDO IWLIST WLAN0 SCAN' ");
			LOGGER.info("PRE-CONDITION " + preCondNumber
					+ " : EXPECTED : PRIVATE WIFI 2.4 AND 5 GHZ SSID's SHOULD BE BROADCASTED IN INTEL WIFIF ADAPTER CONNECTED CLIENT");
			LOGGER.info("#####################################################################################");
			errorMessage = "UNABLE TO VERIFY PRIVATE WIFI 2.4 AND 5 GHZ SSID's BROADCASTING STATUS IN INTEL WIFIF ADAPTER CONNECTED CLIENT";
			listOfCntdDevices = ((Device) device).getConnectedDeviceList();
			LOGGER.info("PRE-CONDITION : LIST OF CONNECTED DEVICE :" + listOfCntdDevices.size());
			String connectionType = null;
			if (null != listOfCntdDevices) {
				for (Dut ssidVisibleDevice : listOfCntdDevices) {
					try {
						connectionType = ((Device) ssidVisibleDevice).getConnectedDeviceInfo().getConnectionType();
						LOGGER.info("PRE-CONDITION : CLIENT MAC :"
								+ ((Device) ssidVisibleDevice).getConnectedDeviceInfo().getWifiMacAddress());
						LOGGER.info("PRE-CONDITION : CONNECTION TYPE :" + connectionType);
						LOGGER.info("PRE-CONDITION : CLIENT IP :"
								+ ((Device) ssidVisibleDevice).getConnectedDeviceInfo().getDeviceIpAddress());
						LOGGER.info("PRE-CONDITION : CLIENT PORT :"
								+ ((Device) ssidVisibleDevice).getConnectedDeviceInfo().getDevicePortAddress());

						status = BroadBandWiFiUtils.validateIfWifiDriverIsIntelForClient(ssidVisibleDevice, tapEnv);
						LOGGER.info("Status of validateIfWifiDriverIsIntelForClient is : " + status);

						status = BroadBandWiFiUtils.validateVisibleStateOfBothSsidInConnectedClient(device, tapEnv,
								ssidVisibleDevice, BroadBandTestConstants.WIFI_PRIVATE, true);
						LOGGER.info("Status of validateVisibleStateOfBothSsidInConnectedClient is : " + status);

						if (CommonMethods.isNotNull(connectionType)
								&& connectionType.trim().equalsIgnoreCase(
										BroadBandConnectedClientTestConstants.STRING_CLIENT_DEVICE_CONNECTION_TYPE_WIFI)
								&& BroadBandWiFiUtils.validateIfWifiDriverIsIntelForClient(ssidVisibleDevice, tapEnv)
								&& BroadBandWiFiUtils.validateVisibleStateOfBothSsidInConnectedClient(device, tapEnv,
										ssidVisibleDevice, BroadBandTestConstants.WIFI_PRIVATE, true)) {
							LOGGER.info("PRE-CONDITION : ADDED INTEL CLIENT IN LIST");
							ssidVisibleDevices.add(ssidVisibleDevice);
							if (ssidVisibleDevices.size() >= requiredDevices) {
								break;
							}
						}
					} catch (Exception e) {
						errorMessage = e.getMessage();
						LOGGER.error("EXCEPTION IN VERIFYING SSID VISBILITY :" + errorMessage);
					}
				}
			}
			status = (ssidVisibleDevices.size() >= requiredDevices);
			if (status) {
				LOGGER.info("PRE-CONDITION " + preCondNumber
						+ " : ACTUAL : PRIVATE WIFI 2.4 GHZ AND 5 GHZ SSID's ARE BROADCASTED IN "
						+ ssidVisibleDevices.size() + " INTEL WIFIF ADAPTER CONNECTED SETUP");
			} else {
				errorMessage = "PRIVATE WIFI 2.4 GHZ AND 5 GHZ SSID's ARE BROADCASTED IN " + ssidVisibleDevices.size()
						+ " CONNECTED SETUP, MIMIMUM " + requiredDevices
						+ " INTEL WIFIF ADAPTER CONNECTED SETUP IS REQUIRED ";
				LOGGER.error("PRE-CONDITION " + preCondNumber + " : ACTUAL : " + errorMessage);
				throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + " PRE-CONDITION " + preCondNumber
						+ " : FAILED : " + errorMessage);
			}
		} catch (Exception e) {
			errorMessage = e.getMessage();
			LOGGER.error("PRE-CONDITION: EXCEPTION OCCURRED WHILE VERIFYING THE SSID VISIBLE STATUS" + errorMessage);

		}
		return ssidVisibleDevices;
	}

	/**
	 * Pre-Condition method to enable/disable the MESH status
	 * 
	 * @param device        instance of{@link Dut}
	 * @param tapEnv        instance of {@link AutomaticsTapApi}
	 * @param isEnabled     True - To be Enabled,False- To be Disabled
	 * @param preCondNumber Pre condition number
	 * @refactor Govardhan
	 */
	public static void executePreConditionToToggleMeshEnableOrDisableStatus(Dut device, AutomaticsTapApi tapEnv,
			boolean isEnabled, int preConStepNumber) throws TestException {
		String errorMessage = null;
		boolean status = false;
		/**
		 * PRECONDITION : ENABLE/DISABLE THE MESH STATUS
		 */
		LOGGER.info("#######################################################################################");
		LOGGER.info("PRE-CONDITION " + preConStepNumber + " : DESCRIPTION : " + (isEnabled ? "ENABLE" : "DISABLE")
				+ " THE MESH STATUS");
		LOGGER.info("PRE-CONDITION " + preConStepNumber + " : ACTION : " + (isEnabled ? "ENABLE" : "DISABLE")
				+ " THE MESH STATUS " + BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_MESH_ENABLE
				+ " STATUS AS " + (isEnabled ? "TRUE" : "FALSE") + " USING WEBPA");
		LOGGER.info("PRE-CONDITION " + preConStepNumber + " : EXPECTED : MESH STATUS MUST BE "
				+ (isEnabled ? "ENABLED" : "DISABLED"));
		LOGGER.info("#######################################################################################");
		errorMessage = "UNABLE TO " + (isEnabled ? "ENABLE" : "DISABLE") + " THE MESH STATUS";
		JSONObject jsonAttribute = new JSONObject();
		try {
			if (!isEnabled) {
				jsonAttribute.put(BroadBandTestConstants.NOTIFY, BroadBandTestConstants.CONSTANT_0);
				status = BroadBandWebPaUtils.setWebPaAttribute(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_MESH_ENABLE, jsonAttribute);
				LOGGER.info("Notify status for disabling mesh= " + status);
			}
		} catch (Exception exception) {
			errorMessage = "Unable to set Notify component to 0";
			LOGGER.error(errorMessage + " : " + exception.getMessage());
		}

		status = BroadBandWebPaUtils.setVerifyWebPAInPolledDuration(device, tapEnv,
				BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_MESH_ENABLE, WebPaDataTypes.BOOLEAN.getValue(),
				isEnabled ? BroadBandTestConstants.TRUE : BroadBandTestConstants.FALSE,
				BroadBandTestConstants.THREE_MINUTES, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		if (status) {
			LOGGER.info("PRE-CONDITION " + preConStepNumber + " : ACTUAL : SUCCESSFULLY "
					+ (isEnabled ? "ENABLED" : "DISABLED") + " THE MESH STATUS");
		} else {
			LOGGER.error("PRE-CONDITION " + preConStepNumber + " : ACTUAL : " + errorMessage);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION " + preConStepNumber
					+ " : FAILED : " + errorMessage);
		}
	}

	/**
	 * Pre-Condition method to enable the public wifi on Gateway
	 * 
	 * @param device        instance of{@link Dut}
	 * @param tapEnv        instance of {@link AutomaticsTapApi}
	 * @param preCondNumber Pre condition number
	 * @refactor Said Hisham
	 * 
	 */

	public static void executePreConditionToEnableThePublicWifiOnGatewayDevice(Dut device, AutomaticsTapApi tapEnv,
			int preCondNumber) throws TestException {

		String errorMessage = null;
		boolean status = false;
		BroadBandResultObject resultObject = null;
		/**
		 * PRE-CONDITION : SET AND VERIFY THE PUBLIC WIFI STATUS IS ENABLED
		 */
		LOGGER.info("#######################################################################################");
		LOGGER.info("PRE-CONDITION " + preCondNumber
				+ " : DESCRIPTION : SET  AND VERIFY THE PUBLIC WIFI STATUS IS ENABLED.");
		LOGGER.info("PRE-CONDITION " + preCondNumber + " : ACTION : EXECUTE WEBPA COMMAND:"
				+ BroadBandWebPaConstants.WEBPA_PARAM_ENABLING_PUBLIC_WIFI);
		LOGGER.info("PRE-CONDITION " + preCondNumber
				+ " : EXPECTED : PUBLIC WIFI MUST BE ENABLED AND IT MUST RETURN VALUE AS TRUE");
		LOGGER.info("#######################################################################################");
		errorMessage = "UNABLE TO ENABLE THE PUBLIC WIFI ON GATEWAY DEVICE";
		List<WebPaParameter> webPaParameters = BroadBandWebPaUtils.getListOfWebpaParametersForBothPublicWifis();
		resultObject = BroadBandWebPaUtils.executeSetAndGetOnMultipleWebPaGetParams(device, tapEnv, webPaParameters);
		status = resultObject.isStatus();
		errorMessage = resultObject.getErrorMessage();
		if (status) {
			LOGGER.info("PRE-CONDITION " + preCondNumber
					+ " : ACTUAL : SUCCESSFULLY ENABLED THE PUBLIC WIFI ON GATEWAY DEVICE ");
		} else {
			LOGGER.error("PRE-CONDITION " + preCondNumber + " : ACTUAL : " + errorMessage);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION " + preCondNumber
					+ " : FAILED : " + errorMessage);
		}
	}

	/**
	 * Pre-Condition method to verify the public wifi broadcasting status
	 * 
	 * @param device        instance of{@link Dut}
	 * @param tapEnv        instance of {@link AutomaticsTapApi}
	 * @param preCondNumber Pre condition number
	 * 
	 */
	public static void executePreConditionToVerifyPublicWifiBroadcastingStatus(Dut device, AutomaticsTapApi tapEnv,
			Dut deviceConnected, int preCondNumber) throws TestException {
		String errorMessage = null;
		boolean status = false;
		/**
		 * PRE-CONDITION : VERIFY THE PUBLIC WIFI 2.4 AND 5 GHZ SSID'S ARE BROADCASTING
		 * IN CONNECTED CLIENT
		 */
		LOGGER.info("#####################################################################################");
		LOGGER.info("PRE-CONDITION " + preCondNumber
				+ " : DESCRIPTION : VERIFY THE PUBLIC WIFI 2.4 AND 5 GHZ SSID'S ARE BROADCASTING IN CONNECTED CLIENT");
		LOGGER.info("PRE-CONDITION " + preCondNumber
				+ " : ACTION : EXECUTE COMMAND FOR WINDOWS: '1.NETSH WLAN SHOW NETWORKS | GREP -I '<PUBLIC_SSID_2_4GHZ>' ,2.NETSH WLAN SHOW NETWORKS | GREP -I '<PUBLIC_SSID_5GHZ>' OR LINUX : 'SUDO IWLIST WLAN0 SCAN'");
		LOGGER.info("PRE-CONDITION " + preCondNumber
				+ " : EXPECTED : PUBLIC WIFI 2.4 AND 5GHZ SSID'S SHOULD BE BROADCASTED IN CONNECTED CLIENT");
		LOGGER.info("#####################################################################################");
		errorMessage = "UNABLE TO VERIFY PUBLIC WIFI 2.4 AND 5 GHZ SSID'S ARE BROADCASTING STATUS IN CONNECTED CLIENT";
		status = BroadBandWiFiUtils.validateBothSsidVisibleStateInConnectedClient(device, tapEnv, deviceConnected,
				BroadBandTestConstants.PUBLIC_WIFI_TYPE, true);
		if (status) {
			LOGGER.info("PRE-CONDITION " + preCondNumber
					+ " : ACTUAL : PUBLIC WIFI 2.4 AND 5 GHZ SSID'S ARE BROADCASTED IN CONNECTED CLIENT");
		} else {
			LOGGER.error("PRE-CONDITION " + preCondNumber + " : ACTUAL : " + errorMessage);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION " + preCondNumber
					+ " : FAILED : " + errorMessage);
		}
	}

	/**
	 * Pre-Condition method to set the telemetry log interval
	 * 
	 * 
	 * @param tapEnv        instance of {@link AutomaticsTapApi}
	 * @param device        instance of{@link Dut}
	 * 
	 * @param preCondNumber Precondition number
	 * 
	 * @Refactor Sruthi Santhosh
	 * 
	 */
	public static void executePreConditionToSetTelemetryLogInterval(AutomaticsTapApi tapEnv, Dut device,
			int preCondNumber) {
		String errorMessage = null;
		boolean status = false;
		/**
		 * PRECONDITION : SET AND VERIFY THE NORMALIZED RSSI LSIT FOR PRIVATE WIFI
		 */
		LOGGER.info("#######################################################################################");
		LOGGER.info("PRE-CONDITION " + preCondNumber
				+ " : DESCRIPTION : SET AND VERIFY THE WIFI TELEMETRY LOG INTERVAL AS 5 MINUTES");
		LOGGER.info("PRE-CONDITION " + preCondNumber
				+ ": ACTION : SET AND VERIFY THE WIFI TELEMETRY LOG INTERVAL USING WEBPA PARAM "
				+ BroadBandWebPaConstants.WEBPA_PARAM_WIFI_TELEMETRY_LOG_INTERVAL);
		LOGGER.info("PRE-CONDITION " + preCondNumber
				+ " : EXPECTED : MUST SET THE WIFI TELEMETRY LOG INTERVAL AS 5 MINUTES");
		LOGGER.info("#######################################################################################");
		errorMessage = "FAILED TO SET THE WIFI TELEMETRY LOG INTERVAL";
		status = BroadBandWebPaUtils.setVerifyWebPAInPolledDuration(device, tapEnv,
				BroadBandWebPaConstants.WEBPA_PARAM_WIFI_TELEMETRY_LOG_INTERVAL, BroadBandTestConstants.CONSTANT_1,
				BroadBandTestConstants.STRING_300, BroadBandTestConstants.THREE_MINUTE_IN_MILLIS,
				BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
		if (status) {
			LOGGER.info("PRE-CONDITION " + preCondNumber
					+ ": ACTUAL : SUCCESSFULLY SET TELEMETRY LOG INTERVAL USING WEBPA SET OPERATION");
		} else {
			LOGGER.error("PRE-CONDITION " + preCondNumber + " : ACTUAL : " + errorMessage);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION " + preCondNumber
					+ " : FAILED : " + errorMessage);
		}
	}

	/**
	 * Pre-Condition method to verify the log file availability
	 * 
	 * @param tapEnv        instance of {@link AutomaticsTapApi}
	 * @param device        instance of{@link Dut}
	 * @param preCondNumber Precondition number
	 * @param logFileName   Log File name
	 * @param pollDuration  Poll Duration
	 * @param timeInterval  Time Interval
	 * 
	 * @Refactor Sruthi Santhosh
	 */
	public static void executePreConditionToVerifyIsLogFileExist(AutomaticsTapApi tapEnv, Dut settop, int preCondNumber,
			String logFileName, long pollDuration, long timeInterval) {
		String errorMessage = null;
		boolean status = false;
		/**
		 * PRECONDITION : Validate the Log file is present in the device
		 */
		LOGGER.info("#######################################################################################");
		LOGGER.info(
				"PRE-CONDITION " + preCondNumber + " : DESCRIPTION : VALIDATE THE " + logFileName + " FILE IS PRESENT");
		LOGGER.info("PRE-CONDITION " + preCondNumber + ": ACTION : EXECUTE COMMAND :");
		LOGGER.info(
				"PRE-CONDITION " + preCondNumber + " : EXPECTED : " + logFileName + " MUST BE PRESENT IN THE DEVICE");
		LOGGER.info("#######################################################################################");
		errorMessage = "FAILED TO  VERIFY THE " + logFileName + " IN THE DEVICE";
		long startTime = System.currentTimeMillis();
		do {
			status = CommonUtils.isFileExists(settop, tapEnv, logFileName);
		} while (!status && ((System.currentTimeMillis() - startTime) < pollDuration)
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, timeInterval));
		if (status) {
			LOGGER.info("PRE-CONDITION " + preCondNumber + ": ACTUAL :  " + logFileName + " IS PRESENT IN THE DEVICE");
		} else {
			LOGGER.error("PRE-CONDITION " + preCondNumber + " : ACTUAL : " + errorMessage);
			throw new TestException(BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION " + preCondNumber
					+ " : FAILED : " + errorMessage);
		}
	}

}

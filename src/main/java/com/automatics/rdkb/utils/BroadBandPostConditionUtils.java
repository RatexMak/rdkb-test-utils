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
import com.automatics.rdkb.constants.RDKBTestConstants.WiFiFrequencyBand;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;
import com.automatics.rdkb.utils.cdl.BroadBandCodeDownloadUtils;
import com.automatics.rdkb.utils.cdl.BroadBandXconfCdlUtils;
import com.automatics.rdkb.utils.telemetry.BroadBandTelemetry2Utils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.wifi.BroadBandWiFiUtils;
import com.automatics.rdkb.utils.wifi.connectedclients.BroadBandConnectedClientUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.webpa.WebPaParameter;

/**
 * Utility class which handles the Post condition related functionality and
 * verification.
 * 
 * @author Muthukumar
 */
public class BroadBandPostConditionUtils {

	/** SLF4J logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandPostConditionUtils.class);

	/**
	 * Post-Condition method to reactivate the device
	 * 
	 * @param tapEnv        {@link AutomaticsTapApi}
	 * @param device        {@link Dut}
	 * @param isReactivated It is 'True' Device already activated.'False' Device Not
	 *                      reactivated
	 * @refactor Govardhan
	 */
	public static void executePostConditionToReActivateDevice(Dut device, AutomaticsTapApi tapEnv,
			boolean isReactivated, int postConStepNumber) throws TestException {
		String errorMessage = null;
		boolean status = false;
		try {
			/**
			 * POST-CONDITION : BEGIN BROAD BAND DEVICE REACTIVATION
			 */
			LOGGER.info("#######################################################################################");
			LOGGER.info(
					"POST-CONDITION " + postConStepNumber + " : DESCRIPTION : BEGIN BROAD BAND DEVICE REACTIVATION.");
			LOGGER.info("POST-CONDITION " + postConStepNumber
					+ " : ACTION : BROAD BAND DEVICE REACTIVATION USING WEBPA OR SNMP. ");
			LOGGER.info("POST-CONDITION " + postConStepNumber + " : EXPECTED : DEVICE SHOULD GET REACTIVATED");
			LOGGER.info("#######################################################################################");
			if (!isReactivated) {
				errorMessage = "FAILED TO REACTIVATE THE ROUTER DEVICE";
				status = false;
				try {
					BroadBandWiFiUtils.reactivateDeviceUsingWebpaOrSnmp(tapEnv, device);
					status = true;
				} catch (TestException e) {
					errorMessage = e.getMessage();
				}
				if (status) {
					LOGGER.info("POST-CONDITION " + postConStepNumber
							+ " : ACTUAL: THE ROUTER DEVICE REACTIVATED SUCCESSFULLY.");
				} else {
					LOGGER.error("POST-CONDITION " + postConStepNumber + ": ACTUAL: " + errorMessage);
				}
			} else {
				LOGGER.info("POST-CONDITION " + postConStepNumber
						+ ": ACTUAL: SKIPPING REACTIVATION STEP AS THE DEVICE WAS ALREADY REACTIVATED SUCCESSFULLY IN PRE-CONDITION.");
			}
		} catch (Exception exception) {
			LOGGER.error(
					"Execution error occurred while executing to verify default radio status post conditions due to exception --> "
							+ exception.getMessage());
		}
	}

	/**
	 * Post-Condition method to trigger Original build on the device. Existing build
	 * on the device before starting the test
	 * 
	 * @param settop                  {@link Settop}
	 * @param hasLatestBuildChanged   It is 'True' - Latest build flashed.'False' -
	 *                                Latest build didn't flashed
	 * 
	 * @param hasOriginalBuildChanged It is 'True'- Orignal build flashed.'False' It
	 *                                is 'True' - Orignal build didn't flashed .
	 */
	public static void executePostConditionToTriggerCdl(Dut device, AutomaticsTapApi tapEnv,
			boolean hasLatestBuildChanged, boolean hasOriginalBuildChanged, int postConStepNumber,
			String initialFirmwareVersion) throws TestException {
		String errorMessage = null;
		boolean status = false;
		boolean isSTBAccessible = false;
		try {
			/**
			 * POST-CONDITION : PERFORM FLASHING THE ORIGINAL BUILD ON THE DEVICE
			 */

			isSTBAccessible = BroadBandCommonUtils.isSTBAccessible(tapEnv, device,
					BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS, BroadBandTestConstants.CONSTANT_5);
			if (isSTBAccessible) {
				LOGGER.info(
						"DEVICE IS SSHABLE GOING TO TRIGGER REQUIRED IMAGE  ON THE DEVICE " + initialFirmwareVersion);
				if (hasLatestBuildChanged && !hasOriginalBuildChanged) {

					postConStepNumber++;
					LOGGER.info(
							"#######################################################################################");
					LOGGER.info("POST-CONDITION " + postConStepNumber
							+ " : DESCRIPTION : PERFORM FLASHING THE ORIGINAL BUILD ON THE DEVICE: "
							+ initialFirmwareVersion);
					LOGGER.info("POST-CONDITION " + postConStepNumber
							+ " : ACTION : FLASH THE ORIGINAL BUILD ON THE DEVICE USING HTTP/ TR-181. ");
					LOGGER.info("POST-CONDITION " + postConStepNumber
							+ " : EXPTECTED : ORIGINAL IMAGE MUST BE FLASHED ON THE DEVICE");
					LOGGER.info(
							"#######################################################################################");
					errorMessage = "UNABLE TO FLASH THE ORIGINAL BUILD ON THE DEVICE. ";
					status = BroadBandCodeDownloadUtils.triggerPreviousCodeDownload(device, tapEnv,
							initialFirmwareVersion);
					if (status) {
						LOGGER.info("POST-CONDITION " + postConStepNumber
								+ " : ACTUAL : ORIGINAL IMAGE FLASHED SUCCESSFULLY ON THE DEVICE.");
					} else {
						LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL :" + errorMessage);
					}
				} else {
					errorMessage = "Failed to revert back the original build as the device is not SSHable";
					LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL :" + errorMessage);
				}
			}
		} catch (Exception exception) {
			LOGGER.error(
					"Execution error occurred while executing to verify default radio status post conditions due to exception --> "
							+ exception.getMessage());
		}
	}

	/**
	 * Post-Condition method to clear the CDL information URL in
	 * /nvram/swupdate.conf
	 * 
	 * @param settop              instance of{@link Settop}
	 * @param tapEnv              instance of {@link AutomaticsTapApi}
	 * @param postConditionNumber String to hold post condition number.
	 */
	public static void executePostConditionToClearCdlInfoInXconf(Dut device, AutomaticsTapApi tapEnv,
			int postConStepNumber) {
		boolean status = false;
		String errorMessage = null;
		LOGGER.info("#######################################################################################");
		LOGGER.info("POST-CONDITION " + postConStepNumber
				+ " : DESCRIPTION:Clear the code download URL information in /nvram/swupdate.conf");
		LOGGER.info("POST-CONDITION " + postConStepNumber + " : ACTION : Set the image server url as empty");
		LOGGER.info("POST-CONDITION " + postConStepNumber
				+ " : EXPECTED : Must clear the code download URL information in /nvram/swupdate.conf");
		LOGGER.info("#######################################################################################");
		errorMessage = "Failed to Clear the code download URL information in /nvram/swupdate.conf";
		status = BroadBandXconfCdlUtils.toClearCdlInfoInXconf(device, tapEnv);
		if (status) {
			LOGGER.info("POST-CONDITION " + postConStepNumber
					+ " :Successfully cleared the code download URL information in /nvram/swupdate.conf ");
		} else {
			LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL : " + errorMessage);
		}
	}

	/**
	 * Post-Condition method to remove backup file
	 * 
	 * @param device              {@link Dut}
	 * 
	 * @param tapEnv              instance of {@link AutomaticsTapApi}
	 * 
	 * @param postConditionNumber int to hold post condition number.
	 * 
	 * @param String              File to be removed.
	 */
	public static void executePostConditionToRemoveBackUpFile(Dut device, AutomaticsTapApi tapEnv,
			int postConStepNumber, String fileToBeRemoved) throws TestException {
		String errorMessage = null;
		boolean status = false;
		LOGGER.info("#######################################################################################");
		LOGGER.info("POST-CONDITION " + postConStepNumber + " : DESCRIPTION : Remove File and verify File removal");
		LOGGER.info("POST-CONDITION " + postConStepNumber
				+ " : ACTION : Execute command to Remove File and verify File removal.");
		LOGGER.info("POST-CONDITION " + postConStepNumber + " : EXPTECTED : File must be removed successfully");
		LOGGER.info("#######################################################################################");
		errorMessage = "Failed to remove the file from the directory";
		try {
			if (CommonUtils.isFileExists(device, tapEnv, fileToBeRemoved)) {
				status = CommonUtils.removeFileandVerifyFileRemoval(tapEnv, device, fileToBeRemoved);
			} else {
				errorMessage = "File not available";
			}
		} catch (Exception e) {
			errorMessage += e.getMessage();
			LOGGER.error(errorMessage);
		}
		if (status) {
			LOGGER.info("POST-CONDITION " + postConStepNumber + " : ACTUAL : File removed successfully ");

		} else {
			LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL : " + errorMessage);
		}
	}

	/**
	 * Method to perform steps to enable Telemetry 2 configs via RFC
	 * 
	 * @param device     {@link Instanceof Dut}
	 * @param stepNumber step number to start with
	 * @param testCaseId test case ID to update
	 */
	public static void PostConditionToUpdateTelemetryVer1SettingsViaRFC(Dut device, int stepNumber,
			AutomaticsTapApi tapEnv) {
		try {

			boolean status = false;
			String errorMessage = null;

			errorMessage = "Failed to configure RFC payload for telemetry 2 configurations";
			status = false;

			LOGGER.info("##################################################################################");
			LOGGER.info("POST-CONDITION " + stepNumber
					+ ": DESCRIPTION : Configure RFC payload to set telemetry 1 configurations");
			LOGGER.info("POST-CONDITION " + stepNumber
					+ ": ACTION : 1. Copy and update /nvram/rfc.properties with mock RFC config server URL2. "
					+ "Post payload after replacing ESTB mac and enable/disable value to RFC URL");
			LOGGER.info("POST-CONDITION " + stepNumber
					+ ": EXPECTED : Successfully rebooted or triggered check-in after configuring RFC payload");
			LOGGER.info("##################################################################################");

			if (BroadBandRfcFeatureControlUtils.executePreconditionForRfcTests(device, tapEnv,
					AutomaticsTapApi.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_TELEMETRY_VER_2_CONFIG)
							.replaceAll(BroadBandTestConstants.TELEMETRY_ENABLE_VAUE, BroadBandTestConstants.FALSE)
							.replaceAll(BroadBandTestConstants.TELEMETRY_VERSION_VALUE,
									BroadBandTestConstants.STRING_VALUE_ONE)
							.replaceAll(BroadBandTestConstants.TELEMETRY_CONFIG_URL_VALUE, " "))) {
				errorMessage = "Unable to reboot device successfully";
				status = CommonMethods.rebootAndWaitForIpAccusition(device, tapEnv);
			}

			if (status) {
				LOGGER.info("POST-CONDITION " + stepNumber
						+ ": ACTUAL : Successfully rebooted or triggered check-in after configuring RFC payload");
			} else {
				LOGGER.error("POST-CONDITION " + stepNumber + ": ACTUAL : " + errorMessage);
			}
			LOGGER.info("##################################################################################");
			if (status) {
				++stepNumber;
				status = false;
				errorMessage = "Setting the telemetry version 1 configurations via RFC has failed";

				LOGGER.info("##################################################################################");
				LOGGER.info("POST-CONDITION " + stepNumber
						+ ": DESCRIPTION : Verify if telemetry version 1 params are updated by RFC");
				LOGGER.info("POST-CONDITION " + stepNumber
						+ ": ACTION : 1. Verify /tmp/rfc_configdata.txt contains posted parameter value2."
						+ " Verify log message for updation in /rdklogs/logs/dcmrfc.log3. Verify parameter value is changed");
				LOGGER.info(
						"POST-CONDITION " + stepNumber + ": EXPECTED : Successfully set parameter value through RFC");
				LOGGER.info("##################################################################################");

				status = BroadBandRfcFeatureControlUtils
						.verifyParameterUpdatedByRfc(device, tapEnv,
								BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS.FEATURE_ENABLE.getParam(),
								BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS.FEATURE_ENABLE.getFactoryDefault())
						.isStatus()
						&& BroadBandRfcFeatureControlUtils.verifyParameterUpdatedByRfc(device, tapEnv,
								BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS.CONFIG_URL.getParam(),
								BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS.CONFIG_URL.getFactoryDefault())
								.isStatus()
						&& BroadBandRfcFeatureControlUtils
								.verifyParameterUpdatedByRfc(device, tapEnv,
										BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS.VERSION.getParam(),
										BroadBandTestConstants.TELEMETRY_2_WEBPA_SETTINGS.VERSION.getFactoryDefault())
								.isStatus();

				if (status) {
					LOGGER.info(
							"POST-CONDITION " + stepNumber + ": ACTUAL : Successfully set parameter value through RFC");
				} else {
					LOGGER.error("POST-CONDITION " + stepNumber + ": ACTUAL : " + errorMessage);
				}
				LOGGER.info("##################################################################################");

				++stepNumber;
				errorMessage = "The configuration set via RFC for telemetry 1 configurations has not taken effect";
				status = false;

				LOGGER.info("##################################################################################");
				LOGGER.info("POST-CONDITION " + stepNumber
						+ ": DESCRIPTION : Validate from webpa that configuration of telemetry 1 configurations via RFC is reflecting");
				LOGGER.info("POST-CONDITION " + stepNumber
						+ ": ACTION : Execute WebPa GET command on the telemetry 1 webpa paramas");
				LOGGER.info(
						"POST-CONDITION " + stepNumber + ": EXPECTED : The set values should be returned via webpa");
				LOGGER.info("##################################################################################");

				status = BroadBandTelemetry2Utils.validateFactoryDefaultsForTelemetryParams(device, tapEnv);

				if (status) {
					LOGGER.info("POST-CONDITION " + stepNumber
							+ ": ACTUAL : The configuration set via RFC for telemetry 1 is successful");
				} else {
					LOGGER.error("POST-CONDITION " + stepNumber + ": ACTUAL : " + errorMessage);
				}
				LOGGER.info("##################################################################################");
			}
		} catch (TestException e) {
			LOGGER.error("Exception occured while configuring telemetry 2 settings via RFC", e);
		}

	}

	/**
	 * Post-Condition method to enable/disable the Moca status
	 * 
	 * @param device              instance of{@link Dut}
	 * @param tapEnv              instance of {@link AutomaticsTapApi}
	 * @param isEnabled           True - To be Enabled,False- To be Disabled
	 * @param postConditionNumber Parameter to hold post condition number.
	 * 
	 */
	public static void executePostConditionRevertDefaultMocaStatus(Dut device, AutomaticsTapApi tapEnv,
			boolean isEnabled, int postConStepNumber) throws TestException {
		String errorMessage = null;
		boolean status = false;
		/**
		 * POST CONDITION : ENABLE/DISABLE THE MoCA STATUS
		 */
		LOGGER.info("#######################################################################################");
		LOGGER.info("POST-CONDITION " + postConStepNumber + " : DESCRIPTION : " + (isEnabled ? "ENABLE" : "DISABLE")
				+ " THE MoCA STATUS");
		LOGGER.info("POST-CONDITION " + postConStepNumber + ": ACTION : " + (isEnabled ? "ENABLE" : "DISABLE")
				+ " THE MoCA STATUS " + BroadBandWebPaConstants.WEBPA_PARAM_FOR_MOCA_INTERFACE_ENABLE + " STATUS AS "
				+ (isEnabled ? "TRUE" : "FALSE") + " USING WEBPA");
		LOGGER.info("POST-CONDITION " + postConStepNumber + " : EXPECTED : MoCA STATUS MUST BE "
				+ (isEnabled ? "ENABLED" : "DISABLED"));
		LOGGER.info("#######################################################################################");
		errorMessage = "UNABLE TO " + (isEnabled ? "ENABLE" : "DISABLE") + " THE MoCA STATUS";
		try {
			status = BroadBandWebPaUtils.setVerifyWebPAInPolledDuration(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_FOR_MOCA_INTERFACE_ENABLE, BroadBandTestConstants.CONSTANT_3,
					isEnabled ? BroadBandTestConstants.TRUE : BroadBandTestConstants.FALSE,
					BroadBandTestConstants.THREE_MINUTES, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
			if (status) {
				LOGGER.info("POST-CONDITION " + postConStepNumber + ": ACTUAL : SUCCESSFULLY "
						+ (isEnabled ? "ENABLED" : "DISABLED") + " THE  MoCA STATUS");
			} else {
				LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL : " + errorMessage);
			}
		} catch (Exception exception) {
			LOGGER.error("Execution error occured while switching the MoCA status  --> " + exception.getMessage());
		}
	}

	/**
	 * Post-Condition method to disable the xifnity wifi
	 * 
	 * @param device              instance of{@link Dut}
	 * @param tapEnv              instance of {@link AutomaticsTapApi}
	 * @param postConditionNumber String to hold post condition number.
	 */
	public static void executePostConditionToDisableXfinityWifi(Dut device, AutomaticsTapApi tapEnv,
			int postConStepNumber) {
		boolean status = false;
		String errorMessage = null;
		try {
			LOGGER.info("#######################################################################################");
			LOGGER.info("POST-CONDITION " + postConStepNumber
					+ " : DESCRIPTION : VERIFY THE XFINITYWIFI STATUS IS DISABLED");
			LOGGER.info("POST-CONDITION " + postConStepNumber + " : ACTION : EXECUTE WEBPA COMMAND:"
					+ BroadBandWebPaConstants.WEBPA_PARAM_ENABLING_PUBLIC_WIFI);
			LOGGER.info("POST-CONDITION " + postConStepNumber + " : EXPECTED : PUBLIC WIFI MUST BE DISABLED");
			LOGGER.info("#######################################################################################");
			errorMessage = "UNABLE TO DISABLE THE PUBLIC WIFI ON GATEWAY DEVICE";
			status = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_ENABLING_PUBLIC_WIFI, BroadBandTestConstants.CONSTANT_3,
					BroadBandTestConstants.FALSE);
			if (status) {
				LOGGER.info("POST-CONDITION " + postConStepNumber
						+ " : ACTUAL : SUCCESSFULLY DISABLED THE PUBLIC WIFI ON GATEWAY DEVICE");
			} else {
				LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL : " + errorMessage);
			}

		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL : " + e.getMessage());
		}
	}

	/**
	 * Post-Condition method to set the default channel values using webpa .
	 * 
	 * <li></li>
	 * <li>Get the default Channel selection mode for 2.4 GHz</li>
	 * <li>Get the default Channel No for for 2.4 GHz</li>
	 * <li>Get the default Channel selection mode for 5 GHz</li>
	 * <li>Get the default Channel No for for 5 GHz</li>
	 * 
	 * @param device                  {@link Dut}
	 * @param tapEnv                  instance of {@link AutomaticsTapApi}
	 * @param defaultChannelValuesMap Its used to get the key and pair value for
	 *                                default channel values
	 * @return postCondStatus True ,IF all default values are set successfully. Else
	 *         False
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean executePostConditionToSetTheDefaultChannelValues(Dut device, AutomaticsTapApi tapEnv,
			HashMap<String, String> defaultChannelValuesMap) {
		boolean postCondStatus = false;
		try {
			if (defaultChannelValuesMap.get(BroadBandTestConstants.DEFAULT_CHANNEL_SELECTION_2GHZ) != null) {
				postCondStatus = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_2GHZ,
						BroadBandTestConstants.CONSTANT_3,
						defaultChannelValuesMap.get(BroadBandTestConstants.DEFAULT_CHANNEL_SELECTION_2GHZ));
			}
			if (defaultChannelValuesMap.get(BroadBandTestConstants.DEFAULT_CHANNEL_SELECTION_5GHZ) != null) {
				postCondStatus = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_5GHZ,
						BroadBandTestConstants.CONSTANT_3,
						defaultChannelValuesMap.get(BroadBandTestConstants.DEFAULT_CHANNEL_SELECTION_5GHZ));
			}
			if (defaultChannelValuesMap.get(BroadBandTestConstants.DEFAULT_CHANNEL_NO_2GHZ) != null) {
				postCondStatus = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_RADIO_CHANNEL_IN_2GHZ,
						BroadBandTestConstants.CONSTANT_2,
						defaultChannelValuesMap.get(BroadBandTestConstants.DEFAULT_CHANNEL_NO_2GHZ));
			}
			if (defaultChannelValuesMap.get(BroadBandTestConstants.DEFAULT_CHANNEL_NO_5GHZ) != null) {
				postCondStatus = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_RADIO_CHANNEL_IN_5GHZ,
						BroadBandTestConstants.CONSTANT_2,
						defaultChannelValuesMap.get(BroadBandTestConstants.DEFAULT_CHANNEL_NO_5GHZ));
			}
			HashMap<String, String> applySettings = new HashMap<String, String>();
			applySettings.put(BroadBandWebPaConstants.WEBPA_PARAM_WIFI_2_4_APPLY_SETTING, BroadBandTestConstants.TRUE);
			applySettings.put(BroadBandWebPaConstants.WEBPA_PARAM_WIFI_5_APPLY_SETTING, BroadBandTestConstants.TRUE);
			BroadBandWebPaUtils.executeMultipleWebpaParametersSet(device, tapEnv, applySettings,
					WebPaDataTypes.BOOLEAN.getValue());
		} catch (Exception exec) {
			LOGGER.error("Failed to Set the default Channel Values in Post Condition " + exec.getLocalizedMessage());
		}
		return postCondStatus;
	}

	/**
	 * Post-Condition method to enable the public wifi
	 * 
	 * @param device              instance of{@link Dut}
	 * @param tapEnv              instance of {@link AutomaticsTapApi}
	 * @param postConditionNumber String to hold post condition number.
	 * @refactor Govardhan
	 */
	public static void executePostConditionToEnablePublicWifi(Dut device, AutomaticsTapApi tapEnv,
			int postConStepNumber) {
		boolean status = false;
		String errorMessage = null;
		try {
			LOGGER.info("#######################################################################################");
			LOGGER.info(
					"POST-CONDITION " + postConStepNumber + " : DESCRIPTION : VERIFY THE PUBLICWIFI STATUS IS ENABLED");
			LOGGER.info("POST-CONDITION " + postConStepNumber + " : ACTION : EXECUTE WEBPA COMMAND:"
					+ BroadBandWebPaConstants.WEBPA_PARAM_ENABLING_PUBLIC_WIFI);
			LOGGER.info("POST-CONDITION " + postConStepNumber + " : EXPECTED : PUBLIC WIFI MUST BE ENABLED");
			LOGGER.info("#######################################################################################");
			errorMessage = "FAILED TO ENABLE THE PUBLIC WIFI ON GATEWAY DEVICE";
			status = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_ENABLING_PUBLIC_WIFI, BroadBandTestConstants.CONSTANT_3,
					BroadBandTestConstants.TRUE);
			if (status) {
				LOGGER.info("POST-CONDITION " + postConStepNumber
						+ " : ACTUAL : SUCCESSFULLY ENABLED THE PUBLIC WIFI ON GATEWAY DEVICE");
			} else {
				LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL : " + errorMessage);
			}

		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL : " + e.getMessage());
		}
	}

	/**
	 * Method to execute Post-condition steps for Band steering Disabling
	 * Bandsteering and Enabling Mesh via WebPa
	 * 
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * @refactor Said Hisham
	 */

	public static void executePostconditionForBandSteering(AutomaticsTapApi tapEnv, Dut device) {

		boolean status = false;
		LOGGER.info("######################################################");

		LOGGER.info("### POST-CONDITION ### Enable Mesh");

		status = BroadBandMeshUtils.enableOrDisableMesh(device, tapEnv, true);

		if (status)
			LOGGER.info("POST-CONDITION : ACTUAL :Mesh has been enabled again!");
		else
			LOGGER.error("POST-CONDITION : ACTUAL :Mesh was not enabled successfully!");

		LOGGER.info("### POST-CONDITION ### Enable AutoChannel on radios");

		List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
		Map<String, String> webpaResponse = null;

		webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
				BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_2GHZ, AutomaticsConstants.TRUE,
				WebPaDataTypes.BOOLEAN.getValue()));
		webPaParameters.add(BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
				BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_5GHZ, AutomaticsConstants.TRUE,
				WebPaDataTypes.BOOLEAN.getValue()));
		webpaResponse = tapEnv.executeMultipleWebPaSetCommands(device, webPaParameters);
		status = null != webpaResponse && !webpaResponse.isEmpty()
				&& BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
						BroadBandTestConstants.SUCCESS_TXT,
						webpaResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_2GHZ))
				&& BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
						BroadBandTestConstants.SUCCESS_TXT,
						webpaResponse.get(BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_AUTOCHANNELENABLE_STATUS_5GHZ));

		if (status) {
			LOGGER.info("POST-CONDITION : ACTUAL :Auto channel has been enabled again!");
		} else {
			LOGGER.error("POST-CONDITION : ACTUAL :Auto channel was not enabled successfully!");
		}
		LOGGER.info("POST-CONFIGURATIONS : FINAL STATUS - " + status);
		LOGGER.info("################### COMPLETED POST-CONFIGURATIONS ###################");
	}

	/**
	 * Post-Condition method to verify the default radio status
	 * 
	 * @param device {@link Dut}
	 * @Refactor Sruthi Santhosh
	 * 
	 */
	public static void executePostConditionToVerifyDefaultRadioStatus(Dut device, AutomaticsTapApi tapEnv,
			int postConStepNumber) throws TestException {
		String errorMessage = null;
		boolean status = false;
		try {/**
				 * POST-CONDITION : VERIFY THE 2.4 GHZ RAIDO STATUS AS ENABLED
				 */
			LOGGER.info("#######################################################################################");
			LOGGER.info("POST-CONDITION " + postConStepNumber
					+ " : DESCRIPTION : VERIFY THE 2.4 GHZ RADIO STATUS TO BE ENABLED .");
			LOGGER.info("POST-CONDITION " + postConStepNumber
					+ " : ACTION : VERIFY THE 2.4 GHZ RADIO STATUS TO BE ENABLED USING WEBPA.");
			LOGGER.info("POST-CONDITION " + postConStepNumber
					+ " : EXPTECTED : SUCCESSFULLY VERIFIED THE 2.4 GHZ RADIO STATUS AS 'TRUE' ");
			LOGGER.info("#######################################################################################");
			status = false;
			errorMessage = "UNABLE TO VERIFY THE 2.4 GHZ RADIO STATUS AS 'TRUE'.";
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
				LOGGER.info("POST-CONDITION " + postConStepNumber
						+ " : ACTUAL : SUCCESSFULLY VERIFIED/CHANGED THE 2.4 GHZ RADIO STATUS AS 'TRUE'");
			} else {
				LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL :" + errorMessage);
			}

			/**
			 * POST-CONDITION :VERIFY THE THE 5 GHZ RAIDO STATUS AS ENABLED USING WEBPA
			 */
			postConStepNumber++;
			status = false;
			LOGGER.info("#######################################################################################");
			LOGGER.info("POST-CONDITION " + postConStepNumber
					+ " : DESCRIPTION : VERIFY THE 5 GHZ RADIO STATUS TO BE ENABLED .");
			LOGGER.info("POST-CONDITION " + postConStepNumber
					+ " : ACTION : VERIFY THE 5 GHZ RADIO STATUS TO BE ENABLED USING WEBPA.");
			LOGGER.info("POST-CONDITION " + postConStepNumber
					+ " : EXPTECTED : SUCCESSFULLY VERIFIED THE 5 GHZ RADIO STATUS AS 'TRUE' ");
			LOGGER.info("#######################################################################################");
			status = false;
			errorMessage = "UNABLE TO VERIFY THE 5 GHZ RADIO STATUS AS 'TRUE'.";
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
				LOGGER.info("POST-CONDITION " + postConStepNumber
						+ " : ACTUAL : SUCCESSFULLY VERIFIED/CHANGED THE 5 GHZ RADIO STATUS AS 'TRUE'");
			} else {
				LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL :" + errorMessage);
			}
		} catch (Exception exception) {
			LOGGER.error(
					"Execution error occurred while executing to verify default radio status post conditions due to exception --> "
							+ exception.getMessage());
		}
	}

	/**
	 * Post-Condition method to enable the ethwan mode
	 * 
	 * @param device              instance of{@link Dut}
	 * @param tapEnv              instance of {@link AutomaticsTapApi}
	 * @param postConditionNumber String to hold post condition number.
	 * @author Rucha Sonawane
	 * @refactor Athira
	 */
	public static void executePostConditionToEnableEthwanMode(Dut device, AutomaticsTapApi tapEnv,
			int postConStepNumber) {
		boolean status = false;
		String errorMessage = null;
		Boolean ethwanDisableStatus = false;
		try {
			LOGGER.info("#######################################################################################");
			LOGGER.info("POST-CONDITION " + postConStepNumber
					+ " : DESCRIPTION : SET AND VERIFY THE ETHWAN MODE STATUS AS DISABLED");
			LOGGER.info(
					"POST-CONDITION " + postConStepNumber + " : ACTION : EXECUTE WEBPA COMMAND TO ENABLE ETHWAN MODE");
			LOGGER.info("POST-CONDITION " + postConStepNumber + " : EXPECTED : ETHWAN MODE MUST BE ENABLED");
			LOGGER.info("#######################################################################################");
			errorMessage = "UNABLE TO ENALE ETHWAN MODE ON GATEWAY DEVICE";

			status = BroadBandWebPaUtils.getAndVerifyWebpaValueInPolledDuration(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_ETHWAN_MODE_ENABLE, BroadBandTestConstants.TRUE,
					BroadBandTestConstants.TWO_MINUTE_IN_MILLIS, BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS);

			if (status) {
				LOGGER.info("POST-CONDITION " + postConStepNumber
						+ " ACTUAL : Successfully verified device in ETHWAN Mode.");
			} else {
				ethwanDisableStatus = BroadBandWebPaUtils.setWebPAInPolledDuration(device, tapEnv,
						"Device.X_RDKCENTRAL-COM_EthernetWAN.SelectedOperationalMode",
						BroadBandTestConstants.CONSTANT_0,
						BroadBandTestConstants.STRING_DEVICE_OPERATIONAL_MODE_ETHERNET,
						BroadBandTestConstants.THREE_MINUTE_IN_MILLIS, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
				if (!ethwanDisableStatus) {

					ethwanDisableStatus = BroadBandWebPaUtils.setWebPAInPolledDuration(device, tapEnv,
							BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_ETHWAN_MODE_ENABLE,
							BroadBandTestConstants.CONSTANT_3, BroadBandTestConstants.TRUE,
							BroadBandTestConstants.THREE_MINUTE_IN_MILLIS,
							BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
				} else if (ethwanDisableStatus) {
					status = CommonMethods.isSTBRebooted(tapEnv, device, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS,
							BroadBandTestConstants.CONSTANT_10);
					LOGGER.info("POST-CONDITION " + postConStepNumber
							+ " : ACTUAL: Successfully enabled ETHWAN Mode on DUT");
				} else {
					LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL: Failed to enable Ethwan Mode "
							+ BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_ETHWAN_MODE_ENABLE + " webpa params");
				}
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL : " + e.getMessage());
		}
	}

	/**
	 * Post-Condition method to enable the xifnity wifi
	 * 
	 * @param device              instance of{@link Dut}
	 * @param tapEnv              instance of {@link AutomaticsTapApi}
	 * @param postConditionNumber String to hold post condition number.
	 */

	public static void executePostConditionToEnableOrDisablePublicWifiBasedOnStbProperty(Dut device,
			AutomaticsTapApi tapEnv, int postConStepNumber) {
		boolean status = false;
		String errorMessage = null;
		try {
			LOGGER.info(" Going to get property value for " + BroadBandTestConstants.KEY_FOR_PUBLIC_WIFI_WHITELISTING);
			String publicWifiValue = AutomaticsTapApi
					.getSTBPropsValue(BroadBandTestConstants.KEY_FOR_PUBLIC_WIFI_WHITELISTING);
			String loggerString = publicWifiValue.equalsIgnoreCase(BroadBandTestConstants.TRUE) ? "ENABLED"
					: "DISABLED";
			LOGGER.info("#######################################################################################");
			LOGGER.info("POST-CONDITION " + postConStepNumber + " : DESCRIPTION : VERIFY THE PUBLICWIFI STATUS IS "
					+ loggerString);
			LOGGER.info("POST-CONDITION " + postConStepNumber + " : ACTION : EXECUTE WEBPA COMMAND:"
					+ BroadBandWebPaConstants.WEBPA_PARAM_ENABLING_PUBLIC_WIFI);
			LOGGER.info("POST-CONDITION " + postConStepNumber + " : EXPECTED : PUBLIC WIFI MUST BE " + loggerString);

			LOGGER.info("#######################################################################################");
			errorMessage = "FAILED TO " + loggerString + " THE PUBLIC WIFI ON GATEWAY DEVICE";

			LOGGER.info("Going to check and set PublicWifi as configured in Stb.properties");

			status = BroadBandWiFiUtils.checkAndSetPublicWifi(device, tapEnv);

			if (status) {
				LOGGER.info("POST-CONDITION " + postConStepNumber + " : ACTUAL : SUCCESSFULLY " + loggerString
						+ " THE PUBLIC WIFI ON GATEWAY DEVICE");
			} else {
				LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL : " + errorMessage);
			}

		} catch (Exception e) {

			LOGGER.info(e.getMessage());

			LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL : " + e.getMessage());

		}
	}

	/**
	 * Method to delete temporary stored files
	 * 
	 * @param device        Dut instance
	 * @param tapEnv        AutomaticsTapApi instance
	 * @param postCondition int postcondition step
	 * @param backupMap     Map<String,String> contains file with path
	 */
	public static void executePostCondtDeleteTemporaryFilesInGateway(Dut device, AutomaticsTapApi tapEnv,
			int postCondition, Map<String, String> backupMap) {
		boolean status = false;
		boolean atomSyncStatus = false;
		String errorMessage = "Failed to delete temporary file from NVRAM";
		/**
		 * POST-CONDITION : Delete Temporary Files in given path
		 */

		LOGGER.info("##########################################################################");
		LOGGER.info("POST-CONDITION " + postCondition + ": DESCRIPTION : Delete Temporary Files in given path");
		LOGGER.info("POST-CONDITION " + postCondition + ": ACTION : Execute Command rm -rf <filename>");
		LOGGER.info("POST-CONDITION " + postCondition + ": EXPECTED : File Should be removed Successfully");
		LOGGER.info("##########################################################################");
		try {
			atomSyncStatus = CommonMethods.isAtomSyncAvailable(device, tapEnv);
			for (Map.Entry<String, String> backFile : backupMap.entrySet()) {
				status = atomSyncStatus
						? BroadBandCommonUtils.removeFileAndVerifyStatus(tapEnv, device, backFile.getValue())
						: CommonUtils.deleteFile(device, tapEnv, backFile.getValue());
				LOGGER.info(backFile.getValue() + " : File deleted Status: " + status);
			}
		} catch (Exception e) {
			LOGGER.error("Exception caught while deleting file" + e.getMessage());
		}
		if (status) {
			LOGGER.info("POST-CONDITION " + postCondition + ": ACTUAL : Successfully deleted the Temporary Files");
		} else {
			LOGGER.error("POST-CONDITION " + postCondition + ": ACTUAL : " + errorMessage);
		}
	}

	/**
	 * Post-Condition method to start the MESH using system commands
	 * 
	 * @param device            instance of{@link Dut}
	 * @param tapEnv            instance of {@link tapEnv}
	 * @param meshInitialStatus meshInitialStatus to be verified after starting mesh
	 * @param preCondNumber     post condition number
	 * 
	 */
	public static void executePostConditionToStartMeshUsingSystemCommands(Dut device, AutomaticsTapApi tapEnv,
			String meshInitialStatus, int postConStepNumber) throws TestException {
		String errorMessage = null;
		boolean isMeshStatus = false;
		if (!DeviceModeHandler.isBusinessClassDevice(device)) {
			// Perform mesh enabling in postcondition only for Residential device when Mesh
			// is enabled
			if (BroadBandWebPaUtils.getAndVerifyWebpaValueInPolledDuration(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_MESH_ENABLE, BroadBandTestConstants.FALSE,
					BroadBandTestConstants.ONE_MINUTE_IN_MILLIS, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS)) {

				/**
				 * POSTCONDITION : START THE MESH STATUS
				 */
				LOGGER.info("#######################################################################################");
				LOGGER.info("POST-CONDITION " + postConStepNumber + " : DESCRIPTION : START THE MESH SERVICE");
				LOGGER.info("POST-CONDITION " + postConStepNumber
						+ " : ACTION :  + Execute system command to start mesh and verify "
						+ BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_MESH_ENABLE);
				LOGGER.info("POST-CONDITION " + postConStepNumber + " : EXPECTED : MESH SERVICE MUST BE STARTED");
				LOGGER.info("#######################################################################################");
				errorMessage = "UNABLE TO START THE MESH SERVICE";
				try {
					if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
						tapEnv.executeCommandOnAtom(device, BroadBandCommandConstants.CMD_SYSTEM_START_MESH_AGENT);
						tapEnv.executeCommandOnAtom(device, BroadBandCommandConstants.CMD_SYSTEM_START_MESH_SERVICE);
					} else {
						tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_SYSTEM_START_MESH_AGENT);
						tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_SYSTEM_START_MESH_SERVICE);

					}
					isMeshStatus = BroadBandWebPaUtils.getAndVerifyWebpaValueInPolledDuration(device, tapEnv,
							BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_MESH_ENABLE, meshInitialStatus,
							BroadBandTestConstants.ONE_MINUTE_IN_MILLIS,
							BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
				} catch (Exception e) {
					LOGGER.error("Unable to start mesh service due to " + e.getMessage());
				}
				if (isMeshStatus) {
					LOGGER.info("POST-CONDITION " + postConStepNumber
							+ " : ACTUAL : SUCCESSFULLY STARTED THE MESH SERVICE");
				} else {
					LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL : " + errorMessage);
				}
			}
		}
	}

	/**
	 * Post-Condition method to set the initial values for common webpa parameters
	 * 
	 * @param device           instance of{@link Dut}
	 * @param tapEnv           instance of {@link AutomaticsTapApi}
	 * @param postCondNumber   Post condition number
	 * @param initialMapValues Instance of {Map}
	 * @refactor Govardhan
	 */
	public static void executePostConditionToSetTheDefaultValuesForCommonParams(Dut device, AutomaticsTapApi tapEnv,
			int postCondNumber, Map<String, String> initialMapValues) throws TestException {
		String errorMessage = null;
		boolean status = false;
		Map<String, String> commonParamValues = null;
		/**
		 * POST-CONDITION : SET THE INITIAL VALUES FOR COMMON WEBPA PARAMETERS
		 */
		LOGGER.info("#######################################################################################");
		LOGGER.info("POST-CONDITION " + postCondNumber + " : DESCRIPTION : SET THE INITIAL COMMON PARAMETER VALUES");
		LOGGER.info("POST-CONDITION " + postCondNumber + " : ACTION : EXECUTE WEBPA COMMAND :\n "
				+ "	1. Set the value for DMZ LAN IP Address \n" + "	2. Set the value for Device cloud UI status \n"
				+ "	3. Set the value for device control router enable status \n"
				+ "	4. Set the value for port forwarding status");
		LOGGER.info("PRE-CONDITION " + postCondNumber + " : EXPECTED : MUST SET THE INITIAL COMMON PARAMETER VALUES.");
		LOGGER.info("#######################################################################################");
		errorMessage = "FAILED TO SET THE INITIAL COMMON PARAMETER VALUES.";
		BroadBandWebPaUtils.setTheCommonWebParams(device, tapEnv, initialMapValues, true);
		commonParamValues = BroadBandWebPaUtils.executeAndGetListOfWebParameters(device, tapEnv,
				BroadBandWebPaConstants.WEBPA_PARAMS_FOR_COMMON_RESI_DEVICE);
		errorMessage = "Unable to cross verify the common parameter values with initial common parameter values.";
		status = BroadBandWebPaUtils.verifyWebpaGetResponseValues(device, tapEnv, initialMapValues, commonParamValues);
		if (status) {
			LOGGER.info("POST-CONDITION " + postCondNumber
					+ " : ACTUAL : SUCESSFULLY SET THE INITIAL COMMON PARAMETER VALUES");
		} else {
			LOGGER.error("POST" + postCondNumber + " : ACTUAL : " + errorMessage);
		}
	}

	/**
	 * Post-Condition method to set the initial values for 2.4 & 5 GHZ radio's
	 * 
	 * @param tapEnv           {@link AutomaticsTapApi}
	 * @param device           {@link Dut}
	 * @param postCondNumber   Pre condition number
	 * @param initialMapValues Instance of {Map}
	 */
	public static void executePostConditionToSetTheDefaultValuesForBothRadios(Dut device, AutomaticsTapApi tapEnv,
			int postCondNumber, Map<String, String> initialMapValues) throws TestException {
		String errorMessage = null;
		boolean status = false;
		Map<String, String> wifiRadioValues = null;
		/**
		 * POST-CONDITION : SET THE INITIAL VALUES FOR 2.4 & 5 GHZ RADIO'S
		 */
		LOGGER.info("#######################################################################################");
		LOGGER.info(
				"POST-CONDITION " + postCondNumber + " : DESCRIPTION : SET VALUE FOR 2.4 & 5 GHZ RADIOS FROM DEVICE");
		LOGGER.info("POST-CONDITION " + postCondNumber + " : ACTION : EXECUTE WEBPA COMMAND :\n "
				+ "1. Set the value for 2.4 GHz radio extension channel \n"
				+ "2. Set the value for 2.4 GHz radio beacon interval \n"
				+ "3. Set the value for 2.4 GHz radio basic rate \n"
				+ "4. Set the value for 2.4 GHz radio operating standard \n"
				+ "5. Set the value for 2.4 GHz radio transmit power \n"
				+ "6. Set the value for 2.4 GHz radio status \n" + "7. Set the value for 2.4 GHz radio channel \n"
				+ "8. Set the value for 2.4 GHz radio wireless channel \n"
				+ "9. Set the value for 2.4 GHz radio operating channel bandwidth \n"
				+ "10. Set the value for 2.4 GHz radio dfs enable \n"
				+ "11. Set the value for 5 GHz radio extension channel \n"
				+ "12. Set the value for 5 GHz radio beacon interval \n"
				+ "13. Set the value for 5 GHz radio basic rate \n"
				+ "14. Set the value for 5 GHz radio operating standard \n"
				+ "15. Set the value for 5 GHz radio transmit power \n" + "16. Set the value for 5 GHz radio status \n"
				+ "17. Set the value for 5 GHz radio channel \n"
				+ "18. Set the value for 5 GHz radio wireless channel \n"
				+ "19. Set the value for 5 GHz radio operating channel bandwidth \n"
				+ "20. Set the value for 5 GHz radio dfs enable ");
		LOGGER.info("POST-CONDITION " + postCondNumber
				+ " : EXPECTED : MUST SET THE INITIAL VALUES FOR 2.4 & 5 GHZ RADIOS.");
		LOGGER.info("#######################################################################################");
		errorMessage = "FAILED TO SET THE INITIAL VALUES FOR 2.4 & 5 GHZ RADIOS";
		BroadBandWebPaUtils.setTheWebParamForWifiRadios(device, tapEnv, initialMapValues);
		wifiRadioValues = BroadBandWebPaUtils.executeAndGetListOfWebParameters(device, tapEnv,
				BroadBandWebPaConstants.WEBPA_PARAMS_FOR_BOTH_RADIOS);
		errorMessage = "Unable to cross verify the 2.4 & 5 GHz Radios values with 2.4 & 5 GHz Radios values.";
		status = BroadBandWebPaUtils.verifyWebpaGetResponseValues(device, tapEnv, initialMapValues, wifiRadioValues);
		if (status) {
			LOGGER.info(
					"POST-CONDITION " + postCondNumber + " : ACTUAL : SUCESSFULLY SET THE 2.4 & 5 GHZ RADIOS VALUES");
		} else {
			LOGGER.error("POST-CONDITION " + postCondNumber + " : ACTUAL : " + errorMessage);
		}
	}

	/**
	 * Post-Condition method to disable the bridge mode
	 * 
	 * @param tapEnv              {@link AutomaticsTapApi}
	 * @param device              {@link Dut}
	 * @param postConditionNumber String to hold post condition number.
	 * @refactor Govardhan
	 */
	public static void executePostConditionToDisableBirdgeMode(Dut device, AutomaticsTapApi tapEnv,
			int postConStepNumber) {
		boolean status = false;
		String errorMessage = null;
		try {
			LOGGER.info("#######################################################################################");
			LOGGER.info("POST-CONDITION " + postConStepNumber
					+ " : DESCRIPTION : SET AND VERIFY THE BRIDGE MODE STATUS AS DISABLED");
			LOGGER.info("POST-CONDITION " + postConStepNumber + " : ACTION : EXECUTE WEBPA COMMAND:"
					+ BroadBandWebPaConstants.WEBPA_PARAM_BRIDGE_MODE_ENABLE);
			LOGGER.info("POST-CONDITION " + postConStepNumber + " : EXPECTED : BRIDGE MODE MUST BE DISABLED");
			LOGGER.info("#######################################################################################");
			errorMessage = "UNABLE TO DISABLE THE BRIDGE MODE ON GATEWAY DEVICE";
			status = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_BRIDGE_MODE_ENABLE, BroadBandTestConstants.CONSTANT_0,
					BroadBandTestConstants.LAN_MANAGEMENT_MODE_ROUTER);
			if (status) {
				status = false;
				LOGGER.info("Mode changed to " + BroadBandTestConstants.LAN_MANAGEMENT_MODE_ROUTER);
				LOGGER.info("Waiting for three minutes");
				tapEnv.waitTill(BroadBandTestConstants.THREE_MINUTE_IN_MILLIS);
				status = BroadBandWebPaUtils.verifyWebPaProcessIsUp(tapEnv, device, true);
			}
			if (status) {
				LOGGER.info("POST-CONDITION " + postConStepNumber
						+ " : ACTUAL : SUCCESSFULLY DISABLED THE BRIDGE MODE ON GATEWAY DEVICE");
			} else {
				LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL : " + errorMessage);
			}
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL : " + e.getMessage());
		}
	}

	/**
	 * Post-Condition method to disconnect the connected clients
	 * 
	 * @param device                  {@link Dut}
	 * @param deviceConnectedWith5Ghz Instance for device connected with 5 Ghz SSID
	 * @refactor Alan_Bivera
	 * 
	 */
	public static void executePostConditionToDisconnectClientsConnectedWith5Ghz(Dut device, AutomaticsTapApi tapEnv,
			Dut deviceConnectedWith5Ghz, int postConStepNumber) throws TestException {
		String errorMessage = null;
		boolean status = false;
		try {
			/**
			 * POST-CONDITION :DISCONNECTING THE WI-FI CLIENT CONNECTED WITH 5GHz SSID USING
			 * WEBPA
			 */
			status = false;
			errorMessage = null;
			LOGGER.info("#######################################################################################");
			LOGGER.info("POST-CONDITION " + postConStepNumber
					+ " : DESCRIPTION : DISCONNECTING THE WI-FI CLIENT CONNECTED WITH 5GHz SSID ");
			LOGGER.info("POST-CONDITION " + postConStepNumber
					+ " : ACTION : EXECUTE COMMAND TO DISCONNECT THE CONNECTED CLIENT, FOR WINDOWS :netsh wlan disconnect ,FOR LINUX :nmcli con down id '<ssid>' ");
			LOGGER.info("POST-CONDITION " + postConStepNumber
					+ " : EXPTECTED : CONNECTED CLIENT DISCONNECTED SUCCESSFULLY FOR 5 GHZ SSID");
			LOGGER.info("#######################################################################################");
			if (deviceConnectedWith5Ghz != null) {
				errorMessage = "UNABLE TO DISCONNECT CONNECTED CLIENT FOR 5 GHZ SSID. ";
				try {
					status = ConnectedNattedClientsUtils.disconnectSSID(deviceConnectedWith5Ghz, tapEnv,
							BroadBandConnectedClientUtils.getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv,
									WiFiFrequencyBand.WIFI_BAND_5_GHZ));
				} catch (Exception exception) {
					errorMessage = errorMessage + exception.getMessage();
					LOGGER.error(errorMessage);
				}
				if (status) {
					LOGGER.info("POST-CONDITION " + postConStepNumber
							+ " : ACTUAL : CONNECTED CLIENT DISCONNECTED SUCCESSFULLY FOR 5 GHZ SSID.");
				} else {
					LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL :" + errorMessage);
				}
				LOGGER.info("#######################################################################################");
			} else {
				LOGGER.info("POST-CONDITION " + postConStepNumber
						+ " : ACTUAL : ALREADY CONNECTED CLIENT DISCONNECTED SUCCESSFULLY FOR 5 GHZ SSID.");
			}
		} catch (Exception exception) {
			LOGGER.error(
					"Execution error occurred while executing to disconnect client post conditions due to exception --> "
							+ exception.getMessage());
		}
	}

	/**
	 * Post-Condition method to disconnect the connected clients for 2.4 GHz
	 * 
	 * @param device                  {@link Dut}
	 * @param deviceConnectedWith2Ghz Instance for device connected with 2.4 Ghz
	 *                                SSID
	 * @refactor Alan_Bivera
	 */
	public static void executePostConditionToDisconnectClientsConnectedWith2Ghz(Dut device, AutomaticsTapApi tapEnv,
			Dut deviceConnectedWith2Ghz, int postConStepNumber) throws TestException {
		String errorMessage = null;
		boolean status = false;
		try {
			/**
			 * POST-CONDITION :DISCONNECTING THE WI-FI CLIENT CONNECTED WITH 5GHz SSID USING
			 * WEBPA
			 */
			status = false;
			errorMessage = null;
			LOGGER.info("#######################################################################################");
			LOGGER.info("POST-CONDITION " + postConStepNumber
					+ " : DESCRIPTION : DISCONNECTING THE WI-FI CLIENT CONNECTED WITH 2GHz SSID ");
			LOGGER.info("POST-CONDITION " + postConStepNumber
					+ " : ACTION : EXECUTE COMMAND TO DISCONNECT THE CONNECTED CLIENT, FOR WINDOWS :netsh wlan disconnect ,FOR LINUX :nmcli con down id '<ssid>' ");
			LOGGER.info("POST-CONDITION " + postConStepNumber
					+ " : EXPTECTED : CONNECTED CLIENT DISCONNECTED SUCCESSFULLY FOR 2 GHZ SSID");
			LOGGER.info("#######################################################################################");
			if (deviceConnectedWith2Ghz != null) {
				errorMessage = "UNABLE TO DISCONNECT CONNECTED CLIENT FOR 2 GHZ SSID. ";
				try {
					status = ConnectedNattedClientsUtils.disconnectSSID(deviceConnectedWith2Ghz, tapEnv,
							BroadBandConnectedClientUtils.getSsidNameFromGatewayUsingWebPaOrDmcli(device, tapEnv,
									WiFiFrequencyBand.WIFI_BAND_2_GHZ));
				} catch (Exception exception) {
					errorMessage = errorMessage + exception.getMessage();
					LOGGER.error(errorMessage);
				}
				if (status) {
					LOGGER.info("POST-CONDITION " + postConStepNumber
							+ " : ACTUAL : CONNECTED CLIENT DISCONNECTED SUCCESSFULLY FOR 2 GHZ SSID.");
				} else {
					LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL :" + errorMessage);
				}
				LOGGER.info("#######################################################################################");
			} else {
				LOGGER.info("POST-CONDITION " + postConStepNumber
						+ " : ACTUAL : ALREADY CONNECTED CLIENT DISCONNECTED SUCCESSFULLY FOR 2 GHZ SSID.");
			}
		} catch (Exception exception) {
			LOGGER.error(
					"Execution error occurred while executing to disconnect client post conditions due to exception --> "
							+ exception.getMessage());
		}
	}

	/**
	 * Common step for executing post-conditions. ie., to disable the XDNS feature
	 * 
	 * @param device
	 * @param testCaseId
	 * @Refactor Sruthi Santhosh
	 */
	public static void executePostConditionForXdns(Dut device, AutomaticsTapApi tapEnv, boolean reactiveRouter)
			throws TestException {
		try {
			// boolean variable to store the status
			boolean status = false;
			// Error message
			String errorMessage = null;
			LOGGER.info("################### STARTING POST-CONFIGURATIONS ###################");
			LOGGER.info("#######################################################################################");
			LOGGER.info(
					"POST-CONDITION 1 : DESCRIPTION : Disable and verify the XDNS feature using webpa param 'Device.DeviceInfo.X_RDKCENTRAL-COM_EnableXDNS'");
			LOGGER.info(
					"POST-CONDITION 1 : ACTION : Disable XDNS feature using webpa param Device.DeviceInfo.X_RDKCENTRAL-COM_EnableXDNS");
			LOGGER.info(
					"POST-CONDITION 1 : EXPECTED : XDNS feature should be disabled using webpa param 'Device.DeviceInfo.X_RDKCENTRAL-COM_EnableXDNS'");
			LOGGER.info("#######################################################################################");
			String response = tapEnv.executeWebPaCommand(device,
					BroadBandWebPaConstants.WEBPA_PARAM_TO_GET_XDNS_FEATURE_STATUS);
			if (CommonMethods.isNotNull(response)
					&& CommonUtils.patternSearchFromTargetString(response, AutomaticsConstants.TRUE)) {
				// Error message
				errorMessage = "Failed to disable the XDNS feature using webpa param 'Device.DeviceInfo.X_RDKCENTRAL-COM_EnableXDNS'";
				status = BroadBandWebPaUtils.setVerifyWebPAInPolledDuration(device, tapEnv,
						BroadBandWebPaConstants.WEBPA_PARAM_TO_GET_XDNS_FEATURE_STATUS, AutomaticsConstants.CONSTANT_3,
						AutomaticsConstants.FALSE, AutomaticsConstants.THREE_MINUTES,
						BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
				LOGGER.info("POST-CONDITION 1  - ACTUAL: " + (status
						? "XDNS feature disabled sucessfully using webpa param 'Device.DeviceInfo.X_RDKCENTRAL-COM_EnableXDNS'"
						: errorMessage));
			} else {
				LOGGER.info("POST-CONDITION 1  - ACTUAL: XDNS override feature is already in disabled state.");
			}
			if (reactiveRouter) {
				LOGGER.info("#######################################################################################");
				LOGGER.info("POST-CONDITION 2 : DESCRIPTION : Reactivate the router device after factory reset");
				LOGGER.info("POST-CONDITION 2 : ACTION : Set values to 2.4GHz and 5GHz - Private SSID and Password");
				LOGGER.info("POST-CONDITION 2 : EXPECTED : The device should be Reactivated successfully");
				LOGGER.info("#######################################################################################");
				errorMessage = "Failed to reactivate device after factory reset";
				status = false;
				try {
					BroadBandWiFiUtils.reactivateDeviceUsingWebpaOrSnmp(tapEnv, device);
					status = true;
				} catch (TestException e) {
					errorMessage = e.getMessage();
				}
				LOGGER.info("POST-CONDITION 2 - ACTUAL: "
						+ (status ? "The device Reactivated successfully." : errorMessage));
			}
		} catch (Exception exception) {
			LOGGER.error("Execution error occurred while executing post conditions due to exception --> "
					+ exception.getMessage());
		}
	}
	
	/**
     * Post-Condition method to used to disable the TR69 Configuration
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link ECatsTapApi}
     * @param postConStepNumber
     *            Post condition number
     * 
     * @refactor yamini.s
     */
    public static void postConditionToDisableTR069Configuration(Dut device, AutomaticsTapApi tapEnv,
	    int postConStepNumber) {
	String errorMessage = null;
	boolean status = false;
	/**
	 * POST-CONDITION : Disable the TR69 configuration
	 */
	LOGGER.info("#######################################################################################");
	LOGGER.info("POST-CONDITION " + postConStepNumber + " : DESCRIPTION : Disable the TR69 configuration");
	LOGGER.info("POST-CONDITION " + postConStepNumber + " : ACTION : Set "
		+ BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_ENABLECWMP + " as FALSE.");
	LOGGER.info("POST-CONDITION " + postConStepNumber
		+ " : EXPTECTED : Disable the TR69 configuration configuration should be success");
	LOGGER.info("#######################################################################################");
	errorMessage = "Failed to Disable the TR69 configuration";
	try {
	    status = BroadBandCommonUtils.getWebPaValueAndVerify(device, tapEnv,
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_ENABLECWMP,
		    BroadBandTestConstants.FALSE);
	    LOGGER.info("GET DEVICE_MANAGEMENTSERVER_ENABLECWMP :" + status);
	} catch (Exception exception) {
	    LOGGER.error(errorMessage + " : " + exception.getMessage());
	}
	try {
	    if (!status) {
		LOGGER.info("TRYING TO SET DEVICE_MANAGEMENTSERVER_ENABLECWMP :" + status);
		status = BroadBandWebPaUtils.setAndGetParameterValuesUsingWebPa(device, tapEnv,
			BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_ENABLECWMP,
			WebPaDataTypes.BOOLEAN.getValue(), BroadBandTestConstants.FALSE);
	    }
	} catch (Exception exception) {
	    LOGGER.error(errorMessage + " : " + exception.getMessage());
	}
	if (status) {
	    LOGGER.info("POST-CONDITION " + postConStepNumber
		    + " : ACTUAL : TR69 configuration configuration disabled successfully.");
	} else {
	    LOGGER.error("POST-CONDITION " + postConStepNumber + " : ACTUAL : " + errorMessage);
	}
    }


}
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
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.wifi.BroadBandWiFiUtils;

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
		tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_SYSTEM_MESH_STATUS);
		tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_SYSTEM_STOP_MESH_AGENT);
		tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_SYSTEM_STOP_MESH_SERVICE);
	    isMeshDisabled = BroadBandWebPaUtils.getAndVerifyWebpaValueInPolledDuration(device, tapEnv,
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_MESH_ENABLE, meshInitialStatus,
		    BroadBandTestConstants.ONE_MINUTE_IN_MILLIS, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
	} catch (Exception e) {
	    LOGGER.error("Unable to Stop mesh service due to " + e.getMessage());
	}
	if (!isMeshDisabled) {
	    LOGGER.info("PRE-CONDITION " + preConStepNumber + " : ACTUAL :  WEBPA RESPONSE FOR "+BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_MESH_ENABLE+"IS NULL. SUCCESSFULLY STOPPED THE MESH STATUS");
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
    public static void executePreConditionToDisableCodeBigFirst(Dut device, AutomaticsTapApi tapEnv, int preConStepNumber)
	    throws TestException {
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
}

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
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;

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
}

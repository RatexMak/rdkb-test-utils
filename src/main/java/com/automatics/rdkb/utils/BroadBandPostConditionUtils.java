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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Dut;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.utils.cdl.BroadBandCodeDownloadUtils;
import com.automatics.rdkb.utils.cdl.BroadBandXconfCdlUtils;
import com.automatics.rdkb.utils.wifi.BroadBandWiFiUtils;
import com.automatics.tap.AutomaticsTapApi;

/**
 * Utility class which handles the Post condition related functionality and verification.
 * 
 * @author Muthukumar
 */
public class BroadBandPostConditionUtils {

    /** SLF4J logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandPostConditionUtils.class);

    /**
     * Post-Condition method to reactivate the device
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param isReactivated
     *            It is 'True' Device already activated.'False' Device Not reactivated
     * @refactor Govardhan
     */
    public static void executePostConditionToReActivateDevice(Dut device, AutomaticsTapApi tapEnv, boolean isReactivated,
    		int postConStepNumber) throws TestException {
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
     * Post-Condition method to trigger Original build on the device. Existing build on the device before starting the
     * test
     * 
     * @param settop
     *            {@link Settop}
     * @param hasLatestBuildChanged
     *            It is 'True' - Latest build flashed.'False' - Latest build didn't flashed
     * 
     * @param hasOriginalBuildChanged
     *            It is 'True'- Orignal build flashed.'False' It is 'True' - Orignal build didn't flashed .
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
		LOGGER.info("#######################################################################################");
		LOGGER.info("POST-CONDITION " + postConStepNumber
			+ " : DESCRIPTION : PERFORM FLASHING THE ORIGINAL BUILD ON THE DEVICE: "
			+ initialFirmwareVersion);
		LOGGER.info("POST-CONDITION " + postConStepNumber
			+ " : ACTION : FLASH THE ORIGINAL BUILD ON THE DEVICE USING HTTP/ TR-181. ");
		LOGGER.info("POST-CONDITION " + postConStepNumber
			+ " : EXPTECTED : ORIGINAL IMAGE MUST BE FLASHED ON THE DEVICE");
		LOGGER.info("#######################################################################################");
		errorMessage = "UNABLE TO FLASH THE ORIGINAL BUILD ON THE DEVICE. ";
		status = BroadBandCodeDownloadUtils.triggerPreviousCodeDownload(device, tapEnv, initialFirmwareVersion);
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
     * Post-Condition method to clear the CDL information URL in /nvram/swupdate.conf
     * 
     * @param settop
     *            instance of{@link Settop}
     * @param tapEnv
     *            instance of {@link ECatsTapApi}
     * @param postConditionNumber
     *            String to hold post condition number.
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
}
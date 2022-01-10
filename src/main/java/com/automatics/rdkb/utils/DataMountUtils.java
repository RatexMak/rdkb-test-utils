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
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;

public class DataMountUtils {
    /** SLF4J logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DataMountUtils.class);

    /**
     * Utility method that executes the created shell script using 'sh ' command.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param dataMount
     *            String representing the mount; possible values like /nvram, /nvram2
     * @param isProdBuild
     *            Boolean representing whether the device runs on PROD build.
     * 
     * @return Boolean representing result of Shell Script execution; true if the script cannot be executed; false if it
     *         can be executed.
     * @refactor Govardhan
     */
    public static boolean verifyScriptExecution(AutomaticsTapApi tapEnv, Dut device, String dataMount,
	    boolean isProdBuild) {
	LOGGER.debug("ENTERING METHOD verifyScriptExecution");
	String command = dataMount + BroadBandTestConstants.DOT_OPERATOR + BroadBandTestConstants.SLASH_SYMBOL
		+ BroadBandCommandConstants.FILE_TEST_SHELL_SCRIPT;
	LOGGER.info("COMMAND TO BE EXECUTED: " + command);
	String response = tapEnv.executeCommandUsingSsh(device, command);
	boolean result = CommonMethods.isNotNull(response)
		&& response.contains(BroadBandTestConstants.ERROR_PERMISSION_DENIED)
		&& !response.contains(BroadBandTestConstants.TEXT_HELLO_WORLD);
	LOGGER.info("SHELL SCRIPT EXECUTION FAILED: " + result);
	LOGGER.debug("ENDING METHOD verifyScriptExecution");
	return result;
    }

    /**
     * Utility method that creates a shell in the data only partition.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param dataMount
     *            String representing the mount; possible values like /nvram, /nvram2
     * 
     * @return Boolean representing result of Shell Script creation.
     * @refactor Govardhan
     */
    public static boolean createShellScript(AutomaticsTapApi tapEnv, Dut device, String dataMount) {
	LOGGER.debug("ENTERING METHOD createShellScript");
	String command = BroadBandCommandConstants.CMD_ECHO_E + BroadBandTestConstants.FILE_TEST_SHELL_SCRIPT_CONTENT
		+ BroadBandTestConstants.REDIRECT_OPERATOR + dataMount
		+ BroadBandCommandConstants.FILE_TEST_SHELL_SCRIPT;
	LOGGER.info("COMMAND TO BE EXECUTED: " + command);
	String response = tapEnv.executeCommandUsingSsh(device, command);
	boolean result = CommonMethods.isNull(response) && CommonUtils.isFileExists(device, tapEnv,
		dataMount + BroadBandCommandConstants.FILE_TEST_SHELL_SCRIPT);
	LOGGER.info("SHELL SCRIPT CREATED FOR EXECUTION: " + result);
	LOGGER.debug("ENDING METHOD createShellScript");
	return result;
    }

    /**
     * Utility method that executes the created shell script using the dot operation after granting rwx permission to
     * all users.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param dataMount
     *            String representing the mount; possible values like /nvram, /nvram2
     * @param isProdBuild
     *            Boolean representing whether the device runs on PROD build.
     * 
     * @return Boolean representing result of Shell Script execution; true if the script cannot be executed on PROD
     *         build; false if it can be executed on PROD build.
     * @refactor Govardhan
     */
    public static boolean verifyScriptExecutionAfterGrantingPermission(AutomaticsTapApi tapEnv, Dut device,
	    String dataMount, boolean isProdBuild) {
	LOGGER.debug("ENTERING METHOD verifyScriptExecutionAfterGrantingPermission");
	// Grant 777 Permission
	String command = BroadBandCommandConstants.CMD_CHMOD + BroadBandCommandConstants.CHMOD_777_VALUE + dataMount
		+ BroadBandCommandConstants.FILE_TEST_SHELL_SCRIPT;
	LOGGER.info("COMMAND TO BE EXECUTED: " + command);
	String response = tapEnv.executeCommandUsingSsh(device, command);
	boolean result = CommonMethods.isNull(response);
	if (result) {
	    command = dataMount + BroadBandTestConstants.DOT_OPERATOR + BroadBandTestConstants.SLASH_SYMBOL
		    + BroadBandCommandConstants.FILE_TEST_SHELL_SCRIPT;
	    LOGGER.info("COMMAND TO BE EXECUTED: " + command);
	    response = tapEnv.executeCommandUsingSsh(device, command);
	    result = CommonMethods.isNotNull(response)
		    && response.contains(BroadBandTestConstants.ERROR_PERMISSION_DENIED)
		    && !response.contains(BroadBandTestConstants.TEXT_HELLO_WORLD);
	    LOGGER.info("SHELL SCRIPT EXECUTION FAILED: " + result);
	    // In case of PROD Build, the partition is Non-Executable; else, it is not.
	    // Hence re-validation shell script execution response;
	    result = isProdBuild ? result
		    : CommonMethods.isNotNull(response) && response.contains(BroadBandTestConstants.TEXT_HELLO_WORLD);
	}
	LOGGER.info("SHELL SCRIPT EXECUTION FAILED (FINAL) : " + result);
	LOGGER.debug("ENDING METHOD verifyScriptExecutionAfterGrantingPermission");
	return result;
    }
}

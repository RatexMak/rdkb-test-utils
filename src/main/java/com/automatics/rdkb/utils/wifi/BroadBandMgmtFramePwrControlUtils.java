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
package com.automatics.rdkb.utils.wifi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Dut;
import com.automatics.enums.TR69ParamDataType;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.tr69.BroadBandTr69Utils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.test.AutomaticsTestBase;
import com.automatics.utils.CommonMethods;
import com.automatics.webpa.WebPaParameter;
import com.automatics.rdkb.enums.BroadBandManagementPowerControlEnum;

/**
 * Utility class which handles the RDK B WiFi Access Point Management Frame Power Levels.
 * 
 * @author BALAJI V, INFOSYS
 * @refactor Alan_Bivera
 * 
 */
public class BroadBandMgmtFramePwrControlUtils extends AutomaticsTestBase {

    /**
     * Logger instance for {@link BroadBandMgmtFramePwrControlUtils}
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandMgmtFramePwrControlUtils.class);

    /**
     * Utility Method to verify the power level is within the range.
     * 
     * @param actualValue
     *            String representing the actual value.
     * 
     * @return Boolean representing the result of the operation.
     */
    public static boolean verifyPowerLevelIsWithinRange(String actualValue) {
	LOGGER.debug("ENTERING METHOD: verifyPowerLevelIsWithinRange");
	boolean result = false;
	try {
	    int iActualValue = Integer.parseInt(actualValue.trim());
	    result = iActualValue >= -20 && iActualValue <= 0;
	} catch (NumberFormatException numberFormatException) {
	    // Suppress the exception
	    LOGGER.error(numberFormatException.getMessage());
	}
	LOGGER.info("POWER LEVEL IS WITHIN RANGE 0 - -20: " + result);
	LOGGER.debug("ENDING METHOD: verifyPowerLevelIsWithinRange");
	return result;
    }

    /**
     * Utility Method to retrieve the WiFi Access Points based on the radio: 2.4GHz or 5GHz.
     * 
     * @param wifiRadio5Ghz
     *            Boolean representing the radio: 2.4GHz or 5GHz
     * @return List of BroadBandManagementPowerControlEnum.
     */
    public static List<BroadBandManagementPowerControlEnum> getWifiAccessPointsBasedOnRadio(boolean wifiRadio5Ghz) {
	LOGGER.debug("ENTERING METHOD: getWifiAccessPoints");
	List<BroadBandManagementPowerControlEnum> wifiAccessPointsList = new ArrayList<BroadBandManagementPowerControlEnum>();
	for (BroadBandManagementPowerControlEnum wifiAccessPoint : BroadBandManagementPowerControlEnum.values()) {
	    if (wifiRadio5Ghz == wifiAccessPoint.isWifiRadio5Ghz()) {
		wifiAccessPointsList.add(wifiAccessPoint);
	    }
	}
	LOGGER.info("BROADBAND WIFI ACCESS POINTS TO BE VALIDATED: " + wifiAccessPointsList.size());
	LOGGER.debug("ENDING METHOD: getWifiAccessPoints");
	return wifiAccessPointsList;
    }

    /**
     * Retrieve the value to be set from the Broadband Management Frame Power Control Enum based on the WebPA Parameter.
     * 
     * @param webPaParameter
     *            String representing the WebPA Parameter.
     * @return String representing the Value To Be Set.
     */
    public static String getValueToBeSet(String webPaParameter) {
	LOGGER.debug("ENTERING METHOD: getParameterBasedOnWebPa");
	String valueToBeSet = null;
	BroadBandManagementPowerControlEnum[] mgmtPowerControls = BroadBandManagementPowerControlEnum.values();
	for (int iCounter = 0; iCounter < mgmtPowerControls.length; iCounter++) {
	    if (mgmtPowerControls[iCounter].getWebPaParamMgmtPower().equalsIgnoreCase(webPaParameter)) {
		valueToBeSet = String.valueOf(mgmtPowerControls[iCounter].getValueToBeSet());
		break;
	    }
	}
	LOGGER.info("VALUE TO BE SET FOR PARAMETER: " + webPaParameter + " IS = " + valueToBeSet);
	LOGGER.debug("ENDING METHOD: getParameterBasedOnWebPa");
	return valueToBeSet;
    }

    /**
     * Utility Method to set the Management Frame Power Level for the given WiFi Access Points using WebPA.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Settop}
     * @param wifiAccessPoints
     *            List of BroadBandManagementPowerControlEnum
     * @param setDefaultValue
     *            Boolean Flag representing either default value to be set or not; TRUE in case default value to be set.
     * 
     * @return {@link BroadBandResultObject}
     */
    public static BroadBandResultObject setMgmtFramePowerLevelsWebPa(AutomaticsTapApi tapEnv, Dut device,
	    List<BroadBandManagementPowerControlEnum> wifiAccessPoints, boolean setDefaultValue) {
	LOGGER.debug("ENTERING METHOD: setMgmtFramePowerLevelsWebPa");
	BroadBandResultObject resultObj = new BroadBandResultObject();
	boolean result = false;
	String errorMessage = BroadBandTestConstants.EMPTY_STRING;
	// Create WebPA Parameter list for all parameters for which power level needs to be set.
	List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
	WebPaParameter webPaParam = null;
	int valueToBeSet = 0;
	for (BroadBandManagementPowerControlEnum wifiAccessPoint : wifiAccessPoints) {
	    webPaParam = new WebPaParameter();
	    webPaParam.setName(wifiAccessPoint.getWebPaParamMgmtPower());
	    webPaParam.setDataType(BroadBandTestConstants.CONSTANT_1);
	    valueToBeSet = setDefaultValue ? wifiAccessPoint.getDefaultValue() : wifiAccessPoint.getValueToBeSet();
	    webPaParam.setValue(String.valueOf(valueToBeSet));
	    webPaParameters.add(webPaParam);
	}
	// Execute Multiple WebPA Commands.
	Map<String, String> managementFramePowerControlWebPaParameters = tapEnv.executeMultipleWebPaSetCommands(device,
		webPaParameters);
	// Parse the WebPA Command Execution Result.
	if (null != managementFramePowerControlWebPaParameters
		&& !managementFramePowerControlWebPaParameters.isEmpty()) {
	    for (String parameterName : managementFramePowerControlWebPaParameters.keySet()) {
		if (managementFramePowerControlWebPaParameters.get(parameterName)
			.equalsIgnoreCase(BroadBandTestConstants.SNMP_RESPONSE_SUCCESS)) {
		    LOGGER.info("SUCCESSFULLY SET THE VALUE FOR PARAMETER: " + parameterName);
		} else {
		    errorMessage = BroadBandCommonUtils.concatStringUsingStringBuffer(errorMessage,
			    BroadBandTestConstants.SINGLE_SPACE_CHARACTER, parameterName);
		}
	    }
	    if (CommonMethods.isNull(errorMessage)) {
		result = true;
	    } else {
		errorMessage = "UNABLE TO SET THE VALUE FOR PARAMETERS: " + errorMessage;
	    }
	}
	resultObj.setStatus(result);
	resultObj.setErrorMessage(errorMessage);
	LOGGER.info("SET MANAGEMENT FRAMES POWER LEVEL: " + result);
	LOGGER.debug("ENDING METHOD: setMgmtFramePowerLevelsWebPa");
	return resultObj;
    }

    /**
     * Utility Method to set the Management Frame Power Level for the given WiFi Access Points using TR-69.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Settop}
     * @param wifiAccessPoints
     *            List of BroadBandManagementPowerControlEnum
     * 
     * @return {@link BroadBandResultObject}
     */
    public static BroadBandResultObject setMgmtFramePowerLevelsTr69(AutomaticsTapApi tapEnv, Dut device,
	    List<BroadBandManagementPowerControlEnum> wifiAccessPoints) {
	LOGGER.debug("ENTERING METHOD: setMgmtFramePowerLevelsTr69");
	BroadBandResultObject resultObj = new BroadBandResultObject();
	boolean result = false;
	String errorMessage = BroadBandTestConstants.EMPTY_STRING;
	for (BroadBandManagementPowerControlEnum wifiAccessPoint : wifiAccessPoints) {
	    try {
		result = BroadBandTr69Utils.setTR69ParameterValueUsingRestApi(tapEnv, device, wifiAccessPoint.getWebPaParamMgmtPower(),
			TR69ParamDataType.INT, String.valueOf(wifiAccessPoint.getValueToBeSet()));
		if (result) {
		    LOGGER.info(
			    "SUCCESSFULLY SET THE VALUE FOR PARAMETER: " + wifiAccessPoint.getWebPaParamMgmtPower());
		} else {
		    errorMessage = BroadBandCommonUtils.concatStringUsingStringBuffer(errorMessage,
			    BroadBandTestConstants.SINGLE_SPACE_CHARACTER, wifiAccessPoint.getWebPaParamMgmtPower());
		}
	    } catch (Exception exception) {
		// Log & Suppress the Exception.
		errorMessage = BroadBandCommonUtils.concatStringUsingStringBuffer(errorMessage,
			BroadBandTestConstants.SINGLE_SPACE_CHARACTER, wifiAccessPoint.getWebPaParamMgmtPower(),
			BroadBandTestConstants.COLON_AND_SPACE, exception.getMessage());
		LOGGER.error("EXCEPTION OCCURRED WHILE SETTING THE TR-69 PARAMETER: " + exception.getMessage());
	    }
	}
	if (CommonMethods.isNull(errorMessage)) {
	    result = true;
	} else {
	    errorMessage = "UNABLE TO SET THE VALUE FOR PARAMETERS: " + errorMessage;
	}
	resultObj.setStatus(result);
	resultObj.setErrorMessage(errorMessage);
	LOGGER.info("SET MANAGEMENT FRAMES POWER LEVEL USING TR-69: " + result);
	LOGGER.debug("ENDING METHOD: setMgmtFramePowerLevelsTr69");
	return resultObj;
    }

    /**
     * Utility Method to set the Management Frame Power Level for the given WiFi Access Points that were set earlier.
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Settop}
     * @param wifiAccessPoints
     *            List of BroadBandManagementPowerControlEnum
     * @param checkDefaultValue
     *            Boolean representing the values to be validated; TRUE for default values; FALSE for values set.
     * 
     * @return {@link BroadBandResultObject}
     */
    public static BroadBandResultObject verifyMgmtPowerLevels(AutomaticsTapApi tapEnv, Dut device,
	    List<BroadBandManagementPowerControlEnum> wifiAccessPoints, boolean checkDefaultValue) {
	LOGGER.debug("ENTERING METHOD: verifyMgmtPowerLevels");
	BroadBandResultObject resultObj = new BroadBandResultObject();
	boolean result = false;
	String actualValue = null;
	String expectedValue = null;
	String errorMessage = BroadBandTestConstants.EMPTY_STRING;
	// Create WebPA Parameter list for all parameters for which power level needs to be set.
	List<String> webPaParameters = new ArrayList<String>();
	for (BroadBandManagementPowerControlEnum wifiAccessPoint : wifiAccessPoints) {
	    webPaParameters.add(wifiAccessPoint.getWebPaParamMgmtPower());
	}
	// Execute Multiple WebPA Commands.
	Map<String, String> managementFramePowerControlWebPaParameters = tapEnv.executeMultipleWebPaGetCommands(device,
		webPaParameters.toArray(new String[webPaParameters.size()]));

	// Parse the WebPA Command Execution Result.
	if (null != managementFramePowerControlWebPaParameters
		&& !managementFramePowerControlWebPaParameters.isEmpty()) {
	    // Loop through the WebPA response map.
	    for (String parameterName : managementFramePowerControlWebPaParameters.keySet()) {
		actualValue = managementFramePowerControlWebPaParameters.get(parameterName);
		expectedValue = checkDefaultValue ? BroadBandTestConstants.STRING_ZERO : getValueToBeSet(parameterName);
		if (actualValue.equalsIgnoreCase(expectedValue)) {
		    LOGGER.info("SUCCESSFULLY VERIFIED THE VALUE OF PARAMETER: " + parameterName);
		} else {
		    errorMessage = BroadBandCommonUtils.concatStringUsingStringBuffer(errorMessage,
			    BroadBandTestConstants.SINGLE_SPACE_CHARACTER, parameterName, ": EXPECTED VALUE = ",
			    expectedValue, ", ACTUAL VALUE = ", actualValue);
		}
	    }
	    result = CommonMethods.isNull(errorMessage);
	}
	resultObj.setStatus(result);
	resultObj.setErrorMessage(errorMessage);
	LOGGER.info("VERIFIED THE MANAGEMENT FRAMES POWER LEVEL: " + result);
	LOGGER.debug("ENDING METHOD: verifyMgmtPowerLevels");
	return resultObj;
    }
}
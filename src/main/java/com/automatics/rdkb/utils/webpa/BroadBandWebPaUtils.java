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
package com.automatics.rdkb.utils.webpa;

/**
 * 
 * Utility class for WebPA related functionality.
 * 
 * @author Selvaraj Mariyappan
 * @refactor Govardhan
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Dut;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.CommonUtils;
import com.automatics.rdkb.utils.dmcli.DmcliUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.webpa.WebPaParameter;
import com.automatics.webpa.WebPaServerResponse;
import com.automatics.rdkb.utils.wifi.BroadBandSystemUtils;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;

public class BroadBandWebPaUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandWebPaUtils.class);

    /**
     * Utility Method to set and get parameter values using WebPA command
     * 
     * @param device
     *            The Dut to be used.
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance.
     * @param webpaParameter
     *            The webpa parameter that has to be passed.
     * @param dataType
     *            Data type of the value.
     * @param valueToBePassed
     *            Value that has to be passed.
     * @return status Boolean representing the result of set and get actions.
     * 
     * @author Govardhan
     */
    public static boolean setAndGetParameterValuesUsingWebPa(Dut device, AutomaticsTapApi tapEnv, String webPaParam,
	    int dataType, String valueToBePassed) {
	LOGGER.debug("STARTING METHOD: setAndGetParameterValuesUsingWebPa");
	boolean status = false;
	String response = null;
	WebPaParameter webPaParameter = new WebPaParameter();
	webPaParameter.setDataType(dataType);
	webPaParameter.setName(webPaParam);
	webPaParameter.setValue(valueToBePassed);
	status = BroadBandCommonUtils.setWebPaParam(tapEnv, device, webPaParameter);
	LOGGER.info("Status of setting WebPaParameter values: " + status);
	if (status) {
	    status = false;

	    if (webPaParam.contains("Device.WiFi")) {
		LOGGER.info(
			"Waiting for 90 seconds to reflect the WiFi changes before getting or setting any WiFi parameters.");
		tapEnv.waitTill(BroadBandTestConstants.NINETY_SECOND_IN_MILLIS);
	    }

	    response = tapEnv.executeWebPaCommand(device, webPaParam);
	    LOGGER.info("valueToBePassed: " + valueToBePassed);
	    LOGGER.info("response: " + response);
	    if (CommonMethods.isNotNull(response)
		    && CommonUtils.patternSearchFromTargetString(response, valueToBePassed)) {
		status = true;
	    }
	    LOGGER.info("Status of getting WebPaParameter values: " + status);
	}
	LOGGER.debug("ENDING METHOD: setAndGetParameterValuesUsingWebPa");
	return status;
    }

    /**
     * Helper Method to verify the WebPA is up & running. Verified using WebPA command execution against a sample
     * parameter 2.4 GHz Private SSID Name.
     * 
     * @param tapEnv
     *            {@link ECatsTapApi}
     * @param device
     *            {@link Dut}
     * @param pollProcess
     *            Boolean representing whether polling needs to be performed for validation or NOT.
     * 
     * @return Boolean representing the result of operation; TRUE if WebPA Command Execution returns value; else FALSE
     * @author Govardhan
     */
    public static boolean verifyWebPaProcessIsUp(AutomaticsTapApi tapEnv, Dut device, boolean pollProcess) {
	LOGGER.debug("ENTERING METHOD: verifyWebPaProcessIsUp");
	boolean result = false;
	String response = null;
	long startTime = System.currentTimeMillis();
	do {
	    tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
	    try {
		response = tapEnv.executeWebPaCommand(device,
			BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_SERIAL_NUMBER);
	    } catch (Exception testException) {
		// Log & Suppress the exception.
		LOGGER.error("EXCEPTION OCCURRED WHILE VERIFYING WEBPA PROCESS IS UP: " + testException.getMessage());
	    }
	    result = CommonMethods.isNotNull(response);
	} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.EIGHT_MINUTE_IN_MILLIS && !result
		&& pollProcess);
	LOGGER.info("WEBPA PROCESS VERIFIED WITH WEBPA COMMAND EXECUTION AGAINST SAMPLE PARAMETER: " + result);
	LOGGER.debug("ENDING METHOD: verifyWebPaProcessIsUp");
	return result;
    }

    /**
     * Utility Method to get parameter value using WebPA command. If WebPA fails fall back to DMCLI to query same
     * parameter. This method is only used for setting preconditions.
     * 
     * @param device
     *            The Dut to be used.
     * @param tapApi
     *            The {@link AutomaticsTapApi} instance.
     * @return Parameter values which retrieved from WebPA and fall back as DMCLI.
     * @author Govardhan
     */
    public static String getParameterValuesUsingWebPaOrDmcli(Dut device, AutomaticsTapApi tapApi, String parameter) {
	LOGGER.info("Starting Method getParameterValuesUsingWebPaOrDmcli()");
	String parameterValue = null;
	boolean isWebpaConnBroken = Boolean.parseBoolean(System.getProperty(
		BroadBandTestConstants.SYSTEM_PROPERTY_WEBPA_CONNECTIVITY_BROKEN, BroadBandTestConstants.FALSE));
	LOGGER.info("Is WebpaConnectionBroken : " + isWebpaConnBroken);
	if (!isWebpaConnBroken) {
	    try {
		parameterValue = tapApi.executeWebPaCommand(device, parameter);
	    } catch (Exception e) {
		LOGGER.error("Exception caught while retrieving value using webpa: " + e.getMessage());
	    }
	}
	if (CommonMethods.isNull(parameterValue)) {
	    try {
		parameterValue = DmcliUtils.getParameterValueUsingDmcliCommand(device, tapApi, parameter);
	    } catch (Exception e) {

		LOGGER.error("Exception caught while retrieving value using dmcli: " + e.getMessage());
	    }
	}
	LOGGER.info("Parameter Value is : " + parameterValue);
	LOGGER.info("Ending Method getParameterValuesUsingWebPaOrDmcli()");
	return parameterValue;
    }

    /**
     * Method to generate an object of type WebPaParameter
     * 
     * @param string
     *            parametername @ param string value @ param int dataType
     * 
     * @return WebPaParameter
     * @author Govardhan
     */

    public static WebPaParameter generateWebpaParameterWithValueAndType(String parameter, String value, int dataType) {
	LOGGER.debug("STARTING METHOD: generateWebpaParameterWithValueAndType");
	WebPaParameter webparameter = new WebPaParameter();
	webparameter.setDataType(dataType);
	webparameter.setName(parameter);
	webparameter.setValue(value);
	LOGGER.debug("EXITING METHOD: generateWebpaParameterWithValueAndType");
	return webparameter;
    }

    /**
     * Helper method to get the WebPa value and compare with given value in polling
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param webPaParam
     *            WebPa parameter name
     * @param valueToBeMatched
     *            value to be matched with the webpa response
     * @param maxDuration
     *            maximum polling duration
     * @param pollDuration
     *            wait time for each itration
     * @return status
     * @author Govardhan
     * 
     */
    public static boolean getAndVerifyWebpaValueInPolledDuration(Dut device, AutomaticsTapApi tapEnv, String webPaParam,
	    String valueToBeMatched, long maxDuration, long pollDuration) {
	LOGGER.debug("STARTING METHOD: getAndVerifyWebpaValueInPolledDuration");
	boolean status = false;
	String response = null;
	long startTime = System.currentTimeMillis();
	do {
	    response = tapEnv.executeWebPaCommand(device, webPaParam);
	    if (CommonMethods.isNotNull(response)) {
		status = response.equalsIgnoreCase(valueToBeMatched);
	    }
	} while ((System.currentTimeMillis() - startTime) < maxDuration && !status
		&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, pollDuration));
	LOGGER.info("WebPa response for given parameter is: " + response);
	LOGGER.debug("ENDING METHOD: getAndVerifyWebpaValueInPolledDuration");
	return status;
    }

    /**
     * Helper method to set parameter values using WebPA command then get value after wait period and verify
     * 
     * @param device
     *            The Dut to be used.
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance.
     * @param webpaParameter
     *            The webpa parameter that has to be passed.
     * @param dataType
     *            Data type of the value.
     * @param valueToBePassed
     *            Value that has to be passed.
     * @param waitDuration
     *            value in milliseconds to wait for after setting value
     * @return status Boolean representing the result of set and get actions.
     * 
     * @author Govardhan
     */
    public static boolean verifyWebPaValueAfterDuration(Dut device, AutomaticsTapApi tapEnv, String webPaParam,
	    int dataType, String valueToBePassed, long waitDuration) {
	LOGGER.debug("Entering Method: verifyWebPaValueAfterDuration");
	boolean result = false;
	String response = null;
	WebPaParameter webPaParameter = new WebPaParameter();
	webPaParameter.setDataType(dataType);
	webPaParameter.setName(webPaParam);
	webPaParameter.setValue(valueToBePassed);
	result = BroadBandCommonUtils.setWebPaParam(tapEnv, device, webPaParameter);
	if (result) {
	    // Waiting after successful setting of parameter
	    LOGGER.info("Waiting for " + waitDuration / 1000 + " seconds");
	    tapEnv.waitTill(waitDuration);
	    response = tapEnv.executeWebPaCommand(device, webPaParam);
	    result = CommonMethods.isNotNull(response) && response.equalsIgnoreCase(valueToBePassed);
	}
	LOGGER.info("Result of setting WebPaParameter values: " + result);
	LOGGER.debug("Exiting Method: verifyWebPaValueAfterDuration");
	return result;
    }

    /**
     * Method to do polled set of WebPA parameter, then poll get operation for specified duration and verify value is
     * set.
     * 
     * @param device
     *            The Dut to be used.
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance.
     * @param webpaParameter
     *            The webpa parameter that has to be passed.
     * @param dataType
     *            Data type of the value.
     * @param valueToBePassed
     *            Value that has to be passed.
     * @param maxDuration
     *            value in milliseconds to totally poll for
     * @param pollDuration
     *            value in milliseconds to wait for in between checks
     * @return result Boolean value representing the result of set and get actions.
     * 
     * @author Govardhan
     */

    public static boolean setVerifyWebPAInPolledDuration(Dut device, AutomaticsTapApi tapEnv, String webPaParam,
	    int dataType, String valueToBePassed, long maxDuration, long pollDuration) {
	LOGGER.debug("STARTING METHOD: setVerifyWebPAInPolledDuration");
	boolean result = false;
	String response = null;
	// Polled fetching of WebPA parameter before setting
	long startTime = System.currentTimeMillis();
	do {
	    response = tapEnv.executeWebPaCommand(device, webPaParam);
	    result = CommonMethods.isNotNull(response);
	    if (result) {
		LOGGER.info("WebPA get successful");
		break;
	    }
	    LOGGER.info("WebPA get failed");
	} while ((System.currentTimeMillis() - startTime) < maxDuration
		&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, pollDuration));
	// Checking for null value of parameter
	/* result = CommonMethods.isNotNull(response); */
	/* if (result) { */
	// Comparing obtained value with value to be set
	result = CommonMethods.isNotNull(response) && response.equalsIgnoreCase(valueToBePassed);
	if (result) {
	    // Returning true as value is already equal to value to be set
	    return true;
	} else {
	    WebPaParameter webPaParameter = new WebPaParameter();
	    webPaParameter = generateWebpaParameterWithValueAndType(webPaParam, valueToBePassed, dataType);
	    // Polled setting of WebPA parameter
	    startTime = System.currentTimeMillis();
	    do {
		result = BroadBandCommonUtils.setWebPaParam(tapEnv, device, webPaParameter);
		if (result) {
		    LOGGER.info("WebPA set successful");
		    break;
		}
		LOGGER.info("WebPA set failed");
	    } while ((System.currentTimeMillis() - startTime) < maxDuration
		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, pollDuration));
	    if (result) {
		// Polled fetching of WebPA parameter after setting
		startTime = System.currentTimeMillis();
		do {
		    response = tapEnv.executeWebPaCommand(device, webPaParam);
		    // Verifying WebPA value after setting is not null and
		    // equal to value to be set
		    result = CommonMethods.isNotNull(response) && response.equalsIgnoreCase(valueToBePassed);
		    if (result) {
			LOGGER.info("WebPA get after set successful");
			break;
		    }
		    LOGGER.info("WebPA get after set failed");
		} while ((System.currentTimeMillis() - startTime) < maxDuration
			&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, pollDuration));
	    }
	}
	/* } */
	LOGGER.debug("EXITING : setVerifyWebPAInPolledDuration");
	return result;
    }

    /**
     * Helper method to get TR-181 data value using WebPA or DMCLI
     * 
     * @param device
     *            Dut instance
     * @param tapApi
     *            AutomaticsTapApi instance
     * @param parameter
     *            TR-181 parameter name
     * @param valueToBeVerified
     *            value to be verified.
     * @return true, if value expected and value obtained or same.
     * 
     * @author Govardhan
     */
    public static boolean getParameterValuesUsingWebPaOrDmcliAndVerify(Dut settop, AutomaticsTapApi tapApi,
	    String parameter, String valueToBeVerified) {
	LOGGER.debug("STARTING METHOD getParameterValuesUsingWebPaOrDmcliAndVerify()");
	boolean status = false;
	String response = getParameterValuesUsingWebPaOrDmcli(settop, tapApi, parameter);
	status = CommonMethods.isNotNull(response) && response.equalsIgnoreCase(valueToBeVerified);
	LOGGER.info("Is value expected is same as value obtained - " + status + " Value expected - " + valueToBeVerified
		+ " value obtained - " + response);
	LOGGER.debug("ENDING METHOD getParameterValuesUsingWebPaOrDmcliAndVerify()");
	return status;
    }

    /**
     * Utility method to verify the supported security modes
     * 
     * @param settop
     *            The device to be validated
     * @param tapEnv
     *            {@link ECatsTapApi} Reference
     * @param parameter
     *            webPA Parameter to set the security mode
     * @param setValue
     * @return true, if webpa set value and get ouput are same and Telemetry marker appeared in the logs
     */

    public static Boolean setAndVerifyAllSecurityModesSupported(Dut device, AutomaticsTapApi tapEnv, String parameter,
	    String setValue) {
	LOGGER.debug("ENTERING METHOD : setAndVerifyAllSecurityModesSupported");
	String webPaGetResponse = null;
	boolean status = false;
	if (CommonMethods.isNotNull(parameter)) {
	    LOGGER.info("***************************************************");
	    LOGGER.info("Doing WEBPA Set with value" + setValue);
	    LOGGER.info("***************************************************");
	    List<WebPaParameter> setResponse = tapEnv.setWebPaParams(device, parameter, setValue, 0);
	    if (CommonMethods.isNotNull(setResponse.get(0).getMessage())
		    && (setResponse.get(0).getMessage().equalsIgnoreCase(BroadBandTestConstants.SUCCESS_TXT))) {
		LOGGER.info("**********************************************");
		LOGGER.info("Doing WEBPA get");
		LOGGER.info("**********************************************");
		webPaGetResponse = tapEnv.executeWebPaCommand(device, parameter);
		status = (CommonMethods.isNotNull(webPaGetResponse) && webPaGetResponse.equalsIgnoreCase(setValue));

	    } else {
		LOGGER.info("WEBPA SET FAILED");
	    }
	}
	LOGGER.debug("ENDING METHOD : setAndVerifyAllSecurityModesSupported");
	return status;

    }

    /**
     * Utility method to check Appropriate Telemetry markers are present in /rdklogs/logs/WiFilogs.txt.0 file after
     * setting the security mode
     * 
     * @param device
     *            The device to be validated
     * @param tapEnv
     *            {@link AutomaticsTapApi} Reference
     * @param setValue
     *            Contains the Security mode that is being verified
     * @param timeStamp
     *            Contains the Current time stamp in device
     * @return
     */
    public static Boolean checkingForTelemetryMarkerInDeviceAfterSettingSecurityModes(Dut device,
	    AutomaticsTapApi tapEnv, String setValue, String timeStamp) {
	boolean status = false;
	if (setValue.equalsIgnoreCase(BroadBandTestConstants.SECURITY_MODE_WPA2_ENTERPRISE)) {
	    setValue = setValue.replace(BroadBandTestConstants.CHARACTER_HYPHEN, CommonMethods.CHARACTER_UNDER_SCORE);
	}
	LOGGER.debug("ENTERING METHOD : checkFortelemetrymarker");
	LOGGER.info("timeStamp is " + timeStamp);
	tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
	long startTime = System.currentTimeMillis();
	status = BroadBandSystemUtils.verifyArmConsoleLogForPollingTime(tapEnv, device,
		BroadBandTestConstants.TELEMTRY_MARKER_FOR_WIFI_CONFIG_CHANGED
			.replace(BroadBandTestConstants.TELEMTRY_MARKER_SET_VALUE, setValue),
		BroadBandTestConstants.LOCATION_WIFI_LOG, timeStamp, BroadBandTestConstants.TWO_MINUTES);
	LOGGER.debug("ENDING METHOD : checkFortelemetrymarker");
	return status;
    }

    /**
     * Utility Method to set and get parameter values using WebPA command or Dmcli Command
     * 
     * @param device
     *            The device to be used.
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance.
     * @param webpaParameter
     *            The webpa parameter that has to be passed.
     * @param dataType
     *            Data type of the value.
     * @param valueToBePassed
     *            Value that has to be passed.
     * @return status Boolean representing the result of set and get actions.
     * 
     * @author Sumathi Gunasekaran
     * @Refactor Athira
     */
    public static boolean setParameterValuesUsingWebPaOrDmcli(Dut device, AutomaticsTapApi tapEnv, String webPaParam,
	    int dataType, String valueToBePassed) {
	LOGGER.debug("STARTING METHOD: setAndGetParameterValuesUsingWebPa");
	boolean status = false;
	String dmcliDatatype = null;
	WebPaParameter webPaParameter = new WebPaParameter();
	webPaParameter.setDataType(dataType);
	webPaParameter.setName(webPaParam);
	webPaParameter.setValue(valueToBePassed);
	status = BroadBandCommonUtils.setWebPaParam(tapEnv, device, webPaParameter);
	LOGGER.debug("Status of setting WebPaParameter values: " + status);
	if (!status) {
	    LOGGER.error("Setting Parameter Value using Webpa failed, so executing DMCLI command");
	    switch (dataType) {
	    case AutomaticsConstants.CONSTANT_0:
		dmcliDatatype = BroadBandTestConstants.DMCLI_SUFFIX_TO_SET_STRING_PARAMETER;
		break;
	    case AutomaticsConstants.CONSTANT_1:
		dmcliDatatype = BroadBandTestConstants.DMCLI_SUFFIX_TO_SET_INT_PARAMETER;
		break;
	    case AutomaticsConstants.CONSTANT_2:
		dmcliDatatype = BroadBandTestConstants.DMCLI_SUFFIX_TO_SET_UINT_PARAMETER;
		break;
	    case AutomaticsConstants.CONSTANT_3:
		dmcliDatatype = BroadBandTestConstants.DMCLI_SUFFIX_TO_SET_BOOLEAN_PARAMETER;
		break;
	    case AutomaticsConstants.CONSTANT_4:
		dmcliDatatype = BroadBandTestConstants.DMCLI_SUFFIX_TO_SET_DATETIME_PARAMETER;
		break;
	    default:
		dmcliDatatype = BroadBandTestConstants.DMCLI_SUFFIX_TO_SET_INVALID_PARAMETER;
		break;
	    }

	    if (webPaParam.contains(BroadBandWebPaConstants.WEBPA_TABLE_DEVICE_WIFI)) {
		if (webPaParam.contains(BroadBandWebPaConstants.WEBPA_INDEX_2_4_GHZ_PRIVATE_SSID)) {
		    webPaParam = webPaParam.replace(BroadBandWebPaConstants.WEBPA_INDEX_2_4_GHZ_PRIVATE_SSID, "1");
		} else if (webPaParam.contains(BroadBandWebPaConstants.WEBPA_INDEX_5_GHZ_PRIVATE_SSID)) {
		    webPaParam = webPaParam.replace(BroadBandWebPaConstants.WEBPA_INDEX_5_GHZ_PRIVATE_SSID, "2");
		} else if (webPaParam.contains(BroadBandWebPaConstants.WEBPA_INDEX_2_4_GHZ_PUBLIC_WIFI)) {
		    webPaParam = webPaParam.replace(BroadBandWebPaConstants.WEBPA_INDEX_2_4_GHZ_PUBLIC_WIFI, "5");
		} else if (webPaParam.contains(BroadBandWebPaConstants.WEBPA_INDEX_5_GHZ_PUBLIC_WIFI)) {
		    webPaParam = webPaParam.replace(BroadBandWebPaConstants.WEBPA_INDEX_5_GHZ_PUBLIC_WIFI, "6");
		} else if (webPaParam.contains(BroadBandWebPaConstants.WEBPA_INDEX_2_4_GHZ_PUBLIC_SSID_AP2)) {
		    webPaParam = webPaParam.replace(BroadBandWebPaConstants.WEBPA_INDEX_2_4_GHZ_PUBLIC_SSID_AP2, "9");
		} else if (webPaParam.contains(BroadBandWebPaConstants.WEBPA_INDEX_5_GHZ_PUBLIC_SSID_AP2)) {
		    webPaParam = webPaParam.replace(BroadBandWebPaConstants.WEBPA_INDEX_5_GHZ_PUBLIC_SSID_AP2, "10");
		} else if (webPaParam.contains(BroadBandTestConstants.RADIO_24_GHZ_INDEX)) {
		    webPaParam = webPaParam.replace(BroadBandTestConstants.RADIO_24_GHZ_INDEX, "1");
		} else if (webPaParam.contains(BroadBandTestConstants.RADIO_5_GHZ_INDEX)) {
		    webPaParam = webPaParam.replace(BroadBandTestConstants.RADIO_24_GHZ_INDEX, "2");
		}
	    }

	    if (!dmcliDatatype.equalsIgnoreCase(BroadBandTestConstants.DMCLI_SUFFIX_TO_SET_INVALID_PARAMETER)) {
		status = DmcliUtils.setParameterValueUsingDmcliCommand(device, tapEnv, webPaParam, dmcliDatatype,
			valueToBePassed);
	    }
	}
	return status;
    }

    /**
     * Utils method to execute WebPA Set command & return WebPa Server response
     * 
     * @param device
     * @param parameter
     * @param value
     * @param dataType
     * @return WebPa Server response
     */
    public static WebPaServerResponse setWebPaParamAndReturnResp(AutomaticsTapApi tapEnv, Dut device, String parameter,
	    String value, int dataType) {

	LOGGER.debug("STARTING METHOD: BroadBandWebPaUtils.setWebPaParamAndReturnResp");
	WebPaParameter webPaParam = new WebPaParameter();
	webPaParam.setDataType(dataType);
	webPaParam.setName(parameter);
	webPaParam.setValue(value);
	List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
	webPaParameters.add(webPaParam);

	WebPaServerResponse webPaSetResponse = tapEnv.setWebPaParameterValues(device, webPaParameters);
	LOGGER.info("WebPA response message: " + webPaSetResponse.getMessage());
	LOGGER.debug("ENDING METHOD: BroadBandWebPaUtils.setWebPaParamAndReturnResp");

	return webPaSetResponse;
    }

    /**
     * Utility Method to set and get parameter values using WebPA command
     * 
     * @param device
     *            The device to be used.
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance.
     * @param webpaParameter
     *            The webpa parameter that has to be passed.
     * @param dataType
     *            Data type of the value.
     * @param valueToBePassed
     *            Value that has to be passed.
     * @return status Boolean representing the result of set and get actions.
     * 
     * @author Praveenkumar Paneerselvam
     * @Refactor Athira
     */
    public static boolean setAndVerifyParameterValuesUsingWebPaorDmcli(Dut device, AutomaticsTapApi tapEnv,
	    String webPaParam, int dataType, String valueToBePassed) {
	LOGGER.debug("STARTING METHOD: setAndVerifyParameterValuesUsingWebPaorDmcli");
	boolean status = false;
	String response = null;
	status = setParameterValuesUsingWebPaOrDmcli(device, tapEnv, webPaParam, dataType, valueToBePassed);
	LOGGER.info("Status of setting WebPaParameter values: " + status);
	if (status) {
	    status = false;
	    if (webPaParam.contains("Device.WiFi")) {
		LOGGER.info(
			"Waiting for 90 seconds to reflect the WiFi changes before getting or setting any WiFi parameters.");
		tapEnv.waitTill(BroadBandTestConstants.NINTY_SECOND_IN_MILLIS);
	    }
	    response = tapEnv.executeWebPaCommand(device, webPaParam);
	    LOGGER.info("valueToBePassed: " + valueToBePassed);
	    LOGGER.info("response: " + response);
	    status = CommonMethods.isNotNull(response)
		    && CommonUtils.patternSearchFromTargetString(response, valueToBePassed);
	    if (!status) {
		LOGGER.info("Validating through WebPA failed vaildating using DMCLI command");
		response = getParameterValuesUsingWebPaOrDmcli(device, tapEnv, webPaParam);
		status = CommonMethods.isNotNull(response)
			&& CommonUtils.patternSearchFromTargetString(response, valueToBePassed);
	    }
	    LOGGER.info("Status of getting WebPaParameter values: " + status);
	} else {
	    LOGGER.info("The WebPaParam " + webPaParam + " could not set to the value " + valueToBePassed);
	}
	LOGGER.debug("ENDING METHOD: setAndVerifyParameterValuesUsingWebPaorDmcli");
	return status;
    }
    
	/**
     * Utility method to set multiple parameters via WebPA or dmcli.
     * 
     * @param device
     *            The device to be validated.
     * @param tapEnv
     *            The {@link AutomaticsTapApi} reference.
     * @param webPaParameters
     *            The list of {@link WebPaParameter}
     * @return True if all parameters are set properly, otherwise false.
     */
    public static boolean setMultipleParametersUsingWebPaOrDmcli(Dut device, AutomaticsTapApi tapEnv,
	    List<WebPaParameter> webPaParameters) {

	boolean status = false;
	if (webPaParameters != null && !webPaParameters.isEmpty()) {
	    boolean isWebpaConnBroken = Boolean.parseBoolean(System.getProperty(
		    BroadBandTestConstants.SYSTEM_PROPERTY_WEBPA_CONNECTIVITY_BROKEN, BroadBandTestConstants.FALSE));
	    if (!isWebpaConnBroken) {
		WebPaServerResponse serverResponse = tapEnv.setWebPaParameterValues(device, webPaParameters);
		if (200 == serverResponse.getStatusCode() || 201 == serverResponse.getStatusCode()) {
		    status = true;
		} else if (520 != serverResponse.getStatusCode()) {
		    System.setProperty(BroadBandTestConstants.SYSTEM_PROPERTY_WEBPA_CONNECTIVITY_BROKEN,
			    BroadBandTestConstants.TRUE);
		}
	    }

	    if (!status) {
		boolean isWiFiParameterPresent = false;
		for (WebPaParameter param : webPaParameters) {
		    if (param.getName().contains(BroadBandWebPaConstants.WEBPA_TABLE_DEVICE_WIFI)) {
			isWiFiParameterPresent = true;
			LOGGER.info("Wi-Fi SET Parameter Present, adding Apply setting parameters");
			break;
		    }

		}
		if (isWiFiParameterPresent) {
		    WebPaParameter wifiRadio2gApplySettings = BroadBandWebPaUtils
			    .generateWebpaParameterWithValueAndType(
				    BroadBandWebPaConstants.WEBPA_PARAM_WIFI_2_4_APPLY_SETTING,
				    BroadBandTestConstants.TRUE, WebPaDataTypes.BOOLEAN.getValue());
		    webPaParameters.add(wifiRadio2gApplySettings);

		    WebPaParameter wifiRadio5gApplySettings = BroadBandWebPaUtils
			    .generateWebpaParameterWithValueAndType(
				    BroadBandWebPaConstants.WEBPA_PARAM_WIFI_5_APPLY_SETTING,
				    BroadBandTestConstants.TRUE, WebPaDataTypes.BOOLEAN.getValue());
		    webPaParameters.add(wifiRadio5gApplySettings);
		}
		BroadBandResultObject resultObject = setMultipleParametersUsingDmcli(device, tapEnv, webPaParameters);
		status = resultObject.isStatus();
	    }
	}

	return status;
    }
	/**
     * Utility method to set Multiple parameters dmcli command
     * 
     * @param device
     *            The Dut to be queried
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance
     * @param webPaParameters
     *            The list of webpa parameters.
     */
    public static BroadBandResultObject setMultipleParametersUsingDmcli(Dut device, AutomaticsTapApi tapEnv,
	    List<WebPaParameter> webPaParameters) {
	StringBuffer failedParams = new StringBuffer().append("Unable to set these parameters : \n");
	BroadBandResultObject broadBandResultObject = new BroadBandResultObject();
	boolean status = false;
	for (WebPaParameter webPaParameter : webPaParameters) {
	    try {
		boolean isDmcliSetCompleted = DmcliUtils.setWebPaParameterValueUsingDmcliCommand(device, tapEnv,
			webPaParameter.getName(), webPaParameter.getDataType(), webPaParameter.getValue());
		if (!isDmcliSetCompleted) {
		    failedParams.append(webPaParameter.getName()).append(" with value : ")
			    .append(webPaParameter.getValue()).append("\n");
		}
	    } catch (Exception ex) {
		LOGGER.error("Exception occured while setting parameter " + webPaParameter.getName(), ex);
		failedParams.append(webPaParameter.getName()).append(" with value : ").append(webPaParameter.getValue())
			.append("\n");
	    }
	}
	status = failedParams.toString().isEmpty();

	broadBandResultObject.setErrorMessage(failedParams.toString());
	broadBandResultObject.setStatus(status);

	return broadBandResultObject;
    }
    
	/**
     * Method to set bridge mode and mesh as precondition or postcondition
     * 
     * @param device
     *            Dut to be used for execution
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance.
     * @param bridgeStatus
     *            Boolean value containing true/false for enable/disable bridge mode
     * @param meshStatus
     *            Boolean value containing value of mesh enable to be set
     * @return result Boolean value containing result of set and verify operations
     * 
     * @author Ashwin Sankarasubramanian, Ashwin sankara
     */

    public static boolean setAndVerifyBridgeModeAndMesh(Dut device, AutomaticsTapApi tapEnv, boolean bridgeStatus,
	    boolean meshStatus) {
	LOGGER.debug("Entering method: setAndVerifyBridgeModeAndMesh");
	boolean result = setVerifyWebPAInPolledDuration(device, tapEnv,
		BroadBandWebPaConstants.WEBPA_PARAM_BRIDGE_MODE_ENABLE, BroadBandTestConstants.CONSTANT_0,
		(bridgeStatus ? BroadBandTestConstants.CONSTANT_BRIDGE_STATIC
			: BroadBandTestConstants.LAN_MANAGEMENT_MODE_ROUTER),
		BroadBandTestConstants.THREE_MINUTE_IN_MILLIS, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
	if (result) {
	    LOGGER.info("Successfully set bridge mode to " + bridgeStatus);
	    result = setVerifyWebPAInPolledDuration(device, tapEnv,
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_MESH_ENABLE,
		    BroadBandTestConstants.CONSTANT_3,
		    (meshStatus ? BroadBandTestConstants.TRUE : BroadBandTestConstants.FALSE),
		    BroadBandTestConstants.THREE_MINUTE_IN_MILLIS, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
	    if (result) {
		LOGGER.info("Successfully set mesh wifi to " + meshStatus);
	    }
	}
	LOGGER.debug("Exiting method: setAndVerifyBridgeModeAndMesh");
	return result;
    }
    
    /**
     * Utility method to get multiple parameter values using WebPA or DMCLI.
     * 
     * @param device
     *            The Dut to be validated.
     * @param tapApi
     *            The {@link AutomaticsTapApi } reference.
     * @param parameters
     *            The array of parameters to be queried.
     * @return {@link Map} of TR-181 and its response.
     */
    public static Map<String, String> getMultipleParameterValuesUsingWebPaOrDmcli(Dut device, AutomaticsTapApi tapApi,
	    String[] parameters) {

	boolean isWebpaConnBroken = Boolean.parseBoolean(System.getProperty(
		BroadBandTestConstants.SYSTEM_PROPERTY_WEBPA_CONNECTIVITY_BROKEN, BroadBandTestConstants.FALSE));
	Map<String, String> parameterValue = null;
	if (!isWebpaConnBroken) {
	    parameterValue = tapApi.executeMultipleWebPaGetCommands(device, parameters);
	}
	if (null == parameterValue || parameterValue.isEmpty()) {
	    parameterValue = DmcliUtils.getParamValuesUsingDmcli(device, tapApi, parameters);
	}

	return parameterValue;
    }
    
    /**
     * Utils method to check read-only attribute of WebPA parameter from WebPa Set Response
     * 
     * @param serverResponse
     * @param WebPaParameter
     * @Refactor Alan_Bivera
     * @return
     */
    public static BroadBandResultObject verifyReadOnlyAtributeOfWebPaParamFromWebPaServerResponse(
	    WebPaServerResponse serverResponse, String WebPaParameter) {
	BroadBandResultObject result = new BroadBandResultObject();
	boolean status = CommonMethods.isNotNull(serverResponse.getMessage())
		&& serverResponse.getMessage().trim().equalsIgnoreCase(BroadBandTestConstants.PARAMETER_NOT_WRITABLE);
	String errorMessage = CommonMethods.isNotNull(serverResponse.getMessage())
		&& serverResponse.getMessage().trim().equalsIgnoreCase(AutomaticsConstants.SUCCESS)
			? WebPaParameter + " is writable"
			: "Unable to check READ-ONLY attribute of WebPA Param: " + WebPaParameter + "Actual Response: "
				+ serverResponse.getMessage();
	result.setErrorMessage(errorMessage);
	result.setStatus(status);
	return result;
    }
}

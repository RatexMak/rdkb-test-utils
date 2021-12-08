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

import java.time.LocalTime;
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
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.CommonUtils;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.rdkb.utils.dmcli.DmcliUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.webpa.WebPaConnectionHandler;
import com.automatics.webpa.WebPaParameter;
import com.automatics.webpa.WebPaServerResponse;
import com.automatics.rdkb.utils.wifi.BroadBandSystemUtils;
import com.automatics.rdkb.utils.wifi.BroadBandWiFiUtils;
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
    
    /**
     * Utils method to calculate end to end and box processing time for WebPA operations
     * 
     * @param tapEnv
     * @param device
     * @param isAtom
     * @param isSetOperation
     * @param iteration
     */
    public List<Long> calculateEndToEndAndBoxProcessingTimeForWebPaOperation(AutomaticsTapApi tapEnv, Dut device,
	    boolean isAtom, boolean isSetOperation, Integer iteration) {

	List<Long> result = new ArrayList<>();
	long endToEndProcessingTime = 0;
	long boxProcessingTime = 0;
	String[] WebpaGetParamter = { BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_MODELNAME };
	List<WebPaParameter> listOfWebpaSetParameterObject = null;
	String operation = isSetOperation ? BroadBandTestConstants.SET : BroadBandTestConstants.GET;
	long processendTime = 0;
	long processstartTime = 0;
	int returnCode = 0;
	WebPaServerResponse webPaServerResponse = new WebPaServerResponse();

	try {
		 if (isSetOperation) {
				listOfWebpaSetParameterObject = BroadBandWebPaUtils.webpaSetOneParameter(iteration - 1);
				LOGGER.info("WEBPA PARAMETER AND SET VALUE USED IN ITERATION " + iteration + " : ");
				LOGGER.info("WEBPA PARAM : " + listOfWebpaSetParameterObject.get(0).getName() + " SET VALUE : "
					+ listOfWebpaSetParameterObject.get(0).getValue());
				LOGGER.info("**********************************************************************************");
		 }
		
	    String deviceStartTime = tapEnv.executeCommandUsingSsh(device,
			    BroadBandCommandConstants.CMD_DATE_RDKB_LOG_FORMAT);
	    if (isSetOperation) {
		// Execution of webPA set command
		processstartTime = System.currentTimeMillis();
		webPaServerResponse = WebPaConnectionHandler.get().setWebPaParameterValue(device, listOfWebpaSetParameterObject);
		processendTime = System.currentTimeMillis();

		if ((webPaServerResponse.getStatusCode()) == 200) {
		    LOGGER.info("Verification of webpa get response from device is Success in iteration step " + iteration);
		    endToEndProcessingTime = processendTime - processstartTime;
		} else {
		    LOGGER.info("Verification of webpa get response from device is Failure in iteration step " + iteration);
		}	
		
	    }
	    else {

			// Execution of webPA get command
			processstartTime = System.currentTimeMillis();
			webPaServerResponse = WebPaConnectionHandler.get().getWebPaParamValue(device, WebpaGetParamter);
			processendTime = System.currentTimeMillis();
			LOGGER.info("webPaServerResponse is " + webPaServerResponse);

			returnCode = webPaServerResponse.getStatusCode();
			if (returnCode == 200) {
			    LOGGER.info("Verification of webpa get response from device is Success in iteration step " + iteration);
			    endToEndProcessingTime = processendTime - processstartTime;
			} else {
			    LOGGER.info("Verification of webpa get response from device is Failure in iteration step " + iteration);
			}
	    }


	    String deviceEndTime = tapEnv.executeCommandUsingSsh(device,
		    BroadBandCommandConstants.CMD_DATE_RDKB_LOG_FORMAT);

	    if (endToEndProcessingTime != 0 && CommonMethods.isNotNull(deviceStartTime)
		    && CommonMethods.isNotNull(deviceEndTime)) {

		tapEnv.waitTill(BroadBandTestConstants.FIVE_SECONDS_IN_MILLIS);

		LOGGER.info("##### FETCHING LOG FOR CURRENT WEBPA OPERATION FROM PARODUS LOG FILE! #####");
		String response = tapEnv.executeCommandUsingSsh(device,
			generateGrepCommand(isAtom, BroadBandTestConstants.PARODUS_PROCESS_NAME, deviceStartTime,
				deviceEndTime, WebpaGetParamter[0], BroadBandTestConstants.EMPTY_STRING));
		LOGGER.info("##### LOG RETRIEVED FROM /rdklogs/logs/PARODUSlog.txt FOR CURRENT WEBPA OPERATION : "
			+ response);

		if (CommonMethods.isNotNull(response)) {

		    LOGGER.info(
			    "##### FETCHING TRANSACTION ID FOR THE CURRENT WEBPA OPERATION FROM PARODUS LOG! #####");
		    String transactionId = CommonMethods.patternFinder(response,
			    BroadBandTestConstants.PATTERN_TO_GET_TRANSACTION_ID_FROM_PARODUS_LOG);
		    if (CommonMethods.isNotNull(transactionId)) {
			transactionId = transactionId.trim();
			LOGGER.info("##### TRANSACTION ID OF THE WEBPA OPERATION : " + transactionId);

			LOGGER.info("##### FETCHING LOG FOR CURRENT WEBPA OPERATION FROM WEBPA LOG FILE! #####");
			String webPALog = isAtom
				? CommonMethods.executeCommandInAtomConsole(device, tapEnv,
					generateGrepCommand(isAtom, operation, deviceStartTime, deviceEndTime,
						WebpaGetParamter[0], transactionId))
				: tapEnv.executeCommandUsingSsh(device, generateGrepCommand(isAtom, operation,
					deviceStartTime, deviceEndTime, WebpaGetParamter[0], transactionId));
			LOGGER.info("##### LOG RETRIEVED FROM /rdklogs/logs/WEBPAlog.txt FOR CURRENT WEBPA OPERATION : "
				+ webPALog);

			if (CommonMethods.isNotNull(webPALog)) {
			    LOGGER.info(
				    "##### VALIDATING THE TRANSACTION ID RETRIEVED FROM PARODUS LOG WITH WEBPA LOG, IF ID MATCHES VALIDATING THE WEBPA OPERATION MODE ! #####");
			    boolean validation = isSetOperation
				    ? CommonUtils.patternSearchFromTargetString(webPALog,
					    BroadBandTestConstants.SET_REQUEST)
				    : (CommonUtils.patternSearchFromTargetString(webPALog,
					    BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_MODELNAME)
					    && CommonUtils.patternSearchFromTargetString(webPALog,
						    BroadBandTestConstants.GET));
			    if (validation && CommonUtils.patternSearchFromTargetString(webPALog, transactionId)) {
				LOGGER.info(
					"##### TRANSACTION ID & WEBPA OPERATION MODE ARE SUCCESSFULLY VALIDATED, MEANING PARODUS LOG RETRIEVED IS GOOD ENOUGH TO CALCULATE BOX PROCESSING TIME! #####");

				LOGGER.info(
					"##### FETCHING THE BOX PROCESSING START AND END TIME FROM PARODUS LOG! #####");
				String boxProcessingStartTime = CommonMethods.patternFinder(response,
					BroadBandCommonUtils.concatStringUsingStringBuffer(
						BroadBandTestConstants.OPEN_PARENTHESIS,
						BroadBandTestConstants.REGEX_FOR_RDKB_LOG_TIMESTAMP,
						BroadBandTestConstants.CLOSED_PARANTHESIS,
						BroadBandTestConstants.STRING_WILDCARD,
						BroadBandTraceConstants.PARODUS_WEBPA_REQUEST_RECEIVED_TRACE));
				LOGGER.info("BOX PROCESSING START TIME IN UTC : " + boxProcessingStartTime);
				String boxProcessingEndTime = CommonMethods.patternFinder(response,
					BroadBandCommonUtils.concatStringUsingStringBuffer(
						BroadBandTestConstants.OPEN_PARENTHESIS,
						BroadBandTestConstants.REGEX_FOR_RDKB_LOG_TIMESTAMP,
						BroadBandTestConstants.CLOSED_PARANTHESIS,
						BroadBandTestConstants.STRING_WILDCARD,
						BroadBandTraceConstants.PARODUS_WEBPA_RESPONSE_SENT_TRACE));
				LOGGER.info("BOX PROCESSING END TIME IN UTC : " + boxProcessingEndTime);

				if (CommonMethods.isNotNull(boxProcessingStartTime)
					&& CommonMethods.isNotNull(boxProcessingEndTime)) {

				    LOGGER.info(
					    "##### CONVERTING THE TIME STAMP FETCHED FROM LOG INTO LOCALTIME FORMAT! #####");

				    LocalTime startTime = LocalTime
					    .parse(CommonMethods.patternFinder(boxProcessingStartTime.trim(),
						    BroadBandTestConstants.REGEX_FOR_TIMESTAMP_WITH_ONLY_TIME));
				    LocalTime endTime = LocalTime
					    .parse(CommonMethods.patternFinder(boxProcessingEndTime.trim(),
						    BroadBandTestConstants.REGEX_FOR_TIMESTAMP_WITH_ONLY_TIME));

				    if (null != startTime && null != endTime
					    && startTime.toNanoOfDay() < endTime.toNanoOfDay()) {
					boxProcessingTime = endTime.toNanoOfDay() - startTime.toNanoOfDay();
					LOGGER.info("BOX PROCESSING TIME IN NANOSECONDS FOR CURRENT WEBPA OPERATION : "
						+ boxProcessingTime);
				    }
				}

			    }
			} else {
			    LOGGER.error(
				    "##### UNABLE TO FETCH LOG FOR CURRENT WEBPA OPERATION FROM WEBPA LOG FILE! #####");
			}
		    }
		} else {
		    LOGGER.error("##### UNABLE TO FETCH LOG FOR CURRENT WEBPA OPERATION FROM PARODUS LOG FILE! #####");
		}
	    }

	} catch (Exception e) {
	    LOGGER.error(
		    "EXCEPTION OCCURED CALCULATE END TO END/BOX PROCESSING TIME FOR WEBPA REQUEST : " + e.getMessage());
	}

	result.add(endToEndProcessingTime);
	result.add(boxProcessingTime);

	return result;
    }
    
    /**
     * method to form webpa get parameters
     * 
     * @param noOfParamters
     * @return webpa parameters in string array
     */
    public static String[] webpaGetParameters(Integer noOfParamters) {
	String[] returnOfParamters = new String[noOfParamters];
	switch (noOfParamters) {
	case 5:
	    returnOfParamters[0] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_PRODUCTCLASS;
	    returnOfParamters[1] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_MANUFACTURER_INFO;
	    returnOfParamters[2] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_HARDWARE_VERSION;
	    returnOfParamters[3] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_SERIAL_NUMBER;
	    returnOfParamters[4] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_FIRMWARE_NAME;
	    break;
	case 10:
		returnOfParamters[0] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_PRODUCTCLASS;
	    returnOfParamters[1] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_MANUFACTURER_INFO;
	    returnOfParamters[2] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_LANIP_ADDRESS;
	    returnOfParamters[3] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_LAN_SUBNET;
	    returnOfParamters[4] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_DHCP_SERVER;
	    returnOfParamters[5] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_DHCP_MINADDRESS;
	    returnOfParamters[6] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_DHCP_MAXADDRESS;
	    returnOfParamters[7] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_DHCP_LEASETIME;
	    returnOfParamters[8] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_DNS_CLIENT_SERVER;
	    returnOfParamters[9] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_DNS_CLIENT_SERVER_TYPE;
	    break;
	case 2:
	    returnOfParamters[0] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_PRODUCTCLASS;
	    returnOfParamters[1] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_MANUFACTURER_INFO;
	    break;
	case 3:
	    returnOfParamters[0] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_PRODUCTCLASS;
	    returnOfParamters[1] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_MANUFACTURER_INFO;
	    returnOfParamters[2] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_HARDWARE_VERSION;
	    break;
	case 4:
	    returnOfParamters[0] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_PRODUCTCLASS;
	    returnOfParamters[1] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_MANUFACTURER_INFO;
	    returnOfParamters[2] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_HARDWARE_VERSION;
	    returnOfParamters[3] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_SERIAL_NUMBER;
	    break;
	}
	return returnOfParamters;
    }
    
    /**
     * method to form webpa set one parameter and values
     * 
     * @param iteration
     * @return WebPaParameter object
     */
    public static List<WebPaParameter> webpaSetOneParameter(Integer iteration) {

	String[] setParametersOne = new String[10];
	setParametersOne[0] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_SELFHEAL_PINGINTERVAL;
	Integer[] dataTypeValueOne = { 2 };

	String[] setValuesOne0 = { BroadBandTestConstants.STRING_VALUE_20 };
	String[] setValuesOne1 = { BroadBandTestConstants.STRING_VALUE_30 };
	String[] setValuesOne2 = { BroadBandTestConstants.STRING_VALUE_40 };
	String[] setValuesOne3 = { BroadBandTestConstants.STRING_VALUE_50 };
	String[] setValuesOne4 = { BroadBandTestConstants.STRING_VALUE_60 };
	String[] setValuesOne5 = { BroadBandTestConstants.STRING_VALUE_70 };
	String[] setValuesOne6 = { BroadBandTestConstants.STRING_VALUE_80 };
	String[] setValuesOne7 = { BroadBandTestConstants.STRING_VALUE_90 };
	String[] setValuesOne8 = { BroadBandTestConstants.STRING_VALUE_100 };
	String[] setValuesOne9 = { BroadBandTestConstants.STRING_VALUE_110 };

	List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
	WebPaParameter webPaParam1 = new WebPaParameter();
	webPaParameters.add(webPaParam1);

	for (int count = 0; count < webPaParameters.size(); count++) {
	    webPaParameters.get(count).setName(setParametersOne[count]);
	    webPaParameters.get(count).setDataType(dataTypeValueOne[count]);
	}
	switch (iteration) {
	case 0:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesOne0[count]);
	    }
	    break;
	case 1:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesOne1[count]);
	    }
	    break;
	case 2:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesOne2[count]);
	    }
	    break;
	case 3:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesOne3[count]);
	    }
	    break;
	case 4:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesOne4[count]);
	    }
	    break;
	case 5:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesOne5[count]);
	    }
	    break;
	case 6:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesOne6[count]);
	    }
	    break;
	case 7:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesOne7[count]);
	    }
	    break;
	case 8:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesOne8[count]);
	    }
	    break;
	case 9:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesOne9[count]);
	    }
	    break;

	default:
	    break;
	}
	return webPaParameters;
    }
    
    /**
     * Utils method to generate Grep command
     * 
     * @param isAtom
     * @param operation
     * @param deviceStartTime
     * @param deviceEndTime
     * @param webPaParam
     * @param transactionId
     * @return
     */
    public String generateGrepCommand(boolean isAtom, String operation, String deviceStartTime, String deviceEndTime,
	    String webPaParam, String transactionId) {

	String command = "";

	try {

	    switch (operation) {

	    case BroadBandTestConstants.SET:

		command = isAtom ?

			BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND, "-A",
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.STRING_CONSTANT_4,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.DOUBLE_QUOTE,
				BroadBandTestConstants.SET_REQUEST, BroadBandTestConstants.DOUBLE_QUOTE,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
				BroadBandCommandConstants.LOG_FILE_WEBPA_TEXT,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, AutomaticsConstants.LINUX_PIPE_SYMBOL,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.GREP_COMMAND,
				"-A", BroadBandTestConstants.STRING_VALUE_TWO,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, "-B",
				BroadBandTestConstants.STRING_CONSTANT_1, BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
				BroadBandTestConstants.DOUBLE_QUOTE, transactionId, BroadBandTestConstants.DOUBLE_QUOTE)

			:

			BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND, "-A",
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.STRING_CONSTANT_4,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
				BroadBandTestConstants.SYMBOL_FORWARD_SLASH, BroadBandTestConstants.SINGLE_QUOTE,
				BroadBandTestConstants.SET_REQUEST, BroadBandTestConstants.SYMBOL_FORWARD_SLASH,
				BroadBandTestConstants.SINGLE_QUOTE, BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
				BroadBandCommandConstants.LOG_FILE_WEBPA_TEXT,
				BroadBandCommandConstants.CMD_GREP_AWK_START_RANGE, deviceStartTime.trim(),
				BroadBandCommandConstants.CMD_GREP_AWK_END_RANGE, deviceEndTime.trim(),
				BroadBandTestConstants.DOUBLE_QUOTE_SINGLE_QUOTE);

		break;

	    case BroadBandTestConstants.GET:

		command = isAtom ?

			BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND, "-A",
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.STRING_CONSTANT_4,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.DOUBLE_QUOTE,
				webPaParam, BroadBandTestConstants.DOUBLE_QUOTE,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
				BroadBandCommandConstants.LOG_FILE_WEBPA_TEXT,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, AutomaticsConstants.LINUX_PIPE_SYMBOL,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.GREP_COMMAND,
				"-A", BroadBandTestConstants.STRING_VALUE_TWO,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, "-B",
				BroadBandTestConstants.STRING_CONSTANT_1, BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
				BroadBandTestConstants.DOUBLE_QUOTE, transactionId, BroadBandTestConstants.DOUBLE_QUOTE)
			:

			BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND, "-A",
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.STRING_CONSTANT_4,
				BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
				BroadBandTestConstants.SYMBOL_FORWARD_SLASH, BroadBandTestConstants.SINGLE_QUOTE,
				webPaParam, BroadBandTestConstants.SYMBOL_FORWARD_SLASH,
				BroadBandTestConstants.SINGLE_QUOTE, BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
				BroadBandCommandConstants.LOG_FILE_WEBPA_TEXT,
				BroadBandCommandConstants.CMD_GREP_AWK_START_RANGE, deviceStartTime.trim(),
				BroadBandCommandConstants.CMD_GREP_AWK_END_RANGE, deviceEndTime.trim(),
				BroadBandTestConstants.DOUBLE_QUOTE_SINGLE_QUOTE);
		break;

	    case BroadBandTestConstants.PARODUS_PROCESS_NAME:

		command = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND, "-A",
			BroadBandTestConstants.SINGLE_SPACE_CHARACTER, String.valueOf(AutomaticsConstants.CONSTANT_8),
			BroadBandTestConstants.SINGLE_SPACE_CHARACTER, BroadBandTestConstants.SYMBOL_FORWARD_SLASH,
			BroadBandTestConstants.SINGLE_QUOTE,
			BroadBandTraceConstants.PARODUS_WEBPA_REQUEST_RECEIVED_TRACE,
			BroadBandTestConstants.SYMBOL_FORWARD_SLASH, BroadBandTestConstants.SINGLE_QUOTE,
			BroadBandTestConstants.SINGLE_SPACE_CHARACTER,
			BroadBandTestConstants.RDKLOGS_LOGS_PARODUS_TXT_0,
			BroadBandCommandConstants.CMD_GREP_AWK_START_RANGE, deviceStartTime.trim(),
			BroadBandCommandConstants.CMD_GREP_AWK_END_RANGE, deviceEndTime.trim(),
			BroadBandTestConstants.DOUBLE_QUOTE_SINGLE_QUOTE);
		break;

	    }

	} catch (Exception e) {
	    LOGGER.error("EXCEPTION OCCURED WHILE CONCATING GREP COMMAND : " + e.getMessage());
	}

	return command;

    }

    /**
     * this method has webpa set five parameters with values on iteration
     * 
     * @param iteration
     * @return WebPaParameter object
     */
    public static List<WebPaParameter> webpaSetFiveParameter(Integer iteration) {

	String[] setParametersFive = new String[10];
	setParametersFive[0] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_SELFHEAL_MAXRESET_COUNT;
	setParametersFive[1] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_PRIVATE_24GHZ_SSID;
	setParametersFive[2] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_PRIVATE_5GHZ_SSID;
	setParametersFive[3] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_PRIVATE_24GHZ_PASS;
	setParametersFive[4] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_PRIVATE_5GHZ_PASS;

	Integer[] dataTypeValueFive = { WebPaDataTypes.INTEGER.getValue(), WebPaDataTypes.STRING.getValue(),
		WebPaDataTypes.STRING.getValue(), WebPaDataTypes.STRING.getValue(), WebPaDataTypes.STRING.getValue() };

	String[] setValuesFiveForIterationOne = { BroadBandTestConstants.STRING_VALUE_10,
		BroadBandTestConstants.STRING_VALUE_24GHZ_SSID1, BroadBandTestConstants.STRING_VALUE_5GHZ_SSID1,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY1, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY1 };
	String[] setValuesFiveForIterationTwo = { BroadBandTestConstants.STRING_VALUE_11,
		BroadBandTestConstants.STRING_VALUE_24GHZ_SSID2, BroadBandTestConstants.STRING_VALUE_5GHZ_SSID2,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY2, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY2 };
	String[] setValuesFiveForIterationThree = { BroadBandTestConstants.STRING_VALUE_12,
		BroadBandTestConstants.STRING_VALUE_24GHZ_SSID3, BroadBandTestConstants.STRING_VALUE_5GHZ_SSID3,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY3, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY3 };
	String[] setValuesFiveForIterationFour = { BroadBandTestConstants.STRING_VALUE_13,
		BroadBandTestConstants.STRING_VALUE_24GHZ_SSID4, BroadBandTestConstants.STRING_VALUE_5GHZ_SSID4,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY4, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY4 };
	String[] setValuesFiveForIterationFive = { BroadBandTestConstants.STRING_VALUE_14,
		BroadBandTestConstants.STRING_VALUE_24GHZ_SSID5, BroadBandTestConstants.STRING_VALUE_5GHZ_SSID5,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY5, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY5 };
	String[] setValuesFiveForIterationSix = { BroadBandTestConstants.STRING_VALUE_15,
		BroadBandTestConstants.STRING_VALUE_24GHZ_SSID6, BroadBandTestConstants.STRING_VALUE_5GHZ_SSID6,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY6, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY6 };
	String[] setValuesFiveForIterationSeven = { BroadBandTestConstants.STRING_VALUE_16,
		BroadBandTestConstants.STRING_VALUE_24GHZ_SSID7, BroadBandTestConstants.STRING_VALUE_5GHZ_SSID7,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY7, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY7 };
	String[] setValuesFiveForIterationEight = { BroadBandTestConstants.STRING_VALUE_17,
		BroadBandTestConstants.STRING_VALUE_24GHZ_SSID8, BroadBandTestConstants.STRING_VALUE_5GHZ_SSID8,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY8, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY8 };
	String[] setValuesFiveForIterationNine = { BroadBandTestConstants.STRING_VALUE_18,
		BroadBandTestConstants.STRING_VALUE_24GHZ_SSID9, BroadBandTestConstants.STRING_VALUE_5GHZ_SSID9,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY9, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY9 };
	String[] setValuesFiveForIterationTen = { BroadBandTestConstants.STRING_VALUE_19,
		BroadBandTestConstants.STRING_VALUE_24GHZ_SSID10, BroadBandTestConstants.STRING_VALUE_5GHZ_SSID10,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY10, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY10 };

	List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
	WebPaParameter webPaParam1 = new WebPaParameter();
	WebPaParameter webPaParam2 = new WebPaParameter();
	WebPaParameter webPaParam3 = new WebPaParameter();
	WebPaParameter webPaParam4 = new WebPaParameter();
	WebPaParameter webPaParam5 = new WebPaParameter();

	webPaParameters.add(webPaParam1);
	webPaParameters.add(webPaParam2);
	webPaParameters.add(webPaParam3);
	webPaParameters.add(webPaParam4);
	webPaParameters.add(webPaParam5);

	for (int count = 0; count < webPaParameters.size(); count++) {
	    webPaParameters.get(count).setName(setParametersFive[count]);
	    webPaParameters.get(count).setDataType(dataTypeValueFive[count]);
	}
	switch (iteration) {
	case 0:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesFiveForIterationOne[count]);
	    }
	    break;
	case 1:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesFiveForIterationTwo[count]);
	    }
	    break;
	case 2:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesFiveForIterationThree[count]);
	    }
	    break;
	case 3:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesFiveForIterationFour[count]);
	    }
	    break;
	case 4:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesFiveForIterationFive[count]);
	    }
	    break;
	case 5:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesFiveForIterationSix[count]);
	    }
	    break;
	case 6:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesFiveForIterationSeven[count]);
	    }
	    break;
	case 7:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesFiveForIterationEight[count]);
	    }
	    break;
	case 8:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesFiveForIterationNine[count]);
	    }
	    break;
	case 9:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesFiveForIterationTen[count]);
	    }
	    break;

	default:
	    break;
	}
	return webPaParameters;
    }
    
    /**
     * 
     * Method to form webpa set with ten parameter with values on iteration values
     * 
     * @param iteration
     * @return WebPaParameter object
     */
    public static List<WebPaParameter> webpaSetTenParameter(Integer iteration) {

	String[] setParametersTen = new String[10];
	setParametersTen[0] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_BEACON_INTERVAL;
	setParametersTen[1] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_SELFHEAL_MAXREBOOT;
	setParametersTen[2] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_SELFHEAL_PING_WAITTIME;
	setParametersTen[3] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_2GHZ_REKEY_INTERVAL;
	setParametersTen[4] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_5GHZ_REKEY_INTERVAL;
	setParametersTen[5] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_5GHZ_RADIUS_REAUTH_INTERVAL;
	setParametersTen[6] = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID;
	setParametersTen[7] = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID;
	setParametersTen[8] = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PUBLIC_SSID;
	setParametersTen[9] = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PUBLIC_SSID;

	Integer[] dataTypeValueTen = { WebPaDataTypes.INTEGER.getValue(), WebPaDataTypes.INTEGER.getValue(),
		WebPaDataTypes.INTEGER.getValue(), WebPaDataTypes.INTEGER.getValue(), WebPaDataTypes.INTEGER.getValue(),
		WebPaDataTypes.UNSIGNED_INT.getValue(), WebPaDataTypes.STRING.getValue(),
		WebPaDataTypes.STRING.getValue(), WebPaDataTypes.STRING.getValue(), WebPaDataTypes.STRING.getValue() };

	String[] setValuesForIterationOne = { BroadBandTestConstants.STRING_VALUE_10,
		BroadBandTestConstants.STRING_VALUE_10, BroadBandTestConstants.STRING_VALUE_10,
		BroadBandTestConstants.STRING_VALUE_10, BroadBandTestConstants.STRING_VALUE_10,
		BroadBandTestConstants.STRING_VALUE_10, BroadBandTestConstants.STRING_VALUE_24GHZ_SSID1,
		BroadBandTestConstants.STRING_VALUE_5GHZ_SSID1, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY1,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY1 };
	String[] setValuesForIterationTwo = { BroadBandTestConstants.STRING_VALUE_20,
		BroadBandTestConstants.STRING_VALUE_20, BroadBandTestConstants.STRING_VALUE_20,
		BroadBandTestConstants.STRING_VALUE_20, BroadBandTestConstants.STRING_VALUE_20,
		BroadBandTestConstants.STRING_VALUE_20, BroadBandTestConstants.STRING_VALUE_24GHZ_SSID2,
		BroadBandTestConstants.STRING_VALUE_5GHZ_SSID2, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY2,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY2 };
	String[] setValuesForIterationThree = { BroadBandTestConstants.STRING_VALUE_30,
		BroadBandTestConstants.STRING_VALUE_30, BroadBandTestConstants.STRING_VALUE_30,
		BroadBandTestConstants.STRING_VALUE_30, BroadBandTestConstants.STRING_VALUE_30,
		BroadBandTestConstants.STRING_VALUE_30, BroadBandTestConstants.STRING_VALUE_24GHZ_SSID3,
		BroadBandTestConstants.STRING_VALUE_5GHZ_SSID3, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY3,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY3 };
	String[] setValuesForIterationFour = { BroadBandTestConstants.STRING_VALUE_40,
		BroadBandTestConstants.STRING_VALUE_40, BroadBandTestConstants.STRING_VALUE_40,
		BroadBandTestConstants.STRING_VALUE_40, BroadBandTestConstants.STRING_VALUE_40,
		BroadBandTestConstants.STRING_VALUE_40, BroadBandTestConstants.STRING_VALUE_24GHZ_SSID4,
		BroadBandTestConstants.STRING_VALUE_5GHZ_SSID4, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY4,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY4 };
	String[] setValuesForIterationFive = { BroadBandTestConstants.STRING_VALUE_50,
		BroadBandTestConstants.STRING_VALUE_50, BroadBandTestConstants.STRING_VALUE_50,
		BroadBandTestConstants.STRING_VALUE_50, BroadBandTestConstants.STRING_VALUE_50,
		BroadBandTestConstants.STRING_VALUE_50, BroadBandTestConstants.STRING_VALUE_24GHZ_SSID5,
		BroadBandTestConstants.STRING_VALUE_5GHZ_SSID5, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY5,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY5 };
	String[] setValuesForIterationSix = { BroadBandTestConstants.STRING_VALUE_60,
		BroadBandTestConstants.STRING_VALUE_10, BroadBandTestConstants.STRING_VALUE_60,
		BroadBandTestConstants.STRING_VALUE_60, BroadBandTestConstants.STRING_VALUE_60,
		BroadBandTestConstants.STRING_VALUE_60, BroadBandTestConstants.STRING_VALUE_24GHZ_SSID6,
		BroadBandTestConstants.STRING_VALUE_5GHZ_SSID6, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY6,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY6 };
	String[] setValuesForIterationSeven = { BroadBandTestConstants.STRING_VALUE_70,
		BroadBandTestConstants.STRING_VALUE_70, BroadBandTestConstants.STRING_VALUE_70,
		BroadBandTestConstants.STRING_VALUE_70, BroadBandTestConstants.STRING_VALUE_70,
		BroadBandTestConstants.STRING_VALUE_70, BroadBandTestConstants.STRING_VALUE_24GHZ_SSID7,
		BroadBandTestConstants.STRING_VALUE_5GHZ_SSID7, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY7,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY7 };
	String[] setValuesForIterationEight = { BroadBandTestConstants.STRING_VALUE_80,
		BroadBandTestConstants.STRING_VALUE_80, BroadBandTestConstants.STRING_VALUE_80,
		BroadBandTestConstants.STRING_VALUE_80, BroadBandTestConstants.STRING_VALUE_80,
		BroadBandTestConstants.STRING_VALUE_80, BroadBandTestConstants.STRING_VALUE_24GHZ_SSID8,
		BroadBandTestConstants.STRING_VALUE_5GHZ_SSID8, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY8,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY8 };
	String[] setValuesForIterationNine = { BroadBandTestConstants.STRING_VALUE_90,
		BroadBandTestConstants.STRING_VALUE_90, BroadBandTestConstants.STRING_VALUE_90,
		BroadBandTestConstants.STRING_VALUE_90, BroadBandTestConstants.STRING_VALUE_90,
		BroadBandTestConstants.STRING_VALUE_90, BroadBandTestConstants.STRING_VALUE_24GHZ_SSID9,
		BroadBandTestConstants.STRING_VALUE_5GHZ_SSID9, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY9,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY9 };
	String[] setValuesForIterationTen = { BroadBandTestConstants.STRING_VALUE_100,
		BroadBandTestConstants.STRING_VALUE_100, BroadBandTestConstants.STRING_VALUE_100,
		BroadBandTestConstants.STRING_VALUE_100, BroadBandTestConstants.STRING_VALUE_100,
		BroadBandTestConstants.STRING_VALUE_100, BroadBandTestConstants.STRING_VALUE_24GHZ_SSID10,
		BroadBandTestConstants.STRING_VALUE_5GHZ_SSID10, BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY10,
		BroadBandTestConstants.STRING_VALUE_24_5GHZ_KEY10 };

	List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
	WebPaParameter webPaParam1 = new WebPaParameter();
	WebPaParameter webPaParam2 = new WebPaParameter();
	WebPaParameter webPaParam3 = new WebPaParameter();
	WebPaParameter webPaParam4 = new WebPaParameter();
	WebPaParameter webPaParam5 = new WebPaParameter();
	WebPaParameter webPaParam6 = new WebPaParameter();
	WebPaParameter webPaParam7 = new WebPaParameter();
	WebPaParameter webPaParam8 = new WebPaParameter();
	WebPaParameter webPaParam9 = new WebPaParameter();
	WebPaParameter webPaParam10 = new WebPaParameter();

	webPaParameters.add(webPaParam1);
	webPaParameters.add(webPaParam2);
	webPaParameters.add(webPaParam3);
	webPaParameters.add(webPaParam4);
	webPaParameters.add(webPaParam5);
	webPaParameters.add(webPaParam6);
	webPaParameters.add(webPaParam7);
	webPaParameters.add(webPaParam8);
	webPaParameters.add(webPaParam9);
	webPaParameters.add(webPaParam10);

	for (int count = 0; count < webPaParameters.size(); count++) {
	    webPaParameters.get(count).setName(setParametersTen[count]);
	    webPaParameters.get(count).setDataType(dataTypeValueTen[count]);
	}
	switch (iteration) {
	case 0:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationOne[count]);
	    }
	    break;
	case 1:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationTwo[count]);
	    }
	    break;
	case 2:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationThree[count]);
	    }
	    break;
	case 3:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationFour[count]);
	    }
	    break;
	case 4:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationFive[count]);
	    }
	    break;
	case 5:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationSix[count]);
	    }
	    break;
	case 6:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationSeven[count]);
	    }
	    break;
	case 7:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationEight[count]);
	    }
	    break;
	case 8:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationNine[count]);
	    }
	    break;
	case 9:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationTen[count]);
	    }
	    break;

	default:
	    break;
	}
	return webPaParameters;
    }
    
    /**
     * 
     * Method to form webpa set with ten parameter with values on iteration values For DSL device
     * 
     * @param iteration
     * @return WebPaParameter object
     */
    public static List<WebPaParameter> webpaSetTenParametersForDSL(Integer iteration) {

	String[] setParametersTen = new String[10];
	setParametersTen[0] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_BEACON_INTERVAL;
	setParametersTen[1] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_SELFHEAL_MAXREBOOT;
	setParametersTen[2] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_SELFHEAL_PING_WAITTIME;
	setParametersTen[3] = BroadBandWebPaConstants.WEBPA_PARAMETER_FOR_SELFHEAL_MAXRESET_COUNT;
	setParametersTen[4] = BroadBandWebPaConstants.WEBPA_PARAM_2_4GHZ_WIFI_ACCESSPOINT_ALIAS;
	setParametersTen[5] = BroadBandWebPaConstants.WEBPA_PARAM_5GHZ_WIFI_ACCESSPOINT_ALIAS;
	setParametersTen[6] = BroadBandWebPaConstants.WEBPA_PARAM_2_4GHZ_WIFI_ACCESSPOINT_RETRY_LIMIT;
	setParametersTen[7] = BroadBandWebPaConstants.WEBPA_PARAM_5GHZ_WIFI_ACCESSPOINT_RETRY_LIMIT;
	setParametersTen[8] = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_NAME;
	setParametersTen[9] = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_NAME;

	Integer[] dataTypeValueTen = { WebPaDataTypes.INTEGER.getValue(), WebPaDataTypes.INTEGER.getValue(),
		WebPaDataTypes.INTEGER.getValue(), WebPaDataTypes.INTEGER.getValue(), WebPaDataTypes.STRING.getValue(),
		WebPaDataTypes.STRING.getValue(), WebPaDataTypes.INTEGER.getValue(), WebPaDataTypes.INTEGER.getValue(),
		WebPaDataTypes.STRING.getValue(), WebPaDataTypes.STRING.getValue() };

	String[] setValuesForIterationOne = { BroadBandTestConstants.STRING_VALUE_10,
		BroadBandTestConstants.STRING_VALUE_10, BroadBandTestConstants.STRING_VALUE_10,
		BroadBandTestConstants.STRING_VALUE_4,
		BroadBandTestConstants.STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS1,
		BroadBandTestConstants.STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS1, BroadBandTestConstants.STRING_VALUE_8,
		BroadBandTestConstants.STRING_VALUE_8, BroadBandTestConstants.STRING_VALUE_24GHZ_SSID1,
		BroadBandTestConstants.STRING_VALUE_5GHZ_SSID1 };
	String[] setValuesForIterationTwo = { BroadBandTestConstants.STRING_VALUE_20,
		BroadBandTestConstants.STRING_VALUE_20, BroadBandTestConstants.STRING_VALUE_20,
		BroadBandTestConstants.STRING_VALUE_5,
		BroadBandTestConstants.STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS2,
		BroadBandTestConstants.STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS2, BroadBandTestConstants.STRING_VALUE_9,
		BroadBandTestConstants.STRING_VALUE_9, BroadBandTestConstants.STRING_VALUE_24GHZ_SSID2,
		BroadBandTestConstants.STRING_VALUE_5GHZ_SSID2 };
	String[] setValuesForIterationThree = { BroadBandTestConstants.STRING_VALUE_30,
		BroadBandTestConstants.STRING_VALUE_30, BroadBandTestConstants.STRING_VALUE_30,
		BroadBandTestConstants.STRING_VALUE_6,
		BroadBandTestConstants.STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS3,
		BroadBandTestConstants.STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS3,
		BroadBandTestConstants.STRING_VALUE_10, BroadBandTestConstants.STRING_VALUE_10,
		BroadBandTestConstants.STRING_VALUE_24GHZ_SSID3, BroadBandTestConstants.STRING_VALUE_5GHZ_SSID3 };
	String[] setValuesForIterationFour = { BroadBandTestConstants.STRING_VALUE_40,
		BroadBandTestConstants.STRING_VALUE_40, BroadBandTestConstants.STRING_VALUE_40,
		BroadBandTestConstants.STRING_VALUE_7,
		BroadBandTestConstants.STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS4,
		BroadBandTestConstants.STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS4,
		BroadBandTestConstants.STRING_VALUE_11, BroadBandTestConstants.STRING_VALUE_11,
		BroadBandTestConstants.STRING_VALUE_24GHZ_SSID4, BroadBandTestConstants.STRING_VALUE_5GHZ_SSID4 };
	String[] setValuesForIterationFive = { BroadBandTestConstants.STRING_VALUE_50,
		BroadBandTestConstants.STRING_VALUE_50, BroadBandTestConstants.STRING_VALUE_50,
		BroadBandTestConstants.STRING_VALUE_8,
		BroadBandTestConstants.STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS5,
		BroadBandTestConstants.STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS5,
		BroadBandTestConstants.STRING_VALUE_12, BroadBandTestConstants.STRING_VALUE_12,
		BroadBandTestConstants.STRING_VALUE_24GHZ_SSID5, BroadBandTestConstants.STRING_VALUE_5GHZ_SSID5 };
	String[] setValuesForIterationSix = { BroadBandTestConstants.STRING_VALUE_60,
		BroadBandTestConstants.STRING_VALUE_10, BroadBandTestConstants.STRING_VALUE_60,
		BroadBandTestConstants.STRING_VALUE_9,
		BroadBandTestConstants.STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS6,
		BroadBandTestConstants.STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS6,
		BroadBandTestConstants.STRING_VALUE_13, BroadBandTestConstants.STRING_VALUE_13,
		BroadBandTestConstants.STRING_VALUE_24GHZ_SSID6, BroadBandTestConstants.STRING_VALUE_5GHZ_SSID6 };
	String[] setValuesForIterationSeven = { BroadBandTestConstants.STRING_VALUE_70,
		BroadBandTestConstants.STRING_VALUE_70, BroadBandTestConstants.STRING_VALUE_70,
		BroadBandTestConstants.STRING_VALUE_10,
		BroadBandTestConstants.STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS7,
		BroadBandTestConstants.STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS7,
		BroadBandTestConstants.STRING_VALUE_14, BroadBandTestConstants.STRING_VALUE_14,
		BroadBandTestConstants.STRING_VALUE_24GHZ_SSID7, BroadBandTestConstants.STRING_VALUE_5GHZ_SSID7 };
	String[] setValuesForIterationEight = { BroadBandTestConstants.STRING_VALUE_80,
		BroadBandTestConstants.STRING_VALUE_80, BroadBandTestConstants.STRING_VALUE_80,
		BroadBandTestConstants.STRING_VALUE_11,
		BroadBandTestConstants.STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS8,
		BroadBandTestConstants.STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS8,
		BroadBandTestConstants.STRING_VALUE_15, BroadBandTestConstants.STRING_VALUE_15,
		BroadBandTestConstants.STRING_VALUE_24GHZ_SSID8, BroadBandTestConstants.STRING_VALUE_5GHZ_SSID8 };
	String[] setValuesForIterationNine = { BroadBandTestConstants.STRING_VALUE_90,
		BroadBandTestConstants.STRING_VALUE_90, BroadBandTestConstants.STRING_VALUE_90,
		BroadBandTestConstants.STRING_VALUE_12,
		BroadBandTestConstants.STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS9,
		BroadBandTestConstants.STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS9,
		BroadBandTestConstants.STRING_VALUE_16, BroadBandTestConstants.STRING_VALUE_16,
		BroadBandTestConstants.STRING_VALUE_24GHZ_SSID9, BroadBandTestConstants.STRING_VALUE_5GHZ_SSID9 };
	String[] setValuesForIterationTen = { BroadBandTestConstants.STRING_VALUE_100,
		BroadBandTestConstants.STRING_VALUE_100, BroadBandTestConstants.STRING_VALUE_100,
		BroadBandTestConstants.STRING_VALUE_13,
		BroadBandTestConstants.STRING_VALUE_2_4GHZ_WIFI_ACCESSPOINT_ALIAS10,
		BroadBandTestConstants.STRING_VALUE_5GHZ_WIFI_ACCESSPOINT_ALIAS10,
		BroadBandTestConstants.STRING_VALUE_17, BroadBandTestConstants.STRING_VALUE_17,
		BroadBandTestConstants.STRING_VALUE_24GHZ_SSID10, BroadBandTestConstants.STRING_VALUE_5GHZ_SSID10 };

	List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
	WebPaParameter webPaParam1 = new WebPaParameter();
	WebPaParameter webPaParam2 = new WebPaParameter();
	WebPaParameter webPaParam3 = new WebPaParameter();
	WebPaParameter webPaParam4 = new WebPaParameter();
	WebPaParameter webPaParam5 = new WebPaParameter();
	WebPaParameter webPaParam6 = new WebPaParameter();
	WebPaParameter webPaParam7 = new WebPaParameter();
	WebPaParameter webPaParam8 = new WebPaParameter();
	WebPaParameter webPaParam9 = new WebPaParameter();
	WebPaParameter webPaParam10 = new WebPaParameter();

	webPaParameters.add(webPaParam1);
	webPaParameters.add(webPaParam2);
	webPaParameters.add(webPaParam3);
	webPaParameters.add(webPaParam4);
	webPaParameters.add(webPaParam5);
	webPaParameters.add(webPaParam6);
	webPaParameters.add(webPaParam7);
	webPaParameters.add(webPaParam8);
	webPaParameters.add(webPaParam9);
	webPaParameters.add(webPaParam10);

	for (int count = 0; count < webPaParameters.size(); count++) {
	    webPaParameters.get(count).setName(setParametersTen[count]);
	    webPaParameters.get(count).setDataType(dataTypeValueTen[count]);
	}
	switch (iteration) {
	case 0:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationOne[count]);
	    }
	    break;
	case 1:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationTwo[count]);
	    }
	    break;
	case 2:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationThree[count]);
	    }
	    break;
	case 3:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationFour[count]);
	    }
	    break;
	case 4:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationFive[count]);
	    }
	    break;
	case 5:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationSix[count]);
	    }
	    break;
	case 6:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationSeven[count]);
	    }
	    break;
	case 7:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationEight[count]);
	    }
	    break;
	case 8:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationNine[count]);
	    }
	    break;
	case 9:
	    for (int count = 0; count < webPaParameters.size(); count++) {
		webPaParameters.get(count).setValue(setValuesForIterationTen[count]);
	    }
	    break;

	default:
	    break;
	}
	return webPaParameters;
    }
    

    /**
     * Utility method to retrieve the CMTS MAC address using WebPA parameter Device.DeviceInfo.X_RDKCENTRAL-COM_CMTS_MAC
     * and verify with MAC Address retrieved using Arp table.
     * 
     * @param device
     *            The Dut to be validated.
     * @param tapEnv
     *            The {@link AutomaticsTapApi} reference.
     * @return CMTS Mac Address where this particular device connected.
     * @author Govardhan
     */
    public static String getCmtsMacAddressUsingWebPaCommand(Dut device, AutomaticsTapApi tapEnv) {

	String webPaCmtsMacAddress = tapEnv.executeWebPaCommand(device,
		BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_INFO_RDK_CENTRAL_CMTS_MAC);

	return webPaCmtsMacAddress;
    }
    
    /**
     * Helper method to get the WebPa value and compare with given SNMP value in polling duration
     * 
     * @param device
     *            {@link device}
     * @param tapEnv
     *            {@link tapEnv}
     * @param webPaParam
     *            WebPa parameter name
     * @param snmpValueToBeMatched
     *            value to be matched with the WEBPA response
     * @param maxDuration
     *            maximum polling duration
     * @param pollDuration
     *            wait time for each iteration
     * @return BroadBandResultObject
     * 
     */
    public static BroadBandResultObject getWebpaValueAndVerifySnmpValueInPolledDuration(Dut device,
	    AutomaticsTapApi tapEnv, String webPaParam, String snmpValueToBeMatched, long maxDuration,
	    long pollDuration, boolean isMacComparisionRequired) {
	LOGGER.debug("STARTING METHOD: getWebpaValueAndVerifySnmpValueInPolledDuration");
	boolean status = false;
	String valueRetrievedViaWebpa = null;
	String errorMessage = null;
	BroadBandResultObject broadBandResultObject = new BroadBandResultObject();
	long startTime = System.currentTimeMillis();
	do {
	    valueRetrievedViaWebpa = tapEnv.executeWebPaCommand(device, webPaParam);
	    LOGGER.info("Value Obtained via WEBPA Command is :" + valueRetrievedViaWebpa);
	    errorMessage = "Unable to Obtain value via WEBPA or the value obtained via webpa is null.";
	    if (CommonMethods.isNotNull(valueRetrievedViaWebpa)) {
		errorMessage = "Value Retrieved from WEBPA doesn't match with the Value Retrived from SNMP!";
		if (isMacComparisionRequired) {
		    status = CommonUtils.patternSearchFromTargetString(
			    BroadBandCommonUtils.formatIpOrMacWithoutLeadingZeros(valueRetrievedViaWebpa).toUpperCase(),
			    BroadBandCommonUtils.formatIpOrMacWithoutLeadingZeros(snmpValueToBeMatched).toUpperCase());
		} else {
		    status = CommonUtils.patternSearchFromTargetString(snmpValueToBeMatched.toUpperCase(),
			    valueRetrievedViaWebpa.toUpperCase())
			    || CommonUtils.patternSearchFromTargetString(valueRetrievedViaWebpa.toUpperCase(),
				    snmpValueToBeMatched.toUpperCase());
		}

	    }
	} while ((System.currentTimeMillis() - startTime) < maxDuration && !status
		&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, pollDuration));
	LOGGER.debug("ENDING METHOD: getWebpaValueAndVerifySnmpValueInPolledDuration");
	broadBandResultObject.setErrorMessage(errorMessage);
	broadBandResultObject.setStatus(status);
	return broadBandResultObject;
    }
    
    /**
     * Utility method to retrieve all Device Status related WebPA parameters based on supported models.
     * 
     * @param settop
     *            The device to be validated.
     * @return array of WebPA parameters used for fetching the device status.
     * @refactor Govardhan
     */
    public static String[] retrieveDeviceStatusUsingWebPaParameterForDSLDevices(Dut device, boolean isCiquickTest) {
	List<String> parameters = new ArrayList<String>();

	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_FINGER_PRINT_ENABLE);
	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_AKER_ENABLE);
	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_TO_GET_XDNS_FEATURE_STATUS);
	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_CLOUD_UI);
	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_FOR_SYNDICATION_PARTNER_ID);
	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_2_4GHZ_PRIVATE_SSID);
	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_5GHZ_PRIVATE_SSID);
	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_BRIDGE_MODE_STATUS);
	parameters.add(BroadBandWebPaConstants.WEBPA_WAREHOUSE_WIFI_2G_CHANNEL);
	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_2_4_GHZ_CHANNEL_SELECTION_MODE);
	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_5_GHZ_CHANNEL_SELECTION_MODE);
	parameters.add(BroadBandWebPaConstants.WEBPA_WAREHOUSE_WIFI_5G_CHANNEL);
	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_CODEBIG_FIRST_ENABLE);
	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_MESH_STATUS);
	if (isCiquickTest) {
	    parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_WPS_STATUS_2_4);
	    parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_WPS_STATUS_5);
	}

	String[] parametersArray = new String[parameters.size()];
	parametersArray = parameters.toArray(parametersArray);

	return parametersArray;
    }
    
    /**
     * Utility method to retrieve all Device Status related WebPA parameters based on supported models.
     * 
     * @param device
     *            The Dut to be validated.
     * @return array of WebPA parameters used for fetching the device status.
     * @refactor Govardhan
     */
    public static String[] retrieveDeviceStatusUsingWebPaParameterBasedOnSupportedModels(Dut device, boolean isCiquickTest) {
	List<String> parameters = new ArrayList<String>();

	if (!isCiquickTest) {
		parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_FINGER_PRINT_ENABLE);
		parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_MESH_STATUS);
		parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_AKER_ENABLE);
		parameters.add("Device.WiFi.SSID.10105.Status");
	} else {
	    parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_RDKCENTRAL_AKER_ENABLE);
	    parameters.add("Device.WiFi.SSID.10105.Status");
	}
	parameters.add("Device.WiFi.SSID.10003.Status");

	parameters.add("Device.WiFi.SSID.10103.Status");

	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_FOR_MOCA_INTERFACE_ENABLE);
	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_GET_MOCA_STATUS);
	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_TO_GET_XDNS_FEATURE_STATUS);
	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_CLOUD_UI);

	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_2_4GHZ_PRIVATE_SSID);
	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_SSID_STATUS_FOR_5GHZ_PRIVATE_SSID);
	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_BRIDGE_MODE_STATUS);

	parameters.add(BroadBandWebPaConstants.WEBPA_WAREHOUSE_WIFI_2G_CHANNEL);
	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_2_4_GHZ_CHANNEL_SELECTION_MODE);
	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_5_GHZ_CHANNEL_SELECTION_MODE);
	parameters.add(BroadBandWebPaConstants.WEBPA_WAREHOUSE_WIFI_5G_CHANNEL);

	parameters.add(BroadBandWebPaConstants.WEBPA_PARAM_CODEBIG_FIRST_ENABLE);

	String[] parametersArray = new String[parameters.size()];
	parametersArray = parameters.toArray(parametersArray);

	return parametersArray;
    }

    /**
     * Utility method to check whether SSID is enabled for a given parameter. If not enabled, this method will enable
     * the SSID
     * 
     * @param device
     *            The Dut to be validated
     * @param tapEnv
     *            {@link AutomaticsTapApi} Reference
     * @param parameter
     *            parameter to be enabled
     * @return Returns true if enabled successfully
     * @author Alan_Bivera
     */
    public static boolean checkAndEnableSSIDForGivenInterface(Dut device, AutomaticsTapApi tapEnv, String parameter) {
	LOGGER.debug("ENTERING METHOD : checkAndEnableSSIDForGivenInterface");
	Boolean status = false;
	String webPaGetResponse = tapEnv.executeWebPaCommand(device, parameter);
	if (CommonMethods.isNotNull(webPaGetResponse)
		&& !webPaGetResponse.equalsIgnoreCase(BroadBandTestConstants.TRUE)) {
	    WebPaParameter webPaParam = new WebPaParameter();
	    webPaParam.setDataType(BroadBandTestConstants.CONSTANT_3);
	    webPaParam.setName(parameter);
	    webPaParam.setValue(BroadBandTestConstants.TRUE);
	    status = BroadBandCommonUtils.setWebPaParam(tapEnv, device, webPaParam);
	} else if (CommonMethods.isNotNull(webPaGetResponse)
		&& webPaGetResponse.equalsIgnoreCase(BroadBandTestConstants.TRUE)) {
	    status = true;
	}

	LOGGER.debug("ENDING METHOD : checkAndEnableSSIDForGivenInterface");
	return status;
    }

    /**
     * Utility Method to fetch the interface name based on the device <interface name>
     * 
     * @param settop
     *            Set top to be used
     * @param tapEnv
     *            {@link AutomaticsTapApi} Reference
     * @param band
     *            wifi band
     * @param wifiType
     *            Type of wifi
     * @param parameter
     *            of wifi either Name or MAC address or Bssid
     * @return Returns the interface of the device
     * @Refactor Alan_Bivera
     */

    public static String getWiFiInterface(Dut device, AutomaticsTapApi tapEnv, String band, String wifiType,
	    String parameter) {
	LOGGER.debug("STARTING METHOD : getWiFiInterface()");
	String interfaceName = null;
	
	try {
	    
	    if (parameter.equalsIgnoreCase(BroadBandTestConstants.SSID_PARAM)) {
		
		if(wifiType.equalsIgnoreCase(BroadBandTestConstants.PRIVATE_WIFI_TYPE)
			&& (band.equalsIgnoreCase(BroadBandTestConstants.BAND_5GHZ))) {
		    interfaceName = BroadBandCommonUtils.getAutomaticsPropsValueByResolvingPlatform(device,
			    BroadBandTestConstants.PROP_KEY_WIFI_PRIVATE_5GHZ_INTERFACE_NAME_BY_SSID);	
		    
		}else if(wifiType.equalsIgnoreCase(BroadBandTestConstants.PRIVATE_WIFI_TYPE)
			&& (band.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ))) {
		    interfaceName = BroadBandCommonUtils.getAutomaticsPropsValueByResolvingPlatform(device,
			    BroadBandTestConstants.PROP_KEY_WIFI_PRIVATE_2GHZ_INTERFACE_NAME_BY_SSID);	
		    
		}else if(wifiType.equalsIgnoreCase(BroadBandTestConstants.PUBLIC_WIFI_TYPE)
			&& (band.equalsIgnoreCase(BroadBandTestConstants.BAND_5GHZ))) {
		    interfaceName = BroadBandCommonUtils.getAutomaticsPropsValueByResolvingPlatform(device,
			    BroadBandTestConstants.PROP_KEY_WIFI_PUBLIC_5GHZ_INTERFACE_NAME_BY_SSID);	
		    
		}else if(wifiType.equalsIgnoreCase(BroadBandTestConstants.PUBLIC_WIFI_TYPE)
			&& (band.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ))) {
		    interfaceName = BroadBandCommonUtils.getAutomaticsPropsValueByResolvingPlatform(device,
			    BroadBandTestConstants.PROP_KEY_WIFI_PUBLIC_2GHZ_INTERFACE_NAME_BY_SSID);	
		    
		}
	    }else if (parameter.equalsIgnoreCase(BroadBandTestConstants.BSSID_PARAM)) {
		
		if(wifiType.equalsIgnoreCase(BroadBandTestConstants.PRIVATE_WIFI_TYPE)
			&& (band.equalsIgnoreCase(BroadBandTestConstants.BAND_5GHZ))) {
		    interfaceName = BroadBandCommonUtils.getAutomaticsPropsValueByResolvingPlatform(device,
			    BroadBandTestConstants.PROP_KEY_WIFI_PRIVATE_5GHZ_INTERFACE_NAME_BY_BSSID);	
		    
		}else if(wifiType.equalsIgnoreCase(BroadBandTestConstants.PRIVATE_WIFI_TYPE)
			&& (band.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ))) {
		    interfaceName = BroadBandCommonUtils.getAutomaticsPropsValueByResolvingPlatform(device,
			    BroadBandTestConstants.PROP_KEY_WIFI_PRIVATE_2GHZ_INTERFACE_NAME_BY_BSSID);	
		    
		}else if(wifiType.equalsIgnoreCase(BroadBandTestConstants.PUBLIC_WIFI_TYPE)
			&& (band.equalsIgnoreCase(BroadBandTestConstants.BAND_5GHZ))) {
		    interfaceName = BroadBandCommonUtils.getAutomaticsPropsValueByResolvingPlatform(device,
			    BroadBandTestConstants.PROP_KEY_WIFI_PUBLIC_5GHZ_INTERFACE_NAME_BY_BSSID);	
		    
		}else if(wifiType.equalsIgnoreCase(BroadBandTestConstants.PUBLIC_WIFI_TYPE)
			&& (band.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ))) {
		    interfaceName = BroadBandCommonUtils.getAutomaticsPropsValueByResolvingPlatform(device,
			    BroadBandTestConstants.PROP_KEY_WIFI_PUBLIC_2GHZ_INTERFACE_NAME_BY_BSSID);	
		    
		}
	    }else if (parameter.equalsIgnoreCase(BroadBandTestConstants.MAC_PARAM)) {
		
		if(wifiType.equalsIgnoreCase(BroadBandTestConstants.PRIVATE_WIFI_TYPE)
			&& (band.equalsIgnoreCase(BroadBandTestConstants.BAND_5GHZ))) {
		    interfaceName = BroadBandCommonUtils.getAutomaticsPropsValueByResolvingPlatform(device,
			    BroadBandTestConstants.PROP_KEY_WIFI_PRIVATE_5GHZ_INTERFACE_NAME_BY_MAC);	
		    
		}else if(wifiType.equalsIgnoreCase(BroadBandTestConstants.PRIVATE_WIFI_TYPE)
			&& (band.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ))) {
		    interfaceName = BroadBandCommonUtils.getAutomaticsPropsValueByResolvingPlatform(device,
			    BroadBandTestConstants.PROP_KEY_WIFI_PRIVATE_2GHZ_INTERFACE_NAME_BY_MAC);	
		    
		}else if(wifiType.equalsIgnoreCase(BroadBandTestConstants.PUBLIC_WIFI_TYPE)
			&& (band.equalsIgnoreCase(BroadBandTestConstants.BAND_5GHZ))) {
		    interfaceName = BroadBandCommonUtils.getAutomaticsPropsValueByResolvingPlatform(device,
			    BroadBandTestConstants.PROP_KEY_WIFI_PUBLIC_5GHZ_INTERFACE_NAME_BY_MAC);	
		    
		}else if(wifiType.equalsIgnoreCase(BroadBandTestConstants.PUBLIC_WIFI_TYPE)
			&& (band.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ))) {
		    interfaceName = BroadBandCommonUtils.getAutomaticsPropsValueByResolvingPlatform(device,
			    BroadBandTestConstants.PROP_KEY_WIFI_PUBLIC_2GHZ_INTERFACE_NAME_BY_MAC);	
		    
		}
	    }else {
		
		LOGGER.info("Failed to fetch Wifi interface name. Wifi parameter not supported.");
	    }
	    
	    
	} catch (Exception e) {
		LOGGER.info("No platform dependent extensions are mentioned in  automatics properties");

	}
	LOGGER.debug("ENDING METHOD : getWiFiInterface()");
	return interfaceName;
    }
    
    /**
     * Utility Method to get the SSID from the device side <interface name>
     * 
     * @param settop
     *            Set top to be used
     * @param tapEnv
     *            {@link AutomaticsTapApi} Reference
     * @param interfaceName
     *            Interface name to get the SSID
     * @return Retruns the SSID Name of the particular interface given
     * @Refactor Alan_Bivera
     */
    public static String getSsidNameRetrievedUsingDeviceCommand(Dut device, AutomaticsTapApi tapApi,
	    String interfaceName) {
	LOGGER.debug("STARTING METHOD : getSsidNameRetrievedUsingDeviceCommand()");
	String webPaResponse = null;
	String ssidName = null;
	String command = null;
	if (CommonMethods.isAtomSyncAvailable(device, tapApi)) {
	    command = BroadBandTestConstants.CMD_IW_CONFIG + " " + interfaceName;
	    webPaResponse = CommonMethods.executeCommandInAtomConsole(device, tapApi, command);
	    if (CommonMethods.isNotNull(webPaResponse)
		    && (!webPaResponse.contains(BroadBandTestConstants.NOSUCHDEVICES))) {
		webPaResponse = webPaResponse.replace(BroadBandTestConstants.TEXT_DOUBLE_QUOTE, BroadBandTestConstants.EMPTY_STRING);
		ssidName = CommonMethods.patternFinder(webPaResponse, BroadBandTestConstants.CMD_TO_GREP_SSID_ATOM);
		ssidName = CommonMethods.isNotNull(ssidName) ? ssidName.trim() : null;
		LOGGER.info("ssidName is" + ssidName);
	    } else if (webPaResponse.contains(BroadBandTestConstants.NOSUCHDEVICES)) {
		LOGGER.info("No such devices are found");
		ssidName = BroadBandTestConstants.NOSUCHDEVICES;
	    }

	} else if (DeviceModeHandler.isFibreDevice(device) || DeviceModeHandler.isDSLDevice(device)) {
	    webPaResponse = tapApi.executeCommandUsingSsh(device, interfaceName);
	    if (CommonMethods.isNotNull(webPaResponse)) {
		webPaResponse = webPaResponse.replace(BroadBandTestConstants.TEXT_DOUBLE_QUOTE, BroadBandTestConstants.EMPTY_STRING);
		ssidName = CommonMethods.patternFinder(webPaResponse, BroadBandTestConstants.CMD_TO_GREP_SSID_FIBER);
		ssidName = CommonMethods.isNotNull(ssidName) ? ssidName.trim() : null;
		LOGGER.info("ssidName is" + ssidName);
	    }
	}else{
	    String sbinPath = null;	
	    try {
		    sbinPath = BroadBandCommonUtils.getAutomaticsPropsValueByResolvingPlatform(device,
			    BroadBandTestConstants.PROP_KEY_USER_SBIN_PATH);
		} catch (Exception e) {
		    ssidName = null;
		    sbinPath = null;
		    LOGGER.info("No platform dependent property mentioned in automatics properties");
		}	    
	    interfaceName = CommonMethods.isNotNull(sbinPath) ? sbinPath + interfaceName : interfaceName ;
	    //interfaceName = BroadBandTestConstants.STRING_PATH_USR_SBIN + interfaceName;
	    ssidName = tapApi.executeCommandUsingSsh(device, interfaceName);
	    ssidName = CommonMethods.isNotNull(ssidName) ? ssidName.trim() : null;
	} 
	if (CommonMethods.isNotNull(ssidName)) {
	    return ssidName;
	} else {
	    LOGGER.info("getting the  SSID Name  Inside The Device - FAILED");
	}
	LOGGER.debug("ENDING METHOD : getSsidNameRetrievedUsingDeviceCommand");
	return ssidName;
    }
 
    /**
     * Utility Method to enable the public Wifi
     * 
     * @param device
     *            The Dut to be validated
     * @param tapEnv
     *            {@link AutomaticsTapApi} Reference
     * @return Returns true if public Wifi is enabled.
     * 
     * @author Alan_Bivera
     */

    public static boolean settingWebpaparametersForPublicWifi(Dut device, AutomaticsTapApi tapEnv, String parameter) {

	LOGGER.debug("ENTERING METHOD : settingWebpaparametersForPublicWifi");
	boolean status = false;
	boolean isDscpSet = false;
	boolean isPrimaryRemoteEndpointSet = false;
	boolean isSecondaryRemoteEndpointSet = false;
	boolean isPublicWifiSsidSet = false;
	boolean isPublicWifiParameterEnabled = false;
	boolean isPublicWifiEnabled = false;
	// Setting the Dscp marker
	isDscpSet = BroadBandWiFiUtils.setWebPaParams(device, BroadBandWebPaConstants.WEBPA_PARAM_DSCP_MARK_POLICY,
		BroadBandTestConstants.DSCP_MARK_POLICY, BroadBandTestConstants.INCERMENTAL_ONE);
	LOGGER.info("setting Dscp Mark policy is-" + isDscpSet);
	if (!isDscpSet) {
	    LOGGER.error("SETTING DSCP MARK POLICY FAILED SO PUBLIC WIFI IS NOT CONFIGURED ");
	    return status;
	}

	// Setting the primary tunnel Endpoint
	isPrimaryRemoteEndpointSet = BroadBandWiFiUtils.setWebPaParams(device,
		BroadBandWebPaConstants.WEBPA_PARAM_PRIMARY_REMOTE_ENDPOINT,
		BroadBandTestConstants.PRIMARY_REMOTE_ENDPOINT, BroadBandTestConstants.CONSTANT_0);
	LOGGER.info("SETTING PRIMARY_REMOTE_ENDPOINT IS-" + isPrimaryRemoteEndpointSet);
	if (!isPrimaryRemoteEndpointSet) {
	    LOGGER.error("SETTING PRIMARY_REMOTE_ENDPOINT FAILED SO PUBLIC WIFI IS NOT CONFIGURED");
	    return status;
	}

	// Setting the secondary tunnel Endpoint
	isSecondaryRemoteEndpointSet = BroadBandWiFiUtils.setWebPaParams(device,
		BroadBandWebPaConstants.WEBPA_PARAM_SECONDARY_REMOTE_ENDPOINT,
		BroadBandTestConstants.SECONDARY_REMOTE_ENDPOINT, BroadBandTestConstants.CONSTANT_0);
	LOGGER.info("SETTING SECONDARY REMOTE ENDPOINT IS-" + isSecondaryRemoteEndpointSet);
	if (!isSecondaryRemoteEndpointSet) {
	    LOGGER.error("SETTING SECONDARY REMOTE ENDPOINT FAILED SO PUBLIC WIFI IS NOT CONFIGURED");
	    return status;
	}

	// Setting the public wifi ssid name
	String errorMessage = null;
	if (parameter
		.equalsIgnoreCase(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PUBLIC_SSID_ENABLE_STATUS)) {
	    isPublicWifiSsidSet = BroadBandWiFiUtils.setWebPaParams(device,
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PUBLIC_SSID,
		    BroadBandTestConstants.PUBLIC_WIFI_SSID_5, BroadBandTestConstants.CONSTANT_0);
	    LOGGER.info("Setting the Public Wifi 5GHZ SSID name -" + isPublicWifiSsidSet);
	} else if (parameter
		.equalsIgnoreCase(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PUBLIC_SSID_ENABLE_STATUS)) {
	    isPublicWifiSsidSet = BroadBandWiFiUtils.setWebPaParams(device,
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PUBLIC_SSID,
		    BroadBandTestConstants.PUBLIC_WIFI_SSID_2, BroadBandTestConstants.CONSTANT_0);
	    LOGGER.info("Setting the Public Wifi 2.4GHZ SSID name -" + isPublicWifiSsidSet);
	} else if (parameter.equalsIgnoreCase(BroadBandTestConstants.DUAL_BAND)) {
	    errorMessage = "Failed to set Public Wifi 2.4 Ghz using webpa parameter "
		    + BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PUBLIC_SSID;
	    isPublicWifiSsidSet = BroadBandWiFiUtils.setWebPaParams(device,
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PUBLIC_SSID,
		    BroadBandTestConstants.PUBLIC_WIFI_SSID_2, BroadBandTestConstants.CONSTANT_0);
	    LOGGER.info("Setting the Public Wifi 2.4GHZ SSID name -" + isPublicWifiSsidSet);
	    if (isPublicWifiSsidSet) {
		errorMessage = "Failed to set Public Wifi 2.4 Ghz using webpa parameter "
			+ BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PUBLIC_SSID;
		isPublicWifiSsidSet = BroadBandWiFiUtils.setWebPaParams(device,
			BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PUBLIC_SSID,
			BroadBandTestConstants.PUBLIC_WIFI_SSID_5, BroadBandTestConstants.CONSTANT_0);
		LOGGER.info("Setting the Public Wifi 5GHZ SSID name -" + isPublicWifiSsidSet);
	    }
	}
	if (!isPublicWifiSsidSet) {
	    LOGGER.info("Waiting 90 sec to perform next action with out error");
	    tapEnv.waitTill(BroadBandTestConstants.NINTY_SECOND_IN_MILLIS);
	    LOGGER.error("SETTING PUBLIC WIFI SSID NAME FAILED SO PUBLIC WIFI IS NOT CONFIGURED. ERROR MESSAGE - "
		    + errorMessage);
	    return status;
	}

	// wait for two minutes
	LOGGER.info("Waiting in set public wifi");
	tapEnv.waitTill(RDKBTestConstants.TWO_MINUTES);

	// Setting the public wifi parameter to true
	String[] parameters = { BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PUBLIC_SSID_ENABLE_STATUS,
		BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PUBLIC_SSID_ENABLE_STATUS };
	String webPaGetResponse = null;
	int counter = 0;
	for (counter = 0; counter < parameters.length; counter++) {
	    if (parameter.equals(BroadBandTestConstants.DUAL_BAND) || parameter.equals(parameters[counter])) {
		webPaGetResponse = tapEnv.executeWebPaCommand(device, parameters[counter]);
		if (CommonMethods.isNotNull(webPaGetResponse)
			&& webPaGetResponse.equalsIgnoreCase(BroadBandTestConstants.TRUE)) {
		    isPublicWifiParameterEnabled = true;
		} else {
		    isPublicWifiParameterEnabled = BroadBandWiFiUtils.setWebPaParams(device, parameters[counter],
			    BroadBandTestConstants.TRUE, BroadBandTestConstants.INCERMENTAL_THREE);
		}
		if (!isPublicWifiParameterEnabled) {
		    errorMessage = "Failed to enable Public Wifi using webpa parameter " + parameters[counter];
		    break;
		}
	    }
	    LOGGER.info("Waiting 90 sec to perform next action with out error");
	    tapEnv.waitTill(BroadBandTestConstants.NINTY_SECOND_IN_MILLIS);
	}
	LOGGER.info("Setting public wifi parameter is-" + isPublicWifiParameterEnabled);

	if (!isPublicWifiParameterEnabled) {
	    LOGGER.error("SETTING PUBLIC WIFI PARAMETER FAILED SO PUBLIC WIFI IS NOT CONFIGURED. ERROR MESSAGE - "
		    + errorMessage);
	    return status;
	}
	// Enabling the wifi
	isPublicWifiEnabled = BroadBandWiFiUtils.setWebPaParams(device,
		BroadBandTestConstants.WEBPA_PARAM_ENABLING_PUBLIC_WIFI, BroadBandTestConstants.TRUE,
		BroadBandTestConstants.CONSTANT_3);
	LOGGER.info("ENABLING THE PUBLIC WIFI ON THIS DEVICE-" + isPublicWifiEnabled);
	if (!isPublicWifiEnabled) {
	    LOGGER.error("ENABLING PUBLIC WIFI FAILED SO PUBLIC WIFI IS NOT CONFIGURED");
	    return status;
	}

	status = isDscpSet && isPrimaryRemoteEndpointSet && isSecondaryRemoteEndpointSet && isPublicWifiSsidSet
		&& isPublicWifiParameterEnabled && isPublicWifiEnabled;
	LOGGER.debug("ENDING METHOD : settingWebpaparametersForPublicWifi");
	return status;
    }

    /**
     * Utility Method to get the MAC Address from the device side
     * 
     * 
     * @param settop
     *            Set top to be used
     * @param tapEnv
     *            {@link AutomaticsTapApi} Reference
     * @param interfaceName
     *            Interface name to get the MAC Address
     * @return Returns the MAC Addresss of the particular interface given
     * @Refactor Alan_Bivera
     */
    public static String getMacAddressRetrievedUsingDeviceCommand(Dut device, AutomaticsTapApi tapApi,
	    String interfaceName) {
	LOGGER.debug("STARTING METHOD : getMacAddressRetrievedUsingDeviceCommand()");
	String webPaResponse = null;
	String command = null;
	String macaddress = null;
	if (CommonMethods.isAtomSyncAvailable(device, tapApi)) {
	    command = BroadBandTestConstants.CMD_IW_CONFIG + " " + interfaceName;
	    LOGGER.info("command is" + command);
	    webPaResponse = CommonMethods.executeCommandInAtomConsole(device, tapApi, command);
	    LOGGER.info("webPaResponse is" + webPaResponse);
	    if (CommonMethods.isNotNull(webPaResponse)
		    && (!webPaResponse.contains(BroadBandTestConstants.NOSUCHDEVICES))) {

		macaddress = CommonMethods.patternFinder(webPaResponse,
			BroadBandTestConstants.CMD_TO_GREP_MAC_ADDRESS_ATOM);
		macaddress = CommonMethods.isNotNull(macaddress) ? macaddress.trim() : null;
		LOGGER.info("macaddress is" + macaddress);
	    } else if (webPaResponse.contains(BroadBandTestConstants.NOSUCHDEVICES)) {
		LOGGER.info("No such devices are found");
		macaddress = BroadBandTestConstants.NOSUCHDEVICES;
	    }
	} else if (DeviceModeHandler.isFibreDevice(device) || DeviceModeHandler.isDSLDevice(device)) {
	    webPaResponse = tapApi.executeCommandUsingSsh(device, interfaceName);
	    LOGGER.info("webPaResponse is" + webPaResponse);
	    if (CommonMethods.isNotNull(webPaResponse)) {
		macaddress = CommonMethods.patternFinder(webPaResponse,
			BroadBandTestConstants.CMD_TO_GREP_MAC_ADDRESS_FIBER);
		macaddress = CommonMethods.isNotNull(macaddress) ? macaddress.trim() : null;
		LOGGER.info("macaddress is" + macaddress);
	    }
	}else {
	    String sbinPath = null;	
	    try {
		    sbinPath = BroadBandCommonUtils.getAutomaticsPropsValueByResolvingPlatform(device,
			    BroadBandTestConstants.PROP_KEY_USER_SBIN_PATH);
		} catch (Exception e) {
		    macaddress = null;
		    sbinPath = null;
		    LOGGER.info("No platform dependent property mentioned in automatics properties");
		}	    
	    interfaceName = CommonMethods.isNotNull(sbinPath) ? sbinPath + interfaceName : interfaceName ;
	    macaddress = tapApi.executeCommandUsingSsh(device, interfaceName);
	    macaddress = CommonMethods.isNotNull(macaddress) ? macaddress.trim() : null;
	    LOGGER.info("macaddress is" + macaddress);

	}
	if (CommonMethods.isNotNull(macaddress)) {
	    return macaddress;
	} else {
	    LOGGER.info("getting  MAC Address From Inside The Device - FAILED");
	}
	LOGGER.debug("ENDING METHOD : getMacAddressRetrievedUsingDeviceCommand");
	return macaddress;
    }
    
    /**
     * Utility method to disable the SSID for the given parameter and verify the same from device side for "ssid enable"
     * parameters
     * 
     * @param settop
     *            Set top to be used
     * @param tapEnv
     *            {@link AutomaticsTapApi} Reference
     * @param interfaceName
     *            Interface name to get the MAC Address
     * @param parameter
     *            Wifi parameter to disable it.
     * @return Returns True if SSID is disabled properly
     * @Refactor Alan_Bivera
     */
    public static boolean disableSsidAndVerifyFromDevice(Dut device, AutomaticsTapApi tapApi, String interfaceName,
	    String parameter) {
	LOGGER.debug("ENTERING METHOD : disableSsidAndVerifyFromDevice");
	Boolean status = false;
	boolean webPaResonse = false;
	String output = null;
	webPaResonse = BroadBandWiFiUtils.setWebPaParams(device, parameter, BroadBandTestConstants.FALSE,
		BroadBandTestConstants.CONSTANT_3);
	if (webPaResonse) {
	    tapApi.waitTill(BroadBandTestConstants.THREE_MINUTE_IN_MILLIS);
	    output = getWifiNameFromDevice(device, tapApi, interfaceName);
	    status = CommonMethods.isNotNull(output) && (output.contains(BroadBandTestConstants.NOT_ASSOCIATED)
		    || output.contains(BroadBandTestConstants.INVALID_MAC_ADDRESS)
		    || output.contains(BroadBandTestConstants.OUTOFSERVICE)
		    || output.contains(BroadBandTestConstants.NOSUCHDEVICES));

	}
	LOGGER.debug("ENDING METHOD : disableSsidAndVerifyFromDevice");
	return status;
    }
    
    /**
     * Utility Method to get the Wifi Name from the device
     * 
     * 
     * @param settop
     *            Set top to be used
     * @param tapEnv
     *            {@link AutomaticsTapApi} Reference
     * @param interfaceName
     *            Interface name to get the Wifi Name
     * @return Returns the Wifi Name of the particular interface given
     * @Refactor Alan_Bivera
     */

    public static String getWifiNameFromDevice(Dut device, AutomaticsTapApi tapApi, String interfaceName) {
	LOGGER.debug("STARTING METHOD : getWifiNameFromDevice()");
	String webPaResponse = null;
	String command = null;
	String ssidName = null;
	if (CommonMethods.isAtomSyncAvailable(device, tapApi)) {
	    command = BroadBandTestConstants.CMD_IW_CONFIG + " " + interfaceName;
	    webPaResponse = CommonMethods.executeCommandInAtomConsole(device, tapApi, command);

	    if (CommonMethods.isNotNull(webPaResponse)
		    && (!webPaResponse.contains(BroadBandTestConstants.NOSUCHDEVICES))
		    && (!webPaResponse.contains(BroadBandTestConstants.OUTOFSERVICE))) {
		ssidName = CommonMethods.patternFinder(webPaResponse,
			BroadBandTestConstants.CMD_TO_GREP_MAC_ADDRESS_ATOM);

		ssidName = CommonMethods.isNotNull(ssidName) ? ssidName.trim() : null;
		LOGGER.info("Access Point is" + ssidName);
	    } else if (webPaResponse.contains(BroadBandTestConstants.NOSUCHDEVICES)) {
		LOGGER.info("No such devices were found");
		ssidName = webPaResponse;
	    }
	} else if (DeviceModeHandler.isFibreDevice(device) || DeviceModeHandler.isDSLDevice(device)) {
	    webPaResponse = tapApi.executeCommandUsingSsh(device, interfaceName);
	    if (CommonMethods.isNotNull(webPaResponse)) {
		webPaResponse = webPaResponse.replace(BroadBandTestConstants.TEXT_DOUBLE_QUOTE,
			BroadBandTestConstants.EMPTY_STRING);
		ssidName = CommonMethods.patternFinder(webPaResponse,
			BroadBandTestConstants.CMD_TO_GREP_MAC_ADDRESS_FIBER);
		ssidName = CommonMethods.isNotNull(ssidName) ? ssidName.trim() : null;
		LOGGER.info("Mac Address is: " + ssidName);
	    }
	} else {
	    String sbinPath = null;	
	    try {
		    sbinPath = BroadBandCommonUtils.getAutomaticsPropsValueByResolvingPlatform(device,
			    BroadBandTestConstants.PROP_KEY_USER_SBIN_PATH);
		} catch (Exception e) {
		    ssidName = null;
		    sbinPath = null;
		    LOGGER.info("No platform dependent property mentioned in automatics properties");
		}	    
	    interfaceName = CommonMethods.isNotNull(sbinPath) ? sbinPath + interfaceName : interfaceName ;
	    ssidName = tapApi.executeCommandUsingSsh(device, interfaceName);
	    ssidName = CommonMethods.isNotNull(ssidName) ? ssidName.trim() : null;
	    LOGGER.info("Mac Address is: " + ssidName);
	}
	
	if (CommonMethods.isNotNull(ssidName)) {
	    return ssidName;
	} else {
	    LOGGER.info("Getting MacAddress From Inside The Device - FAILED");
	}
	LOGGER.debug("ENDING METHOD : getWifiNameFromDevice");
	return ssidName;
    }

    /**
     * Utility method to enable the SSID for the given parameter and verify the same from device side for "ssid enable"
     * parameters
     * 
     * @param settop
     *            Set top to be used
     * @param tapEnv
     *            {@link AutomaticsTapApi} Reference
     * @param interfaceName
     *            Interface name to get the MAC Address
     * @param parameter
     *            Wifi parameter to enable it.
     * @return Retruns True if SSID is enabled properly
     * @Refactor Alan_Bivera
     */

    public static boolean enableSsidAndVerifyFromDevice(Dut device, AutomaticsTapApi tapApi, String interfaceName,
	    String parameter) {
	LOGGER.debug("ENTERING METHOD : enableSsidAndVerifyFromDevice");
	Boolean status = false;
	boolean webPaSetResponse = false;
	String outputFromDevice = null;
	LOGGER.info("Enabling the SSID through webPa Set command");
	webPaSetResponse = BroadBandWiFiUtils.setWebPaParams(device, parameter, BroadBandTestConstants.TRUE,
		BroadBandTestConstants.CONSTANT_3);
	if (webPaSetResponse) {
	    tapApi.waitTill(BroadBandTestConstants.THREE_MINUTE_IN_MILLIS);
	    outputFromDevice = getWifiNameFromDevice(device, tapApi, interfaceName);
	    status = CommonMethods.isNotNull(outputFromDevice)
		    && !(outputFromDevice.contains(BroadBandTestConstants.NOT_ASSOCIATED)
			    || outputFromDevice.contains(BroadBandTestConstants.INVALID_MAC_ADDRESS)
			    || outputFromDevice.contains(BroadBandTestConstants.OUTOFSERVICE)
			    || outputFromDevice.contains(BroadBandTestConstants.NOSUCHDEVICES));
	}
	LOGGER.debug("ENDING METHOD : enableSsidAndVerifyFromDevice");
	return status;
    }


}

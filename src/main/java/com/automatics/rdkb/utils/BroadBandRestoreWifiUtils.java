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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Dut;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants.NonDefaultWiFiParametersEnum;
import com.automatics.rdkb.constants.BroadBandWebPaConstants.RdkBSsidParameters;
import com.automatics.rdkb.constants.BroadBandWebPaConstants.WIFI_RESTORE_METHOD;
import com.automatics.rdkb.constants.RDKBTestConstants.WiFiFrequencyBand;
import com.automatics.rdkb.utils.cdl.CodeDownloadUtils;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpMib;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpUtils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.snmp.SnmpDataType;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.webpa.WebPaParameter;

/**
 * Utils for Wifi restore settings
 * 
 * @author anandam.s
 * @Refactor Govardhan
 *
 */
public class BroadBandRestoreWifiUtils {
    /**
     * Logger instance for {@link BroadBandRestoreWifiUtils}
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(BroadBandRestoreWifiUtils.class);

    /**
     * Utility method to perform wifi restore on the device using SNMP
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * 
     * @return Boolean representing the result of the wifi restore Operation.
     * @refactor Govardhan
     */
    public static boolean performWifiRestoreSnmp(AutomaticsTapApi tapEnv, Dut device) {
	LOGGER.info("ENTERING METHOD performWifiRestoreSnmp");
	boolean result = false;
	String snmpOutput = BroadBandSnmpUtils.snmpSetOnEcm(tapEnv, device,
		BroadBandSnmpMib.ESTB_FACTORY_RESET_DEVICE.getOid(), SnmpDataType.INTEGER,
		BroadBandTestConstants.STRING_VALUE_THREE);
	if (CommonMethods.isNotNull(snmpOutput)) {
	    result = snmpOutput.trim().equals(BroadBandTestConstants.STRING_VALUE_THREE);
	    if (result) {
		LOGGER.info("BROAD BAND DEVICE WIFI RESET (SNMP) PERFORMED SUCCESSFULLY: " + result);
	    }
	} else {
	    LOGGER.error("SNMP command for restting wi-fi settings returned null");
	}
	LOGGER.info("ENDING METHOD performWifiRestoreSnmp");
	tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
	return result;
    }

    /**
     * This method reset the wifi settings and restore the default settings
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param method
     *            {@link WIFI_RESTORE_METHOD}
     * @return true if webpa is executed successfully
     * @author anandam.s
     * @refactor Govardhan
     */
    public static boolean restoreDefaultWifiSettings(Dut device, AutomaticsTapApi tapEnv, WIFI_RESTORE_METHOD method) {
	boolean status = false;

	switch (method) {
	case SNMP:
	    status = performWifiRestoreSnmp(tapEnv, device);
	    break;
	case WEBPA:
	    WebPaParameter webPaParam = new WebPaParameter();
	    webPaParam.setDataType(BroadBandTestConstants.CONSTANT_0);
	    webPaParam.setName(BroadBandWebPaConstants.WEBPA_PARAM_FOR_WIFI_RESET);
	    webPaParam.setValue(BroadBandTestConstants.STRING_WIFI_RESET_VALUE);
	    status = BroadBandCommonUtils.setWebPaParam(tapEnv, device, webPaParam);
	    break;
	default:
	    LOGGER.error("Unsupported method of wifi restore");
	    break;
	}
	return status;
    }

    /**
     * Get all the wifi parameters gor the specified band
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param radio
     *            {@link WiFiFrequencyBand}
     * @return map of webpa and its values
     * @author anandam.s
     * @refactor Govardhan
     */
    public static Map<String, String> getAllWifiParametersForRadioUsingWebpa(Dut device, AutomaticsTapApi tapEnv,
	    WiFiFrequencyBand radio) {

	String[] paramList = null;
	switch (radio) {
	case WIFI_BAND_2_GHZ:
	    paramList = BroadBandWebPaConstants.PARAMETERS_FOR_2_4_GHZ_BAND;
	    break;
	case WIFI_BAND_5_GHZ:
	    paramList = BroadBandWebPaConstants.PARAMETERS_FOR_5GHZ_BAND;
	    break;
	default:
	    LOGGER.error("Unsupported radio");
	    break;
	}
	Map<String, String> valuesOfParametersInGivenBand = tapEnv.executeMultipleWebPaGetCommands(device, paramList);
	LOGGER.info(
		"Responses for parameters for " + radio.toString() + " : " + valuesOfParametersInGivenBand.toString());
	return valuesOfParametersInGivenBand;
    }

    /**
     * Private method to print all the wifi paremeters using webpa
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @author anandam.s
     * @refactor Govardhan
     */
    private static void printAllWifiParameters(Dut device, AutomaticsTapApi tapEnv) {
	// Get All the webpa params for wifi settings 2.4GHZ
	Map<String, String> wifiParameterMap = BroadBandRestoreWifiUtils.getAllWifiParametersForRadioUsingWebpa(device,
		tapEnv, WiFiFrequencyBand.WIFI_BAND_2_GHZ);
	Set<String> keys = wifiParameterMap.keySet();
	LOGGER.info("############## 2.4GHZ WIFI PARAMETER VALUES  - BEFORE RESET ##########");
	for (Iterator<String> i = keys.iterator(); i.hasNext();) {
	    String key = (String) i.next();
	    String value = (String) wifiParameterMap.get(key);
	    LOGGER.info("PARAM : " + key + " VALUE : " + value);

	}
	LOGGER.info("######################################################################");
	// Get All the webpa params for wifi settings 2.4GHZ
	wifiParameterMap = BroadBandRestoreWifiUtils.getAllWifiParametersForRadioUsingWebpa(device, tapEnv,
		WiFiFrequencyBand.WIFI_BAND_5_GHZ);
	keys = wifiParameterMap.keySet();
	LOGGER.info("############## 5GHZ WIFI PARAMETER VALUES  - BEFORE RESET ##########");
	for (Iterator<String> i = keys.iterator(); i.hasNext();) {
	    String key = (String) i.next();
	    String value = (String) wifiParameterMap.get(key);
	    LOGGER.info("PARAM : " + key + " VALUE : " + value);
	}
	LOGGER.info("######################################################################");
    }

    /**
     * This Method will set non default values to all wifi parameters
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param nonDefaultParamEnum
     * @return
     * @author anandam.s
     * @refactor Govardhan
     */
    public static boolean setNonDefaultValuesForAllWifiParameters(Dut device, AutomaticsTapApi tapEnv)
	    throws TestException {
	LOGGER.info("STARTING METHOD: setNonDefaultValuesForAllWifiParameters");
	boolean status = true;
	List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
	String errorMessage = "";
	String responseMessage = null;
	Map<String, String> webPaResponse = null;

	WebPaParameter webPaParameter = null;
	for (NonDefaultWiFiParametersEnum param : NonDefaultWiFiParametersEnum.values()) {

	    if (param.getWebPaParam()
		    .equals(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_RADIO_OPERATINGSTANDARDS_IN_5GHZ)
		    && DeviceModeHandler.isDSLDevice(device)) {
		LOGGER.info("Setting default operating standard for DSL 5GHz as it cannot be modified");
		webPaParameter = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(param.getWebPaParam(),
			BroadBandTestConstants.OPERATING_STANDARDS_A_N_AC, param.getValueType());
		webPaParameters.add(webPaParameter);
	    } else if (param.getWebPaParam()
		    .equals(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_RADIO_OPERATINGSTANDARDS_IN_2GHZ)
		    && DeviceModeHandler.isDSLDevice(device)) {
		LOGGER.info("Setting default operating standard for DSL 2.4GHz as it cannot be modified");
		webPaParameter = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(param.getWebPaParam(),
			BroadBandTestConstants.OPERATING_STANDARDS_B_G_N, param.getValueType());
		webPaParameters.add(webPaParameter);
	    } else {
		webPaParameter = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(param.getWebPaParam(),
			param.getWebPaValue(), param.getValueType());
		webPaParameters.add(webPaParameter);
	    }
	}

	webPaResponse = tapEnv.executeMultipleWebPaSetCommands(device, webPaParameters);
	for (NonDefaultWiFiParametersEnum parameter : NonDefaultWiFiParametersEnum.values()) {
	    responseMessage = webPaResponse.get(parameter.getWebPaParam());
	    if (CommonMethods.isNotNull(responseMessage)) {
		if (!responseMessage.trim().equalsIgnoreCase(BroadBandTestConstants.SUCCESS_TXT)) {
		    errorMessage += "Failed to set parameter -" + parameter.getWebPaParam() + "with value -"
			    + parameter.getWebPaValue() + " webpa set response obtained is " + responseMessage + "\n";
		    status = false;
		}

	    } else {
		errorMessage += "Null message obtained for setting webpa parameter -" + parameter + "\n";
	    }

	}

	if (!status) {
	    LOGGER.error(errorMessage);
	    throw new TestException(errorMessage);
	} else {
	    tapEnv.waitTill(BroadBandTestConstants.TWO_MINUTE_IN_MILLIS);
	    printAllWifiParameters(device, tapEnv);
	}

	LOGGER.info("ENDING METHOD: setNonDefaultValuesForAllWifiParameters");
	return status;
    }

    /**
     * This method compares snmp command output for wifi parameters with a gievn value
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param valueExpected
     *            Value for comparison
     * @param mib
     *            SNMP MIB for execution
     * @return true if values are same
     * @author anandam.s
     * @refactor Govardhan
     */
    public static boolean verifySNMPValuesForWifiParameters(AutomaticsTapApi tapEnv, Dut device, String valueExpected,
	    BroadBandSnmpMib mib) {
	boolean status = false;
	String valueObtained = BroadBandSnmpUtils.executeSnmpWalkOnRdkDevices(tapEnv, device, mib.getOid());
	if (CommonMethods.isNotNull(valueObtained) && CommonMethods.isNotNull(valueExpected)) {
	    if (valueObtained.equals(valueExpected)) {
		status = true;
	    } else {
		LOGGER.error("Value obtained from SNMP does not match the expected value.EXPECTED: " + valueExpected
			+ " ACTUAL :" + valueObtained);
	    }
	} else {
	    LOGGER.error("One/Both of the values for comparison is null");
	}
	return status;
    }
    
    /**
     * Verify whether the SSID obtained has last 4 hex values in mac address
     * 
     * @param device
     *            Dut under test
     * @param radio
     *            {@link WiFiFrequencyBand}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param isSnmpNeeded
     *            true if SNMP Validation is Required.
     * @return true if ssid is as per standard
     */
    public static BroadBandResultObject verifyDefaultSSIDForPartner(Dut device, WiFiFrequencyBand radio,
	    AutomaticsTapApi tapEnv, boolean isSnmpNeeded) {
	LOGGER.info("STARTING METHOD verifyDefaultSSIDForPartner");
	BroadBandResultObject broadBandResultObject = new BroadBandResultObject();
	String webpaParameter = null;
	String snmpOid = null;
	RdkBSsidParameters ssidparam = null;
	long startTime = System.currentTimeMillis();
	String partnerId = null;
	String errorMessage = "Unable to validate default SSID of radio " + radio;
	boolean status = false;
	try {
	    do {
		partnerId = tapEnv.executeWebPaCommand(device,
			BroadBandWebPaConstants.WEBPA_PARAM_FOR_SYNDICATION_PARTNER_ID);
		LOGGER.info("Current Partner ID of the device Retrieved via WEBPA is :" + partnerId);
	    } while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.TWO_MINUTE_IN_MILLIS
		    && !CommonMethods.isNotNull(partnerId)
		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
	    if (CommonMethods.isNotNull(partnerId) && BroadBandCommonUtils.verifyPartnerAvailability(partnerId)) {
		if (radio.equals((WiFiFrequencyBand.WIFI_BAND_2_GHZ))) {
		    snmpOid = BroadBandSnmpMib.ECM_WIFI_SSID_2_4.getOid();
		    ssidparam = RdkBSsidParameters.SSID_FOR_2GHZ_PRIVATE_WIFI;
		    webpaParameter = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID;
		} else if (radio.equals((WiFiFrequencyBand.WIFI_BAND_5_GHZ))) {
		    snmpOid = BroadBandSnmpMib.ECM_WIFI_SSID_5.getOid();
		    ssidparam = RdkBSsidParameters.SSID_FOR_5GHZ_PRIVATE_WIFI;
		    webpaParameter = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID;
		}
		String ssid = isSnmpNeeded ? BroadBandSnmpUtils.snmpWalkOnEcm(tapEnv, device, snmpOid) :
			tapEnv.executeWebPaCommand(device, webpaParameter);
		LOGGER.info(radio.toString() + "SSID retieved after wifi reset  :  " + ssid);
		if (CommonMethods.isNotNull(ssid)) {
		    status = BroadBandCommonUtils.validateDefaultSsidforDifferentPartners(device, tapEnv, ssid,
			    ssidparam, device.getModel(), partnerId, radio);
		    broadBandResultObject.setStatus(status);
		    broadBandResultObject.setErrorMessage(errorMessage);
		    return broadBandResultObject;
		} else {
		    errorMessage = "Null value obtained for SSID!";
		    broadBandResultObject.setErrorMessage(errorMessage);
		    broadBandResultObject.setStatus(status);
		}
	    } else {
		errorMessage = "Invalid Partner ID Obtained Via WEBPA Response.";
		broadBandResultObject.setErrorMessage(errorMessage);
		broadBandResultObject.setStatus(status);
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception Occured While Validating the Default SSID: " + e.getMessage());
	    broadBandResultObject.setErrorMessage(e.getMessage());
	}
	LOGGER.debug("Ending METHOD verifyDefaultSSIDForPartner");
	return broadBandResultObject;
    }
    /**
     * Verify whether the SSID obtained has last 4 hex values in mac address
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param radio
     *            {@link WiFiFrequencyBand}
     * @return true if ssid is as per standard
     * @refactor Athira
     */
    public static BroadBandResultObject verifyDefaultSsidForAllPartners(Dut device, AutomaticsTapApi tapEnv,
	    WiFiFrequencyBand radio) {
	LOGGER.debug("STARTING METHOD verifyDefaultSsidForAllPartners");
	// Variable declaration starts
	BroadBandResultObject broadBandResultObject = new BroadBandResultObject();
	String webpaParameter = null;
	RdkBSsidParameters ssidparam = null;
	long startTime = System.currentTimeMillis();
	String partnerId = null;
	String errorMessage = "Unable to validate default SSID of radio " + radio;
	boolean status = false;
	// Variable declaration ends
	try {
	    do {
		partnerId = tapEnv.executeWebPaCommand(device,
			BroadBandWebPaConstants.WEBPA_PARAM_FOR_SYNDICATION_PARTNER_ID);
		LOGGER.info("Current Partner ID of the device Retrieved via WEBPA is :" + partnerId);
	    } while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.TWO_MINUTE_IN_MILLIS
		    && !CommonMethods.isNotNull(partnerId)
		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
	    if (CommonMethods.isNotNull(partnerId) && BroadBandCommonUtils.verifyPartnerAvailability(partnerId)) {
		if (radio.equals((WiFiFrequencyBand.WIFI_BAND_2_GHZ))) {
		    ssidparam = RdkBSsidParameters.SSID_FOR_2GHZ_PRIVATE_WIFI;
		    webpaParameter = BroadBandWebPaConstants.WEBPA_DEFAULT_SSID_NAME_2_4_GHZ;
		} else if (radio.equals((WiFiFrequencyBand.WIFI_BAND_5_GHZ))) {
		    ssidparam = RdkBSsidParameters.SSID_FOR_5GHZ_PRIVATE_WIFI;
		    webpaParameter = BroadBandWebPaConstants.WEBPA_DEFAULT_SSID_NAME_5_GHZ;
		}
		String ssid = tapEnv.executeWebPaCommand(device, webpaParameter);
		LOGGER.info(radio.toString() + "SSID retieved after wifi reset  :  " + ssid);
		if (CommonMethods.isNotNull(ssid)) {
		    status = BroadBandCommonUtils.validateDefaultSsidforDifferentPartners(device, tapEnv, ssid,
			    ssidparam, device.getModel(), partnerId, radio);
		    broadBandResultObject.setStatus(status);
		    broadBandResultObject.setErrorMessage(errorMessage);
		    return broadBandResultObject;
		} else {
		    errorMessage = "Null value obtained for SSID!";
		    broadBandResultObject.setErrorMessage(errorMessage);
		    broadBandResultObject.setStatus(status);
		}
	    } else {
		errorMessage = "Invalid Partner ID Obtained Via WEBPA Response.";
		broadBandResultObject.setErrorMessage(errorMessage);
		broadBandResultObject.setStatus(status);
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception Occured While Validating the Default SSID: " + e.getMessage());
	    broadBandResultObject.setErrorMessage(e.getMessage());
	}
	LOGGER.debug("Ending METHOD verifyDefaultSsidForAllPartners");
	return broadBandResultObject;
    }
    
    /**
     * This method will get the image name to be flashed and verify whether this image is already flashed
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param buildNameToBeTriggerred
     *            Image which is going to be flashed
     * @return
     */
    public static boolean verifyWhetherImageIsAlreadyFlashedInDevice(Dut device, AutomaticsTapApi tapEnv,
	    String buildNameToBeTriggerred) {
	boolean isImageAlreadyFlashed = false;
	String buildNameWithoutExtension = null;
	if (buildNameToBeTriggerred.contains(BroadbandPropertyFileHandler.getDeviceSpecificBuild1())) {
	    buildNameWithoutExtension = buildNameToBeTriggerred.replace(BroadbandPropertyFileHandler.getDeviceSpecificBuild1(), "");
	}  else if (buildNameToBeTriggerred.contains(BroadbandPropertyFileHandler.getDeviceSpecificBuild2())) {
	    buildNameWithoutExtension = buildNameToBeTriggerred
			    .replace(BroadbandPropertyFileHandler.getDeviceSpecificBuild2(), "");
	}else if (buildNameToBeTriggerred.contains(BroadbandPropertyFileHandler.getDeviceSpecificBuild3())) {
	    buildNameWithoutExtension = buildNameToBeTriggerred
		    .replace(BroadbandPropertyFileHandler.getDeviceSpecificBuild3(), "");
	} else if (buildNameToBeTriggerred.contains(BroadbandPropertyFileHandler.getDeviceSpecificBuild4())) {
	    buildNameWithoutExtension = buildNameToBeTriggerred
		    .replace(BroadbandPropertyFileHandler.getDeviceSpecificBuild4(), "");
	}
	isImageAlreadyFlashed = CodeDownloadUtils.verifyImageVersionFromVersionText(tapEnv, device,
		buildNameWithoutExtension);
	return isImageAlreadyFlashed;
    }
    
    /**
     * This method compares two webpa parameters before and after CDL
     * 
     * @param firstSetForComparision
     *            set before CDL
     * @param secondSetForComparision
     *            set after CDL
     * @return true if both webpa values are same
     * @author anandam.s
     * @refactor yamini.s
     */
    public static boolean compareWifiSettingsWithWebpaValuesBeforeAndAfterCDL(
	    Map<String, String> firstSetForComparision, Map<String, String> secondSetForComparision,
	    String webpaParameter) {
	boolean status = false;
	String errorMessage = null;
	String parameterValueAfterCDL = secondSetForComparision.get(webpaParameter);
	String parameterValueBeforeCDL = firstSetForComparision.get(webpaParameter);
	LOGGER.info("Value before CDL : " + parameterValueBeforeCDL);
	LOGGER.info("Value after CDL : " + parameterValueAfterCDL);
	if (CommonMethods.isNotNull(parameterValueBeforeCDL) && CommonMethods.isNotNull(parameterValueAfterCDL)) {
	    if (parameterValueBeforeCDL.equalsIgnoreCase(parameterValueAfterCDL)) {
		LOGGER.info(webpaParameter + "Value did not change after CDL");
		status = true;
	    } else {
		errorMessage = "2.4 GHz SSID has changed after CDL .Value before CDL : " + parameterValueBeforeCDL
			+ " Value after CDL : " + parameterValueAfterCDL;
		LOGGER.error(errorMessage);
		throw new TestException(errorMessage);
	    }
	} else {
	    errorMessage = "One /Both of the parameters obtained is null";
	    LOGGER.error(errorMessage);
	    throw new TestException(errorMessage);
	}
	return status;
    }

    /**
     * This method compares two snmp parameters before and after CDL
     * 
     * @param firstSetForComparision
     *            set before CDL
     * @param secondSetForComparision
     *            set after CDL
     * @return true if both snmp values are same
     * @author anandam.s
     * @refactor yamini.s
     */
    public static boolean compareWifiSettingsWithSNMPParameterValuesBeforeAndAfterCDL(
	    Map<String, String> firstSetForComparision, Map<String, String> secondSetForComparision, String OID) {
	boolean status = false;
	String errorMessage = null;
	String parameterValueAfterCDL = secondSetForComparision.get(OID);
	String parameterValueBeforeCDL = firstSetForComparision.get(OID);
	if (CommonMethods.isNotNull(parameterValueBeforeCDL) && CommonMethods.isNotNull(parameterValueAfterCDL)) {
	    if (parameterValueBeforeCDL.equals(parameterValueAfterCDL)) {
		status = true;
	    } else {
		errorMessage = OID + "Parameter has changed after CDL .Value before CDL : " + parameterValueBeforeCDL
			+ " Value after CDL : " + parameterValueAfterCDL;
		LOGGER.error(errorMessage);
		throw new TestException(errorMessage);
	    }
	} else {
	    errorMessage = "One /Both of the parameters obtained is null";
	    LOGGER.error(errorMessage);
	    throw new TestException(errorMessage);
	}
	return status;
    }

}

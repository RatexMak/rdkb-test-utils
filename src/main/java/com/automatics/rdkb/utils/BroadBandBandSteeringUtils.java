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
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Dut;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants.BAND_STEERING_PARAM;
import com.automatics.rdkb.constants.RDKBTestConstants.WiFiFrequencyBand;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;
import com.automatics.rdkb.utils.dmcli.DmcliUtils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.webpa.WebPaParameter;

/**
 * Utils for band steering tests
 * 
 * @author anandam.s
 * @refactor Govardhan, Alan_Bivera
 */
public class BroadBandBandSteeringUtils {

    /**
     * Logger instance for {@link BroadBandBandSteeringUtils}
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(BroadBandBandSteeringUtils.class);

    /**
     * This method will configure SSID for both Radios
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {{@link AutomaticsTapApi}
     * @return {@link BroadBandResultObject}
     * @refactor Govardhan
     */
    public static BroadBandResultObject configureSSIDForBothRadios(Dut device, AutomaticsTapApi tapEnv,
	    Map<String, String> paramMap) {
	BroadBandResultObject result = new BroadBandResultObject();
	List<String> paramList = new ArrayList<String>();
	for (Map.Entry<String, String> entry : paramMap.entrySet()) {
	    paramList.add(entry.getKey());
	}
	// set new values
	List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
	for (String param : paramList) {
	    WebPaParameter webPaParameter = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(param,
		    paramMap.get(param), WebPaDataTypes.STRING.getValue());
	    webPaParameters.add(webPaParameter);

	}
	Map<String, String> serverResponse = tapEnv.executeMultipleWebPaSetCommands(device, webPaParameters);
	try {
	    for (String param : paramList) {

		boolean status = serverResponse.get(param).equalsIgnoreCase(BroadBandTestConstants.SUCCESS_TXT);
		result.setStatus(status);
		if (!status) {
		    result.setErrorMessage("Failed to Set webpa for " + param);
		    LOGGER.error(result.getErrorMessage());
		    break;
		}
	    }
	} catch (NullPointerException exception) {
	    result.setErrorMessage("Message is obtained as null from webpa server");
	    LOGGER.error(result.getErrorMessage());
	}
	return result;
    }

    /**
     * Get and compare the webpa or dmcli values with the default values
     * 
     * @param settop
     *            {@link Settop}
     * @param webpaParameter
     *            webpa or dmcli parameter to be checked
     * @param defaultValue
     *            expected value
     * @param isWebpa
     *            true if webpa needs to be checked else if dmcli
     * @return BroadBandResultObject {@link BroadBandResultObject}
     * @Refactor Alan_Bivera
     */

    public static BroadBandResultObject getAndCompareWebpaOrDmcliValues(Dut device, AutomaticsTapApi tapEnv,
	    String webpaParameter, String defaultValue, boolean isWebpa,
	    Map<String, String> bandSteeringWebpaResponse) {
	String response = null;
	BroadBandResultObject result = new BroadBandResultObject();
	if (isWebpa) {
	    response = bandSteeringWebpaResponse.get(webpaParameter);
	} else {
	    response = DmcliUtils.getParameterValueUsingDmcliCommand(device, tapEnv, webpaParameter);
	}
	if (CommonMethods.isNotNull(response)) {
	    LOGGER.info(" Response of " + (isWebpa ? "Webpa " : "dmcli ") + webpaParameter + " : " + response);
	    result.setStatus(response.equalsIgnoreCase(defaultValue));
	    if (!result.isStatus()) {
		result.setErrorMessage("Default value of " + (isWebpa ? "Webpa " : "dmcli ") + webpaParameter
			+ "  is not as exepected . EXPECTED :  " + defaultValue + "ACTUAL : " + response);
	    }
	} else {
	    result.setErrorMessage(
		    "Error in getting " + (isWebpa ? "Webpa " : "dmcli ") + " response for " + webpaParameter);
	}

	return result;
    }

    /**
     * Method to enable band steering on the gateway via WebPa
     * 
     * @param device
     *            Instance of Dut
     * @param tapEnv
     *            Instance of AutomaticsTapApi
     * @return result of set operation
     * @refactor Said Hisham
     */
    public static boolean enableDisableBandSteeringViaWebPa(Dut device, AutomaticsTapApi tapEnv, boolean isEnable) {
	boolean setResult = false;
	LOGGER.info("<--- Starting method enableDisableBandSteeringViaWebPa --->");
	List<WebPaParameter> setResponse = tapEnv.setWebPaParams(device,
		BroadBandWebPaConstants.WEBPA_PARAM_BAND_STEERING_ENABLE,
		(isEnable) ? BroadBandTestConstants.TRUE : BroadBandTestConstants.FALSE,
		WebPaDataTypes.BOOLEAN.getValue());
	setResult = null != setResponse && setResponse.size() > 0
		&& CommonMethods.isNotNull(setResponse.get(0).getMessage())
		&& (setResponse.get(0).getMessage().equalsIgnoreCase(BroadBandTestConstants.SUCCESS_TXT));
	LOGGER.info("<--- Ending method enableDisableBandSteeringViaWebPa --->");
	return setResult;
    }
    
	/**
	 * This method will get the PhyThreshold for required radio
	 * 
	 * @param device
	 *            {@link Dut}
	 * @param tapEnv
	 *            {@link AutomaticsTapApi}
	 * @param band
	 * @link {@link WiFiFrequencyBand}
	 * @return parameter value
	 * @author anandam.s
	 * @refactor Alan_Bivera
	 */
	public static String getBandSteeringPhyThreshold(Dut device, AutomaticsTapApi tapEnv, WiFiFrequencyBand band) {

		String parameter = null;
		String response = null;
		switch (band) {
		case WIFI_BAND_2_GHZ:
			parameter = BroadBandWebPaConstants.WEBPA_PARAM_BAND_STEERING_PHY_THRESHOLD_2_4GHZ;
			break;
		case WIFI_BAND_5_GHZ:
			parameter = BroadBandWebPaConstants.WEBPA_PARAM_BAND_STEERING_PHY_THRESHOLD_5GHZ;
			break;
		default:
			LOGGER.error("Wrong radio type passed");
			break;

		}
		if (CommonUtils.isNotEmptyOrNull(parameter)) {
			response = tapEnv.executeWebPaCommand(device, parameter);
		} else {
			LOGGER.error("Wrong radio type passed");
		}
		return response;
	}
	
	/**
	 * This method will set the PhyThreshold for required radio
	 * 
	 * @param device
	 *            {@link Dut}
	 * @param tapEnv
	 *            {@link AutomaticsTapApi}
	 * @param band
	 *            {@link WiFiFrequencyBand}
	 * @param value
	 *            value to be set
	 * @return parameter value
	 * @author anandam.s
	 * @refactor Alan_Bivera
	 */
	public static boolean setBandSteeringPhyThreshold(Dut device, AutomaticsTapApi tapEnv, WiFiFrequencyBand band,
			String value) {

		String parameter = null;
		boolean response = false;
		switch (band) {
		case WIFI_BAND_2_GHZ:
			parameter = BroadBandWebPaConstants.WEBPA_PARAM_BAND_STEERING_PHY_THRESHOLD_2_4GHZ;
			break;
		case WIFI_BAND_5_GHZ:
			parameter = BroadBandWebPaConstants.WEBPA_PARAM_BAND_STEERING_PHY_THRESHOLD_5GHZ;
			break;
		default:
			LOGGER.error("Wrong radio type passed");
			break;
		}
		WebPaParameter webPaParameter = new WebPaParameter();
		webPaParameter.setName(parameter);
		webPaParameter.setDataType(BroadBandTestConstants.CONSTANT_1);
		webPaParameter.setValue(value);
		if (CommonUtils.isNotEmptyOrNull(parameter)) {
			response = BroadBandCommonUtils.setWebPaParam(tapEnv, device, webPaParameter);
		} else {
			LOGGER.error("Wrong radio type passed");
		}
		return response;
	}
	
	/**
	 * This method checks for specific parameter in lbd.conf file
	 * 
	 * @param device
	 *            {@link Settop}
	 * @param tapEnv
	 *            {@link AutomaticsTapApi}
	 * @param band
	 *            {@link WiFiFrequencyBand}
	 * @param param
	 *            {@link BAND_STEERING_PARAM}
	 * @return true if pattern is seen
	 * @refactor Alan_Bivera
	 */
	public static BroadBandResultObject checkForPatternInLbdConfFile(Dut device, AutomaticsTapApi tapEnv,
			WiFiFrequencyBand band, BAND_STEERING_PARAM param) {

		BroadBandResultObject result = new BroadBandResultObject();
		try {
			String fileContents = getContentsForRadioFromLBDConfFile(device, tapEnv, band);
			LOGGER.info(" Relevant contents of file: " + fileContents);
			String checkForString = null;
			if (null != band && null != param) {
				switch (param) {
				case BAND_STEERING_PARAM_APGROUP:
					LOGGER.error("Currently not implemented");
					break;
				case BAND_STEERING_PARAM_IDLE_INACTIVE_TIME:
					checkForString = BroadBandTestConstants.CMD_LBD_IDLE_INACTIVE + "="
							+ ((band == WiFiFrequencyBand.WIFI_BAND_2_GHZ)
									? BroadBandTestConstants.TEST_VALUE_IDLE_INACTIVE_TIME_2GHZ
									: BroadBandTestConstants.TEST_VALUE_IDLE_INACTIVE_TIME_5GHZ);
					break;
				case BAND_STEERING_PARAM_OVERLOAD_INACTIVE_TIME:
					checkForString = BroadBandTestConstants.CMD_LBD_OVERLOAD_INACTIVE + "="
							+ ((band == WiFiFrequencyBand.WIFI_BAND_2_GHZ)
									? BroadBandTestConstants.TEST_VALUE_OVERLOAD_INACTIVE_TIME_2GHZ
									: BroadBandTestConstants.TEST_VALUE_OVERLOAD_INACTIVE_TIME_5GHZ);
					break;
				default:
					result.setStatus(false);
					result.setErrorMessage("Unsupported Pram");
					LOGGER.error(result.getErrorMessage());
					break;

				}
				LOGGER.info("Going to check for String :" + checkForString);
				if (CommonMethods.isNotNull(checkForString)) {
					result.setStatus(fileContents.contains(checkForString));
					if (result.isStatus()) {
						LOGGER.info("String " + checkForString + " is present in lbd.conf");
					} else {
						result.setErrorMessage("String " + checkForString + " is not present in lbd.conf");
						LOGGER.error("String " + checkForString + " is not present in lbd.conf");
					}
				} else {
					result.setStatus(false);
					result.setErrorMessage("String for checking in lbd.conf is null");
					LOGGER.error(result.getErrorMessage());
				}
			} else {
				result.setStatus(false);
				result.setErrorMessage("WifiFrequeny band or band steering parameter is passed as null");
				LOGGER.error(result.getErrorMessage());
			}
		} catch (Exception e) {
			result.setStatus(false);
			result.setErrorMessage("Exception occured !!!" + e.getMessage());
			LOGGER.error(result.getErrorMessage());
		}
		return result;
	}
	
	/**
	 * Get the contents of given radio from LBD.conf file
	 * 
	 * @param settop
	 *            {@link Settop}
	 * @param tapEnv
	 *            {@link AutomaticsTapApi}
	 * @param band
	 *            {@link WiFiFrequencyBand}
	 * @return fileContents
	 * @refactor Alan_Bivera
	 * 
	 */
	public static String getContentsForRadioFromLBDConfFile(Dut device, AutomaticsTapApi tapEnv, WiFiFrequencyBand band)
			throws TestException {
		String fileContents = null;
		try {
			String response = tapEnv.executeCommandOnAtom(device, BroadBandTestConstants.CMD_CAT_LBD);
			LOGGER.debug("Response of command  " + BroadBandTestConstants.CMD_CAT_LBD + " :  " + response);
			switch (band) {
			case WIFI_BAND_2_GHZ:
				fileContents = response.substring(response.indexOf(BroadBandTestConstants.STRING_LBD_HEADING_2G_PARAMS),
						response.indexOf(BroadBandTestConstants.STRING_LBD_HEADING_5G_PARAMS));
				break;
			case WIFI_BAND_5_GHZ:
				fileContents = response.substring(response.indexOf(BroadBandTestConstants.STRING_LBD_HEADING_5G_PARAMS),
						response.indexOf(BroadBandTestConstants.STRING_LBD_STADB_PARAMS));
				break;
			default:
				LOGGER.error("Unsupported radio");
				break;

			}
		} catch (Exception e) {
			throw new TestException("Exception occured while executing command in atom console. " + e.getMessage());
		}
		return fileContents;
	}
	
	/**
	 * This method checks for specific parameter in cfg
	 * 
	 * @param device
	 *            {@link Settop}
	 * @param tapEnv
	 *            {@link AutomaticsTapApi}
	 * @param band
	 *            {@link WiFiFrequencyBand}
	 * @param param
	 *            {@link BAND_STEERING_PARAM}
	 * @return true if pattern is seen
	 * @refactor Alan_Bivera
	 */
	public static BroadBandResultObject checkForPatternInCfg(Dut device, AutomaticsTapApi tapEnv, WiFiFrequencyBand band,
			BAND_STEERING_PARAM param) {

		String command = null;
		String value = null;
		String response = null;
		BroadBandResultObject result = new BroadBandResultObject();
		try {
			if (null != band && null != param) {
				switch (param) {
				case BAND_STEERING_PARAM_APGROUP:
					LOGGER.error("Currently not implemented");
					break;
				case BAND_STEERING_PARAM_IDLE_INACTIVE_TIME:
					command = BroadBandTestConstants.CMD_CFG_IDLE_INACTIVE;
					value = (band == WiFiFrequencyBand.WIFI_BAND_2_GHZ)
							? BroadBandTestConstants.TEST_VALUE_IDLE_INACTIVE_TIME_2GHZ
							: BroadBandTestConstants.TEST_VALUE_IDLE_INACTIVE_TIME_5GHZ;
					break;
				case BAND_STEERING_PARAM_OVERLOAD_INACTIVE_TIME:
					command = BroadBandTestConstants.CMD_CFG_OVERLOAD_INACTIVE;
					value = (band == WiFiFrequencyBand.WIFI_BAND_2_GHZ)
							? BroadBandTestConstants.TEST_VALUE_OVERLOAD_INACTIVE_TIME_2GHZ
							: BroadBandTestConstants.TEST_VALUE_OVERLOAD_INACTIVE_TIME_5GHZ;
					break;
				default:
					LOGGER.error("Unsupported Pram");
					break;

				}
				LOGGER.info("Going to execute command" + command + " and check for string " + value
						+ " in the command output");
				if (CommonMethods.isNotNull(command)) {
					response = tapEnv.executeCommandOnAtom(device, command);
				} else {
					result.setStatus(false);
					result.setErrorMessage("Command to execute ins atom console is null");
					LOGGER.error(result.getErrorMessage());
				}

				LOGGER.info("Response of command  " + command + " :  " + response);
				if (CommonMethods.isNotNull(response)) {
					result.setStatus(response.contains(value));
					if (result.isStatus()) {
						LOGGER.info("String " + value + " is present in cfg");
					} else {
						result.setErrorMessage("String " + value + " is not present in cfg");
						LOGGER.error("String " + value + " is not present in cfg");
					}
				} else {
					result.setStatus(false);
					result.setErrorMessage("Response of command  " + command + "is obtained as null");
					LOGGER.error(result.getErrorMessage());
				}
			} else {
				result.setStatus(false);
				result.setErrorMessage("WifiFrequeny band or band steering parameter is passed as null");
				LOGGER.error(result.getErrorMessage());
			}
		} catch (Exception e) {
			result.setStatus(false);
			result.setErrorMessage("Exception occured !!!" + e.getMessage());
			LOGGER.error(result.getErrorMessage());
		}
		return result;
	}

}

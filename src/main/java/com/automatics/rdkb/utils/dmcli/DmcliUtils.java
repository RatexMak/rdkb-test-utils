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
package com.automatics.rdkb.utils.dmcli;

/**
 * Utility class to handle all DMCLI command utility related functionality.
 * 
 * @author Selvaraj Mariyappan
 * @refactor Govardhan
 */
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Dut;
import com.automatics.error.GeneralError;
import com.automatics.exceptions.FailedTransitionException;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.rdkb.utils.CommonUtils;

public class DmcliUtils {
    /**
     * Logger instance for {@link DmcliUtils}
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DmcliUtils.class);
	/**
	 * Utility method to convert RDK-B WebPA Wi-Fi parameter Index to compatible
	 * dmcli parameter index
	 * 
	 * @param tr181Parameter The WebPA parameter
	 * @return The dmcli compatible parameter.
	 * @author Govardhan
	 */
	public static String convertRdkbWebPaWiFiParameterIndexToDmcliParameterIndex(String tr181Parameter) {
		if (tr181Parameter.contains(BroadBandWebPaConstants.WEBPA_TABLE_DEVICE_WIFI)) {

			if (tr181Parameter.contains(BroadBandWebPaConstants.WEBPA_INDEX_2_4_GHZ_PRIVATE_SSID)) {
				tr181Parameter = tr181Parameter.replace(BroadBandWebPaConstants.WEBPA_INDEX_2_4_GHZ_PRIVATE_SSID, "1");
			} else if (tr181Parameter.contains(BroadBandWebPaConstants.WEBPA_INDEX_5_GHZ_PRIVATE_SSID)) {
				tr181Parameter = tr181Parameter.replace(BroadBandWebPaConstants.WEBPA_INDEX_5_GHZ_PRIVATE_SSID, "2");
			} else if (tr181Parameter.contains(BroadBandWebPaConstants.WEBPA_INDEX_2_4_GHZ_PUBLIC_WIFI)) {
				tr181Parameter = tr181Parameter.replace(BroadBandWebPaConstants.WEBPA_INDEX_2_4_GHZ_PUBLIC_WIFI, "5");
			} else if (tr181Parameter.contains(BroadBandWebPaConstants.WEBPA_INDEX_5_GHZ_PUBLIC_WIFI)) {
				tr181Parameter = tr181Parameter.replace(BroadBandWebPaConstants.WEBPA_INDEX_5_GHZ_PUBLIC_WIFI, "6");
			} else if (tr181Parameter.contains(BroadBandWebPaConstants.WEBPA_INDEX_2_4_GHZ_PUBLIC_SSID_AP2)) {
				tr181Parameter = tr181Parameter.replace(BroadBandWebPaConstants.WEBPA_INDEX_2_4_GHZ_PUBLIC_SSID_AP2,
						"9");
			} else if (tr181Parameter.contains(BroadBandWebPaConstants.WEBPA_INDEX_5_GHZ_PUBLIC_SSID_AP2)) {
				tr181Parameter = tr181Parameter.replace(BroadBandWebPaConstants.WEBPA_INDEX_5_GHZ_PUBLIC_SSID_AP2,
						"10");
			} else if (tr181Parameter.contains(BroadBandTestConstants.RADIO_24_GHZ_INDEX)) {
				tr181Parameter = tr181Parameter.replace(BroadBandTestConstants.RADIO_24_GHZ_INDEX, "1");
			} else if (tr181Parameter.contains(BroadBandTestConstants.RADIO_5_GHZ_INDEX)) {
				tr181Parameter = tr181Parameter.replace(BroadBandTestConstants.RADIO_5_GHZ_INDEX, "2");
			} else if (tr181Parameter.contains("10006")) {
				tr181Parameter = tr181Parameter.replace("10006", "11");
			} else if (tr181Parameter.contains("10106")) {
				tr181Parameter = tr181Parameter.replace("10106", "12");
			}else if (tr181Parameter.contains(BroadBandWebPaConstants.WEBPA_INDEX_2_4_GHZ_OPEN_LNF_AP1)) {
				tr181Parameter = tr181Parameter.replace(BroadBandWebPaConstants.WEBPA_INDEX_2_4_GHZ_OPEN_LNF_AP1, "7");
			} else if (tr181Parameter.contains(BroadBandWebPaConstants.WEBPA_INDEX_5_GHZ_OPEN_LNF_AP2)) {
				tr181Parameter = tr181Parameter.replace(BroadBandWebPaConstants.WEBPA_INDEX_5_GHZ_OPEN_LNF_AP2, "8");
			} else if (tr181Parameter.contains("10007")) {
				tr181Parameter = tr181Parameter.replace("10006", "13");
			} else if (tr181Parameter.contains("10107")) {
				tr181Parameter = tr181Parameter.replace("10106", "14");
			} else if (tr181Parameter.contains("10008")) {
				tr181Parameter = tr181Parameter.replace("10006", "15");
			} else if (tr181Parameter.contains("10108")) {
				tr181Parameter = tr181Parameter.replace("10106", "16");
			}
		}
		return tr181Parameter;
	}


	/**
	 * Utility method to get the TR-181 parameter value using DMCLI command.
	 * 
	 * @param device
	 *            The Dut instance to be validated.
	 * @param tapAPI
	 *            The {@link AutomaticsTapApi} reference.
	 * @param parameter
	 *            TR-181 parameter to get value.
	 * @return The required value.
	 */
	public static String getParameterValueUsingDmcliCommand(Dut device, AutomaticsTapApi tapAPI, String parameter) {
		List<String> requiredValues = getParameterValuesUsingDmcliCommand(device, tapAPI, new String[] { parameter });
		return requiredValues.get(0);
	}
	
	/**
	 * Utility method to get the values for multiple TR-181 parameters using DMCLI
	 * command.
	 * 
	 * @param device    The Dut instance to be validated.
	 * @param tapAPI  The {@link AutomaticsTapApi} reference.
	 * @param parameter TR-181 parameters list to get value.
	 * @return The required value.
	 */
	public static List<String> getParameterValuesUsingDmcliCommand(Dut device, AutomaticsTapApi tapAPI,
			String[] parameters) {
		Map<String, String> dmcliResponse = getParamValuesUsingDmcli(device, tapAPI, parameters);
		List<String> requiredValues = new ArrayList<String>(dmcliResponse.values());
		return requiredValues;
	}
	
	/**
	 * Utility method to get the key values (TR-181, Value) for multiple TR-181
	 * parameters using DMCLI command.
	 * 
	 * @param device    The Dut instance to be validated.
	 * @param tapAPI  The {@link AutomaticsTapApi} reference.
	 * @param parameter TR-181 parameters list to get value.
	 * @return The required value {@link Map}.
	 */
	public static Map<String, String> getParamValuesUsingDmcli(Dut device, AutomaticsTapApi tapAPI,
			String[] parameters) {
		Map<String, String> requiredValues = new LinkedHashMap<String, String>();
		for (String tr181Parameter : parameters) {
			String webPaParameter = tr181Parameter;

			tr181Parameter = convertRdkbWebPaWiFiParameterIndexToDmcliParameterIndex(tr181Parameter);

			String dmcliCommand = BroadBandTestConstants.CMD_DMCLI_GET_VALUE + AutomaticsConstants.SPACE + tr181Parameter;
			String dmcliCommandOutput = tapAPI.executeCommandUsingSsh(device, dmcliCommand);
			if (CommonMethods.isNotNull(dmcliCommandOutput)) {
				String[] outputLines = dmcliCommandOutput.split(RDKBTestConstants.DELIMITER_NEW_LINE);
				String requiredValue = null;
				for (String eachLine : outputLines) {
					requiredValue = CommonMethods.patternFinder(eachLine,
							BroadBandTestConstants.REGULAR_EXPRESSION_DMCLI_COMMAND_VALUE);
					if (CommonMethods.isNotNull(requiredValue)) {
						requiredValue = requiredValue.trim();
						break;
					}
				}
				requiredValues.put(webPaParameter, requiredValue);
			} else {
				throw new FailedTransitionException(GeneralError.SSH_CONNECTION_FAILURE,
						"Unable to connect to Device via SSH command or Device not responding to dmcli command for parameter : "
								+ tr181Parameter);
			}
		}
		return requiredValues;
	}
	/**
	 * Utility method to set the value using dmcli command DMCLI command.
	 * 
	 * @param device    The device instance to be validated.
	 * @param tapEnv  The {@link AutomaticsTapApi} reference.
	 * @param parameter TR-181 parameter to set the value
	 * @return The required value.
	 */
	public static boolean setParameterValueUsingDmcliCommand(Dut device, AutomaticsTapApi tapEnv, String parameter,
			String dataType, String value) {
		String dmcliCommand = null;
		String dmcliResponse = null;
		Boolean status = false;

		dmcliCommand = BroadBandCommonUtils.concatStringUsingStringBuffer(
				BroadBandTestConstants.DMCLI_PREFIX_TO_SET_PARAMETER, AutomaticsConstants.SPACE, parameter,
				AutomaticsConstants.SPACE, dataType, AutomaticsConstants.SPACE, value);
		LOGGER.info("Dmcli Set Command to Execute is:" + dmcliCommand);
		dmcliResponse = tapEnv.executeCommandUsingSsh(device, dmcliCommand);
		if (CommonMethods.isNotNull(dmcliResponse)) {
			status = CommonUtils.isGivenStringAvailableInCommandOutput(dmcliResponse,
					BroadBandTraceConstants.DMCLI_OUTPUT_EXECUTION_SUCCEED);
		} else {
			throw new FailedTransitionException(GeneralError.SSH_CONNECTION_FAILURE,
					"Unable to connect to Device via SSH command or Device not responding to dmcli command for parameter : "
							+ parameter);
		}
		return status;
	}
	
    /**
     * Utility method to addtable using dmcli command.
     * 
     * @param device
     *            The Dut instance to be validated.
     * @param tapApi
     *            The {@link AutomaticsTapApi} reference.
     * @param parameter
     *            dmcli parameter to addtable
     * @return The required value.
     * @Refactor Athira
     */
    public static boolean addTableUsingDmcliCommand(Dut device, AutomaticsTapApi tapApi, String parameter) {
	LOGGER.debug("ENTERING METHOD addTableUsingDmcliCommand");
	String dmcliCommand = null;
	String dmcliResponse = null;
	Boolean status = false;
	try {
	    dmcliCommand = BroadBandCommonUtils.concatStringUsingStringBuffer(
		    BroadBandCommandConstants.DMCLI_PREFIX_TO_ADD_TABLE, AutomaticsConstants.SPACE, parameter);
	    LOGGER.debug("Dmcli Set Command to Execute is:" + dmcliCommand);
	    dmcliResponse = tapApi.executeCommandUsingSsh(device, dmcliCommand);
	    if (CommonMethods.isNotNull(dmcliResponse)) {
		status = CommonUtils.isGivenStringAvailableInCommandOutput(dmcliResponse,
			BroadBandTraceConstants.DMCLI_OUTPUT_EXECUTION_SUCCEED);
	    } else {
		throw new FailedTransitionException(GeneralError.SSH_CONNECTION_FAILURE,
			"Unable to connect to Device via SSH command or Device not responding to dmcli command for parameter : "
				+ parameter);
	    }
	} catch (Exception exception) {
	    LOGGER.error("Exception occurred while adding table using dmcli command");
	}
	LOGGER.debug("ENDING METHOD addTableUsingDmcliCommand");
	return status;
    }
	/**
	 * Utility method to set the WebPA parameter using Dmcli command. Before
	 * executing the command, it converts to dmcli compatible data type and
	 * parameter index.
	 * 
	 * @param device    The device to be used for execution.
	 * @param tapEnv  The {@link AutomaticsTapApi} instance.
	 * @param parameter The WebPA parameter used for query.
	 * @param dataType  The WebPA data type.
	 * @param value     The value to be set.
	 * @return True if parameter set properly.
	 */
	public static boolean setWebPaParameterValueUsingDmcliCommand(Dut device, AutomaticsTapApi tapEnv, String parameter,
			int dataType, String value) {
		boolean status = false;
		String dmcliDatatype = convertWebPaDataTypeToDmcliCompatibleDataType(dataType);
		String tr181parameter = convertRdkbWebPaWiFiParameterIndexToDmcliParameterIndex(parameter);

		if (!dmcliDatatype.equalsIgnoreCase(BroadBandTestConstants.DMCLI_SUFFIX_TO_SET_INVALID_PARAMETER)) {
			status = setParameterValueUsingDmcliCommand(device, tapEnv, tr181parameter, dmcliDatatype, value);
		}
		return status;
	}
	/**
	 * Utility method to convert the WebPA compatible data type to Dmcli compatible
	 * data type.
	 * 
	 * @param dataType The WebPA data type.
	 * @return Dmcli compatible data type.
	 */
	private static String convertWebPaDataTypeToDmcliCompatibleDataType(int dataType) {
		String dmcliDatatype = null;

		switch (dataType) {
		case BroadBandTestConstants.CONSTANT_0:
			dmcliDatatype = BroadBandTestConstants.DMCLI_SUFFIX_TO_SET_STRING_PARAMETER;
			break;
		case BroadBandTestConstants.CONSTANT_1:
			dmcliDatatype = BroadBandTestConstants.DMCLI_SUFFIX_TO_SET_INT_PARAMETER;
			break;
		case BroadBandTestConstants.CONSTANT_2:
			dmcliDatatype = BroadBandTestConstants.DMCLI_SUFFIX_TO_SET_UINT_PARAMETER;
			break;
		case BroadBandTestConstants.CONSTANT_3:
			dmcliDatatype = BroadBandTestConstants.DMCLI_SUFFIX_TO_SET_BOOLEAN_PARAMETER;
			break;
		case BroadBandTestConstants.CONSTANT_4:
			dmcliDatatype = BroadBandTestConstants.DMCLI_SUFFIX_TO_SET_DATETIME_PARAMETER;
			break;
		default:
			dmcliDatatype = BroadBandTestConstants.DMCLI_SUFFIX_TO_SET_INVALID_PARAMETER;
			break;
		}
		return dmcliDatatype;
	}
    
}

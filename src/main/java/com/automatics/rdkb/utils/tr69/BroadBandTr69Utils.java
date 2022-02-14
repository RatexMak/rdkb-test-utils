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
package com.automatics.rdkb.utils.tr69;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.core.SupportedModelHandler;
import com.automatics.device.Dut;
import com.automatics.enums.TR69ParamDataType;
import com.automatics.exceptions.TestException;
import com.automatics.providers.tr69.Parameter;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.test.AutomaticsTestBase;
import com.automatics.utils.AutomaticsUtils;
import com.automatics.utils.CommonMethods;
import com.automatics.webpa.WebPaParameter;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.wifi.BroadBandWiFiUtils;
import com.automatics.rdkb.utils.BroadBandRfcFeatureControlUtils;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;
import com.automatics.utils.AutomaticsPropertyUtility;

public class BroadBandTr69Utils extends AutomaticsTestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandTr69Utils.class);
    
    /**
     * Utility method to execute TR-69 using ACS Rest API.
     * 
     * @param device
     *            The device to be validated
     * @param parameter
     *            The TR-181 parameter to be queried.
     * @return The value corresponding to TR-181 parameter.
     */
    public static String executeTr69ParameterCommand(AutomaticsTapApi tapEnv, Dut device, String parameterName) {

	String parameterValue = null;

	// String parameterName = null;
	// List<Parameter> response = getTR69ParameterValueUsingRestApi(device, new String[] { parameter });
	String response = tapEnv.executeTr69Command(device, new String[]{parameterName});

	if (CommonMethods.isNotNull(response)) {
	    //parameterValue = response.get(0).getParamValue();
	    LOGGER.info("VALUE FOR PARAMTER " + parameterName + " RETRIEVED FROM TELESCOPIC REST API = "
		    + response);
	}
	return response;
    }
    
    /**
     * Helper method to set Tr09 parameter using rest api
     * 
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param device
     *            Dut instance
     * @param paramName
     *            name of TR-181 parameter to be set
     * @param dataType
     *            Data type of the parameter.
     * @param value
     *            value to be set
     * @return true, if rest api responds success message
     * @author Praveenkumar Paneerselvam
     */
    public static boolean setTR69ParameterValueUsingRestApi(AutomaticsTapApi tapEnv, Dut device, String paramName,
	    TR69ParamDataType dataType, String value) {
	LOGGER.debug("STARTING METHOD: setTR69ParameterValueUsingRestApi");
	boolean status = false;
	Parameter setParam = new Parameter();
	setParam.setDataType(dataType.get());
	setParam.setParamName(paramName);
	setParam.setParamValue(value);
	if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
	    BroadBandCommonUtils.getAtomSyncUptimeStatus(device, tapEnv);
	}
	List<Parameter> parameters = new ArrayList<Parameter>();
	parameters.add(setParam);
	String setResponse = Tr69RestAPIUtils.setTR69ParameterValueUsingRestApi(tapEnv, device, parameters);
	LOGGER.info("TR 69 response is - " + setResponse);
	status = BroadBandTestConstants.SUCCESS_TXT.equalsIgnoreCase(setResponse);
	LOGGER.info("Is value set successfully using TR-069 for the parameter " + paramName + " - " + status);
	LOGGER.debug("ENDING METHOD: setTR69ParameterValueUsingRestApi ");
	return status;
    }
    
    /**
     * Utility method used to enable or verify the tr069 configuration
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @return status True- Configuration enabled or verified successfully
     */
    public static boolean enableOrVerifyTheTr69Configuration(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug("Starting Method : enableOrVerifyTheTr69Configuration()");
	boolean status = false;
	WebPaParameter webPaParam = null;
	String response = null;
	Map<String, String> webPaResponse = new HashMap<>();
	List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
	boolean cwmpStatus = false;
	try {
	    LOGGER.info("Verifying TR069 support enable condition");
	    try {
		response = tapEnv.executeWebPaCommand(device, BroadBandWebPaConstants.WEBPA_PARAM_TR69_SUPPORT_ENABLE);
		LOGGER.info("response of tr069 param from webpa: " + response);
		status = BroadBandRfcFeatureControlUtils.enableOrDisableFeatureByRFC(tapEnv, device,
			BroadBandTestConstants.TR069_SUPPORT, true);
	    } catch (Exception e) {
		LOGGER.info("Exception occured while verifying TR069 support param" + e.getMessage());
	    }
	    LOGGER.info("Enabled TR069support parameter");
	    try {
		cwmpStatus = BroadBandCommonUtils.getWebPaValueAndVerify(device, tapEnv,
			BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_ENABLECWMP,
			BroadBandTestConstants.FALSE);
	    } catch (TestException exception) {
		LOGGER.error(exception.getMessage());
	    }
	    if (validateCorrectAcsURL(device, tapEnv) && validateShortAcsPasswrod(device, tapEnv) && cwmpStatus) {
		/** 1.Correct ACS URL & Short Password ***/
		LOGGER.info("Scenario 1 : Correct ACS URL & Short Password");
		webPaParam = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			BroadBandWebPaConstants.WEBPA_PARAM_ALLOW_FIREWALL_HOLE_ALLOW_PORTS,
			BroadBandTestConstants.TRUE, WebPaDataTypes.BOOLEAN.getValue());
		webPaParameters.add(webPaParam);
		webPaParam = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
			BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_ENABLECWMP,
			BroadBandTestConstants.TRUE, WebPaDataTypes.BOOLEAN.getValue());
		webPaParameters.add(webPaParam);
		webPaResponse = BroadBandWebPaUtils.validateMulitpleWebpaResponse(device, tapEnv, webPaParameters);
		status = !webPaResponse.isEmpty() && (webPaResponse.size() == webPaParameters.size());
		if (status) {
		    status = validateTr69Communication(device, tapEnv);
		    if (!status && stopAndStartTr069Process(device, tapEnv)) {
			status = validateTr69Communication(device, tapEnv);
		    }
		}
	    } else if (!validateCorrectAcsURL(device, tapEnv) && validateShortAcsPasswrod(device, tapEnv)) {
		/** 2.ANY ACS URL & Short Password ***/
		LOGGER.info("Scenario 2 : ANY ACS URL & Short Password");
		if (BroadBandCommonUtils.performFactoryResetWebPaByPassingTriggerTime(tapEnv, device,
			BroadBandTestConstants.EIGHT_MINUTE_IN_MILLIS)) {
		    status = setAcsConfigurationOnDevice(device, tapEnv, true)
			    && validateTr69Communication(device, tapEnv);
		    BroadBandWiFiUtils.reactivateDeviceUsingWebpaOrSnmp(tapEnv, device);
		    if (!status && stopAndStartTr069Process(device, tapEnv)) {
			status = validateTr69Communication(device, tapEnv);
		    }
		} else {
		    LOGGER.info("Scenario 2 : Failed to perform factory reset");
		}
	    } else if (!validateCorrectAcsURL(device, tapEnv) && !validateShortAcsPasswrod(device, tapEnv)) {
		/** 3.ANY ACS URL & Long Password ***/
		LOGGER.info("Scenario 3 : ANY ACS URL & Long Password");
		status = setAcsConfigurationOnDevice(device, tapEnv, true) && validateTr69Communication(device, tapEnv);
		if (!status) {
		    if (BroadBandCommonUtils.performFactoryResetWebPaByPassingTriggerTime(tapEnv, device,
			    BroadBandTestConstants.EIGHT_MINUTE_IN_MILLIS)) {
			status = setAcsConfigurationOnDevice(device, tapEnv, true)
				&& validateTr69Communication(device, tapEnv);
			BroadBandWiFiUtils.reactivateDeviceUsingWebpaOrSnmp(tapEnv, device);
			if (!status && stopAndStartTr069Process(device, tapEnv)) {
			    status = validateTr69Communication(device, tapEnv);
			}
		    } else {
			LOGGER.info("Scenario 3 : Failed to perform factory reset");
		    }
		}
	    } else {
		LOGGER.info("Scenario 4 : ACS Configuration enabled by default");
	    }
	} catch (Exception exception) {
	    LOGGER.error("Failed to enableOrVerifyTheTr69Configuration:" + exception.getMessage());
	}
	LOGGER.debug("ENDING METHOD : enableOrVerifyTheTr69Configuration");
	return status;
    }
    
    /**
     * Utility method used to validate the password length(Shot or new Password length will be 12 to 15)
     * 
     * @param Dut
     *            {@link device}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @return True - Updated password is present, Else-False
     */
    public static boolean validateShortAcsPasswrod(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug("STARTING METHOD : validateShortAcsPasswrod()");
	boolean status = false;
	String response = null;
	try {
	    response = tapEnv.executeWebPaCommand(device,
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_PASSWORD);
	    status = CommonMethods.isNotNull(response)
		    && (response.length() >= BroadBandTestConstants.CONSTANT_12 && response.length() < BroadBandTestConstants.CONSTANT_20);
	} catch (Exception e) {
	    LOGGER.error("Failed to verify the " + BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_PASSWORD
		    + " value as expected" + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : validateShortAcsPasswrod()");
	return status;
    }
    
    /**
     * Utility method used to validate TR069 communication
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @return True - Tr069 communication success,Else-False
     */
    public static boolean validateTr69Communication(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug("STARTING METHOD : validateTr69Communication()");
	boolean status = false;
	boolean isSslCertFailure = false;
	try {
	    status = CommonMethods.isNotNull(BroadBandCommonUtils.searchLogFiles(tapEnv, device,
		    BroadBandTestConstants.ACS_REQUEST_COMPLETE, BroadBandTestConstants.RDKLOGS_LOGS_TR69LOG_TXT_0,
		    BroadBandTestConstants.TWO_MINUTE_IN_MILLIS, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS))
		    && CommonMethods.isNotNull(BroadBandCommonUtils
			    .searchLogFiles(tapEnv, device,
				    BroadBandTraceConstants.OPENSSL_SUCCESS_CERTIFICATE_VERIFICATION,
				    BroadBandTestConstants.RDKLOGS_LOGS_TR69LOG_TXT_0,
				    BroadBandTestConstants.TWO_MINUTE_IN_MILLIS,
				    BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS))
		    && verifySerialNumberUsingTr069(device, tapEnv);
	    if (!status) {
		isSslCertFailure = CommonMethods.isNotNull(BroadBandCommonUtils.searchLogFiles(tapEnv, device,
			BroadBandTraceConstants.OPENSSL_FAILED_CERTIFICATE_VERIFICATION,
			BroadBandTestConstants.RDKLOGS_LOGS_TR69LOG_TXT_0, BroadBandTestConstants.TWO_MINUTE_IN_MILLIS,
			BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
		if (isSslCertFailure) {
		    LOGGER.error("Log information " + BroadBandTraceConstants.OPENSSL_FAILED_CERTIFICATE_VERIFICATION
			    + " is available in " + BroadBandTestConstants.RDKLOGS_LOGS_TR69LOG_TXT_0);
		}
	    }
	} catch (Exception e) {
	    LOGGER.error("Failed to validate the TR069 communication" + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : validateTr69Communication()");
	return status;
    }
    
    /**
     * Utility method used to get the serial number using TR069
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @return status True- Serial number retrieved successfully,Else-False
     */
    public static boolean verifySerialNumberUsingTr069(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug("Starting Method : verifySerialNumberUsingTr069()");
	boolean status = false;
	try {
	    String tr69DeviceSerialNumber = BroadBandTr69Utils.executeTr69ParameterCommand(tapEnv, device,
		    BroadBandWebPaConstants.TR69_PARAM_SERIAL_NUMBER);
	    String manufacturerSerialNumber = device.getSerialNumber();
	    status = CommonMethods.isNotNull(tr69DeviceSerialNumber)
		    && CommonMethods.isNotNull(manufacturerSerialNumber)
		    && BroadBandCommonUtils.patternSearchFromTargetString(tr69DeviceSerialNumber.trim(),
			    manufacturerSerialNumber.trim());
	} catch (Exception exception) {
	    LOGGER.error("Failed to verifySerialNumberUsingTr069:" + exception.getMessage());
	}
	LOGGER.debug("ENDING METHOD : verifySerialNumberUsingTr069()");
	return status;
    }
    
    /**
     * Utility method to kill and verify the CcspTr069PaSsp process and it should be restarted immediately
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @return status . True -Process killed and restarted,Else False
     */
    public static boolean stopAndStartTr069Process(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug("Starting Method : killAndVerifyTr069Process()");
	boolean status = false;
	String processIdBeforeKill = null;
	String cmdToKill = null;
	String processIdAfterKill = null;
	String response = null;
	String processName = BroadBandTestConstants.PROCESS_NAME_CCSPTR069PASSP;
	try {
	    if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
		LOGGER.info("Executing command on atom based device model");
		processIdBeforeKill = CommonMethods.getPidOfProcess(device, tapEnv, processName).trim();
		LOGGER.info("Before Process Id : " + processIdBeforeKill);
		cmdToKill = BroadBandCommonUtils.concatStringUsingStringBuffer(BroadBandTestConstants.CMD_KILLALL_11,
			BroadBandTestConstants.SINGLE_SPACE_CHARACTER, processName);
		tapEnv.executeCommandUsingSsh(device, cmdToKill);
		long startTime = System.currentTimeMillis();
		do {
		    processIdAfterKill = CommonMethods.getPidOfProcess(device, tapEnv, processName).trim();
		    LOGGER.info("After Process Id : " + processIdAfterKill);
		    status = CommonMethods.isNotNull(processIdBeforeKill)
			    && CommonMethods.isNotNull(processIdAfterKill)
			    && !processIdAfterKill.equals(processIdBeforeKill);
		} while (!status
			&& (System.currentTimeMillis() - startTime) < BroadBandTestConstants.THREE_MINUTE_IN_MILLIS
			&& BroadBandCommonUtils.hasWaitForDuration(tapEnv,
				BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
	    } else {
		tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_SYSTEM_STOP_START_CCSPTR069PASSP
			.replace(BroadBandTestConstants.STRING_REPLACE, BroadBandTestConstants.STRING_STOP));
		response = CommonMethods.getPidOfProcess(device, tapEnv, processName);
		status = CommonMethods.isNull(response);
		LOGGER.info(processName + " Process stopped :" + status);
		status = false;
		tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
		tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_SYSTEM_STOP_START_CCSPTR069PASSP
			.replace(BroadBandTestConstants.STRING_REPLACE, BroadBandTestConstants.STRING_START));
		tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
		response = CommonMethods.getPidOfProcess(device, tapEnv, processName);
		status = CommonMethods.isNotNull(response);
		LOGGER.info(processName + " Process restarted :" + status);
	    }
	} catch (Exception exception) {
	    LOGGER.error("Failed to kill TR069 process:" + exception.getMessage());
	}
	LOGGER.debug("Ending Method : killAndVerifyTr069Process()");
	return status;
    }
    
    /**
     * Utility method used to set the ACS configuration using webpa
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param isdefault
     *            True-Default ACS url,Else- Correct ACS url
     * @return True-Expected configuration set successful,Else-False
     */
    public static boolean setAcsConfigurationOnDevice(Dut device, AutomaticsTapApi tapEnv, boolean isdefault) {
	LOGGER.debug("STARTING METHOD : setAcsConfigurationOnDevice");
	boolean status = false;
	WebPaParameter webPaParam = null;
	Map<String, String> webPaResponse = new HashMap<>();
	List<WebPaParameter> webPaParameters = new ArrayList<WebPaParameter>();
	try {
	    webPaParam = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_URL,
		    isdefault ? AutomaticsPropertyUtility
			    .getProperty(BroadBandTestConstants.PROP_KEY_TR69_DEFAULT_ACS_URL)
			    : AutomaticsPropertyUtility
				    .getProperty(BroadBandTestConstants.PROP_KEY_TR69_CORRECT_ACS_URL),
		    WebPaDataTypes.STRING.getValue());
	    webPaParameters.add(webPaParam);
	    webPaParam = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_PARTNER_CERT_LOCATION,
		    BroadBandTestConstants.STRING_EXPECTED_TR69_CERTLOCATION, WebPaDataTypes.STRING.getValue());
	    webPaParameters.add(webPaParam);
	    webPaParam = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_ALLOW_FIREWALL_HOLE_ALLOW_PORTS, BroadBandTestConstants.TRUE,
		    WebPaDataTypes.BOOLEAN.getValue());
	    webPaParameters.add(webPaParam);
	    webPaParam = BroadBandWebPaUtils.generateWebpaParameterWithValueAndType(
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_ENABLECWMP,
		    BroadBandTestConstants.TRUE, WebPaDataTypes.BOOLEAN.getValue());
	    webPaParameters.add(webPaParam);
	    webPaResponse = BroadBandWebPaUtils.validateMulitpleWebpaResponse(device, tapEnv, webPaParameters);
	    status = !webPaResponse.isEmpty() && (webPaResponse.size() == webPaParameters.size());
	} catch (Exception exception) {
	    LOGGER.error("Failed to set ACS configuration" + exception.getMessage());
	}
	LOGGER.debug("ENDING METHOD : setAcsConfigurationOnDevice");
	return status;
    }
    /**
     * Utility method used to validate the Correct ACS server URL
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @return True- Expected url is present, Else -False
     */
    public static boolean validateCorrectAcsURL(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug("STARTING METHOD : validateCorrectAcsURL()");
	boolean status = false;
	try {
	    status = BroadBandCommonUtils.getWebPaValueAndVerify(device, tapEnv,
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_URL,
		    AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_TR69_CORRECT_ACS_URL));
	} catch (Exception e) {
	    LOGGER.error("Failed to verify the " + BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_MANAGEMENTSERVER_URL
		    + " value as "
		    + AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_TR69_CORRECT_ACS_URL)
		    + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : validateCorrectAcsURL()");
	return status;
    }
}

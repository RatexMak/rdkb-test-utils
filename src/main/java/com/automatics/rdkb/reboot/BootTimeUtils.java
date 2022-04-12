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

package com.automatics.rdkb.reboot;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Dut;
import com.automatics.enums.TR69ParamDataType;
import com.automatics.exceptions.TestException;
import com.automatics.providers.tr69.Parameter;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandConnectedClientTestConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.reboot.BootTime.BootTimePatterns;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;

public class BootTimeUtils {

    /** SLF4j logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BootTimeUtils.class);

    /**
     * Method to verify status of Interface brlan0
     * 
     * @param device
     *            Set top to be used
     * @return Interface status (UP or DOWN) return status of interface brlan0
     * 
     * @author prashant.mishra12
     * @refactor Said Hisham
     */
    public static String verifyInterfaceBrlan0UpStatus(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug("STARTING METHOD : verifyInterfaceBrlan0UpStatus");
	// Variable declaration starts
	String interfaceStatus = "";
	String response = "";
	String errorMessage = "";
	// Variable declaration ends
	try {
	    response = tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_BRLAN0_STATUS);
	    if (response.contains(BroadBandTestConstants.SBIN_FOLDER_NOT_FOUND_ERROR)) {
		response = tapEnv.executeCommandUsingSsh(device, BroadBandCommandConstants.CMD_BRLAN0_STATUS
			.replace(BroadBandTestConstants.SBIN_FOLDER_CONSTANT, AutomaticsConstants.EMPTY_STRING));
	    }
	    if (CommonMethods.patternMatcher(response, BroadBandTestConstants.STATE_UP_KEYWORD_INTERFACE_BRLAN_0)) {
		interfaceStatus = BroadBandConnectedClientTestConstants.RADIO_STATUS_UP;
	    } else if (CommonMethods.patternMatcher(response,
		    BroadBandTestConstants.STATE_DOWN_KEYWORD_INTERFACE_BRLAN_0)) {
		interfaceStatus = BroadBandConnectedClientTestConstants.RADIO_STATUS_DOWN;
	    }
	} catch (Exception exception) {
	    errorMessage = exception.getMessage();
	    LOGGER.error("Exception occured while getting status (UP or DOWN) for Interface brlan0.");
	    LOGGER.error(errorMessage);
	}
	LOGGER.debug("ENDING METHOD : verifyInterfaceBrlan0UpStatus");
	return interfaceStatus;
    }

    /**
     * Method to verify assigned Ip address(ipv4 & ipv6) to interface brlan0 after selfheal process
     * 
     * @param device
     *            Set top to be used
     * @param ipAddressType
     *            Depends on which Ip address need to be verified (Ipv4 OR Ipv6)
     * @return true if Ip address(Ipv4 or Ipv6 depends on input ipAddressType) assigned successfully to interface brlan0
     * @refactor Said Hisham
     */
    public static boolean verifyIpAddressOfInterfaceBrlan0AfterSelfHeal(Dut device, String ipAddressType,
	    AutomaticsTapApi tapEnv) {
	LOGGER.debug("STARTING METHOD : verifyIpAddressOfInterfaceBrlan0AfterSelfHeal");
	// Variable declaration starts
	boolean isIpAddressAssigned = false;
	String response = null;
	String errorMessage = null;
	int count = 0;
	// Variable declaration ends
	try {
	    do {
		response = tapEnv.executeCommandUsingSsh(device, BroadBandTestConstants.IFCONFIG_BRLAN);
		if (ipAddressType.equalsIgnoreCase(BroadBandTestConstants.String_CONSTANT_IPV4)) {
		    isIpAddressAssigned = CommonMethods.patternMatcher(response,
			    BroadBandTestConstants.INET_V4_ADDRESS_PATTERN);
		} else if (ipAddressType.equalsIgnoreCase(BroadBandTestConstants.String_CONSTANT_IPV6)) {
		    isIpAddressAssigned = CommonMethods.patternMatcher(response,
			    BroadBandTestConstants.INET_V6_ADDRESS_PATTERN);
		}
		if (isIpAddressAssigned) {
		    LOGGER.info(ipAddressType
			    + " address for interface brlan0 retrieved successfully after interface self heal.");
		    break;
		} else {
		    LOGGER.error("UNABLE TO RETRIEVE " + ipAddressType.toUpperCase()
			    + " ADDRESS AFTER SELF HEAL. WILL TRY AGAIN AFTER WAITING FOR 30 SECONDS.");
		    count++;
		    tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		}
	    } while (count < BroadBandTestConstants.INT_VALUE_THIRTY);
	} catch (Exception exception) {
	    errorMessage = exception.getMessage();
	    LOGGER.error("Exception occured while getting ip address for Interface brlan0 after selfheal.");
	    LOGGER.error(errorMessage);
	}
	LOGGER.debug("ENDING METHOD : verifyIpAddressOfInterfaceBrlan0AfterSelfHeal");
	return isIpAddressAssigned;
    }
    
    /**
     * Verify the Device is up after reboot from console logs
     * 
     * @param device
     *            Device under test
     * @return True if obtained the reboot reason
     * 
     * @refactor Athira
     */
    public static boolean verifyDeviceIsUpAfterRebootTelemetryMarkerFromConsoleLogs(AutomaticsTapApi tapApi, Dut device) {

	boolean isTelemetryMarkerPresent = false;
	String deviceLogFileName = BroadBandTestConstants.LOGS_CONSOLE_TXT_0;
	if (CommonMethods.isAtomSyncAvailable(device, tapApi)) {
	    deviceLogFileName = BroadBandTestConstants.RDKLOGS_LOGS_ARM_CONSOLE_0;
	}

	isTelemetryMarkerPresent = verifyRebootReasonFromAnyLogFile(tapApi, device, "RDKB_REBOOT", deviceLogFileName,
		BroadBandTestConstants.RDKB_REBOOT_REASON_ARM_CONSOLE);

	return isTelemetryMarkerPresent;
    }
    
    /**
     * Verify the reboot reason from Arm console logs
     * 
     * @param device
     *            Device under test
     * @param grepString
     *            String to be used for grep
     * @param fileName
     *            Full file path
     * @param matchingString
     *            String for validating matching
     * 
     * @return True if obtained the reboot reason
     * 
     * @refactor Athira
     */
    public static boolean verifyRebootReasonFromAnyLogFile(AutomaticsTapApi tapApi, Dut device, String grepString,
	    String fileName, String matchingString) {

	LOGGER.debug("STARTING METHOD: verifyRebootReasonFromAnyLogFile()");

	boolean rebootLogStatus = false;
	String response = null;
	if (!matchingString.contains(BroadBandTestConstants.STATUS_FAILED)
		&& !matchingString.contains(BroadBandTestConstants.UNKNOWN_REBOOT_REASON)) {
	    String command = "grep -i " + grepString + " " + fileName + " | tail -1";

	    try {
		response = tapApi.executeCommandUsingSsh(device, command);
		if (CommonMethods.isNotNull(response) && response.contains(matchingString)) {
		    LOGGER.info("Succesfully obtained reboot log reson from logs file " + fileName);
		    rebootLogStatus = true;
		}
	    } catch (Exception expetion) {
		LOGGER.error("Failed to get the reboot reson from " + fileName);
		rebootLogStatus = false;
	    }

	    LOGGER.info("Logs status from file " + fileName + " - " + rebootLogStatus);
	    LOGGER.debug("ENDING METHOD: verifyRebootReasonFromAnyLogFile()");
	} else {
	    LOGGER.info("Reboot reason contains " + matchingString);
	}
	return rebootLogStatus;
    }
    
    /**
     * Verify the bootup time from boot logs
     * 
     * @param device
     *            Device under test
     * @return True if obtained the reboot reason
     * 
     * @refactor Athira
     */
    public static void verifyBootupTimeFromBootLog(AutomaticsTapApi tapApi, Dut device) throws TestException {

	LOGGER.debug("STARTING METHOD: verifyBootupTimeFromBootLog()");
	StringBuffer failedValues = new StringBuffer();

	try {

	    BootTime bootTimeValues = BootTimeUtils.getBootTimeValues(device, tapApi);
	    if (!(DeviceModeHandler.isDSLDevice(device) || CommonMethods.isRunningEthwanMode())) {
		if (isBootValueNegative(bootTimeValues.getBootToMocaUptime())
			|| bootTimeValues.getBootToMocaUptime() > BroadBandTestConstants.BOOT_TO_MOCA_UP_MAX_VALUE) {
		    failedValues.append("\n boot_to_MOCA_uptime : Actual - " + bootTimeValues.getBootToMocaUptime()
			    + " Max : " + BroadBandTestConstants.BOOT_TO_MOCA_UP_MAX_VALUE);
		}
	    }
	    if (isBootValueNegative(bootTimeValues.getBootToWifiUptime())
		    || bootTimeValues.getBootToWifiUptime() > BroadBandTestConstants.BOOT_TO_WIFI_UP_MAX_VALUE) {
		failedValues.append("\n boot_to_WIFI_uptime : Actual - " + bootTimeValues.getBootToWifiUptime()
			+ " Max : " + BroadBandTestConstants.BOOT_TO_WIFI_UP_MAX_VALUE);
	    }
	    if (!(DeviceModeHandler.isBusinessClassDevice(device)
		    || DeviceModeHandler.isDSLDevice(device))) {

		if (isBootValueNegative(bootTimeValues.getLnfSSIDUptime()) || bootTimeValues
			.getLnfSSIDUptime() > BroadBandTestConstants.BOOT_TO_LNF_SSID_UP_TIME_MAX_VALUE) {
		    failedValues.append("\n boot_to_LnF_SSID_uptime : Actual - " + bootTimeValues.getLnfSSIDUptime()
			    + "  Max : " + BroadBandTestConstants.BOOT_TO_LNF_SSID_UP_TIME_MAX_VALUE);
		}
	    }
	    if (isBootValueNegative(bootTimeValues.getBootToEthUptime())
		    || bootTimeValues.getBootToEthUptime() > BroadBandTestConstants.BOOT_TO_ETH_UP_MAX_VALUE) {
		failedValues.append("\n boot_to_ETH_uptime : Actual - " + bootTimeValues.getBootToEthUptime()
			+ "  Max : " + BroadBandTestConstants.BOOT_TO_ETH_UP_MAX_VALUE);
	    }

	    if (!(DeviceModeHandler.isFibreDevice(device)
		    || DeviceModeHandler.isDSLDevice(device) || CommonMethods.isRunningEthwanMode())
		    && isBootValueNegative(bootTimeValues.getCmOperationalTime())
		    || bootTimeValues.getCmOperationalTime() > BroadBandTestConstants.CM_DHCP_OPERATIONAL_MAX_VALUE) {
		failedValues.append("\n CM is Operational : Actual - " + bootTimeValues.getCmOperationalTime()
			+ "  Max : " + BroadBandTestConstants.CM_DHCP_OPERATIONAL_MAX_VALUE);
	    }

	    if (!(DeviceModeHandler.isFibreDevice(device)
		    || DeviceModeHandler.isDSLDevice(device) || CommonMethods.isRunningEthwanMode())
		    && isBootValueNegative(bootTimeValues.getDownstreamLockSuccessTime())
		    || bootTimeValues
			    .getDownstreamLockSuccessTime() > BroadBandTestConstants.DOWNSTREAM_LOCK_MAX_VALUE) {
		failedValues
			.append("\n Downstream Lock Success : Actual - " + bootTimeValues.getDownstreamLockSuccessTime()
				+ "  Max : " + BroadBandTestConstants.DOWNSTREAM_LOCK_MAX_VALUE);
	    }
	    if (!DeviceModeHandler.isDSLDevice(device) || CommonMethods.isRunningEthwanMode()) {
		if (isBootValueNegative(bootTimeValues.getSnmpSubAgentUptime()) || bootTimeValues
			.getSnmpSubAgentUptime() > BroadBandTestConstants.BOOT_TO_SNMP_SUB_AGENT_UP_TIME_MAX_VALUE) {
		    failedValues.append("\n boot_to_snmp_subagent_uptime : Actual - " + bootTimeValues.getTr69Uptime()
			    + "  Max : " + BroadBandTestConstants.BOOT_TO_SNMP_SUB_AGENT_UP_TIME_MAX_VALUE);
		}
	    }
	    if (isBootValueNegative(bootTimeValues.getWanUptime())
		    || bootTimeValues.getWanUptime() > BroadBandTestConstants.BOOT_TO_WAN_UP_TIME_MAX_VALUE) {
		failedValues.append("\n boot_to_wan_uptime : Actual - " + bootTimeValues.getTr69Uptime() + "  Max : "
			+ BroadBandTestConstants.BOOT_TO_WAN_UP_TIME_MAX_VALUE);
	    }
	    if (isBootValueNegative(bootTimeValues.getWebpaUptime())
		    || bootTimeValues.getWebpaUptime() > BroadBandTestConstants.BOOT_TO_WEBPA_UP_TIME_MAX_VALUE) {
		failedValues.append("\n boot_to_WEBPA_READY_uptime : Actual - " + bootTimeValues.getWebpaUptime()
			+ "  Max : " + BroadBandTestConstants.BOOT_TO_WEBPA_UP_TIME_MAX_VALUE);
	    }

	    if (CommonMethods.isAtomSyncAvailable(device, tapApi)) {

		if (isBootValueNegative(bootTimeValues.getCmDchpEndTime())
			|| bootTimeValues.getCmDchpEndTime() > BroadBandTestConstants.CM_DHCP_END_MAX_VALUE) {
		    failedValues.append("\n CM DHCP END : Actual - " + bootTimeValues.getCmDchpEndTime() + "  Max : "
			    + BroadBandTestConstants.CM_DHCP_END_MAX_VALUE);
		}

		if (isBootValueNegative(bootTimeValues.getCmDhcpStartTime())
			|| bootTimeValues.getCmDhcpStartTime() > BroadBandTestConstants.CM_DHCP_START_MAX_VALUE) {
		    failedValues.append("\n CM DHCP START : Actual - " + bootTimeValues.getCmDhcpStartTime()
			    + "  Max : " + BroadBandTestConstants.CM_DHCP_START_MAX_VALUE);
		}
	    }
	    if (CommonMethods.isAtomSyncAvailable(device, tapApi)) {
		if (isBootValueNegative(bootTimeValues.getTr69Uptime())
			|| bootTimeValues.getTr69Uptime() > BroadBandTestConstants.BOOT_TO_TR69_UP_TIME_MAX_VALUE) {
		    failedValues.append("\n boot_to_tr069_uptime : Actual - " + bootTimeValues.getTr69Uptime()
			    + "  Max : " + BroadBandTestConstants.BOOT_TO_TR69_UP_TIME_MAX_VALUE);
		}
	    }
	} catch (Exception exception) {
	    LOGGER.error("Failed to verify the boot logs. " + exception.getMessage());
	    failedValues.append(exception.getMessage());
	}

	LOGGER.info("Boot log verification failures - " + failedValues.toString());

	// Checking if any failures occurred.
	if (CommonMethods.isNotNull(failedValues.toString())) {
	    throw new TestException("Failed to validate below boot parameters [ " + failedValues.toString() + " \n]");
	}

	LOGGER.debug("ENDING METHOD: verifyBootupTimeFromBootLog()");
    }
    
    /**
     * Method to get the boot time values from /rdklogs/logs/BootTime.log
     * 
     * @param device
     *            Device under test
     * @param tapApi
     *            {@code AutomaticsTapApi}
     * @return {@code BootTime}
     * @refactor Athira
     */
    public static BootTime getBootTimeValues(Dut device, AutomaticsTapApi tapApi) {

	/** Boot time value instance */
	BootTime bootTimeValues = null;

	/** Response from command */
	String response = null;

	LOGGER.debug("STARTING METHOD: getBootTimeValues()");
	try {
	    /** Getting Boot values from logs */
	    response = tapApi.executeCommandUsingSsh(device, BroadBandTestConstants.CAT_RDKLOGS_LOGS_BOOT_TIME_LOG);

	    if (CommonMethods.isNotNull(response)) {
		bootTimeValues = new BootTime();
		if (!DeviceModeHandler.isDSLDevice(device)) {
		    bootTimeValues.setBootToMocaUptime(getBootTimeParamValueFromResponse(response,
			    BroadBandTestConstants.RDKLOGS_LOGS_BOOTTIME_LOG, BootTimePatterns.BOOT_TO_MOCA_UPTIME));
		}
		bootTimeValues.setBootToEthUptime(getBootTimeParamValueFromResponse(response,
			BroadBandTestConstants.RDKLOGS_LOGS_BOOTTIME_LOG, BootTimePatterns.BOOT_TO_ETH_UPTIME));
		bootTimeValues.setBootToWifiUptime(getBootTimeParamValueFromResponse(response,
			BroadBandTestConstants.RDKLOGS_LOGS_BOOTTIME_LOG, BootTimePatterns.BOOT_TO_WIFI_UPTIME));

		if (!(DeviceModeHandler.isFibreDevice(device)
			|| DeviceModeHandler.isDSLDevice(device) || CommonMethods.isRunningEthwanMode())) {
		    bootTimeValues.setCmOperationalTime(getBootTimeParamValueFromResponse(response,
			    BroadBandTestConstants.RDKLOGS_LOGS_BOOTTIME_LOG, BootTimePatterns.CM_IS_OPERATIONAL_TIME));
		    bootTimeValues.setDownstreamLockSuccessTime(getBootTimeParamValueFromResponse(response,
			    BroadBandTestConstants.RDKLOGS_LOGS_BOOTTIME_LOG,
			    BootTimePatterns.DOWNSTREAM_LOCK_SUCCESS_TIME));
		}
		if (!(DeviceModeHandler.isDSLDevice(device) || CommonMethods.isRunningEthwanMode())) {
		    long snmpSubAgentUpTime = getBootTimeParamValueFromResponse(response,
			    BroadBandTestConstants.RDKLOGS_LOGS_BOOTTIME_LOG,
			    BootTimePatterns.BOOT_TO_SNMP_V2_SUBAGENT_UPTIME);
		    if (snmpSubAgentUpTime == -1) {
			snmpSubAgentUpTime = getBootTimeParamValueFromResponse(response,
				BroadBandTestConstants.RDKLOGS_LOGS_BOOTTIME_LOG,
				BootTimePatterns.BOOT_TO_SNMP_V3_SUBAGENT_UPTIME);
		    }
		    bootTimeValues.setSnmpSubAgentUptime(snmpSubAgentUpTime);
		}
		bootTimeValues.setWanUptime(getBootTimeParamValueFromResponse(response,
			BroadBandTestConstants.RDKLOGS_LOGS_BOOTTIME_LOG, BootTimePatterns.BOOT_TO_WAN_TIME));

		if (CommonMethods.isAtomSyncAvailable(device, tapApi)) {
		    bootTimeValues.setCmDchpEndTime(getBootTimeParamValueFromResponse(response,
			    BroadBandTestConstants.RDKLOGS_LOGS_BOOTTIME_LOG, BootTimePatterns.CM_DHCP_END_TIME));
		    bootTimeValues.setCmDhcpStartTime(getBootTimeParamValueFromResponse(response,
			    BroadBandTestConstants.RDKLOGS_LOGS_BOOTTIME_LOG, BootTimePatterns.CM_DHCP_START_TIME));
		}
		if (CommonMethods.isAtomSyncAvailable(device, tapApi)) {
		    bootTimeValues.setTr69Uptime(getBootTimeParamValueFromResponse(response,
			    BroadBandTestConstants.RDKLOGS_LOGS_BOOTTIME_LOG, BootTimePatterns.BOOT_TO_TR69_UPTIME));
		}
	    }
	    bootTimeValues.setWebpaUptime(getBootTimeParamValueFromResponse(response,
		    BroadBandTestConstants.RDKLOGS_LOGS_BOOTTIME_LOG, BootTimePatterns.BOOT_TO_WEBPA_TIME));

	} catch (Exception e) {
	    bootTimeValues = null;
	    LOGGER.info("Failed to get the boot values from log. " + e.getMessage());
	    throw new TestException("Failed to get the boot values from log. " + e.getMessage());
	}

	LOGGER.debug("ENDING METHOD: getBootTimeValues()");
	return bootTimeValues;
    }
    
    /**
     * 
     * Get boot parameter value from log response
     * 
     * @param response
     *            , Response from command
     * @param pattern
     *            , RegEx to get the value
     * @return value corresponds to the key
     * @refactor Athira
     */
    private static long getBootTimeParamValueFromResponse(String response, String file, BootTimePatterns pattern) {

	long value = -1;

	LOGGER.debug("STARTING METHOD: getBootTimeParamValueFromResponse()");
	String valueObtained = CommonMethods.patternFinder(response, pattern.getPattern());

	if (CommonMethods.isNotNull(valueObtained)) {
	    try {
		value = Long.parseLong(valueObtained);
	    } catch (NumberFormatException exec) {
		value = -1;
		LOGGER.error("Failed to get the value of " + pattern.getPattern() + "" + " from " + file + ". " + exec);
	    }
	} else {
	    LOGGER.error("Failed to get the value of " + pattern.getPattern() + " from " + file);
	    value = -1;
	}

	LOGGER.info("Value for " + pattern.name() + " is " + value);

	LOGGER.debug("ENDING METHOD: getBootTimeParamValueFromResponse()");
	return value;
    }
    
	/**
     * 
     * Check if the value is not negative and zero.
     * 
     * @param value
     *            Time value
     * @return True if the value is negative
     */
    public static boolean isBootValueNegative(long value) {
	boolean status = false;

	if (value <= 0) {
	    status = true;
	}

	return status;
    }
    
    /**
     * Verify the reboot reason from Webpa logs
     * 
     * @param device
     *            Device under test
     * @return True if obtained the reboot reason
     * @refactor Athira
     */
    public static boolean verifyRebootResonFromWebPaCommand(AutomaticsTapApi tapApi, Dut device) {
	boolean rebootStatus = false;
	LOGGER.debug("STARTING METHOD: verifyRebootResonFromWebPaCommand()");

	try {
	    String rebootReasonFromWebPa = tapApi.executeWebPaCommand(device,
		    BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_LAST_REBOOT_REASON);
	    LOGGER.info("The reboot reson from Webpa command - "
			    + rebootReasonFromWebPa);
	    if (CommonMethods.isNotNull(rebootReasonFromWebPa)
		    && rebootReasonFromWebPa.contains(BroadBandTestConstants.RDKB_REBOOT_REASON_TR69_REBOOT)) {
		rebootStatus = true;
	    }
	} catch (Exception expetion) {
	    LOGGER.error("Failed to get the reboot reson from Webpa command - "
		    + BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_LAST_REBOOT_REASON);
	    rebootStatus = false;
	}

	LOGGER.info("Reboot status from Webpa - " + rebootStatus);
	LOGGER.debug("ENDING METHOD: verifyRebootResonFromWebPaCommand()");

	return rebootStatus;
    }
    
    /**
     * Verify the reboot reason from Webpa command
     * 
     * @param device
     *            Device under test
     * @return True if obtained the reboot reason
     * @refactor Athira
     */
    public static boolean verifyRebootReasonFromWebpaOrPardusLogs(AutomaticsTapApi tapApi, Dut device) {
	boolean rebootReasonTelemetryMarker = verifyRebootReasonFromAnyLogFile(tapApi, device, "reboot_reason",
		BroadBandTestConstants.RDKLOGS_LOGS_WEBPA_TXT_0, BroadBandTestConstants.RDKB_REBOOT_REASON_TR69_REBOOT);

	if (!rebootReasonTelemetryMarker) {
	    rebootReasonTelemetryMarker = verifyRebootReasonFromAnyLogFile(tapApi, device, "reboot_reason",
		    BroadBandTestConstants.LOGS_PARODUS_TXT_0, BroadBandTestConstants.RDKB_REBOOT_REASON_TR69_REBOOT);
	}

	return rebootReasonTelemetryMarker;
    }
    
    /**
     * Verify the reboot reason from tr69 logs
     * 
     * @param device
     *            Device under test
     * @return True if obtained the reboot reason
     * @refactor Athira
     */
    public static boolean verifyRebootReasonFromTr69Logs(AutomaticsTapApi tapApi, Dut device) {

	return verifyRebootReasonFromAnyLogFile(tapApi, device, "RDKB_REBOOT",
		BroadBandTestConstants.RDKLOGS_LOGS_TR69LOG_TXT_0, BroadBandTestConstants.RDKB_REBOOT_REASON_TR69_LOG);
    } 
    
    /**
     * Rebooting the box using TR-181 parameter 'Device.X_CISCO_COM_DeviceControl.RebootDevice' via TR-69/ACS
     * 
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance.
     * @param device
     *            The device to be validated.
     * @return true if TR-181 parameter 'Device.X_CISCO_COM_DeviceControl.RebootDevice' via TR-69/ACS
     */
    public static boolean rebootDeviceUsingDeviceControlRebootTr69parameter(AutomaticsTapApi tapEnv, Dut device) {
		
    	String tr69Response = null;
		try {
			Parameter setParam = new Parameter();
		    
		    setParam.setDataType(TR69ParamDataType.STRING.get());
		    setParam.setParamName(BroadBandWebPaConstants.TR69_PARAM_REBOOT);
		    setParam.setParamValue("Device");
		    List<Parameter> parameters = new ArrayList<Parameter>();
		    parameters.add(setParam);
		    tr69Response = tapEnv.setTR69ParameterValues(device, parameters);
			LOGGER.info("tr69Response postreboot: " + tr69Response);

		} catch (Exception exception) {
		    // Log & Suppress the Exception.
		    LOGGER.error("EXCEPTION OCCURRED WHILE UPDATING THE VALUE FOR TR-69 PARAMETER: "
			    + exception.getMessage());
		}

	return CommonMethods.isNotNull(tr69Response)
		&& (tr69Response.toLowerCase().contains(BroadBandTestConstants.STRING_CONNECTION_STATUS.toLowerCase())
			|| tr69Response.toLowerCase().contains(AutomaticsConstants.OK.toLowerCase()));
    }
    
}

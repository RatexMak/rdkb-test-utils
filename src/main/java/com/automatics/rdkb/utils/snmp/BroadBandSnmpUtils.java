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
package com.automatics.rdkb.utils.snmp;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.constants.LinuxCommandConstants;
import com.automatics.constants.SnmpConstants;
import com.automatics.device.Dut;
import com.automatics.enums.ExecutionStatus;
import com.automatics.error.GeneralError;
import com.automatics.exceptions.FailedTransitionException;
import com.automatics.exceptions.TestException;
import com.automatics.providers.snmp.SnmpProvider;
import com.automatics.providers.snmp.SnmpProviderFactory;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandSnmpConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.rdkb.constants.BroadBandSnmpConstants.BROADBAND_WAREHOUSE_DOCSIS_SNMP_LIST;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;
import com.automatics.rdkb.utils.CommonUtils;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.snmp.SnmpCommand;
import com.automatics.snmp.SnmpDataType;
import com.automatics.snmp.SnmpParams;
import com.automatics.snmp.SnmpProtocol;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.AutomaticsUtils;
import com.automatics.utils.BeanUtils;
import com.automatics.utils.CommonMethods;

/**
 * Utility class for Broad Band specific SNMP utility. Common SNMP related Utilities are in {@link SnmpUtils} in
 * rdkv-utils, please refer before defining any new methods to avoid duplication.
 * 
 * @Refactor Athira, Alan_Bivera
 * 
 */
public class BroadBandSnmpUtils {
    /**
     * Logger instance for this particular class.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(BroadBandSnmpUtils.class);

    /**
     * Utility method to validate SNMP process is initialized in /rdklogs/logs/Snmplog.txt.0
     * 
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance. @param device The {@link Dut} instance. @return status. True-
     *            SNMP process initialized.Else-False
     * 
     * @exception
     */
    public static boolean validateSnmpProcessIsInitialized(AutomaticsTapApi tapEnv, Dut device) {
	LOGGER.info("STARTING METHOD: validateSnmpProcessIsInitialized");
	boolean status = false;
	long startTime = System.currentTimeMillis();
	try {
	    do {
		status = CommonMethods.isNotNull(BroadBandCommonUtils.searchLogFiles(tapEnv, device,
			BroadBandTraceConstants.LOG_MESSAGE_SNMP_INITIALIZED, BroadBandCommandConstants.LOG_FILE_SNMP));
	    } while (System.currentTimeMillis() - startTime < BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS && !status
		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
	} catch (Exception exception) {
	    LOGGER.error("Exception occurred while verifying the SNMP initialized :" + exception.getMessage());
	}

	LOGGER.info("ENDING METHOD: validateSnmpProcessIsInitialized");
	return status;
    }

    /**
     * Utility method to execute SNMP get with table index on ECM side.
     * 
     * @param AutomaticsTapApi
     *            The {@link AutomaticsTapApi} instance.
     * @param device
     *            The device to be queried.
     * @param mibOrOid
     *            The MIB or OID need to be queried.
     * @param tableIndex
     *            The table index.
     * @return Command output.
     */
    public static String snmpGetOnEcm(AutomaticsTapApi tapEnv, Dut device, String mibOrOid, String tableIndex) {

	String result = null;
	SnmpParams snmpParam = null;
	String snmpProtocol = System.getProperty(SnmpConstants.SYSTEM_PARAM_SNMP_VERSION,
		SnmpProtocol.SNMP_V2.toString());
	LOGGER.info("Current SNMP protocol: " + snmpProtocol);
	mibOrOid = mibOrOid + "." + tableIndex;
	LOGGER.info("mibOrOid to execute: " + mibOrOid);

	snmpParam = new SnmpParams();
	snmpParam.setSnmpCommand(SnmpCommand.GET);
	snmpParam.setSnmpCommunity("CUSTOM");
	snmpParam.setMibOid(mibOrOid.trim());
	snmpParam.setCommandOption("-t 30 ");

	LOGGER.info("snmpParam  is : " + snmpParam);
	result = tapEnv.executeSnmpCommand(device, snmpParam, tableIndex);
	LOGGER.info("snmpGetOnEcm OutPut is : " + result);
	for (int retryCount = 0; getRetries(result, snmpProtocol, retryCount); ++retryCount) {
	    LOGGER.info("Retrying snmp .Retry count :" + retryCount);
	    snmpParam = new SnmpParams();
	    snmpParam.setSnmpCommand(SnmpCommand.GET);
	    snmpParam.setSnmpCommunity("CUSTOM");
	    snmpParam.setMibOid(mibOrOid.trim());
	    snmpParam.setCommandOption("-t 30 ");

	    result = tapEnv.executeSnmpCommand(device, snmpParam, tableIndex);
	}
	return result;
    }

    /**
     * Utility method to execute SNMP get with table index on ECM side.
     * 
     * @param AutomaticsTapApi
     *            The {@link AutomaticsTapApi} instance.
     * @param device
     *            The device to be queried.
     * @param mibOrOid
     *            The MIB or OID need to be queried.
     * @param tableIndex
     *            The table index.
     * @return Command output.
     */
    public static String snmpGetOnEcm(AutomaticsTapApi tapEnv, Dut device, String mibOrOid) {

	String result = null;
	SnmpParams snmpParam = null;
	String snmpProtocol = System.getProperty(SnmpConstants.SYSTEM_PARAM_SNMP_VERSION,
		SnmpProtocol.SNMP_V2.toString());
	LOGGER.info("Current SNMP protocol: " + snmpProtocol);
	LOGGER.info("mibOrOid to execute: " + mibOrOid);

	snmpParam = new SnmpParams();
	snmpParam.setSnmpCommand(SnmpCommand.GET);
	snmpParam.setSnmpCommunity("CUSTOM");
	snmpParam.setMibOid(mibOrOid.trim());
	snmpParam.setCommandOption("-t 10 ");

	LOGGER.info("snmpParam  is : " + snmpParam);
	result = tapEnv.executeSnmpCommand(device, snmpParam);
	LOGGER.info("snmpGetOnEcm OutPut is : " + result);
	for (int retryCount = 0; getRetries(result, snmpProtocol, retryCount); ++retryCount) {
	    LOGGER.info("Retrying snmp .Retry count :" + retryCount);
	    snmpParam = new SnmpParams();
	    snmpParam.setSnmpCommand(SnmpCommand.GET);
	    snmpParam.setSnmpCommunity("CUSTOM");
	    snmpParam.setMibOid(mibOrOid.trim());
	    snmpParam.setCommandOption("-t 10 ");

	    result = tapEnv.executeSnmpCommand(device, snmpParam);
	}
	return result;
    }

    /**
     * Utility method to execute SNMP get with table index on ECM side.
     * 
     * @param AutomaticsTapApi
     *            The {@link AutomaticsTapApi} instance.
     * @param device
     *            The device to be queried.
     * @param mibOrOid
     *            The MIB or OID need to be queried.
     * @param tableIndex
     *            The table index.
     * @return Command output.
     */
    public static String snmpSetOnEcm(AutomaticsTapApi tapEnv, Dut device, String mibOrOid, SnmpDataType datatype,
	    String value) {

	String result = null;
	SnmpParams snmpParam = null;
	String snmpProtocol = System.getProperty(SnmpConstants.SYSTEM_PARAM_SNMP_VERSION,
		SnmpProtocol.SNMP_V2.toString());
	LOGGER.info("Current SNMP protocol: " + snmpProtocol);

	snmpParam = new SnmpParams();
	snmpParam.setSnmpCommand(SnmpCommand.SET);
	snmpParam.setSnmpCommunity("CUSTOM");
	snmpParam.setMibOid(mibOrOid.trim());
	snmpParam.setDataType(datatype);
	snmpParam.setValue(value);

	result = tapEnv.executeSnmpCommand(device, snmpParam);

	for (int retryCount = 0; getRetries(result, snmpProtocol, retryCount); ++retryCount) {
	    LOGGER.info("Retrying snmp .Retry count :" + retryCount);
	    snmpParam = new SnmpParams();
	    snmpParam.setSnmpCommand(SnmpCommand.SET);
	    snmpParam.setSnmpCommunity("CUSTOM");
	    snmpParam.setMibOid(mibOrOid.trim());
	    snmpParam.setDataType(datatype);
	    snmpParam.setValue(value);

	    result = tapEnv.executeSnmpCommand(device, snmpParam);
	}
	return result;
    }

    /**
     * Utility method to execute SNMP get with table index on ECM side.
     * 
     * @param AutomaticsTapApi
     *            The {@link AutomaticsTapApi} instance.
     * @param device
     *            The device to be queried.
     * @param mibOrOid
     *            The MIB or OID need to be queried.
     * @param tableIndex
     *            The table index.
     * @return Command output.
     */
    public static String snmpSetOnEcm_V3(AutomaticsTapApi tapEnv, Dut device, String mibOrOid, SnmpDataType datatype,
	    String value) {

	String result = null;
	SnmpParams snmpParam = null;
	String snmpProtocol = System.setProperty(SnmpConstants.SYSTEM_PARAM_SNMP_VERSION,
		SnmpProtocol.SNMP_V3.toString());
	LOGGER.info("Current SNMP protocol: " + snmpProtocol);

	snmpParam = new SnmpParams();
	snmpParam.setSnmpCommand(SnmpCommand.SET);
	snmpParam.setSnmpCommunity("CUSTOM");
	snmpParam.setMibOid(mibOrOid.trim());
	snmpParam.setCommandOption("-t 10 ");
	snmpParam.setCommandOption("-OQ -v 3 -u docsisManager");
	snmpParam.setDataType(datatype);
	snmpParam.setValue(value);

	result = tapEnv.executeSnmpCommand(device, snmpParam);

	for (int retryCount = 0; getRetries(result, snmpProtocol, retryCount); ++retryCount) {
	    LOGGER.info("Retrying snmp .Retry count :" + retryCount);
	    snmpParam = new SnmpParams();
	    snmpParam.setSnmpCommand(SnmpCommand.SET);
	    snmpParam.setSnmpCommunity("CUSTOM");
	    snmpParam.setMibOid(mibOrOid.trim());
	    snmpParam.setCommandOption("-t 10 ");
	    snmpParam.setCommandOption("-OQ -v 3 -u docsisManager");
	    snmpParam.setDataType(datatype);
	    snmpParam.setValue(value);

	    result = tapEnv.executeSnmpCommand(device, snmpParam);
	}
	return result;
    }

    private static boolean getRetries(String commandOutput, String snmpProtocol, int retryCount) {
	boolean shouldRetry = false;
	int retry = 0;
	if (SnmpProtocol.SNMP_V3.toString().equals(snmpProtocol)) {
	    try {
		retry = Integer.parseInt(System.getProperty("snmpv3.retry.count", "1"));
	    } catch (NumberFormatException var6) {
		retry = 1;
	    }

	    LOGGER.debug("Is timeout?" + commandOutput.contains("Timeout"));
	    LOGGER.debug("retryCount?" + retryCount);
	    LOGGER.debug("retry" + retry);
	    if ((CommonMethods.isNull(commandOutput) || commandOutput.contains("Timeout")) && retry > 0
		    && retryCount < retry) {
		shouldRetry = true;
	    }
	}

	LOGGER.debug("is retry required ?" + shouldRetry);
	return shouldRetry;
    }

    /**
     * Utility method to execute SNMP SET command with table index on RDKB devices
     * 
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance
     * @param device
     *            The {@link Dut} instance
     * @param oidOrMibName
     *            The MIB or OID name.
     * 
     * @param dataType
     *            The type of data to be set
     * @param value
     *            The value of data to be set
     * @param tableIndex
     *            The table index to be appended.
     * @return Provides the SNMP SET Command output.
     */
    public static String executeSnmpSetWithTableIndexOnRdkDevices(AutomaticsTapApi tapEnv, Dut device,
	    String oidOrMibName, SnmpDataType dataType, String value, String tableIndex) {
	String snmpCommandOutput = null;
	String snmpProtocol = System.getProperty(SnmpConstants.SYSTEM_PARAM_SNMP_VERSION,
		SnmpProtocol.SNMP_V2.toString());
	oidOrMibName = oidOrMibName + "." + tableIndex;
	snmpCommandOutput = BroadBandSnmpUtils.snmpSetOnEcm(tapEnv, device, oidOrMibName, dataType, value);
	return snmpCommandOutput;
    }

    /**
     * Utility method to execute SNMP set with given table index
     * 
     * @param tapEnv
     * @param ipAddress
     * @param snmpProtocol
     * @param mibOrOid
     * @param tableIndex
     * @return Command output
     */
    public static String retrieveSnmpSetOutputWithGivenIndexOnRdkDevices(Dut device, AutomaticsTapApi tapEnv,
	    String mibOrOid, SnmpDataType dataType, String value, String tableIndex) {
	String snmpOutput = null;
	try {
	    String snmpProtocol = System.getProperty(SnmpConstants.SYSTEM_PARAM_SNMP_VERSION,
		    SnmpProtocol.SNMP_V2.toString());
	    mibOrOid = mibOrOid + "." + tableIndex;
	    // models except Fiber devices
	    if (DeviceModeHandler.isFibreDevice(device)
		    || (snmpProtocol.equalsIgnoreCase(SnmpProtocol.SNMP_V3.toString())
			    && !validateSnmpV3EcmIpApplicableModels(device))) {
		// snmpOutput = CommonM.snmpSetOnEstb(tapEnv, device, mibOrOid, dataType, value, tableIndex);
	    } else {
		snmpOutput = BroadBandSnmpUtils.snmpSetOnEcm(tapEnv, device, mibOrOid, dataType, value);
	    }

	} catch (Exception exception) {
	    LOGGER.error("EXCEPTION OCCURRED WHILE DOING SNMP OPERATION: " + exception.getMessage());
	}
	return snmpOutput;
    }

    /**
     * Utility method to execute SNMP set with default table index
     * 
     * @param tapEnv
     * @param ipAddress
     * @param snmpProtocol
     * @param mibOrOid
     * @param tableIndex
     * @return Command output
     */
    public static String retrieveSnmpSetOutputWithDefaultIndexOnRdkDevices(Dut device, AutomaticsTapApi tapEnv,
	    String mibOrOid, SnmpDataType dataType, String value) {
	String snmpOutput = null;
	try {
	    String snmpProtocol = System.getProperty(SnmpConstants.SYSTEM_PARAM_SNMP_VERSION,
		    SnmpProtocol.SNMP_V2.toString());
	    snmpOutput = BroadBandSnmpUtils.snmpSetOnEcm(tapEnv, device, mibOrOid, dataType, value);
	} catch (Exception exception) {
	    LOGGER.error("EXCEPTION OCCURRED WHILE DOING SNMP OPERATION: " + exception.getMessage());
	}
	return snmpOutput;
    }

    /**
     * Utility method to execute SNMP GET command on RDKB devices
     * 
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance
     * @param device
     *            The device to be validated.
     * @param oidOrMibName
     *            The MIB or OID name.
     * @return Provides the SNMP GET Command output.
     */
    public static String executeSnmpGetOnRdkDevices(AutomaticsTapApi tapEnv, Dut device, String oidOrMibName) {
	String snmpCommandOutput = null;
	String snmpProtocol = System.getProperty(SnmpConstants.SYSTEM_PARAM_SNMP_VERSION,
		SnmpProtocol.SNMP_V2.toString());
	snmpCommandOutput = BroadBandSnmpUtils.snmpGetOnEcm(tapEnv, device, oidOrMibName);
	return snmpCommandOutput;
    }

    /**
     * Utility method to execute SNMP GET command on RDKB devices devices.
     * 
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance
     * @param device
     *            The device to be validated.
     * @param oidOrMibName
     *            The MIB or OID name.
     * @return Provides the SNMP GET Command output.
     */
    public static String executeSnmpGetOnRdkDevicesWithTableIndex(AutomaticsTapApi tapEnv, Dut device,
	    String oidOrMibName, String tableIndex) {
	String snmpCommandOutput = null;

	oidOrMibName = oidOrMibName + "." + tableIndex;
	snmpCommandOutput = BroadBandSnmpUtils.snmpGetOnEcm(tapEnv, device, oidOrMibName);
	return snmpCommandOutput;
    }

    /**
     * Utils method to check SNMP status
     * 
     * @param tapEnv
     * @param device
     * @return true if SNMP is up
     */
    public static boolean checkSnmpIsUp(AutomaticsTapApi tapEnv, Dut device) {
	boolean status = false; // stores test status
	try {
	    String snmpOutput = null; // stores SNMP output
	    long pollDuration = 0L; // stores the poll duration
	    long startTime = 0L; // stores the current system time
	    String oidOrMibName = null;

	    LOGGER.info("##VERIFYING SNMP IS UP##");
	    pollDuration = BroadBandTestConstants.FIVE_MINUTES;
	    startTime = System.currentTimeMillis();
	    do {
		LOGGER.info("GOING TO WAIT FOR 30 SECONDS");
		tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		try {
		    snmpOutput = executeSnmpGetOnRdkDevicesWithTableIndex(tapEnv, device,
			    BroadBandSnmpMib.ECM_SYS_DESCR.getOid(), BroadBandSnmpMib.ECM_SYS_DESCR.getTableIndex());
		    LOGGER.info("snmpOutput is : " + snmpOutput);
		} catch (Exception exception) {
		    // Log & Suppress the Exception.
		    LOGGER.error("EXCEPTION OCCURRED WHILE DOING SNMP OPERATION: " + exception.getMessage());
		}
		String firmwareVersion = device.getFirmwareVersion();
		LOGGER.info("firmwareVersion is : " + firmwareVersion);
		status = CommonMethods.isNotNull(snmpOutput) && CommonMethods.isNotNull(firmwareVersion)
			&& CommonUtils.patternSearchFromTargetString(snmpOutput, firmwareVersion);
	    } while ((System.currentTimeMillis() - startTime) < pollDuration && !status);

	    if (!status) {
		LOGGER.error("##SNMP FAILED TO UP IN 5 MINUTES##");
	    } else {
		LOGGER.info("##SNMP IS UP##");
	    }

	} catch (Exception exception) {
	    LOGGER.error("Exception occurred while checking the status of SNMP: " + exception.getMessage());
	}
	return status;
    }

    /**
     * Helper method is to format MAC address to remove leading zeros.
     * 
     * @param macAddress
     *            The given MAC address.
     * @return Formatted string.
     */
    public static String formatMacAddressWithoutLeadingZeros(String macAddress) {
	return BroadBandCommonUtils.formatIpOrMacWithoutLeadingZeros(macAddress);
    }

    /**
     * Utility method to get/format the expected DOCSIS software version from version.txt
     * 
     * @param tapApi
     *            The {@link AutomaticsTapApi} instance.
     * @param device
     *            The device to be validated.
     * @return Expected DOCSIS software version.
     */
    public static String getExpectedDocsisCableDeviceCurrentSoftwareVersion(AutomaticsTapApi tapApi, Dut device) {
	String expectedDocsisSwVersion = null;
	expectedDocsisSwVersion = getExpectedSoftwareVersionUsingDmcliCommand(device, tapApi, false);

	LOGGER.info("Expected Currently Running Docsis Software Version = " + expectedDocsisSwVersion);

	return expectedDocsisSwVersion;
    }

    /**
     * Utility method is to get the expected software version.
     * 
     * @param device
     *            The device to be tested.
     * @param tapApi
     *            The {@link AutomaticsTapApi} instance.
     * @param isTr69
     *            Flag to check the whether SNMP or TR-069 validation.
     * @return Device firmware version.
     */
    public static String getExpectedSoftwareVersionUsingDmcliCommand(Dut device, AutomaticsTapApi tapApi,
	    boolean isTr69) {
	String expectedSoftwareVersion = device.getFirmwareVersion();
	LOGGER.info("Expected software version  = " + expectedSoftwareVersion);
	return expectedSoftwareVersion;
    }

    /**
     * Utility method to execute SNMP WALK command on RDKB devices devices.
     * 
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance
     * @param device
     *            The device to be validated.
     * @param oidOrMibName
     *            The MIB or OID name.
     * @return Provides the SNMP WALK Command output.
     */
    public static String executeSnmpWalkOnRdkDevices(AutomaticsTapApi tapEnv, Dut device, String oidOrMibName) {
	String snmpCommandOutput = null;
	snmpCommandOutput = CommonMethods.snmpWalkOnEstb(tapEnv, device, oidOrMibName);
	return snmpCommandOutput;
    }

    /**
     * Helper method to parse the system descriptor properties from SNMP command and store it in HashMap for further
     * processing.
     * 
     * @param sysDescrSnmpOutput
     *            The SNMP command output.
     * @return HashMap which gives the system descriptor properties.
     */
    public static HashMap<String, String> parseSystemDescriptorInformationFromSnmpOutput(String sysDescrSnmpOutput)
	    throws Exception {

	LOGGER.debug("STARTING METHOD : parseSystemDescriptorInformationFromSnmpOutput()");

	HashMap<String, String> systemDescriptor = new HashMap<String, String>();

	if (CommonMethods.isNotNull(sysDescrSnmpOutput)
		&& sysDescrSnmpOutput.contains(BroadBandTestConstants.LEFT_SHIFT_OPERATOR)
		&& sysDescrSnmpOutput.contains(BroadBandTestConstants.RIGHT_SHIFT_OPERATOR)) {

	    String[] requiredDescriptor = sysDescrSnmpOutput.split(BroadBandTestConstants.LEFT_SHIFT_OPERATOR)[1]
		    .replace(BroadBandTestConstants.RIGHT_SHIFT_OPERATOR, BroadBandTestConstants.EMPTY_STRING)
		    .split(BroadBandTestConstants.SEMI_COLON);
	    for (String sysDescrField : requiredDescriptor) {

		String[] sysDescrFieldKeyValue = sysDescrField.split(BroadBandTestConstants.DELIMITER_COLON);

		if (CommonMethods.isNotNull(sysDescrFieldKeyValue[0])) {
		    systemDescriptor.put(sysDescrFieldKeyValue[0].trim(), sysDescrFieldKeyValue[1].trim());
		} else {
		    LOGGER.error(
			    "parseSystemDescriptorInformationFromSnmpOutput : Either key or value is null observed in system Descriptor field = "
				    + sysDescrField);
		}

	    }
	} else {
	    throw new Exception("Invalid sysDescSnmprOutput!!!!!!!!");
	}

	LOGGER.debug("ENDING : parseSystemDescriptorInformationFromSnmpOutput()");
	return systemDescriptor;
    }

    /**
     * Utility method to execute SNMP set with default table index
     * 
     * @param device
     * @param tapApi
     * @param mibOrOid
     * @param tableIndex
     * @param dataType
     * @param value
     * @return Command output
     */
    public static String retrieveSnmpV3SetOutputWithDefaultIndexOnRdkDevices(Dut device, AutomaticsTapApi tapApi,
	    String mibOrOid, SnmpDataType dataType, String value) {
	String snmpOutput = null;
	try {
	    snmpOutput = BroadBandSnmpUtils.snmpSetOnEcm(tapApi, device, mibOrOid, dataType, value);
	} catch (Exception exception) {
	    LOGGER.error("EXCEPTION OCCURRED WHILE DOING SNMP OPERATION: " + exception.getMessage());
	}
	return snmpOutput;
    }

    /**
     * Utility method to execute SNMP set with default table index
     * 
     * @param device
     * @param tapApi
     * @param mibOrOid
     * @param tableIndex
     * @param dataType
     * @param value
     * @return Command output
     */
    public static String retrieveSnmpV3SetOutputWithDefaultIndexOnRdkDevices_V3(Dut device, AutomaticsTapApi tapApi,
	    String mibOrOid, SnmpDataType dataType, String value) {
	String snmpOutput = null;
	try {
	    snmpOutput = BroadBandSnmpUtils.snmpSetOnEcm_V3(tapApi, device, mibOrOid, dataType, value);
	} catch (Exception exception) {
	    LOGGER.error("EXCEPTION OCCURRED WHILE DOING SNMP OPERATION: " + exception.getMessage());
	}
	return snmpOutput;
    }

    /**
     * Utility method to retrieve the CMTS MAC address using CMTS MAC Address using SNMP MIB
     * docsIfCmCmtsAddress.2(1.3.6.1.2.1.10.127.1.2.1.1.1.2) and verify with MAC Address retrieved using Arp table.
     * 
     * @param device
     *            The Dut to be validated.
     * @param tapEnv
     *            The {@link AutomaticsTapApi} reference.
     * 
     * @return CMTS Mac Address where this particular device connected.
     * @author Govardhan
     */
    public static String getCmtsMacAddressUsingSnmpCommand(Dut device, AutomaticsTapApi tapEnv) {
	String snmpDocsisCmtsMacAddress = BroadBandSnmpUtils.snmpGetOnEcm(tapEnv, device,
		BroadBandSnmpMib.ECM_DOCS_IF_CM_CMTS_MAC_ADDRESS.getOid(), "2");
	return snmpDocsisCmtsMacAddress;
    }

    /**
     * Utility method to execute SNMP set with invalid community string
     * 
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance.
     * @param device
     *            The device to be queried.
     * @param mibOrOid
     *            The MIB that needs to be queried
     * @param dataType
     *            {@link SnmpDataType}
     * @param value
     *            value to set
     * @param tableIndex
     *            The table index of the MIB to be queried
     * 
     * @return Command output
     */
    public static String snmpSetOnEcmForInvalidCommunityString(AutomaticsTapApi tapEnv, Dut device, String mibOrOid,
	    SnmpDataType dataType, String value, String tableIndex) {

	String result = null;
	SnmpParams snmpParam = new SnmpParams();
	mibOrOid = mibOrOid + "." + tableIndex;
	LOGGER.info("mibOrOid to execute: " + mibOrOid);

	snmpParam.setSnmpCommand(SnmpCommand.SET);
	snmpParam.setSnmpCommunity(SnmpConstants.INVALID_COMMUNITY_STRING);
	snmpParam.setMibOid(mibOrOid);
	snmpParam.setValue(value);
	snmpParam.setDataType(dataType);
	result = tapEnv.executeSnmpCommand(device, snmpParam);
	return result;

    }

    /**
     * Utility method to execute SNMP get with table index on ECM side for negative scenario.
     * 
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance.
     * @param device
     *            {@link Dut}The device to be queried.
     * @param mibOrOid
     *            The MIB or OID need to be queried.
     * @param tableIndex
     *            The table index.
     * @return Command output.
     */
    public static String snmpGetOnEcmForInvalidCommunityString(AutomaticsTapApi tapEnv, Dut device, String mibOrOid,
	    String tableIndex) {

	String result = null;
	SnmpParams snmpParam = new SnmpParams();
	mibOrOid = mibOrOid + "." + tableIndex;
	LOGGER.info("mibOrOid to execute: " + mibOrOid);

	snmpParam.setSnmpCommand(SnmpCommand.GET);
	snmpParam.setSnmpCommunity(SnmpConstants.INVALID_COMMUNITY_STRING);
	snmpParam.setMibOid(mibOrOid);
	result = tapEnv.executeSnmpCommand(device, snmpParam);
	return result;

    }

    /**
     * Utility method to execute SNMP get with table index on MTA side
     * 
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance.
     * @param device
     *            {@link Dut}The device to be queried.
     * @param mibOrOid
     *            The MIB or OID need to be queried.
     * @param tableIndex
     *            The table index.
     * @param mtaAddress
     *            MTA address
     * @return Command output.
     */
    public static String snmpGetOnEmta(AutomaticsTapApi tapEnv, Dut device, String mibOrOid, String tableIndex,
	    String mtaAddress) {

	String result = null;
	SnmpParams snmpParam = new SnmpParams();
	mibOrOid = mibOrOid + "." + tableIndex;
	LOGGER.info("mibOrOid to execute: " + mibOrOid);

	snmpParam.setSnmpCommand(SnmpCommand.GET);
	snmpParam.setIpAddress(mtaAddress);
	snmpParam.setMibOid(mibOrOid);
	snmpParam.setSnmpCommunity("MTA_COMMUNITY_STRING");
	result = tapEnv.executeSnmpCommand(device, snmpParam);
	return result;

    }

    /**
     * Utility method to execute SNMP set with table index on MTA side
     * 
     * @param AutomaticsTapApi
     *            The {@link AutomaticsTapApi} instance.
     * @param device
     *            The device to be queried.
     * @param mibOrOid
     *            The MIB or OID need to be queried.
     * @param datatype
     *            {@link SnmpDataType}
     * @param tableIndex
     *            The table index.
     * @param value
     *            value to set
     * @param mtaAddress
     *            MTA address
     * @return Command output.
     */
    public static String snmpSetOnEmta(AutomaticsTapApi tapEnv, Dut device, String mibOrOid, SnmpDataType datatype,
	    String value, String tableIndex, String mtaAddress) {

	String result = null;
	SnmpParams snmpParam = null;
	String snmpProtocol = System.getProperty(SnmpConstants.SYSTEM_PARAM_SNMP_VERSION,
		SnmpProtocol.SNMP_V2.toString());
	mibOrOid = mibOrOid + "." + tableIndex;
	LOGGER.info("mibOrOid to execute: " + mibOrOid);
	snmpParam = new SnmpParams();
	snmpParam.setSnmpCommand(SnmpCommand.SET);
	snmpParam.setMibOid(mibOrOid.trim());
	snmpParam.setIpAddress(mtaAddress);
	snmpParam.setDataType(datatype);
	snmpParam.setValue(value);
	snmpParam.setSnmpCommunity("MTA_COMMUNITY_STRING");
	result = tapEnv.executeSnmpCommand(device, snmpParam);

	for (int retryCount = 0; getRetries(result, snmpProtocol, retryCount); ++retryCount) {
	    LOGGER.info("Retrying snmp .Retry count :" + retryCount);
	    snmpParam = new SnmpParams();
	    snmpParam.setSnmpCommand(SnmpCommand.SET);
	    snmpParam.setMibOid(mibOrOid.trim());
	    snmpParam.setIpAddress(mtaAddress);
	    snmpParam.setDataType(datatype);
	    snmpParam.setValue(value);
	    snmpParam.setSnmpCommunity("MTA_COMMUNITY_STRING");
	    result = tapEnv.executeSnmpCommand(device, snmpParam);
	}
	LOGGER.info("result of executeSnmpCommand :to return" + result);
	return result;
    }

    /**
     * Helper method to identify whether given oid is CM related or not
     * 
     * @param mibOrOid
     *            OID to be classified
     * @return true if it is CM related OIDs
     */
    private static boolean isCableModemRelatedOid(String mibOrOid) {
	boolean isCmRelated = false;
	if (CommonMethods.isNotNull(mibOrOid)) {
	    mibOrOid = mibOrOid.trim();
	    isCmRelated = (mibOrOid.startsWith("1.3.6.1.4.1.4491") || mibOrOid.startsWith("1.3.6.1.2.1")
		    || mibOrOid.startsWith(".1.3.6.1.4.1.4491") || mibOrOid.startsWith(".1.3.6.1.2.1"));
	    LOGGER.info((isCmRelated ? "Cable Modem Related OID : " : "RDKB or MoCA Related OID : ") + mibOrOid);
	}

	return isCmRelated;
    }

    /**
     * Utility method to execute SNMP GET command with table index on RDKB devices.
     * 
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance
     * @param device
     *            The device to be validated.
     * @param oidOrMibName
     *            The MIB or OID name.
     * @param tableIndex
     *            The table index to be appended.
     * @return Provides the SNMP GET Command output.
     */
    public static String executeSnmpGetWithTableIndexOnRdkDevices(AutomaticsTapApi tapEnv, Dut device,
	    String oidOrMibName, String tableIndex) {
	String snmpCommandOutput = null;
	if (isCableModemRelatedOid(oidOrMibName)) {
	    snmpCommandOutput = BroadBandSnmpUtils.snmpGetOnEcm(tapEnv, device, oidOrMibName, tableIndex);
	} else {
	    oidOrMibName = oidOrMibName + "." + tableIndex;
	    snmpCommandOutput = CommonMethods.snmpGetOnEstb(tapEnv, device, device.getHostIpAddress(), oidOrMibName);

	}
	return snmpCommandOutput;
    }

    /**
     * Utility methods to verify the currently running firmware version using sysDescr SNMP MIBs.
     * 
     * @param device
     *            The Dut to be validated.
     * @param buildImageWithoutExtension
     *            The build name to be validated.
     * @return true if build name matches with sysDescr SW VER.
     * @refactor Govardhan
     */
    public static boolean verifyCurrentlyRunningFirmwareVersionUsingSysDescrSnmpCommand(AutomaticsTapApi tapEnv,
	    Dut device, String buildImage) {

	boolean deviceUpgradedSuccessfully = false;
	String snmpSystemDescrOutput = BroadBandSnmpUtils.executeSnmpWalkOnRdkDevices(tapEnv, device,
		BroadBandSnmpMib.ESTB_SYS_DESCRIPTION.getOid());

	LOGGER.info("Snmp System Description output is : " + snmpSystemDescrOutput);

	if (CommonMethods.isNotNull(snmpSystemDescrOutput)) {
	    deviceUpgradedSuccessfully = verifyFirmwareVersionUsingSnmp(snmpSystemDescrOutput, buildImage);
	}
	return deviceUpgradedSuccessfully;
    }

    /**
     * Method to verify if the box is having the expected firmware version using SNMP
     * 
     * @param device
     *            The Dut to be validated.
     * @return status true if the firmware version matches else false
     * @refactor Govardhan
     */
    public static boolean verifyFirmwareVersionUsingSnmp(String snmpSystemDescrOutput,
	    String buildImageWithoutExtension) {
	boolean status = false;
	snmpSystemDescrOutput = snmpSystemDescrOutput.replace(";", "\r\n");
	String softwareVersion = CommonMethods.patternFinder(snmpSystemDescrOutput,
		BroadBandTestConstants.SOFTWARE_VERSION_PATTERN);
	if (CommonMethods.isNotNull(softwareVersion)) {
	    status = softwareVersion.toLowerCase().contains(buildImageWithoutExtension.toLowerCase());
	}
	return status;
    }

    /**
     * Utility method to execute SNMP walk with table index on ECM side.
     * 
     * @param AutomaticsTapApi
     *            The {@link AutomaticsTapApi} instance.
     * @param device
     *            The device to be queried.
     * @param mibOrOid
     *            The MIB or OID need to be queried.
     * @return Command output.
     */
    public static String snmpWalkOnEcm(AutomaticsTapApi tapEnv, Dut device, String mibOrOid) {

	String result = null;
	SnmpParams snmpParam = null;

	snmpParam = new SnmpParams();
	snmpParam.setSnmpCommand(SnmpCommand.WALK);
	snmpParam.setMibOid(mibOrOid.trim());

	result = tapEnv.executeSnmpCommand(device, snmpParam);

	return result;
    }

    /**
     * Utility methods to verify docsis event text.
     * 
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param device
     *            The Dut to be validated.
     * @param eventCount
     *            DocsisEventText
     * 
     * @return true if event count matches with event text count.
     */
    public static boolean verifyDocsisEventText(AutomaticsTapApi tapEnv, Dut device, int eventCount) {

	boolean status = false;
	List<String> match = new ArrayList<String>();

	String snmpCommandOutput = snmpWalkOnEcm(tapEnv, device, BroadBandSnmpMib.ESTB_DOCS_IS_EVENT_TEXT.getOid());
	if (!snmpCommandOutput.contains(SnmpConstants.NO_SUCH_OID_RESPONSE)
		&& !snmpCommandOutput.contains(BroadBandTestConstants.SNMP_TIME_OUT_RESPONSE)) {
	    // returns all the matched list for the given pattern
	    match = BroadBandCommonUtils.patternFinderForMultipleMatches(snmpCommandOutput,
		    BroadBandTestConstants.DOCSIS_EVENT_TEXT_PATTERN);
	    int eventTextCount = match.size();
	    if (eventTextCount == eventCount) {
		status = true;
	    }
	}
	return status;
    }

    /**
     * Utility methods to verify Docs Dev Server Boot State.
     * 
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param device
     *            The {@link Dut} instance
     * 
     * @return true if OID value is 1
     */
    public static boolean verifyDocsDevServerBootState(AutomaticsTapApi tapEnv, Dut device) {
	boolean status = false;

	String snmpOutPut = snmpWalkOnEcm(tapEnv, device, BroadBandSnmpMib.ESTB_DOCS_DEV_SERVER_BOOT_STATE.getOid());
	LOGGER.info("snmp OutPut : " + snmpOutPut);
	if (CommonMethods.isNotNull(snmpOutPut)) {
	    status = snmpOutPut.contains(BroadBandTestConstants.TRUE_FLAG);
	}
	return status;
    }

    /**
     * Utility methods to verify DocsIf CM Status Transmission Power
     * 
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param device
     *            The {@link Dut} instance
     * 
     * @return true if OID value is between the limit range from 80 to 580
     */
    public static boolean verifyDocsIfCmStatusTxPower(AutomaticsTapApi tapEnv, Dut device) {
	boolean status = false;
	List<String> match = new ArrayList<String>();

	String snmpCommandOutput = snmpWalkOnEcm(tapEnv, device,
		BroadBandSnmpMib.ECM_DOCS_IF_CM_STATUS_TX_POWER.getOid());
	LOGGER.info("Snmp command output of mib 'docsIfCmStatusTxPower': " + snmpCommandOutput);
	match = BroadBandCommonUtils.patternFinderForMultipleMatches(snmpCommandOutput,
		BroadBandTestConstants.DOCSIF_MIB_VALUE_PATTERN);
	status = BroadBandCommonUtils.compareListBetweenLimitRange(match, BroadBandTestConstants.INT_VALUE_EIGHTY,
		BroadBandTestConstants.INT_VALUE_FIVE_HUNDRED_AND_EIGHTY);

	return status;
    }

    /**
     * Utility methods to verify DocsIf Down Channel Power
     * 
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param device
     *            The {@link Dut} instance
     * 
     * @return true if OID value is between the limit range from -150 to 150
     */
    public static boolean verifyDocsIfDownChannelPower(AutomaticsTapApi tapEnv, Dut device) {
	boolean status = false;
	List<String> match = new ArrayList<String>();

	String snmpCommandOutput = snmpWalkOnEcm(tapEnv, device,
		BroadBandSnmpMib.ECM_DOCS_IF_DOWN_CHANNEL_POWER_MIB.getOid());
	LOGGER.info("Snmp command output of mib 'docsIfDownChannelPower': " + snmpCommandOutput);
	match = BroadBandCommonUtils.patternFinderForMultipleMatches(snmpCommandOutput,
		BroadBandTestConstants.DOCSIF_MIB_VALUE_PATTERN);
	status = BroadBandCommonUtils.compareListBetweenLimitRange(match,
		BroadBandTestConstants.INT_VALUE_MINUS_HUNDRED_AND_FIFTY,
		BroadBandTestConstants.INT_VALUE_HUNDRED_AND_FIFTY);

	return status;
    }

    /**
     * Utility methods to verify DocsIf SigQSignal Noise
     * 
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param device
     *            The device to be validated.
     * 
     * @return true if OID value is greater than or equal to 120
     */
    public static boolean verifyDocsIfSigQSignalNoise(AutomaticsTapApi tapEnv, Dut device) {
	boolean status = false;
	List<String> match = new ArrayList<String>();

	String snmpCommandOutput = snmpWalkOnEcm(tapEnv, device,
		BroadBandSnmpMib.ECM_DOCSIS_SIGNAL_QUALITY_EXTENDED_RX_MER_TABLE.getOid());
	LOGGER.info("Snmp command output of mib 'docsIf3SignalQualityExtTable': " + snmpCommandOutput);
	match = BroadBandCommonUtils.patternFinderForMultipleMatches(snmpCommandOutput,
		BroadBandTestConstants.DOCSIF_MIB_VALUE_PATTERN);
	status = BroadBandCommonUtils.compareListWithLimitValue(match,
		BroadBandTestConstants.INT_VALUE_HUNDRED_AND_TWENTY);

	return status;
    }

    /**
     * Utility methods to verify System up time
     * 
     * @param device
     *            The device to be validated.
     * 
     * @return true if SNMP MIB response and output of 'cat /proc/uptime' command are same
     */
    public static boolean verifySysUpTime(AutomaticsTapApi tapEnv, Dut device) throws Exception {
	boolean status = false;
	String errorMessage = null;
	// uptime in seconds
	String uptimeCmdResponse = CommonUtils.getUptimeFromProc(tapEnv, device);
	LOGGER.info("Uptime from /proc/uptime = " + uptimeCmdResponse);

	if (CommonUtils.isNotEmptyOrNull(uptimeCmdResponse)) {

	    String snmpCommandOutput = BroadBandSnmpUtils.executeSnmpWalkOnRdkDevices(tapEnv, device,
		    BroadBandSnmpMib.ESTB_SYS_UP_TIME.getOid());

	    LOGGER.info("Uptime from snmp = " + snmpCommandOutput);

	    if (CommonMethods.isNotNull(snmpCommandOutput)) {
		status = BroadBandCommonUtils.verifyUptimeFromSnmpOutput(snmpCommandOutput, uptimeCmdResponse);
	    } else {
		errorMessage = "Failed to retrieve uptime from snmp ";
		LOGGER.error(errorMessage);
		LOGGER.error("snmpCommandOutput = " + snmpCommandOutput);
		throw new TestException(errorMessage);
	    }

	} else {
	    errorMessage = "Failed to retrieve uptime from STB using uptime command";
	    LOGGER.error(errorMessage);
	    LOGGER.error("Response = " + uptimeCmdResponse);
	    throw new TestException(errorMessage);
	}

	return status;
    }

    /**
     * Utility methods to verify DocsIf SigQSignal Noise with range
     * 
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param device
     *            The device to be validated.
     * 
     * @return true if OID value is greater than or equal to 200 and less than or equal to 500
     * @author Deepa Bada
     * @Refactor Athira
     */
    public static boolean verifyDocsIfSigQSignalNoiseWithRange(AutomaticsTapApi tapEnv, Dut device) {
	LOGGER.debug("STARTING METHOD: verifyDocsIfSigQSignalNoiseWithRange");
	boolean status = false;
	List<String> match = new ArrayList<String>();

	String snmpCommandOutput = snmpWalkOnEcm(tapEnv, device,
		BroadBandSnmpMib.ECM_DOCS_IF_SIG_Q_SIGNAL_NOISE.getOid());
	LOGGER.info("Snmp command output of mib 'SigQSignalNoise': " + snmpCommandOutput);
	if (CommonMethods.isNotNull(snmpCommandOutput)) {
	    match = BroadBandCommonUtils.patternFinderForMultipleMatches(snmpCommandOutput,
		    BroadBandTestConstants.DOCSIF_MIB_VALUE_PATTERN);

	    status = BroadBandCommonUtils.compareListBetweenLimitRange(match,
		    BroadBandTestConstants.INT_VALUE_TWO_HUNDRED, BroadBandTestConstants.INT_VALUE_FIVE_HUNDRED);
	}
	LOGGER.debug("ENDING METHOD: verifyDocsIfSigQSignalNoiseWithRange");
	return status;
    }

    /**
     * @param device
     *            The device to be validated
     * @param tapEnv
     *            EactsTapApi instance
     * @return true if OID is read-only
     * @author Deepa Bada
     */
    public static boolean verifyReadOnlySignalNoiseStatus(Dut device, AutomaticsTapApi tapEnv) {

	// snmpoutput of set command
	String snmpOutput = null;
	// Failure reason from snmp output
	String failedReason = null;
	boolean status = false;

	snmpOutput = BroadBandSnmpUtils.retrieveSnmpSetOutputWithDefaultIndexOnRdkDevices(device, tapEnv,
		BroadBandSnmpMib.ECM_DOCS_IF_SIG_Q_SIGNAL_NOISE.getOid(), SnmpDataType.INTEGER,
		BroadBandTestConstants.STRING_CONSTANT_290);
	failedReason = CommonMethods.patternFinder(snmpOutput, BroadBandTestConstants.PATTERN_FINDER_FAILURE_REASON);
	status = CommonMethods.isNotNull(failedReason)
		&& failedReason.equalsIgnoreCase(BroadBandTestConstants.NOT_WRITABLE);

	return status;
    }

    /**
     * Utility method to validate CmEthernetOperSetting value retrieved from SNMP
     * 
     * @param device
     *            The device to be validated.
     * @param output
     *            input
     * @return status output
     * @Refactor Alan_Bivera
     */
    public static boolean validateCmEthernetOperSettingValueRetrievedFromSnmp(String output) {
	boolean status = false;
	try {

	    int result = Integer.parseInt(output);
	    result = result / (BroadBandTestConstants.CONSTANT_1000000);
	    status = (result == BroadBandTestConstants.CONSTANT_0) || (result == BroadBandTestConstants.CONSTANT_10)
		    || (result == BroadBandTestConstants.CONSTANT_100)
		    || (result == BroadBandTestConstants.CONSTANT_1000);

	} catch (Exception exception) {
	    LOGGER.error("Unable to parse string to integer value" + exception.getMessage());

	}
	return status;
    }

    /**
     * Utility method to verify the SNMP output values are greater than expected Range
     * 
     * @param snmpOutput
     * @param expectedValue
     * @return true if all the values are greater than expected Range
     * @Refactor Alan_Bivera
     */
    public static boolean validateSnmpOutputWithExpectedValue(String snmpOutput, int expectedRange) {
	boolean result = false;// stores the test status
	try {
	    List<String> extractedOutput = new ArrayList<String>();
	    extractedOutput = CommonUtils.patternFinderForMultipleMatches(snmpOutput,
		    BroadBandTestConstants.PATTERN_TO_EXTRACT_VALUES_FROM_SNMP_WALK_OUTPUT);
	    if (extractedOutput.size() > BroadBandTestConstants.CONSTANT_0) {
		result = true;
		for (String value : extractedOutput) {
		    if (!(CommonMethods.isNotNull(value)
			    && CommonMethods.patternMatcher(value, BroadBandTestConstants.PATTERN_ONLY_DIGITS)
			    && Integer.parseInt(value) > expectedRange)) {
			result = false;
		    }
		}
	    }
	} catch (Exception exception) {
	    LOGGER.error("Unable to validate values of SNMP walk output" + exception.getMessage());
	    result = false;
	}
	return result;
    }

    /**
     * Utility method to check whether all the OIDs under DOCSIS Signal Quality Tables are READ ONLY
     * 
     * @param device
     * @param tapEnv
     * @param snmpOutput
     * @param childOid
     * @param dataType
     * @param setValue
     * @return true if all the child OID under Docsis signal strength are READ ONLY
     * @Refactor Alan_Bivera
     */
    public static boolean validateReadOnlyAttributeOFDocsisSignalQualityTables(Dut device, AutomaticsTapApi tapEnv,
	    String snmpOutput, String childOid, SnmpDataType dataType, String setValue) {
	boolean result = false; // stores the test status
	try {
	    List<String> tableIndexList = new ArrayList<String>();
	    tableIndexList = CommonUtils.patternFinderForMultipleMatches(snmpOutput,
		    BroadBandTestConstants.PATTERN_TO_EXTRACT_TABLE_INDEX);
	    if (tableIndexList.size() > BroadBandTestConstants.CONSTANT_0) {
		result = true;
		for (String tableIndex : tableIndexList) {
		    if (executeSnmpSetCommand(tapEnv, device, childOid, dataType, setValue, tableIndex)) {
			result = false;
		    }
		}
	    }
	} catch (Exception exeception) {
	    result = false;
	    LOGGER.error("Exception occured while checking READ-ONLY attribute for Docsis Signal table"
		    + exeception.getMessage());
	}
	return result;
    }

    /**
     * Utility method to frame SNMP SET command with mentioned table index and execute the same
     * 
     * @param tapEnv
     * @param device
     * @param oid
     * @param dataType
     * @param value
     * @param tableIndex
     * 
     * @return the SNMP SET response
     * @Refactor Alan_Bivera
     */
    public static boolean executeSnmpSetCommand(AutomaticsTapApi tapEnv, Dut device, String oid, SnmpDataType dataType,
	    String value, String tableIndex) {
	String snmpOutput = executeSnmpSetWithTableIndexOnRdkDevices(tapEnv, device, oid, dataType, value, tableIndex);
	boolean result = CommonMethods.isNotNull(snmpOutput) && snmpOutput.equals(value);
	return result;
    }

    /**
     * Utility method to check snmp Response and compare snmp and webpa response
     * 
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance
     * @param device
     *            The device to be validated.
     * @param snmpResponse
     *            Snmp Response
     * @param WebPaResponse
     *            Webpa Response
     * @return boolean by validating the condition
     */
    public static boolean validateSNMPResponse(AutomaticsTapApi tapEnv, Dut device, String snmpResponse,
	    String WebPaResponse) {
	LOGGER.debug("STARTING METHOD : validateSNMPResponse()");
	boolean result = false;
	try {
	    result = hasNoSNMPErrorOnResponse(tapEnv, device, snmpResponse)
		    && CommonUtils.patternSearchFromTargetString(snmpResponse.trim(), String.valueOf(WebPaResponse));
	} catch (Exception e) {
	    LOGGER.error("Exception Occured in validateSNMPResponse():" + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : validateSNMPResponse()");
	return result;
    }

    /**
     * Utility method to check snmp Response for snmp errors
     * 
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance
     * @param device
     *            The device to be validated.
     * @param snmpResponse
     *            Snmp Response
     * @return boolean by validating the condition
     */
    public static boolean hasNoSNMPErrorOnResponse(AutomaticsTapApi tapEnv, Dut device, String snmpResponse) {
	LOGGER.debug("STARTING METHOD : hasNoSNMPErrorOnResponse()");
	boolean result = false;
	try {
	    result = CommonMethods.isNotNull(snmpResponse)
		    && !BroadBandCommonUtils.patternSearchFromTargetString(snmpResponse,
			    BroadBandSnmpConstants.SNMP_ERROR_RESPONSE_NO_OBJECT)
		    && !BroadBandCommonUtils.patternSearchFromTargetString(snmpResponse,
			    BroadBandSnmpConstants.SNMP_ERROR_RESPONSE_NO_OID)
		    && !BroadBandCommonUtils.patternSearchFromTargetString(snmpResponse,
			    BroadBandTestConstants.SNMP_TIME_OUT_RESPONSE)
		    && !BroadBandCommonUtils.patternSearchFromTargetString(snmpResponse,
			    BroadBandSnmpConstants.SNMP_ERROR_UNKNOWN_OBJECT)
		    && !BroadBandCommonUtils.patternSearchFromTargetString(snmpResponse,
			    BroadBandTestConstants.SNMPV3_TIMEOUT_ERROR);
	} catch (Exception e) {
	    LOGGER.error("Exception Occured in hasNoSNMPErrorOnResponse():" + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : hasNoSNMPErrorOnResponse()");
	return result;
    }

    /**
     * Utility method to retrieve SNMP output and verify the same with system command output
     * 
     * @param tapEnv
     * @param device
     * @param ssid
     * @param tableIndex
     * @param systemCommand
     * @param testParameter
     * @return ResultValues with test result & error message
     */
    public static BroadBandResultObject retrieveSnmpOutputAndVerifyWithSystemCommandOutput(AutomaticsTapApi tapEnv,
	    Dut device, String oid, String tableIndex, String systemCommand, String testParameter) {
	BroadBandResultObject result = new BroadBandResultObject();
	try {
	    boolean status = false; // stores the test status
	    String errorMessage = null; // stores the error message
	    String snmpOutput = null; // stores SNMP output
	    String ssidRetrievedFromSystemCommand = null; // stores ssid retrieved from system command

	    snmpOutput = executeSnmpGetWithTableIndexOnRdkDevices(tapEnv, device, oid, tableIndex);
	    status = CommonMethods.isNotNull(snmpOutput);
	    errorMessage = "Unable to retrieve " + testParameter + " Radio using SNMP";
	    result.setExecutionStatus(status ? ExecutionStatus.PASSED : ExecutionStatus.FAILED);
	    if (status) {
		ssidRetrievedFromSystemCommand = BroadBandWebPaUtils.getSsidNameRetrievedUsingDeviceCommand(device,
			tapEnv, systemCommand);
		status = CommonMethods.isNotNull(ssidRetrievedFromSystemCommand);
		errorMessage = "Using system command unable to retrieve " + testParameter + " Radio";
		result.setExecutionStatus(ExecutionStatus.NOT_TESTED);
	    }
	    if (status) {
		status = CommonUtils.patternSearchFromTargetString(ssidRetrievedFromSystemCommand, snmpOutput);
		errorMessage = testParameter + " Radio retrieved from SNMP & system command do not match";
		result.setExecutionStatus(status ? ExecutionStatus.PASSED : ExecutionStatus.FAILED);
	    }

	    result.setErrorMessage(errorMessage);
	    result.setStatus(status);

	} catch (Exception exception) {
	    LOGGER.error(
		    "Exception occurred while trying to retrieve and verify SNMP output with system command output: "
			    + exception.getMessage());
	}

	return result;
    }

    /**
     * Utility method to retrieve SNMP output and verify the same with WebPA output
     * 
     * @param tapEnv
     * @param device
     * @param ssid
     * @param tableIndex
     * @param webPaParameter
     * @param testParameter
     * @return ResultValues with test result & error message
     */
    public static BroadBandResultObject retrieveSnmpOutputAndVerifyWithWebPaOutput(AutomaticsTapApi tapEnv, Dut device,
	    String oid, String tableIndex, String webPaParameter, String testParameter) {
	BroadBandResultObject result = new BroadBandResultObject();
	try {
	    boolean status = false; // stores the test status
	    String errorMessage = null; // stores the error message
	    String snmpOutput = null; // stores SNMP output
	    String webPaOutput = null; // stores webPa output

	    snmpOutput = executeSnmpGetWithTableIndexOnRdkDevices(tapEnv, device, oid, tableIndex);
	    status = CommonMethods.isNotNull(snmpOutput);
	    errorMessage = "Unable to retrieve " + testParameter + " Radio using SNMP";
	    result.setExecutionStatus(status ? ExecutionStatus.PASSED : ExecutionStatus.FAILED);
	    if (status) {
		webPaOutput = tapEnv.executeWebPaCommand(device, webPaParameter);
		status = CommonMethods.isNotNull(webPaOutput);
		errorMessage = "Using WebPA unable to retrieve " + testParameter + " Radio";
		result.setExecutionStatus(ExecutionStatus.NOT_TESTED);
	    }
	    if (status) {
		status = snmpOutput.equalsIgnoreCase(webPaOutput);
		errorMessage = testParameter + " Radio retrieved from SNMP & WebPA do not match";
		result.setExecutionStatus(status ? ExecutionStatus.PASSED : ExecutionStatus.FAILED);
	    }

	    result.setErrorMessage(errorMessage);
	    result.setStatus(status);

	} catch (Exception exception) {
	    LOGGER.error("Exception occurred while trying to retrieve and verify SNMP output with WebPA output: "
		    + exception.getMessage());
	}

	return result;
    }

    /**
     * Utility method to execute SNMP get with table index on EMTA
     * 
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance.
     * @param device
     *            {@link Dut}The device to be queried.
     * @param mibOrOid
     *            The MIB or OID need to be queried.
     * @param tableIndex
     *            The table index.
     * @return Command output.
     */
    public static String snmpGetOnEmtaForInvalidCommunityString(AutomaticsTapApi tapEnv, Dut device, String mibOrOid,
	    String tableIndex, String mtaAddress) {

	String result = null;
	SnmpParams snmpParam = new SnmpParams();
	mibOrOid = mibOrOid + "." + tableIndex;
	LOGGER.info("mibOrOid to execute: " + mibOrOid);

	snmpParam.setSnmpCommand(SnmpCommand.GET);
	snmpParam.setSnmpCommunity("MTA_COMMUNITY_STRING");
	snmpParam.setMibOid(mibOrOid);
	snmpParam.setIpAddress(mtaAddress);
	result = tapEnv.executeSnmpCommand(device, snmpParam);
	LOGGER.info("mibOrOid to execute: " + mibOrOid);
	return result;

    }

    /**
     * Utility method to execute SNMP set with invalid community string
     * 
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance.
     * @param device
     *            The device to be queried.
     * @param mibOrOid
     *            The MIB that needs to be queried
     * @param dataType
     *            {@link SnmpDataType}
     * @param value
     *            value to set
     * @param tableIndex
     *            The table index of the MIB to be queried
     * 
     * @return Command output
     */
    public static String snmpSetOnEmtaForInvalidCommunityString(AutomaticsTapApi tapEnv, Dut device, String mibOrOid,
	    SnmpDataType dataType, String value, String tableIndex, String mtaAddress) {

	String result = null;
	SnmpParams snmpParam = new SnmpParams();
	mibOrOid = mibOrOid + "." + tableIndex;
	LOGGER.info("mibOrOid to execute: " + mibOrOid);

	snmpParam.setSnmpCommand(SnmpCommand.SET);
	snmpParam.setSnmpCommunity(SnmpConstants.INVALID_COMMUNITY_STRING);
	snmpParam.setMibOid(mibOrOid);
	snmpParam.setValue(value);
	snmpParam.setDataType(dataType);
	snmpParam.setIpAddress(mtaAddress);
	result = tapEnv.executeSnmpCommand(device, snmpParam);
	return result;

    }

    /**
     * Utility method to execute SNMP SET command on Ware House Mibs
     * 
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance
     * @param device
     *            The device to be validated.
     * @param warehouseSnmp
     *            warehouse mibs to be set.
     * 
     * @return true , if Snmp set is successful
     */
    public static boolean snmpSetOnWareHouseMibs(AutomaticsTapApi tapEnv, Dut device,
	    BROADBAND_WAREHOUSE_DOCSIS_SNMP_LIST warehouseSnmp) {
	String snmpOutput = null;
	boolean status = false;
	String output = null;
	snmpOutput = BroadBandSnmpUtils.executeSnmpSetWithTableIndexOnRdkDevices(tapEnv, device, warehouseSnmp.getOid(),
		warehouseSnmp.getDataType(), warehouseSnmp.getValue(), warehouseSnmp.getTableIndex());

	if (!warehouseSnmp.getDataType().equals(SnmpDataType.HEXADECIMAL)) {
	    status = CommonMethods.isNotNull(snmpOutput) && snmpOutput.equals(warehouseSnmp.getValue());
	} else if (CommonMethods.isNotNull(snmpOutput)) {
	    output = snmpOutput.replaceAll("\\s", "");
	    LOGGER.info("ouput : " + output);
	    status = CommonMethods.isNotNull(output) && CommonUtils
		    .patternSearchFromTargetString(warehouseSnmp.getValue().toLowerCase(), output.toLowerCase());
	}
	return status;
    }

    /**
     * Method to execute SNMP Set and verify set value with SNMP Get execution
     * 
     * @param device
     *            The set-top instance
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param snmpMib
     *            MIB for which SNMP Set has to be executed
     * @param valueToBeSet
     *            Value to be set via SNMP Set
     * @param snmpDataType
     *            SNMP data type
     * @param tableIndex
     *            Index for which OID has to be executed
     * @return true if value is set and verified via SNMP Get
     */
    public static BroadBandResultObject snmpSetAndVerifySNMPResponseRdkDevices(Dut device, AutomaticsTapApi tapEnv,
	    String snmpMib, String valueToBeSet, SnmpDataType snmpDataType, String tableIndex) {
	LOGGER.debug("STARTING METHOD: snmpSetAndVerifyGetOnEcmIpWithIndex");
	// Variable declaration starts
	BroadBandResultObject executionStatus = new BroadBandResultObject();
	String errorMessage = "";
	boolean isSnmpSetVerified = false;
	long startTime = 0;
	// Variable declaration starts
	startTime = System.currentTimeMillis();
	String snmpGetResponse = null;
	try {
	    do {
		if (executeSnmpSetCommand(tapEnv, device, snmpMib, snmpDataType, valueToBeSet, tableIndex)) {
		    snmpGetResponse = BroadBandSnmpUtils.executeSnmpGetWithTableIndexOnRdkDevices(tapEnv, device,
			    snmpMib, tableIndex);
		    LOGGER.info("SNMP Get Response: " + snmpGetResponse);
		    isSnmpSetVerified = BroadBandCommonUtils.compareValues(
			    BroadBandTestConstants.CONSTANT_TXT_COMPARISON, valueToBeSet, snmpGetResponse);
		} else {
		    errorMessage = "SNMP set failed. Will try again in 30 seconds.";
		}
	    } while (!isSnmpSetVerified
		    && (System.currentTimeMillis() - startTime) < BroadBandTestConstants.TWO_MINUTE_IN_MILLIS
		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
	} catch (Exception e) {
	    errorMessage = e.getMessage();
	    LOGGER.error("Exception occured while setting SNMP value and cross checking via SNMP Get.");
	    LOGGER.error(executionStatus.getErrorMessage() + errorMessage);
	}
	executionStatus.setErrorMessage(errorMessage);
	executionStatus.setStatus(isSnmpSetVerified);
	LOGGER.debug("ENDING METHOD: snmpSetAndVerifyGetOnEcmIpWithIndex");
	return executionStatus;
    }

    /**
     * Method used to parse and validate the SNM,webpa response for down stream channel frequency
     * 
     * @param webParam
     *            webpa parameter
     * @param webpaJsonResponse
     *            webpa response in Json format
     * @param webpaParamPattern
     *            webpa parameter pattern
     * @param snmpResponse
     *            response for snmb mib
     * @return true if frequency obtained via snmp and webpa matches
     * @throws Exception
     */
    public static BroadBandResultObject parseAndValidateWebpaAndSnmpResponse(String webParam, String webpaJsonResponse,
	    String webpaParamPattern, String snmpResponse) throws Exception {
	LOGGER.debug("STARTING METHOD: parseAndValidateWebpaAndSnmpResponse");
	// Variable declaration starts
	BroadBandResultObject executionStatus = new BroadBandResultObject();
	boolean status = false;
	List<String> ExpectedWebpaValues = new ArrayList<String>();
	List<String> finalWebpaValues = new ArrayList<String>();
	List<String> finalSnmpValues = new ArrayList<String>();
	List<String> ExpectedWebpaList = null;
	List<String> ExpectedSnmpList = null;
	Map<String, String> webParamWithValue = null;
	String errorMessage = "";
	String frequency_range = BroadbandPropertyFileHandler.getRangeOfFrequency();
	// Variable declaration starts
	try {
	    try {
		ExpectedWebpaList = CommonUtils.patternFinderForMultipleMatches(webpaJsonResponse, webpaParamPattern);
		webParamWithValue = BroadBandCommonUtils.getJsonDataFromJsonArrayInMap(webpaJsonResponse);
	    } catch (Exception e) {
		throw new TestException(e.getMessage());
	    }
	    if (!(ExpectedWebpaList.isEmpty() && webParamWithValue.isEmpty())) {
		for (String value : ExpectedWebpaList) {
		    if (CommonUtils.patternSearchFromTargetString(
			    webParamWithValue.get(webParam.replace(BroadBandTestConstants.TR181_NODE_REF, value)),
			    BroadBandTestConstants.PATTERN_FOR_DWNSTRM_CHNL_FREQ_WEBPA_RESPONSE_MHZ)) {
			if (!value.equals(BroadBandTestConstants.STRING_ZERO) || CommonMethods.isNotNull(value)
				|| !value.equalsIgnoreCase(BroadBandTestConstants.EMPTY_STRING)) {
			    ExpectedWebpaValues.add(String
				    .valueOf(CommonMethods.patternFinder(
					    webParamWithValue.get(
						    webParam.replace(BroadBandTestConstants.TR181_NODE_REF, value)),
					    BroadBandTestConstants.PATTERN_FOR_DWNSTRM_CHNL_FREQ_WEBPA_RESPONSE))
				    .trim());
			}
		    }
		}
	    } else {
		errorMessage = "Failed to fetch downstream channel frequency value via Snmp or Webpa";
		throw new TestException(errorMessage);
	    }
	    for (String range : ExpectedWebpaValues) {
		if (BroadBandCommonUtils.compareValues(BroadBandTestConstants.INT_RANGE, frequency_range, range)) {
		    finalWebpaValues.add(range);
		} else {
		    errorMessage = "Downstream channel Frequency obtained via Webpa is not in expected range : "
			    + frequency_range;
		    throw new TestException(errorMessage);
		}
	    }

	    ExpectedSnmpList = CommonMethods.patternFinderToReturnAllMatchedString(snmpResponse,
		    BroadBandTestConstants.PATTERN_FOR_DWNSTRM_CHNL_FREQ_SNMP_RESPONSE);
	    for (String value : ExpectedSnmpList) {
		if (!value.equals(BroadBandTestConstants.STRING_ZERO) || CommonMethods.isNotNull(value)
			|| !value.equalsIgnoreCase(BroadBandTestConstants.EMPTY_STRING)) {
		    finalSnmpValues
			    .add(String
				    .valueOf(new BigInteger(value)
					    .divide(new BigInteger(BroadBandTestConstants.STRING_CONSTANT_1000000)))
				    .trim());
		}
	    }
	    LOGGER.info("List obtained through SnmpValues :" + finalSnmpValues);
	    LOGGER.info("List obtained through webpafinalValues :" + finalWebpaValues);
	    if (finalSnmpValues.containsAll(finalWebpaValues)) {
		status = BroadBandTestConstants.BOOLEAN_VALUE_TRUE;
	    } else {
		errorMessage = "Downstream channel Frequency obtained via Webpa and Snmp is not same";
	    }
	} catch (Exception e) {
	    LOGGER.error(
		    "Exception occured while getting SNMP value and cross checking via Webpa Get." + e.getMessage());
	}
	executionStatus.setErrorMessage(errorMessage);
	executionStatus.setStatus(status);
	LOGGER.debug("ENDING METHOD: parseAndValidateWebpaAndSnmpResponse");
	return executionStatus;
    }

    /**
     * Method to execute SNMP Get and validate the response
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            The device instance
     * @param snmpMibOrOid
     *            MIB for which SNMP Get has to be executed
     * @param tableIndex
     *            Index for which OID has to be executed
     * @param expectedValue
     *            Expected response for SNMP Get
     * @return true if SNMP get return the reponse same as expected value
     * @Refactor Sruthi Santhosh
     * 
     */
    public static boolean performSnmpGetOnRdkDevicesAndVerify(AutomaticsTapApi tapEnv, Dut device, String snmpMibOrOid,
	    String tableIndex, String expectedValue) {
	LOGGER.debug("STARTING METHOD: performSnmpGetOnRdkDevicesAndVerify");
	// Variable declaration starts
	String snmpGetResponse = "";
	String errorMessage = "";
	boolean isSnmpGetResponseVerified = false;
	long startTime = 0;
	// Variable declaration ends
	startTime = System.currentTimeMillis();
	try {
	    do {
		snmpGetResponse = BroadBandSnmpUtils.executeSnmpGetWithTableIndexOnRdkDevices(tapEnv, device,
			snmpMibOrOid, tableIndex);
		LOGGER.info("SNMP Get Response: " + snmpGetResponse);
		isSnmpGetResponseVerified = BroadBandCommonUtils
			.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON, expectedValue, snmpGetResponse);
	    } while (!isSnmpGetResponseVerified
		    && (System.currentTimeMillis() - startTime) < BroadBandTestConstants.TWO_MINUTE_IN_MILLIS
		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
	} catch (Exception e) {
	    errorMessage = e.getMessage();
	    LOGGER.error("Exception occured while getting resoonse and verifying from SNMP get.");
	    LOGGER.error(errorMessage);
	}
	LOGGER.debug("ENDING METHOD: performSnmpGetOnRdkDevicesAndVerify");
	return isSnmpGetResponseVerified;
    }

    /**
     * Method to validate the value of snmp mib
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link Dut}
     * @String snmpMib
     * @String expectedValue
     * 
     * @return status true if value of snmpmib is equal to that of expectedValue
     * 
     */
    public static boolean performSnmpWalkAndVerify(AutomaticsTapApi tapEnv, Dut device, String snmpMib,
	    String tableIndex, String expectedValue) {
	LOGGER.debug("STARTING METHOD: performSnmpWalkAndVerify");
	String commandResponse = null;
	boolean status = false;

	commandResponse = BroadBandSnmpUtils.executeSnmpGetWithTableIndexOnRdkDevices(tapEnv, device, snmpMib,
		tableIndex);

	if ((CommonMethods.isNotNull(commandResponse)) && commandResponse.equalsIgnoreCase(expectedValue)) {
	    status = true;
	}
	if (!status) {
	    LOGGER.error("Expected value for oid " + snmpMib + " is : " + expectedValue + " ACTUAL:" + commandResponse);
	    throw new TestException(
		    "Expected value for oid " + snmpMib + " is : " + expectedValue + " ACTUAL:" + commandResponse);
	}
	LOGGER.debug("ENDING METHOD: performSnmpWalkAndVerify");
	return status;

    }

    /**
     * Method to validate smnpv3 cert output
     * 
     * @param input
     *            Certificate data
     * @return status
     */
    public static boolean validateSnmpv3Cert(String input) {
	LOGGER.debug("STARTING METHOD: validateSnmpv3Cert");
	boolean status = false;
	Calendar calBefore = Calendar.getInstance();
	Calendar calAfter = Calendar.getInstance();
	SimpleDateFormat dateFormat = new SimpleDateFormat(BroadBandTestConstants.DATE_FORMAT_FOR_CERT);
	String certSignature = null;
	String notBeforeDate = null;
	String notAfterDate = null;
	try {
	    certSignature = CommonMethods.patternFinder(input, BroadBandTestConstants.REGEX_TO_RETRIEVE_CERT_SIGNATURE);
	    notBeforeDate = CommonMethods.patternFinder(input,
		    BroadBandTestConstants.REGEX_TO_RETRIEVE_CERT_NOT_BEFORE_DATE);
	    notAfterDate = CommonMethods.patternFinder(input,
		    BroadBandTestConstants.REGEX_TO_RETRIEVE_CERT_NOT_AFTER_DATE);
	    LOGGER.info("Certificate signature:" + certSignature);
	    LOGGER.info("Not before date:" + notBeforeDate);
	    LOGGER.info("Not after date:" + notAfterDate);
	    if (CommonMethods.isNotNull(certSignature) && CommonMethods.isNotNull(notAfterDate)
		    && CommonMethods.isNotNull(notBeforeDate)) {

		calBefore.setTime(dateFormat.parse(notBeforeDate));
		calAfter.setTime(dateFormat.parse(notAfterDate));
		status = CommonUtils.patternSearchFromTargetString(certSignature,
			BroadBandTestConstants.SNMPV3_CERT_SIGNATURE)
			&& (calAfter.get(Calendar.YEAR) - calBefore.get(Calendar.YEAR) == 3);
	    }
	} catch (ParseException e) {
	    LOGGER.error("Parse exception " + e.getMessage());
	} catch (Exception e) {
	    LOGGER.error("Exception caught while parsing output" + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD: validateSnmpv3Cert");
	return status;
    }

    /**
     * Method to perform snmpv3 walk query using ecm ip or estb ip
     * 
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance
     * @param device
     *            The device on which snmp command queried
     * @param oid
     *            The OID or MIB name
     * @return snmpwalk response
     * 
     * @author ArunKumar Jayachandran
     */
    public static String performSnmpWalkUsingSnmpV3(AutomaticsTapApi tapEnv, Dut device, String oid) {
	LOGGER.debug("STARTING METHOD:: performSnmpWalkUsingSnmpV3");
	String snmpOutput = null;
	String snmpProtocol = null;
	try {
	    LOGGER.info("Changing snmp protocol from V2 to v3");
	    String currentProtocol = System.getProperty("snmpVersion");
	    LOGGER.info("Current protocol is: " + currentProtocol);
	    System.setProperty("snmpVersion", SnmpProtocol.SNMP_V3.toString());
	    snmpProtocol = System.getProperty("snmpVersion", SnmpProtocol.SNMP_V2.toString());
	    LOGGER.info("Converted SNMP Protocol to: " + snmpProtocol + " for running this test case!");

	    if (DeviceModeHandler.isFibreDevice(device) && !validateSnmpV3EcmIpApplicableModels(device)) {
		snmpOutput = CommonMethods.snmpWalkOnEstb(tapEnv, device, oid);
	    } else {
		snmpOutput = snmpWalkOnEcm(tapEnv, device, oid);
	    }
	    int retryCount = 0;
	    while (getRetries(snmpOutput, snmpProtocol, retryCount)) {
		LOGGER.info("Retrying snmp .Retry count :" + retryCount);

		if (DeviceModeHandler.isFibreDevice(device) && !validateSnmpV3EcmIpApplicableModels(device)) {
		    snmpOutput = CommonMethods.snmpWalkOnEstb(tapEnv, device, oid);
		} else {
		    snmpOutput = snmpWalkOnEcm(tapEnv, device, oid);
		}
		retryCount++;
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception occurred while verifying the SNMP walk query using V3:" + e.getMessage());
	} finally {
	    LOGGER.info("Changing snmp protocol from V3 to v2");
	    System.setProperty("snmpVersion", SnmpProtocol.SNMP_V2.toString());
	    snmpProtocol = System.getProperty("snmpVersion", SnmpProtocol.SNMP_V2.toString());
	    LOGGER.info("Converted SNMP protocol back to " + snmpProtocol + " after performing snmpv3 query");
	}
	LOGGER.debug("ENDING METHOD:: performSnmpWalkUsingSnmpV3");
	return parseSnmpMibValueFromSnmpOutput(device, snmpOutput, oid);
    }

    /**
     * Method to validate ECM IP based on device model
     * 
     * @param device
     *            Dut instance
     * @return status - Return true
     */
    public static boolean validateSnmpV3EcmIpApplicableModels(Dut device) {
	LOGGER.debug("STARTING METHOD: validateSnmpV3EcmIpApplicableModels");
	boolean isSnmpv3EcmApplicable = false;
	String snmpv3EcmApplicableModels = AutomaticsPropertyUtility.getProperty("snmpv3.ecm.applicable.models");
	LOGGER.info("snmpv3EcmApplicableModels: " + snmpv3EcmApplicableModels);
	if (snmpv3EcmApplicableModels.contains(device.getModel())) {
	    isSnmpv3EcmApplicable = true;
	}
	LOGGER.debug("ENDING METHOD:: validateSnmpV3EcmIpApplicableModels");
	return isSnmpv3EcmApplicable;
    }

    /**
     * Method to parse the snmp response
     * 
     * @param device
     *            The device on which snmp command queried
     * @param snmpOutput
     *            Snmp response
     * @param mib
     *            The OID or MIB name
     * @return parsed output
     */
    public static String parseSnmpMibValueFromSnmpOutput(Dut device, String snmpOutput, String mib) {
	String snmpResult = null;
	String[] requiredValues = snmpOutput.split("=");
	if (requiredValues.length > 0) {
	    snmpResult = requiredValues[requiredValues.length - 1];
	    if (CommonMethods.isNotNull(snmpResult)) {
		snmpResult = snmpResult.replaceAll("\"", "").trim();
	    }

	    return snmpResult;
	} else {
	    throw new FailedTransitionException(GeneralError.SNMP_COMPARISON_FAILURE,
		    "SNMP command not executed properly : snmp output is = " + snmpOutput);
	}
    }

    /**
     * Method for snmpv3 set
     * 
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param device
     *            Dut instance
     * @param oid
     *            String mib or oid
     * @param dataType
     *            String datatype
     * @param value
     *            String value to be set
     * @param tableIndex
     *            String index for mib
     * @return String parsedoutput
     */
    public static String performSnmpSetUsingSnmpV3(AutomaticsTapApi tapEnv, Dut device, String oid,
	    SnmpDataType dataType, String value) {
	LOGGER.debug("STARTING METHOD:: performSnmpWalkUsingSnmpV3");
	String snmpOutput = null;
	String snmpProtocol = null;
	try {
	    LOGGER.info("Changing snmp protocol from V2 to v3");
	    String currentProtocol = System.getProperty("snmpVersion");
	    LOGGER.info("Current protocol is: " + currentProtocol);
	    System.setProperty("snmpVersion", SnmpProtocol.SNMP_V3.toString());
	    snmpProtocol = System.getProperty("snmpVersion", SnmpProtocol.SNMP_V2.toString());
	    LOGGER.info("Converted SNMP Protocol to: " + snmpProtocol + " for running this test case!");
	    if (DeviceModeHandler.isFibreDevice(device) && !validateSnmpV3EcmIpApplicableModels(device)) {
		snmpOutput = snmpSetOnEstb(tapEnv, device, oid, dataType, value);
	    } else {
		snmpOutput = snmpSetOnEcm_V3(tapEnv, device, oid, dataType, value);
	    }
	    int retryCount = 0;
	    while (getRetries(snmpOutput, snmpProtocol, retryCount)) {
		LOGGER.info("Retrying snmp .Retry count :" + retryCount);

		if (DeviceModeHandler.isFibreDevice(device) && !validateSnmpV3EcmIpApplicableModels(device)) {
		    snmpOutput = snmpSetOnEstb(tapEnv, device, oid, dataType, value);
		} else {
		    snmpOutput = snmpSetOnEcm_V3(tapEnv, device, oid, dataType, value);
		}
		retryCount++;
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception occurred while verifying the SNMP walk query using V3:" + e.getMessage());
	} finally {
	    LOGGER.info("Changing snmp protocol from V3 to v2");
	    System.setProperty("snmpVersion", SnmpProtocol.SNMP_V2.toString());
	    snmpProtocol = System.getProperty("snmpVersion", SnmpProtocol.SNMP_V2.toString());
	    LOGGER.info("Converted SNMP protocol back to " + snmpProtocol + " after performing snmpv3 query");
	}
	LOGGER.debug("ENDING METHOD:: performSnmpWalkUsingSnmpV3");
	return parseSnmpMibValueFromSnmpOutput(device, snmpOutput, oid);
    }

    /**
     * Utility method to execute SNMP get with table index on ECM side.
     * 
     * @param AutomaticsTapApi
     *            The {@link AutomaticsTapApi} instance.
     * @param device
     *            The device to be queried.
     * @param mibOrOid
     *            The MIB or OID need to be queried.
     * @param tableIndex
     *            The table index.
     * @return Command output.
     */
    public static String snmpSetOnEstb(AutomaticsTapApi tapEnv, Dut device, String mibOrOid, SnmpDataType datatype,
	    String value) {

	String result = null;
	SnmpParams snmpParam = null;
	String snmpProtocol = System.setProperty(SnmpConstants.SYSTEM_PARAM_SNMP_VERSION,
		SnmpProtocol.SNMP_V2.toString());
	LOGGER.info("Current SNMP protocol: " + snmpProtocol);

	snmpParam = new SnmpParams();
	snmpParam.setSnmpCommand(SnmpCommand.SET);
	snmpParam.setSnmpCommunity("CUSTOM");
	snmpParam.setMibOid(mibOrOid.trim());
	snmpParam.setCommandOption("-t 10 ");
	snmpParam.setCommandOption("-OQ -v 3 -u docsisManager");
	snmpParam.setDataType(datatype);
	snmpParam.setValue(value);
	snmpParam.setIpAddress(CommonUtils.getIPAddress(device));

	result = tapEnv.executeSnmpCommand(device, snmpParam);

	for (int retryCount = 0; getRetries(result, snmpProtocol, retryCount); ++retryCount) {
	    LOGGER.info("Retrying snmp .Retry count :" + retryCount);
	    snmpParam = new SnmpParams();
	    snmpParam.setSnmpCommand(SnmpCommand.SET);
	    snmpParam.setSnmpCommunity("CUSTOM");
	    snmpParam.setMibOid(mibOrOid.trim());
	    snmpParam.setCommandOption("-t 10 ");
	    snmpParam.setCommandOption("-OQ -v 3 -u docsisManager");
	    snmpParam.setDataType(datatype);
	    snmpParam.setValue(value);
	    snmpParam.setIpAddress(CommonUtils.getIPAddress(device));

	    result = tapEnv.executeSnmpCommand(device, snmpParam);
	}
	return result;
    }

    /**
     * Method to execute enable/disable Wifi radio by SNMP
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            The set-top instance
     * @param expectedValue
     *            Expected response for SNMP Get
     * @return true if able to disable 2.4ghz and 5ghz radio
     * 
     * @refactor yamini.s
     */
    public static boolean performEnableOrDisableWifiRadioBySnmp(AutomaticsTapApi tapEnv, Dut device,
	    String expectedValue) {
	LOGGER.debug("STARTING METHOD: performEnableOrDisableWifiRadioBySnmp");
	// Variable declaration starts
	String snmpResponse = "";
	String errorMessage = "";
	boolean status = false;
	boolean status2Ghz = false;
	boolean status5Ghz = false;
	boolean applySettings = false;
	long startTime;
	// Variable declaration ends
	try {
	    startTime = System.currentTimeMillis();
	    do {
		snmpResponse = BroadBandSnmpUtils.retrieveSnmpSetOutputWithGivenIndexOnRdkDevices(device, tapEnv,
			BroadBandSnmpMib.WIFI_2_4_SSID_STATUS.getOid(), SnmpDataType.INTEGER, expectedValue,
			BroadBandSnmpMib.WIFI_2_4_SSID_STATUS.getTableIndex());
		status2Ghz = CommonMethods.isNotNull(snmpResponse) && snmpResponse.equals(expectedValue)
			&& BroadBandSnmpUtils
				.executeSnmpGetWithTableIndexOnRdkDevices(tapEnv, device,
					BroadBandSnmpMib.WIFI_2_4_SSID_STATUS.getOid(),
					BroadBandSnmpMib.WIFI_2_4_SSID_STATUS.getTableIndex())
				.equalsIgnoreCase(expectedValue);
	    } while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.TWO_MINUTE_IN_MILLIS
		    && !status2Ghz
		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS));
	    if (status2Ghz) {
		startTime = System.currentTimeMillis();
		do {
		    snmpResponse = BroadBandSnmpUtils.retrieveSnmpSetOutputWithGivenIndexOnRdkDevices(device, tapEnv,
			    BroadBandSnmpMib.WIFI_5_SSID_STATUS.getOid(), SnmpDataType.INTEGER, expectedValue,
			    BroadBandSnmpMib.WIFI_5_SSID_STATUS.getTableIndex());
		    status5Ghz = CommonMethods.isNotNull(snmpResponse) && snmpResponse.equals(expectedValue)
			    && BroadBandSnmpUtils
				    .executeSnmpGetWithTableIndexOnRdkDevices(tapEnv, device,
					    BroadBandSnmpMib.WIFI_5_SSID_STATUS.getOid(),
					    BroadBandSnmpMib.WIFI_5_SSID_STATUS.getTableIndex())
				    .equalsIgnoreCase(expectedValue);
		} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.TWO_MINUTE_IN_MILLIS
			&& !status5Ghz && BroadBandCommonUtils.hasWaitForDuration(tapEnv,
				BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS));
	    } else {
		LOGGER.info("Unable to Enable/disable 2.4 Ghz Wifi Radio by SNMP");
	    }
	    if (status5Ghz) {
		startTime = System.currentTimeMillis();
		do {
		    snmpResponse = BroadBandSnmpUtils.retrieveSnmpSetOutputWithDefaultIndexOnRdkDevices(device, tapEnv,
			    BroadBandSnmpMib.WIFI_APPLY_SETTINGS_WITH_INDEX.getOid(), SnmpDataType.INTEGER,
			    BroadBandTestConstants.STRING_VALUE_ONE);
		    applySettings = CommonMethods.isNotNull(snmpResponse)
			    && snmpResponse.equals(BroadBandTestConstants.STRING_VALUE_ONE);
		} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.TWO_MINUTE_IN_MILLIS
			&& !applySettings && BroadBandCommonUtils.hasWaitForDuration(tapEnv,
				BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS));
	    } else {
		LOGGER.info("Unable to Enable/disable 5Ghz Wifi Radio by SNMP");
	    }
	    if (applySettings) {
		LOGGER.info("Wait for 5 min to affect changes");
		tapEnv.waitTill(BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS);
	    } else {
		LOGGER.info("Unable to Apply Settings for Enable/disable Wifi Radio by SNMP");
	    }
	    status = status2Ghz && status5Ghz && applySettings;
	} catch (Exception e) {
	    errorMessage = e.getMessage();
	    LOGGER.error("Exception occured while enable/disable Wifi radio by SNMP" + errorMessage);
	}
	LOGGER.debug("ENDING METHOD: performEnableOrDisableWifiRadioBySnmp");
	return status;
    }

    /**
     * Method to perform reboot using cmd and verify the device is accessible without verifying the processes are up
     * using snmp
     * 
     * * @param tapEnv instance of {@link AutomaticsTapApi}
     * 
     * @param device
     *            instance of {@link Dut}
     * @return true if device is up after reboot
     * @refactor Govardhan
     */
    public static boolean rebootUsingCmdAndWaitForStbAccessibleUsingSnmp(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug("STARTING METHOD: rebootAndWaitForStbAccessibleUsingSnmp()");
	boolean isRebooted = false;
	boolean isStbAccessible = false;
	String errorMessage = "Failed to perform reboot";
	try {
//	    tapEnv.executeCommandUsingSsh(device, LinuxCommandConstants.CMD_REBOOT);
//	    LOGGER.info("Device reboot initiated");
//	    
//		AutomaticsUtils.sleep(AutomaticsConstants.TEN_SECONDS);
		
	    if (CommonMethods.rebootAndWaitForIpAccusition(device, tapEnv)) {
		isStbAccessible = getSystemUpTimeUsingSnmp(tapEnv, device, BroadBandTestConstants.TEN_MINUTE_IN_MILLIS,
			BroadBandTestConstants.BOOLEAN_VALUE_TRUE, BroadBandTestConstants.CONSTANT_420);
		LOGGER.info("Device is accessible after reboot :" + isStbAccessible);
	    }
	} catch (Exception exception) {
	    errorMessage = "Exception occurred rebootAndWaitForStbAccessibleUsingSnmp() " + exception.getMessage();
	    LOGGER.error(errorMessage);
	}
	LOGGER.debug("ENDING METHOD: rebootAndWaitForStbAccessibleUsingSnmp()");
	return isStbAccessible;
    }

    /**
     * Utility method used to verify the system up time using snmp
     * 
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link Dut}
     * @param pollDuration
     *            Poll Duration
     * @param isAccessible
     *            True - Do the poll operation to get wait time .
     * @param hardWaitTime
     *            hard wait time after reboot
     * @return -True - It will return the output depends on isWaitTimeExpected
     * @refactor Govardhan
     */
    public static boolean getSystemUpTimeUsingSnmp(AutomaticsTapApi tapEnv, Dut device, long pollDuration,
	    boolean isAccessible, int hardWaitTime) {
	LOGGER.info("STARTING METHOD : getSystemUpTimeUsingSnmp()");
	boolean result = false;
	String snmpCommandOutput = null;
	int actualWaitTime = BroadBandTestConstants.CONSTANT_0;
	int snmpUptimeInSeconds = BroadBandTestConstants.CONSTANT_0;
	try {
	    long startTime = System.currentTimeMillis();
	    do {
		try {
		    snmpCommandOutput = BroadBandSnmpUtils.executeSnmpWalkOnRdkDevices(tapEnv, device,
			    BroadBandSnmpMib.ESTB_SYS_UP_TIME.getOid());
		    LOGGER.info("snmpCommandOutput :" + snmpCommandOutput);
		    if (isAccessible) {
			LOGGER.info("Verifying valid SNMP Response");
			result = hasNoSNMPErrorOnResponse(tapEnv, device, snmpCommandOutput) && CommonMethods
				.patternMatcher(snmpCommandOutput, BroadBandTestConstants.SYS_UP_TIME_INSTANCE);
			if (result) {
			    LOGGER.info("Verifying system up time");
			    snmpUptimeInSeconds = convertSysUpTimeToSeconds(snmpCommandOutput);
			    LOGGER.info("snmp uptime in seconds: " + snmpUptimeInSeconds);
			    if (snmpUptimeInSeconds < hardWaitTime) {
				actualWaitTime = hardWaitTime - snmpUptimeInSeconds;
				LOGGER.info("Waiting for " + actualWaitTime + " seconds");
				tapEnv.waitTill(actualWaitTime * BroadBandTestConstants.ONE_SECOND_IN_MILLIS);
				snmpCommandOutput = BroadBandSnmpUtils.executeSnmpWalkOnRdkDevices(tapEnv, device,
					BroadBandSnmpMib.ESTB_SYS_UP_TIME.getOid());
				LOGGER.info("snmpCommandOutput :" + convertSysUpTimeToSeconds(snmpCommandOutput));
			    }
			}
		    } else {
			LOGGER.info("Verifying invalid SNMP Response");
			result = !hasNoSNMPErrorOnResponse(tapEnv, device, snmpCommandOutput) && !CommonMethods
				.patternMatcher(snmpCommandOutput, BroadBandTestConstants.SYS_UP_TIME_INSTANCE);
		    }
		} catch (Exception e) {
		    LOGGER.error("Failed to verify sysuptime" + e.getMessage());
		}
	    } while (!result && (System.currentTimeMillis() - startTime) < pollDuration
		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
	} catch (Exception e) {
	    LOGGER.error("Exception Occurred in getSystemUpTimeUsingSnmp():" + e.getMessage());
	}
	LOGGER.info("ENDING METHOD : getSystemUpTimeUsingSnmp()");
	return result;
    }

    /**
     * Utility method used to convert the system up time to seconds
     * 
     * @param response
     *            Response to convert
     * @return snmpUptimeInSeconds . It will return the seconds
     * @refactor Govardhan
     */
    public static int convertSysUpTimeToSeconds(String response) {
	LOGGER.debug("STARTING METHOD : convertSysUpTimeToSeconds()");
	int snmpUptimeInSeconds = BroadBandTestConstants.CONSTANT_0;
	try {
	    response = CommonMethods.patternFinder(response, BroadBandTestConstants.SYS_UP_TIME_INSTANCE);
	    LOGGER.info("Uptime from Snmp = " + response);
	    int day = BroadBandCommonUtils.convertStringToInteger((response
		    .split(BroadBandTestConstants.ESCAPE_SEQUENCE_CHARACTER_COLON)[BroadBandTestConstants.CONSTANT_0]));
	    int hour = BroadBandCommonUtils.convertStringToInteger((response
		    .split(BroadBandTestConstants.ESCAPE_SEQUENCE_CHARACTER_COLON)[BroadBandTestConstants.CONSTANT_1]));
	    int min = BroadBandCommonUtils.convertStringToInteger((response
		    .split(BroadBandTestConstants.ESCAPE_SEQUENCE_CHARACTER_COLON)[BroadBandTestConstants.CONSTANT_2]));
	    int sec = BroadBandCommonUtils.convertStringToInteger((response
		    .split(BroadBandTestConstants.ESCAPE_SEQUENCE_CHARACTER_COLON)[BroadBandTestConstants.CONSTANT_3]));
	    snmpUptimeInSeconds = (day * BroadBandTestConstants.CONSTANT_24
		    * BroadBandTestConstants.INTERGER_CONSTANT_3600)
		    + (hour * BroadBandTestConstants.INTERGER_CONSTANT_3600)
		    + (min * BroadBandTestConstants.CONSTANT_60) + sec;
	    LOGGER.info("snmp uptime in seconds: " + snmpUptimeInSeconds);
	} catch (Exception e) {
	    LOGGER.error("Exception Occurred in convertSysUpTimeToSeconds():" + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : convertSysUpTimeToSeconds()");
	return snmpUptimeInSeconds;
    }

    /**
     * Helper method to get Atom Device system up time
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @return the true if Atom Device uptime is greater than 20 mis
     * @refactor Govardhan
     */
    public static boolean getAtomDeviceUptimeStatusUsingSnmp(Dut device, AutomaticsTapApi tapEnv) {
	LOGGER.debug("STARTING METHOD : getAtomDeviceUptimeStatusUsingSnmp()");
	String response = null;
	int uptimeInMin = BroadBandTestConstants.CONSTANT_0;
	boolean status = false;
	long startTime = System.currentTimeMillis();
	String errorMessage = null;
	try {
	    do {
		errorMessage = "SNMP process is not up";
		response = BroadBandSnmpUtils.executeSnmpWalkOnRdkDevices(tapEnv, device,
			BroadBandSnmpMib.ESTB_SYS_UP_TIME.getOid());
		LOGGER.info("Device uptime " + response);
		status = CommonMethods.isNotNull(response);
		if (status) {
		    errorMessage = "Device uptime is not greater than 20 mins";
		    status = false;
		    uptimeInMin = convertSysUpTimeToSeconds(response) / BroadBandTestConstants.CONSTANT_60;
		    if (!(uptimeInMin >= BroadBandTestConstants.CONSTANT_20)) {
			BroadBandCommonUtils.hasWaitForDuration(tapEnv,
				(BroadBandTestConstants.CONSTANT_20 - uptimeInMin) * 60000);
			response = BroadBandSnmpUtils.executeSnmpWalkOnRdkDevices(tapEnv, device,
				BroadBandSnmpMib.ESTB_SYS_UP_TIME.getOid());
			uptimeInMin = convertSysUpTimeToSeconds(response) / BroadBandTestConstants.CONSTANT_60;
			if (uptimeInMin >= BroadBandTestConstants.CONSTANT_20) {
			    status = true;
			    LOGGER.info("Device uptime is greater than 20 mins " + status);
			}
		    } else {
			status = true;
			LOGGER.info("Device uptime is greater than 20 mins " + status);
		    }
		}
	    } while (!status && (System.currentTimeMillis() - startTime) < BroadBandTestConstants.EIGHT_MINUTE_IN_MILLIS
		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
	    if (!status) {
		throw new TestException(
			"Exception occured : SNMP process is not up/ Device uptime is not greater than 20 mins "
				+ errorMessage);
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception occurred while retrieving partner value " + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : getAtomDeviceUptimeStatusUsingSnmp()");
	return status;
    }

    /**
     * Utility method used to perform wifi reset/factory reset
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @param isFactoryReset
     *            True- Perform Factory Reset,False- WiFi Reset
     * @return True- Factory/WiFi Reset is successful
     * @refactor Govardhan
     */
    public static boolean performResetUsingSnmp(AutomaticsTapApi tapEnv, Dut device, boolean isFactoryReset) {
	LOGGER.debug("STARTING METHOD : performResetUsingSnmp()");
	boolean result = false;
	String snmpOutput = null;
	boolean isRebooted = false;
	try {
	    if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
		getAtomDeviceUptimeStatusUsingSnmp(device, tapEnv);
	    }
	    if (DeviceModeHandler.isFibreDevice(device)) {
		snmpOutput = snmpSetOnEstb(tapEnv, device, BroadBandSnmpMib.ESTB_FACTORY_RESET_DEVICE.getOid(),
			SnmpDataType.INTEGER, isFactoryReset ? BroadBandTestConstants.STRING_VALUE_ONE
				: BroadBandTestConstants.STRING_VALUE_THREE).trim();
	    } else {
		snmpOutput = snmpSetOnEcm(tapEnv, device, BroadBandSnmpMib.ESTB_FACTORY_RESET_DEVICE.getOid(),
			SnmpDataType.INTEGER, isFactoryReset ? BroadBandTestConstants.STRING_VALUE_ONE
				: BroadBandTestConstants.STRING_VALUE_THREE).trim();
	    }
	    result = hasNoSNMPErrorOnResponse(tapEnv, device, snmpOutput)
		    && snmpOutput.equals(isFactoryReset ? BroadBandTestConstants.STRING_VALUE_ONE
			    : BroadBandTestConstants.STRING_VALUE_THREE);
	    if (result) {
		result = false;
		if (isFactoryReset) {
		    isRebooted = getSystemUpTimeUsingSnmp(tapEnv, device, BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS,
			    BroadBandTestConstants.BOOLEAN_VALUE_FALSE, BroadBandTestConstants.CONSTANT_540);
		    LOGGER.info("Device is rebooted :" + isRebooted);
		    if (isRebooted) {
			result = getSystemUpTimeUsingSnmp(tapEnv, device,
				BroadBandTestConstants.FIFTEEN_MINUTES_IN_MILLIS,
				BroadBandTestConstants.BOOLEAN_VALUE_TRUE, BroadBandTestConstants.CONSTANT_540);
			LOGGER.info("Device is accessible after reboot :" + result);
		    }
		} else {
		    result = getSystemUpTimeUsingSnmp(tapEnv, device, BroadBandTestConstants.FIFTEEN_MINUTES_IN_MILLIS,
			    BroadBandTestConstants.BOOLEAN_VALUE_TRUE, BroadBandTestConstants.CONSTANT_540);
		}
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception Occurred in performResetUsingSnmp():" + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : performResetUsingSnmp()");
	return result;
    }

    /**
     * Utility method used to get the ssid using snmp
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link Dut}
     * @param wifiBand
     *            2.4/5 GHz
     * @return It will return the ssid based on wifiBand
     * @refactor Govardhan
     */
    public static String getSsidUsingSnmp(AutomaticsTapApi tapEnv, Dut device, String wifiBand) {
	LOGGER.debug("STARTING METHOD : getSsidUsingSnmp()");
	String ssid = null;
	String oid = null;
	String mibWithOutIndex = null;
	String tableIndex = null;
	try {
	    if (wifiBand.equalsIgnoreCase(BroadBandTestConstants.BAND_2_4GHZ)) {
		oid = BroadBandSnmpMib.ECM_WIFI_SSID_2_4.getOid();
	    } else if (wifiBand.equalsIgnoreCase(BroadBandTestConstants.BAND_5GHZ)) {
		oid = BroadBandSnmpMib.ECM_WIFI_SSID_5.getOid();
	    }
	    LOGGER.info("OID :" + oid);
	    mibWithOutIndex = oid.substring(BroadBandTestConstants.CONSTANT_1,
		    oid.lastIndexOf(BroadBandTestConstants.DOT_OPERATOR));
	    tableIndex = oid.substring(
		    oid.lastIndexOf(BroadBandTestConstants.DOT_OPERATOR) + BroadBandTestConstants.CONSTANT_1);
	    ssid = executeSnmpGetWithTableIndexOnRdkDevices(tapEnv, device, mibWithOutIndex, tableIndex);
	    LOGGER.info(" SSID :" + ssid);
	} catch (Exception e) {
	    LOGGER.error("Exception Occurred in getSsidUsingSnmp():" + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : getSsidUsingSnmp()");
	return ssid;
    }

    /**
     * Utility method used to validate the default ssid and password using snmp
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link Dut}
     * @return -True Default ssid verified, Else False
     * @refactor Govardhan
     */
    public static boolean verifyDefaultSsidAndPasswordUsingSnmp(AutomaticsTapApi tapEnv, Dut device) {
	LOGGER.debug("STARTING METHOD : verifyDefaultSsidAndPasswordUsingSnmp()");
	boolean result = false;
	String ssidFor2Ghz = null;
	String ssidFor5Ghz = null;
	String passwordFor2Ghz = null;
	String passwordFor5Ghz = null;
	try {
	    long startTime = System.currentTimeMillis();
	    do {
		ssidFor2Ghz = getSsidUsingSnmp(tapEnv, device, BroadBandTestConstants.BAND_2_4GHZ);
		LOGGER.info("Default SSID for 2.4 GHz :" + ssidFor2Ghz);
		ssidFor5Ghz = getSsidUsingSnmp(tapEnv, device, BroadBandTestConstants.BAND_5GHZ);
		LOGGER.info("Default SSID for 5 GHz :" + ssidFor5Ghz);
		passwordFor2Ghz = executeSnmpGetWithTableIndexOnRdkDevices(tapEnv, device,
			BroadBandSnmpMib.ECM_PRIVATE_WIFI_2_4_WIRELESSPASS.getOid(),
			BroadBandSnmpMib.ECM_PRIVATE_WIFI_2_4_WIRELESSPASS.getTableIndex());
		LOGGER.info("Default password for 2.4 GHz :" + passwordFor2Ghz);
		passwordFor5Ghz = executeSnmpGetWithTableIndexOnRdkDevices(tapEnv, device,
			BroadBandSnmpMib.ECM_PRIVATE_WIFI_5_WIRELESSPASS.getOid(),
			BroadBandSnmpMib.ECM_PRIVATE_WIFI_5_WIRELESSPASS.getTableIndex());
		LOGGER.info("Default password for 5 GHz :" + passwordFor5Ghz);
		result = hasNoSNMPErrorOnResponse(tapEnv, device, ssidFor2Ghz)
			&& hasNoSNMPErrorOnResponse(tapEnv, device, ssidFor5Ghz)
			&& hasNoSNMPErrorOnResponse(tapEnv, device, passwordFor2Ghz)
			&& hasNoSNMPErrorOnResponse(tapEnv, device, passwordFor5Ghz);
	    } while (!result
		    && (System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIFTEEN_MINUTES_IN_MILLIS
		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));

	} catch (Exception e) {
	    LOGGER.error("Exception Occurred in verifyDefaultSsidAndPasswordUsingSnmp():" + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : verifyDefaultSsidAndPasswordUsingSnmp()");
	return result;
    }

    /**
     * Utility method used to set/verify the all ssid status using snmp
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link Dut}
     * @param expectedValue
     *            Expected value
     * @param isSetOper
     *            True - Set Operation, False-Get and verify operation
     * @return -True - Set/Verify operation is success, Else-False
     * @refactor Govardhan
     * 
     */
    public static boolean setOrVerifyAllSsidStatusUsingSnmp(AutomaticsTapApi tapEnv, Dut device, String expectedValue,
	    boolean isSetOper, boolean isdefault) {
	LOGGER.debug("STARTING METHOD : setOrVerifyAllSsidStatusUsingSnmp()");
	boolean result = false;
	String response = null;
	String errorMessage = "";
	boolean isBusinessClsDevice = DeviceModeHandler.isBusinessClassDevice(device);
	int resultCount = BroadBandTestConstants.CONSTANT_0;
	List<String> indexList = BroadBandSnmpConstants.RDKB_WIFI_STATUS_SNMP_MIBS;
	if (isBusinessClsDevice) {
	    indexList = BroadBandSnmpConstants.RDKB_WIFI_STATUS_SNMP_MIBS_BUSINESSCLASS;
	}
	try {
	    for (String index : indexList) {
		if (isSetOper) {
		    LOGGER.info("Performing snmp set operation");
		    response = BroadBandSnmpUtils.executeSnmpSetWithTableIndexOnRdkDevices(tapEnv, device,
			    BroadBandSnmpMib.ECM_STATUS_PRIVATE_WIFI_5_GHZ.getOid(), SnmpDataType.INTEGER,
			    expectedValue, index);
		    result = hasNoSNMPErrorOnResponse(tapEnv, device, response)
			    && response.equalsIgnoreCase(expectedValue);
		    tapEnv.waitTill(BroadBandTestConstants.FIVE_SECONDS_IN_MILLIS);
		} else {
		    LOGGER.info("Performing snmp get operation");
		    if (index.equalsIgnoreCase("10102") && isdefault) {
			expectedValue = BroadBandTestConstants.STRING_CONSTANT_2;
		    }
		    result = BroadBandSnmpUtils.performSnmpGetOnRdkDevicesAndVerify(tapEnv, device,
			    BroadBandSnmpMib.ECM_STATUS_PRIVATE_WIFI_5_GHZ.getOid(), index, expectedValue);
		    if (index.equalsIgnoreCase("10102") && isdefault) {
			expectedValue = BroadBandTestConstants.STRING_CONSTANT_1;
		    }
		}
		if (result) {
		    LOGGER.info("Successfully " + (isSetOper ? "set" : "retrieved") + " the "
			    + BroadBandSnmpMib.ECM_STATUS_PRIVATE_WIFI_5_GHZ.getOid() + "." + index + " status");
		    resultCount = resultCount + BroadBandTestConstants.CONSTANT_1;
		} else {
		    errorMessage += "Failed to " + (isSetOper ? "set" : "verify") + " the "
			    + BroadBandSnmpMib.ECM_STATUS_PRIVATE_WIFI_5_GHZ.getOid() + "." + index + " status as "
			    + expectedValue + "\n";
		}
	    }
	    result = (resultCount == indexList.size());
	    LOGGER.info("IndexList size:" + indexList.size());
	    LOGGER.info("Result Count:" + errorMessage);
	    LOGGER.error(errorMessage);
	} catch (Exception e) {
	    LOGGER.error("Exception Occured in setOrVerifyAllSsidStatusUsingSnmp():" + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : setOrVerifyAllSsidStatusUsingSnmp()");
	return result;
    }

    /**
     * Utility Method to set the custom ssid and password for 2.4 and 5 GHz private wifi
     * 
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param device
     *            {@link Dut}
     * @return Ture- Custom ssid or password set is success,Else-False
     * @refactor Govardhan
     * 
     */
    public static boolean setAndVerifyTheCustomSsidAndPassword(AutomaticsTapApi tapEnv, Dut device) {
	LOGGER.debug("STARTING METHOD : setAndVerifyTheCustomSsidAndPassword()");
	boolean result = false;
	String paramValue = null;
	String snmpOutput = null;
	boolean is2GhzSsidSet = false;
	boolean is5GhzSsidSet = false;
	boolean is2GhzPwdSet = false;
	boolean is5GhzPwdSet = false;
	String hostMacAddress = device.getHostMacAddress();
	hostMacAddress = CommonMethods.isNotNull(hostMacAddress)
		? hostMacAddress.replaceAll(BroadBandTestConstants.DELIMITER_COLON, BroadBandTestConstants.EMPTY_STRING)
		: null;
	LOGGER.info("HOST MAC ADDRESS (FORMATTED): " + hostMacAddress);
	try {
	    // Setting the 2.4GHz Private SSID Value.
	    paramValue = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_2_4_PRIVATE_SSID);
	    if (CommonMethods.isNotNull(hostMacAddress) && CommonMethods.isNotNull(paramValue)) {
		paramValue = paramValue.replace(BroadBandTestConstants.PLACE_HOLDER_FOR_MAC_ADDRESS,
			hostMacAddress.substring(hostMacAddress.length() - BroadBandTestConstants.CONSTANT_4));
		paramValue = paramValue.replace(BroadBandTestConstants.DOUBLE_QUOTE,
			BroadBandTestConstants.EMPTY_STRING);
		snmpOutput = BroadBandSnmpUtils.retrieveSnmpSetOutputWithGivenIndexOnRdkDevices(device, tapEnv,
			BroadBandSnmpMib.ECM_PRIVATE_WIFI_SSID_2_4.getOid(), SnmpDataType.STRING, paramValue,
			BroadBandSnmpMib.ECM_PRIVATE_WIFI_SSID_2_4.getTableIndex());
		is2GhzSsidSet = CommonMethods.isNotNull(snmpOutput) && snmpOutput.equals(paramValue);
	    }
	    LOGGER.info("Successfully set the 2.4 GHz private ssid: " + is2GhzSsidSet);

	    // Setting the 5GHz Private SSID Value.
	    paramValue = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_5_PRIVATE_SSID);
	    if (CommonMethods.isNotNull(hostMacAddress) && CommonMethods.isNotNull(paramValue)) {
		paramValue = paramValue.replace(BroadBandTestConstants.PLACE_HOLDER_FOR_MAC_ADDRESS,
			hostMacAddress.substring(hostMacAddress.length() - BroadBandTestConstants.CONSTANT_4));
		paramValue = paramValue.replace(BroadBandTestConstants.DOUBLE_QUOTE,
			BroadBandTestConstants.EMPTY_STRING);
		snmpOutput = BroadBandSnmpUtils.retrieveSnmpSetOutputWithGivenIndexOnRdkDevices(device, tapEnv,
			BroadBandSnmpMib.ECM_PRIVATE_WIFI_SSID_5.getOid(), SnmpDataType.STRING, paramValue,
			BroadBandSnmpMib.ECM_PRIVATE_WIFI_SSID_5.getTableIndex());
		is5GhzSsidSet = CommonMethods.isNotNull(snmpOutput) && snmpOutput.equals(paramValue);
	    }
	    LOGGER.info("Successfully set the 5 GHz private ssid: " + is5GhzSsidSet);

	    // Setting the 2.4GHz Private SSID Password.
	    paramValue = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_2_4_PRIVATE_SSID_PWD);
	    if (CommonMethods.isNotNull(paramValue)) {
		paramValue = paramValue.replace(BroadBandTestConstants.DOUBLE_QUOTE,
			BroadBandTestConstants.EMPTY_STRING);
		snmpOutput = BroadBandSnmpUtils.retrieveSnmpSetOutputWithGivenIndexOnRdkDevices(device, tapEnv,
			BroadBandSnmpMib.ECM_PRIVATE_WIFI_2_4_PASSPHRASE.getOid(), SnmpDataType.STRING, paramValue,
			BroadBandSnmpMib.ECM_PRIVATE_WIFI_2_4_PASSPHRASE.getTableIndex());
		is2GhzPwdSet = CommonMethods.isNotNull(snmpOutput) && snmpOutput.equals(paramValue);
	    }
	    LOGGER.info("Successfully set the 2.4 GHz password: " + is2GhzPwdSet);

	    // Setting the 5GHz Private SSID Password.
	    paramValue = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_5_PRIVATE_SSID_PWD);
	    if (CommonMethods.isNotNull(paramValue)) {
		paramValue = paramValue.replace(BroadBandTestConstants.DOUBLE_QUOTE,
			BroadBandTestConstants.EMPTY_STRING);
		snmpOutput = BroadBandSnmpUtils.retrieveSnmpSetOutputWithGivenIndexOnRdkDevices(device, tapEnv,
			BroadBandSnmpMib.ECM_PRIVATE_WIFI_5_PASSPHRASE.getOid(), SnmpDataType.STRING, paramValue,
			BroadBandSnmpMib.ECM_PRIVATE_WIFI_5_PASSPHRASE.getTableIndex());
		is5GhzPwdSet = CommonMethods.isNotNull(snmpOutput) && snmpOutput.equals(paramValue);
	    }
	    LOGGER.info("Successfully set the 5 GHz password: " + is5GhzPwdSet);
	    BroadBandSnmpUtils.executeSnmpSetWithTableIndexOnRdkDevices(tapEnv, device,
		    BroadBandSnmpMib.WIFI_APPLY_SETTINGS.getOid(), SnmpDataType.INTEGER,
		    BroadBandTestConstants.STRING_VALUE_ONE, BroadBandTestConstants.STRING_ZERO);
	    LOGGER.info("Waiting for 90 seconds after applying all WiFi settings.");
	    tapEnv.waitTill(BroadBandTestConstants.NINETY_SECOND_IN_MILLIS);
	    result = is2GhzSsidSet && is5GhzSsidSet && is2GhzPwdSet && is5GhzPwdSet;

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in setAndVerifyTheCustomSsidAndPassword():" + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : setAndVerifyTheCustomSsidAndPassword()");
	return result;
    }

}

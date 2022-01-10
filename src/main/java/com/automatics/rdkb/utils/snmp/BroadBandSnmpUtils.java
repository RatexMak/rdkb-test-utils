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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.SnmpConstants;
import com.automatics.device.Dut;
import com.automatics.enums.ExecutionStatus;
import com.automatics.exceptions.TestException;
import com.automatics.providers.snmp.SnmpProvider;
import com.automatics.providers.snmp.SnmpProviderFactory;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandSnmpConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.CommonUtils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.snmp.SnmpCommand;
import com.automatics.snmp.SnmpDataType;
import com.automatics.snmp.SnmpParams;
import com.automatics.snmp.SnmpProtocol;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.BeanUtils;
import com.automatics.utils.CommonMethods;

/**
 * Utility class for Broad Band specific SNMP utility. Common SNMP related
 * Utilities are in {@link SnmpUtils} in rdkv-utils, please refer before
 * defining any new methods to avoid duplication.
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
	 * Utility method to validate SNMP process is initialized in
	 * /rdklogs/logs/Snmplog.txt.0
	 * 
	 * @param tapEnv The {@link AutomaticsTapApi} instance. @param device The
	 *               {@link Dut} instance. @return status. True- SNMP process
	 *               initialized.Else-False
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
	 * @param AutomaticsTapApi The {@link AutomaticsTapApi} instance.
	 * @param device           The device to be queried.
	 * @param mibOrOid         The MIB or OID need to be queried.
	 * @param tableIndex       The table index.
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
		snmpParam.setCommandOption("t 30 ");
		snmpParam.setCommandOption("-OQ -v 2c -c");

		LOGGER.info("snmpParam  is : " + snmpParam);
		result = tapEnv.executeSnmpCommand(device, snmpParam, tableIndex);
		LOGGER.info("snmpGetOnEcm OutPut is : " + result);
		for (int retryCount = 0; getRetries(result, snmpProtocol, retryCount); ++retryCount) {
			LOGGER.info("Retrying snmp .Retry count :" + retryCount);
			snmpParam = new SnmpParams();
			snmpParam.setSnmpCommand(SnmpCommand.GET);
			snmpParam.setSnmpCommunity("CUSTOM");
			snmpParam.setMibOid(mibOrOid.trim());
			snmpParam.setCommandOption("t 30 ");
			snmpParam.setCommandOption("-OQ -v 2c -c");

			result = tapEnv.executeSnmpCommand(device, snmpParam, tableIndex);
		}
		return result;
	}

	/**
	 * Utility method to execute SNMP get with table index on ECM side.
	 * 
	 * @param AutomaticsTapApi The {@link AutomaticsTapApi} instance.
	 * @param device           The device to be queried.
	 * @param mibOrOid         The MIB or OID need to be queried.
	 * @param tableIndex       The table index.
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
		snmpParam.setCommandOption("t 30 ");
		snmpParam.setCommandOption("-OQ -v 2c -c");

		LOGGER.info("snmpParam  is : " + snmpParam);
		result = tapEnv.executeSnmpCommand(device, snmpParam);
		LOGGER.info("snmpGetOnEcm OutPut is : " + result);
		for (int retryCount = 0; getRetries(result, snmpProtocol, retryCount); ++retryCount) {
			LOGGER.info("Retrying snmp .Retry count :" + retryCount);
			snmpParam = new SnmpParams();
			snmpParam.setSnmpCommand(SnmpCommand.GET);
			snmpParam.setSnmpCommunity("CUSTOM");
			snmpParam.setMibOid(mibOrOid.trim());
			snmpParam.setCommandOption("t 30 ");
			snmpParam.setCommandOption("-OQ -v 2c -c");

			result = tapEnv.executeSnmpCommand(device, snmpParam);
		}
		return result;
	}

	/**
	 * Utility method to execute SNMP get with table index on ECM side.
	 * 
	 * @param AutomaticsTapApi The {@link AutomaticsTapApi} instance.
	 * @param device           The device to be queried.
	 * @param mibOrOid         The MIB or OID need to be queried.
	 * @param tableIndex       The table index.
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
		snmpParam.setCommandOption("t 30 ");
		snmpParam.setCommandOption("-OQ -v 2c -c");
		snmpParam.setDataType(datatype);
		snmpParam.setValue(value);

		result = tapEnv.executeSnmpCommand(device, snmpParam);

		for (int retryCount = 0; getRetries(result, snmpProtocol, retryCount); ++retryCount) {
			LOGGER.info("Retrying snmp .Retry count :" + retryCount);
			snmpParam = new SnmpParams();
			snmpParam.setSnmpCommand(SnmpCommand.SET);
			snmpParam.setSnmpCommunity("CUSTOM");
			snmpParam.setMibOid(mibOrOid.trim());
			snmpParam.setCommandOption("t 30 ");
			snmpParam.setCommandOption("-OQ -v 2c -c");
			snmpParam.setDataType(datatype);
			snmpParam.setValue(value);

			result = tapEnv.executeSnmpCommand(device, snmpParam);
		}
		return result;
	}
	/**
	 * Utility method to execute SNMP get with table index on ECM side.
	 * 
	 * @param AutomaticsTapApi The {@link AutomaticsTapApi} instance.
	 * @param device           The device to be queried.
	 * @param mibOrOid         The MIB or OID need to be queried.
	 * @param tableIndex       The table index.
	 * @return Command output.
	 */
	public static String snmpSetOnEcm_V3(AutomaticsTapApi tapEnv, Dut device, String mibOrOid, SnmpDataType datatype,
			String value) {

		String result = null;
		SnmpParams snmpParam = null;
		String snmpProtocol = System.setProperty(SnmpConstants.SYSTEM_PARAM_SNMP_VERSION, SnmpProtocol.SNMP_V3.toString());
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
	 * both XB and XF devices.
	 * 
	 * @param tapEnv       The {@link AutomaticsTapApi} instance
	 * @param settop       The settop to be validated.
	 * @param oidOrMibName The MIB or OID name.
	 * 
	 * @param dataType     The type of data to be set
	 * @param value        The value of data to be set
	 * @param tableIndex   The table index to be appended.
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
			snmpOutput = BroadBandSnmpUtils.snmpSetOnEcm(tapEnv, device, mibOrOid, dataType, value);

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
	 * Utility method to execute SNMP GET command on RDKB devices both XB and XF
	 * devices.
	 * 
	 * @param tapEnv       The {@link AutomaticsTapApi} instance
	 * @param device       The device to be validated.
	 * @param oidOrMibName The MIB or OID name.
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
	 * Utility method to execute SNMP GET command on RDKB devices both XB and XF
	 * devices.
	 * 
	 * @param tapEnv       The {@link AutomaticsTapApi} instance
	 * @param device       The device to be validated.
	 * @param oidOrMibName The MIB or OID name.
	 * @return Provides the SNMP GET Command output.
	 */
	public static String executeSnmpGetOnRdkDevicesWithTableIndex(AutomaticsTapApi tapEnv, Dut device, String oidOrMibName ,String tableIndex) {
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
					snmpOutput = executeSnmpGetOnRdkDevicesWithTableIndex(tapEnv, device, BroadBandSnmpMib.ECM_SYS_DESCR.getOid(), BroadBandSnmpMib.ECM_SYS_DESCR.getTableIndex());
					LOGGER.info("snmpOutput is : " +snmpOutput);
				} catch (Exception exception) {
					// Log & Suppress the Exception.
					LOGGER.error("EXCEPTION OCCURRED WHILE DOING SNMP OPERATION: " + exception.getMessage());
				}
				String firmwareVersion = device.getFirmwareVersion();
				LOGGER.info("firmwareVersion is : " +firmwareVersion);
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
	 * @param macAddress The given MAC address.
	 * @return Formatted string.
	 */
	public static String formatMacAddressWithoutLeadingZeros(String macAddress) {
		return BroadBandCommonUtils.formatIpOrMacWithoutLeadingZeros(macAddress);
	}

	/**
	 * Utility method to get/format the expected DOCSIS software version from
	 * version.txt
	 * 
	 * @param tapApi The {@link AutomaticsTapApi} instance.
	 * @param device The device to be validated.
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
	 * @param device The device to be tested.
	 * @param tapApi The {@link AutomaticsTapApi} instance.
	 * @param isTr69 Flag to check the whether SNMP or TR-069 validation.
	 * @return Device firmware version.
	 */
	public static String getExpectedSoftwareVersionUsingDmcliCommand(Dut device, AutomaticsTapApi tapApi,
			boolean isTr69) {
		String expectedSoftwareVersion = device.getFirmwareVersion();
		LOGGER.info("Expected software version  = " + expectedSoftwareVersion);
		return expectedSoftwareVersion;
	}
    /**
     * Utility method to execute SNMP WALK command on RDKB devices both XB and XF devices.
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
    public static String snmpSetOnEcmForInvalidCommunityString(AutomaticsTapApi tapEnv, Dut device, String mibOrOid, SnmpDataType dataType, String value,  String tableIndex) {

	String result = null;
	SnmpParams snmpParam = new SnmpParams();
	String snmpProtocol = System.getProperty(SnmpConstants.SYSTEM_PARAM_SNMP_VERSION,
		SnmpProtocol.SNMP_V2.toString());
	SnmpProtocol snmpVersion = SnmpProtocol.SNMP_V2;
	if (SnmpProtocol.SNMP_V3.getProtocolVersion().equals(snmpProtocol)) {
	    snmpVersion = SnmpProtocol.SNMP_V3;
	}
	LOGGER.info("Current SNMP protocol: " + snmpProtocol);
	mibOrOid = mibOrOid + "." + tableIndex;
	LOGGER.info("mibOrOid to execute: " + mibOrOid);

	SnmpProviderFactory providerFactory = BeanUtils.getSnmpFactoryProvider();
	SnmpProvider snmpProviderImpl = providerFactory.getSnmpProvider(snmpVersion);
	if (null != snmpProviderImpl) {
	    snmpParam.setSnmpCommand(SnmpCommand.SET);
	    snmpParam.setSnmpCommunity("PRIVATE");
	    snmpParam.setSnmpVersion(snmpVersion);
	    snmpParam.setMibOid(mibOrOid);
	    snmpParam.setDataType(dataType);
	    snmpParam.setValue(value);
	    result = snmpProviderImpl.doSet(device, snmpParam);
	}
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
    public static String snmpGetOnEcmForInvalidCommunityString(AutomaticsTapApi tapEnv, Dut device, String mibOrOid,  String tableIndex) {

	String result = null;
	SnmpParams snmpParam = new SnmpParams();
	String snmpProtocol = System.getProperty(SnmpConstants.SYSTEM_PARAM_SNMP_VERSION,
		SnmpProtocol.SNMP_V2.toString());
	SnmpProtocol snmpVersion = SnmpProtocol.SNMP_V2;
	if (SnmpProtocol.SNMP_V3.getProtocolVersion().equals(snmpProtocol)) {
	    snmpVersion = SnmpProtocol.SNMP_V3;
	}
	LOGGER.info("Current SNMP protocol: " + snmpProtocol);
	mibOrOid = mibOrOid + "." + tableIndex;
	LOGGER.info("mibOrOid to execute: " + mibOrOid);

	SnmpProviderFactory providerFactory = BeanUtils.getSnmpFactoryProvider();
	SnmpProvider snmpProviderImpl = providerFactory.getSnmpProvider(snmpVersion);
	if (null != snmpProviderImpl) {
	    snmpParam.setSnmpCommand(SnmpCommand.GET);
	    snmpParam.setSnmpCommunity("PRIVATE");
	    snmpParam.setSnmpVersion(snmpVersion);
	    snmpParam.setMibOid(mibOrOid);
	    snmpParam.setCommandOption("t 30 ");
	    result = snmpProviderImpl.doGet(device, snmpParam);
	}
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
    public static String snmpGetOnEmta(AutomaticsTapApi tapEnv, Dut device, String mibOrOid,  String tableIndex,String mtaAddress) {

	String result = null;
	SnmpParams snmpParam = new SnmpParams();
	mibOrOid = mibOrOid + "." + tableIndex;
	LOGGER.info("mibOrOid to execute: " + mibOrOid);

	    snmpParam.setSnmpCommand(SnmpCommand.GET);
	    snmpParam.setIpAddress(mtaAddress);
	    snmpParam.setMibOid(mibOrOid);
	    snmpParam.setCommandOption("t 30 ");
	  result = tapEnv.executeSnmpCommand(device, snmpParam);
	return result;
	
    }
    
    /**
     * Utility method to execute SNMP set with table index on ECM side.
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
	    String value,String tableIndex, String mtaAddress) {

	String result = null;
	SnmpParams snmpParam = null;
	String snmpProtocol = System.getProperty(SnmpConstants.SYSTEM_PARAM_SNMP_VERSION,
		SnmpProtocol.SNMP_V2.toString());
	mibOrOid = mibOrOid + "." + tableIndex;
	LOGGER.info("mibOrOid to execute: " + mibOrOid);

	snmpParam = new SnmpParams();
	snmpParam.setSnmpCommand(SnmpCommand.SET);
	snmpParam.setMibOid(mibOrOid.trim());
	snmpParam.setCommandOption("t 30 ");
	snmpParam.setIpAddress(mtaAddress);
	snmpParam.setDataType(datatype);
	snmpParam.setValue(value);

	result = tapEnv.executeSnmpCommand(device, snmpParam);

	for (int retryCount = 0; getRetries(result, snmpProtocol, retryCount); ++retryCount) {
	    LOGGER.info("Retrying snmp .Retry count :" + retryCount);
	    snmpParam = new SnmpParams();
	    snmpParam.setSnmpCommand(SnmpCommand.SET);
	    snmpParam.setMibOid(mibOrOid.trim());
	    snmpParam.setCommandOption("t 30 ");
	    snmpParam.setIpAddress(mtaAddress);
	    snmpParam.setDataType(datatype);
	    snmpParam.setValue(value);

	    result = tapEnv.executeSnmpCommand(device, snmpParam);
	}
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
	    isCmRelated =  (mibOrOid.startsWith("1.3.6.1.4.1.4491") || mibOrOid.startsWith("1.3.6.1.2.1")
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
	
	 LOGGER.info("Snmp System Description output is : "+snmpSystemDescrOutput);

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
     *            The settop to be validated.
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
     *            The settop to be validated.
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
     *            The settop to be validated.
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
     *            The settop to be validated.
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
     *            The settop to be validated.
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
     * @param settop
     *            The settop to be validated.
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
     * @author dbada200
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
     * @param settop
     *            The settop to be validated
     * @param tapEnv
     *            EactsTapApi instance
     * @return true if OID is read-only
     * @author dbada200
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
     * @param settop
     *            The settop to be validated.
     * @param output
     *            input
     * @return status output
     * @Refactor Alan_Bivera
     */
    public static boolean validateCmEthernetOperSettingValueRetrievedFromSnmp(String output) {
	boolean status = false;
	try {
	    // Megabits per second = bps per second ÷ 1,000,000
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
     * @param settop
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
     * @param settop
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
    public static String snmpGetOnEmtaForInvalidCommunityString(AutomaticsTapApi tapEnv, Dut device, String mibOrOid,  String tableIndex, String mtaAddress) {

	String result = null;
	SnmpParams snmpParam = new SnmpParams();
	mibOrOid = mibOrOid + "." + tableIndex;
	LOGGER.info("mibOrOid to execute: " + mibOrOid);

	snmpParam.setSnmpCommand(SnmpCommand.GET);
	snmpParam.setSnmpCommunity(SnmpConstants.INVALID_COMMUNITY_STRING);
	snmpParam.setMibOid(mibOrOid);
	snmpParam.setIpAddress(mtaAddress);
	result = tapEnv.executeSnmpCommand(device, snmpParam);
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
    public static String snmpSetOnEmtaForInvalidCommunityString(AutomaticsTapApi tapEnv, Dut device, String mibOrOid, SnmpDataType dataType, String value,  String tableIndex ,String mtaAddress) {

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
    
}

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Dut;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.constants.SnmpConstants;
import com.automatics.snmp.SnmpCommand;
import com.automatics.snmp.SnmpDataType;
import com.automatics.snmp.SnmpParams;
import com.automatics.snmp.SnmpProtocol;
import com.automatics.rdkb.utils.CommonUtils;

/**
 * Utility class for Broad Band specific SNMP utility. Common SNMP related
 * Utilities are in {@link SnmpUtils} in rdkv-utils, please refer before
 * defining any new methods to avoid duplication.
 * 
 * @author Athira
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

			LOGGER.info("##VERIFYING SNMP IS UP##");
			pollDuration = BroadBandTestConstants.FIVE_MINUTES;
			startTime = System.currentTimeMillis();
			do {
				LOGGER.info("GOING TO WAIT FOR 30 SECONDS");
				tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
				try {
					snmpOutput = executeSnmpGetOnRdkDevices(tapEnv, device, BroadBandSnmpMib.ECM_SYS_DESCR.getOid());
				} catch (Exception exception) {
					// Log & Suppress the Exception.
					LOGGER.error("EXCEPTION OCCURRED WHILE DOING SNMP OPERATION: " + exception.getMessage());
				}
				String firmwareVersion = device.getFirmwareVersion();
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
	 * Utility method to execute SNMP GET command with table index on RDKB devices
	 * both XB and XF devices.
	 * 
	 * @param tapEnv       The {@link AutomaticsTapApi} instance
	 * @param device       The device to be validated.
	 * @param oidOrMibName The MIB or OID name.
	 * @param tableIndex   The table index to be appended.
	 * @return Provides the SNMP GET Command output.
	 */
	public static String executeSnmpGetWithTableIndexOnRdkDevices(AutomaticsTapApi tapEnv, Dut device,
			String oidOrMibName, String tableIndex) {
		String snmpCommandOutput = null;
		String snmpProtocol = System.getProperty(SnmpConstants.SYSTEM_PARAM_SNMP_VERSION,
				SnmpProtocol.SNMP_V2.toString());
		snmpCommandOutput = BroadBandSnmpUtils.snmpGetOnEcm(tapEnv, device, oidOrMibName, tableIndex);
		return snmpCommandOutput;
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
}

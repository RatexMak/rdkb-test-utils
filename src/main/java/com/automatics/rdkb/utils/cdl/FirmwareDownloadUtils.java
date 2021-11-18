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
package com.automatics.rdkb.utils.cdl;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Dut;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.CommonMethods;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.snmp.SnmpDataType;
import com.automatics.rdkb.constants.BroadBandCdlConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpUtils;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpMib;



public class FirmwareDownloadUtils {
	/** SLF4J logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FirmwareDownloadUtils.class);
	
    /**
     * Keys to store rdk logs
     */
    private static final String KEY_FOR_CDL_DOWNLOAD_SUCCESS_LOG = "CDL_DOWNLOAD_SUCCESS_LOG";
    private static final String KEY_FOR_CDL_DOWNLOAD_STARTED_LOG = "CDL_DOWNLOAD_STARTED_LOG";
    private static final String KEY_FOR_CDL_DOWNLOAD_INITIATED_LOG = "CDL_DOWNLOAD_INITIATED_LOG";
    
    /**
     * SSH Connection error - Connection refused.
     */
    public static final String SSH_CONNECTION_ERROR_CONNECTION_REFUSED = "Connection refused";

	/**
	 * Method to retrieve current firmware file name for cdl
	 * 
	 * @param device instance of {@link Dut}
	 * @return current firmware file name for cdl
	 */
	public static String getCurrentFirmwareFileNameForCdl(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("STARTING METHOD: getCurrentFirmwareFileNameForCdl()");

		// file name retrieved for performing the cdl
		String firmwareFileName = null;

		// retrieve current firmware version using webpa command
		firmwareFileName = BroadBandCommonUtils.getCurrentlyRunningImageVersionUsingWebPaCommand(tapEnv, device);
		LOGGER.info("firmwareFileName is "+firmwareFileName+" after the method getCurrentlyRunningImageVersionUsingWebPaCommand");
		// retry from version.txt file
		if (CommonMethods.isNull(firmwareFileName)) {
			firmwareFileName = CodeDownloadUtils.getCurrentRunningImageNameFromVersionTxtFile(device, tapEnv);
			LOGGER.info("firmwareFileName is "+firmwareFileName+" after the method getCurrentRunningImageNameFromVersionTxtFile");
		}
		LOGGER.debug("ENDING METHOD: getCurrentFirmwareFileNameForCdl()");
		return firmwareFileName;
	}
	
	/**
     * Method to get CDL Logs for validation
     * 
     * @param device
     *            instance of {@link Dut}
     * @return CDL Logs for validation
     */
    public static String getCdlLogsForValidation(AutomaticsTapApi tapEnv, Dut device) {

	// CDL Log for validation
	String cdlLogsForValidation = AutomaticsConstants.EMPTY_STRING;

	// retrieve all available cdl logs from the device
	HashMap<String, String> cdlLogs = getCdlStatusLogs(tapEnv, device);

	if (cdlLogs.containsKey(KEY_FOR_CDL_DOWNLOAD_SUCCESS_LOG)) {
	    cdlLogsForValidation = cdlLogs.get(KEY_FOR_CDL_DOWNLOAD_SUCCESS_LOG);
	} else if (cdlLogs.containsKey(KEY_FOR_CDL_DOWNLOAD_STARTED_LOG)) {
	    cdlLogsForValidation = cdlLogs.get(KEY_FOR_CDL_DOWNLOAD_STARTED_LOG);
	} else if (cdlLogs.containsKey(KEY_FOR_CDL_DOWNLOAD_INITIATED_LOG)) {
	    cdlLogsForValidation = cdlLogs.get(KEY_FOR_CDL_DOWNLOAD_INITIATED_LOG);
	}
	return cdlLogsForValidation;
    }
    
    /**
     * Method to wait for given message in RDKB Logs
     * 
     * @param device
     *            instance of {@link Dut}
     * @return CDL logs
     */
    private static HashMap<String, String> getCdlStatusLogs(AutomaticsTapApi tapEnv, Dut device) {

	LOGGER.debug("STARTING METHOD: getCdlLogsForValidation()");
	// maximum loop count
	int maxLoopCount = 35;
	// CDL response
	String response = null;
	// command to retrieve CDL logs
	String command = "cat " + BroadBandTestConstants.RDKLOGS_LOGS_XCONF_TXT_0;

	// CDL LOGS FOR VALIDATION

	// Log for cdl initialization
	String cdlInitializationLog = FirmwareDownloadUtils.getCdlInitializationLog(device);
	// Log for firmware download started
	String cdlDownloadStartedLog = FirmwareDownloadUtils.getCdlDownloadStartedLog(device);
	// Log for cdl download success
	String cdlDownloadSuccess = FirmwareDownloadUtils.getCdlDownloadSuccessLog(device);

	// store rdk logs
	HashMap<String, String> cdlLogs = new HashMap<String, String>();

	for (int index = 1; index <= maxLoopCount; index++) {

	    LOGGER.info(index + "/" + maxLoopCount + "#waiting for the cdl logs ");

	    response = tapEnv.executeCommandInSettopBox(device, command);

	    if (CommonMethods.isNotNull(response)) {

		// validation for cdl initialization
		if (!cdlLogs.containsKey(KEY_FOR_CDL_DOWNLOAD_INITIATED_LOG)
			&& response.contains(cdlInitializationLog)) {
		    cdlLogs.put(KEY_FOR_CDL_DOWNLOAD_INITIATED_LOG, response);
		}

		// validation for cdl download started
		if (!cdlLogs.containsKey(KEY_FOR_CDL_DOWNLOAD_STARTED_LOG)
			&& response.contains(cdlDownloadStartedLog)) {
		    cdlLogs.put(KEY_FOR_CDL_DOWNLOAD_STARTED_LOG, response);
		}

		// validation for cdl download success
		if (!cdlLogs.containsKey(KEY_FOR_CDL_DOWNLOAD_SUCCESS_LOG) && response.contains(cdlDownloadSuccess)) {
		    cdlLogs.put(KEY_FOR_CDL_DOWNLOAD_SUCCESS_LOG, response);
		}

		/*
		 * handle failure scenarios
		 */

		// No upgrade required
		if (response.contains(BroadBandCdlConstants.XCONF_HTTP_DOWNLOAD_NOT_SUCCESSFUL)) {
		    LOGGER.info(
			    "Exiting CDL logs retrieval since retrieved 'XCONF SCRIPT : HTTP download NOT Successful'");
		    break;
		}

		// No upgrade required
		if (response.contains(BroadBandCdlConstants.XCONF_CDL_UPGRADE_OR_DOWNGRADE_NOT_REQUIRED)) {
		    LOGGER.info("Exiting CDL logs retrieval since retrieved 'No upgrade/downgrade required'");
		    break;
		}

		// handle for connection refused scenarios
		if (response.contains(SSH_CONNECTION_ERROR_CONNECTION_REFUSED) && (index > 15)) {
		    LOGGER.info("Exiting CDL logs retrieval since retrieved 'port 22: Connection refused'");
		    break;
		}
	    }

	    // exit cdl logs verification since STB not responding after code download started
	    if (CommonMethods.isNull(response) && cdlLogs.containsKey(KEY_FOR_CDL_DOWNLOAD_STARTED_LOG)
		    && (index > 20)) {
		LOGGER.info(
			"Exiting CDL logs retrieval since matched all the following loop exit scenarios 1) Retrieved server response as empty for the command '"
				+ command + "' 2) retrieved '" + cdlDownloadStartedLog
				+ "' from from RDK logs 3) loop count greater than 20");
		break;
	    }
	    // verify whether CDL success logs are present
	    if (cdlLogs.containsKey(KEY_FOR_CDL_DOWNLOAD_SUCCESS_LOG)) {
		break;
	    }
	}

	LOGGER.debug("ENDING METHOD: getCdlLogsForValidation()");

	return cdlLogs;
    }
    
    /**
 * Method to return cdl initialization log
 * 
 * @param device
 *            instance of {@link Dut}
 * @return cdl Initialization Log
 */
public static String getCdlInitializationLog(Dut device) {
// Log for cdl initialization
String cdlInitializationLog = "XCONF SCRIPT : Download image from HTTP server";
return cdlInitializationLog;
}
/**
* Method to return cdl download started logs
* 
* @param device
*            instance of {@link Dut}
* @return cdl Download Started Log
*/
public static String getCdlDownloadStartedLog(Dut device) {
// Log for firmware download started
String cdlDownloadStartedLog = "### httpdownload started ###";
return cdlDownloadStartedLog;
}

/**
 * Method to return cdl download success log
 * 
 * @param device
 *            instance of {@link Dut}
 * @return cdl Download Success log
 */
public static String getCdlDownloadSuccessLog(Dut device) {
// Log for firmware download started
String cdlDownloadSuccess = "XCONF SCRIPT : HTTP download Successful";
return cdlDownloadSuccess;
}

/**
 * Utility method to get the formatted TFTP Server IPv6 Address.
 */
public static String getFormattedTftpServerIpv6Address(String tftpServerAddress) {
String formattedServerAddress = "";

LOGGER.info("DAC 15 CDL SERVER - TFTP SERVER IPv6 ADDRESS = " + tftpServerAddress);

if (CommonMethods.isNotNull(tftpServerAddress)) {
    tftpServerAddress = tftpServerAddress.replaceAll(BroadBandTestConstants.DELIMITER_COLON, "");
    formattedServerAddress = tftpServerAddress.trim();
    formattedServerAddress = tftpServerAddress.replaceAll("(..)", "$1 ").trim();
}
LOGGER.info("DAC 15 CDL SERVER - FORMATTED TFTP SERVER IPv6 ADDRESS = " + formattedServerAddress);
return formattedServerAddress;
}

/**
 * Method to set server address for SNMP image download via HTTP and TFTP
 * both
 * 
 * @param tapEnv
 *            {@link AutomaticsTapApi}
 * @param device
 *            The set-top instance
 * @return true if Server address is set successfully
 */
public static boolean setServerAddressForSnmpCodeDownload(AutomaticsTapApi tapEnv, Dut device) {
	LOGGER.debug("STARTING METHOD: setServerAddressForSnmpCodeDownload");
	// Variable declaration starts
	boolean isServerSetSuccessful = false;
	String formattedAddress = "";
	String softwareServerAddress = "";
	String snmpSetResponse = "";
	String snmpGetResonse = "";
	String errorMessage = "";
	// Variable declaration ends

	try {
		
		/*String tftpServerAddress  = tapEnv
				.getCatsPropsValue(BroadBandTestConstants.PROP_KEY_HTTP_SERVER_IPV6_ADDRESS);*/
		String tftpServerAddress = AutomaticsPropertyUtility.getProperty(BroadBandTestConstants.PROP_KEY_HTTP_SERVER_IPV6_ADDRESS);
		LOGGER.info("tftpServerAddress is  : " + tftpServerAddress);
		softwareServerAddress = getFormattedTftpServerIpv6Address(tftpServerAddress);
		LOGGER.info("Formatted TFTP Server IPv6 Address for SNMP MIB Cross validation  : " + softwareServerAddress);
		if (CommonMethods.isNotNull(softwareServerAddress)) {
			formattedAddress = new StringBuffer().append(BroadBandTestConstants.DOUBLE_QUOTE)
					.append(softwareServerAddress.toUpperCase()).append(BroadBandTestConstants.DOUBLE_QUOTE)
					.toString();
			LOGGER.info("FORMATTED TFTP SERVER IPV6 ADDRESS IN HEX FORMAT : " + formattedAddress);
			LOGGER.info("BroadBandSnmpMib.ECM_DOCS_DEV_SW_SERVER_ADDRESS.getOid() : " + BroadBandSnmpMib.ECM_DOCS_DEV_SW_SERVER_ADDRESS.getOid());
			snmpSetResponse = BroadBandSnmpUtils.snmpSetOnEcm(tapEnv, device,
					BroadBandSnmpMib.ECM_DOCS_DEV_SW_SERVER_ADDRESS.getOid(), SnmpDataType.HEXADECIMAL,
					formattedAddress);
			LOGGER.info("SNMPSET RESPONSE FOR docsDevSwServerAddress: " + snmpSetResponse);
			if (CommonMethods.patternMatcher(snmpSetResponse.toLowerCase(), softwareServerAddress.toLowerCase())) {
				LOGGER.info("BroadBandSnmpMib.ECM_DOCS_DEV_SW_SERVER_ADDRESS.getOid() : " +BroadBandSnmpMib.ECM_DOCS_DEV_SW_SERVER_ADDRESS.getOid());
				snmpGetResonse = BroadBandSnmpUtils.snmpGetOnEcm(tapEnv, device,
						BroadBandSnmpMib.ECM_DOCS_DEV_SW_SERVER_ADDRESS.getOid());
				LOGGER.info("SNMPGET RESPONSE FOR docsDevSwServerAddress: " + snmpGetResonse);
				isServerSetSuccessful = CommonMethods.patternMatcher(snmpGetResonse.toLowerCase(),
						softwareServerAddress.toLowerCase());
			}

		}
	} catch (Exception exception) {
		errorMessage = exception.getMessage();
		LOGGER.error(errorMessage);
	}
	LOGGER.debug("ENDING METHOD: setServerAddressForSnmpCodeDownload");
	return isServerSetSuccessful;
}

}

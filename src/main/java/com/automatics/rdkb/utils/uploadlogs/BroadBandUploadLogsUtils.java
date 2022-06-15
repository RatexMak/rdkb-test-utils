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

package com.automatics.rdkb.utils.uploadlogs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Device;
import com.automatics.device.Dut;
import com.automatics.rdkb.constants.BroadBandSnmpConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.utils.LogUploadUtils;
import com.automatics.rdkb.utils.BroadBandSystemUtils;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpMib;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpUtils;
import com.automatics.rdkb.utils.CommonUtils;

/**
 * Utility class for DeviceManagementUploadLogsTest
 * 
 * @author Praveen_chandru,Joseph_Maduram.
 * 
 * @refactor yamini.s
 *
 */

public class BroadBandUploadLogsUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandUploadLogsUtils.class);
	
	/**
     * Utility method to verify the Upload status Get parameter value
     * 
     * @param device
     *            {@link Dut}
     * @param uploadstatus
     *            uploadstatus get parameter value
     * 
     * @return true if the upload status output is valid
     * 
     * @refactor yamini.s
     */
    public static boolean verifyGetParamUploadStatus(Dut device, String uploadStatus) {
	LOGGER.debug("ENTERING METHOD verifyGetParamUploadStatus");
	// stores the test status
	boolean status = false;

	if (!uploadStatus.equalsIgnoreCase(BroadBandTestConstants.STATUS_NOT_TRIGGERED)) {
	    uploadStatus = CommonMethods.patternFinder(uploadStatus,
		    BroadBandTestConstants.PATTERN_MATCHER_UPLOAD_STATUS);
	    LOGGER.info("logStatus is" + " " + uploadStatus);
	}

	if (CommonMethods.isNotNull(uploadStatus)) {
	    status = (uploadStatus.equalsIgnoreCase(BroadBandTestConstants.STATUS_NOT_TRIGGERED))
		    || (uploadStatus.equalsIgnoreCase(BroadBandTestConstants.STATUS_FAILED))
		    || (uploadStatus.equalsIgnoreCase(BroadBandTestConstants.STATUS_COMPLETE));

	}
	LOGGER.info("GET PARAM UPLOAD STATUS RESULT: " + status);
	LOGGER.debug("ENDING METHOD verifyGetParamUploadStatus");
	return status;
    }
    
    /**
     * Utility method to verify the Log Upload Status using WebPA; It is polling for 3 minute within an interval of 1
     * seconds to check the Upload Log Status
     * 
     * @param device
     *            {@link Dut}
     * @param expectedStatus
     *            Log Upload status to be checked
     * 
     * @return true if expected status matches with the current status retrieved via WebPA
     * 
     * @refactor yamini.s
     */

    public static boolean verifyLogUploadStatusUsingWebPA(Dut device, AutomaticsTapApi tapApi, String expectedStatus) {
	LOGGER.debug("ENTERING METHOD verifyLogUploadStatusUsingWebPA");
	// stores the logupload status
	String logUploadStatus = null;
	// stores the test status
	boolean status = false;
	String logStatus = null;
	long pollDuration = BroadBandTestConstants.SIX_MINUTE_IN_MILLIS;
	long startTime = System.currentTimeMillis();
	do {
	    logUploadStatus = tapApi.executeWebPaCommand(device,
		    BroadBandWebPaConstants.DEVICE_DEVICE_INFO_RDK_LOG_UPLOAD_STATUS);
	    if (logUploadStatus.trim().equalsIgnoreCase(BroadBandTestConstants.STATUS_NOT_TRIGGERED)) {
		logStatus = logUploadStatus.trim();
	    } else {
		logStatus = CommonMethods.patternFinder(logUploadStatus,
			BroadBandTestConstants.PATTERN_MATCHER_UPLOAD_STATUS);
		logStatus = CommonMethods.isNotNull(logStatus) ? logStatus.trim() : null;
		LOGGER.info("Upload logStatus is" + " " + logStatus);
	    }
	    status = CommonMethods.isNotNull(logStatus) && logStatus.equalsIgnoreCase(expectedStatus)
		    && logUploadStatus.contains(BroadBandTestConstants.TIMEZONE_UTC);
	    if (status) {
		break;
	    } else if (expectedStatus.equalsIgnoreCase(BroadBandTestConstants.STATUS_TRIGGERED)
		    && logStatus.equalsIgnoreCase(BroadBandTestConstants.STATUS_INPROGRESS)) {
		status = true;
		break;
	    } else if (expectedStatus.equalsIgnoreCase(BroadBandTestConstants.STATUS_INPROGRESS)
		    && logStatus.equalsIgnoreCase(BroadBandTestConstants.STATUS_FAILED)) {
		status = true;
		break;
	    }

	} while ((System.currentTimeMillis() - startTime) < pollDuration);
	LOGGER.info("UPLOAD STATUS CHECK USING WEBPA RESULT: " + status);
	LOGGER.debug("ENDING METHOD verifyLogUploadStatusUsingWebPA");
	return status;

    }
    
    /**
     * 
     * Utility method to check and compare the different Log upload status
     * 
     * @param uploadStatusBeforeLogUpload
     *            upload status before triggering the log Upload
     * @param uploadStatusAfterLogUpload
     *            upload status after triggering the log Upload
     * 
     * @return true if the status moves to Complete State. If previous state also is returned as complete, compare the
     *         time of uploadstatus and return true if the latter is greater
     *         
     * @refactor yamini.s
     * 
     */

    public static boolean uploadStatusCheck(String statusBeforeLogUpload, String statusAfterLogUpload) {
	LOGGER.debug("ENTERING METHOD uploadStatusCheck");
	// stores the test status

	boolean status = false;
	String statusBeforeLogUploadAfterSplittingTime = null;
	String statusAfterLogUploadAfterSplittingTime = null;

	statusBeforeLogUploadAfterSplittingTime = CommonMethods.patternFinder(statusBeforeLogUpload,
		BroadBandTestConstants.PATTERN_MATCHER_UPLOAD_STATUS);
	statusBeforeLogUploadAfterSplittingTime = CommonMethods.isNotNull(statusBeforeLogUploadAfterSplittingTime)
		? statusBeforeLogUploadAfterSplittingTime.trim() : null;
	LOGGER.info("statusBeforeLogUploadAfterSplittingTime is" + statusBeforeLogUploadAfterSplittingTime);

	statusAfterLogUploadAfterSplittingTime = CommonMethods.patternFinder(statusAfterLogUpload,
		BroadBandTestConstants.PATTERN_MATCHER_UPLOAD_STATUS);
	statusAfterLogUploadAfterSplittingTime = CommonMethods.isNotNull(statusAfterLogUploadAfterSplittingTime)
		? statusAfterLogUploadAfterSplittingTime.trim() : null;
	LOGGER.info("statusAfterLogUploadAfterSplittingTime is" + statusAfterLogUploadAfterSplittingTime);

	if (statusBeforeLogUpload.equalsIgnoreCase(BroadBandTestConstants.STATUS_NOT_TRIGGERED)
		&& (statusAfterLogUploadAfterSplittingTime.equalsIgnoreCase(BroadBandTestConstants.STATUS_COMPLETE))) {
	    status = true;
	} else if (statusBeforeLogUploadAfterSplittingTime.equalsIgnoreCase(BroadBandTestConstants.STATUS_FAILED)
		&& (statusAfterLogUploadAfterSplittingTime.equalsIgnoreCase(BroadBandTestConstants.STATUS_COMPLETE))) {
	    status = true;

	} else if (statusBeforeLogUploadAfterSplittingTime.equalsIgnoreCase(BroadBandTestConstants.STATUS_COMPLETE)
		&& (statusAfterLogUploadAfterSplittingTime.equalsIgnoreCase(BroadBandTestConstants.STATUS_COMPLETE))) {
	    LOGGER.info("Going to compare the UTC TimeZone since the Uploadstatus are same");
	    boolean timeStatus = timeComparisonTelemetryLogInstances(statusBeforeLogUpload, statusAfterLogUpload);
	    status = timeStatus;
	}
	LOGGER.info("UPLOAD STATUS CHECK RESULT: " + status);
	LOGGER.debug("ENDING METHOD uploadStatusCheck");
	return status;

    }
    
    /**
     * Utility method to covert the UTC time from string to date format and then compare it in time format(milliseconds)
     * 
     * @param uploadStatusBeforeLogUpload
     *            upload status before triggering the log Upload
     * @param uploadStatusAfterLogUpload
     *            upload status after triggering the log Upload
     * 
     * @return true if the latter time in milliseconds is greater
     * 
     * @refactor yamini.s
     */

    public static boolean timeComparisonTelemetryLogInstances(String statusBeforeLogUpload,
	    String statusAfterLogUpload) {
	LOGGER.debug("ENTERING METHOD timeComparisonForUTCTimeZoneFormat");
	// stores the test status
	boolean status = false;
	// String to store the error message
	String errorMessage = null;
	try {
	    statusBeforeLogUpload = CommonMethods.patternFinder(statusBeforeLogUpload,
		    BroadBandTestConstants.PATTERN_MATCHER_TO_GET_UPLOAD_LOG_TIME);
	    LOGGER.info("The Previous UTC Upload time:" + statusBeforeLogUpload);
	    statusAfterLogUpload = CommonMethods.patternFinder(statusAfterLogUpload,
		    BroadBandTestConstants.PATTERN_MATCHER_TO_GET_UPLOAD_LOG_TIME);
	    LOGGER.info("The Final UTC Upload time:" + statusAfterLogUpload);
	    SimpleDateFormat formatter = new SimpleDateFormat(BroadBandTestConstants.FORMAT_TIMEZONE_UTC);
	    Date dateOfUploadStatusBeforeLogUpload = formatter.parse(statusBeforeLogUpload);
	    Date dateOfUploadStatusAfterLogUpload = formatter.parse(statusAfterLogUpload);
	    status = ((dateOfUploadStatusAfterLogUpload.getTime()) > (dateOfUploadStatusBeforeLogUpload.getTime()));
	    errorMessage = "LogUpload time retreived after triggering the upload is not greater than previous Logupload time";
	} catch (Exception exception) {
	    errorMessage = "Exception occured while comparing the logupload Time " + exception.getMessage();
	    LOGGER.error(errorMessage);
	}
	LOGGER.info("TIME COMPARISON FOR UTC TIMEZONE RESULT: " + status);
	LOGGER.debug("ENDING METHOD timeComparisonTelemetryLogInstances");
	return status;
    }
    
    /**
     * Utility method to grep whether the Upload Log file name is present in logs
     * 
     * @param device
     *            {@link Dut}
     * 
     * @return name of the Log File to be Uploaded
     * 
     * @refactor yamini.s
     */
    public static boolean verifyLogUploadMessageInDevice(Dut device, AutomaticsTapApi tapApi, String deviceDateTime) {
	LOGGER.debug("ENTERING METHOD verifyLogUploadMessageInDevice");
	boolean status = false;
	if (CommonMethods.isAtomSyncAvailable(device, tapApi)) {

	    status = BroadBandSystemUtils.verifyArmConsoleLogForPollingTime(tapApi, device,
		    BroadBandTraceConstants.LOGS_UPLOADED_SUCCESSFULLY,
		    BroadBandTestConstants.RDKLOGS_LOGS_ARM_CONSOLE_0, deviceDateTime, BroadBandTestConstants.THREE_MINUTES);

	} else {
	    status = BroadBandSystemUtils.verifyArmConsoleLogForPollingTime(tapApi, device,
		    BroadBandTraceConstants.LOGS_UPLOADED_SUCCESSFULLY,
		    BroadBandTestConstants.RDKLOGS_LOGS_CONSOLE_TXT_0, deviceDateTime, BroadBandTestConstants.THREE_MINUTES);

	}
	LOGGER.debug("ENDING METHOD verifyLogUploadMessageInDevice");
	return status;
    }
    
    /**
     * Utility method to grep the Upload Log file name from ArmconsoleLog.txt
     * 
     * @param device
     *            {@link Dut}
     * 
     * @return name of the Log File to be Uploaded
     * 
     * @refactor yamini.s
     */
    public static String retrieveLogFileName(Dut device, AutomaticsTapApi tapApi) {
	LOGGER.debug("ENTERING METHOD retrieveLogFileName");
	String retrieveLogFileName = null;
	if (CommonMethods.isAtomSyncAvailable(device, tapApi)) {
	    retrieveLogFileName = BroadBandTestConstants.COMMAND_TO_RETRIEVE_LOG_FILE_NAME_ARM_CONSOLE;
	} else {
	    retrieveLogFileName = BroadBandTestConstants.COMMAND_TO_RETRIEVE_LOG_FILE_NAME_CONSOLE_LOG;
	}

	String logUploadRetrieveFileName = tapApi.executeCommandUsingSsh(device, retrieveLogFileName);
	String uploadedLogFileName = CommonMethods.isNotNull(logUploadRetrieveFileName) ? CommonMethods
		.patternFinder(logUploadRetrieveFileName, BroadBandTestConstants.PATTERN_MATCHER_LOG_FILENAME).trim()
		: null;
	LOGGER.debug("ENDING METHOD retrieveLogFileName");
	return uploadedLogFileName;
    }
    
    /**
     * Utility method to add DNS names to /etc/hosts file
     * 
     * @param device
     *            {@link Dut}
     * 
     * @return true if the DNS names are added in hosts file
     * 
     * @refactor yamini.s
     */

    public static boolean addDnsNamesToHostsFile(Dut device, AutomaticsTapApi tapApi) {
	LOGGER.debug("ENTERING METHOD addDnsNamesToHostsFile");
	// stores the test status
	boolean status = false;
	String hostsFileLogsForIpv4 = null;
	String hostsFileLogsForIpv6 = null;
	tapApi.executeCommandUsingSsh(device, BroadBandTestConstants.COMMAND_TO_ADD_DNS_NAMES);
	String hostsFileLogs = tapApi.executeCommandUsingSsh(device, BroadBandTestConstants.COMMAND_TO_GREP_DNS_NAMES);
	LOGGER.info("hostsFileLogs are" + hostsFileLogs);

	hostsFileLogsForIpv4 = CommonMethods.patternFinder(hostsFileLogs,
		BroadBandTestConstants.PATTERN_MATCHER_DNS_NAME_FOR_IPV4_LOOPBACK);
	hostsFileLogsForIpv6 = CommonMethods.patternFinder(hostsFileLogs,
		BroadBandTestConstants.PATTERN_MATCHER_DNS_NAME_FOR_IPV6_LOOPBACK);
	LOGGER.info("hostsFileLogsForIpv4 is" + hostsFileLogsForIpv4);
	LOGGER.info("hostsFileLogsForIpv6 is" + hostsFileLogsForIpv6);

	if (CommonMethods.isNotNull(hostsFileLogsForIpv4) && CommonMethods.isNotNull(hostsFileLogsForIpv6)) {
	    status = (hostsFileLogsForIpv6.equalsIgnoreCase(BroadBandTestConstants.DNS_NAME_FOR_IPV6_LOOPBACK))
		    && (hostsFileLogsForIpv4.equalsIgnoreCase(BroadBandTestConstants.DNS_NAME_FOR_IPV4_LOOPBACK));
	}

	LOGGER.info("ADD DNS NAMES TO HOSTS FILE RESULT: " + status);
	LOGGER.debug("ENDING METHOD addDnsNamesToHostsFile");
	return status;
    }

    /**
     * Utility method to remove DNS names from /etc/hosts file
     * 
     * @param device
     *            {@link Dut}
     * 
     * @return true if the DNS names are removed from hosts file
     * 
     * @refactor yamini.s
     */

    public static boolean removeDnsNamesFromHostsFile(Dut device, AutomaticsTapApi tapApi) {
	LOGGER.debug("ENTERING METHOD removeDnsNamesFromHostsFile");
	// stores the test status
	boolean status = false;
	tapApi.executeCommandUsingSsh(device, BroadBandTestConstants.COMMAND_TO_REMOVE_DNS_NAMES);
	tapApi.executeCommandUsingSsh(device, BroadBandTestConstants.COPY_TMP_TO_HOSTS_FILE);
	String hostsFileLogs = tapApi.executeCommandUsingSsh(device, BroadBandTestConstants.COMMAND_TO_GREP_DNS_NAMES);
	status = (!hostsFileLogs.contains(BroadBandTestConstants.DNS_NAME_FOR_IPV6_LOOPBACK))
		&& (!hostsFileLogs.contains(BroadBandTestConstants.DNS_NAME_FOR_IPV4_LOOPBACK));
	LOGGER.info("REMOVE DNS NAMES TO HOSTS FILE RESULT: " + status);
	LOGGER.debug("ENDING METHOD removeDnsNamesFromHostsFile");
	return status;
    }
    
    /**
     * Utility method to verify the Log Upload Status using WebPA; It is polling for 3 minute within an interval of 1
     * seconds to check the Upload Log Status
     * 
     * @param device
     *            {@link Dut}
     * @param expectedStatus
     *            Log Upload status to be checked
     * 
     * @return true if expected status matches with the current status retrieved via WebPA
     * 
     * @refactor yamini.s
     */

    public static boolean verifyLogUploadInProgressStatusUsingWebPA(Dut device, AutomaticsTapApi tapApi,
	    String expectedStatus) {
	LOGGER.debug("ENTERING METHOD verifyLogUploadInProgressStatusUsingWebPA");
	// stores the logupload status
	String logUploadStatus = null;
	// stores the test status
	boolean status = false;
	String logStatus = null;
	long pollDuration = BroadBandTestConstants.THREE_MINUTES;
	long startTime = System.currentTimeMillis();
	do {
	    logUploadStatus = tapApi.executeWebPaCommand(device,
		    BroadBandWebPaConstants.DEVICE_DEVICE_INFO_RDK_LOG_UPLOAD_STATUS);
	    if (logUploadStatus.trim().equalsIgnoreCase(BroadBandTestConstants.STATUS_NOT_TRIGGERED)) {
		logStatus = logUploadStatus.trim();
	    } else {
		logStatus = CommonMethods.patternFinder(logUploadStatus,
			BroadBandTestConstants.PATTERN_MATCHER_UPLOAD_STATUS);
		logStatus = CommonMethods.isNotNull(logStatus) ? logStatus.trim() : null;
		LOGGER.info("Upload logStatus is" + " " + logStatus);
	    }
	    status = CommonMethods.isNotNull(logStatus) && logStatus.equalsIgnoreCase(expectedStatus)
		    && logUploadStatus.contains(BroadBandTestConstants.TIMEZONE_UTC);
	    if (status) {
		break;
	    } else if (expectedStatus.equalsIgnoreCase(BroadBandTestConstants.STATUS_INPROGRESS)
		    && logStatus.equalsIgnoreCase(BroadBandTestConstants.STATUS_COMPLETE)) {
		status = true;
		break;
	    }

	} while ((System.currentTimeMillis() - startTime) < pollDuration);
	LOGGER.info("UPLOAD STATUS CHECK USING WEBPA RESULT: " + status);
	LOGGER.debug("ENDING METHOD verifyLogUploadInProgressStatusUsingWebPA");
	return status;

    }
    
    /**
     * Utility method to get all the Log Upload Statuses after triggering logupload using SNMP from Triggered >> In
     * Progress state >> Completed/Failed State
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param maxPollDuration
     *            Maximum Polling duration to check log upload status
     * 
     * @return logStatusList List of unique log upload statuses obtained for a duration of "maxPollDuration"
     * 
     * @refactor yamini.s
     */
    public static ArrayList<String> getLogUploadStatusUsingSnmp(Dut device, AutomaticsTapApi tapEnv,
	    long maxPollDuration) {
	LOGGER.debug("ENTERING METHOD : getLogUploadStatusUsingSnmp");
	HashSet<String> logStatusSet = new HashSet<>();
	ArrayList<String> logStatusList = new ArrayList<>();
	// Boolean variable denoting if the log upload status went to 'Triggered'
	boolean startMonitoring = false;
	// String to store the smnp response
	String snmpResponse = null;
	long startTime = System.currentTimeMillis();
	try {
	    // Looping to get the log upload status via snmp command for 'maxPollDuration'
	    do {
		snmpResponse = BroadBandSnmpUtils.executeSnmpGetOnRdkDevices(tapEnv, device,
			BroadBandSnmpMib.ECM_UPLOAD_STATUS.getOid());
		LOGGER.info("OBTAINED LOG UPLOAD RESPONSE VIA SNMP IS : " + snmpResponse);

		if (CommonMethods.isNotNull(snmpResponse) && !startMonitoring
			&& (snmpResponse.contains(BroadBandTestConstants.STATUS_TRIGGERED)
				|| snmpResponse.contains(BroadBandTestConstants.STATUS_INPROGRESS))) {

		    startMonitoring = true;
		    logStatusSet.add(snmpResponse);

		} else if (CommonMethods.isNotNull(snmpResponse)
			&& snmpResponse.contains(BroadBandTestConstants.STATUS_COMPLETE)) {
		    logStatusSet.add(snmpResponse);
		    break;
		}

	    } while ((System.currentTimeMillis() - startTime) < maxPollDuration);

	    
	    if (!logStatusSet.isEmpty()) {
		LOGGER.info("FOLLOWING ARE THE LOG UPLOAD STATUSES OBTAINED :");
		String logUploadStatus = null;
		for (String logResponse : logStatusSet) {
		    if (CommonMethods.patternMatcher(logResponse, BroadBandTestConstants.STATUS_NOT_TRIGGERED)) {
			LOGGER.info("UPLOAD STATUS : " + logResponse);
			logStatusList.add(logResponse);
			LOGGER.error(
				"Log upload is not triggered even after 5 minutes of log upload initiate using SNMP MIB !!!");
			continue;
		    }
		    logUploadStatus = CommonMethods.patternFinder(logResponse,
			    BroadBandTestConstants.PATTERN_MATCHER_UPLOAD_STATUS);
		    if (CommonMethods.isNotNull(logUploadStatus)) {
			LOGGER.info("UPLOAD STATUS : " + logResponse);
			logStatusList.add(logUploadStatus);
		    }
		}
	    }
	} catch (Exception exp) {
	    LOGGER.error("FOLLOWING EXCEPTION OCCURED IN 'getLogUploadStatusUsingSnmp' API : " + exp.getMessage());
	}
	LOGGER.debug("ENDING METHOD : getLogUploadStatusUsingSnmp");
	return logStatusList;
    }
    
    /**
     * Utility method to verify the Log Upload Status using SNMP; It is polling for 3 minute within an interval of 1
     * seconds to check the Upload Log Status
     * 
     * @param device
     *            {@link Dut}
     * @param expectedStatus
     *            Log Upload status to be checked
     * 
     * @return true if expected status matches with the current Status retrieved via SNMP
     * 
     * @refactor yamini.s
     */

    public static boolean verifyLogUploadStatusUsingSnmp(Dut device, AutomaticsTapApi tapApi, String expectedStatus) {
	LOGGER.debug("ENTERING METHOD verifyLogUploadStatusUsingSnmp");
	// intialize the loop value
	// stores the logupload status
	String loguploadStatusSnmp = null;
	String loguploadStatus = null;
	// stores the test status
	boolean status = false;

	long pollDuration = BroadBandTestConstants.TEN_MINUTE_IN_MILLIS;
		do {
	    loguploadStatusSnmp = BroadBandSnmpUtils.executeSnmpGetOnRdkDevices(tapApi, device,
		    BroadBandSnmpMib.ECM_UPLOAD_STATUS.getOid());

	    if (loguploadStatusSnmp.trim().equalsIgnoreCase(BroadBandTestConstants.STATUS_NOT_TRIGGERED)) {
		loguploadStatus = loguploadStatusSnmp.trim();
	    } else {
		loguploadStatus = CommonMethods.patternFinder(loguploadStatusSnmp,
			BroadBandTestConstants.PATTERN_MATCHER_UPLOAD_STATUS);
		loguploadStatus = CommonMethods.isNotNull(loguploadStatus) ? loguploadStatus.trim() : null;
		LOGGER.info("loguploadStatusSnmp is" + loguploadStatus);
	    }
	    status = CommonMethods.isNotNull(loguploadStatus) && loguploadStatus.equalsIgnoreCase(expectedStatus)
		    && loguploadStatusSnmp.contains(BroadBandTestConstants.TIMEZONE_UTC)
		    && !CommonUtils.patternSearchFromTargetString(loguploadStatus,
			    BroadBandSnmpConstants.SNMP_ERROR_RESPONSE_NO_OBJECT)
		    && !CommonUtils.patternSearchFromTargetString(loguploadStatus,
			    BroadBandTestConstants.NO_RESPONSE_FROM);
	   

	    if (expectedStatus.equalsIgnoreCase(BroadBandTestConstants.STATUS_TRIGGERED)
		    && loguploadStatus.equalsIgnoreCase(BroadBandTestConstants.STATUS_INPROGRESS)
		    || loguploadStatus.equalsIgnoreCase(BroadBandTestConstants.STATUS_COMPLETE)) {
		status = true;

	    } else if (expectedStatus.equalsIgnoreCase(BroadBandTestConstants.STATUS_INPROGRESS)
		    && loguploadStatus.equalsIgnoreCase(BroadBandTestConstants.STATUS_FAILED)) {
		status = true;

	    } else if (expectedStatus.equalsIgnoreCase(BroadBandTestConstants.STATUS_INPROGRESS)
		    && loguploadStatus.equalsIgnoreCase(BroadBandTestConstants.STATUS_COMPLETE)) {
		status = true;

	    }

	} while (!status);
	LOGGER.info("UPLOAD STATUS CHECK USING SNMP RESULT: " + status);
	LOGGER.debug("ENDING METHOD verifyLogUploadStatusUsingSnmp");
	return status;

    }
    
    /**
     * Utility method to poll and verify the Log File name in the Log Server using File Action
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            The {@link AutomaticsTapApi} reference.
     * @param uploadedLogFileName
     *            Name of the Log file uploaded in Log server
     * 
     * @return true if s3 path is obtained for log file name
     * 
     * @refactor yamini.s
     */
    public static boolean verifylogFileNameWithFileNameAction(Dut device, AutomaticsTapApi tapApi,
	    String uploadedLogFileName) {
	LOGGER.debug("ENTERING METHOD verifylogFileNameWithFileNameAction");
	// stores the test status
	boolean status = false;
	
	try {
	    String fileExist = LogUploadUtils.getLogFilePathInCloud((Device) device, uploadedLogFileName);
	    LOGGER.info("fileExist " + fileExist);
	    status=CommonMethods.isNotNull(fileExist);
	}catch (Exception e) {
	    LOGGER.error("Exception occured while checking for Log file uploaded status :" + e.getMessage());
	}
	return status;
    }



}

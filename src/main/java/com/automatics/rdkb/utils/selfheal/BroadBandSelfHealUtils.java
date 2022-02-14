package com.automatics.rdkb.utils.selfheal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.core.DeviceProcess;
import com.automatics.device.Dut;
import com.automatics.enums.ProcessRestartOption;
import com.automatics.enums.StbProcess;
import com.automatics.exceptions.TestException;
import com.automatics.providers.crashanalysis.SettopCrashUtils;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpMib;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpUtils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.wifi.BroadBandWiFiUtils;
import com.automatics.snmp.SnmpDataType;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;

/**
 * Utils class to hold common APi's for self heal test scenarios
 * 
 * @author Gnanaprakasham.S
 *
 */
public class BroadBandSelfHealUtils {

	protected static final Logger LOGGER = LoggerFactory.getLogger(BroadBandSelfHealUtils.class);

	/**
	 * Method to verify logupload frequency on atom side
	 * 
	 * @param device             instance
	 * @param tapEnv             {@link AutomaticsTapApi}
	 * @param logUploadFrequency expected logUploadFrequency value
	 * 
	 * @return true atom has expected log uploaded frequency
	 * @author Gnanaprakasham
	 * @Refactor Sruthi Santhosh
	 */
	public static boolean verifyLogUploadFrequencyOnAtomConsole(Dut device, AutomaticsTapApi tapEnv,
			String logUploadFrequency) {
		LOGGER.debug("STARTING METHOD: verifyLogUploadFrequencyOnAtomConsole()");
		String response = null;
		String errorMessage = null;
		boolean status = false;

		response = CommonMethods.executeCommandInAtomConsole(device, tapEnv,
				BroadBandTestConstants.CMD_GET_LOG_UPLOAD_FREQUENCY_ATOM_CONSOLE);

		if (CommonMethods.isNotNull(response)) {

			response = CommonMethods.patternFinder(response, BroadBandTestConstants.PATTERN_GET_LOG_UPLOAD_FREQUENCY);
			if (CommonMethods.isNotNull(response)) {
				LOGGER.info("Obtained logupload frequecy on atom console : " + response);
				status = CommonMethods.isNotNull(response) && response.equalsIgnoreCase(logUploadFrequency);
			} else {
				errorMessage = "Failed to get the log upload frequency from atom console by executing system commands";
				LOGGER.error(errorMessage);
				throw new TestException(errorMessage);
			}

		} else {
			errorMessage = "Obtained null response for executing system command \"cat /tmp/cron/root\"to get log upload frequency on atom console";
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		}
		LOGGER.debug("ENDING METHOD: verifyLogUploadFrequencyOnAtomConsole()");
		return status;
	}

	/**
	 * Method to enable/disable diagnostic mode using webpa params
	 * 
	 * @param device             instance
	 * @param tapEnv             {@link AutomaticsTapApi}
	 * @param diagnosticModeFlag diagnosticModeFlag
	 * @return true if diagnosticMode changed as per flag value
	 * 
	 * @author Gnanaprakasham
	 * @Refactor Sruthi Santhosh
	 */

	public static boolean enableOrDisableDiagnosticModeUsingWebpaParams(Dut device, AutomaticsTapApi tapEnv,
			String diagnosticModeFlag) {
		LOGGER.debug("STARTING METHOD : enableOrDisableDiagnosticModeUsingWebpaParams");
		// Variable to store execution status
		boolean status = false;
		String errorMessage = null;

		try {
			errorMessage = "Not able to enable diagnostic mode using webpa param \"Device.SelfHeal.X_RDKCENTRAL-COM_DiagnosticMode\" ";
			status = BroadBandWiFiUtils.setWebPaParams(device, BroadBandWebPaConstants.WEBPA_PARAM_DIAGNOSTIC_MODE,
					diagnosticModeFlag, WebPaDataTypes.BOOLEAN.getValue());
		} catch (Exception exception) {
			errorMessage = "Exception occurred during execution : " + exception.getMessage();
			LOGGER.error(errorMessage);
		}

		LOGGER.debug("ENDING METHOD : enableOrDisableDiagnosticModeUsingWebpaParams");
		return status;
	}
	
    /**
     * Method to initiate process crash and verify process restarted status
     * 
     * @param device
     *            Dut instance
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param stbProcess
     *            - Process name
     * @return true if process crash in successfully initiated
     * 
     * @author Sumathi Gunasekaran
     * @refactor Athira
     */
    public static boolean initiateProcessCrashAndVerifyProcessRestartedStatus(Dut device, AutomaticsTapApi tapEnv,
	    StbProcess stbProcess) {
	LOGGER.debug("STARTING METHOD : InitiateProcessCrashAndVerifyProcessRestartedStatus");
	// Variable to store process ID
	String processId = null;
	// Instance for settop crash utils
	final SettopCrashUtils settopCrashUtils = new SettopCrashUtils();
	// boolean variable to store status
	boolean status = false;
	// Variable to store command response
	String response = null;
	// Variable to store error message
	String errorMessage = null;
	DeviceProcess pardouseProcess = new DeviceProcess();
	pardouseProcess.setProcessName(stbProcess.getProcessName());
	
	try {

	    processId = CommonMethods.getPidOfProcess(device, tapEnv, stbProcess.getProcessName());

	    if (CommonMethods.isNull(processId) && CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
		// retrieve the PID form ATOM

		processId = BroadBandCommonUtils.getPidOfProcessFromAtomConsole(device, tapEnv, pardouseProcess.getProcessName());

		if (CommonMethods.isNotNull(processId)) {
		    LOGGER.info("SUCCESSFULLY RETRIEVED PROCESS ID FOR " + stbProcess.getProcessName() + " PROCESS is :"+processId);

		} else {
		    errorMessage = "Not able to retrieve process ID for " + stbProcess.getProcessName()
			    + " process either from ARM/ATOM console";
		    LOGGER.error(errorMessage);
		    throw new TestException(errorMessage);
		}

	    } else if (CommonMethods.isNull(processId)) {
		errorMessage = "Not able to get the process ID for " + stbProcess.getProcessName()
			+ " process from ARM console";
		LOGGER.error(errorMessage);
		throw new TestException(errorMessage);
	    } else {
		LOGGER.info("SUCCESSFULLY RETRIEVED PROCESS ID FOR " + stbProcess.getProcessName() + " PROCESS");
	    }

	    // Self heal action will not restarted CR process after crash. instead it will reboot the device
	    // So only kill the current process id
	    if (stbProcess.getProcessName().equalsIgnoreCase(StbProcess.CCSP_CR.getProcessName())) {

		if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
		    response = tapEnv.executeCommandOnAtom(device,
			    ProcessRestartOption.KILL_11.getCommand() + processId);
		} else {
		    response = tapEnv.executeCommandUsingSsh(device,
			    ProcessRestartOption.KILL_11.getCommand() + processId);
		}
		LOGGER.info("Obtained response for process " + stbProcess.getProcessName() + "kill" + response);
		status = CommonMethods.isNull(response);
	    } else {
		// Self heal action will restart the process after crash (other than CR process).
		// So verify process restarted status after crash
		status = restartProcess(device, tapEnv, ProcessRestartOption.KILL_11, stbProcess, processId);
	    }

	} catch (Exception exeception) {
	    throw new TestException(exeception.getMessage());
	}
	LOGGER.debug("ENDING METHOD : InitiateProcessCrashAndVerifyProcessRestartedStatus");
	return status;
    }
    
    /**
     * Method to restart the process
     * 
     * @param device
     *            Dut instance
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param option
     *            Restart option
     * @param stbProcess
     *            - Process name
     * @param processId
     *            Process id to to killed
     * @return true if process restarted successfully
     * 
     * @author sgnana010c
     * 
     */
    public static boolean restartProcess(Dut device, AutomaticsTapApi tapEnv, ProcessRestartOption option,
	    StbProcess processName, String processId) {
	LOGGER.debug("STARTING METHOD : restartProcess");
	// Receiver restart status
	boolean restartStatus = false;
	// loop count
	final int loopCount = 5;
	// New process id
	String newProcessId = null;
	// command to execute
	String command = null;
	String errorMessage = null;

	try {
	    if (option != null && CommonMethods.isNotNull(processId)) {

		command = option.getCommand() + processId;

		// kill the current running process
		tapEnv.executeCommandInSettopBox(device, command);

		for (int i = 0; i < loopCount; i++) {

		    // get the new process id
		    newProcessId = CommonMethods.getPidOfProcess(device, tapEnv, processName.getProcessName());

		    if (CommonMethods.isNotNull(newProcessId)) {

			if (processId.equals(newProcessId)) {
			    LOGGER.info("Unable to restart '" + processName + "' process. Retrying ...!!");
			} else {
			    LOGGER.info("Successfully restarted '" + processName + "' process.");
			    restartStatus = true;
			    break;
			}
		    } else {
			LOGGER.info(processName.getProcessName()
				+ " is not started after crash...waiting for 3 more minutes!!!!");
			tapEnv.waitTill(AutomaticsConstants.THREE_MINUTES);
		    }

		}
	    } else {
		LOGGER.error("Obtained process id/Process restart option is null for process crash !!!  ");
	    }
	} catch (Exception exception) {
	    errorMessage = "Exception occured during restart process " + processName.getProcessName() + " : "
		    + exception.getMessage();
	    LOGGER.error(errorMessage);
	}

	if (!restartStatus) {
	    errorMessage = "Self Heal action is not restarting the " + processName.getProcessName()
		    + " process even after 15 minutes of " + processName.getProcessName() + " process crash ";
	    LOGGER.error(errorMessage);
	    throw new TestException(errorMessage);
	}
	LOGGER.debug("ENDING METHOD : restartProcess");
	return restartStatus;

    }
    
    /**
     * Method for setting precondition for self heal crash scenarios
     */
    public static boolean executePreconditionForSelfHealTestScenario(Dut settop, AutomaticsTapApi tapEnv) {

	boolean status = false;
	String response = null;
	boolean snmpstatus = false;
	/*
	 * we need to use web pa command else we can use the snmp
	 * command .The following command verifies if the device is DSL device or not.
	 */

	boolean isDSLDevice = DeviceModeHandler.isDSLDevice(settop);
	if ((isDSLDevice || CommonMethods.isRunningEthwanMode())) {

	    /*
	     * status = BroadBandWiFiUtils.setWebPaParams(settop, BroadBandWebPaConstants.USAGE_COMPUTE_WINDOW,
	     * BroadBandTestConstants.STRING_VALUE_TWO, WebPaDataTypes.UNSIGNED_INT.getValue());
	     */
	    // unit
	    status = BroadBandWebPaUtils.setAndVerifyParameterValuesUsingWebPaorDmcli(settop, tapEnv,
		    BroadBandWebPaConstants.USAGE_COMPUTE_WINDOW, BroadBandTestConstants.CONSTANT_2,
		    BroadBandTestConstants.STRING_CONSTANT_3);

	} else {

	    if (BroadbandPropertyFileHandler.isDeviceCheckForSelfHeal2(settop)) {
		BroadBandWebPaUtils.setVerifyWebPAInPolledDuration(settop, tapEnv,
			BroadBandWebPaConstants.WEBPA_PARAM_AGGRESSIVE_SELFHEAL_INTERVAL,
			BroadBandTestConstants.CONSTANT_2, BroadBandTestConstants.STRING_CONSTANT_2,
			BroadBandTestConstants.ONE_MINUTE_IN_MILLIS, BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS);
	    }
	    long startTime = System.currentTimeMillis();
	    do {
		response = BroadBandSnmpUtils.retrieveSnmpSetOutputWithDefaultIndexOnRdkDevices(settop, tapEnv,
			BroadBandSnmpMib.ECM_SELFHEAL_RESOURCE_USAGE_COMPUTER_WINDOW.getOid(), SnmpDataType.INTEGER,
			BroadBandTestConstants.STRING_CONSTANT_3);
		snmpstatus = BroadBandSnmpUtils.hasNoSNMPErrorOnResponse(tapEnv, settop, response);
	    } while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.TEN_MINUTE_IN_MILLIS
		    && !snmpstatus
		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));


	    if (CommonMethods.isNotNull(response)) {

		status = response.equalsIgnoreCase(BroadBandTestConstants.STRING_CONSTANT_3);

	    }

	}

	if (!status) {
	    LOGGER.error("Failed to set the precondition for self heal crash scenario verification");
	}

	return status;
    }
}
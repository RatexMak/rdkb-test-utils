package com.automatics.rdkb.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Dut;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpMib;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpUtils;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.wifi.BroadBandWiFiUtils;
import com.automatics.snmp.SnmpDataType;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.webpa.WebPaParameter;

/**
 * Utility class which handles the RDK B Bridge mode common functionalities and
 * verification.
 * 
 * @author revanth.k
 * 
 */
public class BroadBandBridgeModeUtils {
	
    /**
     * Logger instance for {@link BroadBandCommonUtils}
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(BroadBandBridgeModeUtils.class);

	/**
	 * 
	 * Precondition: enable the below parameters - 1)Private SSID's for both 2.4Ghz
	 * and 5Ghz. 2)Public WiFi SSID's for both 2.4Ghz and 5Ghz.
	 * 
	 * @param device
	 * @Refactor Sruthi Santhosh
	 * 
	 */

	public static void preConditionsForBridgeModeTestCases(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("SATRTING METHOD : preConditionsForBridgeModeTestCases ()");
		boolean status = false;
		BroadBandResultObject resultObject = null;
		String errorMessage = "UNABLE TO TRUN BACK THE DEVICE INTO ROUTER MODE";
		LOGGER.info("CHECK IF DEVICE IS INITIALLY IN BRIDGE MODE AND TURN IT BACK TO ROUTER MODE");
		status = BroadBandCommonUtils.verifyDeviceInRouterModeStatusUsingWebPaCommand(tapEnv, device);
		if (!status) {
			try {
				LOGGER.info("DEVICE FOUND TO BE IN BRIDGE MODE, SO REVERTING BACK TO ROUTER MODE");
				status = BroadBandCommonUtils.setDeviceInRouterModeStatusUsingWebPaCommand(tapEnv, device);
			} catch (Exception exception) {
				errorMessage = errorMessage + " " + exception.getMessage();
				LOGGER.error(errorMessage);
				throw new TestException(
						BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION : FAILED : " + errorMessage);
			}
		}
		if (status) {
			try {
				LOGGER.info("ENABLING THE PUBLIC WIFI PARAMETER");
				errorMessage = "UNABLE TO ENABLE THE PUBLIC WIFI ON GATEWAY DEVICE";
				List<WebPaParameter> webPaParameters = BroadBandWebPaUtils.getListOfWebpaParametersForBothPublicWifis();
				resultObject = BroadBandWebPaUtils.executeSetAndGetOnMultipleWebPaGetParams(device, tapEnv,
						webPaParameters);
				status = resultObject.isStatus();
				errorMessage = resultObject.getErrorMessage();
				if (!status) {
					LOGGER.error(errorMessage);
					throw new TestException(
							BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION : FAILED : " + errorMessage);
				}
				LOGGER.info("PUBLIC WIFI ENABLED SUCCESSFULLY");

				errorMessage = "UNABLE TO VERIFY THE PRIVATE WIFI 2.4 AND 5 GHZ STATUS";
				LOGGER.info("VERIFYING THE PRIVATE WIFI 2.4 AND 5 GHZ STATUS");
				status = BroadBandWiFiUtils.enableBoth2And5GhzWiFiSsidsUsingWebPaCommand(device, tapEnv,
						BroadBandTestConstants.PRIVATE_WIFI_TYPE);
				if (!status) {
					LOGGER.error(errorMessage);
					throw new TestException(
							BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION : FAILED : " + errorMessage);
				}
				LOGGER.info("VERIFIED THE PRIVATE WIFI 2.4 AND 5 GHZ STATUS");

			} catch (Exception exception) {
				errorMessage = errorMessage + " " + exception.getMessage();
				LOGGER.error(errorMessage);
				throw new TestException(
						BroadBandTestConstants.PRE_CONDITION_ERROR + "PRE-CONDITION : FAILED : " + errorMessage);
			}
		}
		LOGGER.debug("ENDING METHOD : preConditionsForBridgeModeTestCases ()");
	}
	
    /**
     * Method to enable disable Bridge mode through snmp. throws test exception if value is not set
     * 
     * @param tapEnv
     *            The {@link AutomaticsTapApi} instance
     * @param device
     *            The device to be validated.
     * @param value
     *            The value to which bridge mode snmp oid must be set 1 - for enabling bridge mode 2 - for disabling
     *            bridge mode
     * @Refactor Sruthi Santhosh
     */
    public static boolean enableDisableBridgeModeThroughSnmp(AutomaticsTapApi tapEnv, Dut device, String value) {
	LOGGER.debug("STARTING METHOD : EnableDisableBridgeModeThroughSnmp");
	boolean status = false;

	status = BroadBandSnmpUtils.executeSnmpSetCommand(tapEnv, device,
		BroadBandSnmpMib.ENABLE_DISABLE_BRIDGE_MODE.getOid(), SnmpDataType.INTEGER, value,
		BroadBandSnmpMib.ENABLE_DISABLE_BRIDGE_MODE.getTableIndex());

	LOGGER.debug("ENDING METHOD : EnableDisableBridgeModeThroughSnmp");
	return status;
    }
}
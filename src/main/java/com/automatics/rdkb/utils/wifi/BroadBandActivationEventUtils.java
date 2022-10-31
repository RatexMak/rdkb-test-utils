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

package com.automatics.rdkb.utils.wifi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandTraceConstants;
import com.automatics.rdkb.enums.BroadBandActivationEventEnum;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.test.AutomaticsTestBase;
import com.automatics.utils.CommonMethods;

/**
 * Utility class which handles the RDK B WiFi Activation Journey Events.
 * 
 * @author BALAJI V
 * @refactor yamini.s
 * 
 */
public class BroadBandActivationEventUtils extends AutomaticsTestBase {

    /**
     * Logger instance for {@link BroadBandWebPaAUtils}
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandActivationEventUtils.class);

    /**
     * Utility Method to retrieve the events details to be validated for the device based on the Dut Model.
     * 
     * @param settopModel
     *            String representing the Dut Model.
     * @param wifi1Ssid
     *            String representing the Wifi Radio 2.4 GHz SSID Name
     * @param wifi2Ssid
     *            String representing the Wifi Radio 5 GHz SSID Name
     * 
     * @return List of BroadBandActivationJourneyEnum.
     * @refactor yamini.s
     */
    public static List<BroadBandActivationEventEnum> getEventsToBeValidated(String settopModel, String wifi1Ssid,
	    String wifi2Ssid) {
	LOGGER.info("ENTERING METHOD: getEventsToBeValidated");
	String logMessageToBeSearched = null;
	List<BroadBandActivationEventEnum> eventsList = new ArrayList<BroadBandActivationEventEnum>();
	for (BroadBandActivationEventEnum activationEvent : BroadBandActivationEventEnum.values()) {
	    
	    if (activationEvent.name().equals(BroadBandActivationEventEnum.WIFI_NAME_BROADCAST_2_4.name())) {
		logMessageToBeSearched = activationEvent.getLogMessageToBeSearched();
		
		logMessageToBeSearched = BroadBandCommonUtils.concatStringUsingStringBuffer(logMessageToBeSearched,
			BroadBandTestConstants.DELIMITER_COLON, wifi1Ssid);
		activationEvent.setLogMessageToBeSearched(logMessageToBeSearched);
		
	    } else if (activationEvent.name().equals(BroadBandActivationEventEnum.WIFI_NAME_BROADCAST_5.name())) {
		logMessageToBeSearched = activationEvent.getLogMessageToBeSearched();
		
		logMessageToBeSearched = BroadBandCommonUtils.concatStringUsingStringBuffer(logMessageToBeSearched,
			BroadBandTestConstants.DELIMITER_COLON, wifi2Ssid);
		activationEvent.setLogMessageToBeSearched(logMessageToBeSearched);
		
	    }
	    
	    LOGGER.info("device model :"+settopModel);
	    if (activationEvent.getApplicableDeviceModels().toString().contains(settopModel)) {
		eventsList.add(activationEvent);
	    }
	}
	LOGGER.info("BROADBAND ACTIVATION EVENTS TO BE VALIDATED: " + eventsList.size());
	LOGGER.info("ENDING METHOD: getEventsToBeValidated");
	return eventsList;
    }

    /**
     * Utility method to extract the value from the Activation Journey Log Message.
     * 
     * @param telemetryLogMessage
     *            String representing the Telemetry Log Message.
     * @param activationParameter
     *            String representing the Activation Parameter.
     * @return String representing the value from Telemetry Log Message.
     * @refactor yamini.s
     */
    public static String extractValueActivationJourney(String telemetryLogMessage, String activationParameter) {
	LOGGER.debug("STARTING METHOD extractValueActivationJourney");
	String patternMatcher = null;
	if (telemetryLogMessage.contains(BroadBandTraceConstants.ACTIVATION_LAN_INITIALIZATION_START)
		|| telemetryLogMessage.contains(BroadBandTraceConstants.ACTIVATION_LAN_INITIALIZATION_COMPLETE_NEW)
		|| telemetryLogMessage.contains(BroadBandTraceConstants.ACTIVATION_WAN_INITIALIZATION_START)
		|| telemetryLogMessage.contains(BroadBandTraceConstants.ACTIVATION_WAN_INITIALIZATION_COMPLETE_NEW)) {
	    patternMatcher = BroadBandTestConstants.PATTERN_MATCHER_ACTIVATION_BOOTLOG_VALUE
		    .replace(BroadBandTestConstants.TEXT_TELEMETRY_LOG, activationParameter);

	} else {
	    patternMatcher = BroadBandTestConstants.PATTERN_MATCHER_ACTIVATION_LOG_VALUE
		    .replace(BroadBandTestConstants.TEXT_TELEMETRY_LOG, activationParameter);
	}
	LOGGER.info("PATTERN TO BE MATCHED: " + patternMatcher);
	String telemetryParameterValue = CommonMethods.isNotNull(telemetryLogMessage)
		? CommonMethods.patternFinder(telemetryLogMessage, patternMatcher)
		: null;
	LOGGER.info("TELEMETRY LOG PARAMETER VALUE: " + telemetryParameterValue);
	LOGGER.debug("ENDING METHOD extractValueActivationJourney");
	return telemetryParameterValue;
    }
}

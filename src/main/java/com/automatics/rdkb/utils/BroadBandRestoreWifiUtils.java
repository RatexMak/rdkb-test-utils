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
package com.automatics.rdkb.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Dut;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants.RdkBSsidParameters;
import com.automatics.rdkb.constants.RDKBTestConstants.WiFiFrequencyBand;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpMib;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;

/**
 * Utils for Wifi restore settings
 * 
 * @author anandam.s
 * @Refactor Govardhan
 *
 */
public class BroadBandRestoreWifiUtils {
    /**
     * Logger instance for {@link BroadBandRestoreWifiUtils}
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(BroadBandRestoreWifiUtils.class);

    /**
     * Verify whether the SSID obtained has last 4 hex values in mac address
     * 
     * @param device
     *            Dut under test
     * @param radio
     *            {@link WiFiFrequencyBand}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param isSnmpNeeded
     *            true if SNMP Validation is Required.
     * @return true if ssid is as per standard
     */
    public static BroadBandResultObject verifyDefaultSSIDForPartner(Dut device, WiFiFrequencyBand radio,
	    AutomaticsTapApi tapEnv, boolean isSnmpNeeded) {
	LOGGER.debug("STARTING METHOD verifyDefaultSSIDForPartner");
	BroadBandResultObject broadBandResultObject = new BroadBandResultObject();
	String webpaParameter = null;
	String snmpOid = null;
	RdkBSsidParameters ssidparam = null;
	long startTime = System.currentTimeMillis();
	String partnerId = null;
	String errorMessage = "Unable to validate default SSID of radio " + radio;
	boolean status = false;
	try {
	    do {
		partnerId = tapEnv.executeWebPaCommand(device,
			BroadBandWebPaConstants.WEBPA_PARAM_FOR_SYNDICATION_PARTNER_ID);
		LOGGER.info("Current Partner ID of the device Retrieved via WEBPA is :" + partnerId);
	    } while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.TWO_MINUTE_IN_MILLIS
		    && !CommonMethods.isNotNull(partnerId)
		    && BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
	    if (CommonMethods.isNotNull(partnerId) && BroadBandTestConstants.PARTNER_ID_LIST.contains(partnerId)) {
		if (radio.equals((WiFiFrequencyBand.WIFI_BAND_2_GHZ))) {
		    snmpOid = BroadBandSnmpMib.ECM_WIFI_SSID_2_4.getOid();
		    ssidparam = RdkBSsidParameters.SSID_FOR_2GHZ_PRIVATE_WIFI;
		    webpaParameter = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID;
		} else if (radio.equals((WiFiFrequencyBand.WIFI_BAND_5_GHZ))) {
		    snmpOid = BroadBandSnmpMib.ECM_WIFI_SSID_5.getOid();
		    ssidparam = RdkBSsidParameters.SSID_FOR_5GHZ_PRIVATE_WIFI;
		    webpaParameter = BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID;
		}
		String ssid = tapEnv.executeWebPaCommand(device, webpaParameter);
		LOGGER.info(radio.toString() + "SSID retieved after wifi reset  :  " + ssid);
		if (CommonMethods.isNotNull(ssid)) {
		    status = BroadBandCommonUtils.validateDefaultSsidforDifferentPartners(device, tapEnv, ssid,
			    ssidparam, device.getModel(), partnerId, radio);
		    broadBandResultObject.setStatus(status);
		    broadBandResultObject.setErrorMessage(errorMessage);
		    return broadBandResultObject;
		} else {
		    errorMessage = "Null value obtained for SSID!";
		    broadBandResultObject.setErrorMessage(errorMessage);
		    broadBandResultObject.setStatus(status);
		}
	    } else {
		errorMessage = "Invalid Partner ID Obtained Via WEBPA Response.";
		broadBandResultObject.setErrorMessage(errorMessage);
		broadBandResultObject.setStatus(status);
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception Occured While Validating the Default SSID: " + e.getMessage());
	    broadBandResultObject.setErrorMessage(e.getMessage());
	}
	LOGGER.debug("Ending METHOD verifyDefaultSSIDForPartner");
	return broadBandResultObject;
    }
}

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Device;
import com.automatics.device.Dut;
import com.automatics.rdkb.auth.Crypto;
import com.automatics.rdkb.constants.BroadBandPropertyKeyConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.CommonMethods;

/**
 * Wrapper class for Getting Automatics Properties
 * 
 * @author anandam.s
 */
public class BroadbandPropertyFileHandler {
	/**
	 * Logger instance for {@link BroadbandPropertyFileHandler}
	 */
	protected static final Logger LOGGER = LoggerFactory.getLogger(BroadbandPropertyFileHandler.class);

	/**
	 * Uses the partial key by appending it with the settop platform to get the
	 * property value from cats.props.
	 *
	 * @param settop          The settop instance.
	 * @param partialPropsKey Partial property key which will be appended with the
	 *                        settop platform to form the complete key.(Example:-
	 *                        "cdl.unsigned.image.name." it get resolved to
	 *
	 * @return CATS property value
	 */
	public static String getAutomaticsPropsValueByResolvingPlatform(Dut device, String partialPropsKey) {
		String propertyToCheck = partialPropsKey + device.getManufacturer().toLowerCase() + "_"
				+ device.getModel().toLowerCase();
		LOGGER.info("Platform specific resolved property key is : " + propertyToCheck);
		return AutomaticsTapApi.getSTBPropsValue(propertyToCheck);
	}

	/**
	 * This API will return partner values
	 * 
	 * @return partner values
	 * @author Govardhan
	 */
	public static String getPartnerIDValues() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PARTNER_ID_LIST);
	}

	/**
	 * This API will return the WifiBandForPartner
	 * 
	 * @return models for which should ssid should append with wifiband
	 * @author Govardhan
	 */
	public static String getWifiBandForPartner(Dut device, String wifiBand) {
		return AutomaticsTapApi.getSTBPropsValue(
				BroadBandPropertyKeyConstants.PROP_KEY_WIFIBAND_PARTNERID + wifiBand + device.getManufacturer());
	}

	/**
	 * This API will return the rfc SN or ECMMAC by removing specific chars
	 * 
	 * @return SN or ECMMAC by removing specific chars
	 * @author Govardhan
	 */
	public static String getSerialNumber_ECMAC_PatternAfterRemoval(String partnerID) {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PARTNER_CHARS_REMOVE + partnerID);
	}

	/**
	 * This API will return model and partnerId
	 * 
	 * @return model and partnerId
	 * @author Govardhan
	 */
	public static String getPartnerIDandModelValues() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PARTNERID_DEVICE);
	}

	/**
	 * Utility method to get operating standards based on the model
	 * 
	 * @return operating standards
	 * @author Govardhan
	 */
	public static String getRDKBWifiParametersBasedOnModel(Dut device, String partialKey) {
		String operatingStandards = null;
		try {
			operatingStandards = getAutomaticsPropsValueByResolvingPlatform(device, partialKey);
		} catch (Exception e) {

		}
		return operatingStandards;
	}

	/**
	 * Utility method to get ParodusTokenServerURL
	 * 
	 * @return ParodusTokenServerURL
	 * @author Govardhan
	 */
	public static String getParodusTokenServerURL() {
		String operatingStandards = null;
		try {
			operatingStandards = AutomaticsTapApi
					.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PARODUS_TOKEN_SERVER_URL);
		} catch (Exception e) {

		}
		return operatingStandards;
	}

	public static Boolean isSplunkEnabled() {
		return Boolean.parseBoolean(AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.SPLUNK_ENABLED));
	}

	public static String getSplunkHost() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_SPLUNK_HOST);
	}

	public static Integer getSplunkPort() {
		return Integer.parseInt(AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_SPLUNK_PORT));
	}

	public static String getSplunkUser() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_SPLUNK_USER);
	}

	public static String getSplunkPwd() {
		return Crypto
				.decrypt(AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_SPLUNK_PASSWORD));
	}

	/**
	 * This API will get the latest OpenSSL Version from properties
	 * 
	 * @author said.h
	 */
	public static String getLatestOpenSSLVersion() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.CURRENT_OPENSSL_VERSION);
	}

	/**
	 * This API will check whether the version on the box is prod version or not
	 * 
	 * @author Govardhan
	 */
	public static Boolean verifyIsProdBuildOnDevice(String firmWareVersion) {
		boolean isProdBuild = false;
		try {
			isProdBuild = firmWareVersion.toLowerCase().contains(
					AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_PROD_BUILD_SUBSTRING));
		} catch (Exception e) {

		}
		return isProdBuild;
	}

	/**
	 * This API will return the Maximum reset count
	 * 
	 * @return Maximum reset count
	 */
	public static String getDefaultMaxResetCount() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.DEFAULT_MAX_RESET_COUNT);
	}

	/**
	 * This API will return the prod xconf code download url
	 * 
	 * @return prod CDL server URL
	 */
	public static String getProdCDLServerURL() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.STB_PROP_PROD_CODE_DWLD_URL);
	}

	/**
	 * This API will return the CI server swupdate URL
	 * 
	 * @return CI server swupdate URL This API will get the latest OpenSSL Version
	 *         from properties
	 * 
	 * @author said.h
	 */
	public static String getCIServerSwupdateURL() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_XCONF_CI_SERVER_SOFTWARE_UPDATE_URL_IPV6);
	}

	/**
	 * This API will return the XCONF RFC server URL
	 * 
	 * @return XCONF RFC server URL
	 */
	public static String getXconfRfcURL() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PROXY_XCONF_RFC_URL);
	}
	
	/**
     * This API will get the latest dropbear Version from properties
     * 
     * @author yamini.s
     */
    public static String getLatestDropbearVersion() {
	return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LATEST_DROPBEAR_VERSION);
    }
    
    /**
     * This API will return the Jump server for reverse ssh
     * 
     * @return jump server for reverse ssh
     */
    public static String getReverseSshJumpServer() {
	return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROPERTY_REVERSE_SSH_JUMP_SERVER);
    }
    
    /**
     * This method will return the IP address of a non white listed jump server
     * 
     * @return non white listed jump server IP
     */
    public static String getNonWhiteListedJumpserverIP() {

	return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROPERTY_NON_WHITELISTED_JUMP_SERVER_IP);
    }
    
    /**
     * This API will list of snmp reboot reason for each model
     * 
     * @return model and partnerId
     * @author anandam
     */
    public static String getSnmpRebootReasonList() {
	return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_REBOOT_REASON);
    }
    
}

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
	 * Uses the partial key by appending it with the device platform to get the
	 * property value from cats.props.
	 *
	 * @param device          The device instance.
	 * @param partialPropsKey Partial property key which will be appended with the
	 *                        device platform to form the complete key.(Example:-
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

	/**
	 * This API will return specific partner values saved in propery file
	 * 
	 * @return partner values
	 * @author Athira
	 */
	public static String getSecificPartnerIDValues() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_SPECIFIC_PARTNER_ID);
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

	/**
	 * This API will check whether the status of the device is true or not
	 * 
	 * @author yamini.s
	 */
	public static boolean getStatusForDeviceCheck(Dut device) {
		return Boolean.parseBoolean(
				getAutomaticsPropsValueByResolvingPlatform(device, BroadBandPropertyKeyConstants.PARTIAL_DEVICE_CHECK));
	}

	/**
	 * This API will get the File space for the device
	 * 
	 * @author Govardhan
	 */
	public static String getFileSystemSpaceBasedOnDevice(Dut device) {
		String space = null;
		try {
			space = getAutomaticsPropsValueByResolvingPlatform(device,
					BroadBandPropertyKeyConstants.PROP_KEY_FILE_SYSTEM_SPACE);
		} catch (Exception e) {
			space = BroadBandTestConstants.STRING_VALUE_80;
		}
		return space;
	}

	/**
	 * This API will get the Lighttpd Version From Properties
	 * 
	 * @author Govardhan
	 */
	public static String getLighttpdVersionFromProperties() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_LIGHTTPD_VERSION);
	}

	/**
	 * This API will return the ssid password
	 * 
	 * @author yamini.s
	 */
	public static String getSSIDPassword() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.TEST_SSID_PASSWORD);
	}

	/**
	 * This API will return whether the led logs available
	 * 
	 * @author Athira
	 */
	public static Boolean isSpecificDevice(Dut device) {
		Boolean status = false;
		String model = null;
		try {
			model = BroadbandPropertyFileHandler.getAutomaticsPropsValueByResolvingPlatform(device,
					BroadBandPropertyKeyConstants.PROP_KEY_SPECIFIC_DEVICE);

			if (CommonMethods.isNotNull(model)) {
				status = true;
			}
		} catch (Exception e) {
			LOGGER.info("No device specific value found");
		}
		return status;
	}

	/**
	 * This API will get the dnsmasq version for devices from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDnsMasqVersion() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_DNSMASQ_VERSION);
	}

	/**
	 * This API will get the dnsmasq version for devices with dunfell from
	 * properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDnsMasqVersionForDunfell() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_DUNFELL_DNSMASQ_VERSION);
	}

	/**
	 * This API will return the AutoVault download URL
	 * 
	 * @author Govardhan
	 */
	public static String getAutoVaultDownloadURL() {
		return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.PROP_KEY_AUTOVAULT_DOWNLOAD_URL);
	}

	/**
	 * This API will return the AutoVault Credentials
	 * 
	 * @author Govardhan
	 */
	public static String getAutoVaultBase64Credentials() {
		return AutomaticsPropertyUtility
				.getProperty(BroadBandPropertyKeyConstants.PROP_KEY_AUTOVAULT_BASE64_CREDENTIALS);
	}

	/**
	 * This API will get the stress test file_autovault from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getStressTestFilePath() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.STRESS_TEST_FILE_PATH);
	}

	/**
	 * This API will return whether the device is single reboot or not
	 * 
	 * @author Govardhan
	 */
	public static Boolean isSingleRebootRFCFeatureDevice(Dut device) {
		Boolean status = false;
		String model = null;
		try {
			model = AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_SINGLE_REBOOT_DEVICE);

			if (model.equalsIgnoreCase(device.getModel())) {
				status = true;
			}
		} catch (Exception e) {

		}
		return status;
	}

	/**
	 * This API will get the Lighttpd Version From Properties
	 * 
	 * @author Govardhan
	 */
	public static String getWebpaServerURL() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_WEBPA_SERVER_URL);
	}

	/**
	 * Utility method to get IncorrectWebparURL
	 * 
	 * @return IncorrectWebparURL
	 * @author Govardhan
	 */
	public static String getIncorrectWebparURL() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_INCORRECT_WEBPA_URL);
	}

	/**
	 * Utility method to get getPlumeBackHaulURL
	 * 
	 * @return IncorrectWebparURL
	 * @author Govardhan
	 */
	public static String getPlumeBackHaulURL() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PLUME_BACK_HAUL_URL);
	}

	/**
	 * This API will get the range of frequency from properties
	 * 
	 * @author Rakesh C N
	 */
	public static String getRangeOfFrequency() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.RANGE_FOR_DWNSTRM_CHNL_FREQ_VALUE);
	}

	/**
	 * This API will get the Invalid Firewaredownload file path autovault from
	 * properties
	 * 
	 * @author Rakesh C N
	 */
	public static String getInvalidFile() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.AUTOVAULT_FIRMWAREDOWNLOAD_PATH_DSL);
	}

	/**
	 * This API will get the cpuprocanalyzer file path_autovault from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getCpuProcAnalyzerFile() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.CPUPROCANALYZER_FILE_PATH);
	}

	/**
	 * This API will get the process list file path_autovault from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getProcessListFile() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROCESS_LIST_FILE_PATH);
	}

	/**
	 * This API will get the TCPDUMP file_autovault from properties
	 * 
	 * @author Rakesh C N
	 */
	public static String getTCPDUMPFile() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.FILE_PATH_TCPDUMP);
	}

	/**
	 * Utility method to get CodeDownloadUrl
	 * 
	 * @return CodeDownloadUrl
	 * @author Govardhan
	 */
	public static String getCodeDownloadUrl() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_CDL_URL);
	}

	/**
	 * This API will get the supported txrate 5ghz
	 * 
	 * @author Said Hisham
	 */
	public static String getSupportedTxRate5GhzForDevice() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.SUPPORTED_TX_RATE_5GHZ_FOR_N_DEVICE);
	}

	/**
	 * This API will get the operational txrate
	 * 
	 * @author Said Hisham
	 */
	public static String getOperationalTxRate5GhzForDevice() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.OPERATIONAL_TX_RATE_5GHZ_FOR_N_DEVICE);
	}

	/**
	 * This API will get the supported data txrate
	 * 
	 * @author Said Hisham
	 */
	public static String getSupportedDataTxRate5GhzForDevice() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.SUPPORTED_DATA_TX_RATE_5GHZ_FOR_DEVICE);
	}

	/**
	 * This API will get the supported txrate 2ghz
	 * 
	 * @author Said Hisham
	 */
	public static String getSupportedTxRate2GhzForDevice() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.SUPPORTED_TX_RATE_2GHZ_FOR_DEVICE);
	}

	/**
	 * This API will check for device related to selfheal
	 * 
	 * @author Said Hisham
	 */
	public static boolean isDeviceCheckForSelfHeal(Dut device) {
		Boolean status = Boolean.parseBoolean(getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.PARTIAL_DEVICE_CHECK_SELF_HEAL));
		if (status)
			return status;
		else
			return false;
	}

	public static String getPrivateWifiSsid() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PRIVATE_WIFI_SSID_BAND_STEERING);
	}

	public static String getPrivateWifiPassPhrase() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PRIVATE_WIFI_PASSPHRASE_BAND_STEERING);
	}

	/**
	 * Utility method to get the backupfiles from properties
	 * 
	 * @author yamini.s
	 */
	public static String getSecureMountBackUpFiles() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.SECURE_MOUNT_BACKUP_FILES);
	}

	/**
	 * Utility method to get the ProxyXconfUrl from properties
	 * 
	 * @author yamini.s
	 */
	public static String getProxyXconfUrl() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PROXY_XCONF_URL);

	}

	/**
	 * This API will return whether the led logs available
	 * 
	 * @author Athira
	 */
	public static Boolean isDeviceledlogsAvailable(Dut device) {
		Boolean status = false;
		String model = null;
		try {
			model = BroadbandPropertyFileHandler.getAutomaticsPropsValueByResolvingPlatform(device,
					BroadBandPropertyKeyConstants.PROP_KEY_LEDLOGS_DEVICE);

			if (CommonMethods.isNotNull(model)) {
				status = true;
			}
		} catch (Exception e) {
			LOGGER.info("No device specific value found");
		}
		return status;
	}

	/**
	 * This API will return whether the led logs available for specific device model
	 * 
	 * @author Athira
	 */
	public static Boolean isSpecificDeviceledlogsAvailable(Dut device) {
		Boolean status = false;
		String model = null;
		try {
			model = BroadbandPropertyFileHandler.getAutomaticsPropsValueByResolvingPlatform(device,
					BroadBandPropertyKeyConstants.PROP_KEY_LEDLOGS_SPECIFICDEVICE);

			if (CommonMethods.isNotNull(model)) {
				status = true;
			}
		} catch (Exception e) {
			LOGGER.info("No device specific value found");
		}
		return status;
	}

	/**
	 * Utility method to get the PropKeyForLogUploadSettings from properties
	 * 
	 * @author yamini.s
	 */
	public static String getPropKeyForLogUploadSettings() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_FOR_LOG_UPLOAD_SETTINGS);
	}

	/**
	 * Utility method to get the ci xconf url for telemetry2.0 from properties
	 * 
	 * @author yamini.s
	 */
	public static String getCIXconfUrlForTelemetry2() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.CI_XCONF_URL_TELEMTRY_2);
	}

	/**
	 * Utility method to get the PropKeyForBasicTelemetryProfile from properties
	 * 
	 * @author yamini.s
	 */
	public static String getPropKeyForBasicTelemetryProfile() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_FOR_BASIC_TELEMETRY_PROFILE);
	}

	/**
	 * Utility method to get the PropKeyForTelemetryProfile from properties
	 * 
	 * @author yamini.s
	 */

	public static String getPropKeyForTelemetryProfile() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_FOR_TELEMETRY_PROFILE);
	}

	/**
	 * This API will get the invalid wifi password from the properties
	 * 
	 * @author Said Hisham
	 */
	public static String getInvalidWifiPassword() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.INVALID_WIFI_PASSWORD);
	}

	public static String getEncryptedFileLocationBasedOnDeviceModel(Dut device) {
		String fileLocation = BroadbandPropertyFileHandler.getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.PARTIAL_PROPERTY_KEY_FOR_FILE_LOCATION);
		return fileLocation;
	}

	/**
	 * This API will return whether the led logs available
	 * 
	 * @author Athira
	 */
	public static Boolean isDeviceSupported(Dut device) {
		Boolean status = false;
		String model = null;
		try {
			model = BroadbandPropertyFileHandler.getAutomaticsPropsValueByResolvingPlatform(device,
					BroadBandPropertyKeyConstants.PROP_KEY_SUPPORT_DEVICE);

			if (CommonMethods.isNotNull(model)) {
				status = true;
			}
		} catch (Exception e) {
			LOGGER.info("No device specific value found");
		}
		return status;
	}

	/**
	 * This API will check for device related to selfheal02
	 * 
	 * @author Rakesh C N
	 */
	public static boolean isDeviceCheckForSelfHeal2(Dut device) {
		return Boolean.parseBoolean(getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.PARTIAL_DEVICE_CHECK_SELF_HEAL_02));
	}

	/**
	 * This API will return whether the device support LimitBeacon
	 * 
	 * @author Athira
	 */
	public static Boolean isDeviceLimitBeacon(Dut device) {
		Boolean status = false;
		String model = null;
		try {
			model = BroadbandPropertyFileHandler.getAutomaticsPropsValueByResolvingPlatform(device,
					BroadBandPropertyKeyConstants.PROP_KEY_LIMITBEACON_DEVICE);

			if (CommonMethods.isNotNull(model)) {
				status = true;
			}
		} catch (Exception e) {
			LOGGER.info("No device specific value found");
		}
		return status;
	}

	/**
	 * This API will return partner values
	 * 
	 * @return Syndication partner values
	 * @author Athira
	 */
	public static String getSyndicationPartnerIDValues() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_SYNDICATION_PARTNER_ID_LIST);
	}

	/**
	 * This API will return partner values
	 * 
	 * @return Syndication partner values
	 * @author Athira
	 */
	public static String getSpecificSyndicationPartnerIDValues() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_SPECIFIC_SYNDICATION_PARTNER_ID_LIST);
	}

	/**
	 * This API will check for device related to upgradeStatusUsingSnmpCommand
	 * 
	 * @author Said Hisham
	 */
	public static boolean isDeviceCheckForUpgradeStatusUsingSnmpCommand(Dut device) {
		boolean status = false;
		status = Boolean.parseBoolean(getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.PARTIAL_DEVICE_CHECK_UPGRADE_STATUS_SNMP_COMMAND));
		return status;
	}

	/**
	 * This API will store the property of cert of rdk_manager snmpv3
	 * 
	 * @author yamini.s
	 */
	public static String getPropertyKeyForRDKManagerSNMPV3() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.FILE_SNMPV3_CERT_RDK_MANAGER);
	}

	/**
	 * This API will store the property of cert of rdkb_snmpd snmpv3
	 * 
	 * @author yamini.s
	 */
	public static String getPropertyKeyForRDKBSnmpdSNMPV3() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.FILE_SNMPV3_CERT_RDKB_SNMPD);
	}

	/**
	 * This API will get the corrupt image of same device class from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getCurruptImageForSameDeviceClass(Dut device) {
		return getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.MAP_MODEL_CORRUPT_IMAGE_SAME_DEVICE_CLASS);
	}

	/**
	 * This API will get the corrupt image Information of same device class from
	 * properties
	 * 
	 * @author Said Hisham
	 */
	public static String getCurruptImageForSameDeviceClassInfo(Dut device) {
		return getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.MAP_MODEL_CORRUPT_IMAGE_SAME_DEVICE_CLASS_INFO);
	}

	/**
	 * This API will get the corrupt image of different device class from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getCurruptImageForDifferentDeviceClass(Dut device) {
		return getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.MAP_MODEL_CORRUPT_IMAGE_DIFFERENT_DEVICE_CLASS);
	}

	/**
	 * This API will get the corrupt image Information of different device class
	 * from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getCurruptImageForDifferentDeviceClassInfo(Dut device) {
		return getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.MAP_MODEL_CORRUPT_IMAGE_DIFFERENT_DEVICE_CLASS_INFO);
	}

	/**
	 * This API will get the models with corrupt image of same device class from
	 * properties
	 * 
	 * @author Said Hisham
	 */
	public static String getCurruptImageWithPartialDownload(Dut device) {
		return getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.MAP_MODEL_CORRUPT_IMAGE_PARTIAL_DOWNLOAD);
	}

	/**
	 * This API will get the models with corrupt image info of same device class
	 * from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getCurruptImageWithPartialDownloadInfo(Dut device) {
		return getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.MAP_MODEL_CORRUPT_IMAGE_PARTIAL_DOWNLOAD_INFO);
	}

	/**
	 * This API will get the Xconf firmware location for corrupt images from
	 * properties
	 * 
	 * @author Said Hisham
	 */
	public static String getXconfFirmwareLocationForCorruptImages() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_XCONF_FIRMWARE_LOCATION_CORRUPT_IMAGES);
	}

	/**
	 * This API will check for specific device for negetive cdl tests
	 * 
	 * @author Said Hisham
	 */
	public static boolean isDeviceCheckForNegetiveCdl(Dut device) {
		boolean status = false;
		status = Boolean.parseBoolean(getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.PARTIAL_DEVICE_CHECK_NEGETIVE_CDL));
		return status;
	}

	/**
	 * This API will get the Security banner from properties
	 * 
	 * @author Govardhan
	 */
	public static String getSSHBanner() {
		return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.PROP_KEY_STB_SECURITY_BANNERS);
	}

	/**
	 * This API will give the 5GHZ Possible channels
	 * 
	 * @author Govardhan
	 */
	public static String getPossibleChannelList5GHZ(Dut device) {
		return getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.KEY_FOR_5GHZ_WIFI_POSSIBLE_CHANNELS);
	}

	/**
	 * This API will get the new STBRTL Url from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getNewStbRtlUrl() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.NEW_STBRTL_URL);
	}

	/**
	 * This API will get the old STBRTL Url from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getOldStbRtlUrl() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.OLD_STBRTL_URL);
	}

	/**
	 * This API will get the interface names
	 * 
	 * @author Said Hisham
	 */
	public static String getInterfaceNames(Dut device) {
		String status = getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.PROP_KEY_INTERFACE_NAMES);
		return status;
	}

	/**
	 * This API will get the interface values
	 * 
	 * @author Said Hisham
	 */
	public static String getInterfaceValues(Dut device) {
		String status = getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.PROP_KEY_INTERFACE_VALUES);
		return status;
	}

	/**
	 * This API will get the radio name for for specific device for 2.4ghz from
	 * properties
	 * 
	 * @author Said Hisham
	 */
	public static String getRadioName24BasedOnModel(Dut device) {
		return getAutomaticsPropsValueByResolvingPlatform(device, BroadBandPropertyKeyConstants.RADIO_NAME_24GHZ_CHECK);
	}

	/**
	 * This API will get the radio name for for specific device for 5ghz from
	 * properties
	 * 
	 * @author Said Hisham
	 */
	public static String getRadioName5BasedOnModel(Dut device) {
		return getAutomaticsPropsValueByResolvingPlatform(device, BroadBandPropertyKeyConstants.RADIO_NAME_5GHZ_CHECK);
	}

	/**
	 * This API will check the device type to return the expected value
	 * 
	 * @author yamini.s
	 */
	public static boolean isDeviceCheckToReturnExpectedValue1(Dut device) {
		return Boolean.parseBoolean(
				getAutomaticsPropsValueByResolvingPlatform(device, BroadBandPropertyKeyConstants.DEVICE_CHECK_VALUE1));
	}

	/**
	 * This API will check the device type to return the expected value
	 * 
	 * @author yamini.s
	 */
	public static boolean isDeviceCheckToReturnExpectedValue2(Dut device) {
		return Boolean.parseBoolean(
				getAutomaticsPropsValueByResolvingPlatform(device, BroadBandPropertyKeyConstants.DEVICE_CHECK_VALUE2));
	}

	/**
	 * This API will check the device type to return the expected value
	 * 
	 * @author yamini.s
	 */
	public static boolean isDeviceCheckToReturnExpectedValue3(Dut device) {
		return Boolean.parseBoolean(
				getAutomaticsPropsValueByResolvingPlatform(device, BroadBandPropertyKeyConstants.DEVICE_CHECK_VALUE3));
	}

	/**
	 * This API will get the value for NTPD client
	 * 
	 * @author yamini.s
	 */
	public static String getPropertyKeyForNTPDClient1() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROCESS_NTPD_CLIENT1);

	}

	/**
	 * This API will get the value for NTPD client
	 * 
	 * @author yamini.s
	 */
	public static String getPropertyKeyForNTPDClient2() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROCESS_NTPD_CLIENT2);
	}

	/**
	 * This API will check for specific device for acceptance criteria
	 * 
	 * @author yamini.s
	 */
	public static boolean getStatusForPartialDeviceCheckX(Dut device) {
		return Boolean.parseBoolean(getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.PARTIAL_DEVICE_CHECK_ACCEPTANCE_CRITERIA));
	}

	/**
	 * This API will check for specific device for acceptance criteria
	 * 
	 * @author yamini.s
	 */
	public static boolean getStatusForPartialDeviceCheck(Dut device) {
		return Boolean.parseBoolean(getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.PARTIAL_DEVICE_CHECK_ACCEPTANCE));
	}

	/**
	 * This API will get the value for level one block site address
	 * 
	 * @author yamini.s
	 */
	public static String getPropertyKeyForlevelOneBlockAddress() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_DNS_BLOCK_ADDRESS_FOR_LEVEL_ONE_SITE);
	}

	/**
	 * This API will get the value for level one site host address
	 * 
	 * @author yamini.s
	 */
	public static String getPropertyKeyForlevelOneSiteHostAddress() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_HOST_ADDRESS_FOR_LEVEL_ONE_SITE);
	}

	/**
	 * This API will check for device related to TxRxRate
	 * 
	 * @author Govardhan
	 */
	public static boolean isTxRxRateListDevices(Dut device) {
		boolean isTxRxDevice = false;
		String deviceModel = device.getModel();
		if (CommonMethods.isNotNull(deviceModel)) {
			try {
				String[] txRxRateListDevices = AutomaticsPropertyUtility
						.getProperty(BroadBandPropertyKeyConstants.PROP_KEY_TXRX_RATELIST_DEVICE).split(",");
				for (String model : txRxRateListDevices) {
					if (model.equalsIgnoreCase(deviceModel)) {
						isTxRxDevice = true;
						break;
					}
				}
			} catch (Exception e) {

			}
		}
		return isTxRxDevice;
	}

	/**
	 * This API will get the global DNS IPv4 value from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getGlobalDNSIpv4Value() {
		return AutomaticsPropertyUtility
				.getProperty(BroadBandPropertyKeyConstants.STRING_DEFAULT_GLOBAL_DNS_IPV4_VALUE);
	}

	/**
	 * This API will get the global DNS IPv6 value from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getGlobalDNSIpv6Value() {
		return AutomaticsPropertyUtility
				.getProperty(BroadBandPropertyKeyConstants.STRING_DEFAULT_GLOBAL_DNS_IPV6_VALUE);
	}

	/**
	 * This API will get the DNS IPv4 value for DNS level one primary from
	 * properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDNSIpv4ValueLevelOnePrimary() {
		return AutomaticsPropertyUtility
				.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_IPV4_VALUE_FOR_DNS_LEVEL_ONE_PRIMARY);
	}

	/**
	 * This API will get the DNS IPv4 value for DNS level two primary from
	 * properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDNSIpv4ValueLevelTwoPrimary() {
		return AutomaticsPropertyUtility
				.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_IPV4_VALUE_FOR_DNS_LEVEL_TWO_PRIMARY);
	}

	/**
	 * This API will get the DNS IPv4 value for DNS level three primary from
	 * properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDNSIpv4ValueLevelThreePrimary() {
		return AutomaticsPropertyUtility
				.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_IPV4_VALUE_FOR_DNS_LEVEL_THREE_PRIMARY);
	}

	/**
	 * This API will get the DNS IPv6 value for DNS level one primary from
	 * properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDNSIpv6ValueLevelOnePrimary() {
		return AutomaticsPropertyUtility
				.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_IPV6_VALUE_FOR_DNS_LEVEL_ONE_PRIMARY);
	}

	/**
	 * This API will get the DNS IPv6 value for DNS level two primary from
	 * properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDNSIpv6ValueLevelTwoPrimary() {
		return AutomaticsPropertyUtility
				.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_IPV6_VALUE_FOR_DNS_LEVEL_TWO_PRIMARY);
	}

	/**
	 * This API will get the DNS IPv6 value for DNS level three primary from
	 * properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDNSIpv6ValueLevelThreePrimary() {
		return AutomaticsPropertyUtility
				.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_IPV6_VALUE_FOR_DNS_LEVEL_THREE_PRIMARY);
	}

	/**
	 * This API will get the DNS valid IPv4 value from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDNSValidIpv4Value() {
		return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_VALID_IPV4_VALUE);
	}

	/**
	 * This API will get the DNS valid IPv6 value from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDNSValidIpv6Value() {
		return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_VALID_IPV6_VALUE);
	}

	/**
	 * This API will get the DNS invalid secondary IPv4 value from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDNSInvalidSecondaryIpv4Value() {
		return AutomaticsPropertyUtility
				.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_INVALID_SECONDARY_IPV4_VALUE);
	}

	/**
	 * This API will get the DNS invalid secondary IPv6 value from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDNSInvalidSecondaryIpv6Value() {
		return AutomaticsPropertyUtility
				.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_INVALID_SECONDARY_IPV6_VALUE);
	}

	/**
	 * This API will get the DNS invalid primary IPv4 value from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDNSInvalidPrimaryIpv4Value() {
		return AutomaticsPropertyUtility
				.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_INVALID_PRIMARY_IPV4_VALUE);
	}

	/**
	 * This API will get the DNS invalid primary IPv6 value from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDNSInvalidPrimaryIpv6Value() {
		return AutomaticsPropertyUtility
				.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_INVALID_PRIMARY_IPV6_VALUE);
	}

	/**
	 * This API will get the DNS valid secondary IPv4 value from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDNSValidSecondaryIpv4Value() {
		return AutomaticsPropertyUtility
				.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_VALID_SECONDARY_IPV4_VALUE);
	}

	/**
	 * This API will get the DNS valid secondary IPv6 value from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDNSValidSecondaryIpv6Value() {
		return AutomaticsPropertyUtility
				.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_VALID_SECONDARY_IPV6_VALUE);
	}

	/**
	 * This API will get the DNS another invalid secondary IPv4 value from
	 * properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDNSAnotherInvalidSecondaryIpv4Value() {
		return AutomaticsPropertyUtility
				.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_ANOTHER_INVALID_SECONDARY_IPV4_VALUE);
	}

	/**
	 * This API will get the DNS another invalid secondary IPv6 value from
	 * properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDNSAnotherInvalidSecondaryIpv6Value() {
		return AutomaticsPropertyUtility
				.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_ANOTHER_INVALID_SECONDARY_IPV6_VALUE);

	}

	/**
	 * This API will check the device type to return the expected value
	 * 
	 * @author yamini.s
	 */
	public static boolean isDeviceCheckForXDNS(Dut device) {
		return Boolean.parseBoolean(getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.DEVICE_CHECK_VALUE_XDNS));
	}

	/**
	 * This API will get the current upnp version from properties
	 * 
	 * @author yamini.s
	 */
	public static String getValueForCurrentUPNPVersion(Dut device) {
		return getAutomaticsPropsValueByResolvingPlatform(device, BroadBandPropertyKeyConstants.CURRENT_UPNP_VERSION);
	}

	/**
	 * This API will check for device related to GBPAD
	 * 
	 * @author yamini.s
	 */
	public static boolean isDeviceCheckForGBPAD(Dut device) {
		return Boolean.parseBoolean(getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.PARTIAL_DEVICE_CHECK_GBPAD));
	}

	/**
	 * This API will check for device related to GBPAD
	 * 
	 * @author yamini.s
	 */
	public static boolean isDeviceCheckForGBPAD1(Dut device) {
		return Boolean.parseBoolean(getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.PARTIAL_DEVICE_CHECK_GBPAD1));
	}

	/**
	 * This API will check for device related to GBPAD
	 * 
	 * @author yamini.s
	 */
	public static boolean isDeviceCheckForGBPAD2(Dut device) {
		return Boolean.parseBoolean(getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.PARTIAL_DEVICE_CHECK_GBPAD2));
	}

	/**
	 * This API will check if the server details are configured to upload crash
	 * details
	 * 
	 * @author yamini.s
	 */
	public static Boolean isServerConfiguredToUploadCrashDetails() {
		return Boolean.parseBoolean(AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.IS_SERVER_CONFIGURED_TO_UPLOAD_CRASH_DETAILS));
	}

	/**
	 * This API will get the proxy xconf for rfc update settings url
	 * 
	 * @author Sruthi Santhosh
	 */
	public static String getProxyXconfRfcUrl() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandTestConstants.PROP_KEY_PROXY_XCONF_RFC_URL);
	}

	/**
	 * Method to get page title for captive Portal/login GUI for Syndicate partner
	 * devices
	 * 
	 * @author Govardhan
	 */
	public static String getCaptivePageTitleForSyndicatePartners(String partner) {
		String captivePortalTitle = null;
		try {
			captivePortalTitle = AutomaticsPropertyUtility
					.getProperty(BroadBandPropertyKeyConstants.CAPTIVE_PORTAL_PAGE_TITLE_FOR_PARTNER_DEVICE + partner);
		} catch (Exception e) {
			LOGGER.info("Not able to find the Page Title in Properties for the Partner : " + partner);
		}
		return captivePortalTitle;
	}

	/**
	 * Method to get Captive Portal Header Confirm Message based on partner
	 * 
	 * @author Govardhan
	 */
	public static String getCaptivePortalConfirmHeaderMessageForPartner(String partner) {
		String captivePortalConfirmHeaderMessage = null;
		try {
			captivePortalConfirmHeaderMessage = AutomaticsPropertyUtility.getProperty(
					BroadBandPropertyKeyConstants.CAPTIVE_PORTAL_CONFIGURATION_CONFIRM_PAGE_HEADER_MESSAGE + partner);
		} catch (Exception e) {
			LOGGER.info("Not able to find the Captive Portal Header Confirm Message in Properties for the Partner : "
					+ partner);
		}
		return captivePortalConfirmHeaderMessage;
	}

	/**
	 * Method to get Captive Portal Header Success Message based on partner
	 * 
	 * @author Govardhan
	 */
	public static String getCaptivePortalSuccessHeaderMessageForPartner(String partner) {
		String captivePortalSuccessHeaderMessage = null;
		try {
			captivePortalSuccessHeaderMessage = AutomaticsPropertyUtility.getProperty(
					BroadBandPropertyKeyConstants.CAPTIVE_PORTAL_CONFIGURATION_SUCCESS_PAGE_HEADER_MESSAGE + partner);
		} catch (Exception e) {
			LOGGER.info("Not able to find the Page Title in Properties for the Partner : " + partner);
		}
		return captivePortalSuccessHeaderMessage;
	}

	/**
	 * This API will get the naming conventions for TR69 parameters from properties
	 * 
	 * @author Govardhan
	 */
	public static String getNamingConventionForTR69() {
		return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.TR69_NAMING_CONVENTION_PARAMETERS);
	}

	/**
	 * This API will get the CDL URL for devices which are both business and Atom
	 * console from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getCodeDownloadUrlForBusinessAndAtom() {
		String operatingStandards = null;
		try {
			operatingStandards = AutomaticsTapApi
					.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_CDL_URL_BUSINESS_ATOM);
		} catch (Exception e) {

		}
		return operatingStandards;
	}

	/**
	 * This API will check if the server details are configured to upload logs to
	 * server
	 * 
	 * @author yamini.s
	 */
	public static Boolean isServerConfiguredToUploadToServer() {
		return Boolean.parseBoolean(AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.IS_SERVER_CONFIGURED_TO_UPLOAD_TO_SERVER));
	}

	/**
	 * This method will return the default partner ID configured
	 * 
	 * @return default partner ID
	 */
	public static String getDefaultPartnerID() {

		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROPERTY_DEFAULT_PARTNER);
	}

	/**
	 * This method will get the platform based partner List configured in external
	 * props
	 * 
	 * @param device
	 * @return
	 */
	public static List<String> getPartnerListByResolvingPlatform(Dut device) {
		List<String> partnerList = new ArrayList<String>();

		String partnerListAsString = getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.PROPERTY_PLATFORM_BASED_PARTNER_LIST);
		if (CommonMethods.isNotNull(partnerListAsString)) {
			partnerList = Arrays.asList(partnerListAsString.split(","));
		}
		return partnerList;
	}

	/**
	 * This API will get the home network password with special character
	 * 
	 * @author Govardhan
	 */
	public static String getTheHomeNetworkPasswordWithSpecialCharacter() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PASSWORD_WITH_SPECIAL_CHARACTER);
	}

	/**
	 * This API will get the home network password with space
	 * 
	 * @author Govardhan
	 */
	public static String getTheHomeNetworkPasswordWithSpace() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PASSWORD_WITH_SPACE);
	}

	/**
	 * This API will get the Admin Login Page Title of the LAN GUI page of the
	 * gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getAdminLoginPageTitle() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.ADMIN_LOGIN_PAGE_TITLE);
	}

	/**
	 * This API will get the At a Glance Page Title of the LAN GUI Page of the
	 * gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getAtAGlancePageTitle() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_AT_A_GLANCE);
	}

	/**
	 * This API will get the Page Url for the Hardware Wizard Page of the gateway
	 * device from properties
	 * 
	 * @author Govardhan
	 */
	public static String getPageUrlForHardwareWizardPage() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PAGE_URL_HARDWARE_WIZARD);
	}

	/**
	 * This API will get the Page title for the Hardware Wizard Page of the gateway
	 * device from properties
	 * 
	 * @author Govardhan
	 */
	public static String getPageTitleForHardwareWizardPage() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PAGE_TITLE_HARDWARE_WIZARD);
	}

	/**
	 * This API will get the Link text for the Hardware Wizard Page of the gateway
	 * device from properties
	 * 
	 * @author Govardhan
	 */
	public static String getPageLinkTextForHardwareWizardPage() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_LINK_TEXT_HARDWARE_WIZARD);
	}

	/**
	 * This API will get the home network password with proper standards
	 * 
	 * @author Govardhan
	 */
	public static String getTheHomeNetworkPasswordWithProperStandards() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PASSWORD_MISMATCH);
	}

	/**
	 * This API will get the Page Title of Connected Device page of the gateway
	 * device from properties
	 * 
	 * @author Athira
	 */
	public static String getPageTitleConnectedDevice() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_CONNECTED_DEVICE);
	}

	/**
	 * This API will get the Page Title of Connection WiFi page of the gateway
	 * device from properties
	 * 
	 * @author Athira
	 */
	public static String getPageTitleConnectionWiFi() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_CONNECTION_WIFI);
	}

	/**
	 * This API will get the property Constant to store wifi edit 5 GHz page title
	 * 
	 * @author Govardhan
	 */
	public static String getWiFiConfigurationPageTitle() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PAGE_TITLE_WIFI_CONFIG_PAGE);
	}

	/**
	 * This API will get the Page title for the Change Password and Title for LAN
	 * GUI Page
	 * 
	 * @author Rakesh C N
	 */
	public static String getPageTitleForPasswordchange() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.CHANGE_PASSWORDPAGE_TITLE);
	}

	/**
	 * This API will get the Parental Control > Managed Sites Page Title of the LAN
	 * GUI Page of the gateway device from properties
	 * 
	 * @author Rakesh C N
	 */
	public static String getParentalControlManagedSitesPageTitle() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_PARENTAL_CONTROL_MANAGED_SITES);
	}

	/**
	 * This API will get the page Title for the ParentalControl > Managed Sites >
	 * Add Blocked Domain of the LAN GUI Page of the gateway device from properties
	 * 
	 * @author Rakesh C N
	 */
	public static String getParentalControlAddBlockedDomain() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_PARENTAL_CONTROL_ADD_BLOCKED_DOMAIN);
	}

	/**
	 * This API will get the page Title for the Parental Control > Managed Sites >
	 * Add Blocked Keyword of the LAN GUI Page of the gateway device from properties
	 * 
	 * @author Rakesh C N
	 */
	public static String getParentalControlAddBlockedKeyword() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_PARENTAL_CONTROL_ADD_BLOCKED_KEYWORD);
	}

	/**
	 * This API will get the page Title for the Parental Control > Managed Services
	 * of the LAN GUI Page of the gateway device from properties
	 * 
	 * @author Rakesh C N
	 */
	public static String getParentalControlManagedServicePageTitle() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_PARENTAL_CONTROL_MANAGED_SERVICE);
	}

	/**
	 * This API will get the page Title for the Parental Control > Managed Services
	 * > Add Blocked Service Add Blocked Domain of the LAN GUI Page of the gateway
	 * device from properties
	 * 
	 * @author Rakesh C N
	 */
	public static String getParentalControlAddBlockedService() {
		return AutomaticsTapApi.getSTBPropsValue(
				BroadBandPropertyKeyConstants.PAGE_TITLE_PARENTAL_CONTROL_MANAGED_SERVICES_ADD_BLOCKED_SERVICE);
	}

	/**
	 * This API will get the Page title for the Connected Devices page of the LAN
	 * GUI Page of the gateway device from properties
	 * 
	 * @author Sruthi Santhosh
	 */
	public static String getPageTitleForConnectedDevicesPage() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PAGE_TITLE_CONNECTED_DEVICE_PAGE);
	}

	/**
	 * This API will get the Link Text for the Connected Devices page of the LAN GUI
	 * Page of the gateway device from properties
	 * 
	 * @author Sruthi Santhosh
	 */
	public static String getLinkTextForConnectedDevicesPage() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_LINK_TEXT_CONNECTED_DEVICE_PAGE);
	}

	/**
	 * This API will get the Page url for the Connected Devices page of the LAN GUI
	 * Page of the gateway device from properties
	 * 
	 * @author Sruthi Santhosh
	 */
	public static String getPageUrlForConnectedDevicesPage() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PAGE_URL_CONNECTED_DEVICE_PAGE);
	}

	/**
	 * This API will get the Page title for the Connected Device Edit Device page of
	 * the LAN GUI Page of the gateway device from properties
	 * 
	 * @author Sruthi Santhosh
	 */
	public static String getPageTitleForConnectedDevicesEditDevicePage() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PAGE_TITLE_CONNECTED_DEVICE_EDIT_DEVICE_PAGE);
	}

	/**
	 * This API will get the Link text for the LocalIPNetwork of the LAN GUI Page of
	 * the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getLinkTextForLocalIpNetwork() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_LOCAL_IP_NETWORK);
	}

	/**
	 * This API will get the page URL for the LocalIPNetwork of the LAN GUI Page of
	 * the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForLocalIpNetwork() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_LOCAL_IP_NETWORK);
	}

	/**
	 * This API will get the page Title for the LocalIPNetwork of the LAN GUI Page
	 * of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageTitleForLocalIpNetwork() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_LOCAL_IP_NETWORK);
	}

	/**
	 * This API will get the Link text for the Partner Network of the LAN GUI Page
	 * of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getLinkTextForPartnerNetwork() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_PARTNER_NETWORK);
	}

	/**
	 * This API will get the Page URL for the Partner Network of the LAN GUI Page of
	 * the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForPartnerNetwork() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_PARTNER_NETWORK);
	}

	/**
	 * This API will get the Page Title for the Partner Network of the LAN GUI Page
	 * of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageTitleForPartnerNetwork() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_PARTNER_NETWORK);
	}

	/**
	 * This API will get the FireWall IPV4 Page Title of the LAN GUI Page of the
	 * gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getFireWallIpv4PageTitle() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_FIREWALL_IPV4);
	}

	/**
	 * This API will get the Parental Control > Managed Sites Page Title of the LAN
	 * GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getParentalControlPageTitle() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_PARENTAL_CONTROL_MANAGED_SITES);
	}

	/**
	 * This API will get the Managed Devices Page Title of the LAN GUI Page of the
	 * gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getManagedDevicesPageTitle() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_MANAGED_DEVICES);
	}

	/**
	 * This API will get the FireWall IPV6 Page Title of the LAN GUI Page of the
	 * gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getFireWallIpv6PageTitle() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_FIREWALL_IPV6);
	}

	/**
	 * This API will get the Page Url for the Connection Status of the LAN GUI Page
	 * of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForConnectionStatus() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_CONNECTION_STATUS);
	}

	/**
	 * This API will get the Page title for the Connection Status of the LAN GUI
	 * Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageTitleForConnectionStatus() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_CONNECTION_STATUS);
	}

	/**
	 * This API will get the Link text for the Connection Status of the LAN GUI Page
	 * of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getLinkTextForConnectionStatus() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_CONNECTION_STATUS);
	}

	/**
	 * This API will get the Page Url for the Connection Status of the Reset Restore
	 * Gateway Page of the gateway device from properties
	 * 
	 * @author Athira
	 */
	public static String getPageUrlForResetRestoreGateway() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_RESET_RESTORE_GATEWAY);
	}

	/**
	 * This API will get the Page title for the Connection Status of the Reset
	 * Restore Gateway Page of the gateway device from properties
	 * 
	 * @author Athira
	 */
	public static String getPageTitleForResetRestoreGateway() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_RESET_RESTORE_GATEWAY);
	}

	/**
	 * This API will get the Link text for the Connection Status of the Reset
	 * Restore Gateway Page of the gateway device from properties
	 * 
	 * @author Athira
	 */
	public static String getLinkTextForResetRestoreGateway() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_RESET_RESTORE_GATEWAY);
	}

	/**
	 * This API will get the Page title for the Network Diagnostic Tools page of the
	 * LAN GUI Page of the gateway device from properties
	 * 
	 * @author Sruthi Santhosh
	 */
	public static String getPageTitleForNwDiagToolsPage() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PAGE_TITLE_NW_DIAG_PAGE);
	}

	/**
	 * This API will get the Link Text for the Network Diagnostic Tools page of the
	 * LAN GUI Page of the gateway device from properties
	 * 
	 * @author Sruthi Santhosh
	 */
	public static String getLinkTextForNwDiagToolsPage() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_LINK_TEXT_NW_DIAG_PAGE);
	}

	/**
	 * This API will get the Page url for the Network Diagnostic Tools of the LAN
	 * GUI Page of the gateway device from properties
	 * 
	 * @author Sruthi Santhosh
	 */
	public static String getPageUrlForNwDiagToolsPage() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PAGE_URL_NW_DIAG_PAGE);
	}

	/**
	 * This API will check if the devices is applicable for wifi driver commands
	 * from properties
	 * 
	 * @author Said Hisham
	 */
	public static boolean isWifiDriverCommandsApplicableForDevices(Dut device) {
		return Boolean.parseBoolean(getAutomaticsPropsValueByResolvingPlatform(device,
				BroadBandPropertyKeyConstants.DEVICE_CHECK_WIFI_DRIVER_COMMANDS));
	}

	/**
	 * This API will get the list of devices and corresponding interface values for
	 * Acs Status retrieval for 5ghz
	 * 
	 * @author Said Hisham
	 */
	public static String getListOfCommandsForDevicesForAcsStatusUsingSystemCommands5ghz(Dut device) {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.COMMANDS_TO_GET_INTERFACE_VALUES_FOR_DEVICES_5GHZ);
	}

	/**
	 * This API will get the list of devices and corresponding interface values for
	 * Acs Status retrieval for 2.4ghz
	 * 
	 * @author Said Hisham
	 */
	public static String getListOfCommandsForDevicesForAcsStatusUsingSystemCommands(Dut device) {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.COMMANDS_TO_GET_INTERFACE_VALUES_FOR_DEVICES);
	}

	/**
	 * This API will get the list of expected response for devices for Acs Status
	 * retrieval commands
	 * 
	 * @author Said Hisham
	 */
	public static String getListOfExpectedResponseForDevicesForAcsStatusUsingSystemCommands(Dut device) {
		return AutomaticsTapApi.getSTBPropsValue(
				BroadBandPropertyKeyConstants.RESPONSE_OF_COMMANDS_TO_GET_INTERFACE_VALUES_FOR_DEVICES);
	}

	/**
	 * This API will get the Page Url for the MoCA of the LAN GUI Page of the
	 * gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForMoCA() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_MoCA);
	}

	/**
	 * This API will get the Page title for the MoCA of the LAN GUI Page of the
	 * gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageTitleForMoCA() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_MoCA);
	}

	/**
	 * This API will get the Link text for the MoCA of the LAN GUI Page of the
	 * gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getLinkTextForMoCA() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_MoCA);
	}

	/**
	 * This API will get the Page Url for the Software Page of the LAN GUI Page of
	 * the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForSoftware() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_SOFTWARE);
	}

	/**
	 * This API will get the Page title for the Software Page of the LAN GUI Page of
	 * the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageTitleForSoftware() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_SOFTWARE);
	}

	/**
	 * This API will get the Link text for the Software Page of the LAN GUI Page of
	 * the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getLinkTextForSoftware() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_SOFTWARE);
	}

	/**
	 * This API will get the Page Url for the Hardware Page of the LAN GUI Page of
	 * the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForHardware() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_HARDWARE);
	}

	/**
	 * This API will get the Link text for the Hardware Page of the LAN GUI Page of
	 * the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getLinkTextForHardware() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_HARDWARE);
	}

	/**
	 * This API will get the Page Url for the System Hardware Page of the LAN GUI
	 * Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForSystemHardware() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_SYSTEM_HARDWARE);
	}

	/**
	 * This API will get the Page title for the System Hardware Page of the LAN GUI
	 * Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageTitleForSystemHardware() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_SYSTEM_HARDWARE);
	}

	/**
	 * This API will get the Link text for the System Hardware Page of the LAN GUI
	 * Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getLinkTextForSystemHardware() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_SYSTEM_HARDWARE);
	}

	/**
	 * This API will get the Page Url for the Hardware Lan Page of the LAN GUI Page
	 * of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForHardwareLan() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_HARDWARE_LAN);
	}

	/**
	 * This API will get the Page title for the Hardware Lan Page of the LAN GUI
	 * Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageTitleForHardwareLan() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_HARDWARE_LAN);
	}

	/**
	 * This API will get the Link text for the Hardware Lan Page of the LAN GUI Page
	 * of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getLinkTextForHardwareLan() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_HARDWARE_LAN);
	}

	/**
	 * This API will get the Page Url for the Hardware Wireless Page of the LAN GUI
	 * Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForHardwareWireless() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_HARDWARE_WIRELESS);
	}

	/**
	 * This API will get the Page title for the Hardware Wireless Page of the LAN
	 * GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageTitleForHardwareWireless() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_HARDWARE_WIRELESS);
	}

	/**
	 * This API will get the Link text for the Hardware Wireless of the LAN GUI Page
	 * of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getLinkTextForHardwareWireless() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_HARDWARE_WIRELESS);
	}

	/**
	 * This API will get the Page Url for the Managed Services Page of the LAN GUI
	 * Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForManagedServices() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_MANAGED_SERVICES);
	}

	/**
	 * This API will get the Page title for the ManManaged Services Page of the LAN
	 * GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageTitleForManagedServices() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_MANAGED_SERVICES);
	}

	/**
	 * This API will get the Page Url for the Reports Page of the LAN GUI Page of
	 * the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForReports() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_REPORTS);
	}

	/**
	 * This API will get the Page title for the Reports Page of the LAN GUI Page of
	 * the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageTitleForReports() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_REPORTS);
	}

	/**
	 * This API will get the Link text for the Reports of the LAN GUI Page of the
	 * gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getLinkTextForReports() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_REPORTS);
	}

	/**
	 * This API will get the Page Url for the Advanced Page of the LAN GUI Page of
	 * the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForAdvanced() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_ADVANCED);
	}

	/**
	 * This API will get the Page title for the Advanced Page of the LAN GUI Page of
	 * the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageTitleForAdvanced() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_ADVANCED);
	}

	/**
	 * This API will get the Link text for the Advanced of the LAN GUI Page of the
	 * gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getLinkTextForAdvanced() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_ADVANCED);
	}

	/**
	 * This API will get the Page Url for the Advanced Remote Management Page of the
	 * LAN GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForAdvancedRemoteMgmt() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_ADVANCED_REMOTE_MGMT);
	}

	/**
	 * This API will get the Page title for the Advanced Remote Management Page of
	 * the LAN GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageTitleForAdvancedRemoteMgmt() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_ADVANCED_REMOTE_MGMT);
	}

	/**
	 * This API will get the Link text for the Advanced Remote Management of the LAN
	 * GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getLinkTextForAdvancedRemoteMgmt() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_ADVANCED_REMOTE_MGMT);
	}

	/**
	 * This API will get the Page Url for the Advanced DMZ Page of the LAN GUI Page
	 * of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForAdvancedDmz() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_ADVANCED_DMZ);
	}

	/**
	 * This API will get the Page title for the Advanced DMZ Page of the LAN GUI
	 * Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageTitleForAdvancedDmz() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_ADVANCED_DMZ);
	}

	/**
	 * This API will get the Link text for the Advanced DMZ of the LAN GUI Page of
	 * the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getLinkTextForAdvancedDmz() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_ADVANCED_DMZ);
	}

	/**
	 * This API will get the Page Url for the Advanced Device Discover Page of the
	 * LAN GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForAdvancedDeviceDiscover() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_ADVANCED_DEVICE_DISCOVER);
	}

	/**
	 * This API will get the Page title for the Advanced Device Discover Page of the
	 * LAN GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageTitleForAdvancedDeviceDiscover() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_ADVANCED_DEVICE_DISCOVER);
	}

	/**
	 * This API will get the Link text for the Advanced Device Discover of the LAN
	 * GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getLinkTextForAdvancedDeviceDiscover() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_ADVANCED_DEVICE_DISCOVER);
	}

	/**
	 * This API will get the Page Url for the Troubleshooting Logs Page of the LAN
	 * GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForTroubleShootingLogs() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_TROUBLESHOOTING_LOGS);
	}

	/**
	 * This API will get the Page title for the Troubleshooting Logs Page of the LAN
	 * GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageTitleForTroubleShootingLogs() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_TROUBLESHOOTING_LOGS);
	}

	/**
	 * This API will get the Link text for the Troubleshooting Logs of the LAN GUI
	 * Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getLinkTextForTroubleShootingLogs() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_TROUBLESHOOTING_LOGS);
	}

	/**
	 * This API will get the Page Url for the Troubleshooting Wifi Spectrum Analyzer
	 * Page of the LAN GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForTroubleShootingWifiSpectrumAnalyzer() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_TROUBLESHOOTING_WIFI_SPECTRUM_ANALYZER);
	}

	/**
	 * This API will get the Page title for the Troubleshooting Wifi Spectrum
	 * Analyzer Page of the LAN GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageTitleForTroubleShootingWifiSpectrumAnalyzer() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_TROUBLESHOOTING_WIFI_SPECTRUM_ANALYZER);
	}

	/**
	 * This API will get the Link text for the Troubleshooting Wifi Spectrum
	 * Analyzer of the LAN GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getLinkTextForTroubleShootingWifiSpectrumAnalyzer() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_TROUBLESHOOTING_WIFI_SPECTRUM_ANALYZER);
	}

	/**
	 * This API will get the Page Url for the Troubleshooting MoCA Diagnostics Page
	 * of the LAN GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForTroubleShootingMoCADiagnostics() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_TROUBLESHOOTING_MOCA_DIAGNOSTICS);
	}

	/**
	 * This API will get the Page title for the Troubleshooting MoCA Diagnostics
	 * Page of the LAN GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageTitleForTroubleShootingMoCADiagnostics() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_TROUBLESHOOTING_MOCA_DIAGNOSTICS);
	}

	/**
	 * This API will get the Link text for the Troubleshooting MoCA Diagnostics of
	 * the LAN GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getLinkTextForTroubleShootingMoCADiagnostics() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_TROUBLESHOOTING_MOCA_DIAGNOSTICS);
	}

	/**
	 * This API will get the Page Url for the Troubleshooting Change Password Page
	 * of the LAN GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForTroubleShootingChangePwd() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_TROUBLESHOOTING_CHANGE_PWD);
	}

	/**
	 * This API will get the Page title for the Troubleshooting Change Password Page
	 * of the LAN GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageTitleForTroubleShootingChangePwd() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_TROUBLESHOOTING_CHANGE_PWD);
	}

	/**
	 * This API will get the Link text for the Troubleshooting Change Password Page
	 * of the LAN GUI Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getLinkTextForTroubleShootingChangePwd() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_TROUBLESHOOTING_CHANGE_PWD);
	}

	/**
	 * This API will get the Page Url for the ManagedSites Page of the LAN GUI Page
	 * of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageUrlForManagedSites() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_URL_MANAGED_SITES);
	}

	/**
	 * This API will get the Page title for the ManagedSites Page of the LAN GUI
	 * Page of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getPageTitleForManagedSites() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_MANAGED_SITES);
	}

	/**
	 * This API will get the Link text for the ManagedSites Page of the LAN GUI Page
	 * of the gateway device from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getLinkTextForManagedSites() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_MANAGED_SITES);
	}

	/**
	 * This API will get the Link text for the Connection Status of the Managed
	 * Services Page of the gateway device from properties
	 * 
	 * @author Rakesh C N
	 */
	public static String getLinkTextForManagedServices() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.LINK_TEXT_MANAGED_SERVICES);
	}

	/**
	 * This API will get the Parental Control > Managed Devices Page Title of the
	 * LAN GUI Page of the gateway device from properties
	 * 
	 * @author Rakesh C N
	 */
	public static String getParentalControlManagedDevicesPageTitle() {
		return AutomaticsTapApi
				.getSTBPropsValue(BroadBandPropertyKeyConstants.PAGE_TITLE_PARENTAL_CONTROL_MANAGED_DEVICES);
	}

	/**
	 * This API will get the Link Text for the Port Triggering Add page of the LAN
	 * GUI Page of the gateway device from properties
	 * 
	 * @author Sruthi Santhosh
	 */
	public static String getLinkTextForPortTriggering() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_LINK_TEXT_PORT_TRIGGERING);
	}

	/**
	 * This API will get the Page url for the Port Triggering Add page of the LAN
	 * GUI Page of the gateway device from properties
	 * 
	 * @author Sruthi Santhosh
	 */
	public static String getPageUrlForPortTriggering() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PAGE_URL_PORT_TRIGGERING);
	}

	/**
	 * This API will get the Page title for the Port Triggering page of the LAN GUI
	 * Page of the gateway device from properties
	 * 
	 * @author Sruthi Santhosh
	 */
	public static String getPageTitleForPortTriggering() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_PAGE_TITLE_PORT_TRIGGERING);
	}

	/**
	 * Utility method to get InvalidParodusTokenServerURL
	 * 
	 * @return InvalidParodusTokenServerURL
	 * @author Govardhan
	 */
	public static String getInvalidParodusTokenServerURL() {
		String operatingStandards = null;
		try {
			operatingStandards = AutomaticsTapApi
					.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_INVALID_PARODUS_TOKEN_SERVER_URL);
		} catch (Exception e) {

		}
		return operatingStandards;
	}

	/**
	 * This API will return the Binary path of selenium driver
	 * 
	 * @return Binary path of selenium driver
	 */
	public static String getBinaryPathOfSelenium() {
		return AutomaticsTapApi.getSTBPropsValue("selenium.webdriver.path");
	}

	/**
	 * This API will get the build information based on device from properties
	 * 
	 * @author yamini.s
	 */
	public static String getDeviceSpecificBuild1() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.BUILD_EXTENSION_1);
	}

	/**
	 * This API will get the build information based on device from properties
	 * 
	 * @author yamini.s
	 */
	public static String getDeviceSpecificBuild2() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.BUILD_EXTENSION_2);
	}

	/**
	 * This API will get the build information based on device from properties
	 * 
	 * @author yamini.s
	 */
	public static String getDeviceSpecificBuild3() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.BUILD_EXTENSION_3);
	}

	/**
	 * This API will get the build information based on device from properties
	 * 
	 * @author yamini.s
	 */
	public static String getDeviceSpecificBuild4() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.BUILD_EXTENSION_4);
	}

	/**
	 * This API will get the certificate issuer name from properties
	 * 
	 * @author yamini.s
	 */
	public static String getCertificateIssuerName() {
		return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.VALID_CERT_ISSUER_NAME);
	}

	/**
	 * This API will get the telemetry upload url from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getTelemetryUploadUrl() {
		try {
			return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.TELEMETRY_UPLOAD_URL);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			return null;
		}
	}

	/**
	 * This API will get the DNS Secondary IP TCPDUMP command from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDNSSecondaryIpTcpDump() {
		try {
			return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.DNS_SECONDARY_IP_TCPDUMP);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			return null;
		}
	}

	/**
	 * This API will get the DNS Primary IP TCPDUMP command from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getDNSPrimaryIpTcpDump() {
		try {
			return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.DNS_PRIMARY_IP_TCPDUMP);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			return null;
		}
	}

	/**
	 * This API will get the Invalid DNS Primary IP TCPDUMP command from properties
	 * 
	 * @author Said Hisham
	 */
	public static String getInvalidDNSPrimaryIpTcpDump() {
		try {
			return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.INVALID_DNS_PRIMARY_IP_TCPDUMP);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			return null;
		}
	}

	/**
	 * This API will return if the device is of a particular model
	 * 
	 * @author Athira
	 */
	public static Boolean isParticularDevice(Dut device) {
		Boolean status = false;
		String model = null;
		try {
			model = BroadbandPropertyFileHandler.getAutomaticsPropsValueByResolvingPlatform(device,
					BroadBandPropertyKeyConstants.PROP_KEY_PARTICULAR_DEVICE);

			if (CommonMethods.isNotNull(model)) {
				status = true;
			}
		} catch (Exception e) {
			LOGGER.info("No device specific value found");
		}
		return status;
	}

	/**
	 * This API will return true if current device is in applicable list
	 * 
	 * @author Athira
	 */
	public static Boolean isApplicableDeviceModel(Dut device) {
		Boolean status = false;
		String model = null;
		try {
			model = BroadbandPropertyFileHandler.getAutomaticsPropsValueByResolvingPlatform(device,
					BroadBandPropertyKeyConstants.PROP_KEY_DEVICE_APPLICABLE_MODEL);

			if (CommonMethods.isNotNull(model)) {
				status = true;
			}
		} catch (Exception e) {
			LOGGER.info("No device specific value found");
		}
		return status;
	}
	
    /**
     * This API will return the payload to enable or disable snmpv2
     * 
     * @return payload to enable or disable snmpv2
     * @author Rakesh C N
     */
    public static String getPayloadToEnableDisableSnmpv2() {
	return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_TO_DISABLE_SNMPV2);
    }
    
    /**
     * This API will get log file for docsis reg
     * 
     * @author Sruthi Santhosh
     */
    public static String getLogFileForDocsisReg(Dut device) {
	return getAutomaticsPropsValueByResolvingPlatform(device,
		BroadBandPropertyKeyConstants.PROP_KEY_FILE_CONSOLELOG);
    }
    
    /**
     * This API will get the specific Device in which default values of OVS Bridge control and Linux Bridge Control values are expected as in test from properties
     * 
     * @author Said Hisham
     */
    public static boolean getDeviceForDefaultValuesCheckForOvsAndLinuxBridgeControl(Dut device) {
	try{
	return Boolean.parseBoolean(getAutomaticsPropsValueByResolvingPlatform(device,
		BroadBandPropertyKeyConstants.DEVICE_CHECK_FOR_EXPECTED_DEFAULT_OVS_LINUXBRIDGE_CONTROL));
	}
	catch(Exception e) {
	    LOGGER.info("device model not supported");
	    return false;
	}
    }
    
    
    /**
     * This API will get the Default Bandsteering Enable value of the device from properties
     * 
     * @author Said Hisham
     */
    public static String getDefaultBandSteeringEnableValue() {
	return AutomaticsTapApi
		.getSTBPropsValue(BroadBandPropertyKeyConstants.DEFAULT_BANDSTEERING_ENABLE_VALUE);
    }
}

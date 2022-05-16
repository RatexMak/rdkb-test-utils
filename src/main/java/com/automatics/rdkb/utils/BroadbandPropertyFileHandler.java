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
     * Uses the partial key by appending it with the device platform to get the property value from cats.props.
     *
     * @param device
     *            The device instance.
     * @param partialPropsKey
     *            Partial property key which will be appended with the device platform to form the complete
     *            key.(Example:- "cdl.unsigned.image.name." it get resolved to
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
     * @return CI server swupdate URL This API will get the latest OpenSSL Version from properties
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
     * This API will get the dnsmasq version for devices with dunfell from properties
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
     * This API will get the Invalid Firewaredownload file path autovault from properties
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
	return AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_DAC15_CDL_URL);
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
     * This API will get the corrupt image Information of same device class from properties
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
     * This API will get the corrupt image Information of different device class from properties
     * 
     * @author Said Hisham
     */
    public static String getCurruptImageForDifferentDeviceClassInfo(Dut device) {
	return getAutomaticsPropsValueByResolvingPlatform(device,
		BroadBandPropertyKeyConstants.MAP_MODEL_CORRUPT_IMAGE_DIFFERENT_DEVICE_CLASS_INFO);
    }

    /**
     * This API will get the models with corrupt image of same device class from properties
     * 
     * @author Said Hisham
     */
    public static String getCurruptImageWithPartialDownload(Dut device) {
	return getAutomaticsPropsValueByResolvingPlatform(device,
		BroadBandPropertyKeyConstants.MAP_MODEL_CORRUPT_IMAGE_PARTIAL_DOWNLOAD);
    }

    /**
     * This API will get the models with corrupt image info of same device class from properties
     * 
     * @author Said Hisham
     */
    public static String getCurruptImageWithPartialDownloadInfo(Dut device) {
	return getAutomaticsPropsValueByResolvingPlatform(device,
		BroadBandPropertyKeyConstants.MAP_MODEL_CORRUPT_IMAGE_PARTIAL_DOWNLOAD_INFO);
    }

    /**
     * This API will get the Xconf firmware location for corrupt images from properties
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
     * This API will get the radio name for for specific device for 2.4ghz from properties
     * 
     * @author Said Hisham
     */
    public static String getRadioName24BasedOnModel(Dut device) {
	return getAutomaticsPropsValueByResolvingPlatform(device, BroadBandPropertyKeyConstants.RADIO_NAME_24GHZ_CHECK);
    }

    /**
     * This API will get the radio name for for specific device for 5ghz from properties
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
    	return Boolean.parseBoolean(getAutomaticsPropsValueByResolvingPlatform(device,
        		BroadBandPropertyKeyConstants.DEVICE_CHECK_VALUE1));
    }
    
    /**
     * This API will check the device type to return the expected value
     * 
     * @author yamini.s
     */
    public static boolean isDeviceCheckToReturnExpectedValue2(Dut device) {
    	return Boolean.parseBoolean(getAutomaticsPropsValueByResolvingPlatform(device,
        		BroadBandPropertyKeyConstants.DEVICE_CHECK_VALUE2));
    }

    /**
     * This API will check the device type to return the expected value
     * 
     * @author yamini.s
     */
    public static boolean isDeviceCheckToReturnExpectedValue3(Dut device) {
    	return Boolean.parseBoolean(getAutomaticsPropsValueByResolvingPlatform(device,
        		BroadBandPropertyKeyConstants.DEVICE_CHECK_VALUE3));
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
	return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.STRING_DEFAULT_GLOBAL_DNS_IPV4_VALUE);
    }
    
    /**
     * This API will get the global DNS IPv6 value from properties
     * 
     * @author Said Hisham
     */
    public static String getGlobalDNSIpv6Value() {
	return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.STRING_DEFAULT_GLOBAL_DNS_IPV6_VALUE);
    }
    
    /**
     * This API will get the DNS IPv4 value for DNS level one primary from properties
     * 
     * @author Said Hisham
     */
    public static String getDNSIpv4ValueLevelOnePrimary() {
	return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_IPV4_VALUE_FOR_DNS_LEVEL_ONE_PRIMARY);
    }
    
    /**
     * This API will get the DNS IPv4 value for DNS level two primary from properties
     * 
     * @author Said Hisham
     */
    public static String getDNSIpv4ValueLevelTwoPrimary() {
	return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_IPV4_VALUE_FOR_DNS_LEVEL_TWO_PRIMARY);
    }
    
    /**
     * This API will get the DNS IPv4 value for DNS level three primary from properties
     * 
     * @author Said Hisham
     */
    public static String getDNSIpv4ValueLevelThreePrimary() {
	return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_IPV4_VALUE_FOR_DNS_LEVEL_THREE_PRIMARY);
    }
    
    /**
     * This API will get the DNS IPv6 value for DNS level one primary from properties
     * 
     * @author Said Hisham
     */
    public static String getDNSIpv6ValueLevelOnePrimary() {
	return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_IPV6_VALUE_FOR_DNS_LEVEL_ONE_PRIMARY);
    }
    
    /**
     * This API will get the DNS IPv6 value for DNS level two primary from properties
     * 
     * @author Said Hisham
     */
    public static String getDNSIpv6ValueLevelTwoPrimary() {
	return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_IPV6_VALUE_FOR_DNS_LEVEL_TWO_PRIMARY);
    }
    
    /**
     * This API will get the DNS IPv6 value for DNS level three primary from properties
     * 
     * @author Said Hisham
     */
    public static String getDNSIpv6ValueLevelThreePrimary() {
	return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_IPV6_VALUE_FOR_DNS_LEVEL_THREE_PRIMARY);
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
	return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_INVALID_SECONDARY_IPV4_VALUE);
    }
    
    /**
     * This API will get the DNS invalid secondary IPv6 value from properties
     * 
     * @author Said Hisham
     */
    public static String getDNSInvalidSecondaryIpv6Value() {
	return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_INVALID_SECONDARY_IPV6_VALUE);
    }
    
    /**
     * This API will get the DNS invalid primary IPv4 value from properties
     * 
     * @author Said Hisham
     */
    public static String getDNSInvalidPrimaryIpv4Value() {
	return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_INVALID_PRIMARY_IPV4_VALUE );
    }
    
    /**
     * This API will get the DNS invalid primary IPv6 value from properties
     * 
     * @author Said Hisham
     */
    public static String getDNSInvalidPrimaryIpv6Value() {
	return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_INVALID_PRIMARY_IPV6_VALUE );
    }
    
    /**
     * This API will get the DNS valid secondary IPv4 value from properties
     * 
     * @author Said Hisham
     */
    public static String getDNSValidSecondaryIpv4Value() {
	return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_VALID_SECONDARY_IPV4_VALUE );
    }
    
    /**
     * This API will get the DNS valid secondary IPv6 value from properties
     * 
     * @author Said Hisham
     */
    public static String getDNSValidSecondaryIpv6Value() {
	return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_VALID_SECONDARY_IPV6_VALUE );
    }
    
    /**
     * This API will get the DNS another invalid secondary IPv4 value from properties
     * 
     * @author Said Hisham
     */
    public static String getDNSAnotherInvalidSecondaryIpv4Value() {
	return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_ANOTHER_INVALID_SECONDARY_IPV4_VALUE );
    }
    
    /**
     * This API will get the DNS another invalid secondary IPv6 value from properties
     * 
     * @author Said Hisham
     */
    public static String getDNSAnotherInvalidSecondaryIpv6Value() {
	return AutomaticsPropertyUtility.getProperty(BroadBandPropertyKeyConstants.STRING_DNS_ANOTHER_INVALID_SECONDARY_IPV6_VALUE );
	
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
     * This API will check if the server details are configured to upload crash details
     * 
     * @author yamini.s
     */
    public static Boolean isServerConfiguredToUploadCrashDetails() {
    	return Boolean.parseBoolean(AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.IS_SERVER_CONFIGURED_TO_UPLOAD_CRASH_DETAILS));
        }
    

}

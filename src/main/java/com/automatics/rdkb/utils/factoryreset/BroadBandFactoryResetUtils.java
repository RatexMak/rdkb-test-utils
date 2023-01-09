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

package com.automatics.rdkb.utils.factoryreset;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Dut;
import com.automatics.enums.TR69ParamDataType;
import com.automatics.rdkb.constants.BroadBandCommandConstants;
import com.automatics.rdkb.constants.BroadBandPropertyKeyConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.WebPaParamConstants;
import com.automatics.rdkb.interfaces.FactoryResetSettings;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.rdkb.utils.wifi.BroadBandWiFiUtils;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpMib;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpUtils;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.CommonUtils;
import com.automatics.rdkb.utils.tr69.BroadBandTr69Utils;
import com.automatics.utils.AutomaticsPropertyUtility;

/**
 * Utility class with methods related to Factory reset and its functionality
 * 
 * @author Praveenkumar Paneerselvam
 * @Refactor Athira
 *
 */

public class BroadBandFactoryResetUtils {

	// ** SLF4J logger. *//
	private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandFactoryResetUtils.class);

	/**
	 * Enum class for Wifi Setting option
	 * 
	 * @author Praveenkumar Paneerselvam
	 * @Refactor Athira
	 *
	 */
	public enum WifiSsid implements FactoryResetSettings {
		WIFI_SSID_2_4(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_NAME,
				BroadBandWebPaConstants.WEBPA_DEFAULT_SSID_NAME_2_4_GHZ),
		WIFI_SSID_5(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_NAME,
				BroadBandWebPaConstants.WEBPA_DEFAULT_SSID_NAME_5_GHZ);

		private String webpaParameter = null;
		private String systemDefaultVariable = null;

		private WifiSsid(String webpaParameter, String systemDefaultVariable) {
			this.webpaParameter = webpaParameter;
			this.systemDefaultVariable = systemDefaultVariable;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.automatics.rdkb.utils.factoryreset.BroadBandFactoryResetUtils.
		 * FactoryResetSettings#getDefaultValue(com. automatics.tap.AutomaticsTapApi,
		 * com.automatics.device.Dut)
		 */
		@Override
		public String getDefaultValue(AutomaticsTapApi tapEnv, Dut device) {
			LOGGER.debug("ENTERING METHOD - getDefaultValue() of WIFI SSID");
			String response = tapEnv.executeWebPaCommand(device, this.getSystemDefaultVariable());
			LOGGER.info("Default SSID for 2.4 Ghz of the device is " + response);
			if (CommonMethods.isNull(response)) {
				LOGGER.error("Failed to get default SSID using WebPa parameter " + this.getSystemDefaultVariable());
			}
			LOGGER.debug("EXIT FROM METHOD - getDefaultValue() of WIFI SSID");
			return response;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.automatics.rdkb.utils.factoryreset.BroadBandFactoryResetUtils.
		 * FactoryResetSettings#
		 * updateSettingsValueOnDevice(com.automatics.tap.AutomaticsTapApi,
		 * com.automatics.device.Dut, java.lang.String)
		 */
		@Override
		public boolean updateSettingsValueOnDevice(AutomaticsTapApi tapEnv, Dut device, String value) {
			LOGGER.debug("ENTERING METHOD - updateSettingsValueOnDevice() of WIFI SSID");
			boolean status = false;
			if (CommonMethods.isNotNull(value)) {
				LOGGER.info("Modify Current SSID name of 2.4 Ghz, other than default SSID value");
				status = BroadBandWiFiUtils.setWebPaParams(device, this.getWebpaParameter(), value,
						BroadBandTestConstants.CONSTANT_0);
			}
			LOGGER.debug("ENTERING METHOD - updateSettingsValueOnDevice() of WIFI SSID");
			return status;
		}

		@Override
		public String getWebpaParameter() {
			return webpaParameter;
		}

		/**
		 * Method to get webpa/variable name of the parameter to find default value *
		 * 
		 * @return the systemDefaultVariable
		 */
		public String getSystemDefaultVariable() {
			return systemDefaultVariable;
		}
	}

	/**
	 * Enum class for Parental Control Service Setting option
	 * 
	 * @author Praveenkumar Paneerselvam
	 * @Refactor Athira
	 *
	 */
	public enum ParentalControlService implements FactoryResetSettings {
		PARENTAL_CONTROL_MANAGED_SITES_ENABLE("Device.X_Comcast_com_ParentalControl.ManagedSites.Enable",
				"managedsites_enabled="),
		PARENTAL_CONTROL_MANAGED_SERVICES_ENABLE("Device.X_Comcast_com_ParentalControl.ManagedServices.Enable",
				"managedservices_enabled="),
		PARENTAL_CONTROL_MANAGED_DEVICES_ENABLE("Device.X_Comcast_com_ParentalControl.ManagedDevices.Enable",
				"manageddevices_enabled=");

		private String webpaParameter = null;
		private String systemDefaultVariable = null;

		/**
		 * @return the webpaParameter
		 */
		public String getWebpaParameter() {
			return webpaParameter;
		}

		/**
		 * @return the systemDefaultVariable
		 */
		public String getSystemDefaultVariable() {
			return systemDefaultVariable;
		}

		/**
		 * Private Constructor
		 * 
		 * @param webpaParameter
		 * @param systemDefaultVariable
		 */
		private ParentalControlService(String webpaParameter, String systemDefaultVariable) {
			this.webpaParameter = webpaParameter;
			this.systemDefaultVariable = systemDefaultVariable;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.automatics.rdkb.utils.factoryreset.BroadBandFactoryResetUtils.
		 * FactoryResetSettings#getDefaultValue(com. automatics.tap.AutomaticsTapApi,
		 * com.automatics.device.Dut)
		 */
		@Override
		public String getDefaultValue(AutomaticsTapApi tapEnv, Dut device) {
			String defaultValue = null;
			LOGGER.debug("STARTING METHOD : getDefaultValue() of Parental Control Service");
			defaultValue = getDefaultValueFromSystemDefaults(tapEnv, device, this.getSystemDefaultVariable());
			if (CommonMethods.isNotNull(defaultValue)) {
				defaultValue = BroadBandTestConstants.STRING_VALUE_ONE.equalsIgnoreCase(defaultValue)
						? BroadBandTestConstants.TRUE
						: BroadBandTestConstants.FALSE;
			}
			LOGGER.debug("ENDING METHOD : getDefaultValue() of Parental Control Service");
			return defaultValue;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.automatics.rdkb.utils.factoryreset.BroadBandFactoryResetUtils.
		 * FactoryResetSettings#
		 * updateSettingsValueOnDevice(com.automatics.tap.AutomaticsTapApi,
		 * com.automatics.device.Dut, java.lang.String)
		 */
		@Override
		public boolean updateSettingsValueOnDevice(AutomaticsTapApi tapEnv, Dut device, String valueToBeSet) {
			LOGGER.debug("ENTERING METHOD - updateSettingsValueOnDevice() of Parental Control ");
			boolean status = false;
			status = BroadBandWiFiUtils.setWebPaParams(device, this.getWebpaParameter(), valueToBeSet,
					BroadBandTestConstants.CONSTANT_3);
			LOGGER.debug("STARTING METHOD : getDefaultValue() of Parental Control Service");
			return status;
		}

	}

	/**
	 * Enum class for Wifi Settings 2.4Ghz option
	 * 
	 * @author Praveenkumar Paneerselvam
	 * @Refactor Athira
	 *
	 */
	public enum WifiSettings2Ghz implements FactoryResetSettings {
		// Defaulf value is hardcoded as 40MHz
		CHANNELBANDWIDTH(BroadBandWebPaConstants.WEBPA_PARAM_FOR_OPERATING_BANDWIDTH_IN_2GHZ_BAND,
				BroadBandTestConstants.OPERATING_BANDWIDTH_20_MMZ);

		private String webpaParameter = null;

		private String systemDefaultVariable = null;

		/**
		 * @param webpaParameter
		 * @param systemDefaultVariable
		 */
		private WifiSettings2Ghz(String webpaParameter, String systemDefaultVariable) {
			this.webpaParameter = webpaParameter;
			this.systemDefaultVariable = systemDefaultVariable;
		}

		/**
		 * @return the systemDefaultVariable
		 */
		public String getSystemDefaultVariable() {
			return systemDefaultVariable;
		}

		/**
		 * @param systemDefaultVariable the systemDefaultVariable to set
		 */
		public void setSystemDefaultVariable(String systemDefaultVariable) {
			this.systemDefaultVariable = systemDefaultVariable;
		}

		/**
		 * @return the webpaParameter
		 */
		public String getWebpaParameter() {
			return webpaParameter;
		}

		/**
		 * @param webpaParameter the webpaParameter to set
		 */
		public void setWebpaParameter(String webpaParameter) {
			this.webpaParameter = webpaParameter;
		}

		@Override
		public String getDefaultValue(AutomaticsTapApi tapEnv, Dut device) {
			if (CHANNELBANDWIDTH.equals(this) && (DeviceModeHandler.isDSLDevice(device))) {
				systemDefaultVariable = BroadBandTestConstants.OPERATING_BANDWIDTH_40_MMZ;
			}
			return systemDefaultVariable;
		}

		@Override
		public boolean updateSettingsValueOnDevice(AutomaticsTapApi tapEnv, Dut device, String valueToBeSet) {
			boolean status = false;
			LOGGER.debug("STARTING METHOD : updateSettingsValueOnDevice() of WifiSettings2Ghz");
			status = BroadBandWiFiUtils.setWebPaParams(device, this.getWebpaParameter(), valueToBeSet,
					WebPaDataTypes.STRING.getValue());
			LOGGER.debug("ENDING METHOD : updateSettingsValueOnDevice() of WifiSettings2Ghz");
			return status;
		}
	}

	/**
	 * Enum class for Code Big settings
	 * 
	 * @author Ashwin sankara
	 * @Refactor Athira
	 *
	 */
	public enum CodeBig implements FactoryResetSettings {

		CODE_BIG_ENABLE(BroadBandWebPaConstants.WEBPA_PARAM_CODEBIG_FIRST_ENABLE, null);

		private String webpaParameter = null;
		private String systemDefaultVariable = null;

		private CodeBig(String webpaParameter, String systemDefaultVariable) {
			this.webpaParameter = webpaParameter;
			this.systemDefaultVariable = systemDefaultVariable;
		}

		@Override
		public String getDefaultValue(AutomaticsTapApi tapEnv, Dut device) {
			if (CommonMethods.isAtomSyncAvailable(device, tapEnv) || DeviceModeHandler.isBusinessClassDevice(device)) {
				return BroadBandTestConstants.TRUE;
			} else {
				return BroadBandTestConstants.FALSE;
			}
		}

		@Override
		public boolean updateSettingsValueOnDevice(AutomaticsTapApi tapEnv, Dut device, String value) {
			LOGGER.debug("ENTERING METHOD - updateSettingsValueOnDevice() of CodeBig");
			boolean status = false;
			if (CommonMethods.isNotNull(value)) {
				LOGGER.info("Modify Current code big enable value ");
				status = BroadBandWiFiUtils.setWebPaParams(device, this.getWebpaParameter(), value,
						BroadBandTestConstants.CONSTANT_3);
			}
			LOGGER.debug("ENTERING METHOD - updateSettingsValueOnDevice() of CodeBig");
			return status;
		}

		@Override
		public String getWebpaParameter() {
			return webpaParameter;
		}

		@Override
		public String getSystemDefaultVariable() {
			return systemDefaultVariable;
		}

	}

	/**
	 * Enum class for Telemetry endpoint settings
	 * 
	 * @author Ashwin sankara
	 *
	 */
	public enum TelemetryEndpoint implements FactoryResetSettings {

		TELEMETRY_ENDPOINT_ENABLE(BroadBandWebPaConstants.WEBPA_PARAM_WIFI_TELEMETRY_ENDPOINT_ENABLE,
				BroadBandTestConstants.FALSE);

		private String webpaParameter = null;
		private String systemDefaultVariable = null;

		private TelemetryEndpoint(String webpaParameter, String systemDefaultVariable) {
			this.webpaParameter = webpaParameter;
			this.systemDefaultVariable = systemDefaultVariable;
		}

		@Override
		public String getDefaultValue(AutomaticsTapApi tapEnv, Dut device) {
			return BroadBandTestConstants.FALSE;
		}

		@Override
		public boolean updateSettingsValueOnDevice(AutomaticsTapApi tapEnv, Dut device, String value) {
			LOGGER.debug("ENTERING METHOD - updateSettingsValueOnDevice() of Telemetry Endpoint");
			boolean status = false;
			LOGGER.info("Modify Current Telemetry Endpoint Enable value ");
			status = BroadBandWebPaUtils.setAndVerifyParameterValuesUsingWebPaorDmcli(device, tapEnv,
					this.webpaParameter, BroadBandTestConstants.CONSTANT_3, value);
			LOGGER.debug("ENTERING METHOD - updateSettingsValueOnDevice() of Telemetry Endpoint");
			return status;
		}

		@Override
		public String getWebpaParameter() {
			return webpaParameter;
		}

		@Override
		public String getSystemDefaultVariable() {
			return systemDefaultVariable;
		}

	}

	public enum LimitBeaconDetectionEnum implements FactoryResetSettings {
		LIMIT_BEACON_DETECTION(BroadBandWebPaConstants.WEBPA_PARAM_LIMIT_BEACON_DETECTION,
				BroadBandTestConstants.FALSE);

		private String webpaParameter = null;
		private String systemDefaultVariable = null;

		private LimitBeaconDetectionEnum(String webpaParameter, String systemDefaultVariable) {
			this.webpaParameter = webpaParameter;
			this.systemDefaultVariable = systemDefaultVariable;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.automatics.rdkb.utils.factoryreset.BroadBandFactoryResetUtils.
		 * FactoryResetSettings#getDefaultValue(com. automatics.tap.AutomaticssTapApi,
		 * com.automatics.device.Dut)
		 */
		@Override
		public String getDefaultValue(AutomaticsTapApi tapEnv, Dut device) {
			return this.getSystemDefaultVariable();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.automatics.rdkb.utils.factoryreset.BroadBandFactoryResetUtils.
		 * FactoryResetSettings#
		 * updateSettingsValueOnDevice(com.automatics.tap.AutomaticssTapApi,
		 * com.automatics.device.Dut, java.lang.String)
		 */
		@Override
		public boolean updateSettingsValueOnDevice(AutomaticsTapApi tapEnv, Dut device, String value) {
			boolean status = false;
			try {
				LOGGER.debug("ENTERING METHOD - updateSettingsValueOnDevice() of LimitBeaconDetection");
				status = false;
				if (CommonMethods.isNotNull(value)) {
					LOGGER.info("Modify Current LimitBeaconDetection value other than default value");
					status = BroadBandWebPaUtils.setAndVerifyParameterValuesUsingWebPaorDmcli(device, tapEnv,
							this.webpaParameter, BroadBandTestConstants.CONSTANT_3, value);
				}
			} catch (TestException exception) {
				LOGGER.error("ERROR While setting webpa parameter. Error Message - " + exception.getMessage());
			}
			LOGGER.debug("ENTERING METHOD - updateSettingsValueOnDevice() of LimitBeaconDetection");
			return status;
		}

		@Override
		public String getWebpaParameter() {
			return webpaParameter;
		}

		/**
		 * Method to get webpa/variable name of the parameter to find default value *
		 * 
		 * @return the systemDefaultVariable
		 */
		public String getSystemDefaultVariable() {
			return systemDefaultVariable;
		}
	}

	/**
	 * Enum class for Rabid framework memory limit
	 * 
	 * @author ArunKumar Jayachandran
	 * @Refactor Athira
	 *
	 */
	public enum RabidMemoryLimit implements FactoryResetSettings {
		// Defaulf value is hardcoded as 20MB
		RABIDMEMORYLIMIT(BroadBandWebPaConstants.WEBPA_PARAM_RABID_FW_MEMORY_LIMIT,
				BroadBandTestConstants.STRING_VALUE_20);

		private String webpaParameter = null;
		private String systemDefaultVariable = null;

		private RabidMemoryLimit(String webpaParameter, String systemDefaultVariable) {
			this.webpaParameter = webpaParameter;
			this.systemDefaultVariable = systemDefaultVariable;
		}

		@Override
		public String getDefaultValue(AutomaticsTapApi tapEnv, Dut device) {

			return BroadBandTestConstants.STRING_VALUE_20;
		}

		@Override
		public boolean updateSettingsValueOnDevice(AutomaticsTapApi tapEnv, Dut device, String value) {
			LOGGER.debug("ENTERING METHOD - updateSettingsValueOnDevice() of Rabid framework memory limit");
			boolean status = false;
			LOGGER.info("Modify Current Rabid framework memory limit value ");
			status = BroadBandWebPaUtils.setAndVerifyParameterValuesUsingWebPaorDmcli(device, tapEnv,
					this.webpaParameter, BroadBandTestConstants.CONSTANT_2, value);
			LOGGER.debug("ENTERING METHOD - updateSettingsValueOnDevice() of Rabid framework memory limit");
			return status;
		}

		@Override
		public String getWebpaParameter() {
			return webpaParameter;
		}

		@Override
		public String getSystemDefaultVariable() {
			return systemDefaultVariable;
		}

	}

	/**
	 * Enum class for Force WiFi disable
	 * 
	 * @author Ashwin sankara
	 *
	 */
	public enum ForceWiFiDisable implements FactoryResetSettings {

		FORCEWIFIDISABLE(BroadBandWebPaConstants.WEBPA_PARAM_FORCE_WIFI_DISABLE, BroadBandTestConstants.FALSE);

		private String webpaParameter = null;
		private String systemDefaultVariable = null;

		private ForceWiFiDisable(String webpaParameter, String systemDefaultVariable) {
			this.webpaParameter = webpaParameter;
			this.systemDefaultVariable = systemDefaultVariable;
		}

		@Override
		public String getDefaultValue(AutomaticsTapApi tapEnv, Dut device) {
			return BroadBandTestConstants.FALSE;
		}

		@Override
		public boolean updateSettingsValueOnDevice(AutomaticsTapApi tapEnv, Dut device, String value) {
			LOGGER.debug("ENTERING METHOD - updateSettingsValueOnDevice() of ForceWiFiDisable");
			boolean status = false;
			LOGGER.info("Modify Current Force WiFi disable value ");
			status = BroadBandWebPaUtils.setAndVerifyParameterValuesUsingWebPaorDmcli(device, tapEnv,
					this.webpaParameter, BroadBandTestConstants.CONSTANT_3, value);
			LOGGER.debug("ENTERING METHOD - updateSettingsValueOnDevice() of ForceWiFiDisable");
			return status;
		}

		@Override
		public String getWebpaParameter() {
			return webpaParameter;
		}

		@Override
		public String getSystemDefaultVariable() {
			return systemDefaultVariable;
		}
	}

	/**
	 * Method to add default value of the given parameter in the Map
	 * 
	 * @param device        Dut object instance
	 * @param defaultValues Map to store default value, to its corresponding setting
	 *                      name in key
	 * @param parameters    to which default value has to be fetched.
	 * @author Praveenkumar Paneerselvam
	 * @Refactor Athira
	 */
	public static void addDefaultValueInMap(Dut device, AutomaticsTapApi tapEnv,
			Map<FactoryResetSettings, String> defaultValues, FactoryResetSettings[] parameters) {
		LOGGER.debug("INSIDE METHOD : addDefaultValueInMap()");
		for (FactoryResetSettings parameter : parameters) {
			String response = parameter.getDefaultValue(tapEnv, device);
			LOGGER.info("ACTUAL response for Default value for parameter " + parameter + " is " + response);
			if (CommonMethods.isNull(response)) {
				LOGGER.error("Failed to get default value for parameter " + parameter);
			}
			defaultValues.put(parameter, response);
		}
		LOGGER.debug("EXIT FROM METHOD : addDefaultValueInMap()");
	}

	/**
	 * Enum class for Wifi Setting option SNMP
	 * 
	 * @author Praveenkumar Paneerselvam
	 * @Refactor Athira
	 *
	 */
	public enum WifiSsidSnmp implements FactoryResetSettings {
		WIFI_SSID_2_4(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_2_4_GHZ_PRIVATE_SSID_NAME,
				BroadBandWebPaConstants.WEBPA_INDEX_2_4_GHZ_PRIVATE_SSID),
		WIFI_SSID_5(BroadBandWebPaConstants.WEBPA_PARAM_DEVICE_WIFI_5_GHZ_PRIVATE_SSID_NAME,
				BroadBandWebPaConstants.WEBPA_INDEX_5_GHZ_PRIVATE_SSID);

		private String webpaParameter = null;
		private String systemDefaultVariable = null;

		private WifiSsidSnmp(String webpaParameter, String systemDefaultVariable) {
			this.webpaParameter = webpaParameter;
			this.systemDefaultVariable = systemDefaultVariable;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.automatics.rdkb.utils.factoryreset.BroadBandFactoryResetUtils.
		 * FactoryResetSettings#getDefaultValue(com. automatics.tap.AutomaticssTapApi,
		 * com.automatics.device.Dut)
		 */
		@Override
		public String getDefaultValue(AutomaticsTapApi tapEnv, Dut device) {
			LOGGER.debug("ENTERING METHOD - getDefaultValue() of WIFI SSID");
			String response = BroadBandSnmpUtils.executeSnmpGetWithTableIndexOnRdkDevices(tapEnv, device,
					BroadBandSnmpMib.ECM_DEFAULT_SSID.getOid(), this.getSystemDefaultVariable());
			LOGGER.info("Default SSID for 2.4 Ghz of the device is " + response);
			if (CommonMethods.isNull(response)) {
				LOGGER.error("Failed to get default SSID using SNMP OID " + BroadBandSnmpMib.ECM_DEFAULT_SSID.getOid()
						+ this.getSystemDefaultVariable());
			}
			LOGGER.debug("EXIT FROM METHOD - getDefaultValue() of WIFI SSID");
			return response;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.automatics.rdkb.utils.factoryreset.BroadBandFactoryResetUtils.
		 * FactoryResetSettings#
		 * updateSettingsValueOnDevice(com.automatics.tap.AutomaticsTapApi,
		 * com.automatics.device.Dut, java.lang.String)
		 */
		@Override
		public boolean updateSettingsValueOnDevice(AutomaticsTapApi tapEnv, Dut device, String value) {
			LOGGER.debug("ENTERING METHOD - updateSettingsValueOnDevice() of WIFI SSID");
			boolean status = false;
			if (CommonMethods.isNotNull(value)) {
				LOGGER.info("Modify Current SSID name of 2.4 Ghz, other than default SSID value");
				status = BroadBandWiFiUtils.setWebPaParams(device, this.getWebpaParameter(), value,
						BroadBandTestConstants.CONSTANT_0);
			}
			LOGGER.debug("ENTERING METHOD - updateSettingsValueOnDevice() of WIFI SSID");
			return status;
		}

		@Override
		public String getWebpaParameter() {
			return webpaParameter;
		}

		/**
		 * Method to get webpa/variable name of the parameter to find default value *
		 * 
		 * @return the systemDefaultVariable
		 */
		public String getSystemDefaultVariable() {
			return systemDefaultVariable;
		}
	}

	/**
	 * Enum class for Advanced Feature Service Setting option
	 * 
	 * @author Praveenkumar Paneerselvam
	 * @Refacror Athira
	 *
	 */
	public enum AdvancedFeatureServices implements FactoryResetSettings {
		PORT_MAPPING_ENABLE("Device.NAT.X_Comcast_com_EnablePortMapping", "CosaNAT::port_forward_enabled="),
		HS_PORT_MAPPING_ENABLE("Device.NAT.X_Comcast_com_EnableHSPortMapping", "CosaNAT::port_hs_forward_enabled="),
		PORT_TRIGGER_ENABLE("Device.NAT.X_CISCO_COM_PortTriggers.Enable", "CosaNAT::port_trigger_enabled="),
		DYNAMIC_DNS_ENABLE("Device.X_CISCO_COM_DDNS.Enable", "ddns_enable="),
		DEVICE_DISCOVERY_UPNP_ENABLE("Device.UPnP.Device.UPnPIGD", "upnp_igd_enabled=");

		private String webpaParameter = null;
		private String systemDefaultVariable = null;

		/**
		 * @return the webpaParameter
		 */
		public String getWebpaParameter() {
			return webpaParameter;
		}

		/**
		 * @return the systemDefaultVariable
		 */
		public String getSystemDefaultVariable() {
			return systemDefaultVariable;
		}

		/**
		 * @param webpaParameter
		 * @param systemDefaultVariable
		 */
		private AdvancedFeatureServices(String webpaParameter, String systemDefaultVariable) {
			this.webpaParameter = webpaParameter;
			this.systemDefaultVariable = systemDefaultVariable;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.automatics.rdkb.utils.factoryreset.BroadBandFactoryResetUtils.
		 * FactoryResetSettings#getDefaultValue(com. automatics.tap.AutomaticsTapApi,
		 * com.automatics.device.Dut)
		 */
		public String getDefaultValue(AutomaticsTapApi tapEnv, Dut device) {
			String defaultValue = null;
			LOGGER.debug("STARTING METHOD : getDefaultValue() of AdvanceFeature");
			defaultValue = getDefaultValueFromSystemDefaults(tapEnv, device, this.getSystemDefaultVariable());
			if (CommonMethods.isNotNull(defaultValue)) {
				defaultValue = BroadBandTestConstants.STRING_VALUE_ONE.equalsIgnoreCase(defaultValue)
						? BroadBandTestConstants.TRUE
						: BroadBandTestConstants.FALSE;
			}
			LOGGER.debug("ENDING METHOD : getDefaultValue() of AdvanceFeature");
			return defaultValue;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.automatics.rdkb.utils.factoryreset.BroadBandFactoryResetUtils.
		 * FactoryResetSettings#
		 * updateSettingsValueOnDevice(com.automatics.tap.AutomaticsTapApi,
		 * com.automatics.device.Dut, java.lang.String)
		 */
		@Override
		public boolean updateSettingsValueOnDevice(AutomaticsTapApi tapEnv, Dut device, String valueToBeSet) {
			boolean status = false;
			LOGGER.debug("STARTING METHOD : updateSettingsValueOnDevice() of AdvanceFeature");
			status = BroadBandWiFiUtils.setWebPaParams(device, this.getWebpaParameter(), valueToBeSet,
					BroadBandTestConstants.CONSTANT_3);
			LOGGER.debug("ENDING METHOD : updateSettingsValueOnDevice() of AdvanceFeature");
			return status;
		}

	}

	/**
	 * Enum class for Advanced Feature Service Setting option
	 * 
	 * @author Praveenkumar Paneerselvam
	 *
	 */
	public enum LocalGatewayIPv4 implements FactoryResetSettings {
		LAN_GATEWAY_IP(BroadBandWebPaConstants.WEBPA_PARAM_LAN_IP_ADDRESS,
				BroadBandWebPaConstants.WEBPA_PARAM_DEFAULT_LANIP_FOR_SYNDICATION),
		LAN_DHCP_START(BroadBandWebPaConstants.WEBPA_PARAM_TO_RETRIEVE_DHCP_STARTING_IP_ADDRESS,
				BroadBandWebPaConstants.WEBPA_PARAM_TO_RETRIEVE_DHCP_STARTING_IP_ADDRESS),
		LAN_DHCP_END(BroadBandWebPaConstants.WEBPA_PARAM_TO_RETRIEVE_DHCP_ENDING_IP_ADDRESS,
				BroadBandWebPaConstants.WEBPA_PARAM_TO_RETRIEVE_DHCP_ENDING_IP_ADDRESS);

		private String webpaParameter = null;
		private String systemDefaultVariable = null;

		/**
		 * @return the webpaParameter
		 */
		public String getWebpaParameter() {
			return webpaParameter;
		}

		/**
		 * @return the systemDefaultVariable
		 */
		public String getSystemDefaultVariable() {
			return systemDefaultVariable;
		}

		/**
		 * @param webpaParameter
		 * @param systemDefaultVariable
		 */
		private LocalGatewayIPv4(String webpaParameter, String systemDefaultVariable) {
			this.webpaParameter = webpaParameter;
			this.systemDefaultVariable = systemDefaultVariable;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.automatics.rdkb.utils.factoryreset.BroadBandFactoryResetUtils.
		 * FactoryResetSettings#getDefaultValue(com. automatics.tap.AutomaticsTapApi,
		 * com.automatics.device.Dut)
		 */
		public String getDefaultValue(AutomaticsTapApi tapEnv, Dut device) {
			String defaultValue = null;
			String errorMessage = null;
			LOGGER.debug("STARTING METHOD : getDefaultValue() of AdvanceFeature");
			try {
				if (CommonMethods.isAtomSyncAvailable(device, tapEnv) || DeviceModeHandler.isFibreDevice(device)
						|| DeviceModeHandler.isBusinessClassDevice(device) || DeviceModeHandler.isRPIDevice(device)) {
					switch (getWebpaParameter()) {
					case BroadBandWebPaConstants.WEBPA_PARAM_LAN_IP_ADDRESS:
						defaultValue = getDefaultValueFromSystemDefaults(tapEnv, device,
								BroadBandTestConstants.STRING_LAN_IP_ADDR);
						break;
					case BroadBandWebPaConstants.WEBPA_PARAM_TO_RETRIEVE_DHCP_STARTING_IP_ADDRESS:
						defaultValue = getDefaultValueFromSystemDefaults(tapEnv, device,
								BroadBandTestConstants.STRING_DHCP_START_ADDR);
						break;
					case BroadBandWebPaConstants.WEBPA_PARAM_TO_RETRIEVE_DHCP_ENDING_IP_ADDRESS:
						defaultValue = getDefaultValueFromSystemDefaults(tapEnv, device,
								BroadBandTestConstants.STRING_DHCP_END_ADDR);
						break;
					}
				} else {
					defaultValue = getDefaultValueFromSystemDefaultsFromJson(tapEnv, device,
							this.getSystemDefaultVariable());
				}
			} catch (Exception e) {
				errorMessage = "Exception occured while verifying the default value";
				LOGGER.error(errorMessage);
			}
			LOGGER.debug("ENDING METHOD : getDefaultValue() of AdvanceFeature");
			return defaultValue;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.automatics.rdkb.utils.factoryreset.BroadBandFactoryResetUtils.
		 * FactoryResetSettings#
		 * updateSettingsValueOnDevice(com.automatics.tap.AutomaticsTapApi,
		 * com.automatics.device.Dut, java.lang.String)
		 */
		@Override
		public boolean updateSettingsValueOnDevice(AutomaticsTapApi tapEnv, Dut device, String valueToBeSet) {
			boolean status = false;
			LOGGER.debug("STARTING METHOD : updateSettingsValueOnDevice() of AdvanceFeature");
			status = BroadBandWiFiUtils.setWebPaParams(device, this.getWebpaParameter(), valueToBeSet,
					WebPaDataTypes.STRING.getValue());
			LOGGER.debug("ENDING METHOD : updateSettingsValueOnDevice() of AdvanceFeature");
			return status;
		}

	}

	/**
	 * Utility method to get default value form system default file "
	 * /etc/utopia/system_defaults"
	 * 
	 * @param tapEnv       AutomaticsTapApi Object
	 * @param device       Dut object instance
	 * @param variableName setting name for which
	 * @return default value for the parameter if it is present in file"
	 *         /etc/utopia/system_defaults", else returns null.
	 */
	public static String getDefaultValueFromSystemDefaults(AutomaticsTapApi tapEnv, Dut device, String variableName) {
		String stringResponse = null;
		LOGGER.debug("STARTING METHOD : getDefaultValueFromSystemDefaults(string");
		String command = CommonMethods.concatStringUsingStringBuffer(BroadBandTestConstants.GREP_COMMAND, variableName,
				BroadBandTestConstants.FILE_SYSTEM_DEFAULTS);
		String response = tapEnv.executeCommandUsingSsh(device, command);
		LOGGER.info("Value from default file for " + variableName + " is - " + response);
		if (CommonMethods.isNotNull(response)) {
			stringResponse = CommonMethods.patternFinder(response,
					variableName + BroadBandTestConstants.REG_EXPRESSION_DOT_STAR);
			LOGGER.info("default value for variable " + variableName + " is - " + stringResponse);
		} else {
			LOGGER.error("Default value for variable " + variableName + " is not avaiable in "
					+ BroadBandTestConstants.FILE_SYSTEM_DEFAULTS);
		}
		LOGGER.debug("ENDING METHOD : getDefaultValueFromSystemDefaults(string");
		return stringResponse;
	}

	/**
	 * Utility method to get default value form system default file "
	 * /etc/utopia/system_defaults"
	 * 
	 * @param tapEnv       AutomaticsTapApi Object
	 * @param device       Dut object instance
	 * @param variableName setting name for which
	 * @return default value for the parameter if it is present in file"
	 *         /etc/utopia/system_defaults", else returns null.
	 */
	public static String getDefaultValueFromSystemDefaultsFromJson(AutomaticsTapApi tapEnv, Dut device,
			String variableName) {
		String stringResponse = null;
		LOGGER.debug("STARTING METHOD : getDefaultValueFromSystemDefaults(string");
		String response = tapEnv.executeWebPaCommand(device,
				BroadBandWebPaConstants.WEBPA_PARAM_FOR_SYNDICATION_PARTNER_ID);
		if (CommonMethods.isNotNull(response)) {
			stringResponse = BroadBandWebPaUtils.getWebPaParameterResponseAndPartnersDefaultJson(device, tapEnv,
					variableName, response);
			LOGGER.info("default value for variable " + variableName + " is - " + stringResponse);
		} else {
			LOGGER.error("Default value for variable " + variableName + " is not avaiable in "
					+ BroadBandTestConstants.FILE_SYSTEM_DEFAULTS);
		}
		LOGGER.debug("ENDING METHOD : getDefaultValueFromSystemDefaults(string");
		return stringResponse;
	}

	/**
	 * Method to set settings on device
	 * 
	 * @param device               Dut Instance
	 * @param factoryResetSettings settings option to which value has to be modified
	 * @param defaultValues        default value for the option
	 * @param skipList             is added with option for which value is not set
	 * @param tapEnv               AutomaticsTapApi Instance
	 * @return message with success and failed, based on result.
	 * @author Praveenkumar Paneerselvam
	 * @Refactor Athira
	 */
	public static String updateSettingsOnDevice(Dut device, FactoryResetSettings[] factoryResetSettings,
			Map<FactoryResetSettings, String> defaultValues, List<FactoryResetSettings> skipList,
			AutomaticsTapApi tapEnv) {
		LOGGER.debug("Inside Method : updateSettingsOnDevice()");
		StringBuilder message = new StringBuilder();
		String defaultValue = null;
		for (FactoryResetSettings factoryResetSetting : factoryResetSettings) {
			defaultValue = defaultValues.get(factoryResetSetting);
			if (CommonMethods.isNotNull(defaultValue)) {
				String valueToBeSet = getValueToBeSet(factoryResetSetting, defaultValue, device, tapEnv);
				if (factoryResetSetting.updateSettingsValueOnDevice(tapEnv, device, valueToBeSet)) {
					message.append("SUCCESS: modified value for " + factoryResetSetting
							+ BroadBandTestConstants.DELIMITER_NEW_LINE);
					LOGGER.info("Value set successfully for the parameter " + factoryResetSetting
							+ BroadBandTestConstants.DELIMITER_NEW_LINE);
				} else {
					skipList.add(factoryResetSetting);
					message.append("FAILED : Failed to set value " + valueToBeSet + " for parameter "
							+ factoryResetSetting.toString() + BroadBandTestConstants.DELIMITER_NEW_LINE);
				}
			} else {
				message.append("FAILED: Default Value is not obtained for parameter " + factoryResetSetting.toString()
						+ " Skipping step to modify the value " + BroadBandTestConstants.DELIMITER_NEW_LINE);
			}
		}
		LOGGER.info(message.toString());
		LOGGER.debug("Exit from Method : updateSettingsOnDevice()");
		return message.toString();
	}

	/**
	 * Method to get value to be set in device depending on the parameter.
	 * 
	 * @param factoryResetSetting parameter to which value to be set.
	 * @param defaultValue        Default value of the parameter
	 * @return value to be set, other than default value.
	 * @author Praveenkumar Paneerselvam
	 * @Refactor Athira
	 */
	public static String getValueToBeSet(FactoryResetSettings factoryResetSetting, String defaultValue, Dut device,
			AutomaticsTapApi tapEnv) {
		String valueToBeSet = null;
		LOGGER.info("Inside method getValueToBeSet()");
		if (WifiSsid.WIFI_SSID_2_4.toString().equalsIgnoreCase(factoryResetSetting.toString())
				|| WifiSsid.WIFI_SSID_5.toString().equalsIgnoreCase(factoryResetSetting.toString())) {
			valueToBeSet = BroadBandTestConstants.STRING_TEST_1.equals(defaultValue)
					? BroadBandTestConstants.STRING_TEST_2
					: BroadBandTestConstants.STRING_TEST_1;
		} else if (LocalGatewayIPv4.LAN_GATEWAY_IP.toString().equalsIgnoreCase(factoryResetSetting.toString())) {
			valueToBeSet = AutomaticsPropertyUtility
					.getProperty(BroadBandPropertyKeyConstants.PROP_KEY_TEST_GATEWAY_LOCAL_IP_1);
		} else if (LocalGatewayIPv4.LAN_DHCP_START.toString().equalsIgnoreCase(factoryResetSetting.toString())) {
			valueToBeSet = AutomaticsPropertyUtility
					.getProperty(BroadBandPropertyKeyConstants.PROP_KEY_TEST_GATEWAY_LOCAL_DHCP_START_ADDR);
		} else if (LocalGatewayIPv4.LAN_DHCP_END.toString().equalsIgnoreCase(factoryResetSetting.toString())) {
			valueToBeSet = AutomaticsPropertyUtility
					.getProperty(BroadBandPropertyKeyConstants.PROP_KEY_TEST_GATEWAY_LOCAL_DHCP_END_ADDR);
		} else if (WifiSettings2Ghz.CHANNELBANDWIDTH.toString().equalsIgnoreCase(factoryResetSetting.toString())) {
			if (DeviceModeHandler.isBusinessClassDevice(device) || DeviceModeHandler.isFibreDevice(device)
					|| DeviceModeHandler.isDSLDevice(device) || DeviceModeHandler.isRPIDevice(device)) {
				valueToBeSet = BroadBandTestConstants.OPERATING_BANDWIDTH_20_MMZ;
			} else {
				valueToBeSet = BroadBandTestConstants.OPERATING_BANDWIDTH_40_MMZ;
			}
		} else if (CodeBig.CODE_BIG_ENABLE.toString().equalsIgnoreCase(factoryResetSetting.toString())) {
			valueToBeSet = BroadBandTestConstants.FALSE;
		} else if (TelemetryEndpoint.TELEMETRY_ENDPOINT_ENABLE.toString()
				.equalsIgnoreCase(factoryResetSetting.toString())) {
			valueToBeSet = BroadBandTestConstants.TRUE;
		} else if (LimitBeaconDetectionEnum.LIMIT_BEACON_DETECTION.toString()
				.equalsIgnoreCase(factoryResetSetting.toString())) {
			valueToBeSet = BroadBandTestConstants.TRUE;
		} else if (RabidMemoryLimit.RABIDMEMORYLIMIT.toString().equalsIgnoreCase(factoryResetSetting.toString())) {
			valueToBeSet = BroadBandTestConstants.STRING_VALUE_TEN;
		} else if (ForceWiFiDisable.FORCEWIFIDISABLE.toString().equalsIgnoreCase(factoryResetSetting.toString())) {
			valueToBeSet = BroadBandTestConstants.TRUE;
		} else {
			String currentPartnerIdName = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
					BroadBandWebPaConstants.WEBPA_PARAM_FOR_SYNDICATION_PARTNER_ID);

			valueToBeSet = (CommonMethods.isNotNull(currentPartnerIdName)
					&& BroadBandCommonUtils.verifySpecificPartnerAvailability(currentPartnerIdName))
							? BroadBandTestConstants.FALSE
							: BroadBandTestConstants.TRUE;
		}
		LOGGER.info("value to be set : " + valueToBeSet);
		LOGGER.info("Inside method getValueToBeSet()");
		return valueToBeSet;
	}

	/**
	 * Utility method to perform and verify factory reset through webpa
	 * 
	 * @param device
	 * @param tapEnv
	 * @return true, if factory reset is successfully performed and also reboot
	 *         reason appears as factory reset in webpa log file
	 */
	// Renaming the API from performFactoryUsingWebpaAndVerifyInWebPaLog to
	// performFactoryUsingWebpaAndVerifyInBootTimeLog

	public static boolean performFactoryUsingWebpaAndVerifyInBootTimeLog(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD : performFactoryUsingWebpaAndVerifyInBootTimeLog");
//		boolean status = BroadBandCommonUtils.performFactoryResetWebPaByPassingTriggerTime(tapEnv, device,
//				BroadBandTestConstants.EIGHT_MINUTE_IN_MILLIS);
		boolean status = BroadBandCommonUtils.performFactoryResetWebPa(tapEnv, device);
		LOGGER.debug("Is Factory Reset through webpa success : " + status);
		if (status) {
			status = BroadBandCommonUtils.verifyFactoryResetReasonFromBootTimeLog(device, tapEnv);
		}
		LOGGER.debug("ENDING METHOD : performFactoryUsingWebpaAndVerifyInBootTimeLog");
		return status;
	}

	/**
	 * Utility method to perform Factory Reset on the device using TR69 & then wait
	 * for the device to comeup.
	 * 
	 * @param tapEnv {@link AutomaticsTapApi}
	 * @param device {@link Dut}
	 * 
	 * @return Boolean representing the result of the Factory Reset Operation.
	 * @author Praveenkumar Paneerselvam
	 * @Refactor Athira
	 */
	public static boolean performFactoryResetTR69(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("ENTERING METHOD performFactoryResetTR69");
		long startTime = System.currentTimeMillis();
		boolean result = false;
		do {
			try {
				result = BroadBandTr69Utils.setTR69ParameterValueUsingRestApi(tapEnv, device,
						WebPaParamConstants.WEBPA_PARAM_FACTORY_RESET, TR69ParamDataType.STRING,
						BroadBandTestConstants.STRING_FOR_FACTORY_RESET_OF_THE_DEVICE);
			} catch (Exception exception) {
				LOGGER.error("Exception occurred while factory reseting the device using TR69.");
			}
		} while (!result && ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.THREE_MINUTE_IN_MILLIS)
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.ONE_MINUTE_IN_MILLIS));
		if (result) {
			BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.TWO_MINUTE_IN_MILLIS);
			result = CommonMethods.waitForEstbIpAcquisition(tapEnv, device);
			if (result) {
				result = BroadBandCommonUtils.verifyFactoryResetReasonFromBootTimeLog(device, tapEnv);
			}
		}
		LOGGER.info("BROAD BAND DEVICE FACTORY RESET (TR69) PERFORMED SUCCESSFULLY: " + result);
		LOGGER.debug("ENDING METHOD performFactoryResetTR69");
		tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
		return result;
	}

	/**
	 * Utility method to perform Factory Reset on the device using WebPA
	 * 
	 * @param tapEnv             {@link AutomaticsTapApi}
	 * @param device             {@link Dut}
	 * @param factoryResetObject String Wifi
	 * @return Boolean representing the result of the Factory Reset Operation.
	 * @refactor Govardhan
	 */
	public static boolean performFactoryResetWebPaForWifi(AutomaticsTapApi tapEnv, Dut device,
			String factoryResetObject) {
		LOGGER.debug("ENTERING METHOD performFactoryResetWebPaForWifi");
		boolean result = false;
		try {
			if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
				BroadBandCommonUtils.getAtomSyncUptimeStatus(device, tapEnv);
			}
			result = BroadBandWiFiUtils.setWebPaParams(device, WebPaParamConstants.WEBPA_PARAM_FACTORY_RESET,
					factoryResetObject, BroadBandTestConstants.CONSTANT_0);
		} catch (Exception exception) {
			LOGGER.error("Exception occurred while factory reseting the device using Webpa.");
		}
		LOGGER.info("BROAD BAND DEVICE FACTORY RESET (WEBPA) PERFORMED SUCCESSFULLY: " + result);
		LOGGER.debug("ENDING METHOD performFactoryResetWebPaForWifi");
		return result;
	}

	/**
	 * Method to perform Factory Reset via Webpa and wait for device to come up
	 * 
	 * @param tapEnv             AutomaticsTapApi Object
	 * @param device             Dut instance
	 * @param factoryResetObject Factory Reset Object
	 * @return true if Device goes for successful Factory Reset and comes up later
	 * @refactor Athira
	 */
	public static boolean methodToPerformFactoryResetObjectAndDeviceToComeUp(AutomaticsTapApi tapEnv, Dut device,
			String factoryResetObject) {
		LOGGER.debug("STARTING METHOD: methodToPerformFactoryResetAndDeviceToComeUp()");
		// Variable declaration starts
		boolean result = false;
		String errorMessage = "";
		// Variable declaration ends
		try {
			if (CommonMethods.isAtomSyncAvailable(device, tapEnv)) {
				BroadBandCommonUtils.getAtomDeviceUptimeStatus(device, tapEnv);
			}
			if (performFactoryResetWebPa(tapEnv, device, factoryResetObject)) {
				long startTime = System.currentTimeMillis();
				long pollTime = BroadBandTestConstants.FIFTEEN_MINUTES_IN_MILLIS;
				do {
					result = CommonMethods.isSTBAccessible(device);
				} while (!result && (System.currentTimeMillis() - startTime) < pollTime && BroadBandCommonUtils
						.hasWaitForDuration(tapEnv, BroadBandTestConstants.ONE_MINUTE_IN_MILLIS));
			}
		} catch (Exception e) {
			errorMessage = "Exception occured while performing Factory Reset via Webpa." + errorMessage;
			LOGGER.error(errorMessage);
		}
		LOGGER.debug("ENDING METHOD: methodToPerformFactoryResetAndDeviceToComeUp");
		return result;
	}

	/**
	 * Utility method to perform Factory Reset on the device using WebPA & then wait
	 * for the device to comeup.
	 * 
	 * @param tapEnv             {@link AutomaticsTapApi}
	 * @param device             {@link Dut}
	 * @param factoryResetObject String Router,Wifi,MoCA,VoIP or Dect
	 * @return Boolean representing the result of the Factory Reset Operation.
	 * @refactor Athira
	 */
	public static boolean performFactoryResetWebPa(AutomaticsTapApi tapEnv, Dut device, String factoryResetObject) {
		LOGGER.debug("STARTING METHOD performFactoryResetWebPa");
		// Variable declaration starts
		boolean result = false;
		String errorMessage = "";
		// Variable declaration ends
		try {
			result = BroadBandWiFiUtils.setWebPaParams(device, WebPaParamConstants.WEBPA_PARAM_FACTORY_RESET,
					factoryResetObject, BroadBandTestConstants.CONSTANT_0);
			if (result) {
				LOGGER.info("Successfully executed webpa command for factory reset.Checking whether box rebooted.");
				long startTime = 0L;
				long pollTime = BroadBandTestConstants.EIGHT_MINUTE_IN_MILLIS;
				startTime = System.currentTimeMillis();
				do {
					result = !CommonMethods.isSTBAccessible(device);
				} while ((System.currentTimeMillis() - startTime) < pollTime && !result && BroadBandCommonUtils
						.hasWaitForDuration(tapEnv, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
				if (result) {
					LOGGER.info("BroadBand Device Factory Reset performed successfully.");
				} else {
					LOGGER.error("BroadBand Device Factory Reset couldn't performed.Device didn't go for reboot.");
				}
			} else {
				LOGGER.error(
						"Unable to perform Factory Reset via Webpa.Error happend while executing WebPa Get command.");
			}
		} catch (Exception exception) {
			errorMessage = "Exception occured while performing Factory Reset." + exception.getMessage();
			LOGGER.error(errorMessage);
		}
		LOGGER.debug("ENDING METHOD performFactoryResetWebPa");
		return result;
	}

	/**
	 * Method to perform Reboot via Lockup methods and wait for device to come up
	 * 
	 * @param tapEnv       AutomaticsTapApi Object
	 * @param device       Dut instance
	 * @param RebootMethod Reboot methods
	 * @return true if Device goes for successful rebooted and comes up later
	 * @refactor Alan_Bivera
	 */
	public static boolean methodToPerformRebootUsingKernelLockUP(AutomaticsTapApi tapEnv, Dut device,
			String RebootMethod) {
		LOGGER.debug("STARTING METHOD: methodToPerformRebootUsingKernelLockUP()");
		// Variable declaration starts
		boolean result = false;
		boolean status = false;
		String errorMessage = null;
		// Variable declaration ends
		try {
			CommonUtils.clearLogFile(tapEnv, device, BroadBandCommandConstants.FILE_PATH_RESET_REASON_LOG);
			tapEnv.executeCommandInSettopBox(device, RebootMethod);
			result = CommonMethods.isSTBRebooted(tapEnv, device, BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS,
					BroadBandTestConstants.CONSTANT_5);
			errorMessage = "Unable to reboot the device using" + RebootMethod + "command";
			if (result) {
				status = CommonMethods.isSTBAccessible(tapEnv, device, BroadBandTestConstants.ONE_MINUTE_IN_MILLIS, 8);
				errorMessage = "Unable to verify that device is up & running ";
				LOGGER.info(errorMessage);
			}

		} catch (Exception e) {
			errorMessage = "Exception occured while performing reboot via " + RebootMethod + " method ";
			LOGGER.error(errorMessage);
		}
		LOGGER.debug("ENDING METHOD: methodToPerformRebootUsingKernelLockUP");
		return status;
	}

}

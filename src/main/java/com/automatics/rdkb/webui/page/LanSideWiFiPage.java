/**
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
package com.automatics.rdkb.webui.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.automatics.device.Dut;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandPropertyKeyConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiElements;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiTestConstant;
import com.automatics.rdkb.webui.utils.BroadBandWebUiUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.CommonMethods;
import com.jcraft.jsch.Logger;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.BroadBandSystemUtils;
import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;
import com.automatics.rdkb.utils.ConnectedNattedClientsUtils;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.CommonUtils;
import com.automatics.rdkb.utils.wifi.connectedclients.BroadBandConnectedClientUtils;

public class LanSideWiFiPage extends LanSideBasePage {

	public LanSideWiFiPage(WebDriver lanDriver) {
		setDriver(lanDriver);
	}

	/**
	 * Utility method to select Connection from Gateway menu in GUI Home page
	 * 
	 * @param device Dut instance
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @return true if navigated to connection page and verified title
	 * 
	 * @author Ashiwn Sankara
	 * @refactor Athira
	 */
	public boolean navigateToConnectionFromGateway(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: navigateToConnection");
		// Variable declaration starts
		boolean result = false;
		String pageTitle = "";
		String errorMessage = "";
		// Variable declaration ends
		try {
			pageTitle = BroadbandPropertyFileHandler.getAtAGlancePageTitle();
			driver.findElement(By.linkText(BroadBandWebGuiTestConstant.LINK_TEXT_CONNECTION)).click();
			tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
			result = BroadBandWebUiUtils.validatePageLaunchedStatusWithPageTitle(driver, pageTitle);
		} catch (Exception exception) {
			errorMessage = "Exception occured while clicking on Connection Link." + exception.getMessage();
			LOGGER.error(errorMessage);
		}
		LOGGER.debug("ENDING METHOD: navigateToConnection");
		return result;
	}

	/**
	 * Utility method to select WiFi from Gateway -> Connection menu in GUI Home
	 * page
	 * 
	 * @param settop settop instance
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @return true if navigated to wifi page and verified title
	 * 
	 * @author Ashiwn Sankara
	 * @refactor Govardhan
	 */
	public boolean navigateToWiFiPage(Dut device, AutomaticsTapApi tapEnv) {
		LOGGER.debug("Entering method: navigateToWiFiPage");
		// Variable declaration starts
		boolean result = false;
		String captivePortalLogo = "";
		String pageTitle = "";
		String errorMessage = "";
		boolean isJst = false;
		// Variable declaration ends
		try {

			captivePortalLogo = AutomaticsTapApi
					.getSTBPropsValue(BroadBandPropertyKeyConstants.STRING_LOGO_CAPTIVEPORTAL);
			LOGGER.info("captivePortalLogo in STEP 2 is" + captivePortalLogo);
			if (CommonMethods.isNotNull(captivePortalLogo)) {
				pageTitle = BroadBandWebGuiTestConstant.PAGE_TITLE_WIFI_SNDTE
						.replace(BroadBandTestConstants.STRING_REPLACE, captivePortalLogo);
				LOGGER.info("pageTitle in STEP 2 is" + pageTitle);
			}
			isJst = BroadBandCommonUtils.isWebUiUsesJst(tapEnv, device);
			// Clicking on WiFi link
			driver.findElement(By.xpath((isJst)
					? BroadBandWebGuiTestConstant.XPATH_LAN_UI_WIFI_PAGE
							.replace(BroadBandWebGuiTestConstant.DOT_PHP, BroadBandWebGuiTestConstant.DOT_JST)
					: BroadBandWebGuiTestConstant.XPATH_LAN_UI_WIFI_PAGE)).click();
			tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);

			result = BroadBandWebUiUtils.validatePageLaunchedStatusWithPageTitle(driver, pageTitle);
		} catch (Exception exception) {
			errorMessage = "Exception occured while navigating to WiFi page in LAN Admin GUI." + exception.getMessage();
			LOGGER.error(errorMessage);
		}
		LOGGER.debug("Exiting method: navigateToWiFiPage");
		return result;
	}

	/**
	 * Method to Verify channel bandwidth options in wifi edit page
	 * 
	 * @param device
	 * @param tapEnv
	 * @param is5Ghz
	 * @return status
	 * 
	 * @refactor Athira
	 */
	public BroadBandResultObject verifyBandwidthOptionsInWifiPage(Dut device, AutomaticsTapApi tapEnv, boolean is5Ghz) {
		boolean status = false;
		LOGGER.debug("Starting method: verifyBandwidthOptionsInWifiPage");
		String band20Ghz, band40Ghz, band80Ghz;
		BroadBandResultObject broadBandResultObject = new BroadBandResultObject();
		String errorMessage = "Unable to navigate to wifi edit page";
		try {
			if (navigateToWiFiEditPage(device, tapEnv, is5Ghz)) {
				band20Ghz = LanSideBasePage
						.getText(By.xpath(BroadBandWebGuiElements.ELEMENT_XPATH_CHANNEL_BANDWIDTH_20MHZ));
				band40Ghz = LanSideBasePage
						.getText(By.xpath(BroadBandWebGuiElements.ELEMENT_XPATH_CHANNEL_BANDWIDTH_40MHZ));
				if (is5Ghz) {
					errorMessage = "Unable to validate bandwidth options in 5Ghz wifi edit page";
					band80Ghz = LanSideBasePage
							.getText(By.xpath(BroadBandWebGuiElements.ELEMENT_XPATH_CHANNEL_BANDWIDTH_80MHZ));
					if (CommonUtils.patternSearchFromTargetString(band20Ghz, BroadBandTestConstants.STRING_VALUE_20)
							&& CommonUtils.patternSearchFromTargetString(band40Ghz,
									BroadBandWebGuiTestConstant.STRING_CHANNEL_BANDWIDTH_20_40)
							&& CommonUtils.patternSearchFromTargetString(band80Ghz,
									BroadBandWebGuiTestConstant.STRING_CHANNEL_BANDWIDTH_20_40_80)) {
						status = true;
					}
				} else {
					errorMessage = "Unable to validate bandwidth options in 2.4Ghz wifi edit page";
					if (CommonUtils.patternSearchFromTargetString(band20Ghz, BroadBandTestConstants.STRING_VALUE_20)
							&& CommonUtils.patternSearchFromTargetString(band40Ghz,
									BroadBandWebGuiTestConstant.STRING_CHANNEL_BANDWIDTH_20_40)) {
						status = true;
					}
				}
			}
			navigateToWiFiPage(device, tapEnv);
		} catch (Exception exception) {
			LOGGER.error("Exception in Verifing bandwidth in Edit page " + exception.getMessage());
		}
		LOGGER.debug("Ending method: verifyBandwidthOptionsInWifiPage");
		broadBandResultObject.setErrorMessage(errorMessage);
		broadBandResultObject.setStatus(status);
		return broadBandResultObject;
	}

	/**
	 * Utility method to navigate to 2.4G or 5G WiFi edit page from WiFi page
	 * 
	 * @param settop settop instance
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @param is5Ghz boolean value specifies to navigate to 5Ghz if true else 2.4Ghz
	 * 
	 * @return true if navigated to 2.4G or 5G WiFi edit page and verified title
	 * 
	 * @author Ashiwn Sankara
	 * @refactor Govardhan
	 */
	public boolean navigateToWiFiEditPage(Dut device, AutomaticsTapApi tapEnv, boolean is5Ghz) {
		LOGGER.debug("Entering method: navigateToWiFiEditPage");
		// Variable declaration starts
		boolean result = false;
		String pageTitle = "";
		String captivePortalLogo = "";
		String errorMessage = "";
		boolean isJst = false;
		String xpath = null;
		// Variable declaration ends

		try {
			if (is5Ghz) {
				// captivePortalLogo = BroadBandSystemUtils.getCaptivePortalLogo(device,
				// tapEnv);
				captivePortalLogo = AutomaticsTapApi
						.getSTBPropsValue(BroadBandPropertyKeyConstants.STRING_LOGO_CAPTIVEPORTAL);
				if (CommonMethods.isNull(captivePortalLogo)) {
					return result;
				}
				pageTitle = BroadBandWebGuiTestConstant.PAGE_TITLE_WIFI_5EDIT_PAGE
						.replace(BroadBandTestConstants.STRING_REPLACE, captivePortalLogo);
			} else {

				// captivePortalLogo = BroadBandSystemUtils.getCaptivePortalLogo(device,
				// tapEnv);
				captivePortalLogo = AutomaticsTapApi
						.getSTBPropsValue(BroadBandPropertyKeyConstants.STRING_LOGO_CAPTIVEPORTAL);
				if (CommonMethods.isNull(captivePortalLogo)) {
					return result;
				}
				pageTitle = BroadBandWebGuiTestConstant.PAGE_TITLE_WIFI_24EDIT_PAGE
						.replace(BroadBandTestConstants.STRING_REPLACE, captivePortalLogo);
			}
			// Clicking on WiFi Edit button based on SSID(2.4GHz or 5GHz)
			isJst = BroadBandCommonUtils.isWebUiUsesJst(tapEnv, device);
			xpath = (is5Ghz ? BroadBandWebGuiElements.ELEMENT_XPATH_WIFI_5GHZ_EDIT
					: BroadBandWebGuiTestConstant.XPATH_WIFI_2_4GHz_EDIT_PAGE);
			if (isJst) {
				xpath = xpath.replace(BroadBandWebGuiTestConstant.DOT_PHP, BroadBandWebGuiTestConstant.DOT_JST);
			}
			driver.findElement(By.xpath(xpath)).click();
			tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
			result = BroadBandWebUiUtils.validatePageLaunchedStatusWithPageTitle(driver, pageTitle);
		} catch (Exception exception) {
			errorMessage = "Exception occured while navigating to WiFi Edit page." + exception.getMessage();
			LOGGER.error(errorMessage);
		}
		LOGGER.debug("Exiting method: navigateToWiFiEditPage");
		return result;
	}
	
    /**
     * Method to edit wifi SSID and password Save settings
     * 
     * @param settop
     * @param tapEnv
     * @param driver
     * @param networkName
     * @param networkPassword
     * @param is5Ghz
     * @param bandwidthOption
     * @return status
     * @author Ashiwn Sankara
     * @refactor Govardhan
     */
    public BroadBandResultObject editWifiPageAndSaveSettings(Dut device, AutomaticsTapApi tapEnv, WebDriver driver,
	    String networkName, String networkPassword, boolean is5Ghz, String bandwidthOption) {
	LOGGER.debug("Starting method: editWifiPageAndSaveSettings");
	boolean status = false;
	String errorMessage = "Unable to navigate to wifi edit page";
	BroadBandResultObject broadBandResultObject = new BroadBandResultObject();
	try {
	    if (navigateToWiFiEditPage(device, tapEnv, is5Ghz)) {
		errorMessage = "Unable to edit wifi settings in wifi edit page";
		driver.findElement(By.id(BroadBandWebGuiElements.ELEMENT_ID_WIFI_EDIT_PAGE_SSID_NAME)).clear();
		LanSideWiFiPage.sendKeys(By.id(BroadBandWebGuiElements.ELEMENT_ID_WIFI_EDIT_PAGE_SSID_NAME),
			networkName);
		driver.findElement(By.id(BroadBandWebGuiElements.ELEMENT_ID_SHOW_PWD_CHECKBOX)).click();
		driver.findElement(By.id(BroadBandWebGuiElements.ELEMENT_ID_WIFI_EDIT_PAGE_SSID_PASSWORD)).clear();
		LanSideWiFiPage.sendKeys(By.id(BroadBandWebGuiElements.ELEMENT_ID_WIFI_EDIT_PAGE_SSID_PASSWORD),
			networkPassword);
		// Add channel bandwidth
		LanSideWiFiPage.click(By.id(bandwidthOption));
		LanSideWiFiPage.click(By.id(BroadBandWebGuiElements.ELEMENT_ID_WIFI_EDIT_PAGE_SAVE_SETTING));
		LOGGER.info("Waiting for two minutes to affect changes");
		tapEnv.waitTill(BroadBandTestConstants.TWO_MINUTE_IN_MILLIS);
		driver.navigate().refresh();
		status = true;
	    }
	} catch (Exception exception) {
	    LOGGER.error("Exception in Saving wifi settings \n" + exception.getMessage());
	}
	LOGGER.debug("Ending method: editWifiPageAndSaveSettings");
	broadBandResultObject.setErrorMessage(errorMessage);
	broadBandResultObject.setStatus(status);
	return broadBandResultObject;
    }
}

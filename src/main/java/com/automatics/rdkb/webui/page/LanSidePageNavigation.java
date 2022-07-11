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

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.connection.handler.SeleniumNodeConnectionHandler;
import com.automatics.device.Device;
import com.automatics.device.Dut;
import com.automatics.enums.Browser;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.constants.BroadBandPropertyKeyConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.wifi.connectedclients.BroadBandConnectedClientUtils;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiElements;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiTestConstant;
import com.automatics.rdkb.webui.exception.PageTitleNotFoundException;
import com.automatics.rdkb.webui.utils.BroadBandWebUiUtils;
import com.automatics.selenium.CustomizableBrowserCapabilities;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.CommonMethods;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;
import com.automatics.rdkb.utils.CommonUtils;

public class LanSidePageNavigation {

	/** SLF4j logger. */
	public static final Logger LOGGER = LoggerFactory.getLogger(LanSidePageNavigation.class);

	/**
	 * The driver instance
	 */
	protected static WebDriver driver;

	@FindBy(id = "username")
	private static WebElement userName;
	@FindBy(id = "password")
	private static WebElement password;
	@FindBy(className = "btn")
	private static WebElement logInButton;

	public LanSidePageNavigation(WebDriver lanDriver) {
		setDriver(lanDriver);
	}

	/**
	 * Method to click the web element ID
	 * 
	 */
	public static void click(By elementId) {
		driver.findElement(elementId).click();
	}

	public static void setDriver(WebDriver driver) {
		LanSideBasePage.driver = driver;
	}

	/**
	 * Method to get the current driver instance
	 * 
	 * @return driver instance
	 */
	public WebDriver getWebDriver() {
		return driver;
	}

	/**
	 * Method to verify enabled status for certain fields
	 * 
	 */
	public boolean isEnabled(By elementId) throws Exception {
		return driver.findElement(elementId).isEnabled();
	}

	/**
	 * Method to refresh current web page
	 * 
	 */
	public void refresh() {
		driver.navigate().refresh();
	}

	public static void launchAdminPage(String url) {
		driver.get(url);
	}

	public static void setUserName(String strUserName) {
		userName.sendKeys(strUserName);
	}

	public static void setPassword(String strPassword) {
		password.sendKeys(strPassword);
	}

	/**
	 * Method to close driver
	 */
	public static void closeBrowser() {
		if (null != driver) {
			driver.quit();
		}
	}

	
	/**
	 * Utility method to navigate To Wizard from Gateway menu in MSO GUI Home page
	 * 
	 * @param settop settop instance
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @param driver instance of {@link Webdriver}
	 * @return True,if navigated and title verified Successfully
	 * @refactor Govardhan
	 */
	public boolean navigateToWizardStep1Page(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
		LOGGER.debug("STARTING METHOD : navigateToWizardStep1Page()");
		boolean result = false;
		try {
			result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
					BroadbandPropertyFileHandler.getPageLinkTextForHardwareWizardPage(),
					BroadbandPropertyFileHandler.getPageUrlForHardwareWizardPage(),
					BroadbandPropertyFileHandler.getPageTitleForHardwareWizardPage());

		} catch (Exception e) {
			LOGGER.error("Exception Occured in navigateToWizardStep1Page():" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : navigateToWizardStep1Page()");
		return result;
	}

	/**
	 * Utility method to navigate WiFi Configuration Page from Gateway menu in LAN
	 * GUI Home page
	 * 
	 * @param device device instance
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @param driver instance of {@link Webdriver}
	 * @return True,if navigated and title verified Successfully
	 * @refactor Govardhan
	 */
	public boolean navigateToWiFiConfigurationPage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
		LOGGER.debug("STARTING METHOD : navigateToWiFiConfigurationPage()");
		boolean result = false;
		try {
			result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
					BroadBandWebGuiTestConstant.LINK_TEXT_CONNECTION_WIFI,
					BroadBandWebGuiTestConstant.STRING_WIFI_PAGE_URL,
					BroadbandPropertyFileHandler.getWiFiConfigurationPageTitle());
		} catch (Exception e) {
			LOGGER.error("Exception Occured in navigateToWiFiConfigurationPage():" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : navigateToWiFiConfigurationPage()");
		return result;
	}

	/**
	 * Utility method to navigate WiFi Edit Page in MSO Web GUI
	 * 
	 * @param device Dut instance
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @param driver instance of {@link Webdriver}
	 * @return True,if navigated and title verified Successfully
	 * @refactor Govardhan
	 */
	public boolean navigateToPrivateWiFiEditPage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver,
			String wifiBand) {
		LOGGER.debug("STARTING METHOD : navigateToPrivateWiFiEditPage()");
		boolean status = false;
		String pageTitle = null;
		boolean isJst = false;
		String xpath = null;
		try {
			isJst = BroadBandCommonUtils.isWebUiUsesJst(tapEnv, device);
			xpath = (wifiBand.equalsIgnoreCase(BroadBandTestConstants.BAND_5GHZ)
					? BroadBandWebGuiElements.ELEMENT_XPATH_WIFI_5GHZ_EDIT
					: BroadBandWebGuiTestConstant.XPATH_WIFI_2_4GHz_EDIT_PAGE);
			if (isJst) {
				xpath = xpath.replace(BroadBandWebGuiTestConstant.DOT_PHP, BroadBandWebGuiTestConstant.DOT_JST);
			}
			driver.findElement(By.xpath(xpath)).click();
			tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
			if (wifiBand.equalsIgnoreCase(BroadBandTestConstants.BAND_5GHZ)) {
				pageTitle = AutomaticsTapApi.getSTBPropsValue(BroadBandWebGuiTestConstant.UI_WIFI_5_GHZ_EDIT_PAGE_TITLE);
			} else {
				pageTitle = AutomaticsTapApi.getSTBPropsValue(BroadBandWebGuiTestConstant.UI_WIFI_2_4_GHZ_EDIT_PAGE_TITLE);
			}
			status = BroadBandWebUiUtils.validatePageLaunchedStatusWithPageTitle(driver, pageTitle);
		} catch (Exception e) {
			LOGGER.error("Exception Occured in navigateToPrivateWiFiEditPage():" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : navigateToPrivateWiFiEditPage()");
		return status;
	}

	/**
	 * Utility method to navigate Connected Devices Page from Gateway menu in LAN
	 * GUI Home page
	 * 
	 * @param device Dut instance
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @param driver instance of {@link Webdriver}
	 * @return True,if navigated and title verified Successfully
	 * 
	 * @Refactor Sruthi Santhosh
	 */
	public boolean navigateToConnectedDevicesPage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
		LOGGER.debug("STARTING METHOD : navigateToConnectedDevicesPage()");
		boolean result = false;
		try {

			result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
					BroadbandPropertyFileHandler.getLinkTextForConnectedDevicesPage(),
					BroadbandPropertyFileHandler.getPageUrlForConnectedDevicesPage(),
					BroadbandPropertyFileHandler.getPageTitleForConnectedDevicesPage());

		} catch (Exception e) {
			LOGGER.error("Exception Occured in navigateToConnectedDevicesPage():" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : navigateToConnectedDevicesPage()");
		return result;
	}
	
	/**
	 * Utility method to verify the page launch status by using link text or launch
	 * Url
	 * 
	 * @param device      instance of {@link Dut}
	 * @param tapEnv      instance of {@link AutomaticsTapApi}
	 * @param driver      instance of {@link Webdriver}
	 * @param linkText    Link text to be launched
	 * @param phpPageName PHp file name to be launched
	 * @param pageTitle   Page title to be verified
	 * 
	 * @return True ,If Page is navigated and title verified .
	 * @refactor Said Hisham
	 */
	public boolean verifyPageLaunchedStatusUseLinkOrlanuchUrl(Dut device, AutomaticsTapApi tapEnv, WebDriver driver,
			String linkText, String phpPageName, String pageTitle) {
		LOGGER.info("STARTING METHOD : verifyPageLaunchedStatusUseLinkOrlanuchUrl()");
		boolean result = false;
		boolean isJst = false;
		boolean isWizardLinkAvailable = false;
		String jstPageName = null;
		String deviceIpAddress = null;
		boolean isBusinessClsDevice = DeviceModeHandler.isBusinessClassDevice(device);
		try {
			LOGGER.info("LINK TEXT :" + linkText);
			LOGGER.info("driver :" + driver);
			LOGGER.info("PAGE TITLE BEFORE LINK TEXT CLICK :" + driver.getTitle());
			if (linkText.equalsIgnoreCase(BroadBandWebGuiTestConstant.LINK_TEXT_HARDWARE_WIZARD)) {
				LOGGER.info("Elememts are " + driver.findElements(By.linkText(linkText)).size());
				if (driver.findElements(By.linkText(linkText)).size() != 0) {
					isWizardLinkAvailable = true;
					BufferedImage img = BroadBandWebUiUtils.captureCurrentScreenFromDriver(driver);
					if (null != img) {
						String imageName = System.currentTimeMillis() + "_" + "UI_Checking_wizard_link_error";
						AutomaticsTapApi.saveImages(device, img, imageName);
					}
					LOGGER.error("Wizard hyper link is present on UI side nav bar");
				} else {
					LOGGER.info("Successfully Verified that Wizard link is not available on UI side nav bar");
				}
			} else {
				driver.findElement(By.linkText(linkText)).click();
				tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
				LOGGER.info("PAGE TITLE AFTER LINK TEXT CLICKED :" + driver.getTitle());
				result = BroadBandWebUiUtils.validatePageLaunchedStatusWithPageTitle(driver, pageTitle);
			}
		} catch (Exception e) {
			result = false;
			LOGGER.error("Excepton Occured using Link Text,Launching URL for " + linkText);
		}
		if (!result && !isWizardLinkAvailable) {
			try {
				isJst = BroadBandCommonUtils.isWebUiUsesJst(tapEnv, device);
				if (isJst) {
					jstPageName = phpPageName.replace(BroadBandWebGuiTestConstant.DOT_PHP,
							BroadBandWebGuiTestConstant.DOT_JST);
					LOGGER.info("JST PAGE NAME :" + jstPageName);
				} else if (isBusinessClsDevice) {
					jstPageName = phpPageName.replace(BroadBandWebGuiTestConstant.DOT_PHP,
							BroadBandWebGuiTestConstant.DOT_JST);
					LOGGER.info("JST PAGE NAME : " + jstPageName);
				} else {
					LOGGER.info("PHP PAGE NAME :" + phpPageName);
				}
				if (DeviceModeHandler.isDSLDevice(device)) {
					deviceIpAddress = BroadBandTestConstants.LAN_LOCAL_IP;

				} else {
					if (BroadBandCommonUtils.isSecureWebUIEnabled(device, tapEnv)) {
						deviceIpAddress = BroadBandTestConstants.GATEWAY_ADMIN_PAGE_SECURE_UI_DOMAIN;
					} else {
						deviceIpAddress = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
								BroadBandWebPaConstants.WEBPA_PARAM_GATEWAY_IPV4_ADDRESS);
					}
				}
				String webUiUrl = BroadBandCommonUtils.concatStringUsingStringBuffer(
						BroadBandWebGuiTestConstant.STRING_HTTP, deviceIpAddress, (isJst) ? jstPageName : phpPageName);
				LOGGER.info("URL TO LAUNCH :" + webUiUrl);
				if (CommonMethods.isNotNull(webUiUrl)) {
					LOGGER.info("Driver running is : " + driver);
					driver.navigate().to(webUiUrl);
					tapEnv.waitTill(BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS);
					result = BroadBandWebUiUtils.validatePageLaunchedStatusWithPageTitle(driver, pageTitle);
				}
			} catch (Exception e) {
				result = false;
				LOGGER.error("Excepton Occured using launching URL" + e.getMessage());
			}
		}
		LOGGER.debug("ENDING METHOD : verifyPageLaunchedStatusUseLinkOrlanuchUrl()");
		return result;
	}

	/**
	 * Utility method to navigate Local Ip Configuration Page from Gateway menu in
	 * LAN GUI Home page
	 * 
	 * @param device device instance
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @param driver instance of {@link Webdriver}
	 * @return True,if navigated and title verified Successfully
	 * @refactor Said Hisham
	 */
	public boolean navigateToLocalIpConfigurationPage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
		LOGGER.debug("STARTING METHOD : navigateToLocalIpConfigurationPage()");
		
		boolean result = false;
		
		try {
			result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
					BroadbandPropertyFileHandler.getLinkTextForLocalIpNetwork(),
					BroadbandPropertyFileHandler.getPageUrlForLocalIpNetwork(),
					BroadbandPropertyFileHandler.getPageTitleForLocalIpNetwork());
		} catch (Exception e) {
			LOGGER.error("Exception Occured in navigateToLocalIpConfigurationPage():" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : navigateToLocalIpConfigurationPage()");
		return result;
	}

	/**
	 * Utility method to navigate Partner Network Page from Gateway menu in LAN GUI
	 * Home page
	 * 
	 * @param device device instance
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @param driver instance of {@link Webdriver}
	 * @return True,if navigated and title verified Successfully
	 * @refactor Said Hisham
	 */
	public boolean navigateToPartnerNetworkPage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver,
			boolean isBusinessDevice) {
		LOGGER.debug("STARTING METHOD : navigateToPartnerNetworkPage()");
		
		boolean result = false;
		
		try {
			
			result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
					BroadbandPropertyFileHandler.getLinkTextForPartnerNetwork(),
					BroadbandPropertyFileHandler.getPageUrlForPartnerNetwork(),
					BroadbandPropertyFileHandler.getPageTitleForPartnerNetwork());
		} catch (Exception e) {
			LOGGER.error("Exception Occured in navigateToPartnerNetworkPage():" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : navigateToPartnerNetworkPage()");
		return result;
	}
	
	/**
	 * Utility method to navigate Connection page from Gateway menu in LAN GUI Home
	 * page
	 * 
	 * @param device device instance
	 * @param tapEnv instance of {@link AutomaticsTapApi}
	 * @param driver instance of {@link Webdriver}
	 * @return True,if navigated and title verified Successfully
	 * @refactor Said Hisham
	 */
	public boolean navigateToConnectionStatusPage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
		LOGGER.debug("STARTING METHOD : navigateToConnectionStatusPage()");
		
		boolean result = false;
		
		try {
			result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
					BroadbandPropertyFileHandler.getLinkTextForConnectionStatus(),
					BroadbandPropertyFileHandler.getPageUrlForConnectionStatus(),
					BroadbandPropertyFileHandler.getPageTitleForConnectionStatus());

		} catch (Exception e) {
			LOGGER.error("Exception Occured in navigateToConnectionStatusPage():" + e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : navigateToConnectionStatusPage()");
		return result;
	}


}

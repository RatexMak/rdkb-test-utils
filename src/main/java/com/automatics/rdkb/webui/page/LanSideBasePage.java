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

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.connection.handler.SeleniumNodeConnectionHandler;
import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Device;
import com.automatics.device.Dut;
import com.automatics.enums.Browser;
import com.automatics.rdkb.constants.BroadBandConnectedClientTestConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.constants.RDKBTestConstants.WiFiFrequencyBand;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;
import com.automatics.rdkb.utils.CommonUtils;
import com.automatics.rdkb.utils.ConnectedNattedClientsUtils;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.wifi.connectedclients.BroadBandConnectedClientUtils;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiElements;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiTestConstant;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.CommonMethods;

public class LanSideBasePage {
    /**
     * Logger instance for all WebGUI pages.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(LanSideBasePage.class);
    
    /**
     * The driver instance
     */
    protected static WebDriver driver;

    
    /**
     * Method to get the current web UI page Title
     * 
     * @return current web page Title
     */
    public static String getCurrentTitle() {
	return driver.getTitle();
    }
    
    public static void launchAdminPage(String url) {
	driver.get(url);
    }
    
    public static void setDriver(WebDriver driver) {
	LanSideBasePage.driver = driver;
    }
    
    public static WebDriver getDriver() {
	return driver;
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
     * Method to verify tick mark enabled status
     * 
     */
    public static boolean isSelected(By elementId) throws Exception {
	return driver.findElement(elementId).isSelected();
    }

    /**
     * Method to send user input to certain web element ID
     * 
     */
    public static void sendKeys(By elementId, String value) {
	driver.findElement(elementId).sendKeys(value);
    }

    /**
     * Method to click the web element ID
     * 
     */
    public static void click(By elementId) {
	driver.findElement(elementId).click();
    }
    
    /**
     * Method to get the text from web page
     * 
     */
    public static String getText(By elementId) throws Exception {
	return driver.findElement(elementId).getText();
    }
    
    /**
     * Method to give explicit wait
     * 
     */
    public static void waitForTextToAppear(String textToAppear, By element) {
	WebDriverWait wait = new WebDriverWait(driver, 30);
	wait.until(ExpectedConditions.textToBePresentInElementLocated(element, textToAppear));
    }
    
    /**
     * Method to verify whether certain element is displayed or not
     * 
     */
    public static boolean isDisplayed(By elementId) throws Exception {
	return driver.findElement(elementId).isDisplayed();
    }
    
    /**
     * Method to verify whether gateway page is redirected to Captive Portal
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link Dut}
     * @param connectedClientSettop
     *            Connected client device instance
     * @param partner
     *            partner id
     *
     * @return true if Captive Portal page is launched
     * 
     *
     */
    public static boolean isCaptivePortalPageLaunched(AutomaticsTapApi tapEnv, Dut connectedClientSettop, String url,
	    String partner) {
	LOGGER.debug("STARTING METHOD : isCaptivePortalPageLaunched()");
	boolean status = false;
	try {
	    if (CommonMethods.isNotNull(url)) {
		// Invoke Browser
		invokeBrowserinConnectedClient(connectedClientSettop);
		// Launch URL
		LOGGER.info("Accessing URL : " + url);
		launchAdminPage(url);
		// status = verifyLaunchedPageTitle(BroadBandWebGuiTestConstant.CAPTIVE_PORTAL_PAGE_TITLE);
		// status = verifyLaunchedPageTitle(BroadBandWebUiUtils.getCaptivePageTitleForSynPartDev(partner,
		// true));
		status = verifyLaunchedPageTitle(
			BroadbandPropertyFileHandler.getCaptivePageTitleForSyndicatePartners(partner));
	    }
	} catch (Exception e) {
	    LOGGER.error("EXCEPTION OCCURED WHILE VALIDATING CAPTIVE PORTAL LAUNCH STATUS:" + e.getMessage());
	}

	LOGGER.info("IS CAPTIVE PORTAL PAGE LAUNCHED : " + status);
	LOGGER.debug("ENDING METHOD : isCaptivePortalPageLaunched()");
	return status;
    }  
    
    /**
     * Method to invoke browser in the Connected Client PC
     * 
     * @param connectedClientSettop
     *            Connected client device instance
     * @refactor Govardhan
     */
    public static void invokeBrowserinConnectedClient(Dut clientSettop) {
	try {
	    BroadBandConnectedClientUtils.verifyBrowserAndDriverCapabilityOnConnectedClient(clientSettop);
	    setBrowserAndDriverCapabilities(clientSettop, null);
	    PageFactory.initElements(driver, new LanSideBasePage());
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	} catch (Exception e) {
	    LOGGER.error("Exception occured invokeBrowserinConnectedClient:" + e.getMessage());
	}
    }
    
    /**
     * Utility method used to set the browser and driver capabilities
     * 
     * @param clientSettop
     *            Connected client device instance
     * @param browser
     *            Browser object
     * @refactor Govardhan
     */
    public static void setBrowserAndDriverCapabilities(Dut clientDevice, Browser browser) {
	SeleniumNodeConnectionHandler seleniumNode = null;
	// CustomizableBrowserCapabilities capabilities = null; //Commented to remove build error
	Platform platform = null;
	Browser defaultBrowser = null;
	try {
	    Device ecatsDevice = (Device) clientDevice;
	    seleniumNode = new SeleniumNodeConnectionHandler();
	    if (ecatsDevice.isLinux() || ecatsDevice.isRaspbianLinux()) {
		platform = Platform.LINUX;
		defaultBrowser = Browser.FIREFOX;
	    } else if (ecatsDevice.isWindows()) {
		platform = Platform.WINDOWS;
		defaultBrowser = Browser.CHROME;
	    } else if (ecatsDevice.isMacOS()) {
		platform = Platform.MAC;
		defaultBrowser = Browser.SAFARI;
	    }
	    if (browser != null) {
		// capabilities = new CustomizableBrowserCapabilities(browser, platform, (Device) clientSettop);
		// //Commented to remove build error
		// setDriver(seleniumNode.invokeBrowserInNode(clientSettop, browser, capabilities));
		setDriver(seleniumNode.invokeBrowserInNode(clientDevice, browser));
	    } else {
		// capabilities = new CustomizableBrowserCapabilities(defaultBrowser, platform, (Device) clientSettop);
		// //Commented to remove build error
		// setDriver(seleniumNode.invokeBrowserInNode(clientSettop, defaultBrowser, capabilities));
		setDriver(seleniumNode.invokeBrowserInNode(clientDevice, defaultBrowser));
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception occured in setBrowserAndDriverCapabilities():" + e.getMessage());
	}
    }
    
    /**
     * Method to verify whether gateway page is redirected to Captive Portal
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link Dut}
     * @param connectedClientSettop
     *            Connected client Dut instance
     * @param partner
     *            partner Id of the device
     *
     * @return true if Captive Portal page is launched
     * @refactor Govardhan
     */
    public static boolean isCaptivePortalPageLaunched(AutomaticsTapApi tapEnv, Dut device, Dut connectedClientSettop,
	    String partner) {
	LOGGER.debug("STARTING METHOD : isCaptivePortalPageLaunched()");
	boolean status = false;
	String gatewayUrl = null; // Variable to store URL
	try {
	    // Invoke Browser
	    invokeBrowserinConnectedClient(connectedClientSettop);
	    gatewayUrl = DeviceModeHandler.isBusinessClassDevice(device)
		    ? AutomaticsPropertyUtility.getProperty(BroadBandWebGuiTestConstant.ADMIN_PAGE_URL_BUSINESS_CLASS)
		    : AutomaticsPropertyUtility.getProperty(BroadBandWebGuiTestConstant.ADMIN_PAGE_URL);
	    // Launch URL
	    launchAdminPage(gatewayUrl);
	    status = verifyLaunchedPageTitle(
		    BroadbandPropertyFileHandler.getCaptivePageTitleForSyndicatePartners(partner));
	} catch (Exception e) {
	    LOGGER.error("EXCEPTION OCCURED WHILE VALIDATING CAPTIVE PORTAL LAUNCH STATUS:" + e.getMessage());
	}

	LOGGER.info("IS CAPTIVE PORTAL PAGE LAUNCHED : " + status);
	LOGGER.debug("ENDING METHOD : isCaptivePortalPageLaunched()");
	return status;
    }
    
    /**
     * Method to verify the launched page title with expected title
     * 
     * @param expectedTitle
     * @return true if the launched page title matches with expected page title
     * 
     * @Refactor Sruthi Santhosh
     */
    public static boolean verifyLaunchedPageTitle(String expectedTitle) {
	LOGGER.debug("STARTING METHOD : verifyLaunchedPageTitle()");
	boolean result = false;
	try {
	    String launchedPageTitle = getCurrentTitle();
	    LOGGER.info("TITLE OF CURRENT LAUNCHED PAGE : " + launchedPageTitle + " EXPECTED TITLE : " + expectedTitle);
	    if (CommonMethods.isNotNull(launchedPageTitle) && CommonMethods.isNotNull(expectedTitle)) {
		result = CommonUtils.patternSearchFromTargetString(launchedPageTitle.toUpperCase(),
			expectedTitle.toUpperCase());
	    }
	} catch (Exception e) {
	    LOGGER.error("EXCEPTION OCCURED WHILE VALIDATING PAGE TITLE:" + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : verifyLaunchedPageTitle()");
	return result;
    }
    
    /**
     * Method to configure private Wi-Fi network in captive portal page
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link Dut}
     * @return true if the configuration is successful
     * @refactor Govardhan
     */
    public static boolean configureWifiNetworkInCapPortal(AutomaticsTapApi tapEnv, Dut device) {

	boolean status = false;
	boolean configStatus = false;
	try {
	    String privateWifiSsid2Ghz = AutomaticsPropertyUtility
		    .getProperty(BroadBandTestConstants.PROP_KEY_2_4_PRIVATE_SSID);
	    privateWifiSsid2Ghz = privateWifiSsid2Ghz.replace(BroadBandTestConstants.PLACE_HOLDER_FOR_MAC_ADDRESS,
		    device.getHostMacAddress());
	    privateWifiSsid2Ghz = privateWifiSsid2Ghz.replace(BroadBandTestConstants.DOUBLE_QUOTE,
		    BroadBandTestConstants.EMPTY_STRING);
	    privateWifiSsid2Ghz = privateWifiSsid2Ghz.replace(AutomaticsConstants.COLON,
		    BroadBandTestConstants.EMPTY_STRING);
	    String privateWifiPassword = AutomaticsPropertyUtility
		    .getProperty(BroadBandTestConstants.PROP_KEY_2_4_PRIVATE_SSID_PWD);
	    privateWifiPassword = privateWifiPassword.replace(BroadBandTestConstants.DOUBLE_QUOTE,
		    BroadBandTestConstants.EMPTY_STRING);
	    String privateWifiSsid = CommonMethods.patternFinder(privateWifiSsid2Ghz,
		    BroadBandTestConstants.REGEX_MODEL_FOR_REMOVING_WIFIBAND);
	    String PartnerIdName = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
		    BroadBandWebPaConstants.WEBPA_PARAM_FOR_SYNDICATION_PARTNER_ID);

	    if (CommonMethods.isNotNull(privateWifiSsid) && CommonMethods.isNotNull(privateWifiPassword)) {
		// clicking start up button
		long startTime = System.currentTimeMillis();
		do {
		    try {
			LOGGER.info("CLICKING ON START CONFIGURATION BUTTON");

			click(By.xpath(
				BroadBandWebGuiElements.ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_START_UP_BUTTON));

			waitForTextToAppear(BroadBandTestConstants.CAPTIVE_PORTAL_CONFIGURATION_PAGE_HEADER_MESSAGE, By
				.xpath(BroadBandWebGuiElements.ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_PAGE_HEADER));
			configStatus = true;
		    } catch (Exception e) {
			configStatus = false;
			LOGGER.error("Element start configuration not found will check again");
		    }
		} while (!configStatus
			&& (System.currentTimeMillis() - startTime) < BroadBandTestConstants.FIVE_MINUTE_IN_MILLIS
			&& BroadBandCommonUtils.hasWaitForDuration(tapEnv,
				BroadBandTestConstants.ONE_MINUTE_IN_MILLIS));
		if (configStatus) {
		    driver.manage().window().maximize();
		    // Configure Wi-Fi SSID & password in text box
		    LOGGER.info("CONFIGURING PRIVATE WI-FI SSID AS : " + privateWifiSsid);
		    sendKeys(By.id(BroadBandWebGuiElements.ELEMENT_ID_CAPTIVE_PORTAL_CONFIGURE_WIFI_SSID_TEXTBOX),
			    privateWifiSsid);
		    LOGGER.info("CONFIGURING PRIVATE WI-FI PASSWORD");
		    sendKeys(By.id(BroadBandWebGuiElements.ELEMENT_ID_CAPTIVE_PORTAL_CONFIGURE_WIFI_PASSWORD_TEXTBOX),
			    privateWifiPassword);
		    try {
			LOGGER.info("Entering Tab key");
			WebElement webElement = driver.findElement(By
				.id(BroadBandWebGuiElements.ELEMENT_ID_CAPTIVE_PORTAL_CONFIGURE_WIFI_PASSWORD_TEXTBOX));
			webElement.sendKeys(Keys.TAB);
			LOGGER.info("Tab key entered");
		    } catch (Exception e) {
			LOGGER.info("Exception while entering tab key");
		    }
		    // clicking next button
		    LOGGER.info("CLICKING ON NEXT BUTTON");
		    try {
			LOGGER.info("Wait for 30sec");
			Thread.sleep(30000);
			click(By.xpath(
				BroadBandWebGuiElements.ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_PAGE_NEXT_BUTTON));
		    } catch (Exception e) {
			LOGGER.info("Exception after adding wait time");
		    }
		    try {
			LOGGER.info("VERIFYING THE CLICK");
			if (isDisplayed(By.xpath(
				BroadBandWebGuiElements.ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_PAGE_NEXT_BUTTON))) {
			    LOGGER.info("CLICK ON NEXT BUTTON AGAIN");
			    click(By.xpath(
				    BroadBandWebGuiElements.ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_PAGE_NEXT_BUTTON));
			}
		    } catch (NoSuchElementException e) {
			LOGGER.info("ALREADY CLICKED THE NEXT BUTTON");
		    }

		    // Get the Confirm header message based on partner
		    waitForTextToAppear(
			    BroadbandPropertyFileHandler.getCaptivePortalConfirmHeaderMessageForPartner(PartnerIdName),
			    By.xpath(
				    BroadBandWebGuiElements.ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_CONFIRMATION_PAGE_HEADER));
		    LOGGER.info("REDIRECTED TO CONFIRM WI-FI SETTING PAGE");

		    // clicking next button to confirm configuration
		    LOGGER.info("CLICKING ON NEXT BUTTON TO CONFIRM WI-FI SETTINGS");
		    click(By.xpath(
			    BroadBandWebGuiElements.ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_CONFIRMATION_PAGE_NEXT_BUTTON));
		    Thread.sleep(10000);
		    try {
			LOGGER.info("VERIFYING THE CLICK");
			if (isDisplayed(By.xpath(
				BroadBandWebGuiElements.ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_CONFIRMATION_PAGE_NEXT_BUTTON))) {
			    LOGGER.info("CLICK ON NEXT BUTTON AGAIN TO CONFIRM WI-FI SETTINGS");
			    click(By.xpath(
				    BroadBandWebGuiElements.ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_CONFIRMATION_PAGE_NEXT_BUTTON));
			}
		    } catch (NoSuchElementException e) {
			LOGGER.info("ALREADY CLICKED THE NEXT BUTTON TO CONFIRM WI-FI SETTINGS");
		    }
		    tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);

		    // Get the success header message based on partner
		    waitForTextToAppear(
			    BroadbandPropertyFileHandler.getCaptivePortalSuccessHeaderMessageForPartner(PartnerIdName),
			    By.xpath(
				    BroadBandWebGuiElements.ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_COMPLETION_PAGE_HEADER));
		    LOGGER.info("Captive Portal header text :" + getText(By.xpath(
			    BroadBandWebGuiElements.ELEMENT_XPATH_CAPTIVE_PORTAL_CONFIGURATION_SUCCESS_PAGE_HEADER)));
		    LOGGER.info(
			    "WI-FI SETTINGS HAVE BEEN SUCCESSFULLY CONFIGURED VIA CAPTIVE PORTAL & REDIRECTED TO SETTING SUCCESSFUL PAGE");
		    LOGGER.info("GOING TO WAIT FOR 2 MINUTES TO REFELECT NEW WI-FI SETTINGS");
		    // Confirming configuration
		    LOGGER.info("CONFIRMING THE CONFIGURATION VIA WEBPA/DMCLI");
		    tapEnv.waitTill(BroadBandTestConstants.TWO_MINUTE_IN_MILLIS);
		    String ssidName = BroadBandConnectedClientUtils.getSsidNameFromGatewayUsingWebPaOrDmcli(device,
			    tapEnv, WiFiFrequencyBand.WIFI_BAND_2_GHZ);
		    String passPhraseName = BroadBandConnectedClientUtils.getSsidPassphraseFromGatewayUsingWebPaOrDmcli(
			    device, tapEnv, WiFiFrequencyBand.WIFI_BAND_2_GHZ);
		    String ssidName5Ghz = BroadBandConnectedClientUtils.getSsidNameFromGatewayUsingWebPaOrDmcli(device,
			    tapEnv, WiFiFrequencyBand.WIFI_BAND_5_GHZ);
		    String passPhraseName5Ghz = BroadBandConnectedClientUtils
			    .getSsidPassphraseFromGatewayUsingWebPaOrDmcli(device, tapEnv,
				    WiFiFrequencyBand.WIFI_BAND_5_GHZ);
		    if (CommonMethods.isNotNull(ssidName) && CommonMethods.isNotNull(passPhraseName)
			    && CommonMethods.isNotNull(ssidName5Ghz) && CommonMethods.isNotNull(passPhraseName5Ghz)) {
			status = ssidName.equalsIgnoreCase(privateWifiSsid)
				&& passPhraseName.equalsIgnoreCase(privateWifiPassword)
				&& ssidName5Ghz.equalsIgnoreCase(privateWifiSsid)
				&& passPhraseName5Ghz.equalsIgnoreCase(privateWifiPassword);
		    }
		    LOGGER.info("WI-FI SETTINGS ARE AS PER CONFIGURATION WHEN CHECKED VIA WEBPA/DMCLI : " + status);

		    LOGGER.info("WI-FI SETTINGS HAVE BEEN SUCCESSFULLY CONFIGURED VIA CAPTIVE PORTAL : " + status);
		} else {
		    LOGGER.error("Failed to start configuration captive portal page");
		}
	    }
	} catch (Exception e) {
	    LOGGER.error("EXCEPTION OCCURED WHILE CONFIGURING VALIDATING CAPTIVE PORTAL :" + e.getMessage());
	}

	return status;
    } 
}

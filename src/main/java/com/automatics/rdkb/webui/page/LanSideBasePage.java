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

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
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
import com.automatics.rdkb.webui.exception.PageTitleNotFoundException;
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

    @FindBy(id = "username")
    private static WebElement userName;
    @FindBy(id = "password")
    private static WebElement password;
    @FindBy(className = "btn")
    private static WebElement logInButton;

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
     * Method to accept alert
     * 
     */
    public static void alertAccept() throws Exception {
	Alert alert = driver.switchTo().alert();
	alert.accept();
    }

    /**
     * Method to clear inputs in certain web element ID
     * 
     */
    public static void clear(By elementId) {
	driver.findElement(elementId).clear();
    }

    /**
     * Method to close driver
     */
    public static void closeBrowser() {
	if (null != driver) {
	    driver.quit();
	}
    }

    public static void setUserName(String strUserName) {
	userName.sendKeys(strUserName);
    }

    public static void setPassword(String strPassword) {
	password.sendKeys(strPassword);
    }

    public static void clickLogIn() {
	logInButton.click();
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
		// status =
		// verifyLaunchedPageTitle(BroadBandWebGuiTestConstant.CAPTIVE_PORTAL_PAGE_TITLE);
		// status =
		// verifyLaunchedPageTitle(BroadBandWebUiUtils.getCaptivePageTitleForSynPartDev(partner,
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
	// CustomizableBrowserCapabilities capabilities = null; //Commented to remove
	// build error
	Platform platform = null;
	Browser defaultBrowser = null;
	try {
	    Device connDevice = (Device) clientDevice;
	    seleniumNode = new SeleniumNodeConnectionHandler();
	    if (connDevice.isLinux() || connDevice.isRaspbianLinux()) {
		platform = Platform.LINUX;
		defaultBrowser = Browser.FIREFOX;
	    } else if (connDevice.isWindows()) {
		platform = Platform.WINDOWS;
		defaultBrowser = Browser.CHROME;
	    } else if (connDevice.isMacOS()) {
		platform = Platform.MAC;
		defaultBrowser = Browser.SAFARI;
	    }
	    if (browser != null) {
		// capabilities = new CustomizableBrowserCapabilities(browser, platform,
		// (Device) clientSettop);
		// //Commented to remove build error
		// setDriver(seleniumNode.invokeBrowserInNode(clientSettop, browser,
		// capabilities));
		setDriver(seleniumNode.invokeBrowserInNode(clientDevice, browser));
	    } else {
		// capabilities = new CustomizableBrowserCapabilities(defaultBrowser, platform,
		// (Device) clientSettop);
		// //Commented to remove build error
		// setDriver(seleniumNode.invokeBrowserInNode(clientSettop, defaultBrowser,
		// capabilities));
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

    /**
     * Method to validate broadband login page launched status
     * 
     * @return true if launched status is true
     * @throws PageTitleNotFoundException
     * 
     */
    public static boolean validateLanPageLaunchedStatus() throws PageTitleNotFoundException {
	// Variable to store the web page launched status
	boolean status = false;
	// Variable to store the Admin page title
	String adminPageTitle = null;
	LOGGER.debug("START METHOD :validateLanPageLaunchedStatus ");
	adminPageTitle = getCurrentTitle();
	LOGGER.info("cusAdminPageTitle : " + adminPageTitle);
	if (CommonMethods.isNotNull(adminPageTitle)) {
	    status = CommonUtils.patternSearchFromTargetString(adminPageTitle,
		    BroadbandPropertyFileHandler.getAdminLoginPageTitle());
	    LOGGER.info("Successfully verified Lan Side Admin page launched status : " + status);
	} else {
	    throw new PageTitleNotFoundException(BroadbandPropertyFileHandler.getAdminLoginPageTitle());
	}
	LOGGER.debug("END METHOD :validateCusadminPageLaunched Status ");
	return status;
    }

    /**
     * Method to launch any page using the link text & validate the page launch using the title.
     * 
     * 
     * @param device
     *            Dut instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param linkText
     *            String representing the link text to be clicked.
     * @param expectedPageTitle
     *            String representing the constant for page title expected after performing the 'click' operation.
     * 
     * @return Boolean representing the result of page launch operation.
     * @refactor Athira
     * 
     * 
     */
    public static boolean isPageLaunchedForPartners(Dut device, AutomaticsTapApi tapEnv, String linkText,
	    String expectedPageTitleConstant) {
	LOGGER.debug("STARTING METHOD: isPageLaunchedForPartners");
	String expectedPageTitle = null;
	boolean status = false;
	try {

	    expectedPageTitle = BroadbandPropertyFileHandler.getPageTitleConnectedDevice();
	    LOGGER.info("expectedPageTitle is : " + expectedPageTitle);

	    click(By.linkText(linkText));
	    String pageTitle = driver.getTitle();
	    LOGGER.info("PAGE TITLE: " + pageTitle);

	    status = CommonMethods.isNotNull(pageTitle) && pageTitle.equalsIgnoreCase(expectedPageTitle);
	    LOGGER.info("LAUNCHED THE PAGE WITH TITLE " + expectedPageTitle + " :" + status);
	    LOGGER.debug("ENDING METHOD: isPageLaunchedForPartners");
	} catch (Exception e) {
	    LOGGER.error("Exception occured in isPageLaunchedForPartners " + e.getMessage());

	}
	return status;
    }

    /**
     * Method to get user name
     * 
     * @return the userName
     */
    public static WebElement getUserName() {
	return userName;
    }

    /**
     * Method to launch any page using the link text & validate the page launch using the title.
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param linkText
     *            String representing the link text to be clicked.
     * @param expectedPageTitle
     *            String representing the constant of page Title expected after performing the 'click' operation.
     * 
     * @return Boolean representing the result of page launch operation.
     * @refactor Rakesh C N
     */

    public static boolean isPageLaunchedByUsingWebElementforParentalControlManagedSites(Dut device,
	    AutomaticsTapApi tapEnv, String linkText, String expectedPageTitleConstant) {
	LOGGER.debug("STARTING METHOD: isPageLaunchedForPartners");
	String expectedPageTitle = null;
	boolean status = false;
	try {
	    expectedPageTitle = BroadbandPropertyFileHandler.getParentalControlManagedSitesPageTitle();
	    click(By.linkText(linkText));
	    String pageTitle = driver.getTitle();
	    LOGGER.info("PAGE TITLE: " + pageTitle);
	    status = CommonMethods.isNotNull(pageTitle) && pageTitle.equalsIgnoreCase(expectedPageTitle);
	    LOGGER.info("LAUNCHED THE PAGE WITH TITLE " + expectedPageTitle + " :" + status);
	    LOGGER.debug("ENDING METHOD: isPageLaunchedForPartners");
	} catch (Exception e) {
	    LOGGER.error("Exception occured in isPageLaunchedForPartners " + e.getMessage());
	}
	return status;
    }

    /**
     * Method to get the value of attribute from web page
     * 
     */
    public static String getTextUsingAttribute(By elementId, String attribute) throws Exception {
	return driver.findElement(elementId).getAttribute(attribute);
    }

    /**
     * Method to select values for Blocking time from drop downs in Parental Control
     * 
     * @param hourXpath
     * @param minuteXpath
     * @param amPmXpath
     * @param hourValue
     * @param minuteValue
     * @param amPmValue
     * @return true if all values selected successfully from dropdowns
     * @refactor Rakesh C N
     */
    public boolean selectBlockedTimeValuesInParentalControl(String hourXpath, String minuteXpath, String amPmXpath,
	    String hourValue, String minuteValue, String amPmValue) {
	// Variable Declaration begins
	boolean areValuesSelected = false;
	boolean isHourTimeSelected = false;
	boolean isMinuteTimeSelected = false;
	boolean isAmPmSelected = false;
	String errorMessage = "";
	// Variable Declaration Ends

	try {
	    /** Selecting value for Hour Time */
	    isHourTimeSelected = selectAndValidateValueFromDropDown(hourXpath, hourValue);
	    /** Selecting value for Minute Time */
	    isMinuteTimeSelected = selectAndValidateValueFromDropDown(minuteXpath, minuteValue);
	    /** Selecting value for AM/PM */
	    isAmPmSelected = selectAndValidateValueFromDropDown(amPmXpath, amPmValue);
	    /** Verifying all selected value for Blocked time */
	    areValuesSelected = isHourTimeSelected && isMinuteTimeSelected && isAmPmSelected;
	} catch (Exception e) {
	    errorMessage = e.getMessage();
	    LOGGER.error(errorMessage);
	    LOGGER.error("###################################################################################");
	    LOGGER.error("Exception occured while selecting time values for blocking in Parental Control.");
	    LOGGER.error("###################################################################################");
	}
	return areValuesSelected;
    }

    /**
     * Method to Select and validate value from dropdown
     * 
     * @param dropDownXpath
     *            Xpath for DropDown
     * @param valueToBeSelected
     *            Value to be selected from dropdown
     * @return return true if value selected successfully and validated
     * 
     * 
     */
    public static boolean selectAndValidateValueFromDropDown(String dropDownXpath, String valueToBeSelected) {
	boolean isSelectedSuccessfully = false;
	String errorMessage = "";
	try {
	    LanSideBasePage lanSideBasePage = new LanSideBasePage();
	    lanSideBasePage.selectViaVisibleText(By.xpath(dropDownXpath), valueToBeSelected);
	    String selectedvalue = driver.findElement(By.xpath(dropDownXpath))
		    .getAttribute(BroadBandTestConstants.STRING_VALUE);
	    if (valueToBeSelected.equals(BroadBandTestConstants.MNG_SERVICES_DESCRIPTION_AS_HTTP)) {
		valueToBeSelected = BroadBandTestConstants.HTTP_VALUE_IN_DROPDOWN;
	    }
	    /* Validating Selected value from dropdown */
	    isSelectedSuccessfully = BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
		    valueToBeSelected, selectedvalue);
	} catch (Exception e) {
	    errorMessage = e.getMessage();
	    LOGGER.error(errorMessage);
	    LOGGER.error("############################################################");
	    LOGGER.error("Exception occured while Selecting and validating value from dropdown.");
	    LOGGER.error("############################################################");
	}

	return isSelectedSuccessfully;
    }

    /**
     * Method to click drop down icon and select the id based on visibility
     * 
     */
    public void selectViaVisibleText(By elementId, String visibleText) throws Exception {
	Select dropdown = new Select(driver.findElement(elementId));
	dropdown.selectByVisibleText(visibleText);
    }

    /**
     * Method to verify Alert and Message & also accept the Alert
     * 
     * @param xpathForSaveButton
     *            Xpath for Save button to Click
     * @param popupTitle
     *            Popup Title to verify Alert Title
     * @param expectedAlertMessage
     *            Expected Alert to match with error Message from UI
     * @param tapEnv
     *            AutomaticsTapApi instance
     * 
     * @return true if Alert comes with proper Message.
     * @throws Exception
     * @refactor Rakesh C N
     */
    public static boolean verifyAndAcceptAlert(String xpathForSaveButton, String popupTitle,
	    String expectedAlertMessage, AutomaticsTapApi tapEnv) throws Exception {
	boolean isAlertVerified = false;
	String alertMesaageFromUi = "";
	String errorMessage = "";
	LanSideBasePage.click(By.xpath(xpathForSaveButton));
	tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
	try {
	    /* Waiting for 'Alert' POPUP message */
	    WebDriverWait wait = new WebDriverWait(driver, 30);
	    wait.until(ExpectedConditions.textToBePresentInElementLocated(
		    By.xpath(BroadBandWebGuiElements.ELEMENT_XPATH_PARENTAL_CONTROL_REPORT_GENERATION_POP_UP_TITLE),
		    popupTitle));
	    LOGGER.info("Successfully verified the presents of 'Alert' popup message.");
	    alertMesaageFromUi = getText(BroadBandWebGuiTestConstant.XPATH_FOR_POPUP_MESSAGE);
	    /* Clicking on POPUP OK button */
	    LanSideBasePage.click(
		    By.xpath(BroadBandWebGuiElements.ELEMENT_XPATH_PARENTAL_CONTROL_REPORT_GENERATION_POP_UP_OK));
	    isAlertVerified = BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
		    expectedAlertMessage, alertMesaageFromUi);
	} catch (Exception exception) {
	    LOGGER.info("#######################################################################################");
	    errorMessage = exception.getMessage();
	    LOGGER.error(errorMessage);
	    LOGGER.error("Pop Up not displayed.");
	    LOGGER.error("#######################################################################################");
	}

	return isAlertVerified;
    }

    /**
     * Method to launch any page using the Element Id & validate the page launch using the title.
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param webElementId
     *            String representing the Xpath of WebElement to be clicked.
     * @param expectedPageTitle
     *            String representing the constant of page Title expected after performing the 'click' operation.
     * 
     * @return Boolean representing the result of page launch operation.
     * @author Deepika Sekar
     * @refactor Rakesh C N
     */
    public static boolean isPageLaunchedByUsingWebElementforParentalControlAddBlockDomain(Dut device,
	    AutomaticsTapApi tapEnv, By webElement, String expectedPageTitleConstant) {
	LOGGER.debug("STARTING METHOD: isPageLaunchedByUsingWebElement");
	boolean status = false;
	String expectedPageTitle = BroadbandPropertyFileHandler.getParentalControlAddBlockedDomain();
	try {
	    click(webElement);
	    String pageTitle = driver.getTitle();
	    LOGGER.info("PAGE TITLE: " + pageTitle);
	    status = CommonMethods.isNotNull(pageTitle)
		    && pageTitle.toLowerCase().contains(expectedPageTitle.toLowerCase());
	    LOGGER.info("LAUNCHED THE PAGE WITH TITLE " + expectedPageTitle + " :" + status);
	    LOGGER.debug("ENDING METHOD: isPageLaunchedByUsingWebElement");
	} catch (Exception e) {
	    LOGGER.error("Exception occured in  isPageLaunchedByUsingWebElement " + e.getMessage());
	}
	return status;
    }

    /**
     * Method to launch any page using the Element Id & validate the page launch using the title.
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param webElementId
     *            String representing the Xpath of WebElement to be clicked.
     * @param expectedPageTitle
     *            String representing the constant of page Title expected after performing the 'click' operation.
     * 
     * @return Boolean representing the result of page launch operation.
     * @author Deepika Sekar
     * @refactor Rakesh C N
     */
    public static boolean isPageLaunchedByUsingWebElementforParentalControlAddBlockedKeyword(Dut device,
	    AutomaticsTapApi tapEnv, By webElement, String expectedPageTitleConstant) {
	LOGGER.debug("STARTING METHOD: isPageLaunchedByUsingWebElement");
	boolean status = false;
	String expectedPageTitle = BroadbandPropertyFileHandler.getParentalControlAddBlockedKeyword();
	try {
	    click(webElement);
	    String pageTitle = driver.getTitle();
	    LOGGER.info("PAGE TITLE: " + pageTitle);
	    status = CommonMethods.isNotNull(pageTitle)
		    && pageTitle.toLowerCase().contains(expectedPageTitle.toLowerCase());
	    LOGGER.info("LAUNCHED THE PAGE WITH TITLE " + expectedPageTitle + " :" + status);
	    LOGGER.debug("ENDING METHOD: isPageLaunchedByUsingWebElement");
	} catch (Exception e) {
	    LOGGER.error("Exception occured in  isPageLaunchedByUsingWebElement " + e.getMessage());
	}
	return status;
    }

    /**
     * Method to launch any page using the link text & validate the page launch using the title.
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param linkText
     *            String representing the link text to be clicked.
     * @param expectedPageTitle
     *            String representing the constant of page Title expected after performing the 'click' operation.
     * 
     * @return Boolean representing the result of page launch operation.
     * @refactor Rakesh C N
     */

    public static boolean isPageLaunchedByUsingWebElementforParentalControlManagedService(Dut device,
	    AutomaticsTapApi tapEnv, String linkText, String expectedPageTitleConstant) {
	LOGGER.debug("STARTING METHOD: isPageLaunchedForPartners");
	String expectedPageTitle = null;
	boolean status = false;
	try {
	    expectedPageTitle = BroadbandPropertyFileHandler.getParentalControlManagedServicePageTitle();
	    click(By.linkText(linkText));
	    String pageTitle = driver.getTitle();
	    LOGGER.info("PAGE TITLE: " + pageTitle);
	    status = CommonMethods.isNotNull(pageTitle) && pageTitle.equalsIgnoreCase(expectedPageTitle);
	    LOGGER.info("LAUNCHED THE PAGE WITH TITLE " + expectedPageTitle + " :" + status);
	    LOGGER.debug("ENDING METHOD: isPageLaunchedForPartners");
	} catch (Exception e) {
	    LOGGER.error("Exception occured in isPageLaunchedForPartners " + e.getMessage());
	}
	return status;
    }

    /**
     * Method to launch any page using the Element Id & validate the page launch using the title.
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param webElementId
     *            String representing the Xpath of WebElement to be clicked.
     * @param expectedPageTitle
     *            String representing the constant of page Title expected after performing the 'click' operation.
     * 
     * @return Boolean representing the result of page launch operation.
     * @author Deepika Sekar
     * @refactor Rakesh C N
     */
    public static boolean isPageLaunchedByUsingWebElementAddBlockedService(Dut device, AutomaticsTapApi tapEnv,
	    By webElement, String expectedPageTitleConstant) {
	LOGGER.debug("STARTING METHOD: isPageLaunchedByUsingWebElement");
	boolean status = false;
	String expectedPageTitle = BroadbandPropertyFileHandler.getParentalControlAddBlockedService();
	try {
	    click(webElement);
	    String pageTitle = driver.getTitle();
	    LOGGER.info("PAGE TITLE: " + pageTitle);
	    status = CommonMethods.isNotNull(pageTitle)
		    && pageTitle.toLowerCase().contains(expectedPageTitle.toLowerCase());
	    LOGGER.info("LAUNCHED THE PAGE WITH TITLE " + expectedPageTitle + " :" + status);
	    LOGGER.debug("ENDING METHOD: isPageLaunchedByUsingWebElement");
	} catch (Exception e) {
	    LOGGER.error("Exception occured in  isPageLaunchedByUsingWebElement " + e.getMessage());
	}
	return status;
    }

    /**
     * Method to launch any page using the Element Id & validate the page launch using the title.
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param webElementId
     *            String representing the Xpath of WebElement to be clicked.
     * @param expectedPageTitle
     *            String representing the constant of page Title expected after performing the 'click' operation.
     * 
     * @return Boolean representing the result of page launch operation.
     * @author Deepika Sekar
     * 
     * @Refactor Sruthi Santhosh
     */
    public static boolean isPageLaunchedByUsingWebElementForConnectedDevicesEditDevice(Dut device,
	    AutomaticsTapApi tapEnv, By webElement, String expectedPageTitleConstant) {
	LOGGER.debug("STARTING METHOD: isPageLaunchedByUsingWebElement");

	boolean status = false;
	String expectedPageTitle = BroadbandPropertyFileHandler.getPageTitleForConnectedDevicesEditDevicePage();
	try {
	    click(webElement);
	    String pageTitle = driver.getTitle();
	    LOGGER.info("PAGE TITLE: " + pageTitle);
	    status = CommonMethods.isNotNull(pageTitle)
		    && pageTitle.toLowerCase().contains(expectedPageTitle.toLowerCase());
	    LOGGER.info("LAUNCHED THE PAGE WITH TITLE " + expectedPageTitle + " :" + status);
	    LOGGER.debug("ENDING METHOD: isPageLaunchedByUsingWebElement");
	} catch (Exception e) {
	    LOGGER.error("Exception occured in  isPageLaunchedByUsingWebElement " + e.getMessage());
	}
	return status;
    }

    /**
     * Method to launch any page using the link text & validate the page launch using the title.
     * 
     * @param linkText
     *            String representing the link text to be clicked.
     * @param expectedPageTitle
     *            String representing the Title of the page expected after performing the 'click' operation.
     * 
     * @return Boolean representing the result of page launch operation.
     * @refactor Said Hisham
     */
    public static boolean isPageLaunched(String linkText, String expectedPageTitle) {
	LOGGER.debug("STARTING METHOD: isPageLaunched");
	click(By.linkText(linkText));
	String pageTitle = driver.getTitle();
	LOGGER.info("PAGE TITLE: " + pageTitle);
	boolean status = CommonMethods.isNotNull(pageTitle) && pageTitle.equalsIgnoreCase(expectedPageTitle);
	LOGGER.info("LAUNCHED THE PAGE WITH TITLE " + expectedPageTitle + " :" + status);
	LOGGER.debug("ENDING METHOD: isPageLaunched");
	return status;
    }

    /**
     * Method to launch any page using the link text & validate the page launch using the title.
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param linkText
     *            String representing the link text to be clicked.
     * @param expectedPageTitle
     *            String representing the constant of page Title expected after performing the 'click' operation.
     * 
     * @return Boolean representing the result of page launch operation.
     * @refactor Said Hisham
     */

    public static boolean isFireWallPageLaunchedForPartners(Dut device, AutomaticsTapApi tapEnv, String linkText,
	    String expectedPageTitleConstant) {
	LOGGER.debug("STARTING METHOD: isPageLaunchedForPartners");
	String expectedPageTitle = null;
	boolean status = false;
	try {
	    expectedPageTitle = BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
		    linkText, "IPv4") ? BroadbandPropertyFileHandler.getFireWallIpv4PageTitle()
			    : BroadbandPropertyFileHandler.getFireWallIpv6PageTitle();
	    click(By.linkText(linkText));
	    String pageTitle = driver.getTitle();
	    LOGGER.info("PAGE TITLE: " + pageTitle);
	    status = CommonMethods.isNotNull(pageTitle) && pageTitle.equalsIgnoreCase(expectedPageTitle);
	    LOGGER.info("LAUNCHED THE PAGE WITH TITLE " + expectedPageTitle + " :" + status);
	    LOGGER.debug("ENDING METHOD: isPageLaunchedForPartners");
	} catch (Exception e) {
	    LOGGER.error("Exception occured in isPageLaunchedForPartners " + e.getMessage());
	}
	return status;
    }

    /**
     * Method to Enter value in Text Box
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param webDriver
     *            the web driver.
     * @param textBoxBoxXpath
     *            Xpath of text box in which value should be entered
     * @param value
     *            Value to be entered in text box
     * @return status true if desired value is entered successfully
     * 
     * @author Prashant Mishra
     * 
     * @Refactor Sruthi Santhosh
     */
    public static boolean enterInTextBoxAndValidate(AutomaticsTapApi tapEnv, WebDriver webDriver,
	    String textBoxBoxXpath, String value) {
	LOGGER.debug("STARTING METHOD : enterInTextBoxAndValidate()");
	// Variable declaration starts
	boolean isValueEntered = false;
	WebElement box = null;
	String enteredvalue = "";
	String errorMessage = "";
	// Variable declaration ends

	try {
	    /** Getting the Web Element of Text box xpath */
	    box = webDriver.findElement(By.xpath(textBoxBoxXpath));
	    /** Clearing the box */
	    box.clear();
	    tapEnv.waitTill(BroadBandTestConstants.FIVE_SECONDS_IN_MILLIS);
	    /** Entering the value in text box */
	    box.sendKeys(value);
	    tapEnv.waitTill(BroadBandTestConstants.FIVE_SECONDS_IN_MILLIS);
	    /** Getting entered value in text box */
	    enteredvalue = webDriver.findElement(By.xpath(textBoxBoxXpath))
		    .getAttribute(BroadBandTestConstants.STRING_VALUE);
	    LOGGER.info("Entered value in text box: " + enteredvalue);
	    /** Validating Entered value in text box */
	    isValueEntered = CommonMethods.isNotNull(enteredvalue) && enteredvalue.equalsIgnoreCase(value);
	} catch (Exception exception) {
	    LOGGER.error("Exception occured while entering value in text box.");
	    errorMessage = exception.getMessage();
	    LOGGER.error(errorMessage);
	}
	LOGGER.debug("ENDING METHOD : enterInTextBoxAndValidate()");
	return isValueEntered;
    }

    /**
     * Method to launch any page using the link text & validate the page launch using the title.
     * 
     * @param device
     *            {@link Dut}
     * @param tapEnv
     *            {@link AutomaticsTapApi}
     * @param linkText
     *            String representing the link text to be clicked.
     * @param expectedPageTitle
     *            String representing the constant of page Title expected after performing the 'click' operation.
     * 
     * @return Boolean representing the result of page launch operation.
     * @refactor Rakesh C N
     */

    public static boolean isPageLaunchedByUsingWebElementforParentalControlManagedDevices(Dut device,
	    AutomaticsTapApi tapEnv, String linkText, String expectedPageTitleConstant) {
	LOGGER.debug("STARTING METHOD: isPageLaunchedForPartners");
	String expectedPageTitle = null;
	boolean status = false;
	try {
	    expectedPageTitle = BroadbandPropertyFileHandler.getParentalControlManagedDevicesPageTitle();
	    click(By.linkText(linkText));
	    String pageTitle = driver.getTitle();
	    LOGGER.info("PAGE TITLE: " + pageTitle);
	    status = CommonMethods.isNotNull(pageTitle) && pageTitle.equalsIgnoreCase(expectedPageTitle);
	    LOGGER.info("LAUNCHED THE PAGE WITH TITLE " + expectedPageTitle + " :" + status);
	    LOGGER.debug("ENDING METHOD: isPageLaunchedForPartners");
	} catch (Exception e) {
	    LOGGER.error("Exception occured in isPageLaunchedForPartners " + e.getMessage());
	}
	return status;
    }
}

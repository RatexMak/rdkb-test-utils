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
     * Utility method to navigate To Wizard from Gateway menu 
     * 
     * @param settop
     *            settop instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
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
     * Utility method to navigate WiFi Configuration Page from Gateway menu in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
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
     * Utility method to navigate WiFi Edit Page 
     * 
     * @param device
     *            Dut instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
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
		pageTitle = AutomaticsTapApi
			.getSTBPropsValue(BroadBandWebGuiTestConstant.UI_WIFI_5_GHZ_EDIT_PAGE_TITLE);
	    } else {
		pageTitle = AutomaticsTapApi
			.getSTBPropsValue(BroadBandWebGuiTestConstant.UI_WIFI_2_4_GHZ_EDIT_PAGE_TITLE);
	    }
	    status = BroadBandWebUiUtils.validatePageLaunchedStatusWithPageTitle(driver, pageTitle);
	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToPrivateWiFiEditPage():" + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : navigateToPrivateWiFiEditPage()");
	return status;
    }

    /**
     * Utility method to navigate Connected Devices Page from Gateway menu in LAN GUI Home page
     * 
     * @param device
     *            Dut instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
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
     * Utility method to verify the page launch status by using link text or launch Url
     * 
     * @param device
     *            instance of {@link Dut}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @param linkText
     *            Link text to be launched
     * @param phpPageName
     *            PHp file name to be launched
     * @param pageTitle
     *            Page title to be verified
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
     * Utility method to navigate Local Ip Configuration Page from Gateway menu in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
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
     * Utility method to navigate Partner Network Page from Gateway menu in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
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
     * Utility method to navigate Connection page from Gateway menu in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
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

    /**
     * Test step method used to Login into the LAN Admin GUI web page
     * 
     * @param device
     *            instance of{@link Dut}
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param deviceConnectedWithEthernet
     *            instance of Ethernet Client
     * @param testCaseId
     *            Test case ID
     * @param stepNumber
     *            Step Number
     * @return status True-LAN Admin Login success.Else-LAN Admin Login Fail
     */
    public static boolean executeTestStepToLoginLanAdminWebGui(Dut device, AutomaticsTapApi tapEnv,
	    Dut deviceConnectedWithEthernet, String testCaseId, int stepNumber) {
	/**
	 * STEP : VERIFY LOGIN INTO THE LAN GUI ADIMN PAGE BY USING USERID AND PASSWORD
	 */
	String stepNum = "S" + stepNumber;
	boolean status = false;
	String errorMessage = null;
	WebDriver lanDriver = null;
	LOGGER.info("#######################################################################################");
	LOGGER.info("STEP " + stepNumber
		+ " : DESCRIPTION : Verify the gateway admin page is accessible in connected client and can be logged in using admin/****** credential for residential or cusadmin/****** credential for commercial devices");
	LOGGER.info("STEP " + stepNumber
		+ " : ACTION : Launch the gateway admin gui in browser url : https://10.0.0.1 or https://10.1.10.1 , once the page is loaded ,use username and password as admin/****** for residential or cusadmin/****** for commercial devices to login");
	LOGGER.info("STEP " + stepNumber
		+ " : EXPECTED : Gateway admin page should be accessible from client and can be able to login using admin/****** credential for residential or cusadmin/****** credential for commercial devices");
	LOGGER.info("#######################################################################################");
	errorMessage = "Unable to login gateway admin page in connected client";
	try {
	    status = LanWebGuiLoginPage.logintoLanPage(tapEnv, device, deviceConnectedWithEthernet);
	    lanDriver = LanWebGuiLoginPage.getDriver();
	} catch (Exception e) {
	    errorMessage = e.getMessage();
	    LOGGER.error("Exception occurred during gateway admin page login : " + errorMessage);
	}
	if (status) {
	    LOGGER.info("STEP " + stepNumber + " : ACTUAL : Lan gui admin login successful");
	} else {
	    LOGGER.error("STEP " + stepNumber + " : ACTUAL : " + errorMessage);
	}
	LOGGER.info("**********************************************************************************");
	BroadBandWebUiUtils.updateExecutionStatusForWebGuiStep(lanDriver, tapEnv, device, testCaseId, stepNum, status,
		errorMessage, true);
	return status;
    }

    /**
     * Utility method to navigate TroubleShooting Reset Restore Page from Gateway menu in LAN GUI Home page
     * 
     * @param device
     *            Dut instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @return True,if navigated and title verified Successfully
     * @refactor Athira
     * 
     */
    public boolean navigateToTroubleShootingResetRestorePage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
	LOGGER.debug("STARTING METHOD : navigateToTroubleShootingResetRestorePage()");
	boolean result = false;
	try {
	    String partnerId = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
		    BroadBandWebPaConstants.WEBPA_PARAM_FOR_SYNDICATION_PARTNER_ID);

	    String key = device.getModel();

	    result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
		    BroadbandPropertyFileHandler.getLinkTextForResetRestoreGateway(),
		    BroadbandPropertyFileHandler.getPageUrlForResetRestoreGateway(),
		    BroadbandPropertyFileHandler.getPageTitleForResetRestoreGateway());

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToTroubleShootingResetRestorePage():" + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : navigateToTroubleShootingResetRestorePage()");
	return result;
    }

    /**
     * Utility method to navigate TroubleShooting Network Diagnostics Tools Page from Gateway menu in LAN GUI Home page
     * 
     * @param device
     *            Dut instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @return True,if navigated and title verified Successfully
     * 
     * @Refactor Sruthi Santhosh
     * 
     */
    public boolean navigateToTroubleShootingNwDiagToolsPage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
	LOGGER.debug("STARTING METHOD : navigateToTroubleShootingNwDiagToolsPage()");
	boolean result = false;

	try {

	    result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
		    BroadbandPropertyFileHandler.getLinkTextForNwDiagToolsPage(),
		    BroadbandPropertyFileHandler.getPageUrlForNwDiagToolsPage(),
		    BroadbandPropertyFileHandler.getPageTitleForNwDiagToolsPage());

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToTroubleShootingNwDiagToolsPage():" + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : navigateToTroubleShootingNwDiagToolsPage()");
	return result;
    }

    /**
     * Method to enter Ipv4 Address values in Text Boxes
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param valueTobeEntered
     *            List Contains Ipv4 Address values
     * @param xpathForIpv4Boxes
     *            Xpath for Ipv4 Text boxes
     * @return true if all element of Ipv4 entered successfully
     * 
     * @Refactor Sruthi Santhosh
     */
    public boolean enterIpv4AddressInTextBoxesWithBoxesXpath(AutomaticsTapApi tapEnv, List<String> valueTobeEntered,
	    String xpathForIpv4Boxes) {
	boolean isIpEnteredSuccessfully = true;
	String textBoxBoxXpath = "";
	String errorMessage = "";
	WebElement box = null;
	WebDriver webDriver = null;

	webDriver = LanWebGuiLoginPage.getDriver();

	try {
	    for (int counter = BroadBandTestConstants.CONSTANT_1; counter <= valueTobeEntered.size(); counter++) {
		/* Defining text box Xpath */
		textBoxBoxXpath = xpathForIpv4Boxes.replace(BroadBandTestConstants.DELIMITER_AMPERSAND,
			Integer.toString(counter));
		box = webDriver.findElement(By.xpath(textBoxBoxXpath));
		/* Clearing the box */
		box.clear();
		tapEnv.waitTill(BroadBandTestConstants.FIVE_SECONDS_IN_MILLIS);
		/* Entering the value */
		box.sendKeys(valueTobeEntered.get(counter - BroadBandTestConstants.CONSTANT_1));
		/* Getting Entered Value in the Box */
		String enteredvalue = webDriver.findElement(By.xpath(textBoxBoxXpath))
			.getAttribute(BroadBandTestConstants.STRING_VALUE);
		/*
		 * Validating Entered value
		 *
		 * Breaking loop with status as false when any of the boxes are not entered Properly
		 */
		if (!enteredvalue.equals(valueTobeEntered.get(counter - BroadBandTestConstants.CONSTANT_1))) {
		    isIpEnteredSuccessfully = BroadBandTestConstants.BOOLEAN_VALUE_FALSE;
		    break;
		}
	    }
	} catch (Exception exception) {
	    isIpEnteredSuccessfully = BroadBandTestConstants.BOOLEAN_VALUE_FALSE;
	    errorMessage = exception.getMessage();
	    LOGGER.error(errorMessage);
	    LOGGER.error("######################################################################");
	    LOGGER.error("Exception occured while entering values in IP address Text Boxes.");
	    LOGGER.error("######################################################################");
	}
	return isIpEnteredSuccessfully;
    }

    /**
     * Utility method to navigate MoCA Page from Gateway menu in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @return True,if navigated and title verified Successfully
     * @refactor Said Hisham
     */
    public boolean navigateToMoCAPage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
	LOGGER.info("STARTING METHOD : navigateToMoCAPage()");
	boolean result = false;
	try {
	    result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
		    BroadbandPropertyFileHandler.getLinkTextForMoCA(), BroadbandPropertyFileHandler.getPageUrlForMoCA(),
		    BroadbandPropertyFileHandler.getPageTitleForMoCA());

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToMoCAPage():" + e.getMessage());
	}
	LOGGER.info("ENDING METHOD : navigateToMoCAPage()");
	return result;
    }

    /**
     * Utility method to navigate Software Page from Gateway menu in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @return True,if navigated and title verified Successfully
     * @refactor Said Hisham
     */
    public boolean navigateToSoftwarePage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
	LOGGER.info("STARTING METHOD : navigateToSoftwarePage()");
	boolean result = false;
	try {
	    result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
		    BroadbandPropertyFileHandler.getLinkTextForSoftware(),
		    BroadbandPropertyFileHandler.getPageUrlForSoftware(),
		    BroadbandPropertyFileHandler.getPageTitleForSoftware());

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToSoftwarePage():" + e.getMessage());
	}
	LOGGER.info("ENDING METHOD : navigateToSoftwarePage()");
	return result;
    }

    /**
     * Utility method to navigate Hardware Page from Software Page from Gateway menu in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @return True,if navigated and title verified Successfully
     * @refactor Said Hisham
     */
    public boolean navigateToHardwareFromSoftwarePage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
	LOGGER.info("STARTING METHOD : navigateToHardwareFromSoftwarePage()");
	boolean result = false;
	try {
	    result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
		    BroadbandPropertyFileHandler.getLinkTextForHardware(),
		    BroadbandPropertyFileHandler.getPageUrlForHardware(),
		    BroadbandPropertyFileHandler.getPageTitleForSoftware());

	    if (result) {
		result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
			BroadbandPropertyFileHandler.getLinkTextForSystemHardware(),
			BroadbandPropertyFileHandler.getPageUrlForSystemHardware(),
			BroadbandPropertyFileHandler.getPageTitleForSystemHardware());
	    }

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToHardwareFromSoftwarePage():" + e.getMessage());
	}
	LOGGER.info("ENDING METHOD : navigateToHardwareFromSoftwarePage()");
	return result;
    }

    /**
     * Utility method to navigate Hardware Lan Page from Gateway menu in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @return True,if navigated and title verified Successfully
     * @refactor Said Hisham
     */
    public boolean navigateToHardwareLanPage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
	LOGGER.info("STARTING METHOD : navigateToHardwareLanPage()");
	boolean result = false;
	try {
	    result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
		    BroadbandPropertyFileHandler.getLinkTextForHardwareLan(),
		    BroadbandPropertyFileHandler.getPageUrlForHardwareLan(),
		    BroadbandPropertyFileHandler.getPageTitleForHardwareLan());

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToHardwareLanPage():" + e.getMessage());
	}
	LOGGER.info("ENDING METHOD : navigateToHardwareLanPage()");
	return result;
    }

    /**
     * Utility method to navigate Hardware Wireless Page from Gateway menu in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @return True,if navigated and title verified Successfully
     * @refactor Said Hisham
     */
    public boolean navigateToHardwareWirelessPage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
	LOGGER.info("STARTING METHOD : navigateToHardwareWirelessPage()");
	boolean result = false;
	try {
	    result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
		    BroadbandPropertyFileHandler.getLinkTextForHardwareWireless(),
		    BroadbandPropertyFileHandler.getPageUrlForHardwareWireless(),
		    BroadbandPropertyFileHandler.getPageTitleForHardwareWireless());

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToHardwareWirelessPage():" + e.getMessage());
	}
	LOGGER.info("ENDING METHOD : navigateToHardwareWirelessPage()");
	return result;
    }

    /**
     * Utility method to navigate Managed Services Page from Parental control menu in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @return True,if navigated and title verified Successfully
     * @refactor Said Hisham
     */
    public boolean navigateToManagedServices(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
	LOGGER.info("STARTING METHOD : navigateToManagedServices()");
	boolean result = false;
	try {
	    result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
		    BroadbandPropertyFileHandler.getLinkTextForManagedServices(),
		    BroadbandPropertyFileHandler.getPageUrlForManagedServices(),
		    BroadbandPropertyFileHandler.getPageTitleForManagedServices());

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToManagedServices():" + e.getMessage());
	}
	LOGGER.info("ENDING METHOD : navigateToManagedServices()");
	return result;
    }

    /**
     * Utility method to navigate Reports Page from Parental control menu in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @return True,if navigated and title verified Successfully
     * @refactor Said Hisham
     */
    public boolean navigateToReports(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
	LOGGER.info("STARTING METHOD : navigateToReports()");
	boolean result = false;
	try {
	    result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
		    BroadbandPropertyFileHandler.getLinkTextForReports(),
		    BroadbandPropertyFileHandler.getPageUrlForReports(),
		    BroadbandPropertyFileHandler.getPageTitleForReports());

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToReports():" + e.getMessage());
	}
	LOGGER.info("ENDING METHOD : navigateToReports()");
	return result;
    }

    /**
     * Utility method to navigate Advanced Page in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @return True,if navigated and title verified Successfully
     * @refactor Said Hisham
     */
    public boolean navigateToAdvancedPage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
	LOGGER.info("STARTING METHOD : navigateToAdvancedPage()");
	boolean result = false;
	try {
	    result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
		    BroadbandPropertyFileHandler.getLinkTextForAdvanced(),
		    BroadbandPropertyFileHandler.getPageUrlForAdvanced(),
		    BroadbandPropertyFileHandler.getPageTitleForAdvanced());

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToAdvancedPage():" + e.getMessage());
	}
	LOGGER.info("ENDING METHOD : navigateToAdvancedPage()");
	return result;
    }

    /**
     * Utility method to navigate Advanced Remote Management Page in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @return True,if navigated and title verified Successfully
     * @refactor Said Hisham
     */
    public boolean navigateToAdvancedRemoteMgmtPage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
	LOGGER.info("STARTING METHOD : navigateToAdvancedRemoteMgmtPage()");
	boolean result = false;
	try {
	    result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
		    BroadbandPropertyFileHandler.getLinkTextForAdvancedRemoteMgmt(),
		    BroadbandPropertyFileHandler.getPageUrlForAdvancedRemoteMgmt(),
		    BroadbandPropertyFileHandler.getPageTitleForAdvancedRemoteMgmt());

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToAdvancedRemoteMgmtPage():" + e.getMessage());
	}
	LOGGER.info("ENDING METHOD : navigateToAdvancedRemoteMgmtPage()");
	return result;
    }

    /**
     * Utility method to navigate Advanced Remote Management Page in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @return True,if navigated and title verified Successfully
     * @refactor Said Hisham
     */
    public boolean navigateToAdvancedDmzPage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
	LOGGER.info("STARTING METHOD : navigateToAdvancedDmzPage()");
	boolean result = false;
	try {
	    result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
		    BroadbandPropertyFileHandler.getLinkTextForAdvancedDmz(),
		    BroadbandPropertyFileHandler.getPageUrlForAdvancedDmz(),
		    BroadbandPropertyFileHandler.getPageTitleForAdvancedDmz());

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToAdvancedDmzPage():" + e.getMessage());
	}
	LOGGER.info("ENDING METHOD : navigateToAdvancedDmzPage()");
	return result;
    }

    /**
     * Utility method to navigate Advanced Device Discovery Page in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @return True,if navigated and title verified Successfully
     * @refactor Said Hisham
     */
    public boolean navigateToAdvancedDeviceDiscoverPage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
	LOGGER.info("STARTING METHOD : navigateToAdvancedDeviceDiscoverPage()");
	boolean result = false;
	try {
	    result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
		    BroadbandPropertyFileHandler.getLinkTextForAdvancedDeviceDiscover(),
		    BroadbandPropertyFileHandler.getPageUrlForAdvancedDeviceDiscover(),
		    BroadbandPropertyFileHandler.getPageTitleForAdvancedDeviceDiscover());

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToAdvancedDeviceDiscoverPage():" + e.getMessage());
	}
	LOGGER.info("ENDING METHOD : navigateToAdvancedDeviceDiscoverPage()");
	return result;
    }

    /**
     * Utility method to navigate Troubleshooting Logs Page in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @return True,if navigated and title verified Successfully
     * @refactor Said Hisham
     */
    public boolean navigateToTroubleShootingLogsPage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
	LOGGER.info("STARTING METHOD : navigateToTroubleShootingLogsPage()");
	boolean result = false;
	try {
	    result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
		    BroadbandPropertyFileHandler.getLinkTextForTroubleShootingLogs(),
		    BroadbandPropertyFileHandler.getPageUrlForTroubleShootingLogs(),
		    BroadbandPropertyFileHandler.getPageTitleForTroubleShootingLogs());

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToTroubleShootingLogsPage():" + e.getMessage());
	}
	LOGGER.info("ENDING METHOD : navigateToTroubleShootingLogsPage()");
	return result;
    }

    /**
     * Utility method to navigate Troubleshooting Wifi Spectrum Analyzer Page in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @return True,if navigated and title verified Successfully
     * @refactor Said Hisham
     */
    public boolean navigateToTroubleShootingWifiSpectrumAnalyzerPage(Dut device, AutomaticsTapApi tapEnv,
	    WebDriver driver) {
	LOGGER.info("STARTING METHOD : navigateToTroubleShootingWifiSpectrumAnalyzerPage()");
	boolean result = false;
	try {
	    result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
		    BroadbandPropertyFileHandler.getLinkTextForTroubleShootingWifiSpectrumAnalyzer(),
		    BroadbandPropertyFileHandler.getPageUrlForTroubleShootingWifiSpectrumAnalyzer(),
		    BroadbandPropertyFileHandler.getPageTitleForTroubleShootingWifiSpectrumAnalyzer());

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToTroubleShootingWifiSpectrumAnalyzerPage():" + e.getMessage());
	}
	LOGGER.info("ENDING METHOD : navigateToTroubleShootingWifiSpectrumAnalyzerPage()");
	return result;
    }

    /**
     * Utility method to navigate Troubleshooting MoCA Diagnostics Page in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @return True,if navigated and title verified Successfully
     * @refactor Said Hisham
     */
    public boolean navigateToTroubleShootingMoCADiagnosticsPage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
	LOGGER.info("STARTING METHOD : navigateToTroubleShootingMoCADiagnosticsPage()");
	boolean result = false;
	try {
	    result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
		    BroadbandPropertyFileHandler.getLinkTextForTroubleShootingMoCADiagnostics(),
		    BroadbandPropertyFileHandler.getPageUrlForTroubleShootingMoCADiagnostics(),
		    BroadbandPropertyFileHandler.getPageTitleForTroubleShootingMoCADiagnostics());

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToTroubleShootingMoCADiagnosticsPage():" + e.getMessage());
	}
	LOGGER.info("ENDING METHOD : navigateToTroubleShootingMoCADiagnosticsPage()");
	return result;
    }

    /**
     * Utility method to navigate Troubleshooting Change Password Page in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @return True,if navigated and title verified Successfully
     * @refactor Said Hisham
     */
    public boolean navigateToTroubleShootingChangePwdPage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
	LOGGER.info("STARTING METHOD : navigateToTroubleShootingChangePwdPage()");
	boolean result = false;
	try {
	    result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
		    BroadbandPropertyFileHandler.getLinkTextForTroubleShootingChangePwd(),
		    BroadbandPropertyFileHandler.getPageUrlForTroubleShootingChangePwd(),
		    BroadbandPropertyFileHandler.getPageTitleForTroubleShootingChangePwd());

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToTroubleShootingChangePwdPage():" + e.getMessage());
	}
	LOGGER.info("ENDING METHOD : navigateToTroubleShootingChangePwdPage()");
	return result;
    }

    /**
     * Utility method to navigate to Managed Sites Page in LAN GUI Home page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @return True,if navigated and title verified Successfully
     * @refactor Said Hisham
     */
    public boolean navigateToManagedSites(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
	LOGGER.info("STARTING METHOD : navigateToManagedSites()");
	boolean result = false;
	try {
	    result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
		    BroadbandPropertyFileHandler.getLinkTextForManagedSites(),
		    BroadbandPropertyFileHandler.getPageUrlForManagedSites(),
		    BroadbandPropertyFileHandler.getPageTitleForManagedSites());

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToManagedSites():" + e.getMessage());
	}
	LOGGER.info("ENDING METHOD : navigateToManagedSites()");
	return result;
    }

    /**
     * Utility method to navigate Advanced Port Trigger Page from Gateway menu in LAN GUI Home page
     * 
     * @param device
     *            Dut instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param driver
     *            instance of {@link Webdriver}
     * @return True,if navigated and title verified Successfully
     * 
     * @Refactor Sruthi Santhosh
     */
    public boolean navigateToAdvancedTriggerPage(Dut device, AutomaticsTapApi tapEnv, WebDriver driver) {
	LOGGER.debug("STARTING METHOD : navigateToAdvancedTriggerPage()");
	boolean result = false;
	try {

	    result = verifyPageLaunchedStatusUseLinkOrlanuchUrl(device, tapEnv, driver,
		    BroadbandPropertyFileHandler.getLinkTextForPortTriggering(),
		    BroadbandPropertyFileHandler.getPageUrlForPortTriggering(),
		    BroadbandPropertyFileHandler.getPageTitleForPortTriggering());

	} catch (Exception e) {
	    LOGGER.error("Exception Occured in navigateToAdvancedTriggerPage():" + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : navigateToAdvancedTriggerPage()");
	return result;
    }
}

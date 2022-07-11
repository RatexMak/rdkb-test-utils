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
package com.automatics.rdkb.webui.page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automatics.device.Device;
import com.automatics.device.Dut;
import com.automatics.enums.Browser;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.constants.BroadBandPropertyKeyConstants;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.BroadBandWebPaConstants;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiElements;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiTestConstant;
import com.automatics.rdkb.webui.exception.PageTitleNotFoundException;
import com.automatics.rdkb.webui.utils.BroadBandWebUiUtils;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.CommonMethods;
import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.rdkb.utils.webpa.BroadBandWebPaUtils;
import com.automatics.rdkb.utils.wifi.connectedclients.BroadBandConnectedClientUtils;
import com.automatics.rdkb.constants.WebPaParamConstants.WebPaDataTypes;


public class LanWebGuiLoginPage  extends LanSideBasePage {
	
    /** SLF4j logger. */
    public static final Logger LOGGER = LoggerFactory.getLogger(LanWebGuiLoginPage.class);
    public static String ADMIN_PASSWORD = "";
	
    /**
     * 
     * Method to Login to Lan Page
     *
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link Dut}
     * @param connectedClientSettop
     *            Connected client device instance
     * @return true if login is successful
     * @refactor Govardhan
     * 
     */
    public static boolean logintoLanPage(AutomaticsTapApi tapEnv, Dut device, Dut clientSettop)
	    throws PageTitleNotFoundException, TestException {
	LOGGER.debug("STARTING METHOD :logintoLanPage() ");
	// Variable to store login status
	boolean loginStatus = false;
	// Variable to store admin page default username
	String defaultUserName = null;
	// Variable to store admin page default password
	String defaultPassword = null;
	// Variable to store URL
	String url = null;
	// Variable to store admin page new password
	String newPassword = null;
	// Stores Partner id
	String partnerId = null;
	// Stores Lan Ip address
	String lanipAddress = null;
	try {
	    newPassword = AutomaticsPropertyUtility.getProperty(BroadBandWebGuiTestConstant.ADMIN_PAGE_PASSWORD);
	    if (CommonMethods.isNull(newPassword)) {
		newPassword = BroadBandTestConstants.NON_DEFAULT_LOGIN_PASSWORD;
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception occured while retrieving non default password : " + e.getMessage());
	}
	try {
	    lanipAddress = BroadBandWebPaUtils.getParameterValuesUsingWebPaOrDmcli(device, tapEnv,
		    BroadBandWebPaConstants.WEBPA_PARAM_LAN_IP_ADDRESS);
	    if (CommonMethods.isNotNull(lanipAddress) && CommonMethods.isIpv4Address(lanipAddress)) {
		url = BroadBandTestConstants.URL_HTTP + lanipAddress + BroadBandTestConstants.SLASH_SYMBOL;
		LOGGER.info("URL to be launched: " + url);
	    } else {
		LOGGER.error("Unable to get LAN Ip address to launch LAN Admin GUI URL.");
		return loginStatus;
	    }
	    if (DeviceModeHandler.isBusinessClassDevice(device)) {
		defaultUserName = tapEnv.executeCommandUsingSsh(device,
			BroadBandWebGuiTestConstant.SSH_GET_DEFAULT_USERNAME_BUSINESS_CLASS);
		defaultPassword = tapEnv.executeCommandUsingSsh(device,
			BroadBandWebGuiTestConstant.SSH_GET_DEFAULT_PASSWORD_BUSINESS_CLASS);
	    } else {
		partnerId = tapEnv.executeWebPaCommand(device,
			BroadBandWebPaConstants.WEBPA_PARAM_FOR_SYNDICATION_PARTNER_ID);
		LOGGER.info("Current Partner ID of the device Retrieved via WEBPA is :" + partnerId);
		defaultUserName = tapEnv.executeCommandUsingSsh(device,
			BroadBandWebGuiTestConstant.SSH_GET_DEFAULT_USERNAME);
		defaultPassword = tapEnv.executeCommandUsingSsh(device,
			BroadBandWebGuiTestConstant.SSH_GET_DEFAULT_PASSWORD);
	    }
	    LOGGER.info("Setting password using WEBPA: " + newPassword);
	    if (DeviceModeHandler.isBusinessClassDevice(device)) {
		loginStatus = BroadBandWebPaUtils.setParameterValuesUsingWebPaOrDmcli(device, tapEnv,
			BroadBandWebPaConstants.WEBPA_PARAM_GUI_CUSADMIN_PASSWORD, WebPaDataTypes.STRING.getValue(),
			newPassword);
	    } else {
		loginStatus = BroadBandWebPaUtils.setParameterValuesUsingWebPaOrDmcli(device, tapEnv,
			BroadBandWebPaConstants.WEBPA_PARAM_GUI_ADMIN_PASSWORD, WebPaDataTypes.STRING.getValue(),
			newPassword);
	    }
	    if (loginStatus) {
		defaultPassword = newPassword;
	    } else {
		throw new TestException(
			"Unable to retrieve 'admin' password from device,unable to set using dmcli/webpa");
	    }
	    loginStatus = logintoLanPageinConnectedClient(tapEnv, device, clientSettop, url, defaultUserName,
		    defaultPassword);
	    ADMIN_PASSWORD = defaultPassword;
	} catch (Exception e) {
	    throw new TestException("" + ". Exception : " + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : logintoLanPage()");
	return loginStatus;
    }
    
    /**
     * 
     * Method to Launch browser and login to Lan Page with given username and password
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link Dut}
     * @param connectedClientSettop
     *            Connected client device instance
     * @return true if login is successful
     * @refactor Govardhan
     */
    public static boolean logintoLanPageinConnectedClient(AutomaticsTapApi tapEnv, Dut device, Dut clientSettop,
	    String url, String defaultUserName, String defaultPassword)
	    throws PageTitleNotFoundException, TestException {
	LOGGER.debug("STARTING METHOD :logintoLanPageinConnectedClient() ");
	// Variable to store login status
	boolean loginStatus = false;
	// Variable to store page launched status
	boolean adminPageLaunchedStatus = false;
	Device ecatsSettop = (Device) clientSettop;
	try {
	    // http://10.0.0.1/captiveportal.php
	    // Invoke browser in the Connected Client Setop
	    // Modified as part of DSL Devices to invoke the chromium browser in Linux Client
	    if (DeviceModeHandler.isDSLDevice(device) && (ecatsSettop.isLinux() || ecatsSettop.isRaspbianLinux())) {
		invokeBrowserinConnectedClient(clientSettop, Browser.CHROME.toString());
	    } else {
		invokeBrowserinConnectedClient(clientSettop);
	    }
	    LOGGER.info("URL to be launched in ADMIN UI login page ==> " + url);
	    if (null != driver) {
		if (CommonMethods.isNotNull(defaultPassword) && CommonMethods.isNotNull(defaultUserName)) {
		    // Launch broad band admin web page in browser
		    launchAdminPage(url);
		    if (DeviceModeHandler.isDSLDevice(device)) {
			waitForTextToAppear(BroadBandWebGuiTestConstant.STRING_WEB_GUI_LOGIN_PAGE_HEADING_BCI,
				By.xpath(BroadBandWebGuiTestConstant.ELEMENT_ID_LOGIN_PAGE_HEADING_BCI));
		    } else {

			try {
			    waitForTextToAppear(BroadBandWebGuiTestConstant.STRING_WEB_GUI_LOGIN_PAGE_HEADING,
				    By.xpath(BroadBandWebGuiTestConstant.ELEMENT_ID_LOGIN_PAGE_HEADING));
			} catch (Exception ex) {
			    LOGGER.info("Element not found" + ex.getMessage());
			}
			try {
			    waitForTextToAppear(BroadBandWebGuiTestConstant.STRING_WEB_GUI_LOGIN_PAGE_HEADING,
				    By.xpath(BroadBandWebGuiTestConstant.ELEMENT_ID_LOGIN_PAGE_HEADING_BCI));
			} catch (Exception ex) {
			    LOGGER.info("Element not found" + ex.getMessage());
			}

		    }
		    // Validate whether the login page is came or not
		    adminPageLaunchedStatus = validateLanPageLaunchedStatus();
		    if (adminPageLaunchedStatus) {
			loginStatus = loginIntoAdminPage(device, tapEnv, defaultUserName, defaultPassword);
			if (loginStatus) {
			    try {

				waitForTextToAppear(AutomaticsTapApi.getSTBPropsValue(BroadBandPropertyKeyConstants.PROP_KEY_WEB_GUI_HOME_PAGE_HEADING),
					By.xpath(BroadBandWebGuiTestConstant.ELEMENT_ID_HOME_PAGE_HEADING));
			    } catch (Exception e) {
				LOGGER.info(
					"#######################################################################################");
				LOGGER.info("Exception occured for DSL gateway page verification");
				LOGGER.info(
					"#######################################################################################");
			    }
			}
		    } else {
			throw new TestException("Unable to launch the browser or Admin UI login page");
		    }
		} else {
		    throw new TestException("Default Password of the device is null");
		}
	    } else {
		throw new TestException("Unable to launch the browser or  Admin UI login page : Driver is null");
	    }
	} catch (Exception e) {
	    throw new TestException("" + ". Exception : " + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : logintoLanPageinConnectedClient()");
	return loginStatus;
    }

    /**
     * Method to invoke browser in the Connected Client PC
     * 
     * @param connectedClientSettop
     *            Connected client device instance
     * @browser
     * @author bp-kraman513
     * @author Kanimozhi Raman
     * @refactor Govardhan
     */
    public static void invokeBrowserinConnectedClient(Dut clientSettop, String browser) {
	try {
	    BroadBandConnectedClientUtils.verifyBrowserAndDriverCapabilityOnConnectedClient(clientSettop);
	    if (browser.equalsIgnoreCase(Browser.CHROME.toString())) {
		setBrowserAndDriverCapabilities(clientSettop, Browser.CHROME);
	    } else {
		setBrowserAndDriverCapabilities(clientSettop, null);
	    }
	    PageFactory.initElements(driver, new LanSideBasePage());
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	} catch (Exception e) {
	    LOGGER.error("Exception occured invokeBrowserinConnectedClient:" + e.getMessage());
	}
    }
    
    /**
     * Utility method used to validate the LAN admin login page
     * 
     * @param device
     *            instance of {@link Dut}
     * @param tapEnv
     *            instance of {@link ECatsTapApi}
     * @param userName
     *            Username for LAN Admin Page
     * @param password
     *            Password for LAN Admin Page
     * @return True-Admin login success,Else-False
     * @refactor Govardhan
     */
    public static boolean loginIntoAdminPage(Dut device, AutomaticsTapApi tapEnv, String userName, String password) {
	boolean loginStatus = false;
	boolean isAlertPresent = false;
	String newPassword = null;
	try {
	    // Delete existing contents from user name and password
	    // text box
	    BroadBandWebUiUtils.deleteExistingContentFromTextBox(driver,
		    BroadBandWebGuiElements.ADMINUI_LOGIN_PAGE_USER_NAME);
	    BroadBandWebUiUtils.deleteExistingContentFromTextBox(driver,
		    BroadBandWebGuiElements.ADMINUI_LOGIN_PAGE_PASSWORD);
	    LOGGER.info("Username :" + userName.trim());
	    setUserName(userName.trim());
	    LOGGER.info("Password :" + password.trim());
	    setPassword(password.trim());
	    clickLogIn();
	    isAlertPresent = verifyAlertPresence(tapEnv, device);
	    if (isAlertPresent) {
		// Get new password from the properties file
		newPassword = AutomaticsPropertyUtility.getProperty(BroadBandWebGuiTestConstant.ADMIN_PAGE_PASSWORD);
		if (changePasswordPage(tapEnv, password, newPassword)) {
		    BroadBandWebUiUtils.deleteExistingContentFromTextBox(driver,
			    BroadBandWebGuiElements.ADMINUI_LOGIN_PAGE_USER_NAME);
		    BroadBandWebUiUtils.deleteExistingContentFromTextBox(driver,
			    BroadBandWebGuiElements.ADMINUI_LOGIN_PAGE_PASSWORD);
		    LOGGER.info("Username :" + userName.trim());
		    setUserName(userName.trim());
		    LOGGER.info("New Password :" + newPassword.trim());
		    setPassword(newPassword.trim());
		    clickLogIn();
		}
	    }
	    // Verify login status
	    BroadBandAtGlancePage homepage = new BroadBandAtGlancePage(driver);
	    loginStatus = homepage.verifyAtGlancePageLaunchedStatus();
	} catch (Exception e) {
	    LOGGER.error("Exception ocurred while login into Admin page :" + e.getMessage());
	}
	return loginStatus;
    }
    
    /**
     * Utility method is used to validate the presence of Alert message
     * 
     * @param tapEnv
     *            instance of {@link ECatsTapApi}
     * @param device
     *            instance of {@link Dut}
     * @return True-Alert present ,Eles -False
     * @author Govardhan
     */
    public static boolean verifyAlertPresence(AutomaticsTapApi tapEnv, Dut device) {
	boolean isAlertPresent = false;
	// Try & Catch to Handle exceptions if Alert box is not poped up
	try {
	    WebDriverWait wait = new WebDriverWait(driver, 30);
	    if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
		alertAccept();
		isAlertPresent = true;
		LOGGER.info("Browser Alert :" + isAlertPresent);
	    }
	} catch (Exception e) {
	    LOGGER.info("Exception occurred while validating Browser Alert :" + e.getMessage());
	}
	if (!isAlertPresent) {
	    if (DeviceModeHandler.isBusinessClassDevice(device)) {
		LOGGER.info("Validating UI Alert for Business class device");
		try {
		    LOGGER.info("Waiting for 'Are You Sure?' PopUp message.");
		    waitForTextToAppear(BroadBandWebGuiTestConstant.STRING_ARE_YOU_SURE_ALERT_MESSAGE,
			    By.xpath(BroadBandWebGuiTestConstant.XPATH_TO_GET_POP_TITLE));
		    LOGGER.info("Successfully verified the presents of 'Are You Sure?' popup message.");
		    String retrievedAlertMessage = driver
			    .findElement(BroadBandWebGuiTestConstant.XPATH_FOR_POPUP_MESSAGE).getText();
		    LOGGER.info("Alert message for Business Class Devices : " + retrievedAlertMessage);
		    isAlertPresent = CommonMethods.patternMatcher(retrievedAlertMessage,
			    BroadBandWebGuiTestConstant.PATTERN_LOGIN_ATTEMPT_ALERT);
		    driver.findElement(BroadBandWebGuiTestConstant.XPATH_FOR_OK_IN_POPUP_MESSAGE).click();
		    LOGGER.info("Popup OK Button Clicked to change the default password");
		    tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
		} catch (Exception e) {
		    LOGGER.info("Exception occurred while validating UI Alert for Business Class Device :"
			    + e.getMessage());
		}
	    } else {
		LOGGER.info("Validating UI Alert for Residential class device");
		try {
		    LOGGER.info("Waiting for 'Alert?' PopUp message.");
		    LOGGER.info("Alert Title:" + driver
			    .findElement(By.xpath(BroadBandWebGuiTestConstant.XPATH_TO_GET_POP_TITLE)).getText());
		    LOGGER.info("Alert Message:"
			    + driver.findElement(BroadBandWebGuiTestConstant.XPATH_FOR_POPUP_MESSAGE).getText());
		    waitForTextToAppear("Alert", By.xpath(BroadBandWebGuiTestConstant.XPATH_TO_GET_POP_TITLE));
		    LOGGER.info("Successfully verified the presents of 'Alert' popup message.");
		    String retrievedAlertMessage = driver
			    .findElement(BroadBandWebGuiTestConstant.XPATH_FOR_POPUP_MESSAGE).getText();
		    LOGGER.info("Alert message for Residential Class Devices  : " + retrievedAlertMessage);
		    isAlertPresent = CommonMethods.patternMatcher(retrievedAlertMessage,
			    BroadBandWebGuiTestConstant.PATTERN_DEFAULT_PASSWORD_ALERT);
		    LOGGER.info(BroadBandWebGuiTestConstant.PATTERN_DEFAULT_PASSWORD_ALERT + ":" + isAlertPresent);
		    driver.findElement(BroadBandWebGuiTestConstant.XPATH_FOR_OK_IN_POPUP_MESSAGE).click();
		    LOGGER.info("Popup OK Button Clicked to change the default password");
		    tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
		} catch (Exception e) {
		    LOGGER.info(
			    "Exception occurred while validating UI Alert for Residential Device:" + e.getMessage());
		}
	    }
	    LOGGER.info("UI Alert :" + isAlertPresent);
	}
	return isAlertPresent;
    }

    /**
     * Utility method is used to change the password from LAN admin page
     * 
     * @param tapEnv
     *            instance of {@link Dut}
     * @param defaultPassword
     *            Default Password
     * @param newPassword
     *            New password
     * @return True-New password changed , Else-False
     * @refactor Govardhan
     */
    public static boolean changePasswordPage(AutomaticsTapApi tapEnv, String defaultPassword, String newPassword) {
	boolean changeStatus = false;
	boolean isAlertPresent = false;
	try {
	    LOGGER.info("Redirected to Change the default Password Page");
	    LOGGER.info("Waiting for 1 min for page loading.");
	    tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
	    // Change default password
	    sendKeys(By.xpath(BroadBandWebGuiTestConstant.XPATH_ENTER_OLD_PASSWORD), defaultPassword);
	    sendKeys(By.xpath(BroadBandWebGuiTestConstant.XPATH_ENTER_NEW_USER_PASSWORD), newPassword);
	    sendKeys(By.xpath(BroadBandWebGuiTestConstant.XPATH_VERIFY_RE_ENTER_NEW_USER_PASSWORD), newPassword);
	    click(By.xpath(BroadBandWebGuiTestConstant.XPATH_SAVE_NEW_USER_PASSWORD));
	    // Wait for 10 seconds to save settings
	    tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
	    click(By.xpath(BroadBandWebGuiTestConstant.XPATH_CLICK_OK_AFTER_SAVING_NEW_PASSWORD));
	    LOGGER.info("#######################################################################################");
	    LOGGER.info("Default Password Changed Successfully to:" + newPassword);
	    LOGGER.info("#######################################################################################");
	    try {
		// Wait for 10 seconds to save settings
		tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
		changeStatus = validateLanPageLaunchedStatus();
	    } catch (Exception e) {
		LOGGER.info(
			"Exception occurred while validating Login page in after password change :" + e.getMessage());
	    }
	    if (!changeStatus) {
		try {
		    LOGGER.info("Waiting for 'Alert' PopUp message.");
		    LOGGER.info("Alert Title:" + driver
			    .findElement(By.xpath(BroadBandWebGuiTestConstant.XPATH_TO_GET_POP_TITLE)).getText());
		    LOGGER.info("Alert Message:"
			    + driver.findElement(BroadBandWebGuiTestConstant.XPATH_FOR_POPUP_MESSAGE).getText());
		    waitForTextToAppear("Alert", By.xpath(BroadBandWebGuiTestConstant.XPATH_TO_GET_POP_TITLE));
		    LOGGER.info("Successfully verified the presents of 'Alert' popup message.");
		    String retrievedAlertMessage = driver
			    .findElement(BroadBandWebGuiTestConstant.XPATH_FOR_POPUP_MESSAGE).getText();
		    LOGGER.info("Alert message changePasswordPage() : " + retrievedAlertMessage);
		    isAlertPresent = CommonMethods.patternMatcher(retrievedAlertMessage,
			    BroadBandWebGuiTestConstant.PATTERN_PASSWORD_CHANGED_ALERT);
		    LOGGER.info(BroadBandWebGuiTestConstant.PATTERN_PASSWORD_CHANGED_ALERT + ":" + isAlertPresent);
		    driver.findElement(BroadBandWebGuiTestConstant.XPATH_FOR_OK_IN_POPUP_MESSAGE).click();
		    LOGGER.info("Popup OK Button Clicked after change password ");
		    if (isAlertPresent) {
			// Wait for 10 seconds to save settings
			tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
			changeStatus = validateLanPageLaunchedStatus();
		    }
		} catch (Exception e) {
		    LOGGER.info(
			    "Exception occurred while validating UI Alert in change Password Page :" + e.getMessage());
		}
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception ocurred while changing the default password :" + e.getMessage());
	}
	return changeStatus;
    }
    
    /**
     * Helper method to validate Default Password change prompt in admin page
     * 
     * @param driver
     *            WebDriver instance
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param settop
     *            Dut instance
     * @param clientDut
     *            connected client settop instance
     * @param url
     *            Admin page url
     * @return true if default password change prompt appeared
     * @author Praveenkumar Paneerselvam
     * @refactor Rakesh C N
     */
    public static boolean validateDefaultPasswordChangePromptInAdminPage(WebDriver driver, AutomaticsTapApi tapEnv,
	    Dut settop, Dut clientSettop, String url) {
	LOGGER.debug("STARTING METHOD :validateDefaultPasswordChangePromptInAdminPage() ");
	// Variable to store login status
	boolean loginStatus = false;
	// Variable to store admin page default username
	String defaultUserName = null;
	// Variable to store admin page default password
	String defaultPassword = null;
	// Variable to store admin page new password
	try {
	    if (DeviceModeHandler.isBusinessClassDevice(settop)) {
		defaultUserName = tapEnv.executeCommandUsingSsh(settop,
			BroadBandWebGuiTestConstant.SSH_GET_DEFAULT_USERNAME_BUSINESS_CLASS);
		defaultPassword = tapEnv.executeCommandUsingSsh(settop,
			BroadBandWebGuiTestConstant.SSH_GET_DEFAULT_PASSWORD_BUSINESS_CLASS);
	    } else {
		defaultUserName = tapEnv.executeCommandUsingSsh(settop,
			BroadBandWebGuiTestConstant.SSH_GET_DEFAULT_USERNAME);
		defaultPassword = tapEnv.executeCommandUsingSsh(settop,
			BroadBandWebGuiTestConstant.SSH_GET_DEFAULT_PASSWORD);
		if (CommonMethods.isNull(defaultPassword)) {
		    defaultPassword = BroadBandTestConstants.STRING_PASSWORD;
		}
	    }
	    LOGGER.info("URL to be launched in ADMIN UI login page ==> " + url);
	    if (null != driver) {
		if (CommonMethods.isNotNull(defaultPassword) && CommonMethods.isNotNull(defaultUserName)) {
		    // Launch broad band admin web page in browser
		    launchAdminPage(url);
		    LOGGER.info("URL Launched.Waiting for text to appear.");
		    if (DeviceModeHandler.isBusinessClassDevice(settop)) {
			try {
			    waitForTextToAppear(BroadBandWebGuiTestConstant.STRING_WEB_GUI_LOGIN_PAGE_HEADING_BCI,
				    By.xpath(BroadBandWebGuiTestConstant.ELEMENT_ID_LOGIN_PAGE_HEADING_BCI));
			} catch (Exception ex) {
			    LOGGER.info("Element not found" + ex.getMessage());
			}
		    } else {
			
			try {
			    waitForTextToAppear(BroadBandWebGuiTestConstant.STRING_WEB_GUI_LOGIN_PAGE_HEADING,
				    By.xpath(BroadBandWebGuiTestConstant.ELEMENT_ID_LOGIN_PAGE_HEADING));
			} catch (Exception ex) {
			    LOGGER.info("Element not found" + ex.getMessage());
			}
			try {
			    waitForTextToAppear(BroadBandWebGuiTestConstant.STRING_WEB_GUI_LOGIN_PAGE_HEADING,
				    By.xpath(BroadBandWebGuiTestConstant.ELEMENT_ID_LOGIN_PAGE_HEADING_BCI));
			} catch (Exception ex) {
			    LOGGER.info("Element not found" + ex.getMessage());
			}

		    }
		    // Validate whether the login page is came or not
		    if (validateLanPageLaunchedStatus()) {
			// Delete existing contents from user name and password
			BroadBandWebUiUtils.deleteExistingContentFromTextBox(driver,
				BroadBandWebGuiElements.ADMINUI_LOGIN_PAGE_USER_NAME);
			BroadBandWebUiUtils.deleteExistingContentFromTextBox(driver,
				BroadBandWebGuiElements.ADMINUI_LOGIN_PAGE_PASSWORD);
			LOGGER.info("Default Username:" + defaultUserName.trim());
			setUserName(defaultUserName.trim());
			LOGGER.info("Default Password:" + defaultPassword.trim());
			setPassword(defaultPassword.trim());
			clickLogIn();
			loginStatus = verifyAlertPresence(tapEnv, settop);
		    } else {
			throw new TestException("Unable to launch the browser or ADMIN UI page for login");
		    }
		} else {
		    throw new TestException("Default Password of the device is Null");
		}
	    } else {
		throw new TestException("Unable to launch the browser or ADMIN UI page for login : Driver is null");
	    }
	} catch (Exception e) {
	    throw new TestException("" + ". Exception : " + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : validateDefaultPasswordChangePromptInAdminPage()");
	return loginStatus;
    }
    
    /**
     * Helper method to change password from password change prompt
     * 
     * @param settop
     *            Dut instance
     * @param tapEnv
     *            AutomaticsTapApi  instance
     * @param oldPassword
     *            old password for the user
     * @param newPassword
     *            new password to be changed
     * @return true, if password is changed.
     * 
     * @author Praveenkumar Paneerselvam
     * @refactor Rakesh C N
     */
    public static boolean changePasswordFromPasswordChangePrompt(Dut device, AutomaticsTapApi tapEnv, String oldPassword,
	    String newPassword) {
	LOGGER.debug("STARTING METHOD : changePasswordFromPasswordChangePrompt()");
	boolean status = false;
	//String key = BroadBandCommonUtils.getPartnerValue(device, tapEnv);
	try {
	   // String changePasswordPageTitle = BroadBandWebGuiTestConstant.DEVICE_MODEL_AND_TITLE_MAPPING.get(key)
		//    .get(BroadBandTestConstants.CHANGE_PASSWORD);
	    String changePasswordPageTitle=BroadbandPropertyFileHandler.getPageTitleForPasswordchange();
	    if (BroadBandWebUiUtils.validatePageLaunchedStatusWithPageTitle(driver, changePasswordPageTitle)) {
		LOGGER.info("Redirected to Change the default Password Page");
		// Change default password
		sendKeys(By.xpath(BroadBandWebGuiTestConstant.XPATH_ENTER_OLD_PASSWORD), oldPassword);
		sendKeys(By.xpath(BroadBandWebGuiTestConstant.XPATH_ENTER_NEW_USER_PASSWORD), newPassword);
		sendKeys(By.xpath(BroadBandWebGuiTestConstant.XPATH_VERIFY_RE_ENTER_NEW_USER_PASSWORD), newPassword);
		click(By.xpath(BroadBandWebGuiTestConstant.XPATH_SAVE_NEW_USER_PASSWORD));
		// Wait for 10 seconds to save settings
		tapEnv.waitTill(BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS);
		click(By.xpath(BroadBandWebGuiTestConstant.XPATH_CLICK_OK_AFTER_SAVING_NEW_PASSWORD));
		LOGGER.info("#######################################################################################");
		LOGGER.info("Default Password Changed Successfully to:" + newPassword);
		LOGGER.info("#######################################################################################");
		// Wait for 10 seconds to save settings
		tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);
		status = getUserName() != null ? getUserName().isDisplayed() : false;
	    }
	} catch (Exception exception) {
	    LOGGER.error("Exception Occured while changing password from password change prompt. Exception - "
		    + exception.getMessage());
	}
	LOGGER.debug("ENDING METHOD : changePasswordFromPasswordChangePrompt()");
	return status;
    }
    
    /**
     * 
     * Method to Login to Lan Page username and default password
     *
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param device
     *            instance of {@link Dut}
     * @param connectedClientSettop
     *            Connected client device instance
     * @return true if login is successful
     * @refactor Said Hisham
     */
    public static boolean logintoLanPageUsingDefaultPassWord(AutomaticsTapApi tapEnv, Dut device, Dut clientSettop)
	    throws PageTitleNotFoundException, TestException {
	LOGGER.debug("STARTING METHOD :logintoLanPageUsingDefaultPassWord() ");
	// Variable to store login status
	boolean loginStatus = false;
	// Variable to store admin page default username
	String defaultUserName = null;
	// Variable to store admin page default password
	String defaultPassword = null;
	// Variable to store URL
	String url = null;
	// Variable to store admin page new password
	String newPassword = null;
	try {
	    if (DeviceModeHandler.isBusinessClassDevice(device)) {
		newPassword = BroadBandTestConstants.STRING_BUSINESS_CLASS_DEFAULT_ADMIN_PWD;
	    } else {
		newPassword = AutomaticsTapApi.getSTBPropsValue(BroadBandWebGuiTestConstant.ADMIN_PAGE_PASSWORD);
		if (CommonMethods.isNull(newPassword)) {
		    newPassword = BroadBandTestConstants.NON_DEFAULT_LOGIN_PASSWORD;
		}
	    }
	    LOGGER.info("The newpassword retrieved is : " + newPassword);
	} catch (Exception e) {
	    LOGGER.error("Exception occured while retrieving non default password : " + e.getMessage());
	}
	try {
	    if (DeviceModeHandler.isBusinessClassDevice(device)) {
		url = AutomaticsTapApi.getSTBPropsValue(BroadBandWebGuiTestConstant.ADMIN_PAGE_URL_BUSINESS_CLASS);
		defaultUserName = tapEnv.executeCommandUsingSsh(device,
			BroadBandWebGuiTestConstant.SSH_GET_DEFAULT_USERNAME_BUSINESS_CLASS);
		LOGGER.info("The default user name is : " + defaultUserName);
		defaultPassword = tapEnv.executeCommandUsingSsh(device,
			BroadBandWebGuiTestConstant.SSH_GET_DEFAULT_PASSWORD_BUSINESS_CLASS);
		LOGGER.info("The default password is : " + defaultPassword);
	    } else {
		url = AutomaticsTapApi.getSTBPropsValue(BroadBandWebGuiTestConstant.ADMIN_PAGE_URL);
		if (DeviceModeHandler.isDSLDevice(device)) {
		    url = AutomaticsTapApi.getSTBPropsValue(BroadBandWebGuiTestConstant.DSL_PAGE_URL);
		    LOGGER.info("DSL device Page URL is : " + url);
		}
		defaultUserName = tapEnv.executeCommandUsingSsh(device,
			BroadBandWebGuiTestConstant.SSH_GET_DEFAULT_USERNAME);
		defaultPassword = tapEnv.executeCommandUsingSsh(device,
			BroadBandWebGuiTestConstant.SSH_GET_DEFAULT_PASSWORD);
	    }
	    if (CommonMethods.isNull(defaultPassword)) {
		if (DeviceModeHandler.isDSLDevice(device)) {
		    loginStatus = BroadBandWebPaUtils.setParameterValuesUsingWebPaOrDmcli(device, tapEnv,
			    BroadBandWebPaConstants.WEBPA_PARAM_GUI_ADMIN_PASSWORD, WebPaDataTypes.STRING.getValue(),
			    newPassword);
		} else {
		    loginStatus = BroadBandWebPaUtils.setParameterValuesUsingWebPaOrDmcli(device, tapEnv,
			    BroadBandWebPaConstants.WEBPA_PARAM_GUI_CUSADMIN_PASSWORD, WebPaDataTypes.STRING.getValue(),
			    newPassword);
		}
		if (loginStatus) {
		    defaultPassword = newPassword;
		} else {
		    throw new TestException(
			    "Unable to retrieve 'admin' password from device,unable to set using dmcli/webpa");
		}
	    }
	    loginStatus = logintoLanPageinConnectedClientUsingDefaultPassWord(tapEnv, device, clientSettop, url,
		    defaultUserName, defaultPassword);
	} catch (Exception e) {
	    throw new TestException("" + ". Exception : " + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : logintoLanPageUsingDefaultPassWord()");
	return loginStatus;
    }
    
    /**
     * 
     * Method to Launch browser and login to Lan Page with given username and default password
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * 
     * @param device
     *            instance of {@link Dut}
     * @param connectedClientSettop
     *            Connected client device instance
     * @return true if login is successful
     * @refactor Said Hisham
     */
    public static boolean logintoLanPageinConnectedClientUsingDefaultPassWord(AutomaticsTapApi tapEnv, Dut device,
	    Dut clientSettop, String url, String defaultUserName, String defaultPassword)
	    throws PageTitleNotFoundException, TestException {
	LOGGER.debug("STARTING METHOD :logintoLanPageinConnectedClientUsingDefaultPassWord() ");
	// Variable to store login status
	boolean loginStatus = false;
	// Variable to store page launched status
	boolean adminPageLaunchedStatus = false;
	boolean isAlertPresent = false;
	Device ecatsSettop = (Device) clientSettop;
	try {

	    if (DeviceModeHandler.isDSLDevice(device) && (ecatsSettop.isLinux() || ecatsSettop.isRaspbianLinux())) {
		invokeBrowserinConnectedClient(clientSettop, Browser.CHROME.toString());
	    } else {
		invokeBrowserinConnectedClient(clientSettop);
	    }
	    LOGGER.info("URL to be launched in ADMIN UI login page ==> " + url);
	    if (null != driver) {
		if (CommonMethods.isNotNull(defaultPassword) && CommonMethods.isNotNull(defaultUserName)) {
		    // Launch LAN GUI Admin web page in browser
		    launchAdminPage(url);
		    if (DeviceModeHandler.isBusinessClassDevice(device)) {
			waitForTextToAppear(BroadBandWebGuiTestConstant.STRING_WEB_GUI_LOGIN_PAGE_HEADING_BCI,
				By.xpath(BroadBandWebGuiTestConstant.ELEMENT_ID_LOGIN_PAGE_HEADING_BCI));
		    } else {
			try {
			    waitForTextToAppear(BroadBandWebGuiTestConstant.STRING_WEB_GUI_LOGIN_PAGE_HEADING,
				    By.xpath(BroadBandWebGuiTestConstant.ELEMENT_ID_LOGIN_PAGE_HEADING));
			} catch (Exception ex) {
			    LOGGER.info("Element not found" + ex.getMessage());
			}
			try {
			    waitForTextToAppear(BroadBandWebGuiTestConstant.STRING_WEB_GUI_LOGIN_PAGE_HEADING,
				    By.xpath(BroadBandWebGuiTestConstant.ELEMENT_ID_LOGIN_PAGE_HEADING_BCI));
			} catch (Exception ex) {
			    LOGGER.info("Element not found" + ex.getMessage());
			}

		    }
		    // Validate whether the login page is came or not
		    adminPageLaunchedStatus = validateLanPageLaunchedStatus();
		    if (adminPageLaunchedStatus) {
			// Delete existing contents from user name and password
			// text box
			BroadBandWebUiUtils.deleteExistingContentFromTextBox(driver,
				BroadBandWebGuiElements.ADMINUI_LOGIN_PAGE_USER_NAME);
			BroadBandWebUiUtils.deleteExistingContentFromTextBox(driver,
				BroadBandWebGuiElements.ADMINUI_LOGIN_PAGE_PASSWORD);
			LOGGER.info("Default Username:" + defaultUserName.trim());
			setUserName(defaultUserName.trim());
			LOGGER.info("Default Password:" + defaultPassword.trim());
			setPassword(defaultPassword.trim());
			clickLogIn();
			// Try & Catch to Handle exceptions if Alert box is not poped up
			try {
			    WebDriverWait wait = new WebDriverWait(driver, 30);
			    if (wait.until(ExpectedConditions.alertIsPresent()) != null) {
				alertAccept();
				isAlertPresent = true;
			    }
			} catch (Exception e) {
			    LOGGER.info("Exception occurred while validating Browser Alert :" + e.getMessage());
			}
			if (!isAlertPresent) {
			    LOGGER.info("Validating UI Alert");
			    try {
				LOGGER.info("Waiting for 'Are You Sure?' PopUp message.");
				waitForTextToAppear(BroadBandWebGuiTestConstant.STRING_ARE_YOU_SURE_ALERT_MESSAGE,
					By.xpath(BroadBandWebGuiTestConstant.XPATH_TO_GET_POP_TITLE));
				LOGGER.info("Successfully verified the presents of 'Are You Sure?' popup message.");
				String retrievedAlertMessage = driver
					.findElement(BroadBandWebGuiTestConstant.XPATH_FOR_POPUP_MESSAGE).getText();
				LOGGER.info("ALERT MESSAGE CONTENT : " + retrievedAlertMessage);
				isAlertPresent = CommonMethods.patternMatcher(retrievedAlertMessage,
					BroadBandWebGuiTestConstant.PATTERN_LOGIN_ATTEMPT_ALERT);
				LOGGER.info("isAlertPresent : " + isAlertPresent);
				if (isAlertPresent) {
				    try {
					click(By.id(BroadBandWebGuiElements.ELEMENT_ID_FOR_CANCEL_IN_POPUP_MESSAGE));
					tapEnv.waitTill(BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS);
					LOGGER.info("Cancelled button clicked by using ID ");

				    } catch (Exception e) {
					click(By.xpath(
						BroadBandWebGuiElements.XPATH_FOR_CANCEL_IN_ETHWAN_POPUP_MESSAGE));
					tapEnv.waitTill(BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS);
					LOGGER.info("Cancelled button clicked by using xpath ");
				    }
				}
			    } catch (Exception e) {
				LOGGER.info("Exception occurred while validating UI Alert :" + e.getMessage());
			    }
			}
			try {
			    if (DeviceModeHandler.isDSLDevice(device))
				waitForTextToAppear(BroadBandWebGuiTestConstant.STRING_WEB_GUI_HOME_PAGE_HEADING,
					By.xpath(BroadBandWebGuiTestConstant.ELEMENT_ID_HOME_PAGE_HEADING));

			} catch (Exception e) {
			    LOGGER.info(
				    "#######################################################################################");
			    LOGGER.info("Exception occured for DSL Device gateway page verification");
			    LOGGER.info(
				    "#######################################################################################");
			}
			// Verify login status
			BroadBandAtGlancePage homepage = new BroadBandAtGlancePage(driver);
			loginStatus = homepage.verifyAtGlancePageLaunchedStatus();
		    } else {
			throw new TestException("Unable to launch the browser or LAN GUI Admin page for login");
		    }
		} else {
		    throw new TestException("Default Password of the device is null");
		}
	    } else {
		throw new TestException(
			"Unable to launch the browser or LAN GUI Admin page for login : Driver is null");
	    }
	} catch (Exception e) {
	    throw new TestException("" + ". Exception : " + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : logintoLanPageinConnectedClientUsingDefaultPassWord()");
	return loginStatus;
    }


}

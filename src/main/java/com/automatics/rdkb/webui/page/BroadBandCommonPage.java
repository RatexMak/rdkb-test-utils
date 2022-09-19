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

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Dut;
import com.automatics.exceptions.TestException;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;
import com.automatics.rdkb.utils.CommonUtils;
import com.automatics.rdkb.utils.DeviceModeHandler;
import com.automatics.rdkb.utils.wifi.connectedclients.BroadBandConnectedClientInfo;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiElements;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiTestConstant;
import com.automatics.rdkb.webui.utils.BroadBandWebUiUtils;

public class BroadBandCommonPage extends BroadBandBasePage {

    /**
     * The {@link BroadBandCommonPage} constructor.
     * 
     * @param driver
     *            The web driver instance.
     */
    public BroadBandCommonPage(WebDriver driver) {
	super(driver);
    }

    /**
     * Method to get the text from web page
     * 
     */
    public String getText(By elementId) throws Exception {
	return driver.findElement(elementId).getText();
    }

    /**
     * Method to click the web element ID
     * 
     */
    public void click(By elementId) {
	driver.findElement(elementId).click();
    }

    /**
     * Method to verify the non existence of element; validation is performed based on the web element xpath.
     * 
     * @param webElementXpath
     * @return True if element does not exists ; else false
     * @author Deepa Bada
     * @refactor Said Hisham
     */
    public boolean isElementByXpathAvailable(String webElementXpath) {
	LOGGER.debug("STARTING METHOD: isElementByXpathAvailable");
	boolean result = false;
	try {
	    driver.findElement(By.xpath(webElementXpath));
	    LOGGER.error("Element present in UI");
	    result = true;
	} catch (org.openqa.selenium.NoSuchElementException noSuchElementException) {
	    // Log & Suppress the Exception
	    LOGGER.info("Element is not present in UI");

	}
	LOGGER.info(" Is Element Present : " + result);
	LOGGER.debug("ENDING METHOD: isElementByXpathAvailable");
	return result;
    }

    /**
     * Utils method to get the Connected client Info of all Online Devices
     * 
     * @param driver
     *            Web driver
     * @return list of all online Devices
     */
    public static List<BroadBandConnectedClientInfo> getDetailsOfActiveDevicesFromGui(WebDriver driver) {
	String hostNameXpath = null;
	String clientInfoXpath = null;
	String connectTypeXpath = null;
	String rssiLevelXpath = null;
	int iterationCount = 1;
	BroadBandConnectedClientInfo connectedClientInfo = null;
	List<BroadBandConnectedClientInfo> listOfActiveDevices = new ArrayList<>();
	BroadBandCommonPage connectionPage = new BroadBandCommonPage(driver);

	do {
	    iterationCount++;
	    // Starting with 2nd Row, As 1st Row is for Heading
	    hostNameXpath = BroadBandWebGuiElements.ELEMENT_XPATH_ONLINE_DEVICES_HOST_NAME
		    .replaceAll(BroadBandTestConstants.PLACE_HOLDER_FOR_ROW_NUMBER, Integer.toString(iterationCount));
	    connectTypeXpath = BroadBandWebGuiElements.ELEMENT_XPATH_ONLINE_DEVICES_CONNECTION_TYPE
		    .replaceAll(BroadBandTestConstants.PLACE_HOLDER_FOR_ROW_NUMBER, Integer.toString(iterationCount));
	    clientInfoXpath = BroadBandWebGuiElements.ELEMENT_XPATH_ONLINE_DEVICES_INFO
		    .replaceAll(BroadBandTestConstants.PLACE_HOLDER_FOR_ROW_NUMBER, Integer.toString(iterationCount));
	    connectedClientInfo = new BroadBandConnectedClientInfo();

	    rssiLevelXpath = BroadBandWebGuiElements.ELEMENT_XPATH_RSSI_LEVEL.replaceAll("##ROW##",
		    Integer.toString(iterationCount));
	    try {
		// Exception occurs in case there is no value for the given xpath ie., Execution
		// breaks in case no
		// further rows are available.
		connectionPage.click(By.xpath(hostNameXpath));
	    } catch (Exception e) {
		LOGGER.error(
			"BREAKING EXECUTION, AS NO FURTHER ROWS ARE AVAILABLE IN THE 'ONLINE DEVICES - PRIVATE NETWORK TABLE'.");
		break;
	    }
	    try {
		String hostName = connectionPage.getText(By.xpath(hostNameXpath));
		if (CommonMethods.isNotNull(hostName)) {
		    connectedClientInfo.setHostName(hostName.trim());
		    connectedClientInfo.setActiveStatus(true);
		}

		String rssiLevel = connectionPage.getText(By.xpath(rssiLevelXpath));
		if (CommonMethods.isNotNull(rssiLevel)) {
		    connectedClientInfo.setrssiLevel(rssiLevel.trim());
		}

		String connectionType = connectionPage.getText(By.xpath(connectTypeXpath));
		if (CommonMethods.isNotNull(connectionType)) {
		    connectedClientInfo.setConnectionType(connectionType.trim());
		}
		String clientInfo = LanSideBasePage.getText(By.xpath(clientInfoXpath)).toLowerCase();
		if (CommonMethods.isNotNull(clientInfo)) {
		    String ipAddress = CommonMethods.patternFinder(clientInfo,
			    BroadBandTestConstants.PATTERN_TO_GET_IPV4_ADDRESS);
		    String macAddress = CommonMethods.patternFinder(clientInfo,
			    BroadBandTestConstants.PATTERN_TO_GET_MAC_ADDRESS);
		    if (CommonMethods.isIpv4Address(ipAddress)) {
			connectedClientInfo.setIpv4Address(ipAddress);
		    }
		    if (CommonMethods.isNotNull(macAddress)) {
			macAddress = macAddress
				.replace(BroadBandTestConstants.COLON, BroadBandTestConstants.EMPTY_STRING)
				.toUpperCase();
			connectedClientInfo.setMacAddress(macAddress);
		    }
		}
		LOGGER.info("DETAILS OF CONNECTED CLIENT IN ROW " + (iterationCount - 1) + ":-"
			+ connectedClientInfo.toString());
		listOfActiveDevices.add(connectedClientInfo);

	    } catch (Exception ex) {
		LOGGER.error(
			"Exception occured while retrieving Connected clients details from GUI : " + ex.getMessage());
	    }
	} while (iterationCount <= BroadBandTestConstants.CONSTANT_10);
	// Assuming there are 9 clients connected to the gateway and listed in web GUI
	// from row #2-#10 under "Connected
	// Devices --> online Devices" section.

	return listOfActiveDevices;
    }

    /**
     * Utils to enable/disable Radio button
     * 
     * @param tapEnv
     *            tapEnv
     * @param xpath
     *            Radio button xPath
     * @return true if the radio button is enabled
     * @refactor Rakesh C N
     */
    public static boolean enableOrDisableRadioButton(AutomaticsTapApi tapEnv, String xpath) {

	boolean status = false;

	try {
	    LOGGER.info("Clicking on radio button");
	    LanSideBasePage.click(By.xpath(xpath));
	    tapEnv.waitTill(BroadBandTestConstants.FIVE_SECONDS_IN_MILLIS);
	    status = BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
		    BroadBandTestConstants.TRUE,
		    LanSideBasePage.getTextUsingAttribute(
			    By.xpath(xpath.replace(BroadBandWebGuiElements.LABLE_TAG_IN_XPATH,
				    BroadBandTestConstants.EMPTY_STRING)),
			    BroadBandWebGuiTestConstant.ARIA_CHECKED_ATTRIBUTE));
	} catch (Exception e) {
	    LOGGER.error("Exception occurred while enabling/disabling Radio Button : " + e.getMessage());
	}
	return status;
    }

    /**
     * Utils method to add Parental Control Invalid Rule
     * 
     * @param tapEnv
     *            tapEnv
     * @param type
     *            Parental Control rule type
     * 
     * @return true if the rule is added
     * @refactor Rakesh C N
     */
    public static boolean addParentalControlInvalidRule(AutomaticsTapApi tapEnv, String type) {

	boolean status = false;
	LanSideBasePage basePage = new LanSideBasePage();
	String saveButton = "";

	switch (type) {

	case BroadBandTestConstants.MANAGED_SITES_ADD_SITES:
	    LanSideBasePage
		    .clear(By.xpath(BroadBandWebGuiElements.XPATH_PARENTAL_CONTROL_MANAGED_SITES_ADD_SITES_TEXT_BOX));
	    LanSideBasePage.sendKeys(
		    By.xpath(BroadBandWebGuiElements.XPATH_PARENTAL_CONTROL_MANAGED_SITES_ADD_SITES_TEXT_BOX),
		    BroadBandTestConstants.URL_HTTP_FACEBOOK);
	    saveButton = BroadBandWebGuiElements.ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SITES_SAVE_BLOCKED_SITES;
	    break;

	case BroadBandTestConstants.MANAGED_SITES_ADD_KEYWORD:
	    LanSideBasePage.sendKeys(
		    By.xpath(BroadBandWebGuiElements.XPATH_PARENTAL_CONTROL_MANAGED_SITES_ADD_KEYWORD_TEXT_BOX),
		    BroadBandWebGuiTestConstant.STRING_GOOGLE);
	    saveButton = BroadBandWebGuiElements.ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SITES_SAVE_BLOCKED_SITES;
	    break;

	case BroadBandTestConstants.MANAGED_SERVICES:
	    LanSideBasePage.sendKeys(By.xpath(
		    BroadBandWebGuiElements.ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SERVICE_USER_DEFINED_SERVICE_TEXT_BOX),
		    BroadBandWebGuiTestConstant.STRING_USER_DEFINED_SERVICE);
	    LanSideBasePage.sendKeys(
		    By.xpath(
			    BroadBandWebGuiElements.ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SERVICE_START_PORT_TEXT_BOX),
		    BroadBandTestConstants.START_PORT);
	    LanSideBasePage.sendKeys(
		    By.xpath(BroadBandWebGuiElements.ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SERVICE_END_PORT_TEXT_BOX),
		    BroadBandTestConstants.END_PORT);
	    saveButton = BroadBandWebGuiElements.ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SERVICES_SAVE_BLOCKED_SERVICES;
	    break;

	case BroadBandTestConstants.MANAGED_DEVICES:
	    LanSideBasePage.sendKeys(By.xpath(
		    BroadBandWebGuiElements.ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SERVICE_COMPUTER_NAME_TEXT_BOX),
		    BroadBandTestConstants.VALUE_MACADDRESS_FIRST_XDNS);
	    LanSideBasePage.sendKeys(By
		    .xpath(BroadBandWebGuiElements.ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SERVICE_MAC_ADDRESS_TEXT_BOX),
		    BroadBandTestConstants.VALUE_MACADDRESS_FIRST_XDNS);
	    saveButton = BroadBandWebGuiElements.ELEMENT_XPATH_PARENTAL_CONTROL_MANAGED_SITES_SAVE_BLOCKED_DEVICE;
	    break;
	}
	try {
	    if (BroadBandCommonPage.enableOrDisableRadioButton(tapEnv,
		    BroadBandWebGuiElements.XPATH_PARENTAL_CONTROL_ALWAYS_BLOCK_NO_BUTTON)) {
		if (basePage.selectBlockedTimeValuesInParentalControl(
			BroadBandWebGuiElements.XPATH_PARENTAL_CONTROL_START_TIME_HOUR,
			BroadBandWebGuiElements.XPATH_PARENTAL_CONTROL_START_TIME_MINUTE,
			BroadBandWebGuiElements.XPATH_PARENTAL_CONTROL_START_TIME_AM_PM,
			BroadBandTestConstants.STRING_VALUE_11, BroadBandTestConstants.DOUBLE_ZERO_IN_DOUBLE_QUOTES,
			BroadBandTestConstants.TIME_AM)) {
		    if (basePage.selectBlockedTimeValuesInParentalControl(
			    BroadBandWebGuiElements.XPATH_PARENTAL_CONTROL_END_TIME_HOUR,
			    BroadBandWebGuiElements.XPATH_PARENTAL_CONTROL_END_TIME_MINUTE,
			    BroadBandWebGuiElements.XPATH_PARENTAL_CONTROL_END_TIME_AM_PM,
			    BroadBandTestConstants.STRING_VALUE_9, BroadBandTestConstants.DOUBLE_ZERO_IN_DOUBLE_QUOTES,
			    BroadBandTestConstants.TIME_AM)) {

			status = LanSideBasePage.verifyAndAcceptAlert(saveButton,
				BroadBandTestConstants.POPUP_TTILE_ALERT,
				BroadBandTestConstants.POP_UP_MESSAGE_START_TIME_GREATER_THAN_END_TIME, tapEnv);

			BroadBandWebUiUtils.clickById(LanSideBasePage.getDriver(),
				BroadBandWebGuiElements.ELEMENT_ID_CANCEL_PORT_FORWARDING_CONFIGURATION);
		    }
		}
	    }

	} catch (Exception e) {
	    LOGGER.error("Exception occurred while adding Parental Control Invalid Rule : " + e.getMessage());
	}

	return status;
    }

    /**
     * Utils method to configure client ip as static ip
     * 
     * @param hostName
     *            Host name of the client to be filled in respective text box
     * @param hostMacAddress
     *            Host MAC address of the client to be filled in respective text box
     * @param ipAddress
     *            IPv4 address of the client to be filled in respective text box
     * @param tapEnv
     *            AutomaticsTapApi instance
     * @param pageTitleKeyword
     *            Page title to be validated after clicking on save button
     * @param webDriver
     *            WebDriver instance
     * @return true if client ip is configured as static ip successfully
     * 
     * @Refactor Sruthi Santhosh
     */
    public static boolean configureReservedIpAddress(String hostName, String hostMacAddress, String ipAddress,
	    AutomaticsTapApi tapEnv, String pageTitleKeyword, WebDriver webDriver) {
	// Variable declaration starts
	String errorMessage = "Failed to configure Reserved IP Address";
	boolean status = false;
	// Variable declaration ends
	try {
	    LOGGER.info("Entering the hostname in host name Text Box : " + hostName);
	    LanSideBasePage.clear(By.xpath(BroadBandWebGuiElements.XPATH_RESERVED_IP_HOST_NAME));
	    LanSideBasePage.sendKeys(By.xpath(BroadBandWebGuiElements.XPATH_RESERVED_IP_HOST_NAME), hostName);

	    LOGGER.info("Entering the host MAC address in host name Text Box : " + hostMacAddress);
	    LanSideBasePage.clear(By.xpath(BroadBandWebGuiElements.XPATH_RESERVED_IP_MAC_ADDRESS));
	    LanSideBasePage.sendKeys(By.xpath(BroadBandWebGuiElements.XPATH_RESERVED_IP_MAC_ADDRESS), hostMacAddress);

	    LOGGER.info("Entering the IP in Reserved IP Address Text Box : " + ipAddress);
	    LanSideBasePage.clear(By.xpath(BroadBandWebGuiElements.XPATH_RESERVED_IP_TEXT_BOX));
	    LanSideBasePage.sendKeys(By.xpath(BroadBandWebGuiElements.XPATH_RESERVED_IP_TEXT_BOX), ipAddress);

	    LOGGER.info("Clicking on Save Button !");
	    LanSideBasePage.click(By.xpath(BroadBandWebGuiElements.XPATH_ADD_DEVICE_WITH_RESERVED_IP_SAVE_BUTTON));

	    LOGGER.info("Waiting for 30 sec after clicking on save button.");
	    tapEnv.waitTill(BroadBandTestConstants.THREE_SECOND_IN_MILLIS);
	    status = BroadBandWebUiUtils.validatePageLaunchedStatusWithPageTitle(webDriver, pageTitleKeyword);
	} catch (Exception e) {
	    errorMessage = errorMessage + "Exception occurred while configuring Reserved IP: " + e.getMessage();
	    LOGGER.error(errorMessage);
	}
	return status;
    }

    /**
     * Utility method to select Firewall in Gateway menu from GUI Home page
     * 
     * @param Dut
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @return true if navigated to Firewall page and verified title
     * @refactor Said hisham
     */
    public static boolean navigateToFirewall(WebDriver driver, Dut device) {
	LOGGER.debug("Entering method: navigateToFirewall");
	boolean result = false;
	try {
	    result = LanSideBasePage.isPageLaunched(BroadBandWebGuiTestConstant.LINK_TEXT_FIREWALL,
		    BroadbandPropertyFileHandler.getAtAGlancePageTitle());
	} catch (Exception e) {
	    LOGGER.error("Excepton Occured while navigating to Firewall Page");
	}
	LOGGER.debug("Exiting method: navigateToFirewall");
	return result;
    }

    /**
     * Utility method to retrieve Firewall Security Level from Web GUI
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * 
     * @return IPv4 Firewall Level
     * @refactor Said hisham
     */
    public static String getFirewallSecurityLevel(WebDriver driver, Dut device, String ipInterface) {
	LOGGER.debug("Entering method: getFirewallSecurityLevel");

	String firewallLevel = null;
	List<String> xpathList = null;

	try {
	    switch (ipInterface) {
	    case BroadBandTestConstants.String_CONSTANT_IPV4:
		xpathList = BroadBandWebGuiElements.XPATH_FOR_FIREWALL_SETTINGS_IPV4;
		break;
	    case BroadBandTestConstants.String_CONSTANT_IPV6:
		xpathList = BroadBandWebGuiElements.XPATH_FOR_FIREWALL_SETTINGS_IPV6;
		break;
	    }

	    driver.navigate().refresh();

	    for (String xpath : xpathList) {
		try {
		    if (driver.findElement(By.xpath(xpath)).isSelected()) {
			firewallLevel = BroadBandTestConstants.FIREWALL_SETTINGS.get(xpath);
			LOGGER.info("Default Firewall Level retrieved from GUI is : " + firewallLevel);
			break;
		    }
		} catch (NoSuchElementException e) {
		    LOGGER.info(e.getMessage());
		    continue;
		}
	    }
	} catch (Exception e) {
	    LOGGER.error("Excepton Occured while retrieving Firewall Security Level");
	}
	LOGGER.debug("Exiting method: getFirewallSecurityLevel");
	return firewallLevel;
    }

    /**
     * Utility method to navigate to 'Managed Devices' page from 'At a Glance' page
     * 
     * @param device
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @return true if navigated to Firewall page and verified title
     * @refactor Said Hisham
     */
    public static boolean navigateToManagedDevicesFromAtAGlance(WebDriver driver, Dut device) {

	LOGGER.debug("Entering method: navigateToManagedDevicesFromAtAGlance");
	boolean result = false;
	boolean isBusinessClassDevice = DeviceModeHandler.isBusinessClassDevice(device);
	try {
	    String expectedTitle = BroadbandPropertyFileHandler.getParentalControlPageTitle();

	    String linkText = isBusinessClassDevice ? BroadBandWebGuiTestConstant.LINK_TEXT_CONTENT_FILTERING
		    : BroadBandWebGuiTestConstant.LINK_TEXT_PARENTAL_CONTROL;

	    if (LanSideBasePage.isPageLaunched(linkText, expectedTitle)) {
		expectedTitle = BroadbandPropertyFileHandler.getManagedDevicesPageTitle();
		result = LanSideBasePage.isPageLaunched(BroadBandWebGuiTestConstant.LINK_TEXT_MANAGED_DEVICES,
			expectedTitle);
	    }
	} catch (Exception e) {
	    LOGGER.error("Excepton Occured while navigating to 'Managed Devices' Page from 'At a Glance' page");
	}
	LOGGER.debug("Exiting method: navigateToManagedDevicesFromAtAGlance");
	return result;
    }

    /**
     * Utility method to select Firewall in Gateway menu from GUI Home page
     * 
     * @param Dut
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @return true if navigated to Firewall page and verified title
     * @refactor Athira
     */
    public static boolean navigateToFirewallFromPartnerPage(WebDriver driver, Dut device) {
	LOGGER.debug("Entering method: navigateToFirewall");
	boolean result = false;
	try {

	    result = LanSideBasePage.isPageLaunched(BroadBandWebGuiTestConstant.LINK_TEXT_FIREWALL,
		    BroadbandPropertyFileHandler.getPageTitleForPartnerNetwork());
	    if (result)
		result = LanSideBasePage.isPageLaunched(BroadBandWebGuiTestConstant.LINK_TEXT_IPV4,
			BroadbandPropertyFileHandler.getFireWallIpv4PageTitle());
	} catch (Exception e) {
	    LOGGER.error("Excepton Occured while navigating to Firewall Page");
	}
	LOGGER.debug("Exiting method: navigateToFirewall");
	return result;
    }

    /**
     * Utility method to select Firewall in Gateway menu from GUI Home page
     * 
     * @param Dut
     *            device instance
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @return true if navigated to Firewall page and verified title
     * @refactor Athira
     */
    public static boolean navigateToFirewallIPv6(WebDriver driver, Dut device) {
	LOGGER.debug("Entering method: navigateToFirewall");
	boolean result = false;
	try {

	    result = LanSideBasePage.isPageLaunched(BroadBandWebGuiTestConstant.LINK_TEXT_FIREWALL,
		    BroadbandPropertyFileHandler.getAtAGlancePageTitle());
	    if (result)
		result = LanSideBasePage.isPageLaunched(BroadBandWebGuiTestConstant.LINK_TEXT_IPV6,
			BroadbandPropertyFileHandler.getFireWallIpv6PageTitle());
	} catch (Exception e) {
	    LOGGER.error("Excepton Occured while navigating to Firewall Page");
	}
	LOGGER.debug("Exiting method: navigateToFirewall");
	return result;
    }

    /**
     * Utility method to modify Firewall setting for IPV4 or IPV6 from Firewall Page
     * 
     * @param tapEnv
     * @param settop
     * @param driver
     * @param elementId
     * @param securityLevel
     * @param ipInterface
     * @return True,if modifying and saving the firewall configuration is successful
     * 
     * @refactor Athira
     */
    public static boolean configureFirewallSetting(AutomaticsTapApi tapEnv, Dut device, WebDriver driver,
	    String elementId, String securityLevel, String ipInterface) {

	LOGGER.debug("STARTING METHOD : configureFirewallSetting()");

	boolean status = false;

	try {
	    LOGGER.info("Selecting Firewall setting by clicking the security level!");
	    driver.findElement(By.id(elementId)).click();
	    tapEnv.waitTill(BroadBandTestConstants.THREE_SECOND_IN_MILLIS);

	    LOGGER.info("clicking the 'Save Settings' button!");
	    driver.findElement(By.id(BroadBandWebGuiElements.ELEMENT_ID_SUBMIT_FIREWALL_BUTTON)).click();

	    LOGGER.info("Waiting for 20 seconds for the changes to reflect!");
	    tapEnv.waitTill(BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS);

	    status = BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON, securityLevel,
		    BroadBandCommonPage.getFirewallSecurityLevel(driver, device, ipInterface));

	} catch (Exception e) {
	    LOGGER.error("Exception occured while configuring Firewall settings" + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD : configureFirewallSetting()");
	return status;
    }

    /**
     * This method is to select an element using visible text
     * 
     * 
     * @param visibleTextId
     *            id for the drop down element visible text
     * @param visibleText
     *            drop down element visible text
     * @param validationText
     *            Text to be validated while waiting for navigation
     * @param XpathForValidationText
     *            Xpath for the text to be validated
     * 
     * @return returns status of the operation
     * 
     * @Refactor Sruthi Santhosh
     */
    public boolean selectElementFromDropDownByVisibleText(String visibleTextId, String visibleText,
	    String validationText, String xPathForValidationText) throws TestException {
	boolean status = false;
	LOGGER.debug("STARTING METHOD: selectElementFromDropDownByVisibleText()");
	try {
	    // Select visibleTextId passed
	    selectViaVisibleText(By.id(visibleTextId), visibleText);
	    // waiting for validationText
	    waitForTextToAppear(validationText, By.xpath(xPathForValidationText));
	    LOGGER.info("Successfully verified the presence of validation text : " + validationText);
	    status = true;
	} catch (InterruptedException interruptedException) {
	    LOGGER.error("Interrupted Exception occured while selecting element : " + visibleTextId + ". Exception : "
		    + interruptedException.getMessage());
	    throw new TestException("Interrupted Exception occured while selecting element : " + visibleTextId
		    + ". Exception : " + interruptedException.getMessage());
	} catch (Exception e) {
	    throw new TestException(
		    "Exception occured while selecting element : " + visibleTextId + ". Exception : " + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD: selectElementFromDropDownByVisibleText()");
	return status;
    }

    /**
     * Method to click drop down icon and select the id based on visibility
     * 
     * @Refactor Sruthi Santhosh
     */
    public void selectViaVisibleText(By elementId, String visibleText) throws Exception {
	Select dropdown = new Select(driver.findElement(elementId));
	dropdown.selectByVisibleText(visibleText);
    }

    /**
     * Method to get the port mode enable status using "aria-checked" attribute of Enable and Disable buttons.
     * 
     * @return boolean true if portfowarding is enabled /false if portfowarding is in disabled state
     * 
     * @refactor Rakesh C N
     **/
    public static boolean getPortForwardingModeEnabledStatus(WebDriver driver, Dut device) {
	LOGGER.debug("STARTING METHOD: getPortForwardingModeEnabledStatus");
	boolean status = false;
	String areaCheckedAttributeForEnablebutton = null;
	String areaCheckedAttributeForDisablebutton = null;
	String errorMessage = null;
	try {

	    areaCheckedAttributeForEnablebutton = driver
		    .findElement(By.xpath(BroadBandWebGuiTestConstant.XPATH_FOR_PORT_FORWARDING_MODE_ENABLE_LINK))
		    .getAttribute(BroadBandWebGuiTestConstant.ARIA_CHECKED_ATTRIBUTE);

	    areaCheckedAttributeForDisablebutton = driver
		    .findElement(By.xpath((BroadBandWebGuiTestConstant.XPATH_FOR_PORT_FORWARDING_MODE_DISABLE_LINK)))
		    .getAttribute(BroadBandWebGuiTestConstant.ARIA_CHECKED_ATTRIBUTE);

	    LOGGER.debug("area checked attribute obtained for disable portfowarding Button is"
		    + areaCheckedAttributeForDisablebutton);
	    LOGGER.debug("area checked attribute obtained for enable portfowarding Button is"
		    + areaCheckedAttributeForEnablebutton);
	    if (CommonMethods.isNotNull(areaCheckedAttributeForDisablebutton)
		    && CommonMethods.isNotNull(areaCheckedAttributeForEnablebutton)) {
		if (areaCheckedAttributeForEnablebutton.equalsIgnoreCase(BroadBandTestConstants.FALSE)
			&& areaCheckedAttributeForDisablebutton.equalsIgnoreCase(BroadBandTestConstants.TRUE)) {
		    status = false;
		}
		if (areaCheckedAttributeForEnablebutton.equalsIgnoreCase(BroadBandTestConstants.TRUE)
			&& areaCheckedAttributeForDisablebutton.equalsIgnoreCase(BroadBandTestConstants.FALSE)) {
		    status = true;
		}
		LOGGER.info("portfowarding Mode enabled status as obtained from WEBGUI is " + status);
		return status;
	    } else {
		errorMessage = "Unable to retrieve the portfowarding mode enable/disable status from webgui -->attribute obtained for Enable button is: "
			+ areaCheckedAttributeForEnablebutton + "attribute obtained for Disable button is "
			+ areaCheckedAttributeForDisablebutton;
		throw new TestException(errorMessage);
	    }
	} catch (Exception exception) {
	    errorMessage = "Unable to check the portfowarding status from WEBGUI due to: " + exception.getMessage();
	    LOGGER.error(errorMessage);
	    throw new TestException(
		    "Unable to check the portfowarding status from WEBGUI due to: " + exception.getMessage());
	}
    }

    /**
     * Method to get the https port mode enable status using "aria-checked" attribute of Enable and Disable buttons.
     * 
     * @return boolean true if http is enabled /false if http is in disabled state
     * 
     * 
     **/
    public static boolean getHttpsModeEnabledStatus(WebDriver driver, Dut device) {
	LOGGER.debug("STARTING METHOD: getHttpsModeEnabledStatus");
	boolean status = false;
	String areaCheckedAttributeForEnablebutton = null;
	String areaCheckedAttributeForDisablebutton = null;
	String errorMessage = null;
	try {
	    areaCheckedAttributeForEnablebutton = driver
		    .findElement(By.xpath(BroadBandWebGuiTestConstant.XPATH_FOR_HTTPS_MODE_ENABLE_LINK))
		    .getAttribute(BroadBandWebGuiTestConstant.ARIA_CHECKED_ATTRIBUTE);
	    areaCheckedAttributeForDisablebutton = driver
		    .findElement(By.xpath((BroadBandWebGuiTestConstant.XPATH_FOR_HTTPS_MODE_DISABLE_LINK)))
		    .getAttribute(BroadBandWebGuiTestConstant.ARIA_CHECKED_ATTRIBUTE);
	    LOGGER.debug("area checked attribute obtained for disable Https Button is"
		    + areaCheckedAttributeForDisablebutton);
	    LOGGER.debug(
		    "area checked attribute obtained for enable Https Button is" + areaCheckedAttributeForEnablebutton);
	    if (CommonMethods.isNotNull(areaCheckedAttributeForDisablebutton)
		    && CommonMethods.isNotNull(areaCheckedAttributeForEnablebutton)) {
		if (areaCheckedAttributeForEnablebutton.equalsIgnoreCase(RDKBTestConstants.FALSE)
			&& areaCheckedAttributeForDisablebutton.equalsIgnoreCase(RDKBTestConstants.TRUE)) {
		    status = false;
		}
		if (areaCheckedAttributeForEnablebutton.equalsIgnoreCase(RDKBTestConstants.TRUE)
			&& areaCheckedAttributeForDisablebutton.equalsIgnoreCase(RDKBTestConstants.FALSE)) {
		    status = true;
		}
		LOGGER.info("Https Mode enabled status as obtained from WEBGUI is " + status);
		return status;
	    } else {
		errorMessage = "Unable to retrieve the Https mode enable/disable status from webgui -->attribute obtained for Enable button is: "
			+ areaCheckedAttributeForEnablebutton + "attribute obtained for Disable button is "
			+ areaCheckedAttributeForDisablebutton;
		throw new TestException(errorMessage);
	    }
	} catch (Exception exception) {
	    errorMessage = "Unable to check the Https status from WEBGUI due to: " + exception.getMessage();
	    LOGGER.error(errorMessage);
	    throw new TestException("Unable to check the Https status from WEBGUI due to: " + exception.getMessage());
	}
    }

    /**
     * Utils method to modify Client DHCP Configuration
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param webDriver
     *            webDriver object
     * @param setToReservedIp
     *            boolean to switch between DHCP and Reserved IP
     * @param clientMacAddress
     *            Wi-Fi/Ethernet Client MAC Address
     * @param reservedIpAddress
     *            IPv4 address in DHCP Range
     * @return true if the DHCP configuration is successfully modified
     * @refctor Rakesh C N
     */
    public static boolean modifyClientDhcpConfiguration(AutomaticsTapApi tapEnv, WebDriver webDriver,
	    boolean setToReservedIp, String clientMacAddress, String reservedIpAddress) {

	LOGGER.debug("STARTING METHOD :modifyClientDhcpConfiguration");

	boolean status = false;
	String hostNameXpath = null;
	String clientInfoXpath = null;
	String editButtonXpath = null;
	int iterationCount = 1;
	String reservedIpRadioXpath = null;
	String reserverdIpTextBoxXpath = null;
	String dhcpRadioXpath = null;
	String saveButtonXpath = null;
	String editPageHeaderXpath = null;
	String configurationXpath = null;

	BroadBandCommonPage connectionPage = new BroadBandCommonPage(webDriver);

	do {

	    try {
		iterationCount++;

		hostNameXpath = BroadBandWebGuiElements.ELEMENT_XPATH_ONLINE_DEVICES_HOST_NAME.replaceAll(
			BroadBandTestConstants.PLACE_HOLDER_FOR_ROW_NUMBER, Integer.toString(iterationCount));
		clientInfoXpath = BroadBandWebGuiElements.ELEMENT_XPATH_ONLINE_DEVICES_INFO.replaceAll(
			BroadBandTestConstants.PLACE_HOLDER_FOR_ROW_NUMBER, Integer.toString(iterationCount));
		editButtonXpath = BroadBandWebGuiElements.XPATH_ONLINE_DEVICES_CELL_ELEMENT
			.replaceAll(BroadBandTestConstants.PLACE_HOLDER_FOR_DIV_1_NUMBER,
				Integer.toString(iterationCount))
			.replaceAll(BroadBandTestConstants.PLACE_HOLDER_FOR_DIV_2_NUMBER,
				BroadBandTestConstants.STRING_VALUE_FIVE);
		configurationXpath = BroadBandWebGuiElements.XPATH_ONLINE_DEVICES_CELL_ELEMENT_PATH
			.replaceAll(BroadBandTestConstants.PLACE_HOLDER_FOR_DIV_1_NUMBER,
				Integer.toString(iterationCount))
			.replaceAll(BroadBandTestConstants.PLACE_HOLDER_FOR_DIV_2_NUMBER,
				BroadBandTestConstants.STRING_VALUE_TWO);
		editPageHeaderXpath = BroadBandWebGuiElements.XPATH_ONLINE_DEVICES_EDIT_PAGE_HEADER.replaceAll(
			BroadBandTestConstants.PLACE_HOLDER_FOR_ROW_NUMBER, Integer.toString(iterationCount + 2));
		dhcpRadioXpath = BroadBandWebGuiElements.XPATH_ONLINE_DEVICES_EDIT_PAGE_ELEMENT
			.replaceAll(BroadBandTestConstants.PLACE_HOLDER_FOR_ROW_NUMBER,
				Integer.toString(iterationCount + 2))
			.replaceAll(BroadBandTestConstants.PLACE_HOLDER_FOR_DIV_1_NUMBER,
				BroadBandTestConstants.STRING_VALUE_TWO)
			.replaceAll(BroadBandTestConstants.PLACE_HOLDER_FOR_DIV_2_NUMBER,
				BroadBandTestConstants.STRING_VALUE_THREE);
		reservedIpRadioXpath = BroadBandWebGuiElements.XPATH_ONLINE_DEVICES_EDIT_PAGE_RESERVED_IP_RADIO_BUTTON
			.replaceAll(BroadBandTestConstants.PLACE_HOLDER_FOR_ROW_NUMBER,
				Integer.toString(iterationCount + 2));
		reserverdIpTextBoxXpath = BroadBandWebGuiElements.XPATH_ONLINE_DEVICES_EDIT_PAGE_ELEMENT
			.replaceAll(BroadBandTestConstants.PLACE_HOLDER_FOR_ROW_NUMBER,
				Integer.toString(iterationCount + 2))
			.replaceAll(BroadBandTestConstants.PLACE_HOLDER_FOR_DIV_1_NUMBER,
				BroadBandTestConstants.STRING_VALUE_TWO)
			.replaceAll(BroadBandTestConstants.PLACE_HOLDER_FOR_DIV_2_NUMBER,
				BroadBandTestConstants.STRING_VALUE_FIVE);
		saveButtonXpath = BroadBandWebGuiElements.XPATH_ONLINE_DEVICES_EDIT_PAGE_ELEMENT
			.replaceAll(BroadBandTestConstants.PLACE_HOLDER_FOR_ROW_NUMBER,
				Integer.toString(iterationCount + 2))
			.replaceAll(BroadBandTestConstants.PLACE_HOLDER_FOR_DIV_1_NUMBER,
				BroadBandTestConstants.STRING_VALUE_TWO)
			.replaceAll(BroadBandTestConstants.PLACE_HOLDER_FOR_DIV_2_NUMBER,
				BroadBandTestConstants.STRING_VALUE_SEVEN);
		try {
		    connectionPage.click(By.xpath(hostNameXpath));
		} catch (NoSuchElementException e) {
		    if (e.getMessage().contains(hostNameXpath)) {
			LOGGER.error(
				"BREAKING EXECUTION, AS NO FURTHER ROWS ARE AVAILABLE IN THE 'ONLINE DEVICES - PRIVATE NETWORK TABLE'.");
		    }
		    break;
		}

		LOGGER.info("IDENTIFYING THE REQUIRED CLIENT (" + clientMacAddress + ") IN ONLINE DEVICES TABLE!");
		String clientInfo = LanSideBasePage.getText(By.xpath(clientInfoXpath)).toLowerCase();
		String macAddressFromGui = CommonMethods
			.patternFinder(clientInfo, BroadBandTestConstants.PATTERN_TO_GET_MAC_ADDRESS)
			.replace(AutomaticsConstants.COLON, BroadBandTestConstants.EMPTY_STRING);
		LOGGER.info("MAC ADDRESS LISTED IN ONLINE CONNECTED DEVICES TABLE IN ROW #" + (iterationCount - 1)
			+ " : " + macAddressFromGui.toUpperCase());

		if (CommonMethods.isNotNull(clientInfo)
			&& BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
				clientMacAddress, macAddressFromGui.toUpperCase())) {

		    connectionPage.click(By.xpath(hostNameXpath));

		    LOGGER.info("CLIENT FOUND! CLICKING ON EDIT BUTTON!");
		    connectionPage.click(By.xpath(editButtonXpath));
		    tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);

		    LOGGER.info("VERIFYING PAGE TITLE!");
		    if (BroadBandCommonPage.verifyTextinPage(webDriver.findElement(By.xpath(editPageHeaderXpath)),
			    BroadBandWebGuiTestConstant.HEADER_TITLE_CONNECTED_DEVICES_EDIT_PAGE)) {

			LOGGER.info("PAGE TITLE VERIFIED SUCCESSFULLY !");

			if (setToReservedIp) {
			    LOGGER.info("CHANGING CONFIGURATION! CLICKING RESERVED IP RADIO BUTTON!");
			    connectionPage.click(By.xpath(reservedIpRadioXpath));

			    LOGGER.info("ENTERING RESERVED IP AS : " + reservedIpAddress);
			    connectionPage.clear(By.xpath(reserverdIpTextBoxXpath));
			    connectionPage.sendKeys(By.xpath(reserverdIpTextBoxXpath), reservedIpAddress);
			} else {
			    LOGGER.info("CHANGING CONFIGURATION! CLICKING DHCP RADIO BUTTON!");
			    connectionPage.click(By.xpath(dhcpRadioXpath));
			}

			LOGGER.info("CLICKING SAVE BUTTON BUTTON!");
			connectionPage.click(By.xpath(saveButtonXpath));
			tapEnv.waitTill(BroadBandTestConstants.FIFTY_SECONDS_IN_MILLIS);

			connectionPage.refresh();

			LOGGER.info("VERIFYING THE OPERATION!");
			status = BroadBandCommonPage.verifyTextinPage(
				webDriver.findElement(By.xpath(configurationXpath)),
				setToReservedIp ? BroadBandTestConstants.RESERVED_IP
					: BroadBandTestConstants.STRING_DHCP);
			break;
		    } else {
			LOGGER.error("UNABLE TO LAUNCH THE EDIT PAGE DEVICE");
			break;
		    }
		} else {
		    connectionPage.click(By.xpath(hostNameXpath));
		}

	    } catch (Exception ex) {
		LOGGER.error(
			"EXCEPTION OCCURED WHILE RETRIEVING CONNECTED CLIENTS DETAILS FROM GUI : " + ex.getMessage());
	    }
	} while (iterationCount <= BroadBandTestConstants.CONSTANT_10);

	LOGGER.debug("ENDING METHOD :modifyClientDhcpConfiguration");
	return status;
    }

    /**
     * Method to verify text contains in UI page.
     * 
     * @param webDriver
     * @param WebElement
     * @param TexttoVerify
     * @author Prince ArunRaj
     * @refactor Rakesh C N
     */
    public static boolean verifyTextinPage(WebElement webelement, String TexttoVerify) {
	LOGGER.debug("STARTING METHOD :verifyTextinPage");
	boolean status = false;
	if (webelement != null) {
	    String content = webelement.getText();
	    LOGGER.info("CONTENT:" + content);
	    LOGGER.info("TEXT TO VERIFY : " + TexttoVerify);
	    status = CommonMethods.isNotNull(content)
		    && CommonUtils.isGivenStringAvailableInCommandOutput(content, TexttoVerify);
	    if (!status) {
		LOGGER.info("TEXT TO VERIFY : " + TexttoVerify + ": IS NOT FOUND IN : " + content);
	    }
	} else {
	    LOGGER.info("WebElement is not found : null");
	}
	LOGGER.debug("ENDING METHOD :verifyTextinPage");
	return status;
    }
}

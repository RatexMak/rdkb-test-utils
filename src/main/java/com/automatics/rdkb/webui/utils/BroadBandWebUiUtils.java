/* Copyright 2021 Comcast Cable Communications Management, LLC
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
package com.automatics.rdkb.webui.utils;

import java.awt.image.BufferedImage;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.assertthat.selenium_shutterbug.core.PageSnapshot;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;
import com.automatics.device.Device;
import com.automatics.device.Dut;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiElements;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiTestConstant;
import com.automatics.rdkb.webui.page.BroadBandCommonPage;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpMib;
import com.automatics.rdkb.utils.snmp.BroadBandSnmpUtils;
import com.automatics.rdkb.webui.page.BroadbandLocalIpConfigurationPage;
import com.automatics.utils.CommonMethods;
import com.automatics.rdkb.utils.CommonUtils;
import com.automatics.rdkb.utils.DeviceModeHandler;

public class BroadBandWebUiUtils {

	/**
	 * Logger instance for {@link BroadBandWebUiUtils} class.
	 */
	public static Logger LOGGER = LoggerFactory.getLogger(BroadBandWebUiUtils.class);

	/**
	 * Save Web page Images in jenkins and Update the execution status based on
	 * current test steps for RDKB webgui tests.
	 * 
	 * @param settop         The settop to be used.
	 * @param testId         The manual test ID
	 * @param testStepNumber The manual test step number.
	 * @param status         The execution status.
	 * @param errorMessage   The error message.
	 * @param blockExecution The flag to throw an exception to block further
	 *                       execution of test cases.
	 * @refactor Govardhan
	 */
	public static void updateExecutionStatusForWebGuiStep(WebDriver driver, AutomaticsTapApi tapEnv, Dut device,
			String testId, String testStepNumber, boolean status, String errorMessage, boolean blockExecution) {

		BufferedImage img = BroadBandWebUiUtils.captureCurrentScreenFromDriver(driver);
		if (null != img) {
			String imageName = System.currentTimeMillis() + "_" + "UI_after_" + testId + "_" + testStepNumber;
			AutomaticsTapApi.saveImages(device, img, imageName);
		}
		tapEnv.updateExecutionStatus(device, testId, testStepNumber, status, errorMessage, blockExecution);
	}

	/**
	 * Utility to capture the current visible screen disabled on screen.
	 * 
	 * @param webDriver The web driver.
	 * @return Buffered image.
	 * @refactor Govardhan
	 */
	public static BufferedImage captureCurrentScreenFromDriver(final WebDriver webDriver) {
		final BufferedImage[] bufferredImages = new BufferedImage[1];
		final long threadTimeout = 15000L;
		/*
		 * ImageIO is blocking call, so added Thread to break blocking call after 15
		 * seconds if it is blocked.
		 */
		Thread readerThread = new Thread() {

			public void run() {
				PageSnapshot pageSnapshot = null;
				try {
					pageSnapshot = Shutterbug.shootPage(webDriver, ScrollStrategy.BOTH_DIRECTIONS, 500, true);
					bufferredImages[0] = pageSnapshot.getImage();
				} catch (Exception e1) {
					LOGGER.error("Failed to capture the broad band Web UI..!!!! ", e1);
				} finally {
					pageSnapshot = null;
				}
			}
		};
		try {
			readerThread.start();
			readerThread.join(threadTimeout);
			readerThread.interrupt();
		} catch (InterruptedException iexc) {
			LOGGER.info("Reader thread got interrupted after 15  seconds");
		}
		return bufferredImages[0];
	}

	/**
	 * Helper method to delete existing content from text box
	 * 
	 * @param cmnDriver  Web driver
	 * @param locationId locatorId for the web element
	 * @author Govardhan
	 */
	public static void deleteExistingContentFromTextBox(WebDriver cmnDriver, String locaionId) {
		WebElement toClear = cmnDriver.findElement(By.name(locaionId));
		toClear.sendKeys(Keys.CONTROL + "a");
		toClear.sendKeys(Keys.DELETE);
	}

	/**
	 * Utility method to validate the page title against current page title
	 * 
	 * @param driver, webdriver instance
	 * @param string, Page title
	 * @return true, if the string value is same as current page title
	 * @author Praveenkumar Paneerselvam
	 * @refactor Said Hisham
	 */
	public static boolean validatePageLaunchedStatusWithPageTitle(WebDriver driver, String string) {
		LOGGER.debug("STARTING METHOD: validatePageLaunchedStatusWithPageTitle() ");
		// Variable to store page navigation status
		boolean status = false;
		AutomaticsTapApi tapEnv = AutomaticsTapApi.getInstance();
		// Variable to store software page title
		String pageTitle = null;
		long startTimeStamp = System.currentTimeMillis();
		do {
			pageTitle = driver.getTitle();
			LOGGER.info("Current Page title - " + pageTitle);
			LOGGER.info("Parameter string - " + string);
			if (null != (pageTitle)) {
				status = pageTitle.toLowerCase().contains(string.toLowerCase());
				LOGGER.info("Successfully verified page navigation status" + status);
			}
		} while (!status && (System.currentTimeMillis() - startTimeStamp) < BroadBandTestConstants.TWO_MINUTE_IN_MILLIS
				&& BroadBandCommonUtils.hasWaitForDuration(tapEnv, BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS));
		LOGGER.info("Current URL: " + driver.getCurrentUrl());
		LOGGER.debug("ENDING METHOD: validatePageLaunchedStatusWithPageTitle() ");
		return status;
	}

	/**
	 * @param xpath xpath for the element
	 * @return return true if element not present,else false
	 * @author Deepa Bada
	 * @refactor Athira
	 */
	public static boolean verifySaveOrRestoreConfigurationsButtonExists(String xpath, WebDriver driver) {
		boolean status = false;
		BroadBandCommonPage commonPage = new BroadBandCommonPage(driver);
		status = !(commonPage.isElementByXpathAvailable(xpath));
		LOGGER.info(" Is SAVE CURRENT CONFIGURATION button exists ?:"
				+ (status ? "Button does not exist" : "Button exists"));
		return status;
	}

	/**
	 * Helper method to click element by id
	 * 
	 * @param webDriver webDriver
	 * @param elementId String containing element id
	 * 
	 * @return true if click is success
	 * @refactor Rakesh C N
	 */
	public static boolean clickById(WebDriver webDriver, String elementId) {
		LOGGER.debug("Entering method: clickById");
		boolean result = false;
		try {
			webDriver.findElement(By.id(elementId)).click();
			result = true;
		} catch (Exception e) {
			LOGGER.error("Exception occured while clicking element with id '" + elementId + "' : " + e.getMessage());
		}
		LOGGER.debug("Exiting method: clickById");
		return result;
	}

	/**
	 * This method will to select stateless autoconfig in local Ip configuration
	 * page
	 * 
	 * @param TapEnv            instance of {@link AutomaticsTapApi}
	 * 
	 * @param webDriver         {@link WebDriver}
	 * 
	 * @param toSelectStateless To select or unselect SLAAC mode
	 * @return status of SLAAC Mode
	 * @refactor Said hisham
	 */

	public static boolean selectSLAACMode(AutomaticsTapApi tapEnv, WebDriver webDriver, boolean toSelectStatelessMode) {
		boolean status = false;
		try {
			if (toSelectStatelessMode) {
				if (webDriver.findElement(By.id(BroadBandWebGuiElements.ID_CHECKBOX_STATEFUL)).isSelected()) {
					webDriver.findElement(By.id(BroadBandWebGuiElements.ID_CHECKBOX_STATEFUL)).click();
					webDriver.findElement(By.xpath(BroadBandWebGuiElements.XPATH_CHECKBOX_STATELESS)).click();

					tapEnv.waitTill(BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS);
					LOGGER.info("Stateless Auto config mode is clicked");
					status = !webDriver.findElement(By.id(BroadBandWebGuiElements.ID_CHECKBOX_STATEFUL)).isSelected();
				} else {
					LOGGER.info("Already is in Stateless config mode");
					status = true;
				}
			} else if (!toSelectStatelessMode) {
				tapEnv.waitTill(BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS);
				if (!webDriver.findElement(By.id(BroadBandWebGuiElements.ID_CHECKBOX_STATEFUL)).isSelected()) {
					webDriver.findElement(By.id(BroadBandWebGuiElements.ID_CHECKBOX_STATEFUL)).click();
					webDriver.findElement(By.xpath(BroadBandWebGuiElements.XPATH_CHECKBOX_STATELESS)).click();

					tapEnv.waitTill(BroadBandTestConstants.TWENTY_SECOND_IN_MILLIS);
					LOGGER.info("Stateful config mode is clicked");
					status = webDriver.findElement(By.id(BroadBandWebGuiElements.ID_CHECKBOX_STATEFUL)).isSelected();
				} else {
					LOGGER.info("Already is in Stateful config mode");
					status = true;
				}
			}
		} catch (Exception e) {
			LOGGER.error(
					" Exception occured while selecting SLAAC mode in Local Ip configuration page" + e.getMessage());
		}
		return status;
	}

	/**
	 * Method to click typical firewall settings
	 * 
	 * @param driver, webdriver instance
	 * 
	 * @return True, if typical firewall settings is enabled
	 * 
	 * @refactor Athira
	 */
	public static boolean isTypicalFirewallSelected(WebDriver driver, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: isTypicalFirewallSelected");
		boolean result = false;
		try {
			driver.findElement(By.xpath(BroadBandWebGuiTestConstant.XPATH_FOR_TYPICAL_SECURITY)).click();
			tapEnv.waitTill(BroadBandTestConstants.FIVE_SECONDS_IN_MILLIS);
			driver.findElement(By.xpath(BroadBandWebGuiTestConstant.XPATH_FOR_SAVE_SETTINGS)).click();
			tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
			result = true;
		} catch (NoSuchElementException noSuchElementException) {
			// Log & Suppress the Exception
			LOGGER.info("Element is not present in UI");
		}
		LOGGER.debug("ENDING METHOD: isTypicalFirewallSelected");
		return result;
	}

	/**
	 * Method to verify block ICMP check box is enabled or not
	 * 
	 * @param driver, webdriver instance
	 * @param value,  boolean value as true to enable and false to disable.
	 * 
	 * @return True, if enabled/disabled according to param passed
	 * 
	 * @refactor Athira
	 */

	public static boolean isBlockIcmpCheckBoxEnabled(WebDriver driver, boolean toEnable, AutomaticsTapApi tapEnv) {
		LOGGER.debug("STARTING METHOD: isBlockIcmpCheckBoxEnabled");
		boolean result = false;
		WebElement element = null;
		try {
			driver.findElement(By.xpath(BroadBandWebGuiTestConstant.XPATH_FOR_CUSTOM_SECURITY)).click();
			tapEnv.waitTill(BroadBandTestConstants.FIVE_SECONDS_IN_MILLIS);
			element = driver.findElement(By.xpath(BroadBandWebGuiTestConstant.XPATH_FOR_BLOCK_ICMP));
			if (element.isSelected() && toEnable) {
				LOGGER.info("block ICMP is already selected ");
				result = true;
			} else if (!element.isSelected() && toEnable) {
				element.click();
				tapEnv.waitTill(BroadBandTestConstants.FIVE_SECONDS_IN_MILLIS);
				driver.findElement(By.xpath(BroadBandWebGuiTestConstant.XPATH_FOR_SAVE_SETTINGS)).click();
				tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
				result = true;
			} else if (!toEnable && element.isSelected()) {
				element.click();
				tapEnv.waitTill(BroadBandTestConstants.FIVE_SECONDS_IN_MILLIS);
				driver.findElement(By.xpath(BroadBandWebGuiTestConstant.XPATH_FOR_SAVE_SETTINGS)).click();
				tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);
				result = true;
			} else if (!toEnable && !element.isSelected()) {
				LOGGER.info("block ICMP is not selected already ");
				result = true;
			}
		} catch (NoSuchElementException noSuchElementException) {
			// Log & Suppress the Exception
			LOGGER.info("Element is not present in UI");
		}
		LOGGER.debug("ENDING METHOD: isBlockIcmpCheckBoxEnabled");
		return result;
	}

	/**
	 * Method used to validate subnet mask address from local IP network page
	 * matches with the corresponding SNMP value retrieved
	 * 
	 * @param driver instance of {@link Webdriver}
	 * @param TapEnv instance of {@link AutomaticsTapApi}
	 * @param device instance of {@link Dut}
	 * @refactor Athira
	 **/
	public static boolean verifySubnetMaskAddressFromLocalIpNetworkPageAndSnmp(WebDriver driver,
			AutomaticsTapApi tapEnv, Dut device, boolean isBusinessClassDevice) {
		LOGGER.debug("STARTING METHOD : verifySubnetMaskAddressFromLocalIpNetworkPageAndSnmp()");
		boolean status = false;
		String subnetMaskAddressFromUI = null;
		String subnetMaskAddressFromSnmp = null;
		try {
			if (isBusinessClassDevice) {
				subnetMaskAddressFromUI = BroadbandLocalIpConfigurationPage.retrieveValuesInSeriesFromTextBoxUsingId(
						driver, BroadBandWebGuiTestConstant.XPATH_IPV4_SUBNET_MASK, BroadBandTestConstants.CONSTANT_1,
						BroadBandTestConstants.CONSTANT_4, BroadBandTestConstants.DOT_OPERATOR);
			} else {
				// drop down box in residential type devices.
				Select select = new Select(driver.findElement(By.id(BroadBandWebGuiElements.SUBNET_MASK_DROPDOWN_ID)));
				subnetMaskAddressFromUI = select.getFirstSelectedOption().getText();
			}
			subnetMaskAddressFromSnmp = BroadBandSnmpUtils.executeSnmpGetWithTableIndexOnRdkDevices(tapEnv, device,
					BroadBandSnmpMib.ECM_SUBNET_MASK.getOid(), BroadBandSnmpMib.ECM_SUBNET_MASK.getTableIndex());
			LOGGER.info("SUBNET MASK VALUE RETRIEVED FROM UI IS: " + subnetMaskAddressFromUI);
			LOGGER.info("SUBNET MASK VALUE RETRIEVED VIA SNMP IS: " + subnetMaskAddressFromSnmp);
			status = CommonMethods.isNotNull(subnetMaskAddressFromUI)
					&& CommonMethods.isIpv4Address(subnetMaskAddressFromUI)
					&& CommonMethods.isNotNull(subnetMaskAddressFromSnmp)
					&& CommonMethods.isIpv4Address(subnetMaskAddressFromSnmp)
					&& CommonUtils.patternSearchFromTargetString(subnetMaskAddressFromUI, subnetMaskAddressFromSnmp);
		} catch (Exception e) {
			LOGGER.error(
					"EXCEPTION OCCURED WHILE RETRIEVING SUBNET MASK ADDRESS FROM LOCAL IP NETWORK PAGE IN LAN GUI. "
							+ e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : verifySubnetMaskAddressFromLocalIpNetworkPageAndSnmp()");
		return status;
	}

	/**
	 * Method used to validate DHCP Beginning Address from local IP network page
	 * matches with the corresponding SNMP value retrieved
	 * 
	 * @param driver instance of {@link Webdriver}
	 * @param TapEnv instance of {@link AutomaticsTapApi}
	 * @param device instance of {@link Dut}
	 * 
	 * @refactor Athira
	 **/
	public static boolean verifyDhcpBeginningAddressFromLocalIpNetworkPageAndSnmp(WebDriver driver,
			AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("STARTING METHOD : verifyDhcpBeginningAddressFromLocalIpNetworkPageAndSnmp()");
		boolean status = false;
		String dhcpBeginningAddressFromUI = null;
		String dhcpBeginningAddressFromSnmp = null;
		try {
			dhcpBeginningAddressFromUI = BroadbandLocalIpConfigurationPage.retrieveValuesInSeriesFromTextBoxUsingId(
					driver, BroadBandWebGuiElements.IPV4_DHCP_BEGIN_ADDRESS_ID, BroadBandTestConstants.CONSTANT_1,
					BroadBandTestConstants.CONSTANT_4, BroadBandTestConstants.DOT_OPERATOR);
			dhcpBeginningAddressFromSnmp = BroadBandSnmpUtils.executeSnmpGetWithTableIndexOnRdkDevices(tapEnv, device,
					BroadBandSnmpMib.ECM_DHCP_START_IP.getOid(), BroadBandSnmpMib.ECM_DHCP_START_IP.getTableIndex());
			LOGGER.info("DHCP BEGINNING ADDRESS VALUE RETRIEVED FROM UI IS: " + dhcpBeginningAddressFromUI);
			LOGGER.info("DHCP BEGINNING ADDRESS VALUE RETRIEVED VIA SNMP IS: " + dhcpBeginningAddressFromSnmp);
			status = CommonMethods.isNotNull(dhcpBeginningAddressFromUI)
					&& CommonMethods.isIpv4Address(dhcpBeginningAddressFromUI)
					&& CommonMethods.isNotNull(dhcpBeginningAddressFromSnmp)
					&& CommonMethods.isIpv4Address(dhcpBeginningAddressFromSnmp) && CommonUtils
							.patternSearchFromTargetString(dhcpBeginningAddressFromUI, dhcpBeginningAddressFromSnmp);
		} catch (Exception e) {
			LOGGER.error(
					"EXCEPTION OCCURED WHILE RETRIEVING DHCP BEGINNING ADDRESS VALUE FROM LOCAL IP NETWORK PAGE IN LAN GUI. "
							+ e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : verifyDhcpBeginningAddressFromLocalIpNetworkPageAndSnmp()");
		return status;
	}

	/**
	 * Method used to validate DHCP Ending Address from local IP network page
	 * matches with the corresponding SNMP value retrieved
	 * 
	 * @param driver instance of {@link Webdriver}
	 * @param TapEnv instance of {@link AutomaticsTapApi}
	 * @param device instance of {@link Dut}
	 * 
	 * @refactor Athira
	 **/
	public static boolean verifyDhcpEndingAddressFromLocalIpNetworkPageAndSnmp(WebDriver driver,
			AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("STARTING METHOD : verifyDhcpEndingAddressFromLocalIpNetworkPageAndSnmp()");
		boolean status = false;
		String dhcpEndingAddressFromUI = null;
		String dhcpEndingAddressFromSnmp = null;
		try {
			dhcpEndingAddressFromUI = BroadbandLocalIpConfigurationPage.retrieveValuesInSeriesFromTextBoxUsingId(driver,
					BroadBandWebGuiElements.IPV4_DHCP_END_ADDRESS_ID, BroadBandTestConstants.CONSTANT_1,
					BroadBandTestConstants.CONSTANT_4, BroadBandTestConstants.DOT_OPERATOR);
			dhcpEndingAddressFromSnmp = BroadBandSnmpUtils.executeSnmpGetWithTableIndexOnRdkDevices(tapEnv, device,
					BroadBandSnmpMib.ECM_DHCP_END_IP.getOid(), BroadBandSnmpMib.ECM_DHCP_END_IP.getTableIndex());
			LOGGER.info("DHCP ENDING ADDRESS VALUE RETRIEVED FROM UI IS: " + dhcpEndingAddressFromUI);
			LOGGER.info("DHCP ENDING ADDRESS VALUE RETRIEVED VIA WEBPA IS: " + dhcpEndingAddressFromSnmp);
			status = CommonMethods.isNotNull(dhcpEndingAddressFromUI)
					&& CommonMethods.isIpv4Address(dhcpEndingAddressFromUI)
					&& CommonMethods.isNotNull(dhcpEndingAddressFromSnmp)
					&& CommonMethods.isIpv4Address(dhcpEndingAddressFromSnmp)
					&& CommonUtils.patternSearchFromTargetString(dhcpEndingAddressFromUI, dhcpEndingAddressFromSnmp);
		} catch (Exception e) {
			LOGGER.error(
					"EXCEPTION OCCURED WHILE RETRIEVING DHCP ENDING ADDRESS VALUE FROM LOCAL IP NETWORK PAGE IN LAN GUI. "
							+ e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : verifyDhcpEndingAddressFromLocalIpNetworkPageAndSnmp()");
		return status;
	}

	/**
	 * Method used to validate DHCP Lease Time from local IP network page matches
	 * with the corresponding SNMP value retrieved
	 * 
	 * @param driver instance of {@link Webdriver}
	 * @param TapEnv instance of {@link AutomaticsTapApi}
	 * @param device instance of {@link Dut}
	 * 
	 * @refactor Athira
	 **/
	public static boolean verifyDhcpLeaseTimeFromLocalIpNetworkPageAndSnmp(WebDriver driver, AutomaticsTapApi tapEnv,
			Dut device) {
		LOGGER.debug("STARTING METHOD : verifyDhcpLeaseTimeFromLocalIpNetworkPageAndSnmp()");
		boolean status = false;
		String dhcpLeaseExpireTime = null;
		String dhcpLeaseTime = null;
		int dhcpLeaseSecondsFromWebPa = 0;
		int dhcpLeaseSeconds = 0;
		try {
			dhcpLeaseExpireTime = driver.findElement(By.id(BroadBandWebGuiElements.ELEMENT_ID_DHCP_LEASE_TIME_AMOUNT))
					.getAttribute(BroadBandWebGuiElements.ELEMENT_GET_ATTRIBUTE_VALUE);
			LOGGER.info("THE DHCPv4 LEASE TIME FROM LOCAL IP NETWORK WEBGUI PAGE IS : " + dhcpLeaseExpireTime);
			String dhcpLeaseExpireMeasure = driver
					.findElement(By.id(BroadBandWebGuiElements.ELEMENT_ID_DHCP_LEASE_TIME_MEASURE))
					.getAttribute(BroadBandWebGuiElements.ELEMENT_GET_ATTRIBUTE_VALUE);
			LOGGER.info("THE DHCPv4 LEASE MEASUREMENT FROM WEBGUI PAGE IS : " + dhcpLeaseExpireMeasure);
			dhcpLeaseTime = BroadBandSnmpUtils.executeSnmpGetWithTableIndexOnRdkDevices(tapEnv, device,
					BroadBandSnmpMib.ECM_DHCP_LEASE_TIME.getOid(),
					BroadBandSnmpMib.ECM_DHCP_LEASE_TIME.getTableIndex());
			if (CommonMethods.isNotNull(dhcpLeaseTime) && CommonMethods.isNotNull(dhcpLeaseExpireMeasure)
					&& CommonMethods.isNotNull(dhcpLeaseExpireTime)) {
				try {
					dhcpLeaseSecondsFromWebPa = Integer.parseInt(dhcpLeaseTime);
					dhcpLeaseSeconds = BroadBandCommonUtils.convertTimeToSeconds(Integer.parseInt(dhcpLeaseExpireTime),
							dhcpLeaseExpireMeasure);
					status = (dhcpLeaseSeconds == dhcpLeaseSecondsFromWebPa);
				} catch (NumberFormatException e) {
					LOGGER.error(
							"NUMBER FORMAT EXCEPTION WHILE CONVERING DHCPv4 LEASE TIME FROM LOCAL IP NETWORK PAGE");
				}
			}
		} catch (Exception e) {
			LOGGER.error("EXCEPTION OCCURED WHILE RETRIEVING DHCPv4 LEASE TIME FROM LOCAL IP NETWORK PAGE IN LAN GUI. "
					+ e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : verifyDhcpLeaseTimeFromLocalIpNetworkPageAndSnmp()");
		return status;
	}

	/**
	 * Method to verify preferred private check box button is enabled or not
	 * 
	 * @param driver, webdriver instance
	 * @param value,  boolean value as true to enable and false to disable.
	 * 
	 * @return True, if enabled/disabled according to param passed
	 * @refactor Rakesh C N
	 */

	public static boolean isPreferredPrivateCheckBoxEnabled(WebDriver driver, boolean toEnable) {
		LOGGER.debug("STARTING METHOD: verifyPreferredPrivateCheckBoxEnabledOrDisabled");
		boolean result = false;
		WebElement element = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions
					.presenceOfElementLocated((By.xpath(BroadBandWebGuiTestConstant.XPATH_FOR_PREFER_PRIVATE))));
			element = driver.findElement(By.xpath(BroadBandWebGuiTestConstant.XPATH_FOR_PREFER_PRIVATE));

			if (element.isSelected() && toEnable) {
				LOGGER.info("prefer private wifi is already enabled ");
				result = true;
			} else if (!element.isSelected() && toEnable) {
				wait.until(ExpectedConditions
						.presenceOfElementLocated((By.xpath(BroadBandWebGuiTestConstant.XPATH_FOR_PREFER_PRIVATE))));
				driver.findElement(By.xpath(BroadBandWebGuiTestConstant.XPATH_FOR_PREFER_PRIVATE)).click();
				LOGGER.info("clicking prefer private wifi to enable it");
				result = true;
			}

			else if (!element.isSelected() && !toEnable) {
				LOGGER.info("prefer Private wifi is in already  disabled state");
				result = true;
			}

			else if (element.isSelected() && !toEnable) {
				wait.until(ExpectedConditions
						.presenceOfElementLocated((By.xpath(BroadBandWebGuiTestConstant.XPATH_FOR_PREFER_PRIVATE))));
				driver.findElement(By.xpath(BroadBandWebGuiTestConstant.XPATH_FOR_PREFER_PRIVATE)).click();
				LOGGER.info("clicking prefer private wifi to disable it");
				result = true;
			}

		} catch (NoSuchElementException noSuchElementException) {
			// Log & Suppress the Exception
			LOGGER.info("Element is not present in UI");

		}
		LOGGER.debug("ENDING METHOD: verifyPreferredPrivateCheckBoxEnabledOrDisabled");
		return result;
	}

	/**
	 * This method will verify the IPv6 addresses in Admin UI network page
	 * 
	 * @param xpath       xpath of element
	 * @param elementName name of element
	 * @param webDriver   {@link WebDriver}
	 * @return return true if retrieved ip is ipv6 address
	 * 
	 * @refactor Athira
	 */

	public static boolean verifyIpV6AddressElementInPartnerNetworkPage(String xpath, String elementName,
			WebDriver webDriver) {
		LOGGER.debug("STARTING METHOD : verifyIpV6AddressElementInPartnerNetworkPage()");
		boolean status = false;
		try {
			String elementRetrieved = webDriver.findElement(By.xpath(xpath)).getText();

			LOGGER.info(elementName + " retrieved from Web GUI : " + elementRetrieved);
			status = CommonMethods.isNotNull(elementRetrieved) && CommonMethods.isIpv6Address(elementRetrieved);
		} catch (Exception e) {
			LOGGER.error(" Exception occured while getting " + elementName + " from Partner Admin UI Network page"
					+ e.getMessage());
		}
		LOGGER.debug("ENDING METHOD : verifyIpV6AddressElementInPartnerNetworkPage()");
		return status;
	}

	/**
	 * 
	 * This method fetched the appropriate IP for RDKB devices to launch login page
	 * 
	 * @param device Dut Object
	 * @param tapEnv Tap env
	 * @return Returns the appropriate IP address.For all RDKB devices except fibre
	 *         devices, ECMIP will be returned. For fibre device, Erouter IP address
	 *         will be returned
	 * @Refactor Sruthi Santhosh
	 */
	public static String getIPAddressForloginPage(Dut device, AutomaticsTapApi tapEnv) {
		String ipAddressForloginPage = null;
		if (DeviceModeHandler.isFibreDevice(device)) {
			ipAddressForloginPage = ((Device) device).getErouterIpAddress();
		} else {
			ipAddressForloginPage = ((Device) device).getEcmIpAddress();
		}
		return ipAddressForloginPage;
	}
}

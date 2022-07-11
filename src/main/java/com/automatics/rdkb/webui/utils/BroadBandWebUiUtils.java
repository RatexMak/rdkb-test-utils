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

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.assertthat.selenium_shutterbug.core.PageSnapshot;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;
import com.automatics.device.Dut;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiElements;
import com.automatics.rdkb.webui.page.BroadBandCommonPage;

public class BroadBandWebUiUtils {
    
    /**
     * Logger instance for {@link BroadBandWebUiUtils} class.
     */
    public static Logger LOGGER = LoggerFactory.getLogger(BroadBandWebUiUtils.class);
    /**
     * Save Web page Images in jenkins and Update the execution status based on current test steps for RDKB webgui
     * tests.
     * 
     * @param settop
     *            The settop to be used.
     * @param testId
     *            The manual test ID
     * @param testStepNumber
     *            The manual test step number.
     * @param status
     *            The execution status.
     * @param errorMessage
     *            The error message.
     * @param blockExecution
     *            The flag to throw an exception to block further execution of test cases.
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
     * @param webDriver
     *            The web driver.
     * @return Buffered image.
     * @refactor Govardhan
     */
    public static BufferedImage captureCurrentScreenFromDriver(final WebDriver webDriver) {
	final BufferedImage[] bufferredImages = new BufferedImage[1];
	final long threadTimeout = 15000L;
	/*
	 * ImageIO is blocking call, so added Thread to break blocking call after 15 seconds if it is blocked.
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
     * @param cmnDriver
     *            Web driver
     * @param locationId
     *            locatorId for the web element
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
     * @param driver,
     *            webdriver instance
     * @param string,
     *            Page title
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
     * @param xpath
     *            xpath for the element
     * @return return true if element not present,else false
     * @author Deepa Bada
     * @refactor Athira
     */
    public static boolean verifySaveOrRestoreConfigurationsButtonExists(String xpath,WebDriver driver) {
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
     * @param webDriver
     *            webDriver
     * @param elementId
     *            String containing element id
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
     * This method will to select stateless autoconfig in local Ip configuration page
     * 
     * @param TapEnv
     *            instance of {@link AutomaticsTapApi}
     * 
     * @param webDriver
     *            {@link WebDriver}
     * 
     * @param toSelectStateless
     *            To select or unselect SLAAC mode
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
    
    
}

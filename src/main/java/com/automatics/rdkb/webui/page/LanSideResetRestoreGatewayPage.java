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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.automatics.exceptions.TestException;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiTestConstant;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiTestConstant.RestoreGateWay;

public class LanSideResetRestoreGatewayPage  extends LanSideBasePage {
	
    // Element id to store reset gateway link text
    @FindBy(linkText = "RESET")
    private WebElement reset;
    // Element id to store reset wifi module link text
    @FindBy(linkText = "RESET Wi-Fi MODULE")
    private WebElement resetWifiModule;
    // Element id to store reset wifi router link text
    @FindBy(linkText = "RESET Wi-Fi ROUTER")
    private WebElement resetWifiRouter;
    // Element id to store reset wifi settings link text
    @FindBy(linkText = "RESTORE Wi-Fi SETTINGS")
    private WebElement restoreWifiSettings;
    // Element id to store reset wifi password link text
    @FindBy(linkText = "RESET PASSWORD")
    private WebElement resetPassword;
    // Element id to store factory reset link text
    @FindBy(linkText = "RESTORE FACTORY SETTINGS")
    private WebElement restoreFactorySettings;
    @FindBy(linkText = "RESET MTA MODULE")
    private WebElement resetMtaModule;
    // Element ID for pop up ok for factory reset
    @FindBy(xpath = "/html/body/div[@id='__alertLiveCont']/div[@id='popup_container']/div[@id='popup_content']/div[@id='popup_panel']/input[@id='popup_ok']")
    private WebElement acceptResetConfirmation;

	
    /**
     * Method to reset gateway settings based on request
     * 
     * @param resetType
     *            - reset type of the gateway
     * 
     * @author Gnanaprakasham.s
     * @refactor Athira
     */
    public void resetGatewaySettingsBasedOnRequest(RestoreGateWay resetType) {
	LOGGER.debug("STARTING METHOD : resetGatewaySettingsBasedOnRequest()");
	// Variable declaration starts
	WebElement locatorId = null;
	// Variable declaration ends
	try {
	    // click on Reset option from the 'Troubleshooting > Reset / Restore
	    // Gateway' page
	    LOGGER.info("Going to click " + resetType.toString()
		    + " Button in 'Troubleshooting > Reset / Restore Gateway' page");
	    locatorId = getWebElementIdFromReset(resetType);
	    locatorId.click();

	    String areYouSureAlert = BroadBandWebGuiTestConstant.STRING_ARE_YOU_SURE_ALERT_MESSAGE;
	    String xPathForPopup = BroadBandWebGuiTestConstant.XPATH_FOR_POP_UPS;
	    String validationTextInProgress = BroadBandWebGuiTestConstant.VALIDATION_TEXT_FOR_OPERATION_IN_PROGRESS;
	    if (resetType.equals(RestoreGateWay.RESTORE_FACTORY_SETTINGS_ITALIAN)) {
		xPathForPopup = BroadBandWebGuiTestConstant.XPATH_FOR_POP_UPS_ITALIAN;
		validationTextInProgress = BroadBandWebGuiTestConstant.VALIDATION_TEXT_FOR_OPERATION_IN_PROGRESS_MODAL;
	    }
	    if (resetType.equals(RestoreGateWay.RESET_MTA_MODULE)) {
		areYouSureAlert = BroadBandTestConstants.STRING_ARE_YOU_SURE_ALERT_MESSAGE;
		xPathForPopup = BroadBandWebGuiTestConstant.XPATH_FOR_POP_UPS;
		validationTextInProgress = BroadBandWebGuiTestConstant.VALIDATION_TEXT_FOR_OPERATION_IN_PROGRESS_MODAL;
	    }
	    LOGGER.info("Waiting for '" + areYouSureAlert + "' PopUp message.");
	    waitForTextToAppear(areYouSureAlert,
		    By.xpath(BroadBandWebGuiTestConstant.WEBELEMENT_ID_FACTORY_RESET_ALERT_MESSAGE));
	    LOGGER.info("Successfully verified the presence of '" + areYouSureAlert + "' popup message.");
	    // select okay button
	    LOGGER.info("Clicking on 'ok' button to accpet PopUp.");
	    acceptResetConfirmation.click();
	    if (!resetType.equals(RestoreGateWay.RESET_MTA_MODULE)) {
		// Validating Operation is in progress Message
		boolean isPopAvailWithId = false;
		try {
		    LOGGER.info("Pop up Message :" + driver
			    .findElement(By.xpath(BroadBandWebGuiTestConstant.XPATH_FOR_POP_UP_MODAL)).getText());
		    waitForTextToAppear(BroadBandWebGuiTestConstant.VALIDATION_TEXT_FOR_OPERATION_IN_PROGRESS_MODAL,
			    By.xpath(BroadBandWebGuiTestConstant.XPATH_FOR_POP_UP_MODAL));
		    isPopAvailWithId = true;
		} catch (Exception e) {
		    LOGGER.info("Pop up alert is updated , Verifying updated Pop alert");
		}
		if (!isPopAvailWithId) {
		    try {
			LOGGER.info("driver.findElement(By.xpath(xPathForPopup))==="
				+ driver.findElement(By.xpath(xPathForPopup)));
			isPopAvailWithId = driver.findElement(By.xpath(xPathForPopup)).isDisplayed();
			LOGGER.info("isPopAvailWithId====" + isPopAvailWithId);
			LOGGER.info("Pop up Message :" + driver.findElement(By.xpath(xPathForPopup)).getText());
			waitForTextToAppear(validationTextInProgress, By.xpath(xPathForPopup));
		    } catch (Exception e) {
			LOGGER.info("Pop up alert is updated , Verifying updated Pop alert");
		    }
		}
	    }
	} catch (Exception exception) {
	    throw new TestException("Exception occured during restarting WiFi Module => " + exception.getMessage());
	}
	LOGGER.debug("ENDING METHOD : resetGatewaySettingsBasedOnRequest()");
    }
    
    /**
     * Method to get the web element ID for corresponding gateway reset type
     * 
     * @param resetType
     *            - Gateway reset type
     * 
     * @return Locator ID of corresponding reset type
     * 
     * @author Gnanaprakasham.s
     */
    private WebElement getWebElementIdFromReset(RestoreGateWay resetType) {

	WebElement resetElementId = null;

	// get the web element id for corresponding reset type
	switch (resetType) {

	case RESTART_GATEWAY:
	    resetElementId = reset;
	    break;
	case RESTART_ONLY_WIFI_MODULE:
	    resetElementId = resetWifiModule;
	    break;
	case RESTART_ONLY_WIFI_ROUTER:
	    resetElementId = resetWifiRouter;
	    break;
	case RESTORE_WIFI_SETTINGS:
	    resetElementId = restoreWifiSettings;
	    break;
	case RESET_PASSWORD:
	    resetElementId = resetPassword;
	    break;
	case RESTORE_FACTORY_SETTINGS:
	    resetElementId = restoreFactorySettings;
	    break;
	case RESET_MTA_MODULE:
	    resetElementId = resetMtaModule;
	    break;
	default:
	    LOGGER.error("Invalid gateway reset request");
	    break;
	}

	return resetElementId;

    }
    
    /**
     * Method to reset factory settings
     * 
     * @author Gnanaprakasham S
     * @refactor Athira
     */
    public void restoreFactorySettings() {
	LOGGER.debug("STARTING METHOD: restoreFactorySettings()");
	// Variable declaration starts
	String areYouSureAlert = BroadBandWebGuiTestConstant.ARE_YOU_SURE_FACTORY_RESTORE;
	// Variable declaration ends
	try {
	    // clicking on reset option from the 'Troubleshooting > Reset / Restore Gateway' page
	    LOGGER.info(
		    "Going to click RESTORE FACTORY SETTINGS Button in 'Troubleshooting > Reset / Restore Gateway' page");
	    click(By.id(BroadBandWebGuiTestConstant.ELEMENT_ID_FACTORY_RESET_BUTTON));
	    
		LOGGER.info("In Else part===");
		waitForTextToAppear(areYouSureAlert,
			By.xpath(BroadBandWebGuiTestConstant.STRING_XPATH_FOR_POPUP_MESSAGE));
	   
	    LOGGER.info("Successfully verified the presence of 'Are You Sure?' popup message");

	    // select okay button
	    LOGGER.info("Clicking on Ok button to accept Pop Up.");
	    click(By.id(BroadBandWebGuiTestConstant.ELEMENT_ID_OK_BUTTON_POP_UP_MESSAGE));
	    LOGGER.info("Pop Up Accepted.");
	} catch (Exception exception) {
	    throw new TestException("Exception occured during execution => " + exception.getMessage());
	}
	LOGGER.debug("ENDING METHOD: restoreFactorySettings()");
    }

}

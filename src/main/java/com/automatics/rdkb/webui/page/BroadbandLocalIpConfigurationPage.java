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

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.automatics.device.Dut;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiTestConstant;
import com.automatics.rdkb.webui.menu.BroadBandWebUiGatewayEnum;
import com.automatics.rdkb.webui.utils.BroadBandWebUiUtils;
import com.automatics.tap.AutomaticsTapApi;

public class BroadbandLocalIpConfigurationPage extends BroadBandBasePage {

	public BroadbandLocalIpConfigurationPage(WebDriver driver) {
		super(driver);
	}

	/**
	 * Method with parameter which ID's are in series to enter text into text box
	 * 
	 * @param webDriver
	 * @param textArrayToEnter
	 * @param elementId
	 * @param counterOfTextSeries
	 * @return broadbandresultobject
	 * @refactor Said Hisham
	 */
	public static BroadBandResultObject enterValuesInSeriesIntoTextBoxUsingId(WebDriver webDriver,
			String[] textArrayToEnter, String elementId, Integer counterOfTextSeries) {
		LOGGER.debug("STARTING METHOD: enterValuesInSeriesIntoTextBoxUsingId");
		BroadBandResultObject broadBandResultObject = new BroadBandResultObject();
		String errorMessage = "Unable to enter values in specified Element ID";
		boolean status = false;
		try {
			for (int count = 0; count < textArrayToEnter.length; count++) {
				webDriver.findElement(By.id(elementId + counterOfTextSeries)).clear();
				webDriver.findElement(By.id(elementId + counterOfTextSeries)).sendKeys(textArrayToEnter[count]);
				counterOfTextSeries++;
				if (count == textArrayToEnter.length - 1)
					status = true;
			}
		} catch (Exception e) {
			status = false;
			LOGGER.error("Exception while entering values into Element ID");
		}
		broadBandResultObject.setErrorMessage(errorMessage);
		broadBandResultObject.setStatus(status);
		LOGGER.debug("ENDING METHOD: enterValuesInSeriesIntoTextBoxUsingId");
		return broadBandResultObject;
	}
	
    /**
     * Method takes Input Map and String to Iterate over given elements,verify text fields available to configure
     * 
     * @param valuesInMapToValidate
     * @param valueElementIdToValidate
     * @param webDriver
     * @return broadbandresultobject
     * 
     * @refactor Athira
     */
    public static BroadBandResultObject validateTextFieldsIsAvailable(Map<String, String> valuesInMapToValidate,
	    String valueElementIdToValidate, WebDriver webDriver) {
	BroadBandResultObject broadBandResultObject = new BroadBandResultObject();
	boolean status = false;
	String errorMessage = "Unable to validate text fields";
	try {
	    for (int i = 1; i <= valuesInMapToValidate.size(); i++) {
		switch (valuesInMapToValidate.get(valueElementIdToValidate + i)) {
		case BroadBandTestConstants.STRING_ENABLED:
		    status = webDriver.findElement(By.id(valueElementIdToValidate + i)).isEnabled()
			    && webDriver.findElement(By.id(valueElementIdToValidate + i)).isDisplayed();
		    LOGGER.info("ELEMENT AVAILABLE TO CONFIGURE STATUS :" + status);
		    errorMessage = "ELEMENT TEXT BOX " + i + " IS NOT CONFIGURABLE ";
		    break;

		case BroadBandTestConstants.STRING_DISABLED:
		    status = !webDriver.findElement(By.id(valueElementIdToValidate + i)).isEnabled()
			    && webDriver.findElement(By.id(valueElementIdToValidate + i)).isDisplayed();
		    LOGGER.info("ELEMENT NOT AVAILABLE TO CONFIGURE STATUS :" + status);
		    errorMessage = "ELEMENT TEXT BOX " + i + " IS CONFIGURABLE ";
		    break;

		default:
		    status = false;
		    errorMessage = "ELEMENT NOT FOUND IN GIVEN MAP";
		    break;
		}
		if (!status)
		    break;

	    }
	} catch (Exception e) {
	    LOGGER.error("Exception caught while validating fields");
	}
	broadBandResultObject.setStatus(status);
	broadBandResultObject.setErrorMessage(errorMessage);
	return broadBandResultObject;
    }
    
	/**
	 * Helper method to navigate to local ip page from At Glance page
	 * 
	 * @param tapEnv AutomaticstapApi instance
	 * @return true if navigation to local ip page is success.
	 * 
	 * @Refactor Sruthi Santhosh
	 */
	public boolean navigateToLocalIpPage(AutomaticsTapApi tapEnv, Dut device) {
		LOGGER.debug("STARTING METHOD: navigateToLocalIpPage()");
		boolean localIpPageNavigationStatus = false;
		try {

			LOGGER.info("Clicking on Connection Expand button");
			LanSideBasePage.click(By.linkText(BroadBandWebUiGatewayEnum.CONNECTION.getCommand()));
			tapEnv.waitTill(BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS);
			LOGGER.info("Clicking on Local Ip Network button");
			WebElement localIpButton = driver
					.findElement(By.linkText(BroadBandWebGuiTestConstant.STRING_NAME_LOCAL_IP_NETWORK));
			if (null != localIpButton) {
				localIpButton.click();
				LOGGER.info("Validating local IP page navigation");
				long startTime = System.currentTimeMillis();
				do {
					localIpPageNavigationStatus = BroadBandWebUiUtils.validatePageLaunchedStatusWithPageTitle(driver,
							BroadbandPropertyFileHandler.getPageTitleForLocalIpNetwork());
				} while ((System.currentTimeMillis() - startTime) < BroadBandTestConstants.THREE_MINUTE_IN_MILLIS
						&& !localIpPageNavigationStatus && BroadBandCommonUtils.hasWaitForDuration(tapEnv,
								BroadBandTestConstants.THIRTY_SECOND_IN_MILLIS));
			} else {
				LOGGER.error("Local IP Tab not present. Failed to navigate to Local IP Page");
			}
		} catch (Exception exception) {
			localIpPageNavigationStatus = false;
			LOGGER.error("Failed to navigate to local IP page. Error - " + exception.getMessage());
		}
		LOGGER.info("Is navigation to Local IP page success - " + localIpPageNavigationStatus);
		LOGGER.debug("Ending METHOD: navigateToLocalIpPage()");
		return localIpPageNavigationStatus;

	}
	
    /**
     * Method to retrieve text with parameter which ID's are in series
     * 
     * @param webDriver
     * @param textArrayToEnter
     * @param elementId
     * @param counterOfTextSeries
     * @return value
     * 
     * @refactor Athira
     */
    public static String retrieveValuesInSeriesFromTextBoxUsingId(WebDriver webDriver, String elementId,
	    Integer startCounter, Integer endCounter, String delimiter) {
	LOGGER.debug("STARTING METHOD: retrieveValuesInSeriesFromTextBoxUsingId");
	String valueRetrieved = "";
	try {
	    for (int count = startCounter; count <= endCounter; count++) {
		valueRetrieved = valueRetrieved + webDriver.findElement(By.id(elementId + count))
			.getAttribute(BroadBandTestConstants.STRING_VALUE);
		if (count != endCounter) {
		    valueRetrieved = valueRetrieved + delimiter;
		}
	    }
	} catch (Exception e) {
	    valueRetrieved = null;
	    LOGGER.error("Exception while retrieving values into Element ID " + e.getMessage());
	}

	LOGGER.debug("ENDING METHOD: retrieveValuesInSeriesFromTextBoxUsingId");
	return valueRetrieved;
    }
	
}
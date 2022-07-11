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

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.automatics.rdkb.BroadBandResultObject;

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
}
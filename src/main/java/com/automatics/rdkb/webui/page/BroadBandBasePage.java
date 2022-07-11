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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BroadBandBasePage {
	/**
	 * Logger instance for all WebGUI pages.
	 */
	protected static final Logger LOGGER = LoggerFactory.getLogger(BroadBandBasePage.class);

	/**
	 * The driver instance
	 */
	protected WebDriver driver;

	/**
	 * {@link BroadBandBasePage} constructor.
	 * 
	 * @param driver The driver instance.
	 */
	public BroadBandBasePage(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Method to give explicit wait
	 * 
	 */

	public void waitForTextToAppear(String textToAppear, By element) {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.textToBePresentInElementLocated(element, textToAppear));
	}

}

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
import org.openqa.selenium.support.PageFactory;

import com.automatics.exceptions.TestException;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiTestConstant;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;
import com.automatics.rdkb.webui.page.BroadBandBasePage;

/**
 * Class to map Troubleshooting > Network Diagnostic page
 * 
 * @author Gnanaprakasham.s
 * 
 */
public class BroadBandDiagnosticToolsPage extends BroadBandBasePage {

	/**
	 * The {@link BroadBandDiagnosticToolsPage} constructor.
	 * 
	 * @param driver The web driver instance.
	 */

	public BroadBandDiagnosticToolsPage(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	/**
	 * Method to get the connectivity status for IPv4 address in diagnostic page
	 * 
	 * @author Gnanaprakasham.s
	 * 
	 * @Refactor Sruthi Santhosh
	 */
	public boolean verifyConnectivityTestForIpv4Address(AutomaticsTapApi tapEnv, String expectedResponse) {

		boolean status = false;

		LanSideBasePage.click(By.id(BroadBandWebGuiTestConstant.WEB_ELEMENT_ID_IPV4_CONNECTIVITY_CHECK));

		LanSideBasePage.waitForTextToAppear(BroadBandWebGuiTestConstant.VALIDATION_TEXT_FOR_OPERATION_IN_PROGRESS_MODAL,
				By.xpath(BroadBandWebGuiTestConstant.XPATH_FOR_POP_UP_MODAL));

		tapEnv.waitTill(BroadBandTestConstants.ONE_MINUTE_IN_MILLIS);

		String connectivityResponse = driver
				.findElement(By.id(BroadBandWebGuiTestConstant.WEB_ELEMENT_ID_GET_IPV4_CONNECTIVITY_STATUS)).getText();

		if (CommonMethods.isNotNull(connectivityResponse)) {

			status = connectivityResponse.equals(expectedResponse);

			if (!status) {
				throw new TestException(
						"Failed to get the expected response for ipv4 connectivity check . Expected response is : "
								+ expectedResponse + " but obtained respo se is : " + connectivityResponse);

			}
		} else {
			throw new TestException("Obtained null response for connectivity check for ipv4 address ");
		}
		return status;

	}
}

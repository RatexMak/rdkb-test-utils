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

import org.openqa.selenium.By;

import com.automatics.rdkb.webui.constants.BroadBandWebGuiTestConstant;
import com.automatics.rdkb.webui.exception.PageTitleNotFoundException;
import com.automatics.utils.CommonMethods;
import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;

public class LanSideTroubleShootingPage extends LanSideBasePage {
	
    /**
     * Method to select a particular link from current page
     * 
     */
    public void selectLinkText(String link) {
	driver.findElement(By.linkText(link)).click();
	;
    }

    /**
     * Method to Navigate to trouble shooting page and verify navigation status
     * 
     * throws exception when navigation failed
     */

    public void navigateToTroubleShootingHomePage() {
	LOGGER.debug("STARTING METHOD : navigateToTroubleShootingHomePage()");
	// Variable to store status
	boolean status = false;
	String throubleShootingPageTitle = "";

	/**
	 * Clicking on Troubleshooting link to navigate to Troubleshooting home page.
	 */
	selectLinkText(BroadBandWebGuiTestConstant.ELEMENT_ID_TROUBLE_SHOOTING_PAGE);
	LOGGER.info("Waiting for text to appear 'Troubleshooting > Logs'.");
	waitForTextToAppear(BroadBandWebGuiTestConstant.STRING_TROUBLE_SHOOTING_PAGE_HEADING,
		By.xpath(BroadBandWebGuiTestConstant.ELEMENT_ID_TROUBLE_SHOOTING_PAGE_HEADING));
	LOGGER.info("Desired text 'Troubleshooting > Logs' appeared successfully.");
	LOGGER.info("Getting page title of Troubleshooting home page.");
	throubleShootingPageTitle = driver.getTitle();
	LOGGER.info("Obtained trouble shooting page title is: " + throubleShootingPageTitle);
	/** Verifying null value for page title */
	if (CommonMethods.isNotNull(throubleShootingPageTitle)) {

	    LOGGER.info("Verifying page title for obtained trouble shooting home page.");
	    status = CommonMethods.patternMatcher(throubleShootingPageTitle.toLowerCase(),
		    BroadBandWebGuiTestConstant.ELEMENT_ID_TROUBLE_SHOOTING_PAGE.toLowerCase());
	    if (status) {
		LOGGER.info("Successfully verified Troubleshooting page navigation status: " + status);
	    } else {
		LOGGER.error("Failed to navigate to troubleshooting page from Home page !!!! ");
	    }
	} else {
	    LOGGER.error("Obtained trouble shooting page title is null.");
	    throw new PageTitleNotFoundException(BroadbandPropertyFileHandler.getPageTitleForTroubleShootingLogs());
	}
	LOGGER.debug("ENDING METHOD : navigateToTroubleShootingHomePage()");
    }

    /**
     * Method to click on Reset / Restore Gateway in Troubleshooting menu
     * 
     * @return instance of {@link BroadBandResetRestoreGatewayPage} else null
     * @refactor Athira
     */
    public LanSideResetRestoreGatewayPage clickOnRestartRestoreGateway(Boolean... isDSLDevice) {

	// page title
	String pageTitle = null;
	// validation status
	boolean status = false;
	// instance of BroadBandResetRestoreGatewayPage to map to 'Troubleshooting >
	// Reset / Restore Gateway'
	LanSideResetRestoreGatewayPage resetRestoreGatewayPage = null;

	try {
	    // navigate to Trouble shooting home page
	    navigateToTroubleShootingHomePage();

	    // select Reset/Restore Gateway option
	    navigateToGatewayResetPage();
	    // read current page title
	    pageTitle = driver.getTitle();

	    if (CommonMethods.isNotNull(pageTitle)) {
		LOGGER.info("Obtained Page title from driver instance : " + pageTitle);
		status = pageTitle.toLowerCase()
			.contains(BroadbandPropertyFileHandler.getPageTitleForResetRestoreGateway().toLowerCase())
			|| pageTitle.toLowerCase()
				.contains(BroadBandWebGuiTestConstant.RESET_RESTORE_GATEWAY_PAGE_TITLE_1.toLowerCase());
		LOGGER.info("The verification status of 'Troubleshooting > Reset / Restore Gateway' is " + status);
		if (status) {
		    resetRestoreGatewayPage = new LanSideResetRestoreGatewayPage();
		} else {
		    throw new PageTitleNotFoundException(BroadbandPropertyFileHandler.getPageTitleForResetRestoreGateway());
		}

	    } else {
		throw new PageTitleNotFoundException(BroadbandPropertyFileHandler.getPageTitleForResetRestoreGateway());
	    }
	} catch (Exception exception) {
	    LOGGER.error("Exception occured in clickOnRestartRestoreGateway(). ie, " + exception.getMessage(),
		    exception);
	    throw new PageTitleNotFoundException(exception.getMessage());
	}
	return resetRestoreGatewayPage;
    }

    /**
     * Method to navigate to gateway reset page and verify navigation status
     * 
     */

    private void navigateToGatewayResetPage() {

	// Variable to store status
	boolean status = false;
	String resetRestoregatewayLinkText = BroadBandWebGuiTestConstant.ELEMENT_ID_RESET_GATEWAY_IN_TROUBLESHOOTING_PAGE;
	String resetPageHeading = BroadBandWebGuiTestConstant.STRING_GATEWAY_RESET_PAGE_HEADING;
	String resetPageTitle = BroadbandPropertyFileHandler.getPageTitleForResetRestoreGateway();
	String gatewayResetPageTitle = "";

	LOGGER.info("In navigateToGatewayResetPage :resetRestoregatewayLinkText is : " + resetRestoregatewayLinkText);
	selectLinkText(resetRestoregatewayLinkText);
	waitForTextToAppear(resetPageHeading,
		By.xpath(BroadBandWebGuiTestConstant.ELEMENT_ID_GATEWAY_RESET_PAGE_HEADING));

	gatewayResetPageTitle = driver.getTitle();
	LOGGER.info("Obtained gateway reset page title : " + gatewayResetPageTitle);
	if (CommonMethods.isNotNull(gatewayResetPageTitle)) {

	    status = CommonMethods.patternMatcher(gatewayResetPageTitle.toLowerCase(), resetPageTitle.toLowerCase())
		    || CommonMethods.patternMatcher(gatewayResetPageTitle.toLowerCase(),
			    BroadBandWebGuiTestConstant.RESET_RESTORE_GATEWAY_PAGE_TITLE_1.toLowerCase());
	    if (status) {
		LOGGER.info("Successfully verified Trouble shooting page navigation status" + status);
	    } else {
		LOGGER.error("Failed to navigate to gateway reset page frm trouble shooting page !!!!!");
		throw new PageTitleNotFoundException(resetPageTitle);
	    }
	} else {
	    throw new PageTitleNotFoundException(resetPageTitle);
	}
    }



}

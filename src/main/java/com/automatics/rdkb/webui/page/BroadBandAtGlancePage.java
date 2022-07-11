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

import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;
import com.automatics.utils.CommonMethods;

public class BroadBandAtGlancePage extends BroadBandBasePage {
    public BroadBandAtGlancePage(WebDriver driver) {
	super(driver);	
    }
    
    /**
     * Method to validate At Glance page launched status
     * 
     * @return true if home page launched successfully
     * @throws PageTitleNotFoundException
     * 
     * @author sgnana010c
     */

    public boolean verifyAtGlancePageLaunchedStatus() {
	LOGGER.debug("STARTING METHOD : verifyAtGlancePageLaunchedStatus ()");
	boolean status = false;
	// Variable to store home page title
	String pageTitle = null;

	try {
	    LOGGER.info("Driver:::" + driver);
	    pageTitle = driver.getTitle();
	    LOGGER.info("pageTitle:::" + pageTitle);
	    if (CommonMethods.isNotNull(pageTitle)) {
		LOGGER.info("Obtained Page title from driver instance : " + pageTitle);
		status = pageTitle.contains(BroadbandPropertyFileHandler.getAtAGlancePageTitle());
		LOGGER.info("Verified AdminUI Home page title, Expected title:"
			+ BroadbandPropertyFileHandler.getAtAGlancePageTitle() + "Actual title: " + pageTitle
			+ " status for launching atGlancePage:" + status);

	    } else {
		LOGGER.error("Page title returned as NULL..Expected Page title should be :"
			+ BroadbandPropertyFileHandler.getAtAGlancePageTitle());
	    }
	} catch (Exception e) {
	    LOGGER.error("Exception occure while verifying At Glance Page.");
	}
	LOGGER.debug("ENDING METHOD : verifyAtGlancePageLaunchedStatus ()");
	return status;
    }

}

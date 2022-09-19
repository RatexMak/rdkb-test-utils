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

package com.automatics.rdkb.webui;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;
import com.automatics.test.AutomaticsTestBase;

/**
 * Class to hold base test for Web GUI Automation
 * 
 * @author Gnanaprakasham S
 * @Refactor Alan_Bivera
 */
public class BroadBandWebUiBaseTest extends AutomaticsTestBase{
    
    /** SLF4j logger. */
    public static final Logger LOGGER = LoggerFactory.getLogger(BroadBandWebUiBaseTest.class);

    /**
     * System property which decides which browser should we use for validation.
     */
    private static final String BROWSER_SWITCH_CHROME_BROWSER = "USE_CHROME_BROWSER";


    private static final String SELENIUM_FIREFOX_DEFAULT_BINARY_PATH = "/usr/local/bin/geckodriver";

    protected WebDriver driver;
    

    /**
     * 
     * Method to invoke browser and set all the necessary preconditions
     * 
     */
    public void invokeBrowser() throws Exception {

	LOGGER.debug("STARTING METHOD :invokeBrowser() ");

	String driverPath = BroadbandPropertyFileHandler.getBinaryPathOfSelenium();
	DesiredCapabilities caps = DesiredCapabilities.phantomjs();

	ArrayList<String> cliArgsCap = new ArrayList<String>();
	cliArgsCap.add("--web-security=false");
	cliArgsCap.add("--ssl-protocol=any");
	cliArgsCap.add("--ignore-ssl-errors=true");
	cliArgsCap.add("--webdriver-loglevel=NONE");

	caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
	caps.setCapability("takesScreenshot", true);
	caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, driverPath);

	driver = new PhantomJSDriver(caps);

	driver.manage().window().maximize();
	driver.manage().deleteAllCookies();
	LOGGER.debug("ENDING METHOD :invokeBrowser() ");
    }


}

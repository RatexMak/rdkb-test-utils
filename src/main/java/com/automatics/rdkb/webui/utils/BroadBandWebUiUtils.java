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

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.assertthat.selenium_shutterbug.core.PageSnapshot;
import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;
import com.automatics.device.Dut;
import com.automatics.tap.AutomaticsTapApi;

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
}

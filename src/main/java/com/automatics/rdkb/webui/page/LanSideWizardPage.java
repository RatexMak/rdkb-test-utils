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

import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.webui.constants.BroadBandWebGuiElements;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.rdkb.utils.BroadBandCommonUtils;
import com.automatics.rdkb.utils.BroadbandPropertyFileHandler;


public class LanSideWizardPage extends LanSideBasePage  {
	
    /**
     * Utils method to validate the home network password cannot be set with password mismatch
     * 
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @return true -if new password and re-enter password is mismatched false-else
     * @refactor Govardhan
     */
    public boolean validateHomeNetworkPasswordMismatch(AutomaticsTapApi tapEnv) {
	LOGGER.debug("STARTING METHOD: validateHomeNetworkPasswordMismatch()");
	boolean status = false;
	try {

	    LOGGER.info("Entering the New Password : " + BroadBandTestConstants.PASSWORD_WITH_LETTERS_AND_NUMBERS);
	    LanSideBasePage.clear(
		    By.xpath(BroadBandWebGuiElements.XPATH_TROUBLESHOOTING_CHANGE_CREDENTIALS_NEW_PASSWORD_TEXT_BOX));
	    LanSideBasePage.sendKeys(
		    By.xpath(BroadBandWebGuiElements.XPATH_TROUBLESHOOTING_CHANGE_CREDENTIALS_NEW_PASSWORD_TEXT_BOX),
		    BroadBandTestConstants.PASSWORD_WITH_LETTERS_AND_NUMBERS);

	    LOGGER.info("Entering the Re-Enter Password : "
		    + BroadbandPropertyFileHandler.getTheHomeNetworkPasswordWithProperStandards());
	    LanSideBasePage.clear(By.xpath(
		    BroadBandWebGuiElements.XPATH_TROUBLESHOOTING_CHANGE_CREDENTIALS_RE_ENTER_NEW_PASSWORD_TEXT_BOX));
	    LanSideBasePage.sendKeys(By.xpath(
		    BroadBandWebGuiElements.XPATH_TROUBLESHOOTING_CHANGE_CREDENTIALS_RE_ENTER_NEW_PASSWORD_TEXT_BOX),
		    BroadbandPropertyFileHandler.getTheHomeNetworkPasswordWithProperStandards());
	    tapEnv.waitTill(BroadBandTestConstants.THREE_SECOND_IN_MILLIS);

	    LOGGER.info("Click on Save button!");
	    LanSideBasePage
		    .click(By.xpath(BroadBandWebGuiElements.XPATH_TROUBLESHOOTING_CHANGE_CREDENTIALS_SAVE_BUTTON));
	    tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);

	    LOGGER.info("Verifying the error message!");
	    status = BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
		    BroadBandTestConstants.PASSWORD_MISMATCH_ERROR_MESSAGE, LanSideBasePage
			    .getText(By.xpath(BroadBandWebGuiElements.XPATH_FOR_RETYPE_PASSWORD_ERROR_UI_PORTAL)));
	} catch (Exception e) {
	    LOGGER.error(
		    "Exception occurred while verifying the home network password cannot be set with password mismatch : "
			    + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD: validateHomeNetworkPasswordMismatch()");
	return status;
    }

    /**
     * Utils method to validate home network password fields with blank
     *
     * @return status true -if blank false -else
     */
    public boolean validatePasswordFieldsWithBlank() {
	LOGGER.debug("STARTING METHOD: validatePasswordFieldsWithBlank()");
	boolean status = false;
	boolean currentStatus = false;
	boolean newStatus = false;
	boolean reenterStatus = false;
	try {

	    LanSideBasePage
		    .click(By.xpath(BroadBandWebGuiElements.XPATH_TROUBLESHOOTING_CHANGE_CREDENTIALS_SAVE_BUTTON));
	    LOGGER.info("Leaving the 'current Password' as blank!");

	    currentStatus = BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
		    BroadBandTestConstants.PASSWORD_BLANK_FIELD_ERROR_MESSAGE, LanSideBasePage.getText(
			    By.xpath(BroadBandWebGuiElements.XPATH_FOR_CURRENT_PASSWORD_ERROR_UI_PORTAL)));
	    LOGGER.info("Leaving the 'New Password' as blank!");
	    newStatus = BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
		    BroadBandTestConstants.PASSWORD_BLANK_FIELD_ERROR_MESSAGE, LanSideBasePage
			    .getText(By.xpath(BroadBandWebGuiElements.XPATH_FOR_NEW_PASSWORD_ERROR_UI_PORTAL)));

	    LOGGER.info("Leaving the 're enter Password' as blank!");
	    reenterStatus = BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
		    BroadBandTestConstants.PASSWORD_BLANK_FIELD_ERROR_MESSAGE, LanSideBasePage
			    .getText(By.xpath(BroadBandWebGuiElements.XPATH_FOR_RETYPE_PASSWORD_ERROR_UI_PORTAL)));
	    LOGGER.info("Verifying the error message!");
	    status = currentStatus && newStatus && reenterStatus;
	} catch (Exception e) {
	    LOGGER.error("Exception occurred while verifying the home network password cannot be set as blank : "
		    + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD: validatePasswordFieldsWithBlank()");
	return status;
    }
    
    /**
     * Utils method to configure home network password
     *
     * @param tapEnv
     *            instance of {@link AutomaticsTapApi}
     * @param currentPassword
     *            current password
     * @param newPassword
     *            new password
     * @return status true-if new password entered with space/special char
     * @refactor Govardhan
     */
    public boolean validateNewPasswordFieldWithSpaceOrSpecialChar(AutomaticsTapApi tapEnv, String currentPassword,
	    String newPassword) {
	LOGGER.debug("STARTING METHOD: validateNewPasswordFieldWithSpaceOrSpecialChar()");
	boolean status = false;
	try {
	    LOGGER.info("Entering current password : " + currentPassword);
	    LanSideBasePage.clear(By
		    .xpath(BroadBandWebGuiElements.XPATH_TROUBLESHOOTING_CHANGE_CREDENTIALS_CURRENT_PASSWORD_TEXT_BOX));
	    LanSideBasePage.sendKeys(
		    By.xpath(
			    BroadBandWebGuiElements.XPATH_TROUBLESHOOTING_CHANGE_CREDENTIALS_CURRENT_PASSWORD_TEXT_BOX),
		    currentPassword);

	    LOGGER.info("Entering the New Password : " + newPassword);
	    LanSideBasePage.clear(
		    By.xpath(BroadBandWebGuiElements.XPATH_TROUBLESHOOTING_CHANGE_CREDENTIALS_NEW_PASSWORD_TEXT_BOX));
	    LanSideBasePage.sendKeys(
		    By.xpath(BroadBandWebGuiElements.XPATH_TROUBLESHOOTING_CHANGE_CREDENTIALS_NEW_PASSWORD_TEXT_BOX),
		    newPassword);

	    LOGGER.info("Entering the Re-Enter Password : " + newPassword);
	    LanSideBasePage.clear(By.xpath(
		    BroadBandWebGuiElements.XPATH_TROUBLESHOOTING_CHANGE_CREDENTIALS_RE_ENTER_NEW_PASSWORD_TEXT_BOX));
	    LanSideBasePage.sendKeys(By.xpath(
		    BroadBandWebGuiElements.XPATH_TROUBLESHOOTING_CHANGE_CREDENTIALS_RE_ENTER_NEW_PASSWORD_TEXT_BOX),
		    newPassword);

	    LOGGER.info("Click on Save button!");
	    LanSideBasePage
		    .click(By.xpath(BroadBandWebGuiElements.XPATH_TROUBLESHOOTING_CHANGE_CREDENTIALS_SAVE_BUTTON));
	    tapEnv.waitTill(BroadBandTestConstants.TEN_SECOND_IN_MILLIS);

	    LOGGER.info("Verifying the error message!");
	    status = BroadBandCommonUtils.compareValues(BroadBandTestConstants.CONSTANT_TXT_COMPARISON,
		    BroadBandTestConstants.PASSWORD_SPECIAL_CHARACTER_AND_SPACE_ERROR_MESSAGE, LanSideBasePage
			    .getText(By.xpath(BroadBandWebGuiElements.XPATH_FOR_NEW_PASSWORD_ERROR_UI_PORTAL)));

	} catch (Exception e) {
	    LOGGER.error(
		    "Exception occurred while verifying the home network password can be set only within the standards : "
			    + e.getMessage());
	}
	LOGGER.debug("ENDING METHOD: validateNewPasswordFieldWithSpaceOrSpecialChar()");
	return status;

    }

}

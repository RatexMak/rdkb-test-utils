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
package com.automatics.rdkb.utils.cdl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.automatics.constants.AutomaticsConstants;
import com.automatics.device.Dut;
import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;

public class CodeDownloadUtils {

    /** SLF4J logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeDownloadUtils.class);

    /** Identifies the beginning of image name in the telnet out put. */
    private static final String IDENTIFIER_FOR_BEGINNING_OF_IMAGE_NAME = "imagename";

    /** Deployed Version Service Url : STB Property **/
    private static final String DEPLOYED_VERSION_SERVICE_BASE_URL = "deployed.version.service.base.url";

    /** user Agent to access deployed version service *************/
    private static final String DEPLOYED_VERSION_SERVICE_USER_AGENT = "deployed.version.service.userAgent";

    /** Authorization to access deployed version service *****/
    private static final String DEPLOYED_VERSION_SERVICE_AUTHORIZATION = "deployed.version.service.authorization";

    /** HTTP 200 status */
    private static final int HTTP_CODE_200 = 200;

    /** GA Group Name **/
    private static final String GA_GROUP = "gAcceptance";

    /** Internals Group Name **/
    private static final String INTERNALS_GROUP = "internals";

    /** 'Not Available' Response in Deployed Version Service API **/
    private static final String NOT_AVAILABLE = "Not_Available";

    /** The constant for the build name for signed image. */
    private static final String SIGNED_BUILD_IMAGE_EXTENSION = "-signed";

    /**
     * Method to parse the output of version details and provide the image version alone.
     * 
     * @param versionOutput
     *            output of Linux command to retrieve image version.
     * 
     * @return the image version.
     */
    public static String parseOutputToGetImageVersion(String commandOutput) {
	String imageVersion = null;
	boolean isImageVersionFound = false;

	// For line by line parsing.
	String[] splittedTopCommandOutput = commandOutput.split("\n");

	for (String lineByLineOutput : splittedTopCommandOutput) {
	    String trimedLineOutput = lineByLineOutput.trim();
	    LOGGER.debug("Line by line output : " + trimedLineOutput);

	    // Skipping empty line and line beginning with hash.
	    if (!trimedLineOutput.startsWith(AutomaticsConstants.DELIMITER_HASH)
		    && !AutomaticsConstants.EMPTY_STRING.equals(trimedLineOutput)
		    && trimedLineOutput.startsWith(IDENTIFIER_FOR_BEGINNING_OF_IMAGE_NAME) && !isImageVersionFound) {

		imageVersion = trimedLineOutput.replaceFirst(IDENTIFIER_FOR_BEGINNING_OF_IMAGE_NAME,
			AutomaticsConstants.EMPTY_STRING);
		imageVersion = imageVersion.replaceFirst("(:|=)", AutomaticsConstants.EMPTY_STRING);
		isImageVersionFound = true;
	    }
	}

	return imageVersion;
    }

    /**
     * Method to get the current running image name from Version.txt file
     * 
     * @param device
     *            Device under test
     * @param tapEnv
     *            {@code AutomaticsTapApi} instance
     * @return Current running image version
     */
    public static String getCurrentRunningImageNameFromVersionTxtFile(Dut device, AutomaticsTapApi tapEnv) {

	LOGGER.info("STARTING getCurrentRunningImageNameFromVersionTxtFile()");
	String versionDetails = tapEnv.executeCommandUsingSsh(device,
		RDKBTestConstants.CMD_GREP_IMAGE_NAME_FROM_VERSION_FILE);
	LOGGER.info("Console output of version.txt file : " + versionDetails);
	versionDetails = CommonMethods.patternFinder(versionDetails, "imagename[:=](\\S+)");
	String imageVersion = parseOutputToGetImageVersion(versionDetails);
	if (CommonMethods.isNotNull(imageVersion)) {
	    imageVersion = imageVersion.trim();
	    imageVersion = imageVersion.contains(AutomaticsConstants.BINARY_BUILD_IMAGE_EXTENSION)
		    ? imageVersion.replaceAll(AutomaticsConstants.BINARY_BUILD_IMAGE_EXTENSION, "")
		    : imageVersion;
	}
	LOGGER.info("Version details retrived : " + imageVersion);

	LOGGER.info("COMPLETED getCurrentRunningImageNameFromVersionTxtFile()");
	return versionDetails;
    }

    /**
     * Verifies the image version passed with the version on version.txt file.
     * 
     * @param tapEnv
     *            Automatics tap instance.
     * @param device
     *            The Dut box where the command to be executed.
     * @param requestImageVersion
     *            Build version
     * 
     * @return true if the version is matching with requested one.
     */
    public static boolean verifyImageVersionFromVersionText(AutomaticsTapApi tapEnv, Dut device,
	    String requestImageVersion) {

	LOGGER.debug("STARTING METHOD: CodeDownloadUtils.verifyImageVersionFromVersionText");
	/** Modified by New Device Team **/
	boolean versionMatches = false;

	String versionDetails = tapEnv.executeCommandUsingSsh(device,
		RDKBTestConstants.CMD_GREP_IMAGE_NAME_FROM_VERSION_FILE);
	LOGGER.debug("Console output of version.txt file : " + versionDetails);

	if (CommonMethods.isNull(versionDetails)) {
	    LOGGER.error("Obtained null response for " + RDKBTestConstants.CMD_GREP_IMAGE_NAME_FROM_VERSION_FILE
		    + " command execution. Hence try one more time");
	    versionDetails = tapEnv.executeCommandUsingSsh(device,
		    RDKBTestConstants.CMD_GREP_IMAGE_NAME_FROM_VERSION_FILE);
	}

	String imageVersion = parseOutputToGetImageVersion(versionDetails);
	LOGGER.info("Firmeware Version retrived from version.txt : " + imageVersion + ", Requested Firmeware Version : "
		+ requestImageVersion);
	if (CommonMethods.isNotNull(imageVersion) && CommonMethods.isNotNull(requestImageVersion)) {
	    imageVersion = imageVersion.trim();
	    LOGGER.debug("Image version : " + imageVersion);
	    requestImageVersion = requestImageVersion.trim();
	    requestImageVersion = StringUtils
		    .replaceEach(requestImageVersion, RDKBTestConstants.IMAGE_VERSION, RDKBTestConstants.REPLACE)
		    .trim();
	    versionMatches = requestImageVersion.equalsIgnoreCase(imageVersion);
	    LOGGER.debug("verifyImageVersionFromVersionText - " + versionMatches);
	}
	LOGGER.debug("ENDING METHOD: CodeDownloadUtils.verifyImageVersionFromVersionText");
	return versionMatches;
    }
}

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

/**
 * Enumeration for Code download status for the MIB "docsDevSwOperStatus" and its corresponsing OID value
 * "1.3.6.1.2.1.69.1.3.4".
 * 
 * <p>
 * The available download status
 * </p>
 * 
 * <p>
 * ,
 * </p>
 * 
 * <ul>
 * <li>"InProgress(1) indicates that a TFTP or HTTP download is underway, either as a result of a version mismatch at
 * provisioning or as a result of a upgradeFromMgt request. No other docsDevSw* objects can be modified in this
 * state.</li>
 * <li></li>
 * <li>CompleteFromProvisioning(2) indicates that the last</li>
 * <li>software upgrade was a result of version mismatch at provisioning.</li>
 * <li>CompleteFromMgt(3) indicates that the last software upgrade was a result of setting docsDevSwAdminStatus to
 * upgradeFromMgt.</li>
 * <li>Failed(4) indicates that the last attempted download failed, ordinarily due to TFTP or HTTP timeout."</li>
 * </ul>
 * 
 * @author Selvaraj Mariyappan
 * @Refactor Athira
 */
public enum CodeDownloadStatus {
    /** In progress. */
    IN_PROGRESS("inProgress"),

    /** Complete from provisioning. */
    COMPLETE_FROM_PROVISIONING("completeFromProvisioning"),

    /** Complete from management. */
    COMPLETE_FROM_MGT("completeFromMgt"),

    /** Failed. */
    FAILED("failed"),

    /** Others. */
    OTHER("other");

    private String status;

    /**
     * Enumeration constructor.
     * 
     * @param The
     *            download status
     */
    private CodeDownloadStatus(String value) {
	this.status = value;
    }

    /**
     * Method to get the download status.
     * 
     * @return The download status in the forms of string.
     */
    public String getStatus() {
	return status;
    }
}

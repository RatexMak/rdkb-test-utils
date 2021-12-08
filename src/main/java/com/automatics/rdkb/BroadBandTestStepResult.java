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
package com.automatics.rdkb;

import com.automatics.enums.ExecutionStatus;

/**
 * Represents the Test results for each test steps.
 * 
 * @author Selvaraj Mariyappan
 * @refactor Govardhan
 */
public class BroadBandTestStepResult {

	/**
	 * Device MAC Address.
	 */
	private String deviceMacAddress = null;

	/**
	 * Manual Test ID.
	 */
	private String manualTestId = null;

	/**
	 * Manual Test step number.
	 */
	private String stepNumber = null;

	/**
	 * Execution status.
	 */
	private ExecutionStatus status = null;

	/**
	 * THe Error message.
	 */
	private String errorMessage = null;

	public BroadBandTestStepResult(String macAddr, String testId, String stepNum, ExecutionStatus status,
			String errorMsg) {
		this.deviceMacAddress = macAddr;
		this.manualTestId = testId;
		this.stepNumber = stepNum;
		this.status = status;
		this.errorMessage = errorMsg;
	}

	/**
	 * @return the deviceMacAddress
	 */
	public String getDeviceMacAddress() {
		return deviceMacAddress;
	}

	/**
	 * @param deviceMacAddress
	 *            the deviceMacAddress to set
	 */
	public void setDeviceMacAddress(String deviceMacAddress) {
		this.deviceMacAddress = deviceMacAddress;
	}

	/**
	 * @return the manualTestId
	 */
	public String getManualTestId() {
		return manualTestId;
	}

	/**
	 * @param manualTestId
	 *            the manualTestId to set
	 */
	public void setManualTestId(String manualTestId) {
		this.manualTestId = manualTestId;
	}

	/**
	 * @return the stepNumber
	 */
	public String getStepNumber() {
		return stepNumber;
	}

	/**
	 * @param stepNumber
	 *            the stepNumber to set
	 */
	public void setStepNumber(String stepNumber) {
		this.stepNumber = stepNumber;
	}

	/**
	 * @return the status
	 */
	public ExecutionStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(ExecutionStatus status) {
		this.status = status;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}

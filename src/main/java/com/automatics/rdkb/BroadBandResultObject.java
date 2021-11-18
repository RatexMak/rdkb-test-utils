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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.automatics.enums.ExecutionStatus;

/**
 * Result Object is used to pass the utility or framework specific error messages to Test classes, so that we can add
 * these appropriate error messages in automation report. This is to reduce the manual intervention to identify exact
 * root cause for particular test failure.
 * 
 * @refactor Govardhan
 *
 */
public class BroadBandResultObject {

    /**
     * Validation status.
     */
    private boolean status = false;

    /**
     * Error Message if validation fails.
     */
    private String errorMessage = null;

    /**
     * Execution status.
     */
    private ExecutionStatus executionStatus = null;

    /**
     * Map with process name and its PID
     */
    private Map<String, String> processPidMap = new HashMap<String, String>();

    /**
     * @return the output
     */
    public BroadBandResultObject() {
    }

    /**
     * Constructor with parameters.
     * 
     * @param status
     *            The verification status
     * @param errorMessage
     *            The error message
     */
    public BroadBandResultObject(boolean status, String errorMessage) {
	this.status = status;
	this.errorMessage = errorMessage;
    }

    /**
     * @return the status
     */
    public boolean isStatus() {
	return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(boolean status) {
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

    /**
     * @return the execution status
     */
    public ExecutionStatus getExecutionStatus() {
	return executionStatus;
    }

    /**
     * @param executionStatus
     *            the executionStatus to set
     */
    public void setExecutionStatus(ExecutionStatus executionStatus) {
	this.executionStatus = executionStatus;
    }

    /**
     * Output
     */
    private String output = null;

    /**
     * @return the output
     */
    public String getOutput() {
	return output;
    }

    /**
     * @param status
     *            the output to set
     */
    public void setOutput(String output) {
	this.output = output;
    }

    /**
     * Execution status.
     */
    private List<String> runningProcessList = null;

    /**
     * Execution status.
     */
    private List<String> notRunningProcessList = null;

    /**
     * @return the runningProcessList
     */
    public List<String> getRunningProcessList() {
	return runningProcessList;
    }

    /**
     * @param runningProcessList
     *            the runningProcessList to set
     */
    public void setRunningProcessList(List<String> runningProcessList) {
	this.runningProcessList = runningProcessList;
    }

    /**
     * @return the notRunningProcessList
     */
    public List<String> getNotRunningProcessList() {
	return notRunningProcessList;
    }

    /**
     * @param notRunningProcessList
     *            the notRunningProcessList to set
     */
    public void setNotRunningProcessList(List<String> notRunningProcessList) {
	this.notRunningProcessList = notRunningProcessList;
    }

    /**
     * @return the processPidMap
     */
    public Map<String, String> getProcessPidMap() {
	return processPidMap;
    }

    /**
     * @param processPidMap
     *            the processPidMap to set
     */
    public void setProcessPidMap(Map<String, String> processPidMap) {
	this.processPidMap = processPidMap;
    }

}
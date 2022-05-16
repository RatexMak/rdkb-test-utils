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

package com.automatics.rdkb.utils;

/**
 * POJO Object that holds the result details.
 * 
 * @author BALAJI V
 * @refactor Said Hisham
 */
public class ResultValues {

    private boolean result = false;
    private String message = null;

    /**
     * @return the result
     */
    public boolean isResult() {
	return result;
    }

    /**
     * @param result
     *            the result to set
     */
    public void setResult(boolean result) {
	this.result = result;
    }

    /**
     * @return the message
     */
    public String getMessage() {
	return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
	this.message = message;
    }

    @Override
    /**
     * Override the toString method.
     */
    public String toString() {
	String strResultVo = this.isResult() + " - " + this.message;
	return strResultVo;
    }
}

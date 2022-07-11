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
package com.automatics.rdkb.webui.exception;

/**
 * Exception class to handle PageTitle Not Found Exception
 * 
 * @author Gnanaprakasham S
 * @refactor Govardhan
 *
 */
public class PageTitleNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 6482172192314620315L;

    public PageTitleNotFoundException() {
	super("Page Title Not Found from driver instance..!!!");
    }

    public PageTitleNotFoundException(String pageTitle) {
	super("Expected pageTitle:" + pageTitle + "driver returned Null value For pagetitle..!!!");
    }
}

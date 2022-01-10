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

package com.automatics.rdkb.utils.webpa;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Dut;
import com.automatics.tap.AutomaticsTapApi;

/**
 * Class to define Thread class to execute multiple webpa requests in parallel
 * 
 * @author asanka200
 *
 */
public class BroadBandParallelWebPaRequest implements Callable<String> {

    /**
     * Logger instance for {@link BroadBandParallelWebPaRequest}
     */
    private Logger LOGGER = LoggerFactory.getLogger(BroadBandParallelWebPaRequest.class);

    private Dut settopobj;

    private AutomaticsTapApi tapApi;

    private String webPaParameter;

    public BroadBandParallelWebPaRequest(Dut device, AutomaticsTapApi tapEnv, String parameter) {
	settopobj = device;
	tapApi = tapEnv;
	webPaParameter = parameter;
    }

    @Override
    public String call() {
	String response = null;
	try {
	    LOGGER.info("Entering thread call method");
	    response = tapApi.executeWebPaCommand(settopobj, webPaParameter);
	} catch (Exception e) {
	    LOGGER.error("Exception occured while executing webpa request in thread call method: " + e.getMessage());
	}
	return response;
    }

    @Override
    protected void finalize() throws Throwable {
	super.finalize();
	settopobj = null;
	webPaParameter = null;
	tapApi = null;
	LOGGER = null;
    }
}
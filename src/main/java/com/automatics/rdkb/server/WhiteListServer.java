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
package com.automatics.rdkb.server;

import com.automatics.rdkb.constants.RDKBTestConstants;
import com.automatics.resource.IServer;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.test.AutomaticsTestBase;
import com.automatics.utils.AutomaticsPropertyUtility;

public class WhiteListServer extends AutomaticsTestBase implements IServer{ 
    /** WhiteListServer server object. */
    public static WhiteListServer wlServer = null;
    
    /** Host IP address. */
    private String hostIp = null;

    /** WhiteListServer password. */
    private String password = null;

    /** WhiteListServer user ID. */
    private String userId = null;
    
    /** Sets hostIP, username and password
     * 
     * @param tapEnv
     *            TAP environment object.
     * @param jumpServerIp
     * 	          Specific jump server ip which will be used to create IServer instance
     */
    public WhiteListServer(AutomaticsTapApi tapEnv, String jumpServerIp) {
	hostIp =jumpServerIp;
	password = AutomaticsPropertyUtility.getProperty(RDKBTestConstants.PROP_KEY_CI_SERVER_PASSWORD);
	userId = AutomaticsPropertyUtility.getProperty(RDKBTestConstants.PROP_KEY_CI_SERVER_USER_ID);
    }
    
    /**
     * Provides the instance of the server object.
     * 
     * @param tapEnv
     *            TAP environment object.
     * @param jumpServerIp
     * 	          Specific jump server ip which will be used to create IServer instance
     * @return the instance of the server.
     */
    public static final synchronized WhiteListServer getInstance(AutomaticsTapApi tapEnv, String jumpServerIp) {
	wlServer = new WhiteListServer(tapEnv, jumpServerIp);
	return wlServer;
    }
    
    /**
     * Provides the host IP of the server.
     *
     * @return  the host IP.
     */
    @Override
    public String getHostIp() {
	return hostIp;
    }

    /**
     * Provides the password for establishing the connection.
     *
     * @return  the password of server.
     */
    @Override
    public String getPassword() {
	return password;
    }

    /**
     * Provides the user ID for establishing the connection.
     *
     * @return  the user ID.
     */
    @Override
    public String getUserId() {
	return userId;
    }
}

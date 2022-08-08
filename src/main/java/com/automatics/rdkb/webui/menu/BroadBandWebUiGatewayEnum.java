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
package com.automatics.rdkb.webui.menu;

/**
 * Enums to hold the sub menu for Gateway Menu in Broad band Web UI
 * 
 * @author Gnanaprakasham.s
 * 
 * @Refactor Sruthi Santhosh
 */

public enum BroadBandWebUiGatewayEnum {

	AT_A_GLANCE("At a Glance"), INITIAL_SETUP("Initial Setup"), CONNECTION("Connection"),
	CONNECTED_DEVICES("Connected Devices"), FIREWALL("Firewall"), SOFTWARE("Software"), HARDWARE("Hardware"),
	WIZARD("Wizard");

	private String command;

	private BroadBandWebUiGatewayEnum(String command) {

		this.command = command;
	}

	public String getCommand() {
		return this.command;
	}

}
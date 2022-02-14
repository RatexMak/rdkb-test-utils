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
package com.automatics.rdkb.utils.tr69;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.core.SupportedModelHandler;
import com.automatics.device.Dut;
import com.automatics.exceptions.TestException;
import com.automatics.providers.tr69.Parameter;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.CommonMethods;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.automatics.providers.tr69.TR69Provider;
import com.automatics.rdkb.constants.BroadBandTestConstants;
import com.automatics.rdkb.constants.TR69ParamConstants;

public class Tr69RestAPIUtils {
	
	/**
	* Jersey client connection timeout.
	*/
	private static final int CONNECTION_TIMEOUT = 30000;
	
	/**
	* enum to store the type of TR69 request
	*/
	private enum Tr69Type {
	GET,
	SET,
	DOWNLOAD,
	PATH
	}
	
	/**
	* Jersey client read timeout.
	*/
	private static final int READ_TIMEOUT = 60000;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BroadBandTr69Utils.class);
	/**
	* Utility method to set TR69 parameter values using Telescope Rest API
	*
	* @param device
	* The device to be validated.
	* @param parameters
	* The parameter list.
	* @return TR69 Parameter response.
	* @throws JSONException
	*/
	public static String setTR69ParameterValueUsingRestApi(AutomaticsTapApi tapEnv, Dut device,
	List<Parameter> parameters) {
	String responseParams = null;
	ResteasyClient client = null;
	Response response = null;
	String errorMessage = null;
	try {
	getDeviceManagementServerSettings(device, tapEnv);
	ParameterList parentList = new ParameterList();
	parentList.setList(parameters);
	String json = new Gson().toJson(parentList);

	
	ApacheHttpClient4Engine httpEngine = new ApacheHttpClient4Engine(CommonMethods.getTlsEnabledHttpClient());
	client = new ResteasyClientBuilder().httpEngine(httpEngine)
	.establishConnectionTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
	.socketTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS).build();


	String completeUrl = getFormattedTelescopeRestApiUrl(tapEnv, device, null, Tr69Type.SET);
	ResteasyWebTarget target = client.target(completeUrl);

	LOGGER.info("Posting to telescopic service url - " + completeUrl + " with data - " + json + "");

	// executing post request
	response = target.request().post(Entity.entity(json, MediaType.APPLICATION_JSON));

	if (null != response && HttpStatus.SC_OK == response.getStatus()) {
	String responseValue = response.readEntity(String.class);
	LOGGER.info("Posting - " + completeUrl + " : " + json + " : " + responseValue);
	responseParams = new JSONObject(responseValue).getString("status");
	} else {
	// Failed to set TR69 param properly
	errorMessage = "FAILED : SETTING TR69 PARAM:" + parameters.get(0) + " : "
	+ response.readEntity(String.class);
	LOGGER.error(errorMessage);
	throw new TestException(errorMessage);
	}
	} catch (Exception e) {
	errorMessage = e.getMessage();
	LOGGER.error("EXCEPTION : FETCHING TR69 PARAM: " + parameters.get(0) + " : " + e.getMessage());
	throw new TestException(errorMessage);
	} finally {
	if (null != client) {
	client.close();
	}
	if (null != response) {
	response.close();
	}
	}
	return responseParams;
	}
	
	/**
	* Helper method to format the telescope Rest API URL
	*
	* @param device
	* The device to be used.
	* @param parameters
	* The parameter list.
	* @param isGet
	* The flag to check whether get or set operation.
	* @return The formatted telescope rest API.
	*/
	
	private static String getFormattedTelescopeRestApiUrl(AutomaticsTapApi tapEnv, Dut device, String[] parameters,
	Tr69Type type) {
	LOGGER.debug("STARTING METHOD : getFormattedTelescopeRestApiUrl");
	String serialNumber=device.getSerialNumber();
	if (CommonMethods.isNull(serialNumber) || device.getSerialNumber().equals("NA")) {
	    serialNumber = tapEnv.executeWebPaCommand(device, TR69ParamConstants.TR69_PARAM_SERIAL_NUMBER);
	}
	StringBuffer completeUrl = new StringBuffer();
	//String restUrl = tapEnv.getCatsPropsValue(TELESCOPIC_REST_API_URL);
	String restUrl =AutomaticsPropertyUtility
		    .getProperty(BroadBandTestConstants.TELESCOPIC_REST_API_URL);
	completeUrl = completeUrl.append(restUrl).append(device.getModel()).append("/").append(serialNumber)
	.append("/");
	if (type == Tr69Type.GET) {
	completeUrl.append("names=");
	for (String param : parameters) {
	completeUrl.append(param).append(",");
	}
	completeUrl.deleteCharAt(completeUrl.length() - 1);
	} else if (type == Tr69Type.DOWNLOAD) {
	completeUrl.append("download");
	} else if (type == Tr69Type.PATH) {
	if (null != parameters && parameters.length == 1) {
	completeUrl.append("path=").append(parameters[0]);
	}
	}
	LOGGER.info("complete url for " + type + " " + completeUrl.toString());
	LOGGER.debug("ENDING METHOD : getFormattedTelescopeRestApiUrl");
	return completeUrl.toString();
	}
	
	/**
	* Utility method to get ACS server configuration details
	*
	* @param device
	* The device to be validated.
	* @param tapEnv
	* The {@link AutomaticsTapApi} reference.
	*/
	public static void getDeviceManagementServerSettings(Dut device, AutomaticsTapApi tapEnv) {
	// Device.ManagementServer. parameter returns 0 parameter count and there is no message key in response for rdkv
	// devices. Getting exception.
	if (!SupportedModelHandler.isRDKV(device)) {
	CommonMethods.executeWebPA(device, BroadBandTestConstants.WEBPA_PARAM_DEVICE_MANAGEMENTSERVER);

	String response = tapEnv.executeCommandUsingSsh(device, "uptime");
	LOGGER.info("Device uptime :" + response);
	}
	}
	
	  /**
	     * Utility method to get TR69 parameter values using Telescope Rest API
	     * 
	     * @param dut
	     *            The dut to be validated.
	     * @param parameters
	     *            The parameter list.
	     * @param tapEnv
	     *            TODO
	     * @return TR69 Parameter response.
	     */
	    public static List<Parameter> getTR69ParameterValueUsingRestApi(AutomaticsTapApi tapEnv, Dut dut, String[] parameters) {
		ParameterList acsResponse = null;
		Response response = null;
		try {
		    getDeviceManagementServerSettings(dut, tapEnv);
		    String completeUrl = getFormattedTelescopeRestApiUrl(tapEnv, dut, parameters, Tr69Type.GET);
		    LOGGER.info("TELESCOPIC SERVICE REST URL = " + completeUrl);
		    // Break the connection after 60 seconds in case of failure, otherwise client
		    // will wait indefinitely.

		    ApacheHttpClient4Engine httpEngine = new ApacheHttpClient4Engine(CommonMethods.getTlsEnabledHttpClient());
		    ResteasyClient client = new ResteasyClientBuilder().httpEngine(httpEngine).establishConnectionTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
			    .socketTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS).build();

		    ResteasyWebTarget target = client.target(completeUrl);
		    response = target.request().get();

		    LOGGER.info("Posting  - " + completeUrl + " : Response" + response.getStatus());
		    if (HttpStatus.SC_OK == response.getStatus()) {
			acsResponse = response.readEntity(ParameterList.class);
		    } else {
			// Failed to fetch TR69 param properly
			String errorMessage = "FAILED : FETCHING TR69 PARAM:" + parameters[0] + " : " + response.readEntity(String.class);
			LOGGER.error(errorMessage);
			throw new TestException(errorMessage);
		    }
		} catch (Exception e) {
		    LOGGER.error("EXCEPTION : FETCHING TR69 PARAM: " + parameters[0] + " : " + e.getMessage());
		    throw new TestException(e.getMessage());
		} finally {
		    if (null != response) {
			response.close();
		    }
		}
		return acsResponse.getList();
	    }

}

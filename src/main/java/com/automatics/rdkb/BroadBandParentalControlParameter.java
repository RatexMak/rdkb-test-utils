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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.automatics.error.GeneralError;
import com.automatics.exceptions.FailedTransitionException;

/**
 * pojo class created to set a parental control rule.
 * 
 * To set a parental control rule we have to make a web pa request with JSON string containing the parameters
 * BlockMethod,Site, AlwaysBlock,StartTime,EndTime,BlockDays
 * 
 * @author revanth.k
 * */
public class BroadBandParentalControlParameter {

    // variable to store the type of Blocking whether using a URL or Keyword
    private String BlockMethod;
    // variable to store the name of the site that has to be blocked
    private String Site;
    // variable to tell whether to block the url always or for a specific period
    private String AlwaysBlock;
    // variable to store the start time form which URL has to be blocked
    private String StartTime;
    // variable to store the end time form which URL has to be blocked
    private String EndTime;
    // variable to store the no.of days url has to be blocked
    private String BlockDays;

    public String getBlockMethod() {
	return BlockMethod;
    }

    public void setBlockMethod(String blockMethod) {
	BlockMethod = blockMethod;
    }

    public String getSite() {
	return Site;
    }

    public void setSite(String site) {
	Site = site;
    }

    public String getAlwaysBlock() {
	return AlwaysBlock;
    }

    public void setAlwaysBlock(String alwaysBlock) {
	AlwaysBlock = alwaysBlock;
    }

    public String getStartTime() {
	return StartTime;
    }

    public void setStartTime(String startTime) {
	StartTime = startTime;
    }

    public String getEndTime() {
	return EndTime;
    }

    public void setEndTime(String endTime) {
	EndTime = endTime;
    }

    public String getBlockDays() {
	return BlockDays;
    }

    public void setBlockDays(String blockDays) {
	BlockDays = blockDays;
    }

    public JSONObject toJson() {
	JSONObject parentalControlwebpaParameter = new JSONObject();
	try {
	    parentalControlwebpaParameter.put("BlockMethod", BlockMethod);
	    parentalControlwebpaParameter.put("Site", Site);
	    parentalControlwebpaParameter.put("AlwaysBlock", AlwaysBlock);
	    parentalControlwebpaParameter.put("StartTime", StartTime);
	    parentalControlwebpaParameter.put("EndTime", EndTime);
	    parentalControlwebpaParameter.put("BlockDays", BlockDays);
	} catch (JSONException jex) {
	    throw new FailedTransitionException(GeneralError.INCORRECT_JSON, jex);
	}
	return parentalControlwebpaParameter;
    }

    public BroadBandParentalControlParameter(String BlockMethod, String Site, String AlwaysBlock, String StartTime,
	    String EndTime, String BlockDays) {
	this.BlockMethod = BlockMethod;
	this.Site = Site;
	this.AlwaysBlock = AlwaysBlock;
	this.StartTime = StartTime;
	this.EndTime = EndTime;
	this.BlockDays = BlockDays;
    }

}

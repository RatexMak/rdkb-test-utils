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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.constants.AutomaticsConstants;
import com.automatics.error.GeneralError;
import com.automatics.exceptions.FailedTransitionException;
import com.automatics.providers.connection.SshConnection;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.AutomaticsUtils;
import com.automatics.utils.CommonMethods;
import com.automatics.rdkb.constants.BroadBandTestConstants;

public class ServerUtils {
	
    private static final int SSH_CONNECTION_MAX_ATTEMPT = 4;
	/** SLF4j Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerUtils.class);

    /**
     * Method establish a SSH connection to the host using the user name.
     *
     * @param   userName  The SSH user name.
     * @param   hostIp    The host name to which connection to be established.
     *
     * @return  The SSH connection.
     */
    public static SshConnection getSshConnection(String userName, String hostIp) {       
    	
    	SshConnection connection = null;

    	String randomJumpServer = null;
    	String sshFailureMesaage = "";
    	LOGGER.debug("SSH Host IP : " + hostIp);

    	for (int retryCount = 1; retryCount <= SSH_CONNECTION_MAX_ATTEMPT ; retryCount++) {
    		try {
    			LOGGER.debug("SSH connection attempet : " + retryCount );
    			connection = new SshConnection(userName, hostIp);
    		} catch (Exception e) {

    			// Trying once more
    			LOGGER.info("SSH connection attempet : " + retryCount + " failed due to " + e.getMessage() + " for "+hostIp+". Trying once more..");
    			sshFailureMesaage = e.getMessage();
    			connection = null;

    			if(retryCount != SSH_CONNECTION_MAX_ATTEMPT) {
    				//Get the random Jump server
    				randomJumpServer = getRandomJumpServer(hostIp);

    				if(CommonMethods.isNotNull(randomJumpServer)) {
    					hostIp = randomJumpServer;
    				} 

    				AutomaticsUtils.sleep(AutomaticsConstants.TEN_SECONDS);
    			}

    		}

    		if(null != connection) {
    			break;
    		} 
    	}

    	if (null == connection) {
    		throw new FailedTransitionException(GeneralError.SSH_CONNECTION_FAILURE, sshFailureMesaage);
    	}

        return connection;
    }
    
    /**
     * Method establish a SSH connection to the host using the default user name defined in
     * configuration file.
     *
     * @param   hostIp  The host name to which connection to be established.
     *
     * @return  The SSH connection.
     */
    public static SshConnection getSshConnection(String hostIp) {       
    	
    	SshConnection connection = null;

    	String randomJumpServer = null;
    	String sshFailureMesaage = "";
    	
		// We don't log if the current thread is PollingThread
		if (!Thread.currentThread().getName().equalsIgnoreCase("PollingThread")) {
			LOGGER.info("SSH Host IP : " + hostIp);
		}

    	for (int retryCount = 1; retryCount <= SSH_CONNECTION_MAX_ATTEMPT ; retryCount++) {
    		try {
    			LOGGER.debug("SSh connection attempet : " + retryCount );
    			connection = new SshConnection(hostIp);
    		} catch (Exception e) {

    			// Trying once more
    			LOGGER.info("SSh connection attempet : " + retryCount + " failed due to " + e.getMessage() + " for "+hostIp+". Trying once more..");
    			sshFailureMesaage = e.getMessage();
    			connection = null;

    			if(retryCount != SSH_CONNECTION_MAX_ATTEMPT) {
    				//Get the random Jump server
    				randomJumpServer = getRandomJumpServer(hostIp);

    				if(CommonMethods.isNotNull(randomJumpServer)) {
    					hostIp = randomJumpServer;
    				} 

    				AutomaticsUtils.sleep(AutomaticsConstants.TEN_SECONDS);
    			}

    		}

    		if(null != connection) {
    			break;
    		} 
    	}

    	if (null == connection) {
    		throw new FailedTransitionException(GeneralError.SSH_CONNECTION_FAILURE, sshFailureMesaage);
    	}

        return connection;
    }
    /**
     * Method to get a random Jump server, as the current connection is throwing error
     * 
     * @return randomJumpServer
     */
    private static String getRandomJumpServer() {
        
        String randomJumpServer = null;
        String awsJumpServers = null;
        
        int randomNumber = 0;

	if (System.getProperty(AutomaticsConstants.PROPERTY_IS_PROD_BUILD_TEST, "N").equalsIgnoreCase("Y")) {
	    awsJumpServers = AutomaticsPropertyUtility
		    .getProperty(BroadBandTestConstants.PROP_KEY_JUMP_SERVER_IP_ADDRESS_LIST_PROD_TESTS);

	} else if (System.getProperty("isAWS", "N").equalsIgnoreCase("Y")) {
	    awsJumpServers = AutomaticsPropertyUtility.getProperty("new.aws.vm.ip.address");
	} else {
	    awsJumpServers = AutomaticsPropertyUtility.getProperty("aws.vm.ip.address");
	}
	awsJumpServers = getAWSJumpServers();
        if(CommonMethods.isNotNull(awsJumpServers)) {
            randomNumber = (int ) (Math.random() * awsJumpServers.split(",").length);
            randomJumpServer = awsJumpServers.split(",")[randomNumber];
            
            LOGGER.info("RANDOM JUMP SERVER OBTAINED AS - "+randomJumpServer);
        }      
        
        return randomJumpServer;
    }
    
   
    /**
     * Method to get a random Jump server, as the current connection is throwing error
     * 
     * @return randomJumpServer
     */
    private static String getRandomJumpServer(String previousSelectedIp) {

    	String randomJumpServer = null;
    	String awsJumpServers = null;
    	String[] jumpServersSplited = null;

    	int randomNumber = 0;
    	
    	
    	if(isSNMPHost(previousSelectedIp)){
    		awsJumpServers = getSNMPAWSJumpServers();
    	}
    	else {
    		awsJumpServers = getAWSJumpServers();
    	}    		
    	
    	if(CommonMethods.isNotNull(awsJumpServers)) {
    		jumpServersSplited = awsJumpServers.split(",");
    		
    		if (jumpServersSplited.length != 1) {
    			for (int i =1; i<=5 ; i++) {
    				randomNumber = (int ) (Math.random() * jumpServersSplited.length);
    				randomJumpServer = jumpServersSplited[randomNumber];
		    LOGGER.debug(i + ". RANDOM JUMP SERVER OBTAINED AS - " + randomJumpServer
			    + " Checking if its previously used - " + previousSelectedIp);
    				if(!randomJumpServer.equalsIgnoreCase(previousSelectedIp)) {
			LOGGER.debug(i + ". Obtained non matching jump server - " + randomJumpServer);
    					break; 
    				} else {
			LOGGER.debug(i + ". Random server obtained is same as previously used. Taking new one");
    				}
    			}
    		} else {
    			randomJumpServer = jumpServersSplited[0];
    		}
    	}
	LOGGER.debug("FINAL RANDOM JUMP SERVER OBTAINED AS - " + randomJumpServer);

    	return randomJumpServer;
    }
    
    /**
     * Thsi method fetched for the property new.aws.vm.ip.address/aws.vm.ip.address , and returns the result. This will
     * be the list of jump servers, one of which may be selected during course of execution to run ssh/scp utilities.
     * 
     * @return String which has list of jumpservers separated by comma
     */
    public static String getAWSJumpServers() {
	String awsJumpServers = null;
	if (System.getProperty(AutomaticsConstants.PROPERTY_IS_PROD_BUILD_TEST, "N").equalsIgnoreCase("Y")) {
	    awsJumpServers = AutomaticsPropertyUtility
		    .getProperty(BroadBandTestConstants.PROP_KEY_JUMP_SERVER_IP_ADDRESS_LIST_PROD_TESTS);

	} else if (System.getProperty("isAWS", "N").equalsIgnoreCase("Y")) {
	    awsJumpServers = AutomaticsPropertyUtility.getProperty("new.aws.vm.ip.address");
	} else {
	    awsJumpServers = AutomaticsPropertyUtility.getProperty("aws.vm.ip.address");
	}
	LOGGER.debug("Jumpservers = " + awsJumpServers);
	return awsJumpServers;
    }    
    
    /**
     * Thsi method fetched for the property snmp.host.vm.ip.backup , and returns the result. This will
     * be the list of jump servers, one of which may be selected during course of execution to run ssh/scp utilities.
     * 
     * @return String which has list of jumpservers separated by comma
     */
    public static String getSNMPAWSJumpServers() {
	String snmpawsJumpServers = null;
	
	snmpawsJumpServers = AutomaticsPropertyUtility.getProperty("snmp.host.vm.ip.backup");
	
	LOGGER.debug("snmp Jumpservers = " + snmpawsJumpServers);
	return snmpawsJumpServers;
    }
    
    /**
     * Method to check the given ip is a snmp server host 
     * 
     * @return true/false
     */
    
    private static boolean isSNMPHost(String selectedIp){
    	
    	String snmpv2Host=AutomaticsPropertyUtility.getProperty("snmp.host.vm.ip");
    	String snmpv3Host=AutomaticsPropertyUtility.getProperty("snmp.v3.host.vm.ip");
    	    	
    	if(CommonMethods.isNotNull(snmpv2Host)&&CommonMethods.isNotNull(snmpv3Host)){
    		
    	if(selectedIp.equals(snmpv2Host)||selectedIp.equals(snmpv3Host)){
    		return true;
    	}
    	
    	}
    	LOGGER.info("isSNMPHost: false");	
    	return false;
    }
    
    /**
     * Closes the secure shell connection.
     *
     * @param connection
     *            The SSH connection object.
     */
    public static final void closeSshConnection(SshConnection connection) {

	if (null != connection) {
	    connection.disconnect();
	    LOGGER.debug("closeSshConnection(): " + connection.getHostName());
	}
    }
}

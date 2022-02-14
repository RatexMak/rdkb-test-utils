package com.automatics.rdkb.utils;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.connection.handler.SplunkConnectionHandler;
import com.automatics.exceptions.SplunkConnectionFailedException;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.CommonMethods;

public class SplunkUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SplunkUtils.class);


	 /**
     * Helper method that searches the <code>searchString</code> within splunk. Returns the search results as a
     * Collection.
     * 
     * @param tapApi
     *            - instance of AutomaticsTapApi
     * 
     * @param searchString
     *            - value to be searched in Splunk
     * @param host
     *            - splunk host IP, if nothing mentioned it will choose default one configured
     * @param port
     *            - splunk port, if nothing mentioned it will choose default one configured
     * @param startIndex
     *            - start time from which search should begin
     * @return search results
     * 
     */
    public static Collection<String> searchInSplunk(AutomaticsTapApi tapApi, String searchString, String host, int port,
	    String startIndex) {
	LOGGER.info("STARTING METHOD: SplunkUtils.searchInSplunk");

	Collection<String> searchResults = searchInSplunk(tapApi, searchString, host, port, startIndex, null);
	LOGGER.info("ENDING METHOD: SplunkUtils.searchInSplunk");
	return searchResults;
    }
    
    /**
     * Helper method that searches the <code>searchString</code> within splunk. Returns the search results as a
     * Collection.
     * 
     * @param tapApi
     *            - instance of AutomaticsTapApi
     * 
     * @param searchString
     *            - value to be searched in Splunk
     * @param host
     *            - splunk host IP, if nothing mentioned it will choose default one configured
     * @param port
     *            - splunk port, if nothing mentioned it will choose default one configured
     * @param startIndex
     *            - start time from which search should begin
     * @param schema
     *            - Schema to be used for connection
     * @return search results
     */
    public static Collection<String> searchInSplunk(AutomaticsTapApi tapApi, String searchString, String host, int port,
			String startIndex, String schema) {
		LOGGER.info("STARTING METHOD: SplunkUtils.searchInSplunk");

		Collection<String> searchResults = null;
		if (null == host) {
			// If host is not mentioned, go with default Splunk server
			
			host = BroadbandPropertyFileHandler.getSplunkHost();
		}
		if (port == -1) {
			// If port is not mentioned, go with default Splunk server
			
			port = BroadbandPropertyFileHandler.getSplunkPort();
		}
		
		String userName = BroadbandPropertyFileHandler.getSplunkUser();
		String password = BroadbandPropertyFileHandler.getSplunkPwd();
		
		LOGGER.info("Connecting to Splunk : " + host + " Port: " + port);
		SplunkConnectionHandler splunkConnectionHandler = null;
		String startTimeInQuery = " earliest=-4h";
		if (null != startIndex) {
			startTimeInQuery = " earliest=-" + startIndex;
		}
		try {
			// get currentTime
			long timeBeforeLogin = System.currentTimeMillis();
			if (CommonMethods.isNull(schema)) {
				splunkConnectionHandler = SplunkConnectionHandler.getInstance(host, port, userName, password);
			} else {
				splunkConnectionHandler = SplunkConnectionHandler.getInstance(host, port, userName, password, schema);
			}
			if (!searchString.contains("index")) {
				searchString = searchString + " index=* ";
			}
			long timeAfterSplunkLogin = System.currentTimeMillis();
			// Splunk login is taking more than 3 mins which is causing failure in certain
			// cases where each min matters eg.DVR
			try {
				long totalTime = timeAfterSplunkLogin - timeBeforeLogin;
				int timeDiff = (int) TimeUnit.MILLISECONDS.toMinutes(totalTime); // Convert difference in minutes
				String timeDiffInString = Integer.toString(timeDiff);
				if (timeDiff > 1 && CommonMethods.isNotNull(timeDiffInString)) {
					startTimeInQuery = startTimeInQuery + "-" + timeDiffInString + "m";
				}
			} catch (Exception e) {
				// Do nothing to the startTimeInQuery if exception occurs
			}

			String searchQuery = searchString + startTimeInQuery + " latest=now()";
			LOGGER.info("Starting to seach Splunk with query : " + searchQuery);
			LOGGER.info("About to execute query : " + searchQuery);
			splunkConnectionHandler.executeQuery(searchQuery);
			LOGGER.info("search query executed!!");
			searchResults = splunkConnectionHandler.getSearchResults();
			LOGGER.info(" - COLLECTION OF RESULTS STRINGS - " + searchResults);

			if (null != searchResults && !searchResults.isEmpty()) {
				for (String resultString : searchResults) {
					LOGGER.info("Result: " + resultString);
				}
			}

		} catch (SplunkConnectionFailedException e) {
			LOGGER.error("SplunkUtils.searchInSplunk : Failed to obtain Splunk connectivity. - " + e.getMessage());
		} catch (Exception e) {
			LOGGER.error("SplunkUtils.searchInSplunk : Splunk Search failed. - " + e.getMessage());
		} finally {
			if (splunkConnectionHandler != null) {
				splunkConnectionHandler.logout();
			}
		}
		LOGGER.info("ENDING METHOD: SplunkUtils.searchInSplunk");
		return searchResults;
	}
}

package com.automatics.rdkb.wt.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.apache.http.HttpEntity;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automatics.device.Dut;
import com.automatics.rdkb.BroadBandResultObject;
import com.automatics.rdkb.constants.BroadBandPropertyKeyConstants;
import com.automatics.rdkb.constants.RDKBTestConstants.WiFiFrequencyBand;
import com.automatics.rdkb.utils.BroadBandRestoreWifiUtils;
import com.automatics.rdkb.wt.object.Station;
import com.automatics.rdkb.wt.exception.WtResponseException;
import com.automatics.tap.AutomaticsTapApi;
import com.automatics.utils.AutomaticsPropertyUtility;
import com.automatics.utils.CommonMethods;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.automatics.rdkb.utils.dmcli.DmcliUtils;

public class WtClientUtils {

	/**
	 * Logger instance for {@link WtClientUtils}
	 */
	protected static final Logger LOGGER = LoggerFactory.getLogger(WtClientUtils.class);

	/**
	 * Helper method to create new stations
	 * 
	 * @param ssid
	 * @param passphrase
	 * @param url
	 * @param wifiBand
	 * @param numberOfStations
	 * @return
	 */
	public static boolean createNewStations(String ssid, String passphrase, String url, WiFiFrequencyBand wifiBand,
			int numberOfStations, String wtSimulatorBaseUrl) {
		LOGGER.info("STARTING METHOD : createNewStations");
		int stationNum = WiFiFrequencyBand.WIFI_BAND_2_GHZ.equals(wifiBand) ? 1210 : 1500;
		numberOfStations = stationNum + numberOfStations;
		BroadBandResultObject result = null;
		Station station = null;
		List<String> nonConnectedStations = new ArrayList<String>();
		for (int counter = stationNum; counter <= numberOfStations; counter++) {

			station = new Station();
			station.setwtSimulatorBaseUrl(wtSimulatorBaseUrl);
			station.setAlias("sta" + (counter + 1));
			station.setMode(WiFiFrequencyBand.WIFI_BAND_2_GHZ.equals(wifiBand) ? 6 : 9);
			station.setSsid(ssid);
			station.setPsk(passphrase);
			// station.setStatus("enabled");
			station.setRadio(WiFiFrequencyBand.WIFI_BAND_2_GHZ.equals(wifiBand) ? "wiphy0" : "wiphy1");
			station.setEthernet_interface("eth1");
			result = createClientUsingWtApi(station, url);
			if (!result.isStatus()) {
				nonConnectedStations.add(station.getAlias() + ",");
			}
		}
		boolean status = nonConnectedStations.isEmpty();
		if (!status) {
			LOGGER.error("Failed to create station" + nonConnectedStations.toString());
		}
		LOGGER.info("ENDING METHOD : createNewStations");
		return status;
	}

	public static BroadBandResultObject createClientUsingWtApi(Station station, String url) {
		LOGGER.info("STARTING METHOD : createClientUsingWisstApi");
		BroadBandResultObject result = new BroadBandResultObject();
		try {

			JSONObject param = new JSONObject();
			param.put("alias", station.getAlias());
			param.put("ssid", station.getSsid());
			param.put("psk", station.getPsk());
			param.put("radio", station.getRadio());
			param.put("mode", station.getMode());
			param.put("ethernet_interface", station.getEthernet_interface());
			LOGGER.info("JSON OBJECT - " + param.toString());
			URL obj = new URL(url);
			HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setRequestProperty("Content-Type", "application/json");
			postConnection.setDoOutput(true);
			OutputStream os = postConnection.getOutputStream();
			os.write(param.toString().getBytes());
			os.flush();
			os.close();
			int responseCode = postConnection.getResponseCode();
			String status = postConnection.getResponseMessage();
			LOGGER.info("POST Response Code :  " + responseCode);
			LOGGER.info("POST Response Message : " + postConnection.getResponseMessage());

			JSONObject objectName = new JSONObject(status);
			status = objectName.getString("status");

			result.setStatus(responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK
					|| status == "SUCCESS");

		} catch (Exception exception) {
			LOGGER.error("Exception occurred While connecting to Wifi Band. Error Message - " + exception.getMessage());
		}
		LOGGER.info("ENDING METHOD : createClientUsingWisstApi");
		return result;
	}

	public static boolean validateResponse(Response response) {

		boolean status = false;

		LOGGER.info("RESPONSE FROM WISST SERVER: " + response);
		if (null != response) {
			if (response.getStatus() == 200) {
				status = true;
			}
		}

		return status;
	}

	public static BroadBandResultObject verifyDevicesAreConnectedToGateway(Dut device, AutomaticsTapApi tapEnv,
			Station station, boolean testConnected) {
		BroadBandResultObject result = new BroadBandResultObject();
		String deviceMac = null;
		String response = null;
		StringBuffer errorMessage = new StringBuffer();
		String activeStatus = testConnected ? "true" : "false";
		deviceMac = station.getMac();
		response = tapEnv.executeCommandUsingSsh(device, "dmcli eRT getv Device.Hosts.Host. | grep -iB2 " + deviceMac
				+ " | grep -iv " + deviceMac + " | grep -i \\.PhysAddress");
		if (CommonMethods.isNotNull(response)) {
			response = CommonMethods.patternFinder(response, "Device.Hosts.Host.(\\d+).PhysAddress");
			if (CommonMethods.isNotNull(response)) {
				response = DmcliUtils.getParameterValueUsingDmcliCommand(device, tapEnv,
						"Device.Hosts.Host." + response + ".Active");
				if (!(CommonMethods.isNotNull(response) && response.contains(activeStatus))) {
					errorMessage.append(
							" Device " + deviceMac + " is not Active. RESPONSE from Dmcli command is " + response);
				}
			}
		} else {
			errorMessage.append(" Failed to find device Mac from Device.Hosts.Host.. Response is -" + response);
		}
		result.setStatus(CommonMethods.isNull(errorMessage.toString()));
		result.setErrorMessage(errorMessage.toString());
		return result;
	}

	public static List<Station> getAllStationsDetails(String wtSimulatorBaseUrl, String url)
			throws WtResponseException {

		LOGGER.info("************* Fetching stations detail *************");

		String responseJson = null;
		Response response = null;
		ObjectMapper mapper = null;
		Station[] stations = null;
		List<Station> list = null;

		try {
			LOGGER.info("CONTACTING WISST SERVER");

			JSONObject param = new JSONObject();
			//param.put("wtSimulatorBaseUrl", wtSimulatorBaseUrl);
			param.put("wtSimulatorBaseUrl","http://localhost:8081/test");
			LOGGER.info("JSON OBJECT - " + param.toString());
			URL obj = new URL(url);
			LOGGER.info("obj - " + obj);
			HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setRequestProperty("Content-Type", "application/json");
			postConnection.setDoOutput(true);
			OutputStream os = postConnection.getOutputStream();
			os.write(param.toString().getBytes());
			os.flush();
			os.close();
			int responseCode = postConnection.getResponseCode();
			String status = postConnection.getResponseMessage();
			LOGGER.info("POST Response Code :  " + responseCode);
			LOGGER.info("POST Response Message : " + postConnection.getResponseMessage());

			JSONObject objectName = new JSONObject(status);
			status = objectName.getString("status");
			

			ResteasyClient client = getClient();
			ResteasyWebTarget target = client.target(url);

			response = target.request().post(Entity.entity("Content-Type", "application/json"));

			if (null != response) {
				if (response.getStatus() == 200) {
					String respData = response.readEntity(String.class);
					LOGGER.info("Response: {}", respData);

					if (null != respData && !respData.isEmpty()) {
						mapper = new ObjectMapper();
						try {
							stations = mapper.readValue(responseJson, Station[].class);
							for (Station station : stations) {
								if (null == station.getStatus()) {
									station.setStatus(station.getStatusOld());
								}
								if (null == station.getConnectionStatus()) {
									station.setConnectionStatus(station.getConnectionStatusOld());
								}
							}

							list = Arrays.asList(stations);

						} catch (JsonProcessingException e) {
							LOGGER.error("Exception parsing json for device lock {}", "Content-Type", e);
						} catch (IOException e) {
							LOGGER.error("Exception parsing json for device lock {}", "Content-Type", e);
						}
					} else {
						LOGGER.info("Failed to get Station or no Station created");
					}
				} else {
					LOGGER.info("Failed to lock device {} : Status: {}", "Content-Type", response.getStatus());
				}

			}

		} catch (Exception e) {
			LOGGER.error("Exception from WiSST client - ", e);
			throw new WtResponseException(e.getMessage());
		} finally {
			if (null != response) {
				response.close();
			}
		}

		LOGGER.info("Response station list - " + stations.toString());

		LOGGER.info("************* List of stations obtained *************");
		LOGGER.info("Size of Stations - " + list.size());
		return list;

	}

	public static String deleteStation(String stationId, String wtSimulatorBaseUrl, String url)
			throws WtResponseException {
		LOGGER.info("************* Started deleting a station *************");

		String responseJson = null;
		Station[] stations = null;
		Response response = null;
		BroadBandResultObject result = new BroadBandResultObject();

		try {
			LOGGER.info("CONTACTING WISST SERVER");

			JSONObject param = new JSONObject();
			param.put("wtSimulatorBaseUrl", wtSimulatorBaseUrl);
			param.put("stationId", stationId);
			LOGGER.info("JSON OBJECT - " + param.toString());
			URL obj = new URL(url);
			HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
			postConnection.setRequestMethod("POST");
			postConnection.setRequestProperty("Content-Type", "application/json");
			postConnection.setDoOutput(true);
			OutputStream os = postConnection.getOutputStream();
			os.write(param.toString().getBytes());
			os.flush();
			os.close();
			int responseCode = postConnection.getResponseCode();
			String status = postConnection.getResponseMessage();
			LOGGER.info("POST Response Code :  " + responseCode);
			LOGGER.info("POST Response Message : " + postConnection.getResponseMessage());

			JSONObject objectName = new JSONObject(status);
			status = objectName.getString("status");

			result.setStatus(responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK
					|| status == "SUCCESS");

			LOGGER.info("Response from delete station - " + status);
		} catch (Exception e) {
			LOGGER.error("Exception from WiSST client - ", e);
			throw new WtResponseException(e.getMessage());
		} finally {
			if (null != response) {
				response.close();
			}
		}

		LOGGER.info("************* Deleted station *************");

		return responseJson;
	}

	/**
	 * Gets rest easy client instance
	 * 
	 * @return ResteasyClient
	 */
	private static ResteasyClient getClient() {
		ResteasyClient client = new ResteasyClientBuilder().build();
		return client;
	}
}

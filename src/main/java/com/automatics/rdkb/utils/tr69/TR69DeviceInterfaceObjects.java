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

public enum TR69DeviceInterfaceObjects {

    STATS_BYTESSENT("Device.IP.Interface.{i}.Stats.BytesSent"),
    STATS_BYTESRECEIVED("Device.IP.Interface.{i}.Stats.BytesReceived"),
    STATS_PACKETSSENT("Device.IP.Interface.{i}.Stats.PacketsSent"),
    STATS_PACKETSRECEIVED("Device.IP.Interface.{i}.Stats.PacketsReceived"),
    STATS_ERRORSSENT("Device.IP.Interface.{i}.Stats.ErrorsSent"),
    STATS_ERRORSRECEIVED("Device.IP.Interface.{i}.Stats.ErrorsReceived"),
    STATS_UNICASTPACKETSSENT("Device.IP.Interface.{i}.Stats.UnicastPacketsSent"),
    STATS_UNICASTPACKETSRECEIVED("Device.IP.Interface.{i}.Stats.UnicastPacketsReceived"),
    STATS_DISCARDPACKETSSENT("Device.IP.Interface.{i}.Stats.DiscardPacketsSent"),
    STATS_DISCARDPACKETSRECEIVED("Device.IP.Interface.{i}.Stats.DiscardPacketsReceived"),
    STATS_MULTICASTPACKETSSENT("Device.IP.Interface.{i}.Stats.MulticastPacketsSent"),
    STATS_MULTICASTPACKETSRECEIVED("Device.IP.Interface.{i}.Stats.MulticastPacketsReceived"),
    STATS_BROADCASTPACKETSSENT("Device.IP.Interface.{i}.Stats.BroadcastPacketsSent"),
    STATS_BROADCASTPACKETSRECEIVED("Device.IP.Interface.{i}.Stats.BroadcastPacketsReceived"),
    STATS_UNKNOWNPROTOPACKETSRECEIVED("Device.IP.Interface.{i}.Stats.UnknownProtoPacketsReceived"),
    ENABLE("Device.IP.Interface.{i}.Enable"),
    IPV4ENABLE("Device.IP.Interface.{i}.IPv4Enable"),
    IPV6ENABLE("Device.IP.Interface.{i}.IPv6Enable"),
    LASTCHANGE("Device.IP.Interface.{i}.LastChange"),
    MAXMTUSIZE("Device.IP.Interface.{i}.MaxMTUSize"),
    STATUS("Device.IP.Interface.{i}.Status"),
    LOCAL_IP_CONFIG("Device.IP.Interface.{i}."),
    MAC_FILTER("Device.WiFi.AccessPoint.{i}."),
    MAC_FILTER_MODE("Device.WiFi.AccessPoint.{i}.X_CISCO_COM_MacFilterTable.");

    private String objectName;

    /**
     * Enumeration constructor.
     * 
     * @param objectName
     *            The parameter to hold the object name.
     * @param value
     *            The parameter name prefix.
     */
    private TR69DeviceInterfaceObjects(String objectName) {
	this.objectName = objectName;
    }

    /**
     * Method to get the parameter object name.
     * 
     * @return The object name
     */
    public String getObjectName() {
	return this.objectName;
    }

    /**
     * Method to assign index to objects.
     * 
     * @return The object name with index
     */
    public String assignIndexAndReturn(int i) {
	return this.objectName.replace("{i}", Integer.toString(i));
    }

}


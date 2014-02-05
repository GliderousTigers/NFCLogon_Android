package com.glideroustigers.nfclogon.network.lan;

public interface UDPDiscoveryListener
{
    void onDataReceived(UDPDiscoveryResponse response);
}

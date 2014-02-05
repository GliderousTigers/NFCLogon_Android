package com.glideroustigers.nfclogon.network.lan;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class UDPDiscoveryResponse
{
    private UDPDiscovery discovery;
    public final byte[] data;
    public final SocketAddress from;
    private final DatagramSocket socket;

    UDPDiscoveryResponse(UDPDiscovery discovery, DatagramSocket socket, byte[] data, SocketAddress from)
    {
        this.discovery = discovery;
        this.data = data;
        this.from = from;
        this.socket = socket;
    }

    public void reply(byte[] data) throws IOException
    {
        this.discovery.stopBroadcasting();
        this.socket.send(new DatagramPacket(data, data.length, this.from));
    }
}

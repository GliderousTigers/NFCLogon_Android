package com.glideroustigers.nfclogon.comm.lan;

import com.glideroustigers.nfclogon.comm.CommResponse;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class UDPCommResponse extends CommResponse
{
    private UDPComm discovery;
    public final SocketAddress from;
    private final DatagramSocket socket;

    UDPCommResponse(UDPComm discovery, DatagramSocket socket, byte[] data, SocketAddress from)
    {
        super(data);
        this.discovery = discovery;
        this.from = from;
        this.socket = socket;
    }

    public void reply(byte[] data) throws IOException
    {
        this.discovery.stopBroadcasting();
        this.socket.send(new DatagramPacket(data, data.length, this.from));
    }
}

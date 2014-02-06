package com.glideroustigers.nfclogon.comm.lan;

import com.glideroustigers.nfclogon.comm.CommResponse;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class UDPCommResponse extends CommResponse
{
    private UDPComm comm;
    private final SocketAddress from;
    private final DatagramSocket socket;

    UDPCommResponse(UDPComm comm, DatagramSocket socket, byte[] data, SocketAddress from)
    {
        super(comm, data);
        this.comm = comm;
        this.from = from;
        this.socket = socket;
    }

    public void reply(byte[] data) throws IOException
    {
        this.comm.stopBroadcasting();
        this.socket.send(new DatagramPacket(data, data.length, this.from));
    }
}

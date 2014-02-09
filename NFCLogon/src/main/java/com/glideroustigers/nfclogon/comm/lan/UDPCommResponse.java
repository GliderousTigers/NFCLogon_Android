package com.glideroustigers.nfclogon.comm.lan;

import com.glideroustigers.nfclogon.comm.CommResponse;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

/**
 * Class to represent a response received over UDP.
 */
public class UDPCommResponse extends CommResponse
{
    // the UDPComm object that received the data
    private UDPComm comm;

    // the address from which the data was received
    private final SocketAddress from;

    // the socket on which the data was received
    private final DatagramSocket socket;

    /**
     * Constructs a new response.
     * @param comm the {@link com.glideroustigers.nfclogon.comm.lan.UDPComm} object
     *             that received the data.
     * @param socket the socket on which the data was received.
     * @param data the data received.
     * @param from the address from which the data was received.
     */
    UDPCommResponse(UDPComm comm, DatagramSocket socket, byte[] data, SocketAddress from)
    {
        super(comm, data);
        this.comm = comm;
        this.from = from;
        this.socket = socket;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reply(byte[] data) throws IOException
    {
        this.comm.stopBroadcasting();
        this.socket.send(new DatagramPacket(data, data.length, this.from));
    }
}

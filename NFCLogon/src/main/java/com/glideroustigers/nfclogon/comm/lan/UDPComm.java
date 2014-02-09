package com.glideroustigers.nfclogon.comm.lan;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import com.glideroustigers.nfclogon.comm.Comm;
import com.glideroustigers.nfclogon.comm.CommListener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Class for UDP communication between the device and the PC.
 * @author Alexandre Cormier
 */
public class UDPComm extends Comm
{
    // whether to broadcast or not
    private volatile boolean broadcast;

    // socket on which to send and receive data
    private DatagramSocket socket;

    // packet to broadcast for device discovery
    private DatagramPacket broadcastPacket;

    // buffer for the data we receive
    private byte[] buffer;

    // packet to receive data with
    private DatagramPacket receivePacket;

    /**
     * Constructs a new {@link com.glideroustigers.nfclogon.comm.lan.UDPComm} that will
     * listen on the specified port.
     * @param port the port to listen on.
     * @throws IOException if the socket cannot be created.
     */
    public UDPComm(int port) throws IOException
    {
        this(null, null, port);
    }

    /**
     * Constructs a new {@link com.glideroustigers.nfclogon.comm.lan.UDPComm} that will
     * broadcast data on the specified port.
     * @param context the context to use.
     * @param broadcastData data to broadcast.
     * @param port port to broadcast on.
     * @throws IOException if the socket cannot be created.
     */
    public UDPComm(Context context, byte[] broadcastData, int port) throws IOException
    {
        super();
        this.broadcast = (broadcastData != null);
        if (this.broadcast)
        {
            this.socket = new DatagramSocket();
            this.broadcastPacket = new DatagramPacket(broadcastData, broadcastData.length, this.getBroadcastAddress(context), port);
        }
        else
        {
            this.socket = new DatagramSocket(port);
        }
        this.socket.setBroadcast(true);
        this.socket.setSoTimeout(1000);
        this.buffer = new byte[1000];
        this.receivePacket = new DatagramPacket(this.buffer, this.buffer.length);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void run()
    {
        try
        {
            while (!this.isClosed())
            {
                if (UDPComm.this.broadcast)
                {
                    this.socket.send(this.broadcastPacket);
                }
                try
                {
                    this.socket.receive(this.receivePacket);
                    byte[] data = Arrays.copyOfRange(this.buffer,
                                                     this.receivePacket.getOffset(),
                                                     this.receivePacket.getOffset() + this.receivePacket.getLength());
                    this.notifyListeners(new UDPCommResponse(UDPComm.this,
                                                             this.socket,
                                                             data,
                                                             this.receivePacket.getSocketAddress()));
                }
                catch (SocketTimeoutException e)
                {
                    // keep going.
                }
            }
        }
        catch (IOException e)
        {
            // ok... execute finally.
        }
        finally
        {
            this.socket.close();
        }
    }

    /**
     * Stops the broadcast.
     */
    public void stopBroadcasting()
    {
        this.broadcast = false;
    }

    // gets the broadcast address
    private InetAddress getBroadcastAddress(Context context) throws UnknownHostException
    {
        DhcpInfo dhcp = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getDhcpInfo();
        int broadcastInt = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] broadcastBytes = new byte[4];
        for (int i = 0; i < 4; i++)
        {
            broadcastBytes[i] = (byte) (broadcastInt >> (8 * i));
        }
        return InetAddress.getByAddress(broadcastBytes);
    }
}

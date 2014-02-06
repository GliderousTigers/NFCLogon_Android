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

public class UDPComm extends Comm
{
    private volatile boolean broadcast;
    private DatagramSocket socket;
    private DatagramPacket broadcastPacket;
    private byte[] buffer;
    private DatagramPacket receivePacket;

    public UDPComm(Context context, int port) throws IOException
    {
        this(context, null, port);
    }

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

        }
        finally
        {
            this.socket.close();
        }
    }

    public void stopBroadcasting()
    {
        this.broadcast = false;
    }

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

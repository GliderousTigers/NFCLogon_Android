package com.glideroustigers.nfclogon.network.lan;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;

public class UDPDiscovery
{
    private volatile boolean broadcast;
    private volatile boolean open;

    private volatile LinkedList<UDPDiscoveryListener> listeners;

    public UDPDiscovery(Context context, int port) throws IOException
    {
        this(context, null, port);
    }

    public UDPDiscovery(Context context, byte[] broadcastData, int port) throws IOException
    {
        this.broadcast = (broadcastData != null);
        this.open = true;
        this.listeners = new LinkedList<UDPDiscoveryListener>();
        new Thread(new UDPRunnable(context, broadcastData, port)).start();
    }

    public boolean addListnener(UDPDiscoveryListener listener)
    {
        return this.listeners.add(listener);
    }

    public boolean removeListener(UDPDiscoveryListener listener)
    {
        return this.listeners.remove(listener);
    }

    public void stopBroadcasting()
    {
        this.broadcast = false;
    }

    public void close()
    {
        this.open = false;
    }

    private class UDPRunnable implements Runnable
    {
        private DatagramSocket socket;
        private DatagramPacket broadcastPacket;
        private byte[] buffer;
        private DatagramPacket receivePacket;

        private UDPRunnable(Context context, byte[] broadcastData, int port) throws IOException
        {
            this.socket = new DatagramSocket();
            this.socket.setBroadcast(true);
            this.socket.setSoTimeout(1000);
            this.broadcastPacket = new DatagramPacket(broadcastData, broadcastData.length, this.getBroadcastAddress(context), port);
            this.buffer = new byte[1000];
            this.receivePacket = new DatagramPacket(this.buffer, this.buffer.length);
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

        private void notifyListeners(UDPDiscoveryResponse response)
        {
            for (UDPDiscoveryListener listener : UDPDiscovery.this.listeners)
            {
                listener.onDataReceived(response);
            }
        }

        @Override
        public void run()
        {
            try
            {
                while (UDPDiscovery.this.open)
                {
                    if (UDPDiscovery.this.broadcast)
                    {
                        this.socket.send(this.broadcastPacket);
                    }
                    try
                    {
                        this.socket.receive(this.receivePacket);
                        byte[] data = Arrays.copyOfRange(this.buffer,
                                                         this.receivePacket.getOffset(),
                                                         this.receivePacket.getOffset() + this.receivePacket.getLength());
                        this.notifyListeners(new UDPDiscoveryResponse(UDPDiscovery.this,
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
    }
}

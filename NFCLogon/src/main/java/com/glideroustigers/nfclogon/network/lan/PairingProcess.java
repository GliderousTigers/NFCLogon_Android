package com.glideroustigers.nfclogon.network.lan;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class PairingProcess
{
    private static final String DISCOVERY_STRING = "NFC_DEVICE";
    private static final int PORT = 31337;

    public PairingProcess(Context context)
    {
        new Thread(new PairingRunnable(context)).start();
    }

    private class PairingRunnable implements Runnable
    {
        private Context context;

        private PairingRunnable(Context context)
        {
            this.context = context;
        }

        @Override
        public void run()
        {
            try
            {
                DatagramSocket socket = new DatagramSocket();
                DatagramPacket packet = new DatagramPacket(DISCOVERY_STRING.getBytes(), DISCOVERY_STRING.length(), this.getBroadcastAddress(), PORT);
                socket.setBroadcast(true);
                while (true)
                {
                    socket.send(packet);
                    Thread.sleep(500);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        private InetAddress getBroadcastAddress() throws UnknownHostException
        {
            DhcpInfo dhcp = ((WifiManager) this.context.getSystemService(Context.WIFI_SERVICE)).getDhcpInfo();
            int broadcastInt = (dhcp.gateway & dhcp.netmask) | ~dhcp.netmask;
            byte[] broadcastBytes = new byte[4];
            for (int i = 0; i < 4; i++)
            {
                broadcastBytes[i] = (byte) (broadcastInt >> (8 * i));
            }
            return InetAddress.getByAddress(broadcastBytes);
        }
    }
}

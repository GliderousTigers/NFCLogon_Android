package com.glideroustigers.nfclogon.activities;

import android.app.Activity;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.glideroustigers.nfclogon.R;
import com.glideroustigers.nfclogon.network.lan.PairingProcess;
import com.glideroustigers.nfclogon.network.lan.UDPDiscovery;
import com.glideroustigers.nfclogon.network.lan.UDPDiscoveryListener;
import com.glideroustigers.nfclogon.network.lan.UDPDiscoveryResponse;
import com.glideroustigers.nfclogon.utils.Crypto;
import com.glideroustigers.nfclogon.utils.Device;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class MainActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main_activity);

        try
        {
            new UDPDiscovery(this, ("NFC" + Device.getName()).getBytes(), 31337).addListnener(new UDPDiscoveryListener() {
                @Override
                public void onDataReceived(UDPDiscoveryResponse response)
                {
                    try
                    {
                        response.reply(response.data);
                    }
                    catch (IOException e)
                    {

                    }
                }
            });
        }
        catch (IOException e)
        {

        }

        //new PairingProcess(this);
    }
}

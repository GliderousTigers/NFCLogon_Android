package com.glideroustigers.nfclogon.activities;

import android.app.Activity;
import android.os.Bundle;

import com.glideroustigers.nfclogon.R;
import com.glideroustigers.nfclogon.comm.Comm;
import com.glideroustigers.nfclogon.comm.CommListener;
import com.glideroustigers.nfclogon.comm.CommResponse;
import com.glideroustigers.nfclogon.comm.bluetooth.BTComm;
import com.glideroustigers.nfclogon.comm.lan.UDPCommResponse;
import com.glideroustigers.nfclogon.comm.lan.UDPComm;
import com.glideroustigers.nfclogon.utils.Device;

import java.io.IOException;

public class MainActivity extends Activity implements CommListener
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main_activity);

        try
        {
            Comm udp = new UDPComm(this, ("NFC" + Device.getName()).getBytes(), 31337);
            udp.addListener(this);
            udp.start();
            Comm bt = new BTComm();
            bt.addListener(this);
            bt.start();
        }
        catch (IOException e)
        {

        }
    }

    @Override
    public void onDataReceived(CommResponse response)
    {
        try
        {
            response.reply(response.data);
        }
        catch (IOException e)
        {

        }
    }
}

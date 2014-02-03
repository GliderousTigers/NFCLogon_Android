package com.glideroustigers.nfclogon.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.glideroustigers.nfclogon.R;
import com.glideroustigers.nfclogon.utils.Crypto;
import com.glideroustigers.nfclogon.utils.Device;

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
        Toast.makeText(this, Device.getName(), Toast.LENGTH_LONG).show();
    }
}

package com.glideroustigers.nfclogon.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.glideroustigers.nfclogon.R;
import com.glideroustigers.nfclogon.utils.Device;

public class MainActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main_activity);
        Toast.makeText(this, Device.getUUID(this), Toast.LENGTH_LONG).show();
    }
}

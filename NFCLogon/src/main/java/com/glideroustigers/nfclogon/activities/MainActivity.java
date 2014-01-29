package com.glideroustigers.nfclogon.activities;

import android.app.Activity;
import android.os.Bundle;
import android.nfc.NfcAdapter;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;


import com.glideroustigers.nfclogon.R;



public class MainActivity extends Activity
{
    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main_activity);
        TextView textView = (TextView) findViewById(R.id.derp);
        textView.setText("First String");
        resolveIntent(getIntent());
    }

    void resolveIntent(Intent intent) {
        // Parse the intent
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            // When a tag is discovered we send it to the service to be save. We
            // include a PendingIntent for the service to call back onto. This
            // will cause this activity to be restarted with onNewIntent(). At
            // that time we read it from the database and view it.
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                msgs = new NdefMessage[] {msg};
            }
            TextView textView = (TextView) findViewById(R.id.derp);
            textView.setText(new String(msgs[0].getRecords()[0].getPayload()));
        } else {
            TextView textView = (TextView) findViewById(R.id.derp);
            textView.setText("Nope");
            return;
        }
    }


}



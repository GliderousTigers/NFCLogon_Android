package com.glideroustigers.nfclogon.activities;

import android.app.Activity;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.nfc.NfcAdapter;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;


import com.glideroustigers.nfclogon.R;

import static android.nfc.NdefRecord.createMime;


public class MainActivity extends Activity
{
    private static final String TAG = "MainActivity";
    public static String PACKAGE_NAME;
    private String payload;
    NfcAdapter mNfcAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        PACKAGE_NAME = getApplicationContext().getPackageName();
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main_activity);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        resolveIntent(getIntent());

    }

    void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];

                }
                payload = new String(msgs[0].getRecords()[0].getPayload());
                payload = payload.substring(3);
                HandshakeClient hsc = new HandshakeClient(payload.getBytes(), this);
            } else {
                // Unknown tag type
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                msgs = new NdefMessage[] {msg};
                Log.d(TAG, new String(msgs[0].getRecords()[0].getPayload()));
                payload = new String(msgs[0].getRecords()[0].getPayload());
                payload = payload.substring(3);
                HandshakeClient hsc = new HandshakeClient(payload.getBytes(), this);
            }

            Toast.makeText(this,"Done", Toast.LENGTH_SHORT).show();
            finish();


            }
        }
    }



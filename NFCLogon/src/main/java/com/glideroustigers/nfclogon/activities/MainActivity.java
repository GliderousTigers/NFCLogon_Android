package com.glideroustigers.nfclogon.activities;

import android.app.Activity;
import android.os.Bundle;
import android.nfc.NfcAdapter;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.glideroustigers.nfclogon.R;

import static android.nfc.NdefRecord.createMime;


public class MainActivity extends Activity
{
    public String PACKAGE_NAME = this.getPackageName();
    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main_activity);
        resolveIntent(getIntent());
    }

    void resolveIntent(Intent intent) {
        // Parse the intent
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];

                }
            Toast.makeText(this, new String(msgs[0].getRecords()[0].getPayload()), Toast.LENGTH_SHORT).show();
            finish();
            } else {
                // Unknown tag type
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {record});
                msgs = new NdefMessage[] {msg};
                Log.d(TAG, new String(msgs[0].getRecords()[0].getPayload()));
            }
            Toast.makeText(this, new String(msgs[0].getRecords()[0].getPayload()), Toast.LENGTH_SHORT).show();
            finish();

        }
        else
            return;
        }

        public NdefMessage createNdefMessage(String text) {
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { createMime(
                        "text/ascii", text.getBytes()),
                });
        return msg;
        }

         public NdefMessage createAppNdef(){
        String text = PACKAGE_NAME;
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] {createMime(
                        "application", text.getBytes()),
                });
        return msg;
         }
    }



package com.glideroustigers.nfclogon.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import java.util.Arrays;

public class NFCActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedBundleState)
    {
        super.onCreate(savedBundleState);
        Toast.makeText(this, new String(this.getMessages()[0].getRecords()[0].getPayload()), Toast.LENGTH_SHORT).show();
        this.finish();
    }

    private NdefMessage[] getMessages()
    {
        Parcelable[] rawMessages = (Parcelable[]) this.getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage[] messages = new NdefMessage[rawMessages.length];
        for (int i = 0; i < rawMessages.length; i++)
        {
            messages[i] = (NdefMessage) rawMessages[i];
        }
        return messages;
    }
}

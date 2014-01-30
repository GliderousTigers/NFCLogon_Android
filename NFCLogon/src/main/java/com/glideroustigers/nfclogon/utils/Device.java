package com.glideroustigers.nfclogon.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.glideroustigers.nfclogon.R;

import java.util.UUID;

public abstract class Device
{
    public static String getUUID(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.pref_device_file), Context.MODE_PRIVATE);
        if (!prefs.contains(context.getString(R.string.pref_key_uuid)))
        {
            UUIDProvider[] providers = { new TelephonyUUIDProvider(),
                                         new SerialUUIDProvider(),
                                         new AndroidUUIDProvider(),
                                         new CustomUUIDProvider() };
            String id = null;
            for (int i = 0; id == null; i++)
            {
                id = providers[i].getUUID(context);
            }
            prefs.edit().putString(context.getString(R.string.pref_key_uuid), UUID.nameUUIDFromBytes(id.getBytes()).toString()).commit();
        }
        return prefs.getString(context.getString(R.string.pref_key_uuid), null);
    }

    private interface UUIDProvider
    {
        String getUUID(Context context);
    }

    private static class TelephonyUUIDProvider implements UUIDProvider
    {
        @Override
        public String getUUID(Context context)
        {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return (tm == null) ? null : tm.getDeviceId();
        }
    }

    private static class SerialUUIDProvider implements UUIDProvider
    {
        @Override
        public String getUUID(Context context)
        {
            return Build.SERIAL.equals(Build.UNKNOWN) ? null : Build.SERIAL;
        }
    }

    private static class AndroidUUIDProvider implements UUIDProvider
    {
        @Override
        public String getUUID(Context context)
        {
            String id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            return id.equals("9774d56d682e549c") ? null : id;
        }
    }

    private static class CustomUUIDProvider implements UUIDProvider
    {
        @Override
        public String getUUID(Context context)
        {
            return UUID.randomUUID().toString();
        }
    }
}

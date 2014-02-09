package com.glideroustigers.nfclogon.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.glideroustigers.nfclogon.R;

import java.util.UUID;

/**
 * Utility class to get information about the device.
 * @author Alexandre Cormier
 */
public abstract class Device
{
    /**
     * Gets a device name consisting the brand and model.
     * @return the device name.
     */
    public static final String getName()
    {
        return Build.BRAND + " " + Build.MODEL;
    }

    /**
     * Gets the version of Android the device is running.
     * @return the version of Android the device is running.
     */
    public static final String getVersion()
    {
        return Build.VERSION.RELEASE;
    }

    /**
     * Gets a unique device identifier.
     * @param context the context to use.
     * @return the unique device identifier.
     */
    public static String getUDID(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.pref_device_file), Context.MODE_PRIVATE);
        if (!prefs.contains(context.getString(R.string.pref_key_uuid)))
        {
            UDIDProvider[] providers = { new TelephonyUDIDProvider(),
                                         new SerialUDIDProvider(),
                                         new AndroidUDIDProvider(),
                                         new CustomUDIDProvider() };
            String id = null;
            for (int i = 0; id == null; i++)
            {
                id = providers[i].getUDID(context);
            }
            prefs.edit().putString(context.getString(R.string.pref_key_uuid), UUID.nameUUIDFromBytes(id.getBytes()).toString()).commit();
        }
        return prefs.getString(context.getString(R.string.pref_key_uuid), null);
    }

    // interface to represent a device id provider
    private interface UDIDProvider
    {
        // gets the device id
        String getUDID(Context context);
    }

    // class to provide a telephony-based device id
    private static class TelephonyUDIDProvider implements UDIDProvider
    {
        @Override
        public String getUDID(Context context)
        {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return (tm == null) ? null : tm.getDeviceId();
        }
    }

    // class providing the serial number as a device id
    private static class SerialUDIDProvider implements UDIDProvider
    {
        @Override
        public String getUDID(Context context)
        {
            return Build.SERIAL.equals(Build.UNKNOWN) ? null : Build.SERIAL;
        }
    }

    // class providing the value of ANDROID_ID
    private static class AndroidUDIDProvider implements UDIDProvider
    {
        @Override
        public String getUDID(Context context)
        {
            String id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            return id.equals("9774d56d682e549c") ? null : id;
        }
    }

    // class generating a device id
    private static class CustomUDIDProvider implements UDIDProvider
    {
        @Override
        public String getUDID(Context context)
        {
            return UUID.randomUUID().toString();
        }
    }
}

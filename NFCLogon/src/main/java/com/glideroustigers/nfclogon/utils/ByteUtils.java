package com.glideroustigers.nfclogon.utils;

import java.util.Arrays;

public abstract class ByteUtils
{
    public static final byte[] toBytes(int i)
    {
        byte[] bytes = new byte[4];
        for (int j = 0; j < 4; j++)
        {
            bytes[j] = (byte) (i >> (8 * (3 - j)));
        }
        return bytes;
    }

    public static final int getInt(byte[] bytes)
    {
        int i = 0;
        for (int j = 0; j < 4; j++)
        {
            i |= (bytes[j] & 0xFF) << (8 * (3 - j));
        }
        return i;
    }

    public static final byte[] subArray(byte[] array, int offset, int length)
    {
        return Arrays.copyOfRange(array, offset, offset + length);
    }

    public static final byte[] concat(byte[] first, byte[]... others)
    {
        int length = first.length;
        for (byte[] array : others)
        {
            length += array.length;
        }
        byte[] newArray = Arrays.copyOf(first, length);
        int offset = first.length;
        for (byte[] array : others)
        {
            System.arraycopy(array, 0, newArray, offset, array.length);
            offset += array.length;
        }
        return newArray;
    }
}

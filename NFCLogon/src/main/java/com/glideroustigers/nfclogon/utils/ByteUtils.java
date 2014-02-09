package com.glideroustigers.nfclogon.utils;

import java.util.Arrays;

/**
 * Utility class to perform operations on bytes and byte arrays.
 */
public abstract class ByteUtils
{
    /**
     * Converts an int to a byte array.
     * @param i the int to convert.
     * @return the int as a byte array.
     */
    public static final byte[] toBytes(int i)
    {
        byte[] bytes = new byte[4];
        for (int j = 0; j < 4; j++)
        {
            bytes[j] = (byte) (i >> (8 * (3 - j)));
        }
        return bytes;
    }

    /**
     * Converts a byte array to an int.
     * @param bytes the byte array to convert.
     * @return the byte array as an int.
     */
    public static final int getInt(byte[] bytes)
    {
        int i = 0;
        for (int j = 0; j < 4; j++)
        {
            i |= (bytes[j] & 0xFF) << (8 * (3 - j));
        }
        return i;
    }

    /**
     * Gets a part of a byte array.
     * @param array the array to get a part of.
     * @param offset the start index of the sub-array.
     * @param length the length of the sub-array.
     * @return the sub-array starting at offset (inclusive) and ending at
     *         offset + length (exclusive)
     */
    public static final byte[] subArray(byte[] array, int offset, int length)
    {
        return Arrays.copyOfRange(array, offset, offset + length);
    }

    /**
     * Concatenates byte arrays.
     * @param first the first array.
     * @param others the other arrays to add to the first.
     * @return a single array that contains those passed as parameters
     */
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

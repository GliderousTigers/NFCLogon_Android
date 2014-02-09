package com.glideroustigers.nfclogon.comm.packets;

import com.glideroustigers.nfclogon.utils.ByteUtils;

/**
 * Class used to represent a field in a packet.
 * @author Alexandre Cormier
 */
public class PacketField
{
    // the field's data
    private byte[] data;

    /**
     * Constructs a new field with the data provided.
     * @param data the field's data.
     */
    public PacketField(byte[] data)
    {
        this.data = data;
    }

    /**
     * Constructs a new field object from a received packet.
     * @param packetData the packet's data.
     * @param offset the offset at which the desired field is located.
     */
    public PacketField(byte[] packetData, int offset)
    {
        int length = ByteUtils.getInt(ByteUtils.subArray(data, offset, 4));
        this.data = ByteUtils.subArray(data, offset + 4, length);
    }

    /**
     * Gets this field as bytes, as it must be added to a packet.
     * @return this field as bytes.
     */
    public byte[] getBytes()
    {
        return ByteUtils.concat(ByteUtils.toBytes(this.data.length), this.data);
    }

    /**
     * Gets the total length of this field, including the four
     * bytes used to store the data's length.
     * @return the total length of this field.
     */
    public int getLength()
    {
        return this.data.length + 4;
    }

    /**
     * Gets this field's data.
     * @return this field's data.
     */
    public byte[] getData()
    {
        return this.data;
    }
}

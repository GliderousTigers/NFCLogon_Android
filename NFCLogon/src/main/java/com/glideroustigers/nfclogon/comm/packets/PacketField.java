package com.glideroustigers.nfclogon.comm.packets;

import com.glideroustigers.nfclogon.utils.ByteUtils;

public class PacketField
{
    private byte[] data;

    public PacketField(byte[] data)
    {
        this.data = data;
    }

    public PacketField(byte[] packetData, int offset)
    {
        int length = ByteUtils.getInt(ByteUtils.subArray(data, offset, 4));
        this.data = ByteUtils.subArray(data, offset + 4, length);
    }

    public byte[] getBytes()
    {
        return ByteUtils.concat(ByteUtils.toBytes(this.data.length), this.data);
    }

    public int getLength()
    {
        return this.data.length + 4;
    }

    public byte[] getData()
    {
        return this.data;
    }
}

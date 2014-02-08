package com.glideroustigers.nfclogon.comm.packets;

import com.glideroustigers.nfclogon.utils.ByteUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Base class for different packets that passing from the Android device
 * to the PC and vice-versa during pairing and the unlocking handshake.
 * All subclasses must have a default constructor taking no parameter
 * with package access level.
 */
public abstract class AbstractPacket
{
    // map packet type names to their respective classes
    private static HashMap<String, Class<? extends AbstractPacket>> map;
    static
    {
        map = new HashMap<String, Class<? extends AbstractPacket>>();
        map.put(RSAPublicKeyPacket.PACKET_TYPE, RSAPublicKeyPacket.class);
    }

    // packet type name
    private String packetType;

    // fields contained in this packet
    private ArrayList<PacketField> fields;

    /**
     * Constructs an empty packet with the specified type name.
     * @param packetType the type of this packet. Each subclass must have
     *                   a unique name and pass it here.
     */
    protected AbstractPacket(String packetType)
    {
        this.packetType = packetType;
    }

    /**
     * Gets the fields to add to this packet when constructing it to send.
     * @return this packets' fields.
     */
    protected abstract ArrayList<PacketField> getFields();

    /**
     * Called when receiving a packet and creating it with {@link #get(byte[])}, after
     * it has been parsed and fields are available from the {@link #getField(int)} method.
     * @throws InvalidPacketException if the received packet's format is incorrect.
     */
    protected abstract void onParseFinished() throws InvalidPacketException;

    /**
     * Gets a packet object from the data received.
     * @param data raw data received from the PC.
     * @return an {@link com.glideroustigers.nfclogon.comm.packets.AbstractPacket}
     *         constructed with the data received.
     * @throws InvalidPacketException if the received packet's format is incorrect.
     */
    public static AbstractPacket get(byte[] data) throws InvalidPacketException
    {
        try
        {
            PacketField packetTypeField = new PacketField(data, 0);
            AbstractPacket packet = map.get(new String(packetTypeField.getData())).newInstance();
            packet.parse(Arrays.copyOfRange(data, packetTypeField.getLength(), data.length - 1));
            return packet;
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            throw new InvalidPacketException(e);
        }
    }

    /**
     * Gets a field from its position in the packet.
     * @param index position in the packet of the field to get.
     * @return the field at the specified index.
     */
    protected final PacketField getField(int index)
    {
        return this.fields.get(index);
    }

    // parse the data received into fields
    private void parse(byte[] data) throws InvalidPacketException
    {
        this.fields = new ArrayList<PacketField>();
        int offset = 0;
        while (offset < data.length)
        {
            PacketField field = new PacketField(data, offset);
            this.fields.add(field);
            offset += field.getLength();
        }
        this.onParseFinished();
    }

    /**
     * Gets the raw data of this packet to send to the PC.
     * @return this packet's data.
     */
    public final byte[] getBytes()
    {
        if (this.fields == null)
        {
            this.fields = this.getFields();
        }
        byte[] data = new PacketField(this.packetType.getBytes()).getBytes();
        for (PacketField field : this.fields)
        {
            ByteUtils.concat(data, field.getBytes());
        }
        return data;
    }
}

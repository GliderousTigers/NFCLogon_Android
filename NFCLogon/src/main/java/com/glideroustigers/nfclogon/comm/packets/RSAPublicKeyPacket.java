package com.glideroustigers.nfclogon.comm.packets;

import com.glideroustigers.nfclogon.utils.ByteUtils;
import com.glideroustigers.nfclogon.utils.Crypto;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Arrays;

public class RSAPublicKeyPacket extends AbstractPacket
{
    static final String PACKET_TYPE = "PK";

    private RSAPublicKey key;

    RSAPublicKeyPacket()
    {
        super(PACKET_TYPE);
    }

    public RSAPublicKeyPacket(RSAPublicKey key)
    {
        this();
        this.key = key;
    }

    public RSAPublicKey getPublicKey()
    {
        return this.key;
    }

    @Override
    protected ArrayList<PacketField> getFields()
    {
        ArrayList<PacketField> fields = new ArrayList<PacketField>();
        fields.add(new PacketField(this.key.getModulus().toByteArray()));
        fields.add(new PacketField(this.key.getPublicExponent().toByteArray()));
        return fields;
    }

    @Override
    protected void onParseFinished() throws InvalidPacketException
    {
        try
        {
            BigInteger modulus = new BigInteger(this.getField(0).getData());
            BigInteger exponent = new BigInteger(this.getField(1).getData());
            this.key = Crypto.getPublicKey(modulus, exponent);
        }
        catch (GeneralSecurityException e)
        {
            throw new InvalidPacketException(e);
        }
    }
}

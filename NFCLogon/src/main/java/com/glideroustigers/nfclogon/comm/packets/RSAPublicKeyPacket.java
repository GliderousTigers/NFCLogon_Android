package com.glideroustigers.nfclogon.comm.packets;

import com.glideroustigers.nfclogon.utils.ByteUtils;
import com.glideroustigers.nfclogon.utils.Crypto;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that represents a packet encapsulating an RSA public key.
 * @author Alexandre Cormier
 */
public class RSAPublicKeyPacket extends AbstractPacket
{
    /**
     * The name of this packet type.
     */
    public static final String PACKET_TYPE = "PK";

    // the public key itself
    private RSAPublicKey key;

    // default constructor needed for AbstractPacket.get()
    RSAPublicKeyPacket()
    {
        super(PACKET_TYPE);
    }

    /**
     * Construct a new {@link java.security.interfaces.RSAPublicKey} from the public
     * key to send.
     * @param key the public key to send.
     */
    public RSAPublicKeyPacket(RSAPublicKey key)
    {
        this();
        this.key = key;
    }

    /**
     * Gets the public key encapsulated in this packet.
     * @return the public key encapsulated in this packet.
     */
    public RSAPublicKey getPublicKey()
    {
        return this.key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ArrayList<PacketField> getFields()
    {
        ArrayList<PacketField> fields = new ArrayList<PacketField>();
        fields.add(new PacketField(this.key.getModulus().toByteArray()));
        fields.add(new PacketField(this.key.getPublicExponent().toByteArray()));
        return fields;
    }

    /**
     * {@inheritDoc}
     */
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

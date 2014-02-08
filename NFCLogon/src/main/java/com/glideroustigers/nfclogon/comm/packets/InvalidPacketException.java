package com.glideroustigers.nfclogon.comm.packets;

/**
 * Exception thrown when a received packet's format is incorrect.
 */
public class InvalidPacketException extends Exception
{
    /**
     * Constructs an {@link com.glideroustigers.nfclogon.comm.packets.InvalidPacketException}
     * with the inner exception that caused it.
     * @param cause the inner exception causing this
     *              {@link com.glideroustigers.nfclogon.comm.packets.InvalidPacketException}.
     */
    InvalidPacketException(Exception cause)
    {
        super(cause);
    }
}

package com.glideroustigers.nfclogon.comm.packets;

public class InvalidPacketException extends Exception
{
    InvalidPacketException(Exception cause)
    {
        super(cause);
    }
}

package com.glideroustigers.nfclogon.comm;

import java.io.IOException;

public abstract class CommResponse
{
    public final byte[] data;

    public CommResponse(byte[] data)
    {
        this.data = data;
    }

    public abstract void reply(byte[] data) throws IOException;
}

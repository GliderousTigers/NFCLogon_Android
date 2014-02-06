package com.glideroustigers.nfclogon.comm.bluetooth;

import com.glideroustigers.nfclogon.comm.CommResponse;

import java.io.IOException;

public class BTCommResponse extends CommResponse
{
    BTCommResponse(byte[] data)
    {
        super(data);
    }

    @Override
    public void reply(byte[] data) throws IOException
    {

    }
}

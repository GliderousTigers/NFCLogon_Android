package com.glideroustigers.nfclogon.comm;

import android.os.Handler;

import java.io.IOException;

public abstract class CommResponse
{
    private Handler handler;
    public final byte[] data;

    public CommResponse(Comm comm, byte[] data)
    {
        this.handler = comm.handler;
        this.data = data;
    }

    public abstract void reply(byte[] data) throws IOException;

    public void runOnUiThread(Runnable r)
    {
        this.handler.post(r);
    }
}

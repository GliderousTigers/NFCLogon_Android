package com.glideroustigers.nfclogon.comm;

import android.os.Handler;

import java.io.IOException;

/**
 * Class to encapsulate the data received from a communication and provide
 * some methods that will be useful when receiving data, namely to reply.
 */
public abstract class CommResponse
{
    // handler to run code on the ui thread
    private Handler handler;

    /**
     * The data received.
     */
    public final byte[] data;

    /**
     * Constructs a new response.
     * @param comm the {@link com.glideroustigers.nfclogon.comm.Comm} object that received
     *             the response.
     * @param data the data recived.
     */
    public CommResponse(Comm comm, byte[] data)
    {
        this.handler = comm.handler;
        this.data = data;
    }

    /**
     * Replies to the client who sent the data.
     * @param data data to send.
     * @throws IOException if there is a problem sending a data.
     */
    public abstract void reply(byte[] data) throws IOException;

    /**
     * Executes a runnable on the ui thread.
     * @param r the runnable to execute.
     */
    public final void runOnUiThread(Runnable r)
    {
        this.handler.post(r);
    }
}

package com.glideroustigers.nfclogon.comm;

/**
 * Interface for a communication listener, to get the data received.
 */
public interface CommListener
{
    /**
     * Called when a packet is received.
     * @param response object containing the received packet and a mean to reply.
     */
    void onDataReceived(CommResponse response);
}

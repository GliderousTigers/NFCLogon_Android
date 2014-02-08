package com.glideroustigers.nfclogon.comm;

import android.os.Handler;

import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.LogRecord;

/**
 * Class to represent the communication between the Android device
 * and the PC. A subclass should be created for each communication mean.
 */
public abstract class Comm implements Closeable
{
    // list of listeners waiting for the reception of packets
    private volatile LinkedList<CommListener> listeners;

    // whether or not the communication was closed
    private volatile boolean closed;

    // thread in which the communication is run
    private Thread thread;

    // handler to run code on the ui thread upon the reception of a packet
    final Handler handler;

    /**
     * Constructs a new {@link com.glideroustigers.nfclogon.comm.Comm} object.
     */
    public Comm()
    {
        this.listeners = new LinkedList<CommListener>();
        this.closed = false;
        this.thread = new Thread(new CommRunnable());
        this.handler = new Handler();
    }

    /**
     * Starts the communication. The {@link #run()} method is called from a new thread.
     */
    public void start()
    {
        this.thread.start();
    }

    /**
     * Called from a worker thread when the communication is started. Subclasses should
     * loop as long as {@link #isClosed()} returns {@code false}.
     */
    protected abstract void run();

    /**
     * Registers a listener with this communication.
     * @param listener the listener to register.
     */
    public void addListener(CommListener listener)
    {
        this.listeners.add(listener);
    }

    /**
     * Unregisters a listener from this communication.
     * @param listener the listener to unregister.
     * @return whether or not the listener was unregistered.
     */
    boolean removeListener(CommListener listener)
    {
        return this.listeners.remove(listener);
    }

    /**
     * Notifies listeners of the reception of a packet.
     * @param response object containing the received packet and a mean to reply.
     */
    protected void notifyListeners(CommResponse response)
    {
        for (CommListener listener : this.listeners)
        {
            listener.onDataReceived(response);
        }
    }

    /**
     * Gets whether or not the communication was closed.
     * @return whether or not the communication was closed.
     */
    public boolean isClosed()
    {
        return this.closed;
    }

    /**
     * Closes this communication and waits for the worker thread to exit.
     * @throws IOException should not happen.
     */
    @Override
    public void close() throws IOException
    {
        this.closed = true;
        try
        {
            this.thread.join();
        }
        catch (InterruptedException e)
        {
            // should not happen, but we won't take any chance
            throw new IOException(e);
        }
    }

    // runnable that is executed in the worker thread
    private class CommRunnable implements Runnable
    {
        @Override
        public void run()
        {
            Comm.this.run();
        }
    }
}

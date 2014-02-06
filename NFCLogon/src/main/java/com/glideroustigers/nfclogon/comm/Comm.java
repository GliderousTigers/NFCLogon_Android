package com.glideroustigers.nfclogon.comm;

import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;

public abstract class Comm implements Closeable
{
    private volatile LinkedList<CommListener> listeners;
    private boolean closed;
    private Thread thread;

    public Comm()
    {
        this.listeners = new LinkedList<CommListener>();
        this.closed = false;
        this.thread = new Thread(new CommRunnable());
    }

    public void start()
    {
        this.thread.start();
    }

    protected abstract void run();

    public boolean addListener(CommListener listener)
    {
        return this.listeners.add(listener);
    }

    boolean removeListener(CommListener listener)
    {
        return this.listeners.remove(listener);
    }

    protected void notifyListeners(CommResponse response)
    {
        for (CommListener listener : this.listeners)
        {
            listener.onDataReceived(response);
        }
    }

    public boolean isClosed()
    {
        return this.closed;
    }

    @Override
    public void close() throws IOException
    {
        this.closed = false;
        try
        {
            this.thread.join();
        }
        catch (InterruptedException e)
        {
            throw new IOException(e);
        }
    }

    private class CommRunnable implements Runnable
    {
        @Override
        public void run()
        {
            Comm.this.run();
        }
    }
}

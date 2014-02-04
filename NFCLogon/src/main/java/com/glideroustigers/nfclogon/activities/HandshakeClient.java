package com.glideroustigers.nfclogon.activities;

import android.content.Context;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class HandshakeClient {

    private final int PC_APP_PORT = 41337;

    private byte[] payload;
    private String serverIp = "derp";
    private Context ctx;
    private Handler handler = new Handler();

    public HandshakeClient(byte[] payload, Context ctx)
    {
        this.ctx = ctx;
        this.payload = payload;
            if (!serverIp.equals("")) {
                Thread cThread = new Thread(new HandshakeThread());
                cThread.start();
                try{
                    cThread.join();
                }
                catch(Exception e)
                {
                  Log.e("HandshakeClient", e.getMessage());
                }

            }
        }


    public class HandshakeThread implements Runnable {

        Socket socket;
        public void sendBytes(byte[] myByteArray, int start, int len) throws IOException {
            if (len < 0)
                throw new IllegalArgumentException("Negative length not allowed");
            if (start < 0 || start >= myByteArray.length)
                throw new IndexOutOfBoundsException("Out of bounds: " + start);

            OutputStream out = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);

            if (len > 0) {
                dos.write(myByteArray, start, len);
            }
        }
        public void run() {
            try {
                InetAddress serverInetAddr = InetAddress.getByName("10.17.64.242");
                Log.d("HandshakeClient", serverInetAddr.getHostAddress());
                this.socket = new Socket(serverInetAddr, PC_APP_PORT);
                Log.d("HandshakeClient", "C: Connecting...");
                    try {
                        Log.d("HandshakeClient", "C: Sending command.");
                        Log.d("HandshakeClient", "C: Sent.");
                    } catch (Exception e) {
                        Log.e("HandshakeClient", "S: Error", e);
                    }
                sendBytes(payload, 0, payload.length);
                socket.close();
                Log.d("HandshakeClient", "C: Closed.");
            } catch (Exception e) {
                Log.e("HandshakeClient", "C: Error", e);
            }
        }
    }
}

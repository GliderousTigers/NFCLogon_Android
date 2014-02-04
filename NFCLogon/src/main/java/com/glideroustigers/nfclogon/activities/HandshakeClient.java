package com.glideroustigers.nfclogon.activities;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class HandshakeClient {

    private final int PC_APP_PORT = 41337;

    private String payload;
    private String serverIp = "derp";
    private Context ctx;
    private boolean connected = false;
    private Handler handler = new Handler();

    public HandshakeClient(String payload, Context ctx)
    {
        this.ctx = ctx;
        this.payload = payload;
        if (!connected) {
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
    }

    public class HandshakeThread implements Runnable {

        public void run() {
            try {
                InetAddress serverInetAddr = InetAddress.getByName("jerome-pc");
                Toast.makeText(ctx, serverInetAddr.getHostAddress(), Toast.LENGTH_LONG).show();
                Socket socket = new Socket(serverInetAddr, PC_APP_PORT);
                Log.d("HandshakeClient", "C: Connecting...");
                connected = true;
                while (connected) {
                    try {
                        Log.d("HandshakeClient", "C: Sending command.");
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                                .getOutputStream())), true);
                        //tag TODO
                        out.println(payload);
                        Log.d("HandshakeClient", "C: Sent.");
                    } catch (Exception e) {
                        Log.e("HandshakeClient", "S: Error", e);
                    }
                }
                socket.close();
                Log.d("HandshakeClient", "C: Closed.");
            } catch (Exception e) {
                Log.e("HandshakeClient", "C: Error", e);
                connected = false;
            }
        }
    }
}

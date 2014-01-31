package com.glideroustigers.nfclogon.activities;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class HandshakeClient {

    private final int PC_APP_PORT = 41337;

    private String serverIp = "";
    private boolean connected = false;
    private Handler handler = new Handler();

    public HandshakeClient()
    {

    }

    private View.OnClickListener connectListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!connected) {
                if (!serverIp.equals("")) {
                    Thread cThread = new Thread(new HandshakeThread());
                    cThread.start();
                }
            }
        }
    };

    public class HandshakeThread implements Runnable {

        public void run() {
            try {
                InetAddress serverInetAddr = InetAddress.getByName(serverIp);
                Socket socket = new Socket(serverInetAddr, PC_APP_PORT);
                Log.d("HandshakeClient", "C: Connecting...");
                connected = true;
                while (connected) {
                    try {
                        Log.d("HandshakeClient", "C: Sending command.");
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                                .getOutputStream())), true);
                        //tag TODO
                        out.println("Plop!");
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

package com.glideroustigers.nfclogon.activities;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class ClientActivity extends ActionBarActivity {

    private EditText serverIp;

    private Button connectPhones;

    private String serverIpAddress = "";

    private boolean connected = false;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client);

        serverIp = (EditText) findViewById(R.id.server_ip);
        connectPhones = (Button) findViewById(R.id.connect_phones);
        connectPhones.setOnClickListener(connectListener);
    }

    private OnClickListener connectListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!connected) {
                serverIpAddress = serverIp.getText().toString();
                if (!serverIpAddress.equals("")) {
                    Thread cThread = new Thread(new ClientThread());
                    cThread.start();
                }
            }
        }
    };

    public class ClientThread implements Runnable {

        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                Log.d("ClientActivity", "C: Connecting...");
                Socket socket = new Socket(serverAddr, ServerActivity.SERVERPORT);
                connected = true;
                while (connected) {
                    try {
                        Log.d("ClientActivity", "C: Sending command.");
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
                                .getOutputStream())), true);
                        // WHERE YOU ISSUE THE COMMANDS
                        out.println("Hey Server!");
                        Log.d("ClientActivity", "C: Sent.");
                    } catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                    }
                }
                socket.close();
                Log.d("ClientActivity", "C: Closed.");
            } catch (Exception e) {
                Log.e("ClientActivity", "C: Error", e);
                connected = false;
            }
        }
    }
}

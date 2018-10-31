package com.example.ramya.brdservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "CheckNetworkStatus";
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    private TextView networkStatus;
    private boolean status;
    private int val;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

        networkStatus = (TextView) findViewById(R.id.networkStatus);
    }

    @Override
    protected void onDestroy() {
        Log.v(LOG_TAG, "onDestory");
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            Log.v(LOG_TAG, "Receieved notification about network status");
            isNetworkAvailable(context);
        }

        private boolean isNetworkAvailable(Context context) {

            ConnectivityManager connectivity = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo mobinfo = null;
                NetworkInfo wifinfo = null;
                mobinfo = connectivity.getActiveNetworkInfo();
                wifinfo = connectivity.getActiveNetworkInfo();
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            if (!isConnected) {
                                if (mobinfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                                    Log.v(LOG_TAG, "Now you are connected to Internet!");
                                    networkStatus.setText("Mobile Network");
                                    Toast.makeText(getApplicationContext(), "Connected to Mobile Network ", Toast.LENGTH_LONG).show();
                                    isConnected = true;
                                }
                                // return true;
                                else if (wifinfo.getType() == (ConnectivityManager.TYPE_WIFI))
                                    networkStatus.setText("Wifi Network");
                                Toast.makeText(getApplicationContext(), " Connected to Wifi Network", Toast.LENGTH_LONG).show();

                                isConnected = true;
                            }
                            return true;

                        }


                    }
                }
            }

            Log.v(LOG_TAG, "You are not connected to Internet!");
            if (isConnected)
                Toast.makeText(getApplicationContext(), "No network", Toast.LENGTH_LONG).show();
            networkStatus.setText("You are not connected to Internet!");
            isConnected = false;
            return false;

        }
    }
}





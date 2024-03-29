package com.bananatech.asgha.ouda_client;

import android.app.ActivityManager;
import android.app.Service;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;

import com.bananatech.asgha.ouda_client.SocketService;
public class MainActivity extends AppCompatActivity {

    //private Socket mSocket;
    boolean connected = false;
    Intent sock_ser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*try {
            mSocket = IO.socket("http://ouda-server.herokuapp.com:80");
        } catch (URISyntaxException e) {}

        mSocket.on("notify", onNotify);*/

        sock_ser = new Intent(this, SocketService.class);
        final Button connectButton = (Button) findViewById(R.id.connect);
        if(isServiceRunning(SocketService.class)) {
            connected = true;
            connectButton.setText("Disconnect and Stop");
        }
        connectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                /*if(mSocket.connected()) {
                    mSocket.disconnect();
                    connectButton.setText("Connect and Run");
                }else{
                    mSocket.connect();
                    connectButton.setText("Disconnect and Stop");
                }*/
                if(connected){
                    stopService(sock_ser);
                    connectButton.setText("Connect and Run");
                    connected = false;
                }else{
                    startService(sock_ser);
                    connectButton.setText("Disconnect and Stop");
                    connected = true;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isServiceRunning(Class<?> serviceClass){
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceClass.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    /*private Emitter.Listener onNotify = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String toSend = (String) args[0];
                    Notification notification = new NotificationCompat.Builder(getApplication())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("OUDA")
                            .setContentText(toSend)
                            .extend(new NotificationCompat.WearableExtender().setHintShowBackgroundOnly(true))
                            .build();
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplication());
                    int notificationId = 1;
                    notificationManager.notify(notificationId, notification);
                }
            });
        }
    };*/

}


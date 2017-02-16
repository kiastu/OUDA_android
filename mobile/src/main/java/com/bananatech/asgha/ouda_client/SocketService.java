package com.bananatech.asgha.ouda_client;

import android.app.IntentService;
import android.app.Service;
import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;

import com.bananatech.asgha.ouda_client.R;
import com.github.nkzawa.emitter.Emitter;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class SocketService extends Service {
    private Socket mSocket;

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){

        try {
            mSocket = IO.socket("http://ouda-server.herokuapp.com:80");
        } catch (URISyntaxException e) {
        }

        mSocket.on("notify", onNotify);
        mSocket.connect();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("notify", onNotify);
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    private Emitter.Listener onNotify = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
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
    };

}
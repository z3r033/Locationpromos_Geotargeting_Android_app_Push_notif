package com.appandroid.locationpromos.geofence;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.appandroid.locationpromos.R;
import com.appandroid.locationpromos.UserActivity;

public class LocationService extends Service {


    private static final String CHANNEL_ID ="channel 1" ;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       Intent notificationIntent = new Intent(this, UserActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Location Alarm")
                .setContentText("You reached the location.")
                .setSmallIcon(R.drawable.ic_baseline_add_24)
                .setContentIntent(pendingIntent)
                .build();

        new DownloadFilesTask().execute();

        startForeground(1, notification);
        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadFilesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
          //  getLocation();
            return null;
        }

    }

}
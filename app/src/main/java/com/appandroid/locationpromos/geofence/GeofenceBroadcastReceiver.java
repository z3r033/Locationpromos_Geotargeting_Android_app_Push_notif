package com.appandroid.locationpromos.geofence;

import android.app.ListActivity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.appandroid.locationpromos.R;
import com.appandroid.locationpromos.Settings;
import com.appandroid.locationpromos.UserActivity;
import com.appandroid.locationpromos.database.dbhelper;
import com.appandroid.locationpromos.models.BrandModel;
import com.appandroid.locationpromos.utils.BitmapConvert;
import com.appandroid.locationpromos.utils.NotificationUtils;
import com.appandroid.locationpromos.utils.SharedPref;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationServices;

import java.util.List;

public class GeofenceBroadcastReceiver  extends BroadcastReceiver {

    private static final String CHANNEL_ID ="channel 1" ;
    private NotificationUtils mNotificationUtils;
    private String audioFile;
    private dbhelper dbhelper;
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            dbhelper = new dbhelper(context);
            mNotificationUtils= new NotificationUtils(context);;
            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
            if (geofencingEvent.hasError()) {
                String errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.getErrorCode());
                Log.e("TAG", errorMessage);
                return;
            }

            // Get the transition type.
            int geofenceTransition = geofencingEvent.getGeofenceTransition();

            // Test that the reported transition was of interest.
            if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                    geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

                // Get the geofences that were triggered. A single event can trigger
                // multiple geofences.
                List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
                String locId = triggeringGeofences.get(0).getRequestId();
                sendNotification(locId, context);
                String[] split= locId.split("/");
                int userid = Integer.valueOf(split[0]);
                int brandId = Integer.valueOf(split[1]);
                BrandModel model = dbhelper.getBrandById(brandId);
                Boolean Check = Boolean.valueOf(SharedPref.readSharedSetting(context,"silent","False"));
               if(!Check) {
                    Notification.Builder nb = mNotificationUtils.getAndroidChannelNotification("Location Alarm:\nBrand title " + model.getBrand_title(), "Notification text : \n" + model.getNotification_text());
                    Bitmap bm = BitmapConvert.getImage(model.getBrand_image());
                    nb.setLargeIcon(bm);
                    PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                            new Intent(context, UserActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                    nb.setContentIntent(contentIntent);
                    mNotificationUtils.getManager().notify(101, nb.build());
                }
                Toast.makeText(context,"...",Toast.LENGTH_LONG).show();
                Intent serviceIntent = new Intent(context, LocationServices.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent);
                }else{
                    context.startService(serviceIntent);
                }
            } else {
                // Log the error.
                Log.e("TAG", "Error");
            }
        }

        private void sendNotification(String locId, Context context) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_add_24)
                    .setContentTitle("Location Reached")
                    .setContentText(" you reached " + locId)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setAutoCancel(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(1, builder.build());
        }


}

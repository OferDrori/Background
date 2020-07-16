package com.guy.backgroundgps;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Locale;

public class LocationService extends Service {

    public static int NOTIFICATION_ID = 153;
    private int lastShownNotificationId = -1;
    public static String CHANNEL_ID = "com.guy.backgroundgps.CHANNEL_ID_FOREGROUND";
    public static String MAIN_ACTION = "com.guy.backgroundgps.locationservice.action.main";

    public static final String START_FOREGROUND_SERVICE = "START_FOREGROUND_SERVICE";
    public static final String PAUSE_FOREGROUND_SERVICE = "PAUSE_FOREGROUND_SERVICE";
    public static final String STOP_FOREGROUND_SERVICE = "STOP_FOREGROUND_SERVICE";

    private NotificationCompat.Builder notificationBuilder;

    private boolean isServiceRunningRightNow = false;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;//FSDFSD
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("User");
    private LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (locationResult.getLastLocation() != null) {
                newLocation(locationResult.getLastLocation());
            } else {
                Log.d("pttt", "Location information isn't available.");
            }
        }

        @Override
        public void onLocationAvailability(LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);
            Toast.makeText(LocationService.this, "Loc= " + locationAvailability.isLocationAvailable(), Toast.LENGTH_SHORT).show();
            locationAvailability.isLocationAvailable();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(START_FOREGROUND_SERVICE)) {
            if (isServiceRunningRightNow) {
                return START_STICKY;
            }

            isServiceRunningRightNow = true;
            notifyToUserForForegroundService();
            startRecording();
        } else if (intent.getAction().equals(PAUSE_FOREGROUND_SERVICE)) {

        } else if (intent.getAction().equals(STOP_FOREGROUND_SERVICE)) {
            stopRecording();
            stopForeground(true);
            stopSelf();
            isServiceRunningRightNow = false;
            return START_NOT_STICKY;
        }
        return START_STICKY;
    }

    private void newLocation(Location lastLocation) {
        Intent intent = new Intent(MainActivity.BROADCAST_NEW_LOCATION_DETECTED);

        String json = new Gson().toJson(new MyLoc(lastLocation));
        intent.putExtra("EXTRA_LOCATION", json);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        notificationBuilder.setContentText("Current speed: " + String.format("%.2f", lastLocation.getSpeed() * 3.6) + " kph");
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void stopRecording() {
        if (fusedLocationProviderClient != null) {
            Task<Void> task = fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            task.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("pttt", "stop Location Callback removed.");
                        stopSelf();
                    } else {
                        Log.d("pttt", "stop Failed to remove Location Callback.");
                    }
                }
            });
        }
    }

    private void startRecording() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationRequest = new LocationRequest();
            locationRequest.setSmallestDisplacement(1.0f);
            locationRequest.setInterval(5000);//
            locationRequest.setFastestInterval(5000);
            //locationRequest.setMaxWaitTime(TimeUnit.MINUTES.toMillis(2));
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onDestroy() {
        Log.d("pttt", "LocationService - onDestroy");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("pttt", "LocationService - onBind");
        // Used only in case of bound services.
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("pttt", "LocationService - onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("pttt", "LocationService - onTaskRemoved");
        super.onTaskRemoved(rootIntent);
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        Log.d("pttt", "LocationService - dump");
        super.dump(fd, writer, args);
    }



    // // // // // // // // // // // // // // // // Notification  // // // // // // // // // // // // // // //


    private void notifyToUserForForegroundService() {
        // On notification click
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder = getNotificationBuilder(this,
                CHANNEL_ID,
                NotificationManagerCompat.IMPORTANCE_LOW); //Low importance prevent visual appearance for this notification channel on top

        notificationBuilder.setContentIntent(pendingIntent) // Open activity
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_sattelite)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                .setContentTitle("App in progress")
                .setContentText("Content")
        ;

        Notification notification = notificationBuilder.build();

        startForeground(NOTIFICATION_ID, notification);

        if (NOTIFICATION_ID != lastShownNotificationId) {
            // Cancel previous notification
            final NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
            notificationManager.cancel(lastShownNotificationId);
        }
        lastShownNotificationId = NOTIFICATION_ID;
    }

    public static NotificationCompat.Builder getNotificationBuilder(Context context, String channelId, int importance) {
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            prepareChannel(context, channelId, importance);
            builder = new NotificationCompat.Builder(context, channelId);
        } else {
            builder = new NotificationCompat.Builder(context);
        }
        return builder;
    }

    @TargetApi(26)
    private static void prepareChannel(Context context, String id, int importance) {
        final String appName = context.getString(R.string.app_name);
        String notifications_channel_description = "Cycling map channel";
        String description = notifications_channel_description;
        final NotificationManager nm = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);

        if(nm != null) {
            NotificationChannel nChannel = nm.getNotificationChannel(id);

            if (nChannel == null) {
                nChannel = new NotificationChannel(id, appName, importance);
                nChannel.setDescription(description);

                // from another answer
                nChannel.enableLights(true);
                nChannel.setLightColor(Color.BLUE);

                nm.createNotificationChannel(nChannel);
            }
        }
    }
}

/*
Method to detect if user has selected “Don’t Ask Again” while requesting for permission:
https://blog.usejournal.com/method-to-detect-if-user-has-selected-dont-ask-again-while-requesting-for-permission-921b95ded536

Android Turn on GPS programmatically:
https://medium.com/@droidbyme/android-turn-on-gps-programmatically-d585cf29c1ef

Get Current location using FusedLocationProviderClient in Android:
https://medium.com/@droidbyme/get-current-location-using-fusedlocationproviderclient-in-android-cb7ebf5ab88e

Full library that implements location and permission:
https://github.com/klaasnotfound/LocationAssistant/tree/master/app/src/main/res/values
 */
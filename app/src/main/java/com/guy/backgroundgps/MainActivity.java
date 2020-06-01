package com.guy.backgroundgps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private final int LOCATION_PERMISSIONS_REQUEST_CODE = 125;

    private AppCompatButton main_BTN_start;
    private AppCompatButton main_BTN_pause;
    private AppCompatButton main_BTN_stop;
    private AppCompatButton main_BTN_info;

    private int clock = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("pttt", "MainActivity - onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        findViews();
        initViews();

//        MyClockTickerV4.CycleTicker clockRefresh = new MyClockTickerV4.CycleTicker() {
//            @Override
//            public void secondly(int repeatsRemaining) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d("pttt", "Clock= " + clock++);
//                    }
//                });
//            }
//
//            @Override
//            public void done() {
//
//            }
//        };
//        MyClockTickerV4.getInstance().addCallback(clockRefresh, MyClockTickerV4.CONTINUOUSLY_REPEATS, 1000);
//
//        MyClockTickerV4.getInstance().removeCallback(clockRefresh);

//        new Timer().schedule(
//                new java.util.TimerTask() {
//                    @Override
//                    public void run() {
//                        Log.d("pttt", "Clock= " + clock++);
//                    }
//                },
//                0, 1000
//        );

//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                Log.d("pttt", "Clock= " + clock++);
//            }
//        }, 0, 1000);

//        final Handler myHandler = new Handler();
//        myHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                myHandler.postDelayed(this, 1000);
//                Log.d("pttt", "Clock= " + clock++);
//            }
//        }, 1000);


        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION
                        ,Manifest.permission.ACCESS_FINE_LOCATION
                        ,Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        ,Manifest.permission.FOREGROUND_SERVICE
                },
                LOCATION_PERMISSIONS_REQUEST_CODE);

        validateButtons();
    }

    private void validateButtons() {
        if (isMyServiceRunning(LocationService.class)) {
            main_BTN_start.setEnabled(false);
            main_BTN_pause.setEnabled(true);
            main_BTN_stop.setEnabled(true);
        } else {
            main_BTN_start.setEnabled(true);
            main_BTN_pause.setEnabled(false);
            main_BTN_stop.setEnabled(false);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        int counter = 0;
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> runs = manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                counter++;
                //return true;
            }
        }

        Log.d("pttt", "Counter= " + counter);
        if (counter > 0)
            return true;
        return false;
    }



    private void startService() {
        actionToService(LocationService.START_FOREGROUND_SERVICE);
        validateButtons();
    }

    private void pauseService() {
        actionToService(LocationService.PAUSE_FOREGROUND_SERVICE);
        validateButtons();
    }

    private void stopService() {
        actionToService(LocationService.STOP_FOREGROUND_SERVICE);
        validateButtons();
    }

    private void actionToService(String action) {
        Intent startIntent = new Intent(MainActivity.this, LocationService.class);
        startIntent.setAction(action);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(startIntent);
            // or
            //ContextCompat.startForegroundService(this, startIntent);
        } else {
            startService(startIntent);
        }
    }




    // // // // // // // // // // // // // // // // Permissions  // // // // // // // // // // // // // // //

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(MainActivity.this, "Result code = " + grantResults[0], Toast.LENGTH_SHORT).show();

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void openAppSettingsManually() {
        Intent intent = new Intent();
        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    // // // // // // // // // // // // // // // // Views  // // // // // // // // // // // // // // //

    private void initViews() {
        main_BTN_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }
        });

        main_BTN_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseService();
            }
        });

        main_BTN_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
                validateButtons();
            }
        });

        main_BTN_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateButtons();
            }
        });
    }

    private void findViews() {
        main_BTN_start = findViewById(R.id.main_BTN_start);
        main_BTN_pause = findViewById(R.id.main_BTN_pause);
        main_BTN_stop = findViewById(R.id.main_BTN_stop);
        main_BTN_info = findViewById(R.id.main_BTN_info);
    }
}

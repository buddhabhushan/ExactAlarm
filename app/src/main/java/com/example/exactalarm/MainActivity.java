package com.example.exactalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ToggleButton alarmToggle;

    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";

    NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmToggle = findViewById(R.id.alarmToggle);

        final long triggerTime = 1110;

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);



        Intent notifyIntent = new Intent(this, AlarmReceiver.class);

        // See if the alarm is already set up or not
        boolean alarmUp = (PendingIntent.getBroadcast(this,
                NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_NO_CREATE) != null);

        alarmToggle.setChecked(alarmUp);

        // Setting the pending intent for the alarm
        final PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(
                this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final AlarmManager alarmManager = (AlarmManager)getSystemService(
                ALARM_SERVICE);

        alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    if(alarmManager != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR, 11);
                            calendar.set(Calendar.MINUTE, 11);
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                                    calendar.getTimeInMillis(), alarmPendingIntent);
                        }
                    }
                }
                else {
                    if(alarmManager != null) {
                        alarmManager.cancel(alarmPendingIntent);
                        mNotificationManager.cancelAll();
                    }
                }
                Toast.makeText(MainActivity.this, "toastMessage", Toast.LENGTH_SHORT).show();

            }
        });

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create Notification Channel
            NotificationChannel notificationChannel = new NotificationChannel(
                    PRIMARY_CHANNEL_ID, "Exact Alarm Notification", NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setDescription("This notification triggers exactly at 11:11 AM");

            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
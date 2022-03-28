package com.tuinite.timemanagement.boardcast_receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import static com.tuinite.timemanagement.boardcast_receivers.ReminderBoardcastReceiver.ringtone;

public class Notification_cancel_receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("DATE_TIME", 0);
        try {
            if (ringtone.isPlaying() == true) {
                ringtone.stop();
                Log.i("STOP_RINTONE", "Ringtone Stopped");
            } else {
                Log.i("STOP_RINTONE", "Ringtone Not Stopped");
            }
        } catch (Exception e) {

        }

        Log.i("NOTIFICATION_CANCEL 1", String.valueOf(id));


        Intent cancelIntent = new Intent(context, ReminderBoardcastReceiver.class);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, id, cancelIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(cancelPendingIntent);
        try {
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.cancel(id);
        } catch (Exception e) {
            Log.i("NOTIFICATION_CANCEL 2", String.valueOf(e));
        }


    }
}

package com.tuinite.timemanagement.boardcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

import static com.tuinite.timemanagement.boardcast_receivers.RoutineBroadcastReceiver.ringtone;

public class SnoozeRoutineItemBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ringtone.isPlaying()) {
            ringtone.stop();
        }

        int pendingIntentId = intent.getIntExtra("PENDING_INTENT_ID", 0);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.cancel(pendingIntentId);

     /*   Intent notificationIntent = new Intent(context, RoutineBroadcastReceiver.class);

      //  notificationIntent.putExtra("ITEM_TITLE", routineItems.get(i).getItemTitle());
        notificationIntent.putExtra("PENDING_INTENT_ID", (int) 1);
      //  notificationIntent.putExtra("ROUTINE_ALARM", routineItems.get(i).getItemAlarm());


        PendingIntent notificationPendingIntent = PendingIntent.getBroadcast(context, (int) 1, notificationIntent, 0);


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        alarmManager.set(AlarmManager.RTC_WAKEUP, notification_time, notificationPendingIntent);*/
    }
}

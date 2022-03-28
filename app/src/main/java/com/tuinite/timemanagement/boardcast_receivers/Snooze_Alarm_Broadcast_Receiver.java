package com.tuinite.timemanagement.boardcast_receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

import static com.tuinite.timemanagement.boardcast_receivers.ReminderBoardcastReceiver.ringtone;

public class Snooze_Alarm_Broadcast_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra("DATE_TIME", 0);
        String title = intent.getStringExtra("TITLE");
        Boolean alarm = intent.getBooleanExtra("ROUTINE_ALARM", false);
        if (ringtone.isPlaying()) {
            ringtone.stop();
        }

        Intent reIntent = new Intent(context, ReminderBoardcastReceiver.class);
        PendingIntent rePendingIntent = PendingIntent.getBroadcast(context, id, reIntent, 0);
        AlarmManager alarmManager1 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager1.cancel(rePendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.cancel(id);


        Intent alarmIntent = new Intent(context, ReminderBoardcastReceiver.class);
        alarmIntent.putExtra("TITLE", title);
        alarmIntent.putExtra("DATE_TIME", id);
        alarmIntent.putExtra("ALARM", alarm);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT < 23) {
            if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 1000 * 60 * 1, pendingIntent);

            } else {
                // alarmManager.set(AlarmManager.RTC_WAKEUP, alramTime,1000, pendingIntent);
                alarmManager.set(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 1000 * 60 * 1, pendingIntent);
                //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,alramTime,1000*60*5,pendingIntent);
            }
        } else {
            //  alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,alramTime,1000*60*5,pendingIntent);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 1000 * 60 * 1, pendingIntent);
        }
    }
}

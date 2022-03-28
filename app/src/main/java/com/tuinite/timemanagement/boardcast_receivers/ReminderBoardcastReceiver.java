package com.tuinite.timemanagement.boardcast_receivers;


import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.tuinite.timemanagement.R;


public class ReminderBoardcastReceiver extends BroadcastReceiver {
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
    static Ringtone ringtone;

    @Override
    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("TITLE");
        Boolean alarm = intent.getBooleanExtra("ALARM", false);
        int id = intent.getIntExtra("DATE_TIME", 0);
        //  final Uri currentTone = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM);
        if (alarm == true) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
            ringtone.play();
            Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Alarm. Faks...", Toast.LENGTH_LONG).show();
        }

        //Cancel Reminder Notification
        Intent calcelIntent = new Intent(context, Notification_cancel_receiver.class);
        calcelIntent.putExtra("DATE_TIME", id);
        PendingIntent cancelPedingIntent = PendingIntent.getBroadcast(context, id, calcelIntent, 0);

        //Snooze Reminder Notification
        Intent snoozeIntent = new Intent(context, Snooze_Alarm_Broadcast_Receiver.class);
        snoozeIntent.putExtra("DATE_TIME", id);
        snoozeIntent.putExtra("TITLE", title);
        snoozeIntent.putExtra("ROUTINE_ALARM", alarm);
        PendingIntent snoozePedingIntent = PendingIntent.getBroadcast(context, id, snoozeIntent, 0);


        String GROUP_KEY_REMIANDER_NOTIFICATION = "com.android.example.REMINDER_NOTIFICATION";


        // RemoteViews notificationView= new RemoteViews(context.getPackageName(),R.layout.remainder_notification);

        //   notificationView.setTextViewText(R.id.notification_rem_title,title);
//
        //  notificationView.setOnClickPendingIntent(R.id.notification_rem_done,cancelPedingIntent);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "REMINDER_ALARM")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("You have a reminder")
                .setContentText(title)
                //   .setCustomContentView(notificationView)
                .setGroup(GROUP_KEY_REMIANDER_NOTIFICATION)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .addAction(R.drawable.add_icon, "Cancel", cancelPedingIntent)
                .addAction(R.drawable.account_icon, "Snooze", snoozePedingIntent)
                .setColor(Color.YELLOW)
                .setAutoCancel(true)


                // .setSound(currentTone)
                .setPriority(NotificationCompat.PRIORITY_HIGH);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(id, builder.build());


    }


}







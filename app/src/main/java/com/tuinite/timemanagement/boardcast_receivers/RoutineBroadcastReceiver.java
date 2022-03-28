package com.tuinite.timemanagement.boardcast_receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.tuinite.timemanagement.R;

public class RoutineBroadcastReceiver extends BroadcastReceiver {
    static Ringtone ringtone;
    public final int ROUTINE_END_TASK_CODE = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        int taskId = intent.getIntExtra("TASK_ID", 0);
        int routineId = intent.getIntExtra("ROUTINE_ID", 0);
        String taskTitle = intent.getStringExtra("TASK_TITLE");
        Boolean alarm = intent.getBooleanExtra("TASK_ALARM", false);
        int taskCode = intent.getIntExtra("TASK_CODE", 0);

        if (alarm) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
            ringtone.play();
        }

        String notification_button_text = "Skip";
        if (taskCode == 1) {
            notification_button_text = "Done";
        }


        Intent view = new Intent(context, doneRoutineTaskBroadcastReceiver.class);
        view.putExtra("TASK_ID", taskId);
        view.putExtra("ROUTINE_ID", routineId);
        view.putExtra("TASK_CODE", taskCode);
        PendingIntent viewPendingIntent = PendingIntent.getBroadcast(context, taskId, view, 0);


        Intent skipIntent = new Intent(context, SkipRoutineTaskBroadCastReceiver.class);
        skipIntent.putExtra("TASK_ID", taskId);
        skipIntent.putExtra("ROUTINE_ID", routineId);
        PendingIntent skipPedingIntent = PendingIntent.getBroadcast(context, taskId, skipIntent, 0);

        String GROUP_KEY_ROUTINE_NOTIFICATION = "com.tuinite.ROUTINE_NOTIFICATION";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ROUTINE_ALARM")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Routine Alert")
                .setContentText(taskTitle)
                //   .setCustomContentView(notificationView)
                .setGroup(GROUP_KEY_ROUTINE_NOTIFICATION)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                //   .addAction(R.drawable.add_icon,"Skip",skipPedingIntent)
                .addAction(R.drawable.clock_icon, notification_button_text, skipPedingIntent)
                .addAction(R.drawable.account_icon, "View", viewPendingIntent)
                .setColor(Color.YELLOW)
                .setAutoCancel(true)
                // .setSound(currentTone)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(taskId, builder.build());
    }

}

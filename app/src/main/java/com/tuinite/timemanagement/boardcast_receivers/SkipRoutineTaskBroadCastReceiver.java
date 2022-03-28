package com.tuinite.timemanagement.boardcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

import static com.tuinite.timemanagement.boardcast_receivers.RoutineBroadcastReceiver.ringtone;

public class SkipRoutineTaskBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ringtone.isPlaying()) {
            ringtone.stop();
        }

        int taskId = intent.getIntExtra("TASK_ID", 0);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.cancel(taskId);
    }
}

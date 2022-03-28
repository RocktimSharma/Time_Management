package com.tuinite.timemanagement.Notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.tuinite.timemanagement.boardcast_receivers.RoutineBroadcastReceiver;
import com.tuinite.timemanagement.entities.Routine;
import com.tuinite.timemanagement.entities.RoutineTasks;
import com.tuinite.timemanagement.relations.RoutineWithRoutineTask;
import com.tuinite.timemanagement.viewmodels.RoutineViewmodel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RoutineNotification {
    private Context context;

    private int routineId;
    private Routine routine;

    private List<RoutineTasks> routineTask = new ArrayList<>();
    private List<RoutineWithRoutineTask> routineWithRoutineTask = new ArrayList<>();

    public RoutineNotification(Context context, int routineId) {
        this.context = context;
        this.routineId = routineId;

     /*   RoutineTaskViewModel routineTaskViewModel=ViewModelProviders.of((FragmentActivity) context).get(RoutineTaskViewModel.class);
        routineTaskViewModel.getRoutineTasks(routineId).observe((LifecycleOwner) context, new Observer<List<RoutineTasks>>() {
            @Override
            public void onChanged(List<RoutineTasks> routineTasks) {

                routineTask=routineTasks;
            }
        });*/

        RoutineViewmodel routineViewmodel = ViewModelProviders.of((FragmentActivity) context).get(RoutineViewmodel.class);
        routineViewmodel.getRoutinewithRoutineTasks(routineId).observe((LifecycleOwner) context, new Observer<List<RoutineWithRoutineTask>>() {
            @Override
            public void onChanged(List<RoutineWithRoutineTask> routineWithRoutineTasks) {
               /* for(int i=0;i<routineWithRoutineTasks.get(0).routineTasksList.size();i++){
                  //  Log.i("ROUTINE_NOTIFICATION 3",String.valueOf(routineWithRoutineTasks.get(i).routine.getRoutineTitle()));
                    Log.i("ROUTINE_NOTIFICATION 4",String.valueOf(routineWithRoutineTasks.get(0).routineTasksList.get(i).getTaskTitle()));

                }*/
                routineWithRoutineTask = routineWithRoutineTasks;
                createRoutineNotificationChannel();
                scheduleNotification();

            }
        });

    }


    //Creating the notification channel
    private void createRoutineNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final Uri currentTone = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);
            CharSequence name = "Routine Channel";
            String description = "Channel for routine";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel routineChannel = new NotificationChannel("ROUTINE_ALARM", name, importance);
            routineChannel.setDescription(description);
            // channel.setSound(currentTone);
            routineChannel.enableVibration(false);
            //  channel.setVibrationPattern(new long[]{1000,2000});
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(routineChannel);

        }
    }


    //Sheduling Notification and alarm
    private void scheduleNotification() {
        //   Log.i("scheduleNotification",String.valueOf(true));
        //   Log.i("Working",String.valueOf(routineDays.sun));

        routine = routineWithRoutineTask.get(0).routine;
        routineId = routine.getRoutineId();

        if (routine.getRoutineDays().sun == true) {
            setNotification(Calendar.SUNDAY);
        }
        if (routine.getRoutineDays().mon == true) {
            setNotification(Calendar.MONDAY);
        }
        if (routine.getRoutineDays().tue == true) {
            setNotification(Calendar.TUESDAY);
        }
        if (routine.getRoutineDays().wed == true) {
            setNotification(Calendar.WEDNESDAY);
        }
        if (routine.getRoutineDays().thu == true) {
            setNotification(Calendar.THURSDAY);
        }
        if (routine.getRoutineDays().fri == true) {
            setNotification(Calendar.FRIDAY);
        }
        if (routine.getRoutineDays().sat == true) {
            setNotification(Calendar.SATURDAY);
        }


    }


    private void setNotification(int day) {

        routineTask = routineWithRoutineTask.get(0).routineTasksList;
        Calendar routineCalender = Calendar.getInstance();
        for (int i = 0; i < routineTask.size(); i++) {
            Date taskStartTime = routineTask.get(i).getTaskStartTime();
            routineCalender.setTime(taskStartTime);
            routineCalender.set(Calendar.DAY_OF_WEEK, day);
            routineCalender.set(Calendar.SECOND, 0);
            routineCalender.set(Calendar.MILLISECOND, 0);

            Log.i("ROUTINE_NOTIFICATION 1", String.valueOf(routineCalender.getTimeInMillis()));
            // Check we aren't setting it in the past which would trigger it to fire instantly
            if (routineCalender.getTimeInMillis() < System.currentTimeMillis()) {
                routineCalender.add(Calendar.DAY_OF_YEAR, 7);
                Log.i("ROUTINE_NOTIFICATION 2", String.valueOf(routineCalender.getTimeInMillis()));
            }

            long notification_time = routineCalender.getTimeInMillis();
            int id = routineTask.get(i).getTaskId();

            Intent notificationIntent = new Intent(context, RoutineBroadcastReceiver.class);


            notificationIntent.putExtra("TASK_ID", (int) id);
            notificationIntent.putExtra("ROUTINE_ID", routineId);
            notificationIntent.putExtra("TASK_TITLE", routineTask.get(i).getTaskTitle());
            notificationIntent.putExtra("TASK_ALARM", routineTask.get(i).getTaskAlarm());


            PendingIntent notificationPendingIntent = PendingIntent.getBroadcast(context, id, notificationIntent, 0);


            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


            alarmManager.set(AlarmManager.RTC_WAKEUP, notification_time, notificationPendingIntent);
            //   alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,notification_time,AlarmManager.INTERVAL_DAY*7,notificationPendingIntent);


        }
    }
}
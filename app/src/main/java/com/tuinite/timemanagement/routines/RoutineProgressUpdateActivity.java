package com.tuinite.timemanagement.routines;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tuinite.timemanagement.R;
import com.tuinite.timemanagement.boardcast_receivers.RoutineBroadcastReceiver;
import com.tuinite.timemanagement.entities.RoutineProgress;
import com.tuinite.timemanagement.entities.RoutineTasks;
import com.tuinite.timemanagement.viewmodels.RoutineProgressViewModel;
import com.tuinite.timemanagement.viewmodels.RoutineTaskViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;


public class RoutineProgressUpdateActivity extends AppCompatActivity {

    Date nextTaskStartTime;
    int nextTaskId;
    Boolean nextTaskAlarm;
    long[] pattern = {
            0,  // Start immediately
            1000, 200, 1000, 200, 1000, 200

    };
    Boolean isCoundown;
    private List<RoutineTasks> routineTask = new ArrayList<>();
    private int taskId;
    private String taskTitle;
    private ProgressBar progressBar;
    private TextView countdown_tv;
    private TextView status_tv;
    private Button skip_btn;
    private Button start_btn;
    private Button end_btn;
    private Button extendTime_btn;
    private Button done_btn;
    private Switch focus_sw;
    private TextView info_tv;
    private RoutineTaskViewModel routineTaskViewModel;
    private RoutineProgressViewModel routineProgressViewModel;
    private CountDownTimer countDownTimer;
    private Date startTime;
    private Date endTime;
    private Calendar calendar;
    private Date progressStartTime;
    private Date progressDate;
    private Vibrator vibrator;
    private TextView updateStatus;
    private NotificationManagerCompat notificationManagerCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_progress_update);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#121212"));
            // getSupportActionBar().hide();
        }


        TextView tasktitle_tv = findViewById(R.id.routine_progress_update_task_title_tv);
        countdown_tv = findViewById(R.id.routine_progress_update_task_countdown_tv);
        TextView taskStartTime_tv = findViewById(R.id.routine_progress_update_task_start_time_tv);
        TextView taskEndTime_tv = findViewById(R.id.routine_progress_update_task_end_time_tv);
        updateStatus = findViewById(R.id.routine_progress_update_status_tv);

        progressBar = findViewById(R.id.routine_progress_update_task_progress_bar);

        status_tv = findViewById(R.id.routine_progress_update_status_tv);

        skip_btn = findViewById(R.id.routine_progress_update_task_skip_btn);
        skip_btn.setOnClickListener(v -> {
            finishAffinity();
        });

        start_btn = findViewById(R.id.routine_progress_update_task_start_now_btn);
        start_btn.setOnClickListener(v -> insertProgress());

        end_btn = findViewById(R.id.routine_progress_update_task_end_btn);
        end_btn.setVisibility(GONE);
        end_btn.setOnClickListener(v -> {
            countDownTimer.cancel();
            Calendar c = Calendar.getInstance();
            updateEndTime(c.getTimeInMillis());
            finishAffinity();
            cancelAlarm(taskId, false);

        });

        extendTime_btn = findViewById(R.id.routine_progress_update_task_extend_time_btn);
        extendTime_btn.setVisibility(GONE);
        extendTime_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                long difference = (nextTaskStartTime.getTime() - calendar.getTimeInMillis()) / (60 * 1000);
                if (difference <= 15) {
                    try {
                        Dialog dialog = new BottomSheetDialog(RoutineProgressUpdateActivity.this);
                        LayoutInflater layoutInflater = getLayoutInflater();
                        View view = layoutInflater.inflate(R.layout.routine_bottom_sheet_dialog, null);
                        dialog.setContentView(view);
                        dialog.show();

                        TextView nextTaskTime = view.findViewById(R.id.routine_bottom_sheet_dialog_next_task_tv);
                        nextTaskTime.setText("Your next task is from " + nextTaskStartTime);

                        Button yes = view.findViewById(R.id.routine_bottom_sheet_dialog_yes_btn);
                        yes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelAlarm(nextTaskId, nextTaskAlarm);
                                dialog.cancel();
                                long newEndTime = calendar.getTimeInMillis() + 900000;
                                updateEndTime(newEndTime);
                                updateUI(900000);
                            }
                        });

                        Button no = view.findViewById(R.id.routine_bottom_sheet_dialog_no_btn);
                        no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finishAffinity();
                            }
                        });


                    } catch (Exception e) {
                        Log.i("PROGRESS_UPDATE 22", "FAILED" + e);
                    }
                } else {
                    long newEndTime = calendar.getTimeInMillis() + 900000;
                    updateEndTime(newEndTime);
                    updateUI(900000);
                    scheduleEndNotification(newEndTime);
                }


            }
        });

        done_btn = findViewById(R.id.routine_progress_update_task_done_btn);
        done_btn.setVisibility(GONE);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        focus_sw = findViewById(R.id.routine_progress_update_task_focus_sw);
        focus_sw.setChecked(true);
        info_tv = findViewById(R.id.routine_progress_update_info_tv);
        focus_sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                info_tv.setVisibility(View.VISIBLE);
            } else {
                info_tv.setVisibility(GONE);
            }
        });


        Intent intent = getIntent();
        createNotificationChannel();

        int taskCode = intent.getIntExtra("TASK_CODE", 0);

        taskId = intent.getIntExtra("TASK_ID", 0);
        Log.i("PROGRESS_UPDATE 21", String.valueOf(taskId));
        if (taskId != 0) {

            try {
                routineTaskViewModel = ViewModelProviders.of(this).get(RoutineTaskViewModel.class);
                routineTaskViewModel.getSingleRoutineTask(taskId).observe(this, new Observer<List<RoutineTasks>>() {
                    @Override
                    public void onChanged(List<RoutineTasks> routineTasks) {
                        routineTask = routineTasks;
                        taskTitle = routineTask.get(0).getTaskTitle();
                        tasktitle_tv.setText(taskTitle);
                        endTime = routineTask.get(0).getTaskEndTime();
                        startTime = routineTask.get(0).getTaskStartTime();
                        long dif = (Math.abs(Calendar.getInstance().getTimeInMillis() - startTime.getTime())) / 1000 * 120;
                        Log.i("ROUTINE DIF", String.valueOf(dif));
                        if (dif > 2) {
                            updateStatus.setText("Oh! You are late");
                        } else {
                            updateStatus.setText("Bravo! You are on time");
                        }
                        String taskStartTime = String.valueOf(DateFormat.format("hh:mm aa", startTime));
                        String taskEndTime = String.valueOf(DateFormat.format("hh:mm aa", endTime));
                        taskStartTime_tv.setText(taskStartTime);
                        taskEndTime_tv.setText(taskEndTime);
                        calendar = Calendar.getInstance();
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        progressStartTime = calendar.getTime();
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        progressDate = calendar.getTime();
                        if (taskCode == 1) {
                            onFinishUpdateUI();
                        }
                    }
                });


            } catch (Exception e) {
                Log.i("PROGRESS_UPDATE 2", "FAILED" + e);
            }
        } else {
            Log.i("PROGRESS_UPDATE 3", String.valueOf(taskId));
        }


    }

    private void cancelAlarm(int id, Boolean alarm) {


        try {
            if (alarm) {
                Intent cancelIntent = new Intent(this, RoutineBroadcastReceiver.class);
                PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(this, id, cancelIntent, 0);
                AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(cancelPendingIntent);
            }

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.cancel(id);
        } catch (Exception e) {
            Log.i("PROGRESS_UPDATE 23", "FAILED" + e);
        }
    }

    private void updateEndTime(long endTime) {
        calendar.setTimeInMillis(endTime);
        routineProgressViewModel.updateEndTime(calendar.getTime(), progressDate, taskId);
    }

    private void insertProgress() {


        calendar.set(Calendar.HOUR_OF_DAY, endTime.getHours());
        calendar.set(Calendar.MINUTE, endTime.getMinutes());

        Date progressEndTime = calendar.getTime();

        calendar = Calendar.getInstance();
        if (calendar.getTimeInMillis() < progressEndTime.getTime()) {

            int routineId = routineTask.get(0).getTaskRoutineId();
            try {
                RoutineProgress routineProgress = new RoutineProgress(progressDate, routineId, taskId, progressStartTime, progressEndTime);
                routineProgressViewModel = ViewModelProviders.of(this).get(RoutineProgressViewModel.class);
                routineProgressViewModel.routineProgressInsertion(routineProgress);

            } catch (Exception e) {
                Log.i("PROGRESS_UPDATE 4", "FAILED" + e);
            }
            calendar.setTime(endTime);
            scheduleEndNotification(calendar.getTimeInMillis());
            updateUI(0);
        } else {
            info_tv.setText("Task end time exceed");
        }

    }

    private void scheduleEndNotification(long newEndTime) {

        Intent notificationIntent = new Intent(this, RoutineBroadcastReceiver.class);


        notificationIntent.putExtra("TASK_ID", taskId);
        notificationIntent.putExtra("TASK_TITLE", taskTitle);


        PendingIntent notificationPendingIntent = PendingIntent.getBroadcast(this, taskId, notificationIntent, 0);


        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);


        alarmManager.set(AlarmManager.RTC_WAKEUP, newEndTime, notificationPendingIntent);

    }

    private void updateUI(long coundown) {

        if (focus_sw.isChecked()) {
            start_btn.setVisibility(GONE);
            skip_btn.setVisibility(GONE);
            end_btn.setVisibility(View.VISIBLE);
            extendTime_btn.setVisibility(GONE);
            done_btn.setVisibility(GONE);
            calendar = Calendar.getInstance();

            long countdownTime;
            if (coundown == 0) {


                countdownTime = Math.abs(endTime.getTime() - calendar.getTimeInMillis());
            } else {
                countdownTime = coundown;
            }

            isCoundown = true;
            countDownTimer = new CountDownTimer(countdownTime, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    long Mmin = (millisUntilFinished / 1000) / 60;
                    long Ssec = (millisUntilFinished / 1000) % 60;
                    if (Ssec < 10) {
                        countdown_tv.setText("" + Mmin + ":0" + Ssec);
                    } else countdown_tv.setText("" + Mmin + ":" + Ssec);

                    try {
                        int remain = (int) (countdownTime - millisUntilFinished);
                        int progress = (int) (((int) countdownTime - remain) / (int) countdownTime) * 100;

                        float a = (countdownTime - remain);
                        float b = a / countdownTime;
                        float c = b * 100;
                        progressBar.setProgress((int) c);

                        Log.i("PROGRESS_UPDATE 5", String.valueOf(progress));
                        Log.i("PROGRESS_UPDATE 6", "CountDown  " + countdownTime);
                        Log.i("PROGRESS_UPDATE 7", "Remain  " + remain);
                        Log.i("PROGRESS_UPDATE 8", "Milli  " + millisUntilFinished);
                        Log.i("PROGRESS_UPDATE 9", "A  " + a);
                        Log.i("PROGRESS_UPDATE 10", "B  " + b);
                        Log.i("PROGRESS_UPDATE 11", "C  " + c);
                    } catch (Exception e) {
                        Log.i("PROGRESS_UPDATE 12", String.valueOf(e));
                    }
                }

                @Override
                public void onFinish() {
                    isCoundown = false;
                    onFinishUpdateUI();
                }
            }.start();


            vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);


        } else {
            finishAffinity();
        }


    }

    private void onFinishUpdateUI() {
        Calendar calendar = Calendar.getInstance();
        Date currentTaskEndTime = calendar.getTime();


        try {
            routineTaskViewModel.getNextTask(currentTaskEndTime).observe(this, new Observer<List<RoutineTasks>>() {
                @Override
                public void onChanged(List<RoutineTasks> routineTasks) {
                    if (routineTasks.size() != 0) {
                        nextTaskId = routineTasks.get(0).getTaskId();
                        nextTaskStartTime = routineTasks.get(0).getTaskStartTime();
                        nextTaskAlarm = routineTasks.get(0).getTaskAlarm();
                        long differnce = (nextTaskStartTime.getTime() - currentTaskEndTime.getTime()) / (60 * 1000);
                        if (differnce > 20) {
                            status_tv.setText("Next Task is from " + DateFormat.format("hh:mm aa", nextTaskStartTime));
                            end_btn.setVisibility(GONE);
                            extendTime_btn.setVisibility(View.VISIBLE);
                            done_btn.setVisibility(View.VISIBLE);

                        } else {
                            status_tv.setText("Bravo! You have completed your task");
                        }
                    }
                }
            });
        } catch (Exception e) {
            Log.i("PROGRESS_UPDATE 13", String.valueOf(e));
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            NotificationChannel channel = new NotificationChannel("FOCUS_NOTIFICATION", "Focus Channel", NotificationManager.IMPORTANCE_HIGH);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification() {
        try {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "FOCUS_NOTIFICATION")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Focus")
                    .setContentText("You are losing focus")
                    .setDefaults(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_CALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);


            notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(0000001, builder.build());
        } catch (Exception e) {
            Log.i("PROGRESS_UPDATE 14", String.valueOf(e));
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e("lifecycle", "onStart invoked");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("lifecycle", "onResume invoked");
        try {
            vibrator.cancel();
            notificationManagerCompat.cancel(0000001);
        } catch (Exception e) {
            Log.i("PROGRESS_UPDATE 13", String.valueOf(e));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (focus_sw.isChecked() && isCoundown) {
            Log.e("lifecycle", "onPause invoked");
            vibrator.vibrate(pattern, 1);
            showNotification();
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.e("lifecycle", "onStop invoked");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("lifecycle", "onRestart invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vibrator.cancel();
        notificationManagerCompat.cancel(0000001);
    }

}
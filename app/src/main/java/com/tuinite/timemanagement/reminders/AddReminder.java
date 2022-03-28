package com.tuinite.timemanagement.reminders;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tuinite.timemanagement.R;
import com.tuinite.timemanagement.boardcast_receivers.ReminderBoardcastReceiver;
import com.tuinite.timemanagement.entities.Reminder;
import com.tuinite.timemanagement.viewmodels.ReminderViewModel;

import java.util.Calendar;
import java.util.Date;

public class AddReminder extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    protected TextInputEditText et_title;
    //Declaring the required member variables
    Intent intent;
    int UPDATE_CODE = 0;
    int reminderId;
    Calendar calendar;
    String title;
    String date;
    String time;
    String note;
    Boolean alarm;
    Date dateTime;
    int notificationId;
    private ReminderViewModel reminderViewModel;
    private TextView tv_save;
    private ImageButton tv_cancel;
    private TextInputLayout tl_title;
    private TextInputLayout tl_date;
    private TextInputEditText et_date;
    private TextInputLayout tl_time;
    private TextInputEditText et_time;
    private TextInputEditText et_note;
    private SwitchCompat sw_alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        intent = getIntent();

        calendar = Calendar.getInstance();

        // initialising the layout
        tv_cancel = findViewById(R.id.add_reminder_cancel_imbtn);
        tv_save = findViewById(R.id.add_reminder_save_tv);
        tl_title = findViewById(R.id.add_reminder_title_textInput_layout);
        et_title = findViewById(R.id.add_reminder_title_et);

        tl_date = findViewById(R.id.add_reminder_date_textInput_layout);
        et_date = findViewById(R.id.add_reminder_date_et);
        et_date.setOnClickListener(v -> {
            //Time Date Dailog
            DialogFragment datePicker = new com.tuinite.timemanagement.helper_classes.DatePicker();
            Bundle args = new Bundle();
            args.putBoolean("LIMIT", true);
            datePicker.setArguments(args);
            datePicker.show(getSupportFragmentManager(), "Date Picker");
        });


        tl_time = findViewById(R.id.add_reminder_time_textInput_layout);
        et_time = findViewById(R.id.add_reminder_time_et);
        et_time.setOnClickListener(v -> {
            //Time Picker Dailog
            DialogFragment timePicker = new com.tuinite.timemanagement.helper_classes.TimePicker();
            timePicker.show(getSupportFragmentManager(), "Time Picker");
        });

        et_note = findViewById(R.id.add_reminder_note_et);
        sw_alarm = findViewById(R.id.add_reminder_alram_switch);

        //Setting save onclick listener to save to database
        tv_save.setOnClickListener(v -> {
            //Call validate method
            validate();
        });


        //Setting save onclick listener to cancel
        tv_cancel.setOnClickListener(v -> {
            //Close Activity
            finish();
        });
        reminderViewModel = ViewModelProviders.of(this).get(ReminderViewModel.class);
        try {
            UPDATE_CODE = intent.getIntExtra("UPDATE_CODE", 0);
            if (UPDATE_CODE == 1) {
                reminderId = intent.getIntExtra("REMINDER_ID", 0);
                editReminder(reminderId);
            }
        } catch (Exception e) {
            Log.e("ADD_REMINDER ERROR 1", String.valueOf(e));
        }

    }


    //Date Set Listener
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.i("Calender Year", String.valueOf(year));
        Log.i("Calender Month", String.valueOf(month));
        Log.i("Calender Day", String.valueOf(dayOfMonth));
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Log.i("Calender Date", String.valueOf(calendar.getTimeInMillis()));
        et_date.setText(DateFormat.format("dd-MM-yyyy", calendar));
        et_time.setText(null);


    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.i("Calender Hour", String.valueOf(hourOfDay));
        Log.i("Calender Minute", String.valueOf(minute));
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Log.i("Calender Time", String.valueOf(calendar.getTimeInMillis()));
        et_time.setText(DateFormat.format("hh:mm aa", calendar));

    }


    //Input Data validation method
    private void validate() {

        title = et_title.getText().toString().trim();
        date = et_date.getText().toString().trim();
        time = et_time.getText().toString().trim();
        note = et_note.getText().toString().trim();
        alarm = sw_alarm.isChecked();

        //Validating data
        if (title.isEmpty()) {
            tl_title.setError("Title cannot be empty");
            return;
        } else {
            tl_title.setError(null);
        }

        if (date.isEmpty()) {
            tl_date.setError("Date cannot be empty");
            return;
        } else {
            tl_date.setError(null);
        }

        if (time.isEmpty()) {
            tl_time.setError("Time cannot be empty");
            return;
        } else {
            tl_time.setError(null);
        }
        //Check contion if user slect future time or not
        if (calendar.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
            tl_time.setError(null);
            dateTime = calendar.getTime();
        } else {
            et_time.setText(null);
            tl_time.setError("Select future time");
            et_date.setText(null);
            tl_date.setError("Select future date");
            return;
        }


        try {
            Reminder reminder = new Reminder(dateTime, title, note, alarm);
            if (UPDATE_CODE == 1) {

                cancelNotification();
                reminder.setReminderId(reminderId);
                reminderViewModel.updateReminder(reminder);


            } else {
                Log.i("ADD_REMINDER 23", "Working");

                reminderViewModel.reminderInsertion(reminder);
                Toast.makeText(this, "Reminder Saved", Toast.LENGTH_SHORT).show();

            }
            scheduleReminderNotification();
            finish();
        } catch (Exception e) {
            Log.i("ReminderInsertionError", String.valueOf(e));
        }
    }

    //Cancel Notification
    private void cancelNotification() {
        Intent cancelIntent = new Intent(this, ReminderBoardcastReceiver.class);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(this, notificationId, cancelIntent, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(cancelPendingIntent);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.cancel(notificationId);
    }


    //Notification Scheduling Method
    private void scheduleReminderNotification() {

        //Creating notfication channel
        createNotificationChannel();
        long id = calendar.getTimeInMillis();
        Log.i("ADD_REMINDER", String.valueOf(id));

        Intent alarmIntent = new Intent(this, ReminderBoardcastReceiver.class);
        alarmIntent.putExtra("TITLE", title);
        alarmIntent.putExtra("ALARM", alarm);
        alarmIntent.putExtra("DATE_TIME", id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) id, alarmIntent, 0);


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT < 23) {
            if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, id, pendingIntent);

            } else {
                // alarmManager.set(AlarmManager.RTC_WAKEUP, alramTime,1000, pendingIntent);
                alarmManager.set(AlarmManager.RTC_WAKEUP, id, pendingIntent);
                //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,alramTime,1000*60*5,pendingIntent);
            }
        } else {
            //  alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,alramTime,1000*60*5,pendingIntent);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, id, pendingIntent);
        }
    }


    //Create Notification Channel Method
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final Uri currentTone = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM);
            CharSequence name = "Reminder Channel";
            String description = "Channel for reminder";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("REMINDER_ALARM", name, importance);
            channel.setDescription(description);
            channel.enableVibration(false);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

        }
    }


    //Edit Reminder
    private void editReminder(int reminderId) {

        reminderViewModel.getSingleReminder(reminderId).observe(this, reminders -> {
            title = reminders.get(0).getTitle();
            dateTime = reminders.get(0).getDateTime();
            calendar.setTime(dateTime);
            notificationId = (int) calendar.getTimeInMillis();
            note = reminders.get(0).getNote();
            alarm = reminders.get(0).getAlarm();
            et_title.setText(title);
            et_note.setText(note);
            sw_alarm.setChecked(alarm);
            et_date.setText(DateFormat.format("dd-MM-yyy", dateTime));
            et_time.setText(DateFormat.format("hh:mm aa", dateTime));
            Log.i("EDIT REMINDER 1", String.valueOf(reminderId));
        });

    }

}

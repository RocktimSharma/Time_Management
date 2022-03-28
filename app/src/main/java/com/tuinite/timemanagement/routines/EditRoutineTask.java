package com.tuinite.timemanagement.routines;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputLayout;
import com.tuinite.timemanagement.R;
import com.tuinite.timemanagement.entities.RoutineTasks;
import com.tuinite.timemanagement.viewmodels.RoutineTaskViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditRoutineTask extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    Calendar calendar;
    private Intent intent;
    private int routineTaskId;
    private TextView saveTask_tv;
    private ImageButton cancelTask_imbtn;
    private EditText taskTitle_et, taskStartTime_et, taskEndTime_et, taskNote_et;
    private TextInputLayout taskEndTime_tl, taskTitle_tl, taskStartTime_tl;
    private SwitchCompat taskAlarm_sw;
    private RoutineTaskViewModel routineTaskViewModel;
    private Date taskStartTime, taskEndTime;
    private int routineId;
    private int timePickerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_routine_task);

        cancelTask_imbtn = findViewById(R.id.edit_routineTask_cancel_imbtn);
        cancelTask_imbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveTask_tv = findViewById(R.id.edit_routineTask_save_tv);
        saveTask_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRoutineTask();
            }
        });

        intent = getIntent();
        calendar = Calendar.getInstance();

        routineTaskId = intent.getIntExtra("ROUTINE_TASK_ID", 0);


        taskTitle_et = findViewById(R.id.edit_routineTask_title_et);
        taskTitle_tl = findViewById(R.id.edit_routineTask_title_input_layout);


        taskStartTime_et = findViewById(R.id.edit_routineTask_start_time_et);
        //Task Start Time EditText OnClickListener
        taskStartTime_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerId = 1;
                DialogFragment timePicker = new com.tuinite.timemanagement.helper_classes.TimePicker();
                timePicker.show(getSupportFragmentManager(), "START_TIME");


            }
        });
        taskStartTime_tl = findViewById(R.id.edit_routineTask_start_time_input_layout);


        taskEndTime_et = findViewById(R.id.edit_routineTask_end_time_et);
        // Task End Time EditText OnClickListener
        taskEndTime_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerId = 2;
                DialogFragment timePicker = new com.tuinite.timemanagement.helper_classes.TimePicker();
                timePicker.show(getSupportFragmentManager(), "END_TIME");


            }
        });
        taskEndTime_tl = findViewById(R.id.edit_routineTask_end_time_input_layout);

        taskNote_et = findViewById(R.id.edit_routineTask_note_et);

        taskAlarm_sw = findViewById(R.id.edit_routineTask_alarm_sw);

        routineTaskViewModel = ViewModelProviders.of(this).get(RoutineTaskViewModel.class);
        if (routineTaskId != 0) {
            routineTaskViewModel.getSingleRoutineTask(routineTaskId).observe(this, new Observer<List<RoutineTasks>>() {
                @Override
                public void onChanged(List<RoutineTasks> routineTasks) {
                    routineId = routineTasks.get(0).getTaskRoutineId();
                    taskTitle_et.setText(routineTasks.get(0).getTaskTitle());
                    taskStartTime = routineTasks.get(0).getTaskStartTime();
                    taskEndTime = routineTasks.get(0).getTaskEndTime();
                    taskStartTime_et.setText(DateFormat.format("hh:mm aa", taskStartTime));
                    taskEndTime_et.setText(DateFormat.format("hh:mm aa", taskEndTime));
                    taskNote_et.setText(routineTasks.get(0).getTaskNote());
                    taskAlarm_sw.setChecked(routineTasks.get(0).getTaskAlarm());
                }
            });
        } else {
            routineId = intent.getIntExtra("ROUTINE_ID", 0);
        }

    }


    private void updateRoutineTask() {
        routineTaskViewModel.getRoutineTasks(routineId).observe(this, new Observer<List<RoutineTasks>>() {
            @Override
            public void onChanged(List<RoutineTasks> routineTasks) {
                validateTask(routineTasks);
            }
        });
    }

    private void validateTask(List<RoutineTasks> routineTasks) {
        String title = taskTitle_et.getText().toString().trim();
        String startTime = taskStartTime_et.getText().toString().trim();
        String endTime = taskEndTime_et.getText().toString().trim();
        String note = taskNote_et.getText().toString().trim();

        if (title.isEmpty()) {
            taskTitle_tl.setError("Task Title cannot be empty");
            return;
        } else {
            taskTitle_tl.setError(null);
        }

        if (startTime.isEmpty()) {
            taskStartTime_tl.setError("Select Start Time");
            return;
        } else {
            taskStartTime_tl.setError(null);
        }

        if (endTime.isEmpty()) {
            taskEndTime_tl.setError("Select End Time");
            return;
        } else {
            taskEndTime_tl.setError(null);
        }


        int size = routineTasks.size();

        if (size > 0)

            for (int i = 0; i < size; i++) {
                if (routineTaskId != routineTasks.get(i).getTaskId()) {
                    Log.e("Worked 4", "Worked 4");

                    String previousTaskTitle = routineTasks.get(i).getTaskTitle();
                    Date previousTaskStartTime = routineTasks.get(i).getTaskStartTime();
                    Date previousTaskEndTime = routineTasks.get(i).getTaskEndTime();

                    if (previousTaskTitle.equals(title)) {
                        Log.e("Worked 5", "Worked 5");
                        taskTitle_tl.setError("Task Title already exists");
                        return;
                    } else {
                        Log.e("Worked 6", "Worked 6");
                        taskTitle_tl.setError(null);
                    }
                    Log.i("VALIDATE_TASK 1", String.valueOf(previousTaskEndTime));
                    Log.i("VALIDATE_TASK 2", String.valueOf(taskEndTime));
                    Log.i("VALIDATE_TASK 3", String.valueOf(taskEndTime.before(previousTaskStartTime)));


                    if (taskEndTime.before(previousTaskStartTime)) {
                        taskStartTime_tl.setError(null);
                        taskEndTime_tl.setError(null);

                    } else if (taskStartTime.after(previousTaskEndTime)) {
                        taskStartTime_tl.setError(null);
                        taskEndTime_tl.setError(null);

                    } else {
                        taskStartTime_tl.setError("Time already selected ");
                        taskEndTime_tl.setError("Time already selected");
                        return;
                    }
                }


            }

        try {
            RoutineTasks routineTasks1 = new RoutineTasks(routineId, title, taskStartTime, taskEndTime, note, taskAlarm_sw.isChecked());

            if (routineTaskId == 0) {
                routineTaskViewModel.routineTaskInsertion(routineTasks1);
                Log.e("Worked 2", "Worked");
                finish();
            } else {
                routineTasks1.setTaskId(routineTaskId);
                routineTaskViewModel.updateRoutineTask(routineTasks1);
                Log.e("Worked 3", "Worked");
                finish();
            }

            Toast.makeText(this, "Saved Changes", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Worked 1", String.valueOf(e));
            Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show();
        }
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
        Log.i("Click Set", String.valueOf(calendar.getTimeInMillis()));
        Log.i("Calender Tag", String.valueOf(view.getId()));
        Log.i("Calender Extra", String.valueOf(view.getTag()));

        if (timePickerId == 1) {
            taskStartTime = calendar.getTime();
            Log.i("Click Start", String.valueOf(calendar.getTimeInMillis()));
            if (taskEndTime != null) {
                if (taskStartTime.before(taskEndTime)) {
                    taskStartTime_tl.setError(null);
                    taskStartTime_et.setText(DateFormat.format("hh:mm aa", calendar));
                } else {
                    taskStartTime = null;
                    taskStartTime_tl.setError("Start Time should be less than Start Time");
                    taskStartTime_et.setText(null);
                }
            } else {
                taskStartTime_tl.setError(null);
                taskStartTime_et.setText(DateFormat.format("hh:mm aa", calendar));
            }


        } else if (timePickerId == 2) {
            taskEndTime = calendar.getTime();
            Log.i("Click End", String.valueOf(calendar.getTimeInMillis()));
            if (taskStartTime != null) {
                if (taskEndTime.after(taskStartTime)) {
                    taskEndTime_tl.setError(null);
                    taskEndTime_et.setText(DateFormat.format("hh:mm aa", calendar));
                } else {
                    taskEndTime = null;
                    taskEndTime_tl.setError("End Time should be greater than Start Time");
                    taskEndTime_et.setText(null);
                }
            } else {
                taskEndTime_tl.setError(null);
                taskEndTime_et.setText(DateFormat.format("hh:mm aa", calendar));
            }
        }

    }
}
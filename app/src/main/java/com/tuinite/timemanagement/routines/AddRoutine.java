package com.tuinite.timemanagement.routines;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.TimePickerDialog;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.tuinite.timemanagement.R;
import com.tuinite.timemanagement.entities.Routine;
import com.tuinite.timemanagement.entities.RoutineDays;
import com.tuinite.timemanagement.entities.RoutineTasks;
import com.tuinite.timemanagement.recycler_adapters.RoutineTaskRecyclerAdapter;
import com.tuinite.timemanagement.viewmodels.RoutineTaskViewModel;
import com.tuinite.timemanagement.viewmodels.RoutineViewmodel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.view.View.GONE;
import static android.widget.Toast.LENGTH_SHORT;

public class AddRoutine extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimePickerDialog.OnTimeSetListener {

    public static List<RoutineTasks> routineTasksList = new ArrayList<>();
    String[] repeat = {"Everyday", "Custom"};
    Boolean customRepeat = false;
    //Total milli-seconds in a day
    long totalMilliSecs = 86400000;
    long routineId;
    // private NotificationReceiver nReceiver;
    Boolean isVisible = false;
    RecyclerView routineTask_recycler_view;
    Date taskStartTime, taskEndTime;
    AnimatedVectorDrawable addTaskBtnAnimation;
    LinearLayout taskFormLinearLayout;
    Button addTask;
    private ImageButton cancelRoutineImbtn;
    private TextView saveRoutine_tv;
    private TextInputLayout routineTitle_tl;
    private EditText routineTitle_et;
    private TextView timeLeft_tv;
    private RoutineDays routineDays;
    private CheckBox sun_check, mon_check, tue_check, wed_check, thu_check, fri_check, sat_check;
    private TextView noData_tv;
    private RoutineTaskRecyclerAdapter routineTaskRecyclerAdapter;
    private EditText taskTitle_et, taskStartTime_et, taskEndTime_et, taskNote_et;
    private TextInputLayout taskEndTime_tl, taskTitle_tl, taskStartTime_tl;
    private SwitchCompat taskAlarm_sw;
    private LinearLayout repeatLinearLayout;
    private TextView saveTask_tv;
    private TextView cancelTask_tv;
    private Calendar calendar;
    private int timePickerId;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_routine);

        addTask = findViewById(R.id.add_routine_add_task_btn);

        calendar = Calendar.getInstance();

        cancelRoutineImbtn = findViewById(R.id.add_routine_cancel_imbtn);
        cancelRoutineImbtn.setOnClickListener(v -> finish());

        saveRoutine_tv = findViewById(R.id.add_routine_save_tv);
        saveRoutine_tv.setOnClickListener(v -> validateAndSaveRoutine());

        routineTitle_tl = findViewById(R.id.add_routine_title_input_layout);
        routineTitle_et = findViewById(R.id.add_routine_title_et);

        noData_tv = findViewById(R.id.add_routine_no_data_tv);
        noData_tv.setVisibility(View.VISIBLE);


        sun_check = findViewById(R.id.add_routine_checkBox_sun);
        mon_check = findViewById(R.id.add_routine_checkBox_mon);
        tue_check = findViewById(R.id.add_routine_checkBox_tue);
        wed_check = findViewById(R.id.add_routine_checkBox_wed);
        thu_check = findViewById(R.id.add_routine_checkBox_thu);
        fri_check = findViewById(R.id.add_routine_checkBox_fri);
        sat_check = findViewById(R.id.add_routine_checkBox_sat);


        routineDays = new RoutineDays(true, true, true, true, true, true, true);

        timeLeft_tv = findViewById(R.id.add_routine_time_left_tv);


        repeatLinearLayout = findViewById(R.id.add_routine_repeat_linear_layout);

        taskTitle_et = findViewById(R.id.add_routine_add_task_title_et);
        taskTitle_tl = findViewById(R.id.add_routine_add_task_title_input_layout);


        taskStartTime_et = findViewById(R.id.add_routine_add_task_start_time_et);
        taskStartTime_tl = findViewById(R.id.add_routine_add_task_start_time_input_layout);


        taskEndTime_et = findViewById(R.id.add_routine_add_task_end_time_et);
        taskEndTime_tl = findViewById(R.id.add_routine_aadd_task_end_time_input_layout);

        taskNote_et = findViewById(R.id.add_routine_add_task_note_et);
        saveTask_tv = findViewById(R.id.add_routine_add_task_save_tv);
        cancelTask_tv = findViewById(R.id.add_routine_add_task_cancel_tv);
        cancelTask_tv.setOnClickListener(v -> closeTaskForm());


        taskAlarm_sw = findViewById(R.id.add_routine_add_task_alarm_sw);

        taskFormLinearLayout = findViewById(R.id.add_routine_add_task_linear_layout);


        routineTask_recycler_view = findViewById(R.id.add_routine_add_task_recycler_view);
        routineTask_recycler_view.setVisibility(GONE);

        routineTaskRecyclerAdapter = new RoutineTaskRecyclerAdapter(routineTasksList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        routineTask_recycler_view.setLayoutManager(mLayoutManager);
        routineTask_recycler_view.setHasFixedSize(true);
        routineTask_recycler_view.setAdapter(routineTaskRecyclerAdapter);


        //Add Task Button Animation
        addTask.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {


                //Closing Add Task Form
                if (isVisible == true) {
                    closeTaskForm();

                } else {

                    taskFormLinearLayout.animate().translationY(0).setDuration(300).alpha(1).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            taskFormLinearLayout.setVisibility(View.VISIBLE);
                            Drawable img = AddRoutine.this.getDrawable(R.drawable.avd_add_to_cross);
                            img.setBounds(0, 0, 60, 60);
                            addTask.setCompoundDrawables(null, null, img, null);

                            addTaskBtnAnimation = (AnimatedVectorDrawable) img;
                            addTaskBtnAnimation.start();
                        }
                    });

                    isVisible = true;
                }

            }
        });


        //Task Start Time EditText OnClickListener
        taskStartTime_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerId = 1;
                DialogFragment timePicker = new com.tuinite.timemanagement.helper_classes.TimePicker();
                timePicker.show(getSupportFragmentManager(), "START_TIME");


            }
        });

        // Task End Time EditText OnClickListener
        taskEndTime_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerId = 2;
                DialogFragment timePicker = new com.tuinite.timemanagement.helper_classes.TimePicker();
                timePicker.show(getSupportFragmentManager(), "END_TIME");


            }
        });


        //Save Task On Click Listner
        saveTask_tv.setOnClickListener(v -> {
            validateAndSaveTask();

        });


        //Repeat Spinner
        Spinner repeatSpinnner = findViewById(R.id.add_routine_repeat_spinner);
        repeatSpinnner.setOnItemSelectedListener(this);

        // Create the instance of ArrayAdapter
        // having the list of choices
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                R.layout.repat_spinner,
                repeat);


        // set simple layout resource file
        // for each Task of spinner
        ad.setDropDownViewResource(
                R.layout.spinner_dropdown);


        repeatSpinnner.setAdapter(ad);

    }

    private void closeTaskForm() {
        taskFormLinearLayout.animate().translationY(-100).setDuration(300).alpha(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                taskFormLinearLayout.setVisibility(GONE);

            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                Drawable img = AddRoutine.this.getDrawable(R.drawable.avd_cross_to_add);
                img.setBounds(0, 0, 60, 60);
                addTask.setCompoundDrawables(null, null, img, null);

                addTaskBtnAnimation = (AnimatedVectorDrawable) img;
                addTaskBtnAnimation.start();
            }
        });

        isVisible = false;
        resetTaskform();
    }

    private void validateAndSaveRoutine() {
        String routineTitle = routineTitle_et.getText().toString().trim();
        if (routineTitle.isEmpty()) {
            routineTitle_tl.setError("Set Routine Title");
            return;
        } else {
            routineTitle_tl.setError(null);
        }

        if (customRepeat == true) {
            routineDays = new RoutineDays(sun_check.isChecked(), mon_check.isChecked(), tue_check.isChecked(), wed_check.isChecked(), thu_check.isChecked(), fri_check.isChecked(), sat_check.isChecked());

        } else {
            routineDays = new RoutineDays(true, true, true, true, true, true, true);
        }


        try {

            Routine routine = new Routine(routineTitle, routineDays, false);
            RoutineViewmodel routineViewmodel = ViewModelProviders.of(this).get(RoutineViewmodel.class);
            routineId = routineViewmodel.routineInsertion(routine);

            if (routineId == 0) {
                routineTitle_tl.setError("Title already exists");
            } else {

                for (int i = 0; i < routineTasksList.size(); i++) {
                    RoutineTasks Task = routineTasksList.get(i);
                    RoutineTasks routineTasks = new RoutineTasks((int) routineId, Task.getTaskTitle(), Task.getTaskStartTime(), Task.getTaskEndTime(), Task.getTaskNote(), Task.getTaskAlarm());
                    RoutineTaskViewModel routineTaskViewModel = ViewModelProviders.of(AddRoutine.this).get(RoutineTaskViewModel.class);
                    routineTaskViewModel.routineTaskInsertion(routineTasks);

                }
                finish();
            }

        } catch (Exception e) {
            Log.i("Error", String.valueOf(e));
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


    //
    private void validateAndSaveTask() {

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


        int size = routineTasksList.size();

        if (size > 0) {

            for (int i = 0; i < size; i++) {
                String previousTaskTitle = routineTasksList.get(i).getTaskTitle();
                Date previousTaskStartTime = routineTasksList.get(i).getTaskStartTime();
                Date previousTaskEndTime = routineTasksList.get(i).getTaskEndTime();

                if (previousTaskTitle.equals(title)) {
                    taskTitle_tl.setError("Task Title already exists");
                    Toast.makeText(AddRoutine.this, "Same", Toast.LENGTH_LONG).show();
                    return;
                } else {
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
                    taskStartTime_tl.setError("Time already selected 1");
                    taskEndTime_tl.setError("Time already selected");
                    return;
                }


            }
        }

        try {
            Log.i("Validate Task", "Success");
            RoutineTasks routineTasks = new RoutineTasks(1, taskTitle_et.getText().toString(), taskStartTime, taskEndTime, note, true);
            routineTasksList.add(routineTasks);
            routineTaskRecyclerAdapter.notifyDataSetChanged();
            noData_tv.setVisibility(GONE);
            routineTask_recycler_view.setVisibility(View.VISIBLE);
            //    Toast.makeText(AddRoutine.this, "Done", Toast.LENGTH_LONG).show();

            long selected_time = taskEndTime.getTime() - taskStartTime.getTime();
            try {
                Log.i("VALIDATE_TASK 4", String.valueOf(taskEndTime.getTime()));
            } catch (Exception e) {
                Log.i("VALIDATE_TASK 5", String.valueOf(e));
            }

            totalMilliSecs = totalMilliSecs - selected_time;
            Log.i("VALIDATE_TASK 5", String.valueOf(totalMilliSecs));

            String time_left = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(totalMilliSecs),
                    TimeUnit.MILLISECONDS.toMinutes(totalMilliSecs) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalMilliSecs)),
                    TimeUnit.MILLISECONDS.toSeconds(totalMilliSecs) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalMilliSecs)));
            Log.i("VALIDATE_TASK 6", time_left);
            timeLeft_tv.setText(time_left);
            resetTaskform();
        } catch (Exception e) {
            Toast.makeText(AddRoutine.this, (CharSequence) e, LENGTH_SHORT).show();
            Log.i("VALIDATE_TASK Error", String.valueOf(e));
        }
    }

    private void resetTaskform() {
        taskTitle_et.setText(null);
        taskStartTime_et.setText(null);
        taskEndTime_et.setText(null);
        taskNote_et.setText(null);
        taskAlarm_sw.setChecked(false);
        taskTitle_tl.setError(null);
        taskStartTime_tl.setError(null);
        taskEndTime_tl.setError(null);
        taskStartTime = null;
        taskEndTime = null;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // make toastof name of course
        // which is selected in spinner
        if (position == 1) {
            repeatLinearLayout.setVisibility(View.VISIBLE);
            customRepeat = true;


        } else {
            repeatLinearLayout.setVisibility(GONE);
            customRepeat = false;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
//  class NotificationReceiver extends BroadcastReceiver {

//   @Override
//  public void onReceive(Context context, Intent intent) {
//   //   String temp = intent.getStringExtra("notification_event") + "\n" ;
//   Log.i("APP NOTI",temp);
//}
//  } */


/*

 chipGroup = findViewById(R.id.chipGroup);

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip = group.findViewById(checkedId);
                if (chip != null)
                    tag = (String) chip.getChipText();

            }
        });
 */

//    nReceiver = new NotificationReceiver();
//   IntentFilter filter = new IntentFilter();
//   filter.addAction("com.tuinite.timemanagement.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
//   registerReceiver(nReceiver,filter);

   /*  taskBlockAppNotification_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    try {

                        List<ApplicationInfo> pkgAppsList = getPackageManager().getInstalledApplications(0);
                        //  taskNote_et.setText((CharSequence) pkgAppsList.get(0));
                        //    Toast.makeText(AddRoutine.this,pkgAppsList.get(0).category,Toast.LENGTH_LONG).show();
                        PackageManager packageManager = AddRoutine.this.getPackageManager();
                        for (int i = 0; i < pkgAppsList.size(); i++) {
                            String app = pkgAppsList.get(i).loadLabel(packageManager).toString();
                        }

                        Dialog dialog=new Dialog(AddRoutine.this);

                        dialog.setContentView(R.layout.apps_list);

                        RecyclerView apprecyclerView=(RecyclerView)dialog.findViewById(R.id.app_list_recyclerView);

                        AppListRecyclerAdapter appListRecyclerAdapter=new AppListRecyclerAdapter(pkgAppsList);
                        RecyclerView.LayoutManager LayoutManager = new LinearLayoutManager(AddRoutine.this);

                        apprecyclerView.setLayoutManager(LayoutManager);

                       apprecyclerView.setAdapter(appListRecyclerAdapter);

                       dialog.show();

                        AlertDialog.Builder alertDialog = new
                                AlertDialog.Builder(AddRoutine.this);
                        View rowList = getLayoutInflater().inflate(R.layout.apps_list, null);
                        ListView listView = rowList.findViewById(R.id.List);
                        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        alertDialog.setView(rowList);
                        AlertDialog dialog = alertDialog.create();
                        dialog.show();

                     //   Intent i = new Intent("com.tuinite.timemanagement.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
                      //  i.putExtra("command","list");
                     //   sendBroadcast(i);

                    }
                    catch (Exception e){
                        Toast.makeText(AddRoutine.this,(CharSequence) e,Toast.LENGTH_LONG).show();
                    }
                }
            }
        });*/

   
   /*
   
       private void showStartTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddRoutine.this,
                R.style.DialogTheme, (view, hourOfDay, minute) -> {
            //Initialize hour & minute
            sHour = hourOfDay;
            sMinute = minute;
            //Initialize Calender
            Calendar calendar = Calendar.getInstance();


            //    currentDay=calendar.get(Calendar.DAY_OF_MONTH);
            //set hour on calender
            //     calendar.set(Calendar.DAY_OF_MONTH,currentDay);
            calendar.set(Calendar.HOUR_OF_DAY, sHour);
            //set minute on calender
            calendar.set(Calendar.MINUTE, sMinute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);


            //  startTime=calendar.getTimeInMillis();

            startTime = calendar.getTime();

            //    itemStartTime= (Date) DateFormat.format("hh:mm aa",calendar);


            taskStartTime_et.setText(DateFormat.format("hh:mm aa", startTime));


        }, cHour, cMinute, false);

        //Show TimePicker Dialog
        timePickerDialog.show();

    }
   
   
    */
   
   
    /*  private void showEndTimePicker(){
        TimePickerDialog timePickerDialog= new TimePickerDialog(AddRoutine.this,
                R.style.DialogTheme , (view, hourOfDay, minute) -> {
            //Initialize hour & minute
            sHour=hourOfDay;
            sMinute=minute;
            //Initialize Calender
            Calendar calendar=Calendar.getInstance();
       //     currentDay=calendar.get(Calendar.DAY_OF_MONTH);
            //set hour on calender
     //       calendar.set(Calendar.DAY_OF_MONTH,currentDay);

            //set hour on calender
            calendar.set(Calendar.HOUR_OF_DAY,sHour);
            //set minute on calender
            calendar.set(Calendar.MINUTE,sMinute);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);





        //    endTime=calendar.getTimeInMillis();
            endTime=calendar.getTime();


            if(endTime.compareTo(startTime)<0){
                taskEndTime_tl.setError("End Time cannot be less than Start Time");
                taskEndTime_et.setText(null);
                return;
            }else if(endTime.compareTo(startTime)==0){
                taskEndTime_tl.setError("End Time cannot be equal to Start Time");
                taskEndTime_et.setText(null);
                return;
            }
            else{
                taskEndTime_tl.setError(null);
                //itemEndTime= (Date) DateFormat.format("hh:mm aa",calendar);

                taskEndTime_et.setText(DateFormat.format("hh:mm aa",endTime));
            }







        },cHour, cMinute, false);


        //Show TimePicker Dialog
        timePickerDialog.show();


    }*/


//Sheduling Notification and alarm
 /*   private void scheduleNotification() {
        Log.i("scheduleNotification", String.valueOf(true));
        Log.i("Working", String.valueOf(routineDays.sun));
        if (routineDays.sun) {
            setNotification(Calendar.SUNDAY);
        }
        if (routineDays.mon) {
            setNotification(Calendar.MONDAY);
        }
        if (routineDays.tue) {
            setNotification(Calendar.TUESDAY);
        }
        if (routineDays.wed) {
            setNotification(Calendar.WEDNESDAY);
        }
        if (routineDays.thu) {
            setNotification(Calendar.THURSDAY);
        }
        if (routineDays.fri) {
            setNotification(Calendar.FRIDAY);
        }
        if (routineDays.sat) {
            setNotification(Calendar.SATURDAY);
        }


    }*/

  /*  private void setNotification(int day) {


        Calendar routineCalender = Calendar.getInstance();
        routineCalender.set(Calendar.DAY_OF_WEEK, day);
        for (int i = 0; i < routineTasksList.size(); i++) {
            Date itemStartTime = routineTasksList.get(i).getTaskStartTime();


            int hours = itemStartTime.getHours();
            int min = itemStartTime.getMinutes();
            int sec = itemStartTime.getSeconds();

            Log.i("Hours", String.valueOf(hours));
            Log.i("Min", String.valueOf(min));
            Log.i("sec", String.valueOf(sec));

            routineCalender.set(Calendar.HOUR_OF_DAY, hours);
            routineCalender.set(Calendar.MINUTE, min);
            routineCalender.set(Calendar.SECOND, sec);
            routineCalender.set(Calendar.MILLISECOND, 0);

            Log.i("TimeSet1", String.valueOf(routineCalender.getTimeInMillis()));
            // Check we aren't setting it in the past which would trigger it to fire instantly
            if (routineCalender.getTimeInMillis() < System.currentTimeMillis()) {
                routineCalender.add(Calendar.DAY_OF_YEAR, 7);
                Log.i("TimeSet", String.valueOf(routineCalender.getTimeInMillis()));
            }

            long notification_time = routineTasksList.get(i).getTaskStartTime().getTime();

            Intent notificationIntent = new Intent(AddRoutine.this, RoutineBroadcastReceiver.class);

            notificationIntent.putExtra("ITEM_TITLE", routineTasksList.get(i).getTaskTitle());
            notificationIntent.putExtra("PENDING_INTENT_ID", (int) notification_time);
            notificationIntent.putExtra("ROUTINE_ALARM", routineTasksList.get(i).getTaskAlarm());


            PendingIntent notificationPendingIntent = PendingIntent.getBroadcast(AddRoutine.this, (int) notification_time, notificationIntent, 0);


            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


            alarmManager.set(AlarmManager.RTC_WAKEUP, notification_time, notificationPendingIntent);
            //   alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,notification_time,AlarmManager.INTERVAL_DAY*7,notificationPendingIntent);


        }


        //   Toast.makeText(AddRoutine.this, (int) routineCalender.getTimeInMillis(),Toast.LENGTH_LONG).show();


        //  for(int i=0;i<routineTasksList.size();i++){

        //  }
    }*/



        /*  try{
            RoutineTasks routineTasks = new RoutineTasks(itemTitle,time1,time2,itemNote,tag,taskAlarm_sw.isChecked(),taskBlockAppNotification_sw.isChecked(),5,5,7);
            RoutineTaskViewModel routineItemViewModel = ViewModelProviders.of(AddRoutine.this).get(RoutineTaskViewModel.class);
            routineItemViewModel.routineItemInsertion(routineTasks);

            Toast.makeText(AddRoutine.this,"Saved", Toast.LENGTH_LONG).show();
        }catch(Exception e){
            Toast.makeText(AddRoutine.this, (CharSequence) e, Toast.LENGTH_LONG).show();
        }*/


//     long time=Math.abs(endTime-startTime);

//       totalMilliSecs=totalMilliSecs-time;

 /*       long selected_time=Math.abs(endTime.getTime()-startTime.getTime());
        totalMilliSecs=totalMilliSecs-selected_time;

        String time_left = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(totalMilliSecs),
                TimeUnit.MILLISECONDS.toMinutes(totalMilliSecs) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(totalMilliSecs)),
                TimeUnit.MILLISECONDS.toSeconds(totalMilliSecs) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(totalMilliSecs)));
         timeLeft_tv.setText(time_left);

        RoutineTasks routineTasks = new RoutineTasks(0,itemTitle,startTime,endTime,itemNote,tag,taskAlarm_sw.isChecked(),taskBlockAppNotification_sw.isChecked(),5);

        routineTasksList.add(routineTasks);
        routineTaskRecyclerAdapter.notifyDataSetChanged();

        Toast.makeText(AddRoutine.this,"Done",Toast.LENGTH_LONG).show();


    /*    try{
            RoutineTaskClass routineItemClass =new RoutineTaskClass(taskTitle_et.getText().toString(),taskStartTime_et.getText().toString(),taskEndTime_et.getText().toString(),
                    taskNote_et.getText().toString(),tag,startTime,endTime,taskAlarm_sw.isChecked(),taskBlockAppNotification_sw.isChecked());
            routineTasksList.add(routineItemClass);

            routineTaskRecyclerAdapter.notifyDataSetChanged();

            Toast.makeText(AddRoutine.this,"Done",Toast.LENGTH_LONG).show();

        }catch (Exception e){
            Toast.makeText(AddRoutine.this, (CharSequence) e, LENGTH_SHORT).show();
            Log.i("Errir", String.valueOf(e));
            return;

        }*/
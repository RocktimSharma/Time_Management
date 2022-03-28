package com.tuinite.timemanagement.routines;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.tuinite.timemanagement.R;
import com.tuinite.timemanagement.entities.RoutineProgress;
import com.tuinite.timemanagement.entities.RoutineTasks;
import com.tuinite.timemanagement.helper_classes.TaskStats;
import com.tuinite.timemanagement.recycler_adapters.RoutineProgressRecyclerAdapter;
import com.tuinite.timemanagement.recycler_adapters.RoutineTaskRecyclerAdapter;
import com.tuinite.timemanagement.viewmodels.RoutineProgressViewModel;
import com.tuinite.timemanagement.viewmodels.RoutineTaskViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RoutineStats extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    long time;
    RoutineProgressRecyclerAdapter routineProgressRecyclerAdapter;
    private Toolbar toolbar;
    private LineChart graphView;
    private ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
    private List<RoutineProgress> routineProgress = new ArrayList<>();
    private List<RoutineTasks> routineTask = new ArrayList<>();
    private ArrayList<Entry> plannedValues = new ArrayList<Entry>();
    private ArrayList<Entry> followedValues = new ArrayList<Entry>();
    private List<TaskStats> taskStats = new ArrayList();
    private Intent intent;
    private RecyclerView routineTaskRecyclerView;
    private TextView statsDate;
    private RoutineTaskRecyclerAdapter routineItemRecyclerAdapter;
    private RoutineTaskViewModel routineTaskViewModel;
    private Calendar calendar;
    private int routineId;
    private Date progressDate;
    private int plannedTotalHours, followedTotalHours;

    private ProgressBar plannedProgressBar, followedProgressBar;

    private TextView workedHour_tv, plannedHour_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine_stats);
        intent = getIntent();
        routineId = intent.getIntExtra("ROUTINE_ID", 0);
        Log.i("ROUTINE_STATS 56", String.valueOf(routineId));
        String routineTitle = intent.getStringExtra("ROUTINE_TITLE");
        Toolbar toolbar = findViewById(R.id.routine_stats_toolbar);
        toolbar.setTitle(routineTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);

        progressDate = calendar.getTime();

        followedProgressBar = findViewById(R.id.routine_stats_worked_time_progress_bar);
        plannedProgressBar = findViewById(R.id.routine_stats_planned_time_progress_bar);
        workedHour_tv = findViewById(R.id.routine_stats_worked_htrs_tv2);
        plannedHour_tv = findViewById(R.id.routine_stats_planned_htrs_tv1);

        statsDate = findViewById(R.id.routine_stats_graph_date);
        statsDate.setText(DateFormat.format("dd-MM-yyyy", calendar));
        statsDate.setOnClickListener(v -> {
            followedValues.clear();
            DialogFragment datePicker = new com.tuinite.timemanagement.helper_classes.DatePicker();
            Bundle args = new Bundle();
            args.putBoolean("LIMIT", false);
            datePicker.setArguments(args);
            datePicker.show(getSupportFragmentManager(), "Date Picker");
        });

        graphView = (LineChart) findViewById(R.id.routine_stats_graph);
        graphView.setDragEnabled(true);
        graphView.setScaleEnabled(true);


        routineTaskRecyclerView = findViewById(R.id.routine_stats_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        routineTaskRecyclerView.setLayoutManager(layoutManager);
        routineTaskRecyclerView.setHasFixedSize(true);
        routineProgressRecyclerAdapter = new RoutineProgressRecyclerAdapter();
        routineTaskRecyclerView.setAdapter(routineProgressRecyclerAdapter);

        routineTaskViewModel = ViewModelProviders.of(this).get(RoutineTaskViewModel.class);


        //  plotData();
        //  getRoutineProgress();

        routineTaskViewModel.getRoutineTasks(routineId).observe(this, new Observer<List<RoutineTasks>>() {
            @Override
            public void onChanged(List<RoutineTasks> routineTasks) {
                routineTask = routineTasks;
                plannedValues.clear();
                Log.i("ROUTINE_STATS 74", String.valueOf(routineTasks.get(0).getTaskTitle()));
                //  RoutineTaskRecyclerAdapter routineTaskRecyclerAdapter = new RoutineTaskRecyclerAdapter(routineTasks);
                //   routineTaskRecyclerView.setAdapter(routineTaskRecyclerAdapter);
                float hour = 0;
                for (int i = 0; i < routineTasks.size(); i++) {
                    //   Log.i("ROUTINE_STATS 74", String.valueOf(routineTasks.get(i).getTaskTitle()));
                    Date startTime = routineTasks.get(i).getTaskStartTime();
                    Date endTime = routineTasks.get(i).getTaskEndTime();
                    calendar.setTime(startTime);
                    long start = calendar.getTimeInMillis();
                    calendar.setTime(endTime);
                    long end = calendar.getTimeInMillis();
                    plannedValues.add(new Entry(start, hour));
                    Log.i("ROUTINE_STATS 75", start + "  " + hour);
                    Log.i("ROUTINE_STATS 77", String.valueOf((end - start)));
                    float dif = (end - start) / (1000 * 60);
                    hour = hour + dif;
                    plannedValues.add(new Entry(end, hour));
                    Log.i("ROUTINE_STATS 76", end + "  " + hour);

                }
                plannedTotalHours = (int) hour;
                getRoutineProgress();


            }
        });


    }


    public void getRoutineProgress() {
        RoutineProgressViewModel routineProgressViewModel = ViewModelProviders.of(this).get(RoutineProgressViewModel.class);
        routineProgressViewModel.getRoutineProgress(progressDate, routineId).observe(this, new Observer<List<RoutineProgress>>() {
            @Override
            public void onChanged(List<RoutineProgress> routineProgresses) {
                routineProgress = routineProgresses;
                Log.i("ROUTINE_STATS 82", "Size Progress " + routineProgresses.size());
                float hour = 0;
                for (int i = 0; i < routineProgresses.size(); i++) {
                    Date startTime = routineProgresses.get(i).getTaskStartTime();
                    Date endTime = routineProgresses.get(i).getTaskEndTime();
                    calendar.setTime(startTime);
                    long start = calendar.getTimeInMillis();
                    calendar.setTime(endTime);
                    long end = calendar.getTimeInMillis();
                    followedValues.add(new Entry(start, hour));
                    Log.i("ROUTINE_STATS 75", start + "  " + hour);
                    Log.i("ROUTINE_STATS 77", String.valueOf((end - start)));
                    float dif = (end - start) / (1000 * 60);
                    hour = hour + dif;
                    followedValues.add(new Entry(end, hour));
                    Log.i("ROUTINE_STATS 76", end + "  " + hour);

                }
                followedTotalHours = (int) hour;

                plotData();
                prepareList();
            }

        });

    }

    private void prepareList() {
        TaskStats stats;

        Log.i("ROUTINE_STATS 80", "Size " + routineTask.size());
        for (int i = 0; i < routineTask.size(); i++) {
            int routineTaskId = routineTask.get(i).getTaskId();
            long planned;
            long worked;
            String taskTitle = routineTask.get(i).getTaskTitle();
            planned = routineTask.get(i).getTaskStartTime().getTime() - routineTask.get(i).getTaskEndTime().getTime();
            Log.i("ROUTINE_STATS 81", "Size Progress " + routineProgress.size());
            if (routineProgress.size() != 0) {
                for (int j = 0; j < routineProgress.size(); j++) {
                    int progressTaskId = routineProgress.get(j).getProgressTaskId();


                    Log.i("ROUTINE_STATS 77", "Planned " + planned);
                    if (routineTaskId == progressTaskId) {
                        worked = routineProgress.get(j).getTaskStartTime().getTime() - routineProgress.get(j).getTaskEndTime().getTime();
                        stats = new TaskStats(taskTitle, planned, worked);
                        Log.i("ROUTINE_STATS 78", "Worked " + worked);
                        taskStats.add(stats);
                    } else {
                        stats = new TaskStats(taskTitle, planned, 0);
                        Log.i("ROUTINE_STATS 79", "Worked is none");
                        taskStats.add(stats);
                    }


                }
            } else {
                stats = new TaskStats(taskTitle, planned, 0);
                Log.i("ROUTINE_STATS 89", "Worked is none");
                taskStats.add(stats);
            }


        }

        routineProgressRecyclerAdapter.setTaskProgress(taskStats);
        routineProgressRecyclerAdapter.notifyDataSetChanged();


    }


    private void plotData() {

        plannedProgressBar.setProgress(plannedTotalHours / 24);
        followedProgressBar.setProgress(followedTotalHours / 24);
        workedHour_tv.setText(String.valueOf(timeCalculation(followedTotalHours)));
        plannedHour_tv.setText(String.valueOf(timeCalculation(plannedTotalHours)));

        graphView.setDragEnabled(true);
        graphView.setScaleEnabled(true);
        graphView.getAxisRight().setEnabled(false);
        graphView.getDescription().setEnabled(false);
        //  graphView.setBackgroundColor(Color.parseColor("#242A4F"));
        graphView.getLegend().setTextColor(this.getResources().getColor(R.color.textColor));
        graphView.getAxisLeft().setDrawGridLines(false);
        graphView.getXAxis().setDrawGridLines(false);
        XAxis xAxis = graphView.getXAxis();
        xAxis.setValueFormatter(new LineChartXAxisValueFormatter());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-90);
        xAxis.setAxisLineColor(this.getResources().getColor(R.color.textColor));
        xAxis.setTextColor(this.getResources().getColor(R.color.textColor));
        YAxis yAxis = graphView.getAxisLeft();
        yAxis.setStartAtZero(true);
        yAxis.setSpaceBottom(10);
        yAxis.setAxisLineColor(this.getResources().getColor(R.color.textColor));
        yAxis.setTextColor(this.getResources().getColor(R.color.textColor));
        //    yAxis.setAxisMaximum(24);
        //       yAxis.setAxisMinimum(0);


        yAxis.setValueFormatter(new HourMinYAxisValueFormatter());


        //  graphView.setVisibleYRangeMinimum(0, YAxis.AxisDependency.LEFT);

        //   graphView.getAxisLeft().setAxisMinimum(-10);
        //  graphView.getAxisLeft().setAxisMaximum(24);


        LineDataSet plannedLineDataSet = new LineDataSet(plannedValues, "Planned");
        plannedLineDataSet.setColor(Color.parseColor("#10D4F9"));
        plannedLineDataSet.setDrawHighlightIndicators(true);
        plannedLineDataSet.setHighLightColor(Color.WHITE);
        plannedLineDataSet.setDrawCircles(false);
        plannedLineDataSet.setDrawValues(false);


        LineDataSet followedLineDataSet = new LineDataSet(followedValues, "Followed");
        followedLineDataSet.setColor(Color.parseColor("#FF0000"));
        followedLineDataSet.setCircleColor(Color.BLACK);
        followedLineDataSet.setCircleRadius(0);
        followedLineDataSet.setCircleHoleRadius(0);
        followedLineDataSet.setDrawHighlightIndicators(true);
        followedLineDataSet.setHighLightColor(Color.RED);
        followedLineDataSet.setValueTextSize(12);
        followedLineDataSet.setValueTextColor(Color.GRAY);
        followedLineDataSet.setDrawCircles(false);
        followedLineDataSet.setDrawValues(false);


        iLineDataSets.add(plannedLineDataSet);
        iLineDataSets.add(followedLineDataSet);


        LineData lineData = new LineData(iLineDataSets);
        graphView.invalidate();
        graphView.setData(lineData);

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        progressDate = calendar.getTime();
        Log.i("Calender Date", String.valueOf(calendar.getTimeInMillis()));
        statsDate.setText(DateFormat.format("dd-MM-yyyy", calendar));
        if (graphView.getData() != null)
            graphView.getData().clearValues();
        taskStats.clear();
        graphView.clear();
        getRoutineProgress();


    }

    public String timeCalculation(int value) {

        value = Math.abs(value);

        int hour = value / 60;
        int min = value % 60;
        String time;
        if (hour == 0) {
            if (min < 10) {
                time = String.format("%dm",
                        min);
            } else {
                time = String.format("%02dm",
                        min);
            }

        } else if (hour < 10) {
            if (min == 0) {
                time = String.format("%dh", hour);
            } else {
                time = String.format("%dh:%02dm", hour,
                        min);
            }
        } else {
            if (min == 0) {
                time = String.format("%02dh", hour);
            } else {
                time = String.format("%02dh:%02dm", hour,
                        min);
            }
        }
        return time;


    }

    public class HourMinYAxisValueFormatter extends IndexAxisValueFormatter {


        private DecimalFormat mFormat;


        @Override
        public String getFormattedValue(float value) {
            // "value" represents the position of the label on the axis (x or y)


            String formatedvalue = String.valueOf(value);
            int hour = (int) (value / 60f);
            int min = (int) (value % 60f);
            Log.i("ROTINE_STATS 212", String.valueOf(hour));
            if (min == 0) {
                formatedvalue = hour + "h";
            } else if (hour == 0) {
                formatedvalue = min + "m";
            } else {
                formatedvalue = hour + "h " + min + "m";
            }
            Log.i("ROTINE_STATS 21", formatedvalue);
            return formatedvalue;

        }


    }

    private class LineChartXAxisValueFormatter extends IndexAxisValueFormatter {


        public String getFormattedValue(float value) {
            String label = (String) android.text.format.DateFormat.format("hh:mm aa", (long) value);

            Log.i("Label", String.valueOf(value));


            return label;
            // return date;
        }
    }

}

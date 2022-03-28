package com.tuinite.timemanagement.routines;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tuinite.timemanagement.R;
import com.tuinite.timemanagement.entities.Routine;
import com.tuinite.timemanagement.entities.RoutineDays;
import com.tuinite.timemanagement.entities.RoutineTasks;
import com.tuinite.timemanagement.recycler_adapters.RoutineTaskRecyclerAdapter;
import com.tuinite.timemanagement.relations.RoutineWithRoutineTask;
import com.tuinite.timemanagement.viewmodels.RoutineViewmodel;

import java.util.ArrayList;
import java.util.List;

public class EditRoutine extends AppCompatActivity {

    private Intent intent;
    private TextInputLayout routineTitle_tl;
    private TextInputEditText routineTitle_et;
    private CheckBox sun_check, mon_check, tue_check, wed_check, thu_check, fri_check, sat_check;
    private ImageButton cancel_imbtn;
    private TextView saveRoutine_tv;
    private RecyclerView routineTask_recyclerView;
    private List<RoutineTasks> routineTasks = new ArrayList<>();
    private RoutineViewmodel routineViewmodel;
    private int routineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_routine);

        intent = getIntent();
        routineId = intent.getIntExtra("ROUTINE_ID", 0);

        cancel_imbtn = findViewById(R.id.edit_routine_cancel_imbtn);
        cancel_imbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveRoutine_tv = findViewById(R.id.edit_routine_save_tv);
        saveRoutine_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRoutine();
            }
        });

        Button addnewTask = findViewById(R.id.edit_routine_task_add_new_task);
        addnewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditRoutine.this, EditRoutineTask.class);
                intent.putExtra("ROUTINE_TASK_ID", 0);
                intent.putExtra("ROUTINE_ID", routineId);
                startActivity(intent);
            }
        });


        routineTitle_tl = findViewById(R.id.edit_routine_title_input_layout);
        routineTitle_et = findViewById(R.id.edit_routine_title_et);

        routineTask_recyclerView = findViewById(R.id.edit_routine_task_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        routineTask_recyclerView.setLayoutManager(layoutManager);
        routineTask_recyclerView.setHasFixedSize(true);


        sun_check = findViewById(R.id.edit_routine_checkBox_sun);
        mon_check = findViewById(R.id.edit_routine_checkBox_mon);
        tue_check = findViewById(R.id.edit_routine_checkBox_tue);
        wed_check = findViewById(R.id.edit_routine_checkBox_wed);
        thu_check = findViewById(R.id.edit_routine_checkBox_thu);
        fri_check = findViewById(R.id.edit_routine_checkBox_fri);
        sat_check = findViewById(R.id.edit_routine_checkBox_sat);


        routineViewmodel = ViewModelProviders.of(this).get(RoutineViewmodel.class);

        routineViewmodel.getRoutinewithRoutineTasks(routineId).observe(this, new Observer<List<RoutineWithRoutineTask>>() {
            @Override
            public void onChanged(List<RoutineWithRoutineTask> routineWithRoutineTasks) {
                setRoutine(routineWithRoutineTasks);
            }
        });
    }

    private void updateRoutine() {
        String routineTitle = routineTitle_et.getText().toString().trim();
        if (routineTitle.isEmpty()) {
            routineTitle_tl.setError("Title cannot be empty ");
            return;
        } else {
            routineTitle_tl.setError(null);
        }
        RoutineDays routineDays = new RoutineDays(sun_check.isChecked(), mon_check.isChecked(), tue_check.isChecked(), wed_check.isChecked(), thu_check.isChecked(), fri_check.isChecked(), sat_check.isChecked());
        Routine routine = new Routine(routineTitle, routineDays, false);

        routine.setRoutineId(routineId);
        try {
            routineViewmodel.updateRoutine(routine);
            finish();
        } catch (Exception e) {
            routineTitle_tl.setError("Try different title");
            Toast.makeText(this, "Failed to Update", Toast.LENGTH_SHORT).show();
        }

    }

    private void setRoutine(List<RoutineWithRoutineTask> routineWithRoutineTasks) {
        Routine routine = routineWithRoutineTasks.get(0).routine;
        routineTasks = routineWithRoutineTasks.get(0).routineTasksList;
        RoutineTaskRecyclerAdapter routineTaskRecyclerAdapter = new RoutineTaskRecyclerAdapter(routineTasks);
        routineTask_recyclerView.setAdapter(routineTaskRecyclerAdapter);
        routineTitle_et.setText(routine.getRoutineTitle());
        sun_check.setChecked(routine.getRoutineDays().sun);
        mon_check.setChecked(routine.getRoutineDays().mon);
        tue_check.setChecked(routine.getRoutineDays().tue);
        wed_check.setChecked(routine.getRoutineDays().wed);
        thu_check.setChecked(routine.getRoutineDays().thu);
        fri_check.setChecked(routine.getRoutineDays().fri);
        sat_check.setChecked(routine.getRoutineDays().sat);

    }

}
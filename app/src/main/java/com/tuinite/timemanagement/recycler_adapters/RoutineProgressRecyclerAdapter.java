package com.tuinite.timemanagement.recycler_adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuinite.timemanagement.R;
import com.tuinite.timemanagement.helper_classes.TaskStats;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class RoutineProgressRecyclerAdapter extends RecyclerView.Adapter<RoutineProgressRecyclerAdapter.RoutineProgressViewHolder> {

    private List<TaskStats> taskProgress = new ArrayList<>();

    @NonNull
    @Override
    public RoutineProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("Worked called", "yes");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.routine_progress_recycler_layout, parent, false);

        return new RoutineProgressViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineProgressViewHolder holder, int position) {
        Log.i("Worked called", "yes");
        TaskStats currentTaskProgress = taskProgress.get(position);

        String title = currentTaskProgress.getTaskTitle();
        String planned = timeCalculation(currentTaskProgress.getPlannedTaskTime());
        String worked = timeCalculation(currentTaskProgress.getWorkTaskTime());

        holder.taskTitle_tv.setText(title);
        holder.taskPlanned_tv.setText(planned);
        holder.taskWorked_tv.setText(worked);


    }

    public String timeCalculation(long value) {

        value = Math.abs(value);

        long hour = TimeUnit.MILLISECONDS.toHours(value);
        long min = TimeUnit.MILLISECONDS.toMinutes(value) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(value));
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

    @Override
    public int getItemCount() {
        return taskProgress.size();
    }

    public void setTaskProgress(List<TaskStats> taskProgress) {
        Log.i("Worked 74784", "yes");
        this.taskProgress = taskProgress;
        notifyDataSetChanged();

    }

    public class RoutineProgressViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle_tv;
        TextView taskPlanned_tv;
        TextView taskWorked_tv;

        public RoutineProgressViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle_tv = itemView.findViewById(R.id.routine_progress_recycler_layout_routine_task_title_tv);
            taskPlanned_tv = itemView.findViewById(R.id.routine_progress_recycler_layout_routine_task_planned_tv);
            taskWorked_tv = itemView.findViewById(R.id.routine_progress_recycler_layout_worked_time_tv);
        }
    }
}

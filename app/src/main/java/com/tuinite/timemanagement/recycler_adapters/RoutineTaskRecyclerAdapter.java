package com.tuinite.timemanagement.recycler_adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.tuinite.timemanagement.R;
import com.tuinite.timemanagement.boardcast_receivers.RoutineBroadcastReceiver;
import com.tuinite.timemanagement.entities.RoutineTasks;
import com.tuinite.timemanagement.routines.EditRoutineTask;
import com.tuinite.timemanagement.viewmodels.RoutineTaskViewModel;

import java.util.List;

public class RoutineTaskRecyclerAdapter extends RecyclerView.Adapter<RoutineTaskRecyclerAdapter.RoutineTaskViewHolder> {

    int previousExpandedPosition = -1;
    int mExpandedPosition = -1;
    Context context;
    private List<RoutineTasks> routineTasksList;


    public RoutineTaskRecyclerAdapter(List<RoutineTasks> routineTasks) {
        this.routineTasksList = routineTasks;
    }

    @NonNull
    @Override
    public RoutineTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.routine_task_card, parent, false);

        return new RoutineTaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineTaskViewHolder holder, int position) {

        final boolean isExpanded = position == mExpandedPosition;
        //    holder.linearLayout1.setVisibility(isExpanded?View.VISIBLE: GONE);


        String tag;

        RoutineTasks routineTasks = routineTasksList.get(position);
        holder.itemTitle_tv.setText(routineTasks.getTaskTitle());
        //  holder.itemStartDate_tv.setText(routineTasks.getItemStartDate());
        //  holder.itemEndDate_tv.setText(routineTasks.getItemAlarmEndTime());

        String startTime = String.valueOf(DateFormat.format("hh:mm aa", routineTasks.getTaskStartTime()));
        String endTime = String.valueOf(DateFormat.format("hh:mm aa", routineTasks.getTaskEndTime()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditRoutineTask.class);
                intent.putExtra("ROUTINE_TASK_ID", routineTasks.getTaskId());
                context.startActivity(intent);
            }
        });

        holder.editTask_imbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditRoutineTask.class);
                intent.putExtra("ROUTINE_TASK_ID", routineTasks.getTaskId());
                intent.putExtra("ROUTINE_ID", 0);
                context.startActivity(intent);
            }
        });

        holder.deleteTask_imbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoutineTaskViewModel routineTaskViewModel = ViewModelProviders.of((FragmentActivity) context).get(RoutineTaskViewModel.class);
                routineTaskViewModel.deleteRoutineTask(routineTasks);
                Intent cancelIntent = new Intent(context, RoutineBroadcastReceiver.class);
                PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, routineTasks.getTaskId(), cancelIntent, 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(cancelPendingIntent);
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                notificationManagerCompat.cancel(routineTasks.getTaskId());
            }
        });


        holder.itemStartDate_tv.setText(startTime);
        holder.itemEndDate_tv.setText(endTime);


        holder.itemNote_tv.setText(routineTasks.getTaskNote());


    }

    @Override
    public int getItemCount() {
        return routineTasksList.size();

    }

    public class RoutineTaskViewHolder extends RecyclerView.ViewHolder {

        public TextView itemTitle_tv, itemStartDate_tv, itemEndDate_tv, itemNote_tv;

        public ImageButton editTask_imbtn, deleteTask_imbtn;

        public RoutineTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemTitle_tv = (TextView) itemView.findViewById(R.id.routine_task_recycler_layout_title_tv);
            itemStartDate_tv = (TextView) itemView.findViewById(R.id.routine_task_recycler_layout_start_time_tv);
            itemEndDate_tv = (TextView) itemView.findViewById(R.id.routine_task_recycler_layout_end_time_tv);
            itemNote_tv = (TextView) itemView.findViewById(R.id.routine_task_recycler_layout_note_tv);
            editTask_imbtn = itemView.findViewById(R.id.routine_task_recycler_layout_edit_imbtn);
            deleteTask_imbtn = itemView.findViewById(R.id.routine_task_recycler_layout_delete_imbtn);


        }
    }
}

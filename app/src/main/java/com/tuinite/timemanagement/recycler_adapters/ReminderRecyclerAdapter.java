package com.tuinite.timemanagement.recycler_adapters;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
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
import com.tuinite.timemanagement.boardcast_receivers.ReminderBoardcastReceiver;
import com.tuinite.timemanagement.entities.Reminder;
import com.tuinite.timemanagement.reminders.AddReminder;
import com.tuinite.timemanagement.viewmodels.ReminderViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;

public class ReminderRecyclerAdapter extends RecyclerView.Adapter<ReminderRecyclerAdapter.ReminderHolder> {
    Context context;
    private List<Reminder> reminders = new ArrayList<>();

    @NonNull
    @Override
    public ReminderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_card, parent, false);
        Log.i("Called", String.valueOf(itemView));
        return new ReminderRecyclerAdapter.ReminderHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ReminderHolder holder, int position) {

        Reminder currentReminder = reminders.get(position);
        String title = currentReminder.getTitle();

        Date time = currentReminder.getDateTime();
        String note = currentReminder.getNote();

        holder.reminderTitle_tv.setText(title);
        holder.reminderTime_tv.setText(DateFormat.format("hh:mm aa", time));
        if (note.isEmpty()) {
            holder.reminderNote_tv.setVisibility(GONE);
        } else {
            holder.reminderNote_tv.setVisibility(View.VISIBLE);
            holder.reminderNote_tv.setText(note);

        }

        holder.edit_reminder_imbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int reminderId = currentReminder.getReminderId();
                Intent intent = new Intent(context, AddReminder.class);
                intent.putExtra("UPDATE_CODE", 1);
                intent.putExtra("REMINDER_ID", reminderId);
                context.startActivity(intent);
            }
        });

        holder.delete_reminder_imbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ReminderViewModel reminderViewModel = ViewModelProviders.of((FragmentActivity) context).get(ReminderViewModel.class);
                    reminderViewModel.deleteReminder(currentReminder);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(time);
                    Intent cancelIntent = new Intent(context, ReminderBoardcastReceiver.class);
                    PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context, (int) calendar.getTimeInMillis(), cancelIntent, 0);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(cancelPendingIntent);
                    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                    notificationManagerCompat.cancel((int) calendar.getTimeInMillis());
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
        for (int i = 0; i < reminders.size(); i++) {
            Log.i("REMINDER", String.valueOf(reminders.get(i).getTitle()));
        }
        notifyDataSetChanged();
    }

    public class ReminderHolder extends RecyclerView.ViewHolder {
        private ImageButton edit_reminder_imbtn;
        private ImageButton delete_reminder_imbtn;
        private TextView reminderTitle_tv;
        private TextView reminderTime_tv;
        private TextView reminderNote_tv;

        public ReminderHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            edit_reminder_imbtn = itemView.findViewById(R.id.reminder_card_edit_imbtn);
            delete_reminder_imbtn = itemView.findViewById(R.id.reminder_card_delete_imbtn);
            reminderTitle_tv = itemView.findViewById(R.id.reminder_card_title_tv);
            reminderTime_tv = itemView.findViewById(R.id.reminder_card_time_tv);
            reminderNote_tv = itemView.findViewById(R.id.reminder_card_note_tv);

        }

    }
}

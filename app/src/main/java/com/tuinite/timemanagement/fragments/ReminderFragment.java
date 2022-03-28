package com.tuinite.timemanagement.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tuinite.timemanagement.R;
import com.tuinite.timemanagement.entities.Reminder;
import com.tuinite.timemanagement.helper_classes.CalendarUtils;
import com.tuinite.timemanagement.recycler_adapters.CalendarAdapter;
import com.tuinite.timemanagement.recycler_adapters.ReminderRecyclerAdapter;
import com.tuinite.timemanagement.reminders.AddReminder;
import com.tuinite.timemanagement.viewmodels.ReminderViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.tuinite.timemanagement.helper_classes.CalendarUtils.daysInWeekArray;
import static com.tuinite.timemanagement.helper_classes.CalendarUtils.monthYearFromDate;
import static com.tuinite.timemanagement.helper_classes.CalendarUtils.selectedDate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReminderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class ReminderFragment extends Fragment implements CalendarAdapter.OnItemListener {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ImageButton previousWeek_btn, nextWeek_btn;
    ReminderRecyclerAdapter reminderRecyclerAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView monthYear_tv;
    private RecyclerView calendar_recycler_view;
    private RecyclerView reminder_recycler_view;

    public ReminderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReminderFragment newInstance(String param1, String param2) {
        ReminderFragment fragment = new ReminderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.reminder_fragment, container, false);

        CalendarUtils.selectedDate = LocalDate.now();

        calendar_recycler_view = view.findViewById(R.id.reminder_frag_calender_recycler_view);
        monthYear_tv = view.findViewById(R.id.reminder_frag_monthYear_tv);
        previousWeek_btn = view.findViewById(R.id.reminder_frag_previous_week_action_imbtn);
        previousWeek_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousWeekAction();
            }
        });

        nextWeek_btn = view.findViewById(R.id.reminder_frag_next_week_action_imbtn);
        nextWeek_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextWeekAction();
            }
        });

        reminder_recycler_view = view.findViewById(R.id.reminder_frag_recyclerview);
        reminder_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        reminder_recycler_view.setHasFixedSize(true);
        reminderRecyclerAdapter = new ReminderRecyclerAdapter();
        reminder_recycler_view.setAdapter(reminderRecyclerAdapter);

        FloatingActionButton addReminder_fab = view.findViewById(R.id.reminder_frag_add_fab);
        addReminder_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddReminder.class));
            }
        });

        setWeekView();
        return view;
    }

    private void reteiveData() {
        java.util.Date date = java.sql.Date.valueOf(String.valueOf(selectedDate));
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 00);
        c.set(Calendar.MINUTE, 00);
        c.set(Calendar.SECOND, 00);
        Date dayStart = c.getTime();
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 900);
        Date dayEnd = c.getTime();
        Log.i("DAYSTART", String.valueOf(dayStart));
        Log.i("DAYEnd", String.valueOf(dayEnd));

        ReminderViewModel reminderViewModel = ViewModelProviders.of(this).get(ReminderViewModel.class);
        reminderViewModel.getReminder(dayStart, dayEnd).observe(getViewLifecycleOwner(), new Observer<List<Reminder>>() {
            @Override
            public void onChanged(List<Reminder> reminders) {
                reminderRecyclerAdapter.setReminders(reminders);
                Log.i("REMinder Size", String.valueOf(reminders.size()));

            }
        });
    }

    //Week View
    private void setWeekView() {
        monthYear_tv.setText(monthYearFromDate(CalendarUtils.selectedDate));
        Log.i("Selected Date", String.valueOf(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 7);
        calendar_recycler_view.setLayoutManager(layoutManager);
        calendar_recycler_view.setAdapter(calendarAdapter);
        reteiveData();
    }

    //Prevoius Week
    public void previousWeekAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    //Next Week
    public void nextWeekAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

}






























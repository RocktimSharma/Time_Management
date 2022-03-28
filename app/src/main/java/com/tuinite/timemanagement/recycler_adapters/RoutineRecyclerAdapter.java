package com.tuinite.timemanagement.recycler_adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.tuinite.timemanagement.DAO.RoutineDao;
import com.tuinite.timemanagement.Notification.RoutineNotification;
import com.tuinite.timemanagement.R;
import com.tuinite.timemanagement.entities.Routine;
import com.tuinite.timemanagement.relations.RoutineWithRoutineTask;
import com.tuinite.timemanagement.routines.EditRoutine;
import com.tuinite.timemanagement.routines.RoutineStats;
import com.tuinite.timemanagement.viewmodels.RoutineViewmodel;

import java.util.ArrayList;
import java.util.List;

public class RoutineRecyclerAdapter extends RecyclerView.Adapter<RoutineRecyclerAdapter.RoutineViewHolder> {

    Context context;
    private List<Routine> routines = new ArrayList<>();
    private List<RoutineWithRoutineTask> routineWithRoutineTasks = new ArrayList<>();
    private LinearLayout daysLinearLayout;

    @NonNull
    @Override
    public RoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.routine_recyclerview_layout, parent, false);
        return new RoutineRecyclerAdapter.RoutineViewHolder((itemView));
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineViewHolder holder, int position) {

        Routine routine = routines.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditRoutine.class);
                intent.putExtra("ROUTINE_ID", routine.getRoutineId());
                context.startActivity(intent);
            }
        });


        holder.routineTitle_tv.setText(routine.getRoutineTitle());

        holder.routine_card.setTag(routine.getRoutineId());

        if (routine.getActive()) {
            holder.routineActive_sw.setChecked(true);
        } else {
            holder.routineActive_sw.setChecked(false);
        }


        if (routine.getRoutineDays().sun == true) {
            //  holder.sun_tv.setBackground(ContextCompat.getDrawable(context, rounded_rectangle));
            //holder.sun_tv.setTextColor(Color.parseColor("#434343"));
            addTextView("Sun");
        } else {
            //   holder.sun_tv.setBackground(null);
            // holder.sun_tv.setTextColor(Color.parseColor("#DBDBE5"));

        }
        if (routine.getRoutineDays().mon == true) {
            //  holder.mon_tv.setBackground(ContextCompat.getDrawable(context, rounded_rectangle));
            // holder.mon_tv.setTextColor(Color.parseColor("#434343"));
            addTextView("Mon");
        } else {
            //  holder.mon_tv.setBackground(null);
            // holder.mon_tv.setTextColor(Color.parseColor("#DBDBE5"));
        }
        if (routine.getRoutineDays().tue == true) {
            // holder.tue_tv.setBackground(ContextCompat.getDrawable(context, rounded_rectangle));
            //  holder.tue_tv.setTextColor(Color.parseColor("#434343"));
            addTextView("Tue");
        } else {
            //   holder.tue_tv.setBackground(null);
            // holder.tue_tv.setTextColor(Color.parseColor("#DBDBE5"));
        }
        if (routine.getRoutineDays().wed == true) {
            //  holder.wed_tv.setBackground(ContextCompat.getDrawable(context, rounded_rectangle));
            //  holder.wed_tv.setTextColor(Color.parseColor("#434343"));
            addTextView("Wed");
        } else {
            // holder.wed_tv.setBackground(null);
            // holder.wed_tv.setTextColor(Color.parseColor("#DBDBE5"));

        }
        if (routine.getRoutineDays().thu == true) {
            //  holder.thu_tv.setBackground(ContextCompat.getDrawable(context, rounded_rectangle));
            //  holder.thu_tv.setTextColor(Color.parseColor("#434343"));
            addTextView("Thu");
        } else {
            // holder.thu_tv.setBackground(null);
            //  holder.thu_tv.setTextColor(Color.parseColor("#DBDBE5"));
        }
        if (routine.getRoutineDays().fri == true) {
            // holder.fri_tv.setBackground(ContextCompat.getDrawable(context, rounded_rectangle));
            // holder.fri_tv.setTextColor(Color.parseColor("#434343"));
            addTextView("Fri");
        } else {
            //   holder.fri_tv.setBackground(null);
            //  holder.fri_tv.setTextColor(Color.parseColor("#DBDBE5"));

        }
        if (routine.getRoutineDays().sat == true) {
            //  holder.sat_tv.setBackground(ContextCompat.getDrawable(context, rounded_rectangle));
            // holder.sat_tv.setTextColor(Color.parseColor("#434343"));
            addTextView("Sat");
        } else {
            //  holder.sat_tv.setBackground(null);
            // holder.sat_tv.setTextColor(Color.parseColor("#DBDBE5"));
        }

        int routineId = routine.getRoutineId();

        holder.routineActive_sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RoutineViewmodel routineViewmodel = ViewModelProviders.of((FragmentActivity) context).get(RoutineViewmodel.class);
                if (isChecked) {
                    RoutineNotification routineNotification = new RoutineNotification(context, routineId);
                    try {

                        routineViewmodel.updateRoutineStatus(routineId, true);
                    } catch (Exception e) {

                    }
                } else {
                    try {
                        routineViewmodel.updateRoutineStatus(routineId, false);
                    } catch (Exception e) {

                    }
                }
            }
        });

        holder.stats_imbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RoutineStats.class);
                intent.putExtra("ROUTINE_ID", routineId);
                intent.putExtra("ROUTINE_TITLE", holder.routineTitle_tv.getText());
                context.startActivity(intent);
            }
        });


    }

    private void addTextView(String text) {
        TextView days_tv = new TextView(context);
        days_tv.setText(text);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1f;
        layoutParams.setMargins(8, 0, 12, 0);
        days_tv.setLayoutParams(layoutParams);

        days_tv.setTextSize(14);
        days_tv.setGravity(Gravity.CENTER);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.montserrat);
        days_tv.setTypeface(typeface);
        days_tv.setTextColor(context.getResources().getColor(R.color.secondaryTextColor));
        daysLinearLayout.addView(days_tv);


    }

    @Override
    public int getItemCount() {
        return routines.size();
    }

    public void setRoutineWithRoutineItems(List<RoutineWithRoutineTask> routineWithRoutineTasks) {
        this.routineWithRoutineTasks = routineWithRoutineTasks;
        notifyDataSetChanged();
    }

    public void setRoutines(List<Routine> routines) {
        this.routines = routines;
        notifyDataSetChanged();
    }

    private Routine getRoutinePosition(int adapterPosition) {
        return routines.get(adapterPosition);
    }

    class RoutineViewHolder extends RecyclerView.ViewHolder {

        RoutineDao routineDao;
        private CardView routine_card;
        private TextView routineTitle_tv;
        private Switch routineActive_sw;
        private TextView routineTask_count_tv;
        private ImageButton stats_imbtn;
        private ImageButton del_imbtn;
        private RoutineViewmodel routineViewmodel;


        public RoutineViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            routineViewmodel = ViewModelProviders.of((FragmentActivity) context).get(RoutineViewmodel.class);

            routine_card = (CardView) itemView.findViewById(R.id.routine_recycler_layout_card);
            routineTitle_tv = (TextView) itemView.findViewById(R.id.routine_recycler_layout_routine_title_tv);
            routineActive_sw = (Switch) itemView.findViewById(R.id.routine_recycler_layout_active_sw);
            daysLinearLayout = (LinearLayout) itemView.findViewById(R.id.routine_recycler_layout_linear_layout);

            stats_imbtn = (ImageButton) itemView.findViewById(R.id.routine_recycler_layout_stats_imbtn);

            del_imbtn = (ImageButton) itemView.findViewById(R.id.routine_recycler_layout_del_imbtn);

            del_imbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteRoutine();
                }
            });


        }

        private void deleteRoutine() {
            int id = (int) routine_card.getTag();

            Boolean is_active = routineActive_sw.isChecked();

            if (is_active) {
                Log.i("Active", "Routine is active");

            } else {
                routineViewmodel.deleteRoutine(getRoutinePosition(getAdapterPosition()));
            }


        }

    }

}


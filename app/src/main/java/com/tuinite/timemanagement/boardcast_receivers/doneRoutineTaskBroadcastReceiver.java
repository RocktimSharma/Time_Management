package com.tuinite.timemanagement.boardcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.tuinite.timemanagement.routines.RoutineProgressUpdateActivity;

import static com.tuinite.timemanagement.boardcast_receivers.RoutineBroadcastReceiver.ringtone;

public class doneRoutineTaskBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ringtone.isPlaying()) {
            ringtone.stop();
        }

        int taskId = intent.getIntExtra("TASK_ID", 0);
        int routineId = intent.getIntExtra("ROUTINE_ID", 0);
        int taskCode = intent.getIntExtra("TASK_CODE", 0);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.cancel(taskId);

        try {
            //  new InsertProgress(routineId,taskId,context).execute();
            Intent newIntent = new Intent(context, RoutineProgressUpdateActivity.class);
            newIntent.putExtra("TASK_ID", taskId);
            newIntent.putExtra("TASK_CODE", taskCode);
            context.startActivity(newIntent);
        } catch (Exception e) {
            Log.i("DONE_ROUTINE 1", "Failed " + e);
        }


    }

 /*   class InsertProgress extends AsyncTask<Void, Void, Void> {
        int routineId;
        int routineTaskId;
        Context context;

        public InsertProgress(int routineId, int routineTaskId,Context context) {
            this.routineId = routineId;
            this.routineTaskId = routineTaskId;
            this.context=context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Calendar calendar=Calendar.getInstance();
            Date date=calendar.getTime();
            RoutineProgress routineProgress=new RoutineProgress(date,routineId,routineTaskId,date,date);
            try {
                RoutineProgressViewModel routineProgressViewModel = ViewModelProviders.of((FragmentActivity) context).get(RoutineProgressViewModel.class);
                routineProgressViewModel.routineProgressInsertion(routineProgress);
            }
            catch (Exception e){
                Log.i("DONE_ROUTINE 2","Failed "+e);
            }
            return  null;
        }
    }*/

}

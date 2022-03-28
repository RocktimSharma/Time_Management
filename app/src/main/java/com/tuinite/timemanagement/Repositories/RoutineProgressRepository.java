package com.tuinite.timemanagement.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.tuinite.timemanagement.DAO.RoutineProgressDao;
import com.tuinite.timemanagement.db.AppDb;
import com.tuinite.timemanagement.entities.RoutineProgress;

import java.util.Date;
import java.util.List;

public class RoutineProgressRepository {

    private RoutineProgressDao routineProgressDao;
    private LiveData<List<RoutineProgress>> routineProgressLiveData;

    //Constructor
    public RoutineProgressRepository(Application application) {
        AppDb appDb = AppDb.getInstance(application);
        this.routineProgressDao = appDb.routineProgressDao();

    }

    //Retevive Routine Progress
    public LiveData<List<RoutineProgress>> getRoutineProgress(Date progressDate, int routineId) {
        return routineProgressDao.getRoutineProgress(progressDate, routineId);
    }


    //Insert RoutineProgress
    public void routineProgressInsertion(RoutineProgress routineProgress) {
        new RoutineProgressInsertionAsyncTask(routineProgressDao).execute(routineProgress);
    }

    //Update Routine Progress
    public void updateEndtime(Date progressEndTime, Date progressDate, int taskId) {
        new UpdateEndTimeAyncTask(routineProgressDao, progressEndTime, progressDate, taskId).execute();
    }


    // Routine Progress Inserttion AsyncTask
    public class RoutineProgressInsertionAsyncTask extends AsyncTask<RoutineProgress, Void, Void> {
        RoutineProgressDao routineProgressDao;

        public RoutineProgressInsertionAsyncTask(RoutineProgressDao routineProgressDao) {
            this.routineProgressDao = routineProgressDao;
        }

        @Override
        protected Void doInBackground(RoutineProgress... routineProgresses) {
            routineProgressDao.routineProgressInsertion(routineProgresses[0]);
            return null;
        }
    }


    // Routine Progress Update AsyncTask
    public class UpdateEndTimeAyncTask extends AsyncTask<Void, Void, Void> {
        RoutineProgressDao routineProgressDao;
        Date progressEndTime;
        Date progressDate;
        int taskId;

        public UpdateEndTimeAyncTask(RoutineProgressDao routineProgressDao, Date progressEndTime, Date progressDate, int taskId) {
            this.routineProgressDao = routineProgressDao;
            this.progressEndTime = progressEndTime;
            this.progressDate = progressDate;
            this.taskId = taskId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            routineProgressDao.updateEndTime(progressEndTime, progressDate, taskId);
            return null;
        }
    }
}

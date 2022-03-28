package com.tuinite.timemanagement.Repositories;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.tuinite.timemanagement.DAO.RoutineDao;
import com.tuinite.timemanagement.db.AppDb;
import com.tuinite.timemanagement.entities.Routine;
import com.tuinite.timemanagement.relations.RoutineWithRoutineTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class RoutineRepository {

    private RoutineDao routineDao;
    private LiveData<List<Routine>> routineLiveData;

    //Constructor
    public RoutineRepository(Application application) {
        AppDb appDb = AppDb.getInstance(application);
        routineDao = appDb.routineDao();
        routineLiveData = routineDao.getRoutine();


    }

    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Routine>> getRoutine() {
        return routineLiveData;
    }

    public LiveData<List<RoutineWithRoutineTask>> getRoutineWithroutineTasks(int routineId) {
        return routineDao.getRoutineWithRoutineTasks(routineId);
    }

    //Creating method for Reminder Inseration Operation
    public int routineInsertion(Routine routine) {
        try {
            return new InsertRoutineAsyncTask(routineDao).execute(routine).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;


    }

    //Delete Routine
    public void deleteRoutine(Routine routine) {
        new DeleteRoutineAsyncTask(routineDao).execute(routine);
    }

    //Update Routine
    public void updateRoutine(Routine routine) {
        new UpdateRoutineAsyncTask(routineDao).execute(routine);
    }

    //Update Routine status
    public void updateRoutineStatus(int routineId, boolean isActive) {

        RoutineStatusUpdateParams params = new RoutineStatusUpdateParams(routineId, isActive);
        new UpdateRoutineStatusAsyncTask(routineDao).execute(params);
    }

    //Routine Status Update Helper Class
    private static class RoutineStatusUpdateParams {
        int routineId;
        boolean isActive;


        public RoutineStatusUpdateParams(int routineId, boolean isActive) {
            this.routineId = routineId;
            this.isActive = isActive;
        }
    }

    //Update Routine Status AsncTask
    private static class UpdateRoutineStatusAsyncTask extends AsyncTask<RoutineStatusUpdateParams, Void, Void> {

        RoutineDao routineDao;

        public UpdateRoutineStatusAsyncTask(RoutineDao routineDao) {
            this.routineDao = routineDao;
        }

        @Override
        protected Void doInBackground(RoutineStatusUpdateParams... routineStatusUpdateParams) {

            int routineId = routineStatusUpdateParams[0].routineId;
            boolean isActive = routineStatusUpdateParams[0].isActive;

            routineDao.updateRoutineStatus(routineId, isActive);

            return null;
        }
    }



    /*
    For LiveDta room automatically perform the operation in backgroud thread but for other database
    database operation we have to execute them in background thread manually because room does not
    allow database operation on main thread as it could freeze the app
    */

    //AsyncTask for executing Inserstion operation on background thread
    private static class InsertRoutineAsyncTask extends AsyncTask<Routine, Void, Integer> {
        private RoutineDao routineDao;

        private InsertRoutineAsyncTask(RoutineDao routineDao) {
            this.routineDao = routineDao;
        }

        @Override
        protected Integer doInBackground(Routine... routines) {
            try {
                int id = (int) routineDao.routineInsertion(routines[0]);
                return id;
            } catch (SQLiteConstraintException sqlConstraintexception) {
                Log.e("ROUTINE_REPO INSERTION", String.valueOf(sqlConstraintexception));
                return 0;
            }


        }
    }

    //Delete RoutineAsyncTask
    private static class DeleteRoutineAsyncTask extends AsyncTask<Routine, Void, Void> {
        private RoutineDao routineDao;

        public DeleteRoutineAsyncTask(RoutineDao routineDao) {
            this.routineDao = routineDao;
        }

        @Override
        protected Void doInBackground(Routine... routines) {
            routineDao.deleteRoutine(routines[0]);
            return null;
        }
    }

    //Update Routine AsyncTask
    private static class UpdateRoutineAsyncTask extends AsyncTask<Routine, Void, String> {
        RoutineDao routineDao;

        public UpdateRoutineAsyncTask(RoutineDao routineDao) {
            this.routineDao = routineDao;
        }

        @Override
        protected String doInBackground(Routine... routines) {

            try {
                routineDao.updateRoutine(routines[0]);
                return null;
            } catch (SQLiteConstraintException sqlConstraintexception) {
                Log.e("ROUTINE_REPO UPDATE", String.valueOf(sqlConstraintexception));
                return null;
            }

        }
    }
}

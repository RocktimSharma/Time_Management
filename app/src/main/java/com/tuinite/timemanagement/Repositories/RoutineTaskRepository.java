package com.tuinite.timemanagement.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.tuinite.timemanagement.DAO.RoutineTaskDao;
import com.tuinite.timemanagement.db.AppDb;
import com.tuinite.timemanagement.entities.RoutineTasks;

import java.util.Date;
import java.util.List;

public class RoutineTaskRepository {

    private RoutineTaskDao routineTaskDao;

    //Constructor
    public RoutineTaskRepository(Application application) {
        AppDb appDb = AppDb.getInstance(application);
        this.routineTaskDao = appDb.routineTaskDao();

    }

    // Routine Task Insertion
    public void routineTaskInsertion(RoutineTasks routineTasks) {
        new RoutineTaskInsertionAsyncTask(routineTaskDao).execute(routineTasks);
    }


    // Reteive Routine Task
    public LiveData<List<RoutineTasks>> getRoutineTasks(int routineId) {
        return routineTaskDao.getRoutineTasks(routineId);
    }

    // Reteive Single Task
    public LiveData<List<RoutineTasks>> getSingleRoutineTask(int taskId) {
        return routineTaskDao.getSingleRoutineTask(taskId);
    }

    // Get Next RoutineTask
    public LiveData<List<RoutineTasks>> getNextTask(Date endTime) {
        return routineTaskDao.getNextTask(endTime);
    }

    // Update RoutineTask
    public void updateRoutineTask(RoutineTasks routineTasks) {
        new UpdateRoutineTask(routineTaskDao).execute(routineTasks);
    }

    // Delete Routine Task
    public void deleteRoutineTask(RoutineTasks routineTasks) {
        new DeleteRoutineTaskAsyncTask(routineTaskDao).execute(routineTasks);
    }


    // Routine Task Insertion AsyncTAsk
    public class RoutineTaskInsertionAsyncTask extends AsyncTask<RoutineTasks, Void, Void> {
        RoutineTaskDao routineTaskDao;

        public RoutineTaskInsertionAsyncTask(RoutineTaskDao routineTaskDao) {
            this.routineTaskDao = routineTaskDao;
        }


        @Override
        protected Void doInBackground(RoutineTasks... routineTasks) {
            routineTaskDao.routineTaskInsertion(routineTasks[0]);
            return null;
        }
    }


    // Upadet Routine Tak AnyscTask
    public class UpdateRoutineTask extends AsyncTask<RoutineTasks, Void, Void> {
        RoutineTaskDao routineTaskDao;

        public UpdateRoutineTask(RoutineTaskDao routineTaskDao) {
            this.routineTaskDao = routineTaskDao;
        }

        @Override
        protected Void doInBackground(RoutineTasks... routineTasks) {
            routineTaskDao.updateRoutineTask(routineTasks[0]);
            return null;
        }
    }

    //Delete Routine AsyncTask
    public class DeleteRoutineTaskAsyncTask extends AsyncTask<RoutineTasks, Void, Void> {
        RoutineTaskDao routineTaskDao;

        public DeleteRoutineTaskAsyncTask(RoutineTaskDao routineTaskDao) {
            this.routineTaskDao = routineTaskDao;
        }

        @Override
        protected Void doInBackground(RoutineTasks... routineTasks) {
            routineTaskDao.deleteRoutineTask(routineTasks[0]);
            return null;
        }
    }
}

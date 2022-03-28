package com.tuinite.timemanagement.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.tuinite.timemanagement.DAO.ReminderDao;
import com.tuinite.timemanagement.db.AppDb;
import com.tuinite.timemanagement.entities.Reminder;

import java.util.Date;
import java.util.List;

@SuppressWarnings("ALL")
public class ReminderRepository {
    //Creating required member variables
    private ReminderDao reminderDao;
    private LiveData<List<Reminder>> reminderLiveData;
    //   LiveData<ReminderDao.RemainderUuid> remianderByTitle;

    //Creating the Constructor
    public ReminderRepository(Application application) {
        AppDb appDb = AppDb.getInstance(application);
        reminderDao = appDb.dao();
        //  reminderLiveData= reminderDao.getRemainder();

        //    remianderByTitle =reminderDao.loadUuid(title);

    }

    //Creating method for Reminder Inseration Operation
    public void reminderInsertion(Reminder reminder) {
        new InsertReminderAsyncTask(reminderDao).execute(reminder);
    }

    //Creating method for Reminder Deletion Operation
    public void deleteReminder(Reminder reminder) {
        new DeleteReminderAsyncTask(reminderDao).execute(reminder);
    }

    //Creating method for All Reminder Deletion Operation
    public void deleteAllReminders() {

        new DeleteAllRemindersAsyncTask(reminderDao).execute();
    }


    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Reminder>> getRemainder(Date dayStart, Date dayEnd) {
        return reminderDao.getRemainder(dayStart, dayEnd);
    }

    public LiveData<List<Reminder>> getSingleReminder(int reminderId) {
        return reminderDao.getSingleReminder(reminderId);
    }

    public void updateReminder(Reminder reminder) {
        new UpdateReminderAsyncTask(reminderDao).execute(reminder);
    }


    private static class PriorityUpdateParams {
        int uuid;
        int priority;


        PriorityUpdateParams(int uuid, int priority) {
            this.uuid = uuid;
            this.priority = priority;

        }
    }

   /* public void updatePriority(int uuid, int priority) {

        PriorityUpdateParams params = new PriorityUpdateParams(uuid,priority);
        new UpdatePriorityAsyncTask(reminderDao).execute(params);
    }*/


    // public LiveData<List<ReminderDao.ReminderUuid>> getRemainderUuid(String title) {
    //  return reminderDao.getReminderUuid(title);
    // }




    /*
    For LiveDta room automatically perform the operation in backgroud thread but for other database
    database operation we have to execute them in background thread manually because room does not
    allow database operation on main thread as it could freeze the app
    */

    //AsyncTask for executing Inserstion operation on background thread
    private static class InsertReminderAsyncTask extends AsyncTask<Reminder, Void, Void> {
        private ReminderDao reminderDao;

        private InsertReminderAsyncTask(ReminderDao reminderDao) {
            this.reminderDao = reminderDao;
        }

        @Override
        protected Void doInBackground(Reminder... reminders) {
            reminderDao.reminderInsertion(reminders[0]);
            return null;
        }
    }


    //AsyncTask for executing Deletion operation on background thread
    public static class DeleteReminderAsyncTask extends AsyncTask<Reminder, Void, Void> {
        private ReminderDao reminderDao;

        private DeleteReminderAsyncTask(ReminderDao reminderDao) {
            this.reminderDao = reminderDao;
        }


        @Override
        protected Void doInBackground(Reminder... reminders) {
            reminderDao.deleteReminder(reminders[0]);
            return null;
        }
    }


    //AsyncTask for executing All Deletion operation on background thread
    public static class DeleteAllRemindersAsyncTask extends AsyncTask<Void, Void, Void> {
        private ReminderDao reminderDao;

        private DeleteAllRemindersAsyncTask(ReminderDao reminderDao) {
            this.reminderDao = reminderDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            reminderDao.deleteAllReminders();
            return null;
        }
    }

    public static class UpdateReminderAsyncTask extends AsyncTask<Reminder, Void, Void> {
        ReminderDao reminderDao;

        public UpdateReminderAsyncTask(ReminderDao reminderDao) {
            this.reminderDao = reminderDao;
        }

        @Override
        protected Void doInBackground(Reminder... reminders) {
            reminderDao.updateReminder(reminders[0]);
            return null;
        }
    }



 /*   public static  class UpdatePriorityAsyncTask extends AsyncTask<PriorityUpdateParams,Void,Void>{
        private ReminderDao reminderDao;

        public UpdatePriorityAsyncTask(ReminderDao reminderDao) {

            this.reminderDao=reminderDao;
        }



        @Override
        protected Void doInBackground(PriorityUpdateParams... priorityUpdateParams) {
            int priority =priorityUpdateParams[0].priority;
            int uuid = priorityUpdateParams[0].uuid;


             reminderDao.updatePriority(uuid, priority);
            return null;
        }
    }*/


}


/*
Repository is not the part of android architecture components but it is a best practise to use
repositories as it provides a abstraction layer between various data sources and rest of the app.

The repository class will be responsible for interacting with the Room database on behalf of the
ViewModel and will need to provide methods that use the DAO to insert, delete and query product
records. With the exception of the getAllProducts() DAO method (which returns a LiveData object)
these database operations will need to be performed on separate threads from the main thread using
the AsyncTask class.


Why use a Repository?
A Repository manages queries and allows you to use multiple backends. In the most common example,
the Repository implements the logic for deciding whether to fetch data from a network or use results
cached in a local database.
*/

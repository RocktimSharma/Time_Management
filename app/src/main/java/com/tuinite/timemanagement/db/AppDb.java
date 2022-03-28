package com.tuinite.timemanagement.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.tuinite.timemanagement.DAO.ReminderDao;
import com.tuinite.timemanagement.DAO.RoutineDao;
import com.tuinite.timemanagement.DAO.RoutineProgressDao;
import com.tuinite.timemanagement.DAO.RoutineTaskDao;
import com.tuinite.timemanagement.entities.Reminder;
import com.tuinite.timemanagement.entities.Routine;
import com.tuinite.timemanagement.entities.RoutineProgress;
import com.tuinite.timemanagement.entities.RoutineTasks;
import com.tuinite.timemanagement.helper_classes.DateConverter;

@Database(entities = {Reminder.class, Routine.class, RoutineTasks.class, RoutineProgress.class}, version = 3, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDb extends RoomDatabase {

    private static AppDb INSTANCE;

    public static synchronized AppDb getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDb.class, "time_management_database").fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract ReminderDao dao();

    public abstract RoutineDao routineDao();

    public abstract RoutineTaskDao routineTaskDao();

    public abstract RoutineProgressDao routineProgressDao();
}
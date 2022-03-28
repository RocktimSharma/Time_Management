package com.tuinite.timemanagement.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.tuinite.timemanagement.entities.RoutineTasks;

import java.util.Date;
import java.util.List;

@androidx.room.Dao
public interface RoutineTaskDao {
    @Insert
    void routineTaskInsertion(RoutineTasks routineTasks);

    @Query("SELECT * FROM RoutineTasks WHERE taskRoutineId=:routineId")
    LiveData<List<RoutineTasks>> getRoutineTasks(int routineId);

    @Query("SELECT * FROM RoutineTasks WHERE taskId=:taskId")
    LiveData<List<RoutineTasks>> getSingleRoutineTask(int taskId);

    @Query("SELECT * FROM RoutineTasks WHERE taskStartTime>=:endTime ORDER BY taskStartTime ASC LIMIT 1")
    LiveData<List<RoutineTasks>> getNextTask(Date endTime);

    @Update
    void updateRoutineTask(RoutineTasks routineTasks);

    @Delete
    void deleteRoutineTask(RoutineTasks routineTasks);


}

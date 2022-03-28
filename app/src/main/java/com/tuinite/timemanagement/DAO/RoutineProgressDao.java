package com.tuinite.timemanagement.DAO;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tuinite.timemanagement.entities.RoutineProgress;

import java.util.Date;
import java.util.List;

@Dao
public interface RoutineProgressDao {

    @Insert
    void routineProgressInsertion(RoutineProgress routineProgress);

    @Query("SELECT * FROM RoutineProgress WHERE progressDate=:progressDate AND progressRoutineId=:routineId")
    LiveData<List<RoutineProgress>> getRoutineProgress(Date progressDate, int routineId);


    @Query("UPDATE RoutineProgress SET  taskEndTime=:progressEndTime WHERE progressDate=:progressDate AND progressTaskId=:taskId")
    void updateEndTime(Date progressEndTime, Date progressDate, int taskId);
}

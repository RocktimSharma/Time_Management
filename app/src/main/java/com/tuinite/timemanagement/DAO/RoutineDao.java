package com.tuinite.timemanagement.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.tuinite.timemanagement.entities.Routine;
import com.tuinite.timemanagement.relations.RoutineWithRoutineTask;

import java.util.List;

@androidx.room.Dao
public interface RoutineDao {

    //Insert Routine
    @Insert
    long routineInsertion(Routine routine);

    //Get Routines
    @Query("SELECT * FROM Routine ORDER BY routineId DESC ")
    LiveData<List<Routine>> getRoutine();


    //Delete Routine
    @Delete
    void deleteRoutine(Routine routine);


    @Transaction
    @Query("SELECT * FROM Routine WHERE routineId=:routineID")
    LiveData<List<RoutineWithRoutineTask>> getRoutineWithRoutineTasks(int routineID);


    @Query("UPDATE Routine SET active= :isActive WHERE routineId=:routineID")
    int updateRoutineStatus(int routineID, boolean isActive);


    @Update
    void updateRoutine(Routine routine);


}

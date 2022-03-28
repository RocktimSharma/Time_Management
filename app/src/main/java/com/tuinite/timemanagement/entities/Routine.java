package com.tuinite.timemanagement.entities;


import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "Routine", indices = {@Index(value = "routineTitle", unique = true)})
public class Routine {
    @PrimaryKey(autoGenerate = true)
    private int routineId;
    @NonNull
    private String routineTitle;
    @Embedded
    @NonNull
    private RoutineDays routineDays;
    @NonNull
    private Boolean active;


    public Routine(String routineTitle, RoutineDays routineDays, Boolean active) {
        this.routineTitle = routineTitle;
        this.routineDays = routineDays;
        this.active = active;
    }

    public int getRoutineId() {
        return routineId;
    }

    public void setRoutineId(int routineId) {
        this.routineId = routineId;
    }

    public String getRoutineTitle() {
        return routineTitle;
    }

    public RoutineDays getRoutineDays() {
        return routineDays;
    }

    public Boolean getActive() {
        return active;
    }
}




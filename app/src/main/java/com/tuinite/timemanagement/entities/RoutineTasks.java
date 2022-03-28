package com.tuinite.timemanagement.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.tuinite.timemanagement.helper_classes.DateConverter;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.RESTRICT;

//,foreignKeys = @ForeignKey(entity = Routine.class,parentColumns = "routineId",childColumns = "itemId",onDelete = CASCADE)
@Entity(tableName = "RoutineTasks", foreignKeys = @ForeignKey(entity = Routine.class, parentColumns = "routineId", childColumns = "taskRoutineId", onDelete = CASCADE, onUpdate = RESTRICT))
public class RoutineTasks {
    @PrimaryKey(autoGenerate = true)
    private int taskId;
    @NonNull
    private int taskRoutineId;
    @NonNull
    private String taskTitle;
    @NonNull
    @TypeConverters({DateConverter.class})
    private Date taskStartTime;
    @NonNull
    @TypeConverters({DateConverter.class})
    private Date taskEndTime;
    private String taskNote;
    @NonNull
    private Boolean taskAlarm;


    public RoutineTasks(int taskRoutineId, String taskTitle, Date taskStartTime, Date taskEndTime, String taskNote, Boolean taskAlarm) {
        this.taskRoutineId = taskRoutineId;
        this.taskTitle = taskTitle;
        this.taskStartTime = taskStartTime;
        this.taskEndTime = taskEndTime;
        this.taskNote = taskNote;
        this.taskAlarm = taskAlarm;

    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskRoutineId() {
        return taskRoutineId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public Date getTaskStartTime() {
        return taskStartTime;
    }

    public Date getTaskEndTime() {
        return taskEndTime;
    }

    public String getTaskNote() {
        return taskNote;
    }


    public Boolean getTaskAlarm() {
        return taskAlarm;
    }

}

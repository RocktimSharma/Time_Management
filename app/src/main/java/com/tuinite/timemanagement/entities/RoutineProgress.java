package com.tuinite.timemanagement.entities;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.TypeConverters;

import com.tuinite.timemanagement.helper_classes.DateConverter;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.RESTRICT;

@Entity(tableName = "RoutineProgress", primaryKeys = {"progressDate", "progressRoutineId", "progressTaskId"}, foreignKeys = {@ForeignKey(entity = RoutineTasks.class, parentColumns = {"taskId"}, childColumns = {"progressTaskId"}, onDelete = CASCADE, onUpdate = RESTRICT),
        @ForeignKey(entity = Routine.class, parentColumns = {"routineId"}, childColumns = {"progressRoutineId"}, onDelete = CASCADE, onUpdate = RESTRICT)})
public class RoutineProgress {
    @NonNull
    @TypeConverters(DateConverter.class)
    Date progressDate;
    int progressRoutineId;
    int progressTaskId;
    @NonNull
    @TypeConverters(DateConverter.class)
    Date taskStartTime;
    @NonNull
    @TypeConverters(DateConverter.class)
    Date taskEndTime;

    public RoutineProgress(Date progressDate, int progressRoutineId, int progressTaskId, Date taskStartTime, Date taskEndTime) {
        this.progressDate = progressDate;
        this.progressRoutineId = progressRoutineId;
        this.progressTaskId = progressTaskId;
        this.taskStartTime = taskStartTime;
        this.taskEndTime = taskEndTime;
    }

    public Date getProgressDate() {
        return progressDate;
    }

    public int getProgressRoutineId() {
        return progressRoutineId;
    }

    public int getProgressTaskId() {
        return progressTaskId;
    }

    public Date getTaskStartTime() {
        return taskStartTime;
    }

    public Date getTaskEndTime() {
        return taskEndTime;
    }
}

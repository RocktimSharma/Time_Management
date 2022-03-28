package com.tuinite.timemanagement.entities;

import androidx.annotation.NonNull;
import androidx.room.TypeConverters;

import com.tuinite.timemanagement.helper_classes.DateConverter;

import java.util.Date;

public class ProgressID {
    @TypeConverters({DateConverter.class})
    private Date progressDate;
    private int progressRoutineId;
    private int progressItemId;

    public ProgressID(@NonNull Date progressDate, int progressRoutineId, int progressItemId) {
        this.progressDate = progressDate;
        this.progressRoutineId = progressRoutineId;
        this.progressItemId = progressItemId;
    }
}

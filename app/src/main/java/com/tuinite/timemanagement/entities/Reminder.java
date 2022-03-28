package com.tuinite.timemanagement.entities;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.tuinite.timemanagement.helper_classes.DateConverter;

import java.util.Date;

@Entity(tableName = "Reminder")  //indices = {@Index(value = {"title","uuid"}, unique = true)}
public class Reminder {
    @PrimaryKey(autoGenerate = true)
    private int reminderId;
    @TypeConverters(DateConverter.class)
    @NonNull
    private Date dateTime;
    @NonNull
    private String title;
    private String note;
    @NonNull
    private Boolean alarm;

    public Reminder(Date dateTime, String title, String note, Boolean alarm) {
        this.dateTime = dateTime;
        this.title = title;
        this.note = note;
        this.alarm = alarm;
    }

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public Boolean getAlarm() {
        return alarm;
    }
}

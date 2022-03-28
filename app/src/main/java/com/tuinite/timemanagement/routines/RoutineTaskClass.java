package com.tuinite.timemanagement.routines;

import java.util.Date;

public class RoutineTaskClass {
    private String itemTitle, itemNote, tag;

    private Date startTime, endTime;
    private Boolean alarm, block;

    public RoutineTaskClass(String itemTitle, String itemNote, String tag, Date startTime, Date endTime, Boolean alarm, Boolean block) {
        this.itemTitle = itemTitle;
        this.itemNote = itemNote;
        this.tag = tag;
        this.startTime = startTime;
        this.endTime = endTime;
        this.alarm = alarm;
        this.block = block;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public String getItemNote() {
        return itemNote;
    }

    public String getTag() {
        return tag;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Boolean getAlarm() {
        return alarm;
    }

    public Boolean getBlock() {
        return block;
    }
}

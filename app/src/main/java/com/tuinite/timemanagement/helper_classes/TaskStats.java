package com.tuinite.timemanagement.helper_classes;

public class TaskStats {
    String taskTitle;
    long plannedTaskTime;
    long workTaskTime;

    public TaskStats(String taskTitle, long plannedTaskTime, long workTaskTime) {
        this.taskTitle = taskTitle;
        this.plannedTaskTime = plannedTaskTime;
        this.workTaskTime = workTaskTime;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public long getPlannedTaskTime() {
        return plannedTaskTime;
    }

    public long getWorkTaskTime() {
        return workTaskTime;
    }
}

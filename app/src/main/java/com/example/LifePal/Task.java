package com.example.LifePal;
import java.lang.String;


public class Task {

    private String TaskName;

    private String description;
    private String time_left;

    private String time_spent;

    private String Tag;

    private boolean started,finished, isStopWatch;

    public Task(String TaskName, String description, String time_left, String Tag, boolean isStopWatch) {
        this.TaskName = TaskName;
        this.description = description;
        this.time_left = time_left;
        this.Tag = Tag;
        this.time_spent = "0";
        this.started = false;
        this.finished = false;
        this.isStopWatch = isStopWatch;
    }

    public String getTaskName() {
        return TaskName;
    }

    public String getDescription() {
        return description;
    }

    public String getTimeLeft() {
        return time_left;
    }

    public String getTimeSpent() {
        return time_spent;
    }

    public String getTag() {
        return Tag;
    }

    public boolean getStarted() {
        return started;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }
    public boolean getFinished() {
        return finished;
    }
    public boolean getIsStopWatch() {return isStopWatch;}

    public void setTaskName(String TaskName) {
        this.TaskName = TaskName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimeLeft(String time_left) {
        this.time_left = time_left;
    }

    public void setTimeSpent(String time_spent) {
        this.time_spent = time_spent;
    }

    public void setTag(String Tag) {
        this.Tag = Tag;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}

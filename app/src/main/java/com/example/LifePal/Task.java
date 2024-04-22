package com.example.LifePal;
import java.lang.String;
import java.util.Calendar;


public class Task {

    private String TaskName;

    private String description;
    private String time_left;

    private String time_spent;

    private String Tag;

    private boolean started,finished, isStopWatch;

    private Integer Year;
    private Integer Month;
    private Integer Day;
    private Integer Hour;
    private Integer Minute;
    private Integer Second;

    public Task() {
    }

    public Task(String TaskName, String description, String time_left, String Tag, boolean isStopWatch) {
        this.TaskName = TaskName;
        this.description = description;
        this.time_left = time_left;
        this.Tag = Tag;
        this.time_spent = "0";
        this.started = false;
        this.finished = false;
        this.isStopWatch = isStopWatch;
        this.Year = Calendar.YEAR;
        this.Month = Calendar.MONTH;
        this.Day = Calendar.DATE;
        this.Hour = Calendar.HOUR;
        this.Minute = Calendar.MINUTE;
        this.Second = Calendar.SECOND;

    }

    public Integer getYear() {
        return Year;
    }

    public Integer getMonth() {
        return Month;
    }

    public Integer getDay() {
        return Day;
    }

    public Integer getHour() {
        return Hour;
    }

    public Integer getMinute() {
        return Minute;
    }

    public Integer getSecond() {
        return Second;
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

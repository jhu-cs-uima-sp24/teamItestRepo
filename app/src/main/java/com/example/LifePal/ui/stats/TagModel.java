package com.example.LifePal.ui.stats;

public class TagModel {
    private String name;
    private int time;

    public TagModel(String name, int time) {

        this.name = name;
        this.time = time;
    }

    // Getters
    public String getName() { return name; }
    public String getTime() {
        int hours = time/60;
        int min = time%60;
        return Integer.toString(hours) + "M " + Integer.toString(min) + "S";
    }
}

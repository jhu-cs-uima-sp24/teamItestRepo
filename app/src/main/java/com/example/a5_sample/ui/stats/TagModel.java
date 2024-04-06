package com.example.a5_sample.ui.stats;

public class TagModel {
    private String name;
    private int time;

    public TagModel(String name, int time) {
        this.name = name;
        this.time = time;
    }

    // Getters
    public String getName() { return name; }
    public String getTime() { return Integer.toString(time); }
}

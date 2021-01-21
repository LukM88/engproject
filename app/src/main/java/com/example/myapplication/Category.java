package com.example.myapplication;

import java.util.List;

public class Category {
    private int id;
    private String name;
    private String color;
    public Category(int id, String name, String color){
        this.color = color;
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public String getColor() {
        return color;
    }
    public String getName(){
        return name;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
}

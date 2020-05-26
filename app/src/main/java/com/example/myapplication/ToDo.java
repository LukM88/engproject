package com.example.myapplication;

import java.util.Date;

public class ToDo {
    private String ID;
    private String name;
    private String description;
    private String HH;
    private String MM;
    private String priority;
    private String day;
    private String month;
    private String year;
    private String owner;
    private Boolean state;
    private Integer photo;
    ToDo(Date date){
        this.name="ToDo";
        this.description="description";
        this.HH="24";
        this.MM="00";
        this.priority="High";
    }
    ToDo(){

    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name=name;
    }
    public String getName() {
        return name;
    }

    public String getPriority() {
        return priority;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getHH() {
        return HH;
    }

    public void setHH(String HH) {
        this.HH = HH;
    }

    public String getMM() {
        return MM;
    }

    public void setMM(String MM) {
        this.MM = MM;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getTime(){
        return getHH()+":"+getMM();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getID() {
        return this.ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}


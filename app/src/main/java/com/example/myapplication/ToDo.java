package com.example.myapplication;

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
    private Boolean state;
    private String imgPath;
    private String notification;
    private String durationInMinutes;
    private String repeat;

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
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


    public String getID() {
        return this.ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
    public String getImgPath() {
        return this.imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath == null ? "" : imgPath;
    }
    public String getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(String duration) {
        this.durationInMinutes = duration;
    }

    public void setNotification(String notification){
        this.notification=notification;
    }
    public String getNotification(){
        return this.notification;
    }

    public String getDate(){
        MyDate myDate = new MyDate();
        myDate.setDate(getDay(), getMonth(), getYear());
        return myDate.getDate();
    }
}


package com.example.myapplication;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MyDate {
    private String day;
    private String month;
    private String year;
    private String date;
    private final String format = "yyyy-mm-dd";

    public MyDate(){
        this.day = Calendar.getInstance().get(Calendar.DATE) < 10 ? 0 + String.valueOf(Calendar.getInstance().get(Calendar.DATE))
                                                                  : String.valueOf(Calendar.getInstance().get(Calendar.DATE));
        this.month = Calendar.getInstance().get(Calendar.MONTH) < 10 ? 0 + String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1)
                                                                     : String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
        this.year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        setDate();
    }
    public void setDay(String day) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.set(Calendar.DATE, Integer.parseInt(day));
            if (calendar.get(Calendar.DATE) < 10 && !day.contains("0")){
                this.day = 0 + day;
            } else{
                this.day = day;
            }
                setDate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setMonth(String month) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.set(Calendar.MONTH, Integer.parseInt(month));
            if (calendar.get(Calendar.MONTH) < 10 && !month.contains("0")){
                this.month = 0 + month;
            } else{
                this.month = month;
            }
            setDate();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setDate() {
        this.date = format;
        this.date = this.date.replace("yyyy", year);
        this.date = this.date.replace("mm", month);
        this.date = this.date.replace("dd", day);
    }

    public void setYear(String year) {
        try {
            Integer.parseInt(year);
            this.year=year;
            setDate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setDate(String day, String month, String year) {
        setDay(day);
        setMonth(month);
        setYear(year);
        setDate();
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getDate() {
        return date;
    }
    public MyDate getWeekDates(String dayOfWeek) {
        Calendar date = Calendar.getInstance();
        switch(dayOfWeek){
            case "mo":
                date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE) - date.get(Calendar.DAY_OF_WEEK) + Calendar.MONDAY);
                break;
            case "tu":
                date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE) - date.get(Calendar.DAY_OF_WEEK) + Calendar.TUESDAY);
                break;
            case "we":
                date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE) - date.get(Calendar.DAY_OF_WEEK) + Calendar.WEDNESDAY);
                break;
            case "th":
                date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE) - date.get(Calendar.DAY_OF_WEEK) + Calendar.THURSDAY);
                break;
            case "fr":
                date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE) - date.get(Calendar.DAY_OF_WEEK) + Calendar.FRIDAY);
                break;
            case "sa":
                date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE) - date.get(Calendar.DAY_OF_WEEK) + Calendar.SATURDAY);
                break;
            case "su":
                if(date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                    date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE) - date.get(Calendar.DAY_OF_WEEK) + Calendar.SUNDAY);
                } else{
                    date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE) - date.get(Calendar.DAY_OF_WEEK) + 8);
                }

                break;
        }
        MyDate mydate = new MyDate();
        mydate.setDate(Integer.toString(date.get(Calendar.DATE)), Integer.toString(date.get(Calendar.MONTH) + 1), Integer.toString(date.get(Calendar.YEAR)));
        return mydate;
    }

    public Map<String, String> increaseDateByDays(int increaseBy) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, increaseBy);
        setDate(Integer.toString(calendar.get(Calendar.DATE)),
                Integer.toString(calendar.get(Calendar.MONTH) + 1),
                Integer.toString(calendar.get(Calendar.YEAR)));
        return new HashMap<String,String>(){{
            put("day", getDay());
            put("month", getMonth());
            put("year", getYear());
        }};
    }
    public Map<String, String> increaseDateByMonths(int increaseBy) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, increaseBy);
        setDate(Integer.toString(calendar.get(Calendar.DATE)),
                Integer.toString(calendar.get(Calendar.MONTH) + 1),
                Integer.toString(calendar.get(Calendar.YEAR)));
        return new HashMap<String, String>(){{
            put("day", getDay());
            put("month", getMonth());
            put("year", getYear());
        }};
    }
    public Map<String, String> increaseDateByYears(int increaseBy) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, increaseBy);
        setDate(Integer.toString(calendar.get(Calendar.DATE)),
                Integer.toString(calendar.get(Calendar.MONTH) + 1),
                Integer.toString(calendar.get(Calendar.YEAR)));
        return new HashMap<String, String>(){{
            put("day", getDay());
            put("month", getMonth());
            put("year", getYear());
        }};
    }
}

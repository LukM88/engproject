package com.example.myapplication;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class MyDate {
    private String day;
    private String month;
    private String year;
    private String date;
    private String format = "yyyy-mm-dd";




    public MyDate(){
        String currentDateTimeString = DateFormat.getDateInstance(DateFormat.SHORT, Locale.UK).format(new Date());
       // String currentDateTimeString2 = DateFormat.getDateInstance(DateFormat.DEFAULT,Locale.UK).format(new Date());
       /* if(currentDateTimeString.equals(currentDateTimeString2)){
            //TODO na windzie napraw to do nwego formaty
            System.out.println(currentDateTimeString);
            System.out.println(currentDateTimeString.substring(0,2));
            setDay(currentDateTimeString.substring(0,2));
            setMonth(currentDateTimeString.substring(4,5));
            setYear(currentDateTimeString2.substring(currentDateTimeString2.length()-4,currentDateTimeString2.length()));
            getDate();*/
      //  }else{

            setDay(currentDateTimeString.substring(0, 2));
            setMonth(currentDateTimeString.substring(3, 5));
            setYear(currentDateTimeString.substring(currentDateTimeString.length() - 4));

       // }


    }
    public void setDay(String day) {
        try {
            if (Integer.parseInt(day) <= 31 && Integer.parseInt(day) > 0) {
                if (Integer.parseInt(day) < 10 && day.length() == 1){
                    this.day = 0 + day;
                } else{
                    this.day = day;
                }
                setDate();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setMonth(String month) {
        try {
            if (Integer.parseInt(month) <= 12 && Integer.parseInt(month) > 0) {
                if(Integer.parseInt(month) < 10 && month.length() == 1){
                    this.month = 0 + month;
                }else{
                    this.month = month;
                    setDate();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setDate() {
        String date;
        String separator;
        format = format.toUpperCase();
        if(!format.contains("M")
                || !format.contains("Y")
                || !format.contains("D")){
            format = "yyyy-mm-dd";
            setDate();
        }
        if (format.indexOf("Y") > format.indexOf("M")){
            separator = format.substring(format.indexOf("Y") - 1, format.indexOf("Y"));
            if (format.indexOf("M") > format.indexOf("D")){
                date = day + separator + month + separator + year;
            } else if(format.indexOf("Y") > format.indexOf("D")){
                date = month + separator + day + separator + year;
            } else{
                date = month + separator + year + separator + day;
            }
        } else{
            separator = format.substring(format.indexOf("M") - 1, format.indexOf("M"));
            if (format.indexOf("M") < format.indexOf("D")){
                date = year + separator + month + separator + day;
            } else if(format.indexOf("M") > format.indexOf("D")){
                date = year + separator + day + separator + month;
            } else{
                date = day + separator + year + separator + month;
            }
        }
        this.date = date;
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
        String date;
        String separator;
        format = format.toUpperCase();
            if (format.lastIndexOf("Y") == format.length() - 1){
                separator=format.substring (format.lastIndexOf("Y") - 4,format.lastIndexOf("Y") - 3);
                if (format.lastIndexOf("M") < format.lastIndexOf("D")){
                    date = month + separator + day + separator + year;
                } else{
                    date = day + separator + month + separator + year;
                }
            } else if (format.lastIndexOf("D") == format.length() - 1){
                separator = format.substring(format.lastIndexOf("D") - 2, format.lastIndexOf("D") - 1);
                if(format.lastIndexOf("M") < format.lastIndexOf("Y")){
                    date = month+separator+year + separator + day;
                } else{
                    date = year + separator + month + separator + day;
                }
            } else if(format.lastIndexOf("M") == format.length() - 1){
                separator = format.substring(format.lastIndexOf("M") - 2, format.lastIndexOf("M") - 1);
                if (format.lastIndexOf("Y") < format.lastIndexOf("D")){
                    date = year + separator + day + separator + month;
                } else{
                    date = day + separator + year + separator + month;
                }
            } else{
                separator = "-";
                date = day + separator + month + separator + year;
            }



        this.date = date;
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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
        setDate(this.day,this.month,this.year);
    }


}

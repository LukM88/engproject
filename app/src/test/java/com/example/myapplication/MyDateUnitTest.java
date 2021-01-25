package com.example.myapplication;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;
public class MyDateUnitTest {



    @Test
    public void MyDateGettersTest(){
        assertEquals(String.valueOf(Calendar.getInstance().get(Calendar.DATE)), new MyDate().getDay());
        assertEquals(0 + String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1), new MyDate().getMonth());
        assertEquals(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), new MyDate().getYear());
        assertEquals(Calendar.getInstance().get(Calendar.YEAR) +
                "-" + 0 + (Calendar.getInstance().get(Calendar.MONTH) + 1) +
                "-" + Calendar.getInstance().get(Calendar.DATE), new MyDate().getDate());
        Calendar date = Calendar.getInstance();
        date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE) - date.get(Calendar.DAY_OF_WEEK) + Calendar.MONDAY);
        assertEquals(Integer.parseInt(new MyDate().getWeekDates("mo").getDay()), date.get(Calendar.DATE));
        assertEquals(date.get(Calendar.MONTH) + 1, Integer.parseInt(new MyDate().getWeekDates("mo").getMonth()));
        assertEquals(date.get(Calendar.YEAR), Integer.parseInt(new MyDate().getWeekDates("mo").getYear()));
        }
    @Test
    public void MyDateSetterTest(){
        MyDate myDate = new MyDate();
        myDate.setDay("22");
        myDate.setMonth("1");
        myDate.setYear("2020");
        assertEquals(myDate.getYear(), "2020");
        assertEquals(myDate.getMonth(), "01");
        assertEquals(myDate.getDay(), "22");
    }
}

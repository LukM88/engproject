package com.example.myapplication;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;
public class MyDateUnitTest {



    @Test
    public void MyDateGettersTest(){
        MyDate date = new MyDate();
        date.setDate("09", "02", "2020");
        assertEquals("09", date.getDay());
        assertEquals("02", date.getMonth());
        assertEquals("2020", date.getYear());
        assertEquals("2020-02-09", date.getDate());
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.set(calendarDate.get(Calendar.YEAR),
                        calendarDate.get(Calendar.MONTH),
                    calendarDate.get(Calendar.DATE) - calendarDate.get(Calendar.DAY_OF_WEEK) + Calendar.MONDAY);
        assertEquals(Integer.parseInt(new MyDate().getWeekDates("mo").getDay()), calendarDate.get(Calendar.DATE));
        assertEquals(calendarDate.get(Calendar.MONTH) + 1, Integer.parseInt(new MyDate().getWeekDates("mo").getMonth()));
        assertEquals(calendarDate.get(Calendar.YEAR), Integer.parseInt(new MyDate().getWeekDates("mo").getYear()));
        }
}

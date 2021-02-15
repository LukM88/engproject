package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JobServiceForApp extends JobService {
    private static final String TAG = "JobServiceForApp";
    private boolean jobCancelled = false;
    private NotificationManagerCompat notificationManagerCompat;
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "JOB STARTED");
        doBackgroundWork(params);
        return true;
    }

    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (ToDo todo : new DatabaseHelper(getBaseContext()).getToDoesForWeek(new MyDate())){
                    if(jobCancelled){
                        return;
                    }
                    Calendar date = Calendar.getInstance();
                    date.set(Calendar.YEAR, Integer.parseInt(todo.getYear()));
                    date.set(Calendar.MONTH, Integer.parseInt(todo.getMonth()) - 1);
                    date.set(Calendar.DATE, Integer.parseInt(todo.getDay()));
                    date.set(Calendar.HOUR_OF_DAY, Integer.parseInt(todo.getHH()));
                    date.set(Calendar.MINUTE, Integer.parseInt(todo.getMM()));
                    date.set(Calendar.SECOND, 0);
                    String notificationDelayOption = todo.getNotification();
                    int delayValue = Integer.parseInt(String.valueOf(notificationDelayOption.charAt(0)));
                    if(notificationDelayOption.contains("day")){
                        date.set(Calendar.DATE, date.get(Calendar.DATE) - delayValue);
                    } else if(notificationDelayOption.contains("hour")){
                        date.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY) - delayValue);
                    } else if(notificationDelayOption.contains("week")){
                        date.set(Calendar.DATE, date.get(Calendar.DATE) - delayValue * 7);
                    }
                    if(date.before(Calendar.getInstance())){
                        continue;
                    }
                    startAlarm(date, todo.getName(), todo.getDescription());
                }
                for (ToDo todo : new DatabaseHelper(getApplicationContext()).getToDoes(new MyDate())){

                    MyDate date = new MyDate();
                    if(todo.getRepeat().contains("day")){
                        date.increaseDateByDays(Integer.parseInt(String.valueOf(todo.getRepeat().charAt(0))));
                        todo.setDay(date.getDay());
                        todo.setMonth(date.getMonth());
                        todo.setYear(date.getYear());
                    } else if(todo.getRepeat().contains("month")){
                        date.increaseDateByMonths(Integer.parseInt(String.valueOf(todo.getRepeat().charAt(0))));
                        todo.setDay(date.getDay());
                        todo.setMonth(date.getMonth());
                        todo.setYear(date.getYear());
                    } else if(todo.getRepeat().equals("year")){
                        todo.setYear(new MyDate().increaseDateByYears(1).get("year"));
                    } else if(todo.getRepeat().equals("week")){
                        date.increaseDateByDays(7);
                        todo.setDay(date.getDay());
                        todo.setMonth(date.getMonth());
                        todo.setYear(date.getYear());
                    } else if(todo.getRepeat().equals("weekend")){
                        todo.setDay(new MyDate().getWeekDates("sa").increaseDateByDays(7).get("day"));
                        todo.setMonth(new MyDate().getWeekDates("sa").increaseDateByDays(7).get("month"));
                        todo.setYear(new MyDate().getWeekDates("sa").increaseDateByDays(7).get("year"));
                    }
                    Map<String, String> newRecord = new HashMap<String, String>();
                    newRecord.put("name", todo.getName());
                    newRecord.put("descriptions", todo.getDescription());
                    newRecord.put("HH", todo.getHH());
                    newRecord.put("MM", todo.getMM());
                    newRecord.put("duration", todo.getDurationInMinutes());
                    newRecord.put("priority", todo.getPriority());
                    newRecord.put("state", "");
                    newRecord.put("day", todo.getDay());
                    newRecord.put("month", todo.getMonth());
                    newRecord.put("year", todo.getYear());
                    newRecord.put("imgPath", todo.getImgPath());
                    newRecord.put("notification", todo.getNotification());
                    newRecord.put("category", new DatabaseHelper(getApplicationContext()).getTodoCategory(todo.getID()).getName());
                    newRecord.put("repeat", todo.getRepeat());

                    try{
                        if(!todo.getRepeat().equals("None")){
                            new DatabaseHelper(getApplicationContext()).insertEvent(newRecord);
                            if(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 7
                                    && todo.getRepeat().equals("weekend")){
                                todo.setDay(new MyDate().increaseDateByDays(1).get("day"));
                                todo.setMonth(new MyDate().increaseDateByDays(1).get("month"));
                                todo.setYear(new MyDate().increaseDateByDays(1).get("year"));
                                newRecord.put("day", todo.getDay());
                                newRecord.put("month", todo.getDay());
                                newRecord.put("year", todo.getYear());
                                new DatabaseHelper(getApplicationContext()).insertEvent(newRecord);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        Log.d(TAG, "JOB FINISHED");
                        jobFinished(params, false);
                    }
                }
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "JOB UNFINISHED");
        jobCancelled = true;
        return false;
    }
    private void startAlarm(Calendar dateTime, String name, String description){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("name", name);
        intent.putExtra("description", description);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1,intent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, dateTime.getTimeInMillis(), pendingIntent);
    }
}

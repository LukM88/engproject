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
                Log.d(TAG, "JOB FINISHED");
                jobFinished(params, false);
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

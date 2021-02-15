package com.example.myapplication;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public  NavController navController;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_calender, R.id.navAddEvent,R.id.statisticsSelector)
                .setDrawerLayout(drawer)
                .build();
         navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        scheduleJob(this.getCurrentFocus());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final ListView listaWynikow = findViewById(R.id.listaWynikow);
        SearchView searchView = findViewById(R.id.serchBar) ;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                listaWynikow.setVisibility(View.VISIBLE);
                listaWynikow.setAdapter(new SearchAdapter(getBaseContext(),s));

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                listaWynikow.setVisibility(View.VISIBLE);
                listaWynikow.setAdapter(new SearchAdapter(getBaseContext(),s));

                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                listaWynikow.setVisibility(View.GONE);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void scheduleJob(View v){
        ComponentName componentName = new ComponentName(this, JobServiceForApp.class);
        JobInfo info = new JobInfo.Builder(123,componentName)
                .setPersisted(true)
                .setPeriodic(1000 * 60 * 60 * 24)
                .build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int result = scheduler.schedule(info);
        if(result == JobScheduler.RESULT_SUCCESS){
            Log.d(TAG, "success");
        } else{
            Log.d(TAG, "fail");
        }
    }
    public void cancelJob(View v){
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d(TAG, "canceled");
    }



}

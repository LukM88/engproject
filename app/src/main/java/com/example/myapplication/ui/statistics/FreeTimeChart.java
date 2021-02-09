package com.example.myapplication.ui.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Category;
import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.MyDate;
import com.example.myapplication.R;
import com.example.myapplication.ToDo;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;


public class FreeTimeChart extends Fragment {
    private BarChart chart;
    private final String[] week = new String[]{"mo", "tu", "we", "th", "fr", "sa", "su"};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_free_time_chart, container, false);
        chart = root.findViewById(R.id.barChart);
        setUpChart();
        return root;
    }

    private Number getFreeTime(String dayName) {
        List<ToDo> todoes = new DatabaseHelper(getContext()).getToDoes(new MyDate().getWeekDates(dayName.toLowerCase()));
        float tasksDuration = 0;
        for(ToDo todo : todoes){
            tasksDuration += Float.parseFloat(todo.getDurationInMinutes())/60;
        }
        if ((24 - tasksDuration - 8) < 1){
            return 0;
        } else{
            return 24 - tasksDuration - 8;
        }
    }

    private Number getOtherCategoriesTime(String dayName, float categoriesDuration){
        List<ToDo> todoes = new DatabaseHelper(getContext()).getToDoes(new MyDate().getWeekDates(dayName.toLowerCase()));
        float tasksDuration = 0;
        for(ToDo todo : todoes){
            tasksDuration += Float.parseFloat(todo.getDurationInMinutes())/60;
        }
            return tasksDuration - categoriesDuration;
    }
    private void setUpChart(){
        chart.setMaxVisibleValueCount(40);
        chart.setPinchZoom(false);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(false);
        chart.setHighlightFullBarEnabled(false);

        YAxis leftAxis = chart.getAxisLeft();

        leftAxis.setAxisMinimum(0f);
        chart.getAxisRight().setEnabled(false);
        XAxis xLabels = chart.getXAxis();
        xLabels.setValueFormatter(new IndexAxisValueFormatter(week));
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setYEntrySpace(16f);
        l.setWordWrapEnabled(true);
        l.setFormToTextSpace(16f);
        l.setDrawInside(false);
        l.setFormSize(24f);
        l.setFormToTextSpace(24f);
        l.setXEntrySpace(16f);
        l.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);

        ArrayList<BarEntry> yValues = new ArrayList<>();
        for (int i = 0; i < week.length; i++){
            float[] durations = new float[new DatabaseHelper(getContext()).getCategories().size() + 3];
            durations[0] = 8;
            int j = 1;
            float totalDuration = 0;
            for(Category category : new DatabaseHelper(getContext()).getCategories()){
                durations[j] = (float)new DatabaseHelper(getContext()).getCategoryDayleDuration(category.getName(), new MyDate().getWeekDates(week[i]));
                totalDuration += durations[j];
                j++;
            }
            durations[j] = (float)getFreeTime(week[i]);
            durations[j+1] = (float)getOtherCategoriesTime(week[i],totalDuration);
            yValues.add(new BarEntry(i, durations));
        }
        BarDataSet set1 = new BarDataSet(yValues, null);

        set1.setDrawIcons(false);
        set1.setValueTextSize(16f);
        List<Category> categories = new DatabaseHelper(getContext()).getCategories();
        categories.add(0, new Category(999, "Sleep", "#DDDDDD"));
        categories.add(new Category(999, "Free Time", "#11ff11"));
        categories.add(new Category(999, "Other things", "#ff0000"));
        String[] categoriesNames = new String[categories.size()];
        int[] categoriesColors = new int[categories.size()];
        int j = 0;
        for(Category category : categories){
            categoriesNames[j] = category.getName();
            categoriesColors[j] = Color.parseColor(category.getColor());
            j++;
        }
        set1.setStackLabels(categoriesNames);
        set1.setColors(categoriesColors);
        BarData data = new BarData(set1);
        data.setValueTextSize(16f);
        chart.setData(data);
        chart.setFitBars(true);
        chart.invalidate();
    }
}
package com.example.myapplication.ui.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.MyDate;
import com.example.myapplication.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FinishedTasksGraph extends Fragment {
    private final Map<String, Integer> complited = new HashMap<String, Integer>();
    private PieChart pieChart;

    public static FinishedTasksGraph newInstance() {
       return new FinishedTasksGraph();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_done_tasks, container, false);
        Map<String, Integer> tasks = new DatabaseHelper(getContext()).getDoneEventsStatistics(new MyDate());
        complited.put("Undone", tasks.get("Undone"));
        complited.put("Done", tasks.get("Done"));
        pieChart = root.findViewById(R.id.pieChart);
        setData();
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        return root;
    }

    private void setData(){
        ArrayList<PieEntry> entries = new ArrayList<>();
        for(String key : complited.keySet()){
            entries.add(new PieEntry(complited.get(key), key));
        }
        PieDataSet pieDataSet = new PieDataSet(entries, "Tasks");
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(16f);

        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Done tasks");
        pieChart.setEntryLabelTextSize(16f);
        pieChart.setCenterTextSize(16f);
        pieChart.animate();
    }

}

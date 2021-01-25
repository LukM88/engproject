package com.example.myapplication.ui.statistics;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.MyDate;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class FinishedTasksGraph extends Fragment {
    private AnyChartView chart;
    private String[] groups = {"Not Done","Done"};
    private int[] complited;

    public static FinishedTasksGraph newInstance() {
       return new FinishedTasksGraph();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_done_tasks, container, false);
        final TextView textView = root.findViewById(R.id.textView);
        DatabaseHelper db = new DatabaseHelper(getContext());
        complited = db.getDoneEventsStatistics(new MyDate());
        chart = root.findViewById(R.id.wykres);
        setupChart();
        return root;
    }

    private void setupChart() {
        Pie pie = AnyChart.pie();
        pie.title("Done tasks for taday");
        List<DataEntry> dataEntries = new ArrayList<DataEntry>();
        for(int i = 0; i<groups.length; i++) {
            dataEntries.add(new ValueDataEntry(groups[i], complited[i]));
        }

        pie.data(dataEntries);
        chart.setChart(pie);
    }


}

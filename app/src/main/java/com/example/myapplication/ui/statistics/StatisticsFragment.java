package com.example.myapplication.ui.statistics;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends Fragment {
    private AnyChartView chart;
    private String[] groups = {"Wykonane","Nie Wykonane"};
    private int[] wykonaneValues = {1,4};

    private StatisticsViewModel mViewModel;

    public static StatisticsFragment newInstance() {
       return new StatisticsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mViewModel = ViewModelProviders.of(this).get(StatisticsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statistics2, container, false);
        final TextView textView = root.findViewById(R.id.textView);
        mViewModel.getText().observe(getViewLifecycleOwner(),new Observer<String>(){

            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        chart = root.findViewById(R.id.wykres);
        sertupChart();
        return root;
    }

    private void sertupChart() {
        //TODO zrób dane pobierane z bazy
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<DataEntry>();
        for(int i = 0; i<groups.length; i++) {
            dataEntries.add(new ValueDataEntry(groups[i], wykonaneValues[i]));
        }
        pie.data(dataEntries);
        chart.setChart(pie);
    }


}

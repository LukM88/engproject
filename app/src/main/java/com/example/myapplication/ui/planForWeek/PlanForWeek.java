package com.example.myapplication.ui.planForWeek;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.HeatDataEntry;
import com.anychart.charts.HeatMap;
import com.anychart.charts.Resource;
import com.anychart.enums.AvailabilityPeriod;
import com.anychart.enums.SelectionMode;
import com.anychart.enums.TimeTrackingMode;
import com.anychart.graphics.vector.SolidFill;
import com.anychart.scales.calendar.Availability;
import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.MyDate;
import com.example.myapplication.R;
import com.example.myapplication.ToDo;

import java.util.ArrayList;
import java.util.List;

public class PlanForWeek extends Fragment {

    private class ResourceDataEntry extends DataEntry {
        ResourceDataEntry(String name, Activity[] activities) {
            setValue("name", name);
            setValue("activities", activities);
        }
    }

    private class Activity extends DataEntry {
        Activity(String name, Interval[] intervals, String fill) {
            setValue("name", name);
            setValue("intervals", intervals);
            setValue("fill", fill);
        }
    }

    private class Interval extends DataEntry {
        Interval(String start, String end, Integer minutesPerDay) {
            setValue("start", start);
            setValue("end", end);
            setValue("minutesPerDay", minutesPerDay);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View root = inflater.inflate(R.layout.fragment_plan_for_week, container, false);

        AnyChartView chart = root.findViewById(R.id.plan);
        Resource resource = AnyChart.resource();

        resource.zoomLevel(1d)
                .timeTrackingMode(TimeTrackingMode.ACTIVITY_PER_CHART)
                .currentStartDate(new MyDate().getDate());
        System.out.println(new MyDate().getDate());

        resource.resourceListWidth(120);

        resource.calendar().availabilities(new Availability[] {
                new Availability(AvailabilityPeriod.DAY, (Double) null, 10d, true, (Double) null, (Double) null, 18d),
                new Availability(AvailabilityPeriod.DAY, (Double) null, 14d, false, (Double) null, (Double) null, 15d),
                new Availability(AvailabilityPeriod.WEEK, (Double) null, (Double) null, false, 5d, (Double) null, 18d),
                new Availability(AvailabilityPeriod.WEEK, (Double) null, (Double) null, false, 6d, (Double) null, 18d)
        });
        DatabaseHelper db = new DatabaseHelper(getContext());
        ArrayList<ToDo> ToDoes = db.getToDoes(new MyDate());
        List<DataEntry> data = new ArrayList<>();
        data.add(new ResourceDataEntry(
                                "8:00",
                new Activity[]{
                        new Activity(
                                "name",
                                new Interval[]{
                                        new Interval(new MyDate().getDate(), new MyDate().getDate(), 60)
                                                },
                                "#62BEC1")

                                }));



        resource.data(data);

        chart.setChart(resource);

        return root;
    }
}
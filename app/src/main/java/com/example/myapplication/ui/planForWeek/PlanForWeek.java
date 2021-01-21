package com.example.myapplication.ui.planForWeek;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Resource;
import com.anychart.enums.AvailabilityPeriod;
import com.anychart.enums.TimeTrackingMode;
import com.anychart.scales.calendar.Availability;
import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.MyDate;
import com.example.myapplication.R;
import com.example.myapplication.ToDo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        resource.resourceListWidth(120);

        resource.calendar().availabilities(new Availability[] {
                new Availability(AvailabilityPeriod.DAY, (Double) null, 10d, true, (Double) null, (Double) null, 18d),
                new Availability(AvailabilityPeriod.DAY, (Double) null, 14d, false, (Double) null, (Double) null, 15d),
                new Availability(AvailabilityPeriod.WEEK, (Double) null, (Double) null, false, 5d, (Double) null, 18d),
                new Availability(AvailabilityPeriod.WEEK, (Double) null, (Double) null, false, 6d, (Double) null, 18d)
        });
        DatabaseHelper db = new DatabaseHelper(getContext());
        List<DataEntry> data = new ArrayList<>();


        List<ToDo> toDos = db.getToDoesForWeek(new MyDate());
        Map<Integer, List<ToDo>> todoHourMap = new HashMap<Integer, List<ToDo>>();
        for(ToDo todo : toDos){
            Integer hour = Integer.parseInt(todo.getHH());
            if(todoHourMap.containsKey(hour)){
                for(int i = 0; i < todoHourMap.get(hour).size(); i++){
                    if(Integer.parseInt(todoHourMap.get(hour).get(i).getMM()) > Integer.parseInt(todo.getMM())){
                        todoHourMap.get(hour).add(i, todo);
                    }
                }
                if(!todoHourMap.get(hour).contains(todo)){
                    todoHourMap.get(hour).add(todo);
                }
            } else{
                todoHourMap.put(hour, new ArrayList<ToDo>());
                todoHourMap.get(hour).add(todo);
            }
        }
        for(int i = 0; i < 24; i++){
            Activity[] activities;
            if(todoHourMap.containsKey(i)){
                activities = new Activity[todoHourMap.get(i).size()];
            } else {
                activities = new Activity[]{};
            }

            int j = 0;
            if(todoHourMap.containsKey(i)){
                for (ToDo todo : todoHourMap.get(i)){
                    activities[j] = new Activity(todo.getName() + " " + todo.getTime(),
                            new Interval[]{new Interval(todo.getDate(),
                                    todo.getDate(),
                                    Integer.parseInt(todo.getDurationInMinutes()))},
                            db.getColorForCategory(todo.getID()));
                    j++;
                }
            }
            db.close();
            data.add(new ResourceDataEntry(
                    i+":00",
                    activities));
        }

        //TODO wykorzystać interwały do zdarzeń cyklicznych


//                new Activity[]{
//                        new Activity(
//                                "name",
////                                new Interval[]{
////                                        new Interval(new MyDate().getDate(), new MyDate().getDate(), 60),
////                                        new Interval(new MyDate().getDate(), new MyDate().getDate(), 60)
////                                                }TODO,
//                                "#62BEC1")
//
//                                }));

        resource.data(data);

        chart.setChart(resource);

        return root;
    }
}
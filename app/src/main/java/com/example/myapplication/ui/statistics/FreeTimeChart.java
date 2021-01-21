package com.example.myapplication.ui.statistics;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian3d;
import com.anychart.core.cartesian.series.Column3d;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.HoverMode;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.graphics.vector.SolidFill;
import com.example.myapplication.Category;
import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.MyDate;
import com.example.myapplication.R;
import com.example.myapplication.ToDo;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.List;


public class FreeTimeChart extends Fragment {
private AnyChartView freeTimeChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_free_time_chart, container, false);
        freeTimeChart = root.findViewById(R.id.freeTimeChart);
        prepareFreeTimeChart();
        return root;
    }

    private void prepareFreeTimeChart(){
        Cartesian3d column3d = AnyChart.column3d();
        column3d.yScale().stackMode(ScaleStackMode.VALUE);
        column3d.animation(true);
        column3d.title("Time occupation");
        column3d.title().padding(0d, 0d, 15d, 0d);
        List<Category> categories = new DatabaseHelper(getContext()).getCategories();
        List<DataEntry> seriesData = new ArrayList<>();
        switch(categories.size()){
            case 3:
                seriesData.add(new CustomDataEntry("Mo", 8,
                                                    new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(0).getName(), new MyDate().getLastWeekDayDate("mo")),
                                                    new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(1).getName(), new MyDate().getLastWeekDayDate("mo")),
                                                    new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(2).getName(), new MyDate().getLastWeekDayDate("mo")),
                                                   null,
                                                   null));
                seriesData.add(new CustomDataEntry("Tu", 8,
                        new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(0).getName(), new MyDate().getLastWeekDayDate("tu")),
                        new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(1).getName(), new MyDate().getLastWeekDayDate("tu")),
                        new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(2).getName(), new MyDate().getLastWeekDayDate("tu")),
                        null,
                        null));
                seriesData.add(new CustomDataEntry("We", 8,
                        new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(0).getName(), new MyDate().getLastWeekDayDate("we")),
                        new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(1).getName(), new MyDate().getLastWeekDayDate("we")),
                        new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(2).getName(), new MyDate().getLastWeekDayDate("we")),
                        null,
                        null));
                seriesData.add(new CustomDataEntry("Th", 8,
                        new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(0).getName(), new MyDate().getLastWeekDayDate("th")),
                        new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(1).getName(), new MyDate().getLastWeekDayDate("th")),
                        new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(2).getName(), new MyDate().getLastWeekDayDate("th")),
                        null,
                        null));
                seriesData.add(new CustomDataEntry("Fr", 8,
                        new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(0).getName(), new MyDate().getLastWeekDayDate("fr")),
                        new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(1).getName(), new MyDate().getLastWeekDayDate("fr")),
                        new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(2).getName(), new MyDate().getLastWeekDayDate("fr")),
                        null,
                        null));
                seriesData.add(new CustomDataEntry("Sa", 8,
                        new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(0).getName(), new MyDate().getLastWeekDayDate("sa")),
                        new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(1).getName(), new MyDate().getLastWeekDayDate("sa")),
                        new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(2).getName(), new MyDate().getLastWeekDayDate("sa")),
                        null,
                        null));
                seriesData.add(new CustomDataEntry("Su", 8,
                        new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(0).getName(), new MyDate().getLastWeekDayDate("su")),
                        new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(1).getName(), new MyDate().getLastWeekDayDate("su")),
                        new DatabaseHelper(getContext()).getCategoryDayleDuration(categories.get(2).getName(), new MyDate().getLastWeekDayDate("su")),
                        null,
                        null));
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            default:
                break;

        }

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Data = set.mapAs("{ x: 'x', value: 'freeTime' }");
        //

        int i = 2;
        for(Category category : new DatabaseHelper(getContext()).getCategories()){
            Column3d serie = column3d.column(set.mapAs("{x: 'x', value: 'value" + i + "' }"));
            serie.name(category.getName());
            serie.fill(new SolidFill(category.getColor(), 1d));
            serie.stroke("1 #f7f3f3");
            serie.hovered().stroke("3 #f7f3f3");
            i++;
        }


        Column3d series = column3d.column(series1Data);
        series.name("sleep");
        series.fill(new SolidFill("#FFFFFF", 1d));
        series.stroke("1 #f7f3f3");
        series.hovered().stroke("3 #f7f3f3");

        Column3d series2 = column3d.column(series2Data);
        series2.name("free time");
        series2.fill(new SolidFill("#00ff00", 1d));
        series2.stroke("1 #f7f3f3");
        series2.hovered().stroke("3 #f7f3f3");

        column3d.legend().enabled(true);
        column3d.legend().fontSize(13d);
        column3d.legend().padding(0d, 0d, 20d, 0d);
        String[] ticks = new String[24];
        for(i = 0; i < 24; i++){
            ticks[i] = Integer.toString(i);
        }
        column3d.yScale().ticks(ticks);
        column3d.xAxis(0).stroke("1 #a18b7e");
        column3d.xAxis(0).labels().fontSize("#a18b7e");
        column3d.yAxis(0).stroke("1 #a18b7e");
        column3d.yAxis(0).labels().fontColor("#a18b7e");
        column3d.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        column3d.yAxis(0).title().enabled(true);
        column3d.yAxis(0).title().text("Duration (h)");
        column3d.yAxis(0).title().fontColor("#a18b7e");

        column3d.interactivity().hoverMode(HoverMode.BY_X);

        column3d.tooltip()
                .displayMode(TooltipDisplayMode.UNION)
                .format("{%Value} {%SeriesName}");

        column3d.yGrid(0).stroke("#a18b7e", 1d, null, null, null);
        column3d.xGrid(0).stroke("#a18b7e", 1d, null, null, null);

        freeTimeChart.setChart(column3d);
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String dayOfWeek, Number sleep, Number work, Number relax, Number sport, Number value5, Number value6) {
            super(dayOfWeek, sleep);
            setValue("value2", work);
            setValue("value3", relax);
            setValue("value4", sport);
            setValue("value5", value5);
            setValue("value6", value6);
            setValue("freeTime", getFreeTime(dayOfWeek));
        }
    }

    private Number getFreeTime(String dayName) {
        List<ToDo> todoes = new DatabaseHelper(getContext()).getToDoes(new MyDate().getLastWeekDayDate(dayName.toLowerCase()));
        float tusksDuration = 0;
        for(ToDo todo : todoes){
            tusksDuration += Float.parseFloat(todo.getDurationInMinutes())/60;
        }
        if ((24 - tusksDuration - 8) < 1){
            return 0;
        } else{
            return 24 - tusksDuration - 8;
        }
    }
}
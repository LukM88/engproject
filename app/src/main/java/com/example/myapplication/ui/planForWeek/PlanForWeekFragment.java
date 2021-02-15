package com.example.myapplication.ui.planForWeek;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.MyDate;
import com.example.myapplication.R;
import com.example.myapplication.ToDo;

public class PlanForWeekFragment extends Fragment {
    private final String[] week = new String[]{"mo", "tu", "we", "th", "fr", "sa", "su"};
    private ItemAdapter mAdapter;
    private ListView plan;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View root = inflater.inflate(R.layout.fragment_plan_for_week, container, false);
        mAdapter = new ItemAdapter(getContext());
        for (int i = 0; i < week.length; i++) {
            mAdapter.addSectionHeaderItem(week[i] + ' ' + new MyDate().getWeekDates(week[i]).getDate());
            for (ToDo todo : new DatabaseHelper(getContext()).orderTodoes(new DatabaseHelper(getContext()).getToDoes(new MyDate().getWeekDates(week[i])))){
                mAdapter.addItem(todo.getID());
            }
        }
        plan = root.findViewById(R.id.planForWeek);
        plan.setAdapter(mAdapter);
        return root;
    }
}
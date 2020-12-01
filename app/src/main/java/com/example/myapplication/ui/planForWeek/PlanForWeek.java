package com.example.myapplication.ui.planForWeek;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.example.myapplication.R;

public class PlanForWeek extends Fragment {

    TableLayout week;

    public View onCreate(@NonNull LayoutInflater inflater,
                         ViewGroup container, final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View root = inflater.inflate(R.layout.fragment_plan_for_week, container, false);
        week = root.findViewById(R.id.table);

        return root;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plan_for_week, container, false);
    }
}
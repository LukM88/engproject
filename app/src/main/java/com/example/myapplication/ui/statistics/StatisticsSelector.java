package com.example.myapplication.ui.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.myapplication.R;

public class StatisticsSelector extends Fragment {

    private ImageButton goToDoneTasksButt;
    private ImageButton goToDoneFreeTimeGraphButt;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_statistics_selector, container, false);
        goToDoneFreeTimeGraphButt = root.findViewById(R.id.imageButton3);
        goToDoneTasksButt = root.findViewById(R.id.goToDoneTaskChar);

        goToDoneTasksButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.statisticsFragment);
            }
        });
        goToDoneFreeTimeGraphButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.freeTimeChart);
            }
        });
        return root;
    }
}
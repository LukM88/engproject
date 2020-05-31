package com.example.myapplication.ui.calender;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MainActivity;
import com.example.myapplication.MyDate;
import com.example.myapplication.R;
import com.example.myapplication.ToDo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class PlanForDay extends Fragment {
    private ListView lista;
    private TextView title;
    protected TextView description;
    protected TextView time;
    private FloatingActionButton addButt;
    private Button backButt;
    private MyDate date = new MyDate();
    private ToDo details;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.date.setDay(this.getArguments().getString("day"));
        this.date.setMonth(this.getArguments().getString("month"));
        this.date.setYear(this.getArguments().getString("year"));
        final View root = inflater.inflate(R.layout.fragment_plan_for_day, container, false);


        //date.setDay());
        //d/ate.setMonth();
        //date.setYear();
        title = root.findViewById(R.id.dateText);
        title.setText(date.getDate());
        lista = root.findViewById(R.id.listForDay);
        AdapterForPlan adapter = new AdapterForPlan(getContext(), date);

        lista.setAdapter(adapter);


        description = root.findViewById(R.id.textView2);



        addButt = root.findViewById(R.id.fab2);
        addButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String selectedDate = date.getDate();
                Bundle bundle = new Bundle();
                bundle.putString("day",date.getDay());
                bundle.putString("month",date.getMonth());
                bundle.putString("year",date.getYear());
                Navigation.findNavController(root).navigate(R.id.addPlanForDay,bundle);
            }
        });
        return root;
    }

    }

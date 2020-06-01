package com.example.myapplication.ui.calender;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavAction;
import androidx.navigation.Navigation;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.MyDate;
import com.example.myapplication.R;

public class CalenderFragment extends Fragment {

    private CalendarView calendar;
    private Button todoBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
         final View root = inflater.inflate(R.layout.fragment_calender, container, false);

        DatabaseHelper db = new DatabaseHelper(getContext());
        //db.showEvents();
        calendar = root.findViewById(R.id.calendar);
        todoBtn = root.findViewById(R.id.todoBtn);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                MyDate date = new MyDate();
                date.setDay(String.valueOf(dayOfMonth));
                date.setMonth( String.valueOf(month + 1));
                date.setYear(String.valueOf(year));

                String selectedDate = date.getDate();
                Bundle bundle = new Bundle();
                bundle.putString("day",date.getDay());
                bundle.putString("month",date.getMonth());
                bundle.putString("year",date.getYear());
                Navigation.findNavController(root).navigate(R.id.action_nav_calender_to_planForDay,bundle);

                Toast.makeText(getContext(), selectedDate, Toast.LENGTH_LONG).show();

            }
        });
        todoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(root).navigate(R.id.action_nav_calender_to_todo);

            }
        });



        return root;
    }
}

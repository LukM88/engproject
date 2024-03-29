package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.myapplication.MyDate;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class HomeFragment extends Fragment {

    private ListView listView;
    private FloatingActionButton floatingButt;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        listView = root.findViewById(R.id.listView);
        CustomAdapter customAdapter = new CustomAdapter(getContext());
        listView.setAdapter(customAdapter);

        floatingButt = root.findViewById(R.id.fab);
        floatingButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("day", new MyDate().getDay());
                bundle.putString("month", new MyDate().getMonth());
                bundle.putString("year", new MyDate().getYear());
                Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_slideshow, bundle);
            }
        });




        return root;

    }



}

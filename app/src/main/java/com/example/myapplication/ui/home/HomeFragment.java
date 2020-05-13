package com.example.myapplication.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.example.myapplication.R;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    String value;
    String[] superStarNames = {"Simple", "Data", "For", "TODO", "List","","","","","","","","","",""};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        ListView listView = root.findViewById(R.id.listView);
        ListView listView1 = root.findViewById(R.id.listView2);
        CustomAdapter customAdapter = new CustomAdapter(getContext(), superStarNames, listView1);
        listView.setAdapter(customAdapter);





        return root;

    }



}

package com.example.myapplication.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;


import androidx.navigation.Navigation;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.ToDo;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    ArrayList<ToDo> names=new ArrayList<ToDo>();
    Context context;
    LayoutInflater inflter;
    String value;
    ListView gridView;
    DatabaseHelper dbHelper;

    public CustomAdapter(Context context) {
        this.context = context;
        inflter = (LayoutInflater.from(context));
        dbHelper = new DatabaseHelper(context);
        names=dbHelper.getToDoes();
        dbHelper.close();
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        view = inflter.inflate(R.layout.list_items, null);
        final CheckedTextView simpleCheckedTextView = view.findViewById(R.id.simpleCheckedTextView);
        names = orderTodoes(names);
        simpleCheckedTextView.setText(names.get(position).getName());

        if (names.get(position).getState()) {
            simpleCheckedTextView.setChecked(true);
            simpleCheckedTextView.setCheckMarkDrawable(R.drawable.check);
        } else {
            simpleCheckedTextView.setChecked(false);
        }
        if(names.get(position).getPriority().equals("medium")){
            simpleCheckedTextView.setBackgroundColor(Color.YELLOW);
        }
        if(names.get(position).getPriority().equals("high")){
            simpleCheckedTextView.setBackgroundColor(Color.RED);
        }

        simpleCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper= new DatabaseHelper(context);

                if (names.get(position).getState()) {
                    value = "un-Checked";
                    dbHelper.updateTodoStatus(names.get(position));
                    names.get(position).setState(false);
                    simpleCheckedTextView.setCheckMarkDrawable(null);
                    simpleCheckedTextView.setChecked(false);
                } else {
                    dbHelper.updateTodoStatus(names.get(position));

                    value = "Checked";
                    simpleCheckedTextView.setCheckMarkDrawable(R.drawable.check);
                    simpleCheckedTextView.setChecked(true);
                    names.get(position).setState(true);
                }
                dbHelper.close();

                Toast.makeText(context, value, Toast.LENGTH_SHORT).show();
            }
        });
        final View finalView = view;
        simpleCheckedTextView.setOnLongClickListener (new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Bundle bundle = new Bundle();
                try{
                    bundle.putInt("id",Integer.parseInt(names.get(position).getID()));
                    Navigation.findNavController(finalView).navigate(R.id.detailsFromHome,bundle);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
                return false;
            }



        });
        return view;
    }

    private ArrayList<ToDo> orderTodoes(ArrayList<ToDo> data){
        ArrayList<ToDo> high = new ArrayList<ToDo>();
        ArrayList<ToDo> medium = new ArrayList<ToDo>();
        ArrayList<ToDo> low = new ArrayList<ToDo>();
        for (ToDo todo : data) {
            boolean added = false;
            switch(todo.getPriority()){
                case "high":
                    if(high.isEmpty()){
                        high.add(todo);
                    } else{
                        for(int j = 0; j < high.size(); j++){
                            if(high.get(j).getHH().equals(todo.getHH())){
                                if(Integer.parseInt(high.get(j).getMM()) > Integer.parseInt(todo.getMM())){
                                    high.add(j, todo);
                                    added = true;
                                    break;
                                }
                            } else{
                                if(Integer.parseInt(high.get(j).getHH()) > Integer.parseInt(todo.getHH())){
                                    high.add(j, todo);
                                    added = true;
                                    break;
                                }
                            }
                        }
                        if(!added){
                            high.add(todo);
                        }
                    }
                    break;
                case "medium":
                    if(medium.isEmpty()){
                        medium.add(todo);
                    } else{
                        for(int j = 0; j < medium.size(); j++){
                            if(medium.get(j).getHH().equals(todo.getHH())){
                                if(Integer.parseInt(medium.get(j).getMM()) > Integer.parseInt(todo.getMM())){
                                    medium.add(j, todo);
                                    added = true;
                                    break;
                                }
                            } else{
                                if(Integer.parseInt(medium.get(j).getHH()) > Integer.parseInt(todo.getHH())){
                                    medium.add(j, todo);
                                    added = true;
                                    break;
                                }
                            }
                        }
                        if(!added){
                            medium.add(todo);
                        }
                    }
                    break;
                default:
                    if(low.isEmpty()){
                        low.add(todo);
                    } else{
                        for(int j = 0; j < low.size(); j++){
                            if(low.get(j).getHH().equals(todo.getHH())){
                                if(Integer.parseInt(low.get(j).getMM()) > Integer.parseInt(todo.getMM())){
                                    low.add(j, todo);
                                    added = true;
                                    break;
                                }
                            } else{
                                if(Integer.parseInt(low.get(j).getHH()) > Integer.parseInt(todo.getHH())){
                                    low.add(j, todo);
                                    added = true;
                                    break;
                                }
                            }
                        }
                        if(!added){
                            low.add(todo);
                        }
                    }
            }
        }
        for (int i = 0; i < data.size(); i++){
            if(i < high.size()){
                data.set(i, high.get(i));
            }
            if(i >= high.size() && i-high.size() < medium.size()){
                data.set(i , medium.get(i - high.size()));
            }
            if(i >= high.size() + medium.size()){
                data.set(i, low.get(i - (high.size() + medium.size())));
            }
        }
        return data;
    }
}
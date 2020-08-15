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
        simpleCheckedTextView.setText(names.get(position).getName());

        //System.out.println(imageView.isShown());
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

// perform on Click Event Listener on CheckedTextView
        simpleCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper= new DatabaseHelper(context);

                if (names.get(position).getState()) {
// set cheek mark drawable and set checked property to false
                    value = "un-Checked";
                    dbHelper.updateTodoStatus(names.get(position));
                    names.get(position).setState(false);
                    simpleCheckedTextView.setCheckMarkDrawable(null);
                    simpleCheckedTextView.setChecked(false);
                } else {
                    dbHelper.updateTodoStatus(names.get(position));
// set cheek mark drawable and set checked property to true

                    value = "Checked";
                    simpleCheckedTextView.setCheckMarkDrawable(R.drawable.check);
                    simpleCheckedTextView.setChecked(true);
                    names.get(position).setState(true);
                }
                //names.set(position,dbHelper.getEvent(Integer.parseInt(names.get(position).getID())));
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
}
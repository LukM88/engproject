package com.example.myapplication.ui.calender;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import androidx.navigation.Navigation;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.ToDo;
import com.example.myapplication.MyDate;

import java.util.ArrayList;

public class AdapterForPlan extends BaseAdapter {
    ArrayList<ToDo> names = new ArrayList<ToDo>();
    Context context;
    LayoutInflater inflter;
    String value;
    ListView gridView;
    DatabaseHelper dbHelper;
    int focuse=0;
    public AdapterForPlan(Context context, MyDate date){
        this.context = context;
        inflter = (LayoutInflater.from(context));
        dbHelper = new DatabaseHelper(context);
        names=dbHelper.getToDoes(date);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.list_items, null);

        final CheckedTextView simpleCheckedTextView = convertView.findViewById(R.id.simpleCheckedTextView);
        simpleCheckedTextView.setText(names.get(position).getName()+" "+names.get(position).getTime());
        this.focuse=position;
        if (names.get(position).getState()) {
            simpleCheckedTextView.setChecked(true);
            simpleCheckedTextView.setCheckMarkDrawable(R.drawable.check);
        } else {
            simpleCheckedTextView.setChecked(false);
        }
        simpleCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                dbHelper= new DatabaseHelper(context);

                if (names.get(position).getState()) {
                    value = "un-Checked";
                    dbHelper.updateTodoStatus(names.get(position).getID(), names.get(position).getState());
                    names.get(position).setState(false);
                    simpleCheckedTextView.setCheckMarkDrawable(null);
                    simpleCheckedTextView.setChecked(false);
                } else {
                    dbHelper.updateTodoStatus(names.get(position).getID(), names.get(position).getState());

                    value = "Checked";
                    simpleCheckedTextView.setCheckMarkDrawable(R.drawable.check);
                    simpleCheckedTextView.setChecked(true);
                    names.get(position).setState(true);
                }
                //names.set(position,dbHelper.getEvent(Integer.parseInt(names.get(position).getID())));
                dbHelper.close();

            }
        });
        if(names.get(position).getPriority().equals("medium")){
            simpleCheckedTextView.setBackgroundColor(Color.YELLOW);
        }
        if(names.get(position).getPriority().equals("high")){
            simpleCheckedTextView.setBackgroundColor(Color.RED);
        }

        final View finalConvertView = convertView;
        simpleCheckedTextView.setOnLongClickListener (new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Bundle bundle = new Bundle();
                try{
                    bundle.putInt("id",Integer.parseInt(names.get(position).getID()));
                    Navigation.findNavController(finalConvertView).navigate(R.id.detailsFromPlan,bundle);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
                return false;
            }



        });

        return convertView;
    }

}

package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.view.ViewParentCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter {
    ArrayList<ToDo> names=new ArrayList<ToDo>();
    Context context;
    LayoutInflater inflter;
    String value;
    ListView gridView;
    DatabaseHelper dbHelper;


    public SearchAdapter(Context context,String name) {
        this.context = context;
        inflter = (LayoutInflater.from(context));
        dbHelper = new DatabaseHelper(context);
        names=dbHelper.getEventsWithName(name);
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
        simpleCheckedTextView.setText(names.get(position).getName()+" "+names.get(position).getDate());
        simpleCheckedTextView.setTextColor(Color.WHITE);
        //System.out.println(imageView.isShown());
        if (names.get(position).getState()) {
            simpleCheckedTextView.setChecked(true);
            simpleCheckedTextView.setCheckMarkDrawable(R.drawable.check);
        } else {
            simpleCheckedTextView.setChecked(false);
        }
        if(names.get(position).getPriority().equals("medium")){
            simpleCheckedTextView.setBackgroundColor(Color.YELLOW);
            simpleCheckedTextView.setTextColor(Color.WHITE);
        }
        if(names.get(position).getPriority().equals("high")){
            simpleCheckedTextView.setBackgroundColor(Color.RED);
        }

// perform on Click Event Listener on CheckedTextView

        simpleCheckedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO przejście w formie hiperłącza
            }
        });
        simpleCheckedTextView.setOnLongClickListener (new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context,names.get(position).getDescription()+" \ntime:"+names.get(position).getTime()  ,Toast.LENGTH_LONG).show();
                return false;
            }



        });
        return view;
    }
}
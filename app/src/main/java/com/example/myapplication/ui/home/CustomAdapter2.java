package com.example.myapplication.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.R;

public class CustomAdapter2 extends BaseAdapter {
    private Context context;
    private String names;
    private LayoutInflater inflter;
    public CustomAdapter2(Context context, String names) {
        this.context = context;
        this.names = names;
        inflter = (LayoutInflater.from(context));
    }
    @Override
    public int getCount() {
        return 0;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.list_items, null);







        return convertView;
    }
}

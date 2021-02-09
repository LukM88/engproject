package com.example.myapplication.ui.planForWeek;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.MyDate;
import com.example.myapplication.R;
import com.example.myapplication.ToDo;

import java.util.ArrayList;
import java.util.TreeSet;

public class ItemAdapter extends BaseAdapter{
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private final ArrayList<String> mData = new ArrayList<String>();
    private final TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private final LayoutInflater mInflater;

    public ItemAdapter(Context context) {
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.list_content, null);
                    holder.nameText = (TextView) convertView.findViewById(R.id.itemName);
                    holder.hourText = (TextView) convertView.findViewById(R.id.itemHour);
                    holder.durationText = (TextView) convertView.findViewById(R.id.durationText);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.list_header, null);
                    holder.nameText = (TextView) convertView.findViewById(R.id.sectionHeader);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(rowType == TYPE_ITEM){
            ToDo todo = new DatabaseHelper(mInflater.getContext()).getEvent(Integer.parseInt(mData.get(position)));
            if(todo.getPriority().equals("medium")){
                holder.nameText.setBackgroundColor(Color.YELLOW);
            }
            if(todo.getPriority().equals("high")){
                holder.nameText.setBackgroundColor(Color.RED);
                holder.nameText.setTextColor(Color.WHITE);
            }
            holder.nameText.setText(todo.getName());
            holder.hourText.setText(todo.getTime());
            holder.durationText.setText(Float.parseFloat(todo.getDurationInMinutes())/60 + "h");
        } else {
            if(new MyDate().getDate().equals(new MyDate().getWeekDates(mData.get(position).substring(0, 2)).getDate())){
                holder.nameText.setBackgroundColor(Color.MAGENTA);
            } else{
                holder.nameText.setBackgroundColor(Color.CYAN);
            }
            holder.nameText.setText(mData.get(position));
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView nameText;
        public TextView hourText;
        public TextView durationText;
    }

}

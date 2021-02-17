package com.example.myapplication.ui.details;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.ToDo;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;


public class DetailsFragment extends Fragment {

    ToDo event ;
    TextView name;
    TextView description;
    TextView date;
    TextView time;
    TextView priority;
    TextView notification;
    TextView durationOutput;
    TextView repeat;
    TextView category;
    ImageView img;
    Button deleteButt;
    Button updateButt;

    public static Bitmap scaleImage(Bitmap orginalBitmap){
        Bitmap bitmap;
        try {
            bitmap = Bitmap.createScaledBitmap(orginalBitmap, 4000, 4000, false);

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            if (orginalBitmap.getHeight() < 4000 || orginalBitmap.getWidth() < 4000) {
                if (orginalBitmap.getHeight() < 4000 && orginalBitmap.getWidth() < 4000) {
                    bitmap = Bitmap.createBitmap(orginalBitmap, 0, 0, orginalBitmap.getWidth(), orginalBitmap.getHeight(), null, true);
                } else if (orginalBitmap.getWidth() < 4000) {
                    bitmap = Bitmap.createBitmap(orginalBitmap, 0, 0, orginalBitmap.getWidth(), 4000, null, true);
                } else {
                    bitmap = Bitmap.createBitmap(orginalBitmap, 0, 0, 4000, orginalBitmap.getHeight(), null, true);
                }
            } else {
                bitmap = Bitmap.createBitmap(orginalBitmap, 0, 0, 4000, 4000, null, true);

            }
        }
        return bitmap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_details, container, false);
        DatabaseHelper db = new DatabaseHelper(getContext());
        event = db.getEvent(getArguments().getInt("id"));
        db.close();
        name = root.findViewById(R.id.nameText);
        description = root.findViewById(R.id.textView3);
        date = root.findViewById(R.id.dateText);
        time = root.findViewById(R.id.timeText);
        priority = root.findViewById(R.id.priorityText);
        notification = root.findViewById(R.id.notificationText);
        img = root.findViewById(R.id.imageView2);
        durationOutput = root.findViewById(R.id.durationOutput);
        repeat = root.findViewById(R.id.repeatValueDisp);
        category = root.findViewById(R.id.categoryValueDisp);
        deleteButt = root.findViewById(R.id.deleteButt);
        updateButt = root.findViewById(R.id.editButt);

        name.setText(event.getName());
        description.setText(event.getDescription());
        date.setText(event.getDate());
        time.setText(event.getTime());
        durationOutput.setText(event.getDurationInMinutes() + " min");
        priority.setText(event.getPriority());
        notification.setText(event.getNotification());
        description.setText(event.getDescription());
        repeat.setText(event.getRepeat());
        category.setText(new DatabaseHelper(getContext()).getTodoCategory(event.getID()).getName());
        deleteButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int results = new DatabaseHelper(getContext()).deleteEvent(event.getID());
                if(results == 1){
                    Bundle bundle = new Bundle();
                    bundle.putString("day", event.getDay());
                    bundle.putString("month", event.getMonth());
                    bundle.putString("year", event.getYear());
                    Navigation.findNavController(root).navigate(R.id.action_detailsFragment_to_nav_calender, bundle);
                } else {
                    Snackbar.make(getView(), "Something went wrong", BaseTransientBottomBar.LENGTH_SHORT).show();
                }

            }
        });
        updateButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("ID", event.getID());
                bundle.putString("name", event.getName());
                bundle.putString("notification", event.getNotification());
                bundle.putString("day", event.getDay());
                bundle.putString("month", event.getMonth());
                bundle.putString("year", event.getYear());
                bundle.putString("HH", event.getHH());
                bundle.putString("MM", event.getMM());
                bundle.putString("priority", event.getPriority());
                bundle.putString("duration", event.getDurationInMinutes());
                bundle.putString("description", event.getDescription());
                bundle.putString("imgPath", event.getImgPath());
                bundle.putBoolean("state", event.getState());
                bundle.putString("repeat", event.getRepeat());
                bundle.putString("category", new DatabaseHelper(getContext()).getTodoCategory(event.getID()).getName());
                Navigation.findNavController(root).navigate(R.id.action_detailsFragment_to_editToDo, bundle);
            }
        });

        if(!event.getImgPath().isEmpty()) {
            img.setImageBitmap(scaleImage(BitmapFactory.decodeFile(event.getImgPath())));
        }
        return root;
    }
}
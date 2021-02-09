package com.example.myapplication.ui.details;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
            Bitmap bitmap = BitmapFactory.decodeFile(event.getImgPath());
            Bitmap bitmap2;

            try {
                bitmap2 = Bitmap.createScaledBitmap(bitmap, 4000, 4000, false);

            } catch (OutOfMemoryError e) {
                Toast.makeText(getContext(), "Invalid graphic file", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                if (bitmap.getHeight() < 4000 || bitmap.getWidth() < 4000) {
                    if (bitmap.getHeight() < 4000 && bitmap.getWidth() < 4000) {
                        bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), null, true);// itmap(bitmap,imageView.getMaxWidth(),imageView.getMaxHeight(),false);
                    } else if (bitmap.getWidth() < 4000) {
                        bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), 4000, null, true);// itmap(bitmap,imageView.getMaxWidth(),imageView.getMaxHeight(),false);
                    } else {
                        bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, 4000, bitmap.getHeight(), null, true);// itmap(bitmap,imageView.getMaxWidth(),imageView.getMaxHeight(),false);
                    }
                } else {
                    bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, 4000, 4000, null, true);// itmap(bitmap,imageView.getMaxWidth(),imageView.getMaxHeight(),false);

                }
            }
            img.setImageBitmap(bitmap2);
        }
        return root;
    }
}
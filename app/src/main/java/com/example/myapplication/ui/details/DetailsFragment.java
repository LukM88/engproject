package com.example.myapplication.ui.details;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.ToDo;


public class DetailsFragment extends Fragment {

    int id;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_details, container, false);
        id = getArguments().getInt("id");
        DatabaseHelper db = new DatabaseHelper(getContext());
        event = db.getEvent(id);
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
package com.example.myapplication.ui.editEvent;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EditToDo extends Fragment {

    private final int RESULT_LOAD_IMAGE = 1;
    private ImageView imageView;
    private Button editButt;
    private Spinner category;
    private ImageButton dateButt;
    private DatePickerDialog datePicker;
    private TextView dateView;
    private EditText descriptionText;
    private EditText durationText;
    private EditText nameText;
    private Spinner notification;
    private Spinner repeater;
    private final String picturePath = "";
    private Spinner priority;
    private final Map<String, Integer> selectedDate = new HashMap<String, Integer>();
    private ImageButton timeButt;
    private TimePickerDialog timePicker;
    private TextView timeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_edit_to_do, container, false);
        final ImageButton imgButt = root.findViewById(R.id.addImageButton);
        nameText = root.findViewById(R.id.eventNameText);
        nameText.setText(getArguments().getString("name"));
        imageView = root.findViewById(R.id.imageView);
        descriptionText = root.findViewById(R.id.descriptionText);
        durationText = root.findViewById(R.id.durationValue);
        dateButt = root.findViewById(R.id.datePickButt);

        selectedDate.put("year", Integer.parseInt(getArguments().getString("year")));
        selectedDate.put("month", Integer.parseInt(getArguments().getString("month")));
        selectedDate.put("day", Integer.parseInt(getArguments().getString("day")));
        selectedDate.put("hour", Integer.parseInt(getArguments().getString("HH")));
        selectedDate.put("minute", Integer.parseInt(getArguments().getString("MM")));
        dateButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calender = Calendar.getInstance();
                int day = calender.get(Calendar.DAY_OF_MONTH);
                int month = calender.get(Calendar.MONTH);
                int year = calender.get(Calendar.YEAR);
                // date picker dialog
                datePicker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateView.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                selectedDate.put("year", year);
                                selectedDate.put("month", month + 1);
                                selectedDate.put("day", dayOfMonth);
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });
        timeButt = root.findViewById(R.id.timePickButt);
        timeButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calender = Calendar.getInstance();
                int hour = calender.get(Calendar.HOUR);
                int minute = calender.get(Calendar.MINUTE);
                // date picker dialog
                timePicker = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if(selectedDate.isEmpty()){
                                    Snackbar.make(root,"Set date first", BaseTransientBottomBar.LENGTH_SHORT).show();
                                } else{
                                    if(minute < 10){
                                        timeView.setText(hourOfDay + ":0" + minute);
                                    } else{
                                        timeView.setText(hourOfDay + ":" + minute);
                                    }
                                    selectedDate.put("hour", hourOfDay);
                                    selectedDate.put("minute", minute);
                                }

                            }
                        }, hour, minute, true);
                timePicker.show();
            }
        });
        dateView = root.findViewById(R.id.dateText);
        timeView = root.findViewById(R.id.timeText);
        selectedDate.put("day", Integer.parseInt(getArguments().getString("day")));
        selectedDate.put("month", Integer.parseInt(requireArguments().getString("month")));
        selectedDate.put("year", Integer.parseInt(getArguments().getString("year")));
        dateView.setText(new StringBuilder().append(getArguments().getString("day")).append("/").append(getArguments().getString("month")).append("/").append(getArguments().getString("year")).toString());
        timeView.setText(new StringBuilder().append(getArguments().getString("HH")).append(":").append(getArguments().getString("MM")).toString());

        durationText.setText(getArguments().getString("duration"));
        priority = root.findViewById(R.id.prioritySpinner);
        notification = root.findViewById(R.id.notificationSpinner);
        category = root.findViewById(R.id.categorySpinber);
        repeater = root.findViewById(R.id.repeaterSpiner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.notification_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notification.setAdapter(adapter2);
        List<String> categories =new DatabaseHelper(getContext()).getCategoriesNames();
        categories.add(0, "None");
        ArrayAdapter<String> adapter3 = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, categories);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter3);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(getContext(), R.array.repeatability_array, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeater.setAdapter(adapter4);

        imgButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(null);
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        editButt = root.findViewById(R.id.editEventButt);
        editButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                    if(!nameText.getText().toString().isEmpty()
                            && !selectedDate.isEmpty()
                            && selectedDate.containsKey("hour")) {
                        Map<String, String> data = new HashMap<String, String>(){{
                            put("ID", getArguments().getString("ID"));
                            put("name", nameText.getText().toString());
                            put("descriptions", descriptionText.getText().toString());
                            put("HH", selectedDate.get("hour").toString());
                            if (selectedDate.get("minute")<10){
                                put("MM", 0 + selectedDate.get("minute").toString());
                            } else{
                                put("MM", selectedDate.get("minute").toString());
                            }
                            put("duration", durationText.getText().toString());
                            put("priority", priority.getSelectedItem().toString());
                            put("state", String.valueOf(getArguments().getBoolean("state")));

                            if (selectedDate.get("day") < 10){
                                put("day", 0 + selectedDate.get("day").toString());
                            } else{
                                put("day", selectedDate.get("day").toString());
                            }

                            if (selectedDate.get("month")<10){
                                put("month", 0 + selectedDate.get("month").toString());
                            } else{
                                put("month", selectedDate.get("month").toString());
                            }

                            put("year", selectedDate.get("year").toString());
                            put("imgPath", picturePath);
                            put("notification", notification.getSelectedItem().toString());
                            put("repeat", repeater.getSelectedItem().toString());
                            put("category", category.getSelectedItem().toString());
                        }};
                        if(data.get("duration").isEmpty()) data.put("duration", "00");
                        try {
                            dbHelper.updateTodo(data);
                            dbHelper.showEvents();
                        }catch (Exception e){
                            Toast.makeText(getContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }finally {
                            dbHelper.close();
                            Toast.makeText(getContext(),"Event edited",Toast.LENGTH_LONG).show();

                            final NavController navController = Navigation.findNavController(root);
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", Integer.parseInt(getArguments().getString("ID")));
                            navController.navigate(R.id.action_editToDo_to_detailsFragment, bundle);

                        }
                    }else{
                        Toast.makeText(getContext(),"Required data is missing!!",Toast.LENGTH_LONG).show();
                    }

                }catch (NumberFormatException e){
                    Toast.makeText(getContext(),"An error during insertion ",Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }



            }
        });
        if(!getArguments().getString("imgPath").isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(getArguments().getString("imgPath"));
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
            imageView.setImageBitmap(bitmap2);
        }
        return root;
    }
}
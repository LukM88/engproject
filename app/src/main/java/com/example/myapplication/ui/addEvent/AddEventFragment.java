package com.example.myapplication.ui.addEvent;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

//import android.support.v4.app.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.MainActivity;
import com.example.myapplication.MyDate;
import com.example.myapplication.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AddEventFragment extends Fragment {


    private int RESULT_LOAD_IMAGE = 1;
    private ImageView imageView;
    private Button addButton;
    private Button cancelButt;
    private EditText nameText;
    private EditText descriptionText;
    private TextView dateView;
    private TextView timeView;
    private ImageButton dateButt;
    private ImageButton timeButt;
    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;
    private Spinner notification;
    private Spinner priority;
    private String picturePath = "";
    private Map<String, Integer> selectedDate = new HashMap<String, Integer>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_add_event, container, false);
        //final TextView textView = root.findViewById(R.id.text_slideshow);
        final ImageButton imgButt = root.findViewById(R.id.addImageButton);
        nameText = root.findViewById(R.id.eventNameText);
        imageView = root.findViewById(R.id.imageView);
        descriptionText = root.findViewById(R.id.descriptionText);
        dateButt = root.findViewById(R.id.datePickButt);
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
        System.out.println(selectedDate.values());
        if(getArguments().getString("day") != null){
            selectedDate.put("day", Integer.parseInt(getArguments().getString("day")));
            selectedDate.put("month", Integer.parseInt(getArguments().getString("month")));
            selectedDate.put("year", Integer.parseInt(getArguments().getString("year")));
            System.out.println(selectedDate.values());
        }
        if(getArguments().getString("day") != null){
            dateView.setText(selectedDate.get("day") + "/" + selectedDate.get("month") + "/" + selectedDate.get("year"));
        } else{
            dateView.setText("dd/mm/yyyy");
            timeView.setText("hh:mm");
        }

        priority = root.findViewById(R.id.prioritySpinner);
        notification = root.findViewById(R.id.notificationSpinner);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.notification_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notification.setAdapter(adapter2);

        imgButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageBitmap(null);
                //imageView.setImageResource(R.drawable.check);
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        addButton = root.findViewById(R.id.addEventButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println(selectedDate.values());
                        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                        if(!nameText.getText().toString().isEmpty() 
                                && nameText.getText().toString().length()!=1 
                                && !selectedDate.isEmpty()) {
                            Map<String, String> data = new HashMap<String, String>(){{
                                put("name", nameText.getText().toString());
                                put("description", descriptionText.getText().toString());
                                put("HH", selectedDate.get("hour").toString());
                                if (selectedDate.get("minute")<10){
                                    put("MM", 0 + selectedDate.get("minute").toString());
                                } else{
                                    put("MM", selectedDate.get("minute").toString());
                                }
                                put("priority", priority.getSelectedItem().toString());
                                put("state", "");

                                if (selectedDate.get("day")<10){
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
                            }};
                            System.out.println(data);

                            try {
                                dbHelper.addEvent(data);
                                dbHelper.showEvents();
                            }catch (Exception e){
                                Toast.makeText(getContext(),"Invalid data type!",Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }finally {
                                dbHelper.close();
                                Toast.makeText(getContext(),"Event added",Toast.LENGTH_LONG).show();

                                final NavController navController = Navigation.findNavController(root);
                                navController.navigate(R.id.action_nav_toHome);

                            }
                        }else{
                            Toast.makeText(getContext(),"Event have to have name!!",Toast.LENGTH_LONG).show();
                        }

                }catch (NumberFormatException e){
                    Toast.makeText(getContext(),"Wrong time format",Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }



            }
        });
//        calendar=root.findViewById(R.id.calendarViewAdd);
//        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView,int year, int month, int dayOfMonth) {
//                MyDate date = new MyDate();
//                date.setDay(String.valueOf(dayOfMonth));
//                date.setMonth( String.valueOf(month + 1));
//                date.setYear(String.valueOf(year));
//
//                selectedDay[0]=date.getDay();
//                selectedMonth[0]=date.getMonth();
//                selectedYear[0]=date.getYear();
//                Snackbar.make(root,"Zmieniono datę na "+date.getDate(), BaseTransientBottomBar.LENGTH_SHORT).show();
//            }
//        });
        cancelButt = root.findViewById(R.id.cancelButton);
        cancelButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_nav_toHome);
            }
        });




        return root;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            this.picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            Bitmap bitmap2;

            try {
                bitmap2 = Bitmap.createScaledBitmap(bitmap, 4000, 4000, false);

            }catch (OutOfMemoryError  e){
                Toast.makeText(getContext(),"Invalid graphic try other",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                if(bitmap.getHeight()<4000||bitmap.getWidth()<4000){
                    if(bitmap.getHeight()<4000 && bitmap.getWidth()<4000){
                        bitmap2 = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),null,true);// itmap(bitmap,imageView.getMaxWidth(),imageView.getMaxHeight(),false);
                    }else if(bitmap.getWidth()<4000){
                        bitmap2 = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),4000,null,true);// itmap(bitmap,imageView.getMaxWidth(),imageView.getMaxHeight(),false);
                    }
                    else{
                        bitmap2 = Bitmap.createBitmap(bitmap,0,0,4000,bitmap.getHeight(),null,true);// itmap(bitmap,imageView.getMaxWidth(),imageView.getMaxHeight(),false);
                    }
                }
                else{
                    bitmap2 = Bitmap.createBitmap(bitmap,0,0,4000,4000,null,true);// itmap(bitmap,imageView.getMaxWidth(),imageView.getMaxHeight(),false);

                }
            }
            imageView.setImageBitmap(bitmap2);

            /*if(bitmap.getHeight()<4000||bitmap.getWidth()<4000){
                if(bitmap.getHeight()<4000 && bitmap.getWidth()<4000){
                    bitmap2 = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),null,true);// itmap(bitmap,imageView.getMaxWidth(),imageView.getMaxHeight(),false);
                }else if(bitmap.getWidth()<4000){
                    bitmap2 = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),4000,null,true);// itmap(bitmap,imageView.getMaxWidth(),imageView.getMaxHeight(),false);
                }
                else{
                    bitmap2 = Bitmap.createBitmap(bitmap,0,0,4000,bitmap.getHeight(),null,true);// itmap(bitmap,imageView.getMaxWidth(),imageView.getMaxHeight(),false);
                }
            }
            else{
                bitmap2 = Bitmap.createBitmap(bitmap,0,0,4000,4000,null,true);// itmap(bitmap,imageView.getMaxWidth(),imageView.getMaxHeight(),false);

            }*/
        }
    }



}

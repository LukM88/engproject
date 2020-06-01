package com.example.myapplication.ui.addEvent;

import android.Manifest;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

//import android.support.v4.app.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.MainActivity;
import com.example.myapplication.MyDate;
import com.example.myapplication.R;
import com.example.myapplication.ui.calender.CalenderFragment;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class AddEventFragment extends Fragment {


    private int RESULT_LOAD_IMAGE = 1;
    private ImageView imageView;
    private Button addButton;
    private Button cancleButt;
    private EditText nameText;
    private EditText descriptionText;

    private CalendarView calendar;
    private EditText HH;
    private EditText MM;
    private Spinner notification;
    private Spinner priority;
    private String picturePath = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_add_event, container, false);
        //final TextView textView = root.findViewById(R.id.text_slideshow);
        final Button imgButt = root.findViewById(R.id.addImageButton);
        nameText = root.findViewById(R.id.eventNameText);
        imageView = root.findViewById(R.id.imageView);
        descriptionText = root.findViewById(R.id.descriptionText);
        HH = root.findViewById(R.id.hourText);
        MM = root.findViewById(R.id.minutesText);
        final String[] selectedDay = new String[1];
        final String[] selectedMonth= new String[1];
        final String[] selectedYear= new String[1];
        if(getArguments().getString("day")==null||getArguments().getString("month")==null||getArguments().getString("year")==null){
            MyDate date = new MyDate();
            date.getDate();
             selectedDay[0] = date.getDay();
             selectedMonth[0] = date.getMonth();
             selectedYear[0] = date.getYear();
             Toast.makeText(getContext(),"Wybrana data to "+date.getDate(),Toast.LENGTH_LONG).show();
        }else{
            selectedDay[0] = getArguments().getString("day");
            selectedMonth[0] = getArguments().getString("month");
            selectedYear[0] = getArguments().getString("year");
            MyDate date = new MyDate();
            date.setYear(selectedYear[0]);
            date.setMonth(selectedMonth[0]);
            date.setDay(selectedDay[0]);
            Toast.makeText(getContext(),"Wybrana data to "+date.getDate(),Toast.LENGTH_LONG).show();
        }
        priority = root.findViewById(R.id.prioritySpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority.setAdapter(adapter);
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
                if(HH.getText().equals(null)){
                    HH.setText("12");
                }
                if(MM.getText().equals(null)){
                    MM.setText("00");
                }

                DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                if(!nameText.getText().toString().isEmpty()) {

                        try {
                            dbHelper.addEvent(nameText.getText().toString(), descriptionText.getText().toString(), HH.getText().toString(), MM.getText().toString(), priority.getSelectedItem().toString(), false, selectedDay[0], selectedMonth[0], selectedYear[0], MainActivity.login,picturePath);
                            dbHelper.showEvents();
                        }catch (Exception e){
                            Toast.makeText(getContext(),"Invalid data type!!",Toast.LENGTH_LONG).show();
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

            }
        });
        calendar=root.findViewById(R.id.calendarViewAdd);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView,int year, int month, int dayOfMonth) {
                MyDate date = new MyDate();
                date.setDay(String.valueOf(dayOfMonth));
                date.setMonth( String.valueOf(month + 1));
                date.setYear(String.valueOf(year));

                selectedDay[0]=date.getDay();
                selectedMonth[0]=date.getMonth();
                selectedYear[0]=date.getYear();
                Snackbar.make(root,"Zmieniono datę na "+date.getDate(), BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
        cancleButt = root.findViewById(R.id.cancelButton);
        cancleButt.setOnClickListener(new View.OnClickListener() {
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
                Toast.makeText(getContext(),"Nie właściwy plik graficzny",Toast.LENGTH_SHORT).show();
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

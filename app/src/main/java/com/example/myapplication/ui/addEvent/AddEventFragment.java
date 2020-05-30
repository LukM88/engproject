package com.example.myapplication.ui.addEvent;

import android.Manifest;
import android.app.Activity;
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
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.os.Bundle;
import android.support.v4.app.*;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.anychart.graphics.vector.Image;
import com.example.myapplication.DatabaseHelper;
import com.example.myapplication.MainActivity;
import com.example.myapplication.MyDate;
import com.example.myapplication.R;
import com.example.myapplication.ui.calender.CalenderFragment;

import com.google.android.material.navigation.NavigationView;

import java.security.Permissions;

public class AddEventFragment extends Fragment {


    private int RESULT_LOAD_IMAGE = 1;
    private ImageView imageView;
    private Button addButton;
    private EditText nameText;
    private EditText descriptionText;
    private Spinner day;
    private Spinner month;
    private Spinner year;
    private EditText HH;
    private EditText MM;
    private Spinner notification;
    private Spinner priority;
    private String picturePath = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_event, container, false);
        //final TextView textView = root.findViewById(R.id.text_slideshow);
        final Button imgButt = root.findViewById(R.id.addImageButton);
        nameText = root.findViewById(R.id.eventNameText);
        imageView = root.findViewById(R.id.imageView);
        descriptionText = root.findViewById(R.id.descriptionText);
        HH = root.findViewById(R.id.hourText);
        MM = root.findViewById(R.id.minutesText);
        MyDate date = new MyDate();
        final String selectedDay = date.getDay();
        final String selectedMonth = date.getMonth();
        final String selectedYear = date.getYear();

        imgButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                imageView.setImageBitmap(null);
                //imageView.setImageResource(R.drawable.check);
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
                //TODO napisanie dodawania eventu do bazy przemyśleć wywalenie spinerów na dizeń
                DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                if(!nameText.getText().toString().isEmpty()) {
                    if(!selectedYear.isEmpty()|| !selectedMonth.isEmpty() || !selectedDay.isEmpty()){
                        try {
                            dbHelper.addEvent(nameText.getText().toString(), descriptionText.getText().toString(), HH.getText().toString(), MM.getText().toString(), "low", false, selectedDay, selectedMonth, selectedYear, MainActivity.login,picturePath);
                            dbHelper.showEvents();
                        }catch (Exception e){
                            Toast.makeText(getContext(),"Invalid data type!!",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }finally {
                            dbHelper.close();
                        }
                    }else {
                        Toast.makeText(getContext(),"Event have to have date",Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getContext(),"Event have to have name!!",Toast.LENGTH_LONG).show();
                }
                Toast.makeText(getContext(),"Event added",Toast.LENGTH_LONG).show();

                FragmentManager fragmentManager = new FragmentManager() {
                    @NonNull
                    @Override
                    public FragmentTransaction beginTransaction() {
                        return super.beginTransaction();
                    }
                };
                CalenderFragment first = new CalenderFragment();
                Bundle args = new Bundle();
                args.putInt("zero",0);
                first.setArguments(args);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.mobile_navigation,first);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });




        return root;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

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

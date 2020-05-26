package com.example.myapplication.ui.addEvent;

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
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.anychart.graphics.vector.Image;
import com.example.myapplication.R;

public class AddEventFragment extends Fragment {

    private AddEventViewModel addEventViewModel;
    private static int RESULT_LOAD_IMAGE = 1;
    private ImageView imageView;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            


            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));


        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addEventViewModel =
                ViewModelProviders.of(this).get(AddEventViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_event, container, false);
        //final TextView textView = root.findViewById(R.id.text_slideshow);
        final Button imgButt = root.findViewById(R.id.addImageButton);
        imageView = root.findViewById(R.id.imageView);
        imgButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO

                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        addEventViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
         //       textView.setText(s);
            }
        });


        return root;
    }




}

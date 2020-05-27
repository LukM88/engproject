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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.anychart.graphics.vector.Image;
import com.example.myapplication.R;

import java.security.Permissions;

public class AddEventFragment extends Fragment {


    private int RESULT_LOAD_IMAGE = 1;
    private ImageView imageView;
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
            String picturePath = cursor.getString(columnIndex);
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_add_event, container, false);
        //final TextView textView = root.findViewById(R.id.text_slideshow);
        final Button imgButt = root.findViewById(R.id.addImageButton);
        imageView = root.findViewById(R.id.imageView);
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




        return root;
    }




}

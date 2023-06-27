package com.example.test2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity11 extends AppCompatActivity {
    ImageView avatar;
    EditText fname,lname,email,mobile,age;
    Button view;

    public static final int REQUEST_STORAGE=101;
    String storagePermission[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main11);

        //create methods
        findId();
        storagePermission=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        pickImage();
        displayData();
    }

    private void displayData() {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity11.this,MainActivity12.class);
                intent.putExtra("avatar",imageViewToByte());
                intent.putExtra("fname",fname.getText().toString());
                intent.putExtra("lname",lname.getText().toString());
                intent.putExtra("email",email.getText().toString());
                intent.putExtra("mobile",mobile.getText().toString());
                intent.putExtra("age",age.getText().toString());
                startActivity(intent);
            }
        });
    }

    private byte[] imageViewToByte() {
        Bitmap bitmap=((BitmapDrawable)avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,byteArrayOutputStream);
        byte[]bytes=byteArrayOutputStream.toByteArray();
        return bytes;
    }

    private void pickImage() {
        avatar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int avatar=0;
                if (avatar==0){
                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        pickFromGallery();
                    }
                }
            }
        });
    }

    private void pickFromGallery() {
        Intent intent=new Intent();
        intent.setType("image/jpg/png");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Pick Image"),101);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(storagePermission,REQUEST_STORAGE);
    }

    private boolean checkStoragePermission() {
        boolean result= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void findId() {
        avatar=(ImageView)findViewById(R.id.avatar);
        fname=(EditText)findViewById(R.id.fname);
        lname=(EditText)findViewById(R.id.lname);
        email=(EditText)findViewById(R.id.email);
        mobile=(EditText)findViewById(R.id.mobile);
        age=(EditText)findViewById(R.id.age);
        view=(Button)findViewById(R.id.view);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101&&resultCode==RESULT_OK){
            Uri uri=data.getData();
            Bitmap bitmap=null;
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            }catch (IOException e){
                e.printStackTrace();
            }
            avatar.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_STORAGE:{
                if (grantResults.length>0){
                    boolean result=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if (result){
                        pickFromGallery();
                    }else{
                        Toast.makeText(this, "Please enable storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }
}
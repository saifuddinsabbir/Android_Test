package com.example.test2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.BiFunction;


public class MainActivity12 extends AppCompatActivity {

    ImageView avatarview;
    TextView fnameview,lnameview,emailview,mobileview,ageview;
    LinearLayout linearLayout;
    ImageButton save;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main12);

        findId();
        viewData();
        savePdf();
    }

    private void savePdf() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("size",linearLayout.getWidth()+" "+linearLayout.getHeight());
                bitmap=loadBitmapFromView(linearLayout,linearLayout.getWidth(),linearLayout.getHeight());
                createPdf();
            }
        });
    }

    private void createPdf() {
        DisplayMetrics displayMetrics=new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float height=displayMetrics.heightPixels;
        float width=displayMetrics.widthPixels;

        int convertHeight=(int)height,
                convertWidth=(int)width;

        PdfDocument document=new PdfDocument();
        PdfDocument.PageInfo pageInfo=new PdfDocument.PageInfo.Builder(convertWidth,convertHeight,1).create();
        PdfDocument.Page page=document.startPage(pageInfo);

        Canvas canvas=page.getCanvas();
        Paint paint=new Paint();
        canvas.drawPaint(paint);
        bitmap=Bitmap.createScaledBitmap(bitmap,convertWidth,convertHeight,true);
        canvas.drawBitmap(bitmap,0,0,null);
        document.finishPage(page);

        //write document content
        String targetPdf="sdcard/page.pdf";
        File filepath=new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filepath));
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, "something want wrong try again"+e.toString(), Toast.LENGTH_SHORT).show();
        }
        //close document
        document.close();
        Toast.makeText(this, "pdf created successfully", Toast.LENGTH_SHORT).show();
        //openPdf();
    }

    private void openPdf() {
        File file=new File("/sdcard/page.pdf");
        Intent intent=new Intent(Intent.ACTION_VIEW);
        Uri uri=Uri.fromFile(file);
        intent.setDataAndType(uri,"application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, "No application found for pdf reader", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap loadBitmapFromView(LinearLayout linearLayout, int width, int height) {
        bitmap=Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        linearLayout.draw(canvas);
        return bitmap;
    }

    private void viewData() {
        Intent intent=getIntent();
        byte[]bytes=intent.getByteArrayExtra("avatar");
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        avatarview.setImageBitmap(bitmap);
        String fname=intent.getStringExtra("fname");
        String lname=intent.getStringExtra("lname");
        String email=intent.getStringExtra("email");
        String mobile=intent.getStringExtra("mobile");
        String age=intent.getStringExtra("age");

        //set view
        fnameview.setText(":-"+fname.toString());
        lnameview.setText(":-"+lname.toString());
        emailview.setText(":-"+email.toString());
        mobileview.setText(":-"+mobile.toString());
        ageview.setText(":-"+age.toString());
    }

    private void findId() {
        avatarview=(ImageView)findViewById(R.id.avatarview);
        fnameview=(TextView)findViewById(R.id.fnameview);
        lnameview=(TextView)findViewById(R.id.lnameview);
        emailview=(TextView)findViewById(R.id.emailview);
        mobileview=(TextView)findViewById(R.id.mobileview);
        ageview=(TextView)findViewById(R.id.ageview);
        linearLayout=findViewById(R.id.lld);
        save=findViewById(R.id.save);
    }
}
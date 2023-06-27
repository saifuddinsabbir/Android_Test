package com.example.test2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AppRating extends AppCompatActivity {

    final static int REQUEST_CODE = 1232;
    private static final int CREATE_FILE = 1;

    TextView nameText, ageText;
    Button downloadPDFBtn1, sharePDFBtn1;
    
    Bitmap bmp, scaledcmp, bitmap;

    LinearLayout downloadLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_rating);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();



        nameText = findViewById(R.id.nameTextId);
        ageText = findViewById(R.id.ageTextId);
        downloadPDFBtn1 = findViewById(R.id.downloadPDFBtn1);
        sharePDFBtn1 = findViewById(R.id.sharePDFBtn1);
        downloadLayout = findViewById(R.id.downloadLayoutId);

        nameText.setText("Name: "+getIntent().getStringExtra("name"));
        ageText.setText("Age: "+getIntent().getStringExtra("age"));


        downloadPDFBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askPermission();
                bitmap = loadBitmapFromView(downloadLayout,downloadLayout.getWidth(),downloadLayout.getHeight());
                covertXMLtoPDF();
                downloadPDFBtn1.setVisibility(View.INVISIBLE);
                sharePDFBtn1.setVisibility(View.VISIBLE);
            }
        });

        sharePDFBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String fileName = "example_XML.pdf";
                File file = new File(downloadsDir, fileName);

                if(file.exists()) {
                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    share.setType("application/pdf");
                    startActivity(share);
                }
            }
        });

//        PdfDocument pdfDocument = new PdfDocument();
//        Paint paint = new Paint();
//        Paint titlePaint = new Paint();
//
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200, 2100, 1).create();
//        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
//        Canvas canvas = page.getCanvas();
//
//        canvas.drawBitmap(scaledcmp, 0, 0, paint);
//
//        titlePaint.setTextAlign(Paint.Align.CENTER);
//        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        titlePaint.setTextSize(70);
//        canvas.drawText("Prescription", 1200/2, 270, titlePaint);
//
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(2);
//        canvas.drawRect(20, 70, 1200-20, 860, paint);
//
//
//        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("application/pdf");
//        intent.putExtra(Intent.EXTRA_TITLE, "invoice.pdf");
//        startActivityForResult(intent, CREATE_FILE);

    }

    private Bitmap loadBitmapFromView(LinearLayout linearLayout, int width, int height) {
        bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        linearLayout.draw(canvas);
        return bitmap;
    }



    private void askPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && false == Environment.isExternalStorageManager()) {
            Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
            startActivity(new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri));
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    private void covertXMLtoPDF() {
//        View view = LayoutInflater.from(this).inflate(R.layout.activity_app_rating, null);
//
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            this.getDisplay().getRealMetrics(displayMetrics);
//        } else {
//            this.getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
//        }
//
//        view.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY),
//                View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY));
//
//        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);

        //-----------------------------------------------------
        DisplayMetrics displayMetrics=new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float height=displayMetrics.heightPixels;
        float width=displayMetrics.widthPixels;

        int convertHeight=(int)height,
                convertWidth=(int)width;

        Toast.makeText(this, convertWidth + " " + convertHeight, Toast.LENGTH_SHORT).show();

        PdfDocument document=new PdfDocument();
        PdfDocument.PageInfo pageInfo=new PdfDocument.PageInfo.Builder(convertWidth,convertHeight,1).create();
        PdfDocument.Page page=document.startPage(pageInfo);

        Canvas canvas=page.getCanvas();
        Paint paint=new Paint();
        canvas.drawPaint(paint);
        bitmap=Bitmap.createScaledBitmap(bitmap,convertWidth,convertHeight,true);
        canvas.drawBitmap(bitmap,0,0,null);
        document.finishPage(page);
        //---------------------------------------------------------

//        PdfDocument document = new PdfDocument();
//
//        int viewWidth = view.getMeasuredWidth();
//        int viewHeight = view.getMeasuredHeight();
//
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(viewWidth, viewHeight, 1).create();
//        PdfDocument.Page page = document.startPage(pageInfo);
//
//        Canvas canvas = page.getCanvas();
//        view.draw(canvas);
//
//        document.finishPage(page);

//        String targetPdf="page.pdf";
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "example_XML.pdf";
        File file = new File(downloadsDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(this, "File converted successfully", Toast.LENGTH_SHORT).show();
            openPdf();

        } catch (FileNotFoundException e) {
            Toast.makeText(this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private void openPdf() {
        try {
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String fileName = "example_XML.pdf";
            File file = new File(downloadsDir, fileName);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri=Uri.fromFile(file);
            intent.setDataAndType(uri,"application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No application found for pdf reader", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
        }
    }
}
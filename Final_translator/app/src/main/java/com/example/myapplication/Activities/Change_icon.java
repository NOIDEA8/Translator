package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.StringPrepParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;

import com.example.myapplication.ActivityCollector.BaseActivity;

import java.net.URI;

public class Change_icon extends BaseActivity {

    private static final int REQUEST_READ_EXTERNAL_STORAGE = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("aaaaaaa","icon");
        // 检查READ_EXTERNAL_STORAGE权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 权限未被授予，请求权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        } else {
            openAlbum();
        }
    }

    private void openAlbum() {
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (requestCode == 1) {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        openAlbum();
                    } else {
                        Toast.makeText(this,"未得到相册权限",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
                break;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    if(Build.VERSION.SDK_INT>=19){
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                }
        }
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        //对应处理照片




    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.provides.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];
                String selection= MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            } else if ("com.android.provides.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath=getImagePath(uri,null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath=uri.getPath();
        }
        //对应处理照片



    }

    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path=null;
        Cursor cursor= getContentResolver().query(uri,null,selection,null,null);
        if (cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}
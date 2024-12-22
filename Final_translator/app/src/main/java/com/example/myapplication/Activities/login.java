package com.example.myapplication.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ActivityCollector.ActivityCollector;
import com.example.myapplication.ActivityCollector.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.Database.UsersDatabaseHelper;



public class login extends BaseActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private  CheckBox rememberPassword;
    private EditText editAccount;
    private EditText editPassword;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button login = findViewById(R.id.login);
        editAccount= findViewById(R.id.account_edit);
        editPassword= findViewById(R.id.password_edit);
        TextView sign_up = findViewById(R.id.sign_up);
        rememberPassword=findViewById(R.id.remember_password);
        name="";

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editAccount.getText().toString().isEmpty() || editPassword.getText().toString().isEmpty()) {
                    Toast.makeText(com.example.myapplication.Activities.login.this,
                            "请完整填写账号与密码", Toast.LENGTH_SHORT).show();
                } else {
                    checkLogin(editAccount.getText().toString(), editPassword.getText().toString());
                }
            }
        });
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(com.example.myapplication.Activities.login.this, com.example.myapplication.Activities.sign_up.class);
                startActivity(intent);
            }
        });

        pref= getSharedPreferences("logined",MODE_PRIVATE);
        Boolean isRemember=pref.getBoolean("remember_password",false);
        if(isRemember){
            String account=pref.getString("account","");
            String password=pref.getString("password","");
            editAccount.setText(account);
            editPassword.setText(password);
            rememberPassword.setChecked(true);
            Intent intent = new Intent(login.this, Mainpage.class);
            intent.putExtra("name", name);
            intent.putExtra("account", account);
            startActivity(intent);
            Toast.makeText(login.this, "您已成功登录", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @SuppressLint("Range")
    private void checkLogin(String account, String password) {
        UsersDatabaseHelper dbHelper = new UsersDatabaseHelper(this, "Users", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = "account= ? AND password= ?";
        String[] selectionArgs = new String[]{account, password};
        Cursor cursor = db.query("Users", null, selection, selectionArgs, null, null, null);
        int num = cursor.getCount();//问符合条件值个数
        cursor.moveToFirst();
        if (num == 0) {
            Toast.makeText(this, "账号或密码错误，请重新输入", Toast.LENGTH_SHORT).show();
        } else {
            editor=pref.edit();
            if(rememberPassword.isChecked()){
                editor.putBoolean("remember_password",true);
                editor.putString("account",editAccount.getText().toString());
                editor.putString("password",editPassword.getText().toString());
            }else{
                editor.clear();
            }
            editor.apply();
            name = cursor.getString(cursor.getColumnIndex("name"));
            Intent intent = new Intent(login.this, Mainpage.class);
            intent.putExtra("name", name);
            intent.putExtra("account", account);
            startActivity(intent);
            Toast.makeText(login.this, "您已成功登录", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
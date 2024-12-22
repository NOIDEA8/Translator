package com.example.myapplication.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.ActivityCollector.BaseActivity;
import com.example.myapplication.R;
import com.example.myapplication.Database.UsersDatabaseHelper;

import java.util.Random;

public class sign_up extends BaseActivity {
    private UsersDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        dbHelper = new UsersDatabaseHelper(this, "Users", null, 1);

        EditText newname = findViewById(R.id.edit_new_name);
        EditText newpassword = findViewById(R.id.edit_new_password);
        Button sign_up = findViewById(R.id.comfirm);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newname.getText().toString().isEmpty() ||
                        newpassword.getText().toString().isEmpty()) {
                    Toast.makeText(sign_up.this, "请完整填写账号与密码", Toast.LENGTH_SHORT).show();
                } else {
                    addUsers(newname.getText().toString(), newpassword.getText().toString());
                }
            }
        });
    }

    private void addUsers(String name, String password) {
        String account = randomAccount();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("password", password);
        cv.put("account", account);//174641 668956 656738 188527
        db.insert("Users", null, cv);
        Intent intent = new Intent(sign_up.this, sign_up_succeed.class);
        intent.putExtra("account", account);
        //Toast.makeText(sign_up.this,account,Toast.LENGTH_SHORT).show();
        startActivity(intent);
        cv.clear();
        finish();
    }

    private String randomAccount() {
        StringBuilder sb = new StringBuilder();
        String password = null;
        for (int i = 0; i < 6; i++) {
            Random random = new Random();
            int randomNumber = random.nextInt(10);  // 生成一个介于1到10之间的随机整数
            password = sb.append(randomNumber).toString();
        }
        return password;
    }
}
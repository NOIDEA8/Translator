package com.example.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.ActivityCollector.BaseActivity;
import com.example.myapplication.R;

public class sign_up_succeed extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_succeed);
        Button back = findViewById(R.id.back);
        TextView accountShow = findViewById(R.id.accountlayout);

        Intent intentForAccount = getIntent();
        String account = intentForAccount.getStringExtra("account");
        accountShow.setText(account);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentForBack = new Intent(sign_up_succeed.this, login.class);
                //Toast.makeText(sign_up_succeed.this,account,Toast.LENGTH_SHORT).show();
                startActivity(intentForBack);
                finish();
            }
        });
    }
}
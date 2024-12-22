package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.ActivityCollector.BaseActivity;
import com.example.myapplication.Database.UsersDatabaseHelper;
import com.example.myapplication.Fragment.MineFragment;
import com.example.myapplication.R;

public class Change_name extends BaseActivity implements View.OnClickListener {
    private ImageButton back;
    private Button OK;
    private EditText name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_name);
        initview();
    }

    private void initview(){
        back=findViewById(R.id.change_name_back);
        OK=findViewById(R.id.Ok);
        name=findViewById(R.id.edit_name);
        back.setOnClickListener(this);
        OK.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent1=new Intent(Change_name.this, Mainpage.class);

        switch (v.getId()){
            case R.id.change_name_back:
                finish();
                break;
            case R.id.Ok:
                String newname=name.getText().toString();
                if(newname.isEmpty()){
                    Toast.makeText(this,"请输入你的新昵称",Toast.LENGTH_SHORT).show();
                }else {

                    Log.d("改名","111");
                    Intent intent=getIntent();
                    String mParam2=intent.getStringExtra("account");

                    Toast.makeText(this,"成功",Toast.LENGTH_SHORT).show();
                    UsersDatabaseHelper dbhelp = new UsersDatabaseHelper(this, "Users", null, 1);
                    SQLiteDatabase db = dbhelp.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put("name",newname);
                    db.update("Users", cv, "account=?", new String[]{mParam2});
                    cv.clear();

                    intent1.putExtra("name",newname);
                    intent1.putExtra("account",mParam2);
                    intent1.putExtra("vpPage",2);
                    //获取当前活动id
                    intent1.putExtra("activityID",android.os.Process.myPid());
                    startActivity(intent1);
                    Log.d("Aaaaaaaaaaaa","我来了gebi");
                    finish();
                }
        }
    }
}
package com.example.myapplication.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.ActivityCollector.BaseActivity;
import com.example.myapplication.Database.UsersDatabaseHelper;
import com.example.myapplication.Fragment.AItryFragment;
import com.example.myapplication.RecyclerViews.History_words_Adaptor;
import com.example.myapplication.RecyclerViews.TipsKnowledge;
import com.example.myapplication.RecyclerViews.TipsKnowledgeAdaptor;
import com.example.myapplication.VPAdaptor.MyFragmentStateVPAdaptor;
import com.example.myapplication.Fragment.MainFragment;
import com.example.myapplication.Fragment.MineFragment;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mainpage extends AppCompatActivity implements View.OnClickListener {
    private ViewPager mViewPager;//300659，退出程序就退出应用的特性要改912325  205716
    private MyFragmentStateVPAdaptor mStateVPAdaptor;
    private List<Fragment> mFragmentList;
    private TextView tMain, tMine,tAi;
    private  MineFragment mineFragment;
    private MainFragment mainFragment;
    private AItryFragment aItryFragment;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        //实例对应
        initView();
        initData();
        Log.d("我在这","我在这");
        mStateVPAdaptor = new MyFragmentStateVPAdaptor(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mStateVPAdaptor);
        //页面滑动的监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //获取当前页位置
            @Override
            public void onPageSelected(int position) {
                onViewPagerSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

            //创建一个fragment的adaptor，然后设置为ViewPager的adaptor，这样就可把viewpager里放入碎片（主要贪图数据源）
            setClickListener();
            //默认首页
            onViewPagerSelected(0) ;


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.main_text:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.mine_text:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.ai_text:
                mViewPager.setCurrentItem(1);
                break;
            default:
                break;

        }
    }

    private void setClickListener() {
        tMine.setOnClickListener(this);
        tMain.setOnClickListener(this);
        tAi.setOnClickListener(this);
    }

    //自定义方法
    private void onViewPagerSelected(int position) {
        primaryButtonState();
        switch (position) {
            case 0:
                //Color.parseColor,将括号内的颜色转为int以适应函数
                tMain.setTextColor(Color.BLACK);
                tMain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40); // 使用SP单位，16是字体大小

                break;
            case 1:
                tAi.setTextColor(Color.BLACK);
                tAi.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
                break;
            case 2:
                tMine.setTextColor(Color.BLACK);
                tMine.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40); // 使用SP单位，16是字体大小
                break;
            default:
                break;
        }
    }

    private void primaryButtonState() {
        tMain.setTextColor(Color.GRAY);
        tMain.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        tAi.setTextColor(Color.GRAY);
        tAi.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        tMine.setTextColor(Color.GRAY);
        tMine.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
    }

    private void initView() {
        mViewPager = findViewById(R.id.vp);
        tMain = findViewById(R.id.main_text);
        tMine = findViewById(R.id.mine_text);
        tAi=findViewById(R.id.ai_text);
    }

    private void initData() {
        mFragmentList = new ArrayList<>();
        intent = getIntent();

        mineFragment = MineFragment.newInstance(intent.getStringExtra("name"),
                intent.getStringExtra("account"),
                this);
        mineFragment.onAttach(Mainpage.this);

        aItryFragment=AItryFragment.newInstance(null,null);
        aItryFragment.onAttach(Mainpage.this);

        mainFragment = MainFragment.newInstance(null, null);

        mFragmentList.add(mainFragment);
        mFragmentList.add(aItryFragment);
        mFragmentList.add(mineFragment);
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onNewIntent(Intent intentnew) {
        super.onNewIntent(intent);
        Log.d("我在这","我在这33333");
        intent=intentnew;
        mViewPager.setAdapter(mStateVPAdaptor);
        if (intent.hasExtra("vpPage")) {
            Log.d("我在这","我在这");
            int value = intent.getIntExtra("vpPage", 0);
            mViewPager.setCurrentItem(value, false);
            /*int preAPPID=intent.getIntExtra("activityID",0);
            android.os.Process.killProcess(preAPPID);*/
            setClickListener();
            onViewPagerSelected(value);
        }else{
            //创建一个fragment的adaptor，然后设置为ViewPager的adaptor，这样就可把viewpager里放入碎片（主要贪图数据源）
            setClickListener();
            //默认首页
            onViewPagerSelected(0) ;
        }
    }
}
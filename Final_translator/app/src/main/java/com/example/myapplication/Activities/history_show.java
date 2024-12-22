package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myapplication.Database.HistoryWordDatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.RecyclerViews.HistoryWords;
import com.example.myapplication.RecyclerViews.History_words_Adaptor;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class history_show extends AppCompatActivity {
    private List<HistoryWords> history_words;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private History_words_Adaptor adaptor;
    private SearchView sv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_history_show);
        recyclerView = findViewById(R.id.show_history);
        sv=findViewById(R.id.search_history);
        history_words = new ArrayList<>();
        history_words.clear();
        addDataFromBase();
        initRecycler(history_words);
    }
    private void initRecycler( List<HistoryWords> resouseList) {

        layoutManager = new LinearLayoutManager(this);
        adaptor = new History_words_Adaptor(resouseList, this,this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adaptor);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //点击搜索时调用
            @Override
            public boolean onQueryTextSubmit(String query) {
                //返回true，SearchView通常不会执行任何默认的搜索行为。
                //返回false，表示你没有处理用户的搜索请求，SearchView可能会尝试执行其自己的默认搜索行为，
                // 这取决于你的应用是如何配置SearchView的
                return false;
            }

            //在输入内容发生变化时调用
            @Override
            public boolean onQueryTextChange(String newText) {
                int textLength = newText.length();
                if (textLength != 0) {
                    List<HistoryWords> temp=searchByText(newText);
                    history_words.clear();
                    history_words=temp;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initRecycler(history_words);
                        }
                    });
                } else {
                    Log.d("search","执行1次");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            history_words.clear();
                            addDataFromBase();
                            initRecycler(history_words);
                        }
                    });
                }

                return true;
            }
        });
    }
    @SuppressLint("Range")
    private void addDataFromBase() {
        HistoryWordDatabaseHelper dbHelper = new HistoryWordDatabaseHelper(this, "HistoryWords",
                null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Intent intent=getIntent();
        String account=intent.getStringExtra("account");
        Cursor cursor = null;
        try {
            cursor = db.query("HistoryWords", null, "account=?",new String[]{account},
                    null, null, "id DESC");
            if (cursor.moveToFirst()) {
                do {
                    HistoryWords hw = new HistoryWords();
                    hw.setOrigin(cursor.getString(cursor.getColumnIndex("origin")));
                    hw.setTranslated(cursor.getString(cursor.getColumnIndex("translated")));
                    hw.setFromLanguage(cursor.getString(cursor.getColumnIndex("fromLanguage")));
                    hw.setToLanguage(cursor.getString(cursor.getColumnIndex("toLanguage")));
                    history_words.add(hw);

                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            // Handle the exception here
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        history_words=arrange(history_words);
    }

    private List<HistoryWords>searchByText(String text){
        List<HistoryWords> tempList = new ArrayList<>();
        int textLength = text.length();
        for (HistoryWords hws : history_words) {
            int wordLength = hws.getOrigin().length();
            if (wordLength < textLength) {

            } else {
                String hwsSub = hws.getOrigin().substring(0, textLength);
                if (hwsSub.equals(text)) {
                    tempList.add(hws);
                }
            }
        }
        return tempList;
    }

   private List<HistoryWords> arrange(List<HistoryWords> list){
        //temp仅为了排名字
        List<String> temp=new ArrayList<>();
        //最后返回的排好的list
        List<HistoryWords> finalList=new ArrayList<>();
        //取出传入的列表的原句内容
        for(HistoryWords hs:list){
            String stemp=hs.getOrigin();
            temp.add(stemp);
        }
        //排序
        String[] arrays = temp.toArray(new String[temp.size()]);
        List<String> ids=new ArrayList<>();
        Comparator<Object> com = Collator.getInstance(java.util.Locale.CHINA);
        Arrays.sort(arrays, com);
        //排序后与源数据比较后返回处理后的数据源
        for(String s:arrays){
            for(HistoryWords hs1:list){
                String origin=hs1.getOrigin();
                if(origin.equals(s)){
                    if(ids.contains(hs1.getTranslated())){

                    } else{
                        finalList.add(hs1);
                        ids.add(hs1.getTranslated());
                        break;}
                }
            }
        }
        return finalList;
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("在这里2","在这里2");
        history_words.clear();
        sv.setQuery("", false);
        history_words.clear();
        addDataFromBase();
        initRecycler(history_words);
    }
}
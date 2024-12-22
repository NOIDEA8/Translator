package com.example.myapplication.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Database.HistoryWordDatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.RecyclerViews.History_words_Adaptor;
import com.example.myapplication.RecyclerViews.HistoryWords;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

//监听spinner还没做
public class search extends AppCompatActivity {
    private Spinner from;
    private Spinner to;
    private SearchView sv;
    private Intent startSearch;
    private String sto;
    private String sfrom;
    private String tolanguage;
    private String fromlanguage;
    private Intent getResult;
    private List<HistoryWords> history_words;
    private RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    History_words_Adaptor adaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        startSearch = new Intent(search.this, searchResult.class);
        getResult = null;
        from = findViewById(R.id.spinner_from);
        to = findViewById(R.id.spinner_to);
        sv = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.history_words);
        history_words = new ArrayList<>();

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //点击搜索时调用
            @Override
            public boolean onQueryTextSubmit(String query) {
                //返回true，SearchView通常不会执行任何默认的搜索行为。
                //返回false，表示你没有处理用户的搜索请求，SearchView可能会尝试执行其自己的默认搜索行为，
                // 这取决于你的应用是如何配置SearchView的
                startSearch.putExtra("origin_Contant", query);
                startSearch.putExtra("account",getIntent().getStringExtra("account"));
                startActivityForResult(startSearch, 1);
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

        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //选择一个项时调用，并且传入选中项的位置（position）和ID（id）
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sfrom = from.getSelectedItem().toString();
                fromlanguage = chooseType(sfrom);
                startSearch.putExtra("fromLanguage", fromlanguage);
            }

            //没有选中任何项时被调用
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                from.setSelection(0);
            }
        });

        to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sto = to.getSelectedItem().toString();
                tolanguage = chooseType(sto);
                startSearch.putExtra("toLanguage", tolanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                to.setSelection(0);
            }
        });
        Log.d("在这里","在这里");
        history_words.clear();
        addDataFromBase();
        initRecycler(history_words);
        //Toast.makeText(this,adaptor.getItemCount(),Toast.LENGTH_SHORT).show();
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

    private void initRecycler( List<HistoryWords> resouseList) {

        layoutManager = new LinearLayoutManager(this);
        adaptor = new History_words_Adaptor(resouseList, this,this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adaptor);
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

    private String chooseType(String inType) {
        String return_type = "auto";
        switch (inType) {
            case "自动检测(auto)":
                return_type = "auto";
                break;
            case "中文(zh)":
                return_type = "zh";
                break;
            case "英语(en)":
                return_type = "en";
                break;
            case "繁体中文(cht)":
                return_type = "cht";
                break;
            case "粤语(yue)":
                return_type = "yue";
                break;
            case "文言文(wyw)":
                return_type = "wyw";
                break;
            case "法语(fra)":
                return_type = "fra";
                break;
            case "韩语(kor)":
                return_type = "kor";
                break;
            case "意大利语(it)":
                return_type = "it";
                break;
            case "日语(jp)":
                return_type = "jp";
                break;
            case "西班牙语(spa)":
                return_type = "spa";
                break;
            case "希腊语(el)":
                return_type = "el";
                break;
            default:
                break;
        }
        return return_type;
    }

   /* @Override*/
    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Log.d("在这里3","在这里3");
                    getResult = getIntent();
                    history_words.clear();
                    addNewData(getResult);
                }
        }
    }*/

    @SuppressLint("Range")
    private void addDataFromBase() {
        int n=0;
        HistoryWordDatabaseHelper dbHelper = new HistoryWordDatabaseHelper(this, "HistoryWords",
                null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Intent intent=getIntent();
        String account=intent.getStringExtra("account");
        Cursor cursor = null;
        try {
            cursor = db.query("HistoryWords", null, "account=?", new String[]{account},
                    null, null, "id DESC");
            if (cursor.moveToFirst()) {
                do {
                    HistoryWords hw = new HistoryWords();
                    hw.setOrigin(cursor.getString(cursor.getColumnIndex("origin")));
                    hw.setTranslated(cursor.getString(cursor.getColumnIndex("translated")));
                    hw.setFromLanguage(cursor.getString(cursor.getColumnIndex("fromLanguage")));
                    hw.setToLanguage(cursor.getString(cursor.getColumnIndex("toLanguage")));
                    history_words.add(hw);
                    n++;
                } while (cursor.moveToNext()&&n<15);
            }
        } catch (SQLException e) {
            // Handle the exception here
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        //history_words=arrange(history_words);
    }

    /*private void addNewData(Intent intent) {
        String origin1 = intent.getStringExtra("origin");
        String result1 = intent.getStringExtra("result");
        if (origin1 != null && result1 != null) {
            HistoryWords hw = new HistoryWords();
            hw.setOrigin(intent.getStringExtra("origin"));
            hw.setTranslated(intent.getStringExtra("result"));
            history_words.add(hw);
        }
        //history_words=arrange(history_words);
    }*/

}
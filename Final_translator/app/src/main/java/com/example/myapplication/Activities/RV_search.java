package com.example.myapplication.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ActivityCollector.BaseActivity;
import com.example.myapplication.Database.HistoryWordDatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.Translator.TranslateResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RV_search extends BaseActivity {
    private Intent intent;
    private String fromLanguage;//目标语言
    private String toLanguage;//翻译语言
    private String origincontent;
    private String originResult;
    private Spinner toWhich;
    private TextView tv;
    private TextView otvo;
    private TextView otvr;
    private LinearLayout recyclervew_search;
    private ImageButton ibrvBack;
    private final String appId = "20240414002023290";
    private final String key = "Sfk0qmfG7EnoDD_U0ccD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_search);
        initlanguage();
    }

    private void initlanguage() {
        intent = getIntent();
        origincontent = intent.getStringExtra("origin");
        fromLanguage = intent.getStringExtra("fromLanguage");
        originResult=intent.getStringExtra("translated");

        tv=findViewById(R.id.result_sentence);
        toWhich = findViewById(R.id.rv_spinner_to);
        otvo=findViewById(R.id.rv_origin_sentence);
        otvr=findViewById(R.id.rv_resulContent);
        recyclervew_search=findViewById(R.id.recyclervew_search);
        ibrvBack=findViewById(R.id.rv_back);

        toWhich.setSelection(0);
        toLanguage = chooseType(toWhich.getSelectedItem().toString());

        recyclervew_search.setVisibility(View.INVISIBLE);

        toWhich.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toLanguage = chooseType(toWhich.getSelectedItem().toString());
                translation(origincontent);
                Log.d("RV_search",toLanguage);
                Log.d("RV_search",fromLanguage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                translation(origincontent);
            }
        });

        ibrvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void translation(String inputTx) {
        //获取输入的内容
        //判断输入内容是否为空
        if (!inputTx.isEmpty() || !"".equals(inputTx)) {//不为空
            String salt = num(1);
            //拼接一个字符串然后加密
            String spliceStr = appId + inputTx + salt + key;//根据百度要求 拼接
            String sign = stringToMD5(spliceStr);//将拼接好的字符串进行MD5加密   作为一个标识
            Log.d("searchResult", "进入翻译器 ");
            //异步Get请求访问网络
            asyncGet(inputTx, fromLanguage, toLanguage, salt, sign);
        }
    }

    public static String num(int a) {
        Random r = new Random(a);
        int ran1 = 0;
        for (int i = 0; i < 5; i++) {
            ran1 = r.nextInt(100);
            System.out.println(ran1);
        }
        return java.lang.String.valueOf(ran1);//valueOf:转数字为字符串
    }

    public static String stringToMD5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    private void asyncGet(String content, String fromType, String toType, String salt, String sign) {
        //通用翻译API HTTP地址：
        //http://api.fanyi.baidu.com/api/trans/vip/translate
        //通用翻译API HTTPS地址：
        //https://fanyi-api.baidu.com/api/trans/vip/translate
        Log.d("searchResult", "进入解析器 ");
        String httpStr = "http://api.fanyi.baidu.com/api/trans/vip/translate";
        String httpsStr = "https://fanyi-api.baidu.com/api/trans/vip/translate";
        //拼接请求的地址
        String url = httpsStr +
                "?appid=" + appId + "&q=" + content + "&from=" + fromType + "&to=" +
                toType + "&salt=" + salt + "&sign=" + sign;
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("searchResult", "失败");
                goToThread(e.toString(), 0);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("searchResult", "成功");
                goToThread(response.body().string(), 1);
                Log.d("searchResult", "成功2222");
            }
        });
    }

    private void goToThread(final Object object, final int key) {
        //切换到主线程处理数据
        if (object == null) {
        }
        if (key == 0) {//异常返回
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showMsg("异常信息：" + object.toString());
                }
            });
            Log.e("MainActivity", object.toString());
        } else {//正常返回
            //通过Gson 将 JSON字符串转为实体Bean
            final TranslateResult result = new Gson().fromJson(object.toString(), TranslateResult.class);
            Log.d("MainActivity", object.toString());
            //显示翻译的结果
            if (result.getTrans_result() == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showMsg(object.toString());
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("searchResult", "更新UI");
                        tv.setText(result.getTrans_result().get(0).getDst());
                        otvo.setText(origincontent);
                        otvr.setText(originResult);
                        recyclervew_search.setVisibility(View.VISIBLE);
                        record();
                    }
                });

            }
            //show.setText(result.getTrans_result().get(0).getDst());
        }
    }

    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void record() {
        HistoryWordDatabaseHelper dbHelper = new HistoryWordDatabaseHelper(this, "HistoryWords",
                null, 1);
        String origin = origincontent;
        String translated = tv.getText().toString();
        if (recordJudger(origin, translated)) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("origin", origin);
            cv.put("fromLanguage", fromLanguage);
            cv.put("translated", translated);
            cv.put("toLanguage", toLanguage);
            cv.put("account",getIntent().getStringExtra("account"));
            db.insert("HistoryWords", null, cv);
            cv.clear();
            Log.d("searchResult", fromLanguage + toLanguage);
        }
    }

    private boolean recordJudger(String origin, String translated) {
        boolean returnString = false;
        HistoryWordDatabaseHelper dbHelper = new HistoryWordDatabaseHelper(this, "HistoryWords",
                null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query("HistoryWords", null, "origin=? AND translated=?", new String[]{origin, translated},
                    null, null, null);
            if (cursor.getCount() != 0) {
                returnString = false;
            } else returnString = true;
        } catch (SQLException e) {
            // Handle the exception here
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return returnString;
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
}
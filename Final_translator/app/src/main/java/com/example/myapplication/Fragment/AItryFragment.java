package com.example.myapplication.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.RecyclerViews.ChatAdaptor;
import com.example.myapplication.RecyclerViews.ChatMsg;
import com.iflytek.sparkchain.core.LLM;
import com.iflytek.sparkchain.core.LLMCallbacks;
import com.iflytek.sparkchain.core.LLMConfig;
import com.iflytek.sparkchain.core.LLMError;
import com.iflytek.sparkchain.core.LLMEvent;
import com.iflytek.sparkchain.core.LLMResult;
import com.iflytek.sparkchain.core.SparkChain;
import com.iflytek.sparkchain.core.SparkChainConfig;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AItryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AItryFragment extends Fragment {
    private Context context;
    private RecyclerView rv;
    private ChatAdaptor adaptor;
    private EditText inputText;
    private Button send;
    private LLM llm;
    private List<ChatMsg> msgList=new ArrayList<>();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String result;
    private int statue;
    private String question_o;
    private boolean prevent;
    private String recordMsg;
    private String mParam1;
    private String mParam2;

    public AItryFragment() {
        // Required empty public constructor
    }


    public static AItryFragment newInstance(String param1, String param2) {
        AItryFragment fragment = new AItryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ai_try, container, false);
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

    }

    private void initView(View view){
        inputText=view.findViewById(R.id.edit_msg);
        send=view.findViewById(R.id.bt_msg);
        rv=view.findViewById(R.id.rv_msg);
        LinearLayoutManager layoutManager=new LinearLayoutManager(context);
        rv.setLayoutManager(layoutManager);
        adaptor=new ChatAdaptor(msgList);
        rv.setAdapter(adaptor);
        statue=0;
        prevent=false;

        result=null;
        recordMsg=null;
        question_o="";

        if(msgList.size()==0){
            ChatMsg msgFirstReceive=new ChatMsg("有什么能够帮您？",ChatMsg.TYPE_RECEIVED);
            msgList.add(msgFirstReceive);
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=inputText.getText().toString();
                if (!"".equals(content)&&statue!=1&&!prevent){
                    ChatMsg msg=new ChatMsg(content,ChatMsg.TYPE_SEND);
                    msgList.add(msg);
                    adaptor.notifyItemInserted(msgList.size()-1);
                    rv.scrollToPosition(msgList.size()-1);
                    prevent=true;
                    new Thread(new Runnable() {
                       @Override
                       public void run() {
                           initConfig();
                           llm = getLLm();
                           initLLMcallback(llm);
                           questioning(llm);

                       }
                   }).start();
                }else if("".equals(content)){
                    Toast.makeText(context,"请先输入文字",Toast.LENGTH_SHORT).show();
                } else if(statue==1){
                    Toast.makeText(context,"正在回答，请稍后",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initLLMcallback(LLM llm) {
        LLMCallbacks llmCallbacks = new LLMCallbacks() {
            StringBuilder contain=new StringBuilder();
            int n=0;
            @Override
            public void onLLMResult(LLMResult llmResult, Object usrContext) {
               if((statue=llmResult.getStatus())==1||llmResult.getStatus()==0){
                   n++;
                   contain.append(llmResult.getContent());
                   if(n==1){
                       ChatMsg msgTips=new ChatMsg("正在回答，请稍等",ChatMsg.TYPE_RECEIVED);
                       msgList.add(msgTips);
                       getActivity().runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               adaptor.notifyItemInserted(msgList.size()-1);
                               rv.scrollToPosition(msgList.size()-1);
                           }
                       });
                   }
                   Log.d("在这里",llmResult.getContent());

               } else if ((statue=llmResult.getStatus())==2) {
                   contain.append(llmResult.getContent());
                   result=contain.toString();
                   contain.setLength(0);
                   statue=0;
                   Log.d("在这里",result);
                   n=0;

                   msgList.remove(msgList.size()-1);
                   ChatMsg msg=new ChatMsg(result,ChatMsg.TYPE_RECEIVED);
                   msgList.add(msg);

                   getActivity().runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           adaptor.notifyItemRemoved(msgList.size()-1);
                           adaptor.notifyItemInserted(msgList.size()-1);
                           rv.scrollToPosition(msgList.size()-1);
                       }
                   });
                    prevent=false;
               }
            }
            @Override
            public void onLLMEvent(LLMEvent event, Object usrContext) {
                Log.w("是我","onLLMEvent:" + " " + event.getEventID() + " " + event.getEventMsg());

            }
            @Override
            public void onLLMError(LLMError error, Object usrContext) {
                //Log.e("是我","onLLMError:" + " " + error.getErrCode() + " " + error.getErrMsg());
            }
        };
        llm.registerLLMCallbacks(llmCallbacks);
    }
    private void questioning(LLM llm) {
        String myContext = "myContext";
        String results=null;
        results=result;

        if(question_o.isEmpty()||question_o==""){
            String question1 =inputText.getText().toString() ;
            if(question1!=""&&!question1.isEmpty()) {
                inputText.setText("");
                int ret = llm.arun(question1,myContext);
                question_o=inputText.getText().toString();
                Log.d("异步是我","turn1:"+results+ret);
                question_o=question1;
            }
        }else {
            String question2 = inputText.getText().toString();
            inputText.setText("");
            try {
                JSONArray array = new JSONArray();
                JSONObject item_1 = new JSONObject();
                item_1.put("role", "user");
                item_1.put("content", question_o);
                JSONObject item_2 = new JSONObject();
                item_2.put("role", "assistant");
                item_2.put("content", results);
                JSONObject item_3 = new JSONObject();
                item_3.put("role", "user");
                item_3.put("content", question2);
                array.put(item_1).put(item_2).put(item_3);
                int ret = llm.arun(array.toString(), myContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

       while (statue!=2) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
        }

        results = "";
        Log.d("异步是我", "turn2:" + results);
        SparkChain.getInst().unInit();
    }
    @NonNull
    private static LLM getLLm() {
        LLMConfig llmConfig = LLMConfig.builder();
        llmConfig.domain("general");
        llmConfig.url("wss://spark-api.xf-yun.com/v1.1/chat");//如果使用generalv2，domain和url都可缺省，SDK默认；如果使用general，url可缺省，SDK会自动补充；如果是其他，则需要设置domain和url。
        LLM llm = new LLM(llmConfig);
        return llm;
    }

    public void initConfig(){
        SparkChainConfig config =  SparkChainConfig.builder()
                .appID("4629d5c4")
                .apiKey("c19e6b82329a24d7b610c1ff2bd9c7f9")
                .apiSecret("MWEzNTUzM2VmYWE1MTgxZjFjM2RhOThi");//从平台获取的授权appid，apikey,apisecrety
        int ret = SparkChain.getInst().init(context, config);
        Log.d("保佑","sdk init:"+ret);

    }

}
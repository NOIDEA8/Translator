package com.example.myapplication.Fragment;

import android.content.Context;
import android.content.Intent;
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

import com.example.myapplication.Activities.search;
import com.example.myapplication.R;
import com.example.myapplication.RecyclerViews.TipsKnowledge;
import com.example.myapplication.RecyclerViews.TipsKnowledgeAdaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainFragment extends Fragment implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private View show;
    private Context context;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private TipsKnowledgeAdaptor adaptor;
    private TipsKnowledge tipsKnowledge;
    private List<TipsKnowledge> tipsKnowledgeList;
    private List<String> tipsResourse;

    public MainFragment() {

    }


    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        show = view.findViewById(R.id.click_for_search);
        show.setOnClickListener(this);
        recyclerView= view.findViewById(R.id.rv_tips_knowledge);
        tipsKnowledgeList = new ArrayList<>();
        initrecycler(randomResourse());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //这里是效仿有道翻译，具体方法是fragment重叠，具体看对应xml文件
            case R.id.click_for_search:
                Log.d("Mainpage", "到了碎片");
                Intent intent = new Intent(context, search.class);
                Log.d("Mainpage", "到了碎片2");
                Intent intent1=getActivity().getIntent();
                intent.putExtra("account",intent1.getStringExtra("account"));
                startActivity(intent);

                break;
            default:
                break;
        }
    }
    private void initrecycler(List<TipsKnowledge> resouseList){
        layoutManager = new LinearLayoutManager(context);
        adaptor = new TipsKnowledgeAdaptor(resouseList, context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adaptor);
    }
    private void initTipsResourse(){
        tipsResourse=new ArrayList<>();
        tipsResourse.add("柬埔寨语的字母表非常长，总共有74个字母，比许多其他语言的字母表都要复杂。");
        tipsResourse.add("英语单词“alphabet”（字母表）来源于希腊字母表的前两个字母——alpha（阿尔法）和beta（贝塔）。");
        tipsResourse.add("英语中“race car”和“kayak”这样的单词，从前往后读和从后往前读都是相同的，这种词被称为回文词。");
        tipsResourse.add("在德语中，形容词的性（阳性、中性和阴性）取决于它所修饰的名词的性。");
        tipsResourse.add("西班牙语中有一种称为“未来未完成时”的时态，用来描述未来某个时间点将会进行但尚未完成的动作。");
        tipsResourse.add("俄语中有六个不同的格（case），用于表示名词在句子中的不同角色和关系。");
        tipsResourse.add("阿拉伯语是一种从右到左书写的语言，与大多数西方语言从左到右的书写方式相反。");
        tipsResourse.add("在日语中，同一个汉字可以有多种读音，取决于它在句子中的位置和上下文。");
        tipsResourse.add("法语中的“bonjour”和意大利语中的“buongiorno”都表示“早上好”，但发音和拼写有所不同，体现了语言之间的亲缘关系。");
        tipsResourse.add("芬兰语中有15个格，是拥有最多格的语言之一，这使得芬兰语的句子结构非常灵活和复杂。");
        tipsResourse.add("荷兰语中的“ij”是一个特殊的字母组合，发音类似于英语中的“ai”音，但它被视为一个单独的字母。");
        tipsResourse.add("韩语使用了一种独特的字母系统，称为朝鲜文字，其字母形状和发音与其他语言有很大不同。");
        tipsResourse.add("泰语中的声调对于意义至关重要，不同的声调可以改变单词的含义。");
        tipsResourse.add("匈牙利语是一种属于乌拉尔语系的语言，与欧洲大多数语言不属于同一语系。");
        tipsResourse.add("土耳其语是一种粘着语，通过添加后缀来改变单词的词性、时态和语态。\n");
        tipsResourse.add("在夏威夷语中，亲属关系的称呼非常详细，可以精确到特定的家庭成员和血缘关系。");
        tipsResourse.add("韩语中的敬语和平语有着明显的区别，反映了韩国社会严格的等级制度。");
        tipsResourse.add("意大利语中的“amore”（爱）发音优美，常被用于歌曲和诗歌中，展现了意大利语的浪漫气质。");
        tipsResourse.add("德语中的长词“Rindfleischetikettierungsüberwachungsaufgabenübertragungsgesetz”意为“牛肉标签监管职责转移法”，展示了德语构词法的强大能力。");
        tipsResourse.add("阿拉伯语中的《古兰经》是用古典阿拉伯语写成的，对阿拉伯语的发展产生了深远影响。");
        tipsResourse.add("汉语中的成语和谚语往往富含深厚的文化意蕴，如“画蛇添足”和“井底之蛙”，都反映了古代中国的智慧和哲理。");

    }

    private List<TipsKnowledge> randomResourse(){
        initTipsResourse();
        Random rand=new Random();
        List<Integer> order=new ArrayList<>();
        List<TipsKnowledge> finalList=new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int temp=rand.nextInt(20);
            if(order.contains(temp)){i--;}
            else{order.add(temp);}
        }
        for (int i = 0; i < 5; i++) {
            tipsKnowledge=new TipsKnowledge();
            tipsKnowledge.setKnowledge(tipsResourse.get(order.get(i)));
            finalList.add(tipsKnowledge);
        }
        return finalList;
    }
}
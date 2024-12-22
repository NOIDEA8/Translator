package com.example.myapplication.RecyclerViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class TipsKnowledgeAdaptor extends RecyclerView.Adapter<TipsKnowledgeAdaptor.ViewHolder>{
    private List<TipsKnowledge> mtipsList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView TipsKnowledge;
        public ViewHolder(View view) {
            super(view);
            TipsKnowledge=view.findViewById(R.id.Tips_knowledge);
        }
    }//建立内部类viewfolder继承自recyclerview

    public TipsKnowledgeAdaptor(List<TipsKnowledge> mtipssList, Context context) {
        this.mtipsList = mtipssList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_tips_knowledge, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }//为viewholder建模

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       TipsKnowledge tk=mtipsList.get(position);
       int order=position+1;
       holder.TipsKnowledge.setText(order+"."+tk.getKnowledge());
    }//为viewfolder赋值

    @Override
    public int getItemCount() {
        return mtipsList.size();
    }//展示内容容量

    public List<TipsKnowledge> getMwordsList() {
        return mtipsList;
    }

    public void setMtipsList(List<TipsKnowledge> mwordsList) {
        this.mtipsList = mwordsList;
    }
}

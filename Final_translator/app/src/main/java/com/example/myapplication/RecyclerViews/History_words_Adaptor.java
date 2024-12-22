package com.example.myapplication.RecyclerViews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activities.RV_search;
import com.example.myapplication.R;

import java.util.List;

public class History_words_Adaptor extends RecyclerView.Adapter<History_words_Adaptor.ViewHolder> {
    private List<HistoryWords> mwordsList;
    private Context context;
    private Activity activity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View wordView;
        TextView origin;
        TextView translated;

        public ViewHolder(View view) {
            super(view);
            origin = view.findViewById(R.id.origin);
            translated = view.findViewById(R.id.translated);
            wordView = view;
        }
    }//建立内部类viewfolder继承自recyclerview

    public History_words_Adaptor(List<HistoryWords> mwordsList, Context context,Activity activity) {
        this.mwordsList = mwordsList;
        this.context = context;
        this.activity=activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_words, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }//为viewholder建模

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryWords words = mwordsList.get(position);
        Log.d("login", words.getOrigin() + "(" + words.getFromLanguage() + ")"
                + words.getTranslated() + "(" + words.getToLanguage() + ")");
        holder.origin.setText(words.getOrigin() + "(" + words.getFromLanguage() + ")");
        holder.translated.setText(words.getTranslated() + "(" + words.getToLanguage() + ")");
        holder.wordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                HistoryWords hw = mwordsList.get(position);
                Intent intent = new Intent(context, RV_search.class);
                Intent intent1=activity.getIntent();
                intent.putExtra("origin", hw.getOrigin());
                intent.putExtra("fromLanguage", hw.getFromLanguage());
                intent.putExtra("toLanguage",hw.getToLanguage());
                intent.putExtra("translated",hw.getTranslated());
                intent.putExtra("account",intent1.getStringExtra("account"));
                Log.d("MainPage", hw.getFromLanguage());
                Log.d("MainPage", hw.getOrigin());
                context.startActivity(intent);
            }
        });
    }//为viewfolder赋值

    @Override
    public int getItemCount() {
        return mwordsList.size();
    }//展示内容容量

    public List<HistoryWords> getMwordsList() {
        return mwordsList;
    }

    public void setMwordsList(List<HistoryWords> mwordsList) {
        this.mwordsList = mwordsList;
    }
}


package com.example.myapplication.RecyclerViews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.iflytek.sparkchain.plugins.mail.Mail;

import java.util.List;

public class ChatAdaptor extends RecyclerView.Adapter<ChatAdaptor.ViewHolder> {
    private List<ChatMsg> mMsgList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        RelativeLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        public ViewHolder(View view){
            super(view);
            leftLayout=(LinearLayout) view.findViewById(R.id.left_layout);
            rightLayout=(RelativeLayout) view.findViewById(R.id.right_layout);
            leftMsg=(TextView) view.findViewById(R.id.left_msg_tv);
            rightMsg=(TextView) view.findViewById(R.id.right_msg_tv);
        }
    }

    public ChatAdaptor(List<ChatMsg> mMsgList) {
        this.mMsgList = mMsgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_chat,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        ChatMsg msg=mMsgList.get(position);
        if(msg.getType()==ChatMsg.TYPE_RECEIVED){
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
        } else if (msg.getType()== ChatMsg.TYPE_SEND) {
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getContent());

        }
    }
    @Override
    public int getItemCount(){
        return  mMsgList.size();
    }
}

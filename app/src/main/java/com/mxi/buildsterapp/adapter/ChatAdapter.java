package com.mxi.buildsterapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.model.ChatMessageData;

import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.CustomViewHolder> {

    private Context context;
    CommanClass cc;
    private ArrayList<ChatMessageData> arrayList;

    String firstname;

    public ChatAdapter(Context context, ArrayList<ChatMessageData> list) {
        cc = new CommanClass(context);
        this.context = context;
        arrayList = ((ArrayList) list);


        firstname = cc.loadPrefString("firstname");
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.chat_item, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {

        final ChatMessageData list = arrayList.get(i);

        if(list.getFromId().equals(cc.loadPrefString("user_id"))){
            holder.ll_user1_chat.setVisibility(View.VISIBLE);
            holder.ll_user2_chat.setVisibility(View.GONE);

            holder.tv_user1_chat_message.setText(list.getChatMessage());
            holder.tv_user1_name.setText(list.getFromName());
            holder.tv_user1_time.setText(list.getFromTime());

        }else{
            holder.ll_user2_chat.setVisibility(View.VISIBLE);
            holder.ll_user1_chat.setVisibility(View.GONE);

            holder.tv_user2_chat_message.setText(list.getChatMessage());
            holder.tv_user2_name.setText(list.getFromName());
            holder.tv_user2_time.setText(list.getToTime());
        }
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_user1_chat,ll_user2_chat;
        protected TextView tv_user1_name, tv_user2_name, tv_user1_time, tv_user2_time,tv_user1_chat_message,tv_user2_chat_message;

        public CustomViewHolder(View convertView) {
            super(convertView);
            ll_user1_chat= (LinearLayout) convertView.findViewById(R.id.ll_user1_chat);
            ll_user2_chat= (LinearLayout) convertView.findViewById(R.id.ll_user2_chat);

            tv_user1_name = (TextView) convertView.findViewById(R.id.tv_user1_name);
            tv_user2_name = (TextView) convertView.findViewById(R.id.tv_user2_name);

            tv_user1_time = (TextView) convertView.findViewById(R.id.tv_user1_time);
            tv_user2_time = (TextView) convertView.findViewById(R.id.tv_user2_time);

            tv_user1_chat_message = (TextView) convertView.findViewById(R.id.tv_user1_chat_message);
            tv_user2_chat_message = (TextView) convertView.findViewById(R.id.tv_user2_chat_message);

        }
    }

}
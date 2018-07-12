package com.mxi.buildsterapp.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.model.ActionItemData;
import com.mxi.buildsterapp.model.MessageData;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    static CommanClass cc;

    private List<MessageData> message_list;
    private int rowLayout;
    public static Context mContext;


    public MessageAdapter(List<MessageData> message_list, int rowLayout, Context context) {

        cc = new CommanClass(context);

        this.message_list = message_list;
        this.rowLayout = rowLayout;
        this.mContext = context;

    }

    @Override
    public int getItemCount() {
        return message_list == null ? 0 : message_list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        MessageData msg = message_list.get(i);
        viewHolder.tv_name.setText(msg.getName());

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public LinearLayout header_click;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            header_click = (LinearLayout) itemView.findViewById(R.id.header_click);

        }


    }

}

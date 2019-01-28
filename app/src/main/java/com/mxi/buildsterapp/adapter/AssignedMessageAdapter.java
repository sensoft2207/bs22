package com.mxi.buildsterapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.activity.ChatActivity;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.model.MessageData;
import com.mxi.buildsterapp.model.MyProjectMessageData;

import java.util.ArrayList;
import java.util.List;

public class AssignedMessageAdapter extends RecyclerView.Adapter<AssignedMessageAdapter.ViewHolder> {

    static CommanClass cc;

    private List<MyProjectMessageData> message_list;
    private int rowLayout;
    public static Context mContext;

    Activity activity;


    public AssignedMessageAdapter(List<MyProjectMessageData> message_list, int rowLayout, Context context) {

        cc = new CommanClass(context);

        this.message_list = message_list;
        this.rowLayout = rowLayout;
        this.mContext = context;

        activity = (Activity) mContext;

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
        MyProjectMessageData msg = message_list.get(i);
        viewHolder.tv_name.setText(msg.getSender_name());

        if (msg.getUnread_count().equals("0")){

            viewHolder.ln_message_read.setVisibility(View.GONE);

        }else {
            viewHolder.ln_message_read.setVisibility(View.VISIBLE);
        }

        if (msg.getText().equals("")){
            viewHolder.tv_last_message.setText("Send message");
            viewHolder.tv_last_message.setTextColor(Color.parseColor("#006CA6"));
        }else {
            viewHolder.tv_last_message.setText(msg.getText());
        }

        viewHolder.tv_date.setText(msg.getCreated_datetime());

        Glide.with(mContext).load(msg.getSender_image()).into(viewHolder.iv_worker_pic);

        viewHolder.header_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getMessageIDs(viewHolder.getAdapterPosition());


            }
        });

    }

    private void getMessageIDs(int adapterPosition) {

        MyProjectMessageData md = message_list.get(adapterPosition);

        cc.savePrefString("username",md.getSender_name());
        cc.savePrefString("user_profilepic",md.getSender_image());
        cc.savePrefString("from_user_id",md.getFrom_user_id());
        cc.savePrefString("to_user_id",md.getId());


        Intent intentViewMyProject = new Intent(mContext, ChatActivity.class);
        intentViewMyProject.putExtra("pro_type","assigned");
        mContext.startActivity(intentViewMyProject);
        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_last_message,tv_date;
        ImageView iv_worker_pic;
        LinearLayout header_click,ln_message_read;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_last_message = (TextView) itemView.findViewById(R.id.tv_last_message);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);

            header_click = (LinearLayout) itemView.findViewById(R.id.header_click);
            ln_message_read = (LinearLayout) itemView.findViewById(R.id.ln_message_read);

            iv_worker_pic = (ImageView) itemView.findViewById(R.id.iv_worker_pic);

        }


    }

    public void filterList(ArrayList<MyProjectMessageData> message_list_filtered) {
        this.message_list = message_list_filtered;
        notifyDataSetChanged();
    }

}


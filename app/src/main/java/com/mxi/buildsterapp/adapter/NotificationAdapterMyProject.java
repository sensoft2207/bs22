package com.mxi.buildsterapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.mxi.buildsterapp.activity.MyProjectActionItemDetailUpdate;
import com.mxi.buildsterapp.activity.WebViewActivity;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.model.NotificationData;

import java.util.List;

public class NotificationAdapterMyProject extends RecyclerView.Adapter<NotificationAdapterMyProject.ViewHolder> {

    static CommanClass cc;

    private List<NotificationData> notification_list;
    private int rowLayout;
    public static Context mContext;

    Activity activity;

    public NotificationAdapterMyProject(List<NotificationData> notification_list, int rowLayout, Context context) {

        cc = new CommanClass(context);

        this.notification_list = notification_list;
        this.rowLayout = rowLayout;
        this.mContext = context;

        activity = (Activity) mContext;

    }

    @Override
    public int getItemCount() {
        return notification_list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        NotificationData aItem = notification_list.get(i);

        viewHolder.tv_description.setText(aItem.getTitle());
        viewHolder.tv_floor_title.setText(aItem.getFloor_title());
        viewHolder.tv_location.setText(aItem.getLocation());
        viewHolder.tv_def_dec.setText("no");
        viewHolder.tv_sender_name.setText(aItem.getSender_name());
        viewHolder.tv_def_dec.setText(aItem.getDescription());

        Glide.with(mContext).load(aItem.getScreen_image()).into(viewHolder.iv_project_images);

        if (aItem.getStatus().equals("Unread")){

            viewHolder.ln_message_read.setVisibility(View.VISIBLE);

        }else {
            viewHolder.ln_message_read.setVisibility(View.GONE);
        }


        viewHolder.header_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActionItemID(viewHolder.getAdapterPosition());
            }
        });

    }

    private void getActionItemID(int adapterPosition) {

        if (!cc.isConnectingToInternet()) {

            cc.showToast(mContext.getString(R.string.no_internet));

        } else {

            NotificationData aData = notification_list.get(adapterPosition);

            cc.savePrefString("action_screen_id",aData.getNotification_id());
            cc.savePrefString("action_def_id",aData.getDef_id());
            cc.savePrefString("titlee",aData.getTitle());

            Intent intentViewMyProject = new Intent(mContext, MyProjectActionItemDetailUpdate.class);

            intentViewMyProject.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intentViewMyProject);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout header_click, ln_message_read, ln_accepted_task;
        ImageView iv_project_images;

        TextView tv_description, tv_floor_title, tv_location, tv_def_dec, tv_sender_name;

        public ViewHolder(View itemView) {
            super(itemView);


            tv_description = (TextView) itemView.findViewById(R.id.tv_description);
            tv_floor_title = (TextView) itemView.findViewById(R.id.tv_floor_title);
            tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            tv_def_dec = (TextView) itemView.findViewById(R.id.tv_def_dec);
            tv_sender_name = (TextView) itemView.findViewById(R.id.tv_sender_name);

            header_click = (LinearLayout) itemView.findViewById(R.id.header_click);
            ln_message_read = (LinearLayout) itemView.findViewById(R.id.ln_message_read);
            ln_accepted_task = (LinearLayout) itemView.findViewById(R.id.ln_accepted_task);
            iv_project_images = (ImageView) itemView.findViewById(R.id.iv_project_images);

        }


    }


}


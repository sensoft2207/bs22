package com.mxi.buildsterapp.adapter;

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
import com.mxi.buildsterapp.activity.AssignedProjectActionItemUpdate;
import com.mxi.buildsterapp.activity.MyProjectActionItemDetailUpdate;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.model.ActionItemData;

import java.util.ArrayList;
import java.util.List;

public class TradeworkerClickAssignedItemAdapter extends RecyclerView.Adapter<TradeworkerClickAssignedItemAdapter.ViewHolder> {

    static CommanClass cc;

    private List<ActionItemData> screen_list;
    private int rowLayout;
    public static Context mContext;

//    Activity activity;

    public TradeworkerClickAssignedItemAdapter(List<ActionItemData> screen_list, int rowLayout, Context context) {

        cc = new CommanClass(context);

        this.screen_list = screen_list;
        this.rowLayout = rowLayout;
        this.mContext = context;

//        activity = (Activity) mContext;

    }

    @Override
    public int getItemCount() {
        return screen_list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        ActionItemData aItem = screen_list.get(i);
        viewHolder.tv_screen_name.setText(aItem.getFloor_title());
        viewHolder.tv_created_date.setText(aItem.getCreated_datetime());
        viewHolder.tv_def_dec.setText(aItem.getDeficiency_desc());
        viewHolder.tv_workername.setText(aItem.getFirstname()+" "+aItem.getLastname());
        viewHolder.tv_location.setText(aItem.getLocation());

        Glide.with(mContext).load(aItem.getDef_image()).into(viewHolder.iv_worker_pic);

        Glide.with(mContext).load(aItem.getProfile_img()).into(viewHolder.iv_worker_pic_two);
        // Glide.with(mContext).load(R.drawable.student).into(viewHolder.iv_worker_pic);

        if (aItem.getCreated_by().equals("tw_to_self") || aItem.getCreated_by().equals("pm_to_tw") ){

            viewHolder.ln_blue_item_circle.setVisibility(View.VISIBLE);
            viewHolder.ln_green_item_circle.setVisibility(View.GONE);
            viewHolder.ln_purple_item_circle.setVisibility(View.GONE);

        }

        if (aItem.getCreated_by().equals("tw_to_pm")){

            viewHolder.ln_blue_item_circle.setVisibility(View.GONE);
            viewHolder.ln_green_item_circle.setVisibility(View.VISIBLE);
            viewHolder.ln_purple_item_circle.setVisibility(View.GONE);

        }


        if (aItem.getUnread_comment().equals("0")){
            viewHolder.ln_message_read.setVisibility(View.GONE);


        }else {
            viewHolder.ln_message_read.setVisibility(View.VISIBLE);
        }

        if (aItem.getStatus_item().equals("approve")){

            viewHolder.ln_accepted_task.setVisibility(View.VISIBLE);
            viewHolder.ln_blue_item_circle.setVisibility(View.GONE);
            viewHolder.ln_green_item_circle.setVisibility(View.GONE);
            viewHolder.ln_purple_item_circle.setVisibility(View.VISIBLE);

        }else {

            viewHolder.ln_accepted_task.setVisibility(View.GONE);
        }

        viewHolder.header_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActionItemID(viewHolder.getAdapterPosition());
            }
        });

    }

    private void getActionItemID(int adapterPosition) {

        if (!cc.isConnectingToInternet()){

            cc.showToast(mContext.getString(R.string.no_internet));

        }else {

            ActionItemData aData = screen_list.get(adapterPosition);

            cc.savePrefString("action_screen_name",aData.getFloor_title());
            cc.savePrefString("action_a_screen_id",aData.getFloor_id());
            cc.savePrefString("action_a_def_id",aData.getId());
            cc.savePrefString("action_a_tradeworker_id",aData.getTradeworker_id());
            cc.savePrefString("action_a_location",aData.getLocation());
            cc.savePrefString("action_a_date",aData.getCreated_datetime());
            cc.savePrefString("title","");

//            cc.savePrefString("action_profileimg",aData.getProfile_img());
//            cc.savePrefString("action_def_dec",aData.getDeficiency_desc());
//            cc.savePrefString("action_tradeworker_name",aData.getFirstname()+" "+aData.getLastname());

            Intent intentViewMyProject = new Intent(mContext, AssignedProjectActionItemUpdate.class);
            intentViewMyProject.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intentViewMyProject);
//            activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_screen_name,tv_created_date,tv_workername,tv_def_dec,tv_location;
        public LinearLayout header_click,ln_message_read,ln_accepted_task;
        ImageView iv_worker_pic,iv_worker_pic_two;

        LinearLayout ln_green_item_circle,ln_purple_item_circle,ln_blue_item_circle;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_screen_name = (TextView) itemView.findViewById(R.id.tv_screen_name);
            tv_created_date = (TextView) itemView.findViewById(R.id.tv_created_date);
            tv_workername = (TextView) itemView.findViewById(R.id.tv_workername);
            tv_location = (TextView) itemView.findViewById(R.id.tv_location);
            tv_def_dec = (TextView) itemView.findViewById(R.id.tv_def_dec);
            header_click = (LinearLayout) itemView.findViewById(R.id.header_click);
            ln_message_read = (LinearLayout) itemView.findViewById(R.id.ln_message_read);
            ln_accepted_task = (LinearLayout) itemView.findViewById(R.id.ln_accepted_task);
            iv_worker_pic = (ImageView) itemView.findViewById(R.id.iv_worker_pic);
            iv_worker_pic_two = (ImageView) itemView.findViewById(R.id.iv_worker_pic_two);

            ln_green_item_circle = (LinearLayout) itemView.findViewById(R.id.ln_green_item_circle);
            ln_purple_item_circle = (LinearLayout) itemView.findViewById(R.id.ln_purple_item_circle);
            ln_blue_item_circle = (LinearLayout) itemView.findViewById(R.id.ln_blue_item_circle);

        }


    }

    public void filterList(ArrayList<ActionItemData> screen_list_filtered) {
        this.screen_list = screen_list_filtered;
        notifyDataSetChanged();
    }

}

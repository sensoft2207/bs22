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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.activity.ViewPagerAssignedProject;
import com.mxi.buildsterapp.activity.ViewPagerMyProject;
import com.mxi.buildsterapp.activity.WebViewActivity;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.model.MyProjectData;
import com.mxi.buildsterapp.model.MyProjectScreen;

import java.util.ArrayList;
import java.util.List;

public class AssignedProjectAdapter extends RecyclerView.Adapter<AssignedProjectAdapter.ViewHolder> {

    CommanClass cc;

    private List<MyProjectData> projectList;
    private int rowLayout;
    public static Context mContext;

    Activity activity;




    public AssignedProjectAdapter(List<MyProjectData> projectList, int rowLayout, Context context) {


        this.projectList = projectList;
        this.rowLayout = rowLayout;
        this.mContext = context;

        activity = (Activity) mContext;

        cc = new CommanClass(context);

    }

    @Override
    public int getItemCount() {
        return projectList == null ? 0 : projectList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        MyProjectData myItem = projectList.get(i);
        viewHolder.tv_project_name.setText(myItem.getProject_name());

        Glide.with(mContext).load(myItem.getProject_image()).
                listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        viewHolder.progressBar.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        viewHolder.progressBar.setVisibility(View.GONE);

                        return false;
                    }
                }).into(viewHolder.iv_back);


       /* if (myItem.getDef().equals("0")){

            viewHolder.ln_project_def.setVisibility(View.INVISIBLE);
        }else {

            viewHolder.tv_project_def.setText(myItem.getDef());
            viewHolder.ln_project_def.setVisibility(View.VISIBLE);
        }

        if (myItem.getMessage_count().equals("0")){

            viewHolder.ln_project_message.setVisibility(View.INVISIBLE);
        }else {

            viewHolder.tv_project_message.setText(myItem.getMessage_count());
            viewHolder.ln_project_message.setVisibility(View.VISIBLE);
        }

        if (myItem.getNew_def().equals("0")){

            viewHolder.ln_project_new_def.setVisibility(View.INVISIBLE);
        }else {

            viewHolder.tv_project_new_def.setText(myItem.getNew_def());
            viewHolder.ln_project_new_def.setVisibility(View.VISIBLE);
        }

*/

        if (myItem.getApproved_count().equals("0")) {

//            viewHolder.ln_awaiting_approval.setVisibility(View.INVISIBLE);
        } else {

            viewHolder.tv_awaiting_approval.setText(myItem.getApproved_count());
            viewHolder.ln_awaiting_approval.setVisibility(View.VISIBLE);
        }

        if (myItem.getDef().equals("0")) {

//            viewHolder.ln_assigned_pins.setVisibility(View.INVISIBLE);
        } else {

            viewHolder.tv_assigned_pins.setText(myItem.getDef());
            viewHolder.ln_assigned_pins.setVisibility(View.VISIBLE);
        }


        if (myItem.getMytask().equals("0")) {

//            viewHolder.ln_my_pins.setVisibility(View.INVISIBLE);
        } else {

            viewHolder.tv_my_pins.setText(myItem.getMytask());
            viewHolder.ln_my_pins.setVisibility(View.VISIBLE);
        }


        if (myItem.getUnread_notification_message_count().equals("0")) {

            viewHolder.ln_unread_notification.setVisibility(View.INVISIBLE);
        } else {

//            viewHolder.tv_unread_notification.setText("");
            viewHolder.ln_unread_notification.setVisibility(View.VISIBLE);
        }


        viewHolder.iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getProjectNameandID(viewHolder.getAdapterPosition());
            }
        });



    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_project_name,tv_project_def,tv_project_message,tv_project_new_def;
        ImageView iv_back;

        LinearLayout ln_project_def,ln_project_message,ln_project_new_def;

        LinearLayout ln_unread_notification,ln_my_pins,ln_assigned_pins,ln_awaiting_approval;
        TextView tv_unread_notification,tv_my_pins,tv_assigned_pins,tv_awaiting_approval;


        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);


            tv_project_name = (TextView) itemView.findViewById(R.id.tv_project_name);
            tv_project_message = (TextView) itemView.findViewById(R.id.tv_project_message);
            tv_project_def = (TextView) itemView.findViewById(R.id.tv_project_def);
            tv_project_new_def = (TextView) itemView.findViewById(R.id.tv_project_new_def);

            tv_unread_notification = (TextView) itemView.findViewById(R.id.tv_unread_notification);
            tv_my_pins = (TextView) itemView.findViewById(R.id.tv_my_pins);
            tv_assigned_pins = (TextView) itemView.findViewById(R.id.tv_assigned_pins);
            tv_awaiting_approval = (TextView) itemView.findViewById(R.id.tv_awaiting_approval);


            iv_back = (ImageView) itemView.findViewById(R.id.iv_back);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);


            ln_project_message = (LinearLayout) itemView.findViewById(R.id.ln_project_message);
            ln_project_def = (LinearLayout) itemView.findViewById(R.id.ln_project_def);
            ln_project_new_def = (LinearLayout) itemView.findViewById(R.id.ln_project_new_def);

            ln_unread_notification = (LinearLayout) itemView.findViewById(R.id.ln_unread_notification);
            ln_my_pins = (LinearLayout) itemView.findViewById(R.id.ln_my_pins);
            ln_assigned_pins = (LinearLayout) itemView.findViewById(R.id.ln_assigned_pins);
            ln_awaiting_approval = (LinearLayout) itemView.findViewById(R.id.ln_awaiting_approval);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }

    private void getProjectNameandID(int adapterPosition) {

        if (!cc.isConnectingToInternet()){

            cc.showToast(mContext.getString(R.string.no_internet));

        }else {

            MyProjectData myItem = projectList.get(adapterPosition);

            cc.savePrefString("project_id_assigned", myItem.getProject_id());
            cc.savePrefString("project_id_assigned2", myItem.getProject_id());
            cc.savePrefString("project_name_assigned", myItem.getProject_name());

            Intent intentViewMyProject = new Intent(mContext, ViewPagerAssignedProject.class);
            mContext.startActivity(intentViewMyProject);
            activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        }

    }

    public void filterList(ArrayList<MyProjectData> project_list_filtered) {
        this.projectList = project_list_filtered;
        notifyDataSetChanged();
    }
}


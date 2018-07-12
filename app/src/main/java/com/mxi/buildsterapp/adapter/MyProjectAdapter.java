package com.mxi.buildsterapp.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.activity.HomeActivity;
import com.mxi.buildsterapp.activity.LoginActivity;
import com.mxi.buildsterapp.activity.ViewPagerMyProject;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.model.MyProjectData;

import java.util.ArrayList;
import java.util.List;

public class MyProjectAdapter extends RecyclerView.Adapter<MyProjectAdapter.ViewHolder>{

    CommanClass cc;

    private List<MyProjectData> projectList;
    private int rowLayout;
    public static Context mContext;

    Activity activity;


    public MyProjectAdapter(List<MyProjectData> projectList, int rowLayout, Context context) {


        this.projectList = projectList;

        this.rowLayout = rowLayout;
        this.mContext = context;

        activity = (Activity) mContext;

        cc = new CommanClass(context);

    }

    @Override
    public int getItemCount() {
        return projectList.size();
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

        viewHolder.ln_change_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        viewHolder.ln_show_trad_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        viewHolder.ln_view_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentViewMyProject = new Intent(mContext, ViewPagerMyProject.class);
                mContext.startActivity(intentViewMyProject);
                activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });

        viewHolder.ln_delete_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteDialog();
            }
        });


        Glide.with(mContext).load(R.drawable.bg_item).into(viewHolder.iv_back);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout ln_change_photo, ln_show_trad_list, ln_view_project, ln_delete_project;
        TextView tv_project_name;
        ImageView iv_back;

        public ViewHolder(View itemView) {
            super(itemView);


            tv_project_name = (TextView) itemView.findViewById(R.id.tv_project_name);

            iv_back = (ImageView) itemView.findViewById(R.id.iv_back);

            ln_change_photo = (LinearLayout) itemView.findViewById(R.id.ln_change_photo);
            ln_show_trad_list = (LinearLayout) itemView.findViewById(R.id.ln_show_trad_list);
            ln_view_project = (LinearLayout) itemView.findViewById(R.id.ln_view_project);
            ln_delete_project = (LinearLayout) itemView.findViewById(R.id.ln_delete_project);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }

    public void filterList(ArrayList<MyProjectData> project_list_filtered) {
        this.projectList = project_list_filtered;
        notifyDataSetChanged();
    }


    private void deleteDialog() {

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.delete_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        TextView tv_info = (TextView) dialog.findViewById(R.id.tv_info);
        Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
        Button btn_no = (Button) dialog.findViewById(R.id.btn_no);

        tv_info.setText(mContext.getString(R.string.delete_project));

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });



        dialog.show();

    }
}


package com.mxi.buildsterapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.activity.PinDragDropActivity;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.model.MyProjectData;

import java.util.List;

public class ViewMyProjectAdapter extends RecyclerView.Adapter<ViewMyProjectAdapter.ViewHolder> {

    CommanClass cc;

    private List<MyProjectData> projectList;
    private int rowLayout;
    public static Context mContext;



    public ViewMyProjectAdapter(List<MyProjectData> projectList, int rowLayout, Context context) {


        this.projectList = projectList;
        this.rowLayout = rowLayout;
        this.mContext = context;


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

        viewHolder.ln_view_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("ViewScreen", "........");

                Intent intentPin = new Intent(mContext, PinDragDropActivity.class);
                intentPin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intentPin);



            }
        });

        viewHolder.ln_delete_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("DeleteScreen", "........");
            }
        });

        Glide.with(mContext).load(R.drawable.bg_item).into(viewHolder.iv_back);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_project_name, yv;
        ImageView iv_back;
        LinearLayout ln_view_screen, ln_delete_screen;

        public ViewHolder(View itemView) {
            super(itemView);


            tv_project_name = (TextView) itemView.findViewById(R.id.tv_project_name);
            iv_back = (ImageView) itemView.findViewById(R.id.iv_back);

            ln_view_screen = (LinearLayout) itemView.findViewById(R.id.ln_view_screen);
            ln_delete_screen = (LinearLayout) itemView.findViewById(R.id.ln_delete_screen);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }
}


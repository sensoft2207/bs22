package com.mxi.buildsterapp.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.model.MyProjectData;

import java.util.ArrayList;
import java.util.List;

public class AssignedProjectAdapter extends RecyclerView.Adapter<AssignedProjectAdapter.ViewHolder> {

    CommanClass cc;

    private List<MyProjectData> projectList;
    private int rowLayout;
    public static Context mContext;


    public AssignedProjectAdapter(List<MyProjectData> projectList, int rowLayout, Context context) {


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

        Glide.with(mContext).load(R.drawable.bg_item).into(viewHolder.iv_back);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_project_name;
        ImageView iv_back;

        public ViewHolder(View itemView) {
            super(itemView);


            tv_project_name = (TextView) itemView.findViewById(R.id.tv_project_name);
            iv_back = (ImageView) itemView.findViewById(R.id.iv_back);


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
}


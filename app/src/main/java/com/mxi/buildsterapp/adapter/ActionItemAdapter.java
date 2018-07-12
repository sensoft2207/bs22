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
import com.mxi.buildsterapp.model.MyProjectData;


import java.util.ArrayList;
import java.util.List;

public class ActionItemAdapter extends RecyclerView.Adapter<ActionItemAdapter.ViewHolder> {

    static CommanClass cc;

    private List<ActionItemData> screen_list;
    private int rowLayout;
    public static Context mContext;


    public ActionItemAdapter(List<ActionItemData> screen_list, int rowLayout, Context context) {

        cc = new CommanClass(context);

        this.screen_list = screen_list;
        this.rowLayout = rowLayout;
        this.mContext = context;

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
        viewHolder.tv_screen_name.setText(aItem.getScreen_name());

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_screen_name;
        public LinearLayout header_click;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_screen_name = (TextView) itemView.findViewById(R.id.tv_screen_name);
            header_click = (LinearLayout) itemView.findViewById(R.id.header_click);

        }


    }

    public void filterList(ArrayList<ActionItemData> screen_list_filtered) {
        this.screen_list = screen_list_filtered;
        notifyDataSetChanged();
    }

}


package com.mxi.buildsterapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
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
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.model.CommentData;
import com.mxi.buildsterapp.model.Country;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    static CommanClass cc;

    private List<CommentData> comment_list;
    private int rowLayout;
    public static Context mContext;


    public CommentAdapter(List<CommentData> comment_list, int rowLayout, Context context) {

        cc = new CommanClass(context);

        this.comment_list = comment_list;
        this.rowLayout = rowLayout;
        this.mContext = context;

    }

    @Override
    public int getItemCount() {
        return comment_list == null ? 0 : comment_list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        CommentData cData = comment_list.get(i);


        viewHolder.tv_name.setText(cData.getUser_name());
        viewHolder.tv_comment.setText(cData.getComment());

        Glide.with(mContext).load(cData.getUser_image()).
                listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        viewHolder.progress_user.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        viewHolder.progress_user.setVisibility(View.GONE);

                        return false;
                    }
                }).into(viewHolder.iv_user_pic);


    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_comment;
        LinearLayout header_click,header_two;
        ImageView iv_user_pic;
        ProgressBar progress_user;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);
            iv_user_pic = (ImageView) itemView.findViewById(R.id.iv_user_pic);
            header_click = (LinearLayout) itemView.findViewById(R.id.header_click);
            header_two = (LinearLayout) itemView.findViewById(R.id.header_two);
            progress_user = (ProgressBar) itemView.findViewById(R.id.progress_user);


        }


    }

}

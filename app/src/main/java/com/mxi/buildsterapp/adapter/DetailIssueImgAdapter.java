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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.activity.ActionDetailIssueImgSlider;
import com.mxi.buildsterapp.activity.ActionDetailIssueImgSliderTwo;
import com.mxi.buildsterapp.activity.MyProjectActionItemDetailUpdate;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.model.CommentData;
import com.mxi.buildsterapp.model.KidTaskData;

import java.util.List;

public class DetailIssueImgAdapter extends RecyclerView.Adapter<DetailIssueImgAdapter.ViewHolder> {

    static CommanClass cc;

    private List<KidTaskData> task_img_list;
    private int rowLayout;
    public static Context mContext;

    Activity activity;


    public DetailIssueImgAdapter(List<KidTaskData> task_img_list, int rowLayout, Context context) {

        cc = new CommanClass(context);

        this.task_img_list = task_img_list;
        this.rowLayout = rowLayout;
        this.mContext = context;

        activity = (Activity) mContext;

    }

    @Override
    public int getItemCount() {
        return task_img_list == null ? 0 : task_img_list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        final KidTaskData cData = task_img_list.get(i);


        Glide.with(mContext).load(cData.getTaskImage()).
                listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        viewHolder.progress_screen_img.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        viewHolder.progress_screen_img.setVisibility(View.GONE);

                        return false;
                    }
                }).into(viewHolder.iv_screen_isuue_img);

        viewHolder.header_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cData.getFrom_slider().equals("myproject")){

                    Intent intentViewMyProject = new Intent(mContext, ActionDetailIssueImgSlider.class);
                    intentViewMyProject.putExtra("from","myproject");
                    mContext.startActivity(intentViewMyProject);
                    activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                    Log.e("@@From","myproject");

                }else {

                    Intent intentViewMyProject = new Intent(mContext, ActionDetailIssueImgSliderTwo.class);
                    intentViewMyProject.putExtra("from","myproject");
                    mContext.startActivity(intentViewMyProject);
                    activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                    Log.e("@@From","assignedproject");
                }

            }
        });

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout header_click;
        ImageView iv_screen_isuue_img;
        ProgressBar progress_screen_img;

        public ViewHolder(View itemView) {
            super(itemView);


            iv_screen_isuue_img = (ImageView) itemView.findViewById(R.id.iv_screen_isuue_img);
            header_click = (LinearLayout) itemView.findViewById(R.id.header_click);
            progress_screen_img = (ProgressBar) itemView.findViewById(R.id.progress_screen_img);


        }


    }

}

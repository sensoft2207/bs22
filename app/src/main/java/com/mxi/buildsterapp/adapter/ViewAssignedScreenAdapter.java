package com.mxi.buildsterapp.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.activity.PinDragDropActivity;
import com.mxi.buildsterapp.activity.WebViewActivity;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.MyProjectScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//
//import static com.mxi.buildsterapp.fragment.HomeScreenFragmentUpdate.ln_no_screen_center;
//import static com.mxi.buildsterapp.fragment.HomeScreenFragmentUpdate.rc_project;


public class ViewAssignedScreenAdapter extends RecyclerView.Adapter<ViewAssignedScreenAdapter.ViewHolder> {

    CommanClass cc;

    private List<MyProjectScreen> screenList;
    private int rowLayout;
    public static Context mContext;

    Dialog dialog,dialog2;

    Activity activity;

    String URL,def_id,user_id;



    public ViewAssignedScreenAdapter(List<MyProjectScreen> screenList, int rowLayout, Context context) {


        this.screenList = screenList;
        this.rowLayout = rowLayout;
        this.mContext = context;

        activity = (Activity) mContext;

        cc = new CommanClass(context);

    }

    @Override
    public int getItemCount() {
        return screenList == null ? 0 : screenList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        MyProjectScreen myItem = screenList.get(i);

        /*if (myItem.getDef_pending().equals("0")) {

            viewHolder.ln_def_pending.setVisibility(View.INVISIBLE);
        } else {

            viewHolder.tv_def_pending.setText(myItem.getDef_pending());
            viewHolder.ln_def_pending.setVisibility(View.VISIBLE);
        }

        if (myItem.getNew_def().equals("0")) {

            viewHolder.ln_project_new_def.setVisibility(View.INVISIBLE);
        } else {

            viewHolder.tv_project_new_def.setText(myItem.getDef_pending());
            viewHolder.ln_project_new_def.setVisibility(View.VISIBLE);
        }

        if (myItem.getUnread_comment_count().equals("0")) {

            viewHolder.ln_project_message.setVisibility(View.INVISIBLE);
        } else {

            viewHolder.tv_project_message.setText(myItem.getDef_pending());
            viewHolder.ln_project_message.setVisibility(View.VISIBLE);
        }
*/
        viewHolder.tv_project_name.setText(myItem.getScreen_name());
        viewHolder.tv_my_pins.setText(myItem.getMytasks_blue());
        viewHolder.tv_assigned_pins.setText(myItem.getSent_task_green());
        viewHolder.tv_awaiting_pins.setText(myItem.getDef_completed_purple());


        viewHolder.ln_view_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getScreenViewID(viewHolder.getAdapterPosition());
            }
        });


        Glide.with(mContext).load(myItem.getScreen_image()).
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
    }

    private void getScreenViewID(int adapterPosition) {

        if (!cc.isConnectingToInternet()){

            cc.showToast(mContext.getString(R.string.no_internet));

        }else {

            MyProjectScreen myItem = screenList.get(adapterPosition);

            cc.savePrefString("screen_name_main",myItem.getScreen_name());
            cc.savePrefString("issue_img_main",myItem.getScreen_image());

            def_id = myItem.getScreen_id();
            user_id = cc.loadPrefString("user_id");

            URL = "http://mbdbtechnology.com/projects/buildster/Floorview/show/"+def_id+"/Included_project/"+user_id;

            Intent intentPin = new Intent(mContext, WebViewActivity.class);
            intentPin.putExtra("screen_type","assignproject");
            intentPin.putExtra("def_idd",def_id);
            intentPin.putExtra("url",URL);
            intentPin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intentPin);
            activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_project_name,tv_def_pending,tv_project_new_def,tv_project_message;
        ImageView iv_back;
        LinearLayout ln_view_screen,ln_def_pending,ln_project_new_def,ln_project_message;

        ProgressBar progressBar;

        TextView tv_my_pins,tv_assigned_pins,tv_awaiting_pins;

        public ViewHolder(View itemView) {
            super(itemView);


            tv_def_pending = (TextView) itemView.findViewById(R.id.tv_def_pending);
            tv_project_new_def = (TextView) itemView.findViewById(R.id.tv_project_new_def);
            tv_project_message = (TextView) itemView.findViewById(R.id.tv_project_message);

            tv_my_pins = (TextView) itemView.findViewById(R.id.tv_my_pins);
            tv_assigned_pins = (TextView) itemView.findViewById(R.id.tv_assigned_pins);
            tv_awaiting_pins = (TextView) itemView.findViewById(R.id.tv_awaiting_pins);


            tv_project_name = (TextView) itemView.findViewById(R.id.tv_project_name);
            iv_back = (ImageView) itemView.findViewById(R.id.iv_back);

            ln_view_screen = (LinearLayout) itemView.findViewById(R.id.ln_view_screen);

            ln_def_pending = (LinearLayout) itemView.findViewById(R.id.ln_def_pending);
            ln_project_new_def = (LinearLayout) itemView.findViewById(R.id.ln_project_new_def);
            ln_project_message = (LinearLayout) itemView.findViewById(R.id.ln_project_message);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }

    private void visibleCreate() {

        if (screenList.size() == 0) {

//            ln_no_screen_center.setVisibility(View.VISIBLE);
//            rc_project.setVisibility(View.INVISIBLE);


        } else {

//            ln_no_screen_center.setVisibility(View.INVISIBLE);
//            rc_project.setVisibility(View.VISIBLE);

        }
    }

    public void filterList(ArrayList<MyProjectScreen> screen_list_filtered) {
        this.screenList = screen_list_filtered;
        notifyDataSetChanged();
    }
}


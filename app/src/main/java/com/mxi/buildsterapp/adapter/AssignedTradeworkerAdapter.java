package com.mxi.buildsterapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.activity.TradeWorkerClickActionIemManager;
import com.mxi.buildsterapp.model.TradWorkerData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssignedTradeworkerAdapter extends RecyclerView.Adapter<AssignedTradeworkerAdapter.MyViewHolder> {


    List<TradWorkerData> workerList = Collections.emptyList();
    Context context;


    public AssignedTradeworkerAdapter(List<TradWorkerData> workerList, Context context) {

        this.workerList = workerList;
        this.context = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ln_trade_green_count, ln_trade_purple_count;
        TextView tv_trade_green_count, tv_trade_purple_count;


        ImageView iv_worker_pic;
        TextView tv_worker_name;
        ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);


            iv_worker_pic = (ImageView) view.findViewById(R.id.iv_worker_pic);
            tv_worker_name = (TextView) view.findViewById(R.id.tv_worker_name);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);

            ln_trade_green_count = (LinearLayout) view.findViewById(R.id.ln_trade_green_count);
            ln_trade_purple_count = (LinearLayout) view.findViewById(R.id.ln_trade_purple_count);


            tv_trade_green_count = (TextView) view.findViewById(R.id.tv_trade_green_count);
            tv_trade_purple_count = (TextView) view.findViewById(R.id.tv_trade_purple_count);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_tradeworker_item_assigned, parent, false);

        itemView.getLayoutParams().width = (int) (getScreenWidth() / 4); /// THIS LINE WILL DIVIDE OUR VIEW INTO NUMBERS OF PARTS

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        TradWorkerData td = workerList.get(position);

        if (td.getFullnameIs().equals("yes")) {

            /*if (td.getFullnameIsEdited().equals("yes")){
                holder.tv_worker_name.setText(td.getFullname());
            }else {
                holder.tv_worker_name.setText(td.getFirstname()+" "+td.getLastname());
            }*/

            holder.tv_worker_name.setText(td.getFirstname() + " " + td.getLastname());

            holder.ln_trade_green_count.setVisibility(View.VISIBLE);
            holder.ln_trade_purple_count.setVisibility(View.VISIBLE);


            holder.tv_trade_green_count.setText(td.getBlue());
            holder.tv_trade_purple_count.setText(td.getPurple());
            holder.tv_trade_green_count.setTextColor(Color.parseColor("#00AFFB"));
            holder.tv_trade_purple_count.setTextColor(Color.parseColor("#B900F9"));

            Glide.with(context).load(td.getProfile_image()).
                    listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                            holder.progressBar.setVisibility(View.GONE);

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                            holder.progressBar.setVisibility(View.GONE);

                            return false;
                        }
                    }).into(holder.iv_worker_pic);

            holder.iv_worker_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    getWorkerNameID2(holder.getAdapterPosition());


                }
            });

        } else {
            holder.tv_worker_name.setText(td.getFirstname() + " " + td.getLastname());

            holder.ln_trade_green_count.setVisibility(View.VISIBLE);
            holder.ln_trade_purple_count.setVisibility(View.GONE);


            holder.tv_trade_green_count.setText(td.getGreen());

            Glide.with(context).load(td.getProfile_image()).
                    listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                            holder.progressBar.setVisibility(View.GONE);

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                            holder.progressBar.setVisibility(View.GONE);

                            return false;
                        }
                    }).into(holder.iv_worker_pic);

            holder.iv_worker_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    getWorkerNameID(holder.getAdapterPosition());


                }
            });
        }


    }

    private void getWorkerNameID2(int adapterPosition) {

        TradWorkerData td = workerList.get(adapterPosition);


        Intent intentInvite = new Intent(context, TradeWorkerClickActionIemManager.class);
        intentInvite.putExtra("tradeworker_id", td.getId());
        intentInvite.putExtra("tradeworker_image", td.getProfile_image());
        intentInvite.putExtra("tradeworker_green", td.getBlue());
        intentInvite.putExtra("tradeworker_blue", td.getPurple());
        intentInvite.putExtra("man", "1");
        intentInvite.putExtra("user_type", "pm");
        intentInvite.putExtra("tradeworker_name", td.getFirstname() + " " + td.getLastname());
        context.startActivity(intentInvite);
    }


    @Override
    public int getItemCount() {
        return workerList.size();
    }

    private void getWorkerNameID(int adapterPosition) {

        TradWorkerData td = workerList.get(adapterPosition);


        Intent intentInvite = new Intent(context, TradeWorkerClickActionIemManager.class);
        intentInvite.putExtra("tradeworker_id", td.getId());
        intentInvite.putExtra("tradeworker_image", td.getProfile_image());
        intentInvite.putExtra("tradeworker_green", td.getGreen());
        intentInvite.putExtra("tradeworker_blue", td.getPurple());
        intentInvite.putExtra("man", "2");
        intentInvite.putExtra("user_type", "td");
        intentInvite.putExtra("tradeworker_name", td.getFirstname() + " " + td.getLastname());
        context.startActivity(intentInvite);


    }


    public int getScreenWidth() {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x;
    }

    public void filterList(ArrayList<TradWorkerData> worker_list_filtered) {
        this.workerList = worker_list_filtered;
        notifyDataSetChanged();
    }
}

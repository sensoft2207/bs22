package com.mxi.buildsterapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.activity.InviteTradeworkerTwo;
import com.mxi.buildsterapp.activity.TradeworkerClickActionItem;
import com.mxi.buildsterapp.model.TradWorkerData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TradWorkerAdapterReassign extends RecyclerView.Adapter<TradWorkerAdapterReassign.MyViewHolder> {


    List<TradWorkerData> workerList = Collections.emptyList();
    Context context;
//    Activity activity;


    public TradWorkerAdapterReassign(List<TradWorkerData> workerList, Context contextt) {

        this.workerList = workerList;
        this.context = contextt;

//        activity = (Activity) context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ln_trade_green_count,ln_trade_purple_count,ln_trade_grid;
        TextView tv_trade_green_count,tv_trade_purple_count;

        CircleImageView iv_worker_pic;
        TextView tv_worker_name;
        ProgressBar progressBar;
        public MyViewHolder(View view) {
            super(view);


            iv_worker_pic =(CircleImageView) view.findViewById(R.id.iv_worker_pic);
            tv_worker_name =(TextView) view.findViewById(R.id.tv_worker_name);
            progressBar =(ProgressBar) view.findViewById(R.id.progress);

            ln_trade_green_count =(LinearLayout) view.findViewById(R.id.ln_trade_green_count);
            ln_trade_purple_count =(LinearLayout) view.findViewById(R.id.ln_trade_purple_count);
            ln_trade_grid =(LinearLayout) view.findViewById(R.id.ln_trade_grid);

            tv_trade_green_count =(TextView) view.findViewById(R.id.tv_trade_green_count);
            tv_trade_purple_count =(TextView) view.findViewById(R.id.tv_trade_purple_count);
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_tradworker_item, parent, false);


//        itemView.getLayoutParams().width = (int) (getScreenWidth() / 4); /// THIS LINE WILL DIVIDE OUR VIEW INTO NUMBERS OF PARTS

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        TradWorkerData td = workerList.get(position);

        if (td.getIdAdd().equals("000")){


            holder.ln_trade_green_count.setVisibility(View.GONE);
            holder.ln_trade_purple_count.setVisibility(View.GONE);

            holder.tv_worker_name.setText(td.getFirstname()+" "+td.getLastname());

            if (td.getIsSettingProfile().equals("no")){


                holder.ln_trade_green_count.setVisibility(View.VISIBLE);

                holder.ln_trade_purple_count.setVisibility(View.VISIBLE);

                holder.tv_trade_green_count.setText(td.getGreen());
                holder.tv_trade_purple_count.setText("0");
                holder.tv_trade_green_count.setTextColor(Color.parseColor("#00AFFB"));

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


                        getWorkerNameIDManager(holder.getAdapterPosition());


                    }
                });


            }else {

                holder.ln_trade_grid.setVisibility(View.GONE);

            }




        }else {

            holder.ln_trade_green_count.setVisibility(View.VISIBLE);
            holder.ln_trade_purple_count.setVisibility(View.VISIBLE);

            holder.tv_trade_green_count.setText(td.getGreen());
            holder.tv_trade_purple_count.setText(td.getPurple());

            holder.tv_worker_name.setText(td.getProfile_name());


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

//            holder.iv_worker_pic.setBorderColor(Color.parseColor("#"+td.getColor_code()));

            holder.iv_worker_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    getWorkerNameID(holder.getAdapterPosition());


                }
            });


        }


        holder.tv_worker_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("POSSSS", String.valueOf(holder.getAdapterPosition()));
            }
        });


    }

    private void getWorkerNameID(int adapterPosition) {

        TradWorkerData td = workerList.get(adapterPosition);

        Intent intent = new Intent("reassign_data");
        // You can also include some extra data.
        intent.putExtra("tradeworker_id", td.getId());
        intent.putExtra("tradeworker_image",td.getProfile_image());
        intent.putExtra("tradeworker_name",td.getFirstname()+" "+td.getLastname());
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);


       /* Intent intentInvite = new Intent(context, TradeworkerClickActionItem.class);
        intentInvite.putExtra("tradeworker_id",td.getId());
        intentInvite.putExtra("tradeworker_image",td.getProfile_image());
        intentInvite.putExtra("tradeworker_green",td.getGreen());
        intentInvite.putExtra("tradeworker_blue",td.getPurple());
        intentInvite.putExtra("man","3");
        intentInvite.putExtra("tradeworker_name",td.getFirstname()+" "+td.getLastname());
        intentInvite.putExtra("user_type","td");
        context.startActivity(intentInvite);*/
//        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);


    }

    private void getWorkerNameIDManager(int adapterPosition) {

        TradWorkerData td = workerList.get(adapterPosition);

        Intent intent = new Intent("reassign_data");
        // You can also include some extra data.
        intent.putExtra("tradeworker_id", td.getId());
        intent.putExtra("tradeworker_image",td.getProfile_image());
        intent.putExtra("tradeworker_name",td.getFirstname()+" "+td.getLastname());
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

      /*  Intent intentInvite = new Intent(context, TradeworkerClickActionItem.class);
        intentInvite.putExtra("tradeworker_id",td.getId());
        intentInvite.putExtra("tradeworker_image",td.getProfile_image());
        intentInvite.putExtra("tradeworker_green",td.getGreen());
        intentInvite.putExtra("tradeworker_blue","0");
        intentInvite.putExtra("man","3");
        intentInvite.putExtra("tradeworker_name",td.getFirstname()+" "+td.getLastname());
        intentInvite.putExtra("user_type","pm");
        context.startActivity(intentInvite);*/
//        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);


    }


    @Override
    public int getItemCount()
    {
        return workerList.size();
    }


    public int getScreenWidth() {

        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
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

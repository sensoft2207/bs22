package com.mxi.buildsterapp.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.model.TradWorkerData;
import com.mxi.buildsterapp.model.TradeWorkerPinData;

import java.util.Collections;
import java.util.List;

public class TradeWorkerPinAdapter extends RecyclerView.Adapter<TradeWorkerPinAdapter.MyViewHolder> {


    List<TradeWorkerPinData> workerList = Collections.emptyList();
    Context context;


    public TradeWorkerPinAdapter(List<TradeWorkerPinData> workerList, Context context) {

        this.workerList = workerList;
        this.context = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_worker_pic,iv_pin;
        TextView tv_worker_name;
        public MyViewHolder(View view) {
            super(view);


            iv_worker_pic =(ImageView) view.findViewById(R.id.iv_worker_pic);
            iv_pin =(ImageView) view.findViewById(R.id.iv_pin);
            tv_worker_name =(TextView) view.findViewById(R.id.tv_worker_name);
        }
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_pin_trade_worker_item, parent, false);

        itemView.getLayoutParams().width = (int) (getScreenWidth() / 5); /// THIS LINE WILL DIVIDE OUR VIEW INTO NUMBERS OF PARTS

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.iv_worker_pic.setImageResource(workerList.get(position).imageId);
        holder.iv_pin.setImageResource(workerList.get(position).imageIdPin);
        holder.tv_worker_name.setText(workerList.get(position).txt);

        holder.iv_worker_pic.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                String list = workerList.get(position).txt.toString();
            }

        });

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
}


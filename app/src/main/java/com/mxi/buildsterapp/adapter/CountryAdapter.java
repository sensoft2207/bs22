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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.model.Country;

import java.util.List;


/**
 * Created by vishal on 27/2/18.
 */

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    static CommanClass cc;

    private List<Country> countries;
    private int rowLayout;
    public static Context mContext;


    public CountryAdapter(List<Country> countries, int rowLayout, Context context) {

        cc = new CommanClass(context);

        this.countries = countries;
        this.rowLayout = rowLayout;
        this.mContext = context;

    }

    @Override
    public int getItemCount() {
        return countries == null ? 0 : countries.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        Country myCountry = countries.get(i);
        viewHolder.tv_country_name.setText(myCountry.getCountry_name());

        viewHolder.header_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCountryDataByPosition(viewHolder.getAdapterPosition());
            }
        });
    }

    private void getCountryDataByPosition(int adapterPosition) {

        Country c_data = countries.get(adapterPosition);

        String country_name = c_data.getCountry_name();
        String country_id = c_data.getId();

        Intent intent = new Intent("custom-event-name");
        intent.putExtra("country_name", country_name);
        intent.putExtra("country_id", country_id);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_country_name;
        public LinearLayout header_click;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_country_name = (TextView) itemView.findViewById(R.id.tv_country_name);
            header_click = (LinearLayout) itemView.findViewById(R.id.header_click);

        }


    }

}


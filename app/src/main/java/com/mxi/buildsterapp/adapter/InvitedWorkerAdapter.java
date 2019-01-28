package com.mxi.buildsterapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.activity.InviteTradeworker;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.Country;
import com.mxi.buildsterapp.model.InvitedWorkerData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvitedWorkerAdapter extends RecyclerView.Adapter<InvitedWorkerAdapter.ViewHolder> {

    static CommanClass cc;

    private List<InvitedWorkerData> invited_list;
    private int rowLayout;
    public static Context mContext;


    public InvitedWorkerAdapter(List<InvitedWorkerData> invited_list, int rowLayout, Context context) {

        cc = new CommanClass(context);

        this.invited_list = invited_list;
        this.rowLayout = rowLayout;
        this.mContext = context;

    }

    @Override
    public int getItemCount() {
        return invited_list == null ? 0 : invited_list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        InvitedWorkerData in = invited_list.get(i);
        viewHolder.tv_w_name.setText(in.getWorker_name());
        viewHolder.tv_w_email.setText(in.getWorker_email());

        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getInviteDataByPosition(viewHolder.getAdapterPosition());
            }
        });
    }

    private void getInviteDataByPosition(int adapterPosition) {

        InvitedWorkerData i_data = invited_list.get(adapterPosition);

        if (!cc.isConnectingToInternet()){

            cc.showToast(mContext.getString(R.string.no_internet));

        }else {

            deleteInvite(i_data.getWorker_id(),adapterPosition);
        }


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_w_name,tv_w_email;
        public LinearLayout header_click;
        ImageView iv_delete;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_w_name = (TextView) itemView.findViewById(R.id.tv_w_name);
            tv_w_email = (TextView) itemView.findViewById(R.id.tv_w_email);
            header_click = (LinearLayout) itemView.findViewById(R.id.header_click);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);

        }


    }

    private void deleteInvite(final String worker_id, final int adapterPosition) {

        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please wait...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.DELETE_INVITED_WORKER,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:invitedelete", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                invited_list.remove(adapterPosition);
                                notifyItemRemoved(adapterPosition);
                                pDialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));

                                /* UpdatedData();*/
                            }
                        } catch (JSONException e) {
                            pDialog.dismiss();
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                cc.showToast(mContext.getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("pro_id"));
                params.put("worker_id", worker_id);

                Log.e("request invite", params.toString());
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", Const.Authorizations.AUTHORIZATION);
                Log.i("request header", headers.toString());
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

}

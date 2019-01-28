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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.mxi.buildsterapp.activity.WebviewActivityUpdate;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.MyProjectData;
import com.mxi.buildsterapp.model.MyProjectScreen;
import com.mxi.buildsterapp.model.TradWorkerData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//import static com.mxi.buildsterapp.fragment.HomeScreenFragmentUpdate.ln_no_screen_center;
//import static com.mxi.buildsterapp.fragment.HomeScreenFragmentUpdate.rc_project;
import static com.mxi.buildsterapp.fragment.MyprojectFragment.ln_create_project_center;
import static com.mxi.buildsterapp.fragment.MyprojectFragment.rc_my_project;

public class ViewMyProjectAdapter extends RecyclerView.Adapter<ViewMyProjectAdapter.ViewHolder> {

    CommanClass cc;

    private List<MyProjectScreen> screenList;
    private int rowLayout;
    public static Context mContext;

    Dialog dialog,dialog2;

    Activity activity;

    String URL,def_id,user_id;


    public ViewMyProjectAdapter(List<MyProjectScreen> screenList, int rowLayout, Context context) {


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

       /* if (myItem.getDef_pending().equals("0")) {

            viewHolder.ln_def_pending.setVisibility(View.INVISIBLE);
        } else {

            viewHolder.tv_def_pending.setText(myItem.getDef_pending());
            viewHolder.ln_def_pending.setVisibility(View.VISIBLE);
        }

        if (myItem.getDef_approved().equals("0")) {

            viewHolder.ln_project_approved.setVisibility(View.INVISIBLE);
        } else {

            viewHolder.tv_project_approved.setText(myItem.getDef_approved());
            viewHolder.ln_project_approved.setVisibility(View.VISIBLE);
        }

        if (myItem.getUnread_total_count().equals("0")) {

            viewHolder.ln_project_message.setVisibility(View.INVISIBLE);
        } else {

            viewHolder.tv_project_message.setText(myItem.getUnread_total_count());
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

        viewHolder.ln_delete_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cc.isConnectingToInternet()){

                    cc.showToast(mContext.getString(R.string.no_internet));

                }else {

                    getScreenIDDelete(viewHolder.getAdapterPosition());

                }
            }
        });

        viewHolder.ln_edit_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cc.isConnectingToInternet()){

                    cc.showToast(mContext.getString(R.string.no_internet));

                }else {

                    getChangeScreenID(viewHolder.getAdapterPosition());
                }

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

            URL = "http://mbdbtechnology.com/projects/buildster/Floorview/show/"+def_id+"/My_project/"+user_id;

            Intent intentPin = new Intent(mContext, WebViewActivity.class);
            intentPin.putExtra("def_idd",def_id);
            intentPin.putExtra("screen_type","myproject");
            intentPin.putExtra("url",URL);
            intentPin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intentPin);
            activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        }
    }

    private void getChangeScreenID(int adapterPosition) {

        MyProjectScreen myItem = screenList.get(adapterPosition);

        changeScreenDialog(myItem.getScreen_id(),myItem.getScreen_name(), adapterPosition);
    }

    private void changeScreenDialog(final String screen_id, String screen_name, final int adapterPosition) {

        dialog2 = new Dialog(mContext);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.edit_screen_name_dialog);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog2.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        final EditText ed_screen_name = (EditText) dialog2.findViewById(R.id.ed_screen_name);
        Button btn_save = (Button) dialog2.findViewById(R.id.btn_save);
        Button btn_cancle = (Button) dialog2.findViewById(R.id.btn_cancle);

        ed_screen_name.setText(screen_name);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String screen_name = ed_screen_name.getText().toString();

                if (!cc.isConnectingToInternet()) {

                    cc.showToast(mContext.getString(R.string.no_internet));

                } else if (screen_name.equals("")){

                    cc.showToast("Please enter screen name");
                }else {

                    changeScreenNameWS(screen_id,screen_name,adapterPosition);
                }

            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog2.dismiss();
            }
        });


        dialog2.show();
    }

    private void changeScreenNameWS(final String screen_id, final String screen_name, int adapterPosition) {

        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please wait...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.CHANGE_SCREEN_NAME,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:change screen", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                pDialog.dismiss();
                                dialog2.dismiss();
                                cc.showToast(jsonObject.getString("message"));
                                sendScreenAlertWS();
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
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("screen_id", screen_id);
                params.put("screen_name", screen_name);

                Log.e("request change screen", params.toString());
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

    private void sendScreenAlertWS() {

        Intent intent = new Intent("alert_screenname_change");
        intent.putExtra("alert", "screen_alert");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    private void getScreenIDDelete(int adapterPosition) {

        MyProjectScreen myItem = screenList.get(adapterPosition);

        deleteDialog(myItem.getScreen_id(), adapterPosition);

    }

    private void deleteDialog(final String screen_id, final int adapterPosition) {

        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.delete_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        TextView tv_info = (TextView) dialog.findViewById(R.id.tv_info);
        Button btn_yes = (Button) dialog.findViewById(R.id.btn_yes);
        Button btn_no = (Button) dialog.findViewById(R.id.btn_no);

        tv_info.setText(mContext.getString(R.string.delete_screen));

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cc.isConnectingToInternet()) {

                    cc.showToast(mContext.getString(R.string.no_internet));

                } else {

                    deleteScreen(screen_id, adapterPosition);
                }

            }
        });

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void deleteScreen(final String screen_id, final int adapterPosition) {

        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please wait...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.DELETE_SCREEN,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:delete screen", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                screenList.remove(adapterPosition);
                                notifyItemRemoved(adapterPosition);
                                pDialog.dismiss();
                                dialog.dismiss();
                                cc.showToast(jsonObject.getString("message"));

                                visibleCreate();

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
                params.put("project_id", cc.loadPrefString("project_id_main"));
                params.put("screen_id", screen_id);

                Log.e("request delete screen", params.toString());
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


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_project_name,tv_def_pending,tv_project_approved,tv_project_message;
        ImageView iv_back;
        LinearLayout ln_view_screen, ln_delete_screen, ln_edit_screen,ln_def_pending,ln_project_approved,ln_project_message;

        ProgressBar progressBar;

        TextView tv_my_pins,tv_assigned_pins,tv_awaiting_pins;

        public ViewHolder(View itemView) {
            super(itemView);


            tv_def_pending = (TextView) itemView.findViewById(R.id.tv_def_pending);
            tv_project_approved = (TextView) itemView.findViewById(R.id.tv_project_approved);
            tv_project_message = (TextView) itemView.findViewById(R.id.tv_project_message);

            tv_my_pins = (TextView) itemView.findViewById(R.id.tv_my_pins);
            tv_assigned_pins = (TextView) itemView.findViewById(R.id.tv_assigned_pins);
            tv_awaiting_pins = (TextView) itemView.findViewById(R.id.tv_awaiting_pins);

            tv_project_name = (TextView) itemView.findViewById(R.id.tv_project_name);
            iv_back = (ImageView) itemView.findViewById(R.id.iv_back);

            ln_view_screen = (LinearLayout) itemView.findViewById(R.id.ln_view_screen);
            ln_delete_screen = (LinearLayout) itemView.findViewById(R.id.ln_delete_screen);
            ln_edit_screen = (LinearLayout) itemView.findViewById(R.id.ln_edit_screen);

            ln_def_pending = (LinearLayout) itemView.findViewById(R.id.ln_def_pending);
            ln_project_approved = (LinearLayout) itemView.findViewById(R.id.ln_project_approved);
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


package com.mxi.buildsterapp.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
import com.mxi.buildsterapp.activity.EditprofileActivity;
import com.mxi.buildsterapp.activity.ViewPagerMyProject;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.Country;
import com.mxi.buildsterapp.model.InvitedWorkerData;
import com.mxi.buildsterapp.model.MyProjectData;
import com.mxi.buildsterapp.utils.AndyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mxi.buildsterapp.fragment.MyprojectFragment.ln_create_project_center;
import static com.mxi.buildsterapp.fragment.MyprojectFragment.rc_my_project;

public class MyProjectAdapter extends RecyclerView.Adapter<MyProjectAdapter.ViewHolder> {

    CommanClass cc;

    private List<MyProjectData> projectList;
    private int rowLayout;
    public static Context mContext;

    Activity activity;

    Dialog dialog;
    Dialog dialog2;
    Dialog dialog3;

    LinearLayout ln_no_record;
    RecyclerView rv_invited_user_list;
    ArrayList<InvitedWorkerData> invited_list;
    ProgressDialog progressDialog;

    ProgressBar progressBar2;

    private CallbackInterface mCallback;

    public interface CallbackInterface {


        void onHandleSelection(String project_id, Dialog dialog_change, ImageView iv_bg_change, Button btn_change);
    }

    public MyProjectAdapter(List<MyProjectData> projectList, int rowLayout, Context context) {


        this.projectList = projectList;

        this.rowLayout = rowLayout;
        this.mContext = context;

        activity = (Activity) mContext;

        cc = new CommanClass(context);

        try {

            mCallback = (CallbackInterface) context;

        } catch (ClassCastException ex) {

        }
    }


    @Override
    public int getItemCount() {
        return projectList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        final MyProjectData myItem = projectList.get(i);
        viewHolder.tv_project_name.setText(myItem.getProject_name());

        Glide.with(mContext).load(myItem.getProject_image()).
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

     /*   if (myItem.getDef().equals("0")) {

            viewHolder.ln_project_def.setVisibility(View.INVISIBLE);
        } else {

            viewHolder.tv_project_def.setText(myItem.getDef());
            viewHolder.ln_project_def.setVisibility(View.VISIBLE);
        }

        if (myItem.getMessage_count().equals("0")) {

            viewHolder.ln_project_message.setVisibility(View.INVISIBLE);
        } else {

            viewHolder.tv_project_message.setText(myItem.getMessage_count());
            viewHolder.ln_project_message.setVisibility(View.VISIBLE);
        }

        if (myItem.getApproved_count().equals("0")) {

            viewHolder.ln_project_approved.setVisibility(View.INVISIBLE);
        } else {

            viewHolder.tv_project_approved.setText(myItem.getApproved_count());
            viewHolder.ln_project_approved.setVisibility(View.VISIBLE);
        }
*/

        if (myItem.getApproved_count().equals("0")) {

//            viewHolder.ln_awaiting_approval.setVisibility(View.INVISIBLE);
        } else {

            viewHolder.tv_awaiting_approval.setText(myItem.getApproved_count());
            viewHolder.ln_awaiting_approval.setVisibility(View.VISIBLE);
        }

        if (myItem.getDef().equals("0")) {

//            viewHolder.ln_assigned_pins.setVisibility(View.INVISIBLE);
        } else {

            viewHolder.tv_assigned_pins.setText(myItem.getDef());
            viewHolder.ln_assigned_pins.setVisibility(View.VISIBLE);
        }


        if (myItem.getMytask().equals("0")) {

//            viewHolder.ln_my_pins.setVisibility(View.INVISIBLE);
        } else {

            viewHolder.tv_my_pins.setText(myItem.getMytask());
            viewHolder.ln_my_pins.setVisibility(View.VISIBLE);
        }


        if (myItem.getUnread_notification_message_count().equals("0")) {

            viewHolder.ln_unread_notification.setVisibility(View.INVISIBLE);
        } else {

//            viewHolder.tv_unread_notification.setText("");
            viewHolder.ln_unread_notification.setVisibility(View.VISIBLE);
        }

        viewHolder.ln_change_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getProjectIDChangePhoto(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.ln_show_trad_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                invitePos(viewHolder.getAdapterPosition());

            }
        });

        viewHolder.ln_view_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getProjectNameandID(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.ln_delete_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getProjectIDDelete(viewHolder.getAdapterPosition());

            }
        });

    }

    private void invitePos(int adapterPosition) {

        if (!cc.isConnectingToInternet()){

            cc.showToast(mContext.getString(R.string.no_internet));

        }else {

            MyProjectData myItem = projectList.get(adapterPosition);

            cc.savePrefString("pro_id", myItem.getProject_id());


            invitedDialog(myItem.getProject_id());
        }
    }


    private void getProjectIDChangePhoto(int adapterPosition) {

        MyProjectData myItem = projectList.get(adapterPosition);

        changePhoto(myItem.getProject_id(), myItem.getProject_image());

    }

    private void changePhoto(String project_id, String project_image) {

        if (!cc.isConnectingToInternet()) {

            cc.showToast(mContext.getString(R.string.no_internet));

        } else {

            changeDialog(project_id, project_image);

        }
    }

    private void changeDialog(final String project_id, String project_image) {

        dialog2 = new Dialog(mContext);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setCancelable(false);
        dialog2.setContentView(R.layout.change_project_photo_dialog);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog2.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        final Button btn_change = (Button) dialog2.findViewById(R.id.btn_change);
        Button btn_close = (Button) dialog2.findViewById(R.id.btn_close);
        LinearLayout ln_change_photo = (LinearLayout) dialog2.findViewById(R.id.ln_change_photo);
        final ImageView iv_bg_change = (ImageView) dialog2.findViewById(R.id.iv_bg_change);
        final ProgressBar progressBar = (ProgressBar) dialog2.findViewById(R.id.progress);

        cc.savePrefBoolean("isNotSelected", true);

        Glide.with(mContext).load(project_image).
                listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        progressBar.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        progressBar.setVisibility(View.GONE);

                        return false;
                    }
                }).into(iv_bg_change);


        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cc.loadPrefBoolean("isNotSelected") == true) {

                    cc.showToast("Please select project photo");

                } else {

                }

            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog2.dismiss();
            }
        });

        ln_change_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCallback != null) {
                    mCallback.onHandleSelection(project_id, dialog2, iv_bg_change, btn_change);
                }

            }
        });


        dialog2.show();


    }

    private void getProjectNameandID(int adapterPosition) {

        if (!cc.isConnectingToInternet()){

            cc.showToast(mContext.getString(R.string.no_internet));

        }else {

            MyProjectData myItem = projectList.get(adapterPosition);

            cc.savePrefString("project_id_main", myItem.getProject_id());
            cc.savePrefString("project_name_main", myItem.getProject_name());
            cc.savePrefString("project_address_main", myItem.getProject_address());

            Intent intentViewMyProject = new Intent(mContext, ViewPagerMyProject.class);
            mContext.startActivity(intentViewMyProject);
            activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        }

    }

    private void getProjectIDDelete(int adapterPosition) {

        if (!cc.isConnectingToInternet()){

            cc.showToast(mContext.getString(R.string.no_internet));

        }else {

            MyProjectData myItem = projectList.get(adapterPosition);

            deleteDialog(myItem.getProject_id(), adapterPosition);

        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout ln_change_photo, ln_show_trad_list, ln_view_project, ln_delete_project, ln_project_message,
                ln_project_def, ln_project_approved;
        TextView tv_project_name, tv_project_message, tv_project_def, tv_project_approved;
        ImageView iv_back;
        ProgressBar progressBar;

        LinearLayout ln_unread_notification,ln_my_pins,ln_assigned_pins,ln_awaiting_approval;
        TextView tv_unread_notification,tv_my_pins,tv_assigned_pins,tv_awaiting_approval;

        public ViewHolder(View itemView) {
            super(itemView);


            tv_project_name = (TextView) itemView.findViewById(R.id.tv_project_name);
            tv_project_message = (TextView) itemView.findViewById(R.id.tv_project_message);
            tv_project_def = (TextView) itemView.findViewById(R.id.tv_project_def);
            tv_project_approved = (TextView) itemView.findViewById(R.id.tv_project_approved);

            tv_unread_notification = (TextView) itemView.findViewById(R.id.tv_unread_notification);
            tv_my_pins = (TextView) itemView.findViewById(R.id.tv_my_pins);
            tv_assigned_pins = (TextView) itemView.findViewById(R.id.tv_assigned_pins);
            tv_awaiting_approval = (TextView) itemView.findViewById(R.id.tv_awaiting_approval);

            iv_back = (ImageView) itemView.findViewById(R.id.iv_back);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);



            ln_change_photo = (LinearLayout) itemView.findViewById(R.id.ln_change_photo);
            ln_show_trad_list = (LinearLayout) itemView.findViewById(R.id.ln_show_trad_list);
            ln_view_project = (LinearLayout) itemView.findViewById(R.id.ln_view_project);
            ln_delete_project = (LinearLayout) itemView.findViewById(R.id.ln_delete_project);
            ln_project_message = (LinearLayout) itemView.findViewById(R.id.ln_project_message);
            ln_project_def = (LinearLayout) itemView.findViewById(R.id.ln_project_def);
            ln_project_approved = (LinearLayout) itemView.findViewById(R.id.ln_project_approved);

            ln_unread_notification = (LinearLayout) itemView.findViewById(R.id.ln_unread_notification);
            ln_my_pins = (LinearLayout) itemView.findViewById(R.id.ln_my_pins);
            ln_assigned_pins = (LinearLayout) itemView.findViewById(R.id.ln_assigned_pins);
            ln_awaiting_approval = (LinearLayout) itemView.findViewById(R.id.ln_awaiting_approval);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

    }

    public void filterList(ArrayList<MyProjectData> project_list_filtered) {
        this.projectList = project_list_filtered;
        notifyDataSetChanged();
    }


    private void deleteDialog(final String project_id, final int adapterPosition) {

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

        tv_info.setText(mContext.getString(R.string.delete_project));

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cc.isConnectingToInternet()) {

                    cc.showToast(mContext.getString(R.string.no_internet));

                } else {

                    deleteProject(project_id, adapterPosition);
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

    private void deleteProject(final String project_id, final int adapterPosition) {

        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please wait...");
        pDialog.show();

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.DELETE_PROJECT,
                new Response.Listener<String>() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onResponse(String response) {
                        Log.e("response:delete pro", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                projectList.remove(adapterPosition);
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
                params.put("project_id", project_id);

                Log.e("request delete project", params.toString());
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

    private void visibleCreate() {

        if (projectList.size() == 0) {

            ln_create_project_center.setVisibility(View.VISIBLE);
            rc_my_project.setVisibility(View.INVISIBLE);


        } else {

            ln_create_project_center.setVisibility(View.INVISIBLE);
            rc_my_project.setVisibility(View.VISIBLE);

        }
    }

    private void invitedDialog(String project_id) {

        dialog3 = new Dialog(mContext);
        dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog3.setCancelable(false);
        dialog3.setContentView(R.layout.invited_tradeworker_dialog);
        dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog3.getWindow().setLayout((6 * width) / 7, ActionBar.LayoutParams.WRAP_CONTENT);


        ImageView iv_close = (ImageView) dialog3.findViewById(R.id.iv_close);

        progressBar2 = (ProgressBar) dialog3.findViewById(R.id.progress);

        ln_no_record = (LinearLayout) dialog3.findViewById(R.id.ln_no_record);

        rv_invited_user_list = (RecyclerView) dialog3.findViewById(R.id.rv_invited_user_list);
        rv_invited_user_list.setLayoutManager(new LinearLayoutManager(mContext));
        rv_invited_user_list.setItemAnimator(new DefaultItemAnimator());


        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog3.dismiss();
            }
        });

        if (!cc.isConnectingToInternet()) {

            AndyUtils.showToast(mContext, mContext.getString(R.string.no_internet));

        } else {

            getInvitedList(project_id);

        }

        dialog3.show();

    }

    private void getInvitedList(final String project_id) {



        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_INVITED_USER,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:country data", response);

                        invited_list = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                progressBar2.setVisibility(View.GONE);

                                JSONArray dataArray = jsonObject.getJSONArray("invite_tradeworker_list");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    InvitedWorkerData i_model = new InvitedWorkerData();
                                    i_model.setWorker_id(jsonObject1.getString("id"));
                                    i_model.setWorker_name(jsonObject1.getString("firstname") + " " + jsonObject1.getString("lastname"));
                                    i_model.setWorker_email(jsonObject1.getString("email_id"));

                                    invited_list.add(i_model);
                                }


                                InvitedWorkerAdapter inviteAdapter = new InvitedWorkerAdapter(invited_list, R.layout.invited_user_list_item, mContext);
                                rv_invited_user_list.setAdapter(inviteAdapter);

                                ln_no_record.setVisibility(View.INVISIBLE);
                                rv_invited_user_list.setVisibility(View.VISIBLE);


                            } else if (jsonObject.getString("status").equals("404")) {

                                progressBar2.setVisibility(View.GONE);

                                ln_no_record.setVisibility(View.VISIBLE);
                                rv_invited_user_list.setVisibility(View.INVISIBLE);


                            } else {
                                progressBar2.setVisibility(View.GONE);
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar2.setVisibility(View.GONE);
                cc.showToast(mContext.getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", project_id);
                params.put("role", "My_project");

                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", Const.Authorizations.AUTHORIZATION);
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                Log.i("request header", headers.toString());
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }
}


package com.mxi.buildsterapp.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.adapter.ChatAdapter;
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.model.ChatMessageData;
import com.mxi.buildsterapp.utils.EndlessRecyclerViewScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ChatActivity extends AppCompatActivity {

    CommanClass cc;

    TextView tv_username;

    ImageView iv_back,iv_user_pic;

    RecyclerView rv_chat;

    boolean SEMAFOR = false;
    boolean SEMAFOR_Send_Message = false;
    ImageView iv_send_chat;
    EditText et_message_chat;
    ProgressDialog pDialog;
    String FinalUrl = "";
    LinearLayout ll_main, ll_sendMessage;
    String target_user_id = "";
    String from_user_id = "";
    boolean patient = false;
    int total_messages = 0;
    Integer responseString = null;
    ArrayList<ChatMessageData> chatMessageList;
    ArrayList<ChatMessageData> tempChatMessageList;
    ChatAdapter chatAdapter;
    LinearLayoutManager llm;
    int total_chat_count = 0;
    int total_pages = 0;
    boolean flag = true;

    Handler handler;

    boolean stopHandler = true;

    int rc_count = 0;

    String pro_type;

    Timer timer;

    ProgressDialog progressDialogChat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        progressDialogChat = new ProgressDialog(this);
        progressDialogChat.setMessage("Please wait...");
        progressDialogChat.setCancelable(false);

        pro_type = getIntent().getStringExtra("pro_type");

        target_user_id = cc.loadPrefString("to_user_id");
        from_user_id = cc.loadPrefString("from_user_id");

        chatMessageList = new ArrayList<>();
        tempChatMessageList = new ArrayList<>();
        llm = new LinearLayoutManager(this);

        tv_username = (TextView) findViewById(R.id.tv_username);
        rv_chat = (RecyclerView) findViewById(R.id.rv_chat);
        iv_send_chat = (ImageView) findViewById(R.id.iv_send_chat);
        iv_user_pic = (ImageView) findViewById(R.id.iv_user_pic);
        et_message_chat = (EditText) findViewById(R.id.et_message_chat);
        ll_main = (LinearLayout) findViewById(R.id.ll_main);
        ll_sendMessage = (LinearLayout) findViewById(R.id.ll_sendMessage);
        ll_sendMessage.setEnabled(false);

        iv_back = (ImageView) findViewById(R.id.iv_back);

        tv_username.setText(cc.loadPrefString("username"));

        Glide.with(this).load(cc.loadPrefString("user_profilepic")).into(iv_user_pic);

        clickListner();
    }

    private void clickListner() {

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                onBackPressed();

            }
        });

        iv_send_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = et_message_chat.getText().toString();

                if(!cc.isConnectingToInternet()){

                    cc.showToast("No internet connection");

                }else {

                    if (!message.isEmpty()) {
                        SEMAFOR = true;
                        //et_message_chat.setText("");
                        makeJsonCallSendMessage(message);
                    }else {

                        cc.showToast("Please enter message");
                    }
                }
            }
        });

        makeJsonCallGetChat();
    }

    private void makeJsonCallSendMessage(final String message) {

        et_message_chat.setText("");
        progressDialogChat.show();

        SEMAFOR_Send_Message=true;
        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.SEND_MESSAGE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("@@@All Chat", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {

                                et_message_chat.setText("");

                               /* ChatMessageData data = new ChatMessageData();

                                JSONObject object = jsonObject.getJSONObject("message_data");

                                String time = object.getString("created_date");

                                SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                Date date = null;
                                try {
                                    date = dFormat.parse(time);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                                String timeString = dateFormat.format(date);

                                data.setFromId(object.getString("from_id"));
                                data.setFromName(object.getString("from_name"));
                                data.setFromTime(timeString);
                                data.setToId(object.getString("to_id"));
                                data.setToName(object.getString("to_name"));
                                data.setToTime(timeString);

                                data.setChatMessage(object.getString("message"));

                                chatMessageList.add(data);
                                if (chatAdapter == null){

                                    chatAdapter = new ChatAdapter(ActivityChat.this, chatMessageList);
                                }
*/
                                //chatAdapter.notifyItemRangeInserted(0, chatMessageList.size());
                               /* chatAdapter.notifyItemInserted(chatMessageList.size()-1);
                                rv_chat.scrollToPosition(chatMessageList.size()-1);*/
                                SEMAFOR_Send_Message=false;
                                SEMAFOR = false;

                                makeJsonCallGetChat2();

                            } else {
                                SEMAFOR_Send_Message=false;
                                SEMAFOR = false;

                                cc.showToast(jsonObject.getString("message"));

                            }

                        } catch (JSONException e) {
                            SEMAFOR = false;
                            e.printStackTrace();
                            Log.e("sendchat Error", e.toString());
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                cc.showSnackbar(ll_main, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", cc.loadPrefString("user_id"));

                if (pro_type.equals("mypro")){

                    params.put("project_id", cc.loadPrefString("project_id_main"));
                    params.put("project_type", "My_project");
                }else {

                    params.put("project_id", cc.loadPrefString("project_id_assigned"));
                    params.put("project_type", "Included_project");
                }
                params.put("to_user_id", target_user_id);
                params.put("msg", message);


                Log.i("@@@Send Message", params.toString());

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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");


    }
    private void makeJsonCallGetChat() {
        pDialog = new ProgressDialog(ChatActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.show();


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_CHAT_MESSAGE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("@@@All Chat", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();


                            if (jsonObject.getString("status").equals("200")) {
                                //total_chat_count = jsonObject.getInt("total_chat_count");

                                JSONArray jsonChatArray = jsonObject.getJSONArray("message_result");

                                total_chat_count = jsonChatArray.length();

                                total_pages = total_chat_count / 20;
                                float temp = total_chat_count % 20;
                                if (temp != 0.00) {
                                    total_pages = total_pages + 1;
                                }
                                if (!chatMessageList.isEmpty()) {
                                    chatMessageList.clear();
                                    total_messages = 0;
                                }

                                Log.e("@TotalPages", total_pages + "");

                                total_messages = jsonChatArray.length();

                                Log.e("@TotalMessage", total_messages + "");


                                readMessage();


                                callAsynchronousTask();



                            } else {

                                if (chatMessageList.isEmpty()) {
                                    cc.showToast("Message not found");
//                                    callAsynchronousTask();

                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("chat Error", e.toString());

                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
//                cc.showSnackbar(ll_main, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", cc.loadPrefString("user_id"));

                if (pro_type.equals("mypro")){

                    params.put("project_id", cc.loadPrefString("project_id_main"));

                }else {

                    params.put("project_id", cc.loadPrefString("project_id_assigned"));
                }
                params.put("to_user_id", target_user_id);


                Log.i("@@@ GetChat", params.toString());

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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    private void makeJsonCallGetChat2() {



        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_CHAT_MESSAGE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("@@@All Chat", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("status").equals("200")) {
                                //total_chat_count = jsonObject.getInt("total_chat_count");

                                JSONArray jsonChatArray = jsonObject.getJSONArray("message_result");

                                total_chat_count = jsonChatArray.length();

                                total_pages = total_chat_count / 20;
                                float temp = total_chat_count % 20;
                                if (temp != 0.00) {
                                    total_pages = total_pages + 1;
                                }
                                if (!chatMessageList.isEmpty()) {
                                    chatMessageList.clear();
                                    total_messages = 0;
                                }

                                Log.e("@TotalPages", total_pages + "");

                                total_messages = jsonChatArray.length();

                                Log.e("@TotalMessage", total_messages + "");


                                readMessage();


                                callAsynchronousTask();



                            } else {

                                if (chatMessageList.isEmpty()) {
                                    cc.showToast("Message not found");
//                                    callAsynchronousTask();

                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("chat Error", e.toString());

                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

//                cc.showSnackbar(ll_main, getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", cc.loadPrefString("user_id"));

                if (pro_type.equals("mypro")){

                    params.put("project_id", cc.loadPrefString("project_id_main"));

                }else {

                    params.put("project_id", cc.loadPrefString("project_id_assigned"));
                }
                params.put("to_user_id", target_user_id);


                Log.i("@@@ GetChat", params.toString());

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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
    }

    private void readMessage() {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.READ_MESSAGE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("response:read", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("status").equals("200")){


                            }else{

                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                if (pro_type.equals("mypro")){

                    params.put("project_id", cc.loadPrefString("project_id_main"));

                }else {

                    params.put("project_id", cc.loadPrefString("project_id_assigned"));
                }
                Log.e("request read", params.toString());
                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("UserAuth", cc.loadPrefString("user_token"));
                headers.put("Authorization", cc.loadPrefString("Authorization"));
                Log.i("request header", headers.toString());
                return headers;
            }

        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");

    }

    public void callAsynchronousTask() {
        handler = new Handler();
        timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            if (!SEMAFOR) {

                                Log.e("@@SEMAFOR", "Is Free");

                                new PerformBackgroundTask().execute();

                            } else {
                                Log.e("@@SEMAFOR", "Is In Use");
                            }

                            // PerformBackgroundTask this class is the class that extends AsynchTask

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 3000); //execute in every 50000 ms

        //new PerformBackgroundTask().execute();
    }


    private class PerformBackgroundTask extends AsyncTask<Void, Integer, Integer> {


        public PerformBackgroundTask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            SEMAFOR = true;
            Log.e("@@@In Async Task", "In Background");
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private Integer uploadFile() {

            if (!tempChatMessageList.isEmpty()) {
                tempChatMessageList.clear();
                responseString = 0;
            }


            for (int i = 1; i <= total_pages; i++) {

                final int finalI = i;
                final int finalI1 = i;
                StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_CHAT_MESSAGE,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                Log.e("@@@All Chat Async", response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);



                                    if (jsonObject.getString("status").equals("200")) {

                                        JSONArray jsonChatArray1 = jsonObject.getJSONArray("message_result");

                                        total_chat_count = jsonChatArray1.length();

                                        total_pages = total_chat_count / 20;
                                        float temp = total_chat_count % 20;
                                        if (temp != 0.00) {
                                            total_pages = total_pages + 1;
                                        }

                                        /*if (!chatMessageList.isEmpty()) {
                                            chatMessageList.clear();
                                            chatAdapter.notifyDataSetChanged();

                                            total_messages = 0;
                                        }
*/
                                        Log.e("@TotalPages_routine", total_pages + "");


                                        JSONArray jsonChatArray = jsonObject.getJSONArray("message_result");

                                        for (int i = 0; i < jsonChatArray.length(); i++) {

                                            ChatMessageData data = new ChatMessageData();

                                            JSONObject object = (JSONObject) jsonChatArray.get(i);

                                            String time = object.getString("created_datetime");

/*
                                            SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                            Date date = null;
                                            try {
                                                date = dFormat.parse(time);
                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                                            String timeString = dateFormat.format(date);
*/

                                            data.setFromId(object.getString("from_user_id"));
//                                            data.setFromName(object.getString("from_name"));
                                            data.setFromName(object.getString("sender_firstname") +" "+ object.getString("sender_lastname"));
                                            data.setFromTime(time);
                                            data.setToId(object.getString("to_user_id"));
//                                            data.setToName(object.getString("to_name"));
                                            data.setToName(object.getString("reciever_firstname") +" "+ object.getString("reciever_lastname"));

                                            data.setToTime(time);
                                            data.setMessageId(object.getString("id"));
                                            data.setChatMessage(object.getString("text"));

                                            tempChatMessageList.add(data);

                                        }


                                        if (finalI == total_pages) {
                                            if (!SEMAFOR_Send_Message) {
                                                /*if (tempChatMessageList.size() == total_messages) {

                                                    if (!chatMessageList.isEmpty()) {
                                                        chatMessageList.clear();
                                                    }

                                                    chatMessageList.addAll(tempChatMessageList);

                                                    total_messages = chatMessageList.size();

                                                    tempChatMessageList.clear();

                                                    chatAdapter = new ChatAdapter(ChatActivity.this, chatMessageList);


                                                    if (flag) {
                                                        flag = false;
                                                        ll_sendMessage.setEnabled(true);
                                                    }

                                                    llm.setStackFromEnd(true);
                                                    rv_chat.setLayoutManager(llm);
                                                   *//* rv_chat.addOnScrollListener(new EndlessRecyclerViewScrollListener( llm) {
                                                        @Override
                                                        public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                                                            Toast.makeText(ChatActivity.this,"LAst",Toast.LENGTH_LONG).show();

                                                            Log.e("@@RC","-----1");
                                                        }
                                                    });*//*
                                                    rv_chat.setAdapter(chatAdapter);
                                                } else {
                                                    Log.e("@@Rv", "Remains Same");

                                                }
*/
                                                if (!chatMessageList.isEmpty()) {
                                                    chatMessageList.clear();
                                                }

                                                chatMessageList.addAll(tempChatMessageList);

                                                total_messages = chatMessageList.size();

                                                tempChatMessageList.clear();

                                                chatAdapter = new ChatAdapter(ChatActivity.this, chatMessageList);

                                                if (flag) {
                                                    flag = false;
                                                    ll_sendMessage.setEnabled(true);
                                                }

                                                llm.setStackFromEnd(true);


                                                rv_chat.setLayoutManager(llm);

                                                rv_chat.setAdapter(chatAdapter);

                                                progressDialogChat.dismiss();

                                               /* if (stopHandler == false){

                                                    Log.e("@@HAND","........True");
                                                    Log.e("@@HAND_COUNT", String.valueOf(rc_count));

                                                    if (rc_count == 1){

                                                        rv_chat.setAdapter(chatAdapter);

                                                    }else {

                                                    }

                                                    rc_count++;

                                                }else {

                                                    stopHandler = false;
                                                    rc_count++;

                                                    Log.e("@@HAND","........False");
                                                }

                                                rv_chat.addOnScrollListener(new EndlessRecyclerViewScrollListener( llm) {
                                                    @Override
                                                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                                                        Toast.makeText(ChatActivity.this,"LAst",Toast.LENGTH_LONG).show();

                                                        //rv_chat.setAdapter(chatAdapter);
                                                    }
                                                });
*/

                                            }

                                        }


                                    } else {

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progressDialogChat.dismiss();
                                    Log.e("chat Error", e.toString());
                                }
                            }

                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialogChat.dismiss();

//                        cc.showSnackbar(ll_main, getString(R.string.ws_error));
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("user_id", cc.loadPrefString("user_id"));
                        if (pro_type.equals("mypro")){

                            params.put("project_id", cc.loadPrefString("project_id_main"));

                        }else {

                            params.put("project_id", cc.loadPrefString("project_id_assigned"));
                        }
                        params.put("to_user_id", target_user_id);

                        /*params.put("page", finalI1 + "");

                        if (!patient) {
                            params.put("target_user_id", target_user_id);
                        }*/

                        Log.i("@@@ GetChat", params.toString());

                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("UserAuth", cc.loadPrefString("user_token"));
                        headers.put("Authorization", "delta141forceSEAL8PARA9MARCOSBRAHMOS");
                        Log.i("request header", headers.toString());
                        return headers;
                    }
                };

                jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                AppController.getInstance().addToRequestQueue(jsonObjReq, "Temp");
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.e("Chat: result", "Response from server: " + result);
            SEMAFOR = false;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (timer == null){

        }else {
            timer.cancel();
        }
    }
}

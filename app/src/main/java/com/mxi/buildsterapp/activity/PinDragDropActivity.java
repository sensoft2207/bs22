package com.mxi.buildsterapp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
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
import com.mxi.buildsterapp.comman.AppController;
import com.mxi.buildsterapp.comman.CheckNetworkConnection;
import com.mxi.buildsterapp.comman.CommanClass;
import com.mxi.buildsterapp.comman.Const;
import com.mxi.buildsterapp.dragevent.DragController;
import com.mxi.buildsterapp.dragevent.DragLayer;
import com.mxi.buildsterapp.model.TradeWorkerPinData;
import com.mxi.buildsterapp.utils.DoubleClickListener;
import com.mxi.buildsterapp.utils.ZoomLinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PinDragDropActivity extends AppCompatActivity implements /*View.OnDragListener,*/ View.OnLongClickListener {

    CommanClass cc;

    TextView tv_screen_name;

    ImageView iv_back, iv_dropdown;

//    ImageView iv_issue_img;

    LinearLayout ln_visible_invisible, ln_no_worker;

    boolean isDropdown = false;

    RecyclerView rc_worker_list;
    TradeWorkerPinAdapter twpAdapter;
    List<TradeWorkerPinData> workerList;

    ProgressBar progress;

//    ZoomLinearLayout zoomLinearLayout;

    int i = 0;

    View.OnClickListener clickListener;


//    private DragController mDragController;
//    private DragLayer mDragLayer;

    ImageView shape;

    int x_touch = 0;
    int y_touch = 0;

    byte[] byteArrayEditedIssue;

    int shap_counter = 0;

    String projectName, issue_img_main;

    RelativeLayout ll_main;


    WebView home_web;

    ProgressBar pb;
    ImageView iv_no_network;


    String url;



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:{
                    webViewGoBack();
                }break;
            }
        }
    };


    private ValueCallback<Uri[]> mUploadMessageArr;
    private ValueCallback<Uri> mUploadMessage;

    private final static int REQUEST_SELECT_FILE = 1;
    private final static int REQUEST_SELECT_FILE_LEGACY = 1002;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_dra_drop_activity);

        init();
    }

    private void init() {

        cc = new CommanClass(this);

        projectName = "demo";

//        mDragController = new DragController(this);

        issue_img_main = cc.loadPrefString("issue_img_main");

        home_web = (WebView)findViewById(R.id.home_web);
        pb = (ProgressBar) findViewById(R.id.pb);
        iv_no_network = (ImageView) findViewById(R.id.iv_no_network);

        ln_visible_invisible = (LinearLayout) findViewById(R.id.ln_visible_invisible);
        ln_no_worker = (LinearLayout) findViewById(R.id.ln_no_worker);

        tv_screen_name = (TextView) findViewById(R.id.tv_screen_name);

        ll_main = (RelativeLayout) findViewById(R.id.ll_main);

        progress = (ProgressBar) findViewById(R.id.progress);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_dropdown = (ImageView) findViewById(R.id.iv_dropdown);
//        iv_issue_img = (ImageView) findViewById(R.id.iv_issue_img);

//        Glide.with(this).load(issue_img_main).into(iv_issue_img);

        tv_screen_name.setText(cc.loadPrefString("screen_name_main"));


        rc_worker_list = (RecyclerView) findViewById(R.id.rc_worker_list);

//        zoomLinearLayout = (ZoomLinearLayout) findViewById(R.id.zoom_linear_layout);
//
//        zoomLinearLayout.init(PinDragDropActivity.this);

//        DragController dragController = mDragController;
//        mDragLayer = (DragLayer) findViewById(R.id.fl_drag_view);
//
//        mDragLayer.setOnDragListener(this);
//        mDragLayer.setDragController(dragController);
//        dragController.addDropTarget(mDragLayer);
//


        url = getIntent().getStringExtra("url");

        startWebView(url);

        Log.e("URLLL",url);

        home_web.setOnDragListener(new MyDragListener());

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

        iv_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isDropdown == false) {

                    iv_dropdown.setImageResource(R.drawable.ic_up);

                    isDropdown = true;

                    ln_visible_invisible.setVisibility(View.GONE);


                } else {

                    iv_dropdown.setImageResource(R.drawable.ic_down);

                    isDropdown = false;

                    ln_visible_invisible.setVisibility(View.VISIBLE);

                }

            }
        });

        if (!cc.isConnectingToInternet()) {

            cc.showToast(getString(R.string.no_internet));

        } else {

            getInvitedTradeworkerWS();

        }

    }

//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public boolean onDrag(View v, DragEvent event) {
//        switch (event.getAction()) {
//            case DragEvent.ACTION_DRAG_ENTERED:
//
//                break;
//            case DragEvent.ACTION_DRAG_EXITED:
//
//                break;
//            case DragEvent.ACTION_DRAG_ENDED:
//
//                break;
//            case DragEvent.ACTION_DROP:
//                Log.e("Drop", "Yes its Activity Drop");
//                final float dropX = event.getX();
//                final float dropY = event.getY();
//                final DragData state = (DragData) event.getLocalState();
//
//                final LinearLayout ln_main = (LinearLayout) LayoutInflater.from(this).inflate(
//                        R.layout.rc_pin_trade_worker_item, mDragLayer, false);
//
//                shape = (ImageView) ln_main.findViewById(R.id.iv_pin);
//                shape.setImageResource(state.item);
////                cc.savePrefInt("pin_resource", state.item);
////
////                i = cc.loadPrefInt("counter");
////                i = i + 1;
////                cc.savePrefInt("counter", i);
////                shape.setTag(false);
////                shape.setId(i);
////                Log.e("PinTag", i + "");
//
//                if (shape.getParent() != null) {
//
//                    ((ViewGroup) shape.getParent()).removeView(shape); // <- fix
//
//                } else {
//
//                    ln_main.addView(shape);
//
//                }
//
//
////                shape.setOnClickListener(doubleClickListener);
////                shape.setOnLongClickListener(this);
//
//                int x = (int) (dropX - (float) state.width / 2);
//                final int y = (int) (dropY - (float) state.height / 2);
//                DragLayer.LayoutParams params;
//
//                params = new DragLayer.LayoutParams(DragLayer.LayoutParams.WRAP_CONTENT, DragLayer.LayoutParams.WRAP_CONTENT, x, y);
//
//                if (shape.getParent() != null) {
//
//                    ((ViewGroup) shape.getParent()).removeView(shape); // <- fix
//
//                } else {
//
//                    mDragLayer.addView(shape, params);
////                    int totalPin = cc.loadPrefInt(projectName + "_counter");
////                    totalPin = totalPin + 1;
////                    cc.savePrefInt(projectName + "_" + totalPin, shape.getId());
////                    cc.savePrefInt(shape.getId() + "_x", x);
////                    cc.savePrefInt(shape.getId() + "_y", y);
////                    cc.savePrefInt(projectName + "_counter", totalPin);
////
////
////                    Log.e("@@XXXX", String.valueOf(x));
////                    Log.e("@@YYYY", String.valueOf(y));
//
//                }
//
//
//
//                /*Pending vishal.................*/
//
//
//                shape.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent ev) {
//                        boolean handledHere = false;
//
//                        final int action = ev.getAction();
//
//                        switch (action) {
//                            case MotionEvent.ACTION_DOWN:
//
//                                x_touch = (int) ev.getX();
//                                y_touch = (int) ev.getY();
//
//                                cc.savePrefInt(shape.getId() + "_x", x_touch);
//                                cc.savePrefInt(shape.getId() + "_y", y_touch);
//
//                                handledHere = startDrag(v);
//                                if (handledHere) {
//                                    v.performClick();
//                                }
//
//                                Log.e("In Action Down", x_touch + "_" + y_touch);
//
//                                break;
//
//                            case MotionEvent.ACTION_UP:
//                                x_touch = (int) ev.getX();
//                                y_touch = (int) ev.getY();
//                                Log.e("In Action Up", x_touch + "_" + y_touch);
//                                break;
//                        }
//
//                        return handledHere;
//                    }
//                });
//
//
//                break;
//            default:
//                break;
//        }
//        return true;
//    }


//    DoubleClickListener doubleClickListener = new DoubleClickListener() {
//        @Override
//        public void onSingleClick(View v) {
//
//        }
//
//        @Override
//        public void onDoubleClick(final View view_main) {
//
//            Log.e("@PIN_Tag", view_main.getTag() + "");
//            Log.e("@PIN_Id", view_main.getId() + "");
//
//            view_main.setTag(cc.loadPrefBoolean(String.valueOf(view_main.getId())));
//
//            boolean isTrue = (boolean) view_main.getTag();
//            if (!isTrue) {
//                final Dialog d = new BottomSheetDialog(PinDragDropActivity.this);
//                d.setContentView(R.layout.bottom_dialog_issue);
//                d.setCancelable(true);
//                d.show();
//
//                Drawable fDraw = shape.getDrawable();
//                Drawable sDraw = getResources().getDrawable(R.drawable.ic_pin_red);
//
//                if (fDraw.hashCode() == sDraw.hashCode()) {
//
//                    cc.showToast("Same Image");
//                } else {
//                    cc.showToast("Not same Image");
//                }
//
//
//                Button btn_next_dialog = (Button) d.findViewById(R.id.btn_next_dialog);
//                Button btn_delete_dialog = (Button) d.findViewById(R.id.btn_delete_dialog);
//
//                btn_next_dialog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        /*getEditedIssueImage();
//
//                        Intent intentPostIssue = new Intent(EditProDetailActivity.this, PostIssueActivity.class);
//                        intentPostIssue.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intentPostIssue.putExtra("byteArrayEditedIssue", byteArrayEditedIssue);
//                        intentPostIssue.putExtra("PinId", view_main.getId());
//                        startActivity(intentPostIssue);
//                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//                        d.dismiss();
//*/
//
//                    }
//                });
//
//                btn_delete_dialog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        int pinId = v.getId();
//                        int pinNumber;
//                        boolean foundMatch = false;
//                        int totalPin = cc.loadPrefInt(projectName + "_counter");
//                        totalPin = totalPin - 1;
//                        cc.savePrefInt(projectName + "_counter", totalPin);
//                        for (int j = 1; j < +totalPin; j++) {
//                            int curPin = cc.loadPrefInt(projectName + "_" + j);
//                            if (curPin == pinId) {
//                                foundMatch = true;
//                            }
//
//                            if (foundMatch) {
//                                int k = j + 1;
//                                int nextPinId = cc.loadPrefInt(projectName + "_" + k);
//                                cc.savePrefInt(projectName + "_" + j, nextPinId);
//                            }
//                        }
//
//                        cc.removePref(String.valueOf(v.getId()));
//
//                        mDragLayer.removeView(view_main);
//                        d.dismiss();
//                    }
//                });
//
//            }
//        }
//    };

    @Override
    public boolean onLongClick(View view) {

        cc.showToast("Pin Id " + view.getId() + " is Long Pressed");

        // showSnackbarView(view,this);
        return true;
    }

   /* public void showSnackbarView(View view,Context context){
        // Create the Snackbar
        Snackbar snackbar = Snackbar.make(ll_main, "", Snackbar.LENGTH_LONG);
// Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
// Hide the text

        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

// Inflate our custom view
        LayoutInflater mInflater= LayoutInflater.from(context);
        View snackView = mInflater.inflate(R.layout.raw_issues_list, null);
// Configure the view


        ImageView ivIssue = (ImageView) findViewById(R.id.iv_issue);
        TextView tvIssueTitle = (TextView) findViewById(R.id.tv_issue_title);
        TextView tvIssueDescription = (TextView) findViewById(R.id.tv_issue_description);
        LinearLayout llComplete = (LinearLayout) findViewById(R.id.ll_complete);
        LinearLayout llRemains = (LinearLayout) findViewById(R.id.ll_reject);





//If the view is not covering the whole snackbar layout, add this line
        layout.setPadding(0,0,0,0);

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        snackbar.show();
    }*/


    private void getInvitedTradeworkerWS() {


        StringRequest jsonObjReq = new StringRequest(Request.Method.POST, Const.ServiceType.GET_INVITED_TRADEWORKER,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.e("response:invited data", response);

                        workerList = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            progress.setVisibility(View.GONE);

                            if (jsonObject.getString("status").equals("200")) {


                                JSONArray dataArray = jsonObject.getJSONArray("invite_tradeworker_list");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    JSONObject jsonObject1 = dataArray.getJSONObject(i);


                                    TradeWorkerPinData m_data = new TradeWorkerPinData();
                                    m_data.setId(jsonObject1.getString("id"));
                                    m_data.setFirstname(jsonObject1.getString("firstname"));
                                    m_data.setLastname(jsonObject1.getString("lastname"));
                                    m_data.setProfile_image(jsonObject1.getString("profile_image_thumb"));
                                    m_data.setColor_code(jsonObject1.getString("color_code"));
                                    m_data.setPin(R.drawable.ic_pin_red);

                                    workerList.add(m_data);
                                }

                                ln_no_worker.setVisibility(View.INVISIBLE);
                                rc_worker_list.setVisibility(View.VISIBLE);

                                twpAdapter = new TradeWorkerPinAdapter(workerList, getApplication());

                                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(PinDragDropActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                rc_worker_list.setLayoutManager(horizontalLayoutManager);
                                rc_worker_list.setAdapter(twpAdapter);


                            } else if (jsonObject.getString("status").equals("404")) {
                                progress.setVisibility(View.GONE);
                                ln_no_worker.setVisibility(View.VISIBLE);
                                rc_worker_list.setVisibility(View.INVISIBLE);

                            } else {
                                progress.setVisibility(View.GONE);
                                cc.showToast(jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progress.setVisibility(View.GONE);
                cc.showToast(getString(R.string.ws_error));
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", cc.loadPrefString("user_id"));
                params.put("project_id", cc.loadPrefString("project_id_main"));
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

    public class TradeWorkerPinAdapter extends RecyclerView.Adapter<TradeWorkerPinAdapter.MyViewHolder> {


        List<TradeWorkerPinData> workerList = Collections.emptyList();
        Context context;

        int item;


        public TradeWorkerPinAdapter(List<TradeWorkerPinData> workerList, Context context) {

            this.workerList = workerList;
            this.context = context;
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView iv_worker_pic, iv_pin;

            TextView tv_worker_name;

            ProgressBar progressBar;

            public MyViewHolder(View view) {
                super(view);


                iv_worker_pic = (ImageView) view.findViewById(R.id.iv_worker_pic);
                iv_pin = (ImageView) view.findViewById(R.id.iv_pin);
                tv_worker_name = (TextView) view.findViewById(R.id.tv_worker_name);
                progressBar = (ProgressBar) view.findViewById(R.id.progress);
            }
        }


        @Override
        public TradeWorkerPinAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rc_pin_trade_worker_item, parent, false);

            itemView.getLayoutParams().width = (int) (getScreenWidth() / 5); /// THIS LINE WILL DIVIDE OUR VIEW INTO NUMBERS OF PARTS

            return new TradeWorkerPinAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final TradeWorkerPinAdapter.MyViewHolder holder, final int position) {

            final TradeWorkerPinData td = workerList.get(position);

            holder.tv_worker_name.setText(td.getFirstname() + " " + td.getLastname());

            holder.iv_pin.setImageResource(td.getPin());

            final View shape = holder.iv_pin;


            holder.iv_pin.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                            v);
                    v.startDrag(data, shadowBuilder, v, 0);


//                    TradeWorkerPinData tdp = workerList.get(position);
//
//                    item = tdp.getPin();
//
//                    final DragData state = new DragData(item, shape.getWidth(), shape.getHeight());
//                    final View.DragShadowBuilder shadow = new View.DragShadowBuilder(shape);
//                    ViewCompat.startDragAndDrop(shape, null, shadow, state, 0);

                    return true;
                }
            });

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


        }


        @Override
        public int getItemCount() {
            return workerList.size();
        }

        public int getScreenWidth() {

            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            return size.x;
        }
    }

    public class DragData {

        public final int item;
        public final int width;
        public final int height;

        public DragData(int item, int width, int height) {
            this.item = item;
            this.width = width;
            this.height = height;
        }

    }

//    public boolean startDrag(View v) {
//
//        Object obj = v;
//        mDragController.startDrag(v, mDragLayer, obj, DragController.DRAG_ACTION_MOVE);
//
//        return true;
//
//    }

    class MyDragListener implements View.OnDragListener {
        Drawable enterShape = getResources().getDrawable(
                R.drawable.ic_pin_red);
        Drawable normalShape = getResources().getDrawable(R.drawable.ic_pin_red);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    float X = event.getX();
                    float Y = event.getY();
                    View view = (View) event.getLocalState();
                    view.setX(X-(view.getWidth()/2));
                    view.setY(Y-(view.getHeight()/2));
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    WebView container = (WebView) v;
                    
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundDrawable(normalShape);
                default:
                    break;
            }
            return true;
        }
    }

    private void startWebView(String url) {

        if(CheckNetworkConnection.isInternetAvailable(this))
        {


            home_web.getSettings().setJavaScriptEnabled(true);

            home_web.getSettings().setAppCacheEnabled(true);
            // settings.setBuiltInZoomControls(true);
            home_web.getSettings().setPluginState(WebSettings.PluginState.ON);
            home_web.getSettings().setJavaScriptEnabled(true);
            home_web.getSettings().setBuiltInZoomControls(false);
            home_web.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            home_web.getSettings().setAllowFileAccess(true);



            home_web.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {
                    pb.setProgress(progress);
                    if (progress == 100) {
                        pb.setVisibility(View.GONE);
                        //iv_no_network.setVisibility(View.GONE);

                    } else {
                        pb.setVisibility(View.VISIBLE);
                        //iv_no_network.setVisibility(View.GONE);
                    }


                }

                @SuppressWarnings("unused")
                public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                    mUploadMessage = uploadMsg;

                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("*/*");

                    startActivityForResult(Intent.createChooser(i, getString(R.string.select_file)), REQUEST_SELECT_FILE_LEGACY);

                }

                // For Android 3.0+
                @SuppressWarnings("unused")
                public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                    mUploadMessage = uploadMsg;

                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType(acceptType);

                    startActivityForResult(Intent.createChooser(i, getString(R.string.select_file)), REQUEST_SELECT_FILE_LEGACY);
                }

                // For Android 4.1+
                @SuppressWarnings("unused")
                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                    mUploadMessage = uploadMsg;

                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType(acceptType);

                    startActivityForResult(Intent.createChooser(i, getString(R.string.select_file)), REQUEST_SELECT_FILE_LEGACY);
                }

                // For Android 5.0+
                @SuppressLint("NewApi")
                public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                    if (mUploadMessageArr != null) {
                        mUploadMessageArr.onReceiveValue(null);
                        mUploadMessageArr = null;
                    }

                    mUploadMessageArr = filePathCallback;

                    Intent intent = fileChooserParams.createIntent();

                    try {
                        startActivityForResult(intent, REQUEST_SELECT_FILE);
                    } catch (ActivityNotFoundException e) {
                        mUploadMessageArr = null;

                        cc.showToast("Error in file chooser");

                        return false;
                    }

                    return true;
                }





            });
            home_web.setWebViewClient(new WebViewClient() {
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                    home_web.loadData("<center><b></b></center>", "text/html", null);
                    iv_no_network.setVisibility(View.VISIBLE);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    view.loadUrl(url);
                    //view.loadUrl(url);

                    return false;
                }
            });

            home_web.setOnKeyListener(new View.OnKeyListener(){

                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK
                            && event.getAction() == MotionEvent.ACTION_UP
                            ) {

                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                        onBackPressed();
                        handler.sendEmptyMessage(1);
                        return true;
                    }

                    return false;
                }

            });



            home_web.loadUrl(url);


        }
        else{

            iv_no_network.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
        }

    }

    private void webViewGoBack() {

        home_web.goBack();

        home_web.clearHistory();
    }

    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_FILE_LEGACY) {
            if (mUploadMessage == null) return;

            Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();

            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }

        else if (requestCode == REQUEST_SELECT_FILE) {
            if (mUploadMessageArr == null) return;

            mUploadMessageArr.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            mUploadMessageArr = null;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        home_web.clearHistory();
        home_web.clearCache(true);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        home_web.clearHistory();
        home_web.clearCache(true);
    }
}

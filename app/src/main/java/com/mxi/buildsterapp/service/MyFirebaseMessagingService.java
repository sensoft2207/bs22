package com.mxi.buildsterapp.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mxi.buildsterapp.R;
import com.mxi.buildsterapp.activity.AssignedProjectActionItemUpdate;
import com.mxi.buildsterapp.activity.ChatActivity;
import com.mxi.buildsterapp.activity.LoginActivity;
import com.mxi.buildsterapp.activity.MyProjectActionItemDetailUpdate;
import com.mxi.buildsterapp.comman.CommanClass;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;

import me.leolin.shortcutbadger.ShortcutBadger;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    CommanClass cc;
    private static final String TAG = "FirebaseMessageService";
    private static final String CHANNEL_ID = "007";
    Bitmap bitmap;
    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



    Bitmap bannerimage;
    private static int MY_NOTIFICATION_ID=1;
    NotificationManager notificationManager;
    Notification myNotification;

    String project_id = "";
    String notification_type = "";
    String user_id = "";
    String screen_id = "";
    String deficiency_id = "";
    String to_user_id = "";
    String project_type = "";

    Uri soundUri;


    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @SuppressLint("WrongThread")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        //

        Map<String, String> data = remoteMessage.getData();
//        String message = data.get("Message");
        String message = data.get("body");
        String image = data.get("image");
        String title = data.get("title");

        project_id = remoteMessage.getData().get("project_id");
        notification_type = remoteMessage.getData().get("notification_type");
        user_id = remoteMessage.getData().get("user_id");
        screen_id = remoteMessage.getData().get("screen_id");
        deficiency_id = remoteMessage.getData().get("deficiency_id");
        to_user_id = remoteMessage.getData().get("to_user_id");
        project_type = remoteMessage.getData().get("project_type");

        Log.e("@@NotiFicationContent", String.valueOf(remoteMessage.getData()));

        cc = new CommanClass(this);

        int badgeCount = 1;
        ShortcutBadger.applyCount(getApplicationContext(), badgeCount); //for 1.1.4+
//        ShortcutBadger.with(getApplicationContext()).count(badgeCount); //for 1.1.3

        sendNotification(title,message,project_id,notification_type,user_id,screen_id,deficiency_id,to_user_id);

    }

    private void sendNotification(String title, String messageBody, String project_id, String notification_type,
                                  String user_id, String screen_id, String deficiency_id, String to_user_id) {


        soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (notification_type.equals("new pin") || notification_type.equals("approved task") || notification_type.equals("new comment")
                || notification_type.equals("Completed for review") || notification_type.equals("approved task")){

            if (project_type.equals("My_project")){

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
                Intent intent = new Intent(getApplicationContext(), MyProjectActionItemDetailUpdate.class);

                cc.savePrefString("project_id_main",project_id);
//                cc.savePrefString("user_id",user_id);
                cc.savePrefString("action_screen_id",screen_id);
                cc.savePrefString("action_def_id",deficiency_id);
                cc.savePrefString("titlee","");
//            cc.savePrefString("to_user_id",to_user_id);
//            cc.savePrefString("notification_type",notification_type);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);




                NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                bigText.bigText(title);
                bigText.setBigContentTitle(messageBody);

                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setSmallIcon(R.drawable.ic_notification);
                mBuilder.setContentTitle(title);
                mBuilder.setContentText(messageBody);
                mBuilder.setPriority(Notification.PRIORITY_HIGH);
                mBuilder.setVibrate(new long[]{100, 250});
                mBuilder.setAutoCancel(true);
                mBuilder.setSound(soundUri);
                mBuilder.setColor(getResources().getColor(R.color.colorPrimary));
                mBuilder.setStyle(bigText);

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "fcm", NotificationManager.IMPORTANCE_DEFAULT);
                    mNotificationManager.createNotificationChannel(channel);
                }

                int num = (int) System.currentTimeMillis();
                mNotificationManager.notify(num, mBuilder.build());

            }else {


                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
                Intent intent = new Intent(getApplicationContext(), AssignedProjectActionItemUpdate.class);

                cc.savePrefString("project_id_assigned",project_id);
//                cc.savePrefString("user_id",user_id);
                cc.savePrefString("action_a_screen_id",screen_id);
                cc.savePrefString("action_a_def_id",deficiency_id);
                cc.savePrefString("to_id_type","no");
                cc.savePrefString("title","");
//            cc.savePrefString("to_user_id",to_user_id);
//            cc.savePrefString("notification_type",notification_type);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                bigText.bigText(title);
                bigText.setBigContentTitle(messageBody);

                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setSmallIcon(R.drawable.ic_notification);
                mBuilder.setContentTitle(title);
                mBuilder.setContentText(messageBody);
                mBuilder.setPriority(Notification.PRIORITY_HIGH);
                mBuilder.setVibrate(new long[]{100, 250});
                mBuilder.setAutoCancel(true);
                mBuilder.setSound(soundUri);
                mBuilder.setColor(getResources().getColor(R.color.colorPrimary));
                mBuilder.setStyle(bigText);

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "fcm", NotificationManager.IMPORTANCE_DEFAULT);
                    mNotificationManager.createNotificationChannel(channel);
                }

                int num = (int) System.currentTimeMillis();
                mNotificationManager.notify(num, mBuilder.build());

            }

        }else {

            if (project_type.equals("My_project")){

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);

                cc.savePrefString("project_id_main",project_id);
//                cc.savePrefString("user_id",user_id);
                cc.savePrefString("from_user_id",user_id);
                cc.savePrefString("to_user_id",user_id);

                intent.putExtra("pro_type","mypro");

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                bigText.bigText(title);
                bigText.setBigContentTitle(messageBody);

                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setSmallIcon(R.drawable.ic_notification);
                mBuilder.setContentTitle(title);
                mBuilder.setContentText(messageBody);
                mBuilder.setPriority(Notification.PRIORITY_HIGH);
                mBuilder.setVibrate(new long[]{100, 250});
                mBuilder.setAutoCancel(true);
                mBuilder.setSound(soundUri);
                mBuilder.setColor(getResources().getColor(R.color.colorPrimary));
                mBuilder.setStyle(bigText);

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "fcm", NotificationManager.IMPORTANCE_DEFAULT);
                    mNotificationManager.createNotificationChannel(channel);
                }

                int num = (int) System.currentTimeMillis();
                mNotificationManager.notify(num, mBuilder.build());

            }else {

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);

                cc.savePrefString("project_id_assigned",project_id);
//                cc.savePrefString("user_id",user_id);
                cc.savePrefString("from_user_id",user_id);
                cc.savePrefString("to_user_id",to_user_id);

                intent.putExtra("pro_type","assignpro");

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
                bigText.bigText(title);
                bigText.setBigContentTitle(messageBody);

                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setSmallIcon(R.drawable.ic_notification);
                mBuilder.setContentTitle(title);
                mBuilder.setContentText(messageBody);
                mBuilder.setPriority(Notification.PRIORITY_HIGH);
                mBuilder.setVibrate(new long[]{100, 250});
                mBuilder.setAutoCancel(true);
                mBuilder.setSound(soundUri);
                mBuilder.setColor(getResources().getColor(R.color.colorPrimary));
                mBuilder.setStyle(bigText);

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "fcm", NotificationManager.IMPORTANCE_DEFAULT);
                    mNotificationManager.createNotificationChannel(channel);
                }

                int num = (int) System.currentTimeMillis();
                mNotificationManager.notify(num, mBuilder.build());

            }

        }
    }



}

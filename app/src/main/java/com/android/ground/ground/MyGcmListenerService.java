package com.android.ground.ground;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.ground.ground.R;
import com.android.ground.ground.controller.fc.management.FCManagementActivity;
import com.android.ground.ground.controller.fc.management.FragmentClubMessageEdit;
import com.android.ground.ground.controller.person.main.AlarmFragment;
import com.android.ground.ground.controller.person.main.MainActivity;
import com.android.ground.ground.controller.person.message.MyMessageFragment;
import com.android.ground.ground.controller.person.splash.SplashActivity;
import com.android.ground.ground.model.ActivityStack;
import com.android.ground.ground.model.MyApplication;
import com.google.android.gms.gcm.GcmListenerService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import java.util.List;

public class MyGcmListenerService extends GcmListenerService {

   private static final String TAG = "GCM_Example";
    public static final String NOTI_TAG = "11";
    public static final String MY_MESSAGE_TAG = "12";
   public static final String CLUB_MESSAGE_TAG = "13";

   // 커스텀 메세지 받기
   @Override
   public void onMessageReceived(String from, Bundle data) {

      Log.d(TAG, "From: " + from);
      Log.d("hello", data.toString());
      String title = data.getString("title");
      String body = data.getString("body");

      Log.d(TAG, "GCMListener - onMessageReceived");



      if (from.startsWith("/topics/")) {
         // message received from some topic.
      } else {
         // normal downstream message.
      }

      // [START_EXCLUDE]
      /**
       * Production applications would usually process the message here.
       * Eg: - Syncing with server.
       *     - Store message in local database.
       *     - Update UI.
       */

      /**
       * In some cases it may be useful to show a notification indicating to the user
       * that a message was received.
       */

      // 도착한 메세지를 사용자에게 알린다.
       sendNotification(title, body);

       //refresh

//       ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
//       List<ActivityManager.RunningTaskInfo> Info = am.getRunningTasks(1);
//       ComponentName topActivity = Info.get(0).topActivity;
//
//       String topactivityname = topActivity.getPackageName();
//       Log.d("hello", topactivityname);

//       ActivityStack activityStack =  ActivityStack.getInstance();
//       Activity foregroundActivity = activityStack.getForegroundActivity();
//       Log.d("hello", foregroundActivity.toString());

       try{
            AlarmFragment fragmentByTag = (AlarmFragment)((AppCompatActivity)MyApplication.currentTopActivity).getSupportFragmentManager().findFragmentByTag(NOTI_TAG);
           if(fragmentByTag != null){
               if(fragmentByTag.isVisible()){
                   fragmentByTag.searchNoti();
               }
           }
       }catch(NullPointerException e){
           e.printStackTrace();
       }catch (ClassCastException e){
           e.printStackTrace();
       }

       try {
           MyMessageFragment myMessageFragment = (MyMessageFragment) ((AppCompatActivity) MyApplication.currentTopActivity).getSupportFragmentManager().findFragmentByTag(MY_MESSAGE_TAG);
           if (myMessageFragment != null) {
               if (myMessageFragment.isVisible()) {
                     myMessageFragment.searchMyMessage();
               }
           }
       }catch(NullPointerException e){
           e.printStackTrace();
       }catch (ClassCastException e){
           e.printStackTrace();
       }

       try {
          FragmentClubMessageEdit messageEdit =  ((FragmentClubMessageEdit) ((FCManagementActivity) MyApplication.currentTopActivity).mAdapter.getTabFragment(0));
            if (messageEdit !=null) {
                messageEdit.searchClubMessage();
           }
       }catch(NullPointerException e){
           e.printStackTrace();
       }catch (ClassCastException e){
           e.printStackTrace();
       }

   }

   private void sendNotification(String title, String message) {
      Log.d("hello", title);
      Log.d("hello", message);
      // 알림 터치시 - MainActivity가 나타나도록
      Intent intent = new Intent(this, SplashActivity.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
      PendingIntent pendingIntent = PendingIntent.getActivity(this, 0/* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

      // 알림 효과음
      Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

      // 알림 객체
      NotificationCompat.Builder noti = new NotificationCompat.Builder(this);

      noti.setSmallIcon(R.mipmap.icon)      // 아이콘
      .setContentTitle(title)             // 제목
      .setContentText(message)            // 내용
      .setContentIntent(pendingIntent)    // 알림 선택시
      .setSound(soundUri)                 // 알림음
      .setAutoCancel(true)                // 센터에서 자동 삭제
      .build();

      // 알림 매니저를 통해서 발송
      NotificationManager notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      notiManager.notify(0 /* ID of notification */, noti.build());
   }
}
class HttpRequest {

    // A SyncHttpClient is an AsyncHttpClient
    public static AsyncHttpClient syncHttpClient= new SyncHttpClient();
    public static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    public static void setCookieStore(PersistentCookieStore cookieStore) {
        getClient().setCookieStore(cookieStore);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().post(url, params, responseHandler);
    }

    /**
     * @return an async client when calling from the main thread, otherwise a sync client.
     */
    private static AsyncHttpClient getClient()
    {
        // Return the synchronous HTTP client when the thread is not prepared
        if (Looper.myLooper() == null)
            return syncHttpClient;
        return asyncHttpClient;
    }
}
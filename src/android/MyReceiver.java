package com.wistron.ptsApp.jpush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.wistron.ptsApp.PushPlugin;
import com.wistron.swpc.pts2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by king on 2017/10/30.
 * Edited by Anne on 2018/9/3
 */

public class MyReceiver extends BroadcastReceiver {
  private static final String TAG = MyReceiver.class.getSimpleName();
  private SharedPreferences sharedPreferences;
  private SharedPreferences.Editor editor;
  public static String LOGINDATA = "LoginData";
  private String MessageUrl = "";

  @Override
  public void onReceive(Context context, Intent intent) {

    // 保存账号密码
    sharedPreferences = context.getSharedPreferences(LOGINDATA,
      context.MODE_MULTI_PROCESS);
    editor = sharedPreferences.edit();
    try {
      Bundle bundle = intent.getExtras();
      Log.i(TAG, "[MyReceiver] onReceive - " + intent.getAction());
      if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
        Log.i(TAG, "[MyReceiver] get Registration Id : " + regId);
        editor.putString("Registration_Id", regId);
        editor.commit();

      } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        Log.i(TAG, "[MyReceiver] Received jpush CustomMessages: " + printBundle(bundle));
        parseCustomMessage(context, bundle);

      } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
        Log.i(TAG, "[MyReceiver]  Received jpush notify");
        int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        Log.i(TAG, "[MyReceiver] Received notifyID: " + notifactionId);

      } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
        Log.i(TAG, "[MyReceiver] click and open notify");

      } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
        String url = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        Log.i(TAG, "[MyReceiver] user Received RICH PUSH CALLBACK: " + url);
        //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

      } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);

      } else if ("com.wistron.swpc.pts2.action.NOTIFICATION_CLICKED".equals(intent.getAction())) {//点击推送消息
//        String url = intent.getStringExtra("MessageUrl");
        String data = intent.getStringExtra("JSONData");
        int type = intent.getIntExtra(PushPlugin.TYPE, -1);
        Intent intentMainActivity = new Intent();
        intentMainActivity.setComponent(new ComponentName("com.wistron.swpc.pts2", "com.wistron.swpc.pts2.MainActivity"));
        intentMainActivity.setAction(Intent.ACTION_VIEW);
        intentMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(intentMainActivity);
//        PushPlugin.setNotifyUrl(intent.getStringExtra("MessageUrl"));
        PushPlugin.setNotifyData(data);
        if (type != -1) {
          NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
          notificationManager.cancel(type);
        }
      } else {
        Log.i(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }


  // 打印所有的 intent extra 数据
  private static String printBundle(Bundle bundle) {
    StringBuilder sb = new StringBuilder();
    for (String key : bundle.keySet()) {
      if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
        sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
      } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
        sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
      } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
        if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
          Log.i(TAG, "This message has no Extra data");
          continue;
        }

        try {
          JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
          Iterator<String> it = json.keys();

          while (it.hasNext()) {
            String myKey = it.next();
            sb.append("\nkey:" + key + ", value: [" +
              myKey + " - " + json.optString(myKey) + "]");
          }
        } catch (JSONException e) {
          Log.e(TAG, "Get message extra JSON error!");
        }

      } else {
        sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
      }
    }
    return sb.toString();
  }

  /**
   * 处理自定义消息
   *
   * @param context
   * @param bundle  jpush推送的bundle对象
   */
  private void parseCustomMessage(Context context, Bundle bundle) {
    int requestCode = (int) System.currentTimeMillis();
    String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
    JSONObject json = null;
    String MsgTitle = "";
    String body = "";
    String badge = "";
    String extras = "";
    String subsystem = "";
    String url = "";
    try {
      json = new JSONObject(message);
      MsgTitle = json.getString("title");
      body = json.getString("body");
      badge = json.getString("badge");
      extras = json.getString("extras");
      Log.i(TAG, "MsgTitle=" + MsgTitle + ",body=" + body + "badge=" + badge + ",extras=" + extras);
      JSONObject extrasJson = new JSONObject(extras);
      subsystem = extrasJson.getString("subsystem");
      url = extrasJson.getString("url");
      Log.i(TAG, "subsystem=" + subsystem + ",url=" + url);
      if (!"".equals(body)) {
        MessageUrl = url;
        Intent intentClick = new Intent(context, MyReceiver.class);
        intentClick.setAction("com.wistron.swpc.pts2.action.NOTIFICATION_CLICKED");
        intentClick.putExtra("MessageUrl", url);
        intentClick.putExtra("JSONData", message);
        intentClick.putExtra(PushPlugin.TYPE, requestCode);
        PendingIntent pendingIntentClick = PendingIntent.getBroadcast(context, requestCode, intentClick, PendingIntent.FLAG_ONE_SHOT);

        Intent intentCancel = new Intent(context, MyReceiver.class);
        intentCancel.setAction("com.wistron.swpc.pts2.action.NOTIFICATION_CANCELLED");
        intentCancel.putExtra("MessageUrl", url);
        intentCancel.putExtra(PushPlugin.TYPE, requestCode);
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(context, requestCode, intentCancel, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
          .setContentText(body)
          .setSmallIcon(R.mipmap.icon)
          .setContentTitle(MsgTitle)
          .setSound(defaultSoundUri)//设置提示声音
          .setContentIntent(pendingIntentClick)
          .setDeleteIntent(pendingIntentCancel);
        ;
        Notification notification = builder.build();
        notificationManager.notify(requestCode, notification);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    } catch (NumberFormatException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }


}

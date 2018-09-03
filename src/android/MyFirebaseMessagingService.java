package com.wistron.ptsApp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wistron.ptsApp.MyLog;
import com.wistron.ptsApp.jpush.MyReceiver;
import com.wistron.swpc.pts2.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by king on 2017/10/26.
 * Edited by Anne on 2017/9/3
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
  private static final String TAG = "MyFirebaseMsgService";

  @Override
  public void onDeletedMessages() {
    Log.i(TAG, "onDeletedMessages= ");
    super.onDeletedMessages();
  }

  @Override
  public void onMessageSent(String s) {
    Log.i(TAG, "onMessageSent= " + s);
    super.onMessageSent(s);
  }

  @Override
  public void onSendError(String s, Exception e) {
    Log.i(TAG, "onSendError= " + s);
    super.onSendError(s, e);
  }

  @Override
  public void handleIntent(Intent intent) {
    //此处拦截  防止app在后台运行时 fcm调用系统的notifcation显示
    Log.i(TAG, "handleIntent= " + intent.getExtras().getString("gcm.notification.body"));
    sendNotification(intent.getExtras().getString("gcm.notification.body"));
//        super.handleIntent(intent);
  }

  /**
   * Called when message is received.
   *
   * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
   */
  // [START receive_message]
  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    MyLog.d(TAG, "From: " + remoteMessage.getFrom());

    if (remoteMessage.getData().size() > 0) {
      MyLog.d(TAG, "Message data payload: " + remoteMessage.getData());

      if (/* Check if data needs to be processed by long running job */ true) {
        // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
        scheduleJob();
      } else {
        // Handle message within 10 seconds
        handleNow();
      }

    }

    // Check if message contains a notification payload.
    if (remoteMessage.getNotification() != null) {
      MyLog.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
      sendNotification(remoteMessage.getNotification().getBody());
    }
  }

  // [END receive_message]

  /**
   * Schedule a job using FirebaseJobDispatcher.
   */
  private void scheduleJob() {
    // [START dispatch_job]
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
//        Job myJob = dispatcher.newJobBuilder()
//                .setService(MyJobService.class)
//                .setTag("my-job-tag")
//                .build();
//        dispatcher.schedule(myJob);
    // [END dispatch_job]
  }

  /**
   * Handle time allotted to BroadcastReceivers.
   */
  private void handleNow() {
    MyLog.d(TAG, "Short lived task is done.");
  }

  /**
   * Create and show a simple notification containing the received FCM message.
   *
   * @param messageBody FCM message body received.
   */
  private void sendNotification(String messageBody) {
    int requestCode = (int) System.currentTimeMillis();
    JSONObject json = null;
    String MsgTitle = "";
    String body = "";
    String badge = "";
    String extras = "";
    String subsystem = "";
    String url = "";
    try {
      json = new JSONObject(messageBody);
      MsgTitle = json.getString("title");
      body = json.getString("body");
      badge = json.getString("badge");
      extras = json.getString("extras");
      Log.i(TAG, "MsgTitle=" + MsgTitle + ",body=" + body + "badge=" + badge + ",extras=" + extras);
      JSONObject extrasJson = new JSONObject(extras);
      Log.e(TAG, "extrasJson=" + extrasJson.toString());
      subsystem = extrasJson.getString("subsystem");
      url = extrasJson.getString("url");
      Log.i(TAG, "subsystem=" + subsystem + ",url=" + url);

      if (!"".equals(body)) {
        Intent intentClick = new Intent(this, MyReceiver.class);
        intentClick.setAction("com.wistron.swpc.pts2.action.NOTIFICATION_CLICKED");
        intentClick.putExtra("MessageUrl", url);
        intentClick.putExtra("JSONData", messageBody);
        intentClick.putExtra(PushPlugin.TYPE, requestCode);
        PendingIntent pendingIntentClick = PendingIntent.getBroadcast(this, requestCode, intentClick, PendingIntent.FLAG_ONE_SHOT);

        Intent intentCancel = new Intent(this, MyReceiver.class);
        intentCancel.setAction("com.wistron.swpc.pts2.action.NOTIFICATION_CANCELLED");
        intentCancel.putExtra("MessageUrl", url);
        intentCancel.putExtra(PushPlugin.TYPE, requestCode);
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this, requestCode, intentCancel, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
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

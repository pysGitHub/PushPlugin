package com.wistron.ptsApp;

import android.content.Context;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.SharedPreferences;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.wistron.ptsApp.jpush.MyReceiver;

import cn.jpush.android.api.JPushInterface;

/**
 * This class echoes a string called from JavaScript.
 */
public class PushPlugin extends CordovaPlugin {
  public static final String TAG = PushPlugin.class.getSimpleName();
  public static final String TYPE = "type";
  private Context mContext;
  static CallbackContext pushUrlCallbackContext;
  static CallbackContext pushTokenCallbackContext;
  private SharedPreferences sharedPreferences;

  /**
   * 回传推送消息页面的url到js
   *
   * @param Url 解析推送消息得到的页面url
   */
  public static void setNotifyUrl(String Url) {
    MyLog.d(TAG, "PushPlugin getNotifyUrl=======" + Url);
    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, Url);
    pluginResult.setKeepCallback(true);
    pushUrlCallbackContext.sendPluginResult(pluginResult);

  }


  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
    mContext = cordova.getActivity().getApplicationContext();
    sharedPreferences = mContext.getSharedPreferences(MyReceiver.LOGINDATA, mContext.MODE_MULTI_PROCESS);
    JPushInterface.init(mContext);//初始化极光推送插件
    JPushInterface.setDebugMode(true);
    MyLog.d(TAG, "==> FCMPlugin initialize");
    FirebaseApp.initializeApp(mContext);
    FirebaseMessaging.getInstance().subscribeToTopic("android");
    FirebaseMessaging.getInstance().subscribeToTopic("all");
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

    if (action.equals("initPushMethod")) {
      JPushInterface.init(mContext);//初始化极光推送插件
      JPushInterface.setDebugMode(true);
      return true;
    }
    if (action.equals("receivePushUrl")) {
      this.pushUrlCallbackContext = callbackContext;
      return true;
    } else if (action.equals("getPushToken")) {
      this.getPushToken(callbackContext);
      return true;
    }
    return false;
  }

  private void getPushToken(CallbackContext callbackContext) {
    String localJPushToken = sharedPreferences.getString("Registration_Id", "isEmpty");
    String localFcmToken =  sharedPreferences.getString("Registration_Id_Fcm", "isEmpty");
    if (!localFcmToken.equals("isEmpty") && localFcmToken.length() > 0) {
      callbackContext.success(localFcmToken);
    } else {
      if (!localJPushToken.equals("isEmpty") && localJPushToken.length() > 0) {
        callbackContext.success(localJPushToken);
      }else 
      callbackContext.error("isEmpty");
    }


  }

}

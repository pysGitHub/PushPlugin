package com.wistron.ptsApp;




import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.wistron.ptsApp.MyLog;

/**
 * Created by king on 2017/10/26.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIdService";
    public static String LOGINDATA = "LoginData";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        MyLog.i(TAG, "Refreshed token: " + refreshedToken);
        saveRegistrationToken(refreshedToken);
    }

    /**
     * 保存token
     * @param refreshedToken  注册返回的令牌
     */
    private void saveRegistrationToken(String refreshedToken) {
        sharedPreferences = getSharedPreferences(LOGINDATA, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("Registration_Id_Fcm", refreshedToken);
        editor.commit();
    }

}

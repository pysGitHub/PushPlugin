package com.wistron.ptsApp;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by s1704002 on 2017/11/06.
 */

public class MyLog {
    public static void i(String TAG,String message){
        Log.i(TAG,message);
//        appendLog("I",TAG,message);
    }
    public static void d(String TAG,String message){
        Log.d(TAG,message);
//        appendLog("D",TAG,message);
    }
    public static void e(String TAG,String message){
        Log.e(TAG,message);
//        appendLog("E",TAG,message);
    }
    public static void v(String TAG,String message){
        Log.v(TAG,message);
//        appendLog("V",TAG,message);
    }
    public static void w(String TAG,String message){
        Log.w(TAG,message);
//        appendLog("W",TAG,message);
    }

//    private static void appendLog(String level, String tag, String message) {
//        SimpleDateFormat mSimpleDateFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = new Date(System.currentTimeMillis());
//        String time = mSimpleDateFormat.format(date);
//
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){//判断sd卡是否存在
//            String path = Environment.getExternalStorageDirectory()+"/WistronPTSApp";
////            getExternalFilesDir(null).toString()
//           createLogcatFile(date,time,level,tag,message,path);
//        }else {//sd卡不存在 则存到内置存储中
//            Log.i("myLog","sdcard unmounted");
//            String path = "/mnt/sdcard/PTSA";
//            createLogcatFile(date,time,level,tag,message,path);
//        }
//
//    }
//
//    private static void createLogcatFile(Date date, String time, String level, String tag, String message,String path) {
//        File mDir = new File(path);
//        if (!mDir.exists()){
//            Log.i("myLog","path="+path);
////            mDir.mkdir();
//            Log.i("myLog","mkdir="+mDir.mkdir());
////            mDir.mkdirs();
//        }
//        File mFile = new File(path , "/logcat.txt");
//        if (!mFile.exists()){
//            try {
//                Log.i("myLog","mFile="+mFile.getPath());
//                mFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        try {
//            BufferedWriter bf = null;
//            Date preDate = new Date(mFile.lastModified());
//            if (preDate.getDate() != date.getDate()){//文件最后修改的时间和文件的创建时间不相等 此时覆盖原来的log 重新写入
//                bf = new BufferedWriter(new FileWriter(mFile,false));
//            }else {
//                bf = new BufferedWriter(new FileWriter(mFile,true));
//            }
//            bf.append(time + " "+ level + "/" + tag  + "(" + android.os.Process.myPid() + "): " + message);
//            bf.newLine();
//            bf.flush();
//            bf.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}

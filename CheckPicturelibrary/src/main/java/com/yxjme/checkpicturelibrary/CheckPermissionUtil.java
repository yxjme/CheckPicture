package com.yxjme.checkpicturelibrary;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import java.util.ArrayList;
import java.util.List;


public class CheckPermissionUtil {


    public static final int REQUEST_PHONE_PERMISSIONS = 0x123  ;

    /**
     * @param activity
     */
  public static void initPermission(Activity activity) {
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.CAMERA);
            if ((activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
            if ((activity.checkSelfPermission(Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WAKE_LOCK);
            if ((activity.checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);

            if (permissionsList.size() != 0) {
                activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_PHONE_PERMISSIONS);
            }
        }
    }





    /**
     * 请求权限
     *
     * @param activity
     * @param permission
     * @param requestPermissionsInterface
     */
    public static void requestPermission(Activity activity, String permission, requestPermissionsInterface requestPermissionsInterface ){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(activity,permission)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity,new String[]{permission},REQUEST_PHONE_PERMISSIONS);
                requestPermissionsInterface.isRequest();
            }else {
                requestPermissionsInterface.notRequest();
            }
        }else {
            requestPermissionsInterface.notRequest();
        }
    }



    public interface  requestPermissionsInterface{
        void isRequest();
        void notRequest();
    }


    /**
     * 检测权限
     *
     * @param activity
     * @param permission
     */
    public static void checkPermission(Activity activity, String permission, requestPermissionsInterface requestPermissionsInterface) {
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(permission);
            if (permissionsList.size() != 0) {
                requestPermissionsInterface.isRequest();
                activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_PHONE_PERMISSIONS);
            }else {
                requestPermissionsInterface.notRequest();
            }
        }else {
            requestPermissionsInterface.notRequest();
        }
    }

    /**
     * 检测权限
     *
     * @param activity
     */
    public static void checkPermission(Activity activity, List<String> list, requestPermissionsInterface requestPermissionsInterface) {
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M&&list!=null&&list.size()>0) {
            for (String p:list){
                if ((activity.checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED))
                    permissionsList.add(p);
            }

            if (permissionsList.size() != 0) {
                requestPermissionsInterface.isRequest();
                activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_PHONE_PERMISSIONS);
            }else {
                requestPermissionsInterface.notRequest();
            }
        }else {
            requestPermissionsInterface.notRequest();
        }
    }


    /**
     *
     * @param activity
     */
    public static void checkPermission(Activity activity, requestPermissionsInterface callPermission) {
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((activity.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.CALL_PHONE);
            if ((activity.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.CAMERA);
            if ((activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if ((activity.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.ACCESS_NETWORK_STATE);
            if ((activity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.READ_PHONE_STATE);
            if (permissionsList.size() != 0) {
                activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_PHONE_PERMISSIONS);
                callPermission.isRequest();
            }else {
                callPermission.notRequest();
            }
        }else {
            callPermission.notRequest();
        }
    }
}

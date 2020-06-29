package com.yxjme.checkpicturelibrary;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.IOException;
import static android.net.Uri.fromFile;

public class CheckPictureUtil {


    static String photo = "checkPhotoLib";
    /**拍照上传图片*/
    public static final int TAKE_PHOTO = 1;
    /**选择相册图片*/
    public static final int CHECKED_PHOTO = 2;



    /**
     * 相册
     * @param activity
     */
    public static void onPickPicture(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, CHECKED_PHOTO);
    }



    private static String getPhotoPath(Activity activity){
        String p= getSaveDir(activity)+ File.separator+photo;
        File files=new File(getSaveDir(activity)+ File.separator+ photo);
        if(!files.exists()){
            files.mkdir();
        }
        return p ;
    }



    /**
     * 拍照  返回文件
     *
     * @param activity
     */
    public static void onTakePhotoFile(Activity activity,String packAge, CallBack<File> callBack) {
        Uri outputFileUri ;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(getPhotoPath(activity), "p.jpg");
            if(file.exists()){
                file.delete();
            }
            if (Build.VERSION.SDK_INT >= 24) {
                outputFileUri = FileProvider.getUriForFile(activity.getApplicationContext(), packAge+".fileProvider",  file);
            } else {
                outputFileUri = fromFile(file);
            }

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            activity.startActivityForResult(intent, TAKE_PHOTO);
            callBack.result(file);
        } else {
            callBack.error("您的设备不支持此功能！");
        }
    }




    /**
     * 拍照
     *
     * @param activity
     */
    public static void onTakePhotoUri(Activity activity,String packAge, CallBack<Uri> callBack) {
        Uri outputFileUri ;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(getPhotoPath(activity), "p.jpg");
            if(file.exists()){
                file.delete();
            }
            if (Build.VERSION.SDK_INT >= 24) {
                outputFileUri = FileProvider.getUriForFile(activity.getApplicationContext(), packAge+".fileProvider",  file);
            } else {
                outputFileUri = fromFile(file);
            }

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            activity.startActivityForResult(intent, TAKE_PHOTO);

            callBack.result(outputFileUri);
        } else {
            callBack.error("您的设备不支持此功能！");
        }
    }



    /**
     * 拍照
     *
     * @param activity
     */
    public static Uri onTakePhoto(Activity activity,String packAge) {
        Uri outputFileUri ;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(getPhotoPath(activity), System.currentTimeMillis()+".jpg");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= 24) {
                outputFileUri = FileProvider.getUriForFile(activity.getApplicationContext(), packAge+".fileProvider",  file);
            } else {
                outputFileUri = fromFile(file);
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            activity.startActivityForResult(intent, TAKE_PHOTO);
            return outputFileUri;
        } else {
            Toast.makeText(activity,"您的设备不支持此功能",Toast.LENGTH_LONG).show();
        }
        return null ;
    }





    /**
     * 本地文件保存路径
     *
     * @return
     */
    public static String getSaveDir(Context context) {
        String fileDir;
        // 判断SD卡是否存在
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            fileDir = Environment.getExternalStorageDirectory().getPath();
            if (isEmptyStr(fileDir)) {
//                Log.e(TAG, "-------------->SD card path == null !");
                File f = context.getExternalCacheDir();
                fileDir = f.getPath();
            }
        } else {
//            Log.e(TAG, "-------------->SD card not mounted!");
            File f = context.getCacheDir();
            fileDir = f.getPath();
        }
        return fileDir;
    }


    /**
     * 判断字符串是否为空
     *
     * @param string
     * @return
     */
    public static boolean isEmptyStr(CharSequence string) {
        if (TextUtils.isEmpty(string) || "null".equals(string)) {
            return true;
        } else {
            return false;
        }
    }



    public static File uriToFile(Uri uri, Context context) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA }, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2以后
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
            //Log.i(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }



    /**
     * 路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri )
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] {MediaStore.Images.ImageColumns.DATA
            }, null, null, null ); if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) { data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}

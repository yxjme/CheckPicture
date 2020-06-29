package com.yxjme.checkpicture;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.yxjme.checkpicturelibrary.CallBack;
import com.yxjme.checkpicturelibrary.CheckPermissionUtil;
import com.yxjme.checkpicturelibrary.CheckPictureDialog;
import com.yxjme.checkpicturelibrary.CheckPictureUtil;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ImageView image ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.image);
    }



    public void btn(View view){

        List<String> list = new ArrayList<>();
        list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        list.add(Manifest.permission.CAMERA);
        CheckPermissionUtil.checkPermission(this, list, new CheckPermissionUtil.requestPermissionsInterface() {
            @Override
            public void isRequest() {

            }

            @Override
            public void notRequest() {
                CheckPictureDialog dialog=new CheckPictureDialog(MainActivity.this);
                dialog.AddUserHeadListener(new CheckPictureDialog.AddUserHeadListener() {
                    @Override
                    public void onPickPicture() {
                        CheckPictureUtil.onPickPicture(MainActivity.this);
                    }

                    @Override
                    public void onTakePhoto() {
                        CheckPictureUtil.onTakePhotoUri(MainActivity.this, getPackageName(), new CallBack<Uri>() {
                            @Override
                            public void result(Uri uri) {
                                outFileUri = uri ;
                            }

                            @Override
                            public void error(String s) {

                            }
                        });
                    }
                });
                dialog.show();
            }
        });
    }



    Uri outFileUri;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                //选择相册图片上传
                case CheckPictureUtil.CHECKED_PHOTO:
                    image.setImageURI(intent.getData());
                    break;
                //拍照上传图片
                case CheckPictureUtil.TAKE_PHOTO:
                    image.setImageURI(outFileUri);
                    break;
                //剪切图片上传
//                case UCrop.REQUEST_CROP:
//                    resultUri = UCrop.getOutput(intent);
//                    break;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
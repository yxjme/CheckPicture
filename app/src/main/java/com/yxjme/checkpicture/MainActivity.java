package com.yxjme.checkpicture;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.yxjme.checkpicturelibrary.CallBack;
import com.yxjme.checkpicturelibrary.CheckPermissionUtil;
import com.yxjme.checkpicturelibrary.CheckPictureDialog;
import com.yxjme.checkpicturelibrary.CheckPictureUtil;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ImageView image ;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.image);


        final NestedScrollView mNestedScrollView = findViewById(R.id.mNestedScrollView);
        final FloatButton floatButton = new FloatButton(this);
        floatButton.init(this,Gravity.BOTTOM|Gravity.RIGHT);
        floatButton.setSrc(R.mipmap.icon_search);
        mNestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                floatButton.move(i1);
            }
        });
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNestedScrollView.setScrollY(0);
            }
        });
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
                                Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                dialog.show();
            }
        });
    }



    Uri outFileUri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent intent) {
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
}
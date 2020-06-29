package com.yxjme.checkpicturelibrary;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class CheckPictureDialog extends Dialog implements View.OnClickListener {

    private TextView pick_picture;
    private TextView take_photo;
    private TextView cancel;

    public CheckPictureDialog(Context context) {
        super(context,R.style.dialog_0);
        setContentView(R.layout.dialog_edit_user_head_picture_layout);
        initView();
        initWindow();
    }


    private void initView() {
        pick_picture = findViewById(R.id.pick_picture);
        pick_picture.setOnClickListener(this);
        take_photo = findViewById(R.id.take_photo);
        take_photo.setOnClickListener(this);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
    }


    /**
     * 是否支持拍照
     * @param b
     * @return
     */
    public CheckPictureDialog isSupportTakePhoto(boolean b){
        if (b){
            take_photo.setVisibility(View.VISIBLE);
        }else {
            take_photo.setVisibility(View.GONE);
        }
        return CheckPictureDialog.this ;
    }


    private void initWindow() {
        Window window = getWindow();
        window .setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams l = window.getAttributes();
        l.width= WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(l);
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.pick_picture) {
            if (addUserHeadListener == null) return;
            addUserHeadListener.onPickPicture();
            dismiss();
        } else if (id == R.id.take_photo) {
            if (addUserHeadListener == null) return;
            addUserHeadListener.onTakePhoto();
            dismiss();
        } else if (id == R.id.cancel) {
            dismiss();
        }
    }




    private AddUserHeadListener addUserHeadListener ;
    public interface AddUserHeadListener{
        void onPickPicture();
        void onTakePhoto();
    }


    public CheckPictureDialog AddUserHeadListener(AddUserHeadListener addUserHeadListener) {
        this.addUserHeadListener = addUserHeadListener;
        return CheckPictureDialog.this;
    }
}

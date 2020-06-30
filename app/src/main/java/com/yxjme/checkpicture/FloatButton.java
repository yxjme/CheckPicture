package com.yxjme.checkpicture;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FloatButton extends LinearLayout {

    private ImageView imageView;
    private int maxMoveY=500;

    public FloatButton( Context context) {
        this(context,null);
    }

    public FloatButton( Context context,  AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FloatButton( Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        imageView = new ImageView(getContext());
        setGravity(Gravity.CENTER);
        addView(imageView);
    }


    public void setSrc(int id){
        if (imageView!=null){
            imageView.setImageResource(id);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void init(Activity activity, int gravity){
        FrameLayout rootView= (FrameLayout) activity.getWindow().getDecorView();
        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = gravity;
        layoutParams.width = dpToPx(60);
        layoutParams.height =  dpToPx(60);
        rootView.addView(this,layoutParams);


        GradientDrawable gradientDrawable=new GradientDrawable();
        gradientDrawable.setCornerRadius(dpToPx(100));
        gradientDrawable.setColor(Color.WHITE);
        setBackground(gradientDrawable);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            setTranslationZ(10);
        }

        anim(1,0);
        isV = false ;
        isG = true ;
    }



    /**
     * @param dy
     */
    public void move(int dy){
        if (dy>maxMoveY){
            if (!isV){
                anim(0,1);
            }
            isV = true ;
            isG = false ;
        }else {
            if (!isG){
                anim(1,0);
            }
            isV = false ;
            isG = true ;
        }
    }


    boolean isV = false ;
    boolean isG = true ;

    private void anim(final float s, float e){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(s,e);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float scale = (float) valueAnimator.getAnimatedValue();
                MarginLayoutParams marginLayoutParams= (MarginLayoutParams) getLayoutParams();
                marginLayoutParams.rightMargin = dpToPx(20);
                marginLayoutParams.bottomMargin = (int) (dpToPx(40) *scale);
                setLayoutParams(marginLayoutParams);

                setScaleX(scale);
                setScaleY(scale);
            }
        });
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setDuration(300);
        valueAnimator.start();
    }


    public int  dpToPx(int dp){
        float density = getContext().getResources().getDisplayMetrics().density ;
        return (int) (density * dp + 0.5f);
    }



}

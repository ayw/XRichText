package com.sendtion.xrichtextdemo.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.sendtion.xrichtextdemo.R;

/**
 * Created by HH on 2017/12/25.
 */

public class PictureDialog extends Dialog implements android.view.View.OnClickListener {

    private OnHeadListener mListener;
    private LinearLayout linearLayout;

    public PictureDialog(Context context) {
        super(context);
    }

    public PictureDialog(Context context, OnHeadListener listener, int theme) {
        super(context, theme);
        this.mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_choose_picture);
        init();
    }

    public void init() {
        findViewById(R.id.ll_xiangce).setOnClickListener(this);
        findViewById(R.id.ll_paizhao).setOnClickListener(this);
        findViewById(R.id.cancelLL).setOnClickListener(this);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_xiangce:
                if (ClickTimeUtils.isFastDoubleClick()) {
                    return;
                } else {
                    mListener.getText(false, 102);
                    this.dismiss();
                }
                break;
            case R.id.ll_paizhao:
                if (ClickTimeUtils.isFastDoubleClick()) {
                    return;
                } else {
                    mListener.getText(true, 102);
                    this.dismiss();
                }
                break;

            case R.id.cancelLL:
                this.dismiss();
                break;
        }
    }

    public interface OnHeadListener {
        void getText(boolean state, int param);
    }

    @Override
    public void show() {
        super.show();
        animationShow(400);
    }

    @Override
    public void dismiss() {
        // super.dismiss();
        animationHide(400);
    }

    private void animationShow(int mDuration) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(linearLayout, "translationY", 1000, 0).setDuration
                (mDuration));
        animatorSet.start();
    }


    private void animationHide(int mDuration) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ObjectAnimator.ofFloat(linearLayout, "translationY", 0, 1000).setDuration
                (mDuration));
        animatorSet.start();

        animatorSet.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                PictureDialog.super.dismiss();
            }


            @Override
            public void onAnimationCancel(Animator animator) {
            }


            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
    }
}

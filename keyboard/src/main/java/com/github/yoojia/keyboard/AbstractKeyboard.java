package com.github.yoojia.keyboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * @author YOOJIA.CHEN (yoojia.chen@gmail.com)
 */
class AbstractKeyboard {

    private final Context mContext;
    private final PopupWindow mPopupWindow;

    protected final OnKeyActionListener mOnKeyActionListener;

    public AbstractKeyboard(Context context, OnKeyActionListener onKeyActionListener) {
        mContext = context;
        if (onKeyActionListener == null) {
            throw new NullPointerException("onKeyActionListener == null");
        }
        mOnKeyActionListener = onKeyActionListener;
        mPopupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));
    }

    protected View putContentView(int layoutResId) {
        final View view = View.inflate(mContext, layoutResId, null);
        mPopupWindow.setContentView(view);
        return view;
    }

    public void show(final View anchorView) {
        mPopupWindow.showAtLocation(anchorView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        onShow();
    }

    protected void onShow(){ }

    public void dismiss(){
        mPopupWindow.dismiss();
    }

    protected String getInput(TextView[] inputs) {
        final StringBuilder buff = new StringBuilder(inputs.length);
        for (TextView item : inputs){
            String text = String.valueOf(item.getText());
            if (! TextUtils.isEmpty(text)){
                buff.append(text);
            }
        }
        return buff.toString();
    }

}

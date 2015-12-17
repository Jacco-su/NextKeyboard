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

    protected final OnCommitListener mCommitListener;

    public AbstractKeyboard(Context context, OnCommitListener commitListener) {
        mContext = context;
        mCommitListener = commitListener;
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
        final StringBuilder value = new StringBuilder(inputs.length);
        for (TextView item : inputs){
            if (! TextUtils.isEmpty(item.getText())){
                value.append(item.getText());
            }
        }
        return value.toString();
    }

}

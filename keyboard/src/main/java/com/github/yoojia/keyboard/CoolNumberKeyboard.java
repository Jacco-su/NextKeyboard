package com.github.yoojia.keyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 车辆号码键盘
 *
 * @author  yoojia.chen@gmail.com
 * @version version 2015-04-24
 * @since   1.0
 */
public class CoolNumberKeyboard {

    private static final int NUMBER_LEN = 7;

    private static final String CHINESE = "|京津晋冀蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼渝川贵云藏陕甘青宁新|港澳警学挂";

    private final Context mContext;
    private final PopupWindow mPopupWindow;
    private final KeyboardView mKeyboardView;
    private final TextView[] mNumber = new TextView[NUMBER_LEN];

    private final OnKeyboardActionHandler mKeyboardActionHandler = new OnKeyboardActionHandler(){
        @Override
        public void onKey(int charCode, int[] keyCodes) {
            if (charCode >= 400){ // 400 See keyboard xml
                charCode = CHINESE.charAt(charCode - 400);
            }
            mSelectedTextView.setText(Character.toString((char) charCode));
            autoNextNumber();
        }
    };

    private final View.OnClickListener mNumberSelectedHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mSelectedTextView != null){
                mSelectedTextView.setActivated(false);
            }
            mSelectedTextView = (TextView) view;
            mSelectedTextView.setActivated(true);
            int id = view.getId();
            if (id == R.id.keyboard_number_0) {
                if (mShowKeyboard != R.xml.keyboard_province) {
                    mShowKeyboard = R.xml.keyboard_province;
                    showProvince();
                }
            } else if (id == R.id.keyboard_number_1) {
                if (mShowKeyboard != R.xml.keyboard_city_code) {
                    mShowKeyboard = R.xml.keyboard_city_code;
                    showCityCode();
                }
            } else if (id == R.id.keyboard_number_6) {
                if (mShowKeyboard != R.xml.keyboard_number_extra) {
                    mShowKeyboard = R.xml.keyboard_number_extra;
                    showNumberExtra();
                }
            } else {
                if (mShowKeyboard != R.xml.keyboard_number) {
                    mShowKeyboard = R.xml.keyboard_number;
                    showNumber();
                }
            }
            mKeyboardView.invalidateAllKeys();
            mKeyboardView.invalidate();
        }
    };

    private final View.OnClickListener mCommitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            StringBuilder number = new StringBuilder(NUMBER_LEN);
            for (TextView i : mNumber){
                if (!" ".equals(i.getText())){
                    number.append(i.getText());
                }
            }
            if (number.length() == NUMBER_LEN){
                mCommitListener.onCommit(number.toString());
                dismiss();
            }
        }
    };

    private int mShowKeyboard = 0;
    private TextView mSelectedTextView;
    private OnNumberCommitListener mCommitListener;

    public CoolNumberKeyboard(Context context) {
        mContext = context;
        final View contentView = View.inflate(context, R.layout.vehicle_keyboard, null);

        mNumber[0] = (TextView) contentView.findViewById(R.id.keyboard_number_0);
        mNumber[1] = (TextView) contentView.findViewById(R.id.keyboard_number_1);
        mNumber[2] = (TextView) contentView.findViewById(R.id.keyboard_number_2);
        mNumber[3] = (TextView) contentView.findViewById(R.id.keyboard_number_3);
        mNumber[4] = (TextView) contentView.findViewById(R.id.keyboard_number_4);
        mNumber[5] = (TextView) contentView.findViewById(R.id.keyboard_number_5);
        mNumber[6] = (TextView) contentView.findViewById(R.id.keyboard_number_6);

        for (TextView m : mNumber) m.setOnClickListener(mNumberSelectedHandler);

        mKeyboardView = (KeyboardView) contentView.findViewById(R.id.keyboard_view);
        mKeyboardView.setOnKeyboardActionListener(mKeyboardActionHandler);
        mKeyboardView.setPreviewEnabled(false);// !!! Must be false

        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));

        View commit = contentView.findViewById(R.id.keyboard_commit);
        commit.setOnClickListener(mCommitClickListener);
    }


    /**
     * 显示车牌输入法
     * @param activity Activity
     */
    public void show(Activity activity, String givenNumber, OnNumberCommitListener commitListener){
        mCommitListener = commitListener;
        show(activity.getWindow().getDecorView().getRootView());
        if (TextUtils.isEmpty(givenNumber)){
            for (TextView i : mNumber) i.setText(" ");
        }else if (NUMBER_LEN != givenNumber.length()){
            throw new IllegalArgumentException("Illegal vehicle number length");
        }else{
            char[] numbers = givenNumber.toUpperCase().toCharArray();
            for (int i = 0;i<NUMBER_LEN;i++){
                mNumber[i].setText(Character.toString(numbers[i]));
            }
        }
    }

    public void dismiss(){
        mPopupWindow.dismiss();
    }

    private void showProvince(){
        Keyboard keyboard = new Keyboard(mContext, R.xml.keyboard_province);
        mKeyboardView.setKeyboard(keyboard);
    }

    private void showCityCode(){
        Keyboard keyboard = new Keyboard(mContext, R.xml.keyboard_city_code);
        mKeyboardView.setKeyboard(keyboard);
    }

    private void showNumber(){
        Keyboard keyboard = new Keyboard(mContext, R.xml.keyboard_number);
        mKeyboardView.setKeyboard(keyboard);
    }

    private void showNumberExtra(){
        Keyboard keyboard = new Keyboard(mContext, R.xml.keyboard_number_extra);
        mKeyboardView.setKeyboard(keyboard);
    }

    /**
     * 自动跳转到下一个输入框
     */
    private void autoNextNumber(){
        int numberId = mSelectedTextView.getId();
        if (numberId == R.id.keyboard_number_0) {
            mNumber[1].performClick();

        } else if (numberId == R.id.keyboard_number_1) {
            mNumber[2].performClick();

        } else if (numberId == R.id.keyboard_number_2) {
            mNumber[3].performClick();

        } else if (numberId == R.id.keyboard_number_3) {
            mNumber[4].performClick();

        } else if (numberId == R.id.keyboard_number_4) {
            mNumber[5].performClick();

        } else if (numberId == R.id.keyboard_number_5) {
            mNumber[6].performClick();

        } else if (numberId == R.id.keyboard_number_6) {
            // nop
        }
    }

    private void show(View anchorView){
        mPopupWindow.showAtLocation(anchorView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mNumber[0].performClick();
    }

}

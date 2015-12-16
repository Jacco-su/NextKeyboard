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
public class VehiclePlateKeyboard {

    private static final int NUMBER_LENGTH = 7;

    private static final String CHINESE = "|京津晋冀蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼渝川贵云藏陕甘青宁新|港澳警学挂";

    private final Context mContext;
    private final PopupWindow mPopupWindow;
    private final KeyboardView mKeyboardView;
    private final TextView[] mNumber = new TextView[NUMBER_LENGTH];

    private final boolean mAutoCommit;
    private View mCommitButton;
    private int mShowKeyboard = 0;
    private TextView mSelectedTextView;
    private OnNumberCommitListener mCommitListener;

    private Keyboard mProvinceKeyboard;
    private Keyboard mCityCodeKeyboard;
    private Keyboard mNumberKeyboard;
    private Keyboard mNumberExtraKeyboard;

    public VehiclePlateKeyboard(Context context) {
        this(context, false);
    }

    public VehiclePlateKeyboard(Context context, boolean autoCommit) {
        mContext = context;
        mAutoCommit = autoCommit;
        final View contentView = View.inflate(context, R.layout.next_keyboard_vehicle_plate, null);

        mNumber[0] = (TextView) contentView.findViewById(R.id.keyboard_number_0);
        mNumber[1] = (TextView) contentView.findViewById(R.id.keyboard_number_1);
        mNumber[2] = (TextView) contentView.findViewById(R.id.keyboard_number_2);
        mNumber[3] = (TextView) contentView.findViewById(R.id.keyboard_number_3);
        mNumber[4] = (TextView) contentView.findViewById(R.id.keyboard_number_4);
        mNumber[5] = (TextView) contentView.findViewById(R.id.keyboard_number_5);
        mNumber[6] = (TextView) contentView.findViewById(R.id.keyboard_number_6);

        final View.OnClickListener numberSelectedHandler = new View.OnClickListener() {
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
                        mKeyboardView.setKeyboard(mProvinceKeyboard);
                    }
                } else if (id == R.id.keyboard_number_1) {
                    if (mShowKeyboard != R.xml.keyboard_city_code) {
                        mShowKeyboard = R.xml.keyboard_city_code;
                        mKeyboardView.setKeyboard(mCityCodeKeyboard);
                    }
                } else if (id == R.id.keyboard_number_6) {
                    if (mShowKeyboard != R.xml.keyboard_number_extra) {
                        mShowKeyboard = R.xml.keyboard_number_extra;
                        mKeyboardView.setKeyboard(mNumberExtraKeyboard);
                    }
                } else {
                    if (mShowKeyboard != R.xml.keyboard_number) {
                        mShowKeyboard = R.xml.keyboard_number;
                        mKeyboardView.setKeyboard(mNumberKeyboard);
                    }
                }
                mKeyboardView.invalidateAllKeys();
                mKeyboardView.invalidate();
            }
        };
        for (TextView m : mNumber) {
            m.setOnClickListener(numberSelectedHandler);
        }

        mProvinceKeyboard = new Keyboard(mContext, R.xml.keyboard_province);
        mCityCodeKeyboard = new Keyboard(mContext, R.xml.keyboard_city_code);
        mNumberKeyboard = new Keyboard(mContext, R.xml.keyboard_number);
        mNumberExtraKeyboard = new Keyboard(mContext, R.xml.keyboard_number_extra);

        mKeyboardView = (KeyboardView) contentView.findViewById(R.id.keyboard_view);
        mKeyboardView.setOnKeyboardActionListener(new OnKeyboardActionHandler(){
            @Override
            public void onKey(int charCode, int[] keyCodes) {
                if (charCode >= 400){ // 400 See keyboard xml
                    charCode = CHINESE.charAt(charCode - 400);
                }
                mSelectedTextView.setText(Character.toString((char) charCode));
                autoNextNumber();
            }
        });
        mKeyboardView.setPreviewEnabled(false);// !!! Must be false

        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));

        mCommitButton = contentView.findViewById(R.id.keyboard_commit);
        mCommitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder number = new StringBuilder(NUMBER_LENGTH);
                for (TextView i : mNumber){
                    if (!" ".equals(i.getText())){
                        number.append(i.getText());
                    }
                }
                if (number.length() == NUMBER_LENGTH){
                    mCommitListener.onCommit(number.toString());
                    dismiss();
                }
            }
        });
    }

    /**
     * 显示车牌输入法
     * @param activity Activity
     */
    public void show(Activity activity, String givenNumber, OnNumberCommitListener commitListener){
        mCommitListener = commitListener;
        if ( ! TextUtils.isEmpty(givenNumber)){
            if (NUMBER_LENGTH != givenNumber.length()){
                throw new IllegalArgumentException("Illegal vehicle number length");
            }else{
                final char[] numbers = givenNumber.toUpperCase().toCharArray();
                for (int i = 0;i< NUMBER_LENGTH;i++){
                    mNumber[i].setText(Character.toString(numbers[i]));
                }
            }
        }
        final View anchorView = activity.getWindow().getDecorView().getRootView();
        anchorView.post(new Runnable() {
            @Override
            public void run() {
                mPopupWindow.showAtLocation(anchorView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                mNumber[0].performClick();
            }
        });

    }

    public void dismiss(){
        mPopupWindow.dismiss();
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
        } else if (numberId == R.id.keyboard_number_6 && mAutoCommit) {
            mCommitButton.performClick();
        }
    }

}

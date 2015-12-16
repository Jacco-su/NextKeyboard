package com.github.yoojia.keyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.github.yoojia.keyboard.AbstractKeyboard;
import com.github.yoojia.keyboard.OnCommitListener;
import com.github.yoojia.keyboard.OnKeyboardActionHandler;
import com.github.yoojia.keyboard.R;

/**
 * 车辆号码键盘
 *
 * @author  yoojia.chen@gmail.com
 * @version version 2015-04-24
 * @since   1.0
 */
public class VehiclePlateKeyboard extends AbstractKeyboard{

    private static final int NUMBER_LENGTH = 7;

    private static final String CHINESE = "|京津晋冀蒙辽吉黑沪苏浙皖闽赣鲁豫鄂湘粤桂琼渝川贵云藏陕甘青宁新|港澳警学挂";

    private final KeyboardView mKeyboardView;
    private final TextView[] mNumber = new TextView[NUMBER_LENGTH];

    private View mCommitButton;
    private int mShowKeyboard = 0;
    private TextView mSelectedTextView;

    private Keyboard mProvinceKeyboard;
    private Keyboard mCityCodeKeyboard;
    private Keyboard mNumberKeyboard;
    private Keyboard mNumberExtraKeyboard;

    private String mDefaultPlateNumber;

    public VehiclePlateKeyboard(Context context, OnCommitListener commitListener) {
        super(context, commitListener);

        final View contentView = putContentView(R.layout.next_keyboard_vehicle_plate);

        mNumber[0] = (TextView) contentView.findViewById(R.id.keyboard_number_0);
        mNumber[1] = (TextView) contentView.findViewById(R.id.keyboard_number_1);
        mNumber[2] = (TextView) contentView.findViewById(R.id.keyboard_number_2);
        mNumber[3] = (TextView) contentView.findViewById(R.id.keyboard_number_3);
        mNumber[4] = (TextView) contentView.findViewById(R.id.keyboard_number_4);
        mNumber[5] = (TextView) contentView.findViewById(R.id.keyboard_number_5);
        mNumber[6] = (TextView) contentView.findViewById(R.id.keyboard_number_6);

        final View.OnClickListener listener = createNumberListener();
        for (TextView m : mNumber) {
            m.setOnClickListener(listener);
        }

        mProvinceKeyboard = new Keyboard(context, R.xml.keyboard_vehicle_province);
        mCityCodeKeyboard = new Keyboard(context, R.xml.keyboard_vehicle_code);
        mNumberKeyboard = new Keyboard(context, R.xml.keyboard_vehicle_number);
        mNumberExtraKeyboard = new Keyboard(context, R.xml.keyboard_vehicle_number_extra);

        mKeyboardView = (KeyboardView) contentView.findViewById(R.id.keyboard_view);
        mKeyboardView.setOnKeyboardActionListener(new OnKeyboardActionHandler(){
            @Override
            public void onKey(int charCode, int[] keyCodes) {
                if (charCode >= 400){ // 400 See keyboard xml
                    charCode = CHINESE.charAt(charCode - 400);
                }
                mSelectedTextView.setText(Character.toString((char) charCode));
                nextNumber();
            }
        });
        mKeyboardView.setPreviewEnabled(false);// !!! Must be false

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

    public void setDefaultPlateNumber(String number) {
        mDefaultPlateNumber = number;
    }

    @Override
    public void show(View anchorView) {
        if ( ! TextUtils.isEmpty(mDefaultPlateNumber)){
            if (NUMBER_LENGTH != mDefaultPlateNumber.length()){
                throw new IllegalArgumentException("Illegal vehicle number:" + mDefaultPlateNumber);
            }else{
                final char[] numbers = mDefaultPlateNumber.toUpperCase().toCharArray();
                for (int i = 0;i< NUMBER_LENGTH;i++){
                    mNumber[i].setText(Character.toString(numbers[i]));
                }
            }
        }
        super.show(anchorView);
    }

    @Override
    protected void onShow() {
        mNumber[0].performClick();
    }

    private void nextNumber(){
        final int numberId = mSelectedTextView.getId();
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
        }
    }

    private View.OnClickListener createNumberListener() {
        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedTextView != null){
                    mSelectedTextView.setActivated(false);
                }
                mSelectedTextView = (TextView) view;
                mSelectedTextView.setActivated(true);
                int id = view.getId();
                if (id == R.id.keyboard_number_0) {
                    if (mShowKeyboard != R.xml.keyboard_vehicle_province) {
                        mShowKeyboard = R.xml.keyboard_vehicle_province;
                        mKeyboardView.setKeyboard(mProvinceKeyboard);
                    }
                } else if (id == R.id.keyboard_number_1) {
                    if (mShowKeyboard != R.xml.keyboard_vehicle_code) {
                        mShowKeyboard = R.xml.keyboard_vehicle_code;
                        mKeyboardView.setKeyboard(mCityCodeKeyboard);
                    }
                } else if (id == R.id.keyboard_number_6) {
                    if (mShowKeyboard != R.xml.keyboard_vehicle_number_extra) {
                        mShowKeyboard = R.xml.keyboard_vehicle_number_extra;
                        mKeyboardView.setKeyboard(mNumberExtraKeyboard);
                    }
                } else {
                    if (mShowKeyboard != R.xml.keyboard_vehicle_number) {
                        mShowKeyboard = R.xml.keyboard_vehicle_number;
                        mKeyboardView.setKeyboard(mNumberKeyboard);
                    }
                }
                mKeyboardView.invalidateAllKeys();
                mKeyboardView.invalidate();
            }
        };
        return listener;
    }

}

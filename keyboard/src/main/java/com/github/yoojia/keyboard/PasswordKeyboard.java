package com.github.yoojia.keyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.widget.TextView;

/**
 *
 * @author YOOJIA.CHEN (yoojia.chen@gmail.com)
 */
public class PasswordKeyboard extends AbstractKeyboard{

    private final KeyboardView mKeyboardView;
    private final Keyboard mNumberKeyboard;

    private final TextView[] mNumber = new TextView[6];

    private TextView mSelectedTextView;

    public PasswordKeyboard(Context context, OnCommitListener commitListener) {
        super(context, commitListener);
        final View contentView = putContentView(R.layout.next_keyboard_password);

        mNumber[0] = (TextView) contentView.findViewById(R.id.keyboard_number_0);
        mNumber[1] = (TextView) contentView.findViewById(R.id.keyboard_number_1);
        mNumber[2] = (TextView) contentView.findViewById(R.id.keyboard_number_2);
        mNumber[3] = (TextView) contentView.findViewById(R.id.keyboard_number_3);
        mNumber[4] = (TextView) contentView.findViewById(R.id.keyboard_number_4);
        mNumber[5] = (TextView) contentView.findViewById(R.id.keyboard_number_5);

        final View.OnClickListener listener = createNumberListener();
        for (TextView m : mNumber) {
            m.setOnClickListener(listener);
        }

        mNumberKeyboard = new Keyboard(context, R.xml.keyboard_numbers);
        mKeyboardView = (KeyboardView) contentView.findViewById(R.id.keyboard_view);
        mKeyboardView.setOnKeyboardActionListener(new OnKeyboardActionHandler() {
            @Override
            public void onKey(int charCode, int[] keyCodes) {
                mSelectedTextView.setText(Character.toString((char) charCode));
                nextNumber();
            }
        });
        mKeyboardView.setPreviewEnabled(false);// !!! Must be false
        mKeyboardView.setKeyboard(mNumberKeyboard);
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
            }
        };
        return listener;
    }
}

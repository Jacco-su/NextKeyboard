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
        final View contentView = putContentView(R.layout.next_keyboard_number);

        mNumber[0] = (TextView) contentView.findViewById(R.id.keyboard_number_0);
        mNumber[1] = (TextView) contentView.findViewById(R.id.keyboard_number_1);
        mNumber[2] = (TextView) contentView.findViewById(R.id.keyboard_number_2);
        mNumber[3] = (TextView) contentView.findViewById(R.id.keyboard_number_3);
        mNumber[4] = (TextView) contentView.findViewById(R.id.keyboard_number_4);
        mNumber[5] = (TextView) contentView.findViewById(R.id.keyboard_number_5);

        mNumberKeyboard = new Keyboard(context, R.xml.keyboard_numbers);
        mKeyboardView = (KeyboardView) contentView.findViewById(R.id.keyboard_view);
        mKeyboardView.setOnKeyboardActionListener(new OnKeyboardActionHandler() {
            @Override
            public void onKey(int charCode, int[] keyCodes) {
                mSelectedTextView.setText(Character.toString((char) charCode));
            }
        });
        mKeyboardView.setPreviewEnabled(false);// !!! Must be false
        mKeyboardView.setKeyboard(mNumberKeyboard);
    }
}

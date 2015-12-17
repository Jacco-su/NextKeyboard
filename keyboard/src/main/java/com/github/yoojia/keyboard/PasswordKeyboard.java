package com.github.yoojia.keyboard;

import android.app.Activity;
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

    private final TextView[] mNumbersTextView = new TextView[6];

    private TextView mSelectedTextView;

    public PasswordKeyboard(Context context, OnCommitListener commitListener) {
        super(context, commitListener);
        final View contentView = putContentView(R.layout.keyboard_password);

        mNumbersTextView[0] = (TextView) contentView.findViewById(R.id.keyboard_number_0);
        mNumbersTextView[1] = (TextView) contentView.findViewById(R.id.keyboard_number_1);
        mNumbersTextView[2] = (TextView) contentView.findViewById(R.id.keyboard_number_2);
        mNumbersTextView[3] = (TextView) contentView.findViewById(R.id.keyboard_number_3);
        mNumbersTextView[4] = (TextView) contentView.findViewById(R.id.keyboard_number_4);
        mNumbersTextView[5] = (TextView) contentView.findViewById(R.id.keyboard_number_5);

        final View.OnClickListener listener = createNumberListener();
        for (TextView m : mNumbersTextView) {
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
        mNumbersTextView[0].performClick();
    }

    private void nextNumber(){
        final int viewId = mSelectedTextView.getId();
        if (viewId == R.id.keyboard_number_0) {
            mNumbersTextView[1].performClick();
        } else if (viewId == R.id.keyboard_number_1) {
            mNumbersTextView[2].performClick();
        } else if (viewId == R.id.keyboard_number_2) {
            mNumbersTextView[3].performClick();
        } else if (viewId == R.id.keyboard_number_3) {
            mNumbersTextView[4].performClick();
        } else if (viewId == R.id.keyboard_number_4) {
            mNumbersTextView[5].performClick();
        } else if (viewId == R.id.keyboard_number_5) {
            final String number = getInput(mNumbersTextView);
            if (number.length() == mNumbersTextView.length){
                mCommitListener.onCommit(number);
                dismiss();
            }
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

    public static void show(Activity context, OnCommitListener listener) {
        new PasswordKeyboard(context, listener).show(context.getWindow().getDecorView().getRootView());
    }

    public static PasswordKeyboard create(Context context, OnCommitListener listener) {
        return new PasswordKeyboard(context, listener);
    }
}

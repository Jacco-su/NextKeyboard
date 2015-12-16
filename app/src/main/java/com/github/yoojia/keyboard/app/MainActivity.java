package com.github.yoojia.keyboard.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.yoojia.keyboard.PasswordKeyboard;
import com.github.yoojia.keyboard.VehiclePlateKeyboard;
import com.github.yoojia.keyboard.OnCommitListener;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView display = (TextView) findViewById(R.id.display);

        final Button vehicle = (Button) findViewById(R.id.vehicle);

        final View anchorView = MainActivity.this.getWindow().getDecorView().getRootView();
        vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new VehiclePlateKeyboard(getBaseContext(), new OnCommitListener() {
                    @Override
                    public void onCommit(String input) {
                        display.setText(input);
                    }
                }).show(anchorView);
            }
        });

        final Button number = (Button) findViewById(R.id.number);
        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PasswordKeyboard(getBaseContext(), new OnCommitListener() {
                    @Override
                    public void onCommit(String input) {

                    }
                }).show(anchorView);
            }
        });
    }

}

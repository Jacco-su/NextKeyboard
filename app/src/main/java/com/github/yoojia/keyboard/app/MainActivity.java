package com.github.yoojia.keyboard.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.yoojia.keyboard.PasswordKeyboard;
import com.github.yoojia.keyboard.VehiclePlateKeyboard;
import com.github.yoojia.keyboard.OnKeyActionListener;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView display = (TextView) findViewById(R.id.display);

        final Button vehicle = (Button) findViewById(R.id.vehicle);

        vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VehiclePlateKeyboard keyboard = new VehiclePlateKeyboard(MainActivity.this, new OnKeyActionListener() {
                    @Override
                    public void onFinish(String input) {
                        display.setText(input);
                    }

                    @Override
                    public void onProcess(String input) {
                        display.setText("Processing: " + input);
                    }
                });
                keyboard.setDefaultPlateNumber("WJ粤12345");
                keyboard.show(getWindow().getDecorView().getRootView());
            }
        });

        final Button number = (Button) findViewById(R.id.number);
        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordKeyboard.show(MainActivity.this, new OnKeyActionListener() {
                    @Override
                    public void onFinish(String input) {
                        display.setText(input);
                    }

                    @Override
                    public void onProcess(String input) {
                        display.setText("Processing: " + input);
                    }
                });
            }
        });
    }

}

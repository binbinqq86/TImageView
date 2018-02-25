package com.example.tb.timageview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {

    private CheckBox cb;
    private Button circle;
    private Button rect;
    private Button corner;
    private BaseImageView biv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    biv.setHasBorder(true);
                }else{
                    biv.setHasBorder(false);
                }
                biv.reDraw();
            }
        });
        circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biv.setCircle(true).setRounderCorner(false).setPartlyCorner(false).reDraw();
            }
        });
        rect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biv.setCircle(false).setRounderCorner(false).setPartlyCorner(false).reDraw();
            }
        });
        corner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biv.setPartlyCorner(false).setRounderCorner(true).setCircle(false).reDraw();
            }
        });
    }

    private void initView() {
        cb = (CheckBox) findViewById(R.id.cb);
        circle = (Button) findViewById(R.id.circle);
        rect = (Button) findViewById(R.id.rect);
        corner = (Button) findViewById(R.id.corner);
        biv = (BaseImageView) findViewById(R.id.biv);
    }
}

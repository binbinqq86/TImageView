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
    private Button cornerAll,corner1;
    private BaseImageView biv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        if(biv.isHasBorder()){
            cb.setChecked(true);
        }
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
                biv.setCircle(true).setCornerType(null).reDraw();
            }
        });
        cornerAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biv.setCircle(false).setCornerType(BaseImageView.CornerType.ALL).reDraw();
            }
        });
        corner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biv.setCircle(false).setCornerType(BaseImageView.CornerType.TOP_LEFT).reDraw();
            }
        });
    }

    private void initView() {
        cb = (CheckBox) findViewById(R.id.cb);
        circle = (Button) findViewById(R.id.circle);
        corner1 = (Button) findViewById(R.id.corner_1);
        cornerAll = (Button) findViewById(R.id.corner_all);
        biv = (BaseImageView) findViewById(R.id.biv);
    }
}

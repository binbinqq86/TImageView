package com.example.tb.timageview;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {

    private CheckBox cb;
    private Button circle;
    private Button cornerAll,corner1,oval1,oval2,hexagon;
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
                ConstraintLayout.LayoutParams clp= (ConstraintLayout.LayoutParams) biv.getLayoutParams();
                clp.height= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics());
                clp.width= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics());
                biv.setLayoutParams(clp);
                biv.setCircle(true).setOtherType(null).setOval(false).setCornerType(null).reDraw();
            }
        });
        cornerAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout.LayoutParams clp= (ConstraintLayout.LayoutParams) biv.getLayoutParams();
                clp.height= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics());
                clp.width= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics());
                biv.setLayoutParams(clp);
                biv.setCircle(false).setOtherType(null).setOval(false).setCornerType(BaseImageView.CornerType.ALL).reDraw();
            }
        });
        corner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout.LayoutParams clp= (ConstraintLayout.LayoutParams) biv.getLayoutParams();
                clp.height= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics());
                clp.width= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics());
                biv.setLayoutParams(clp);
                biv.setCircle(false).setOtherType(null).setOval(false).setCornerType(BaseImageView.CornerType.TOP_LEFT).reDraw();
            }
        });
        oval1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout.LayoutParams clp= (ConstraintLayout.LayoutParams) biv.getLayoutParams();
                clp.height= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200,getResources().getDisplayMetrics());
                clp.width= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics());
                biv.setLayoutParams(clp);
                biv.setCircle(false).setOtherType(null).setOval(true).setCornerType(null).reDraw();
            }
        });
        oval2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout.LayoutParams clp= (ConstraintLayout.LayoutParams) biv.getLayoutParams();
                clp.width= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200,getResources().getDisplayMetrics());
                clp.height= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics());
                biv.setLayoutParams(clp);
                biv.setCircle(false).setOtherType(null).setOval(true).setCornerType(null).reDraw();
            }
        });
        hexagon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout.LayoutParams clp= (ConstraintLayout.LayoutParams) biv.getLayoutParams();
                clp.width= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics());
                clp.height= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics());
                biv.setLayoutParams(clp);
                biv.setCircle(false).setOtherType(BaseImageView.OtherType.HEXAGON).setOval(false).setCornerType(null).reDraw();
            }
        });
    }

    private void initView() {
        cb = (CheckBox) findViewById(R.id.cb);
        circle = (Button) findViewById(R.id.circle);
        corner1 = (Button) findViewById(R.id.corner_1);
        cornerAll = (Button) findViewById(R.id.corner_all);
        biv = (BaseImageView) findViewById(R.id.biv);
        oval1=findViewById(R.id.oval_1);
        oval2=findViewById(R.id.oval_2);
        hexagon=findViewById(R.id.hexagon);
    }
}

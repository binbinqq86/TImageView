package com.example.tb.timageview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @auther tb
 * @time 2018/2/27 上午10:37
 * @desc
 */
public class Test extends View {
    public Test(Context context) {
        super(context);
    }
    
    public Test(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public Test(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint=new Paint();
        int w=50;//线宽
        int r=200;//圆角半径
        int cx=300,cy=300;
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.MITER);//空心的时候拐角连接方式
        paint.setStrokeCap(Paint.Cap.BUTT);// 设置线帽，即线两端突出的帽子一样的，默认无
        paint.setDither(true);//防抖动
        paint.setShader(null);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(w);
        
        canvas.drawCircle(r,r,r-w/2f,paint);
        paint.setColor(0xaaddaacc);
        canvas.drawRoundRect(new RectF(w/2f,w/2f,2*cx-w/2f,2*cx-w/2f),r-w/2f,r-w/2f,paint);
        paint.setColor(0xaaffaacc);
        canvas.drawArc(new RectF(w/2f,w/2f,2*r-w/2f,2*r-w/2f),180,90,false,paint);
        
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        canvas.drawArc(new RectF(w,w,2*r-w,2*r-w),180,90,true,paint);
    }
}

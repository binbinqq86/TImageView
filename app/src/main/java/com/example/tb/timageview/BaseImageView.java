package com.example.tb.timageview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

/**
 * @auther tb
 * @time 2018/2/24 上午10:50
 * @desc 圆形，圆角，带边框，高斯模糊。。。
 * TODO 椭圆、利用path绘制各种图案的待加入。。。
 * <a href="http://blog.csdn.net/binbinqq86/article/details/78329238">另外一种绘制方式</a>
 */
@SuppressLint("AppCompatCustomView")
public class BaseImageView extends ImageView {
    private static final String TAG = "BaseImageView";
    /**
     * 圆角半径
     */
    private float corner;
    /**
     * 边框宽度
     */
    private float borderWidth;
    /**
     * 边框颜色，没有图片时候的默认色
     */
    private int borderColor, defaultColor;//具体颜色值，非资源id
    /**
     * 是否圆形，有无边框，高斯模糊
     */
    private boolean isCircle, hasBorder, isBlur;
    /**
     * 高斯模糊半径
     */
    private float blurRadius;
    /**
     * 控件宽高
     */
    private int width, height;
    /**
     * 图片画笔和边框画笔
     */
    private Paint mPaintDrawable, mPaintBorder;
    private BitmapShader mBitmapShader;
    private Matrix mMatrix;
    private Path path;
    
    /**
     * 圆角的类型
     * 顺时针1234四个角，四个角可分为1,2,3,4,12,13,14,23,24,34,123,124,134,234,1234十五种情况
     */
    public enum CornerType {
        ALL,
        TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT,
        TOP_LEFT_TOP_RIGHT, TOP_LEFT_BOTTOM_RIGHT, TOP_LEFT_BOTTOM_LEFT,
        TOP_RIGHT_BOTTOM_RIGHT, TOP_RIGHT_BOTTOM_LEFT, BOTTOM_RIGHT_BOTTOM_LEFT,
        TOP_LEFT_TOP_RIGHT_BOTTOM_RIGHT, TOP_LEFT_TOP_RIGHT_BOTTOM_LEFT, TOP_LEFT_BOTTOM_RIGHT_BOTTOM_LEFT, TOP_RIGHT_BOTTOM_RIGHT_BOTTOM_LEFT
    }
    
    private CornerType cornerType = null;
    
    public BaseImageView(Context context) {
        super(context);
        init();
    }
    
    public BaseImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public BaseImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(context, attrs, defStyleAttr);
        init();
    }
    
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        obtainStyledAttrs(context, attrs, defStyleAttr);
        init();
    }
    
    private void obtainStyledAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.baselib_BaseImageView, defStyleAttr, 0);
        try {
            int ct = a.getInteger(R.styleable.baselib_BaseImageView_baselib_corner_type, 0);
            cornerType = getCornerType(ct);
            
            corner = a.getDimension(R.styleable.baselib_BaseImageView_baselib_corner, 0);
            blurRadius = a.getDimension(R.styleable.baselib_BaseImageView_baselib_blur_radius, 0);
            
            borderWidth = a.getDimension(R.styleable.baselib_BaseImageView_baselib_border_width, 0);
            borderColor = a.getColor(R.styleable.baselib_BaseImageView_baselib_border_color, 0);
            defaultColor = a.getColor(R.styleable.baselib_BaseImageView_baselib_default_color, 0);
            
            isCircle = a.getBoolean(R.styleable.baselib_BaseImageView_baselib_is_circle, false);
            hasBorder = a.getBoolean(R.styleable.baselib_BaseImageView_baselib_has_border, false);
            isBlur = a.getBoolean(R.styleable.baselib_BaseImageView_baselib_is_blur, false);
        } finally {
            a.recycle();
        }
    }
    
    private void init() {
        setBackgroundColor(Color.YELLOW);
//        setScaleType(ScaleType.CENTER_CROP);//由于下面是自己绘制，所以该属性失效
        mMatrix = new Matrix();
        mPaintDrawable = new Paint();
        mPaintDrawable.setAntiAlias(true);
        mPaintDrawable.setStyle(Paint.Style.FILL);
        mPaintDrawable.setStrokeJoin(Paint.Join.ROUND);//空心的时候拐角连接方式
        mPaintDrawable.setStrokeCap(Paint.Cap.BUTT);// 设置线帽，即线两端突出的帽子一样的，默认无
        mPaintDrawable.setDither(true);//防抖动
        mPaintDrawable.setShader(null);
        
        mPaintBorder = new Paint(mPaintDrawable);
        mPaintBorder.setStyle(Paint.Style.STROKE);
        mPaintBorder.setStrokeWidth(borderWidth);
        mPaintBorder.setColor(borderColor);
        mPaintBorder.setStrokeJoin(Paint.Join.MITER);//空心的时候拐角连接方式
        
        path = new Path();
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 当模式为圆形模式的时候，我们强制让宽高一致
         */
        if (isCircle) {
            int result = Math.min(getMeasuredHeight(), getMeasuredWidth());
            setMeasuredDimension(result, result);
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);//一定要去掉，否则原来的src还会绘制
        drawBitmap(canvas);
        if (hasBorder) {
            drawBorder(canvas);
        }
    }
    
    private void drawBitmap(Canvas canvas) {
        Drawable drawable = getDrawable();
        Bitmap bitmap = getBitmap(drawable);
        
        setUpShader(bitmap);
        if (isCircle) {
            float r = hasBorder ? width / 2f - borderWidth : width / 2f;
            canvas.drawCircle(width / 2f, height / 2f, r, mPaintDrawable);
        } else if (cornerType != null) {
            //corner为圆心到视图边缘的距离，也就是这个圆角的半径，
            //所以里面的图片的圆角的半径为corner减去边框宽度，因为二者圆心一致，这样才能平行画圆
            float cr = corner - borderWidth;
            cr = cr > 0 ? cr : 0;
            switch (cornerType) {
                case ALL:
                    if (hasBorder) {
                        //Math.ceil进位来保证不留白，扩大一点绘制区域
                        RectF rf = new RectF(borderWidth, borderWidth, (float) Math.ceil(width - borderWidth), (float) Math.ceil(height - borderWidth));
                        
                        canvas.drawRoundRect(rf, cr, cr, mPaintDrawable);
                    } else {
                        RectF rf = new RectF(0, 0, width, height);
                        //corner为圆心到边缘的距离
                        canvas.drawRoundRect(rf, corner, corner, mPaintDrawable);
                    }
                    break;
                case TOP_LEFT:
                    //分块绘制，也可以采用path来绘制
                    if (hasBorder) {
//                        path.reset();
//                        path.moveTo(borderWidth,borderWidth+cr+borderWidth);
//                        path.addArc(new RectF(borderWidth, borderWidth, cr * 2f + borderWidth, cr * 2f + borderWidth), 180, 90);
//                        path.lineTo(width-borderWidth,borderWidth);
//                        path.lineTo(width-borderWidth,height-borderWidth);
//                        path.lineTo(borderWidth,height-borderWidth);
//                        path.close();
//                        canvas.drawPath(path,mPaintDrawable);

//                        canvas.drawRoundRect(new RectF(borderWidth, borderWidth, cr * 2f+borderWidth, cr * 2f+borderWidth),cr,cr,mPaintDrawable);
                        canvas.drawArc(new RectF(borderWidth, borderWidth, corner * 2f - borderWidth, cr * 2f + borderWidth), 180, 90, true, mPaintDrawable);
                        canvas.drawRect(new RectF(borderWidth, corner, corner, height - borderWidth), mPaintDrawable);
                        canvas.drawRect(new RectF(corner, borderWidth, width - borderWidth, height - borderWidth), mPaintDrawable);
                    } else {
                        //drawRoundRect也可以，不过多绘制了一部分圆
                        canvas.drawRoundRect(new RectF(0, 0, corner * 2f, corner * 2f), corner, corner, mPaintDrawable);
                        canvas.drawRect(new RectF(0, corner, corner, height), mPaintDrawable);
                        canvas.drawRect(new RectF(corner, 0, width, height), mPaintDrawable);
                    }
                    break;
                case TOP_RIGHT:
                    break;
                case BOTTOM_RIGHT:
                    break;
                case BOTTOM_LEFT:
                    break;
                case TOP_LEFT_TOP_RIGHT:
                    break;
                case TOP_LEFT_BOTTOM_RIGHT:
                    break;
                case TOP_LEFT_BOTTOM_LEFT:
                    break;
                case TOP_RIGHT_BOTTOM_RIGHT:
                    break;
                case TOP_RIGHT_BOTTOM_LEFT:
                    break;
                case BOTTOM_RIGHT_BOTTOM_LEFT:
                    break;
                case TOP_LEFT_TOP_RIGHT_BOTTOM_RIGHT:
                    break;
                case TOP_LEFT_TOP_RIGHT_BOTTOM_LEFT:
                    break;
                case TOP_LEFT_BOTTOM_RIGHT_BOTTOM_LEFT:
                    break;
                case TOP_RIGHT_BOTTOM_RIGHT_BOTTOM_LEFT:
                    break;
            }
        } else {
            //矩形
            RectF rf = hasBorder ? new RectF(borderWidth, borderWidth, (float) Math.ceil(width - borderWidth), (float) Math.ceil(height - borderWidth)) : new RectF(0, 0, width, height);
            canvas.drawRect(rf, mPaintDrawable);
        }
    }
    
    private void drawBorder(Canvas canvas) {
        if (isCircle) {
            //圆形
            canvas.drawCircle(width / 2f, height / 2f, (width - borderWidth) / 2f, mPaintBorder);
        } else if (cornerType != null) {
            //半径为圆心到边框中点的距离（空心的情况，圆弧的半径到线宽的中点，而不是边缘!!!）
            float cr = corner - borderWidth / 2f;
            cr = cr > 0 ? cr : 0;
            switch (cornerType) {
                case ALL:
                    //圆角——边框宽度中点为圆角半径边缘
                    RectF rf = new RectF(borderWidth / 2f, borderWidth / 2f, width - borderWidth / 2f, height - borderWidth / 2f);
                    canvas.drawRoundRect(rf, cr, cr, mPaintBorder);
                    break;
                case TOP_LEFT:
                    //两种方案都可以
//                    path.reset();
//                    path.moveTo(0, corner);
//                    path.addArc(new RectF(borderWidth / 2f, borderWidth / 2f, corner * 2f-borderWidth/2f, corner * 2f-borderWidth/2f), 180, 90);
//                    path.lineTo(width - borderWidth / 2f, borderWidth / 2f);
//                    path.lineTo(width - borderWidth / 2f, height - borderWidth / 2f);
//                    path.lineTo(borderWidth / 2f, height - borderWidth / 2f);
//                    path.close();
//                    canvas.drawPath(path, mPaintBorder);
                    
                    canvas.drawArc(new RectF(borderWidth / 2f, borderWidth / 2f, corner * 2f - borderWidth / 2f, corner * 2f - borderWidth / 2f), 180, 90, false, mPaintBorder);
                    canvas.drawLine(corner, borderWidth / 2f, width, borderWidth / 2f, mPaintBorder);
                    canvas.drawLine(width - borderWidth / 2f, borderWidth, width - borderWidth / 2f, height, mPaintBorder);
                    canvas.drawLine(width - borderWidth, height - borderWidth / 2f, 0, height - borderWidth / 2f, mPaintBorder);
                    canvas.drawLine(borderWidth / 2f, height - borderWidth, borderWidth / 2f, corner, mPaintBorder);
                    break;
                case TOP_RIGHT:
                    break;
                case BOTTOM_RIGHT:
                    break;
                case BOTTOM_LEFT:
                    break;
                case TOP_LEFT_TOP_RIGHT:
                    break;
                case TOP_LEFT_BOTTOM_RIGHT:
                    break;
                case TOP_LEFT_BOTTOM_LEFT:
                    break;
                case TOP_RIGHT_BOTTOM_RIGHT:
                    break;
                case TOP_RIGHT_BOTTOM_LEFT:
                    break;
                case BOTTOM_RIGHT_BOTTOM_LEFT:
                    break;
                case TOP_LEFT_TOP_RIGHT_BOTTOM_RIGHT:
                    break;
                case TOP_LEFT_TOP_RIGHT_BOTTOM_LEFT:
                    break;
                case TOP_LEFT_BOTTOM_RIGHT_BOTTOM_LEFT:
                    break;
                case TOP_RIGHT_BOTTOM_RIGHT_BOTTOM_LEFT:
                    break;
            }
        } else {
            //矩形
            RectF rf = new RectF(borderWidth / 2f, borderWidth / 2f, width - borderWidth / 2f, height - borderWidth / 2f);
            canvas.drawRect(rf, mPaintBorder);
        }
    }
    
    /**
     * 处理图片大于或者小于控件的情况
     * （不是针对大图内存溢出的处理，此处的处理只是为了让图片居中绘制——centerCrop:参照imageView的处理）
     *
     * @param bitmap
     */
    private void setUpShader(Bitmap bitmap) {
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        
        int dWidth = bitmap.getWidth();
        int dHeight = bitmap.getHeight();
        
        int vWidth = width;
        int vHeight = height;
        if (hasBorder) {
            vWidth -= 2 * borderWidth;
            vHeight -= 2 * borderWidth;
        }
        if (dWidth == vWidth && dHeight == vHeight) {
        
        } else {
            float scale = 1.0f;
            float dx = 0, dy = 0;
            
            if (dWidth * vHeight > vWidth * dHeight) {
                scale = (float) vHeight / (float) dHeight;
                dx = (vWidth - dWidth * scale) * 0.5f;
            } else {
                scale = (float) vWidth / (float) dWidth;
                dy = (vHeight - dHeight * scale) * 0.5f;
            }
            
            mMatrix.setScale(scale, scale);
            if (hasBorder) {//有边框的情况，view视图缩小了，所以需要对图片移动一个边框宽度的处理
                dx += borderWidth;
                dy += borderWidth;
            }
            //上一个缩放操作完成之后，进行移动（把图片中心与视图中心对应，这样保证图片居中，而原来图片是左上角对应视图左上角）,与pre对应
            //<a href="http://blog.csdn.net/programchangesworld/article/details/49078387">参考</a>
            mMatrix.postTranslate(dx, dy);
            
            mBitmapShader.setLocalMatrix(mMatrix);
        }
        
        mPaintDrawable.setShader(mBitmapShader);
    }
    
    /**
     * 获取imageview设置的图片（针对大图，此时必须已经处理过了，否则会造成内存溢出）
     * <p>
     * 获取bitmap：
     * 1、如果设置了src为图片则返回该图片，
     * 2、如果设置了src为颜色值则返回颜色值，
     * 3、如果没有设置src，则返回默认颜色值（未设置则为透明）
     *
     * @param drawable
     * @return
     */
    private Bitmap getBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof ColorDrawable) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);
            int color = ((ColorDrawable) drawable).getColor();
            c.drawARGB(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
        } else {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);
            c.drawARGB(Color.alpha(defaultColor), Color.red(defaultColor), Color.green(defaultColor), Color.blue(defaultColor));
        }
        return bitmap;
    }
    
    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }
    
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        //TODO 状态保存和恢复
    }
    
    private CornerType getCornerType(int ct) {
        switch (ct) {
            case 1234:
                return CornerType.ALL;
            case 1:
                return CornerType.TOP_LEFT;
            case 2:
                return CornerType.TOP_RIGHT;
            case 3:
                return CornerType.BOTTOM_RIGHT;
            case 4:
                return CornerType.BOTTOM_LEFT;
            case 12:
                return CornerType.TOP_LEFT_TOP_RIGHT;
            case 13:
                return CornerType.TOP_LEFT_BOTTOM_RIGHT;
            case 14:
                return CornerType.TOP_LEFT_BOTTOM_LEFT;
            case 23:
                return CornerType.TOP_RIGHT_BOTTOM_RIGHT;
            case 24:
                return CornerType.TOP_RIGHT_BOTTOM_LEFT;
            case 34:
                return CornerType.BOTTOM_RIGHT_BOTTOM_LEFT;
            case 123:
                return CornerType.TOP_LEFT_TOP_RIGHT_BOTTOM_RIGHT;
            case 124:
                return CornerType.TOP_LEFT_TOP_RIGHT_BOTTOM_LEFT;
            case 134:
                return CornerType.TOP_LEFT_BOTTOM_RIGHT_BOTTOM_LEFT;
            case 234:
                return CornerType.TOP_RIGHT_BOTTOM_RIGHT_BOTTOM_LEFT;
        }
        return null;
    }
    
    /**
     * 链式调用完下面的属性设置后，重新绘制
     */
    public void reDraw() {
        init();//重置画笔
        postInvalidate();
    }
    
    //set get==================================================================
    public BaseImageView setCorner(float corner) {
        this.corner = corner;
        return this;
    }
    
    public BaseImageView setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        return this;
    }
    
    public BaseImageView setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        return this;
    }
    
    public BaseImageView setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
        return this;
    }
    
    public BaseImageView setCircle(boolean circle) {
        isCircle = circle;
        return this;
    }
    
    public BaseImageView setHasBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
        return this;
    }
    
    public BaseImageView setBlur(boolean blur) {
        isBlur = blur;
        return this;
    }
    
    public BaseImageView setBlurRadius(float blurRadius) {
        this.blurRadius = blurRadius;
        return this;
    }
    
    public BaseImageView setCornerType(CornerType cornerType) {
        this.cornerType = cornerType;
        return this;
    }
    
    public CornerType getCornerType() {
        return cornerType;
    }
    
    public float getCorner() {
        return corner;
    }
    
    public float getBorderWidth() {
        return borderWidth;
    }
    
    public int getBorderColor() {
        return borderColor;
    }
    
    public int getDefaultColor() {
        return defaultColor;
    }
    
    public boolean isCircle() {
        return isCircle;
    }
    
    public boolean isHasBorder() {
        return hasBorder;
    }
    
    public boolean isBlur() {
        return isBlur;
    }
    
    public float getBlurRadius() {
        return blurRadius;
    }
}

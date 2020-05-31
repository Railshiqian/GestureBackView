package widgettest.demo.com.customwidget.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import widgettest.demo.com.customwidget.utils.AppUtil;

public class SystemBackView extends View {

    private static final int RAC_PHYSICAL_WIDTH = 2; // cm
    private static final int BOTTOM_GAP = 20;

    private float mTagWidth;
    private Paint mPaint = new Paint();
    private Path mPath = new Path();
    private Path mArrowPath = new Path();
    private int mWidth;
    private int mHeigth;
    private float mCurrentMaxHeight = 100f;
    private float mCurrentArcHeight;
    private float mDownX;

    public SystemBackView(Context context) {
        super(context);
    }

    public SystemBackView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SystemBackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2f);
        mTagWidth = AppUtil.cmToPxForWidth(context, RAC_PHYSICAL_WIDTH);
        mCurrentMaxHeight = mTagWidth / 4;
        mCurrentArcHeight = 0;
        setFocusable(true);
        setTranslationY(mCurrentMaxHeight*2);
        setFocusableInTouchMode(true);
        Log.d("Test", "mTagWidth:" + mTagWidth);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    int index = 0;
    int total = 50;

    @Override
    protected void onDraw(Canvas canvas) {

        if (mCurrentArcHeight > 0) {
            if (mWidth == 0 && mHeigth == 0) {
                mWidth = getMeasuredWidth();
                mHeigth = getMeasuredHeight();
            }
            Log.d("TestBackView", "mWidth:" + mWidth + ",mHeight:" + mHeigth);

            float tagStartX = mDownX - mTagWidth / 2;
            float tagStartY = mHeigth - BOTTOM_GAP;

            mPaint.setAlpha(255);
            mPaint.setColor(Color.RED);
            mPath.reset();
            mPath.moveTo(0, tagStartY);

            mPath.lineTo(tagStartX, tagStartY);

            index = 0;
            for (; index < total; index++) {
                float positionX = tagStartX + mTagWidth * index / total;
                double positionY = tagStartY - getSinHeight(index);
                //Log.d("TestBackView", "positionX:" + positionX + ",positionY:" + positionY);
                mPath.lineTo(positionX, (float) positionY);

            }
            mPath.lineTo(mWidth, tagStartY);
            canvas.drawPath(mPath, mPaint);

            drawBackArrow(canvas);

        }
        super.onDraw(canvas);
    }

    private void drawBackArrow(Canvas canvas) {
        mArrowPath.reset();
        float startX = mDownX - mCurrentMaxHeight / 4;

        mArrowPath.moveTo(startX, mHeigth + mCurrentMaxHeight / 4 - mCurrentArcHeight*4/3);
        mArrowPath.lineTo(mDownX, mHeigth - mCurrentArcHeight*4/3);
        mArrowPath.lineTo(mDownX + mCurrentMaxHeight / 4, mHeigth + mCurrentMaxHeight / 4 - mCurrentArcHeight*4/3);

        mPaint.setAlpha((int)(mCurrentArcHeight*255/mCurrentMaxHeight));
        mPaint.setColor(Color.BLUE);
        canvas.drawPath(mArrowPath, mPaint);
    }

    private double getSinHeight(int index) {

        //-2*PI/4  to 2*PI*3/4  y = sin(x)周期=2*PI
        double sinX = -2 * Math.PI / 4 + 2 * Math.PI * index / total;

        //double height = (Math.sin(sinX) + 0.5) * mCurrentArcHeight;
        double height = mCurrentArcHeight * Math.sin(sinX) + mCurrentArcHeight;

        return height;
    }

    float downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                float currentY = event.getY();
                float distane = downY - currentY;
                if (distane > 0) {

                    mCurrentArcHeight = mCurrentMaxHeight * distane / mTagWidth;
                    mCurrentArcHeight = mCurrentArcHeight >= mCurrentMaxHeight ? mCurrentMaxHeight : mCurrentArcHeight;
                    Log.d("TestBackView", "mCurrentArcHeight:" + mCurrentArcHeight);
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mCurrentArcHeight = 0;
                invalidate();
                break;
        }
        return true;
    }
}
package de.jandrotek.android.aspectra.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import de.jandrotek.android.aspectra.core.ConfigViewSettings;

//import static de.jandrotek.android.aspectra.core.ConfigViewSettings;

/**
 * Created by jan on 12.01.15.
 */
public class ConfigLinesView extends View {
    private static final String TAG = "ConfigView";

        private ConfigViewSettings mViewSettings = null;

    private float mConfigWidthX;
    private float mConfigHeightY;

    private float[] mWidthPointsX = new float[4];
    private float[] mHeightPointsY = new float[4];

    private final Paint mLinePaint0 = new Paint();
    private final Paint mLinePaint1 = new Paint();
    private final Paint mLinePaint2 = new Paint();
    private final Paint mLinePaint3 = new Paint();
    private final Paint mLinePaint4 = new Paint();
    private final Paint mLinePaint5 = new Paint();
    private final Paint mLinePaint6 = new Paint();
    private final Paint mLinePaint7 = new Paint();
    private final Paint mLinePaint8 = new Paint();

    private final Path mPath0 = new Path();
    private final Path mPath1 = new Path();
    private final Path mPath2 = new Path();
    private final Path mPath3 = new Path();
    private final Path mPath4 = new Path();
    private final Path mPath5 = new Path();
    private final Path mPath6 = new Path();
    private final Path mPath7 = new Path();
    private final Path mPath8 = new Path();

    public ConfigLinesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mViewSettings = ConfigViewSettings.getInstance();
        initialize();
    }

    public ConfigLinesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mViewSettings = ConfigViewSettings.getInstance();
        initialize(); // here we get
    }

    public ConfigLinesView(Context context) {
        super(context);
        mViewSettings = ConfigViewSettings.getInstance();
        initialize(); // here too
    }


    @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
            //here you have the size of the view and you can do stuff
        mConfigHeightY = b - t;
        mConfigWidthX = r - l;
        if(mConfigWidthX > 0){
            if(BuildConfig.DEBUG) {

                Log.e(TAG, "width = " + mConfigWidthX + ", height = " + mConfigHeightY);
            }
            setConfigDimensions(mConfigWidthX, mConfigHeightY);
            initializeLines();

        }
    }

    public void setPreviewDimensions(int widthX, int heightY){
        mViewSettings.setPreviewDimensions(widthX, heightY);
        initializeLines();
    }

    private void setConfigDimensions(float widthX, float heightY){
        mViewSettings.setConfigDimensions(widthX, heightY);
        initializeLines();
    }

    private void initialize() {

        mLinePaint0.setAntiAlias(true);
        mLinePaint0.setColor(Color.GREEN);
        mLinePaint0.setStrokeWidth(5);
        mLinePaint0.setStyle(Paint.Style.STROKE);

        mLinePaint1.setAntiAlias(true);
        mLinePaint1.setColor(Color.GREEN);
        mLinePaint1.setStrokeWidth(5);
        mLinePaint1.setStyle(Paint.Style.STROKE);

        mLinePaint2.setAntiAlias(true);
        mLinePaint2.setColor(Color.GREEN);
        mLinePaint2.setStrokeWidth(5);
        mLinePaint2.setStyle(Paint.Style.STROKE);

        mLinePaint3.setAntiAlias(true);
        mLinePaint3.setColor(Color.GREEN);
        mLinePaint3.setStrokeWidth(5);
        mLinePaint3.setStyle(Paint.Style.STROKE);

        mLinePaint4.setAntiAlias(true);
        mLinePaint4.setColor(Color.GREEN);
        mLinePaint4.setStrokeWidth(5);
        mLinePaint4.setStyle(Paint.Style.STROKE);

        mLinePaint5.setAntiAlias(true);
        mLinePaint5.setColor(Color.GREEN);
        mLinePaint5.setStrokeWidth(5);
        mLinePaint5.setStyle(Paint.Style.STROKE);

        mLinePaint6.setAntiAlias(true);
        mLinePaint6.setColor(Color.GREEN);
        mLinePaint6.setStrokeWidth(5);
        mLinePaint6.setStyle(Paint.Style.STROKE);

        mLinePaint7.setAntiAlias(true);
        mLinePaint7.setColor(Color.GREEN);
        mLinePaint7.setStrokeWidth(5);
        mLinePaint7.setStyle(Paint.Style.STROKE);

        mLinePaint8.setAntiAlias(true);
        mLinePaint8.setColor(Color.RED);
        mLinePaint8.setStrokeWidth(5);
        mLinePaint8.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath0, mLinePaint0);
        canvas.drawPath(mPath1, mLinePaint1);
        canvas.drawPath(mPath2, mLinePaint2);
        canvas.drawPath(mPath3, mLinePaint3);
        canvas.drawPath(mPath4, mLinePaint4);
        canvas.drawPath(mPath5, mLinePaint5);
        canvas.drawPath(mPath6, mLinePaint6);
        canvas.drawPath(mPath7, mLinePaint7);
        canvas.drawPath(mPath8, mLinePaint8);
    }

    public void setPercent(float widthStartX, float widthEndX, float heightStartY, float deltaLinesY) {
        mViewSettings.setPercent(widthStartX, widthEndX, heightStartY,  deltaLinesY);
            initializeLines();
    }

    private void initializeLines(){
        if(mViewSettings.isConfigured()){
            mViewSettings.calcXYPoints();
            mWidthPointsX = mViewSettings.getPointsX();
            mHeightPointsY = mViewSettings.getPointsY();

            mPath0.reset();
            mPath1.reset();
            mPath2.reset();
            mPath3.reset();
            mPath4.reset();
            mPath5.reset();
            mPath6.reset();
            mPath7.reset();
            mPath8.reset();


            mPath0.moveTo(mWidthPointsX[0], mHeightPointsY[1]);
            mPath0.lineTo(mWidthPointsX[1], mHeightPointsY[1]);

            mPath1.moveTo(mWidthPointsX[2], mHeightPointsY[1]);
            mPath1.lineTo(mWidthPointsX[3], mHeightPointsY[1]);

            mPath2.moveTo(mWidthPointsX[0], mHeightPointsY[2]);
            mPath2.lineTo(mWidthPointsX[1], mHeightPointsY[2]);

            mPath3.moveTo(mWidthPointsX[2], mHeightPointsY[2]);
            mPath3.lineTo(mWidthPointsX[3], mHeightPointsY[2]);

            mPath4.moveTo(mWidthPointsX[1], mHeightPointsY[0]);
            mPath4.lineTo(mWidthPointsX[1], mHeightPointsY[1]);

            mPath5.moveTo(mWidthPointsX[1], mHeightPointsY[2]);
            mPath5.lineTo(mWidthPointsX[1], mHeightPointsY[3]);

            mPath6.moveTo(mWidthPointsX[2], mHeightPointsY[0]);
            mPath6.lineTo(mWidthPointsX[2], mHeightPointsY[1]);

            mPath7.moveTo(mWidthPointsX[2], mHeightPointsY[2]);
            mPath7.lineTo(mWidthPointsX[2], mHeightPointsY[3]);

            mPath8.moveTo(mWidthPointsX[1], mHeightPointsY[1]);
            mPath8.lineTo(mWidthPointsX[2], mHeightPointsY[1]);
            mPath8.lineTo(mWidthPointsX[2], mHeightPointsY[2]);
            mPath8.lineTo(mWidthPointsX[1], mHeightPointsY[2]);
            mPath8.close();

            invalidate();
        }
    }
}

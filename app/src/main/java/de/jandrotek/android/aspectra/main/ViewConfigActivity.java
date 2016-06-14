package de.jandrotek.android.aspectra.main;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.core.ConfigViewSettings;


public class ViewConfigActivity extends BaseActivity
        implements CameraViewFragment.OnFragmentInteractionListener {

    private CameraViewFragment mCameraViewFragment;
    private SeekBar mSbStartW;
    private SeekBar mSbEndW;
    private SeekBar mSbStartH;
    private SeekBar mSbAreaY;
    private TextView mSbStartWValue;
    private TextView mSbEndWValue;
    private TextView mSbStartHValue;
    private TextView mSbAreaYValue;

    private TextView mSbStartWName;
    private TextView mSbEndWName;
    private TextView mSbStartHName;
    private TextView mSbAreaYName;

    private RelativeLayout mBlockStartW;
    private RelativeLayout mBlockEndW;
    private RelativeLayout mBlockStartH;
    private RelativeLayout mBlockAreaY;

    //    private int mPercentStartW;
//    private int mPercentEndW;
//    private int mPercentStartH;
//    private int mPercentEndH;
//    private int mDeltaLinesY;
    private boolean mPrefsChanged = false;
    private boolean mSeekBarCreated = false;

//    private ConfigViewSettings mViewSettings = null;

    //public Handler getHandler() {
    //    return mHandler;
    //}

    //Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateFromPreferences();
        if (mSpectrumLanscapeOrientation) {
            setContentView(R.layout.activity_view_config_cam_land);
        } else {
            setContentView(R.layout.activity_view_config_cam_port);
        }

        // blocks
        mBlockStartW    = (RelativeLayout) findViewById(R.id.sbcWidthStart);
        mBlockEndW      = (RelativeLayout) findViewById(R.id.sbcWidthEnd);
        mBlockStartH    = (RelativeLayout) findViewById(R.id.sbcHeightStart);
        mBlockAreaY     = (RelativeLayout) findViewById(R.id.sbcHeightEnd);

        // seekbars
        mSbStartW   = (SeekBar) mBlockStartW.findViewById(R.id.seekBar);
        mSbEndW     = (SeekBar) mBlockEndW.findViewById(R.id.seekBar);
        mSbStartH   = (SeekBar) mBlockStartH.findViewById(R.id.seekBar);
        mSbAreaY    = (SeekBar) mBlockAreaY.findViewById(R.id.seekBar);


        // name text
        mSbStartWName   = (TextView) mBlockStartW.findViewById(R.id.sbtextName);
        mSbEndWName     = (TextView) mBlockEndW.findViewById(R.id.sbtextName);
        mSbStartHName   = (TextView) mBlockStartH.findViewById(R.id.sbtextName);
        mSbAreaYName    = (TextView) mBlockAreaY.findViewById(R.id.sbtextName);

        mSbStartWName.setText(R.string.name_width_start);
        mSbEndWName.setText(R.string.name_width_end);
        mSbStartHName.setText(R.string.name_height_start);
        mSbAreaYName.setText(R.string.name_count_of_lines);

        // act value text
        mSbStartWValue  = (TextView) mBlockStartW.findViewById(R.id.sbtextValue);
        mSbEndWValue    = (TextView) mBlockEndW.findViewById(R.id.sbtextValue);
        mSbStartHValue  = (TextView) mBlockStartH.findViewById(R.id.sbtextValue);
        mSbAreaYValue   = (TextView) mBlockAreaY.findViewById(R.id.sbtextValue);

        mSbStartW.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < mEndPercentX) {
                    mStartPercentX = progress;
                    mPrefsChanged = true;
                    mSbStartWValue.setText(Integer.toString(mStartPercentX));
                    updateLinesInConfigView();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSbEndW.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > mStartPercentX) {
                    mEndPercentX = progress;
                    mPrefsChanged = true;
                    mSbEndWValue.setText(Integer.toString(mEndPercentX));
                    updateLinesInConfigView();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSbStartH.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mStartPercentY = progress;
                    mPrefsChanged = true;
                mSbStartHValue.setText(Integer.toString(mStartPercentY));
                updateLinesInConfigView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSbAreaY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPrefsChanged = true;
                mScanAreaWidth = calcCountLinesY(progress);
                mSbAreaYValue.setText(Integer.toString(mScanAreaWidth));
                updateLinesInConfigView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSeekBarCreated = true;

        if (savedInstanceState == null) {
            mCameraViewFragment = CameraViewFragment.newInstance(AspectraGlobals.ACT_ITEM_VIEW_CONFIG);
            getSupportFragmentManager().beginTransaction()
// from LiveViewAct
                    .add(R.id.fragmentHolderCameraView_vca, mCameraViewFragment)
// orig here
                            // .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        getScreenOrientation();
        mCameraViewFragment.setDeviceOrientation(mDeviceOrientation);
        setDeviceOrientationInViewSettings();
        updateConfViewSettings();

    }

    @Override
    public void onResume(){
        super.onResume();
        updateFromPreferences();
        mViewSettings = ConfigViewSettings.getInstance();
        mViewSettings.setSpectrumOrientationLandscape(mSpectrumLanscapeOrientation);
        getScreenOrientation();
        mCameraViewFragment.setDeviceOrientation(mDeviceOrientation);
        mViewSettings.setDeviceOrientation(mDeviceOrientation);
        updateLinesInConfigView();
        setDeviceOrientationInViewSettings();
        updateConfViewSettings();

    }

    private int calcCountLinesY(int progress) {
        int temp = 1;
        if(progress < 2){
            temp = 1;
        } else if (progress < 4){
            temp = 2;
        } else if (progress < 8){
            temp = 4;
        } else if (progress < 16){
            temp = 8;
        } else if (progress < 32){
            temp = 16;
        } else if (progress < 64){
            temp = 32;
        } else if (progress < 128){
            temp = 64;
        } else {
            temp = 1;
        }
        return temp;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            Intent intent = new Intent(this, AspectraGlobalPrefsActivity.class);
//            startActivity(intent);
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();

        //get params from ConfigView
        acceptNewPercentSettings();

        if ((mPrefsChanged) && (mAspectraSettings != null)) {
            mAspectraSettings.saveSettings();
        }
        if (mCameraViewFragment != null) {
            mCameraViewFragment.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCameraViewFragment != null) {
            mCameraViewFragment.onStop();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

        // do whatever you wish with the uri
    }

    public void acceptNewPercentSettings() {

        if (mPrefsChanged) {
            mAspectraSettings.setPrefsWidthStart(mStartPercentX);
            mAspectraSettings.setPrefsWidthEnd(mEndPercentX);
            mAspectraSettings.setPrefsHeightStart(mStartPercentY);
            mAspectraSettings.setPrefsHeightEnd(mEndPercentY);
            mAspectraSettings.setPrefsScanAreaWidth(mScanAreaWidth);

            mAspectraSettings.saveSettings();
        }
    }


    protected void updateFromPreferences() {
        super.updateFromPreferences();
//        mPercentStartW = mAspectraSettings.getPrefsWidthStart();
//        mPercentEndW = mAspectraSettings.getPrefsWidthEnd();
//        mPercentStartH = mAspectraSettings.getPrefsHeightStart();
//        mPercentEndH = mAspectraSettings.getPrefsHeightEnd();
//        mDeltaLinesY = mAspectraSettings.getPrefsScanAreaWidth();
        updateSeekBars();
        if (mCameraViewFragment != null) {
            mCameraViewFragment.setStartPercentHX(mStartPercentX);
            mCameraViewFragment.setEndPercentHX(mEndPercentX);
            mCameraViewFragment.setStartPercentVY(mStartPercentY);
            mCameraViewFragment.setEndPercentVY(mEndPercentY);
            mCameraViewFragment.setScanAreaWidth(mScanAreaWidth);
        }
//        if(mViewSettings != null){
//            mViewSettings.setConfigStartPercentX(mStartPercentX);
//            mViewSettings.setConfigEndPercentX(mPercentEndW);
//            mViewSettings.setConfigStartPercentY(mPercentStartH);
//            mViewSettings.setConfigEndPercentY(mPercentEndH);
//            mViewSettings.setAmountLinesY(mDeltaLinesY);
//        }

    }

    protected void updateLinesInConfigView() {
        if (mCameraViewFragment != null) {
            mCameraViewFragment.updateBorderInConfigView(mStartPercentX, mEndPercentX, mStartPercentY, mScanAreaWidth);
        }
    }

    private void updateSeekBars() {
    // start values for bars
        if(mSeekBarCreated) {
            mSbStartW.setProgress(mStartPercentX);
            mSbEndW.setProgress(mEndPercentX);
            mSbStartH.setProgress(mStartPercentY);
            mSbAreaY.setProgress(mScanAreaWidth);
            mSbStartWValue.setText(Integer.toString(mStartPercentX));
            mSbEndWValue.setText(Integer.toString(mEndPercentX));
            mSbStartHValue.setText(Integer.toString(mStartPercentY));
            mSbAreaYValue.setText(Integer.toString(mScanAreaWidth));
        }
    }
}

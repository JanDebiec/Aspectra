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

    private int mPersentStartW;
    private int mPersentEndW;
    private int mPersentStartH;
    private int mPersentEndH;
    private int mDeltaLinesY;
    private boolean mPrefsChanged = false;
    private boolean mSeekBarCreated = false;

    private ConfigViewSettings mViewSettings = null;

    //public Handler getHandler() {
    //    return mHandler;
    //}

    //Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateOrientationFromPrefs();
        if (mSpectrumLanscapeOrientation) {
            setContentView(R.layout.activity_view_config_cam_land);
        } else {
            setContentView(R.layout.activity_view_config_cam_port);
        }

        mViewSettings = ConfigViewSettings.getInstance();
        mViewSettings.setSpectrumOrientationLandscape(mSpectrumLanscapeOrientation);

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
                if (progress < mPersentEndW) {
                    mPersentStartW = progress;
                    mPrefsChanged = true;
                    mSbStartWValue.setText(Integer.toString(mPersentStartW));
                    updateConfigView();
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
                if (progress > mPersentStartW) {
                    mPersentEndW = progress;
                    mPrefsChanged = true;
                    mSbEndWValue.setText(Integer.toString(mPersentEndW));
                    updateConfigView();
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
                    mPersentStartH = progress;
                    mPrefsChanged = true;
                    mSbStartHValue.setText(Integer.toString(mPersentStartH));
                    updateConfigView();
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
                mDeltaLinesY = calcCountLinesY(progress);
                mSbAreaYValue.setText(Integer.toString(mDeltaLinesY));
                updateConfigView();
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
        updateFromPreferences();

    }

    @Override
    public void onResume(){
        super.onResume();
        updateFromPreferences();
        mViewSettings = ConfigViewSettings.getInstance();
        mViewSettings.setSpectrumOrientationLandscape(mSpectrumLanscapeOrientation);
        getScreenOrientation();
        mCameraViewFragment.setDeviceOrientation(mDeviceOrientation):
        mViewSettings.setDeviceOrientation(mDeviceOrientation);
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
        acceptNewPersentSettings();

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

    public void acceptNewPersentSettings() {

        if (mPrefsChanged) {
            mAspectraSettings.setPrefsWidthStart(mPersentStartW);
            mAspectraSettings.setPrefsWidthEnd(mPersentEndW);
            mAspectraSettings.setPrefsHeightStart(mPersentStartH);
            mAspectraSettings.setPrefsHeightEnd(mPersentEndH);
            mAspectraSettings.setPrefsScanAreaWidth(mDeltaLinesY);

            mAspectraSettings.saveSettings();
        }
    }


    protected void updateFromPreferences() {
        super.updateFromPreferences();
        mPersentStartW = mAspectraSettings.getPrefsWidthStart();
        mPersentEndW = mAspectraSettings.getPrefsWidthEnd();
        mPersentStartH = mAspectraSettings.getPrefsHeightStart();
        mPersentEndH = mAspectraSettings.getPrefsHeightEnd();
        mDeltaLinesY = mAspectraSettings.getPrefsScanAreaWidth();
        updateSeekBars();
        if (mCameraViewFragment != null) {
            mCameraViewFragment.setStartPercentHX(mPersentStartW);
            mCameraViewFragment.setEndPercentHX(mPersentEndW);
            mCameraViewFragment.setStartPercentVY(mPersentStartH);
            mCameraViewFragment.setEndPercentVY(mPersentEndH);
            mCameraViewFragment.setScanAreaWidth(mDeltaLinesY);
        }
        if(mViewSettings != null){
            mViewSettings.setConfigStartPercentX(mPersentStartW);
            mViewSettings.setConfigEndPercentX(mPersentEndW);
            mViewSettings.setConfigStartPercentY(mPersentStartH);
            mViewSettings.setConfigEndPercentY(mPersentEndH);
            mViewSettings.setAmountLinesY(mDeltaLinesY);
        }

    }

    protected void updateConfigView() {
        if (mCameraViewFragment != null) {
//            mCameraViewFragment.updateBorderInConfigView(mPersentStartW, mPersentEndW, mPersentStartH, mPersentEndH);
            mCameraViewFragment.updateBorderInConfigView(mPersentStartW, mPersentEndW, mPersentStartH, mDeltaLinesY);
        }
    }

    private void updateSeekBars() {
    // start values for bars
        if(mSeekBarCreated) {
            mSbStartW.setProgress(mPersentStartW);
            mSbEndW.setProgress(mPersentEndW);
            mSbStartH.setProgress(mPersentStartH);
            mSbAreaY.setProgress(mDeltaLinesY);
            mSbStartWValue.setText(Integer.toString(mPersentStartW));
            mSbEndWValue.setText(Integer.toString(mPersentEndW));
            mSbStartHValue.setText(Integer.toString(mPersentStartH));
            mSbAreaYValue.setText(Integer.toString(mDeltaLinesY));
        }
    }
}

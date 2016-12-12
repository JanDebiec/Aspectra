//TODO we do need PlotViewController for that activity
// input fileNames, output int[] data for presenter

package de.jandrotek.android.aspectra.viewer;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import de.jandrotek.android.aspectra.core.AspectraGlobals;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewFragment;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewPresenter;
import de.jandrotek.android.aspectra.libprefs.AspectraGlobalPrefsActivity;
import de.jandrotek.android.aspectra.libspectrafiles.File2PlotConverter;


public class ItemDetailActivity extends AppCompatActivity
//        implements PlotViewFragment_notToUse.OnFragmentInteractionListener
//        implements PlotViewFragment.OnFragmentInteractionListener
{
    private static final String TAG = "DetailItemsAct";
    private static PlotViewFragment mPlotViewFragment;
    private static PlotViewPresenter mPlotViewPresenter = null;
    private static ArrayList<String> mNames = null;

    private static File2PlotConverter mFile2PlotConverter = null;
    private int mPlotCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate() called");
        }

        setContentView(R.layout.activity_item_detail);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();

            mNames = getIntent().getExtras().getStringArrayList(AspectraGlobals.ARG_ITEM_IDS);
            mPlotViewFragment = PlotViewFragment.newInstance(1);//TODO parameter?
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, mPlotViewFragment)
                    .commit();

            //TODO: display content
        }
        if (mFile2PlotConverter == null) {
            mFile2PlotConverter = new File2PlotConverter();
        }
        mPlotCount = mNames.size();
        mFile2PlotConverter.init(mNames);
        if (mPlotViewPresenter == null) {
            mPlotViewPresenter = new PlotViewPresenter(mPlotCount, mPlotViewFragment);
        }
        // Show the Up button in the action bar.
//        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

//    @Override
//    public void onFragmentInteraction(Uri uri){
//
//        // do whatever you wish with the uri
//    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPlotViewPresenter.clearAllSeries();
        int[][] arrayOfData = mFile2PlotConverter.getPlotData();
        mPlotViewPresenter.clearAllSeries();
        mPlotViewPresenter.init(mPlotCount, arrayOfData);
        int length = mPlotViewPresenter.getmDataLengthMax();

        mPlotViewPresenter.updateFragmentPort(0, length);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_activity_base, menu);
        return true;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, AspectraGlobalPrefsActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_help){

        }
        else if (id == R.id.action_about){

        } else if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

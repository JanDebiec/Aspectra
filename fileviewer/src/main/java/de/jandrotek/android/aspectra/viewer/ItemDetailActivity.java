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
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewController;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewControllerBuilder;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewFragment;
import de.jandrotek.android.aspectra.libprefs.AspectraGlobalPrefsActivity;

//import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewFragment_notToUse;

public class ItemDetailActivity extends AppCompatActivity
//        implements PlotViewFragment_notToUse.OnFragmentInteractionListener
//        implements PlotViewFragment.OnFragmentInteractionListener
{
    private static final String TAG = "DetailItemsAct";
    private static PlotViewFragment mPlotViewFragment;
    private static PlotViewController mPlotViewController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(BuildConfig.DEBUG) {
            Log.d(TAG, "onCreate() called");
        }

        setContentView(R.layout.activity_item_detail);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();

            ArrayList<String> names = getIntent().getExtras().getStringArrayList(AspectraGlobals.ARG_ITEM_IDS);
            mPlotViewController = new PlotViewControllerBuilder().setParam1(AspectraGlobals.ACT_ITEM_VIEW_PLOT).setItems(names).getInstancePlotViewController();
            mPlotViewFragment = PlotViewFragment.newInstance(names != null ? names.size() : 0);
            mPlotViewController.init(mPlotViewFragment);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, mPlotViewFragment)
                    .commit();

            //TODO: display content
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
        mPlotViewController.initDisplayInFragment();// must be called when fragment already exists
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

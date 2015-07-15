package de.jandrotek.android.aspectra.analyze;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;

public class AnalyzeActivity extends AppCompatActivity {

    private AnalyzeFragment mAnalyzeFragment;
    private Fragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        if (savedInstanceState == null) {
//            Bundle arguments = new Bundle();
            //Restore the fragment's instance
//            mContent = getSupportFragmentManager().getFragment(
//                    savedInstanceState, "mContent");

            Map<String, String> spectra = new HashMap<>();

            if(getIntent().getExtras().containsKey(AnalyzeFragment.ARG_ITEM_REFERENCE)){
                spectra.put(AnalyzeFragment.ARG_ITEM_REFERENCE, getIntent().getExtras().getString(AnalyzeFragment.ARG_ITEM_REFERENCE));
            }
            if(getIntent().getExtras().containsKey(AnalyzeFragment.ARG_ITEM_EDIT)){
                spectra.put(AnalyzeFragment.ARG_ITEM_EDIT, getIntent().getExtras().getString(AnalyzeFragment.ARG_ITEM_EDIT));
            }

            mAnalyzeFragment = AnalyzeFragment.newInstance(spectra);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_analyze_container, mAnalyzeFragment)
                    .commit();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "mContent", mAnalyzeFragment);


    }

    @Override
    public void onRestoreInstanceState(Bundle inState){
        mAnalyzeFragment = (AnalyzeFragment)getSupportFragmentManager().getFragment(inState,"mContent");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_analyze_not_used, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

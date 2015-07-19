package de.jandrotek.android.aspectra.analyze;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;

import java.util.HashMap;
import java.util.Map;

import de.jandrotek.android.aspectra.core.SpectrumChr;
import de.jandrotek.android.aspectra.libspectrafiles.SpectrumFiles;
import de.jandrotek.android.aspectra.libtouch.TouchView;

public class AnalyzeActivity extends AppCompatActivity
        implements TouchView.OnTouchViewInteractionListener
{
    public static final String ARG_ITEM_REFERENCE = "item_reference";
    public static final String ARG_ITEM_EDIT = "item_edit";

    private AnalyzeFragment mAnalyzeFragment;
    private Fragment mContent;
    private TouchView mTouchView;

    private String mSpectrumNameToEdit;
    private String mSpectrumAbsNameToEdit;
    private String mSpectrumNameReference;
    private String mSpectrumNameAbsReference;
    private int mSpectrumToEditLength;
    private int mSpectrumReferenceLength;
    private int[] mSpectrumToEditValues = null;
    private int[] mSpectrumReferenceValues = null;
    private int mSpectrumLengthMax;
    private SpectrumChr mSpectrumToEdit;
    private SpectrumChr mSpectrumReference;
    private Map<String, String> mSpectraMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze);

        if (savedInstanceState == null) {
            mSpectraMap = new HashMap<>();

            // spectrum reference
            if(getIntent().getExtras().containsKey(ARG_ITEM_REFERENCE)){
                mSpectrumNameReference = getIntent().getExtras().getString(ARG_ITEM_REFERENCE);
                mSpectraMap.put(ARG_ITEM_REFERENCE, mSpectrumNameReference);
            }

            // spectrum to edit
            if(getIntent().getExtras().containsKey(ARG_ITEM_EDIT)){
                mSpectrumNameToEdit = getIntent().getExtras().getString(ARG_ITEM_EDIT);
                mSpectraMap.put(ARG_ITEM_EDIT, mSpectrumNameToEdit);
            }

            mAnalyzeFragment = AnalyzeFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_analyze_plot_container, mAnalyzeFragment)
                    .commit();
            mTouchView = (TouchView)findViewById(R.id.analyze_touchview_overlay);
        }

        generateGraphViewData();
        updateSpectraInFragment(mSpectrumLengthMax);
    }

    private void updateSpectraInFragment(int mSpectrumLengthMax) {
        if (mAnalyzeFragment != null) {
            mAnalyzeFragment.setSpectrumLengthMax(mSpectrumLengthMax);
            mAnalyzeFragment.setSpectrumReferenceValues(mSpectrumReferenceValues);
            mAnalyzeFragment.setSpectrumToEditValues(mSpectrumToEditValues);
        }
    }

    private void generateGraphViewData(){
        if (mSpectrumNameToEdit != null) {
            mSpectrumAbsNameToEdit = SpectrumFiles.mPath + "/" + mSpectrumNameToEdit;
            mSpectrumToEdit = new SpectrumChr(mSpectrumAbsNameToEdit);
            try {
                mSpectrumToEditLength = mSpectrumToEdit.readValuesChr();
                mSpectrumToEditValues = mSpectrumToEdit.getValues();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mSpectrumNameReference != null) {

            mSpectrumNameAbsReference = SpectrumFiles.mPath + "/" + mSpectrumNameReference;
            mSpectrumReference = new SpectrumChr(mSpectrumNameAbsReference);
            try {
                mSpectrumReferenceLength = mSpectrumReference.readValuesChr();
                mSpectrumReferenceValues= mSpectrumReference.getValues();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mSpectrumLengthMax = Math.max(mSpectrumToEditLength, mSpectrumReferenceLength);

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
        getMenuInflater().inflate(R.menu.menu_analyze_edit, menu);
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

    //TODO: interaction with TouchView
    public void onTouchViewInteraction(int _toolId, float _value){
//        mToolName.setText(Integer.toHexString(_toolId));
//        mToolValue.setText(Float.toString(_value));
    }
}

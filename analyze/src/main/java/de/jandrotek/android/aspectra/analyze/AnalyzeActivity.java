package de.jandrotek.android.aspectra.analyze;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;

import de.jandrotek.android.aspectra.core.SpectrumBase;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewFragment;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewPresenter;
import de.jandrotek.android.aspectra.libtouch.TouchView;

public class AnalyzeActivity extends AppCompatActivity
        implements TouchView.OnTouchViewInteractionListener
{
    public static final String ARG_ITEM_REFERENCE = "item_reference";
    public static final String ARG_ITEM_EDIT = "item_edit";
    public static final int ePlotCount = 2;


    //    private AnalyzeFragment mAnalyzeFragment;
    private PlotViewFragment mViewFragment;
    private AnalyzeViewController mViewController;
    private PlotViewPresenter mPlotViewPresenter;
    private Fragment mContent;
    private TouchView mTouchView;

    private String mSpectrumNameToEdit;
    private String mSpectrumNameReference;
    private int mSpectrumLengthMax;
    private SpectrumBase mSpectrumToEdit;
    private SpectrumBase mSpectrumToEditBackup;
    private boolean mSpectrumAlreadyEdited = false;
    private Map<String, String> mSpectraMap;
    public static boolean mCalcBusy = false;


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

            mViewFragment = PlotViewFragment.newInstance(2);
            mPlotViewPresenter = new PlotViewPresenter(ePlotCount, mViewFragment);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_analyze_plot_container, mViewFragment)
                    .commit();
            mViewController = new AnalyzeViewControllerBuilder()
                    .setPlotCount(ePlotCount)
                    .setPlotViewFragment(mViewFragment)
                    .setPlotViewPresenter(mPlotViewPresenter)
                    .setNameToEdit(mSpectrumNameToEdit)
                    .setNameReference(mSpectrumNameReference)
                    .createAnalyzeViewController();
//            mViewController.init(mViewFragment, mPlotViewPresenter, mSpectrumNameToEdit, mSpectrumNameReference);

            mTouchView = (TouchView)findViewById(R.id.analyze_touchview_overlay);
        }

        mCalcBusy = false;
    }



    @Override
    public void onResume(){
        int length;
        super.onResume();
        mViewController.initDisplayInFragment();
        length = mViewController.generateGraphViewData();
        mSpectrumLengthMax = length;
        mViewController.updateSpectraView(mSpectrumLengthMax);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "mContent", mViewFragment);


    }

    @Override
    public void onRestoreInstanceState(Bundle inState){
        mViewFragment = (PlotViewFragment) getSupportFragmentManager().getFragment(inState, "mContent");
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
        if (!mSpectrumAlreadyEdited) {
            mSpectrumAlreadyEdited = true;
            mSpectrumToEditBackup = mSpectrumToEdit;
        }
        if (!mCalcBusy) {
            if (_toolId == TouchView.ePlotAction_Move) {
                mCalcBusy = true;
                new CalcTask(_toolId,_value, 0f).execute();
//                while(mCalcBusy)// add timeout
//                    ;
//                updateEditedSpectrumInFragment();
            }
        }
    }


//    private void updateEditedSpectrumInFragment() {
//        mSpectrumToEditLength = mSpectrumToEdit.getDataSize();
//        mSpectrumLengthMax = Math.max(mSpectrumToEditLength, mSpectrumReferenceLength);
//        mSpectrumToEditValues = mSpectrumToEdit.getValues();
//        mViewController.updateSpectraView(mSpectrumLengthMax);
//        mAnalyzeFragment.updateEditedPlot();
//    }

    public class CalcTask extends AsyncTask<Void, Void, Void> {
        private final int action;
        private final float factor;
        private final float staticPoint;

        CalcTask(int action, float factor, float staticPoint){
            this.action = action;
            this.factor = factor;
            this.staticPoint = staticPoint;
        }

        @Override
        protected Void doInBackground(Void... params) {
//            mCalcBusy = true;
            if (action == TouchView.ePlotAction_Move) {
                mViewController.calcNewSpectraPositions((int) factor);
            }
            return(null);
        }

        @Override
        protected void onPostExecute(Void arg0) {
            mViewController.updateEditedSpectrumInFragment();
            mCalcBusy = false;
        }

    }
}

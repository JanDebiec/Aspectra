package de.jandrotek.android.aspectra.plottest;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewFragment;
import de.jandrotek.android.aspectra.libplotspectrav3.PlotViewPresenter;

public class MainActivity extends AppCompatActivity
    implements ButtonHolderFragment.OnButtonClickListener
{

    public PlotTestController mController = null;
    private PlotViewPresenter mPlotViewPresenter = null;

    private static PlotViewFragment mPlotViewFragment = null;
    private static ButtonHolderFragment mButtonHolderFragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> dummyItems = null;

        mController = new PlotTestController(this);

        mButtonHolderFragment = ButtonHolderFragment.newInstance(this);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentButtonHolder, mButtonHolderFragment)
                .commit();

        mPlotViewFragment = PlotViewFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fvPlotView, mPlotViewFragment)
                .commit();
        mPlotViewPresenter = new PlotViewPresenter(1, mPlotViewFragment);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_ntu, menu);
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

    public void onButtonClickListener(int _buttonId){
        switch (_buttonId) {
            case ButtonHolderFragment.eButtonMoveLeft:{
                mController.onButtonMoveLeft();
                break;
            }
            case ButtonHolderFragment.eButtonMoveRight:{
                mController.onButtonMoveRight();
                break;
            }
            case ButtonHolderFragment.eButtonStretch:{
                mController.onButtonStretch();
                break;
            }
            case ButtonHolderFragment.eButtonSqueeze:{
                mController.onButtonSqeeze();
                break;
            }
        } // switch
    }

    public void updatePlot(int[] data){
        mPlotViewPresenter.updateSinglePlot(0, data);
    }

    public void onFragmentInteraction(Uri uri){

        // do whatever you wish with the uri
    }

}

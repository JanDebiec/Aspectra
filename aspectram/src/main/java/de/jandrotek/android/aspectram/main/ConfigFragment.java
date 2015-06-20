package de.jandrotek.android.aspectram.main;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConfigFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConfigFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigFragment extends Fragment {

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

    public void setPersentStartW(int mPersentStartW) {
        this.mPersentStartW = mPersentStartW;
    }

    public void setPersentEndW(int mPersentEndW) {
        this.mPersentEndW = mPersentEndW;
    }

    public void setPersentStartH(int mPersentStartH) {
        this.mPersentStartH = mPersentStartH;
    }

    public void setDeltaLinesY(int mDeltaLinesY) {
        this.mDeltaLinesY = mDeltaLinesY;
    }

    private int mPersentStartW;
    private int mPersentEndW;
    private int mPersentStartH;
    private int mDeltaLinesY;

    private boolean mSeekBarCreated = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnConfigFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfigFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfigFragment newInstance(String param1, String param2) {
        ConfigFragment fragment = new ConfigFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ConfigFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_config, container, false);
// from old ViewConfigActivity
        // blocks
        mBlockStartW    = (RelativeLayout) rootView.findViewById(R.id.sbcWidthStart);
        mBlockEndW      = (RelativeLayout) rootView.findViewById(R.id.sbcWidthEnd);
        mBlockStartH    = (RelativeLayout) rootView.findViewById(R.id.sbcHeightStart);
        mBlockAreaY     = (RelativeLayout) rootView.findViewById(R.id.sbcHeightEnd);

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

        updateSeekBars();
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnConfigFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        //updateSeekBars();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mSeekBarCreated = false;
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnConfigFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onConfigFragmentInteraction(int startPercentX, int endPercentX, int startPercentY, int deltaLinesY);
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

    protected void updateConfigView() {
        if (mListener != null) {
            mListener.onConfigFragmentInteraction(mPersentStartW, mPersentEndW, mPersentStartH, mDeltaLinesY);
        }
    }

    public void updateSeekBars() {
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

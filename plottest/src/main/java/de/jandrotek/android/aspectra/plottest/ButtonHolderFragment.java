package de.jandrotek.android.aspectra.plottest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by jan on 04.08.15.
 */
public class ButtonHolderFragment extends Fragment {

    OnButtonClickListener mCallback;

    private Button mButtonMoveLeft;
    private Button mButtonMoveRight;
    private Button mButtonStretch;
    private Button mButtonSqueeze;

    public static final int eButtonMoveLeft = 0;
    public static final int eButtonMoveRight = 1;
    public static final int eButtonStretch = 2;
    public static final int eButtonSqueeze = 3;

    PlotTestController mController = null;

    public static ButtonHolderFragment newInstance(MainActivity context) {
        ButtonHolderFragment  fragment = new ButtonHolderFragment();


        fragment.mCallback = (OnButtonClickListener)context;
        fragment.mController = context.mController;
        return fragment;
    }

    public ButtonHolderFragment() {
    }
        public interface OnButtonClickListener {
        // TODO: Update argument type and name
        void onButtonClickListener(int _buttonId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View masterView = inflater.inflate(R.layout.fragment_button_holder, container, false);

        mButtonMoveLeft = (Button)masterView.findViewById(R.id.buttonMoveLeft);
        mButtonMoveRight = (Button)masterView.findViewById(R.id.buttonMoveRight);
        mButtonStretch = (Button)masterView.findViewById(R.id.buttonStretch);
        mButtonSqueeze = (Button)masterView.findViewById(R.id.buttonSqueeze);

        mButtonMoveLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onButtonClickListener(eButtonMoveLeft);

            }
        });
        mButtonMoveRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onButtonClickListener(eButtonMoveRight);

            }
        });
        mButtonStretch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onButtonClickListener(eButtonStretch);

            }
        });
        mButtonSqueeze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onButtonClickListener(eButtonSqueeze);

            }
        });

        return masterView;
    }

}

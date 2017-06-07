/**
 * This file is part of Aspectra.
 *
 * Aspectra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Aspectra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Aspectra.  If not, see <http://www.gnu.org/licenses/lgpl.html>.
 *
 * Copyright Jan Debiec
 */
package de.jandrotek.android.aspectra.viewer;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import java.util.ArrayList;
import java.util.List;

//import de.jandrotek.android.aspectra.main.ListContent.SpectrumItem;

import de.jandrotek.android.aspectra.libspectrafiles.ListContent;

public class ItemListFragment extends ListFragment {
    private static final String TAG = "ListItemsFrag";
    ListView mPrivateListView;
    private SpectrumAdapter mAdapter;
    ArrayList<String> filesNames;
    ListViewerModeListener mModeListener;

    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private Callbacks mCallbacks = sDummyCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;
    public interface Callbacks {
        void onItemSelected(ArrayList<String> filesNames);
    }
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(ArrayList<String> filesNames) {
        }
    };

    public ItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        filesNames = new ArrayList<>();
        mAdapter = new SpectrumAdapter(ListContent.ITEMS);
        setListAdapter(mAdapter);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "onViewCreated");
        }

        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }

        mPrivateListView = getListView();
        mPrivateListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mModeListener = new ListViewerModeListener(
                this, getListView());
        mPrivateListView.setMultiChoiceModeListener(mModeListener        );
        mPrivateListView.clearChoices();
    }

    public boolean performActions(MenuItem item) {
        SparseBooleanArray checked = getListView().getCheckedItemPositions();

        switch (item.getItemId()) {
            case R.id.item_delete: {
                ArrayList<ListContent.SpectrumItem> positions = new ArrayList<>();
                for (int i=0; i < checked.size(); i++) {
                    if (checked.valueAt(i)) {
                        int originalPosition = checked.keyAt(i);
                        positions.add( ListContent.getItem(originalPosition));
                    }
                }
                for (ListContent.SpectrumItem spectrum : positions) {
//                    ListContent.SpectrumItem item;
                    Log.d(TAG, spectrum.getName());
//                   // mAdapter.remove(ListContent.SpectrumItem);
//                    //mAdapter.remove(spectra.get(position));
                }
                return(true);
            }
            case R.id.item_show: {
                ArrayList<ListContent.SpectrumItem> positions = new ArrayList<>();

                int nPlotsCount = checked.size();
                filesNames.clear();
                ItemListActivity activity = (ItemListActivity) getActivity();
                activity.mPlotsCount = nPlotsCount;

                for (int i=0; i < nPlotsCount; i++) {
                    if (checked.valueAt(i)) {
                        int originalPosition = checked.keyAt(i);
                        positions.add( ListContent.getItem(originalPosition));
                    }
                }
                for (ListContent.SpectrumItem spectrum : positions) {
                    String fileName = spectrum.getName();
                    if(BuildConfig.DEBUG) {
                        Log.d(TAG, fileName);
                    }
                    filesNames.add(fileName);
                }
                // call MultiPlotViewer
                mCallbacks.onItemSelected(filesNames);

                return(true);
            }

        }// switch
        return(false);
    }
        @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
            mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "onDetach");
        }

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }


    @Override
    public void onStart() {
        super.onStart();
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "onStart");
        }
        if(mModeListener.activeMode != null) {
            mModeListener.activeMode.finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "onResume");
        }
        if(mModeListener.activeMode != null) {
            mModeListener.activeMode.finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "onPause");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "onStop");
        }
        if(mModeListener.activeMode != null) {
            mModeListener.activeMode.finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "onDestroy");
        }
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        filesNames.clear();
        ListContent.SpectrumItem spectrum = ListContent.getItem(position);
        String fileName = spectrum.getName();
        filesNames.add(fileName);
        mCallbacks.onItemSelected(filesNames);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    private class SpectrumAdapter extends ArrayAdapter<ListContent.SpectrumItem> {

        public SpectrumAdapter(List<ListContent.SpectrumItem> spectra) {
            super(getActivity(), android.R.layout.simple_list_item_activated_2, spectra);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TwoLineListItem twoLineListItem;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                twoLineListItem = (TwoLineListItem) inflater.inflate(
                        android.R.layout.simple_list_item_activated_2, null);
            } else {
                twoLineListItem = (TwoLineListItem) convertView;
            }

            TextView text1 = twoLineListItem.getText1();
            TextView text2 = twoLineListItem.getText2();

            text1.setText(ListContent.ITEMS.get(position).getName());
            text2.setText(ListContent.ITEMS.get(position).getNotes());

            return twoLineListItem;

        }
    }
}



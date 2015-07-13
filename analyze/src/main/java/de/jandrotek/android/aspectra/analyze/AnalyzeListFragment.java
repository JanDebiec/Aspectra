package de.jandrotek.android.aspectra.analyze;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.jandrotek.android.aspectra.libspectrafiles.ListContent;

public class AnalyzeListFragment extends ListFragment {
    //  implements  MultiChoiceModeListener{
    private static final String TAG = "ListItemsFrag";
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    ListView mPrivateListView;
    private SpectrumAdapter mAdapter;
    ArrayList<String> filesNames;
//    AnalyzeListModeListener mModeListener;
    private Callbacks mCallbacks = sDummyCallbacks;
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private ActionMode mActionMode;
    private String mFileName;
    public interface Callbacks {
        void onItemSelected(Map<String, String> spectraNames);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(Map<String, String> spectraNames) {
        }
    };

    public AnalyzeListFragment() {
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
        mPrivateListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mPrivateListView.setLongClickable(true);

    }

        @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

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
    }

    @Override
    public void onResume() {
        super.onResume();
        if(BuildConfig.DEBUG) {
            Log.i(TAG, "onResume");
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
        mFileName = spectrum.getName();
        //TODO: call edit
//        mCallbacks.onItemSelected(filesNames);
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

            final int PositionInList = position;
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

            twoLineListItem.setOnLongClickListener(new View.OnLongClickListener() {
                // Called when the user long-clicks on someView
                public boolean onLongClick(View view) {
                    if (mActionMode != null) {
                        return false;
                    }
                    mFileName = ListContent.ITEMS.get(PositionInList).getName();

                    // Start the CAB using the ActionMode.Callback defined above
                    mActionMode = getActivity().startActionMode(mActionModeCallback);
                    view.setSelected(true);
                    return true;
                }
            });
            return twoLineListItem;

        }
    }



    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_list_menu, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Map<String, String> spectraNames = new HashMap<>();
            switch (item.getItemId()) {
                case R.id.item_set_reference:
                    spectraNames.put(AnalyzeFragment.ARG_ITEM_REFERENCE, mFileName);
                    mode.finish(); // Action picked, so close the CAB
                    mCallbacks.onItemSelected(spectraNames);
                    return true;
                case R.id.item_delete:
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                case R.id.item_edit:
                    spectraNames.put(AnalyzeFragment.ARG_ITEM_EDIT, mFileName);
                    mode.finish(); // Action picked, so close the CAB
                    mCallbacks.onItemSelected(spectraNames);
                    return true;
                default:
                    return false;
            }
        }


        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };
}



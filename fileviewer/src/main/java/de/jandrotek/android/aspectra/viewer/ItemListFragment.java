package de.jandrotek.android.aspectra.viewer;

//TODO: 02.06.2015 do we need that file???
// seems yes, if we have two pane layout

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

//import de.jandrotek.android.aspectra.main.ListContent.SpectrumItem;

import static android.widget.AbsListView.MultiChoiceModeListener;
import static android.widget.AbsListView.OnClickListener;

/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ItemListFragment extends ListFragment {
//  implements  MultiChoiceModeListener{
    private static final String TAG = "ListItemsFrag";

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;


            /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };


    //JD addsOn
    ListView mPrivateListView;
    private SpectrumAdapter mAdapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new SpectrumAdapter(ListContent.ITEMS);
        setListAdapter(mAdapter);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(BuildConfig.DEBUG) {
            Log.i(TAG, "onViewCreated");
        }

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }

        mPrivateListView = getListView();
        mPrivateListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mPrivateListView.setMultiChoiceModeListener(new ViewerModeListener(
                this, getListView())
        //{
//        mPrivateListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
//            @Override
//            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
//                // Capture total checked items
//                final int checkedCount = mPrivateListView.getCheckedItemCount();
//                // Set the CAB title according to total checked items
//                mode.setTitle(checkedCount + " Selected");
//                // Calls toggleSelection method from ListViewAdapter Class
////                SpectrumAdapter.toggleSelection(position);
//                toggleSelection(position);
//
//            }
//
//            @Override
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                //: must return true
//                mode.getMenuInflater().inflate(R.menu.activity_base, menu);
//                mode.getMenuInflater().inflate(R.menu.multi_action_menu, menu);
//                return true;
//            }
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.item_delete:
//
//// temporary disabled, find getSelectedIds()
////                        // Calls getSelectedIds method from ListViewAdapter Class
////                    SparseBooleanArray selected = SpectrumAdapter
////                            .getSelectedIds();
////                    // Captures all selected ids with a loop
////                    for (int i = (selected.size() - 1); i >= 0; i--) {
////                        if (selected.valueAt(i)) {
////                            ListContent.SpectrumItem selecteditem = SpectrumAdapter
////                                    .getItem(selected.keyAt(i));
////                            // Remove selected items following the ids
//////                            SpectrumAdapter.remove(selecteditem);
//             //           }
//                        // Close CAB
//                        mode.finish();
//                        return true;
//               //     }
//                    case R.id.item_show:
//                        //TODO: show more spectra in plot
//                        return true;
//                    case R.id.item_analyze:
//                        Intent intent = new Intent(getActivity(), AnalyzeActivity.class);
//                        startActivity(intent);
//                        mode.finish();
//                        return true;
//                    default:
//                        return false;
//                }
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode mode) {
//
//            }
        //}
        );
    }

    public boolean performActions(MenuItem item) {
        SparseBooleanArray checked=getListView().getCheckedItemPositions();

        switch (item.getItemId()) {

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

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(ListContent.ITEMS.get(position).id);
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
//            super(getActivity(), R.layout.list_item_spectrum, spectra);
            super(getActivity(), android.R.layout.simple_list_item_activated_1, spectra);

        }

        class ViewHolder {
            protected TextView notesText;
            protected TextView nameText;
            protected CheckBox checkbox;
        }

//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View view = null;
//            ListContent.SpectrumItem spectrum = getItem(position);
//            // if we weren't given a view, inflate one
//            if (null == convertView) {
//                view = getActivity().getLayoutInflater()
//                        .inflate(R.layout.list_item_spectrum, null);
//                final ViewHolder viewHolder = new ViewHolder();
//                viewHolder.nameText = (TextView) view.findViewById(R.id.tv_spectra_list_item_name);
//                viewHolder.notesText = (TextView) view.findViewById(R.id.tv_spectra_list_item_notes);
//                viewHolder.checkbox = (CheckBox) view.findViewById(R.id.cb_spectra_list_item_checked);
//                viewHolder.checkbox.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ListContent.SpectrumItem item = (ListContent.SpectrumItem) viewHolder.checkbox.getTag();
//                        item.setSelected(v.isSelected());
//                    }
//                });
//
//
////                viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////                    @Override
////                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////
////                        SpectrumItem item = (SpectrumItem)viewHolder.checkbox.getTag();
////                        item.setSelected(buttonView.isChecked());
////                    }
////                });
//                view.setTag(viewHolder);
//                viewHolder.checkbox.setTag(spectrum);
//            } else {
//                view = convertView;
//                ((ViewHolder) view.getTag()).checkbox.setTag(spectrum);
//            }
//            ViewHolder holder = (ViewHolder) view.getTag();
//            holder.nameText.setText(spectrum.getName());
//            holder.notesText.setText(spectrum.getNotes());
//            holder.checkbox.setChecked(spectrum.isSelected());
//            return view;
//
//        }
//
////        public void toggleSelection(int position) {get
////            ListContent.SpectrumItem spectrum = getItem(position);
////            //boolean actFlag =
////
////        }
    }

    public void toggleSelection(int position) {
        ListContent.SpectrumItem spectrum = mAdapter.getItem(position);
        boolean actFlag = spectrum.isSelected();
        spectrum.setSelected(!actFlag);

    }
            void onCbClick(View v){
                boolean isSelected = v.isSelected();
            }

//            private class DummyAdapter extends ArrayAdapter<SpectrumItem> {
//                public DummyAdapter(List<SpectrumItem> spectra) {
////            public DummyAdapter(ArrayList<SpectrumItem> spectra) {
//                    super(getActivity(), android.R.layout.simple_list_item_1, spectra);
//                }
//
//                @Override
//                public View getView(int position, View convertView, ViewGroup parent) {
//                    // if we weren't given a view, inflate one
//                    if (null == convertView) {
//                        convertView = getActivity().getLayoutInflater()
//                                .inflate(R.layout.list_item_spectrum, null);
//                    }
//
//                    // configure the view for this Crime
//                    ListContent.SpectrumItem spectrum = getItem(position);
//
//                    TextView nameTextView =
//                            (TextView)convertView.findViewById(R.id.tv_spectra_list_item_name);
//                    nameTextView.setText(spectrum.getName());
//                    TextView notesTextView =
//                            (TextView)convertView.findViewById(R.id.tv_spectra_list_item_notes);
//                    notesTextView.setText(spectrum.getNotes());
//                    CheckBox spectraCheckBox =
//                            (CheckBox)convertView.findViewById(R.id.cb_spectra_list_item_checked);
//                    spectraCheckBox.setChecked(false);
//
//                    return convertView;
//                }
}

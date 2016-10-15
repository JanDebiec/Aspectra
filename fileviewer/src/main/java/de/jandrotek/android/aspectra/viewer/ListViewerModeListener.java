package de.jandrotek.android.aspectra.viewer;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ListViewerModeListener implements
        AbsListView.MultiChoiceModeListener{
    ItemListFragment host;
    ActionMode activeMode;
    ListView lv;

    ListViewerModeListener(ItemListFragment host, ListView lv) {
        this.host=host;
        this.lv=lv;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater=host.getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_activity_base, menu);
        inflater.inflate(R.menu.action_menu_multi, menu);
        mode.setTitle(R.string.context_title);
        mode.setSubtitle("(1)");
        activeMode=mode;

        return(true);
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return(false);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        boolean result=host.performActions(item);

        updateSubtitle(activeMode);

        return(result);
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        activeMode=null;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position,
                                          long id, boolean checked) {
        //TODO use checked to changed the state of view
        updateSubtitle(mode);
    }

    private void updateSubtitle(ActionMode mode) {
        mode.setSubtitle("(" + lv.getCheckedItemCount() + ")");
    }
}

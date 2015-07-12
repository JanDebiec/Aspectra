package de.jandrotek.android.aspectra.analyze;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by jan on 24.06.15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AnalyzeListModeListener implements
        AbsListView.MultiChoiceModeListener{
    AnalyzeListFragment host;
    ActionMode activeMode;
    ListView lv;

    AnalyzeListModeListener(AnalyzeListFragment host, ListView lv) {
        this.host=host;
        this.lv=lv;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater=host.getActivity().getMenuInflater();

        inflater.inflate(R.menu.menu_activity_base, menu);
        inflater.inflate(R.menu.action_menu_multi, menu);
//        inflater.inflate(R.menu.context, menu);
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
        updateSubtitle(mode);
    }

    private void updateSubtitle(ActionMode mode) {
        mode.setSubtitle("(" + lv.getCheckedItemCount() + ")");
    }
}

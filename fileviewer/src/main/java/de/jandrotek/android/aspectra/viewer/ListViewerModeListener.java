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

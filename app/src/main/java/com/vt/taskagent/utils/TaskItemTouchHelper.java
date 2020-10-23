package com.vt.taskagent.utils;

import com.vt.taskagent.view.TaskAdapter;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/** Contract to enable swipe to delete recycler view items in app
 *
 * @author Aaron Arellano
 * @verrsion 20201022
 */
public class TaskItemTouchHelper extends ItemTouchHelper.Callback {

    private TaskAdapter mTaskAdapter;

    public TaskItemTouchHelper(TaskAdapter mAdapter) {
        this.mTaskAdapter = mAdapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        // allow us to swipe to the right
        return makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        // TODO do not currently support drag and drop
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mTaskAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}

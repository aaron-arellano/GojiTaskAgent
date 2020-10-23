package com.vt.taskagent.utils;

/** Adapter interface to enable contract between TaskItemTouchHelper and app RecyclerView
 *
 * @author Aaron
 * @version 20201022
 */
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}

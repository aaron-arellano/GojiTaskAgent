package com.vt.taskagent.utils;

/** Adapter interface to enable contract between TaskItemTouchHelper and app RecyclerView
 *
 * @author Aaron
 * @version 20201022
 */
public interface ItemTouchHelperAdapter {

    /** When implemented will have set of actions to complete when item is moved on the app screen
     *
     * @param fromPosition current view item position
     * @param toPosition new view item position
     * @return true if the item moved as expected
     */
    boolean onItemMove(int fromPosition, int toPosition);

    /** When implemented will do some action when the item is swiped off the screen
     *
     * @param position current position of the view item in the recycler view
     */
    void onItemDismiss(int position);
}

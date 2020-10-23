package com.vt.taskagent.utils;

import com.vt.taskagent.R;
import com.vt.taskagent.view.TaskAdapter;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
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
    private Context mContext;
    private ColorDrawable mBackground;

    public TaskItemTouchHelper(TaskAdapter mAdapter, Context context) {
        this.mTaskAdapter = mAdapter;
        this.mContext = context;
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

    // delete the item when we swipe to the right
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mTaskAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }


    // override method to draw red background when deleting
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable trashIcon = mContext.getDrawable(R.drawable.ic_delete);
        View v = viewHolder.itemView;
        int vHeight = v.getBottom() - v.getTop();
        int trashHeight = trashIcon.getIntrinsicHeight();
        int trashWidth = trashIcon.getIntrinsicWidth();

        mBackground = new ColorDrawable(mContext.getColor(R.color.colorRed));
        // set the bounds of the red background within the recycler view viewHolder for the given Task in the list
        // setBounds (left, top, right, bottom),, dx is the offset of the viewHolder size
        int top = (int)(v.getTop() + (vHeight*.05));
        mBackground.setBounds(v.getLeft(), top, v.getRight(), v.getBottom());
        mBackground.draw(c);

        // draw the trash icon next
        int trashTop = v.getTop() + (trashHeight/2);
        int trashBottom = trashTop + trashHeight;
        int trashLeft = v.getLeft() + (trashWidth/2);
        int trashRight = trashLeft + trashWidth;
        trashIcon.setBounds(trashLeft, trashTop, trashRight, trashBottom);
        trashIcon.draw(c);
    }
}

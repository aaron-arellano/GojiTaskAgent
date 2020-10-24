package com.vt.taskagent.view;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;
import com.vt.taskagent.model.Task;
import com.vt.taskagent.model.TaskLab;
import com.vt.taskagent.utils.ItemTouchHelperAdapter;

/** An adapter that bridges the creation and binding of TaskHolder views to RecyclerView so the
 *  use can see their Tasks on the main app activity in a recycler format as they scroll.
 *
 *  @author Aaron Arellano
 *  @version 2020.05.22
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskHolder> implements ItemTouchHelperAdapter{

    //model fields
    private List<Task> mTasks;
    private Context mContext;
    public TaskAdapter(List<Task> tasks) { mTasks = tasks; }

    @Override
    public TaskHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new TaskHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(TaskHolder holder, int position) {
        Task task = mTasks.get(position);
        holder.bind(task);
    }
    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public Task getTaskFromAdapter(int pos) {
        return mTasks.get(pos);
    }

    public List<Task> getTasks() { return mTasks; }

    public void setTasks(List<Task> tasks) {
        mTasks = tasks;
    }

    public void setActivityContext(Context context) { mContext = context; }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        // TODO implement when drag and drop is supported
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        Task task = mTasks.get(position);
        TaskLab.getInstance(mContext).deleteTask(task);
        List<Task> tasks = TaskLab.getInstance(mContext).getTasks();
        this.setTasks(tasks);
        this.notifyItemRemoved(position);
    }
}

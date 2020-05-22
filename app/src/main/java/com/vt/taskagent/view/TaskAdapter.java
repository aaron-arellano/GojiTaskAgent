package com.vt.taskagent.view;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;
import com.vt.taskagent.model.Task;

/** An adapter that bridges the creation and binding of TaskHolder views to RecyclerView so the
 *  use can see their Tasks on the main app activity in a recycler format as they scroll.
 *
 *  @author Aaron Arellano
 *  @version 2020.05.22
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

    //model fields
    private List<Task> mTasks;

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

    public void setTasks(List<Task> tasks) {
        mTasks = tasks;
    }
}

package com.vt.taskagent.view;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DateFormat;

import com.vt.taskagent.R;
import com.vt.taskagent.controller.TaskListFragment;
import com.vt.taskagent.model.Task;

/** The View class that displays everything in the task activity.
 *
 *  @author Aaron Arellano
 *  @version 2020.05.22
 */
public class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //model field
    private Task mTask;
    private String TAG = "Tag";

    //view field
    private TextView mTitleTextView;
    private TextView mDateTextView;

    public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.list_item_task, parent, false));
        // sets listeners to each task in the recycler view
        itemView.setOnClickListener(this);
        mTitleTextView = itemView.findViewById(R.id.task_title);
        mDateTextView = itemView.findViewById(R.id.task_date);
    }

    public void bind(Task task) {
        mTask = task;
        mTitleTextView.setText(mTask.getTitle());
        //set the formatted date from the original date given
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        //Log.v(TAG, dateMedium);
        mDateTextView.setText(df.format(mTask.getRevealedDate()));

        ImageView imageView = itemView.findViewById(R.id.imageView);
        if (mTask.isRealized()) {
            imageView.setImageResource(R.drawable.sun);
        } else if (mTask.isDeferred()){
            imageView.setImageResource(R.drawable.sleepyv2);
        } else {
            imageView.setImageDrawable(null);
        }

    }

    // method for clicking a task
    @Override
    public void onClick(View view) {
        Context context = view.getContext();
        TaskListFragment.Callbacks callbacks = (TaskListFragment.Callbacks) context;
        // this callback gives the functionality for an action to occur on the list of tasks
        callbacks.onTaskSelected(mTask);
    }
}

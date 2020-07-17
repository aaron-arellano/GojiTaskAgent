package com.vt.taskagent.controller;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import com.vt.taskagent.R;
import com.vt.taskagent.model.Task;
import com.vt.taskagent.model.TaskEntry;

/** This is the Activity that displays everything in the main menu
 *  of the application. Implements callbacks for asynchronous calls of the Task and TaskList
 *  fragments. TaskListFragment callbacks only implemented in this concrete class for tablets.
 *
 *  @author Aaron Arellano
 *  @version 2020.05.22
 */

public class TaskListActivity extends SingleFragmentActivity implements TaskFragment.Callbacks, TaskListFragment.Callbacks{

    @Override
    protected Fragment createFragment() {
        return new TaskListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    // called in TaskListFragment, this creates the task activity for the task to pop up.
    public void onTaskSelected(Task task){
        //phone
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = TaskActivity.newIntent(this, task.getId());
            startActivity(intent);
        } else {  //tablet
            Fragment newDetail = TaskFragment.newInstance(task.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    //tablet, method called in this activity because tablet has both fragments in a single pane
    @Override
    public void onTaskUpdated(Task task) {
        TaskListFragment listFragment = (TaskListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
    // not used
    public void onEntrySelected(TaskEntry entry) {}
}

package com.vt.taskagent.controller;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import com.vt.taskagent.R;
import com.vt.taskagent.model.Task;
import com.vt.taskagent.model.TaskLab;
import com.vt.taskagent.utils.TaskItemTouchHelper;
import com.vt.taskagent.view.TaskAdapter;

/** Class controls Android life cyle of the Task List or the entries the user enters to this app.
 *
 *  @author Aaron Arellano
 *  @version 2020.05.22
 */

public class TaskListFragment extends Fragment {

    //view field
    private RecyclerView mTaskRecyclerView;
    private TaskAdapter mTaskAdapter;
    //tablet
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onTaskSelected(Task task);
    }

    //called to let the FragmentManager know that TaskListFragment needs to receive menu callbacks
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        mTaskRecyclerView = view.findViewById(R.id.task_recycler_view);
        mTaskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        // call the ItemTouchHelper and attach to recycler view
        ItemTouchHelper.Callback taskItemTouchHelper = new TaskItemTouchHelper(mTaskAdapter, getContext());
        new ItemTouchHelper(taskItemTouchHelper).attachToRecyclerView(mTaskRecyclerView);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    //tablet
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //gives the fragment access to all methods in the hosting Activity
        // that implement Callbacks
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //set context to null because you cannot access it after this point
        mCallbacks = null;
    }

    //inflating a menu resource
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_task:
                Task task = new Task();
                TaskLab.getInstance(getActivity()).addTask(task);
                /*Intent intent = TaskActivity
                        .newIntent(getActivity(), task.getId());
                startActivity(intent);*/
                updateUI();
                // goes to new task activity once it is created
                mCallbacks.onTaskSelected(task);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI() {
        List<Task> tasks = TaskLab.getInstance(getActivity()).getTasks();
        if (mTaskAdapter == null) {
           mTaskAdapter = new TaskAdapter(tasks);
           mTaskAdapter.setActivityContext(getActivity());
           mTaskRecyclerView.setAdapter(mTaskAdapter);
        } else {
            mTaskAdapter.setTasks(tasks);
            mTaskAdapter.notifyDataSetChanged();
        }
    }
}

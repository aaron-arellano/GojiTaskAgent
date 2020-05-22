package com.vt.taskagent.controller;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import com.vt.taskagent.R;
import com.vt.taskagent.model.Task;
import com.vt.taskagent.model.TaskEntry;
import java.util.UUID;

/** The Task Activity is secondary level activity or shell that contains the necessary fragments
 *  for a task. This activity contains the photo, task entries and task title the user sees in the
 *  app.
 *
 *  @author Aaron Arellano
 *  @version 2020.05.22
 */
public class TaskActivity extends SingleFragmentActivity implements TaskFragment.Callbacks{

    private static final String EXTRA_TASK_ID =
            "taskintent.task_id";

    public static Intent newIntent(Context packageContext, UUID taskId) {
        Intent intent = new Intent(packageContext, TaskActivity.class);
        intent.putExtra(EXTRA_TASK_ID, taskId);
        return intent;
    }

    // not used, implemented in DreaListActivity for tablets.
    @Override
    public void onTaskUpdated(Task task) {}

    // add logic here for a task entry to be edited, this will be in TaskFragment
    @Override
    public void onEntrySelected(TaskEntry entry) {
        // get the current TaskFragment and call the DialogFragment from it to update entry text
        TaskFragment taskFragment = (TaskFragment)
                getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_container);
        FragmentManager manager =
                taskFragment != null ? taskFragment.getFragmentManager() : null;
        DialogPickerFragment dialog = new DialogPickerFragment();
        // create a bundle to send to the dialog picker fragment to track entry
        Bundle args = new Bundle();
        args.putString("entryID", entry.getTaskEntryID().toString());
        dialog.setArguments(args);
        // // // // // // // // // // // // // // // // // // // // // // // //
        dialog.setTargetFragment(
                taskFragment, 0);
        try {
            dialog.show(manager, "DialogComment");
        }
        catch (NullPointerException npe) {
            Log.e("NullFragmentManager", "The Fragment Manager returned null: " + npe.toString());
        }
    }

    @Override
    protected Fragment createFragment() {
        UUID taskId = (UUID) getIntent().getSerializableExtra(EXTRA_TASK_ID);
        return TaskFragment.newInstance(taskId);
    }

    @Override
    public void onBackPressed() {
        EditText title = findViewById(R.id.task_title_hint);
        String titleString = title.getText().toString();
        if (titleString.equals("")) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.picker_extra)
                    .setMessage(R.string.extra_credit_dialogue)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, null)
                    .create().show();
        } else { super.onBackPressed(); }

    }


}

package edu.vt.cs.cs5254.dreamcatcher.controller;

import android.app.Activity;
import androidx.activity.*;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AlertDialog;
import android.widget.EditText;
import edu.vt.cs.cs5254.dreamcatcher.R;
import edu.vt.cs.cs5254.dreamcatcher.model.Dream;
import edu.vt.cs.cs5254.dreamcatcher.model.DreamEntry;

import java.util.UUID;

/** Te Dream Activity is the higher level shell that contains the fragments for a Dream
 *
 */
public class DreamActivity extends SingleFragmentActivity implements DreamFragment.Callbacks{

    private static final String EXTRA_DREAM_ID =
            "dreamintent.dream_id";

    public static Intent newIntent(Context packageContext, UUID dreamId) {
        Intent intent = new Intent(packageContext, DreamActivity.class);
        intent.putExtra(EXTRA_DREAM_ID, dreamId);
        return intent;
    }

    // not used, implemented in DreaListActivity for tablets.
    @Override
    public void onDreamUpdated(Dream dream) {}

    // add logic here for a dream entry to be edited, this will be in DreamFragment
    @Override
    public void onEntrySelected(DreamEntry entry) {
        // get the current DreamFragment and call the DialogFragment from it to update entry text
        DreamFragment dreamFragment = (DreamFragment)
                getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_container);
        FragmentManager manager =
                dreamFragment.getFragmentManager();
        DialogPickerFragment dialog = new DialogPickerFragment();
        // create a bundle to send to the dialog picker fragment to track entry
        Bundle args = new Bundle();
        args.putString("entryID", entry.getDreamEntryID().toString());
        dialog.setArguments(args);
        // // // // // // // // // // // // // // // // // // // // // // // //
        dialog.setTargetFragment(
                dreamFragment, 0);
        dialog.show(manager, "DialogComment");
    }

    @Override
    protected Fragment createFragment() {
        UUID dreamId = (UUID) getIntent().getSerializableExtra(EXTRA_DREAM_ID);
        return DreamFragment.newInstance(dreamId);
    }

    @Override
    public void onBackPressed() {
        EditText title = findViewById(R.id.dream_title_hint);
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

package edu.vt.cs.cs5254.dreamcatcher.controller;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import edu.vt.cs.cs5254.dreamcatcher.R;
import edu.vt.cs.cs5254.dreamcatcher.model.Dream;
import edu.vt.cs.cs5254.dreamcatcher.model.DreamEntry;

import java.util.UUID;

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

    // add logic here to do something when an entry is clicked, this will be in DreamFragment
    @Override
    public void onEntrySelected(DreamEntry entry) {
        // placeholder
        new AlertDialog.Builder(this)
                .setTitle("This is a Place Holder")
                .setMessage("Placeholder")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, null)
                .create().show();
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

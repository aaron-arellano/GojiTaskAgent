package edu.vt.cs.cs5254.dreamcatcher.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

import edu.vt.cs.cs5254.dreamcatcher.R;
import edu.vt.cs.cs5254.dreamcatcher.model.Dream;
import edu.vt.cs.cs5254.dreamcatcher.model.DreamEntry;

/**
 * Created by Aaron on 2/13/2018.  This is the Activity that displays everything in the main menu
 * of the application. Implements callbacks for asynchronous calls of the Dram and DreamList
 * fragments. DreamListFragment callbacks only implemented in this concrete class for tablets.
 */

public class DreamListActivity extends SingleFragmentActivity implements DreamFragment.Callbacks, DreamListFragment.Callbacks{

    @Override
    protected Fragment createFragment() {
        return new DreamListFragment();
    }

    //project 4 tablet
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    // called in DreamListFragment, this creates the dream activity for the dream to pop up.
    public void onDreamSelected(Dream dream){
        //phone
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = DreamActivity.newIntent(this, dream.getId());
            startActivity(intent);
        } else {  //tablet
            Fragment newDetail = DreamFragment.newInstance(dream.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    //tablet, method called in this activity because tablet has both fragments in a single pane
    @Override
    public void onDreamUpdated(Dream dream) {
        DreamListFragment listFragment = (DreamListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
    // not used
    public void onEntrySelected(DreamEntry entry) {}
}

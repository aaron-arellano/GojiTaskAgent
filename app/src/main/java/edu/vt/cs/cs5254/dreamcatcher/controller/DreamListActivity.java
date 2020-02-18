package edu.vt.cs.cs5254.dreamcatcher.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

import edu.vt.cs.cs5254.dreamcatcher.R;
import edu.vt.cs.cs5254.dreamcatcher.model.Dream;

/**
 * Created by Aaron on 2/13/2018.
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

    //called in DreamListFragment
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

    //tablet
    @Override
    public void onDreamUpdated(Dream dream) {
        DreamListFragment listFragment = (DreamListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}

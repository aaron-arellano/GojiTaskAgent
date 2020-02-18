package edu.vt.cs.cs5254.dreamcatcher.controller;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import edu.vt.cs.cs5254.dreamcatcher.R;

/**
 * Created by Aaron on 2/13/2018.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_fragment);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        //createFragment must be implemented by a concrete subclass of SingleFragmentAcSvity.
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    //code for project 4 tablet layout/fragment
    //this will be overridden in DreamListActivity
    @LayoutRes // <-- tells android this is a valid layout ID
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }


    protected abstract Fragment createFragment();

}

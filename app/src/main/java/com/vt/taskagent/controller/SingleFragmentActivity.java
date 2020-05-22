package com.vt.taskagent.controller;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import com.vt.taskagent.R;

/** Interface to inflate the proper view to the app. This class gets the designated fragment and
 *  passes it back to the view.
 *
 *  @author Aaron Arellano
 *  @version 2020.05.22
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        //createFragment must be implemented by a concrete subclass of SingleFragmentActivity.
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @LayoutRes // <-- tells android this is a valid layout ID
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }


    protected abstract Fragment createFragment();

}

package edu.vt.cs.cs5254.dreamcatcher.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.vt.cs.cs5254.dreamcatcher.R;
import edu.vt.cs.cs5254.dreamcatcher.model.Dream;
import edu.vt.cs.cs5254.dreamcatcher.model.DreamLab;
import edu.vt.cs.cs5254.dreamcatcher.view.DreamAdapter;

/**
 * Created by Aaron on 2/13/2018.
 */

public class DreamListFragment extends Fragment {

    //view field
    private RecyclerView mDreamRecyclerView;
    private DreamAdapter mDreamAdapter;
    //tablet
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onDreamSelected(Dream dream);
    }


    //called to let the FragmentManager know that DreamListFragment needs to receive menu callbacks
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dream_list, container, false);
        mDreamRecyclerView = view.findViewById(R.id.dream_recycler_view);
        mDreamRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
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
        inflater.inflate(R.menu.fragment_dream_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_dream:
                Dream dream = new Dream();
                DreamLab.getInstance(getActivity()).addDream(dream);
                /*Intent intent = DreamActivity
                        .newIntent(getActivity(), dream.getId());
                startActivity(intent);*/
                updateUI();
                // goes to new dream activity once it is created
                mCallbacks.onDreamSelected(dream);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI() {
        List<Dream> dreams = DreamLab.getInstance(getActivity()).getDreams();
        if (mDreamAdapter == null) {
           mDreamAdapter = new DreamAdapter(dreams);
           mDreamRecyclerView.setAdapter(mDreamAdapter);
        } else {
            mDreamAdapter.setDreams(dreams);
            mDreamAdapter.notifyDataSetChanged();
        }
    }
}

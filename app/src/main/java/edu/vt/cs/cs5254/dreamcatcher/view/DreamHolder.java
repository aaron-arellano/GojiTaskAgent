package edu.vt.cs.cs5254.dreamcatcher.view;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DateFormat;

import edu.vt.cs.cs5254.dreamcatcher.R;
import edu.vt.cs.cs5254.dreamcatcher.controller.DreamActivity;
import edu.vt.cs.cs5254.dreamcatcher.controller.DreamListFragment;
import edu.vt.cs.cs5254.dreamcatcher.model.Dream;

/**
 * Created by Aaron on 2/13/2018.
 */

public class DreamHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //model field
    private Dream mDream;
    private String TAG = "Tag";

    //view field
    private TextView mTitleTextView;
    private TextView mDateTextView;

    public DreamHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.list_item_dream, parent, false));
        // sets listeners to each dream in the recycler view
        itemView.setOnClickListener(this);
        mTitleTextView = itemView.findViewById(R.id.dream_title);
        mDateTextView = itemView.findViewById(R.id.dream_date);
    }

    public void bind(Dream dream) {
        mDream = dream;
        mTitleTextView.setText(mDream.getTitle());
        //set the formatted date from the original date given
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        //Log.v(TAG, dateMedium);
        mDateTextView.setText(df.format(mDream.getRevealedDate()));

        ImageView imageView = itemView.findViewById(R.id.imageView);
        if (mDream.isRealized()) {
            imageView.setImageResource(R.drawable.sun);
        } else if (mDream.isDeferred()){
            imageView.setImageResource(R.drawable.sleepyv2);
        } else {
            imageView.setImageDrawable(null);
        }

    }

    // method for clicking a dream
    @Override
    public void onClick(View view) {
        Context context = view.getContext();
        DreamListFragment.Callbacks callbacks = (DreamListFragment.Callbacks) context;
        // this callback gives the functionality for an action to occur on the list of dreams
        callbacks.onDreamSelected(mDream);
    }
}

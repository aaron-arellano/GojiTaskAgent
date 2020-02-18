package edu.vt.cs.cs5254.dreamcatcher.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
        itemView.setOnClickListener(this);
        mTitleTextView = itemView.findViewById(R.id.dream_title);
        mDateTextView = itemView.findViewById(R.id.dream_date);
        //don't like this but it is needed for tablet callback
        //DreamListFragment.getCallback().onDreamSelected(mDream);
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

    @Override
    public void onClick(View view) {
        Context context = view.getContext();
        DreamListFragment.Callbacks callbacks = (DreamListFragment.Callbacks) context;
        callbacks.onDreamSelected(mDream);
        /*Intent intent = DreamActivity.newIntent(context, mDream.getId());
        context.startActivity(intent);*/
    }
}

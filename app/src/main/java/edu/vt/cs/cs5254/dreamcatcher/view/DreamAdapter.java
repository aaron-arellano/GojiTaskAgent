package edu.vt.cs.cs5254.dreamcatcher.view;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import edu.vt.cs.cs5254.dreamcatcher.model.Dream;

/**
 * Created by Aaron on 2/13/2018.
 */

public class DreamAdapter extends RecyclerView.Adapter<DreamHolder> {

    //model fields
    private List<Dream> mDreams;

    public DreamAdapter(List<Dream> dreams) { mDreams = dreams; }

    @Override
    public DreamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new DreamHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(DreamHolder holder, int position) {
        Dream dream = mDreams.get(position);
        holder.bind(dream);
    }
    @Override
    public int getItemCount() {
        return mDreams.size();
    }

    public void setDreams(List<Dream> dreams) {
        mDreams = dreams;
    }
}

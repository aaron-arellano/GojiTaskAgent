package edu.vt.cs.cs5254.dreamcatcher.view;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;
import edu.vt.cs.cs5254.dreamcatcher.model.DreamEntry;

public class EntryAdapter extends RecyclerView.Adapter<EntryHolder> {

    //model fields
    private List<DreamEntry> mEntries;

    public EntryAdapter(List<DreamEntry> entries) { mEntries = entries; }

    @Override
    public EntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new EntryHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(EntryHolder holder, int position) {
        DreamEntry entry = mEntries.get(position);
        holder.bind(entry);
    }
    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    public void setDreamEntries(List<DreamEntry> entries) { mEntries = entries; }
}

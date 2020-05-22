package com.vt.taskagent.view;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;
import com.vt.taskagent.model.TaskEntry;

/** An adapter that bridges the creation and binding of EntryHolder views to RecyclerView so the
 *  use can see their task entries on the entry activity in a recycler format as they scroll.
 *
 *  @author Aaron Arellano
 *  @version 2020.05.22
 *  */
public class EntryAdapter extends RecyclerView.Adapter<EntryHolder> {

    //model fields
    private List<TaskEntry> mEntries;

    public EntryAdapter(List<TaskEntry> entries) { mEntries = entries; }

    @Override
    public EntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new EntryHolder(layoutInflater, parent);
    }

    @Override
    public void onBindViewHolder(EntryHolder holder, int position) {
        TaskEntry entry = mEntries.get(position);
        holder.bind(entry);
    }
    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    public void setTaskEntries(List<TaskEntry> entries) { mEntries = entries; }
}

package com.vt.taskagent.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/** Definition of the task itself. Holds all the necessary properties which are stored and retrieved
 *  from the sqlite DB
 *
 * @author Aaron
 * @version 20201022
 */
public class Task {

    private UUID mId;
    private String mTitle;
    private Date mRevealedDate;
    private Date mCheckboxDate;
    private boolean mRealized;
    private boolean mDeferred;
    private List<TaskEntry> mTaskEntries;

    public Task() {
        this(UUID.randomUUID());
        mTitle = null;
        mRealized = false;
        mDeferred = false;
        mTaskEntries = new ArrayList<>();
    }

    public Task(UUID id) {
        mId = id;
        mRevealedDate = new Date();
    }

    // getters
    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public Date getRevealedDate() {
        return mRevealedDate;
    }

    public boolean isRealized() {
        return mRealized;
    }

    public boolean isDeferred() { return mDeferred; }

    public List<TaskEntry> getTaskEntries() { return mTaskEntries; }

    // setters
    public void setTitle(String title) {
        mTitle = title;
    }

    public void setRevealedDate(Date revealedDate) {
        mRevealedDate = revealedDate;
    }

    public void setRealized(boolean realized) {
        mRealized = realized;
    }

    public void setDeferred(boolean deferred) { mDeferred = deferred; }

    // convenience methods

    public void addRevealed() {
        TaskEntry entry = new TaskEntry("Task Revealed", new Date(), TaskEntryKind.REVEALED);
        mTaskEntries.add(entry);
    }

    public void addComment(String text) {
        TaskEntry entry = new TaskEntry(text, new Date(), TaskEntryKind.COMMENT);
        mTaskEntries.add(entry);
    }

    public void addTaskRealized() {
        TaskEntry entry = new TaskEntry("Task Realized", new Date(), TaskEntryKind.REALIZED);
        mTaskEntries.add(entry);
    }

    public void addTaskDeferred() {
        TaskEntry entry = new TaskEntry("Task Deferred", new Date(), TaskEntryKind.DEFERRED);
        mTaskEntries.add(entry);
    }

    // method to update task entry comments
    public void updateEntryComment(String comment, String uuid) {
        int pos = 0;
        for (TaskEntry entry : mTaskEntries) {
            if (entry.getTaskEntryID().toString().equals(uuid)) {
                break;
            }
            pos++;
        }
        // update the entry in the array, logic in place for DB already
        TaskEntry entry = mTaskEntries.get(pos);
        entry.setText(comment);
        mTaskEntries.set(pos, entry);
    }

    // Consider consolidating further into a method
    // (for example, selectTaskRealized) that includes:
    // (1) logic related to deferred, ensure deferred is not set
    // (2) setting realized, if not deferred ,set realized to true
    // (3) creating + adding realized task entry

    public void removeTaskRealized() {
        Iterator<TaskEntry> iterator = mTaskEntries.iterator();
        while (iterator.hasNext()) {
            TaskEntry e = iterator.next();
            if (e.getKind() == TaskEntryKind.REALIZED) {
                //want to remove element from the iterator not the task entries
                //mTaskEntries.remove(e);
                iterator.remove();
            }
        }
    }

    public void removeTaskDeferred() {
        Iterator<TaskEntry> iterator = mTaskEntries.iterator();
        while (iterator.hasNext()) {
            TaskEntry e = iterator.next();
            if (e.getKind() == TaskEntryKind.DEFERRED) {
                //want to remove element from the iterator not the task entries
                //mTaskEntries.remove(e);
                iterator.remove();
            }
        }
    }

    public void setTaskEntries(List<TaskEntry> taskEntries) {
        mTaskEntries = taskEntries;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

    public Date getCheckboxDate() {
        return mCheckboxDate;
    }

    public void setCheckboxDate(Date checkboxDate) {
        mCheckboxDate = checkboxDate;
    }
}

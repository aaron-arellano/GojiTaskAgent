package edu.vt.cs.cs5254.dreamcatcher.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dream {

    private UUID mId;
    private String mTitle;
    private Date mRevealedDate;
    private Date mCheckboxDate;
    private boolean mRealized;
    private boolean mDeferred;
    private List<DreamEntry> mDreamEntries;

    public Dream() {
        this(UUID.randomUUID());
        mTitle = null;
        mRealized = false;
        mDeferred = false;
        mDreamEntries = new ArrayList<>();
    }

    public Dream(UUID id) {
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

    public List<DreamEntry> getDreamEntries() { return mDreamEntries; }

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
        DreamEntry entry = new DreamEntry("Dream Revealed", new Date(), DreamEntryKind.REVEALED);
        mDreamEntries.add(entry);
    }

    public void addComment(String text) {
        DreamEntry entry = new DreamEntry(text, new Date(), DreamEntryKind.COMMENT);
        mDreamEntries.add(entry);
    }

    public void addDreamRealized() {
        DreamEntry entry = new DreamEntry("Dream Realized", new Date(), DreamEntryKind.REALIZED);
        mDreamEntries.add(entry);
    }

    public void addDreamDeferred() {
        DreamEntry entry = new DreamEntry("Dream Deferred", new Date(), DreamEntryKind.DEFERRED);
        mDreamEntries.add(entry);
    }

    // method to update dream entry comments
    public void updateEntryComment(String comment, String uuid) {
        int pos = 0;
        for (DreamEntry entry : mDreamEntries) {
            if (entry.getDreamEntryID().toString().equals(uuid)) {
                break;
            }
            pos++;
        }
        // update the entry in the array, logic in place for DB already
        DreamEntry entry = mDreamEntries.get(pos);
        entry.setText(comment);
        mDreamEntries.set(pos, entry);
    }

    // Consider consolidating further into a method
    // (for example, selectDreamRealized) that includes:
    // (1) logic related to deferred, ensure deferred is not set
    // (2) setting realized, if not deferred ,set realized to true
    // (3) creating + adding realized dream entry

    public void removeDreamRealized() {
        Iterator<DreamEntry> iterator = mDreamEntries.iterator();
        while (iterator.hasNext()) {
            DreamEntry e = iterator.next();
            if (e.getKind() == DreamEntryKind.REALIZED) {
                //want to remove element from the iterator not the dream entries
                //mDreamEntries.remove(e);
                iterator.remove();
            }
        }
    }

    public void removeDreamDeferred() {
        Iterator<DreamEntry> iterator = mDreamEntries.iterator();
        while (iterator.hasNext()) {
            DreamEntry e = iterator.next();
            if (e.getKind() == DreamEntryKind.DEFERRED) {
                //want to remove element from the iterator not the dream entries
                //mDreamEntries.remove(e);
                iterator.remove();
            }
        }
    }

    public void setDreamEntries(List<DreamEntry> dreamEntries) {
        mDreamEntries = dreamEntries;
    }

    //project4
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

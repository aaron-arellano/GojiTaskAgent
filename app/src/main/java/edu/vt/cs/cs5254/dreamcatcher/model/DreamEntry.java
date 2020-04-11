package edu.vt.cs.cs5254.dreamcatcher.model;

import java.util.Date;
import java.util.UUID;

/** Defines the properties of a Dream Entry
 *
 */
public class DreamEntry {

    private String mText;
    private Date mDate;
    private DreamEntryKind mKind;
    // created to keep track of dreams
    private UUID mdreamEntryID;

    public DreamEntry(String text, Date date, DreamEntryKind kind) {
        this(UUID.randomUUID());
        mText = text;
        mDate = date;
        mKind = kind;
    }

    // add secondary constructor for rebuilding DreamEntry from SQLite
    public DreamEntry(UUID dreamEntryID) {
        mdreamEntryID = dreamEntryID;
    }

    public String getText() {
        return mText;
    }

    public Date getDate() {
        return mDate;
    }

    public DreamEntryKind getKind() {
        return mKind;
    }

    // return the dream entry ID to track it
    public UUID getDreamEntryID() { return mdreamEntryID; }

    // add setters for SQLite scenario
    public void setText(String text) {
        mText = text;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setKind(DreamEntryKind kind) {
        mKind = kind;
    }

}

package com.vt.taskagent.model;

import java.util.Date;
import java.util.UUID;

/** Defines the properties of a Task Entry, the view within inner activity that extends the main
 *  activity in the app. This model grabs the entry information from the db and sets it here.
 *
 * @author Aaron Arellano
 * @version 2020.05.22
 */
public class TaskEntry {

    private String mText;
    private Date mDate;
    private TaskEntryKind mKind;
    // created to keep track of tasks
    private UUID mTaskEntryID;

    public TaskEntry(String text, Date date, TaskEntryKind kind) {
        this(UUID.randomUUID());
        mText = text;
        mDate = date;
        mKind = kind;
    }

    // add secondary constructor for rebuilding TaskEntry from SQLite
    public TaskEntry(UUID taskEntryID) {
        mTaskEntryID = taskEntryID;
    }

    public String getText() {
        return mText;
    }

    public Date getDate() {
        return mDate;
    }

    public TaskEntryKind getKind() {
        return mKind;
    }

    // return the task entry ID to track it
    public UUID getTaskEntryID() { return mTaskEntryID; }

    // add setters for SQLite scenario
    public void setText(String text) {
        mText = text;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setKind(TaskEntryKind kind) {
        mKind = kind;
    }

}

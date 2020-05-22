package com.vt.taskagent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.vt.taskagent.database.TaskBaseHelper;
import com.vt.taskagent.database.TaskDbSchema.*;

/** The factory for creating a task entry, this class gets the entry information from the view
 *  which was passed by the controller and places the data into the local SQLite db.
 *
 *  @author Aaron Arellano
 *  @version 2020.05.22
 */

public class TaskEntryLab {

    private static TaskEntryLab sTaskEntryLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static TaskEntryLab getInstance(Context context) {
        if (sTaskEntryLab == null) { sTaskEntryLab = new TaskEntryLab(context); }
        return sTaskEntryLab;
    }

    private TaskEntryLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TaskBaseHelper(mContext).getWritableDatabase();
    }

    //use the ENUM.name() to store the string value of the enum in the SQlite db
    private static ContentValues getEntryValues(TaskEntry entry, Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskEntryTable.Cols.TEXT, entry.getText());
        values.put(TaskEntryTable.Cols.DATE, entry.getDate().getTime());
        values.put(TaskEntryTable.Cols.KIND, entry.getKind().name());
        values.put(TaskEntryTable.Cols.ENTRYID, entry.getTaskEntryID().toString());
        values.put(TaskEntryTable.Cols.UUID, task.getId().toString());
        return values;
    }

    //simple implementation compared to the other
    public void addTaskEntry(TaskEntry entry, Task task) {
        ContentValues values = getEntryValues(entry, task);
        mDatabase.insert(TaskEntryTable.NAME, null, values);
    }

    /*The method first removes *all* of the entries associated with the specified task from
    the database ( using mDatabase.delete(...) ), and then adds them again - one at a time - by
    iterating over the task's list of entries.
    */
    public void updateTaskEntries(Task task) {
        String uuidString = task.getId().toString();
        //delete all entries for this task in the db
        mDatabase.delete(TaskEntryTable.NAME,
                TaskEntryTable.Cols.UUID + " = ?",
                new String[] { uuidString }); // is stored as a String in DB
        //iterate through entries entry Task obj
        //add them to the db one at a time
        for (TaskEntry entry: task.getTaskEntries()) {
            addTaskEntry(entry, task);
        }
    }

    //Need TaskEntryCursorWrapper() in model package
    public List<TaskEntry> getTaskEntries(Task task) {
        List<TaskEntry> entries = new ArrayList<>();
        TaskEntryCursorWrapper cursor = queryEntries(null, null);
        int i = 0;
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                //compare the two uuids for equality
                if(task.getId().equals(cursor.getUuid())) {
                    entries.add(cursor.getEntry());
                }
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return entries;
    }

    private TaskEntryCursorWrapper queryEntries(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TaskEntryTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new TaskEntryCursorWrapper(cursor);
    }
}

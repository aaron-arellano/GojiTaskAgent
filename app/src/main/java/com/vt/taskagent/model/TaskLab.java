package com.vt.taskagent.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.vt.taskagent.database.TaskDbSchema.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vt.taskagent.database.TaskBaseHelper;

/** Like the factory that created the entries and stored them in the local SQLite db, this class
 *  creates tasks at the main view level and stores their appropriate information.
 *
 * @author Aaron Arellano
 *  @version 2020.05.22
 */
public class TaskLab {

    private static TaskLab sTaskLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static TaskLab getInstance(Context context) {
        if (sTaskLab == null) { sTaskLab = new TaskLab(context); }
        return sTaskLab;
    }

    private TaskLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new TaskBaseHelper(mContext).getWritableDatabase();
    }

    public List<Task> getTasks() { //return new ArrayList<>();
        List<Task> tasks = new ArrayList<>();
        TaskCursorWrapper cursor = queryTasks(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tasks.add(cursor.getTask());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return tasks;
    }

    public Task getTask(UUID id) {
        TaskCursorWrapper cursor = queryTasks(
                TaskTable.Cols.UUID + " = ?",
                new String[]{ id.toString() }
        );
        try {
            if (cursor.getCount() == 0) { return null; }
            cursor.moveToFirst();
            Task task = cursor.getTask();
            List<TaskEntry> entries =
                    TaskEntryLab.getInstance(mContext).getTaskEntries(task);
            task.setTaskEntries(entries);
            return task;
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getTaskValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskTable.Cols.UUID, task.getId().toString());
        values.put(TaskTable.Cols.TITLE, task.getTitle());
        values.put(TaskTable.Cols.DATE, task.getRevealedDate().getTime());
        values.put(TaskTable.Cols.DEFERRED, task.isDeferred() ? 1 : 0);
        values.put(TaskTable.Cols.REALIZED, task.isRealized() ? 1 : 0);
        return values;
    }

    public void addTask(Task task) {
        if(task.getTitle() == null) {
            task.addRevealed();
        }
        ContentValues values = getTaskValues(task);
        mDatabase.insert(TaskTable.NAME, null, values);
        for (TaskEntry entry: task.getTaskEntries()) {
            TaskEntryLab.getInstance(mContext).addTaskEntry(entry, task);
        }
    }

    public void updateTask(Task task) {
        String uuidString = task.getId().toString();
        ContentValues values = getTaskValues(task);
        //Log.v("HEY", "UPDATE :      " + task.getTitle());
        mDatabase.update(TaskTable.NAME, values,
                TaskTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    // Added to enable swipe and delete tasks from database
    public void deleteTask(Task task) {
        String uuidString = task.getId().toString();
        mDatabase.delete(TaskTable.NAME,
                TaskTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    //private Cursor queryTasks(String whereClause, String[] whereArgs) {
    private TaskCursorWrapper queryTasks(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TaskTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new TaskCursorWrapper(cursor);
    }

    //project4
    public File getPhotoFile(Task task) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, task.getPhotoFilename());
    }
}
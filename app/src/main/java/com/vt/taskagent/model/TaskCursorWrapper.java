package com.vt.taskagent.model;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import com.vt.taskagent.database.TaskDbSchema.*;

/** This class creates a cursor that tracks tasks in the SQLite DB.
 *
 * @author Aaron Arellano
 * @version 2020.05.22
 */

public class TaskCursorWrapper extends CursorWrapper {

    public TaskCursorWrapper(Cursor cursor) { super(cursor); }

    public Task getTask() {
        String uuidString = getString(getColumnIndex(TaskTable.Cols.UUID));
        String title = getString(getColumnIndex(TaskTable.Cols.TITLE));
        long date = getLong(getColumnIndex(TaskTable.Cols.DATE));
        int isDeferred = getInt(getColumnIndex(TaskTable.Cols.DEFERRED));
        int isRealized = getInt(getColumnIndex(TaskTable.Cols.REALIZED));
        Task task = new Task(UUID.fromString(uuidString));
        task.setTitle(title);
        task.setRevealedDate(new Date(date));
        task.setDeferred(isDeferred != 0);
        task.setRealized(isRealized != 0);
        return task;
    }
}

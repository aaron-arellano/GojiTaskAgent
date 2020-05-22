package com.vt.taskagent.model;

import android.database.Cursor;
import android.database.CursorWrapper;
import java.util.Date;
import java.util.UUID;
import com.vt.taskagent.database.TaskDbSchema.*;

/** The cursor that handles querying of task entries from the back-end database.
 *
 *  @author Aaron Arellano
 *  @version 2020.05.22
 */

public class TaskEntryCursorWrapper extends CursorWrapper {

    private UUID mId;
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public TaskEntryCursorWrapper(Cursor cursor) { super(cursor); }

    public TaskEntry getEntry() {
        String text = getString(getColumnIndex(TaskEntryTable.Cols.TEXT));
        long date = getLong(getColumnIndex(TaskEntryTable.Cols.DATE));
        String kind = getString(getColumnIndex(TaskEntryTable.Cols.KIND));
        TaskEntryKind k = TaskEntryKind.valueOf(kind);
        String uuidString = getString(getColumnIndex(TaskEntryTable.Cols.ENTRYID));
        TaskEntry entry = new TaskEntry(UUID.fromString(uuidString));
        entry.setText(text);
        entry.setDate(new Date(date));
        entry.setKind(k);
        return entry;
    }

    public UUID getUuid() {
        String uuidString = getString(getColumnIndex(TaskEntryTable.Cols.UUID));
        mId = UUID.fromString(uuidString);
        return mId;
    }
}

package edu.vt.cs.cs5254.dreamcatcher.model;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import edu.vt.cs.cs5254.dreamcatcher.database.DreamDbSchema.*;

/**
 * Created by Aaron on 3/10/2018.
 */

public class DreamEntryCursorWrapper extends CursorWrapper {

    private UUID mId;
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public DreamEntryCursorWrapper(Cursor cursor) { super(cursor); }

    public DreamEntry getEntry() {
        String text = getString(getColumnIndex(DreamEntryTable.Cols.TEXT));
        long date = getLong(getColumnIndex(DreamEntryTable.Cols.DATE));
        String kind = getString(getColumnIndex(DreamEntryTable.Cols.KIND));
        DreamEntryKind k = DreamEntryKind.valueOf(kind);
        String uuidString = getString(getColumnIndex(DreamEntryTable.Cols.ENTRYID));
        DreamEntry entry = new DreamEntry(UUID.fromString(uuidString));
        entry.setText(text);
        entry.setDate(new Date(date));
        entry.setKind(k);
        return entry;
    }

    public UUID getUuid() {
        String uuidString = getString(getColumnIndex(DreamEntryTable.Cols.UUID));
        mId = UUID.fromString(uuidString);
        return mId;
    }
}

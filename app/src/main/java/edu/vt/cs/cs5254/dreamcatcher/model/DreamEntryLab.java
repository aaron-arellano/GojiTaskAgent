package edu.vt.cs.cs5254.dreamcatcher.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.vt.cs.cs5254.dreamcatcher.database.DreamBaseHelper;
import edu.vt.cs.cs5254.dreamcatcher.database.DreamDbSchema.*;

/**
 * Created by Aaron on 3/7/2018.
 */

public class DreamEntryLab {

    private static DreamEntryLab sDreamEntryLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static DreamEntryLab getInstance(Context context) {
        if (sDreamEntryLab == null) { sDreamEntryLab = new DreamEntryLab(context); }
        return sDreamEntryLab;
    }

    private DreamEntryLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new DreamBaseHelper(mContext).getWritableDatabase();
    }

    //use the ENUM.name() to store the string value of the enum in the SQlite db
    private static ContentValues getEntryValues(DreamEntry entry, Dream dream) {
        ContentValues values = new ContentValues();
        values.put(DreamEntryTable.Cols.TEXT, entry.getText());
        values.put(DreamEntryTable.Cols.DATE, entry.getDate().getTime());
        values.put(DreamEntryTable.Cols.KIND, entry.getKind().name());
        values.put(DreamEntryTable.Cols.ENTRYID, entry.getDreamEntryID().toString());
        values.put(DreamEntryTable.Cols.UUID, dream.getId().toString());
        return values;
    }

    //simple implementation compared to the other
    public void addDreamEntry(DreamEntry entry, Dream dream) {
        ContentValues values = getEntryValues(entry, dream);
        mDatabase.insert(DreamEntryTable.NAME, null, values);
    }

    /*The method first removes *all* of the entries associated with the specified dream from
    the database ( using mDatabase.delete(...) ), and then adds them again - one at a time - by
    iterating over the dream's list of entries.
    */
    public void updateDreamEntries(Dream dream) {
        String uuidString = dream.getId().toString();
        //delete all entries for this dream in the db
        mDatabase.delete(DreamEntryTable.NAME,
                DreamEntryTable.Cols.UUID + " = ?",
                new String[] { uuidString }); // is stored as a String in DB
        //iterate through entries entry Dream obj
        //add them to the db one at a time
        for (DreamEntry entry: dream.getDreamEntries()) {
            addDreamEntry(entry, dream);
        }
    }

    //Need DreamEntryCursorWrapper() in model package
    public List<DreamEntry> getDreamEntries(Dream dream) {
        List<DreamEntry> entries = new ArrayList<>();
        DreamEntryCursorWrapper cursor = queryEntries(null, null);
        int i = 0;
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                //compare the two uuids for equality
                if(dream.getId().equals(cursor.getUuid())) {
                    entries.add(cursor.getEntry());
                }
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return entries;
    }

    private DreamEntryCursorWrapper queryEntries(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DreamEntryTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new DreamEntryCursorWrapper(cursor);
    }
}

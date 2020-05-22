package com.vt.taskagent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.vt.taskagent.database.TaskDbSchema.*;

/** Helper class to perform SQL queries on the task entries and tasks in this app.
 *
 * @author Aaron Arellano
 * @version 2020.05.22
 */

public class TaskBaseHelper extends SQLiteOpenHelper {

    public static final int VERSION = 2;
    public static final String DATABASE_NAME = "taskBase.db";

    public TaskBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    /* Execute xml to create database
     *
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TaskTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                TaskTable.Cols.UUID + " text not null unique, " +
                TaskTable.Cols.TITLE + " text, " +
                TaskTable.Cols.DATE + " integer, " +
                TaskTable.Cols.DEFERRED + " integer, " +
                TaskTable.Cols.REALIZED + " integer)"
        );
        db.execSQL("create table " + TaskEntryTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                TaskEntryTable.Cols.TEXT + " text, " +
                TaskEntryTable.Cols.DATE + " integer, " +
                TaskEntryTable.Cols.KIND + " text, " +
                TaskEntryTable.Cols.ENTRYID + " text not null, " +
                TaskEntryTable.Cols.UUID + " text not null, " +
                "foreign key (" + TaskEntryTable.Cols.UUID + ") references " +
                TaskTable.NAME + "(" + TaskTable.Cols.UUID + "))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
    }
}

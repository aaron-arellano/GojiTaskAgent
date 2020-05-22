package com.vt.taskagent.database;

/** SQLite Schema for the tasks and task entries.
 *
 * @author Aaron Arellano
 * @version 2020.05.22
 */

public class TaskDbSchema {
    public static final class TaskTable {
        public static final String NAME = "task";

        public static final class Cols {
            public static final String UUID = "task_uuid";
            public static final String TITLE = "task_title";
            public static final String DATE = "task_date";
            public static final String DEFERRED = "task_deferred";
            public static final String REALIZED = "task_realized";
        }
    }

    public static final class TaskEntryTable {
        public static final String NAME = "task_entry";

        public static final class Cols {
            public static final String TEXT = "entry_text";
            public static final String DATE = "entry_date";
            public static final String KIND = "entry_kind";
            public static final String ENTRYID = "entry_uuid";
            public static final String UUID = "task_uuid";
        }
    }
}

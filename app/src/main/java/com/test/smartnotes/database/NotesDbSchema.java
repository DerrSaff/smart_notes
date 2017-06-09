package com.test.smartnotes.database;

/**
 * Created by saff on 08.06.17.
 */

public class NotesDbSchema {
    public static final class NotesTable {
        public static final String NAME = "NOTES";

        public static final class Cols {
            public static final String ID ="_id";
            public static final String NOTE_TITLE ="NOTE_TITLE";
            public static final String NOTE_TEXT ="NOTE_TEXT";
            public static final String IMPORTANCE ="IMPORTANCE";
            public static final String IMAGE_PATH ="IMAGE_PATH";
            public static final String LONGITUDE ="LONGITUDE";
            public static final String LATITUDE ="LATITUDE";
        }
    }
}

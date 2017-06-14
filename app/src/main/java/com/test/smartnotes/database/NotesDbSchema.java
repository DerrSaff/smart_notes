package com.test.smartnotes.database;

/**
 * Created by saff on 08.06.17.
 */

public class NotesDbSchema {
    public static final class NotesTable {
        public static final String NAME = "notes";

        public static final class Cols {
            public static final String ID ="_id";
            public static final String NOTE_TITLE ="note_title";
            public static final String NOTE_TEXT ="note_text";
            public static final String IMPORTANCE ="importance";
            public static final String IMAGE_PATH ="image_path";
            public static final String LONGITUDE ="longitude";
            public static final String LATITUDE ="latitude";
        }
    }
}

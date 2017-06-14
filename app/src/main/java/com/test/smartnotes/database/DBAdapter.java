package com.test.smartnotes.database;

/**
 * Created by saff on 08.06.17.
 */

import android.content.ContentValues;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.SQLException;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.test.smartnotes.database.NotesDbSchema.NotesTable;

import java.util.ArrayList;
import java.util.List;

public class DBAdapter {

    /** if debug is set true then it will show all Logcat message **/
    public static final boolean DEBUG = true;

    /** Logcat TAG **/
    public static final String LOG_TAG = "DBAdapter";

    /** Database Version **/
    private static final int DATABASE_VERSION = 1;

    /** Database Name **/
    private static final String DATABASE_NAME = "SmartNotes.db";

    /** Used to open database in synchronized way **/
    private static DataBaseHelper DBHelper = null;

    public static void init(Context context) {
        if (DBHelper == null) {
            if (DEBUG)
                Log.i(LOG_TAG, context.toString());
            DBHelper = new DataBaseHelper(context);
        }
    }

    /** Create notes table **/
    private static final String NOTES_CREATE = "CREATE TABLE " + NotesTable.NAME +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NotesTable.Cols.NOTE_TITLE + " TEXT, " +
            NotesTable.Cols.NOTE_TEXT + " TEXT, " +
            NotesTable.Cols.IMPORTANCE + " INTEGER, " +
            NotesTable.Cols.IMAGE_PATH + " TEXT, " +
            NotesTable.Cols.LATITUDE + " REAL, " +
            NotesTable.Cols.LONGITUDE + " REAL);";

    /** Main Database creation INNER class **/
    private static class DataBaseHelper extends SQLiteOpenHelper {
        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            if (DEBUG)
                Log.i(LOG_TAG, "notes table create");
            try {
                db.execSQL(NOTES_CREATE);


            } catch (Exception exception) {
                if (DEBUG)
                    Log.i(LOG_TAG, "Exception onCreate() exception");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

    }

    /** Open database for insert, update, delete in synchronized manner **/
    private static synchronized SQLiteDatabase open() throws SQLException {
        return DBHelper.getWritableDatabase();
    }

    /** Escape string for single quotes (insert, update) **/
    private static String sqlEscapeString(String aString) {
        String aReturn = "";

        if (null != aString) {
            //aReturn = aString.replace("'", "''");
            aReturn = DatabaseUtils.sqlEscapeString(aString);
            // Remove the enclosing single quotes ...
            aReturn = aReturn.substring(1, aReturn.length() - 1);
        }

        return aReturn;
    }

    /** UnEscape string for single quotes (show data) **/
    private static String sqlUnEscapeString(String aString) {

        String aReturn = "";

        if (null != aString) {
            aReturn = aString.replace("''", "'");
        }

        return aReturn;
    }

    // CRUD Operations

    /** Add a new Note **/
    public static void addNoteData(NoteData noteData) {
        final SQLiteDatabase db = open();

        String noteTitle = sqlEscapeString(noteData.getNoteTitle());
        String noteText = sqlEscapeString(noteData.getNoteText());
        int importance = noteData.getImportance();
        String imagePath = noteData.getImagePath();
        double latitude = noteData.getLatitude();
        double longitude = noteData.getLongitude();

        ContentValues noteValues = new ContentValues();
        noteValues.put(NotesTable.Cols.NOTE_TITLE, noteTitle);
        noteValues.put(NotesTable.Cols.NOTE_TEXT, noteText);
        noteValues.put(NotesTable.Cols.IMPORTANCE, importance);
        noteValues.put(NotesTable.Cols.IMAGE_PATH, imagePath);
        noteValues.put(NotesTable.Cols.LATITUDE, latitude);
        noteValues.put(NotesTable.Cols.LONGITUDE, longitude);

        db.insert(NotesTable.NAME, null, noteValues);
        db.close();
    }

    /** Get a Note **/
    public static NoteData getNoteData(int id) {
        final SQLiteDatabase db = open();

        Cursor cursor =
            db.query(NotesTable.NAME,
                new String[] {
                    NotesTable.Cols.ID, NotesTable.Cols.NOTE_TITLE, NotesTable.Cols.NOTE_TEXT,
                    NotesTable.Cols.IMPORTANCE, NotesTable.Cols.IMAGE_PATH, NotesTable.Cols.LATITUDE,
                    NotesTable.Cols.LONGITUDE
                },
                NotesTable.Cols.ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return new NoteData(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Integer.parseInt(cursor.getString(3)),
                cursor.getString(4),
                Double.parseDouble(cursor.getString(5)),
                Double.parseDouble(cursor.getString(6))
        );

    }

    /** Get All Notes **/
    public static List<NoteData> getAllUserData() {
        final SQLiteDatabase db = open();
        String selectQuery = "SELECT * FROM " + NotesTable.NAME + ";";
        Cursor cursor = db.rawQuery(selectQuery, null);

        List<NoteData> notesList = new ArrayList<>();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                NoteData data = new NoteData();
                data.setID(Integer.parseInt(cursor.getString(0)));
                data.setNoteTitle(cursor.getString(1));
                data.setNoteText(cursor.getString(2));
                data.setImportance(Integer.parseInt(cursor.getString(3)));
                data.setImagePath(cursor.getString(4));
                data.setLatitude(Double.parseDouble(cursor.getString(5)));
                data.setLongitude(Double.parseDouble(cursor.getString(6)));

                notesList.add(data);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return notesList;
    }

    /** Update a Note **/
    public static int updateNoteData(NoteData data) {
        final SQLiteDatabase db = open();

        ContentValues noteValues = new ContentValues();
        noteValues.put(NotesTable.Cols.NOTE_TITLE, data.getNoteTitle());
        noteValues.put(NotesTable.Cols.NOTE_TEXT, data.getNoteText());
        noteValues.put(NotesTable.Cols.IMPORTANCE, data.getImportance());
        noteValues.put(NotesTable.Cols.IMAGE_PATH, data.getImagePath());
        noteValues.put(NotesTable.Cols.LATITUDE, data.getLatitude());
        noteValues.put(NotesTable.Cols.LONGITUDE, data.getLongitude());

        return db.update(NotesTable.NAME, noteValues, NotesTable.Cols.ID + " = ?",
                new String[] { String.valueOf(data.getID()) });
    }

    /** Delete a Note **/
    public static void deleteUserData(NoteData data) {
        final SQLiteDatabase db = open();
        db.delete(NotesTable.NAME, NotesTable.Cols.ID + " = ?",
                new String[] { String.valueOf(data.getID()) });
        db.close();
    }
}
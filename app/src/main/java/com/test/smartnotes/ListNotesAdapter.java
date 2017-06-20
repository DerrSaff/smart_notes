package com.test.smartnotes;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by saff on 18.06.17.
 */

public class ListNotesAdapter extends CursorAdapter {
    public ListNotesAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.notes_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        LinearLayout noteBackground = (LinearLayout) view.findViewById(R.id.noteBackground);
        ImageView noteImage = (ImageView) view.findViewById(R.id.noteImage);
        TextView noteTitle = (TextView) view.findViewById(R.id.noteTitle);
        ImageButton editNoteButton = (ImageButton) view.findViewById(R.id.editNote);
        ImageButton removeNoteButton = (ImageButton) view.findViewById(R.id.removeNote);

        String note_title = cursor.getString(cursor.getColumnIndexOrThrow("note_title"));
        int priority = cursor.getInt(cursor.getColumnIndexOrThrow("importance"));

        noteTitle.setText(note_title);
//        tvPriority.setText(String.valueOf(priority));
    }
}

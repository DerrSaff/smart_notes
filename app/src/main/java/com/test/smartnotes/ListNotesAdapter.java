package com.test.smartnotes;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
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

        LinearLayout noteItem = (LinearLayout) view.findViewById(R.id.noteBackground);
        TextView noteTitle = (TextView) view.findViewById(R.id.noteTitle);

        int importance = cursor.getInt(cursor.getColumnIndexOrThrow("importance"));
        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String note_title = cursor.getString(cursor.getColumnIndexOrThrow("note_title"));

        if (importance == 1) {
            noteItem.setBackgroundColor(context.getResources().getColor(R.color.importanceHigh));
        }
        else if (importance == 2) {
            noteItem.setBackgroundColor(context.getResources().getColor(R.color.importanceAverage));
        }
        else if (importance == 3) {
            noteItem.setBackgroundColor(context.getResources().getColor(R.color.importanceLow));
        }
        else {
            noteItem.setBackgroundColor(context.getResources().getColor(R.color.importanceUndefined));
        }

        noteTitle.setText(note_title);
        noteTitle.setTag(id);
    }
}

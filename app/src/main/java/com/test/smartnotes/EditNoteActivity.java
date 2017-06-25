package com.test.smartnotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.test.smartnotes.database.DBAdapter;
import com.test.smartnotes.database.NoteData;

public class EditNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Intent intent = getIntent();
        long id = intent.getLongExtra("id", 1);

        NoteData note = DBAdapter.getNoteData(id);

        TextView noteTitle = (TextView) findViewById(R.id.noteTitle);
        noteTitle.setText(note.getNoteTitle());

        TextView noteText = (TextView) findViewById(R.id.noteText);
        noteText.setText(note.getNoteText());

        Spinner importanceSpinner = (Spinner) findViewById(R.id.importanceSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.importanceItems, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        importanceSpinner.setAdapter(adapter);

        Log.d("note importance", String.valueOf(note.getImportance()));
        importanceSpinner.setSelection(note.getImportance());
    }
}

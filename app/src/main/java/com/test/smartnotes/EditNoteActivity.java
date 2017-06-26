package com.test.smartnotes;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

    public void onSaveNote (View view) {
        String noteTitle;
        String noteText;
        int importance;
        String imagePath = "no_image"; // a stub for future development
        double longitude = -200.0; // a stub for future development
        double latitude = -200.0; // a stub for future development

        long id = getIntent().getLongExtra("id", 1);

        EditText noteTitleView = (EditText) findViewById(R.id.noteTitle);
        if ( String.valueOf(noteTitleView.getText()).equals("") ) {
            noteTitle = getString(R.string.no_title);
        }
        else {
            noteTitle = String.valueOf(noteTitleView.getText());
        }

        EditText noteTextView = (EditText) findViewById(R.id.noteText);
        noteText = String.valueOf(noteTextView.getText());

        Spinner spinnerView = (Spinner) findViewById(R.id.importanceSpinner);
        importance = spinnerView.getSelectedItemPosition();

        Log.d("noteTitle", noteTitle);
        Log.d("noteText", noteText);
        Log.d("Importance position", String.valueOf(importance));
        Log.d("imagePath", imagePath);
        Log.d("longitude", String.valueOf(longitude));
        Log.d("latitude", String.valueOf(latitude));

        int result = DBAdapter.updateNoteData( new NoteData(id, noteTitle, noteText, importance, imagePath, longitude, latitude));
        if (result == 0) {
            Snackbar.make(view, R.string.no_changes, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else if (result == 1) {
            Intent intent = new Intent(EditNoteActivity.this, ViewNoteActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("updated", true);
            startActivity(intent);
        }

    }
}

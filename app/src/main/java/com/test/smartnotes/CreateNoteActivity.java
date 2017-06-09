package com.test.smartnotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.test.smartnotes.database.DBAdapter;
import com.test.smartnotes.database.NoteData;

public class CreateNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        Spinner importanceSpinner = (Spinner) findViewById(R.id.importanceSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.importanceItems, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        importanceSpinner.setAdapter(adapter);
    }

    public void onSaveNote (View view) {
        String noteTitle;
        String noteText;
        int importance;
        String imagePath = "test"; // a stub for future development
        double longitude = 0.0; // a stub for future development
        double latitude = 0.0; // a stub for future development

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

        DBAdapter.addNoteData( new NoteData(noteTitle, noteText, importance, imagePath, longitude, latitude));

        startActivity(new Intent(this, ListNotesActivity.class));
    }
}

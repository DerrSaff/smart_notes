package com.test.smartnotes;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.test.smartnotes.database.DBAdapter;
import com.test.smartnotes.database.NoteData;

public class ViewNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_note_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        long id = intent.getLongExtra("id", 1);

        NoteData note = DBAdapter.getNoteData(id);

        TextView noteTitle = (TextView) findViewById(R.id.view_note_noteTitle);
        noteTitle.setText(note.getNoteTitle());

        TextView noteText = (TextView) findViewById(R.id.view_note_noteText);
        noteText.setText(note.getNoteText());

        TextView noteImportance = (TextView) findViewById(R.id.view_note_importance);
        int importance = note.getImportance();

        if (importance == 1) {
            noteImportance.setText(R.string.importance_high);
            noteImportance.setBackgroundColor(this.getResources().getColor(R.color.importanceHigh));
        }
        else if (importance == 2) {
            noteImportance.setText(R.string.importance_average);
            noteImportance.setBackgroundColor(this.getResources().getColor(R.color.importanceAverage));
        }
        else if (importance == 3) {
            noteImportance.setText(R.string.importance_low);
            noteImportance.setBackgroundColor(this.getResources().getColor(R.color.importanceLow));
        }
        else {
            noteImportance.setText(R.string.importance_undefined);
            noteImportance.setBackgroundColor(this.getResources().getColor(R.color.importanceUndefined));
        }

        double longitude = note.getLongitude();
        double latitude = note.getLatitude();
        Button noteMap = (Button) findViewById(R.id.view_note_map_button);

        if (longitude == -200 && latitude == -200) {
            noteMap.setVisibility(View.GONE);
        }

        String imagePath = note.getImagePath();
        ImageView noteImage = (ImageView) findViewById(R.id.view_note_noteImage);
        if (imagePath.equals("no_image")) {
            noteImage.setVisibility(View.GONE);
        }

        Log.d("received boolean", String.valueOf(getIntent().getBooleanExtra("updated", false)));

        if (getIntent().getBooleanExtra("updated", false)) {
            Snackbar.make(findViewById(android.R.id.content), R.string.note_updated, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ViewNoteActivity.this, ListNotesActivity.class));
    }
}

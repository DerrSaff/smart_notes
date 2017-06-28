package com.test.smartnotes;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.test.smartnotes.database.DBAdapter;
import com.test.smartnotes.database.NoteData;

public class EditNoteActivity extends AppCompatActivity {

    private Button mbuttonAddImage;
    private Button mbuttonRemoveImage;
    private ImageView mNoteImage;
    private String mImagePath = null;

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

        mbuttonAddImage = (Button) findViewById(R.id.addImageButton);
        mbuttonRemoveImage = (Button) findViewById(R.id.removeImageButton);
        mNoteImage = (ImageView) findViewById(R.id.noteImage);

        if (note.getImagePath() == null) {
            mNoteImage.setVisibility(View.GONE);
            mbuttonRemoveImage.setVisibility(View.GONE);
        }
        else {
            mImagePath = note.getImagePath();
            mNoteImage.setImageURI(Uri.parse(note.getImagePath()));
            mbuttonAddImage.setText(R.string.change_image);
            mbuttonRemoveImage.setVisibility(View.VISIBLE);
        }
    }

    public void onSaveNote (View view) {
        String noteTitle;
        String noteText;
        int importance;
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
        Log.d("imagePath", String.valueOf(mImagePath));
        Log.d("longitude", String.valueOf(longitude));
        Log.d("latitude", String.valueOf(latitude));

        int result = DBAdapter.updateNoteData( new NoteData(id, noteTitle, noteText, importance, mImagePath, longitude, latitude));
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

    public void onAddImage (View view) {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, 0);
    }

    public void onRemoveImage (View view) {
        mbuttonAddImage.setText(R.string.add_image);
        view.setVisibility(View.GONE);
        mNoteImage.setImageURI(null);
        mNoteImage.setVisibility(View.GONE);
        mImagePath = null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Uri selectedMediaUri = data.getData();
            mNoteImage.setImageURI(selectedMediaUri);
            mNoteImage.setVisibility(View.VISIBLE);

            mbuttonAddImage.setText(R.string.change_image);
            mbuttonRemoveImage.setVisibility(View.VISIBLE);

            mImagePath = selectedMediaUri.toString();
            Log.d("image Path", mImagePath);
        }
    }
}

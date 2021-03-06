package com.test.smartnotes;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.test.smartnotes.database.DBAdapter;
import com.test.smartnotes.database.NoteData;

public class NoteFormActivity extends AppCompatActivity {

    private Button mButtonAddImage;
    private Button mButtonRemoveImage;
    private ImageView mNoteImage;
    private String mImagePath = null;
    static final String IMAGE_PATH = "imagePath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_form);

        Spinner importanceSpinner = (Spinner) findViewById(R.id.importanceSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.importanceItems, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        importanceSpinner.setAdapter(adapter);

        mButtonAddImage = (Button) findViewById(R.id.addImageButton);
        mButtonRemoveImage = (Button) findViewById(R.id.removeImageButton);
        mNoteImage = (ImageView) findViewById(R.id.noteImage);

        Intent intent = getIntent();

        if (intent.getStringExtra("purpose").equals("create")) {
            if (savedInstanceState != null && savedInstanceState.getString(IMAGE_PATH) != null) {
                mImagePath = savedInstanceState.getString(IMAGE_PATH);
                loadImage(Uri.parse(mImagePath));
                addImageButtons();
            }
            else {
                onRemoveImage(mButtonRemoveImage);
            }
        }
        else if (intent.getStringExtra("purpose").equals("edit")) {
            long id = intent.getLongExtra("id", 1);
            NoteData note = DBAdapter.getNoteData(id);

            TextView noteTitle = (TextView) findViewById(R.id.noteTitle);
            noteTitle.setText(note.getNoteTitle());

            TextView noteText = (TextView) findViewById(R.id.noteText);
            noteText.setText(note.getNoteText());

            importanceSpinner.setSelection(note.getImportance());

            if (savedInstanceState != null || note.getImagePath() != null) {
                if (savedInstanceState != null && savedInstanceState.getString(IMAGE_PATH) != null){
                    loadImage(Uri.parse(savedInstanceState.getString(IMAGE_PATH)));
                }
                else {
                    loadImage(Uri.parse(note.getImagePath()));
                }
                addImageButtons();
            }
            else {
                onRemoveImage(mButtonRemoveImage);
            }
        }

        mNoteImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW); intent.setDataAndType(Uri.parse(mImagePath),"image/*");
                startActivity(intent);
            }
        });
    }

    public void onSaveNote (View view) {
        String noteTitle;
        String noteText;
        int importance;
        double longitude = -200.0; // a stub for future development
        double latitude = -200.0; // a stub for future development

        EditText noteTitleView = (EditText) findViewById(R.id.noteTitle);
        if ( String.valueOf(noteTitleView.getText()).equals("") ) {
            noteTitle = getString(R.string.no_title);
        }
        else {
            noteTitle = String.valueOf(noteTitleView.getText());
        }

        EditText noteTextView = (EditText) findViewById(R.id.noteText);
        noteText = String.valueOf(noteTextView.getText());

        if (String.valueOf(noteTextView.getText()).equals("")) {
            Toast.makeText(getApplicationContext(), R.string.note_no_text, Toast.LENGTH_SHORT).show();
            return;
        }

        Spinner spinnerView = (Spinner) findViewById(R.id.importanceSpinner);
        importance = spinnerView.getSelectedItemPosition();

        Intent intent = new Intent(NoteFormActivity.this, ViewNoteActivity.class);

        if (getIntent().getStringExtra("purpose").equals("create")) {
            DBAdapter.addNoteData( new NoteData(noteTitle, noteText, importance, mImagePath, longitude, latitude));
        }
        else if (getIntent().getStringExtra("purpose").equals("edit")) {
            long id = getIntent().getLongExtra("id", 1);
            int result = DBAdapter.updateNoteData( new NoteData(id, noteTitle, noteText, importance, mImagePath, longitude, latitude));

            if (result == 0) {
                Snackbar.make(view, R.string.no_changes, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            else if (result == 1) {
                intent.putExtra("id", id);
                intent.putExtra("updated", true);
                startActivity(intent);
            }
        }

        startActivity(new Intent(this, ListNotesActivity.class));
    }

    public void onAddImage (View view) {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, 0);
    }

    public void onRemoveImage (View view) {
        mNoteImage.setImageURI(null);
        mNoteImage.setVisibility(View.GONE);
        mImagePath = null;
        mButtonAddImage.setText(R.string.add_image);
        mButtonRemoveImage.setVisibility(View.GONE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            loadImage(data.getData());
            addImageButtons();
        }
    }

    private void loadImage(Uri uri) {
        mNoteImage.setImageURI(uri);
        mNoteImage.setVisibility(View.VISIBLE);
        mImagePath = uri.toString();
    }

    private void addImageButtons() {
        mButtonAddImage.setText(R.string.change_image);
        mButtonRemoveImage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(IMAGE_PATH, mImagePath);
        super.onSaveInstanceState(savedInstanceState);
    }
}

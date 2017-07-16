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

    private Button mButtonAddImage;
    private Button mButtonRemoveImage;
    private ImageView mNoteImage;
    private String mImagePath = null;
    static final String IMAGE_PATH = "imagePath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_form);

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

        importanceSpinner.setSelection(note.getImportance());

        mButtonAddImage = (Button) findViewById(R.id.addImageButton);
        mButtonRemoveImage = (Button) findViewById(R.id.removeImageButton);
        mNoteImage = (ImageView) findViewById(R.id.noteImage);

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

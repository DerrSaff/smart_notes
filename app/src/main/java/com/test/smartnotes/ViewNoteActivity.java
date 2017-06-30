package com.test.smartnotes;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.test.smartnotes.database.DBAdapter;
import com.test.smartnotes.database.NoteData;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;
import com.vk.sdk.dialogs.VKShareDialog;
import com.vk.sdk.dialogs.VKShareDialogBuilder;
import com.vk.sdk.util.VKUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class ViewNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.view_note_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        long id = intent.getLongExtra("id", 1);

        final NoteData note = DBAdapter.getNoteData(id);

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
        if (imagePath == null) {
            noteImage.setVisibility(View.GONE);
        }
        else {
            noteImage.setImageURI(Uri.parse(imagePath));
        }

        if (getIntent().getBooleanExtra("updated", false)) {
            Snackbar.make(findViewById(android.R.id.content), R.string.note_updated, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        noteImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW); intent.setDataAndType(Uri.parse(note.getImagePath()),"image/*");
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        long id = getIntent().getLongExtra("id", 1);
        Intent intent;

        switch (item.getItemId()) {
            case R.id.action_edit_note:
                intent = new Intent(ViewNoteActivity.this, EditNoteActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                return true;
            case R.id.action_remove_note:
                RemoveNoteDialog(String.valueOf(id));
                return true;
            case R.id.action_export_to_text:
                exportNoteToSD(this, getIntent().getLongExtra("id", 1));
                return true;
            case R.id.action_share_note:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.dialog_choose_social_network)
                        .setCancelable(true)
                        .setItems(R.array.socialNetworksItems, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        shareVK();
                                        break;
                                    case 1:
                                        // fb
                                        break;
                                    case 2:
                                        // tw
                                        break;
                                }
                            }
                        });
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void shareVK() {
        NoteData note = DBAdapter.getNoteData(getIntent().getLongExtra("id", 1));
        FragmentManager fragment_manager = getFragmentManager();

        VKSdk.login(ViewNoteActivity.this, VKScope.WALL, VKScope.PHOTOS);
        VKShareDialogBuilder builder = new VKShareDialogBuilder();
        builder.setText(note.getNoteTitle() + "\n" + note.getNoteText());
        if (note.getImagePath() != null) {
            try {
                Uri imagePath = Uri.parse(note.getImagePath());
                Bitmap noteImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                builder.setAttachmentImages(new VKUploadImage[]{
                        new VKUploadImage(noteImage, VKImageParameters.pngImage())
                });
            }
            catch (IOException ioEx) {
                ioEx.printStackTrace();
                Log.d("input output", "exception catched");
            }
        }
        builder.setShareDialogListener(new VKShareDialog.VKShareDialogListener() {
            @Override
            public void onVkShareComplete(int postId) {
                Toast.makeText(getApplicationContext(), R.string.sharing_complete, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onVkShareCancel() {
                Toast.makeText(getApplicationContext(), R.string.sharing_cancel, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onVkShareError(VKError error) {
                Toast.makeText(getApplicationContext(), R.string.sharing_error, Toast.LENGTH_SHORT).show();
            }
        });
        builder.show(fragment_manager, "VK_SHARE_DIALOG");
    }

    public void RemoveNoteDialog (final String id) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        DBAdapter.deleteNoteData(id);
                        dialog.dismiss();
                        Intent intent = new Intent(ViewNoteActivity.this, ListNotesActivity.class);
                        intent.putExtra("removed", true);
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.option_yes, dialogClickListener)
                .setNegativeButton(R.string.option_no, dialogClickListener).show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ViewNoteActivity.this, ListNotesActivity.class));
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public void exportNoteToSD(Context context, long id) {

        if (isExternalStorageWritable()) {
            try {
                File root = new File(Environment.getExternalStorageDirectory(), "smart_notes");
                if (!root.exists()) {
                    root.mkdirs();
                }

                NoteData note = DBAdapter.getNoteData(id);
                String sFileName = note.getNoteTitle() + ".txt";
                String sBody = note.getNoteText();

                File gpxfile = new File(root, sFileName);
                FileWriter writer = new FileWriter(gpxfile);
                writer.append(sBody);
                writer.flush();
                writer.close();

                Toast.makeText(context, R.string.export_success, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(context, R.string.export_fail, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Toast.makeText(getApplicationContext(), R.string.vk_auth_success, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(VKError error) {
                Toast.makeText(getApplicationContext(), R.string.vk_auth_error, Toast.LENGTH_SHORT).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}

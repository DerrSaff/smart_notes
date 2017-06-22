package com.test.smartnotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.test.smartnotes.database.DBAdapter;

public class ListNotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBAdapter.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton createNoteButton = (FloatingActionButton) findViewById(R.id.create_note_button);
        createNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(ListNotesActivity.this, CreateNoteActivity.class));
            }
        });

        ListView listView = (ListView)findViewById(R.id.notes_listView);
        ListNotesAdapter listNotesAdapter = new ListNotesAdapter(this, DBAdapter.getAllNoteData());
        listView.setAdapter(listNotesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            Log.d("Clicked item id", "itemClick: position = " + position + ", id = " + id);
            Intent intent = new Intent(ListNotesActivity.this, ViewNoteActivity.class);
            intent.putExtra("id", (int) id);
            startActivity(intent);
            }
        });

    }

    public void onRemoveClick (final View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        DBAdapter.deleteNoteData(String.valueOf(view.getTag()));
                        dialog.dismiss();
                        refreshListView();
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

    private void refreshListView () {
        // Most likely not the best way to solve the refresh task but it works
        ListView listView = (ListView)findViewById(R.id.notes_listView);
        ListNotesAdapter listNotesAdapter = new ListNotesAdapter(this, DBAdapter.getAllNoteData());
        listView.setAdapter(listNotesAdapter);
    }

}

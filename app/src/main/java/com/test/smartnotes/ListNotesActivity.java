package com.test.smartnotes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
        ListNotesAdapter listNotesAdapter = new ListNotesAdapter(this, DBAdapter.getAllUserData());
        listView.setAdapter(listNotesAdapter);
    }

}

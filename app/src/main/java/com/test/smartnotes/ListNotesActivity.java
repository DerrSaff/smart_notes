package com.test.smartnotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.test.smartnotes.database.DBAdapter;

public class ListNotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBAdapter.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.list_notes_toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView)findViewById(R.id.notes_listView);
        ListNotesAdapter listNotesAdapter = new ListNotesAdapter(this, DBAdapter.getAllNoteData());
        listView.setAdapter(listNotesAdapter);
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            Log.d("Clicked item id", "itemClick: position = " + position + ", id = " + id);
            Intent intent = new Intent(ListNotesActivity.this, ViewNoteActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
            }
        });

        if (getIntent().getBooleanExtra("removed", false)) {
            Snackbar.make(findViewById(android.R.id.content), R.string.note_removed, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_notes_context_menu, menu);
    }

    public void RemoveNoteDialog (final String id) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        DBAdapter.deleteNoteData(id);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_notes_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_note:
                Intent intent = new Intent(ListNotesActivity.this, NoteFormActivity.class);
                intent.putExtra("purpose", "create");
                startActivity(intent);
                return true;
//            case R.id.action_show_map:
                // stub for future development
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_edit_note:
                Intent intent = new Intent(ListNotesActivity.this, NoteFormActivity.class);
                intent.putExtra("purpose", "edit");
                intent.putExtra("id", info.id);
                startActivity(intent);
                return true;
            case R.id.action_remove_note:
                RemoveNoteDialog(String.valueOf(info.id));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}

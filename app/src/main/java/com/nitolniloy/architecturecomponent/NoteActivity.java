package com.nitolniloy.architecturecomponent;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

public class NoteActivity extends AppCompatActivity implements NoteListAdapter.OnDeleteClickListener {

    private NoteViewModel noteViewModel;
    private static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_NOTE_ACTIVITY_REQUEST_CODE = 2;

    private NoteListAdapter noteListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteActivity.this, AddNoteActivity.class);
                startActivityForResult(intent, NEW_NOTE_ACTIVITY_REQUEST_CODE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        noteListAdapter = new NoteListAdapter( this, this);
        recyclerView.setAdapter(noteListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                noteListAdapter.setNotes(notes);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            // Code to insert note
            final String note_id = UUID.randomUUID().toString();
            Note note = new Note(note_id, data.getStringExtra(AddNoteActivity.NOTE_ADDED), data.getStringExtra(AddNoteActivity.NOTE_DESC));
            noteViewModel.insert(note);

            Toast.makeText(
                    getApplicationContext(),
                    R.string.saved,
                    Toast.LENGTH_LONG).show();
        } else if (requestCode == UPDATE_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            // Code to update the note
            Note note = new Note(
                    data.getStringExtra(EditNoteActivity.NOTE_ID),
                    data.getStringExtra(EditNoteActivity.UPDATED_NOTE),
                    data.getStringExtra(EditNoteActivity.UPDATED_DESC));
            noteViewModel.updateNote(note);

            Toast.makeText(
                    getApplicationContext(),
                    R.string.updated,
                    Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void OnDeleteClickListener(Note myNote) {
        noteViewModel.deleteNote(myNote);
    }
}

package com.nitolniloy.architecturecomponent;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditNoteActivity extends AppCompatActivity {


    public static final String NOTE_ID = "note_id";
    static final String UPDATED_NOTE = "note_text";
    static final String UPDATED_DESC = "note_desc";

    private EditText etNote, etUpdateDesc;
    private Bundle bundle;
    private String noteId;
    private LiveData<Note> note;

    EditNoteViewModel noteModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        etNote = findViewById(R.id.updateNote);
        etUpdateDesc = findViewById(R.id.etUpdateDesc);

        Button btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedNote = etNote.getText().toString();
                String updateDesc = etUpdateDesc.getText().toString();
                Intent resultIntent = new Intent();
                resultIntent.putExtra(NOTE_ID, noteId);
                resultIntent.putExtra(UPDATED_NOTE, updatedNote);
                resultIntent.putExtra(UPDATED_DESC, updateDesc);
                setResult(RESULT_OK, resultIntent); // this will call note activity. and hit onActionResult
                finish();
            }
        });

        Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bundle = getIntent().getExtras();

        if (bundle != null) {
            noteId = bundle.getString("note_id");
        }

        noteModel = ViewModelProviders.of(this).get(EditNoteViewModel.class);
        note = noteModel.getNote(noteId);
        note.observe(this, new Observer<Note>() {
            @Override
            public void onChanged(@Nullable Note note) {
                etUpdateDesc.setText(note.getNoteDesc());
                etNote.setText(note.getNoteTitle());
            }
        });

    }
}

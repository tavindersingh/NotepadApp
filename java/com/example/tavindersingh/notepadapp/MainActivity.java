package com.example.tavindersingh.notepadapp;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private String title_string;
    private String content_string;
    private String date_string;
    private String id_string;
    private boolean isForUpdate = false;
    private EditText title;
    private EditText content;

    private String currentDateTimeString;

    private NoteDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (EditText)findViewById(R.id.title_edittext);
        content = (EditText)findViewById(R.id.content_edittext);

        db = new NoteDatabaseHandler(this);
        if(this.getIntent().getExtras().getString("UniqueId").equals("UPDATE")) {
            title_string = getIntent().getExtras().getString("title");
            content_string = getIntent().getExtras().getString("content");
            date_string = getIntent().getExtras().getString("date");
            id_string = getIntent().getExtras().getString("id");
        }

        if(title_string != null || content_string != null) {
            isForUpdate = true;
            title.setText(title_string);
            content.setText(content_string);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.done) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            }

            if(!isForUpdate) {
                db.addNote(new Note(title.getText().toString(), content.getText().toString(), currentDateTimeString));
                Toast.makeText(getApplicationContext(), "Note Added", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), NotesListActivity.class);
                startActivity(intent);
            }
            else {
                db.updateNote(new Note(Integer.parseInt(id_string), title.getText().toString(), content.getText().toString(), currentDateTimeString));
                Toast.makeText(getApplicationContext(), "Note Updated", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), NotesListActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

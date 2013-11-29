package com.myexample.singletable;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import com.myexample.singletable.contentprovider.ProjectProvider;
import com.myexample.singletable.cursoradapter.PersonCursorAdapter;
import com.myexample.singletable.loader.PersonLoader;
import com.myexample.singletable.model.Person;

import java.util.Random;
import java.util.UUID;

public class PersonActivity extends ListActivity {

    private SimpleCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        getActionBar().setTitle(R.string.person);

        String[] uiBindFrom = {  Person.COL_FIRST_NAME, Person.COL_LAST_NAME, Person.COL_DESCRIPTION};
        int[] uiBindTo = {R.id.first_name_text, R.id.last_name_text, R.id.description_text};

        /*Empty adapter that is used to display the loaded data*/
        mAdapter = new PersonCursorAdapter(this,R.layout.person_layout, null, uiBindFrom, uiBindTo,0);
        setListAdapter(mAdapter);
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(0, null, new PersonLoader(this, mAdapter));

        findViewById(R.id.bottom_group).setVisibility(View.GONE);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                addRow();
                break;
            case R.id.update:
                updateRow();
                break;
            case R.id.delete:
                deleteRow();
                break;
        }
    }

    private void deleteRow() {
        if ( mAdapter.getCount() > 0 ){
            Cursor cursor = mAdapter.getCursor();
            cursor.moveToPosition(mAdapter.getCount()-1);
            Person person = new Person(cursor);
            getContentResolver().delete(ContentUris.withAppendedId(ProjectProvider.URI_PERSONS, person.id),
                    null, null);
        }
    }

    private void updateRow() {
        if ( mAdapter.getCount() > 0 ){
            Cursor cursor = mAdapter.getCursor();
            cursor.moveToPosition(mAdapter.getCount()-1);
            Person person = new Person(cursor);
            person.firstName = "Kedar";
            person.lastName = "Sarmalkar";
            person.description = "Empty nothing to talk about";
            getContentResolver().update(ContentUris.withAppendedId(ProjectProvider.URI_PERSONS, person.id)
                    , person.getContent(), null, null);
        }
    }

    private void addRow() {
        String[] firstName = new String[] { "Ralph", "John", "Neil", "Jackie" };
        String[] lastName = new String[] { "Lauren", "Doe", "Armstrong", "Chan" };
        final Random random = new Random();

        Person person = new Person();
        person.firstName = firstName[random.nextInt(4)];
        person.lastName = lastName[random.nextInt(4)];
        person.description = firstName[random.nextInt(4)] +" "+ lastName[random.nextInt(4)] +" "+ firstName[random
                .nextInt(4)];
        person.departmentId = UUID.randomUUID().toString();

        getContentResolver().insert(ProjectProvider.URI_PERSONS, person.getContent());
    }
}

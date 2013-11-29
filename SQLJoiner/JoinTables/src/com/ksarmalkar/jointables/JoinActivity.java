package com.ksarmalkar.jointables;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import com.ksarmalkar.jointables.contentprovider.ProjectProvider;
import com.ksarmalkar.jointables.cursoradapter.JoinCursorAdapter;
import com.ksarmalkar.jointables.loader.JoinLoader;
import com.ksarmalkar.jointables.model.Department;
import com.ksarmalkar.jointables.model.Person;

import java.util.Random;
import java.util.UUID;

import static com.ksarmalkar.jointables.helper.DBHelper.getColumnAlias;

public class JoinActivity extends ListActivity {

    private SimpleCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        String[] uiBindFrom = { Person.COL_FIRST_NAME,  Person.COL_LAST_NAME,
                getColumnAlias(Department.TABLE_NAME, Department.COL_DEPARTMENT_NAME)};
        int[] uiBindTo = {R.id.first_name_text, R.id.last_name_text, R.id.description_text};

        /*Empty adapter that is used to display the loaded data*/
        mAdapter = new JoinCursorAdapter(this, R.layout.person_layout, null, uiBindFrom, uiBindTo,0);
        setListAdapter(mAdapter);
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(2, null, new JoinLoader(this, mAdapter));

//        findViewById(R.id.group).setVisibility(View.GONE);
        final Button backButton = (Button) findViewById(R.id.departments_button);
        backButton.setText(R.string.back);
        findViewById(R.id.join_button).setVisibility(View.GONE);
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
            case R.id.departments_button:
                finish();
                break;
        }
    }

    private void deleteRow() {
        if ( mAdapter.getCount() > 0 ){
            Cursor cursor = mAdapter.getCursor();
            cursor.moveToPosition(mAdapter.getCount()-1);
            Person person = new Person();
            person.id = cursor.getLong(cursor.getColumnIndex(Person.COL_ID));
            getContentResolver().delete(ContentUris.withAppendedId(ProjectProvider.URI_PERSONS, person.id),
                    null, null);
            getContentResolver().delete(ProjectProvider.URI_DEPARTMENTS, Department.COL_DEPARTMENT_ID+"=?",
                    new String[]{person.departmentId});
        }
    }

    private void updateRow() {
        if ( mAdapter.getCount() > 0 ){
            Cursor cursor = mAdapter.getCursor();
            cursor.moveToPosition(mAdapter.getCount()-1);
            Person person = new Person();
            person.id = cursor.getLong(cursor.getColumnIndex(Person.COL_ID));
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

        String[] companyName = new String[] { "Google", "Amplify", "Apple", "Yahoo!" };
        String[] departmentName = new String[] { "Engineering", "Finance", "Sales", "Marketing" };

        Department department = new Department();
        department.departmentId = person.departmentId;
        department.companyName = companyName[random.nextInt(4)];
        department.departmentName = departmentName[random.nextInt(4)];
        getContentResolver().insert(ProjectProvider.URI_PERSONS, person.getContent());
        getContentResolver().insert(ProjectProvider.URI_DEPARTMENTS, department.getContent());
    }
}
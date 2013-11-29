package com.myexample.jointables.cursoradapter;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import com.myexample.jointables.R;
import com.myexample.jointables.contentprovider.ProjectProvider;
import com.myexample.jointables.model.Department;
import com.myexample.jointables.model.Person;

public class PersonCursorAdapter extends SimpleCursorAdapter {


    private ContentResolver contentResolver;

    public PersonCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        contentResolver = context.getContentResolver();
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        super.bindView(view, context, cursor);
        final Person person = new Person(cursor);
        Button deleteButton = (Button) view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentResolver.delete(ContentUris.withAppendedId(ProjectProvider.URI_PERSONS,
                        person.id), null, null);
                contentResolver.delete(ProjectProvider.URI_DEPARTMENTS, Department.COL_DEPARTMENT_ID + "=?",
                        new String[]{person.departmentId});
            }
        });
    }
}

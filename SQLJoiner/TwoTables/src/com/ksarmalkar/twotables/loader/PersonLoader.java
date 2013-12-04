package com.ksarmalkar.twotables.loader;

import android.content.Context;
import android.content.CursorLoader;
import com.ksarmalkar.twotables.contentprovider.ProjectProvider;
import com.ksarmalkar.twotables.model.Person;

public class PersonLoader extends CursorLoader {

    public PersonLoader(Context context, String selection, String[] selectionArgs, String sortOrder) {
        super(context, ProjectProvider.URI_PERSONS, Person.FIELDS, selection, selectionArgs, sortOrder);
    }
}

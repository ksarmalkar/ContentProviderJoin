package com.ksarmalkar.singletable.loader;

import android.content.Context;
import android.content.CursorLoader;
import com.ksarmalkar.singletable.contentprovider.ProjectProvider;
import com.ksarmalkar.singletable.model.Person;

public class PersonLoader extends CursorLoader {

    public PersonLoader(Context context, String selection, String[] selectionArgs, String sortOrder) {
        super(context, ProjectProvider.URI_PERSONS, Person.FIELDS, selection, selectionArgs, sortOrder);
    }
}

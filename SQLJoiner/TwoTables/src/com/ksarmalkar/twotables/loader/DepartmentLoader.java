package com.ksarmalkar.twotables.loader;

import android.content.Context;
import android.content.CursorLoader;
import com.ksarmalkar.twotables.contentprovider.ProjectProvider;
import com.ksarmalkar.twotables.model.Department;

public class DepartmentLoader extends CursorLoader {

    public DepartmentLoader(Context context, String selection, String[] selectionArgs, String sortOrder) {
        super(context, ProjectProvider.URI_DEPARTMENTS, Department.FIELDS, selection, selectionArgs, sortOrder);
    }
}

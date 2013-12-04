package com.ksarmalkar.jointables.loader;

import android.content.Context;
import android.content.CursorLoader;
import com.ksarmalkar.jointables.contentprovider.ProjectProvider;
import com.ksarmalkar.jointables.model.Department;

public class DepartmentLoader extends CursorLoader {

    public DepartmentLoader(Context context, String selection, String[] selectionArgs, String sortOrder) {
        super(context, ProjectProvider.URI_DEPARTMENTS, Department.FIELDS, selection, selectionArgs, sortOrder);
    }
}

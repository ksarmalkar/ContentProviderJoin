package com.ksarmalkar.jointables.loader;

import android.content.Context;
import android.content.CursorLoader;
import com.ksarmalkar.jointables.contentprovider.ProjectProvider;
import com.ksarmalkar.jointables.model.Department;
import com.ksarmalkar.jointables.model.Person;

public class JoinLoader extends CursorLoader {

    public JoinLoader(Context context, String selection, String[] selectionArgs, String sortOrder) {
        super(context, ProjectProvider.URI_PERSONS_DEPARTMENTS, createdCombinedProjection(),
                selection, selectionArgs, sortOrder);
    }

    private static String[] createdCombinedProjection() {
        String personProjection[] = Person.getQualifiedColumns();
        String departmentProjection[] = Department.getQualifiedColumns();
        int personLength = personProjection.length;
        int departmentLength = departmentProjection.length;

        String projection[] = new String[personLength + departmentLength];
        for (int i = 0; i < personLength; i++) {
            projection[i] = personProjection[i];
        }

        for (int i = 0; i < departmentLength; i++) {
            projection[personLength + i] = departmentProjection[i];
        }

        return projection;
    }
}

package com.ksarmalkar.twotables.sqlitedb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.ksarmalkar.twotables.model.Department;
import com.ksarmalkar.twotables.model.Person;

public class ProjectDatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    private static ProjectDatabaseHandler singleton;

    public static ProjectDatabaseHandler getInstance(final Context context) {
        if (singleton == null) {
            singleton = new ProjectDatabaseHandler(context);
        }
        return singleton;
    }

    public ProjectDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(Person.CREATE_TABLE);
        database.execSQL(Department.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

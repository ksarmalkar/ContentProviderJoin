package com.ksarmalkar.twotables.model;

import android.content.ContentValues;
import android.database.Cursor;

public class Department {

    // SQL convention says Table name should be "singular", so not Persons
    public static final String TABLE_NAME = "Department";
    // Naming the id column with an underscore is good to be consistent
    // with other Android things. This is ALWAYS needed
    public static final String COL_ID = "_id";
    // These fields can be anything you want.
    public static final String COL_DEPARTMENT_ID = "department_id";
    public static final String COL_DEPARTMENT_NAME = "department_name";
    public static final String COL_COMPANY_NAME = "company_name";

    // For database projection so order is consistent
    public static final String[] FIELDS = { COL_ID, COL_DEPARTMENT_ID, COL_DEPARTMENT_NAME, COL_COMPANY_NAME};

    /*
     * The SQL code that creates a Table for storing Persons in.
     * Note that the last row does NOT end in a comma like the others.
     * This is a common source of error.
     */
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COL_ID + " INTEGER PRIMARY KEY,"
                    + COL_DEPARTMENT_ID + " INTEGER,"
                    + COL_DEPARTMENT_NAME + " TEXT NOT NULL DEFAULT '',"
                    + COL_COMPANY_NAME + " TEXT NOT NULL DEFAULT ''"
                    + ")";

    // Fields corresponding to database columns
    public long id = -1;
    public String departmentId = "";
    public String departmentName = "";
    public String companyName = "";

    /**
     * No need to do anything, fields are already set to default values above
     */
    public Department() {
    }

    /**
     * Convert information from the database into a Person object.
     */
    public Department(final Cursor cursor) {
        // Indices expected to match order in FIELDS!
        this.id = cursor.getLong(cursor.getColumnIndex(Department.COL_ID));
        this.departmentId = cursor.getString(cursor.getColumnIndex(Department.COL_DEPARTMENT_ID));
        this.departmentName = cursor.getString(cursor.getColumnIndex(Department.COL_DEPARTMENT_NAME));
        this.companyName = cursor.getString(cursor.getColumnIndex(Department.COL_COMPANY_NAME));
    }

    /**
     * Return the fields in a ContentValues object, suitable for insertion
     * into the database.
     */
    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        // Note that ID is NOT included here
        values.put(COL_DEPARTMENT_ID, departmentId);
        values.put(COL_DEPARTMENT_NAME, departmentName);
        values.put(COL_COMPANY_NAME, companyName);

        return values;
    }
}

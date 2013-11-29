package com.myexample.singletable.model;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * A class representation of a row in table "Person".
 */
public class Person {

    // SQL convention says Table name should be "singular", so not Persons
    public static final String TABLE_NAME = "Person";
    // Naming the id column with an underscore is good to be consistent
    // with other Android things. This is ALWAYS needed
    public static final String COL_ID = "_id";
    // These fields can be anything you want.
    public static final String COL_FIRST_NAME = "first_name";
    public static final String COL_LAST_NAME = "last_name";
    public static final String COL_DESCRIPTION = "bio";
    public static final String COL_DEPARTMENT_ID = "department_id";

    public static final String ALIAS_JOINER = "_";

    // For database projection so order is consistent
    public static final String[] FIELDS = { COL_ID, COL_FIRST_NAME, COL_LAST_NAME,
            COL_DESCRIPTION, COL_DEPARTMENT_ID};

    /*
     * The SQL code that creates a Table for storing Persons in.
     * Note that the last row does NOT end in a comma like the others.
     * This is a common source of error.
     */
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COL_ID + " INTEGER PRIMARY KEY,"
                    + COL_FIRST_NAME + " TEXT NOT NULL DEFAULT '',"
                    + COL_LAST_NAME + " TEXT NOT NULL DEFAULT '',"
                    + COL_DESCRIPTION + " TEXT NOT NULL DEFAULT '',"
                    + COL_DEPARTMENT_ID + " INTEGER NOT NULL"
                    + ")";

    // Fields corresponding to database columns
    public long id = -1;
    public String departmentId = "";
    public String firstName = "";
    public String lastName = "";
    public String description = "";

    /**
     * No need to do anything, fields are already set to default values above
     */
    public Person() {
    }

    public static String[] getQualifiedColumns() {
        String[] qualifiedColumns = new String[FIELDS.length];
        for (int i = 0; i < FIELDS.length; i++) {
            qualifiedColumns[i] = addPrefix(FIELDS[i]);
        }

        return qualifiedColumns;
    }

    /**
     * Convert information from the database into a Person object.
     */
    public Person(final Cursor cursor) {
        // Indices expected to match order in FIELDS!
        this.id = cursor.getLong(cursor.getColumnIndex(Person.COL_ID));
        this.departmentId = cursor.getString(cursor.getColumnIndex(Person.COL_DEPARTMENT_ID));
        this.firstName = cursor.getString(cursor.getColumnIndex(Person.COL_FIRST_NAME));
        this.lastName = cursor.getString(cursor.getColumnIndex(Person.COL_LAST_NAME));
        this.description = cursor.getString(cursor.getColumnIndex(Person.COL_DESCRIPTION));
    }

    /**
     * Return the fields in a ContentValues object, suitable for insertion
     * into the database.
     */
    public ContentValues getContent() {
        final ContentValues values = new ContentValues();
        // Note that ID is NOT included here
        values.put(COL_FIRST_NAME, firstName);
        values.put(COL_LAST_NAME, lastName);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_DEPARTMENT_ID, departmentId);

        return values;
    }

    public static String addPrefix(String column) {
        return TABLE_NAME + "." + column;
    }

    public static String getColumnAlias(String column){
        return TABLE_NAME + ALIAS_JOINER + column;
    }
}

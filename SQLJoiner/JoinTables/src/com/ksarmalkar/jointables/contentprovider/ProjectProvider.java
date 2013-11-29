package com.ksarmalkar.jointables.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import com.ksarmalkar.jointables.model.Department;
import com.ksarmalkar.jointables.model.Person;
import com.ksarmalkar.jointables.sqlitedb.ProjectDatabaseHandler;

import java.util.HashMap;

import static com.ksarmalkar.jointables.helper.DBHelper.ALIAS_JOINER;
import static com.ksarmalkar.jointables.helper.DBHelper.addPrefix;

public class ProjectProvider extends ContentProvider {

    // All URIs share these parts
    public static final String AUTHORITY = "com.ksarmalkar.jointables.contentprovider";
    public static final String SCHEME = "content://";

    // URIs
    // Used for all persons
    public static final String PERSONS = SCHEME + AUTHORITY + "/person";
    public static final Uri URI_PERSONS = Uri.parse(PERSONS);
    // Used for a single person, just add the id to the end
    public static final String PERSON_BASE = PERSONS + "/";

    // URIs
    // Used for all department
    public static final String DEPARTMENTS = SCHEME + AUTHORITY + "/department";
    public static final Uri URI_DEPARTMENTS = Uri.parse(DEPARTMENTS);
    // Used for a single department, just add the id to the end
    public static final String DEPARTMENTS_BASE = DEPARTMENTS + "/";
    private SQLiteDatabase readableDatabase;

    //URIs
    // Used for join
    public static final String PERSONS_DEPARTMENTS = SCHEME + AUTHORITY + "/personsdepartments";
    public static final Uri URI_PERSONS_DEPARTMENTS = Uri.parse(PERSONS_DEPARTMENTS);

    @Override
    public boolean onCreate() {
        this.readableDatabase = ProjectDatabaseHandler
                .getInstance(getContext())
                .getReadableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor result;
        if (URI_PERSONS.equals(uri)) {
            result = readableDatabase
                    .query(Person.TABLE_NAME, Person.FIELDS, null, null, null,
                            null, null, null);

            result.setNotificationUri(getContext().getContentResolver(), URI_PERSONS);
        }else if (uri.toString().startsWith(PERSON_BASE)) {
            final long id = Long.parseLong(uri.getLastPathSegment());
            result = readableDatabase
                    .query(Person.TABLE_NAME, Person.FIELDS,
                            Person.COL_ID + " IS ?",
                            new String[]{String.valueOf(id)}, null, null,
                            null, null);

            result.setNotificationUri(getContext().getContentResolver(), URI_PERSONS);
        }else if (URI_DEPARTMENTS.equals(uri)) {
            result = readableDatabase
                    .query(Department.TABLE_NAME, Department.FIELDS, null, null, null,
                            null, null, null);

            result.setNotificationUri(getContext().getContentResolver(), URI_DEPARTMENTS);
        }else if (uri.toString().startsWith(DEPARTMENTS_BASE)) {
            final long id = Long.parseLong(uri.getLastPathSegment());
            result = readableDatabase
                    .query(Department.TABLE_NAME, Department.FIELDS,
                            Department.COL_ID + " IS ?",
                            new String[] { String.valueOf(id) }, null, null,
                            null, null);

            result.setNotificationUri(getContext().getContentResolver(), URI_DEPARTMENTS);
        }else if ( URI_PERSONS_DEPARTMENTS.equals(uri) ){

            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            String table;

            StringBuilder sb = new StringBuilder();
            sb.append(Person.TABLE_NAME);
            sb.append(" LEFT OUTER JOIN ");
            sb.append(Department.TABLE_NAME);
            sb.append(" ON (");
            sb.append(addPrefix(Person.TABLE_NAME, Person.COL_DEPARTMENT_ID));
            sb.append(" = ");
            sb.append(addPrefix(Department.TABLE_NAME, Department.COL_DEPARTMENT_ID));
            sb.append(")");
            table = sb.toString();

            queryBuilder.setTables(table);
            queryBuilder.setProjectionMap(buildColumnMap());

            result = queryBuilder.query(readableDatabase, projection, selection, selectionArgs, null, null,
                    sortOrder);
            result.setNotificationUri(getContext().getContentResolver(), URI_PERSONS_DEPARTMENTS);
        }
        else {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        getContext().getContentResolver().notifyChange(Uri.parse(AUTHORITY), null, true);

        return result;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (URI_PERSONS.equals(uri)) {
            long id = readableDatabase.insertOrThrow(Person.TABLE_NAME, null, values);
            return getUriForId(id, uri);
        }else if (URI_DEPARTMENTS.equals(uri)) {
            long id = ProjectDatabaseHandler
                    .getInstance(getContext())
                    .getReadableDatabase().insertOrThrow(Department.TABLE_NAME, null, values);
            return getUriForId(id, uri);
        }

        return null;
    }

    private Uri getUriForId(long id, Uri uri) {
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
            notifyUris(itemUri, null);
            return itemUri;
        }
        // s.th. went wrong:
        throw new SQLException(
                "Problem while operating on uri: " + uri);
    }

    private void notifyUris(Uri itemUri, ContentObserver contentObserver) {
        getContext().getContentResolver().notifyChange(itemUri, contentObserver);
        getContext().getContentResolver().notifyChange(URI_PERSONS_DEPARTMENTS, contentObserver);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int id = -1;
        if (URI_PERSONS.equals(uri)) {
            id = readableDatabase.delete(Person.TABLE_NAME, selection, selectionArgs);
            notifyUris(URI_PERSONS, null);
        }else if (uri.toString().startsWith(PERSON_BASE)) {
            final long personId = Long.parseLong(uri.getLastPathSegment());
            id = readableDatabase.delete(Person.TABLE_NAME,
                    Person.COL_ID + " IS ?",
                    new String[]{String.valueOf(personId)});
            notifyUris(URI_PERSONS, null);
        }else if (URI_DEPARTMENTS.equals(uri)) {
            id = readableDatabase.delete(Department.TABLE_NAME, selection, selectionArgs);
            notifyUris(URI_DEPARTMENTS, null);
        }else if (uri.toString().startsWith(DEPARTMENTS_BASE)) {
            final long departmentId = Long.parseLong(uri.getLastPathSegment());
            id = readableDatabase.delete(Department.TABLE_NAME,
                    Department.COL_ID + " IS ?",
                    new String[]{String.valueOf(departmentId)});
            notifyUris(URI_DEPARTMENTS, null);
        }
        return id;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int id = -1;
        if (URI_PERSONS.equals(uri)) {
            id = readableDatabase.update(Person.TABLE_NAME, values, selection, selectionArgs);
            notifyUris(URI_PERSONS, null);
        }else if (uri.toString().startsWith(PERSON_BASE)) {
            final long personId = Long.parseLong(uri.getLastPathSegment());
            id = readableDatabase.update(Person.TABLE_NAME, values,
                    Person.COL_ID + " IS ?",
                    new String[]{String.valueOf(personId)});
            notifyUris(URI_PERSONS, null);
        }else if (URI_DEPARTMENTS.equals(uri)) {
            id = readableDatabase.update(Department.TABLE_NAME, values, selection, selectionArgs);
            notifyUris(URI_DEPARTMENTS, null);
        }else if (uri.toString().startsWith(DEPARTMENTS_BASE)) {
            final long departmentId = Long.parseLong(uri.getLastPathSegment());
            id = readableDatabase.update(Department.TABLE_NAME, values,
                    Department.COL_ID + " IS ?",
                    new String[]{String.valueOf(departmentId)});
            notifyUris(URI_DEPARTMENTS, null);
        }
        return id;
    }


    /**
     * Because the tables we're joining have columns of the same name, we have to map column names to aliases.
     * The person table is the primary table here, so the alias is just the column name. For the department table,
     * the alias is calculated by adding the table name plus "_", then the column name.
     *
     * @return
     */
    private static HashMap<String, String> buildColumnMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        String personProjection[] = Person.FIELDS;
        for (String col : personProjection) {

            String qualifiedCol = addPrefix(Person.TABLE_NAME, col);
            map.put(qualifiedCol, qualifiedCol + " as " + col);
        }

        String departmentProjection[] = Department.FIELDS;
        for (String col : departmentProjection) {

            String qualifiedCol = addPrefix(Department.TABLE_NAME, col);
            String alias = qualifiedCol.replace(".", ALIAS_JOINER);
            map.put(qualifiedCol, qualifiedCol + " AS " + alias);
        }

        return map;
    }

}

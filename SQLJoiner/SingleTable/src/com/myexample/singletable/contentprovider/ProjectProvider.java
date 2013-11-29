package com.myexample.singletable.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.myexample.singletable.model.Person;
import com.myexample.singletable.sqlitedb.ProjectDatabaseHandler;


public class ProjectProvider extends ContentProvider {

    // All URIs share these parts
    public static final String AUTHORITY = "com.myexample.singletable.contentprovider";
    public static final String SCHEME = "content://";

    // URIs
    // Used for all persons
    public static final String PERSONS = SCHEME + AUTHORITY + "/person";
    public static final Uri URI_PERSONS = Uri.parse(PERSONS);
    // Used for a single person, just add the id to the end
    public static final String PERSON_BASE = PERSONS + "/";

    private SQLiteDatabase readableDatabase;

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
        }
        else {
            throw new UnsupportedOperationException("Not yet implemented");
        }

//        result.setNotificationUri(getContext().getContentResolver(), URI_PERSONS_DEPARTMENTS);

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
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int id = -1;
        if (URI_PERSONS.equals(uri)) {
            id = readableDatabase.delete(Person.TABLE_NAME, selection, selectionArgs);
//            getContext().getContentResolver().notifyChange(URI_PERSONS, null);
            notifyUris(URI_PERSONS, null);
        }else if (uri.toString().startsWith(PERSON_BASE)) {
            final long personId = Long.parseLong(uri.getLastPathSegment());
            id = readableDatabase.delete(Person.TABLE_NAME,
                    Person.COL_ID + " IS ?",
                    new String[]{String.valueOf(personId)});
//            getContext().getContentResolver().notifyChange(URI_PERSONS, null);
            notifyUris(URI_PERSONS, null);
        }
        return id;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int id = -1;
        if (URI_PERSONS.equals(uri)) {
            id = readableDatabase.update(Person.TABLE_NAME, values, selection, selectionArgs);
//            getContext().getContentResolver().notifyChange(URI_PERSONS, null);
            notifyUris(URI_PERSONS, null);
        }else if (uri.toString().startsWith(PERSON_BASE)) {
            final long personId = Long.parseLong(uri.getLastPathSegment());
            id = readableDatabase.update(Person.TABLE_NAME, values,
                    Person.COL_ID + " IS ?",
                    new String[]{String.valueOf(personId)});
//            getContext().getContentResolver().notifyChange(URI_PERSONS, null);
            notifyUris(URI_PERSONS, null);
        }
        return id;
    }

}

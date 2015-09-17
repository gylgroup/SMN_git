package com.example.smn.smn.db;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Simple database access helper class.
 * 
 * @author Dan Breslau
 */
public class DbAdapter {

    private static final String DATABASE_NAME = "smn.sqlite3";
    private static final String TABLE_NAME = "ciudades";
    private static final int DATABASE_VERSION = 1;

    private SQLiteHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Activity mActivity;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param activity
     *            the Activity that is using the database
     */
    public DbAdapter(Context context, Activity activity) {
        this.mActivity = activity;
        mDbHelper = new SQLiteHelper(context, DATABASE_NAME);
        mDb = mDbHelper.openDataBase();
    }

    /**
     * Closes the database.
     */
    public void close() {
        mDbHelper.close();
    }

    /**
     * Return a Cursor that returns all states (and their state capitals) where
     * the state name begins with the given constraint string.
     * 
     * @param constraint
     *            Specifies the first letters of the states to be listed. If
     *            null, all rows are returned.
     * @return Cursor managed and positioned to the first state, if found
     * @throws SQLException
     *             if query fails
     */
    public Cursor getCiudades(String constraint) throws SQLException {

        String queryString = "SELECT _id, tipo, nombre, provincia FROM " + TABLE_NAME;

        if (constraint != null) {
            // Query for any rows where the state name begins with the
            // string specified in constraint.
            //
            // NOTE:
            // If wildcards are to be used in a rawQuery, they must appear
            // in the query parameters, and not in the query string proper.
            // See http://code.google.com/p/android/issues/detail?id=3153

            constraint = "%" + constraint.trim() + "%";
            queryString += " WHERE nombre LIKE ? OR provincia LIKE ?";
        }
        String params[] = { constraint, constraint };

        if (constraint == null) {
            // If no parameters are used in the query,
            // the params arg must be null.
            params = null;
        }
        try {
            Cursor cursor = mDb.rawQuery(queryString, params);
            if (cursor != null) {
                this.mActivity.startManagingCursor(cursor);
                cursor.moveToFirst();
                return cursor;
            }
        }
        catch (SQLException e) {
            Log.e("AutoCompleteDbAdapter", e.toString());
            throw e;
        }

        return null;
    }


    public String getNombreCiudadPorId(String id){

        String queryString = "SELECT nombre FROM " + TABLE_NAME;
        if (id != null) {
            id = id.trim();
            queryString += " WHERE _id = ?";
        }
        queryString += " LIMIT 1";
        String params[] = { id };

        if (id == null) {
            // If no parameters are used in the query,
            // the params arg must be null.
            params = null;
        }
        try {
            Cursor cursor = mDb.rawQuery(queryString, params);
            if (cursor != null) {
                this.mActivity.startManagingCursor(cursor);
                cursor.moveToFirst();
                String nombre = cursor.getString(0).substring(0,1).toUpperCase() +
                        cursor.getString(0).substring(1).toLowerCase();
                return nombre;
            }
        }
        catch (SQLException e) {
            Log.e("AutoCompleteDbAdapter", e.toString());
            throw e;
        }

        return id;

    }


}

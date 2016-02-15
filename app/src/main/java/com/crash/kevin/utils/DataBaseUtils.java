package com.crash.kevin.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by hades on 2016/2/15.
 */
public class DataBaseUtils {
    private static final String TAG = DataBaseUtils.class.getSimpleName();

    /**
     * Database Version
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Database Name
     */
    private static final String DATABASE_NAME = "student_database";

    /**
     * Table Name
     */
    private static final String DATABASE_TABLE = "tb_student";

    /**
     * Table columns
     */
    public static final String KEY_NAME = "name";
    public static final String KEY_GRADE = "grade";
    public static final String KEY_ROWID = "_id";

    /**
     * Database creation sql statement
     */
    private static final String CREATE_STUDENT_TABLE = "create table"+
            DATABASE_TABLE +"("+ KEY_ROWID +"integer primary key autoincrement,"+
            KEY_NAME +"text not null,"+ KEY_GRADE +"text not null);";

    /**
     * Context
     */
    private final Context mCtx;

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Inner private class. Database Helper class for creating and updating db
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * onCreate method is called for the 1st time when database doesn't exists
         * @param db
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "Creating DataBase:"+ CREATE_STUDENT_TABLE);
            db.execSQL(CREATE_STUDENT_TABLE);
        }

        /**
         * onUpgrade method is called when database version change
         * @param db
         * @param oldVersion
         * @param newVersion
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version"+ oldVersion +"to"+ newVersion);
        }

    }

    /**
     * Constructor - takes the context to allow the database to
     * be opened/created
     */
    public DataBaseUtils(Context ctx) {
        this.mCtx = ctx;
    }

    public DataBaseUtils open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public long createStudent(String name, String grade) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_GRADE, grade);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteStudent(long rowId) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID +"="+rowId, null) > 0;
    }

    public Cursor fetchAllStudents(long id) {
        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[] {
                KEY_ROWID, KEY_NAME, KEY_GRADE}, KEY_ROWID +"="+ id, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public boolean updateStudent(int id, String name, String standard) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_GRADE, standard);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID +"="+ id, null) > 0;
    }
}

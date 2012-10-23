package com.github.wanzheng.dbassistant;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class Provider extends ContentProvider {
    private DBOpenHelper openHelper;
    public static final String DB_NAME = "test.db";

    @Override
    public boolean onCreate() {
        openHelper = new DBOpenHelper(getContext(), DB_NAME, null, 1);
        return true;
    }

    public static Uri getUri() {
        return Uri.parse("content://com.github.wanzheng.dbassistant/tb1/");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Log.d(DBBackup.TAG, "insert: " + contentValues);

        SQLiteDatabase db = openHelper.getWritableDatabase();
        long id = db.insert("tb1", null, contentValues);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

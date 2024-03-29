package com.github.wanzheng.dbassistant;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.*;
import java.text.ParseException;

public class DBBackup extends Activity
{
    public static final String TAG = "DBBackup";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ContentResolver resolver = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("_id", 1);
        values.put("value", 100);
        values.put("name", "bob");
        resolver.insert(Provider.getUri(), values);

        File dbFile = getDatabasePath(Provider.DB_NAME);
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
        File dumpFile = new File(Environment.getExternalStorageDirectory() + "/db.dump");

        Writer writer = null;
        try {
            Log.d(TAG, "start exporting ...");
            writer = new BufferedWriter(new FileWriter(dumpFile));
            Exporter.dump(db, writer);
            Log.d(TAG, "exporting done.");
        } catch (Exception e) {
            Log.e(TAG, "Failed to dump db: " + e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    // null
                }
            }
        }
        db.close();

        db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + "/new.db", null);
        try {
            Log.d(TAG, "start importing ...");
            Importer.importDB(db, new FileReader(dumpFile));
            Log.d(TAG, "importing done.");
        } catch (Exception e) {
            Log.e(TAG, "Failed to import db: " + e);
        }
        db.close();
    }
}

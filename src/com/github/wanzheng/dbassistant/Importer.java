package com.github.wanzheng.dbassistant;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Importer {
    private static final String TAG = "DBBackupAssistant";

    public static void importDB(SQLiteDatabase db, Reader reader) throws IOException, ParseException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;

        line = bufferedReader.readLine();
        if (line == null) {
            return;
        }
        int version = getVersion(line);
        Log.d(TAG, "version = " + version);
        db.setVersion(version);

        while ((line = bufferedReader.readLine()) != null) {
            if (line.startsWith("#")) {
                continue;
            }
            Log.d(TAG, "executing: " + line);
            db.execSQL(line);
        }
    }

    private static int getVersion(String line) throws ParseException {
        Pattern pattern = Pattern.compile("#\\s*version:\\s*(\\d+)");
        Matcher matcher = pattern.matcher(line);
        if (! matcher.matches()) {
            throw new ParseException("Failed to parse verion from line: " + line, 0);
        }

        try {
            return Integer.valueOf(matcher.group(1));
        } catch (Exception e) {
            throw new ParseException("Failed to parse verion from line: " + line, 0);
        }
    }
}

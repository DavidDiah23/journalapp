package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "journal.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_ENTRIES = "journal_entries";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_MOOD = "mood";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_CREATED_AT = "created_at";

    private static final String CREATE_TABLE_ENTRIES =
            "CREATE TABLE " + TABLE_ENTRIES + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TITLE + " TEXT NOT NULL," +
                    COLUMN_CONTENT + " TEXT NOT NULL," +
                    COLUMN_DATE + " TEXT NOT NULL," +
                    COLUMN_MOOD + " TEXT," +
                    COLUMN_CATEGORY + " TEXT," +
                    COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
        onCreate(db);
    }

    public long addJournalEntry(JournalEntry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, entry.getTitle());
        values.put(COLUMN_CONTENT, entry.getContent());
        values.put(COLUMN_DATE, entry.getDate());
        values.put(COLUMN_MOOD, entry.getMood());
        values.put(COLUMN_CATEGORY, entry.getCategory());

        long id = db.insert(TABLE_ENTRIES, null, values);
        entry.setId((int) id);
        db.close();
        return id;
    }

    public List<JournalEntry> getAllEntries() {
        List<JournalEntry> entryList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ENTRIES + " ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                try {
                    JournalEntry entry = new JournalEntry();
                    entry.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    entry.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                    entry.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)));
                    entry.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                    entry.setMood(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOOD)));
                    entry.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                    entry.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)));
                    entryList.add(entry);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return entryList;
    }

    public JournalEntry getEntry(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ENTRIES, null, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);

        JournalEntry entry = null;

        if (cursor != null && cursor.moveToFirst()) {
            try {
                entry = new JournalEntry();
                entry.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                entry.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                entry.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)));
                entry.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                entry.setMood(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOOD)));
                entry.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                entry.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }

        db.close();
        return entry;
    }

    public int updateEntry(JournalEntry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, entry.getTitle());
        values.put(COLUMN_CONTENT, entry.getContent());
        values.put(COLUMN_DATE, entry.getDate());
        values.put(COLUMN_MOOD, entry.getMood());
        values.put(COLUMN_CATEGORY, entry.getCategory());

        int result = db.update(TABLE_ENTRIES, values, COLUMN_ID + "=?",
                new String[]{String.valueOf(entry.getId())});
        db.close();
        return result;
    }

    public void deleteEntry(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ENTRIES, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<JournalEntry> searchEntries(String query) {
        List<JournalEntry> entryList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ENTRIES +
                " WHERE " + COLUMN_TITLE + " LIKE ? OR " + COLUMN_CONTENT + " LIKE ?" +
                " ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + query + "%", "%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                try {
                    JournalEntry entry = new JournalEntry();
                    entry.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    entry.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                    entry.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)));
                    entry.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                    entry.setMood(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOOD)));
                    entry.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                    entry.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)));
                    entryList.add(entry);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return entryList;
    }

    public List<JournalEntry> getEntriesByMood(String mood) {
        List<JournalEntry> entryList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ENTRIES +
                " WHERE " + COLUMN_MOOD + "=?" +
                " ORDER BY " + COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{mood});

        if (cursor.moveToFirst()) {
            do {
                try {
                    JournalEntry entry = new JournalEntry();
                    entry.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    entry.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                    entry.setContent(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)));
                    entry.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                    entry.setMood(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOOD)));
                    entry.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                    entry.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)));
                    entryList.add(entry);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return entryList;
    }
}

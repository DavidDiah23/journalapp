package com.example.journalapp;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {JournalEntry.class}, version = 1)
public abstract class JournalDatabase extends RoomDatabase {
    public abstract JournalDao journalDao();
    private static volatile JournalDatabase INSTANCE;

    public static JournalDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (JournalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    JournalDatabase.class, "journal_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
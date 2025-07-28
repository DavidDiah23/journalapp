package com.example.journalapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface JournalDao {
    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    LiveData<List<JournalEntry>> getAllEntries();

    @Insert
    void insertEntry(JournalEntry entry);

    @Update
    void updateEntry(JournalEntry entry);

    @Delete
    void deleteEntry(JournalEntry entry);

    @Query("SELECT * FROM journal_entries WHERE mood = :mood")
    LiveData<List<JournalEntry>> getEntriesByMood(String mood);

    @Query("SELECT * FROM journal_entries WHERE category = :category")
    LiveData<List<JournalEntry>> getEntriesByCategory(String category);
}
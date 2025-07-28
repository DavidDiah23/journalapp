package com.example.journalapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "journal_entries")
public class JournalEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String content;
    private String mood;
    private String category;
    private long date;

    public JournalEntry(String title, String content, String mood, String category, long date) {
        this.title = title;
        this.content = content;
        this.mood = mood;
        this.category = category;
        this.date = date;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
}
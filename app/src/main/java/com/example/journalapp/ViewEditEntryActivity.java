package com.example.journalapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewEditEntryActivity extends AppCompatActivity {
    
    public static final String EXTRA_ENTRY_ID = "entry_id";
    public static final String EXTRA_ENTRY_TITLE = "entry_title";
    public static final String EXTRA_ENTRY_CONTENT = "entry_content";
    public static final String EXTRA_ENTRY_MOOD = "entry_mood";
    public static final String EXTRA_ENTRY_CATEGORY = "entry_category";
    public static final String EXTRA_ENTRY_DATE = "entry_date";
    public static final String EXTRA_IS_EDITING = "is_editing";
    
    private TextView tvTitle, tvContent, tvDate, tvMood, tvCategory;
    private EditText etTitle, etContent;
    private boolean isEditing = false;
    private JournalEntry currentEntry;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);
        
        // Initialize views
        initViews();
        setupToolbar();
        
        // Get entry data from intent
        Intent intent = getIntent();
        if (intent != null) {
            int entryId = intent.getIntExtra(EXTRA_ENTRY_ID, -1);
            String title = intent.getStringExtra(EXTRA_ENTRY_TITLE);
            String content = intent.getStringExtra(EXTRA_ENTRY_CONTENT);
            String mood = intent.getStringExtra(EXTRA_ENTRY_MOOD);
            String category = intent.getStringExtra(EXTRA_ENTRY_CATEGORY);
            long date = intent.getLongExtra(EXTRA_ENTRY_DATE, System.currentTimeMillis());
            isEditing = intent.getBooleanExtra(EXTRA_IS_EDITING, false);
            
            if (entryId == -1) {
                // New entry
                currentEntry = new JournalEntry("", "", "", "", System.currentTimeMillis());
                isEditing = true;
            } else {
                // Existing entry
                currentEntry = new JournalEntry(title != null ? title : "", 
                                              content != null ? content : "", 
                                              mood != null ? mood : "", 
                                              category != null ? category : "", 
                                              date);
                currentEntry.setId(entryId);
            }
            
            displayEntry(currentEntry);
            if (isEditing) {
                enableEditMode();
            }
        }
    }
    
    private void initViews() {
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        tvDate = findViewById(R.id.tv_date);
        tvMood = findViewById(R.id.tv_mood);
        tvCategory = findViewById(R.id.tv_category);
        
        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
    }
    
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Journal Entry");
        }
    }
    
    private void displayEntry(JournalEntry entry) {
        tvTitle.setText(entry.getTitle());
        tvContent.setText(entry.getContent());
        etTitle.setText(entry.getTitle());
        etContent.setText(entry.getContent());
        
        // Format date
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
        String dateStr = sdf.format(new Date(entry.getDate()));
        tvDate.setText(dateStr);
        
        // Set mood if available
        if (entry.getMood() != null && !entry.getMood().isEmpty()) {
            tvMood.setText(entry.getMood());
            tvMood.setVisibility(View.VISIBLE);
        } else {
            tvMood.setVisibility(View.GONE);
        }
        
        // Set category if available
        if (entry.getCategory() != null && !entry.getCategory().isEmpty()) {
            tvCategory.setText(entry.getCategory());
            tvCategory.setVisibility(View.VISIBLE);
        } else {
            tvCategory.setVisibility(View.GONE);
        }
    }
    
    private void enableEditMode() {
        // Hide view mode, show edit mode
        tvTitle.setVisibility(View.GONE);
        tvContent.setVisibility(View.GONE);
        etTitle.setVisibility(View.VISIBLE);
        etContent.setVisibility(View.VISIBLE);
        
        // Focus on title for editing
        etTitle.requestFocus();
        
        isEditing = true;
        invalidateOptionsMenu();
    }
    
    private void saveEntry() {
        if (currentEntry != null) {
            currentEntry.setTitle(etTitle.getText().toString());
            currentEntry.setContent(etContent.getText().toString());
            
            // Update view mode text
            tvTitle.setText(currentEntry.getTitle());
            tvContent.setText(currentEntry.getContent());
            
            // Switch back to view mode
            tvTitle.setVisibility(View.VISIBLE);
            tvContent.setVisibility(View.VISIBLE);
            etTitle.setVisibility(View.GONE);
            etContent.setVisibility(View.GONE);
            
            isEditing = false;
            invalidateOptionsMenu();
            
            // Save to database
            JournalDatabase database = JournalDatabase.getInstance(this);
            JournalDao dao = database.journalDao();
            
            new Thread(() -> {
                if (currentEntry.getId() == 0) {
                    // New entry
                    dao.insertEntry(currentEntry);
                } else {
                    // Update existing entry
                    dao.updateEntry(currentEntry);
                }
                runOnUiThread(() -> {
                    Toast.makeText(this, "Entry saved successfully!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                });
            }).start();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_entry, menu);
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem editItem = menu.findItem(R.id.action_edit);
        MenuItem saveItem = menu.findItem(R.id.action_save);
        
        if (isEditing) {
            if (editItem != null) editItem.setVisible(false);
            if (saveItem != null) saveItem.setVisible(true);
        } else {
            if (editItem != null) editItem.setVisible(true);
            if (saveItem != null) saveItem.setVisible(false);
        }
        
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (id == R.id.action_edit) {
            enableEditMode();
            return true;
        } else if (id == R.id.action_save) {
            saveEntry();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        if (isEditing) {
            // Show confirmation dialog or auto-save
            saveEntry();
        } else {
            super.onBackPressed();
        }
    }
} 
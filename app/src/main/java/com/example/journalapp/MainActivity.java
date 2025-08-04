package com.example.journalapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements JournalEntryAdapter.OnEntryClickListener {
    
    private RecyclerView recyclerView;
    private JournalEntryAdapter adapter;
    private LinearLayout emptyState;
    private LinearLayout searchLayout;
    private HorizontalScrollView filterScroll;
    private EditText etSearch;
    private ImageButton btnSearch, btnFilter, btnClearSearch;
    private FloatingActionButton fabAddEntry;
    
    private ActivityResultLauncher<Intent> addEntryLauncher;
    private ActivityResultLauncher<Intent> viewEditEntryLauncher;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        initViews();
        setupRecyclerView();
        setupClickListeners();
        setupActivityResultLaunchers();
        loadEntries();
    }
    
    private void initViews() {
        recyclerView = findViewById(R.id.recycler_entries);
        emptyState = findViewById(R.id.empty_state);
        searchLayout = findViewById(R.id.search_layout);
        filterScroll = findViewById(R.id.filter_scroll);
        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);
        btnFilter = findViewById(R.id.btn_filter);
        btnClearSearch = findViewById(R.id.btn_clear_search);
        fabAddEntry = findViewById(R.id.fab_add_entry);
    }
    
    private void setupRecyclerView() {
        adapter = new JournalEntryAdapter(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    
    private void setupClickListeners() {
        btnSearch.setOnClickListener(v -> toggleSearch());
        btnFilter.setOnClickListener(v -> toggleFilter());
        btnClearSearch.setOnClickListener(v -> clearSearch());
        fabAddEntry.setOnClickListener(v -> addNewEntry());
    }
    
    private void setupActivityResultLaunchers() {
        addEntryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadEntries();
                }
            }
        );
        
        viewEditEntryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadEntries();
                }
            }
        );
    }
    
    private void toggleSearch() {
        if (searchLayout.getVisibility() == View.VISIBLE) {
            searchLayout.setVisibility(View.GONE);
            clearSearch();
        } else {
            searchLayout.setVisibility(View.VISIBLE);
            etSearch.requestFocus();
        }
    }
    
    private void toggleFilter() {
        if (filterScroll.getVisibility() == View.VISIBLE) {
            filterScroll.setVisibility(View.GONE);
        } else {
            filterScroll.setVisibility(View.VISIBLE);
        }
    }
    
    private void clearSearch() {
        etSearch.setText("");
        loadEntries(); // Reload all entries
    }
    
    private void addNewEntry() {
        Intent intent = new Intent(this, ViewEditEntryActivity.class);
        intent.putExtra(ViewEditEntryActivity.EXTRA_IS_EDITING, true);
        addEntryLauncher.launch(intent);
    }
    
    private void loadEntries() {
        JournalDatabase database = JournalDatabase.getInstance(this);
        JournalDao dao = database.journalDao();
        
        new Thread(() -> {
            List<JournalEntry> entries = dao.getAllEntriesList();
            runOnUiThread(() -> {
                adapter.setEntries(entries);
                updateEmptyState(entries.isEmpty());
            });
        }).start();
    }
    
    private void updateEmptyState(boolean isEmpty) {
        if (isEmpty) {
            recyclerView.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }
    }
    
    @Override
    public void onEntryClick(JournalEntry entry) {
        Intent intent = new Intent(this, ViewEditEntryActivity.class);
        intent.putExtra(ViewEditEntryActivity.EXTRA_ENTRY_ID, entry.getId());
        intent.putExtra(ViewEditEntryActivity.EXTRA_ENTRY_TITLE, entry.getTitle());
        intent.putExtra(ViewEditEntryActivity.EXTRA_ENTRY_CONTENT, entry.getContent());
        intent.putExtra(ViewEditEntryActivity.EXTRA_ENTRY_MOOD, entry.getMood());
        intent.putExtra(ViewEditEntryActivity.EXTRA_ENTRY_CATEGORY, entry.getCategory());
        intent.putExtra(ViewEditEntryActivity.EXTRA_ENTRY_DATE, entry.getDate());
        intent.putExtra(ViewEditEntryActivity.EXTRA_IS_EDITING, false);
        
        viewEditEntryLauncher.launch(intent);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadEntries();
    }
}
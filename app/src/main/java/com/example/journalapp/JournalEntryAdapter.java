package com.example.journalapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JournalEntryAdapter extends RecyclerView.Adapter<JournalEntryAdapter.EntryViewHolder> {
    
    private List<JournalEntry> entries = new ArrayList<>();
    private Context context;
    private OnEntryClickListener listener;
    
    public interface OnEntryClickListener {
        void onEntryClick(JournalEntry entry);
    }
    
    public JournalEntryAdapter(Context context, OnEntryClickListener listener) {
        this.context = context;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_journal_entry, parent, false);
        return new EntryViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        JournalEntry entry = entries.get(position);
        holder.bind(entry);
    }
    
    @Override
    public int getItemCount() {
        return entries.size();
    }
    
    public void setEntries(List<JournalEntry> entries) {
        this.entries = entries;
        notifyDataSetChanged();
    }
    
    public void addEntry(JournalEntry entry) {
        entries.add(0, entry);
        notifyItemInserted(0);
    }
    
    public void updateEntry(JournalEntry entry) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getId() == entry.getId()) {
                entries.set(i, entry);
                notifyItemChanged(i);
                break;
            }
        }
    }
    
    public void removeEntry(JournalEntry entry) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getId() == entry.getId()) {
                entries.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }
    
    class EntryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvContent, tvDate, tvMood, tvCategory;
        
        public EntryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvMood = itemView.findViewById(R.id.tv_mood);
            tvCategory = itemView.findViewById(R.id.tv_category);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEntryClick(entries.get(position));
                }
            });
        }
        
        public void bind(JournalEntry entry) {
            tvTitle.setText(entry.getTitle());
            tvContent.setText(entry.getContent());
            
            // Format date
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
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
    }
} 
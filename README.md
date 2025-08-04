# Journal App

A simple Android journal application with editable entries and colorful text display.

## Features

### 📝 Editable Journal Entries
- **View Mode**: Read your journal entries with beautiful formatting
- **Edit Mode**: Tap the edit button to modify your entries
- **Auto-save**: Changes are automatically saved when you exit edit mode
- **Color-coded Text**: Different text elements use distinct colors for better readability

### 🎨 Color Scheme
- **Blue**: Entry titles and main headings
- **Green**: Entry content and descriptions
- **Purple**: Dates and secondary information
- **Orange**: Edit mode titles
- **Red**: Edit mode content

### 📱 User Interface
- **Modern Material Design**: Clean and intuitive interface
- **RecyclerView**: Smooth scrolling through entries
- **Card Layout**: Each entry displayed in an attractive card
- **Floating Action Button**: Easy access to create new entries
- **Search & Filter**: Built-in search and filtering capabilities

### 💾 Data Persistence
- **Room Database**: Local storage using Android Room
- **CRUD Operations**: Create, Read, Update, Delete entries
- **Automatic Backups**: Android's built-in backup system

## How to Use

1. **Create New Entry**: Tap the + button to create a new journal entry
2. **View Entries**: Tap on any entry to view it in full detail
3. **Edit Entries**: In the view screen, tap the edit button to modify the entry
4. **Save Changes**: Tap the save button or press back to save your changes
5. **Search**: Use the search icon to find specific entries
6. **Filter**: Use the filter icon to filter entries by mood or category

## Technical Details

- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Architecture**: MVVM with Room Database
- **UI Components**: RecyclerView, CardView, Material Design
- **Database**: SQLite with Room ORM

## Building the App

1. Open the project in Android Studio
2. Sync Gradle files
3. Build and run on an Android device or emulator

The app will automatically create the database and you can start adding journal entries immediately. 
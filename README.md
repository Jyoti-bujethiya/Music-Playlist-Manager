# Music Playlist Manager

A Java application that demonstrates linked list data structure implementation through a practical music playlist management system with a graphical user interface.

## Table of Contents
1. [Getting Started](#getting-started)
2. [User Interface Guide](#user-interface-guide)
3. [Code Structure and Technical Details](#code-structure-and-technical-details)
4. [Features](#features)
5. [Implementation Details](#implementation-details)
6. [Troubleshooting](#troubleshooting)

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- A Java IDE (optional) like IntelliJ IDEA, Eclipse, or VS Code with Java extensions

### Compilation and Execution
Open a terminal/command prompt, navigate to the project directory and run:

```bash
# Compile the application
javac MusicPlayerGUI.java

# Run the application
java MusicPlayerGUI
```

The graphical user interface should launch with sample songs already loaded.

## User Interface Guide

The Music Playlist Manager UI consists of several components:

### Main Window
- **Title Bar**: Shows "Music Playlist Manager" and the current playlist name
- **Playlist Panel** (Center): Displays the list of songs in your playlist
- **Control Panel** (Right): Contains buttons for playlist manipulation
- **Player Panel** (Bottom): Shows the current playing song and playback controls
- **Menu Bar** (Top): Contains File and Help menus

### Playlist Controls (Right Panel)
- **Add Song**: Opens a dialog to add a new song to the playlist
- **Remove Song**: Removes the currently selected song
- **Move Up**: Moves the selected song up one position in the playlist
- **Move Down**: Moves the selected song down one position in the playlist
- **Shuffle**: Randomly reorders all songs in the playlist

### Playback Controls (Bottom Panel)
- **Previous** (⏮): Moves to the previous song in the playlist
- **Play/Pause** (▶/⏸): Toggles playback state (simulation only)
- **Next** (⏭): Moves to the next song in the playlist
- **Now Playing**: Displays information about the current song

### File Menu
- **New Playlist**: Creates a new empty playlist
- **Open Playlist**: Loads a saved playlist from a .dat file
- **Save Playlist**: Saves the current playlist to a .dat file
- **Exit**: Closes the application

### Help Menu
- **About**: Shows information about the application

### Song Addition Dialog
When adding a new song, you'll need to provide:
- **Title** (required)
- **Artist** (required)
- **Album** (optional)
- **Duration** in seconds (required, must be a positive number)

## Code Structure and Technical Details

The application consists of four main classes:

### 1. Song Class
Represents a music track with properties and methods:
- **Properties**: title, artist, album, duration
- **Methods**: getters, setters, formatted duration, toString

### 2. Node Class
Forms the building blocks of the linked list:
- **Properties**: song data, next pointer, previous pointer
- **Methods**: getters, setters for all properties

### 3. Playlist Class
Implements a doubly-linked list for the playlist:
- **Core Functions**: add, remove, navigation, shuffle
- **Data Structure**: head node, tail node, current node
- **Serialization**: save/load functionality

### 4. MusicPlayerGUI Class
Provides the graphical user interface:
- **UI Components**: JFrame, JPanels, JButtons, JList
- **Event Handlers**: button clicks, menu selections
- **Playlist Operations**: wraps Playlist methods with UI updates

## Features

### Core Features
- Add songs to the beginning, end, or at a specific position
- Remove songs by position or by title/artist
- Display all songs with their details
- Calculate and show total playlist duration
- Navigate through the playlist (next/previous)
- Track current playing song

### Advanced Features
- Shuffle the playlist using Fisher-Yates algorithm
- Move songs up or down
- Save playlists to files (.dat format)
- Load playlists from files
- Sort functionality (by various attributes)

## Implementation Details

### Linked List Implementation
The playlist uses a **doubly-linked list** data structure, which offers several advantages:
- O(1) insertion at both ends
- Efficient navigation in both directions
- O(1) removal when node references are available
- Dynamic size adjustment

### Key Algorithms

#### Adding a Song
1. Create a new Node with the Song
2. If the list is empty, set both head and tail to the new node
3. Otherwise, link the new node appropriately:
   - For adding at end: Update tail's next pointer and new node's prev pointer
   - For adding at beginning: Update head's prev pointer and new node's next pointer
   - For adding at position: Navigate to position, update four pointers

#### Removing a Song
1. Handle special cases (empty list, removing head, removing tail)
2. For middle nodes, update prev and next node pointers to skip the removed node
3. Update size counter
4. Handle currentSong pointer if removing the currently playing song

#### Shuffling
1. Convert linked list to array
2. Apply Fisher-Yates shuffle algorithm
3. Rebuild linked list with shuffled order

### Serialization
The playlist save/load functionality uses Java's ObjectInputStream/ObjectOutputStream:
1. Songs are stored as separate fields
2. On load, songs are reconstructed and a new linked list is built
3. Current song position is preserved

### UI Implementation
- Uses Java Swing for the GUI components
- Implements event listeners for user interactions
- Updates the UI whenever the playlist changes
- Ensures proper selection state management

## Troubleshooting

### Common Compilation Errors
- **Class not found**: Make sure all four Java files are in the same directory
- **Comment syntax errors**: Ensure file headers use proper Java comment syntax (`//`)
- **Missing methods**: Check for typos in method names or signatures

### Runtime Issues
- **Empty playlist display**: Check that the updateSongList() method is called after playlist modifications
- **NullPointerException**: Ensure proper null checks especially when navigating the playlist
- **File not found**: Check file paths when saving/loading playlists

### UI Concerns
- **UI looks different**: The application uses the system's look and feel (UIManager.getSystemLookAndFeelClassName()). Appearance will vary by operating system.
- **Dialog scale issues**: If text fields or buttons appear cut off, adjust the dialog size parameters

---

This application demonstrates the practical application of linked lists in software development, combining data structures knowledge with a useful end-user application.

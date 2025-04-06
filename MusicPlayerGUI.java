import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MusicPlayerGUI extends JFrame {
    private Playlist currentPlaylist;
    private DefaultListModel<String> songListModel;
    private JList<String> songList;
    private JLabel nowPlayingLabel;
    private JLabel durationLabel;
    private JButton prevButton, playButton, nextButton;
    private Song currentlyPlayingSong;
    private boolean isPlaying = false;

    public MusicPlayerGUI() {
        // Initialize playlist
        currentPlaylist = new Playlist("My Playlist");
        addSampleSongs();

        // Set up the main frame
        setTitle("Music Playlist Manager");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create playlist panel (left side)
        JPanel playlistPanel = createPlaylistPanel();
        mainPanel.add(playlistPanel, BorderLayout.CENTER);

        // Create controls panel (right side)
        JPanel controlsPanel = createControlsPanel();
        mainPanel.add(controlsPanel, BorderLayout.EAST);

        // Create player panel (bottom)
        JPanel playerPanel = createPlayerPanel();
        mainPanel.add(playerPanel, BorderLayout.SOUTH);

        // Add menu bar
        setJMenuBar(createMenuBar());

        // Add the main panel to the frame
        add(mainPanel);

        // Update the song list display
        updateSongList();
    }

    private JPanel createPlaylistPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Playlist"));

        // Create song list model and JList
        songListModel = new DefaultListModel<>();
        songList = new JList<>(songListModel);
        songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        songList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedIndex = songList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        // Move to the selected song
                        moveToSong(selectedIndex);
                    }
                }
            }
        });

        // Add JList to a scroll pane
        JScrollPane scrollPane = new JScrollPane(songList);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add info panel at the bottom
        JPanel infoPanel = new JPanel(new BorderLayout());
        durationLabel = new JLabel("Total Duration: " + currentPlaylist.getTotalDurationFormatted());
        infoPanel.add(durationLabel, BorderLayout.WEST);
        JLabel songCountLabel = new JLabel("Songs: " + currentPlaylist.getSize());
        infoPanel.add(songCountLabel, BorderLayout.EAST);
        panel.add(infoPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createControlsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Controls"));
        panel.setPreferredSize(new Dimension(200, 300));

        // Add song button
        JButton addButton = new JButton("Add Song");
        addButton.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddSongDialog();
            }
        });
        panel.add(addButton);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Remove song button
        JButton removeButton = new JButton("Remove Song");
        removeButton.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = songList.getSelectedIndex();
                if (selectedIndex != -1) {
                    currentPlaylist.removeSong(selectedIndex);
                    updateSongList();
                } else {
                    JOptionPane.showMessageDialog(MusicPlayerGUI.this,
                            "Please select a song to remove.",
                            "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        panel.add(removeButton);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Move up button
        JButton moveUpButton = new JButton("Move Up");
        moveUpButton.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        moveUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = songList.getSelectedIndex();
                if (selectedIndex > 0) {
                    currentPlaylist.moveSongUp(selectedIndex);
                    updateSongList();
                    songList.setSelectedIndex(selectedIndex - 1);
                }
            }
        });
        panel.add(moveUpButton);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Move down button
        JButton moveDownButton = new JButton("Move Down");
        moveDownButton.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        moveDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = songList.getSelectedIndex();
                if (selectedIndex != -1 && selectedIndex < currentPlaylist.getSize() - 1) {
                    currentPlaylist.moveSongDown(selectedIndex);
                    updateSongList();
                    songList.setSelectedIndex(selectedIndex + 1);
                }
            }
        });
        panel.add(moveDownButton);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Shuffle button
        JButton shuffleButton = new JButton("Shuffle");
        shuffleButton.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        shuffleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentPlaylist.shuffle();
                updateSongList();
            }
        });
        panel.add(shuffleButton);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Add vertical glue to push everything to the top
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createPlayerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Now Playing"));

        // Create now playing label
        nowPlayingLabel = new JLabel("No song selected");
        nowPlayingLabel.setHorizontalAlignment(JLabel.CENTER);
        nowPlayingLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(nowPlayingLabel, BorderLayout.CENTER);

        // Create player controls
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        // Previous button
        prevButton = new JButton("⏮ Previous");
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Song prev = currentPlaylist.previousSong();
                if (prev != null) {
                    updateNowPlaying(prev);
                }
            }
        });
        controlsPanel.add(prevButton);

        // Play/Pause button
        playButton = new JButton("▶ Play");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPlaying) {
                    playButton.setText("▶ Play");
                    isPlaying = false;
                } else {
                    Song current = currentPlaylist.getCurrentSong();
                    if (current != null) {
                        playButton.setText("⏸ Pause");
                        isPlaying = true;
                        updateNowPlaying(current);
                    }
                }
            }
        });
        controlsPanel.add(playButton);

        // Next button
        nextButton = new JButton("Next ⏭");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Song next = currentPlaylist.nextSong();
                if (next != null) {
                    updateNowPlaying(next);
                }
            }
        });
        controlsPanel.add(nextButton);

        panel.add(controlsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");

        // New playlist
        JMenuItem newItem = new JMenuItem("New Playlist");
        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(MusicPlayerGUI.this,
                        "Enter new playlist name:",
                        "New Playlist", JOptionPane.QUESTION_MESSAGE);
                if (name != null && !name.trim().isEmpty()) {
                    currentPlaylist = new Playlist(name);
                    updateSongList();
                    nowPlayingLabel.setText("No song selected");
                    setTitle("Music Playlist Manager - " + name);
                }
            }
        });
        fileMenu.add(newItem);

        // Open playlist
        JMenuItem openItem = new JMenuItem("Open Playlist");
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Open Playlist");
                fileChooser.setFileFilter(new FileNameExtensionFilter("Playlist Files (*.dat)", "dat"));
                
                int result = fileChooser.showOpenDialog(MusicPlayerGUI.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    Playlist loadedPlaylist = Playlist.loadFromFile(selectedFile.getAbsolutePath());
                    if (loadedPlaylist != null) {
                        currentPlaylist = loadedPlaylist;
                        updateSongList();
                        setTitle("Music Playlist Manager - " + currentPlaylist.getName());
                        JOptionPane.showMessageDialog(MusicPlayerGUI.this,
                                "Playlist loaded successfully!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(MusicPlayerGUI.this,
                                "Failed to load playlist.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        fileMenu.add(openItem);

        // Save playlist
        JMenuItem saveItem = new JMenuItem("Save Playlist");
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Playlist");
                fileChooser.setFileFilter(new FileNameExtensionFilter("Playlist Files (*.dat)", "dat"));
                
                int result = fileChooser.showSaveDialog(MusicPlayerGUI.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();
                    if (!filePath.endsWith(".dat")) {
                        filePath += ".dat";
                    }
                    
                    if (currentPlaylist.saveToFile(filePath)) {
                        JOptionPane.showMessageDialog(MusicPlayerGUI.this,
                                "Playlist saved successfully!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(MusicPlayerGUI.this,
                                "Failed to save playlist.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        fileMenu.add(saveItem);

        fileMenu.addSeparator();

        // Exit
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitItem);

        // Help menu
        JMenu helpMenu = new JMenu("Help");

        // About
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MusicPlayerGUI.this,
                        "Music Playlist Manager\nA Java Linked List Implementation\nVersion 1.0",
                        "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private void showAddSongDialog() {
        // Create custom dialog
        JDialog dialog = new JDialog(this, "Add Song", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Title:"));
        JTextField titleField = new JTextField();
        formPanel.add(titleField);

        formPanel.add(new JLabel("Artist:"));
        JTextField artistField = new JTextField();
        formPanel.add(artistField);

        formPanel.add(new JLabel("Album:"));
        JTextField albumField = new JTextField();
        formPanel.add(albumField);

        formPanel.add(new JLabel("Duration (seconds):"));
        JTextField durationField = new JTextField();
        formPanel.add(durationField);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        buttonPanel.add(cancelButton);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String title = titleField.getText().trim();
                    String artist = artistField.getText().trim();
                    String album = albumField.getText().trim();
                    int duration = Integer.parseInt(durationField.getText().trim());

                    if (title.isEmpty() || artist.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog,
                                "Title and artist are required!",
                                "Missing Information", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    if (duration <= 0) {
                        JOptionPane.showMessageDialog(dialog,
                                "Duration must be a positive number!",
                                "Invalid Duration", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    Song newSong = new Song(title, artist, album, duration);
                    currentPlaylist.addSong(newSong);
                    updateSongList();
                    dialog.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog,
                            "Duration must be a valid number!",
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(addButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void updateNowPlaying(Song song) {
        if (song != null) {
            currentlyPlayingSong = song;
            nowPlayingLabel.setText(song.getTitle() + " - " + song.getArtist() + " (" + song.getFormattedDuration() + ")");
            if (!isPlaying) {
                playButton.setText("▶ Play");
            } else {
                playButton.setText("⏸ Pause");
            }
        } else {
            nowPlayingLabel.setText("No song selected");
            playButton.setText("▶ Play");
            isPlaying = false;
        }
    }

    private void moveToSong(int index) {
        // Get the song at the specified index
        Song song = currentPlaylist.getSong(index);
        if (song != null) {
            // We need to iterate through the playlist to set the current song
            // This is a limitation of our current implementation
            // In a real application, we might modify the playlist class to support this directly
            int currentIndex = 0;
            Node current = currentPlaylist.getHead(); // Assuming you add a getHead() method to Playlist
            
            while (current != null && currentIndex < index) {
                current = current.getNext();
                currentIndex++;
            }
            
            if (current != null) {
                currentPlaylist.setCurrentNode(current); // Assuming you add a setCurrentNode method to Playlist
                updateNowPlaying(song);
            }
        }
    }

    private void updateSongList() {
        songListModel.clear();
        
        // Traverse the playlist and add each song to the list model
        Node current = currentPlaylist.getHead(); // Assuming you add a getHead() method
        int index = 0;
        
        while (current != null) {
            Song song = current.getSong();
            String displayText = song.getTitle() + " - " + song.getArtist() + " (" + song.getFormattedDuration() + ")";
            songListModel.addElement(displayText);
            current = current.getNext();
            index++;
        }
        
        // Update playlist information
        durationLabel.setText("Total Duration: " + currentPlaylist.getTotalDurationFormatted());
    }

    private void addSampleSongs() {
        currentPlaylist.addSong(new Song("Bohemian Rhapsody", "Queen", "A Night at the Opera", 354));
        currentPlaylist.addSong(new Song("Hotel California", "Eagles", "Hotel California", 390));
        currentPlaylist.addSong(new Song("Billie Jean", "Michael Jackson", "Thriller", 294));
        currentPlaylist.addSong(new Song("Sweet Child O' Mine", "Guns N' Roses", "Appetite for Destruction", 356));
        currentPlaylist.addSong(new Song("Imagine", "John Lennon", "Imagine", 183));
    }

    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Start the application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MusicPlayerGUI app = new MusicPlayerGUI();
                app.setVisible(true);
            }
        });
    }
}

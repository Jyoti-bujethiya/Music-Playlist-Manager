// Playlist.java
import java.io.*;
import java.util.Random;

public class Playlist {
    private Node head;
    private Node tail;
    private Node currentSong;
    private String name;
    private int size;
    
    // Getters for GUI - These are needed for the GUI implementation
    public Node getHead() {
        return head;
    }
    
    public void setCurrentNode(Node node) {
        this.currentSong = node;
    }

    public Playlist(String name) {
        this.name = name;
        this.head = null;
        this.tail = null;
        this.currentSong = null;
        this.size = 0;
    }

    // Add song to the end of playlist
    public void addSong(Song song) {
        Node newNode = new Node(song);
        size++;

        if (head == null) {
            head = newNode;
            tail = newNode;
            currentSong = newNode; // Set as current if it's the first song
            return;
        }

        tail.setNext(newNode);
        newNode.setPrev(tail);
        tail = newNode;
    }

    // Add song to the beginning of playlist
    public void addSongToBeginning(Song song) {
        Node newNode = new Node(song);
        size++;

        if (head == null) {
            head = newNode;
            tail = newNode;
            currentSong = newNode;
            return;
        }

        newNode.setNext(head);
        head.setPrev(newNode);
        head = newNode;
    }

    // Add song at specific position (0-based index)
    public boolean addSongAt(Song song, int position) {
        if (position < 0 || position > size) {
            return false;
        }

        if (position == 0) {
            addSongToBeginning(song);
            return true;
        }

        if (position == size) {
            addSong(song);
            return true;
        }

        Node newNode = new Node(song);
        Node current = head;
        
        // Navigate to the position
        for (int i = 0; i < position - 1; i++) {
            current = current.getNext();
        }

        // Insert the new node
        newNode.setNext(current.getNext());
        newNode.setPrev(current);
        
        if (current.getNext() != null) {
            current.getNext().setPrev(newNode);
        }
        
        current.setNext(newNode);
        size++;
        
        return true;
    }

    // Remove song by position
    public boolean removeSong(int position) {
        if (head == null || position < 0 || position >= size) {
            return false;
        }

        // If removing the current song, move to next
        if (currentSong == getNodeAt(position)) {
            nextSong();
        }

        if (position == 0) {
            // Remove head
            head = head.getNext();
            if (head != null) {
                head.setPrev(null);
            } else {
                tail = null;
            }
        } else if (position == size - 1) {
            // Remove tail
            tail = tail.getPrev();
            if (tail != null) {
                tail.setNext(null);
            } else {
                head = null;
            }
        } else {
            // Remove from middle
            Node current = getNodeAt(position);
            current.getPrev().setNext(current.getNext());
            current.getNext().setPrev(current.getPrev());
        }

        size--;
        return true;
    }

    // Remove song by title and artist
    public boolean removeSong(String title, String artist) {
        if (head == null) {
            return false;
        }

        Node current = head;
        int position = 0;

        while (current != null) {
            if (current.getSong().getTitle().equalsIgnoreCase(title) && 
                current.getSong().getArtist().equalsIgnoreCase(artist)) {
                return removeSong(position);
            }
            current = current.getNext();
            position++;
        }

        return false;
    }

    // Get node at position
    private Node getNodeAt(int position) {
        if (position < 0 || position >= size) {
            return null;
        }

        Node current = head;
        for (int i = 0; i < position; i++) {
            current = current.getNext();
        }
        return current;
    }

    // Get song at position
    public Song getSong(int position) {
        Node node = getNodeAt(position);
        return node != null ? node.getSong() : null;
    }

    // Display playlist
    public void displayPlaylist() {
        if (head == null) {
            System.out.println("Playlist is empty.");
            return;
        }

        System.out.println("\n===== " + name + " =====");
        System.out.println("Total songs: " + size);
        System.out.println("Total duration: " + getTotalDurationFormatted());

        Node current = head;
        int index = 0;

        while (current != null) {
            String currentMarker = (current == currentSong) ? " ► " : "   ";
            System.out.println(index + "." + currentMarker + current.getSong());
            current = current.getNext();
            index++;
        }
        System.out.println("===================\n");
    }

    // Calculate total duration of playlist in seconds
    public int getTotalDuration() {
        int total = 0;
        Node current = head;

        while (current != null) {
            total += current.getSong().getDuration();
            current = current.getNext();
        }

        return total;
    }

    // Get formatted total duration
    public String getTotalDurationFormatted() {
        int totalSeconds = getTotalDuration();
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%d:%02d", minutes, seconds);
        }
    }

    // Navigation methods
    public Song getCurrentSong() {
        return currentSong != null ? currentSong.getSong() : null;
    }

    public Song nextSong() {
        if (currentSong == null || currentSong.getNext() == null) {
            // If at the end, loop back to beginning
            currentSong = head;
        } else {
            currentSong = currentSong.getNext();
        }
        return getCurrentSong();
    }

    public Song previousSong() {
        if (currentSong == null || currentSong.getPrev() == null) {
            // If at the beginning, loop to end
            currentSong = tail;
        } else {
            currentSong = currentSong.getPrev();
        }
        return getCurrentSong();
    }

    // Shuffle the playlist
    public void shuffle() {
        if (size <= 1) {
            return;
        }

        // Convert linked list to array for easier shuffling
        Song[] songs = new Song[size];
        Node current = head;
        int index = 0;

        while (current != null) {
            songs[index++] = current.getSong();
            current = current.getNext();
        }

        // Shuffle the array using Fisher-Yates algorithm
        Random random = new Random();
        for (int i = songs.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Song temp = songs[i];
            songs[i] = songs[j];
            songs[j] = temp;
        }

        // Rebuild the linked list with shuffled songs
        head = null;
        tail = null;
        currentSong = null;
        size = 0;

        for (Song song : songs) {
            addSong(song);
        }
    }

    // Move song up in the playlist
    public boolean moveSongUp(int position) {
        if (position <= 0 || position >= size) {
            return false;
        }

        Node current = getNodeAt(position);
        Node previous = current.getPrev();

        // Update links to swap nodes
        if (previous.getPrev() != null) {
            previous.getPrev().setNext(current);
        } else {
            head = current;
        }

        if (current.getNext() != null) {
            current.getNext().setPrev(previous);
        } else {
            tail = previous;
        }

        current.setPrev(previous.getPrev());
        previous.setNext(current.getNext());
        current.setNext(previous);
        previous.setPrev(current);

        return true;
    }

    // Move song down in the playlist
    public boolean moveSongDown(int position) {
        if (position < 0 || position >= size - 1) {
            return false;
        }
        return moveSongUp(position + 1);
    }

    // Save playlist to file
    public boolean saveToFile(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            // Save playlist name and size
            oos.writeObject(name);
            oos.writeInt(size);

            // Save current song position
            int currentPosition = -1;
            if (currentSong != null) {
                Node temp = head;
                int pos = 0;
                while (temp != null) {
                    if (temp == currentSong) {
                        currentPosition = pos;
                        break;
                    }
                    temp = temp.getNext();
                    pos++;
                }
            }
            oos.writeInt(currentPosition);

            // Save all songs
            Node current = head;
            while (current != null) {
                Song song = current.getSong();
                oos.writeObject(song.getTitle());
                oos.writeObject(song.getArtist());
                oos.writeObject(song.getAlbum());
                oos.writeInt(song.getDuration());
                current = current.getNext();
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error saving playlist: " + e.getMessage());
            return false;
        }
    }

    // Load playlist from file
    public static Playlist loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            // Read playlist info
            String playlistName = (String) ois.readObject();
            int size = ois.readInt();
            int currentPosition = ois.readInt();

            Playlist playlist = new Playlist(playlistName);

            // Read and add all songs
            for (int i = 0; i < size; i++) {
                String title = (String) ois.readObject();
                String artist = (String) ois.readObject();
                String album = (String) ois.readObject();
                int duration = ois.readInt();

                Song song = new Song(title, artist, album, duration);
                playlist.addSong(song);
            }

            // Set current song
            if (currentPosition >= 0) {
                Node current = playlist.head;
                for (int i = 0; i < currentPosition; i++) {
                    current = current.getNext();
                }
                playlist.currentSong = current;
            }

            return playlist;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading playlist: " + e.getMessage());
            return null;
        }
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }
}
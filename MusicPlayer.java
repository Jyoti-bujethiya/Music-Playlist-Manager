// MusicPlayer.java
import java.util.Scanner;

public class MusicPlayer {
    private static Scanner scanner = new Scanner(System.in);
    private static Playlist currentPlaylist;

    public static void main(String[] args) {
        System.out.println("Welcome to Music Playlist Manager!");
        
        // Create a new playlist
        System.out.print("Enter a name for your playlist: ");
        String playlistName = scanner.nextLine();
        currentPlaylist = new Playlist(playlistName);
        
        // Add some sample songs
        addSampleSongs();
        
        boolean quit = false;
        while (!quit) {
            displayMenu();
            System.out.print("Choose an option: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                
                switch (choice) {
                    case 1:
                        addSong();
                        break;
                    case 2:
                        removeSong();
                        break;
                    case 3:
                        playSong();
                        break;
                    case 4:
                        currentPlaylist.displayPlaylist();
                        break;
                    case 5:
                        currentPlaylist.shuffle();
                        System.out.println("Playlist shuffled!");
                        break;
                    case 6:
                        moveSong();
                        break;
                    case 7:
                        savePlaylist();
                        break;
                    case 8:
                        loadPlaylist();
                        break;
                    case 9:
                        quit = true;
                        System.out.println("Thank you for using Music Playlist Manager!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
        
        scanner.close();
    }
    
    private static void displayMenu() {
        System.out.println("\n=== MUSIC PLAYLIST MANAGER ===");
        System.out.println("1. Add a song");
        System.out.println("2. Remove a song");
        System.out.println("3. Play music");
        System.out.println("4. Display playlist");
        System.out.println("5. Shuffle playlist");
        System.out.println("6. Move a song");
        System.out.println("7. Save playlist");
        System.out.println("8. Load playlist");
        System.out.println("9. Exit");
    }
    
    private static void addSong() {
        System.out.println("\n=== ADD SONG ===");
        System.out.print("Enter song title: ");
        String title = scanner.nextLine();
        
        System.out.print("Enter artist name: ");
        String artist = scanner.nextLine();
        
        System.out.print("Enter album name: ");
        String album = scanner.nextLine();
        
        int duration = 0;
        boolean validDuration = false;
        while (!validDuration) {
            try {
                System.out.print("Enter duration (in seconds): ");
                duration = Integer.parseInt(scanner.nextLine());
                if (duration > 0) {
                    validDuration = true;
                } else {
                    System.out.println("Duration must be positive.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
        
        Song newSong = new Song(title, artist, album, duration);
        
        System.out.println("\nWhere do you want to add this song?");
        System.out.println("1. At the beginning");
        System.out.println("2. At the end");
        System.out.println("3. At a specific position");
        
        int choice = 0;
        boolean validChoice = false;
        while (!validChoice) {
            try {
                System.out.print("Choose an option: ");
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 1 && choice <= 3) {
                    validChoice = true;
                } else {
                    System.out.println("Please choose from 1-3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
        
        switch (choice) {
            case 1:
                currentPlaylist.addSongToBeginning(newSong);
                break;
            case 2:
                currentPlaylist.addSong(newSong);
                break;
            case 3:
                System.out.print("Enter position (0 to " + currentPlaylist.getSize() + "): ");
                try {
                    int position = Integer.parseInt(scanner.nextLine());
                    if (currentPlaylist.addSongAt(newSong, position)) {
                        System.out.println("Song added successfully!");
                    } else {
                        System.out.println("Invalid position. Song not added.");
                    }
                    return;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid position. Song added at the end.");
                    currentPlaylist.addSong(newSong);
                }
                break;
        }
        
        System.out.println("Song added successfully!");
    }
    
    private static void removeSong() {
        if (currentPlaylist.getSize() == 0) {
            System.out.println("Playlist is empty.");
            return;
        }
        
        currentPlaylist.displayPlaylist();
        System.out.println("\n=== REMOVE SONG ===");
        System.out.println("1. Remove by position");
        System.out.println("2. Remove by title and artist");
        
        int choice = 0;
        boolean validChoice = false;
        while (!validChoice) {
            try {
                System.out.print("Choose an option: ");
                choice = Integer.parseInt(scanner.nextLine());
                if (choice == 1 || choice == 2) {
                    validChoice = true;
                } else {
                    System.out.println("Please choose 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
        
        if (choice == 1) {
            System.out.print("Enter position to remove (0 to " + (currentPlaylist.getSize() - 1) + "): ");
            try {
                int position = Integer.parseInt(scanner.nextLine());
                if (currentPlaylist.removeSong(position)) {
                    System.out.println("Song removed successfully!");
                } else {
                    System.out.println("Invalid position. No song removed.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid position. No song removed.");
            }
        } else {
            System.out.print("Enter song title: ");
            String title = scanner.nextLine();
            
            System.out.print("Enter artist name: ");
            String artist = scanner.nextLine();
            
            if (currentPlaylist.removeSong(title, artist)) {
                System.out.println("Song removed successfully!");
            } else {
                System.out.println("Song not found. Nothing removed.");
            }
        }
    }
    
    private static void playSong() {
        if (currentPlaylist.getSize() == 0) {
            System.out.println("Playlist is empty. Nothing to play.");
            return;
        }
        
        boolean playing = true;
        
        while (playing) {
            Song currentSong = currentPlaylist.getCurrentSong();
            System.out.println("\n=== NOW PLAYING ===");
            System.out.println(currentSong);
            System.out.println("=================");
            System.out.println("1. Next song");
            System.out.println("2. Previous song");
            System.out.println("3. Stop playing");
            
            int choice = 0;
            boolean validChoice = false;
            while (!validChoice) {
                try {
                    System.out.print("Choose an option: ");
                    choice = Integer.parseInt(scanner.nextLine());
                    if (choice >= 1 && choice <= 3) {
                        validChoice = true;
                    } else {
                        System.out.println("Please choose from 1-3.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
            }
            
            switch (choice) {
                case 1:
                    currentPlaylist.nextSong();
                    break;
                case 2:
                    currentPlaylist.previousSong();
                    break;
                case 3:
                    playing = false;
                    break;
            }
        }
    }
    
    private static void moveSong() {
        if (currentPlaylist.getSize() <= 1) {
            System.out.println("Not enough songs to move.");
            return;
        }
        
        currentPlaylist.displayPlaylist();
        System.out.println("\n=== MOVE SONG ===");
        System.out.println("1. Move a song up");
        System.out.println("2. Move a song down");
        
        int choice = 0;
        boolean validChoice = false;
        while (!validChoice) {
            try {
                System.out.print("Choose an option: ");
                choice = Integer.parseInt(scanner.nextLine());
                if (choice == 1 || choice == 2) {
                    validChoice = true;
                } else {
                    System.out.println("Please choose 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
        
        try {
            System.out.print("Enter song position: ");
            int position = Integer.parseInt(scanner.nextLine());
            
            boolean success = false;
            if (choice == 1) {
                success = currentPlaylist.moveSongUp(position);
            } else {
                success = currentPlaylist.moveSongDown(position);
            }
            
            if (success) {
                System.out.println("Song moved successfully!");
                currentPlaylist.displayPlaylist();
            } else {
                System.out.println("Could not move song. Invalid position.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid position.");
        }
    }
    
    private static void savePlaylist() {
        System.out.print("Enter filename to save (e.g., myplaylist.dat): ");
        String filename = scanner.nextLine();
        
        if (currentPlaylist.saveToFile(filename)) {
            System.out.println("Playlist saved successfully to " + filename);
        } else {
            System.out.println("Failed to save playlist.");
        }
    }
    
    private static void loadPlaylist() {
        System.out.print("Enter filename to load (e.g., myplaylist.dat): ");
        String filename = scanner.nextLine();
        
        Playlist loaded = Playlist.loadFromFile(filename);
        if (loaded != null) {
            currentPlaylist = loaded;
            System.out.println("Playlist \"" + currentPlaylist.getName() + "\" loaded successfully!");
            currentPlaylist.displayPlaylist();
        } else {
            System.out.println("Failed to load playlist.");
        }
    }
    
    private static void addSampleSongs() {
        currentPlaylist.addSong(new Song("Bohemian Rhapsody", "Queen", "A Night at the Opera", 354));
        currentPlaylist.addSong(new Song("Hotel California", "Eagles", "Hotel California", 390));
        currentPlaylist.addSong(new Song("Billie Jean", "Michael Jackson", "Thriller", 294));
        currentPlaylist.addSong(new Song("Sweet Child O' Mine", "Guns N' Roses", "Appetite for Destruction", 356));
        currentPlaylist.addSong(new Song("Imagine", "John Lennon", "Imagine", 183));
        System.out.println("Added 5 sample songs to get you started!");
    }
}
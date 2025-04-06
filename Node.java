// Node.java
public class Node {
    private Song song;
    private Node next;
    private Node prev; // For doubly-linked list

    public Node(Song song) {
        this.song = song;
        this.next = null;
        this.prev = null;
    }

    // Getters
    public Song getSong() {
        return song;
    }

    public Node getNext() {
        return next;
    }

    public Node getPrev() {
        return prev;
    }

    // Setters
    public void setSong(Song song) {
        this.song = song;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}
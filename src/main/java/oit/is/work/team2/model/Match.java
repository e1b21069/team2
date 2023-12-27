package oit.is.work.team2.model;

public class Match {
    int id;
    int roomId;
    String word;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}

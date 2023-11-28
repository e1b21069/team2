package oit.is.work.team2.model;

public class User {
    private int id;
    private String name;
    private int roomId;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getRoomId() {
        return roomId;
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

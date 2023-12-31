package oit.is.work.team2.model;

public class Room {
    private int id;
    private String name;
    
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

    public Room(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

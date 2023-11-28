package oit.is.work.team2.model;

public class MatchInfo {
    private int id;
    private String roomName;
    private int pplNum;
    private boolean isActive;

    public int getId() {
        return this.id;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public int getPplNum() {
        return this.pplNum;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setPplNum(int pplNum) {
        this.pplNum = pplNum;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public MatchInfo(int id, String roomName, int pplNum, boolean isActive) {
        this.id = id;
        this.roomName = roomName;
        this.pplNum = pplNum;
        this.isActive = isActive;
    }
}
package oit.is.work.team2.model;

public class WordleLog{
  int id;
  int roomId;
  int userId;
  String ans;
  int result;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int roomId() {
    return roomId;
  }

  public void setRoomId(int roomId) {
    this.roomId = roomId;
  }

  public int userId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getAns() {
    return ans;
  }

  public void setAns(String ans) {
    this.ans = ans;
  }

  public int getResult() {
    return result;
  }

  public void setResult(int result) {
    this.result = result;
  }
}
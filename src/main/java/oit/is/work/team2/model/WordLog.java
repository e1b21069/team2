package oit.is.work.team2.model;

public class WordLog {
  int id;
  int roomId;
  int userId;
  String ans;
  int eatcnt;
  int bitecnt;

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

  public int getEatcnt() {
    return eatcnt;
  }

  public void setEatcnt(int eatcnt) {
    this.eatcnt = eatcnt;
  }

  public int getBitecnt() {
    return bitecnt;
  }

  public void setBitecnt(int bitecnt) {
    this.bitecnt = bitecnt;
  }

}

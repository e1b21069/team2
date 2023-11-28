package oit.is.work.team2.model;

public class Dictionary {
  int id;
  String word;

  public Dictionary() {
  }

  public Dictionary(int id, String word) {
    this.id = id;
    this.word = word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public String getWord() {
    return word;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
}

package oit.is.work.team2.model;

public class Dictionary {
  int id;
  String word;

  public Dictionary() {
  }

  public Dictionary(int id2, String word2) {
    this.id = id2;
    this.word = word2;
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

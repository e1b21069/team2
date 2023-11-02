package oit.is.work.team2.model;

public class Dictionary {
  int id;
  String word;

  // Thymeleafでフィールドを扱うためにはgetter/setterが必ず必要
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

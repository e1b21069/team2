package oit.is.work.team2.model;

public class Numeron {

  public boolean Atari(String word, String ans) {
    if (word.equals(ans) == true) {
      return true;
    }
    return false;
  }

  public int eatjudge(String word, String ans) {
    int hitcnt = 0;
    for (int i = 0; i < word.length(); i++) {
      char b = word.charAt(i);
      char c = ans.charAt(i);
      if (b == c) {
        hitcnt++;
      }
    }
    return hitcnt;
  }

  public int bitejudge(String word, String ans) {

    for (int i = 0; i < word.length(); i++) {
      char b = word.charAt(i);
      for (int j = 0; j < ans.length(); j++) {
        char c = ans.charAt(j);

        if (b == c) {

        } else {

        }
      }
    }

    return 1;
  }

}

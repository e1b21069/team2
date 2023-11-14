package oit.is.work.team2.model;

public class Numeron {

  public boolean Atari(String word, String ans) {
    if (word.equals(ans) == true) {
      return true;
    }
    return false;
  }

  // wordとansの文字が位置も含めて一致している個数を返す
  public int eatjudge(String word, String ans) {
    int eatcnt = 0;
    for (int i = 0; i < word.length(); i++) {
      if (word.charAt(i) == ans.charAt(i)) {
        eatcnt++;
      }
    }
    return eatcnt;
  }

  // wordの文字列の中にansの文字列が何個含まれているかどうか
  public int bitejudge(String word, String ans) {
    int bitecnt = 0;
    for (int i = 0; i < word.length(); i++) {
      for (int j = 0; j < ans.length(); j++) {
        if (word.charAt(i) == ans.charAt(j) && i != j) {
          bitecnt++;
        }
      }
    }
    return bitecnt;
  }


}

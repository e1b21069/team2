package oit.is.work.team2.model;

public class Numeron {
  int eatcnt = 0;

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
    this.eatcnt = eatcnt;
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

    // お題「rose」答え「rooo」=> 2eat 4bite -> 2eat 0bite表示
    // お題「book」答え「bobb」=> 2eat 0bite -> 2eat 2bite表示

    if (bitecnt == 4 && eatcnt > 0) {
      bitecnt = bitecnt - eatcnt;
      if (bitecnt < 0) {
        bitecnt = 0;
      }
    }

    return bitecnt;
  }

}

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
  /*
   * public int bitejudge(String word, String ans) {
   * int bitecnt = 0;
   * for (int i = 0; i < ans.length(); i++) {
   * for (int j = 0; j < word.length(); j++) {
   * if (ans.charAt(i) == word.charAt(j) && i != j) {
   * bitecnt++;
   * }
   * }
   * }
   * // お題「rose」答え「rooo」=> 2eat 4bite -> 2eat 0bite表示
   * bitecnt = bitecnt - eatcnt;
   * if (bitecnt < 0) {
   * bitecnt = 0;
   * }
   * return bitecnt;
   * }
   */

  public int bitejudge(String word, String ans) {
    int bitecnt = 0;
    int flag0 = 0, flag1 = 0, flag2 = 0, flag3 = 0;
    int finishFlag = 0;
    for (int i = 0; i < ans.length(); i++) {
      for (int j = 0; j < word.length(); j++) {
        if (word.charAt(i) == ans.charAt(j) && i != j) {
          switch (j) {
            case 0:
              if (flag0 == 0) {
                bitecnt++;
                flag0 = 1;
                finishFlag = 1;
              }
              break;

            case 1:
              if (flag1 == 0) {
                bitecnt++;
                flag1 = 1;
                finishFlag = 1;
              }
              break;

            case 2:
              if (flag2 == 0) {
                bitecnt++;
                flag2 = 1;
                finishFlag = 1;
              }
              break;

            case 3:
              if (flag3 == 0) {
                bitecnt++;
                flag3 = 1;
                finishFlag = 1;
              }
              break;
          }
        }
        if (finishFlag == 1) {
          finishFlag = 0;
          break;
        }
      }
    }
    return bitecnt - eatjudge(word, ans);
  }

}

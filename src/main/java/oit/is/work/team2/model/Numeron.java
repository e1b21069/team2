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

    for (int i = 0; i < word.length(); i++) {
      if (word.charAt(i) == ans.charAt(i)) {
        switch (i) {
          case 0:
            flag0 = 1;
          case 1:
            flag1 = 1;
          case 2:
            flag2 = 1;
          case 3:
            flag3 = 1;
        }
      }
    }

    for (int i = 0; i < ans.length(); i++) {
      int j = i;
      int cnt = 0;
      while (cnt < 4) {
        if (word.charAt(i) == ans.charAt(j)) {
          if (i == j) {
            finishFlag = 1;
          } else {
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
        }
        if (finishFlag == 1) {
          finishFlag = 0;
          break;
        }
        j++;
        if (j == 4) {
          j = 0;
        }
        cnt++;
      }
    }
    return bitecnt;
  }

}

package oit.is.work.team2.model;

public class Wordle {

  public boolean Atari(String word, String ans) {
    if (word.equals(ans) == true) {
      return true;
    }
    return false;
  }

  public int wordEat(String word, String ans) {
    int judge = 0;
    int[] used = {-1, -1, -1, -1}; // カウントした文字の位置
    // 位置が一致する文字の探索
    for (int i = 0; i < 4; i++) {
        if(ans.charAt(i) == word.charAt(i)) {
            int p = (int)Math.pow(10, i);
            judge = judge + (2*p);
            used[i] = 2;
        }
    }
    // 位置が一致しない文字の探索
    outerLoop:
    for (int i = 0; i < ans.length(); i++) {
      middleLoop:
      for(int j = 0; j < word.length(); j++) {
        if(ans.charAt(i) == word.charAt(j) && i == j) {
            break;
        } else if(ans.charAt(i) == word.charAt(j)) {
            innerLoop:
            for(int k = 0; k < word.length(); k++) {
                if(ans.charAt(i) == word.charAt(k) && used[k] == -1){
                    int p = (int)Math.pow(10, i);
                    judge = judge + p;
                    used[k] = 1;
                    break middleLoop;
                }
            }
        }
      }
    }
    return judge;
  }

  // wordとansの文字が位置も含めて一致している個数を返す
  public int wordjudge(String word, String ans) {
    int judge = 0;
    int[] used = {-1, -1, -1, -1}; // カウントした文字の位置
    // 位置が一致する文字の探索
    for (int i = 0; i < 4; i++) {
        if(ans.charAt(i) == word.charAt(i)) {
            int p = (int)Math.pow(10, i);
            judge = judge + (2*p);
            used[i] = 2;
        }
    }
    // 位置が一致しない文字の探索
    outerLoop:
    for (int i = 0; i < ans.length(); i++) {
      middleLoop:
      for(int j = 0; j < word.length(); j++) {
        if(ans.charAt(i) == word.charAt(j) && i == j) {
            break;
        } else if(ans.charAt(i) == word.charAt(j)) {
            innerLoop:
            for(int k = 0; k < word.length(); k++) {
                if(ans.charAt(i) == word.charAt(k) && used[k] == -1){
                    int p = (int)Math.pow(10, i);
                    judge = judge + p;
                    used[k] = 1;
                    break middleLoop;
                }
            }
        }
      }
    }
    return judge;
  }
}

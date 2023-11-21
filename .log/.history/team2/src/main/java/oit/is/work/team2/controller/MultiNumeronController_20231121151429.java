package oit.is.work.team2.controller;

import java.util.ArrayList;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.work.team2.model.WordLog;
import oit.is.work.team2.service.AsyncPlayMatch;

@Controller
@RequestMapping("/multiNumeron")
public class MultiNumeronController {

  private final Logger logger = LoggerFactory.getLogger(MultiNumeronController.class);

  @Autowired
  private AsyncPlayMatch ap1;

  @GetMapping("getLog")
  public String getWordLog(ModelMap model) {
    // ワードログを取得
    final ArrayList<WordLog> wordLogs = ap1.syncShowWordLogList();
    model.addAttribute("wordLogs", wordLogs);
    return "multiNumeron.html";
  }

  @GetMapping("step1")
  public SseEmitter pushCount() {
    // infoレベルでログを出力する
    logger.info("playturn");

    // finalは初期化したあとに再代入が行われない変数につける（意図しない再代入を防ぐ）
    final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);//
    // 引数にLongの最大値をTimeoutとして指定する

    try {
      this.ap1.playturn(emitter);
    } catch (IOException e) {
      // 例外の名前とメッセージだけ表示する
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
      emitter.complete();
    }
    return emitter;
  }
  /*
   * @Autowired
   * private DictionaryMapper dictionaryMapper;
   *
   * @Autowired
   * private WordLogMapper wordLogMapper;
   *
   * String randomWord = "";
   *
   * @GetMapping("/multiNumeron")
   * public String theme(ModelMap model) {
   * ArrayList<Dictionary> allWords = dictionaryMapper.selectAll();
   * if (allWords.isEmpty())
   * return "No words found in the database";
   * // ランダムなインデックスを生成
   * int randomIndex = (int) (Math.random() * allWords.size());
   * // ランダムに選択した単語を代入
   * this.randomWord = allWords.get(randomIndex).getWord();
   * model.addAttribute("randomWord", randomWord);
   * return "numeron.html";
   * }
   *
   * @PostMapping("/multiNumeron/step1")
   *
   * @Transactional
   * public String numeron(@RequestParam String ans, ModelMap model) {
   * boolean atari = false;
   * int eatcnt = 0, bitecnt = 0;
   * Numeron numeron = new Numeron();
   * model.addAttribute("randomWord", this.randomWord);
   * model.addAttribute("ans", ans);
   * atari = numeron.Atari(this.randomWord, ans);
   * model.addAttribute("atari", atari);
   * eatcnt = numeron.eatjudge(this.randomWord, ans);
   * model.addAttribute("eatcnt", eatcnt);
   * bitecnt = numeron.bitejudge(this.randomWord, ans);
   * model.addAttribute("bitecnt", bitecnt);
   *
   * // 単語を追加
   * wordLogMapper.insert(ans, eatcnt, bitecnt);
   * // 単語リストを取得
   * ArrayList<WordLog> logwords = wordLogMapper.selectAll();
   * model.addAttribute("logwords", logwords);
   *
   * if (eatcnt == 4) {
   * return "result.html";
   * }
   *
   * return "numeron.html";
   * }
   */

}

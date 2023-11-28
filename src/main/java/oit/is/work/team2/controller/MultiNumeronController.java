package oit.is.work.team2.controller;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import jakarta.servlet.http.HttpSession;
import oit.is.work.team2.model.Dictionary;
import oit.is.work.team2.model.Numeron;
import oit.is.work.team2.model.WordLog;
import oit.is.work.team2.model.WordLogMapper;
import oit.is.work.team2.model.Match;
import oit.is.work.team2.model.MatchMapper;
import oit.is.work.team2.service.AsyncPlayMatch;

@Controller
@RequestMapping("/multiNumeron")
public class MultiNumeronController {

  private final Logger logger = LoggerFactory.getLogger(MultiNumeronController.class);

  @Autowired
  private AsyncPlayMatch ap1;

  @Autowired
  private WordLogMapper wordLogMapper;
  @Autowired
  private MatchMapper matchMapper;

  private String randomWord = "";

  @GetMapping("room")
  public String enterRoom(ModelMap model) {
    return "multiRoom.html";
  }

  @GetMapping("sse")
  public SseEmitter sse() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.ap1.asyncShowWordLogsList(sseEmitter);
    return sseEmitter;
  }

  @GetMapping("getLog")
  public String getWordLog(ModelMap model) {
    // ワードログを取得
    final ArrayList<WordLog> wordLogs = ap1.syncShowWordLogList();
    model.addAttribute("wordLogs", wordLogs);
    return "multiNumeron.html";
  }

  // @PostMapping("addWordLog")
  // @Transactional
  // public String addWordLog(ModelMap model, @RequestParam String ans,
  // @RequestParam int eatcnt,
  // @RequestParam int bitecnt) {
  // model.addAttribute("addAns", ans);
  // model.addAttribute("addEatcnt", eatcnt);
  // model.addAttribute("addBitecnt", bitecnt);
  // // 単語を追加
  // this.ap1.syncAddWordLogs(ans, eatcnt, bitecnt);
  // // 単語リストを取得
  // final ArrayList<WordLog> wordLogs = ap1.syncShowWordLogList();
  // model.addAttribute("wordLogs", wordLogs);
  // return "multiNumeron.html";
  // }

  @GetMapping("/{param}")
  @Transactional
  public String numeronSet(@PathVariable String param, ModelMap model) {
    if(Integer.parseInt(param) == 1) {
      randomWord = ap1.setupMatch();
      model.addAttribute("randomWord", randomWord);
      return "multiNumeron.html";
    }
    return "multiWait.html";
  }

  @PostMapping("step1")
  @Transactional
  public String numeron(@RequestParam String ans, ModelMap model) {
    boolean atari = false;
    int eatcnt = 0, bitecnt = 0;
    String randomWord = matchMapper.selectWord(1);
    Numeron numeron = new Numeron();
    model.addAttribute("ans", ans);
    atari = numeron.Atari(randomWord, ans);
    model.addAttribute("atari", atari);
    eatcnt = numeron.eatjudge(randomWord, ans);
    model.addAttribute("eatcnt", eatcnt);
    bitecnt = numeron.bitejudge(randomWord, ans);
    model.addAttribute("bitecnt", bitecnt);

    // 単語を追加
    this.ap1.syncAddWordLogs(ans, eatcnt, bitecnt);

    // 単語リストを取得
    final ArrayList<WordLog> wordLogs = ap1.syncShowWordLogList();
    model.addAttribute("wordLogs", wordLogs);

    if (eatcnt == 4) {
      int gamecnt = wordLogMapper.dataCount();
      model.addAttribute("gamecnt", gamecnt);
      return "result.html";
    }

    return "multiWait.html";
  }



  @PostMapping("step2")
  @Transactional
  public String waitNumeron() {
    boolean dbUpdated = true;
    try {
      while (true) {// 無限ループ
      dbUpdated = this.ap1.selectUpdate();
      // logが更新されていなければ0.5s休み
      if (false == dbUpdated) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
      }
      this.ap1.switchUpdate();
      return "multiNumeron.html";
      }
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    }
    return "index.html";
  }

  // @GetMapping("step1")
  // public SseEmitter pushCount() {
  // // infoレベルでログを出力する
  // logger.info("playturn");

  // // finalは初期化したあとに再代入が行われない変数につける（意図しない再代入を防ぐ）
  // final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);//
  // // 引数にLongの最大値をTimeoutとして指定する

  // try {
  // this.ap1.asyncShowWordLogsList(emitter);
  // } catch (IOException e) {
  // // 例外の名前とメッセージだけ表示する
  // logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
  // emitter.complete();
  // }
  // return emitter;
  // }
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

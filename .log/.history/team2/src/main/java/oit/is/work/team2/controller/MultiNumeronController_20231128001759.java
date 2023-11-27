//MultiNumeronController.java

package oit.is.work.team2.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.work.team2.model.WordLog;
import oit.is.work.team2.model.Dictionary;
import oit.is.work.team2.model.DictionaryMapper;
import oit.is.work.team2.model.MatchInfo;
import oit.is.work.team2.model.MatchInfoMapper;
import oit.is.work.team2.model.Numeron;
import oit.is.work.team2.model.User;
import oit.is.work.team2.model.UserMapper;
import oit.is.work.team2.service.AsyncPlayMatch;

@Controller
@RequestMapping("/multiNumeron")
public class MultiNumeronController {

  @Autowired
  private AsyncPlayMatch playMatch;

  @Autowired
  private DictionaryMapper dictionaryMapper;

  @Autowired
  private UserMapper usermapper;

  @Autowired
  private MatchInfoMapper matchinfomapper;

  String randomWord = "";

  String name;

  @PostMapping("multiRoom")
  public String multiRoom(ModelMap model, @RequestParam String name) {
    this.name = name;
    model.addAttribute("name", name);
    ArrayList<User> users = usermapper.selectAll();
    ArrayList<MatchInfo> activeMatches = matchinfomapper.selectActiveMatches();
    model.addAttribute("users", users);
    model.addAttribute("activematches", activeMatches);
    return "multiRoom.html";
  }

  @GetMapping("match")
  public String match(@RequestParam String id, ModelMap model) {
    model.addAttribute("name", this.name);
    User opp = usermapper.selectById(id);
    model.addAttribute("opponent", opp);
    return "match.html";
  }

  @GetMapping("sse")
  public SseEmitter sse() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.playMatch.asyncShowWordLogsList(sseEmitter);
    return sseEmitter;
  }

  @GetMapping("getLog")
  public String getWordLog(ModelMap model) {
    // ワードログを取得
    final ArrayList<WordLog> wordLogs = playMatch.syncShowWordLogList();
    model.addAttribute("wordLogs", wordLogs);
    return "multiNumeron.html";
  }

  @PostMapping("addWordLog")
  @Transactional
  public String addWordLog(ModelMap model, @RequestParam String ans, @RequestParam int eatcnt,
      @RequestParam int bitecnt) {
    model.addAttribute("addAns", ans);
    model.addAttribute("addEatcnt", eatcnt);
    model.addAttribute("addBitecnt", bitecnt);
    // 単語を追加
    this.playMatch.syncAddWordLogs(ans, eatcnt, bitecnt);
    // 単語リストを取得
    final ArrayList<WordLog> wordLogs = playMatch.syncShowWordLogList();
    model.addAttribute("wordLogs", wordLogs);
    return "multiNumeron.html";
  }

  @GetMapping("")
  public String theme(ModelMap model) {
    ArrayList<Dictionary> allWords = dictionaryMapper.selectAll();
    if (allWords.isEmpty())
      return "No words found in the database";
    // ランダムなインデックスを生成
    int randomIndex = (int) (Math.random() * allWords.size());
    // ランダムに選択した単語を代入
    this.randomWord = allWords.get(randomIndex).getWord();
    model.addAttribute("randomWord", randomWord);
    return "multiNumeron.html";
  }

  @PostMapping("step1")
  @Transactional
  public String numeron(@RequestParam String ans, ModelMap model) {
    boolean atari = false;
    int eatcnt = 0, bitecnt = 0;
    Numeron numeron = new Numeron();
    model.addAttribute("randomWord", this.randomWord);
    model.addAttribute("ans", ans);
    atari = numeron.Atari(this.randomWord, ans);
    model.addAttribute("atari", atari);
    eatcnt = numeron.eatjudge(this.randomWord, ans);
    model.addAttribute("eatcnt", eatcnt);
    bitecnt = numeron.bitejudge(this.randomWord, ans);
    model.addAttribute("bitecnt", bitecnt);

    // 単語を追加
    playMatch.syncAddWordLogs(ans, eatcnt, bitecnt);
    // 単語リストを取得
    ArrayList<WordLog> logwords = playMatch.syncShowWordLogList();
    model.addAttribute("logwords", logwords);

    if (eatcnt == 4) {
      return "result.html";
    }
    // 3秒待つ
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    return "multiWait.html";
  }

}
//MultiNumeronController.java

package oit.is.work.team2.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties.Async;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oit.is.work.team2.model.WordLog;
import oit.is.work.team2.model.Dictionary;
import oit.is.work.team2.model.DictionaryMapper;
import oit.is.work.team2.model.MatchInfo;
import oit.is.work.team2.model.MatchInfoMapper;
import oit.is.work.team2.model.Numeron;
import oit.is.work.team2.model.Room;
import oit.is.work.team2.model.RoomMapper;
import oit.is.work.team2.model.UserMapper;
import oit.is.work.team2.model.WordLog;
import oit.is.work.team2.model.WordLogMapper;

import oit.is.work.team2.service.AsyncPlayMatch;

import java.util.concurrent.TimeUnit;

import oit.is.work.team2.model.Match;
import oit.is.work.team2.model.MatchMapper;

@Controller
@RequestMapping("/multiNumeron")
public class MultiNumeronController {

  private int screenNumber = 0;
  private final Logger logger = LoggerFactory.getLogger(MultiNumeronController.class);

  @Autowired
  private AsyncPlayMatch playMatch;

  @Autowired
  private DictionaryMapper dictionaryMapper;

  @Autowired
  private UserMapper usermapper;

  @Autowired
  private MatchInfoMapper matchinfomapper;

  @Autowired
  private RoomMapper roommapper;

  @Autowired
  private MatchMapper matchMapper;

  @Autowired
  private WordLogMapper wordLogMapper;

  String randomWord = "";

  String name = "";

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

  @GetMapping("/{param}")
  @Transactional
  public String numeronSet(@PathVariable String param, ModelMap model) {
    if (Integer.parseInt(param) == 1) {
      randomWord = playMatch.setupMatch();
      String word = matchMapper.selectWord(1);
      model.addAttribute("word", word);
      return "multiNumeron.html";
    }
    ArrayList<WordLog> wordlogs = playMatch.syncShowWordLogList();
    model.addAttribute("wordlogs", wordlogs);
    String word = matchMapper.selectWord(1);
    model.addAttribute("word", word);
    return "multiWait.html";
  }

  // ソロプレイ用
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
    playMatch.syncAddWordLogs(ans, eatcnt, bitecnt);

    // 単語リストを取得
    ArrayList<WordLog> wordlogs = playMatch.syncShowWordLogList();
    model.addAttribute("wordlogs", wordlogs);

    if (eatcnt == 4) {
      return "result.html";
    }
    return "multi.html";
  }

  @PostMapping("multiRoom")
  public String multiRoom(ModelMap model, @RequestParam String name) {
    this.name = name;
    model.addAttribute("name", name);
    ArrayList<Room> rooms = roommapper.selectAll();
    model.addAttribute("rooms", rooms);
    ArrayList<MatchInfo> activeMatches = matchinfomapper.selectActiveMatches();
    model.addAttribute("activematches", activeMatches);
    return "multiRoom.html";
  }

  @GetMapping("match")
  public String match(@RequestParam String roomId, ModelMap model) {
    usermapper.insert(Integer.parseInt(roomId), this.name); // ユーザーをDBに追加
    Room room = roommapper.selectById(Integer.parseInt(roomId));
    model.addAttribute("room", room);

    // roomIdのmatchinfoをtureにする
    MatchInfo matchinfo = matchinfomapper.selectMatchInfoById(Integer.parseInt(roomId));
    matchinfo.setIsActive(true);
    matchinfomapper.updateMatchInfoIsActive(matchinfo);
    // roomIdのuserの数を取得
    int pplNum = usermapper.selectCountByRoomId(Integer.parseInt(roomId));
    matchinfo.setPplNum(pplNum);
    matchinfomapper.updateMatchInfoPplNum(matchinfo);

    return "match.html";
  }

  @PostMapping("/nextScreen")
    public void nextScreen() {
        if (screenNumber % 2 == 0) {
          // クライアントに新しい画面情報を通知
          playMatch.sendData("Switch to screen " + 1);
        } else {
          // クライアントに新しい画面情報を通知
          playMatch.sendData("Switch to screen " + 2);
        }
   }

  // 先行が選択したあとに呼び出される
  @PostMapping("/first")
  @Transactional
  public String numeronfirst(@RequestParam String ans, ModelMap model) {
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
    playMatch.syncAddWordLogs(ans, eatcnt, bitecnt);

    // 単語リストを取得
    ArrayList<WordLog> wordlogs = wordLogMapper.selectAll();
    model.addAttribute("wordlogs", wordlogs);
    

    screenNumber++;
    if (eatcnt == 4) {
      return "result.html";
    }
    try {
      TimeUnit.MILLISECONDS.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return "multiWait.html";
  }
  
  // 相手が選択を行ったあとに呼び出される
  @GetMapping("/second")
  public String numeronSecond(ModelMap model) {
    ArrayList<WordLog> wordlogs = wordLogMapper.selectAll();
    model.addAttribute("wordlogs", wordlogs);
    String word = matchMapper.selectWord(1);
    model.addAttribute("word", word);
    return "multiNumeron.html";
  }

  @GetMapping("sse")
  public SseEmitter sse() {
    final SseEmitter sseEmitter = new SseEmitter();
    return sseEmitter;
  }

  @GetMapping("sseWait")
  public SseEmitter sseWait() {
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

}
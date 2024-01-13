package oit.is.work.team2.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.work.team2.model.Dictionary;
import oit.is.work.team2.model.DictionaryMapper;
import oit.is.work.team2.model.MatchInfo;
import oit.is.work.team2.model.MatchInfoMapper;
import oit.is.work.team2.model.MatchMapper;
import oit.is.work.team2.model.Numeron;
import oit.is.work.team2.model.Room;
import oit.is.work.team2.model.RoomMapper;
import oit.is.work.team2.model.UserMapper;
import oit.is.work.team2.model.WordLog;
import oit.is.work.team2.model.WordLogMapper;
import oit.is.work.team2.service.AsyncPlayMatch;

@Controller
@RequestMapping("/multiNumeron")
public class MultiNumeronController {

  private int screenNumber = 0;
  private int[] roomPlayer = {0, 0, 0, 0, 0, 0};
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

  @GetMapping("/numeronSet")
  @Transactional
  public String numeronSet(ModelMap model, Principal prin) {
    int roomId = usermapper.selectRoomIdByName(prin.getName());
    if(roomPlayer[roomId-1] == 0) {
      roomPlayer[roomId-1] = 1;
      this.randomWord = playMatch.setupMatch(roomId);
      String word = matchMapper.selectWord(roomId);
      model.addAttribute("word", word);
      return "multiNumeron.html";
    } else if(roomPlayer[roomId-1] == 1) {
      roomPlayer[roomId-1] = 0;
      ArrayList<WordLog> wordlogs = playMatch.syncShowWordLogList(roomId);
      model.addAttribute("wordlogs", wordlogs);
      String word = matchMapper.selectWord(roomId);
      model.addAttribute("word", word);
      return "multiWait.html";
    }
    return "multiWait.html";
  }

  @GetMapping("multiRoom")
  public String multiRoom(ModelMap model, Principal prin) {
    ArrayList<Room> rooms = roommapper.selectAll();
    model.addAttribute("rooms", rooms);
    ArrayList<MatchInfo> activeMatches = matchinfomapper.selectActiveMatches();
    model.addAttribute("activematches", activeMatches);
    return "multiRoom.html";
  }

  @GetMapping("match")
  public String match(@RequestParam String roomId, ModelMap model, Principal prin) {
    usermapper.insert(Integer.parseInt(roomId), prin.getName()); // ユーザーをDBに追加
    Room room = roommapper.selectById(Integer.parseInt(roomId));
    model.addAttribute("room", room);

    // roomIdのmatchinfoをtureにする
    MatchInfo matchinfo = matchinfomapper.selectMatchInfoById(Integer.parseInt(roomId));
    matchinfo.setIsActive(true);
    matchinfomapper.updateMatchInfoIsActive(matchinfo);
    // roomIdのuserの数を取得
    int pplNum = usermapper.selectCountByRoomId(Integer.parseInt(roomId));
    // 部屋の人数が3人以上になる時、multi.htmlへ戻る
    if (pplNum >= 3) {
      return "multiReturn.html";
    }
    matchinfo.setPplNum(pplNum);
    matchinfomapper.updateMatchInfoPplNum(matchinfo);

    return "match.html";
  }

  // どっかで使うかも
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
  public String numeronfirst(@RequestParam String ans, ModelMap model, Principal prin) {
    int roomId = usermapper.selectRoomIdByName(prin.getName());
    boolean atari = false;
    int eatcnt = 0, bitecnt = 0;
    String randomWord = matchMapper.selectWord(roomId);
    Numeron numeron = new Numeron();
    model.addAttribute("ans", ans);
    atari = numeron.Atari(randomWord, ans);
    model.addAttribute("atari", atari);
    eatcnt = numeron.eatjudge(randomWord, ans);
    model.addAttribute("eatcnt", eatcnt);
    bitecnt = numeron.bitejudge(randomWord, ans);
    model.addAttribute("bitecnt", bitecnt);

    // 単語を追加
    playMatch.syncAddWordLogs(roomId, ans, eatcnt, bitecnt);

    // 単語リストを取得
    ArrayList<WordLog> wordlogs = wordLogMapper.selectAllByRoomId(roomId);
    model.addAttribute("wordlogs", wordlogs);

    screenNumber++;
    if (eatcnt == 4) {
      screenNumber = 0;
      return "result.html";
    }
    try {
      TimeUnit.MILLISECONDS.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if(screenNumber == 1) {
      return "firstWait.html";
    }
    return "multiWait.html";
  }

  // 相手が選択を行ったあとに呼び出される
  @GetMapping("/second")
  public String numeronSecond(ModelMap model, Principal prin) {
    int roomId = usermapper.selectRoomIdByName(prin.getName());
    ArrayList<WordLog> wordlogs = wordLogMapper.selectAllByRoomId(roomId);
    model.addAttribute("wordlogs", wordlogs);
    String word = matchMapper.selectWord(roomId);
    model.addAttribute("word", word);
    return "multiNumeron.html";
  }

  @GetMapping("/lose")
  public String lose() {
    return "testLose.html";
  }

  @GetMapping("sse")
  public SseEmitter sse(Principal prin) {
    int roomId = usermapper.selectRoomIdByName(prin.getName());
    final SseEmitter sseEmitter = new SseEmitter();
    this.playMatch.asyncUpdate(sseEmitter, roomId);
    return sseEmitter;
  }

  @GetMapping("sseWait")
  public SseEmitter sseWait(Principal prin) {
    int roomId = usermapper.selectRoomIdByName(prin.getName());
    final SseEmitter sseEmitter = new SseEmitter();
    this.playMatch.asyncShowWordLogsList(sseEmitter, roomId);
    return sseEmitter;
  }

  @GetMapping("sseWaitFirst")
  public SseEmitter sseWaitFirst(Principal prin) {
    int roomId = usermapper.selectRoomIdByName(prin.getName());
    final SseEmitter sseEmitter = new SseEmitter();
    this.playMatch.asyncShowSecond(sseEmitter, roomId);
    return sseEmitter;
  }

  @GetMapping("sseRoomWait")
  public SseEmitter sseRoomWait(Principal prin) {
    int roomId = usermapper.selectRoomIdByName(prin.getName());
    final SseEmitter sseEmitter = new SseEmitter();
    this.playMatch.asyncShowRoom(sseEmitter, roomId);
    return sseEmitter;
  }

  @GetMapping("getLog")
  public String getWordLog(ModelMap model, Principal prin) {
    int roomId = usermapper.selectRoomIdByName(prin.getName());
    // ワードログを取得
    final ArrayList<WordLog> wordLogs = playMatch.syncShowWordLogList(roomId);
    model.addAttribute("wordLogs", wordLogs);
    return "multiNumeron.html";
  }

  // numeron.htmlに戻る
  @GetMapping("back")
  public String back(Principal prin) {
    int roomId = usermapper.selectRoomIdByName(prin.getName());
    wordLogMapper.deleteByRoomId(roomId);
    usermapper.deleteByRoomId(roomId);
    matchinfomapper.resetMatchInfo(roomId);
    matchMapper.deleteByRoomId(roomId);
    return "redirect:/numeron";
  }
}

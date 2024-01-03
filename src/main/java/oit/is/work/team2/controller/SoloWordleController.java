package oit.is.work.team2.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.work.team2.model.Dictionary;
import oit.is.work.team2.model.DictionaryMapper;
import oit.is.work.team2.model.User;
import oit.is.work.team2.model.UserMapper;
import oit.is.work.team2.model.Wordle;
import oit.is.work.team2.model.WordleLog;
import oit.is.work.team2.model.WordleLogMapper;

@Controller
@RequestMapping("/soloWordle")
public class SoloWordleController {

  @Autowired
  private DictionaryMapper dictionaryMapper;

  @Autowired
  private WordleLogMapper wordleLogMapper;

  @Autowired
  private UserMapper userMapper;

  private String randomWord = "";

  @GetMapping("")
  public String theme(ModelMap model, Principal prin) {
    User user = new User();
    user.setName(prin.getName());
    userMapper.insertUser(user);
    ArrayList<Dictionary> allWords = dictionaryMapper.selectAll();
    if (allWords.isEmpty())
      return "No words found in the database";
    // ランダムなインデックスを生成
    int randomIndex = (int) (Math.random() * allWords.size());
    // ランダムに選択した単語を代入
    this.randomWord = allWords.get(randomIndex).getWord();
    model.addAttribute("randomWord", randomWord);
    return "soloWordle.html";
  }

  @PostMapping("/solo")
  @Transactional
  public String solo(@RequestParam String ans, ModelMap model, Principal prin) {
    String name = prin.getName();
    int userId = userMapper.selectIdByName(name);
    boolean atari = false;
    Wordle wordle = new Wordle();
    model.addAttribute("randomWord", this.randomWord);
    model.addAttribute("ans", ans);
    atari = wordle.Atari(this.randomWord, ans);
    model.addAttribute("atari", atari);
    int result = wordle.wordEat(this.randomWord, ans);
    model.addAttribute("result", result);

    // 単語を追加
    wordleLogMapper.insertWithUserId(ans, userId, result);
    // 単語リストを取得
    ArrayList<WordleLog> logwords = wordleLogMapper.selectAllByUserId(userId);
    model.addAttribute("logwords", logwords);

    if (atari == true) {
      int gamecnt = wordleLogMapper.dataCount();
      model.addAttribute("gamecnt", gamecnt);
      return "soloResult.html";
    }
    return "soloWordle.html";
  }

  // numeron.htmlに戻る
  @GetMapping("back")
  public String back(Principal prin) {
    int userId = userMapper.selectIdByName(prin.getName());
    wordleLogMapper.deleteByUserId(userId);
    userMapper.deleteById(userId);
    return "redirect:/numeron";
  }
}

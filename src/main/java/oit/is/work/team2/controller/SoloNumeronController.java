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

import java.security.Principal;

import oit.is.work.team2.model.Dictionary;
import oit.is.work.team2.model.DictionaryMapper;
import oit.is.work.team2.model.Numeron;
import oit.is.work.team2.model.WordLogMapper;
import oit.is.work.team2.model.WordLog;
import oit.is.work.team2.model.UserMapper;
import oit.is.work.team2.model.User;

@Controller
@RequestMapping("/soloNumeron")
public class SoloNumeronController {

  @Autowired
  private DictionaryMapper dictionaryMapper;

  @Autowired
  private WordLogMapper wordLogMapper;

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
    return "soloNumeron.html";
  }

  @PostMapping("solo")
  @Transactional
  public String solo(@RequestParam String ans, ModelMap model, Principal prin) {
    String name = prin.getName();
    int userId = userMapper.selectIdByName(name);
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
    wordLogMapper.insertWithUserId(ans, userId, eatcnt, bitecnt);
    // 単語リストを取得
    ArrayList<WordLog> logwords = wordLogMapper.selectAllByUserId(userId);
    model.addAttribute("logwords", logwords);

    if (eatcnt == 4) {
      int gamecnt = wordLogMapper.dataCount();
      model.addAttribute("gamecnt", gamecnt);
      return "result.html";
    }
    return "soloNumeron.html";
  }

  // numeron.htmlに戻る
  @GetMapping("back")
  public String back(Principal prin) {
    int userId = userMapper.selectIdByName(prin.getName());
    wordLogMapper.deleteByUserId(userId);
    userMapper.deleteById(userId);
    return "redirect:/numeron";
  }
}

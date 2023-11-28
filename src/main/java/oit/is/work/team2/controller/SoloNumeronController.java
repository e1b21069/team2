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

import oit.is.work.team2.model.Dictionary;
import oit.is.work.team2.model.DictionaryMapper;
import oit.is.work.team2.model.Numeron;
import oit.is.work.team2.model.WordLogMapper;
import oit.is.work.team2.model.WordLog;

@Controller
@RequestMapping("/soloNumeron")
public class SoloNumeronController {

  @Autowired
  private DictionaryMapper dictionaryMapper;

  @Autowired
  private WordLogMapper wordLogMapper;

  private String randomWord = "";

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
    return "soloNumeron.html";
  }

  @PostMapping("step1")
  @Transactional
  public String solo(@RequestParam String ans, ModelMap model) {
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
    wordLogMapper.insert(ans, eatcnt, bitecnt);
    // 単語リストを取得
    ArrayList<WordLog> logwords = wordLogMapper.selectAll();
    model.addAttribute("logwords", logwords);

    if (eatcnt == 4) {
      int gamecnt = wordLogMapper.dataCount();
      model.addAttribute("gamecnt", gamecnt);
      return "result.html";
    }

    return "soloNumeron.html";
  }
}
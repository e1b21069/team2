package oit.is.work.team2.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.work.team2.model.Dictionary;
import oit.is.work.team2.model.DictionaryMapper;
import oit.is.work.team2.model.Numeron;

@Controller
public class NumeronController {

  @Autowired
  private DictionaryMapper dictionaryMapper;

  String randomWord = "";

  @GetMapping("/numeron")
  public String theme(ModelMap model) {
    ArrayList<Dictionary> allWords = dictionaryMapper.selectAllDictionary();
    if (allWords.isEmpty())
      return "No words found in the database";
    // ランダムなインデックスを生成
    int randomIndex = (int) (Math.random() * allWords.size());
    // ランダムに選択した単語を代入
    this.randomWord = allWords.get(randomIndex).getWord();
    model.addAttribute("randomWord", randomWord);
    return "numeron.html";
  }

  @GetMapping("/numeron/admin")
  public String themeAD(ModelMap model) {
    ArrayList<Dictionary> allWords = dictionaryMapper.selectAllDictionary();
    if (allWords.isEmpty())
      return "No words found in the database";
    // ランダムなインデックスを生成
    int randomIndex = (int) (Math.random() * allWords.size());
    // ランダムに選択した単語を代入
    this.randomWord = allWords.get(randomIndex).getWord();
    model.addAttribute("randomWord", randomWord);
    return "numeron.html";
  }

  @PostMapping("/numeron/step1")
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
    return "numeron.html";
  }

}

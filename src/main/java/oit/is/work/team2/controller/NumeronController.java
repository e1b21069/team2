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

@Controller
public class NumeronController {

    @Autowired
    private DictionaryMapper dictionaryMapper;

    @GetMapping("/numeron")
    public String theme(ModelMap model) {
        ArrayList<Dictionary> dictionary = dictionaryMapper.selectAllDictionary();
        model.addAttribute("dictionary", dictionary);
        // DBからランダムな単語を取得するメソッドを呼び出す
        String randomWord = getRandomWord();
        model.addAttribute("randomWord", randomWord);
        return "numeron.html";
    }

    @PostMapping("/numeron/step1")
    public String numeron(@RequestParam String ans, ModelMap model) {
        String randomWord = getRandomWord();
        model.addAttribute("randomWord", randomWord);
        model.addAttribute("ans", ans);
        return "numeron.html";
    }

    private String getRandomWord() {
        ArrayList<Dictionary> allWords = dictionaryMapper.selectAllDictionary();
        if (allWords.isEmpty())
            return "No words found in the database";
        // ランダムなインデックスを生成
        int randomIndex = (int) (Math.random() * allWords.size());
        // ランダムに選択した単語を返す
        return allWords.get(randomIndex).getWord();
    }

}
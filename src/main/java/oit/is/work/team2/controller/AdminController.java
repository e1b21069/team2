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

import oit.is.work.team2.model.DictionaryMapper;
import oit.is.work.team2.model.Dictionary;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    DictionaryMapper dictionaryMapper;

    @GetMapping("")
    public String admin() {
        return "admin.html";
    }

    @GetMapping("getList")
    public String getDictionaries(ModelMap model) {
        // 単語リストを取得
        ArrayList<Dictionary> dictionaries = dictionaryMapper.selectAll();
        model.addAttribute("dictionaries", dictionaries);
        return "admin.html";
    }

    @GetMapping("deleteWord")
    @Transactional
    public String deleteWord(@RequestParam Integer id, ModelMap model) {
        // 削除対象の単語を取得
        Dictionary dictionary1 = dictionaryMapper.selectById(id);
        model.addAttribute("dictionary1", dictionary1);
        // 削除
        dictionaryMapper.deleteById(id);
        // 削除後の単語リストを取得
        ArrayList<Dictionary> dictionaries = dictionaryMapper.selectAll();
        model.addAttribute("dictionaries", dictionaries);
        return "admin.html";
    }

    @GetMapping("editWord")
    @Transactional
    public String editWord(@RequestParam Integer id, ModelMap model) {
        // 編集対象の単語を取得
        Dictionary dictionary2 = dictionaryMapper.selectById(id);
        model.addAttribute("dictionary2", dictionary2);
        // 単語リストを取得
        ArrayList<Dictionary> dictionaries = dictionaryMapper.selectAll();
        model.addAttribute("dictionaries", dictionaries);
        return "admin.html";
    }

    @PostMapping("updateWord")
    public String updateWord(@RequestParam Integer id, @RequestParam String word, ModelMap model) {
        System.out.println("step4");
        System.out.println(id);
        System.out.println(word);
        Dictionary dictionary = new Dictionary(id, word);
        // 編集
        dictionaryMapper.updateById(dictionary);
        // 単語リストを取得
        ArrayList<Dictionary> dictionaries = dictionaryMapper.selectAll();
        model.addAttribute("dictionaries", dictionaries);
        return "admin.html";
    }

    @PostMapping("addWord")
    @Transactional
    public String addWord(ModelMap model, @RequestParam String word) {
        model.addAttribute("addWord", word);
        // 単語を追加
        dictionaryMapper.insert(word);
        // 単語リストを取得
        ArrayList<Dictionary> dictionaries = dictionaryMapper.selectAll();
        model.addAttribute("dictionaries", dictionaries);
        return "admin.html";
    }

}

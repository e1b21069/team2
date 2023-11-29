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

import oit.is.work.team2.model.DictionaryMapper;
import oit.is.work.team2.service.AsyncAdminDictionaryService;
import oit.is.work.team2.model.Dictionary;

@Controller
@RequestMapping("/dictionary")
public class AdminDictionaryController {

    @Autowired
    DictionaryMapper dictionaryMapper;

    @Autowired
    AsyncAdminDictionaryService adminService;

    @GetMapping("")
    public String dictionary(ModelMap model) {
        // 単語リストを取得
        final ArrayList<Dictionary> dictionaries = adminService.syncShowDictionariesList();
        model.addAttribute("dictionaries", dictionaries);
        return "adminDictionary.html";
    }

    @GetMapping("getList")
    public String getDictionaries(ModelMap model) {
        // 単語リストを取得
        final ArrayList<Dictionary> dictionaries = adminService.syncShowDictionariesList();
        model.addAttribute("dictionaries", dictionaries);
        return "adminDictionary.html";
    }

    @GetMapping("sse")
    public SseEmitter sse() {
        final SseEmitter sseEmitter = new SseEmitter();
        this.adminService.asyncShowDictionariesList(sseEmitter);
        return sseEmitter;
    }

    @GetMapping("deleteWord")
    @Transactional
    public String deleteWord(@RequestParam Integer id, ModelMap model) {
        // 選択した単語を削除し，削除対象の単語をmodelに登録
        final Dictionary dictionary1 = this.adminService.syncDeleteDictionaries(id);
        model.addAttribute("dictionary1", dictionary1);
        // 残りの単語リストを取得してmodelに登録
        final ArrayList<Dictionary> dictionaries = adminService.syncShowDictionariesList();
        model.addAttribute("dictionaries", dictionaries);
        return "adminDictionary.html";
    }

    @GetMapping("editWord")
    @Transactional
    public String editWord(@RequestParam Integer id, ModelMap model) {
        // 編集対象の単語を取得
        final Dictionary dictionary2 = this.adminService.syncEditDictionaries(id);
        model.addAttribute("dictionary2", dictionary2);
        // 単語リストを取得
        final ArrayList<Dictionary> dictionaries = adminService.syncShowDictionariesList();
        model.addAttribute("dictionaries", dictionaries);
        return "adminDictionary.html";
    }

    @PostMapping("updateWord")
    public String updateWord(@RequestParam Integer id, @RequestParam String word, ModelMap model) {
        System.out.println("updateWord");
        System.out.println(id);
        System.out.println(word);
        Dictionary dictionary = new Dictionary(id, word);
        // 編集
        this.adminService.syncUpdateDictionaries(dictionary);
        // 単語リストを取得
        final ArrayList<Dictionary> dictionaries = adminService.syncShowDictionariesList();
        model.addAttribute("dictionaries", dictionaries);
        model.addAttribute("beforeWord", word);
        return "adminDictionary.html";
    }

    @PostMapping("addWord")
    @Transactional
    public String addWord(ModelMap model, @RequestParam String word) {
        model.addAttribute("addWord", word);
        // 単語を追加
        this.adminService.syncAddDictionaries(word);
        // 単語リストを取得
        final ArrayList<Dictionary> dictionaries = adminService.syncShowDictionariesList();
        model.addAttribute("dictionaries", dictionaries);
        return "adminDictionary.html";
    }

}

package oit.is.work.team2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wordle")
public class WordleController {
    @GetMapping("")
    public String wordle() {
        return "wordle.html";
    }

    // wordle.htmlに戻る
    @GetMapping("back")
    public String back() {
        return "redirect:/wordle"; // "/numeron"へのリダイレクトを指定
    }
}

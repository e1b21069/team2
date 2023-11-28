package oit.is.work.team2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/numeron")
public class NumeronController {
    @GetMapping("")
    public String numeron() {
        return "numeron.html";
    }

    // numeron.htmlに戻る
    @GetMapping("back")
    public String back() {
        return "redirect:/numeron"; // "/numeron"へのリダイレクトを指定
    }
}

package oit.is.work.team2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NumeronController {
    @GetMapping("numeron")
    public String numeron() {
        return "numeron.html";
    }
}

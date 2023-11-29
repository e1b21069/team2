package oit.is.work.team2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("")
    public String admin(ModelMap model) {
        return "admin.html";
    }

    @GetMapping("dictionary")
    public String dictionary(ModelMap model) {
        return "adminDictionary.html";
    }

    @GetMapping("rooms")
    public String rooms(ModelMap model) {
        return "adminRooms.html";
    }
}

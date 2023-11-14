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

import oit.is.work.team2.model.WordLogMapper;
import oit.is.work.team2.model.DictionaryMapper;
import oit.is.work.team2.model.WordLog;

@Controller
public class WordLogController {
    @Autowired
    WordLogMapper wordLogMapper;
}

package org.example.controller;

import org.example.service.GetStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @Autowired
    private GetStats getStats;

    @GetMapping("/")
    public String getIndex(Model model) {
        getStats.getStatsForPage(model);
        System.out.println(1);
        return "index";
    }
}

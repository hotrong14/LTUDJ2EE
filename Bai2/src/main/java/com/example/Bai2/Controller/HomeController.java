package com.example.Bai2.Controller;

import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class HomeController {
    // @GetMapping("/home")
    // public String index() {
    //     return "index";
    // }
    @GetMapping("/home")
    public String home (Model model) {
        model.addAttribute("name", "TrongDZ");
        return "index";
    }
}
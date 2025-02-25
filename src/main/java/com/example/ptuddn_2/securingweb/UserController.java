package com.example.ptuddn_2.securingweb;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/dashboard")
    public String userDashboard(Model model) {
        model.addAttribute("title", "User Dashboard");
        return "user"; // ✅ Trả về file user.html
    }
}

package com.example.ptuddn_2.securingweb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Trang chính sau khi đăng nhập
    @GetMapping("/home")
    public String home() {
        return "home";  // Chuyển hướng đến trang home.html
    }

    // Trang dành cho Admin
    @GetMapping("/admin/home")
    public String adminHome() {
        return "admin";  // Chuyển hướng đến trang admin_home.html
    }

    // Trang dành cho User
    @GetMapping("/user/home")
    public String userHome() {
        return "user";  // Chuyển hướng đến trang user_home.html
    }
}

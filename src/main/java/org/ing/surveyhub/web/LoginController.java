package org.ing.surveyhub.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/admin";
    }

    @GetMapping("/admin")
    public String dashboard() {
        return "admin/dashboard";
    }
}

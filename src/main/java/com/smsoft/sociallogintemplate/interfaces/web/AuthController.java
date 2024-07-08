package com.smsoft.sociallogintemplate.interfaces.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home() {
        return "main";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }
}
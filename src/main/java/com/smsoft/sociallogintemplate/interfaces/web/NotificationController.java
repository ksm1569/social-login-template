package com.smsoft.sociallogintemplate.interfaces.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @GetMapping
    public String notificationsPage() {
        return "notifications";
    }
}
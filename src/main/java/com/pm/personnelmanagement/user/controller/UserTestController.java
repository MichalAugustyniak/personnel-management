package com.pm.personnelmanagement.user.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/users")
public class UserTestController {
    @GetMapping
    public String test(HttpSession session) {
        return "SESSIONID=" + session.getId() +
                "\nisLogged=" + session.getAttribute("isLogged");
    }

}

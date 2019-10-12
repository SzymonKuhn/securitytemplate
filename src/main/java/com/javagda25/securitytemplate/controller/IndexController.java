package com.javagda25.securitytemplate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping ("/")
public class IndexController  {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String indexZLogowaniem() {
        return "login-form";
    }

    @GetMapping("/tylkodlakozakow")
    public String kozaki() {
        return "index";
    }
}

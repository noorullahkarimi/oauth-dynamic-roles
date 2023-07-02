package com.example.oauth_jdbc_authentication.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @GetMapping("/login")
    public String login(){
        return "/login.html";
    }

    @GetMapping("/")
    @ResponseBody
    public String index(){
        return "original page";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin(){
        return "admin page";
    }

    @GetMapping("/user")
    @ResponseBody
    public String users(){
        return "users page";
    }
}

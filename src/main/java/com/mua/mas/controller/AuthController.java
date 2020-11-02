package com.mua.mas.controller;

import com.mua.mas.model.LoginCredential;
import com.mua.mas.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/join")
    public Boolean join(@RequestBody LoginCredential loginCredential){
        return service.join(loginCredential.getUsername(), loginCredential.getPassword());
    }

    @GetMapping("/check")
    public Boolean check(@RequestParam String username){
        return service.check(username);
    }

    @PostMapping("/login")
    public LoginCredential login(@RequestBody LoginCredential loginCredential){
        return service.login(loginCredential.getUsername(),loginCredential.getPassword());
    }

}

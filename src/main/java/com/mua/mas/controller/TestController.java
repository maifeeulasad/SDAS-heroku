package com.mua.mas.controller;

import com.mua.mas.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService service;

    @GetMapping("/time")
    public String time(@RequestParam(required = false) Boolean auth){
        return service.time(auth);
    }

    @GetMapping("/auth")
    public Boolean auth(){
        return service.auth();
    }

}

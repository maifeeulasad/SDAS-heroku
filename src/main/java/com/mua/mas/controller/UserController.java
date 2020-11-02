package com.mua.mas.controller;

import com.mua.mas.model.User;
import com.mua.mas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;


    @GetMapping("/edit")
    public Boolean edit(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email){
        return service.edit(name,email);
    }

    @PostMapping("/add/contact")
    public Boolean addContact(@RequestBody String contact){
        return service.addContact(contact);
    }

    @PostMapping("/remove/contact")
    public Boolean removeContact(@RequestBody String contact){
        return service.removeContact(contact);
    }

    @GetMapping("/details")
    public User getOne(@RequestParam(name = "user_id",required = false) Long userId){
        if(userId==null)
            return service.details();
        return service.getOne(userId);
    }

    @GetMapping("/validate/same")
    public Boolean isSameUser(@RequestParam(name = "user_id") Long userId){
        return service.isSameUser(userId);
    }

}

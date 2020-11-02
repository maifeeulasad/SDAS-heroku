package com.mua.mas.controller;

import com.mua.mas.model.Session;
import com.mua.mas.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/session")
public class SessionController {

    @Autowired
    private SessionService service;

    @PostMapping("/create")
    public Long create(@RequestParam(name = "classroom_id") Long classroomId,
                       @RequestBody Session session){
        return service.create(classroomId,session);
    }

}

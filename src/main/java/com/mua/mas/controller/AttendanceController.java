package com.mua.mas.controller;

import com.mua.mas.dto.AttendanceDto;
import com.mua.mas.model.Attendance;
import com.mua.mas.model.State;
import com.mua.mas.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService service;

    @GetMapping("/all")
    public List<AttendanceDto> all(@RequestParam(name = "session_id") Long sessionId){
        return service.all(sessionId);
    }

    @PostMapping("/attend")
    public Boolean attend(@RequestParam(name = "session_id") Long sessionId,
                          @RequestBody Attendance attendance){
        if(attendance.getCode()==null
                || attendance.getCoordinate()==null){
            return false;
        }
        return service.attend(sessionId,attendance);
    }

    @GetMapping("/verify")
    public Boolean verify(@RequestParam(name = "session_id") Long sessionId,
                          @RequestParam(name = "attendance_id") Long attendanceId,
                          @RequestParam State state){
        return service.verify(sessionId,attendanceId,state);
    }

}

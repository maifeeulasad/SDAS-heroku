package com.mua.mas.controller;

import com.mua.mas.dto.ClassroomDto;
import com.mua.mas.dto.SessionDto;
import com.mua.mas.dto.UserClassroomRoleDto;
import com.mua.mas.model.Classroom;
import com.mua.mas.model.Role;
import com.mua.mas.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/classroom")
public class ClassroomController {

    @Autowired
    private ClassroomService service;

    @GetMapping("/all")
    public List<ClassroomDto> all(@RequestParam(name = "query",required = false) String keyword){
        if(keyword==null)
            return service.all();
        return service.all(keyword);
    }

    @GetMapping("/create")
    public Long create(@RequestParam String name,
                       @RequestParam(required = false) String details,
                       @RequestParam Role role){
        return service.create(name,details,role);
    }


    @GetMapping("/role")
    public Role role(@RequestParam(name = "classroom_id") Long classroomId){
        return service.role(classroomId);
    }

    @PostMapping("/edit")
    public Boolean edit(@RequestParam(name = "classroom_id") Long classroomId,
                     @RequestBody Classroom classroom){
        return service.edit(classroomId,classroom);
    }

    @GetMapping("/sessions")
    public List<SessionDto> sessions(@RequestParam(name = "classroom_id") Long classroomId){
        return service.sessions(classroomId);
    }

    @GetMapping("/roles")
    public List<UserClassroomRoleDto> roles(@RequestParam(name = "classroom_id") Long classroomId){
        return service.roles(classroomId);
    }

    @GetMapping("/join")
    public Boolean join(@RequestParam(name = "classroom_id") Long classroomId){
        return service.join(classroomId);
    }

    @GetMapping("/add")
    public Boolean add(@RequestParam(name = "classroom_id") Long classroomId,
                       @RequestParam(name = "user_id", required = false) Long userId,
                       @RequestParam(required = false) String username,
                       @RequestParam(required = false) Role role){
        if(userId==null && username==null){
            return false;
        }
        if(role!=null){
            if(userId!=null){
                return service.add(classroomId,userId,role);
            }
            return service.add(classroomId,username,role);
        }
        if(userId!=null){
            return service.add(classroomId,userId,null);
        }
        return service.add(classroomId,username,null);
    }

    @GetMapping("/assign")
    public Boolean assign(@RequestParam(name = "classroom_id") Long classroomId,
                          @RequestParam(name = "user_id", required = false) Long userId,
                          @RequestParam(required = false) String username,
                          @RequestParam Role role){
        if(userId==null && username==null){
            return false;
        }
        if(userId!=null){
            return service.assign(classroomId,userId,role);
        }
        return service.assign(classroomId, username, role);
    }

    @GetMapping("/remove")
    public Boolean remove(@RequestParam(name = "classroom_id") Long classroomId,
                          @RequestParam(name = "user_id", required = false) Long userId,
                          @RequestParam(required = false) String username){
        if(userId==null && username==null){
            return false;
        }
        if(userId!=null){
            return service.remove(classroomId,userId);
        }
        return service.remove(classroomId, username);
    }

}

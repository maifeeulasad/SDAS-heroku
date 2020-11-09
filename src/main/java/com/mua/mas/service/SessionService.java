package com.mua.mas.service;

import com.mua.mas.model.*;
import com.mua.mas.repo.ClassroomRepo;
import com.mua.mas.repo.SessionRepo;
import com.mua.mas.repo.UserClassroomRoleRepo;
import com.mua.mas.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserClassroomRoleRepo userClassroomRoleRepo;
    @Autowired
    private ClassroomRepo classroomRepo;
    @Autowired
    private SessionRepo sessionRepo;

    public Long create(Long classroomId, Session session) {
        if(session.getBounds().size()<3){
            return -1L;
        }
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            Optional<Classroom> optionalClassroom = classroomRepo.findById(classroomId);
            if(!optionalClassroom.isPresent()){
                return -1L;
            }
            Classroom classroom = optionalClassroom.get();
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalUser = userRepo.findByUsername(principal.getUsername());
            if(!optionalUser.isPresent()){
                return -1L;
            }
            User user = optionalUser.get();
            Optional<UserClassroomRole> optionalUserClassroomRole
                    = userClassroomRoleRepo
                    .findByUser_UserIdAndClassroom_ClassroomId(user.getUserId(), classroomId);
            if(!optionalUserClassroomRole.isPresent()){
                return -1L;
            }
            UserClassroomRole userClassroomRole = optionalUserClassroomRole.get();
            if(userClassroomRole.getRole()== Role.CR || userClassroomRole.getRole()==Role.Teacher){
                session.setClassroom(classroom);
                classroom.getSessions().add(session);
                return sessionRepo.save(session).getSessionId();
            }
        }
        return -1L;
    }
}

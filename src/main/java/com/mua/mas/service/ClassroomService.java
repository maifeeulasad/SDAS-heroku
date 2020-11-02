package com.mua.mas.service;

import com.mua.mas.dto.ClassroomDto;
import com.mua.mas.dto.SessionDto;
import com.mua.mas.dto.UserClassroomRoleDto;
import com.mua.mas.model.*;
import com.mua.mas.repo.ClassroomRepo;
import com.mua.mas.repo.SessionRepo;
import com.mua.mas.repo.UserClassroomRoleRepo;
import com.mua.mas.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ClassroomService {

    @Autowired
    private UserClassroomRoleRepo userClassroomRoleRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ClassroomRepo classroomRepo;
    @Autowired
    private SessionRepo sessionRepo;

    public Long create(String name,String details,Role role) {
        if(role == null || role==Role.Student)
            return -1L;
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalUser = userRepo.findByUsername(principal.getUsername());
            if(optionalUser.isEmpty()){
                return -1L;
            }
            User user = optionalUser.get();
            Classroom classroom = new Classroom();
            classroom.setName(name);
            classroom.setDetails(details);
            UserClassroomRole userClassroomRole = new UserClassroomRole(user,role,classroom);
            classroom.setUserClassroomRoleList(List.of(userClassroomRole));
            user.setUserClassroomRoles(List.of(userClassroomRole));
            UserClassroomRole saveUserClassroomRole=userClassroomRoleRepo.save(userClassroomRole);
            return saveUserClassroomRole.getClassroom().getClassroomId();
        }
        return -1L;
    }

    public Boolean join(Long classroomId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalUser = userRepo.findByUsername(principal.getUsername());
            if(optionalUser.isEmpty()){
                return false;
            }
            Optional<Classroom> classroomOptional = classroomRepo.findById(classroomId);
            if(classroomOptional.isEmpty()){
                return false;
            }
            Classroom classroom = classroomOptional.get();
            User user = optionalUser.get();
            Optional<UserClassroomRole> optionalUserClassroomRole
                    = userClassroomRoleRepo.findByUser_UserIdAndClassroom_ClassroomId(user.getUserId(),classroomId);
            if(optionalUserClassroomRole.isEmpty()){
                UserClassroomRole userClassroomRole = new UserClassroomRole(user,Role.Pending,classroom);
                user.getUserClassroomRoles().add(userClassroomRole);
                userClassroomRole.setUser(user);
                userClassroomRole.setClassroom(classroom);
                classroom.getUserClassroomRoleList().add(userClassroomRole);
                userClassroomRoleRepo.save(userClassroomRole);
                return true;
            }
        }
        return false;
    }

    public Boolean add(Long classroomId, Long userId, Role role) {
        Optional<User> user = userRepo.findById(userId);
        if(user.isEmpty()){
            return false;
        }
        return add(classroomId,user.get(),role);
    }

    public Boolean add(Long classroomId, String username, Role role) {
        Optional<User> user = userRepo.findByUsername(username);
        if(user.isEmpty()){
            return false;
        }
        return add(classroomId,user.get(),role);
    }

    public Boolean add(Long classroomId, User userTobeAdded, Role role) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalUser = userRepo.findByUsername(principal.getUsername());
            if(optionalUser.isEmpty()){
                return false;
            }
            User user = optionalUser.get();
            Optional<UserClassroomRole> optionalUserClassroomRole
                    = userClassroomRoleRepo.findByUser_UserIdAndClassroom_ClassroomId(user.getUserId(),classroomId);
            if(optionalUserClassroomRole.isEmpty()){
                return false;
            }
            Optional<Classroom> optionalClassroom = classroomRepo.findById(classroomId);
            if(optionalClassroom.isEmpty()){
                return false;
            }
            Classroom classroom = optionalClassroom.get();
            UserClassroomRole userClassroomRole = optionalUserClassroomRole.get();
            Role roleOfUser = userClassroomRole.getRole();
            if(roleOfUser == Role.CR || roleOfUser == Role.Teacher){
                if(role==null){
                    role = Role.Student;
                }
            }else{
                role=Role.Pending;
            }
            UserClassroomRole userClassroomRoleTobeAdded
                    = new UserClassroomRole(userTobeAdded,role,classroom);
            classroom.getUserClassroomRoleList().add(userClassroomRoleTobeAdded);
            userTobeAdded.getUserClassroomRoles().add(userClassroomRoleTobeAdded);
            userClassroomRoleRepo.save(userClassroomRole);
            return true;
        }
        return false;
    }


    public Boolean assign(Long classroomId, Long userId, Role role) {
        Optional<User> user = userRepo.findById(userId);
        if(user.isEmpty()){
            return false;
        }
        return assign(classroomId,user.get(),role);
    }

    public Boolean remove(Long classroomId, String username) {
        Optional<User> user = userRepo.findByUsername(username);
        if(user.isEmpty()){
            return false;
        }
        return remove(classroomId,user.get());
    }

    public Boolean remove(Long classroomId, Long userId) {
        Optional<User> user = userRepo.findById(userId);
        if(user.isEmpty()){
            return false;
        }
        return remove(classroomId,user.get());
    }


    public Boolean remove(Long classroomId,User userTobeAssigned){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalUser = userRepo.findByUsername(principal.getUsername());
            if (optionalUser.isEmpty()) {
                return false;
            }
            User user = optionalUser.get();
            Optional<UserClassroomRole> optionalCurrentUserClassroomRole
                    = userClassroomRoleRepo.findByUser_UserIdAndClassroom_ClassroomId(user.getUserId(), classroomId);
            if (optionalCurrentUserClassroomRole.isEmpty()) {
                return false;
            }
            Optional<Classroom> optionalClassroom = classroomRepo.findById(classroomId);
            if(optionalClassroom.isEmpty()){
                return false;
            }
            Classroom classroom = optionalClassroom.get();;
            Role currentRole = optionalCurrentUserClassroomRole.get().getRole();
            Optional<UserClassroomRole> optionalUserClassroomRole
                    = userClassroomRoleRepo
                    .findByUser_UserIdAndClassroom_ClassroomId(userTobeAssigned.getUserId(), classroomId);
            if (optionalUserClassroomRole.isEmpty()) {
                return true;
            }
            UserClassroomRole userClassroomRole = optionalUserClassroomRole.get();
            userClassroomRole.setClassroom(null);
            classroom.getUserClassroomRoleList().remove(userClassroomRole);
            user.getUserClassroomRoles().remove(userClassroomRole);
            userClassroomRole.setUser(null);
            userClassroomRoleRepo.save(userClassroomRole);
            return true;
        }
        return false;
    }


    public Boolean assign(Long classroomId, String username, Role role) {
        Optional<User> user = userRepo.findByUsername(username);
        if(user.isEmpty()){
            return false;
        }
        return assign(classroomId,user.get(),role);
    }

    public Boolean assign(Long classroomId,User userTobeAssigned,Role role){
        if(role==Role.Pending){
            return false;
        }
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalUser = userRepo.findByUsername(principal.getUsername());
            if (optionalUser.isEmpty()) {
                return false;
            }
            User user = optionalUser.get();
            Optional<UserClassroomRole> optionalCurrentUserClassroomRole
                    = userClassroomRoleRepo.findByUser_UserIdAndClassroom_ClassroomId(user.getUserId(), classroomId);
            if (optionalCurrentUserClassroomRole.isEmpty()) {
                return false;
            }
            Optional<Classroom> optionalClassroom = classroomRepo.findById(classroomId);
            if(optionalClassroom.isEmpty()){
                return false;
            }
            Classroom classroom = optionalClassroom.get();;
            Role currentRole = optionalCurrentUserClassroomRole.get().getRole();
            Optional<UserClassroomRole> optionalUserClassroomRole
                    = userClassroomRoleRepo
                    .findByUser_UserIdAndClassroom_ClassroomId(userTobeAssigned.getUserId(), classroomId);
            UserClassroomRole userClassroomRole;
            if (optionalUserClassroomRole.isEmpty()) {
                userClassroomRole = new UserClassroomRole();
                userClassroomRole.setClassroom(classroom);
                classroom.getUserClassroomRoleList().add(userClassroomRole);
                userClassroomRole.setUser(userTobeAssigned);
                userTobeAssigned.getUserClassroomRoles().add(userClassroomRole);
            }else {
                userClassroomRole = optionalUserClassroomRole.get();
            }
            if(currentRole==Role.CR || currentRole==Role.Teacher){
                userClassroomRole.setRole(role);
            }else{
                userClassroomRole.setRole(Role.Pending);
            }
            userClassroomRoleRepo.save(userClassroomRole);
            return true;
        }
        return false;
    }

    public List<ClassroomDto> all() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalUser = userRepo.findByUsername(principal.getUsername());
            if (optionalUser.isEmpty()) {
                return new ArrayList<>();
            }
            User user = optionalUser.get();
            return classroomRepo.findByUserId(user.getUserId());
        }
        return new ArrayList<>();
    }


    public List<ClassroomDto> all(String keyword) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalUser = userRepo.findByUsername(principal.getUsername());
            if (optionalUser.isEmpty()) {
                return new ArrayList<>();
            }
            User user = optionalUser.get();
            return classroomRepo.findByUserIdAndKeyword(user.getUserId(),keyword);
        }
        return new ArrayList<>();
    }

    public List<SessionDto> sessions(Long classroomId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalUser = userRepo.findByUsername(principal.getUsername());
            if (optionalUser.isEmpty()) {
                return new ArrayList<>();
            }
            User user = optionalUser.get();
            Optional<UserClassroomRole> optionalCurrentUserClassroomRole
                    = userClassroomRoleRepo.findByUser_UserIdAndClassroom_ClassroomId(user.getUserId(), classroomId);
            if (optionalCurrentUserClassroomRole.isEmpty()) {
                return new ArrayList<>();
            }
            Optional<Classroom> optionalClassroom = classroomRepo.findById(classroomId);
            if(optionalClassroom.isEmpty()){
                return new ArrayList<>();
            }
            Role role = optionalCurrentUserClassroomRole.get().getRole();
            List<SessionDto> res;
            if(role == Role.Teacher){
                res = sessionRepo.findByClassroomId(classroomId);
            }else{
                res = sessionRepo.findByClassroomId(classroomId,false);
            }
            if(res!=null){
                return res;
            }
        }
        return new ArrayList<>();
    }

    public List<UserClassroomRoleDto> roles(Long classroomId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalUser = userRepo.findByUsername(principal.getUsername());
            if (optionalUser.isEmpty()) {
                return new ArrayList<>();
            }
            User user = optionalUser.get();
            Optional<Classroom> optionalClassroom = classroomRepo.findById(classroomId);
            if(optionalClassroom.isEmpty()){
                return new ArrayList<>();
            }
            List<UserClassroomRoleDto> userClassroomRoles
                    = userClassroomRoleRepo.findByClassroom_ClassroomId(classroomId);
            if(userClassroomRoles!=null){
                return userClassroomRoles;
            }
        }
        return new ArrayList<>();
    }

    public Role role(Long classroomId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalUser = userRepo.findByUsername(principal.getUsername());
            if (optionalUser.isEmpty()) {
                return null;
            }
            User user = optionalUser.get();
            Optional<UserClassroomRole> optionalUserClassroomRole
                    = userClassroomRoleRepo.findByUser_UserIdAndClassroom_ClassroomId(user.getUserId(), classroomId);
            if (optionalUserClassroomRole.isEmpty()) {
                return null;
            }
            UserClassroomRole userClassroomRole = optionalUserClassroomRole.get();
            return userClassroomRole.getRole();
        }
        return null;
    }

    public Boolean edit(Long classroomId, Classroom classroom) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalUser = userRepo.findByUsername(principal.getUsername());
            if (optionalUser.isEmpty()) {
                return false;
            }
            User user = optionalUser.get();
            Optional<UserClassroomRole> optionalUserClassroomRole
                    = userClassroomRoleRepo.findByUser_UserIdAndClassroom_ClassroomId(user.getUserId(), classroomId);
            if (optionalUserClassroomRole.isEmpty()) {
                return false;
            }
            UserClassroomRole userClassroomRole = optionalUserClassroomRole.get();
            userClassroomRole.getClassroom().setNextSessionTime(classroom.getNextSessionTime());
            userClassroomRole.getClassroom().setDetails(classroom.getDetails());
            userClassroomRoleRepo.save(userClassroomRole);
            return true;
        }
        return false;
    }
}

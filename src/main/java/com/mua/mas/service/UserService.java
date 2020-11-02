package com.mua.mas.service;

import com.mua.mas.model.User;
import com.mua.mas.model.UserPrincipal;
import com.mua.mas.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public Boolean edit(String name,String email){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalUser = userRepo.findByUsername(principal.getUsername());
            if(optionalUser.isEmpty()){
                return false;
            }
            User user = optionalUser.get();
            if(name!=null)
                user.setName(name);
            if(email!=null)
                user.setEmail(email);
            userRepo.save(user);
            return true;
        }
        return false;
    }

    public Boolean addContact(String contact) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalUser = userRepo.findByUsername(principal.getUsername());
            if(optionalUser.isEmpty()){
                return false;
            }
            User user = optionalUser.get();
            if(user.getContacts().contains(contact)){
                return false;
            }
            user.getContacts().add(contact);
            userRepo.save(user);
            return true;
        }
        return false;
    }

    public Boolean removeContact(String contact) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalUser = userRepo.findByUsername(principal.getUsername());
            if(optionalUser.isEmpty()){
                return false;
            }
            User user = optionalUser.get();
            if(!user.getContacts().contains(contact)){
                return false;
            }
            user.getContacts().remove(contact);
            userRepo.save(user);
            return true;
        }
        return false;
    }

    public User details() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalUser = userRepo.findByUsername(principal.getUsername());
            if(optionalUser.isEmpty()){
                return null;
            }
            User user = optionalUser.get();
            user.setUserClassroomRoles(null);
            user.setLoginCredential(null);
            user.setAttendances(null);
            return user;
        }
        return null;
    }

    public User getOne(Long userId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            Optional<User> optionalUser = userRepo.findById(userId);
            if(optionalUser.isEmpty()){
                return null;
            }
            User user = optionalUser.get();
            user.setUserClassroomRoles(null);
            user.setLoginCredential(null);
            user.setAttendances(null);
            return user;
        }
        return null;
    }

    public Boolean isSameUser(Long userId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            Optional<User> optionalUser = userRepo.findByUsername(principal.getUsername());
            if (optionalUser.isEmpty()) {
                return false;
            }
            Optional<User> optionalUserTarget = userRepo.findById(userId);
            if(optionalUserTarget.isEmpty()){
                return false;
            }
            User userTarget = optionalUserTarget.get();
            User user = optionalUser.get();
            return user.getUserId().equals(userTarget.getUserId());
        }
        return false;
    }

}

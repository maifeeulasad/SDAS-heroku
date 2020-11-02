package com.mua.mas.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TestService {

    public String time(Boolean auth){
        if(!auth){
            return new Date().toString();
        }
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            return new Date().toString();
        }
        return "";
    }

    public Boolean auth() {
        try{
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return authentication.isAuthenticated();
        }catch (Exception e){
            return false;
        }
    }
}

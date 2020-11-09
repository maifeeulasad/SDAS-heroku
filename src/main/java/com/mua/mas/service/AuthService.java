package com.mua.mas.service;

import com.mua.mas.model.LoginCredential;
import com.mua.mas.model.User;
import com.mua.mas.property.Properties;
import com.mua.mas.repo.LoginCredentialRepo;
import com.mua.mas.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    @Autowired
    private LoginCredentialRepo loginCredentialRepo;
    @Autowired
    private JwtTokenProvider tokenProvider;

    public Boolean join(String username,String password){
        List<LoginCredential> loginCredentials
                = loginCredentialRepo.findByUsername(username);
        if(loginCredentials==null || loginCredentials.size()==0){
            LoginCredential loginCredential = new LoginCredential(username,password);
            User user = new User();
            user.setLoginCredential(loginCredential);
            user.setUsername(username);
            user.setName(username);
            loginCredential.setUser(user);
            //System.out.println("username" +user.getUsername());
            //System.out.println("lc username" +loginCredential.getUser().getUsername());
            //user.setLoginCredential(loginCredential);
            loginCredentialRepo.save(loginCredential);
            return true;
        }
        return false;
    }

    public Boolean check(String username) {
        List<LoginCredential> loginCredentials
                = loginCredentialRepo.findByUsername(username);
        return loginCredentials==null || loginCredentials.size()==0;
    }

    public LoginCredential login(String username, String password) {
        List<LoginCredential> loginCredentials = loginCredentialRepo.findByUsernameAndPassword(username,password);
        if(loginCredentials!=null && loginCredentials.size()!=0){
            String jwt = tokenProvider.generateToken(username);
            LoginCredential loginCredential = new LoginCredential();
            loginCredential.setKey(Properties.tokenPrefix + jwt);
            return loginCredential;
        }
        return null;
    }
}

package com.mua.mas.repo;

import com.mua.mas.model.LoginCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginCredentialRepo extends JpaRepository<LoginCredential,Long> {
    List<LoginCredential> findByUsername(String username);
    List<LoginCredential> findByUsernameAndPassword(String username,String password);
}

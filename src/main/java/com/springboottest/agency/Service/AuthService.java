package com.springboottest.agency.Service;

// import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboottest.agency.Entity.UserEntity;
import com.springboottest.agency.Repository.UserRepo;
import com.springboottest.agency.Security.JwtHelper;

import jakarta.annotation.PostConstruct;

@Service
public class AuthService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtHelper helper;

    public ResponseEntity<Map<String, Object>> loginGenerateToken(UserEntity loginDetail) {

    // sanitize username
    String userName = loginDetail.getUsername() != null? loginDetail.getUsername().trim().toLowerCase(): "";
    UserEntity loginInfo = userRepo.findByUsername(userName);

    // ========== CHECK USERNAME ==========
    if (loginInfo == null) {
        return ResponseClass.responseFailure("Invalid User Name");
    }

    // ========== CHECK PASSWORD ==========
    if (!passwordEncoder.matches(loginDetail.getPassword(), loginInfo.getPassword())) {
        return ResponseClass.responseFailure("Invalid Password");
    }

    // ========== GENERATE TOKEN ==========
    String token = helper.generateToken(loginInfo);
    Map<String,Object> response = new HashMap<>();
    response.put("status", 200);
    response.put("success", true);
    response.put("message", "Login Successful");
    response.put("token", token);
    // return (ResponseEntity<Map<String, Object>>) response;
    return ResponseEntity.ok(response);
}

    @PostConstruct
public void createTestUser() {
    if (userRepo.findByUsername("deepak") == null) {
        UserEntity user = new UserEntity();
        user.setUsername("deepak");
        user.setPassword(passwordEncoder.encode("deepak123"));
        user.setRole("ADMIN");
        userRepo.save(user);
    }



}

    


    
}

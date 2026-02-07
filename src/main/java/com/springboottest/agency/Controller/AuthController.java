package com.springboottest.agency.Controller;

import com.springboottest.agency.Entity.UserEntity;
import com.springboottest.agency.Security.JwtHelper;
import com.springboottest.agency.Service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

// import java.util.HashMap;
// import java.util.Map;

@RestController
@RequestMapping("/auth")
//@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    // @Autowired
    // private AuthenticationManager authenticationManager; 

    // @Autowired
    // private JwtHelper jwtHelper;

    @Autowired
    private AuthService authService;

    // ✅ LOGIN API
    @PostMapping("/login")
        public ResponseEntity<?> loginUser(@RequestBody UserEntity loginDetail) {
            System.out.println("🔥 AUTH CONTROLLER HIT 🔥");
            return authService.loginGenerateToken(loginDetail);  
}


    

}

package com.springboottest.agency.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboottest.agency.Entity.UserEntity;
import com.springboottest.agency.Service.UserService;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService ;

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserEntity userEntity) {
    return userService.addUser(userEntity);
 }

 // ✅ GET USERS (THIS WAS MISSING)
    @GetMapping("/all")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

}

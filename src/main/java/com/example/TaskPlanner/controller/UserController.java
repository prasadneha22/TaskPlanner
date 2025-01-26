package com.example.TaskPlanner.controller;

import com.example.TaskPlanner.DTO.LoginDto;
import com.example.TaskPlanner.Service.UserService;
import com.example.TaskPlanner.entity.Users;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController()
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Users> register(@RequestBody Users user){
        Users registerUser = userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerUser);

    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody LoginDto loginDto){
        Map<String,Object> response = userService.verify(loginDto);
        if(response.containsKey("Error")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.ok(response);
    }


}

package com.alp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alp.entities.User;
import com.alp.service.UserService;


@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
	
	@Autowired
    private UserService service;
 
	 @PostMapping("/registration")
	 public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
	        service.registerUser(user);
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "User registered successfully!");
	        return ResponseEntity.ok(response);
	 }
	 
    @GetMapping("/all")
    public List<User> getAllRegistrations() {
        return service.getAllRegistrations();
    }
    
    
    
//    @PostMapping("/login")
//    public ResponseEntity<Map<String, String>> login(@RequestBody User loginRequest) {
//        boolean isAuthenticated = service.login(loginRequest.getUsername(), loginRequest.getPassword());
//        Map<String, String> response = new HashMap<>();
//        if (isAuthenticated) {
//            response.put("message", "Login successful!");
//            return ResponseEntity.ok(response);
//        } else {
//            response.put("message", "Invalid username or password!");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//        }
//    }
    
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        return service.login(username, password);
    }
    
}

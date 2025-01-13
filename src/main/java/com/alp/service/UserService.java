package com.alp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.alp.entities.User;
import com.alp.repository.UserRepository;


@Service
public class UserService {
	
	@Autowired
    private UserRepository userRepository;
	
	//private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	//private BCryptPasswordEncoder passwordEncoder;

	
    public User registerUser(User user) {
    	//user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    public List<User> getAllRegistrations() {
        return userRepository.findAll();
    }
    
//    public boolean login(String username, String password) {
//    	
//        Optional<User> userOptional = userRepository.findByUsername(username);
//        
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            return password.equals(user.getPassword());
//        }
//        
//        return false;
//    }
    
    public Map<String, String> login(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        Map<String, String> response = new HashMap<>();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (password.equals(user.getPassword())) {
                response.put("message", "Login successful!");
                response.put("username", user.getUsername());
                return response;
            }
        }
        response.put("message", "Invalid credentials");
        return response;
    }
}

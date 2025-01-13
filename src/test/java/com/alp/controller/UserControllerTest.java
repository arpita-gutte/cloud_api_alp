package com.alp.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.alp.controller.UserController;
import com.alp.entities.User;
import com.alp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
 
class UserControllerTest {
 
	private MockMvc mockMvc;
	
    @InjectMocks
    private UserController userController;
 
    @Mock
    private UserService userService;
 
    public UserControllerTest() {
        MockitoAnnotations.openMocks(this); 
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }
    
    @Test
    void testLoginSuccess() {
        
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "testUser");
        loginRequest.put("password", "testPassword123");
 
        Map<String, String> mockResponse = new HashMap<>();
        mockResponse.put("message", "Login successful!");
        mockResponse.put("username", "testUser");
 
        when(userService.login("testUser", "testPassword123")).thenReturn(mockResponse);
 
        Map<String, String> response = userController.login(loginRequest);
 
        assertEquals("Login successful!", response.get("message"));
        assertEquals("testUser", response.get("username"));
    }
 
    @Test
    void testLoginFailure() {
 
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", "invalidUser");
        loginRequest.put("password", "wrongPassword");
 
        Map<String, String> mockResponse = new HashMap<>();
        mockResponse.put("message", "Invalid credentials");
 
        when(userService.login("invalidUser", "wrongPassword")).thenReturn(mockResponse);
 
        Map<String, String> response = userController.login(loginRequest);
 
        assertEquals("Invalid credentials", response.get("message"));
    }
    
	@Test
	public void testRegisteredUser() throws Exception{
		User user=new User();
		user.setEmail("test@example.com");
		user.setUsername("testUser");
		user.setPassword("pass123");
		
		//doNothing().when(userService).registerUser(Mockito.any(User.class));
		
		when(userService.registerUser(Mockito.any(User.class))).thenReturn(user);
		
		ObjectMapper objectMapper=new ObjectMapper();
		String userJson=objectMapper.writeValueAsString(user);
 
		
		mockMvc.perform(post("/users/registration").contentType(MediaType.APPLICATION_JSON).content(userJson)).andExpect(status().isOk()).andExpect(content().json("{\"message\":\"User registered successfully!\"}"));
		
		verify(userService,times(1)).registerUser(Mockito.any(User.class));
	}
    
}

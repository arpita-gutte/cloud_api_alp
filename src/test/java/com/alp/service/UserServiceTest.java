package com.alp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.alp.entities.User;
import com.alp.repository.UserRepository;
import com.alp.service.UserService;

public class UserServiceTest {
	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private UserRepository userRepository;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	public UserServiceTest() {
		MockitoAnnotations.openMocks(this); //mock initialization
	}
	
	@Test
	void testLoginSuccess() {
		User user = new User();
		user.setUsername("testUser");
		user.setPassword("testPassword123");
		
		when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
		
		Map<String, String> response = userService.login("testUser", "testPassword123");
		
		assertEquals("Login successful!", response.get("message"));
		assertEquals("testUser", response.get("username"));
	}
	
	@Test
	void testLoginFailure() {
		when(userRepository.findByUsername("invalidUser")).thenReturn(Optional.empty());
		
		Map<String, String> response = userService.login("invalidUser", "wrongPassword");
		
		assertEquals("Invalid credentials", response.get("message"));
	}
	
	@Test
	public void testRegisterUser() {
		User user=new User();
		user.setEmail("test@example.com");
		user.setUsername("testUser");
		user.setPassword("pass123");
		
		when(userRepository.save(user)).thenReturn(user);
		
		User registeredUser=userService.registerUser(user);
		
		assertNotNull(registeredUser);
		assertEquals("test@example.com",registeredUser.getEmail());
		assertEquals("testUser",registeredUser.getUsername());
		
		verify(userRepository,times(1)).save(user);
	}
}

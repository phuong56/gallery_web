package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public Optional<User> getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	
	public void saveUser(User user) {
		userRepository.save(user);
	}
	
	public User authenticateUser(String username, String password) {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isPresent() && user.get().getPassword().equals(password)) {
			return user.get();
		}
		return null;
	}
	
	public User registerUser(User user) {
		return userRepository.save(user);
	}
	
	public Boolean isUserAlreadyExist(String username, String email) {
		return userRepository.findByUsername(username).isPresent() || userRepository.findByEmail(email).isPresent();
	}
	
	
	
}

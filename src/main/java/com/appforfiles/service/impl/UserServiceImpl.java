package com.appforfiles.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.appforfiles.model.User;
import com.appforfiles.repository.UserRepository;
import com.appforfiles.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public List<User> getAllUsers() {
		return (List<User>) userRepository.findAll();
	}

	@Override
	public User getUserById(Integer id) {
		return userRepository.getOne(id);
	}

	@Override
	public User getUserByUsernameAndPassword(String username, String password) {
		return userRepository.getUserByUsernameAndPassword(username, password);
	}

	@Override
	public boolean isUserByUsernameAndPassword(String username, String password) {
		if (userRepository.getUserByUsernameAndPassword(username, password) != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void saveOrUpdate(User user) {
		userRepository.save(user);
	}

	@Override
	public void deleteById(Integer id) {
		userRepository.deleteById(id);
	}

	@Override
	public void delete(User user) {
		userRepository.delete(user);
	}

	@Override
	public User getUserByUsername(String username) {
		return userRepository.getUserByUsername(username);
	}

}

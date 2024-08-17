package com.leaseleap.userservice.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leaseleap.userservice.model.PatchUserRequest;
import com.leaseleap.userservice.model.Users;
import com.leaseleap.userservice.repository.UserRepository;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepo;
	
	public void createUser(Users user) {
		userRepo.save(user);
	}

	public List<Users> listUsers() {
		return userRepo.findAll();		
	}

	public Users validateUser(Long userId) {
		return userRepo.findById(userId).orElse(null);	
	}
	
	public Users updateUser(Long userId, PatchUserRequest patchUserRequest) {
		Users user = userRepo.findById(userId).orElseThrow();
		updateDetails(user, patchUserRequest);
		return user;		
	}	

	public void deleteUser(Long userId) {		
		if(userId <= 0L) return;
		userRepo.deleteById(userId);
	}
	
	private void updateDetails(Users user, PatchUserRequest patchUserRequest) {
		if(Objects.nonNull(patchUserRequest) && Objects.nonNull(patchUserRequest.getFirstName())) {
			user.setFirstName(patchUserRequest.getFirstName());
		}
		if(Objects.nonNull(patchUserRequest) && Objects.nonNull(patchUserRequest.getLastName())) {
			user.setLastName(patchUserRequest.getLastName());
		}
		if(Objects.nonNull(patchUserRequest) && Objects.nonNull(patchUserRequest.getEmail())) {
			user.setEmail(patchUserRequest.getEmail());
		}
		if(Objects.nonNull(patchUserRequest) && Objects.nonNull(patchUserRequest.getMobileNumber())) {
			user.setMobileNumber(patchUserRequest.getMobileNumber());
		}
		userRepo.save(user);
	}

}

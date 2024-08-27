package com.leaseleap.userservice.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.leaseleap.userservice.exception.InvalidUserIdException;
import com.leaseleap.userservice.exception.UserCreationException;
import com.leaseleap.userservice.exception.UserNotFoundException;
import com.leaseleap.userservice.exception.UserUpdateFailedException;
import com.leaseleap.userservice.model.LoginUserRequest;
import com.leaseleap.userservice.model.PatchUserRequest;
import com.leaseleap.userservice.model.Users;
import com.leaseleap.userservice.repository.UserRepository;
import com.leaseleap.userservice.util.PasswordUtil;
import com.leaseleap.userservice.util.UserConstants;
import jakarta.transaction.Transactional;


@Service
@Transactional
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepo;
	
	public Users createUser(Users user) {
		String encodedPassword = PasswordUtil.encode(user.getPassword());
		user.setPassword(encodedPassword);
		Users savedUser = userRepo.save(user);
		if(Objects.isNull(savedUser)) {
			throw new UserCreationException("Cannot create the user with username: "+user.getUsername());
		}
		return savedUser;
	}

	public List<Users> listUsers() {
		List<Users> userList = userRepo.findAll();
		if(Objects.isNull(userList) || userList.isEmpty()) {
			throw new UserNotFoundException("No users found");
		}
		return userList;
	}

	public Users validateUser(Long userId) {
		Users validatedUser = userRepo.findById(userId).orElse(null);
		if(Objects.isNull(validatedUser)) {
			throw new UserNotFoundException("Cannot find the user with user ID: "+userId);
		}
		return validatedUser;	
	}
	
	public Users validateUserByUsername(String username) {
		Users validatedUser = userRepo.findByUsername(username);
		if(Objects.isNull(validatedUser)) {
			throw new UserNotFoundException("Cannot find a user with username: "+username);
		}
		return validatedUser;
	}
	
	public Users updateUser(Long userId, PatchUserRequest patchUserRequest) {
		Users user = userRepo.findById(userId).orElseThrow();
		user = updateDetails(user, patchUserRequest);
		if(Objects.isNull(user)) {
			throw new UserUpdateFailedException("Update failed for the user with username: "+ user.getUsername());
		}
		return user;
	}	

	public void deleteUser(Long userId) {		
		if(userId <= 0L) {
			throw new InvalidUserIdException("Invalid User ID: "+userId);
		}
		userRepo.deleteById(userId);
	}
	
	private Users updateDetails(Users user, PatchUserRequest patchUserRequest) {
		if(Objects.nonNull(patchUserRequest) && Objects.nonNull(patchUserRequest.getFirstName())) {
			user.setFirstName(patchUserRequest.getFirstName());
		}
		if(Objects.nonNull(patchUserRequest) && Objects.nonNull(patchUserRequest.getLastName())) {
			user.setLastName(patchUserRequest.getLastName());
		}
		if(Objects.nonNull(patchUserRequest) && Objects.nonNull(patchUserRequest.getPassword())) {
			user.setPassword(PasswordUtil.encode(patchUserRequest.getPassword()));
		}
		if(Objects.nonNull(patchUserRequest) && Objects.nonNull(patchUserRequest.getEmail())) {
			user.setEmail(patchUserRequest.getEmail());
		}
		if(Objects.nonNull(patchUserRequest) && Objects.nonNull(patchUserRequest.getMobileNumber())) {
			user.setMobileNumber(patchUserRequest.getMobileNumber());
		}
		return userRepo.save(user);
	}

	public boolean login(LoginUserRequest loginUser) {
		Users validatedUser = validateUserByUsername(loginUser.getUsername());
		if(Objects.nonNull(validatedUser)) {			
			return PasswordUtil.matches(loginUser.getPassword(), validatedUser.getPassword());			
		} else {
			throw new UsernameNotFoundException("Cannot find a user with username: "+loginUser.getUsername());
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userRepo.findByUsername(username);
		if(Objects.isNull(user)) {
			throw new UsernameNotFoundException("Cannot find the username: "+username);
		}
		return new User(user.getUsername(),
				user.getPassword(), Collections.singletonList(
						new SimpleGrantedAuthority(UserConstants.USER)));
	}	

}

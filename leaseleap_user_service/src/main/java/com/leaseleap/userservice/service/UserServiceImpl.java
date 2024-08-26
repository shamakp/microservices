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
	
	public void createUser(Users user) {
		String encodedPassword = PasswordUtil.encode(user.getPassword());
		user.setPassword(encodedPassword);
		userRepo.save(user);
	}

	public List<Users> listUsers() {
		return userRepo.findAll();		
	}

	public Users validateUser(Long userId) {
		return userRepo.findById(userId).orElse(null);	
	}
	
	public Users validateUserByUsername(String username) {
		Users validatedUser = userRepo.findByUsername(username);
		return Objects.nonNull(validatedUser) ? validatedUser : null;
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
		if(Objects.nonNull(patchUserRequest) && Objects.nonNull(patchUserRequest.getPassword())) {
			user.setPassword(PasswordUtil.encode(patchUserRequest.getPassword()));
		}
		if(Objects.nonNull(patchUserRequest) && Objects.nonNull(patchUserRequest.getEmail())) {
			user.setEmail(patchUserRequest.getEmail());
		}
		if(Objects.nonNull(patchUserRequest) && Objects.nonNull(patchUserRequest.getMobileNumber())) {
			user.setMobileNumber(patchUserRequest.getMobileNumber());
		}
		userRepo.save(user);
	}

	public boolean login(LoginUserRequest loginUser) {
		Users validatedUser = validateUserByUsername(loginUser.getUsername());
		if(Objects.nonNull(validatedUser)) {
			return PasswordUtil.matches(
					PasswordUtil.encode(loginUser.getPassword()), validatedUser.getPassword());			
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

package com.leaseleap.userservice.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import com.leaseleap.userservice.model.LoginUserRequest;
import com.leaseleap.userservice.model.PatchUserRequest;
import com.leaseleap.userservice.model.Users;


public interface UserService extends UserDetailsService {
	
	public void createUser(Users user);

	public List<Users> listUsers();

	public Users validateUser(Long userId);
	
	public Users updateUser(Long userId, PatchUserRequest patchUserRequest);
	
	public void deleteUser(Long userId);
	
	public Users validateUserByUsername(String username);
	
	public boolean login(LoginUserRequest loginUser);


}
